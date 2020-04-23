package com.xuyurepos.controller.cardmanager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuyurepos.common.exception.BizException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.util.FileUtil;
import com.xuyurepos.service.impl.manager.ImportTempExcelParse;
import com.xuyurepos.service.manager.ImportTempService;
import com.xuyurepos.service.manager.XuyuContentCardInfoImportService;
import com.xuyurepos.vo.system.SystemFunctionVo;

@Controller
@RequestMapping(value = "/cardmanager")
public class CardManagerControl {
	@Autowired
	private ImportTempService importTempService; 
	@Autowired
	private  XuyuContentCardInfoImportService xuyuContentCardInfoImportService;
	
	@Autowired
	private ImportTempExcelParse importTempExcelParse;
	
	@Autowired
	private BizException bizException; 
	
	Logger log=LoggerFactory.getInstance().getLogger(CardManagerControl.class);
	
//	@RequestMapping(value = "/importTemp", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public String importTemp(){
//		try {
//			Map<String, Object> map = new HashMap<String, Object>();
//			// 数据导入库里面
//			 List<File> files = FileUtil.searchFiles(new File("C:\\Users\\yangfei\\Desktop\\旭宇科技\\初始化数据\\最后一批数据"), ".xlsx");
//	        System.out.println("共找到:" + files.size() + "个文件");
//	        for (File file : files) {
//	            importTempExcelParse.anaLysisData(file.getAbsolutePath());
//	        }
//			
//			map.put("sucess", true);
//			String result = JSONObject.toJSONString(map);
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	
//	}
	
	// 保存
	@RequestMapping(value = "/excelImport", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String save(SystemFunctionVo systemLookUpVo, HttpServletRequest request) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			importTempService.anaLysisData("");
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 调整后台备注
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setMark", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public  String setMark(HttpServletRequest request,HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xuyuContentCardInfoImportService.setMark(request);
			map.put("sucess", true);
			String result = JSONObject.toJSONString(map);
			return result;
		}catch (Exception e) {
			bizException.resolveException(request, response, 1011, e);
			return null;
		}
	}

}
