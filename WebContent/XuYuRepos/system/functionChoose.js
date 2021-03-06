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
                onDblClickRow :function(rowIndex,rowData){
                	 opener.document.getElementById("modFuncId").value=rowData.id;
                	 opener.document.getElementById("modFuncName").value=rowData.funcName;
                	 window.close();
                },
                columns:[[
                        {
                        	    hidden:true,
                                field:'id',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                                field:'funcName',
                                title:'模块名称',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'funcDesc',
                            title:'模块描述',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'action',
                            title:'模块地址',
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
                    url:bathPath+'/systemfunction/findList',
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
        
        obj.find();

})
obj={
	    // 查询
	    find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/systemfunction/findList';
	              $("#table").datagrid('load',{
	            	  funcName : $.trim($("#funcNameQuery").val()),
	            	  funcDesc : $.trim($("#funcDescQuery").val()),
	            	  action : $.trim($("#actionQuery").val()),
	              })
	    },
	    submit:function(){
	    	 var rows=$("#table").datagrid("getSelections");
             if(rows.length>0){
            	 var id=$("#table").datagrid('getSelected').id;
            	 var funcName=$("#table").datagrid('getSelected').funcName;
            	 opener.document.getElementById("modFuncId").value=id;
            	 opener.document.getElementById("modFuncName").value=funcName;
            	 window.close();
             }else{
            	 $.messager.alert('信息提示','请选择您需要的记录','info');
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
        cancle:function () {
        	window.close();
        }
}

