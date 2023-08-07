<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "annullaIstanzaRiesameCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String documentiElencoUrl = "/view/documentiElencoView.jsp";
  String annullaIstanzaRiesameUrl = "/view/annullaIstanzaRiesameView.jsp";
  String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione annulla istanza riesame."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String actionUrl = "../layout/documentiElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  DocumentoVO documentoVO = null;
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  // Recupero l'id del documento che si intende modificare
  
  ValidationErrors errors = new ValidationErrors();
  ValidationError error = null;
  String messaggioErrore = null;

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/annullaIstanzaRiesame.htm";
  
  
  if(request.getParameter("annulla") != null) 
  {    
    // Torno alla pagina della ricerca/elenco dei documenti
    %>
      <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
    <%
  }
  
  
  
  
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {     
    request.setAttribute("action", action);
    operazione = "conferma";
    request.setAttribute("operazione", operazione);
    String[] idIstanzaRiesame = request.getParameterValues("idIstanzaRiesame");
    session.setAttribute("idIstanzaRiesameFt", idIstanzaRiesame);
    String anno = request.getParameter("anno");
    session.setAttribute("annoFt", anno);
    
    %>
      <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
  else 
  {
	  // L'utente ha selezionato il tasto "conferma"
	  if("conferma".equalsIgnoreCase(operazione)) 
	  { 
	    String[] idIstanzaRiesaame = (String[])session.getAttribute("idIstanzaRiesameFt");
	    session.removeAttribute("idIstanzaRiesameFt");
	    String anno = (String)session.getAttribute("annoFt");
      session.removeAttribute("annoFt");
	    
	    if(Validator.isNotEmpty(idIstanzaRiesaame))
	    {
	      Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
	      for(int i=0;i<idIstanzaRiesaame.length;i++)
	        vIdIstanzaRiesame.add(new Long(idIstanzaRiesaame[i]));
	        
	      messaggioErrore = gaaFacadeClient.annullaIstanzaRiesame(vIdIstanzaRiesame, 
	        anagAziendaVO.getIdAzienda().longValue(), new Integer(anno).intValue(), ruoloUtenza);
	        
	      request.setAttribute("messaggioErrore", messaggioErrore);
	    
	    }    
	    
	    // Torno alla pagina della ricerca/elenco dei documenti
	    %>
	      <jsp:forward page="<%= annullaIstanzaRiesameUrl %>" />
	    <%
	  }
	
	
		//  Ricerco sul DB i dati del documento selezionato
	  try 
	  {
	    Long idDocumento = Long.decode(request.getParameter("idDocumento"));
	    
	    documentoVO = anagFacadeClient.getDettaglioDocumento(idDocumento, true); //Nuova modifica Luca 19/04/2010
	     
	    
	    if(!"S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
	    {
	      errors.add("error", new ValidationError(AnagErrors.ERRORE_DOCUMENTO_NON_ISTANZA_RIESAME));
		    request.setAttribute("errors", errors);
		    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
		    return;
	    }
	    
	    if(documentoVO.getFaseIstanzaRiesame() != SolmrConstants.FASE_IST_RIESAM_FOTO)
	    {
	      errors.add("error", new ValidationError(AnagErrors.ERRORE_DOCUMENTO_NON_ISTANZA_RIESAME_FOTO));
	      request.setAttribute("errors", errors);
	      request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
	      return;
	    }    
	  }
	  catch(SolmrException se) 
	  {
	    errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_DOCUMENTO));
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
	    return;
	  }
	  
	  // Controllo che la tipologia di documento selezionata non sia scaduta
	  if(documentoVO.getTipoDocumentoVO().getDataFineValidita() != null) 
	  {
	    errors.add("error", new ValidationError(AnagErrors.ERRORE_MODIFICA_DOCUMENTO_KO_FOR_TIPOLOGIA));
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
	    return;
	  }
	  
	  try 
	  {    
	    Vector<ParticellaIstanzaRiesameVO> vListParticelle = gaaFacadeClient
	      .getLisParticellaFromIstanzaFoto(anagAziendaVO.getIdAzienda().longValue(), 
	      DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
	      
	    if(vListParticelle != null)
	    {
	      request.setAttribute("vListParticelle", vListParticelle);
	      request.setAttribute("anno", ""+DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
	    }
	    else
	    {
	      messaggioErrore = "Non e' stata trovata nessuna particella annullabile per il documento corrente.";
	      request.setAttribute("messaggioErrore", messaggioErrore);
	    }   
	  }
	  catch(SolmrException se) 
	  {
	    errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_PARTICELLE_ISTANZA_FOTO));
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
	    return;
	  }
	}
	  
  
  
  
  

  
  
  
  
	// Vado alla pagina di modifica del documento
  %>
    <jsp:forward page="<%= annullaIstanzaRiesameUrl %>" />
    
    

  
