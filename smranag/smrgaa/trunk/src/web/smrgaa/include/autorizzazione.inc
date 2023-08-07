<%
{
  java.util.HashMap iride2mappings=(java.util.HashMap)application.getAttribute("iride2mappings");
  it.csi.solmr.presentation.security.Autorizzazione autorizzazione=
  (it.csi.solmr.presentation.security.Autorizzazione) iride2mappings.get(iridePageName);
  request.setAttribute("iridePageNameForCU",iridePageName);
  if (autorizzazione==null)
  {
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] Autorizzazione è null");
    request.setAttribute("errorMessage",it.csi.solmr.etc.anag.AnagErrors.ERRORE_ABILITAZIONE_NO_ABILITAZIONE);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  it.csi.solmr.dto.iride2.Iride2AbilitazioniVO iride2AbilitazioniVO =
  (it.csi.solmr.dto.iride2.Iride2AbilitazioniVO) session.getAttribute("iride2AbilitazioniVO");
  it.csi.solmr.dto.profile.RuoloUtenza ruoloUtenzaAut =(it.csi.solmr.dto.profile.RuoloUtenza)session.getAttribute("ruoloUtenza");
  if (!autorizzazione.isUtenteAbilitato(iride2AbilitazioniVO,ruoloUtenzaAut.isReadWrite()))
  {
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] utente non abilitato da iride");
    request.setAttribute("errorMessage",it.csi.solmr.etc.anag.AnagErrors.ERRORE_ABILITAZIONE_NO_ABILITAZIONE);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
  String _errorMessageAut=autorizzazione.hasCompetenzaDato(request,response, ruoloUtenzaAut, new it.csi.solmr.client.anag.AnagFacadeClient());
  if (_errorMessageAut!=null)
  {
    it.csi.solmr.util.SolmrLogger.debug(this,"[autorizzazione.inc::service] utente abilitato da iride ma non dalla competenza del dato");
    request.setAttribute("errorMessage"," "+_errorMessageAut);
    %><jsp:forward page="<%=it.csi.solmr.etc.SolmrConstants.JSP_ERROR_PAGE%>" /><%
    return;
  }
}
%>
