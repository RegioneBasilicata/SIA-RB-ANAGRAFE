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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboDettaglioUsoEfa.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idTipoEfa = request.getParameter("idTipoEfa");
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  try
  {
    if(Validator.isNotEmpty(idTipoEfa)
      && Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione))
    {
	  
	    Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(new Long(idTipoEfa),
	      new Long(idUtilizzo), new Long(idTipoDestinazione));
	    if(Validator.isNotEmpty(vTipoDettaglioUso))
	    {
			  for(int i=0;i<vTipoDettaglioUso.size();i++) 
		    {
		      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(i);
		      htmpl.newBlock("blkTipiDettaglioUsoEfa");
		      
		      htmpl.set("blkTipiDettaglioUsoEfa.idTipoDettaglioUsoEfa", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
		      htmpl.set("blkTipiDettaglioUsoEfa.descCompleta", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+tipoDettaglioUsoVO.getDescrizione());
		      
		      String descrizione = null;
		      if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
		      {
		        descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
		      }
		      else 
		      {
		        descrizione = tipoDettaglioUsoVO.getDescrizione();
		      }
		      htmpl.set("blkTipiDettaglioUsoEfa.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
		    }
		  }
		  else
	    {
	      htmpl.newBlock("blkNoTipiDettaglioUsoEfa");
	    } 
		  
		  htmpl.set("numeroUtilizziEfa", ""+indice);
		
	  }
	  else
    {
      htmpl.newBlock("blkNoTipiDettaglioUsoEfa");
    }  
		      
  }
  catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
  
  /*if(Validator.isNotEmpty(provenienza))
  {
    if("popModTerrCondUso".equalsIgnoreCase(provenienza))
    {                    
      htmpl.set("indice", indice);
    }
    else if("inserisciParticellareCondUso".equalsIgnoreCase(provenienza))
    {                    
      htmpl.set("indice", indice);
    }
  }*/
    
  
	
 	

%>
<%= htmpl.text()%>
