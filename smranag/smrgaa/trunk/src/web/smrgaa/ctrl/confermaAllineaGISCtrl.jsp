<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	String iridePageName = "confermaAllineaGISCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String terreniParticellareElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
  String confermaAllineaGISUrl = "/view/confermaAllineaGISView.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String messaggioErrore = null;
  
  final String errMsg = "Impossibile procedere nella sezione allinea a eleggibiltà. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO"); 
	
	// L'utente ha cliccato il pulsante SI
	if(Validator.isNotEmpty(request.getParameter("conferma"))) 
  {
		PlSqlCodeDescription plCode = null;
     
    try
    {
      plCode = gaaFacadeClient.allineaSupEleggibilePlSql(anagAziendaVO.getIdAzienda().longValue(), 
        ruoloUtenza.getIdUtente().longValue()); 
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - confermaAllineaGISCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    if((plCode !=null) && (Validator.isNotEmpty(plCode.getDescription())))
    {
      if(plCode.getDescription().equalsIgnoreCase("1"))
      {
        messaggioErrore = plCode.getOtherdescription(); 
        request.setAttribute("messaggioErrore", messaggioErrore);        
        %>
          <jsp:forward page= "<%= confermaAllineaGISUrl %>" />
        <%        
      }
      else if(plCode.getDescription().equalsIgnoreCase("2"))
      {
        messaggioErrore = "Alcune particelle non sono state aggiornate " +
             "in quanto prive delle informazioni di eleggibilità per l'uso del suolo specificato";
        request.setAttribute("messaggioErrore", messaggioErrore);
        
        %>
          <jsp:forward page= "<%= confermaAllineaGISUrl %>" />
        <%        
      }
    }
    
	  // Vado alla pagina di inserimento particella(primo step)
  	%>
    	<jsp:forward page= "<%= terreniParticellareElencoCtrlUrl %>" />
  	<%
	}
	// Altrimenti ha selezionato il tasto NO
	else if(Validator.isNotEmpty(request.getParameter("annulla"))) 
  {
		
		// Torno alla pagina di elenco/ricerca delle particelle
		%>
    	<jsp:forward page= "<%= terreniParticellareElencoCtrlUrl %>" />
  	<%
	}
  
  
%>

<jsp:forward page="<%=confermaAllineaGISUrl%>"/>
