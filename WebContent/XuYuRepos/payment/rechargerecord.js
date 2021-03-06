// 加载树
$(function () {
	    // 回车事件
	    $(document).keydown(function (event) {
	       if (event.keyCode == 13) {
	         $('#btnQuery').triggerHandler('click');
	       }
	     });
	    
        $("#availableTypeQuery").combobox({
            url: basePath+'/systemlookupitem/lookUp?fLookUpId=AVAILABLE_TYPE',
            method : "post",
            valueField: 'value',
            textField: 'text',
            selected: 'selected'
         });
        
        $("#ycQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
	    });
      
        $("#comeonQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=YES_NO',
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
	    
	    var height= document.body.clientHeight-180;
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
                       	 field:'orgName',
                            title:'客户',
                            width:100,
                            align:'center'
                        },
                        {
                            field:'accessNum',
                            title:'接入号',
                            width:150,
                            align:'center'
                        },
                        {
                            field:'iccid',
                            title:'ICCID',
                            width:150,
                            align:'center'
                        },
                        {
	                    	 field:'totalGprs',
	                         title:'充值流量',
	                         width:50,
	                         align:'center',
	                         formatter:function (val,row) {
                         		return val+"MB";
	                         }
                        },
                        {
                        	field:'comboType',
                            title:'有效期',
                            width:50,
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
                        	field:'createDate',
                            title:'充值时间',
                            width:100,
                            align:'center'
                        },{
                        	field:'createUser',
                            title:'操作人账号',
                            width:50,
                            align:'center'
                        },{
                        	field:'comeon',
                            title:'加油包',
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
                        },{
                            field:'yc',
                            title:'预充值',
                            width:50,
                            align:'center',
                            formatter:function (val,row) {
                        		var yc="";
                        		var fields=$("#ycQuery").combobox('getData');
                        		$.each(fields,function(k,v){
                        			if(v.value==val){
                        				yc=v.text;
                        			}
                        		})
                                return yc;
                            }
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
                    url:basePath+'/xuyurecharge/findList',
                    data : {
                    	accessNum : $.trim($("#accessNumQuery").textbox('getValue')),
                  	    createUser: $.trim($("#createUserQuery").textbox('getValue')),
                  	    createDateStart:$.trim($("#createDateStartQuery").datebox("getValue")),
                  	    createDateEnd:$.trim($("#createDateEndQuery").datebox("getValue")),
                  	    accessNumStart:$.trim($("#accessNumStartQuery").textbox('getValue')),
                  	    accessNumEnd:$.trim($("#accessNumEndQuery").textbox('getValue')),
                  	    iccidStart:$.trim($("#iccidStartQuery").val()),
	                  	iccidEnd:$.trim($("#iccidEndQuery").val()),
                  	    availableType:$.trim($("#availableTypeQuery").combobox('getValue')),
                  	    agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
                	    yc:$.trim($("#ycQuery").combobox('getValue')),
                	    comeon:$.trim($("#comeonQuery").combobox('getValue')),
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
	             opts.url = basePath+'/xuyurecharge/findList';
                  $("#table").datagrid('load',{
                	  accessNum : $.trim($("#accessNumQuery").textbox('getValue')),
                	  createUser: $.trim($("#createUserQuery").textbox('getValue')),
                	  createDateStart:$.trim($("#createDateStartQuery").datebox("getValue")),
                	  createDateEnd:$.trim($("#createDateEndQuery").datebox("getValue")),
                	  accessNumStart:$.trim($("#accessNumStartQuery").textbox('getValue')),
                	  accessNumEnd:$.trim($("#accessNumEndQuery").textbox('getValue')),
                	  iccidStart:$.trim($("#iccidStartQuery").val()),
	                  iccidEnd:$.trim($("#iccidEndQuery").val()),
                	  availableType:$.trim($("#availableTypeQuery").combobox('getValue')),
                	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
                	  yc:$.trim($("#ycQuery").combobox('getValue')),
                	  comeon:$.trim($("#comeonQuery").combobox('getValue'))
                  })
        },
        export:function(){
        	   $.messager.confirm('提示信息','是否导出所有查询记录',function (flg) {
                if(flg){
                	    $.ajax({            
                          type:"POST",   //post提交方式默认是get
                          url:basePath+'/xuyurecharge/exporData',
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
                        	  accessNum : $.trim($("#accessNumQuery").textbox('getValue')),
                        	  createUser: $.trim($("#createUserQuery").textbox('getValue')),
                        	  createDateStart:$.trim($("#createDateStartQuery").datebox("getValue")),
                        	  createDateEnd:$.trim($("#createDateEndQuery").datebox("getValue")),
                        	  accessNumStart:$.trim($("#accessNumStartQuery").textbox('getValue')),
                        	  accessNumEnd:$.trim($("#accessNumEndQuery").textbox('getValue')),
                        	  iccidStart:$.trim($("#iccidStartQuery").val()),
      	                  	  iccidEnd:$.trim($("#iccidEndQuery").val()),
                        	  availableType:$.trim($("#availableTypeQuery").combobox('getValue')),
                        	  agencyQuery:$.trim($("#agencyQuery").combobox('getValue')),
                        	  yc:$.trim($("#ycQuery").combobox('getValue')),
                        	  comeon:$.trim($("#comeonQuery").combobox('getValue'))
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
            })
         },
        queryRes:function(){
        	 $("#queryForm").form('reset');
        }
}

