ebank_public.allSuit:physicalkey_result_back();
function trun_page_PD0004()
	local channelId = "ebank_product_deposit";
	local trancode = "PD0004";
	local post_boby = {id = channelId,tranCode = trancode};
	ert.channel:next_page(channelId, trancode, post_boby);
end

function turn_page_PD0004(num,z1,z2)
	finsh_continue_bug_flag = "1";
	if num == "4" then
	 	channelId = "ebank_product_deposit";
	 	trancode = "PD0004";
	else
		channelId = "ebank_product_deposit";
	 	trancode = "PD0002";
	end

	local post_boby = {id = channelId,tranCode = trancode,zn_rate=z1,zn_amount=z2};
	ert.channel:next_page(channelId, trancode, post_boby);
end