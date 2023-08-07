<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>




<%

	  // Pulisco la sessione dai filtri di altre sezioni
  	String noRemove = "";
  	WebUtils.removeUselessFilter(session, noRemove);
  	
  	//rimuovo i valori di un eventuale stampa creata dalla validazione!!!
  	

  	String iridePageName = "stampeCtrl.jsp";
  	String stampaFascicoloUrl = "/servlet/StampaFascicoloServlet";
    String stampaComunicazione10RUrl = "/servlet/StampaComunicazione10RServlet";
    String stampaAllegatiUrl = "/servlet/StampaAllegatiServlet";
    String stampaVariazioneCatastaleUrl = "/servlet/StampaVariazioneCatastaleServlet";
    String stampaFascicoloNewUrl = "/servlet/StampaFascicoloModolServlet";
  	
%>
	<%@include file = "/include/autorizzazione.inc" %>
  
<%
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String stampaUrl = "/view/stampeView.jsp";
  String visualizzaFileStampaUrl = "/ctrl/visualizzaFileStampaCtrl.jsp";
  final String codiceStampa = "STAMPE";
  
  final String errMsg = "Impossibile procedere nella sezione scelta stampa" +
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 
  String tipoStampa = request.getParameter("tipoStampa");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  Long idPianoRiferimento = null;
  // Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina e quindi imposto il piano di riferimento alla data odierna
  if(!Validator.isNotEmpty(idDichiarazioneConsistenza)) 
  {
    idPianoRiferimento = new Long("-1");
  }
  else 
  {
    idPianoRiferimento = Long.decode(idDichiarazioneConsistenza);
  }
  
  
  
  try
  {
    Vector<TipoReportVO> vTipoReport = null;
    if(idPianoRiferimento.compareTo(new Long(0)) > 0)
    {
      vTipoReport = gaaFacadeClient.getElencoTipoReportByValidazione(codiceStampa, idPianoRiferimento);
    }
    else
    {
      vTipoReport = gaaFacadeClient.getElencoTipoReport(codiceStampa, null);
    }
    request.setAttribute("vTipoReport", vTipoReport);
  }
  catch (SolmrException se) {
    SolmrLogger.info(this, " - stampeCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  //Richiesta la stampa PDF
  if ("stampaPdf".equals(request.getParameter("stampaPdf")))
  {
  
    if(idPianoRiferimento.compareTo(new Long(0)) > 0)
    {
      AllegatoDichiarazioneVO allegatoDichiarazioneVO = gaaFacadeClient
         .getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(idPianoRiferimento, tipoStampa);
      if(Validator.isNotEmpty(allegatoDichiarazioneVO)
         && Validator.isNotEmpty(allegatoDichiarazioneVO.getIdAllegato()))
      {
        %>
          <jsp:forward page="<%= visualizzaFileStampaUrl %>">
            <jsp:param name="idAllegato" value="<%= allegatoDichiarazioneVO.getIdAllegato() %>" /> 
          </jsp:forward>
        <%
      }
    }
    
  
    if("FASCICOLO".equalsIgnoreCase(tipoStampa))
    {
      %>
        <jsp:forward page="<%= stampaFascicoloUrl %>"/>
      <%
    }
    else if("COMUNICAZIONE10/R".equalsIgnoreCase(tipoStampa))
    {
      %>
        <jsp:forward page="<%= stampaComunicazione10RUrl %>"/>
      <%
    }
    else if("ALLEGATI".equalsIgnoreCase(tipoStampa))
    {
      %>
        <jsp:forward page="<%= stampaAllegatiUrl %>"/>
      <%
    }
    else if("VARIAZIONI_CATASTALI".equalsIgnoreCase(tipoStampa))
    {
      %>
        <jsp:forward page="<%= stampaVariazioneCatastaleUrl %>"/>
      <%
    }
    else
    {
      %>
        <jsp:forward page="<%= stampaFascicoloNewUrl %>">
          <jsp:param name="codiceStampaGenerico" value="<%= tipoStampa %>" /> 
        </jsp:forward>
      <%
    }
    
    return;
  }
  else
  {
    try
    {      
      
      Date dataRiferimento = null;
      if(idPianoRiferimento.longValue() > 0 )
      {
        ConsistenzaVO consistenzaVO = null;
        try 
        {
          consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idPianoRiferimento);
        }
        catch(SolmrException se) 
        {}
        dataRiferimento = consistenzaVO.getDataDichiarazione();
      }
    
      RichiestaTipoReportVO subReportElements[] = gaaFacadeClient
        .getElencoSubReportRichiesta(tipoStampa, dataRiferimento);
        
      if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
      {
        String parametroSpal = null;
        try 
        {
          parametroSpal = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_SPAL);;
        }
        catch(SolmrException se) 
        {
        
          SolmrLogger.info(this, " - stampaCtrl.jsp - FINE PAGINA");
          String messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_SPAL;
          String messaggio = messaggioErrore +": "+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        HashMap<String,String> hMapQuadriPA = new HashMap<String,String>();  
        StringTokenizer strTokenQuadriPA = new StringTokenizer(parametroSpal,",");
        while(strTokenQuadriPA.hasMoreTokens())
        {
          String strTemp = strTokenQuadriPA.nextToken();
          hMapQuadriPA.put(strTemp,strTemp);
        }
        
        //blocco i quadri per pa locale indicati nella variabile SPAL
        for(int i=0;i<subReportElements.length;i++)
        {
          if(hMapQuadriPA.get(subReportElements[i].getIdReportSubReport().toString()) != null)
          {
            subReportElements[i].setSelezionabile(false);
          }
        }
      }
        
      
      request.setAttribute("subReportElements", subReportElements);
  
    }
    catch (SolmrException se) {
      SolmrLogger.info(this, " - stampaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    %>
      <jsp:forward page="<%=stampaUrl%>" />
    <%
  
  }

%>
  
  
