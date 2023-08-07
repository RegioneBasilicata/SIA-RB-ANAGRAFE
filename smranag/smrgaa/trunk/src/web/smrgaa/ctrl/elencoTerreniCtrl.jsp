<%@ page language="java" contentType="text/html" isErrorPage="true"%>
<%@page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO"%>
<%@page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO"%>

<%!public static final String VIEW    = "/view/elencoTerreniView.jsp";
  public static final String RICERCA = "../layout/ricercaTerreno.htm";
%>
<%
  String iridePageName = "elencoTerreniCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%
  FiltriRicercaTerrenoVO filtriRicercaTerrenoVO = (FiltriRicercaTerrenoVO) session
      .getAttribute("filtriRicercaTerrenoVO");
  if (filtriRicercaTerrenoVO == null)
  {
    response.sendRedirect(RICERCA);
    return;
  }
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  long ids[] = anagFacadeClient.ricercaIdParticelleTerreni(filtriRicercaTerrenoVO);
  if (ids != null)
  {
    String paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
    if (paginaCorrenteRichiesta==null)
    {
      String mantieniPagina=request.getParameter("mantieniPagina");
      if ("true".equals(mantieniPagina))
      {
        paginaCorrenteRichiesta=String.valueOf(filtriRicercaTerrenoVO.getPaginaCorrente());
      }
    }
    PaginazioneUtils pager = PaginazioneUtils.newInstance(ids,
        SolmrConstants.NUM_RIGHE_PER_PAGINA_RICERCA_TERRENI, filtriRicercaTerrenoVO.getPaginaCorrente(),
        paginaCorrenteRichiesta, "paginaCorrente");
    long idsForPage[] = pager.getIdForCurrentPage(true);
    if (idsForPage != null && idsForPage.length > 0)
    {
      RigaRicercaTerreniVO righe[] = anagFacadeClient
          .getRigheRicercaTerreniByIdParticellaRange(idsForPage);
      pager.setRighe(righe);
    }
    filtriRicercaTerrenoVO.setPaginaCorrente(pager.getPaginaCorrente());
    session.setAttribute("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO);
    request.setAttribute("pager", pager);
  }
%><jsp:forward page="<%=VIEW%>" />
