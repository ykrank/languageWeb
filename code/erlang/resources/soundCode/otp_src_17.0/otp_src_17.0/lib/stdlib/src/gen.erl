%%
%% %CopyrightBegin%
%%
%% Copyright Ericsson AB 1996-2014. All Rights Reserved.
%%
%% The contents of this file are subject to the Erlang Public License,
%% Version 1.1, (the "License"); you may not use this file except in
%% compliance with the License. You should have received a copy of the
%% Erlang Public License along with this software. If not, it can be
%% retrieved online at http://www.erlang.org/.
%%
%% Software distributed under the License is distributed on an "AS IS"
%% basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
%% the License for the specific language governing rights and limitations
%% under the License.
%%
%% %CopyrightEnd%
%%
-module(gen).
-compile({inline,[get_node/1]}).

%%%-----------------------------------------------------------------
%%% This module implements the really generic stuff of the generic
%%% standard behaviours (e.g. gen_server, gen_fsm).
%%%
%%% The standard behaviour should export init_it/6.
%%%-----------------------------------------------------------------
-export([start/5, start/6, debug_options/1,
	 call/3, call/4, reply/2]).

-export([init_it/6, init_it/7]).

-export([format_status_header/2]).

-define(default_timeout, 5000).

%%-----------------------------------------------------------------

-type linkage()    :: 'link' | 'nolink'.
-type emgr_name()  :: {'local', atom()}
                    | {'global', term()}
                    | {'via', Module :: module(), Name :: term()}.

-type start_ret()  :: {'ok', pid()} | 'ignore' | {'error', term()}.

-type debug_flag() :: 'trace' | 'log' | 'statistics' | 'debug'
                    | {'logfile', string()}.
-type option()     :: {'timeout', timeout()}
		    | {'debug', [debug_flag()]}
		    | {'spawn_opt', [proc_lib:spawn_option()]}.
-type options()    :: [option()].

%%-----------------------------------------------------------------
%% Starts a generic process.
%% start(GenMod, LinkP, Mod, Args, Options)
%% start(GenMod, LinkP, Name, Mod, Args, Options)
%%    GenMod = atom(), callback module implementing the 'real' fsm
%%    LinkP = link | nolink
%%    Name = {local, atom()} | {global, term()} | {via, atom(), term()}
%%    Args = term(), init arguments (to Mod:init/1)
%%    Options = [{timeout, Timeout} | {debug, [Flag]} | {spawn_opt, OptionList}]
%%      Flag = trace | log | {logfile, File} | statistics | debug
%%          (debug == log && statistics)
%% Returns: {ok, Pid} | ignore |{error, Reason} |
%%          {error, {already_started, Pid}} |
%%    The 'already_started' is returned only if Name is given 
%%-----------------------------------------------------------------

-spec start(module(), linkage(), emgr_name(), module(), term(), options()) ->
	start_ret().

start(GenMod, LinkP, Name, Mod, Args, Options) ->
    case where(Name) of
	undefined ->
	    do_spawn(GenMod, LinkP, Name, Mod, Args, Options);
	Pid ->
	    {error, {already_started, Pid}}
    end.

-spec start(module(), linkage(), module(), term(), options()) -> start_ret().

start(GenMod, LinkP, Mod, Args, Options) ->
    do_spawn(GenMod, LinkP, Mod, Args, Options).

%%-----------------------------------------------------------------
%% Spawn the process (and link) maybe at another node.
%% If spawn without link, set parent to ourselves 'self'!!!
%%-----------------------------------------------------------------
do_spawn(GenMod, link, Mod, Args, Options) ->
    Time = timeout(Options),
    proc_lib:start_link(?MODULE, init_it,
			[GenMod, self(), self(), Mod, Args, Options], 
			Time,
			spawn_opts(Options));
do_spawn(GenMod, _, Mod, Args, Options) ->
    Time = timeout(Options),
    proc_lib:start(?MODULE, init_it,
		   [GenMod, self(), self, Mod, Args, Options], 
		   Time,
		   spawn_opts(Options)).

