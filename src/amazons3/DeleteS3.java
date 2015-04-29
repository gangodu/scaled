package amazons3;

	import java.io.IOException;

	import com.amazonaws.AmazonClientException;
	import com.amazonaws.AmazonServiceException;
	import com.amazonaws.auth.profile.ProfileCredentialsProvider;
	import com.amazonaws.services.s3.AmazonS3;
	import com.amazonaws.services.s3.AmazonS3Client;
	import com.amazonaws.services.s3.model.DeleteObjectRequest;

	public class DeleteS3 {

	    private static String bucketName = "scale-userdata";
	    private static String keyName    = "scale-userdata"; 

	    public static void main(String[] args) throws IOException 
	    {
	        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
	        try 
	        {
	            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
	        } 
	        
	        catch (AmazonServiceException ase) 
	        {
	            System.out.println("Caught an AmazonServiceException.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } 
	        catch (AmazonClientException ace) 
	        {
	            System.out.println("Caught an AmazonClientException.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
	    }
	}