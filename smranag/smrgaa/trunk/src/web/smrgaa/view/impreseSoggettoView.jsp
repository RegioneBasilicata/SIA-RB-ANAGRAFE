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

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/impreseSoggetto.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  PersonaFisicaVO pf = (PersonaFisicaVO)session.getAttribute("personaFisicaVO");

  Vector vectAziende = (Vector)session.getAttribute("listAziende");
  Vector elencoSoggetti = new Vector();
  for(int i=0; i<vectAziende.size();i++){
    AnagAziendaVO aaVO = (AnagAziendaVO)vectAziende.elementAt(i);
    Vector pfVect = anagFacadeClient.findPersonaFisicaByIdSoggettoAndIdAzienda(pf.getIdSoggetto(), aaVO.getIdAzienda());
    elencoSoggetti.addAll(pfVect);
  }
  Collections.sort(elencoSoggetti);
  AnagAziendaVO aaVO = null;
  for(int k = 0; k<elencoSoggetti.size();k++){
    PersonaFisicaVO pfVO = (PersonaFisicaVO)elencoSoggetti.elementAt(k);
    for(int j = 0; j < vectAziende.size(); j++) {
      aaVO = (AnagAziendaVO)vectAziende.elementAt(j);
      if(aaVO.getIdAzienda().compareTo(pfVO.getIdAzienda()) == 0) {
        break;
      }
    }
    ComuneVO comune = anagFacadeClient.getComuneByISTAT(pfVO.getResComune());
    String siglaProvincia="";

    htmpl.set("cognome", pfVO.getCognome());
    htmpl.set("nome", pfVO.getNome());
    htmpl.set("indirizzo", pfVO.getResIndirizzo());
    if(comune != null &&
   comune.getFlagEstero() != null &&
   comune.getFlagEstero().equals(SolmrConstants.FLAG_S)){
      htmpl.set("comune", comune.getDescom());
      htmpl.set("siglaProvincia", pfVO.getResCittaEstero());
    }else{
      htmpl.set("comune", pfVO.getDescResComune());
      siglaProvincia = anagFacadeClient.getSiglaProvinciaByIstatProvincia(comune.getIstatProvincia());
      htmpl.set("siglaProvincia", siglaProvincia);
      htmpl.set("CAP", pfVO.getResCAP());
    }
    String dataInizioRuolo = "";
    String dataFineRuolo = "";
    if(pfVO.getDataInizioRuolo()!=null){
      String gg = ""+DateUtils.extractDayFromDate(pfVO.getDataInizioRuolo());
      String mm = ""+DateUtils.extractMonthFromDate(pfVO.getDataInizioRuolo());
      if(gg.length()==1)
        dataInizioRuolo = "0"+gg+"/";
      else dataInizioRuolo = gg+"/";
      if(mm.length()==1)
        dataInizioRuolo += "0"+mm+"/";
      else dataInizioRuolo += mm+"/";
      dataInizioRuolo += DateUtils.extractYearFromDate(pfVO.getDataInizioRuolo());
    }
    htmpl.newBlock("rigaAziende");
    if(request.getParameter("selectedSoggetto")!=null)
      htmpl.set("rigaAziende.idSoggetto", ""+request.getParameter("selectedSoggetto"));
    htmpl.set("rigaAziende.idAnagAzienda", ""+aaVO.getIdAnagAzienda());
    htmpl.set("rigaAziende.idAzienda", ""+aaVO.getIdAzienda());
    htmpl.set("rigaAziende.aziende", ""+aaVO.getIdAnagAzienda());
    htmpl.set("rigaAziende.tipoRuolo",pfVO.getRuolo());
    htmpl.set("rigaAziende.dal", ""+dataInizioRuolo);
    if(pfVO.getDataFineRuolo()!=null){
      String gg = ""+DateUtils.extractDayFromDate(pfVO.getDataFineRuolo());
      String mm = ""+DateUtils.extractMonthFromDate(pfVO.getDataFineRuolo());
      if(gg.length()==1)
        dataFineRuolo = "0"+gg+"/";
      else dataFineRuolo = gg+"/";
      if(mm.length()==1)
        dataFineRuolo += "0"+mm+"/";
      else dataFineRuolo += mm+"/";
      dataFineRuolo += DateUtils.extractYearFromDate(pfVO.getDataFineRuolo());

      htmpl.set("rigaAziende.al", ""+dataFineRuolo);
    }
    else {
      if(Validator.isNotEmpty(aaVO.getDataCessazione())) {
        htmpl.set("rigaAziende.al", DateUtils.formatDate(aaVO.getDataCessazione()));
      }
      else {
        htmpl.set("rigaAziende.al", "");
      }
    }
    htmpl.set("rigaAziende.denominazione",aaVO.getDenominazione());
    htmpl.set("rigaAziende.CUAA",aaVO.getCUAA());
    htmpl.set("rigaAziende.PIVA",aaVO.getPartitaIVA());
    if(aaVO.getSedelegComune()!=null){
      htmpl.set("rigaAziende.sedeLegale",aaVO.getSedelegComune()+" ("+aaVO.getSedelegProv()+") "+aaVO.getSedelegIndirizzo());
    }
    else{
      htmpl.set("rigaAziende.sedeLegale",aaVO.getSedelegCittaEstero());
    }
  }
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%= htmpl.text()%>
