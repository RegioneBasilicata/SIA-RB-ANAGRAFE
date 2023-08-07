<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeModifica.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioni = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioni");
	Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettaglioUso = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettaglioUso");
	Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUso");
	Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarieta");
  Hashtable<Integer,Vector<TipoTipologiaVinoVO>> elencoTipoTipologiaVino = (Hashtable<Integer,Vector<TipoTipologiaVinoVO>>)request.getAttribute("elencoTipoTipologiaVino");
	TipoFormaAllevamentoVO[] elencoFormeAllevamento = (TipoFormaAllevamentoVO[])request.getAttribute("elencoFormeAllevamento");
	TipoCausaleModificaVO[] elencoCausaleModifica = (TipoCausaleModificaVO[])request.getAttribute("elencoCausaleModifica");
	Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori");
	Hashtable<String,ValidationErrors> elencoErroriAltriVitigni = (Hashtable<String,ValidationErrors>)request.getAttribute("elencoErroriAltriVitigni");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	String[] elencoDataImpianto = request.getParameterValues("dataImpianto");
  String[] elencoDataPrimaProduzione = request.getParameterValues("dataPrimaProduzione");
  String[] elencoDataSovrainnesto = request.getParameterValues("dataSovrainnesto");
  String parametroAccessoIdoneita = (String)request.getAttribute("parametroAccessoIdoneita");
  String parametroAccessoVarArea = (String)request.getAttribute("parametroAccessoVarArea");
  String parametroAccessoDtSovr = (String)request.getAttribute("parametroAccessoDtSovr");
  String parametroAccessoAltriDati = (String)request.getAttribute("parametroAccessoAltriDati");
  Vector<Long> vPraticheIdParticella = (Vector<Long>)request.getAttribute("vPraticheIdParticella");
  Vector<Long> vIdStoricoUvModVITI = (Vector<Long>)request.getAttribute("vIdStoricoUvModVITI");
  //CompensazioneAziendaVO compensazioneAziendaVO = (CompensazioneAziendaVO)request.getAttribute("compensazioneAziendaVO");
  
  
  
  
	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
  
  
  
   String htmlStringKOToll = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
   String imkoToll = "ko.gif";
   String imokToll = "ok.gif";
  
  
	StringProcessor jssp = new JavaScriptStringProcessor();
	
	
	htmpl.newBlock("blkDati");
	if(ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteProvinciale()) 
	{
		htmpl.set("blkDati.valuePulsante", "conferma e valida");
		htmpl.newBlock("blkDati.blkConfermaPA");
	}
	else 
	{
		htmpl.set("blkDati.valuePulsante", "conferma");
	}
	for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
  {
  
		htmpl.newBlock("blkDati.blkElenco");
		
		
    
    StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
    StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();   
    
    
    if(Validator.isNotEmpty(vPraticheIdParticella))
    {
      if(vPraticheIdParticella.contains(storicoParticellaVO.getIdParticella()))
      {
        htmpl.set("blkDati.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
          new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT+"'",
          AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT}), null);
      }
    }
    
    
    if(Validator.isNotEmpty(vIdStoricoUvModVITI))
    {
      if(vIdStoricoUvModVITI.contains(storicoUnitaArboreaVO.getIdStoricoUnitaArborea()))
      {
        htmpl.set("blkDati.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
          new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA_JAVASCRIPT+"'",
          AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA}), null);
      }
    }
    
    
    //TOLLERANZA
    /*if(Validator.isNotEmpty(compensazioneAziendaVO)
      && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
    {
      //non metto la tolleranza se è stato fatto il consolidamento
    }
    else
    {
      if(i == 0) 
      {
        htmpl.newBlock("blkDati.blkEtichettaTolleranza");
      }
      htmpl.newBlock("blkDati.blkElenco.blkTolleranza");
    
	    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTolleranza()))
	    { 
	      if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
	      {     
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imokToll, SolmrConstants.UV_TOLLERANZA_OK, ""}), null);
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_TOLLERANZA_FUORI_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.ISO_TOLLERANZA_PLSQL_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UV_NON_PRESENTE) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_NON_PRESENTE_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0) 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO, ""}), null);              
	      }
	      else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0) 
        {
          htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO, ""}), null);              
        }
	      else 
	      {
	        htmpl.set("blkDati.blkElenco.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKOToll, new Object[] { pathErrori + "/"+ imkoToll, SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO, ""}), null);              
	      }
	    }
	  }*/
    
    
    
    //Ci sono UV validate e il ruolo è intermediario!!!    
    if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
    {
      
    
      //Nn si possono modificare i dati dell'idoneità
      if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE))
        || storicoUnitaArboreaVO.isBloccaModificaIdoneitaValida())
      {
        htmpl.set("blkDati.blkElenco.disabledIdoneita", "disabled=\"disabled\"",null);
        htmpl.newBlock("blkDati.blkElenco.blkIdoneitaDisabled");  
      }
      
      //Nn si possono modificare i dati della varietà e l'area
      if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        htmpl.set("blkDati.blkElenco.disabledVarieta", "disabled=\"disabled\"",null);
        htmpl.newBlock("blkDati.blkElenco.blkVarietaDisabled");   
      }
      
      //Nn si possono modificare la data sovrainnesto
      if(parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        htmpl.set("blkDati.blkElenco.disabledDataSovr", "disabled=\"disabled\"",null);
        htmpl.newBlock("blkDati.blkElenco.blkDataSovrDisabled");   
      }
      
      //Nn si possono modificare i dati che nn fanno parte dei parametri precedenti
      if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        htmpl.set("blkDati.blkElenco.disabledAltriDati", "disabled=\"disabled\"",null);
        htmpl.newBlock("blkDati.blkElenco.blkAltriDatiDisabled");   
      }      
    }
     
    
    
    
    
		htmpl.set("blkDati.blkElenco.numero", String.valueOf(i));
		htmpl.set("blkDati.blkElenco.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
		htmpl.set("blkDati.blkElenco.countArea", String.valueOf(i));
		htmpl.set("blkDati.blkElenco.countSestoSuFila", String.valueOf(i));
		htmpl.set("blkDati.blkElenco.countSestoTraFile", String.valueOf(i));
		htmpl.set("blkDati.blkElenco.countNumeroCeppi", String.valueOf(i));
		// COMUNE
		htmpl.set("blkDati.blkElenco.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("blkDati.blkElenco.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
		// SEZIONE
		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
			htmpl.set("blkDati.blkElenco.sezione", storicoParticellaVO.getSezione());
		}
		// FOGLIO
		htmpl.set("blkDati.blkElenco.foglio", storicoParticellaVO.getFoglio());
		// PARTICELLA
		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
			htmpl.set("blkDati.blkElenco.particella", storicoParticellaVO.getParticella());
		}
		// SUBALTERNO
		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
			htmpl.set("blkDati.blkElenco.subalterno", storicoParticellaVO.getSubalterno());
		}
		// SUPERFICIE CATASTALE
		htmpl.set("blkDati.blkElenco.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
		//SUPERFICIE ELEGGIBILE
    if(storicoParticellaVO.getParticellaCertificataVO() != null)
    {
      ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
      if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
        && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
      {
        //Per la query è popolato solo il primo elemento
        ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
        if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
          htmpl.set("blkDati.blkElenco.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
        }
        else 
        {
          htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
        }
      }
      else
      {
        htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
      }
    }
    
    // DESTINAZIONE_PRODUTTIVA
		if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
    {
			for(int c = 0; c < elencoTipiUsoSuolo.length; c++) {
				TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo[c];
				htmpl.newBlock("blkDati.blkElenco.blkTipiUsoSuolo");
				htmpl.set("blkDati.blkElenco.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
				htmpl.set("blkDati.blkElenco.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
				if(storicoUnitaArboreaVO.getIdUtilizzo() != null && storicoUnitaArboreaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
					htmpl.set("blkDati.blkElenco.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
				}
			}
		}
		
		if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
    {
	    if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
	       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
	           && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getIdUtilizzo() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkVarietaDisabled.idTipoUtilizzo", ""+storicoUnitaArboreaVO.getIdUtilizzo());
	      }      
	    }
	    
	    if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
         || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
             && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        if(storicoUnitaArboreaVO.getIdTipoDestinazione() != null)
        {
          htmpl.set("blkDati.blkElenco.blkVarietaDisabled.idTipoDestinazione", ""+storicoUnitaArboreaVO.getIdTipoDestinazione());
        }      
      }
      
      if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
         || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
             && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        if(storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null)
        {
          htmpl.set("blkDati.blkElenco.blkVarietaDisabled.idTipoDettaglioUso", ""+storicoUnitaArboreaVO.getIdTipoDettaglioUso());
        }      
      }
      
      if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
         || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
             && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
      {
        if(storicoUnitaArboreaVO.getIdTipoQualitaUso() != null)
        {
          htmpl.set("blkDati.blkElenco.blkVarietaDisabled.idTipoQualitaUso", ""+storicoUnitaArboreaVO.getIdTipoQualitaUso());
        }      
      }
	    
	    if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getIdVarieta() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkVarietaDisabled.idVarieta", ""+storicoUnitaArboreaVO.getIdVarieta());
	      }      
	    }
	    
	    if(parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getArea() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkVarietaDisabled.area", ""+StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getAnnoRiferimento() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.annoRiferimento", ""+storicoUnitaArboreaVO.getAnnoRiferimento());
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getDataImpianto() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.dataImpianto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataImpianto()));
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.dataPrimaProduzione", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataPrimaProduzione()));
	      }      
	    }
	    
	    if(parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getDataSovrainnesto() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkDataSovrDisabled.dataSovrainnesto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataSovrainnesto()));
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getSestoSuFila() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getSestoTraFile() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getNumCeppi() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
	      }      
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getPercentualeFallanza() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.percentualeFallanza", Formatter.formatDouble(
	          storicoUnitaArboreaVO.getPercentualeFallanza()));
	      }
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      //htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.count",String.valueOf(i));
	      if(SolmrConstants.FLAG_S.equalsIgnoreCase(storicoUnitaArboreaVO.getFlagImproduttiva())) 
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.valFlagImproduttiva", "S");
	      }
	      else {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.valFlagImproduttiva", "N");
	      }      
	    }
	    
	    if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE))
       || storicoUnitaArboreaVO.isBloccaModificaIdoneitaValida())
	    {
	      if((storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null))
	      {
	        htmpl.set("blkDati.blkElenco.blkIdoneitaDisabled.idTipologiaVino",
	          ""+storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getIdTipologiaVino());
	      }
	    }
	    
	    if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE))
       || storicoUnitaArboreaVO.isBloccaModificaIdoneitaValida())
	    {
	      if(storicoUnitaArboreaVO.getAnnoIscrizioneAlbo() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkIdoneitaDisabled.annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
	      }
	    }
	    
	    if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE))
       || storicoUnitaArboreaVO.isBloccaModificaIdoneitaValida())
	    {
	      if(storicoUnitaArboreaVO.getMatricolaCCIAA() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkIdoneitaDisabled.matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
	      }
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.idFormaAllevamento",""+storicoUnitaArboreaVO.getIdFormaAllevamento());
	      }
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      if(storicoUnitaArboreaVO.getPercentualeVarieta() != null)
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.percentualeVitigno",""+storicoUnitaArboreaVO.getPercentualeVarieta());
	      }
	    }
	    
	    if(parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
       || (SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())
           && parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)))
	    {
	      htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.count",String.valueOf(i));
	      if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	      {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.valAltroVitigno", "S");
	      }
	      else {
	        htmpl.set("blkDati.blkElenco.blkAltriDatiDisabled.valAltroVitigno", "N");
	      }      
	    }
		    
	  }
	  
	  // Carico la combo Destinazione solo se l'utente ha selezionato il tipo
    // uso suolo corrispondente
    if(elencoDestinazioni != null && elencoDestinazioni.size() > 0 
      && storicoUnitaArboreaVO.getIdUtilizzo() != null) 
    {
      Enumeration<Integer> enumeraDestinazione = elencoDestinazioni.keys();
      while(enumeraDestinazione.hasMoreElements()) 
      {
        Integer key = (Integer)enumeraDestinazione.nextElement();
        if(key.compareTo(new Integer(i)) == 0) 
        {
          if(Validator.isNotEmpty(elencoDestinazioni.get(key))) 
          {
            Vector<TipoDestinazioneVO> vTipoDestinazione = elencoDestinazioni.get(key);
            for(int d = 0; d < vTipoDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(d);
              htmpl.newBlock("blkDati.blkElenco.blkTipiDestinazione");
              htmpl.set("blkDati.blkElenco.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              htmpl.set("blkDati.blkElenco.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
              if(storicoUnitaArboreaVO.getIdTipoDestinazione() != null) 
              {
                if(storicoUnitaArboreaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
                {
                  htmpl.set("blkDati.blkElenco.blkTipiDestinazione.selected", "selected=\"selected\"", null);
                }
              }
            }
          }
        }
      }
    }
    
    if(elencoDettaglioUso != null && elencoDettaglioUso.size() > 0 
      && storicoUnitaArboreaVO.getIdUtilizzo() != null
      && storicoUnitaArboreaVO.getIdTipoDestinazione() != null) 
    {
      Enumeration<Integer> enumeraDettaglioUso = elencoDettaglioUso.keys();
      while(enumeraDettaglioUso.hasMoreElements()) 
      {
        Integer key = (Integer)enumeraDettaglioUso.nextElement();
        if(key.compareTo(new Integer(i)) == 0) 
        {
          if(Validator.isNotEmpty(elencoDettaglioUso.get(key))) 
          {
            Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = elencoDettaglioUso.get(key);
            for(int d = 0; d < vTipoDettaglioUso.size(); d++) 
            {
              TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(d);
              htmpl.newBlock("blkDati.blkElenco.blkTipiDettaglioUso");
              htmpl.set("blkDati.blkElenco.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
              htmpl.set("blkDati.blkElenco.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
              if(storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null) 
              {
                if(storicoUnitaArboreaVO.getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
                {
                  htmpl.set("blkDati.blkElenco.blkTipiDettaglioUso.selected", "selected=\"selected\"", null);
                }
              }
            }
          }
        }
      }
    }
    
    if(elencoQualitaUso != null && elencoQualitaUso.size() > 0 
      && storicoUnitaArboreaVO.getIdUtilizzo() != null
      && storicoUnitaArboreaVO.getIdTipoDestinazione() != null
      && storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null) 
    {
      Enumeration<Integer> enumeraQualitaUso = elencoQualitaUso.keys();
      while(enumeraQualitaUso.hasMoreElements()) 
      {
        Integer key = (Integer)enumeraQualitaUso.nextElement();
        if(key.compareTo(new Integer(i)) == 0) 
        {
          if(Validator.isNotEmpty(elencoQualitaUso.get(key))) 
          {
            Vector<TipoQualitaUsoVO> vTipoQualitaUso = elencoQualitaUso.get(key);
            for(int d = 0; d < vTipoQualitaUso.size(); d++) 
            {
              TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(d);
              htmpl.newBlock("blkDati.blkElenco.blkTipiQualitaUso");
              htmpl.set("blkDati.blkElenco.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
              htmpl.set("blkDati.blkElenco.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+tipoQualitaUsoVO.getDescrizioneQualitaUso());
              if(storicoUnitaArboreaVO.getIdTipoQualitaUso() != null) 
              {
                if(storicoUnitaArboreaVO.getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
                {
                  htmpl.set("blkDati.blkElenco.blkTipiQualitaUso.selected", "selected=\"selected\"", null);
                }
              }
            }
          }
        }
      }
    }
    
		// Carico la combo VITIGNO solo se l'utente ha selezionato il tipo
		// uso suolo corrispondente
		if(elencoVarieta != null && elencoVarieta.size() > 0 
      && storicoUnitaArboreaVO.getIdUtilizzo() != null
      && storicoUnitaArboreaVO.getIdTipoDestinazione() != null
      && storicoUnitaArboreaVO.getIdTipoDettaglioUso() != null
      && storicoUnitaArboreaVO.getIdTipoQualitaUso() != null)
    {
			Enumeration<Integer> enumeraVarieta = elencoVarieta.keys();
			while(enumeraVarieta.hasMoreElements()) 
      {
				Integer key = (Integer)enumeraVarieta.nextElement();
				if(key.compareTo(new Integer(i)) == 0) 
        {
					if(Validator.isNotEmpty(elencoVarieta.get(key))) 
          {
						Vector<TipoVarietaVO> tipoVarieta = elencoVarieta.get(key);
						for(int d = 0; d < tipoVarieta.size(); d++) 
            {
							TipoVarietaVO tipoVarietaVO = tipoVarieta.get(d);
							htmpl.newBlock("blkDati.blkElenco.blkTipiVarieta");
							htmpl.set("blkDati.blkElenco.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
							htmpl.set("blkDati.blkElenco.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
							if(storicoUnitaArboreaVO.getIdVarieta() != null) 
              {
								if(storicoUnitaArboreaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
                {
									htmpl.set("blkDati.blkElenco.blkTipiVarieta.selected", "selected=\"selected\"", null);
								}
							}
						}
					}
					else 
          {
						htmpl.newBlock("blkDati.blkElenco.blkVarietaPrimariaHidden");
					}
				}
			}
		}
		else 
		{
			htmpl.newBlock("blkDati.blkElenco.blkVarietaPrimariaHidden");
		}
    
    
		// SUPERFICIE VITATA
		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
			htmpl.set("blkDati.blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));				
		}
    
		// ANNO RIFERIMENTO
		htmpl.set("blkDati.blkElenco.annoRiferimento", storicoUnitaArboreaVO.getAnnoRiferimento());
    
		// DATA IMPIANTO
    if(elencoDataImpianto !=null)
    {
      htmpl.set("blkDati.blkElenco.dataImpianto",elencoDataImpianto[i]);
    }
    else
    {
      htmpl.set("blkDati.blkElenco.dataImpianto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataImpianto()));
    }
    
    // DATA PRIMA PRODUZIONE
    if(elencoDataPrimaProduzione !=null)
    {
      htmpl.set("blkDati.blkElenco.dataPrimaProduzione",elencoDataPrimaProduzione[i]);
    }
    else
    {
      htmpl.set("blkDati.blkElenco.dataPrimaProduzione", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataPrimaProduzione()));
    }
    
    // DATA SOVRAINNESTO
    if(elencoDataSovrainnesto !=null)
    {
      htmpl.set("blkDati.blkElenco.dataSovrainnesto", elencoDataSovrainnesto[i]);
    }
    else
    {
      htmpl.set("blkDati.blkElenco.dataSovrainnesto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataSovrainnesto()));
    }
    
		// SESTO SU FILA
		htmpl.set("blkDati.blkElenco.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
    
		// SESTO TRA FILE
		htmpl.set("blkDati.blkElenco.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
    
		// NUMERO CEPPI
		htmpl.set("blkDati.blkElenco.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
    
    // PERCENTUALE FALLANZA
    //a regime metto ilvalore imputtato
    if(request.getParameter("regimeUnitaArboreeModifica") !=null)
    {
      htmpl.set("blkDati.blkElenco.percentualeFallanza", request.getParameterValues("percentualeFallanza")[i]);
    }
    else
    {
      htmpl.set("blkDati.blkElenco.percentualeFallanza", Formatter.formatDouble(storicoUnitaArboreaVO.getPercentualeFallanza()));
    }
    
    // FLAG IMPRODUTTIVA
    //htmpl.set("blkDati.blkElenco.count", String.valueOf(i));
    if(SolmrConstants.FLAG_S.equalsIgnoreCase(storicoUnitaArboreaVO.getFlagImproduttiva())) 
    {
      htmpl.set("blkDati.blkElenco.checkedImproduttivaS", "checked=\"checked\"");
    }
    else {
      htmpl.set("blkDati.blkElenco.checkedImproduttivaN", "checked=\"checked\"");
    }
    
    
       
    // Carico la combo Tipologia Vino solo se l'utente ha selezionato la varietà
    // corrispondente
    if(elencoTipoTipologiaVino != null && elencoTipoTipologiaVino.size() > 0 
      && storicoUnitaArboreaVO.getIdVarieta() != null) 
    {
      
      Enumeration<Integer> enumeraTipoTipologiaVino = elencoTipoTipologiaVino.keys();
      while(enumeraTipoTipologiaVino.hasMoreElements()) 
      {
        Integer key = (Integer)enumeraTipoTipologiaVino.nextElement();
        if(key.compareTo(new Integer(i)) == 0) 
        {
          if(Validator.isNotEmpty(elencoTipoTipologiaVino.get(key))) 
          {
            Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = elencoTipoTipologiaVino.get(key);
            for(int d = 0; d < vTipoTipologiaVino.size(); d++) 
            {
              TipoTipologiaVinoVO tipoTipologiaVinoVO = vTipoTipologiaVino.get(d);
              htmpl.newBlock("blkDati.blkElenco.blkTipologiaVino");
              htmpl.set("blkDati.blkElenco.blkTipologiaVino.idTipologiaVino", ""+tipoTipologiaVinoVO.getIdTipologiaVino().longValue());
              htmpl.set("blkDati.blkElenco.blkTipologiaVino.descrizione", tipoTipologiaVinoVO.getDescrizione());
              if((storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null)
                && (storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getIdTipologiaVino().compareTo(tipoTipologiaVinoVO.getIdTipologiaVino()) == 0))
              {
                htmpl.set("blkDati.blkElenco.blkTipologiaVino.selected", "selected=\"selected\"");
              }
            }
          }
          
        }
      }
    }
    
    
    //Anno iscrizione albo
    htmpl.set("blkDati.blkElenco.annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
    
    //Matricola
    htmpl.set("blkDati.blkElenco.matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
    
    
    
		// FORMA ALLEVAMENTO
		if(elencoFormeAllevamento != null && elencoFormeAllevamento.length > 0) {
			for(int e = 0; e < elencoFormeAllevamento.length; e++) {
				TipoFormaAllevamentoVO tipoFormaAllevamentoVO = (TipoFormaAllevamentoVO)elencoFormeAllevamento[e];
				htmpl.newBlock("blkDati.blkElenco.blkTipiFormaAllevamento");
				htmpl.set("blkDati.blkElenco.blkTipiFormaAllevamento.idFormaAllevamento", tipoFormaAllevamentoVO.getIdFormaAllevamento().toString());
				htmpl.set("blkDati.blkElenco.blkTipiFormaAllevamento.descrizione", tipoFormaAllevamentoVO.getDescrizione());
				if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null && storicoUnitaArboreaVO.getIdFormaAllevamento().compareTo(tipoFormaAllevamentoVO.getIdFormaAllevamento()) == 0) {
					htmpl.set("blkDati.blkElenco.blkTipiFormaAllevamento.selected", "selected=\"selected\"");
				}
			}
		}
    
		// PERCENTUALE VITIGNO
		htmpl.set("blkDati.blkElenco.percentualeVitigno", storicoUnitaArboreaVO.getPercentualeVarieta());
    
		// ALTRI VITIGNI
		htmpl.set("blkDati.blkElenco.count", String.valueOf(i));
		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			htmpl.set("blkDati.blkElenco.checkedS", "checked=\"checked\"");
		}
		else {
			htmpl.set("blkDati.blkElenco.checkedN", "checked=\"checked\"");
		}
    
		// CAUSALI MODIFICA
		if(elencoCausaleModifica != null && elencoCausaleModifica.length > 0) {
			for(int f = 0; f < elencoCausaleModifica.length; f++) {
				TipoCausaleModificaVO tipoCausaleModificaVO = (TipoCausaleModificaVO)elencoCausaleModifica[f];
				htmpl.newBlock("blkDati.blkElenco.blkTipiCausaliModifica");
				htmpl.set("blkDati.blkElenco.blkTipiCausaliModifica.idCausaleModifica", tipoCausaleModificaVO.getIdCausaleModifica().toString());
				htmpl.set("blkDati.blkElenco.blkTipiCausaliModifica.descrizione", tipoCausaleModificaVO.getDescrizione());
				if(storicoUnitaArboreaVO.getIdCausaleModifica() != null && storicoUnitaArboreaVO.getIdCausaleModifica().compareTo(tipoCausaleModificaVO.getIdCausaleModifica()) == 0) {
					htmpl.set("blkDati.blkElenco.blkTipiCausaliModifica.selected", "selected=\"selected\"");
				}
			}
		}
		// GESTIONE ERRORI
		if(elencoErrori != null && elencoErrori.size() > 0) {
			ValidationErrors errors = (ValidationErrors)elencoErrori.elementAt(i);
			if(errors != null && errors.size() > 0) {
				Iterator<String> iter = htmpl.getVariableIterator();
	 			while(iter.hasNext()) {
	 				String key = (String)iter.next();
	 			    if(key.startsWith("err_")) {
	 			    	String property = key.substring(4);
	 			    	Iterator<ValidationError> errorIterator = errors.get(property);
	 			    	if(errorIterator != null) {
	 			    		ValidationError error = (ValidationError)errorIterator.next();
	 			    		htmpl.set("blkDati.blkElenco.err_"+property,
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
		if(elencoErroriAltriVitigni != null && elencoErroriAltriVitigni.size() > 0) {
			ValidationErrors errorsAltriVitigni = (ValidationErrors)elencoErroriAltriVitigni.get(new Integer(i));
			if(errorsAltriVitigni != null && errorsAltriVitigni.size() > 0) {
			 	Iterator<String> iter = htmpl.getVariableIterator();
			 	while(iter.hasNext()) {
			 		String chiave = (String)iter.next();
			 		if(chiave.startsWith("err_")) {
			 		    String property = chiave.substring(4);
			 		    Iterator<ValidationError> errorIterator = errorsAltriVitigni.get(property);
			 			if(errorIterator != null) {
			 			   	ValidationError error = (ValidationError)errorIterator.next();
			 			   	htmpl.set("blkElencoDati.blkElenco.err_"+property,
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
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
	

%>
<%= htmpl.text()%>

