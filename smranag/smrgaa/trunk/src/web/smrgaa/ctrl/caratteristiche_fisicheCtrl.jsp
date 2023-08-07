<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="javax.servlet.http.HttpSession.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String caratteristicheFisicheUrl = "/view/caratteristiche_fisicheView.jsp";
  String actionUrl = "../layout/motori_agricoli_incarico.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella sezione macchine agricole dettaglio. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  String iridePageName = "caratteristiche_fisicheCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
    
  <%

  try 
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    
    
    DittaUMAVO dittaUmaVO = anagFacadeClient.getDittaUmaByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("dittaUmaVO", dittaUmaVO);
    String idPossessoMacchinaStr = request.getParameter("idPossessoMacchina");
    Long idPossessoMacchina = null;
    if(Validator.isNotEmpty(idPossessoMacchinaStr))
      idPossessoMacchina = new Long(idPossessoMacchinaStr);
    else
      throw new SolmrException("idPossessoMacchina a null");
    
    PossessoMacchinaVO possessoMacchinaVO = gaaFacadeClient.getPosessoMacchinaFromId(idPossessoMacchina.longValue());
    request.setAttribute("possessoMacchinaVO", possessoMacchinaVO);
    
    if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO()))
    {
      UtenteAbilitazioni utenteAbilitazioni = anagFacadeClient.getUtenteAbilitazioniByIdUtenteLogin(possessoMacchinaVO.getMacchinaVO().getExtIdUtenteAggiornamento());
      RuoloUtenza ruoloUtenzaMod = new RuoloUtenzaPapua(utenteAbilitazioni); 
      
      request.setAttribute("ruoloUtenzaMod", ruoloUtenzaMod);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - caratteristiche_fisicheCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_DATI_UMA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

%>
<jsp:forward page="<%=caratteristicheFisicheUrl%>" />


 