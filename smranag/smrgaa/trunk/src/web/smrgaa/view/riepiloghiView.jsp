<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@page import="java.math.BigDecimal"%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/riepiloghi.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
  	<%@include file = "/view/remoteInclude.inc" %>
  <%

	Long idTipoRicerca = (Long)request.getAttribute("idTipoRicerca");
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	String escludiAsservimento = (String)request.getAttribute("escludiAsservimento");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");  
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  Vector<TipoRiepilogoVO> vTipoRiepilogo = (Vector<TipoRiepilogoVO>)request.getAttribute("vTipoRiepilogo");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  Long idPianoRiferimento = null;
  if(Validator.isEmpty(idDichiarazioneConsistenza)) 
  {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
  }
  else 
  {
    idPianoRiferimento = Long.decode(idDichiarazioneConsistenza);
  }
	
	HtmplUtil.setErrors(htmpl, errors, request, application);
	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	
	if(Validator.isNotEmpty(vTipoRiepilogo))
	{
		for(int i=0;i<vTipoRiepilogo.size();i++) 
		{
	    TipoRiepilogoVO tipoRiepilogoVO = vTipoRiepilogo.get(i);
	    htmpl.newBlock("blkTipoRiepilogo");
	    htmpl.set("blkTipoRiepilogo.idTipoRiepilogo", ""+tipoRiepilogoVO.getIdTipoRiepilogo());
	    htmpl.set("blkTipoRiepilogo.descrizione", tipoRiepilogoVO.getNome());
	    if((idTipoRicerca != null) && tipoRiepilogoVO.getIdTipoRiepilogo() == idTipoRicerca.longValue()) 
	    {
	      htmpl.set("blkTipoRiepilogo.selected", "selected=\"selected\"", null);
	    }
	    else
	    {
	      if("S".equalsIgnoreCase(tipoRiepilogoVO.getFlagDefault()))
	      {
	        htmpl.set("blkTipoRiepilogo.selected", "selected=\"selected\"", null);
	      }
	    }
	    
	    htmpl.newBlock("blkTipoRiepilogoHidden");
	    htmpl.set("blkTipoRiepilogoHidden.flagEscludiAsservimento", tipoRiepilogoVO.getFlagEscludiAsservimento());
      htmpl.set("blkTipoRiepilogoHidden.idTipoRiepilogoAsservimento", ""+tipoRiepilogoVO.getIdTipoRiepilogo());
	    
	  }
	}
	
	if(Validator.isNotEmpty(escludiAsservimento) 
	  && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	{
    htmpl.set("checkedEscludiAsservimento", "checked=\"checked\"", null);
  }
  	
 	
	// Combo Piano di riferimento
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);

	// Nel caso in cui si sia verificato un errore durante l'elaborazione dei
	// riepiloghi
	if(Validator.isNotEmpty(messaggioErrore)) 
  {
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	else 
  {
		// RIEPILOGO TITOLO POSSESSO
		if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO) == 0) 
    {
			BigDecimal totSupUtilizzata = new BigDecimal(0);
			if(idPianoRiferimento.intValue() == -1) 
      {
				StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoParticelleRiepiloghi");
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoPossesso");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
						StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
						htmpl.newBlock("blkRisultatoRiepilogoPossesso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.idTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getCode().toString());
						htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.descrizioneTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getDescription());
            String supUtilizzata = storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata();
            //Se asservimento prendo la sup condotta
            if(Validator.isNotEmpty(conduzioneParticellaVO.getTitoloPossesso())
             && conduzioneParticellaVO.getTitoloPossesso().getCode().longValue() == SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO)
            {
              supUtilizzata = conduzioneParticellaVO.getSuperficieCondotta();
            }
						htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(supUtilizzata));
						totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(supUtilizzata));
					}
					htmpl.set("blkRisultatoRiepilogoPossesso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else 
      {
				StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoParticelleRiepiloghi");
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoPossesso");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
						StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaVO.getElencoConduzioniDichiarate()[0];
						htmpl.newBlock("blkRisultatoRiepilogoPossesso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.idTitoloPossesso", conduzioneDichiarataVO.getTitoloPossesso().getCode().toString());
						htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.descrizioneTitoloPossesso", conduzioneDichiarataVO.getTitoloPossesso().getDescription());
						String supUtilizzata = storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata();
            //Se asservimento prendo la sup condotta
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getTitoloPossesso())
             && conduzioneDichiarataVO.getTitoloPossesso().getCode().longValue() == SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO)
            {
              supUtilizzata = conduzioneDichiarataVO.getSuperficieCondotta();
            }
            htmpl.set("blkRisultatoRiepilogoPossesso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(supUtilizzata));
						totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(supUtilizzata));
					}
					htmpl.set("blkRisultatoRiepilogoPossesso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
		// RIEPILOGO TITOLO POSSESSO COMUNE
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO_COMUNE) == 0) 
    {
			BigDecimal totSupUtilizzata = new BigDecimal(0);
			StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
			if(idPianoRiferimento.intValue() == -1) 
      {
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoPossComune");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
						htmpl.newBlock("blkRisultatoRiepilogoPossComune.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.indice", String.valueOf(i));
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.descrizioneTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getDescription());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.idTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getCode().toString());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.descrizioneComuneParticella", elencoStorico[i].getComuneParticellaVO().getDescom());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.siglaProvinciaParticella", elencoStorico[i].getComuneParticellaVO().getSiglaProv());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.istatComuneParticella", elencoStorico[i].getIstatComune());
						String supUtilizzata = storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata();
            //Se asservimento prendo la sup condotta
            if(Validator.isNotEmpty(conduzioneParticellaVO.getTitoloPossesso())
             && conduzioneParticellaVO.getTitoloPossesso().getCode().longValue() == SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO)
            {
              supUtilizzata = conduzioneParticellaVO.getSuperficieCondotta();
            }
            htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(supUtilizzata));
            totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(supUtilizzata));
					}
					htmpl.set("blkRisultatoRiepilogoPossComune.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else 
      {
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoPossComune");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaVO.getElencoConduzioniDichiarate()[0];
						htmpl.newBlock("blkRisultatoRiepilogoPossComune.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.indice", String.valueOf(i));
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.descrizioneTitoloPossesso", conduzioneDichiarataVO.getTitoloPossesso().getDescription());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.idTitoloPossesso", conduzioneDichiarataVO.getTitoloPossesso().getCode().toString());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.descrizioneComuneParticella", elencoStorico[i].getComuneParticellaVO().getDescom());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.siglaProvinciaParticella", elencoStorico[i].getComuneParticellaVO().getSiglaProv());
						htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.istatComuneParticella", elencoStorico[i].getIstatComune());
						String supUtilizzata = storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata();
            //Se asservimento prendo la sup condotta
            if(Validator.isNotEmpty(conduzioneDichiarataVO.getTitoloPossesso())
             && conduzioneDichiarataVO.getTitoloPossesso().getCode().longValue() == SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO)
            {
              supUtilizzata = conduzioneDichiarataVO.getSuperficieCondotta();
            }
            htmpl.set("blkRisultatoRiepilogoPossComune.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(supUtilizzata));
            totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(supUtilizzata));
					}
					htmpl.set("blkRisultatoRiepilogoPossComune.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			    htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
		// RIEPILOGO COMUNE
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_COMUNE) == 0) 
    {
			BigDecimal totSupUtilizzata = new BigDecimal(0);
			StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
			if(idPianoRiferimento.intValue() == -1) 
      {
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoComune");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
						htmpl.newBlock("blkRisultatoRiepilogoComune.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.indice", String.valueOf(i));
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.descrizioneComuneParticella", elencoStorico[i].getComuneParticellaVO().getDescom());
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.siglaProvinciaParticella", elencoStorico[i].getComuneParticellaVO().getSiglaProv());
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.istatComuneParticella", elencoStorico[i].getIstatComune());
            htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.supUtilizzata", 
              StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
					}
					htmpl.set("blkRisultatoRiepilogoComune.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else 
      {
				if(elencoStorico != null && elencoStorico.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoComune");
					for(int i = 0; i < elencoStorico.length; i++) 
          {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            ConduzioneDichiarataVO conduzioneDichiarataVO = storicoParticellaVO.getElencoConduzioniDichiarate()[0];
						htmpl.newBlock("blkRisultatoRiepilogoComune.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.indice", String.valueOf(i));
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.descrizioneComuneParticella", elencoStorico[i].getComuneParticellaVO().getDescom());
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.siglaProvinciaParticella", elencoStorico[i].getComuneParticellaVO().getSiglaProv());
						htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.istatComuneParticella", elencoStorico[i].getIstatComune());
            htmpl.set("blkRisultatoRiepilogoComune.blkElencoRisultato.supUtilizzata", 
              StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
					}
					htmpl.set("blkRisultatoRiepilogoComune.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
		// RIEPILOGO DESTINAZIONE D'USO
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_USO) == 0) 
    {
			BigDecimal totSupUtilizzate = new BigDecimal(0);
			BigDecimal totSupUtilizzateSAU = new BigDecimal(0);
			if(idPianoRiferimento.intValue() == -1) 
      {
        BigDecimal totSenzaUsoSuolo = (BigDecimal)request.getAttribute("totSenzaUsoSuolo");
				UtilizzoParticellaVO[] elencoUtilizzi = (UtilizzoParticellaVO[])request.getAttribute("elencoUtilizzi");
				if(elencoUtilizzi != null && elencoUtilizzi.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoDestinazioneUso");
					for(int i = 0; i < elencoUtilizzi.length; i++) 
          {
						UtilizzoParticellaVO utilizzoParticellaVO = elencoUtilizzi[i];
						htmpl.newBlock("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.idUtilizzo", utilizzoParticellaVO.getIdUtilizzo().toString());
						String descrizione = utilizzoParticellaVO.getTipoUtilizzoVO().getDescrizione();
						if(Validator.isNotEmpty(utilizzoParticellaVO.getTipoUtilizzoVO().getAnnoFineValidita())) {
							descrizione += " [uso non attivo]";
						}
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.descUtilizzoParticella", descrizione);
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
						if(utilizzoParticellaVO.getTipoUtilizzoVO().getFlagSau().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
							htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzataSAU", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
							totSupUtilizzateSAU = totSupUtilizzateSAU.add(new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata()));
						}
            else
            {
              htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzataSAU", SolmrConstants.DEFAULT_SUPERFICIE);
            }
						totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata()));
					}
					if((totSenzaUsoSuolo != null)
            && (totSenzaUsoSuolo.doubleValue() > 0)) 
          {
  					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.idUtilizzo", "0");
  					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.descUtilizzoParticella", "SENZA USO DEL SUOLO SPECIFICATO");
  					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzata", 
              StringUtils.parseSuperficieField(totSenzaUsoSuolo.toString()));
            
            totSupUtilizzate = totSupUtilizzate.add(totSenzaUsoSuolo);  						
					}					
					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.totSupUtilizzataSAU", StringUtils.parseSuperficieField(totSupUtilizzateSAU.toString()));
				}
				else 
        {
					htmpl.newBlock("blkErrore");
			  	htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else 
      {
				UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = (UtilizzoDichiaratoVO[])request.getAttribute("elencoUtilizziDichiarati");
				BigDecimal totSenzaUsoSuoloDichiarato = (BigDecimal)request.getAttribute("totSenzaUsoSuoloDichiarato");
        if(elencoUtilizziDichiarati != null && elencoUtilizziDichiarati.length > 0) 
        {
					htmpl.newBlock("blkRisultatoRiepilogoDestinazioneUso");
					for(int i = 0; i < elencoUtilizziDichiarati.length; i++) 
          {
						UtilizzoDichiaratoVO utilizzoDichiaratoVO = elencoUtilizziDichiarati[i];
						htmpl.newBlock("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.idUtilizzo", utilizzoDichiaratoVO.getIdUtilizzo().toString());
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.descUtilizzoParticella", utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione());
						htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
						if(utilizzoDichiaratoVO.getTipoUtilizzoVO().getFlagSau().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
							htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzataSAU", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
							totSupUtilizzateSAU = totSupUtilizzateSAU.add(new BigDecimal(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
						}
            else
            {
              htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzataSAU", SolmrConstants.DEFAULT_SUPERFICIE);
            }
						totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
					}
          
          
          if((totSenzaUsoSuoloDichiarato != null)
            && (totSenzaUsoSuoloDichiarato.doubleValue() > 0)) 
          {
            htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.idUtilizzo", "-1");
            htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.descUtilizzoParticella", "SENZA USO DEL SUOLO SPECIFICATO");
            htmpl.set("blkRisultatoRiepilogoDestinazioneUso.blkElencoRisultato.supUtilizzata", 
              StringUtils.parseSuperficieField(totSenzaUsoSuoloDichiarato.toString()));
            
            totSupUtilizzate = totSupUtilizzate.add(totSenzaUsoSuoloDichiarato);              
          } 
          
          
					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
					htmpl.set("blkRisultatoRiepilogoDestinazioneUso.totSupUtilizzataSAU", StringUtils.parseSuperficieField(totSupUtilizzateSAU.toString()));
				}
				else {
					htmpl.newBlock("blkErrore");
			  		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
		// RIEPILOGO USO SECONDARIO
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_USO_SECONDARIO) == 0) 
    {
			BigDecimal totSupUtilizzateSecondarie = new BigDecimal(0);
			BigDecimal totSupUtilizzateSecondarieSAU = new BigDecimal(0);
			if(idPianoRiferimento.intValue() == -1) {
				UtilizzoParticellaVO[] elencoUtilizziSecondari = (UtilizzoParticellaVO[])request.getAttribute("elencoUtilizziSecondari");
				if(elencoUtilizziSecondari != null && elencoUtilizziSecondari.length > 0) {
					htmpl.newBlock("blkRisultatoRiepilogoUsoSecondario");
					for(int i = 0; i < elencoUtilizziSecondari.length; i++) {
						UtilizzoParticellaVO utilizzoParticellaVO = elencoUtilizziSecondari[i];
						htmpl.newBlock("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.idUtilizzoSecondario", utilizzoParticellaVO.getIdUtilizzoSecondario().toString());
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.descUsoSecondario", utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getDescrizione());
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.supSecondariaUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
						if(utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getFlagSau().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
							htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.supSecondariaUtilizzataSAU", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
							totSupUtilizzateSecondarieSAU = totSupUtilizzateSecondarieSAU.add(new BigDecimal(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
						}
						totSupUtilizzateSecondarie = totSupUtilizzateSecondarie.add(new BigDecimal(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
					}
					htmpl.set("blkRisultatoRiepilogoUsoSecondario.totSupUtilizzataSecondaria", StringUtils.parseSuperficieField(totSupUtilizzateSecondarie.toString()));
					htmpl.set("blkRisultatoRiepilogoUsoSecondario.totSupUtilizzataSecondariaSAU", StringUtils.parseSuperficieField(totSupUtilizzateSecondarieSAU.toString()));
				}
				else {
					htmpl.newBlock("blkErrore");
			  		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else {
				UtilizzoDichiaratoVO[] elencoUtilizziSecondariDichiarati = (UtilizzoDichiaratoVO[])request.getAttribute("elencoUtilizziSecondariDichiarati");
				if(elencoUtilizziSecondariDichiarati != null && elencoUtilizziSecondariDichiarati.length > 0) {
					htmpl.newBlock("blkRisultatoRiepilogoUsoSecondario");
					for(int i = 0; i < elencoUtilizziSecondariDichiarati.length; i++) {
						UtilizzoDichiaratoVO utilizzoDichiaratoVO = elencoUtilizziSecondariDichiarati[i];
						htmpl.newBlock("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.idUtilizzoSecondario", utilizzoDichiaratoVO.getIdUtilizzoSecondario().toString());
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.descUsoSecondario", utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getDescrizione());
						htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.supSecondariaUtilizzata", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
						if(utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getFlagSau().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
							htmpl.set("blkRisultatoRiepilogoUsoSecondario.blkElencoRisultato.supSecondariaUtilizzataSAU", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
							totSupUtilizzateSecondarieSAU = totSupUtilizzateSecondarieSAU.add(new BigDecimal(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
						}
						totSupUtilizzateSecondarie = totSupUtilizzateSecondarie.add(new BigDecimal(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
					}
					htmpl.set("blkRisultatoRiepilogoUsoSecondario.totSupUtilizzataSecondaria", StringUtils.parseSuperficieField(totSupUtilizzateSecondarie.toString()));
					htmpl.set("blkRisultatoRiepilogoUsoSecondario.totSupUtilizzataSecondariaSAU", StringUtils.parseSuperficieField(totSupUtilizzateSecondarieSAU.toString()));
				}
				else {
					htmpl.newBlock("blkErrore");
			  		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
		// RIEPILOGO MACRO USO
		else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_MACRO_USO) == 0) {
			BigDecimal totSupUtilizzate = new BigDecimal(0);
			if(idPianoRiferimento.intValue() == -1) {
				UtilizzoParticellaVO[] elencoUtilizzi = (UtilizzoParticellaVO[])request.getAttribute("elencoUtilizzi");
				if(elencoUtilizzi != null && elencoUtilizzi.length > 0) {
					htmpl.newBlock("blkRisultatoRiepilogoMacroUso");
					for(int i = 0; i < elencoUtilizzi.length; i++) {
						UtilizzoParticellaVO utilizzoParticellaVO = elencoUtilizzi[i];
						htmpl.newBlock("blkRisultatoRiepilogoMacroUso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.idMacroUso", utilizzoParticellaVO.getTipoMacroUsoVO().getIdMacroUso().toString());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.codiceMacroUso", utilizzoParticellaVO.getTipoMacroUsoVO().getCodice());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.descrizioneMacroUso", utilizzoParticellaVO.getTipoMacroUsoVO().getDescrizione());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
						totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata()));
					}
					htmpl.set("blkRisultatoRiepilogoMacroUso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
				}
				else {
					htmpl.newBlock("blkErrore");
			  		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
			else {
				UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = (UtilizzoDichiaratoVO[])request.getAttribute("elencoUtilizziDichiarati");
				if(elencoUtilizziDichiarati != null && elencoUtilizziDichiarati.length > 0) {
					htmpl.newBlock("blkRisultatoRiepilogoMacroUso");
					for(int i = 0; i < elencoUtilizziDichiarati.length; i++) {
						UtilizzoDichiaratoVO utilizzoDichiaratoVO = elencoUtilizziDichiarati[i];
						htmpl.newBlock("blkRisultatoRiepilogoMacroUso.blkElencoRisultato");
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.idMacroUso", utilizzoDichiaratoVO.getTipoMacroUsoVO().getIdMacroUso().toString());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.codiceMacroUso", utilizzoDichiaratoVO.getTipoMacroUsoVO().getCodice());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.descrizioneMacroUso", utilizzoDichiaratoVO.getTipoMacroUsoVO().getDescrizione());
						htmpl.set("blkRisultatoRiepilogoMacroUso.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
						totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
					}
					htmpl.set("blkRisultatoRiepilogoMacroUso.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
				}
				else {
					htmpl.newBlock("blkErrore");
			  		htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
				}
			}
		}
    // RIEPILOGO ZONA ALTIMETRICA
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_ZONA_ALTIMETRICA) == 0) 
    {
      BigDecimal totSupUtilizzate = new BigDecimal(0);
      if(idPianoRiferimento.intValue() == -1) {
        StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
        if(elencoStorico != null && elencoStorico.length > 0) {
          htmpl.newBlock("blkRisultatoRiepilogoZonaAltimetrica");
          for(int i = 0; i < elencoStorico.length; i++) {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            htmpl.newBlock("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato");
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.idZonaAltimetrica", storicoParticellaVO.getIdZonaAltimetrica().toString());
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.descZonaAltimetrica", storicoParticellaVO.getZonaAltimetrica().getDescription());
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]) && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata())) {
              totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            }
          }
          htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
        }
        else {
          htmpl.newBlock("blkErrore");
            htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
        }
      }
      else {
        StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
        if(elencoStorico != null && elencoStorico.length > 0) {
          htmpl.newBlock("blkRisultatoRiepilogoZonaAltimetrica");
          for(int i = 0; i < elencoStorico.length; i++) {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            htmpl.newBlock("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato");
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.idZonaAltimetrica", storicoParticellaVO.getIdZonaAltimetrica().toString());
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.descZonaAltimetrica", storicoParticellaVO.getZonaAltimetrica().getDescription());
            htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
          }
          htmpl.set("blkRisultatoRiepilogoZonaAltimetrica.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
        }
        else {
          htmpl.newBlock("blkErrore");
            htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
        }
      }
    }
    // RIEPILOGO CASO PARTICOLARE
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_CASO_PARTICOLARE) == 0) 
    {
      BigDecimal totSupUtilizzate = new BigDecimal(0);
      if(idPianoRiferimento.intValue() == -1) {
        StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
        if(elencoStorico != null && elencoStorico.length > 0) {
          htmpl.newBlock("blkRisultatoRiepilogoCasoParticolare");
          for(int i = 0; i < elencoStorico.length; i++) {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            htmpl.newBlock("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato");
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.descCasoParticolare", storicoParticellaVO.getCasoParticolare().getDescription());
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]) && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata())) {
              totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            }
          }
          htmpl.set("blkRisultatoRiepilogoCasoParticolare.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
        }
        else {
          htmpl.newBlock("blkErrore");
            htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
        }
      }
      else {
        StoricoParticellaVO[] elencoStorico = (StoricoParticellaVO[])request.getAttribute("elencoStorico");
        if(elencoStorico != null && elencoStorico.length > 0) {
          htmpl.newBlock("blkRisultatoRiepilogoCasoParticolare");
          for(int i = 0; i < elencoStorico.length; i++) {
            StoricoParticellaVO storicoParticellaVO = elencoStorico[i];
            htmpl.newBlock("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato");
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.descCasoParticolare", storicoParticellaVO.getCasoParticolare().getDescription());
            htmpl.set("blkRisultatoRiepilogoCasoParticolare.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
            totSupUtilizzate = totSupUtilizzate.add(new BigDecimal(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
          }
          htmpl.set("blkRisultatoRiepilogoCasoParticolare.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzate.toString()));
        }
        else {
          htmpl.newBlock("blkErrore");
            htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
        }
      }
    }
    // RIEPILOGO TIPO EFA
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_EFA) == 0) 
    {
      BigDecimal totValoreDopoConversione = new BigDecimal(0);
      BigDecimal totValoreDopoPonderazione = new BigDecimal(0);
      Vector<UtilizzoParticellaVO> elencoUtilizzi = (Vector<UtilizzoParticellaVO>)request.getAttribute("elencoUtilizzi");
      if(elencoUtilizzi != null) 
      {
        htmpl.newBlock("blkRisultatoRiepilogoTipoEfa");
        for(int i = 0; i < elencoUtilizzi.size(); i++) 
        {
          UtilizzoParticellaVO utilizzoParticellaVO = elencoUtilizzi.get(i);
          htmpl.newBlock("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato");
          if(!"S".equalsIgnoreCase(utilizzoParticellaVO.getPadreEfa()))
          {
            htmpl.newBlock("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio");
            if(utilizzoParticellaVO.getValoreOriginale().compareTo(new BigDecimal(0)) == 0)
            {
              htmpl.newBlock("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.blkZeroValoreFiglio");
            }
            else
            {
              htmpl.newBlock("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.blkValoreFiglio");
              htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.blkValoreFiglio.idTipoEfa", ""+utilizzoParticellaVO.getIdTipoEfa());
            }
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.descTipoEfa", utilizzoParticellaVO.getDescTipoEfaEfa());
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.descUnitaMisura", utilizzoParticellaVO.getDescUnitaMisuraEfa());
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.valoreOriginale", Formatter.formatDouble4(utilizzoParticellaVO.getValoreOriginale()));
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.valoreDopoConversione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoConversione()));
            totValoreDopoConversione = totValoreDopoConversione.add(utilizzoParticellaVO.getValoreDopoConversione());
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkFiglio.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoPonderazione()));
            totValoreDopoPonderazione = totValoreDopoPonderazione.add(utilizzoParticellaVO.getValoreDopoPonderazione());
          }
          else
          {
            htmpl.newBlock("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkPadre");
            htmpl.set("blkRisultatoRiepilogoTipoEfa.blkElencoRisultato.blkPadre.descTipoEfa", utilizzoParticellaVO.getDescTipoEfaEfa());
          }
          
          
        }
        htmpl.set("blkRisultatoRiepilogoTipoEfa.totValoreDopoConversione", Formatter.formatDouble4(totValoreDopoConversione));
        htmpl.set("blkRisultatoRiepilogoTipoEfa.totValoreDopoPonderazione", Formatter.formatDouble4(totValoreDopoPonderazione));
      }
      else 
      {
        htmpl.newBlock("blkErrore");
        htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
      }
        
      
    }
    // RIEPILOGO TIPO AREA
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_AREA) == 0) 
    {
      Vector<TipoAreaVO> elencoTipoArea = (Vector<TipoAreaVO>)request.getAttribute("elencoTipoArea");
      if(elencoTipoArea != null) 
      {
        htmpl.newBlock("blkRisultatoRiepilogoTipoArea");
        for(int i = 0; i < elencoTipoArea.size(); i++) 
        {
          TipoAreaVO tipoAreaVO = elencoTipoArea.get(i);
          htmpl.newBlock("blkRisultatoRiepilogoTipoArea.blkTipoArea");
          htmpl.set("blkRisultatoRiepilogoTipoArea.blkTipoArea.descTipoArea", tipoAreaVO.getDescrizione());
          BigDecimal totSupUtilizzo = new BigDecimal(0);
          for(int k=0;k<tipoAreaVO.getvTipoValoreArea().size();k++)
          {
            TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(k);            
            
            htmpl.newBlock("blkRisultatoRiepilogoTipoArea.blkTipoArea.blkElencoRisultato");
          
            String idTipoValoraArea = tipoValoreAreaVO.getIdTipoValoreArea()+"_"+tipoAreaVO.getFlagEsclusivoFoglio();
            htmpl.set("blkRisultatoRiepilogoTipoArea.blkTipoArea.blkElencoRisultato.idTipoValoreArea", idTipoValoraArea);
            htmpl.set("blkRisultatoRiepilogoTipoArea.blkTipoArea.blkElencoRisultato.descTipoValoreArea",tipoValoreAreaVO.getDescrizione());
            htmpl.set("blkRisultatoRiepilogoTipoArea.blkTipoArea.blkElencoRisultato.supUtilizzata", Formatter.formatDouble4(tipoValoreAreaVO.getSupUtilizzata()));
            totSupUtilizzo = totSupUtilizzo.add(tipoValoreAreaVO.getSupUtilizzata());          
          }         
        
          htmpl.set("blkRisultatoRiepilogoTipoArea.blkTipoArea.totSupUtilizzata", Formatter.formatDouble4(totSupUtilizzo));
        }
      }
      else 
      {
        htmpl.newBlock("blkErrore");
        htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
      }
        
      
    }        
    // RIEPILOGO TIPO GREENING
    else if(idTipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_GREENING) == 0) 
    {
      BigDecimal totSupUtilizzata = new BigDecimal(0);
     
      Vector<UtilizzoParticellaVO> elencoUtilizzi = (Vector<UtilizzoParticellaVO>)request.getAttribute("elencoUtilizzi");
      if(elencoUtilizzi != null) 
      {
        htmpl.newBlock("blkRisultatoRiepilogoTipoGreening");
        for(int i = 0; i < elencoUtilizzi.size(); i++) 
        {
          UtilizzoParticellaVO utilizzoParticellaVO = elencoUtilizzi.get(i);
          TipoUtilizzoVO tipoUtilizzoVO = utilizzoParticellaVO.getTipoUtilizzoVO();
          htmpl.newBlock("blkRisultatoRiepilogoTipoGreening.blkElencoRisultato");
          
          String descGruppoColturaGreen = tipoUtilizzoVO.getCodiceDiversificazione();
          descGruppoColturaGreen += " "+tipoUtilizzoVO.getDescrizioneFamiglia()+" - "+tipoUtilizzoVO.getDescrizioneGenere();
          if("S".equalsIgnoreCase(tipoUtilizzoVO.getTipoDiversificazione()))
          {
            descGruppoColturaGreen += " - "+tipoUtilizzoVO.getDescrizioneSpecie();
          }
          
          if(Validator.isNotEmpty(utilizzoParticellaVO.getTipoPeriodoSemina()))
          {
            descGruppoColturaGreen += " Epoca semina: "+utilizzoParticellaVO.getTipoPeriodoSemina().getDescrizione();
          }
                   
          htmpl.set("blkRisultatoRiepilogoTipoGreening.blkElencoRisultato.descGruppoColturaGreen", descGruppoColturaGreen);
          htmpl.set("blkRisultatoRiepilogoTipoGreening.blkElencoRisultato.supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
          totSupUtilizzata = totSupUtilizzata.add(new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata()));
          
        }
        htmpl.set("blkRisultatoRiepilogoTipoGreening.totSupUtilizzata", StringUtils.parseSuperficieField(totSupUtilizzata.toString()));
        
      }
      else 
      {
        htmpl.newBlock("blkErrore");
        htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
      }
        
      
    }
	}

  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
