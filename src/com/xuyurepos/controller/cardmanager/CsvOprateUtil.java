package com.xuyurepos.controller.cardmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import com.xuyurepos.controller.system.SystemUserControl;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.autorule.AotoRuleManagerService;
import com.xuyurepos.service.logger.LoggerInfoService;
import com.xuyurepos.service.quartz.QuartzJobServer;
import com.xuyurepos.vo.autorule.AutoRuleLogDTO;
import com.xuyurepos.vo.common.UploadFileDto;
import com.xuyurepos.vo.logger.LoggerInfoDto;
import com.xuyurepos.vo.quartzJob.QuartzJobDto;

@Controller
@RequestMapping(value = "/csvoprate")
public class CsvOprateUtil {

	private static final String UPLOAD_FAILE="{sucess:false,mess:'文件上传失败'}";
	private static final String UPLOAD_SUCESS="{sucess:true,mess:'文件上传成功'}";
	
	Logger logger=LoggerFactory.getInstance().getLogger(SystemUserControl.class);
	
    @Autowired
	QuartzJobServer quartzJobServer;
    @Autowired
    LoggerInfoService loggerInfoService;
    @Autowired
	private AotoRuleManagerService aotoRuleManagerService; 
	@Autowired
	private BizException bizException;

	Logger log=LoggerFactory.getInstance().getLogger(CsvOprateUtil.class);
	
