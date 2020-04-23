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
	    
	    // 附件类型
	    $("#downTypeQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=DOWN_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
	    });
	    
	    $("#statuQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
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
	                            field:'createUser',
	                            title:'创建人账号',
	                            width:100,
	                            align:'center'
                        },
                        {
                                field:'createDate',
                                title:'导出时间',
                                width:100,
                                align:'center'
                        },
                        {
                            field:'downType',
                            title:'附件类型',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                         		var downType="";
                         		var fields=$("#downTypeQuery").combobox('getData');
                         		$.each(fields,function(k,v){
                         			if(v.value==val){
                         				downType=v.text;
                         			}
                         		})
                                return downType;
                             }
                        },
                        {
                            field:'statu',
                            title:'导出是否完成',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                         		var statu="";
                         		var fields=$("#statuQuery").combobox('getData');
                         		$.each(fields,function(k,v){
                         			if(v.value==val){
                         				statu=v.text;
                         			}
                         		})
                                return statu;
                             }
                        },{
                    	    hidden:true,
                            field:'filename',
                            title:'编号',
                            width:100,
                            align:'center'
                        },{
                    	    hidden:true,
                            field:'filepath',
                            title:'编号',
                            width:100,
                            align:'center'
                        },{
                    	    hidden:true,
                            field:'annexeName',
                            title:'编号',
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
                    url:basePath+'/systemfunction/findList',
                    data : {
                    	downType : $.trim($("#downTypeQuery").val()),
                    	statu : $.trim($("#statuQuery").val()),
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
	             opts.url = basePath+'/xuyudowncontrol/findList';
                  $("#table").datagrid('load',{
                	  downType : $.trim($("#downTypeQuery").val()),
                	  statu : $.trim($("#statuQuery").val())
                  })
        },
        export:function(){
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		$("#detailBox").dialog({
                        closed: false
                    });
            		var result=rows[0];
            		if(result.statu=='y'){
            			// 下载错误的文件
                		var filename = result.filename;
       					var filepath = result.filepath;
       					var filenameCN = result.annexeName;
       					if (filename != null&& filename != ""&& filename != "undfined") {
       						window.open(basePath+'/systemannexe/downFile?filename='+filename+'&annexeName='+encodeURIComponent(filenameCN)+'&uploadPath='+encodeURIComponent(filepath),'_self'); 
       					}else{
                    		$.messager.alert('信息提示','数据异常','error');
       					}
            		}else{
            			$.messager.alert('信息提示','导出还未完成，请稍后再试','info');
            		}

            		
            	}else{
            		$.messager.alert('信息提示','只能导出一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择需要导出的记录','info');
            }
        },
        queryRes:function(){
        	 $("#queryForm").form('reset');
        }
}

