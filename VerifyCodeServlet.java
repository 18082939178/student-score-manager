package com.score.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 登录图形验证码 Servlet
 * 生成 4 位字符验证码图片，并将验证码（忽略大小写）写入 Session，
 * 登录时由 LoginServlet 校验。
 */
@WebServlet("/verifyCode")
public class VerifyCodeServlet extends HttpServlet {

    private static final String CODE_KEY = "verifyCode";

    /** 去除易混淆字符（0/O、1/I/L 等） */
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("image/jpeg");
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setDateHeader("Expires", 0);

        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, width, height);

        Random random = new Random();
        StringBuilder code = new StringBuilder();

        // 逐个字符绘制（随机颜色、字号、偏移）
        for (int i = 0; i < 4; i++) {
            char c = CHARS.charAt(random.nextInt(CHARS.length()));
            code.append(c);
            g.setColor(new Color(40 + random.nextInt(150), 60 + random.nextInt(120), 130 + random.nextInt(90)));
            g.setFont(new Font("Arial", Font.BOLD, 26 + random.nextInt(8)));
            g.drawString(String.valueOf(c), 12 + i * 26, 27 + random.nextInt(8));
        }

        // 干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(180 + random.nextInt(70), 180 + random.nextInt(70), 190 + random.nextInt(60)));
            g.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }
        // 噪点
        for (int i = 0; i < 60; i++) {
            g.setColor(new Color(150 + random.nextInt(100), 150 + random.nextInt(100), 150 + random.nextInt(100)));
            g.fillRect(random.nextInt(width), random.nextInt(height), 1, 1);
        }

        g.dispose();

        // 存入 Session（与登录表单共用）
        req.getSession().setAttribute(CODE_KEY, code.toString());

        ImageIO.write(image, "jpeg", resp.getOutputStream());
    }
}
