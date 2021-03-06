<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>卡信息管理</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
    
</head>
<body  style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
<div data-options="region:'center',split:true">
        <!--表格查询-->
        <form id="queryForm" method="post">
        <div class="tableFind" id="tableFind" data-options="region:'north',border:false" style="height:95px;">
            <p>
                <label>关键字:</label>
                                     <input  name="keywords" id="keywordsQuery" prompt="接入号、IMSI、ICCID" type="text" class="easyui-textbox"/>
                <label>客户名称:</label><select  name="agency" id="agencyQuery" prompt="请选择" class="easyui-combotree"></select>    
	            <label>运营商:</label>
	                                 <select  name="operator" id="operatorQuery"      prompt="请选择" class="easyui-combobox"></select>
	           
            </p>
            <p class="findButAlign">
                 <a id="a2" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
			     <a id="a3" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
			     <a id="a1" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-collapse'" onclick="obj.collapse()">&nbsp;</a>
            </p>
            
            <div id="queryMore">
	            <p>
	                <label>二级运营商:</label>
	                                 <select  name="ownerPlace" id="ownerPlaceQuery"      prompt="请选择" class="easyui-combobox"></select>
		            <label>套餐类型:</label>
		                                    <select  name="comboType" id="comboTypeQuery" prompt="请选择" class="easyui-combobox"></select>
			        <label>套餐名称:</label>
			                                <select  name="comboName" id="comboNameQuery" prompt="请选择" class="easyui-combobox"></select>
	            </p>
	            <p>
	                <label>实际套餐名称:</label>
		                                 <select  name="realComboType" id="realComboTypeQuery" prompt="请选择" class="easyui-combobox"></select>
		            <label>卡状态:</label>
		                                 <select  name="workingCondition" id="workingConditionQuery"  prompt="请选择" class="easyui-combobox"></select>
		            <label>在线状态:</label>
                                         <select id="billingStatusQuery" prompt="请选择" class="easyui-combobox"></select>
	            </p>
	            <p>
		            <label>开户日期:</label>
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realEstablishStartQuery">
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realEstablishEndQuery">
		            <label>激活日期:</label>
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realActivateStartQuery">
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realActivateEndQuery">
		            <label>到期日期:</label>
							               <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realWaitDateStartQuery">
								           <input style="width:10%;" class="easyui-datebox" data-options="sharedCalendar:'#cc'" id="realWaitDateEndQuery">
	            </p>
	             <p>
		            <label>接入号:</label>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="accessNumEndQuery"/>
		            <label>ICCID:</label>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="iccidEndQuery"/>
		            <label>IMSI:</label> 
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="imsiStartQuery"/>
		                                 <input style="width:10%;" type="text" class="easyui-textbox" id="imsiEndQuery"/>
		           
		            
	            </p>
	            <p>
	                            <label>卡号备注:</label>
				                                   <input type="text" class="easyui-textbox" id="markQuery"/>
				                <label>支付类型:</label>
				                <select name="paymentType" id="paymentTypeQuery" prompt="请选择" class="easyui-combobox"></select>
				                <label>余额;</label>
	                            <select  id="remainGpsType" class="easyui-combobox" prompt="请选择" style="width:10%;">
			                            <option value=">">大于</oprtion>
			                            <option value="=">等于</oprtion>
			                            <option value="<">小于</oprtion>
	                            </select>
	                            <input style="width:10%;" type="text" class="easyui-textbox" id="remainGps"/>
	            </p>
	            <p>
	                            <label>是否群组:</label>
	                            <select  id="isowner"  prompt="请选择" class="easyui-combobox"></select>
					            <a id="a5" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
					            <a id="a6" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.queryRes()">重置</a>
					            <a id="a4" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-expand'" onclick="obj.expand()">&nbsp;</a>
	            </p>
	            <div  class="formDiv" hidden="true">
			            <select id="workingConditionYd"  style="display：none" class="easyui-combobox"></select>
			            <select id="workingConditionLt"  style="display：none" class="easyui-combobox"></select>
			            <select id="workingConditionDx"  style="display：none" class="easyui-combobox"></select>
			            
			            <select id="ownerPlaceYd"  style="display：none" class="easyui-combobox"></select>
			            <select id="ownerPlaceLt"  style="display：none" class="easyui-combobox"></select>
			            <select id="ownerPlaceDx"  style="display：none" class="easyui-combobox"></select>
	            </div>
            </div>
        </div>
        </form>
        <div id="cc" class="easyui-calendar"></div>
        <!--表格列表-->
        <div class="tableCon" >
            <table id="table" class="tableStyle" data-options="region:'center'"></table>
            <div id="tabelBut">
                    <div>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-edit'"  onclick="obj.editAll()">批量修改</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-remove'" onclick="obj.del()">删除</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-more'" onclick="obj.detail()">详情</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-redo'" onclick="obj.export()">导出</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-back'" onclick="obj.addBox()">发送短信</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-sum'" onclick="obj.recharge()">充值</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-tip'" onclick="obj.setOwner()">划卡</a>
	                    <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-filter'" onclick="obj.submitType()">数据批量导入</a>
	                     <a href="#" class="easyui-linkbutton" plain="true"  data-options="iconCls:'icon-lock'" onclick="obj.addMark()">备注</a>
    				</div>
                </div>
            </div>
 </div>
