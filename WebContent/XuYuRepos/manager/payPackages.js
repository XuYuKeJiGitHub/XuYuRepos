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
	    
	     //下拉框值加载
        $("#operaQuery").combobox({
          url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
          method : "post",
          valueField: 'value',
          textField: 'text',
          selected: 'selected'
        });
        
        $("#opera").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
          });
        
        $("#availableTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=AVAILABLE_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
         });
        
        $("#availableType").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=AVAILABLE_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto'
         });
        
        $("#stateQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=PACKAGES_STATE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto'
         });
        
        $("#comeonQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto'
         });
        
        $("#comeon").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto',
			onChange: function (n,o) {
		    	  if(n=='y'){
		    		  $("#availableType").combobox('setValue','月清零');
		    	  }else{
		    		  $("#availableType").combobox('setValue','');
		    	  }
	       }
        });
        
        $("#discountQuery").combobox({
        url: basePath+'/systemlookupitem/lookUp?fLookUpId=DISCOUNT',
        method : "post",
        valueField: 'value',
        textField: 'text',
        selected: 'selected',
        panelHeight:'auto'
    });

    $("#discount").combobox({
        url: basePath+'/systemlookupitem/lookUp?fLookUpId=DISCOUNT',
        method : "post",
        valueField: 'value',
        textField: 'text',
        selected: 'selected',
        panelHeight:'auto'
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
		
		
		$("#agency").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agency").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
        
        
        
        var height= document.body.clientHeight-135;
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
                checkOnSelect:true,
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
                        	field:'orgName',
                            title:'客户名称',
                            width:200,
                            align:'center'
                        },
                        {
                            field:'opera',
                            title:'运营商',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var provider="";
                        		var fields=$("#opera").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				provider=v.text;
                        			}
                        		})
                                return provider;
                            }
                        },
                        {
                            field:'gprs',
                            title:'流量套餐(MB)',
                            width:100,
                            align:'center',

                        },
                        {
                        	field:'comeon',
                            title:'是否加油包',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var comeon="";
                        		var fields=$("#comeon").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				comeon=v.text;
                        			}
                        		})
                                return comeon;
                            }
                        },
                        {
                        	field:'discount',
                            title:'折扣比例',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var discount="";
                        		var fields=$("#discount").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				discount=v.text;
                        			}
                        		})
                                return discount;
                            }
                        },
                        {
                        	field:'selfPrice',
                            title:'售价',
                            width:50,
                            align:'center'
                        },
                        {
                        	 field:'actualPrice',
                             title:'原价',
                             width:50,
                             align:'center'
                        },{
                        	field:'availableType',
                            title:'有效期',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var state="";
                        		var fields=$("#availableTypeQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				state=v.text;
                        			}
                        		})
                                return state;
                            }
                        },
                        {
                        	field:'state',
                            title:'上架状态',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var state="";
                        		var fields=$("#stateQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				state=v.text;
                        			}
                        		})
                                return state;
                            }
                        },
                        {
                            field:'updateTime',
                            title:'更新时间',
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
                $.ajax({
                    type : "POST",
                    dataType : "json",
                    url:basePath+'/xuyupackages/findList',
                    data : {
                        gprs:$.trim($("#gprsQuery").val()),
                    	opera:$("#operaQuery").combobox('getValue'),
                  	    availableType:$("#availableTypeQuery").combobox('getValue'),
                  	    queryAgencyId:$.trim($("#agencyQuery").combobox('getValue')),
                  	    state:$.trim($("#stateQuery").combobox('getValue')),
                 	    comeon:$("#comeonQuery").combobox('getValue'),
                	    discount:$.trim($("#discountQuery").combobox('getValue')),
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
        
        obj.find();
        
        if(orgCode=='1000'){
        }else{
        	 $("#addbtn").hide();
        	 $("#deletebtn").hide();
        	 $("#sendbtn").hide();
        }

})
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = basePath+'/xuyupackages/findList';
                  $("#table").datagrid('load',{
                       gprs:$.trim($("#gprsQuery").val()),
                	   opera:$("#operaQuery").combobox('getValue'),
                	   availableType:$("#availableTypeQuery").combobox('getValue'),
                	   queryAgencyId:$.trim($("#agencyQuery").combobox('getValue')),
                	   state:$.trim($("#stateQuery").combobox('getValue')),
                	   comeon:$("#comeonQuery").combobox('getValue'),
                	   discount:$.trim($("#discountQuery").combobox('getValue')),
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
                	    url:basePath+'/xuyupackages/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	if(orgCode=='1000'){
                        		$("#gprs").numberbox({disabled:false});
                            	$("#order").numberbox({disabled:false});
                            	$("#selfPrice").numberbox({disabled:false});
                            	$("#actualPrice").numberbox({disabled:false});
                            	$("#desc").textbox({disabled:false});
                            	$("#comeon").combobox({disabled:false});
                            	$("#discount").combobox({disabled:false});
                            	$("#opera").combobox({disabled:false});
                            	$("#availableType").combobox({disabled:false});
                            	$("#commission").numberbox({disabled:false});
                        	}else{
                            	$("#gprs").numberbox({disabled:true});
                            	$("#order").numberbox({disabled:false});
                            	$("#selfPrice").numberbox({disabled:false});
                            	$("#actualPrice").numberbox({disabled:true});
                            	$("#desc").textbox({disabled:false});
                            	$("#commission").numberbox({disabled:true});
                            	$("#comeon").combobox({disabled:true});
                            	$("#discount").combobox({disabled:true});
                            	$("#opera").combobox({disabled:true});
                            	$("#availableType").combobox({disabled:true});
                        	}
                        	$("#id").val(data.model.id);
                        	$("#gprs").numberbox('setValue',data.model.gprs);
                        	$("#order").numberbox('setValue',data.model.order);
                        	$("#selfPrice").numberbox('setValue',data.model.selfPrice);
                        	$("#actualPrice").numberbox('setValue',data.model.actualPrice);
                        	$("#desc").textbox("setValue",data.model.desc);
                        	$("#comeon").combobox('setValue', data.model.comeon);
                        	$("#discount").combobox('setValue', data.model.discount);
                        	$("#opera").combobox('setValue', data.model.opera);
                        	$("#availableType").combobox('setValue', data.model.availableType);
                        	$("#commission").numberbox('setValue',data.model.commission);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	// 
        	    	var comeon=$("#comeon").combobox('getValue');
        	    	var availableType=$("#availableType").combobox('getValue');
        	    	if((comeon=="y"&&availableType=="1")||(comeon=="n")){
        	    		// 判断价格售价必须大于原价
        	    		var selfPrice=$("#selfPrice").numberbox('getValue');
                    	var actualPrice=$("#actualPrice").numberbox('getValue');
                    	var total=(parseFloat(selfPrice)-parseFloat(actualPrice));
                    	if(total<0){
                    		$.messager.alert('信息提示','售价必须大于原价','info');
                    	}else{
                    		$.ajax({            
          	                  type:"POST",   //post提交方式默认是get
          	                  url:basePath+'/xuyupackages/save',
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
        	    	}else{
        	    		 $.messager.alert('信息提示','加油包只支持月套餐','info');
        	    	}
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
        canSend:function(){
	            $("#sendBox").dialog({
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
                                              url:basePath+'/xuyupackages/delete',
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
        // 上架或者下架
        setState:function (id) {
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
                    $.messager.confirm('确定删除','你确定上架或下架选择的记录吗？',function (flg) {
                            if(flg){
                                    var ids=[];
                                    for(i=0;i<rows.length;i++){
                                            ids.push(rows[i].id);

                                    }
                                    var num=ids.length;
                                   $.ajax({
                                           type:'post',
                                           url:basePath+'/xuyupackages/setState',
                                           data:{
                                                   ids:ids.join(';')
                                           },
                                           beforesend:function () {
                                                   $("#table").datagrid('loading');
                                                   
                                           },
                                           success:function (data) {
                                        	   $.messager.show({
                   	                            title:'提示信息',
                   	                            msg:"操作成功"
                   	                           })
                   	                           obj.find();
                                           }
                                   })
                            }
                    })

            }
            else{
                    $.messager.alert('信息提示','请选择要操作的记录','info');
            }
        },
        payNotify:function(){
        	 $.ajax({
                 type:'post',
                 url:basePath+'/xuyurecharge/payNotify',
                 data:{
                	 out_trade_no:'20190608220100059972'
                 },
                 beforesend:function () {
                     $("#table").datagrid('loading');
                 },
                 success:function (data) {
              	   $.messager.show({
                         title:'提示信息',
                         msg:"操作成功"
                        })
                        obj.find();
                 }
         })
        },
        send:function(){
        	var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	$("#sendBox").dialog({
                        closed: false
                })
            	var ids="";
                for(i=0;i<rows.length;i++){
                	if((i+1)%3==0){
                		ids=ids+rows[i].id+";"+"\r";
                	}else{
                		ids=ids+rows[i].id+";";
                	}
                }
                $("#sendForm").form('reset');
                var result1=ids;
                $("#ids").textbox('setValue',result1);
            }else{
                $.messager.alert('信息提示','请选择要发送的数据','info');
            }
        },
        sumSend:function(){
        	 var valid=$("#sendForm").form('enableValidation').form('validate');
     	     if(valid){
     	       	 $.ajax({
                     type:"POST",   //post提交方式默认是get
                     url:basePath+'/xuyupackages/packageSend',
                     data:$("#sendForm").serialize(),   //序列化               
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
    	                       });
    	                       obj.canSend()
    	                       obj.find();
    	                	}
                     }  
    	        });
     	     }
        }
}

// 弹出框加载
$("#addBox").dialog({
        title:"新增/修改充值套餐",
        width:480,
        height:600,
        closed: true,
        modal:true,
        shadow:true
})

// 弹出框加载
$("#sendBox").dialog({
        title:"发送充值套餐",
        width:480,
        height:350,
        closed: true,
        modal:true,
        shadow:true
})

