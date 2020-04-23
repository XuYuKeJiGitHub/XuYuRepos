<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>物联卡满期提示</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body>
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
    	<form id="queryForm" method="post">
        <!--表格查询-->
	        <div class="tableFind" data-options="region:'north',border:false" style="height:205px;">
	            <p>
	            	<label>运营商:</label><select  name="providerQuery" id="providerQuery1"  editable="false" class="easyui-combobox"></select>
					<label>满期日到期时间:</label><input class="easyui-datebox" name="startDDate" id="startDDate" data-options="sharedCalendar:'#cc'" style="width:8%;"><input style="width:8%;"  name="endDDate" id="endDDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
	            </p>
	            <p>	
	            	<label>二级地区:</label><select  name="ownerPlace" id="ownerPlace1"  editable="false" class="easyui-combobox"></select>
	            	<label>沉默期到期时间:</label><input class="easyui-datebox" name="startWDate" id="startWDate" data-options="sharedCalendar:'#cc'" style="width:8%;"><input style="width:8%;"  name="endWDate" id="endWDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
	            </p>
	            <p>
	            	<label>客户:</label><select  name="agency" id="agency1"  prompt="请选择" class="easyui-combotree"></select>
	                <label>实际沉默期到期时间:</label><input class="easyui-datebox" name="startRWDate" id="startRWDate" data-options="sharedCalendar:'#cc'" style="width:8%;"><input style="width:8%;"  name="endRWDate" id="endRWDate" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
	            </p>
	            <p>
	                <label>群组:</label><select  name="ownerId" id="ownerId1"  editable="false" class="easyui-combobox"></select>
	                <label>接入号:</label><input type="text" id="accessNum" name="accessNum"/>
		         </p>
		         <p  class="findButAlign">
		            <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.res()">重置</a>
	             </p>
	           
	        </div>
        </form>
        <div class="formDiv" hidden="true">
	        <select id="comboType" style="display：none" class="easyui-combobox"></select>
	        <select id="comboName" style="display：none" class="easyui-combobox"></select>
            <select id="workingConditionYd"  style="display：none" class="easyui-combobox"></select>
            <select id="workingConditionLt"  style="display：none" class="easyui-combobox"></select>
            <select id="workingConditionDx"  style="display：none" class="easyui-combobox"></select>
            <select id="billingStatus"  style="display：none" class="easyui-combobox"></select>
        </div>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon" data-options="region:'center'">
            <table id="table" class="tableStyle"></table>
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
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/src/jquery.form.js"></script>
<script src=deadLineDateInfo.js></script>
</body>
</html>