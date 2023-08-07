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

	java.io.InputStream layout = application.getResourceAsStream("/layout/tabellaDatiNewRichSoggAssCaa.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idAzienda = request.getParameter("idAzienda");  
  String tipoModifica = request.getParameter("tipoModifica");
  String numRiga = request.getParameter("numRiga");
  String[] dataIngresso = request.getParameterValues("dataIngresso");
  String[] dataUscita = request.getParameterValues("dataUscita");
  String[] codiceEnte = request.getParameterValues("codiceEnte");
  Vector<String> vCodiceEnte = new Vector<String>();
  if(Validator.isNotEmpty(codiceEnte))
  {
    for(int i=0;i<codiceEnte.length;i++)
    {
      if(Validator.isNotEmpty(codiceEnte[i]))
      {
        vCodiceEnte.add(codiceEnte[i]);
      }
    }
  } 
 
  
  try
  {
  
    AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    request.setAttribute("anagAziendaVO", anagAziendaVO);
  
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        new Long(SolmrConstants.RICHIESTA_VAR_SOCI).longValue());
    
  
    IntermediarioAnagVO intermediario = anagFacadeClient
      .findIntermediarioVOByIdAzienda(new Long(idAzienda));
      
    Vector<IntermediarioAnagVO> vIntermediari = anagFacadeClient.findFigliIntermediarioVOById(intermediario.getIdIntermediario());
      
   
   Vector<IntermediarioAnagVO> vIntermediariModCodFisc = new Vector<IntermediarioAnagVO>();    
   for(int j=0;j<vIntermediari.size();j++)
   {
     IntermediarioAnagVO intermediarioAnagVO = vIntermediari.get(j);
     if(vCodiceEnte.contains(intermediarioAnagVO.getCodiceFiscale()))
     {
       vIntermediariModCodFisc.add(intermediarioAnagVO);
     }
   }
      
      
     
    Vector<IntermediarioAnagVO> vIntermediariMod = new Vector<IntermediarioAnagVO>();
    //E' presente almeno un caa a video...
    if(vIntermediariModCodFisc.size() > 0)
    {
      //Devo far vedere solo quelli che non sono stati eliminati...
      for(int j=0;j<vIntermediariModCodFisc.size();j++)
      {
        IntermediarioAnagVO intermediarioAnagVO = vIntermediariModCodFisc.get(j);
        if(Validator.isNotEmpty(tipoModifica)
	        && "elimina".equalsIgnoreCase(tipoModifica))
	      {
	        if(j != new Integer(numRiga).intValue())
	        {
	          vIntermediariMod.add(intermediarioAnagVO);
	        }
	      }
         
      }
    }
    
    
    
    
    
    
    //Calcolo numero soggetti
    int numAziende = 0;
    if(Validator.isNotEmpty(vIntermediariMod.size() > 0))
    {
      numAziende = vIntermediariMod.size();
    }
    
    
    
    int countAziende = 0;
    
    for(int i=0;i<numAziende;i++)
    {    
    
      htmpl.newBlock("blkElencoAzAssociate");
      
      htmpl.set("blkElencoAzAssociate.idRiga", ""+i);
      
      
      IntermediarioAnagVO intermediarioAnagVO = vIntermediariMod.get(i);
      
      
      
      htmpl.set("blkElencoAzAssociate.cuaa", intermediarioAnagVO.getExtCuaa());
      htmpl.set("blkElencoAzAssociate.partitaIva", intermediarioAnagVO.getPartitaIva());
      htmpl.set("blkElencoAzAssociate.denominazione", intermediarioAnagVO.getDenominazione());
      htmpl.set("blkElencoAzAssociate.codiceEnte", intermediarioAnagVO.getCodiceFiscale());
      htmpl.set("blkElencoAzAssociate.desCom", intermediarioAnagVO.getDesCom());
      htmpl.set("blkElencoAzAssociate.siglaProvincia", intermediarioAnagVO.getSglProv());
      htmpl.set("blkElencoAzAssociate.indirizzo", intermediarioAnagVO.getIndirizzo(), null);
      htmpl.set("blkElencoAzAssociate.cap", intermediarioAnagVO.getCap());
      
      
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
      
      countAziende++;  
        
      
    }
    
  
  
	  
	  
		  
	}
	catch(Throwable ex)
	{
	  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
	}
    
  
	
 	

%>
<%= htmpl.text()%>
