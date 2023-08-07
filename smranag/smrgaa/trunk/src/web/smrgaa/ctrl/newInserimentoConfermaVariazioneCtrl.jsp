<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.pdf.modol.PdfNuovaRichiestaVariazioneModol" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoConfermaVariazioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova");

  String newInserimentoConfermaVariazioneUrl = "/view/newInserimentoConfermaVariazioneView.jsp";  
  String indietroIrrUrl = "/ctrl/newInserimentoRichVarIrrCtrl.jsp";
  String indietroRichSoggAssCaaUrl = "/ctrl/newInserimentoRichSoggAssCaaCtrl.jsp";
  String indietroRichSoggAssUrl = "/ctrl/newInserimentoRichSoggAssCtrl.jsp";
  String stampaNuovaIscrizioneUrl = "/servlet/StampaRichVarModolServlet";
  String stampaNuovaIscrizioneFirmaUrl = "/servlet/StampaRichVarModolServlet?param=firma";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova richiesta variazione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  String idTipoRichiesta = request.getParameter("idTipoRichiesta");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String idAzienda = request.getParameter("idAzienda");
  if(Validator.isEmpty(idAzienda))
    idAzienda = request.getParameter("idAziendaInd");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
    new Long(idTipoRichiesta).longValue());
  AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
  request.setAttribute("anagAziendaVO", anagAziendaVO);
  aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
  aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  String testoPerTutti = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_PAG_FINE_NAP_2);
  request.setAttribute("testoPerTutti", testoPerTutti+"<br><br>");
  
  
  Vector<StatoRichiestaVO> vStatoRichiesta = gaaFacadeClient.getListStatoRichiesta();
  request.setAttribute("vStatoRichiesta", vStatoRichiesta);
  
  
  
  if(Validator.isNotEmpty(operazione) && "indietro".equalsIgnoreCase(operazione)) 
  { 
    if(idTipoRichiesta.equalsIgnoreCase(new Integer(SolmrConstants.RICHIESTA_VAR_IRRORATRICI).toString()))
    {        
	    %>
	      <jsp:forward page="<%= indietroIrrUrl %>"/>
	    <%
	    return;
	  }
	  else if(idTipoRichiesta.equalsIgnoreCase(new Integer(SolmrConstants.RICHIESTA_VAR_SOCI).toString()))
	  {
	    Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
      if(vActor.contains(SolmrConstants.GESTORE_CAA))
      {
        %>
          <jsp:forward page="<%= indietroRichSoggAssCaaUrl %>"/>
        <%
        return;
      }
      else
      {
        %>
          <jsp:forward page="<%= indietroRichSoggAssUrl %>"/>
        <%
        return;
      }
	  }
  }
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if(Validator.isNotEmpty(operazione) && "stampa".equalsIgnoreCase(operazione)) 
  {
    %>
      <jsp:forward page="<%= stampaNuovaIscrizioneUrl %>"/>
    <%
    return;
  }
  
  if(Validator.isNotEmpty(operazione) && "stampaFirma".equalsIgnoreCase(operazione)) 
  {
    %>
      <jsp:forward page="<%= stampaNuovaIscrizioneFirmaUrl %>"/>
    <%
    return;
  }  
  
  
  if(Validator.isNotEmpty(operazione) && "stampaConferma".equalsIgnoreCase(operazione)) 
  {  
    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_STAMPATA);
    gaaFacadeClient.aggiornaStatoRichiestaValCess(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
      iterRichiestaAziendaVO);
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
      new Long(idTipoRichiesta).longValue());
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());
    
  }
 
  
  if(Validator.isNotEmpty(operazione) && "trasmPA".equalsIgnoreCase(operazione)) 
  {    
  
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
      new Long(idTipoRichiesta).longValue());
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    
    PdfNuovaRichiestaVariazioneModol pdfGenerator = new PdfNuovaRichiestaVariazioneModol();
    pdfGenerator.generaDocumento(request, response);
  
    IterRichiestaAziendaVO iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
    iterRichiestaAziendaVO.setIdStatoRichiesta(SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA);
    gaaFacadeClient.aggiornaStatoRichiestaValCess(aziendaNuovaVO, ruoloUtenza.getIdUtente().longValue(), 
      iterRichiestaAziendaVO);
    
    gaaFacadeClient.ribaltaMacchineNuovaRichiesta(aziendaNuovaVO.getIdRichiestaAzienda(), 
      ruoloUtenza.getIdUtente());
      
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
      new Long(idTipoRichiesta).longValue());
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());
    //request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    
   
   
  }
  
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  request.setAttribute("valCess", "valCess");
  
  

  
  

%>
  <jsp:forward page="<%= newInserimentoConfermaVariazioneUrl %>"/>
  
