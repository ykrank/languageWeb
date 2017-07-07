globalSign = "2";
(function(this)

	local accBalance;
	--初始化
	function initview(response)
	    local json_data = response["responseBody"];

    	    local json_tbl = json:objectFromJSON(json_data) or {};
            json_tbl["body"] = json_tbl["body"] or {};

	    local CustName = json_tbl["body"]["custName"];
	    local EAccountNo = json_tbl["body"]["eAccountNo"];
	    local hxbIncome = json_tbl["body"]["hxbIncome"]or 0;
	    local znckIncome = json_tbl["body"]["znckIncome"] or 0;
	    local AccBalance = json_tbl["body"]["accBalance"] or 0;
	    accBalance = AccBalance;
	    local hxbProduct = json_tbl["body"]["hxbProduct"];
	    local hxbBalance = json_tbl["body"]["hxbBalance"] or 0;
	    local znckProduct = json_tbl["body"]["znckProduct"];
	    local znckBalance = json_tbl["body"]["znckBalance"] or 0;
	    local p2pkProduct = json_tbl["body"]["p2pkProduct"];
	    local p2pBalance = json_tbl["body"]["p2pBalance"] or 0;
	    local hbProduct = json_tbl["body"]["hbProduct"];
	    local hbBalance = json_tbl["body"]["hbBalance"] or 0;
	    
	    local AccIncome = tonumber(hxbIncome)+tonumber(znckIncome);
	    local TotalAssets = tonumber(AccBalance)+tonumber(hxbBalance)+tonumber(znckBalance)+tonumber(p2pBalance)+tonumber(hbBalance);

	    ert("#custName"):attr("value",CustName);
	    ert("#EAccountNo"):attr("value",EAccountNo);
	    ert("#fsize"):attr("value",format_money(TotalAssets));
	    ert("#AccBalance"):attr("value",format_money(AccBalance));
	    ert("#AccIncome"):attr("value",format_money(AccIncome));
	    ert("#hxbBalance"):attr("value",format_money(hxbBalance));
	    ert("#znckBalance"):attr("value",format_money(znckBalance));
	    ert("#p2pBalance"):attr("value",format_money(p2pBalance));
	    ert("#hbBalance"):attr("value",format_money(hbBalance));
    	    window:hide(121);
	end
	window:showContent("local:FullSLoading1.xml", 121);
	ert.channel:next_page("ebank_manage_asset","EMAS0001",{id="ebank_manage_asset",tranCode="EMAS0001"},{show_loading=false,request_callback=initview})


	--资金转入转出
	function next_money_into(signValue,AccBalance)
	    AccBalance = accBalance;
	    local CustName = ert("#custName"):val();
	    ert.channel:first_page("ebank_manage_asset","EMAS0002",{id = "ebank_manage_asset",tranCode = "EMAS0002",CustName=CustName,AccBalance=AccBalance,signValue=signValue});
	end

	--电子账户
	function e_wallet()
	    ert.channel:next_page("ebank_manage_asset","EMAS0004",{id = "ebank_manage_asset",tranCode = "EMAS0004"});
	end

	--华兴宝
	function turn_page_PX0004()
	    ert.channel:first_page("ebank_product_homepage","PH0001",  {id="ebank_product_homepage",tranCode="PH0001"},{show_loading=false,request_callback=login_callback_PX0004});
	end

	function login_callback_PX0004(response)
		local code = response["responseCode"];
        if code == 200 then
		    local json_data = response["responseBody"];
		    local json_tbl = json:objectFromJSON(json_data);
		    ert.channel:first_page("ebank_product_huaxingbao","PX0004",{id = "ebank_product_huaxingbao",tranCode = "PX0004"},{show_loading=false,request_callback=callback_PX0004});
		else
			window:alert("网络请求失败");
            return;
        end;
	end

	function callback_PX0004(response)
		local code = response["responseCode"];
        if code == 200 then
        	window:showContent("local:FullSLoading1.xml", 12);
	    	ert.channel:first_page("ebank_product_huaxingbao","PX0004",{id = "ebank_product_huaxingbao",tranCode = "PX0004"},{show_loading=false,just_page=true});
		else
			window:alert("网络请求失败");
            return;
        end;
	end

	--智能存款
	function trun_page_PD0004()
	    ert.channel:first_page("ebank_product_homepage","PH0001",  {id="ebank_product_homepage",tranCode="PH0001"},{show_loading=false,request_callback=login_callback_PD0004});
	end

	function login_callback_PD0004(response)
		local code = response["responseCode"];
        if code == 200 then
			local json_data = response["responseBody"];
			local json_tbl = json:objectFromJSON(json_data);
			global_zn_amount= json_tbl['body']['start_amount'];
	    	ert.channel:first_page("ebank_product_deposit","PD0004",{id = "ebank_product_deposit",tranCode = "PD0004"},{show_loading=false,request_callback=callback_PD0004});
		else
			window:alert("网络请求失败");
            return;
        end;
	end

	function callback_PD0004(response)
		local code = response["responseCode"];
        if code == 200 then
        	window:showContent("local:FullSLoading1.xml", 12);
	    	ert.channel:first_page("ebank_product_deposit","PD0004",{id = "ebank_product_deposit",tranCode = "PD0004"},{show_loading=false,just_page=true});
		else
			window:alert("网络请求失败");
            return;
        end;
	end
	--P2P
	function next_P2P()
	    ert.channel:first_page("ebank_P2P","EBAP0001",{id = "ebank_P2P",tranCode = "EBAP0001"});
	end

	--我的红包
	function next_red_packet()
	    ert.channel:first_page("ebank_red_packet","EBRP0001",{id = "ebank_red_packet",tranCode = "EBRP0001"});
	end

	--资产配置
	function asset_allocation()
	    ert.channel:first_page("ebank_manage_asset","EMAS0005",{id = "ebank_manage_asset",tranCode = "EMAS0005"});
	end

	ebank_public.allSuit:physicalkey_exit_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0001"));