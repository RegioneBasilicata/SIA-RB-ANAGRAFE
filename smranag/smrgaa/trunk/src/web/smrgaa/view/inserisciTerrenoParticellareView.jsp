<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/inserisciTerrenoParticellare.htm");
  Htmpl htmpl = new Htmpl(layout);
  
  %>
      <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String provvisoria = (String)request.getAttribute("provvisoria");
  Long idEvento = (Long)request.getAttribute("idEvento");
  String isCertificata = (String)session.getAttribute("isCertificata");
  Vector<CasoParticolareVO> elencoCasiParticolari = (Vector<CasoParticolareVO>)request.getAttribute("elencoCasiParticolari");
  TipoIrrigazioneVO[] elencoIrrigazione = (TipoIrrigazioneVO[])request.getAttribute("elencoIrrigazione");
  TipoRotazioneColturaleVO[] elencoRotazioneColturale = (TipoRotazioneColturaleVO[])request.getAttribute("elencoRotazioneColturale");
  TipoTerrazzamentoVO[] elencoTerrazzamenti = (TipoTerrazzamentoVO[])request.getAttribute("elencoTerrazzamenti");
  Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = (Vector<TipoMetodoIrriguoVO>)request.getAttribute("vMetodoIrriguo");
  Vector<TipoAreaVO> vTipoArea = (Vector<TipoAreaVO>)request.getAttribute("vTipoArea");
  String confermaSupCatFraz = (String)request.getAttribute("confermaSupCatFraz");
  String confermaSupCatAcc = (String)request.getAttribute("confermaSupCatAcc");
  String isPiemontese = (String)request.getAttribute("isPiemontese");
  String regimeInserisciTerrenoParticellare = request.getParameter("regimeInserisciTerrenoParticellare");
  
  htmpl.set("provvisoria", provvisoria);
  if(idEvento != null) {
    htmpl.set("idEvento", idEvento.toString());
  }
  
  if(Validator.isNotEmpty(confermaSupCatFraz)) {
    htmpl.set("confermaSupCatFraz", confermaSupCatFraz);
  }
  if(Validator.isNotEmpty(confermaSupCatAcc)) {
    htmpl.set("confermaSupCatAcc", confermaSupCatAcc);
  }
  
  
  String flagObbligoCatastale = (String)session.getAttribute("flagObbligoCatastale");  
  
  
  // Dati chiave logica della particella:
  // Visualizzo il GIS solo se la particella risulta censita in archivio e non è stata dichiarata provvisoria dall'utente
  if(storicoParticellaVO.getIdStoricoParticella() != null && Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
    htmpl.newBlock("blkAbacoGis");
    htmpl.set("blkAbacoGis.idStoricoParticella", storicoParticellaVO.getIdStoricoParticella().toString());
  }
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
  
  //htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
  
  // Dati catastali
  htmpl.set("supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
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
  if(elencoCasiParticolari != null)
  {
	  for(int l = 0; l < elencoCasiParticolari.size(); l++) 
	  {
	    CasoParticolareVO casoParticolareVO = elencoCasiParticolari.get(l);
	    htmpl.newBlock("blkTipiCasoParticolare");
	    htmpl.set("blkTipiCasoParticolare.idCasoParticolare", ""+casoParticolareVO.getIdCasoParticolare());
	    htmpl.set("blkTipiCasoParticolare.descrizione", ""+casoParticolareVO.getIdCasoParticolare());
	    if(storicoParticellaVO.getIdCasoParticolare() != null) 
	    {
	      if(storicoParticellaVO.getIdCasoParticolare()
	        .compareTo(new Long(casoParticolareVO.getIdCasoParticolare())) == 0) 
	      {
	        htmpl.set("blkTipiCasoParticolare.selected", "selected=\"selected\"", null);
	      }
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
            if(Validator.isEmpty(regimeInserisciTerrenoParticellare))
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
            if(Validator.isEmpty(regimeInserisciTerrenoParticellare))
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
  
  
  
  

  
  // Gestione degli errori
  if(errors != null && errors.size() > 0) {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>
<%= htmpl.text()%>
 