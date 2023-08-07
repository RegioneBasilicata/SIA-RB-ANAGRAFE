<%@ page language="java" contentType="text/html" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "nuovaAziendaAnagraficaReloadCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%


  String url = "/view/nuovaAziendaAnagraficaView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");


  // Prelievo dei parametri per la costruzione del value object.
  String cuaa = request.getParameter("CUAA");
  String partitaIVA = request.getParameter("partitaIVA");
  String denominazione = request.getParameter("denominazione");
  String tipoIntermediarioDelegato = request.getParameter("idIntermediario");
  String idTipoIntermediarioDelegato = null;
  if(tipoIntermediarioDelegato != null && !tipoIntermediarioDelegato.equals("")) 
  {
    idTipoIntermediarioDelegato = tipoIntermediarioDelegato;
  }
  else 
  {
    if(utenteAbilitazioni.getRuolo().isUtenteIntermediario()) 
    {
      //profiloUtenza.getIdEnte ...anche qui...proviamo...
      idTipoIntermediarioDelegato = new Long(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario()).toString();
    }
  }
  Integer idTipoFormaGiuridica = null;

  Integer idTipoAzienda = null;
  if(request.getParameter("tipiAzienda") != null && !request.getParameter("tipiAzienda").equals("")) 
  {
    idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
  }

  ValidationErrors errors = new ValidationErrors();

  // Recupero il valore della provincia competenza selezionata
  String provinciaCompetenza = null;

  if(utenteAbilitazioni.getRuolo().isUtenteProvinciale())
  {
    //Anche qui profiloUtenza.getIdEnte....proviamo
    provinciaCompetenza = new Long(utenteAbilitazioni.getEnteAppartenenza().getAmmCompetenza().getIdAmmCompetenza()).toString();
  }
  else 
    provinciaCompetenza = request.getParameter("provincePiemonte");

  // Se l'oggetto AnagAziendaVO è nullo significa che è la prima volta che tento di effettuare
  // l'inserimento di una nuova azienda agricola è quindi lo istanzio.
  if (anagAziendaVO == null) 
  {
    anagAziendaVO = new AnagAziendaVO();
  }

  // Setto il value object
  anagAziendaVO.setCUAA(cuaa.toUpperCase());
  try
  {
    anagAziendaVO.setIdAziendaSubentro(new Long(request.getParameter("idAziendaSubentro")));
  } 
  catch(Exception e) {}
  anagAziendaVO.setOldCUAA(cuaa.toUpperCase());
  anagAziendaVO.setPartitaIVA(partitaIVA);
  anagAziendaVO.setDenominazione(denominazione.toUpperCase());


  if(idTipoAzienda != null) 
  {
    anagAziendaVO.setTipiAzienda(String.valueOf(idTipoAzienda));
    String tipiAzienda = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
    anagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
  }
  else
  {
    anagAziendaVO.setTipiAzienda(null);
    anagAziendaVO.setTipoTipologiaAzienda(null);
  }
  anagAziendaVO.setTipoFormaGiuridica(null);

  anagAziendaVO.setProvincePiemonte(provinciaCompetenza);
  anagAziendaVO.setProvCompetenza(provinciaCompetenza);
  anagAziendaVO.setTelefono(request.getParameter("telefono"));
  anagAziendaVO.setFax(request.getParameter("fax"));
  anagAziendaVO.setSitoWEB(request.getParameter("sitoWEB"));
  anagAziendaVO.setMail(request.getParameter("mail"));
  anagAziendaVO.setPec(request.getParameter("pec"));
  anagAziendaVO.setNote(request.getParameter("note"));

  
  
  anagAziendaVO.setIdIntermediarioDelegato(idTipoIntermediarioDelegato);
  session.setAttribute("insAnagVO",anagAziendaVO);



%>
<jsp:forward page="<%= url %>"/>
