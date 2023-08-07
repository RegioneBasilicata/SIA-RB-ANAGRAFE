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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaSezioneMantenimento.htm");

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
		  if(catalogoMatriceVO != null)
		  {
		    Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatriceVO.getIdCatalogoMatrice(), "N");
		    Vector<Long> vIdMantenimentoDef = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatriceVO.getIdCatalogoMatrice(), "S");
		    if(vIdMantenimento != null && vIdMantenimentoDef != null)
		    {
		      Long valDefault = vIdMantenimentoDef.get(0);
		      Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
		      if(vPraticaMantenim != null)
		      {
		        for(int l = 0; l < vPraticaMantenim.size(); l++) 
		        {
		          TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vPraticaMantenim.get(l);
		          htmpl.newBlock("blkTipoPraticaMantenimento");
		          htmpl.set("blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
		          htmpl.set("blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
		         
		          if(valDefault.compareTo(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento())) == 0) 
		          {
		            htmpl.set("blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
		          }               
		        }
		      }
		    
		    }
		    else
			  {
			    htmpl.newBlock("blkNoTipoPraticaMantenimento");
			  }
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipoPraticaMantenimento");
		  }
            	  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
	{
	  htmpl.newBlock("blkNoTipoPraticaMantenimento");
	}
	
 	

%>
<%= htmpl.text()%>
