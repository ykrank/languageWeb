(function(this)

	local Accno;--转入转出账号
	local BankName_c;--转入转出所属银行
	local BankFlag;--本行(1)他行(0)标志
	local SignValue;--转入(0)转出(1)标志
	local SimpPayFlag;--是否签约标志
	local province_list;--省份城市的数据
	local cityCode;--城市code
	local OpenBankNo;--行号
	local Money;--提现金额
	local pathId;--汇路编号
	local  ChoiceBankName;--选择银行的行名
	local FalgCon;--汇路标志(1)网银互联、(2)小额、(3)大额

	--获取省份城市的数据
	function get_m()
	    ert.channel:next_page("ebank_manage_asset","EMAS0012",{id = "ebank_manage_asset",tranCode = "EMAS0012"},{request_callback=get_m_callback});
	end

	function get_m_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
    	        local json_data = response["responseBody"];
    	        local json_tbl = json:objectFromJSON(json_data) or {};
    	        province_list = json_tbl["body"]["provincelist"];
    	        local div_province = [[<div name="select_province" class="trtd_h44_w320" border="0">
    	    			          <img src="get_down.png" class = "public_img_h7_w12_r15,top16"></img>
                                              <label class="public_label_left,top15">省份</label>
		                            <select class="select_h30_w180_t8_fs14_c3A3A3A,left123">]];
    	        for key,value in pairs(province_list) do
    	    	    if value["code"] == "11" then
    	    	        province_choice("11");
    	    	    end
                      div_province = div_province..[[<option name="option_province" onclick="province_choice(']]..value["code"]..[[')">]]..value["name"]..[[</option>]];
    	        end
    	        div_province = div_province..[[</select></div>]];
    	        local show_div= document:getElementsByName("select_province");
                 if show_div ~= nil and #show_div>0 then
                     show_div[1]:setInnerHTML(div_province);
                 end
             else
                 window:alert("网络请求失败");
                 return;
             end
	end

	function province_choice(code)
	     local div_city = [[<div name="select_city" class="trtd_h44_w320" border="0">
    	    	                         <img src="get_down.png" class = "public_img_h7_w12_r15,top16"></img>
                                           <label class="public_label_left,top15">城市</label>
		                         <select class="select_h30_w180_t8_fs14_c3A3A3A,left123">]];
	     for key_p,value_p in pairs(province_list) do
	     	if value_p["code"] == code then
	     	    for key_c,value_c in pairs(value_p["list"]) do
	     	    	cityCode = value_c["code"];
                          div_city = div_city..[[<option name="option_city" >]]..value_c["name"]..[[</option>]];
                      end
                  end
    	    end
    	    div_city = div_city..[[</select></div>]];
    	    local show_div= document:getElementsByName("select_city");
             if show_div ~= nil and #show_div>0 then
                 show_div[1]:setInnerHTML(div_city);
             end
	end

	--如果总页数返回""或者nil-->"0"
	function change_CountNum (recordCountNum)
	    if recordCountNum == "" or recordCountNum == nil then
	    	recordCountNum = "0";
	    end
	    return recordCountNum;
	end

	--页码pageNumber 
	local pageNumber = "1";
	--每页显示记录数countNum
	local countNum = "10";
	--总条数
	local recordCountNum = "1";

	local div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_bank_seach1()" preurl ="upUrl()" border="0">]];

	function get_bank_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
	        local json_data = response["responseBody"];
	        local json_tbl = json:objectFromJSON(json_data) or {};
	        local transaction_list = json_tbl["body"]["transactionlist"];

	        recordCountNum = change_CountNum(json_tbl["body"]["recordCountNum"]);
                 pageNumber = pageNumber + 1;
	     
	    --判断是否有数据
                 if recordCountNum == "0" then
	    	    local div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
			                 <label class="label_l136_t10_w320">无记录</label>
			               </div>]];
		    local show_div= document:getElementsByName("ref_bank");
	             if show_div ~= nil and #show_div>0 then
	                 show_div[1]:setInnerHTML(div);
	             end
	        else
		    for key,value in pairs(transaction_list) do
		          div = div..[[
		          <tr class="trtd_h40_w320" border="1" onclick="get_bank_hide(']]..value["openBankNo"]..[[',']]..value["bankName"]..[[')">
	                       <td class="trtd_h40_w320">
	                             <label class="label_l15_fs14_c4D4D4D">]]..value["bankName"]..[[</label>
	                         <label class="public_line_b1"></label>
	                       </td>
	                   </tr>]];
		    end
      	        end
      	        local div_callback ="";
                 div_callback = div..[[</table></div>]];
                 local show_div= document:getElementsByName("ref_bank");
                 if show_div ~= nil and #show_div>0 then
                     show_div[1]:setInnerHTML(div_callback);
                 end
             else
                 window:alert("网络请求失败");
                 return;
             end
	end

	--显示选择的银行结果
	function get_bank_hide(openBankNo,bankName)
	  OpenBankNo = openBankNo;
	  ChoiceBankName = bankName;
	  local div = [[<div name="select_bank" class="trtd_h44_w320" border="0" onclick="get_bank_name()">
	       <img src="right_jt.png" class = "public_img_h12_w7_r15,top16"></img>
	       <label class="public_label_left,top15">开户行</label>]];
	     if string.len(bankName) >36 then
	       	div = div..[[
	       	<label id="select_bank_label" class="public_input_w100_l117,width180,top5,left123">]]..bankName..[[</label>]];
	     else
	       	div = div..[[
	       	<label id="select_bank_label" class="public_input_w100_l117,width180,top15,left123">]]..bankName..[[</label>]];
	     end
	     div = div..[[</div>]];
	     local show_div= document:getElementsByName("select_bank");
	     if show_div ~= nil and #show_div>0 then
	        show_div[1]:setInnerHTML(div);
	     end
	     window:hide(200);
	     get_bank_money();
	end

	function get_bank_hide_back()
	      div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_bank_seach1()" preurl ="upUrl()" border="0">]];
	      pageNumber = "1";
	      window:hide(200);
	end

	--向下滑动空方法
	function upUrl()
		
	end

	local share_div = [[<?xml version="1.0" encoding="UTF-8" ?>
             <content>
               <head>
                 <style>
                   .body {width:320px;top:0px;background-color: #EBEBEE;}
                   .div_t44_w320{top: 44px;width: 320px;background-color: #EBEBEE;}
                   .div_t0_h45_w320{top 0px;height: 45px;width: 320px;background-color: #FFFFFF;}
                   .img_t7_h30_w290_l15{top: 7px;height: 30px;width: 290px;left: 15px;}
                   .img_t16_h12_w12_r30{top: 16px;height: 12px;width: 12px;right: 30px;}
                   .input_t8_l20_h30_w245_c999999{top: 8px;left: 20px;height: 30px;width: 245px;font-size: 14px;color: #999999;}
                   .input_t8_r15_h30_w50{top: 8px;right: 15px;height: 30px;width: 50px;}
                   .div_t45_h450_w320{top:45px;height:450px;width:320px;background-color: #FFFFFF;}
                   .label_l15_fs14_c4D4D4D{left: 15px;font-size: 14px;color: #4D4D4D;}
                 </style>
               </head>
               <body name='body'>
                  <div class="ebank_public_header_div" border="0">
                     <div border ="0">
                        <input type="button" class="ebank_public_header_div_left" border ="0" enable="true" onclick="get_bank_hide_back()"/>
                        <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                     </div>
		   <label class="ebank_public_header_label">选择银行</label>
		   <label class="public_line_b1"></label>
		</div>
                  <div class="div_t44_w320" border="0">
                     <div class="div_t0_h45_w320" border="0">
                      <img src="ssk.png" class="img_t7_h30_w290_l15"></img>
                      <img src="ssbutton.png" class="img_t16_h12_w12_r30"></img>
                      <input name="getBankName" type="text" class="input_t8_l20_h30_w245_c999999" border ="0" hold="搜索银行"/>
                      <input type="button" class="input_t8_r15_h30_w50" border ="0" onclick="get_bank_seach()"/>
                      <label class="public_line_b1"></label>
                     </div>
	            <div name="ref_bank" class="div_t45_h450_w320" border="0"></div>
	         </div>
               </body>
             </content>]];
	    
         --搜索,清空table,pageNumber还原为第一页
         function get_bank_seach()
             div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_bank_seach1()" preurl ="upUrl()" border="0">]];
             pageNumber = "1";
             local bankName = ert.dom:get_ctrl_value_by_name_ex("getBankName");
             ert.channel:next_page("ebank_manage_asset","EMAS0010",{id = "ebank_manage_asset",tranCode = "EMAS0010",turnPageBeginPos=pageNumber,TurnPageShowNum=countNum,bankName=bankName},{request_callback=get_bank_callback,Money=Money});           
         end

         --防止再show一次页面
         function get_bank_seach1()
             local bankName = ert.dom:get_ctrl_value_by_name_ex("getBankName");
             if recordCountNum ~= "0" and tonumber(pageNumber) > tonumber(math.ceil(recordCountNum/countNum)) then
		window:alert("最后一页");
		return;
	    else
                  ert.channel:next_page("ebank_manage_asset","EMAS0010",{id = "ebank_manage_asset",tranCode = "EMAS0010",turnPageBeginPos=pageNumber,TurnPageShowNum=countNum,bankName=bankName},{request_callback=get_bank_callback,Money=Money});
             end        
         end

	function get_bank_name()
	    --校验是否输入金额
	    Money = string.gsub(ert("#inuput_money"):val(),",","");
	    if  Money == "" then
	    	window:alert("请输入转出金额");
	    	return;
	    end

	     div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_bank_seach1()" preurl ="upUrl()" border="0">]];
	     pageNumber = "1";
	    
	    --show出模糊查询页面
             window:showContent(share_div, 200);
             
             ert.channel:next_page("ebank_manage_asset","EMAS0010",{id = "ebank_manage_asset",tranCode = "EMAS0010",turnPageBeginPos=pageNumber,TurnPageShowNum=countNum,bankName=""},{request_callback=get_bank_callback,Money=Money});
	end

	--试算接口
	function get_bank_money()
	    input_onblur();
	    Money = ert("#inuput_money"):val();
	    if Money ~= nil then
	       Money = string.gsub(ert("#inuput_money"):val(),",","");
	    end

	    if Money ~= nil and Money ~= "" and OpenBankNo ~= nil and OpenBankNo ~= "" then
	        ert.channel:next_page("ebank_manage_asset","EMAS0008",{id = "ebank_manage_asset",tranCode = "EMAS0008",amountIn=Money,openBankNo=OpenBankNo},{request_callback=get_callback});
	    end
	end

	function get_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
	        local json_data = response["responseBody"];
    	        local json_tbl = json:objectFromJSON(json_data) or {};
	        pathId = json_tbl["body"]["pathId"];
	        local pathDescription = json_tbl["body"]["pathDescription"];--汇路名称
	        ert("#con_tr"):css("display","block");
	        ert("#con_nullblank"):css("display","block");
	        location:reload();
	        local div_con = [[
	        <div name="select_con" class="trtd_h44_w320" border="0">
                    <label class="public_label_left,top15">汇路</label>
                    <select class="select_h30_w180_t8_fs14_c3A3A3A,left123">]];
                    if pathId == "" or pathId == nil then
              	      div_con = div_con..[[
                        <option name="option1" onclick="choice_con(1)">网银互联</option>
                        <option name="option2" onclick="choice_con(2)">小额</option>
                        <option name="option3" onclick="choice_con(3)">大额</option>]];
                    elseif pathId == "IBPS" then
                	      div_con = div_con..[[
                        <option name="option1" onclick="choice_con(1)">]]..pathDescription..[[</option>
                        <option name="option2" onclick="choice_con(2)">小额</option>
                        <option name="option3" onclick="choice_con(3)">大额</option>]];
                    elseif pathId == "HVPS" then
                	      div_con = div_con..[[
                        <option name="option3" onclick="choice_con(3)">]]..pathDescription..[[</option>
                        <option name="option1" onclick="choice_con(1)">网银互联</option>
                        <option name="option2" onclick="choice_con(2)">小额</option>]];
                    elseif pathId == "BEPS" then
                	      div_con = div_con..[[
                        <option name="option2" onclick="choice_con(2)">]]..pathDescription..[[</option>
                        <option name="option1" onclick="choice_con(1)">网银互联</option>
                        <option name="option3" onclick="choice_con(3)">大额</option>]];
                    end
                    div_con = div_con..[[
                    </select>
                </div>]];
                local show_div= document:getElementsByName("select_con");
	       if show_div ~= nil and #show_div>0 then
	          show_div[1]:setInnerHTML(div_con);
	       end
	       if pathId == "HVPS" then
	           choice_con(3);
	       elseif pathId == "BEPS" then
	           choice_con(2);
	       elseif pathId == "IBPS" then
	           choice_con(1);
	       end
	    else
	       window:alert("网络请求失败");
                return;
             end
	end

	--汇路选择判断
	function choice_con(falgCon)
	    FalgCon = falgCon;
             if falgCon == 1 then
             	pathId = "IBPS"
             	ert("#province_tr"):css("display","none");
	    	ert("#city_tr"):css("display","none");
	    	ert("#network_tr"):css("display","none");
	    	ert("#null_tr"):css("display","none");
	    elseif falgCon == 2 then
	    	pathId = "BEPS"
	    	ert("#province_tr"):css("display","block");
	    	ert("#city_tr"):css("display","block");
	    	ert("#network_tr"):css("display","block");
	    	ert("#null_tr"):css("display","block");
	    	get_m();
	    elseif falgCon == 3 then
	    	pathId = "HVPS"
	    	ert("#province_tr"):css("display","block");
	    	ert("#city_tr"):css("display","block");
	    	ert("#network_tr"):css("display","block");
	    	ert("#null_tr"):css("display","block");
	    	get_m();
	    end
	    location:reload();
	end

	--页码pageNumber 
	local pageNumber_n = "1";
	--每页显示记录数countNum
	local countNum_n = "10";
	--总条数
	local recordCountNum_n = "1";

	local div = [[<div name="network_tr" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_network_seach1()" preurl ="upUrl()" border="0">]];

	function get_network_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
	        local json_data = response["responseBody"];
	        local json_tbl = json:objectFromJSON(json_data) or {};
	        local transaction_list = json_tbl["body"]["transactionlist"];

	        recordCountNum_n = change_CountNum(json_tbl["body"]["recordCountNum"]);
                 pageNumber_n = pageNumber_n + 1;
	     
	        --判断是否有数据
                 if recordCountNum_n == "0" then
	    	    local div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
			                 <label class="label_l136_t10_w320">无记录</label>
			               </div>]];
		    local show_div= document:getElementsByName("ref_bank");
	             if show_div ~= nil and #show_div>0 then
	                 show_div[1]:setInnerHTML(div);
	             end
	        else
		    for key,value in pairs(transaction_list) do
		          div = div..[[
		          <tr class="trtd_h40_w320" border="1" onclick="get_network_hide(']]..value["bankName"]..[[')">
	                       <td class="trtd_h40_w320">
	                         <label class="label_l15_fs14_c4D4D4D">]]..value["bankName"]..[[</label>
	                         <label class="public_line_b1"></label>
	                       </td>
	                   </tr>]];
		    end
      	        end
      	        local div_callback ="";
                 div_callback = div..[[</table></div>]];
                 local show_div= document:getElementsByName("ref_bank");
                 if show_div ~= nil and #show_div>0 then
                 show_div[1]:setInnerHTML(div_callback);
                 end
             else
                 window:alert("网络请求失败");
                 return;
             end
	end

	--显示选择的网点结果
	function get_network_hide(bankName)
	  local div = [[<div name="select_network" class="trtd_h44_w320" border="0" onclick="get_bank_name()">
	       <img src="right_jt.png" class = "public_img_h12_w7_r15,top16"></img>
	       <label class="public_label_left,top15">网点</label>]];
	     if string.len(bankName) >36 then
	       	div = div..[[
	       	<label id="select_network_label" class="public_input_w100_l117,width180,top5,left123">]]..bankName..[[</label>]];
	     else
	       	div = div..[[
	       	<label id="select_network_label" class="public_input_w100_l117,width180,top15,left123">]]..bankName..[[</label>]];
	     end
	     div = div..[[</div>]];
	     local show_div= document:getElementsByName("select_network");
	     if show_div ~= nil and #show_div>0 then
	        show_div[1]:setInnerHTML(div);
	     end
	     window:hide(300);
	end

	function get_network_hide_back()
	      div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_network_seach1()" preurl ="upUrl()" border="0">]];
	      pageNumber = "1";
	      window:hide(300);
	end

	--向下滑动空方法
	function upUrl()
		
	end

	local share_div = [[<?xml version="1.0" encoding="UTF-8" ?>
             <content>
               <head>
                 <style>
                   .body {width:320px;top:0px;background-color: #EBEBEE;}
                   .div_t44_w320{top: 44px;width: 320px;background-color: #EBEBEE;}
                   .div_t0_h45_w320{top 0px;height: 45px;width: 320px;background-color: #FFFFFF;}
                   .img_t7_h30_w290_l15{top: 7px;height: 30px;width: 290px;left: 15px;}
                   .img_t16_h12_w12_r30{top: 16px;height: 12px;width: 12px;right: 30px;}
                   .input_t8_l20_h30_w245_c999999{top: 8px;left: 20px;height: 30px;width: 245px;font-size: 14px;color: #999999;}
                   .input_t8_r15_h30_w50{top: 8px;right: 15px;height: 30px;width: 50px;}
                   .div_t45_h450_w320{top:45px;height:450px;width:320px;background-color: #FFFFFF;}
                   .label_l15_fs14_c4D4D4D{left: 15px;font-size: 14px;color: #4D4D4D;}
                 </style>
               </head>
               <body name='body'>
                  <div class="ebank_public_header_div" border="0">
                     <div border ="0">
                        <input type="button" class="ebank_public_header_div_left" border ="0" enable="true" onclick="get_network_hide_back()"/>
                        <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                     </div>
		   <label class="ebank_public_header_label">选择网点</label>
		   <label class="public_line_b1"></label>
		</div>
                  <div class="div_t44_w320" border="0">
                     <div class="div_t0_h45_w320" border="0">
                      <img src="ssk.png" class="img_t7_h30_w290_l15"></img>
                      <img src="ssbutton.png" class="img_t16_h12_w12_r30"></img>
                      <input name="getNetworkName" type="text" class="input_t8_l20_h30_w245_c999999" border ="0" hold="搜索网点"/>
                      <input type="button" class="input_t8_r15_h30_w50" border ="0" onclick="get_network_seach()"/>
                      <label class="public_line_b1"></label>
                     </div>
	            <div name="ref_bank" class="div_t45_h450_w320" border="0"></div>
	         </div>
               </body>
             </content>]];
	    
         --搜索,清空table,pageNumber还原为第一页
         function get_network_seach()
             div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_network_seach1()" preurl ="upUrl()" border="0">]];
             pageNumber_n = "1";
             local bankNameNet = ert.dom:get_ctrl_value_by_name_ex("getNetworkName");
             ert.channel:next_page("ebank_manage_asset","EMAS0009",{id = "ebank_manage_asset",tranCode = "EMAS0009",turnPageBeginPos=pageNumber_n,TurnPageShowNum=countNum_n,bankName=bankNameNet,cityCode=cityCode},{request_callback=get_network_callback});           
         end

         --防止再show一次页面
         function get_network_seach1()
             local bankNameNet = ert.dom:get_ctrl_value_by_name_ex("getNetworkName");
             if recordCountNum_n ~= "0" and tonumber(pageNumber_n) > tonumber(math.ceil(recordCountNum_n/countNum_n)) then
		window:alert("最后一页");
		return;
	    else
                  ert.channel:next_page("ebank_manage_asset","EMAS0009",{id = "ebank_manage_asset",tranCode = "EMAS0009",turnPageBeginPos=pageNumber_n,TurnPageShowNum=countNum_n,bankName=bankNameNet,cityCode=cityCode},{request_callback=get_network_callback});
             end        
         end

	function get_network_name()
	     div = [[<div name="ref_bank" class="div_t45_h400_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height440" type="scrolltable" nexturl="get_network_seach1()" preurl ="upUrl()" border="0">]];
	     pageNumber = "1";
	    
	    --show出模糊查询页面
             window:showContent(share_div, 300);
             
             ert.channel:next_page("ebank_manage_asset","EMAS0009",{id = "ebank_manage_asset",tranCode = "EMAS0009",turnPageBeginPos=pageNumber_n,TurnPageShowNum=countNum_n,bankName="",cityCode=cityCode},{request_callback=get_network_callback});
	end

	--选择卡号
	function option_choice(accNO,bankFlag,signValue,bankName_c)
	    if signValue == "0" and bankFlag == "0" then
	       ert.channel:next_page("ebank_manage_asset","EMAS0022",{id = "ebank_manage_asset",tranCode = "EMAS0022",account=accNO},{request_callback=simpPay_callback});
	    end

	    Accno = accNO;
	    BankName_c = bankName_c;
	    BankFlag = bankFlag;
	    SignValue = signValue;
	    if SignValue == "0" then
	        if BankFlag == "0" then
	      	   ert("#nullblank"):css("display","none");
	    	   ert("#receivedtime"):css("display","block");
	    	   ert("#tr_code"):css("display","block");
	    	   ert("#div_line"):css("display","block");
	    	   ert("#tr_balance"):css("display","none");
	    	   ert("#phone_val"):css("display","block");
	    	   ert("#username_val"):css("display","none");
	        else
	      	   ert("#nullblank"):css("display","block");
	    	   ert("#receivedtime"):css("display","none");
	    	   ert("#tr_code"):css("display","none");
	    	   ert("#div_line"):css("display","none");
	    	   ert("#tr_balance"):css("display","block");
	    	   ert("#phone_val"):css("display","none");
	    	   ert("#username_val"):css("display","block");
	        end
	        location:reload();
	    else
	        if BankFlag == "0" then
	      	   ert("#nullblank"):css("display","none");
	    	   ert("#receivedtime"):css("display","block");
	    	   ert("#bank_tr"):css("display","block");
	        else
	      	   ert("#nullblank"):css("display","block");
	    	   ert("#receivedtime"):css("display","none");
	    	   ert("#bank_tr"):css("display","none");
	        end
	        location:reload();
	    end
	end

	function simpPay_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
	        local json_data = response["responseBody"];
	        local json_tbl = json:objectFromJSON(json_data) or {};
	        local simpPayFlag = json_tbl["body"]["flag"];
	        SimpPayFlag = simpPayFlag;
	        if simpPayFlag == nil or simpPayFlag == "" then
	            simpPayFlag = "";
	        end
	        if simpPayFlag == "1" and SignValue == "0" and BankFlag == "0" then
	            window:alert("该卡未签约,请签约", "确定", "取消",hint_callback);
	        end
	    else
	        window:alert("网络请求失败");
                 return;
             end
	end

	function hint_callback(index)
	     local req_value = ert.channel:get_request("ebank_manage_asset","EMAS0002");
	     if index == 0 then
	     	local context = {Accno=Accno,accBalance=req_value["AccBalance"]};
        		ert.channel:first_page("ebank_manage_asset","EMAS0020",{id="ebank_tied_card",tranCode="BOUND001"},{context=context});
	     end
	end
         
	--转入转出提交
	function click_into(signValue,AccBalance)
	    Money = string.gsub(ert("#inuput_money"):val(),",","");
	    local Node = ert("#node"):val();
	    local PassWord = ert("#tran_password"):val();
	    local Choicebakn = ert("#choicebakn"):val();
	    local selectBankLabel = ert("#select_bank_label"):val();--开户行
	    local selectNetworkLabel = ert("#select_network_label"):val();--网点

	    --校验是否选择下挂银行
	    if  Choicebakn == "请选择" then
	    	window:alert("请选择银行卡");
	    	return;
	    end

	    --校验金额
	    if SignValue == "1" then
	    	if Money == nil or Money == "" then
	             window:alert("请输入转出金额");
	             return;
	         end
	    	if tonumber(Money) > tonumber(AccBalance) then
	    	    window:alert("转出金额不能大于账户余额");
	    	    return;
	    	end
	    else
	    	if Money == nil or Money == "" then
	             window:alert("请输入转入金额");
	             return;
	         end
	    end

	    --校验是否选择了开户行
	    if BankFlag == "0" then
	        if selectBankLabel == "请选择开户行" then
	    	    window:alert("请选择开户行");
	    	    return;
	        end
	        if FalgCon ~= 1 then
	            --校验是否选择了网点
	            if selectNetworkLabel == "请选择网点" then
	    	        window:alert("请选择网点");
	    	        return;
	            end
	        end
	    end

	    --校验短信验证码
	    if (SignValue == "0" and BankFlag == "0") or (SignValue == "1" and BankFlag == "0") or (SignValue == "1" and BankFlag == "1") then
	        if  Node == "" then
	    	    window:alert("请输短信验证码");
	    	    return;
	        end
	        if  string.len(Node) ~= 6 then
	    	    window:alert("请输6位短信验证码");
	    	    return;
	        end
	    end

	    --校验是否输入交易密码
	    if  PassWord == "" then
	    	window:alert("请输交易密码");
	    	return;
	    end
	    if string.len(PassWord) < 6 then
	     	window:alert("请输入6位数字交易密码");
	    	return;
	    end
	    
	    --校验卡是否签约
	    if SimpPayFlag == "1" and SignValue == "0" and BankFlag == "0" then
	    	window:alert("该卡未签约，请重新选择卡号");
	    	return;
	    end

	    if BankFlag == "1" then
	    	--提交转出、转入本行数据
	    	ert.channel:next_page("ebank_manage_asset","EMAS0003",{id = "ebank_manage_asset",tranCode = "EMAS0003",account=Accno,amountIn=Money,smsValidationCode=Node,tranPassword=encrypt:desRsa(PassWord),SignValue=SignValue,BankFlag=BankFlag,BankName_c=BankName_c});
	    else
	    	--提交转出、转入他行数据
	    	ert.channel:next_page("ebank_manage_asset","EMAS0003",{id = "ebank_manage_asset",tranCode = "EMAS0003",bankName=ChoiceBankName,bankNo=OpenBankNo,pathId=pathId,account=Accno,amountIn=Money,smsValidationCode=Node,tranPassword=encrypt:desRsa(PassWord),SignValue=SignValue,BankFlag=BankFlag,BankName_c=BankName_c});
	    end
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0002"));