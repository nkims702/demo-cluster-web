package com.kims.cluster;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;



@Controller
public class LoginController {
	
	
	/**
	  * http://localhost:8080/process/getConditionList/names/sdfsasdfa/ages/22
	  * @param name
	  * @param age
	  * @return
	  */
	 @RequestMapping(value="/", method=RequestMethod.GET)
	 public ModelAndView  home(HttpServletRequest req){
	  System.out.println("/home !!!!");
	  HttpSession session = req.getSession();
	  System.out.println("session is new : " + session.isNew());
	  if( session.isNew() || session == null) {
		  session.setAttribute("test", "session chk");
		  
	  }else {
		  
		  
	  }
	  System.out.println("home session is new : " + session.isNew() + ", get session : " + session.getAttribute("test"));
	  return new ModelAndView("index");

	 }
	 
	 @RequestMapping(value="/chksession", method=RequestMethod.GET)
	 public ModelAndView  chksession(HttpServletRequest req){
	  System.out.println("/chksession !!!!");
	   
	  HttpSession session = req.getSession();
	  System.out.println("chksession session id : " + session.getId() + ", session test value : " + session.getAttribute("test"));
	   
	  
	  return new ModelAndView("index_chk_session");

	 }

}
