<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "inserisciTerrenoParticellareCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String inserisciTerrenoParticellareCtrlUrl = "/ctrl/inserisciTerrenoCtrl.jsp";
  String inserisciTerrenoParticellareUrl = "/view/inserisciTerrenoParticellareView.jsp";
  
  String actionUrl = "../layout/inserisciTerreno.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserimento particelle."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  StoricoParticellaVO[] elencoParticelleEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
  ValidationErrors errors = new ValidationErrors();
  ValidationError error = null;
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
  Long idEvento = null;
  if(Validator.isNotEmpty(request.getParameter("idEvento"))) {
    idEvento = Long.decode(request.getParameter("idEvento"));
    request.setAttribute("idEvento", idEvento);
  }
  String provvisoria = request.getParameter("provvisoria");
  request.setAttribute("provvisoria", provvisoria);
  
  
  
  try 
  {
    ProvinciaVO provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
    if(provinciaControlloVO != null) 
    {
      if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
      {
        request.setAttribute("isPiemontese", "true");
      }
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - inserisciTerrenoParticellareCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  } 
  
  //caricamento tipi area 
  try 
  {
    Vector<TipoAreaVO> vTipoArea = gaaFacadeClient.getAllValoriTipoArea();
    request.setAttribute("vTipoArea", vTipoArea);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareInserisciCondUsoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }     
  
  
  
  
  double superficieGrafica = 0;
  if(Validator.isNotEmpty(storicoParticellaVO.getSuperficieGrafica()))
  {
    superficieGrafica = Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.'));
  }
  else
  {
    storicoParticellaVO.setSuperficieGrafica(StringUtils.parseSuperficieField(new Double(superficieGrafica).toString()));
  }  
  double superficieCatastale = 0;
  if(Validator.isNotEmpty(storicoParticellaVO.getSupCatastale()))
  {
    superficieCatastale = Double.parseDouble(storicoParticellaVO.getSupCatastale().replace(',', '.'));
  }
  else
  {
    storicoParticellaVO.setSupCatastale(StringUtils.parseSuperficieField(new Double(superficieCatastale).toString()));
  }
  
  if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
  {  
	  if(((superficieGrafica == 0) && (superficieCatastale == 0))
	    || (idEvento.compareTo(SolmrConstants.INSERIMENTO_ACCORPAMENTO) == 0)
	    || (idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0))  
	  {
		  //Parametro che mi permette di mantenere la possibilità di modificare la sup catastale
		  //Rmisso se l'inserimento è andato a buon finer 
		  //oppure nell'inserimentoTerreno solo se entro dal menu (prima volta!)
		  session.setAttribute("flagObbligoCatastale","true");
	  }
	}
  //Se la particella è null
  //Sono nel caso della provvisoria quindi non devo modifcare la sup catastale..
  // e deve essere 0
  else
  {
    storicoParticellaVO.setSupCatastale(SolmrConstants.DEFAULT_SUPERFICIE);
  }
  

  // Recupero i casi particolari
  String particellaObbligatoria = null;
  if(Validator.isEmpty(storicoParticellaVO.getParticella()))
  {
    particellaObbligatoria = "N";
  }
  Vector<CasoParticolareVO> elencoCasiParticolari = null;
  try 
  {
    elencoCasiParticolari = gaaFacadeClient.getCasiParticolari(particellaObbligatoria);
    request.setAttribute("elencoCasiParticolari", elencoCasiParticolari);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_CASI_PARTICOLARI);
    errors.add("idCasoParticolare", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
    return;
  }

  // Ricerco i tipi irrigazione
  TipoIrrigazioneVO[] elencoIrrigazione = null;
  try 
  {
    elencoIrrigazione = anagFacadeClient.getListTipoIrrigazione(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, true);
    request.setAttribute("elencoIrrigazione",elencoIrrigazione);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_IRRIGAZIONE);
    errors.add("idIrrigazione", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
    return;
  }
  
  
  // Ricerco i tipi rotazione colturale
  TipoRotazioneColturaleVO[] elencoRotazioneColturale = null;
  try 
  {
    elencoRotazioneColturale = anagFacadeClient.getListTipoRotazioneColturale(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, null);
    request.setAttribute("elencoRotazioneColturale",elencoRotazioneColturale);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_ROTAZIONE_COLTURALE);
    errors.add("idRotazioneColturale", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
    return;
  }
  
  // Ricerco i tipi terrazzamenti
  TipoTerrazzamentoVO[] elencoTerrazzamenti = null;
  try 
  {
    elencoTerrazzamenti = anagFacadeClient.getListTipoTerrazzamento(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, null);
    request.setAttribute("elencoTerrazzamenti",elencoTerrazzamenti);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_TERRAZZAMENTI);
    errors.add("idTerrazzamento", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
    return;
  }
  
  // Ricerco i tipi metodo irriguo
  Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = null;
  try 
  {
    vMetodoIrriguo = gaaFacadeClient.getElencoMetodoIrriguo();
    request.setAttribute("vMetodoIrriguo", vMetodoIrriguo);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_METODO_IRRIGUO);
    errors.add("idMetodoIrriguo", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
    return;
  }
  
  
  


  // L'utente ha selezionato il pulsante indietro
  if("indietro".equals(request.getParameter("operazionePart")))
  {
    
    storicoParticellaVO.setSupCatastale(request.getParameter("supCatastale"));
    
    //salvataggio valori tipo area...
    Vector<TipoAreaVO> vTipoArea = storicoParticellaVO.getvValoriTipoArea();
    String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
    String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
    int numValore = 0;
    for(int j=0;j<vTipoArea.size();j++)
    {
      if(j%2 == 0)
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea1[numValore]);
      }
      else
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea2[numValore]);
        numValore++;
      }
     
    }
    
    if(Validator.isNotEmpty(request.getParameter("idCasoParticolare"))) {
      storicoParticellaVO.setIdCasoParticolare(Long.decode(request.getParameter("idCasoParticolare")));
    }
    else {
      storicoParticellaVO.setIdCasoParticolare(null);
    }
    if(Validator.isNotEmpty(request.getParameter("flagCaptazionePozzi"))) {
      storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_S);
    }
    else {
      storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
    }
    if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) {
      storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
    }
    else {
      storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
    }
    if(Validator.isNotEmpty(request.getParameter("idIrrigazione"))) {
      storicoParticellaVO.setIdIrrigazione(Long.decode(request.getParameter("idIrrigazione")));
    }
    else {
      storicoParticellaVO.setIdIrrigazione(null);
    }
    if(Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) 
    {
      storicoParticellaVO.setIdMetodoIrriguo(Long.decode(request.getParameter("idMetodoIrriguo")));
    }
    else 
    {
      storicoParticellaVO.setIdMetodoIrriguo(null);
    }
    if(Validator.isNotEmpty(request.getParameter("idRotazioneColturale"))) {
      storicoParticellaVO.setIdRotazioneColturale(Long.decode(request.getParameter("idRotazioneColturale")));
    }
    else {
      storicoParticellaVO.setIdRotazioneColturale(null);
    }
    if(Validator.isNotEmpty(request.getParameter("idTerrazzamento"))) {
      storicoParticellaVO.setIdTerrazzamento(Long.decode(request.getParameter("idTerrazzamento")));
    }
    else {
      storicoParticellaVO.setIdTerrazzamento(null);
    }
    // Rimetto in sessione l'oggetto aggiornato
    session.setAttribute("storicoParticellaVO", storicoParticellaVO);

    // Torno al primo step di inserimento della particella
    %>
      <jsp:forward page="<%=inserisciTerrenoParticellareCtrlUrl%>" >
        <jsp:param name="indietro" value="indietro"/>
      </jsp:forward>
    <%
    return;
  }
 
  // L'utente ha premuto il tasto salva
  if("salva".equals(request.getParameter("operazionePart"))
    || Validator.isNotEmpty(request.getParameter("confermo")))
  {
    // Risetto tutti i valori presentati a video e ricalcolo le combo in fuzione
    // dei valori selezionati e ne controllo la correttezza formale
    storicoParticellaVO.setSupCatastale(request.getParameter("supCatastale"));
    // La superficie catastale è obbligatoria se la superifcie grafica è = 0
    if(!Validator.isNotEmpty(storicoParticellaVO.getSupCatastale())) 
    {
      errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    // Altrimenti, se è valorizzata ...
    else 
    {
      // ... controllo che sia un valore valido
      // se e solo se non è una particella provvisoria
      if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
      {
	      if(Validator.validateDouble(storicoParticellaVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) == null) 
	      {
	        errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	      }
	    }
    }
    
    //salvataggio valori tipo area...
    Vector<TipoAreaVO> vTipoArea = storicoParticellaVO.getvValoriTipoArea();
    String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
    String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
    int numValore = 0;
    for(int j=0;j<vTipoArea.size();j++)
    {
      if(j%2 == 0)
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea1[numValore]);
      }
      else
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea2[numValore]);
        numValore++;
      }
     
    }
    
    
    if(Validator.isNotEmpty(request.getParameter("idCasoParticolare"))) {
      storicoParticellaVO.setIdCasoParticolare(Long.decode(request.getParameter("idCasoParticolare")));
    }
    else {
      storicoParticellaVO.setIdCasoParticolare(null);
      errors.add("idCasoParticolare", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    if(Validator.isNotEmpty(request.getParameter("flagCaptazionePozzi"))) {
      storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_S);
    }
    else {
      storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
    }
    if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) {
      storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
    }
    else {
      storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
    }
    if(Validator.isNotEmpty(request.getParameter("idIrrigazione"))) {
      storicoParticellaVO.setIdIrrigazione(Long.decode(request.getParameter("idIrrigazione")));
    }
    else {
      storicoParticellaVO.setIdIrrigazione(null);
      // ... il tipo irrigazione è obbligatorio
      if(storicoParticellaVO.getFlagIrrigabile().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        errors.add("idIrrigazione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
    }
 
    // Il tipo rotazione coltura è obbligatorio
    if(request.getParameter("idRotazioneColturale").equalsIgnoreCase("-1")) {
      errors.add("idRotazioneColturale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    else
    {
      storicoParticellaVO.setIdRotazioneColturale(Long.decode(request.getParameter("idRotazioneColturale")));
    }
    // Il tipo terrazzamento è obbligatorio
    if(request.getParameter("idTerrazzamento").equalsIgnoreCase("-1")) {
      errors.add("idTerrazzamento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    else
    {
      storicoParticellaVO.setIdTerrazzamento(Long.decode(request.getParameter("idTerrazzamento")));
    }
    if(Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) 
    {
      storicoParticellaVO.setIdMetodoIrriguo(Long.decode(request.getParameter("idMetodoIrriguo")));
    }
    else 
    {
      storicoParticellaVO.setIdMetodoIrriguo(null);
    }
    
    
    
    
    
    //Controlli supCatastale
    if(session.getAttribute("flagObbligoCatastale") != null)
    {
      if(errors.size() == 0) 
      {       
        double supCatastaleDb = NumberUtils.arrotonda(Double.parseDouble(storicoParticellaVO.getSupCatastale().replace(',', '.')),4);
        if(supCatastaleDb == 0)
        {
          errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_KO_SUP_CATASTALE_ZERO));
        }     
      
      }
    }
    
    
    
    
    
    
    

    // Se si sono verificati errori li visualizzo
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
      return;
    }
    
    //se sono qui tutti i controlli sono andatia buon fine
    if(session.getAttribute("flagObbligoCatastale") != null)
    {
      double supCatastaleDb = NumberUtils.arrotonda(Double.parseDouble(storicoParticellaVO.getSupCatastale().replace(',', '.')),4);
      
      if(Validator.isEmpty(request.getParameter("confermaSupCatFraz")))
      {      
	      if(idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)
	      {
	        StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
	        double supCatastaleOldDb = NumberUtils.arrotonda(Double.parseDouble(elencoStoricoEvento[0].getSupCatastale().replace(',', '.')),4);
	        //Se la particella è cessata puà essere la superficie catastale a 0!!!!
	        //Quindi non devo fare nessun controllo
	        if(supCatastaleOldDb !=0)
	        {          
	          if(supCatastaleDb > supCatastaleOldDb)
	          {
	            //errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_KO_SUP_CATASTALE_FRAZIONAMENTO));
	            request.setAttribute("confermaSupCatFraz", "confermaSupCatFraz");
              request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
              return;       
	          }
	        }
	      }
	    }        
      
      if(Validator.isEmpty(request.getParameter("confermaSupCatAcc")))
      {   
	      if(idEvento.compareTo(SolmrConstants.INSERIMENTO_ACCORPAMENTO) == 0)
	      {
	        StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
	        double supCatastaleOldDb = 0;
	        for(int k=0;k<elencoStoricoEvento.length;k++)
	        {
	          supCatastaleOldDb = supCatastaleOldDb + NumberUtils.arrotonda(Double.parseDouble(elencoStoricoEvento[k].getSupCatastale().replace(',', '.')),4);
	          supCatastaleOldDb = NumberUtils.arrotonda(supCatastaleOldDb,4);
	        }
	        //Se la particella è cessata puà essere la superficie catastale a 0!!!!
	        //Quindi non devo fare nessun controllo
	        if(supCatastaleOldDb !=0)
	        {
	          if(supCatastaleDb > NumberUtils.arrotonda(supCatastaleOldDb,4))
	          {
	            //errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_KO_SUP_CATASTALE_ACCORPAMENTO));
	            request.setAttribute("confermaSupCatAcc", "confermaSupCatAcc");
              request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
              return;
	          }
	        }        
	      }
	    }
      
    }
    
    
    
    // Se ho passato tutti i controlli effettuo l'inserimento della particella
    try 
    {
    
      session.removeAttribute("flagObbligoCatastale");
      
      
      String msg=anagFacadeClient.inserisciParticella(storicoParticellaVO, ruoloUtenza, elencoParticelleEvento, idEvento);
      if (msg!=null)
      {
        error = new ValidationError(msg);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
        return;
      }
    }
    catch(SolmrException se) {
      error = new ValidationError(AnagErrors.ERRORE_KO_INSERIMENTO_PARTICELLA);
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(inserisciTerrenoParticellareUrl).forward(request, response);
      return;
    }
    
    
    
    storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
    session.removeAttribute("storicoParticellaVO");
    session.removeAttribute("elencoStoricoEvento");
    StoricoParticellaVO nuovoStoricoParticellaVO = new StoricoParticellaVO();
    nuovoStoricoParticellaVO.setComuneParticellaVO(storicoParticellaVO.getComuneParticellaVO());
    nuovoStoricoParticellaVO.setSezione(storicoParticellaVO.getSezione());
    nuovoStoricoParticellaVO.setFoglio(storicoParticellaVO.getFoglio());
    session.setAttribute("storicoParticellaVO", nuovoStoricoParticellaVO);
    // Vado alla pagina di inserimento particella(primo step)
    %>
      <jsp:forward page= "<%= inserisciTerrenoParticellareCtrlUrl %>" >
        <jsp:param name="inserimento" value="inserimento"/>
      </jsp:forward>
    <%
    
    
  }

  // Vado alla pagina di inserimento dati catastlai
  %>
    <jsp:forward page="<%=inserisciTerrenoParticellareUrl%>" />
  

