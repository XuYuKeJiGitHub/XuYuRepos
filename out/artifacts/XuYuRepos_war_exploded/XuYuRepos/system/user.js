/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
	
	 // 回车事件
	    $(document).keydown(function (event) {
	       if (event.keyCode == 13) {
	         $('#btnQuery').triggerHandler('click');
	       }
	     });
	    
		$('#email').validatebox({
		    required: true,
		    validType: 'email'
		});
		
		$('#mobile').validatebox({
		    required: true,
		    validType: 'phoneNum'
		});
		

        //下拉框值加载
        $("#sex").combobox({
          url: bathPath+'/systemlookupitem/lookUp?fLookUpId=USER_SEX',
          method : "post",
          valueField: 'value',
          textField: 'text',
          selected: 'selected'
        });
        
        // 左边树
		$('#tt').tree({
			  //异步树 
		    url: bathPath+'/org/loadOrgTree',
		    onClick:function(node){
		    	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/user/findList'+"?random="+Math.random();
	             var orgId=node.id;
	             if(orgId=='0'){
	            	 orgId='';
	             }
		    	 $("#table").datagrid('load',{
		    		 queryOrgId:orgId
               })
		    },
		    onBeforeExpand: function(node){
		    	if(node){
		    		$('#tt').tree('options').url = bathPath+'/org/loadOrgChildrenTree?FID='+node.id+"&random="+Math.random();  
		    	}
		    } 
		});
		
		$("#orgNameQuery").combotree({
	        url:bathPath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#orgNameQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
		
		$("#deptNameQuery").combotree({
	        url:bathPath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#deptNameQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
		
		var height= document.body.clientHeight-75;
        // 加载表格
        $("#table").datagrid({
                title:"数据列表",
                url:'',
                loadMsg:"正在加载数据...",
                fitColumns:true,
                striped:true,
                pagination:true,
                width:'100%',
                rownumbers:true,
                pageNumber:1,
                nowrap:true,
                height:height,
                sortName:'id',
                checkOnSelect:true,
                sortOrder:'asc',
                toolbar: '#tabelBut',
                columns:[[
                        {
                                checkbox:true,
                                field:'no',
                                width:100,
                                align:'center'
                        },
                        {
                        	    hidden:true,
                                field:'id',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                                field:'userName',
                                title:'账号',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'cname',
                            title:'姓名',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'sex',
                            title:'姓别',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                                if(val=='01'){
                                        return '<div style="color: green">男</div>';
                                }
                                else if(val=='02'){
                                        return '<div style="color: red">女</div>';
                                }

                            }
                        },
                        {
                            field:'orgName',
                            title:'所属机构',
                            width:100,
                            align:'center'
                        },
                        {
                                field:'deptName',
                                title:'所属部门',
                                width:100,
                                align:'center'
                        }
                ]]
        })
        
        var p = $('#table').datagrid('getPager');
        $(p).pagination({
            pageSize: 10,//每页显示的记录条数，默认为10
            pageList: [10,20,50,100],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '共 {total} 条记录',
            onSelectPage: function (pageNumber, pageSize) {//分页触发
                find(pageNumber, pageSize);
            }
        });
        
        function find(pageNumber, pageSize)
        {
                $("#table").datagrid('getPager').pagination({pageSize : pageSize, pageNumber : pageNumber});//重置
                $("#table").datagrid("loading"); //加屏蔽
                var orgId='';
                if($("#tt").tree('getSelected')!=null){
                	orgId=$("#tt").tree('getSelected').id;
                }
                $.ajax({
                    type : "POST",
                    dataType : "json",
                    url:bathPath+'/user/findList',
                    data : {
                    	userName:$.trim($("#usernameQuery").val()),
                 	    cname:$.trim($("#cnameQuery").val()),
                 	    orgId:orgId,
                        pageNumber : pageNumber,
                        pageSize : pageSize
                    },
                    success : function(data) {
                        $("#table").datagrid('loadData',data);
                        $("#table").datagrid("loaded"); //移除屏蔽
                    },
                    error : function(err) {
                        $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                        $("#table").datagrid("loaded"); //移除屏蔽
                    }
                });

        }
        
        obj.loadFirst();

})
obj={
        // 查询
	   loadFirst:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/user/findList';
	             var orgId='';
                 if($("#tt").tree('getSelected')!=null){
                	orgId=$("#tt").tree('getSelected').id;
                 }
                  $("#table").datagrid('load',{
                	    pageNumber : 1,
	       		        pageSize : 10,
                	    userName:$.trim($("#usernameQuery").val()),
                	    cname:$.trim($("#cnameQuery").val()),
                	    orgId:orgId
                  })
        },
        find:function () {
        	obj.reload();
        },
        reload:function(){
        	var orgId='';
            if($("#tt").tree('getSelected')!=null){
            	orgId=$("#tt").tree('getSelected').id;
            }
        	var options = $("#table").datagrid("getPager").data("pagination").options;
         	var pnum = options.pageNumber;
         	var psize = options.pageSize;
         	$("#table").datagrid("reload",{
	              userName:$.trim($("#usernameQuery").val()),
	        	  cname:$.trim($("#cnameQuery").val()),
	        	  orgId:orgId,
              	  pageNumber:pnum,
            	  pageSize:psize
         	});
        },
        // 添加
        addBox:function () {
                $("#addBox").dialog({
                        closed: false
                })
                $("#addForm").form('reset');
                $('#userName').attr('readonly',false);  
                $("#can").hide();
                $("#res").show();
        },// 编辑
        edit:function (id) {
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		var id=rows[0].id;
            		obj.editOne(id);
            	}else{
            		$.messager.alert('信息提示','只能编辑一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择要修改的记录','info');
            }
        },
        editRole:function (id) {
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		var id=rows[0].id;
            		var userName=rows[0].userName;
            		var cname=rows[0].cname;
            		 $("#res1").hide();
                     $("#can1").show();
            		 $("#addRoleBox").dialog({
                         closed: false,
                    });
            		 
            		 $.ajax({
                 	    url:bathPath+'/user/find',
                         type:'POST',
                         dataType:'json',
                         data: { 
                         	id:id
                         },
                         success:function (data) {
                         	$("#userRoleId").val(data.model.userRoleId);
                         	$("#userName1").val(userName);
                         	$("#cname1").val(cname);
                         }
                    })
            	}else{
            		$.messager.alert('信息提示','只能编辑一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择要修改的记录','info');
            }
        },
        // 编辑
        editOne:function (id) {
                $("#res").hide();
                $("#can").show();
                $("#addBox").dialog({
                        closed: false,
                });
                
                $.ajax({
                	    url:bathPath+'/user/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#id").val(data.model.id);
                        	$("#userName").val(data.model.userName);
                        	$('#userName').attr('readonly',true);  
                        	$("#cname").val(data.model.cname);
                        	$("#orgNameQuery").combobox('setText',data.model.orgName)
                        	$("#orgId").val(data.model.orgId);
                        	$("#deptNameQuery").combobox('setText',data.model.deptName)
                        	$("#deptId").val(data.model.deptId);
                        	$("#email").val(data.model.email);
                        	$("#mobile").val(data.model.mobile);
                        	$("#sex").combobox('setValue', data.model.sex);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	var orgId=$("#orgNameQuery").combobox('getValue');
        	    	var orgName=$("#orgNameQuery").combobox('getText');
        	    	var deptId=$("#deptNameQuery").combobox('getValue');
        	    	var deptName=$("#deptNameQuery").combobox('getText');
        	    	$('#orgId').val(orgId);
        	    	$('#deptId').val(deptId);
        	    	$("#orgName").val(orgName);
        	    	$("#deptName").val(deptName);
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/user/save',
  	                  data:$("#addForm").serialize(),   //序列化               
  	                  beforeSend: function () {
                        $.messager.progress({
                            title: '提示信息',
                            msg: '正在处理，请稍候……',
                            text: ''
                        });
                      },
                      complete: function () {
                        $.messager.progress('close');
                      },
  	                  error:function(request) {      // 设置表单提交出错                 
  	                      $("#showMsg").html(request);  //登录错误提示信息
  	                  },
  	                  success:function(data) {
	  	                	if(data.sucess!=undefined&&data.sucess==false){
	  	                		$.messager.alert('信息提示',data.msg,'error');
	  	                	}else{
	  	                		$.messager.show({
		                            title:'提示信息',
		                            msg:"操作成功"
		                        })
		                        obj.can()
		                        obj.find();
	  	                	}
  	                  }            
  	               });
        	    }
        },
        queryRes:function(){
    	    $("#queryForm").form('reset');
        },
        // 重置表单
        res:function () {
                $("#addForm").form('reset');

        },
        // 取消表单
        can:function () {
                $("#addBox").dialog({
                        closed: true
                })
        },
        can1:function () {
            $("#addRoleBox").dialog({
                    closed: true
            })
        },
        // 删除多个
        del:function () {
               var rows=$("#table").datagrid("getSelections");
               if(rows.length>0){
                       $.messager.confirm('确定删除','你确定要删除你选择的记录吗？',function (flg) {
                               if(flg){
                                       var ids=[];
                                       for(i=0;i<rows.length;i++){
                                               ids.push(rows[i].id);

                                       }
                                       var num=ids.length;
                                      $.ajax({
                                              type:'post',
                                              url:bathPath+'/user/delete',
                                              data:{
                                                      ids:ids.join(';')
                                              },
                                              beforesend:function () {
                                                      $("#table").datagrid('loading');
                                                      
                                              },
                                              success:function (data) {
                                                      if(data){
                                                              $("#table").datagrid('loaded');
                                                              $("#table").datagrid('load');
                                                              $("#table").datagrid('unselectAll');
                                                              $.messager.show({
                                                                      title:'提示',
                                                                      msg:num+'个用户被删除'
                                                              })
                                                      }
                                                      else{
                                                              $.messager.show({
                                                                      title:'警示信息',
                                                                      msg:"信息删除失败"
                                                              })
                                                      }
                                              }
                                      })
                               }

                       })

               }
               else{
                       $.messager.alert('提示','请选择要删除的记录','info');
               }

        },

        //删除一个
        delOne:function (id) {
                id=$("#table").datagrid('getSelected').id;
                $.messager.confirm('提示信息','是否删除所选择记录',function (flg) {
                        if(flg){
                                $.ajax({
                                        type:'post',
                                        url:bathPath+'/user/delete',
                                        data:{
                                        	    ids:id
                                        },
                                        beforesend:function () {
                                                $("#table").datagrid('loading');

                                        },
                                        success:function (data) {
                                                if(data){
                                                        $("#table").datagrid("loaded");
                                                        $("#table").datagrid("load");
                                                        $("#table").datagrid("unselectRow");
                                                        $.messager.show({
                                                                title:'提示信息',
                                                                msg:"信息删除成功"
                                                        })
                                                }
                                                else{
                                                        $.messager.show({
                                                                title:'警示信息',
                                                                msg:"信息删除失败"
                                                        })
                                                }
                                        }
                                })
                        }
                })
        }
}


function openOrg(){
    $("#openOrgId").val('');
	var  url = bathPath + "/XuYuRepos/system/orgChoose.jsp";
	var iWidth=350; //弹出窗口的宽度;
    var iHeight=300; //弹出窗口的高度;
    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	 window.open('','newWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	 $("#openForm").submit();  
};

function openDept(){
	$("#openOrgId").val('');
	
	$("#openOrgId").val($("#orgId").val());
	
	var  url = bathPath + "/XuYuRepos/system/orgChoose.jsp";
	var iWidth=350; //弹出窗口的宽度;
    var iHeight=300; //弹出窗口的高度;
    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	window.open('','newWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	$("#openForm").submit();  
};


// 弹出框加载
$("#addBox").dialog({
        title:"用户信息维护",
        width:500,
        height:350,
        closed: true,
        modal:true,
        shadow:true
})

$("#addRoleBox").dialog({
        title:"用户角色信息维护",
        width:500,
        height:350,
        closed: true,
        modal:true,
        shadow:true
})

