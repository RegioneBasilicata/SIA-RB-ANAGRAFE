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
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboDestinazione.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  //String idParticellaCertificata = request.getParameter("idParticellaCertificata");
  
  TipoUtilizzoVO tipoUtilizzoVO = null;
  try
  {
    
    if(Validator.isNotEmpty(idUtilizzo))
    {
	  
	    Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(new Long(idUtilizzo));
	    tipoUtilizzoVO = anagFacadeClient.findTipoUtilizzoByPrimaryKey(new Long(idUtilizzo));
	    if(Validator.isNotEmpty(vTipoDestinazione))
	    {
			  for(int i=0;i<vTipoDestinazione.size();i++) 
		    {
		      TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(i);
		      htmpl.newBlock("blkTipiDestinazione");
		      
		      htmpl.set("blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
		      htmpl.set("blkTipiDestinazione.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
		      String descrizione = null;
		      if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
		      {
		        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
		      }
		      else 
		      {
		        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
		      }
		      htmpl.set("blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
		      
		      if(vTipoDestinazione.size() == 1)
		      {
		        htmpl.set("blkTipiDestinazione.selected", "selected=\"selected\"", null);
		      }
		    
		    }
		  }
		  else
	    {
	      htmpl.newBlock("blkNoTipiDestinazione");
	    }  
		
	  }
	  else
    {
      htmpl.newBlock("blkNoTipiDestinazione");
    }      
  }
  catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
  
  if(Validator.isNotEmpty(provenienza))
  {
    if(provenienza.equalsIgnoreCase("modificaMultiplaTerreni"))
    {
      htmpl.newBlock("blkSelectModMul");
      htmpl.newBlock("blkCoeffRiduzioneTerrMult");
      if(Validator.isNotEmpty(tipoUtilizzoVO))
        htmpl.set("blkCoeffRiduzioneTerrMult.coefficienteRiduzioneCombo", Formatter.formatDouble2(tipoUtilizzoVO.getCoefficienteRiduzione()));
    }
    else if("inserisciParticellareCondUso".equalsIgnoreCase(provenienza))
    {
      htmpl.newBlock("blkSelectInsTer");      
      htmpl.set("blkSelectInsTer.indice", indice);
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
    else if(provenienza.equalsIgnoreCase("modificaMultiplaUV"))
    {
      htmpl.newBlock("blkSelectModMulUV");
    }
  }
      	

%>
<%= htmpl.text()%>
