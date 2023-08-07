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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboTipoAreaElenco.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idDichiarazioneConsistenzaStr = request.getParameter("idDichiarazioneConsistenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  
  try
  {   
    Vector<TipoAreaVO> elencoTipiArea = new Vector<TipoAreaVO>();  
    if(Validator.isNotEmpty(idDichiarazioneConsistenzaStr))
    {  	    
		  Long  idDichiarazioneConsistenza = Long.decode(idDichiarazioneConsistenzaStr);
		    
	    if(idDichiarazioneConsistenza.longValue() <= 0)
	    {
	      elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(null);
	    }   
	    else if(idDichiarazioneConsistenza.longValue() > 0)
	    {
	      elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(idDichiarazioneConsistenza);
	    }
	  }
	  else
	  {
	    elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(null);
	  }
	        
	  if(elencoTipiArea != null && elencoTipiArea.size() > 0) 
    {
      for(int i=0;i<elencoTipiArea.size();i++) 
	    {
	      TipoAreaVO tipoAreaVO = elencoTipiArea.get(i);
	      for(int k=0;k<tipoAreaVO.getvTipoValoreArea().size();k++)
	      {	 
	        TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(k); 
	            
		      htmpl.newBlock("blkTipoArea");
		      String idTipoValoreAreaStr = tipoValoreAreaVO.getIdTipoValoreArea()+"_"+tipoAreaVO.getFlagEsclusivoFoglio();
		      htmpl.set("blkTipoArea.idTipoValoraArea", idTipoValoreAreaStr);
		      
		      htmpl.set("blkTipoArea.descrizione", "("+tipoAreaVO.getDescrizione()+") "+tipoValoreAreaVO.getDescrizione());
		      if(Validator.isNotEmpty(filtriParticellareRicercaVO)  && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdTipoValoreArea()))
          {
            if(idTipoValoreAreaStr.equalsIgnoreCase(filtriParticellareRicercaVO.getIdTipoValoreArea()+"_"+filtriParticellareRicercaVO.getFlagFoglio())) 
		        {
		          htmpl.set("blkTipoArea.selected", "selected=\"selected\"", null);
		        }
		      }
		    }
	    }				  
		}
  }
  catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
  
	
	
	
	
    
  
	
 	

%>
<%= htmpl.text()%>
