package com.xuyurepos.service.impl.payment;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.xuyurepos.common.exception.CustomException;
import com.xuyurepos.common.log.LoggerFactory;
import com.xuyurepos.common.page.PageModel;
import com.xuyurepos.common.util.UUIDUtils;
import com.xuyurepos.dao.payment.XuyuPayaddressDao;
import com.xuyurepos.entity.payment.XuyuPayaddress;
import com.xuyurepos.entity.system.SystemUser;
import com.xuyurepos.service.payment.XuyuPayaddressService;	
/**
 * 充值链接生成
 * @author yangfei
 *
 */
@Service
@Transactional
public class XuyuPayaddressServiceImpl implements XuyuPayaddressService{
	
	Logger logger=LoggerFactory.getInstance().getLogger(XuyuPayaddressServiceImpl.class);
	
	private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
	
	@Autowired
	private  XuyuPayaddressDao xuyuPayaddressDao;
    
	/**
	 * 生成充值链接
	 * @throws CustomException 
	 */
	@Override
	public void saveInfo() throws CustomException {
		try {
			XuyuPayaddress xuyuPayaddress=new XuyuPayaddress();
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SystemUser systemUser=(SystemUser) request.getSession().getAttribute("systemUser");
			xuyuPayaddress.setAgencyId(systemUser.getOrgId());
			xuyuPayaddress.setUuid(UUIDUtils.getUUID());
			xuyuPayaddress.setAddress("https://iots.shingsou.com/wxpay/index.html?id="+xuyuPayaddress.getUuid());
			xuyuPayaddress.setType("01");
			xuyuPayaddressDao.insert(xuyuPayaddress);
		}catch (DuplicateKeyException e) {
			throw new CustomException("充值链接已经生成请勿重复操作");
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
    
	/**
	 * 查询
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void findList(PageModel pageModel) {
		try {
			pageModel.setRows(xuyuPayaddressDao.selectUserListWithPage(pageModel));
		    pageModel.setTotal(xuyuPayaddressDao.selectUserCountWithPage(pageModel));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void outputCaptcha(String url, HttpServletResponse response) throws WriterException {
		ServletOutputStream sos = null;
		try {
            int width = 600;
            int height = 600;
            // 二维码图片格式
            String format = "gif";
            // 设置编码，防止中文乱码
            Hashtable<EncodeHintType, Object> ht = new Hashtable<EncodeHintType, Object> ();
            ht.put (EncodeHintType.CHARACTER_SET, "UTF-8");
            // 设置二维码参数(编码内容，编码类型，图片宽度，图片高度, 编码格式)
            BitMatrix bitMatrix = new MultiFormatWriter ().encode (url, BarcodeFormat.QR_CODE, width, height, ht);
            // 生成二维码(定义二维码输出服务器路径)
            int b_width = bitMatrix.getWidth ();
            int b_height = bitMatrix.getHeight ();
            // 建立图像缓冲器
            BufferedImage image = new BufferedImage (b_width, b_height, BufferedImage.TYPE_3BYTE_BGR);
            for ( int x = 0; x < b_width; x++ )
            {
                for ( int y = 0; y < b_height; y++ )
                {
                    image.setRGB (x, y, bitMatrix.get (x, y) ? BLACK : WHITE);
                }
            }
			sos = response.getOutputStream();
			ImageIO.write(image, "jpeg", sos);
			sos.flush();
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sos != null) {
					sos.close();
				}
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}