</div>  

<!--导入前选择运营商-->
<div id="importTypeBox">
      <form id="importTypeForm" method="post">
	     <div class="formDiv"><label>运营商:</label><select  name="operator" id="operator"  style="width:465px" prompt="请选择" class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
	     <div class="formDiv" id="ownerPlaceDiv"><label>二级运营商:</label><select  name="ownerPlace" id="ownerPlace"  style="width:465px" prompt="请选择" class="easyui-combobox"></select><span class="formSpan">*</span></div>
	     <div class="formDiv"><label>套餐:</label><select  name="comboType" id="comboType"  style="width:465px" prompt="请选择" class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
	     <div class="formDiv"><label>沉默期类型:</label><select  name="waitType" id="waitType"  style="width:465px" prompt="请选择" class="easyui-combobox"></select></div>
	     <div class="formDiv"><label>测试期类型:</label><select  name="testType" id="testType"  style="width:465px" prompt="请选择" class="easyui-combobox"></select></div>
	     <div class="formDiv"><label>卡种:</label><select  name="cardType" id="cardType"  style="width:465px" prompt="请选择" class="easyui-combobox"></select></div>
	     <div class="formDiv"><label>规格:</label><select  name="standard" id="standard"  style="width:465px" prompt="请选择" class="easyui-combobox"></select></div>
	     <div class="formDiv"><label>成本单价:</label><input type="text" name="unitCost" id="unitCost" style="width:465px" data-options="required:true"/><span class="formSpan">*</span></div>
	     <div class="forSubmint">
	            <a href="#" class="easyui-linkbutton" plain="true"  iconCls="icon-ok"  onclick="obj.import1()">确定</a>
	      </div>
      </form>
</div>    	
<!--//导入弹出框-->
<div id="importBox" >
  			<form id="importForm"  action="<%=basePath%>/csvoprate/upload" enctype="multipart/form-data" method="post">
		        	<div class="formDiv">
			            <tr>
			                <td><input type="file" name="name" style="width: 80%;" id="uploadFile" class="easyui-validatebox" data-options="required:true"></td><span class="formSpan">*</span>
			                 <a href="#" class="easyui-linkbutton" style="width: 30%;" iconCls="icon-ok"  onclick="obj.loadModel()">模板下载</a>    
			            </tr>
			        </div>
			        <div class="forSubmint">
			            <tr><td><a style="width: 30%;left:10%;"   class="easyui-linkbutton" id="upFile" onclick="obj.uploadFile()" iconCls="icon-redo" >确认导入</a></td></tr>
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
        
        <div  class="formDiv" hidden="true">
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
<!--充值弹出框 -->
<div id="rechargeBox" >
    <form id="addRechargeForm" method="post">
        <div class="formDiv"><label>接入号:</label><input style="width:410px;height:160px;"   class="easyui-textbox" name="accessNums" id="accessNumsRecharge" prompt="系统支持输入,请使用;分割，或者一行一条" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>使用区间:</label><select  name="yesNo" id="yesNoRecharge"  style="width:410px" prompt="请选择" class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="numTypeRecharge"><label>号码类型:</label> <select   name="numType" id="numTypeRechargeId"  style="width:410px" prompt="请选择" class="easyui-combobox" ></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="accessNumStartRecharge" ><label>开始号码;</label><input type="text" style="width:410px" id="accessNumStartRechargeId" name="accessNumStart" class="easyui-validatebox"/><span class="formSpan">*</span></div>
		<div class="formDiv" id="accessNumEndRecharge"><label>结束号码;</label><input type="text"  style="width:410px" id="accessNumEndRechargeId" name="accessNumEnd"   class="easyui-validatebox"/><span class="formSpan">*</span></div>
        <div class="formDiv"><label>充值次数:</label><input type="text" name="chargeCost" id="chargeCost" style="width:410px" class="easyui-numberbox" data-options="required:true,min:1,precision:0"/><span class="formSpan">*</span></div>
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumRecharge()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canRecharge()">取消</a>
        </div>
    </form>
