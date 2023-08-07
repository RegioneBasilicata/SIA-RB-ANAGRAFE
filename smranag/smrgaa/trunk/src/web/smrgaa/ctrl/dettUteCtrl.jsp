<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%

	String iridePageName = "dettUteCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	if(request.getParameter("elenco") != null) {
  	    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  		ResourceBundle res = ResourceBundle.getBundle("config");
  		String ambienteDeploy = res.getString("ambienteDeploy");
  		SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  		String SEDI ="";
  		if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
  			SEDI = "../layout/";
  		else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
  			SEDI = "/layout/";
  		SEDI += "sedi.htm";
  		
  		
  		
    	%>
      		<jsp:forward page= "../layout/sedi.htm"/>
      		<jsp:forward page="<%= SEDI %>" />      	
    	<%
    	session.removeAttribute("uteVO");
  	}
  	else{
    	%>
      		<jsp:forward page= "../view/dettUteView.jsp"/>
    	<%
  	}

%>