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
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricatiModCaratteristiche.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");

  /*fabbricatoVO.setSuperficieFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieFabbricato()));
  fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getDimensioneFabbricato()));
  fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getAltezzaFabbricato()));
  fabbricatoVO.setLarghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
  fabbricatoVO.setLunghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
  fabbricatoVO.setVolumeUtilePresuntoFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getVolumeUtilePresuntoFabbricato()));
  fabbricatoVO.setSuperficieCopertaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieCopertaFabbricato()));
  fabbricatoVO.setSuperficieScopertaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaFabbricato()));
  fabbricatoVO.setSuperficieScopertaExtraFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaExtraFabbricato()));*/

  if (Validator.isNotEmpty(fabbricatoVO.getUnitaMisura()))
      htmpl.set("unitaMisuraPar", "("+fabbricatoVO.getUnitaMisura()+")");
    else htmpl.set("unitaMisuraPar", "");

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  htmpl.set("idTipologiaFabbricato",""+fabbricatoVO.getIdTipologiaFabbricato());
  htmpl.set("descTipologiaFabbricato",fabbricatoVO.getDescrizioneTipoFabbricato());




  Long idTipologiaFabbricato=null;

  if((fabbricatoVO != null) && (fabbricatoVO.getIdTipologiaFabbricato() != null))
  { 
    idTipologiaFabbricato=fabbricatoVO.getIdTipologiaFabbricato();
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = fabbricatoVO.getTipoTipologiaFabbricatoVO();
    
    if(!"N".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getTipoFormula()))
    {
      htmpl.newBlock("tastoCalcola");
    }
    
    if("potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
    {
      fabbricatoVO.setSuperficieScopertaFabbricato(null);
      htmpl.set("readOnlySupScoperta","readOnly");
    }
    
    
    if(tipoTipologiaFabbricatoVO.getVLabel() !=null)
    {
      //vettore coi nomi delle etichette da associare ai campi di input
      Vector vLabel = tipoTipologiaFabbricatoVO.getVLabel();
      if(vLabel.size() == 2)
      {
        htmpl.newBlock("tipologiaFabbricatoDimensioniA");
        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricatoLabel",(String)vLabel.get(0));
        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
      }
      else if(vLabel.size() == 3)
      {
        htmpl.newBlock("tipologiaFabbricatoDimensioniB");
        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricatoLabel",(String)vLabel.get(0));
        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricatoLabel",(String)vLabel.get(2));
        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getAltezzaFabbricato()));
      }
      else
      {
        throw new SolmrException("Il Vettore con le Label ha dimensione diversa da 2 o 3");
      }
    }
    
    String flagStoccaggio = tipoTipologiaFabbricatoVO.getFlagStoccaggio();
    if(Validator.isNotEmpty(flagStoccaggio) && flagStoccaggio.equalsIgnoreCase("S"))
    {
      htmpl.set("dimcolspan","1");
      htmpl.set("dimcolspanAnno","2");
      htmpl.newBlock("blkFlagStoccaggio");
      htmpl.set("blkFlagStoccaggio.volumeUtilePresunto", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getVolumeUtilePresuntoFabbricato()));
      htmpl.set("blkFlagStoccaggio.superficieScopertaExtraFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaExtraFabbricato()));
    }
    
  }
  
  htmpl.set("dimcolspan","2");
  htmpl.set("dimcolspanAnno","5");
    
  Vector elencoTipiFormaFabbricato = null;
  try
  {
    if (idTipologiaFabbricato!=null)
      elencoTipiFormaFabbricato = anagFacadeClient.getTipiFormaFabbricato(idTipologiaFabbricato);
  }
  catch(SolmrException se) {}  
    
  if(elencoTipiFormaFabbricato != null && elencoTipiFormaFabbricato.size()!=0)
  {
    htmpl.newBlock("cmbForma");
    Iterator iteraTipiFormaFabbricato = elencoTipiFormaFabbricato.iterator();
    while(iteraTipiFormaFabbricato.hasNext()) {
      htmpl.newBlock("cmbForma.formaFabbricato");
      CodeDescription code = (CodeDescription)iteraTipiFormaFabbricato.next();
      htmpl.set("cmbForma.formaFabbricato.idCodice",code.getCode().toString());
      if(fabbricatoVO != null) {
        if(fabbricatoVO.getTipiFormaFabbricato() != null) {
          if(fabbricatoVO.getTipiFormaFabbricato().equals(code.getCode().toString()))
            htmpl.set("cmbForma.formaFabbricato.check", "selected");
        }
      }
      htmpl.set("cmbForma.formaFabbricato.descrizione",code.getDescription());
    }
  }

  Vector elencoTipologiaColturaSerra = null;
  try
  {
    if (idTipologiaFabbricato!=null)
      elencoTipologiaColturaSerra = anagFacadeClient.getTipiColturaSerra(idTipologiaFabbricato);
  }
  catch(SolmrException se) {}

  if(elencoTipologiaColturaSerra != null && elencoTipologiaColturaSerra.size()!=0)
  {
    htmpl.newBlock("cmbSerra");
    htmpl.set("cmbSerra.mesiRiscSerra",fabbricatoVO.getMesiRiscSerra());
    htmpl.set("cmbSerra.oreRisc",fabbricatoVO.getOreRisc());
    Iterator iteraTipologiaColturaSerra = elencoTipologiaColturaSerra.iterator();
    while(iteraTipologiaColturaSerra.hasNext()) {
      htmpl.newBlock("cmbSerra.serra");
      CodeDescription code = (CodeDescription)iteraTipologiaColturaSerra.next();
      htmpl.set("cmbSerra.serra.idCodice",code.getCode().toString());
      if(fabbricatoVO != null) {
        if(fabbricatoVO.getTipologiaColturaSerra() != null) {
          if(fabbricatoVO.getTipologiaColturaSerra().equals(code.getCode().toString()))
            htmpl.set("cmbSerra.serra.check", "selected");
        }
      }
      htmpl.set("cmbSerra.serra.descrizione",code.getDescription());
    }
  }




  //HtmplUtil.setValues(htmpl,fabbricatoVO);
  
  htmpl.set("superficieFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieFabbricato()));
  htmpl.set("dimensioneFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getDimensioneFabbricato()));
  htmpl.set("superficieCopertaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieCopertaFabbricato()));
  htmpl.set("superficieScopertaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaFabbricato()));
  htmpl.set("denominazioneFabbricato", fabbricatoVO.getDenominazioneFabbricato());
  htmpl.set("annoCostruzioneFabbricato", fabbricatoVO.getAnnoCostruzioneFabbricato());
  htmpl.set("noteFabbricato", fabbricatoVO.getNoteFabbricato());
  
  
  
  HtmplUtil.setErrors(htmpl,errors,request, application);
  HtmplUtil.setValues(htmpl,anagVO);


%>
<%= htmpl.text()%>
