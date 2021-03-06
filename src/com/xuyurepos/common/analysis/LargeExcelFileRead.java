package com.xuyurepos.common.analysis;


import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.xuyurepos.common.log.LoggerFactory;
/**
 * excel解析基类
 * @author yangfei
 *
 */
public abstract class LargeExcelFileRead {
	
	Logger logger=LoggerFactory.getInstance().getLogger(LargeExcelFileRead.class);
	
	private SheetHandler sheetHandler;

	public SheetHandler getSheetHandler() {
		return sheetHandler;
	}

	private void setSheetHandler(SheetHandler sheetHandler) {
		this.sheetHandler = sheetHandler;
	}

	// 处理一个sheet
	public void processOneSheet(String filename) throws Exception {
		InputStream sheet2 = null;
		OPCPackage pkg = null;
		try {
			pkg = OPCPackage.open(filename);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			StylesTable stylesTable= r.getStylesTable();
			XMLReader parser = fetchSheetParser(sst,stylesTable);
			sheet2 = r.getSheet("rId1");
			InputSource sheetSource = new InputSource(sheet2);
			parser.parse(sheetSource);
			dealData(sheetHandler.getRowContents(),1);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pkg != null) {
				pkg.close();
			}
			if (sheet2 != null) {
				sheet2.close();
			}
		}
	}

	// 处理多个sheet
	public void processAllSheets(String filename) throws Exception {
		OPCPackage pkg = null;
		InputStream sheet = null;
		try {
			pkg = OPCPackage.open(filename);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			StylesTable stylesTable= r.getStylesTable();
			XMLReader parser = fetchSheetParser(sst,stylesTable);
			Iterator<InputStream> sheets = r.getSheetsData();
			int i=1;
			while (sheets.hasNext()) {
				  sheetHandler.getRowContents().clear();
				  sheetHandler.setRowContents(new LinkedHashMap<String, String>());
				  sheet = sheets.next();
                  InputSource sheetSource = new InputSource(sheet);
                  parser.parse(sheetSource);
				  dealData(sheetHandler.getRowContents(),i);
				  if (sheet != null) {
						sheet.close();
						sheet=null;
				  }
				  i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pkg != null) {
				pkg.close();
			}
			if (sheet != null) {
				sheet.close();
			}
		}
	}
	
	protected  abstract void  dealData(LinkedHashMap<String, String> map,int i);

	private XMLReader fetchSheetParser(SharedStringsTable sst,StylesTable stylesTable) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
		setSheetHandler(new SheetHandler(sst,stylesTable));
		ContentHandler handler = (ContentHandler) sheetHandler;
		parser.setContentHandler(handler);
		return parser;
	}
}
