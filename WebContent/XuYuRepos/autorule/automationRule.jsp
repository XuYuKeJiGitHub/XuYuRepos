<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>规则管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
    	<form id="queryForm" method="post">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
            <p>
	            <label>规则名称:</label><input type="text" name="ruleName" id="ruleNameQuery"/>
	            <label>制定规则时间:</label>
	                                 <input name="startDate" id="startDate" style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
	                                 <input name="endDate" id="endDate"     style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
                <label>状态:</label>
					                <select class="easyui-combobox" name="isValidate" id="isValidate">
					                	<option value="">全部</option>
					                    <option value="N">失效</option>
						                <option value="Y">正常</option>
					                </select>
	            
            </p>
            <p class="findButAlign">
                <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
	            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
            </p>
           
        </div>
        </form>
        <div id="cc" class="easyui-calendar"></div>
         
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-add'" onclick="obj.addBox()">新增规则</a>
                </div>
         </div>
        </div>
    </div>
</div>
 <div id="addBox" style="display:block">
					    <form id="addForm" method="post">
					    	<label>规则名称:</label>
					    	<div class="formDiv">
					    	<label hidden="true"></label>
					        <input type="text" class="easyui-validatebox" id="ruleName" name="ruleName" data-options="required:true"><span class="formSpan">*</span>
					        <input id="id" name="id" hidden="true" type="text"/>
					        </div>
					        <label>选择规则类型:</label>
						     <div class="formDiv">
						     	<label hidden="true"></label>
<!-- 						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;" type="radio" name="ruleType" value="01" onchange="obj.choseUnit()">数据服务昨日使用量:</input> -->
						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="02" onchange="obj.choseUnit()">流量池使用百分比:</input>
						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="03" onchange="obj.choseUnit()" checked="checked">流量剩余量:</input>
<!-- 						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="04" onchange="obj.choseUnit()">公司流量累计使用量:</input> -->
					         </div>
					         <div class="formDiv">
					         <label hidden="true"></label>
						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="05" onchange="obj.choseUnit()">有效期到期提醒:</input>
						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="06" onchange="obj.choseUnit()">沉默期到期提醒:</input>
						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="07" onchange="obj.choseUnit()">沉默期到期(客户)提醒:</input>
<!-- 						         <input   style="border: 2px solid #0d9aec;background-color: #0d9aec;"type="radio" name="ruleType" value="08" onchange="obj.choseUnit()">套餐最大差异量:</input> -->
					         </div>
					         <div  id="setDays" style="display:none">
						         <label style="margin: 10px 35px 5px 5px ;">提前天数设置:</label><input type="text" id="days" name="days"/><label>(天)</label>
					         </div>
					         <div  id="setGprs"  style="display:block;" >
						         <label style="margin: 10px 35px 5px 5px ;">流量用量设置:</label><input type="text" id="gprs" name="gprs"/><label>(MB)</label>
					         </div>
					         <div  id="setRate"  style="display:none">
						         <label style="margin: 10px 35px 5px 5px ;">流量百分比设置:</label><input type="text" id="rate" name="rate"/><label>(%)</label>
					         </div>
					        <label>定义触发范围:</label>
					        <div class="formDiv">
					        	<label hidden="true"></label>
						         <input style="border: 2px solid #0d9aec;" type="radio" name="bindType" value="01" onchange="obj.choseType()">手动选择</input>
						         <input style="border: 2px solid #0d9aec;" type="radio" name="bindType" value="02" onchange="obj.choseType()">号码段设置</input>
						         <input style="border: 2px solid #0d9aec;" type="radio"name="bindType" value="03"  onchange="obj.choseType()">群组维度设置</input>
<!-- 						         <input style="border: 2px solid #0d9aec;" type="radio"name="bindType" value="04" checked="checked" onchange="obj.choseType()">适应所有</input> -->
					        </div>
					        <div class="formDiv" id="inputCard" style="display:none;">
					        	<label>输入卡号:</label>
					        	<div class="formDiv"><input style="width:620;height:150px;" class="easyui-textbox" name="accessNums" id="accessNums" data-options="multiline:true"></input></div>
					        </div>
					        <div class="formDiv" id="intervalCard" style="display:none;">
					        	<label>使用卡段:</label>
					        	<div class="formDiv"><label>卡段开始:</label><input type="text" id="accessNumStartId"  name="accessNumStart" class="easyui-validatebox" data-options="required:true"/><span class="formSpan">*</span></div>
								<div class="formDiv"><label>卡段结束:</label><input type="text"  id="accessNumEndId" name="accessNumEnd"   class="easyui-validatebox" data-options="required:true"/><span class="formSpan">*</span></div>
					        </div>
					        <div class="formDiv" id="groupCard" style="display:none;">
					        	<label>选择群组:</label>
					        	<input type="text"   name="ownerName" id="ownerName" editable="false" class="easyui-validatebox" data-options="required:true"><span class="formSpan">*</span><a href="#" class="easyui-linkbutton" onclick="obj.openOwner()">选择</a>
        						<input type="text" hidden="true" id="ownerId"  name="ownerId">
					        </div>
					        <!-- table class="easyui-datagrid" title="物联网卡" style="width:640px;height:250px" style="display:none;"
								data-options="singleSelect:true,collapsible:true,url:'datagrid_data1.json',method:'get'">
							<thead>
								<tr>
									<th data-options="field:'itemid',width:80">物联卡号</th>
									<th data-options="field:'productid',width:80">企业ID</th>
									<th data-options="field:'listprice',width:80,align:'right'">卡号备注</th>
									<th data-options="field:'unitcost',width:80,align:'right'">物联卡状态</th>
									<th data-options="field:'attr1',width:80">开户时间</th>
									<th data-options="field:'status',width:80,align:'center'">ICCID</th>
									<th data-options="field:'status',width:80,align:'center'">IMSI</th>
									<th data-options="field:'status',width:80,align:'center'">群组</th>
								</tr>
							</thead>
						   </table-->
						   <label>定义触发动作:</label>
					        <!-- div class="formDiv03">
						        <label>短信发送时间 从:</label>
						        <input class="easyui-datebox" id= data-options="sharedCalendar:'#cc'" style="width:120px;">
						        <label>至:</label>
						        <input style="width:120px;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
					        </div-->
					         <div class="formDiv">
						       <label hidden="true"></label>
						         <input style="border: 2px solid #0d9aec;" type="radio" name="oprateType" value="01"  checked="checked" >仅提醒</input>
						         <input style="border: 2px solid #0d9aec;" type="radio" name="oprateType" value="02" >自动停机</input>
						         <input style="border: 2px solid #0d9aec;" type="radio"name="oprateType" value="03" >自动复机</input>
					        </div>
					         <div class="formDiv"><label>发送短信至:</label><input type="text" id="messageId" name="messageId"/>
					        </div>
					        
					         <div class="formDiv"><label>发送邮箱至:</label><input type="text" id="emailId" name="emailId"/>
					        </div>
						   
					        <div class="forSubmint">
					            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">确定</a>
					            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.res()">取消</a>
					        </div>
					    </form>
					    <form id="openOwnerForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/groupChoose.jsp" method="post" target="newOwnerWin">
   						 </Form>  
					</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=automationRule.js></script>


</body>
</html>