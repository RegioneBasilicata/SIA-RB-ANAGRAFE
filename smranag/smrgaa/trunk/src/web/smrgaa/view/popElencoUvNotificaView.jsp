<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%


 	java.io.InputStream layout = application.getResourceAsStream("/layout/popElencoUvNotifica.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
  String idDichiarazioneConsistenza = (String)request.getParameter("idDichiarazioneConsistenza");

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	String messaggioErrore = null;
	
 	String urlChiamante = request.getParameter("urlChiamante");
 	htmpl.set("urlChiamante", urlChiamante);
 	String idTipoEntita = request.getParameter("idTipoEntita");
  htmpl.set("idTipoEntita", idTipoEntita);
  String descrizione = request.getParameter("descrizione");
  htmpl.set("descrizione", descrizione);
 	
  
  
  
  if(Validator.isNotEmpty(idDichiarazioneConsistenza))
    htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  
 	
 	// Recupero i parametri dal javascript
 	if(Validator.isNotEmpty(urlChiamante)
 	  && (urlChiamante.indexOf("nuovaNotifica") != -1)) 
 	{
	 	htmpl.set("idTipologiaNotifica", request.getParameter("idTipologiaNotifica"));
	 	htmpl.set("idCategoriaNotifica", request.getParameter("idCategoriaNotifica"));
 	}
 	
 	String istatComuniConduzioniParticelle = request.getParameter("istatComuniConduzioniParticelle");
 	String istatProvinciaConduzioniParticelle = request.getParameter("istatProvinciaConduzioniParticelle");
 	String sezione = request.getParameter("sezione");
 	String foglio = request.getParameter("foglio");
 	String particella = request.getParameter("particella");
 	String subalterno = request.getParameter("subalterno");
 	String idTipoUtilizzoElenco = request.getParameter("idTipoUtilizzoElenco");
 	String idVarieta = request.getParameter("idVarieta");
 	String idTipologiaVino = request.getParameter("idTipologiaVino");

 	// Costruisco il VO filtro per la ricerca
 	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
 	filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(new Long(idDichiarazioneConsistenza));
 	filtriUnitaArboreaRicercaVO.setIstatComune(istatComuniConduzioniParticelle);
 	filtriUnitaArboreaRicercaVO.setIstatProvincia(istatProvinciaConduzioniParticelle);
 	filtriUnitaArboreaRicercaVO.setSezione(sezione);
 	filtriUnitaArboreaRicercaVO.setFoglio(foglio);
 	filtriUnitaArboreaRicercaVO.setParticella(particella);
 	filtriUnitaArboreaRicercaVO.setSubalterno(subalterno);
 	if(Validator.isNotEmpty(idTipoUtilizzoElenco))
 	  filtriUnitaArboreaRicercaVO.setIdUtilizzo(new Long(idTipoUtilizzoElenco));
 	if(Validator.isNotEmpty(idVarieta))
    filtriUnitaArboreaRicercaVO.setIdVarieta(new Long(idVarieta));
  if(Validator.isNotEmpty(idTipologiaVino))
    filtriUnitaArboreaRicercaVO.setIdTipologiaVino(new Long(idTipologiaVino));
 	
 	// Effettuo la ricerca
 	Vector<StoricoParticellaVO> elencoParticelle = null;
 	if(!Validator.isNotEmpty(messaggioErrore)) 
  {
 		try 
    {
      elencoParticelle = anagFacadeClient.getListParticelleUvBasic(filtriUnitaArboreaRicercaVO, anagAziendaVO);      
 		}
 		catch(SolmrException se) {
   		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_UV;
 		}
 	}
  
    
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) || (elencoParticelle.size() == 0)) 
  {
 		htmpl.newBlock("blkNoParticelle");
 		if(Validator.isNotEmpty(messaggioErrore)) 
 		{
   		htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
 		}
 		else 
 		{
 			htmpl.set("blkNoParticelle.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_UV_FOUND);
 		}
 	}
 	// Altrimenti visualizzo l'elenco delle particelle trovate
 	else 
  {
   	htmpl.newBlock("blkUv");
   	for(int i = 0; i < elencoParticelle.size(); i++) 
    {
   		htmpl.newBlock("blkUv.blkElencoUv");
   		StoricoParticellaVO storicoParticellaElencoVO = elencoParticelle.get(i);
   		
   		if(Validator.isEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO()))
      {
        htmpl.set("blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea());
        htmpl.set("blkUv.blkElencoUv.progrUnar", storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getProgrUnar());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO()))
          htmpl.set("blkUv.blkElencoUv.destProd", "["+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getCodice()+"] "+
            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoUtilizzoVO().getDescrizione());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO()))
          htmpl.set("blkUv.blkElencoUv.descVitigno", "["+storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getCodiceVarieta()+"] "+
            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoVarietaVO().getDescrizione());
        htmpl.set("blkUv.blkElencoUv.annoImpianto", 
            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getAnnoImpianto());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO()))
          htmpl.set("blkUv.blkElencoUv.descIdoneita", 
            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getTipoTipologiaVinoVO().getDescrizione());
        htmpl.set("blkUv.blkElencoUv.annoIdoneita", 
            storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getAnnoIscrizioneAlbo());      
        htmpl.set("blkUv.blkElencoUv.supVitata", StringUtils.parseSuperficieField(
          storicoParticellaElencoVO.getStoricoUnitaArboreaVO().getArea()));
      }
      else
      {
        htmpl.set("blkUv.blkElencoUv.idStoricoUnitaArborea", ""+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea());
        htmpl.set("blkUv.blkElencoUv.progrUnar", storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getProgrUnar());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO()))
          htmpl.set("blkUv.blkElencoUv.destProd", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getCodice()+"] "+
            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoUtilizzoVO().getDescrizione());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO()))
          htmpl.set("blkUv.blkElencoUv.descVitigno", "["+storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getCodiceVarieta()+"] "+
            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoVarietaVO().getDescrizione());
        htmpl.set("blkUv.blkElencoUv.annoImpianto", 
            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoImpianto());
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO()))
          htmpl.set("blkUv.blkElencoUv.descIdoneita", 
            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getTipoTipologiaVinoVO().getDescrizione());
        htmpl.set("blkUv.blkElencoUv.annoIdoneita", 
            storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getAnnoIscrizioneAlbo());      
        htmpl.set("blkUv.blkElencoUv.supVitata", StringUtils.parseSuperficieField(
          storicoParticellaElencoVO.getUnitaArboreaDichiarataVO().getArea()));
      }
   		htmpl.set("blkUv.blkElencoUv.descComuneParticella", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
   		htmpl.set("blkUv.blkElencoUv.siglaProvinciaParticella", storicoParticellaElencoVO.getComuneParticellaVO().getSiglaProv());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSezione())) {
     		htmpl.set("blkUv.blkElencoUv.sezione", storicoParticellaElencoVO.getSezione().toUpperCase());
   		}
   		htmpl.set("blkUv.blkElencoUv.foglio", storicoParticellaElencoVO.getFoglio());
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticella())) {
     		htmpl.set("blkUv.blkElencoUv.particella", storicoParticellaElencoVO.getParticella());
   		}
   		if(Validator.isNotEmpty(storicoParticellaElencoVO.getSubalterno())) {
     		htmpl.set("blkUv.blkElencoUv.subalterno", storicoParticellaElencoVO.getSubalterno());
   		}
    }
 	}

%>
<%= htmpl.text()%>
