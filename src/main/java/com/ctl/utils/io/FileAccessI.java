/**@author 		guolin
 *  @date    		2015-11-4����3:34:58
 *  @package_name NetFox
 *  @project_name   ctlUtils
 *  @version 		version.1.0
 */
package com.ctl.utils.io;

import java.io.*;

public class FileAccessI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3568154451225949879L;
	RandomAccessFile oSavedFile;
	long nPos;

	public FileAccessI() throws IOException {
		this("", 0);
	}

	public FileAccessI(String sName, long nPos) throws IOException {
		oSavedFile = new RandomAccessFile(sName, "rw");
		this.nPos = nPos;
		oSavedFile.seek(nPos);
	}

	public synchronized int write(byte[] b, int nStart, int nLen) {
		int n = -1;
		try {
			oSavedFile.write(b, nStart, nLen);
			n = nLen;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return n;
	}
}
