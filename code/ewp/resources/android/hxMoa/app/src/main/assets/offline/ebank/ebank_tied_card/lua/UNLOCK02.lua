(function(this)
   
   function this.next_cardList()
   	   ert.channel:next_page("ebank_tied_card","TCM00001",{id="ebank_tied_card",tranCode="TCM00001"},{});
   end

   window:setPhysicalkeyListener("backspace", this.next_cardList);
     
end)(ert.channel:get_page("ebank_tied_card","UNLOCK02"));
