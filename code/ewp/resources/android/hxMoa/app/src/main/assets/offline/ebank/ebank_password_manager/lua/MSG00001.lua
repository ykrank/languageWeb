(function(this)
    ebank_public.allSuit:physicalkey_back();
    local info = "";
      function click()
        local msg = ert("#msg"):val();
        local next_channelId = "ebank_password_manager";
        local next_trancode = "MSG00002";
        if msg =="" then
        	window:alert("请输入预留信息");
        	return
        end;
        if string.len(msg) > 30  then
        	window:alert("预留信息最多只能输入10个汉字");
        	return
        end;
        info = msg;
        local post_body = {id=next_channelId,tranCode=next_trancode,msg=msg};
        ert.channel:next_page(next_channelId, next_trancode, post_body,{request_callback=msg_callback});
    end;

    function msg_callback(response)
        local code = response["responseCode"];
        if code == 200 then
            local json_data = response["responseBody"];
            local json_tbl = json:objectFromJSON(json_data);
            local flag = json_tbl["body"]["flag"];
            if flag == "1" then
                obligate = info;
                local next_channelId = "ebank_product_recommend";
                local next_trancode = "LH0001";
                local post_body = {id=next_channelId,tranCode=next_trancode};
                ert.channel:first_page(next_channelId, next_trancode, post_body);
            end
        end
    end

end)(ert.channel:get_page("login_password_modify","MSG00001"));
