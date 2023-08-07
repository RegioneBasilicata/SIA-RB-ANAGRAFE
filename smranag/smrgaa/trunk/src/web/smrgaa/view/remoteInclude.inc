
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>

<jsp:useBean id="cp" class="it.csi.solmr.util.PageCache" scope="application"/>

<%

String head= "";
String header= "";
String footer  = "";
String headMenuScroll = "";
String headMenuScrollIE6 = "";
boolean nullTestalino = false;
it.csi.jsf.htmpl.Htmpl htmplTemp = null;
String pathErrori = null;

String pathToFollow = (String)session.getAttribute("pathToFollow");
SolmrLogger.debug(this, "--- pathToFollow ="+pathToFollow);

String activeSite = null;

// Effettuo tutte le operazioni di recupero immagini solo se la sessione è attiva
if(it.csi.solmr.util.Validator.isNotEmpty(pathToFollow)) {
  // Parametro che arriva dalla pagina grafica di RUPAR o SISTEMA PIEMONTE
  activeSite =  (String)session.getAttribute("activeSite");
  SolmrLogger.debug(this, "--- activeSite ="+activeSite);

  /*SolmrLogger.debug(this, "\n\n\n\n\n\n#################################");
  SolmrLogger.debug(this, "pathToFollow: "+pathToFollow);
  SolmrLogger.debug(this, "activeSite: "+activeSite);
  SolmrLogger.debug(this, "#################################\n\n\n\n\n\n");*/

  if(!it.csi.solmr.util.Validator.isNotEmpty(activeSite))
  {

    if(pathToFollow.equalsIgnoreCase("rupar"))        
    {
      activeSite = application.getInitParameter("immaginiSviluppoRupar");
      head = application.getInitParameter("headRupar");
      header = application.getInitParameter("headerRupar");
      footer = application.getInitParameter("footerRupar");
      pathErrori = application.getInitParameter("erroriRupar");
      headMenuScroll = application.getInitParameter("headMenuScrollRupar");
      headMenuScrollIE6 = application.getInitParameter("headMenuScrollRuparIE6");
    }
    else if(pathToFollow.equalsIgnoreCase("sispie"))
    {
      activeSite = application.getInitParameter("immaginiSviluppoSispie");
      head = application.getInitParameter("headSispie");
      header = application.getInitParameter("headerSispie");
      footer = application.getInitParameter("footerSispie");
      pathErrori = application.getInitParameter("erroriSispie");
    	headMenuScroll = application.getInitParameter("headMenuScrollSispie");
      headMenuScrollIE6 = application.getInitParameter("headMenuScrollSispieIE6");
    }
    else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
    	activeSite = application.getInitParameter("immaginiSviluppoTOBECONFIG");
        head = application.getInitParameter("headTOBECONFIG");
        header = application.getInitParameter("headerTOBECONFIG");
        footer = application.getInitParameter("footerTOBECONFIG");
        pathErrori = application.getInitParameter("erroriTOBECONFIG");
      	headMenuScroll = application.getInitParameter("headMenuScrollTOBECONFIG");
        headMenuScrollIE6 = application.getInitParameter("headMenuScrollTOBECONFIGIE6");
    }

  }

  String urlHead = activeSite + head;
  head = cp.requestRemotePage(urlHead, application);
  String urlHeader = activeSite + header;
  header = cp.requestRemotePage(urlHeader, application);
  String urlFooter = activeSite + footer;
  footer = cp.requestRemotePage(urlFooter, application);
  headMenuScroll = activeSite + headMenuScroll;
  headMenuScrollIE6 = activeSite + headMenuScrollIE6;
  
  SolmrLogger.debug(this, "--- urlHead ="+urlHead);
  //SolmrLogger.debug(this, "--- head ="+head);
  SolmrLogger.debug(this, "--- urlHeader ="+urlHeader);
  //SolmrLogger.debug(this, "--- header ="+header);
  SolmrLogger.debug(this, "--- urlFooter ="+urlFooter);
  //SolmrLogger.debug(this, "--- footer ="+footer);
  //SolmrLogger.debug(this, "--- headMenuScroll ="+headMenuScroll);
  //SolmrLogger.debug(this, "--- headMenuScrollIE6 ="+headMenuScrollIE6);
  

  // permette la propagazione dell'oggetto cp tra i server del cluster
  application.setAttribute("cp", cp);

  java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(footer.getBytes());
  htmplTemp = new it.csi.jsf.htmpl.Htmpl(bais);
  footer = htmplTemp.text();
}

java.util.HashMap iride2mappings=(java.util.HashMap)application.getAttribute("iride2mappings");
String iridePageNameForCU = (String)request.getAttribute("iridePageNameForCU");
it.csi.solmr.util.SolmrLogger.debug(this,"[remote.inc] iridePageNameForCU: " +iridePageNameForCU);
if(iridePageNameForCU != null && !iridePageNameForCU.equals(""))
{
   it.csi.solmr.presentation.security.Autorizzazione autorizzazione = (it.csi.solmr.presentation.security.Autorizzazione) iride2mappings.get(iridePageNameForCU);
   request.setAttribute("iridePageNameForCU",iridePageNameForCU);
   if(autorizzazione != null)
   {
      autorizzazione.writeMenu(htmpl, request);
      autorizzazione.writeException(exception, htmpl);
   }
}

%>
