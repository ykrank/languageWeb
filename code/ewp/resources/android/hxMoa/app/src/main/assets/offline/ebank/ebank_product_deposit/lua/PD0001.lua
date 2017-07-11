 --跳转到下一页并传值
function turn_page_PD0002(z1,z2)
	local channelId = "ebank_product_deposit";
	local trancode = "PD0002";
	local post_boby = {id = channelId,tranCode = trancode,zn_rate=z1,zn_amount=z2};
	ert.channel:first_page(channelId, trancode, post_boby,{});
end

--智能存款日期转换
function date_to_day(num)
	if (num) =="1天" then
		return 1;
	elseif (num) == "7天" then
		return 7;
	elseif (num) == "三个月" then
		return 90;
	elseif (num) == "六个月" then
		return 180;
	elseif (num) == "一年" then
		return 365;
	elseif (num) == "两年" then
		return 365*2;
	elseif (num) == "三年" then
		return 365*3;
	elseif (num) == "五年" then
		return 365*5;
	else 
		return "10";
	end
end

--图表点击事件
function show_alert()
	return;
end
