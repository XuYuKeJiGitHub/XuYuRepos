/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
		
		//运营商
		$("#providerQuery1").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text'
		});
		//地区
		$("#ownerPlace1").combobox({
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
		$("#agency1").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agency1").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
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
                        }
                        ,
                        {
                        	field:'accessNum',
                            title:'接入号',
                            width:130,
                            align:'center'
                        },
                        {
                            field:'iccid',
                            title:'长号',
                            width:130,
                            align:'center'
                        },{
                            field:'imsi',
                            title:'短号',
                            width:130,
                            align:'center'
                        }
                        ,{
                       	 field:'provider',
                         title:'运营商',
                         width:100,
                         sortable:true,
                         align:'center',
                    	 formatter:function (val,row) {
                     		var value="";
                     		var fields=$("#providerQuery1").combobox('getData');
                     		$.each(fields,function(k,v){
                     			if(v.value==val){
                     				value=v.text;
                     			}
                     		})
                             return value;
                         }
	                    }
                        ,
                        {
	                  	 field:'ownerPlace',
	                       title:'地区',
	                       sortable:true,
	                       width:100,
	                       align:'center',
	                       formatter:function (val,row) {
		                		var value="";
		                		var fields=$("#ownerPlace1").combobox('getData');
		                		$.each(fields,function(k,v){
		                			if(v.value==val){
		                				value=v.text;
		                			}
		                		})
		                        return value;
	                        }
	                    }
                        ,{
                          	 field:'agency',
                             title:'客户',
                             hidden:true,
                             width:50,
                             align:'center'
//                             formatter:function (val,row) {
//                         		var value="";
//                         		var fields=$("#agency1").combobox('getData');
//                         		$.each(fields,function(k,v){
//                         			if(v.value==val){
//                         				value=v.text;
//                         			}
//                         		})
//                                 return value;
//                             }
                        },
                        {
                         	field:'agencyName',
                            title:'客户',
                            width:80,
                            align:'center'
                       }
                        ,{
                     	 field:'workingCondition',
                          title:'通讯状态',
                          width:100,
                          align:'center'
                        },{
                    	 field:'deadLineDate',
                         title:'满期日期',
                         width:100,
                         align:'center'
                        },{
                    	 field:'waitDate',
                         title:'沉默到期日期',
                         width:100,
                         align:'center'
                        },{
                    	 field:'realWaitDate',
                         title:'实际沉默到期日期',
                         width:120,
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
                    url:bathPath+'/findgprsyes/findDeadLineList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        provider:$.trim($("#providerQuery1").combobox('getValue')),
                  	  ownerPlace:$.trim($("#ownerPlace1").combobox('getValue')),
                  	  ownerId:$.trim($("#ownerId1").combobox('getValue')),
                  	  accessNum:$.trim($("#accessNum").val()),
                  	  agency:$.trim($("#agency1").combobox('getValue')),
                  	  startDDate:$.trim($("#startDDate").datebox("getValue")),
                  	  endDDate:$.trim($("#endDDate").datebox("getValue")),
                  	  startWDate:$.trim($("#startWDate").datebox("getValue")),
                  	  endWDate:$.trim($("#endWDate").datebox("getValue")),
                  	  startRWDate:$.trim($("#startRWDate").datebox("getValue")),
                  	  endRWDate:$.trim($("#endRWDate").datebox("getValue"))
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
	             opts.url = bathPath+'/findgprsyes/findDeadLineList';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  provider:$.trim($("#providerQuery1").combobox('getValue')),
                	  ownerPlace:$.trim($("#ownerPlace1").combobox('getValue')),
                	  ownerId:$.trim($("#ownerId1").combobox('getValue')),
                	  accessNum:$.trim($("#accessNum").val()),
                	  agency:$.trim($("#agency1").combobox('getValue')),
                	  startDDate:$.trim($("#startDDate").datebox("getValue")),
                	  endDDate:$.trim($("#endDDate").datebox("getValue")),
                	  startWDate:$.trim($("#startWDate").datebox("getValue")),
                	  endWDate:$.trim($("#endWDate").datebox("getValue")),
                	  startRWDate:$.trim($("#startRWDate").datebox("getValue")),
                	  endRWDate:$.trim($("#endRWDate").datebox("getValue"))
                  })
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        doexport:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	var filename="满期日到期卡信息";
                	var exportType="deadLineExport";
                	var provider=$.trim($("#providerQuery1").combobox('getValue'));
                	var ownerPlace=$.trim($("#ownerPlace1").combobox('getValue'));
                	var ownerId=$.trim($("#ownerId1").combobox('getValue'));
                	var accessNum=$.trim($("#accessNum").val());
                	var agency=$.trim($("#agency1").combobox('getValue'));
                	var startDDate=$.trim($("#startDDate").datebox("getValue"));
                	var endDDate=$.trim($("#endDDate").datebox("getValue"));
                	var startWDate=$.trim($("#startWDate").datebox("getValue"));
                	var endWDate=$.trim($("#endWDate").datebox("getValue"));
                	var startRWDate=$.trim($("#startRWDate").datebox("getValue"));
                	var endRWDate=$.trim($("#endRWDate").datebox("getValue"));            	
                	window.open(basePath+'/csvoprate/export?filename='+filename+'&exportType='+exportType+'&provider='+provider+'&ownerPlace='
	       				+ownerPlace+'&ownerId='+ownerId+'&accessNum='+accessNum+'&agency='+agency+'&startDDate='+startDDate+'&endDDate='+endDDate
	       				+'&startWDate='+startWDate+'&endWDate='+endWDate+'&startRWDate='+startRWDate+'&endRWDate='+endRWDate);
                }
        	})
        }
}