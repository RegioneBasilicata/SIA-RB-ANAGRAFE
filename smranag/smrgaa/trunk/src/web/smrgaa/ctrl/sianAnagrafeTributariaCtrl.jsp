<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
   String iridePageName = "sianAnagrafeTributariaCtrl.jsp";
   %>
      <%@include file = "/include/autorizzazione.inc" %>
   <%

   String sianAnagrafeTributariaUrl = "/view/sianAnagrafeTributariaView.jsp";
   String anagraficaUrl = "/view/anagraficaView.jsp";
   String action = "../layout/sianAnagrafeTributaria.htm";
   String attenderePregoUrl = "/view/attenderePregoView.jsp";

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
   String messaggioErrore = null;
   ValidationErrors errors = new ValidationErrors();
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   String operazione = request.getParameter("operazione");
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

   // L'utente ha selezionato la voce di menù anagrafe tributaria e io lo mando alla
   // pagina di attesa per il caricamento dati
   if("attenderePrego".equalsIgnoreCase(operazione)) {
     request.setAttribute("action", action);
     operazione = null;
     request.setAttribute("operazione", operazione);
     %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
     <%
   }
   else 
   {
     // Pulisco la sessione
     session.removeAttribute("anagTrib");

     // Se ha un CUAA valido cerco nell'anagrafe tributaria l'azienda
     // corrispondente
     SianAnagTributariaGaaVO anagTrib = null;
     try 
     {
       anagTrib = anagFacadeClient.ricercaAnagrafica(anagAziendaVO.getCUAA(), ProfileUtils.getSianUtente(ruoloUtenza));
     }
     catch(SolmrException se) 
     {
       messaggioErrore = se.getMessage();
       request.setAttribute("messaggioErrore", messaggioErrore);
       %>
          <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
       <%
     }
     session.setAttribute("anagTrib", anagTrib);
     
     
      Vector<AziendaAtecoSecVO> vAziendaAtecoSec = null;
      try
      {
        vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
      }
      catch(SolmrException se) 
      {      
        messaggioErrore = se.getMessage();
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
        <%
      }
      session.setAttribute("vAziendaAtecoSec", vAziendaAtecoSec);

     // Se l'azienda che ho trovato sul SIAN ha un CUAA uguale a un codice fiscale ricerco
     // il titolare rapp/legale: andiamo per codice fiscale e non per
     // forma giuridica perchè non sono ancora sicure le tipologie di
     // forma giuridica che devono essere sempre prese in considerazione
     PersonaFisicaVO personaFisicaVO = null;
     if(anagTrib!= null) 
     {
       if(anagTrib.getCodiceFiscale().length() == 16) 
       {

         //Controllo che la descrizione del comune di nascita sia presente sul
         //nostro DB per permettere di importarla.
         try
         {
           java.util.Vector temp=null;
           if (anagTrib.getComuneNascita()!=null &&
           !"".equals(anagTrib.getComuneNascita()))
            temp=anagFacadeClient.getComuniNonEstintiLikeProvAndCom(null,anagTrib.getComuneNascita(),null);
           if (temp!=null && temp.size()!=0)
           {
             ComuneVO comuneNascitaVO=(ComuneVO)temp.get(0);
             if (comuneNascitaVO!=null)
              request.setAttribute("istatNascita",comuneNascitaVO.getIstatComune());
           }
         }
         catch(Exception e){}
         
         
         //Controllo che la descrizione del comune di residenza sia presente sul
         //nostro DB per permettere di importarla.
         try
         {
           java.util.Vector temp=null;
           if (anagTrib.getComuneResidenza()!=null &&
           !"".equals(anagTrib.getComuneResidenza()))
            temp=anagFacadeClient.getComuniNonEstintiLikeProvAndCom(null,anagTrib.getComuneResidenza(),null);
           if (temp!=null && temp.size()!=0)
           {
             ComuneVO comuneResidenzaVO=(ComuneVO)temp.get(0);
             if (comuneResidenzaVO!=null)
              request.setAttribute("istatResidenza",comuneResidenzaVO.getIstatComune());
           }
         }
         catch(Exception e){}
       }

       //Controllo che la descrizione del comune della sede legale sia presente sul
       //nostro DB per permettere di importarla.
       try
       {
         java.util.Vector temp=null;
         if (anagTrib.getComuneSedeLegale()!=null &&
           !"".equals(anagTrib.getComuneSedeLegale()))
         temp=anagFacadeClient.getComuniByDescCom(anagTrib.getComuneSedeLegale());
         if (temp!=null && temp.size()!=0)
         {
           ComuneVO comuneResidenzaVO=(ComuneVO)temp.get(0);
           if (comuneResidenzaVO!=null)
            request.setAttribute("istatSedeLegale",comuneResidenzaVO.getIstatComune());
         }
       }
       catch(Exception e){}

       //Controllo che la descrizione del comune del domicilio fiscale sia presente sul
       //nostro DB per permettere di importarla.
       try
       {
         java.util.Vector temp=null;
         if (anagTrib.getComuneDomicilioFiscale()!=null &&
           !"".equals(anagTrib.getComuneDomicilioFiscale()))
         temp=anagFacadeClient.getComuniByDescCom(anagTrib.getComuneDomicilioFiscale());
         if (temp!=null && temp.size()!=0)
         {
           ComuneVO comuneResidenzaVO=(ComuneVO)temp.get(0);
           if (comuneResidenzaVO!=null)
            request.setAttribute("istatDomicilioFiscale",comuneResidenzaVO.getIstatComune());
         }
       }
       catch(Exception e){}


       try 
       {
         personaFisicaVO = anagFacadeClient.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
       }
       catch(SolmrException se) 
       {
         messaggioErrore = se.getMessage();
         request.setAttribute("messaggioErrore", messaggioErrore);
         %>
           <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
         <%
       }
       request.setAttribute("personaFisicaVO", personaFisicaVO);
     }

     %>
      <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
     <%
   }
%>

