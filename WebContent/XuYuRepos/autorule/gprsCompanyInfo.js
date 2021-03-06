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
    
		//运营商
		$("#providerQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text'
		});
		//地区
		$("#ownerPlace").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
            method : "post",
            valueField: 'value',
            textField: 'text'
		});
		//套餐
		$("#comboType").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_COMBO_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		 });
		
		var height= document.body.clientHeight-205;
        // 加载表格
        $("#table").datagrid({
                cache:false,
	    	    height:height,
	            title:"数据列表",
	            loadMsg:"正在加载数据...",
	            striped:true,
	            pagination:true,
	            rownumbers:true,
	            pageNumber:1,
	            nowrap:false,
	            sortName:'id',
	            checkOnSelect:true,
	            sortOrder:'asc',
	            toolbar: '#tabelBut',
	            remoteSort:false,
	            onSortColumn: function (sort,order) {
//	            	obj.reload();
	            },      
	            frozenColumns:[[
                        {
                        	    checkbox:true,
                                field:'id',
                                title:'编号',
                                width:5,
                                align:'center'
                        }
                        ,{
                        	 field:'provider',
                             title:'运营商12',
                             width:90,
                             sortable:true,
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
                        }
                        ,{
                      	 field:'ownerPlace',
                           title:'地区',
                           width:90,
                           sortable:true,
                           align:'center',
                           formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#ownerPlace").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        }
                        ,{
                    	 field:'realComboType',
                         title:'套餐类型',
                         width:90,
                         sortable:true,
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
                        }
                        ,{
                    	 field:'cardNumTot',
                         title:'卡总数',
                         sortable:true,
                         width:80,
                         align:'center'
                        },{
                    	 field:'cardNumActive',
                         title:'激活总数',
                         width:90,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'cardNumWait',
                         title:'沉默期总数',
                         width:90,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'cardNumTest',
                         title:'测试期总数',
                         width:90,
                         align:'center'
                        },{
                    	 field:'cardNumStop',
                         title:'报停总数',
                         sortable:true,
                         width:90,
                         align:'center'
                        },{
                    	 field:'useGprs',
                         title:'当月已用流量',
                         sortable:true,
                         width:100,
                         align:'center'
                        },{
                    	 field:'useGprsActive',
                         title:'激活使用流量',
                         sortable:true,
                         width:100,
                         align:'center'
                        },{
                    	 field:'useGprsTest',
                         title:'测试期使用流量',
                         sortable:true,
                         width:100,
                         align:'center'
                        },{
                    	 field:'useGprsStop',
                         title:'报停使用流量',
                         width:100,
                         hidden:true,
                         align:'center'
                        },{
                    	 field:'totalGprs',
                         title:'当月流量总量',
                         sortable:true,
                         width:100,
                         align:'center'
                        },{
                    	 field:'useGprsPercent',
                         title:'流量使用百分比',
                         sortable:true,
                         width:100,
                         align:'center',
                         formatter:function (val,row) {
                       		var value=val+'%';
                            return value;
                           }
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
                    url:bathPath+'/findgprsyes/findGprsCompanyList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        provider:$.trim($("#providerQuery").combobox('getValue')),
                  	  ownerPlace:$.trim($("#ownerPlace").combobox('getValue')),
                  	  comboType:$.trim($("#comboType").combobox('getValue'))
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
                obj.find();
        }

})
obj={
        // 查询
        find:function () {
	        	 var opts = $("#table").datagrid("options");
	             opts.url = bathPath+'/findgprsyes/findGprsCompanyList';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  provider:$.trim($("#providerQuery").combobox('getValue')),
                	  ownerPlace:$.trim($("#ownerPlace").combobox('getValue')),
                	  comboType:$.trim($("#comboType").combobox('getValue'))
                  })
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        doexport:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	
                }
        	})
        }
}


