<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String basePath = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	
	<meta http-equiv="Access-Control-Allow-Origin" content="iots.shingsou.com">
	<title>物联网平台登录</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/themes/default/easyui.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-easyui-1.7.0/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" type="text/css" href="css/login/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/login/icon.css" />
	<link rel="stylesheet" type="text/css" href="css/login/cmpIndex.css" />
	<link rel="stylesheet" type="text/css" href="css/login/layer.css" />
	<script language="JavaScript">   
	if (window != top)   
	top.location.href = location.href;   
	</script>  
</head>
<body>
    <div class="banner">
        <div class="container header">
            <div class="header-bar">
                <span id="notice"></span>
                <ul class="header-bar-action">
                    <li>
                        <span class="icon icon-Online-top-up"></span>
                        <span><a href="#">物联卡充值</a></span>
                    </li>
                </ul>
            </div>

            <div class="clear"></div>
            <!--logo及菜单-->
            <div class="logo-menus">
                <div class="logo l_float" onclick=""></div>
                <div class="menus r_float">
                    <ul class="l_float">
                        <li><a class="current-menu" data-nav="banner" href="login.jsp" onclick="qiehuan(this)" id="titleSy">首页</a></li>
                        <li data-nav="card-fun">
                        <a href="login.jsp#wulianka" onclick="qiehuan(this)" id="titleWlk">物联卡</a>
                        </li>
                        <li data-nav="card-platform">
                         <a href="login.jsp#pingtai" onclick="qiehuan(this)" id="titlePt">平台</a>
                        </li>
                        <li data-nav="api">
                            <a class="url" target="_blank" href="#">API介绍</a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="loginContainer loginContainer1" style="display: block">
                <div class="login">
                    <div class="login-top">
                        <h4>
                            <a class="loginTop-left" style="text-decoration: none;border-bottom: 2px solid #49a1ea;color: #49a1ea;" href="javascript:void(0);" onclick="showUserNameLogin();" id="userNameLoginBtn">账号登录</a>
                            <a class="loginTop-right" style="text-decoration: none;border-bottom: 2px solid #000000;color: #000000;" href="javascript:void(0);" onclick="showPhoneLogin();" id="phoneLoginBtn">手机登录</a>
                        </h4>
                    </div>

                   <form id="loginForm" action=<%=basePath%>/user/login method="POST">
	                    <div class="login-form r_float" style="display: block" id="userNameLogin">
	                        
	                        <div>
	                            <span class="icon icon-lock"></span>
	                            <input type="text" id="aw-login-user-name" name="userName" class="form-control" placeholder="账号">
	                        </div>
	                        <div>
	                            <span class="icon icon-lock"></span>
	                            <input type="password" id="aw-login-user-password" name="password" class="form-control" placeholder="密码">
	                        </div>
	                        <div class="confirm">
	                            <span class="icon icon-lock"></span>
	                            <input type="text" id="jcaptcha" name="j_captcha"  class="form-control" maxlength="4" placeholder="验证码">
	                        </div>
	                        <div class="confirm-text"><img width="80" height="36" id="captchaImg" ></div>
	                        <div class="info">
	                            <span id="info"></span>
	                        </div>
	                        <div class="submit">
	                            <a id="loginBtn" href="javascript:void();" style="color:#fff;" class="btn btn-primary login-btn">登录</a>
	                            <a href="#">忘记密码？</a>
	                            <a class="cursorDefault"></a>
	                            <a href="#" id="touristLink">游客访问</a>
	                        </div>
	                    </div>
                    </form>

             
                    <form id="loginFormPhone"  method="POST">
	                    <div class="login-form r_float" style="display: none" id="phoneNumLogin">
	                        
	                         <div>
	                            <span class="icon icon-lock"></span>
	                            <input type="text" id="phoneNum" name="phoneNum" maxlength="11" placeholder="移动、电信手机号码">
	                        </div>
	                            <div style="border:0px solid transparent">
	                                <div class="confirm" style="border: 1px solid #dadada">
	                                   <span class="icon icon-lock"></span>
	                                    <input type="text" id="jcaptcha2" name="j_captcha" maxlength="4" style="width: 90px;" placeholder="图片验证码">
	                                </div>
	                                <div class="confirm-text" style="background: none">
	                                    <img width="80" height="36" id="captchaImg2">
	                                </div>
	                            </div>
	                            
	                            <div class="confirmP">
	                                <span class="icon icon-lock"></span>
	                                <input type="text" id="msgCode" name="msgCode" maxlength="6" placeholder="短信验证码">
	                            </div>
	                            <div class="confirm-textP">
	                                   <button class="btn btn-primary getCode-btn" id="getCode">
	                                       点击获取
	                                   </button>
	                            </div>
	                        <div class="info">
	                            <span id="info2" style="height: 14px"></span>
	                        </div>
	                        <div class="submit">
	                            <button class="btn btn-primary login-btn" id="loginPhoneBtn">登录</button>
	                        </div>
	                    </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="slide-img">
            <ul class="row slide-container">
                <li class="current">
                    <a>
                        <img src="<%=request.getContextPath()%>/images/login/slide1.png">
                    </a>
                </li>
            </ul>
        </div>
    </div>


    <a name="wulianka"></a><!-- 定义锚点 -->
    <div class="card-fun container">
        <div class="card-fun-title text-center">
            <h2>物联卡业务功能&amp;优势</h2>
            <p>物联卡业务是中国移动面向物联网用户提供的采用物联网专用的号段作为MSISDN的移动通信接入业务，通过专用网元设备支持短信和数据服务等基础通信服务，并提供通信状态管理和通信鉴权等智能通道服务，同时默认开通物联网专用的短信接入服务号和物联网专用APN。
            </p>
        </div>
        <ul class="card-list row">
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-one col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>高质量的网络</h5>
                    <p>中国移动拥有全球规模最大，覆盖范围最广的物联网网络，目前已建设超过300万个2/3/4G基站。率先在国内搭建了公众物联网，建设物联网专用核心网元，为物联网客户提供优质的网络服务。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-two col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>API开放能力</h5>
                    <p>通过平台对接的方式向客户平台输出7大类、20多个API，满足客户使用情况查询、账户信息查询、物联卡状态查询等业务运营管理需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-three col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>一点接入全网使用</h5>
                    <p>可实现各省公司／政企公司一点业务受理、开通，为客户提供全网智能连接服务无漫游费，满足客户全网“一站式服务”需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-four col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>丰富码号资源</h5>
                    <p>拥有13位以10647、10648开头、11位以1476、1724、1789、1849开头的物联网专用号码，总容量达两亿以上，满足行业客户大量的码号需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-five col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>安全可靠的数据传输</h5>
                    <p>通过VPN（MPLS VPN、IPSec、L2tp）或者物理专线的方式在网络侧为用户提供专用的数据传输通道，满足用户的高安全性、可靠性、专用性需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-six col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>工业级SIM卡</h5>
                    <p>提供车规级、工业级等多类型物联卡，提供插拔式和贴片式等物联卡形态，满足客户对抗震动、抗腐蚀和耐高低温等多方面的个性需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-seven col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>灵活计费方式</h5>
                    <p>针对物联网业务的特殊性，提供测试期、沉默期、长周期套餐（季包、半年包、年包）、流量池、加油包、安心包等灵活计费方式。满足客户生产测试、库存、运输等全业务流程需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-eight col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>空中写卡</h5>
                    <p>在不更换物联卡的情况下，通过OTA方式实现数据动态切换，满足跨境、跨运营商客户实现“换卡不换号”的需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-nine col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>用户自助管理</h5>
                    <p>向客户分配物联卡连接管理平台专用账号，满足客户对终端的工作状态、通信状态等进行实时自主管理的需求。为客户提供物联卡的在线状态查询、开关机状态查询、故障诊断工具、阈值告警等服务，满足行业客户的通信故障诊断需求。</p>
                </div>
            </li>
            <li class="col-lg-6 col-xs-12">
                <span class="icon icon-ten col-xs-1"></span>
                <div class="col-xs-11 padding-right-50">
                    <h5>位置定位服务</h5>
                    <p>提供LBS基站定位能力，满足客户在终端无流量、低功耗的业务场景下，及时快速获取物联网终端位置信息的需求。</p>
                </div>
            </li>
        </ul>
    </div>

    <a name="pingtai"></a><!-- 定义锚点 -->
    <div class="card-platform">
        <div class="container">
            <h2>物联卡连接管理平台</h2>
            <ul class="card-platform-list row">
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon1"></span>
                    <h5>通信管理－终端状态整体浏览</h5>
                    <p>通信管理能够将“SIM卡信息”、“账户信息”、“通信状态”、“连接历史”和“使用历史”等数据在一个界面全面展示。</p>
                </li>
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon2"></span>
                    <h5>告警管理－7类规则涵盖数据、短信、账户监控</h5>
                    <p>将用户从监控室解放出来，只需要根据不同的需求设置不同的告警规则，就可以在第一时间内知晓故障信息并采取措施解决故障。</p>
                </li>
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon3"></span>
                    <h5>智能诊断－GPRS／短信逐级检测</h5>
                    <p>用户可以对指定终端的“用户状态”、“GPRS／短信订购状态”、“开关机状态”等进行实时检测，方便用户定位故障。</p>
                </li>
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon4"></span>
                    <h5>资费计划－自由选择、随心搭配</h5>
                    <p>用户可以进行“订购套餐查询”、“订购新套餐”、“套餐变更”和“套餐退订”操作，自由选择所需套餐。</p>
                </li>
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon5"></span>
                    <h5>停复机管理－主动停复机</h5>
                    <p>实现反向控制，在终端损坏、遗失等情况下客户可在连接管理平台主动关停机，减少客户损失。</p>
                </li>
                <li class="col-lg-4 col-md-6">
                    <span class="card-platform-icon card-platform-icon6"></span>
                    <h5>API集成应用－7类共计20多个API</h5>
                    <p>目前提供7大类供20多个API集成应用接口给第三方平台，极大的方便了客户获取SIM卡的通信、账户信息，并能够根据实际业务需求，进行整合和应用。</p>
                </li>
            </ul>
        </div>
    </div>
    
    <div class="footer">
        <div class="container">
            <div class="row footer-content">
                <div class="col-lg-8">
                    <!-- 
                    <div class="col-lg-12">
                        <h3>友情链接</h3>
                    </div>-->
                </div>
                <div class="col-lg-4">
                    <!-- 
                    <div class="col-xs-12">
                        <h3>联系我们</h3>
                        <ul>
                            <li>邮箱：</li>
                            
                            <li>传真：</li>
                        </ul>
                    </div> -->
                </div>
            </div>
            <div class="footer-bar">
                <img style="width: 20px; height: 20px;" src="http://www.beian.gov.cn/img/ghs.png" alt="备案标识" />
                <a href="http://beian.miit.gov.cn" target="_blank" rel="nofollow noopener noreferrer">沪ICP备13002254号</a>
                ©2019shingsou版权所有
            </div>
        </div>
    </div>

    <div class="fix-part">
        <div class="go-top">
            <span class="icon icon-shang"></span>
            <p>GOTOP</p>
        </div>
    </div>
    
    <input type = "text" hidden=true id = "code1"/>  
    <input type = "text" hidden=true id = "code2"/> 
