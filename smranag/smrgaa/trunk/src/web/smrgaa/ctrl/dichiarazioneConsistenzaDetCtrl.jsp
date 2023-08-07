<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
	public final static String VIEW = "../view/dichiarazioneConsistenzaDetView.jsp";
%>

<%

	String iridePageName = "dichiarazioneConsistenzaDetCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaDetCtrl.jsp - INIZIO PAGINA");

 	try 
  {
   	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
   	String[] orderBy = null;   	
   	Long idDichiarazioneConsistenza = null;
   	Long idAllegato = null;
   	
   	String radiobutton = request.getParameter("radiobutton");
	  SolmrLogger.debug(this, "radiobutton: " +radiobutton);
	
	  if(Validator.isNotEmpty(radiobutton)) 
	  {
	    if(radiobutton.contains("_"))
	    {
	      StringTokenizer st = new StringTokenizer(radiobutton, "_");
	      idDichiarazioneConsistenza = new Long(st.nextToken());
	      idAllegato = new Long(st.nextToken());
	      request.setAttribute("idAllegato", idAllegato);
	    }
	    else
	    {
	      idDichiarazioneConsistenza = new Long(radiobutton);
	    }
	  }

   	SolmrLogger.debug(this, "idDichiarazioneConsistenza: " + idDichiarazioneConsistenza);

   	ConsistenzaVO consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
   	request.setAttribute("consistenzaVO", consistenzaVO);
     
    if(idDichiarazioneConsistenza != null)
    {
      FascicoloNazionaleVO fascicoloNazionaleVO = anagFacadeClient.getInfoRisultatiSianDichiarazioneConsistenza(idDichiarazioneConsistenza.longValue());
      request.setAttribute("fascicoloNazionaleVO", fascicoloNazionaleVO);
    }
    
    
    InvioFascicoliVO invioFascicoliVO = gaaFacadeClient.getLastSchedulazione(idDichiarazioneConsistenza.longValue());
    request.setAttribute("invioFascicoliVO", invioFascicoliVO);
    
    AllegatoDichiarazioneVO allegatoDichiarazioneVO = gaaFacadeClient
      .getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza, new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));
    request.setAttribute("allegatoDichiarazioneVO", allegatoDichiarazioneVO);
    
    
    Vector<AllegatoDichiarazioneVO> vAllegatiDichiarazioneVO = gaaFacadeClient
      .getAllElencoAllegatiDichiarazione(idDichiarazioneConsistenza);
    request.setAttribute("vAllegatiDichiarazioneVO", vAllegatiDichiarazioneVO);

   	// Recupero le eventuali anomalie
	  ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO = (ErrAnomaliaDicConsistenzaVO)session.getAttribute("errAnomaliaDicConsistenzaRicercaVO");    	
   	boolean isRefresh = false;
   	// L'utente ha selezionato il pulsante aggiorna
   	if(request.getParameter("aggiorna") != null || errAnomaliaDicConsistenzaRicercaVO != null) 
   	{
   		if(request.getParameter("aggiorna") == null) 
   		{
   			isRefresh = true;
   		}
   		errAnomaliaDicConsistenzaRicercaVO = new ErrAnomaliaDicConsistenzaVO();
   		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaBloccante(request.getParameter("segnalazioneBloccante"));
   		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaWarning(request.getParameter("segnalazioneWarning"));
   		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaOk(request.getParameter("segnalazioneOk"));
   		errAnomaliaDicConsistenzaRicercaVO.setIdGruppoControllo(request.getParameter("idGruppoControllo"));
   		errAnomaliaDicConsistenzaRicercaVO.setIdControllo(request.getParameter("idControllo"));
   		if(Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO.getIdControllo())) 
   		{
   			request.setAttribute("idControllo", Long.decode(errAnomaliaDicConsistenzaRicercaVO.getIdControllo()));
   		}
   	}
   	else 
   	{ 
  	  errAnomaliaDicConsistenzaRicercaVO = new ErrAnomaliaDicConsistenzaVO();
  		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaBloccante(SolmrConstants.FLAG_S);
   		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaWarning(SolmrConstants.FLAG_N);
   		errAnomaliaDicConsistenzaRicercaVO.setTipoAnomaliaOk(null);
   	}
   	session.setAttribute("errAnomaliaDicConsistenzaRicercaVO", errAnomaliaDicConsistenzaRicercaVO);
   	
   	// Carico la combo relativa al tipo gruppo controllo
   	CodeDescription[] elencoTipoGruppoControllo = null;
   	try 
   	{
   		orderBy = new String[]{SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
   		elencoTipoGruppoControllo = anagFacadeClient.getListTipoGruppoControlloByIdDichiarazioneConsistenza(idDichiarazioneConsistenza, orderBy);
   		request.setAttribute("elencoTipoGruppoControllo", elencoTipoGruppoControllo);
   	}
   	catch(SolmrException se) 
   	{
   		setError(request, AnagErrors.ERRORE_TIPO_GRUPPO_CONTROLLO);
   		%>
			<jsp:forward page="<%=VIEW%>" />
   		<%
   	}
   	
   	// Carico la combo relativa al controllo solo se è stato selezionato il gruppo controllo di riferimento
   	String gruppoControllo = request.getParameter("idGruppoControllo");
   	TipoControlloVO[] elencoTipoControllo = null;
   	if(Validator.isNotEmpty(gruppoControllo)) 
   	{
   		request.setAttribute("idGruppoControllo", Long.decode(gruppoControllo));
   		try 
   		{
   			orderBy = new String[]{SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
   			elencoTipoControllo = anagFacadeClient.getListTipoControlloByIdDichiarazioneConsistenzaAndIdGruppoControllo(idDichiarazioneConsistenza, Long.decode(gruppoControllo), orderBy);
   			request.setAttribute("elencoTipoControllo", elencoTipoControllo);
   		}
   		catch(SolmrException se) 
   		{
      	setError(request, AnagErrors.ERRORE_TIPO_CONTROLLO);
      	%>
   		  	<jsp:forward page="<%=VIEW%>" />
       	<%
      }
   	}
   	
   	ErrAnomaliaDicConsistenzaVO[] anomalie = (ErrAnomaliaDicConsistenzaVO[])session.getAttribute("anomalieDichiarazioniConsistenza");
   	if(!isRefresh) {
    	orderBy = new String[]{SolmrConstants.ORDER_BY_ID_GRUPPO_CONTROLLO_ASC, SolmrConstants.ORDER_BY_ORDINAMENTO_ASC, SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY_FOR_ANOMALIE};
    	anomalie = anagFacadeClient.getListAnomalieByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), SolmrConstants.ID_FASE_DICHIARAZIONE, errAnomaliaDicConsistenzaRicercaVO, orderBy);
    	session.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
   	}
   
   	// Se sono presenti anomalie
   	if(anomalie != null && anomalie.length > 0) 
   	{
   		HashMap elencoDocumentiAssociati = new HashMap();
   		// Recupero i documenti associati(nuova gestione) o i tipi documento associati(vecchia gestione)
   		for(int i = 0; i < anomalie.length; i++) 
   		{
   			ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = (ErrAnomaliaDicConsistenzaVO)anomalie[i];
   			// Se le anomalie riscontrate sono state risolte
   			if(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione() != null) {
    			// Se è presente l'id_documento vuol dire che è stato associato un documento
    			// alla correzione e quindi è stata applicata la nuova gestione
    			if(errAnomaliaDicConsistenzaVO.getIdDocumento() != null) {
    				DocumentoVO documentoVO = null;
    				try {
    					documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(errAnomaliaDicConsistenzaVO.getIdDocumento());
    					elencoDocumentiAssociati.put(errAnomaliaDicConsistenzaVO.getIdDichiarazioneCorrezione(), documentoVO);
    				}
    				catch(SolmrException se) 
    				{
    					setError(request, AnagErrors.ERRORE_KO_DOCUMENTO);
    			   	%>
						  	<jsp:forward page="<%=VIEW%>" />
    			    <%
    				}    			
    			}
   			}
   		}
   		// Metto l'elenco dei documenti in request
   		request.setAttribute("elencoDocumentiAssociati", elencoDocumentiAssociati);
   	}

    request.setAttribute("radiobutton", idDichiarazioneConsistenza);

   	%>
   		<jsp:forward page="<%= VIEW %>"/>
   	<%
 	}
 	catch(Exception e) 
 	{
   	if(e instanceof SolmrException) 
   	{
    	setError(request, e.getMessage());
   	}
   	else 
   	{
    	e.printStackTrace();
    	setError(request, "Si è verificato un errore di sistema");
   	}
   	%>
   		<jsp:forward page="<%=VIEW%>" />
   	<%
 	}

 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaDetCtrl.jsp - FINE PAGINA");
%>

<%!
	private void setError(HttpServletRequest request, String msg) {
    	SolmrLogger.error(this, "\n\n\n\n\n\n\n\n\n\n\nmsg="+msg+"\n\n\n\n\n\n\n\n");
    	ValidationErrors errors = new ValidationErrors();
    	errors.add("error", new ValidationError(msg));
    	request.setAttribute("errors", errors);
  	}
%>
