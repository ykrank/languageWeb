ebank_public.allSuit:physicalkey_back();

--多选框获取value
local che_flag = "0";
function this.get_fage()
	if(che_flag == "0")then
		che_flag = "1";
	else
		che_flag = "0";
	end
end

ert("#checkbox_id"):click(this.get_fage);


function trun_page_PD0003(z1,z2,money)
	--[[输入控制]]
	inuput_money = ert("#inuput_money"):val();
  inuput_money = string.gsub(inuput_money,",","");
	passoword_id = ert("#passoword_id"):val();

	  if inuput_money == "" then
        window:alert("请输入转入金额");
        return;
    end

    if inuput_money =="." then
        window:alert("请输入正确的转入金额");
        return;
    end

    if tonumber(inuput_money) == 0 then
        window:alert("转入金额不能为0");
        return;
    end
    
    if tonumber(inuput_money) < 50 then
        window:alert("起购金额不得低于50元");
        return;
    end
    if tonumber(inuput_money) > tonumber(money) then
    	window:alert("转入金额不能大于可用金额");
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
   -- window:alert(passoword_id);

    if che_flag =="1"then
			local channelId = "ebank_product_deposit";
			local trancode = "PD0003";
			local post_boby = {id = channelId,tranCode = trancode,zn_rate=z1,zn_amount=z2,BuyBalance=inuput_money,password=passoword_id};
			ert.channel:next_page(channelId, trancode, post_boby,{});
	else
        window:alert("还未阅读智能存款产品服务协议");
	end;
end

--页面标记
local tag = 1;

--显示弹出框            
function contentShow()
    --显示框
    window:showContent("local:FullSLoading1.xml",11);
    --弹出框报文
    local showboby=[[
    <div class="div_showContent" name="showcontent_div" border="0">
        <div class="ebank_public_header_div" border="0">
            <div class="ebank_public_header_div_left" border ="0" onclick="hidetable()">
                <img src="back.png" class="ebank_public_header_div_left_image2"></img>
            </div>
        <label class="ebank_public_header_label">智能存款协议内容</label>
        <label class="public_line_b1"></label>
        </div>
    <div class="public_main_div_1" border="0">
    <label class="label_w320_h40_t10">
    "智能存款"产品服务协议</label>
    <label class="label_f14_w290_l15">
 
甲   方：投资者 
乙   方：广东华兴银行股份有限公司（以下简称“华兴银行”） 
乙方为甲方提供的个人E账户人民币智能存款产品服务是不固定存期，可提前支取，并根据本金的实际留存时间对应的存款利率靠档计息，兼具收益性和流动性的一款定期存款产品。为规范甲、乙双方在本产品下的权利义务，双方依照中国法律法规和有关金融监管规章，经平等自愿协商一致，特就个人E账户人民币智能存款产品服务达成如下协议，承诺信守： 
第一条 声明与保证
1、甲方保证本协议下的存款本金是其合法所有的资金，依法可以用作本协议下的存款服务。 
2、在签署本协议后，甲方可通过乙方提供的渠道（包括但不限于华兴银行网站、手机银行）进行具体交易。 
第二条 业务与服务
（一）资金计息规则
1、存款服务的内容：在本协议有效期内，对于符合“智能存款”产品服务条件的资金，自起息日起存续届满一个存期的，乙方对到期的单笔本金进行自动本息转存，本金结付收益的规则以本协议第（二）条项下的约定为准。 
2、本产品每笔起存金额为人民币：50元。因甲方提前支取等原因，造成每笔存款本金不足起存金额时，乙方将提示甲方需全额支取。 
（二）收益计算规则
1、双方约定：存款时乙方根据甲方每笔本金的实际存期对应的利率计付利息。 
2、个人E账户下每笔智能存款本金依照起息日和终止日的不同各自进行独立管理，各自计算本金持有期及该笔资金的收益。就每一笔本金而言，收益计算的具体规则如下： 
（1）当每一笔本金存续期限届满一年时，乙方在届满日当日按该笔本金当前余额，适用存入日乙方1年期整存整取存款利率计算该笔资金的收益，计算公式为：利息=本金*整存整取1年期年利率。 
（2）当单笔本金因提前支取等各种原因发生变动，而未届满存期的，甲方授权乙方按照以下约定变更存期并结付收益:
  a.存期不满7天，整个存期按存入日1天通知存款利率计息（乙方挂牌执行利率）； 
  b.存期7天以上（含7天）不满3个月，整个存期按存入日7天通知存款利率计息（乙方挂牌执行利率）； 
  c.存期3个月以上（含三个月）不满6个月，整个存期按存入日3个月存款利率计息（乙方挂牌执行利率）； 
  d.存期6个月以上（含六个月）不满1年，整个存期按存入日6个月存款利率计息（乙方挂牌执行利率）； 
  e.存期1年以上（含1年），整个存期按存入日1年存款利率计息（乙方挂牌执行利率）。
（三）产品服务终止规则
1、发生以下事项时，乙方有权终止“智能存款”产品服务：
（1）当发生监管政策变化等特殊事项时，乙方有权终止相应本金（可以是一笔或者多笔本金）的“智能存款”产品服务。 
（2）甲方账户被执行了司法冻结、司法扣划。 
2、其它需要说明的事项包括:“智能存款”产品服务终止后，将按照第二条第（二）款约定的规则结付收益，本息转入活期账户。 
（四）解约规则
1、双方在此一致同意：本协议有效期限内，甲方和乙方任一方有权在任一个自然日解约。 
2、乙方单方提出解约的：应该通过在华兴银行网站上发布公告或者采用电话银行、手机短信等其他方式发布解约通知。甲方应自行通过乙方咨询电话等届时可适当利用的多种途径，以查询账户状态及/或打印对账单等方式了解签约信息、交易明细及本协议是否被终止等信息。 
3、甲方单方提出解约的：
（1）在任一自然日到乙方华兴银行网银、手机银行办理手续；
（2）乙方在收到解约通知的自然日按照本协议第二条第（二）款约定的计息规则的结付收益，本息转入活期账户。 
（五）法律适用与争议解决 
甲乙双方在履行本协议过程中引发的一切争议纠纷，双方可通过协商或调解解决，协商调解不成的，向乙方所在地（广州市天河区）人民法院提起诉讼。
    </label>
  </div>
    </div>
  ]]
  --动态设置报文
  local div_control_show = document:getElementsByName("showcontent_div");
  div_control_show[1]:setInnerHTML(showboby);
  --显示弹出框 
  window:showControl(div_control_show[1],tag); 
  --设置物理键监听
  window:setPhysicalkeyListener("backspace",hidetable);   
  --隐藏框
  window:hide(11);
end;


--关闭弹出框
function hidetable()    
    --显示框
    window:showContent("local:FullSLoading1.xml",12);
    window:hide(tag);
    ebank_public.allSuit:physicalkey_back();
    --隐藏框
    window:hide(12); 
    ryt:reload();

end;