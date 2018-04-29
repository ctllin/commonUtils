package com.ctl.utils;

import java.awt.Color;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class BarChart2Util {

	static String imgpath = "./testBar2.png";
	static OutputStream out;
	static InputStream in;

	public static Image createBar() throws  IOException {
		out = new FileOutputStream(imgpath);
		in = new FileInputStream(imgpath);
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

		// \u83B7\u53D6\u6570\u636E\u96C6\u5BF9\u8C61
		IntervalXYDataset dataset = createDataset();
		// \u521B\u5EFA\u56FE\u5F62\u5BF9\u8C61
		JFreeChart jfreechart = ChartFactory.createXYBarChart(
				"08\u5E74\u56FE\u4E66\u9500\u552E\u91CF", null, false,
				"\u9500\u91CF", dataset, PlotOrientation.VERTICAL, true, false,
				false);
		// \u8BBE\u7F6E\u80CC\u666F\u4E3A\u767D\u8272
		jfreechart.setBackgroundPaint(Color.white);
		// \u83B7\u5F97\u56FE\u8868\u533A\u57DF\u5BF9\u8C61
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		// \u8BBE\u7F6E\u533A\u57DF\u5BF9\u8C61\u80CC\u666F\u4E3A\u7070\u8272
		xyplot.setBackgroundPaint(Color.lightGray);
		// \u8BBE\u7F6E\u533A\u57DF\u5BF9\u8C61\u7F51\u683C\u7EBF\u8C03\u4E3A\u767D\u8272
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		// \u83B7\u663E\u793A\u56FE\u5F62\u5BF9\u8C61
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
		// \u8BBE\u7F6E\u4E0D\u663E\u793A\u8FB9\u6846\u7EBF
		xybarrenderer.setDrawBarOutline(false);
		// \u5C06\u56FE\u8868\u5DF2\u6570\u636E\u6D41\u7684\u65B9\u5F0F\u8FD4\u56DE\u7ED9\u5BA2\u6237\u7AEF
		ChartUtils.writeChartAsPNG(out, jfreechart, 500, 270);
		out.close();
		Image img = ImageIO.read(in);
		in.close();
		return img;
	}

	/**
	 * \u8FD4\u56DE\u6570\u636E\u96C6
	 * 
	 * @return
	 */
	private static IntervalXYDataset createDataset() {
		// \u521B\u5EFA\u57FA\u672C\u6570\u636E
		XYSeries xyseries = new XYSeries("JAVA");
		XYSeries xyseries1 = new XYSeries("PHP");
		for (int i = 1; i <= 12; i++) {
			// \u6DFB\u52A0\u6570\u636E
			xyseries.add(i, DataUtils.getRandomData());
			xyseries1.add(i, DataUtils.getRandomData());
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		xyseriescollection.addSeries(xyseries1);
		return new XYBarDataset(xyseriescollection, 0.90000000000000002D);
	}

	public static void main(String[] args) throws  IOException {
		JFrame frame = new JFrame("Test Chart");
		BarChart2Util.createBar();
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(imgpath));
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
	}
}
