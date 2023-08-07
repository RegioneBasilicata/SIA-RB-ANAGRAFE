  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/anagrafica.htm");
	Htmpl htmpl = new Htmpl(layout);
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	//Devo cambiare la label della provincia di competenza a seconda che sono
	//in piemonte o in sardegna
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_PIEMONTE);
	
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");
	AnagAziendaVO anagAziendaProvenienzaVO = (AnagAziendaVO)request.getAttribute("anagAziendaProvenienzaVO");
	AnagAziendaVO[] elencoAziendeDestinazione = (AnagAziendaVO[])request.getAttribute("elencoAziendeDestinazione");
	// Se  il VO vuol dire che arrivo dalla ricerca dell'azienda e non dall'inserimento
	// Se  il VO vuol dire che arrivo dalla ricerca dell'azienda e non dall'inserimento
  if(anagAziendaVO == null) 
  {
    anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  }  
  
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	if(anagAziendaVO != null && anagAziendaVO.getDataCessazione() != null 
    && !anagAziendaVO.getDataCessazione().equals("")) 
  {
  	htmpl.set("dataCessazione", DateUtils.formatDate(anagAziendaVO.getDataCessazione()));
	}
	if(anagAziendaVO != null && anagAziendaVO.getTipoFormaGiuridica()!= null 
    && anagAziendaVO.getTipoFormaGiuridica().getDescription()!= null 
    && !anagAziendaVO.getTipoFormaGiuridica().getDescription().equals("")) 
  {
  	anagAziendaVO.setTipiFormaGiuridica(anagAziendaVO.getTipoFormaGiuridica().getDescription());
	}

	if(anagAziendaVO.isFlagAziendaProvvisoria()) 
  {
  	htmpl.newBlock("blkFaseCostituzione");
     if (anagAziendaVO.isFlagAziendaProvvisoria())
       htmpl.set("blkFaseCostituzione.faseCostituzione", (String) SolmrConstants.get("FASE_COSTITUZIONE_CUAA_INSEDIAMENTO_GIOVANI"));
  }
	if(anagAziendaProvenienzaVO != null) 
  {
		htmpl.newBlock("blkProvenienza");
		htmpl.set("blkProvenienza.cuaaProvenienza", anagAziendaProvenienzaVO.getCUAA());
		if(Validator.isNotEmpty(anagAziendaProvenienzaVO.getDenominazione())) 
    {
			htmpl.set("blkProvenienza.denominazioneProvenienza", " - "+anagAziendaProvenienzaVO.getDenominazione());
		}
	}
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
  if(anagAziendaVO.getIdCessazione() != null) 
  {
		if(anagAziendaVO.getTipoCessazioneVO() != null) 
    {
		  htmpl.set("tipoCessazione", anagAziendaVO.getTipoCessazioneVO().getDescrizione());
		}
		if(Validator.isNotEmpty(anagAziendaVO.getCausaleCessazione())) 
    {
			if(anagAziendaVO.getCausaleCessazione().length() > 100) 
      {
				htmpl.set("causaleCessazione", " - "+anagAziendaVO.getCausaleCessazione().substring(0, 100));
			}
			else 
      {
				htmpl.set("causaleCessazione", " - "+anagAziendaVO.getCausaleCessazione());
			}
		}
	}
	if(Validator.isNotEmpty(anagAziendaVO.getCausaleCessazione())) 
  {
		if(anagAziendaVO.getCausaleCessazione().length() > 100) 
    {
			htmpl.set("causaleCessazione", anagAziendaVO.getCausaleCessazione().substring(0, 100));
		}
		else 
    {
			htmpl.set("causaleCessazione", anagAziendaVO.getCausaleCessazione());
		}
	}
	
	//pop notifiche
	Vector<NotificaVO> elencoNotifiche = (Vector<NotificaVO>)request.getAttribute("elencoNotifiche");
	if(Validator.isNotEmpty(elencoNotifiche))
	{
	  htmpl.newBlock("blkjQuery");
	  for (int i=0; i < elencoNotifiche.size(); i++)
    {
      NotificaVO notificaVO = (NotificaVO) elencoNotifiche.get(i);
      htmpl.newBlock("blkElencoNotifiche");     

      htmpl.set("blkElencoNotifiche.descCategoriaNotifica", notificaVO.getDescCategoriaNotifica());
      //htmpl.set("blkElencoNotifiche.descTipologiaNotifica", notificaVO.getDescTipologiaNotifica());
      
      if(Validator.isNotEmpty(notificaVO.getDescrizione()))
      {
	      if (notificaVO.getDescrizione().length() > 100)
	      {
	        htmpl.set("blkElencoNotifiche.descrizione", " - "+notificaVO.getDescrizione().substring(0, 100) + " [...]");
	        htmpl.set("blkElencoNotifiche.descCompleta", notificaVO.getDescrizione());
	      }
	      else
	      {
	        htmpl.set("blkElencoNotifiche.descrizione", " - "+ notificaVO.getDescrizione());
	      }
	    }
        
    }
	}
	
	
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(anagAziendaVO.getDataUltimaModifica() !=null)
  {
    dateUlt = DateUtils.formatDate(anagAziendaVO.getDataUltimaModifica());
  } 
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, anagAziendaVO.getUtenteUltimaModifica(),
    anagAziendaVO.getEnteUltimaModifica(), anagAziendaVO.getMotivoModifica());
    
    
  //aggiungere qui i dati della nuova tabella delle associazioni collegate
  Vector<AnagAziendaVO> associazioniCollegate = (Vector<AnagAziendaVO>)request.getAttribute("associazioniCollegateVector");
  if(associazioniCollegate!=null&&associazioniCollegate.size()>0)
  {
    htmpl.newBlock("blkAssociazioniCollegate");
    htmpl.newBlock("blkRiga");
    int dim = associazioniCollegate.size();
    
    for(int i=0; i<dim; i++)
    {
      AnagAziendaVO azCollegata = (AnagAziendaVO)associazioniCollegate.get(i);
      htmpl.set("blkAssociazioniCollegate.blkRiga.cuaaCollegati", azCollegata.getCUAA());
      htmpl.set("blkAssociazioniCollegate.blkRiga.partitaIvaCollegati", azCollegata.getPartitaIVA());
      htmpl.set("blkAssociazioniCollegate.blkRiga.nomeAziendaCollegati", azCollegata.getDenominazione());
      htmpl.set("blkAssociazioniCollegate.blkRiga.comuneCollegati", azCollegata.getSedelegComune()+" ("+azCollegata.getSedelegProv()+")");
      htmpl.set("blkAssociazioniCollegate.blkRiga.indirizzoCollegati", azCollegata.getSedelegIndirizzo());
      htmpl.set("blkAssociazioniCollegate.blkRiga.capCollegati", azCollegata.getSedelegCAP());
    }
  }
  
  htmpl.set("cuaa", anagAziendaVO.getCUAA());
  htmpl.set("partitaIVA", anagAziendaVO.getPartitaIVA());
  htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  htmpl.set("intestazionePartitaIva", anagAziendaVO.getIntestazionePartitaIva());
  htmpl.set("strTipoAzienda", anagAziendaVO.getTipoTipologiaAzienda().getDescription());
  htmpl.set("tipiFormaGiuridica", anagAziendaVO.getTipiFormaGiuridica());
  htmpl.set("descrizioneProvCompetenza", anagAziendaVO.getDescrizioneProvCompetenza());
  htmpl.set("mail", anagAziendaVO.getMail());
  htmpl.set("sito", anagAziendaVO.getSitoWEB());
  htmpl.set("telefono", anagAziendaVO.getTelefono());
  htmpl.set("fax", anagAziendaVO.getFax());
  htmpl.set("pec", anagAziendaVO.getPec());
  htmpl.set("note", anagAziendaVO.getNote());
  
  
  /*if(Validator.isNotEmpty(anagAziendaVO.getCodiceAgriturismo()))
  {
    altreInformazioni = "Azienda agrituristica";
  }
  
  if(Validator.isNotEmpty(gaaFacadeClient.getOperatoreBiologicoAttivo(anagAziendaVO.getIdAzienda())))
  {
    if(Validator.isNotEmpty(altreInformazioni))
    {
      altreInformazioni += " e biologica";
    }
    else
    {
      altreInformazioni = "Azienda biologica";
    }
  }*/
  String altreInformazioni = "";
  Vector<TipoInfoAggiuntivaVO> vTipoInfoAggiuntiva = gaaFacadeClient
    .getTipoInfoAggiuntive(anagAziendaVO.getIdAzienda());
  if(Validator.isNotEmpty(vTipoInfoAggiuntiva))
  {
    for(int i=0;i<vTipoInfoAggiuntiva.size();i++)
    {
      if(i !=0)
      {
        altreInformazioni +="<br>";
      }
      altreInformazioni += vTipoInfoAggiuntiva.get(i).getDescrizione();
    }
  }  
  htmpl.set("altreInformazioni", altreInformazioni, null);
  
	//HtmplUtil.setValues(htmpl, anagAziendaVO);
	HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
