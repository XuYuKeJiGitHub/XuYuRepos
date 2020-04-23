<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>数据同步</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <div data-options="region:'center',split:true">
        <!--表格查询-->
        <div class="tableFind"  data-options="region:'north',border:false" style="height:65px;">
             <p>
	            <label>同步时间:</label><input style="width:8%;" type="text"/>
	            <a id="btn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-search'" onclick="obj.find()">查询</a>
	            <a id="zhbtn" href="javascript:" class="easyui-linkbutton tableFindBut" data-options="iconCls:'icon-redo'" onclick="obj.find()">重置</a>
	            
            </p>
        </div>
        <!--表格列表-->
        <div class="tableCon">
            <table id="table" data-options="region:'center'" class="tableStyle"></table>
            <div id="tabelBut">
                <div>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.submitType()">导入数据</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.openFileUpload()">更新状态</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.cleanLT">联通跑批清零</a>
                    <a href="#" class="easyui-linkbutton" plain="true"  onclick="obj.cleanYDandDX">移动电信跑批清零</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--导入前选择运营商-->
<div id="importTypeBox">
      <form id="importTypeForm" method="post">
	     <div class="formDiv"><label>运营商:</label><select  name="operator" id="operator"  style="width:335px" prompt="请选择"  class="easyui-combobox" data-options="required:true"></select><span class="formSpan">*</span></div>
	     <div class="forSubmint">
	            <a href="#" class="easyui-linkbutton"  iconCls="icon-ok"  onclick="obj.import1()">确定</a>
	      </div>
      </form>
</div> 
<!--//导入弹出框-->
<div id="importBox" >
  			<form id="importForm"  action="<%=request.getContextPath()%>/systemannexes/upload" enctype="multipart/form-data" method="post">
		        	<div class="formDiv">
			            <tr>
			                <td><input type="file" name="name" style="width: 60%;" id="uploadFile" class="easyui-validatebox" data-options="required:true"></td><span class="formSpan">*</span>
			                 <a href="#" class="easyui-linkbutton" style="width: 30%;" iconCls="icon-ok"  onclick="obj.loadModel()">模板下载</a>    
			            </tr>
			        </div>
			        <div class="forSubmint">
			            <tr><td><a style="width: 30%;left:10%;" class="easyui-linkbutton" id="upFile" onclick="obj.uploadFile()" iconCls="icon-redo" >确认导入</a></td></tr>
		        	</div>
		    </form>
</div>
 <!--更新状态弹出框-->
<div id="importBox2" >
    <form id="importStateForm"  action="<%=request.getContextPath()%>/systemannexes/uploadState" enctype="multipart/form-data" method="post">
        <div class="formDiv">
            <tr>
                <td><input type="file" name="name" style="width: 60%;" id="uploadStateFile" class="easyui-validatebox" data-options="required:true"></td><span class="formSpan">*</span>
            </tr>
        </div>
        <div class="forSubmint">
            <tr><td><a style="width: 30%;left:10%;" class="easyui-linkbutton" id="upStateFile" onclick="obj.uploadStateFile()" iconCls="icon-redo" >确认导入</a></td></tr>
        </div>
    </form>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/src/jquery.form.js"></script>
<script src=importdata.js></script>


</body>
</html>