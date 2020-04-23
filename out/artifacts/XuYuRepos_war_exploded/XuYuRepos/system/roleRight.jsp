<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>菜单权限管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <!--左侧树-->
    <div data-options="region:'east',split:true,title:'菜单数据'", style="width: 300px">
        <div id="treeBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="obj.save()">保存</a>
                </div>
        </div>
		<ul id="tt" class="easyui-tree" data-options="method:'get',animate:true,checkbox:true">
		</ul>
    </div>
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
            <form id="queryForm" method="post">
	            <p>
	                <label>角色名称：</label><input type="text" id="rolenameQuery" class="easyui-textbox"/>
	                <label>角色代码：</label><input type="text" id="roleCodeQuery" class="easyui-textbox"/>
	            </p>
	            <p>
	                <label>角色类型：</label><select  name="roleType" id="roleTypeQuery" prompt="请选择"  class="easyui-combobox"></select>
	                <a id="btnQuery" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
			        <a id="btnReset" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table"  data-options="region:'center'"  class="tableStyle"></table>
            <div id="tabelBut">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src="roleRight.js"></script>


</body>
</html>