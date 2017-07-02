-module(net_test).
-export([createid/0,t/0]).

get() ->

 inets:start(),
 Url = "http://www.baidu.com",
 {ok,Result} = httpc:request(get,{Url,[]},[],[]),
 inets:stop(),
 Result.


post() ->
    inets:start(),
%case httpc:request(post,{"http://172.16.52.19:4003",[],"application/x-www-form-urlencoded",lists:concat(["MB1002.do"])},[],[]) of
case httpc:request(post,{"http://172.16.52.19:4003/MB1002.do",[],"application/x-www-form-urlencoded;charset=utf-8",[]},[],[]) of
    {ok,{_,_,Body}} ->
        file:write_file("t.txt",[Body]),
        Body;
    {error,Reason} ->
    io:format("error ,...... ~p~n",[Reason])
end.