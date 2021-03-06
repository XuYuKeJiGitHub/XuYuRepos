package com.xuyurepos.controller.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuyurepos.common.constants.SystemConstants;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.upload.FileTypeConstance;
import com.xuyurepos.common.util.ProsUtil;
import com.xuyurepos.common.util.StringUtil;
import com.xuyurepos.entity.system.SystemAnnexe;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.system.SystemAnnexeService;
import com.xuyurepos.vo.system.SystemAnnexeVo;
/**
 * 系统附件相关处理类
 * @author yangfei
 */
@Controller
@RequestMapping(value = "/systemannexe")
public class SystemAnnexeControl {
	
	private static final String UPLOAD_FAILE="{sucess:false,mess:'文件上传失败'}";
	
	@Autowired
	private SystemAnnexeService systemAnnexeService; 
	
	Logger logger=LoggerFactory.getInstance().getLogger(SystemUserControl.class);
	
    private static Map<String, String> TYPE_MAP = new HashMap<String, String>();
    
    @Autowired
	private BizException bizException; 
	

	/**
	 * 文件上传
	 * @throws Exception 
	 */
	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void upload(HttpServletRequest request,HttpServletResponse response) throws Exception {
       StringBuffer fileName=new StringBuffer();
       String isImport=request.getParameter("isImport");
       String uploadPath=request.getParameter("uploadPath");
       
       response.setCharacterEncoding(SystemConstants.CHARSET_UTF_8);
       PrintWriter out=response.getWriter();
       if(!ServletFileUpload.isMultipartContent(request)){
    	   out.println(UPLOAD_FAILE);
       }
       String name=null;
       FileItem fileItem=null;
       try {
		    StringBuilder builder=new StringBuilder();
		    if(StringUtil.isNotBlank(uploadPath)){
		    	builder.append(uploadPath);
		    }else if(null==isImport){
		    	builder.append(ProsUtil.getProperty("uploadPath"));
		    }else{
		    	builder.append(ProsUtil.getProperty("importPath"));
		    }
		    if(!builder.toString().equals(File.separator)){
		    	builder.append(File.separator);
		    	fileName.append("import_");
		    }
		    
		    // 查看目录是否存在
		    String saveFilePath=builder.toString();
		    if(!new File(saveFilePath).exists()){
		    	new File(saveFilePath).mkdir();
		    }
		    
		    SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
		    DiskFileItemFactory factory=new DiskFileItemFactory();
		    ServletFileUpload upload=new ServletFileUpload(factory);
		    upload.setHeaderEncoding(SystemConstants.CHARSET_UTF_8);
		    upload.setSizeMax(Long.parseLong(ProsUtil.getProperty("maxUploadFileSize")));
		    
		    List<FileItem> items=upload.parseRequest(request);
		    String nameDate=new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss").format(new Date());
		    String uploadName=null;
		    for (Iterator<FileItem> i= items.iterator(); i.hasNext();) {
				 fileItem = (FileItem) i.next();
				 if(fileItem.isFormField()){
					 name=fileItem.getFieldName();
					 out.println(UPLOAD_FAILE);
				 }else{
					 name=fileItem.getName();
                     String simpleFileName = name.substring(name.lastIndexOf("\\")+1, name.length());
					 if(saveFilePath==null||"".equals(saveFilePath)){
						 out.println(UPLOAD_FAILE);
					 }else{
						 uploadName=systemUser.getUserName()+"_"+nameDate+FileTypeConstance.getExtFileName(fileItem.getName());
						 fileName.append(uploadName);
						 fileItem.write(new File(saveFilePath+'/'+fileName));
						 String annexeId="";
						
						 // 数据库操作
						 if(null==isImport){
							 SystemAnnexeVo systemAnnexe=new SystemAnnexeVo();
							 systemAnnexe.setCreateTime(new Date());
							 systemAnnexe.setAnnexeSize(Double.valueOf(fileItem.getSize()));
							 systemAnnexe.setAnnexeType(name.substring(name.indexOf(".")));
							 systemAnnexe.setAnnexeName(simpleFileName);
							 systemAnnexe.setUploadPath(saveFilePath+fileName);
							 systemAnnexe.setUploadUser(systemUser.getUserName());
							 systemAnnexe.setUploadUserName(systemUser.getCname());
							 systemAnnexe.setRelationInfo("");
							 systemAnnexe.setRelationMod("");
							 SystemAnnexe result=systemAnnexeService.saveInfo(systemAnnexe);
							 annexeId=result.getAnnexeId();
						 }
						 out.print("{success:true,annexeId:'"+annexeId+"'}");
					 }
				 }
			}
		    out.flush();
		    out.close();
	   }catch (SizeLimitExceededException e) {
		   logger.info("超出文件大小");
		   out.print("{sucess:false,mess:'文件上传失败,超出最大文件限制'}");
		   out.close();
	   } catch (Exception e) {
		   logger.info("未知错误");
		   out.print(UPLOAD_FAILE);
		   out.close();
	   }
	}
	
