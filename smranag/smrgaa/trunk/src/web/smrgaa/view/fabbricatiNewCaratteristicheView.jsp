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
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricatiNewCaratteristiche.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%


  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  if(fabbricatoVO !=null)
  {
    fabbricatoVO.setSuperficieFabbricato(StringUtils.parseSuperficieField(fabbricatoVO.getSuperficieFabbricato()));
    fabbricatoVO.setDimensioneFabbricato(StringUtils.parseSuperficieField(fabbricatoVO.getDimensioneFabbricato()));
    fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldTwoDecimal(fabbricatoVO.getAltezzaFabbricato()));
    fabbricatoVO.setLarghezzaFabbricato(StringUtils.parseDoubleFieldTwoDecimal(fabbricatoVO.getLarghezzaFabbricato()));
    fabbricatoVO.setLunghezzaFabbricato(StringUtils.parseDoubleFieldTwoDecimal(fabbricatoVO.getLunghezzaFabbricato()));
    fabbricatoVO.setVolumeUtilePresuntoFabbricato(StringUtils.parseDoubleField(fabbricatoVO.getVolumeUtilePresuntoFabbricato()));
    fabbricatoVO.setSuperficieCopertaFabbricato(StringUtils.parseSuperficieField(fabbricatoVO.getSuperficieCopertaFabbricato()));
    fabbricatoVO.setSuperficieScopertaFabbricato(StringUtils.parseSuperficieField(fabbricatoVO.getSuperficieScopertaFabbricato()));
    fabbricatoVO.setSuperficieScopertaExtraFabbricato(StringUtils.parseSuperficieField(fabbricatoVO.getSuperficieScopertaExtraFabbricato()));
    
    if (Validator.isNotEmpty(fabbricatoVO.getUnitaMisura()))
      htmpl.set("unitaMisuraPar", "("+fabbricatoVO.getUnitaMisura()+")");
    else htmpl.set("unitaMisuraPar", "");
  }

  Vector<CodeDescription> elencoTipologiaFabbricato = null;
  try 
  {
    elencoTipologiaFabbricato = anagFacadeClient.getTipiTipologiaFabbricato();
  }
  catch(SolmrException se) {}

  if(elencoTipologiaFabbricato != null) 
  {
    Iterator<CodeDescription> iteraTipologiaFabbricato = elencoTipologiaFabbricato.iterator();
    while(iteraTipologiaFabbricato.hasNext()) 
    {
      htmpl.newBlock("tipologiaFabbricato");
      CodeDescription code = iteraTipologiaFabbricato.next();
      htmpl.set("tipologiaFabbricato.idCodice",code.getCode().toString());
      if(fabbricatoVO != null) 
      {
        if(fabbricatoVO.getIdTipologiaFabbricato() != null) 
        {
          if(fabbricatoVO.getIdTipologiaFabbricato().compareTo(Long.decode(code.getCode().toString())) == 0) 
          {
            htmpl.set("tipologiaFabbricato.check", "selected");
          }
        }
      }
      htmpl.set("tipologiaFabbricato.descrizione",code.getDescription());
    }
  }

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
      Vector<String> vLabel = tipoTipologiaFabbricatoVO.getVLabel();
      if(vLabel.size() == 2)
      {
        htmpl.set("colspanPrima","3");
        htmpl.newBlock("tipologiaFabbricatoDimensioniA");
        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricatoLabel", vLabel.get(0));
        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricatoLabel", vLabel.get(1));
        htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricato",fabbricatoVO.getLarghezzaFabbricato());
        htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricato",fabbricatoVO.getLunghezzaFabbricato());
      }
      else if(vLabel.size() == 3)
      {
        htmpl.set("colspanPrima","5");
        htmpl.newBlock("tipologiaFabbricatoDimensioniB");
        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricatoLabel", vLabel.get(0));
        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricatoLabel", vLabel.get(1));
        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricatoLabel", vLabel.get(2));
        htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricato",fabbricatoVO.getLarghezzaFabbricato());
        htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricato",fabbricatoVO.getLunghezzaFabbricato());
        htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricato",fabbricatoVO.getAltezzaFabbricato());
      }
      else
      {
        throw new SolmrException("Il Vettore con le Label ha dimensione diversa da 2 o 3");
      }
    }
    
    String flagStoccaggio = tipoTipologiaFabbricatoVO.getFlagStoccaggio();
    if(Validator.isNotEmpty(flagStoccaggio) && flagStoccaggio.equalsIgnoreCase(SolmrConstants.STRUTTURA_STANDARD))
    {
      htmpl.set("dimcolspan","1");
      htmpl.set("dimcolspanAnno","2");
      htmpl.newBlock("blkFlagStoccaggio");
      htmpl.set("blkFlagStoccaggio.volumeUtilePresunto",fabbricatoVO.getVolumeUtilePresuntoFabbricato());
      htmpl.set("blkFlagStoccaggio.superficieScopertaExtraFabbricato",fabbricatoVO.getSuperficieScopertaExtraFabbricato());
    }    
  }
  
  htmpl.set("dimcolspan","2");
  htmpl.set("dimcolspanAnno","5");


  Vector<CodeDescription> elencoTipiFormaFabbricato = null;
  try
  {
    if (idTipologiaFabbricato!=null)
    {
      elencoTipiFormaFabbricato = anagFacadeClient.getTipiFormaFabbricato(idTipologiaFabbricato);
    }
  }
  catch(SolmrException se) {}

  if(elencoTipiFormaFabbricato != null && elencoTipiFormaFabbricato.size()!=0)
  {
    htmpl.newBlock("cmbForma");
    Iterator<CodeDescription> iteraTipiFormaFabbricato = elencoTipiFormaFabbricato.iterator();
    while(iteraTipiFormaFabbricato.hasNext()) 
    {
      htmpl.newBlock("cmbForma.formaFabbricato");
      CodeDescription code = (CodeDescription)iteraTipiFormaFabbricato.next();
      htmpl.set("cmbForma.formaFabbricato.idCodice",code.getCode().toString());
      if(fabbricatoVO != null) 
      {
        if(fabbricatoVO.getTipiFormaFabbricato() != null) 
        {
          if(fabbricatoVO.getTipiFormaFabbricato().equals(code.getCode().toString()))
          {
            htmpl.set("cmbForma.formaFabbricato.check", "selected");
          }
        }
      }
      htmpl.set("cmbForma.formaFabbricato.descrizione",code.getDescription());
    }
  }

  Vector<CodeDescription> elencoTipologiaColturaSerra = null;
  try
  {
    if (idTipologiaFabbricato!=null)
    {
      elencoTipologiaColturaSerra = anagFacadeClient.getTipiColturaSerra(idTipologiaFabbricato);
    }
  }
  catch(SolmrException se) {}

  if(elencoTipologiaColturaSerra != null && elencoTipologiaColturaSerra.size()!=0)
  {
    htmpl.newBlock("cmbSerra");
    htmpl.set("cmbSerra.mesiRiscSerra",fabbricatoVO.getMesiRiscSerra());
    htmpl.set("cmbSerra.oreRisc",fabbricatoVO.getOreRisc());
    Iterator<CodeDescription> iteraTipologiaColturaSerra = elencoTipologiaColturaSerra.iterator();
    while(iteraTipologiaColturaSerra.hasNext()) 
    {
      htmpl.newBlock("cmbSerra.serra");
      CodeDescription code = (CodeDescription)iteraTipologiaColturaSerra.next();
      htmpl.set("cmbSerra.serra.idCodice",code.getCode().toString());
      if(fabbricatoVO != null) 
      {
        if(fabbricatoVO.getTipologiaColturaSerra() != null) 
        {
          if(fabbricatoVO.getTipologiaColturaSerra().equals(code.getCode().toString()))
          {
            htmpl.set("cmbSerra.serra.check", "selected");
          }
        }
      }
      htmpl.set("cmbSerra.serra.descrizione",code.getDescription());
    }
  }


  // Setto la data di inizio validità del fabbricato alla data di sistema
  htmpl.set("strDataInizioValiditaFabbricato", DateUtils.getCurrentDateString());
  
  

  HtmplUtil.setValues(htmpl,fabbricatoVO);
  HtmplUtil.setErrors(htmpl,errors,request, application);
  HtmplUtil.setValues(htmpl,anagVO);


%>
<%= htmpl.text()%>
