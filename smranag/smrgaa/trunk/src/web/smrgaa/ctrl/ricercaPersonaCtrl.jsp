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

<%

  String iridePageName = "ricercaPersonaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  WebUtils.removeUselessAttributes(session);
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  //session.removeAttribute("personaVO");
  AnagAziendaVO aaVO = null;
  //Vector vectIdAnagAzienda = null;
  int numBlock = 1;
  int sizeResult = 0;
  String url = "";

  String errorPage = "/view/ricercaPersonaView.jsp";
  String risultatoRicerca = "/layout/elencoPersone.htm";

  ValidationException valEx=null;
  Validator validator = null;
  ValidationErrors errors = new ValidationErrors();
  String codFiscale ="";
  String cognome ="";
  String nome ="";
  Vector personeVector = new Vector();

  if(request.getParameter("ricerca") != null) {
    request.setAttribute("primaRicerca","false");
    session.removeAttribute("currPage");
    aaVO = new AnagAziendaVO();
    if(request.getParameter("codFiscale") != null){
      codFiscale = request.getParameter("codFiscale");
      aaVO.setCodFiscale(codFiscale);
    }
    if(request.getParameter("cognome") != null){
      cognome = request.getParameter("cognome");
      aaVO.setCognome(cognome);
      if(request.getParameter("nome") != null){
        nome = request.getParameter("nome");
        aaVO.setNome(nome);
      }
    }
    // Recupero i parametri
    String dataNascitaPersonaFisica = request.getParameter("dataNascita");
    String provinciaNascitaPersonaFisica = request.getParameter("nascitaProv");
    String comuneNascitaPersonaFisica = request.getParameter("descNascitaComune");
    String statoEsteroNascitaPersonaFisica = request.getParameter("nascitaStatoEstero");
    String provinciaResidenzaPersonaFisica = request.getParameter("resProvincia");
    String comuneResidenzaPersonaFisica = request.getParameter("descResComune");
    String descStatoResidenzaPersonaFisica = request.getParameter("descStatoEsteroResidenza");
    // Setto i parametri all'interno del VO
    aaVO.setDataNascitaPersonaFisica(dataNascitaPersonaFisica);
    aaVO.setProvinciaNascitaPersonaFisica(provinciaNascitaPersonaFisica);
    aaVO.setComuneNascitaPersonaFisica(comuneNascitaPersonaFisica);
    aaVO.setStatoEsteroNascitaPersonaFisica(statoEsteroNascitaPersonaFisica);
    aaVO.setProvinciaResidenzaPersonaFisica(provinciaResidenzaPersonaFisica);
    aaVO.setComuneResidenzaPersonaFisica(comuneResidenzaPersonaFisica);
    aaVO.setStatoEsteroResidenzaPersonaFisica(descStatoResidenzaPersonaFisica);
    validator = new Validator(errorPage);

    // Effettuo la validazione dei dati

    errors = aaVO.validateRicercaPersona();
    if (! (errors == null || errors.size() == 0)) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }



    // Recupero, se valorizzati, i codici istat relativi al luogo di nascita e a quello di residenza
    // della persona fisica "ricercata" dall'utente
    String istatNascita = null;
    String istatResidenza = null;
    if(provinciaNascitaPersonaFisica != null && !provinciaNascitaPersonaFisica.equals("")) {
      try {
        istatNascita = anagFacadeClient.ricercaCodiceComune(comuneNascitaPersonaFisica, provinciaNascitaPersonaFisica);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descNascitaComune",error);
      }
    }
    else {
      if(statoEsteroNascitaPersonaFisica != null && !statoEsteroNascitaPersonaFisica.equals("")) {
        try {
          istatNascita = anagFacadeClient.ricercaCodiceComune(statoEsteroNascitaPersonaFisica, "");
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("nascitaStatoEstero",error);
        }
      }
    }
    if(provinciaResidenzaPersonaFisica != null && !provinciaResidenzaPersonaFisica.equals("")) {
      try {
        istatResidenza = anagFacadeClient.ricercaCodiceComune(comuneResidenzaPersonaFisica, provinciaResidenzaPersonaFisica);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descResComune",error);
      }
    }
    else {
      if(descStatoResidenzaPersonaFisica != null && !descStatoResidenzaPersonaFisica.equals("")) {
        try {
          istatResidenza = anagFacadeClient.ricercaCodiceComune(descStatoResidenzaPersonaFisica, "");
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("descResComune",error);
        }
      }
    }

    validator = new Validator(errorPage);

    // Effettuo la validazione dei dati
    if (! (errors == null || errors.size() == 0)) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
    try{
      if(request.getParameter("codFiscale") != null)
        codFiscale = request.getParameter("codFiscale");
      if(request.getParameter("cognome") != null){
        cognome = request.getParameter("cognome");
        if(request.getParameter("nome") != null)
          nome = request.getParameter("nome");
      }
      boolean personaAttiva = true;

      if(request.getParameter("personaAttiva")==null)

        personaAttiva = false;

      personeVector = anagFacadeClient.getIdPersoneFisiche(codFiscale, cognome, nome, dataNascitaPersonaFisica,
                                                           istatNascita, istatResidenza, personaAttiva);

      sizeResult=personeVector.size();
      session.setAttribute("listIdPF",personeVector);
      Vector rangeIdPF=new Vector();
      int limiteA;
      if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG)
        limiteA=sizeResult;
      else
        limiteA=SolmrConstants.NUM_MAX_ROWS_PAG;
      for(int i=(numBlock-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
          i<limiteA;i++){
        rangeIdPF.addElement(personeVector.elementAt(i));
      }
      
      Vector rangePF = anagFacadeClient.getListPersoneFisicheByIdRange(rangeIdPF);
      

      session.setAttribute("listRange",rangePF);
      url = risultatoRicerca;
    }//fine try
    catch (SolmrException exc) {
      ValidationError error = new ValidationError(exc.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  }//fine if(request.getParameter("ricerca") != null)
  else
  {
    /**
     * Questo variabile mi serve per selezionare di default il check
     * relativo alla persona attiva, la prima volta che chiamo la ricerca
     */
    request.setAttribute("primaRicerca","true");
    url = errorPage;
  }
    %>
      <jsp:forward page ="<%=url%>" />
    <%
%>
