<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.teramo.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

   String iridePageName = "registroStallaBDNCtrl.jsp";
   %>
      <%@include file = "/include/autorizzazione.inc" %>
   <%

   String sianAnagrafeZootecnicaUrl = "/view/registroStallaBDNView.jsp";
   String action = "../layout/registroStallaBDN.htm";
   String attenderePregoUrl = "/view/attenderePregoView.jsp";
   String erroreUrl = "/view/erroreView.jsp";
   String allevamentiUrl = "/view/allevamentiView.jsp";
   String excelUrl = "/servlet/ExcelRegistroStallaServlet";


   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   String messaggioErrore = null;
   String operazione = request.getParameter("operazione");

   try
   {

     AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
     RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
     ValidationError error = null;
     ValidationErrors errors = new ValidationErrors();

     // L'utente ha selezionato la voce di menù anagrafe zootecnica e io lo mando alla
     // pagina di attesa per il caricamento dati
     if("attenderePrego".equalsIgnoreCase(operazione)) {
       request.setAttribute("action", action);
       operazione = null;
       request.setAttribute("operazione", operazione);
       session.setAttribute("idAllevamentoRegistroStalla", request.getParameter("radiobutton"));
       %>
          <jsp:forward page= "<%= attenderePregoUrl %>" />
       <%
     }
     else
     {
      //Controllo se è stato selezionato lo scarico del file excel
      if("excel".equals(request.getParameter("operazione")))
      {
        %>
            <jsp:forward page="<%=excelUrl%>" />
        <%
        return;
      }
      else
      {


         // Rimuovo dalla sessione il precedente vettore selezionato
         session.removeAttribute("elencoRegistroStalla");



         String idAllevamento=(String)session.getAttribute("idAllevamentoRegistroStalla");
         session.removeAttribute("idAllevamentoRegistroStalla");
         long idAllevamentoL=0;
         it.csi.solmr.dto.anag.AllevamentoAnagVO all=null;
         String codSpecieTeramo=null;


         try
         {
           idAllevamentoL=Long.parseLong(idAllevamento);
         }
         catch(Exception e)
         {
           //Non è stato selezionato un allevamento nell\u2019elenco allevamenti.
           //il sistema visualizza il messaggio\u201Cselezionare una voce dall\u2019elenco!\u201D
           request.setAttribute("messaggioErrore", AnagErrors.ERR_NESSUNA_SELEZIONE_BDN);
           %>
              <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
           <%
           return;
         }




         all = anagFacadeClient.getAllevamento(new Long(idAllevamentoL));

         StringcodeDescription temp=null;
         if (all!=null && all.getIdSpecieAnimaleLong()!=null)
          temp=anagFacadeClient.getSianTipoSpecieByIdSpecieAnimale(all.getIdSpecieAnimaleLong().longValue());
         if (temp!=null) codSpecieTeramo=temp.getCode();

         if (all!=null && (all.getCodiceAziendaZootecnica()==null || "".equals(all.getCodiceAziendaZootecnica())))
         {
           //Il codice azienda zootecnica non è valorizzato (quindi questa funzionalità non ha significato)
           //il sistema visualizza il messaggio
           //\u201Ccodice zootecnico non valorizzato: impossibile consultare il Registro stalla\u201D
           request.setAttribute("messaggioErrore", AnagErrors.ERR_COD_AZ_ZOOTECNICO_NULL_BDN);
           %>
              <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
           <%
           return;
         }




           ElencoRegistroDiStallaVO elencoRegistroStalla = null;
           try
           {
             elencoRegistroStalla = anagFacadeClient
              .elencoRegistriStalla(all.getCodiceAziendaZootecnica(),
                codSpecieTeramo,anagAziendaVO.getCUAA(),"","", ProfileUtils.getSianUtente(ruoloUtenza));
           }
           catch(SolmrException se) {
             messaggioErrore = se.getMessage();
             request.setAttribute("messaggioErrore", messaggioErrore);
             %>
                <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
             <%
           }

           // Metto il nuovo vettore in session
           session.setAttribute("elencoRegistroStalla", elencoRegistroStalla);

           if (all!=null)
           {
             if (all.getTipoSpecieAnimaleAnagVO()!=null)
                session.setAttribute("descrizioneSpecie", all.getTipoSpecieAnimaleAnagVO().getDescrizione());
             request.setAttribute("capAll", all.getCap());
             if (all.getComuneVO()!=null)
                request.setAttribute("comuneAll", all.getComuneVO().getDescom());
             request.setAttribute("indirizzoAll", all.getIndirizzo());
             request.setAttribute("codiceAziendaZoo", all.getCodiceAziendaZootecnica());
           }

           // Se tutto va a buon fine vado alla pagina di elenco allevamenti provenienti dal SIAN
           %>
              <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
           <%
         }
     }
   }
   catch(Exception e)
   {
     e.printStackTrace();
     
     /*messaggioErrore = e.toString();
     request.setAttribute("messaggioErrore", messaggioErrore);*/
     
     request.setAttribute("messaggioErrore", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);     
     %>
        <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
     <%
   }

%>

