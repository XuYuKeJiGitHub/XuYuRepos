<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../base.jsp"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>机构选择(双击也可选择)</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css">
</head>
<body style="width:100%;height:100%;">
<div class="easyui-layout" data-options="fit:true" id="useBox">
    <!--左侧树-->
    <div data-options="region:'center',title:'机构数据'">
        <div id="tabelBut">
            <div>
                <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-ok" onclick="obj.submit()">确定</a>
                <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-cancel"   onclick="obj.cancle()">取消</a>
            </div>
        </div>
        <ul id="tt">
		</ul>
    </div>
</div>
<script type="text/javascript">
var bathPath="<%=basePath%>";
var openOrgId="<%=request.getParameter("openOrgId")%>";
</script>
<script src="orgChoose.js"></script>


</body>
</html>