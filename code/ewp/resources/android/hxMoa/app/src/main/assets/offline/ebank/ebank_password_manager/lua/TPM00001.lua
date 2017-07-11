(function( this )
	ebank_public.allSuit:physicalkey_back();
	-- 标识
	local flag = 0;
	-- 获取验证码的标识
	--local flag = nil;

	--检查身份证--
	function check_OTPNO( OTPNO )
        if OTPNO == "" then
           window:alert("验证码不能为空！");
           return 0
        end;

        if string.len(OTPNO) ~= 6 then
          window:alert("验证码位数不对");
          return 0
        end;
        if string.find(OTPNO,[[%D]])~= nil and string.find(OTPNO,"[%D]")~= 0 then
          window:alert("验证码只能输入数字！");
          return 0
        end
        return 1;
    end

    function input_check_IDNO(IDNO)
    	if string.len(IDNO)~=18 then
    		window:alert("您输入的身份证位数不正确，请重新输入");
    		return
    	end	
		local _, n = IDNO:gsub('[\128-\225]', '')
		if n > 0 then
			 window:alert("证件号码不能输入中文");
	 		return 1;
        end;
        return 0;
    end;

	function switch()
		local switch = document:getElementsByName("switch1");
		local checked = switch[1]:getPropertyByName("checked");

		local tb1 = document:getElementsByName("tb1");
		local tb2 = document:getElementsByName("tb2");

		if checked == "true" then
			tb1[1]:setStyleByName("display","block");
			tb2[1]:setStyleByName("display","none");
			--location:reload();
			flag = 1;
		else
			tb1[1]:setStyleByName("display","none");
			tb2[1]:setStyleByName("display","block");
			--location:reload();
			flag = 0;
		end;
		location:reload();
	end

    function click()
    local post_body = nil;
    local next_channelId = "ebank_password_manager";
    local next_trancode = "TPM00002";

	local tranPassword = ert("#old_password"):attr("value");
    this.tranPassword = tranPassword;
	local New_tranPassword1 = ert("#new_password1"):attr("value");
    this.New_tranPassword1 = New_tranPassword1;
    local New_tranPassword2 = ert("#new_password2"):attr("value");
    this.New_tranPassword2 = New_tranPassword2;
    
    if flag == 1 then

    local customerName = ert("#customerName"):val();
    local IDNO = ert("#IDNO"):val();
	local MOBILE = ert("#MOBILE"):attr("value");
	local OTPNO = ert("#node"):attr("value");
	   if customerName =="" then
		   	window:alert("请输入姓名！");
		   	return
	   	end;
	   	if IDNO =="" then
	   		window:alert("请输入身份证！");
	   		return
	   	elseif input_check_IDNO(IDNO)~=0 then

	   		return
	   	end;
	   	if check_OTPNO(OTPNO) ~=1 then
	   		return
   		end
   		----------------------------------
	if New_tranPassword1 == "" then
		window:alert("新交易密码不能为空");
		return
	end
	if string.len(New_tranPassword1) ~= 6 then
		 window:alert("新交易密码只能输入6位数字");
		 return
	end;
	if string.find(New_tranPassword1,[[%D]])~= nil and string.find(New_tranPassword1,"[%D]")~= 0 then
		window:alert("新交易密码只能输入6位数字");
		return
	end
	----------------------------------
	if New_tranPassword2 =="" then
  			window:alert("确认交易密码不能为空");
  			return
  	end

	if New_tranPassword2 ~= New_tranPassword1 then
		 window:alert("两次输入的新交易密码不一致");
		 return
	end;
	--if New_tranPassword1 ~= "" and New_tranPassword2 ~= "" then
		New_tranPassword1 = encrypt:desRsa(New_tranPassword1)
		New_tranPassword2 = encrypt:desRsa(New_tranPassword2)
	--end
	----------------------------------
	   post_body = {id=next_channelId,tranCode=next_trancode,customerName=customerName,IDNO=IDNO,MOBILE=MOBILE,OTPNO=OTPNO,New_tranPassword1=New_tranPassword1,flag=flag};
	   
	else 
		if tranPassword == "" then
			window:alert("交易密码不能为空");
			return
		end

		if string.len(tranPassword) ~= 6 then
		 window:alert("交易密码只能输入6位数字！");
		 return
		end;
		if string.find(tranPassword,[[%D]])~= nil and string.find(tranPassword,"[%D]")~= 0 then
			window:alert("交易密码只能输入6位数字！");
			return
		end
		
		----------------------------------
	if New_tranPassword1 == "" then
		window:alert("新交易密码不能为空");
		return
	end
	if string.len(New_tranPassword1) ~= 6 then
		 window:alert("新交易密码只能输入6位数字");
		 return
	end;
	if string.find(New_tranPassword1,[[%D]])~= nil and string.find(New_tranPassword1,"[%D]")~= 0 then
		window:alert("新交易密码只能输入6位数字");
		return
	end
	----------------------------------
	if New_tranPassword2 =="" then
		window:alert("确认交易密码不能为空");
		return
  	end

	if New_tranPassword2 ~= New_tranPassword1 then
		 window:alert("两次输入的新交易密码不一致");
		 return
	end;
	if tranPassword == New_tranPassword2 then
		window:alert("新交易密码与原交易密码是一样的");
		return
	end
		tranPassword = encrypt:desRsa(tranPassword)
	--if New_tranPassword1 ~= "" and New_tranPassword2 ~= "" then
		New_tranPassword1 = encrypt:desRsa(New_tranPassword1)
		New_tranPassword2 = encrypt:desRsa(New_tranPassword2)
	--end
	----------------------------------
		post_body = {id=next_channelId,tranCode=next_trancode,tranPassword=tranPassword,New_tranPassword1=New_tranPassword1,flag=flag};
	end
        ert.channel:next_page(next_channelId, next_trancode, post_body);
    end;
end)(ert.channel:get_page("ebank_password_manager","TPM00001"));
