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
		$("#cardOrgId").combotree({
		        url:bathPath+'/xuyucardtype/loadOrgTree', //这是第一次加载树的url 加载根节点
		        onBeforeExpand:function(node){   //展开前获取数据
		        	 $("#cardOrgId").combotree("tree").tree("options").url=bathPath+'/xuyucardtype/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
		        }
		 });
		
		// 左边树
		$('#tt').tree({
			  //异步树 
		    url: bathPath+'/xuyucardtype/loadOrgTree',
		    onClick:function(node){
		    	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/xuyucardtype/findList'+'?random='+Math.random();
		    	 $("#table").datagrid('load',{
		    		 upOrgId:node.id
               })
		    },
		    onBeforeExpand: function(node){
		    	if(node){
		    		$('#tt').tree('options').url = bathPath+'/xuyucardtype/loadOrgChildrenTree?FID='+node.id+'&random='+Math.random();  
		    	}
		    } 
		});
       
        // 加载表格
        $("#table").datagrid({
                title:"数据列表",
                iconCls:"icon-left02",
                url:'',
                loadMsg:"正在加载数据...",
                fitColumns:true,
                striped:true,
                pagination:true,
                width:'100%',
                rownumbers:true,
                pageNumber:1,
                nowrap:true,
                height:'auto',
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
                            field:'cardName',
                            title:'卡名称',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'cardLevel',
                            title:'卡层级',
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
                    url:bathPath+'/xuyucardtype/findList',
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
	              opts.url = bathPath+'/xuyucardtype/findList';
                  $("#table").datagrid('load',{
                	   cardName:$.trim($("#cardNameQuery").val())
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
                	    url:bathPath+'/xuyucardtype/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#id").val(data.model.id);
                        	$("#cardName").val(data.model.cardName);
                        	$("#cardOrgId").combotree('setValue', data.model.cardOrgId);
                        }
                })
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/xuyucardtype/save',
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
                                              url:bathPath+'/xuyucardtype/delete',
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
                                                                      msg:num+'个机构被删除'
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
                                        url:bathPath+'/xuyucardtype/delete',
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
        title:"卡类信息维护",
        width:500,
        height:350,
        closed: true,
        modal:true,
        shadow:true
})

