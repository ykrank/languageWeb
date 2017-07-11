(function( this )
	ebank_public.allSuit:physicalkey_result_back();
    function click()
      -- window:alert("按钮被点击111！");
		local next_channelId = "ebank_login_register";
		local next_trancode = "LOGIN001";
		local post_body = {id=next_channelId,tranCode=next_trancode,};
        ert.channel:next_page(next_channelId, next_trancode, {},{just_page=true});
    end;

    

end)(ert.channel:get_page("ebank_password_manager","FGPD0003"));
