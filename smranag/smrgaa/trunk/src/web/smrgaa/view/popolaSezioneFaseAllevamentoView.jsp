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
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaSezioneFaseAllevamento.htm");

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
	  
	    CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(new Long(idUtilizzo),new Long(idVarieta), 
        new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso));
		  if(catalogoMatriceVO != null && "S".equalsIgnoreCase(catalogoMatriceVO.getPermanente()))
		  {
		    Vector<TipoFaseAllevamentoVO> vFaseAllev = gaaFacadeClient.getElencoFaseAllevamento();
		    if(vFaseAllev != null)
		    {		    
		      for(int l = 0; l < vFaseAllev.size(); l++) 
		      {
		        TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vFaseAllev.get(l);
		        htmpl.newBlock("blkTipoFaseAllevamento");
		        htmpl.set("blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
		        htmpl.set("blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
		         
		        if("S".equalsIgnoreCase(tipoFaseAllevamentoVO.getFlagDefault())) 
		        {
		          htmpl.set("blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
		        }               
		      }		    
		    }
		  }
		  else
		  {
		    htmpl.newBlock("blkHiddenFaseAllevamento");
		    htmpl.set("blkHiddenFaseAllevamento.idFaseAllevamento", "");
		    htmpl.set("disabledFaseAlleavamento", "disabled=\"true\"", null);
		  }
            	  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
  {
    htmpl.newBlock("blkHiddenFaseAllevamento");
    htmpl.set("blkHiddenFaseAllevamento.idFaseAllevamento", "");
    htmpl.set("disabledFaseAlleavamento", "disabled=\"true\"", null);
  }
	
 	

%>
<%= htmpl.text()%>
