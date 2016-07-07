<b>Description:</b>

The service uses declarative annotations provided by the Java REST API. Request paths include end-points to serialize the first given number of records, the next N records, to upload an entity, and to delete a list.

JPA entity beans, an EJB state-less session bean, and the EntityManager interface are used to manage data access.

<b>Entity Model:</b> 
 
The relational model consists of a client table, and image records related on the client id.
The image record contains a BLOB field to store image data, a time-stamp for ordered retrievals, 
a description, and fields for the original file name and a generated name based on the number
of duplicated filenames. 

<b>Client Model</b>

The ClientResponse model consists of a client entity, a list of related image record objects, and a field for the total number of records, since a given number is returned with each request. Also included are fields for the beginning and end range of record numbers retrieved, which can be used by the client for an ordered retrieval.

The client model also includes an error enumeration class and an error class. The enumeration contains constants corresponding to the types of client errors that may occur. . . The error object contains a field for the enumeration, the error message, a detail, and info field for additional text such as the value of the customer id. 

The enumeration and response codes may be used on the client to determine the error processing flow, for example to differentiate between a user-entry error, a programming error, or a recoverable service error.

<b>Error Handling</b>

The application implements the ExceptionMapper to return the error object to the client. The error handler maps to a custom client exception that is thrown from the method where the error occurs.

Another ExceptionMapper handles application errors such as a PersistenceException.

<i>Since the end-points produce XML, the client model and entity beans are coded with Java XML binding annotations.</i>

<b>EJB Methods<b>
EJB methods are invoked to return the data lists or execute the update transaction.

1.	Find customer by id to validate. All endpoints first validate the Customer Id by invoking the EJB find method. The EJB uses an error utility to throw a Client Exception if a NoResultException occurs.

2.	Find the picture by id – used by delete to hold pictures before they are deleted since any remaining pictures having the same name are renumbered

3.	Find a picture list by original name – used by insert to number the file name

4.	Find range – returns a list of image entities given the customer id, and an array containing the upper and lower index

5.	Count – returns the total number of image records by Customer Id

6.	Insert – persists an Image Entity 

7.	Delete – deletes image records given an array of photo id’s

8.	Adjust Names Transaction – updates the name field given a list of deleted objects For each id the procedure retrieves a list by original name in date ascending order, changes the name according to its index, and merges back into the data.

<b>Endpoints</b>

All endpoints return the same response object to simplifly client processing.

<b>Initialize:</b>

The first and next endpoints use a client utility to compute the indices, and return a response object initialized with the list, indices, and total.

The find first method returns the first n number of records or initializes the response object with a null entity list, a total of 0, and indices set to a -1.

<b>Find Next:</b>

The find Next end-point includes path parameters that indicate the range indices. This end-point assumes that the indices are from the previous client response. The utility calculates the next indices based on the upper index or throws a Client Exception if the indices are negative, the upper index is greater than the total, or the upper index is greater than the total.

<b>Insert:</b>

The insert end-point defines the persistence entity as a resource method parameter that is extracted into the argument list by the runtime.

A Validator Utility is used to ensure that at least one of the client fields has been assigned (since the runtime may create a new object, if the uploaded XML cannot be deserialized.) 

JEE bean validation is used to validate size constraints. If the bean is null, contains all empty fields, or exceeds a length constraint, the Validator throws a Client exception.

The picture is resized according to the size range of the BLOB data type using Java AWT and ImageIO within a resize utility. If no picture has been uploaded, a default picture is assigned. If the picture cannot be resized or an IOException occurs a Client Exception is thrown 
(The IO exception is thrown as temporary enumeration).

The picture name is copied to the original name field. The record count, by original file name, plus one is appended, to the original and assigned to the name field. The entity is then persisted using an EJB method. 

The initialize method of the Calculate Utility returns the first n records in descending order, which includes the newly inserted record.

<b>Delete:</b>

The delete end-point extracts query parameters having the same key into a List of photo 
<span style="font-family:Courier-New"> ids </span>.
Since the picture names with the same name will be re-numbered after deletion, a list of the records to be deleted is obtained before the deletion. 

The list of photo-ids is passed to the EJB for deletion. If no error is thrown, the held deleted list is passed to the EJB for the update names transaction.

For each entity in the list, the transaction obtains a record-set by the deleted picture name. The set is retrieved in date ascending order. Then, for each object in the set, the name is parsed and re-numbered according to the iteration index. The record is updated with the new name.





 