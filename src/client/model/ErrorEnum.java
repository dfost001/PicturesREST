package client.model;

import javax.xml.bind.annotation.XmlEnum;
//import javax.xml.bind.annotation.XmlType;

@XmlEnum(String.class)
public enum ErrorEnum {
	
     UNSUPPORTED_IMAGE_TYPE,
     NO_MORE_RECORDS,
     INVALID_UPLOAD_OBJECT,
     INVALID_CUSTOMER_ID,
     INVALID_INDICES,
     TEMPORARY_DATABASE_ERROR,
     TEMPORARY_IO_ERROR,
     EMPTY_UPLOAD,
     INVALID_COLUMN,
     DELETE_ERROR;
     
     public String value() {
    	 return name();
     }
     
     public static ErrorEnum fromValue(String value) {
    	 return valueOf(value);
     }
}
