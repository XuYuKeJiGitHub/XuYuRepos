/**
 * Created by Administrator on 2017/11/8.
 */
$("#mainBox").layout({
        fit:true,
        border:false
})
$("#mean").menu('show',{
        left: 200,
        top: 100
})
$("#left01").accordion({
        border:false

})
$("#con").tabs({
        fit:true,
        border:false
})
$("#myMes").dialog({
        title:"个人信息详情",
        width:400,
        height:420,
        modal:true,
        iconCls:'icon-mes',
        maximizable:true,
        closed: true

})
function openMes() {
        $("#myMes").dialog({
                closed: false

        })
        
        // 设置值
        $("#u_userName").val(userName);
        $("#u_cname").val(cname);

}
function saveExit() {
        $.messager.confirm('退出确认','你是否退出系统？',function(but){
              if(but==true){
            	  $.ajax({            
	                  type:"GET",   //post提交方式默认是get
	                  url:basePath+"/user/logout", 
	                  error:function(request) {},
	                  success:function(data) {
	                	  if(data.sucess==true){
	                		   window.location.href = "login.jsp";
	                	  }else{
	                		  $.messager.alert('信息提示',data.msg);
	                	  }
	                  }            
	            });
              }
        })

}

 //删除Tabs
function closeTab(menu,type){
    var allTabs = $("#con").tabs('tabs');
    var allTabtitle = [];
    $.each(allTabs,function(i,n){
        var opt=$(n).panel('options');
        if(opt.closable)
            allTabtitle.push(opt.title);
    });

    switch (type){
        case 1 :
            var curTabTitle = $(menu).data("tabTitle");
            $("#con").tabs("close", curTabTitle);
            return false;
        break;
        case 2 :
            for(var i=0;i<allTabtitle.length;i++){
                $('#con').tabs('close', allTabtitle[i]);
            }
        break;
        case 3 :
        	 var curTabTitle = $(menu).data("tabTitle");
        	 for(var i=0;i<allTabtitle.length;i++){
        		 if(curTabTitle!=allTabtitle[i]){
        			 $('#con').tabs('close', allTabtitle[i]);
        		 }
             }
        break;
    }
}


//监听右键事件，创建右键菜单
$('#con').tabs({
    onContextMenu:function(e, title,index){
        $('#mm1').menu('show', {
            left: e.pageX,
            top: e.pageY
        }).data("tabTitle", title);
        e.preventDefault();
    }
});

//右键菜单click
$("#mm1").menu({
    onClick : function (item) {
    	debugger;
        closeTab(this, item.name);
    }
});

// 初始化操作
$(function () {
	
	 $("#cname").html(cname);
	// 获取基本菜单
	 $.ajax({  
		type: 'POST',
		dataType: "json",
		url:basePath+'/systemmenu/loadUserFirstMenu',
		success: function(data){
			$.each(data,function(i,n){
				$('#menu').accordion('add',{
					title: n.name,
					selected: false,
					content:'<div style="padding:10px"><ul name="'+n.name+'" id="'+n.id+'"></ul></div>',
				});
			});
		}
	}); 
	
	 // 动态菜单第二部
	$('#menu').accordion({
		onSelect: function(title,index){
			var id=$("ul[name='"+title+"']").attr('id');
			$("ul[name='"+title+"']").tree({
				url: basePath+'/systemmenu/loadUserChildrenMenu?FID='+id,
				onClick:function(node){
			        var testVal=node.text;
			        var tab=$('#con').tabs('getTab',testVal);
			        if(tab==undefined||tab==''){
			        	    var thisUrl=basePath+node.attributes.href;
					        var con = '<div style="overflow：hidden;height:100%;"><iframe scrolling="no" frameborder="0" scroll="false"  src="'+thisUrl+'" style="width:100%;height:100%;"></div>';
					        $('#con').tabs('add',{
				                title: testVal,
				                selected: true,
				                closable:true,
				                content:con
			                });
			        }else{
			        	$('#con').tabs('select',testVal);
			        }
				}
			});
		}
	});	
})

obj={
	 sum:function () {
 	    var valid=$("#addForm").form('enableValidation').form('validate');
 	    if(valid){
 	    	
 	    	var cname =$("#u_cname").val();
 	    	var a1=$.md5('111111');
 	    	var old_password =$("#u_old_password").val();
 	    	var new_password =$("#u_new_password").val();
 	    	var confirm_password=$("#u_confirm_password").val();
 	    	if(new_password!=''||confirm_password!=''){
 	    		if(new_password!=confirm_password){
 	    			$.messager.alert('信息提示','两次密码不一致');
 	    			return false;
 	    		}
 	    	}else{
 	    		
 	    	}
 	    	if(old_password!=''){
 	    		old_password=$.md5(old_password)
 	    	};
 	    	if(new_password!=''){
 	    		new_password=$.md5(new_password)
 	    	};
 	    	
 	    	$.ajax({            
                 type:"POST",   //post提交方式默认是get
                 url:basePath+'/user/resetName',
                 data:{
                	 cname:cname,
                	 old_password:old_password,
                     new_password:new_password
                 },    
                 error:function(request) {      // 设置表单提交出错                 
                     $("#showMsg").html(request);  //登录错误提示信息
                 },
                 success:function(data) {
                	 if(data.sucess==true){
                		 if(old_password==''){
                			 $.messager.show({
                                 title:'提示信息',
                                 msg:"操作成功"
                             })
                		 }else{
	                		   $.messager.alert({
	                               title:'提示信息',
	                               msg:"密码修改成功，请重新登录"
	                          });
                			 
	                		  setTimeout(function(){
	                			  $.ajax({            
		           	                  type:"GET",   //post提交方式默认是get
		           	                  url:basePath+"/user/logout", 
		           	                  error:function(request) {},
		           	                  success:function(data) {
		           	                	  if(data.sucess==true){
		           	                		   window.location.href = "login.jsp";
		           	                	  }else{
		           	                		  $.messager.alert('信息提示',data.msg);
		           	                	  }
		           	                  }            
	           	                  });
	                		  },2000);
                			  
                		 }
                	 }else{
                		 $.messager.alert('信息提示',data.msg);
                	 }
	                	
                 }            
              });
 	    }else{
 	    	alert("校验不通过");
 	    }
   }
}
