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
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/elencoRicercaRichiesta.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  int totalePagine;
  int pagCorrente;
  Integer currPage;

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Vector<Long> vectIdRichiestaAzienda = (Vector<Long>)session.getAttribute("listIdRicercaAzienda");
  Vector<AziendaNuovaVO> rangeAziendaNuova = (Vector<AziendaNuovaVO>)request.getAttribute("listRangeAziendaNuova");
  String messaggio = (String)request.getAttribute("messaggio");
  String regimeElencoRicercaRichiesta = request.getParameter("regimeElencoRicercaRichiesta");
  
  
  String arrivo = request.getParameter("arrivo");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
  if(Validator.isNotEmpty(anagAziendaVO))
  {
    htmpl.set("idAzienda", ""+anagAziendaVO.getIdAzienda());
  }
  else
  {
    htmpl.set("idAzienda", request.getParameter("idAzienda"));
  }
  
  
  
  
  if(Validator.isEmpty(regimeElencoRicercaRichiesta))
  {
    htmpl.set("primoIngressso", "true");
  }
  
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_2")))
  {
    htmpl.set("motRich", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_3")))
  {
    htmpl.set("statoRich", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_4")))
  {
    htmpl.set("dataAgg", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_5")))
  {
    htmpl.set("utIns", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_6")))
  {
    htmpl.set("not", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_7")))
  {
    htmpl.set("valSuc", "true");
  }
  
  
  Long idTipoRichiesta = (Long)request.getAttribute("idTipoRichiestaSel");
  if(Validator.isEmpty(idTipoRichiesta))
  {
    idTipoRichiesta = (Long)session.getAttribute("idTipoRichiesta");
  }
  
  
  
  if(Validator.isNotEmpty(messaggio))
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggio);
  }
  

  if(rangeAziendaNuova!=null && rangeAziendaNuova.size()>0)
  {
    
    htmpl.newBlock("blkAnagrafica");
    
    if(session.getAttribute("scritturaRichiesta") != null)
    {
      htmpl.newBlock("blkAnagrafica.blkButtonScrittura");
    }
    
    if(session.getAttribute("currPage")==null)
	    pagCorrente=1;
	  else
	    pagCorrente = ((Integer)session.getAttribute("currPage")).intValue();
	  if(vectIdRichiestaAzienda!=null){
	    totalePagine=vectIdRichiestaAzienda.size()/SolmrConstants.NUM_MAX_ROWS_PAG;
	    int resto = vectIdRichiestaAzienda.size()%SolmrConstants.NUM_MAX_ROWS_PAG;
	    if(resto!=0)
	      totalePagine+=1;
	    htmpl.set("blkAnagrafica.currPage",""+pagCorrente);
	    htmpl.set("blkAnagrafica.totPage",""+totalePagine);
	    htmpl.set("blkAnagrafica.numeroRecord",""+vectIdRichiestaAzienda.size());
	    currPage = new Integer(pagCorrente);
	    session.setAttribute("currPage",currPage);
	
	    if(pagCorrente>1)
	      htmpl.newBlock("blkAnagrafica.bottoneIndietro");
	    if(pagCorrente<totalePagine)
	      htmpl.newBlock("blkAnagrafica.bottoneAvanti");
	
	  }
  
  
    Long idRichiestaAziendaSel = (Long)request.getAttribute("idRichiestaAziendaSel");
    for(int i=0; i<rangeAziendaNuova.size();i++)
    {
      AziendaNuovaVO aziendaNuovaVO = rangeAziendaNuova.elementAt(i);
      htmpl.newBlock("blkAnagrafica.rigaAnagrafica");
      if(Validator.isNotEmpty(idRichiestaAziendaSel)) 
      {
        if(idRichiestaAziendaSel.compareTo(aziendaNuovaVO.getIdRichiestaAzienda()) == 0) {
          htmpl.set("blkAnagrafica.rigaAnagrafica.checked","checked=\"checked\"");
        }
      }
      htmpl.set("blkAnagrafica.rigaAnagrafica.idRichiestaAzienda", ""+aziendaNuovaVO.getIdRichiestaAzienda());
      htmpl.set("blkAnagrafica.rigaAnagrafica.idStatoAziendaHd", ""+aziendaNuovaVO.getIdStatoRichiesta());
    
      htmpl.set("blkAnagrafica.rigaAnagrafica.descTipoRichiesta", aziendaNuovaVO.getDescTipoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.descMotivoRichiesta", aziendaNuovaVO.getDescMotivoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.descStatoRichiesta", aziendaNuovaVO.getDescStatoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.dataAggiornamento", DateUtils.formatDateTimeNotNull(aziendaNuovaVO.getDataAggiornamento()));
      htmpl.set("blkAnagrafica.rigaAnagrafica.denominazUtente", aziendaNuovaVO.getDenomUtenteModifica());
      htmpl.set("blkAnagrafica.rigaAnagrafica.note", aziendaNuovaVO.getNoteRichiestaAzienda());
      String validazioneSucc = "No";
      if(Validator.isNotEmpty(aziendaNuovaVO.getDataValidazione()))
      {
        if(aziendaNuovaVO.getDataValidazione().compareTo(aziendaNuovaVO.getDataAggiornamentoIter()) >= 0)
        {
          validazioneSucc = "Si";
        }
      }
      htmpl.set("blkAnagrafica.rigaAnagrafica.validazioneSucc", validazioneSucc);
    }
  }
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
