(function(this)
     
    local mobile = this.context["mobile"];
    
    function change_value()
        local moblie_fomat = ert("#MOBILE"):val();
        if string.sub(moblie_fomat, 1, 1) == "+" or string.sub(moblie_fomat, 1, 1) == "-" then
            ert("#MOBILE"):attr("value","");
        end
    end

--注册信息填写下一步
      function this.next_page()
          local loginPwd_old = ert("#lg_pwd"):val();
          if loginPwd_old == "" then
             window:alert("请输入原登录密码");
             return;
          end
          if string.len(loginPwd_old) < 6 then
             window:alert("原登录密码不小于6位");
             return;
          end

          local loginPwd = ert("#new_lg_pwd"):val();
          if loginPwd == "" then
             window:alert("请设置新登录密码");
             return;
          end
          if string.len(loginPwd) < 6 then
             window:alert("新登录密码不能小于6位");
             return;
          end
          
          if string.match(loginPwd,[[%s]])==nil and string.match(loginPwd,'[·¥£€]')==nil then
              if string.match(loginPwd,[[%w]])==nil or string.match(loginPwd,[[%A]])==nil or string.match(loginPwd,[[%D]])==nil then

                  window:alert("新登录密码必须由数字、字母、符号任意二种组合");
                  return;

              end
          else
              window:alert("新登录密码不能有空格或\'·¥£€\'这4个字符");
              return;
          end

          local loginPwd2 = ert("#new_lg_pwd2"):val();
          if loginPwd2 == "" then
             window:alert("请确认新登录密码");
             return;
          end
          if loginPwd ~= loginPwd2 then
             window:alert("新登录密码与确认新登录密码必须保持一致");
             return;
          end
          loginPwd_old = encrypt:rsa(loginPwd_old);
          loginPwd = encrypt:rsa(loginPwd);
          loginPwd2 = encrypt:rsa(loginPwd2);
          local QUESTION1 = ert("#QUESTION1"):val();
          if QUESTION1 == "" then
             window:alert("请输入私密问题1");
             return;
          end
          local ANSWER1 = ert("#ANSWER1"):val();
          if ANSWER1 == "" then
             window:alert("请输入私密答案1");
             return;
          end
          local QUESTION2 = ert("#QUESTION2"):val();
          if QUESTION2 == "" then
             window:alert("请输入私密问题2");
             return;
          end
          local ANSWER2 = ert("#ANSWER2"):val();
          if ANSWER2 == "" then
             window:alert("请输入私密答案2");
             return;
          end
          
          local USERID = ert("#USERID"):val();
          
          local LABEL = ert("#LABEL"):val();

          local securityCode = ert("#node"):val();
          if securityCode == "" then
             window:alert("请输入短信验证码");
             return;
          end
          if string.len(securityCode) < 6 then
             window:alert("短信验证码不能小于6位");
             return;
          end

          if phone==nil then
              window:alert("请点击获取短信按钮获取短信");
              return;
          end
          
          obligate = LABEL;
          local post_body={id='ebank_login_register',tranCode='REGI0005',PASSWORD=loginPwd_old,NEWPASSWORD=loginPwd,QUESTION1=QUESTION1,ANSWER1=ANSWER1,QUESTION2=QUESTION2,ANSWER2=ANSWER2,USERID=USERID,LABEL=LABEL,mobile=mobile,securityCode=securityCode}
      
          ert.channel:next_page("ebank_login_register","REGI0005",post_body,{});
      end

      ebank_public.allSuit:physicalkey_exit_back();
      
end)(ert.channel:get_page("ebank_login_register","REGI0013"));
