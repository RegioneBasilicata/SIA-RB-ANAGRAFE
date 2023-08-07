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
 java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricatiNewUbicazione.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

 HtmplUtil.setValues(htmpl, anagVO);

 Vector elencoUteAttive = null;
 try {
   elencoUteAttive = anagFacadeClient.getElencoUteAttiveForAzienda(anagVO.getIdAzienda());
 }
 catch(SolmrException se) {
 }

 if(elencoUteAttive != null && elencoUteAttive.size() > 0) {
   Iterator iteraUteAttive = elencoUteAttive.iterator();
   while(iteraUteAttive.hasNext()) {
     htmpl.newBlock("blkUte");
     UteVO uteVO = (UteVO)iteraUteAttive.next();
     if(fabbricatoVO.getIdUnitaProduttivaFabbricato() != null) {
       if(fabbricatoVO.getIdUnitaProduttivaFabbricato().compareTo(uteVO.getIdUte()) == 0) {
         htmpl.set("blkUte.selected","selected");
       }
     }
     htmpl.set("blkUte.idUnitaProduttiva",uteVO.getIdUte().toString());
     if(Validator.isNotEmpty(uteVO.getIndirizzo())) {
       htmpl.set("blkUte.comuneUte",uteVO.getComune()+" - "+uteVO.getIndirizzo());
     }
     else {
       htmpl.set("blkUte.comuneUte",uteVO.getComune());
     }
   }
 }

 Vector elencoParticelleFabbricato = (Vector)session.getAttribute("elencoParticelleFabbricato");
 if(elencoParticelleFabbricato != null && elencoParticelleFabbricato.size() > 0) {
   Iterator iteraParticelleFabbricato = elencoParticelleFabbricato.iterator();
   htmpl.newBlock("bloccoParticelleFabbricato");
   while(iteraParticelleFabbricato.hasNext()) {
     htmpl.newBlock("bloccoParticelleFabbricato.elencoParticelleFabbricato");
     ParticellaVO particellaVO = (ParticellaVO)iteraParticelleFabbricato.next();
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.idStoricoParticella",particellaVO.getIdStoricoParticella().toString());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.descComuneParticella",particellaVO.getDescComuneParticella());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.siglaProv",particellaVO.getSiglaProvinciaParticella());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.sezione",particellaVO.getSezione());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.foglio",particellaVO.getFoglio().toString());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.particella",particellaVO.getParticella().toString());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.subalterno",particellaVO.getSubalterno());
     htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.superficieCatastale", StringUtils.parseSuperficieField(particellaVO.getSupCatastale()));
     if(particellaVO.isChecked()) {
       htmpl.set("bloccoParticelleFabbricato.elencoParticelleFabbricato.checked","checked");
     }
   }
 }
 String messaggio = (String)session.getAttribute("messaggio");
 session.removeAttribute("messaggio");
 if(messaggio != null && !messaggio.equals("")) {
   htmpl.newBlock("noParticelleFabbricato");
 }

 HtmplUtil.setValues(htmpl,fabbricatoVO);
 HtmplUtil.setErrors(htmpl,errors,request, application);


%>
<%= htmpl.text()%>
