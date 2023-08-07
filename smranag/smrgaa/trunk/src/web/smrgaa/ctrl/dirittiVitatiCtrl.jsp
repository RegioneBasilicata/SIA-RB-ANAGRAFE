<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smrvit.vitiserv.dto.diritto.DirittoVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.servizi.vitiserv.DirittoGaaVO" %>

<%

   String iridePageName = "sianTitoliCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

   String dirittiVitatiUrl = "/view/dirittiVitatiView.jsp";
   String excelUrl = "/servlet/ExcelDirittiVitatiServlet";

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
      
   boolean flagAttivi=true;
   
   if ("true".equals(request.getParameter("flagScaduti"))) flagAttivi=false;

   // Chiamo i servizi di viti per recuperare i diritti
   DirittoGaaVO[] diritti=null;
   try 
   {
     diritti = anagFacadeClient.getDiritti(anagAziendaVO.getIdAzienda().longValue(), flagAttivi, 1, DirittoVO.TIPO_RISULTATO_DEFAULT );
     if (diritti==null || diritti.length==0) // se non ho trovato i diritti comunico un messaggio all'utente
       request.setAttribute("messaggioErrore", it.csi.solmr.etc.anag.AnagErrors.ERR_NO_DIRITTI_IMPIANTO);
     else request.setAttribute("elencoDiritti", diritti);
     //Controllo se è stato selezionato lo scarico del file excel
     if("scarica".equals(request.getParameter("operazione")))
     {
       %>
         <jsp:forward page="<%=excelUrl%>" />
       <%
       return;
     }
   }
   catch(SolmrException se) 
   {
     
     request.setAttribute("messaggioErrore", it.csi.solmr.etc.anag.AnagErrors.ERR_DIRITTI_IMPIANTO);
     %>
        <jsp:forward page="<%= dirittiVitatiUrl %>"/>
     <%
     return;
   }

   %>
      <jsp:forward page="<%= dirittiVitatiUrl %>"/>
   <%
%>

