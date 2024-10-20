package com.kims.cluster;

import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.kims.cluster"})
public class DemoClusterWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		//SpringApplication.run(DemoClusterWebApplication.class, args);
		SpringApplication application = new SpringApplication(DemoClusterWebApplication.class);
		System.out.println("#DemoClusterWebApplication##################"); 
		application.run(args);
	}
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		//return super.configure(builder);
		System.out.println("SpringApplicationBuilder 111=========================================================");
		 
		return builder.sources(DemoClusterWebApplication.class);
	}
	
	/* java -jar 실행 때 세션 필요 함. 
	 * 세션 공유 됨. 
	 *  
		*/
	private static boolean distributable;
	
	public static boolean getDistributable() {
		
		return distributable;
	}
	
	@Bean
	public ServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				// TODO Auto-generated method stub
				//super.postProcessContext(context);
				
				
				DemoClusterWebApplication.distributable = context.getDistributable();
				

				System.out.println("ServletWebServerFactory distributable =========================================================" + distributable); 
			}
			
		};
	}
	


}
