<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>默认界面</title>
    <link type="text/css" rel="stylesheet" href="js/jquery-easyui-1.7.0/themes/default/easyui.css"/>
    <link type="text/css" rel="stylesheet" href="js/jquery-easyui-1.7.0/themes/icon.css"/>
    <link type="text/css" rel="stylesheet" href="css/main.css">
	<script src="js/jquery-easyui-1.7.0/jquery.min.js"></script>
	<script src="js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
	<script src="js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
<div class="allBox">

<!--  
<div class="homeLeft01">
    <a href="#" class="homeUpaBox">
        <div class="aLeft"><img src="images/index/computer.png"/> </div>
        <div class="aRight">
            <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">个</span></p>
            <p class="fontSizeMin">待办未处理</p>
        </div>
    </a>
   <a href="#" class="homeUpaBule">
    <div class="aLeft"><img src="images/index/computer.png"/> </div>
    <div class="aRight">
        <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">元</span></p>
        <p class="fontSizeMin">我的询价金额</p>
    </div>
    </a>
</div>
    <div class="homeLeft01">
        <a href="#" class="homeUpaBule03">
            <div class="aLeft"><img src="images/index/computer.png"/> </div>
            <div class="aRight">
                <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">个</span></p>
                <p class="fontSizeMin">待办未处理</p>
            </div>
        </a>
        <a href="#" class="homeUpaBule04">
            <div class="aLeft"><img src="images/index/computer.png"/> </div>
            <div class="aRight">
                <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">元</span></p>
                <p class="fontSizeMin">我的询价金额</p>
            </div>
        </a>
    </div>
    <div class="homeLeft01">
        <a href="#" class="homeUpaBule01">
            <div class="aLeft"><img src="images/index/computer.png"/> </div>
            <div class="aRight">
                <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">个</span></p>
                <p class="fontSizeMin">待办未处理</p>
            </div>
        </a>
        <a href="#" class="homeUpaBule02">
            <div class="aLeft"><img src="images/index/computer.png"/> </div>
            <div class="aRight">
                <p class="fontSizeBig" style="margin-top: 10px">14<span class="fontSizeMin">元</span></p>
                <p class="fontSizeMin">我的询价金额</p>
            </div>
        </a>
    </div>
    <div id="calder" class="homeLeft02"></div>
    <div class="homeLeft03">
        <h5 class="hStyle"><span>通知公告</span><a href="#">更多</a> </h5>
        <ul class="orderUl" id="notic">
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
        </ul>
    </div>-->
    <div class="clear"></div>
    <div class="homeLeft04">
        <h5 class="hStyle"><span>物联卡基本信息(移动)</span></h5>
        <p class="stataAny" id="chart01"></p>
    </div>
    <div class="homeLeft04">
        <h5 class="hStyle"><span>物联卡基本信息(联通)</span></h5>
        <p class="stataAny" id="chart02"></p>
    </div>
     <div class="homeLeft04">
        <h5 class="hStyle"><span>物联卡基本信息(电信)</span></h5>
        <p class="stataAny" id="chart03"></p>
    </div>
    <!-- 
    <div class="homeLeft03" style="margin-top: 5px">
        <h5 class="hStyle"><span>公开底价</span><a href="#">更多</a> </h5>
        <ul class="orderUl">
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
        </ul>
    </div> -->
    <!-- 
    <div class="clear"></div>
    <div class="homeLeft05">
        <h5 class="hStyle"><span>交货分析</span></h5>
        <p class="stataAny" style="height: 220px" id="chart03"></p>
    </div>-->
    <!-- 
    <div class="homeLeft06" id="rightTab">
        <div title="待办信息" style="display:none;">
            <ul class="orderUl">
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            </ul>
            <p class="tabConP"><a href="#">更多</a> </p>
        </div>
        <div title="已办信息"  style="overflow:auto;display:none;">
            <ul class="orderUl">
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
                <li><a href="#"> 2019年旭宇测试数据</a><span>01-23</span> </li>
            </ul>
            <p class="tabConP"><a href="#">更多</a> </p>
        </div>
    </div>-->
    <div class="clear"></div>

</div>
<script src="js/echarts/echarts.min.js"></script>
<script src="js/index/home.js"></script>


</body>
</html>