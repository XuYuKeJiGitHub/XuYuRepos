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
		
		$("#ownerPlaceDiv").hide();
		
		$("#comboTypeDiv").hide();
		$("#comboNameDiv").hide();
		$("#payTypeDiv").hide();
		$("#silentTypeDiv").hide();
		$("#haveTestDiv").hide();
		$("#ownerNameDiv").hide();
		
	     //下拉框值加载
        $("#operator").combobox({
          url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
          method : "post",
          valueField: 'value',
          textField: 'text',
          selected: 'selected',
          onChange: function (n,o) {
	    	  if(n=='1'){
	    		  $('#ownerPlace').combobox({required:true});
	    	      $("#ownerPlace").combobox({
	    	            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
	    	            method : "post",
	    	            valueField: 'value',
	    	            textField: 'text',
	    	            selected: 'selected'
	    	      });
	    		  $("#ownerPlaceDiv").show();
	    	  }else if(n=='2'){
	    		  $('#ownerPlace').combobox({required:true})
	    		  $("#ownerPlace").combobox({
	    	            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_LT',
	    	            method : "post",
	    	            valueField: 'value',
	    	            textField: 'text',
	    	            selected: 'selected'
	    	      });
	    		  $("#ownerPlaceDiv").show();
	    	  }else if(n=='3'){
	    		  $('#ownerPlace').combobox({required:true})
	    		  $("#ownerPlace").combobox({
	    	            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_DX',
	    	            method : "post",
	    	            valueField: 'value',
	    	            textField: 'text',
	    	            selected: 'selected'
	    	      });
	    		  $("#ownerPlaceDiv").show();
	    	  }
          }
        });
        
	    $("#yesNoOwner").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
		      onChange: function (n,o) {
		    	  if(n=='y'){
		    		  $('#accessNumStartOwnerId').validatebox('options').required = true;
		    		  $('#accessNumEndOwnerId').validatebox('options').required = true;
		    		  $('#numTypeOwnerId').combobox('options').required = true;
		    		  
		    		  $("#numTypeOwner").show();
		    		  $("#accessNumStartOwner").show();
		    		  $("#accessNumEndOwner").show();
		    		 
		    	  }else{
		    		  $('#accessNumStartOwnerId').validatebox('options').required = false;
		    		  $('#accessNumEndOwnerId').validatebox('options').required = false;
		    		  $('#numTypeOwnerId').combobox('options').required = false;
		    		  
		    		  $("#numTypeOwner").hide();
		    		  $("#accessNumStartOwner").hide();
		    		  $("#accessNumEndOwner").hide();
		    		  
		    	  }
		      }
		});
	    
	    $("#isowner").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected'
	    });
	    
	    
	    $("#yesNoRecharge").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
		      onChange: function (n,o) {
		    	  if(n=='y'){
		    		  $('#accessNumStartRechargeId').validatebox('options').required = true;
		    		  $('#accessNumEndRechargeId').validatebox('options').required = true;
		    		  $('#numTypeRechargeId').combobox('options').required = true;
		    		  
		    		  $("#numTypeRecharge").show();
		    		  $("#accessNumStartRecharge").show();
		    		  $("#accessNumEndRecharge").show();
		    	  }else{
		    		  $('#accessNumStartRechargeId').validatebox('options').required = false;
		    		  $('#accessNumEndRechargeId').validatebox('options').required = false;
		    		  $('#numTypeRechargeId').combobox('options').required = false;
		    		  
		    		  $("#numTypeRecharge").hide();
		    		  $("#accessNumStartRecharge").hide();
		    		  $("#accessNumEndRecharge").hide();
		    	  }
		      }
		});
	    
	    // 请选择
	    $("#yesNoPackages").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
	    });
		      
	    
	    
	    
	    
		$("#agencyQuery").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agencyQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
	    
	    $("#yesNoGroup").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text',
		      selected: 'selected',
		      onChange: function (n,o) {
		    	  if(n=='y'){
		    		  $('#ownerName').validatebox('options').required = true;
		    		  
		    		  $('#comboTypeOwner').combobox({required:false})
		    		  $('#comboName').combobox({required:false})
		    		  $('#payType').combobox({required:false})
		    		  
		    		  $("#comboTypeDiv").hide();
		    		  $("#comboNameDiv").hide();
		    		  $("#payTypeDiv").hide();
		    		  $("#silentTypeDiv").hide();
		    		  $("#haveTestDiv").hide();
		    		  $("#ownerNameDiv").show();
		    		  
		    	  }else{
                      $('#ownerName').validatebox('options').required = false;
		    		  
		    		  $('#comboTypeOwner').combobox({required:true})
		    		  $('#comboName').combobox({required:true})
		    		  $('#payType').combobox({required:true})
		    		  
		    		  $("#comboTypeDiv").show();
		    		  $("#comboNameDiv").show();
		    		  $("#payTypeDiv").show();
		    		  $("#silentTypeDiv").show();
		    		  $("#haveTestDiv").show();
		    		  $("#ownerNameDiv").hide();
		    	  }
		      }
		});
	    
	    
	    $("#numTypeRechargeId").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=NUM_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
	    
	    $("#numTypeOwnerId").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=NUM_TYPE',
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
	    
	    $("#billingStatusQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=ONLINE_STATU',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
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
	        		$("#ownerPlaceQuery").combobox("loadData",$("#ownerPlaceYd").combobox('getData')); 
	        	}else if(n=='2'){
	        		$("#workingConditionQuery").combobox("loadData",$("#workingConditionLt").combobox('getData')); 
	        		$("#ownerPlaceQuery").combobox("loadData",$("#ownerPlaceLt").combobox('getData')); 
	        	}else if(n=='3'){
	        		$("#workingConditionQuery").combobox("loadData",$("#workingConditionDx").combobox('getData')); 
	        		$("#ownerPlaceQuery").combobox("loadData",$("#ownerPlaceDx").combobox('getData')); 
	        	}else{
	        		$("#workingConditionQuery").combobox("loadData",'');
	        		$("#ownerPlaceQuery").combobox("loadData",''); 
	        	}
	        }
        });
        
        $("#providerDetail").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
            onChange: function (n,o) {
	        	if(n=='1'){
	        		$("#workingConditionDetail").combobox("loadData",$("#workingConditionYd").combobox('getData')); 
	        	}else if(n=='2'){
	        		$("#workingConditionDetail").combobox("loadData",$("#workingConditionLt").combobox('getData')); 
	        	}else if(n=='3'){
	        		$("#workingConditionDetail").combobox("loadData",$("#workingConditionDx").combobox('getData')); 
	        	}else{
	        		$("#workingConditionDetail").combobox("loadData",'');
	        	}
	        }
        });
        
        $("#paymentTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=PAY_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text'
        });
        
        $("#paymentTypeDetail").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=PAY_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text'
        });
        
	    $("#comboTypeQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#comboTypeDetail").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
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
	    
	    $("#comnoNameDetail").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
        
        
        $("#ownerPlace").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
