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
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboQualitaUsoSecondario.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione) 
    && Validator.isNotEmpty(idTipoDettaglioUso))
  {
  
	  try
	  {
	    Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(
	    new Long(idUtilizzo), new Long(idTipoDestinazione), new Long(idTipoDettaglioUso));
      if(Validator.isNotEmpty(vTipoQualitaUso))
      {
        for(int i=0;i<vTipoQualitaUso.size();i++) 
        {
          TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(i);
          htmpl.newBlock("blkTipiQualitaUso");
          
          htmpl.set("blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
          htmpl.set("blkTipiQualitaUso.descCompleta", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+tipoQualitaUsoVO.getDescrizioneQualitaUso());
          String descrizione = null;
          if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
          {
            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
          }
          else 
          {
            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
          }
          htmpl.set("blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
          
          if(vTipoQualitaUso.size() == 1)
          {
            htmpl.set("blkTipiQualitaUso.selected", "selected=\"selected\"", null);
          }
        
        }
      }
      else
	    {
	      htmpl.newBlock("blkNoTipiQualitaUso");
	    } 
      
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipiQualitaUso");
  } 
	
	if(Validator.isNotEmpty(provenienza))
  {
    if(provenienza.equalsIgnoreCase("modificaMultiplaTerreni"))
    {
      htmpl.newBlock("blkSelectModMul");
    }
    else if("inserisciParticellareCondUso".equalsIgnoreCase(provenienza))
    {
      htmpl.newBlock("blkSelectInsTer");      
      htmpl.set("blkSelectInsTer.indice", indice);
    }
    else if("modificaTerreni".equalsIgnoreCase(provenienza))
    {
      htmpl.newBlock("blkVariabiliMod");      
      htmpl.set("blkVariabiliMod.indice", indice);
    }
    else if("popModTerrCondUso".equalsIgnoreCase(provenienza))
    {
      htmpl.newBlock("blkSelectPopMod");      
      htmpl.set("blkSelectPopMod.indice", indice);
    }
  }
	
	
	
%>
<%= htmpl.text()%>
