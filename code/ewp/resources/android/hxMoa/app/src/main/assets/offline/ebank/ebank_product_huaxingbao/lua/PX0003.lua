ebank_public.allSuit:physicalkey_back();
function turn_page_px0003(num,n1,m2)
    finsh_continue_bug_flag = "1";
	if num == "4" then
	 	channelId = "ebank_product_huaxingbao";
	 	trancode = "PX0004";
	else
		channelId = "ebank_product_huaxingbao";
	 	trancode = "PX0002";
	end
	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2};
	ert.channel:next_page(channelId, trancode, post_boby);
end

 --<div class="div_l10_bffffff_w300,bg#ffffff" border="0">
    --      <img src="check-icon.png" class="img_l100_t25_w36_h36" />
    --      <label class="label_fs17_c4d4d4d">申购成功！</label>
    --      <table border="0" class="table_t86">
    --        <tr>
    --          <td>
    --            <label class="label_l15_h30">产品名称</label>
    --          </td>
    --          <td>
    --            <label class="label_r15_h30">华兴宝</label>
    --          </td>
    --        </tr>
    --         <tr>
    --          <td>
    --            <label class="label_l15_h30">申购金额</label>
    --          </td>
    --          <td>
    --            <label class="label_pink">#{=format_money(subscribe)}#</label>
    --            <label class="label_r15_h30">元</label>
    --          </td>
    --        </tr>
    --         <tr>
    --          <td>
    --            <label class="label_l15_h30">当前持有份额</label>
    --          </td>
    --          <td>
    --            <label class="label_pink">#{=format_money(unit)}#</label>
    --            <label class="label_r15_h30">元</label>
    --          </td>
    --        </tr>
    --         <tr>
    --          <td>
    --            <label class="label_l15_h30">申购日期</label>
    --          </td>
    --          <td>
    --            <label class="label_r15_h30">#{=date}#</label>
    --          </td>
    --        </tr>
    --      </table>  
    --      <label class="public_line_stroke_b1"></label>
 -- --    </div> 
 -- --    <input type="button" class="public_15_w290_h35,button_left" value="查看交易明细" onclick="turn_page_px0003('4','#{=rate_xp}#','#{=profit_xp}#')" border="0"></input>
 -- --    <input type="button" class="public_15_w290_h35,button_right" value="继续申购" onclick="turn_page_px0003('2','#{=rate_xp}#','#{=profit_xp}#')" border="0"></input>