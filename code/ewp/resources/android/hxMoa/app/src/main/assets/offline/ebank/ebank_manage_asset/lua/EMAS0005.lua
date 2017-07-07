(function(this)

	--滑动条回调方法,局部刷新数据
	local percent_value;
	function onchange_calback()
	    percent_value = ert("#percent_value"):val();
	    local percent_value_hxb = tonumber(percent_value)*100;
	    local percent_value_znck = (1-tonumber(percent_value))*100;

	    local div = [[<div name="percent" class="trtd_h44_w320" border="0">
		                <label class="label_c4D4D4D_t15,public_left">华兴宝</label>
		                <label class="label_c4D4D4D_t15,right55">智能存款</label>
		                <label id="percent_hxb" class="label_cF85050_t15,left67">]]..percent_value_hxb..[[%</label>
		                <label id="percent_znck" class="label_cF85050_t15,public_right">]]..percent_value_znck..[[%</label>
		              </div>]];
            local show_div= document:getElementsByName("percent");
	   if show_div ~= nil and #show_div>0 then
	       show_div[1]:setInnerHTML(div);
	   end
	end
	onchange_calback();
	
	--资产配置提交--successSign->是否配置标志
	function sure_result(successSign,falg)
	    local preserveAmount = string.gsub(ert("#inuput_money"):val(),",","");
	    local tranPassword = ert("#tran_password"):val();
	    local percent_hxb = percent_value;
	    local percent_znck = (1 - tonumber(percent_value));

	    local Sign;
	    if falg == "0" then
	    	Sign = "2";--修改
	    else
	    	Sign = "1";--首次签约
	    end
	    
	     --校验是否输入金额
	    if  preserveAmount == "" then
	    	window:alert("请输入留存金额");
	    	return;
	    end

	    --校验是否输入交易密码
	    if  tranPassword == "" then
	    	window:alert("请输交易密码");
	    	return;
	    end
	    if string.len(tranPassword) < 6 then
	     	window:alert("请输入6位数字交易密码");
	    	return;
	    end

	    ert.channel:next_page("ebank_manage_asset","EMAS0006",{id = "ebank_manage_asset",tranCode = "EMAS0006",preserveAmount=preserveAmount,tranPassword=encrypt:desRsa(tranPassword),percent_hxb=percent_hxb,percent_znck=percent_znck,Sign=Sign,successSign=successSign});
	end

	--撤销提交--successSign->是否配置标志
	function cancle_result(successSign)
	    local share_div = [[<?xml version="1.0" encoding="UTF-8" ?>
                <content>
                  <head>
                    <style>
                      .body {width:320px;top:0px;}
                      .div_l29_t100_h160_w262{left: 29px;top: 100px;height: 160px;width: 262px;background-image:url(ebank_manage_asset/images/popup.png);}
		      .div_l17_t35_h26_w26{left: 17px;top: 35px;height: 26px;width: 26px;background-image:url(ebank_manage_asset/images/prompt.png);}
                      .div_l0_t120_h1_w260{left: 0px;top: 120px;height: 1px;width: 262px;background-image:url(ebank_manage_asset/images/line.png);}
		      .label_l48_t41_w200_fs14_c4D4D4D{left: 48px;top: 41px;width: 200px;font-size: 14px;color: #4D4D4D;}
		      .div_t120_l0_h40_w130{top: 120px;left: 0px;height: 40px;width: 130px;}
		      .div_t120_r0_h40_w130{top: 120px;right: 0px;height: 40px;width: 130px;}
		      .label_l47_t8_fs18_c999999{left: 47px;top: 8px;font-size: 18px;color: #999999;}
		      .label_r47_t8_fs18_c69C2FC{right: 47px;top: 8px;font-size: 18px;color: #69C2FC;}
		      .div_t120_r0_h40_w1{top: 120px;right: 131px;height: 40px;width: 1px;color: #D8D8D8;}
                    </style>
                  </head>
                  <body name='body'>
                    <div id="revoke" class="div_l29_t100_h160_w262" border="0">
	               <div class="div_l17_t35_h26_w26" border="0"></div>
	               <div class="div_l0_t120_h1_w260" border="0"></div>
	               <label class="label_l48_t41_w200_fs14_c4D4D4D">撤销配置后，账户的资金不会自动购买理财产品，确定撤销吗？</label>
	               <div class="div_t120_l0_h40_w130" border="0" onclick="hide('0')">
	                 <label class="label_l47_t8_fs18_c999999">取消</label>
	               </div>
	               <div class="div_t120_r0_h40_w130" border="0" onclick="hide('1')">
	                 <label class="label_r47_t8_fs18_c69C2FC">确定</label>
	               </div>
	               <div class="div_t120_r0_h40_w1"></div>
	             </div>
                  </body>
                </content>]];
                window:showContent(share_div, 800);
	end

	function hide(falg)
	   if falg == "1" then
	       ert.channel:next_page("ebank_manage_asset","EMAS0006",{id = "ebank_manage_asset",tranCode = "EMAS0006",Sign="3",successSign=successSign});
	  end
	  window:hide(800);
	end

	--修改配置
	function asset_allocation()
	    ert.channel:next_page("ebank_manage_asset","EMAS0005",{id = "ebank_manage_asset",tranCode = "EMAS0005",falg="0"});
            globalSign="2";
	end

	function back_fun()
	   local req_value = ert.channel:get_request("ebank_manage_asset","EMAS0005");
	   if req_value["falgback"] ~= nil then
	       ert.channel:next_page('ebank_manage_asset','EMAS0001',{id = "ebank_manage_asset",tranCode = "EMAS0001"});
	   else
	       globalSign="1";
      	       ert.channel:show_loading();
      	       ert.channel:back();
      	  end
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0005"));