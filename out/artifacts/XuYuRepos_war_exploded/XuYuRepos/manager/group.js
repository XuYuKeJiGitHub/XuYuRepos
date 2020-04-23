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
	    
		$("#orgName").combotree({
	        url:bathPath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#orgName").combotree("tree").tree("options").url=bathPath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
	
        //下拉框值加载
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
	    
	    
	    $("#payType").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=PAY_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#payTypeQuery").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=PAY_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#silentType").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=SILENT_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#haveTest").combobox({
		      url: bathPath+'/systemlookupitem/lookUp?fLookUpId=HAVE_TEST',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
       
	    
	    var height= document.body.clientHeight-105;
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
                            field:'comboName',
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
                        	field:'ownerName',
                            title:'群组名称',
                            width:200,
                            align:'center'
                        },
                        {
                            field:'payType',
                            title:'支付类型',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#payType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                            field:'silentType',
                            title:'沉默期类型',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#silentType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                        	field:'haveTest',
                            title:'测试期',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#haveTest").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                        	field:'companyName',
                            title:'代理商',
                            width:150,
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
                    url:bathPath+'/xuyuownerinfo/findList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        comboName:$.trim($("#comboNameQuery").val()),
                  	    ownerName:$.trim($("#ownerNameQuery").val()),
                  	    payType:$("#payTypeQuery").combobox('getValue')
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

})
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/xuyuownerinfo/findList';
                  $("#table").datagrid('load',{
                	  comboName:$.trim($("#comboNameQuery").val()),
                	  ownerName:$.trim($("#ownerNameQuery").val()),
                	  payType:$("#payTypeQuery").combobox('getValue')
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
                	    url:bathPath+'/xuyuownerinfo/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#id").val(data.model.id);
                        	$("#comboType").combobox('setValue', data.model.comboType);
                        	$("#comboName").combobox('setValue',data.model.comboName);
                        	$("#owner").val(data.model.owner);
                        	$("#ownerName").val(data.model.ownerName);
                        	$("#payType").combobox('setValue', data.model.payType);
                        	$("#silentType").combobox('setValue', data.model.silentType);
                        	$("#haveTest").combobox('setValue', data.model.haveTest);
                        	$("#orgName").combobox('setText', data.model.companyName);
                        	$("#orgName").combobox('setValue', data.model.companyId);
//                        	$("#orgId").val(data.model.companyId);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	// 先把名字赋值给相关数据
        	    	
        	    	var orgId=$("#orgName").combobox('getValue');
        	    	var orgName=$("#orgName").combobox('getText');
        	    	$('#orgId').val(orgName);
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/xuyuownerinfo/save',
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
        // 重置表单
        res:function () {
            $("#addForm").form('reset');

        },
        resQuery:function(){
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
                                              url:bathPath+'/xuyuownerinfo/delete',
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
                                        url:bathPath+'/xuyuownerinfo/delete',
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

// 弹出框加载
$("#addBox").dialog({
        title:"编辑群组",
        width:550,
        height:500,
        closed: true,
        modal:true,
        shadow:true
})

