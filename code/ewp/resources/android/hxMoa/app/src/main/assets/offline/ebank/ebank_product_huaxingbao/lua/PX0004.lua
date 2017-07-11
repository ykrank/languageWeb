--安卓物理返回键代码
ebank_public.allSuit:physicalkey_back();
--日期控件初始化
dateEnd = ert("#dateEnd"):val();
dateEnd = string.gsub(dateEnd,"-","");
dateStart = get_early_month(dateEnd);
show = "1";
--window:alert(dateEnd);
--ert("#dateStart"):attr("value",dateStart);

--获取当前日期 
local time=os.date("*t",os.time());
local year = time.year;
local month = time.month;
local day = time.day;
local today = year..month..day;

--记录查询日期 
local dateEnd_r_41 = dateEnd;
local dateStart_r_41 =dateStart;
local dateEnd_r_42 = dateEnd;
local dateStart_r_42 =dateStart;
--刷新数据初始化
local div_callback42=[[<div name="mindro" class="div_w320" border="0">
        			<table class="tabel_w320_bgFFFFFF" border="0" type="scrolltable" nexturl="get_date_form_PX00041_PX00042('4')"  preurl ="show2()" name="main_table">]];
local div_callback41=[[<div name="mindro" class="div_w320" border="0">
        			<table class="tabel_w320_bgFFFFFF" border="0" type="scrolltable" nexturl="get_date_form_PX00041_PX00042('3')"  preurl ="show2()" name="main_table">]];

--避免数据得复调用接口获取数据标志
local ask_stop_adate_PX00041 = '';
local ask_stop_adate_PX00042 = '';

--页码pageNum 
local pageNum_41 = "1";
local pageNum_42 = "1";
--每页显示记录数countNum
local countNum = "10";

--暴力测试防闪退标志定义
local fan_san41 = "s1";
local fan_san42 = "s1";

--回调标志判断
local callback_flag = "";

local recordCountNum_41='1';
local recordCountNum_42='1';

function get_date_px0004()
	channelId = "ebank_product_huaxingbao";
	trancode = "PX0004";
	local post_boby = {id = channelId,tranCode = trancode};
	ert.channel:next_page(channelId, trancode, post_boby,{show_loading=false,request_callback=login_callback_px0004});
end

function login_callback_px0004(response)
	local code = response["responseCode"];
    if code == 200 then
		local data_px0004 = response["responseBody"] or {};
	    local table=json:objectFromJSON(data_px0004) or {};
	    table["body"] = table["body"] or {};

		local income = table["body"]["income"];
		local profit = table["body"]["profit"];
		local balance = table["body"]["balance"];

		if (income == "") then
		  income = "0";
		end
		if (profit == "") then
		  profit = "0";
		end
		if (balance == "") then
		  balance = "0";
		end
		ert("#income"):attr("value",format_money(income));
		ert("#profit"):attr("value",format_money(profit));
		ert("#balance"):attr("value",format_money(balance));
		change_color("2");
	else
		window:alert("网络请求失败");
        return;
	end
end
--页面跳转方法
--flag 5赎回  2申购
function turn_page_px0002_px0005(flag,n1,m2)
	finsh_continue_bug_flag = nil;
	channelId = "ebank_product_huaxingbao";
	if flag == "5"then
		trancode = "PX0005";
	elseif flag == "2" then
		trancode = "PX0002";
	else
	end
	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2};
	ert.channel:first_page(channelId, trancode, post_boby);
end

--增加交易日期的格式
function tranDate(str)
	return string.sub(str, 1, 4)..[[-]]..string.sub(str, 5, 6)..[[-]]..string.sub(str, 7, 8)
end

--交易按扭变换
local singe_show = "";--防止重复点击标志判断
function change_color(n)
	if(n=="1")then--防止重复点击标志判断
		if fan_san41 == 's1' then
			fan_san41 ="s0";
			if singe_show == "1" then
			else
				singe_show = "1";
				get_date_form_PX00041_PX00042("2");
			end
			ert("#div_1"):css("background-color","#F7F7F7");
			ert("#lab_1"):css("color","#f85050");
			ert("#div_2"):css("background-color","#ffffff");
			ert("#lab_2"):css("color","#999999");
		--	ert("#lab_31"):attr("value","理财本金(元)");
		--	ert("#lab_32"):attr("value","交易日期");
		--	ert("#lab_33"):attr("value","实际收益(元)");

			ert("#lab_31"):attr("value","交易日期");
			ert("#lab_32"):attr("value","交易金额");
			ert("#lab_33"):attr("value","交易类型");

			ert("#dateStart"):attr("value",format_date(dateStart_r_42));
			ert("#dateEnd"):attr("value",format_date(dateEnd_r_42));
			--window:alert("dateEnd_r_42："..dateEnd_r_42 .. "//dateStart_r_42:"..dateStart_r_42);
		end
	else
			--window:alert("dateEnd_r_41："..dateEnd_r_41 .. "//dateStart_r_41:"..dateStart_r_41);

		if fan_san42 =="s1" then
			fan_san42 = "s0";
			if singe_show == "2" then--防止重复点击标志判断
			else
				singe_show = "2";
				get_date_form_PX00041_PX00042("1");
			end
			ert("#div_1"):css("background-color","#ffffff");
			ert("#lab_1"):css("color","#999999");
			ert("#div_2"):css("background-color","#F7F7F7");
			ert("#lab_2"):css("color","#f85050");
			ert("#lab_31"):attr("value","交易日期");
			ert("#lab_32"):attr("value","交易金额");
			ert("#lab_33"):attr("value","交易类型");

			ert("#dateStart"):attr("value",format_date(dateStart_r_41));
			ert("#dateEnd"):attr("value",format_date(dateEnd_r_41));
		end;
	end;
