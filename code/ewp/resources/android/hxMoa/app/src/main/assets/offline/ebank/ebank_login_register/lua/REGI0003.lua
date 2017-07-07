(function(this)
    
    local last_page_example = ert.channel:get_request("ebank_login_register","REGI0003");
    username_login = last_page_example["username"];
    is_login = "1";

    function this.tied_card()
    	local vaLue_110 = "110";
    	local context = {vaLue_110=vaLue_110};
        ert.channel:first_page("ebank_tied_card","BOUND001",{id='ebank_tied_card',tranCode='BOUND001'},{context=context});
    end

    ebank_public.allSuit:physicalkey_result_back();

end)(ert.channel:get_page("ebank_login_register","REGI0003"));
