<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>


<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%
  SolmrLogger.debug(this, " - dichiarazioneVerificaKoView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneVerifica_ko.htm");
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

  Vector anomalie=(Vector)request.getAttribute("anomalieDichiarazioniConsistenza");

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

  if (anomalie!=null)
  {
    int size=anomalie.size();
    if(size > 0)
    {
      Iterator iteraAnomalie = anomalie.iterator();
      ErrAnomaliaDicConsistenzaVO err = null;
      String descAnomaliaOld="";
      while (iteraAnomalie.hasNext())
      {
        err = (ErrAnomaliaDicConsistenzaVO)iteraAnomalie.next();
        if (!descAnomaliaOld.equals(err.getDescGruppoControllo()))
        {
          descAnomaliaOld=err.getDescGruppoControllo();
          htmpl.newBlock("blkTabAnomalia");
          htmpl.set("blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
        }
        htmpl.newBlock("blkTabAnomalia.blkAnomalia");
        htmpl.set("blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
        htmpl.set("blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
        if (err.isBloccante())
        {
          //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Bloccante.gif");
          // Reperisco l'immagine da ANDROMEDA
          htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
          htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
        }
        else
        {
          //htmpl.set("blkTabAnomalia.blkAnomalia.immagine","Warning.gif");
          // Reperisco l'immagine da ANDROMEDA
          htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
          htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
        }
      }
    }
  }


  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - dichiarazioneVerificaKoView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>

