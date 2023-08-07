<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.*" %>

<%
 java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricatiModUbicazione.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
 UteVO uteVO = (UteVO)request.getAttribute("uteVO");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

 HtmplUtil.setValues(htmpl, anagVO);

 if(uteVO != null) {
   if(Validator.isNotEmpty(uteVO.getIndirizzo())) {
     htmpl.set("indirizzoUte", "- "+uteVO.getIndirizzo());
   }
 }

 // Particelle associate
 Vector elencoParticelleAssociate = (Vector)session.getAttribute("elencoParticelleAssociate");
 if(elencoParticelleAssociate != null && elencoParticelleAssociate.size() > 0) {
   Iterator iteraParticelleAssociate = elencoParticelleAssociate.iterator();
   htmpl.newBlock("blkEtichettaParticelleAssociate");
   while(iteraParticelleAssociate.hasNext()) {
     htmpl.newBlock("blkElencoParticelleAssociate");
     ParticellaVO particellaAssociataVO = (ParticellaVO)iteraParticelleAssociate.next();
     htmpl.set("blkElencoParticelleAssociate.idStoricoParticellaElimina", particellaAssociataVO.getIdStoricoParticella().toString());
     htmpl.set("blkElencoParticelleAssociate.descComuneParticella", particellaAssociataVO.getDescComuneParticella());
     htmpl.set("blkElencoParticelleAssociate.siglaProv", particellaAssociataVO.getSiglaProvinciaParticella());
     if(particellaAssociataVO.getSezione() != null) {
       htmpl.set("blkElencoParticelleAssociate.sezioneParticella", particellaAssociataVO.getSezione());
     }
     htmpl.set("blkElencoParticelleAssociate.foglioParticella", particellaAssociataVO.getFoglio().toString());
     if(particellaAssociataVO.getParticella() != null) {
       htmpl.set("blkElencoParticelleAssociate.particella", particellaAssociataVO.getParticella().toString());
     }
     if(particellaAssociataVO.getSubalterno() != null) {
       htmpl.set("blkElencoParticelleAssociate.subParticella", particellaAssociataVO.getSubalterno());
     }
     htmpl.set("blkElencoParticelleAssociate.supCatastaleParticella", StringUtils.parseSuperficieField(particellaAssociataVO.getSupCatastale()));
     if(particellaAssociataVO.isChecked())
       htmpl.set("blkElencoParticelleAssociate.checked", "checked");
     if(particellaAssociataVO.getDataFineConduzione() != null) {
     	htmpl.set("blkElencoParticelleAssociate.disabled", "disabled");
     	htmpl.set("blkElencoParticelleAssociate.checked", "checked");
     }
   }
 }
 else {
   htmpl.newBlock("blkNoParticelleAssociate");
 }

 // Particelle associabili
 Vector elencoParticelleAssociabili = (Vector)session.getAttribute("elencoParticelleAssociabili");
 if(elencoParticelleAssociabili != null && elencoParticelleAssociabili.size() > 0) {
   Iterator iteraParticelleAssociabili = elencoParticelleAssociabili.iterator();
   htmpl.newBlock("blkEtichettaParticelleAssociabili");
   while(iteraParticelleAssociabili.hasNext()) {
     htmpl.newBlock("blkElencoParticelleAssociabili");
     ParticellaVO particellaAssociabileVO = (ParticellaVO)iteraParticelleAssociabili.next();
     htmpl.set("blkElencoParticelleAssociabili.idStoricoParticellaAssocia", particellaAssociabileVO.getIdStoricoParticella().toString());
     htmpl.set("blkElencoParticelleAssociabili.descComuneParticella", particellaAssociabileVO.getDescComuneParticella());
     htmpl.set("blkElencoParticelleAssociabili.siglaProv", particellaAssociabileVO.getSiglaProvinciaParticella());
     if(particellaAssociabileVO.getSezione() != null) {
       htmpl.set("blkElencoParticelleAssociabili.sezioneParticella", particellaAssociabileVO.getSezione());
     }
     htmpl.set("blkElencoParticelleAssociabili.foglioParticella", particellaAssociabileVO.getFoglio().toString());
     if(particellaAssociabileVO.getParticella() != null) {
       htmpl.set("blkElencoParticelleAssociabili.particella", particellaAssociabileVO.getParticella().toString());
     }
     if(particellaAssociabileVO.getSubalterno() != null) {
       htmpl.set("blkElencoParticelleAssociabili.subParticella", particellaAssociabileVO.getSubalterno());
     }
     htmpl.set("blkElencoParticelleAssociabili.supCatastaleParticella", StringUtils.parseSuperficieField(particellaAssociabileVO.getSupCatastale()));
     if(particellaAssociabileVO.isChecked()) {
       htmpl.set("blkElencoParticelleAssociabili.checked", "checked");
     }
   }
 }
 else {
   htmpl.newBlock("blkNoParticelleAssociabili");
 }

 HtmplUtil.setValues(htmpl,fabbricatoVO);
 HtmplUtil.setErrors(htmpl,errors,request, application);


%>
<%= htmpl.text()%>
