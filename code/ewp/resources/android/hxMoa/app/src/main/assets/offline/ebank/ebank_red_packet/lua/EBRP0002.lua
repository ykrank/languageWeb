(function(this)

	--红包提现提交
	function withdraw_result()
	    local tranPassword = ert("#tran_password"):val();
	    local req_value = ert.channel:get_request("ebank_red_packet","EBRP0002");
	    
	    --校验是否输入交易密码
	    if  string.len(tranPassword) ~= 6 then
	    	window:alert("请输入6位数字交易密码");
	    	return;
	    end

	    --校验0元不能提现
	    --if req_value ==

	    ert.channel:next_page("ebank_red_packet","EBRP0003",{id = "ebank_red_packet",tranCode = "EBRP0003",tranPassword=encrypt:desRsa(tranPassword),amountIn=req_value["WithdrawAmount"]});
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EBRP0002"));