package exceptions;

public class ClientException extends Exception{
	
	  
	private static final long serialVersionUID = 5154981804362943681L;
	
	private String detail = "";	
	
	private String clientInfo = "";
	

	//Construct with ErrorEnum
	public ClientException(String message){
		   super(message);
	}
	
	public ClientException(String message, String detail){
		super(message);
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}

	
	
	
	   
}
