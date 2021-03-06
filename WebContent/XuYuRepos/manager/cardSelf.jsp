<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>卡出库</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
<div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind"  data-options="region:'north',border:false" style="height:125px;">
            <form id="queryForm" method="post">
	            <p>
		             <label>接入号:</label>
		                                 <input type="text"  name="accessNum" id="accessNumQuery" class="easyui-textbox"/>
		             <label>接入号:</label>
		                                 <input type="text"  style="width:10%;" name="accessNumStart" id="accessNumStartQuery" class="easyui-textbox"/>
		                                 <input type="text"  style="width:10%;" name="accessNumEnd" id="accessNumEndQuery" class="easyui-textbox"/>
		             <label>开户日期:</label> 
		                                 <input style="width:10%;" class="easyui-datebox" name="establishDateStart" id="establishDateStartQuery" data-options="sharedCalendar:'#cc'">
		                                 <input style="width:10%;" class="easyui-datebox" name="establishDateEnd" id="establishDateEndQuery" data-options="sharedCalendar:'#cc'">
	            </p>
	            <p>
	                  <label>ICCID:</label>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidEndQuery"/>
		             <label>是否首次出库:</label>
		                                 <select  name="first" id="firstQuery" prompt="请选择" class="easyui-combobox"></select>
                     <label>客户名称:</label>
                                         <select  name="agency" id="agencyQuery" prompt="请选择" class="easyui-combotree"></select>    
	            </p>
	            <p>
	                 <label>运营商:</label>
	                                 <select  name="operator" id="operatorQuery"       prompt="请选择" class="easyui-combobox"></select>
	                 <a id="btn" href="javascript:"   class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
		             <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
	            </p>
            </form>
        </div>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon" data-options="region:'center'">
            <table id="table" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-redo'" plain="true"  iconCls="icon-edit"  onclick="obj.addBox()">批量出库</a>
                    <a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-back'" plain="true"  onclick="obj.history()">历史出库记录</a>
                </div>
            </div>
        </div>
 </div>
</div> 
<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>接入号:</label><input style="width:410px;height:160px;" class="easyui-textbox" name="accessNums" id="accessNums" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>使用区间:</label><select  name="yesNo" id="yesNo"  style="width:410px" prompt="请选择" data-options="required:true" class="easyui-combobox" ></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="numType"><label>号码类型:</label> <select   name="numType" id="numTypeId"  style="width:410px" prompt="请选择" class="easyui-combobox" ></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="accessNumStart" ><label>开始号码;</label><input type="text" id="accessNumStartId" style="width:410px" name="accessNumStart" class="easyui-validatebox" data-options="required:true"/><span class="formSpan">*</span></div>
		<div class="formDiv" id="accessNumEnd"><label>结束号码;</label><input type="text"  id="accessNumEndId" style="width:410px" name="accessNumEnd"   class="easyui-validatebox" data-options="required:true"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>代理商:</label> <select  name="agency" id="orgId" prompt="请选择" style="width:410px"  class="easyui-combotree" data-options="required:true"></select>    <span class="formSpan">*</span></div>
        <div class="formDiv"><label>出库价格:</label><input type="text" name="unitCost" style="width:410px" class="easyui-textbox" data-options="required:true"/><span class="formSpan">*</span></div>
        <div class="formDiv" hidden="true">
            <input type="text" hidden="true" id="first"  name="firstQuery" >
             
            <select id="comboType" style="display：none" class="easyui-combobox"></select>
	        <select id="comboName" style="display：none" class="easyui-combobox"></select>
        </div>
       
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.can()">取消</a>
        </div>
    </form>
    
    <form id="openForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/system/orgChoose.jsp" method="post" target="newWin">
               <input type="text" hidden="true" id="openOrgId"  name="openOrgId" data-options="required:true">
    </Form>  
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=cardSelf.js></script>


</body>
</html>