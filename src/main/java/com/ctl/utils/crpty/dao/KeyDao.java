package com.ctl.utils.crpty.dao;
public interface KeyDao {
	public void createKey(String keyName) throws Exception;
	public boolean checkcard(String banknumber);
}
