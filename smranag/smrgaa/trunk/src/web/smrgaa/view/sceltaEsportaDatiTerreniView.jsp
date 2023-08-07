<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.TipoEsportazioneDatiVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sceltaEsportaDatiTerreni.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");  
  String descrizionePiano = (String)request.getAttribute("descrizionePiano");
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  Vector<TipoEsportazioneDatiVO> vTipoEsportazione = (Vector<TipoEsportazioneDatiVO>)request.getAttribute("vTipoEsportazione");
  
  String tipoEsportaDatiTerreni = request.getParameter("tipoEsportaDatiTerreni");
  
  
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    htmpl.newBlock("blkNoErrore");
  }  
  
  htmpl.set("descrizionePiano", descrizionePiano); 
  
  
  int rowcount = 0;
  for(int i=0;i<vTipoEsportazione.size();i++)
  {
    if(filtriParticellareRicercaVO.getIdPianoRiferimento() <=0)
    {
      if("S".equalsIgnoreCase(vTipoEsportazione.get(i).getEsportazioneInLavorazione()))
      {
        rowcount++;
      }
    }
    else
    {
      if("S".equalsIgnoreCase(vTipoEsportazione.get(i).getEsportazioneDichiarazione()))
      {
        rowcount++;
      }
    }
    
  }
  
  boolean first = true;
  for(int i=0;i<vTipoEsportazione.size();i++)
  {
    if(filtriParticellareRicercaVO.getIdPianoRiferimento() <=0)
    {
      if("S".equalsIgnoreCase(vTipoEsportazione.get(i).getEsportazioneInLavorazione()))
      {
		    htmpl.newBlock("blkEsportazioneDati");
		    if(first)
		    {
		      htmpl.newBlock("blkEsportazioneDati.blkPrimo");
		      htmpl.set("blkEsportazioneDati.blkPrimo.rowcount", ""+rowcount);
		    }
		    
		    htmpl.newBlock("blkEsportazioneDati.blkSecondo");
		    if(first)
        {
          first = false;
          htmpl.set("blkEsportazioneDati.blkSecondo.checkedPrimo", "checked=\"true\"", null);
        }
		    htmpl.set("blkEsportazioneDati.blkSecondo.codiceEsportazione", vTipoEsportazione.get(i).getCodiceEsportazione());
		    htmpl.set("blkEsportazioneDati.blkSecondo.descrizione", vTipoEsportazione.get(i).getDescrizione());
		  }
		}
		else
		{
		  if("S".equalsIgnoreCase(vTipoEsportazione.get(i).getEsportazioneDichiarazione()))
      {
        htmpl.newBlock("blkEsportazioneDati");
        if(first)
        {
          first = false;
          htmpl.newBlock("blkEsportazioneDati.blkPrimo");
          htmpl.set("blkEsportazioneDati.blkPrimo.rowcount", ""+rowcount);
        }
        
        htmpl.newBlock("blkEsportazioneDati.blkSecondo");
        htmpl.set("blkEsportazioneDati.blkSecondo.codiceEsportazione", vTipoEsportazione.get(i).getCodiceEsportazione());
        htmpl.set("blkEsportazioneDati.blkSecondo.descrizione", vTipoEsportazione.get(i).getDescrizione());
      }
		}
  }
  
  
  
  
  
    

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
