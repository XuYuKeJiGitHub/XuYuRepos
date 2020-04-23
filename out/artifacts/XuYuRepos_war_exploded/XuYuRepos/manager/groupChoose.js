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
	                  	payType:$("#payTypeQuery").combobox('getValue'),
	                  	ownerName:$.trim($("#ownerNameQuery").val()),
	                  	companyId:orgId
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
                	  payType:$("#payTypeQuery").combobox('getValue'),
                	  ownerName:$.trim($("#ownerNameQuery").val()),
                	  companyId:orgId
                  })
        },
        submit:function(){
	    	 var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
	           	 var id=$("#table").datagrid('getSelected').id;
	           	 var ownerName=$("#table").datagrid('getSelected').ownerName;
	           	 opener.document.getElementById("ownerId").value=id;
	           	 opener.document.getElementById("ownerName").value=ownerName;
	             var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	             var isOpera = userAgent.indexOf("Opera") > -1;
	             if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
	           		 opener.document.getElementById("ownerName").onclick();
	           	 }else{
	           	     opener.document.getElementById("ownerName").click();
	           	 }
	           	 window.close();
            }else{
           	     $.messager.alert('信息提示','请选择您需要的记录','info');
            }
	    },
       queryRes:function(){
       	 $("#queryForm").form('reset');
       },
       // 取消表单
       cancle:function () {
       	window.close();
       }
}

