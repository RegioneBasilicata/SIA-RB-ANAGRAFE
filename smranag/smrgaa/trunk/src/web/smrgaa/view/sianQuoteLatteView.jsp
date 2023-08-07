<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

java.io.InputStream layout = application.getResourceAsStream("/layout/quoteLatte.htm");
Htmpl htmpl = new Htmpl(layout);

%>
   <%@include file = "/view/remoteInclude.inc" %>
<%

AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
Vector elencoQuoteLatteAziendaVO = (Vector)request.getAttribute("sianQuoteLatteAziendaVO");
RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

// Nuova gestione fogli di stile
htmpl.set("head", head, null);
htmpl.set("header", header, null);
htmpl.set("footer", footer, null);

String messaggioErrore = (String)request.getAttribute("messaggioErrore");
if(Validator.isNotEmpty(messaggioErrore)) {
  htmpl.newBlock("blkErrore");
  htmpl.set("blkErrore.messaggio", messaggioErrore);
}
else {
  htmpl.newBlock("blkQuoteLatte");
  if(elencoQuoteLatteAziendaVO != null) {
    for(int i = 0; i < elencoQuoteLatteAziendaVO.size(); i++) {
      htmpl.newBlock("blkQuoteLatte.blkElencoQuoteLatte");
      SianQuoteLatteAziendaVO sianQuoteLatteAziendaVO =(SianQuoteLatteAziendaVO)elencoQuoteLatteAziendaVO.elementAt(i);
      ComuneVO comuneVO = sianQuoteLatteAziendaVO.getComuneUteVO();
      if(Validator.isNotEmpty(comuneVO)) {
        htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.comune", comuneVO.getDescom()+" ("+comuneVO.getSiglaProv()+")");
      }
      if(Validator.isNotEmpty(sianQuoteLatteAziendaVO.getCampagnaLattiera())) {
        htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.campagnaLattieraCorrente", sianQuoteLatteAziendaVO.getCampagnaLattiera()+"/");
        int campagnaLattieraSuccessiva = Integer.parseInt(sianQuoteLatteAziendaVO.getCampagnaLattiera()) + 1;
        htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.campagnaLattieraSuccessiva", String.valueOf(campagnaLattieraSuccessiva));
      }
      htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.lattieraRif", sianQuoteLatteAziendaVO.getQuotaConsegneRif());
      if(Validator.isNotEmpty(sianQuoteLatteAziendaVO.getTenoreGrassoRif())) {
        htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.tenoreRif", StringUtils.parseEuroField(sianQuoteLatteAziendaVO.getTenoreGrassoRif()));
      }
      htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.venditeRif", sianQuoteLatteAziendaVO.getQuotaVenditeDiretteRif());
      htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.lattieraDis", sianQuoteLatteAziendaVO.getQuotaConsegneDis());
      if(Validator.isNotEmpty(sianQuoteLatteAziendaVO.getTenoreGrassoDis())) {
        htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.tenoreDis", StringUtils.parseEuroField(sianQuoteLatteAziendaVO.getTenoreGrassoDis()));
      }
      htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.venditeDis", sianQuoteLatteAziendaVO.getQuotaVenditeDiretteDis());
      htmpl.set("blkQuoteLatte.blkElencoQuoteLatte.cause", sianQuoteLatteAziendaVO.getCauseForzaMaggiore());
    }
  }
}


%>
<%= htmpl.text()%>
