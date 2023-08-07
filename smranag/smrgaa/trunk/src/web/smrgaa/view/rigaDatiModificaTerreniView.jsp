<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/rigaDatiModificaTerreni.htm");

 	Htmpl htmpl = new Htmpl(layout);
 	String urlRSDI = (String) SolmrConstants.get("RSDI_URL");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String tipoModifica = request.getParameter("tipoModifica");
  String numeroRiga = request.getParameter("numeroRiga");
  StoricoParticellaVO[] elencoStoricoParticella = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticella");
  
  try
  {
  
  
    // Recupero i valori relativi ai tipi uso suolo utilizzati dall'azienda in esame
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
    if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
    {
      elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
    }  
    
    //Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = gaaFacadeClient.getListTipoPeriodoSemina();
    
    
    Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector)session.getAttribute("elencoTipiUsoSuoloSecondario");
    if(elencoTipiUsoSuoloSecondario == null || elencoTipiUsoSuoloSecondario.size() == 0) 
    {
      elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
    }
    
    
    // Recupero i casi particolari
    it.csi.solmr.dto.CodeDescription[] elencoCasiParticolari = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_CODE);    
    
    String particellaObbligatoria = "N";
    Vector<CasoParticolareVO> elencoCasiParticolariPartProv = gaaFacadeClient.getCasiParticolari(particellaObbligatoria);
    
    Vector<TipoSeminaVO> vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
  
  
    int numeroTotaleUtilizzi = 0;
    if(Validator.isNotEmpty(elencoStoricoParticella))
    {
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storicoParticellaVO = elencoStoricoParticella[i];
        ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
        UtilizzoParticellaVO[] elencoUtilizzi = (UtilizzoParticellaVO[])conduzioneParticellaVO.getElencoUtilizzi();
        
        if(new Integer(numeroRiga).intValue() == i)
        {
          
	        htmpl.set("numero", String.valueOf(i));
	        
	        htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
	        if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
	          htmpl.set("siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
	        }
	        if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
	          htmpl.set("sezione", storicoParticellaVO.getSezione());
	        }
	        htmpl.set("foglio", storicoParticellaVO.getFoglio());
	        if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
	          htmpl.set("particella", storicoParticellaVO.getParticella());
	        }
	        if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
	          htmpl.set("subalterno", storicoParticellaVO.getSubalterno());
	        }
	        htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
	        htmpl.set("superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
        
	        //Aggiunta eleggibilità fittizia come tooltip
	        String tooltipDescElegFit = "";
	        String classiEleggibilita = "";
	        if(Validator.isNotEmpty(storicoParticellaVO.getvSupElegFit()))
	        {
	          for(int k=0;k<storicoParticellaVO.getvSupElegFit().size();k++)
	          {
	             tooltipDescElegFit += storicoParticellaVO.getvSupElegFit().get(k).getDescrizione()+": ";
	             classiEleggibilita += storicoParticellaVO.getvSupElegFit().get(k).getDescrizione()+": ";
	             tooltipDescElegFit += Formatter.formatDouble4(storicoParticellaVO
	              .getvSupElegFit().get(k).getSuperficie()) +" ha\n";
	             classiEleggibilita += Formatter.formatDouble4(storicoParticellaVO
                .getvSupElegFit().get(k).getSuperficie()) +" ha<br>";
	          }          
	        }
	        htmpl.set("tooltipDescElegFit", tooltipDescElegFit);
	        htmpl.set("classiEleggibilita", classiEleggibilita, null);
        
	        if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO())
	           && Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	           && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
	           && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
	           && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
	        {
	          htmpl.set("stabilizzato", "true");
	        }
        
             
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
	        htmpl.set("controlliP", titleGis);
        
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
          htmpl.set("immaginiControlliP", immaginiControlliP);
        
        
          // Recupero i titoli di possesso
          it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
      
        
	        htmpl.set("idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
 			htmpl.set("idAzienda", anagAziendaVO.getIdAzienda().toString());
 			htmpl.set("urlRSDI", urlRSDI); 	
 			if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) {
	          for(int a = 0; a < elencoTipoTitoloPossesso.length; a++) {
	            it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTipoTitoloPossesso[a];
	            htmpl.newBlock("blkTitoliPossesso");
	            htmpl.set("blkTitoliPossesso.idTitoloPossesso", code.getCode().toString());
	            htmpl.set("blkTitoliPossesso.descrizione", code.getCode().toString());
	            htmpl.set("blkTitoliPossesso.descTitoloPossesso", code.getDescription());
	            if(conduzioneParticellaVO.getIdTitoloPossesso() != null && conduzioneParticellaVO.getIdTitoloPossesso().compareTo(Long.decode(code.getCode().toString())) == 0) {
	              htmpl.set("blkTitoliPossesso.selected", "selected=\"selected\"",null);
	            }
	          }
	        }
        
	        if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieCondotta())) {
	          htmpl.set("supCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
	        }
	        
	        if(Validator.isNotEmpty(conduzioneParticellaVO.getPercentualePossesso())) {
	          htmpl.set("percentualePossesso", Formatter.formatDouble2(conduzioneParticellaVO.getPercentualePossesso()));
	        }
	        
	        if(Validator.isNotEmpty(conduzioneParticellaVO.getIdTitoloPossesso()) 
	          && (conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0))
	        {
	          htmpl.set("superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
	        }
	        else if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica())) {
	          htmpl.set("superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
	        }
	        
        
        
	        htmpl.set("rowspan", String.valueOf(elencoUtilizzi.length));
	        String nomeBlocco = null;
	        for(int b = 0; b < elencoUtilizzi.length; b++) 
	        {
	        
	          Vector<TipoUtilizzoVO> elencoTipiUsoSuoloEfa = new Vector<TipoUtilizzoVO>();
	          if(b > 0) 
	          {
	            nomeBlocco = "blkElencoUtilizzi.";
	            htmpl.newBlock(nomeBlocco);
	          }
	          else 
	          {
	            nomeBlocco = "";
	          }
          
	          //varibile per identificare la riga dell'utilizzo
	          String numStr = numeroRiga;
	          if(numeroTotaleUtilizzi < 10)
	          {  
	            numStr += "0"+numeroTotaleUtilizzi;
	          }
	          else
	          {
	            numStr += ""+numeroTotaleUtilizzi;
	          }
	          
	          htmpl.set(nomeBlocco+"numTotaleUtilizzo", String.valueOf(numStr));
	          
	          if((storicoParticellaVO.getParticellaCertificataVO() != null)
	            && (storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata() != null))
	          {
	            htmpl.set(nomeBlocco+"idParticellaCertificata", ""+storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata());
	          }
	          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizzi[b];
	          
	          boolean flagUtilEfa = false;
            if("S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa()))
            {
              flagUtilEfa = true;
            }
            
            
            
            if(flagUtilEfa)
            {
              htmpl.set(nomeBlocco+"disabledUt", "disabled=\"disabled\"", null);
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoDisabled");
              htmpl.set(nomeBlocco+"blkNoDisabled.numeroUtilizzi", String.valueOf(b));
              htmpl.set(nomeBlocco+"blkNoDisabled.numTotaleUtilizzo", String.valueOf(numStr));
              /*if((storicoParticellaVO.getParticellaCertificataVO() != null)
	              && (storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata() != null))
	            {
                htmpl.set(nomeBlocco+"blkNoDisabled.idParticellaCertificata",  ""+storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata());
              }  */         
            }
       
	          
	          htmpl.set(nomeBlocco+"numeroUtilizzi", String.valueOf(b));
	          
	          if(!flagUtilEfa)
	          {
		          if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
		          {
		            for(int c = 0; c < elencoTipiUsoSuolo.size(); c++) 
		            {
		              TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(c);
		              htmpl.newBlock(nomeBlocco+"blkTipiUsoSuolo");
		              htmpl.set(nomeBlocco+"blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
		              htmpl.set(nomeBlocco+"blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
		              String descrizione = null;
		              if(tipoUtilizzoVO.getDescrizione().length() > 20) {
		                descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
		              }
		              else {
		                descrizione = tipoUtilizzoVO.getDescrizione();
		              }
		              htmpl.set(nomeBlocco+"blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
		              if(utilizzoParticellaVO.getIdUtilizzo() != null && utilizzoParticellaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
		                htmpl.set(nomeBlocco+"blkTipiUsoSuolo.selected", "selected=\"selected\"",null);
		                htmpl.set(nomeBlocco+"coefficienteRiduzione", Formatter.formatDouble2(tipoUtilizzoVO.getCoefficienteRiduzione()));
		              }
		            }
		          }
		        }
		        else
		        {
		          //ne basta uno in quanto disabilitato...ma per conformita mi creo il vettore cmq
		          elencoTipiUsoSuoloEfa.add(anagFacadeClient.findTipoUtilizzoByPrimaryKey(utilizzoParticellaVO.getIdUtilizzo()));
		          for(int c = 0; c < elencoTipiUsoSuoloEfa.size(); c++) 
              {
                TipoUtilizzoVO tipoUtilizzoVO = elencoTipiUsoSuoloEfa.elementAt(c);
                htmpl.newBlock(nomeBlocco+"blkTipiUsoSuolo");
                htmpl.set(nomeBlocco+"blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
                htmpl.set(nomeBlocco+"blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
                String descrizione = null;
                if(tipoUtilizzoVO.getDescrizione().length() > 20) {
                  descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
                }
                else {
                  descrizione = tipoUtilizzoVO.getDescrizione();
                }
                htmpl.set(nomeBlocco+"blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
                if(utilizzoParticellaVO.getIdUtilizzo() != null && utilizzoParticellaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
                  htmpl.set(nomeBlocco+"blkTipiUsoSuolo.selected", "selected=\"selected\"",null);
                  htmpl.set(nomeBlocco+"coefficienteRiduzione", Formatter.formatDouble2(tipoUtilizzoVO.getCoefficienteRiduzione()));
                }
              }
		        }
		        
		        
		        
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
            {
              Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzoParticellaVO.getIdUtilizzo());
			        if((vDestinazione != null)
			          && (vDestinazione.size() > 0))
			        {
			          for(int d = 0; d < vDestinazione.size(); d++) 
			          {
			            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiDestinazione");
			            htmpl.set(nomeBlocco+"blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
			            String descrizione = null;
			            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
			            {
			              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
			            }
			            htmpl.set(nomeBlocco+"blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazione()))
			            {
			              if(utilizzoParticellaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiDestinazione.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiDestinazione");
	            }                
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiDestinazione");
            }
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
            {
              Vector<TipoDettaglioUsoVO>  vDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoParticellaVO.getIdUtilizzo(),
                utilizzoParticellaVO.getIdTipoDestinazione());
			        if((vDettaglioUso != null)
			          && (vDettaglioUso.size() > 0))
			        {
			          for(int d = 0; d < vDettaglioUso.size(); d++) 
			          {
			            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiDettaglioUso");
			            htmpl.set(nomeBlocco+"blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
			            String descrizione = null;
			            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
			            {
			              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoDettaglioUsoVO.getDescrizione();
			            }
			            htmpl.set(nomeBlocco+"blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUso()))
			            {
			              if(utilizzoParticellaVO.getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiDettaglioUso");
	            }
			      }
			      else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiDettaglioUso");
            }
			      
			      if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
            {
              Vector<TipoQualitaUsoVO>  vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoParticellaVO.getIdUtilizzo(),
                utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso());
			        if((vQualitaUso != null) && (vQualitaUso.size() > 0))
			        {
			          for(int d = 0; d < vQualitaUso.size(); d++) 
			          {
			            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiQualitaUso");
			            htmpl.set(nomeBlocco+"blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
			            String descrizione = null;
			            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
			            {
			              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
			            }
			            htmpl.set(nomeBlocco+"blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUso()))
			            {
			              if(utilizzoParticellaVO.getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiQualitaUso.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiQualitaUso");
	            } 
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiQualitaUso");
            } 
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
            {
              Vector<TipoVarietaVO> tipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoParticellaVO.getIdUtilizzo(),
                utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), utilizzoParticellaVO.getIdTipoQualitaUso());
			        if(tipoVarieta != null)
			        {
			          for(int l = 0; l < tipoVarieta.size(); l++) 
			          {
			            TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
			            htmpl.newBlock(nomeBlocco+"blkTipiVarieta");
			            htmpl.set(nomeBlocco+"blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
			            String descrizione = "";
			            if(tipoVarietaVO.getDescrizione().length() > 20) 
			            {
			              descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoVarietaVO.getDescrizione();
			            }
			            htmpl.set(nomeBlocco+"blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
			            
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdVarieta())) 
			            {
			              if(utilizzoParticellaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiVarieta.selected", "selected=\"selected\"", null);
			              }
			            }             
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiVarieta");
	            }            
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiVarieta");
            }                                           
            
            if(Validator.isNotEmpty(conduzioneParticellaVO.getIdTitoloPossesso()) 
	            && (conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0))
	          {}
	          else
	          {
	            htmpl.set(nomeBlocco+"supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
	          }
	          
	          
	          if(Validator.isEmpty(utilizzoParticellaVO.getDichiarabileEfa())
	            || (Validator.isNotEmpty(utilizzoParticellaVO.getDichiarabileEfa()))
	               && !"S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa()))
	          {
	          
		          if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
	            {
	              Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoParticellaVO.getIdUtilizzo(),
	                utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), utilizzoParticellaVO.getIdTipoQualitaUso(),
	                utilizzoParticellaVO.getIdVarieta());
				        if(vTipoPeriodoSemina != null)
				        {
				          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
				          {
				            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
				            htmpl.newBlock(nomeBlocco+"blkTipoPeriodoSemina");
				            htmpl.set(nomeBlocco+"blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
				            htmpl.set(nomeBlocco+"blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
				            
				            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoPeriodoSemina())) 
				            {
				              if(utilizzoParticellaVO.getIdTipoPeriodoSemina().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
				              {
				                htmpl.set(nomeBlocco+"blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
				              }
				            }             
				          }
				        }
				        else
	              {
	                htmpl.newBlock(nomeBlocco+"blkNoTipoPeriodoSemina");
	              }       
	            }
	            else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipoPeriodoSemina");
	            } 
	            
	            CatalogoMatriceVO catalogoMatricePraticVO = null;
	            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
	            {
	              catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(),
		              utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
		              utilizzoParticellaVO.getIdTipoQualitaUso());
		            Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
		            Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
		            
				        if(vPraticaMantenim != null)
				        {
				          for(int l = 0; l < vPraticaMantenim.size(); l++) 
				          {
				            TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vPraticaMantenim.get(l);
				            htmpl.newBlock(nomeBlocco+"blkTipoPraticaMantenimento");
				            htmpl.set(nomeBlocco+"blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
				            htmpl.set(nomeBlocco+"blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
				            
				            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdPraticaMantenimento())) 
				            {
				              if(utilizzoParticellaVO.getIdPraticaMantenimento().compareTo(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento())) == 0) 
				              {
				                htmpl.set(nomeBlocco+"blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
				              }
				            }             
				          }
				        }
				        else
	              {
	                htmpl.newBlock(nomeBlocco+"blkNoTipoPraticaMantenimento");
	              }   
	            }
	            else
              {
                htmpl.newBlock(nomeBlocco+"blkNoTipoPraticaMantenimento");
              } 
	            
	            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
	            {
	              if(Validator.isNotEmpty(vTipoSemina)) 
	              {
		              for(int e=0;e<vTipoSemina.size(); e++) 
						      {
						        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
						        htmpl.newBlock(nomeBlocco+"blkTipoSemina");
						        htmpl.set(nomeBlocco+"blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
						        htmpl.set(nomeBlocco+"blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
						        if(Validator.isNotEmpty(utilizzoParticellaVO.getIdSemina())
						            && utilizzoParticellaVO.getIdSemina().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
						        {
						          htmpl.set(nomeBlocco+"blkTipoSemina.selected", "selected=\"selected\"", null);
						        }				    
						      }
						    }
						    else
	              {
	                htmpl.newBlock(nomeBlocco+"blkNoTipoSemina");
	              } 
					      
					      if(Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazione()))
				        { 
				          htmpl.set(nomeBlocco+"dataInizioDestinazione", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazione()));
				        }
				        
				        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazione()))
				        { 
				          htmpl.set(nomeBlocco+"dataFineDestinazione",  DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazione()));
				        }                
	            }
	            else
              {
                htmpl.newBlock(nomeBlocco+"blkNoTipoSemina");
              }   
	            
	            
	            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo())
	              && Validator.isNotEmpty(catalogoMatricePraticVO))
	            {	              
		            
		            if(Validator.isNotEmpty(storicoParticellaVO.getParticellaCertificataVO())
		              && Validator.isNotEmpty(storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata()))
		            {
		              utilizzoParticellaVO.setSupEleggibile(anagFacadeClient.getSupEleggPlSqlTotale(
		                storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatricePraticVO.getIdCatalogoMatrice()));
		              utilizzoParticellaVO.setSupEleggibileNetta(anagFacadeClient.getSupEleggNettaPlSqlTotale(
		                storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatricePraticVO.getIdCatalogoMatrice()));
		            }
		            else
		            {
		              utilizzoParticellaVO.setSupEleggibile(new BigDecimal(0));
		              utilizzoParticellaVO.setSupEleggibileNetta(new BigDecimal(0));
		            }
		              
		            BigDecimal supNetta = catalogoMatricePraticVO.getCoefficienteRiduzione();
		            String supUtilizzataStr = utilizzoParticellaVO.getSuperficieUtilizzata();
		                        
		            if(!(Validator.validateDouble(supUtilizzataStr, 999999.9999) == null)) 
		            {                  
		              supNetta = supNetta.multiply(new BigDecimal(
		                supUtilizzataStr.replace(',','.')));
		              utilizzoParticellaVO.setSupNetta(supNetta);
		            }
		            else
		            {
		              utilizzoParticellaVO.setSupNetta(new BigDecimal(0));
		            }
		            
		            htmpl.set(nomeBlocco+"supNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupNetta()));
		            htmpl.set(nomeBlocco+"supEleggibile", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibile()));
	              htmpl.set(nomeBlocco+"supEleggibileNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibileNetta()));
		          }
		        }               
	                
	              
	          
	          if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) 
	          {
	            for(int f = 0; f < elencoTipiUsoSuoloSecondario.size(); f++) 
	            {
	              TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(f);
	              htmpl.newBlock(nomeBlocco+"blkTipiUsoSuoloSecondario");
	              htmpl.set(nomeBlocco+"blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
	              htmpl.set(nomeBlocco+"blkTipiUsoSuoloSecondario.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
	              String descrizione = null;
	              if(tipoUtilizzoVO.getDescrizione().length() > 20) 
	              {
	                descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoUtilizzoVO.getDescrizione();
	              }
	              htmpl.set(nomeBlocco+"blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
	              if(utilizzoParticellaVO.getIdUtilizzoSecondario() != null 
	                && utilizzoParticellaVO.getIdUtilizzoSecondario().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
	              {
	                htmpl.set(nomeBlocco+"blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	          
	          
	          if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario());
			        if((vDestinazione != null) && (vDestinazione.size() > 0))
			        {
			          for(int d = 0; d < vDestinazione.size(); d++) 
			          {
			            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiDestinazioneSecondario");
			            htmpl.set(nomeBlocco+"blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
			            String descrizione = "";
			            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
			            {
			              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
			            }
			            htmpl.set(nomeBlocco+"blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazioneSecondario()))
			            {
			              if(utilizzoParticellaVO.getIdTipoDestinazioneSecondario().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiDestinazioneSecondario");
	            } 
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiDestinazioneSecondario");
            } 
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              Vector<TipoDettaglioUsoVO>  vDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(),
                utilizzoParticellaVO.getIdTipoDestinazioneSecondario());
			        if((vDettaglioUso != null) && (vDettaglioUso.size() > 0))
			        {
			          for(int d = 0; d < vDettaglioUso.size(); d++) 
			          {
			            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiDettaglioUsoSecondario");
			            htmpl.set(nomeBlocco+"blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
			            String descrizione = "";
			            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
			            {
			              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoDettaglioUsoVO.getDescrizione();
			            }
			            htmpl.set(nomeBlocco+"blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario()))
			            {
			              if(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiDettaglioUsoSecondario");
	            }
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiDettaglioUsoSecondario");
            } 
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              Vector<TipoQualitaUsoVO>  vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(),
                utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario());
			        if((vQualitaUso != null) && (vQualitaUso.size() > 0))
			        {
			          for(int d = 0; d < vQualitaUso.size(); d++) 
			          {
			            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
			            htmpl.newBlock(nomeBlocco+"blkTipiQualitaUsoSecondario");
			            htmpl.set(nomeBlocco+"blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
			            String descrizione = "";
			            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
			            {
			              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
			            }
			            else 
			            {
			              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
			            }
			            htmpl.set(nomeBlocco+"blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario()))
			            {
			              if(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
			              }
			            }
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiQualitaUsoSecondario");
	            }
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiQualitaUsoSecondario");
            }
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              Vector<TipoVarietaVO> tipoVarieta =  gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(),
                utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario());
		          if(tipoVarieta != null) 
		          {
		            for(int h = 0; h < tipoVarieta.size(); h++) 
		            {
		              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
		              htmpl.newBlock(nomeBlocco+"blkTipiVarietaSecondaria");
		              htmpl.set(nomeBlocco+"blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
		              String descrizione = "";
		              if(tipoVarietaVO.getDescrizione().length() > 20) 
		              {
		                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
		              }
		              else 
		              {
		                descrizione = tipoVarietaVO.getDescrizione();
		              }
		              htmpl.set(nomeBlocco+"blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
		              if(Validator.isNotEmpty(utilizzoParticellaVO.getIdVarietaSecondaria()))  
		              {
		                if(utilizzoParticellaVO.getIdVarietaSecondaria().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
		                {
		                  htmpl.set(nomeBlocco+"blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
		                }
		              }
		            }
		          }
		          else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipiVarietaSecondaria");
	            }
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipiVarietaSecondaria");
            }
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoParticellaVO.getIdUtilizzoSecondario(),
                utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario(),
                utilizzoParticellaVO.getIdVarietaSecondaria());
			        if(vTipoPeriodoSemina != null)
			        {
			          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
			          {
			            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
			            htmpl.newBlock(nomeBlocco+"blkTipoPeriodoSeminaSecondario");
			            htmpl.set(nomeBlocco+"blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
			            htmpl.set(nomeBlocco+"blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
			            
			            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario())) 
			            {
			              if(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
			              {
			                htmpl.set(nomeBlocco+"blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
			              }
			            }             
			          }
			        }
			        else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipoPeriodoSeminaSecondario");
	            }         
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipoPeriodoSeminaSecondario");
            }
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario())) 
            {
              if(Validator.isNotEmpty(vTipoSemina)) 
              {
	              for(int e=0;e<vTipoSemina.size(); e++) 
					      {
					        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
					        htmpl.newBlock(nomeBlocco+"blkTipoSeminaSecondario");
					        htmpl.set(nomeBlocco+"blkTipoSeminaSecondario.idTipoSeminaSecondario", ""+tipoSeminaVO.getIdTipoSemina());
					        htmpl.set(nomeBlocco+"blkTipoSeminaSecondario.descrizione", tipoSeminaVO.getDescrizioneSemina());
					        if(Validator.isNotEmpty(utilizzoParticellaVO.getIdSeminaSecondario())
					          && utilizzoParticellaVO.getIdSeminaSecondario().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
					        {
					          htmpl.set(nomeBlocco+"blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
					        }
					      }
					    }
					    else
	            {
	              htmpl.newBlock(nomeBlocco+"blkNoTipoSeminaSecondario");
	            }
					    
					    if(Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazioneSec()))
			        { 
			          htmpl.set(nomeBlocco+"dataInizioDestinazioneSec", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazioneSec()));
			        }
			        
			        if(Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazioneSec()))
			        { 
			          htmpl.set(nomeBlocco+"dataFineDestinazioneSec",  DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazioneSec()));
			        }     
            
            }
            else
            {
              htmpl.newBlock(nomeBlocco+"blkNoTipoSeminaSecondario");
            }
            
	          if(Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) {
	            htmpl.set(nomeBlocco+"supUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
	          }
	          
	          numeroTotaleUtilizzi++;
          }
        
	        if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
	        {       
	          //particelle normali
	          if(elencoCasiParticolari != null && elencoCasiParticolari.length > 0) 
	          {
	            for(int l = 0; l < elencoCasiParticolari.length; l++) 
	            {
	              it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoCasiParticolari[l];
	              htmpl.newBlock("blkTipiCasoParticolare");
	              htmpl.set("blkTipiCasoParticolare.idCasoParticolare", code.getCode().toString());
	              htmpl.set("blkTipiCasoParticolare.descrizione", code.getCode().toString());
	              htmpl.set("blkTipiCasoParticolare.descCasiParticolari", code.getDescription());
	              if(storicoParticellaVO.getIdCasoParticolare() != null) {
	                if(storicoParticellaVO.getIdCasoParticolare().compareTo(Long.decode(code.getCode().toString())) == 0) {
	                  htmpl.set("blkTipiCasoParticolare.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	        }
	        //particella provvisorie
	        else
	        {
	          if(Validator.isNotEmpty(elencoCasiParticolariPartProv))
	          {
	            for(int l = 0; l < elencoCasiParticolariPartProv.size(); l++) 
	            {
	              CasoParticolareVO casoParticolareVO = elencoCasiParticolariPartProv.get(l);
	              htmpl.newBlock("blkTipiCasoParticolare");
	              htmpl.set("blkTipiCasoParticolare.idCasoParticolare", 
	                ""+casoParticolareVO.getIdCasoParticolare());
	              htmpl.set("blkTipiCasoParticolare.descrizione", 
	                ""+casoParticolareVO.getIdCasoParticolare());
	              htmpl.set("blkTipiCasoParticolare.descCasiParticolari", 
	                ""+casoParticolareVO.getDescrizione());
	              if(storicoParticellaVO.getIdCasoParticolare() != null) 
	              {
	                if(storicoParticellaVO.getIdCasoParticolare()
	                  .compareTo(new Long(casoParticolareVO.getIdCasoParticolare())) == 0) 
	                {
	                  htmpl.set("blkTipiCasoParticolare.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	        }
	        
	        
	        
        
        } //if riga
        
        
        
      }
    } ///if
    
  
  
	  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
