(function(this)

	--确认返回配置页
	function sure_asset()
	    ert.channel:next_page("ebank_manage_asset","EMAS0005",{id = "ebank_manage_asset",tranCode = "EMAS0005",falgback="0"});
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0006"));