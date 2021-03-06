function init(){
	 $("#useBox").show(); 
}
// 加载树
$(function () {
	
	    // 回车事件
	    $("#tableFind").keydown(function (event) {
	       if (event.keyCode == 13) {
	    	   obj.find();
	       }
	    });
	
	    $("#operatorQuery").combobox({
	        url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
	        method : "post",
	        valueField: 'value',
	        textField: 'text',
	        selected: 'selected',
	        onChange: function (n,o) {
	        	if(n=='1'){
	        		$("#workingConditionQuery").combobox("loadData",$("#workingConditionYd").combobox('getData')); 
	        	}else if(n=='2'){
	        		$("#workingConditionQuery").combobox("loadData",$("#workingConditionLt").combobox('getData')); 
	        	}else if(n=='3'){
	        		$("#workingConditionQuery").combobox("loadData",$("#workingConditionDx").combobox('getData')); 
	        	}else{
	        		$("#workingConditionQuery").combobox("loadData",'');
	        	}
	        }
	    });
	    
	     //下拉框值加载
	    $("#comboTypeQuery").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
		
	     //下拉框值加载
	    $("#comboType").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
	    
	    $("#comboTypeQuery").combobox("loadData",$("#comboType").combobox('getData')); 
	    
	    $("#comboName").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    $("#comboNameQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    
	    $("#operator").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
            onChange: function (n,o) {}
        });
	    
	    $("#billingStatus").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=ONLINE_STATU',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    $("#workingConditionYd").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=MOBILE_CARD_STATU',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    $("#workingConditionLt").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=UNICOM_CARD_STATU',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    $("#workingConditionDx").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=TELECOM_CARD_STATU',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
		$("#agencyQuery").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agencyQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
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
		
	    $("#isowner").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected'
	    });
	    
	    $("#silentType").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=SILENT_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#haveTest").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=HAVE_TEST',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#yesNoPackages").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
	    });
	    
	    
