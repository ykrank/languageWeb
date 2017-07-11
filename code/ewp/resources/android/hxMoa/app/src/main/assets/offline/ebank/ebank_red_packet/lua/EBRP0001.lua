(function(this)
	
	--格式化"1"->发出"2"->领取
	function format_type(type)
	    if type == "1" then
	    	return "发出" ;
	    else
	    	return "领取";
	    end
	end

	--格式化
	function h_type(type)
	    if type == "0" then
	    	type = "普通红包" ;
	    else
	    	type = "随机红包";
	    end
	    return type;
	end

	--如果总页数返回""或者nil-->"0"
	function change_CountNum (recordCountNum)
	    if recordCountNum == "" or recordCountNum == nil then
	    	recordCountNum = "0";
	    end
	    return recordCountNum;
	end

	--防止重复点击标记
	local click_flag  = 0;
	--红包详情列表
	function red_packet_list(tranType)
		if click_flag == 0  then
			 click_flag = 1 ;
			-- window:alert(click_flag)
	    ert.channel:next_page("ebank_red_packet","EBRP0004",{id = "ebank_red_packet",tranCode = "EBRP0004",tranType = tranType});
	    else
	   		window:alert(22)
	    end
	end

	--红包提现
	function withdraw_red_packet(WithdrawAmount)
		if WithdrawAmount =="" or WithdrawAmount ==nil or WithdrawAmount == "0" then
			window:alert("没有可提现的红包");
			return
		end
	    ert.channel:next_page('ebank_red_packet','EBRP0002',{WithdrawAmount = WithdrawAmount},{show_loading=true, just_page=true});
	end

	--红包详情页面
	function details_page(redEnvelopeNo,redEnvelopeType)
	    ert.channel:next_page("ebank_red_packet","EBRP0005",{id = "ebank_red_packet",tranCode = "EBRP0005",redEnvelopeNo=redEnvelopeNo,redEnvelopeType=redEnvelopeType});
	end

	--页码pageNumber 
	local pageNumber = "1";
	--每页显示记录数countNum
	local countNum = "10";
	--总条数
	local recordCountNum = "1";

	local div = [[<div name="referencelist" class="div_t311_h190_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height190" type="scrolltable" nexturl="reference_list('3')" preurl ="upUrl()" border="0">]];

	--回调、交易明细刷新数据
	function refurbish_callback(response)
	    local code = response["responseCode"];
             if code == 200 then
	        local json_data = response["responseBody"];
    	        local json_tbl = json:objectFromJSON(json_data) or {};
    	        json_tbl["body"] = json_tbl["body"] or {};
	        local transaction_list = json_tbl["body"]["transactionlist"]or{};
	    
                 recordCountNum = change_CountNum(json_tbl["body"]["recordCountNum"]);
                 pageNumber = pageNumber + 1;

                 --判断是否有数据
                 if recordCountNum == "0" then
	    	    local div = [[<div name="referencelist" class="div_t311_h190_w320" border="0">
			             <label class="label_l136_t10_w320">暂无数据</label>
			           </div>]];
		    local show_div= document:getElementsByName("referencelist");
	             if show_div ~= nil and #show_div>0 then
	                 show_div[1]:setInnerHTML(div);
	             end
	         else
                      for key,value in pairs(transaction_list) do
            	          div = div..[[
            	          <tr class="trtd_h40_w320" border="1" onclick="details_page(']]..value["redEnvelopeNo"]..[[',']]..h_type(value["redEnvelopeType"])..[[')">
                	              <td class="trtd_h40_w320">
                                  <label class="label_l15_fs14_c999999">]]..format_date_year_month(value["transactionDate"])..[[</label>
                                  <label class="label_r140_fs14_c999999">]]..format_money(value["transactionAmount"])..[[</label>
                                  <label class="label_r35_fs14_c999999">]]..format_type(value["redPacketType"])..[[</label>
                                  <label class="public_line_b1"></label>
                                </td>
                            </tr>]];
                      end
                      local div_callback ="";
                      div_callback = div..[[</table></div>]];
                      local show_div= document:getElementsByName("referencelist");
	             if show_div ~= nil and #show_div>0 then
	                 show_div[1]:setInnerHTML(div_callback);

	             end
	        end
	        ert.channel:hide_loading();
	    else
                 window:alert("网络请求失败");
                 return;
             end
	end

	--向下滑动空方法
	function upUrl()
		
	end

	--红包列表查询--falg判断是否是首次进入
	function reference_list(falg)
		local endDate = ert("#dateEnd"):val();
		endDate = string.gsub(endDate,"-","");
	    local startDate;
	    if falg == "1" then
	        startDate = get_early_month(endDate);
	       -- window:alert(endDate)
	        ert("#dateStart"):attr("value",format_date(startDate));
	        location:reload();
	    elseif falg == "2" then
	    	startDate = change_date(ert("#dateStart"):val());
	    	pageNumber = "1";
	    	div = [[<div name="referencelist" class="div_t215_h250_w320" border="0">
            			<table class="tabel_w320_bgFFFFFF,height190" type="scrolltable" nexturl="reference_list('3')" preurl ="upUrl()" border="0">]];
             else
             	startDate = change_date(ert("#dateStart"):val());
	    end
	    

	    --获取当前日期
	    local date = os.date("%Y%m%d");

	    --日期校验
	    if startDate > date or endDate > date then
	    	window:alert("选择日期不能早于当前日期");
		return;
	    end

	    if startDate > endDate then
	    	window:alert("开始日期必须早于结束日期");
		return;
	    end

	    local after_a_year_years = string.sub(endDate,1,4);
             after_a_year_years = after_a_year_years -2;
             local after_date = tonumber(after_a_year_years .. string.sub(endDate,5,8));
             if after_date > tonumber(startDate) then
                   window:alert("起止日期的跨度不能超过两年");
                   return;
             end
	    	   
             if recordCountNum ~= "0" and tonumber(pageNumber) > tonumber(math.ceil(recordCountNum/countNum)) then
	         window:alert("最后一页");
	         return;
             else
             	ert.channel:hide_all();
             	ert.channel:show_loading();
                  ert.channel:next_page("ebank_red_packet","EBRP0001",{id="ebank_red_packet",tranCode="EBRP0001",startDate=startDate,endDate=endDate,pageNumber=pageNumber,countNum=countNum},{request_callback=refurbish_callback});
             end
	end
	reference_list("1");

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EBRP0001"));