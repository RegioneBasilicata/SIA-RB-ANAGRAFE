<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="java.util.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.search.*"%>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%!
  public static final String LAYOUT="/layout/elencoVariazioni.htm";
%>

<%
  SolmrLogger.debug(this,"[ElencoVariazioniView:service] BEGIN.");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>

<%
  /********************************************************************************************************************************/
  /*                                                                                                                              */
  /*              La parte di codice seguente serve solo per caricare i filtri della ricerca                                      */                   
  /*                                                                                                                              */
  /********************************************************************************************************************************/

  FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO = (FiltriRicercaVariazioniAziendaliVO) session
      .getAttribute("filtriRicercaVariazioniAziendaliVO");
      
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");    

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  //etichetta del gruppo ruolo
  htmpl.bset("etichettaDescGruppoRuolo", ruoloUtenza.getTipoGruppoRuolo().getDescription());
  
  // Recupero il vettore contenente l'elenco delle province
  Vector elencoProvince = (Vector)request.getAttribute("elencoProvincePiemonte");
  if (elencoProvince!=null)
  {
    Iterator iteraProvince = elencoProvince.iterator();
    while(iteraProvince.hasNext()) 
    {
      ProvinciaVO provinciaVO = (ProvinciaVO)iteraProvince.next();
      htmpl.newBlock("blkProvince");
      htmpl.set("blkProvince.idCodice", provinciaVO.getIstatProvincia());
      htmpl.set("blkProvince.descrizione", provinciaVO.getSiglaProvincia());
      if(filtriRicercaVariazioniAziendaliVO != null &&  Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp())) 
          if(provinciaVO.getIstatProvincia().equalsIgnoreCase(filtriRicercaVariazioniAziendaliVO.getIstatProvAmmComp())) 
            htmpl.set("blkProvince.check", "selected = selected");
    }
  }
  
  // Recupero l'array contenente le Categoria di variazione
  CodeDescription[] categoriaVariazione=(CodeDescription[])request.getAttribute("categoriaVariazione");
  if (categoriaVariazione!=null)
  {
    for(int i=0;i<categoriaVariazione.length;i++) 
    {
      htmpl.newBlock("blkCatVariazioni");
      htmpl.set("blkCatVariazioni.idCodice", categoriaVariazione[i].getCode()+"");
      htmpl.set("blkCatVariazioni.descrizione", categoriaVariazione[i].getDescription());
      if(filtriRicercaVariazioniAziendaliVO != null &&  Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione())) 
        if (filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione().equals(""+categoriaVariazione[i].getCode()))  
          htmpl.set("blkCatVariazioni.check", "selected = selected");
    }
  }
  
  // Recupero l'array contenente le Categoria di variazione
  CodeDescription[] tipologiaVariazione=(CodeDescription[])request.getAttribute("tipologiaVariazione");
  if (tipologiaVariazione!=null)
  {
    for(int i=0;i<tipologiaVariazione.length;i++) 
    {
      htmpl.newBlock("blkTipVariazioni");
      htmpl.set("blkTipVariazioni.idCodice", tipologiaVariazione[i].getCode()+"");
      htmpl.set("blkTipVariazioni.descrizione", tipologiaVariazione[i].getDescription());
      if(filtriRicercaVariazioniAziendaliVO != null &&  Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale())) 
        if (filtriRicercaVariazioniAziendaliVO.getIdVariazioneAziendale().equals(""+tipologiaVariazione[i].getCode()))  
          htmpl.set("blkTipVariazioni.check", "selected = selected");
    }
  }
  
  if(filtriRicercaVariazioniAziendaliVO != null)
  {
    //Filtro Data relativa al campo db_dettaglio_variazione_azi.data_variazione
    htmpl.set("dataVariazioneAl", StringUtils.checkNull(filtriRicercaVariazioniAziendaliVO.getStrDataVariazioneAl()));
    //Filtro Data relativa al campo db_dettaglio_variazione_azi.data_variazione 
    htmpl.set("dataVariazioneDal", StringUtils.checkNull(filtriRicercaVariazioniAziendaliVO.getStrDataVariazioneDal()));
    
    //Filtro Codice CUAA relativo all'azienda attiva
    htmpl.set("cuaa", StringUtils.checkNull(filtriRicercaVariazioniAziendaliVO.getCuaa())); 
    
    //Filtro Variazioni storicizzate 
    if (filtriRicercaVariazioniAziendaliVO.isVariazioniStoricizzate())
      htmpl.set("checkedVariazioniStoricizzate", "checked");
      
    String provinciaRicerca = filtriRicercaVariazioniAziendaliVO.getProvinciaRicerca();
    if(Validator.isNotEmpty(provinciaRicerca))
      htmpl.set("provinciaRicerca",provinciaRicerca.toUpperCase());
    
    String comuneRicerca = filtriRicercaVariazioniAziendaliVO.getComuneRicerca();
    if(Validator.isNotEmpty(comuneRicerca))
      htmpl.set("comuneRicerca",comuneRicerca.toUpperCase());  
  }
  
  //Filtro per presa Visione  
  htmpl.newBlock("blkPresaVisione");
  htmpl.set("blkPresaVisione.idCodice", SolmrConstants.FLAG_S);
  htmpl.set("blkPresaVisione.descrizione", SolmrConstants.FLAG_SI);
  if (filtriRicercaVariazioniAziendaliVO!=null && filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null && filtriRicercaVariazioniAziendaliVO.getPresaVisione().booleanValue())
    htmpl.set("blkPresaVisione.check", "selected = selected");
  
  htmpl.newBlock("blkPresaVisione");
  htmpl.set("blkPresaVisione.idCodice", SolmrConstants.FLAG_N);
  htmpl.set("blkPresaVisione.descrizione", SolmrConstants.FLAG_NO);
  if (filtriRicercaVariazioniAziendaliVO!=null && filtriRicercaVariazioniAziendaliVO.getPresaVisione()!=null && !filtriRicercaVariazioniAziendaliVO.getPresaVisione().booleanValue())
    htmpl.set("blkPresaVisione.check", "selected = selected");


  /********************************************************************************************************************************/
  /*                                                                                                                              */
  /*                      Fine parte di codice per il caricaricamento dei filtri della ricerca                                    */                   
  /*                                                                                                                              */
  /*********************************************************************************************************************************/
 
 
  PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
  if (pager != null && pager.getTotaleRighe() > 0)
  {
    RigaRicercaVariazioniAziendaliVO righe[] = (RigaRicercaVariazioniAziendaliVO[]) pager
        .getRighe();
    htmpl.newBlock("blkElenco");
    
    if (Validator.isNotEmpty(request.getAttribute("noExcel")))
      htmpl.newBlock("blkElenco.blkTroppeVariazioni");
    if (Validator.isNotEmpty(request.getAttribute("noPresaVisione")))
      htmpl.newBlock("blkElenco.blkNessunaPresaVisione");  
      
    String troppePreseVisione=(String)request.getAttribute("troppePreseVisione");
    if (Validator.isNotEmpty(troppePreseVisione))
    {
      htmpl.newBlock("blkElenco.blkTroppePreseVisione");  
      htmpl.set("blkElenco.blkTroppePreseVisione.numeroElementiSelezionati",troppePreseVisione);
    }
    
    if (Validator.isNotEmpty(request.getAttribute("presaVisionePresente")))
      htmpl.newBlock("blkElenco.blkPresaVisionePresente");  
    
    //inizio gestione ordinamento
    if(filtriRicercaVariazioniAziendaliVO != null && filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy()!=null)
      filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy().writeOrdinamento(htmpl,"blkElenco.");
    //fine gestione ordinamento
    
    
    
    
    String paginaCorrente = request.getParameter("paginaCorrente");
    HashMap numVariazioniSelezionate = (HashMap)session.getAttribute("numVariazioniSelezionate");
    String[] idPresaVisione = null;
    if (!Validator.isNotEmpty(paginaCorrente))
      paginaCorrente = String.valueOf(pager.getPaginaCorrente());

    
    if (numVariazioniSelezionate!=null) idPresaVisione=(String[])numVariazioniSelezionate.get(paginaCorrente);
    
    pager.paginazione(htmpl,idPresaVisione);
        
    htmpl.set("blkElenco.paginaCorrente", paginaCorrente);
    htmpl.set("blkElenco.paginaCorrenteDup", paginaCorrente);
    ValidationErrors errors = pager.getErrors();
    if (errors != null)
    {
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
  }
  else
  {
    htmpl.newBlock("blkNoVariazioni");
  }
%><%=htmpl.text()%>
