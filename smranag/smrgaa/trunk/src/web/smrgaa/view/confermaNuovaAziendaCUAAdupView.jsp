<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaNuovaAziendaCUAAdup.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  String pageDup=(String)request.getAttribute("pageDup");
  String domanda=(String)request.getAttribute("domanda");
  String radiobuttonAzienda=(String)request.getAttribute("radiobuttonAzienda");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("page", pageDup);
  htmpl.set("domanda", domanda);
  htmpl.set("radiobuttonAzienda", radiobuttonAzienda);


  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagDup");
  // Se  il VO vuol dire che arrivo dalla ricerca dell'azienda e non dall'inserimento
  if(anagAziendaVO == null) {
    anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    if(anagAziendaVO != null){
      if(anagAziendaVO.getCCIAAnumeroREA()!=null && anagAziendaVO.getCCIAAnumeroREA().equals(new Long(0)))
        anagAziendaVO.setCCIAAnumeroREA(null);
    }
  }

  htmpl.set("strAttivitaOTE", anagAziendaVO.getTipoAttivitaOTE().getDescription());
  htmpl.set("strAttivitaATECO", anagAziendaVO.getTipoAttivitaATECO().getDescription());
  htmpl.set("strTipoAzienda", anagAziendaVO.getTipoTipologiaAzienda().getDescription());
  if(anagAziendaVO!=null && anagAziendaVO.getDataCessazione()!= null && !anagAziendaVO.getDataCessazione().equals(""))
    htmpl.set("dataCessazione", DateUtils.formatDate(anagAziendaVO.getDataCessazione()));
  if(anagAziendaVO!=null &&
     anagAziendaVO.getTipoFormaGiuridica()!= null &&
     anagAziendaVO.getTipoFormaGiuridica().getDescription()!= null &&
     !anagAziendaVO.getTipoFormaGiuridica().getDescription().equals(""))
    anagAziendaVO.setTipiFormaGiuridica(anagAziendaVO.getTipoFormaGiuridica().getDescription());
    
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(anagAziendaVO.getDataUltimaModifica() !=null)
  {
    dateUlt = DateUtils.formatDate(anagAziendaVO.getDataUltimaModifica());
  } 
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, 
    anagAziendaVO.getUtenteUltimaModifica(), anagAziendaVO.getEnteUltimaModifica(),
    anagAziendaVO.getMotivoModifica());
    
    HtmplUtil.setValues(htmpl, anagAziendaVO);

%>
<%= htmpl.text()%>
