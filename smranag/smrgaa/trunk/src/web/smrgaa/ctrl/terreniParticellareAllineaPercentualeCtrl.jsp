<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="java.util.ResourceBundle" %>


<%

  String iridePageName = "terreniParticellareAllineaPercentualeCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - terreniParticellareAllineaPercentualeCtrl.jsp - INIZIO PAGINA");
  
  String terreniParticellareAllineaPercentualeURL = "/view/terreniParticellareAllineaPercentualeView.jsp";
  
  
    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
    String actionUrl ="";
    String action = "";
    
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		actionUrl = "../layout/";
		action = "../layout/";
	}	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		actionUrl = "/layout/";
		action = "../layout/";
	}	
	actionUrl += "terreniParticellareElenco.htm";
	SolmrLogger.debug(this, "-- actionUrl ="+actionUrl);
	action += "terreniParticellareAllineaPercentuale.htm";
	SolmrLogger.debug(this, "-- action ="+action);
  
  
  
  String erroreViewUrl = "/view/erroreView.jsp";
  String operazione = request.getParameter("operazione");
  
  
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  
  
  
  final String errMsg = "Impossibile procedere nella sezione allinea percentuale. " +
    "Contattare l'assistenza comunicando il seguente messaggio: "; 
       
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String messaggioErrore = null;
  
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {
    String tipoAllinea = request.getParameter("tipoAllinea");
    operazione = "confermaAllineaSup";
    if(Validator.isNotEmpty(request.getParameter("tipoAllinea"))
        && request.getParameter("tipoAllinea").equalsIgnoreCase("1"))
    {
      operazione = "confermaAllineaSupUtil";
    }    
    request.setAttribute("action", action);
    request.setAttribute("operazione", operazione);
    String percentualePossessoIn = request.getParameter("percentualePossessoIn");
    session.setAttribute("percentualePossessoIn", percentualePossessoIn);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else
  {
	  //sono già nella pagine e richiamo uno scarico
	  if(Validator.isNotEmpty(operazione) 
      && operazione.equalsIgnoreCase("confermaAllineaSupUtil")) 
    {    
      //Allinea la percentuale di possesso rispetto alla superficie utilizzata
   
      try 
      {
        messaggioErrore = gaaFacadeClient
          .cambiaPercentualePossessoSupUtilizzataMassivo(ruoloUtenza,
          anagAziendaVO.getIdAzienda().longValue());
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniParticellareAllineaPercentualeCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_PERCENTUALE_POSSESSO+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      if(Validator.isNotEmpty(messaggioErrore))
      {
        request.setAttribute("messaggioErrore", messaggioErrore);        
        %>
         <jsp:forward page= "<%= terreniParticellareAllineaPercentualeURL %>" />
        <%
        return;
      }
      else
      {
        session.removeAttribute("percentualePossessoIn");
        %>
          <jsp:forward page= "<%= actionUrl %>" />
        <%
        return;
      }
        
        
	  }
	  else if(Validator.isNotEmpty(operazione) 
     && operazione.equalsIgnoreCase("confermaAllineaSup")) 
	  {
	    String percentualePossessoIn = (String)session.getAttribute("percentualePossessoIn");
	      
	    // Verifico che effettivamente sia stata selezionata
	    if(!Validator.isNotEmpty(percentualePossessoIn)) 
	    {
	      messaggioErrore = "Il campo percentuale possesso è obbligatorio"; 
	      request.setAttribute("messaggioErrore", messaggioErrore);        
	      %>
	        <jsp:forward page= "<%= terreniParticellareAllineaPercentualeURL %>" />
	      <%
	      return;    
	    }
	    else 
	    {
	      if(!Validator.isNumberPercentualeMaggioreZeroDecimali(percentualePossessoIn))
	      {	          
	        messaggioErrore = "la percentuale possesso deve essere maggiore di 0 e minore o uguale a 100"; 
	        request.setAttribute("messaggioErrore", messaggioErrore);        
	        %>
	          <jsp:forward page= "<%= terreniParticellareAllineaPercentualeURL %>" />
	        <%
	        return;
	      }
	      else
	      {
	        BigDecimal percentualePossessoBg = new BigDecimal(percentualePossessoIn.replace(",","."));
	       
	        try 
          {
		        messaggioErrore = gaaFacadeClient
		          .cambiaPercentualePossessoMassivo(ruoloUtenza, 
		            anagAziendaVO.getIdAzienda().longValue(), percentualePossessoBg);
		      }
		      catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - terreniParticellareAllineaPercentualeCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_PERCENTUALE_POSSESSO+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
           
          if(Validator.isNotEmpty(messaggioErrore))
          {
	          request.setAttribute("messaggioErrore", messaggioErrore);        
	          %>
	            <jsp:forward page= "<%= terreniParticellareAllineaPercentualeURL %>" />
	          <%
	          return;
	        }
	        else
	        {
	          session.removeAttribute("percentualePossessoIn");        
            %>
              <jsp:forward page= "<%= actionUrl %>" />
            <%
            return;
	        }
	          
	          
	      }         
	        
	    }
	      
	  }        
	  //prima volta che entro
	  else
	  {	  
	    session.removeAttribute("percentualePossessoIn");
	  }
	}
%>
   <jsp:forward page="<%=terreniParticellareAllineaPercentualeURL %>" />