package com.ctl.utils.crpty.dao;

public interface CryptDao {
	/**
	 * 加密class文件夹
	 */
	public boolean unCrptyFilter(String filePath);
	public void crypt();
	public void cryptFilePath(String filePath);
	public void cryptDircotryPath(String dirPath);
}
