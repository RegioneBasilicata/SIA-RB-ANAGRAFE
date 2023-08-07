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


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/elenco.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  int totalePagine;
  int pagCorrente;
  Integer currPage;

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector vectIdAnagAzienda = (Vector)session.getAttribute("listIdAzienda");
  Vector vectRange = (Vector)session.getAttribute("listRange");

  if(session.getAttribute("currPage")==null)
    pagCorrente=1;
  else
    pagCorrente = ((Integer)session.getAttribute("currPage")).intValue();
  if(vectIdAnagAzienda!=null){
    totalePagine=vectIdAnagAzienda.size()/SolmrConstants.NUM_MAX_ROWS_PAG;
    int resto = vectIdAnagAzienda.size()%SolmrConstants.NUM_MAX_ROWS_PAG;
    if(resto!=0)
      totalePagine+=1;
    htmpl.set("currPage",""+pagCorrente);
    htmpl.set("totPage",""+totalePagine);
    htmpl.set("numeroRecord",""+vectIdAnagAzienda.size());
    currPage = new Integer(pagCorrente);
    session.setAttribute("currPage",currPage);

    if(pagCorrente>1)
      htmpl.newBlock("bottoneIndietro");
    if(pagCorrente<totalePagine)
      htmpl.newBlock("bottoneAvanti");

  }

  if(vectRange!=null && vectRange.size()>0){
    htmpl.set("dataSituazioneAlStr",((AnagAziendaVO)vectRange.elementAt(0)).getDataSituazioneAlStr());
    Long idAnagAziendaSelezionata = (Long)request.getAttribute("idAnagAziendaSelezionata");
    for(int i=0; i<vectRange.size();i++){
      AnagAziendaVO aaVO = (AnagAziendaVO)vectRange.elementAt(i);
      htmpl.newBlock("rigaAnagrafica");
      if(Validator.isNotEmpty(idAnagAziendaSelezionata)) {
        if(idAnagAziendaSelezionata.compareTo(aaVO.getIdAnagAzienda()) == 0) {
          htmpl.set("rigaAnagrafica.checked","checked=\"checked\"");
        }
      }
      htmpl.set("rigaAnagrafica.idAzienda",""+aaVO.getIdAnagAzienda());
      String denominazione = aaVO.getDenominazione();
      if(Validator.isNotEmpty(aaVO.getIntestazionePartitaIva()))
        denominazione += " - "+aaVO.getIntestazionePartitaIva();
      htmpl.set("rigaAnagrafica.denominazione", denominazione);
      htmpl.set("rigaAnagrafica.cuaa",aaVO.getCUAA());
      htmpl.set("rigaAnagrafica.partitaIVA",aaVO.getPartitaIVA());
      if(aaVO.getDescComune()!= null && !aaVO.getDescComune().equals(""))
        htmpl.set("rigaAnagrafica.comune",aaVO.getDescComune());
      else if(aaVO.getSedelegCittaEstero()!=null&&!aaVO.getSedelegCittaEstero().equals(""))
        htmpl.set("rigaAnagrafica.comune",aaVO.getSedelegCittaEstero());
      if(aaVO.getSedelegProv()!=null&&!aaVO.getSedelegProv().equals(""))
        htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegProv());
      else if(aaVO.getSedelegEstero()!=null && !aaVO.getSedelegEstero().equals(""))
        htmpl.set("rigaAnagrafica.prov",aaVO.getSedelegEstero());
      htmpl.set("rigaAnagrafica.indirizzo",aaVO.getSedelegIndirizzo());
      htmpl.set("rigaAnagrafica.inizioVal",DateUtils.formatDate(aaVO.getDataInizioVal()));
      if(aaVO.getDataFineVal()!=null) {
        htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataFineVal()));
      }
      else if(Validator.isNotEmpty(aaVO.getDataCessazione())) {
        htmpl.set("rigaAnagrafica.fineVal",DateUtils.formatDate(aaVO.getDataCessazione()));
      }
    }
  }
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
