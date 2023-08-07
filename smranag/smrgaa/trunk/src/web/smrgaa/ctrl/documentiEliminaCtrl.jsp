<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "documentiEliminaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String documentiElencoUrl = "/view/documentiElencoView.jsp";
  String documentiEliminaUrl = "/view/documentiEliminaView.jsp";
  String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Setto l'area di provenienza
  request.setAttribute("pageFrom", "documentale");

  // L'utente ha selezionato il tasto conferma
  if(request.getParameter("conferma") != null) 
  {
    String[] idDocumento = (String[])session.getAttribute("documentiDaEliminare");
    for(int i=0;i<idDocumento.length;i++)
    {
      DocumentoVO documentoVO = anagFacadeClient.getDettaglioDocumento(Long.decode((String)idDocumento[i]), 
        true);  
      
     
      
      
	    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesameNoModTotale()))
	    {      
        error = new ValidationError(AnagErrors.ERRORE_DOC_ANNULLA_IST_RIE_LAVORATA);
        errors.add("particelleLavorate", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
        return;
            
	    }
	    
	    
	    
	    
	    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {
        if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
            || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
        {             
          
          
          if(gaaFacadeClient.isSitiConvocaValid(anagAziendaVO.getIdAzienda().longValue(),
            DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame()))
          {
            error = new ValidationError(AnagErrors.ERRORE_DOC_ANNUL_MOD_IST_RIE_CONVOCA);
            errors.add("particelleLavorate", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
            return;
          }
        }
        
        if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
          || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO))
        {           
          String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };
          Vector<StoricoParticellaVO> elencoParticelleIstanza = anagFacadeClient
              .getListParticelleByIdDocumento(documentoVO.getIdDocumento(), orderBy);
          Vector<Long> vIdParticella = new Vector<Long>(); 
          for(int j=0;j<elencoParticelleIstanza.size();j++)
          {
            if(!vIdParticella.contains(elencoParticelleIstanza.get(j).getIdParticella()))
            {
              vIdParticella.add(elencoParticelleIstanza.get(j).getIdParticella());
	            StoricoParticellaVO stvoTmp = elencoParticelleIstanza.get(j);
	                       
	              
              if(gaaFacadeClient.isParticellaEvasa(anagAziendaVO.getIdAzienda().longValue(),
                stvoTmp.getIdParticella().longValue(), documentoVO.getFaseIstanzaRiesame(), 
                DateUtils.extractYearFromDate(documentoVO.getDataInserimento())))
              {
                error = new ValidationError(AnagErrors.ERRORE_DOC_ANNUL_PARTICELLA_EVASA);
                errors.add("particelleLavorate", error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
                return;
              }
	          }
             
            
          }
           
        }
      
        
      }
	    
	    
	    
	  }

    String note = request.getParameter("note");

    // Se tra i documenti selezionati ve ne era almeno uno con il numero di protocollo...
    if(Validator.isNotEmpty((String)session.getAttribute("hasNumeroProtocollo"))) 
    {
      // Controllo che le note siano state valorizzate
      if(!Validator.isNotEmpty(note)) 
      {
        error = new ValidationError((String)AnagErrors.get("ERR_CAMPO_OBBLIGATORIO"));
        errors.add("note", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
        return;
      }
      // Se valorizzate controllo che non siano più lunghe di 500 caratteri
      else 
      {
        if(note.length() > 500) 
        {
          request.setAttribute("note", note);
          error = new ValidationError((String)AnagErrors.get("ERR_NOTE_ERRATO"));
          errors.add("note", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
          return;
        }
      }
    }

    // Se passo tutti i controlli procedo con l'eliminazione
    try 
    {
      anagFacadeClient.deleteDocumenti((String[])session.getAttribute("documentiDaEliminare"), note, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) {
      error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(documentiEliminaUrl).forward(request, response);
      return;
    }

    // Torno alla pagina di ricerca/elenco dei documenti
    %>
       <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
    <%
  }

  // L'utente ha selezionato il tasto annulla
  else if(request.getParameter("annulla") != null) {
    // Pulisco la sessione
    session.removeAttribute("documentiDaEliminare");

    // Torno alla pagina della ricerca/elenco dei documenti
    %>
       <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
    <%
  }

  // L'utente ha selezionato la funzione elimina dal menù "documenti"
  else 
  {

    // Pulisco la sessione
    session.removeAttribute("hasNumeroProtocollo");
    session.removeAttribute("documentiDaEliminare");
    session.removeAttribute("documentiDaEliminare");

    String[] documentiDaEliminare = request.getParameterValues("idDocumento");

    // Controllo che per i documenti selezionati non ce ne siano di annullati o storicizzati
    DocumentoVO documentoVO = null;
    for(int i = 0; i < documentiDaEliminare.length; i++) 
    {
      try 
      {
        documentoVO = anagFacadeClient.getDettaglioDocumento(Long.decode((String)documentiDaEliminare[i]), 
          false);
      }
      catch(SolmrException se) {
        error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentiElencoUrl).forward(request, response);
        return;
      }

      if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) 
      {
        error = new ValidationError((String)AnagErrors.get("ERR_ELIMINA_DOCUMENTO_KO"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
        return;
      }
      
      if(Validator.isNotEmpty(documentoVO.getTipoDocumentoVO()) 
        && "N".equalsIgnoreCase(documentoVO.getTipoDocumentoVO().getFlagAnnullabile()))
      {      
        error = new ValidationError(AnagErrors.ERRORE_DOC_ANNULLA_IMPOSSIBILE);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
        return;
      }

      if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
        session.setAttribute("hasNumeroProtocollo", "hasNumeroProtocollo");
      }
      session.setAttribute("documentiDaEliminare", documentiDaEliminare);
    }

    // Se invece passo i controlli vado alla pagina di richiesta conferma dell'operazione
    %>
       <jsp:forward page="<%= documentiEliminaUrl %>" />
    <%
  }

%>
