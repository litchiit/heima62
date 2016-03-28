package com.example.mobilesafe.domain;

public class UrlBean {
	/**
	 * json数据信息的封装
	 */
	private String url;
	private int versionCode;
	private String desc;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
