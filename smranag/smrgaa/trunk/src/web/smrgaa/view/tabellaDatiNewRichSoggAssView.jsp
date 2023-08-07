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
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiNewRichSoggAss.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  String   idAzienda = request.getParameter("idAzienda");
  String[] idAziendaEl = request.getParameterValues("idAziendaEl");
  String[] idAssociateAzNuove = request.getParameterValues("idAssociateAzNuove");
  String[] cuaaEl = request.getParameterValues("cuaaEl");
  String[] partitaIvaEl = request.getParameterValues("partitaIvaEl");
  String[] denominazioneEl = request.getParameterValues("denominazioneEl");
  String[] istatComuneEl = request.getParameterValues("istatComuneEl");
  String[] desComEl = request.getParameterValues("desComEl");
  String[] siglaProvinciaEl = request.getParameterValues("siglaProvinciaEl");
  String[] indirizzoEl = request.getParameterValues("indirizzoEl");
  String[] capEl = request.getParameterValues("capEl");
  
  String[] dataIngresso = request.getParameterValues("dataIngresso");
  String[] dataUscita = request.getParameterValues("dataUscita");
  
 
  
 
  
  try
  {
    //Calcolo numero soggetti
    int numAssociati = 0;
    if(Validator.isNotEmpty(idAziendaEl))
    {
      numAssociati = idAziendaEl.length;
    }
    
    if(Validator.isNotEmpty(tipoModifica)
      && "inserisci".equalsIgnoreCase(tipoModifica))
    {
      numAssociati++;
    }
    
    
    int countAssociati = 0;
    
    for(int i=0;i<numAssociati;i++)
    {
      if(Validator.isNotEmpty(tipoModifica)
        && "elimina".equalsIgnoreCase(tipoModifica))
      {
        if(i == new Integer(numRiga).intValue())
        {
          continue;
        }
      }
    
    
      htmpl.newBlock("blkElencoAzAssociate");
      
      htmpl.set("blkElencoAzAssociate.idRiga", ""+countAssociati);
      
      
      
      
      //Ultimo record devo aggiungere quello da inserimento
      if(Validator.isNotEmpty(tipoModifica)
        && "inserisci".equalsIgnoreCase(tipoModifica)
        && i==(numAssociati-1))
      {
        if(Validator.isNotEmpty(idAzienda))
        {
          htmpl.set("blkElencoAzAssociate.idAziendaEl", idAzienda);
          AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
          
          htmpl.set("blkElencoAzAssociate.cuaa", anagAziendaVO.getCUAA());
          htmpl.set("blkElencoAzAssociate.partitaIva", anagAziendaVO.getPartitaIVA());
          htmpl.set("blkElencoAzAssociate.denominazione", anagAziendaVO.getDenominazione());
          htmpl.set("blkElencoAzAssociate.istatComune", anagAziendaVO.getSedelegComune());
          htmpl.set("blkElencoAzAssociate.desCom", anagAziendaVO.getDescComune());
          htmpl.set("blkElencoAzAssociate.siglaProvincia", anagAziendaVO.getSedelegProv());
          htmpl.set("blkElencoAzAssociate.indirizzo", anagAziendaVO.getSedelegIndirizzo());
          htmpl.set("blkElencoAzAssociate.cap", anagAziendaVO.getSedelegCAP());
        }
        else
        {         
	        
	        htmpl.set("blkElencoAzAssociate.cuaa", request.getParameter("cuaa"));
	        htmpl.set("blkElencoAzAssociate.partitaIva", request.getParameter("partitaIva"));
	        htmpl.set("blkElencoAzAssociate.denominazione", request.getParameter("denominazione"));
	        htmpl.set("blkElencoAzAssociate.istatComune", request.getParameter("istatComuneIns"));
	        htmpl.set("blkElencoAzAssociate.desCom", request.getParameter("comuneIns"));
	        htmpl.set("blkElencoAzAssociate.siglaProvincia", request.getParameter("provinciaIns"));
	        htmpl.set("blkElencoAzAssociate.indirizzo", request.getParameter("indirizzoIns"));
	        htmpl.set("blkElencoAzAssociate.cap", request.getParameter("capIns"));
	      }
        
             
      }
      else
      {
        if(request.getParameterValues("idAziendaEl") != null 
          && i <  request.getParameterValues("idAziendaEl").length 
          && Validator.isNotEmpty(request.getParameterValues("idAziendaEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.idAziendaEl", request.getParameterValues("idAziendaEl")[i]);
	      }
	      
	      if(request.getParameterValues("idAssociateAzNuove") != null 
          && i <  request.getParameterValues("idAssociateAzNuove").length 
          && Validator.isNotEmpty(request.getParameterValues("idAssociateAzNuove")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.idAssociateAzNuove", request.getParameterValues("idAssociateAzNuove")[i]);
        }
      
	      if(request.getParameterValues("cuaaEl") != null 
	        && i <  request.getParameterValues("cuaaEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("cuaaEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.cuaa", request.getParameterValues("cuaaEl")[i]);
	      }     
      
	      if(request.getParameterValues("partitaIvaEl") != null 
	        && i <  request.getParameterValues("partitaIvaEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("partitaIvaEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.partitaIva", request.getParameterValues("partitaIvaEl")[i]);
	      }
      
	      if(request.getParameterValues("denominazioneEl") != null 
	        && i <  request.getParameterValues("denominazioneEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("denominazioneEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.denominazione", request.getParameterValues("denominazioneEl")[i]);
	      }       
      
	      if(request.getParameterValues("istatComuneEl") != null 
	        && i <  request.getParameterValues("istatComuneEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("istatComuneEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.istatComune", request.getParameterValues("istatComuneEl")[i]);
	      }
	      
	      if(request.getParameterValues("desComEl") != null 
          && i <  request.getParameterValues("desComEl").length 
          && Validator.isNotEmpty(request.getParameterValues("desComEl")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.desCom", request.getParameterValues("desComEl")[i]);
        }
      
	      if(request.getParameterValues("siglaProvinciaEl") != null 
	        && i <  request.getParameterValues("siglaProvinciaEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("siglaProvinciaEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.siglaProvincia", request.getParameterValues("siglaProvinciaEl")[i]);
	      }
      
	      if(request.getParameterValues("indirizzoEl") != null 
	        && i <  request.getParameterValues("indirizzoEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("indirizzoEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.indirizzo", request.getParameterValues("indirizzoEl")[i]);
	      }
      
	      if(request.getParameterValues("capEl") != null 
	        && i <  request.getParameterValues("capEl").length 
	        && Validator.isNotEmpty(request.getParameterValues("capEl")[i])) 
	      {
	        htmpl.set("blkElencoAzAssociate.cap", request.getParameterValues("capEl")[i]);
	      }   
	      
	      if(request.getParameterValues("dataIngresso") != null 
          && i <  request.getParameterValues("dataIngresso").length 
          && Validator.isNotEmpty(request.getParameterValues("dataIngresso")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.dataIngresso", request.getParameterValues("dataIngresso")[i]);
        }
        
        if(request.getParameterValues("dataUscita") != null 
          && i <  request.getParameterValues("dataUscita").length 
          && Validator.isNotEmpty(request.getParameterValues("dataUscita")[i])) 
        {
          htmpl.set("blkElencoAzAssociate.dataUscita", request.getParameterValues("dataUscita")[i]);
        }       
      
     
      }
      
      
      
      
       countAssociati++;     
      
    }
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
