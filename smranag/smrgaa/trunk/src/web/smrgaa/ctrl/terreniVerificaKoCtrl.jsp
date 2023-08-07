<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors "%>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%!
  public final static String VIEW = "../view/terreniVerificaKoView.jsp";
%>

<%

  String iridePageName = "terreniVerificaKoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String erroreViewUrl = "/view/erroreView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String excelUrl = "/servlet/ExcelControlliTerreniServlet";
  

  SolmrLogger.debug(this, " - terreniVerificaKoCtrl.jsp - INIZIO PAGINA");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  ValidationErrors errors = new ValidationErrors();
  String idControllo = request.getParameter("idControllo");
  String segnalazioneBloccante = request.getParameter("segnalazioneBloccante");
  String segnalazioneWarning = request.getParameter("segnalazioneWarning");
  String segnalazioneOk = request.getParameter("segnalazioneOk");
  
  String operazione = request.getParameter("operazione");
  String ordinamento = "";
  HashMap hFiltriVerificaTerreni = new HashMap();
  
  
  // L'utente ha selezionato l'ordinamento per comune decrescente
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("comuneDisc")) {
    ordinamento = SolmrConstants.ORDER_BY_CONTROLLI_DESC_COMUNE_DESC+","+SolmrConstants.ORDER_BY_CONTROLLI_ORDINAMENTO_ASC;
  }
  // L'utente ha selezionato l'ordinamento per comune ascendente
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("comuneAsc")) {
    ordinamento = SolmrConstants.ORDER_BY_CONTROLLI_DESC_COMUNE_ASC+","+SolmrConstants.ORDER_BY_CONTROLLI_ORDINAMENTO_ASC;
  }
  // L'utente ha selezionato l'ordinamento per titolo possesso decrescente
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idTipologiaDisc")) {
    ordinamento = SolmrConstants.ORDER_BY_CONTROLLI_ORDINAMENTO_DESC+","+SolmrConstants.ORDER_BY_CONTROLLI_DESC_COMUNE_ASC;
  }
  // L'utente ha selezionato l'ordinamento per titolo possesso ascendente
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idTipologiaAsc")) {
    ordinamento = SolmrConstants.ORDER_BY_CONTROLLI_ORDINAMENTO_ASC+","+SolmrConstants.ORDER_BY_CONTROLLI_DESC_COMUNE_ASC;
  }
  
  //Controllo se è stato selezionato lo scarico del file excel
  if("excel".equals(request.getParameter("operazione")))
  {
    %>
        <jsp:forward page="<%=excelUrl%>" />
    <%
    return;
  }
  
  // Recupero i valori relativi al tipo controllo
  TipoControlloVO[] elencoTipiControllo = null;
  try {
    String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
    elencoTipiControllo = anagFacadeClient.getListTipoControlloByIdGruppoControlloAttivi(SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, orderBy);
    request.setAttribute("elencoTipiControllo", elencoTipiControllo);
  }
  catch(SolmrException se) {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idControllo", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(VIEW).forward(request, response);
      return;
  }

  try
  {
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    Long idControlloL = null;
    //utilizzata nell'excel
    String filtroTipologia = null;
    
    if(Validator.isNotEmpty(idControllo))
    {
      idControlloL = new Long(idControllo);
      
      if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
      {
        for(int i = 0; i < elencoTipiControllo.length; i++) 
        {
          TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
          if(idControllo.equalsIgnoreCase(tipoControlloVO.getIdControllo().toString()))
          {
            filtroTipologia = tipoControlloVO.getDescrizione();
          }
        }
      }
    }
    else
    {
      filtroTipologia = "Tutte";
    }
    
    hFiltriVerificaTerreni.put("tipologia",filtroTipologia); 
    
    Vector vTipoErrori = null;
    boolean flagOK = false;
    String filtroSegnalazione = "";
    if(Validator.isEmpty(segnalazioneBloccante) 
      && Validator.isEmpty(segnalazioneWarning) 
      && Validator.isEmpty(segnalazioneOk))
    {
      vTipoErrori = new Vector();
      vTipoErrori.add("S");
      vTipoErrori.add("N");
      flagOK = true;
      filtroSegnalazione = "bloccante, warning, controlli positivi";
    }
    else
    {
      if(Validator.isNotEmpty(segnalazioneBloccante))
      {
        if(vTipoErrori == null)
        {
          vTipoErrori = new Vector();
        }
        vTipoErrori.add("S");
        
        filtroSegnalazione += "bloccante";
      }
      
      if(Validator.isNotEmpty(segnalazioneWarning))
      {
        if(vTipoErrori == null)
        {
          vTipoErrori = new Vector();
        }
        vTipoErrori.add("N");
        
        if(Validator.isNotEmpty(filtroSegnalazione))
        {
          filtroSegnalazione += ", warning";
        }
        else
        {
          filtroSegnalazione += "warning";
        }
      }
      
      if(Validator.isNotEmpty(segnalazioneOk))
      {
        flagOK = true;
        
        if(Validator.isNotEmpty(filtroSegnalazione))
        {
          filtroSegnalazione += ", controlli positivi";
        }
        else
        {
          filtroSegnalazione += "controlli positivi";
        }
      }
    }
    
    hFiltriVerificaTerreni.put("segnalazioni",filtroSegnalazione);
    

    //Recupero le eventuali anomalie
    Vector anomalie = anagFacadeClient.getErroriAnomalieForControlliTerreni(
      anagAziendaVO.getIdAzienda(), idControlloL, vTipoErrori, flagOK, ordinamento);
    session.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
    session.setAttribute("hFiltriVerificaTerreni", hFiltriVerificaTerreni);

    %><jsp:forward page="<%= VIEW %>"/><%
  }
  catch(Exception se)
  {
    SolmrLogger.info(this, " - terreniVerificaKoCtrl.jsp - FINE PAGINA");
    String messaggio = se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  SolmrLogger.debug(this, " - terreniVerificaKoCtrl.jsp - FINE PAGINA");
%>