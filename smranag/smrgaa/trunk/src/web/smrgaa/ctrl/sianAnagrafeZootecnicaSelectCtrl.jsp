<%@page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%
  String iridePageName = "sianAnagrafeZootecnicaSelectCtrl.jsp";
%>
<%@include file="/include/autorizzazione.inc"%>
<%
  String[] listIdAllevamentoSelected = request.getParameterValues("idAllevamento");
  Hashtable<BigDecimal,SianAllevamentiVO> elencoAllevamentiSian = (Hashtable<BigDecimal,SianAllevamentiVO>) session.getAttribute("elencoAllevamentiSian");
  ValidationError error = null;
  ValidationErrors errors = new ValidationErrors();
  String sianAnagrafeZootecnicaUrl = "../view/sianAnagrafeZootecnicaView.jsp";
  
  if (elencoAllevamentiSian != null)
  {
    //Per prima cosa rimuovo i vecchi valori degli elementi selezionati

    Enumeration<SianAllevamentiVO> enumeraAllevamenti = elencoAllevamentiSian.elements();
    while (enumeraAllevamenti.hasMoreElements())
      ((SianAllevamentiVO) enumeraAllevamenti.nextElement()).setSelect(false);
      
    if(listIdAllevamentoSelected == null || listIdAllevamentoSelected.length == 0) 
    {
      error = new ValidationError((String)AnagErrors.get("ERR_NO_ALLEVAMENTI_SIAN_SELECTED_FOR_IMPORT"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(sianAnagrafeZootecnicaUrl).forward(request, response);
      return;
    }   

    if (listIdAllevamentoSelected != null)
    {
      for (int i = 0; i < listIdAllevamentoSelected.length; i++)
      {
        BigDecimal idAllevamento = new BigDecimal((String) listIdAllevamentoSelected[i]);
        ((SianAllevamentiVO) elencoAllevamentiSian.get(idAllevamento)).setSelect(true);
      }
    }
    session.setAttribute("elencoAllevamentiSian",elencoAllevamentiSian);
  }
%>
<jsp:forward page="/view/sianAnagrafeZootecnicaSelectView.jsp" />

