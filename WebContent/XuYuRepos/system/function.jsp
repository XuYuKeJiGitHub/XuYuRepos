<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>模块管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
            <form id="queryForm" method="post">
	            <p>
	                <label>模块名称:</label><input type="text" name="funcName" id="funcNameQuery"/>
	                <label>模块描述:</label><input type="text" name="funcDesc" id="funcDescQuery"/>
	                <label>模块地址:</label><input type="text" name="action"   id="actionQuery"/>
	            </p>
	            <p  class="findButAlign">
	                <a id="btnQuery" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="btnReset" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="obj.addBox()">新增</a>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit"   onclick="obj.edit()">修改</a>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="obj.del()">删除</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>模块名称:</label><input type="text" name="funcName" id="funcName" class="easyui-validatebox" name="name" data-options="required:true"><span class="formSpan">*</span></div>
         <div class="formDiv"><label>模块描述:</label><input type="text" name="funcDesc" id="funcDesc" class="easyui-validatebox" name="pass" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>模块地址:</label><input type="text"  name="action" id="action" class="easyui-validatebox" ></div>
        <input type="text" hidden="true" id="id"  name="id" data-options="required:true">

        <div class="forSubmint"> <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">提交</a>
            <a href="#" class="easyui-linkbutton" id="res" iconCls="icon-redo" onclick="obj.res()" >重置</a>
            <a href="#" class="easyui-linkbutton" id="can" iconCls="icon-cancel" onclick="obj.can()" >取消</a>
        </div>

    </form>
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src="function.js"></script>


</body>
</html>