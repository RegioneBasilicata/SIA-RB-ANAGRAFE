<%@ page language="java"
  contentType="text/html"
  isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO"%>


<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/allegaFileNotifica.htm");
  
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  //imposto per fare in modo che sulla chiusura ricarichi la form padre
  String reloadParent=(String)request.getAttribute("reloadParent");
  if ("SI".equals(reloadParent))
  {
    htmpl.newBlock("blkReloadParent");
  }
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  
  AllegatoDocumentoVO allegatoDocumentoVO = (AllegatoDocumentoVO) request.getAttribute("allegatoDocumentoVO");
  htmpl.set("idDocumento", ""+allegatoDocumentoVO.getIdDocumento());
  //htmpl.set("nomeAllegato", allegatoDocumentoVO.getNomeLogico());
  
  Vector<AllegatoDocumentoVO> vElencoFileAllegati = (Vector<AllegatoDocumentoVO>) request.getAttribute("vElencoFileAllegati");
  if (vElencoFileAllegati != null &&
      vElencoFileAllegati.size() >0)
  {
    htmpl.newBlock("fileAllegatiBlk");

    for ( int i=0; i<vElencoFileAllegati.size(); i++)
    {
      allegatoDocumentoVO = (AllegatoDocumentoVO) vElencoFileAllegati.get(i);
      htmpl.newBlock("fileAllegatiBlk.fileBlk");
      
      htmpl.set("fileAllegatiBlk.fileBlk.idAllegato", allegatoDocumentoVO.getIdAllegato().toString());
      htmpl.set("fileAllegatiBlk.fileBlk.nome", allegatoDocumentoVO.getNomeLogico());
      htmpl.set("fileAllegatiBlk.fileBlk.titleAllegato", allegatoDocumentoVO.getNomeLogico()+" ("
        +allegatoDocumentoVO.getNomeFisico()+")");       
    }
  }
 
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  SolmrLogger.info(this, " - allegaFileNotificaView.jsp - FINE PAGINA");
%>
<%=htmpl.text()%>