<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>充值套餐</title>
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

		            <p  class="findButAlign">
                        <label>流量套餐:</label>
                                             <input   name="gprs" id="gprsQuery" prompt="套餐：M" type="text" class="easyui-textbox"/>
                        <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
			            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
		            </p>
		        </div>
        </form>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-add'"    id="addbtn" onclick="obj.addBox()">新增套餐</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-edit'"   id="editbtn" onclick="obj.edit()">编辑套餐</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-remove'" id="deletebtn" onclick="obj.del()">删除套餐</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-edit'"   id="sendbtn" onclick="obj.send()">发送套餐</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-tip'"                 onclick="obj.setState()" >上架/下架</a>
                    <!-- 
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-add'" onclick="obj.payNotify()">接口测试</a> -->
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
    
        <div class="formDiv"><label>流量(MB):</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,required:true" name="gprs" id="gprs"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>排序:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,required:true" name="order" id="order"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>售价:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,precision:2,required:true" name="selfPrice" id="selfPrice"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>原价:</label><input type="text" style="width:335px" class="easyui-numberbox"  data-options="min:0,precision:2,required:true" name="actualPrice" id="actualPrice"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>套餐描述:</label><input style="width:80%;height:80px;width:335px" class="easyui-textbox" name="desc" id="desc" data-options="multiline:true,validType:'length[0,100]'"></input></div>
        <div class="formDiv"><label>加油包:</label><select  name="comeon" id="comeon"  style="width:335px" prompt="请选择" data-options="required:true" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>折扣比例:</label><select  name="discount" id="discount"  style="width:335px" prompt="请选择"  class="easyui-combobox"></select></div>
        <div class="formDiv"><label>运营商:</label><select  name="opera" id="opera"  style="width:335px" prompt="请选择" data-options="required:true" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>有效期:</label><select  name="availableType" id="availableType"  style="width:335px" prompt="请选择" data-options="required:true" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <input type="text" hidden="true" id="id"  name="id" data-options="required:true">
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">保存</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.can()">取消</a>
        </div>
    </form>
</div>
<!--发送弹出框-->
<div id="sendBox" >
     <form id="sendForm" method="post">
          <div class="formDiv"><label>数据ID:</label><input style="width:335px;height:160px;"   class="easyui-textbox" name="ids" id="ids" data-options="multiline:true"></input></div>
          <div class="formDiv"><label>客户名称:</label><select style="width:335px;"   name="agency" id="agency" prompt="请选择" data-options="required:true" class="easyui-combotree"></select></div>
          <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumSend()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canSend()">取消</a>
        </div>    
     </form>
</div>
<script src=payPackages.js></script>


</body>
</html>