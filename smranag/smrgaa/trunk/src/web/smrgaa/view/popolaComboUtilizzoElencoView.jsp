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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboUtilizzoElenco.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idDichiarazioneConsistenzaStr = request.getParameter("idDichiarazioneConsistenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  
  try
  {   
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();  
    if(Validator.isNotEmpty(idDichiarazioneConsistenzaStr))
    {  	    
		  Long  idDichiarazioneConsistenza = Long.decode(idDichiarazioneConsistenzaStr);
		    
	    if(idDichiarazioneConsistenza.longValue() == 0)
	    {
	      elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazioneStor(anagAziendaVO.getIdAzienda());
	    }
	    else if(idDichiarazioneConsistenza.longValue() < 0)
	    {
	      elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazione(anagAziendaVO.getIdAzienda());
	    }   
	    else if(idDichiarazioneConsistenza.longValue() > 0)
	    {
	      elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrValidazione(idDichiarazioneConsistenza);
	    }
	  }
	  else
	  {
	    elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazione(anagAziendaVO.getIdAzienda());
	  }
	        
	  if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
    {
      int indice = 0;
		  for(int i=0;i<elencoTipiUsoSuolo.size();i++) 
	    {
	      TipoUtilizzoVO tipoUtilizzoVO = elencoTipiUsoSuolo.get(i);	      
	      if(indice == 0) 
	      {
	        htmpl.newBlock("blkTipiUsoSuolo");
	        htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "-1");
	        htmpl.set("blkTipiUsoSuolo.descrizione", "qualunque destinazione produttiva");
	        if(filtriParticellareRicercaVO.getIdUtilizzo().toString().equalsIgnoreCase("-1")) 
	        {
	          htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
	        }
	        htmpl.newBlock("blkTipiUsoSuolo");
	        htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "0");
	        htmpl.set("blkTipiUsoSuolo.descrizione", "senza destinazione produttiva");
	        if(filtriParticellareRicercaVO.getIdUtilizzo().intValue() == 0) {
	          htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
	        }
	      }
	      
	      htmpl.newBlock("blkTipiUsoSuolo");
	      htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", ""+tipoUtilizzoVO.getIdUtilizzo());
	      htmpl.set("blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
	      String descrizione = null;
	      if(tipoUtilizzoVO.getDescrizione().length() > 20) 
	      {
	        descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
	      }
	      else 
	      {
	        descrizione = tipoUtilizzoVO.getDescrizione();
	      }
	      htmpl.set("blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
	      indice++;
	      if(Validator.isNotEmpty(filtriParticellareRicercaVO)
	        && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() == tipoUtilizzoVO.getIdUtilizzo().intValue()))
	      {
	        htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
	      }
	    }				  
		}
		else 
	  {
	    htmpl.newBlock("blkTipiUsoSuolo");
	    htmpl.set("blkTipiUsoSuolo.idTipoUtilizzo", "0");
	    htmpl.set("blkTipiUsoSuolo.descrizione", "senza destinazione produttiva");
	    if(Validator.isNotEmpty(filtriParticellareRicercaVO)
	     && (filtriParticellareRicercaVO.getIdUtilizzo().intValue() == 0)) 
	    {
	      htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
	    }
	  }
  }
  catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
  
	
	
	
	
    
  
	
 	

%>
<%= htmpl.text()%>
