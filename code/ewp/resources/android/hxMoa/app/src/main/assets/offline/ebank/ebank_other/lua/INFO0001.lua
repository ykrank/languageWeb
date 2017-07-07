(function( this )
		ebank_public.allSuit:physicalkey_back();
		
		local data = ert.channel:get_response("ebank_other","INFO0001");

		local json_tbl = json:objectFromJSON(data) or {};
   		local t_body = json_tbl["body"] or {};
		local dataset1 = t_body["info_list"] or {};

		--table中记录的条数
		local table_n = 10;
		--table显示的最多记录数
		local limit = 2;
		--table显示记录的起始索引
		local startIndex = 1;
		--table显示记录的结束索引
		local endIndex = startIndex+limit-1;
		local tr_info = "";


		--拼接table报文
		function detailList(coll_lists,startIndex,endIndex)
		tr_info = "";
			--for i=startIndex,3,1 do
			for key,value in pairs(coll_lists) do
				local v_t = value["title"];
				local v_c = value["content"];
				local date = value["date"];
				v_d = string.sub(date, -5, -1)
				local v_c2 = string.gsub(v_c, "[%#~]", "");

				tr_info = tr_info..
				[[<tr onclick="click(']]..v_t..[[',']]..v_c..[[',']]..date..[[')">
				<td>
				<div class="div_w270_h70_l10" valign="middle"  border="0" >
				<img src="msg1.png" class="img_w25_h25_t20" />
				<label class="date_w40_fs14">]].. v_d..[[</label>
				<label class="label_w200_l60_fs14,t10">]].. v_t..[[</label>
				<label class="label_w200_l60_fs14,t31,color" numlines="2">　　]]..v_c2..[[</label>
				</div>
				<label class="public_line_b1"></label>
				</td>
				<td>
				<div class="div_w40_h70_r10" valign="middle" border="0" >
				<img class="right,w7_h12" src="right_jt.png" />
				</div>
				<label class="public_line_b1"></label>
				</td>
				</tr>]];
			end
			return tr_info;
		end

		--setHtml
		function initial(coll_lists,startIndex,endIndex)
			tr_info=detailList(coll_lists,startIndex,endIndex);
			local table_content = [[<table type="scrolltable" class="public_main_div" border="0" name="table_detail" nextUrl="getMore()">]]..tr_info..[[</table>]];
			local table_detail = document:getElementsByName("table_detail");
			if table_detail and #table_detail>0 then
				table_detail[1]:setInnerHTML(table_content);
			end
		end

			--获得前一页
		function getPre()
				endIndex = startIndex-1;
				startIndex = endIndex-limit+1;
				if startIndex>=1 then
					initial(dataset1,startIndex,endIndex);
				else
					window:alert("已经是第一页了!");
				end
		end

			--获得后一页
		function getMore()
				startIndex = startIndex+limit;
				endIndex = startIndex+limit-1;
				if startIndex<=table_n then 
					if endIndex<=table_n then
					initial(dataset1,startIndex,endIndex);
				else
					endIndex = table_n;
					initial(dataset1,startIndex,endIndex);
				end
			else
				startIndex = startIndex-limit;
				window:alert("已经是最后一页了!");
			end
		end

		initial(dataset1,startIndex,endIndex);


		function click(title,cont,date)
			-- file:write("info.txt",cont);
			local new_str = string.gsub(cont, "[%#]", "<br />　　 ");

			new_str = string.gsub(new_str, "[%~?]", "<![CDATA[  ");
			new_str = string.gsub(new_str, "[%^]", "  ]]>");
           
           -- window:alert(new_str)

			local next_channelId = "ebank_other";
			local next_trancode = "INFO0002";
			local context = {title=title,cont=new_str,date=date};
			ert.channel:next_page(next_channelId, next_trancode, {},{just_page=true,context=context});    	
			end;

		    --init()

    end)(ert.channel:get_page("ebank_other","MANU0001"));
