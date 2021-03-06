﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="base.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Frameset//EN">
<head>
    <title>物联网平台首页</title>
    <link type="text/css" rel="stylesheet" href="css/main.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.md5.js"></script>
</head>
<body style="width:100%;height:100%;">

<div class="easyui-layout" id="mainBox">
    <div data-options="region:'north',split:true" style="height: 60px;overflow: hidden;" class="mainTop">
        <div class="topLogin"><div class="topLongRight">物联网平台</div></div>
        <!-- 
        <div class="topText">
            <a href="home.jsp" class="textActive" target="frameName">
                <p style="margin-top: 10px"><img src="images/index/home.png"/> </p>
                <p style="margin-top: 5px">系统首页</p>
            </a>
            <a href="low.html" target="frameName" >
            <p style="margin-top: 10px"><img src="images/index/computer.png"/> </p>
            <p style="margin-top: 5px">流程图</p>
        </a>
            <a href="staAnalysis1.html"  target="frameName">
                <p style="margin-top: 10px"><img src="images/index/ogrn.png"/> </p>
                <p style="margin-top: 5px">统计分析</p>
            </a>
            <a href="sport01.html" target="frameName">
                <p style="margin-top: 10px"><img src="images/index/action.png"/> </p>
                <p style="margin-top: 5px">报表填写</p>
            </a>
        </div> -->
        <div href="javascript:void(0)" id="mb" class="easyui-menubutton topMean" data-options="menu:'#mm',iconCls:'icon-admin'"><a href="#" id="cname" style="text-decoration:none;color:#FFFFFF"></a></div>
        <div id="mm" >
            <div data-options="iconCls:'icon-man'"><a href="javascript:void(0)" onclick="openMes()">个人信息</a> </div>
            <div data-options="iconCls:'icon-redo'"><a href="javascript:void(0) "onclick="saveExit()">安全退出</a> </div>
        </div>
    </div>
    <!-- 
    <div data-options="region:'south',split:true" style="height:40px;background: #66ccff;" class="footStyle">
        版本所有@2019-2021
    </div> -->
    <div  data-options="region:'west',title:'功能菜单',split:true" style="width:200px;">
	   <div id="menu" class="easyui-accordion">
	   </div>
    </div>
    <div data-options="region:'center'" style="padding:0px;background:#eee;">
        <div id="con">
        </div>
    </div>
</div>

 <!-- 菜单项目 -->
 <div id="mm1" class="easyui-menu" style="width:140px;">
     <div data-options="name:1">关闭</div>
     <div data-options="name:2">全部关闭</div>
     <div data-options="name:3">除此之外全部关闭</div>
 </div>
    
<div id="myMes">
    <form id="addForm" method="post">
		    <p><label class="diaLable">用户名：</label><input    id="u_userName" class="easyui-validatebox TailInput" disabled="disabled" data-options="required:true,novalidate:true" ></p>
		    <p><label class="diaLable">登录名称：</label><input  id="u_cname" class="easyui-validatebox TailInput" data-options="required:true"></p>
		    <p><label class="diaLable">原密码：</label><input    id="u_old_password" type="password"   class="easyui-validatebox TailInput"></p>
		    <p><label class="diaLable">新密码：</label><input    id="u_new_password" type="password"   class="easyui-validatebox TailInput"></p>
		    <p><label class="diaLable">确认密码：</label><input  id="u_confirm_password" type="password" class="easyui-validatebox TailInput"></p>
		    <div class="forSubmint"> <a href="#" class="easyui-linkbutton"  iconCls="icon-save" onclick="obj.sum()" >保存</a><a href="#" class="easyui-linkbutton"  iconCls="icon-redo" >重置</a> </div>
    </form>
</div>
<script src="js/index/main.js"></script>

</body>
</html>