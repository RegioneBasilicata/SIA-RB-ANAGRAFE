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
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboUtilizzoEfa.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idTipoEfa = request.getParameter("idTipoEfa");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  TipoEfaVO tipoEfaVO = null;
  if(Validator.isNotEmpty(idTipoEfa))
  {
  
	  try
	  {
	  
	    Vector<TipoUtilizzoVO> vTipoUtilizzo = gaaFacadeClient.getListTipoUtilizzoEfa(new Long(idTipoEfa).longValue());
		  tipoEfaVO = gaaFacadeClient.getTipoEfaFromPrimaryKey(new Long(idTipoEfa).longValue());
		  if(Validator.isNotEmpty(vTipoUtilizzo))
		  {
			  for(int i=0;i<vTipoUtilizzo.size();i++) 
		    {
		      TipoUtilizzoVO tipoUtilizzoVO = vTipoUtilizzo.get(i);
		      htmpl.newBlock("blkTipiUsoSuoloEfa");
		      
		      htmpl.set("blkTipiUsoSuoloEfa.idTipoUtilizzoEfa", ""+tipoUtilizzoVO.getIdUtilizzo());
		      htmpl.set("blkTipiUsoSuoloEfa.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
		      String descrizione = null;
		      if(tipoUtilizzoVO.getDescrizione().length() > 20) 
		      {
		        descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
		      }
		      else 
		      {
		        descrizione = tipoUtilizzoVO.getDescrizione();
		      }
		      htmpl.set("blkTipiUsoSuoloEfa.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
		    }
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipiUsoSuoloEfa");
		  } 
		  
		  if(Validator.isNotEmpty(tipoEfaVO))
      {
        htmpl.set("fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
        htmpl.set("fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
        htmpl.set("descMisuraHd", tipoEfaVO.getDescUnitaMisura());
      }			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipiUsoSuoloEfa");
  } 
	
	
	/*if(Validator.isNotEmpty(provenienza))
  {
    if(provenienza.equalsIgnoreCase("popModTerrCondUso"))
    {
      htmpl.set("indice", indice);
      if(Validator.isNotEmpty(tipoEfaVO))
      {
	      htmpl.set("fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
	      htmpl.set("fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
	      htmpl.set("descMisuraHd", tipoEfaVO.getDescUnitaMisura());
	    }
    }
    else if(provenienza.equalsIgnoreCase("inserisciParticellareCondUso"))
    {
      htmpl.set("indice", indice);
      if(Validator.isNotEmpty(tipoEfaVO))
      {
        htmpl.set("fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
        htmpl.set("fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
        htmpl.set("descMisuraHd", tipoEfaVO.getDescUnitaMisura());
      }
    }       
  }*/
    
  
	
 	

%>
<%= htmpl.text()%>
