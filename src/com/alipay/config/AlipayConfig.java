package com.alipay.config;

public class AlipayConfig {
	// 商户appid
	public static String APPID = "2015121000954163";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIICXQIBAAKBgQDE9HJPmMZbAGRMLpPK9Q5ohbiAcY9zWO5E6BJ1NF1gQCUn6t1F2VwjqUm+NpptCP/DuViCt9x/gXySswJwX6HgnRwS7jVMkZPnZ6sVMipNrt4mq3LshfXUrjaTb9nr5UGV2VX0buV4+ljvIdwUz/3FszyRgqKVifysgT0Vb5JCGQIDAQABAoGAA2k0XZqIvOS51/PqjVZHJJpEQr00vcupfLoEL9FzWIpj2lgf8ui7xsZUx52MeRzxyQL5vHHQuMiWTrgY77dIR2LH7JTAWh2QosfO+pD9deP0gWicTIvKlfUZ3R4gUjpm0owdEy3BXDibJxcBar4nzXxw8QcgbUluFpwcyZsx340CQQDrRVyGy75zV6pQFoOCOkWPTqh5Q/EYhOx3yF9ZhZnmkMwCwoePsz1Fj/kSfyvcDkEinSMt0j2B0YjDajhavupHAkEA1k7Zb/+TjM+rvLzbrMYbM4uTmOABC790k2XPCvPuI3GlUDuewcDJXA8tDwXneG+2IY4u76oh7pi/jT2czatAnwJBAKZn7uPgpBpM/UecRPQNZznPdtYeuh6Plfm7HcXh+1LbQ5EIKn33VvUmuhLdJBaHdoaXAlTciWiS3drp/GYi9u0CQQCMm6lQMXimv66nI6ZiAozgUeiiOf6VraUX/IlOX2NUgAD91lBAQXQb4Z0nqTEItQrjkz06Y/7lY/Tx/4W872pzAkB499P5IyermBos4cnzdpdxWPvVVmQOGlLOZ+tPnnU5jNcz0xEapBnNNNW9uiQZwgVPS+MoPe3fWFK+/r17ayZ9" ;
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "https://iots.shingsou.com/wappay/notify";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "https://iots.shingsou.com/wappay/return";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA";
}
