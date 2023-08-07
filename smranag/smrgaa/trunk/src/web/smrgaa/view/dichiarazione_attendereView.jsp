<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%!
  private static final String NEXT_PAGE_OK="../layout/dichiarazioneOK.htm";
  private static final String NEXT_PAGE_ANOMALIA="../layout/dichiarazioneAnomalia.htm";
  private static final String NEXT_PAGE_ERRORE="../layout/dichiarazioneErrori.htm";
  private static final String NEXT_PAGE_ERRORE_PLSQL="../layout/errore.htm";
  private static final String PAGE_BACK="../layout/dichiarazioneConsistenza.htm";

%>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/dichiarazione_attendere.htm");

  String nextPage = NEXT_PAGE_ERRORE;
  String erroreHTML="",annoHTML="";

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  out.print(htmpl.text());
  out.flush();


  try
  {
    Integer anno=(Integer)(request.getAttribute("anno"));
    annoHTML = anno.toString();
    Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");
    String ris=client.controlliDichiarazionePLSQL(
      anagVO.getIdAzienda(),anno, idMotivoDichiarazione, ruoloUtenza.getIdUtente());
    if (ris.equals("N"))
    {
      //Posso permettere il salvataggio della dichiarazione
      nextPage=NEXT_PAGE_OK;
    }
    if (ris.equals("E"))
    {
      //Visualizzo gli errori e non permetto il proseguimento
      nextPage=NEXT_PAGE_ERRORE;
    }
    if (ris.equals("A"))
    {
      //Visualizzo le anomalie e permetto il salvataggio
      nextPage=NEXT_PAGE_ANOMALIA;
    }
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
