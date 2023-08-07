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
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "confermaAllineaPercUsoEleggCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String confermaAllineaPercUsoEleggUrl = "../view/confermaAllineaPercUsoEleggView.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione allinea % uso eleggibile."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/confermaAllineaPercUsoElegg.htm";

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
      Vector<Long> elencoIDAllineaPercUsoElegg = (Vector<Long>)session.getAttribute("elencoIDAllineaPercUsoElegg");
      Vector<Long> vIdParticella = null;
      if(Validator.isNotEmpty(elencoIDAllineaPercUsoElegg))
      {
	      for(int i = 0; i < elencoIDAllineaPercUsoElegg.size(); i++) 
	      {
	        StoricoParticellaVO storicoParticellaVO = anagFacadeClient.findStoricoParticellaArboreaConduzione(elencoIDAllineaPercUsoElegg.get(i), 
	          anagAziendaVO.getIdAzienda().longValue());
	        if(vIdParticella == null)
	        {
	          vIdParticella = new Vector<Long>();
	        }
	        
	        if(!vIdParticella.contains(storicoParticellaVO.getIdParticella()))
	        {
	          vIdParticella.add(storicoParticellaVO.getIdParticella());
	        }         
	      }
	    }
      try
      { 
        gaaFacadeClient.allineaPercUsoElegg(anagAziendaVO.getIdAzienda().longValue(), vIdParticella, ruoloUtenza.getIdUtente().longValue());
      }
      catch(SolmrException se) 
      {
        String messaggio = errMsg+": "+AnagErrors.ERR_ALLIENEA_PERC_USO+".\n"+se.toString();
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
  	<jsp:forward page="<%= confermaAllineaPercUsoEleggUrl %>"/>
	

