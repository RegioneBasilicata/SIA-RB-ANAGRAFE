<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/particellareTerreniImporta.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
 Vector<ParticellaVO> elencoParticelle = (Vector<ParticellaVO>)session.getAttribute("elencoParticelle");
 Long idUte = (Long)request.getAttribute("idUte");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 
 it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
 
 String idTitoloPossesso = request.getParameter("idTitoloPossesso");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 
 htmpl.set("idTitoloPossesso", idTitoloPossesso);
  // Valorizzo il titolo di possesso e la LEGENDA TITOLO POSSESSO
  if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) 
  {
    for(int i = 0; i < elencoTipoTitoloPossesso.length; i++) 
    {
      CodeDescription codeDescription = (CodeDescription)elencoTipoTitoloPossesso[i];
      if (codeDescription!=null)
      {
      
        htmpl.set("blkLegendaConduzione.idTitoloPossesso", String.valueOf(codeDescription.getCode())+" - ");         
        if(i == (elencoTipoTitoloPossesso.length - 1))
          htmpl.set("blkLegendaConduzione.descTitoloPossesso", codeDescription.getDescription());
        else
          htmpl.set("blkLegendaConduzione.descTitoloPossesso", codeDescription.getDescription()+", ");
      
        if(idTitoloPossesso != null && idTitoloPossesso.equals(codeDescription.getCode().toString()))
          htmpl.set("descrizioneTitoloPossesso", codeDescription.getDescription());
      }
    }
  }
  
  
              
  
  

 // COMBO UTE
 Iterator<UteVO> iteraUte = elencoUte.iterator();
 while(iteraUte.hasNext()) {
   UteVO uteVO = (UteVO)iteraUte.next();
   htmpl.newBlock("blkElencoUte");
   htmpl.set("blkElencoUte.codice", uteVO.getIdUte().toString());
   htmpl.set("blkElencoUte.descrizione", uteVO.getComune()+" - "+uteVO.getIndirizzo());
   if(elencoUte.size() == 1 && (errors == null || errors.size() == 0)) {
     htmpl.set("blkElencoUte.selected", "selected=\"selected\"");
   }
   else if(Validator.isNotEmpty(idUte) && idUte.compareTo(uteVO.getIdUte()) == 0) {
     htmpl.set("blkElencoUte.selected", "selected=\"selected\"");
   }
 }
 // TABELLA PARTICELLE
 if(elencoParticelle != null && elencoParticelle.size() > 0) 
 {
   Iterator<ParticellaVO> iteraParticelle = elencoParticelle.iterator();
   htmpl.set("numeroRecord", String.valueOf(elencoParticelle.size()));
   while(iteraParticelle.hasNext()) {
     htmpl.newBlock("blkElencoParticelle");
     ParticellaVO particellaElencoVO = (ParticellaVO)iteraParticelle.next();
     htmpl.set("blkElencoParticelle.idConduzioneParticella", particellaElencoVO.getIdConduzioneParticella().toString());
     htmpl.set("blkElencoParticelle.descComuneParticella", particellaElencoVO.getDescComuneParticella());
     htmpl.set("blkElencoParticelle.siglaProvinciaParticella", particellaElencoVO.getSiglaProvinciaParticella());
     htmpl.set("blkElencoParticelle.sezione", particellaElencoVO.getSezione());
     htmpl.set("blkElencoParticelle.foglio", particellaElencoVO.getFoglio().toString());
     if(Validator.isNotEmpty(particellaElencoVO.getParticella())) {
       htmpl.set("blkElencoParticelle.particella", particellaElencoVO.getParticella().toString());
     }
     htmpl.set("blkElencoParticelle.subalterno", particellaElencoVO.getSubalterno());
     htmpl.set("blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(particellaElencoVO.getSupCatastale()));
     htmpl.set("blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(particellaElencoVO.getSuperficieGrafica()));
     htmpl.set("blkElencoParticelle.idTitoloPossesso", particellaElencoVO.getIdTitoloPossesso().toString());
     BigDecimal percentualePossessoTmp = particellaElencoVO.getPercentualePossesso();
     if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
     {
       percentualePossessoTmp = new BigDecimal(1);
     }
     htmpl.set("blkElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
   }
 }

 if(errors != null && errors.size() > 0) {
   HtmplUtil.setErrors(htmpl, errors, request, application);
 }

%>
<%= htmpl.text()%>
