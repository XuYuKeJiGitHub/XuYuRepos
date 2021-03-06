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
		//套餐
		$("#comboNameQuery").combobox({
		      url: basePath+'/systemlookupitem/lookUp?fLookUpId=OWNER_COMBO_NAME',
		      method : "post",
		      valueField: 'value',
		      textField: 'text'
		 });
		
		$("#agencyQuery").combotree({
	        url:basePath+'/org/loadOrgTree', //这是第一次加载树的url 加载根节点
	        onBeforeExpand:function(node){   //展开前获取数据
	        	 $("#agencyQuery").combotree("tree").tree("options").url=basePath+'/org/loadOrgChildrenTree?FID='+node.id; //这是点击根节点的时候发送请求去加载子节点
	        }
	    });
		
		 var height= document.body.clientHeight-110;
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
                sortName:'agency',
                checkOnSelect:false,
                remoteSort:false,
                onSortColumn: function (sort,order) {
	            }, 
                toolbar: '#tabelBut',
                columns:[[
                        {
                        	    checkbox:true,
                                field:'ff',
                                title:'编号',
                                width:50,
                                align:'center'
                        },{
                          	 field:'agency',
                             title:'客户',
                             width:100,
                             sortable:true,
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
                        }
                        ,{
                    	 field:'ownerName',
                         title:'群组名称',
                         width:130,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'comboName',
                         title:'套餐',
                         width:100,
                         sortable:true,
                         align:'center',
                         formatter:function (val,row) {
                     		var value="";
                     		var fields=$("#comboNameQuery").combobox('getData');
                     		$.each(fields,function(k,v){
                     			if(v.value==val){
                     				value=v.text;
                     			}
                     		})
                             return value;
                         }
                        },{
                    	 field:'activeCount',
                         title:'入池卡数',
                         width:100,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'useGprs',
                         title:'当月已用流量',
                         width:100,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'totalGprs',
                         title:'当月流量总量',
                         width:100,
                         sortable:true,
                         align:'center'
                        },{
                    	 field:'useGprsPercent',
                         title:'流量使用百分比',
                         width:100,
                         sortable:true,
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
                    url:bathPath+'/findgprsyes/findGprsMonthList',
                    data : {
                        pageNumber : pageNumber,
                        pageSize : pageSize,
                        provider:$.trim($("#providerQuery").combobox('getValue')),
                  	    comboName:$.trim($("#comboNameQuery").combobox('getValue')),
                  	    agency:$.trim($("#agencyQuery").combobox('getValue'))
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
	             opts.url = bathPath+'/findgprsyes/findGprsMonthList';
	             var options = $("#table").datagrid("getPager").data("pagination").options;
	             var pnum = options.pageNumber;
	          	 var psize = options.pageSize;
                  $("#table").datagrid('load',{
                	  pageNumber : pnum,
	       		      pageSize : psize,
                	  provider:$.trim($("#providerQuery").combobox('getValue')),
                	  comboName:$.trim($("#comboNameQuery").combobox('getValue')),
                	  agency:$.trim($("#agencyQuery").combobox('getValue'))
                  })
        },
        // 重置表单
        res:function () {
                $("#queryForm").form('reset');

        },
        doexport:function(){
        	$.messager.confirm('提示信息','确认是否真的导出',function (flg) {
        		if(flg){
                	var filename="流量池信息导出";
                	var exportType="ownerInfoExport";
                	var provider=$.trim($("#providerQuery").combobox('getValue'));
                	var ownerPlace=$.trim($("#ownerPlace").combobox('getValue'));
                	var comboName=$.trim($("#comboNameQuery").combobox('getValue'));
                	var agency=$.trim($("#agencyQuery").combobox('getValue')); 
                	
                	window.open(basePath+'/csvoprate/export?filename='+filename+'&exportType='+exportType+'&provider='+provider+'&ownerPlace='
	       				+ownerPlace+'&comboName='+comboName+'&agency='+agency);
                }
        	})
        }
}


