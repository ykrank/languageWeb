--华兴银行项目公共lua代码
ebank_public = {};

--自定义全局变量--
global_hx_rate = nil;--华兴宝七日收益率
global_hx_profit = nil;--华兴宝万份收益率
global_zn_rate = nil;--智能存款年化收益率
global_zn_amount = nil;--起购金额

is_login = nil;--判断是否登录
obligate = nil;--预留信息
username_login = nil;--用户名
tiaozhuan = nil;--是否从必须登录流程进入登录页面
phone = nil; --1、判断是否点击获取了短信，2、用来判断最后上传的手机号是否与最后一次获取短信一致

product_back_flag = nil; --推荐返回标题头变量
onblur_flag = nil;--输入框页面返回焦点取消防报错标志
finsh_continue_bug_flag = nil --产品成功页面继续页面返回判断标志
----公共底部导航栏
(function()
  local function init()
      local bottom = {};
      function bottom:set_bottom(str)
          local img1 = "";
          local img2 = "";
          local img3 = "";
          local img4 = "";
          if str == "每日推荐" then
            img1 = "jptj1.png";
            img2 = "cpcs2.png";
            img3 = "wdzc2.png";
            img4 = "yxhd2.png";
          end;
          if str == "产品超市" then
            img1 = "jptj2.png";
            img2 = "cpcs1.png";
            img3 = "wdzc2.png";
            img4 = "yxhd2.png";
          end;
          if str == "我的资产" then
            img1 = "jptj2.png";
            img2 = "cpcs2.png";
            img3 = "wdzc1.png";
            img4 = "yxhd2.png";
          end;
          if str == "营销活动" then
            img1 = "jptj2.png";
            img2 = "cpcs2.png";
            img3 = "wdzc2.png";
            img4 = "yxhd1.png";
          end;
          local content = [[<div class="ebank_public_bottom_div" border="0">
              <label class="public_line_stroke_t0"></label>
              <div class="ebank_public_bottom_div_1" border="0" onclick='ebank_public.bottom:get_page("ebank_product_recommend","LH0001")'>
                <img src="]]..img1..[[" class = "ebank_public_img"></img>
                <label class="ebank_public_label_goloal_text_1">每日推荐</label>
              </div>
              <div class="ebank_public_bottom_div_2" border="0" onclick='ebank_public.bottom:get_page("ebank_product_homepage","PH0001")'>
                <img src="]]..img2..[[" class = "ebank_public_img"></img>
                <label class="ebank_public_label_goloal_text_2">产品超市</label>
              </div>
              <div class="ebank_public_bottom_div_3" border="0" onclick='ebank_public.bottom:get_page("ebank_manage_asset","EMAS0001")'>
                <img src="]]..img3..[[" class = "ebank_public_img"></img>
                <label class="ebank_public_label_goloal_text_3">我的资产</label>
              </div>
              <div class="ebank_public_bottom_div_4" border="0" onclick='ebank_public.bottom:get_page2("ebank_marketing","MARK0001")'>
                <img src="]]..img4..[[" class = "ebank_public_img"></img>
                <label class="ebank_public_label_goloal_text_4">营销活动</label>
              </div>
          </div>]];
          return content;
      end;

      function bottom:get_page(id,trancode)
          ert.channel:first_page(id,trancode,{id=id,tranCode=trancode},{});
      end;

      function bottom:get_page2(id,trancode)
          ert.channel:first_page(id,trancode,{},{just_page=true});
      end;

      ebank_public.bottom = bottom;
  end;
  init();
end)();

