/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
		$('#email').validatebox({
		    required: true,
		    validType: 'email'
		});
		
		$('#mobile').validatebox({
		    required: true,
		    validType: 'phoneNum'
		});
		

        //下拉框值加载
		$("#upOrgId").combotree({
		        value:"1",
		        url:bathPath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
		        onBeforeExpand:function(node){   //展开前获取数据
		        	 $("#upOrgId").combotree("tree").tree("options").url=bathPath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
		        }
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
	            		{checkbox:true,
                            field:'id',
                            title:'id',
                            width:50,
                            hidden:'true',
                            align:'center'
                        },
                        {
                            field:'ruleName',
                            title:'规则名称',
                            width:120,
                            align:'center'
                        },
                        {
                            field:'ruleId',
                            title:'规则Id',
                            width:120,
                            align:'center'
                        },{
                            field:'ownerId',
                            title:'群组Id',
                            width:100,
                            align:'center'
                        },{
                            field:'ownerName',
                            title:'群组名称',
                            width:120,
                            align:'center'
                        },{
                            field:'accessNums',
                            title:'接入号',
                            width:120,
                            align:'center'
                        },{
                            field:'accessNumStart',
                            title:'起始段号',
                            width:120,
                            align:'center'
                        },{
                            field:'accessNumEnd',
                            title:'结束段号',
                            width:120,
                            align:'center'
                        },{
                            field:'rate',
                            title:'百分比%',
                            width:80,
                            align:'center'
                        },{
                            field:'gprs',
                            title:'流量余量',
                            width:100,
                            align:'center'
                        },{
                            field:'days',
                            title:'提醒天数',
                            width:80,
                            align:'center'
                        },{
                            field:'isValidate',
                            title:'状态',
                            width:80,
                            align:'center',
                            formatter:function (val,row) {
                            	var value="";
                            	if('Y'==val){
                     				value="生效";
                     			}else{
                     				value="失效";
                     			}
                            	return value;
                            }
                        },
                        {
                        	 field:'operate',
                             title:'操作',
                             width:150,
                             align:'center',
                             formatter:function (val,row) {
                            	 f = '<a  id="add1" data-id="98" class="operA01"  onclick="obj.stop(\'' + row.id + '\')">暂停</a> ';
                                 e = '<a  id="add2" data-id="98" class="operA"  onclick="obj.editOne(\'' + row.id +'\',\'' + row.accessNums +'\',\'' + row.accessNumStart+'\',\''+ row.accessNumEnd+ '\')">编辑</a> ';
                                 return f+e;
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
                    url:basePath+'/aotorule/findList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        isValidate:$.trim($("#isValidate").combobox('getValue')),
                  	  startDate:$.trim($("#startDate").datebox("getValue")),
                  	  endDate:$.trim($("#endDate").datebox("getValue")),
                  	  ruleName:$.trim($("#ruleNameQuery").val())
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
	             opts.url = bathPath+'/aotorule/findList';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  isValidate:$.trim($("#isValidate").combobox('getValue')),
                	  startDate:$.trim($("#startDate").datebox("getValue")),
                	  endDate:$.trim($("#endDate").datebox("getValue")),
                	  ruleName:$.trim($("#ruleNameQuery").val())
                  })
        },
        choseUnit:function(){
//        	var choseValue=$("input[name='ruleType'][checked]").val();
        	var choseValue=$("input[name='ruleType']:checked").val();
        	if(choseValue=="02"){
        		$("#setDays").hide();
        		$("#setRate").show();
                $("#setGprs").hide();
        	}else if(choseValue=="05"||choseValue=="06"||choseValue=="07"){
        		$("#setDays").show();
        		$("#setRate").hide();
                $("#setGprs").hide();
        	}else{
        		$("#setDays").hide();
        		$("#setRate").hide();
                $("#setGprs").show();
        	}
	        	$("#days").val("");
	    		$("#gprs").val("");
	            $("#rate").val("");
        },choseType:function(){
        	var choseValue=$("input[name='bindType']:checked").val();
        	if(choseValue=="02"){
        		$("#inputCard").hide();
        		$("#intervalCard").show();
                $("#groupCard").hide();
        	}else if(choseValue=="03"){
        		$("#inputCard").hide();
        		$("#intervalCard").hide();
                $("#groupCard").show();
        	}else if(choseValue=="04"){
        		$("#inputCard").hide();
        		$("#intervalCard").hide();
                $("#groupCard").hide();
        	}else{
        		$("#inputCard").show();
        		$("#intervalCard").hide();
                $("#groupCard").hide();
        	}
        },openOwner:function(){
        	var  url = bathPath + "/XuYuRepos/manager/groupChoose.jsp";
        	var iWidth=1000; //弹出窗口的宽度;
            var iHeight=750; //弹出窗口的高度;
            var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
        	window.open('','newOwnerWin',"height="+iHeight+", width="+iWidth+", top="+iTop+", left="+iLeft+",toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");  
        	$("#openOwnerForm").submit();  
        },
        // 添加
        addBox:function () {
                $("#addBox").dialog({
                        closed: false
                })
                $("#addForm").form('reset');
               
                $("#can").hide();
                $("#res").show();
        },// 编辑
        edit:function (id) {
            var rows=$("#table").datagrid("getSelections");
            if(rows.length>0){
            	if(rows.length==1){
            		var id=rows[0].id;
            		var id=rows[0].accessNums;
            		var id=rows[0].accessNumStart;
            		var id=rows[0].accessNumEnd;
            		obj.editOne(id,accessNums,accessNumStart,accessNumEnd);
            	}else{
            		$.messager.alert('信息提示','只能编辑一条数据','info');
            	}
            }else{
                $.messager.alert('信息提示','请选择要修改的记录','info');
            }
        },//暂停
        stop:function (id) {
        	 $.ajax({
                 type:'post',
                 url:bathPath+'/aotorule/stopRule',
                 data:{
                	 id:id
                 },
                 beforesend:function () {
                         $("#table").datagrid('loading');
                 },
                 success:function (data) {
                         if(data){
                                 $("#table").datagrid('loaded');
                                 $("#table").datagrid('load');
                                 $("#table").datagrid('unselectAll');
                                 $.messager.show({
                                         title:'提示',
                                         msg:"规则暂停成功"
                                 })
                         }
                         else{
                                 $.messager.show({
                                         title:'警示信息',
                                         msg:"规则暂停失败"
                                 })
                         }
                 }
         });
        },
        // 编辑
        editOne:function (id,accessNums,accessNumStart,accessNumEnd) {
                $("#res").hide();
                $("#can").show();
                $("#addBox").dialog({
                    closed: false,
                });
                $("#id").val(id);
                $.ajax({
                	    url:bathPath+'/aotorule/editInfo',
                        type:'POST',
                        dataType:'json',
                        data: { 
                        	id:id,
                        	accessNums:accessNums,
                        	accessNumStart:accessNumStart,
                        	accessNumEnd:accessNumEnd
                        },
                        success:function (data) {
                        	$("#ruleName").val(data.model.ruleName);
                        	$(":radio[name='ruleType'][value='" + data.model.ruleType+ "']").prop("checked", "checked");
                        	var choseValue=data.model.ruleType;
                        	$("#days").val("");
              	    		$("#gprs").val("");
              	            $("#rate").val("");
                        	 if(choseValue=="02"){
                         		$("#setDays").hide();
                         		$("#setRate").show();
                                $("#setGprs").hide();
                         	}else if(choseValue=="05"||choseValue=="06"||choseValue=="07"){
                         		$("#setDays").show();
                         		$("#setRate").hide();
                                $("#setGprs").hide();
                         	}else{
                         		$("#setDays").hide();
                         		$("#setRate").hide();
                                $("#setGprs").show();
                         	}
                        	$("#days").val(data.model.days);
              	    		$("#gprs").val(data.model.gprs);
              	            $("#rate").val(data.model.rate);
              	          $(":radio[name='bindType'][value='" + data.model.bindType+ "']").prop("checked", "checked");
              	          var bindType=data.model.bindType;
	              	        $("#accessNums").val();
	          	    		$("#accessNumStartId").val();
	          	            $("#accessNumEndId").val();
	          	            $("#ownerName").val();
	        	            $("#ownerId").val();
              	          if(bindType=="01"){
              	        	$("#inputCard").show();
                     		$("#intervalCard").hide();
                            $("#groupCard").hide();
              	          }else if(bindType=="02"){
              	        	$("#inputCard").hide();
                     		$("#intervalCard").show();
                            $("#groupCard").hide();
              	          }else if(bindType=="03"){
              	        	$("#inputCard").hide();
                     		$("#intervalCard").hide();
                            $("#groupCard").show();
              	          }else{
              	        	$("#inputCard").hide();
                     		$("#intervalCard").hide();
                            $("#groupCard").hide();
              	          }
              	        $("#accessNums").textbox("setValue", data.model.accessNums);
          	    		$("#accessNumStartId").val(data.model.accessNumStart);
          	            $("#accessNumEndId").val(data.model.accessNumEnd);
          	            $("#ownerName").val(data.model.ownerName);
        	            $("#ownerId").val(data.model.ownerId);
              	        $(":radio[name='oprateType'][value='" + data.model.oprateType+ "']").prop("checked", "checked");
              	        $("#messageId").val(data.model.messageId);
              	        $("#emailId").val(data.model.emailId);
                        }
                });
        },
        // 提交表单
        sum:function(){
//        	 alert("aaaaaaaaa");
//        	    var valid=$("#addForm").form('enableValidation').form('validate');
//        	    alert(valid);
        	    //if(valid){
        	    	$.ajax({            
  	                  type:"POST",   //post提交方式默认是get
  	                  url:bathPath+'/aotorule/saveRule',
  	                  data:$("#addForm").serialize(),   //序列化               
  	                  error:function(request) {      // 设置表单提交出错                 
  	                      //$("#showMsg").html(request);  //登录错误提示信息
  	                	$.messager.show({
                            title:'提示信息',
                            msg:request
                        })
  	                  },
  	                  success:function(data) {
	  	                	$.messager.show({
	                            title:'提示信息',
	                            msg:"操作成功"
	                        })
	                        obj.can()
	                        obj.find();
  	                  }            
  	               });
        	    //}
        },
        // 重置表单
        res:function () {
                $("#addForm").form('reset');
                $("#addBox").dialog({
                    closed: true
            })
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
        // 删除多个
        del:function () {
               var rows=$("#table").datagrid("getSelections");
               if(rows.length>0){
                       $.messager.confirm('确定删除','你确定要删除你选择的记录吗？',function (flg) {
                               if(flg){
                                       var ids=[];
                                       for(i=0;i<rows.length;i++){
                                               ids.push(rows[i].id);

                                       }
                                       var num=ids.length;
                                      $.ajax({
                                              type:'post',
                                              url:bathPath+'/org/delete',
                                              data:{
                                                      ids:ids.join(';')
                                              },
                                              beforesend:function () {
                                                      $("#table").datagrid('loading');
                                                      
                                              },
                                              success:function (data) {
                                                      if(data){
                                                              $("#table").datagrid('loaded');
                                                              $("#table").datagrid('load');
                                                              $("#table").datagrid('unselectAll');
                                                              $.messager.show({
                                                                      title:'提示',
                                                                      msg:num+'个用户被删除'
                                                              })
                                                      }
                                                      else{
                                                              $.messager.show({
                                                                      title:'警示信息',
                                                                      msg:"信息删除失败"
                                                              })
                                                      }
                                              }
                                      });
                               }

                       })

               }
               else{
                       $.messager.alert('提示','请选择要删除的记录','info');
               }

        },

        //删除一个
        delOne:function (id) {
                id=$("#table").datagrid('getSelected').id;
                $.messager.confirm('提示信息','是否删除所选择记录',function (flg) {
                        if(flg){
                                $.ajax({
                                        type:'post',
                                        url:bathPath+'/org/delete',
                                        data:{
                                        	    ids:id
                                        },
                                        beforesend:function () {
                                                $("#table").datagrid('loading');

                                        },
                                        success:function (data) {
                                                if(data){
                                                        $("#table").datagrid("loaded");
                                                        $("#table").datagrid("load");
                                                        $("#table").datagrid("unselectRow");
                                                        $.messager.show({
                                                                title:'提示信息',
                                                                msg:"信息删除成功"
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
        },
        export:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	
                }
        	})
        }
}

// 弹出框加载
$("#addBox").dialog({
        title:"新增规则",
        width:800,
        height:600,
        closed: true,
        modal:true,
        shadow:true
})

