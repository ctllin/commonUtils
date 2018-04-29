package com.ctl.utils;

import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieUtil {
	static String imgpath = "./testPie.png";

	/**
	 * \u8FD4\u56DE\u6570\u636E\u96C6
	 * 
	 * @return
	 */
	private static PieDataset createPieDataset() {
		// \u521B\u5EFA\u997C\u5F62\u56FE\u6570\u636E\u96C6\u5BF9\u8C61
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		// \u5206\u522B\u56FE\u5F62\u533A\u57DF\u7684\u8BF4\u660E\u548C\u6570\u636E
		defaultpiedataset.setValue("JAVA", DataUtils.getRandomData());
		defaultpiedataset.setValue("C/C++", DataUtils.getRandomData());
		defaultpiedataset.setValue("PHP", DataUtils.getRandomData());
		defaultpiedataset.setValue("JavaScript", DataUtils.getRandomData());
		defaultpiedataset.setValue("Ajax", DataUtils.getRandomData());
		return defaultpiedataset;
	}

	public static Image createPie(String filePath) throws IOException {
		OutputStream out = new FileOutputStream(imgpath);
		InputStream in = new FileInputStream(imgpath);
		// \u521B\u5EFA\u4E3B\u9898\u6837\u5F0F
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// \u8BBE\u7F6E\u6807\u9898\u5B57\u4F53
		standardChartTheme.setExtraLargeFont(new Font("\u96B6\u4E66",
				Font.BOLD, 20));
		// \u8BBE\u7F6E\u56FE\u4F8B\u7684\u5B57\u4F53
		standardChartTheme.setRegularFont(new Font("\u5B8B\u4E66", Font.PLAIN,
				15));
		// \u8BBE\u7F6E\u8F74\u5411\u7684\u5B57\u4F53
		standardChartTheme
				.setLargeFont(new Font("\u5B8B\u4E66", Font.PLAIN, 15));
		// \u5E94\u7528\u4E3B\u9898\u6837\u5F0F
		ChartFactory.setChartTheme(standardChartTheme);

		PieDataset dataset = PieUtil.createPieDataset();
		// \u521B\u5EFA\u56FE\u5F62\u5BF9\u8C61
		JFreeChart jfreechart = ChartFactory.createPieChart3D("08\u5E74\u56FE\u4E66\u9500\u91CF\u6392\u884C\u699C",
				dataset, true, true, false);
		// \u83B7\u5F97\u56FE\u8868\u533A\u57DF\u5BF9\u8C61
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		// \u8BBE\u7F6E\u56FE\u8868\u533A\u57DF\u7684\u6807\u7B7E\u5B57\u4F53
		pieplot.setLabelFont(new Font("\u5B8B\u4F53", 0, 12));
		// \u8BBE\u7F6E\u56FE\u8868\u533A\u57DF\u65E0\u6570\u636E\u65F6\u7684\u9ED8\u8BA4\u663E\u793A\u6587\u5B57
		pieplot.setNoDataMessage("\u6CA1\u6709\u9500\u552E\u6570\u636E");
		// \u8BBE\u7F6E\u56FE\u8868\u533A\u57DF\u4E0D\u662F\u5706\u5F62\uFF0C\u7531\u4E8E\u662F3D\u7684\u997C\u5F62\u56FE\uFF0C\u5EFA\u8BAE\u8BBE\u7F6E\u4E3Afalse
		pieplot.setCircular(false);
		// \u8BBE\u7F6E\u56FE\u8868\u533A\u57DF\u6587\u5B57\u4E0E\u56FE\u8868\u533A\u57DF\u7684\u95F4\u9694\u8DDD\u79BB\uFF0C0.02\u8868\u793A2%
		pieplot.setLabelGap(0.02D);
		ChartUtils.writeChartAsPNG(out, jfreechart, 500, 270);
		out.close();
		Image img = ImageIO.read(in);
		in.close();
		return img;
	}

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Test Chart");
		Image img = PieUtil.createPie(imgpath);
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(img));
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
	}
}