----公共头部
(function()
  local function init()
      local header = {};
      --初始化头部的公共函数
      --公共头部部分有几种情况:
      --1,首页显示效果(每日推荐，产品超市，我的资产，营销活动)
      --  1.1,未登录情况
      --  1.2,已登录情况
      --2,其它业务页面

      --参数说明：
      --titlename：标题
      --flag:( 1、四大首页;0、购买结果页面;2、其他过程页面;3、其他结果页面;4、返回上一页按钮和完成按钮;5、只有页面标题;6、推荐图片跳转华兴宝返回按扭;7,交易明细返回)
      --shareTitle:购买结果页面分享的标题,   其他传0
      --shareContent:购买结果页面分享的内容,   其他传0
      function header:set_header(titlename,flag,shareTitle,shareContent)
        repeat_click = false;
          local leftdiv="";
          if flag == "1" then
              leftdiv = [[
                  <div class="ebank_public_header_div_left" border ="0" onclick="ebank_public.side.show_left_column()">
                  <img src="left_lean_img.png" class="ebank_public_header_div_left_image"></img>
                  </div>
                   <div class="ebank_public_header_div_right" border ="0" onclick="ebank_public.side.show_right_column()">
                  <img src="rg_lean_img.png" class="ebank_public_header_div_right_image"></img>
                  </div>
              ]];
          elseif flag=="0" then
              leftdiv = [[
                  <div class="ebank_public_header_div_left" border ="0" onclick="ebank_public.header:fenxiang_page(']]..shareTitle..[[',']]..shareContent..[[')">
                  <img src="share.png" class="ebank_public_header_div_left_image3"></img>
                  </div>
                   <div border ="0" class="ebank_public_header_div_right">
                   <input type="button" id="delay_next" class="ebank_public_header_div_right" border ="0" onclick="ebank_public.header:next_home()" enable="true"></input>
                  <label class="ebank_public_header_div_right_label">完成</label>
                  </div>
              ]];
          elseif flag=="2" then
              leftdiv = [[
                    <div border="0">
                    <input type="button" id="delay_return" class="ebank_public_header_div_left" border ="0" onclick="ebank_public.header:return_page()" enable="true" ></input>
                    <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                    </div>
                ]];
          elseif flag=="4" then
              leftdiv = [[
                    <div border ="0">
                    <input type="button" id="delay_return" class="ebank_public_header_div_left" border ="0" onclick="ebank_public.header:return_page()" enable="true" ></input>
                    <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                    </div>
                    <div border ="0" class="ebank_public_header_div_right">
                    <input type="button" id="delay_next" class="ebank_public_header_div_right" border ="0" onclick="ebank_public.header:next_home()" enable="true"></input>
                    <label class="ebank_public_header_div_right_label">完成</label>
                    </div>
                ]];
          elseif flag=="5" then
              leftdiv = [[
                  <div border ="0" class="ebank_public_header_div_right" >
                  
                  </div>
              ]];
          elseif flag=="6" then
              leftdiv = [[
                      <div border="0">
                      <input type="button" id="delay_return" class="ebank_public_header_div_left" border ="0" onclick='ebank_public.bottom:get_page("ebank_product_homepage","PH0001")' enable="true" ></input>
                      <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                      </div>
                  ]];
          elseif flag=="7" then
              leftdiv = [[
                    <div border="0">
                    <input type="button" id="delay_return" class="ebank_public_header_div_left" border ="0" onclick="ebank_public.header:next_home()" enable="true" ></input>
                    <img src="back.png" class="ebank_public_header_div_left_image2"></img>
                    </div>
                ]];
          else
              leftdiv = [[
                  <div border ="0" class="ebank_public_header_div_right" >
                  <input type="button" id="delay_next" class="ebank_public_header_div_right" border ="0" onclick="ebank_public.header:next_home()" enable="true"></input>
                  <label class="ebank_public_header_div_right_label">完成</label>
                  </div>
              ]];
          end;

          local content = [[<div class="ebank_public_header_div" border="0">]]..leftdiv..[[
            <label class="ebank_public_header_label">]]..titlename..[[</label>
            <label class="public_line_b1"></label>
          </div>
          ]];
          return content;
      end;

      --返回箭头返回
      function header:return_page()
        onblur_flag="1";
	if finsh_continue_bug_flag == "1" then
		finsh_continue_bug_flag = nil;
		ert("#delay_next"):attr("enable","false"); 
		ert.channel:show_loading();
        	ert.channel:finish();	
	else
	        if repeat_click == nil or repeat_click == false then
	          repeat_click = true;
	          ert("#delay_return"):attr("enable","false");
	          ert.channel:show_loading();
	          ert.channel:back();
	        end
	end;
      end;

      --点击完成执行事件
      function header:next_home()
          ert("#delay_next"):attr("enable","false");
          ert.channel:show_loading();
          ert.channel:finish();
      end 

      --购买产品结果分享
      function header:fenxiang_page(shareTitle,shareContent)
          local share_div = [[<?xml version="1.0" encoding="UTF-8" ?>
                <content>
                  <head>
                    <style>
                    .body {width:320px;top:0px;}
                    </style>
                  </head>
                  <body name='body'>
                    <div class="share_hyaline_up" border="0" onclick="ebank_public.header:hide_share()"></div>
                    <div class="share_hyaline_down" border="0" onclick="ebank_public.header:hide_share()"></div>
                    <div class="share_hyaline_left" border="0" onclick="ebank_public.header:hide_share()"></div>
                    <div class="share_hyaline_right" border="0" onclick="ebank_public.header:hide_share()"></div>
                    <table class="table_share" border="0">
                      <tr class="tr_title">
                        <td class="tr_title">
                             <label class="share_title">分享到您的社交网络</label>
                        </td>
                      </tr> 
                      <tr class="tr_share">
                          <td class="tr_share">
                              <div class="div_share" border="0" onclick="share:shareToWX(']]..shareTitle..[[',']]..shareContent..[[','https://www.ghbibank.com.cn/eAccountF/')">
                                <img class="img_share" src="wechat.png" />
                                <label class="label_share">微信</label>
                              </div>
                              <div class="div_share2" border="0" onclick="share:shareToWXF(']]..shareTitle..[[',']]..shareContent..[[,'https://www.ghbibank.com.cn/eAccountF/')" >
                                <img class="img_share2" src="friends-circle.png" />
                                <label class="label_share2">朋友圈</label>
                              </div>
                              <div class="div_share3" border="0" onclick="share:shareToSina(']]..shareTitle..[[',']]..shareContent..[[,'https://www.ghbibank.com.cn/eAccountF/')">
                                <img class="img_share3" src="weibo.png" />
                                <label class="label_share3">微博</label>
                              </div>
                          </td>
                      </tr>
                    </table>
                  </body>
                </content>]];
            window:showContent(share_div, 835);
      end;

      --隐藏分享框      
      function header:hide_share()
          window:hide(835);
      end

      ebank_public.header = header;
  end;
  init();
end)();

