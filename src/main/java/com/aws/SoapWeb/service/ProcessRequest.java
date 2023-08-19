package com.aws.SoapWeb.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aws.SoapWeb.constant.ConfigConstants;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;


public class ProcessRequest {
	
	private final static Logger logger = LogManager.getLogger(ProcessRequest.class);
	
	public enum Status{
		SUCCESS,FAILURE
	}
	
	public Status processRequest(int id, String owner, String payload, int timestamp )
	{
		String objectKey = owner+"/Inbound/"+owner+"_"+timestamp+".xml";
		String bucketName=ConfigConstants.S3Bucket;
		
		logger.info("Payload : "+payload);
		
		File file = new File("temp.xml");	       
	    FileWriter wr;
		try {
			wr = new FileWriter(file);
		    wr.write(payload);	         
		    wr.flush();	         
		    wr.close();
		    logger.info("Payload written to file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Error writing payload to file");
			e.printStackTrace();
			return Status.FAILURE;
		}	 
		
		logger.info("S3 Bucket object key : "+objectKey);
		
		Region region = ConfigConstants.region;
		
		S3Client s3 = null;
		try {
			s3 = S3Client.builder()
	            .region(region)
	            .build();
		}
		catch(Exception e){
			logger.error("Unable to build S3 Client");
			e.printStackTrace();
		}

	    String result = putS3Object(s3, bucketName, objectKey, file);
	    logger.info("Tag information: "+result);
	    s3.close();
		
	    if(result != "ERROR WRITING FILE") {
	    	return Status.SUCCESS;
	    }
	    else {
	    	return Status.FAILURE;
	    }
	}
	
	public static String putS3Object(S3Client s3, String bucketName, String objectKey, File file) {

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

            PutObjectResponse response = s3.putObject(putOb,RequestBody.fromFile(file));
            return response.eTag();

        } catch (S3Exception e) {
        	logger.error("Error while writing to "+ConfigConstants.S3Bucket+" : "+e.getMessage());
        	return "ERROR WRITING FILE";
        }
    }

}
