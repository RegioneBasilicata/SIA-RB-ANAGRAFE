<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

  String iridePageName = "ricercaVariazioniCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String ricercaVariazioniUrl = "/view/ricercaVariazioniView.jsp";
  String elencoVariazioniuRL = "/ctrl/elencoVariazioniCtrl.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  Vector elencoProvincePiemonte = null;

  ValidationErrors errors = new ValidationErrors();
  //Rimuovo le eventuali variazioni selezionate nelle precedenti ricerche
  session.removeAttribute("numVariazioniSelezionate");
  
  // Creo il VO del filtro e poi carico i parametri inseriti dall'utente
  FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO=(FiltriRicercaVariazioniAziendaliVO)session.getAttribute("filtriRicercaVariazioniAziendaliVO");
  if (filtriRicercaVariazioniAziendaliVO==null) 
    filtriRicercaVariazioniAziendaliVO = new FiltriRicercaVariazioniAziendaliVO();
  filtriRicercaVariazioniAziendaliVO.setIstatProvAmmComp(request.getParameter("competenza"));
  filtriRicercaVariazioniAziendaliVO.setIdTipoTipologiaVariazione(request.getParameter("catVariazione"));
  filtriRicercaVariazioniAziendaliVO.setIdVariazioneAziendale(request.getParameter("tipVariazione"));
  
  filtriRicercaVariazioniAziendaliVO.setStrDataVariazioneAl(request.getParameter("dataVariazioneAl"));
  filtriRicercaVariazioniAziendaliVO.setDataVariazioneAl(null);
  
  filtriRicercaVariazioniAziendaliVO.setStrDataVariazioneDal(request.getParameter("dataVariazioneDal"));
  filtriRicercaVariazioniAziendaliVO.setDataVariazioneDal(null);
  
  filtriRicercaVariazioniAziendaliVO.setCuaa(toUpper(request.getParameter("cuaa")));
  
  
  String istatComuneRicerca = "";
  String comuneRicerca = request.getParameter("comuneRicerca");
  String provinciaRicerca = request.getParameter("provinciaRicerca");
  
  
  if(Validator.isNotEmpty(comuneRicerca) || Validator.isNotEmpty(provinciaRicerca))
  {
    filtriRicercaVariazioniAziendaliVO.setComuneRicerca(comuneRicerca);
    filtriRicercaVariazioniAziendaliVO.setProvinciaRicerca(provinciaRicerca);    
    if(Validator.isNotEmpty(comuneRicerca) && Validator.isNotEmpty(provinciaRicerca))
    { 
      //caso in cui inseriti provincia e comune a mano
      try 
      {
        istatComuneRicerca = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneRicerca, provinciaRicerca);
        filtriRicercaVariazioniAziendaliVO.setIstatComuneRicerca(istatComuneRicerca);
      }
      catch(SolmrException se) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "comuneRicerca", se.getMessage());
      }        
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "comuneRicerca", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
      errors = ErrorUtils.setValidErrNoNull(errors, "provinciaRicerca", AnagErrors.ERRORE_NO_COMUNE_PROVINCIA);
    }
  }
  else
  {
    filtriRicercaVariazioniAziendaliVO.setIstatComuneRicerca(null);
    filtriRicercaVariazioniAziendaliVO.setComuneRicerca(null);
    filtriRicercaVariazioniAziendaliVO.setProvinciaRicerca(null);
  }
  
  
  String presaVisione=request.getParameter("presaVisione");
  if (Validator.isNotEmpty(presaVisione))
  {
    if (SolmrConstants.FLAG_S.equals(presaVisione))
      filtriRicercaVariazioniAziendaliVO.setPresaVisione(new Boolean(true));
    else filtriRicercaVariazioniAziendaliVO.setPresaVisione(new Boolean(false));  
  }
  else filtriRicercaVariazioniAziendaliVO.setPresaVisione(null);
  
  filtriRicercaVariazioniAziendaliVO.setVariazioniStoricizzate(request.getParameter("variazioniStoricizzate") != null);

  
  //Metto il filtro in session
  session.setAttribute("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO);
    
  
  //Caricamento dei filtri da visualizzare nella pagina
  try 
  {
    //Amministrazione di competenza(Le provincie TOBECONFIG)
    //Accedere alla tavola provincia selezionare il campo Descrizione filtrando per id_regione=01 + entry vuota.
    //Ordinamento: ordinare per descrizione crescente
    elencoProvincePiemonte = anagFacadeClient.getProvinceByRegione(ruoloUtenza.getIstatRegioneAttiva());
    // Metto il vettore in request
    request.setAttribute("elencoProvincePiemonte", elencoProvincePiemonte);
    
    
    //Tipo Variazione
    //Categoria di variazione
    //Selezionare tutte le tuple presenti sulla tavola db_tipo_tipologia_variazione.descrizione + entry vuota
    //Ordinamento: ordinare per descrizione crescente
    CodeDescription[] categoriaVariazione=anagFacadeClient.getCodeDescriptionsNew(SolmrConstants.DB_TIPO_TIPOLOGIA_VARIAZIONE,null,null,null);
    request.setAttribute("categoriaVariazione", categoriaVariazione);
    //Tipologia di variazione
    //Selezionare tutte le tuple presenti sulla tavola db_tipo_ variazione_azienda.descrizione legate alla categoria selezionata nella precedente combo. 
    //(Nel caso non fosse stata selezionata alcuna categoria nella combo prec. non caricare la combo) + entry vuota
    //Ordinamento: ordinare per descrizione crescente
    if (Validator.isNotEmpty(filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione()))
    {
      CodeDescription[] tipologiaVariazione=anagFacadeClient.getCodeDescriptionsNew(SolmrConstants.DB_TIPO_VARIAZIONE_AZIENDA,SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONE,filtriRicercaVariazioniAziendaliVO.getIdTipoTipologiaVariazione(),null);
      request.setAttribute("tipologiaVariazione", tipologiaVariazione);
    }
    
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(se.getMessage());
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ricercaVariazioniUrl).forward(request, response);
    return;
  }
  
  //L'utente ha ricaricato la pagina
  if ("RELOAD".equals(request.getParameter("operazione")))
  {
    request.getRequestDispatcher(ricercaVariazioniUrl).forward(request, response);
    return;
  }

  // L'utente ha premuto il pulsante ricerca
  if(request.getParameter("ricerca") != null) 
  {
    // Effettuo la validazione formale dei dati
    errors = filtriRicercaVariazioniAziendaliVO.validateRicercaVariazioni(errors);

    // Se ci sono errori formali li visualizzo
    if(errors != null && errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaVariazioniUrl).forward(request, response);
      return;
    }
    else
    {
      //imposto il numero di record da visualizzare in ogni pagina
      filtriRicercaVariazioniAziendaliVO.setPasso(Integer.parseInt(anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_RVAR)));
      //imposto il numero max di record per cui fare un file excel
      filtriRicercaVariazioniAziendaliVO.setNumMaxRecordExcel(Integer.parseInt(anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_RVMX)));
      filtriRicercaVariazioniAziendaliVO.setNumTotRecord(gaaFacadeClient.ricercaNumVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza));
      
      //Imposto i campi per l'ordinamento solo se è la prima volta che cre l'oggetto
      if (filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy()==null)
      {
        CampoVO campi[]=new CampoVO[7];
        int indice=0;
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.DATA_VARIAZIONE,"DATA_VARIAZIONE",false);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.PROV_COMP,"PROV_COMP",true);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.CUAA,"CUAA",true);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.DENOMINAZIONE,"DENOMINAZIONE",true);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.TIPOLOGIA_VARIAZIONE,"TIPOLOGIA_VARIAZIONE",true);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.VARIAZIONE,"VARIAZIONE",true);
        campi[indice++]=new CampoVO(FiltriRicercaVariazioniAziendaliVO.COMUNE,"COMUNE",true);
        //Salvo i campi nel filtro
        filtriRicercaVariazioniAziendaliVO.setCampiPerOrderBy(new OrdinamentoCampiVO(campi));
      }
      
      //Metto il filtro in session
      session.setAttribute("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO);
    }

    %>
      <jsp:forward page= "<%= elencoVariazioniuRL %>" />
    <%
  }
  // L'utente ha selezionato la funzione Variazioni
  else 
  {
    // Dal momento che si tratta di un nuovo nodo di partenza ripulisco la sessione
    // da tutti gli oggetti
    String noRemove = "";
    WebUtils.removeUselessFilter(session, noRemove);

    // Vado alla pagina di ricerca terreno
    %>
      <jsp:forward page= "<%= ricercaVariazioniUrl %>" />
    <%
  }


%><%!   
  public static String toUpper(String str)
  {
    return str==null?"":str.toUpperCase();
  }%>