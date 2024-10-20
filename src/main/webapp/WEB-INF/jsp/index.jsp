<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%> 
 <%  

System.out.println( "Session ID : " + session.getId() );  
//session.setAttribute("test", "session µ¿±âÈ­");
 
 %> 
<HTML> 
<HEAD> 
    <TITLE>Session Clustering Test</TITLE> 

</HEAD> 
<BODY> 
<h1>Session Clustering Test</h1> <%=session.getId() %> <br>
session : <%=session.getAttribute("test") %>
 

 
</BODY> 
</HTML>