(function(this)
	ebank_public.allSuit:physicalkey_result_back();
      function click()
        local next_channelId = "ebank_other";
        local next_trancode = "MANU0001";
        local post_body = {id=next_channelId,tranCode=next_trancode,};
        ert.channel:first_page(next_channelId, next_trancode, {},{just_page=true});
    end;

end)(ert.channel:get_page("login_password_modify","LPM00002"));
