<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%

	String iridePageName = "confermaAllineaUVCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	String confermaAllineaUVUrl = "../view/confermaAllineaUVView.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione allinea piano colturale."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/confermaAllineaUV.htm";

	if("attenderePrego".equalsIgnoreCase(operazione)) 
  {     
    request.setAttribute("action", action);
    operazione = "conferma";
    request.setAttribute("operazione", operazione);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else 
  {
    // Ho selezionato il pulsante conferma
    if(Validator.isNotEmpty(operazione)  && operazione.equalsIgnoreCase("conferma")) 
    {
      Vector<Long> elencoIDAllineaUV = (Vector<Long>)session.getAttribute("elencoIDAllineaUV");
      BigDecimal[] idStoricoUnitaArborea = null;
      if(elencoIDAllineaUV != null)
      {        
        idStoricoUnitaArborea = new BigDecimal[elencoIDAllineaUV.size()];
        for(int i=0;i<elencoIDAllineaUV.size();i++)
        {
          idStoricoUnitaArborea[i]=new BigDecimal(elencoIDAllineaUV.get(i));
        }
      }
      try
      { 
        anagFacadeClient.ribaltaUVOnPianoColturale(anagAziendaVO.getIdAzienda(), idStoricoUnitaArborea, ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) 
      {
        String messaggio = errMsg+": "+AnagErrors.ERR_ALLIENEA_PIANO_COLTURALE+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      //String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
      //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	  ResourceBundle res = ResourceBundle.getBundle("config");
	  String ambienteDeploy = res.getString("ambienteDeploy");
	  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	  String url ="";
	  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
		  url = "../layout/";
	  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
		  url = "/layout/";
	  url += "terreniUnitaArboreeElenco.htm";
	
      
      %>
        <jsp:forward page="<%= url %>" />
      <%
      return;  
    }
  }
	
  	
	
	// Vado alla pagina di richiesta conferma eliminazione
	%>
  	<jsp:forward page="<%= confermaAllineaUVUrl %>"/>
	

