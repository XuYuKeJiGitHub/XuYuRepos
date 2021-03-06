<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <!--左侧树-->
    <div data-options="region:'west',split:true,title:'机构数据'", style="width: 200px">
        <ul id="tt">
		</ul>
    </div>
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:65px;">
            <form id="queryForm" method="post">
	            <p>
	                 <label>账号:</label><input type="text" id="usernameQuery"/>
	                 <label>姓名:</label><input type="text" id="cnameQuery" />
	                 <a id="btnQuery" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
				     <a id="btnReset" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="obj.addBox()">新增</a>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit"   onclick="obj.edit()">修改</a>
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="obj.del()">删除</a>
                    <!--  
                    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit"   onclick="obj.editRole()">用户角色修改</a>-->
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>账号:</label><input type="text" name="userName" id="userName" class="easyui-validatebox" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>姓名:</label><input type="text" name="cname" id="cname" class="easyui-validatebox" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>邮箱:</label><input type="text"  name="email" id="email" validType="email" data-options="required:true" invalidMessage="请输入正确的邮箱"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>手机号码:</label><input type="text" name="mobile" id="mobile" validType="phoneNum" data-options="required:true" invalidMessage="请输入正确的手机号码"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>机构:</label><select id="orgNameQuery" style="width:335px" prompt="请选择" class="easyui-combotree"></select></div>
        <div class="formDiv"><label>部门:</label><select id="deptNameQuery" style="width:335px" prompt="请选择" class="easyui-combotree"></select></div>
        <div class="formDiv"><label>性别:</label><select  name="sex" id="sex"  style="width:335px" prompt="请选择"  class="easyui-combobox"></select></div>
        <input type="text" hidden="true" id="orgId"  name="orgId" >
        <input type="text" hidden="true" id="deptId"  name="deptId">
        <input type="text" hidden="true" id="orgName"  name="orgName" >
        <input type="text" hidden="true" id="deptName"  name="deptName">
        <input type="text" hidden="true" id="id"  name="id" data-options="required:true">

        <div class="forSubmint"> <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">提交</a>
            <a href="#" class="easyui-linkbutton" id="res" iconCls="icon-redo" onclick="obj.res()" >重置</a>
            <a href="#" class="easyui-linkbutton" id="can" iconCls="icon-cancel" onclick="obj.can()" >取消</a>
        </div>

    </form>
    
    <form id="openForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/system/orgChoose.jsp" method="post" target="newWin">
               <input type="text" hidden="true" id="openOrgId"  name="openOrgId" data-options="required:true">
    </Form>  
</div>

<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src="user.js"></script>


</body>
</html>