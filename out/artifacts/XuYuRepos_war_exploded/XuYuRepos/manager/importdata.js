/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
        //下拉框值加载
	    $("#operator").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
	      method : "post",
	      valueField: 'value',
	      textField: 'text',
	      selected: 'selected',
	      onChange: function (n,o) {
	    	  if(n=='1'){
	    		  $('#ownerPlace').combobox({required:true});
	    		  $("#ownerPlaceDiv").show();
	    	  }else{
	    		  $('#ownerPlace').combobox({required:false})
	    		  $("#ownerPlaceDiv").hide();
	    		 
	    	  }
	      }
	    });
       
	    
	    var height= document.body.clientHeight-80;
        // 加载表格
        $("#table").datagrid({
                title:"数据列表",
                url:'',
                loadMsg:"正在加载数据...",
                fitColumns:true,
                striped:true,
                pagination:true,
                rownumbers:true,
                pageNumber:1,
                nowrap:true,
                height:height,
                sortName:'id',
                checkOnSelect:false,
                sortOrder:'asc',
                toolbar: '#tabelBut',
                columns:[[
                        {
                        	    checkbox:true,
                                field:'id',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'cname',
                            title:'接入号',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'b3',
                            title:'同步时间',
                            width:100,
                            align:'center'
                        }
                ]]
        })
        
        var p = $('#table').datagrid('getPager');
        $(p).pagination({
            pageSize: 10,//每页显示的记录条数，默认为10
            pageList: [5,10,15,20],//可以设置每页记录条数的列表
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
                $.ajax({
                    type : "POST",
                    dataType : "json",
                    url:basePath+'/org/findList',
                    data : {
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

})
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = basePath+'/org/findList';
                  $("#table").datagrid('load',{
                	   orgName:$.trim($("#usernameQuery").val())
                  })
        },
        // 添加
        addBox:function () {
                $("#addBox").dialog({
                        closed: false
                })
                $("#addForm").form('reset');
               
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
        // 编辑
        editOne:function (id) {
                $("#res").hide();
                $("#can").show();
                $("#addBox").dialog({
                        closed: false,
                });
                
                $.ajax({
                	    url:basePath+'/org/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#id").val(data.model.id);
                        	$("#orgId").val(data.model.orgId);
                        	$("#orgName").val(data.model.orgName);
                        	$("#upOrgId").combobox('setValue', data.model.upOrgId);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:basePath+'/org/save',
  	                  data:$("#addForm").serialize(),   //序列化               
  	                  error:function(request) {      // 设置表单提交出错                 
  	                      $("#showMsg").html(request);  //登录错误提示信息
  	                  },
  	                  success:function(data) {
	  	                	$.messager.show({
	                            title:'提示信息',
	                            msg:"操作成功"
	                        })
	                        obj.can()
	                        obj.find();
  	                  }            
  	               });
        	    }
        },
        submitType:function(){
            $("#importTypeBox").dialog({
                    closed: false
            })
            $("#importTypeForm").form('reset');
        },
        openFileUpload:function(){
            $("#importBox2").dialog({
                    closed: false
            })
            $("#importTypeForm").form('reset');
        },
        cleanLT:function(){
            $("#importBox2").dialog({
                closed: false
            })
            $("#importTypeForm").form('reset');
        },
        cleanYDandDX:function(){
            $("#importBox2").dialog({
                closed: false
            })
            $("#importTypeForm").form('reset');
        },
        import1:function(){
            alert(22222)
        	var valid=$("#importTypeForm").form('enableValidation').form('validate');
     	    if(valid){
     	    	 if($('#operator').combobox('getText')=='请选择'){
     	    		alert('运营商为必填');return false;
     	    	 }
     	    	 // 关掉运营商选择界面
     	         $("#importTypeBox").dialog({
                    closed: true
                 });
     	    	 $("#importBox").dialog({
                     closed: false
                 })
                 $("#importForm").form('reset');
     	    }
           
        },
        upload:function(){
        	// 导入附件示例
    	    var attach = document.getElementById("uploadFile").value;
    	    if(attach == undefined || attach == ""){
    	        alert("请选择文件");
    	        return;
    	    }
    	    
    	    var operator=$("#operator").combobox('getValue');
    	    
    	    var formData = new FormData();
    	    formData.append("file",$("#uploadFile")[0].files[0]);
    	    $.ajax({
    	    	url:basePath+'/systemannexe/upload',
    	    	type:'post',
     	        data: formData,
     	        contentType: false,
     	        processData: false,
    	        success:function(res){
    	        	alert("ok");
    	        },error:function(data) {      // 设置表单提交出错 
                    alert("错误");
                }
    	    })     
        },
    uploadStateFile:function(){
        var attach = document.getElementById("uploadStateFile").value;
        if(attach == undefined || attach == ""){
            alert("请选择文件");
            return;
        }

        var asyncType="cardUpdateState";
        var formData = new FormData();
        formData.append("file",$("#uploadStateFile")[0].files[0]);
        $.ajax({
            url:basePath+'/csvoprate/upload?&asyncType='+asyncType,
            type:'post',
            data: formData,
            contentType: false,
            processData: false,
            beforeSend: function () {
                $.messager.progress({
                    title: '提示信息',
                    msg: '正在上传文件，请稍候……',
                    text: ''
                });
            },
            complete: function () {
                $.messager.progress('close');
            },
            error:function(request) {      // 设置表单提交出错
                $("#showMsg").html(request);  //登录错误提示信息
            },
            success:function(res){
                var resp=eval('(' + res+ ')')
                $.messager.show({
                    title:'提示信息',
                    msg:resp.mess
                });

                obj.can1();
            }
        })
    },
        uploadFile:function(){
    	    var attach = document.getElementById("uploadFile").value;
    	    if(attach == undefined || attach == ""){
    	        return;
    	    }
    	    
    	    var operator=$("#operator").combobox('getValue');
    	    var asyncType="cardUpdate";
    	    var formData = new FormData();
    	    formData.append("file",$("#uploadFile")[0].files[0]);
    	    $.ajax({
    	    	url:basePath+'/csvoprate/upload?operator='+operator+"&asyncType="+asyncType,
    	        type:'post',
    	        data: formData,
    	        contentType: false,
    	        processData: false,
                beforeSend: function () {
                      $.messager.progress({
                          title: '提示信息',
                          msg: '正在上传文件，请稍候……',
                          text: ''
                      });
                },
                complete: function () {
                      $.messager.progress('close');
                },
                error:function(request) {      // 设置表单提交出错                 
                  $("#showMsg").html(request);  //登录错误提示信息
                },
    	        success:function(res){
    	        	var resp=eval('(' + res+ ')')
    	        	$.messager.show({
                      title:'提示信息',
                      msg:resp.mess
                   });
    	        	
    	           obj.can1();
    	        }
    	    })     
        },

    // 导出模板
        loadModel:function(){
        	var operator=$("#operator").combobox('getValue');
        	$.ajax({            
              type:"POST",   //post提交方式默认是get
                url:basePath+'/cardinfoimport/exportModel',
                data:{type:operator},   //序列化               
                error:function(request) {
                	$.messager.show({
                        title:'提示信息',
                        msg:"操作失败请稍候再试"
                    });
                },
                success:function(data) {
					var filename = data.filename;
					var filepath = data.filepath;
					var filenameCN = data.annexeName;
					if (filename != null&& filename != ""&& filename != "undfined") {
						window.open(basePath+'/systemannexe/downFile?filename='+filename+'&annexeName='+encodeURIComponent(filenameCN)+'&uploadPath='+encodeURIComponent(filepath),'_self'); 
					} else {
						Ext.MessageBox.alert('信息提示',
								'文件导出失败，请稍后重试！');
						return false;
					}
                }            
             });
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
        // 取消表单
        can1:function () {
                $("#importBox").dialog({
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
                                              url:basePath+'/org/delete',
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
                                        url:basePath+'/org/delete',
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

$("#importTypeBox").dialog({
    title:"选择运营商",
    width:500,
    height:200,
    closed: true,
    modal:true,
    shadow:true
})


//弹出框加载
$("#importBox").dialog({
    title:"批量导入",
    width:500,
    height:200,
    closed: true,
    modal:true,
    shadow:true
})

//弹出框加载
$("#importBox2").dialog({
    title:"批量导入",
    width:500,
    height:200,
    closed: true,
    modal:true,
    shadow:true
})


