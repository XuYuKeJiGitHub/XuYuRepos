<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/inc/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>XXXXXXXX管理系统-用户管理</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/themes/icon.css">
	
	<script type="text/javascript" src="<%=basePath%>/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery.easyui-1.5.5.4.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery.easyui-1.5.5.4.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/easyui-1.5.5.4-lang-zh_CN.js"></script>
</head>
<body>
	<table id="dg" title="用户管理信息列表" class="easyui-datagrid" style="width:98%;height:600px" data-options="
				rownumbers:true,
				checkOnSelect:true,
				fit: true,
				singleSelect:true,
				autoRowHeight:false,
				url:'user.do?mtd=list',
				pagination:true,
				pageList:[10,20,30],
				pageSize:10
				">
		<thead>
			<tr>
				<th data-options="field:'userId',checkbox:true" width="60">用户ID</th>
				<th data-options="field:'account'" width="80">用户账号</th>
				<th data-options="field:'userName'" width="180">姓名</th>
				<th data-options="field:'phone'" width="180">电话</th>
				<th data-options="field:'sexName'" width="180">性别</th>
				<th data-options="field:'address1'" width="180">地址1</th>
				<th data-options="field:'address2'" width="180">地址2</th>
				<th data-options="field:'typeName'" width="180">用户类型</th>
				<th data-options="field:'isValid'" width="180">是否有效</th>
			</tr>
		</thead>
	</table>
	<div style="display: none;">
		<div id="dialog-div" class="easyui-dialog" title="新增用户" style="width:600px;height:400px;"
	        data-options="iconCls:'icon-save',title:'新增用户',resizable:true,modal:true,closed:true">
	        <form id="ff" method="post">
	        <input type="hidden" name='userId' id="userId">
	            <table>
	                <tr>
	                    <td>用户账号:</td>
	                    <td>
	                    	<input type="text" name="account" id="account" style="width: 300px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td>姓名:</td>
	                    <td>
	                    	<input  type="text" name="userName"  id="userName" style="width: 300px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td>电话:</td>
	                    <td>
	                    	<input  type="text" name="phone"  id="phone" style="width: 300px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td>性别:</td>
	                    <td>
	                    	<select name="sex" id="sex" style="width: 300px;">
	                    		<option value="M">M-男</option>
	                    		<option value="F">F-女</option>
	                    	</select>
	                    </td>
	                </tr>
	                <tr>
	                    <td>地址1:</td>
	                    <td>
	                    	<input  type="text" name="address1"  id="address1" style="width: 300px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td>地址2:</td>
	                    <td>
	                    	<input  type="text" name="address2"  id="address2" style="width: 300px;"></input>
	                    </td>
	                </tr>
	                <tr>
	                    <td>用户类型:</td>
	                    <td>
	                    	<select name="userType" id="userType" style="width: 300px;">
	                    		<option value="M">M-后台管理账号</option>
	                    		<option value="F">F-前台会员账号</option>
	                    	</select>
	                    </td>
	                </tr>
	            </table>
	        </form>
	        <div style="text-align:center;padding:5px">
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">保存</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()" id="clearBtn">重置</a>
	        </div>
	        <div style="text-align:center;padding:5px;color: red;" id="msg">
	        </div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			var pager = $('#dg').datagrid('getPager');	// get the pager of datagrid
			pager.pagination({
				buttons:[{
					iconCls:'icon-add',
					text:'新增',
					handler:function(){
						$("#dialog-div").dialog({
							onOpen:function(){
								$('#ff').form('clear');
							},
							onClose: function () {
								$('#ff').form('clear');
								$("#dg").datagrid('load');
								//$(this).dialog('destroy');
							}
						}
						);
						$("#msg").text("");
						$('#dialog-div').dialog('open');
					}
				},{
					iconCls:'icon-edit',
					text:'编辑',
					handler:function(){
						var row =$("#dg").datagrid('getSelected');
						if(row){
							$("#dialog-div").dialog({
								onOpen:function(){
									$("#tid").val(row.typeId);
									$("#mtd").val("update");
									$("#typeName").val(row.typeName);
									$("#typeSort").val(row.typeSort);
									$("#isIndexPage").val(row.isIndexPage);
								},
								onClose: function () {
									$('#ff').form('clear');
									$("#dg").datagrid('load');
									//$(this).dialog('destroy');
								}
							  }
							);
							$("#msg").text("");
							$('#dialog-div').dialog('open');
						}else{
							alertMsg();
						}
						
					}
				},{
					iconCls:'icon-remove',
					text:'删除',
					handler:function(){
						var row =$("#dg").datagrid('getSelected');
						if(row){
							$.post("productType.do?mtd=del&id="+row.typeId,function(){
							 	$('#dg').datagrid('load'); 
							 });
						}else{
							alertMsg();
						}
						
					}
				}]
			});
			
			$("#searchBtn").click(function(){
				 $("#dg").datagrid('load',{
					 "particArea":$("#particArea").val(),
					 "storeName":$("#storeName").val(),
					 "storeAddr":$("#storeAddr").val()
			     });
			});
			
		});
		
		function alertMsg(){
			$.messager.show({
                title:'提示',
                msg:'请选选取记录后再操作',
                showType:'fade',
                style:{
                    right:'',
                    bottom:''
                }
            });
		}
		
	   function submitForm(){
		   $('#ff').form('submit', {
			    url:'user.do?mtd=saveUS',
			    onSubmit: function(){
			        // do some check
			        // return false to prevent submit;
			    },
			    success:function(data){
			        $("#msg").text(data);
			    }
			});
        }
	   
       function clearForm(){
           $('#ff').form('clear');
       }
	</script>
</body>
</html>