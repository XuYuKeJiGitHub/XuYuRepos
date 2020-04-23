package com.xuyurepos.service.intergration.unicom;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

import com.xuyurepos.common.util.SecretUtils;
import com.xuyurepos.service.intergration.common.XuYuKey;




public class UnicomDesEncrypt {


    public static final String user_name="luying18001";
	

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String authBasic() {

        return    	SecretUtils.base64Encode(user_name+":"+XuYuKey.base64_key);

    }
    



}
