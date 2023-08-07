<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  	java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaParticella.htm");
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
  	String descStatoEsteroParticella = request.getParameter("descStatoEsteroParticella");
  	String sezione = request.getParameter("sezione");
  	String foglio = request.getParameter("strFoglio");
  	// Dal momento che posso arrivare a questa pagina dalla ricerca del terreno
  	// se foglio uguale a null provo a recuperarlo con un altro nome di variabile
  	if(!Validator.isNotEmpty(foglio)) {
    	foglio = request.getParameter("foglio");
  	}
	String particella = request.getParameter("strParticella");
  	// Dal momento che posso arrivare a questa pagina dalla ricerca del terreno
  	// se particella uguale a null provo a recuperarla con un altro nome di variabile
  	if(!Validator.isNotEmpty(particella)) {
    	particella = request.getParameter("particella");
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
  	
  	if(!Validator.isNumericInteger(foglio) || (Validator.isNotEmpty(particella) && !Validator.isNumericInteger(particella)) || comuneVO == null) {
  		htmpl.set("exception", (String)AnagErrors.get(("ERR_NO_PARTICELLE")));
      	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));  		
  	}
  	else {
	  	StoricoParticellaVO[] elencoParticelle = null;
	    try {
	    	elencoParticelle = anagFacadeClient.getListStoricoParticellaVOByParameters(comuneVO.getIstatComune(), sezione, foglio, particella, null, true, SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, null);
	    }
	    catch(SolmrException se) {
	    	if(se.getMessage().equalsIgnoreCase(AnagErrors.ERRORE_FILTRI_GENERICI)) {
	    		htmpl.set("exception", se.getMessage());
	    	}
	    	else {
	    		htmpl.set("exception", (String)AnagErrors.get(("ERR_NO_PARTICELLE")));
	    	}
	      	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
	    }
	
	  	if(elencoParticelle != null && elencoParticelle.length > 0) {
	    	htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
		    for(int i = 0; i < elencoParticelle.length; i++) {
	      		StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
	      		htmpl.newBlock("blkParticelle");
	      		htmpl.set("blkParticelle.idParticella", storicoParticellaVO.getIdParticella().toString());
	      		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
	        		htmpl.set("blkParticelle.particella", storicoParticellaVO.getParticella());
	      		}
	      		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
	        		htmpl.set("blkParticelle.sezione", storicoParticellaVO.getSezione());
	        		htmpl.set("blkParticelle.descrizioneSezione"," - "+storicoParticellaVO.getSezioneVO().getDescrizione());
	      		}
	      		htmpl.set("blkParticelle.foglio", storicoParticellaVO.getFoglio());
	    	}
	  	}
	  	else {
	  		htmpl.set("exception", (String)AnagErrors.get(("ERR_NO_PARTICELLE")));
	      	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));	  		
	  	}
  	}
%>
<%= htmpl.text()%>
