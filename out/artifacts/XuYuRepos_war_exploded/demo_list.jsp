<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/inc/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>XXXXXXXXX管理系统</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/themes/icon.css">
	
	<script type="text/javascript" src="<%=basePath%>/js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery.easyui-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/js/easyui-1.4.4-lang-zh_CN.js"></script>
</head>
<body>
	<table id="dg" title="房源信息列表">
	</table>
	
	<div id="search_content" style="padding:10px;width:100%" >
		<span>
			房源类型:
		</span>
		<select  name="proTypeId" id="proTypeId" style="width: 200px;">
			<option value="">--请选取--</option>
	        <c:forEach items="${allTypeList}" var="tl">
				<option value="${tl.proTypeId}">${tl.proTypeId}_${tl.proTypeName}</option>
			</c:forEach>
		</select>
		<span>
			区域:
		</span>
		<select name="proAreaId" id="proAreaId" style="width: 200px;">
			<option value="">--请选取--</option>
			<c:forEach items="${allAreaList}" var="ra" varStatus="idx">
				<option value="${ra.proAreaId}">${ra.proAreaId}_${ra.proAreaName}</option>
			</c:forEach>
		</select>
		<span>
			类别:
		</span>
		<select name="proCategory" id="proCategory" style="width: 200px;">
			<option value="">--请选取--</option>
			<option value="GY">GY_公寓</option>
			<option value="BS">BS_别墅</option>
			<option value="GC">GC_高层</option>
		</select>
		<span>
			类别:
		</span>
		房源名称
		<input type="text" name="proName" id="proName"></input>
	  	<a class="easyui-linkbutton" data-options="iconCls:'icon-search'" id="searchBtn">查询</a> 
	  	<!-- 
	  	<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="publishBtn">发布项目</a> 
	  	 -->
	</div>
	<div id="test-div"></div>
	<script type="text/javascript">
		$(function(){
			$("#dg").datagrid({
				url:'product.do?mtd=list',
				columns:[[
				    {field:'productName',title:'房源名称',width:'15%',
						formatter: function(value,row,index){
							if (row.productName){
								return "<a style='text-decoration:none;color:#337ab7' href='javascript:void(0);' onclick=openProject('" + row.productName + "'," + row.id + ")>" + row.productName + "</a>";
							} else {
								return "<a href='javascript:void(0);'>"+value+"</a>";
							}
						}
				    },
				    {field:'price',title:'价格',align:'left',width:'4%'},
				    {field:'address',title:'地址',width:'15%'},
				    {field:'floor',title:'楼层',width:'4%'},
				    {field:'use',title:'用途',width:'4%'},
				    {field:'village',title:'小区',width:'8%'},
				    {field:'typeName',title:'房源类型',width:'10%'},
				    {field:'areaName',title:'区域',width:'6%'},
				    {field:'category',title:'类别',width:'6%'},
				    {field:'area',title:'面积',width:'4%'},
				    {field:'createDate',title:'上架时间',width:'6%'}
				]],
				rownumbers:true,
				checkOnSelect:true,
				fit: true,
				fitColumns: true,
				singleSelect:true,
			 	pagination: true,
				autoRowHeight:false,
				toolbar:'#search_content',
			 	pageList:[20],
			 	pageSize:20,
			 	queryParams: {
			 		proTypeId: $("#proTypeId").val(),
			 		proAreaId: $("#proAreaId").val(),
			 		proCategory: $("#proCategory").val(),
			 		proName: $("#proName").val()
				}
            });
			
			var pager = $('#dg').datagrid('getPager');	// get the pager of datagrid
			pager.pagination({
				buttons:[{
					iconCls:'icon-edit',
					text:'编辑',
					handler:function(){
						var row = $('#dg').datagrid('getSelected');
						if(row){
							openProject(row.productName,row.id);
						}else{
							alertMsg();
						}
					}
				},{
					iconCls:'icon-remove',
					text:'删除',
					handler:function(){
						var row = $('#dg').datagrid('getSelected');
						if(row){
							$.post("../mg/product.do?mtd=delPd&id="+row.id,function(){
							 	$('#dg').datagrid('reload'); 
							 });
						}else{
							alertMsg();
						}
					}
				}]
			});
			
			$("#searchBtn").click(function(){
				 $("#dg").datagrid('loadData',[]);
				 
				 $("#dg").datagrid('load',{
					 	proTypeId: $("#proTypeId").val(),
				 		proAreaId: $("#proAreaId").val(),
				 		proCategory: $("#proCategory").val(),
				 		proName: $("#proName").val()
			     });
			});
			
			$("#publishBtn").click(function(){
				parent.openTabs("发布项目","product.do?mtd=e");
				//var _tabs=$('#tabs', window.parent.document);
				/*
				if ($('#tabs', window.parent.document).tabs('exists', "发布项目")){    
					$('#tabs', window.parent.document).tabs('select', "发布项目");    
			    } else {    
			        var content = '<iframe scrolling="auto" frameborder="0"  src="publishList.do?mtd=entryPublish" style="width:100%;height:100%;"></iframe>';    
			        $('#tabs', window.parent.document).tabs('add',{    
			            title:title,    
			            content:content,    
			            closable:true    
			        });    
			    }  */
				
				//window.location.href = 'publishList.do?mtd=entryPublish';
				/*
				$('#test-div').dialog({
				    title: '发布项目',
				    width: 650,
				    height: 720,
				    closed: false,
				    cache: false,
				    resizable:true,
				    href: 'publishList.do?mtd=entryPublish',
				    modal: true
				});*/
			});
		});
		
		function openProject(p_name,p_id){
			parent.openTabs(p_name,"product.do?mtd=entryEdit&id="+p_id);
			
		}
		
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
	</script>
</body>
</html>