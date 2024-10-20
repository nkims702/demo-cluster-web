package com.kims.cluster;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//import com.nexacro.uiadapter17.spring.core.context.ApplicationContextProvider;
//import com.nexacro.uiadapter17.spring.core.resolve.NexacroHandlerMethodReturnValueHandler;
//import com.nexacro.uiadapter17.spring.core.resolve.NexacroMappingExceptionResolver;
//import com.nexacro.uiadapter17.spring.core.resolve.NexacroMethodArgumentResolver;
//import com.nexacro.uiadapter17.spring.core.resolve.NexacroRequestMappingHandlerAdapter;
//import com.nexacro.uiadapter17.spring.core.view.NexacroFileView;
//import com.nexacro.uiadapter17.spring.core.view.NexacroView;
//import com.nexacro.uiadapter17.spring.dao.DbVendorsProvider;
//import com.nexacro.uiadapter17.spring.dao.Dbms;
//import com.nexacro.uiadapter17.spring.dao.dbms.Hsql;
//import com.nexacro17.xapi.tx.PlatformType;
//import com.nexacro17.xeni.services.GridExportImportServlet;


import com.nexacro.java.xapi.tx.PlatformType;
import com.nexacro.java.xeni.services.GridExportImportServlet;
import com.nexacro.uiadapter.spring.core.context.ApplicationContextProvider;
import com.nexacro.uiadapter.spring.core.resolve.NexacroHandlerMethodReturnValueHandler;
import com.nexacro.uiadapter.spring.core.resolve.NexacroMappingExceptionResolver;
import com.nexacro.uiadapter.spring.core.resolve.NexacroMethodArgumentResolver;
import com.nexacro.uiadapter.spring.core.resolve.NexacroRequestMappingHandlerAdapter;
import com.nexacro.uiadapter.spring.core.view.NexacroFileView;
import com.nexacro.uiadapter.spring.core.view.NexacroView;


@Configuration
public class NexacroConfig extends WebAppConfig implements WebMvcRegistrations {
  
  /**
   * Spring의 ApplicationContext를 제공한다.
   * @return
   */
  @Bean
  @Lazy(false)
  public ApplicationContextProvider applicationContextProvider() {
    return new ApplicationContextProvider();
  }
  
