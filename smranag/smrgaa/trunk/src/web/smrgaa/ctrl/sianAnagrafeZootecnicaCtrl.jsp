<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.etc.profile.AgriConstants" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.csi.wrapper.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

   String iridePageName = "sianAnagrafeZootecnicaCtrl.jsp";
   %>
      <%@include file = "/include/autorizzazione.inc" %>
   <%

   String sianAnagrafeZootecnicaUrl = "/view/sianAnagrafeZootecnicaView.jsp";
   String sianAnagrafeZootecnicaFormUrl = "../view/sianAnagrafeZootecnicaView.jsp";
   String action = "../layout/visuraBDN.htm";
   String attenderePregoUrl = "/view/attenderePregoView.jsp";
   String erroreUrl = "/view/erroreView.jsp";
   String allevamentiUrl = "/view/allevamentiView.jsp";


   GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   String messaggioErrore = null;
   String operazione = request.getParameter("operazione");
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   ValidationError error = null;
   ValidationErrors errors = new ValidationErrors();

   // L'utente ha selezionato la voce di menù anagrafe zootecnica e io lo mando alla
   // pagina di attesa per il caricamento dati
   if("attenderePrego".equalsIgnoreCase(operazione)) 
   {
     request.setAttribute("action", action);
     operazione = null;
     request.setAttribute("operazione", operazione);
     %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
     <%
   }
   else 
   {
     // Rimuovo dalla sessione il precedente vettore selezionato
     session.removeAttribute("elencoAllevamentiSian");

     // Se ha un CUAA valido cerco nell'anagrafe zootecnica
     // gli allevamenti corrispondenti
     Hashtable<BigDecimal, SianAllevamentiVO> elencoAllevamenti = null;
     try 
     {
       elencoAllevamenti = gaaFacadeClient.leggiAnagraficaAllevamenti(anagAziendaVO.getCUAA(), DateUtils.getCurrentDateString());
       if(Validator.isNotEmpty(elencoAllevamenti)
         && elencoAllevamenti.containsKey(new BigDecimal(-1)))
       {
         SianAllevamentiVO sianAllevamentiVO = elencoAllevamenti.get(new BigDecimal(-1));
         messaggioErrore = "SEGNALAZIONE BDN: "+sianAllevamentiVO.getCodErrore()+" - "+sianAllevamentiVO.getDescErrore();
	       request.setAttribute("messaggioErrore", messaggioErrore);
	       %>
	          <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
	       <%
       }
     }
     catch(Exception se) 
     {
       /*messaggioErrore = se.getMessage();
       request.setAttribute("messaggioErrore", messaggioErrore);*/
       request.setAttribute("messaggioErrore", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
       %>
          <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
       <%
     }

     // Se sono stati trovati degli allevamenti, controllo se sono già stati censiti su anagrafe
     boolean isPresenteInAnagrafe = false;
     Hashtable<BigDecimal, SianAllevamentiVO> elencoAllevamentiControlled = new Hashtable<BigDecimal, SianAllevamentiVO>();
     Enumeration<SianAllevamentiVO> enumeraAllevamenti = elencoAllevamenti.elements();
     while(enumeraAllevamenti.hasMoreElements()) {
       SianAllevamentiVO sianAllevamentiVO = (SianAllevamentiVO)enumeraAllevamenti.nextElement();
       try {
         isPresenteInAnagrafe = anagFacadeClient.isRecordSianInAnagrafe(anagAziendaVO, sianAllevamentiVO);
         sianAllevamentiVO.setIsPresenteInAnagrafe(isPresenteInAnagrafe);
         elencoAllevamentiControlled.put(sianAllevamentiVO.getAllevId(), sianAllevamentiVO);
       }
       catch(SolmrException se) {
         messaggioErrore = se.getMessage();
         request.setAttribute("messaggioErrore", messaggioErrore);
         %>
            <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
         <%
       }
     }

     // Metto il nuovo vettore in session
     session.setAttribute("elencoAllevamentiSian", elencoAllevamentiControlled);

     // Se tutto va a buon fine vado alla pagina di elenco allevamenti provenienti dal SIAN
     %>
        <jsp:forward page="<%= sianAnagrafeZootecnicaUrl %>"/>
     <%
   }

%>

