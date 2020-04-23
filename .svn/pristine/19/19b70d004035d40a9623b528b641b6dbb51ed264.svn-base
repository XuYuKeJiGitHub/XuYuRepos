/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
	    // 回车事件
	    $("#tableFind").keydown(function (event) {
	       if (event.keyCode == 13) {
	    	   obj.find();
	       }
	    });
	    
	    //下拉框值加载
	    $("#providerQuery").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
	    
	    $("#ownerPlaceQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
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
                            field:'accessNum',
                            title:'接入号',
                            width:180,
                            align:'center'
                        },
                        {
                            field:'createTime',
                            title:'发送时间',
                            width:180,
                            align:'center',
                            formatter:function (val,row) {
                            	if(val==''){
                            	}else{
                            		var now=new Date(val);
                            		var yy = now.getFullYear();      //年
                            		var MM = now.getMonth() + 1;     //月
                            		var dd = now.getDate();          //日
                            	    var hh=now.getHours();      //获取当前小时数(0-23)    
                            	    var mm=now.getMinutes();    //获取当前分钟数(0-59)    
                            	    var ss=now.getSeconds();    //获取当前秒数(0-59) 
                            		var clock = yy + "-";
                            		if(MM < 10) clock += "0";
                            		clock += MM + "-";
                            		if(dd < 10) clock += "0";
                            		clock += dd + " ";
                            		clock += hh + " ";
                            		clock += mm + " ";
                            		clock += ss + " ";
                            		return clock;
                            	}
                            }
                        },
                        {
                            field:'provider',
                            title:'运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#providerQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                            field:'ownerPlace',
                            title:'二级运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		if(row.provider=='1'){
                        			var fields=$("#ownerPlaceQuery").combobox('getData');
                            		$.each(fields,function(k,v){
                            			if(v.value==val){
                            				value=v.text;
                            			}
                            		})
                        		}
                                return value;
                            }
                        },
                        {
                            field:'message',
                            title:'短信内容',
                            width:300,
                            align:'center'
                        },
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
                    url:basePath+'/xuyumessagelog/findList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        accessNum:$("#accessNumQuery").textbox('getValue'),
                        provider:$("#providerQuery").combobox('getValue'),
                        ownerPlace:$("#ownerPlaceQuery").combobox('getValue'),
                        message:$("#message").textbox('getValue')
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
	             opts.url = basePath+'/xuyumessagelog/findList';
                  $("#table").datagrid('load',{
                	  accessNum:$("#accessNumQuery").textbox('getValue'),
                      provider:$("#providerQuery").combobox('getValue'),
                      ownerPlace:$("#ownerPlaceQuery").combobox('getValue'),
                      message:$("#message").textbox('getValue')
                  })
        },queryRes:function(){
          	 $("#queryForm").form('reset');
        }
}

