<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>充值地址生成</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:65px;">
             <p>
	             <label>客户名称:</label>
	                                <select  name="agency" id="agencyQuery" prompt="请选择"  class="easyui-combotree"></select>    
	             <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
	             <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
            </p>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-add'" onclick="obj.sum()">生成充值链接</a>
                    <a href="#" class="easyui-linkbutton copyBtn" plain="true"  data-options="iconCls:'icon-edit'" onclick="obj.copy()">复制充值链接</a>
                </div>
            </div>
        </div>
    </div>
</div>

<input type="text" id="copyVal" readonly value="被复制内容" />
</body>
 
<script src=payAddress.js></script>
</html>