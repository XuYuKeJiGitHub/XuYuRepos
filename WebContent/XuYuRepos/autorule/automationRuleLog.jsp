<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>规则执行日志</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
    	<form id="queryForm" method="post">
        <!--表格查询-->
	        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
	            <p>
		            <label>规则名称:</label><input type="text" id="ruleNameQuery" name="ruleNameQuery"/>
					<label>触发规则时间:</label>
										<input name="startDate" id="startDate" style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
										<input name="endDate" id="endDate"     style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
	                <label>处理方式:</label>
						                <select class="easyui-combobox" id="operateType" name="operateType">
						                    <option value="01">仅提醒</option>
							                <option value="02">停机</option>
							                <option value="03">复机</option>
						                </select>
	            </p>
	            <p>
	                <label>处理状态:</label>
						                <select class="easyui-combobox" name="isDeal" id="isDeal">
						                    <option value="N">待处理</option>
							                <option value="Y">已处理</option>
						                </select>
		            <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.res()">重置</a>
	            </p>
	           
	        </div>
        </form>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-redo'" onclick="obj.doexport()">导出</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=automationRuleLog.js></script>
</body>
</html>