package com.xuyurepos.common.analysis;

import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * excel解析数据处理
 * @author yangfei
 */
public class SheetHandler extends DefaultHandler{
	 private SharedStringsTable sst;
	 private StylesTable stylesTable;
	    private String lastContents;
	    private boolean nextIsString;
	    private String  cellPosition;
	    
	    private CellDataType nextDataType = CellDataType.SSTINDEX; 
	    
	    private String preRef = null, ref = null;  
        //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格  
        private String maxRef = null;  
	    
	    private final DataFormatter formatter = new DataFormatter();   
	    
	    private short formatIndex;   
        private String formatString;  
	    
	     //用一个enum表示单元格可能的数据类型  
        enum CellDataType{   
            BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL   
        }  
	    
	    private  LinkedHashMap<String, String>rowContents=new LinkedHashMap<String, String>(); 

	    public LinkedHashMap<String, String> getRowContents() {
	        return rowContents;
	    }

	    public void setRowContents(LinkedHashMap<String, String> rowContents) {
	        this.rowContents = rowContents;
	    }

	    public SheetHandler(SharedStringsTable sst,StylesTable stylesTable) {
	        this.sst = sst;
	        this.stylesTable=stylesTable;
	    }

	    public void startElement(String uri, String localName, String name,
	            Attributes attributes) throws SAXException {
	        if(name.equals("c")) {
	         //   System.out.print(attributes.getValue("r") + " - ");
	            cellPosition=attributes.getValue("r");
	            String cellType = attributes.getValue("t");
	            if(cellType != null && cellType.equals("s")) {
	                nextIsString = true;
	            } else {
	                nextIsString = false;
	            }
	            
	            //当前单元格的位置  
                ref = attributes.getValue("r");  
                this.setNextDataType(attributes);   
	        }
	        // 清楚缓存内容
	        lastContents = "";
	    }
	    
	    public void setNextDataType(Attributes attributes){   
	    	  
            nextDataType = CellDataType.NUMBER;   
            formatIndex = -1;   
            formatString = null;   
            String cellType = attributes.getValue("t");   
            String cellStyleStr = attributes.getValue("s");   
            if ("b".equals(cellType)){   
                nextDataType = CellDataType.BOOL;  
            }else if ("e".equals(cellType)){   
                nextDataType = CellDataType.ERROR;   
            }else if ("inlineStr".equals(cellType)){   
                nextDataType = CellDataType.INLINESTR;   
            }else if ("s".equals(cellType)){   
                nextDataType = CellDataType.SSTINDEX;   
            }else if ("str".equals(cellType)){   
                nextDataType = CellDataType.FORMULA;   
            }  
            if (cellStyleStr != null){   
                int styleIndex = Integer.parseInt(cellStyleStr);   
                XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);   
                formatIndex = style.getDataFormat();   
                formatString = style.getDataFormatString();   
                if ("m/d/yy" == formatString){   
                    nextDataType = CellDataType.DATE;   
                    formatString = "yyyy-MM-dd";  
                }   
                if (formatString == null){   
                    nextDataType = CellDataType.NULL;   
                    formatString = BuiltinFormats.getBuiltinFormat(formatIndex);   
                }   
            }   
        } 

	    public void endElement(String uri, String localName, String name)
	            throws SAXException {
	        if(nextIsString) {
	            int idx = Integer.parseInt(lastContents);
	            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
	            nextIsString = false;
	        }

	        if(name.equals("v")) {
//	            System.out.println("lastContents:"+cellPosition+";"+lastContents);
	        	String value = this.getDataValue(lastContents.trim(), "");   
	            //数据读取结束后，将单元格坐标,内容存入map中
	        	if(!(cellPosition.length()==2)||(cellPosition.length()==2)){
	                rowContents.put(cellPosition, value);
	            }
	        }
	    }
	    
	    public String getDataValue(String value, String thisStr)   
	    
        {   
            switch (nextDataType)   
            {   
                //这几个的顺序不能随便交换，交换了很可能会导致数据错误   
                case BOOL:   
                char first = value.charAt(0);   
                thisStr = first == '0' ? "FALSE" : "TRUE";   
                break;   
                case ERROR:   
                thisStr = "\"ERROR:" + value.toString() + '"';   
                break;   
                case FORMULA:   
                thisStr = '"' + value.toString() + '"';   
                break;   
                case INLINESTR:   
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());   
                thisStr = rtsi.toString();   
                rtsi = null;   
                break;   
                case SSTINDEX:   
                String sstIndex = value.toString();   
                thisStr = value.toString();   
                break;   
                case NUMBER:   
                if (formatString != null){   
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();   
                }else{  
                    thisStr = value;   
                }   
                thisStr = thisStr.replace("_", "").trim();   
                break;   
                case DATE:   
                    try{  
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);   
                    }catch(NumberFormatException ex){  
                        thisStr = value.toString();  
                    }  
                thisStr = thisStr.replace(" ", "");  
                break;   
                default:   
                thisStr = "";   
                break;   
            }   
            return thisStr;   
        }  

	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
	        lastContents += new String(ch, start, length);
	    }

}
