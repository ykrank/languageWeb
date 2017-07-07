ebank_public.allSuit:physicalkey_back();
--单选按扭--che_flag
--2 普通赎回
--3 快速赎回
local che_flag = "2";
function this.get_fage(flag,atdate)
    che_flag = flag;
    local content = "";
    if flag == "2" then
        content = [[<div class="div_d" border="0" name="label_yuji">
              <img src="ebank_product_huaxingbao/images/tip.png" class="img_w10_h10" />
              <label class="public_label_left,label_l30">预计</label>
              <label class="bFC646B">]]..format_date_to_chinse(atdate)..[[</label>
              <label class="label_new">之前到账</label>
              </div>]];

    elseif flag == "3" then
           content = [[<div class="div_d" border="0" name="label_yuji">
              <img src="ebank_product_huaxingbao/images/tip.png" class="img_w10_h10" />
              <label class="public_label_left,label_l30">资金马上到账，赎回当天无收益</label>
              </div>]];       
    else
    end
    local show_div= document:getElementsByName("label_yuji");--获取div的name
    if show_div ~= nil and #show_div>0 then
        show_div[1]:setInnerHTML(content);--div内容替换  
    end
    location:reload(); 
end;

--跳转结果页
function trun_page_PX0006(n1,m2,money)
	--[[--输入控制]]
	inuput_money = ert("#inuput_money"):val();
    inuput_money = string.gsub(inuput_money,",","");

	passoword_id = ert("#passoword_id"):val();

	if inuput_money == "" then
        window:alert("请输入赎回金额");
        return;
    end

    if inuput_money =="." then
        window:alert("请输入正确的赎回金额");
        return;
    end

    if tonumber(inuput_money) < 1 then
        window:alert("购买金额不能小于1元");
        return;
    end

    if tonumber(inuput_money) == 0 then
        window:alert("赎回金额不能为0");
        return;
    end

    if tonumber(inuput_money) > tonumber(money) then
        window:alert("赎回金额不能大于持有余额");
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
	local channelId = "ebank_product_huaxingbao";
	local trancode = "PX0006";
    --剩余持有份额
    balance = tostring(tonumber(money) - tonumber(inuput_money));
    --赎回标志（上送）che_flag 》 backflag赎回金额inuput_money 》 backbalance  交易密码passoword_id 》 passoWord

	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2,balance= balance,backflag=che_flag,backbalance=inuput_money,passoWord=passoword_id};
	ert.channel:next_page(channelId, trancode, post_boby);

end