//        $("#ownerPlaceQuery").combobox({
//            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
//            method : "post",
//            valueField: 'value',
//            textField: 'text',
//            selected: 'selected'
//        });
        
        $("#ownerPlaceYd").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#ownerPlaceLt").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_LT',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#ownerPlaceDx").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_DX',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#ownerPlaceDetail").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_YD',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        
        
        
        $("#realComboTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_COMBO_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#comboTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
	    $("#comboType").combobox({
	      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_COMBO_TYPE',
	      method : "post",
	      valueField: 'value',
	      textField: 'text'
	    });
	    
	    $("#comboTypeOwner").combobox({
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
	    
	    $("#payType").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=PAY_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#silentType").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=SILENT_TYPE',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
	    
	    $("#waitTypeDetail").combobox({
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
	    
	    $("#testTypeDetail").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=HAVE_TEST',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		});
        
        
        $("#waitType").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_WAIT_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#testType").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR_TEST_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#cardType").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=CARD_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
        });
        
        $("#standard").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=STANDARD',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
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
	            },      
	            frozenColumns:[[{
				            	    checkbox:true,
				                    field:'id',
				                    title:'编号',
				                    width:100,
				                    align:'center'
				            
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
	            ]],
                columns:[[
                        {
                            field:'imsi',
                            title:'imsi',
                            sortable:true,
                            width:150,
                            align:'center'
                        },
                        {
                        	 field:'paymentType',
                             title:'支付类型',
                             width:100,
                             align:'center',
                             formatter:function (val,row) {
                         		var paymentType="";
                         		var fields=$("#paymentTypeQuery").combobox('getData');
                         		$.each(fields,function(k,v){
                         			if(v.value==val){
                         				paymentType=v.text;
                         			}
                         		})
                                return paymentType;
                             }
                        },
                        {
                            field:'provider',
                            title:'运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var provider="";
                        		var fields=$("#operator").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				provider=v.text;
                        			}
                        		})
                                return provider;
                            }
                        },
                        {
                            field:'ownerPlace',
                            title:'二级运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var ownerPlace="";
                        		if(row.provider=='1'){
                        			var fields=$("#ownerPlaceYd").combobox('getData');
                            		$.each(fields,function(k,v){
                            			if(v.value==val){
                            				ownerPlace=v.text;
                            			}
                            		})
                        		}else if(row.provider=='2'){
                        			var fields=$("#ownerPlaceLt").combobox('getData');
                            		$.each(fields,function(k,v){
                            			if(v.value==val){
                            				ownerPlace=v.text;
                            			}
                            		})
                        		}else if(row.provider=='3'){
                        			var fields=$("#ownerPlaceDx").combobox('getData');
                            		$.each(fields,function(k,v){
                            			if(v.value==val){
                            				ownerPlace=v.text;
                            			}
                            		})
                        		}
                                return ownerPlace;
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
                            		workingCondition = '';
                            	}else{
                            		if(row.provider=='1'){
                            			var workingCondition="";
                                		var fields=$("#workingConditionYd").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				workingCondition=v.text;
                                			}
                                		})
                            			workingCondition = '<a  id="add" data-id="98" class=" operA"  onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+workingCondition+'</a> ';
                            		}else if(row.provider=='2'){
                            			var workingCondition="";
                                		var fields=$("#workingConditionLt").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				workingCondition=v.text;
                                			}
                                		})
                            			workingCondition = '<a  id="add" data-id="98" class=" operA" onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+workingCondition+'</a> ';
                            		}else if(row.provider=='3'){
                            			var workingCondition="";
                                		var fields=$("#workingConditionDx").combobox('getData');
                                		$.each(fields,function(k,v){
                                			if(v.value==val){
                                				workingCondition=v.text;
                                			}
                                		})
                            			workingCondition = '<a  id="add" data-id="98" class=" operA"  onclick="obj.userStatusQuery(\'' + row.accessNum + '\')">'+workingCondition+'</a> ';
                            		}
                            	}
                               
                                return workingCondition;
                            }
                        },
                        {
                            field:'comboType',
                            title:'套餐类型',
                            width:100,
                            align:'center',
                        	formatter:function (val,row) {
                        		var comboType="";
                        		var fields=$("#comboTypeQuery").combobox('getData');
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
                        		var fields=$("#comboNameQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				comnoName=v.text;
                        			}
                        		})
                                return comnoName;
                            }
                        },
                        {
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
                            		useGprs = '<a  id="add" data-id="98" class=" operA"   onclick="obj.gprsQuery(\'' + row.accessNum + '\')">0</a> ';
                            	}else{
                            		val=val.toFixed(0);
                            		useGprs = '<a  id="add" data-id="98" class=" operA"   onclick="obj.gprsQuery(\'' + row.accessNum + '\')">'+val+'M</a> ';
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
                            	if(val!=undefined){
                        			totalGprs=val+"MB";
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
                            	totalGprs="0MB";
                            	if(val!=undefined){
                            		val=val.toFixed(0);
                        			totalGprs=val+"MB";
                            	}
                            	return totalGprs;
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
                        	field:'realWaitType',
                            title:'沉默期类型(实际)',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var realWaitType="";
                        		var fields=$("#waitType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				realWaitType=v.text;
                        			}
                        		})
                                return realWaitType;
                            }
                        },
                        {
                        	field:'realTestType',
                            title:'测试期类型(实际)',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var realTestType="";
                        		var fields=$("#testType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				realTestType=v.text;
                        			}
                        		})
                                return realTestType;
                            }
                        },
                        {
                        	field:'cardType',
                            title:'卡种',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var cardType="";
                        		var fields=$("#cardType").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				cardType=v.text;
                        			}
                        		})
                                return cardType;
                            }
                        },
                        {
                        	field:'standard',
                            title:'规格',
                            width:150,
                            align:'center',
                            formatter:function (val,row) {
                        		var standard="";
                        		var fields=$("#standard").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				standard=v.text;
                        			}
                        		})
                                return standard;
                            }
                        },
                        {
                        	field:'realEstablish',
                            title:'开户日期 (实际)',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'realActivate',
                            title:'激活日期 (实际)',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'realWaitDate',
                            title:'沉默期到期时间(实际)',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'billingStatus',
                            title:'在线状态    ',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var billingStatus="";
                        		var fields=$("#billingStatusQuery").combobox('getData');
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
                        },{
                            field:'ownerName',
                            title:'群组',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'realCombotype',
                            title:'实际套餐    ',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var realCombotype="";
                        		var fields=$("#realComboTypeQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				realCombotype=v.text;
                        			}
                        		})
                                return realCombotype;
                            }
                        },
                        {
                        	field:'unitCost',
                            title:'成本单价    ',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'email14',
                            title:'当月费用    ',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'a1',
                            title:'理想化成本    ',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'a2',
                            title:'营收    ',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'email15',
                            title:'上月消费额度    ',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'email16',
                            title:'剩余额度    ',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'email17',
                            title:'实际利润    ',
                            width:200,
                            align:'center'
                        },
                        {
                        	field:'email18',
                            title:'理想化利润    ',
                            width:200,
                            align:'center'
                        },{
                        	field:'mark',
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
                    	pageNumber : pageNumber,
	                  	pageSize : pageSize,
                        keywords:$.trim($("#keywordsQuery").val()),
	                  	paymentType:$.trim($("#paymentTypeQuery").combobox('getValue')),
	                  	provider:$.trim($("#operatorQuery").combobox('getValue')),
	                  	ownerPlace:$.trim($("#ownerPlaceQuery").combobox('getValue')),
	                  	comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
	                  	comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
	                  	realComboType:$.trim($("#realComboTypeQuery").combobox('getValue')),
	                  	agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
	                  	workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
	                  	billingStatus:$.trim($("#billingStatusQuery").combobox('getValue')),
	                  	realEstablishStart:$.trim($("#realEstablishStartQuery").datebox("getValue")),
	                  	realEstablishEnd:$.trim($("#realEstablishEndQuery").datebox("getValue")),
	                  	realActivateStart:$.trim($("#realActivateStartQuery").datebox("getValue")),
	                  	realActivateEnd:$.trim($("#realActivateEndQuery").datebox("getValue")),
	                  	realWaitDateStart:$.trim($("#realWaitDateStartQuery").datebox("getValue")),
	                  	realWaitDateEnd:$.trim($("#realWaitDateEndQuery").datebox("getValue")),
	                  	accessNumStart:$.trim($("#accessNumStartQuery").val()),
	                  	accessNumEnd:$.trim($("#accessNumEndQuery").val()),
	                  	imsiStart:$.trim($("#imsiStartQuery").val()),
	                  	imsiEnd:$.trim($("#imsiEndQuery").val()),
	                  	iccidStart:$.trim($("#iccidStartQuery").val()),
	                  	iccidEnd:$.trim($("#iccidEndQuery").val()),
	                  	mark:$.trim($("#markQuery").val()),
	                  	isowner:$.trim($("#isowner").combobox('getValue')),
	                	remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
	                	remainGps:$.trim($("#remainGps").val())
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
        
        queryLoad();
})
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
	               	  paymentType:$.trim($("#paymentTypeQuery").combobox('getValue')),
	               	  provider:$.trim($("#operatorQuery").combobox('getValue')),
	               	  ownerPlace:$.trim($("#ownerPlaceQuery").combobox('getValue')),
	               	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
	               	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
	               	  realComboType:$.trim($("#realComboTypeQuery").combobox('getValue')),
	               	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
	               	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
	               	  billingStatus:$.trim($("#billingStatusQuery").combobox('getValue')),
	               	  realEstablishStart:$.trim($("#realEstablishStartQuery").datebox("getValue")),
	               	  realEstablishEnd:$.trim($("#realEstablishEndQuery").datebox("getValue")),
	               	  realActivateStart:$.trim($("#realActivateStartQuery").datebox("getValue")),
	               	  realActivateEnd:$.trim($("#realActivateEndQuery").datebox("getValue")),
	               	  realWaitDateStart:$.trim($("#realWaitDateStartQuery").datebox("getValue")),
	               	  realWaitDateEnd:$.trim($("#realWaitDateEndQuery").datebox("getValue")),
	               	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
	               	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
	               	  imsiStart:$.trim($("#imsiStartQuery").val()),
	               	  imsiEnd:$.trim($("#imsiEndQuery").val()),
	               	  iccidStart:$.trim($("#iccidStartQuery").val()),
	               	  iccidEnd:$.trim($("#iccidEndQuery").val()),
	               	  mark:$.trim($("#markQuery").val()),
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
              	  paymentType:$.trim($("#paymentTypeQuery").combobox('getValue')),
              	  provider:$.trim($("#operatorQuery").combobox('getValue')),
              	  ownerPlace:$.trim($("#ownerPlaceQuery").combobox('getValue')),
              	  comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
              	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
              	  realComboType:$.trim($("#realComboTypeQuery").combobox('getValue')),
              	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
              	  workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
              	  billingStatus:$.trim($("#billingStatusQuery").combobox('getValue')),
              	  realEstablishStart:$.trim($("#realEstablishStartQuery").datebox("getValue")),
              	  realEstablishEnd:$.trim($("#realEstablishEndQuery").datebox("getValue")),
              	  realActivateStart:$.trim($("#realActivateStartQuery").datebox("getValue")),
              	  realActivateEnd:$.trim($("#realActivateEndQuery").datebox("getValue")),
              	  realWaitDateStart:$.trim($("#realWaitDateStartQuery").datebox("getValue")),
              	  realWaitDateEnd:$.trim($("#realWaitDateEndQuery").datebox("getValue")),
              	  accessNumStart:$.trim($("#accessNumStartQuery").val()),
              	  accessNumEnd:$.trim($("#accessNumEndQuery").val()),
              	  imsiStart:$.trim($("#imsiStartQuery").val()),
              	  imsiEnd:$.trim($("#imsiEndQuery").val()),
              	  iccidStart:$.trim($("#iccidStartQuery").val()),
              	  iccidEnd:$.trim($("#iccidEndQuery").val()),
              	  mark:$.trim($("#markQuery").val()),
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
        	$("#addMarkBox").dialog({
                closed: false
            });
        	$("#addMarkForm").form('reset');
            if(rows.length>0){
            	$("#mark").textbox('setValue',rows[0].mark);
            }
            $("#accessNumsMark").textbox('setValue',ids);
        },
        // 添加
        addBox:function () {
        	var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	$("#addBox").dialog({
                        closed: false
                })
                var ids=[];
            	var accessNums="";
                for(i=0;i<rows.length;i++){
                	if((i+1)%3==0){
                		accessNums=accessNums+rows[i].accessNum+";"+"\r";
                	}else{
                		accessNums=accessNums+rows[i].accessNum+";";
                	}
                }
                $("#addForm").form('reset');
                var result1=accessNums;
                $("#accessNums").textbox('setValue',result1);
           
            }else{
                $.messager.alert('信息提示','请选择要发送短信的数据','info');
            }
        },// 充值
        recharge:function(){
            var rows=$("#table").datagrid("getSelections");
            var ids="";
            for(i=0;i<rows.length;i++){
            	if((i+1)%3==0){
            		ids=ids+rows[i].accessNum+";"+"\r";
            	}else{
            		ids=ids+rows[i].accessNum+";";
            	}
            }
       	    $("#rechargeBox").dialog({
                 closed: false
            })
            $("#addRechargeForm").form('reset');
       	    var result=ids;
            $("#accessNumsRecharge").textbox('setValue',result);
        },// 划卡
        setOwner:function () {
            var rows=$("#table").datagrid("getSelections");
            var ids="";
            for(i=0;i<rows.length;i++){
            	if((i+1)%3==0){
            		ids=ids+rows[i].accessNum+";"+"\r";
            	}else{
            		ids=ids+rows[i].accessNum+";";
            	}
            }
       	    $("#addOwnerBox").dialog({
                 closed: false
            })
            $("#addOwnerForm").form('reset');
       	    var result=ids;
            $("#accessNumsOwner").textbox('setValue',result);
        },
        // 编辑
        detail:function (id) {
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		$("#detailBox").dialog({
                        closed: false
                    });
            		var result=rows[0];
            		$('#detailForm').form('clear');
            		$("#detailForm").form('load',result);
            	}else{
            		$.messager.alert('信息提示','只能查看一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择要查看的记录','info');
            }
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
        },// 备注
        sumMark:function(){
    	    var valid=$("#addMarkForm").form('enableValidation').form('validate');
    	    if(valid){
    	    	var rows=$("#table").datagrid("getSelections");
    	    	
                var mark=$("#mark").textbox('getValue');
                var accessNums=$("#accessNumsMark").textbox('getValue');
    	    	$.ajax({            
	                  type:"POST",   //post提交方式默认是get
	                  url:basePath+'/cardmanager/setMark',
	                  data:{
	                	  accessNums:accessNums,
	                	  mark:mark
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
        },
        // 划卡
        sumOwner:function () {
        	    var valid=$("#addOwnerForm").form('enableValidation').form('validate');
        	    if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:basePath+'/xuyucontentcardmgrself/setOwner',
  	                  data:$("#addOwnerForm").serialize(),   //序列化             
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
		                        obj.canOwner()
		                        obj.reload();
	  	                	}
  	                  }            
  	               });
        	    }
        },// 充值
        sumRecharge:function(){
    	    var valid=$("#addRechargeForm").form('enableValidation').form('validate');
    	    if(valid){
    	    	// 首先检测下，是流量池的卡还是普通的卡
    	    	// 如果是普通的卡则需要选择套餐
    	    	// 如果是流量池的卡则保持现有充值逻辑
    	     	$.ajax({            
                    type:"POST",   //post提交方式默认是get
                    url:basePath+'/xuyurecharge/checkType',
                    data:$("#addRechargeForm").serialize(),   //序列化    
                    beforeSend: function () {
	                    $.messager.progress({
	                        title: '提示信息',
	                        msg: '正在处理，请稍候……',
	                        text: ''
	                    });
                    },
                    complete: function () {
                    },
	                error:function(request) {      // 设置表单提交出错                 
	                      $("#showMsg").html(request);  //登录错误提示信息
	                },
	                success:function(data) {
	                	if(data.sucess!=undefined&&data.sucess==false){
	                		$.messager.alert('信息提示',data.msg,'error');
	                		return false;
	                	}else{
	                		if(data.type=="01"){
	                			// 流量池充值方式
	                			$.ajax({            
	          	                  type:"POST",   //post提交方式默认是get
	          	                  url:basePath+'/xuyurecharge/save',
	          	                  data:$("#addRechargeForm").serialize(),   //序列化    
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
		          	                        obj.canRecharge();
		            	                	obj.reload();
	            	                	}
	          	                  }            
	          	               });
	                		}else{
	                			// 普通充值方式
	                			// 要先加载套餐
	                			// 直接加载总部套餐
	                			//$.messager.alert('信息提示','暂不支持单卡后台充值','error');
	                			// 如果支持首先要查询套餐
	                			obj.canRecharge();
	                			$("#packagesBox").dialog({
	                                closed: false
	                            });
	                		}
	                	}
	                  }            
	            });
    	    }
        },
        sumPackages:function(){
    	    var valid=$("#packagesForm").form('enableValidation').form('validate');
    	    if(valid){
    	    	// 数据为必填项
    	     	$.ajax({            
                    type:"POST",   //post提交方式默认是get
                    url:basePath+'/xuyurecharge/backOrder',
                    data:{
                    	a:JSON.stringify($("#addRechargeForm").serializeJson()),
                    	b:JSON.stringify($("#packagesForm").serializeJson())
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
  	                        obj.canPackages();
    	                	obj.reload();
	                	}
	                  }            
	            });
    	    }
        },
        // 重置表单
        res:function () {
                $("#addForm").form('reset');
        },
        // 重置表单
        queryRes:function () {
                $("#queryForm").form('reset');

        },
        // 取消表单
        can:function () {
                $("#addBox").dialog({
                        closed: true
                })
        },
        canOwner:function () {
            $("#addOwnerBox").dialog({
                    closed: true
            })
        },
        canRecharge:function () {
            $("#rechargeBox").dialog({
                closed: true
            })
        },
        canMark:function(){
        	$("#addMarkBox").dialog({
                 closed: true
            })
        },
        canDetail:function(){
        	$("#detailForm").dialog({
                closed: true
           })
        },
        can1:function () {
            $("#importBox").dialog({
                    closed: true
            })
        },
        can2:function () {
            $("#batchBox").dialog({
                    closed: true
            })
        },
        canPackages:function(){
            $("#packagesBox").dialog({
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
                                               ids.push(rows[i].accessNum);
                                       }
                                       var num=ids.length;
                                      $.ajax({
                                              type:'post',
                                              url:basePath+'/iccidmanager/delete',
                                              data:{
                                                      ids:ids.join(';')
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
                                              success:function (data) {
                                                      if(data){
                                                              $("#table").datagrid('loaded');
                                                              $("#table").datagrid('load');
                                                              $("#table").datagrid('unselectAll');
                                                              $.messager.show({
                                                                      title:'提示',
                                                                      msg:num+'张卡被删除'
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
                       $.messager.alert('信息提示','请选择要删除的记录','info');
               }
        },
        submitType:function(){
            $("#importTypeBox").dialog({
                    closed: false
            })
            $("#importTypeForm").form('reset');
        },editAll:function(){
            $("#batchBox").dialog({
                    closed: false
            })
            $("#batchForm").form('reset');
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
	                	if(data.sucess!=undefined&&data.sucess==false){
	                		$.messager.alert('信息提示',data.msg,'error');
	                	}else{
	                		$.messager.show({
                            title:'提示信息',
                            msg:"操作成功"
                            })
                            obj.reload();
	                	}
                  }            
               });
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
        import1:function(){
        	var valid=$("#importTypeForm").form('enableValidation').form('validate');

     	    if(valid){
     	    	 if($('#operator').combobox('getText')=='请选择'){
     	    		alert('运营商为必填');return false;
     	    	 }
     	    	 if($('#operator').combobox('getText')=='移动'){
     	    		 if($('#ownerPlace').combobox('getText')=='请选择'){
          	    		alert('二级运营商为必填');return false;
          	    	 }
     	    	 }
     	    	 // 关掉运营商选择界面
     	         $("#importTypeBox").dialog({
                    closed: true
                 });
     	    	 $("#importBox").dialog({
                     closed: false
                 })
                 $("#importForm").form('reset');
     	    }
           
        },
        importData:function(){
        	$.ajax({            
                  type:"POST",   //post提交方式默认是get
                  url:basePath+'/cardmanager/excelImport',
                  error:function(request) {      // 设置表单提交出错                 
                	  $.messager.show({
                          title:'提示信息',
                          msg:"操作失败"
                      })
                  },
                  success:function(data) {
	                  $.messager.show({
                          title:'提示信息',
                          msg:"操作成功"
                      })
                  }            
               });
        },
        // 提交表单
        importCsv:function () {
        	$.ajax({            
              type:"POST",   //post提交方式默认是get
                url:bathPath+'/systemannexe/upload',
                data:{uploadFile:'uploadFile'},   //序列化               
                error:function(request) {      // 设置表单提交出错                 
                    $("#showMsg").html(request);  //登录错误提示信息
                },
                success:function(data) {
	                	$.messager.show({
                        title:'提示信息',
                        msg:"操作成功"
                    })
                    obj.import1()
                    obj.find();
                }            
             });
        },
        uploadFile:function(){
        	    var attach = document.getElementById("uploadFile").value;
        	    if(attach == undefined || attach == ""){
        	        alert("请选择文件123123");
        	        return;
        	    }
        	    
        	    var operator=$("#operator").combobox('getValue');
				alert(operator);
        	    var ownerPlace=$("#ownerPlace").combobox('getValue');
        	    var comboType=$("#comboType").combobox('getValue');
        	    var waitType=$("#waitType").combobox('getValue');
        	    var testType=$("#testType").combobox('getValue');
        	    var cardType=$("#cardType").combobox('getValue');
        	    var standard=$("#standard").combobox('getValue');
        	    var unitCost=$("#unitCost").val();
        	    
        	    var asyncType="cardImport";
        	    
        	    var formData = new FormData();
        	    formData.append("file",$("#uploadFile")[0].files[0]);
        	    $.ajax({
        	    	url:basePath+'/csvoprate/upload?operator='+operator+"&ownerPlace="+ownerPlace+"&asyncType="+asyncType+"&comboType="+comboType+"&waitType="+waitType+"&testType="+testType+"&cardType="+cardType+"&standard="+standard+"&unitCost="+unitCost,
        	        type:'post',
        	        data: formData,
        	        contentType: false,
        	        processData: false,
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
        	        success:function(res){
        	        	var resp=eval('(' + res+ ')')
        	        	$.messager.show({
                          title:'提示信息',
                          msg:resp.mess
                       });
        	        	
        	           obj.can1();
        	        }
        	    })     
        },
        batchFile:function(){
    	    var attach = document.getElementById("batchFile").value;
    	    if(attach == undefined || attach == ""){
    	        alert("请选择文件");
    	        return;
    	    }
    	    var formData = new FormData();
    	    formData.append("file",$("#batchFile")[0].files[0]);
    	    $.ajax({
    	    	url:basePath+'/systemannexe/upload',
    	        type:'post',
    	        data: formData,
    	        contentType: false,
    	        processData: false,
    	        success:function(res){
    	        	var resp=eval('(' + res+ ')')
    	        	var annexeId=resp.annexeId;
    	        	// 开始解析
    	        	$.ajax({
                        type:'post',
                        url:basePath+'/iccidmanager/updateByFile',
                        data:{
                        	annexeId:annexeId
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
    	                	$.messager.show({
  	                            title:'提示信息',
  	                            msg:"操作失败"
  	                        });
    	                },
                        success:function (data) {
    	                	if(data.sucess!=undefined&&data.sucess==false){
    	                		// 下载错误的文件
    	                		var filename = data.filename;
    	       					var filepath = data.filepath;
    	       					var filenameCN = data.annexeName;
    	       					if (filename != null&& filename != ""&& filename != "undfined") {
    		                		$.messager.alert('信息提示','更新失败，请查看附件','error');
    	       						window.open(basePath+'/systemannexe/downFile?filename='+filename+'&annexeName='+encodeURIComponent(filenameCN)+'&uploadPath='+encodeURIComponent(filepath),'_self'); 
    	       					}else{
    		                		$.messager.alert('信息提示',data.msg,'error');
    	       					}
    	                	}else{
        	                	$.messager.show({
      	                            title:'提示信息',
      	                            msg:"操作成功"
      	                        });
      	                        obj.can2();
        	                	obj.reload();
    	                	}
                        }
                    })
    	           
    	        }
    	    })     
        },// 导出模板
        loadModel:function(){
        	var operator=$("#operator").combobox('getValue');
        	$.ajax({            
              type:"POST",   //post提交方式默认是get
                url:basePath+'/xuyucontentcardmgr/exportModel',
                data:{type:operator},   //序列化               
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
        },export:function(){
	       	 $.messager.confirm('提示信息','是否导出所有查询记录',function (flg) {
	             if(flg){
	            	    // 获取当前页数据量
	            	 var options = $("#table").datagrid("getPager").data("pagination").options;
	            	 if(options.total>10000){
	            		 $.messager.alert('信息提示','已在后台生成导出任务，请去数据下载中下载');
		             	 $.ajax({            
	                       type:"POST",   //post提交方式默认是get
	                       url:basePath+'/cardinfoimport/syncExportData',
	                       data:{
	                    	    keywords:$.trim($("#keywordsQuery").val()),
		   	                  	paymentType:$.trim($("#paymentTypeQuery").combobox('getValue')),
		   	                  	provider:$.trim($("#operatorQuery").combobox('getValue')),
		   	                  	ownerPlace:$.trim($("#ownerPlaceQuery").combobox('getValue')),
		   	                  	comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
		   	                  	comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
		   	                  	realComboType:$.trim($("#realComboTypeQuery").combobox('getValue')),
		   	                  	agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
		   	                  	workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
		   	                  	billingStatus:$.trim($("#billingStatusQuery").combobox('getValue')),
		   	                  	realEstablishStart:$.trim($("#realEstablishStartQuery").datebox("getValue")),
		   	                  	realEstablishEnd:$.trim($("#realEstablishEndQuery").datebox("getValue")),
		   	                  	realActivateStart:$.trim($("#realActivateStartQuery").datebox("getValue")),
		   	                  	realActivateEnd:$.trim($("#realActivateEndQuery").datebox("getValue")),
		   	                  	realWaitDateStart:$.trim($("#realWaitDateStartQuery").datebox("getValue")),
		   	                  	realWaitDateEnd:$.trim($("#realWaitDateEndQuery").datebox("getValue")),
		   	                  	accessNumStart:$.trim($("#accessNumStartQuery").val()),
		   	                  	accessNumEnd:$.trim($("#accessNumEndQuery").val()),
		   	                  	imsiStart:$.trim($("#imsiStartQuery").val()),
		   	                  	imsiEnd:$.trim($("#imsiEndQuery").val()),
		   	                  	iccidStart:$.trim($("#iccidStartQuery").val()),
		   	                  	iccidEnd:$.trim($("#iccidEndQuery").val()),
		   	                  	mark:$.trim($("#markQuery").val()),
		   	                    isowner:$.trim($("#isowner").combobox('getValue')),
		                 	    remainGpsType:$.trim($("#remainGpsType").combobox('getValue')),
		                 	    remainGps:$.trim($("#remainGps").val())
	                       }
	                    });
	            	 }else{
		             	   $.ajax({            
	                       type:"POST",   //post提交方式默认是get
	                       url:basePath+'/cardinfoimport/exportData',
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
		   	                  	paymentType:$.trim($("#paymentTypeQuery").combobox('getValue')),
		   	                  	provider:$.trim($("#operatorQuery").combobox('getValue')),
		   	                  	ownerPlace:$.trim($("#ownerPlaceQuery").combobox('getValue')),
		   	                  	comboType:$.trim($("#comboTypeQuery").combobox('getValue')),
		   	                  	comboName:$.trim($("#comboNameQuery").combobox('getValue')),  
		   	                  	realComboType:$.trim($("#realComboTypeQuery").combobox('getValue')),
		   	                  	agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
		   	                  	workingCondition:$.trim($("#workingConditionQuery").combobox('getValue')),
		   	                  	billingStatus:$.trim($("#billingStatusQuery").combobox('getValue')),
		   	                  	realEstablishStart:$.trim($("#realEstablishStartQuery").datebox("getValue")),
		   	                  	realEstablishEnd:$.trim($("#realEstablishEndQuery").datebox("getValue")),
		   	                  	realActivateStart:$.trim($("#realActivateStartQuery").datebox("getValue")),
		   	                  	realActivateEnd:$.trim($("#realActivateEndQuery").datebox("getValue")),
		   	                  	realWaitDateStart:$.trim($("#realWaitDateStartQuery").datebox("getValue")),
		   	                  	realWaitDateEnd:$.trim($("#realWaitDateEndQuery").datebox("getValue")),
		   	                  	accessNumStart:$.trim($("#accessNumStartQuery").val()),
		   	                  	accessNumEnd:$.trim($("#accessNumEndQuery").val()),
		   	                  	imsiStart:$.trim($("#imsiStartQuery").val()),
		   	                  	imsiEnd:$.trim($("#imsiEndQuery").val()),
		   	                  	iccidStart:$.trim($("#iccidStartQuery").val()),
		   	                  	iccidEnd:$.trim($("#iccidEndQuery").val()),
		   	                  	mark:$.trim($("#markQuery").val()),
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
	       						$.messager.alert('信息提示',
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

// 弹出框加载
$("#addBox").dialog({
        title:"发送短信",
        width:500,
        height:480,
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


$("#importTypeBox").dialog({
        title:"选择卡信息",
        width:600,
        height:500,
        closed: true,
        modal:true,
        shadow:true
});

$("#importBox").dialog({
        title:"批量导入",
        width:500,
        height:200,
        closed: true,
        modal:true,
        shadow:true
});


$("#addOwnerBox").dialog({
    title:"划卡",
    width:598,
    height:500,
    closed: true,
    modal:true,
    shadow:true
});

$("#rechargeBox").dialog({
    title:"充值",
    width:598,
    height:500,
    closed: true,
    modal:true,
    shadow:true
});

$("#packagesBox").dialog({
    title:"充值套餐",
    width:598,
    height:300,
    closed: true,
    modal:true,
    shadow:true
});

$("#batchBox").dialog({
    title:"批量更新",
    width:500,
    height:200,
    closed: true,
    modal:true,
    shadow:true
});

$("#detailBox").dialog({
    title:"详细信息",
    width:800,
    height:500,
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
	window.open('','newOwnerWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
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

