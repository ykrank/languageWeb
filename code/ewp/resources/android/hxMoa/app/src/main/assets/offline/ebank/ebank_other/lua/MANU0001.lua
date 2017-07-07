(function( this )
	ebank_public.allSuit:physicalkey_back();
	
    function click(num)
    	this.num=num;
       
    	if num == 1 then

    		local next_channelId = "ebank_password_manager";
			local next_trancode = "LPM00001";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:first_page(next_channelId, next_trancode, post_body);
    	elseif num == 2 then
    		local next_channelId = "ebank_password_manager";
			local next_trancode = "MBRS0001";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:first_page(next_channelId, next_trancode, post_body);

    	elseif num == 3 then
    		local next_channelId = "ebank_password_manager";
			local next_trancode = "TPM00001";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:first_page(next_channelId, next_trancode, post_body);

        elseif num == 5 then
			local next_channelId = "ebank_other";
			local next_trancode = "INFO0001";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:next_page(next_channelId, next_trancode, post_body);

        elseif num == 6 then
			local next_channelId = "ebank_password_manager";
			local next_trancode = "MSG000011";
			local post_body = {id=next_channelId,tranCode=next_trancode};
		    ert.channel:first_page(next_channelId, next_trancode, {},{just_page=true} );

	    elseif num == 7 then
			local next_channelId = "ebank_free_cost";
			local next_trancode = "LIST0001";
			local post_body = {id=next_channelId,tranCode=next_trancode};
		    ert.channel:first_page(next_channelId, next_trancode, post_body);

		elseif num == 8 then
			local next_channelId = "ebank_other";
			local next_trancode = "MANU0002";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:next_page(next_channelId, next_trancode, post_body);

    	elseif num == 4 then
    		local next_channelId = "ebank_freeze";
			local next_trancode = "FREEZE01";
			local post_body = {id=next_channelId,tranCode=next_trancode};
	        ert.channel:first_page(next_channelId, next_trancode, post_body);
    	
    	
    	end
    end;

end)(ert.channel:get_page("ebank_other","MANU0001"));
