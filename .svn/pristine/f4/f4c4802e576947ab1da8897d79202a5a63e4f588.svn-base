<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"/>
		<meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0"> 
		<meta content="yes" name="apple-mobile-web-app-capable"/>
		<meta content="black" name="apple-mobile-web-app-status-bar-style"/>
		<meta content="telephone=no" name="format-detection"/>
		<link rel="stylesheet" type="text/css" href="css/loading.css">
		<link rel="stylesheet" type="text/css" href="css/layer.css">
<!-- 		<script type="text/javascript" src="js/juery-2.1.1.min.js"></script> -->
<!-- 		<script type="text/javascript" src="js/layer.js"></script> -->
<!-- 		<script type="text/javascript" src="js/wx_js_pay.js"></script> -->
</head>
	<body>
		<div class="loading bar">
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
		</div>
		<div align="certer" style="font-size:0.8rem">授权中</div>
		<form id="payForm" method="post" action="../cashier/confirmPay">
			<!-- 订单号 -->
			<input type="hidden" id="orderNo" name="orderNo" value="${orderNo}" />
			<input type="hidden" id="openid" name="openid" value="${openid}" />
			<input type="hidden" id="cancelUrl" name="cancelUrl" value="${cancelUrl}" />
			<input type="hidden" id="uniqueMark" name="uniqueMark" />
			<input type="hidden" id="isDirect" name=""isDirect"" value="Y" />
			<input type="hidden" id="isCombined" name="isCombined" value="${isCombined} }" />
			<input type="hidden" id="wxBrowser" name="wxBrowser" value="Y" />
			<input type="hidden" id="refreshPayid" name="refreshPayid" value="Y" />
			<input type="hidden" id="payType" name="payType" value="Y" />
		</form>
		<script type="text/javascript">
			function getUrlParam(name){
				var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
				var r=window.location.search.substr(1).match(reg);
				if(r!=null){
					return decodeURLComponent(r[2]);
				}
				return null;
			}
			$(function init(){
				var isLoad=sessionStorage.getItem('loadPay');
				//已加载
				if(isLoad==true){
					console.log("重复加载!");
					setTimeout('jsapiPay()',1000);
				}else{
					//未加载
					sessionStorage.getItem('loadPay',true);
					jsapiPay()//调用微信SDK方法
				}
			});
			function recoverBtn(){}
			
			//调用微信SDK方法
			function jsapiPay(){
				$.ajax({
					async:false,
					url:"../sxcashier/jsapiPay",
					data:$("#payForm").serialize(),
					dataType:"json",
					cache:false,
					method:"POST",
					sucess:function(data){
						if(data==null){
							toErrorPage("当前网络不好，请稍后重试",null);
							return;
						}
						if(data.return_code=="FAIL"){
							toErrorPage(data.return_msg,null);
							return;
						}
						appid=data.appid;//公众号名称,商户传入
						timeStamp=data.timeStamp;//时间戳,自1970年以来的秒
						nonceStr=data.nonceStr;//随机窜
						vpackage=data.vpackage;
						signType=data.signType;//微信签名方式
						paySign=data.paySign;
						returnUrl=decodeURL(data.returnUrl);
						returnFailUrl=decodeURL(data.returnFailUrl);
						returnCancelUrl=decodeURL(data.returnCancelUrl);
						$('#mid').val(data.mid);
						if(vpackage!=null&&vpackage!=""){
							if(typeof WeixinJSBrige=="undefined"){
								//异常情况处理 再补充
							}else{
								onBridgeReady();
							}
						}
					},
					error:function(){
						toErrorPage("网络异常或者支付接口异常，请稍后重试",null);
					}
				});
				
			}
			function onBridgeReady(){
				 WeixinJSBridge.invoke(
				            'getBrandWCPayRequest', {
				                "appId": appId,     //公众号名称，由商户传入
				                "timeStamp": timeStamp,         //时间戳，自1970年以来的秒数
				                "nonceStr": nonceStr, //随机串
				                "package": vpackage,
				                "signType": signType,//"MD5",         //微信签名方式：
				                "paySign": paySign //微信签名
				            },
				            function (res) {
				            	// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠
				                if (res.err_msg == "get_brand_wcpay_request：ok") {
				                		window.location.href=returnUrl;
				                	}else if(res.err_msg == "get_brand_wcpay_request：cancel"){//取消支付
				                		$('#refreshPayid').val('N');
				                		cancel();
					                	if($('#isDirect').val()=='Y'&&!isEmpty(showCancelUrl)){
					                		window.location.href=showCancelUrl;
					                	}
				                	}else{
				                		//唤起失败
				                		cancel();
				                		$('#isDirect').val('Y');
				                		toErrorPage("微信唤起失败，请稍后重试"+res.err_msg,showFailUrl);
				                	}
				                	recoverBtn();
				                	sessionStorage.getItem('loadPay',false);
				                }
				            
				 );
			}
// 			https://blog.csdn.net/lwg_1540652358/article/details/84380514
		</script>
	</body>
</html>