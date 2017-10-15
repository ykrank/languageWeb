#### xhtml
### 界面标签属性

1. body
***
padding
    1. 属性名： padding
    2. 作用：控制div是否有10px 的默认间隔;
    3. 示例：padding="none"
top
    1. 属性名：top
    2. 作用：控制页面body距手机顶部状态栏高度
    3. 示例：top=0px
***
***


2. input
***
text
    1. 属性名:
    2. 用13
***
***

3. button
***

    1. 属性名：wait
    2. 作用：客户端点击延时属性，一般用于防重复点击
***
***

4. imagescroller 广告图控件
***
pageControlType
    1.  属性名： pageControlType
    2.  作用：？？
    3. pageControlType = "402" ？
title
    1.  属性名： title
    2.  作用：在广告图上显示一个可关闭的提示文本
    3. title="提示文本"
clear
    1.  属性名： clear
    2.  作用：在广告图上显示一个可关闭的提示文本
    3.  示例：clear = "true"
***


#### lua
显示login框效果
    1. 作用：显示login框效果
    2. 代码示例：ryt:show("local:FullSLoading.xml",load,true);

关闭login效果  ryt:hide(flag)
    1. 作用：关闭login框效果
    2. 代码示例：window:hide(load);

调用客户端执行返回
    1. 作用：调用客户端执行返回
    2. 代码示例：ryt:back();

？？ ryt:add(page)
    1. 作用：加载页面内空
    2. 代码示例：ryt:add(responseDate["responseBody"]);


？？？？？

ryt:clearInterval(myTimer);

database:addData(name,value);
    1. 作用：向客户端数据库添加变量
    2. 示例：database:addData(name,value);

返回指定对象的集合    getElementsByName("div4");
    1. 作用：返回指定对象的集合
    2. 示例：local loadnote = document:getElementsByName("div4");
            loadnote[1]:setInnerHtml("div_conment");

#### cs



#### css
定位
left
right
top
bottom

宽高
width
height

font-size
color
background-image:url(xxx/xx.png);

position:fixed
