--图表点击事件
function show_alert()
  return;
end

--跳转到下一页并传值
function turn_page_PX0002(n1,m2)
	local channelId = "ebank_product_huaxingbao";
	local trancode = "PX0002";
	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2};
	ert.channel:first_page(channelId, trancode, post_boby);
end;

--产品信息收缩
function show_detail(n)
local div;
	if(n == "1")then
		div = [[
        <div class="div_h100_w320" border="0" name="div_but" id="div_but">
          <table border="0">
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('0')">
                <label class="public_line_stroke_t0"></label>
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">收益分配方式与计算方式</label>
                <img src="ebank_product_huaxingbao/images/right1.png" class="img_right_r15_h14_t10_w5" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr><td><label class="label_l15_w290_f14">
            　   
1、每日计算分红收益，按日支付收益；收益以分红形式结转成基金份额。
 
2、每个交易日（T）15:00前的账户余额，T+1交易日开始计算收益，T+2交易日收益将结转至华兴宝余额中；T日15:00后账户余额申购将计算在下一个交易日内。
 
3、华兴宝收益率情况由广发基金管理公司公布并提供，如有疑问，请咨询广发基金管理公司客户服务电话：95105828。
　
</label></td></tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('2')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转入(申购)规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('3')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转出(赎回)，支付规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
          </table>
        </div>]];
	
	elseif(n=="2")then
  
	
		 div = [[<div class="div_h100_w320" border="0" name="div_but" id="div_but">
          <table border="0">
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('1')">
                <label class="public_line_stroke_t0"></label>
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">收益分配方式与计算方式</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('0')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转入(申购)规则</label>
                <img src="ebank_product_huaxingbao/images/right1.png" class="img_right_r15_h14_t10_w5" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr><td><label class="label_l15_w290_f14">
            　
1、现单日无转入累计限额。华兴银行与基金公司有权修改此限额。
 
2、交易日：上海证券交易所和深圳证券交易所的正常交易日。
　
</label></td></tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('3')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转出(赎回)，支付规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
          </table>
        </div>]];
	elseif(n=="3")then		
		 div = [[  <div class="div_h100_w320" border="0" name="div_but" id="div_but">
          <table border="0">
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('1')">
                <label class="public_line_stroke_t0"></label>
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">收益分配方式与计算方式</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('2')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转入(申购)规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('0')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转出(赎回)，支付规则</label>
                <img src="ebank_product_huaxingbao/images/right1.png" class="img_right_r15_h14_t10_w5" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr><td><label class="label_l15_w290_f14">
            　
1、客户发起赎回交易时，账户内份额采取后进先出原则，由基金公司系统自动进行处理。
 
2、快速赎回总额度控制：为避免实时大额赎回过多影响华兴宝业务运营，我行与基金公司设置了实时赎回总额度。我行与基金公司根据实时赎回情况灵活控制实时赎回总额度，如客户实时赎回款项超过系统当日可用实时赎回总额度时，则会提示客户进行普通赎回。
 
3、普通赎回：超过当日实时赎回限额的，客户需进行普通赎回，普通赎回无限额，款项将于T+1日划至客户账户内。
　
</label></td></tr>
          </table>
        </div>]];
    else
    	div = [[
        <div class="div_h100_w320" border="0" name="div_but" id="div_but">
          <table border="0">
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('1')">
                <label class="public_line_stroke_t0"></label>
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">收益分配方式与计算方式</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('2')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转入(申购)规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
            <tr><td class="public_line"></td></tr>
            <tr class="tr_h35_l15_t10_h15_f12_c69c2fc">
              <td onclick="show_detail('3')">
                <img src="ebank_product_huaxingbao/images/tip_1.png" class="img_l15_t8_h14" />
                <label class="label_l15_t10">转出(赎回)，支付规则</label>
                <img src="ebank_product_huaxingbao/images/down.png" class="img_down_r15_h5_t15_w14" />
              </td>
            </tr>
          </table>
        </div>]];
	end;
    
	  local show_div= document:getElementsByName("div_but");--获取div的name
		if show_div ~= nil and #show_div>0 then
		  show_div[1]:setInnerHTML(div);--div内容替换
		end;
    location:reload();

  --  local platform_str = ert.platform:os_info();
  --  if(platform_str=="android")then
  --    location:reload();
  --  end
end

