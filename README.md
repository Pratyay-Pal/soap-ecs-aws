# SOAP Web Service

This code creates a SOAP Web service.

The dockerfile contains details to build the an image with the JAR. The ENTRYPOINT is execution of the JAR file.
On deploying the image on any environment, the Web Service will be accessible. 

The workflow contains details to build the image and upload it to AWS ECR Repository named api-repo.


#### How to use this?

Clone the repository and run as Java Application. Access the WSDL through http://localhost:8080/partner.wsdl.
Use the WSDL to generate the request or use the sample requests given in SampleReqResp folder.


#### Create Image and push to ECR-

Push to master. Github workflow takes over automatically. 
To allow Github to push image to ECR Repository, there are two methods to achieve this-

1) Create IAM Role that trusts Github, and permits it to push image to ECR Repository. This is the recommended approach.
2) Create Access Keys and place it in Github and Environment Variables.  Make sure the IAM User for the Access Keys have permission to push to ECR. This is the method used in the current workflow file.