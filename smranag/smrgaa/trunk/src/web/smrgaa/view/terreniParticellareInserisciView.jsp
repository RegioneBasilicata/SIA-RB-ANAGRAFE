<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareInserisci.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	Vector elencoUte = (Vector)request.getAttribute("elencoUte");
 	Vector<TipoEventoVO> elencoTipoEvento = (Vector<TipoEventoVO>)request.getAttribute("elencoTipoEvento");
 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
 	ConduzioneParticellaVO conduzioneParticellaVO = null;
 	if(storicoParticellaVO != null) {
 		conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
 	}
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String provvisoria = (String)request.getAttribute("provvisoria");
 	Long idEvento = (Long)request.getAttribute("idEvento");
	StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
	String indietro = request.getParameter("indietro");
	if(Validator.isNotEmpty(indietro)) {
		htmpl.set("indietro", indietro);
	}
	
 	if(elencoUte != null && elencoUte.size() > 0) {
 		for(int i = 0; i < elencoUte.size(); i++) {
 			UteVO uteVO = (UteVO)elencoUte.elementAt(i);
 			htmpl.newBlock("blkUte");
 			String descUte = uteVO.getComuneUte().getDescom();
 			if(Validator.isNotEmpty(uteVO.getIndirizzo())) {
 				descUte += " - "+uteVO.getIndirizzo();
 			}
 			htmpl.set("blkUte.idUte", uteVO.getIdUte().toString());
 			htmpl.set("blkUte.descUte", descUte);
 			if(conduzioneParticellaVO != null && conduzioneParticellaVO.getIdUte() != null && conduzioneParticellaVO.getIdUte().compareTo(uteVO.getIdUte()) == 0) {
 				htmpl.set("blkUte.selected", "selected=\"selected\"");
 			}
 		}
 	}
 	
 	if(storicoParticellaVO != null) {
 		// Se non si sono verificati errori gestisco le informazioni relative
 		// all'ubicazione della particella attraverso il VO
 		if(errors == null || errors.size() == 0) {
	 		if(storicoParticellaVO.getComuneParticellaVO() != null) {
	 			if(storicoParticellaVO.getComuneParticellaVO().getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
	 				if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null) {
	 					htmpl.set("siglaProvincia", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
	 				}
	 				htmpl.set("descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
	 				if(request.getAttribute("descStatoEstero") != null) {
	 	 				htmpl.set("descStatoEstero", (String)request.getAttribute("descStatoEstero"));
	 	 			}
	 			}
	 			else {
	 				htmpl.set("descStatoEstero", storicoParticellaVO.getComuneParticellaVO().getDescom());
	 			}
	 		}
 		}
 		// Altrimenti le gestisco attraverso la request
 		else {
 			if(request.getAttribute("siglaProvincia") != null) {
 				htmpl.set("siglaProvincia", (String)request.getAttribute("siglaProvincia"));
 			}
 			if(request.getAttribute("descComune") != null) {
 				htmpl.set("descComune", (String)request.getAttribute("descComune"));
 			}
 			if(request.getAttribute("descStatoEstero") != null) {
 				htmpl.set("descStatoEstero", (String)request.getAttribute("descStatoEstero"));
 			}
 			// Visualizzo i filtri di ricerca precedentemente inseriti
 			if(Validator.isNotEmpty(request.getAttribute("provinciaEvento"))) {
 				htmpl.set("provinciaEvento", (String)request.getAttribute("provinciaEvento"));
 			}
 			if(Validator.isNotEmpty(request.getAttribute("comuneEvento"))) {
 				htmpl.set("comuneEvento", (String)request.getAttribute("comuneEvento"));
 			}
 			if(Validator.isNotEmpty(request.getAttribute("sezioneEvento"))) {
 				htmpl.set("sezioneEvento", ((String)request.getAttribute("sezioneEvento")).toUpperCase());
 			}
 			if(Validator.isNotEmpty(request.getAttribute("foglioEvento"))) {
 				htmpl.set("foglioEvento", (String)request.getAttribute("foglioEvento"));
 			}
 			if(Validator.isNotEmpty(request.getAttribute("particellaEvento"))) {
 				htmpl.set("particellaEvento", (String)request.getAttribute("particellaEvento"));
 			}
 			
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
 			htmpl.set("sezione", storicoParticellaVO.getSezione().toUpperCase());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) {
 			htmpl.set("foglio", storicoParticellaVO.getFoglio());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
 			htmpl.set("particella", storicoParticellaVO.getParticella());
 		}
 		if(Validator.isNotEmpty(provvisoria)) {
 			htmpl.set("checkedProvvisoria", "checked=\"checked\"");
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
 			htmpl.set("subalterno", storicoParticellaVO.getSubalterno());
 		}
 	}

 	if(elencoTipoEvento != null && elencoTipoEvento.size() > 0) 
  {
 		for(int i = 0; i < elencoTipoEvento.size(); i++) 
    {
 			TipoEventoVO code = elencoTipoEvento.get(i);
 			htmpl.newBlock("blkTipiEvento");
 			if(i == 0) {
 				htmpl.set("blkTipiEvento.idEvento", "0");
 				htmpl.set("blkTipiEvento.descrizione", "nuova particella");
 			}
 			htmpl.set("blkTipiEvento.idEvento", ""+code.getIdEvento());
			htmpl.set("blkTipiEvento.descrizione", code.getDescrizione());
			if(idEvento != null && (idEvento.longValue() == code.getIdEvento())) 
      {
				htmpl.set("blkTipiEvento.selected", "selected=\"selected\"");
			}
 		}
 	}
 	
  //Controllo se devo fare vedere il radio cessaParticella  
  if(elencoTipoEvento != null) 
  {
    for(int i=0;i<elencoTipoEvento.size();i++)
    {
      TipoEventoVO code = elencoTipoEvento.get(i);        
      
      
      htmpl.newBlock("blkEventoCessare");
      htmpl.set("blkEventoCessare.codeEvento", ""+code.getIdEvento());
      if(code.isCessParticella())
      {
        htmpl.set("blkEventoCessare.flagCessare", "S");
      }
      else
      {
        htmpl.set("blkEventoCessare.flagCessare", "N");
      }
              
      htmpl.set("blkEventoCessare.index", ""+i);
    }
  }  
  
  
  
 	// Elenco particelle dalle quali è nata la nuova particella
 	if(elencoStoricoEvento != null && elencoStoricoEvento.length > 0) 
  {
 		htmpl.newBlock("blkParticelleEvento");
    
    
 		for(int i = 0; i < elencoStoricoEvento.length; i++) 
    {
 			StoricoParticellaVO storicoParticellaElencoVO = (StoricoParticellaVO)elencoStoricoEvento[i];
 			htmpl.newBlock("blkParticelleEvento.blkElencoParticelle");
      
      htmpl.set("blkParticelleEvento.blkElencoParticelle.idParticellaDaCessare", storicoParticellaElencoVO.getIdParticella().toString());
      if(storicoParticellaElencoVO.isCessaParticella()) 
      {
        htmpl.set("blkParticelleEvento.blkElencoParticelle.checkedParticellaDaCessare", "checked=\"checked\"",null); 
      }
      
      if(storicoParticellaElencoVO.equalsKey(storicoParticellaVO)) 
      {
        htmpl.set("blkParticelleEvento.blkElencoParticelle.disabled", "disabled=\"disabled\"",null); 
      }
        
      
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.idParticella", storicoParticellaElencoVO.getIdParticella().toString());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.descComuneParticellaEvento", storicoParticellaElencoVO.getComuneParticellaVO().getDescom());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.siglaProvParticellaEvento", storicoParticellaElencoVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.sezioneEvento", storicoParticellaElencoVO.getSezione());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.foglioEvento", storicoParticellaElencoVO.getFoglio());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.particellaEvento", storicoParticellaElencoVO.getParticella());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.subalternoEvento", storicoParticellaElencoVO.getSubalterno());
 			htmpl.set("blkParticelleEvento.blkElencoParticelle.supCatastaleEvento", StringUtils.parseSuperficieField(storicoParticellaElencoVO.getSupCatastale()));
      htmpl.set("blkParticelleEvento.blkElencoParticelle.superficieGraficaEvento", StringUtils.parseSuperficieField(storicoParticellaElencoVO.getSuperficieGrafica()));
 		}
 	}
 	
 	// Gestione degli errori
 	if(errors != null && errors.size() > 0) {
 		HtmplUtil.setErrors(htmpl, errors, request, application);
 	}

%>
<%= htmpl.text()%>
