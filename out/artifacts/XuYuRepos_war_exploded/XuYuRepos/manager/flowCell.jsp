<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>流量池信息</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind">
            <p>
	            <label>企业ID;</label><input type="text"/>
	            <label>群组;</label><input type="text"/>
	            
	             <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
	            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.find()">重置</a>
            </p>
             
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.export()">导出</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>卡号:</label><input style="width:80%;height:160px;" class="easyui-textbox" name="message" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>短信内容:</label><input style="width:80%;height:160px;" class="easyui-textbox" name="message" data-options="multiline:true"></input></div>
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">发送短信</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">查看短信记录</a>
        </div>
    </form>
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=flowCell.js></script>


</body>
</html>