do_spawn(GenMod, link, Name, Mod, Args, Options) ->
    Time = timeout(Options),
    proc_lib:start_link(?MODULE, init_it,
			[GenMod, self(), self(), Name, Mod, Args, Options],
			Time,
			spawn_opts(Options));
do_spawn(GenMod, _, Name, Mod, Args, Options) ->
    Time = timeout(Options),
    proc_lib:start(?MODULE, init_it,
		   [GenMod, self(), self, Name, Mod, Args, Options], 
		   Time,
		   spawn_opts(Options)).

%%-----------------------------------------------------------------
%% Initiate the new process.
%% Register the name using the Rfunc function
%% Calls the Mod:init/Args function.
%% Finally an acknowledge is sent to Parent and the main
%% loop is entered.
%%-----------------------------------------------------------------
init_it(GenMod, Starter, Parent, Mod, Args, Options) ->
    init_it2(GenMod, Starter, Parent, self(), Mod, Args, Options).

init_it(GenMod, Starter, Parent, Name, Mod, Args, Options) ->
    case name_register(Name) of
	true ->
	    init_it2(GenMod, Starter, Parent, Name, Mod, Args, Options);
	{false, Pid} ->
	    proc_lib:init_ack(Starter, {error, {already_started, Pid}})
    end.

init_it2(GenMod, Starter, Parent, Name, Mod, Args, Options) ->
    GenMod:init_it(Starter, Parent, Name, Mod, Args, Options).

%%-----------------------------------------------------------------
%% Makes a synchronous call to a generic process.
%% Request is sent to the Pid, and the response must be
%% {Tag, _, Reply}.
%%-----------------------------------------------------------------

%%% New call function which uses the new monitor BIF
%%% call(ServerId, Label, Request)

call(Process, Label, Request) -> 
    call(Process, Label, Request, ?default_timeout).

%% Local or remote by pid
call(Pid, Label, Request, Timeout) 
  when is_pid(Pid), Timeout =:= infinity;
       is_pid(Pid), is_integer(Timeout), Timeout >= 0 ->
    do_call(Pid, Label, Request, Timeout);
%% Local by name
call(Name, Label, Request, Timeout) 
  when is_atom(Name), Timeout =:= infinity;
       is_atom(Name), is_integer(Timeout), Timeout >= 0 ->
    case whereis(Name) of
	Pid when is_pid(Pid) ->
	    do_call(Pid, Label, Request, Timeout);
	undefined ->
	    exit(noproc)
    end;
%% Global by name
call(Process, Label, Request, Timeout)
  when ((tuple_size(Process) == 2 andalso element(1, Process) == global)
	orelse
	  (tuple_size(Process) == 3 andalso element(1, Process) == via))
       andalso
       (Timeout =:= infinity orelse (is_integer(Timeout) andalso Timeout >= 0)) ->
    case where(Process) of
	Pid when is_pid(Pid) ->
	    Node = node(Pid),
 	    try do_call(Pid, Label, Request, Timeout)
 	    catch
 		exit:{nodedown, Node} ->
 		    %% A nodedown not yet detected by global,
 		    %% pretend that it was.
 		    exit(noproc)
	    end;
	undefined ->
	    exit(noproc)
    end;
%% Local by name in disguise
call({Name, Node}, Label, Request, Timeout)
  when Node =:= node(), Timeout =:= infinity;
       Node =:= node(), is_integer(Timeout), Timeout >= 0 ->
    call(Name, Label, Request, Timeout);
%% Remote by name
call({_Name, Node}=Process, Label, Request, Timeout)
  when is_atom(Node), Timeout =:= infinity;
       is_atom(Node), is_integer(Timeout), Timeout >= 0 ->
    if
 	node() =:= nonode@nohost ->
 	    exit({nodedown, Node});
 	true ->
 	    do_call(Process, Label, Request, Timeout)
    end.

