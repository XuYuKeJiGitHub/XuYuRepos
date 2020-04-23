package com.wxpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyConfig extends  WXPayConfig{
private byte[] certData;
public String getAppID() {
    return "wx115e1f1585ba8a8f";
}
public String getMchID() {
    return "1352368801";
}
public String getKey() {
    return "5EF888F4DEA842E731DADAFA1A845589";
}
public MyConfig() throws Exception {
    String certPath = "/data/cert/apiclient_cert.p12";
    File file = new File(certPath);
    InputStream certStream = new FileInputStream(file);
    this.certData = new byte[(int) file.length()];
    certStream.read(this.certData);
    certStream.close();
}

public InputStream getCertStream() {
    ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
    return certBis;
}
public int getHttpConnectTimeoutMs() {
    return 8000;
}
public int getHttpReadTimeoutMs() {
    return 10000;
}
@Override
IWXPayDomain getWXPayDomain() {
	// TODO Auto-generated method stub
	MyDomain domain=new MyDomain();
	return domain;
}

  public static void main(String[] args) throws FileNotFoundException {
	    String certPath = "\\WEB-INF\\classes\\apiclient_cert.p12";
	    File file = new File(certPath);
	    InputStream certStream = new FileInputStream(file);
}

}
