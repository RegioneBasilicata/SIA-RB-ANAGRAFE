<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

 String url = "../layout/elenco.htm";
 java.io.InputStream layout = application.getResourceAsStream("/layout/sedi_det.htm");
 Htmpl htmpl = new Htmpl(layout);

 UteVO uteVO = (UteVO)session.getAttribute("uteVO");
 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 
 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 htmpl.set("denominazione", anagVO.getDenominazione());
 if(anagVO.getCUAA()!=null&&!anagVO.getCUAA().equals(""))
   htmpl.set("CUAA", anagVO.getCUAA()+" - ");
 htmpl.set("dataSituazioneAlStr", anagVO.getDataSituazioneAlStr());

 htmpl.set("idUte", uteVO.getIdUte().toString());
 htmpl.set("radioDettaglio", uteVO.getIdUte().toString());
 htmpl.set("provincia", uteVO.getProvincia());
 htmpl.set("comune", uteVO.getComune());
 htmpl.set("istat", uteVO.getIstat());
 htmpl.set("cap", uteVO.getCap());
 htmpl.set("indirizzo", uteVO.getIndirizzo());
 htmpl.set("denom", uteVO.getDenominazione());
 htmpl.set("ote", uteVO.getOte());
 if(Validator.isNotEmpty(uteVO.getAteco()))
 {
   htmpl.set("ateco", uteVO.getAteco()+" ("+uteVO.getCodeAteco()+")");
 }
 
 
 if((uteVO.getvUteAtecoSec() != null) && (uteVO.getvUteAtecoSec().size() > 0))
 {
   String elencoAttivitaAtecoSec = "";
   for(int i=0;i<uteVO.getvUteAtecoSec().size();i++)
   {
     UteAtecoSecondariVO uteAtecoSecondariVO = uteVO.getvUteAtecoSec().get(i);
     elencoAttivitaAtecoSec += uteAtecoSecondariVO.getDescAttivitaAteco()+" ("
     +uteAtecoSecondariVO.getCodiceAteco()+") <br/>";
   }
   htmpl.set("elencoAttivitaAtecoSec", elencoAttivitaAtecoSec, null);
 }
 
 
 htmpl.set("zonaAlt", uteVO.getZonaAltimetrica());
 htmpl.set("tel", uteVO.getTelefono());
 htmpl.set("fax", uteVO.getFax());
 htmpl.set("dataInizio", DateUtils.formatDate(uteVO.getDataInizioAttivita()));
 if(uteVO.getDataFineAttivita()!=null){
   htmpl.set("blkCessazione.dataFine", DateUtils.formatDate(uteVO.getDataFineAttivita()));
   htmpl.set("blkCessazione.causaleCess", uteVO.getCausaleCessazione());
 }
 
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(uteVO.getDataAggiornamento() !=null)
  {
    dateUlt = DateUtils.formatDate(uteVO.getDataAggiornamento());
  }
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, uteVO.getUtenteUltimaModifica(),
    uteVO.getEnteUltimaModifica(), uteVO.getMotivoModifica());
  //htmpl.set("ultima", uteVO.getUltimaModifica());
 htmpl.set("note", uteVO.getNote());
%>
<%= htmpl.text()%>
