<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>物联卡信息管理管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
<div data-options="region:'center',split:true">
           <!--表格查询-->
           <form id="queryForm" method="post">
		        <div class="tableFind" id="tableFind" data-options="region:'north',border:false" style="height:95px;">
		            <div>
		            <p>
			            <label>关键字:</label>
			                                  <input   name="keywords" id="keywordsQuery" prompt="接入号、IMSI、ICCID" type="text" class="easyui-textbox"/>
			            <label>客户名称:</label>
			                                  <select  name="agency" id="agencyQuery" prompt="请选择" class="easyui-combotree"></select>                      
			            <label>物联卡状态:</label>
			                                  <select name="workingCondition" id="workingConditionQuery"  prompt="请选择"  class="easyui-combobox"></select>
				          
		            </p>
		            <p  class="findButAlign">
				            <a id="a2" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
				            <a id="a3" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.res()">重置</a>
				            <a id="a1" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-collapse'" onclick="obj.collapse()">&nbsp;</a>
		            </p>
                    </div>
		            
		            <div id="queryMore">
			            <p>
			                <label>运营商:</label>
			                                       <select name="operator" id="operatorQuery"  prompt="请选择"  class="easyui-combobox"></select>
				            <label>套餐类型:</label>
				                                    <select  name="comboType" id="comboTypeQuery" prompt="请选择"  class="easyui-combobox"></select>
				            <label>套餐名称:</label>
				                                    <select  name="comboName" id="comboNameQuery" prompt="请选择"  class="easyui-combobox"></select>
			            </p>
			             <p>
			                 <label>接入号:</label>
			                                     <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumStartQuery"/>
				                                 <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumEndQuery"/>
				             <label>ICCID:</label>
				                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidStartQuery"/>
				                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidEndQuery"/>
				            <label>开户日期:</label>
									               <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="establishDateStartQuery">
										           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="establishDateEndQuery">
			            </p>
			            <p>
				            <label>卡号备注:</label>
				                                   <input type="text" class="easyui-textbox" id="markQuery" class="easyui-textbox"/>
				            <label>激活日期:</label>
						                           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="activateDateStartQuery">
										           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="activateDateEndQuery">
		                    <label>群组:</label>
				                                   <input type="text" name="ownerName" id="ownerNameQuery" editable="false" class="easyui-textbox" prompt="请点击图标选择群组"/>
				            <input type="text" hidden="true" id="ownerName"  name="ownerName" onclick="fncallback()" >                       
	                        <input type="text" hidden="true" id="ownerId"  name="owner">
			            </p>
			            <p>
			                <label>余额;</label>
	                         <select  id="remainGpsType" class="easyui-combobox" prompt="请选择" style="width:10%;">
			                            <option value=">">大于</oprtion>
			                            <option value="=">等于</oprtion>
			                            <option value="<">小于</oprtion>
	                         </select>
	                         <input style="width:10%;" type="text" class="easyui-textbox" id="remainGps"/>
				            <label>到期时间:</label>
										           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="deadlineDateStartQuery">
											       <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="deadlineDateEndQuery">
				            <label>沉默期到期:</label>
				                                  <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="waitDateStartQuery">
					                              <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="waitDateEndQuery">
					                        
			            </p>
			            <p>
			                <label>是否群组:</label>
	                        <select  id="isowner"  prompt="请选择" class="easyui-combobox"></select>
					        <a id="a5" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
					        <a id="a6" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.res()">重置</a>    
					        <a id="a4" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-expand'" onclick="obj.expand()">&nbsp;</a>
			            </p>
			            <!-- 
			             <p>
				            <label>本月累计使用流量;</label>
				                                  <select style="width:10%;" name="useGprsType" id="useGprsTypeQuery" prompt="请选择"  class="easyui-combobox"></select>
	                                              <input  style="width:10%;" type="text" class="easyui-textbox" id="useGprsQuery"/>
				            <label>本月累计使用短信;</label>
				                                  <select style="width:10%;" name="messageCountType" id="messageCountTypeQuery" prompt="请选择"  class="easyui-combobox"></select>
	                                              <input  style="width:10%;" type="text" class="easyui-textbox" id="messageCountQuery"/>
				           
					           
			            </p> -->
		            </div>
		        </div>
        </form>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon" id="tableCon">
            <table id="table" class="tableStyle" data-options="region:'center'"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-redo'" onclick="obj.export()">导出</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-back'" onclick="obj.addBox()">发送短信</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-sum'" onclick="obj.addPay()">充值</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-tip'" onclick="obj.del(true)">停机</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-edit'" onclick="obj.del(false)">复机</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-lock'" onclick="obj.addMark()">备注</a>
                </div>
            </div>
        </div>
