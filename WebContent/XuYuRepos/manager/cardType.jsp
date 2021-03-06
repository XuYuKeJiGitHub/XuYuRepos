<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>卡类管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <!--左侧树-->
    <div data-options="region:'west',split:true,title:'卡名称数据'", style="width: 200px">
        <ul id="tt">
		</ul>
    </div>
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind">
            <form id="queryForm" method="post">
	            <p>
	               <label>卡名称;</label><input type="text" id="cardNameQuery"/>
	               <input type="text" name="" style="display: none;"/>
	               <a id="btnQuery" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
				   <a id="btnReset" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" class="tableStyle"></table>
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
        <div class="formDiv"><label>卡名称:</label><input type="text" name="cardName" id="cardName" class="easyui-validatebox" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>上级卡名称:</label><select  name="cardOrgId" id="cardOrgId"  style="width:335px" editable="false" class=""easyui-combotree"></select></div>
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
<script src="cardType.js"></script>


</body>
</html>