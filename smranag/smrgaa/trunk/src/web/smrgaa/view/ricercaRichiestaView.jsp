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


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/ricercaRichiesta.htm");

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
  Vector<CodeDescription> vTipoRichiesta = (Vector<CodeDescription>)request.getAttribute("vTipoRichiesta");
  Vector<StatoRichiestaVO> vStatoRichiesta = (Vector<StatoRichiestaVO>)request.getAttribute("vStatoRichiesta");
  String messaggio = (String)request.getAttribute("messaggio");
  String regimeRicercaRichiesta = request.getParameter("regimeRicercaRichiesta");
  
  if(Validator.isEmpty(regimeRicercaRichiesta))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("pIva", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_1")))
  {
    htmpl.set("sedeLeg", "true");
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
  Long idStatoRichiesta = (Long)request.getAttribute("idStatoRichiestaSel");
  if(Validator.isEmpty(idStatoRichiesta))
  {
    idStatoRichiesta = (Long)session.getAttribute("idStatoRichiesta");
  }
  String cuaaRic = (String)request.getAttribute("cuaaRic");
  if(Validator.isEmpty(cuaaRic))
  {
    cuaaRic = (String)session.getAttribute("cuaaRic");
  }
  htmpl.set("cuaaRic", cuaaRic);
  String partitaIvaRic = (String)request.getAttribute("partitaIvaRic");
  if(Validator.isEmpty(partitaIvaRic))
  {
    partitaIvaRic = (String)session.getAttribute("partitaIvaRic");
  }
  htmpl.set("partitaIvaRic", partitaIvaRic);
  String denominazioneRic = (String)request.getAttribute("denominazioneRic");
  if(Validator.isEmpty(denominazioneRic))
  {
    denominazioneRic = (String)session.getAttribute("denominazioneRic");
  }
  htmpl.set("denominazioneRic", denominazioneRic);
  
  
  for(int j=0;j<vTipoRichiesta.size();j++)
  {
    htmpl.newBlock("blkTipoRichiesta");
    htmpl.set("blkTipoRichiesta.idTipoRichiesta", ""+vTipoRichiesta.get(j).getCode());
    htmpl.set("blkTipoRichiesta.descTipoRichiesta", vTipoRichiesta.get(j).getDescription());
    
    if(Validator.isNotEmpty(idTipoRichiesta) 
      && (idTipoRichiesta.compareTo(new Long(vTipoRichiesta.get(j).getCode())) ==0))
    {
      htmpl.set("blkTipoRichiesta.selected","selected", null);
    }
    else if(Validator.isEmpty(idTipoRichiesta) && (j==0))
    {
      htmpl.set("blkTipoRichiesta.selected","selected", null);
    }   
  }
  
  for(int j=0;j<vStatoRichiesta.size();j++)
  {
    htmpl.newBlock("blkStatoRichiesta");
    htmpl.set("blkStatoRichiesta.idStatoRichiesta", ""+vStatoRichiesta.get(j).getIdStatoRichiesta());
    htmpl.set("blkStatoRichiesta.descStatoRichiesta", vStatoRichiesta.get(j).getDescrizione());
    
    if(Validator.isNotEmpty(idStatoRichiesta) 
      && (idStatoRichiesta.compareTo(new Long(vStatoRichiesta.get(j).getIdStatoRichiesta())) ==0))
    {
      htmpl.set("blkStatoRichiesta.selected","selected", null);
    }
    else if(Validator.isEmpty(idStatoRichiesta)
      && SolmrConstants.RICHIESTA_STATO_TRASMESSA_PA.compareTo(new Long(vStatoRichiesta.get(j).getIdStatoRichiesta())) == 0)
    {
      htmpl.set("blkStatoRichiesta.selected","selected", null);
    }   
  }
  
  
  if(Validator.isNotEmpty(messaggio))
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggio);
  }
  

  if(rangeAziendaNuova!=null && rangeAziendaNuova.size()>0)
  {
    
    htmpl.newBlock("blkAnagrafica");
    
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
      htmpl.set("blkAnagrafica.rigaAnagrafica.denominazione", aziendaNuovaVO.getDenominazione());
      htmpl.set("blkAnagrafica.rigaAnagrafica.cuaa", aziendaNuovaVO.getCuaa());
      htmpl.set("blkAnagrafica.rigaAnagrafica.partitaIVA", aziendaNuovaVO.getPartitaIva());
      String comune = aziendaNuovaVO.getDescComune();
      if(Validator.isNotEmpty(aziendaNuovaVO.getSedelegCittaEstero()))
      {
        comune += " - "+aziendaNuovaVO.getSedelegCittaEstero();
      }
      htmpl.set("blkAnagrafica.rigaAnagrafica.comune", comune);
      if(Validator.isNotEmpty(aziendaNuovaVO.getSedelegProv()))
        htmpl.set("blkAnagrafica.rigaAnagrafica.prov", aziendaNuovaVO.getSedelegProv());
        
      htmpl.set("blkAnagrafica.rigaAnagrafica.indirizzo", aziendaNuovaVO.getSedelegIndirizzo());
      htmpl.set("blkAnagrafica.rigaAnagrafica.descTipoRichiesta", aziendaNuovaVO.getDescTipoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.descMotivoRichiesta", aziendaNuovaVO.getDescMotivoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.descStatoRichiesta", aziendaNuovaVO.getDescStatoRichiesta());
      htmpl.set("blkAnagrafica.rigaAnagrafica.dataAggiornamento", DateUtils.formatDateTimeNotNull(aziendaNuovaVO.getDataAggiornamento()));
      htmpl.set("blkAnagrafica.rigaAnagrafica.denominazUtente", aziendaNuovaVO.getDenomUtenteModifica());
      htmpl.set("blkAnagrafica.rigaAnagrafica.note", aziendaNuovaVO.getNote());
      String validazioneSucc = "No";
      if(Validator.isNotEmpty(aziendaNuovaVO.getDataValidazione()))
      {
        if(aziendaNuovaVO.getDataValidazione().compareTo(aziendaNuovaVO.getDataAggiornamentoIter()) >=0)
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
