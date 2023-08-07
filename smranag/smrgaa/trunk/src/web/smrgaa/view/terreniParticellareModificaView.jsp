<%@page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.math.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareModifica.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	String urlRSDI = (String) SolmrConstants.get("RSDI_URL");

	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	StoricoParticellaVO[] elencoStoricoParticella = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticella");
	it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuolo");
	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazione");
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUso");
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUso");
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarieta");
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSemina");
  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = (Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>)request.getAttribute("elencoPerMantenim");
	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloSecondario");
	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioneSecondario");
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUsoSecondario");
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUsoSecondario");
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarietaSecondaria");
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSeminaSecondario");
  Vector<TipoSeminaVO> vTipoSemina = (Vector<TipoSeminaVO>)request.getAttribute("vTipoSemina");
	it.csi.solmr.dto.CodeDescription[] elencoCasiParticolari = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoCasiParticolari");
	Vector<CasoParticolareVO> elencoCasiParticolariPartProv = (Vector<CasoParticolareVO>)request.getAttribute("elencoCasiParticolariPartProv");
	Vector elencoErroriConduzioniStorico = (Vector)request.getAttribute("elencoErroriConduzioniStorico");
	Vector elencoErroriUtilizzi = (Vector)request.getAttribute("elencoErroriUtilizzi");
	HashMap<Long, TipoUtilizzoVO> elencoTipiUsoSuoloEfa = (HashMap<Long, TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloEfa");
	String onLoad = (String)request.getAttribute("onLoad");
	String regimeTerreniModifica = request.getParameter("regimeTerreniModifica");
	  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO"); 

	
	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
	
	if(Validator.isNotEmpty(messaggioErrore)) 
	{
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	else 
  {
		if(Validator.isNotEmpty(onLoad)) 
    {
			htmpl.set("onLoad", onLoad);
		}
		htmpl.newBlock("blkDati");
		int contatore = 0;
    int numeroTotaleUtilizzi = 0;
    int countNoEfa = 0;
    if(Validator.isNotEmpty(elencoStoricoParticella))
    {
  		for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
  			htmpl.newBlock("blkDati.blkElencoDati");
  			htmpl.set("blkDati.blkElencoDati.numero", String.valueOf(i));
  			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticella[i];
  			ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
  			UtilizzoParticellaVO[] elencoUtilizzi = (UtilizzoParticellaVO[])conduzioneParticellaVO.getElencoUtilizzi();
  			htmpl.set("blkDati.blkElencoDati.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
  			if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
  				htmpl.set("blkDati.blkElencoDati.siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
  			}
  			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
  				htmpl.set("blkDati.blkElencoDati.sezione", storicoParticellaVO.getSezione());
  			}
  			htmpl.set("blkDati.blkElencoDati.foglio", storicoParticellaVO.getFoglio());
  			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
  				htmpl.set("blkDati.blkElencoDati.particella", storicoParticellaVO.getParticella());
  			}
  			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
  				htmpl.set("blkDati.blkElencoDati.subalterno", storicoParticellaVO.getSubalterno());
  			}
  			htmpl.set("blkDati.blkElencoDati.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
        htmpl.set("blkDati.blkElencoDati.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
  			
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
        htmpl.set("blkDati.blkElencoDati.tooltipDescElegFit", tooltipDescElegFit);
        htmpl.set("blkDati.blkElencoDati.classiEleggibilita", classiEleggibilita, null);
        
        /*if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO())
           && Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
			     && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
			     && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
			     && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
			  {
			    htmpl.set("blkDati.blkElencoDati.stabilizzato", "true");
			  }*/
        
             
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
        htmpl.set("blkDati.blkElencoDati.controlliP", titleGis);
        
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
        htmpl.set("blkDati.blkElencoDati.immaginiControlliP", immaginiControlliP);
        
        
        
        htmpl.set("blkDati.blkElencoDati.idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
		htmpl.set("blkDati.blkElencoDati.idAzienda", anagAziendaVO.getIdAzienda().toString());
		htmpl.set("blkDati.blkElencoDati.urlRSDI", urlRSDI); 		        
  			if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) {
  				for(int a = 0; a < elencoTipoTitoloPossesso.length; a++) {
  					it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTipoTitoloPossesso[a];
  					htmpl.newBlock("blkDati.blkElencoDati.blkTitoliPossesso");
  					htmpl.set("blkDati.blkElencoDati.blkTitoliPossesso.idTitoloPossesso", code.getCode().toString());
  					htmpl.set("blkDati.blkElencoDati.blkTitoliPossesso.descrizione", code.getCode().toString());
  					htmpl.set("blkDati.blkElencoDati.blkTitoliPossesso.descTitoloPossesso", code.getDescription());
  					if(conduzioneParticellaVO.getIdTitoloPossesso() != null && conduzioneParticellaVO.getIdTitoloPossesso().compareTo(Long.decode(code.getCode().toString())) == 0) {
  						htmpl.set("blkDati.blkElencoDati.blkTitoliPossesso.selected", "selected=\"selected\"",null);
  					}
  				}
  			}
  			
  			if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieCondotta())) {
          htmpl.set("blkDati.blkElencoDati.supCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
        }
  			
  			if(Validator.isNotEmpty(regimeTerreniModifica)
  			  && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")))
  			{
  			  htmpl.set("blkDati.blkElencoDati.percentualePossesso", request.getParameterValues("percentualePossesso")[i]);
  			}
  			else
  			{
	  			if(Validator.isNotEmpty(conduzioneParticellaVO.getPercentualePossesso())) {
	          htmpl.set("blkDati.blkElencoDati.percentualePossesso", Formatter.formatDouble2(conduzioneParticellaVO.getPercentualePossesso()));
	        }
	      }
  			
  			if(Validator.isNotEmpty(conduzioneParticellaVO.getIdTitoloPossesso()) 
  			  && (conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0))
	      {
	        htmpl.set("blkDati.blkElencoDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
	      }
	      else if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica())) {
  				htmpl.set("blkDati.blkElencoDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
  			}
  			
  			
  			htmpl.set("blkDati.blkElencoDati.rowspan", String.valueOf(elencoUtilizzi.length));
  			String nomeBlocco = null;
  			
  			for(int b = 0; b < elencoUtilizzi.length; b++) 
        {
  				if(b > 0) 
          {
  					nomeBlocco = "blkDati.blkElencoDati.blkElencoUtilizzi";
  					htmpl.newBlock(nomeBlocco);
  				}
  				else 
          {
  					nomeBlocco = "blkDati.blkElencoDati";
  				} 				
          
          //varibile per identificare la riga dell'utilizzo
          htmpl.set(nomeBlocco+".numTotaleUtilizzo", String.valueOf(numeroTotaleUtilizzi));
          
          if((storicoParticellaVO.getParticellaCertificataVO() != null)
            && (storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata() != null))
          {
            htmpl.set(nomeBlocco+".idParticellaCertificata", ""+storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata());
          }
  				UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizzi[b];
  				
  				
  				
  				boolean flagUtilEfa = false;
	        if("S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa()))
	        {
	          flagUtilEfa = true;
	        }
	         
	         
	         
	        if(flagUtilEfa)
	        {
	          htmpl.set(nomeBlocco+".disabledUt", "disabled=\"disabled\"", null);
	        }
	        else
	        {
	          htmpl.newBlock(nomeBlocco+".blkNoDisabled");
	          htmpl.set(nomeBlocco+".blkNoDisabled.numeroUtilizzi", String.valueOf(b));
	          htmpl.set(nomeBlocco+".blkNoDisabled.numTotaleUtilizzo", String.valueOf(numeroTotaleUtilizzi));
	          /*if((storicoParticellaVO.getParticellaCertificataVO() != null)
	            && (storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata() != null))
	          {
	            htmpl.set(nomeBlocco+".blkNoDisabled.idParticellaCertificata",  ""+storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata());
	          }*/           
	        }
  				
  				
  				
  				htmpl.set(nomeBlocco+".numeroUtilizzi", String.valueOf(b));
  				
  				
  				
  				if(!flagUtilEfa)
  				{
	  				if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
	          {
	  					for(int c = 0; c < elencoTipiUsoSuolo.size(); c++) 
	            {
	  						TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(c);
	  						htmpl.newBlock(nomeBlocco+".blkTipiUsoSuolo");
	  						htmpl.set(nomeBlocco+".blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
	  						String descrizione = null;
	  						if(tipoUtilizzoVO.getDescrizione().length() > 20) {
	  							descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
	  						}
	  						else {
	  							descrizione = tipoUtilizzoVO.getDescrizione();
	  						}
	  						htmpl.set(nomeBlocco+".blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
	  						htmpl.set(nomeBlocco+".blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
	  						if(utilizzoParticellaVO.getIdUtilizzo() != null && utilizzoParticellaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
	  							htmpl.set(nomeBlocco+".blkTipiUsoSuolo.selected", "selected=\"selected\"",null);
	                //htmpl.set(nomeBlocco+".coefficienteRiduzione", Formatter.formatDouble2(tipoUtilizzoVO.getCoefficienteRiduzione()));
	  						}
	  					}
	  				}
	  			}
	  			else
	  			{
	  			  if(Validator.isNotEmpty(elencoTipiUsoSuoloEfa)
	  			    && Validator.isNotEmpty(elencoTipiUsoSuoloEfa.get(utilizzoParticellaVO.getIdUtilizzo()))) 
            {
              TipoUtilizzoVO tipoUtilizzoVO = elencoTipiUsoSuoloEfa.get(utilizzoParticellaVO.getIdUtilizzo());
              htmpl.newBlock(nomeBlocco+".blkTipiUsoSuolo");
              htmpl.set(nomeBlocco+".blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
              String descrizione = null;
              htmpl.set(nomeBlocco+".blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
              if(tipoUtilizzoVO.getDescrizione().length() > 20) {
                descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
              }
              else {
                descrizione = tipoUtilizzoVO.getDescrizione();
              }
              htmpl.set(nomeBlocco+".blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
              htmpl.set(nomeBlocco+".blkTipiUsoSuolo.selected", "selected=\"selected\"",null);
              //htmpl.set(nomeBlocco+".coefficienteRiduzione", Formatter.formatDouble2(tipoUtilizzoVO.getCoefficienteRiduzione()));
            }
	  			
	  			}
	  			
	  			
	  			// Carico la combo della destinazione solo se l'utente ha selezionato il tipo
          // uso suolo corrispondente
          if(elencoDestinazione != null && elencoDestinazione.size() > 0 && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
            Enumeration<Integer> enumeraDestinazione = elencoDestinazione.keys();
            while(enumeraDestinazione.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraDestinazione.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoDestinazione.get(key))
                  && (elencoDestinazione.get(key).size() > 0))
                {
                  Vector<TipoDestinazioneVO> vTipoDestinazione = (Vector<TipoDestinazioneVO>)elencoDestinazione.get(key);
                  for(int d = 0; d < vTipoDestinazione.size(); d++) 
                  {
                    TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiDestinazione");
                    htmpl.set(nomeBlocco+".blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
                    htmpl.set(nomeBlocco+".blkTipiDestinazione.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
                    String descrizione = null;
                    if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
                    {
                      descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
                    }
                    htmpl.set(nomeBlocco+".blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoDestinazione() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoDestinazione().compareTo(tipoDestinazioneVO.getIdTipoDestinazione()) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiDestinazione.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiDestinazione");
			          } 
                   
              }
            }
          }
          else
		      {
		        htmpl.newBlock(nomeBlocco+".blkNoTipiDestinazione");
		      } 
          
          
          if(elencoDettUso != null && elencoDettUso.size() > 0 
            && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
            Enumeration<Integer> enumeraDettaUso = elencoDettUso.keys();
            while(enumeraDettaUso.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraDettaUso.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoDettUso.get(key))
                  && (elencoDettUso.get(key).size() > 0)) 
                {
                  Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(key);
                  for(int d = 0; d < vDettaglioUso.size(); d++) 
                  {
                    TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiDettaglioUso");
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUso.descCompleta", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
                    String descrizione = null;
                    if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
                    {
                      descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoDettaglioUsoVO.getDescrizione();
                    }
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoDettaglioUso() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiDettaglioUso");
			          }     
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiDettaglioUso");
          } 
          
          if(elencoQualitaUso != null && elencoQualitaUso.size() > 0 
            && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
            Enumeration<Integer> enumeraQualitaUso = elencoQualitaUso.keys();
            while(enumeraQualitaUso.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraQualitaUso.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoQualitaUso.get(key))
                  && (elencoQualitaUso.get(key).size() > 0)) 
                {
                  Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(key);
                  for(int d = 0; d < vQualitaUso.size(); d++) 
                  {
                    TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiQualitaUso");
                    htmpl.set(nomeBlocco+".blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
                    htmpl.set(nomeBlocco+".blkTipiQualitaUso.descCompleta", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+tipoQualitaUsoVO.getDescrizioneQualitaUso());
                    String descrizione = null;
                    if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
                    {
                      descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
                    }
                    htmpl.set(nomeBlocco+".blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoQualitaUso() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiQualitaUso.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiQualitaUso");
			          }     
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiQualitaUso");
          } 
          
	  			
	  			
	  			
  				// Carico la combo della varietà solo se l'utente ha selezionato il tipo
  				// uso suolo corrispondente
  				if(elencoVarieta != null && elencoVarieta.size() > 0 && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
  					Enumeration<Integer> enumeraVarieta = elencoVarieta.keys();
  					while(enumeraVarieta.hasMoreElements()) 
            {
  						Integer key = (Integer)enumeraVarieta.nextElement();
  						if(key.compareTo(new Integer(contatore)) == 0) 
              {
  							if(Validator.isNotEmpty(elencoVarieta.get(key))
  							  && (elencoVarieta.get(key).size() > 0))
                {
  								Vector<TipoVarietaVO> tipoVarieta = elencoVarieta.get(key);
  								for(int d = 0; d < tipoVarieta.size(); d++) 
                  {
  									TipoVarietaVO tipoVarietaVO = tipoVarieta.get(d);
  									htmpl.newBlock(nomeBlocco+".blkTipiVarieta");
  									htmpl.set(nomeBlocco+".blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
  									htmpl.set(nomeBlocco+".blkTipiVarieta.descCompleta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
  									String descrizione = null;
  									if(tipoVarietaVO.getDescrizione().length() > 20) 
                    {
  										descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
  									}
  									else 
                    {
  										descrizione = tipoVarietaVO.getDescrizione();
  									}
  									htmpl.set(nomeBlocco+".blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
  									if(utilizzoParticellaVO.getIdVarieta() != null) 
                    {
  										if(utilizzoParticellaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
                      {
  											htmpl.set(nomeBlocco+".blkTipiVarieta.selected", "selected=\"selected\"",null);
  										}
  									}
  								}
  							}
  							else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiVarieta");
			          } 
  									
  						}
  					}
  				}
  				else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiVarieta");
          } 
  				
  				
  				// Carico la combo della varietà solo se l'utente ha selezionato il tipo
          // uso suolo corrispondente
          if(elencoPerSemina != null && elencoPerSemina.size() > 0 && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
            Enumeration<Integer> enumeraSemina = elencoPerSemina.keys();
            while(enumeraSemina.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraSemina.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoPerSemina.get(key))
                  && (elencoPerSemina.get(key).size() > 0))
                {
                  Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = elencoPerSemina.get(key);
                  for(int d = 0; d < vTipoPeriodoSemina.size(); d++) 
                  {
                    TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipoPeriodoSemina");
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSemina.descCompleta", tipoPeriodoSeminaVO.getDescrizione());
                    String descrizione = null;
                    if(tipoPeriodoSeminaVO.getDescrizione().length() > 20) 
                    {
                      descrizione = tipoPeriodoSeminaVO.getDescrizione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoPeriodoSeminaVO.getDescrizione();
                    }
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSemina.descrizione", descrizione);
                    if(utilizzoParticellaVO.getIdTipoPeriodoSemina() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoPeriodoSemina().compareTo(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipoPeriodoSemina.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipoPeriodoSemina");
			          }
                    
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipoPeriodoSemina");
          } 
          
          
          if(elencoPerMantenim != null && elencoPerMantenim.size() > 0 && utilizzoParticellaVO.getIdUtilizzo() != null) 
          {
            Enumeration<Integer> enumeraMantenim = elencoPerMantenim.keys();
            while(enumeraMantenim.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraMantenim.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoPerMantenim.get(key))
                  && (elencoPerMantenim.get(key).size() > 0))
                {
                  Vector<TipoPraticaMantenimentoVO> vTipoPratMantenim = elencoPerMantenim.get(key);
                  for(int d = 0; d < vTipoPratMantenim.size(); d++) 
                  {
                    TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPratMantenim.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipoPraticaMantenimento");
                    htmpl.set(nomeBlocco+".blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
                    htmpl.set(nomeBlocco+".blkTipoPraticaMantenimento.descCompleta", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
                    String descrizione = null;
                    if(tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim().length() > 20) 
                    {
                      descrizione = tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim();
                    }
                    htmpl.set(nomeBlocco+".blkTipoPraticaMantenimento.descrizione", descrizione);
                    if(utilizzoParticellaVO.getIdPraticaMantenimento() != null) 
                    {
                      if(utilizzoParticellaVO.getIdPraticaMantenimento().compareTo(tipoPraticaMantenimentoVO.getIdPraticaMantenimento()) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipoPraticaMantenimento.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipoPraticaMantenimento");
			          }
                    
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipoPraticaMantenimento");
          }
          
          
          
          //abilita semina
			    boolean abilitaSemina = false;
			    if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()) && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazione())  
            && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUso()) && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUso())
            && Validator.isNotEmpty(utilizzoParticellaVO.getIdVarieta()) && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoPeriodoSemina()))
			    {
			      abilitaSemina = true;
			    }
			    
			    
			    if(abilitaSemina)
			    {
			      for(int e=0;e<vTipoSemina.size(); e++) 
			      {
			        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
			        htmpl.newBlock(nomeBlocco+".blkTipoSemina");
			        htmpl.set(nomeBlocco+".blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
			        htmpl.set(nomeBlocco+".blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
			        if(utilizzoParticellaVO.getIdSemina() != null)
			        {
			          if(utilizzoParticellaVO.getIdSemina().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
			          {
			            htmpl.set(nomeBlocco+".blkTipoSemina.selected", "selected=\"selected\"", null);
			          }
			        }
			      }
			    }
			    else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipoSemina");
          } 
			    
			    
			    if(abilitaSemina)
			    {
			      htmpl.set(nomeBlocco+".dataInizioDestinazione", utilizzoParticellaVO.getDataInizioDestinazioneStr());
			      htmpl.set(nomeBlocco+".dataFineDestinazione", utilizzoParticellaVO.getDataFineDestinazioneStr());			      
			    }
			    else
			    {
			      htmpl.set(nomeBlocco+".readOnlyDataInizioDestinazione", "readOnly=\"true\"", null);
			      htmpl.set(nomeBlocco+".readOnlyDataFineDestinazione", "readOnly=\"true\"", null);
			    }                  
          
          
          
  				
  				      
	        
	        htmpl.set(nomeBlocco+".coefficienteRiduzione", Formatter.formatDouble2(utilizzoParticellaVO.getCoefficienteRiduzione()));
  				
  				
  				if(Validator.isNotEmpty(conduzioneParticellaVO.getIdTitoloPossesso()) 
              && (conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0))
          {}
          else
          {
	  				if(!flagUtilEfa 
	  				  && Validator.isNotEmpty(regimeTerreniModifica)
	  				  && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")))
	  				{
	  				  htmpl.set(nomeBlocco+".supUtilizzata", request.getParameterValues("supUtilizzata")[countNoEfa]);
	  				}
	  				else
			      {
			        htmpl.set(nomeBlocco+".supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
			      }
			    }
  				
          
          htmpl.set(nomeBlocco+".supNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupNetta()));
          htmpl.set(nomeBlocco+".supEleggibile", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibile()));
          htmpl.set(nomeBlocco+".supEleggibileNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibileNetta()));
          
          
          if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) 
          {
  					for(int f = 0; f < elencoTipiUsoSuoloSecondario.size(); f++) 
  					{
  						TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(f);
  						htmpl.newBlock(nomeBlocco+".blkTipiUsoSuoloSecondario");
  						htmpl.set(nomeBlocco+".blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
  						htmpl.set(nomeBlocco+".blkTipiUsoSuoloSecondario.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
  						String descrizione = null;
  						if(tipoUtilizzoVO.getDescrizione().length() > 20) 
  						{
  							descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
  						}
  						else 
  						{
  							descrizione = tipoUtilizzoVO.getDescrizione();
  						}
  						htmpl.set(nomeBlocco+".blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
  						if(utilizzoParticellaVO.getIdUtilizzoSecondario() != null 
  						  && utilizzoParticellaVO.getIdUtilizzoSecondario().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
  						{
  							htmpl.set(nomeBlocco+".blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"",null);
  						}
  					}
  				}
  				
  				
  				if(elencoDestinazioneSecondario != null && elencoDestinazioneSecondario.size() > 0 
            && utilizzoParticellaVO.getIdUtilizzoSecondario() != null) 
          {
            Enumeration<Integer> enumeraDestinazione = elencoDestinazioneSecondario.keys();
            while(enumeraDestinazione.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraDestinazione.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoDestinazioneSecondario.get(key))
                  && (elencoDestinazioneSecondario.get(key).size() > 0)) 
                {
                  Vector<TipoDestinazioneVO>  vDestinazione = elencoDestinazioneSecondario.get(key);
                  for(int d = 0; d < vDestinazione.size(); d++) 
                  {
                    TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiDestinazioneSecondario");
                    htmpl.set(nomeBlocco+".blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
                    htmpl.set(nomeBlocco+".blkTipiDestinazioneSecondario.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
                    String descrizione = null;
                    if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
                    {
                      descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
                    }
                    htmpl.set(nomeBlocco+".blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoDestinazioneSecondario() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoDestinazioneSecondario().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiDestinazioneSecondario");
			          }  
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiDestinazioneSecondario");
          } 
          
          
          if(elencoDettUsoSecondario != null && elencoDettUsoSecondario.size() > 0 
            && utilizzoParticellaVO.getIdUtilizzoSecondario() != null) 
          {
            Enumeration<Integer> enumeraDettaUso = elencoDettUsoSecondario.keys();
            while(enumeraDettaUso.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraDettaUso.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoDettUsoSecondario.get(key))
                  && (elencoDettUsoSecondario.get(key).size() > 0)) 
                {
                  Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(key);
                  for(int d = 0; d < vDettaglioUso.size(); d++) 
                  {
                    TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiDettaglioUsoSecondario");
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUsoSecondario.descCompleta", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
                    String descrizione = null;
                    if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
                    {
                      descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoDettaglioUsoVO.getDescrizione();
                    }
                    htmpl.set(nomeBlocco+".blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiDettaglioUsoSecondario");
			          }  
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiDettaglioUsoSecondario");
          } 
          
          if(elencoQualitaUsoSecondario != null && elencoQualitaUsoSecondario.size() > 0 
            && utilizzoParticellaVO.getIdUtilizzoSecondario() != null) 
          {
            Enumeration<Integer> enumeraQualitaUso = elencoQualitaUsoSecondario.keys();
            while(enumeraQualitaUso.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraQualitaUso.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoQualitaUsoSecondario.get(key))
                  && (elencoQualitaUsoSecondario.get(key).size() > 0)) 
                {
                  Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(key);
                  for(int d = 0; d < vQualitaUso.size(); d++) 
                  {
                    TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipiQualitaUsoSecondario");
                    htmpl.set(nomeBlocco+".blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
                    htmpl.set(nomeBlocco+".blkTipiQualitaUsoSecondario.descCompleta", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+tipoQualitaUsoVO.getDescrizioneQualitaUso());
                    String descrizione = null;
                    if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
                    {
                      descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
                    }
                    htmpl.set(nomeBlocco+".blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
                    if(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiQualitaUsoSecondario");
			          } 
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiQualitaUsoSecondario");
          } 
  				
  				
  				// Carico la combo della varietà solo se l'utente ha selezionato il tipo
  				// uso suolo corrispondente
  				if(elencoVarietaSecondaria != null && elencoVarietaSecondaria.size() > 0 
  				   && utilizzoParticellaVO.getIdUtilizzoSecondario() != null) 
  				{
  					Enumeration<Integer> enumeraVarietaSecondaria = elencoVarietaSecondaria.keys();
  					while(enumeraVarietaSecondaria.hasMoreElements()) 
  					{
  						Integer key = (Integer)enumeraVarietaSecondaria.nextElement();
  						if(key.compareTo(new Integer(contatore)) == 0) 
  						{
  							if(Validator.isNotEmpty(elencoVarietaSecondaria.get(key))
  							  && (elencoVarietaSecondaria.get(key).size() > 0)) 
  							{
  								Vector<TipoVarietaVO> tipoVarieta = elencoVarietaSecondaria.get(key);
  								for(int d = 0; d < tipoVarieta.size(); d++) 
  								{
  									TipoVarietaVO tipoVarietaVO = tipoVarieta.get(d);
  									htmpl.newBlock(nomeBlocco+".blkTipiVarietaSecondaria");
  									htmpl.set(nomeBlocco+".blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
  									htmpl.set(nomeBlocco+".blkTipiVarietaSecondaria.descCompleta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
  									String descrizione = null;
  									if(tipoVarietaVO.getDescrizione().length() > 20) 
  									{
  										descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
  									}
  									else 
  									{
  										descrizione = tipoVarietaVO.getDescrizione();
  									}
  									htmpl.set(nomeBlocco+".blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
  									if(utilizzoParticellaVO.getIdVarietaSecondaria() != null) 
  									{
  										if(utilizzoParticellaVO.getIdVarietaSecondaria().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
  										{
  											htmpl.set(nomeBlocco+".blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
  										}
  									}
  								}
  							}
  							else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipiVarietaSecondaria");
			          }
  						}
  					}
  				}
  				else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipiVarietaSecondaria");
          }
  				
  				
  				
          
          
          if(elencoPerSeminaSecondario != null && elencoPerSeminaSecondario.size() > 0 
             && utilizzoParticellaVO.getIdUtilizzoSecondario() != null) 
          {
            Enumeration<Integer> enumeraPerSeminSecondaria = elencoPerSeminaSecondario.keys();
            while(enumeraPerSeminSecondaria.hasMoreElements()) 
            {
              Integer key = (Integer)enumeraPerSeminSecondaria.nextElement();
              if(key.compareTo(new Integer(contatore)) == 0) 
              {
                if(Validator.isNotEmpty(elencoPerSeminaSecondario.get(key))
                  && (elencoPerSeminaSecondario.get(key).size() > 0)) 
                {
                  Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = elencoPerSeminaSecondario.get(key);
                  for(int d = 0; d < vTipoPeriodoSemina.size(); d++) 
                  {
                    TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(d);
                    htmpl.newBlock(nomeBlocco+".blkTipoPeriodoSeminaSecondario");
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSeminaSecondario.descCompleta", tipoPeriodoSeminaVO.getDescrizione());
                    String descrizione = null;
                    if(tipoPeriodoSeminaVO.getDescrizione().length() > 20) 
                    {
                      descrizione = tipoPeriodoSeminaVO.getDescrizione().substring(0, 20);
                    }
                    else 
                    {
                      descrizione = tipoPeriodoSeminaVO.getDescrizione();
                    }
                    htmpl.set(nomeBlocco+".blkTipoPeriodoSeminaSecondario.descrizione", descrizione);
                    if(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario() != null) 
                    {
                      if(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario().compareTo(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()) == 0) 
                      {
                        htmpl.set(nomeBlocco+".blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"",null);
                      }
                    }
                  }
                }
                else
			          {
			            htmpl.newBlock(nomeBlocco+".blkNoTipoPeriodoSeminaSecondario");
			          }
              }
            }
          }
          else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipoPeriodoSeminaSecondario");
          }
          
          
          
          //abilita semina
			    boolean abilitaSeminaSec = false;
			    if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario()) && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazioneSecondario())
            && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario()) && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario())
            && Validator.isNotEmpty(utilizzoParticellaVO.getIdVarietaSecondaria()))  
			    {
			      abilitaSeminaSec = true;
			    } 
			    
			    
			    if(abilitaSeminaSec)
			    {
			      for(int e=0;e<vTipoSemina.size(); e++) 
			      {
			        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
			        htmpl.newBlock(nomeBlocco+".blkTipoSeminaSecondario");
			        htmpl.set(nomeBlocco+".blkTipoSeminaSecondario.idTipoSeminaSecondario", ""+tipoSeminaVO.getIdTipoSemina());
			        htmpl.set(nomeBlocco+".blkTipoSeminaSecondario.descrizione", tipoSeminaVO.getDescrizioneSemina());
			        if((utilizzoParticellaVO.getIdSeminaSecondario() != null)
			            && utilizzoParticellaVO.getIdSeminaSecondario().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
		          {
		            htmpl.set(nomeBlocco+".blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
		          }
			      }
			    }
			    else
          {
            htmpl.newBlock(nomeBlocco+".blkNoTipoSeminaSecondario");
          }
			    
			    if(abilitaSeminaSec)
			    {
			      htmpl.set(nomeBlocco+".dataInizioDestinazioneSec", utilizzoParticellaVO.getDataInizioDestinazioneSecStr());
            htmpl.set(nomeBlocco+".dataFineDestinazioneSec", utilizzoParticellaVO.getDataFineDestinazioneSecStr());     
			    }
			    else
			    {
			      htmpl.set(nomeBlocco+".readOnlyDataInizioDestinazioneSec", "readOnly=\"true\"", null);
			      htmpl.set(nomeBlocco+".readOnlyDataFineDestinazioneSec", "readOnly=\"true\"", null);
			    }          
  				
  				
  				
  				
  				
  				if(Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) {
  					htmpl.set(nomeBlocco+".supUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
  				}
  				
  				
  				// GESTIONE ERRORI UTILIZZI
  				if(elencoErroriUtilizzi != null && elencoErroriUtilizzi.size() > 0) {
  					ValidationErrors errors = (ValidationErrors)elencoErroriUtilizzi.elementAt(contatore);
  					if(errors != null && errors.size() > 0) {
  						Iterator iteraErrori = errors.getKeys();
  						while(iteraErrori.hasNext()) {
  			 				String key = (String)iteraErrori.next();
  			 			    if(Validator.isNotEmpty(errors.get(key))) {
  			 			    	Iterator iteraErr = errors.get(key);
  			 			    	if(iteraErr != null) {
  			 			    		ValidationError error = (ValidationError)iteraErr.next();
  			 			    		if(Validator.isNotEmpty(error.getMessage())) {
  			 			    			htmpl.set(nomeBlocco+".err_"+key,
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
  				
  				if(!flagUtilEfa)
          {
            countNoEfa++;
          }
  				contatore++;
          
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
	  					htmpl.newBlock("blkDati.blkElencoDati.blkTipiCasoParticolare");
	  					htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.idCasoParticolare", code.getCode().toString());
	  					htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.descrizione", code.getCode().toString());
	  					htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.descCasiParticolari", code.getDescription());
	  					if(storicoParticellaVO.getIdCasoParticolare() != null) {
	  						if(storicoParticellaVO.getIdCasoParticolare().compareTo(Long.decode(code.getCode().toString())) == 0) {
	  							htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.selected", "selected=\"selected\"",null);
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
              htmpl.newBlock("blkDati.blkElencoDati.blkTipiCasoParticolare");
              htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.idCasoParticolare", 
                ""+casoParticolareVO.getIdCasoParticolare());
              htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.descrizione", 
                ""+casoParticolareVO.getIdCasoParticolare());
              htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.descCasiParticolari", 
                ""+casoParticolareVO.getDescrizione());
              if(storicoParticellaVO.getIdCasoParticolare() != null) 
              {
                if(storicoParticellaVO.getIdCasoParticolare()
                  .compareTo(new Long(casoParticolareVO.getIdCasoParticolare())) == 0) 
                {
                  htmpl.set("blkDati.blkElencoDati.blkTipiCasoParticolare.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
	  		}
  			// GESTIONE ERRORI RELATIVI ALLO STORICO O ALLA CONDUZIONE
  			if(elencoErroriConduzioniStorico != null && elencoErroriConduzioniStorico.size() > 0) {
  				ValidationErrors errors = (ValidationErrors)elencoErroriConduzioniStorico.elementAt(i);
  				if(errors != null && errors.size() > 0) {
  					Iterator iter = htmpl.getVariableIterator();
  		 			while(iter.hasNext()) {
  		 				String key = (String)iter.next();
  		 			    if(key.startsWith("err_")) {
  		 			    	String property = key.substring(4);
  		 			    	Iterator errorIterator = errors.get(property);
  		 			    	if(errorIterator != null) {
  		 			    		ValidationError error = (ValidationError)errorIterator.next();
  		 			    		htmpl.set("blkDati.blkElencoDati.err_"+property,
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
		if(elencoCasiParticolari != null && elencoCasiParticolari.length > 0) {
			for(int l = 0; l < elencoCasiParticolari.length; l++) {
				it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoCasiParticolari[l];
				htmpl.newBlock("blkDati.blkLegendaCasiParticolari");
				htmpl.set("blkDati.blkLegendaCasiParticolari.idCasoParticolare", code.getCode().toString()+" - ");
				if(l == (elencoCasiParticolari.length - 1)) {
					htmpl.set("blkDati.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
				}
				else {
					htmpl.set("blkDati.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
				}
			}
		}
	}

%>
<%= htmpl.text()%>