</div>
<!-- 划卡弹出框 -->
<div id="addOwnerBox" >
    <form id="addOwnerForm" method="post">
        <div class="formDiv"><label>接入号:</label><input style="width:410px;height:160px;" class="easyui-textbox" name="accessNums" id="accessNumsOwner" data-options="multiline:true"></input></div>
        <div class="formDiv"><label>使用区间:</label><select  name="yesNo" id="yesNoOwner"  style="width:410px" prompt="请选择" class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="numTypeOwner"><label>号码类型:</label> <select   name="numType" id="numTypeOwnerId"  style="width:410px" prompt="请选择" class="easyui-combobox" ></select><span class="formSpan">*</span></div>
        <div class="formDiv"><label>使用群组:</label><select  name="groupYesNo" id="yesNoGroup"  style="width:410px" prompt="请选择" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="accessNumStartOwner" ><label>开始号码;</label><input type="text" style="width:410px" id="accessNumStartOwnerId" name="accessNumStart" class="easyui-validatebox"/><span class="formSpan">*</span></div>
		<div class="formDiv" id="accessNumEndOwner"><label>结束号码;</label><input type="text"  style="width:410px" id="accessNumEndOwnerId" name="accessNumEnd"   class="easyui-validatebox"/><span class="formSpan">*</span></div>
		
		<div class="formDiv" id="comboTypeDiv"><label>套餐类型:</label><select  name="comboType" id="comboTypeOwner"  style="width:410px" prompt="请选择" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="comboNameDiv"><label>套餐:</label><select  name="comboName" id="comboName"  style="width:410px" prompt="请选择" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="payTypeDiv"><label>支付类型:</label><select  name="payType" id="payType"  style="width:410px" prompt="请选择" class="easyui-combobox"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="silentTypeDiv"><label>沉默期类型:</label><select  name="silentType" id="silentType"  style="width:410px" prompt="请选择" class="easyui-combobox"></select></div>
        <div class="formDiv" id="haveTestDiv"><label>测试期:</label><select  name="haveTest" id="haveTest"  style="width:410px" prompt="请选择" class="easyui-combobox"></select></div>
        
        <div class="formDiv" id="ownerNameDiv"><label>群组:</label><input type="text"   name="ownerName" id="ownerName" class="easyui-validatebox"><span class="formSpan">*</span><a href="#" class="easyui-linkbutton" onclick="openOwner()">选择</a></div>
        <div class="formDiv" hidden="true">
	        <input type="text" hidden="true" id="ownerId"  name="owner">
        </div>
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumOwner()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canOwner()">取消</a>
        </div>
    </form>
    
    <form id="openOwnerForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/groupChoose.jsp" method="post" target="newOwnerWin"></form>  
    <form id="openMesageForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/message.jsp" method="post" target="newMessageWin"></form>  
    <form id="openPackagesForm" name=form action="<%=request.getContextPath()%>/XuYuRepos/manager/payPackagesChoose.jsp" method="post" target="newPackagesWin"></form>
</div>

<!--套餐选择框 -->
<div id="packagesBox" >
    <form id="packagesForm" method="post">
        <div class="formDiv"><label>是否预充值:</label><select  name="yc" id="yesNoPackages"  style="width:410px" prompt="请选择" class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
        <div class="formDiv" id="ownerNameDiv"><label>套餐:</label><input type="text"   name="payName" id="payName" style="width:410px"  editable="false" class="easyui-validatebox" data-options="required:true" ><span class="formSpan">*</span><a href="#" class="easyui-linkbutton" onclick="openPackages()">选择</a></div>
        <div class="formDiv" hidden="true">
	        <input type="text" hidden="true" id="payId"  name="payId">
        </div>
        <div class="forSubmint">
            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.sumPackages()">确定</a>
            <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel"  onclick="obj.canPackages()">取消</a>
        </div>
    </form>
