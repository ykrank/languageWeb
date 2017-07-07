(function(this)

      function next_page_home()
          ert.channel:next_page("ebank_manage_asset","EMAS0001",{id="ebank_manage_asset",tranCode="EMAS0001"});
      end 
      
      function next_page(accBalance,username)
      	finsh_continue_bug_flag = "1";
          ert.channel:next_page("ebank_manage_asset","EMAS0002",{id="ebank_manage_asset",tranCode="EMAS0002",CustName=username,AccBalance=accBalance,signValue="0"});
      end 

      window:setPhysicalkeyListener("backspace", next_page); 
end)(ert.channel:get_page("ebank_manage_asset","EMAS0021"));
