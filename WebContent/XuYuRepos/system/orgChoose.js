/**
 * Created by Administrator on 2017/11/8.
 */
// 加载树
$(function () {
		// 左边树
		$('#tt').tree({
			  //异步树 
		    url: bathPath+'/org/loadOrgTree',
//		    onClick:function(node){
//		    	 var opts = $("#table").datagrid("options");
//	             opts.url = bathPath+'/org/findList'+'?random='+Math.random();
//		    	 $("#table").datagrid('load',{
//		    		 upOrgId:node.id
//               })
//		    },
		    onDblClick: function(node){
		    	obj.submit();
		    },
		    onBeforeExpand: function(node){
		    	if(node){
		    		$('#tt').tree('options').url = bathPath+'/org/loadOrgChildrenTree?FID='+node.id+'&random='+Math.random();  
		    	}
		    } 
		});
       
       

})
obj={
	   submit:function(){
		    var node = $('#tt').tree('getSelected');
		    if(node==null||node==undefined){
		    	$.messager.alert('信息提示','请选择您需要的记录','info');
		    }else{
		    	if(node.id=='0'){
		    		$.messager.alert('信息提示','不能选择根节点','info');
		    	}else{
		    		 if(openOrgId==''){
		    			 // 选择机构
		    			 opener.document.getElementById("orgId").value=node.id;
				       	 opener.document.getElementById("orgName").value=node.text;
				       	 window.close();
		    		 }else{
		    			 // 选择部门
		    			 opener.document.getElementById("deptId").value=node.id;
				       	 opener.document.getElementById("deptName").value=node.text;
				       	 window.close();
		    		 }
		    	}
		    }
	   }
}