</body>
<script src="js/index/checkCode.js"></script>
<script type="text/javascript">
        function qiehuan(a){
        	$('#titleSy').attr('class','');
        	$('#titleWlk').attr('class','');
        	$('#titlePt').attr('class','');
        	$('#'+a.id).attr('class','current-menu');
        }

		function showPhoneLogin(){
			$('#phoneNumLogin').css('display','block');
			$('#userNameLogin').css('display','none');
		}
		
		function showUserNameLogin(){
			$('#userNameLogin').css('display','block');
			$('#phoneNumLogin').css('display','none');
		}
		
		$(function() {
			$('#aw-login-user-name').validatebox({
			    required: true
			});
			
			$('#aw-login-user-password').validatebox({
			    required: true
			});
			
			$("#loginBtn").click(function(){
				var valid=$("#loginForm").form('enableValidation').form('validate');
				if(valid){
					
					if($('#jcaptcha').val()==''){
						$("#info").html("验证码不能为空");
					}else{
						// 验证码校验
						var jcaptcha=$("#jcaptcha").val();
						var code=$("#code1").val();
						if(jcaptcha.toUpperCase()==code){
							//ajax异步提交  
						    $.ajax({            
				                  type:"POST",   //post提交方式默认是get
				                  url:"<%=basePath%>/user/login", 
				                  data:$("#loginForm").serialize(),   //序列化   
				                  error:function(data) {      // 设置表单提交出错 
				                      $("#info").html(""+data.msg+"");
				                  },
				                  success:function(data) {
				                	  if(data.sucess==true){
				                		   window.location.href = "index.jsp";
				                	  }else{
				                		  //$.messager.alert('信息提示',data.msg);
				                		  $("#info").html(""+data.msg+"");
				                	  }
				                  }  
				            });
						}else{
							$("#info").html("验证码错误");
						}
					}
					
				}else{
					if($('#aw-login-user-name').val()==''){
						$("#info").html("用户名不能为空");
					}else if($('#aw-login-user-password').val()==''){
						$("#info").html("密码不能为空");
					}
				}
			});
			
			$("body").keydown(function() {
	             if (event.keyCode == "13") {//keyCode=13是回车键
	                 $('#loginBtn').click();
	             }
	         });
			
		    function getCode1(){
		    	var code=createCode();
            	$("#code1").val(code);
                $('#captchaImg').attr("src", "<%=basePath%>/user/captcha.form?code="+code+"&timestamp=" + (new Date()).valueOf());
		    }
		    
		    getCode1();
		    
		    function getCode2(){
		    	var code=createCode();
            	$("#code2").val(code);
                $('#captchaImg2').attr("src", "<%=basePath%>/user/captcha.form?code="+code+"&timestamp="+ (new Date()).valueOf());
		    }
			
            // 更换验证码
            $('#captchaImg').click(function(){
            	getCode1();
            }); 
            $('#captchaImg2').click(function() {
            	getCode2();
            }); 
		});
	</script>
</html>

