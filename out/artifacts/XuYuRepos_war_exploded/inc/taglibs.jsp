<%@page import="com.xuyurepos.entity.system.SystemUser"%>
<%
	String serverName="http://"  +  request.getHeader("Host");  
    request.setAttribute("serverName", serverName);
    String path = request.getContextPath(); 
    String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path;
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/default/easyui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
    var basePath="<%=basePath%>";
    
    <%
    SystemUser systemUser=(SystemUser)request.getSession().getAttribute("systemUser");
    if(null==systemUser){
    	%>
    	   top.location.href=basePath+"/login.jsp";
    	<%
    }else{
    	%>
    	    
    	<%
    }
    %>
</script>
