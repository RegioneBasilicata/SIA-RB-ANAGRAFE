<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.math.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareInserisciCondUso.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
 	ConduzioneParticellaVO conduzioneParticellaVO = null;
 	
 	if(storicoParticellaVO != null) {
    conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
  }
 	
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String provvisoria = (String)request.getAttribute("provvisoria");
 	Long idEvento = (Long)request.getAttribute("idEvento");
 	if(Validator.isEmpty(idEvento))
 	  idEvento = (Long)session.getAttribute("idEventoSes");
 	  
 	Vector<CasoParticolareVO> elencoCasiParticolari = (Vector<CasoParticolareVO>)request.getAttribute("elencoCasiParticolari");
 	TipoIrrigazioneVO[] elencoIrrigazione = (TipoIrrigazioneVO[])request.getAttribute("elencoIrrigazione");
  TipoRotazioneColturaleVO[] elencoRotazioneColturale = (TipoRotazioneColturaleVO[])request.getAttribute("elencoRotazioneColturale");
  TipoTerrazzamentoVO[] elencoTerrazzamenti = (TipoTerrazzamentoVO[])request.getAttribute("elencoTerrazzamenti");
  it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTitoliPossesso");
 	Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = (Vector<TipoMetodoIrriguoVO>)request.getAttribute("vMetodoIrriguo");
 	Vector<TipoAreaVO> vTipoArea = (Vector<TipoAreaVO>)request.getAttribute("vTipoArea");
 	//Vector<TipoAreaVO> vTipoAreaValori = (Vector<TipoAreaVO>)request.getAttribute("vTipoAreaValori");
 	
 	
 	Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = (Vector<TipoFaseAllevamentoVO>)request.getAttribute("vTipoFaseAllev");
 	Vector<TipoPiantaConsociataVO> elencoPianteConsociate = (Vector<TipoPiantaConsociataVO>)request.getAttribute("elencoPianteConsociate");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuolo");
 	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazione");
 	Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUso");
 	Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUso");
 	Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarieta");
 	Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSemina");
 	Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = (Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>)request.getAttribute("elencoPerMantenim");
 	Hashtable<Integer,String> elencoFaseAllev = (Hashtable<Integer,String>)request.getAttribute("elencoFaseAllev");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloSecondario");
 	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioneSecondario");
 	Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUsoSecondario");
 	Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUsoSecondario");
 	Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarietaSecondaria");
 	Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSeminaSecondario");
 	TipoImpiantoVO[] elencoTipoImpianto = (TipoImpiantoVO[])request.getAttribute("elencoTipoImpianto");
 	Vector<TipoSeminaVO> vTipoSemina = (Vector<TipoSeminaVO>)request.getAttribute("vTipoSemina");
 	Hashtable<Integer,ValidationErrors> erroriUtilizzi = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriUtilizzi");
 	Hashtable<Integer,ValidationErrors> erroriUtilizziEfa = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriUtilizziEfa");
 	Hashtable<Integer,ValidationErrors> erroriUv = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriUv");
 	Hashtable<Integer,ValidationErrors> erroriPianteConsociate = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriPianteConsociate");
 	String confermaSupGlobale = (String)request.getAttribute("confermaSupGlobale");
  String confermaUsoSuolo = (String)request.getAttribute("confermaUsoSuolo");
  String isPiemontese = (String)request.getAttribute("isPiemontese");
  
  String regimeParticellareInserisciCondUso = request.getParameter("regimeParticellareInserisciCondUso");
  Vector<TipoEfaVO> vTipoEfa = (Vector<TipoEfaVO>)request.getAttribute("vTipoEfa");
  Hashtable<Integer, Vector<TipoUtilizzoVO>> elencoUtilizziEfa = (Hashtable<Integer, Vector<TipoUtilizzoVO>>)request.getAttribute("elencoUtilizziEfa");
  Hashtable<Integer, Vector<TipoDestinazioneVO>> elencoDestinazioneEfa = (Hashtable<Integer, Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioneEfa");
  Hashtable<Integer, Vector<TipoDettaglioUsoVO>> elencoDettaglioUsoEfa = (Hashtable<Integer, Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettaglioUsoEfa");
  Hashtable<Integer, Vector<TipoQualitaUsoVO>> elencoQualitaUsoEfa = (Hashtable<Integer, Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUsoEfa");
  Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarietaEfa = (Hashtable<Integer, Vector<TipoVarietaVO>>)request.getAttribute("elencoVarietaEfa");
  
  String[] arrIdTipoEfa = request.getParameterValues("idTipoEfa");  
  String[] elencoUtilizziEfaSelezionati = request.getParameterValues("idTipoUtilizzoEfa");
  String[] elencoDestinazioneEfaSelezionate = request.getParameterValues("idTipoDeestinazioneEfa");
  String[] elencoDetUsoEfaSelezionate = request.getParameterValues("idTipoDettaglioUsoEfa");
  String[] elencoQualUsoEfaSelezionate = request.getParameterValues("idTipoQualitaUsoEfa");
  String[] elencoVarietaEfaSelezionate = request.getParameterValues("idVarietaEfa");
  String[] elencoAbbPonderazioneVarieta = request.getParameterValues("abbPonderazioneVarieta");
  String[] elencoValoreOriginale = request.getParameterValues("valoreOriginale");


  String[] arrVIdStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
  if(Validator.isEmpty(arrVIdStoricoUnitaArborea))
  {
    arrVIdStoricoUnitaArborea = (String[])session.getAttribute("arrVIdStoricoUnitaArborea");
  }
  
  
  String[] arrVAreaUV = request.getParameterValues("area");
  if(Validator.isEmpty(arrVAreaUV))
  {
    arrVAreaUV = (String[])session.getAttribute("arrVAreaUV");
  }
  
  String parametroInserisciNuovaPart = (String)request.getAttribute("parametroInserisciNuovaPart");
  
  ParticellaCertificataVO particellaCertificataVO = (ParticellaCertificataVO)request.getAttribute("particellaCertificataVO");
  Vector<RegistroPascoloVO> vRegistroPascoloVO = (Vector<RegistroPascoloVO>)request.getAttribute("vRegistroPascoloVO");
  
  
  Vector<StoricoParticellaVO> vStoricoParticellaVO = (Vector<StoricoParticellaVO>)request.getAttribute("vStoricoParticellaVO");  
  
  String flagObbligoCatastale = (String)session.getAttribute("flagObbligoCatastale");  
  
 	
 	htmpl.set("provvisoria", provvisoria);
 	if(idEvento != null) {
 		htmpl.set("idEvento", idEvento.toString());
 	}
  if(Validator.isNotEmpty(confermaSupGlobale)) 
  {
    htmpl.set("confermaSupGlobale", confermaSupGlobale);
    htmpl.set("msgConfermaSupGlobale", (String)request.getAttribute("msgConfermaSupGlobale"), null);
  }
  if(Validator.isNotEmpty(confermaUsoSuolo)) {
    htmpl.set("confermaUsoSuolo", confermaUsoSuolo);
  }
  
  if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO())
     && Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
     && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
     && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
     && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
  {
    htmpl.set("stabilizzato", "true");
  }
 	
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
  
  
  
  if(Validator.isNotEmpty(vStoricoParticellaVO))
  {
    htmpl.newBlock("blkUVImportabili");
    for(int i=0;i<vStoricoParticellaVO.size();i++)
    {
      StoricoParticellaVO storicoParticellaVOTmp = vStoricoParticellaVO.get(i);
      StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVOTmp.getStoricoUnitaArboreaVO();
      htmpl.newBlock("blkUVImportabili.blkElencoUVImportabili");
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.idStoricoUnitaArborea",
        ""+storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
          
      if(Validator.isEmpty(storicoUnitaArboreaVO.getDataCessazione())
        && Validator.isNotEmpty(storicoUnitaArboreaVO.getDataConsolidamentoGis())
        && "N".equalsIgnoreCase(parametroInserisciNuovaPart))
      {
        htmpl.set("blkUVImportabili.blkElencoUVImportabili.disabledUV",
              "disabled=\"true\"", null);
        htmpl.set("blkUVImportabili.blkElencoUVImportabili.storicoUVChecked",
              "checked=\"true\"", null);
        htmpl.newBlock("blkUVImportabili.blkElencoUVImportabili.blkHiddenUv");
        htmpl.set("blkUVImportabili.blkElencoUVImportabili.blkHiddenUv.idStoricoUnitaArborea",
              ""+storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
        
      }
      else
      { 
	      if(Validator.isNotEmpty(arrVIdStoricoUnitaArborea))
	      {
	        for(int j=0;j<arrVIdStoricoUnitaArborea.length;j++)
	        {
	          if(storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString().equalsIgnoreCase(arrVIdStoricoUnitaArborea[j]))
	          {
	            htmpl.set("blkUVImportabili.blkElencoUVImportabili.storicoUVChecked",
	              "checked=\"true\"", null);
	            break;
	          }
	        }
	      }
	    }
      
      
        
        
      ComuneVO comuneVO = storicoParticellaVOTmp.getComuneParticellaVO();
      String comune = comuneVO.getDescom() +" ("+comuneVO.getSiglaProv()+")";
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.descComune", comune);
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.sezione", storicoParticellaVOTmp.getSezione());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.foglio", storicoParticellaVOTmp.getFoglio());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.particella", storicoParticellaVOTmp.getParticella());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.subalterno", storicoParticellaVOTmp.getSubalterno());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.progressivo", storicoUnitaArboreaVO.getProgrUnar());
      
      
      if(Validator.isNotEmpty(idEvento) 
        && (idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0))
      {
        htmpl.newBlock("blkUVImportabili.blkElencoUVImportabili.blkModArea");
        if(Validator.isNotEmpty(arrVAreaUV)  && Validator.isNotEmpty(arrVAreaUV[i]))
        {
          htmpl.set("blkUVImportabili.blkElencoUVImportabili.blkModArea.area", Formatter.formatDouble4(arrVAreaUV[i]));
        
          if(erroriUv != null && erroriUv.size() > 0) 
          {
		        ValidationErrors errorsUv = (ValidationErrors)erroriUv.get(new Integer(i));
		        if(errorsUv != null && errorsUv.size() > 0) {
		          Iterator iter = htmpl.getVariableIterator();
		          while(iter.hasNext()) {
		            String chiave = (String)iter.next();
		            if(chiave.startsWith("err_")) {
		                String property = chiave.substring(4);
		                Iterator errorIterator = errorsUv.get(property);
		                if(errorIterator != null) {
		                  ValidationError error = (ValidationError)errorIterator.next();
		                  htmpl.set("blkUVImportabili.blkElencoUVImportabili.blkModArea.err_"+property,
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
        else
        {
          htmpl.set("blkUVImportabili.blkElencoUVImportabili.blkModArea.area", Formatter.formatDouble4(storicoUnitaArboreaVO.getArea()));
        }
      }
      else
      { 
        htmpl.newBlock("blkUVImportabili.blkElencoUVImportabili.blkLetArea");
        htmpl.set("blkUVImportabili.blkElencoUVImportabili.blkLetArea.area", Formatter.formatDouble4(storicoUnitaArboreaVO.getArea()));
      }
      String vitigno = "";
      if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdVarieta()))
      {
        vitigno = "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "
          +storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione();
      }
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.vitigno", vitigno);
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.cuaa", storicoUnitaArboreaVO.getCuaa());
      htmpl.set("blkUVImportabili.blkElencoUVImportabili.denominazione", storicoUnitaArboreaVO.getDenominazione());
    
    
      
    
    
    
    }
    
  }
  
  
  
  
  // Dati relativi alla particella certificata
  if(Validator.isNotEmpty(particellaCertificataVO))
  {
    if(particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca()) 
    {
      
      //Nuova eleggibilita inizio
      if(particellaCertificataVO.getIdFonteElegg() != null) {
        String descrizioneFonte = particellaCertificataVO.getFonteDatoElegg().getDescription();
        if(particellaCertificataVO.getDataValidazioneFonteElegg() != null) {
          descrizioneFonte += " del " + DateUtils.formatDate(particellaCertificataVO.getDataValidazioneFonteElegg());
        }
        htmpl.set("descFonteElegg", " - Fonte "+descrizioneFonte);
      }
      
      if(particellaCertificataVO.getVParticellaCertEleg() !=null)
      {
        int size = particellaCertificataVO.getVParticellaCertEleg().size();
        int level = 0;
        for(int i=0;i<size;i++)
        {
          ParticellaCertElegVO partVO = (ParticellaCertElegVO)
              particellaCertificataVO.getVParticellaCertEleg().get(i);
          if(i % 2 == 0)
          {
            htmpl.newBlock("blkDatiEleggibilita");
            level=0;
          }
           
          String desc = partVO.getDescrizione();
          desc = desc.substring(0,1).toUpperCase() + desc.substring(1,desc.length()).toLowerCase();         
          htmpl.set("blkDatiEleggibilita.descEleggibilita"+level, desc);
          if(partVO.getDescrizioneFit() !=null)
          {
            String descFit = partVO.getDescrizioneFit();
            descFit = descFit.substring(0,1).toUpperCase() + descFit.substring(1,descFit.length()).toLowerCase();   
            htmpl.set("blkDatiEleggibilita.descEleggibilitaFit"+level, " - "+descFit);
          }
          
          htmpl.set("blkDatiEleggibilita.supEleggibilita"+level, Formatter.formatDouble4(partVO.getSuperficie()));
          level++;
        } 
      }
      
      //Nuova eleggibilita ********** fine
    }
    else 
    {
      
      if(!particellaCertificataVO.isCertificata()) 
      {
        htmpl.set("descFonteElegg", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);       
      }
      else 
      {       
        htmpl.set("descFonteElegg", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      }
    }
  }
  
  
  
  //Registro storico pascoli
  if(vRegistroPascoloVO != null)
  {
    htmpl.newBlock("blkDatiRegistroPascoli");
    for(int i=0;i<vRegistroPascoloVO.size();i++)
    {
      htmpl.newBlock("blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli");
      htmpl.set("blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.annoCampagna",
         ""+vRegistroPascoloVO.get(i).getAnnoCampagna());
      htmpl.set("blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.descFonte",
         vRegistroPascoloVO.get(i).getDescFonte());
      htmpl.set("blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.superficie", 
        Formatter.formatDouble4(vRegistroPascoloVO.get(i).getSuperficie()));
    }
     
  }
  else
  {
    htmpl.newBlock("blkNoDatiRegistroPascoli");
  }
  
  
 	
 	// Dati chiave logica della particella:
 	// Visualizzo il GIS solo se la particella risulta censita in archivio e non è stata dichiarata provvisoria dall'utente
 	if(storicoParticellaVO.getIdStoricoParticella() != null 
 	  && Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
 	{
 		htmpl.newBlock("blkAbacoGis");
 		htmpl.set("blkAbacoGis.idStoricoParticella", storicoParticellaVO.getIdStoricoParticella().toString());
 		
 		
 		String titleGis = "GIS";
 		if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
 		{          
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      titleGis += " - foglio stabilizzato.";
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	       && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	         .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      titleGis += "- foglio in corso di stabilizzazione.";
	    }
	  }                 
    htmpl.set("blkAbacoGis.controlliP", titleGis);
    
    String immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS;
    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
    {
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB;
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	          .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
	    }
	  }          
    htmpl.set("blkAbacoGis.immaginiControlliP", immaginiControlliP);
 		
 		
 		
 	}
 	htmpl.set("descComuneUte", conduzioneParticellaVO.getUteVO().getComuneUte().getDescom());
	if(conduzioneParticellaVO.getUteVO().getComuneUte().getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
 		htmpl.set("siglaProvinciaUte", "("+conduzioneParticellaVO.getUteVO().getComuneUte().getProvinciaVO().getSiglaProvincia().toUpperCase()+")");
	}
	htmpl.set("indirizzoUte", " - "+conduzioneParticellaVO.getUteVO().getIndirizzo());
	htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom().toUpperCase());
	if(storicoParticellaVO.getComuneParticellaVO().getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
		htmpl.set("siglaProvinciaParticella", " ("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia().toUpperCase()+")");
	}
	if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
		htmpl.set("sezione", storicoParticellaVO.getSezione().toUpperCase());
	}
	htmpl.set("foglio", storicoParticellaVO.getFoglio());
	htmpl.set("particella", storicoParticellaVO.getParticella());
	if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
		htmpl.set("subalterno", storicoParticellaVO.getSubalterno().toUpperCase());
	}
	// Dati catastali
  htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
  
  
  
  
	if(Validator.isNotEmpty(flagObbligoCatastale)) 
  {
    if(Validator.validateDouble(storicoParticellaVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) != null) {
      htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    }
    else {
      htmpl.set("supCatastale", storicoParticellaVO.getSupCatastale());
    }
	}
	else 
  {
		htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("readSupCatastale", "readOnly=\"readOnly\"");
	}
  
  
  
  
  
	
	if(storicoParticellaVO.getZonaAltimetrica() != null && Validator.isNotEmpty(storicoParticellaVO.getZonaAltimetrica().getDescription())) {
    htmpl.set("descZonaAltimetrica", storicoParticellaVO.getZonaAltimetrica().getDescription());
  }
	for(int l = 0; l < elencoCasiParticolari.size(); l++) 
	{
		CasoParticolareVO casoParticolareVO = elencoCasiParticolari.get(l);
		htmpl.newBlock("blkTipiCasoParticolare");
		htmpl.set("blkTipiCasoParticolare.idCasoParticolare", ""+casoParticolareVO.getIdCasoParticolare());
		htmpl.set("blkTipiCasoParticolare.descrizione", ""+casoParticolareVO.getIdCasoParticolare());
		if(storicoParticellaVO.getIdCasoParticolare() != null) 
		{
			if(storicoParticellaVO.getIdCasoParticolare().compareTo(new Long(casoParticolareVO.getIdCasoParticolare())) == 0) 
			{
				htmpl.set("blkTipiCasoParticolare.selected", "selected=\"selected\"", null);
			}
		}
	}
	if(Validator.isNotEmpty(storicoParticellaVO.getPercentualePendenzaMedia()))
	{
    htmpl.set("descPendenzaMedia", Formatter.formatDouble2(storicoParticellaVO.getPercentualePendenzaMedia())+"%");       
  }
  if(Validator.isNotEmpty(storicoParticellaVO.getMetriAltitudineMedia())) 
  {
    htmpl.set("descAltitudineMedia", Formatter.formatDouble(storicoParticellaVO.getMetriAltitudineMedia())+"m");       
  }
	if(Validator.isNotEmpty(storicoParticellaVO.getFlagCaptazionePozzi()) && storicoParticellaVO.getFlagCaptazionePozzi().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
		htmpl.set("checkedCaptazionePozzi", "checked=\"checked\"");
	}
	
	if(Validator.isNotEmpty(storicoParticellaVO.getFlagIrrigabile()) && storicoParticellaVO.getFlagIrrigabile().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
		htmpl.set("checkedIrrigabile", "checked=\"checked\"");
	}
	for(int i = 0; i < elencoIrrigazione.length; i++) {
		TipoIrrigazioneVO tipoIrrigazioneVO = (TipoIrrigazioneVO)elencoIrrigazione[i];
		htmpl.newBlock("blkTipiIrrigazione");
		htmpl.set("blkTipiIrrigazione.idIrrigazione", tipoIrrigazioneVO.getIdIrrigazione().toString());
		htmpl.set("blkTipiIrrigazione.descrizione", tipoIrrigazioneVO.getDescrizione());
		if(storicoParticellaVO.getIdIrrigazione() != null) {
			if(storicoParticellaVO.getIdIrrigazione().compareTo(tipoIrrigazioneVO.getIdIrrigazione()) == 0) {
				htmpl.set("blkTipiIrrigazione.selected", "selected=\"selected\"", null);
			}
		}
	}
  
  
  for(int i = 0; i < elencoRotazioneColturale.length; i++) 
  {
    TipoRotazioneColturaleVO tipoRotazioneColturaleVO = (TipoRotazioneColturaleVO)elencoRotazioneColturale[i];
    htmpl.newBlock("blkTipiRotazioneColturale");
    htmpl.set("blkTipiRotazioneColturale.idRotazioneColturale", tipoRotazioneColturaleVO.getIdRotazioneColturale().toString());
    htmpl.set("blkTipiRotazioneColturale.descrizione", tipoRotazioneColturaleVO.getDescrizione());
    if(storicoParticellaVO.getIdRotazioneColturale() != null) 
    {
      if(storicoParticellaVO.getIdRotazioneColturale()
        .compareTo(tipoRotazioneColturaleVO.getIdRotazioneColturale()) == 0) 
      {
        htmpl.set("blkTipiRotazioneColturale.selected", "selected=\"selected\"", null);
      }
    }    
  }
  for(int i = 0; i < elencoTerrazzamenti.length; i++) 
  {
    TipoTerrazzamentoVO tipoTerrazzamentoVO = (TipoTerrazzamentoVO)elencoTerrazzamenti[i];
    htmpl.newBlock("blkTipiTerrazzamenti");
    htmpl.set("blkTipiTerrazzamenti.idTerrazzamento", tipoTerrazzamentoVO.getIdTerrazzamento().toString());
    htmpl.set("blkTipiTerrazzamenti.descrizione", tipoTerrazzamentoVO.getDescrizione());
    
    if(storicoParticellaVO.getIdTerrazzamento() != null) 
    {
      if(storicoParticellaVO.getIdTerrazzamento()
        .compareTo(tipoTerrazzamentoVO.getIdTerrazzamento()) == 0) 
      {
        htmpl.set("blkTipiTerrazzamenti.selected", "selected=\"selected\"", null);
      }
    } 
    
  }
  
  if(Validator.isNotEmpty(vMetodoIrriguo))
  {
	  for(int i=0;i<vMetodoIrriguo.size();i++) 
	  {
	    TipoMetodoIrriguoVO tipoMetodoIrriguoVO = vMetodoIrriguo.get(i);
	    htmpl.newBlock("blkTipiMetodoIrriguo");
	    htmpl.set("blkTipiMetodoIrriguo.idMetodoIrriguo", ""+tipoMetodoIrriguoVO.getIdMetodoIrriguo());
	    htmpl.set("blkTipiMetodoIrriguo.descrizione", tipoMetodoIrriguoVO.getDescrizioneMetodoIrriguo());
	    
	    if(Validator.isNotEmpty(storicoParticellaVO.getIdMetodoIrriguo())) 
	    {
	      if(storicoParticellaVO.getIdMetodoIrriguo()
	        .compareTo(new Long(tipoMetodoIrriguoVO.getIdMetodoIrriguo())) == 0) 
	      {
	        htmpl.set("blkTipiMetodoIrriguo.selected", "selected=\"selected\"", null);
	      }
	    } 	    
	  }
	}
	
	String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
	String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
	int numValore = 0;
	if(Validator.isNotEmpty(vTipoArea))
  {
    for(int i=0;i<vTipoArea.size();i++)
    {
      TipoAreaVO tipoAreaVO = vTipoArea.get(i);
      TipoAreaVO valore = null;
      if(storicoParticellaVO.getvValoriTipoArea() != null)
        valore = storicoParticellaVO.getvValoriTipoArea().get(i); 
        
      boolean comboPiena = false;
      //combo valorizzata se modifcabile
      if(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile()))
      {
        comboPiena = true;
      }
      //o se presnete valore su db
      else if(Validator.isNotEmpty(valore) && Validator.isNotEmpty(valore.getvTipoValoreArea())
        && Validator.isNotEmpty(valore.getvTipoValoreArea().get(0).getValore()))
      {
        comboPiena = true;
      }
      if(i%2 == 0)
      {
        htmpl.newBlock("blkRiga");
        htmpl.set("blkRiga.flagEsclusivoFoglio1", tipoAreaVO.getFlagEsclusivoFoglio());
        htmpl.set("blkRiga.desc1", ""+tipoAreaVO.getDescrizione());
        htmpl.set("blkRiga.idTipoArea1", ""+tipoAreaVO.getIdTipoArea());
        String valore1 = "";
        String valore1First = tipoAreaVO.getvTipoValoreArea().get(0).getValore();
        
        if(comboPiena)
        {
	        for(int j=0;j<tipoAreaVO.getvTipoValoreArea().size();j++)
	        {
	          htmpl.newBlock("blkRiga.blkTipiValoreArea1");
	          TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(j);
	          htmpl.set("blkRiga.blkTipiValoreArea1.descrizione",tipoValoreAreaVO.getDescrizione());
	          htmpl.set("blkRiga.blkTipiValoreArea1.valoreArea1",tipoValoreAreaVO.getValore());
	          if(Validator.isEmpty(regimeParticellareInserisciCondUso))
	          {
		          if((valore != null) && (valore.getvTipoValoreArea() != null)
		            && (valore.getvTipoValoreArea().get(0).getValore() != null))
		          {
		            if(tipoValoreAreaVO.getValore().equalsIgnoreCase(valore.getvTipoValoreArea().get(0).getValore())) 
		            {
		              htmpl.set("blkRiga.blkTipiValoreArea1.selected", "selected=\"selected\"", null);
		              valore1 = tipoValoreAreaVO.getValore();
		            }          
		          }
		        }
		        else
		        {
		          if(Validator.isNotEmpty(arrValoreArea1[numValore]))
		          {
		            if(tipoValoreAreaVO.getValore().equalsIgnoreCase(arrValoreArea1[numValore])) 
	              {
	                htmpl.set("blkRiga.blkTipiValoreArea1.selected", "selected=\"selected\"", null);
	                valore1 = arrValoreArea1[numValore];
	              }  
		          }	        
		        }	          
	        }
	      }
        
        if(!(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile())))
        {
          htmpl.set("blkRiga.disabledValore1", "disabled=\"true\"", null);
          htmpl.newBlock("blkRiga.blkHiddenValoreArea1");
          if(comboPiena)
          {
            if(Validator.isNotEmpty(valore1))
              htmpl.set("blkRiga.blkHiddenValoreArea1.valoreArea1", valore1);
            else
              htmpl.set("blkRiga.blkHiddenValoreArea1.valoreArea1", valore1First);
          }
        }
      }
      else
      {
        htmpl.set("blkRiga.flagEsclusivoFoglio2", tipoAreaVO.getFlagEsclusivoFoglio());
        htmpl.set("blkRiga.desc2", ""+tipoAreaVO.getDescrizione());
        htmpl.set("blkRiga.idTipoArea2", ""+tipoAreaVO.getIdTipoArea());
        String valore2 = "";
        String valore2First = tipoAreaVO.getvTipoValoreArea().get(0).getValore();
        
        if(comboPiena)
        {
	        for(int j=0;j<tipoAreaVO.getvTipoValoreArea().size();j++)
	        {
	          htmpl.newBlock("blkRiga.blkTipiValoreArea2");
	          TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(j);
	          htmpl.set("blkRiga.blkTipiValoreArea2.descrizione",tipoValoreAreaVO.getDescrizione());
	          htmpl.set("blkRiga.blkTipiValoreArea2.valoreArea2",tipoValoreAreaVO.getValore());
	          if(Validator.isEmpty(regimeParticellareInserisciCondUso))
	          {
		          if(Validator.isNotEmpty(valore) && Validator.isNotEmpty(valore.getvTipoValoreArea())
		            && Validator.isNotEmpty(valore.getvTipoValoreArea().get(0).getValore()))
		          {
		            if(tipoValoreAreaVO.getValore().equalsIgnoreCase(valore.getvTipoValoreArea().get(0).getValore())) 
		            {
		              htmpl.set("blkRiga.blkTipiValoreArea2.selected", "selected=\"selected\"", null);
		              valore2 = tipoValoreAreaVO.getValore();
		            }          
		          }
		        }
		        else
		        {
		          if(Validator.isNotEmpty(arrValoreArea2[numValore]))
	            {
	              if(tipoValoreAreaVO.getValore().equalsIgnoreCase(arrValoreArea2[numValore])) 
	              {
	                htmpl.set("blkRiga.blkTipiValoreArea2.selected", "selected=\"selected\"", null);
	                valore2 = arrValoreArea2[numValore];
	              }  
	            }  
		        }
	        }
	      }
        
        if(!(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile())))
        {
          htmpl.set("blkRiga.disabledValore2", "disabled=\"true\"", null);
          htmpl.newBlock("blkRiga.blkHiddenValoreArea2");
          if(comboPiena)
          {
	          if(Validator.isNotEmpty(valore2))
	            htmpl.set("blkRiga.blkHiddenValoreArea2.valoreArea2", valore2);
	          else
	            htmpl.set("blkRiga.blkHiddenValoreArea2.valoreArea2", valore2First);
	        }
        }
        numValore++;
      }      
    }
  
  }
  
  
  
  
  
  if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieCondotta())) 
  {
    htmpl.set("supCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
  }
  
  
  if(Validator.isNotEmpty(conduzioneParticellaVO.getPercentualePossesso())) 
  {
    htmpl.set("percentualePossesso", Formatter.formatDouble2(conduzioneParticellaVO.getPercentualePossesso()));
  }
  
	if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica()) && Validator.validateDouble(conduzioneParticellaVO.getSuperficieAgronomica(), SolmrConstants.FORMAT_SUP_CONDOTTA) != null) {
		htmpl.set("superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
	}
	else {
		htmpl.set("superficieAgronomica", conduzioneParticellaVO.getSuperficieAgronomica());
	}
	for(int i = 0; i < elencoTitoliPossesso.length; i++) {
		it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTitoliPossesso[i];
		htmpl.newBlock("blkTitoliPossesso");
		htmpl.set("blkTitoliPossesso.idTitoloPossesso", code.getCode().toString());
		htmpl.set("blkTitoliPossesso.descrizione","["+ code.getCode().toString()+"]" + " " +code.getDescription());
		if(conduzioneParticellaVO.getIdTitoloPossesso() != null && conduzioneParticellaVO.getIdTitoloPossesso().compareTo(Long.decode(code.getCode().toString())) == 0) {
			htmpl.set("blkTitoliPossesso.selected", "selected=\"selected\"", null);
		}
	}
	// Dati uso del suolo
	// Labels relative ai dati delle piante consociate
	if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0) 
	{
		htmpl.set("colspan", String.valueOf(elencoPianteConsociate.size()));
		for(int i = 0; i < elencoPianteConsociate.size(); i++) 
		{
			TipoPiantaConsociataVO tipoPiantaConsociataVO = (TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(i);
			htmpl.newBlock("blkEtichettaPianteConsociate");
			htmpl.set("blkEtichettaPianteConsociate.descPianteConsociate", tipoPiantaConsociataVO.getDescrizione());
		}
	}
	
	
	//Utilizzi normali   
  Vector<UtilizzoParticellaVO> vUtilizziVO = (Vector<UtilizzoParticellaVO>)request.getAttribute("vUtilizziVO");
  int numeroUtilizzi = 0;
  if(Validator.isNotEmpty(vUtilizziVO)
    && Validator.isEmpty(regimeParticellareInserisciCondUso))
  {
    numeroUtilizzi = vUtilizziVO.size();
  }
  //conto il numero di idUtilizzi presenti
  else
  {
    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")))
      numeroUtilizzi = request.getParameterValues("idTipoUtilizzo").length;
  }
	

	for(int i = 0; i < numeroUtilizzi; i++) 
	{
	  String descrizione = null;
		htmpl.newBlock("blkElencoUtilizzi");
		htmpl.set("blkElencoUtilizzi.numeroUtilizzi", String.valueOf(i));
		htmpl.set("blkElencoUtilizzi.contatore", String.valueOf(i));
		for(int a = 0; a < elencoTipiUsoSuolo.size(); a++) 
		{
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(a);
			htmpl.newBlock("blkElencoUtilizzi.blkTipiUsoSuolo");
			htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
			if(tipoUtilizzoVO.getDescrizione().length() > 20) 
			{
				descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
			}
			else 
			{
				descrizione = tipoUtilizzoVO.getDescrizione();
			}
			htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
			if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
			{
			  if(request.getParameterValues("idTipoUtilizzo") != null 
			    && i < request.getParameterValues("idTipoUtilizzo").length) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]) 
            && Long.decode(request.getParameterValues("idTipoUtilizzo")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
          {
            htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
          }
        }
			}
			else
			{
			  if((vUtilizziVO != null)
			    && (i < vUtilizziVO.size()))
			  {
			    if(vUtilizziVO.get(i).getIdUtilizzo() != null 
			      && vUtilizziVO.get(i).getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
			    {
		        htmpl.set("blkElencoUtilizzi.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		      }
			  
			  }
			}
		}
		
		
		
		
		if(elencoDestinazione != null && elencoDestinazione.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzo") != null)
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazione");
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDestinazione") != null && i < request.getParameterValues("idTipoDestinazione").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i]) 
                  && request.getParameterValues("idTipoDestinazione")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
			    }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
		    }
      }
      else
      {
        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(i);
        if((vDestinazione != null)
          && (vDestinazione.size() > 0))
        {
          for(int d = 0; d < vDestinazione.size(); d++) 
          {
            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazione");
            htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
            descrizione = null;
            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoDestinazione()))
            {
              if(vUtilizziVO.get(i).getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiDestinazione.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
		    }
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazione");
    }
    
    if(elencoDettUso != null && elencoDettUso.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoDestinazione") != null)
          && (request.getParameterValues("idTipoDestinazione")[i] != null))
        {
          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(new Integer(i));
          if(vDettaglioUso != null)
          {
            for(int d = 0; d < vDettaglioUso.size(); d++) 
            {
              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUso");
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
              descrizione = null;
              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDettaglioUso") != null && i < request.getParameterValues("idTipoDettaglioUso").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i]) 
                  && request.getParameterValues("idTipoDettaglioUso")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
			    }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
		    }
      }
      else
      {
        Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(i);
        if((vDettaglioUso != null)
          && (vDettaglioUso.size() > 0))
        {
          for(int d = 0; d < vDettaglioUso.size(); d++) 
          {
            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUso");
            htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
            descrizione = null;
            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoDettaglioUso()))
            {
              if(vUtilizziVO.get(i).getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
		    }
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUso");
    }
    
    
    if(elencoQualitaUso != null && elencoQualitaUso.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoDettaglioUso") != null)
          && (request.getParameterValues("idTipoDettaglioUso")[i] != null))
        {
          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(new Integer(i));
          if(vQualitaUso != null)
          {
            for(int d = 0; d < vQualitaUso.size(); d++) 
            {
              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUso");
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
              descrizione = null;
              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
              }
              else 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoQualitaUso") != null && i < request.getParameterValues("idTipoQualitaUso").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i]) 
                  && request.getParameterValues("idTipoQualitaUso")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
			    }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
		    }
      }
      else
      {
        Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(i);
        if((vQualitaUso != null)
          && (vQualitaUso.size() > 0))
        {
          for(int d = 0; d < vQualitaUso.size(); d++) 
          {
            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUso");
            htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
            descrizione = null;
            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
            {
              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
            }
            else 
            {
              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoQualitaUso()))
            {
              if(vUtilizziVO.get(i).getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiQualitaUso.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
		    }
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUso");
    }
		
		
		
		// Carico la combo della varietà solo se l'utente ha selezionato il tipo
		// uso suolo corrispondente
		if(elencoVarieta != null && elencoVarieta.size() > 0) 
    {  
			if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoQualitaUso") != null)
          && (request.getParameterValues("idTipoQualitaUso")[i] != null))
        {
          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarieta.get(new Integer(i));
          if(tipoVarieta != null)
          {
            for(int l = 0; l < tipoVarieta.size(); l++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiVarieta");
              htmpl.set("blkElencoUtilizzi.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
             
              if(request.getParameterValues("idVarieta") != null && i < request.getParameterValues("idVarieta").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i]) 
                  && request.getParameterValues("idVarieta")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiVarieta.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
			    }   
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
		    }   
      }
      //prima volta che entro nella popup.
      else
      {         
        Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarieta.get(i);
        if(tipoVarieta != null)
        {
          for(int l = 0; l < tipoVarieta.size(); l++) 
          {
            TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiVarieta");
            htmpl.set("blkElencoUtilizzi.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
            if(tipoVarietaVO.getDescrizione().length() > 20) 
            {
              descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoVarietaVO.getDescrizione();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
            
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdVarieta())) 
            {
              if(vUtilizziVO.get(i).getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiVarieta.selected", "selected=\"selected\"", null);
              }
            }             
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
		    }              
      }       
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarieta");
    }    
    
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("supUtilizzata") != null 
        && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[i])) 
      {
        htmpl.set("blkElencoUtilizzi.supUtilizzata", request.getParameterValues("supUtilizzata")[i]);
      }
    }
    else
    {
      if((vUtilizziVO != null) 
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getSuperficieUtilizzata())) 
      {
        htmpl.set("blkElencoUtilizzi.supUtilizzata", StringUtils.parseSuperficieField(vUtilizziVO.get(i).getSuperficieUtilizzata()));
      } 
    }
    
    
    // Carico la combo del perido semina solo se l'utente ha selezionato il tipo
    // uso suolo corrispondente
    if(elencoPerSemina != null && elencoPerSemina.size() > 0) 
    {  
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzo") != null)
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSemina.get(new Integer(i));
          if(vTipoPeriodoSemina != null)
          {
            for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
            {
              TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSemina");
              htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
              htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
             
              if(request.getParameterValues("idTipoPeriodoSemina") != null && i < request.getParameterValues("idTipoPeriodoSemina").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[i]) 
                  && request.getParameterValues("idTipoPeriodoSemina")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
		    } 
      }
      //prima volta che entro nella popup.
      else
      {         
        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSemina.get(i);
        if(vTipoPeriodoSemina != null)
        {
          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
          {
            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
            htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSemina");
            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
            
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoPeriodoSemina())) 
            {
              if(vUtilizziVO.get(i).getIdTipoPeriodoSemina().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
              }
            }             
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
		    }            
      }       
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSemina");
    }   
    
    if(elencoPerMantenim != null && elencoPerMantenim.size() > 0) 
    {  
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzo") != null)
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          Vector<TipoPraticaMantenimentoVO> vTipoPraticMantenim = (Vector<TipoPraticaMantenimentoVO>)elencoPerMantenim.get(new Integer(i));
          if(vTipoPraticMantenim != null)
          {
            for(int l = 0; l < vTipoPraticMantenim.size(); l++) 
            {
              TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPraticMantenim.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipoPraticaMantenimento");
              htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
              htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
             
              if(request.getParameterValues("idPraticaMantenimento") != null && i < request.getParameterValues("idPraticaMantenimento").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idPraticaMantenimento")[i]) 
                  && request.getParameterValues("idPraticaMantenimento")[i].equalsIgnoreCase(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
		    } 
      }
      //prima volta che entro nella popup.
      else
      {         
        Vector<TipoPraticaMantenimentoVO> vTipoPraticMantenim = (Vector<TipoPraticaMantenimentoVO>)elencoPerMantenim.get(new Integer(i));
        if(vTipoPraticMantenim != null)
        {
          for(int l = 0; l < vTipoPraticMantenim.size(); l++) 
          {
            TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPraticMantenim.get(l);
            htmpl.newBlock("blkElencoUtilizzi.blkTipoPraticaMantenimento");
            htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
            htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
            
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdPraticaMantenimento())) 
            {
              if(vUtilizziVO.get(i).getIdPraticaMantenimento().compareTo(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
              }
            }             
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
		    }            
      }       
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
    }  
    
    
    
    if(elencoFaseAllev != null && elencoFaseAllev.size() > 0) 
    {  
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzo") != null)
          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
        {
          if("S".equalsIgnoreCase(elencoFaseAllev.get(new Integer(i))))
          {
	          if(vTipoFaseAllev != null)
	          {
	            for(int l = 0; l < vTipoFaseAllev.size(); l++) 
	            {
	              TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vTipoFaseAllev.get(l);
	              htmpl.newBlock("blkElencoUtilizzi.blkTipoFaseAllevamento");
	              htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
	              htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
	             
	              if(request.getParameterValues("idFaseAllevamento") != null && i < request.getParameterValues("idFaseAllevamento").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i]) 
	                  && request.getParameterValues("idFaseAllevamento")[i].equalsIgnoreCase(new Long(tipoFaseAllevamentoVO.getIdFaseAllevamento()).toString())) 
	                {
	                  htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
	                }
	              }               
	            }	            
	          }
	          else
	          {
	            htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
	            htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
	            htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
	          }
          }
          else
          {
            htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
            htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
            htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
          }
        }
        else
        {
          htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
          htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
          htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
        }
      }
      //prima volta che entro nella popup.
      else
      {         
        if("S".equalsIgnoreCase(elencoFaseAllev.get(new Integer(i))))
        {
          if(vTipoFaseAllev != null)
          {
	          for(int l = 0; l < vTipoFaseAllev.size(); l++) 
	          {
	            TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vTipoFaseAllev.get(l);
	            htmpl.newBlock("blkElencoUtilizzi.blkTipoFaseAllevamento");
	            htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
	            htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
	            
	            if((vUtilizziVO != null)
	              && (i < vUtilizziVO.size())
	              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdFaseAllevamento())) 
	            {
	              if(vUtilizziVO.get(i).getIdFaseAllevamento().compareTo(new Long(tipoFaseAllevamentoVO.getIdFaseAllevamento())) == 0) 
	              {
	                htmpl.set("blkElencoUtilizzi.blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
	              }
	            }             
	          }
	        }
	        else
          {
            htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
            htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
            htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
          }
        }
        else
        {
          htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
          htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
          htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
        }           
      }       
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkHiddenFaseAllevamento");
      htmpl.set("blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
      htmpl.set("blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
    }                
     
    
    //abilita semina
    boolean abilitaSemina = false;
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]))
      {
        abilitaSemina = true;
      }
    }
    else
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getIdUtilizzo()))
      {
        abilitaSemina = true;
      } 
    }
    
    
    if(abilitaSemina)
    {
	    for(int e=0;e<vTipoSemina.size(); e++) 
	    {
	      TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
	      htmpl.newBlock("blkElencoUtilizzi.blkTipoSemina");
	      htmpl.set("blkElencoUtilizzi.blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
	      htmpl.set("blkElencoUtilizzi.blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
	      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
	      {
	        if(request.getParameterValues("idTipoSemina") != null 
	          && i < request.getParameterValues("idTipoSemina").length) 
	        {
	          if(Validator.isNotEmpty(request.getParameterValues("idTipoSemina")[i]) 
	            && Long.decode(request.getParameterValues("idTipoSemina")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	          {
	            htmpl.set("blkElencoUtilizzi.blkTipoSemina.selected", "selected=\"selected\"", null);
	          }
	        }	        
	      }
	      else 
	      {
	        if((vUtilizziVO != null)
	          && (i < vUtilizziVO.size())
	          && Validator.isNotEmpty(vUtilizziVO.get(i).getIdSemina())
	          && vUtilizziVO.get(i).getIdSemina().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
	        {
	          htmpl.set("blkElencoUtilizzi.blkTipoSemina.selected", "selected=\"selected\"", null);
	        }
	      }
	    }
	  }
	  else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoSemina");
    }  
	  
	  
	  if(abilitaSemina)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
      {
        if(request.getParameterValues("dataInizioDestinazione") != null 
          && (i < request.getParameterValues("dataInizioDestinazione").length) 
          && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazione")[i]))
        { 
          htmpl.set("blkElencoUtilizzi.dataInizioDestinazione", request.getParameterValues("dataInizioDestinazione")[i]);
        }
        
        if(request.getParameterValues("dataFineDestinazione") != null 
          && (i < request.getParameterValues("dataFineDestinazione").length) 
          && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazione")[i]))
        { 
          htmpl.set("blkElencoUtilizzi.dataFineDestinazione", request.getParameterValues("dataFineDestinazione")[i]);
        }
      }
      else
      {
        if((vUtilizziVO != null)
          && (i < vUtilizziVO.size())
          && Validator.isNotEmpty(vUtilizziVO.get(i).getDataInizioDestinazione()))
        { 
          htmpl.set("blkElencoUtilizzi.dataInizioDestinazione", DateUtils.formatDateNotNull(vUtilizziVO.get(i).getDataInizioDestinazione()));
        }
        
        if((vUtilizziVO != null)
          && (i < vUtilizziVO.size())
          && Validator.isNotEmpty(vUtilizziVO.get(i).getDataFineDestinazione()))
        { 
          htmpl.set("blkElencoUtilizzi.dataFineDestinazione",  DateUtils.formatDateNotNull(vUtilizziVO.get(i).getDataFineDestinazione()));
        }     
      }
    }
    else
    {
      htmpl.set("blkElencoUtilizzi.readOnlyDataInizioDestinazione", "readOnly=\"true\"", null);
      htmpl.set("blkElencoUtilizzi.readOnlyDataFineDestinazione", "readOnly=\"true\"", null);
    }                   
    
		if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) 
		{
			for(int c = 0; c < elencoTipiUsoSuoloSecondario.size(); c++) 
			{
				TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(c);
				htmpl.newBlock("blkElencoUtilizzi.blkTipiUsoSuoloSecondario");
					htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
				if(tipoUtilizzoVO.getDescrizione().length() > 20) 
				{
					descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
				}
				else 
				{
					descrizione = tipoUtilizzoVO.getDescrizione();
				}
				htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
				
				
				if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
        {
          if(request.getParameterValues("idTipoUtilizzoSecondario") != null 
            && i < request.getParameterValues("idTipoUtilizzoSecondario").length) 
          {
            if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]) 
              && Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
            {
              htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"", null);
            }
          }
        }
        else
        { 
          if((vUtilizziVO != null)
            && (i < vUtilizziVO.size())
            && Validator.isNotEmpty(vUtilizziVO.get(i).getIdUtilizzoSecondario())       
					  && (vUtilizziVO.get(i).getIdUtilizzoSecondario().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)) 
					{
						htmpl.set("blkElencoUtilizzi.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"", null);
					}
				}
			}
		}
		
		if(elencoDestinazioneSecondario != null && elencoDestinazioneSecondario.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
          && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneSecondario.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazioneSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDestinazioneSecondario") != null && i < request.getParameterValues("idTipoDestinazioneSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneSecondario")[i]) 
                  && request.getParameterValues("idTipoDestinazioneSecondario")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
		    } 
      }
      else
      {
        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneSecondario.get(i);
        if((vDestinazione != null)
          && (vDestinazione.size() > 0))
        {
          for(int d = 0; d < vDestinazione.size(); d++) 
          {
            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiDestinazioneSecondario");
            htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
            descrizione = null;
            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoDestinazioneSecondario()))
            {
              if(vUtilizziVO.get(i).getIdTipoDestinazioneSecondario().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
		    } 
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
    } 
    
    if(elencoDettUsoSecondario != null && elencoDettUsoSecondario.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoDestinazioneSecondario") != null)
          && (request.getParameterValues("idTipoDestinazioneSecondario")[i] != null))
        {
          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(new Integer(i));
          if(vDettaglioUso != null)
          {
            for(int d = 0; d < vDettaglioUso.size(); d++) 
            {
              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
              descrizione = null;
              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDettaglioUsoVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoDettaglioUsoSecondario") != null && i < request.getParameterValues("idTipoDettaglioUsoSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]) 
                  && request.getParameterValues("idTipoDettaglioUsoSecondario")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
		    } 
      }
      else
      {
        Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(i);
        if((vDettaglioUso != null)
          && (vDettaglioUso.size() > 0))
        {
          for(int d = 0; d < vDettaglioUso.size(); d++) 
          {
            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario");
            htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
            descrizione = null;
            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDettaglioUsoVO.getDescrizione();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoDettaglioUsoSecondario()))
            {
              if(vUtilizziVO.get(i).getIdTipoDettaglioUsoSecondario().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
		    } 
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
    } 
    
    
    if(elencoQualitaUsoSecondario != null && elencoQualitaUsoSecondario.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoDettaglioUsoSecondario") != null)
          && (request.getParameterValues("idTipoDettaglioUsoSecondario")[i] != null))
        {
          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(new Integer(i));
          if(vQualitaUso != null)
          {
            for(int d = 0; d < vQualitaUso.size(); d++) 
            {
              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUsoSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
              descrizione = null;
              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
              }
              else 
              {
                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
               
              if(request.getParameterValues("idTipoQualitaUsoSecondario") != null && i < request.getParameterValues("idTipoQualitaUsoSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]) 
                  && request.getParameterValues("idTipoQualitaUsoSecondario")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
		    } 
      }
      else
      {
        Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(i);
        if((vQualitaUso != null)
          && (vQualitaUso.size() > 0))
        {
          for(int d = 0; d < vQualitaUso.size(); d++) 
          {
            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
            htmpl.newBlock("blkElencoUtilizzi.blkTipiQualitaUsoSecondario");
            htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
            descrizione = null;
            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
            {
              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
            }
            else 
            {
              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
            }
            htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoQualitaUsoSecondario()))
            {
              if(vUtilizziVO.get(i).getIdTipoQualitaUsoSecondario().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
		    } 
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
    } 
		
		
		if(elencoVarietaSecondaria != null && elencoVarietaSecondaria.size() > 0 ) 
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
      {
        if((request.getParameterValues("idTipoQualitaUsoSecondario") != null)
          && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]))
        {
          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarietaSecondaria.get(new Integer(i));
          if(tipoVarieta != null) 
          {
            for(int h = 0; h < tipoVarieta.size(); h++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiVarietaSecondaria");
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
              if(request.getParameterValues("idVarietaSecondaria") != null 
                && i < request.getParameterValues("idVarietaSecondaria").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idVarietaSecondaria")[i]) 
                  && request.getParameterValues("idVarietaSecondaria")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
		    } 
      }
      else
      {
        if(Validator.isNotEmpty(elencoVarietaSecondaria.get(i))) 
        {
          Vector<TipoVarietaVO> tipoVarieta = elencoVarietaSecondaria.get(i);
          if(tipoVarieta != null) 
          {
            for(int h = 0; h < tipoVarieta.size(); h++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
              htmpl.newBlock("blkElencoUtilizzi.blkTipiVarietaSecondaria");
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
              if((vUtilizziVO != null)
		            && (i < vUtilizziVO.size())
		            && Validator.isNotEmpty(vUtilizziVO.get(i).getIdVarietaSecondaria()))  
              {
                if(vUtilizziVO.get(i).getIdVarietaSecondaria().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
			    } 
        }
      }
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
    } 
    
    
    
    // Carico la combo del perido semina solo se l'utente ha selezionato il tipo
    // uso suolo corrispondente
    if(elencoPerSeminaSecondario != null && elencoPerSeminaSecondario.size() > 0) 
    {  
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
          && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
        {
          Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSeminaSecondario.get(new Integer(i));
          if(vTipoPeriodoSemina != null)
          {
            for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
            {
              TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
              htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario");
              htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
              htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
             
              if(request.getParameterValues("idTipoPeriodoSeminaSecondario") != null && i < request.getParameterValues("idTipoPeriodoSeminaSecondario").length) 
              {
                if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]) 
                  && request.getParameterValues("idTipoPeriodoSeminaSecondario")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
                {
                  htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
                }
              }               
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
			    } 
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
		    } 
      }
      //prima volta che entro nella popup.
      else
      {         
        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSeminaSecondario.get(i);
        if(vTipoPeriodoSemina != null)
        {
          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
          {
            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
            htmpl.newBlock("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario");
            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
            htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
            
            if((vUtilizziVO != null)
              && (i < vUtilizziVO.size())
              && Validator.isNotEmpty(vUtilizziVO.get(i).getIdTipoPeriodoSeminaSecondario())) 
            {
              if(vUtilizziVO.get(i).getIdTipoPeriodoSeminaSecondario().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
              {
                htmpl.set("blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
              }
            }             
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
		    }            
      }       
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
    }     
    
    
    
    //abilita semina
    boolean abilitaSeminaSec = false;
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]))
      {
        abilitaSeminaSec = true;
      }
    }
    else
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getIdUtilizzoSecondario()))
      {
        abilitaSeminaSec = true;
      } 
    }
    
    if(abilitaSeminaSec)
    {
	    for(int e=0;e<vTipoSemina.size(); e++) 
	    {
	      TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
	      htmpl.newBlock("blkElencoUtilizzi.blkTipoSeminaSecondario");
	      htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.idTipoSeminaSecondario", ""+tipoSeminaVO.getIdTipoSemina());
	      htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.descrizione", tipoSeminaVO.getDescrizioneSemina());
	      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
	      {
	        if(request.getParameterValues("idTipoSeminaSecondario") != null 
	          && i < request.getParameterValues("idTipoSeminaSecondario").length) 
	        {
	          if(Validator.isNotEmpty(request.getParameterValues("idTipoSeminaSecondario")[i]) 
	            && Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	          {
	            htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
	          }
	        }
	        
	      }
	      else 
	      {
	        if((vUtilizziVO != null)
	          && (i < vUtilizziVO.size())
	          && Validator.isNotEmpty(vUtilizziVO.get(i).getIdSeminaSecondario())
	          && vUtilizziVO.get(i).getIdSeminaSecondario().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
	        {
	          htmpl.set("blkElencoUtilizzi.blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
	        }
	      }
	    }
	  }
	  else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipoSeminaSecondario");
    } 
	  
	  if(abilitaSeminaSec)
	  {
	    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
      {
		    if(request.getParameterValues("dataInizioDestinazioneSec") != null 
	        && (i < request.getParameterValues("dataInizioDestinazioneSec").length) 
	        && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazioneSec")[i]))
	      { 
		      htmpl.set("blkElencoUtilizzi.dataInizioDestinazioneSec", request.getParameterValues("dataInizioDestinazioneSec")[i]);
		    }
		    
		    if(request.getParameterValues("dataFineDestinazioneSec") != null 
	        && (i < request.getParameterValues("dataFineDestinazioneSec").length) 
	        && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazioneSec")[i]))
	      { 
	        htmpl.set("blkElencoUtilizzi.dataFineDestinazioneSec", request.getParameterValues("dataFineDestinazioneSec")[i]);
	      }
	    }
	    else
	    {
	      if((vUtilizziVO != null)
          && (i < vUtilizziVO.size())
          && Validator.isNotEmpty(vUtilizziVO.get(i).getDataInizioDestinazioneSec()))
        { 
          htmpl.set("blkElencoUtilizzi.dataInizioDestinazioneSec", DateUtils.formatDateNotNull(vUtilizziVO.get(i).getDataInizioDestinazioneSec()));
        }
        
        if((vUtilizziVO != null)
          && (i < vUtilizziVO.size())
          && Validator.isNotEmpty(vUtilizziVO.get(i).getDataFineDestinazioneSec()))
        { 
          htmpl.set("blkElencoUtilizzi.dataFineDestinazioneSec",  DateUtils.formatDateNotNull(vUtilizziVO.get(i).getDataFineDestinazioneSec()));
        }	    
	    }
	  }
	  else
	  {
	    htmpl.set("blkElencoUtilizzi.readOnlyDataInizioDestinazioneSec", "readOnly=\"true\"", null);
	    htmpl.set("blkElencoUtilizzi.readOnlyDataFineDestinazioneSec", "readOnly=\"true\"", null);
	  }          
    
    
    
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("supUtilizzataSecondaria") != null 
        && i < request.getParameterValues("supUtilizzataSecondaria").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.supUtilizzataSecondaria", request.getParameterValues("supUtilizzataSecondaria")[i]);
        }
      }
    }
    else
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getSupUtilizzataSecondaria()))
      {
        htmpl.set("blkElencoUtilizzi.supUtilizzataSecondaria", StringUtils.parseSuperficieField(vUtilizziVO.get(i).getSupUtilizzataSecondaria()));
      }
    }
    
    
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("annoImpianto") != null && i < request.getParameterValues("annoImpianto").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.annoImpianto", request.getParameterValues("annoImpianto")[i]);
        }
      }
    }
    //prima volta che entro
    else 
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getAnnoImpianto()))
      {
        htmpl.set("blkElencoUtilizzi.annoImpianto", vUtilizziVO.get(i).getAnnoImpianto());
      }
    }
    
    
    for(int e = 0; e < elencoTipoImpianto.length; e++) 
    {
      TipoImpiantoVO tipoImpiantoVO = (TipoImpiantoVO)elencoTipoImpianto[e];
      htmpl.newBlock("blkElencoUtilizzi.blkTipiImpianto");
      htmpl.set("blkElencoUtilizzi.blkTipiImpianto.idImpianto", tipoImpiantoVO.getIdImpianto().toString());
      htmpl.set("blkElencoUtilizzi.blkTipiImpianto.descrizione", tipoImpiantoVO.getDescrizione());
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
      {
        if(request.getParameterValues("idImpianto") != null 
          && i < request.getParameterValues("idImpianto").length) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i]) 
            && Long.decode(request.getParameterValues("idImpianto")[i]).compareTo(tipoImpiantoVO.getIdImpianto()) == 0) 
          {
            htmpl.set("blkElencoUtilizzi.blkTipiImpianto.selected", "selected=\"selected\"", null);
          }
        }
      }
      else 
      {
        if((vUtilizziVO != null)
          && (i < vUtilizziVO.size())
          && Validator.isNotEmpty(vUtilizziVO.get(i).getIdImpianto())
          && vUtilizziVO.get(i).getIdImpianto().compareTo(tipoImpiantoVO.getIdImpianto()) == 0)
        {
          htmpl.set("blkElencoUtilizzi.blkTipiImpianto.selected", "selected=\"selected\"", null);
        }
      }
    }    
		
		
		if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("sestoSuFile") != null 
        && i < request.getParameterValues("sestoSuFile").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.sestoSuFile", request.getParameterValues("sestoSuFile")[i]);
        }
      }
    } 
    else 
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getSestoSuFile())) 
      {
        htmpl.set("blkElencoUtilizzi.sestoSuFile", vUtilizziVO.get(i).getSestoSuFile());
      }
    }
      
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("sestoTraFile") != null 
        && i < request.getParameterValues("sestoTraFile").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.sestoTraFile", request.getParameterValues("sestoTraFile")[i]);
        }
      }
    } 
    else 
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getSestoTraFile()))
      {
        htmpl.set("blkElencoUtilizzi.sestoTraFile", vUtilizziVO.get(i).getSestoTraFile());
      }
    }
      
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
    {
      if(request.getParameterValues("numeroPianteCeppi") != null 
        && i < request.getParameterValues("numeroPianteCeppi").length) 
      {
        if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) 
        {
          htmpl.set("blkElencoUtilizzi.numeroPianteCeppi", request.getParameterValues("numeroPianteCeppi")[i]);
        }
      }
    }
    else 
    {
      if((vUtilizziVO != null)
        && (i < vUtilizziVO.size())
        && Validator.isNotEmpty(vUtilizziVO.get(i).getNumeroPianteCeppi()))
      {
        htmpl.set("blkElencoUtilizzi.numeroPianteCeppi", vUtilizziVO.get(i).getNumeroPianteCeppi());
      }
    }
    
		// Sezione relativa alle piante e agli utilizzi consociati
	  //UtilizzoConsociatoVO[] elencoUtilizziConsociati = vUtilizziVO.get(i).getElencoUtilizziConsociati();
		if(Validator.isNotEmpty(elencoPianteConsociate)) 
    {
      int numPianteConsociate = elencoPianteConsociate.size();
		  boolean isNewConsociato = false;
			for(int a = 0; a < numPianteConsociate; a++) 
			{
				htmpl.newBlock("blkElencoUtilizzi.blkElencoPianteConsociate");
				if(Validator.isNotEmpty(regimeParticellareInserisciCondUso)) 
        {
          if(i == 0) 
          {
            if(request.getParameterValues("numeroPianteConsociate") != null 
              && Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[a]);
            }
            else 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
            }
          }
          else 
          {
            int valoreSelected = (i * numPianteConsociate) + a;
            if(valoreSelected < request.getParameterValues("numeroPianteConsociate").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valoreSelected])) 
              {
                htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[valoreSelected]);
              }
              else 
              {
                htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
              }
            }
            else 
            {
              isNewConsociato = true;
            }
          }
        }
        else 
        {
          if(Validator.isNotEmpty(vUtilizziVO)
            && (i < vUtilizziVO.size())
            && Validator.isNotEmpty(vUtilizziVO.get(i).getElencoUtilizziConsociati()))
          {
            UtilizzoConsociatoVO utilizzoConsociatoVO = vUtilizziVO.get(i).getElencoUtilizziConsociati()[a];
            if(Validator.isNotEmpty(utilizzoConsociatoVO.getNumeroPiante()) 
              && !utilizzoConsociatoVO.getNumeroPiante().equalsIgnoreCase("0")) 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", utilizzoConsociatoVO.getNumeroPiante());
            }
            else 
            {
              htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
            }
          }
        }
				// Gestione errori
	 			if(erroriPianteConsociate != null && erroriPianteConsociate.size() > 0) 
	 			{
	 				ValidationErrors errorsConsociate = null;
	 				if(i == 0) 
	 				{
	 					errorsConsociate = (ValidationErrors)erroriPianteConsociate.get(new Integer(a));
	 				}
	 				else 
	 				{
	 					errorsConsociate = (ValidationErrors)erroriPianteConsociate.get(new Integer((i*numPianteConsociate)+a));
	 				}
	 				if(errorsConsociate != null && errorsConsociate.size() > 0) 
	 				{
	 					Iterator iter = htmpl.getVariableIterator();
	 					while(iter.hasNext()) 
	 					{
	 						String chiave = (String)iter.next();
	 						if(chiave.startsWith("err_")) 
	 						{
 				    		String property = chiave.substring(4);
 				    		Iterator errorIterator = errorsConsociate.get(property);
 				    		if(errorIterator != null) 
 				    		{
 				    			ValidationError error = (ValidationError)errorIterator.next();
 				    			htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.err_"+property,
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
			if(request.getParameterValues("numeroPianteConsociate") != null && isNewConsociato) 
			{
				if(request.getParameterValues("numeroPianteConsociate").length < (numeroUtilizzi * numPianteConsociate)) 
				{
					int valorePartenza = (numeroUtilizzi * numPianteConsociate) - request.getParameterValues("numeroPianteConsociate").length;
					for(int j = 0; j < valorePartenza; j++) 
					{
						htmpl.set("blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
					}
			  }
			}
		}
		// Gestione errori
		if(erroriUtilizzi != null && erroriUtilizzi.size() > 0) {
			ValidationErrors errorsUtilizzi = (ValidationErrors)erroriUtilizzi.get(new Integer(i));
			if(errorsUtilizzi != null && errorsUtilizzi.size() > 0) {
		 		Iterator iter = htmpl.getVariableIterator();
		 		while(iter.hasNext()) {
		 			String chiave = (String)iter.next();
		 			if(chiave.startsWith("err_")) {
		 			    String property = chiave.substring(4);
		 			    Iterator errorIterator = errorsUtilizzi.get(property);
		 			    if(errorIterator != null) {
		 			    	ValidationError error = (ValidationError)errorIterator.next();
		 			    	htmpl.set("blkElencoUtilizzi.err_"+property,
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
	
	
	
	Vector<UtilizzoParticellaVO> vUtilizziEfaVO = (Vector<UtilizzoParticellaVO>)request.getAttribute("vUtilizziEfaVO");
  int numeroUtilizziEfa = 0;
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoEfa")))
      numeroUtilizziEfa = request.getParameterValues("idTipoUtilizzoEfa").length;
  }
  else if(Validator.isNotEmpty(vUtilizziEfaVO))
  {
    numeroUtilizziEfa = vUtilizziEfaVO.size();
  }
  
  for(int i=0;i<numeroUtilizziEfa;i++)
  {
    UtilizzoParticellaVO utilizzoParticellaEfaVO = null;
    if(Validator.isEmpty(regimeParticellareInserisciCondUso))
    {
      if(Validator.isNotEmpty(vUtilizziEfaVO))
        utilizzoParticellaEfaVO = vUtilizziEfaVO.get(i);
    }
      
    TipoEfaVO tipoEfaVOSel = null;
      
    htmpl.newBlock("blkElencoEfa");
    htmpl.set("blkElencoEfa.numeroUtilizziEfa", ""+i);
    // Combo tipo efa
    if(vTipoEfa != null) 
    {
      for(int j=0;j<vTipoEfa.size();j++) 
      {
        TipoEfaVO tipoEfaVO = vTipoEfa.get(j);
        htmpl.newBlock("blkElencoEfa.blkTipoEfa");
        htmpl.set("blkElencoEfa.blkTipoEfa.idTipoEfa", ""+tipoEfaVO.getIdTipoEfa());
        htmpl.set("blkElencoEfa.blkTipoEfa.descrizioneEstesa", tipoEfaVO.getDescrizioneEstesaTipoEfa());
        htmpl.set("blkElencoEfa.blkTipoEfa.descrizione", tipoEfaVO.getDescrizioneTipoEfa());
        //prima volta che entro
        if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
        {
          if(Validator.isNotEmpty(arrIdTipoEfa)
            && (i < arrIdTipoEfa.length)
            && Validator.isNotEmpty(arrIdTipoEfa[i])
            && new Long(arrIdTipoEfa[i]).longValue() == tipoEfaVO.getIdTipoEfa()) 
          {
            htmpl.set("blkElencoEfa.blkTipoEfa.selected", "selected=\"selected\"", null);
            htmpl.set("blkElencoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
            htmpl.set("blkElencoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
            htmpl.set("blkElencoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
            
            tipoEfaVOSel = tipoEfaVO;
          }
        }
        else
        {
          if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
            && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoEfa())
            && (utilizzoParticellaEfaVO.getIdTipoEfa() == tipoEfaVO.getIdTipoEfa())) 
          {
            htmpl.set("blkElencoEfa.blkTipoEfa.selected", "selected=\"selected\"", null);
            htmpl.set("blkElencoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
            htmpl.set("blkElencoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
            htmpl.set("blkElencoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
          } 
        }
      }
    }
    
    
    if(Validator.isNotEmpty(elencoUtilizziEfa))
    {
      Vector<TipoUtilizzoVO> vTipoUtilizzo = elencoUtilizziEfa.get(new Integer(i));
      
      if(Validator.isNotEmpty(vTipoUtilizzo)
        && (vTipoUtilizzo.size() > 0))
      {
        for(int j=0;j<vTipoUtilizzo.size();j++) 
        {
          TipoUtilizzoVO tipoUtilizzoVO = vTipoUtilizzo.get(j);
          htmpl.newBlock("blkElencoEfa.blkTipiUsoSuoloEfa");
          
          htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.idTipoUtilizzoEfa", ""+tipoUtilizzoVO.getIdUtilizzo());
          
          String descrizione = null;
          if(tipoUtilizzoVO.getDescrizione().length() > 20) 
          {
            descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
          }
          else 
          {
            descrizione = tipoUtilizzoVO.getDescrizione();
          }
          htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
          
          if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
          {
            if(Validator.isNotEmpty(elencoUtilizziEfaSelezionati)
              && (i < elencoUtilizziEfaSelezionati.length)
              && Validator.isNotEmpty(elencoUtilizziEfaSelezionati[i]))
            {
              if(new Long(elencoUtilizziEfaSelezionati[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.selected", "selected=\"selected\"", null);
              }
            }
          }
          else
          {
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdUtilizzo())
              && utilizzoParticellaEfaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)
            { 
              htmpl.set("blkElencoEfa.blkTipiUsoSuoloEfa.selected", "selected=\"selected\"", null);
            }
          }  
          
        }
      }
      else
	    {
	      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiUsoSuoloEfa");
	    } 
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiUsoSuoloEfa");
    } 
    
    if(elencoDestinazioneEfa != null && elencoDestinazioneEfa.size() > 0)
    {
      if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
      {
        if((request.getParameterValues("idTipoUtilizzoEfa") != null)
          && (request.getParameterValues("idTipoUtilizzoEfa")[i] != null))
        {
          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneEfa.get(new Integer(i));
          if(vDestinazione != null)
          {
            for(int d = 0; d < vDestinazione.size(); d++) 
            {
              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
              htmpl.newBlock("blkElencoEfa.blkTipiDestinazioneEfa");
              htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.idTipoDestinazioneEfa", ""+tipoDestinazioneVO.getIdTipoDestinazione());
              String descrizione = null;
              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
              }
              htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
               
              if(Validator.isNotEmpty(elencoDestinazioneEfaSelezionate)
	              && (i < elencoDestinazioneEfaSelezionate.length)
	              && Validator.isNotEmpty(elencoDestinazioneEfaSelezionate[i]))
	            {
                if(elencoDestinazioneEfaSelezionate[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
                {
                  htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
			    {
			      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneEfa");
			    }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneEfa");
		    }
      }
      else
      {
        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(i);
        if((vDestinazione != null)
          && (vDestinazione.size() > 0))
        {
          for(int d = 0; d < vDestinazione.size(); d++) 
          {
            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
            htmpl.newBlock("blkElencoEfa.blkTipiDestinazioneEfa");
            htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.idTipoDestinazioneEfa", ""+tipoDestinazioneVO.getIdTipoDestinazione());
            String descrizione = null;
            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
            }
            else 
            {
              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
            }
            htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoDestinazione()))
            {
              if(utilizzoParticellaEfaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
              {
                htmpl.set("blkElencoEfa.blkTipiDestinazioneEfa.selected", "selected=\"selected\"",null);
              }
            }
          }
        }
        else
		    {
		      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneEfa");
		    }
      }         
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDestinazioneEfa");
    } 
    
    if(Validator.isNotEmpty(elencoDettaglioUsoEfa))
    {
      Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = elencoDettaglioUsoEfa.get(new Integer(i));
      if(Validator.isNotEmpty(vTipoDettaglioUso)
        && vTipoDettaglioUso.size() > 0)
      {
        for(int j=0;j<vTipoDettaglioUso.size();j++) 
        {
          TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(j);
          htmpl.newBlock("blkElencoEfa.blkTipiDettaglioUsoEfa");
          
          htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.idTipoDettaglioUsoEfa", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
          
          String descrizione = null;
          if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
          {
            descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
          }
          else 
          {
            descrizione = tipoDettaglioUsoVO.getDescrizione();
          }
          htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
          
          if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
          {
            if(Validator.isNotEmpty(elencoDetUsoEfaSelezionate)
              && (i < elencoDetUsoEfaSelezionate.length)
              && Validator.isNotEmpty(elencoDetUsoEfaSelezionate[i]))
            {
              if(new Long(elencoDetUsoEfaSelezionate[i]).compareTo(tipoDettaglioUsoVO.getIdTipoDettaglioUso()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.selected", "selected=\"selected\"", null);
              }       
            }
          }
          else
          {
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoDettaglioUso())
              && utilizzoParticellaEfaVO.getIdTipoDettaglioUso().compareTo(tipoDettaglioUsoVO.getIdTipoDettaglioUso()) == 0)
            { 
              htmpl.set("blkElencoEfa.blkTipiDettaglioUsoEfa.selected", "selected=\"selected\"", null);
            }
          }         
        }
      }
      else
	    {
	      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoEfa");
	    }  
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiDettaglioUsoEfa");
    }
    
    
    if(Validator.isNotEmpty(elencoQualitaUsoEfa))
    {
      Vector<TipoQualitaUsoVO> vTipoQualitaUso = elencoQualitaUsoEfa.get(new Integer(i));
      if(Validator.isNotEmpty(vTipoQualitaUso)
        && vTipoQualitaUso.size() > 0)
      {
        for(int j=0;j<vTipoQualitaUso.size();j++) 
        {
          TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(j);
          htmpl.newBlock("blkElencoEfa.blkTipiQualitaUsoEfa");
          
          htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.idTipoQualitaUsoEfa", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
          
          String descrizione = null;
          if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
          {
            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
          }
          else 
          {
            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
          }
          htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
          
          if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
          {
            if(Validator.isNotEmpty(elencoQualUsoEfaSelezionate)
              && (i < elencoQualUsoEfaSelezionate.length)
              && Validator.isNotEmpty(elencoQualUsoEfaSelezionate[i]))
            {
              if(new Long(elencoQualUsoEfaSelezionate[i]).compareTo(tipoQualitaUsoVO.getIdTipoQualitaUso()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.selected", "selected=\"selected\"", null);
              }       
            }
          }
          else
          {
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoQualitaUso())
              && utilizzoParticellaEfaVO.getIdTipoQualitaUso().compareTo(tipoQualitaUsoVO.getIdTipoQualitaUso()) == 0)
            { 
              htmpl.set("blkElencoEfa.blkTipiQualitaUsoEfa.selected", "selected=\"selected\"", null);
            }
          }         
        }
      }
      else
	    {
	      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoEfa");
	    }  
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiQualitaUsoEfa");
    }
    
    //varieta
    if(Validator.isNotEmpty(elencoVarietaEfa))
    {
      Vector<TipoVarietaVO> vTipoVarieta = elencoVarietaEfa.get(new Integer(i));
      if(Validator.isNotEmpty(vTipoVarieta)
        && vTipoVarieta.size() > 0)
      {
        for(int j=0;j<vTipoVarieta.size();j++) 
        {
          TipoVarietaVO tipoVarietaVO = vTipoVarieta.get(j);
          htmpl.newBlock("blkElencoEfa.blkTipiVarietaEfa");
          
          htmpl.set("blkElencoEfa.blkTipiVarietaEfa.idVarietaEfa", tipoVarietaVO.getIdVarieta().toString());
          
          String descrizione = null;
          if(tipoVarietaVO.getDescrizione().length() > 20) 
          {
            descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
          }
          else 
          {
            descrizione = tipoVarietaVO.getDescrizione();
          }
          htmpl.set("blkElencoEfa.blkTipiVarietaEfa.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
          
          
          if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
          {
            if(Validator.isNotEmpty(elencoVarietaEfaSelezionate)
              && (i < elencoVarietaEfaSelezionate.length)
              && Validator.isNotEmpty(elencoVarietaEfaSelezionate[i]))
            {
              if(new Long(elencoVarietaEfaSelezionate[i]).compareTo(tipoVarietaVO.getIdVarieta()) == 0)
              { 
                htmpl.set("blkElencoEfa.blkTipiVarietaEfa.selected", "selected=\"selected\"", null);
              }       
            }
          }
          else
          {
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdVarieta())
              && utilizzoParticellaEfaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0)
            { 
              htmpl.set("blkElencoEfa.blkTipiVarietaEfa.selected", "selected=\"selected\"", null);         
            } 
          }         
        }
      }
      else
	    {
	      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaEfa");
	    } 
    }
    else
    {
      htmpl.newBlock("blkElencoUtilizzi.blkNoTipiVarietaEfa");
    }
    
    
    
    
    Integer abbaPonderazione = new Integer(1);
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(Validator.isNotEmpty(arrIdTipoEfa)
         && (i < arrIdTipoEfa.length)
         && Validator.isNotEmpty(arrIdTipoEfa[i]))
      {
        htmpl.set("blkElencoEfa.abbPonderazioneVarieta", elencoAbbPonderazioneVarieta[i]);
        abbaPonderazione = new Integer(elencoAbbPonderazioneVarieta[i]);
      }    
    }
    else
    {
      if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
        && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoEfa()))
      { 
        htmpl.set("blkElencoEfa.abbPonderazioneVarieta", ""+utilizzoParticellaEfaVO.getAbbaPonderazione());
        abbaPonderazione = utilizzoParticellaEfaVO.getAbbaPonderazione();
      }    
    }    
    
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(Validator.isNotEmpty(elencoValoreOriginale)
        && (i < elencoValoreOriginale.length)
        && Validator.isNotEmpty(elencoValoreOriginale[i]))
      {
        htmpl.set("blkElencoEfa.valoreOriginale", elencoValoreOriginale[i]);
        
        if(Validator.isNotEmpty(tipoEfaVOSel))
        {
          BigDecimal valoreOriginale = null;
          BigDecimal valoreDopoConversione = null;
          BigDecimal valoreDopoPonderazione = null;
          try
          {
            valoreOriginale = new BigDecimal(elencoValoreOriginale[i].replace(",", "."));
          }
          catch(Exception ex){}
          
          if(Validator.isNotEmpty(valoreOriginale))
          {
            valoreDopoConversione = valoreOriginale.multiply(tipoEfaVOSel.getFattoreDiConversione());
            valoreDopoConversione = valoreDopoConversione.divide(new BigDecimal(10000), 4,BigDecimal.ROUND_HALF_UP);
            valoreDopoPonderazione = valoreDopoConversione.multiply(tipoEfaVOSel.getFattoreDiPonderazione());
            valoreDopoPonderazione = valoreDopoPonderazione.multiply(new BigDecimal(abbaPonderazione.intValue()));
          }
          
          
          if(Validator.isNotEmpty(valoreDopoConversione))
            htmpl.set("blkElencoEfa.valoreDopoConversione", Formatter.formatDouble4(valoreDopoConversione));
            
          if(Validator.isNotEmpty(valoreDopoPonderazione))
            htmpl.set("blkElencoEfa.valoreDopoPonderazione", Formatter.formatDouble4(valoreDopoPonderazione));
        } 
        
               
      }
    }
    else
    {
      if(Validator.isNotEmpty(utilizzoParticellaEfaVO))
      {
        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreOriginale()))
          htmpl.set("blkElencoEfa.valoreOriginale",  Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreOriginale()));
        
        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreDopoConversione()))
          htmpl.set("blkElencoEfa.valoreDopoConversione", Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreDopoConversione()));
          
        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreDopoPonderazione()))
          htmpl.set("blkElencoEfa.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreDopoPonderazione()));
      }
    
    } 
    
    
    
    
    // Gestione errori
    if(erroriUtilizziEfa != null && erroriUtilizziEfa.size() > 0) 
    {
      ValidationErrors errorsUtilizziEfa = (ValidationErrors)erroriUtilizziEfa.get(new Integer(i));
      if(errorsUtilizziEfa != null && errorsUtilizziEfa.size() > 0) 
      {
        Iterator iter = htmpl.getVariableIterator();
        while(iter.hasNext()) 
        {
          String chiave = (String)iter.next();
          if(chiave.startsWith("err_")) 
          {
            String property = chiave.substring(4);
            Iterator errorIterator = errorsUtilizziEfa.get(property);
            if(errorIterator != null) 
            {
              ValidationError error = (ValidationError)errorIterator.next();
              htmpl.set("blkElencoEfa.err_"+property,
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
    
    
  } // for utilizzi efa
	
	
	
	
 	
	
 	// Gestione degli errori
 	if(errors != null && errors.size() > 0) {
 		HtmplUtil.setErrors(htmpl, errors, request, application);
 	}

%>
<%= htmpl.text()%>
