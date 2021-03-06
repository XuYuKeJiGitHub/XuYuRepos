package com.xuyurepos.entity.manager;

import java.io.Serializable;
/**
 * 后台首次导入数据
 * @author yangfei
 *
 */
@SuppressWarnings("serial")
public class QuartzImport implements Serializable{
	

	private int id;
	
	private String a;
	
	private String b;
	
	private String c;
	
	private String d;
	
	private String e;
	
	private String f;
	
	private String g;
	
	private String h;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	@Override
	public String toString() {
		return "ImportTemp [id=" + id + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f
				+ ", g=" + g + ", h=" + h + "]";
	}
	
}