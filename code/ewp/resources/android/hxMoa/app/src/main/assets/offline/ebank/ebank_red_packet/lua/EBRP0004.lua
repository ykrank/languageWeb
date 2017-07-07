(function(this)
	--test
	--local test11 = ert("#test11"):val();

	--格式化
	function h_type(type)
	    if type == "0" then
	    	type = "普通红包" ;
	    else
	    	type = "随机红包";
	    end
	    return type;
	end

	--查询全部红包传空
	function change_type(redEnvelopeType)	
	    if tostring(redEnvelopeType) == "2" or tostring(redEnvelopeType) == "3" then
	    	return "";
	    else
	    	return redEnvelopeType;
	    end
	end

	--如果总页数返回""或者nil-->"0"
	function change_CountNum (recordCountNum)
	    if recordCountNum == "" or recordCountNum == nil then
	    	recordCountNum = "0";
	    end
	    return recordCountNum;
	end

	--页码pageNum 
	local pageNum = "1";
	--每页显示记录数countNum
	local countNum = "10";
	--总条数
	local recordCountNum = "1";

	local div = [[<div name="redpacketlist" class="div_t34_w320,bgFFFFFF" border="0">
          			<table class="tabel_t0_w320_bgFFFFFF,height460" type="scrolltable" nexturl="red_packet_choice()" preurl ="upUrl()" border="0">]];

    --防止重复调用接口
    local check_div = ""

	--回调、刷新红包数据
	function redpacketType_callback(response)
	   local code = response["responseCode"];
            if code == 200 then
	       local json_data = response["responseBody"];
    	       local json_tbl = json:objectFromJSON(json_data);
	       local redEnvelope_list = json_tbl["body"]["redEnvelopeList"] or {};

	       recordCountNum = change_CountNum(json_tbl["body"]["recordCountNum"]);
                pageNum = pageNum + 1;

                --判断是否有数据
                 if recordCountNum == "0" then
			    	    local div = [[<div name="redpacketlist" class="div_t34_w320" border="0">
					                 <label class="label_l136_t10_w320">暂无数据</label>
					               </div>]];
					    local show_div= document:getElementsByName("redpacketlist");
		             if show_div ~= nil and #show_div>0 then
		                 show_div[1]:setInnerHTML(div);
		             end
	   		     else
                      for key,value in pairs(redEnvelope_list) do
                  	    if value["rotalNum_3015T"] == nil or value["rotalNum_3015T"]=="" then
                  		value["rotalNum_3015T"] = 0
                  	    end
                  	    if value["remainNum_3015T"] == nil or value["remainNum_3015T"]=="" then
                  		    value["remainNum_3015T"] = 0
                  	    end
		              	
		              	    --window:alert(tonumber(value["rotalNum_3015T"]) - tonumber(value["remainNum_3015T"]))
		                        local claimNum = tonumber(value["rotalNum_3015T"]) - tonumber(value["remainNum_3015T"]);
		                        --已领取红包个数
		                        if value["rotalNum_3015T"] == 0 and value["remainNum_3015T"] == 0 then
			              		    div = div..[[
			        	      	    <tr class="trtd_h50_w320" hide="true" >
			                          <td class="trtd_h50_w320">
			                            <label class="label_t10_fs12_c4D4D4D,public_left">]]..h_type(value["redEnvelopeType"])..[[</label>]];
			              	    else
			        	          div = div..[[
			        	          <tr class="trtd_h50_w320" onclick="details_page(']]..value["redEnvelopeNo"]..[[',']]..h_type(value["redEnvelopeType"])..[[')">
			                          <td class="trtd_h50_w320">
			                            <label class="label_t10_fs12_c4D4D4D,public_left">]]..h_type(value["redEnvelopeType"])..[[</label>]];
		                        end

		                            if value["amount"] == "1" then
		                                div = div..[[
		                                <label class="label_t10_fs12_cF85050,public_right">]]..format_money(value["totalAmount"])..[[元</label>]];
		                            else
		                                div = div..[[
		                                <label class="label_t10_fs12_cF85050,public_right">]]..format_money(value["amount"])..[[元</label>]];
		                            end
		                            div = div..[[
		                            <label class="label_t28_fs12_c4D4D4D,public_left,color999999">]]..format_date_year_month(value["startDate"])..[[</label>
		                            <label class="label_t28_fs12_c4D4D4D,public_right,color999999">]]..claimNum.."/"..value["rotalNum_3015T"]..[[</label>
		                            <label class="public_line_b1,width290,public_left"></label>
		                          </td>
		                        </tr>]];
                      end

                      local div_callback ="";
                      check_div = div;
                      div_callback = div..[[</table></div>]];
                      local show_div= document:getElementsByName("redpacketlist");
		             if show_div ~= nil and #show_div>0 then
		                 show_div[1]:setInnerHTML(div_callback);
		             end
	    	    end
	        location:reload();
	    else
                 window:alert("网络请求失败");
                 return;
             end
	end


	--向下滑动空方法
	function upUrl()
		
	end

	--选择红包状->redEnvelopeType->"2"(全部红包),"0"(普通红包),"1"(随机红包)
	function red_packet_choice(redEnvelopeType)
		
		    if redEnvelopeType == "2" then
		    	ert("#all_redpacket"):css("color","#F85050");
		    	ert("#stabdard_redpacket"):css("color","#666666");
		    	ert("#random_redpacket"):css("color","#666666");
		    	ert("#all_redpacket"):attr("enable","false");
	            ert("#stabdard_redpacket"):attr("enable","true");
	            ert("#random_redpacket"):attr("enable","true");
		    	-- ert("#have_redpacket"):css("color","#666666");
		    	-- ert("#dnf_redpacket"):css("color","#666666");
		    	--清空数据
		    	div = [[<div name="redpacketlist" class="div_t34_w320,bgFFFFFF" border="0">
	          			<table class="tabel_t0_w320_bgFFFFFF,height460" type="scrolltable" nexturl="red_packet_choice(]]..redEnvelopeType..[[)" preurl ="upUrl()" border="0">]];
	          	pageNum = "1";
          	elseif redEnvelopeType == "3" then
	    	ert("#all_redpacket"):css("color","#F85050");
	    	ert("#stabdard_redpacket"):css("color","#666666");
	    	ert("#random_redpacket"):css("color","#666666");
	    	ert("#all_redpacket"):attr("enable","false");
            ert("#stabdard_redpacket"):attr("enable","true");
            ert("#random_redpacket"):attr("enable","true");
	    	-- ert("#have_redpacket"):css("color","#666666");
	    	-- ert("#dnf_redpacket"):css("color","#666666");
	    	--清空数据
	    	div = [[<div name="redpacketlist" class="div_t34_w320,bgFFFFFF" border="0">
          			<table class="tabel_t0_w320_bgFFFFFF,height460" type="scrolltable" nexturl="red_packet_choice(]]..redEnvelopeType..[[)" preurl ="upUrl()" border="0">]];
	          	pageNum = "1";
		    elseif redEnvelopeType == "0" then
		    	ert("#all_redpacket"):css("color","#666666");
		    	ert("#stabdard_redpacket"):css("color","#F85050");
		    	ert("#random_redpacket"):css("color","#666666");
		    	ert("#all_redpacket"):attr("enable","true");
	            ert("#stabdard_redpacket"):attr("enable","false");
	            ert("#random_redpacket"):attr("enable","true");
		    	-- ert("#have_redpacket"):css("color","#666666");
		    	-- ert("#dnf_redpacket"):css("color","#666666");
		    	--清空数据
		    	div = [[<div name="redpacketlist" class="div_t34_w320,bgFFFFFF" border="0">
	          			<table class="tabel_t0_w320_bgFFFFFF,height460" type="scrolltable" nexturl="red_packet_choice(]]..redEnvelopeType..[[)" preurl ="upUrl()" border="0">]];
	          	pageNum = "1";
		    elseif redEnvelopeType == "1" then
		    	ert("#all_redpacket"):css("color","#666666");
		    	ert("#stabdard_redpacket"):css("color","#666666");
		    	ert("#random_redpacket"):css("color","#F85050");
		    	ert("#all_redpacket"):attr("enable","true");
	            ert("#stabdard_redpacket"):attr("enable","true");
	            ert("#random_redpacket"):attr("enable","false");
		    	-- ert("#have_redpacket"):css("color","#666666");
		    	-- ert("#dnf_redpacket"):css("color","#666666");
		    	--清空数据
		    	div = [[<div name="redpacketlist" class="div_t34_w320,bgFFFFFF" border="0">
	          			<table class="tabel_t0_w320_bgFFFFFF,height460" type="scrolltable" nexturl="red_packet_choice(]]..redEnvelopeType..[[)" preurl ="upUrl()" border="0">]];	
	          	pageNum = "1";
		    end
		    --获取红包交易类型
		    local req_value = ert.channel:get_request("ebank_red_packet","EBRP0004");
		      
             if recordCountNum ~= "0" and tonumber(pageNum) > tonumber(math.ceil(recordCountNum/countNum)) then
		        window:alert("最后一页");
		        return;
             else
             	--window:alert(change_type(redEnvelopeType)..":"..req_value["tranType"]..":"..pageNum..":"..countNum)
             	
                 ert.channel:next_page("ebank_red_packet","EBRP0004",{id = "ebank_red_packet",tranCode = "EBRP0004",redEnvelopeType = change_type(redEnvelopeType),tranType=req_value["tranType"],pageNum=pageNum,countNum=countNum},{request_callback=redpacketType_callback});
                
             end
	end
	red_packet_choice("3");

	--红包详情页面--redEnvelopeNo:红包编号,redEnvelopeType:红包类型
	function details_page(redEnvelopeNo,redEnvelopeType)
		--window:alert(redEnvelopeNo.."-->"..redEnvelopeType)
	    ert.channel:next_page("ebank_red_packet","EBRP0005",{id = "ebank_red_packet",tranCode = "EBRP0005",redEnvelopeNo=redEnvelopeNo,redEnvelopeType=redEnvelopeType});
	end

	ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EBRP0004"));