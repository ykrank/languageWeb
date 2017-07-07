(function(this)
  
    local last_page_example = ert.channel:get_page("ebank_tied_card","UNLOCK01");
    local bank = last_page_example.context["bank"];
    local account = last_page_example.context["account"];
    local mobile = last_page_example.context["mobile"];

--解绑银行卡
    function this.unlock_result()
        local securityCode = ert("#node"):val();
        if securityCode == "" then
            window:alert("请输入6位短信验证码");
            return;
        end
        if string.len(securityCode) < 6 then
            window:alert("请输入6位短信验证码");
            return;
        end
        if phone==nil then
            window:alert("请点击获取短信按钮获取短信");
            return;
        end

        local context = {bank=bank,account=account};
        ert.channel:next_page("ebank_tied_card","UNLOCK02",{id='ebank_tied_card',tranCode='UNLOCK02',bank=bank,account=account,mobile=mobile,securityCode=securityCode},{context=context});
    end

    ebank_public.allSuit:physicalkey_back();


end)(ert.channel:get_page("ebank_tied_card","UNLOCK01"));
