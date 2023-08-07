<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.*"%>
<%@ page import="it.csi.util.performance.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	
	//Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
	StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
	// START
	watcher.start();	

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareElenco.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	  String urlRSDI = (String) SolmrConstants.get("RSDI_URL");

	//TreeMap elencoPianiRiferimento = (TreeMap)request.getAttribute("elencoPianiRiferimento");
 	Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
 	Vector<ComuneVO> elencoComuni = (Vector<ComuneVO>)request.getAttribute("elencoComuni");
 	TipoMacroUsoVO[] elencoMacroUso = (TipoMacroUsoVO[])request.getAttribute("elencoMacroUso");
 	it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTitoliPossesso");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuolo");
 	TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
 	Vector<TipoAreaVO> elencoTipiArea = (Vector<TipoAreaVO>)request.getAttribute("elencoTipiArea");
  it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoZonaAltimetrica");
  it.csi.solmr.dto.CodeDescription[] elencoCasoParticolare = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoCasoParticolare");
 	Vector<TipoEfaVO> vTipoEfa = (Vector<TipoEfaVO>)request.getAttribute("vTipoEfa");
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  Vector<BaseCodeDescription> elencoTipoDocumento = (Vector<BaseCodeDescription>)request.getAttribute("elencoTipoDocumento"); 
  Vector<BaseCodeDescription> elencoDocumento = (Vector<BaseCodeDescription>)request.getAttribute("elencoDocumento"); 
  Vector<BaseCodeDescription> elencoProtocolloDocumento = (Vector<BaseCodeDescription>)request.getAttribute("elencoProtocolloDocumento"); 
  
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Vector<StoricoParticellaVO> elencoParticelle = (Vector<StoricoParticellaVO>)request.getAttribute("elencoParticelle");
 	String paginaCorrente = (String)request.getAttribute("paginaCorrente");
 	HashMap numUtilizziSelezionati = (HashMap)session.getAttribute("numUtilizziSelezionati");
 	HashMap numParticelleSelezionate = (HashMap)session.getAttribute("numParticelleSelezionate");
 	Vector elencoIdSelezionati = null;
 	Vector elencoIdConduzioneSelezionati = null;
 	if(numUtilizziSelezionati != null && numUtilizziSelezionati.size() > 0) 
 	{
 		elencoIdSelezionati = (Vector)numUtilizziSelezionati.get(paginaCorrente);
 		elencoIdConduzioneSelezionati = (Vector)numParticelleSelezionate.get(paginaCorrente);
 	}
 
 	
 	HashMap elencoAziende = (HashMap)request.getAttribute("elencoAziende");
  HashMap elencoAziendeConferite = (HashMap)request.getAttribute("elencoAziendeConferite");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String idDichiarazioneConsistenza = "";
  
  
  
  String regimeElencoTerreni = request.getParameter("regimeElencoTerreni");
  String regimeElencoTerreniNoPart = request.getParameter("regimeElencoTerreniNoPart");
  
  if(Validator.isEmpty(regimeElencoTerreni)
    || Validator.isNotEmpty(regimeElencoTerreniNoPart))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("supCat", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_1")))
  {
    htmpl.set("cond", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_2")))
  {
    htmpl.set("dataInizioCond", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_3")))
  {
    htmpl.set("dataFineCond", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_4")))
  {
    htmpl.set("supElegg", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_5")))
  {
    htmpl.set("supEleggNetta", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_6")))
  {
    htmpl.set("supAgr", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_7")))
  {
    htmpl.set("usoSecondario", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_8")))
  {
    htmpl.set("casoParticolare", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_9")))
  {
    htmpl.set("elemCaratPaes", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_10")))
  {
    htmpl.set("azAss", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_11")))
  {
    htmpl.set("azConf", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_12")))
  {
    htmpl.set("seminaPrimario", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_13")))
  {
    htmpl.set("seminaSecondario", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_14")))
  {
    htmpl.set("mantenimento", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_15")))
  {
    htmpl.set("allevamento", "true");
  }
  
  
  
  
 	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);
	
	// Combo Piano di riferimento
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  if(filtriParticellareRicercaVO != null)
  {
    idDichiarazioneConsistenza = filtriParticellareRicercaVO.getIdPianoRiferimento().toString();
  }
  else
  {
    idDichiarazioneConsistenza = "-1";
  }
  
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_TERRENI,
    ruoloUtenza);
	
	// Combo unità produttive
	if(elencoUte != null && elencoUte.size() > 0) 
  {
		Iterator<UteVO> iteraUte = elencoUte.iterator();
		while(iteraUte.hasNext()) {
			UteVO uteVO = (UteVO)iteraUte.next();
			htmpl.newBlock("blkUte");
			htmpl.set("blkUte.idUnitaProduttiva", uteVO.getIdUte().toString());
			String descrizione = uteVO.getComuneUte().getDescom()+" - "+uteVO.getIndirizzo();
			if(Validator.isNotEmpty(uteVO.getDenominazione()))
        descrizione += " - "+uteVO.getDenominazione();
			if(Validator.isNotEmpty(uteVO.getDataFineAttivita())) {
				descrizione += " fino al "+DateUtils.formatDate(uteVO.getDataFineAttivita());
			}
			htmpl.set("blkUte.descrizione", descrizione);
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
        && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUte())) 
      {
				if(filtriParticellareRicercaVO.getIdUte().intValue() == uteVO.getIdUte().intValue()) {
					htmpl.set("blkUte.selected", "selected=\"selected\"", null);
				}
			}
		}
	}  
	
	// Combo dei comuni su cui insistono le particelle
	if(elencoComuni != null && elencoComuni.size() > 0) 
  {
		Iterator<ComuneVO> iteraComuni = elencoComuni.iterator();
		while(iteraComuni.hasNext()) 
    {
			ComuneVO comuneVO = (ComuneVO)iteraComuni.next();
			htmpl.newBlock("blkComuniConduzioniParticelle");
			htmpl.set("blkComuniConduzioniParticelle.istatComuniConduzioniParticelle", comuneVO.getIstatComune());
			String descrizione = comuneVO.getDescom();
			if(comuneVO.getProvinciaVO() != null) {
				descrizione += " ("+comuneVO.getProvinciaVO().getSiglaProvincia()+")";
			}
			htmpl.set("blkComuniConduzioniParticelle.descrizione", descrizione);
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
        && Validator.isNotEmpty(filtriParticellareRicercaVO.getIstatComune())) 
      {
				if(filtriParticellareRicercaVO.getIstatComune().equalsIgnoreCase(comuneVO.getIstatComune())) {
					htmpl.set("blkComuniConduzioniParticelle.selected", "selected=\"selected\"", null);
				}
			}
		}
	}
	
	// Sezione
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getSezione())) 
  {
		htmpl.set("sezione", filtriParticellareRicercaVO.getSezione());
	}
	// Foglio
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getFoglio())) 
  {
		htmpl.set("foglio", filtriParticellareRicercaVO.getFoglio());
	}
	// Particella
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getParticella())) 
  {
		htmpl.set("particella", filtriParticellareRicercaVO.getParticella());
	}
	// Subalterno
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getSubalterno())) 
  {
		htmpl.set("subalterno", filtriParticellareRicercaVO.getSubalterno());
	}
	
	// Combo uso suolo
	if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
  {
		Iterator<TipoUtilizzoVO> iteraTipiUsoSuolo = elencoTipiUsoSuolo.iterator();
		int indice = 0;
		while(iteraTipiUsoSuolo.hasNext()) 
    {
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)iteraTipiUsoSuolo.next();
			htmpl.newBlock("blkTipiUsoSuolo");
			if(indice == 0) 
      {
				htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "-1");
				htmpl.set("blkTipiUsoSuolo.descrizione", "qualunque destinazione produttiva");
				if(filtriParticellareRicercaVO.getIdUtilizzo().toString().equalsIgnoreCase("-1")) {
					htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
				}
				htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "0");
				htmpl.set("blkTipiUsoSuolo.descrizione", "senza destinazione produttiva");
				if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() == 0) {
					htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
				}
			}
			htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
			htmpl.set("blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
			String codice = "";
			String stato = "";
			if(Validator.isNotEmpty(tipoUtilizzoVO.getCodice())) {
				codice += "["+tipoUtilizzoVO.getCodice()+"] ";
			}
			if(Validator.isNotEmpty(tipoUtilizzoVO.getAnnoFineValidita())) {
				stato = " [destinazione produttiva non attiva]";
			}
			htmpl.set("blkTipiUsoSuolo.descrizione", codice + tipoUtilizzoVO.getDescrizione() + stato);
			indice++;
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
        && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() == tipoUtilizzoVO.getIdUtilizzo().intValue()))
      {
				htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
			}
		}
	}
	else 
  {
		htmpl.newBlock("blkTipiUsoSuolo");
		htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "0");
		htmpl.set("blkTipiUsoSuolo.descrizione", "senza destinazione produttiva");
		if(Validator.isNotEmpty(filtriParticellareRicercaVO)
     && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() == 0)) 
    {
			htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
		}
	}
	
	// Checkbox "uso primario" e "uso secondario"
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario())) 
  {
		if(filtriParticellareRicercaVO.getCheckUsoPrimario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			htmpl.set("checkedPrimario", "checked=\"checked\"", null);
		}
	}
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario())) 
  {
		if(filtriParticellareRicercaVO.getCheckUsoSecondario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
			htmpl.set("checkedSecondario", "checked=\"checked\"", null);
		}
	}
	
	// Combo macro uso
	if(elencoMacroUso != null && elencoMacroUso.length > 0) 
  {
		for(int i = 0; i < elencoMacroUso.length; i++) 
    {
			TipoMacroUsoVO tipoMacroUsoVO = elencoMacroUso[i];
			htmpl.newBlock("blkMacroUso");
			htmpl.set("blkMacroUso.idMacroUso", tipoMacroUsoVO.getIdMacroUso().toString());
			String descrizione = tipoMacroUsoVO.getCodice()+" - ";
			if(tipoMacroUsoVO.getDescrizione().length() > 40) {
				descrizione += tipoMacroUsoVO.getDescrizione().substring(0, 20);
			}
			else {
				descrizione += tipoMacroUsoVO.getDescrizione();
			}
			htmpl.set("blkMacroUso.descrizione", descrizione);
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdMacroUso())) 
      {
				if(filtriParticellareRicercaVO.getIdMacroUso().compareTo(tipoMacroUsoVO.getIdMacroUso()) == 0) {
					htmpl.set("blkMacroUso.selected", "selected=\"selected\"", null);
				}
			}
		}
	}
	
	// Combo titoli possesso
	if(elencoTitoliPossesso != null && elencoTitoliPossesso.length > 0) 
  {
		for(int i = 0; i < elencoTitoliPossesso.length; i++) 
    {
			it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTitoliPossesso[i];
			htmpl.newBlock("blkTitoliPossesso");
			htmpl.set("blkTitoliPossesso.idTitoloPossesso", code.getCode().toString());
			htmpl.set("blkTitoliPossesso.descrizione", code.getDescription());
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTitoloPossesso())) 
      {
				if(filtriParticellareRicercaVO.getIdTitoloPossesso().intValue() == code.getCode().intValue()) {
					htmpl.set("blkTitoliPossesso.selected", "selected=\"selected\"", null);
				}
			}
		}
	}
  
  // Combo Tipo Documento
  if(elencoTipoDocumento != null && elencoTipoDocumento.size() > 0) 
  {
    for(int i = 0; i < elencoTipoDocumento.size(); i++) {
      BaseCodeDescription code = (BaseCodeDescription)elencoTipoDocumento.get(i);
      htmpl.newBlock("blkTipoDocumento");
      htmpl.set("blkTipoDocumento.idTipoDocumento", ""+code.getCode());
      htmpl.set("blkTipoDocumento.descrizione", code.getDescription());
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoDocumento())) 
      {
        if(filtriParticellareRicercaVO.getIdTipoDocumento().longValue() == code.getCode()) {
          htmpl.set("blkTipoDocumento.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  // Combo Documento
  if(elencoDocumento != null && elencoDocumento.size() > 0) 
  {
    for(int i=0; i<elencoDocumento.size(); i++)
    {
      BaseCodeDescription cd = (BaseCodeDescription)elencoDocumento.get(i);
      htmpl.newBlock("blkComboDocumento");
      htmpl.set("blkComboDocumento.idDocumento", ""+cd.getCode());
      htmpl.set("blkComboDocumento.idTipoDocumento", ""+cd.getItem());
      htmpl.set("blkComboDocumento.descDocumento", cd.getDescription());
      htmpl.set("blkComboDocumento.index", ""+i);
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdDocumento())) 
      {
        if(filtriParticellareRicercaVO.getIdDocumento().longValue() == cd.getCode()) {
          htmpl.set("documentoSel",""+cd.getCode());
        }
      }
    }
  }
  
  // Combo Protocollo Documento
  if(elencoProtocolloDocumento != null && elencoProtocolloDocumento.size() > 0) 
  {
    for(int i=0; i<elencoProtocolloDocumento.size(); i++)
    {
      BaseCodeDescription cd = (BaseCodeDescription)elencoProtocolloDocumento.get(i);
      htmpl.newBlock("blkProtocolloComboDocumento");
      htmpl.set("blkProtocolloComboDocumento.idDocumento", ""+cd.getCode());
      htmpl.set("blkProtocolloComboDocumento.extIdDocumento", ""+cd.getItem());
      htmpl.set("blkProtocolloComboDocumento.descProtocolloDocumento", cd.getDescription());
      htmpl.set("blkProtocolloComboDocumento.index", ""+i);
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdProtocolloDocumento())) 
      {
        if(filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue() == cd.getCode()) {
          htmpl.set("protocolloDocumentoSel",""+cd.getCode());
        }
      }
    }
  }
  
  //combo tipo Area
  if(elencoTipiArea != null && elencoTipiArea.size() > 0) 
  {
    for(int i=0;i<elencoTipiArea.size();i++) 
    {
      TipoAreaVO tipoAreaVO = elencoTipiArea.get(i);
      for(int k=0;k<tipoAreaVO.getvTipoValoreArea().size();k++)
      {  
        TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(k); 
         
        String idTipoValoreAreaStr = tipoValoreAreaVO.getIdTipoValoreArea()+"_"+tipoAreaVO.getFlagEsclusivoFoglio();  
        htmpl.newBlock("blkTipoArea");
        htmpl.set("blkTipoArea.idTipoValoraArea", idTipoValoreAreaStr);
        
        htmpl.set("blkTipoArea.descrizione", "("+tipoAreaVO.getDescrizione()+") "+tipoValoreAreaVO.getDescrizione());
        if(Validator.isNotEmpty(filtriParticellareRicercaVO)  && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
        {
          if(idTipoValoreAreaStr.equalsIgnoreCase(filtriParticellareRicercaVO.getIdTipoValoreArea()+"_"+filtriParticellareRicercaVO.getFlagFoglio())) 
            htmpl.set("blkTipoArea.selected", "selected=\"selected\"", null);
        }
      }
    }         
  }
	
	
  
  // Combo ZonaAltimetrica
  if(elencoZonaAltimetrica != null && elencoZonaAltimetrica.length > 0) 
  {    
    for(int i = 0; i < elencoZonaAltimetrica.length; i++) {
      it.csi.solmr.dto.CodeDescription code = elencoZonaAltimetrica[i];
      htmpl.newBlock("blkZonaAltimetrica");
      htmpl.set("blkZonaAltimetrica.idZonaAltimetrica", code.getCode().toString());
      htmpl.set("blkZonaAltimetrica.descrizione", code.getDescription());
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdZonaAltimetrica())) 
      {
        if(filtriParticellareRicercaVO.getIdZonaAltimetrica().compareTo(Long.decode(code.getCode().toString())) == 0) {
          htmpl.set("blkZonaAltimetrica.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  // Combo CasoParticolare
  if(elencoCasoParticolare != null && elencoCasoParticolare.length > 0) 
  {    
    for(int i = 0; i < elencoCasoParticolare.length; i++) {
      it.csi.solmr.dto.CodeDescription code = elencoCasoParticolare[i];
      htmpl.newBlock("blkCasoParticolare");
      htmpl.set("blkCasoParticolare.idCasoParticolare", code.getCode().toString());
      htmpl.set("blkCasoParticolare.descrizione", code.getDescription());
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
        && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCasoParticolare())) 
      {
        if(filtriParticellareRicercaVO.getIdCasoParticolare().compareTo(Long.decode(code.getCode().toString())) == 0) {
          htmpl.set("blkCasoParticolare.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  // Combo Tipo EFA
  if(Validator.isNotEmpty(vTipoEfa)) 
  {    
    for(int i = 0; i < vTipoEfa.size(); i++) 
    {
      TipoEfaVO tipoEfaVO = vTipoEfa.get(i);
      htmpl.newBlock("blkTipoEfa");
      htmpl.set("blkTipoEfa.idTipoEfa", ""+tipoEfaVO.getIdTipoEfa());
      htmpl.set("blkTipoEfa.descrizione", tipoEfaVO.getDescrizioneTipoEfa());
      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
        && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa())) 
      {
        if(filtriParticellareRicercaVO.getIdTipoEfa().compareTo(new Long(tipoEfaVO.getIdTipoEfa())) == 0) {
          htmpl.set("blkTipoEfa.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
	
	// Escludi asservimento
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento()) 
    && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
  {
		htmpl.set("checkedEscludiAsservimento", "checked=\"checked\"", null);
	}
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && (filtriParticellareRicercaVO.getIdTitoloPossesso() != null)) 
  {
		htmpl.set("disabledEscludiAsservimento", "disabled=\"disabled\"", null);
    htmpl.set("disabledEscludiConferimento", "disabled=\"disabled\"", null);
	}
	// Solo asservite
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckSoloAsservite()) 
    && filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
  {
		htmpl.set("checkedSoloAsservite", "checked=\"checked\"", null);
	}
  
  // Escludi conferimento 
  if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento()) 
    && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
  {
    htmpl.set("checkedEscludiConferimento", "checked=\"checked\"", null);
  }
  // Solo conferite 
  if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckSoloConferite()) 
    && filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
  {
    htmpl.set("checkedSoloConferite", "checked=\"checked\"", null);
  }
	
	// Immagini relative alle segnalazioni
	String sourceImage = null;
	if(pathToFollow.equalsIgnoreCase("rupar")) {
		sourceImage = application.getInitParameter("erroriRupar");
	}
	else if(pathToFollow.equalsIgnoreCase("sispie")) {
		sourceImage = application.getInitParameter("erroriSispie");
	}
	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
		sourceImage = application.getInitParameter("erroriTOBECONFIG");
	}
	
	htmpl.set("srcBloccante", sourceImage+(String)SolmrConstants.get("IMMAGINE_BLOCCANTE"));
	htmpl.set("descImmagineBloccante", (String)SolmrConstants.DESC_TITLE_BLOCCANTE);	
	htmpl.set("srcWarning", sourceImage+(String)SolmrConstants.get("IMMAGINE_WARNING"));
	htmpl.set("descImmagineWarning", (String)SolmrConstants.DESC_TITLE_WARNING);	
	htmpl.set("srcOk", sourceImage+(String)SolmrConstants.get("IMMAGINE_OK"));
	htmpl.set("descImmagineOk", (String)SolmrConstants.DESC_TITLE_POSITIVO);	
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneBloccante())) 
  {
		htmpl.set("checkedBloccante", "checked=\"checked\"", null);	
	}
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneWarning())) 
  {
		htmpl.set("checkedWarning", "checked=\"checked\"", null);	
	}
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
    && Validator.isNotEmpty(filtriParticellareRicercaVO.getTipoSegnalazioneOk())) 
  {
		htmpl.set("checkedOk", "checked=\"checked\"", null);	
	}
	
	
	// Data Esecuzione controlli
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getDataEsecuzioneControlli())) 
  {
		htmpl.set("dataEsecuzioneControlli", "Controlli effettuati il "+filtriParticellareRicercaVO.getDataEsecuzioneControlli());
	}
	else {
		htmpl.set("dataEsecuzioneControlli",(String)SolmrConstants.get("NO_CONTROLLI_PROCEDURA_ESEGUITI"));
	}
	// Data esecuzione controlli dichiarati
	if(Validator.isNotEmpty(filtriParticellareRicercaVO)
   && Validator.isNotEmpty(filtriParticellareRicercaVO.getDataEsecuzioneControlliDichiarati())) 
  {
		htmpl.set("dataEsecuzioneControlliDichiarati", "Controlli effettuati il "+filtriParticellareRicercaVO.getDataEsecuzioneControlliDichiarati());
	}
	else {
		htmpl.set("dataEsecuzioneControlliDichiarati",(String)SolmrConstants.get("NO_CONTROLLI_PROCEDURA_ESEGUITI"));
	}
	
	// Combo tipo controllo
	if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
  {
		for(int i = 0; i < elencoTipiControllo.length; i++) {
			TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
			htmpl.newBlock("blkTipoControllo");
			htmpl.set("blkTipoControllo.idControllo", tipoControlloVO.getIdControllo().toString());
			htmpl.set("blkTipoControllo.descrizione", tipoControlloVO.getDescrizione());
			if(Validator.isNotEmpty(filtriParticellareRicercaVO)
       && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdControllo())) 
      {
				if(filtriParticellareRicercaVO.getIdControllo().compareTo(tipoControlloVO.getIdControllo()) == 0) {
					htmpl.set("blkTipoControllo.selected", "selected=\"selected\"", null);
				}
			}
		}
	}
	
	
	// Combo relativa ai tipi tipologia notifica
  Vector<CodeDescription> vTipologiaNotifica = (Vector<CodeDescription>)request.getAttribute("vTipologiaNotifica");
  if(vTipologiaNotifica != null)
  {
    for(int i = 0; i < vTipologiaNotifica.size(); i++) 
    {
      CodeDescription code = vTipologiaNotifica.get(i);
      htmpl.newBlock("blkTipologiaNotifica");
      htmpl.set("blkTipologiaNotifica.idTipologiaNotifica", code.getCode().toString());
      htmpl.set("blkTipologiaNotifica.descTipologiaNotifica", code.getDescription());
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO))
      {
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipologiaNotifica())
          && (filtriParticellareRicercaVO.getIdTipologiaNotifica().intValue() == code.getCode().intValue())) 
        {
          htmpl.set("blkTipologiaNotifica.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
  
  // Combo Categoria
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = (Vector<TipoCategoriaNotificaVO>)request.getAttribute("vCategoriaNotifica");
  if(vCategoriaNotifica != null) 
  {
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);
      htmpl.newBlock("blkCategoriaCombo");
      htmpl.set("blkCategoriaCombo.idCategoriaNotifica", ""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());
      htmpl.set("blkCategoriaCombo.idTipologiaNotifica", ""+tipoCategoriaNotificaVO.getIdTipologiaNotifica());
      htmpl.set("blkCategoriaCombo.descCategoriaNotifica", tipoCategoriaNotificaVO.getDescrizione());
      htmpl.set("blkCategoriaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(filtriParticellareRicercaVO))
      {
        if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdCategoriaNotifica()) 
          && (filtriParticellareRicercaVO.getIdCategoriaNotifica().intValue() == tipoCategoriaNotificaVO.getIdCategoriaNotifica().intValue())) 
        {
          htmpl.set("categoriaSel",""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());        
        }
      }
    }
  }
  
  
  //********** Visualizzazione filtri ***********
  //UTE
  int countRigheFiltri = 0;
  if(Validator.isNotEmpty(filtriParticellareRicercaVO))
  {
    if(filtriParticellareRicercaVO.getIdUte() != null)
    {
      if(elencoUte != null) 
      {
        for(int i=0;i<elencoUte.size();i++)
        {
          UteVO uteVO = (UteVO)elencoUte.get(i);
          if(filtriParticellareRicercaVO.getIdUte().intValue() == uteVO.getIdUte().intValue()) 
          {
            String descrizione = "UTE: "+uteVO.getComuneUte().getDescom()+" - "+uteVO.getIndirizzo();
            if(Validator.isNotEmpty(uteVO.getDataFineAttivita())) 
            {
              descrizione += " fino al "+DateUtils.formatDate(uteVO.getDataFineAttivita());
            }
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    
    //Titolo Possesso
    if(filtriParticellareRicercaVO.getIdTitoloPossesso() != null)
    {
      if(elencoTitoliPossesso != null)
      {
        for(int i = 0; i < elencoTitoliPossesso.length; i++) 
        {
          it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTitoliPossesso[i];
          if(filtriParticellareRicercaVO.getIdTitoloPossesso().intValue() == code.getCode().intValue()) 
          {
            String descrizione = "Titolo possesso: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    // Escludi asservimento
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento()) 
      && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      String descrizione = "Esclusi asservimento: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    // Solo asservite
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckSoloAsservite()) 
      && filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      String descrizione = "Solo asservite: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
   
    // Escludi conferimento 
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiConferimento()) 
      && filtriParticellareRicercaVO.getCheckEscludiConferimento().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      String descrizione = "Escludi conferimento: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    // Solo conferite 
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckSoloConferite()) 
      && filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
      String descrizione = "Solo conferite: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    // Combo uso suolo
    if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
    {
      String descrizione = "Destinazione produttiva: ";
      if(filtriParticellareRicercaVO.getIdUtilizzo().toString().equalsIgnoreCase("-1")) 
      {
        descrizione += "qualunque destinazione produttiva";
      }
      else if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() == 0)
      {
        descrizione += "senza destinazione produttiva";
      }
      else
      {
        for(int i=0;i<elencoTipiUsoSuolo.size();i++)
        {
          TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.get(i);
          if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() == tipoUtilizzoVO.getIdUtilizzo().intValue()) 
          {
            if(Validator.isNotEmpty(tipoUtilizzoVO.getCodice())) 
            {
              descrizione  += "["+tipoUtilizzoVO.getCodice()+"] ";
            }
            descrizione += tipoUtilizzoVO.getDescrizione();
            if(Validator.isNotEmpty(tipoUtilizzoVO.getAnnoFineValidita())) 
            {
              descrizione += " [destinazione produttiva non attivo]";
            }
            break;
          }  
        }    
      }
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    else 
    {
      String descrizione = "Uso: senza destinazione produttiva";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    
    // Uso primario
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoPrimario()) &&
      filtriParticellareRicercaVO.getCheckUsoPrimario().equalsIgnoreCase(SolmrConstants.FLAG_S))
    {
      String descrizione = "Destinazione produttiva primaria: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    //Uso secondario
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckUsoSecondario()) &&
      filtriParticellareRicercaVO.getCheckUsoSecondario().equalsIgnoreCase(SolmrConstants.FLAG_S))
    {
      String descrizione = "Destinazione produttiva secondaria: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    
    // Combo macro uso
    if(filtriParticellareRicercaVO.getIdMacroUso() != null)
    {
      if(elencoMacroUso != null) 
      {
        for(int i = 0; i < elencoMacroUso.length; i++) 
        {
          TipoMacroUsoVO tipoMacroUsoVO = elencoMacroUso[i];
          if(filtriParticellareRicercaVO.getIdMacroUso().compareTo(tipoMacroUsoVO.getIdMacroUso()) == 0) 
          {
            String descrizione = "Macro uso: ";
            descrizione += tipoMacroUsoVO.getCodice()+" - ";
            if(tipoMacroUsoVO.getDescrizione().length() > 40) {
              descrizione += tipoMacroUsoVO.getDescrizione().substring(0, 20);
            }
            else {
              descrizione += tipoMacroUsoVO.getDescrizione();
            }
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    
    // TIPO AREA 
    if(filtriParticellareRicercaVO.getIdTipoValoreArea() != null)
    {
      if(elencoTipiArea != null)
      {
        for(int i=0;i<elencoTipiArea.size();i++) 
		    {
		      TipoAreaVO tipoAreaVO = elencoTipiArea.get(i);
		      for(int k=0;k<tipoAreaVO.getvTipoValoreArea().size();k++)
		      {  
		        TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(k); 
		         
		        String idTipoValoreAreaStr = tipoValoreAreaVO.getIdTipoValoreArea()+"_"+tipoAreaVO.getFlagEsclusivoFoglio();  
		        if(Validator.isNotEmpty(filtriParticellareRicercaVO)  && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
		        {
		          if(idTipoValoreAreaStr.equalsIgnoreCase(filtriParticellareRicercaVO.getIdTipoValoreArea()+"_"+filtriParticellareRicercaVO.getFlagFoglio()))
		          { 
		            String descrizione = "Tipo area : "+"("+tipoAreaVO.getDescrizione()+") "+tipoValoreAreaVO.getDescrizione();
		            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
		            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
		            countRigheFiltri++;
		            break;
		          }
		        }
		      }
		    }       
      }
    }
    
    
    
    // Zona Altimetrica
    if(filtriParticellareRicercaVO.getIdZonaAltimetrica() != null)
    {
      if(elencoZonaAltimetrica != null)
      {
        for(int i = 0; i < elencoZonaAltimetrica.length; i++) 
        {
          it.csi.solmr.dto.CodeDescription code = elencoZonaAltimetrica[i];
          if(filtriParticellareRicercaVO.getIdZonaAltimetrica().compareTo(Long.decode(code.getCode().toString())) == 0) 
          {
            String descrizione = "Zona altimetrica: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    
    // Caso particolare
    if(filtriParticellareRicercaVO.getIdCasoParticolare() != null)
    {
      if(elencoCasoParticolare != null)
      {
        for(int i = 0; i < elencoCasoParticolare.length; i++) 
        {
          it.csi.solmr.dto.CodeDescription code = elencoCasoParticolare[i];
          if(filtriParticellareRicercaVO.getIdCasoParticolare().compareTo(Long.decode(code.getCode().toString())) == 0) 
          {
            String descrizione = "Caso particolare: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    
    // Tipo EFA
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoEfa()))
    {
      if(vTipoEfa != null)
      {
        for(int i = 0; i < vTipoEfa.size(); i++) 
        {
          TipoEfaVO tipoEfaVO = vTipoEfa.get(i);
          if(filtriParticellareRicercaVO.getIdTipoEfa().compareTo(new Long(tipoEfaVO.getIdTipoEfa())) == 0) 
          {
            String descrizione = "Tipo EFA: "+tipoEfaVO.getDescrizioneTipoEfa();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    
    //Tipologia notifica
    if(filtriParticellareRicercaVO.getIdTipologiaNotifica() != null)
    {
      if(vTipologiaNotifica != null)
      {
        for(int i = 0; i < vTipologiaNotifica.size(); i++) 
        {
          CodeDescription code = vTipologiaNotifica.get(i);
          if(filtriParticellareRicercaVO.getIdTipologiaNotifica().longValue() == code.getCode()) 
          {
            String descrizione = "Tipologia notifica: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    //Categoria notifica
    if(filtriParticellareRicercaVO.getIdCategoriaNotifica() != null)
    {
      if(vCategoriaNotifica != null)
      {
        for(int i = 0; i < vCategoriaNotifica.size(); i++) 
        {
          TipoCategoriaNotificaVO code = vCategoriaNotifica.get(i);
          if(filtriParticellareRicercaVO.getIdCategoriaNotifica().longValue() == code.getIdCategoriaNotifica().longValue()) 
          {
            String descrizione = "Categoria notifica: "+code.getDescrizione();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    // Notifiche chiuse
    if(Validator.isNotEmpty(filtriParticellareRicercaVO.getFlagNotificheChiuse()) &&
      filtriParticellareRicercaVO.getFlagNotificheChiuse().equalsIgnoreCase(SolmrConstants.FLAG_S))
    {
      String descrizione = "Notifiche chiuse: Si";
      aggiungiBloccoFiltro(countRigheFiltri, htmpl);
      aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
      countRigheFiltri++;
    }
    
    //Tipo Documento
    if(filtriParticellareRicercaVO.getIdTipoDocumento() != null)
    {
      if(elencoTipoDocumento != null)
      {
        for(int i = 0; i < elencoTipoDocumento.size(); i++) 
        {
          BaseCodeDescription code = (BaseCodeDescription)elencoTipoDocumento.get(i);
          if(filtriParticellareRicercaVO.getIdTipoDocumento().longValue() == code.getCode()) 
          {
            String descrizione = "Tipo Documento: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    //Documento
    if(filtriParticellareRicercaVO.getIdDocumento() != null)
    {
      if(elencoDocumento != null)
      {
        for(int i = 0; i < elencoDocumento.size(); i++) 
        {
          BaseCodeDescription code = (BaseCodeDescription)elencoDocumento.get(i);
          if(filtriParticellareRicercaVO.getIdDocumento().longValue() == code.getCode()) 
          {
            String descrizione = "Descrizione documento: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
    //Documento
    if(filtriParticellareRicercaVO.getIdProtocolloDocumento() != null)
    {
      if(elencoProtocolloDocumento != null)
      {
        for(int i = 0; i < elencoProtocolloDocumento.size(); i++) 
        {
          BaseCodeDescription code = (BaseCodeDescription)elencoProtocolloDocumento.get(i);
          if(filtriParticellareRicercaVO.getIdProtocolloDocumento().longValue() == code.getCode()) 
          {
            String descrizione = "Documento: "+code.getDescription();
            aggiungiBloccoFiltro(countRigheFiltri, htmpl);
            aggiungiFiltro(countRigheFiltri, htmpl, descrizione);
            countRigheFiltri++;
            break;
          }
        }
      }
    }
  }
  
  
  
  
  //************ Fine Visualizzazione filtri ****************
	
	// Visualizzazione errori relativi ai filtri di ricerca o al nun max di particelle
	// selezionabili per le operazioni di modifica
	HtmplUtil.setErrors(htmpl, errors, request, application);
	
	// Visualizzazione degli errori relativi alla ricerca
	if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkNoParticelle");
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	// Altrimenti visualizzo l'esito della ricerca
	else 
  {
		if(elencoParticelle != null && elencoParticelle.size() > 0) 
    {
			htmpl.newBlock("blkParticelle");
      
      if(session.getAttribute("brogliaccio") != null)
      {
        htmpl.newBlock("blkParticelle.blkBrogliaccio");
      }
			
			// Gestisco la paginazione
			elencoParticelle = (Vector<StoricoParticellaVO>)HtmplUtil.paginazionePerGruppi(Integer.parseInt(paginaCorrente), elencoParticelle, htmpl, request, errors, "blkParticelle", application, filtriParticellareRicercaVO);
			
			//ruolo azienda o titolare
			if(Validator.isNotEmpty(request.getAttribute("elencoRidotto")))
			{
			  htmpl.set("colonneLimitate","ok");
			}
			
			
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getOrderBy())) 
      {
        if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_UTILIZZO_SECONDARIO_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_UTILIZZO_SECONDARIO_ASC_PIANO_ATTUALE)) 
        {
          htmpl.set("blkParticelle.ordinaUsoSecondario", "giu");
          htmpl.set("blkParticelle.descOrdinaUsoSecondario", "ordine decrescente");
          htmpl.set("blkParticelle.tipoOrdinaUsoSecondario", "descUtilizzoSecondarioDisc");
        }
        else 
        {
          htmpl.set("blkParticelle.ordinaUsoSecondario", "su");
          htmpl.set("blkParticelle.descOrdinaUsoSecondario", "ordine crescente");
          htmpl.set("blkParticelle.tipoOrdinaUsoSecondario", "descUtilizzoSecondarioAsc");
        }
        // Ordinamento per varietà secondaria
        /*if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_VARIETA_SECONDARIA_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_VARIETA_SECONDARIA_ASC_PIANO_ATTUALE)) 
        {
          htmpl.set("blkParticelle.ordinaVarietaSecondaria", "giu");
          htmpl.set("blkParticelle.descOrdinaVarietaSecondaria", "ordine decrescente");
          htmpl.set("blkParticelle.tipoOrdinaVarietaSecondaria", "descVarietaSecondariaDisc");
        }
        else 
        {
          htmpl.set("blkParticelle.ordinaVarietaSecondaria", "su");
          htmpl.set("blkParticelle.descOrdinaVarietaSecondaria", "ordine crescente");
          htmpl.set("blkParticelle.tipoOrdinaVarietaSecondaria", "descVarietaSecondariaAsc");
        }*/
        
      }
      else 
      {
        htmpl.set("blkParticelle.ordinaUsoSecondario", "su");
        htmpl.set("blkParticelle.descOrdinaUsoSecondario", "ordine crescente");
        htmpl.set("blkParticelle.tipoOrdinaUsoSecondario", "descUtilizzoSecondarioAsc");
        //htmpl.set("blkParticelle.ordinaVarietaSecondaria", "su");
        //htmpl.set("blkParticelle.descOrdinaVarietaSecondaria", "ordine crescente");
        //htmpl.set("blkParticelle.tipoOrdinaVarietaSecondaria", "descVarietaSecondariaAsc");
      }
			
			// Gestisco le frecce relative all'ordinamento:
			if(Validator.isNotEmpty(filtriParticellareRicercaVO.getOrderBy())) 
      {
				if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_COMUNE_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equals(SolmrConstants.ORDER_BY_DESC_COMUNE_ASC_PIANO_ATTUALE)) 
        {
					htmpl.set("blkParticelle.ordinaComune", "giu");
					htmpl.set("blkParticelle.descOrdinaComune", "ordine decrescente");										
					htmpl.set("blkParticelle.tipoOrdinaComune", "comuneDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaComune", "su");
					htmpl.set("blkParticelle.descOrdinaComune", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaComune", "comuneAsc");
				}
				if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_ID_TITOLO_POSSESSO_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equals(SolmrConstants.ORDER_BY_ID_TITOLO_POSSESSO_ASC_PIANO_ATTUALE)) 
        {
					htmpl.set("blkParticelle.ordinaConduzione", "giu");
					htmpl.set("blkParticelle.descOrdinaConduzione", "ordine decrescente");
					htmpl.set("blkParticelle.tipoOrdinaConduzione", "idTitoloPossessoDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaConduzione", "su");
					htmpl.set("blkParticelle.descOrdinaConduzione", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaConduzione", "idTitoloPossessoAsc");
				}
				if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_CODICE_TIPO_MACRO_USO_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equals(SolmrConstants.ORDER_BY_CODICE_TIPO_MACRO_USO_ASC_PIANO_ATTUALE)) 
        {
					htmpl.set("blkParticelle.ordinaMacroUso", "giu");
					htmpl.set("blkParticelle.descOrdinaMacroUso", "ordine decrescente");
					htmpl.set("blkParticelle.tipoOrdinaMacroUso", "codiceMacroUsoDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaMacroUso", "su");
					htmpl.set("blkParticelle.descOrdinaMacroUso", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaMacroUso", "codiceMacroUsoAsc");
				}
				if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_DESC_UTILIZZO_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equals(SolmrConstants.ORDER_BY_DESC_UTILIZZO_ASC_PIANO_ATTUALE)) 
        {
					htmpl.set("blkParticelle.ordinaUsoPrimario", "giu");
					htmpl.set("blkParticelle.descOrdinaUsoPrimario", "ordine decrescente");
					htmpl.set("blkParticelle.tipoOrdinaUsoPrimario", "descUtilizzoDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaUsoPrimario", "su");
					htmpl.set("blkParticelle.descOrdinaUsoPrimario", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaUsoPrimario", "descUtilizzoAsc");
				}
				// Ordinamento per varietà primaria
				/*if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_VARIETA_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equals(SolmrConstants.ORDER_BY_DESC_VARIETA_ASC_PIANO_ATTUALE)) 
        {
					htmpl.set("blkParticelle.ordinaVarietaPrimaria", "giu");
					htmpl.set("blkParticelle.descOrdinaVarietaPrimaria", "ordine decrescente");
					htmpl.set("blkParticelle.tipoOrdinaVarietaPrimaria", "descVarietaDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaVarietaPrimaria", "su");
					htmpl.set("blkParticelle.descOrdinaVarietaPrimaria", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaVarietaPrimaria", "descVarietaAsc");
				}*/
				
				if(filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_CHAR_ID_CASO_PARTICOLARE_ASC)
          || filtriParticellareRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_ID_CASO_PARTICOLARE_ASC_PIANO_ATTUALE))
        {
				  htmpl.set("blkParticelle.ordinaCasoParticolare", "giu");
					htmpl.set("blkParticelle.descOrdinaCasoParticolare", "ordine decrescente");
					htmpl.set("blkParticelle.tipoOrdinaCasoParticolare", "idCasoParticolareDisc");
				}
				else 
        {
					htmpl.set("blkParticelle.ordinaCasoParticolare", "su");
					htmpl.set("blkParticelle.descOrdinaCasoParticolare", "ordine crescente");
					htmpl.set("blkParticelle.tipoOrdinaCasoParticolare", "idCasoParticolareAsc");
				}
				
			}
			else 
      {
				htmpl.set("blkParticelle.ordinaComune", "giu");
				htmpl.set("blkParticelle.descOrdinaComune", "ordine decrescente");
				htmpl.set("blkParticelle.tipoOrdinaComune", "comuneDisc");
				htmpl.set("blkParticelle.ordinaConduzione", "su");
				htmpl.set("blkParticelle.descOrdinaConduzione", "ordine crescente");
				htmpl.set("blkParticelle.tipoOrdinaConduzione", "idTitoloPossessoAsc");
				htmpl.set("blkParticelle.ordinaMacroUso", "su");
				htmpl.set("blkParticelle.descOrdinaMacroUso", "ordine crescente");
				htmpl.set("blkParticelle.tipoOrdinaMacroUso", "codiceMacroUsoAsc");
				htmpl.set("blkParticelle.ordinaUsoPrimario", "su");
				htmpl.set("blkParticelle.descOrdinaUsoPrimario", "ordine crescente");
				htmpl.set("blkParticelle.tipoOrdinaUsoPrimario", "descUtilizzoAsc");
				//htmpl.set("blkParticelle.ordinaVarietaPrimaria", "su");
				//htmpl.set("blkParticelle.descOrdinaVarietaPrimaria", "ordine crescente");
				//htmpl.set("blkParticelle.tipoOrdinaVarietaPrimaria", "descVarietaAsc");
				htmpl.set("blkParticelle.ordinaCasoParticolare", "su");
				htmpl.set("blkParticelle.descOrdinaCasoParticolare", "ordine crescente");
				htmpl.set("blkParticelle.tipoOrdinaCasoParticolare", "idCasoParticolareAsc");
				
			}
			
			
			Iterator<StoricoParticellaVO> iteraParticelle = elencoParticelle.iterator();
			
			while(iteraParticelle.hasNext()) 
      {
				StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)iteraParticelle.next();
				ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
				htmpl.newBlock("blkElencoParticelle");
				// Se il criterio di ricerca era per piano di riferimento uguale ad anno corrente
				// con eventualmente le conduzioni storicizzate
				if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
        {
					ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
          
          UtilizzoParticellaVO utilizzoParticellaVO = null;
          if(conduzioneParticellaVO.getElencoUtilizzi() != null) {
            utilizzoParticellaVO = (UtilizzoParticellaVO)conduzioneParticellaVO.getElencoUtilizzi()[0];
          }
					
					htmpl.set("blkParticelle.blkElencoParticelle.idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
					htmpl.set("blkParticelle.blkElencoParticelle.idAzienda", anagAziendaVO.getIdAzienda().toString());
					htmpl.set("blkParticelle.blkElencoParticelle.urlRSDI", urlRSDI);

					// Gestione dei record selezionati per le varie operazioni di modifica multipla
					// del dato
					if(elencoIdSelezionati != null && elencoIdSelezionati.size() > 0) 
					{
						for(int i = 0; i < elencoIdSelezionati.size(); i++) 
						{
							Long idElementoSelezionato = (Long)elencoIdSelezionati.elementAt(i);							
							if(idElementoSelezionato.compareTo(new Long(-1)) == 0) 
							{
							  if(elencoIdConduzioneSelezionati != null)
							  {
								  Long idConduzioneSelezionato = (Long)elencoIdConduzioneSelezionati.elementAt(i);
									if(idConduzioneSelezionato.compareTo(conduzioneParticellaVO.getIdConduzioneParticella()) == 0) 
									{
										if(idElementoSelezionato.compareTo(utilizzoParticellaVO.getIdUtilizzoParticella()) == 0) 
										{
											htmpl.set("blkParticelle.blkElencoParticelle.checked", "checked=\"checked\"", null);
										}		
									}
							  }
							}
							else 
							{
								if(idElementoSelezionato.compareTo(utilizzoParticellaVO.getIdUtilizzoParticella()) == 0) 
								{
									htmpl.set("blkParticelle.blkElencoParticelle.checked", "checked=\"checked\"", null);
								}
							}
						}
					}
          //Icone del gis
          String titleGis= "";
          String immaginiControlliP = "";
          String stabilizzazione = "";
          if(storicoParticellaVO.getSospesaGis() != null)
          {
            titleGis += "GIS Anomalie: Lavorazione sospesa il ";
            titleGis += DateUtils.formatDateNotNull(particellaCertificataVO.getDataSospensione());
            if(Validator.isNotEmpty(particellaCertificataVO.getMotivazioneGis()))
            {
              titleGis += " - " +particellaCertificataVO.getMotivazioneGis();
            }
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS;
            }  
          }         
          if(storicoParticellaVO.getGisP30() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P30";
            }
            else
            {
              titleGis += "GIS Anomalie: P30";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	            {
	              immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P30_STAB;
	            }
	            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	            {
	              immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P30_STAB_CORSO;
	            }
	            else
	            {
	              immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P30;
	            }  
              
            }          
          }   
          if(storicoParticellaVO.getGisP25() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P25";
            }
            else
            {
              titleGis += "GIS Anomalie: P25";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
              {
                immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P25_STAB;
              }
              else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                  .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
              {
                immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P25_STAB_CORSO;
              }
              else
              {
                immaginiControlliP = SolmrConstants.IMMAGINE_GIS_P25;
              }  
            }          
            
          }          
          if(storicoParticellaVO.getGisP26() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P26";
            }
            else
            {
              titleGis += "GIS Anomalie: P26";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB;
              }
              else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                  .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB_CORSO;
              }
              else
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26;
              }  
            }  
          }
          if(Validator.isEmpty(titleGis))
          {
            titleGis += "GIS";
          }
          
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
          
                    
          htmpl.set("blkParticelle.blkElencoParticelle.controlliP", titleGis);
          if(Validator.isEmpty(immaginiControlliP))
          {
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS;
            }   
          }  
          htmpl.set("blkParticelle.blkElencoParticelle.immaginiControlliP", immaginiControlliP);
          
          
          
          
          //Icone istanza riesame
          String titleIstanzaRiesame= "";
          String immaginiIstanzaRiesame = "";
          //E' stata lavorata
          
          
          if(Validator.isNotEmpty(storicoParticellaVO.getIstanzaRiesame()))
          {
	          if(storicoParticellaVO.getIstanzaRiesame().compareTo(new Long(2)) == 0)
	          {
	            titleIstanzaRiesame += "Istanza di riesame evasa";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_FINE;
	          }
	          else if(storicoParticellaVO.getIstanzaRiesame().compareTo(new Long(1)) == 0)
	          {
	            titleIstanzaRiesame += "Richiesta istanza di riesame";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_INIZIO;         
	          }
	        }
                           
          htmpl.set("blkParticelle.blkElencoParticelle.descTitleIstanzaRiesame", titleIstanzaRiesame);
          htmpl.set("blkParticelle.blkElencoParticelle.immaginiIstanzaRiesame", immaginiIstanzaRiesame);       
          
          
          
					// Icona di segnalazione delle anomalie
					if(Validator.isNotEmpty(conduzioneParticellaVO.getEsitoControllo())) {
						// Bloccante
						if(conduzioneParticellaVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_BLOCCANTE)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_BLOCCANTE);
						}
						// Warning
						else if(conduzioneParticellaVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_WARNING)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_WARNING);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_WARNING);
						}
						// Positivo
						else if(conduzioneParticellaVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_POSITIVO)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_POSITIVO);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_POSITIVO);
						}
					}
					// Icona di segnalazione relativo allo stato del documento
					if(storicoParticellaVO.getDocumentoVO() != null) 
          {
						// Documento Ok
						if((DateUtils.formatDate(storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMinDataFineValidita())
                  .equalsIgnoreCase(DateUtils.formatDate(storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita())) 
                  && storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita().getTime() > System.currentTimeMillis()) 
               || (storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMinDataFineValidita().getTime() < System.currentTimeMillis() 
                  && storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita().getTime() > System.currentTimeMillis()) 
               || storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMinDataFineValidita().getTime() > System.currentTimeMillis()) 
            {
							htmpl.set("blkParticelle.blkElencoParticelle.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_OK);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_OK);																							
						}
						// Documento scaduto
						else if((storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMinDataFineValidita().getTime() 
                      == storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita().getTime() 
                      && storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita().getTime() 
                      < System.currentTimeMillis()) 
                    || storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMaxDataFineValidita().getTime() 
                      < System.currentTimeMillis()) 
            {
							htmpl.set("blkParticelle.blkElencoParticelle.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_SCADUTO);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_SCADUTO);																							
						}
					}
					// Icona relativa allo schedario viti-vinicolo
					if(storicoParticellaVO.getParticellaVO() != null) {
						if(storicoParticellaVO.getStoricoUnitaArboreaVO() != null) {
							if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
								htmpl.set("blkParticelle.blkElencoParticelle.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO);
								htmpl.set("blkParticelle.blkElencoParticelle.classSchedario", SolmrConstants.CLASS_SCHEDARIO);
							}
							else if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_SCHEDARIO_MODIFICA)) {
								htmpl.set("blkParticelle.blkElencoParticelle.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO_MODIFICATO);
								htmpl.set("blkParticelle.blkElencoParticelle.classSchedario", SolmrConstants.CLASS_SCHEDARIO_MODIFICA);
							}
						}
					}
          
          // Icona relativa al biologico
          if(storicoParticellaVO.getBiologico() != null) 
          {
            htmpl.set("blkParticelle.blkElencoParticelle.descBiologico", SolmrConstants.DESC_TITLE_BIOLOGICO);
            htmpl.set("blkParticelle.blkElencoParticelle.classBiologico", SolmrConstants.CLASS_BIOLOGICO);
          }
          
          if(Validator.isNotEmpty(storicoParticellaVO.getInNotifica())) 
          {
            htmpl.set("blkParticelle.blkElencoParticelle.classNotifica", SolmrConstants.STATO_NOTIFICHE_PARTICELLARI);
            htmpl.set("blkParticelle.blkElencoParticelle.descNotifica", SolmrConstants.DESC_NOTIFICHE_PARTICELLARI);
          }
          
					htmpl.set("blkParticelle.blkElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
					htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());					
					htmpl.set("blkParticelle.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());					
					htmpl.set("blkParticelle.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
					htmpl.set("blkParticelle.blkElencoParticelle.particella", storicoParticellaVO.getParticella());					
					htmpl.set("blkParticelle.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
					htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
          htmpl.set("blkParticelle.blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
					
          //Aggiunta eleggibilità fittizia come tooltip
          String tooltipDescElegFit = "";
          if(Validator.isNotEmpty(storicoParticellaVO.getvSupElegFit()))
          {
            for(int k=0;k<storicoParticellaVO.getvSupElegFit().size();k++)
            {
               tooltipDescElegFit += storicoParticellaVO.getvSupElegFit().get(k).getDescrizione()+": ";
               tooltipDescElegFit += Formatter.formatDouble4(storicoParticellaVO
                .getvSupElegFit().get(k).getSuperficie()) +" ha\n";
            }          
          }
          htmpl.set("blkParticelle.blkElencoParticelle.tooltipDescElegFit", tooltipDescElegFit);
          
          
          
          htmpl.set("blkParticelle.blkElencoParticelle.idTitoloPossesso", conduzioneParticellaVO.getIdTitoloPossesso().toString());
          htmpl.set("blkParticelle.blkElencoParticelle.descTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getDescription());
          BigDecimal percentualePossessoTmp = conduzioneParticellaVO.getPercentualePossesso();
          htmpl.set("blkParticelle.blkElencoParticelle.percentualePossessoDecimal", Formatter.formatDouble2(percentualePossessoTmp));
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
					htmpl.set("blkParticelle.blkElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
					// Visualizzo la label ed eventualmente il valore relativo alla data fine conduzione solo
					// se il piano di riferimento indicato nei filtri di ricerca è "anno di sistema+ in lavorazione(comprensiva di conduzioni storicizzate)"
					
					htmpl.set("blkParticelle.blkElencoParticelle.dataInizioConduzione", DateUtils.formatDate(conduzioneParticellaVO.getDataInizioConduzione()));
					if(conduzioneParticellaVO.getDataFineConduzione() != null) 
					{
            htmpl.set("blkParticelle.blkElencoParticelle.dataFineConduzione", DateUtils.formatDate(conduzioneParticellaVO.getDataFineConduzione()));              
          }
          
					
					
	
					if(particellaCertificataVO != null) 
          {
            if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
              && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
            {
              //Per la query è popolato solo il primo elemento
              ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
              if(Validator.isNotEmpty(partCertVO.getSuperficie())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
              }
              else 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
              }
              
              if(Validator.isNotEmpty(partCertVO.getSuperficieEleggibileNetta())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", Formatter.formatDouble4(partCertVO.getSuperficieEleggibileNetta()));         
              }
              else 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
              }
            }
            else
            {
              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
            }
					}
					else
          {
            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
          }	
					
          String supAgronomica = "";
          if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica())) 
          {
            supAgronomica = StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica());
          }
          else 
          {
            supAgronomica = SolmrConstants.DEFAULT_SUPERFICIE;
          }
          if(conduzioneParticellaVO.getIdTitoloPossesso()
            .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            supAgronomica = StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta());
          }
          htmpl.set("blkParticelle.blkElencoParticelle.supAgronomica", supAgronomica);
									
					// Visualizzo di colore diverso lo sfondo della chiave logica delle particelle modificate
					if(Validator.isNotEmpty(conduzioneParticellaVO.getRecordModificato()) && conduzioneParticellaVO.getRecordModificato().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
						htmpl.set("blkParticelle.blkElencoParticelle.color", SolmrConstants.COLOR_PARTICELLE_MODIFICATE);
					}
					if(utilizzoParticellaVO != null) 
					{
						// Visualizzo i dati relativi all'uso, esclusa la superficie utilizzata che 
						// verrà visualizzata sempre, solo se si tratta di un utilizzo censito su DB
						htmpl.set("blkParticelle.blkElencoParticelle.idUtilizzo", utilizzoParticellaVO.getIdUtilizzoParticella().toString());
						if(utilizzoParticellaVO.getIdUtilizzoParticella().longValue() > 0) 
						{
							if(utilizzoParticellaVO.getTipoMacroUsoVO() != null) 
							{
								htmpl.set("blkParticelle.blkElencoParticelle.titleMacroUso", utilizzoParticellaVO.getTipoMacroUsoVO().getDescrizione());
								htmpl.set("blkParticelle.blkElencoParticelle.codiceMacroUso", utilizzoParticellaVO.getTipoMacroUsoVO().getCodice());
							}
							htmpl.set("blkParticelle.blkElencoParticelle.descUsoPrimario", "["+utilizzoParticellaVO.getTipoUtilizzoVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoVO().getDescrizione());
							if(utilizzoParticellaVO.getTipoDestinazione() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoDestinazionePrimario", 
                  "["+utilizzoParticellaVO.getTipoDestinazione().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione());
              }
							if(utilizzoParticellaVO.getTipoDettaglioUso() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoDettUsoPrimario", "["+utilizzoParticellaVO.getTipoDettaglioUso().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione());
              }
              if(utilizzoParticellaVO.getTipoQualitaUso() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoQualitaUsoPrimario", "["+utilizzoParticellaVO.getTipoQualitaUso().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso());
              }
              if(utilizzoParticellaVO.getTipoVarietaVO() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoVarietaVO().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descVarietaPrimario", "["+utilizzoParticellaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaVO().getDescrizione());
              }
              if(utilizzoParticellaVO.getTipoPeriodoSemina() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPeriodoSemina().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoSeminaPrimario", utilizzoParticellaVO.getTipoPeriodoSemina().getDescrizione());
              }
              if(utilizzoParticellaVO.getTipoSemina() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoSemina().getDescrizioneSemina())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descSeminaPrimario", utilizzoParticellaVO.getTipoSemina().getDescrizioneSemina());
              }
              
              htmpl.set("blkParticelle.blkElencoParticelle.dataInizioSeminaPrimario", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazione()));
              htmpl.set("blkParticelle.blkElencoParticelle.dataFineSeminaPrimario", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazione()));
							
							
							if(utilizzoParticellaVO.getTipoUtilizzoSecondarioVO() != null) 
							{
                htmpl.set("blkParticelle.blkElencoParticelle.descUsoSecondario", "["+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getDescrizione());
                if(utilizzoParticellaVO.getTipoDestinazioneSecondario() != null 
                  && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoDestinazioneSecondario", "["+utilizzoParticellaVO.getTipoDestinazioneSecondario().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione());
                }
                if(utilizzoParticellaVO.getTipoDettaglioUsoSecondario() != null 
	                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione())) 
	              {
	                htmpl.set("blkParticelle.blkElencoParticelle.descTipoDettUsoSecondario", "["+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione());
	              }
	              if(utilizzoParticellaVO.getTipoQualitaUsoSecondario() != null 
                  && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoQualitaUsoSecondario", "["+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso());
                }
                if(utilizzoParticellaVO.getTipoVarietaSecondariaVO() != null) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descVarietaSecondaria", "["+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getDescrizione());
                }
	              if(utilizzoParticellaVO.getTipoPeriodoSeminaSecondario() != null 
	                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPeriodoSeminaSecondario().getDescrizione())) 
	              {
	                htmpl.set("blkParticelle.blkElencoParticelle.descTipoSeminaSecondario", utilizzoParticellaVO.getTipoPeriodoSeminaSecondario().getDescrizione());
	              }
	              
	              if(utilizzoParticellaVO.getTipoSeminaSecondario() != null 
                  && Validator.isNotEmpty(utilizzoParticellaVO.getTipoSeminaSecondario().getDescrizioneSemina())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descSeminaSecondario", utilizzoParticellaVO.getTipoSeminaSecondario().getDescrizioneSemina());
                }
	              
                htmpl.set("blkParticelle.blkElencoParticelle.superficieSecondariaUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
                
                htmpl.set("blkParticelle.blkElencoParticelle.dataInizioSeminaSecondario", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazioneSec()));
                htmpl.set("blkParticelle.blkElencoParticelle.dataFineSeminaSecondario", DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazioneSec()));
              
              }
              
              
              if(utilizzoParticellaVO.getTipoPraticaMantenimento() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descMantenimento", utilizzoParticellaVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim());
              }
              
              if(utilizzoParticellaVO.getTipoFaseAllevamento() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descAllevamento", utilizzoParticellaVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento());
              }
              
              if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoEfa()))
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoEfa", utilizzoParticellaVO.getDescTipoEfaEfa());
                htmpl.set("blkParticelle.blkElencoParticelle.valoreOriginale", Formatter.formatDouble4(utilizzoParticellaVO.getValoreOriginale()));
                htmpl.set("blkParticelle.blkElencoParticelle.descUnitaMisura", utilizzoParticellaVO.getDescUnitaMisuraEfa());
                htmpl.set("blkParticelle.blkElencoParticelle.valoreDopoConversione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoConversione()));
                htmpl.set("blkParticelle.blkElencoParticelle.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoPonderazione()));
              }
              
						}
						
            
            String supUtilizzo = utilizzoParticellaVO.getSuperficieUtilizzata();
            if(conduzioneParticellaVO.getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
            {
              supUtilizzo = SolmrConstants.DEFAULT_SUPERFICIE;
            }
						htmpl.set("blkParticelle.blkElencoParticelle.superficieUtilizzata", StringUtils.parseSuperficieField(supUtilizzo));
					}
					
					if(storicoParticellaVO.getIdCasoParticolare() != null) 
					{
						htmpl.set("blkParticelle.blkElencoParticelle.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());						
					}
					
					
					if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_N)
            && filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_N))
          {
            htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", "1");
            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle");
            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle");
          }	
					
					if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
						AnagAziendaVO[] elenco = (AnagAziendaVO[])elencoAziende.get(storicoParticellaVO.getIdStoricoParticella());
						htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", String.valueOf(elenco.length));
						
						for(int i = 0; i < elenco.length; i++) 
            {
							AnagAziendaVO anagAziendaVOTmp = elenco[i];
							if(i == 0) 
              {
								htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle");
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.cuaaAss", anagAziendaVOTmp.getCUAA());
								String denominazione = "";
								if(anagAziendaVOTmp.getDenominazione().length() > 20) {
									denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
								}
								else {
									denominazione = anagAziendaVOTmp.getDenominazione();
								}
								
								if(Validator.isNotEmpty(anagAziendaVOTmp.getDataCessazione()))
                {
                  denominazione += " (Cessata)";
                }
								
								
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.denominazioneAss", denominazione);
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.aziendeAsservite", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
							}
							else 
              {
								htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple");
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.cuaaAss", anagAziendaVOTmp.getCUAA());
								String denominazione = "";
								if(anagAziendaVOTmp.getDenominazione().length() > 20) {
									denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
								}
								else {
									denominazione = anagAziendaVOTmp.getDenominazione();
								}
								
								if(Validator.isNotEmpty(anagAziendaVOTmp.getDataCessazione()))
								{
								  denominazione += " (Cessata)";
								}
								
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.denominazioneAss", denominazione);
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.aziendeAsservite", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
							}
						}
					}
        
          
          if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            AnagAziendaVO[] elenco = (AnagAziendaVO[])elencoAziendeConferite.get(storicoParticellaVO.getIdStoricoParticella());
            
          
          
            htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", String.valueOf(elenco.length));
            
            for(int i = 0; i < elenco.length; i++) 
            {
              AnagAziendaVO anagAziendaVOTmp = elenco[i];
              if(i == 0) 
              {
                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle");
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.cuaaConf", anagAziendaVOTmp.getCUAA());
                String denominazione = "";
                if(anagAziendaVOTmp.getDenominazione().length() > 20) {
                  denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
                }
                else {
                  denominazione = anagAziendaVOTmp.getDenominazione();
                }
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.denominazioneConf", denominazione);
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.aziendeConferita", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
              }
              else 
              {
                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple");
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.cuaaConf", anagAziendaVOTmp.getCUAA());
                String denominazione = "";
                if(anagAziendaVOTmp.getDenominazione().length() > 20) {
                  denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
                }
                else {
                  denominazione = anagAziendaVOTmp.getDenominazione();
                }
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.denominazioneConf", denominazione);
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.aziendeConferita", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
              }
            }
          }
          
				}
				// Se invece si tratta di una ricerca per piano di riferimento relativo ad una
				// data di dichiarazione di consistenza
				else 
        {
					ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)storicoParticellaVO.getElencoConduzioniDichiarate()[0];
					UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
					if(conduzioneDichiarataVO.getElencoUtilizzi() != null) {
						utilizzoDichiaratoVO = (UtilizzoDichiaratoVO)conduzioneDichiarataVO.getElencoUtilizzi()[0];
					}
					htmpl.set("blkParticelle.blkElencoParticelle.idConduzione", conduzioneDichiarataVO.getIdConduzioneDichiarata().toString());
          Long idConduzioneParticella = conduzioneDichiarataVO.getIdConduzioneParticella();
          if (idConduzioneParticella!=null)
          {
            htmpl.set("blkParticelle.blkElencoParticelle.blkCondPart.idConduzioneParticella", idConduzioneParticella.toString());
					}
          // Gestione dei record selezionati per le varie operazioni di modifica multipla
					// del dato
					if(elencoIdSelezionati != null && elencoIdSelezionati.size() > 0) 
					{	
						for(int i = 0; i < elencoIdSelezionati.size(); i++) 
						{
							Long idElementoSelezionato = (Long)elencoIdSelezionati.elementAt(i);
							if(idElementoSelezionato.compareTo(new Long(-1)) == 0) 
							{
							  if(elencoIdConduzioneSelezionati != null)
							  {
							    Long idConduzioneSelezionato = (Long)elencoIdConduzioneSelezionati.elementAt(i);
									if(idConduzioneSelezionato.compareTo(conduzioneDichiarataVO.getIdConduzioneDichiarata()) == 0) 
									{
										if(idElementoSelezionato.compareTo(utilizzoDichiaratoVO.getIdUtilizzoDichiarato()) == 0) 
										{
											htmpl.set("blkParticelle.blkElencoParticelle.checked", "checked=\"checked\"",null);
										}		
									}
							  }
							}
							else 
							{
								if(idElementoSelezionato.compareTo(utilizzoDichiaratoVO.getIdUtilizzoDichiarato()) == 0) 
								{
									htmpl.set("blkParticelle.blkElencoParticelle.checked", "checked=\"checked\"",null);
								}
							}
						}
					}
          
          //Icone del gis
          String titleGis= "";
          String immaginiControlliP = "";
          if(storicoParticellaVO.getSospesaGis() != null)
          {
            titleGis += "GIS Anomalie: Lavorazione sospesa il ";
            titleGis += DateUtils.formatDateNotNull(particellaCertificataVO.getDataSospensione());
            if(Validator.isNotEmpty(particellaCertificataVO.getMotivazioneGis()))
            {
              titleGis += " - "+particellaCertificataVO.getMotivazioneGis();
            }
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_SOSPESA_GIS;
            }
          }         
          if(storicoParticellaVO.getGisP30() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P30";
            }
            else
            {
              titleGis += "GIS Anomalie: P30";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30_STAB;
              }
              else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                  .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30_STAB_CORSO;
              }
              else
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30;
              }
            }          
          }   
          if(storicoParticellaVO.getGisP25() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P25";
            }
            else
            {
              titleGis += "GIS: Anomalie P25";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25_STAB;
              }
              else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
                && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                  .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25_STAB_CORSO;
              }
              else
              {
                immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25;
              }
            }          
            
          }          
          if(storicoParticellaVO.getGisP26() != null)
          {
            if(Validator.isNotEmpty(titleGis))
            {
              titleGis += ", P26";
            }
            else
            {
              titleGis += "GIS: Anomalie P26";
            }
            
            if(Validator.isEmpty(immaginiControlliP))
            {
              if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	            {
	              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB;
	            }
	            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	            {
	              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB_CORSO;
	            }
	            else
	            {
	              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26;
	            }
            }  
          }
          if(Validator.isEmpty(titleGis))
          {
            titleGis += "GIS";
          }
          
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
          
                    
          htmpl.set("blkParticelle.blkElencoParticelle.controlliP", titleGis);
          if(Validator.isEmpty(immaginiControlliP))
          {
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS;
            }   
          }  
          htmpl.set("blkParticelle.blkElencoParticelle.immaginiControlliP", immaginiControlliP);
          
          
          
          
          //Icone istanza riesame
          String titleIstanzaRiesame= "";
          String immaginiIstanzaRiesame = "";
          if(Validator.isNotEmpty(storicoParticellaVO.getvIstanzaRiesame()))
          {
            //la query ritona solo l'ultima fase
            //se valorizzata data_evasione...
	          if(Validator.isNotEmpty(storicoParticellaVO.getvIstanzaRiesame().get(0).getDataEvasione()))
	          {
	            titleIstanzaRiesame += "Istanza di riesame evasa";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_FINE;
	          }
	          //altrimenti se ritorna la fase cmq la data_richiesta deve essere per forza presente
	          else
	          {
	            titleIstanzaRiesame += "Richiesta istanza di riesame";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_INIZIO;	          
	          }
	        }      
          
                            
          htmpl.set("blkParticelle.blkElencoParticelle.descTitleIstanzaRiesame", titleIstanzaRiesame);
          htmpl.set("blkParticelle.blkElencoParticelle.immaginiIstanzaRiesame", immaginiIstanzaRiesame);
          
          
          
          
          
          
					// Icona di segnalazione delle anomalie
					if(Validator.isNotEmpty(conduzioneDichiarataVO.getEsitoControllo())) {
						// Bloccante
						if(conduzioneDichiarataVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_BLOCCANTE)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_BLOCCANTE);
						}
						// Warning
						else if(conduzioneDichiarataVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_WARNING)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_WARNING);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_WARNING);
						}
						// Positivo
						else if(conduzioneDichiarataVO.getEsitoControllo().equalsIgnoreCase((String)SolmrConstants.ESITO_CONTROLLO_POSITIVO)) {
							htmpl.set("blkParticelle.blkElencoParticelle.classImmagine", (String)SolmrConstants.IMMAGINE_ESITO_POSITIVO);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleAnomalia", (String)SolmrConstants.DESC_TITLE_POSITIVO);
						}
					}
					// Icona di segnalazione relativo allo stato del documento
					if(storicoParticellaVO.getDocumentoVO() != null 
            && storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO() != null) 
          {
            //Se valorizzato, vuol dire che esiste almeno un documento relativo al periodo della dichiarazione
            // di consistenza
						if(storicoParticellaVO.getDocumentoVO().getDocumentoConduzioneVO().getMinDataInizioValidita() != null) 
            {
							// Documento Ok
						  htmpl.set("blkParticelle.blkElencoParticelle.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_OK);
							htmpl.set("blkParticelle.blkElencoParticelle.descTitleDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_OK);																							
						}
					}
					// Icona relativa allo schedario viti-vinicolo
					if(storicoParticellaVO.getParticellaVO() != null) {
						if(storicoParticellaVO.getUnitaArboreaDichiarataVO() != null) {
							if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
								htmpl.set("blkParticelle.blkElencoParticelle.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO);
								htmpl.set("blkParticelle.blkElencoParticelle.classSchedario", SolmrConstants.CLASS_SCHEDARIO);
							}
							else if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_SCHEDARIO_MODIFICA)) {
								htmpl.set("blkParticelle.blkElencoParticelle.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO_MODIFICATO);
								htmpl.set("blkParticelle.blkElencoParticelle.classSchedario", SolmrConstants.CLASS_SCHEDARIO_MODIFICA);
							}
						}
					}
          
          // Icona relativa al biologico
          if(storicoParticellaVO.getBiologico() != null) 
          {
            htmpl.set("blkParticelle.blkElencoParticelle.descBiologico", SolmrConstants.DESC_TITLE_BIOLOGICO);
            htmpl.set("blkParticelle.blkElencoParticelle.classBiologico", SolmrConstants.CLASS_BIOLOGICO);
          }
          
          if(Validator.isNotEmpty(storicoParticellaVO.getInNotifica())) 
          {
            htmpl.set("blkParticelle.blkElencoParticelle.classNotifica", SolmrConstants.STATO_NOTIFICHE_PARTICELLARI);
            htmpl.set("blkParticelle.blkElencoParticelle.descNotifica", SolmrConstants.DESC_NOTIFICHE_PARTICELLARI);
          }
          
					htmpl.set("blkParticelle.blkElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
					htmpl.set("blkParticelle.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());					
					htmpl.set("blkParticelle.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());					
					htmpl.set("blkParticelle.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
					htmpl.set("blkParticelle.blkElencoParticelle.particella", storicoParticellaVO.getParticella());					
					htmpl.set("blkParticelle.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
					htmpl.set("blkParticelle.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
          htmpl.set("blkParticelle.blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
					htmpl.set("blkParticelle.blkElencoParticelle.idTitoloPossesso", conduzioneDichiarataVO.getIdTitoloPossesso().toString());
					BigDecimal percentualePossessoTmp = conduzioneDichiarataVO.getPercentualePossesso();
					htmpl.set("blkParticelle.blkElencoParticelle.percentualePossessoDecimal", Formatter.formatDouble2(percentualePossessoTmp));
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
					htmpl.set("blkParticelle.blkElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
				
					
					if(particellaCertificataVO != null) 
          {
	          //Nuova eleggibilità!!!
	          if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
	            && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
	          {
	            //Per la query è popolato solo il primo elemento
	            ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
	            if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
	              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
	            }
	            else 
	            {
	              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
	            }
	            if(Validator.isNotEmpty(partCertVO.getSuperficieEleggibileNetta())) {
	              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", Formatter.formatDouble4(partCertVO.getSuperficieEleggibileNetta()));         
	            }
	            else 
	            {
	              htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
	            }
	          }
	          else
	          {
	            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
	            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
	          }
	        }
	        else 
          {
            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
            htmpl.set("blkParticelle.blkElencoParticelle.supEleggibileNetta", SolmrConstants.DEFAULT_SUPERFICIE);
          }
					
					String supAgronomica = "";
          if(Validator.isNotEmpty(conduzioneDichiarataVO.getSuperficieAgronomica())) 
          {
            supAgronomica = StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieAgronomica());
          }
          else 
          {
            supAgronomica = SolmrConstants.DEFAULT_SUPERFICIE;
          }
          
          if(conduzioneDichiarataVO.getIdTitoloPossesso()
            .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            supAgronomica = StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta());
          }
          
          htmpl.set("blkParticelle.blkElencoParticelle.supAgronomica", supAgronomica);
          
          
					if(utilizzoDichiaratoVO != null) 
          {
						// Visualizzo i dati relativi all'uso, esclusa la superficie utilizzata che 
						// verrà visualizzata sempre, solo se si tratta di un utilizzo censito su DB
						htmpl.set("blkParticelle.blkElencoParticelle.idUtilizzo", utilizzoDichiaratoVO.getIdUtilizzoDichiarato().toString());
						if(utilizzoDichiaratoVO.getIdUtilizzoDichiarato().longValue() > 0) 
						{
							if(utilizzoDichiaratoVO.getTipoMacroUsoVO() != null) 
							{
								htmpl.set("blkParticelle.blkElencoParticelle.titleMacroUso", utilizzoDichiaratoVO.getTipoMacroUsoVO().getDescrizione());
								htmpl.set("blkParticelle.blkElencoParticelle.codiceMacroUso", utilizzoDichiaratoVO.getTipoMacroUsoVO().getCodice());
							}
							htmpl.set("blkParticelle.blkElencoParticelle.descUsoPrimario", "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()+"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione());
							if(utilizzoDichiaratoVO.getTipoDestinazione()!= null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDestinazione().getDescrizioneDestinazione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoDestinazionePrimario", "["+utilizzoDichiaratoVO.getTipoDestinazione().getCodiceDestinazione()+"] "+utilizzoDichiaratoVO.getTipoDestinazione().getDescrizioneDestinazione());
              }
							if(utilizzoDichiaratoVO.getTipoDettaglioUso() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDettaglioUso().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoDettUsoPrimario", "["+utilizzoDichiaratoVO.getTipoDettaglioUso().getCodiceDettaglioUso()+"] "+utilizzoDichiaratoVO.getTipoDettaglioUso().getDescrizione());
              }
              if(utilizzoDichiaratoVO.getTipoQualitaUso() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoQualitaUso().getDescrizioneQualitaUso())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoQualitaUsoPrimario", "["+utilizzoDichiaratoVO.getTipoQualitaUso().getCodiceQualitaUso()+"] "+utilizzoDichiaratoVO.getTipoQualitaUso().getDescrizioneQualitaUso());
              }
              if(utilizzoDichiaratoVO.getTipoVarietaVO() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descVarietaPrimario", "["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()+"] "+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione());
              }
              if(utilizzoDichiaratoVO.getTipoPeriodoSemina() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPeriodoSemina().getDescrizione())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoSeminaPrimario", "["+utilizzoDichiaratoVO.getTipoPeriodoSemina().getCodice()+"] "+utilizzoDichiaratoVO.getTipoPeriodoSemina().getDescrizione());
              }
              if(utilizzoDichiaratoVO.getTipoSemina() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoSemina().getDescrizioneSemina())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descSeminaPrimario", utilizzoDichiaratoVO.getTipoPeriodoSemina().getDescrizione());
              }
              
              htmpl.set("blkParticelle.blkElencoParticelle.dataInizioSeminaPrimario", DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataInizioDestinazione()));
              htmpl.set("blkParticelle.blkElencoParticelle.dataFineSeminaPrimario", DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataFineDestinazione()));
							
							if(utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO() != null) 
						  {
                htmpl.set("blkParticelle.blkElencoParticelle.descUsoSecondario", "["+utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getCodice()+"] "+utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getDescrizione());
                htmpl.set("blkParticelle.blkElencoParticelle.superficieSecondariaUtilizzata", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
                if(utilizzoDichiaratoVO.getTipoDestinazioneSecondario() != null 
                  && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoDestinazioneSecondario", "["+utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getCodiceDestinazione()+"] "+utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione());
                }
                if(utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario() != null 
                  && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getDescrizione())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoDettUsoSecondario", "["+utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getCodiceDettaglioUso()+"] "+utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getDescrizione());
                }
                if(utilizzoDichiaratoVO.getTipoQualitaUsoSecondario() != null 
                  && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoQualitaUsoSecondario", "["+utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getCodiceQualitaUso()+"] "+utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso());
                }
                if(utilizzoDichiaratoVO.getTipoVarietaSecondariaVO() != null) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descVarietaSecondaria", "["+utilizzoDichiaratoVO.getTipoVarietaSecondariaVO().getCodiceVarieta()+"] "+utilizzoDichiaratoVO.getTipoVarietaSecondariaVO().getDescrizione());
                }
                if(utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario() != null 
                  && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario().getDescrizione())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descTipoSeminaSecondario", utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario().getDescrizione());
                }
                if(utilizzoDichiaratoVO.getTipoSeminaSecondario() != null 
                  && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoSeminaSecondario().getDescrizioneSemina())) 
                {
                  htmpl.set("blkParticelle.blkElencoParticelle.descSeminaSecondario", utilizzoDichiaratoVO.getTipoSeminaSecondario().getDescrizioneSemina());
                }
                
                htmpl.set("blkParticelle.blkElencoParticelle.dataInizioSeminaSecondario", DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataInizioDestinazioneSec()));
                htmpl.set("blkParticelle.blkElencoParticelle.dataFineSeminaSecondario", DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataFineDestinazioneSec()));
              }
              
              if(utilizzoDichiaratoVO.getTipoPraticaMantenimento() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descMantenimento", utilizzoDichiaratoVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim());
              }
              
              if(utilizzoDichiaratoVO.getTipoFaseAllevamento() != null 
                && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento())) 
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descAllevamento", utilizzoDichiaratoVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento());
              }
              
              if(Validator.isNotEmpty(utilizzoDichiaratoVO.getIdTipoEfa()))
              {
                htmpl.set("blkParticelle.blkElencoParticelle.descTipoEfa", utilizzoDichiaratoVO.getDescTipoEfaEfa());
                htmpl.set("blkParticelle.blkElencoParticelle.valoreOriginale", Formatter.formatDouble4(utilizzoDichiaratoVO.getValoreOriginale()));
                htmpl.set("blkParticelle.blkElencoParticelle.descUnitaMisura", utilizzoDichiaratoVO.getDescUnitaMisuraEfa());
                htmpl.set("blkParticelle.blkElencoParticelle.valoreDopoConversione", Formatter.formatDouble4(utilizzoDichiaratoVO.getValoreDopoConversione()));
                htmpl.set("blkParticelle.blkElencoParticelle.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoDichiaratoVO.getValoreDopoPonderazione()));
              }
						}
						
            
						htmpl.set("blkParticelle.blkElencoParticelle.superficieUtilizzata", 
              StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
					}
          //asservimento
          else
          {
            if(conduzioneDichiarataVO.getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
            {
              htmpl.set("blkParticelle.blkElencoParticelle.superficieUtilizzata", 
                SolmrConstants.DEFAULT_SUPERFICIE);
            }
          }
					
					
					if(storicoParticellaVO.getIdCasoParticolare() != null) 
					{
            htmpl.set("blkParticelle.blkElencoParticelle.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());           
          }
         
					
          if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_N)
            && filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_N))
          {
            htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", "1");
            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle");
            htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle");
						
					}
					if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
						AnagAziendaVO[] elenco = (AnagAziendaVO[])elencoAziende.get(storicoParticellaVO.getIdStoricoParticella());
						htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", String.valueOf(elenco.length));
						
						for(int i = 0; i < elenco.length; i++) {
							AnagAziendaVO anagAziendaVOTmp = elenco[i];
							if(i == 0) {
								htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle");
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.cuaaAss", anagAziendaVOTmp.getCUAA());
								String denominazione = "";
								if(anagAziendaVOTmp.getDenominazione().length() > 20) {
									denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
								}
								else {
									denominazione = anagAziendaVOTmp.getDenominazione();
								}
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.denominazioneAss", denominazione);
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoSingle.aziendeAsservite", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
							}
							else {
								htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple");
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.cuaaAss", anagAziendaVOTmp.getCUAA());
								String denominazione = "";
								if(anagAziendaVOTmp.getDenominazione().length() > 20) {
									denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
								}
								else {
									denominazione = anagAziendaVOTmp.getDenominazione();
								}
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.denominazioneAss", denominazione);
								htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaAsservimentoMultiple.aziendeAsservite", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
							}
						}
					}
					
          if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            AnagAziendaVO[] elenco = (AnagAziendaVO[])elencoAziendeConferite.get(storicoParticellaVO.getIdStoricoParticella());
            
          
          
            htmpl.set("blkParticelle.blkElencoParticelle.valoreRowspan", String.valueOf(elenco.length));
            
            for(int i = 0; i < elenco.length; i++) 
            {
              AnagAziendaVO anagAziendaVOTmp = elenco[i];
              if(i == 0) 
              {
                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle");
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.cuaaConf", anagAziendaVOTmp.getCUAA());
                String denominazione = "";
                if(anagAziendaVOTmp.getDenominazione().length() > 20) {
                  denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
                }
                else {
                  denominazione = anagAziendaVOTmp.getDenominazione();
                }
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.denominazioneConf", denominazione);
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoSingle.aziendeConferita", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
              }
              else 
              {
                htmpl.newBlock("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple");
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.cuaaConf", anagAziendaVOTmp.getCUAA());
                String denominazione = "";
                if(anagAziendaVOTmp.getDenominazione().length() > 20) {
                  denominazione = anagAziendaVOTmp.getDenominazione().substring(0, 20);
                }
                else {
                  denominazione = anagAziendaVOTmp.getDenominazione();
                }
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.denominazioneConf", denominazione);
                htmpl.set("blkParticelle.blkElencoParticelle.blkAziendaConferimentoMultiple.aziendeConferita", anagAziendaVOTmp.getCUAA()+" - "+anagAziendaVOTmp.getDenominazione());
              }
            }
          }
          
          
				}
			}
		} //if particelle
		else
		{
		  htmpl.newBlock("blkNoParticelle");
		}
	}
	
	//  Monitoraggio
    watcher.dumpElapsed("terreniParticellareElencoView.jsp", "Viewer of terreni search area", "In terreniParticellareElencoView.jsp from the beginning to the end", null);

%>
<%= htmpl.text()%>

<%!
  public void aggiungiBloccoFiltro(int count, Htmpl htmpl)
  {
    if((count % 3) == 0)
    {
      htmpl.newBlock("blkElencoFiltri");
    }
  }
  
  public void aggiungiFiltro(int count, Htmpl htmpl, String valFltro)
  {
    if((count % 3) == 0)
    {
      htmpl.set("blkElencoFiltri.filtroZero",valFltro);
    }
    
    if((count % 3) == 1)
    {
      htmpl.set("blkElencoFiltri.filtroUno",valFltro);
    }
    
    if((count % 3) == 2)
    {
      htmpl.set("blkElencoFiltri.filtroDue",valFltro);
    }
    
  }
  

 %>

