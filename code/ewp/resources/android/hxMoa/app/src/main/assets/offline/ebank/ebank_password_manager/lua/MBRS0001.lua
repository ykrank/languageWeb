(function( this )
	ebank_public.allSuit:physicalkey_back();
	-- 标识
	local flag = 0;
	-- 获取验证码的标识
	local OTPSEQNO1 ="";
	local OTPSEQNO2 ="";

	local num_1 = "";
	local timer2 = "";
	local timer3 = "";
	local second  ;
	local second2 ; 

	--点击获取的标志
	 msg_check1 = "";
	 msg_check2 = "";
---------------------------------------------------------------------------------
	function get_msg_code(num)
		num_1 = num;
		local mobile = "";
		local flag = nil;
		if num == 1 then 
			msg_check1 = true
           mobile = ert("#MOBILE"):val();
          
        else
        	msg_check2 = true
        	mobile = ert("#MOBILE2"):val();
        	flag = "1";
        end
          if mobile == "" then
             window:alert("请输入手机号码");
             return;
          end
          if flag == "1" then
          	if mobile == ert("#MOBILE"):val() then
             window:alert("新手机与原手机号码一致，请重新输入");
             ert("#MOBILE2"):attr("value","");
             return;
             end
             if string.len(mobile) ~= 11 then
             	window:alert("手机号码位数不对");
             	return
             end
          end
          ert.channel:first_page("ebank_public","NOTE0000",{id='ebank_public',tranCode='NOTE0000',mobile=mobile,flag=flag},{request_callback=msg_code_callback})
    end

      function msg_code_callback(response)

          local code = response["responseCode"];
          if code == 200 then

              local json_data = response["responseBody"];
              local json_tbl = json:objectFromJSON(json_data);
              local smsState = json_tbl["body"]["smsState"];
              if smsState == "00" then
                  
                  if num_1 == 1 then
                  	OTPSEQNO1 = json_tbl["body"]["OTPSEQNO"];
                  	second = tonumber(120);
                    timer2 = timer:startTimer(1, 1, showSec, 0);
                  else
                  	OTPSEQNO2 = json_tbl["body"]["OTPSEQNO"];
                  	second2 = tonumber(120);
                    timer3 = timer:startTimer(1, 1, showSec2, 0);
                  end
              elseif smsState == "02" then
                  window:alert("该手机号已被注册，请重新输入");
              return;

              else
                  window:alert("发送失败");
                  return;
              end
          else
          end
      end


      function showSec()
          if second > 1 then
              second = second - 1;
              local ctrl = document:getElementsByName("msg_code_1");
              if ctrl and #ctrl > 0 then
                ert("#msg_code_1"):attr("value",tostring(second).."s");
                ert("#msg_code_1"):css("background-image","note_click.png");
                ert("#msg_code_1"):attr("enable","false");
              else
                timer:stopTimer(timer2);
                timer2=nil;
                second=nil;
                return;
              end
          else
              timer:stopTimer(timer2);
              ert("#msg_code_1"):attr("value","重新发送");
              ert("#msg_code_1"):attr("enable","true");
              ert("#msg_code_1"):css("background-image","btn_bg.png");
          end
      end

      function showSec2()
          if second2 > 1 then
              second2 = second2 - 1;
              local ctrl = document:getElementsByName("msg_code_2");
              if ctrl and #ctrl > 0 then
                ert("#msg_code_2"):attr("value",tostring(second2).."s");
                ert("#msg_code_2"):css("background-image","note_click.png");
                ert("#msg_code_2"):attr("enable","false");
              else
                timer:stopTimer(timer3);
                timer3=nil;
                second2=nil;
                return;
              end
          else
              timer:stopTimer(timer3);
              ert("#msg_code_2"):attr("value","重新发送");
              ert("#msg_code_2"):attr("enable","true");
              ert("#msg_code_2"):css("background-image","btn_bg.png");
          end
      end

      function msg_onblur( num )
	     if num == 1 and msg_check1 ~= true then
          window:alert("请先获取短信验证码");
          return;
        end
        if num == 2 and msg_check2~=true then
          window:alert("请先获取短信验证码");
          return
        end
      end

---------------------------------------------------------------------------------
	function switch()
		local switch = document:getElementsByName("switch1");
		local checked = switch[1]:getPropertyByName("checked");

		local tb1 = document:getElementsByName("tb1");
		local tb2 = document:getElementsByName("tb2");
		local title2 = document:getElementsByName("title2");

		if checked == "true" then
			--window:alert("switch开启");
			title2[1]:setPropertyByName("value","您正在修改手机号码!")
			tb2[1]:setStyleByName("display","none");
			tb1[1]:setStyleByName("display","block");
			--location:reload();
			flag = 1;
		else
			--window:alert("switch关闭");
			title2[1]:setPropertyByName("value","您正在重置手机号码!")
			tb1[1]:setStyleByName("display","none");
			tb2[1]:setStyleByName("display","block");
			--location:reload();
			flag = 0;
		end;
		location:reload();
	end

    function click()
    local post_body = nil;
    local next_channelId = "ebank_password_manager";
    local next_trancode = "MBRS0002";

		    if flag == 1 then

		    	local MOBILE = ert("#MOBILE"):attr("value");
				-- 验证码 ----------------------
				local OTPNO = ert("#OTPNO"):attr("value");
				if OTPNO == "" then
					 window:alert("验证码不能为空！");
					 return
				end;
				if string.len(OTPNO) ~= 6 then
					window:alert("验证码位数不对!");
					return
				end;
				if string.find(OTPNO,[[%D]])~= nil and string.find(OTPNO,"[%D]")~= 0 then
					window:alert("验证码只能输入数字！"..OTPNO);
					return
				end;
				--------------------------------------------
				local MOBILE2 = ert("#MOBILE2"):attr("value");
				local OTPNO2 = ert("#OTPNO2"):attr("value");

				if string.len(MOBILE2) ~= 11 then
					window:alert("手机号码位数不对");
					return
				end;
				-- 验证码2 ----------------------
				if OTPNO2 == "" then
					 window:alert("验证码不能为空！");
					 return
				end;
				if string.len(OTPNO2) ~= 6 then
					window:alert("验证码2位数不对");
					return
				end;
				if string.find(OTPNO2,[[%D]])~= nil and string.find(OTPNO2,"[%D]")~= 0 then
					window:alert("验证码只能输入数字！");
					return
				end;
			   	post_body = {id=next_channelId,tranCode=next_trancode,MOBILE=MOBILE,OTPNO1=OTPNO, OTPSEQNO1=OTPSEQNO1, MOBILE2=MOBILE2,OTPNO2=OTPNO2,OTPSEQNO2=OTPSEQNO2,flag=flag};
			else  
				local customerName = ert("#customerName"):val();
				if  customerName == "" then
		         window:alert("请输入姓名");
		         return
		      	end
			    --身份证-------------------------
			    local IDNO = ert("#IDNO"):val();
			    if IDNO == "" then 
				   window:alert("您还未输入身份证，请输入");
				elseif string.len(IDNO) ~= 18 then 
			        window:alert("您输入的身份证位数不正确，请重新输入");
			    elseif input_check_chinese(IDNO) == 0 then
			    --不需要操作
			    end
				----------------------------------
			    -- 交易密码 ----------------------
			    local tranPassword = ert("#tranPassword"):val(); 
			    if string.len(tranPassword) ~= 6 then
					 window:alert("交易密码只能输入6位数字！");
					 return
				end;
				if string.find(tranPassword,[[%D]])~= nil and string.find(tranPassword,"[%D]")~= 0 then
					window:alert("交易密码只能输入数字！");
					return
				end
				
				--------------------------------------------
				local MOBILE2 = ert("#MOBILE2"):attr("value");
				local OTPNO2 = ert("#OTPNO2"):attr("value");
				if string.len(MOBILE2) ~= 11 then
					window:alert("手机号码位数不对");
					return
				end;
				--验证码2 ----------------------
				if OTPNO2 == "" or OTPNO2 ==nil then
					 window:alert("验证码不能为空！"..OTPNO2);
					 return
				end;
				if string.len(OTPNO2) ~= 6 then
					window:alert("验证码位数不对"..OTPNO2);
					return
				end;
				if string.find(OTPNO2,[[%D]])~= nil and string.find(OTPNO2,"[%D]")~= 0 then
					window:alert("验证码只能输入数字！");
					return
				end;
				----------------------------------
				tranPassword = encrypt:desRsa(tranPassword);
				post_body = {id=next_channelId,tranCode=next_trancode,customerName=customerName,IDNO=IDNO,tranPassword=tranPassword,MOBILE2=MOBILE2,OTPNO2=OTPNO2,OTPSEQNO2=OTPSEQNO2,flag=flag};
			end
        ert.channel:next_page(next_channelId, next_trancode, post_body);
    end;
end)(ert.channel:get_page("ebank_password_manager","MBRS0001"));
