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
	            frozenColumns:[[{checkbox:true,
                            field:'id',
                            title:'id',
                            width:50,
                            align:'center'
                        },{
                            field:'ruleId',
                            title:'ruleId',
                            hidden:true,
                            width:50,
                            align:'center'
                        },{
                            field:'ruleName',
                            title:'规则名称',
                            width:130,
                            align:'center'
                        },{
                    	 field:'ownerId',
                         title:'群组id',
                         width:130,
                         hidden:true,
                         align:'center'
                        },{
                    	 field:'ownerName',
                         title:'群组',
                         width:130,
                         align:'center'
                        },{
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
                        },{
                        	 field:'provider',
                             title:'运营商',
                             width:100,
                             align:'center'
                        },{
                      	 field:'ownerPlace',
                           title:'地区',
                           width:100,
                           align:'center'
                        },{
                     	 field:'workingCondition',
                          title:'通讯状态',
                          width:100,
                          align:'center'
                        },{
                    	 field:'isDeal',
                         title:'处理状态',
                         width:100,
                         align:'center'
                        },{
                    	 field:'operateType',
                         title:'处理方式',
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
                    url:bathPath+'/aotorule/findListLog',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        operateType:$.trim($("#operateType").combobox('getValue')),
                  	  isDeal:$.trim($("#isDeal").combobox('getValue')),
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
	             opts.url = bathPath+'/aotorule/findListLog';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  operateType:$.trim($("#operateType").combobox('getValue')),
                	  isDeal:$.trim($("#isDeal").combobox('getValue')),
                	  startDate:$.trim($("#startDate").datebox("getValue")),
                	  endDate:$.trim($("#endDate").datebox("getValue")),
                	  ruleName:$.trim($("#ruleNameQuery").val())
                  })
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        doexport:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
                if(flg){
                	var filename="自动化规则执行日志";
                	var exportType="autoRuleLogExport";
                	var ruleNameQuery=$.trim($("#ruleNameQuery").val());
                	var startDate=$.trim($("#startDate").datebox("getValue"));
                	var endDate=$.trim($("#endDate").datebox("getValue"));
                	var operateType=$.trim($("#operateType").combobox('getValue'));
                	var isDeal=$.trim($("#isDeal").combobox('getValue'));           
                 	window.open(basePath+'/csvoprate/export?filename='+filename+'&exportType='+exportType+'&ruleNameQuery='+ruleNameQuery+'&ruleNameQuery='
	       				+ruleNameQuery+'&startDate='+startDate+'&endDate='+endDate+'&operateType='+operateType+'&isDeal='+isDeal); 
	       					
                }
        	})
        }
}


