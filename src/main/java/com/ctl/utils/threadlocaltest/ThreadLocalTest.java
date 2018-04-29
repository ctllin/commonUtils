package com.ctl.utils.threadlocaltest;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-1-25下午3:04:00
 * @package_name com.test
 * @project_name wastest
 * @version 	 version.1.0
 * @description	 
 */

public class ThreadLocalTest {

	/**@author 			guolin
	 * @date    		2016-1-25下午3:04:00
	 * @package_name 	com.test
	 * @project_name   	wastest
	 * @version 		version.1.0
	 * @description		
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadLocalValue tlv=new ThreadLocalValue();
		ClientThread th1=new ClientThread(tlv);
		ClientThread th2=new ClientThread(tlv);
		ClientThread th3=new ClientThread(tlv);
		new Thread(th1).start();
		new Thread(th2).start();
		new Thread(th3).start();
	}

}
