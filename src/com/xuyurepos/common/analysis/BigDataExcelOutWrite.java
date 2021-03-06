package com.xuyurepos.common.analysis;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.xuyurepos.common.log.LoggerFactory;
/**
 * 数据导出公用类
 * @author yangfei
 */
public class BigDataExcelOutWrite {
	/***
	 * 日志相关处理
	 */
	private Logger logger=LoggerFactory.getInstance().getLogger(BigDataExcelOutWrite.class);
	
    private String filename;
	private String sheetName; //创建sheet页的名称
	@SuppressWarnings("unused")
	private String titleName; //标题名称
	private String[] colmnsNames;
	
	private Map<String, String> fieldLabel;// 数据库映射字段
	/**
	 * 直接采用构造方法会导致类无法使用
	 */
	@SuppressWarnings("unused")
	private BigDataExcelOutWrite() {}
	/**
	 * 构造方法
	 * @param filename 文件名称
	 * @param sheetName sheet名字
	 * @param titleName 标题名称
	 * @param colmnsNames 列名称
	 */
    public BigDataExcelOutWrite(String filename,String sheetName,String titleName,String[] colmnsNames,Map<String, String> fieldLabel){
    	this.filename=filename;
    	this.sheetName = sheetName;
    	this.titleName = titleName;
        this.colmnsNames=colmnsNames;
        this.fieldLabel=fieldLabel;
    }
	
	/**
	 * 数据库连接操作
	 * 
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
 
		// 使用jdbc链接数据库
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://39.108.158.92:3306/xuyurepos?useUnicode=true&&characterEncoding=UTF-8";
		String username = "F4";
		String password = "XuyuRe";
		// 获取数据库连接
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	
	/**
	 * 执行导出Excel操作
	 * @param isClose 连接是否需要关闭
	 * @param conn 连接对象
	 * @param sql  查询语句注意查询字段的顺序请与导出的顺序一致
	 * 否则会有错误,<BR/>
	 * 如果存在类型转换请注意一定要给别名<BR/>
	 * 如果只需要创建表头则sql可以传入空，连接也为空
	 * @return
	 */
	public boolean WriteExcel(boolean isClose,Connection conn,String sql) {
		// 内存中只创建100个对象，写临时文件，当超过100条，就将内存中不用的对象释放。
		SXSSFWorkbook wb = new SXSSFWorkbook(100);
		Font xssfFont=wb.createFont();
		xssfFont.setColor(XSSFFont.COLOR_NORMAL);
		xssfFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		xssfFont.setFontHeightInPoints((short)10);
		xssfFont.setFontName("宋体");
		
		CellStyle cellStyle=wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFont(xssfFont);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setLocked(false);
		
		Font xssfFont3=wb.createFont();
		xssfFont3.setColor(XSSFFont.COLOR_NORMAL);
		xssfFont3.setFontHeightInPoints((short)9);
		xssfFont3.setFontName("宋体");
		CellStyle xssfCellStyle3=wb.createCellStyle();
		xssfCellStyle3.setFont(xssfFont3);
		xssfCellStyle3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		xssfCellStyle3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		xssfCellStyle3.setBorderTop(XSSFCellStyle.BORDER_THIN);
		xssfCellStyle3.setBorderRight(XSSFCellStyle.BORDER_THIN);
		xssfCellStyle3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		xssfCellStyle3.setLocked(false);
		
		Sheet sheet = null; // 工作表对象
		Row nRow = null; // 行对象
		Cell nCell = null; // 列对象
		try {
			if(sql!=null&&!"".equals(sql)){
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql); // 获取执行结果
				ResultSetMetaData rsmd = rs.getMetaData(); // 获取执行结果的结构(rs.getMetaData().getTableName(1))就可以返回表名,rs.getMetaData().getColumnCount())
	 
				long startTime = System.currentTimeMillis();
				logger.info("开始执行时间 : " + startTime / 1000 + "m");
				int rowNo = 0; // 总行号
				int pageRowNo = 0; // 页行号
	 
				while (rs.next()) {
					// 打印50000条后切换到下个工作表，可根据需要自行拓展，2百万，3百万...数据一样操作，只要不超过1048576就可以
					if (rowNo % 50000 == 0) {
						logger.info("当前sheet页为:" + rowNo / 50000 );
						sheet = wb.createSheet(sheetName+ (rowNo / 50000 + 1));// 建立新的sheet对象
						sheet = wb.getSheetAt(rowNo / 50000); // 动态指定当前的工作表
						pageRowNo = 1; // 每当新建了工作表就将当前工作表的行号重置为1
						
					    //定义表头
					    nRow = sheet.createRow(0);
	                    for (int i = 0; i < colmnsNames.length; i++) {
	                    	Cell cell1=nRow.createCell(i);
							cell1.setCellStyle(cellStyle);
							cell1.setCellValue(colmnsNames[i]);
						}
					}
					rowNo++;
					nRow = sheet.createRow(pageRowNo++); // 新建行对象
	                int j=0;
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						String columnName = rsmd.getColumnName(i + 1);
						if (fieldLabel.containsKey(columnName)) {
							nCell = nRow.createCell(j);
							if (rs.getObject(columnName) != null) {
								nCell.setCellValue(""+rs.getObject(columnName));
							}else{
								nCell.setCellValue("");
							}
							nCell.setCellStyle(xssfCellStyle3);
							j++;
						}
					}
	 
					if (rowNo % 10000 == 0) {
						logger.info("row no: " + rowNo);
					}
				}
				
				// 如果没有数据则直接输出表头
				if(rowNo==0){
					sheet = wb.createSheet(sheetName);// 建立新的sheet对象
					sheet = wb.getSheetAt(rowNo); // 动态指定当前的工作表
					pageRowNo = 1; // 每当新建了工作表就将当前工作表的行号重置为1
					
				    //定义表头
				    nRow = sheet.createRow(0);
	                for (int i = 0; i < colmnsNames.length; i++) {
	                	Cell cell1=nRow.createCell(i);
						cell1.setCellStyle(cellStyle);
						cell1.setCellValue(colmnsNames[i]);
					}
				}
	 
				long finishedTime = System.currentTimeMillis(); // 处理完成时间
				logger.info("数据读取完成耗时 : " + (finishedTime - startTime) + "ms");
				logger.info("数据读取完成耗时 : " + (finishedTime - startTime) / 1000 + "m");
				
				FileOutputStream fOut = new FileOutputStream(filename);//将数据写入Excel
				wb.write(fOut);
				fOut.flush(); // 刷新缓冲区
				fOut.close();
	 
				long stopTime = System.currentTimeMillis(); // 写文件时间
				logger.info("数据写入Excel表格中耗时 : " + (stopTime - startTime) + "ms");
				logger.info("数据写入Excel表格中耗时 : " + (stopTime - startTime) / 1000 + "m");
				if (isClose) {
					this.close(rs, stmt, conn);
				}
			}else{
				long startTime = System.currentTimeMillis();
				int rowNo = 0; // 总行号
				int pageRowNo = 0; // 页行号
				sheet = wb.createSheet(sheetName);// 建立新的sheet对象
				sheet = wb.getSheetAt(rowNo); // 动态指定当前的工作表
				pageRowNo = 1; // 每当新建了工作表就将当前工作表的行号重置为1
				
			    //定义表头
			    nRow = sheet.createRow(0);
                for (int i = 0; i < colmnsNames.length; i++) {
                	Cell cell1=nRow.createCell(i);
					cell1.setCellStyle(cellStyle);
					cell1.setCellValue(colmnsNames[i]);
				}
                FileOutputStream fOut = new FileOutputStream(filename);//将数据写入Excel
				wb.write(fOut);
				fOut.flush(); // 刷新缓冲区
				fOut.close();
				
				long stopTime = System.currentTimeMillis(); // 写文件时间
				logger.info("数据写入Excel表格中耗时 : " + (stopTime - startTime) + "ms");
				logger.info("数据写入Excel表格中耗时 : " + (stopTime - startTime) / 1000 + "m");
			}
 
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
 