  /**
   * 넥사크로플랫폼 RequestMappingHandlerAdapter 구현체 등록
   */
  @Override
  public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
      return new NexacroRequestMappingHandlerAdapter();
  }
    
  /**
   * 넥사크로플랫폼 ArgumentResolver 등록
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
     
      NexacroMethodArgumentResolver nexacroResolver = new NexacroMethodArgumentResolver();
      resolvers.add(nexacroResolver);
      
      super.addArgumentResolvers(resolvers);
  }

  /**
   * 넥사크로플랫폼 ReturnValueHandler 등록
   */
  @Override
  public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        
      NexacroHandlerMethodReturnValueHandler returnValueHandler = new NexacroHandlerMethodReturnValueHandler();
        
      NexacroFileView nexacroFileView = new NexacroFileView();        
      NexacroView     nexacroView     = new NexacroView();
      nexacroView.setDefaultContentType(PlatformType.CONTENT_TYPE_XML);
      nexacroView.setDefaultCharset("UTF-8");
        
      returnValueHandler.setView(nexacroView);
      returnValueHandler.setFileView(nexacroFileView);
        
      handlers.add(returnValueHandler);
        
      super.addReturnValueHandlers(handlers);
  }
    
  /**
   * 넥사크로플랫폼 에러 처리 ExceptionResolver 등록
   */
  @Override
  public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        
      NexacroView nexacroView = new NexacroView();
      nexacroView.setDefaultContentType(PlatformType.CONTENT_TYPE_XML);
      nexacroView.setDefaultCharset("UTF-8");
        
      NexacroMappingExceptionResolver nexacroException = new NexacroMappingExceptionResolver();
        
      nexacroException.setView(nexacroView);
      nexacroException.setShouldLogStackTrace(true);
      nexacroException.setShouldSendStackTrace(true);
      nexacroException.setDefaultErrorMsg("fail.common.msg");
      nexacroException.setMessageSource(messageSource());
      nexacroException.setOrder(1);
      resolvers.add(nexacroException);
        
      super.configureHandlerExceptionResolvers(resolvers);
  }
    
  /**
   * Dbms별 meta정보 조회를 위한 bean 등록
   * mybatis 사용 시 데이터가 없는 경우 null 반환
   * nexacro platform 컬럼정보 생성을 위해 metadata 정보 조회 후 컬럼 생성 처리
   * @return
  
  @Bean
  public DbVendorsProvider dbmsProvider() {
    DbVendorsProvider dbmsProvider = new DbVendorsProvider();
    
    Map<String, Dbms> dbvendors = new HashMap<String, Dbms>();
    dbvendors.put("HSQL Database Engine",new Hsql());
    dbmsProvider.setDbvendors(dbvendors);
    return dbmsProvider;
  }
     */
  /**
   * 엑셀처리 서블릿 등록
   */
  @Bean
  public ServletRegistrationBean<GridExportImportServlet> gridExportImportServletBean() {
      ServletRegistrationBean<GridExportImportServlet> bean = 
          new ServletRegistrationBean<GridExportImportServlet>( new GridExportImportServlet()
              , "/XExportImport.do"
              );
      bean.setLoadOnStartup(1);
      return bean;
  }
    
  /**
   * Register XENI INIT-PARAMETER
   */
  @Bean
  public ServletContextInitializer initializer() {

      return new ServletContextInitializer() {
        /*
         * 넥사크로플랫폼 엑셀 처리 모듈:nexacro17-xeni.jar를 이용한 엑셀 처리 설정
        */ 
        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setInitParameter("export-path"       , "file:////usr/local/webapps/export/");  //엑셀 export 임시폴더 생성 기준 디렉터리
                servletContext.setInitParameter("import-path"       , "file:////usr/local/webapps/import/");  //엑셀 import 임시폴더 생성 기준 디렉터리
        	
        		//servletContext.setInitParameter("export-path"       , "C:\\TEMP\\export");  //엑셀 export 임시폴더 생성 기준 디렉터리
        		//servletContext.setInitParameter("import-path"       , "C:\\TEMP\\import/");  //엑셀 import 임시폴더 생성 기준 디렉터리
        	
        	
                servletContext.setInitParameter("monitor-enabled"   , "true");    //임시파일 삭제를 위한 모니터링 여부
                servletContext.setInitParameter("monitor-cycle-time", "30");      //임시파일 삭제를 위한 모니터링 주기( default:분)
                servletContext.setInitParameter("file-storage-time" , "10");      //임시파일 생성 디렉터리 모니터링 주기 (default:분)
        }
      };
  }
  
  /**
   * CommonsMultipartResolver 사용
   * Multipart Resolver - Upload upto 10 Megabytes
   * This is for XENI IMPORT
   */
  @Bean
  public CommonsMultipartResolver multipartResolver() throws IOException {
         CommonsMultipartResolver resolver = new CommonsMultipartResolver();
         resolver.setDefaultEncoding("utf-8");
         resolver.setMaxUploadSize(100000000);
         resolver.setMaxInMemorySize(100000000);
         resolver.setUploadTempDir((Resource) tempFileDir());
         return resolver;
  }

  /**
      * 파일 업로드 Temp 폴더 설정
     * windows 서버의 경우 경로 설정
     */
    @Bean
    public FileSystemResource tempFileDir() {
        FileSystemResource resource = new FileSystemResource("/usr/local/webapps/temp");
    	//FileSystemResource resource = new FileSystemResource("C:\\TEMP");
        return resource;
    }
  
  /**
   *  MultipartFilter 재정의    
   */  
  @Bean
  @Order(0)
  public MultipartFilter multipartFilter() {
         MultipartFilter multipartFilter = new MultipartFilter();
         multipartFilter.setMultipartResolverBeanName("multipartResolver");
         return multipartFilter;
  }

}