<%@ page language="java" contentType="text/html" isErrorPage="true"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%!
  public static final String VIEW    = "/view/elencoVariazioniView.jsp";
  public static final String PRESA_VISIONE_URL    = "/ctrl/ricercaVariazioniVisioneCtrl.jsp";
  public static final String RICERCA = "../layout/ricercaVariazioni.htm";
  public static final String EXCEL_URL = "/servlet/ExcelVariazioniAziendaliServlet";
%>
<%
  String iridePageName = "elencoVariazioniCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%

  FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO = (FiltriRicercaVariazioniAziendaliVO) session
      .getAttribute("filtriRicercaVariazioniAziendaliVO");
  if (filtriRicercaVariazioniAziendaliVO == null)
  {
    response.sendRedirect(RICERCA);
    return;
  }
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  Vector elencoProvincePiemonte = null;
  ValidationErrors errors = new ValidationErrors();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  
  HashMap numVariazioniSelezionate = (HashMap)session.getAttribute("numVariazioniSelezionate");
  if(numVariazioniSelezionate == null) numVariazioniSelezionate = new HashMap();  
  
  String operazione=request.getParameter("operazione");
  String[] idPresaVisione = request.getParameterValues("idPresaVisione");
  String paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
  
  if (Validator.isNotEmpty(operazione))
  {
    //Controllo se è stato selezionato lo scarico del file excel
    if("excel".equals(request.getParameter("operazione")))
    {
      if (filtriRicercaVariazioniAziendaliVO.isExcelOK())
      {
        %>
            <jsp:forward page="<%=EXCEL_URL%>" />
        <%
        return;
      }
      else request.setAttribute("noExcel","noExcel");
    }
    else
    {
      //Controllo se è stato selezionata la presa visione
      if("PRESA_VISIONE".equals(request.getParameter("operazione")))
      {
        //memorizzo gli id selezionati dall'utente
        if (Validator.isNotEmpty(idPresaVisione) && filtriRicercaVariazioniAziendaliVO!=null) 
        {
            numVariazioniSelezionate.put(filtriRicercaVariazioniAziendaliVO.getPaginaCorrente()+"", idPresaVisione);
            session.setAttribute("numVariazioniSelezionate", numVariazioniSelezionate);
        }
        else 
        {
          numVariazioniSelezionate.remove(filtriRicercaVariazioniAziendaliVO.getPaginaCorrente()+"");
          session.setAttribute("numVariazioniSelezionate", numVariazioniSelezionate);
        }
        //Controllo che sia stato selezionato almeno un elemento
        if (!numVariazioniSelezionate.isEmpty())
        {
          //trasformo i dati contenuti nella tabella di hash in un array di stringhe 
          
          // Verifico che non siano state selezionate più particelle rispetto a quelle consentite
          Vector elencoIdPresaVisione=new Vector();
          Set elencoKeys = numVariazioniSelezionate.keySet();
          Iterator iteraKey = elencoKeys.iterator();
          while(iteraKey.hasNext()) 
          {
            String selezioni[]= (String[])numVariazioniSelezionate.get((String)iteraKey.next());
            if(selezioni != null) 
              for(int a = 0; a < selezioni.length; a++)
                elencoIdPresaVisione.add(new Long(selezioni[a]));
          }
          
          
          //Controllo che non siano stati selezionati più elementi di quanti indicati nel parametro ID_PARAMETRO_RVAP
          //imposto il numero di record da visualizzare in ogni pagina
          int maxRighe=Integer.parseInt(anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_RVAP));
           
          if (elencoIdPresaVisione.size()>maxRighe) 
            request.setAttribute("troppePreseVisione",""+maxRighe);
          else  
          {
            //Verifico che fra gli elementi selezionati non ci siano elementi in presa visione
            if (gaaFacadeClient.isPresaVisione(elencoIdPresaVisione, ruoloUtenza))               
              request.setAttribute("presaVisionePresente","presaVisionePresente");
            else
            { 
              request.setAttribute("elencoIdPresaVisione",elencoIdPresaVisione); 
              %>
                  <jsp:forward page="<%=PRESA_VISIONE_URL%>" />
              <%
              return;
            }
          }
        }
        else request.setAttribute("noPresaVisione","noPresaVisione");
      }
      else
      {   
        filtriRicercaVariazioniAziendaliVO.getCampiPerOrderBy().setCampoVOByDesc(operazione);
        session.removeAttribute("numVariazioniSelezionate");
      }
    }
  }
  else
  {
    //memorizzo gli id selezionati dall'utente
    if (Validator.isNotEmpty(idPresaVisione) && filtriRicercaVariazioniAziendaliVO!=null) 
    {
        numVariazioniSelezionate.put(filtriRicercaVariazioniAziendaliVO.getPaginaCorrente()+"", idPresaVisione);
        session.setAttribute("numVariazioniSelezionate", numVariazioniSelezionate);
    }
    else 
    {
      numVariazioniSelezionate.remove(filtriRicercaVariazioniAziendaliVO.getPaginaCorrente()+"");
      session.setAttribute("numVariazioniSelezionate", numVariazioniSelezionate);
    }
  }
  
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
    request.getRequestDispatcher(VIEW).forward(request, response);
    return;
  }
  
  
  
  if (filtriRicercaVariazioniAziendaliVO.getNumTotRecord() != 0)
  {
    if (paginaCorrenteRichiesta==null)
    {
      String mantieniPagina=request.getParameter("mantieniPagina");
      if ("true".equals(mantieniPagina))
      {
        paginaCorrenteRichiesta=String.valueOf(filtriRicercaVariazioniAziendaliVO.getPaginaCorrente());
      }
    }
    PaginazioneUtils pager = PaginazioneUtils.newInstance(filtriRicercaVariazioniAziendaliVO.getNumTotRecord(),
                             filtriRicercaVariazioniAziendaliVO.getPasso(),
                             filtriRicercaVariazioniAziendaliVO.getPaginaCorrente(),
                             paginaCorrenteRichiesta, "paginaCorrente");
        
    filtriRicercaVariazioniAziendaliVO.setPrimoElemento(pager.getPrimoElementoPaginaCorrente());    
    RigaRicercaVariazioniAziendaliVO righe[] = gaaFacadeClient.getRigheRicercaVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza,false);
    pager.setRighe(righe);
    filtriRicercaVariazioniAziendaliVO.setPaginaCorrente(pager.getPaginaCorrente());
    session.setAttribute("filtriRicercaVariazioniAziendaliVO", filtriRicercaVariazioniAziendaliVO);
    request.setAttribute("pager", pager);
  }
%><jsp:forward page="<%=VIEW%>" />
