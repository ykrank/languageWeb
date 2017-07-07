(function(this)
	
--解绑银行卡
      function this.unlock_page(bank,account,mobile)
          local context={bank=bank,account=account,mobile=mobile};
          ert.channel:next_page('ebank_tied_card','UNLOCK01',{},{just_page=true,context=context})
      end

--人脸识别
      local code = "";
      local face = "";
      function startFaceCallback(arg)
          if string.len(arg["data"]) > 100 then
            face=arg["data"];
             -- _,_,face=string.find(arg["data"],"<face>(.+)</face>");
          else
              code = arg["code"];
          end
          
          if arg["data"]=="人脸识别成功！" or arg["data"]=="活体检测成功" then 
              local post_body={id='ebank_tied_card',tranCode='FACE0001',code=code,face=face};
              ert.channel:next_page('ebank_tied_card','FACE0001',post_body,{})
          elseif string.len(arg["data"]) < 100 then
              window:alert("人脸识别失败！");
              return;
          end
          
      end
          
      function startFace()
          native:openFace(startFaceCallback);
      end;

--添加银行卡
      function this.bound_page(card_num)
          -- local card_num = card_num;
          -- if tonumber(card_num)>0 then
          --     startFace();
          -- else
          --     ert.channel:next_page('ebank_tied_card','BOUND001',{id='ebank_tied_card',tranCode='BOUND001'},{})
          -- end
          ert.channel:next_page('ebank_tied_card','BOUND001',{id='ebank_tied_card',tranCode='BOUND001'},{})
      end

      function this.next_homePage()
          ert.channel:show_loading();
          ert.channel:finish();
      end

---------------------------------------------------------------------------
    local data=this:get_data();
    local table=json:objectFromJSON(data) or {};
    table["body"]=table["body"] or {};
    local account_list = table["body"]["list"] or {};
    local mobile = table["body"]["mobile"];
    local card_num = table["body"]["num"];
    local list_tr = "";

    function init(account_list,mobile,card_num)
      list_tr="";
      -- for key,v in pairs(account_list) do
        
      -- end
    end

     -- init();
---------------------------------------------------------------------------



      window:setPhysicalkeyListener("backspace", this.next_homePage);

end)(ert.channel:get_page("ebank_tied_card","TCM00001"));