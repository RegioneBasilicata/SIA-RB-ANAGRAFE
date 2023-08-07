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
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoSoggetti.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String regimeInserimentoSoggetti = request.getParameter("regimeInserimentoSoggetti");
  
  
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
  
  Vector<CodeDescription> elencoRuoli = (Vector<CodeDescription>)request.getAttribute("elencoRuoli");
  Vector<ProvinciaVO> vProvince =  (Vector<ProvinciaVO>)request.getAttribute("vProvince");
  HashMap<String, Vector<ComuneVO>> hProvCom = (HashMap<String, Vector<ComuneVO>>)request.getAttribute("hProvCom"); 
  Vector<SoggettoAziendaNuovaVO> vSoggettiAziendaNuova = (Vector<SoggettoAziendaNuovaVO>)request.getAttribute("vSoggettiAziendaNuova");   
  SoggettoAziendaNuovaVO rappLegaleVO = (SoggettoAziendaNuovaVO)request.getAttribute("soggettoAziendaNuovaVO");
  
  
  
  if(rappLegaleVO != null)
  {
    htmpl.newBlock("blkElencoSoggetti");
    htmpl.newBlock("blkElencoSoggetti.blkRapLegale");
      
    htmpl.set("blkElencoSoggetti.blkRapLegale.dataInizioRuolo", DateUtils.formatDateNotNull(rappLegaleVO.getDataInizioRuolo()));
    htmpl.set("blkElencoSoggetti.blkRapLegale.codiceFiscale", rappLegaleVO.getCodiceFiscale());
    htmpl.set("blkElencoSoggetti.blkRapLegale.cognome", rappLegaleVO.getCognome());
    htmpl.set("blkElencoSoggetti.blkRapLegale.nome", rappLegaleVO.getNome());
    htmpl.set("blkElencoSoggetti.blkRapLegale.sglProv", rappLegaleVO.getSglProv());
    htmpl.set("blkElencoSoggetti.blkRapLegale.desCom", rappLegaleVO.getDesCom());
    htmpl.set("blkElencoSoggetti.blkRapLegale.indirizzo", rappLegaleVO.getIndirizzo());
    htmpl.set("blkElencoSoggetti.blkRapLegale.cap", rappLegaleVO.getCap());
    htmpl.set("blkElencoSoggetti.blkRapLegale.telefono", rappLegaleVO.getTelefono());
    htmpl.set("blkElencoSoggetti.blkRapLegale.email", rappLegaleVO.getEmail());
  }
  
  int numSoggetti = 0;
  if(Validator.isEmpty(regimeInserimentoSoggetti) 
    && Validator.isNotEmpty(vSoggettiAziendaNuova))
  {
    numSoggetti = vSoggettiAziendaNuova.size();
  }
  else
  {
    if(Validator.isNotEmpty(request.getParameterValues("idRuolo")))
      numSoggetti = request.getParameterValues("idRuolo").length;
  }
  
  
  if(numSoggetti > 0)
  {
  
    for(int i=0;i<numSoggetti;i++)
    {
      htmpl.newBlock("blkElencoSoggetti");
      htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto");
      
      SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = null;
      if(Validator.isEmpty(regimeInserimentoSoggetti) 
        && Validator.isNotEmpty(vSoggettiAziendaNuova))
      {      
        soggettoAziendaNuovaVO = vSoggettiAziendaNuova.get(i);
      } 
      
      htmpl.set("blkElencoSoggetti.blkAltroSoggetto.idRiga", ""+i);
      
      
      if(elencoRuoli != null && elencoRuoli.size() > 0) 
      {
        Iterator<CodeDescription> iteraRuoli = elencoRuoli.iterator();
        while(iteraRuoli.hasNext()) 
        {
          htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli");
          CodeDescription code = (CodeDescription)iteraRuoli.next();
      
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.idRuolo", code.getCode().toString());
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.descRuolo", code.getDescription());
          
          
          if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
	        {
	          if(request.getParameterValues("idRuolo") != null && i < request.getParameterValues("idRuolo").length) 
	          {
	            if(Validator.isNotEmpty(request.getParameterValues("idRuolo")[i]) 
	              && Integer.decode(request.getParameterValues("idRuolo")[i]).compareTo(code.getCode()) == 0) 
	            {
	              htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.selected", "selected=\"selected\"", null);
	            }
	          }
	        }
	        //prima volta che entro...
	        else 
	        {
	          if(soggettoAziendaNuovaVO != null) 
	          {
	            if(Validator.isNotEmpty(soggettoAziendaNuovaVO.getIdRuolo()) 
	              && soggettoAziendaNuovaVO.getIdRuolo().compareTo(code.getCode()) == 0) 
	            {
	              htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.selected", "selected=\"selected\"", null);
	            }
	          }
	        }         
        }
      }
      
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("dataInizioRuolo") != null 
          && i <  request.getParameterValues("dataInizioRuolo").length 
          && Validator.isNotEmpty(request.getParameterValues("dataInizioRuolo")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.dataInizioRuolo", request.getParameterValues("dataInizioRuolo")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getDataInizioRuolo())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.dataInizioRuolo", DateUtils.formatDateNotNull(soggettoAziendaNuovaVO.getDataInizioRuolo()));
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("codiceFiscale") != null 
          && i <  request.getParameterValues("codiceFiscale").length 
          && Validator.isNotEmpty(request.getParameterValues("codiceFiscale")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.codiceFiscale", request.getParameterValues("codiceFiscale")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getCodiceFiscale())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.codiceFiscale", soggettoAziendaNuovaVO.getCodiceFiscale());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("cognome") != null 
          && i <  request.getParameterValues("cognome").length 
          && Validator.isNotEmpty(request.getParameterValues("cognome")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cognome", request.getParameterValues("cognome")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getCognome())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cognome", soggettoAziendaNuovaVO.getCognome());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("nome") != null 
          && i <  request.getParameterValues("nome").length 
          && Validator.isNotEmpty(request.getParameterValues("nome")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.nome", request.getParameterValues("nome")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getNome())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.nome", soggettoAziendaNuovaVO.getNome());
        } 
      }     
      
	    
	    if(vProvince!=null)
      {
        for(int j=0;j<vProvince.size();j++)
        {
          ProvinciaVO provinciaVO = vProvince.get(j);
          htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince");
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.istatProvincia", provinciaVO.getIstatProvincia());
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.sglProv", provinciaVO.getSiglaProvincia());
          
          
          if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
          {
            if(request.getParameterValues("istatProvincia") != null && i < request.getParameterValues("istatProvincia").length) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("istatProvincia")[i]) 
                && request.getParameterValues("istatProvincia")[i].equalsIgnoreCase(provinciaVO.getIstatProvincia())) 
              {
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.selected", "selected=\"selected\"", null);
              }
            }
          }
          //prima volta che entro...
          else 
          {
            if(soggettoAziendaNuovaVO != null) 
            {
              if(Validator.isNotEmpty(soggettoAziendaNuovaVO.getIstatProvincia()) 
                && soggettoAziendaNuovaVO.getIstatProvincia().equalsIgnoreCase(provinciaVO.getIstatProvincia()))
              {
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.selected", "selected=\"selected\"", null);
              }
            }
          }         
          
        }
      }
      
      if(hProvCom != null && hProvCom.size() > 0) 
      {
        if(Validator.isNotEmpty(regimeInserimentoSoggetti))
        {
          if((request.getParameterValues("istatProvincia") != null)
            && (request.getParameterValues("istatProvincia")[i] != null))
          {
            Vector<ComuneVO> vComune = hProvCom.get(request.getParameterValues("istatProvincia")[i]);
            if(vComune != null)
            {
              for(int l = 0; l < vComune.size(); l++) 
              {
                ComuneVO comuneVO = vComune.get(l);
                htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni");
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.istatComune", comuneVO.getIstatComune());
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.desCom", comuneVO.getDescom());
              
                if(request.getParameterValues("istatComune") != null && i < request.getParameterValues("istatComune").length) 
                {
                  if(Validator.isNotEmpty(request.getParameterValues("istatComune")[i]) 
                    && request.getParameterValues("istatComune")[i].equalsIgnoreCase(comuneVO.getIstatComune())) 
                  {
                    htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.selected", "selected=\"selected\"", null);
                  }
                }               
              }
            }
            else 
            {
              htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
            } 
          }
          else 
          {
            htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
          }   
        }
        //prima volta che entro nella popup.
        else
        {
          
          if(Validator.isNotEmpty(soggettoAziendaNuovaVO)
            && Validator.isNotEmpty(soggettoAziendaNuovaVO.getIstatProvincia()))
          {
            Vector<ComuneVO> vComune = hProvCom.get(soggettoAziendaNuovaVO.getIstatProvincia());
            if(Validator.isNotEmpty(vComune))
            {
	            for(int l = 0; l < vComune.size(); l++) 
	            {
	              ComuneVO comuneVO = vComune.get(l);
                htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni");
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.istatComune", comuneVO.getIstatComune());
                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.desCom", comuneVO.getDescom());
	              
	              if(Validator.isNotEmpty(soggettoAziendaNuovaVO.getIstatComune())
	                && (soggettoAziendaNuovaVO.getIstatComune().equalsIgnoreCase(comuneVO.getIstatComune()))) 
	              {
	                htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoComuni.selected", "selected=\"selected\"", null);
	              }             
	            }
	          }
	          else 
	          {
	            htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
	          }   
          }
          else 
          {
            htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
          }             
        }       
      }
      else 
      {
        htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
      } 
      
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("indirizzo") != null 
          && i <  request.getParameterValues("indirizzo").length 
          && Validator.isNotEmpty(request.getParameterValues("indirizzo")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.indirizzo", request.getParameterValues("indirizzo")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getIndirizzo())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.indirizzo", soggettoAziendaNuovaVO.getIndirizzo());
        } 
      } 
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("cap") != null 
          && i <  request.getParameterValues("cap").length 
          && Validator.isNotEmpty(request.getParameterValues("cap")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cap", request.getParameterValues("cap")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getCap())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cap", soggettoAziendaNuovaVO.getCap());
        } 
      }
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("telefono") != null 
          && i <  request.getParameterValues("telefono").length 
          && Validator.isNotEmpty(request.getParameterValues("telefono")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.telefono", request.getParameterValues("telefono")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getTelefono())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.telefono", soggettoAziendaNuovaVO.getTelefono());
        } 
      }
      
      
      if(Validator.isNotEmpty(regimeInserimentoSoggetti)) 
      {
        if(request.getParameterValues("email") != null 
          && i <  request.getParameterValues("email").length 
          && Validator.isNotEmpty(request.getParameterValues("email")[i])) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.email", request.getParameterValues("email")[i]);
        }
      }
      else
      {
        if((soggettoAziendaNuovaVO != null)
          && Validator.isNotEmpty(soggettoAziendaNuovaVO.getEmail())) 
        {
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.email", soggettoAziendaNuovaVO.getEmail());
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
		            htmpl.set("blkElencoSoggetti.blkAltroSoggetto.err_"+property,
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
