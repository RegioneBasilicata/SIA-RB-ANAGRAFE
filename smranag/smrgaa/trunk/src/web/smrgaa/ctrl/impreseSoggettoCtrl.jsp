<%@ page language="java"
  contentType="text/html"
  isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

  String iridePageName = "impreseSoggettoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  String precPage = "/view/elencoPersoneView.jsp";
  String elencoAziendeURL = "/view/impreseSoggettoView.jsp";
  String dettaglioAzienda = "/layout/anagrafica.htm";
  String dettagliAziendaView = "/view/anagraficaView.jsp";
  String url = "";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

  Validator validator = null;
  ValidationErrors errors = new ValidationErrors();
  try{
    if(request.getParameter("selectedSoggetto") != null && request.getParameter("idPersonaFisica") != null){
      Long idSoggetto = new Long(request.getParameter("selectedSoggetto"));
      Vector idAziendeVect = anagFacadeClient.getIdAziendeBySoggetto(idSoggetto);

      if(idAziendeVect.size()>0){
        Vector aziendeResult = anagFacadeClient.findAziendeByIdAziende(idAziendeVect);
        PersonaFisicaVO pfVO = anagFacadeClient.findByPrimaryKey(new Long(request.getParameter("idPersonaFisica")));
        //Vector pfVO = anagFacadeClient.findPersonaFisicaVector(new Long(request.getParameter("idPersonaFisica")));
        session.setAttribute("listAziende",aziendeResult);
        session.setAttribute("personaFisicaVO",pfVO);
        url = elencoAziendeURL;
      }

    }else if(request.getParameter("indietro") != null){
      url = precPage;
    }else //if(request.getParameter("idAnagAzienda") != null){
      if(request.getParameter("dettAzienda") != null&&request.getParameter("dettAzienda").equals("true")){
    if(request.getParameter("aziende")==null){
      ValidationError error = new ValidationError("Selezionare un''azienda");
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(elencoAziendeURL).forward(request, response);
      return;
    }else{
      Long idAnagAzienda = new Long(""+request.getParameter("aziende"));
      AnagAziendaVO anagAziendaVO = anagFacadeClient.getAziendaById(idAnagAzienda);
      if (utenteAbilitazioni.getRuolo().isUtenteIntermediario()) {
        try {
          anagFacadeClient.utenteConDelega(utenteAbilitazioni, anagAziendaVO.getIdAzienda());
        } catch (Exception exc) {
          ValidationError error = new ValidationError(AnagErrors.INTERMEDIARIO_SENZA_DELEGA);
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(elencoAziendeURL).forward(request, response);
          return;
        }
      }
      if(anagAziendaVO!=null && anagAziendaVO.getTipoFormaGiuridica()!=null)
        anagAziendaVO.setTipiFormaGiuridica(anagAziendaVO.getTipoFormaGiuridica().getDescription());
      session.setAttribute("anagAziendaVO", anagAziendaVO);
      url = dettagliAziendaView;
    }
      }
      else if(request.getParameter("idPersonaFisica") == null){
        ValidationError error = new ValidationError("Selezionare una persona");
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(precPage).forward(request, response);
        return;
      }
  }catch(SolmrException ex){
    ValidationError error = new ValidationError(ex.getMessage());
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(precPage).forward(request, response);
  }

  if (dettagliAziendaView.equals(url))
  {
    /***
     *  Dato che questo controller lavora  con diverse view (appartenenti
     * a macroCU diversi sono necessarie più include)
     **/
    iridePageName = "anagraficaCtrl.jsp";
    %><%@include file = "/include/autorizzazione.inc" %><%
  }

  %>
  <jsp:forward page = "<%=url%>" />
  <%
%>
