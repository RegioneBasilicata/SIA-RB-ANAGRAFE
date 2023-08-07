<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

  String iridePageName = "aziendeCollegateCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - aziendeColleagateCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //Vector elencoCollegate = null;
  String operazione = request.getParameter("operazione");
  String regimeAzColl = request.getParameter("regimeAzColl");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String aziendeCollegateUrl = "/view/aziendeCollegateView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String aziendeCollegateModificaCtrl = "/ctrl/aziendeCollegateModificaCtrl.jsp";
  String aziendeCollegateEliminaCtrl = "/ctrl/aziendeCollegateEliminaCtrl.jsp";
  String aziendeCollegateInserisciCtrl = "/ctrl/aziendeCollegateInserisciCtrl.jsp";
  String entiAppartenenzaCtrl = "/ctrl/enti_appartenenzaCtrl.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";
  String excelUrl = "/servlet/ExcelElencoSociServlet";
  String excelUVUrl = "/servlet/ExcelElencoSociRiepilogoVinoDOPProvinciaServlet";
  ArrayList<CodeDescription> arAziendeLink = (ArrayList<CodeDescription>)session.getAttribute("elencoAziendeLink");
  //Identifica l'azienda selezionata per link,dettaglio,collegate
  String aziendaCorrente = (String)session.getAttribute("aziendaCollegataCorrente");
  boolean flagDettaglio = false;
  ValidationErrors errors = null;
  String storico = request.getParameter("storico");
  BigDecimal totSupAziendeCollegate[] = (BigDecimal[])session.getAttribute("totSupAziendeCollegate");
  
  
  //Lo setto la prima volta che entro e finchè rimane la stessa sessione non 
  //lo cancello più.
  //Usato nel caso entri con ruolo Azienda_agricola,titolare e rappresentante legale
  if(session.getAttribute("idAziendaElencoSoci") == null)
  {
    session.setAttribute("idAziendaElencoSoci",anagAziendaVO.getIdAzienda());  
  }
  
  
  
  //Controllo se è stato selezionato lo scarico del file excel
  if("excel".equals(request.getParameter("operazione")))
  {
    %>
        <jsp:forward page="<%=excelUrl%>" />
    <%
    return;
  }
  //Controllo se è stato selezionato lo scarico del file excel
  if("excelUV".equals(request.getParameter("operazione")))
  {
    %>
        <jsp:forward page="<%=excelUVUrl%>" />
    <%
    return;
  }
  
  WebUtils.removeUselessFilter(session, "aziendaCollegataCorrente, "+
    "elencoAziendeLink, filtriRicercaAziendeCollegateVO, totSupAziendeCollegate");
  
  
  //id azienda corrente
  String idAziendaCollegata = "";
  
  final String errMsg = "Impossibile procedere nella sezione "+anagAziendaVO.getLabelElencoAssociati()+"."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  
  //Se è la prima volta che entro (entro da altre sezioni!!) pulisco tutta la sessione...
  if(!Validator.isNotEmpty(regimeAzColl))
  {
    WebUtils.removeUselessFilter(session,null);
    arAziendeLink = null;
    aziendaCorrente = null;
    //totSupAziendeCollegate = gaaFacadeClient.getTOTSupCondottaAndSAU(anagAziendaVO
      //.getIdAzienda().longValue());
    //session.setAttribute("totSupAziendeCollegate", totSupAziendeCollegate);
  }
  
  
  //metto in sessione per i link l'azienda di partenza
  if(arAziendeLink == null)
  {
    arAziendeLink = new ArrayList<CodeDescription>();
    arAziendeLink.add(getCodeDescription(anagAziendaVO));
    session.setAttribute("elencoAziendeLink",arAziendeLink);
  }
  
  
  FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO 
    = (FiltriRicercaAziendeCollegateVO) session.getAttribute("filtriRicercaAziendeCollegateVO");
  if (filtriRicercaAziendeCollegateVO == null)
  {
    filtriRicercaAziendeCollegateVO = new FiltriRicercaAziendeCollegateVO(); 
  }
  
  session.setAttribute("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO);
    
  //Tutte le aziende collegate
  String[] elencoIdElem = request.getParameterValues("idElem");
  //aziende selezionate
  String[] elencoIdAziendaSel = request.getParameterValues("chkIdAzienda");
  
  //gestione dei checkbox con la paginazione!! ***************
  //la prima volta che entro non ho valorizzato nessun elemento (arrivo da terreniImporttaAsservimento!!!  
  if(Validator.isNotEmpty(elencoIdElem))
  {
    HashMap<String,String> hId = filtriRicercaAziendeCollegateVO.getHIdSelezionati();
    if(hId == null)
    {
      hId = new HashMap<String,String>();
    }
    
    HashMap<String,Long> hElimin = new HashMap<String,Long>();
    for(int i=0;i<elencoIdElem.length;i++)
    {
      hElimin.put(elencoIdElem[i], new Long(elencoIdElem[i]));
    }
    
    if(Validator.isNotEmpty(elencoIdAziendaSel))
    {  
      for(int i=0;i<elencoIdAziendaSel.length;i++)
      {
        String idAziendaCollegataSel = getIdAziendaCollegataFromChk(elencoIdAziendaSel[i]);
        hElimin.remove(idAziendaCollegataSel);
      }
      
      HashMap<String,String> hSel = new HashMap<String,String>();
      for(int i=0;i<elencoIdAziendaSel.length;i++)
      {
        String idAziendaCollegataSel = getIdAziendaCollegataFromChk(elencoIdAziendaSel[i]);
        hSel.put(idAziendaCollegataSel, elencoIdAziendaSel[i]);
      }
      
      if(hSel.size() > 0)
      {
        for(int i=0;i<elencoIdAziendaSel.length;i++)
        {
          String idAziendaCollegataSel = getIdAziendaCollegataFromChk(elencoIdAziendaSel[i]);
          hId.put(idAziendaCollegataSel, elencoIdAziendaSel[i]);
        }
      }
    }
  
    if(hElimin.size() > 0)
    {
      Iterator<String> iterator = hElimin.keySet().iterator();
      while(iterator.hasNext())
      {
        hId.remove(iterator.next());
      }
    }
    
    filtriRicercaAziendeCollegateVO.setHIdSelezionati(hId);
  }
  
  
  
  if(Validator.isNotEmpty(aziendaCorrente)) //clicco su aziende collegate e sono già nella sezione
  {
    idAziendaCollegata = aziendaCorrente;
  }
  else //inizializzazione
  {
    idAziendaCollegata = anagAziendaVO.getIdAzienda().toString();
  }
  
  //ho selezionato la voce dettaglio  
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("dettaglio"))
  {
    String[] elencoIdAzienda = request.getParameterValues("chkIdAzienda");
    // Controllo che venga selezionato un solo elemento dell'elenco
    if((elencoIdAzienda != null) && (elencoIdAzienda.length > 1)) 
    {
      request.setAttribute("messaggioErrore",AnagErrors.ERRORE_PIU_VOCI_SELEZIONATE_AC);      
    }
    else //Altrimenti redirigo sulla pagina di anagrafica.
    {
      if((elencoIdAzienda != null)) 
      {
        String censita = getCensitaFromChk(elencoIdAzienda[0]);
        //Se il valore è zero ho selezionato un'azienda non censita in anagrafe
        if(censita.equalsIgnoreCase("false"))
        {
          request.setAttribute("messaggioErrore",AnagErrors.ERRORE_AZIENDA_NON_CENSITA);
          flagDettaglio = true; 
        }
        else
        {
          idAziendaCollegata = getIdAziendaFromChk(elencoIdAzienda[0]);
          request.setAttribute("idAziendaAccesso", idAziendaCollegata);
                    
          %>
            <jsp:forward page = "<%=accessoAziendaURL%>" />
          <%
          return;
        }          
               
      }
      else //nessuna azienda selezionata
      {
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_NO_ELENCO_SOCI_SELEZIONATI);
        flagDettaglio = true;
      }
    }
  } //Ho selezionato la voce collegate
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("collegate"))
  { 
    
    //resetto il filtro per evitare problemi
    filtriRicercaAziendeCollegateVO = new FiltriRicercaAziendeCollegateVO();
    session.setAttribute("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO);
    String[] elencoIdAzienda = request.getParameterValues("chkIdAzienda");
    // Controllo che venga selezionato un solo elemento dell'elenco
    if((elencoIdAzienda != null) && (elencoIdAzienda.length > 1)) 
    {
      request.setAttribute("messaggioErrore",AnagErrors.ERRORE_PIU_VOCI_SELEZIONATE_AC);
    } //selezionato correttamente una sola azienda
    else if((elencoIdAzienda != null) && (elencoIdAzienda.length == 1)) 
    {
      String messaggioErrore = null;      
      String censita = getCensitaFromChk(elencoIdAzienda[0]);
        
      //Se il valore è zero ho selezionato un'azienda non censita in anagrafe
      if(censita.equalsIgnoreCase("false"))
      {
        messaggioErrore = AnagErrors.ERRORE_AZIENDA_NON_CENSITA; 
      }
      else
      {
        idAziendaCollegata = getIdAziendaFromChk(elencoIdAzienda[0]);
      }
      
      if(!Validator.isNotEmpty(messaggioErrore))
      {
        //boolean errStorico = false;
        String storicizzato = getStoricizzataFromChk(elencoIdAzienda[0]);
        if(storicizzato.equalsIgnoreCase("true"))
        {
          messaggioErrore = AnagErrors.ERRORE_COLLEGATE_AZIENDE_STORICIZZATE;
        }
      }
      
      if(Validator.isNotEmpty(messaggioErrore)) //l'elemento è storicizzato!!
      {
        request.setAttribute("messaggioErrore", messaggioErrore);
      }
      else //proseguo correttamente nella discesa
      {
        AnagAziendaVO aAziendaVO = null;
        idAziendaCollegata = getIdAziendaFromChk(elencoIdAzienda[0]);
        try {
          aAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAziendaCollegata));      
        }
        catch (SolmrException se) {
          SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+""+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        arAziendeLink.add(getCodeDescription(aAziendaVO));
      }
    }
    else //nessuna azienda selezionata
    {
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_NO_ELENCO_SOCI_SELEZIONATI);
        flagDettaglio = true;
    }
  }//Ho selezionato la voce linkate
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("linkate"))
  { 
    
    //resetto il filtro per evitare problemi
    filtriRicercaAziendeCollegateVO = new FiltriRicercaAziendeCollegateVO();
    session.setAttribute("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO);
    
    idAziendaCollegata = request.getParameter("idAzienda");
    ArrayList<CodeDescription> arTmp = null;
    for(int i=0;i<arAziendeLink.size();i++)
    {
      if(arTmp == null)
      {
        arTmp = new ArrayList<CodeDescription>();
      }
      CodeDescription code = (CodeDescription)arAziendeLink.get(i);
      arTmp.add(code);
      if(code.getSecondaryCode().equalsIgnoreCase(idAziendaCollegata))
      {
        break;
      }
    }
    
    session.setAttribute("elencoAziendeLink",arTmp);
  }//ho selezionato la voce modifica o elimina 
  else if(Validator.isNotEmpty(operazione) 
    && (operazione.equalsIgnoreCase("modifica") || operazione.equalsIgnoreCase("elimina")))
  {
  
    session.removeAttribute("elencoIdAziendeCollegate");
    HashMap<String,String> hSelezionati = filtriRicercaAziendeCollegateVO.getHIdSelezionati();
    if((hSelezionati !=null) && (hSelezionati.size() > 0)) 
    {
      // Controllo che vengano selezionati un numero di elementi inferiore al parametro RAZA
      // Recupero il parametro che mi indica il numero massimo di record selezionabili
      String parametroRaza = null;
      try {
        parametroRaza = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RAZA);;
      }
      catch(SolmrException se) {
      
        SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
        String messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RAZA;
        String messaggio = messaggioErrore +": "+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      Integer paramRazInt = new Integer(parametroRaza);
      if(hSelezionati.size() > paramRazInt.intValue()) 
      {
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_TROPPE_VOCI_SELEZIONATE);      
      }
      else
      {        
        boolean errStorico = false;
        
        Iterator<String> iterator = hSelezionati.keySet().iterator();
        while(iterator.hasNext())
        {
          String strTmp = (String)hSelezionati.get(iterator.next());
          String storicizzato = getStoricizzataFromChk(strTmp);
          if(storicizzato.equalsIgnoreCase("true"))
          {
            errStorico = true;
            break;
          }
        }
        
        if(errStorico) //almeno un elemento è storicizzato!!
        {
          request.setAttribute("messaggioErrore",AnagErrors.ERRORE_MODIFICA_AZIENDE_STORICIZZATE);
        }
        else //redirigo alla pagina di modifica
        {
          Vector<Long> vIdCollegate = new Vector<Long>();
          
          iterator = hSelezionati.keySet().iterator();
          while(iterator.hasNext())
          {
            String strTmp = (String)hSelezionati.get(iterator.next());
            String idAziendeCollegate = getIdAziendaCollegataFromChk(strTmp);
            vIdCollegate.add(new Long(idAziendeCollegate));
          }
          
          session.setAttribute("elencoIdAziendeCollegate",vIdCollegate);
          
          String aziendeCollegateUrlTmpCtrl = aziendeCollegateModificaCtrl;
          
          if(operazione.equalsIgnoreCase("elimina"))
          {
            aziendeCollegateUrlTmpCtrl = aziendeCollegateEliminaCtrl;
          }
          
          %>
            <jsp:forward page= "<%= aziendeCollegateUrlTmpCtrl %>" />
          <%
          return;
        }
        
      }     
    }
    else
    {
      request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_ELENCO_SOCI_SELEZIONATI);
      flagDettaglio = true;
    }
  }
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisci"))
  {
    %>
      <jsp:forward page= "<%= aziendeCollegateInserisciCtrl %>" />
    <%
    return;
  }
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("indietro"))
  {
    Vector<AnagAziendaVO> elencoPadriCollegate = null;
    try 
    {
      elencoPadriCollegate = anagFacadeClient.getEntiAppartenenzaByIdAzienda(anagAziendaVO.getIdAzienda(), true);
    }
    catch (SolmrException se) {
      SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    if(elencoPadriCollegate !=null)
    {
      ArrayList<Long> arrIdAz = null;
      for(int i=0;i<elencoPadriCollegate.size();i++)
      {
        AnagAziendaVO aVO = (AnagAziendaVO)elencoPadriCollegate.get(i);
        if(arrIdAz == null)
        {
          arrIdAz = new ArrayList<Long>();
        }
        if(!arrIdAz.contains(aVO.getIdAzienda()))
        {
          arrIdAz.add(aVO.getIdAzienda());
        }
      }
      
      if(arrIdAz.size() > 1) //Vado nell'elenco aziende padre perchè esitono piu aziende padre
      {    
        %>
          <jsp:forward page= "<%= entiAppartenenzaCtrl %>" />
        <%
        return;
      }
      else //vado direttamente nel dettaglio!!!!
      {
        Long idAziendaPadre = (Long)arrIdAz.get(0);
          
        request.setAttribute("idAziendaAccesso",idAziendaPadre.toString());
        %>
          <jsp:forward page = "<%=accessoAziendaURL%>" />
        <%
        return;          
      }
    }
    else
    {
      request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_PADRE);
      flagDettaglio = true;
    }
  }
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("aggiorna"))
  {
  
    filtriRicercaAziendeCollegateVO.setPaginaCorrente(1);
    filtriRicercaAziendeCollegateVO.setHIdSelezionati(null);
    
    if(Validator.isNotEmpty(storico))
    {
      filtriRicercaAziendeCollegateVO.setStorico(true);
    }
    else
    {
      filtriRicercaAziendeCollegateVO.setStorico(false);
    }
    
  
    // Recupero i parametri inseriti dall'utente
    String cuaaRicerca = request.getParameter("cuaaRicerca");
    String partitaIvaRicerca = request.getParameter("partitaIvaRicerca");
    String denominazioneRicerca = request.getParameter("denominazioneRicerca");
  
    // Setto i valori all'interno del VO
    if(Validator.isNotEmpty(cuaaRicerca))
    {
      String cuaaRicercaTmp = cuaaRicerca;
      cuaaRicercaTmp = cuaaRicercaTmp.trim();
      cuaaRicercaTmp = cuaaRicercaTmp.toUpperCase();
      filtriRicercaAziendeCollegateVO.setCuaaRicerca(cuaaRicercaTmp);
    }
    else
    {
      filtriRicercaAziendeCollegateVO.setCuaaRicerca(null);
    }
    if(Validator.isNotEmpty(partitaIvaRicerca))
    {
      String partitaIvaRicercaTmp = partitaIvaRicerca;
      partitaIvaRicercaTmp = partitaIvaRicercaTmp.trim();
      partitaIvaRicercaTmp = partitaIvaRicercaTmp.toUpperCase();
      filtriRicercaAziendeCollegateVO.setPartitaIvaRicerca(partitaIvaRicercaTmp);
    }
    else
    {
      filtriRicercaAziendeCollegateVO.setPartitaIvaRicerca(null);
    }
    if(Validator.isNotEmpty(denominazioneRicerca))
    {
      String denominazioneRicercaTmp = denominazioneRicerca;
      denominazioneRicercaTmp = denominazioneRicercaTmp.trim();
      denominazioneRicercaTmp = denominazioneRicercaTmp.toUpperCase();
      filtriRicercaAziendeCollegateVO.setDenominazioneRicerca(denominazioneRicercaTmp);
    }
    else
    {
      filtriRicercaAziendeCollegateVO.setDenominazioneRicerca(null);
    }
    
    
    String istatComuneRicerca = "";
    String comuneRicerca = request.getParameter("comuneRicerca");
    String provinciaRicerca = request.getParameter("provinciaRicerca");
    
    
    if(Validator.isNotEmpty(comuneRicerca) || Validator.isNotEmpty(provinciaRicerca))
    {
      filtriRicercaAziendeCollegateVO.setComuneRicerca(comuneRicerca);
      filtriRicercaAziendeCollegateVO.setProvinciaRicerca(provinciaRicerca);    
      if(Validator.isNotEmpty(comuneRicerca) && Validator.isNotEmpty(provinciaRicerca))
      { 
        //caso in cui inseriti provincia e comune a mano
        try 
        {
          istatComuneRicerca = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneRicerca, provinciaRicerca);
          filtriRicercaAziendeCollegateVO.setIstatComuneRicerca(istatComuneRicerca);
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
      filtriRicercaAziendeCollegateVO.setIstatComuneRicerca(null);
      filtriRicercaAziendeCollegateVO.setComuneRicerca(null);
      filtriRicercaAziendeCollegateVO.setProvinciaRicerca(null);
    }
    
     // Effettuo la validazione formale dei dati
    errors = filtriRicercaAziendeCollegateVO.validateRicerca(errors);
    
    if(errors !=null)
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page= "<%= aziendeCollegateUrl %>" />
      <%
      return;
    }
  
  }
  else //ho selezionato aggiorna, non fa nulla
  {}
  
  
  //Variabile usata in sessione nello scarico excel
  //session.setAttribute("flagStoricoAzColl",storico);
  
  //verifico se è l'azienda root e nel caso positivo la segno con livello 1
  //per poterla discriminare all'interno di AutorizzazioneCUStandard!!
  CodeDescription codePrim = (CodeDescription)arAziendeLink.get(0);
  if(codePrim.getSecondaryCode().equalsIgnoreCase(idAziendaCollegata))
  {
    session.setAttribute("levelAziendaCollegata", new Long(1));
  }
  else
  {
    session.setAttribute("levelAziendaCollegata", new Long(2));
  }
  
  
  
  
  filtriRicercaAziendeCollegateVO.setIdAzienda(new Long(idAziendaCollegata));
  session.setAttribute("aziendaCollegataCorrente",idAziendaCollegata);
  long ids[] = null;
  try
  {
    ids = gaaFacadeClient.ricercaIdAziendeCollegate(filtriRicercaAziendeCollegateVO);
    if (ids != null)
    {
      String paginaCorrenteRichiesta = "";
      //Setto a 1 la pagina sto facendo la ricerca
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("aggiorna"))
      {
        paginaCorrenteRichiesta = "1";
      }
      else
      {
        paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
        if (paginaCorrenteRichiesta==null)
        {
          String mantieniPagina=request.getParameter("mantieniPagina");
          if ("true".equals(mantieniPagina))
          {
            paginaCorrenteRichiesta=String.valueOf(filtriRicercaAziendeCollegateVO.getPaginaCorrente());
          }
        }
      }
      PaginazioneUtils pager = PaginazioneUtils.newInstance(ids,
          SolmrConstants.NUM_RIGHE_PER_PAGINA_AZIENDE_COLLEGATE, filtriRicercaAziendeCollegateVO.getPaginaCorrente(),
          paginaCorrenteRichiesta, "paginaCorrente");
      long idsForPage[] = pager.getIdForCurrentPage(true);
      if (idsForPage != null && idsForPage.length > 0)
      {
        RigaRicercaAziendeCollegateVO righe[] = gaaFacadeClient
            .getRigheRicercaAziendeCollegateByIdAziendaCollegata(idsForPage);
        
        //Cerco la delega
        long[] idsAssociate = null;
        Vector<Long> idsAssociateLg = null;
        HashMap<Long,DelegaVO> hDelega = null;
        boolean flagSenzaDelega = false;
        Vector<Long> idUtenteLg = null;
        long[] arrIdUtente = null;
        HashMap<Long,RuoloUtenza> hRuoloUtenza = new HashMap<Long,RuoloUtenza>();
        //RuoloUtenza[] arrRuoloUtenza = null;
        AmmCompetenzaVO[] arrAmmCompetenza = null;
        HashMap<String,AmmCompetenzaVO> hAmmCompetenza = new HashMap<String,AmmCompetenzaVO>();          
        for(int i=0;i<righe.length;i++)
        {
          if(righe[i].getIdAziendaAssociata() != null)
          {
            if(idsAssociateLg == null)
            {
              idsAssociateLg = new Vector<Long>();
            }
            
            if(idUtenteLg == null)
            {
              idUtenteLg = new Vector<Long>();
            }
            
            if(!idUtenteLg.contains(righe[i].getIdUtenteAggiornamento()))
            {
              idUtenteLg.add(righe[i].getIdUtenteAggiornamento());
            }
            
            idsAssociateLg.add(righe[i].getIdAziendaAssociata());
          }        
        }
        
        if(idsAssociateLg !=null)
        {
          idsAssociate = new long[idsAssociateLg.size()];
          for(int i=0;i<idsAssociateLg.size();i++)
          {
            idsAssociate[i] = ((Long)idsAssociateLg.get(i)).longValue();
          }
        }
        
        if(idUtenteLg !=null)
        {
          arrIdUtente = new long[idUtenteLg.size()];
          for(int i=0;i<idUtenteLg.size();i++)
          {
            arrIdUtente[i] = idUtenteLg.get(i).longValue();
          }
        }
        
        if(idsAssociate != null)
        {
          hDelega = gaaFacadeClient.getDelegaAndIntermediario(idsAssociate);
        }
        
        //Controllo se almeno una delle aziende associate non ha la delega
        if(hDelega != null)
        {
          for(int i=0;i<righe.length;i++)
          {
            if(righe[i].getIdAziendaAssociata() != null)
            {
              if(hDelega.get(righe[i].getIdAziendaAssociata()) == null)
              {
                flagSenzaDelega = true;
                break;
              }
            }
          }
        }
        else
        {
          flagSenzaDelega = true;
        }
        
        
        //Deve esistere almeno una azienda che abbia valorizzato l'id_Azienda_associata
        if(flagSenzaDelega && idsAssociateLg != null)
        {          
          UtenteAbilitazioni[] arrUtenteAbilitazioni = anagFacadeClient.getUtenteAbilitazioniByIdUtenteLoginRange(arrIdUtente);
	        if(arrUtenteAbilitazioni != null)
	        {
	          for(int i=0;i<arrUtenteAbilitazioni.length;i++)   
	          {  
	            RuoloUtenza ruoloUtenzaTmp = new RuoloUtenzaPapua(arrUtenteAbilitazioni[i]);
	            hRuoloUtenza.put(arrUtenteAbilitazioni[i].getIdUtenteLogin(), ruoloUtenza); 
	          }
	        }
          
          arrAmmCompetenza = anagFacadeClient.serviceGetListAmmCompetenza();          
          if(arrAmmCompetenza != null)
          {
            for(int i=0;i<arrAmmCompetenza.length;i++)
            {
              hAmmCompetenza.put(arrAmmCompetenza[i].getCodiceAmministrazione(), arrAmmCompetenza[i]);              
            }            
          }          
        }
        
        //asocio il check se selezionato!
        for(int i=0;i<righe.length;i++)
        {
          if(Validator.isNotEmpty(filtriRicercaAziendeCollegateVO.getHIdSelezionati())
            && (filtriRicercaAziendeCollegateVO.getHIdSelezionati().size() > 0)) 
          {
            String idElementoSelezionato = String.valueOf(righe[i].getIdAziendaCollegata());
            HashMap<String,String> hash = filtriRicercaAziendeCollegateVO.getHIdSelezionati();
            if(hash.containsKey(idElementoSelezionato)) 
            {
              righe[i].setChecked(true);
            }                
          }
          
          if((hDelega !=null) && (hDelega.get(righe[i].getIdAziendaAssociata()) != null))
          {
            DelegaVO delegaVO = (DelegaVO)hDelega.get(righe[i].getIdAziendaAssociata());
            
            String detentoreFascicolo ="";
            if(delegaVO.getCodiceAgea() != null)
            {            
              detentoreFascicolo += StringUtils.parseCodiceAgea(delegaVO.getCodiceAgea())+"<br>";
            }
            detentoreFascicolo += delegaVO.getDenomIntermediario();
            righe[i].setDetentoreFascicolo(detentoreFascicolo);
          }
          else
          {
            if((righe[i].getIdUtenteValidazione() != null) 
              && (hRuoloUtenza.get(righe[i].getIdUtenteValidazione()) != null))
            {
              RuoloUtenza ruoloUt = (RuoloUtenza)hRuoloUtenza.get(righe[i].getIdUtenteValidazione());
              if(ruoloUt.isUtentePA())
              {
                AmmCompetenzaVO ammComVO = (AmmCompetenzaVO)hAmmCompetenza.get(ruoloUt.getCodiceEnte());
                righe[i].setDetentoreFascicolo(ammComVO.getDescrizione());              
              }            
            }
          }
          
          righe[i].setStorico(filtriRicercaAziendeCollegateVO.isStorico());
          boolean paLocale = false;
          if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
          {
            paLocale = true;
          } 
          righe[i].setPaLocale(paLocale);
          
          
        }         
        pager.setRighe(righe);
      }
      filtriRicercaAziendeCollegateVO.setPaginaCorrente(pager.getPaginaCorrente());
      session.setAttribute("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO);
      request.setAttribute("pager", pager);
      
       //Se è la prima volta che entro (entro da altre sezioni!!) inserisco il totale
      // se il numero delle aziende associate è minore di ???
      if(!Validator.isNotEmpty(regimeAzColl))
      {
        if(ids.length < SolmrConstants.MAX_NUMERO_SOCI_PER_TOTALE)
        {
          totSupAziendeCollegate = gaaFacadeClient.getTOTSupCondottaAndSAU(anagAziendaVO
            .getIdAzienda().longValue());
          session.setAttribute("totSupAziendeCollegate", totSupAziendeCollegate);
        }
      }
      
    }
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  session.setAttribute("elencoIdAziendeCollegateExcel",ids);
  
  
  
  
  boolean flagPagErrore = true;
  if(ids == null) //nessun record trovato!!
  {
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("collegate"))
    {
      //Rimuovo l'ultimo elemento poichè non è stato trovato nessun figlio
      ArrayList<CodeDescription> arListTmp = (ArrayList<CodeDescription>)session.getAttribute("elencoAziendeLink");
      if((arListTmp !=null) && (arListTmp.size() > 1))
      { 
        int last = arListTmp.size()-1;
        arListTmp.remove(last);
        session.setAttribute("elencoAziendeLink",arListTmp);
        
        //Rimetto come azienda corrente l'ultima azienda che ha figli!
        CodeDescription code = (CodeDescription)arListTmp.get((arListTmp.size()-1));
        idAziendaCollegata = code.getSecondaryCode();
        
        //verifico se è l'azienda root e nel caso positivo la segno con livello 1
        //per poterla discriminare all'interno di AutorizzazioneCUStandard!!
        CodeDescription codePrimTmp = (CodeDescription)arAziendeLink.get(0);
        if(codePrimTmp.getSecondaryCode().equalsIgnoreCase(idAziendaCollegata))
        {
          session.setAttribute("levelAziendaCollegata", new Long(1));
        }
        else
        {
          session.setAttribute("levelAziendaCollegata", new Long(2));
        }
        
        try 
        {
          session.setAttribute("aziendaCollegataCorrente",idAziendaCollegata);
          
          filtriRicercaAziendeCollegateVO.setIdAzienda(new Long(idAziendaCollegata));
          ids = gaaFacadeClient.ricercaIdAziendeCollegate(filtriRicercaAziendeCollegateVO);
          if (ids != null)
          {
            String paginaCorrenteRichiesta = "";
            if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("aggiorna"))
            {
              paginaCorrenteRichiesta = "1";
            }
            else
            {
              paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
              if (paginaCorrenteRichiesta==null)
              {
                String mantieniPagina=request.getParameter("mantieniPagina");
                if ("true".equals(mantieniPagina))
                {
                  paginaCorrenteRichiesta=String.valueOf(filtriRicercaAziendeCollegateVO.getPaginaCorrente());
                }
              }
            }
            PaginazioneUtils pager = PaginazioneUtils.newInstance(ids,
                SolmrConstants.NUM_RIGHE_PER_PAGINA_AZIENDE_COLLEGATE, filtriRicercaAziendeCollegateVO.getPaginaCorrente(),
                paginaCorrenteRichiesta, "paginaCorrente");
            long idsForPage[] = pager.getIdForCurrentPage(true);
            if (idsForPage != null && idsForPage.length > 0)
            {
              RigaRicercaAziendeCollegateVO righe[] = gaaFacadeClient
                  .getRigheRicercaAziendeCollegateByIdAziendaCollegata(idsForPage);
                  
              //asocio il check se selezionato!
              for(int i=0;i<righe.length;i++)
              {
                if(Validator.isNotEmpty(filtriRicercaAziendeCollegateVO.getHIdSelezionati())
                  && (filtriRicercaAziendeCollegateVO.getHIdSelezionati().size() > 0)) 
                {
                  String idElementoSelezionato = String.valueOf(righe[i].getIdAziendaCollegata());
                  HashMap<String,String> hash = filtriRicercaAziendeCollegateVO.getHIdSelezionati();
                  if(hash.containsKey(idElementoSelezionato)) 
                  {
                    righe[i].setChecked(true);
                  }                
                }
                
                righe[i].setStorico(filtriRicercaAziendeCollegateVO.isStorico());
                boolean paLocale = false;
                if(ruoloUtenza.isUtentePALocale() || ruoloUtenza.isUtentePALocaleSuper())
			          {
			            paLocale = true;
			          } 
                righe[i].setPaLocale(paLocale);
              }    
                  
              pager.setRighe(righe);
            }
            filtriRicercaAziendeCollegateVO.setPaginaCorrente(pager.getPaginaCorrente());
            session.setAttribute("filtriRicercaAziendeCollegateVO", filtriRicercaAziendeCollegateVO);
            request.setAttribute("pager", pager);
            
            //Se è la prima volta che entro (entro da altre sezioni!!) inserisco il totale
            // se il numero delle aziende associate è minore di ???
            if(!Validator.isNotEmpty(regimeAzColl))
            {
              if(ids.length < SolmrConstants.MAX_NUMERO_SOCI_PER_TOTALE)
              {
                totSupAziendeCollegate = gaaFacadeClient.getTOTSupCondottaAndSAU(anagAziendaVO
                  .getIdAzienda().longValue());
                session.setAttribute("totSupAziendeCollegate", totSupAziendeCollegate);
              }
            }
            
            
          }
          
               
        }
        catch (SolmrException se) {
          SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+""+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
        
        //metto in sessione per excel
        //session.setAttribute("elencoCollegate", elencoCollegate);
        
        //se flagDettaglio == true significa che non è stata selezionata nessuna azienda
        //e scrive il messaggio d'errore corretto anche quando non esitono azienda collegate
        if(!flagDettaglio) 
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_COLLEGATE );
        }
      }
    }
    else
    {    
      //se flagDettaglio == true significa che non è stata selezionata nessuna azienda
      //e scrive il messaggio d'errore corretto anche quando non esitono azienda collegate
      if(!flagDettaglio) 
      {
        flagPagErrore = false;
        request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_COLLEGATE );
      }
    }
    
  }
  
  if(flagPagErrore && (request.getAttribute("messaggioErrore") !=null))
  {
    request.setAttribute("history","true");
    //request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;    
  } 
  
  
  

  

