 window:showContent("local:FullSLoading1.xml", 121);
function login_callback3_1(response)
  local code = response["responseCode"];
  if code == 200 then
  	local data = response["responseBody"];
    local table=json:objectFromJSON(data) or {};
    local productId = table["body"]["productId"];
    global_hx_rate = table["body"]["hx_rate"];
    global_zn_rate = table["body"]["zn_rate"];
    global_hx_profit = table["body"]["hx_profit"];
    --如果为nil，则重新赋值为"";
  	if global_hx_rate ==nil then
  		global_hx_rate = "";
  	end;

  	if global_zn_rate ==nil then
  		global_zn_rate = "";
  	end;

   	if global_hx_profit ==nil then
  		global_hx_profit = "";
  	end;

    local income = "";
    local rate = "";
    local profit = "";
    local amount = "";

      if productId == "0900200100202" then
          income = format_money(table["body"]["hx_income"]);
          rate = table["body"]["hx_rate"];
          profit = table["body"]["hx_profit"];
      elseif productId == "0900100200205" then
          income = format_money(table["body"]["zn_income"]);
          rate = table["body"]["zn_rate"];
          amount = table["body"]["zn_amount"];
      else
      end

      local tempNum = string.len(income);
      if rate == "" then
      	rate = "-";
      end
      if profit == "" then
      	profit = "-";
      end

      local contert = [[
  	    <div class="div_w320_t44_l0" border="0" name="main1">
          <div name="main" class="div_w320_h24_t0_bf8f8f8" id="main" border="0">
            <img src="ebank_product_recommend/images/notice.png" class="img_l15_w12_h12_t6"/>
            <label class="label_f24_c666666_t3_w266_l27" loop="true" id ="news" velocity="25"></label>
            <img src="ebank_product_recommend/images/noticeclose.png" class="img_r15_w12_h12_t6" onclick="close_welcome_news()"/>
          </div>
          <div border="0" class="div_t24_l0">]]
            if productId == "0900200100202" then
            	contert = contert .. [[ <label class="label_t15_w320_c66666_f21">华兴宝</label>
              <div border="0" class="div_t47_l0">
                <img src="ebank_product_recommend/images/databg.png" class="img_h234_w234_t0_l43"/>
                <label class="label_w320_t30,label_color">]]..rate..[[</label>
                <label class="label_w320_t51,label_color">七日年化收益率</label>
                <label class="label_w320_t148,label_color_w">]]..profit..[[</label>
                <label class="label_w320_t178,label_color_w">万份收益</label>
              </div>
              ]];
            elseif productId == "0900100200205" then
            	contert = contert .. [[ <label class="label_t15_w320_c66666_f21">智能存款</label>
              <div border="0" class="div_t47_l0">
                <img src="ebank_product_recommend/images/databg.png" class="img_h234_w234_t0_l43"/>
                <label class="label_w320_t30,label_color">]]..rate..[[</label>
                <label class="label_w320_t51,label_color">最高年化收益率(%)</label>
                <label class="label_w320_t148,label_color_w">]]..amount..[[</label>
                <label class="label_w320_t178,label_color_w">起购金额(元)</label>
              </div>
              ]];
            else
            end
            	contert = contert .. [[
                <label class="label_l15_f12_c666666_t296">已累计为客户创造收益</label>
                <div class="div_w320_h50" border="0">
                    <div class="div_t11_r30" border="0">]];
                    for i=1,tempNum,1 do
                      nuk = string.sub(income,i,i)
                      if nuk == "," or nuk =="." then
                      	contert = contert .. [[<label class="label_color">]]..nuk..[[</label>]]
                      else
                      	contert = contert .. [[<div class="div_line" border="0">
                          <div class="label_l8_t1,label_color" border="0">
                          <label>]]..nuk..[[</label>
                          </div>
                      </div>]]
                      end
                    end
                    contert = contert .. [[
                    </div>
                    <label class="lable_txt,label_color">元</label>
                </div>
            <input class="public_15_w290_h35,button_l15_h35" type="button" value="申购" border="0" onclick="turn_page_hxp(']]..productId..[[',']]..rate..[[',']]..profit..[[')"></input>
          </div>
        </div>
      ]];
      local show_div = document:getElementsByName("main1");
      if show_div ~= nil and # show_div>0 then
      	show_div[1]:setInnerHTML(contert);
      end
    local post_boby1 = {id='ebank_other',tranCode='INFO0001'};
  	ert.channel:first_page("ebank_other","INFO0001",post_boby1,{show_loading=false,request_callback=login_callback2});
    else
      window:alert("网络请求失败");
      return
    end
end
		ert.channel:next_page("ebank_product_recommend","LH0001",  {id="ebank_product_recommend",tranCode="LH0001"},{show_loading=false,request_callback=login_callback3_1});

--顶部公告代码	
	function login_callback2( response )
    local code = response["responseCode"];
    if code == 200 then
  		local data = response["responseBody"];
  		local table = json:objectFromJSON(data) or {};
  		table["body"] = table["body"] or {};
  		table["info_list"] = table["info_list"] or {};
  		
  		local news = table['body']["info_list"][1]["title"];
  		if news=="" then
  			news = "热烈祝贺华兴银行投融资平台正式上线！"
  		end
  		ert("#news"):attr("value",news);
      window:hide(121);
    else
      window:alert("网络请求失败");
      return;
    end;
	end
    
--关闭欢迎信息
function close_welcome_news()
	local div = [[<div name="main" class="div_w320_h24_t44_bf8f8f8" id ="main" border="0"></div>]];
    local show_div= document:getElementsByName("main");--获取div的name
        if show_div ~= nil and #show_div>0 then
            show_div[1]:setInnerHTML(div);--div内容替换
		end;
	ert("#main"):css("background-color","#EBEBEE");
end

--跳转推荐产品相应首页
function turn_page_hxp(num,n1,m2)
	if num == "0900200100202" then
		channelId = "ebank_product_huaxingbao";
		trancode = "PX0001";
	elseif  num == "0900100200205" then
		channelId = "ebank_product_deposit";
		trancode = "PD0001";
	else
	end
	local post_boby = {id = channelId,tranCode = trancode,rate_xp=n1,profit_xp=m2};
	ert.channel:first_page(channelId, trancode, post_boby);
end

ebank_public.allSuit:physicalkey_exit_back();