<%@ page language="java" contentType="text/html" isErrorPage="true"%>
<%@page import="it.csi.solmr.exception.SolmrException"%>
<%@page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.util.Validator"%>
<%@page
	import="it.csi.smranag.smrgaa.dto.ParticellaDettaglioValidazioniVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.solmr.util.ValidationErrors"%>
<%@page import="it.csi.solmr.util.ValidationError"%>
<%@page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%!public static final String VIEW = "/view/ricercaTerrenoDettaglioValidazioniView.jsp";%>
<%
  String iridePageName = "ricercaTerrenoDettaglioValidazioniCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%
  session.removeAttribute("filtriParticellareRicercaVO");
  String ids[] = request.getParameterValues("idParticella");
  if (ids == null || ids.length > 1)
  {
    throw new SolmrException("Selezionare una e una sola particella");
  }
  long idParticella = new Long(request
      .getParameter("idParticella")).longValue();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  StoricoParticellaVO storicoParticellaVO = anagFacadeClient
      .findStoricoParticellaVOByIdParticella(idParticella);
  request.setAttribute("storicoParticellaVO", storicoParticellaVO);
  Long anno = null;
  try
  {
    if (request.getParameter("isPaginaDettaglioValidazioni")!=null) // Non sto arrivando da una pagina del tab o dalla cessazione
    {
	    String strAnno=request.getParameter("anno");
	    if (strAnno!=null)
	    {
	      // Se anno!= null l'utente ha selezionato un anno nella combo e premuto "ricerca"
	      anno = new Long(strAnno);
	    }
    }
  }
  catch (Exception e)
  {
    // L'unico errore possibile è che sia stato premuto ricerca con la combo valorizzata a "-seleziona-"
    // ==> indico di selezionare un anno
    ValidationErrors errors = new ValidationErrors();
    errors.add("anno",new ValidationError(AnagErrors.ERRORE_SELEZIONARE_ANNO));
    request.setAttribute("errors",errors);
  }
  long anni[] = anagFacadeClient 
      .getElencoAnniDichiarazioniConsistenzaByIdParticella(idParticella);
  boolean ordineAscendente[] = null;

  if (anni != null && anni.length > 0)
  {
    request.setAttribute("anni", anni);
    String ordine = request.getParameter("ordine");
    int tipoOrdine = 0;
    if (ordine != null)
    {
      if (ordine.equalsIgnoreCase("cuaa"))
      {
        tipoOrdine = SolmrConstants.ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_CUAA;
      }
      else
      {
        if (ordine.equalsIgnoreCase("validazioni"))
        {
          tipoOrdine = SolmrConstants.ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_VALIDAZIONI;
        }
      }
      boolean cuaaAsc = !Validator.isNotEmpty(request
          .getParameter("cuaaDiscendente"));
      boolean validazioniAsc = !Validator.isNotEmpty(request
          .getParameter("validazioniDiscendente"));
      ordineAscendente = new boolean[]
      { cuaaAsc, validazioniAsc };
    }
    else
    {
      ordineAscendente = new boolean[]
      { true, false };
    }
    ParticellaDettaglioValidazioniVO particellaDettaglioValidazioni[] = anagFacadeClient
        .getParticellaDettaglioValidazioni(idParticella,
            anno, tipoOrdine, ordineAscendente);
    if (particellaDettaglioValidazioni != null)
    {
      request.setAttribute("particellaDettaglioValidazioni",
          particellaDettaglioValidazioni);
    }
  }
  else
  {
    ordineAscendente = new boolean[]
    { true, false };
    // Non ha senso richiamare il metodo di dettaglio se non ci sono dichiarazioni per la 
    // particella indicata, in tal caso non ci sarebbero dati ==> la view non trovando in
    // request l'array di dettaglio scriverà che non ci sono validazioni.
  }
  request.setAttribute("ordineAscendente", ordineAscendente);
%><jsp:forward page="<%=VIEW%>" />
