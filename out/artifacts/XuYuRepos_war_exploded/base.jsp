<%@page import="com.xuyurepos.entity.system.SystemUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String basePath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/gray/easyui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/ux/validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clipboard.min.js"></script>
<script type="text/javascript">
    var basePath="<%=request.getContextPath()%>";
    
    <%
    SystemUser systemUser=(SystemUser)request.getSession().getAttribute("systemUser");
    if(null==systemUser){
    	%>
    	 window.top.location.href ="<%=request.getContextPath()%>/login.jsp";  
    	<%
    }else{
    	String cname=systemUser.getCname();
    	String userName=systemUser.getUserName();
    	String orgId=systemUser.getOrgId();
    	String orgName=systemUser.getOrgName();
    	String deptId=systemUser.getDeptId();
    	String deptName=systemUser.getDeptName();
    	String orgLevel=systemUser.getOrgLevel();
    	String orgCode=systemUser.getOrgCode();
    	out.print("var cname = '"+cname+"';");
	    out.print("var userName = '"+userName+"';");
	    out.print("var orgId = '"+orgId+"';");
	    out.print("var orgName = '"+orgName+"';");
	    out.print("var deptId = '"+deptId+"';");
	    out.print("var deptName = '"+deptName+"';");
	    out.print("var orgLevel = '"+orgLevel+"';");
	    out.print("var orgCode = '"+orgCode+"';");
    	%>
    	<%
    }
    %>
    // 回话过期处理
    $.ajaxSetup({
      type: 'POST',
      complete: function(xhr,status) {
        var sessionStatus = xhr.getResponseHeader('sessionstatus');
        if(sessionStatus == 'timeout') {
          window.top.location.href = window.top.location;  
        }
      }
    });
    if ($.fn.datagrid){
    	$.fn.datagrid.defaults.pageSize = 10;//这里一定要用datagrid.defaults.pageSize，用pagination.defaults.pageSize一直不行
    	$.fn.datagrid.defaults.pageList = [10,20,50,100,200,300,500,1000,2000];//这里一定要有，不然上面的也不起效
    }
    $.fn.combobox.defaults.filter = function(q, row){  
        var opts = $(this).combobox('options');  
        return row[opts.textField].indexOf(q) >= 0;  
    }  
    
    // 获取表单的json
    $.fn.serializeJson = function () {
    	 var o = {};
         var a = this.serializeArray();
         $.each(a, function () {
             if (o[this.name]) {
                 if (!o[this.name].push) {
                     o[this.name] = [o[this.name]];
                 }
                 o[this.name].push(this.value || '');
             } else {
                 o[this.name] = this.value || '';
             }
         });
         return o;
    };
    
    // 设置下拉框自动检索
    $.fn.combobox.defaults.onHidePanel = function(q, row){  
    	var valueField = $(this).combobox("options").valueField;
        var val = $(this).combobox("getValue");  //当前combobox的值
        var allData = $(this).combobox("getData");   //获取combobox所有数据
        var result = true;      //为true说明输入的值在下拉框数据中不存在
        for (var i = 0; i < allData.length; i++) {
            if (val == allData[i][valueField]) {
                result = false;
                break;
            }
        }
        if (result) {
            $(this).combobox("clear");
        }
    }
    
    $.fn.combotree.defaults.editable = true;
    $.extend($.fn.combotree.defaults.keyHandler, {
        up: function () {
            console.log('up');
        },
        down: function () {
            console.log('down');
        },
        enter: function () {
            console.log('enter');
        },
        query: function (q) {
            var t = $(this).combotree('tree');
            t.tree('doFilter', q);
        }
    });
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/XuYuRepos/common/load.js"></script>
