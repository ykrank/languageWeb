ebank_public.allSuit:physicalkey_exit_back();
function back_fun_text()
	ert.channel:back();
end

--跳转页面
function  turn_page_product(num,n1,m2)
	if num == "1" then
		 channelId = "ebank_product_huaxingbao";
		 trancode = "PX0001";
	else
		 channelId = "ebank_product_deposit";
		 trancode = "PD0001";
	end

	local post_boby = {id = channelId,tranCode = trancode,rate_xp= n1,profit_xp= m2};
	ert.channel:first_page(channelId, trancode, post_boby,{});
end
