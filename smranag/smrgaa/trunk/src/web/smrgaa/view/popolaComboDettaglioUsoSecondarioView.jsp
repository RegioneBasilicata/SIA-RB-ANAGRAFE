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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboDettaglioUsoSecondario.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione))
  {
  
	  try
	  {
	    Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(new Long(idUtilizzo), new Long(idTipoDestinazione));
	    if(Validator.isNotEmpty(vTipoDettaglioUso))
	    {
			  for(int i=0;i<vTipoDettaglioUso.size();i++) 
		    {
		      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(i);
		      htmpl.newBlock("blkTipiDettaglioUso");
		      
		      htmpl.set("blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
		      htmpl.set("blkTipiDettaglioUso.descCompleta", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
		      String descrizione = null;
		      if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
		      {
		        descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
		      }
		      else 
		      {
		        descrizione = tipoDettaglioUsoVO.getDescrizione();
		      }
		      htmpl.set("blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
		    
		      if(vTipoDettaglioUso.size() == 1)
          {
            htmpl.set("blkTipiDettaglioUso.selected", "selected=\"selected\"", null);
          }
		    
		    }
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipiDettaglioUso");
		  } 
      
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipiDettaglioUso");
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
