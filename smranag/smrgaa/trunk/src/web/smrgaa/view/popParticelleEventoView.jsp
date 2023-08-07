<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.util.performance.*" %>
<%@ page import="java.util.*" %>
<%

	// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
 	StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
 	// START
 	watcher.start();
  

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popParticelleEvento.htm");

 	Htmpl htmpl = new Htmpl(layout);
 
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	String messaggioErrore = null;
	
 	String urlChiamante = request.getParameter("urlChiamante");
 	htmpl.set("urlChiamante", urlChiamante);
  
  //Indipendentemente dal motivo per cui sono qui devo andare a vedere
  //se ci sono righe nelle particelle associate e memorizzarle in session particelleAssociate
  Vector particelleAssociate=(Vector) session.getAttribute("particelleAssociate");
     
  if (particelleAssociate != null)
  {
    String supConduzione[]=request.getParameterValues("supConduzione");
    String idConduzione[]=request.getParameterValues("idConduzione");
    String supUtilizzata[]=request.getParameterValues("supUtilizzata");
    String idTipoUtilizzo[]=request.getParameterValues("idTipoUtilizzo");
    String idVarieta[]=request.getParameterValues("idVarieta");
    
    
    //devo memorizzare i dati inseriti/modificati dall'utente 
    int size=particelleAssociate.size();
    if (supConduzione!=null)
    {
      for (int i=0;i<size && i<supConduzione.length;i++)
      {
        ParticellaAssVO particella=(ParticellaAssVO)particelleAssociate.get(i);
        particella.setSupCondotta(supConduzione[i]);
        if (supUtilizzata!=null) particella.setSupUtilizzata(supUtilizzata[i]);
        if (idConduzione!=null) particella.setIdConduzione(idConduzione[i]);
        if (idTipoUtilizzo!=null) particella.setIdUtilizzo(idTipoUtilizzo[i]);
        if (idVarieta!=null) particella.setIdVarieta(idVarieta[i]);
      }
    }
    session.setAttribute("particelleAssociate",particelleAssociate);    
  }
  
  

 	// Recupero i parametri dal javascript
 	if(Validator.isNotEmpty(urlChiamante)) {
	 	Long idTipologiaDocumento = Long.decode(request.getParameter("idTipologiaDocumento"));
	 	htmpl.set("idTipologiaDocumento", idTipologiaDocumento.toString());
	 	Long idCategoriaDocumento = Long.decode(request.getParameter("idCategoriaDocumento"));
	 	htmpl.set("idCategoriaDocumento", idCategoriaDocumento.toString());
	 	Long idTipoDocumento = Long.decode(request.getParameter("idTipoDocumento"));
	 	htmpl.set("idTipoDocumento", idTipoDocumento.toString());
 	}
 	String dataInizioValidita = request.getParameter("dataInizioValidita");
 	if(Validator.isNotEmpty(dataInizioValidita)) {
   		htmpl.set("dataInizioValidita", dataInizioValidita);
 	}
 	String dataFineValidita = request.getParameter("dataFineValidita");
 	if(Validator.isNotEmpty(dataFineValidita)) {
   		htmpl.set("dataFineValidita", dataFineValidita);
 	}
 	String protocolla = request.getParameter("protocolla");
 	if(Validator.isNotEmpty(protocolla) && protocolla.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
   		htmpl.set("protocolla", protocolla);
 	}
 	String numeroProtocolloEsterno = request.getParameter("numeroProtocolloEsterno");
 	if(Validator.isNotEmpty(numeroProtocolloEsterno)) {
 		htmpl.set("numeroProtocolloEsterno", numeroProtocolloEsterno);
 	}
 	String note = request.getParameter("note");
 	if(Validator.isNotEmpty(note)) {
 		htmpl.set("note", note);
 	}
 	String siglaProvinciaParticella = request.getParameter("siglaProvinciaParticella");
 	if(Validator.isNotEmpty(siglaProvinciaParticella)) {
 		htmpl.set("siglaProvinciaParticella", siglaProvinciaParticella);
 	}

 	String visualizzaParticelleAssociate = request.getParameter("particelleAssociate");


  Long idParticella=null;
  

 	// Effettuo la ricerca
 	ParticellaAssVO[] particelle = null;
 	if(!Validator.isNotEmpty(messaggioErrore)) 
  {
   		try 
      {
        idParticella=new Long(request.getParameter("idParticella"));
        particelle=anagFacadeClient.getParticellaForDocAzCessata(idParticella) ;
   		}
   		catch(SolmrException se) {
     		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
   		}
 	}
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) || particelle.length == 0) {
   		htmpl.newBlock("blkNoParticelle");
   		if(Validator.isNotEmpty(messaggioErrore)) {
	   		htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
   		}
   		else {
   			htmpl.set("blkNoParticelle.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
   		}
 	}
 	// Altrimenti visualizzo l'elenco delle particelle trovate
 	else 
  {
   		if(Validator.isNotEmpty(visualizzaParticelleAssociate))
     		htmpl.set("checkedParticelleAssociate", "checked=\"checked\"");
        
     	htmpl.newBlock("blkParticelle");
     	for(int i = 0; i < particelle.length; i++) 
      {
       		htmpl.newBlock("blkParticelle.blkElencoParticelle");
       		htmpl.set("blkParticelle.blkElencoParticelle.idParticelleAssociate", ""+i);
       		htmpl.set("blkParticelle.blkElencoParticelle.descComuneParticella", particelle[i].getDescComuneParticella());
       		htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", particelle[i].getSiglaProvinciaParticella());
       		if(Validator.isNotEmpty(particelle[i].getSezione())) {
         		htmpl.set("blkParticelle.blkElencoParticelle.sezione", particelle[i].getSezione().toUpperCase());
       		}
       		htmpl.set("blkParticelle.blkElencoParticelle.foglio", particelle[i].getFoglio().toString());
       		if(Validator.isNotEmpty(particelle[i].getParticella())) {
         		htmpl.set("blkParticelle.blkElencoParticelle.particella", particelle[i].getParticella().toString());
       		}
       		if(Validator.isNotEmpty(particelle[i].getSubalterno())) {
	       		htmpl.set("blkParticelle.blkElencoParticelle.subalterno", particelle[i].getSubalterno());
       		}
       		htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(particelle[i].getSupCatastale()));
          htmpl.set("blkParticelle.blkElencoParticelle.descEvento", particelle[i].getDescrizioneEvento());
     	}
      session.setAttribute("particelleDaAssociare",particelle);
 	}

 	// Monitoraggio
 	watcher.dumpElapsed("popParticelleEventoView.jsp", "Creation of list of particellaVO", "In popDocumentoParticelleView.jsp view from the beginning to the end", "");
 	// STOP
 	watcher.stop();

%>
<%= htmpl.text()%>
