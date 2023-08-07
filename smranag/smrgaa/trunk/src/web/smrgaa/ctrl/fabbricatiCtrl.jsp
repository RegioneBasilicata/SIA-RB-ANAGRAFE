<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	// Pulisco la sessione dai filtri di altre sezioni
  WebUtils.removeUselessFilter(session, null);

  String iridePageName = "fabbricatiCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String url = "../view/fabbricatiView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationError error = null;
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  if(errors == null) 
  {
    errors = new ValidationErrors();
  }

  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
	Long idPianoRiferimento = null;
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
    //idPianoRiferimento = new Long("-1");
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }
  
  String idUte = request.getParameter("idUte");
  Long idUteLg = null;
  if(Validator.isNotEmpty(idUte))
  {
    idUteLg = new Long(idUte);
  }
  // Recupero i valori relativi alle unità produttive
  Vector<UteVO> elencoUte = new Vector<UteVO>();
  try 
  {
    String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE, (String)SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
    elencoUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
    request.setAttribute("elencoUte", elencoUte);
  }
  catch(SolmrException se) 
  {
    errors.add("idUte", new ValidationError(AnagErrors.ERRORE_KO_UTE));
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(url).forward(request, response);
    return;
  }
  

	// Ho cliccato sulla voce di menù "fabbricati"
	FabbricatoVO[] elencoFabbricati = null;
  try 
  {
    String[] orderBy = {SolmrConstants.ORDER_BY_DESC_COMUNE, SolmrConstants.ORDER_BY_DESC_TIPO_TIPOLOGIA_FABBICATO_ASC ,SolmrConstants.ORDER_BY_DATA_INIZIO_VALIDITA_ASC, SolmrConstants.ORDER_BY_DATA_FINE_VALIDITA_ASC};
    elencoFabbricati = anagFacadeClient.getListFabbricatiAziendaByPianoRifererimento(anagAziendaVO.getIdAzienda(), idPianoRiferimento, idUteLg, orderBy);
    request.setAttribute("elencoFabbricati",elencoFabbricati);
  }
  catch(SolmrException se) 
  {
    errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_FABBRICATI));
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(url).forward(request, response);
    return;
  }

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
    request.setAttribute("statoAzienda", se);
  }
  	
  %>
    <jsp:forward page="<%= url %>"/>
  <%
%>
