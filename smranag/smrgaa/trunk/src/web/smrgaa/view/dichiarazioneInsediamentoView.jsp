<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>


<%
  SolmrLogger.debug(this, " - dichiarazioneInsediamentoView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazioneInsediamento.htm");
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


  String messaggioErrore = (String) request.getAttribute("messaggioErrore");
  if (Validator.isNotEmpty(messaggioErrore))
  {
    //Devo visualizzare l'errore
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }
  else
  {
    //Proseguo normalmente con la visualizzazione della pagina
    SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

    if (((String)SolmrConstants.get("P_ESITO_CONTR")).equals((String)session.getAttribute("common")))
      //L'utente non può proseguire
      htmpl.newBlock("blkLblAnomalie");
    else
      //L'utente può proseguire
      htmpl.newBlock("blkBtnConferma");

    Vector anomalie=(Vector)request.getAttribute("anomalieDichiarazioniInsediamento");

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

          if (((String)SolmrConstants.get("DICHIARAZIONE_SEGNALAZIONE_BLOCCANTE")).equals(err.getBloccanteStr()))
          {
            // Reperisco l'immagine da ANDROMEDA
            htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
            htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
          }
          else
            if (((String)SolmrConstants.get("DICHIARAZIONE_SEGNALAZIONE_WARNING")).equals(err.getBloccanteStr()))
            {
              // Reperisco l'immagine da ANDROMEDA
              htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
              htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
            }
            else
            {
              // Reperisco l'immagine da ANDROMEDA
              htmpl.set("blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "ok.gif");
              htmpl.set("blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("NO_ANOMALIA"));
            }
        }
      }
    }
  }

  SolmrLogger.debug(this, " - dichiarazioneInsediamentoView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>


