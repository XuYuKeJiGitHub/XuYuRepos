package com.xuyurepos.service.payment;

import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.page.PageModel;

public interface XuyuPayaddressService {

	public void saveInfo()throws CustomException;

	@SuppressWarnings("rawtypes")
	public void findList(PageModel pageModel);

	public void outputCaptcha(String url, HttpServletResponse response)throws WriterException;

}
