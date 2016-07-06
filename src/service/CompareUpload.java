package service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class CompareUpload {
	
	public String compare(byte[] upload) throws IOException {
		
		String msg = "";
		
		byte[] source = this.readOriginal();
		
		int sidx = 0;
		
		String notMatched = "";
		
		for(int i=0; i < upload.length; i++){
			if(source[sidx] == upload[i]){
				sidx++;
			}
			else {
				notMatched = new Byte(upload[i]).toString();
				msg += notMatched + " at " + i + ",";			}
				
		}
		if(msg.isEmpty())
			msg = "Arrays are equal";
		
		return msg;
		
	}
	
	public String compareResized(byte[] upload, byte[] source) {
		
        int sidx = 0;
        
        String msg="";
		
		String notMatched = "";		
		
		if(upload.length != source.length) {
			msg = "Source and upload resized have different lengths";
			return msg;
		}
		
		for(int i=0; i < upload.length; i++){
			if(source[sidx] == upload[i]){
				sidx++;
			}
			else {
				notMatched = new Byte(upload[i]).toString();
				msg += notMatched + " at " + i + ",";			}
				
		}
		if(msg.isEmpty())
			msg = "Arrays are equal";
		
		return msg;
		
	}
	
	private byte[] readOriginal() throws IOException{
		
		
			byte[] picBuffer = null;
			byte[] buf = new byte[1024];
			int read = 0;
			FileInputStream fs = 
					new FileInputStream("C:\\Users\\Dinah\\Pictures\\samplePictures2\\desert.jpg");
			//FileInputStream fs = 
			//	new FileInputStream("C:\\Users\\Dinah\\Pictures\\phoneCamera\\museum.mp4");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while((read = fs.read(buf)) > -1)
				bos.write(buf,0,read);
			picBuffer = bos.toByteArray();
			fs.close();
			return picBuffer;
		
		
		
	}

}
