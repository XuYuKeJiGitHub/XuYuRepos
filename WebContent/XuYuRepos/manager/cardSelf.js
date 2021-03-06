/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
	
	    // 回车事件
	    $(document).keydown(function (event) {
	       if (event.keyCode == 13) {
	    	   obj.find();
	       }
	    });
	    
	    $("#numType").hide();
	    $("#accessNumStart").hide();
	    $("#accessNumEnd").hide();
	    
	    $("#accessNumStartOwner").hide();
	    $("#accessNumEndOwner").hide();
	   //下拉框值加载
	    $("#yesNo").combobox({
	      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
	      method : "post",
	      valueField: 'value',
	      textField: 'text',
	      selected: 'selected',
	      onChange: function (n,o) {
	    	  if(n=='y'){
	    		  $('#accessNumStartId').validatebox('options').required = true;
	    		  $('#accessNumEndId').validatebox('options').required = true;
	    		  $('#numTypeId').combobox('options').required = true;
	    		  
	    		  $("#accessNumStart").show();
	    		  $("#accessNumEnd").show();
	    		  $("#numType").show();
	    		  
	    		  
	    		  
	    	  }else{
	    		  $('#accessNumStartId').validatebox('options').required = false;
	    		  $('#accessNumEndId').validatebox('options').required = false;
	    		  $('#numTypeId').combobox('options').required = false;
	    		  
	    		  $("#accessNumStart").hide();
	    		  $("#accessNumEnd").hide();
	    		  $("#numType").hide();
	    		  
	    		 
	    	  }
	      }
	    });
	    
	    
	    $("#numTypeId").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=NUM_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#comboType").combobox({
	      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
	    
	    $("#comboName").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
		$("#agencyQuery").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agencyQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
