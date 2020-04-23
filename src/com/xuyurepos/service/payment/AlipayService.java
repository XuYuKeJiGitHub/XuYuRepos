package com.xuyurepos.service.payment;

import java.util.Map;

import com.xuyurepos.common.exception.CustomException;

public interface AlipayService {

	Map<String, Object> checkNums(String accessNums)throws CustomException ;

	Map<String, Object> createOrder(String accessNums,String money)throws CustomException;

}
