package com.xuyurepos.common.constants;
/**
 * 系统常量
 * @author yangfei
 *
 */
public interface SystemConstants {
	/**
	 * 字符集编码方式
	 */
	public static final String CHARSET_UTF_8  ="UTF-8";
	
	/**
	 * 空字符串
	 */
	public static final String STRINGEMPTY  ="";
	
	/**
	 * 字符串yes
	 */
	public static final String STRING_YES  ="y";
	
	/**
	 * 字符串no
	 */
	public static final String STRING_NO  ="n";
	/**
	 * 字符串pf 频繁请求
	 */
	public static final String STRING_PF  ="p";
	/**
	 * 逗号分隔符
	 */
	public static final String STRING_COMM  =",";
	
	/**
	 * 分号分隔符
	 */
	public static final String STRING_SENT  =";";
	
	/**
	 * 换行符
	 */
	public static final String STRING_ENTER  ="\n";
	
	/**
	 * 页面换行符
	 */
	public static final String STRING_HTML_ENTER  ="\r\n";
	/**
	 * 停复机接收状态码 00 复机
	 */
	public static final String STATE_FJ  ="00";
	/**
	 * 停复机接收状态码 02 停机
	 */
	public static final String STATE_TJ  ="02";
	/**
	 * 停复机返回页面状态码 0 成功
	 */
	public static final String STATE_CG  ="0";
	/**
	 * 停复机返回页面状态码 1 失败
	 */
	public static final String STATE_SB  ="1";
	/**
	 * 停复机返回页面状态码 -1  频繁操作
	 */
	public static final String STATE_PF  ="-1";
	/**
	 * 停复机返回页面状态码 2  接口或系统异常
	 */
	public static final String STATE_YC  ="2";
	/**
	 * 停复机返回页面状态码 3  余额不足 不能复机
	 */
	public static final String STATE_YEBZ  ="3";
	/**
	 * 停复机返回页面状态码-2  暂不支持
	 */
	public static final String STATE_ZBZC  ="-2";

}
