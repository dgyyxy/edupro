package com.edu.common.util;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * ppt或pptx 转为图片
 */
public class PPTtoImage {
    public static void main(String[] args) throws Exception {
        toImage2007();
        //toImage2003();
    }
    public static void toImage2007() throws Exception{
        FileInputStream is = new FileInputStream("D:\\poi-test\\pptToImg\\test.pptx");
        XMLSlideShow ppt = new XMLSlideShow(is);
        is.close();

        Dimension pgsize = ppt.getPageSize();
        System.out.println(pgsize.width+"--"+pgsize.height);

        for (int i = 0; i < ppt.getSlides().size(); i++) {
            try {
                //防止中文乱码
                for(XSLFShape shape : ppt.getSlides().get(i).getShapes()){
                    if(shape instanceof XSLFTextShape) {
                        XSLFTextShape tsh = (XSLFTextShape)shape;
                        for(XSLFTextParagraph p : tsh){
                            for(XSLFTextRun r : p){
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                // render
                ppt.getSlides().get(i).draw(graphics);

                // save the output
                String filename = "D:\\poi-test\\pptToImg\\07-" + (i+1) + ".jpg";
                System.out.println(filename);
                FileOutputStream out = new FileOutputStream(filename);
                javax.imageio.ImageIO.write(img, "png", out);
                out.close();
            } catch (Exception e) {
                System.out.println("第"+i+"张ppt转换出错");
            }
        }
        System.out.println("7success");
    }

    public static void toImage2003(){
        try {
            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl("D:\\poi-test\\pptToImg\\test.pptx"));

            Dimension pgsize = ppt.getPageSize();
            for (int i = 0; i < ppt.getSlides().size(); i++) {
                //防止中文乱码
                for(HSLFShape shape : ppt.getSlides().get(i).getShapes()){
                    if(shape instanceof HSLFTextShape) {
                        HSLFTextShape tsh = (HSLFTextShape)shape;
                        for(HSLFTextParagraph p : tsh){
                            for(HSLFTextRun r : p){
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                // render
                ppt.getSlides().get(i).draw(graphics);

                // save the output
                String filename = "D:/demo/03-760-" + (i+1) + ".jpg";
                System.out.println(filename);
                FileOutputStream out = new FileOutputStream(filename);
                javax.imageio.ImageIO.write(img, "png", out);
                out.close();
//				resizeImage(filename, filename, width, height);

            }
            System.out.println("3success");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /***
     * 功能 :调整图片大小
     * @param srcImgPath 原图片路径 
     * @param distImgPath  转换大小后图片路径 
     * @param width   转换后图片宽度 
     * @param height  转换后图片高度 
     */
    public static void resizeImage(String srcImgPath, String distImgPath,
                                   int width, int height) throws IOException {

        File srcFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg = null;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                0, null);

        ImageIO.write(buffImg, "JPEG", new File(distImgPath));

    }
}