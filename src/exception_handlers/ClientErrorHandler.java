package exception_handlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.GenericEntity;

import exceptions.ClientException;


import client.model.ClientErrorResponse;
import client.model.ErrorEnum;

@Provider
public class ClientErrorHandler implements javax.ws.rs.ext.ExceptionMapper<ClientException> {
	
	private final String unsupportedImageType = "Please select another image for upload. ";	
    private final String emptyUpload = "Upload did not contain an image or a comment." ;
    private final String invalidCustomerId = "Please enter a valid Customer ID.";
    private final String invalidIndices = "'From' index is greater than 'to' index.";
    private final String invalidUploadObject = "Upload entity cannot be converted to PicSample";
    private final String noMoreRecords = "'To' index exceeds the number of customer records.";
    private final String temporaryDatabaseError = "Temporary error performing database operation.";
    private final String temporaryImageIO = "Temporary error processing image data.";
    private final String deleteError = "Picture could not be found for deletion.";
    private final String invalidColumn = "Your entry exceeds the allowed length.";


	@Override
	public Response toResponse(ClientException clientEx) {		
		
		ErrorEnum err = ErrorEnum.valueOf(clientEx.getMessage());
		
		ClientErrorResponse client = initError(clientEx);
		
		GenericEntity<ClientErrorResponse> entity = new GenericEntity<ClientErrorResponse>(client){};
		
		Response.Status status = this.getStatus(err);		
				
		Response response = Response.status(status).entity(entity).type(MediaType.APPLICATION_XML).build();
		
		return response;
	}
	
	private ClientErrorResponse initError(ClientException ex){
		
        ErrorEnum err = ErrorEnum.valueOf(ex.getMessage());
		
		String detail = ex.getDetail();	
		
		
		ClientErrorResponse response = new ClientErrorResponse();
		
		response.setErrorEnum(err);
		response.setDetail(detail);
		this.initMessage(err, ex.getClientInfo(), response);
		return response;
		
		
	}
	
	private void initMessage(ErrorEnum err, String clientInfo, ClientErrorResponse response){
		String msg = "";
		String info = "Try closing the browser window, and logging in again.";
		switch(err){
		case EMPTY_UPLOAD:
		    msg = emptyUpload;	
		    break;
		case INVALID_CUSTOMER_ID:
			msg = this.invalidCustomerId;
			response.setInfo(clientInfo); //customer id entry
			break;
		case DELETE_ERROR:
		    msg = this.deleteError;
		    response.setInfo(clientInfo); //photo id
		    break;
		case INVALID_INDICES:
			msg = invalidIndices;
			break;
		case INVALID_UPLOAD_OBJECT:
			msg = invalidUploadObject;
			break;
		case NO_MORE_RECORDS:
			msg = noMoreRecords;
			break;
		case TEMPORARY_DATABASE_ERROR:
			msg = temporaryDatabaseError;
			response.setInfo(info);
			break;
		case UNSUPPORTED_IMAGE_TYPE:
			msg = this.unsupportedImageType;
			response.setInfo(clientInfo); //picName
			break;
		case TEMPORARY_IO_ERROR:
			msg = temporaryImageIO;
			response.setInfo(info);
			break;
		case INVALID_COLUMN:
			msg = invalidColumn;
			response.setInfo(clientInfo);
			break;
		}
		
		response.setMessage(msg);
	}
	
	private Response.Status getStatus(ErrorEnum err){
		if(err.equals(ErrorEnum.TEMPORARY_DATABASE_ERROR))
			return Status.SERVICE_UNAVAILABLE;
		else if(err.equals(ErrorEnum.TEMPORARY_IO_ERROR))
			return Status.SERVICE_UNAVAILABLE;
		return Status.BAD_REQUEST;
	}

}
