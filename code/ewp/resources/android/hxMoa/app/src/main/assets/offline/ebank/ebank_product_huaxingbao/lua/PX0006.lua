ebank_public.allSuit:physicalkey_result_back();
function turn_page_px0006(num,n1,m2)
	finsh_continue_bug_flag = "1";
	if num == "4" then
	 	channelId = "ebank_product_huaxingbao";
	 	trancode = "PX0004";
	else
		channelId = "ebank_product_huaxingbao";
	 	trancode = "PX0005";
	end
	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2};
	ert.channel:next_page(channelId, trancode, post_boby);
end