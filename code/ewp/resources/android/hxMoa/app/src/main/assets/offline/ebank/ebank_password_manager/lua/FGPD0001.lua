(function( this )

  ebank_public.allSuit:physicalkey_back();
--  --校验姓名
   local function check_name(name)
      if  name == "" then
         window:alert("请输入姓名");
      else
        return 1;
      end
    end

--  --校验ID卡
 local function check_ID_card(ID_card)
  local IDNO = ert("#ID_card"):val();
    if ID_card == "" then 
    	   window:alert("您还未输入身份证，请输入");
    elseif string.len(ID_card) ~= 18 then 
      window:alert("您输入的身份证位数不正确，请重新输入");
    elseif input_check_chinese(ID_card)==0 then
    --不需要操作
    else
      return 1;
    end
  end

--提交姓名和身份证
  function click()
      local customerName = ert("#username"):val();
      local IDNO = ert("#ID_card"):val();
      local next_channelId = "ebank_password_manager";
      local next_trancode = "FGPD0002";
     
      if check_name(customerName) == 1 and check_ID_card(IDNO) == 1 then
        ---------------身份证校验-------------------------------
        if string.match(IDNO,[[%s]]) ~= nil then
             window:alert("身份证号码不能有空格");
             return;
          end
          local id1 = string.sub(IDNO, 1, 17);
          local id2 = string.sub(IDNO, 18, 18);
          local _, count = string.gsub(id1, "%d", "");
          if count < 17  then
              window:alert("身份证号码前17位不允许出现特殊字符或字母");
              return;
          end
          if string.match(id2,[[%d]]) == nil and id2 ~= "X" and id2 ~= "x" then
              window:alert("身份证号码最后一位只允许出现数字或者X");
              return;
          end

          if certificate_verify(IDNO) == 0 then
              return;
          end

          if id2 == "x" then 
            IDNO = id1.."X";
          end
          ----------------------------------------------
        local post_body = {id=next_channelId,tranCode=next_trancode,customerName=customerName,IDNO=IDNO};
         ert.channel:next_page(next_channelId, next_trancode, post_body);
      end
    end
end)(ert.channel:get_page("ebank_password_manager","FGPD0001"));
