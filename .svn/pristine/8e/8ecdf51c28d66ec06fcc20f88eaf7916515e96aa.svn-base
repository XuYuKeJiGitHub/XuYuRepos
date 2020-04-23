<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>短信发送记录查询</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" id="tableFind" data-options="region:'north',border:false" style="height:95px;">
              <form id="queryForm" method="post">
	             <p>
		             <label>接入号:</label>
                                     <input  name="accessNum" id="accessNumQuery"   prompt="接入号" type="text" class="easyui-textbox"/>
		            <label>运营商:</label>
		                             <select  name="provider" id="providerQuery"  prompt="请选择"  class="easyui-combobox"></select>
		            <label>二级运营商:</label>
	                                 <select  name="ownerPlace" id="ownerPlaceQuery"  prompt="请选择"  class="easyui-combobox"></select>                      
		           
	            </p>
	            <p>
	                <label>短信内容:</label>
                                     <input  name="message" id="message"   type="text" class="easyui-textbox"/>
	                <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
            </div>
        </div>
    </div>
</div>
<script src=message.js></script>


</body>
</html>