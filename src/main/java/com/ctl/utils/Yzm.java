package com.ctl.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class Yzm extends HttpServlet {
	private int HEIGHT = 30;
	private int WIDTH = 80;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("image/jpeg");
		String str = "0123456789abcdefghijklmnopqrstuvwxyz";
		char[] chs = str.toUpperCase().toCharArray();
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			buf.append(chs[random.nextInt(36)] + "");
		}

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		
		
		g.setColor(new Color(55 + random.nextInt(200),
				55 + random.nextInt(200), 55 + random.nextInt(200)));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
//		g.setColor(new Color(255 - random.nextInt(255), 255 - random
//				.nextInt(255), 255 - random.nextInt(255)));
		g.setColor(new Color(random.nextInt(100),  random
		.nextInt(100), random.nextInt(100)));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,17));
		g.drawString(buf.toString(), 10, 18);

		
		for (int i = 0; i < 100; i++) {
			g.setColor(new Color(random.nextInt(255), random.nextInt(255),
					random.nextInt(255)));
			g.drawOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 1, 1);
		}
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(random.nextInt(255), random.nextInt(255),
					random.nextInt(255)));
			g.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), WIDTH
					- random.nextInt(WIDTH), HEIGHT - random.nextInt(HEIGHT));

		}
		
		ServletOutputStream out = response.getOutputStream();
		javax.imageio.ImageIO.write(image, "jpeg", out);
	//	System.out.println(buf);
		HttpSession session=request.getSession();
		session.setAttribute("number",buf.toString());
		out.flush();
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("..............");
		doGet(request, response);
	}

}