</div>
</div>
<!-- 充值弹出框 -->
<div id="addPayBox" >
    <form id="addPayForm" method="post">
        <div class="formDiv"><label>卡号:</label><input style="width:410px;height:160px;" class="easyui-textbox"     id="accessNumPay" name="accessNum" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>是否预充值:</label><select  name="yc" id="yesNoPackages"  style="width:410px" prompt="请选择"  class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="ownerNameDiv"><label>套餐:</label><input type="text"   name="payName" id="payName" style="width:410px"  editable="false" class="easyui-validatebox" data-options="required:true" ><span class="formSpan">*</span><a href="#" class="easyui-linkbutton" onclick="openPackages()">选择</a></div>
        <div class="formDiv" hidden="true">
	        <input type="text" hidden="true" id="payId"  name="payId">
        </div>
        <div class="formDiv"><label>充值方式:</label><select  name="tradeType" id="tradeType" style="width:410px" prompt="请选择" data-options="required:true" class="easyui-combobox"><span class="formSpan">*</span></select>								           </div>
        <div class="forSubmint">
             <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumPay()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canPay()">取消</a>
        </div>
    </form>
</div>
<!-- 备注弹出框 -->
<div id="addMarkBox" >
    <form id="addMarkForm" method="post">
        <div class="formDiv"><label>接入号:</label><input style="width:80%;height:160px;" class="easyui-textbox" name="accessNums" id="accessNumsMark" prompt="系统支持输入,请使用;分割，或者一行一条" data-options="multiline:true,required:true"></input></div>
        <div class="formDiv"><label>备注内容:</label><input style="width:80%;height:160px;" class="easyui-textbox" id="mark" name="mark" data-options="multiline:true,validType:'length[0,500]'"></input></div>
        <div class="forSubmint">
             <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumMark()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canMark()">取消</a>
        </div>
    </form>
</div>        
<!--//新增弹出框-->
<div id="addBox" >
    <form id="addForm" method="post">
        <div class="formDiv"><label>卡号:</label><input style="width:80%;height:160px;" class="easyui-textbox"     id="accessNums" name="accessNums" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>短信内容:</label><input style="width:80%;height:160px;" class="easyui-textbox" id="message" name="message" data-options="multiline:true,validType:'length[5,30]'"></input></div>
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sum()">发送短信</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="openMesage()">查看短信记录</a>
        </div>
        
       
        
        <div class="formDiv" hidden="true">
	        <select id="comboType" style="display：none" class="easyui-combobox"></select>
	        <select id="comboName" style="display：none" class="easyui-combobox"></select>
	        <select id="operator"  style="display：none" class="easyui-combobox"></select>
	        
            <select id="workingConditionYd"  style="display：none" class="easyui-combobox"></select>
            <select id="workingConditionLt"  style="display：none" class="easyui-combobox"></select>
            <select id="workingConditionDx"  style="display：none" class="easyui-combobox"></select>
            
            <select id="billingStatus"  style="display：none" class="easyui-combobox"></select>
            
            <select  name="silentType" id="silentType"  style="display：none;width:410px" prompt="请选择"  class="easyui-combobox"></select>
            <select  name="haveTest" id="haveTest"  style="display：none;width:410px" prompt="请选择"  class="easyui-combobox"></select>
        </div>
        
    </form>
    
    <form id="openOwnerForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/groupChoose.jsp" method="post" target="newOwnerWin"></form>  
    
    <form id="openMesageForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/message.jsp" method="post" target="newMessageWin"></form>  
    <form id="openPackagesForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/payPackagesChoose.jsp" method="post" target="newPackagesWin"></form>
</div>
<script src=iccidManager.js></script>
</body>
</html>