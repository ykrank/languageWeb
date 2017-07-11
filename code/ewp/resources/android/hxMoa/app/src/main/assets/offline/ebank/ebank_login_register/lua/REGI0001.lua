(function(this)
 
 --获取注册验证json
      function this.register_callback(response)
          local code = response["responseCode"];
          if code == 200 then
              local json_data = response["responseBody"];
              local json_tbl = json:objectFromJSON(json_data) or {};
              json_tbl["body"]=json_tbl["body"] or {};
              --local error_num = json_tbl["header"]["error_num"];
              --local error_msg = json_tbl["header"]["error_msg"];
              local checkType = json_tbl["body"]["checkType"];
              local mobile = json_tbl["body"]["mobile"];
              local eAccountNo = json_tbl["body"]["eAccountNo"];
              local custno = json_tbl["body"]["custno"];
              local last_request_infor = ert.channel:get_request("ebank_login_register","REGI0002");
              local username = last_request_infor["username"];
              local cardId = last_request_infor["cardId"];
              if checkType == "4" then  --已有E账号
                  window:alert("该客户已注册投融资平台，请直接登录", "确定",function(index)
                        if index == 0 then
                            ert.channel:next_page('ebank_login_register','LOGIN001',{},{just_page=true})
                        end
                    end);
              else
                  local context = {mobile=mobile,checkType=checkType,username=username,cardId=cardId,eAccountNo=eAccountNo,custno=custno};
                  ert.channel:next_page('ebank_login_register','REGI0002',{},{just_page=true,context=context})
              end;
          else
              window:alert("网络请求失败");
              return;
          end
      end

--注册下一步
     function this.next_page()
          local userName = ert("#username"):val();
          local cardId = ert("#cardId"):val();
          if userName == "" then
             window:alert("请输入姓名");
             return;
          end
          if cardId == "" then
             window:alert("请输入身份证号码");
             return;
          end
          if input_check_chinese(cardId) == 0 then
              return;
          end
          if string.len(cardId) ~= 18 then
             window:alert("身份证号码不能小于18位");
             return;
          end
          if string.match(cardId,[[%s]]) ~= nil then
             window:alert("身份证号码不能有空格");
             return;
          end
          local id1 = string.sub(cardId, 1, 17);
          local id2 = string.sub(cardId, 18, 18);
          local _, count = string.gsub(id1, "%d", "");
          if count < 17  then
              window:alert("身份证号码前17位不允许出现特殊字符或字母");
              return;
          end
          if string.match(id2,[[%d]]) == nil and id2 ~= "X" and id2 ~= "x" then
              window:alert("身份证号码最后一位只允许出现数字或者X");
              return;
          end

          if certificate_verify(cardId) == 0 then
              return;
          end
          if id2 == "x" then
            cardId = id1.."X"
          end

          ert.channel:next_page("ebank_login_register","REGI0002",{id='ebank_login_register',tranCode='REGI0002',username=userName,cardId=cardId},{request_callback=this.register_callback});
      end

      ebank_public.allSuit:physicalkey_back();

end)(ert.channel:get_page("ebank_login_register","REGI0001"));
