package ejb;

//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import exception_handlers.ErrorUtil;
import exceptions.ClientException;





//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.NoResultException;

import org.jboss.logging.Logger;

import model.Customer;
import model.PicSample;

@Stateless(name="PicturesFacade")
public class PicturesFacade {
	
	 private static final Logger logger = Logger.getLogger("ejb.PicturesFacade");
	 
	@Resource
	private SessionContext ctx;
	
     @PersistenceContext(unitName="PicturesREST")
     private EntityManager em;     
	
     public Customer customerById(int id) throws ClientException, PersistenceException {
    	 
 		logger.info("Entering PicturesFacade#customerById");
 		
 		String sql = "Select c from Customer c where c.id = " + id;
 		
 		Customer c = null;
 		
 		try {
 		
 		Query q = em.createQuery(sql, Customer.class);
 		c = (Customer) q.getSingleResult();
 		
 		} catch(NoResultException rex){
 			
 			throw ErrorUtil.initInvalidId(rex, id);
 			
 		} catch(Exception ex){
    		
			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException("PicturesFacade#customerById:" + ex.getMessage(),ex);
			else
				throw ErrorUtil.initTemporaryDb(ex, "PicturesFacade#customerById");
			
    	} 		
 		return c;
 	}	
     
	public PicSample findPicSample(int id) throws ClientException,
			PersistenceException {

		try {

			String sql = "Select p from PicSample p where p.photoId = " + id;

			Query q = em.createQuery(sql, PicSample.class);

			PicSample pic = (PicSample) q.getSingleResult();

			return pic;
		} catch (NoResultException ex) {
			throw ErrorUtil.initDeleteError(Integer.valueOf(id).toString());
		} catch (Exception ex) {

			ctx.setRollbackOnly();

			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException(
						"PicturesFacade#findPicSample:debugging:"
								+ ex.getMessage(), ex);
			else
				throw ErrorUtil.initTemporaryDb(ex,
						"PicturesFacade#findPicSample");

		}

	}
     
     @SuppressWarnings("unchecked")
	public List<PicSample> findSampleByName(String name, Integer customerId) throws Exception {
    	 
    	 String lname = name.toLowerCase();
    	 
    	 String sql = "Select p from PicSample p "
    			 + "where LOWER(p.originalPicName) = "
    			 + ":lname "
    			 + "and p.customer.id = :id order by p.dtUpdated asc";
    	 
    	 Query query = em.createQuery(sql, PicSample.class);
    	 
    	 query.setParameter("lname", lname);
    	 
    	 query.setParameter("id", customerId.intValue());
    	 
    	 List<PicSample> pics = (List<PicSample>) query.getResultList();
    	 
    	 String debug = pics == null ? "null" : Integer.valueOf(pics.size()).toString();
    	 
    	 logger.info("findSampleByName " + debug);
    	 
    	 return pics;
     }
	/* rangeIndex[] contains from and to indices. To retrieve
	 * the first three records {0,2} is the array assignment.
	 */
	@SuppressWarnings("unchecked")
	public List<PicSample> findRange(int customerId, int[] rangeIndex)
			throws ClientException, PersistenceException {
		String sql = "Select x from PicSample x "
				+ "where x.customer.id = :id order by x.dtUpdated desc";
		List<PicSample> list = null;
		try {
			Query query = em.createQuery(sql);
			query.setParameter("id", customerId);
			query.setMaxResults(rangeIndex[1] - rangeIndex[0] + 1);
			query.setFirstResult(rangeIndex[0]);
			list = (List<PicSample>) query.getResultList();
		} catch (Exception ex) {
			
			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException("PicturesFacade#findRange:" + ex.getMessage(), ex);
			else
				throw ErrorUtil.initTemporaryDb(ex, "PicturesFacade#findRange");
		}
		return list;
	}
    
