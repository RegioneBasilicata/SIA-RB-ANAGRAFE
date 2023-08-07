<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>

<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popSchedarioViticolo.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	StoricoParticellaVO[] elencoUnitaArboreeImportabili = (StoricoParticellaVO[])request.getAttribute("elencoUnitaArboreeImportabili");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
	
 	// Se si è verificato un errore 
 	if(Validator.isNotEmpty(messaggioErrore)) {
 		htmpl.newBlock("blkNoUnitaArboree");
 		htmpl.set("blkNoUnitaArboree.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti...
 	else 
 	{
		// Elenco unità arboree selezionate
		if(elencoUnitaArboreeImportabili != null && elencoUnitaArboreeImportabili.length > 0) 
		{
			Hashtable<Long,String> mappa = new Hashtable<Long,String>();
      Hashtable<Long,String> mappaSupGrafica = new Hashtable<Long,String>();
			double totArea = 0;
			int countImportabile = 0;
			htmpl.set("blkUnitaArboree.numeroRecords", String.valueOf(elencoUnitaArboreeImportabili.length));
			for(int i = 0; i < elencoUnitaArboreeImportabili.length; i++) 
			{
				StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoUnitaArboreeImportabili[i];
				StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
				
				
				
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
				
				
				
				
				
				
				htmpl.newBlock("blkUnitaArboree.blkElenco");
				if(trovato && ruoloUtenza.isUtenteIntermediario())
				{
				  htmpl.newBlock("blkUnitaArboree.blkElenco.blkNoCheck");
          htmpl.set("blkUnitaArboree.blkElenco.blkNoCheck.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
				}
				else if(SolmrConstants.FLAG_S_BINARIO.equalsIgnoreCase(storicoUnitaArboreaVO.getImportabile())) 
				{
					htmpl.newBlock("blkUnitaArboree.blkElenco.blkCheck");
					htmpl.set("blkUnitaArboree.blkElenco.blkCheck.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
					countImportabile++;
				}
				else 
				{
					htmpl.newBlock("blkUnitaArboree.blkElenco.blkNoCheck");
					htmpl.set("blkUnitaArboree.blkElenco.blkNoCheck.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
				}
				htmpl.set("blkUnitaArboree.blkElenco.cuaa", storicoUnitaArboreaVO.getCuaa());
				if(Validator.isNotEmpty(storicoUnitaArboreaVO.getDenominazione())) {
					if(storicoUnitaArboreaVO.getDenominazione().length() > 100) {
						htmpl.set("blkUnitaArboree.blkElenco.denominazione", storicoUnitaArboreaVO.getDenominazione().substring(0, 100));
					}
					else {
						htmpl.set("blkUnitaArboree.blkElenco.denominazione", storicoUnitaArboreaVO.getDenominazione());
					}
				}
				htmpl.set("blkUnitaArboree.blkElenco.codiceFiscaleIntermediario", storicoUnitaArboreaVO.getCodFiscaleIntermediario());
				htmpl.set("blkUnitaArboree.blkElenco.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
		 		htmpl.set("blkUnitaArboree.blkElenco.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv()); 			
		 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
		 			htmpl.set("blkUnitaArboree.blkElenco.sezione", storicoParticellaVO.getSezione());
		 		}
		 		htmpl.set("blkUnitaArboree.blkElenco.foglio", storicoParticellaVO.getFoglio());
		 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
		 			htmpl.set("blkUnitaArboree.blkElenco.particella", storicoParticellaVO.getParticella());
		 		}
		 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
		 			htmpl.set("blkUnitaArboree.blkElenco.subalterno", storicoParticellaVO.getSubalterno());
		 		}
		 		htmpl.set("blkUnitaArboree.blkElenco.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
        htmpl.set("blkUnitaArboree.blkElenco.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
		 		htmpl.set("blkUnitaArboree.blkElenco.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
		 		if((mappa.get(storicoParticellaVO.getIdStoricoParticella()) == null)) {
					mappa.put(storicoParticellaVO.getIdStoricoParticella(), storicoParticellaVO.getSupCatastale());
				}
        if((mappaSupGrafica.get(storicoParticellaVO.getIdStoricoParticella()) == null)) {
          mappaSupGrafica.put(storicoParticellaVO.getIdStoricoParticella(), storicoParticellaVO.getSuperficieGrafica());
        }
		 		if(storicoUnitaArboreaVO.getIdUtilizzo() != null) {
		 			String codice = "";
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice())) {
						codice += "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] ";
						htmpl.set("blkUnitaArboree.blkElenco.destinazioneProduttiva", codice + storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
					}
		 		}
		 		if(storicoUnitaArboreaVO.getIdVarieta() != null) {
		 			htmpl.set("blkUnitaArboree.blkElenco.vitigno", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
		 		}
		 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
		 			htmpl.set("blkUnitaArboree.blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
		 			totArea += Double.valueOf(storicoUnitaArboreaVO.getArea().replace(',', '.')).doubleValue();
		 		}
		 		//htmpl.set("blkUnitaArboree.blkElenco.annoImpianto", storicoUnitaArboreaVO.getAnnoImpianto());
        if(storicoUnitaArboreaVO.getDataImpianto() != null) {
          htmpl.set("blkUnitaArboree.blkElenco.dataImpianto", DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
        }
        if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null) {
          htmpl.set("blkUnitaArboree.blkElenco.dataPrimaProduzione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataPrimaProduzione()));
        }
		 		htmpl.set("blkUnitaArboree.blkElenco.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
		 		htmpl.set("blkUnitaArboree.blkElenco.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
		 		htmpl.set("blkUnitaArboree.blkElenco.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
		 		if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
		 			htmpl.set("blkUnitaArboree.blkElenco.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
		 		}
		 		htmpl.set("blkUnitaArboree.blkElenco.percentualeVitigno", storicoUnitaArboreaVO.getPercentualeVarieta());
		 		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
		 			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_SI);
		 		}
		 		else {
		 			htmpl.set("blkUnitaArboree.blkElenco.altriVitigni", SolmrConstants.FLAG_NO);
		 		}
		 		if(storicoUnitaArboreaVO.getDataCessazione() != null) {
		 			htmpl.set("blkUnitaArboree.blkElenco.dataCessazione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataCessazione()));
		 		}
			}
			if(countImportabile > 0) 
			{
				htmpl.newBlock("blkUnitaArboree.blkImporta");
				htmpl.newBlock("blkUnitaArboree.blkButton");
			}
			// TOTALI
			Enumeration<Long> enumeraMappa = mappa.keys();
			double totSupCatastale = 0;
			while(enumeraMappa.hasMoreElements()) {
				Long idStoricoParticella = (Long)enumeraMappa.nextElement();
				String supCatastale = (String)mappa.get(idStoricoParticella);
				totSupCatastale += Double.valueOf(supCatastale.replace(',', '.')).doubleValue();
			}
			htmpl.set("blkUnitaArboree.totSupCatastale", StringUtils.parseSuperficieField(String.valueOf(totSupCatastale)));
      Enumeration<Long> enumeraMappaSupGrafica = mappaSupGrafica.keys();
      double totSuperficieGrafica = 0;
      while(enumeraMappaSupGrafica.hasMoreElements()) {
        Long idStoricoParticella = (Long)enumeraMappaSupGrafica.nextElement();
        String superficieGrafica = (String)mappaSupGrafica.get(idStoricoParticella);
        totSuperficieGrafica += Double.valueOf(superficieGrafica.replace(',', '.')).doubleValue();
      }
      htmpl.set("blkUnitaArboree.totSuperficieGrafica", StringUtils.parseSuperficieField(String.valueOf(totSuperficieGrafica)));
			htmpl.set("blkUnitaArboree.totArea", StringUtils.parseSuperficieField(String.valueOf(totArea)));
		}
		// Se non ho trovato unità arboree importabili
		else {
			htmpl.newBlock("blkNoUnitaArboree");
	 		htmpl.set("blkNoUnitaArboree.messaggioErrore", AnagErrors.ERRORE_NO_UV_FOUND_IMPORT_BY_SCHEDARIO);
		}
	}	

%>
<%= htmpl.text()%>

