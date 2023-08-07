<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaTrasferimentoUfficio.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String nomeIntermediario_1 = (String)request.getAttribute("nomeIntermediario_1");
  String nomeIntermediario_2 = (String)request.getAttribute("nomeIntermediario_2");
  Vector elencoAziende = (Vector)request.getAttribute("elencoAziende");
  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    htmpl.set("nomeIntermediario_1",nomeIntermediario_1);
    htmpl.set("nomeIntermediario_2",nomeIntermediario_2);
    
    if(elencoAziende != null && elencoAziende.size() > 0) 
    {
      htmpl.newBlock("blkAziende");
      Iterator iteraAziende = elencoAziende.iterator();
      while(iteraAziende.hasNext()) 
      {
        AnagAziendaVO azVO = (AnagAziendaVO)iteraAziende.next();
        htmpl.newBlock("blkElencoAziende");
         
        htmpl.set("blkAziende.blkElencoAziende.cuaa",azVO.getCUAA());
        htmpl.set("blkAziende.blkElencoAziende.partitaIva",azVO.getPartitaIVA());
        htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",azVO.getDenominazione());
        if(azVO.getIdAzienda() !=null)
        {
          htmpl.set("blkAziende.blkElencoAziende.idAzienda",azVO.getIdAzienda().toString());  
        }
        
        String sedeLegale = "";
        if(Validator.isNotEmpty(azVO.getSedelegCittaEstero()))
        {
          sedeLegale = azVO.getSedelegIndirizzo()+" - ";
          sedeLegale = azVO.getSedelegCittaEstero();
        }
        else
        {
          sedeLegale = azVO.getSedelegIndirizzo()+" - ";
          sedeLegale += azVO.getSedelegCAP()+" - ";
          sedeLegale += azVO.getDescComune() +" ("+azVO.getSedelegProv()+") ";
        }
        
        htmpl.set("blkAziende.blkElencoAziende.sedeLegale",sedeLegale);
      }
    }
  
  
  }

  
  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);
  out.print(htmpl.text());

%>