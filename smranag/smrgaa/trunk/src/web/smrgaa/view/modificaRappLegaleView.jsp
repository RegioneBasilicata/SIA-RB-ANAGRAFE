<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/anagraficaRappLegale_mod.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");

  PersonaFisicaVO vo = (PersonaFisicaVO)session.getAttribute("pfVOModifica");


  if(session.getAttribute("modificaCampiAnagrafici") != null)
  {
    htmpl.set("disabled","disabled=\"disabled\"", null);
    htmpl.newBlock("blkFieldDisabled");
    htmpl.set("blkFieldDisabled.cognome", personaVO.getCognome());
    htmpl.set("blkFieldDisabled.nome", personaVO.getNome());
    htmpl.set("blkFieldDisabled.sesso", personaVO.getSesso());
    if(Validator.isNotEmpty(personaVO.getNascitaData()))
      htmpl.set("blkFieldDisabled.strNascitaData", DateUtils.formatDate(personaVO.getNascitaData()));
    if(Validator.isNotEmpty(personaVO.getNascitaProv()))
      htmpl.set("blkFieldDisabled.nascitaProv", personaVO.getNascitaProv());
    if(Validator.isNotEmpty(personaVO.getDescNascitaComune()))
      htmpl.set("blkFieldDisabled.descNascitaComune", personaVO.getDescNascitaComune());
    if(Validator.isNotEmpty(personaVO.getNascitaCittaEstero()))
      htmpl.set("blkFieldDisabled.nascitaStatoEstero", personaVO.getNascitaCittaEstero());
    if(Validator.isNotEmpty(personaVO.getCittaNascita()))
      htmpl.set("blkFieldDisabled.cittaNascita", personaVO.getCittaNascita());
    if(Validator.isNotEmpty(personaVO.getResIndirizzo()))
      htmpl.set("blkFieldDisabled.resIndirizzo", personaVO.getResIndirizzo());
    if(Validator.isNotEmpty(personaVO.getResProvincia()))
      htmpl.set("blkFieldDisabled.resProvincia", personaVO.getResProvincia());
    if(Validator.isNotEmpty(personaVO.getDescResComune()))
      htmpl.set("blkFieldDisabled.descResComune", personaVO.getDescResComune());
    if(Validator.isNotEmpty(personaVO.getResCAP()))
      htmpl.set("blkFieldDisabled.resCAP", personaVO.getResCAP());
    if(Validator.isNotEmpty(personaVO.getStatoEsteroRes()))
      htmpl.set("blkFieldDisabled.statoEsteroRes", personaVO.getStatoEsteroRes());
    if(Validator.isNotEmpty(personaVO.getCittaResidenza()))
      htmpl.set("blkFieldDisabled.cittaResidenza", personaVO.getCittaResidenza());
    if(Validator.isNotEmpty(personaVO.getDomIndirizzo()))
      htmpl.set("blkFieldDisabled.domIndirizzo", personaVO.getDomIndirizzo());
    if(Validator.isNotEmpty(personaVO.getDomProvincia()))
      htmpl.set("blkFieldDisabled.domProvincia", personaVO.getDomProvincia());
    if(Validator.isNotEmpty(personaVO.getDomComune()))
      htmpl.set("blkFieldDisabled.domComune", personaVO.getDomComune());
    if(Validator.isNotEmpty(personaVO.getDomCAP()))
      htmpl.set("blkFieldDisabled.domCAP", personaVO.getDomCAP());
    if(Validator.isNotEmpty(personaVO.getDomicilioStatoEstero()))
      htmpl.set("blkFieldDisabled.domicilioStatoEstero", personaVO.getDomicilioStatoEstero());
    if(Validator.isNotEmpty(personaVO.getDescCittaEsteroDomicilio()))
      htmpl.set("blkFieldDisabled.descCittaEsteroDomicilio", personaVO.getDescCittaEsteroDomicilio());
    if(Validator.isNotEmpty(personaVO.getNote()))
      htmpl.set("blkFieldDisabled.note", personaVO.getNote());
    if(Validator.isNotEmpty(personaVO.getIdTitoloStudio()))
      htmpl.set("blkFieldDisabled.idTitoloStudio", ""+personaVO.getIdTitoloStudio());
    if(Validator.isNotEmpty(personaVO.getIdIndirizzoStudio()))
      htmpl.set("blkFieldDisabled.idIndirizzoStudio", ""+personaVO.getIdIndirizzoStudio());
  }
  else
  {
    htmpl.newBlock("blkFieldEnabled");
  }



  if(personaVO!=null) {
    if(personaVO.getNascitaData() != null) {
      personaVO.setStrNascitaData(DateUtils.formatDate(personaVO.getNascitaData()));
    }
  }

  // Recupero il codice fiscale del rappresentante legale
  String isOk = (String)session.getAttribute("isOk");
  if(Validator.isNotEmpty(isOk)) 
  {
    if(isOk.equalsIgnoreCase("true")) 
    {
      if(vo != null && Validator.isNotEmpty(vo.getCodiceFiscale())) 
      {
        htmpl.set("codiceFiscale", vo.getCodiceFiscale());
      }
      else {
      
        htmpl.set("codiceFiscale", personaVO.getCodiceFiscale());
      }
      htmpl.set("readOnly", "readOnly");
    }
    else {
      //htmpl.set("codiceFiscale", personaVO.getCodiceFiscale());
    	htmpl.set("codiceFiscale", vo.getCodiceFiscale());
    }
  }

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(anagAziendaVO.getCUAA() != null) {
    htmpl.set("CUAA",anagAziendaVO.getCUAA());
  }
  htmpl.set("aziendaSelezionata",anagAziendaVO.getDenominazione());
  htmpl.set("dataSituazioneAl",anagAziendaVO.getDataSituazioneAlStr());

  if(errors == null) {
    if(vo!=null){
      if(vo.getSesso()!=null){
        if(vo.getSesso().equalsIgnoreCase("M")) {
          htmpl.set("checkedM","checked");
        }
        else {
          htmpl.set("checkedF","checked");
        }
      }
      else{
        htmpl.set("checkedM","checked");
      }
      ComuneVO comuneVO = null;
      if(vo.getNascitaComune()!=null && !vo.getNascitaComune().equals("")){
        try{
          comuneVO = anagFacadeClient.getComuneByISTAT(vo.getNascitaComune());
        }
        catch(Exception ex) {
        }
      }
      if(comuneVO!=null && comuneVO.getFlagEstero().equals(SolmrConstants.FLAG_S)) {
        htmpl.set("nascitaStatoEstero",vo.getDescNascitaComune());
        htmpl.set("cittaNascita",vo.getNascitaCittaEstero());
        htmpl.set("descNascitaComune","");
        htmpl.set("nascitaProv","");
      }
      else{
        htmpl.set("descNascitaComune",vo.getDescNascitaComune());
        htmpl.set("nascitaProv",vo.getNascitaProv());
      }
      // Valorizzo la città estera di residenza
      if(Validator.isNotEmpty(vo.getResCittaEstero())) {
        htmpl.set("cittaResidenza", vo.getResCittaEstero());
      }
      HtmplUtil.setValues(htmpl, vo);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
    else{
      HtmplUtil.setValues(htmpl, request);
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
  }
  else {

    String sesso = (String)session.getAttribute("sesso");
    session.removeAttribute("sesso");
    if(sesso != null) {
      if(sesso.equalsIgnoreCase("M")) {
        htmpl.set("checkedM","checked");
      }
      else {
        htmpl.set("checkedF","checked");
      }
    }
    else{
      htmpl.set("checkedM","checked");
    }
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }
  if(anagAziendaVO.getCUAA()!=null && !anagAziendaVO.getCUAA().equals(""))
    htmpl.set("aziendaSelezionata",anagAziendaVO.getCUAA()+" - "+anagAziendaVO.getDenominazione());
  else
    htmpl.set("aziendaSelezionata",anagAziendaVO.getDenominazione());

  String operazione = (String)session.getAttribute("operazione");
  session.removeAttribute("operazione");
  htmpl.set("operazione",operazione);


  // Informazioni relative al titolo di studio
  Vector elencoTitoliStudio = null;
  Long idTitoloStudio = (Long)session.getAttribute("idTitoloStudio");
  try {
    elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
  }
  catch(SolmrException se) {}

  if(elencoTitoliStudio != null) {
    Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
    while(iteraTitoliStudio.hasNext()) {
      htmpl.newBlock("elencoTitoliStudio");
      CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
      if(vo.getIdTitoloStudio() != null) {
        if(vo.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
          htmpl.set("elencoTitoliStudio.check","selected");
        }
      }
      htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
      htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
    }
    if(vo.getIdTitoloStudio() != null) {
      Vector elencoIndirizziStudio = null;
      try {
        elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(vo.getIdTitoloStudio());
      }
      catch(SolmrException se) {
      }
      if(elencoIndirizziStudio != null) {
        Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
        while(iteraIndirizziStudio.hasNext()) {
          htmpl.newBlock("elencoIndirizziStudio");
          CodeDescription code = (CodeDescription)iteraIndirizziStudio.next();
          if(vo.getIdIndirizzoStudio() != null) {
            if(vo.getIdIndirizzoStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
              htmpl.set("elencoIndirizziStudio.check","selected");
            }
          }
          htmpl.set("elencoIndirizziStudio.idCodice",code.getCode().toString());
          htmpl.set("elencoIndirizziStudio.descrizione",code.getDescription());
        }
      }
    }
  }

%>
<%= htmpl.text()%>
