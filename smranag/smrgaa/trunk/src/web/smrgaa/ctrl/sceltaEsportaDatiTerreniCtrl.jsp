<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "sceltaEsportaDatiTerreniCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - sceltaEsportaDatiTerreniCtrl.jsp - INIZIO PAGINA");
  
  String sceltaEsportaDatiTerreniURL = "/view/sceltaEsportaDatiTerreniView.jsp";
  String excelUrlBrogliaccio = "/servlet/ExcelBrogliaccioServlet";
  String excelUrlStabilizzazioneGis = "/servlet/ExcelStabilizzazioneGisServlet";
  //String excelUrlAvvicendamento = "/servlet/ExcelAvvicendamentoServlet";
  String actionUrl = "../layout/terreniParticellareElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione esporta dati terreni. " +
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String messaggioErrore = null;
  
  HashMap<Long,ConsistenzaVO> elencoPianiRiferimento = new HashMap<Long,ConsistenzaVO>();
  try 
  {
    String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
    ConsistenzaVO[] elencoDateConsistenza = anagFacadeClient.getListDichiarazioniConsistenzaByIdAzienda(anagAziendaVO.getIdAzienda(), orderBy);
    // Inserisco a mano i due riferimenti non reperibili da DB richiesti dal dominio
    ConsistenzaVO consistenzaVO = new ConsistenzaVO();
    consistenzaVO.setIdDichiarazioneConsistenza("0");
    ConsistenzaVO consistenzaStorVO = new ConsistenzaVO();
    consistenzaStorVO.setIdDichiarazioneConsistenza("-1");
    elencoPianiRiferimento.put(new Long(-1), consistenzaStorVO);
    elencoPianiRiferimento.put(new Long(0), consistenzaVO);     
    if(elencoDateConsistenza != null && elencoDateConsistenza.length > 0) 
    {
      for(int i = 0; i < elencoDateConsistenza.length; i++) 
      {
        ConsistenzaVO consistenzaElencoVO = (ConsistenzaVO)elencoDateConsistenza[i];
        elencoPianiRiferimento.put(Long.decode(consistenzaElencoVO.getIdDichiarazioneConsistenza()), consistenzaElencoVO);
      }
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - sceltaEsportaDatiTerreniCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
    
  String descrizionePiano = "";
  if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == 0)
  {
    descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
  }
  else if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
  {
    descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
  }
  else
  {
    ConsistenzaVO consVO = elencoPianiRiferimento.get(filtriParticellareRicercaVO.getIdPianoRiferimento());
    descrizionePiano = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consVO.getData());
  }
  request.setAttribute("descrizionePiano", descrizionePiano);
  
  
  Vector<TipoEsportazioneDatiVO> vTipoEsportazione = null;
  try 
  {
    vTipoEsportazione = gaaFacadeClient.getTipoEsportazioneDati(SolmrConstants.ESPORTAZIONE_DATI_TERRENI, 
      ruoloUtenza.getCodiceRuolo());
    request.setAttribute("vTipoEsportazione", vTipoEsportazione);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - sceltaEsportaDatiTerreniCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_ESPORTAZIONE_DATI+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  //sono già nella pagine e richiamo uno scarico
  if(request.getParameter("tipoEsportaDatiTerreni") != null)
  {        
    String esportaDatiTerreni = request.getParameter("tipoEsportaDatiTerreni");
    //Scarico brogliaccio
    if(Validator.isNotEmpty(esportaDatiTerreni)
      && SolmrConstants.ESP_DATI_TERRENI_BROGLIACCIO.equalsIgnoreCase(esportaDatiTerreni))
    {
      %>
        <jsp:forward page="<%=excelUrlBrogliaccio %>" />
      <%
      return;
    }
    //Scarico stabilizzazione gis
    else if(Validator.isNotEmpty(esportaDatiTerreni)
      && SolmrConstants.ESP_DATI_TERRENI_STAB_GIS.equalsIgnoreCase(esportaDatiTerreni))
    {
    
      %>
        <jsp:forward page="<%=excelUrlStabilizzazioneGis %>" />
      <%
      return;      
      
    }
    //Avvicendamento
    /*else if(Validator.isNotEmpty(esportaDatiTerreni)
      && SolmrConstants.ESP_DATI_TERRENI_AVVICENDAMENTO.equalsIgnoreCase(esportaDatiTerreni))
    {
    
      anagFacadeClient.aggiornaPraticaAziendaPLQSL(anagAziendaVO.getIdAzienda());
      %>
        <jsp:forward page="<%=excelUrlAvvicendamento %>" />
      <%
      return;      
      
    }*/
      
  }     
  
%>
   <jsp:forward page="<%=sceltaEsportaDatiTerreniURL %>" />