end

function get_date_form_PX00041_PX00042(num)
	--window:alert(pageNum_41);
	if(show ~= "1")then
		ert.channel:show_loading();
	end;
	show = "0";
	callback_flag = num;
	--页码pageNum
	--每页显示记录数countNum
	if num == "1" then
		if ask_stop_adate_PX00041 ~= div_callback41 then
			local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00041',pageNum = pageNum_41,countNum=countNum,startDate=dateStart_r_41,endDate=dateEnd_r_41};
    		ert.channel:next_page("ebank_product_huaxingbao","PX00041",post_boby,{show_loading=false,request_callback=login_callback2});
		else
    		show_innerHTML();
		end
	elseif num == "2" then
        --交易明细 局部刷新回调 防止数据重载判断
		--window:alert("pageNum_41: //"..pageNum_41.."//recordCountNum_41: //"..recordCountNum_41.."//countNum//"..countNum);

		if ask_stop_adate_PX00042 ~= div_callback42 then
    		local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00042',pageNum = pageNum_42,countNum=countNum,startDate=dateStart_r_42,endDate=dateEnd_r_42};
    		ert.channel:next_page("ebank_product_huaxingbao","PX00042",post_boby,{show_loading=false,request_callback=login_callback2});
    	else
    		show_innerHTML();
    	end
    elseif num == "3" then
    	--window:alert(tonumber(math.ceil(tonumber(recordCountNum_41)/countNum)).."pageNum_41"..pageNum_41);
		if tonumber(pageNum_41) > tonumber(math.ceil(tonumber(recordCountNum_41)/countNum)) then
			window:alert("最后一页");
    		ert.channel:hide_loading();	
			return;
		else
			local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00041',pageNum = pageNum_41,countNum=countNum,startDate=dateStart_r_41,endDate=dateEnd_r_41};
    	--	window:alert(post_boby);
    		ert.channel:next_page("ebank_product_huaxingbao","PX00041",post_boby,{show_loading=false,request_callback=login_callback2});
    	end;
    elseif num == "4" then
    	--收益明细 局部刷新回调
    	--window:alert("pageNum_42: //"..pageNum_42.."//recordCountNum_42: //"..recordCountNum_42.."//countNum//"..countNum.."//startDate"..startDate.."//endDate"..endDate);
    	if tonumber(pageNum_42) > tonumber(math.ceil(tonumber(recordCountNum_42)/countNum)) then
			window:alert("最后一页");
    		ert.channel:hide_loading();
			return;
		else
			local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00042',pageNum = pageNum_42,countNum=countNum,startDate=dateStart_r_42,endDate=dateEnd_r_42};
    		ert.channel:next_page("ebank_product_huaxingbao","PX00042",post_boby,{show_loading=false,request_callback=login_callback2});
		end
    else
	end;
end

