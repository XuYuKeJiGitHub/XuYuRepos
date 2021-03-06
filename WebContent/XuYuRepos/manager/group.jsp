<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>群组管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
              <form id="queryForm" method="post">
	             <p>
		            <label>套餐名称:</label><input type="text" name="comboName" id="comboNameQuery" class="easyui-textbox">
		            <label>支付类型:</label><select  name="payType" id="payTypeQuery"  prompt="请选择"  class="easyui-combobox"></select>
		            <label>群组名称:</label><input type="text" name="ownerName" id="ownerNameQuery" class="easyui-textbox">
	            </p>
	            <p class="findButAlign">
	                <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.resQuery()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  iconCls="icon-add" onclick="obj.addBox()">新增群组</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  iconCls="icon-edit" onclick="obj.edit()">编辑群组</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  iconCls="icon-remove" onclick="obj.del()">删除群组</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  iconCls="icon-edit" onclick="">维护群组数据</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>套餐类型:</label><select  name="comboType" id="comboType"  style="width:368px" prompt="请选择"  class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>套餐:</label><select  name="comboName" id="comboName"  style="width:368px" prompt="请选择"  class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>群组代码:</label><input type="text" name="owner" id="owner" class="easyui-validatebox"></div>
        <div class="formDiv"><label>群组名称:</label><input type="text" name="ownerName" id="ownerName" class="easyui-validatebox" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>支付类型:</label><select  name="payType" id="payType"  style="width:368px" prompt="请选择"  class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>沉默期类型:</label><select  name="silentType" id="silentType"  style="width:368px" prompt="请选择"  class="easyui-combobox"></select></div>
        <div class="formDiv"><label>测试期:</label><select  name="haveTest" id="haveTest"  style="width:368px" prompt="请选择"  class="easyui-combobox"></select></div>
        <div class="formDiv"><label>代理商:</label><select  name="companyId" id="orgName" style="width:368px" prompt="请选择" class="easyui-combotree"></select></div>    
        <!--  
        <input type="text" name="companyName" id="orgName" prompt="请选择"  class="easyui-validatebox" data-options="required:true"><a href="#" class="easyui-linkbutton" onclick="openOrg()">选择</a></div>-->
        <input type="text" hidden="true" id="orgId"  name="companyName" >
        <input type="text" hidden="true" id="id"  name="id" data-options="required:true">
        <div class="forSubmint"> <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">提交</a>
            <a href="#" class="easyui-linkbutton" id="res" iconCls="icon-redo" onclick="obj.res()" >重置</a>
            <a href="#" class="easyui-linkbutton" id="can" iconCls="icon-cancel" onclick="obj.can()" >取消</a>
        </div>
    </form>
    
     <form id="openForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/system/orgChoose.jsp" method="post" target="newWin">
               <input type="text" hidden="true" id="openOrgId"  name="openOrgId" data-options="required:true">
    </form>  
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=group.js></script>


</body>
</html>