<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>

<%
  String iridePageName = "stampeCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/stampe.htm");
  SolmrLogger.debug(this, "Found layout: " + layout);
  Htmpl htmpl = new Htmpl(layout);
%>
  <%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
  String tipoStampa = request.getParameter("tipoStampa");
  htmpl.set("tipoStampa", tipoStampa);
  String labelPianoLavorazione = request.getParameter("labelPianoLavorazione");
  htmpl.set("labelPianoLavorazione", labelPianoLavorazione);
  
  htmpl.set("descrizionePianoLavorazione", " - Piano di riferimento: " +labelPianoLavorazione);
  
  RichiestaTipoReportVO subReportElements[] = (RichiestaTipoReportVO[])request.getAttribute("subReportElements");
  Vector<TipoReportVO> vTipoReport = (Vector<TipoReportVO>)request.getAttribute("vTipoReport");
  
  if(subReportElements != null)
  {
  
     for(int j=0;j<vTipoReport.size();j++)
     {
       TipoReportVO tipoReportVO = vTipoReport.get(j);
       if(tipoReportVO.getCodiceReport().equalsIgnoreCase(tipoStampa))
       {
         htmpl.set("descrizioneReport",tipoReportVO.getDescrizione());
         break;
       }   
     }
     for(int i=0;i<subReportElements.length;i++)
     {
       RichiestaTipoReportVO richiestaTipoReportVO = subReportElements[i];
       
       if(richiestaTipoReportVO.isSelezionabile())
       {
         htmpl.newBlock("blkQuadri");
         //Se non sono al piano corrente devo disabilitare alcuni quadri
         //poiche sono obbligatori
         if(!"-1".equalsIgnoreCase(idDichiarazioneConsistenza))
         {
           if("D".equalsIgnoreCase(richiestaTipoReportVO.getObbligatorio()))
           {
             htmpl.set("blkQuadri.checkedQuadro", "checked");
             htmpl.set("blkQuadri.disableQuadro", "disabled=\"true\"");
             htmpl.set("blkQuadri.obbligatorio", "S");
           }
           else
           {
             htmpl.set("blkQuadri.obbligatorio", "N");
           }
         }
         htmpl.set("blkQuadri.codice", ""+richiestaTipoReportVO.getIdReportSubReport());
         htmpl.set("blkQuadri.descrizioneSelezione", richiestaTipoReportVO.getDescrizioneSelezione());
       }
     }  
  }

  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%=htmpl.text()%>
