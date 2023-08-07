<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popCercaParticelleInsUnarFrazionate.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	StoricoParticellaVO[] elencoParticelle = (StoricoParticellaVO[])request.getAttribute("elencoParticelle");
 	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	// Se ci sono errori li visualizzo
 	if(Validator.isNotEmpty(messaggioErrore) || elencoParticelle.length == 0) 
 	{
    htmpl.newBlock("blkNoParticelle");
   	if(Validator.isNotEmpty(messaggioErrore)) 
   	{
	  	htmpl.set("blkNoParticelle.messaggioErrore", messaggioErrore);
   	}
   	else 
   	{
   	  htmpl.set("blkNoParticelle.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
    }
 	}
 	// Altrimenti visualizzo l'elenco delle particelle trovate
 	else 
 	{
   	htmpl.newBlock("blkParticelle");
   	htmpl.set("blkParticelle.numeroRecords", String.valueOf(elencoParticelle.length));
   	int countImportabili = 0;
		for(int i = 0; i < elencoParticelle.length; i++) 
		{
			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
			StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
			htmpl.newBlock("blkParticelle.blkElencoParticelle");
			
			
			boolean trovato = false;
			if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
      {
				if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
	      {
	        for(int j = 0; j < elencoTipiUsoSuolo.length; j++) 
	        {
	          TipoUtilizzoVO tipoUtilizzoVO = elencoTipiUsoSuolo[j];
	          if((storicoUnitaArboreaVO.getIdUtilizzo() != null) 
	            && storicoUnitaArboreaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
	          {
	            if("S".equalsIgnoreCase(tipoUtilizzoVO.getFlagUnar()))
	            {                 
	              trovato = true;
	              break;   
	            }
	          }
	        }
	      }
	    }
	    
	    if(trovato && ruoloUtenza.isUtenteIntermediario())
	    {
	      htmpl.newBlock("blkParticelle.blkElencoParticelle.blkKOImport");
	    }
	    else
	    {
	      htmpl.newBlock("blkParticelle.blkElencoParticelle.blkOKImport");
	      countImportabili++;
	    }
			
			
			
			
			htmpl.set("blkParticelle.blkElencoParticelle.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
			htmpl.set("blkParticelle.blkElencoParticelle.cuaa", storicoUnitaArboreaVO.getCuaa());
			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getDenominazione())) 
			{
				if(storicoUnitaArboreaVO.getDenominazione().length() > 100) 
				{
					htmpl.set("blkParticelle.blkElencoParticelle.denominazione", storicoUnitaArboreaVO.getDenominazione().substring(0, 100));
				}
				else 
				{
					htmpl.set("blkParticelle.blkElencoParticelle.denominazione", storicoUnitaArboreaVO.getDenominazione());
				}
			}
			htmpl.set("blkParticelle.blkElencoParticelle.codiceFiscaleIntermediario", storicoUnitaArboreaVO.getCodFiscaleIntermediario());
			htmpl.set("blkParticelle.blkElencoParticelle.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
	 		htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv()); 			
	 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());
	 		}
	 		htmpl.set("blkParticelle.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
	 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.particella", storicoParticellaVO.getParticella());
	 		}
	 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
	 		}
	 		htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
      htmpl.set("blkParticelle.blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
	 		htmpl.set("blkParticelle.blkElencoParticelle.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
	 		if(storicoUnitaArboreaVO.getIdUtilizzo() != null) 
	 		{
	 			String codice = "";
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) 
				{
					codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
					htmpl.set("blkParticelle.blkElencoParticelle.destinazioneProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
				}
	 		}
	 		if(storicoUnitaArboreaVO.getIdVarieta() != null) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.vitigno", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	 		}
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	 		}
	 		//htmpl.set("blkParticelle.blkElencoParticelle.annoImpianto", storicoUnitaArboreaVO.getAnnoImpianto());
	 		if(storicoUnitaArboreaVO.getDataImpianto() != null) 
	 		{
        htmpl.set("blkParticelle.blkElencoParticelle.dataImpianto", DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
      }
      if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) 
      {
        htmpl.set("blkParticelle.blkElencoParticelle.dataPrimaProduzione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataPrimaProduzione()));
      }
      htmpl.set("blkParticelle.blkElencoParticelle.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
	 		htmpl.set("blkParticelle.blkElencoParticelle.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
	 		htmpl.set("blkParticelle.blkElencoParticelle.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
	 		if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
	 		}
	 		htmpl.set("blkParticelle.blkElencoParticelle.percentualeVitigno", storicoUnitaArboreaVO.getPercentualeVarieta());
	 		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.altriVitigni", SolmrConstants.FLAG_SI);
	 		}
	 		else 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.altriVitigni", SolmrConstants.FLAG_NO);
	 		}
	 		if(storicoUnitaArboreaVO.getDataCessazione() != null) 
	 		{
	 			htmpl.set("blkParticelle.blkElencoParticelle.dataCessazione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataCessazione()));
	 		}
		}
		
		if(countImportabili > 0)
		{
		  htmpl.newBlock("blkParticelle.blkInserisci");
		  htmpl.newBlock("blkParticelle.blkButton");
		}
 	}

%>
<%= htmpl.text()%>
