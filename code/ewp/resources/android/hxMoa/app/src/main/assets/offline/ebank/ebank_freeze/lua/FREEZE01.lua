(function(this)
	ebank_public.allSuit:physicalkey_back();
	function Callback(buttonIndex)

		if buttonIndex == 0 then
		local next_channelId = "ebank_freeze";
		local next_trancode = "FREEZE02";
		local post_body = {id=next_channelId,tranCode=next_trancode};
        ert.channel:next_page(next_channelId, next_trancode, post_body);
		else
			return;
		end
	end

    function click()
       window:alert("确认冻结E账户？","确定","取消",Callback);
		
    end;

    function unfreeze()
    	local next_channelId = "ebank_freeze";
		local next_trancode = "UNFZ0001";
		local eAccountNo = ert("#eAccountNo"):val();
		local context = {eAccountNo=eAccountNo};
		ert.channel:next_page(next_channelId, next_trancode, {},{just_page=true,context=context});
    end
    

end)(ert.channel:get_page("ebank_freeze","FREEZE02"));
