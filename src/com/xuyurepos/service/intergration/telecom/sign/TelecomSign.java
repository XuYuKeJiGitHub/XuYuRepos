package com.xuyurepos.service.intergration.telecom.sign;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xuyurepos.service.intergration.telecom.util.TelecomCore;



@Service
public class TelecomSign  {

static	Logger logger=Logger.getLogger(TelecomSign.class);

	public static String sign(String[] strArray,String protal) {
		
		String sign="";
		
		try {

			
		    sign=	TelecomCore.buildMysign(strArray,protal);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
      return  sign;
	}

}
