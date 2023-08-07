<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  	

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagrafica_mod.htm");

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaProvenienzaVO = (AnagAziendaVO)request.getAttribute("anagAziendaProvenienzaVO"); 
  AnagAziendaVO[] elencoAziendeDestinazione = (AnagAziendaVO[])request.getAttribute("elencoAziendeDestinazione");
  
  //Vettore di AziendaAtecoSecVO
  //Vector vAziendaAtecoSec = (Vector)session.getAttribute("vAziendaAtecoSec");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Devo cambiare la label della provincia di competenza a seconda che sono
  // in piemonte o in sardegna
  boolean piemonte=SolmrConstants.ID_REG_PIEMONTE.equals(ruoloUtenza.getIstatRegioneAttiva());
  if(piemonte) 
  {
    htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_PIEMONTE);
  }
  else 
  {
    htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_SARDEGNA);
  }

  AnagAziendaVO vo = (AnagAziendaVO)session.getAttribute("voAnagModifica");


  String richiestaModifica = (String)session.getAttribute("richiestaModifica");
  session.removeAttribute("richiestaModifica");
  if(richiestaModifica != null) 
  {
    htmpl.set("richiestaModifica",richiestaModifica);
  }
  else 
  {
    htmpl.set("richiestaModifica","");
  }
  
  SolmrLogger.debug(this, "-- modificaCampiAnagrafici ="+session.getAttribute("modificaCampiAnagrafici"));
  if(session.getAttribute("modificaCampiAnagrafici") != null)
  {
	SolmrLogger.debug(this, "-- DISABILITO I CAMPI DELLA MODIFICA AZIENDA"); 
    htmpl.set("disabled","disabled=\"disabled\"", null);
    htmpl.newBlock("blkFieldDisabled");
    if(Validator.isNotEmpty(anagAziendaVO.getPartitaIVA()))
      htmpl.set("blkFieldDisabled.partitaIVA", anagAziendaVO.getPartitaIVA());
    if(Validator.isNotEmpty(anagAziendaVO.getDenominazione()))
      htmpl.set("blkFieldDisabled.denominazione", anagAziendaVO.getDenominazione());
    if(Validator.isNotEmpty(anagAziendaVO.getIntestazionePartitaIva()))
      htmpl.set("blkFieldDisabled.intestazionePartitaIva", anagAziendaVO.getIntestazionePartitaIva());
    if(Validator.isNotEmpty(anagAziendaVO.getTipiAzienda()))
      htmpl.set("blkFieldDisabled.tipiAzienda", anagAziendaVO.getTipiAzienda());
    if(Validator.isNotEmpty(anagAziendaVO.getTipoFormaGiuridica()))
      htmpl.set("blkFieldDisabled.tipiFormaGiuridica", ""+anagAziendaVO.getTipoFormaGiuridica().getCode());
    if(Validator.isNotEmpty(anagAziendaVO.getProvincePiemonte()))
      htmpl.set("blkFieldDisabled.provincePiemonte", anagAziendaVO.getProvincePiemonte());
    if(Validator.isNotEmpty(anagAziendaVO.getNote()))
      htmpl.set("blkFieldDisabled.note", anagAziendaVO.getNote());
  }

  // Controllo che, se il cuaa risulta errato, sia inserito in un campo di text affinchè possa essere
  // modificato dall'utente
  String isOk = (String)session.getAttribute("isOk");
  if(Validator.isNotEmpty(isOk)) 
  {
    if(isOk.equalsIgnoreCase("true")) 
    {
      if(vo.getCUAA() != null) 
      {
        htmpl.set("cuaa", vo.getCUAA().toUpperCase());
      }
      else 
      {
        htmpl.set("cuaa", anagAziendaVO.getCUAA().toUpperCase());
      }
      htmpl.set("readOnly", "readOnly");
    }
    else 
    {
      if(vo.getCUAA() != null) 
      {
        htmpl.set("cuaa", vo.getCUAA().toUpperCase());
      }
      else 
      {
        if(Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
        {
          htmpl.set("cuaa", anagAziendaVO.getCUAA().toUpperCase());
        }
      }
   	}
 	}
  
  Vector elencoProvince = anagFacadeClient.getProvinceByRegione(ruoloUtenza.getIstatRegioneAttiva());
  String codProvincia = request.getParameter("provincePiemonte");
  if(codProvincia == null) 
  {
    codProvincia = anagAziendaVO.getProvCompetenza();
  }
  Iterator iteraProvince = elencoProvince.iterator();
  while(iteraProvince.hasNext()) 
  {
    htmpl.newBlock("codiceProvince");
    ProvinciaVO province = (ProvinciaVO)iteraProvince.next();
    if(codProvincia.equalsIgnoreCase(province.getIstatProvincia())) 
    {
      htmpl.set("codiceProvince.check", "selected");
    }
    htmpl.set("codiceProvince.idCodice",province.getIstatProvincia());
    htmpl.set("codiceProvince.descrizione",province.getDescrizione());
  }

  
  
  
  
  if(errors == null) 
  {
    
    
    if(vo != null && session.getAttribute("load") != null) 
    {
      session.removeAttribute("load");
      
      
      
      htmpl.set("CUAA",vo.getCUAA());
      HtmplUtil.setValues(htmpl, vo);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
    else 
    {
      HtmplUtil.setValues(htmpl, request);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
  }
  else 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }
  if(anagAziendaVO.isFlagAziendaProvvisoria()) 
  {
    htmpl.newBlock("blkFaseCostituzione");
    if(anagAziendaVO.getIdAziendaSubentro() != null) 
    {
      String cuaaSubentro = "";
      try 
      {
        AnagAziendaVO temp = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
        if(temp != null) 
        {
          cuaaSubentro=temp.getCUAA();
        }
      }
      catch(Exception e) {}
      htmpl.set("blkFaseCostituzione.faseCostituzione", (String) SolmrConstants.get("FASE_COSTITUZIONE_CUAA_SUBENTRO")+" "+cuaaSubentro);
    }
    else
    { 
      htmpl.set("blkFaseCostituzione.faseCostituzione", (String) SolmrConstants.get("FASE_COSTITUZIONE_CUAA_INSEDIAMENTO_GIOVANI"));
  	}
  }

  // Carico le combo dei tipi azienda e delle forme giuridiche.
  // Lo faccio qua altrimenti dovrei duplicarlo nei due controller che
  // chiamano la pagina
  Collection collTipiAzienda = null;
  if(anagAziendaVO.isFlagAziendaProvvisoria()) 
  {
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,new Boolean(true));
  }
  else 
  {
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,null);
  }
  if(collTipiAzienda != null && collTipiAzienda.size() > 0) 
  {
    Iterator iterFormaGiuridica = collTipiAzienda.iterator();
    while(iterFormaGiuridica.hasNext()) 
    {
      TipoTipologiaAziendaVO tipiAzienda = (TipoTipologiaAziendaVO)iterFormaGiuridica.next();
      htmpl.newBlock("cmbTipiAzienda");
      htmpl.set("cmbTipiAzienda.idTipiAzienda",""+tipiAzienda.getIdTipologiaAzienda());
      htmpl.set("cmbTipiAzienda.descTipiAzienda",tipiAzienda.getDescrizione());
      if(vo != null) 
      {
        if(tipiAzienda.getIdTipologiaAzienda().equals(vo.getTipiAzienda())) 
        {
          htmpl.set("cmbTipiAzienda.selected","selected");
        }
      }
      else 
      {
        if(anagAziendaVO != null && tipiAzienda.getIdTipologiaAzienda().equals(anagAziendaVO.getTipiAzienda())) 
        {
          htmpl.set("cmbTipiAzienda.selected","selected");
        }
      }
    }
  }
  
  
 	if((anagAziendaVO != null && anagAziendaVO.getTipiAzienda() != null) 
    || (vo != null && vo.getTipiAzienda() != null)) 
  {
    Collection collFormaGiuridica = null;
    if(vo != null) 
    {
      if(vo.getTipiAzienda() != null) 
      {
        collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(vo.getTipiAzienda()));
      }
    }
    else 
    {
      if(anagAziendaVO.getTipiAzienda() != null) 
      {
        collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(anagAziendaVO.getTipiAzienda()));
      }
    }
    
    if(collFormaGiuridica != null && collFormaGiuridica.size() > 0) 
    {
      Iterator iterFormaGiuridica = collFormaGiuridica.iterator();
      while(iterFormaGiuridica.hasNext()) 
      {
        CodeDescription cdFormaGiuridica = (CodeDescription)iterFormaGiuridica.next();
        htmpl.newBlock("cmbTipoFormaGiuridica");
        htmpl.set("cmbTipoFormaGiuridica.idTipiFormaGiuridica",""+cdFormaGiuridica.getCode());
        htmpl.set("cmbTipoFormaGiuridica.descTipiFormaGiuridica",cdFormaGiuridica.getDescription());
        if(vo != null) 
        {
          if(vo.getTipoFormaGiuridica() != null) 
          {
            if(cdFormaGiuridica.getCode().equals(vo.getTipoFormaGiuridica().getCode()))
            {
              htmpl.set("cmbTipoFormaGiuridica.selected","selected");
            }
          }
        }
        else 
        {
          if(anagAziendaVO.getTipoFormaGiuridica() != null) 
          {
            if(anagAziendaVO!=null && cdFormaGiuridica.getCode().equals(anagAziendaVO.getTipoFormaGiuridica().getCode()))
            {
              htmpl.set("cmbTipoFormaGiuridica.selected","selected");
            }
          }
        }
      }
    }
  }
  
  
 
  
  // Azienda di provenienza
  if(anagAziendaProvenienzaVO != null) 
  {
    htmpl.newBlock("blkProvenienza");
  	htmpl.set("blkProvenienza.cuaaProvenienza", anagAziendaProvenienzaVO.getCUAA());
  	if(Validator.isNotEmpty(anagAziendaProvenienzaVO.getDenominazione())) 
    {
  	  htmpl.set("blkProvenienza.denominazioneProvenienza", " - "+anagAziendaProvenienzaVO.getDenominazione());
  	}
  }
  // Azienda/e di destinazione
  if(elencoAziendeDestinazione != null && elencoAziendeDestinazione.length > 0) 
  {
    htmpl.newBlock("blkDestinazione");
  	if(elencoAziendeDestinazione.length == 1) 
    {
  	  htmpl.set("blkDestinazione.labelDestinazione", "Azienda di destinazione");
  		htmpl.newBlock("blkDestinazione.blkElencoD");
  		htmpl.set("blkDestinazione.blkElencoD.cuaaDestinazione", elencoAziendeDestinazione[0].getCUAA());
  	  if(Validator.isNotEmpty(elencoAziendeDestinazione[0].getDenominazione())) 
      {
  	    htmpl.set("blkDestinazione.blkElencoD.denominazioneDestinazione", " - "+elencoAziendeDestinazione[0].getDenominazione());
  	  }
  	}
  	else 
    {
      htmpl.set("blkDestinazione.labelDestinazione", "Aziende di destinazione");
  		for(int i = 0; i < elencoAziendeDestinazione.length; i++) 
      {
  		  htmpl.newBlock("blkDestinazione.blkElencoD");
  			AnagAziendaVO anagDestinazione = (AnagAziendaVO)elencoAziendeDestinazione[i];
  			htmpl.set("blkDestinazione.blkElencoD.cuaaDestinazione", anagDestinazione.getCUAA());
  			if(Validator.isNotEmpty(anagDestinazione.getDenominazione())) 
        {
  	  	  htmpl.set("blkDestinazione.blkElencoD.denominazioneDestinazione", " - "+anagDestinazione.getDenominazione());
  	  	}
  			if(i < elencoAziendeDestinazione.length - 1) 
        {
  			  htmpl.set("blkDestinazione.blkElencoD.separatore", "; ");  					
  			}
  		}
  	}
  }
  
  

%>
<%= htmpl.text()%>
