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
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboVarietaEfa.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idTipoEfa = request.getParameter("idTipoEfa");
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
  String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idTipoEfa)
    && Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione)
    && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso))
  {
  
	  try
	  {
	  
	    Vector<TipoVarietaVO> vTipoVarieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(new Long(idTipoEfa), new Long(idUtilizzo),
	      new Long(idTipoDestinazione), new Long(idTipoDettaglioUso),new Long(idTipoQualitaUso));
	    if(vTipoVarieta != null)
	    {
			  for(int i=0;i<vTipoVarieta.size();i++) 
		    {
		      TipoVarietaVO tipoVarietaVO = vTipoVarieta.get(i);
		      htmpl.newBlock("blkTipiVarietaEfa");
		      
		      htmpl.set("blkTipiVarietaEfa.idVarietaEfa", tipoVarietaVO.getIdVarieta().toString());
		      htmpl.set("blkTipiVarietaEfa.descCompleta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
		      String descrizione = null;
		      if(tipoVarietaVO.getDescrizione().length() > 20) 
		      {
		        descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
		      }
		      else 
		      {
		        descrizione = tipoVarietaVO.getDescrizione();
		      }
		      htmpl.set("blkTipiVarietaEfa.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
		    }
		    
		    Integer abbPonderazione = gaaFacadeClient.getAbbPonderazioneByMatrice(new Long(idTipoEfa), 
            new Long(idUtilizzo), new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso), vTipoVarieta.get(0).getIdVarieta());
        htmpl.set("abbPonderazioneVarieta", ""+abbPonderazione);
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipiVarietaEfa");
		  } 
		  
		  htmpl.set("indice", indice);
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipiVarietaEfa");
  } 
	
	/*if(Validator.isNotEmpty(provenienza))
  {
    if(provenienza.equalsIgnoreCase("popModTerrCondUso"))
    {
      htmpl.set("indice", indice);
    }
    else if(provenienza.equalsIgnoreCase("inserisciParticellareCondUso"))
    {
      htmpl.set("indice", indice);
    }       
  }*/
    
  
	
 	

%>
<%= htmpl.text()%>
