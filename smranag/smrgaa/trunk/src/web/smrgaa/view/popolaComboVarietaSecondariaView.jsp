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
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboVarietaSecondaria.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
  String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione) 
      && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso))
  {
  
	  try
	  {
	    Vector<TipoVarietaVO> tipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
        new Long(idUtilizzo), new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso));
		  for(int i=0;i<tipoVarieta.size();i++) 
	    {
	      TipoVarietaVO tipoVarietaVO = tipoVarieta.get(i);
	      htmpl.newBlock("blkTipiVarieta");
	      
	      htmpl.set("blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
	      htmpl.set("blkTipiVarieta.descCompleta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
	      if(i==0)
	      {
	        htmpl.set("blkTipiVarieta.selected", "selected=\"selected\"", null);
	      }
	      
	      String descrizione = null;
	      if(tipoVarietaVO.getDescrizione().length() > 20) 
	      {
	        descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
	      }
	      else 
	      {
	        descrizione = tipoVarietaVO.getDescrizione();
	      }
	      htmpl.set("blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
	    }	  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipiVarieta");
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
