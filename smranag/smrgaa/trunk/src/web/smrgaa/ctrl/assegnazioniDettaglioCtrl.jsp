<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.exception.SolmrException"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smruma.umaserv.dto.AssegnazioneVO" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.WebUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "assegnazioniDettaglioCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - assegnazioniDettaglioCtrl.jsp - INIZIO PAGINA");
  String assegnazioniDettaglioUrl = "/view/assegnazioniDettaglioView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  WebUtils.removeUselessFilter(session, null);
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String idDomandaAssegnazione = request.getParameter("idDomandaAssegnazione"); 
  
  
  
  final String errMsg = "Il servizio dei buoni carburante è momentaneamente non disponibile. ";
    
  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) {
    request.setAttribute("statoAzienda", se);
  }
  
  Vector<Long> vIdDomandAssegnazione = new Vector<Long>();
  if(Validator.isNotEmpty(idDomandaAssegnazione))
  {
    StringTokenizer st = new StringTokenizer(idDomandaAssegnazione, ",");
    while(st.hasMoreTokens()) 
    {
      vIdDomandAssegnazione.add(new Long(st.nextToken()));
    }  
  }
  
  AssegnazioneVO[] arrAssegnazioneDettaglio = null;
  if(vIdDomandAssegnazione.size() > 0)
  {
	  try
	  {
	    long[] arrIdDomandaAssegnazione = new long[vIdDomandAssegnazione.size()];
	    for(int i=0;i<vIdDomandAssegnazione.size();i++)
	    {
	      arrIdDomandaAssegnazione[i] = vIdDomandAssegnazione.get(i).longValue();
	    }
	    arrAssegnazioneDettaglio = gaaFacadeClient.umaservGetDettAssegnazioneByRangeIdDomAss(
	      arrIdDomandaAssegnazione);    
	  }
	  catch (SolmrException se) 
	  {
	    SolmrLogger.info(this, " - assegnazioneDettaglioCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+""+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
	}
  request.setAttribute("arrAssegnazioneDettaglio",arrAssegnazioneDettaglio);
  
  

  
  
  
  


  SolmrLogger.debug(this, " - assegnazioniDettaglioCtrl.jsp - FINE PAGINA");

 %>
 
 
 
 <jsp:forward page= "<%= assegnazioniDettaglioUrl %>" />
