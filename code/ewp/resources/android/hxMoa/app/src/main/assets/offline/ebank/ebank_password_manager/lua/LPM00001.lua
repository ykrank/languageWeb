(function(this)
  ebank_public.allSuit:physicalkey_back();
    -- 获取验证码的标识
    local flag = nil;
 
    function check_OTPNO( OTPNO )
        if OTPNO == "" then
           window:alert("请输入6位短信验证码");
           return 1;
        end;

        if string.len(OTPNO) ~= 6 then
          window:alert("请输入6位短信验证码");
          return 2;
        end;
        if string.find(OTPNO,[[%D]])~= nil and string.find(OTPNO,"[%D]")~= 0 then
          window:alert("验证码只能输入数字");
          return 3;
        end
        return 0;
    end

    function click()
      
    local MOBILE = ert("#MOBILE"):attr("value");
    local OTPNO = ert("#node"):attr("value");
    if check_OTPNO( OTPNO ) ~= 0 then
      return
    end

    local password_1 = ert("#password_modify1"):attr("value");
    this.password_1 = password_1;
    local password_2 = ert("#password_modify2"):attr("value");
    this.password_2 = password_2;
    local password_3 = ert("#password_modify3"):attr("value");
    this.password_3 = password_3;

    if password_1 == "" then
     window:alert("原密码不能为空");
     return;
    end;

    if string.len(password_1) < 6 then
     window:alert("原密码不能小于6位");
     return;
    end;
    if string.len(password_1) > 16 then
        window:alert("原登录密码不能多于16位");
        return
    end 

    if password_2 == ""  then
     window:alert("新密码不能为空");
     return;
    end;
    if string.len(password_2) < 6 then
     window:alert("新密码不能小于6位");
     return;
    end;
    
    if string.len(password_2) > 16 then
        window:alert("新密码不能多于16位");
        return
    end
----------------密码校验---------------------------------------
      if string.match(password_2,[[%s]])==nil and string.match(password_2,'[·¥£€]')==nil then
            if string.match(password_2,[[%w]])==nil or string.match(password_2,[[%A]])==nil or string.match(password_2,[[%D]])==nil then

                window:alert("登录密码必须由数字、字母、符号任意二种组合");
                return;
            end
      else
        window:alert("登录密码不能有空格或\'·¥£€\'这4个字符");
        return;
      end
  -------------------------------------------------------
    if password_3 =="" then
        window:alert("确认登录密码不能为空");
        return
    end

    if password_3 ~= password_2 then
     window:alert("两次输入的新密码不一样");
     return;
    end;
    
     password_1 = encrypt:rsa(password_1);
     password_2 = encrypt:rsa(password_2);
     password_3 = encrypt:rsa(password_3);

    local channelId = "ebank_password_manager";
    local trancode = "LPM00002";
    local post_body = {id=channelId,tranCode=trancode,MOBILE=MOBILE,OTPNO=OTPNO,PSWD=password_1,NEW_PSWD=password_2};
    ert.channel:next_page(channelId,trancode,post_body);
    end;  
     
end)(ert.channel:get_page("ebank_password_manager","LPM00001"));
