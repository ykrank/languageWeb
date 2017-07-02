%%
%% %CopyrightBegin%
%%
%% Copyright Ericsson AB 1999-2014. All Rights Reserved.
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
{application, crypto,
   [{description, "CRYPTO"},
    {vsn, "3.3"},
    {modules, [crypto,
	       crypto_ec_curves]},
    {registered, []},
    {applications, [kernel, stdlib]},
    {env, []},
    {runtime_dependencies, ["erts-6.0","stdlib-2.0","kernel-3.0"]}]}.

