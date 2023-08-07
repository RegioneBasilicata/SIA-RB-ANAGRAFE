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
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoRichSoggAssCaa.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regimeInserimentoRichSoggAssCaa = request.getParameter("regimeInserimentoRichSoggAssCaa");
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  htmpl.set("idAzienda", request.getParameter("idAzienda"));
  
  if(request.getAttribute("msgErrore") != null)
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", (String)request.getAttribute("msgErrore"));
  }
  
  String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  StringProcessor jssp = new JavaScriptStringProcessor();
  
  Vector<ValidationErrors> elencoErrori = (Vector<ValidationErrors>)request.getAttribute("elencoErrori"); 
  Vector<IntermediarioAnagVO> vIntermediari = (Vector<IntermediarioAnagVO>)request.getAttribute("vIntermediari");   
  HashMap<String, AzAssAziendaNuovaVO> hAssAzienda = (HashMap<String, AzAssAziendaNuovaVO>)request.getAttribute("hAssAzienda");
  
  if(vIntermediari != null)
  {
  
    for(int i=0;i<vIntermediari.size();i++)
    {
      htmpl.newBlock("blkElencoAzAssociate");
      IntermediarioAnagVO intermediarioAnagVO = vIntermediari.get(i);
      htmpl.set("blkElencoAzAssociate.idRiga", ""+i);
      
      
	    
	    htmpl.set("blkElencoAzAssociate.cuaa", intermediarioAnagVO.getExtCuaa());
	    htmpl.set("blkElencoAzAssociate.partitaIva", intermediarioAnagVO.getPartitaIva());
	    htmpl.set("blkElencoAzAssociate.denominazione", intermediarioAnagVO.getDenominazione());
	    htmpl.set("blkElencoAzAssociate.codiceEnte", intermediarioAnagVO.getCodiceFiscale());
	    htmpl.set("blkElencoAzAssociate.desCom", intermediarioAnagVO.getDesCom());
	    htmpl.set("blkElencoAzAssociate.siglaProvincia", intermediarioAnagVO.getSglProv());
	    htmpl.set("blkElencoAzAssociate.indirizzo", intermediarioAnagVO.getIndirizzo());
	    htmpl.set("blkElencoAzAssociate.cap", intermediarioAnagVO.getCap());
	    
	    
	    if(Validator.isNotEmpty(regimeInserimentoRichSoggAssCaa)) 
      {
        if(request.getParameterValues("dataIngresso") != null 
          && i <  request.getParameterValues("dataIngresso").length 
          && Validator.isNotEmpty(request.getParameterValues("dataIngresso")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.dataIngresso", request.getParameterValues("dataIngresso")[i]);
        }
      }
      else
      {
        if((hAssAzienda != null)
          && Validator.isNotEmpty(hAssAzienda.get(intermediarioAnagVO.getCodiceFiscale()))) 
        {
          AzAssAziendaNuovaVO azAssAziendaNuovaVO = hAssAzienda.get(intermediarioAnagVO.getCodiceFiscale());
          htmpl.set("blkElencoAzAssociate.dataIngresso", DateUtils.formatDateNotNull(azAssAziendaNuovaVO.getDataIngresso()));
        } 
      }
      
      
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAssCaa)) 
      {
        if(request.getParameterValues("dataUscita") != null 
          && i <  request.getParameterValues("dataUscita").length 
          && Validator.isNotEmpty(request.getParameterValues("dataUscita")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.dataUscita", request.getParameterValues("dataUscita")[i]);
        }
      }
      else
      {
        if((hAssAzienda != null)
          && Validator.isNotEmpty(hAssAzienda.get(intermediarioAnagVO.getCodiceFiscale()))) 
        {
          AzAssAziendaNuovaVO azAssAziendaNuovaVO = hAssAzienda.get(intermediarioAnagVO.getCodiceFiscale());
          htmpl.set("blkElencoAzAssociate.dataUscita", DateUtils.formatDateNotNull(azAssAziendaNuovaVO.getDataUscita()));
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
		            htmpl.set("blkElencoAzAssociate.err_"+property,
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
