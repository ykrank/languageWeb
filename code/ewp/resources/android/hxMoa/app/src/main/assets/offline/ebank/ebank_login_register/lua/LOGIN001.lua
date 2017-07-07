(function(this)

--选择
      local checked_flag = 1;
      function this.checked(value)
         checked_flag = value;
         if checked_flag == 1 then
            ert("#select1"):css("color","#FF5A5F");
            ert("#select2"):css("color","#999999");

            ert("#select1_line"):css("display","block");
            ert("#select2_line"):css("display","none");

            ert("#select1"):attr("enable","false");
            ert("#select2"):attr("enable","true");

            ert("#userId1"):css("display","block");
            ert("#userId2"):css("display","none");
            location:reload();
         else
            ert("#select2"):css("color","#FF5A5F");
            ert("#select1"):css("color","#999999");

            ert("#select2_line"):css("display","block");
            ert("#select1_line"):css("display","none");

            ert("#select2"):attr("enable","false");
            ert("#select1"):attr("enable","true");
            
            ert("#userId2"):css("display","block");
            ert("#userId1"):css("display","none");
            location:reload();
         end
      end

      function this.return_home()
          ert.channel:first_page("ebank_product_recommend","LH0001",{id="ebank_product_recommend",tranCode="LH0001"},{});
      end

      local last_page = ert.channel:get_page("ebank_login_register","LOGIN001") or {};
      
--校验登录密码
      local function check_password(password)
          if string.len(password) < 6 then
             window:alert("登录密码不能小于6位");
             return;
          else
             return 1;
          end
      end

--获取登录json，进行判断
      function this.login_callback(response)
          local code = response["responseCode"];
          if code == 200 then
              local json_data = response["responseBody"];
              local json_tbl = json:objectFromJSON(json_data) or {};
              json_tbl["body"]=json_tbl["body"] or {};
              --local error_num = json_tbl["header"]["error_num"];
              --local error_msg = json_tbl["header"]["error_msg"];
              local flag = json_tbl["body"]["flag"];
              username_login = json_tbl["body"]["username_login"];
              obligate = json_tbl["body"]["obligate"];
              local cardId = json_tbl["body"]["cardId"];
              local custno = json_tbl["body"]["custno"];
              --firstloginstate:0-非首次登陆 1-首次登陆
              local firstloginstate = json_tbl["body"]["firstloginstate"];

              if flag == "1" then  --有卡有网银没有E账号，进入注册页面完善信息注册

                  if firstloginstate=="1" then  --有卡有网银没有E账号,网银首次登陆，去注册和设置安全信息
                      local mobile = json_tbl["body"]["mobile"];
                      local context = {mobile=mobile,username=username_login,cardId=cardId,custno=custno};
                      ert.channel:next_page('ebank_login_register','REGI0012',{},{just_page=true,context=context})
                  else  --有卡有网银没有E账号,网银非首次登陆,去注册
                      local mobile = json_tbl["body"]["mobile"];
                      local checkType = "2";
                      local context = {mobile=mobile,checkType=checkType,username=username_login,cardId=cardId,custno=custno};
                      ert.channel:next_page('ebank_login_register','REGI0002',{},{just_page=true,context=context})
                  end

              elseif flag == "2" then  --有卡有网银有E账号

                  if firstloginstate=="1" then  --有E账户，首次登陆,去设置安全信息

                      local mobile = json_tbl["body"]["mobile"];
                      local context = {mobile=mobile};
                      ert.channel:next_page('ebank_login_register','REGI0013',{},{just_page=true,context=context})

                  else  --有E账号，进入首页
                      is_login = "1";
                      local bindcard_flag = json_tbl["body"]["bindcard_flag"];--是否绑卡：1、没绑卡；0、已绑卡
                      last_page.context=last_page.context or {};
                      local id = last_page.context["id"];
                      local trancode = last_page.context["trancode"];
                      local post_body = last_page.context["post_body"];
                      if id == "" or id == nil then
                          ert.channel:first_page("ebank_product_recommend", "LH0001", {id="ebank_product_recommend",tranCode="LH0001"},{});
                      else
                          if bindcard_flag == "1" and (trancode=="EMAS0001" or trancode=="PD0002" or trancode=="PX0002") then
                              ert.channel:first_page("ebank_product_recommend", "LH0001", {id="ebank_product_recommend",tranCode="LH0001"},{});
                          else
                              if  trancode == "MANU0001" then
                                  ert.channel:first_page(id, trancode,{},{just_page=true});
                              else              
                                  ert.channel:first_page(id, trancode,post_body,{});
                              end
                              
                          end
                      end;
                  end

              else
                  return;
              end
          else
              window:alert("网络请求失败");
              return;
          end
      end

--登录
      function this.login()
          local loginType;
          local userId = ert("#userId"):val();
          local password = ert("#password"):val();
          if userId == "" then
             window:alert("请输入用户名");
             return;
          end
          if password == "" then
             window:alert("请输入密码");
             return;
          end
          local _, n = userId:gsub('[\128-\225]', '')
          if n > 0 then
             window:alert("用户名或密码错误");
             return;
          end;

          if string.len(userId) == 11 and string.sub(userId,1,1) == "1" then
              loginType = 5;
          elseif string.len(userId) == 18 then
              loginType = 2;
          elseif string.len(userId) == 19 then
              loginType = 3;
          elseif string.len(userId)>=6 and string.len(userId)<=14 then
              loginType = 1;
          else
              window:alert("用户名或密码错误");
              return;
          end
          if check_password(password) == 1 then
            password = encrypt:rsa(password);
              ert.channel:next_page("ebank_login_register","LOGIN001",{id='ebank_login_register',tranCode='LOGIN001',loginType=loginType,userId=userId,password=password},{request_callback=this.login_callback});
          end
      end
      ert(".btn_lg0001_next_page"):click(this.login);
      -- function this.login()
      --     local loginType;
      --     local userId;
      --     if checked_flag == 1 then
      --         userId = ert("#userId1"):val();
      --     else
      --         userId = ert("#userId2"):val();
      --     end
      --     local password = ert("#password"):val();
      --     if userId == "" then
      --        window:alert("请输入用户名");
      --        return;
      --     end
      --     if password == "" then
      --        window:alert("请输入密码");
      --        return;
      --     end

      --     if checked_flag == 1 then
      --         if string.len(userId) == 11 then
      --             loginType = 5;
      --         elseif string.len(userId) == 18 then
      --             loginType = 2;
      --         else
      --             loginType = 3;
      --         end
      --     else
      --         loginType = 1;
      --     end
      --     if check_password(password) == 1 then
      --         ert.channel:next_page("ebank_login_register","LOGIN001",{id='ebank_login_register',tranCode='LOGIN001',loginType=loginType,userId=userId,password=password},{request_callback=this.login_callback});
      --     end
      -- end
      -- ert(".btn_lg0001_next_page"):click(this.login);

--E账户注册
      function this.next_page_1()
          ert.channel:next_page('ebank_login_register','REGI0001',{},{just_page=true})
      end;
      ert(".btn_eregi_next_page"):click(this.next_page_1);

--忘记密码
      function this.next_page_2()
          ert.channel:first_page('ebank_password_manager','FGPD0001',{},{just_page=true})
      end;
      ert(".btn_wjmima_next_page"):click(this.next_page_2);

      window:setPhysicalkeyListener("backspace", this.return_home);

end)(ert.channel:get_page("ebank_login_register","LOGIN001"));
