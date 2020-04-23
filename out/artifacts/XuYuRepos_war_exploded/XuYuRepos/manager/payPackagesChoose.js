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
        $("#operaQuery").combobox({
          url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
          method : "post",
          valueField: 'value',
          textField: 'text',
          selected: 'selected'
        });
        
        $("#opera").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=OPERATOR',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
          });
        
        $("#availableTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=AVAILABLE_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
         });
        
        $("#availableType").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=AVAILABLE_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
            multiple:true,
			panelHeight:'auto'
         });
        
        $("#stateQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=PACKAGES_STATE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
            multiple:true,
			panelHeight:'auto'
         });
        
        $("#discountQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=DISCOUNT',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto'
         });
        
        $("#comeonQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected',
			panelHeight:'auto'
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
		
		
		$("#agency").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agency").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
        
        
        
        var height= document.body.clientHeight-140;
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
                        	field:'orgName',
                            title:'客户名称',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'opera',
                            title:'运营商',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var provider="";
                        		var fields=$("#opera").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				provider=v.text;
                        			}
                        		})
                                return provider;
                            }
                        },
                        {
                            field:'gprs',
                            title:'流量套餐(MB)',
                            width:100,
                            align:'center'
                        },
                        {
                        	field:'comeon',
                            title:'是否加油包',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var comeon="";
                        		var fields=$("#comeonQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				comeon=v.text;
                        			}
                        		})
                                return comeon;
                            }
                        },
                        {
                        	field:'discount',
                            title:'折扣比例',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var discount="";
                        		var fields=$("#discountQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				discount=v.text;
                        			}
                        		})
                                return discount;
                            }
                        },
                        {
                        	field:'selfPrice',
                            title:'售价',
                            width:100,
                            align:'center'
                        },
                        {
                        	 field:'actualPrice',
                             title:'原价',
                             width:100,
                             align:'center'
                        },{
                        	field:'availableType',
                            title:'有效期',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var state="";
                        		var fields=$("#availableTypeQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				state=v.text;
                        			}
                        		})
                                return state;
                            }
                        },
                        {
                        	field:'state',
                            title:'上架状态',
                            width:100,
                            align:'center',
                            formatter:function (val,row) {
                        		var state="";
                        		var fields=$("#stateQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				state=v.text;
                        			}
                        		})
                                return state;
                            }
                        },
                        {
                            field:'updateTime',
                            title:'更新时间',
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
                    url:basePath+'/xuyupackages/findList',
                    data : {
                    	opera:$("#operaQuery").combobox('getValue'),
                  	    availableType:$("#availableTypeQuery").combobox('getValue'),
                  	    queryAgencyId:$.trim($("#agencyQuery").combobox('getValue')),
                  	    state:$.trim($("#stateQuery").combobox('getValue')),
                  	    comeon:$("#comeonQuery").combobox('getValue'),
                  	    gprsquery:$("#gprsquery").val(),
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
	             opts.url = basePath+'/xuyupackages/findList';
                  $("#table").datagrid('load',{
                	   opera:$("#operaQuery").combobox('getValue'),
                	   availableType:$("#availableTypeQuery").combobox('getValue'),
                	   queryAgencyId:$.trim($("#agencyQuery").combobox('getValue')),
                	   state:$.trim($("#stateQuery").combobox('getValue')),
                	   comeon:$("#comeonQuery").combobox('getValue'),
                	   gprsquery:$("#gprsquery").val()
                  })
        },
        // 重置表单
        queryRes:function () {
               $("#queryForm").form('reset');
        },
        submit:function(){
	       var rows=$("#table").datagrid("getSelections");
           if(rows.length>0){
	          	 var id=$("#table").datagrid('getSelected').id;
	          	 var payName=$("#table").datagrid('getSelected').gprs;
	          	 opener.document.getElementById("payId").value=id;
	          	 opener.document.getElementById("payName").value=payName;
	          	 window.close();
           }else{
          	 $.messager.alert('信息提示','请选择您需要的记录','info');
           }
	    },
        // 取消表单
        cancle:function () {
      	   window.close();
        }
}