</div>

<!--批量更新-->
<div id="batchBox" >
  			<form id="batchForm"  action="<%=basePath%>/csvoprate/upload" enctype="multipart/form-data" method="post">
		        	<div class="formDiv">
			            <tr>
			                <td><input type="file" name="name" style="width: 80%;" id="batchFile" class="easyui-validatebox" data-options="required:true"></td><span class="formSpan">*</span>
			            </tr>
			        </div>
			        <div class="formDiv">
			            <tr>
			                <td><span class="formSpan">请使用系统导出的文件导入,ID千万不能修改.</span></td>
			            </tr>
			        </div>
			        <div class="forSubmint">
			            <tr><td><a editable="false"  style="width: 30%;left:10%;" class="easyui-linkbutton" onclick="obj.batchFile()" iconCls="icon-redo" >确认导入</a></td></tr>
		        	</div>
		    </form>
</div>
<!-- 详情框 -->
<div id="detailBox" >
		    <form id="detailForm" method="post">
		    
		            <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">接入号:</label><input  name="accessNum" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">ICCID:</label><input  name="iccid" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">IMSI:</label><input  name="imsi" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">支付类型:</label><input  name="paymentType" id="paymentTypeDetail" style="width: 220px;" prompt="请选择" class="easyui-combobox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">运营商:</label><input  name="provider" id="providerDetail" style="width: 220px;"  prompt="请选择" class="easyui-combobox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">二级运营商:</label><input  name="ownerPlace"  id="ownerPlaceDetail" style="width: 220px;" prompt="请选择" class="easyui-combobox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">客户名称:</label><input  name="orgName" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">卡状态:</label><input  name="workingCondition" id="workingConditionDetail" style="width: 220px;" prompt="请选择" class="easyui-combobox"/></td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td> <label style="margin-left: 10px;width: 100px;">套餐类型:</label><input  name="comboType" id="comboTypeDetail" style="width: 220px;"  prompt="请选择" class="easyui-combobox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">套餐:</label><input  name="comnoName" id="comnoNameDetail" style="width: 220px;" prompt="请选择" class="easyui-combobox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">当月用量:</label><input  name="useGprs" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">套餐总量:</label><input  name="totalGprs" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">余额:</label><input  name="remainGps" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">已发短信:</label><input  name="messageCount" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">沉默期类型:</label><input  name="waitType" id="waitTypeDetail" style="width: 220px;"  prompt="请选择" class="easyui-combobox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">测试期类型:</label><input  name="testType" id="testTypeDetail" style="width: 220px;" prompt="请选择" class="easyui-combobox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">到期日期:</label><input  name="deadlineDate" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td> <label style="margin-left: 10px;width: 100px;">开户日期:</label><input  name="establishDate" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">激活日期:</label><input  name="activateDate" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">沉默期到期时间:</label><input  name="waitDate" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">沉默期类型(实际):</label><input  name="realWaitType" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">测试期类型(实际):</label><input  name="realTestType" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">开户日期 (实际):</label><input  name="realEstablish" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td> <label style="margin-left: 10px;width: 100px;">激活日期 (实际):</label><input  name="realActivate" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">沉默期到期时间(实际):</label><input  name="realWaitDate" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">在线状态:</label><input  name="billingStatus" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">实际套餐    :</label><input  name="realCombotype" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">成本单价:</label><input  name="unitCost" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">当月费用   :</label><input  name="email14" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">理想化成本:</label><input  name="a1" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">营收   :</label><input  name="a2" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">上月消费额度:</label><input  name="email15" style="width: 220px;" type="text" class="easyui-textbox"/> </td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">剩余额度   :</label><input  name="email16" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			                <td><label style="margin-left: 10px;width: 100px;">实际利润:</label><input  name="email17" style="width: 220px;" type="text" class="easyui-textbox"/></td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">理想化利润 :</label><input  name="email18" style="width: 220px;"  type="text" class="easyui-textbox"/></td>
			            </tr>
			        </div>
			        
			        <div class="formDiv">
			            <tr>
			                <td><label style="margin-left: 10px;width: 100px;">备注内容:</label><input style="width:80%;height:80px;" class="easyui-textbox" name="mark" data-options="multiline:true"></input></div></td>
			            </tr>
			        </div>
		    </form>
</div>
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/src/jquery.form.js"></script>
 <script src=cardManager.js></script>
</body>
</html>