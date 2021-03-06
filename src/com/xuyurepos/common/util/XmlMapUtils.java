package com.xuyurepos.common.util;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
 
 
/**
 * 
 * 提供Map<String,Object>转XML，XML转Map<String,Object>
 * 
 * @author MOSHUNWEI
 * @since 2020-03-09
 * @version 5.0
 */
 
 
public class XmlMapUtils {
 
 
	/**
	 * 
	 * 通过Map创建XML,Map可以多层转换
	 * 
	 * @param params
	 * @return	String-->XML
	 */
	public static String createXmlByMap(String parentName,Map<String, Object> params,String encoding,boolean isCDATA){
		Document doc = DocumentHelper.createDocument();
		doc.addElement(parentName);
		String xml = iteratorXml(doc.getRootElement(),parentName,params,isCDATA);
		return formatXML(xml,encoding);
	}
	
	/**
	 * 
	 * 通过Map创建XML,Map可以多层转换
	 * 可以自定义parent节点
	 * 
	 * @param params
	 * @return	String-->XML
	 */
	public static String createXmlByMap(String parentName,Map<String, Object> params,String encoding){
		Document doc = DocumentHelper.createDocument();
		doc.addElement(parentName);
		String xml = iteratorXml(doc.getRootElement(),parentName,params,false);
		return formatXML(xml,encoding);
	}
	
	/**
	 * 
	 * 通过Map创建XML,Map可以多层转
	 * 固定节点parent为Document
	 * 
	 * @param params
	 * @return	String-->XML
	 */
	public static String createXmlByMap(Map<String, Object> params,String encoding){
		String parentName = "Document";
		Document doc = DocumentHelper.createDocument();
		doc.addElement(parentName);
		String xml = iteratorXml(doc.getRootElement(),parentName,params,false);
		return formatXML(xml,encoding);
	}
	
	/**
	 * 
	 * 通过Map创建XML,Map可以多层转 
	 * 固定节点parent为Document
	 * 
	 * @param params 可自定义根节点
	 * @return	String-->XML
	 */
	public static String createXmlByMap2(Map<String, Object> params,String encoding,String parentName){
		Document doc = DocumentHelper.createDocument();
		doc.addElement(parentName);
		String xml = iteratorXml(doc.getRootElement(),parentName,params,false);
		return formatXML(xml,encoding);
	}
	
	/**
	 * 
	 * MapToXml循环遍历创建xml节点
	 * 此方法在value中加入CDATA标识符
	 * 
	 * @param element 根节点
	 * @param parentName 子节点名字
	 * @param params map数据
	 * @return String-->Xml
	 */
	
