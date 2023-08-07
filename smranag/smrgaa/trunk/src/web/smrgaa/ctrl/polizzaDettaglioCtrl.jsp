<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.FiltroPolizzaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "polizzaDettaglioCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String polizzaDettaglioUrl = "/view/polizzaDettaglioView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio polizze. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String messaggioErrore = null;
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  FiltroPolizzaVO filtroPolizzaVO = (FiltroPolizzaVO)session.getAttribute("filtroPolizzaVO");
    
    
    
  WebUtils.removeUselessFilter(session, "filtroPolizzaVO");

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
    
  filtroPolizzaVO.setIdPolizzaAssicurativa(request.getParameter("idPolizzaAssicurativa"));  
  Long idPolizzaAssicurativa = Long.decode(  filtroPolizzaVO.getIdPolizzaAssicurativa());
  
  session.setAttribute("filtroPolizzaVO", filtroPolizzaVO);
  
  PolizzaVO polizzaVO = null; 
  try 
  { 
    polizzaVO = gaaFacadeClient.getDettaglioPolizza(idPolizzaAssicurativa);
      
    request.setAttribute("polizzaVO", polizzaVO);
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - polizzaDettaglioCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  if(Validator.isNotEmpty(polizzaVO))
  {
  
    if(polizzaVO.getTipoPolizza().equalsIgnoreCase(SolmrConstants.TIPO_POLIZZA_COLTURA))
    {
      TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioColturaVO = null; 
      try 
      { 
        tPolizzaDettaglioColturaVO = gaaFacadeClient.getDettaglioPolizzaColtura(idPolizzaAssicurativa);
          
        request.setAttribute("tPolizzaDettaglioColturaVO", tPolizzaDettaglioColturaVO);
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - polizzaDettaglioCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    
    
    if(polizzaVO.getTipoPolizza().equalsIgnoreCase(SolmrConstants.TIPO_POLIZZA_STRUTTURA))
    {
      TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioStrutturaVO = null; 
      try 
      { 
        tPolizzaDettaglioStrutturaVO = gaaFacadeClient.getDettaglioPolizzaStruttura(idPolizzaAssicurativa);
          
        request.setAttribute("tPolizzaDettaglioStrutturaVO", tPolizzaDettaglioStrutturaVO);
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - polizzaDettaglioCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    
    
    if(polizzaVO.getTipoPolizza().equalsIgnoreCase(SolmrConstants.TIPO_POLIZZA_ZOOTECNIA))
    {
      TreeMap<Long,Vector<PolizzaDettaglioVO>> tPolizzaDettaglioZootecniaVO = null; 
      try 
      { 
        tPolizzaDettaglioZootecniaVO = gaaFacadeClient.getDettaglioPolizzaZootecnia(idPolizzaAssicurativa);
          
        request.setAttribute("tPolizzaDettaglioZootecniaVO", tPolizzaDettaglioZootecniaVO);
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - polizzaDettaglioCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    
  } 
  
  
  
  
  
   	
 	%>
  	<jsp:forward page="<%= polizzaDettaglioUrl %>" />
 	