%>

<jsp:forward page= "<%= aziendeCollegateUrl %>" />

<%!
    CodeDescription getCodeDescription(AnagAziendaVO anagAziendaVO)
    {
      CodeDescription code = new CodeDescription();
      code.setCodeFlag(anagAziendaVO.getCUAA());
      code.setDescription(anagAziendaVO.getDenominazione());
      code.setSecondaryCode(anagAziendaVO.getIdAzienda().toString());
      
      return code;
      
    }
    
    //Ritorna l'idAzienda della tripla "idAzienda,idAziendaCollegata,storicizzata,censita" settata nel checkbox 
    String getIdAziendaFromChk(String chk)
    {      
      return getIdFromChk(chk,0);
    }
    
    //Ritorna l'idAziendaCollegata della tripla "idAzienda,idAziendaCollegata,storicizzata,censita" settata nel checkbox 
    String getIdAziendaCollegataFromChk(String chk)
    {      
      return getIdFromChk(chk,1);
    }
    
    //Ritorna la Stringa storicizzata coi valori true o false 
    //della tripla "idAzienda,idAziendaCollegata,storicizzata,censita" settata nel checkbox 
    String getStoricizzataFromChk(String chk)
    {      
      return getIdFromChk(chk,2);
    }
    
    //Ritorna la Stringa censita coi valori true o false 
    //della tripla "idAzienda,idAziendaCollegata,storicizzata,censita" settata nel checkbox 
    String getCensitaFromChk(String chk)
    {      
      return getIdFromChk(chk,3);
    }
    
    String getIdFromChk(String chk, int i)
    {
      ArrayList<String> arr = new ArrayList<String>();
      StringTokenizer strZ = new StringTokenizer(chk,",");
      while(strZ.hasMoreTokens())
      {
        arr.add(strZ.nextToken());
      }
      
      return (String)arr.get(i);
    }


 %>
