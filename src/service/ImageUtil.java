/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.awt.AlphaComposite;
//import java.awt.Dimension;
import java.awt.Graphics2D;
//import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
//import javax.imageio.ImageReadParam;
//import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author Dinah3
 */
public class ImageUtil {
	
	public String err = "";
	public String filExt = "";

    private byte[] imgOriginal = null;
    private byte[] imgResize = null;    
    int width;
    int height;
    private ImageWriter writer = null;
    private ImageReader reader = null;

    public ImageUtil(int width, int height) {
       this.width = width;
       this.height=height;
       if(width <= 0)
           width = 160;
       if(height <= 0)
           height = 160;
    }

    public byte[] sizeImage(byte[] original) throws IOException{
        this.imgOriginal = original;
        if (imgOriginal == null || original.length == 0) {
            err = "Image array is empty";
            return null;
        }
        if(!processFileType())
        	return null;
        BufferedImage bimg = this.doResize();
        
        if(bimg != null)
            this.getResizedBytes(bimg);
        else {
        	err = "Image could not be resized.";
        }
        return imgResize;
    }

   /* private BufferedImage doResizeSubSampling() {
        BufferedImage bufImage = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(imgOriginal);
        Object source = (Object) bis;
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
            ImageReader reader = (ImageReader) readers.next();
            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceSubsampling(4, 4, 0, 0);
            //param.setSourceRenderSize(new Dimension(160,160));
            reader.setInput(iis, true); //seek forwardonly
            bufImage = reader.read(0, param);
            return bufImage;
        } catch (IOException ioex) {
            err = "ImageInputStream:" + ioex.getMessage();
            return null;
        }
    }*/
    
    private boolean processFileType() throws IOException{
    	getReader();
    	if(reader == null){
    		err = "No reader for image.";
    		return false;
    	}
    	writer = ImageIO.getImageWriter(reader);
    	if(writer == null){
    		err = "No writer for image.";
    		return false;
    	}
    	return true;
    }
    
	private void getReader() throws IOException   {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(this.imgOriginal);
			ImageInputStream iis = ImageIO.createImageInputStream(is);
			Iterator<ImageReader> it = ImageIO.getImageReaders(iis);
			if (it.hasNext()){
				reader = it.next();
				filExt = reader.getFormatName();
			}
		} catch (IOException io) {
			throw new IOException("ImageUtil#getReader:" + io.getMessage());
		}
	}
    
   

    private BufferedImage doResize() throws IOException {
        BufferedImage resized = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(this.imgOriginal);
        try {
            ImageInputStream iis = ImageIO.createImageInputStream((Object) bis);
            resized = this.processResize(ImageIO.read(iis));
        } catch (IOException ioex) {
            String msg = "ImageUtil#doResize:ImageInputStream:" + ioex.getMessage();
            throw new IOException(msg);
        }
        return resized;
    }
    
    private BufferedImage processResize(BufferedImage original){
        
        int type = original.getType() == 0? BufferedImage.TYPE_INT_ARGB : original.getType();
        BufferedImage resized = new BufferedImage(width, height, type);
        Graphics2D g = resized.createGraphics();
	g.drawImage(original, 0, 0, width, height, null);
    g.setComposite(AlphaComposite.Src);
 	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        return resized;
    }

    private void getResizedBytes(BufferedImage bufImg) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageOutputStream ios = null;
        
        try {
            ios = ImageIO.createImageOutputStream((Object) bos);
        } catch (IOException ioex) {
            String msg = "ImageUtil#getResizedBytes:createImageOutputStream:" + ioex.getMessage();
            throw new IOException(msg);
        }
       
        writer.setOutput(ios);
        
        try {
            writer.write(bufImg);
            this.imgResize = bos.toByteArray();
        } catch (IOException ioex) {
            String msg = "ImageUtil#getResizedBytes:write:" + ioex.getMessage();
            throw new IOException(msg);
        }
       
    }
}
