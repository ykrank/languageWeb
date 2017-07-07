(function(this)

    local last_page_example = ert.channel:get_request("ebank_login_register","REGI0004");
    local mobile = last_page_example["mobile"];

    function this.change_info()
        local context = {mobile=mobile};
    	ert.channel:next_page('ebank_login_register','REGI0013',{},{just_page=true,context=context})
    end

    ebank_public.allSuit:physicalkey_exit_back();

end)(ert.channel:get_page("ebank_login_register","REGI0004"));
