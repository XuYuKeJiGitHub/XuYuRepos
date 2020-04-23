<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/inc/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>XXXXXXXXXXX</title>
	<link rel="stylesheet" type="text/css" href="../css/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="../css/themes/icon.css">
	
	<script type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="../js/jquery.easyui-1.5.5.4.min.js"></script>
	
	<style type="text/css">
		.easyui-accordion ul{
			list-style: none;
			padding: 0;
			margin: 0;
		}
		
		.easyui-accordion ul li{
			margin-top: 8px;
		}
		
		.easyui-accordion ul li a{
		 	display:inline-block;
			padding-left: 0px;
			color: #555555; 
			text-decoration:none;
			line-height: 18px;
		}
	</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:50px;background:#398EBC;padding-top:2px;padding-left: 10px;">
		<div id="log" style="float:left;width:280px;height: 65px;">
			<a href="http://www.haiaiju.com/" title="去首页看看"><img src="../images/qrCodeIcon_lj.png" width="50px;" height="30px;" style="margin:16px;"></a>
			<span style="height: 50px;line-height: 50px;font-size: 20px;font-weight: 300;color: #FFFFFF;vertical-align:24px;">XXXXXXXXXX</span>
		</div>
		<div style="float: right;width: 280px;color:#fff; padding:20px; text-align:right;font-size:12px;">
			当前用户： ${sessionScope.loginUser.account}(${sessionScope.loginUser.name})
			<a href="javascript:void(0);" onclick="loginOff();" style="color:#fff;text-decoration:none;">退出</a>
		</div>
	</div>
	<div data-options="region:'west',split:true,title:'导航菜单'" style="width:180px;padding:10px;">
		<div class="easyui-accordion" data-options="fit:true,border:false,onSelect:getHelp" id='menu'>
			<c:forEach items="${sessionScope.loginUser.leftMenuList }" var="menu">
				<div title="${menu.name}" style="padding:10px;">
					<ul>
						<c:forEach items="${menu.menu2thList}" var="menu2">
							<li>
								<a href="javascript:void(0)" data-href="${menu2.url}">
									<img src="../icon/${menu2.icon}" style="vertical-align: text-bottom;"> ${menu2.name}
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		</div>
	</div>
	<div data-options="region:'center',title:'',iconCls:'icon-ok'">
		<div class="easyui-tabs" data-options="fit:true,border:false,plain:true,pill:true" id="tabs">
			<div title="关于" data-options="href:'about.do'" style="padding:10px"></div>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$("#menu ul a").on("click",function(){
				var url = $(this).attr("data-href");
				var title = $.trim($(this).text());
				openTabs(title,url);
			});
			
			
		});
		
		function getHelp(){
			var v=$("#menu").accordion('getSelected');
			if(v){
				var panelIndex = $('#menu').accordion('getPanelIndex', v);
				var url = '';
				if(panelIndex==0){
					url = "help_center.do";
				} else if(panelIndex==1){
					url = "help_manage.do";
				} else if(panelIndex==2){
					url = "help_option_data.do";
				} else if(panelIndex==3){
					url = "help_tool.do";
				} else if(panelIndex==4){
					url = "help_system.do";
				}
				//$(".easyui-layout").layout('panel','east').panel('setTitle','我的新标题')
				$('.easyui-layout').layout('panel','east').panel("refresh",url);
				
			}
		}
		
		function openTabs(p_title,p_url){
			if ($('#tabs').tabs('exists', p_title)){    
		        $('#tabs').tabs('select', p_title);    
		    } else {    
		        var content = '<iframe scrolling="auto" frameborder="0"  src="'+p_url+'" style="width:100%;height:100%;"></iframe>';    
		        $('#tabs').tabs('add',{    
		            title:p_title,    
		            content:content,    
		            closable:true    
		        });    
		    } 
		}
		
		function closeTabs(p_title){
			if ($('#tabs').tabs('exists', p_title)){    
		        $('#tabs').tabs('close', p_title);    
		    } 
		}
		
		function loginOff(){
			window.location.href="userLogin.do?mtd=loginOff";
		}
	</script>
</body>
</html>