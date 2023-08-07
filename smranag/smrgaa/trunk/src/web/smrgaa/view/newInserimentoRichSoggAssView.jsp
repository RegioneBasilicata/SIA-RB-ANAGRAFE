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
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoRichSoggAss.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regimeInserimentoRichSoggAss = request.getParameter("regimeInserimentoRichSoggAss");
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  if(Validator.isNotEmpty(regimeInserimentoRichSoggAss))  
    htmpl.set("idAziendaInd", request.getParameter("idAziendaInd"));
  else
    htmpl.set("idAziendaInd", request.getParameter("idAzienda"));
  
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
  Vector<AzAssAziendaNuovaVO> vAssAzienda = (Vector<AzAssAziendaNuovaVO>)request.getAttribute("vAssAzienda");   
   
  
  String[] cuaaEl = request.getParameterValues("cuaaEl");
  
  
  int numAziende = 0;
  if(Validator.isEmpty(regimeInserimentoRichSoggAss) 
    && Validator.isNotEmpty(vAssAzienda))
  {
    numAziende = vAssAzienda.size();
  }
  else
  {
    if(Validator.isNotEmpty(request.getParameterValues("cuaaEl")))
      numAziende = request.getParameterValues("cuaaEl").length;
  }
  
  
  if(numAziende > 0)
  {
  
    for(int i=0;i<numAziende;i++)
    {
      htmpl.newBlock("blkElencoAzAssociate");
      
      AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
      if(Validator.isEmpty(regimeInserimentoRichSoggAss) 
        && Validator.isNotEmpty(vAssAzienda))
      {      
        azAssAziendaNuovaVO = vAssAzienda.get(i);
      } 
      
      htmpl.set("blkElencoAzAssociate.idRiga", ""+i);
      
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("idAziendaEl") != null 
          && i <  request.getParameterValues("idAziendaEl").length 
          && Validator.isNotEmpty(request.getParameterValues("idAziendaEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.idAziendaEl", request.getParameterValues("idAziendaEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getIdAziendaAssociata()))
        {
          htmpl.set("blkElencoAzAssociate.idAziendaEl", ""+azAssAziendaNuovaVO.getIdAziendaAssociata());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("idAssociateAzNuove") != null 
          && i <  request.getParameterValues("idAssociateAzNuove").length 
          && Validator.isNotEmpty(request.getParameterValues("idAssociateAzNuove")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.idAssociateAzNuove", request.getParameterValues("idAssociateAzNuove")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getIdAssociateAzNuove()))
        {
          htmpl.set("blkElencoAzAssociate.idAssociateAzNuove", ""+azAssAziendaNuovaVO.getIdAssociateAzNuove());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("cuaaEl") != null 
          && i <  request.getParameterValues("cuaaEl").length 
          && Validator.isNotEmpty(request.getParameterValues("cuaaEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.cuaa", request.getParameterValues("cuaaEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getCuaa()))
        {
          htmpl.set("blkElencoAzAssociate.cuaa", azAssAziendaNuovaVO.getCuaa());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("partitaIvaEl") != null 
          && i <  request.getParameterValues("partitaIvaEl").length 
          && Validator.isNotEmpty(request.getParameterValues("partitaIvaEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.partitaIva", request.getParameterValues("partitaIvaEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getPartitaIva()))
        {
          htmpl.set("blkElencoAzAssociate.partitaIva", azAssAziendaNuovaVO.getPartitaIva());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("denominazioneEl") != null 
          && i <  request.getParameterValues("denominazioneEl").length 
          && Validator.isNotEmpty(request.getParameterValues("denominazioneEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.denominazione", request.getParameterValues("denominazioneEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getDenominazione()))
        {
          htmpl.set("blkElencoAzAssociate.denominazione", azAssAziendaNuovaVO.getDenominazione());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("istatComuneEl") != null 
          && i <  request.getParameterValues("istatComuneEl").length 
          && Validator.isNotEmpty(request.getParameterValues("istatComuneEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.istatComune", request.getParameterValues("istatComuneEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getIstatComune()))
        {
          htmpl.set("blkElencoAzAssociate.istatComune", azAssAziendaNuovaVO.getIstatComune());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("desComEl") != null 
          && i <  request.getParameterValues("desComEl").length 
          && Validator.isNotEmpty(request.getParameterValues("desComEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.desCom", request.getParameterValues("desComEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getDesCom()))
        {
          htmpl.set("blkElencoAzAssociate.desCom", azAssAziendaNuovaVO.getDesCom());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("siglaProvinciaEl") != null 
          && i <  request.getParameterValues("siglaProvinciaEl").length 
          && Validator.isNotEmpty(request.getParameterValues("siglaProvinciaEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.siglaProvincia", request.getParameterValues("siglaProvinciaEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getSglProv()))
        {
          htmpl.set("blkElencoAzAssociate.siglaProvincia", azAssAziendaNuovaVO.getSglProv());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("indirizzoEl") != null 
          && i <  request.getParameterValues("indirizzoEl").length 
          && Validator.isNotEmpty(request.getParameterValues("indirizzoEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.indirizzo", request.getParameterValues("indirizzoEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getIndirizzo()))
        {
          htmpl.set("blkElencoAzAssociate.indirizzo", azAssAziendaNuovaVO.getIndirizzo());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
      {
        if(request.getParameterValues("capEl") != null 
          && i <  request.getParameterValues("capEl").length 
          && Validator.isNotEmpty(request.getParameterValues("capEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.cap", request.getParameterValues("capEl")[i]);
        }
      }
      else
      {
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getCap()))
        {
          htmpl.set("blkElencoAzAssociate.cap", azAssAziendaNuovaVO.getCap());
        } 
      }
      
     
      
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
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
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getDataIngresso()))
        {
          htmpl.set("blkElencoAzAssociate.dataIngresso", DateUtils.formatDateNotNull(azAssAziendaNuovaVO.getDataIngresso()));
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoRichSoggAss)) 
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
        if((azAssAziendaNuovaVO != null)
          && Validator.isNotEmpty(azAssAziendaNuovaVO.getDataUscita()))
        {
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
