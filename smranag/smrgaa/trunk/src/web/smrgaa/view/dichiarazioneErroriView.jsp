<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>


<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>

<%
  SolmrLogger.debug(this, " - dichiarazioneAnomaliaView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazione_E.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);


  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

  Vector errori=(Vector)request.getAttribute("erroriDichiarazioniConsistenza");

  String percorsoErrori = null;
  if(pathToFollow.equalsIgnoreCase("rupar")) {
    percorsoErrori = "/css_rupar/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("sispie")) {
	    percorsoErrori = "/css/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	   percorsoErrori="/css/agricoltura/im/";  
  }


  if (errori!=null)
  {
    int size=errori.size();
    if(size > 0)
    {
      Iterator iteraErrori = errori.iterator();
      ErrAnomaliaDicConsistenzaVO err = null;
      String descErroreOld="";
      while (iteraErrori.hasNext())
      {
        err = (ErrAnomaliaDicConsistenzaVO)iteraErrori.next();
        if (!descErroreOld.equals(err.getDescGruppoControllo()))
        {
          descErroreOld=err.getDescGruppoControllo();
          htmpl.newBlock("blkTabErrore");
          htmpl.set("blkTabErrore.descrizioneErrore",descErroreOld);
        }
        htmpl.newBlock("blkTabErrore.blkErrore");
        htmpl.set("blkTabErrore.blkErrore.tipologia",err.getTipoAnomaliaErrore());
        htmpl.set("blkTabErrore.blkErrore.descrizione",err.getDescAnomaliaErrore());
        if (err.isBloccante())
        {
          //htmpl.set("blkTabErrore.blkErrore.immagine","Bloccante.gif");
          // Reperisco l'immagine da ANDROMEDA
          htmpl.set("blkTabErrore.blkErrore.immagine", percorsoErrori + "Bloccante.gif");
          htmpl.set("blkTabErrore.blkErrore.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
        }
        else
        {
          //htmpl.set("blkTabErrore.blkErrore.immagine","Warning.gif");
          // Reperisco l'immagine da ANDROMEDA
          htmpl.set("blkTabErrore.blkErrore.immagine", percorsoErrori + "Warning.gif");
          htmpl.set("blkTabErrore.blkErrore.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
        }
      }
    }
  }


  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - dichiarazioneErroriView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>

