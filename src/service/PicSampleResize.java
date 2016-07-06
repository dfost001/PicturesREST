package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.util.List;

import client.model.ErrorEnum;
import exceptions.ClientException;
import model.PicSample;

public class PicSampleResize {	
	
	
	private String module = "";
	
	private static String defaultPicName = "default-photo.jpg";
	
	
	public PicSampleResize(){
		module = this.getClass().getName();
	}
      
	public PicSample process(PicSample sample)throws ClientException {
		try {
		   validate(sample);
		}
		catch(IOException ex){
			throw new ClientException(ErrorEnum.TEMPORARY_IO_ERROR.name(), ex.getMessage());
		}
		return sample;
	}
	
	private void validate(PicSample sample) throws ClientException, IOException{
		
		byte[] picBytes = sample.getPhoto();
		
		String comment = sample.getComment();
		
		if((picBytes == null || picBytes.length == 0)&& (comment == null || comment.isEmpty()))
			throw new ClientException(ErrorEnum.EMPTY_UPLOAD.name());
		
		else if(picBytes == null || picBytes.length == 0)
			initDefaultPicture(sample);
		
		else
			resizePicture(sample);
	}
	
	
	
	private void initDefaultPicture(PicSample sample) throws IOException{
		InputStream is = this.getClass().getResourceAsStream("/default_image/tree_re.jpg");
		byte[] buffer = new byte[1024];
		int ct = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {

			while ((ct = is.read(buffer)) != -1)
				bos.write(buffer, 0, ct);

			byte[] pic = bos.toByteArray();

			sample.setPhoto(pic);
			
			sample.setPicName(PicSampleResize.defaultPicName);
			
			sample.setOriginalPicName(PicSampleResize.defaultPicName);
			
		} catch (IOException io) {
			throw new IOException(module + "#initDefaultPicture:IOException:"
					+ io.getMessage());
		}
	}
	
	private void resizePicture(PicSample sample) throws IOException, ClientException {
		
		ImageUtil util = new ImageUtil(160, 160);
		
		byte[] resized = null;		
		
		resized = util.sizeImage(sample.getPhoto());		
		
		if(resized == null) {
			ClientException client =
					new ClientException(ErrorEnum.UNSUPPORTED_IMAGE_TYPE.name(), util.err);
		    client.setClientInfo("Unsupported image type for " + sample.getPicName());
		    throw client;
		}
		sample.setPhoto(resized);
		
		String name = assignName(sample.getPicName(), util.filExt);		
		
		sample.setPicName(name);
	}
	 /*
	  * To do: replace period with underscores, then add extension
	  * Not tested
	  */
	private String assignName(String name, String ext){
		if(name == null | name.isEmpty()){
			name = "photo";
		}
		String edited = name.replaceAll("\\.", "_");
		edited = edited.replace((char)32, (char)45); //all spaces with a dash
		return edited + "." + ext;
		
	}
	
}//end class
