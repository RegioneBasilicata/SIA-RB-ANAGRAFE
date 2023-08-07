<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.UtenteIrideVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  public final static String VIEW = "../view/manodoperaNewConduzioneView.jsp";
  public final static String INDIETRO = "../layout/manodoperaNew.htm";
  public final static String ELENCO = "../layout/manodopera.htm";
%>

<%

  String iridePageName = "manodoperaNewConduzioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  try 
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

    ValidationErrors errors = new ValidationErrors();

    //HashMap tenuto in session e contenente tutti i dati per l'inserimento della manodopera
    HashMap hmManodopera = (HashMap) session.getAttribute("common");
    ManodoperaVO manodoperaVO = (ManodoperaVO) hmManodopera.get("manodoperaVO");

    //Vector necessario per la gestione delle attività complementari inserite dall'utente
    Vector vDettaglioAttivita = null;
    DettaglioAttivitaVO dettaglioAttivitaVO = null;

    //Elenco id Tipo Attività complementari selezionate dall'utente
    if (hmManodopera.get("idTipoAttCompl") == null) {
      //se è la prima volta
      hmManodopera.put("idTipoAttCompl", new String[0]);
    }
    else if (request.getParameterValues("idTipoAttCompl") == null
           && request.getParameter("dataInizioValidita") != null) {
      //non ci sono elementi dalla stessa pagina
      hmManodopera.put("idTipoAttCompl", new String[0]);
    }
    if (request.getParameterValues("idTipoAttCompl") != null) {
      //refresh, inserisci o salva
      hmManodopera.put("idTipoAttCompl", request.getParameterValues("idTipoAttCompl"));
    }

    //Elenco descrizione Tipo Attività complementari selezionate dall'utente
    if (hmManodopera.get("desTipoAttCompl") == null) {
      //se è la prima volta
      hmManodopera.put("desTipoAttCompl", new String[0]);
    }
    if (request.getParameterValues("desTipoAttCompl") != null) {
      //refresh, inserisci o salva
      hmManodopera.put("desTipoAttCompl", request.getParameterValues("desTipoAttCompl"));
    }

    //Elenco descrizione Attività complementari inserite dall'utente
    if (hmManodopera.get("descrizioneAttivitaCompl") == null) {
      //se è la prima volta
      hmManodopera.put("descrizioneAttivitaCompl", new String[0]);
    }
    if (request.getParameterValues("descrizioneAttCompl") != null) {
      //refresh, inserisci o salva
      hmManodopera.put("descrizioneAttivitaCompl", request.getParameterValues("descrizioneAttCompl"));
    }

    //Forma di conduzione
    if (request.getParameter("tipoFormaConduzione") != null) {
      manodoperaVO.setTipoFormaConduzione(request.getParameter("tipoFormaConduzione"));
    }

    hmManodopera.put("manodoperaVO", manodoperaVO);
    session.setAttribute("common", hmManodopera);

    //E' stato premuto il tasto indietro
    if (request.getParameter("indietro") != null) {
      response.sendRedirect(INDIETRO);
      return;
    }

    //E' stato premuto il tasto salva
    if (request.getParameter("salva") != null) 
    {
      //Attività complementari svolte in azienda (tipologia e descrizione)
      //Caricamento del vettore contenente DettaglioAttivitaVO necessario per l'inserimento
      String[] idTipoAttComplStr = (String[]) hmManodopera.get("idTipoAttCompl");
      if (idTipoAttComplStr != null && idTipoAttComplStr.length > 0) {
        String[] desTipoAttComplStr = (String[]) hmManodopera.get("desTipoAttCompl");
        String[] descrizioneStr = (String[]) hmManodopera.get("descrizioneAttivitaCompl");

        vDettaglioAttivita = new Vector();

        for (int i=0; i<idTipoAttComplStr.length; i++) {
          dettaglioAttivitaVO = new DettaglioAttivitaVO();
          dettaglioAttivitaVO.setIdManodoperaLong(manodoperaVO.getIdManodoperaLong());
          dettaglioAttivitaVO.setAttivitaComplementari(new CodeDescription(new Integer(idTipoAttComplStr[i]),desTipoAttComplStr[i]));
          dettaglioAttivitaVO.setDescrizione(descrizioneStr[i].trim());
          vDettaglioAttivita.add(dettaglioAttivitaVO);
        }
        manodoperaVO.setVDettaglioAttivita(vDettaglioAttivita);
      }

      manodoperaVO.setDataInizioValidita(DateUtils.getCurrentDateString());
      errors = manodoperaVO.validateConduzione(request, anagFacadeClient.findLastManodopera(anagAziendaVO.getIdAzienda()).getDataFineValidita());

      if ((errors != null && errors.size() > 0)
        || (request.getAttribute("messaggioErrore") != null)) 
      {
        request.setAttribute("errors", errors);
      }
      else 
      {
        //inserimento manodopera su db
        //IdAzienda
        manodoperaVO.setIdAziendaLong(anagAziendaVO.getIdAzienda());

        //IdUtenteAggiornamento
        UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
        utenteIrideVO.setIdUtente(ruoloUtenza.getIdUtente());
        manodoperaVO.setUtenteAggiornamento(utenteIrideVO);

        //Tipo Forma Conduzione
        TipoFormaConduzioneVO tipoFormaConduzioneVO = new TipoFormaConduzioneVO();
        tipoFormaConduzioneVO.setIdFormaConduzione(manodoperaVO.getTipoFormaConduzione());
        manodoperaVO.setTipoFormaConduzioneVO(tipoFormaConduzioneVO);

        anagFacadeClient.insertManodopera(manodoperaVO, anagAziendaVO.getIdAzienda());

        response.sendRedirect(ELENCO);
        return;
      }
    }

    %>
      <jsp:forward page="<%= VIEW %>"/>
    <%
  }
  catch(Exception e) {
    if (e instanceof SolmrException) {
      setError(request, e.getMessage());
    }
    else {
      e.printStackTrace();
      setError(request, "Si è verificato un errore di sistema");
    }
    %>
      <jsp:forward page="<%=VIEW%>" />
    <%
  }
%>

<%!
  private void setError(HttpServletRequest request, String msg) {

    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError(msg));
    request.setAttribute("errors", errors);
  }
%>