	@SuppressWarnings("unchecked")
	public static String iteratorXml(Element element,String parentName,Map<String,Object> params,boolean isCDATA) {
		Element e = element.addElement(parentName);
		Set<String> set = params.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			if(params.get(key) instanceof Map) {
				iteratorXml(e,key,(Map<String,Object>)params.get(key),isCDATA);
			}else {
				String value = params.get(key)==null?"":params.get(key).toString();
				if(!isCDATA) {
					e.addElement(key).addText(value);	
				}else {
					e.addElement(key).addCDATA(value);	
				}
			}
		}
		return e.asXML(); 
	}
	
	/**
	 * 格式化xml,显示为容易看的XML格式
	 * 
	 * @param inputXML
	 * @return
	 */
	public static String formatXML(String inputXML,String encoding){
		String requestXML = null;
		XMLWriter writer = null;
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(new StringReader(inputXML));
			if (document != null) {
				StringWriter stringWriter = new StringWriter();
				OutputFormat format = new OutputFormat("	", true);//格式化，每一级前的空格
				format.setEncoding(encoding);
				format.setNewLineAfterDeclaration(false);	//xml声明与内容是否添加空行
				format.setSuppressDeclaration(false);		//是否设置xml声明头部
				format.setNewlines(true);		//设置分行
				writer = new XMLWriter(stringWriter, format);
				writer.write(document);
				writer.flush();
				requestXML = stringWriter.getBuffer().toString();
			}
			return requestXML;
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}finally {
			if (writer != null) { 
				try {
					writer.close();
				} catch (IOException e) {
					
				}
			}
		}
	}
 
	
	/** 
	 * 
	* 通过XML转换为Map<String,Object> 
	* 
	* @param xml 为String类型的Xml
	* @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
	*/  
	public static Map<String, Object> createMapByXml(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element rootElement = doc.getRootElement();
		elementTomap(rootElement,map);
		return map;
	}
	
	/***
	 *  
	 * XmlToMap核心方法，里面有递归调用 
	 *  
	 * @param map 
	 * @param ele 
	 */  
	@SuppressWarnings("unchecked")
	public static Map<String, Object> elementTomap (Element outele,Map<String,Object> outmap) {
		List<Element> list = outele.elements();
		int size = list.size();
	if(size == 0){
			outmap.put(outele.getName(), outele.getTextTrim());
	}else{
		Map<String, Object> innermap = new HashMap<String, Object>();
		for(Element ele1 : list){
			String eleName = ele1.getName();
			Object obj =  innermap.get(eleName);
			if(obj == null){
				elementTomap(ele1,innermap);
			}else{
				if(obj instanceof java.util.Map){
				List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
				list1.add((Map<String, Object>) innermap.remove(eleName));
				elementTomap(ele1,innermap);
				list1.add((Map<String, Object>) innermap.remove(eleName));
				innermap.put(eleName, list1);
			}else{
				elementTomap(ele1,innermap);
				((List<Map<String, Object>>)obj).add(innermap);
			}
		}
	}
		outmap.put(outele.getName(), innermap);
	}
	return outmap;
}
public static void main(String[] args) {


		
		String strXml=	"<?xml version=\"1.0\" encoding=\"GBK\" ?>\r\n" + 
				"<operation_in>\r\n" + 
				"<process_code>OPEN_QRYINTERNETGRPPOOLGPRS</process_code>\r\n" + 
				"<app_id>109000000066</app_id>\r\n" + 
				"<access_token>ABCDE12345ABCDE12409</access_token>\r\n" + 
				"<sign></sign>\r\n" + 
				"<verify_code></verify_code>\r\n" + 
				"<req_type></req_type>\r\n" + 
				"<terminal_id></terminal_id>\r\n" + 
				"<accept_seq></accept_seq>\r\n" + 
				"<req_time>20171123194841</req_time>\r\n" + 
				"<req_seq>0_9834345</req_seq>\r\n" + 
				"<content>\r\n" + 
				"<qrytype>0</qrytype>\r\n" + 
				"<eccode>250241138250250000</eccode>\r\n" + 
				"<cycle>201711</cycle>\r\n" + 
				"</content>\r\n" + 
				"</operation_in>\r\n" + 
				""	;
		
		
	String strXml2=	"<?xml version=\"1.0\" encoding=\"GBK\" ?>\r\n" + 
		"<operation_out>\r\n" + 
		"<req_seq>0_9834345</req_seq>\r\n" + 
		"<resp_seq>0</resp_seq>\r\n" + 
		"<resp_time>20171123194223</resp_time>\r\n" + 
		"<response>\r\n" + 
		"<resp_type>0</resp_type>\r\n" + 
		"<resp_code>0000</resp_code>\r\n" + 
		"<resp_desc>\r\n" + 
		"<![CDATA[ 成功\r\n" + 
		"  ]]>\r\n" + 
		"</resp_desc>\r\n" + 
		"</response>\r\n" + 
		"<content>\r\n" + 
		"<resultlist>\r\n" + 
		"<gprsinfo>\r\n" + 
		"<user_id>925099101145662699</user_id>\r\n" + 
		"<max_value>47431680</max_value>\r\n" + 
		"<cumulate_value>11011093</cumulate_value>\r\n" + 
		"<product_id>2000006071</product_id>\r\n" + 
		"<product_name>GPRS中小流量新5元套餐（原子产品）</product_name>\r\n" + 
		"</gprsinfo>\r\n" + 
		"</resultlist>\r\n" + 
		"<resultlist>\r\n" + 
		"<gprsinfo>\r\n" + 
		"<user_id>925099101141207556</user_id>\r\n" + 
		"<max_value>61440000</max_value>\r\n" + 
		"<cumulate_value>9354036</cumulate_value>\r\n" + 
		"<product_id>2000006071</product_id>\r\n" + 
		"<product_name>GPRS中小流量新5元套餐（原子产品）</product_name>\r\n" + 
		"</gprsinfo>\r\n" + 
		"</resultlist>\r\n" + 
		"<resultlist>\r\n" + 
		"<gprsinfo>\r\n" + 
		"<user_id>925099101145709613</user_id>\r\n" + 
		"<max_value>15360000</max_value>\r\n" + 
		"<cumulate_value>1902928</cumulate_value>\r\n" + 
		"<product_id>2000006071</product_id>\r\n" + 
		"<product_name>GPRS中小流量新5元套餐（原子产品）</product_name>\r\n" + 
		"</gprsinfo>\r\n" + 
		"</resultlist>\r\n" + 
		"</content>\r\n" + 
		"<emergency_status>0</emergency_status>\r\n" + 
		"</operation_out>\r\n" + 
		"";
	
	
	String strXml3="<?xml   version=\"1.0\"     encoding=\"utf-8\"?>  <root>       <web:NEW_DATA_TICKET_QRsp    xmlns:web=\"http://www.example.org/webservice\">            <CHARGE_CNT_CH>0.0元</CHARGE_CNT_CH>            <DURATION_CNT_CH>8小时7分钟46秒</DURATION_CNT_CH>                <IRESULT>0</IRESULT>              <TOTALCOUNT>2</TOTALCOUNT>              <TOTALPAGE>1</TOTALPAGE>              <TOTAL_BYTES_CNT>26.28MB</TOTAL_BYTES_CNT>  <GROUP_TRANSACTIONID>1000000252201610286127001373</GROUP_TRANSACTIONID>         <number>1064910000000</number>       </web:NEW_DATA_TICKET_QRsp> </root>";
	
	    Map resultMap=createMapByXml(strXml3);
		System.out.println(resultMap);
		System.out.println(createXmlByMap(resultMap,"GBK"));
		System.out.println(createXmlByMap("Parent", resultMap,"GBK"));
		System.out.println(createXmlByMap("Parent", resultMap,"GBK",true));
	}
	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data Map类型数据
	 * @return XML格式的字符串
	 * @throws Exception
	 */
	public static String mapToXml(Map<String, String> map) throws Exception {
	    try {
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
	        org.w3c.dom.Document document = documentBuilder.newDocument();
	        org.w3c.dom.Element root = document.createElement("xml");
	    document.appendChild(root);
	    for (String key: map.keySet()) {
	        String value = map.get(key);
	        if (value == null) {
	            value = "";
	        }
	        value = value.trim();
	        org.w3c.dom.Element filed = document.createElement(key);
	        filed.appendChild(document.createTextNode(value));
	        root.appendChild(filed);
	    }
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    DOMSource source = new DOMSource(document);
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    transformer.transform(source, result);
	    String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
	        writer.close();
	        return output;
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return null;
	    } 
	}
	
	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data Map类型数据
	 * @return XML格式的字符串
	 * @throws Exception
	 */
	public static String mapToXmlGBK(Map<String, String> map, String parentNode) throws Exception {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document document = documentBuilder.newDocument();
			org.w3c.dom.Element root = document.createElement(parentNode);
			document.appendChild(root);
			for (String key: map.keySet()) {
				String value = map.get(key);
				if (value == null) {
					value = "";
				}
				value = value.trim();
				org.w3c.dom.Element filed = document.createElement(key);
				filed.appendChild(document.createTextNode(value));
				root.appendChild(filed);
			}
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
			writer.close();
			return output;
		} catch (Exception e) {  
			e.printStackTrace();  
			return null;
		} 
	}

}
