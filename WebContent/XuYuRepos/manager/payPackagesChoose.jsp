<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>选择充值套餐</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
          <form id="queryForm" method="post">
		        <div class="tableFind" data-options="region:'north',border:false" style="height:125px;">
		             <p>
			             <label>客户名称:</label>
			                                <select  name="agency" id="agencyQuery" prompt="请选择"  class="easyui-combotree"></select>    
			            <label>运营商:</label>
			                                <select  name="opera" id="operaQuery"  prompt="请选择"  class="easyui-combobox"></select>
			            <label>有效期:</label>
			                                <select  name="availableType" id="availableTypeQuery" prompt="请选择"  class="easyui-combobox"></select>               
		            </p>
		            
		            <p>
		                <label>上架状态:</label>
			                                <select  name="state" id="stateQuery" prompt="请选择"  class="easyui-combobox"></select> 
			            <label>是否为加油包:</label>
			                                <select  name="comeon" id="comeonQuery" prompt="请选择"  class="easyui-combobox"></select> 
			            <label>折扣比例:</label>
			                                <select  name="discount" id="discountQuery" prompt="请选择"  class="easyui-combobox"></select> 
		            </p>
		            <P>
		                <label>套餐:</label>
			                                <input  name="gprsquery" id="gprsquery" type="text" class="easyui-textbox"/>
		                <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
			            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
		            </P>
		        </div>
        </form>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="obj.submit()">确定</a>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit"   onclick="obj.cancle()">取消</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
    
        <div class="formDiv"><label>流量(MB):</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0" name="gprs" id="gprs"/></div>
        <div class="formDiv"><label>排序:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0" name="order" id="order"/></div>
        <div class="formDiv"><label>售价:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,precision:2" name="selfPrice" id="selfPrice"/></div>
        <div class="formDiv"><label>原价:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,precision:2" name="actualPrice" id="actualPrice"/></div>
        <div class="formDiv"><label>套餐描述:</label><input style="width:80%;height:160px;width:335px" class="easyui-textbox" name="desc" id="desc" data-options="multiline:true,validType:'length[0,100]'"></input></div>
        <div class="formDiv"><label>运营商:</label><select  name="opera" id="opera"  style="width:335px" prompt="请选择"  class="easyui-combobox"></select></div>
        <div class="formDiv"><label>有效期:</label><select  name="availableType" id="availableType"  style="width:335px" prompt="请选择"  class="easyui-combobox"></select></div>
        <input type="text" hidden="true" id="id"  name="id" data-options="required:true">
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">保存</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.can()">取消</a>
        </div>
    </form>
</div>
<script src=payPackagesChoose.js></script>


</body>
</html>