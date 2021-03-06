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
		
		  //下拉框值加载
        $("#fLookUpId").combobox({
          url: bathPath+'/systemlookup/lookUp',
          method : "post",
          valueField: 'value',
          textField: 'text',
          selected: 'selected'
        });
        
        $("#fLookUpIdQuery").combobox({
            url: bathPath+'/systemlookup/lookUp',
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
                width:'100%',
                rownumbers:true,
                pageNumber:1,
                nowrap:true,
                height:height,
                sortName:'id',
                checkOnSelect:true,
                sortOrder:'asc',
                toolbar: '#tabelBut',
                columns:[[{
			               	    checkbox:true,
			                    field:'no',
			                    width:100,
			                    align:'center'
                        },
                        {
                        	    hidden:true,
                                field:'fId',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                                field:'fCode',
                                title:'数据字典代码',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'fValue',
                            title:'数据字典名称',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'fDesc',
                            title:'数据字典描述',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'fOrderId',
                            title:'数据字典排序',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'fLookUpId',
                            title:'数据字典分类',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'flfDesc',
                            title:'数据字典分类描述',
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
                    url:bathPath+'/systemlookupitem/findList',
                    data : {
	                	fCode:$.trim($("#fCodeQuery").val()),
	                	fValue:$.trim($("#fValueQuery").val()),
	                	fDesc:$.trim($("#fDescQuery").val()),
	                	fLookUpId:$.trim($('#fLookUpIdQuery').combobox('getValue')),
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

})
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/systemlookupitem/findList';
                  $("#table").datagrid('load',{
	                	  fCode:$.trim($("#fCodeQuery").val()),
	                	  fValue:$.trim($("#fValueQuery").val()),
	                	  fDesc:$.trim($("#fDescQuery").val()),
	                	  fLookUpId:$.trim($('#fLookUpIdQuery').combobox('getValue'))
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
            		var id=rows[0].fId;
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
                	    url:bathPath+'/systemlookupitem/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#fId").val(data.model.fId);
                        	$("#fCode").val(data.model.fCode);
                        	$("#fValue").val(data.model.fValue);
                        	$("#fDesc").val(data.model.fDesc);
                        	$('#fOrderId').numberbox('setValue', data.model.fOrderId);
                        	$('#fLookUpId').combobox('setValue', data.model.fLookUpId);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/systemlookupitem/save',
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
        resQuery:function () {
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
        // 删除多个
        del:function () {
               var rows=$("#table").datagrid("getSelections");
               if(rows.length>0){
                       $.messager.confirm('确定删除','你确定要删除你选择的记录吗？',function (flg) {
                               if(flg){
                                       var ids=[];
                                       for(i=0;i<rows.length;i++){
                                               ids.push(rows[i].fId);

                                       }
                                       var num=ids.length;
                                      $.ajax({
                                              type:'post',
                                              url:bathPath+'/systemlookupitem/delete',
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
                                                                      msg:num+'个数据字典被删除'
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
                                        url:bathPath+'/systemlookupitem/delete',
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

// 弹出框加载
$("#addBox").dialog({
        title:"数据字典维护",
        width:650,
        height:350,
        closed: true,
        modal:true,
        shadow:true
})

