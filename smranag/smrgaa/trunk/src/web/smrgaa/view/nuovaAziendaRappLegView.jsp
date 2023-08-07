<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaAziendaRappLeg.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
	
	htmpl.set("radiobuttonAzienda",(String)request.getAttribute("radiobuttonAzienda"));
	request.setAttribute("radiobuttonAzienda", (String)request.getAttribute("radiobuttonAzienda"));
	htmpl.set("cuaaProvenienza",(String)request.getAttribute("cuaaProvenienza"));
	request.setAttribute("cuaaProvenienza", (String)request.getAttribute("cuaaProvenienza"));
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");

  String codiceFiscale = (String)session.getAttribute("codiceFiscaleRapp");
  session.removeAttribute("codiceFiscaleRapp");

  Date dataNascita = null;
  String sesso = null;
  ComuneVO comuneVO = null;
  if(codiceFiscale != null) {
    dataNascita = DateUtils.getDataNascitaFromCF(codiceFiscale);
    sesso = StringUtils.getSessoFromCF(codiceFiscale);
    try {
      String codiceFiscaleFinale = StringUtils.convertiCFOmonimo(codiceFiscale);
      comuneVO = anagFacadeClient.getComuneByCUAA(codiceFiscaleFinale.substring(11,15).toUpperCase());
    }
    catch(Exception e) {
    }
  }


  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
  if(errors == null) {
    if(personaFisicaVO != null) {
      if(personaFisicaVO.isNewPersonaFisica()) {
        if(anagAziendaVO.getTipoFormaGiuridica().getCode().compareTo(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)) == 0) {
          if(sesso != null) {
            if(sesso.equalsIgnoreCase("M")) {
              htmpl.set("checkedM","checked");
            }
            else {
              htmpl.set("checkedF","checked");
            }
          }
          else {
            if(personaFisicaVO.getSesso() != null) {
              if(personaFisicaVO.getSesso().equalsIgnoreCase("M")) {
                htmpl.set("checkedM","checked");
              }
              else if(personaFisicaVO.getSesso().equalsIgnoreCase("F")) {
                htmpl.set("checkedF","checked");
              }
            }
          }
          if(comuneVO != null) {
            if(!comuneVO.getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
              htmpl.set("descNascitaComune",comuneVO.getDescom());
              htmpl.set("nascitaProv",comuneVO.getSiglaProv());
            }
            else {
              htmpl.set("nascitaStatoEstero",comuneVO.getDescom());
            }
          }
          if(Validator.isNotEmpty(dataNascita)) {
            htmpl.set("nascitaData",DateUtils.formatDate(dataNascita));
          }
          else if(Validator.isNotEmpty(personaFisicaVO.getNascitaData())) {
            htmpl.set("nascitaData",DateUtils.formatDate(personaFisicaVO.getNascitaData()));
          }
        }
        else {
          if(personaFisicaVO.getSesso() != null) {
            if(personaFisicaVO.getSesso().equalsIgnoreCase("M")) {
              htmpl.set("checkedM","checked");
            }
            else {
              htmpl.set("checkedF","checked");
            }
          }
          if(personaFisicaVO.getNascitaData() != null) {
            htmpl.set("nascitaData",DateUtils.formatDate(personaFisicaVO.getNascitaData()));
          }
          if(codiceFiscale != null) {
            htmpl.set("codiceFiscale",codiceFiscale);
          }
          else {
            htmpl.set("codiceFiscale",personaFisicaVO.getCodiceFiscale());
          }
        }
      }
      else {
        if(personaFisicaVO.getSesso() != null) {
          if(personaFisicaVO.getSesso().equalsIgnoreCase("M")) {
            htmpl.set("checkedM","checked");
          }
          else {
            htmpl.set("checkedF","checked");
          }
        }
        if(personaFisicaVO.getNascitaData() != null) {
          htmpl.set("nascitaData",DateUtils.formatDate(personaFisicaVO.getNascitaData()));
        }
      }
      if(codiceFiscale != null) {
        htmpl.set("codiceFiscale",codiceFiscale);
      }
      else {
        htmpl.set("codiceFiscale",personaFisicaVO.getCodiceFiscale());
      }
      if(personaFisicaVO.getStatoEsteroRes()!=null && !personaFisicaVO.getStatoEsteroRes().equals(""))
      {
            htmpl.set("descStatoEsteroResidenza",personaFisicaVO.getStatoEsteroRes());
            htmpl.set("stato",personaFisicaVO.getResComune());
            htmpl.set("cittaResidenza",personaFisicaVO.getResCittaEstero());
            htmpl.set("resComune",personaFisicaVO.getResComune());
      }
      HtmplUtil.setValues(htmpl, personaFisicaVO);
    }
    else {
      String erroreCF = (String)session.getAttribute("erroreCF");
      session.removeAttribute("erroreCF");
      htmpl.set("codiceFiscale",StringUtils.checkNull(codiceFiscale));
      if(erroreCF != null && !erroreCF.equals("")) {
        htmpl.set("nascitaData",DateUtils.formatDate(dataNascita));
        if(sesso.equalsIgnoreCase("M")) {
          htmpl.set("checkedM","checked");
        }
        else {
          htmpl.set("checkedF","checked");
        }
        if(comuneVO != null) {
          if(!comuneVO.getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
            htmpl.set("descNascitaComune",comuneVO.getDescom());
            htmpl.set("nascitaProv",comuneVO.getSiglaProv());
          }
          else {
            htmpl.set("nascitaStatoEstero",comuneVO.getDescom());
          }
        }
      }
    }
  }
  else {
    if(personaFisicaVO != null) {
      if(personaFisicaVO.getSesso() != null) {
        if(personaFisicaVO.getSesso().equalsIgnoreCase("M")) {
          htmpl.set("checkedM","checked");
        }
        else {
          htmpl.set("checkedF","checked");
        }
      }
    }
    HtmplUtil.setValues(htmpl, request);
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }


  // Informazioni relative al titolo di studio
  Vector elencoTitoliStudio = null;
  try {
    elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
  }
  catch(SolmrException se) {}

  if(elencoTitoliStudio != null) {
    Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
    while(iteraTitoliStudio.hasNext()) {
      htmpl.newBlock("elencoTitoliStudio");
      CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
      if(personaFisicaVO != null) {
        if(personaFisicaVO.getIdTitoloStudio() != null) {
          if(personaFisicaVO.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
            htmpl.set("elencoTitoliStudio.check","selected");
          }
        }
      }
      htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
      htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
    }
    if(personaFisicaVO != null) {
      if(personaFisicaVO.getIdTitoloStudio() != null) {
        Vector elencoIndirizziStudio = null;
        try {
          if(personaFisicaVO.getIdTitoloStudio() != null) {
            elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(personaFisicaVO.getIdTitoloStudio());
          }
        }
        catch(SolmrException se) {
        }
        if(elencoIndirizziStudio != null) {
          Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
          while(iteraIndirizziStudio.hasNext()) {
            htmpl.newBlock("elencoIndirizziStudio");
            CodeDescription code = (CodeDescription)iteraIndirizziStudio.next();
            if(personaFisicaVO.getIdIndirizzoStudio() != null) {
              if(personaFisicaVO.getIdIndirizzoStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
                htmpl.set("elencoIndirizziStudio.check","selected");
              }
            }
            htmpl.set("elencoIndirizziStudio.idCodice",code.getCode().toString());
            htmpl.set("elencoIndirizziStudio.descrizione",code.getDescription());
          }
        }
      }
    }
  }

%>
<%= htmpl.text() %>
