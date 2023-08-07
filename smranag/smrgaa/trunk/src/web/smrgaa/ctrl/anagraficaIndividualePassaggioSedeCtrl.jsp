<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "anagraficaIndividualePassaggioSedeCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String anagraficaUrl = "/view/anagraficaView.jsp";
  String url = "/view/anagraficaIndividualePassaggioSedeView.jsp";
  String annullaUrl = "/view/anagraficaIndividualePassaggioNuovoTitolareView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagPassaggioVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");
  PersonaFisicaVO personaTitolareVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
  PersonaFisicaVO personaTitolareOldVO = (PersonaFisicaVO)session.getAttribute("personaTitolareOld");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // L'utente ha premuto il tasto avanti.
  if(request.getParameter("avanti") != null) 
  {
    // Recupero i parametri
    String sedeLegaleIndirizzo = request.getParameter("sedelegIndirizzo");
    String sedeLegaleProvincia = request.getParameter("sedelegProv");
    String descrizioneSedeComune = request.getParameter("sedelegComune");
    String sedeLegaleCap = request.getParameter("sedelegCAP");
    String sedeLegaleEstero = request.getParameter("statoEstero");
    String cittaSedeLegaleEstero = request.getParameter("sedelegCittaEstero");
    String telefono = request.getParameter("telefono");
    String fax = request.getParameter("fax");
    String sitoWeb = request.getParameter("sitoWEB");
    String mail = request.getParameter("mail");
    // Setto i valori del VO
    anagPassaggioVO.setSedelegIndirizzo(sedeLegaleIndirizzo);
    anagPassaggioVO.setSedelegProv(sedeLegaleProvincia);
    anagPassaggioVO.setDescComune(descrizioneSedeComune);
    anagPassaggioVO.setSedelegCAP(sedeLegaleCap);
    anagPassaggioVO.setStatoEstero(sedeLegaleEstero);
    anagPassaggioVO.setSedelegCittaEstero(cittaSedeLegaleEstero);
    anagPassaggioVO.setTelefono(telefono);
    anagPassaggioVO.setFax(fax);
    anagPassaggioVO.setSitoWEB(sitoWeb);
    anagPassaggioVO.setMail(mail);
    // Effettuo la validazione dei dati
    ValidationErrors errors =  anagPassaggioVO.validateSedeLegale();
    if(errors != null && errors.size() != 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Ricerco l'istat del comune della sede legale e la provincia di competenza
    String istatComune = null;
    String istatStatoEstero = null;
    try 
    {
      if(descrizioneSedeComune != null && !descrizioneSedeComune.equals("")) 
      {
        istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(descrizioneSedeComune,sedeLegaleProvincia);
        ComuneVO comune = anagFacadeClient.getComuneByISTAT(istatComune);
      }
      else 
      {
        istatStatoEstero = anagFacadeClient.ricercaCodiceComune(sedeLegaleEstero,"");
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    anagPassaggioVO.setSedelegComune(istatComune);
    anagPassaggioVO.setSedelegEstero(istatStatoEstero);
    String cognomeNomeUtente = ruoloUtenza.getDenominazione();
    //Da vedere con stefano getInfoAggiuntiva
    anagPassaggioVO.setUltimaModifica(DateUtils.formatDate(new Date(System.currentTimeMillis()))+" "
      +cognomeNomeUtente+" "+ruoloUtenza.getDescrizioneEnte());
    anagPassaggioVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());

    // Se non è più legato alla società senza ruolo
    String ruolo = (String)session.getAttribute("ruolo");
    if(ruolo.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      personaTitolareOldVO.setIdRuolo(new Long(SolmrConstants.TIPORUOLO_COADIUVANTE));
    }
    if(ruolo.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
    {
      personaTitolareOldVO.setIdRuolo(new Long(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG));
    }
    Long idAnagAzienda = null;
    try 
    {
      idAnagAzienda = anagFacadeClient.updateTitolareAzienda(anagPassaggioVO, personaTitolareOldVO, personaTitolareVO, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    String tipiFormaGiuridica = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA, SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE);
    anagPassaggioVO.setTipiFormaGiuridica(tipiFormaGiuridica);
    anagPassaggioVO.setIdAnagAzienda(idAnagAzienda);

    try 
    {
      anagPassaggioVO = anagFacadeClient.getAziendaById(anagPassaggioVO.getIdAnagAzienda());
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    session.removeAttribute("anagPassaggioVO");
    session.setAttribute("anagAziendaVO",anagPassaggioVO);
    session.removeAttribute("personaTitolareVO");
    session.removeAttribute("personaTitolareOld");
    session.removeAttribute("indietro");
    %>
      <jsp:forward page="<%= anagraficaUrl %>"/>
    <%
  }
  // L'utente ha premuto il tasto annulla
  if(request.getParameter("indietro") != null) 
  {
    // Recupero i parametri
    String sedeLegaleIndirizzo = request.getParameter("sedelegIndirizzo");
    String sedeLegaleProvincia = request.getParameter("sedelegProv");
    String descrizioneSedeComune = request.getParameter("sedelegComune");
    String sedeLegaleCap = request.getParameter("sedelegCAP");
    String sedeLegaleEstero = request.getParameter("statoEstero");
    String telefono = request.getParameter("telefono");
    String fax = request.getParameter("fax");
    String sitoWeb = request.getParameter("sitoWEB");
    String mail = request.getParameter("mail");
    // Setto i valori del VO
    anagPassaggioVO.setSedelegIndirizzo(sedeLegaleIndirizzo);
    anagPassaggioVO.setSedelegProv(sedeLegaleProvincia);
    anagPassaggioVO.setSedelegComune(descrizioneSedeComune);
    anagPassaggioVO.setDescComune(descrizioneSedeComune);
    anagPassaggioVO.setSedelegCAP(sedeLegaleCap);
    anagPassaggioVO.setStatoEstero(sedeLegaleEstero);
    anagPassaggioVO.setTelefono(telefono);
    anagPassaggioVO.setFax(fax);
    anagPassaggioVO.setMail(mail);
    session.removeAttribute("anagPassaggioVO");
    session.setAttribute("anagPassaggioVO",anagPassaggioVO);
    session.setAttribute("indietro","indietro");
    %>
       <jsp:forward page="<%= annullaUrl %>"/>
    <%
  }
%>

