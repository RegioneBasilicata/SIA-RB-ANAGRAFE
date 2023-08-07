<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiNewSoggetti.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  String[] idRuolo = request.getParameterValues("idRuolo");
  String[] istatProvincia = request.getParameterValues("istatProvincia");
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
 
  
  try
  {
  
    SoggettoAziendaNuovaVO rappLegaleVO = gaaFacadeClient.getRappLegaleNuovaIscrizione(idAziendaNuova.longValue());
     
    Vector<CodeDescription> elencoRuoli = anagFacadeClient.getTipiRuoloNonTitolareAndNonSpecificato();
    request.setAttribute("elencoRuoli", elencoRuoli);
    
    Vector<ProvinciaVO> vProvince =  anagFacadeClient.getProvince();
    String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
    request.setAttribute("vProvince", vProvince);
    
    
    HashMap<String, Vector<ComuneVO>> hProvCom = new HashMap<String, Vector<ComuneVO>>();
    if(istatProvincia != null) 
    {
      for(int i = 0; i < istatProvincia.length; i++) 
      {         
        if(Validator.isNotEmpty(istatProvincia[i])) 
        {
          Vector<ComuneVO> vComune = anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvincia[i]);
          hProvCom.put(istatProvincia[i], vComune);   
        }
      }
    }
    
    
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
    
    
    
    //Calcolo numero soggetti
    int numSoggetti = 0;
    if(Validator.isNotEmpty(idRuolo))
    {
      numSoggetti = idRuolo.length;
    }
    
    if(Validator.isNotEmpty(tipoModifica)
      && "inserisci".equalsIgnoreCase(tipoModifica))
    {
      numSoggetti++;
    }
    
    
    int countSoggetti = 0;
    
    for(int i=0;i<numSoggetti;i++)
    {
      if(Validator.isNotEmpty(tipoModifica)
        && "elimina".equalsIgnoreCase(tipoModifica))
      {
        if(i == new Integer(numRiga).intValue())
        {           
          continue;
        }
      }
    
    
      htmpl.newBlock("blkElencoSoggetti");
      htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto");
      
      htmpl.set("blkElencoSoggetti.blkAltroSoggetto.idRiga", ""+countSoggetti);
      
      
      if(elencoRuoli != null && elencoRuoli.size() > 0) 
      {
        Iterator<CodeDescription> iteraRuoli = elencoRuoli.iterator();
        while(iteraRuoli.hasNext()) 
        {
          htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli");
          CodeDescription code = (CodeDescription)iteraRuoli.next();
      
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.idRuolo", code.getCode().toString());
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.descRuolo", code.getDescription());
          
          
          if(request.getParameterValues("idRuolo") != null && i < request.getParameterValues("idRuolo").length) 
          {
            if(Validator.isNotEmpty(request.getParameterValues("idRuolo")[i]) 
              && Integer.decode(request.getParameterValues("idRuolo")[i]).compareTo(code.getCode()) == 0) 
            {
              htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoRuoli.selected", "selected=\"selected\"", null);
            }
          }               
        }
      }
      
      
      if(request.getParameterValues("dataInizioRuolo") != null 
        && i <  request.getParameterValues("dataInizioRuolo").length 
        && Validator.isNotEmpty(request.getParameterValues("dataInizioRuolo")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.dataInizioRuolo", request.getParameterValues("dataInizioRuolo")[i]);
      }
      
      if(request.getParameterValues("codiceFiscale") != null 
        && i <  request.getParameterValues("codiceFiscale").length 
        && Validator.isNotEmpty(request.getParameterValues("codiceFiscale")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.codiceFiscale", request.getParameterValues("codiceFiscale")[i]);
      }
      
      
      
      
      if(request.getParameterValues("cognome") != null 
        && i <  request.getParameterValues("cognome").length 
        && Validator.isNotEmpty(request.getParameterValues("cognome")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cognome", request.getParameterValues("cognome")[i]);
      }
      
      if(request.getParameterValues("nome") != null 
        && i <  request.getParameterValues("nome").length 
        && Validator.isNotEmpty(request.getParameterValues("nome")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.nome", request.getParameterValues("nome")[i]);
      }     
      
      
      if(vProvince!=null)
      {
        for(int j=0;j<vProvince.size();j++)
        {
          ProvinciaVO provinciaVO = vProvince.get(j);
          htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince");
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.istatProvincia", provinciaVO.getIstatProvincia());
          htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.sglProv", provinciaVO.getSiglaProvincia());
          
          
          if(request.getParameterValues("istatProvincia") != null && i < request.getParameterValues("istatProvincia").length) 
          {
            if(Validator.isNotEmpty(request.getParameterValues("istatProvincia")[i]) 
              && request.getParameterValues("istatProvincia")[i].equalsIgnoreCase(provinciaVO.getIstatProvincia())) 
            {
              htmpl.set("blkElencoSoggetti.blkAltroSoggetto.blkElencoProvince.selected", "selected=\"selected\"", null);
            }
          }        
        }
      }
      
      if(hProvCom != null && hProvCom.size() > 0) 
      {
        if((request.getParameterValues("istatProvincia") != null)
          && i < request.getParameterValues("istatProvincia").length
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
      else 
      {
        htmpl.newBlock("blkElencoSoggetti.blkAltroSoggetto.blkNoComuni");
      } 
      
      
      if(request.getParameterValues("indirizzo") != null 
        && i <  request.getParameterValues("indirizzo").length 
        && Validator.isNotEmpty(request.getParameterValues("indirizzo")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.indirizzo", request.getParameterValues("indirizzo")[i]);
      }
      
      if(request.getParameterValues("cap") != null 
        && i <  request.getParameterValues("cap").length 
        && Validator.isNotEmpty(request.getParameterValues("cap")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.cap", request.getParameterValues("cap")[i]);
      }
      
      if(request.getParameterValues("telefono") != null 
        && i <  request.getParameterValues("telefono").length 
        && Validator.isNotEmpty(request.getParameterValues("telefono")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.telefono", request.getParameterValues("telefono")[i]);
      }
      
      if(request.getParameterValues("email") != null 
        && i <  request.getParameterValues("email").length 
        && Validator.isNotEmpty(request.getParameterValues("email")[i])) 
      {
        htmpl.set("blkElencoSoggetti.blkAltroSoggetto.email", request.getParameterValues("email")[i]);
      }        
      
      countSoggetti++;  
        
      
    }
    
  
  
	  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
