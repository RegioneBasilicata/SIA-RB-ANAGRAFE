<%@page import="it.csi.solmr.etc.SolmrConstants"%>
  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  	java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaSezione.htm");
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

  	if(descComuneParticella != null && !descComuneParticella.equals("")) {
    	htmpl.newBlock("blkComune");
    	htmpl.set("blkComune.siglaProvinciaParticella",siglaProvinciaParticella);
    	htmpl.set("blkComune.descComuneParticella",descComuneParticella);
  	}
  	else {
    	htmpl.newBlock("blkStato");
    	htmpl.set("blkStato.descStatoEsteroParticella",descStatoEsteroParticella);
  	}

  	Vector elencoSezioni = null;
  	ComuneVO comuneVO = null;

  	try {
    	if(descComuneParticella != null && !descComuneParticella.equals("")) {
      		comuneVO = anagFacadeClient.getComuneByParameters(descComuneParticella, siglaProvinciaParticella, null, SolmrConstants.FLAG_S, null);
    	}
    	else {
    		comuneVO = anagFacadeClient.getComuneByParameters(descComuneParticella, null, null, SolmrConstants.FLAG_S, null);
    	}
    	if(comuneVO != null) {
    		if(!Validator.isNotEmpty(sezione)) {
    			elencoSezioni = anagFacadeClient.getSezioniByComune(comuneVO.getIstatComune());
    		}
    		else {
    			SezioneVO sezioneVO = anagFacadeClient.getSezioneByParameters(comuneVO.getIstatComune(), sezione);
    			if(sezioneVO != null) {
					CodeDescription code = new CodeDescription();
					code.setSecondaryCode(sezioneVO.getSezione());
					code.setDescription(sezioneVO.getDescrizione());
					elencoSezioni = new Vector();
					elencoSezioni.add(code);
    			}
    			else {
    				htmpl.set("exception",(String)AnagErrors.get("ERR_NO_SEZIONI"));
    	        	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
    			}
    		}
    	}
    	else {
    		htmpl.set("exception",(String)AnagErrors.get("ERR_NO_SEZIONI"));
        	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
    	}
  	}
  	catch(SolmrException se) {
    	htmpl.set("exception",(String)AnagErrors.get("ERR_NO_SEZIONI"));
    	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  	}

  	if(elencoSezioni != null && elencoSezioni.size() > 0) {
    	htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    	Iterator sezioniIterator = elencoSezioni.iterator();
    	while(sezioniIterator.hasNext()) {
      		CodeDescription code = (CodeDescription)sezioniIterator.next();
      		htmpl.newBlock("blkSezioni");
      		htmpl.set("blkSezioni.sezione",code.getSecondaryCode());
      		if(code.getDescription() != null) {
        		htmpl.set("blkSezioni.descrizione",code.getDescription());
      		}
    	}
  	}
  	else {
  		htmpl.set("exception",(String)AnagErrors.get("ERR_NO_SEZIONI"));
    	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));  		
  	}

%>

<%= htmpl.text()%>