	// 执行关闭流的操作
	private void close(ResultSet rs, Statement stmt, Connection conn)throws SQLException {
		rs.close();
		stmt.close();
		conn.close();
	}
	//测试方法
	public static void main(String[] args) throws Exception {
		String filename="D:/bigData.xlsx";
		String sql = "select ACCESS_NUM,MARK,IMSI,ICCID,DATE_FORMAT(REAL_ESTABLISH,'%Y-%m-%d') as REAL_ESTABLISH,DATE_FORMAT(REAL_ACTIVATE,'%Y-%m-%d') as REAL_ACTIVATE from XUYU_CONTENT_CARD_MGR t1 where 1=2 ";
		String[] colmnsList = {
	  			"接入号"
	  			,"卡号备注"
	  			,"IMSI"
	  			,"ICCID"
	  			,"开户日期"
	  			,"激活日期"
	  	};
		// 字段映射
		Map<String, String> fieldLabel=new HashMap<String, String>();
		fieldLabel.put("ACCESS_NUM", "接入号");
		fieldLabel.put("MARK", "卡号备注");
		fieldLabel.put("IMSI", "IMSI");
		fieldLabel.put("ICCID", "ICCID");
		fieldLabel.put("REAL_ESTABLISH", "开户日期");
		fieldLabel.put("REAL_ACTIVATE", "激活日期");
		BigDataExcelOutWrite bdeo = new BigDataExcelOutWrite(filename,"Sheet","",colmnsList,fieldLabel);
		bdeo.WriteExcel(true,bdeo.getConnection(),"");
	}

}
