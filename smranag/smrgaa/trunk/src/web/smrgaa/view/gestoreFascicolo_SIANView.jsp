<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.IntermediarioAnagVO" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>

<%
  SolmrLogger.debug(this, " - gestoreFascicolo_SIANView.jsp - INIZIO PAGINA");
  java.io.InputStream layout = application.getResourceAsStream("/layout/gestoreFascicoloCAA_SIAN.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");


  try
  {
    AnagFacadeClient client = new AnagFacadeClient();

    // Nuova gestione fogli di stile
    htmpl.set("head", head, null);
    htmpl.set("header", header, null);
    htmpl.set("footer", footer, null);

    
    String messaggioErrore = (String)request.getAttribute("messaggioErrore");

    if(Validator.isNotEmpty(messaggioErrore)) {
      htmpl.newBlock("blkGestioneFascicoloMessaggio");
      htmpl.set("blkGestioneFascicoloMessaggio.messaggio",messaggioErrore);
    }
 
    try
    {      
      //Recupero i dati da SIAN
      SianTrovaFascicoloVO sianTrovaFascicoloVO = 
        (SianTrovaFascicoloVO)session.getAttribute("gestoreFascicoloSIAN");
      if (sianTrovaFascicoloVO !=null)
      {
        htmpl.newBlock("blkFascicoloSian");
        if(sianTrovaFascicoloVO.getOrganismoPagatore()!=null)
        {
          htmpl.set("blkFascicoloSian.organismoPagatore", client.getOrganismoPagatoreFormatted(sianTrovaFascicoloVO.getOrganismoPagatore()));
        }
        if(sianTrovaFascicoloVO.getDataAperturaFascicolo()!=null)
        {
          htmpl.set("blkFascicoloSian.dataAperturaFascicolo",StringUtils.
              parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataAperturaFascicolo()));
        }
        if(sianTrovaFascicoloVO.getDataChiusuraFascicolo()!=null)
        {
          htmpl.set("blkFascicoloSian.dataChiusuraFascicolo",StringUtils.
              parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataChiusuraFascicolo()));
        }
        htmpl.set("blkFascicoloSian.codiceTipoDetentore",sianTrovaFascicoloVO.getTipoDetentore().toString());
        if (sianTrovaFascicoloVO.getDetentore()!=null)
        {
          htmpl.set("blkFascicoloSian.detentore",StringUtils.parseCodiceAgea(sianTrovaFascicoloVO.getDetentore()));
          IntermediarioAnagVO iVO = client.findIntermediarioVOByCodiceEnte(sianTrovaFascicoloVO.getDetentore());
          if(iVO !=null)
          {
            htmpl.set("blkFascicoloSian.denomDetentore", "( "+iVO.getDenominazione() + " )");
          }
        }
        if(sianTrovaFascicoloVO.getDataValidazFascicolo()!=null)
        {
          htmpl.set("blkFascicoloSian.dataValidazFascicolo",StringUtils.
              parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataValidazFascicolo()));
        }
        if(sianTrovaFascicoloVO.getDataSottMandato()!=null)
        {
          htmpl.set("blkFascicoloSian.dataSottMandato",StringUtils.
              parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataSottMandato()));
        }
        if(sianTrovaFascicoloVO.getDataElaborazione()!=null)
        {
          htmpl.set("blkFascicoloSian.dataElaborazione",StringUtils.
              parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataElaborazione()));
        }
        
      }
      else
      {
        htmpl.set("blkGestioneFascicoloMessaggio.messaggio",AnagErrors.ERRORE_SERVIZIO_SIAN_NO_DATI);   
      }
    }
    catch(SolmrException se)
    {
      SolmrLogger.error(this,"\n\n Errore nella pagina gestoreFascicoloView.jsp nella chiamata al servizio trovaFascicolo: "+se.toString()+"\n\n");
      htmpl.newBlock("blkGestioneFascicoloMessaggio");
      htmpl.set("blkGestioneFascicoloMessaggio.messaggio", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
    
    }

  }
  catch(Exception e)
  {
    SolmrLogger.fatal(this,"\n\n Errore nella pagina gestoreFascicolo_SIANView.jsp: "+e.toString()+"\n\n");
    if (errors ==null) errors=new ValidationErrors();
    errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  }
  
  HtmplUtil.setErrors(htmpl,errors,request, application);
  out.print(htmpl.text());

  SolmrLogger.debug(this, " - gestoreFascicolo_SIANView.jsp - FINE PAGINA");
%>