--产品推荐信息     
(function()

  local function init()
      local new_broduct = {}; 

      function new_broduct:new_broduct_show()
        
        local new_info = [[
        <div border="0" class="suggest_new_div">
          <div class="line_l0_w125_h1"></div>
            <label class="label_l132_fs14_c4D4D4D">为您推荐</label>
            <div class="line_r0_w125_h1"></div>
            <label class="public_line_stroke_b1"></label>
          <div class="image_div" border="0">
            <label class="public_line_stroke_t0"></label>
            <img src="tj_bg.png" class = "img_h75_w150_l5" onclick="ebank_public.new_broduct:turn_page_px00001()"></img>
            <img src="tj_bg2.png" class = "img_h75_w150_r5" onclick="ebank_public.new_broduct:turn_page_pd00001()"></img>
            <label class="label_f14_cffffff">]]..global_hx_rate..[[七日年化收益率</label>
            <label class="label_f14_cffffff,l160">]]..global_zn_rate..[[最高年化收益率</label>
            <label class="label_f10_cffffff">华兴宝</label>
            <label class="label_f10_cffffff,l160">智能存款</label>
          </div>
        </div>]]
        return new_info;
      end

      function new_broduct:turn_page_px00001()
        --推荐返回标题头变量
        product_back_flag = "1";
        local channelId = "ebank_product_huaxingbao";
        local trancode = "PX0001";
        ert.channel:first_page(channelId, trancode,{id = channelId,tranCode = trancode},{});
      end

      function new_broduct:turn_page_pd00001()
        --推荐返回标题头变量
        product_back_flag = "1";
        local channelId = "ebank_product_deposit";
        local trancode = "PD0001";
        ert.channel:first_page(channelId, trancode,{id = channelId,tranCode = trancode},{});
      end

      ebank_public.new_broduct = new_broduct;
  end;
  init();
end)();

