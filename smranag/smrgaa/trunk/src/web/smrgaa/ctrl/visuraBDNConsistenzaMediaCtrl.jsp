<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.teramo.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.ws.ResponseWsBridgeVO" %>
<%@ page import="it.csi.smranags.wsbridge.dto.bdn.WBConsistenzaStatAllevamentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "visuraBDNConsistenzaMediaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String visuraBDNConsistenzaMediaUrl = "/view/visuraBDNConsistenzaMediaView.jsp";
  String action = "../layout/visuraBDNConsistenzaMedia.htm";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String erroreUrl = "/view/erroreView.jsp";
  String allevamentiUrl = "/view/allevamentiView.jsp";
  String messaggioErroreBovini = "";
  String messaggioErroreCaprini = "";


  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String operazione = request.getParameter("operazione");

  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();

  // L'utente ha selezionato la voce di menù consisetnza media e io lo mando alla
  // pagina di attesa per il caricamento dati
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {
    request.setAttribute("action", action);
    operazione = null;
    request.setAttribute("operazione", operazione);
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else
  {
    
    //estraggo le specie in possesso tra gli allevamenti..
    Vector<Long> vIdSpecieAzienda = gaaFacadeClient.getElencoSpecieAzienda(
      anagAziendaVO.getIdAzienda().longValue());
      
    if(Validator.isNotEmpty(vIdSpecieAzienda)
     && (vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BOVINI_ALLEVAMENTO)
          || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BOVINI_CARNE)
          || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BUFALINI)
          || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_OVINI)
          || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_CAPRINI)))
    {
      
	    if(Validator.isNotEmpty(vIdSpecieAzienda)
	      && (vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BOVINI_ALLEVAMENTO)
	        || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BOVINI_CARNE)
	        || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_BUFALINI)))
	    {  
		    try 
		    {
		       ResponseWsBridgeVO responseWsBridgeVO = gaaFacadeClient.serviceConsistenzaStatisticaMediaAllevamento(
		         anagAziendaVO.getCUAA());
		         
		         
		       if(Validator.isNotEmpty(responseWsBridgeVO.getvDati()))
		       {
		         HashMap<String,String> hCodDescSpecie = new HashMap<String,String>();
		         Vector<Object> vConsistenzaBovini = 
		           (Vector<Object>)responseWsBridgeVO.getvDati();
		         for(int i=0;i<vConsistenzaBovini.size();i++)
		         {
		           WBConsistenzaStatAllevamentoVO consAll = (WBConsistenzaStatAllevamentoVO)vConsistenzaBovini.get(i);
		           if(Validator.isNotEmpty(consAll.getCodiceSpecie()))
		           {
		             if(hCodDescSpecie.get(consAll.getCodiceSpecie()) == null)
		             {
		               StringcodeDescription sianTipoSpecie = anagFacadeClient.getSianTipoSpecieByCodiceSpecie(consAll.getCodiceSpecie());
		               if(Validator.isNotEmpty(sianTipoSpecie.getDescription()))
		               {
		                 hCodDescSpecie.put(consAll.getCodiceSpecie(), sianTipoSpecie.getDescription());
		               }
		             }
		           }
		         }
		         request.setAttribute("hCodDescSpecie", hCodDescSpecie);        
		         request.setAttribute("vConsistenzaBovini", responseWsBridgeVO.getvDati()); 
		       }
		       
		       if(Validator.isNotEmpty(responseWsBridgeVO.getvErrori()))
		       {
		         messaggioErroreBovini = "SEGNALAZIONE BDN Bovini/Bufalini:<br>";
		         if(responseWsBridgeVO.getvErrori().size() > 0)
		         {
			         for(int i=0;i<responseWsBridgeVO.getvErrori().size();i++)
			         {           
			           CodeDescription cd = responseWsBridgeVO.getvErrori().get(i);
			           messaggioErroreBovini += cd.getCode()+" - "+cd.getCodeFlag()+" - "+cd.getDescription()+"<br>";
			         }
			       }
			       else
			       {
			         messaggioErroreBovini += "il servizio BDN non ritorna nessun dato";
			       }        
		       }
		       else if (Validator.isEmpty(responseWsBridgeVO.getvDati()))
		       {
		         messaggioErroreBovini += "il servizio BDN non ritorna nessun dato";
		       }
		       
		       if(Validator.isNotEmpty(messaggioErroreBovini))
		       {          
	           request.setAttribute("messaggioErroreBovini", messaggioErroreBovini);
	         }
		     }
		     catch(SolmrException se) 
		     {
		      // messaggioErroreBovini = se.getMessage();		      
		       //request.setAttribute("messaggioErroreBovini", messaggioErroreBovini);
		       request.setAttribute("messaggioErroreBovini", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
		       %>
		          <jsp:forward page="<%= visuraBDNConsistenzaMediaUrl %>"/>
		       <%
		     }
		   }
	     
	     if(Validator.isNotEmpty(vIdSpecieAzienda)
	      && (vIdSpecieAzienda.contains(SolmrConstants.SPECIE_OVINI)
	        || vIdSpecieAzienda.contains(SolmrConstants.SPECIE_CAPRINI)))
	     {
		     try 
		     {
		       ResponseWsBridgeVO responseWsBridgeVO = gaaFacadeClient.serviceConsistenzaUbaCensimOvini(
		         anagAziendaVO.getCUAA());
		         
		         
		       if(Validator.isNotEmpty(responseWsBridgeVO.getvDati()))
		       {       
		         request.setAttribute("vConsistenzaCaprini", responseWsBridgeVO.getvDati()); 
		       }
		       
		       if(Validator.isNotEmpty(responseWsBridgeVO.getvErrori()))
		       {
		         messaggioErroreCaprini = "SEGNALAZIONE BDN Ovini/Caprini:<br>";
		         if(responseWsBridgeVO.getvErrori().size() > 0)
		         {
			         for(int i=0;i<responseWsBridgeVO.getvErrori().size();i++)
			         {           
			           CodeDescription cd = responseWsBridgeVO.getvErrori().get(i);
			           messaggioErroreCaprini += cd.getCode()+" - "+cd.getCodeFlag()+" - "+cd.getDescription()+"<br>";
			         }
			       }
			       else
			       {
			         messaggioErroreCaprini += "il servizio BDN relativo agli ovini/caprini non ritorna nessun dato";
			       }	                
		       }
		       else if (Validator.isEmpty(responseWsBridgeVO.getvDati()))
	         {
	           messaggioErroreCaprini += "il servizio BDN non ritorna nessun dato";
	         }
	         
	         if(Validator.isNotEmpty(messaggioErroreCaprini))
	         {          
	           request.setAttribute("messaggioErroreCaprini", messaggioErroreCaprini);
	         }
		     }
		     catch(SolmrException se) 
		     {
		       /*messaggioErroreCaprini = se.getMessage();
		       request.setAttribute("messaggioErroreCaprini", messaggioErroreCaprini);*/
		       request.setAttribute("messaggioErroreCaprini", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
		       %>
		          <jsp:forward page="<%= visuraBDNConsistenzaMediaUrl %>"/>
		       <%
		     }
		   }
		 }
		 else
		 {
		   request.setAttribute("messaggioErroreBovini", "non sono presenti allevamenti Ovini/Caprini/Bovini/Bufalini su Anagrafe");
		 }
   }
     
 %>
   <jsp:forward page="<%= visuraBDNConsistenzaMediaUrl %>"/>

