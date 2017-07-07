(function(this)

	--确认返回我的红包
	function sure_inquiry()
	    ert.channel:next_page("ebank_red_packet","EBRP0001",{id = "ebank_red_packet",tranCode = "EBRP0001"});
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EBRP0003"));