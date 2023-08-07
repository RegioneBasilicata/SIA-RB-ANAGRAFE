<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeInserisci.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
	Vector<TipoDestinazioneVO> vTipoDestinazione = (Vector<TipoDestinazioneVO>)request.getAttribute("vTipoDestinazione");
	Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = (Vector<TipoDettaglioUsoVO>)request.getAttribute("vTipoDettaglioUso");
	Vector<TipoQualitaUsoVO> vTipoQualitaUso = (Vector<TipoQualitaUsoVO>)request.getAttribute("vTipoQualitaUso");
	Vector<TipoVarietaVO> vTipoVarieta = (Vector<TipoVarietaVO>)request.getAttribute("vTipoVarieta");
	TipoFormaAllevamentoVO[] elencoFormeAllevamento = (TipoFormaAllevamentoVO[])request.getAttribute("elencoFormeAllevamento");
	TipoCausaleModificaVO[] elencoCausaleModifica = (TipoCausaleModificaVO[])request.getAttribute("elencoCausaleModifica");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO)request.getAttribute("storicoUnitaArboreaVO");
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	Vector<TipoVarietaVO> elencoVarietaVitigni = (Vector<TipoVarietaVO>)request.getAttribute("elencoVarietaVitigni");
 	Hashtable<Integer,ValidationErrors> erroriAltriVitigni = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriAltriVitigni");
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	String parametroAccessoDtSovr = (String)request.getAttribute("parametroAccessoDtSovr");
 	
 	
 	
 	if(ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteProvinciale()) 
  {
    htmpl.set("valuePulsante", "conferma e valida");
    htmpl.newBlock("blkConfermaPA");
  }
  else 
  {
    htmpl.set("valuePulsante", "conferma");
  }
 	
 	
 	
 	
 	//Se valorizzato è la prima volta che entro del caso duplica
 	boolean primaVoltaDuplica = false;
  if(Validator.isNotEmpty(request.getParameter("idUnita")))
  {
    primaVoltaDuplica = true;
  }
  
  
  //Gestione errori
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
	
	if(storicoParticellaVO != null) 
  {
		htmpl.set("esito", SolmrConstants.FLAG_S);
		htmpl.set("descComuneUv", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("siglaProvUv", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
		htmpl.set("sezioneUv", storicoParticellaVO.getSezione());
		htmpl.set("foglioUv", storicoParticellaVO.getFoglio());
		htmpl.set("particellaUv", storicoParticellaVO.getParticella());
		htmpl.set("subalternoUv", storicoParticellaVO.getSubalterno());
		htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
    htmpl.set("provinciaCCIAA", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
  
  }
	
	//prima volta che entro col caso duplica
	if(primaVoltaDuplica)
	{
	  htmpl.set("duplica", "duplica");
	  String menzioneGeografica = "";
    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getElencoMenzioneGeografica()))
    {
      Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = storicoUnitaArboreaVO.getElencoMenzioneGeografica();
      for(int i=0;i<elencoMenzioneGeografica.size();i++)
      {
        TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
        if(i !=0)
        {
          menzioneGeografica += " - "; 
        }          
        menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
      }
    }
     
    htmpl.set("menzioneGeografica", menzioneGeografica);
     
    // DATA IMPIANTO
    htmpl.set("dataImpianto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataImpianto()));
    // ANNO RIFERIMENTO
    htmpl.set("annoRiferimento", storicoUnitaArboreaVO.getAnnoRiferimento());
    //DATA PRIMA PRODUZIONE
    htmpl.set("dataPrimaProduzione", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataPrimaProduzione()));
    //DATA SOVRAINNESTO
    if(parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
      && !ruoloUtenza.isUtentePA())
    {
      htmpl.set("disabledDataSovr", "disabled=\"disabled\"",null);
    }
    htmpl.set("dataSovrainnesto", "");
    // SUPERFICIE VITATA
    htmpl.set("area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea())); 
    // SESTO SU FILA
    htmpl.set("sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
    // SESTO TRA FILE
    htmpl.set("sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
    // NUMERO CEPPI
    htmpl.set("numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
    // PERCENTUALE VITIGNO
    htmpl.set("percentualeVitigno", storicoUnitaArboreaVO.getPercentualeVarieta());
    // PERCENTUALE Fallanza
    htmpl.set("percentualeFallanza", Formatter.formatDouble(storicoUnitaArboreaVO.getPercentualeFallanza()));
    // Flag improduttiva
    if(SolmrConstants.FLAG_S.equalsIgnoreCase(storicoUnitaArboreaVO.getFlagImproduttiva())) 
    {
      htmpl.set("checkedImproduttivaS", "checked=\"checked\"");
    }
    else 
    {
      htmpl.set("checkedImproduttivaN", "checked=\"checked\"");
    }
    // ALTRI VITIGNI
    if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      htmpl.set("checkedS", "checked=\"checked\"");
    }
    else 
    {
      htmpl.set("checkedN", "checked=\"checked\"");
    }
    // COLTURA SPECIALIZZATA
    if(storicoUnitaArboreaVO.getColturaSpecializzata().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      htmpl.set("checkedSiSpec", "checked=\"checked\"");
    }
    else 
    {
      htmpl.set("checkedNoSpec", "checked=\"checked\"");
    }
    // RICADUTA
    if(storicoUnitaArboreaVO.getRicaduta().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      htmpl.set("checkedSiRicaduta", "checked=\"checked\"");
    }
    else 
    {
      htmpl.set("checkedNoRicaduta", "checked=\"checked\"");
    }
     
    //Matricola CCIAA
    htmpl.set("matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
     
    //Anno iscrizione albo
    htmpl.set("annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());    
     
    //Vigna
    htmpl.set("vigna", storicoUnitaArboreaVO.getVigna());
     
     
    //Annotazione in Etichetta
    htmpl.set("annotazioneEtichetta", storicoUnitaArboreaVO.getEtichetta());
	
	}
	//Inserimento normale
	else
	{
	
	  htmpl.set("duplica", request.getParameter("duplica"));
	
		if(storicoUnitaArboreaVO != null) 
	  {
	  
	  
	    String menzioneGeografica = "";
	    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getElencoMenzioneGeografica()))
	    {
	      Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = storicoUnitaArboreaVO.getElencoMenzioneGeografica();
	      for(int i=0;i<elencoMenzioneGeografica.size();i++)
	      {
	        TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
	        if(i !=0)
	        {
	          menzioneGeografica += " - "; 
	        }          
	        menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
	      }
	    }
	    
	    htmpl.set("menzioneGeografica", menzioneGeografica);
	    
			// DATA IMPIANTO
			htmpl.set("dataImpianto", request.getParameter("dataImpianto"));
			// ANNO RIFERIMENTO
			htmpl.set("annoRiferimento", storicoUnitaArboreaVO.getAnnoRiferimento());
	    //DATA PRIMA PRODUZIONE
	    htmpl.set("dataPrimaProduzione", request.getParameter("dataPrimaProduzione"));
	    //DATA SOVRAINNESTO
	    if(!parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_TUTTO)
	      && !ruoloUtenza.isUtentePA())
      {
        htmpl.set("disabledDataSovr", "disabled=\"disabled\"",null);
      }
      htmpl.set("dataSovrainnesto", request.getParameter("dataSovrainnesto"));
			// SUPERFICIE VITATA
			htmpl.set("area", storicoUnitaArboreaVO.getArea());	
			// SESTO SU FILA
			htmpl.set("sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
			// SESTO TRA FILE
			htmpl.set("sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
			// NUMERO CEPPI
			htmpl.set("numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
			// PERCENTUALE VITIGNO
			htmpl.set("percentualeVitigno", storicoUnitaArboreaVO.getPercentualeVarieta());
			// PERCENTUALE FALLANZA
			String percentualeFallanza = request.getParameter("percentualeFallanza");
			if(Validator.isEmpty(percentualeFallanza))
			{
			  percentualeFallanza = "0";
			}
      htmpl.set("percentualeFallanza", percentualeFallanza);
      // Flag improduttiva
      if(SolmrConstants.FLAG_S.equalsIgnoreCase(storicoUnitaArboreaVO.getFlagImproduttiva())) 
      {
        htmpl.set("checkedImproduttivaS", "checked=\"checked\"");
      }
      else 
      {
        htmpl.set("checkedImproduttivaN", "checked=\"checked\"");
      }
			// ALTRI VITIGNI
			if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	    {
				htmpl.set("checkedS", "checked=\"checked\"");
			}
			else 
	    {
				htmpl.set("checkedN", "checked=\"checked\"");
			}
			// COLTURA SPECIALIZZATA
			if(storicoUnitaArboreaVO.getColturaSpecializzata().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	    {
				htmpl.set("checkedSiSpec", "checked=\"checked\"");
			}
			else 
	    {
				htmpl.set("checkedNoSpec", "checked=\"checked\"");
			}
			// RICADUTA
			if(storicoUnitaArboreaVO.getRicaduta().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
			{
				htmpl.set("checkedSiRicaduta", "checked=\"checked\"");
			}
			else 
			{
				htmpl.set("checkedNoRicaduta", "checked=\"checked\"");
			}
	    
	    //Matricola CCIAA
	    String matricolaCCIAA = request.getParameter("matricolaCCIAA");
	    if(Validator.isNotEmpty(matricolaCCIAA))
	    {
	      htmpl.set("matricolaCCIAA", matricolaCCIAA);
	    }
	    else
	    {
	      htmpl.set("matricolaCCIAA","");
	    }
	    
	    //Anno iscrizione albo
	    String annoIscrizioneAlbo = request.getParameter("annoIscrizioneAlbo");
	    if(Validator.isNotEmpty(annoIscrizioneAlbo))
	    {
	      htmpl.set("annoIscrizioneAlbo", annoIscrizioneAlbo);
	    }
	    else
	    {
	      htmpl.set("annoIscrizioneAlbo","");
	    }
	    
	    
	    //Vigna
	    String vigna = request.getParameter("vigna");
	    if(Validator.isNotEmpty(vigna))
	    {
	      htmpl.set("vigna", vigna);
	    }
	    else
	    {
	      htmpl.set("vigna","");
	    }
	    
	    
	    //Annotazione in Etichetta
	    String annotazioneEtichetta = request.getParameter("annotazioneEtichetta");
	    if(Validator.isNotEmpty(annotazioneEtichetta))
	    {
	      htmpl.set("annotazioneEtichetta", annotazioneEtichetta);
	    }
	    else
	    {
	      htmpl.set("annotazioneEtichetta","");
	    }
	    
		}
  }
  
  
			
	// DESTINAZIONE_PRODUTTIVA
	if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
  {
		for(int i = 0; i < elencoTipiUsoSuolo.length; i++) {
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo[i];
			htmpl.newBlock("blkTipiUsoSuolo");
			htmpl.set("blkTipiUsoSuolo.idUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
			htmpl.set("blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
			if(storicoUnitaArboreaVO != null && storicoUnitaArboreaVO.getIdUtilizzo() != null && storicoUnitaArboreaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
				htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"");
			}
		}
	}
	
	if(Validator.isNotEmpty(vTipoDestinazione)) 
  {
    for(int d = 0; d < vTipoDestinazione.size(); d++) 
    {
      TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(d);
      htmpl.newBlock("blkTipiDestinazione");
      htmpl.set("blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
      htmpl.set("blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
      if(storicoUnitaArboreaVO.getIdTipoDestinazione() != null) 
      {
        if(storicoUnitaArboreaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
        {
          htmpl.set("blkTipiDestinazione.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  if(Validator.isNotEmpty(vTipoDettaglioUso)) 
  {
    for(int d = 0; d < vTipoDettaglioUso.size(); d++) 
    {
      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(d);
      htmpl.newBlock("blkTipiDettaglioUso");
      htmpl.set("blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
      htmpl.set("blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
      if(storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null) 
      {
        if(storicoUnitaArboreaVO.getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
        {
          htmpl.set("blkTipiDettaglioUso.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  if(Validator.isNotEmpty(vTipoQualitaUso)) 
  {
    for(int d = 0; d < vTipoQualitaUso.size(); d++) 
    {
      TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(d);
      htmpl.newBlock("blkTipiQualitaUso");
      htmpl.set("blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
      htmpl.set("blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+tipoQualitaUsoVO.getDescrizioneQualitaUso());
      if(storicoUnitaArboreaVO.getIdTipoQualitaUso() != null) 
      {
        if(storicoUnitaArboreaVO.getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
        {
          htmpl.set("blkTipiQualitaUso.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
	// Carico la combo VITIGNO solo se l'utente ha selezionato il tipo
	// uso suolo corrispondente
	if(Validator.isNotEmpty(vTipoVarieta)) 
  {
		for(int i = 0; i < vTipoVarieta.size(); i++) 
    {
			TipoVarietaVO tipoVarietaVO = vTipoVarieta.get(i);
			htmpl.newBlock("blkTipiVarieta");
			htmpl.set("blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
			htmpl.set("blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
			if(storicoUnitaArboreaVO != null && storicoUnitaArboreaVO.getIdVarieta() != null) {
				if(storicoUnitaArboreaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) {
					htmpl.set("blkTipiVarieta.selected", "selected=\"selected\"", null);
				}
			}
		}
	}		
	// FORMA ALLEVAMENTO
	if(elencoFormeAllevamento != null && elencoFormeAllevamento.length > 0) 
  {
		for(int e = 0; e < elencoFormeAllevamento.length; e++) {
			TipoFormaAllevamentoVO tipoFormaAllevamentoVO = (TipoFormaAllevamentoVO)elencoFormeAllevamento[e];
			htmpl.newBlock("blkTipiFormaAllevamento");
			htmpl.set("blkTipiFormaAllevamento.idFormaAllevamento", tipoFormaAllevamentoVO.getIdFormaAllevamento().toString());
			htmpl.set("blkTipiFormaAllevamento.descrizione", tipoFormaAllevamentoVO.getDescrizione());
			if(storicoUnitaArboreaVO != null && storicoUnitaArboreaVO.getIdFormaAllevamento() != null && storicoUnitaArboreaVO.getIdFormaAllevamento().compareTo(tipoFormaAllevamentoVO.getIdFormaAllevamento()) == 0) {
				htmpl.set("blkTipiFormaAllevamento.selected", "selected=\"selected\"");
			}
		}
	}
	// CAUSALI MODIFICA
	if(elencoCausaleModifica != null && elencoCausaleModifica.length > 0) 
  {
		for(int f = 0; f < elencoCausaleModifica.length; f++) {
			TipoCausaleModificaVO tipoCausaleModificaVO = (TipoCausaleModificaVO)elencoCausaleModifica[f];
			htmpl.newBlock("blkTipiCausaliModifica");
			htmpl.set("blkTipiCausaliModifica.idCausaleModifica", tipoCausaleModificaVO.getIdCausaleModifica().toString());
			htmpl.set("blkTipiCausaliModifica.descrizione", tipoCausaleModificaVO.getDescrizione());
			if(storicoUnitaArboreaVO != null && storicoUnitaArboreaVO.getIdCausaleModifica() != null && storicoUnitaArboreaVO.getIdCausaleModifica().compareTo(tipoCausaleModificaVO.getIdCausaleModifica()) == 0) {
				htmpl.set("blkTipiCausaliModifica.selected", "selected=\"selected\"");
			}
		}
	}
  
  
  if((request.getAttribute("vTipoTipologiaVino") != null) 
    && (storicoUnitaArboreaVO.getIdUtilizzo() != null)
    && (storicoUnitaArboreaVO.getIdVarieta() !=null))
  {
    Vector<?> vTipoTipologiaVino = (Vector<?>)request.getAttribute("vTipoTipologiaVino");
    for(int i=0;i<vTipoTipologiaVino.size();i++)
    {
      TipoTipologiaVinoVO tipoTipologiaVinoVO = (TipoTipologiaVinoVO)vTipoTipologiaVino.get(i);
      htmpl.newBlock("blkTipologiaHiddenVino");
      if((tipoTipologiaVinoVO.getVinoDoc() != null) && tipoTipologiaVinoVO.getVinoDoc().equalsIgnoreCase("S"))
      {        
        htmpl.set("blkTipologiaHiddenVino.idTipologiaVinoDoc", "S");
      }
      else
      {
        htmpl.set("blkTipologiaHiddenVino.idTipologiaVinoDoc", "N");
      }
      
      if(tipoTipologiaVinoVO.getCodiceMipaf() != null)
      {
        htmpl.set("blkTipologiaHiddenVino.hiddenCodiceMipaf", tipoTipologiaVinoVO.getCodiceMipaf()); 
      }
      else
      {
        htmpl.set("blkTipologiaHiddenVino.hiddenCodiceMipaf", "");
      }
      
      if(tipoTipologiaVinoVO.getFlagGestioneVigna() != null)
      {
        htmpl.set("blkTipologiaHiddenVino.hiddenFlagGestioneVigna", tipoTipologiaVinoVO.getFlagGestioneVigna()); 
      }
      
      if(tipoTipologiaVinoVO.getFlagGestioneEtichetta() != null)
      {
        htmpl.set("blkTipologiaHiddenVino.hiddenFlagGestioneEtichetta", tipoTipologiaVinoVO.getFlagGestioneEtichetta()); 
      }
      
      if(tipoTipologiaVinoVO.getVignaVO() != null)
      {        
        htmpl.set("blkTipologiaHiddenVino.hiddenProvVignaRegionale", tipoTipologiaVinoVO.getVignaVO().getMenzione()); 
      }      
      
      
      
      htmpl.newBlock("blkTipologiaVino");        
      htmpl.set("blkTipologiaVino.idTipologiaVino", ""+tipoTipologiaVinoVO.getIdTipologiaVino().longValue());
      htmpl.set("blkTipologiaVino.descrizione", tipoTipologiaVinoVO.getDescrizione());
      
      
      String idTipologiaVino = request.getParameter("idTipologiaVino");     
      
      //prima volta che entro col duplica!!!!
      if(primaVoltaDuplica)
      {
        if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdTipologiaVino()))
        {
          idTipologiaVino = storicoUnitaArboreaVO.getIdTipologiaVino().toString();
        }      
      }
      
      
      if(Validator.isNotEmpty(idTipologiaVino) 
        && (tipoTipologiaVinoVO.getIdTipologiaVino().compareTo(new Long(idTipologiaVino))== 0))
      {
        htmpl.set("blkTipologiaVino.selected", "selected=\"selected\"");
        if(Validator.isNotEmpty(tipoTipologiaVinoVO.getFlagGestioneVigna())
          && "N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneVigna()))
        {
          htmpl.set("readVigna","readOnly=\"readOnly\"", null);        
        }
        
        if(Validator.isNotEmpty(tipoTipologiaVinoVO.getFlagGestioneEtichetta())
          && "N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneEtichetta()))
        {
          htmpl.set("readAnnotazioneEtichetta","readOnly=\"readOnly\"", null);        
        }
        
        
        if((tipoTipologiaVinoVO.getVinoDoc() != null)
          && tipoTipologiaVinoVO.getVinoDoc().equalsIgnoreCase("S"))
        {
          htmpl.set("checkedVinoDoc", "checked=\"checked\"");
          htmpl.set("vinoDoc", "S");
        }
        
      }
      
    }
    
  }
  
  
  if(storicoUnitaArboreaVO != null)
  {
		AltroVitignoVO[] elencoAtriVitigni = storicoUnitaArboreaVO.getElencoAltriVitigni();
		if(elencoAtriVitigni != null && elencoAtriVitigni.length > 0) 
		{
			htmpl.newBlock("blkAltriVitigni");
			for(int i = 0; i < elencoAtriVitigni.length; i++) 
			{
				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAtriVitigni[i];
				htmpl.newBlock("blkAltriVitigni.blkElencoAltriVitigni");
				htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.numeroAltriVitigni", String.valueOf(i));
				htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.contatore", String.valueOf(i));
				if((storicoUnitaArboreaVO.getIdUtilizzo() != null) && (storicoUnitaArboreaVO.getIdTipoDestinazione() != null)
				  && (storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null) && (storicoUnitaArboreaVO.getIdTipoQualitaUso() != null))  
				{
					for(int a = 0; a < elencoVarietaVitigni.size(); a++) 
					{
						TipoVarietaVO tipoVarietaVO = elencoVarietaVitigni.get(a);
						htmpl.newBlock("blkAltriVitigni.blkElencoAltriVitigni.blkVarietaVitigno");
						htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.blkVarietaVitigno.idVarietaVitigno", tipoVarietaVO.getIdVarieta().toString());
						htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.blkVarietaVitigno.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
			 			if(altroVitignoVO != null && altroVitignoVO.getIdVarieta() != null && altroVitignoVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) {
			 				htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.blkVarietaVitigno.selected", "selected=\"selected\"");
			 			}
			 		}
				}
				htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.percentuale", altroVitignoVO.getPercentualeVitigno());
				// Gestione errori
				if(erroriAltriVitigni != null && erroriAltriVitigni.size() > 0) {
					ValidationErrors errorsAltriVitigni = (ValidationErrors)erroriAltriVitigni.get(new Integer(i));
					if(errorsAltriVitigni != null && errorsAltriVitigni.size() > 0) {
					 	Iterator<?> iter = htmpl.getVariableIterator();
					 	while(iter.hasNext()) {
					 		String chiave = (String)iter.next();
					 		if(chiave.startsWith("err_")) {
					 		    String property = chiave.substring(4);
					 		    Iterator<ValidationError> errorIterator = errorsAltriVitigni.get(property);
					 			if(errorIterator != null) {
					 			 	ValidationError error = (ValidationError)errorIterator.next();
					 			   	htmpl.set("blkAltriVitigni.blkElencoAltriVitigni.err_"+property,
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
				}
			}
		}
	}
  
  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
	
	// Se ci sono errori li visualizzo
	if(errors != null && errors.size() > 0) {
		HtmplUtil.setErrors(htmpl, errors, request, application);
	}

%>
<%= htmpl.text()%>

