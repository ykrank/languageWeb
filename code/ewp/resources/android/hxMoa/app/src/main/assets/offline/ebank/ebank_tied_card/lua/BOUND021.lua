(function(this)
      
      local req_val = ert.channel:get_page("ebank_tied_card","BOUND021");
      local vaLue_110 = req_val.context["vaLue_110"];

      --人脸识别
      local code = "";
      local face = "";
      function startFaceCallback(arg)
          if string.len(arg["data"]) > 100 then
            face=arg["data"];
          else
              code = arg["code"];
          end
          
          if arg["data"]=="人脸识别成功！" or arg["data"]=="活体检测成功" then 
              local post_body={id='ebank_tied_card',tranCode='FACE0001',code=code,face=face};
              ert.channel:next_page('ebank_tied_card','FACE0001',post_body,{})
          elseif string.len(arg["data"]) < 100 then
              window:alert("人脸识别失败！");
              return;
          end
          
      end
          
      function startFace()
          native:openFace(startFaceCallback);
      end;
      
      if vaLue_110 == "110" then
          function next_home_title()
              ert.channel:first_page("ebank_product_recommend","LH0001",{id="ebank_product_recommend",tranCode="LH0001"},{});
          end 
          window:setPhysicalkeyListener("backspace", next_home_title); 
      else
          function next_cardList()
              ert.channel:next_page("ebank_tied_card","TCM00001",{id="ebank_tied_card",tranCode="TCM00001"},{});
          end
          window:setPhysicalkeyListener("backspace", next_cardList);
      end

      function next_page()
          startFace();
      end

      function next_home()
          ert.channel:show_loading();
          ert.channel:finish();
      end

end)(ert.channel:get_page("ebank_tied_card","BOUND021"));
