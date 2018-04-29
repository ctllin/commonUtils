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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class AreaChartUtil {
    /**
     * 
     * @param imgPath \u751F\u6210\u7684\u6587\u4EF6\u8DEF\u5F84\u4F8B\u5982c:/tmpArea.png
     * @return
     * @throws
     * @throws IOException
     */
	public static Image createArea(String imgPath) throws  IOException {
		if(null==imgPath){
			imgPath = "./tmpArea.png";
		}
		OutputStream out = new FileOutputStream(imgPath);
		InputStream in = new FileInputStream(imgPath);
		// \u521B\u5EFA\u4E3B\u9898\u6837\u5F0F
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// \u8BBE\u7F6E\u6807\u9898\u5B57\u4F53
		standardChartTheme.setExtraLargeFont(new Font("\u96B6\u4E66", Font.BOLD, 20));
		// \u8BBE\u7F6E\u56FE\u4F8B\u7684\u5B57\u4F53
		standardChartTheme.setRegularFont(new Font("\u5B8B\u4E66", Font.PLAIN, 15));
		// \u8BBE\u7F6E\u8F74\u5411\u7684\u5B57\u4F53
		standardChartTheme.setLargeFont(new Font("\u5B8B\u4E66", Font.PLAIN, 15));
		// \u5E94\u7528\u4E3B\u9898\u6837\u5F0F
		ChartFactory.setChartTheme(standardChartTheme);
		// \u83B7\u53D6\u6570\u636E\u96C6\u5BF9\u8C61
		CategoryDataset dataset = createDataset();
		// \u521B\u5EFA\u56FE\u5F62\u5BF9\u8C61
		JFreeChart jfreechart = ChartFactory.createAreaChart("08\u5E74\u56FE\u4E66\u9500\u552E\u91CF\u6392\u540D",
				"\u6309\u5B63\u5EA6", "\u9500\u91CF", dataset, PlotOrientation.VERTICAL, true, true,
				false);
		// \u83B7\u5F97\u56FE\u8868\u533A\u57DF\u5BF9\u8C61
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		// \u8BBE\u7F6E\u524D\u666F\u8272\u4E3A50%\u900F\u660E
		categoryplot.setForegroundAlpha(0.5F);
		// \u8BBE\u7F6EX\u8F74\u504F\u79FB\u91CF
		categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		// \u8BBE\u7F6E\u5317\u666F\u8272\u4E3A\u6D45\u7070\u8272
		categoryplot.setBackgroundPaint(Color.lightGray);
		// \u8BBE\u7F6E\u663E\u793A\u7F51\u683C\u7EBF
		categoryplot.setDomainGridlinesVisible(true);
		// \u8BBE\u7F6E\u7F51\u683C\u7EBF\u4E3A\u767D\u8272
		categoryplot.setDomainGridlinePaint(Color.white);
		// \u8BBE\u7F6E\u663E\u793A\u8FB9\u754C\u7EBF
		categoryplot.setRangeGridlinesVisible(true);
		// \u8BBE\u7F6E\u663E\u793A\u8FB9\u754C\u7EBF\u4E3A\u767D\u8272
		categoryplot.setRangeGridlinePaint(Color.white);
		// \u83B7\u5F97X\u8F74\u5BF9\u8C61
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		// \u8BBE\u7F6EX\u8F74\u6807\u7B7E\u4F4D\u7F6E\u4E3A45\u5EA6\u89D2
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// \u8BBE\u7F6E\u4E0EX\u8F74\u7684\u8865\u767D\u4E3A0
		categoryaxis.setLowerMargin(0.0D);
		categoryaxis.setUpperMargin(0.0D);

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
		String[] category1 = { "\u7B2C\u4E00\u5B63\u5EA6", "\u7B2C\u4E8C\u5B63\u5EA6", "\u7B2C\u4E09\u5B63\u5EA6", "\u7B2C\u56DB\u5B63\u5EA6" };
		// \u5206\u7C7B\u7EF4\u5EA6
		String[] category2 = { "JAVA", "C/C++", "PHP" };
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
		Image img = AreaChartUtil.createArea(null);
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(img));
		frame.add(label);
		frame.pack();
		frame.setVisible(true);
	}
}
