<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeElimina.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	TipoCessazioneUnarVO[] elencoCessazioniUnar = (TipoCessazioneUnarVO[])request.getAttribute("elencoCessazioniUnar");
	StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Long idCessazionUnar = (Long)request.getAttribute("idCessazionUnar");
 	String note = (String)request.getAttribute("note");
  Vector<Long> vPraticheIdParticella = (Vector<Long>)request.getAttribute("vPraticheIdParticella");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  Vector<Long> vIdStoricoUvModVITI = (Vector<Long>)request.getAttribute("vIdStoricoUvModVITI");
 	
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
 	
	htmpl.newBlock("blkOperazioneCessazione"); 		
	// Combo motivo cessazione
	if(elencoCessazioniUnar != null && elencoCessazioniUnar.length > 0) 
  {
		for(int i = 0; i < elencoCessazioniUnar.length; i++) 
    {
			TipoCessazioneUnarVO tipoCessazioneUnarVO = (TipoCessazioneUnarVO)elencoCessazioniUnar[i];
			htmpl.newBlock("blkOperazioneCessazione.blkTipiCessazioneUnar");
			htmpl.set("blkOperazioneCessazione.blkTipiCessazioneUnar.idCessazionUnar", tipoCessazioneUnarVO.getIdCessazioneUnar().toString());
			htmpl.set("blkOperazioneCessazione.blkTipiCessazioneUnar.descrizione", tipoCessazioneUnarVO.getDescrizione());
			if(idCessazionUnar != null 
        && tipoCessazioneUnarVO.getIdCessazioneUnar().compareTo(idCessazionUnar) == 0) 
      {
				htmpl.set("blkOperazioneCessazione.blkTipiCessazioneUnar.selected", "selected=\"selected\"");
			}
		}
	}
	// Note
	if(Validator.isNotEmpty(note)) 
  {
		htmpl.set("blkOperazioneCessazione.note", note);
	}
	// Elenco unità arboree selezionate
	if(elencoStoricoParticellaArboreaVO != null && elencoStoricoParticellaArboreaVO.length > 0) 
  {
		for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
    {
			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
			StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
			htmpl.newBlock("blkOperazioneCessazione.blkElenco");
      if(Validator.isNotEmpty(vPraticheIdParticella))
      {
        if(vPraticheIdParticella.contains(storicoParticellaVO.getIdParticella()))
        {
          htmpl.set("blkOperazioneCessazione.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT+"'",
            AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT}), null);
        }
      }
      
      
      if(Validator.isNotEmpty(vIdStoricoUvModVITI))
      {
        if(vIdStoricoUvModVITI.contains(storicoUnitaArboreaVO.getIdStoricoUnitaArborea()))
        {
          htmpl.set("blkOperazioneCessazione.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA+"'",
            AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA}), null);
        }
      }
      
      
      
			htmpl.set("blkOperazioneCessazione.blkElenco.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
	 		htmpl.set("blkOperazioneCessazione.blkElenco.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv()); 			
	 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
	 			htmpl.set("blkOperazioneCessazione.blkElenco.sezione", storicoParticellaVO.getSezione());
	 		}
	 		htmpl.set("blkOperazioneCessazione.blkElenco.foglio", storicoParticellaVO.getFoglio());
	 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
	 			htmpl.set("blkOperazioneCessazione.blkElenco.particella", storicoParticellaVO.getParticella());
	 		}
	 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
	 			htmpl.set("blkOperazioneCessazione.blkElenco.subalterno", storicoParticellaVO.getSubalterno());
	 		}
	 		htmpl.set("blkOperazioneCessazione.blkElenco.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
      htmpl.set("blkOperazioneCessazione.blkElenco.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
	 		htmpl.set("blkOperazioneCessazione.blkElenco.progressivo", storicoUnitaArboreaVO.getProgrUnar());
	 		if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
	 			String codice = "";
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) {
					codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
					htmpl.set("blkOperazioneCessazione.blkElenco.destinazioneProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
				}
	 		}
	 		if(storicoUnitaArboreaVO.getIdVarieta() != null) {
	 			htmpl.set("blkOperazioneCessazione.blkElenco.vitigno", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	 		}
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
	 			htmpl.set("blkOperazioneCessazione.blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	 		}
		}
	}
	// Gestisco gli errori
	if(errors != null && errors.size() > 0) {
		Iterator iter = htmpl.getVariableIterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
		    if(key.startsWith("err_")) {
		    	String property = key.substring(4);
		    	Iterator errorIterator = errors.get(property);
		    	if(errorIterator != null) {
		    		ValidationError error = (ValidationError)errorIterator.next();
		    		htmpl.set("blkOperazioneCessazione.err_"+property,
		                      MessageFormat.format(htmlStringKO,
		                      new Object[] {
		                      pathErrori + "/"+ imko,
		                      "'"+jssp.process(error.getMessage())+"'",
		                      error.getMessage()}),
		                      null);
		    	}
		    }
		}
	}
  
  
  // Se si è verificato un errore 
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
 		

%>
<%= htmpl.text()%>