	// 下载模板
	@RequestMapping(value = "/export", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void export(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			
			String fileName=request.getParameter("filename");
			String exportType=request.getParameter("exportType");
			response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF8")+".csv");
			StringBuffer sb=new StringBuffer();
			OutputStream outs = response.getOutputStream();
			outs.write(generateExportHead(sb,exportType,request));
			sb.setLength(0);
			outs.flush();
			outs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据下载类型拼接模板头文件
	 * 
	 */
	public byte[]  generateExportHead(StringBuffer sb,String exportType,HttpServletRequest request) {
		 byte[] buff = null;
		try {
			if(exportType.equals("autoRuleLogExport")){
				String[] csvHead= {"规则名称","群组","接入号","长号","短号","运营商","地区","通讯状态","处理状态","处理方式"};
				String headName="自动化规则执行日志";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				String ruleNameQuery=request.getParameter("ruleNameQuery");
				String startDate=request.getParameter("startDate");
				String endDate=request.getParameter("endDate");
				String operateType=request.getParameter("operateType");
				String isDeal=request.getParameter("isDeal");
				Map map=new HashMap();
				map.put("ruleNameQuery", ruleNameQuery);
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				map.put("operateType", operateType);
				map.put("isDeal", isDeal);
				List<AutoRuleLogDTO> list=aotoRuleManagerService.getRuleLogInfo(map);
				if(list!=null&&!list.isEmpty()) {
					int length1=list.size();
					AutoRuleLogDTO autoRuleLogDTO=null;
					for(int i=0;i<length1;i++) {
						sb.append("\n");
						autoRuleLogDTO=new AutoRuleLogDTO();
						autoRuleLogDTO=list.get(i);
						sb.append(autoRuleLogDTO.getRuleName()).append(",");
						sb.append(autoRuleLogDTO.getOwnerId()).append(",");
						sb.append(autoRuleLogDTO.getAccessNum()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getIccid()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getImsi()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getProvider()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlace()).append(",");
						sb.append(autoRuleLogDTO.getWorkingCondition()).append(",");
						sb.append(autoRuleLogDTO.getIsDeal()).append(",");
						sb.append(autoRuleLogDTO.getOperateType()).append(",");
					}
				}
				 	buff=sb.toString().getBytes("gbk");
			}else if(exportType.equals("comboDiffExport")){
				String[] csvHead= {"群组","接入号","长号","短号","运营商","地区","代理商","通讯状态","流量用量","套餐","流量月份"};
				String headName="月度流量与套餐差异信息";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				String provider=request.getParameter("providerQuery");
				String ownerPlace=request.getParameter("ownerPlace");
				String ownerId=request.getParameter("ownerId");
				String accessNum=request.getParameter("accessNum");
				String agency=request.getParameter("agency");
				String comboType=request.getParameter("comboType");
				String useGprs=request.getParameter("useGprs");
				Map map=new HashMap();
				map.put("provider", provider);
				map.put("ownerPlace", ownerPlace);
				map.put("ownerId", ownerId);
				map.put("accessNum", accessNum);
				map.put("agency", agency);
				map.put("comboType", comboType);
				map.put("useGprs", useGprs);
				List<AutoRuleLogDTO> list=aotoRuleManagerService.getcomboDiffInfo(map);
				if(list!=null&&!list.isEmpty()) {
					int length1=list.size();
					AutoRuleLogDTO autoRuleLogDTO=null;
					for(int i=0;i<length1;i++) {
						sb.append("\n");
						autoRuleLogDTO=new AutoRuleLogDTO();
						autoRuleLogDTO=list.get(i);
						sb.append(autoRuleLogDTO.getOwnerId()).append(",");
						sb.append(autoRuleLogDTO.getAccessNum()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getIccid()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getImsi()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getProvider()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlace()).append(",");
						sb.append(autoRuleLogDTO.getAgency()).append(",");
						sb.append(autoRuleLogDTO.getWorkingCondition()).append(",");
						sb.append(autoRuleLogDTO.getUseGprs()).append(",");
						sb.append(autoRuleLogDTO.getComboName()).append(",");
						sb.append(autoRuleLogDTO.getQueryDate()).append(",");
					}
				}
				 	buff=sb.toString().getBytes("gbk");
			}else if(exportType.equals("deadLineExport")){
				String[] csvHead= {"群组","接入号","长号","短号","运营商","地区","代理商","通讯状态","满期日期","沉默期到期日期","实际沉默期到期日期"};
				String headName="满期日到期物联卡信息";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				String provider=request.getParameter("providerQuery");
				String ownerPlace=request.getParameter("ownerPlace");
				String ownerId=request.getParameter("ownerId");
				String accessNum=request.getParameter("accessNum");
				String agency=request.getParameter("agency");
				String startDDate=request.getParameter("startDDate");
				String endDDate=request.getParameter("endDDate");
				String startWDate=request.getParameter("startWDate");
				String endWDate=request.getParameter("endWDate");
				String startRWDate=request.getParameter("startRWDate");
				String endRWDate=request.getParameter("endRWDate");
				Map map=new HashMap();
				map.put("provider", provider);
				map.put("ownerPlace", ownerPlace);
				map.put("ownerId", ownerId);
				map.put("accessNum", accessNum);
				map.put("agency", agency);
				map.put("startDDate", startDDate);
				map.put("endDDate", endDDate);
				map.put("startWDate", startDDate);
				map.put("startWDate", endDDate);
				map.put("startRWDate", startDDate);
				map.put("endRWDate", endDDate);
				List<AutoRuleLogDTO> list=aotoRuleManagerService.getDeadLineInfo(map);
				if(list!=null&&!list.isEmpty()) {
					int length1=list.size();
					AutoRuleLogDTO autoRuleLogDTO=null;
					for(int i=0;i<length1;i++) {
						sb.append("\n");
						autoRuleLogDTO=new AutoRuleLogDTO();
						autoRuleLogDTO=list.get(i);
						sb.append(autoRuleLogDTO.getOwnerId()).append(",");
						sb.append(autoRuleLogDTO.getAccessNum()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getIccid()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getImsi()).append("\t").append(",");
						sb.append(autoRuleLogDTO.getProvider()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlace()).append(",");
						sb.append(autoRuleLogDTO.getAgency()).append(",");
						sb.append(autoRuleLogDTO.getWorkingCondition()).append(",");
						sb.append(autoRuleLogDTO.getDeadLineDate()).append(",");
						sb.append(autoRuleLogDTO.getWaitDate()).append(",");
						sb.append(autoRuleLogDTO.getRealWaitDate()).append(",");
					}
				}
				 	buff=sb.toString().getBytes("gbk");
			}else if(exportType.equals("ownerInfoExport")){

				String[] csvHead= {"运营商","地区","群组","套餐类型","入池卡数","当月流量用量","当月流量总量","流量使用百分比"};
				String headName="满期日到期物联卡信息";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				String provider=request.getParameter("providerQuery");
				String ownerPlace=request.getParameter("ownerPlace");
				String comboName=request.getParameter("comboName");
				String agency=request.getParameter("agency");
				Map map=new HashMap();
				map.put("provider", provider);
				map.put("ownerPlace", ownerPlace);
				map.put("comboName", comboName);
				map.put("agency", agency);
				List<AutoRuleLogDTO> list=aotoRuleManagerService.getOwnerInfo(map);
				if(list!=null&&!list.isEmpty()) {
					int length1=list.size();
					AutoRuleLogDTO autoRuleLogDTO=null;
					for(int i=0;i<length1;i++) {
						sb.append("\n");
						autoRuleLogDTO=new AutoRuleLogDTO();
						autoRuleLogDTO=list.get(i);
						sb.append(autoRuleLogDTO.getProvider()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlace()).append(",");
						sb.append(autoRuleLogDTO.getOwnerName()).append(",");
						sb.append(autoRuleLogDTO.getComboName()).append(",");
						sb.append(autoRuleLogDTO.getActiveCount()).append(",");
						sb.append(autoRuleLogDTO.getUseGprs()).append(",");
						sb.append(autoRuleLogDTO.getTotalGprs()).append(",");
						sb.append(autoRuleLogDTO.getUseGprsPercent()).append(",");
					}
				}
				 	buff=sb.toString().getBytes("gbk");
			
			}else if(exportType.equals("yesterdayInfoExport")){
				String[] csvHead= {"群组代码","群组名称","接入号","长号","短号","运营商代码","运营商名称","地区代码","地区名称","客户代码","客户名称","通讯状态","通讯状态名称","当月流量","当日流量","流量使用日期"};
				String headName="当日流量使用情况";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				String provider=request.getParameter("providerQuery");
				String ownerPlace=request.getParameter("ownerPlace");
				String ownerId=request.getParameter("ownerId");
				String accessNum=request.getParameter("accessNum");
				String agency=request.getParameter("agency");
				String queryDate=request.getParameter("queryDate");
				String gprsYesterday=request.getParameter("gprsYesterday");
				Map map=new HashMap();
				map.put("provider", provider);
				map.put("ownerPlace", ownerPlace);
				map.put("ownerId", ownerId);
				map.put("accessNum", accessNum);
				map.put("agency", agency);
				map.put("queryDate", queryDate);
				map.put("gprsYesterday", gprsYesterday);
				List<AutoRuleLogDTO> list=aotoRuleManagerService.getGprsYesterdayInfo(map);
				if(list!=null&&!list.isEmpty()) {
					int length1=list.size();
					AutoRuleLogDTO autoRuleLogDTO=null;
					for(int i=0;i<length1;i++) {
						sb.append("\n");
						autoRuleLogDTO=new AutoRuleLogDTO();
						autoRuleLogDTO=list.get(i);
						sb.append(autoRuleLogDTO.getOwnerId()).append(",");
						sb.append(autoRuleLogDTO.getOwnerName()).append(",");
						sb.append(autoRuleLogDTO.getAccessNum()).append(",");
						sb.append(autoRuleLogDTO.getIccid()).append(",");
						sb.append(autoRuleLogDTO.getImsi()).append(",");
						sb.append(autoRuleLogDTO.getProvider()).append(",");
						sb.append(autoRuleLogDTO.getProviderName()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlace()).append(",");
						sb.append(autoRuleLogDTO.getOwnerPlaceName()).append(",");
						sb.append(autoRuleLogDTO.getAgency()).append(",");
						sb.append(autoRuleLogDTO.getAgencyName()).append(",");
						sb.append(autoRuleLogDTO.getWorkingCondition()).append(",");
						sb.append(autoRuleLogDTO.getWorkingConditionName()).append(",");
						sb.append(autoRuleLogDTO.getUseGprs()).append(",");
						sb.append(autoRuleLogDTO.getGprsYesterday()).append(",");
						sb.append(autoRuleLogDTO.getQueryDate()).append(",");
					}
				}
				 	buff=sb.toString().getBytes("gbk");
			
			}else {
				String[] csvHead= {"规则名称","群组","卡号","接入号","长号","短号","运营商","地区","通讯状态","处理状态","处理方式"};
				String headName="物联卡导入信息";
				int length=csvHead.length;
				sb.append("").append(",").append("")
					.append(",").append(headName)
					.append(",").append("\n");
				for(int i=0;i<length;i++) {
					sb.append(csvHead[i]).append(",");
				}
				  buff=sb.toString().getBytes("gbk");
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		return buff;
	}
	

	/**
	 * 文件上传
	 * @throws IOException 
	 */
	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public void upload(HttpServletRequest request,HttpServletResponse response,String operator,String ownerPlace,String asyncType) throws IOException {
       StringBuffer fileName=new StringBuffer();
       String isImport=request.getParameter("isImport");
       String uploadPath=request.getParameter("uploadPath");

       String comboType=request.getParameter("comboType");
       String waitType=request.getParameter("waitType");
       String testType=request.getParameter("testType");
       String cardType=request.getParameter("cardType");
       String standard=request.getParameter("standard");
       String unitCost=request.getParameter("unitCost");

       logger.info("operator:"+operator+";ownerPlace:"+ownerPlace+";asyncType:"+asyncType);
       logger.info("comboType:"+comboType+";waitType:"+waitType+";testType:"+testType);
       logger.info("cardType:"+cardType+";standard:"+standard+";unitCost:"+unitCost);

       UploadFileDto uploadFileDto=null;
       QuartzJobDto quartzJobDto=null;
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
		    String uploadBatchNo=null;
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
						 fileItem.write(new File(saveFilePath+fileName));
						 uploadBatchNo=loggerInfoService.getSequence("jobTaskSeq");
						 uploadFileDto=new UploadFileDto();
						 quartzJobDto =new QuartzJobDto();
						 uploadFileDto.setAnnexeId(uploadBatchNo);
						 uploadFileDto.setAnnexeSize(Double.valueOf(fileItem.getSize()));
						 uploadFileDto.setAnnexeType(name.substring(name.indexOf(".")));
						 uploadFileDto.setAnnexeName(simpleFileName);
						 uploadFileDto.setRelationMod(ownerPlace);
						 uploadFileDto.setUploadPath(saveFilePath+fileName);
						 uploadFileDto.setUploadUser(systemUser.getUserName());
						 uploadFileDto.setUploadUserName(systemUser.getCname());
						 uploadFileDto.setUploadBatchNo(uploadBatchNo);
						 uploadFileDto.setRelationInfo(operator);
						 uploadFileDto.setComboType(comboType);
						 uploadFileDto.setWaitType(waitType);
						 uploadFileDto.setTestType(testType);
						 uploadFileDto.setCardType(cardType);
						 uploadFileDto.setStandard(standard);
						 uploadFileDto.setUnitCost(unitCost);
						 //设置附件的ip地址
						 try {
							String address = InetAddress.getLocalHost().getHostAddress();
							logger.info("当前服务器IP地址：" + address);
							 uploadFileDto.setIp(address);
							 quartzJobDto.setIp(address);
						 }catch (Exception e) {
							e.printStackTrace();
						 }
						 logger.info("获取字段值"+uploadFileDto.getAnnexeId());
						 quartzJobServer.saveUploadFile(uploadFileDto);

						 quartzJobDto.setId(uploadBatchNo);
						 quartzJobDto.setAsyncBatchNo(uploadBatchNo);
						 if("cardImport".equals(asyncType)){
							 quartzJobDto.setAsyncType("cardImport");
							 quartzJobDto.setAsyncName("导卡");
						 }else{
							 quartzJobDto.setAsyncType("cardUpdate");
							 quartzJobDto.setAsyncName("数据更新");
						 }

						 quartzJobDto.setAsyncFlag("01");
						 quartzJobServer.saveAsyncJobTask(quartzJobDto);

						 LoggerInfoDto loggerInfo=new LoggerInfoDto();
							loggerInfo.setId(loggerInfoService.getSequence("seq"));
							if("cardImport".equals(asyncType)){
								loggerInfo.setModelId("cardImport");
								loggerInfo.setModelName("导卡");
								loggerInfo.setOpreate("import");
							}else{
								loggerInfo.setModelId("cardUpdate");
								loggerInfo.setModelName("数据更新");
								loggerInfo.setOpreate("update");
							}

							loggerInfo.setMark(uploadBatchNo);
							loggerInfo.setCreateUser(systemUser.getUserName());
							loggerInfoService.saveLogger(loggerInfo);
						 // 数据库操作
						 if(null==isImport){

						 }
						 out.print(UPLOAD_SUCESS);
//						 out.print("{success:true,realFileName:'"+fileName+"',fileFullName:'" + saveFilePath + File.separator + fileName + "'}");
					 }
				 }
			}
		    out.flush();
		    out.close();
	   } catch (Exception e) {
		   e.printStackTrace();
		   out.println(UPLOAD_FAILE);
		   out.close();
	   }
	}
	
/**
     * 导入
     * 
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }

}