(function(this)
	--资金转入转出
	function next_money_into(signValue,AccBalance,CustName)
	    finsh_continue_bug_flag = "1";
	    ert.channel:next_page("ebank_manage_asset","EMAS0002",{id = "ebank_manage_asset",tranCode = "EMAS0002",CustName=CustName,AccBalance=AccBalance,signValue=signValue});
	end

	--电子账户
	function detail_inquiry()
	    finsh_continue_bug_flag = "1";
	    ert.channel:next_page("ebank_manage_asset","EMAS0004",{id = "ebank_manage_asset",tranCode = "EMAS0004"});
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0003"));