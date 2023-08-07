<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "dichiarazioneAnomaliaScegliMotivoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String dichiarazioneAnomaliaScegliMotivoUrl = "/view/dichiarazioneAnomaliaScegliMotivoView.jsp";
	String dichiarazioneConsistenzaCtrl = "../ctrl/dichiarazioneConsistenzaCtrl.jsp";
	String erroreViewUrl = "/view/erroreView.jsp";
  String dichiarazioneNewCtrl = "../ctrl/dichiarazioneNewCtrl.jsp";
  
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
	
	// Rimuovo l'eventuale tipo motivo dichiarazione precedentemente selezionato
	session.removeAttribute("idMotivoDichiarazione");
	//Rimuovo l'eventuale idVAlidazione rimasto dopo la creazione
	//session.removeAttribute("idDichiarazioneConsistenzaDaValidPartenza");
	//session.removeAttribute("idDichiarazioneConsistenzaDaValid");
	
	// Controllo che il CUAA dell'azienda agricola sia valorizzato
	if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
  {
	  //Non posso proseguire con la dichiarazione
    String messaggio = AnagErrors.ERR_KO_NEW_DICHIARAZIONE_CONSISTENZA_FOR_CUAA;
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", dichiarazioneConsistenzaCtrl);
    //E' stata fatta una previsione del piano colturale
    %>
    	<jsp:forward page="<%=erroreViewUrl%>" />
    <%
	}
	else 
  {
		// Recupero l'elenco dei tipi motivo dichiarazione
		Vector<TipoMotivoDichiarazioneVO> vMotivoDichiarazione = null;
		try 
		{
			vMotivoDichiarazione = anagFacadeClient.getListTipoMotivoDichiarazione(
			  anagAziendaVO.getIdAzienda().longValue());
			request.setAttribute("vMotivoDichiarazione", vMotivoDichiarazione);
		}
		catch(SolmrException se) 
		{
			String messaggio = AnagErrors.ERR_KO_TIPO_MOTIVO_DICHIARAZIONE;
	  	request.setAttribute("messaggioErrore",messaggio);
	  	%>
	  		<jsp:forward page="<%=dichiarazioneAnomaliaScegliMotivoUrl%>" />
	  	<%
		}
		
		
		try 
    {
      AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_VALIDAZIONE);
      request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    }
    catch(SolmrException se) 
    {
      String messaggio = AnagErrors.ERR_KO_RICERCA_DATI_RICH_AZ;
      request.setAttribute("messaggioErrore",messaggio);
      %>
        <jsp:forward page="<%=dichiarazioneAnomaliaScegliMotivoUrl%>" />
      <%
    }
    
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("avanti"))
    {
      try
      {
        String idMotivoDichiarazioneStr = request.getParameter("idMotivoDichiarazione");
        Long idMotivoDichiarazione = Long.decode(idMotivoDichiarazioneStr);
        if(anagFacadeClient.newDichConsAmmessa(
          anagAziendaVO.getIdAzienda(),ruoloUtenza.getIdUtente(),idMotivoDichiarazione)) 
        {
          request.setAttribute("operazioneAtt","attenderePrego");
          request.setAttribute("idMotivoDichiarazioneAtt",idMotivoDichiarazioneStr);
          %>
            <jsp:forward page="<%=dichiarazioneNewCtrl%>" />
          <%
        }
        else
        {
          // Non è possibile eliminare la dichiarazione
          ValidationErrors ve = new ValidationErrors();
          ve.add("error",new ValidationError(SolmrConstants.NO_NUOVA_DICHIARAZIONE_CONSISTENZA));
          request.setAttribute("errors",ve);
        }
      }
      catch(SolmrException s) {
        ValidationErrors ve = new ValidationErrors();
        ve.add("error",new ValidationError(s.getMessage()));
        request.setAttribute("errors",ve);
        s.printStackTrace();
      }
      catch(Exception e) {
        it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
        ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
        request.setAttribute("errors",ve);
        e.printStackTrace();
      }
    }
    
	}
	
	// Vado alla pagina della scelta tipo motivo dichiarazione
	%>
		<jsp:forward page="<%=dichiarazioneAnomaliaScegliMotivoUrl%>" />
	<%

%>