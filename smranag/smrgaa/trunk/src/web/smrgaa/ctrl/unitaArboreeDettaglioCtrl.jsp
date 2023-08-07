<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
	
	String iridePageName = "unitaArboreeDettaglioCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String unitaArboreeDettaglioUrl = "/view/unitaArboreeDettaglioView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
	StoricoParticellaVO storicoParticellaVO = null;
	AltroVitignoVO[] elencoAltriVitigni = null;
	AltroVitignoDichiaratoVO[] elencoAltriVitigniDichiarati = null;
  Vector<TipoTipologiaVinoVO> elencoDocRicaduta = null;
  //se piano in lavorazione è idStoricoUnitaArborea altrimenti idUnitaArboreaDichiarata
	Long idUnita = Long.decode(request.getParameter("idUnita"));
	request.setAttribute("idStoricoUnitaArborea", idUnita); 
  HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
	String pagina = request.getParameter("pagina");
  
  
  
  
  
  
  // Gestione della selezione delle unità arboree
  Vector<Long> elenco = new Vector<Long>();
  if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) 
  {
    numUnitaArboreeSelezionate.remove(pagina);
    if(idUnita != null) 
    {
      numUnitaArboreeSelezionate.remove(pagina);
      elenco.add(idUnita);
    }
  }
  else 
  {
    if(numUnitaArboreeSelezionate == null) {
      numUnitaArboreeSelezionate = new HashMap<String,Vector<Long>>();
    }
    // Se non ho selezionato il pulsante annulla
    if(idUnita != null) 
    {
      elenco.add(idUnita);
    }
  }
  if(elenco.size() > 0) {
    numUnitaArboreeSelezionate.put(pagina, elenco);
    session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
  }
  
  
  
  
  
  
  
  
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionato dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriUnitaArboreaRicercaVO == null 
    || !Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento())) 
  {
		String messaggio = AnagErrors.ERRORE_KO_DETTAGLIO_UNAR;
 		request.setAttribute("messaggioErrore",messaggio);
 		%>
		<jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
 		<%
 		return;
	}
	else 
  {		
		// Cerco i dati dell'unità arborea in relazione al piano di riferimento selezionato
		try 
    {
			if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
      {
				storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idUnita);
				if(storicoParticellaVO != null) 
        {
					try 
          {
						String[] orderBy = {SolmrConstants.ORDER_BY_DESCRIZIONE_VARIETA_ASC};
						elencoAltriVitigni = anagFacadeClient.getListAltroVitignoByIdStoricoUnitaArborea(idUnita, orderBy);
					}
					catch(SolmrException se) 
          {
						String messaggio = AnagErrors.ERRORE_KO_ALTRI_VITIGNI;
			   		request.setAttribute("messaggioErrore",messaggio);
			   		%>
			    		<jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
			   		<%
			   		return;	
					}
          
          //metto solo se il vino è DOC
          if((storicoParticellaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO() != null)
            && "S".equalsIgnoreCase(storicoParticellaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getVinoDoc()))
          {
            try 
            {
              elencoDocRicaduta = anagFacadeClient.getListTipoTipologiaVinoRicaduta(
                storicoParticellaVO.getIstatComune(),
                storicoParticellaVO.getStoricoUnitaArboreaVO().getIdVarieta().longValue(),
                storicoParticellaVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getIdTipologiaVino().longValue(), null);
            }
            catch(SolmrException se) 
            {
              String messaggio = AnagErrors.ERRORE_KO_DOC_RICADUTA;
              request.setAttribute("messaggioErrore",messaggio);
              %>
                <jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
              <%
              return; 
            }
          }
				}
			}
			else 
      {
				storicoParticellaVO = anagFacadeClient.findParticellaArboreaDichiarata(idUnita);
				if(storicoParticellaVO != null) 
        {
					try 
          {
						String[] orderBy = {SolmrConstants.ORDER_BY_DESCRIZIONE_VARIETA_ASC};
						elencoAltriVitigniDichiarati = anagFacadeClient.getListAltroVitignoDichiaratoByIdUnitaArboreaDichiarata(idUnita, orderBy);
					}
					catch(SolmrException se) 
          {
						String messaggio = AnagErrors.ERRORE_KO_ALTRI_VITIGNI;
			   		request.setAttribute("messaggioErrore",messaggio);
			   		%>
			    		<jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
			   		<%
			   		return;	
					}
          
          //metto solo se il vino è DOC
          if((storicoParticellaVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO() != null)
            && "S".equalsIgnoreCase(storicoParticellaVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO().getVinoDoc()))
          {
            try 
            {
              elencoDocRicaduta = anagFacadeClient.getListTipoTipologiaVinoRicaduta(
                storicoParticellaVO.getIstatComune(),
                storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdVarieta().longValue(),
                storicoParticellaVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO().getIdTipologiaVino().longValue(), 
                storicoParticellaVO.getUnitaArboreaDichiarataVO().getDataInserimentoDichiarazione());
            }
            catch(SolmrException se) 
            {
              String messaggio = AnagErrors.ERRORE_KO_DOC_RICADUTA;
              request.setAttribute("messaggioErrore",messaggio);
              %>
                <jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
              <%
              return; 
            }
          }
				}
			}
		}
		catch(SolmrException se) 
    {
			String messaggio = AnagErrors.ERRORE_KO_DETTAGLIO_UNAR;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		%>
	    		<jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
	   		<%
	   		return;
		}
		request.setAttribute("storicoParticellaVO", storicoParticellaVO);
		if(elencoAltriVitigni != null && elencoAltriVitigni.length > 0) 
    {
			request.setAttribute("elencoAltriVitigni",elencoAltriVitigni);
		}
		else 
    {
			if(elencoAltriVitigniDichiarati != null && elencoAltriVitigniDichiarati.length > 0) 
      {
				request.setAttribute("elencoAltriVitigniDichiarati",elencoAltriVitigniDichiarati);
			}
		}
    
    request.setAttribute("elencoDocRicaduta", elencoDocRicaduta);
    
	}
	
	// Vado alla pagina di dettaglio
	%>
		<jsp:forward page="<%=unitaArboreeDettaglioUrl%>" />