----侧边栏
(function()
  local function init()
      local side = {};
      --初始化侧边栏的公共函数
      
      --show出左边侧边栏
          function side.show_left_column()
              local div = [[<?xml version="1.0" encoding="UTF-8" ?>
                  <content>
                    <head>
                      <style>
                      .body {width:320px;top:0px;}
                      </style>
                          </head>
                      <body name='body'>
                      <div class="public_div_left_column" border="0" name="menu_div">
                      <div class="public_div2_left_column" border="0">]];
                         
                if is_login=="1" then
                    div = div .. [[   
                          <img src="local:head_portrait.png" class="head_portrait"></img>   
                          <label class="label_username">#{=username_login}#</label>
                          <label class="label_obligate">预留信息:#{=obligate}#</label>]];
                else 
                    div = div .. [[      
                          <img src="local:head_portrait2.png" class="head_portrait"></img>
                          <input type="button" class="login_button" onclick='ebank_public.side:go_page("ebank_login_register","LOGIN001")' value="登录"></input>
                          <label class="label_obligate">登录查看信息！</label>]];
                end
                    div = div .. [[
                          
                          <label class="line_left_column2"></label>
                          <img src="local:huaxing.png" class="huaxing"></img>
                          <label class="line_left_column3"></label>
                      </div>

                      <table class="public_tabel_left_column2" border="0">
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:go_page("ebank_other","MANU0001")'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:account.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">账号管理</label>
                              </td>
                          </tr>
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:go_page2("ebank_password_manager","MSG00001")'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:change.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">修改预留信息</label>
                              </td>
                          </tr>
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:go_page2("ebank_tied_card","TCM00001")'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:bankcard.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">银行卡管理</label>
                              </td>
                          </tr>
                          <tr class="tr_tabel_left_column2,com_ui_h10">
                              <td class="td_tabel_left_column2,com_ui_h10">
                                  <label class="line_left_column"></label>
                              </td>
                          </tr>
                          <tr class="tr_tabel_left_column2" onclick='check_update()'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:check.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">检查更新</label>
                              </td>
                          </tr>
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:go_page("ebank_other","HELP0001")'> 
                              <td class="td_tabel_left_column2">
                                  <img src="local:help.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">帮助中心</label>
                              </td>
                          </tr>]];
                if is_login=="1" then
                    div = div .. [[       
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:close_page()'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:exit.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">安全退出</label>
                              </td>
                          </tr>]];
                else 
                    div = div .. [[      
                          <tr class="tr_tabel_left_column2" onclick='ebank_public.side:go_page("ebank_login_register","REGI0001")'>
                              <td class="td_tabel_left_column2">
                                  <img src="local:zhuce.png" class="public_left_column_img" />
                                  <label class="public_left_column_menu">快速注册</label>
                              </td>
                          </tr>]];
                end
                   div = div .. [[</table>
                      <img src="local:cebeijing.png" class="dibu_img" />
                      <div class="public_right_blind" onclick="ebank_public.side.hide_left_column()" border="0"></div>
              </div>
              </body>
              </content>]];
              window:showContent(div, 112);
          end
          --hide左边侧边栏
          function side.hide_left_column()
              window:hide(112);
          end

          function side:go_page(id,trancode)
              side.hide_left_column()
              ert.channel:first_page(id,trancode,{},{just_page=true});
          end;
          function side:go_page2(id,trancode)
              side.hide_left_column();
              ert.channel:first_page(id,trancode,{id=id,tranCode=trancode},{});
          end;

          function side:close_page()
              window:alert("是否退出客户端","退出","取消",
                function(index)
                    if index == 0 then
                        window:close();
                    end
                end);
          end;
          --show出右上角侧边栏
          function side.show_right_column()

              local right_div = [[<?xml version="1.0" encoding="UTF-8" ?>
                  <content>
                    <head>
                      <style>
                      .body {width:320px;top:0px;}
                      </style>
                    </head>
                    <body name='body'>
                      <div class="public_right_column_hyaline" onclick="ebank_public.side.hide_right_column()" border="0"></div>
                      <div class="public_right_column_hyaline3" onclick="ebank_public.side.hide_right_column()" border="0"></div>
                      <table class="public_table_right_column" border="0">
                        <tr class="public_tr_right_column,com_ui_h10">
                          <td class="public_td_right_column,com_ui_h10">
                          </td>
                        </tr>]];
                      if is_login=="1" then

                        right_div =right_div.. [[
                        <tr class="public_tr_right_column" onclick='ebank_public.side:close_page()'>
                          <td class="public_td_right_column">
                            <img src="local:right_exit.png" class="public_right_column_img"></img>
                            <label class="public_right_column_menu">安全退出</label>
                            <label class="public_right_column_line"></label>
                          </td>
                        </tr>]];
                      else
                        
                        right_div =right_div.. [[
                        <tr class="public_tr_right_column" onclick='ebank_public.side:get_page("ebank_login_register","LOGIN001")'>
                          <td class="public_td_right_column">
                            <img src="local:right_login.png" class="public_right_column_img"></img>
                            <label class="public_right_column_menu">登录/注册</label>
                            <label class="public_right_column_line"></label>
                          </td>
                        </tr> ]];
                      end
                        right_div =right_div.. [[
                        <tr class="public_tr_right_column"  onclick='ebank_public.side:get_page2("ebank_other","INFO0001")'>
                          <td class="public_td_right_column">
                            <img src="local:notification.png" class="public_right_column_img"></img>
                            <label class="public_right_column_menu">消息</label>
                          </td>
                        </tr> 
                      </table>

                      <div class="public_right_column_hyaline2" onclick="ebank_public.side.hide_right_column()" border="0"></div>
                    </body>
                  </content>]];
              window:showContent(right_div, 511);
          end
            --hide右上角侧边栏
          function side.hide_right_column()
              window:hide(511);
          end

          function side:get_page(id,trancode)
              side.hide_right_column()
              ert.channel:first_page(id,trancode,{},{just_page=true});
          end;
          function side:get_page2(id,trancode)
              side.hide_right_column()
              ert.channel:first_page(id,trancode,{id=id,tranCode=trancode},{});
          end;

      ebank_public.side = side;
  end;
  init();
end)();

----短信验证：当前页面手机号码控件的id必须为MOBILE
(function()
  local function init()
      local msg = { 
      --是否点击获取的标志
      msg_onblur="";
      };
      local timer2= "";
      local second= "";
      
      function msg.msg_page(flag)
        msg.msg_onblur="";
          local flag=flag;
          if flag == "1" then
             flag="1";
          else
              flag="0";
          end 
          local msgDiv=[[
              <div border="0">
                   <label class="public_label_left10,public_w100_h26">短信验证码</label>
                    <input class="public_input_w100_l117" maxleng="6" type="text" id="node" hold="请输入验证码" onchange="change_codevalue()" border="0" style="-wap-input-format:'N'" value="" onblur="ebank_public.msg.onblur()" />
                    <input type="button" name="msg_code" enable="true" id="msg_code" class="public_button_node" value="获取验证码"  onclick="ebank_public.msg.get_msg_code(']]..flag..[[')"  />
               </div>
          ]];
          return msgDiv;
      end

      function msg.onblur( )
        if msg.msg_onblur~=true then
          window:alert("请先获取短信验证码")
          return
        end
      end

      function change_codevalue()
          local node_fomat = ert("#node"):val();
          if string.sub(node_fomat, 1, 1) == "+" or string.sub(node_fomat, 1, 1) == "-" then
              ert("#node"):attr("value","");
          end
      end

      function msg.get_msg_code(flag)
        msg.msg_onblur=true
          local mobile = ert("#MOBILE"):val();
          if mobile == "" then
             window:alert("请输入手机号码");
             return;
          end
          if string.len(mobile) < 11 then
             window:alert("手机号码不能小于11位");
             return;
          end
          phone=mobile;
          local flag = flag;
          ert.channel:first_page("ebank_public","NOTE0000",{id='ebank_public',tranCode='NOTE0000',mobile=mobile,flag=flag},{request_callback=ebank_public.msg.msg_code_callback})
      end

      function msg.msg_code_callback(response)
          local code = response["responseCode"];
          if code == 200 then
              local json_data = response["responseBody"];
              local json_tbl = json:objectFromJSON(json_data);
              local smsState = json_tbl["body"]["smsState"];
              if smsState == "00" then
                  second = tonumber(120);
                  timer2 = timer:startTimer(1, true, msg.showSec, 0);
              elseif smsState == "02" then
                  window:alert("该手机号已被注册，请重新输入");
                  return;
              else
                  window:alert("发送失败");
                  return;
              end
          end
      end
    
      function msg.showSec()
          if second > 1 then
              second = second - 1;
              local ctrl = document:getElementsByName("msg_code");
              if ctrl and #ctrl > 0 then
                ert("#msg_code"):attr("value",tostring(second).."s");
                ert("#msg_code"):css("background-image","note_click.png");
                ert("#msg_code"):attr("enable","false");
              else
                timer:stopTimer(timer2);
                timer2=nil;
                second=nil;
                return;
              end
          else
              timer:stopTimer(timer2);
              ert("#msg_code"):attr("value","重新发送");
              ert("#msg_code"):attr("enable","true");
              ert("#msg_code"):css("background-image","btn_bg.png");
          end
      end
      ebank_public.msg = msg;
  end;
  init();
end)();

--页面公共方法
(function()
  local function init()
      local allSuit={};
      function allSuit:physicalkey_back()--返回上一页物理返回
          window:setPhysicalkeyListener("backspace", physicalkey_back_fun);
      end;

      function allSuit:physicalkey_result_back()--结果页面物理返回
          window:setPhysicalkeyListener("backspace", physicalkey_back_result);
      end;

      function allSuit:physicalkey_exit_back()--退出app物理返回
          window:setPhysicalkeyListener("backspace", physicalkey_exit_fun);
      end;

      function physicalkey_back_fun()
          ert.channel:show_loading();
          ert.channel:back();
      end;

      function physicalkey_back_result()
          ert.channel:show_loading();
          ert.channel:finish();
      end;

      function physicalkey_exit_fun()
          ert.channel:show_loading();
          window:alert("是否退出客户端","退出","取消",
                function(index)
                    if index == 0 then
                        window:close();
                    else
                        ert.channel:hide_loading();
                    end
                end);
      end;
      
      ebank_public.allSuit = allSuit;
  end;
  init();
end)();

--金额格式化方法 format_money(num)
function format_money(str)
    if str == nil or str == "" then
        return "";
    end
    str = strip_str(str);
    local money = tonumber(str,10);
    local money_str = string.format("%.2f", money);
    while true do
        money_str, k = string.gsub(money_str, "^(-?%d+)(%d%d%d)",'%1,%2')
        if (k==0) then
            break;
        end;
    end;
    if string.find(money_str,"%.") == nil then
         money_str = money_str .. ".00";
    elseif string.len(money_str) - string.find(money_str,"%.") == 1 then
         money_str = money_str .. "0";
    end;
    if string.find(money_str, "-") then
            money_str = string.sub(money_str, 2, -1);
    end;
    return money_str;
end;

function strip_str(str)
    if str == nil then
        return "";
    else
        local new_str = string.gsub(str, "^[ ]+", "");
        new_str = string.gsub(new_str, "[ ]+$", "");
        return new_str;
    end;
end;

----------------金额实时格式化------------------------------
function input_onchange() 
  onblur_flag = nil;
  local node_fomat = ert("#inuput_money"):val();
  --加 减 消除--
  if string.sub(node_fomat, 1, 1) == "+" or string.sub(node_fomat, 1, 1) == "-" then
    ert("#inuput_money"):attr("value","");
  end
  --小数二位后自动 消除 
  if string.find(node_fomat,"%.")~=nil and string.len(node_fomat) - string.find(node_fomat,"%.") >2  then
    node_fomat = input_format_money_point(node_fomat);
    window:closeKeyboard();
  end
end

function input_format_money_point(str)
  str = strip_str_empty(str);
  local money_str = str;
  while true do
      money_str, k = string.gsub(money_str, "^(-?%d+)(%d%d%d)",'%1,%2')
      if (k==0) then
          break;
      end;
  end;
  return money_str;
end;

function strip_str_empty(str)
    if str == nil then
        return "";
    else
        local new_str = string.gsub(str, " ", "");
        --new_str = string.gsub(new_str, "[ ]+$", "");
        new_str = string.gsub(new_str, "[%,]", "");
        return new_str;
    end;
end;

function input_onblur()
  if onblur_flag =="1" then
    onblur_flag = nil;
  else
    local node_fomat = ert("#inuput_money"):val();
    if node_fomat == "." then
      window:alert("请输入正确的金额");
      return
    end
    --输入金额第二次校验
    if tonumber(node_fomat) == 0 then
      window:alert("输入金额不能为0");
      return
    end
    node_fomat = string.gsub(node_fomat..",", ",", "");
    node_fomat = string.sub(node_fomat, 1, 13);
    --输入金额第二次校验 拦截0.0000009923
    if format_money(node_fomat) == "0.00" then
      ert("#inuput_money"):attr("value",format_money(node_fomat));
      window:alert("输入金额不能为0");
      return
    end
    ert("#inuput_money"):attr("value",format_money(node_fomat));
  end;
end

-- 检查是否输入中文
function input_check_chinese(str)
      str = str or "";
      local _, n = str:gsub('[\128-\225]', '')

      if n > 0 then
         window:alert("证件号码不能输入中文");
         return 0;
      end;
 end;

--手机号格式化方法
function format_mobile(mobile)
    mobile = mobile or "";
    mobile = strip_str(mobile);
    return string.sub(mobile, 1, 3) .. [[****]] .. string.sub(mobile, -4, -1);
end;  

--E账号格式化方法
function format_account(account)
    account = account or "";
    mobile = strip_str(account);
    return string.sub(account, 1, 4) .. [[ ]] .. [[***********]]..[[ ]].. string.sub(account, 16, 19);
end;  

--获取一个月前的日期  输入 20151127 00:00:00   to    输出：20151027
function get_early_month(dateStart)
  dateStartY=string.sub(dateStart,1,4);
  dateStartM=string.sub(dateStart,5,6);
  dateStartD=string.sub(dateStart,7,8);

  if tonumber(dateStartM) == 1 then
      dateStartY = dateStartY -1;
      dateStartM ='12';
  elseif tonumber(dateStartM) == 4 or tonumber(dateStartM) == 6 or tonumber(dateStartM) == 9 or tonumber(dateStartM) == 11 then --每月三十天；
      dateStartM = dateStartM -1;
  elseif tonumber(dateStartM) == 2 or tonumber(dateStartM) == 5 or tonumber(dateStartM) == 7 or tonumber(dateStartM) == 10 or tonumber(dateStartM) == 12 then --每月三十一天以及二月；
      dateStartM = dateStartM - 1;
      if tonumber(dateStartD) == 31 then
        dateStartD = 30;
      end;
  else --三月份
      if (dateStartY%4 == 0 and dateStartY%100 ~= 0 )or (dateStartY%400 == 0) then
        if tonumber(dateStartD) == 30 or tonumber(dateStartD) == 31 then
          dateStartD = 29;
        end
      else
        if tonumber(dateStartD) == 29 or tonumber(dateStartD) == 30 or tonumber(dateStartD) == 31 then
          dateStartD = 28;
        end
      end
    dateStartM = dateStartM - 1;
  end
  local  maxl_m = string.len(dateStartM);
  local  maxl_d = string.len(dateStartD);
  if maxl_m == 1 then         --长度判断;
    dateStartM = '0'.. dateStartM;
  end;
  if maxl_d == 1 then         --长度判断;
    dateStartD = '0'.. dateStartD;
  end;
  result_date=dateStartY..dateStartM..dateStartD;
  return result_date;
end
  
--日期提取格式化- 输入 2015-11-27 00:00:00   to    输出：11-17 
function format_date_month_day(date)
  local date_1 = string.sub(date,6,10);
  return date_1;
end

--日期提取格式化- 输入 2015-11-27 00:00:00   to    输出：2015-11-17 
function format_date_year_month(date)
  local date_1 = string.sub(date,1,10);
  return date_1;
end

--日期式化方法 20150708->2015-07-08
function format_date(date)
    date = date or "";
    return string.sub(date, 1, 4) ..'-'.. string.sub(date, 5, 6)..'-'.. string.sub(date, 7, 8);
end;

--日期式化方法 20150708->7月8号
function format_date_to_chinse(date)
    date = date or "";
    return tostring(tonumber(string.sub(date, 5, 6)))..'月'.. tostring(tonumber(string.sub(date, 7, 8)))..'日';
end;

--日期式化方法 2015-07-08->20150708
function change_date(date)
    date = date or "";
    if string.find(date, "-") then
        return string.sub(date, 1, 4) .. string.sub(date, 6, 7) .. string.sub(date, 9, 10);
    else
        return date;
    end
end

function update_callback(response)
  local code = response["responseCode"];
      if code == 200 then

          local json_data = response["responseBody"] or {};
         --  window:alert(json_data)
          local json_tbl = json:objectFromJSON(json_data);
          local t_body = json_tbl["body"] or {};
          local c_version = t_body["c_version"];--客户端版本号
          local s_version = t_body["s_version"];--服务器版本号
          local ad_url = t_body["ad_url"];--安卓下载地址
          local ip_url = t_body["ip_url"];--IOS下载地址

          window:alert("c_version:"..c_version.."-----s_version:"..s_version)
          if c_version ~= s_version then
            local model = system:getInfo("model")--手机类型
            local url = "";--下载地址
            if model=="iPhone" then
              url = ip_url;
            else
              url = ad_url;
            end
            local context = {model=model,c_version=c_version,s_version=s_version,url=url};
            ert.channel:first_page("ebank_other", "UPDATE02",{id = "ebank_other",trancode = "UPDATE02"},{just_page=true,context = context});
           else
             window:alert("你的客户端是最新的版本!")
            end
      else
        window:alert("网络请求失败")
      end
end

--检查更新
function check_update() 
  -- local channelId = "ebank_other";
  -- local trancode = "UPDATE01";
  -- local post_body = {id = channelId,tranCode = trancode};
  -- ert.channel:first_page(channelId, trancode,post_body,{request_callback=update_callback});
  window:alert("你的客户端是最新的版本!")
end

--身份证验证最后一位校验码
function certificate_verify(param)
    local x = 0;
    local y = 0;
    local a = string.sub(param, 1, 17);
    local a_id = string.sub(param, 18, 18);
   -- window:alert(a_id)
    if a_id == "x" then
      a_id = "X"
    end
    local b = "7910584216379105842";
    local c = "10X98765432";
    local sum = 0;
    for var=1,17,1 do
        x=x+1;
        y=y+1;

        local a1 = string.sub(a, x, x);
        local b1 = 0;
        if y==3 or y==14 then  
            b1 = string.sub(b, y, y+1);
            y=y+1;
        else
            b1 = string.sub(b, y, y);
        end
        sum=sum+tonumber(a1)*tonumber(b1);
    end 
    local remainder=tonumber(sum)%11;

    local verifycode=string.sub(c, remainder+1, remainder+1);
    if verifycode ~= a_id then
        window:alert("请输入正确的身份证号码");
        return 0;
    else           
        return 1;
    end
end

--输入框去空格方法
function onblur_empty_format(id)
   local str_id = ert("#"..id):val();
   ert("#"..id):attr("value",strip_str_empty(str_id));
end

  --截取卡号后4位
  function acc_sub_four(bankAccNo)
      local num_len = string.len(bankAccNo);
      return "("..string.sub(bankAccNo,num_len-3,num_len)..")";
  end

  --银行卡格式化方法
function format_account_num(account)
    account = account or "";
    mobile = strip_str(account);
    local num_len = string.len(account);
    return string.sub(account, 1, 4) .. [[ ]] .. [[***********]]..[[ ]].. string.sub(account, num_len-3, num_len);
end; 