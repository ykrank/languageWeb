(function( this )
	ebank_public.allSuit:physicalkey_back();
	-- 获取验证码的标识
	-- local flag = nil;

	-- 手机or交易密码验证标识
	local flag = 1;
	-- 验证序列号

	local req_tbl = ert.channel:get_request("ebank_password_manager","FGPD0002");
	local customerName = req_tbl["customerName"];
    local IDNO = req_tbl["IDNO"];

    local resp_json = ert.channel:get_response("ebank_password_manager","FGPD0002")
    local json_tbl = json:objectFromJSON(resp_json) or {};
    local t_body = json_tbl["body"] or {};
	local eAccountNo = t_body["eAccountNo"];
	
	function check(num)
		this.num=num;
		flag = num;
		--获取的tb
		local tb1 = document:getElementsByName("tb1");
		local tb2 = document:getElementsByName("tb2");
		if num == 1 then 
			ert("#select1"):css("color","#FF5A5F");
            ert("#select2"):css("color","#999999");

            ert("#select1_line"):css("display","block");
            ert("#select2_line"):css("display","none");

            ert("#select1"):attr("enable","false");
            ert("#select2"):attr("enable","true");

			tb2[1]:setStyleByName("display","none");
			tb1[1]:setStyleByName("display","block");
			location:reload();
		else
			ert("#select2"):css("color","#FF5A5F");
            ert("#select1"):css("color","#999999");

            ert("#select2_line"):css("display","block");
            ert("#select1_line"):css("display","none");

            ert("#select2"):attr("enable","false");
            ert("#select1"):attr("enable","true");

			tb1[1]:setStyleByName("display","none");
			tb2[1]:setStyleByName("display","block");
			location:reload();
		end;
	end;

    function click()
    	--window:alert(eAccountNo);
    	local post_body = nil;
    	local next_channelId = "ebank_password_manager";
	    local next_trancode = "FGPD0003";
	    --------------
		local password1 = ert("#new_password1"):attr("value");
	    this.password1 = password1;
	    local password2 = ert("#new_password2"):attr("value");
	    this.password2 = password2;
	    if password1 == "" then
			 window:alert("新登录密码不能为空！");
			 return
		end;
		if string.len(password1) < 6 then
			window:alert("新登录密码不能小于6位");
			return
		end;
		if string.len(password1) > 16 then
			window:alert("新登录密码不能多于16位");
			return
		end;
		-- if string.match(password1,[[%a]]) ==nil then
		-- 	 window:alert("登录密码必须包含字母");
		-- 	 return;
		-- else
		-- 	if string.match(password1,[[%g]])==nil or string.match(password1,[[%d]])==nil then
		-- 		window:alert("登录密码不能只含有字母");
		-- 		return;
		-- 	end
		-- end;
		 

	    ----------------密码校验---------------------------------------
		if string.match(password1,[[%s]])==nil and string.match(password1,'[·¥£€]')==nil then
		  if string.match(password1,[[%w]])==nil or string.match(password1,[[%A]])==nil or string.match(password1,[[%D]])==nil then

		      window:alert("登录密码必须由数字、字母、符号任意二种组合");
		      return;
		  end
		else
		  window:alert("登录密码不能有空格或\'·¥£€\'这4个字符");
		  return;
		end
  -------------------------------------------------------
  		if password2 =="" then
  			window:alert("确认登录密码不能为空");
  			return
  		end
		if password2 ~= password1 then
			window:alert("两次输入的密码不一样");
			return
		end;
		---------------加密--------------------
			password1 = encrypt:rsa(password1);
			password2 = encrypt:rsa(password2);
		---------------------------------------
		if flag == 1 then
		   local MOBILE = ert("#MOBILE"):attr("value");
		   local OTPNO = ert("#node"):attr("value");
		   if OTPNO == "" then
			 window:alert("验证码不能为空！");
			 return
			end;

			if string.len(OTPNO) ~= 6 then
				window:alert("验证码位数不对");
				return
			end;
			if string.find(OTPNO,[[%D]])~= nil and string.find(OTPNO,"[%D]")~= 0 then
				window:alert("验证码只能输入数字！");
				return
			end
		   post_body = {id=next_channelId,tranCode=next_trancode,customerName=customerName,IDNO=IDNO,PSWD1=password1,MOBILE=MOBILE,OTPNO=OTPNO,flag=flag};
		else
			-------------eAccountNo---------------------------
			local tranPassword = ert("#tranPassword"):attr("value");
			if string.len(tranPassword) ~= 6 then
			 window:alert("交易密码位数不对！");
			 return
			end;
			----------------------------------
			if string.find(tranPassword,[[%D]])~= nil and string.find(tranPassword,"[%D]")~= 0 then
				window:alert("交易密码只能输入数字！");
				return
			end
			tranPassword = encrypt:desRsa(tranPassword);
			-----------------------------------
			post_body = {id=next_channelId,tranCode=next_trancode,customerName=customerName,IDNO=IDNO,PSWD1=password1,tranPassword=tranPassword,eAccountNo=eAccountNo,flag=flag};
		end
	     	ert.channel:next_page(next_channelId, next_trancode, post_body);
    end;
end)(ert.channel:get_page("ebank_password_manager","FGPD0002"));