--收益明细 局部刷新回调
function login_callback2(response)
	local code = response["responseCode"];
    if code == 200 then
		local json_data = response["responseBody"] or {};
	    local json_tbl = json:objectFromJSON(json_data) or {};
		local transDetailList = json_tbl["body"]["transDetailList"] or {};

		if callback_flag == "2" or callback_flag == "4" then

			dateEnd_r_42 = dateEnd;
			dateStart_r_42 = dateStart;

			recordCountNum_42 = json_tbl["body"]["recordCountNum"];
			pageNum_42 = tonumber(pageNum_42) + 1;
			for k,v in pairs(transDetailList) do
			div_callback42=div_callback42..[[
				<tr class="tr_td40,bgf7f7f7">
		              <td class="tr_td40,bgf7f7f7">
		                <label class="w100_fs12,label_date">]]..tranDate(v['tranDate'])..[[</label>
	               		<label class="w100_fs12,label_money">]]..format_money(v['amount'])..[[</label>
	               		<label class="w100_fs12,label_tac">]]..v['tranType']..[[</label>
	               		<label class="public_line_b1"></label>
		                <label class="public_line_b1"></label>
		              </td>
		            </tr>
		        ]];
			end
			ask_stop_adate_PX00042 = div_callback42;
			show_innerHTML()
		else
			dateEnd_r_41 = dateEnd;
			dateStart_r_41 = dateStart;

			recordCountNum_41 = json_tbl["body"]["recordCountNum"];
			pageNum_41 = tonumber(pageNum_41) + 1;
			for k,v in pairs(transDetailList) do
			div_callback41=div_callback41..[[
				<tr class="tr_td40,bgf7f7f7">
	              <td class="tr_td40,bgf7f7f7">
	                <label class="w100_fs12,label_date">]]..tranDate(v['tranDate'])..[[</label>
	                <label class="w100_fs12,label_money">]]..format_money(v['amount'])..[[</label>
	                <label class="w100_fs12,label_tac">]]..v['tranType']..[[</label>
	                <label class="public_line_b1"></label>
	              </td>
	            </tr>
	          ]];
	    	end
	    	ask_stop_adate_PX00041 = div_callback41;
			show_innerHTML();
		end;
	else
		window:alert("网络请求失败");
        return;
	end
end

function show_innerHTML()
	local div_callback3 ="";
	if callback_flag == "2" or callback_flag == "4" then
		div_callback3 = div_callback42 ..
	    [[
	      </table>
	      </div>
	    ]]; 
   	else
   		div_callback3 = div_callback41 .. [[
	      </table>
	      </div>
	    ]]; 
   	end
  	local show_div= document:getElementsByName("mindro");--获取div的name
	if show_div ~= nil and #show_div>0 then
	    show_div[1]:setInnerHTML(div_callback3);--div内容替换	end;
	end
	fan_san41 ="s1";
	fan_san42 ="s1";
    window:hide(12);
    ert.channel:hide_loading();	

end;


function alert_show()
	--日期是否有变动
	dateStart = ert("#dateStart"):val();
	dateEnd = ert("#dateEnd"):val();
	dateStart = string.gsub(dateStart,"-","");
	dateEnd = string.gsub(dateEnd,"-","");

	if dateStart > today then
		window:alert("选择日期不能早于当前日期");
		return;
	end;

	if dateEnd > today then
		window:alert("选择日期不能早于当前日期");
		return;
	end;

	if dateStart > dateEnd then
		window:alert("开始日期必须早于结束日期");
		return;
	end;

	local after_a_year_years = string.sub(dateEnd,1,4);
    after_a_year_years = after_a_year_years -2;
    local after_date = tonumber(after_a_year_years .. string.sub(dateEnd,5,8));
    if after_date > tonumber(dateStart) then
          window:alert("起止日期的跨度不能超过两年");
          return;
    end

	if callback_flag == "2" or callback_flag == "4" then
		if(dateStart == dateStart_r_42 and dateEnd == dateEnd_r_42)then
			--window:alert("起始日期与结束日期未改变");
			flash_div('42');
		else
			--window:alert("42不相同");
			flash_div('42');
		end
	else
		if(dateStart == dateStart_r_41 and dateEnd == dateEnd_r_41)then
			--window:alert("起始日期与结束日期未改变");
			flash_div('41');
		else
			--window:alert("41不相同");
			flash_div('41');
		end
	end
end

--日期变动刷新列表
function flash_div(flag)
	local dateStart = ert("#dateStart"):val();
	local dateEnd = ert("#dateEnd"):val();
	dateStart = string.gsub(dateStart,"-","");
	dateEnd = string.gsub(dateEnd,"-","");
	if flag == "41" then
		pageNum_41 = "1";
	 	div_callback41=[[<div name="mindro" class="div_w320" border="0">
        			<table class="tabel_w320_bgFFFFFF" border="0" type="scrolltable" nexturl="get_date_form_PX00041_PX00042('3')"  preurl ="show2()"  name="main_table">]];
		local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00041',pageNum = pageNum_41,countNum=countNum,startDate=dateStart,endDate=dateEnd};
    	ert.channel:next_page("ebank_product_huaxingbao","PX00041",post_boby,{request_callback=login_callback2});
	else
		pageNum_42 = "1";
	 	div_callback42=[[<div name="mindro" class="div_w320" border="0">
        			<table class="tabel_w320_bgFFFFFF" border="0" type="scrolltable" nexturl="get_date_form_PX00041_PX00042('4')"  preurl ="show2()" name="main_table">]];
		local post_boby = {id='ebank_product_huaxingbao',tranCode='PX00042',pageNum = pageNum_42,countNum=countNum,startDate=dateStart,endDate=dateEnd};
    	ert.channel:next_page("ebank_product_huaxingbao","PX00042",post_boby,{request_callback=login_callback2});        
	end;
end

get_date_px0004();
