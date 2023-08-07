<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>


<%!
  public static String CONTROLLA_MODIFICHE_URL="../layout/dichiarazioneControllaModifiche.htm";
  public static String MODIFICHE_URL="/view/dichiarazioneModificheView.jsp";
%>


<%

  String iridePageName = "dichiarazioneControllaAnnoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  try
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    Integer anno;
    anno=new Integer(request.getParameter("radiobutton"));
    if (anno.intValue()==DateUtils.getCurrentYear().intValue())
    {
      //è stato selezionato l'anno corrente
      response.sendRedirect(CONTROLLA_MODIFICHE_URL);
    }
    else
    {
      if (anagClient.controlloUltimeModifiche(anagVO.getIdAzienda(),DateUtils.getCurrentYear()))
      {
        %><jsp:forward page="<%=MODIFICHE_URL%>" /><%
      }
      else
      {
        request.setAttribute("anno",anno);
                
        //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
    	ResourceBundle res = ResourceBundle.getBundle("config");
    	String ambienteDeploy = res.getString("ambienteDeploy");
    	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
    	String URL ="";
    	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
    		URL = "../layout/";
    	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
    		URL = "/layout/";
    	URL += "dichiarazioneControllaModifiche.htm";
        
        
        %><jsp:forward page="<%=URL%>" /><%
      }
    }
  }
  catch(SolmrException s)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new ValidationError(s.getMessage()));
    request.setAttribute("errors",ve);
    s.printStackTrace();
  }
  catch(Exception e)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    e.printStackTrace();
  }
%>

