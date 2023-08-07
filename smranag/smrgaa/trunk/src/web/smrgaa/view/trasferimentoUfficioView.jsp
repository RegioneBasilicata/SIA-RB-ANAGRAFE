<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/trasferimentoUfficio.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String intermediario_1 = request.getParameter("idIntermediario_1");
  String intermediario_2 = request.getParameter("idIntermediario_2");
  
  if(!Validator.isNotEmpty(intermediario_1))
  {
    intermediario_1 = (String)session.getAttribute("idIntermediario_1");
  }
  
  if(!Validator.isNotEmpty(intermediario_2))
  {
    intermediario_2 = (String)session.getAttribute("idIntermediario_2");
  }
  
  String txtCuaa = request.getParameter("cuaa");
  if(!Validator.isNotEmpty(txtCuaa))
  {
    txtCuaa = (String)session.getAttribute("cuaaTrasferimentoUfficio");
  }
  
  Vector elencoCAA = (Vector)request.getAttribute("elencoCAA");
  Vector elencoCAA_2 = (Vector)request.getAttribute("elencoCAA_2");
  Vector elencoAziende = (Vector)request.getAttribute("elencoAziende");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String paginaCorrente = (String)request.getAttribute("paginaCorrente");
  
  HashMap numUtilizziSelezionati = (HashMap)session.getAttribute("numUtilizziSelezionati");
  Vector elencoIdSelezionati = null;
  
  if(numUtilizziSelezionati != null && numUtilizziSelezionati.size() > 0) {
    elencoIdSelezionati = (Vector)numUtilizziSelezionati.get(paginaCorrente);
  }
  
  if(Validator.isNotEmpty(intermediario_1)) {
    request.setAttribute("idIntermediario_1", Long.decode(intermediario_1));
  }
  Long idIntermediario_1 = null;
  if(Validator.isNotEmpty(intermediario_1)) 
  {
    idIntermediario_1 = Long.decode(intermediario_1);
  }
  
  if(elencoCAA != null && elencoCAA.size() > 0) 
  {
    Iterator iteraCAA = elencoCAA.iterator();
    while(iteraCAA.hasNext()) 
    {
      htmpl.newBlock("blkElencoCAA_1");
      CodeDescription code = (CodeDescription)iteraCAA.next();
      htmpl.set("blkElencoCAA_1.codice_1", code.getCode().toString());
      htmpl.set("blkElencoCAA_1.descrizione_1", code.getDescription());
      
      if(Validator.isNotEmpty(idIntermediario_1) && idIntermediario_1.compareTo(Long.decode(code.getCode().toString())) == 0) {
        htmpl.set("blkElencoCAA_1.selected_1", "selected=\"selected\"");
      }
    }
  }
  
  if(Validator.isNotEmpty(intermediario_2)) {
    request.setAttribute("idIntermediario_2", Long.decode(intermediario_2));
  }
  Long idIntermediario_2 = null;
  if(Validator.isNotEmpty(intermediario_2)) 
  {
    idIntermediario_2 = Long.decode(intermediario_2);
  }
  
  if(elencoCAA_2 != null && elencoCAA_2.size() > 0) 
  {
    Iterator iteraCAA = elencoCAA_2.iterator();
    while(iteraCAA.hasNext()) 
    {
      htmpl.newBlock("blkElencoCAA_2");
      CodeDescription code = (CodeDescription)iteraCAA.next();
      htmpl.set("blkElencoCAA_2.codice_2", code.getCode().toString());
      htmpl.set("blkElencoCAA_2.descrizione_2", code.getDescription());
      
      if(Validator.isNotEmpty(idIntermediario_2) && idIntermediario_2.compareTo(Long.decode(code.getCode().toString())) == 0) {
        htmpl.set("blkElencoCAA_2.selected_2", "selected=\"selected\"");
      }
    }
  }
  
  htmpl.set("txtCuaa", txtCuaa);
  
  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    
    if(request.getParameter("operazione") !=null) 
    {
      if(elencoAziende != null && elencoAziende.size() > 0) 
      {
        htmpl.newBlock("blkAziende");
        
        elencoAziende = (Vector)HtmplUtil.paginazionePerGruppiTrasfAzienda(Integer.parseInt(paginaCorrente), elencoAziende, htmpl, request, errors, "blkAziende", application);
        
        Iterator iteraAziende = elencoAziende.iterator();
        while(iteraAziende.hasNext()) 
        {
          AnagAziendaVO azVO = (AnagAziendaVO)iteraAziende.next();
          htmpl.newBlock("blkElencoAziende");
          
          htmpl.set("blkAziende.blkElencoAziende.idAzienda",azVO.getIdAzienda().toString());
          
          if(elencoIdSelezionati != null && elencoIdSelezionati.size() > 0) {
            for(int i = 0; i < elencoIdSelezionati.size(); i++) {
              Long idElementoSelezionato = (Long)elencoIdSelezionati.elementAt(i);
              if(idElementoSelezionato.compareTo(azVO.getIdAzienda()) == 0) {
                htmpl.set("blkAziende.blkElencoAziende.checked", "checked=\"checked\"");
              } 
            }
          }
          htmpl.set("blkAziende.blkElencoAziende.cuaa",azVO.getCUAA());
          htmpl.set("blkAziende.blkElencoAziende.partitaIva",azVO.getPartitaIVA());
          htmpl.set("blkAziende.blkElencoAziende.nomeAzienda",azVO.getDenominazione());
          
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
  }

  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());

%>