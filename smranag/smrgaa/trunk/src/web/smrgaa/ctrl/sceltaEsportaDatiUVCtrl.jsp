<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
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

  String iridePageName = "sceltaEsportaDatiUVCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - sceltaEsportaDatiUVCtrl.jsp - INIZIO PAGINA");
  
  String sceltaEsportaDatiUVURL = "/view/sceltaEsportaDatiUVView.jsp";
  String excelUrlSemplice = "/servlet/ExcelUnitaVitateElencoSempliceServlet";
  String excelUrlParcelle = "/servlet/ExcelUnitaVitateElencoIsoleParcelleServlet";
  String excelUrlCompensazione = "/servlet/ExcelUnitaVitateElencoCompensazioneServlet";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";  
  
  final String errMsg = "Impossibile procedere nella sezione esporta dati unità arboree. " +
    "Contattare l'assistenza comunicando il seguente messaggio: ";    
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String messaggioErrore = null;
  
  HashMap<Long,ConsistenzaVO> elencoPianiRiferimento = new HashMap<Long,ConsistenzaVO>();
  try 
  {
    String[] orderBy = new String[]{SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
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
    SolmrLogger.info(this, " - sceltaEsportaDatiUVCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  String descrizionePiano = "";
  if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
  {
    if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() == 0)
    {
      descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
    }
    else if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() == -1)
    {
      descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
    }
    else
    {
      ConsistenzaVO consVO = elencoPianiRiferimento.get(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento());
      descrizionePiano = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consVO.getData());
    }
  }
  
  request.setAttribute("descrizionePiano", descrizionePiano);
  
  
  //sono già nella pagine e richiamo uno scarico
  if(request.getParameter("regimeEsportaDatiUV") != null)
  {   
    
    Vector<StoricoParticellaArboreaExcelVO> elencoUnitaArboree = null;
    
    //Scarico semplice
    if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiUV"))
      && request.getParameter("tipoEsportaDatiUV").equalsIgnoreCase("1"))
    {
      %>
        <jsp:forward page="<%=excelUrlSemplice %>" />
      <%
      return;
    }
    //Compensazione aziendale
    else if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiUV"))
      && request.getParameter("tipoEsportaDatiUV").equalsIgnoreCase("2"))
    {
	    
	    if(session.getAttribute("flagFinePlsql") != null)
	    {
	      
	      session.removeAttribute("flagFinePlsql");    
        %>
          <jsp:forward page="<%=excelUrlCompensazione %>" />
        <%
        return;
      }
      else
      {
        PlSqlCodeDescription plCode = null;
     
	      try
	      {
	        plCode = gaaFacadeClient.compensazioneAziendalePlSql(anagAziendaVO.getIdAzienda().longValue(), 
	          ruoloUtenza.getIdUtente().longValue()); 
	      }
	      catch (SolmrException se) 
	      {
	        SolmrLogger.info(this, " - sceltaEsportaDatiUVCtrl.jsp - FINE PAGINA");
	        String messaggio = errMsg+""+se.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      }
	      
	      
	      if(plCode !=null)
	      {
	        if(Validator.isNotEmpty(plCode.getDescription()))
	        {
	          messaggioErrore = "Scarico compensazione aziendale: "+plCode.getOtherdescription(); 
	          request.setAttribute("messaggioErrore", messaggioErrore);        
	          %>
	            <jsp:forward page= "<%= sceltaEsportaDatiUVURL %>" />
	          <%        
	        }
	        else if(Validator.isNotEmpty(plCode.getItem()) && ((String)plCode.getItem()).equalsIgnoreCase("3"))
	        {
	          messaggioErrore = "Scarico compensazione aziendale: Non sono state calcolate le Isole/parcelle Gis, impossibile proseguire.";
	          request.setAttribute("messaggioErrore", messaggioErrore);
	          
	          %>
	            <jsp:forward page= "<%= sceltaEsportaDatiUVURL %>" />
	          <%        
	        }
	      }
      
      
        session.setAttribute("flagFinePlsql","true");
        %>
          <jsp:forward page="<%=sceltaEsportaDatiUVURL %>" />
        <%
        return;
      }
      //Eliminato return per sbloccare il tasto nella pagina
    }
    //Scarico isole parcelle (classico)
    else
    {
    
      %>
        <jsp:forward page="<%=excelUrlParcelle %>" />
      <%
      return;      
      
    }
      
  }
  else
  { //prima volta che entro
    session.removeAttribute("flagFinePlsql");
  }
%>
   <jsp:forward page="<%=sceltaEsportaDatiUVURL %>" />