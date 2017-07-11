(function(this)

--获取上个页面输入的数据
     local last_page_example = ert.channel:get_page("ebank_login_register","REGI0012");
     local username = last_page_example.context["username"];
     local cardId = last_page_example.context["cardId"];

     local mobile = this.context["mobile"];
     local custno = this.context["custno"];
     
     local che_flag = "0";
     function this.get_fage()
        if che_flag == "0" then
            che_flag = "1";
        else
            che_flag = "0";
        end
    end

    function change_value()
        local moblie_fomat = ert("#MOBILE"):val();
        if string.sub(moblie_fomat, 1, 1) == "+" or string.sub(moblie_fomat, 1, 1) == "-" then
            ert("#MOBILE"):attr("value","");
        end
    end

--注册信息填写下一步
      function this.next_page()
              local securityCode = ert("#node"):val();
              if securityCode == "" then
                 window:alert("请输入短信验证码");
                 return;
              end
              if string.len(securityCode) < 6 then
                 window:alert("短信验证码不能小于6位");
                 return;
              end
              local tradePwd = ert("#tac"):val();
              if tradePwd == "" then
                 window:alert("请设置交易密码");
                 return;
              end
              if string.len(tradePwd) < 6 then
                 window:alert("交易密码不能小于6位");
                 return;
              end

              local _, count = string.gsub(tradePwd, "%d", "");
              if count ~= 6 then
                  window:alert("交易密码应为6位数字");
                  return;
              end

              local tradePwd2 = ert("#tac2"):val();
              if tradePwd2 == "" then
                 window:alert("请确认交易密码");
                 return;
              end
              if tradePwd~=tradePwd2 then
                 window:alert("交易密码与确认交易密码必须保持一致");
                 return;
              end
              tradePwd = encrypt:desRsa(tradePwd);
              tradePwd2 = encrypt:desRsa(tradePwd2);
              local recommend = ert("#referrer"):val();

              if che_flag=="0" then
                  window:alert("请勾选阅读并同意E账户开户协议");
                  return;
              end  
              
              if phone==nil then
                  window:alert("请点击获取短信按钮获取短信");
                  return;
              end

              local post_body={id='ebank_login_register',tranCode='REGI0004',mobile=mobile,securityCode=securityCode,tradePwd=tradePwd,tradePwd2=tradePwd2,recommend=recommend,username=username,cardId=cardId,custno=custno}
          
              ert.channel:next_page("ebank_login_register","REGI0004",post_body,{});
          
      end

      function this.showContent()
          window:showContent("local:FullSLoading1.xml",11);
          local showboby=[[
          <div class="div_showContent" name="showcontent_div" border="0">
              <div class="ebank_public_header_div" border="0">
                  <div class="ebank_public_header_div_left" border ="0" onclick="hidetable()">
                      <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                  </div>
              <label class="ebank_public_header_label">E账户开户协议</label>
              <label class="public_line_b1"></label>
              </div>
          <div class="public_main_div_1" border="0">
          <label class="label_w320_h40_t10">
          广东华兴银行个人E账户介绍</label>
          <label class="label_f14_w290_l15">
       
一、产品定义
“个人E账户”是通过电子渠道实现开户，无物理介质，主要依托互联网进行投资理财、资金转入、转出等业务处理的人民币结算账户。该账户转入时只能接受广东华兴银行或者他行同名账户的资金，只能向绑定账户转账实现资金转出，必须本人实名开立。
二、申请办理
1、客户登录广东华兴银行投融资平台PC端、APP端，微信银行等电子渠道进行办理；
2、客户阅读并同意本须知和业务相关协议、规定；
3、客户输入个人信息、账户交易密码、绑定手机号码在内的相关信息，绑定的手机号码，在客户办理业务的过程中广东华兴银行将在必要时向该手机号码发送验证码，或主动致电该号码核实客户身份。
4、客户通过不同的方式进行身份验证和账户激活。
三、业务须知
1、开户
当申请人完成身份信息、联系信息、绑定手机、登录密码、账户密码设置后，即可完成开户。
2、身份认证、绑定激活
在申请人申请开立“个人E账户”后，可通过我行提供的渠道使用绑定账户的在线签订华兴卡快捷支付签约协议、三方支付协议，或通过他行转账、汇款等方式，向“个人E账户”进行转账汇款，以验证您绑定账户的真实有效性，并激活“个人E账户”。
3、绑定账户与绑定手机号码
（1）绑定账户：是指申请人在申请开立广东华兴银行“个人E账户”后，主动绑定本行/他行同名银行卡，绑定帐户须为申请人本人以实名制开立且真实持有的有效银行卡或存折。“个人E账户”只能向绑定账户转账出款。
注：客户首次转账至“个人E账户”的广东华兴银行账户或者他行银行同名账户默认为绑定账户。
（2）绑定手机号码：是指申请人在申请开立“个人E账户”时设定的，用于接收由广东华兴银行发送的各种动态密码、交易提醒短信，必要时广东华兴银行人员用于与申请人联络以便核实身份信息的手机号码。
4、密码重置
申请人遗失“个人E账户”交易密码时，需由申请人进行手机动态密码验证，核验通过后，交易密码重置成功。
5、变更绑定手机号码 
申请人因遗失手机，需变更绑定手机号码时，需验证申请人新手机号码的验证码、账户交易密码、客户姓名、身份证号码，核验身份通过后，绑定手机号码变更成功。 
6、自助冻结和自助解冻
遇特殊情况，申请人可以自助冻结“个人E账户”。当账户需要解除冻结时，由申请人进行账户自助解冻。
四、客户权利义务  
1、客户自愿申请开立“个人E账户”，同意遵守监管部门、广东华兴银行的相关业务条款，并遵守国家有关金融法律法规、政策及广东华兴银行业务规章制度。
    2、客户保证向广东华兴银行提供的所有申请资料真实、有效、合法。广东华兴银行应对客户相关个人资料保密，但在广东华兴银行内部使用、法律法规及金融监管机构另有规定或客户与广东华兴银行双方另有约定的除外。客户保证“个人E账户”开户为客户本人实名办理。
    3、客户同意将“个人E账户”自动设置为个人银行结算账户。客户保证其对个人银行结算账户的使用遵守《人民币银行结算账户管理办法》及金融监管机构其他账户管理的有关规定。
    4、客户申请开立“个人E账户”所设定的绑定账户、绑定手机号码，必须是客户本人实名合法获得，并处于客户有效掌控下。
    5、客户申请开立“个人E账户”后可通过广东华兴银行投融资平台办理转账，基金、理财产品等业务，各项业务的具体开通时间以广东华兴银行推出时间为准。客户通过“个人E账户”办理广东华兴银行其他业务时，应当同时遵守广东华兴银行该业务的业务规定及该业务的服务协议及客户须知。“个人E账户”不能在柜台、ATM等办理取款、转账等业务。为不断改进服务，提高服务的安全性、可靠性、方便性，广东华兴银行有权依据法律、法规、规章或业务需要对“个人E账户”服务的内容、操作流程、章程、客户须知或收费标准等进行调整，涉及收费或其他客户权利义务变更的调整，将于正式对外公告后施行，自公告施行之日公告内容构成对广东华兴银行与客户间协议约定的有效修改和补充。如果客户不同意接受广东华兴银行的调整内容，客户有权向广东华兴银行申请终止“个人E账户”服务。客户既不申请终止服务，又继续使用广东华兴银行“个人E账户”服务的，视为客户接受广东华兴银行对“个人E账户”服务的相关调整。
   6、因不可抗力或供电、通讯、网络等非广东华兴银行原因导致“个人E账户”不能正常使用的，广东华兴银行将视情况协助客户解决或提供必要的帮助，但不承担责任。对于在交易过程中，因暂时的网络通讯故障或其他原因造成的错账现象，广东华兴银行有权根据实际交易情况进行账务处理。
本人已理解并确认上述开户须知中有关免除、限制贵行责任的条款，本人同意贵行有权根据国家法律法规及业务需要对本须知内容进行调整，并同意该项调整将在贵行网站公告一定时期后执行，无需另行通知本人。如果本人不愿接受贵行公告内容的，应在贵行公告施行前向贵行申请终止相关服务。
          </label>
        </div>
        </div>
        ]]
        local div_control_show = document:getElementsByName("showcontent_div");
        div_control_show[1]:setInnerHTML(showboby);
        window:showControl(div_control_show[1],987); 
        window:setPhysicalkeyListener("backspace",hidetable);   
        window:hide(11);
      end;

      function hidetable()    
          window:showContent("local:FullSLoading1.xml",12);
          window:hide(987);
          ebank_public.allSuit:physicalkey_back();
          window:hide(12); 
          ryt:reload();
      end;

      ebank_public.allSuit:physicalkey_exit_back();
      
end)(ert.channel:get_page("ebank_login_register","REGI0012"));
