(function(this)
        
	      function change_codevalue()
		      local node_fomat = ert("#node"):val();
		      if string.sub(node_fomat, 1, 1) == "+" or string.sub(node_fomat, 1, 1) == "-" then
		          ert("#node"):attr("value","");
		      end
		  end
          
          local th_account_a="";
          local choicebakn2_a="";
		  function this.get_msg_code()
		        th_account_a = ert("#accno"):val();

	            choicebakn2_a = ert("#choicebakn2"):val();
	            if choicebakn2_a == "" then
	             	window:alert("请选择开户行");
		            return;
	            end
                local i = string.find(choicebakn2_a, "_");
                local bankid = string.sub(choicebakn2_a,1,i-1);

		        ert.channel:first_page("ebank_tied_card","BOUND022",{id='ebank_tied_card',tranCode='BOUND022',payerAccount=th_account_a,payerBankNo=bankid},{request_callback=msg_code_callback})
		  end
          
          local agreementId;
		  function msg_code_callback(response)
		      local code = response["responseCode"];
		      if code == 200 then
		          local json_data = response["responseBody"];
		          local json_tbl = json:objectFromJSON(json_data) or {};
		          json_tbl["body"]=json_tbl["body"] or {};
		          local flag = json_tbl["body"]["flag"];
		          agreementId = json_tbl["body"]["agreementId"];
		          if flag == "0" then
		              second = tonumber(120);
		              timer2 = timer:startTimer(1, true, showSec, 0);
		          else
		              window:alert("发送失败");
		              return;
		          end
		      end
		  end

		  function showSec()
		      if second > 1 then
		          second = second - 1;
		          local ctrl = document:getElementsByName("msg_code");
		          if ctrl and #ctrl > 0 then
		            ert("#msg_code"):attr("value",tostring(second).."s");
		            ert("#msg_code"):css("background-image","note_click.png");
		            ert("#msg_code"):attr("enable","false");
		          else
		            timer:stopTimer(timer2);
		            timer2=nil;
		            second=nil;
		            return;
		          end
		      else
		          timer:stopTimer(timer2);
		          ert("#msg_code"):attr("value","重新发送");
		          ert("#msg_code"):attr("enable","true");
		          ert("#msg_code"):css("background-image","btn_bg.png");
		      end
		  end

	    local che_flag2 = "0";
	    function this.get_fage2()
	        if che_flag2 == "0" then
	            che_flag2 = "1";
	        else
	            che_flag2 = "0";
	        end
	    end
        
		function this.next_page(accBalance)
            local accno = ert("#accno"):val();

            local choicebakn2 = ert("#choicebakn2"):val();
            if choicebakn2 == "" then
             	window:alert("请选择开户行");
	            return;
            end

            local i = string.find(choicebakn2, "_");
            local bankid = string.sub(choicebakn2,1,i-1);
           
            local th_acountname = string.sub(choicebakn2,i+1,string.len(choicebakn2));

			local cardid = ert("#cardid"):val();
			local username = ert("#username"):val();
			local mobile = ert("#MOBILE"):val();
        
			local securityCode = ert("#node"):val();
            if securityCode == "" then
                window:alert("请输入短信验证码");
                return;
            end
            if string.len(securityCode) < 6 then
                window:alert("短信验证码不能小于6位");
                return;
            end

            if che_flag2=="0" then
                window:alert("请勾选阅读并同意三方支付签约协议");
                return;
            end

            if th_account_a=="" or choicebakn2_a=="" then
                window:alert("请点击获取短信按钮获取正确短信");
                return;
            end

           if accno~=th_account_a then
                window:alert("他行账号与获取短信的账号不一致");
                timer:stopTimer(timer2);
                ert("#msg_code"):attr("value","重新发送");
                ert("#msg_code"):attr("enable","true");
                ert("#msg_code"):css("background-image","btn_bg.png");
                return;
            end
            if choicebakn2~=choicebakn2_a then
               window:alert("开户行与获取短信的开户行不一致");
                timer:stopTimer(timer2);
                ert("#msg_code"):attr("value","重新发送");
                ert("#msg_code"):attr("enable","true");
                ert("#msg_code"):css("background-image","btn_bg.png");
                return;
            end
	        
          	local post_body={id='ebank_manage_asset',tranCode='EMAS0021',account=accno,bankmake=th_acountname,bankid=bankid,cardId=cardid,username=username,mobile=mobile,smsValidationCode=securityCode,agreementId=agreementId};

          	local context = {account=accno,bankname=th_acountname,bankid=bankid,username=username,mobile=mobile,vaLue_110=vaLue_110,accBalance=accBalance};

          	ert.channel:next_page('ebank_manage_asset','EMAS0021',post_body,{context=context})
      	end

      function this.showContent2()
          window:showContent("local:FullSLoading1.xml",11);
          local showboby=[[
          <div class="div_showContent" name="showcontent_div" border="0">
              <div class="ebank_public_header_div" border="0">
                  <div class="ebank_public_header_div_left" border ="0" onclick="hidetable()">
                      <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                  </div>
              <label class="ebank_public_header_label">三方支付签约协议</label>
              <label class="public_line_b1"></label>
              </div>
          <div class="public_main_div_1" border="0">
          <label class="label_w320_h40_t10">
          三方支付签约协议</label>
          <label class="label_f14_w290_l15">
       
甲   方：投资者 
乙   方：广东华兴银行股份有限公司（以下简称“华兴银行”） 
为规范甲、乙双方在本产品下的权利义务，双方依照中国法律法规和有关金融监管规章，经平等自愿协商一致，特就三方支付签约服务达成如下协议，承诺信守：
甲方同意接受本协议的全部约定内容，确认承担由此产生的一切责任。
一、甲方保证提供给华兴银行的银行卡资料（包括：卡号、姓名、证件号码、手机号码等）为本人持有的真实、完整、准确、合法、有效的银行卡信息，并同意华兴银行将以上信息送至发卡银行进行核验。
二、甲方同意并授权华兴银行根据甲方指定的金额通过支付清算机构以及发卡银行，从甲方提供的银行卡扣款。甲方承诺前述委托扣款是本人做出，不得向支付清算机构以及发卡银行提出异议。
三、因提供他人银行卡资料或虚假信息、甲方提供的银行卡账户余额不足或被挂失、冻结、销户等原因引起的一切法律责任，由甲方自行承担，与华兴银行无关。
四、因支付清算机构或发卡银行原因导致扣款错误或延迟，给甲方造成损失的，由过错方承担责任，华兴银行将协助甲方向其追究相应责任。

本协议适用中华人民共和国法律。双方如有争议，应尽可能通过协商、调解解决，协商、调解不成，任何一方均有权向广州仲裁委员会申请仲裁，仲裁按照该委员会届时有效的仲裁规则进行。仲裁裁决是终局的，对双方均有法律约束力。

          </label>
        </div>
        </div>
        ]]
        local div_control_show = document:getElementsByName("showcontent_div");
        div_control_show[1]:setInnerHTML(showboby);
        window:showControl(div_control_show[1],789); 
        window:setPhysicalkeyListener("backspace",hidetable);   
        window:hide(11);
      end;

      function hidetable()    
          window:showContent("local:FullSLoading1.xml",12);
          window:hide(789);
          ebank_public.allSuit:physicalkey_back();
          window:hide(12); 
          ryt:reload();
      end;
        
        ebank_public.allSuit:physicalkey_back();
end)(ert.channel:get_page("ebank_manage_asset","EMAS0020"));
