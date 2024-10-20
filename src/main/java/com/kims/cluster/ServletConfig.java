package com.kims.cluster;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nexacro.java.xeni.services.GridExportImportServlet;

//@Configuration
public class ServletConfig {

	//@Bean
	public ServletRegistrationBean<GridExportImportServlet> getServletRegistrationBean(){
		
		ServletRegistrationBean<GridExportImportServlet> registrationBean 
			= new ServletRegistrationBean<>(new GridExportImportServlet());
		
		System.out.println("=========================================================");
		registrationBean.addUrlMappings("/XExportImport");
		
		registrationBean.addInitParameter("export-path", "/export");
		registrationBean.addInitParameter("monitor-enabled", "true");
		registrationBean.addInitParameter("monitor-cycle-time", "5");
		registrationBean.addInitParameter("file-storage-time", "1");
		
		return registrationBean;
		
		
	}
	
}
