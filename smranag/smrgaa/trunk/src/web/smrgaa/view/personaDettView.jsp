<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/persona_det.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  HtmplUtil.setValues(htmpl, request);
  //Valorizza segnaposto errore htmpl
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  PersonaFisicaVO pfVO = (PersonaFisicaVO)session.getAttribute("personaFisicaVO");
  String dataNascita = "";
  if(pfVO.getNascitaData()!=null)
    dataNascita = DateUtils.extractDayFromDate(pfVO.getNascitaData())+"/"+DateUtils.extractMonthFromDate(pfVO.getNascitaData())+"/"+DateUtils.extractYearFromDate(pfVO.getNascitaData());

  htmpl.set("cognome", pfVO.getCognome());
  htmpl.set("nome", pfVO.getNome());
  htmpl.set("codFiscale", pfVO.getCodiceFiscale());
  htmpl.set("dataNascita", dataNascita);
  if(pfVO.getNascitaCittaEstero()!=null)
    htmpl.set("luogoNascita", pfVO.getNascitaCittaEstero());
  else htmpl.set("luogoNascita", pfVO.getDescNascitaComune());
  htmpl.set("sesso", pfVO.getSesso());
  htmpl.set("resIndirizzo", pfVO.getResIndirizzo());
  if(pfVO.getStatoEsteroRes()!=null) {
    htmpl.set("resComune", "");
    htmpl.set("resProvincia", "");
  }
  else {
    htmpl.set("resComune", pfVO.getDescResComune());
    htmpl.set("resProvincia", pfVO.getDescResProvincia());
  }
  htmpl.set("resCAP", pfVO.getResCAP());
  htmpl.set("statoEstero", pfVO.getStatoEsteroRes());
  if(pfVO.getResCittaEstero()!=null)
    htmpl.set("citta", pfVO.getResCittaEstero());
  else htmpl.set("citta", pfVO.getCittaResidenza());
  htmpl.set("telefono", pfVO.getResTelefono());
  htmpl.set("fax", pfVO.getResFax());
  htmpl.set("email", pfVO.getResMail());
  htmpl.set("domIndirizzo", pfVO.getDomIndirizzo());
  if(Validator.isNotEmpty(pfVO.getDomProvincia())) {
    htmpl.newBlock("blkDomicilioItalia");
    htmpl.set("blkDomicilioItalia.domProvincia", pfVO.getDomProvincia());
    htmpl.set("blkDomicilioItalia.domComune", pfVO.getDomComune());
    htmpl.set("blkDomicilioItalia.domCAP", pfVO.getDomCAP());
  }
  else {
    if(Validator.isNotEmpty(pfVO.getDomicilioStatoEstero())) {
      htmpl.newBlock("blkDomicilioEstero");
      htmpl.set("blkDomicilioEstero.domicilioStatoEstero", pfVO.getDomicilioStatoEstero());
    }
    if(Validator.isNotEmpty(pfVO.getDescCittaEsteroDomicilio())) {
      htmpl.set("blkDomicilioEstero.descCittaEsteroDomicilio", pfVO.getDescCittaEsteroDomicilio());
    }
  }

  // Inserisco le informazioni relative al titolo di studio
  String titoloIndirizzoStudio = null;
  if(pfVO.getIdTitoloStudio() != null) {
    titoloIndirizzoStudio = pfVO.getDescrizioneTitoloStudio();
  }
  if(pfVO.getIdIndirizzoStudio() != null) {
    titoloIndirizzoStudio += " - "+pfVO.getDescrizioneIndirizzoStudio();
  }
  if(titoloIndirizzoStudio != null) {
    htmpl.set("titoloIndirizzoStudio",titoloIndirizzoStudio);
  }

  htmpl.set("note", pfVO.getNote());

  HtmplUtil.setValues(htmpl, request);

%>
<%= htmpl.text()%>
