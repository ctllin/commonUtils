package com.ctl.utils;

import java.io.*;
import java.util.prefs.*;

public class RegisterUtil {

	/**
	 * @category systemRoot HKEY_LOCAL_MACHINE%Software%JavaSoft%prefs\u4E0B\u5199\u5165\u6CE8\u518C\u8868\u503C
	 * @param key
	 * @param String
	 *            value
	 * @param nodeDir\u5728prefs\u4E0B\u9762\u7684\u67D0\u4E2A\u4EBA\u76EE\u5F55
	 *            \u4F8B\u5982/com/ctl/util
	 */
	public static void putStringSystemRoot(String key, String value,
			String nodeDir) {
		if ("".equals(nodeDir) || null == nodeDir) {
			nodeDir = "/ctl";
		}
		Preferences pre = Preferences.systemRoot().node(nodeDir);
		pre.put(key, value);
		// prefsdemo.
		/**//* \u5BFC\u51FA\u5230XML\u6587\u4EF6 */
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("H:/registerSrc.xml");
			pre.exportNode(fos);
			fos.close();
		} catch (Exception e) {
			System.err.println(" Cannot export nodes:  " + e);
		}
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684\u8DEF\u5F84
	 * @category systemRoot HKEY_LOCAL_MACHINE%Software%JavaSoft%prefs\u4E0B\u5199\u5165\u6CE8\u518C\u8868\u503C
	 * @param key
	 * @param String
	 *            value
	 */
	public static void putStringSystemRoot(String key, String value) {
		putStringSystemRoot(key, value, null);
	}

