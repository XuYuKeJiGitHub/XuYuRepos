<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>套餐差异量卡信息</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
    	<form id="queryForm" method="post">
        <!--表格查询-->
	        <div class="tableFind" data-options="region:'north',border:false" style="height:165px;">
	            <p>
	                <label>运营商:</label><select  name="providerQuery" id="providerQuery"  editable="false" class="easyui-combobox"></select>
	                <label>二级地区:</label><select  name="ownerPlace" id="ownerPlace"  editable="false" class="easyui-combobox"></select>
	                <label>客户:</label><select  name="agency" id="agency"  prompt="请选择" class="easyui-combotree"></select>
	            </p>
	            <p>
	            	<label>群组:</label><select  name="ownerId" id="ownerId"  editable="false" class="easyui-combobox"></select>
	                <label>接入号:</label><input type="text" id="accessNum" name="accessNum"/>
	            	<label>差异量(>):</label><input type="text" id="useGprs" name="useGprs"/>(M)
	            </p>
	            	<label>套餐:</label><select  name="comboType" id="comboType1"  editable="false" class="easyui-combobox"></select>
	            <p>
	            </p>
	           	<p class="findButAlign">
		            <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.res()">重置</a>
	           	</p>
	        </div>
        </form>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.doexport()">导出</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=comboMaxDiffInfo.js></script>
</body>
</html>