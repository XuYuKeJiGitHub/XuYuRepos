<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>系统审计日志</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
   	 <form id="queryForm" method="post">
        <!--表格查询-->
        <div class="tableFind" data-options="region:'north',border:false" style="height:95px;">
            <p>
	            <label>模块名称</label>
       			<select class="easyui-combobox" name="modelId" id="modelId">
       				<option value="">全部</option>
	                <option value="cardManager">卡管理</option>
	                <option value="cardSend">卡出库</option>
	                <option value="gprsPool">流量池信息</option>
               </select>
	            <label>操作时间</label>
						              <input name="startDate"  id="startDate" style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" >
						              <input name="endDate"    id="endDate"   style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" >
                <label>操作人</label><input name="oprate" id="oprate" type="text" class="easyui-textbox"/>
            </p>
            <p>
                <label>备注</label><input   name="mark" id="mark"     type="text" class="easyui-textbox"/>
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
                    <a href="#" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" plain="false"  onclick="obj.export1()">导出</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>规则名称:</label><input type="text" class="easyui-validatebox" name="name" data-options="required:true"><span class="formSpan">*</span></div>
        <div class="formDiv"><label>选择规则类型:</label>
	         <input class="easyui-radiobutton" name="fruit" value="1" label="本月短信累计使用量：">
	         <input class="easyui-radiobutton" name="fruit" value="2" label="本月数据服务累计使用量：">
        </div>
        
        <div class="formDiv"><label>定义触发范围:</label>
	         <input class="easyui-radiobutton" name="fruit" value="1" label="手动选择：">
	         <input class="easyui-radiobutton" name="fruit" value="2" label="文件批量导入：">
        </div>
        
        <table class="easyui-datagrid" title="物联网卡" style="width:700px;height:250px"
			data-options="singleSelect:true,collapsible:true,url:'datagrid_data1.json',method:'get'">
		<thead>
			<tr>
				<th data-options="field:'itemid',width:80">物联卡号</th>
				<th data-options="field:'productid',width:100">企业ID</th>
				<th data-options="field:'listprice',width:80,align:'right'">卡号备注</th>
				<th data-options="field:'unitcost',width:80,align:'right'">物联卡状态</th>
				<th data-options="field:'attr1',width:250">开户时间</th>
				<th data-options="field:'status',width:60,align:'center'">ICCID</th>
				<th data-options="field:'status',width:60,align:'center'">IMSI</th>
			</tr>
		</thead>
	   </table>
	   
	    <div class="formDiv"><label>定义触发条件与动作:</label>
        </div>
        
        <div class="formDiv"><label>物联卡状态变更从:</label>
	                               <select class="easyui-combobox">
					                    <option>请选择</oprtion>
						                <option>正常</oprtion>
						                <option>停机</oprtion>
					                </select>
					         <label>变更为:</label>
	                                <select class="easyui-combobox">
					                    <option>请选择</oprtion>
						                <option>正常</oprtion>
						                <option>停机</oprtion>
					                </select>
        </div>
        
        <div class="formDiv">
	        <label>执行：短信发送时间:</label>
	        <input class="easyui-datebox" data-options="sharedCalendar:'#cc'" style="width:8%;">
	        <label>至:</label>
	        <input style="width:8%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
        </div>
        
         <div class="formDiv"><label>发送短信至;</label><input type="text"/>
        </div>
        
         <div class="formDiv"><label>发送邮箱至;</label><input type="text"/>
        </div>
	   
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">取消</a>
        </div>
    </form>
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
</script>
<script src=loggerInfo.js></script>


</body>
</html>