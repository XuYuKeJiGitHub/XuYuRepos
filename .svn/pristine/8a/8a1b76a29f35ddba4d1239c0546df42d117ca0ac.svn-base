package com.xuyurepos.common;

 
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.alipay.util.UniqueOrderGenerate;
 
 
public class WriteWordUtil {
 
    public static void main(String[] args) throws IOException {
    	 String srcPath = "D:\\《Vip特权服务费与扣款授权委托书》.docx";
         String destPath = "D:\\《Vip特权服务费与扣款授权委托书》-" + System.currentTimeMillis() + ".docx";
  
         InputStream in = new FileInputStream(srcPath);
         InputStream in1 = new FileInputStream(srcPath);
         FileOutputStream out = new FileOutputStream(destPath);
         Map<String, String> map = new HashMap<>();
         map.put("${idNo}", "身份证");
         map.put("${userName}","姓名");
         map.put("${telNo}", "4");
         map.put("${bankNo}", "5");
         map.put("${bank}", "6");
         map.put("${year}", "8");
         map.put("${month}", "9");
         map.put("${day}", "10");
         replaceText(in,in1, out, map);
         in1.close();
         in.close();
         out.close();
    }
    
    private static Map<String,Object> getMap(InputStream inputStream,Map<String, String> map){
    	Map<String,Object> mapp=new  LinkedHashMap<String,Object>();
        try {
            XWPFDocument document;//= new XWPFDocument(POIXMLDocument.openPackage(srcPath));
            document = new XWPFDocument(inputStream);
            //1. 替换段落中的指定文字
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            String text;
            Set<String> set;
            XWPFParagraph paragraph;
            List<XWPFRun> run;
            String key;
            String value="";
            int valuei=0;
            Map<String,Object> mapKey=new  HashMap<String,Object>();
            while (itPara.hasNext()) {
                paragraph = itPara.next();
                set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    run = paragraph.getRuns();
                    String a="";
                    for (int i = 0, runSie = run.size(); i < runSie; i++) {
                        text = run.get(i).getText(run.get(i).getTextPosition());
                        if(key.indexOf(text)!=-1){
                        	 a=a+text;
                        	 if(text.indexOf("}")!=-1){
                        		 if(map.get(a)!=null){
                        			 mapp.put(valuei+"ok"+i,a);
                        		 }
                        		 a="";
                        	 }
                        }
                    }
                }
                valuei++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapp;
    }
 
    public static void replaceText(InputStream inputStream,InputStream inputStream1, OutputStream outputStream, Map<String, String> map) {
        try {
        	Map<String,Object> mapp=getMap(inputStream1, map);
            XWPFDocument document;//= new XWPFDocument(POIXMLDocument.openPackage(srcPath));
            document = new XWPFDocument(inputStream);
            //1. 替换段落中的指定文字
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            String text;
            Set<String> set;
            XWPFParagraph paragraph;
            List<XWPFRun> run;
            String value="";
            int valuei=0;
            String key="";
            while (itPara.hasNext()) {
                paragraph = itPara.next();
                set = map.keySet();
                run = paragraph.getRuns();
                for (int i = 0, runSie = run.size(); i < runSie; i++) {
                    text = run.get(i).getText(run.get(i).getTextPosition());
                    if(mapp.get(valuei+"ok"+i)!=null){
                    	key=""+mapp.get(valuei+"ok"+i);
                    	run.get(i).setText(map.get(mapp.get(valuei+"ok"+i)), 0);
                    }else{
						// 循环map中的key
						Set<String> set1 = map.keySet();
						Iterator<String> iterator1 = set1.iterator();
						String key1;
						while (iterator1.hasNext()) {
							key1 = iterator1.next();
							if(key1.indexOf(text)!=-1){
								run.get(i).setText("", 0);
							}
						}
                    }
                }
                valuei++;
            }
            //3.输出流
            document.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}