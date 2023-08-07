<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

 AnagFacadeClient client = new AnagFacadeClient();
 java.io.InputStream layout = application.getResourceAsStream("/layout/sedi.htm");
 Htmpl htmpl = new Htmpl(layout);

 UteVO uteVO = null;
 AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 Long idElemento = (Long)request.getAttribute("idElemento");

 /**
  * Se era stato selezionato il check dello storico devo ripresentarlo
  */

 Boolean storico = (Boolean)request.getAttribute("storico");

 if ("si".equals(request.getParameter("storico")))
 {
   htmpl.set("checkedStorico", "checked");
 }
 else if(storico != null) {
   if(storico.booleanValue()) {
     htmpl.set("checkedStorico", "checked");
   }
 }

 Vector v_ute = (Vector)session.getAttribute("v_ute");
 if(v_ute!=null && v_ute.size() > 0) {
   htmpl.newBlock("blkNoUte");
   Iterator iter = v_ute.iterator();
   while(iter.hasNext()){
     uteVO = (UteVO)iter.next();
     htmpl.newBlock("blkUte");
     htmpl.set("blkNoUte.blkUte.idUte", uteVO.getIdUte().toString());
     if(v_ute.size() == 0) {
       htmpl.set("blkNoUte.blkUte.checked", "checked");
     }
     else if(idElemento != null) {
       if(idElemento.compareTo(uteVO.getIdUte()) == 0) {
         htmpl.set("blkNoUte.blkUte.checked", "checked");
       }
     }
     htmpl.set("blkNoUte.blkUte.prov",uteVO.getProvincia());
     htmpl.set("blkNoUte.blkUte.com",uteVO.getComune());
     htmpl.set("blkNoUte.blkUte.ind",uteVO.getIndirizzo());
     htmpl.set("blkNoUte.blkUte.denominazione",uteVO.getDenominazione());
     if(uteVO.getDataInizioAttivita()!=null)
       htmpl.set("blkNoUte.blkUte.dataInizio", DateUtils.formatDate(uteVO.getDataInizioAttivita()));
     else
       htmpl.set("blkNoUte.blkUte.dataInizio","");
     if(uteVO.getDataFineAttivita()!=null)
       htmpl.set("blkNoUte.blkUte.dataFine", DateUtils.formatDate(uteVO.getDataFineAttivita()));
     else
       htmpl.set("blkNoUte.blkUte.dataFine","");
   }

   if(request.getAttribute("messaggio")!=null)
     htmpl.set("messaggio",(String)request.getAttribute("messaggio"));
   if(request.getAttribute("modifica")!=null)
     htmpl.set("blkNoErr.exc","Il record è stato modificato");
 }
 // Se non esistono unità produttive legate all'azienda agricola selezionata faccio comparire il messaggio
 // all'utente
 else {
   htmpl.newBlock("blkNoUnitaProduttive");
 }

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%= htmpl.text()%>
