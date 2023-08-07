  <%@page import="it.csi.smranag.smrgaa.dto.anagrafe.GreeningVO"%>
<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.DateUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
 	java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaIndicatori.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  CodeDescription codeAteco = (CodeDescription)request.getAttribute("codeAteco");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  
  Long idDichiarazioneLg = null;
  if(Validator.isNotEmpty(idDichiarazioneConsistenza))
  {
    idDichiarazioneLg = new Long(idDichiarazioneConsistenza);
  }
  
  
  /*if(anagAziendaVO.getTipoAttivitaOTE() != null && anagAziendaVO.getTipoAttivitaOTE().getCode() != null)
  {
    htmpl.set("strAttivitaOTE", anagAziendaVO.getTipoAttivitaOTE().getDescription()
     +" ("+anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode()+")");
  }*/
  
  //selezionata validazione
  if((idDichiarazioneLg != null) && (idDichiarazioneLg.compareTo(new Long(0)) > 0))
  {
	  if(codeAteco != null)
	  {
	    htmpl.set("strAttivitaATECO", codeAteco.getDescription()
	     +" ("+codeAteco.getSecondaryCode()+")");
	  }
  }
  else
  {
	  if(anagAziendaVO.getTipoAttivitaATECO() != null && anagAziendaVO.getTipoAttivitaATECO().getCode() != null)
	  {
	    htmpl.set("strAttivitaATECO", anagAziendaVO.getTipoAttivitaATECO().getDescription()
	     +" ("+anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode()+")");
	  }
	}
  
  @SuppressWarnings("unchecked")
  Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)
  	request.getAttribute("vAziendaAtecoSec");
  	  
  if((vAziendaAtecoSec != null) && (vAziendaAtecoSec.size() > 0))
  {
    String elencoAttivitaAtecoSec = "";
    for(int i=0;i<vAziendaAtecoSec.size();i++)
    {
      AziendaAtecoSecVO aziendaAtecoSec = vAziendaAtecoSec.get(i);
      elencoAttivitaAtecoSec += aziendaAtecoSec.getDescAttivitaAteco()+" ("
      +aziendaAtecoSec.getCodAttivitaAteco()+") <br/>";
    }
    htmpl.set("elencoAttivitaAtecoSec", elencoAttivitaAtecoSec, null);
  }
  
  //Dimensione Azienda
  /*if(anagAziendaVO.getDescDimensioneAzienda() != null)
  {
    htmpl.set("descDimensioneAzienda", anagAziendaVO.getDescDimensioneAzienda());
  }*/
  
  /*if(anagAziendaVO.getClasseUde() != null)
  {
    htmpl.set("classeUde", anagAziendaVO.getClasseUde().toString());
  }*/
  
  //RLS
  /*if(anagAziendaVO.getRls() != null)
  {
    htmpl.set("rls", Formatter.formatDouble2Separator(anagAziendaVO.getRls()));
  }*/
  
  //ULU
  /*if(anagAziendaVO.getUlu() != null)
  {
    htmpl.set("ulu", Formatter.formatDouble1(anagAziendaVO.getUlu()));
  }*/
  
  //PAGAMENTO ECOLOGICO - Inizio
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient, anagAziendaVO.getIdAzienda(), 
  	bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_NORMALE,
    ruoloUtenza);
  
  @SuppressWarnings("unchecked")
  Vector<GruppoGreeningVO> gruppiGreening = (Vector<GruppoGreeningVO>)request.getAttribute("gruppiGreening");
  
  if (gruppiGreening!=null) {
  	for (GruppoGreeningVO gruppo : gruppiGreening) {
  		htmpl.newBlock("blkGruppoGreening");

  		if (Validator.isNotEmpty(gruppo.getDescGruppoGreening())) {
  			htmpl.set("blkGruppoGreening.descGruppo", gruppo.getDescGruppoGreening().toUpperCase());
  		}
  		
  		for (GreeningVO greening : gruppo.getListaGreening()) {
  			htmpl.newBlock("blkGruppoGreening.blkGreening");
  			htmpl.set("blkGruppoGreening.blkGreening.tipo", greening.getDescTipoGreening());
  			
  			if (greening.getIdAziendaGreening()!=null) {
  				StringBuffer descrizione = new StringBuffer(greening.getDescEsitoGreening());
  				
  				if (Validator.isNotEmpty(greening.getValoreCalcolato())) {
  					descrizione.append(" ").append(greening.getValoreCalcolato());
  				}
  				
  				if (greening.getValoreCalcolatoNumber()!=null) {
  					descrizione.append(" ").append(
  						Formatter.formatDouble2Separator(greening.getValoreCalcolatoNumber()));
  				}
  				
  				if (greening.getValoreCalcolatoDate()!=null) {
  					descrizione.append(" ").append(
  						DateUtils.formatDateTimeNotNull(greening.getValoreCalcolatoDate()));
  				}
  				
  				for (int i=0; i<greening.getListaDescTipiEsonero().size(); i++) {
  					if (i==0) {
  						descrizione.append(" ");
  					}else {
  						descrizione.append(" - ");
  					}
  					
  					descrizione.append(greening.getListaDescTipiEsonero().get(i));
  				}
  				
  				htmpl.set("blkGruppoGreening.blkGreening.descrizione", descrizione.toString());
  			}
  		}
  	}
  }
	//PAGAMENTO ECOLOGICO - Fine
	
  HtmplUtil.setValues(htmpl, request);
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	
	if (errors!=null) {
  	HtmplUtil.setErrors(htmpl, errors, request, application);
	}
	
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if (Validator.isNotEmpty(messaggioErrore)) {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}

%>
<%= htmpl.text()%>
