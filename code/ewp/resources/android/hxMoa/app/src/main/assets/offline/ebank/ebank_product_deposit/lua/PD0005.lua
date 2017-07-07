ebank_public.allSuit:physicalkey_back();


--跳转结果页
function trun_page_PD0006(z1,z2,money)
	--[[--输入控制]]
	inuput_money = ert("#inuput_money"):val();
    inuput_money = string.gsub(inuput_money,",","");
	passoword_id = ert("#passoword_id"):val();

	if inuput_money == "" then
        window:alert("请输入支取金额");
        return;
    end

    if inuput_money =="." then
        window:alert("请输入正确的支取金额");
        return;
    end

    if tonumber(inuput_money) == 0 then
        window:alert("支取金额不能为0");
        return;
    end

    if tonumber(inuput_money) > tonumber(money) then
        window:alert("支取金额不能大于持有金额");
        return;
    end

    if (tonumber(money) - tonumber(inuput_money) < 50) and (tonumber(money) - tonumber(inuput_money) > 0) then
        window:alert("留存金额低于起存金额，需全部支取");
        return;
    end

    if passoword_id == "" then
        window:alert("请输入交易密码");
        return;
    end

    if string.len(passoword_id) < 6 then
     	window:alert("请输入6位数字交易密码");
    	return;
    end;
    
    passoword_id =encrypt:desRsa(passoword_id);
	--ebank_public.bottom:get_page("ebank_product_huaxingbao","PX0003");
	local channelId = "ebank_product_deposit";
	local trancode = "PD0006";
	local post_boby = {id = channelId,tranCode = trancode,zn_rate=z1,zn_amount=z2,backbalance=inuput_money,password=passoword_id};
	ert.channel:next_page(channelId, trancode, post_boby);

end