    public int count(int customerId) throws ClientException, PersistenceException {
    	Long result = null;
    	String sql = "Select count(x) from PicSample x where x.customer.id = :id";
    	try {
    	Query q = em.createQuery(sql);
    	q.setParameter("id", customerId);
    	result = (Long)q.getSingleResult();
    	}
    	catch(Exception ex){
    		if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException("PicturesFacade#count:" + ex.getMessage(), ex);
			else
				throw ErrorUtil.initTemporaryDb(ex, "PicturesFacade#count");
    	}
    	logger.info("PicturesFacade#count:" + result.intValue());
    	return result.intValue();
    }		
	
	
	public Integer insertPicSample2(Customer customer, PicSample pic)
			throws ClientException,PersistenceException {
		
		logger.info("Entering insertPicSample2");
		logger.info("picName size=" + pic.getPicName().length());
		logger.info("photo size=" + pic.getPhoto().length);
		
		try {
			pic.setCustomer(customer);			
			
			pic.setDtUpdated(new Date());	
			
			em.persist(pic);	
			
		}
		catch (Exception ex) {			
			//ctx.setRollbackOnly(); -- done transparently
			
			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException
				("PicturesFacade#insertPicSample2:debugging:" + ex.getMessage(),ex);
			else
				throw ErrorUtil.initTemporaryDb(ex, "PicturesFacade#insertPicSample2");
		}
		return pic.getPhotoId();

	}
	
	
	public void deletePicSample(List<Integer> photoId, Integer customerId) throws Exception {

		int count = -1;

		String msg = "";

		try {

			for (Integer id : photoId) {

				msg = id.toString();

				this.findPicSample(id);				

				count = doDelete(id);				
				
				logger.info("doDelete rows affected " + count);
				
				em.flush();				
			} // end for
		}// end try
		  catch (NoResultException ex) {
			throw ErrorUtil.initDeleteError(msg);
		} catch (Exception ex) {
			
			 ctx.setRollbackOnly(); 

			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException(
						"PicturesFacade#deletePicSample:debugging:"
								+ ex.getMessage(), ex);
			else
				throw ErrorUtil.initTemporaryDb(ex,
						"PicturesFacade#deletePicSample");

		}

	}
	
	private int doDelete(int photoId){
		String sql = "Delete from pic_sample where photoId = " + photoId;
		Query q = em.createNativeQuery(sql);
		int count = q.executeUpdate();
		return count;
	}
	
	/*private void doDelete2(PicSample pic){
		em.remove(em.merge(pic));
	}*/
	
	public void adjustNamesTransaction(List<PicSample> pics) throws PersistenceException, ClientException {
	  try {	
		for(PicSample p : pics)
			updateNames(p);
	  }
	  catch(PersistenceException ex){
		  this.ctx.setRollbackOnly();
		  throw ex;
	  }
	  catch(ClientException ex){
		  this.ctx.setRollbackOnly();
		  throw ex;
	  }
	}
	
	private void updateNames(PicSample deletedPic) throws ClientException,
			PersistenceException {

		try {

			List<PicSample> samples = this.findSampleByName(
				deletedPic.getOriginalPicName(), deletedPic.getCustomer().getId());

			for (int i = 0; i < samples.size(); i++) {

				Integer rowNum = new Integer(i + 1);

				PicSample pic = samples.get(i);

				String name = this.adjustName(pic.getPicName(), rowNum);

				pic.setPicName(name);

				em.merge(pic);
			}
		} catch (Exception ex) {			

			if (ErrorUtil.isApplicationException(ex))
				throw new PersistenceException(
						"PicturesFacade#updateNames:debugging:"
								+ ex.getMessage(), ex);
			else
				throw ErrorUtil.initTemporaryDb(ex,
						"PicturesFacade#updateNames");

		}
	}
	
	private String adjustName(String name, Integer rowNum) {
		
		String ext = name.substring(name.indexOf(".")); //include period
		
		int pos = name.lastIndexOf("_");
		
		String edited = name.substring(0,pos) + "_" + rowNum.toString() + ext;
		
		return edited;
	}
	
 
}//end class	
