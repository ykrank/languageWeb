(function(this)
    ebank_public.allSuit:physicalkey_result_back();
    function click()
    	local next_channelId = "ebank_product_recommend";
		local next_trancode = "LH0001";
		local post_body = {id=next_channelId,tranCode=next_trancode};
		ert.channel:next_page(next_channelId, next_trancode, post_body);
    end
    

end)(ert.channel:get_page("ebank_freeze","FREEZE02"));
