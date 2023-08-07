<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO" %>
<%@ page import="java.util.*" %>

<%

  
	String iridePageName = "fabbricatiDetCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String url = "../view/fabbricatiView.jsp";
	String fabbricatoDetUrl = "../view/fabbricatiDetView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	ValidationErrors errors = new ValidationErrors();

	FabbricatoVO fabbricatoVO = null;
  ConsistenzaVO consistenzaVO = null;
  	
	// Recupero il piano di riferimento
	//Long idPianoRiferimento = (Long)session.getAttribute("idPianoRiferimentoFabbricato");
  
  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
  Long idPianoRiferimento = null;
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    idPianoRiferimento = new Long("-1");
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }

	// Ho cliccato sulla voce di menù "dettaglio"
	// Controllo che l'utente abbia selezionato un fabbricato da visualizzare
	String idFabbricato = request.getParameter("idFabbricato");
	if(!Validator.isNotEmpty(idFabbricato)) 
  {
  	ValidationError error = new ValidationError((String)AnagErrors.get("ERR_FABBRICATO_NO_SELEZIONATO"));
  	errors.add("error", error);
  	request.setAttribute("errors", errors);
  	request.getRequestDispatcher(url).forward(request, response);
  	return;
	}
	// Se è stato selezionato il fabbricato da visualizzare ricerco i suoi dati su DB
	else 
  {
  	try 
    {
  		fabbricatoVO = anagFacadeClient.findFabbricatoByPrimaryKey(Long.decode(idFabbricato));
      //Aggiunta tipologia fabbricato!!!!
      TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = gaaFacadeClient.getInfoTipologiaFabbricato(fabbricatoVO.getIdTipologiaFabbricato());
      fabbricatoVO.setTipoTipologiaFabbricatoVO(tipoTipologiaFabbricatoVO);
      Date dataInserimentoDichiarazione = null;
      if(idPianoRiferimento.compareTo(new Long(-1)) != 0) 
      {  
        consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(new Long(idPianoRiferimento));
        dataInserimentoDichiarazione = consistenzaVO.getDataInserimentoDichiarazione();
      }
      
      FabbricatoBioVO fabbricatoBioVO = gaaFacadeClient
        .getFabbricatoBio(fabbricatoVO.getIdFabbricato().longValue(), 
        anagAziendaVO.getIdAzienda().longValue(), dataInserimentoDichiarazione);
      request.setAttribute("fabbricatoBioVO", fabbricatoBioVO);
      
      
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(se.getMessage());
  		errors.add("error", error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(url).forward(request, response);
  		return;
  	}

  	// Metto l'oggetto in sessione
  	session.setAttribute("fabbricatoVO", fabbricatoVO);

  	// Ricerco la informazioni relative all'unità produttiva su cui si trova il fabbricato
  	UteVO uteVO = null;
  	try 
    {
    	uteVO = anagFacadeClient.getUteById(fabbricatoVO.getIdUnitaProduttivaFabbricato());
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(se.getMessage());
  		errors.add("error", error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(url).forward(request, response);
  		return;
  	}	
  	// Metto l'oggetto in request
  	request.setAttribute("uteVO", uteVO);

  	// Ricerco le particella su cui insiste il fabbricato
  	StoricoParticellaVO[] elencoParticelleFabbricato = null;
  	try 
    {
  		String[] orderBy = {SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY};
  		elencoParticelleFabbricato = anagFacadeClient.getListParticelleForFabbricato(Long.decode(idFabbricato), idPianoRiferimento, orderBy);
  		request.setAttribute("elencoParticelleFabbricato", elencoParticelleFabbricato);
  	}
  	catch(SolmrException se) 
    {
  		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_PARTICELLE_FABBRICATO);
  		errors.add("error", error);
  		request.setAttribute("errors", errors);
  		request.getRequestDispatcher(url).forward(request, response);
  		return;
  	}
  	// Vado alla pagina del dettaglio
  	%>
		<jsp:forward page="<%= fabbricatoDetUrl %>"/>
  	<%
	}
%>

