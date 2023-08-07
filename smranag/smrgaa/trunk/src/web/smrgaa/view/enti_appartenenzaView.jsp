<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/enti_appartenenza.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 
  Vector elencoCollegate = (Vector)request.getAttribute("elencoCollegate");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  String storico = request.getParameter("storico");
  
  String[] elencoIdAzienda = request.getParameterValues("chkIdAzienda");
  
  if(Validator.isNotEmpty(storico))
  {
    htmpl.set("checkedStorico","checked=\"checked\"");
  }
  
  if(elencoCollegate != null && elencoCollegate.size() > 0) 
  {
    htmpl.newBlock("blkAziende");
    
    //solo se selezionato il check Storico visualizzo la data fine validità
    if(Validator.isNotEmpty(storico))
    {
      htmpl.newBlock("blkAziende.blkStorico");
    }
    
    
    
    Iterator iteraAziende = elencoCollegate.iterator();
    while(iteraAziende.hasNext()) 
    {
      AnagAziendaVO azVO = (AnagAziendaVO)iteraAziende.next();
      htmpl.newBlock("blkElencoAziende");
      
      String storicoChk = "false";
      if(azVO.getDataFineVal() !=null)
      {
        storicoChk = "true";
      }
      
      String chk = azVO.getIdAzienda().toString()+","+azVO.getIdAziendaCollegata().toString()+","+storicoChk;
      htmpl.set("blkAziende.blkElencoAziende.chkIdAzienda",chk);
      
      if(elencoIdAzienda != null && elencoIdAzienda.length > 0) {
        for(int i = 0; i < elencoIdAzienda.length; i++) {
          String idElementoSelezionato = elencoIdAzienda[i];
          if(idElementoSelezionato.compareTo(chk) == 0) {
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
        htmpl.set("blkAziende.blkElencoAziende.comune",azVO.getSedelegCittaEstero());
        htmpl.set("blkAziende.blkElencoAziende.indirizzo",azVO.getSedelegIndirizzo());
      }
      else
      {
        String comune = sedeLegale += azVO.getDescComune() +" ("+azVO.getSedelegProv()+") ";
        htmpl.set("blkAziende.blkElencoAziende.comune",comune);
        htmpl.set("blkAziende.blkElencoAziende.indirizzo", azVO.getSedelegIndirizzo());
        htmpl.set("blkAziende.blkElencoAziende.cap", azVO.getSedelegCAP());
      }
      
      htmpl.set("blkAziende.blkElencoAziende.dataIngresso",
        DateUtils.formatDateNotNull(azVO.getDataIngresso()));
      htmpl.set("blkAziende.blkElencoAziende.dataUscita",
        DateUtils.formatDateNotNull(azVO.getDataUscita()));
      htmpl.set("blkAziende.blkElencoAziende.dataInizioValidita",
        DateUtils.formatDateNotNull(azVO.getDataInizioVal()));
      
      //solo se selzionato il check Storico visualizzo la data fine validità  
      if(Validator.isNotEmpty(storico))
      {
        htmpl.newBlock("blkElencoStorico");
        htmpl.set("blkAziende.blkElencoAziende.blkElencoStorico.dataFineValidita",
        DateUtils.formatDateNotNull(azVO.getDataFineVal()));
      }
      
      
      String dateUlt = DateUtils.formatDateNotNull(azVO.getDataAggiornamento());
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkAziende.blkElencoAziende.ultimaModificaVw", dateUlt, azVO.getDescrizioneUtenteModifica(),
        azVO.getDescrizioneEnteUtenteModifica(),null);
      
    }
  }
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>
