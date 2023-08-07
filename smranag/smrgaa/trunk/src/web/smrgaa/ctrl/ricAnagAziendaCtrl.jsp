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
<%@page import="it.csi.solmr.dto.iride2.Iride2AbilitazioniVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<jsp:useBean id="aziendaVO" scope="request" class="it.csi.solmr.dto.anag.AnagAziendaVO">
<jsp:setProperty name="aziendaVO" property="*" />
<%
  aziendaVO.setCUAA(request.getParameter("CUAA"));
%>
</jsp:useBean>
<%

  WebUtils.removeUselessAttributes(session);

  String iridePageName = "ricAnagAziendaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  aziendaVO.setDescComune(request.getParameter("comune"));
  aziendaVO.setSedelegProv(request.getParameter("provincia"));
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  session.removeAttribute("anagAziendaVO");
  
  
  //Usato al momento per lo scambio dei MCU Iride nel passaggio
  //Dal "CAA_REALE" (Quello relativo al ruolo) e quello limitato
  //per poter accedere in sola visualizzazione alle aziende socie senza delega
  HashMap<String, Iride2AbilitazioniVO> hIride2Abilitazioni = (HashMap<String, Iride2AbilitazioniVO>)session.getAttribute("hIride2Abilitazioni");
  //Controllo se valorizzato l'attributo.
  //Se cosi' vuoldire che arrivo da una selezione del CAA_LIMITATO
  //nei soci quindi devo rispristinare i MCU e i diritti corretti.
  if(Validator.isNotEmpty(hIride2Abilitazioni))
  {
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    //Mantiene il diritto "Reale" relativo al ruolo!!! 
    String dirittoRW = (String)session.getAttribute("dirittoRW");
    
    ruoloUtenza.setDirittoAccesso(dirittoRW);
    session.setAttribute("iride2AbilitazioniVO", hIride2Abilitazioni.get("CAA_REALE"));
  }
  
  
  
  
  
  //Rimuovo l'attributo introdotto eventalmente nell'elenco soci per evitare problemi con iride!!!
  session.removeAttribute("idAziendaElencoSoci");

  Integer idTipoAzienda = null;

  if(request.getParameter("tipiAzienda") != null && !request.getParameter("tipiAzienda").equals(""))
  {
    idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
  }
  if(idTipoAzienda != null) {
    aziendaVO.setTipiAzienda(String.valueOf(idTipoAzienda));
    String tipiAzienda = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
    aziendaVO.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
  }
  else
  {
    aziendaVO.setTipiAzienda(null);
    aziendaVO.setTipoTipologiaAzienda(new CodeDescription());
  }

  String tipiFormaGiuridica = request.getParameter("tipiFormaGiuridica");

  Integer idTipoFormaGiuridica = null;

  if(tipiFormaGiuridica != null && !tipiFormaGiuridica.equals("")) {
    idTipoFormaGiuridica = Integer.decode(request.getParameter("tipiFormaGiuridica"));
  }

  if(idTipoFormaGiuridica != null) {
    aziendaVO.setTipiFormaGiuridica(String.valueOf(idTipoFormaGiuridica));
    aziendaVO.setTipoFormaGiuridica(new CodeDescription(Integer.decode(tipiFormaGiuridica),""));
  }
  else
  {
    aziendaVO.setTipiFormaGiuridica(null);
    aziendaVO.setTipoFormaGiuridica(new CodeDescription());
  }




  session.removeAttribute("personaVO");
  AnagAziendaVO aaVO = null;
  Vector vectIdAnagAzienda = null;
  String dataPuntuale = "";
  String dataAvanzata = "";
  int numBlock = 1;
  int sizeResult = 0;
  String url = null;
  String errorPage = "/view/ricAnagAziendaView.jsp";
  //String ricPuntualeURL = "/view/anagraficaView.jsp";
  String ricAvanzataURL = "/view/elencoView.jsp";

  ValidationException valEx=null;
  Validator validator = null;

  if(request.getParameter("ricercaPuntuale") != null) {
    session.removeAttribute("currPage");
    session.removeAttribute("listRange");
    session.removeAttribute("listIdAzienda");
    session.removeAttribute("insAnagVO");
    dataPuntuale = request.getParameter("dataPuntuale");
    aziendaVO.setDataSituazioneAlStr(dataPuntuale);

    ValidationErrors errors = aziendaVO.validateRicPuntuale();
    if (! (errors == null || errors.size() == 0)) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }

    validator = new Validator(errorPage);
    try {
      url = ricAvanzataURL;

      /**
       * Rimouovo gli eventuali parametri passati dalla ricerca avanzata
       * */
      aziendaVO.setDenominazione("");
      aziendaVO.setSedelegProv("");
      aziendaVO.setSedelegComune("");
      aziendaVO.setDescComune("");
      aziendaVO.setSedelegEstero("");
      aziendaVO.setTipoFormaGiuridica(new CodeDescription());
      aziendaVO.setTipiAzienda(null);
      aziendaVO.setPosizioneSchedario("");
      
      if (Validator.isNotEmpty(aziendaVO.getCUAA()))
        vectIdAnagAzienda = anagFacadeClient.getIdAnagAziendeCollegatebyCUAA(aziendaVO.getCUAA());
      else
        vectIdAnagAzienda = anagFacadeClient.getListIdAziende(aziendaVO,DateUtils.parse(dataPuntuale,"dd/MM/yyyy"), false);

      sizeResult=vectIdAnagAzienda.size();
      session.setAttribute("listIdAzienda",vectIdAnagAzienda);
      Vector rangeIdAA=new Vector();

      int limiteA;

      if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG;
        for(int i=(numBlock-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
            i<limiteA;i++){
          rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
        }
        Vector rangAnagAzienda = anagFacadeClient.getListAziendeByIdRange(rangeIdAA);
        for(int j=0; j<rangAnagAzienda.size(); j++){
          ((AnagAziendaVO)rangAnagAzienda.elementAt(j)).setDataSituazioneAlStr(dataPuntuale);
        }
        session.setAttribute("listRange",rangAnagAzienda);
    }
    catch (SolmrException sex) {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  %>
      <jsp:forward page ="<%=url%>" />
  <%
  }
  else if(request.getParameter("ricercaAvanzata") != null) {
    session.removeAttribute("currPage");
    session.removeAttribute("listRange");
    session.removeAttribute("listIdAzienda");
    session.removeAttribute("insAnagVO");
    dataAvanzata = request.getParameter("dataAvanzata");

    // Riazzero i parametri della ricerca puntuale
    aziendaVO.setCUAA(null);
    aziendaVO.setPartitaIVA(null);

    aziendaVO.setDataSituazioneAlStr(dataAvanzata);
    aziendaVO.setSedelegProv(request.getParameter("provincia"));

    String descSedeLegaleComune = request.getParameter("comune");
    String descSedeLegaleEstero = request.getParameter("sedelegEstero");

    aziendaVO.setDescComune(descSedeLegaleComune);
    aziendaVO.setSedelegEstero(descSedeLegaleEstero);

    ValidationErrors errors = aziendaVO.validateRicAvanzata();
    if (! (errors == null || errors.size() == 0)) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      Iterator iter = errors.get();
      while(iter.hasNext())
      return;
    }

    // Controllo che, se inseriti, il comune o lo stato estero siano corretti
    String istatComune = null;
    if(Validator.isNotEmpty(descSedeLegaleComune)) {
      try {
        istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(descSedeLegaleComune, aziendaVO.getSedelegProv());
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(errorPage).forward(request, response);
        return;
      }
    }
    else if(Validator.isNotEmpty(descSedeLegaleEstero)) {
      try {
        istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(descSedeLegaleEstero, "");
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
        errors.add("sedelegEstero", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(errorPage).forward(request, response);
        return;
      }
    }

    if(aziendaVO.getDescComune() == null || aziendaVO.getDescComune().equals("")) {
      aziendaVO.setDescComune(aziendaVO.getSedelegEstero());
    }
    validator = new Validator(errorPage);

    try {
      url = ricAvanzataURL;
        boolean attivita = true;
      if(request.getParameter("attivita")==null)
        attivita = false;
      vectIdAnagAzienda = anagFacadeClient.getListIdAziende(aziendaVO,DateUtils.parse(dataAvanzata,"dd/MM/yyyy"), attivita);

      aziendaVO.setDescComune(descSedeLegaleComune);
      aziendaVO.setSedelegEstero(descSedeLegaleEstero);

      sizeResult=vectIdAnagAzienda.size();
      session.setAttribute("listIdAzienda",vectIdAnagAzienda);
      Vector rangeIdAA=new Vector();

      int limiteA;

      if(sizeResult<SolmrConstants.NUM_MAX_ROWS_PAG)
          limiteA=sizeResult;
        else
          limiteA=SolmrConstants.NUM_MAX_ROWS_PAG;
        for(int i=(numBlock-1)*SolmrConstants.NUM_MAX_ROWS_PAG;
            i<limiteA;i++){
          rangeIdAA.addElement(vectIdAnagAzienda.elementAt(i));
        }
        Vector rangAnagAzienda = anagFacadeClient.getListAziendeByIdRange(rangeIdAA);
        for(int j=0; j<rangAnagAzienda.size(); j++){
          ((AnagAziendaVO)rangAnagAzienda.elementAt(j)).setDataSituazioneAlStr(dataAvanzata);
        }
        session.setAttribute("listRange",rangAnagAzienda);
    }
    catch (SolmrException sex) {
      ValidationError error = new ValidationError(sex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(errorPage).forward(request, response);
      return;
    }
  %>
      <jsp:forward page ="<%=url%>" />
  <%
  }
  // Else provvisorio per effettuare le prove e passare dai link e non dai pulsanti
  else {
    // Metto un parametro in request che mi indichi che si tratta del primo accesso
    // alla pagina
    request.setAttribute("firstTime", "si");
    %>
       <jsp:forward page = "/view/ricAnagAziendaView.jsp" />
    <%
  }
%>
