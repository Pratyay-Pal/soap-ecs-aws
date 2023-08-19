package com.aws.SoapWeb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.aws.SoapWeb.PartnerRequest;
import com.aws.SoapWeb.PartnerResponse;
import com.aws.SoapWeb.service.ProcessRequest.Status;

@Endpoint
public class SoapApiEndpoint {
	
	private final Logger logger = LogManager.getLogger(SoapApiEndpoint.class);
	
	@PayloadRoot(namespace = "http://partnerfiles.com/payload", localPart = "PartnerRequest" )
	@ResponsePayload
	public PartnerResponse partnerResponse(@RequestPayload PartnerRequest partnerRequest)
	{
		logger.info("Received Request : "+partnerRequest.toString());
		ProcessRequest processRequestObj = new ProcessRequest();
		Status status = processRequestObj.processRequest(partnerRequest.getId(), partnerRequest.getOwner(), partnerRequest.getPayload(), partnerRequest.getTimestamp());
		
		PartnerResponse partnerResponse = new PartnerResponse();
		partnerResponse.setStatus(mapStatus(status));
		
		return partnerResponse;
	}

	private com.aws.SoapWeb.Status mapStatus(Status status) {
		
		if(status == Status.FAILURE)
		{
			return com.aws.SoapWeb.Status.FAILURE;
		}		
		return com.aws.SoapWeb.Status.SUCCESS;
	}

}
