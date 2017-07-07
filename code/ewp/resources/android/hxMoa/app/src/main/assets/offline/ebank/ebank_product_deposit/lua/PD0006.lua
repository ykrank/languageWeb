ebank_public.allSuit:physicalkey_result_back();


function turn_page_pD0006(num,z1,z2)
	finsh_continue_bug_flag = "1";
	if num == "4" then
	 	channelId = "ebank_product_deposit";
	 	trancode = "PD0004";
	else
		channelId = "ebank_product_deposit";
	 	trancode = "PD0005";
	end
	local post_boby = {id = channelId,tranCode = trancode,zn_rate=z1,zn_amount=z2};
	ert.channel:next_page(channelId, trancode, post_boby);
end

