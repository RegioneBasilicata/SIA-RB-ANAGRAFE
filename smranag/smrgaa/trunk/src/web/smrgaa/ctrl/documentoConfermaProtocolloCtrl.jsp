<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "documentoConfermaProtocolloCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

   String documentoConfermaProtocollaUrl = "/view/documentoConfermaProtocolloView.jsp";
   String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
   ValidationError error = null;
   ValidationErrors errors = new ValidationErrors();
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

   // Setto l'area di provenienza
   request.setAttribute("pageFrom", "documentale");

   // L'utente ha selezionato il tasto "annulla" dalla pagina di richiesta conferma dell'operazione
   if(request.getParameter("annulla") != null) 
   {
     // Pulisco la sessione
     session.removeAttribute("documentiDaProtocollare");

     // Torno alla pagina della ricerca/elenco dei documenti
     %>
        <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
     <%
   }

   // L'utente ha selezionato la funzione "protocolla" dal menù "Documenti"
   else 
   {
     // Recupero i documenti selezionati
     session.removeAttribute("documentiDaProtocollare");
     String[] documentiDaProtocollare = request.getParameterValues("idDocumento");

     // Effettuo la ricerca dei documenti selezionati
     int countProtocollo = 0;
     DocumentoVO documentoVO = null;
     for(int i = 0; i < documentiDaProtocollare.length; i++) 
     {
       try 
       {
         documentoVO = anagFacadeClient.getDettaglioDocumento(Long.decode((String)documentiDaProtocollare[i]), 
         false);
       }
       catch(SolmrException se) 
       {
         error = new ValidationError(se.getMessage());
         errors.add("error", error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
         return;
       }

       // Controllo che non siano stati selezionati documenti storicizzati o annullati
       if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) 
       {
         error = new ValidationError((String)AnagErrors.get("ERR_PROTOCOLLA_DOCUMENTO_KO"));
         errors.add("error", error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
         return;
       }
       
       
       //solo per quelli di fase 1
       if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesameNoModTotale()))
	     {
         error = new ValidationError(AnagErrors.ERRORE_DOC_PROTOC_IST_RIE_LAVORATA);
         errors.add("error", error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
         return;     
	     }
	     
	     if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
	     {
	       if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
               || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
         {
           if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
           {
             error = new ValidationError(AnagErrors.ERRORE_DOC_ANNUL_MOD_IST_RIE_CONVOCA);
             errors.add("error", error);
             request.setAttribute("errors", errors);
             request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
             return;           
           }           
         }
	      
	     }
       
       

       if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
       {
         countProtocollo++;
       }
     }
     
     
     
     
     
     
     
     

     // Se sono stati selezionati dei documenti con numero protocollo già valorizzato invio l'utente alla pagina di richiesta
     // conferma dell'operazione
     if(countProtocollo > 0) 
     {
       session.setAttribute("documentiDaProtocollare", documentiDaProtocollare);
       %>
          <jsp:forward page="<%= documentoConfermaProtocollaUrl %>" />
       <%
     }
     // Altrimenti effettuo subito l'operazione di riprotocollazione del documento
     else 
     {

       // Se l'utente non è un intermediario e non è un OPR GESTORE

       if(!ruoloUtenza.isUtenteIntermediario() && !ruoloUtenza.isUtenteOPRGestore()) 
       {
         // Contatto il servizio di comune per reperire la sigla amministrazione
         String siglaAmministrazione = null;
         try 
         {
           AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
           siglaAmministrazione = ammCompetenzaVO.getSiglaAmministrazione();
           ruoloUtenza.setSiglaAmministrazione(siglaAmministrazione);
           //profile.setSiglaAmministrazione(siglaAmministrazione);
         }
         catch(SolmrException se) 
         {
           error = new ValidationError((String)AnagErrors.get("ERR_INSERT_DOCUMENTO_KO_FOR_COMUNE"));
           errors.add("error", error);
           request.setAttribute("errors", errors);
           request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
           return;
         }
       }

       try 
       {
         anagFacadeClient.protocollaDocumenti(documentiDaProtocollare, anagAziendaVO.getIdAzienda(), ruoloUtenza);
       }
       catch(SolmrException se) 
       {
         error = new ValidationError(se.getMessage());
         errors.add("error", error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
         return;
       }

       // Pulisco la sessione
       session.removeAttribute("documentiDaProtocollare");

       // Vado alla pagina di ricerca/elenco dei documenti
       %>
          <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
       <%
     }
   }

%>
