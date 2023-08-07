<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%
  
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  	java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaFoglio.htm");
  	Htmpl htmpl = new Htmpl(layout);

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);

  	String siglaProvinciaParticella = request.getParameter("siglaProvinciaParticella");
  	String descComuneParticella = request.getParameter("descComuneParticella");
  	// Nel caso il valore fosse nullo vuol dire che arrivo dai documenti e quindi lo recupero nuovamente
  	if(!Validator.isNotEmpty(descComuneParticella)) {
    	descComuneParticella = request.getParameter("comune");
  	}
  	String descStatoEsteroParticella = request.getParameter("descStatoEsteroParticella");
  	String sezione = request.getParameter("sezione");
  	if(Validator.isNotEmpty(request.getParameter("sezioneFiltro"))) {
    	sezione = request.getParameter("sezioneFiltro");
  	}
  	String foglio = request.getParameter("strFoglio");

  	// Dal momento che posso arrivare a questa pagina dalla ricerca del terreno
  	// se foglio uguale a null provo a recuperarlo con un altro nome di variabile
  	if(!Validator.isNotEmpty(foglio)) {
    	foglio = request.getParameter("foglio");
  	}
  	if(Validator.isNotEmpty(request.getParameter("foglioFiltro"))) {
    	foglio = request.getParameter("foglioFiltro");
  	}

	ComuneVO comuneVO = null;

	if(descComuneParticella != null && !descComuneParticella.equals("")) {
    	htmpl.newBlock("blkComune");
    	htmpl.set("blkComune.siglaProvinciaParticella",siglaProvinciaParticella);
    	htmpl.set("blkComune.descComuneParticella",descComuneParticella);
    	try {
      		comuneVO = anagFacadeClient.getComuneByParameters(descComuneParticella, siglaProvinciaParticella, null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_N);
    	}
    	catch(SolmrException se) {}
  	}
  	else {
    	htmpl.newBlock("blkStato");
    	htmpl.set("blkStato.descStatoEsteroParticella",descStatoEsteroParticella);
    	try {
    		comuneVO = anagFacadeClient.getComuneByParameters(descComuneParticella, null, null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_S);
    	}
    	catch(SolmrException se) {}
  	}

  	FoglioVO[] elencoFogli = null;
  	try {
  		if(comuneVO != null) {
        	elencoFogli = anagFacadeClient.getFogliByParameters(comuneVO.getIstatComune(),sezione, foglio);
  		}
      	else {
        	htmpl.set("exception", (String)AnagErrors.get("ERR_NO_FOGLI"));
        	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
      	}
    }
  	catch(SolmrException se) {
    	htmpl.set("exception", (String)AnagErrors.get("ERR_NO_FOGLI"));
    	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  	}

  	if(elencoFogli != null && elencoFogli.length > 0) {
    	htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    	for(int i = 0; i < elencoFogli.length; i++) {
      		FoglioVO foglioVO = (FoglioVO)elencoFogli[i];
      		htmpl.newBlock("blkFogli");
     	 	htmpl.set("blkFogli.idFoglio",foglioVO.getIdFoglio().toString());
      		if(foglioVO.getSezione() != null) {
        		htmpl.set("blkFogli.sezione",foglioVO.getSezione());
        		htmpl.set("blkFogli.descrizioneSezione"," - "+foglioVO.getDescrizioneSezione());
      		}
      		if(foglioVO.getFoglio() != null) {
        		htmpl.set("blkFogli.foglio",foglioVO.getFoglio().toString());
      		}
    	}
  	}

%>
<%= htmpl.text()%>