	/**
	 * @category systemRoot HKEY_LOCAL_MACHINE%Software%JavaSoft%prefs\u4E0B\u5199\u5165\u6CE8\u518C\u8868\u503C
	 * @param key
	 * @param int value
	 * @param nodeDir\u5728prefs\u4E0B\u9762\u7684\u67D0\u4E2A\u4EBA\u76EE\u5F55
	 *            \u4F8B\u5982/com/ctl/util
	 */
	public static void putIntSystemRoot(String key, int value, String nodeDir) {
		if ("".equals(nodeDir) || null == nodeDir) {
			nodeDir = "/ctl";
		}
		Preferences pre = Preferences.systemRoot().node(nodeDir);
		pre.putInt(key, value);
		try {
			FileOutputStream fos = new FileOutputStream("registerSrc.xml");
			pre.exportNode(fos);
			fos.close();
		} catch (Exception e) {
			System.err.println(" Cannot export nodes:  " + e);
		}

	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684\u8DEF\u5F84
	 * @category systemRoot HKEY_LOCAL_MACHINE%Software%JavaSoft%prefs\u4E0B\u5199\u5165\u6CE8\u518C\u8868\u503C
	 * @param key
	 * @param int value
	 * @param nodeDir\u5728prefs\u4E0B\u9762\u7684\u67D0\u4E2A\u4EBA\u76EE\u5F55
	 *            \u4F8B\u5982/com/ctl/util
	 */
	public static void putIntSystemRoot(String key, int value) {
		putIntSystemRoot(key, value, null);
	}

	/**
	 * 
	 * @param key
	 *            \u8981\u67E5\u627E\u7684\u503C\u7684\u952E
	 * @param def
	 *            \u4E0D\u5B58\u5728\u65F6\u8FD4\u56DE def
	 * @param nodeDir
	 *            \u8DEF\u5F84
	 * @return
	 */
	public static int getIntFromSystemRoot(String key, int def, String nodeDir) {
		if ("".equals(nodeDir) || null == nodeDir) {
			nodeDir = "/ctl";
		}
		Preferences pre = Preferences.systemRoot().node(nodeDir);
		return pre.getInt(key, def);
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684node\u8DEF\u5F84
	 * @param key
	 * @param def
	 * @return
	 */
	public static int getIntFromSystemRoot(String key, int def) {
		return getIntFromSystemRoot(key, def, null);
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684 \u9519\u8BEF\u8FD4\u56DE\u503C
	 * @param key
	 * @param def
	 * @return
	 */
	public static int getIntFromSystemRoot(String key, String nodeDir) {
		return getIntFromSystemRoot(key, -10000, nodeDir);

	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684 \u9519\u8BEF\u8FD4\u56DE\u503C \u548Cnode\u8DEF\u5F84
	 * @param key
	 * @return
	 */
	public static int getIntFromSystemRoot(String key) {
		return getIntFromSystemRoot(key, -10000, null);

	}

	/**
	 * 
	 * @param key
	 *            \u8981\u67E5\u627E\u7684\u503C\u7684\u952E
	 * @param def
	 *            \u4E0D\u5B58\u5728\u65F6\u8FD4\u56DE def
	 * @param nodeDir
	 *            \u8DEF\u5F84
	 * @return
	 */
	public static String getStringFromSystemRoot(String key, String def,
			String nodeDir) {
		if ("".equals(nodeDir) || null == nodeDir) {
			nodeDir = "/ctl";
		}
		if ("".equals(def) || null == def) {
			def = "\u4E0D\u5B58\u5728";
		}

		Preferences pre = Preferences.systemRoot().node(nodeDir);
		return pre.get(key, def);
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684 node\u8DEF\u5F84
	 * @param key
	 * @param def
	 * @return
	 */
	public static String getStringFromSystemRoot_NodeDir(String key, String def) {
		return getStringFromSystemRoot(key, def, null);
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684 \u9519\u8BEF\u8FD4\u56DE\u503C
	 * @param key
	 * @param nodeDir
	 * @return
	 */
	public static String getStringFromSystemRoot_Def(String key, String nodeDir) {
		return getStringFromSystemRoot(key, null, nodeDir);
	}

	/**
	 * @category \u4F7F\u7528\u9ED8\u8BA4\u7684 \u9519\u8BEF\u8FD4\u56DE\u503C \u548Cnode\u8DEF\u5F84
	 * @param key
	 * @return
	 */
	public static String getStringFromSystemRoot(String key) {
		return getStringFromSystemRoot(key, null, null);
	}

	/**
	 * @category \u5220\u9664\u952E \u4E3Akey \u7684\u6CE8\u518C\u9879
	 * @param key
	 * @param nodeDir
	 *            \u8981\u5220\u9664\u7684\u9879\u7684\u76EE\u5F55
	 */
	public static void removeKeySystemRoot(String key, String nodeDir) {
		if ("".equals(nodeDir) || null == nodeDir) {
			nodeDir = "/ctl";
		}
		Preferences pre = Preferences.systemRoot().node(nodeDir);
		pre.remove(key);
	}

	/**
	 * @category \u5220\u9664\u952E \u4E3Akey \u7684\u6CE8\u518C\u9879 \u4F7F\u7528\u9ED8\u8BA4\u76EE\u5F55
	 * @param key  \u8981\u5220\u9664\u7684\u9879\u7684\u76EE\u5F55
	 *           
	 */
	public static void removeKeySystemRoot(String key) {
		removeKeySystemRoot(key, null);
	}

	/**
	 * @category \u5220\u9664\u8282\u70B9 \u8DEF\u5F84\u5982\u4E0B /test
	 * @param nodeDir
	 */
	public static void deleteNodeSystemRoot(String nodeDir){
		if ("".equals(nodeDir) || null == nodeDir) {
			return;
		}
		Preferences pre = Preferences.systemRoot();
		try {
			pre.node(nodeDir).removeNode();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		// RegisterUtil.putIntSystemRoot("times", 3,"");
		//System.out.println(RegisterUtil.getIntFromSystemRoot("times", -1, ""));
		//RegisterUtil.removeKeySystemRoot("")
		RegisterUtil.deleteNodeSystemRoot("/ctl");
	}
}
