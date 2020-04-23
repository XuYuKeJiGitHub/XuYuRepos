package com.xuyurepos.common.util.httpclient;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 网络工具类
 *
 * @author Sven Augustus
 */
public abstract class HttpUtils {

	private static final String DEFAULT_CHARSET = "utf-8";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,
								int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET, connectTimeout,
				readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,
								String charset, int connectTimeout, int readTimeout)
			throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(params, charset);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(charset);
		}
		return doPost(url, ctype, content, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url 请求地址
	 * @param content 请求数据
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, String content, int connectTimeout,
								int readTimeout) throws IOException {
		return doPost(url, content, DEFAULT_CHARSET, connectTimeout,
				readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url 请求地址
	 * @param content 请求数据
	 * @param content 字符编码
	 *doPost
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, String content, String charset,
								int connectTimeout, int readTimeout) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		return doPost(url, ctype, content.getBytes(charset), connectTimeout,
				readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字节数组
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, String ctype, byte[] content,
								int connectTimeout, int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				conn = getConnection(new URL(url), METHOD_POST, ctype);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				throw e;
			}
			try {
				out = conn.getOutputStream();
				out.write(content);
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * 执行HTTP GET请求。
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params)
			throws IOException {
		return doGet(url, params, DEFAULT_CHARSET);
	}

	/**
	 * 执行HTTP GET请求。
	 *
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params,
							   String charset) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "text/html;application/x-www-form-urlencoded;charset="
					+ charset;
			String query = buildQuery(params, charset);
			try {
				conn = getConnection(buildGetUrl(url, query), METHOD_GET,
						ctype);
			} catch (IOException e) {
				throw e;
			}
			try {
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rsp;
	}

	public static HttpURLConnection getConnection(URL url) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			SSLContext ctx = null;
			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(new KeyManager[0],
						new TrustManager[]{new DefaultTrustManager()},
						new SecureRandom());
			} catch (Exception e) {
				throw new IOException(e);
			}
			HttpsURLConnection connHttps = (HttpsURLConnection) url
					.openConnection();
			connHttps.setSSLSocketFactory(ctx.getSocketFactory());
			connHttps.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return false;//默认认证不通过，进行证书校验。
				}
			});
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}
		return conn;
	}

	private static HttpURLConnection getConnection(URL url, String method,
												   String ctype) throws IOException {
		HttpURLConnection conn = getConnection(url);
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
		conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}

	private static URL buildGetUrl(String strUrl, String query)
			throws IOException {
		URL url = new URL(strUrl);
		if (StringUtils.isEmpty(query)) {
			return url;
		}
		StringBuilder urlSb = new StringBuilder(strUrl);

		if (StringUtils.isEmpty(url.getQuery())) {
			if (strUrl.endsWith("?")) {
				urlSb.append(query);
			} else {
				urlSb.append("?").append(query);
			}
		} else {
			if (strUrl.endsWith("&")) {
				urlSb.append(query);
			} else {
				urlSb.append("&").append(query);
			}
		}
		return new URL(strUrl);
	}

	public static Map<String, String> getParamsFromUrl(String url)
			throws IOException {
		Map<String, String> map = null;
		if (url != null && url.indexOf('?') != -1) {
			map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
		}
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	protected static String getResponseAsString(HttpURLConnection conn)
			throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":"
						+ conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	protected static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
	}

	/**
	 * 组装URL请求参数串。
	 *
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String buildQuery(Map<String, String> params)
			throws IOException {
		return buildQuery(params, null);
	}

	/**
	 * 组装URL请求参数串。
	 *
	 * @param params
	 * @param charset 编码选项
	 * @return
	 * @throws IOException
	 */
	public static String buildQuery(Map<String, String> params, String charset)
			throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();

			// 忽略参数名或参数值为空的参数
			if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(name).append("=")
						.append((StringUtils.isEmpty(charset)
								? value
								: URLEncoder.encode(value, charset)));
			}
		}
		return query.toString();
	}

	/**
	 * 从URL中提取所有的参数。
	 *
	 * @param query URL地址
	 * @return 参数映射
	 * @throws IOException
	 */
	public static Map<String, String> splitUrlQuery(String query)
			throws IOException {
		return splitUrlQuery(query, null);
	}

	/**
	 * 从URL中提取所有的参数。
	 *
	 * @param query URL地址
	 * @param charset 解码，为空时不解码
	 * @return 参数映射
	 * @throws IOException
	 */
	public static Map<String, String> splitUrlQuery(String query,
													String charset) throws IOException {
		Map<String, String> result = new HashMap<String, String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					String val = "";
					try {
						val = StringUtils.isEmpty(charset)
								? param[1]
								: URLDecoder.decode(param[1], charset);
					} catch (IOException e) {
						throw e;
					}
					result.put(param[0], val);
				}
			}
		}
		return result;
	}

	/**
	 * 使用默认的UTF-8字符集反编码请求参数值。
	 *
	 * @param value 参数值
	 * @return 反编码后的参数值
	 */
	public static String decode(String value) {
		return decode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用默认的UTF-8字符集编码请求参数值。
	 *
	 * @param value 参数值
	 * @return 编码后的参数值
	 */
	public static String encode(String value) {
		return encode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用指定的字符集反编码请求参数值。
	 *
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 反编码后的参数值
	 */
	public static String decode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLDecoder.decode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 使用指定的字符集编码请求参数值。
	 *
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 编码后的参数值
	 */
	public static String encode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLEncoder.encode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 从HTTP请求中提取所有的参数。
	 *
	 * @param request HTTP请求对象
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParams(
			final HttpServletRequest request) throws IOException {
		return getRequestParams(request, null, false);
	}

	/**
	 * 从http请求中提取所有的参数。
	 *
	 * @param request HTTP请求对象
	 * @param removeEmptyElement 去除空参数选项
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParams(
			final HttpServletRequest request, boolean removeEmptyElement)
			throws IOException {
		return getRequestParams(request, null, removeEmptyElement);
	}

	/**
	 * 从http请求中提取所有的参数。
	 *
	 * @param request HTTP请求对象
	 * @param charset 解码，为空时不解码
	 * @param removeEmptyElement 去除空参数选项
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParams(
			final HttpServletRequest request, String charset,
			boolean removeEmptyElement) throws IOException {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String val = "";
				try {
					val = StringUtils.isEmpty(charset)
							? request.getParameter(en)
							: URLDecoder.decode(request.getParameter(en),
									charset);
				} catch (IOException e) {
					throw e;
				}
				res.put(en, val);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (removeEmptyElement) {
					if (null == res.get(en) || "".equals(res.get(en))) {
						res.remove(en);
					}
				}
			}
		}
		return res;
	}

	/**
	 * 从http请求中提取所有的参数，支持提取列表参数。
	 *
	 * @param request HTTP请求对象
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParamArray(
			final HttpServletRequest request) throws IOException {
		return getRequestParamArray(request, null, false);
	}

	/**
	 * 从http请求中提取所有的参数，支持提取列表参数。
	 *
	 * @param request HTTP请求对象
	 * @param removeEmptyElement 去除空参数选项
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParamArray(
			final HttpServletRequest request, boolean removeEmptyElement)
			throws IOException {
		return getRequestParamArray(request, null, removeEmptyElement);
	}

	/**
	 * 从http请求中提取所有的参数，支持提取列表参数。
	 *
	 * @param request HTTP请求对象
	 * @param charset 解码，为空时不解码
	 * @param removeEmptyElement 去除空参数选项
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getRequestParamArray(
			final HttpServletRequest request, String charset,
			boolean removeEmptyElement) throws IOException {
		Map<String, String> res = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		Set<Entry<String, Object>> entries = requestParams.entrySet();
		for (Entry<String, Object> entry : entries) {
			String name = entry.getKey();
			String[] values = (String[]) entry.getValue();
			StringBuilder valueStr = new StringBuilder();
			String pre = "";
			for (int i = 0; i < values.length; i++) {
				String val = "";
				try {
					val = StringUtils.isEmpty(charset)
							? values[i]
							: URLDecoder.decode(values[i], charset);
				} catch (IOException e) {
					throw e;
				}
				valueStr.append(pre).append(val);
				pre = ",";
			}
			res.put(name, valueStr.toString());
			//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
			if (removeEmptyElement) {
				if (StringUtils.isEmpty(valueStr.toString())) {
					res.remove(name);
				}
			}
		}
		return res;
	}

	/**
	 * 从http请求中提取流数据。
	 *
	 * @param request HTTP请求对象
	 * @return
	 * @throws IOException
	 */
	public static String getStreamParams(final HttpServletRequest request)
			throws IOException {
		return getStreamParams(request, null);
	}

	/**
	 * 从http请求中提取流数据。
	 *
	 * @param request HTTP请求对象
	 * @param charset 编码
	 * @return
	 * @throws IOException
	 */
	public static String getStreamParams(final HttpServletRequest request,
										 String charset) throws IOException {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			CharBuffer bos = CharBuffer.allocate(256);
			reader = new BufferedReader((StringUtils.isEmpty(charset)
					? new InputStreamReader(request.getInputStream())
					: new InputStreamReader(request.getInputStream(),
							charset)));
			while (reader.read(bos) != -1) {
				bos.flip();
				sb.append(bos.toString());
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		return sb.toString();
	}

	/**
	 * 从http请求中提取IP地址。
	 *
	 * @param request HTTP请求对象
	 * @return
	 * @throws IOException
	 */
	public static String getRemoteAddr(final HttpServletRequest request)
			throws IOException {
		String ip = request.getHeader("X-Forwarded-For");
		if (isIpNull(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isIpNull(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isIpNull(ip)) {
			ip = request.getRemoteAddr();
		}
		if (!isIpNull(ip) && ("0:0:0:0:0:0:0:1".equals(ip)
				|| "0:0:0:0:0:0:0:1%0".equals(ip) || "127.0.0.1".equals(ip))) {
			try {
				InetAddress iaClient = InetAddress.getLocalHost();
				ip = iaClient.getHostAddress();
			} catch (IOException e) {
				throw e;
			}
		}
		// 如果是多级代理的话,通过x-forwarded-for可能返回多个IP的现象,这里我始终取第一个有效的IP
		return (ip == null) ? null : ip.split(",")[0];
	}

	private static boolean isIpNull(String ip) {
		return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
	}

	// 定义正则表达式
	private static final String IPADDR_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

	/**
	 * 判断字符串是否ip地址
	 *
	 * @param ipAddr
	 * @return
	 */
	public static boolean isIPAddr(String ipAddr) {
		// 判断ip地址是否与正则表达式匹配
		if (ipAddr != null && ipAddr.matches(IPADDR_REGEX)) {
			// 返回判断信息
			return true;
		}
		return false;
	}

	/**
	 * IP地址转为long
	 *
	 * @param ipAddr
	 * @return
	 */
	public static long ipAddrToLong(String ipAddr) {
		if (ipAddr == null || !isIPAddr(ipAddr))
			return 0;
		String[] startIP = ipAddr.split("\\.");
		if (startIP == null || startIP.length < 4)
			return 0;
		return (Long.parseLong(startIP[0]) << 24)
				+ (Long.parseLong(startIP[1]) << 16)
				+ (Long.parseLong(startIP[2]) << 8)
				+ Long.parseLong(startIP[3]);
	}

	/**
	 * 验证IP地址是否在指定的IP段内
	 *
	 * @param checkedIpAddress 需验证的IP地址
	 * @param startIPAddress  IP段的起始地址
	 * @param endIPAddress IP段的结束地址
	 * @return
	 * @throws Exception
	 */
	public static boolean validateIPAddressRange(String checkedIpAddress,
												 String startIPAddress, String endIPAddress) throws IOException {
		if (!isIPAddr(checkedIpAddress))
			throw new IOException("方法参数checkedIpAddress不是一个合法格式的IP地址！");
		if (!isIPAddr(startIPAddress))
			throw new IOException("方法参数startIPAddress不是一个合法格式的IP地址！");
		if (!isIPAddr(endIPAddress))
			throw new IOException("方法参数endIPAddress不是一个合法格式的IP地址！");
		long ip1 = ipAddrToLong(startIPAddress);
		long ip2 = ipAddrToLong(endIPAddress);
		if (ip1 > ip2)
			throw new IOException("指定IP段的起始地址不能大于结束地址！");
		long ip = ipAddrToLong(checkedIpAddress);
		if (ip >= ip1 && ip <= ip2)
			return true;
		return false;
	}

}
