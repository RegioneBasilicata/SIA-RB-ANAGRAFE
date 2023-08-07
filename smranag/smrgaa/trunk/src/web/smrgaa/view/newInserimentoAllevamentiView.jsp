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
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoAllevamenti.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  if(request.getAttribute("messaggio") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("messaggio"));
  }
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori"); 
  
  HashMap<Long, Vector<TipoCategoriaAnimaleAnagVO>> hSpecCat = (HashMap<Long, Vector<TipoCategoriaAnimaleAnagVO>>)request.getAttribute("hSpecCat"); 
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = (Vector<UteAziendaNuovaVO>)request.getAttribute("vUteAziendaNuova");
  Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova = (Vector<AllevamentoAziendaNuovaVO>)session.getAttribute("vAllevamentiAziendaNuova");   
  Vector<TipoASLAnagVO> vAsl = (Vector<TipoASLAnagVO>)request.getAttribute("vAsl");
  Vector<TipoSpecieAnimaleAnagVO> vSpecie = (Vector<TipoSpecieAnimaleAnagVO>)request.getAttribute("vSpecie");
  
  if(vAllevamentiAziendaNuova != null)
  {
  
    for(int i=0;i<vAllevamentiAziendaNuova.size();i++)
    {
      htmpl.newBlock("blkElencoAllevamenti");
      AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO = vAllevamentiAziendaNuova.get(i);
      htmpl.set("blkElencoAllevamenti.idRiga", ""+i);
	    
	    htmpl.set("blkElencoAllevamenti.codiceAziendaZootecnica", allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica());
	    
	    if(vAsl!=null)
      {
        for(int j=0;j<vAsl.size();j++)
        {
          TipoASLAnagVO aslVO = vAsl.get(j);
          htmpl.newBlock("blkElencoAllevamenti.blkElencoAsl");
          htmpl.set("blkElencoAllevamenti.blkElencoAsl.idAsl", aslVO.getIdASL());
          htmpl.set("blkElencoAllevamenti.blkElencoAsl.descAsl", aslVO.getDescrizione());
          if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdAsl()) 
            && allevamentoAziendaNuovaVO.getIdAsl().toString().equals(aslVO.getIdASL()))
          {
            htmpl.set("blkElencoAllevamenti.blkElencoAsl.selected","selected", null);
          }
        }
      }
	    
	    String unitaMisura = "";
	    if(vSpecie!=null)
      {
        for(int j=0;j<vSpecie.size();j++)
        {
          TipoSpecieAnimaleAnagVO specieVO = vSpecie.get(j);
          htmpl.newBlock("blkElencoAllevamenti.blkElencoSpecie");
          htmpl.set("blkElencoAllevamenti.blkElencoSpecie.idSpecieAnimale", specieVO.getIdSpecieAnimale());
          htmpl.set("blkElencoAllevamenti.blkElencoSpecie.descSpecieAnimale", specieVO.getDescrizione());
          if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()) 
            && allevamentoAziendaNuovaVO.getIdSpecieAnimale().toString().equals(specieVO.getIdSpecieAnimale()))
          {
            htmpl.set("blkElencoAllevamenti.blkElencoSpecie.selected","selected", null);
            unitaMisura = specieVO.getUnitaDiMisura();
          }
        }
      }
      
      
      if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdSpecieAnimale()))
      {
        Vector<TipoCategoriaAnimaleAnagVO> vCategorie = hSpecCat.get(allevamentoAziendaNuovaVO.getIdSpecieAnimale());
        for(int j=0;j<vCategorie.size();j++)
        {
          TipoCategoriaAnimaleAnagVO categoriaVO = vCategorie.get(j);
          htmpl.newBlock("blkElencoAllevamenti.blkElencoCategorie");
          htmpl.set("blkElencoAllevamenti.blkElencoCategorie.idCategoriaAnimale", categoriaVO.getIdCategoriaAnimale());
          htmpl.set("blkElencoAllevamenti.blkElencoCategorie.descCategoria", categoriaVO.getDescrizioneCategoriaAnimale());
          if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdCategoriaAnimale()) 
            && allevamentoAziendaNuovaVO.getIdCategoriaAnimale().toString().equals(categoriaVO.getIdCategoriaAnimale()))
          {
            htmpl.set("blkElencoAllevamenti.blkElencoCategorie.selected","selected", null);
          }
          else if(vCategorie.size() == 1)
          {
            htmpl.set("blkElencoAllevamenti.blkElencoCategorie.selected","selected", null);
          }
        }
      
      }
      
      htmpl.set("blkElencoAllevamenti.numeroCapi", allevamentoAziendaNuovaVO.getStrNumeroCapi());
      htmpl.set("blkElencoAllevamenti.unitaMisura", unitaMisura);
      htmpl.set("blkElencoAllevamenti.note", allevamentoAziendaNuovaVO.getNote());      
      
      
      for(int j=0;j<vUteAziendaNuova.size();j++)
      {
        htmpl.newBlock("blkElencoAllevamenti.blkElencoUte");
        htmpl.set("blkElencoAllevamenti.blkElencoUte.idUteAziendaNuova", ""+vUteAziendaNuova.get(j).getIdUteAziendaNuova());
        String descUte = "";
        if(Validator.isNotEmpty(vUteAziendaNuova.get(j).getDenominazione()))
        {
          descUte += vUteAziendaNuova.get(j).getDenominazione()+" - ";
        }
        descUte += vUteAziendaNuova.get(j).getDesCom()+" ("+vUteAziendaNuova.get(j).getSglProvincia()+")";
        htmpl.set("blkElencoAllevamenti.blkElencoUte.descUte", descUte);
        if(Validator.isNotEmpty(allevamentoAziendaNuovaVO.getIdUteAziendaNuova()) 
          && (allevamentoAziendaNuovaVO.getIdUteAziendaNuova().compareTo(vUteAziendaNuova.get(j).getIdUteAziendaNuova()) ==0)
          || (vUteAziendaNuova.size() == 1))
        {
          htmpl.set("blkElencoAllevamenti.blkElencoUte.selected","selected", null);
        }   
      }
        
        
        
      if(elencoErrori != null && elencoErrori.size() > 0) 
		  {
		    ValidationErrors errors = (ValidationErrors)elencoErrori.elementAt(i);
		    if(errors != null && errors.size  () > 0) 
		    {
		      Iterator iter = htmpl.getVariableIterator();
		      while(iter.hasNext()) 
		      {
		        String key = (String)iter.next();
		        if(key.startsWith("err_")) 
		        {
		          String property = key.substring(4);
		          Iterator errorIterator = errors.get(property);
		          if(errorIterator != null) 
		          {
		            ValidationError error = (ValidationError)errorIterator.next();
		            htmpl.set("blkElencoAllevamenti.err_"+property,
		                  MessageFormat.format(htmlStringKO,
		                  new Object[] {
		                  pathErrori + "/"+ imko,
		                  "'"+jssp.process(error.getMessage())+"'",
		                  error.getMessage()}),
		                  null);
		          }
		        }
		      }
		    }
		  }
    }
  
  }
  
  
  
  
  
  
  
  
  
  
	
	


      
  

%>
<%= htmpl.text() %>
