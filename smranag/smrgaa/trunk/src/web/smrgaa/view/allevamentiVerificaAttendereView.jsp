<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  private static final String NEXT_PAGE="../layout/allevamentiVerifica.htm";
  private static final String NEXT_PAGE_ERRORE_PLSQL="../layout/errore.htm";
  private static final String PAGE_BACK="../layout/anagrafica.htm";

%>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/allevamentiVerificaAttendere.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  String nextPage = NEXT_PAGE;
  String erroreHTML="",annoHTML="";

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String daNextPage=null;

  htmpl.set("denominazione", anagVO.getDenominazione());
  htmpl.set("CUAA", anagVO.getCUAA());

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("denominazione",anagVO.getDenominazione());
  htmpl.set("oldCUAA",anagVO.getCUAA());
  htmpl.set("dataSituazioneAlStr",anagVO.getDataSituazioneAlStr());

  out.print(htmpl.text());
  out.flush();


  try
  {
    Integer anno=(Integer)(request.getAttribute("anno"));
    String ris=client.controlliVerificaPLSQL(
      anagVO.getIdAzienda(),anno,SolmrConstants.ID_GRUPPO_CONTROLLO_ALLEVAMENTI, ruoloUtenza.getIdUtente());
    
    nextPage=NEXT_PAGE;
    annoHTML=anno.toString();
  }
  catch(SolmrException e)
  {
    nextPage=NEXT_PAGE_ERRORE_PLSQL;
    erroreHTML=e.getMessage();
    e.printStackTrace();
  }
    %>
<script language="javascript1.2" type="text/javascript">
  window.document.form1.anno.value="<%=annoHTML%>";
  window.document.form1.errore.value="<%=erroreHTML%>";
  window.document.form1.pageBack.value="<%=PAGE_BACK%>";
  window.document.form1.action='<%=nextPage%>';
  window.document.form1.submit();
</script>
