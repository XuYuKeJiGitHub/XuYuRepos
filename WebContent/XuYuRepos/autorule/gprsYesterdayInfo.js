/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
		
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
		//群组
		$("#ownerId1").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_COMBO_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		 });
		//客户
		$("#agency").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agency").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
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
		 //下拉框值加载
	    $("#comboType").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
	    
	    $("#comboName").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
//	    $("#billingStatus").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=ONLINE_STATU',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
//	    
//	    $("#workingConditionYd").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=MOBILE_CARD_STATU',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
//	    
//	    $("#workingConditionLt").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=UNICOM_CARD_STATU',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
//	    
//	    $("#workingConditionDx").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=TELECOM_CARD_STATU',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
	    
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
	            frozenColumns:[[{
	            			checkbox:true,
                            field:'id',
                            title:'id',
                            width:50,
                            align:'center'
                        },{
                    	 field:'ownerId',
                         title:'群组',
                         width:130,
                         sortable:true,
                         align:'center',
                         formatter:function (val,row) {
                      		var value="";
                      		var fields=$("#ownerId1").combobox('getData');
                      		$.each(fields,function(k,v){
                      			if(v.value==val){
                      				value=v.text;
                      			}
                      		})
                              return value;
                          }
                        },{
                            field:'accessNum',
                            title:'接入号',
                            sortable:true,
                            width:130,
                            align:'center'
                        },
                        {
                            field:'iccid',
                            title:'长号',
                            sortable:true,
                            width:130,
                            align:'center'
                        },{
                            field:'imsi',
                            title:'短号',
                            sortable:true,
                            width:130,
                            align:'center'
                        },{
                        	 field:'provider',
                             title:'运营商',
                             width:100,
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
                        },{
                      	 field:'ownerPlace',
                           title:'地区',
                           sortable:true,
                           width:100,
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
                        },{
                          	 field:'agency',
                             title:'客户',
                             hidden:true,
                             width:50,
                             align:'center'
                        },
                        {
                            field:'agencyName',
                            title:'客户名称',
                            sortable:true,
                            width:100,
                            align:'center'
                        },
                        {
                            field:'workingCondition',
                            title:'卡状态    ',
                            hidden:true,
                            width:100,
                            align:'center'
                        },
                        {
                            field:'workingConditionName',
                            title:'卡状态    ',
                            width:100,
                            sortable:true,
                            align:'center'
                        },
                        {
                            field:'comboType',
                            title:'套餐类型',
                            width:100,
                            sortable:true,
                            align:'center',
                        	formatter:function (val,row) {
                        		var comboType="";
                        		var fields=$("#comboType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				comboType=v.text;
                        			}
                        		})
                                return comboType;
                            }
                        },
                        {
                            field:'comboName',
                            title:'套餐',
                            sortable:true,
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var comnoName="";
                        		var fields=$("#comboName").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				comnoName=v.text;
                        			}
                        		})
                                return comnoName;
                            }
                        },
//                        {
//                        	field:'billingStatus',
//                            title:'在线状态    ',
//                            width:100,
//                            align:'center',
//                            formatter:function (val,row) {
//                        		var billingStatus="";
//                        		var fields=$("#billingStatus").combobox('getData');
//                        		$.each(fields,function(k,v){
//                        			if(v.value==val){
//                        				billingStatus=v.text;
//                        			}
//                        		})
//                                return billingStatus;
//                            }
//                        },
                        {
                    	 field:'useGprs',
                         title:'当月用量',
                         width:100,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'useGprsTest',
                         title:'日用流量',
                         width:100,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'queryDate',
                         title:'流量使用日期',
                         width:100,
                         sortable:true,
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
                    url:bathPath+'/findgprsyes/findGprsYesDList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        provider:$.trim($("#providerQuery").combobox('getValue')),
                  	  	ownerPlace:$.trim($("#ownerPlace").combobox('getValue')),
                  	  	ownerId:$.trim($("#ownerId1").combobox('getValue')),
                  	  	accessNum:$.trim($("#accessNum").val()),
                  	  	agency:$.trim($("#agency").combobox('getValue')),
                  	  	queryDate:$.trim($("#queryDate").datebox("getValue")),
                  	  	gprsYesterday:$.trim($("#gprsYesterday").val())
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
	             opts.url = bathPath+'/findgprsyes/findGprsYesDList';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  provider:$.trim($("#providerQuery").combobox('getValue')),
                	  ownerPlace:$.trim($("#ownerPlace").combobox('getValue')),
                	  ownerId:$.trim($("#ownerId1").combobox('getValue')),
                	  accessNum:$.trim($("#accessNum").val()),
                	  agency:$.trim($("#agency").combobox('getValue')),
                	  queryDate:$.trim($("#queryDate").datebox("getValue")),
                	  gprsYesterday:$.trim($("#gprsYesterday").val())
                  })
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        doexport:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	var filename="昨日用量信息导出";
                	var exportType="yesterdayInfoExport";
                	var provider=$.trim($("#providerQuery").combobox('getValue'));
                	var ownerPlace=$.trim($("#ownerPlace").combobox('getValue'));
                	var ownerId=$.trim($("#ownerId1").combobox('getValue'));
                	var agency=$.trim($("#agency").combobox('getValue'));  
                	var accessNum=$.trim($("#accessNum").val());
                	var queryDate=$.trim($("#queryDate").datebox("getValue"));
                	var gprsYesterday=$.trim($("#gprsYesterday").val());
                	window.open(basePath+'/csvoprate/export?filename='+filename+'&exportType='+exportType+'&provider='+provider+'&ownerPlace='
	       				+ownerPlace+'&ownerId='+ownerId+'&agency='+agency+'&accessNum='+accessNum+'&queryDate='+queryDate+'&gprsYesterday='+gprsYesterday);
                }
        	})
        }
}


