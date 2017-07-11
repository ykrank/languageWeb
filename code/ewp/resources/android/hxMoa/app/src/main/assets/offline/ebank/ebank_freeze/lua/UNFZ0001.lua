(function(this)
	ebank_public.allSuit:physicalkey_back();

    function click()
    
    	local next_channelId = "ebank_freeze";
		local next_trancode = "FREEZE03";
		local eAccountNo = ert("#eAccountNo"):val();
		local MOBILE = ert("#MOBILE"):val();
		local OTPNO = ert("#node"):val();
		--window:alert(OTPNO)
			if OTPNO == "" then
	           window:alert("验证码不能为空！");
	           return
	        end;
	        if string.len(OTPNO) ~= 6 then
	          window:alert("请输入6位验证码");
	          return
	        end;
	        if string.find(OTPNO,"[%D]")~= nil and string.find(OTPNO,"[%D]")~= 0 then
	          window:alert("验证码只能输入数字！");
	          return
	        end
		local tranPassword = ert("#tranPassword"):val();
			if tranPassword == "" then
				window:alert("交易密码不能为空！");
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
			tranPassword = encrypt:desRsa(tranPassword);

			local post_body = {id=next_channelId,tranCode=next_trancode,eAccountNo=eAccountNo,MOBILE=MOBILE,OTPNO=OTPNO,tranPassword=tranPassword};
		ert.channel:next_page(next_channelId, next_trancode, post_body);
		--ert.channel:next_page(next_channelId, "FREEZE02", post_body);
    end
end)(ert.channel:get_page("ebank_freeze","UNFZ0001"));
