<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="java.text.*"%>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoConferma.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  final String COLORE_GRIGIO = "color:grey;";
  
  Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova = (Vector<RichiestaAziendaDocumentoVO>)
    request.getAttribute("vAllegatiAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");  
  Vector<StatoRichiestaVO> vStatoRichiesta = (Vector<StatoRichiestaVO>)request.getAttribute("vStatoRichiesta");
  
  
  if(request.getAttribute("messaggio") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("messaggio"));
  }
  
  String testoNoAllegati = (String)request.getAttribute("testoNoAllegati");
  String testoPerTutti = (String)request.getAttribute("testoPerTutti");
  
  
  boolean isStampNoAllegati = false;
  if(vAllegatiAziendaNuova != null)
  {
    for(int i=0;i<vAllegatiAziendaNuova.size();i++)
    {
      if(vAllegatiAziendaNuova.get(i).getvAllegatoDocumento() == null)
      {
        isStampNoAllegati = true;
        break;
      }
    }
  }
  

  if(isStampNoAllegati)
  {
    htmpl.set("testoNoAllegati", testoNoAllegati, null);
  }
  
  htmpl.set("testoPerTutti", testoPerTutti, null);
  
  StatoRichiestaVO statoRichiestaVO = null;
  for(int i=0;i<vStatoRichiesta.size();i++)
  {
    if("S".equalsIgnoreCase(vStatoRichiesta.get(i).getFlagVisualizzaStep()))
    {
      htmpl.newBlock("blkElencoStep");
      if(aziendaNuovaVO.getIdStatoRichiesta().intValue() 
        == vStatoRichiesta.get(i).getIdStatoRichiestaPrecedente().intValue())
      {
	      htmpl.newBlock("blkElencoStep.blkBoldElencoStep");
	      htmpl.set("blkElencoStep.blkBoldElencoStep.descrizioneEstesa", vStatoRichiesta.get(i).getDescrizioneEstesa(), null);
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoStep.blkNormaleElencoStep");
        htmpl.set("blkElencoStep.blkNormaleElencoStep.descrizioneEstesa", vStatoRichiesta.get(i).getDescrizioneEstesa(), null);
	    }
    }
    
    if(vStatoRichiesta.get(i).getIdStatoRichiesta().intValue() == aziendaNuovaVO.getIdStatoRichiesta().intValue())
    {
      statoRichiestaVO = vStatoRichiesta.get(i);
    }
  }
  
  String intestazionePasso = "";
  String descrizionePasso = "";
  if(Validator.isNotEmpty(aziendaNuovaVO) 
    && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_BOZZA) ==0))
  {
  
    intestazionePasso = statoRichiestaVO.getIntestazionePasso();
    descrizionePasso = statoRichiestaVO.getDescrizionePasso();
    htmpl.newBlock("blkIndietro");
    htmpl.newBlock("blkStampa");  
  }
  
  
  /*if(Validator.isNotEmpty(aziendaNuovaVO) 
    && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_STAMPATA) ==0))
  {
    intestazionePasso = statoRichiestaVO.getIntestazionePasso();
    descrizionePasso = statoRichiestaVO.getDescrizionePasso();
    htmpl.newBlock("blkIndietro");    
    htmpl.newBlock("blkApponiDigitale");
  }*/
  
  if(Validator.isNotEmpty(aziendaNuovaVO) 
    && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_STAMPATA) ==0))
  {
    intestazionePasso = statoRichiestaVO.getIntestazionePasso();
    descrizionePasso = statoRichiestaVO.getDescrizionePasso();
    htmpl.newBlock("blkIndietro");
    htmpl.newBlock("blkButtonTrasmPA");
  }
  
  if(Validator.isNotEmpty(aziendaNuovaVO) 
    && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA) ==0))
  {
    intestazionePasso = statoRichiestaVO.getIntestazionePasso();
    descrizionePasso = statoRichiestaVO.getDescrizionePasso();
    htmpl.newBlock("blkButtonDopoTrasm");
  }
  
  
  htmpl.set("intestazionePasso", intestazionePasso);
  htmpl.set("descrizionePasso", descrizionePasso, null);
  
  
  
  
  
      
  

%>
<%= htmpl.text() %>
