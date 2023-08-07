<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@page import="java.util.Date"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO" %>
<%@ page import="java.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popIstanzaRiesame.htm");
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
  Vector<IstanzaRiesameVO> vIstanzaRiesame = (Vector<IstanzaRiesameVO>)request.getAttribute("vIstanzaRiesame");
  Date dataInserimentoDichiarazione = (Date)request.getAttribute("dataInserimentoDichiarazione");
  
	
	
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
  
  if(Validator.isNotEmpty(vIstanzaRiesame))
  {
    htmpl.newBlock("blkElencoFasiIstanza");
    for(int i=0;i<vIstanzaRiesame.size();i++)
    {
      htmpl.newBlock("blkFaseIstanza");
      IstanzaRiesameVO istanzaRiesameVO = vIstanzaRiesame.get(i);
      htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.anno", ""+istanzaRiesameVO.getAnno());
      htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.protocolloIstanza", ""+istanzaRiesameVO.getProtocolloIstanza());
      htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.descFase", istanzaRiesameVO.getDescFaseIstanzaRiesame());
      if(Validator.isNotEmpty(dataInserimentoDichiarazione))
      {
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataRichiesta())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataRichiesta", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataRichiesta()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataAggiornamento())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione)
          && Validator.isNotEmpty(istanzaRiesameVO.getDataEvasione()))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataEvasione", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataAggiornamento()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataEvasione())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataFotointerpretazione", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataEvasione()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataInvioGis())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataInvioGis", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataInvioGis()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataSiticonvoca())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataSitiConvoca", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataSiticonvoca()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataSospensioneScaduta())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataSospensioneScaduta", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataSospensioneScaduta()));
        }
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataAnnullamento())
          && istanzaRiesameVO.getDataRichiesta().before(dataInserimentoDichiarazione))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataAnnullamento", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataAnnullamento()));
        }
      }
      else
      {
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataRichiesta", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataRichiesta()));
        if(Validator.isNotEmpty(istanzaRiesameVO.getDataAggiornamento())
          && Validator.isNotEmpty(istanzaRiesameVO.getDataEvasione()))
        {
          htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataEvasione", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataAggiornamento()));
        }
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataFotointerpretazione", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataEvasione()));
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataInvioGis", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataInvioGis()));
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataSitiConvoca", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataSiticonvoca()));
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataSospensioneScaduta", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataSospensioneScaduta()));
        htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.dataAnnullamento", DateUtils.formatDateNotNull(istanzaRiesameVO.getDataAnnullamento()));
      }
      
      htmpl.set("blkElencoFasiIstanza.blkFaseIstanza.note", istanzaRiesameVO.getNote());
    }
  
  }

 	


%>
<%= htmpl.text()%>
