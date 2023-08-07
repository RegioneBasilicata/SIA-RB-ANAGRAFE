<%@ page language="java"   contentType="text/html" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="consistenzaVO" scope="request" class="it.csi.solmr.dto.anag.ConsistenzaVO" >
  <jsp:setProperty name="consistenzaVO" property="*" />
</jsp:useBean>

<%!
	public final static String VIEW = "/view/dichiarazioneAnomaliaView.jsp";
%><%
	String iridePageName = "dichiarazioneAnomaliaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	SolmrLogger.debug(this, " - dichiarazioneAnomaliaCtrl.jsp - INIZIO PAGINA");
	
	
	final String errMsg = "Impossibile procedere nella sezione crea una validazione."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/dichiarazioneConsistenza.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  //rimuovo eventuali parametri sessione messi in altra validazione
  session.removeAttribute("aziendaNuovaDichiarazioneVO");
  session.removeAttribute("flagInvioMail");
	
	
  Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");
  String operazione = request.getParameter("operazione");
  

  //per prima cosa vado a vedere se posso permettere di protocollare
  // Recupero da COMUNE il valore del parametro "DCNP" per la gestione della protocollazione
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  consistenzaVO.setPossibileProtocollo(false);
  try
  {
    if (SolmrConstants.FLAG_S.equals(anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_COMUNE_DCNP)))
    {
      consistenzaVO.setPossibileProtocollo(true);
    }
  }
  catch(SolmrException se) 
  {
    // Catturo l'eccezione ma non modifico il comportamento dell'applicativo: se non fosse possibile reperire il
    // valore da comune lo considero = null
  }
  request.setAttribute("consistenzaVO",consistenzaVO);

	try 
  {
  	Integer annoDichiarazione = null;
  	if(Validator.isNotEmpty(request.getParameter("anno"))) 
  	{
    	annoDichiarazione = new Integer(request.getParameter("anno"));
  	}
  	else 
  	{
    	annoDichiarazione = (Integer)session.getAttribute("anno");
  	}
  	session.setAttribute("anno", annoDichiarazione);
  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  	
  	
  	
  	
  	AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAziendaConValida(anagAziendaVO.getIdAzienda().longValue(), 
      SolmrConstants.RICHIESTA_VALIDAZIONE);
    boolean trovataRichiesta = false;
    if(Validator.isNotEmpty(aziendaNuovaVO) 
      && aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_VALIDATA) == 0)
    {
      trovataRichiesta = true;
      request.setAttribute("trovataRichiesta", "true");
    }
    


  	//if(request.getParameter("salva") != null)
  	if(Validator.isNotEmpty(operazione)
  	  && "salva".equalsIgnoreCase(operazione)) 
  	{
  		consistenzaVO.setIdMotivo(idMotivoDichiarazione.toString());
    	ValidationErrors errors = consistenzaVO.validate();
    	if(errors != null && errors.size() != 0) 
    	{
      	request.setAttribute("errors",errors);
    	}
    	else 
    	{
     		AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
     		consistenzaVO.setIdUtente(ruoloUtenza.getIdUtente().toString());
     		if(anagVO.isPossiedeDelegaAttiva()) 
     		{
     			it.csi.solmr.dto.anag.DelegaVO delegaVO = anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
     			if(delegaVO != null) 
     			{
       			it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = anagFacadeClient.serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(delegaVO.getIdIntermediario(), new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE), null);
       			if(intermediarioVO != null) 
       			{
         			consistenzaVO.setResponsabile(intermediarioVO.getResponsabile());
       			}
     			}
     		}

      	// Imposto id procedimento per anagrafe
      	consistenzaVO.setIdProcedimento(((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG")).toString());

        //Parte necessaria per la gestione protocollo
        if(!ruoloUtenza.isUtenteIntermediario() && !ruoloUtenza.isUtenteOPRGestore())
        {
          try
          {
            // Contatto il servizio di comune per reperire la sigla amministrazione
            String siglaAmministrazione = null;
            it.csi.solmr.dto.comune.AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
            // Se comune mi restituisce l'amministrazione di competenza, recupero la sigla per
            // l'inserimento del documento
            if(ammCompetenzaVO != null) 
            {
              siglaAmministrazione = ammCompetenzaVO.getSiglaAmministrazione();
              ruoloUtenza.setSiglaAmministrazione(siglaAmministrazione);
            }
            // Altrimenti segnalo l'errore perchè questa informazione è indispensabile per poter inserire
            // correttamente il documento
            else
            {
              SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+": "+AnagErrors.ERR_NEW_DICHIARAZIONE_CONSISTENZA_KO_FOR_COMUNE;
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
          }
          catch(SolmrException se)
          {           
            SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
				    String messaggio = errMsg+": "+AnagErrors.ERR_NEW_DICHIARAZIONE_CONSISTENZA_KO_FOR_COMUNE+".\n"+se.toString();
				    request.setAttribute("messaggioErrore",messaggio);
				    request.setAttribute("pageBack", actionUrl);
				    %>
				      <jsp:forward page="<%= erroreViewUrl %>" />
				    <%
				    return;
          }
     		}


        Long idDichiarazioneConsistenza = null;
        try
        {
      	  idDichiarazioneConsistenza = anagFacadeClient
      	    .salvataggioDichiarazionePLQSL(consistenzaVO,anagVO.getIdAzienda(),annoDichiarazione,ruoloUtenza);
      	}
      	catch(SolmrException se) 
	      {
	        SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_VALIDAZIONE+".\n"+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%=erroreViewUrl %>" />
	        <%
	        return;
	      }  
	      
      	session.removeAttribute("idMotivoDichiarazione");
      	session.setAttribute("aziendaNuovaDichiarazioneVO", aziendaNuovaVO);
      	session.setAttribute("flagInvioMail", consistenzaVO.getFlagInvioMail());
      	session.setAttribute("idDichiarazioneConsistenzaStampaPartenza", idDichiarazioneConsistenza);
      	
      	
          
        // Ricerco i dati dell'anagrafe in modo da presentare i dati aggiornati in quanto la dichiarazione
        // di consistenza a volte storicizza l'azienda
        try 
        {
          boolean delegaAttiva=anagAziendaVO.isPossiedeDelegaAttiva();
          DelegaVO delega=anagAziendaVO.getDelegaVO();
          anagAziendaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAzienda());
          anagAziendaVO.setPossiedeDelegaAttiva(delegaAttiva);
          anagAziendaVO.setDelegaVO(delega);
        }
        catch(SolmrException se) 
        {
           SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
				   String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_VALIDAZIONE+".\n"+se.toString();
				   request.setAttribute("messaggioErrore",messaggio);
				   request.setAttribute("pageBack", actionUrl);
				   %>
				     <jsp:forward page="<%= erroreViewUrl %>" />
				   <%
				   return;
        }
      
        // Metto il nuovo oggetto in sessione
        session.removeAttribute("anagAziendaVO");
        session.setAttribute("anagAziendaVO", anagAziendaVO);
          
          
      	response.sendRedirect("../layout/dichiarazioneConsistenza.htm");
      	return;
    	}
    }
   	// Recupero le eventuali anomalie
   	session.removeAttribute("anomalieDichiarazioniConsistenza");
   	Vector<ErrAnomaliaDicConsistenzaVO> anomalie = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);
   	if(anomalie != null) 
   	{
   		session.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
   		HashMap<String,DocumentoVO> elencoDocumenti = new HashMap<String,DocumentoVO>();
   		for(int i = 0; i < anomalie.size(); i++) 
   		{
   			ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)anomalie.elementAt(i);
   			if(errAnomaliaDicConsistenzaVO.getIdDocumento() != null) 
   			{
   				DocumentoVO documentoVO = null;
   				try 
   				{
   					documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(errAnomaliaDicConsistenzaVO.getIdDocumento());
   					elencoDocumenti.put(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione(), documentoVO);
   				}
   				catch(SolmrException se) 
   				{ 			    	
 			    	SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
	          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DOCUMENTO+".\n"+se.toString();
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("pageBack", actionUrl);
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;
   				}
   			}
   		}
   		request.setAttribute("elencoDocumenti", elencoDocumenti);
   	}
 	}
 	catch(Exception e) 
  {
  
    SolmrLogger.info(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_VALIDAZIONE+".\n"+e.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
 	}
 	
  SolmrLogger.debug(this, " - dichiarazioneAnomaliaCtrl.jsp - FINE PAGINA");
  %>
  <jsp:forward page="<%= VIEW %>"/>