	/**
	 * 文件下载
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "/downFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void down(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 String filename = request.getParameter("filename");
	     String annexeName = request.getParameter("annexeName");
	     String uploadPath = request.getParameter("uploadPath");
	     String filepath;
	     if(filename != null && !"".equals(filename)&& annexeName != null && !"".equals(annexeName)) {
	    	 StringBuilder builder = new StringBuilder();
	            if(StringUtils.isNotBlank(uploadPath)){
	            	builder.append(uploadPath);
	            }else
	            	builder.append(ProsUtil.getProperty("uploadPath"));
	            if (!builder.toString().endsWith(File.separator)) {
	                builder.append(File.separator);
	            }
	            builder.append(File.separator);
	            builder.append(filename);
	            filepath = builder.toString();
	            logger.info("最终文件地址："+filepath);
	            File file = new File(filepath);
	            downFile(request, response, file, filename, annexeName, filepath);
	     }
	}
	
	/**
	 * 封装文件下载
	 * @param request
	 * @param response
	 * @param file
	 * @param filename
	 * @param annexeName
	 * @param filepath
	 * @throws IOException
	 */
	private void downFile(HttpServletRequest request,HttpServletResponse response,File file,String filename,String annexeName,String filepath) throws IOException{
    	
    	int data=0;
    	BufferedInputStream inputStream=null;
    	BufferedOutputStream outputStream=null;
    	try {
    		logger.info("ANNFileDownloadServlet 文档下载开始");
        	long millis1 = System.currentTimeMillis();
    		TYPE_MAP.put("xls", "application/vnd.ms-excel");
    		TYPE_MAP.put("doc", "application/msword");
    		TYPE_MAP.put("pdf", "application/pdf");
    		TYPE_MAP.put("ppt", "application/vnd.ms-powerpoint");
    		TYPE_MAP.put("zip", "application/zip");
    		TYPE_MAP.put("bmp", "image/bmp");
    		TYPE_MAP.put("gif", "image/gif");
    		TYPE_MAP.put("ief", "image/ief");
    		TYPE_MAP.put("jpeg", "image/jpeg");
    		TYPE_MAP.put("jpg", "image/jpeg");
    		TYPE_MAP.put("jpe", "image/jpeg");
    		TYPE_MAP.put("png", "image/png");
    		TYPE_MAP.put("exe", "application/octet-stream");
    		TYPE_MAP.put("xml", "text/xml");
    		
    		response.setContentType(FileTypeConstance.getHttpheaderofFile(filename));
    		response.setHeader("Content-Length", String.valueOf(file.length()));
    		response.setHeader("Content-Disposition", "attachment; filename=\""+URLEncoder.encode(new String(FileTypeConstance.getFileNameWithoutExt(annexeName).getBytes("UTF-8"),"UTF-8"),"UTF-8") 
             + FileTypeConstance.getExtFileName(annexeName) + "\"");
    		inputStream = new BufferedInputStream(new FileInputStream(filepath));
        	outputStream = new BufferedOutputStream(response.getOutputStream());
        	byte[] buff = new byte[1024];
        	data = inputStream.available();
        	int nResult = 0;
        	
        	while (inputStream.available() > 0) {
        		nResult = inputStream.read(buff);
        		if (nResult == -1)
        			break;
        		outputStream.write(buff, 0, nResult);
        	}
                
            long millis2 = System.currentTimeMillis();
            logger.info("ANNFileDownloadServlet 文档下载结束，下载文件名称："+filename
            		+" 共下载："+data+"字节.耗时："+(millis2-millis1));
            logger.info("doPost(HttpServletRequest request, HttpServletResponse response) ANNFileDownloadServlet end");
		} catch (Exception e) {
			logger.error("下载错误："+e.getMessage());
			e.printStackTrace();
		}finally {
        	outputStream.flush();
        	inputStream.close();
        	outputStream.close();
		}
    	
       
	}

}
