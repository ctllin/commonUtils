package com.ctl.utils.music;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicUtil {
	static Logger logger = LoggerFactory.getLogger(MusicUtil.class);
/**
 * 
 * @param filePath \u97F3\u9891\u6587\u4EF6\u7684\u4F4D\u7F6E
 * @throws UnsupportedAudioFileException
 * @throws IOException
 * @throws LineUnavailableException
 */
	public static void play(String filePath)throws UnsupportedAudioFileException, IOException,LineUnavailableException {
		// \u83B7\u53D6\u97F3\u9891\u8F93\u5165\u6D41
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
		// \u83B7\u53D6\u97F3\u9891\u7F16\u7801\u5BF9\u8C61
		AudioFormat audioFormat = audioInputStream.getFormat();
		// \u8BBE\u7F6E\u6570\u636E\u8F93\u5165
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,audioFormat, AudioSystem.NOT_SPECIFIED);
		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(audioFormat);
		sourceDataLine.start();
		/*
		 * \u4ECE\u8F93\u5165\u6D41\u4E2D\u8BFB\u53D6\u6570\u636E\u53D1\u9001\u5230\u6DF7\u97F3\u5668
		 */
		int count;
		byte tempBuffer[] = new byte[1024];
		while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
			if (count > 0) {
				sourceDataLine.write(tempBuffer, 0, count);
			}
		}
		// \u6E05\u7A7A\u6570\u636E\u7F13\u51B2,\u5E76\u5173\u95ED\u8F93\u5165
		sourceDataLine.drain();
		sourceDataLine.close();
	}

	public static void main(String[] args) throws LineUnavailableException,UnsupportedAudioFileException, IOException, URISyntaxException {
		String path=MusicUtil.class.getClassLoader().getResource("./").getPath()+"musicres/RMB.WAV";
		System.out.println(path);
		//MusicUtil.play(URLDecoder.decode(path,"utf-8"));
		MoneyUtil.TransToRMB("12345.56");
	}

}