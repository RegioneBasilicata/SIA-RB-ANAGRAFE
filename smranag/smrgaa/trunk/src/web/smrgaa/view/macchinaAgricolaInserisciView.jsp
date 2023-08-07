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
  java.io.InputStream layout = application.getResourceAsStream("/layout/macchinaAgricolaInserisci.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  Vector<UteVO> vUte = (Vector<UteVO>)request.getAttribute("vUte");  
  Vector<TipoMacchinaGaaVO> vTipoMacchina = (Vector<TipoMacchinaGaaVO>)request.getAttribute("vTipoMacchina");
  Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = (Vector<TipoGenereMacchinaGaaVO>)request.getAttribute("vGenereMacchina");
  Vector<TipoCategoriaGaaVO> vCategoria = (Vector<TipoCategoriaGaaVO>)request.getAttribute("vCategoria");
  Vector<CodeDescription> vTipoMarca = (Vector<CodeDescription>)request.getAttribute("vTipoMarca");
  Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = (Vector<TipoFormaPossessoGaaVO>)request.getAttribute("vTipoFormaPossesso");
  
  String regimeMacchinaAgricolaInserisci = request.getParameter("regimeMacchinaAgricolaInserisci");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
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
      if (Validator.isNotEmpty(request.getParameter("idUte")) && uteVO.getIdUte().toString().equals((String) request.getParameter("idUte")))
      {
        htmpl.set("blkComboUte.selected", "selected=\"selected\"", null);
      }
    }
  }
  //valorizzo la combo dell'UTE - END
  
  String idTipoMacchina = request.getParameter("idTipoMacchina");
  if(vTipoMacchina != null)
  {
    for(int i=0;i<vTipoMacchina.size();i++) 
    {
      TipoMacchinaGaaVO tipoMacchinaGaaVO = vTipoMacchina.get(i);
      htmpl.newBlock("blkTipoMacchina");
      htmpl.set("blkTipoMacchina.idTipoMacchina", ""+tipoMacchinaGaaVO.getIdTipoMacchina());
      htmpl.set("blkTipoMacchina.descrizione", tipoMacchinaGaaVO.getDescrizione());
      
      if(Validator.isNotEmpty(idTipoMacchina) 
        && new Long(idTipoMacchina).longValue() == tipoMacchinaGaaVO.getIdTipoMacchina()) 
      {
        htmpl.set("blkTipoMacchina.selected", "selected=\"selected\"",null);
      }
    }
  }
  
  String idGenereMacchina = request.getParameter("idGenereMacchina");
  if(vGenereMacchina != null)
  {
    for(int i=0;i<vGenereMacchina.size();i++) 
    {
      TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = vGenereMacchina.get(i);
      htmpl.newBlock("blkTipoGenereMacchina");
      htmpl.set("blkTipoGenereMacchina.idGenereMacchina", ""+tipoGenereMacchinaGaaVO.getIdGenereMacchina());
      htmpl.set("blkTipoGenereMacchina.descrizione", tipoGenereMacchinaGaaVO.getDescrizione());
      
      if(Validator.isNotEmpty(idGenereMacchina) 
        && new Long(idGenereMacchina).longValue() == tipoGenereMacchinaGaaVO.getIdGenereMacchina()) 
      {
        htmpl.set("blkTipoGenereMacchina.selected", "selected=\"selected\"",null);
      }
    }
  }
  else
  {
    htmpl.newBlock("blkNoTipoGenereMacchina");
  }
  
  
  String idCategoria = request.getParameter("idCategoria");
  if(vCategoria != null)
  {
    for(int i=0;i<vCategoria.size();i++) 
    {
      TipoCategoriaGaaVO tipoCategoriaGaaVO = vCategoria.get(i);
      htmpl.newBlock("blkTipoCategoria");
      htmpl.set("blkTipoCategoria.idCategoria", ""+tipoCategoriaGaaVO.getIdCategoria());
      htmpl.set("blkTipoCategoria.descrizione", tipoCategoriaGaaVO.getDescrizione());
      
      if(Validator.isNotEmpty(idCategoria) 
        && new Long(idCategoria).longValue() == tipoCategoriaGaaVO.getIdCategoria()) 
      {
        htmpl.set("blkTipoCategoria.selected", "selected=\"selected\"",null);
      }
    }
  }
  else
  {
    htmpl.newBlock("blkNoTipoCategoria");
  }
  
  String idMarca = request.getParameter("idMarca");
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
      }
    }
  }
  
  String idTipoFormaPossesso = request.getParameter("idTipoFormaPossesso");
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
  
  
  String percentualePossesso = request.getParameter("percentualePossesso");
  String abilitaPercentualePossesso = "N";
  if(Validator.isNotEmpty(tipoFormaPossessoGaaSelVO))
    abilitaPercentualePossesso = tipoFormaPossessoGaaSelVO.getAbilitaPercentualePossesso();
  
  if(Validator.isNotEmpty(regimeMacchinaAgricolaInserisci))
  {
    htmpl.set("percentualePossesso", percentualePossesso);
    if(!"S".equalsIgnoreCase(abilitaPercentualePossesso))
      htmpl.set("readOnly", "readOnly=\"true\"",null);
  }
  else
  {
    htmpl.set("percentualePossesso", "100");
    htmpl.set("readOnly", "readOnly=\"true\"",null);
  }

  htmpl.set("annoCostruzione", request.getParameter("annoCostruzione"));
  htmpl.set("modello", request.getParameter("modello"));
  htmpl.set("matricolaTelaio", request.getParameter("matricolaTelaio"));
  htmpl.set("dataCarico", request.getParameter("dataCarico"));
  htmpl.set("note", request.getParameter("note"));

  
     
      
  
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
%>

