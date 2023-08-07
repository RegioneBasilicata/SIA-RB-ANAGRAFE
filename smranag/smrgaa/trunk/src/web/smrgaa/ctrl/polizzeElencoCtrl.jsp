<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.PolizzaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.polizze.FiltroPolizzaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "polizzeElencoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String polizzeElencoUrl = "/view/polizzeElencoView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String excelUrl = "/servlet/ExcelElencoPolizzeServlet";
  
  
  final String errMsg = "Impossibile procedere nella sezione polizze. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  ValidationErrors errors = null;
  
  WebUtils.removeUselessFilter(session, "filtroPolizzaVO");
  FiltroPolizzaVO filtroPolizzaVO = (FiltroPolizzaVO)session.getAttribute("filtroPolizzaVO");
  if(Validator.isEmpty(filtroPolizzaVO))
  {
    filtroPolizzaVO = new FiltroPolizzaVO();
  }
    
  
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //String annoCampagna = request.getParameter("annoCampagna");
  //String intervento = request.getParameter("intervento");
  if(request.getParameter("annoCampagna") != null)
  {
    filtroPolizzaVO.setAnnoCampagna(request.getParameter("annoCampagna"));
  }
  if(request.getParameter("intervento") != null)
  {
    filtroPolizzaVO.setIntervento(request.getParameter("intervento"));
  }
  session.setAttribute("filtroPolizzaVO", filtroPolizzaVO);
  
  
  //Controllo se è stato selezionato lo scarico del file excel
  if("excel".equals(request.getParameter("operazione")))
  {
    %>
        <jsp:forward page="<%=excelUrl%>" />
    <%
    return;
  }
  
  
  

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
  
  
  
  Vector<Integer> elencoAnnoCampagna = new Vector<Integer>();
  try 
  {
    elencoAnnoCampagna = gaaFacadeClient.getElencoAnnoCampagnaByIdAzienda(
      anagAziendaVO.getIdAzienda().longValue());
      
    request.setAttribute("elencoAnnoCampagna", elencoAnnoCampagna);
  }
  catch(SolmrException se) 
  {
    errors = ErrorUtils.setValidErrNoNull(errors, "annoCampagna", AnagErrors.ERR_POL_ANNO_CAMPAGNA);
  }
  
  Vector<BaseCodeDescription> elencoIntervento = new Vector<BaseCodeDescription>();
  try 
  {
    elencoIntervento = gaaFacadeClient.getElencoInterventoByIdAzienda(
      anagAziendaVO.getIdAzienda().longValue());
      
    request.setAttribute("elencoIntervento", elencoIntervento);
  }
  catch(SolmrException se) 
  {
    errors = ErrorUtils.setValidErrNoNull(errors, "intervento", AnagErrors.ERR_POL_INTERVENTO);
  }
  
  
  
  
  Integer annoCampagnaInt = null;
  if(Validator.isNotEmpty(filtroPolizzaVO.getAnnoCampagna()))
  {
    annoCampagnaInt = new Integer(filtroPolizzaVO.getAnnoCampagna());
  } 
  
  Long idIntervento = null;
  if(Validator.isNotEmpty(filtroPolizzaVO.getIntervento()))
  {
    idIntervento = new Long(filtroPolizzaVO.getIntervento());
  }
  
  Vector<PolizzaVO> vPolizzaVO = null; 
  try 
  { 
    vPolizzaVO = gaaFacadeClient.getElencoPolizze(anagAziendaVO.getIdAzienda().longValue(),
      annoCampagnaInt, idIntervento);
      
    request.setAttribute("vPolizzaVO", vPolizzaVO);
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - polizzeElencoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  } 
  
  
  
  

	// Vado alla pagina di elenco delle pratiche
	%>
  	<jsp:forward page= "<%= polizzeElencoUrl %>" />
  	



