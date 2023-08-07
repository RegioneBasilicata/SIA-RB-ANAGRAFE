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
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaSezioneSeminaSecondario.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idUtilizzo = request.getParameter("idUtilizzo");
  String idTipoDestinazione = request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
  String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
  String idVarieta = request.getParameter("idVarieta");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione) 
      && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso)
      && Validator.isNotEmpty(idVarieta))
  {  
	  try
	  {
		  Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo( new Long(idUtilizzo), 
		    new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso), new Long(idVarieta));
      if(vTipoPeriodoSemina != null)
      {
        for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
        {
          TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
          htmpl.newBlock("blkTipoPeriodoSemina");
          htmpl.set("blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
          htmpl.set("blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
         
          if("S".equalsIgnoreCase(tipoPeriodoSeminaVO.getFlagDefault())) 
          {
            htmpl.set("blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
          }               
        }
      }
      else
		  {
		    htmpl.newBlock("blkNoTipoPeriodoSemina");
		  }
      
      Vector<TipoSeminaVO> vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
      for(int e=0;e<vTipoSemina.size(); e++) 
      {
        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
        htmpl.newBlock("blkTipoSemina");
        htmpl.set("blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
        htmpl.set("blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
        if("S".equalsIgnoreCase(tipoSeminaVO.getFlagDefault()))
        {
          htmpl.set("blkTipoSemina.selected", "selected=\"selected\"", null);
        }
      }
     
               	  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkNoTipoPeriodoSemina");
    htmpl.newBlock("blkNoTipoSemina");
  }
	
	
	if(Validator.isNotEmpty(provenienza))
  {
    if("inserisciParticellareCondUso".equalsIgnoreCase(provenienza))
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