do_call(Process, Label, Request, Timeout) ->
    try erlang:monitor(process, Process) of
	Mref ->
	    %% If the monitor/2 call failed to set up a connection to a
	    %% remote node, we don't want the '!' operator to attempt
	    %% to set up the connection again. (If the monitor/2 call
	    %% failed due to an expired timeout, '!' too would probably
	    %% have to wait for the timeout to expire.) Therefore,
	    %% use erlang:send/3 with the 'noconnect' option so that it
	    %% will fail immediately if there is no connection to the
	    %% remote node.

	    catch erlang:send(Process, {Label, {self(), Mref}, Request},
		  [noconnect]),
	    receive
		{Mref, Reply} ->
		    erlang:demonitor(Mref, [flush]),
		    {ok, Reply};
		{'DOWN', Mref, _, _, noconnection} ->
		    Node = get_node(Process),
		    exit({nodedown, Node});
		{'DOWN', Mref, _, _, Reason} ->
		    exit(Reason)
	    after Timeout ->
		    erlang:demonitor(Mref, [flush]),
		    exit(timeout)
	    end
    catch
	error:_ ->
	    %% Node (C/Java?) is not supporting the monitor.
	    %% The other possible case -- this node is not distributed
	    %% -- should have been handled earlier.
	    %% Do the best possible with monitor_node/2.
	    %% This code may hang indefinitely if the Process 
	    %% does not exist. It is only used for featureweak remote nodes.
	    Node = get_node(Process),
	    monitor_node(Node, true),
	    receive
		{nodedown, Node} -> 
		    monitor_node(Node, false),
		    exit({nodedown, Node})
	    after 0 -> 
		    Tag = make_ref(),
		    Process ! {Label, {self(), Tag}, Request},
		    wait_resp(Node, Tag, Timeout)
	    end
    end.

get_node(Process) ->
    %% We trust the arguments to be correct, i.e
    %% Process is either a local or remote pid,
    %% or a {Name, Node} tuple (of atoms) and in this
    %% case this node (node()) _is_ distributed and Node =/= node().
    case Process of
	{_S, N} when is_atom(N) ->
	    N;
	_ when is_pid(Process) ->
	    node(Process)
    end.

wait_resp(Node, Tag, Timeout) ->
    receive
	{Tag, Reply} ->
	    monitor_node(Node, false),
	    {ok, Reply};
	{nodedown, Node} ->
	    monitor_node(Node, false),
	    exit({nodedown, Node})
    after Timeout ->
	    monitor_node(Node, false),
	    exit(timeout)
    end.

%%
%% Send a reply to the client.
%%
reply({To, Tag}, Reply) ->
    Msg = {Tag, Reply},
    try To ! Msg catch _:_ -> Msg end.

%%%-----------------------------------------------------------------
%%%  Misc. functions.
%%%-----------------------------------------------------------------
where({global, Name}) -> global:whereis_name(Name);
where({via, Module, Name}) -> Module:whereis_name(Name);
where({local, Name})  -> whereis(Name).

name_register({local, Name} = LN) ->
    try register(Name, self()) of
	true -> true
    catch
	error:_ ->
	    {false, where(LN)}
    end;
name_register({global, Name} = GN) ->
    case global:register_name(Name, self()) of
	yes -> true;
	no -> {false, where(GN)}
    end;
name_register({via, Module, Name} = GN) ->
    case Module:register_name(Name, self()) of
	yes ->
	    true;
	no ->
	    {false, where(GN)}
    end.


timeout(Options) ->
    case opt(timeout, Options) of
	{ok, Time} ->
	    Time;
	_ ->
	    infinity
    end.

spawn_opts(Options) ->
    case opt(spawn_opt, Options) of
	{ok, Opts} ->
	    Opts;
	_ ->
	    []
    end.

opt(Op, [{Op, Value}|_]) ->
    {ok, Value};
opt(Op, [_|Options]) ->
    opt(Op, Options);
opt(_, []) ->
    false.

debug_options(Opts) ->
    case opt(debug, Opts) of
	{ok, Options} -> sys:debug_options(Options);
	_ -> []
    end.

format_status_header(TagLine, Pid) when is_pid(Pid) ->
    lists:concat([TagLine, " ", pid_to_list(Pid)]);
format_status_header(TagLine, RegName) when is_atom(RegName) ->
    lists:concat([TagLine, " ", RegName]);
format_status_header(TagLine, Name) ->
    {TagLine, Name}.