//		    ,onLoadSuccess:function(node,data){
//	        	 // 默认全部展开
//		         var t = $(this);
//		   		 if(data){
//				     $(data).each(function(index,d){
//						if(this.state == 'closed'){
//						    t.tree('expandAll');
//						}
//				     });
//				}
//		    }
	    });
		
		$("#orgId").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#orgId").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
		
	    $("#firstQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
	    });
	    
        $("#operatorQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
	    
	    $("#firstQuery").combobox('setValue','y');
	    
	    
	    var height= document.body.clientHeight-140;
        // 加载表格
        $("#table").datagrid({
                title:"数据列表",
                url:'',
                loadMsg:"正在加载数据...",
                fitColumns : true,
                striped:true,
                pagination:true,
                rownumbers:true,
                pageNumber:1,
                nowrap:true,
                height:height+'px;',
                sortName:'id',
                checkOnSelect:true,
                sortOrder:'asc',
                toolbar: '#tabelBut',
                remoteSort:false,
                onSortColumn: function (sort,order) {
//	            	obj.reload();
//	            	return false;
	            },  
                columns:[[
                        {
                        	    checkbox:true,
                                field:'id',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'accessNum',
                            title:'接入号',
                            sortable:true,
                            width:180,
                            align:'center'
                            
                        },
                        {
                            field:'iccid',
                            title:'iccid',
                            sortable:true,
                            width:180,
                            align:'center'
                        },
                        {
                            field:'provider',
                            title:'运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var provider="";
                        		var fields=$("#operatorQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				provider=v.text;
                        			}
                        		})
                                return provider;
                            }
                        },
                        {
                        	 field:'agency',
                             title:'代理商id',
                             hidden:true,
                             width:180,
                             align:'center'
                        },
                        {
                            field:'orgName',
                            title:'代理商',
                            width:180,
                            align:'center'
                        },
                        {
                        	 field:'realEstablish',
                             title:'开户日期',
                             width:100,
                             align:'center'
                        },
                        {
                            field:'comboType',
                            title:'套餐类型',
                            width:100,
                            align:'center',
                        	formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#comboType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                            field:'comnoName',
                            title:'套餐',
                            width:200,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#comboName").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                            field:'owner',
                            title:'群组id',
                            hidden:true,
                            width:200,
                            align:'center'
                        },
                        {
                            field:'ownerName',
                            title:'群组',
                            width:200,
                            align:'center'
                        }
                ]]
        })
        
        var p = $('#table').datagrid('getPager');
        $(p).pagination({
        	pageSize: 10,//每页显示的记录条数，默认为20
            pageList: [10,20,50,100,200,300,500,1000,2000],//可以设置每页记录条数的列表
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
                    url:bathPath+'/xuyucontentcardmgrself/findList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        firstQuery:$.trim($("#firstQuery").combobox('getValue')),
                        provider:$.trim($("#operatorQuery").combobox('getValue')),
                		id:$.trim($("#agencyQuery").combobox('getValue')),
                        accessNum:$.trim($("#accessNumQuery").val()),
                  	    accessNumStart:$.trim($("#accessNumStartQuery").val()),
                  	    iccidStart:$.trim($("#iccidStartQuery").val()),
	                  	iccidEnd:$.trim($("#iccidEndQuery").val()),
                  	    accessNumEnd:$.trim($("#accessNumEndQuery").val()),
                  	    establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
                  	    establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue"))
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
		 loadFirst:function(){
        	 var opts = $("#table").datagrid("options");
             opts.url = bathPath+'/xuyucontentcardmgrself/findList';
              $("#table").datagrid('load',{
            	  pageNumber : 1,
                  pageSize : 10,
                  firstQuery:$.trim($("#firstQuery").combobox('getValue')),
                  provider:$.trim($("#operatorQuery").combobox('getValue')),
          		  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
            	  accessNum:$.trim($("#accessNumQuery").val()),
            	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
            	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
            	  iccidStart:$.trim($("#iccidStartQuery").val()),
                  iccidEnd:$.trim($("#iccidEndQuery").val()),
            	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
            	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue"))
              })
		 },
        // 查询
        find:function () {
        	obj.reload();
        },
        reload:function(){
        	var options = $("#table").datagrid("getPager").data("pagination").options;
         	var pnum = options.pageNumber;
         	var psize = options.pageSize;
         	$("#table").datagrid("load",{
         		 firstQuery:$.trim($("#firstQuery").combobox('getValue')),
         		 provider:$.trim($("#operatorQuery").combobox('getValue')),
         		 agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
         		 accessNum:$.trim($("#accessNumQuery").val()),
           	     accessNumStart:$.trim($("#accessNumStartQuery").val()),
           	     accessNumEnd:$.trim($("#accessNumEndQuery").val()),
           	     iccidStart:$.trim($("#iccidStartQuery").val()),
                 iccidEnd:$.trim($("#iccidEndQuery").val()),
           	     establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
           	     establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
	           	 pageSize:psize,
	     		 pageNumber:pnum
         	});
        },
        // 添加
        addBox:function () {
        	    // 判断当前卡是否还在当前代理商名下
                var rows=$("#table").datagrid("getSelections");
                var ids="";
                var bool=true;
                for(i=0;i<rows.length;i++){
                	    if(rows[i].agency!=orgId){
                	    	bool=false;
                	    	if(orgLevel!='1'){
                	    		break;
                	    	}else{
                    	    	if((i+1)%3==0){
                            		ids=ids+rows[i].accessNum+";"+"\r";
                            	}else{
                            		ids=ids+rows[i].accessNum+";";
                            	}
                	    	}
                	    }else{
                	    	if((i+1)%3==0){
                        		ids=ids+rows[i].accessNum+";"+"\r";
                        	}else{
                        		ids=ids+rows[i].accessNum+";";
                        	}
                	    }
                }
                // 判断当前用户是否为总部
                if(orgLevel=='1'){
                	bool=true
                };
                if(bool==false){
                	$.messager.alert('信息提示','当前卡已经不属于本代理商，不能出库','info');
                }else{
                	 $("#addBox").dialog({
	                         closed: false
	                 })
	                 $("#addForm").form('reset');
                	 var result=ids;
                     $("#accessNums").textbox('setValue',result);
                }
               
        },
        // 编辑
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
                	    url:bathPath+'/org/find',
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
        	    	$("#first").val($("#firstQuery").combobox('getValue'));
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/xuyucontentcardmgrself/selfAll',
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
        // 重置表单
        res:function () {
                $("#addForm").form('reset');

        },
        // 重置表单
        queryRes:function () {
                $("#queryForm").form('reset');

        },
        // 取消表单
        can:function () {
                $("#addBox").dialog({
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
                                              url:bathPath+'/org/delete',
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
                                        url:bathPath+'/org/delete',
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
        },
        export:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	
                }
        	})
        }
}

// 弹出框加载
$("#addBox").dialog({
        title:"批量出库",
        width:598,
        height:480,
        closed: true,
        modal:true,
        shadow:true
});


