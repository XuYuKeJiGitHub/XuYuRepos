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
        
		$("#agencyQuery").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agencyQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
        
        
        
        var height= document.body.clientHeight-80;
        // 加载表格
        $("#table").datagrid({
                title:"数据列表",
                url:'',
                loadMsg:"正在加载数据...",
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
                        	hidden:true, 
                            field:'id',
                            title:'编号',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'orgName',
                            title:'客户名称',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'address',
                            title:'充值链接',
                            width:600,
                            align:'center'
                        },
                        {
                        	field:'addressImg',
                            title:'二维码',
                            width:600,
                            align:'center',
                            formatter:function (val,row) {
                            	var url=basePath+"/xuyupayaddress/captcha.form?url="+row.address;
                            	var addressImg="<img style='width:300px; height:300px' src='"+url+"'>";
                            	return addressImg;
                            }
                        }
                ]],
                onLoadSuccess: function(data){   
                    var panel = $(this).datagrid('getPanel');   
                    var tr = panel.find('div.datagrid-body tr');   
                    tr.each(function(){   
                        var td = $(this).children('td[field="addressImg"]');   
                        td.children("div").css({   
                            "height": "300px" 
                        });   
                    });   
                }
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
                    url:basePath+'/xuyupayaddress/findList',
                    data : {
                  	    queryAgencyId:$.trim($("#agencyQuery").combobox('getValue')),
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

});


var clipboard = new Clipboard('.copyBtn', {
    text: function() {
    	var bool=obj.copy();
    	if(bool==true){
    		 return $('#copyVal').val()
    	}else{
    		 return "";
    	}
       
    }
});

clipboard.on('success', function(e) {
    console.log(e);
    alert("复制成功");
});

clipboard.on('error', function(e) {
    console.log(e);
    alert("复制失败");
});
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = basePath+'/xuyupayaddress/findList';
                  $("#table").datagrid('load',{
                	   queryAgencyId:$.trim($("#agencyQuery").combobox('getValue'))
                  })
        },
        copy:function(){
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		var address=rows[0].address;
            		$('#copyVal').val(address);
            		return true;
            	}else{
            		$.messager.alert('信息提示','只能选择一条数据','info');
            		return false;
            	}
            }else{
                $.messager.alert('信息提示','请选择要操作的记录','info');
                return false;
            }
        },
        // 提交表单
        sum:function () {
	    	$.ajax({            
                type:"POST",   //post提交方式默认是get
                url:basePath+'/xuyupayaddress/save',
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
	                		
	                		obj.find();
	                	}
                }                
             });
        },
        // 重置表单
        queryRes:function () {
                $("#queryForm").form('reset');

        }
}

