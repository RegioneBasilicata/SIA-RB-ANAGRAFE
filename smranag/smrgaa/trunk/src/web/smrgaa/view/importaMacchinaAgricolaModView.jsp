<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/importaMacchinaAgricolaMod.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  Vector<UteVO> vUte = (Vector<UteVO>)request.getAttribute("vUte");  
  Vector<CodeDescription> vTipoMarca = (Vector<CodeDescription>)request.getAttribute("vTipoMarca");
  Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = (Vector<TipoFormaPossessoGaaVO>)request.getAttribute("vTipoFormaPossesso");
  Vector<CodeDescription> vTipoScarico = (Vector<CodeDescription>)request.getAttribute("vTipoScarico");
  PossessoMacchinaVO possessoMacchinaVO = (PossessoMacchinaVO)request.getAttribute("possessoMacchinaVO");
  String isPossesoMultiplo = (String)request.getAttribute("isPossesoMultiplo");
  String regimeImportaMacchinaAgricolaMod = request.getParameter("regimeImportaMacchinaAgricolaMod");
  String parametroModMaccLibera = (String)request.getAttribute("parametroModMaccLibera");
  boolean modificabile = false;
  String operazione = request.getParameter("operazione");
  String modificaUte = request.getParameter("modificaUte");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("idPossessoMacchina", ""+possessoMacchinaVO.getIdPossessoMacchina());
  
  if("S".equalsIgnoreCase(parametroModMaccLibera)
    && Validator.isEmpty(isPossesoMultiplo))
  {
    modificabile = true;  
  }
  
  
  
  
  //valorizzo la combo dell'UTE - START
  if (vUte.size() == 1)
  {
    UteVO uteVO = vUte.get(0);
    htmpl.newBlock("blkComboUte");
    htmpl.set("blkComboUte.idUte", uteVO.getIdUte().toString());
    if (Validator.isNotEmpty(uteVO.getIndirizzo()))
    {
      htmpl.set("blkComboUte.descUte", uteVO.getComune() + " (" + uteVO.getProvincia() + ") - " + uteVO.getIndirizzo());
    }
    else
    {
      htmpl.set("blkComboUte.descUte", uteVO.getComune() + " (" + uteVO.getProvincia() + ")");
    }
  }
  else
  {
    String idUte = "";
    if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
    { 
      idUte = request.getParameter("idUte");      
    }
    else
    {
      idUte = possessoMacchinaVO.getIdUte().toString();
    }
  
  
    htmpl.newBlock("blkComboUte");
    htmpl.set("blkComboUte.idUte", "");
    htmpl.set("blkComboUte.descUte", "");
    for (int i = 0; i < vUte.size(); i++)
    {
      UteVO uteVO = vUte.get(i);
      htmpl.newBlock("blkComboUte");
      htmpl.set("blkComboUte.idUte", uteVO.getIdUte().toString());
      if (Validator.isNotEmpty(uteVO.getIndirizzo()))
      {
        htmpl.set("blkComboUte.descUte", uteVO.getComune() + " (" + uteVO.getProvincia() + ") - " + uteVO.getIndirizzo());
      }
      else
      {
        htmpl.set("blkComboUte.descUte", uteVO.getComune() + " (" + uteVO.getProvincia() + ")");
      }
      
      
      if (Validator.isNotEmpty(idUte) && uteVO.getIdUte().toString().equals(idUte))
	    {
	      htmpl.set("blkComboUte.selected", "selected=\"selected\"", null);
	    }
    }
  }
  //valorizzo la combo dell'UTE - END
  
  htmpl.set("descTipoMacchina", possessoMacchinaVO.getMacchinaVO().getGenereMacchinaVO().getTipoMacchinaVO().getDescrizione());
  htmpl.set("descGenereMacchina", possessoMacchinaVO.getMacchinaVO().getGenereMacchinaVO().getDescrizione());
  htmpl.set("descTipoCategoria", possessoMacchinaVO.getMacchinaVO().getTipoCategoriaVO().getDescrizione());
  
  if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO().getAnnoCostruzione()))
  {
    if(!modificabile)
    {
      htmpl.set("annoCostruzione", ""+possessoMacchinaVO.getMacchinaVO().getAnnoCostruzione());
      htmpl.set("readOnlyAnnoCostr", "readOnly=\"true\"",null);
    }
    else
    {
      if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
      {
        htmpl.set("annoCostruzione", request.getParameter("annoCostruzione"));      
      }
      else
      {
        htmpl.set("annoCostruzione", ""+possessoMacchinaVO.getMacchinaVO().getAnnoCostruzione());
      }
    }
  }
  else
  {
    htmpl.set("annoCostruzione", request.getParameter("annoCostruzione"));
  }
  
  String idMarca = "";
  boolean disabledIdMarca = false;
  if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO().getIdMarca()))
  {
    if(!modificabile)
    {
      idMarca = possessoMacchinaVO.getMacchinaVO().getIdMarca().toString();
      htmpl.set("disabledTipoMarca", "disabled=\"disabled\"",null);
      disabledIdMarca = true;
    }
    else
    {
      if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
      {
        idMarca = request.getParameter("idMarca");      
      }
      else
      {
        idMarca = possessoMacchinaVO.getMacchinaVO().getIdMarca().toString();
      }
    }
  }
  else
  {
    idMarca = request.getParameter("idMarca");
  }
  
  
  if(vTipoMarca != null)
  {
    for(int i=0;i<vTipoMarca.size();i++) 
    {
      CodeDescription tipoMarca = vTipoMarca.get(i);
      htmpl.newBlock("blkTipoMarca");
      htmpl.set("blkTipoMarca.idMarca", ""+tipoMarca.getCode());
      htmpl.set("blkTipoMarca.descrizione", tipoMarca.getDescription());
      
      if(Validator.isNotEmpty(idMarca) 
        && new Integer(idMarca).intValue() == tipoMarca.getCode().intValue()) 
      {
        htmpl.set("blkTipoMarca.selected", "selected=\"selected\"",null);
        if(disabledIdMarca)
        {
          htmpl.newBlock("blkHiddenTipoMarca");
          htmpl.set("blkHiddenTipoMarca.idMarca", ""+tipoMarca.getCode());
        }
        
      }
    }
  }
  
  
  if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO().getModello()))
  {
    if(!modificabile)
    {
      htmpl.set("modello", possessoMacchinaVO.getMacchinaVO().getModello());
      htmpl.set("readOnlyModello", "readOnly=\"true\"",null);
    }
    else
    {
      if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
      {
        htmpl.set("modello", request.getParameter("modello"));      
      }
      else
      {
        htmpl.set("modello", ""+possessoMacchinaVO.getMacchinaVO().getModello());
      }
    }
  }
  else
  {
    htmpl.set("modello", request.getParameter("modello"));
  }
  
  if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO().getMatricolaTelaio()))
  {
    if(!modificabile)
    {
      htmpl.set("matricolaTelaio", possessoMacchinaVO.getMacchinaVO().getMatricolaTelaio());
      htmpl.set("readOnlyMatrTelaio", "readOnly=\"true\"",null);
    }
    else
    {
      if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
      {
        htmpl.set("matricolaTelaio", request.getParameter("matricolaTelaio"));      
      }
      else
      {
        htmpl.set("matricolaTelaio", ""+possessoMacchinaVO.getMacchinaVO().getMatricolaTelaio());
      }
    }
  }
  else
  {
    htmpl.set("matricolaTelaio", request.getParameter("matricolaTelaio"));
  }
  
  
  
  
  String idTipoFormaPossesso = "";
  if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
  {
    idTipoFormaPossesso = request.getParameter("idTipoFormaPossesso");
  }
  
  TipoFormaPossessoGaaVO tipoFormaPossessoGaaSelVO = null;
  if(vTipoFormaPossesso != null)
  {
    for(int i=0;i<vTipoFormaPossesso.size();i++) 
    {
      TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO = vTipoFormaPossesso.get(i);
      htmpl.newBlock("blkTipoFormaPossesso");
      htmpl.set("blkTipoFormaPossesso.idTipoFormaPossesso", ""+tipoFormaPossessoGaaVO.getIdTipoFormaPossesso());
      htmpl.set("blkTipoFormaPossesso.descrizione", tipoFormaPossessoGaaVO.getDescrizione());
      
      htmpl.newBlock("blkHiddenAbilitaPercPossesso");
      htmpl.set("blkHiddenAbilitaPercPossesso.abilitaPercentualePossesso", tipoFormaPossessoGaaVO.getAbilitaPercentualePossesso());
      
      if(Validator.isNotEmpty(idTipoFormaPossesso) 
        && new Long(idTipoFormaPossesso).longValue() == tipoFormaPossessoGaaVO.getIdTipoFormaPossesso()) 
      {
        htmpl.set("blkTipoFormaPossesso.selected", "selected=\"selected\"",null);
        tipoFormaPossessoGaaSelVO = tipoFormaPossessoGaaVO;
      }
    }
  }
  
  
  String abilitaPercentualePossesso = "N";
  if(Validator.isNotEmpty(tipoFormaPossessoGaaSelVO))
    abilitaPercentualePossesso = tipoFormaPossessoGaaSelVO.getAbilitaPercentualePossesso();
    
  if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
  {
    String percentualePossesso = request.getParameter("percentualePossesso");
    htmpl.set("percentualePossesso", percentualePossesso);
    
    if(!"S".equalsIgnoreCase(abilitaPercentualePossesso))
    {
      htmpl.set("readOnlyPerPoss", "readOnly=\"true\"",null);
    }
  }
  else
  {
    htmpl.set("percentualePossesso", "100");
	  htmpl.set("readOnlyPerPoss", "readOnly=\"true\"",null);
  }


  if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
  {
    String dataCarico = request.getParameter("dataCarico");
    htmpl.set("dataCarico", dataCarico);
  }
  
  
  
  
  
  
  
  if(Validator.isNotEmpty(regimeImportaMacchinaAgricolaMod))
    htmpl.set("note", request.getParameter("note"));
  else
    htmpl.set("note", possessoMacchinaVO.getMacchinaVO().getNote());

  
     
      
  
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
%>

