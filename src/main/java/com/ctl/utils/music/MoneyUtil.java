package com.ctl.utils.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MoneyUtil {
	static Logger logger = LoggerFactory.getLogger(MoneyUtil.class);
	public static String TransToRMB(String money) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		return TransToRMB(money,true);
	}
	/**
	 * @description \u4EBA\u6C11\u5E01\u6570\u5B57\u91D1\u989D\u8F6C\u5927\u5199\u91D1\u989D
	 * @param money \u6570\u5B57\u91D1\u989D\u4F8B\u5982123434.11
	 * @return
	 * @throws LineUnavailableException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public static String TransToRMB(String money,boolean isAudio) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		int index = money.indexOf(".");
		if (index < 0) {// \u6CA1\u6709\u89D2\u5206
			money = money + ".00";
			index = money.indexOf(".");
		}
		if (money.substring(index, money.length()).length() < 3) {// \u6CA1\u6709\u5206
			money = money + "0";
			index = money.indexOf(".");
		}
		logger.info("小写金额:"+money);
		money = money.replaceAll("\\D", "");// \u53BB\u9664"."
		int length = money.length();
		// \u8D27\u5E01\u5927\u5199\u5F62\u5F0F
		String bigLetter[] = { "\u96F6", "\u58F9", "\u8D30", "\u53C1", "\u8086", "\u4F0D", "\u9646", "\u67D2", "\u634C", "\u7396" };
		// \u8D27\u5E01\u5355\u4F4D
		String unit[] = { "\u4EDF", "\u4F70", "\u62FE", "\u4E07", "\u4EDF", "\u4F70", "\u62FE", "\u4EBF", "\u4EDF", "\u4F70","\u62FE", "\u4E07", "\u4EDF", "\u4F70", "\u62FE", "\u5143", "\u89D2", "\u5206" };
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < length; i++) {
			int num = Integer.parseInt(String.valueOf(money.charAt(i)));
			buf.append(bigLetter[num]);
			buf.append(unit[unit.length - money.length() + i]);
		}
		String moneyTmp = buf.toString();
		buf = null;
		for (int i = 0; i < 4; i++) {
			// \u4EBF \u4E07 \u5143\u662F\u56DB\u4E2A\u4E3A\u5355\u5143\u7684\u6700\u540E\u4E00\u4F4D\u4F8B\u59821234(\u4EBF)4567(\u4E07)7891(\u5143)
			moneyTmp = moneyTmp.replaceAll("\u96F6\u4EBF", "\u4EBF");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u4E07", "\u4E07");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u4EDF", "\u96F6");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u4F70", "\u96F6");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u62FE", "\u96F6");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u5143", "\u5143");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u89D2", "\u96F6");
			moneyTmp = moneyTmp.replaceAll("\u96F6\u5206", "\u6574");
		}
		moneyTmp = moneyTmp.replaceAll("\u4EBF\u4E07", "\u4EBF");
		moneyTmp = moneyTmp.replaceAll("\u62FE\u5143", "\u62FE\u5143\u96F6");
		moneyTmp = moneyTmp.replaceAll("[\u96F6]{1,}", "\u96F6");// \u591A\u4E2A\u8FDE\u7EED\u7684\u96F6\u66FF\u6362\u4E3A\u4E00\u4E2A\u96F6
		moneyTmp = moneyTmp.replaceAll("\u96F6\u6574", "\u6574");
		logger.info("大写金额:"+moneyTmp);
		if(isAudio){
			String path=MoneyUtil.class.getClassLoader().getResource("./").getPath()+"musicres/";
			//System.out.println(path);
			MusicUtil.play(URLDecoder.decode(path,"utf-8")+"RMB.WAV");
			String strs[]=moneyTmp.split("");
			for(int i=0;i<strs.length;i++ ){
				String filePath=path+strs[i]+".WAV";
				MusicUtil.play(URLDecoder.decode(filePath,"utf-8"));
				//logger.info(filePath);
			}
		}
		return moneyTmp;
	}

}
