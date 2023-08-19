package com.aws.SoapWeb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.aws.SoapWeb.constant.ConfigConstants;

@EnableWs
@Configuration
public class WebServiceConfig {
	
	private final Logger logger = LogManager.getLogger(WebServiceConfig.class);
	
	@Bean
	ServletRegistrationBean servletRegistrationBean(ApplicationContext applicationContext)
	{
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(applicationContext);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		
		logger.info("Starting API at "+ConfigConstants.baseURI);
		return new ServletRegistrationBean(messageDispatcherServlet,ConfigConstants.baseURI);
	}
	
	@Bean(name="partner")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema xsdSchema)
	{
		DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
		defaultWsdl11Definition.setPortTypeName("PayloadPort");
		defaultWsdl11Definition.setLocationUri(ConfigConstants.baseURI);
		defaultWsdl11Definition.setSchema(xsdSchema);
		defaultWsdl11Definition.setTargetNamespace("http://partnerfiles.com/payload");
		
		logger.info("URI for partner at /partner with namespace http://partnerfiles.com/payload");
		return defaultWsdl11Definition;		
	}
	
	@Bean
	public XsdSchema xsdSchema()
	{
		return new SimpleXsdSchema(new ClassPathResource(ConfigConstants.Schema));
	}

}
