<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>



<%

	String iridePageName = "stampaProtocolloCtrl.jsp";
%>
	<%@include file = "/include/autorizzazione.inc" %>  
<%
  String stampaProtocolloUrl = "/servlet/StampaProtocolloJasperServlet";
  //String stampaDocumentoUrl = "/servlet/StampaDocumentoServlet";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String stampaProtocolloView = "/view/stampaProtocolloView.jsp";
  final String codiceStampa = "DOCUMENTI_STAMPA_PROTOCOLLO";  
  
  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  
  String documentiElencoUrl ="";
  String documentiDettaglioUrl ="";
  String documentoConfermaInserisciUrl ="";
  
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
	  documentiElencoUrl = "../layout/";
	  documentiDettaglioUrl = "../layout/";
	  documentoConfermaInserisciUrl = "../layout/";
  }		
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
	  documentiElencoUrl = "/layout/";
	  documentiDettaglioUrl = "/layout/";
	  documentoConfermaInserisciUrl = "/layout/";
  }		
  documentiElencoUrl += "documentiElenco.htm";
  documentiDettaglioUrl += "documentoDettaglio.htm";
  documentoConfermaInserisciUrl += "documentoConfermaInserisci.htm";
  
  final String errMsg = "Impossibile procedere nella sezione stampa protocollo" +
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  WebUtils.removeUselessFilter(session, "documentoRicercaVO,idDocumentoStampaProtocollo");  
 
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String[] vIdDocumento = request.getParameterValues("idDocumento");
  //mi discrimina se arrivo dal dettaglio oppure dall'elenco dei documenti oppure dalla confermainserimentodocumento
  String origineStampaProtocollo = request.getParameter("origineStampaProtocollo");
  
  long[] idDocumento = null;
  if(Validator.isNotEmpty(vIdDocumento))
  {
  
    idDocumento = new long[vIdDocumento.length];
    
    for(int i=0;i<vIdDocumento.length;i++)
    {
      idDocumento[i] = new Long(vIdDocumento[i]).longValue();
    }
    
    request.getSession().setAttribute("idDocumentoStampaProtocollo", idDocumento);
  }
  
  
  if ("stampaPdf".equals(request.getParameter("stampaPdf"))
   || "stampaDettaglioPdf".equals(request.getParameter("stampaDettaglioPdf")) 
   || "stampaConfermaInserisciPdf".equals(request.getParameter("stampaConfermaInserisciPdf")))
  {
    %>
      <jsp:forward page="<%= stampaProtocolloUrl %>"/>
    <%
  }
  
  
  Vector<TipoReportVO> vTipoStampaReport = null;
  Vector<TipoReportVO> vTipoReport = null;
  HashMap<String,String> hTipoReport = new HashMap<String,String>();
  TreeMap<String,RichiestaTipoReportVO[]> hSubReportElements = null;
  boolean stampaDiretta = false;
  HashMap<Long,String> hReportCompatibile = new HashMap<Long,String>();
  Vector<CodeDescription> vElencoPropietari = null;
  HashMap<Long,String> hProprietari = new HashMap<Long,String>();
  HashMap<String,String> hProprietariInTutti = new HashMap<String,String>();
  try
  {
    vElencoPropietari = gaaFacadeClient.getElencoPropietariDocumento(idDocumento);
    int numeroDoc = idDocumento.length;
    if(Validator.isNotEmpty(vElencoPropietari))
    {
	    //Controllo se il proprietario è associabile a tutti i documenti
	    for(int i=0;i<vElencoPropietari.size();i++)
	    {
	      if(numeroDoc == vElencoPropietari.get(i).getCode().intValue())
	      {
	        hProprietariInTutti.put(vElencoPropietari.get(i).getDescription(), "ok");
	      }
	    }
	  }
    
  
    vTipoStampaReport = gaaFacadeClient.getListTipiDocumentoStampaProtocollo(idDocumento);
    //Mi serve per prendere i titoli delle sezioni.
    vTipoReport = gaaFacadeClient.getElencoTipoReport(codiceStampa, null);
    for(int i=0;i<vTipoReport.size();i++)
    {
      hTipoReport.put(vTipoReport.get(i).getCodiceReport(),
        vTipoReport.get(i).getDescrizione());    
    }
    
    
    //Altri doc nn protocollo
    boolean flagAltriDoc = false;
    if(vTipoStampaReport.size() > 1)
    {
      flagAltriDoc = true;
    }
    
    RichiestaTipoReportVO subReportElements[] = null;
    for(int i=0;i<vTipoStampaReport.size();i++)
    {
      String codiceReport = vTipoStampaReport.get(i).getCodiceReport();
      subReportElements = gaaFacadeClient
        .getElencoSubReportRichiesta(codiceReport, null);
        
        
        
      if(subReportElements == null)
      {
        SolmrLogger.info(this, " - stampProtocolloCtrl.jsp - FINE PAGINA");
		    String messaggio = errMsg+""+"Per il documento "+codiceReport+" non è stato configurato nessun sureport!";
		    request.setAttribute("messaggioErrore",messaggio);
		    request.setAttribute("pageBack", actionUrl);
		    %>
		      <jsp:forward page="<%= erroreViewUrl %>" />
		    <%
		    return;
      }
        
      int numCompatibili = gaaFacadeClient.getCountDocumentoCompatibile(
        vTipoStampaReport.get(i).getIdTipoReport(), idDocumento);
         
      if(numCompatibili == idDocumento.length)
      {
        hReportCompatibile.put(new Long(vTipoStampaReport.get(i).getIdTipoReport()), "ok");
      }
      
      if("S".equalsIgnoreCase(vTipoStampaReport.get(i).getFlagSceltaPropietari())
        || "N".equalsIgnoreCase(vTipoStampaReport.get(i).getFlagSceltaPropietari()))
      {
        hProprietari.put(new Long(vTipoStampaReport.get(i).getIdTipoReport()), "ok");
      }
        
      boolean esisteSelezionabile = false;
      
      //Controllo se esiste almeno un subReport selezionabile
      //perchè se non ne esiste nessuno vado direttamente alla stampa  
      for(int j=0;j<subReportElements.length;j++)
      {
        if(subReportElements[j].isSelezionabile())
        {
          esisteSelezionabile = true;
          break;
        }
      }
      
      if(esisteSelezionabile || flagAltriDoc)
      {
        if(hSubReportElements == null)
        {
          hSubReportElements = new TreeMap<String,RichiestaTipoReportVO[]>();
        }
        
        String descReport = hTipoReport.get(codiceReport);
        
        hSubReportElements.put(descReport, subReportElements);       
      }
      else
      {
        // se nn ci sono subreport selezionabili ed esiste un unico
        //tipo di report sono probabilmente nel caso vecchio..
        //quindi procedo direttamente alla stampa 
        if(vTipoStampaReport.size() == 1)
        {
          stampaDiretta = true;
        }
      }    
      
        
    }
    
        
  }
  catch (SolmrException se) {
    SolmrLogger.info(this, " - stampProtocolloCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  //Se non trovo almeno un subReport da scegliere stampo direttamente
  //SPOSTARE dentro vie e popolare con htmlpl onload!!!!
  if(stampaDiretta)
  {
    if(Validator.isNotEmpty(origineStampaProtocollo))
    {
      if(origineStampaProtocollo.equalsIgnoreCase("elencoDocumenti"))
      {
        %>      
          <jsp:forward page= "<%= documentiElencoUrl %>" >
            <jsp:param name="stampaPdf" value="stampaPdf"/>
          </jsp:forward>
        <%
        return;
      }
      else if(origineStampaProtocollo.equalsIgnoreCase("dettaglioDocumenti"))
      {
        %>      
          <jsp:forward page= "<%= documentiDettaglioUrl %>" >
            <jsp:param name="stampaDettaglioPdf" value="stampaDettaglioPdf"/>
          </jsp:forward>
        <%
        return;
      }
      else if(origineStampaProtocollo.equalsIgnoreCase("documentoConfermaInserisci"))
      {
        %>      
          <jsp:forward page= "<%= documentoConfermaInserisciUrl %>" >
            <jsp:param name="stampaConfermaInserisciPdf" value="stampaConfermaInserisciPdf"/>
          </jsp:forward>
        <%
        return;
      }
    }
  }
  
  request.setAttribute("hSubReportElements",hSubReportElements);
  request.setAttribute("hReportCompatibile",hReportCompatibile);
  request.setAttribute("vElencoPropietari", vElencoPropietari);
  request.setAttribute("hProprietari", hProprietari);
  request.setAttribute("hProprietariInTutti", hProprietariInTutti);
  
   
  
%>
  <jsp:forward page="<%= stampaProtocolloView %>"/>
  
  
  
  
  
   
  
  
  
  
  
    
  
