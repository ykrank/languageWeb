(function(this)

--脸部识别成功进行下一步绑卡
      function this.next_page()
          ert.channel:next_page('ebank_tied_card','BOUND001',{id='ebank_tied_card',tranCode='BOUND001'},{})
      end

--返回银行卡管理
      function this.tied_card()
          ert.channel:next_page('ebank_tied_card','TCM00001',{id='ebank_tied_card',tranCode='TCM00001'},{})
      end

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
              window:alert(arg["data"]);
              return;
          end
      end
          
      function startFace()
          native:openFace(startFaceCallback);
      end;


--重新识别
      function this.face_page()
          startFace();
      end

      function this.last_page()
        ert.channel:next_page("ebank_tied_card","TCM00001",{id="ebank_tied_card",tranCode="TCM00001"},{});
      end

      window:setPhysicalkeyListener("backspace", this.last_page);


end)(ert.channel:get_page("ebank_tied_card","FACE0001"));
