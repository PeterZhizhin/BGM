package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;

public class BackgroundUtils {

    /**
     *  Рисуем текстовые заголовки
     * @param source исходная картинка, на которой рисуем
     * @param titles заголовки
     * @param xPositions координаты по оси икс
     * @param yPositions координаты по оси игрек
     * @param fontColor цвет шрифта (new Color(255, 255, 255); )
     * @param fontsize размер шрифта в пунктах
     * @return новая картинка
     * @throws Exception например, если не смогли подгрузить шрифт
     */
    public static BufferedImage drawTitles (
            BufferedImage source, String[] titles, int[] xPositions, int[] yPositions, Color fontColor,
            float fontsize)
            throws Exception
    {

            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new FileInputStream(new File("resources/fonts/CRISTAL.ttf")));
        font=font.deriveFont(fontsize);
        BufferedImage img=new BufferedImage(source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2= (Graphics2D) img.getGraphics();
        g2.drawImage(source, 0, 0, null);
        g2.setColor(fontColor);
        g2.setFont(font);
        for (int i=0; i<titles.length; i++) {
            g2.drawString(titles[i], xPositions[i], yPositions[i]);
        }
        g2.dispose();
        return img;
    }

    /**
     * Блюрим картинку
     * @param source исходная картинка
     * @param radius радиус блюра
     * @return заблюренная картинка
     */
    public static BufferedImage blur(BufferedImage source, int radius) {

        BufferedImage img=new BufferedImage(source.getWidth()+radius*2, source.getHeight()+radius*2,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2= (Graphics2D) img.getGraphics();
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, source.getWidth()+radius*2, source.getHeight()+radius*2);
        g2.drawImage(source, radius, radius, null);
        g2.dispose();

        int square = radius * radius;
        float sum = 0;
        float[] matrix = new float[square];
        for (int i = 0; i < square; i++) {
            int dx = i % radius - radius / 2;
            int dy = i / radius - radius / 2;
            matrix[i] = (float) (radius - Math.sqrt(dx * dx + dy * dy));
            sum += matrix[i];
        }
        for (int i = 0; i < square; i++) {
            matrix[i] /= sum;
        }

        BufferedImageOp op = new ConvolveOp(
                new Kernel(radius, radius, matrix),
                ConvolveOp.EDGE_ZERO_FILL, null);

        BufferedImage res= op.filter(img, null);

        BufferedImage out=new BufferedImage(source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g3= (Graphics2D) out.getGraphics();
        g3.drawImage(res, -radius, -radius, null);
        g3.dispose();
        return out;

    }

    public static void main(String[] args) throws Exception {


        BufferedImage test=new BufferedImage(800, 600, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2= (Graphics2D) test.getGraphics();
        Color black=new Color(0, 0, 0);
        g2.setColor(black);
        g2.fillRect(0, 0, 800, 600);

        BufferedImage titled=drawTitles(
                test,
                new String[]{"Тест", "Test", "1234", "!@#$"},
                new int[]{20, 20, 20, 20},
                new int[]{100, 200, 300, 400},
                new Color(255, 255, 255),
                74f
        );

        ImageIO.write(titled, "png", new File("чёткаякартинка.png"));
        ImageIO.write(blur(titled, 120), "png", new File("размытаякартинка.png"));

    }




}

