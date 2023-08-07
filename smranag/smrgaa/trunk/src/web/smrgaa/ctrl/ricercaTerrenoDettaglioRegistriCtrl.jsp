<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@page import="it.csi.solmr.exception.SolmrException"%>
<%@page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.dto.anag.ParticellaCertificataVO"%>
<%@page import="it.csi.smranag.smrgaa.exception.ErrorPageException"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>



<%!public static final String VIEW = "/view/ricercaTerrenoDettaglioRegistriView.jsp";%>
<%
  String iridePageName = "ricercaTerrenoDettaglioRegistriCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%
  String ids[] = request.getParameterValues("idParticella");
  if (ids == null || ids.length > 1)
  {
    throw new ErrorPageException("Selezionare una e una sola particella");
  }
  long idParticella = new Long(ids[0]).longValue(); // l'elemento in posizione 0 deve esistere se la if precedente � stata superata
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  StoricoParticellaVO storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdParticella(idParticella);
  request.setAttribute("storicoParticellaVO", storicoParticellaVO);
  ParticellaCertificataVO particellaCertificataVO = anagFacadeClient.findParticellaCertificataByParametersNewElegFit(storicoParticellaVO
    .getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), storicoParticellaVO
    .getParticella(), storicoParticellaVO.getSubalterno(), true, null);
  Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
  if (particellaCertificataVO != null)
  {
    request.setAttribute("particellaCertificataVO", particellaCertificataVO);
        
    if(particellaCertificataVO.getIdParticellaCertificata() != null)
    {
      vRegistroPascoloVO = gaaFacadeClient
        .getRegistroPascoliPianoLavorazione(particellaCertificataVO.getIdParticellaCertificata().longValue());
    }
  }
  
  if(vRegistroPascoloVO == null)
  {
    vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliPianoLavorazioneChiaveCatastale(
      storicoParticellaVO.getIstatComune(), storicoParticellaVO.getFoglio(), 
      storicoParticellaVO.getParticella(), storicoParticellaVO.getSezione(), storicoParticellaVO.getSubalterno());
    
  }   
  request.setAttribute("vRegistroPascoloVO", vRegistroPascoloVO);
  
  
  
%><jsp:forward page="<%=VIEW%>" />