//	    $("#useGprsTypeQuery").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=CALC_TYPE',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
//	    
//	    $("#messageCountTypeQuery").combobox({
//		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=CALC_TYPE',
//		      method : "post",
//		      valueField: 'value',
//		      textField: 'text'
//	    });
	    
	    // 充值方式
	    $("#tradeType").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=TRADE_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    var height= document.body.clientHeight-105;
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
//	            	return false;
	            },   
                columns:[[
                        {
                        	    checkbox:true,
                                field:'id',
                                title:'编号',
                                width:100,
                                align:'center'
                        },
                        {
                                field:'orgId',
                                title:'智能诊断',
                                width:100,
                                align:'center',
                                hidden:true,
                                formatter:function (val,row) {
                                    e = '<a  id="add" data-id="98" class=" operA"  onclick="obj.editOne(\'' + row.id + '\')">诊断</a> ';
                                    return e;
                                }
                        },
                        {
                            field:'accessNum',
                            title:'接入号',
                            sortable:true,
                            width:180,
                            align:'center'
                        },
                        {
                            field:'iccid',
                            title:'iccid',
                            sortable:true,
                            width:180,
                            align:'center'
                        },
                        {
                            field:'imsi',
                            title:'imsi',
                            width:150,
                            hidden:true,
                            align:'center'
                        },
                        {
                            field:'provider',
                            title:'运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var value="";
                        		var fields=$("#operator").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				value=v.text;
                        			}
                        		})
                                return value;
                            }
                        },
                        {
                          	   field:'agency',
                               title:'代理商id',
                               hidden:true,
                               width:180,
                               align:'center'
                        },
                        {
                              field:'orgName',
                              title:'客户名称',
                              width:180,
                              align:'center'
                        },
                        {
                            field:'workingCondition',
                            title:'卡状态    ',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                            	if(val==undefined){
                            		 e = '';
                            	}else{
                            		if(row.provider=='1'){  //运营商 
                            			var value="";
                                		var fields=$("#workingConditionYd").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				value=v.text;
                                			}
                                		})
                            			e = '<a  id="add" data-id="98" class="operA"  onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+value+'</a> ';
                            		}else if(row.provider=='2'){
                            			var value="";
                                		var fields=$("#workingConditionLt").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				value=v.text;
                                			}
                                		})
                            			e = '<a  id="add" data-id="98" class="operA"  onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+value+'</a> ';
                            		}else if(row.provider=='3'){
                            			var value="";
                                		var fields=$("#workingConditionDx").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				value=v.text;
                                			}
                                		})
                            			e = '<a  id="add" data-id="98" class="operA"  onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+value+'</a> ';
                            		}
                            	}
                               
                                return e;
                            }
                        },
                        {
                            field:'comboType',
                            title:'套餐类型',
                            width:100,
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
                            field:'comnoName',
                            title:'套餐',
                            width:200,
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
                        },{
                        	field:'chargeGprs',
                            title:'充值前用量 ',
                            hidden:true,
                            sortable:true,
                            width:100,
                            align:'center'
                        },                      
                        {
                        	field:'useGprs',
                            title:'当月用量 ',
                            sortable:true,
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                            	if(val==undefined){
                            		useGprs = '<a  id="add" data-id="98" class=" operA"  onclick="obj.gprsQuery(\'' + row.accessNum + '\')">0</a> ';
                            	}else{
                            		if(row.chargeGprs!=undefined){
                            			val=(val-row.chargeGprs).toFixed(0);
                            		}else{
                            			val=val.toFixed(0);
                            		}
                            		useGprs = '<a  id="add" data-id="98" class=" operA"  onclick="obj.gprsQuery(\'' + row.accessNum + '\')">'+val+'M</a> ';
                            	}
                                
                                return useGprs;
                            }
                        },
                        {
                        	field:'totalGprs',
                            title:'套餐总量',
                            sortable:true,
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                            	totalGprs="0MB";
                            	var ownerName=row.ownerName;
                            	if(ownerName==undefined||ownerName==''){
                            		if(val!=undefined){
                            			totalGprs=val+"MB";
                            		}
                            	}else{
                            		if(row.comnoName!=undefined){
                            			if(val!=undefined){
                                			totalGprs=row.comnoName+"MB";
                            			}
                                	}
                            	}
                            	return totalGprs;
                            }
                        },
                        {
                        	field:'remainGps',
                            title:'余额    ',
                            sortable:true,
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                            	remainGps="0MB";
                            	if(val!=undefined){
                            		val=val.toFixed(0);
                            		remainGps=val+"MB";
                            	}
                            	return remainGps;
                            }
                        },
                        {
                        	field:'messageCount',
                            title:'已发短信    ',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                            	if(val==undefined){
                            		messageCount = '<a  id="add" data-id="98" class=" operA"  onclick="">0条</a> ';
                           		    return messageCount;
                            	}else{
                            		 messageCount = '<a  id="add" data-id="98" class=" operA"  onclick="">'+val+'条</a> ';
                            		 return messageCount;
                            	}
                            }
                        },
                        {
                        	field:'waitType',
                            title:'沉默期类型',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var waitType="";
                        		var fields=$("#silentType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				waitType=v.text;
                        			}
                        		})
                                return waitType;
                            }
                        },
                        {
                        	field:'testType',
                            title:'测试期类型',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var testType="";
                        		var fields=$("#haveTest").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				testType=v.text;
                        			}
                        		})
                                return testType;
                            }
                        },
                        {
                        	field:'deadlineDate',
                            title:'到期日期    ',
                            sortable:true,
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'establishDate',
                            title:'开户日期    ',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'activateDate',
                            title:'激活日期    ',
                            width:100,
                            align:'center'
                        },
     
                        {
                        	field:'waitDate',
                            title:'沉默期到期时间    ',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'billingStatus',
                            title:'在线状态    ',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var billingStatus="";
                        		var fields=$("#billingStatus").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				billingStatus=v.text;
                        			}
                        		})
                        		if(billingStatus!=""){
                        			billingStatus = '<a  id="add" data-id="98" class=" operA"  onclick="obj.changeCardState(\'' + row.accessNum + '\')">'+billingStatus+'</a> ';
                        		}
                                return billingStatus;
                            }
                        },
                        {
                        	field:'ownerName',
                            title:'群组    ',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'markFirst',
                            title:'备注    ',
                            width:200,
                            align:'center'
                        }
                        
                ]]
        })
        
        var p = $('#table').datagrid('getPager');
        $(p).pagination({
            pageSize: 10,//每页显示的记录条数，默认为20
            pageList: [10,20,50,100,200,300,500,1000,2000],//可以设置每页记录条数的列表
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
                    url:basePath+'/iccidmanager/findList',
                    data : {
                    	  keywords:$.trim($("#keywordsQuery").val()),
                    	  owner:$.trim($("#ownerId").val()),
	                  	  provider:$.trim($("#operatorQuery").combobox('getValue')),
	                  	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
	                  	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
	                  	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
	                  	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
	                  	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
	                  	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
	                  	  iccidStart:$.trim($("#iccidStartQuery").val()),
	                  	  iccidEnd:$.trim($("#iccidEndQuery").val()),
	                  	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
	                  	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
	                  	  activateDateStart:$.trim($("#activateDateStartQuery").datebox("getValue")),
	                  	  activateDateEnd:$.trim($("#activateDateEndQuery").datebox("getValue")),
	                  	  deadlineDateStart:$.trim($("#deadlineDateStartQuery").datebox("getValue")),
	                  	  deadlineDateEnd:$.trim($("#deadlineDateEndQuery").datebox("getValue")),
	                  	  waitDateStart:$.trim($("#waitDateStartQuery").datebox("getValue")),
	                  	  waitDateEnd:$.trim($("#waitDateEndQuery").datebox("getValue")),
	                  	  markFirst:$.trim($("#markQuery").val()),
	                  	  isowner:$.trim($("#isowner").combobox('getValue')),
	                	  remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
	                	  remainGps:$.trim($("#remainGps").val()),
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
        
        // 加载默认查询条件
        queryLoad=function(){
             $("#a4").hide();
             $("#a5").hide();
             $("#a6").hide();

             $("#a1").show();
             $("#a2").show();
             $("#a3").show();
             
             obj.loadFirst();
        }
        
        $('#ownerNameQuery').textbox({
            icons: [{
                iconCls:'icon-add',
            handler: function(e){
            	openOwner();
            }
            }]
        });
        
        queryLoad();
       
        
        
        
        function collapse(){
        	$("#queryMore").show();
        	$("#a1").hide();
        	$("#a2").hide();
        	$("#a3").hide();
        	
        	$("#a4").show();
        	$("#a5").show();
        	$("#a6").show();
        	
        	$('#tableFind').attr("style","height:280px;");
        	var vCenterHeight=document.body.clientHeight-295;
        	$("#table").datagrid({
                height: vCenterHeight+"px"
            });
        }
        function expand(){
        	$("#queryMore").hide();
        	$("#a4").hide();
        	$("#a5").hide();
        	$("#a6").hide();
        	
        	$("#a1").show();
        	$("#a2").show();
        	$("#a3").show();
        	
        	$('#tableFind').attr("style","height:65px;");
        	var vCenterHeight=document.body.clientHeight-90;
        	$("#table").datagrid({
                height: vCenterHeight+"px"
            });
        }

});
obj={
        // 查询
		loadFirst:function(){
			var opts = $("#table").datagrid("options");
   		    opts.url = basePath+'/iccidmanager/findList';
   		    var options = $("#table").datagrid("getPager").data("pagination").options;
       		$("#table").datagrid('load',{
	       		  pageNumber : 1,
	       		  pageSize : 10,
            	  keywords:$.trim($("#keywordsQuery").val()),
            	  owner:$.trim($("#ownerId").val()),
            	  provider:$.trim($("#operatorQuery").combobox('getValue')),
            	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
            	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
            	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
            	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
            	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
            	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
            	  iccidStart:$.trim($("#iccidStartQuery").val()),
            	  iccidEnd:$.trim($("#iccidEndQuery").val()),
            	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
            	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
            	  activateDateStart:$.trim($("#activateDateStartQuery").datebox("getValue")),
            	  activateDateEnd:$.trim($("#activateDateEndQuery").datebox("getValue")),
            	  deadlineDateStart:$.trim($("#deadlineDateStartQuery").datebox("getValue")),
            	  deadlineDateEnd:$.trim($("#deadlineDateEndQuery").datebox("getValue")),
            	  waitDateStart:$.trim($("#waitDateStartQuery").datebox("getValue")),
            	  waitDateEnd:$.trim($("#waitDateEndQuery").datebox("getValue")),
            	  markFirst:$.trim($("#markQuery").val()),
            	  isowner:$.trim($("#isowner").combobox('getValue')),
                  remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
                  remainGps:$.trim($("#remainGps").val())
           });
		},
        find:function () {
        	obj.reload();
        },// 重新加载
        reload:function(){
        	var options = $("#table").datagrid("getPager").data("pagination").options;
         	var pnum = options.pageNumber;
         	var psize = options.pageSize;
         	$("#table").datagrid("load",{
         		  keywords:$.trim($("#keywordsQuery").val()),
         		  owner:$.trim($("#ownerId").val()),
              	  provider:$.trim($("#operatorQuery").combobox('getValue')),
              	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
              	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
              	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
              	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
              	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
              	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
              	  iccidStart:$.trim($("#iccidStartQuery").val()),
              	  iccidEnd:$.trim($("#iccidEndQuery").val()),
              	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
              	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
              	  activateDateStart:$.trim($("#activateDateStartQuery").datebox("getValue")),
              	  activateDateEnd:$.trim($("#activateDateEndQuery").datebox("getValue")),
              	  deadlineDateStart:$.trim($("#deadlineDateStartQuery").datebox("getValue")),
              	  deadlineDateEnd:$.trim($("#deadlineDateEndQuery").datebox("getValue")),
              	  waitDateStart:$.trim($("#waitDateStartQuery").datebox("getValue")),
              	  waitDateEnd:$.trim($("#waitDateEndQuery").datebox("getValue")),
              	  markFirst:$.trim($("#markQuery").val()),
              	  isowner:$.trim($("#isowner").combobox('getValue')),
            	  remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
            	  remainGps:$.trim($("#remainGps").val()),
              	  pageNumber:pnum,
              	  pageSize:psize
         	});
        },// 添加备注
        addMark:function(){
        	 var rows=$("#table").datagrid("getSelections");
        	 var ids="";
             for(i=0;i<rows.length;i++){
             	if((i+1)%3==0){
             		ids=ids+rows[i].accessNum+";"+"\r";
             	}else{
             		ids=ids+rows[i].accessNum+";";
             	}
            }
            if(rows.length>0){
            	$("#mark").textbox('setValue',rows[0].markFirst);
            }
            $("#addMarkBox").dialog({
                closed: false
            });
        	$("#addMarkForm").form('reset');
            $("#accessNumsMark").textbox('setValue',ids);
        },
        addPay:function(){
        	var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
                	// 判断数据是否为流量池的卡
                	if(rows[0].ownerName==undefined||rows[0].ownerName==''){
                		$("#addPayBox").dialog({
                            closed: false
                        });
                    	$("#addPayForm").form('reset');
                    	$("#accessNumPay").textbox('setValue',rows[0].accessNum);
                	}else{
                		$.messager.alert('信息提示','流量池的卡不支持自助充值','info');
                	}
            	}else{
            		$.messager.alert('信息提示','只能充值一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择要充值的数据','info');
            }
        },
        // 添加
        addBox:function () {
	        	var rows=$("#table").datagrid("getSelections");
	            if(rows.length>0){
	            	 $("#addBox").dialog({
	                        closed: false
	                })
	                var ids=[];
	            	var accessNums=[];
	                for(i=0;i<rows.length;i++){
	         	    	accessNums.push(rows[i].accessNum);
	                }
	                $("#addForm").form('reset');
	                var result1=accessNums.join(';');
	                $("#accessNums").textbox('setValue',result1);
	           
	            }else{
	                $.messager.alert('信息提示','请选择要发送短信的数据','info');
	            }
               
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
                	    url:basePath+'/org/find',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id
                        },
                        success:function (data) {
                        	$("#id").val(data.model.id);
                        	$("#orgId").val(data.model.orgId);
                        	$("#orgName").val(data.model.orgName);
                        	$("#upOrgId").combobox('setValue', data.model.upOrgId);
                        }
                })
        },
        userStatusQuery:function(accessNum){
        	// 用户实时状态查询
	    	$.ajax({            
                  type:"POST",   //post提交方式默认是get
                  url:basePath+'/iccidmanager/userStatusQuery',
                  data: { 
                	  accessNums:accessNum
                  },
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
                	 if(data=="-2"){
                		$.messager.show({
                            title:'提示信息',
                            msg:"此卡号暂不支持状态刷新操作，详情请联系管理员"
                		})
                	}else{
                		$("#table").datagrid('reload');
                		$.messager.show({
                            title:'提示信息',
                            msg:"卡状态刷新成功"
                		})
                	}
                  }            
               });
        },// 备注
        sumMark:function(){
    	    var valid=$("#addMarkForm").form('enableValidation').form('validate');
    	    if(valid){
                var mark=$("#mark").textbox('getValue');
                var accessNums=$("#accessNumsMark").textbox('getValue');
    	    	$.ajax({            
	                  type:"POST",   //post提交方式默认是get
	                  url:basePath+'/iccidmanager/setMark',
	                  data:{
	                	  accessNums:accessNums,
	                	  markFirst:mark
	                  },         
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
	                        obj.canMark()
	                        obj.reload();
  	                	}
	                  }            
	               });
    	    }
        },sumPay:function(){
    	    var valid=$("#addPayForm").form('enableValidation').form('validate');
    	    if(valid){
    	    	// 首先要获取当前卡号的金额信息
    	    	var accessNumPay=$.trim($("#accessNumPay").textbox('getValue'))
    	        var tradeType=$.trim($("#tradeType").combobox('getValue'));
    	    	var yc=$.trim($("#yesNoPackages").combobox('getValue'));
    	    	var payId=$.trim($("#payId").val());
    	    	
    	    	
    	    	if(tradeType=="WX"){
    	    		// 微信支付
    	    		// 生成订单
        			$.ajax({            
        		         type:"POST",   //post提交方式默认是get
        		         data : {
        		        	 accessNums : accessNumPay,
        		        	 yc:yc,
        		        	 payId:payId
        		         },
        		         url:basePath+'/xuyurecharge/createOrder',
        		         success:function(data){
        		        	 if(data.sucess!=undefined&&data.sucess==false){
       	                		$.messager.alert('信息提示',data.msg,'error');
       	                	 }else{
        		        		 // 直接扫码支付
        		        		 $.ajax({            
	                		         type:"POST",   //post提交方式默认是get
	                		         data : {
	                		        	 totalFee :data.order.price,
	                		        	 tradeType:'NATIVE',
	                		        	 out_trade_no:data.order.id
	                		         },
	                		         url:basePath+'/wxpay/pay',
	                		         success:function(data){
	                		        	 if(data.sucess!=undefined&&data.sucess==false){
	                		        		$.messager.alert('信息提示',data.msg,'error');
	                		        	 }else{
                		        		    var  url = "../../wxpay/paynative.html?code_url="+data.code_url;
	                		        		var iWidth=500; //弹出窗口的宽度;
	                		        	    var iHeight=500; //弹出窗口的高度;
	                		        	    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
	                		        	    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	                		        		window.open(url,'',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	                		        	 }
	                		         }
        		        	    });
       	                	 }
        				 },
        				 error:function(rep){
        					 $.messager.alert('信息提示','订单生成错误，请稍候再试','info');
        				 }
        		    });
    	    	}else if(tradeType=="ZH"){
    	    		
    	    	}
    	    }
        },
        gprsQuery:function(accessNum){
        	// 用户实时状态查询
	    	$.ajax({            
                  type:"POST",   //post提交方式默认是get
                  url:basePath+'/iccidmanager/gprsQuery',
                  data: { 
                	  accessNums:accessNum
                  },
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
	                		obj.reload();
	                	}
                  }            
               });
        },
        changeCardState:function(accessNum){
        	// 用户实时状态查询
	    	$.ajax({            
                  type:"POST",   //post提交方式默认是get
                  url:basePath+'/iccidmanager/changeCardState',
                  data: { 
                	  accessNums:accessNum
                  },
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
	                	}
                  }            
               });
        },
        // 提交表单
        sum:function () {
        	    var valid=$("#addForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:basePath+'/iccidmanager/messageSend',
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
		                        obj.reload();
	  	                	}
  	                  }            
  	               });
        	    }
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        canMark:function(){
        	$("#addMarkBox").dialog({
                 closed: true
            })
        },
        // 取消表单
        can:function () {
            $("#addBox").dialog({
                    closed: true
            })
        },
        canPay:function(){
            $("#addPayBox").dialog({
                    closed: true
            })
        },
        // 停复机
        del:function (bool) {
               var rows=$("#table").datagrid("getSelections");
               if(rows.length>0){
                       $.messager.confirm('信息提示','你确定要停复机吗？',function (flg) {
                               if(flg){
                                       var ids=[];
                                       for(i=0;i<rows.length;i++){
                                               ids.push(rows[i].accessNum);

                                       }
                                       var num=ids.length;
                                      $.ajax({
                                              type:'post',
                                              url:basePath+'/iccidmanager/changeCardStateAll',
                                              data:{
                                            	  accessNums:ids.join(';'),
                                            	  bool:bool
                                              },
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
                                              beforesend:function () {
                                                      $("#table").datagrid('loading');
                                                      
                                              },
                                              success:function (data) {
                                                      if(data=='0'){
                                                              $("#table").datagrid('loaded');
                                                              $("#table").datagrid('load');
                                                              $("#table").datagrid('unselectAll');
                                                              $.messager.show({
                                                                      title:'提示',
                                                                      msg:num+'个用户被停复机'
                                                              })
                                                      }else if(data=='1'){
                                                              $.messager.show({
                                                                      title:'警示信息',
                                                                      msg:"停复机失败"
                                                              })
                                                      }else if(data=='3'){
                                                          $.messager.show({
                                                              title:'警示信息',
                                                              msg:"余额不足 不能复机"
                                                          })
                                                      }else if(data=='-2'){
                                                          $.messager.show({
                                                              title:'警示信息',
                                                              msg:"此卡号暂不支持停复机操作，详情请联系管理员"
                                                          })
                                                      }else {//-1
                                                          $.messager.show({
                                                              title:'警示信息',
                                                              msg:"请十分钟之后再对此卡号进行停复机操作"
                                                          })
                                                     }  
                                              }
                                      })
                               }

                       })

               }
               else{
                       $.messager.alert('信息提示','请选择要停复机的记录','info');
               }

        },
        collapse:function(){
        	$("#queryMore").show();
        	$("#a1").hide();
        	$("#a2").hide();
        	$("#a3").hide();
        	
        	$("#a4").show();
        	$("#a5").show();
        	$("#a6").show();
        	
        	$('#tableFind').attr("style","height:280px;");
        	var vCenterHeight=document.body.clientHeight-290;
        	$("#table").datagrid({
                height: vCenterHeight+"px"
            });
        },
        expand:function(){
        	$("#queryMore").hide();
        	$("#a4").hide();
        	$("#a5").hide();
        	$("#a6").hide();
        	
        	$("#a1").show();
        	$("#a2").show();
        	$("#a3").show();
        	
        	$('#tableFind').attr("style","height:95px;");
        	var vCenterHeight=document.body.clientHeight-105;
        	$("#table").datagrid({
                height: vCenterHeight+"px"
            });
        },
        export:function(){
        	 $.messager.confirm('提示信息','是否导出所有查询记录',function (flg) {
                if(flg){
                	
                    // 获取当前页数据量
	            	 var options = $("#table").datagrid("getPager").data("pagination").options;
	            	 if(options.total>10000){
	            		 $.messager.alert('信息提示','已在后台生成导出任务，请去数据下载中下载');
		             	 $.ajax({            
	                       type:"POST",   //post提交方式默认是get
	                       url:basePath+'/iccidmanager/syncExportData',
	                       data:{
	                    	  keywords:$.trim($("#keywordsQuery").val()),
	                  		  owner:$.trim($("#ownerId").val()),
	                       	  provider:$.trim($("#operatorQuery").combobox('getValue')),
	                       	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
	                       	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
	                       	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
	                       	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
	                       	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
	                       	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
	                       	  iccidStart:$.trim($("#iccidStartQuery").val()),
	                       	  iccidEnd:$.trim($("#iccidEndQuery").val()),
	                       	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
	                       	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
	                       	  activateDateStart:$.trim($("#activateDateStartQuery").datebox("getValue")),
	                       	  activateDateEnd:$.trim($("#activateDateEndQuery").datebox("getValue")),
	                       	  deadlineDateStart:$.trim($("#deadlineDateStartQuery").datebox("getValue")),
	                       	  deadlineDateEnd:$.trim($("#deadlineDateEndQuery").datebox("getValue")),
	                       	  waitDateStart:$.trim($("#waitDateStartQuery").datebox("getValue")),
	                       	  waitDateEnd:$.trim($("#waitDateEndQuery").datebox("getValue")),
	                       	  markFirst:$.trim($("#markQuery").val()),
	                      	  isowner:$.trim($("#isowner").combobox('getValue')),
		                	  remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
		                	  remainGps:$.trim($("#remainGps").val())
	                       }
	                    });
	            	 }else{
	              	    $.ajax({            
	                          type:"POST",   //post提交方式默认是get
	                          url:basePath+'/iccidmanager/exportData',
	                          beforeSend: function () {
	                              $.messager.progress({
	                                  title: '提示信息',
	                                  msg: '系统正在导出数据，请稍候……',
	                                  text: ''
	                              });
	                          },
	                          complete: function () {
	                              $.messager.progress('close');
	                          },
	                          data:{
	                        	  keywords:$.trim($("#keywordsQuery").val()),
		                  		  owner:$.trim($("#ownerId").val()),
		                       	  provider:$.trim($("#operatorQuery").combobox('getValue')),
		                       	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
		                       	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
		                       	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
		                       	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
		                       	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
		                       	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
		                       	  iccidStart:$.trim($("#iccidStartQuery").val()),
		                       	  iccidEnd:$.trim($("#iccidEndQuery").val()),
		                       	  establishDateStart:$.trim($("#establishDateStartQuery").datebox("getValue")),
		                       	  establishDateEnd:$.trim($("#establishDateEndQuery").datebox("getValue")),
		                       	  activateDateStart:$.trim($("#activateDateStartQuery").datebox("getValue")),
		                       	  activateDateEnd:$.trim($("#activateDateEndQuery").datebox("getValue")),
		                       	  deadlineDateStart:$.trim($("#deadlineDateStartQuery").datebox("getValue")),
		                       	  deadlineDateEnd:$.trim($("#deadlineDateEndQuery").datebox("getValue")),
		                       	  waitDateStart:$.trim($("#waitDateStartQuery").datebox("getValue")),
		                       	  waitDateEnd:$.trim($("#waitDateEndQuery").datebox("getValue")),
		                       	  markFirst:$.trim($("#markQuery").val()),
		                       	  isowner:$.trim($("#isowner").combobox('getValue')),
		                	      remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
		                	      remainGps:$.trim($("#remainGps").val())
	                          },
	                          error:function(request) {
	                          	$.messager.show({
	                                  title:'提示信息',
	                                  msg:"操作失败请稍候再试"
	                              });
	                          },
	                          success:function(data) {
	          					var filename = data.filename;
	          					var filepath = data.filepath;
	          					var filenameCN = data.annexeName;
	          					if (filename != null&& filename != ""&& filename != "undfined") {
	          						window.open(basePath+'/systemannexe/downFile?filename='+filename+'&annexeName='+encodeURIComponent(filenameCN)+'&uploadPath='+encodeURIComponent(filepath),'_self'); 
	          					} else {
	          						Ext.MessageBox.alert('信息提示',
	          								'文件导出失败，请稍后重试！');
	          						return false;
	          					}
	                          }            
	                       });
	            	 }
                }
            });
        	
        }
}

