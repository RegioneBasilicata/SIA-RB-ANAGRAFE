<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@page import="java.util.Date"%>
<%@ page import="java.util.*" %>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popPresenzaNotificheParticelle.htm");
 	Htmpl htmpl = new Htmpl(layout);

	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%


 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
  Vector<NotificaVO> vNotifiche = (Vector<NotificaVO>)request.getAttribute("vNotifiche");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
	
	
 	htmpl.set("descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 	htmpl.set("siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
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
  
  if(Validator.isNotEmpty(vNotifiche))
  {
    htmpl.newBlock("blkElencoNotifiche");
    for(int i=0;i<vNotifiche.size();i++)
    {
      htmpl.newBlock("blkElencoNotifiche.blkNotifica");
      NotificaVO notificaVO = vNotifiche.get(i);
      htmpl.set("blkElencoNotifiche.blkNotifica.descTipologiaNotifica", notificaVO.getDescTipologiaNotifica());
      htmpl.set("blkElencoNotifiche.blkNotifica.descCategoriaNotifica", notificaVO.getDescCategoriaNotifica());
     
      NotificaEntitaVO notificaEntitaVO =  notificaVO.getvNotificaEntita().get(0);
      htmpl.set("blkElencoNotifiche.blkNotifica.note", notificaEntitaVO.getNote());      
      htmpl.set("blkElencoNotifiche.blkNotifica.dataInserimento", DateUtils.formatDateNotNull(notificaEntitaVO.getDataInizioValidita()));
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkElencoNotifiche.blkNotifica.descUtenteApertura", null, notificaEntitaVO.getDenUtente(),
        notificaEntitaVO.getDenEnteUtente(), null); 
      htmpl.set("blkElencoNotifiche.blkNotifica.dataChiusura", DateUtils.formatDateNotNull(notificaEntitaVO.getDataFineValidita()));
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkElencoNotifiche.blkNotifica.descUtenteChiusura", null,notificaEntitaVO.getDenUtenteChiusura(),
        notificaEntitaVO.getDenEnteUtenteChiusura(), null);
      htmpl.set("blkElencoNotifiche.blkNotifica.noteChiusura", notificaEntitaVO.getNoteChiusuraEntita());      
    }
  
  }

 	


%>
<%= htmpl.text()%>
