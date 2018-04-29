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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChartUtil {

	static String imgpath = "./testBar.png";
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
		CategoryDataset dataset = createDataset();
		// \u521B\u5EFA\u56FE\u5F62\u5BF9\u8C61
		JFreeChart jfreechart = ChartFactory.createBarChart(
				"08\u5E74\u56FE\u4E66\u9500\u552E\u91CF\u6392\u540D",
				"\u6309\u5B63\u5EA6", "\u9500\u91CF", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		// \u83B7\u5F97\u56FE\u8868\u533A\u57DF\u5BF9\u8C61
		CategoryPlot categoryPlot = (CategoryPlot) jfreechart.getPlot();
		// \u8BBE\u7F6E\u7F51\u683C\u7EBF\u53EF\u89C1
		categoryPlot.setDomainGridlinesVisible(true);
		// \u83B7\u5F97x\u8F74\u5BF9\u8C61
		CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
		// \u8BBE\u7F6Ex\u8F74\u663E\u793A\u7684\u5206\u7C7B\u540D\u79F0\u7684\u663E\u793A\u4F4D\u7F6E\uFF0C\u5982\u679C\u4E0D\u8BBE\u7F6E\u5219\u6C34\u5E73\u663E\u793A
		// \u8BBE\u7F6E\u540E\uFF0C\u53EF\u4EE5\u659C\u50CF\u663E\u793A\uFF0C\u4F46\u5206\u7C7B\u89D2\u5EA6\uFF0C\u56FE\u8868\u7A7A\u95F4\u6709\u9650\u65F6\uFF0C\u5EFA\u8BAE\u91C7\u7528
		// categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
		// .createUpRotationLabelPositions(0.39269908169872414D));
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(0.39269908169872414D));

		categoryAxis.setCategoryMargin(0.0D);
		// \u83B7\u663E\u793A\u56FE\u5F62\u5BF9\u8C61
		BarRenderer barRenderer3d = (BarRenderer) categoryPlot.getRenderer();
		// \u8BBE\u7F6E\u4E0D\u663E\u793A\u8FB9\u6846\u7EBF
		barRenderer3d.setDrawBarOutline(false);
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
	private static CategoryDataset createDataset() {
		// \u65F6\u95F4\u7EF4\u5EA6
		String[] category1 = { "\u7B2C\u4E00\u5B63\u5EA6",
				"\u7B2C\u4E8C\u5B63\u5EA6", "\u7B2C\u4E09\u5B63\u5EA6",
				"\u7B2C\u56DB\u5B63\u5EA6" };
		// \u5206\u7C7B\u7EF4\u5EA6
		String[] category2 = { "JAVA", "C/C++", "PHP", "C#", "Object C" };
		DefaultCategoryDataset defaultdataset = new DefaultCategoryDataset();
		for (int i = 0; i < category1.length; i++) {
			String category = category1[i];
			for (int j = 0; j < category2.length; j++) {
				String cat = category2[j];
				// \u6A21\u62DF\u6DFB\u52A0\u6570\u636E
				defaultdataset.addValue(DataUtils.getRandomData(), cat,
						category);
			}
		}
		return defaultdataset;
	}

	public static void main(String[] args) throws  IOException {
		JFrame frame = new JFrame("Test Chart");
		BarChartUtil.createBar();
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(imgpath));
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
	}
}