//弹出框加载
$("#addBox").dialog({
        title:"发送短信",
        width:450,
        height:500,
        closed: true,
        modal:true,
        shadow:true
});

$("#addMarkBox").dialog({
    title:"备注",
    width:500,
    height:500,
    closed: true,
    modal:true,
    shadow:true
});

$("#addPayBox").dialog({
    title:"充值",
    width:598,
    height:420,
    closed: true,
    modal:true,
    shadow:true
});

function openOwner(){
	var  url = basePath + "/XuYuRepos/manager/groupChoose.jsp";
	var iWidth=1000; //弹出窗口的宽度;
    var iHeight=750; //弹出窗口的高度;
    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
    var winObj=window.open('','newOwnerWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	$("#openOwnerForm").submit();  
};

function openMesage(){
	var  url = basePath + "/XuYuRepos/manager/message.jsp";
	var iWidth=1000; //弹出窗口的宽度;
    var iHeight=750; //弹出窗口的高度;
    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	window.open('','newMessageWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	$("#openMesageForm").submit();  
};

function openPackages(){
	var  url = basePath + "/XuYuRepos/manager/payPackagesChoose.jsp";
	var iWidth=1000; //弹出窗口的宽度;
    var iHeight=750; //弹出窗口的高度;
    var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	window.open('','newPackagesWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
	$("#openPackagesForm").submit();  
};


function fncallback(){
	$("#ownerNameQuery").textbox("setValue",$("#ownerName").val());
};







