<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>客户充值记录</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:180px;">
            <form id="queryForm" method="post">
	            <p>
	                <label>关键字:</label>
	                                       <input type="text" name="accessNum" id="accessNumQuery" class="easyui-textbox"/>
	                <label>充值时间:</label>
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="createDateStartQuery">
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="createDateEndQuery">
                    <label>充值方式:</label>
	                                       <select  name="tradeType" id="tradeTypeQuery"      prompt="请选择"  class="easyui-combobox"></select>								           
	            </p>
	            <p>
	                <label>充值状态:</label>
	                                 <select  name="orderStatus" id="orderStatusQuery"      prompt="请选择"  class="easyui-combobox"></select>
	               <label>接入号段:</label>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumEndQuery"/>
	               <label>ICCID:</label>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidEndQuery"/>		
	            </p>
	            <p>
	                <label>客户名称:</label>
	                                <select  name="agency" id="agencyQuery" prompt="请选择"  class="easyui-combotree"></select>
	                <label>预充值:</label>
	                                 <select  name="yc" id="ycQuery"      prompt="请选择"  class="easyui-combobox"></select>	
	                <label>加油包:</label>
	                                 <select  name="comeon" id="comeonQuery"      prompt="请选择"  class="easyui-combobox"></select>	                 	
	            </p>
	            <p>
	                <label>有效期:</label>
                                      <select  name="availableType" id="availableTypeQuery" prompt="请选择"  class="easyui-combobox"></select> 
                    <a id="btnQuery" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="btnReset" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>     
	            </p>
            </form>
        </div>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-redo" onclick="obj.export()">导出</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="rechargecustomer.js"></script>


</body>
</html>