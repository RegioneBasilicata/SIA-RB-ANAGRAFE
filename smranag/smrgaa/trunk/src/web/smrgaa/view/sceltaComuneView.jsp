  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaComune.htm");
  	Htmpl htmpl = new Htmpl(layout);

	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

	String provincia = request.getParameter("provincia");
	// Dal momento che questo pop-up viene chiamato da diversi punti
 	// cerco di recuperare la provincia in tutti i modi in cui può essere
 	// stata valorizzata
 	String comune = request.getParameter("comune");
 	// Dal momento che questo pop-up viene chiamato da diversi punti
 	// cerco di recuperare il comune in tutti i modi in cui può essere
 	// stato valorizzato
 	String obiettivo = request.getParameter("obiettivo");
 	String provenienza = request.getParameter("provenienza");
 	obiettivo = obiettivo == null ? "" : obiettivo;
 	Vector elencoComuni = null;
	SolmrLogger.debug(this, "Value of parameter [PROVINCIA] in sceltaComuneView.jsp: "+provincia+"\n");
	SolmrLogger.debug(this, "Value of parameter [COMUNE] in sceltaComuneView.jsp: "+comune+"\n");
	SolmrLogger.debug(this, "Value of parameter [FLAG_N] in sceltaComuneView.jsp: "+SolmrConstants.FLAG_N+"\n");
 	try 
 	{
   	if(provenienza == null || provenienza.equals("")) 
   	{
    	elencoComuni = anagFacadeClient.getComuniLikeProvAndCom(provincia,comune);
   	}
   	else 
   	{
    	if(provenienza.equalsIgnoreCase("documentale")) 
    	{
      	elencoComuni = anagFacadeClient.getComuniNonEstintiLikeProvAndCom(provincia,comune, null);
     	}
     	else if(provenienza.equalsIgnoreCase("territoriale")) 
     	{
      	String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
     		elencoComuni = anagFacadeClient.getComuniByParameters(comune, provincia, null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_N, orderBy);
     	}
     	//Arrivo da ricercaTerreni
     	else if(provenienza.equalsIgnoreCase("funzionalitaTerreni")) 
      {
        String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
        elencoComuni = anagFacadeClient.getComuniByParameters(comune, provincia, null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_N, orderBy);
      }  
     	else 
     	{
      	elencoComuni = anagFacadeClient.getComuniNonEstintiLikeProvAndCom(provincia,comune, SolmrConstants.FLAG_N);
     	}
   	}
 	}
 	catch(SolmrException se) 
 	{
   	htmpl.set("exception",AnagErrors.RICERCACOMUNI);
   	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
 	}
 	if(elencoComuni != null && elencoComuni.size() > 0) 
 	{
   	htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
   	Iterator comuniIterator = elencoComuni.iterator();
   	while(comuniIterator.hasNext()) 
   	{
   		ComuneVO comuneVO = (ComuneVO)comuniIterator.next();
   		htmpl.newBlock("elencoComuni");
   		htmpl.set("elencoComuni.provincia",comuneVO.getDescrProv());
   		htmpl.set("elencoComuni.comune",comuneVO.getDescom());
   		htmpl.set("elencoComuni.istat",comuneVO.getIstatComune());
   		htmpl.set("elencoComuni.istatComune",comuneVO.getIstatComune());
   		htmpl.set("elencoComuni.istatProvincia",comuneVO.getIstatProvincia());
   		htmpl.set("elencoComuni.cap",comuneVO.getCap());
   		htmpl.set("elencoComuni.siglaProvincia",comuneVO.getSiglaProv());
   		htmpl.set("elencoComuni.zonaAltimetrica",String.valueOf(comuneVO.getZonaAlt()));
   		htmpl.set("elencoComuni.codiceFiscaleComune",comuneVO.getCodfisc());
   	}
 	}
 	else 
 	{
 		htmpl.set("exception",AnagErrors.RICERCACOMUNI);
   	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
 	}
 	htmpl.set("obiettivo", obiettivo);


%>
<%= htmpl.text()%>
