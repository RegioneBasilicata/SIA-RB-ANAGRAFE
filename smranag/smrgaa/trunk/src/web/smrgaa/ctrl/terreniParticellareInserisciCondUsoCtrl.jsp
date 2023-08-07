<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "terreniParticellareInserisciCondUsoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  
  SolmrLogger.debug(this, " - terreniParticellareInserisciCondUsoCtrl.jsp - INIZIO PAGINA");

	String terreniParticellareInserisciCtrlUrl = "/ctrl/terreniParticellareInserisciCtrl.jsp";
	String terreniParticellareInserisciCondUsoUrl = "/view/terreniParticellareInserisciCondUsoView.jsp";
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserimento particelle."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	StoricoParticellaVO[] elencoParticelleEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
	ValidationErrors errors = new ValidationErrors();
	ValidationErrors errori = new ValidationErrors();
	ValidationError error = null;
	Vector<UtilizzoParticellaVO> vUtilizziEfaVO = new Vector<UtilizzoParticellaVO>();
  Vector<UtilizzoParticellaVO> vUtilizziVO = new Vector<UtilizzoParticellaVO>();
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	boolean isPiemontese = false;
	
	Long idEvento = null;
	if(Validator.isNotEmpty(request.getParameter("idEvento"))) 
	{
	  idEvento = Long.decode(request.getParameter("idEvento"));
		request.setAttribute("idEvento", idEvento);
		session.setAttribute("idEventoSes", idEvento);
	}
	//me lo perdo uando a finestra popAggiungUsi...
	if(Validator.isEmpty(idEvento))
	{
	  idEvento = (Long)session.getAttribute("idEventoSes");
	}
	
	String provvisoria = request.getParameter("provvisoria");
	request.setAttribute("provvisoria", provvisoria);
	String[] elencoUtilizziSelezionati = request.getParameterValues("idTipoUtilizzo");
	String[] elencoUtilizziSecondariSelezionati = request.getParameterValues("idTipoUtilizzoSecondario");
	String[] elencoDestSelezionati = request.getParameterValues("idTipoDestinazione");
	String[] elencoDestSecondariSelezionati = request.getParameterValues("idTipoDestinazioneSecondario");
	String[] elencoDettUsoSelezionati = request.getParameterValues("idTipoDettaglioUso");
  String[] elencoDettUsoSecondariSelezionati = request.getParameterValues("idTipoDettaglioUsoSecondario");
  String[] elencoQualitaUsoSelezionati = request.getParameterValues("idTipoQualitaUso");
  String[] elencoQualitaUsoSecondariSelezionati = request.getParameterValues("idTipoQualitaUsoSecondario");
	String[] elencoVarietaSelezionate = request.getParameterValues("idVarieta");
  String[] elencoVarietaSecondarieSelezionate = request.getParameterValues("idVarietaSecondaria");
	String[] arrIdTipoEfa = request.getParameterValues("idTipoEfa");
  String[] elencoUtilizziEfaSelezionati = request.getParameterValues("idTipoUtilizzoEfa");
  String[] elencoDestinazioneEfaSelezionate = request.getParameterValues("idTipoDestinazioneEfa");
  String[] elencoDettaglioUsoEfaSelezionate = request.getParameterValues("idTipoDettaglioUsoEfa");
  String[] elencoQualitaUsoEfaSelezionate = request.getParameterValues("idTipoQualitaUsoEfa");
  String[] elencoVarietaEfaSelezionate = request.getParameterValues("idVarietaEfa");
  String[] elencoValoreOriginale = request.getParameterValues("valoreOriginale");
  String regimeParticellareInserisciCondUso = request.getParameter("regimeParticellareInserisciCondUso");
	
	String indice = (String)session.getAttribute("indice");
	
	boolean isKoUtilizzi = false;
	boolean isKoPiante = false;
	boolean isKoUtilizziEfa = false;
	Hashtable<Integer,ValidationErrors> erroriUtilizzi = new Hashtable<Integer,ValidationErrors>();
	Hashtable<Integer,ValidationErrors> erroriPianteConsociate = new Hashtable<Integer,ValidationErrors>();
	Hashtable<Integer,ValidationErrors> erroriUv = new Hashtable<Integer,ValidationErrors>();
	Hashtable<Integer,ValidationErrors> erroriUtilizziEfa = new Hashtable<Integer,ValidationErrors>();
	double totSupUtilizzata = 0;
  
  
  //Ricavo gli id_particella per avere le uv 
  Long idParticellaCurr = null;
  if(Validator.isNotEmpty(storicoParticellaVO))
  {
    if(Validator.isNotEmpty(storicoParticellaVO.getIdParticella()))
    {
      idParticellaCurr = storicoParticellaVO.getIdParticella();
    } 
  }
  
  
  //ricavo gli utilizzi se arrivo la prima volta
  if(Validator.isEmpty(regimeParticellareInserisciCondUso))
  {
    ConduzioneParticellaVO conduzioneVO = storicoParticellaVO.getElencoConduzioni()[0];
    UtilizzoParticellaVO[] elencoUtilizziVO = conduzioneVO.getElencoUtilizzi();
    if(Validator.isNotEmpty(elencoUtilizziVO))
    {
	    for(int a = 0; a < elencoUtilizziVO.length; a++) 
	    {
	      UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizziVO[a];
	      if(Validator.isNotEmpty(utilizzoParticellaVO.getDichiarabileEfa()) 
	        && "S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa()))
	      {
	        vUtilizziEfaVO.add(utilizzoParticellaVO);
	      }
	      else
	      {      
	        vUtilizziVO.add(utilizzoParticellaVO);
        }  
            
	    }
	  }
	  
	  request.setAttribute("vUtilizziEfaVO", vUtilizziEfaVO);
    request.setAttribute("vUtilizziVO", vUtilizziVO);  
  }
  
  
  
  //Ricavo gli id_particella per avere le uv 
  Vector<Long> vIdParticella = null;
  if(Validator.isNotEmpty(elencoParticelleEvento))
  {
    for(int i=0;i<elencoParticelleEvento.length;i++)
    {
      if(Validator.isNotEmpty(elencoParticelleEvento[i].getIdParticella()))
      {
        if(vIdParticella == null)
        {
          vIdParticella = new Vector<Long>();
        }
        if(!vIdParticella.contains(elencoParticelleEvento[i].getIdParticella()))
        {
           vIdParticella.add(elencoParticelleEvento[i].getIdParticella());
        }
      }
    } 
  } 
  //*********************************
  

  Vector<StoricoParticellaVO> vStoricoParticellaVO = null;
  try 
  {
    vStoricoParticellaVO = gaaFacadeClient
      .getListUVForInserimento(idParticellaCurr, vIdParticella, anagAziendaVO.getIdAzienda());
    request.setAttribute("vStoricoParticellaVO",vStoricoParticellaVO);  
      
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
  
  
  try 
  {
	  ProvinciaVO provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
	  if(provinciaControlloVO != null) 
	  {
	    if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
	    {
	      request.setAttribute("isPiemontese", "true");
	      isPiemontese = true;
	    }
	  }
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
  
  
  Vector<TipoAreaVO> vTipoAreaAll = null;
  //caricamento tipi area 
  try 
  {
    vTipoAreaAll = gaaFacadeClient.getAllValoriTipoArea();
    request.setAttribute("vTipoArea", vTipoAreaAll);
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
  
  
  //caricamento tipo allevamento
  try 
  {
    Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = gaaFacadeClient.getElencoFaseAllevamento();
    request.setAttribute("vTipoFaseAllev", vTipoFaseAllev);
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
  
  
  
  String parametroInserisciNuovaPart = null;
  try 
  {
    parametroInserisciNuovaPart = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INSERISCI_NUOVA_PART);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareCondUsoCtrlCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INSERISCI_NUOVA_PART+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("parametroInserisciNuovaPart", parametroInserisciNuovaPart);
  
  
  
  //Nuova eleggibilità
 
  ParticellaCertificataVO particellaCertificataVO = null;
  if(Validator.isNotEmpty(storicoParticellaVO))
  {
    particellaCertificataVO = anagFacadeClient.findParticellaCertificataByParametersNewElegFit(
        storicoParticellaVO.getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), 
        storicoParticellaVO.getParticella(), storicoParticellaVO.getSubalterno(), true, null);
        
    request.setAttribute("particellaCertificataVO", particellaCertificataVO);
  }
  
  
  if(Validator.isNotEmpty(particellaCertificataVO)
    && particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca())
  {
    Vector<RegistroPascoloVO> vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliPianoLavorazione(
      particellaCertificataVO.getIdParticellaCertificata().longValue());
    request.setAttribute("vRegistroPascoloVO", vRegistroPascoloVO);
     
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
  //Prima volta che entro
  if(session.getAttribute("flagObbligoCatastale") == null)
  {
    if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
    { 
	    if(((superficieGrafica == 0) && (superficieCatastale == 0))
	      || (idEvento.compareTo(SolmrConstants.INSERIMENTO_ACCORPAMENTO) == 0)
	      || (idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)) 
		  {
		    //Parametro che mi permette di mantenere la possibilità di modificare la sup catastale
		    //Rimosso se l'inserimento è andato a buon fine 
		    //oppure nel terrenoParticellareInserisci solo se entro dal menu (prima volta!)
		    session.setAttribute("flagObbligoCatastale","true");
		  }
		}
		//Se la particella è null
		//Sono nel caso della provvisoria quindi non devo modifcare la sup catastale..
		// e deve essere 0, anche sup condotta e percentuale possesso a 0
		else
		{
		  storicoParticellaVO.setSupCatastale(SolmrConstants.DEFAULT_SUPERFICIE);
		  if((storicoParticellaVO.getElencoConduzioni() != null)
		    &&  (storicoParticellaVO.getElencoConduzioni()[0] != null))
		  {
		    storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(new BigDecimal(0));
		    storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(SolmrConstants.DEFAULT_SUPERFICIE);
		  }
		}
	}

	// Se indice è valorizzato, vuol dire che l'utente ha cercato di aggiungere un altro uso del suolo
	if(Validator.isNotEmpty(indice)) {
		if(Validator.isNotEmpty(session.getAttribute("idTipoUtilizzoPrimario"))) {
			storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[Integer.parseInt(indice)].setIdUtilizzo(Long.decode((String)session.getAttribute("idTipoUtilizzoPrimario")));
			session.removeAttribute("idTipoUtilizzoPrimario");
		}
		if(Validator.isNotEmpty(session.getAttribute("idTipoUtilizzoSecondario"))) {
			storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[Integer.parseInt(indice)].setIdUtilizzoSecondario(Long.decode((String)session.getAttribute("idTipoUtilizzoSecondario")));
			session.removeAttribute("idTipoUtilizzoSecondario");
		}
		session.removeAttribute("indice");
		session.setAttribute("storicoParticellaVO", storicoParticellaVO);
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
	  request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
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
	  request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
	  return;
	}

	// Ricerco i titoli possesso
	it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = null;
	try 
	{
		elencoTitoliPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
		request.setAttribute("elencoTitoliPossesso", elencoTitoliPossesso);
	}
	catch(SolmrException se) 
	{
		error = new ValidationError(AnagErrors.ERRORE_KO_TITOLO_POSSESSO);
	 	errors.add("idTitoloPossesso", error);
	 	request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
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
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
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
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
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
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  // Ricerco i tipi metodo irriguo
  Vector<TipoSeminaVO> vTipoSemina = null;
  try 
  {
    vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
    request.setAttribute("vTipoSemina", vTipoSemina);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_SEMINA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  

	// Recupero l'elenco delle piante consociate attive censite sul DB
 	Vector<TipoPiantaConsociataVO> elencoPianteConsociate = null;
 	try 
 	{
 		elencoPianteConsociate = anagFacadeClient.getListPianteConsociate(true);
 		request.setAttribute("elencoPianteConsociate", elencoPianteConsociate);
 	}
 	catch(SolmrException se) 
 	{
 		error = new ValidationError(AnagErrors.ERRORE_KO_PIANTE_CONSOCIATE);
	 	errors.add("error", error);
	 	request.setAttribute("errors", errors);
	  request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
	  return;
 	}
 	
 	
 	// Ricerco gli usi primari legati all'azienda
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
	if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
	{
 		try 
 		{
	 		elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
	 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
 		}
 		catch(SolmrException se) 
 		{
 			error = new ValidationError(AnagErrors.ERRORE_KO_USO_PRIMARIO);
 		 	errors.add("error", error);
 		 	request.setAttribute("errors", errors);
 		  request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
 		  return;
 		}
	}
	else 
	{
		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
	}
	
	// Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSelezionati[i]));
            elencoDestinazione.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzo.getIdUtilizzo());
            elencoDestinazione.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    request.setAttribute("elencoDestinazione", elencoDestinazione);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRIMARIA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]));
            elencoDettUso.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione());
            elencoDettUso.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    request.setAttribute("elencoDettUso", elencoDettUso);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_DETT_USO_PRIMARIA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  
  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]));
            elencoQualitaUso.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso());
            elencoQualitaUso.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    request.setAttribute("elencoQualitaUso", elencoQualitaUso);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }

	// Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
	Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer,Vector<TipoVarietaVO>>();
 	try 
 	{
 	  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
 	  {
	 		if(elencoUtilizziSelezionati != null) 
	 		{
	 			for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
	 			{
					if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
					{
						Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]));
						elencoVarieta.put(new Integer(i), vVarieta);
					}
	 			}
			}
	  }
 		else 
 		{
 			if(vUtilizziVO != null) 
 			{
 				for(int i = 0; i < vUtilizziVO.size(); i++) 
 				{
 					UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
 					if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
 					{
 						Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso());
 						elencoVarieta.put(new Integer(i), vVarieta);
 					}
 	 			}
 			}
 		}
 		request.setAttribute("elencoVarieta", elencoVarieta);
 	}
 	catch(SolmrException se) 
 	{
 		error = new ValidationError(AnagErrors.ERRORE_KO_VARIETA_PRIMARIA);
		errors.add("error", error);
		request.setAttribute("errors", errors);
		request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
		return;
	}
	
	// Ricerco il periodo semina in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]),
              Long.decode(elencoVarietaSelezionate[i]));
            elencoPerSemina.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso(),
              utilizzo.getIdVarieta());
            elencoPerSemina.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    request.setAttribute("elencoPerSemina", elencoPerSemina);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  
  // Ricerco il pratica Mantenim in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = new Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>();
  Hashtable<Integer,String> elencoFaseAllev = new Hashtable<Integer,String>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoVarietaSelezionate[i]), Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), 
              Long.decode(elencoQualitaUsoSelezionati[i]));
            Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
            Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
            elencoPerMantenim.put(new Integer(i), vPraticaMantenim);
            
            String faseAllevamento = "N";
            if("S".equalsIgnoreCase(catalogoMatricePraticVO.getPermanente()))
              faseAllevamento = "S";
            elencoFaseAllev.put(new Integer(i), faseAllevamento);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdVarieta(), utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso());
            Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
            Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
            elencoPerMantenim.put(new Integer(i), vPraticaMantenim);
            
            String faseAllevamento = "N";
            if("S".equalsIgnoreCase(catalogoMatricePraticVO.getPermanente()))
              faseAllevamento = "S";
            elencoFaseAllev.put(new Integer(i), faseAllevamento);
          }
        }
      }
    }
    request.setAttribute("elencoPerMantenim", elencoPerMantenim);
    request.setAttribute("elencoFaseAllev", elencoFaseAllev);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_PRATICA_MANTENIMENTO);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }  
  	
	
	

	// Ricerco gli usi secondari legati all'azienda
	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloSecondario");
 	if(elencoTipiUsoSuoloSecondario == null || elencoTipiUsoSuoloSecondario.size() == 0) 
 	{
		try 
		{
			elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
	 		request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
		}
		catch(SolmrException se) 
    {
			error = new ValidationError(AnagErrors.ERRORE_KO_USO_SECONDARIO);
			errors.add("idUtilizzoSecondario", error);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
			return;
		}
 	}
 	else 
 	{
 		request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
 	}
 	
 	
 	// Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]));
            elencoDestinazioneSecondario.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzo.getIdUtilizzoSecondario());
            elencoDestinazioneSecondario.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    request.setAttribute("elencoDestinazioneSecondario", elencoDestinazioneSecondario);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_DEST_USO_SECONDARIO);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]));
            elencoDettUsoSecondario.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario());
            elencoDettUsoSecondario.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    request.setAttribute("elencoDettUsoSecondario", elencoDettUsoSecondario);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_DETTAGLIO_USO_SECONDARIO);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  
  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]));
            elencoQualitaUsoSecondario.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario());
            elencoQualitaUsoSecondario.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    request.setAttribute("elencoQualitaUsoSecondario", elencoQualitaUsoSecondario);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_QUALITA_USO_SECONDARIO);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  }
  
  // Ricerco la varietà secondaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = new Hashtable<Integer,Vector<TipoVarietaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]));
            elencoVarietaSecondaria.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario(), utilizzo.getIdTipoQualitaUsoSecondario());
            elencoVarietaSecondaria.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    request.setAttribute("elencoVarietaSecondaria", elencoVarietaSecondaria);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_VARIETA_SECONDARIA);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  } 
  
  // Ricerco il periodo semina in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]),
              Long.decode(elencoVarietaSecondarieSelezionate[i]));
            elencoPerSeminaSecondario.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario(), utilizzo.getIdTipoQualitaUsoSecondario(),
              utilizzo.getIdVarietaSecondaria());
            elencoPerSeminaSecondario.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    request.setAttribute("elencoPerSeminaSecondario", elencoPerSeminaSecondario);
  }
  catch(SolmrException se) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA_SEC);
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
    return;
  } 

	
  
	// Ricerco i tipi impianti attivi
 	TipoImpiantoVO[] elencoTipoImpianto = null;
 	try 
 	{
 		elencoTipoImpianto = anagFacadeClient.getListTipoImpianto(true, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
 		request.setAttribute("elencoTipoImpianto", elencoTipoImpianto);
 	}
 	catch(SolmrException se) 
 	{
 		error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_IMPIANTO);
		errors.add("idImpianto", error);
		request.setAttribute("errors", errors);
		request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
		return;
	}
	
	
	
	// TIPO EFA
  Vector<TipoEfaVO> vTipoEfa = null;
  try 
  {
    vTipoEfa = gaaFacadeClient.getListTipoEfa();
    request.setAttribute("vTipoEfa", vTipoEfa);
  }
  catch(SolmrException se) 
  {
  
    SolmrLogger.info(this, " - terreniParticellareInserisciCondUsoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_EFA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  Hashtable<Integer, Vector<TipoUtilizzoVO>> elencoUtilizziEfa = new Hashtable<Integer, Vector<TipoUtilizzoVO>>();
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa))
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        { 
          Vector<TipoUtilizzoVO> vUtilizzi = gaaFacadeClient.getListTipoUtilizzoEfa(new Long(arrIdTipoEfa[i]));
          if(vUtilizzi != null)
            elencoUtilizziEfa.put(new Integer(i), vUtilizzi);
        }
      }   
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vUtilizziEfaVO))
    {
      for(int i = 0; i < vUtilizziEfaVO.size(); i++) 
      { 
        if(Validator.isNotEmpty(vUtilizziEfaVO.get(i).getIdTipoEfa()))
        {
	        Vector<TipoUtilizzoVO> vUtilizzi = gaaFacadeClient.getListTipoUtilizzoEfa(vUtilizziEfaVO.get(i).getIdTipoEfa());
	        if(vUtilizzi != null)
	          elencoUtilizziEfa.put(new Integer(i), vUtilizzi);
	      }  
      }   
    }
  }
  request.setAttribute("elencoUtilizziEfa", elencoUtilizziEfa);
  
  
  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer, Vector<TipoDestinazioneVO>> elencoDestinazioneEfa = new Hashtable<Integer, Vector<TipoDestinazioneVO>>();
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      { 
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {        
          Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getListTipoDestinazioneEfa(
            new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziEfaSelezionati[i]));
          if(Validator.isNotEmpty(vTipoDestinazione))
            elencoDestinazioneEfa.put(new Integer(i), vTipoDestinazione);
        }       
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vUtilizziEfaVO))
    {
      for(int i = 0; i < vUtilizziEfaVO.size(); i++) 
      {  
        if(Validator.isNotEmpty(vUtilizziEfaVO.get(i).getIdTipoEfa()))
        {       
          Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getListTipoDestinazioneEfa(
            vUtilizziEfaVO.get(i).getIdTipoEfa(), vUtilizziEfaVO.get(i).getIdUtilizzo());
          elencoDestinazioneEfa.put(new Integer(i), vTipoDestinazione);
        }
        
      }
    } 
  }
  request.setAttribute("elencoDestinazioneEfa", elencoDestinazioneEfa);
  
  
  // Recupero la varietà primaria relativa ai tipi uso suolo utilizzati dall'azienda in esame  
  Hashtable<Integer, Vector<TipoDettaglioUsoVO>> elencoDettaglioUsoEfa = new Hashtable<Integer, Vector<TipoDettaglioUsoVO>>();
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {         
          Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(new Long(arrIdTipoEfa[i]), 
            new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]));
          if(Validator.isNotEmpty(vTipoDettaglioUso))
            elencoDettaglioUsoEfa.put(new Integer(i), vTipoDettaglioUso);
        }
      }
    }
  }
  //prima volta che entro
  else
  {
    if(vUtilizziEfaVO != null) 
    {
      for(int i = 0; i < vUtilizziEfaVO.size(); i++) 
      { 
        if(Validator.isNotEmpty(vUtilizziEfaVO.get(i).getIdTipoEfa()))
        {        
          Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(
            vUtilizziEfaVO.get(i).getIdTipoEfa(), vUtilizziEfaVO.get(i).getIdUtilizzo(),
            vUtilizziEfaVO.get(i).getIdTipoDestinazione());
          if(Validator.isNotEmpty(vTipoDettaglioUso))
            elencoDettaglioUsoEfa.put(new Integer(i), vTipoDettaglioUso);
        }
      }
    } 
  }
  request.setAttribute("elencoDettaglioUsoEfa", elencoDettaglioUsoEfa);
  
  // Recupero la varietà primaria relativa ai tipi uso suolo utilizzati dall'azienda in esame  
  Hashtable<Integer, Vector<TipoQualitaUsoVO>> elencoQualitaUsoEfa = new Hashtable<Integer, Vector<TipoQualitaUsoVO>>();
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {         
          Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(new Long(arrIdTipoEfa[i]), 
            new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]), 
            new Long(elencoDettaglioUsoEfaSelezionate[i]));
          if(Validator.isNotEmpty(vTipoQualitaUso))
            elencoQualitaUsoEfa.put(new Integer(i), vTipoQualitaUso);
        }
      }
    }
  }
  //prima volta che entro
  else
  {
    if(vUtilizziEfaVO != null) 
    {
      for(int i = 0; i < vUtilizziEfaVO.size(); i++) 
      { 
        if(Validator.isNotEmpty(vUtilizziEfaVO.get(i).getIdTipoEfa()))
        {        
          Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(
            vUtilizziEfaVO.get(i).getIdTipoEfa(), vUtilizziEfaVO.get(i).getIdUtilizzo(),
            vUtilizziEfaVO.get(i).getIdTipoDestinazione(), vUtilizziEfaVO.get(i).getIdTipoDettaglioUso());
          if(Validator.isNotEmpty(vTipoQualitaUso))
            elencoQualitaUsoEfa.put(new Integer(i), vTipoQualitaUso);
        }
      }
    } 
  }
  request.setAttribute("elencoQualitaUsoEfa", elencoQualitaUsoEfa);    
    
    
  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarietaEfa = new Hashtable<Integer, Vector<TipoVarietaVO>>();
  if(Validator.isNotEmpty(regimeParticellareInserisciCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      { 
        if(Validator.isNotEmpty(arrIdTipoEfa[i]))
        {        
          Vector<TipoVarietaVO> varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
            new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]),
            new Long(elencoDettaglioUsoEfaSelezionate[i]), new Long(elencoQualitaUsoEfaSelezionate[i]));
          if(Validator.isNotEmpty(varieta))
            elencoVarietaEfa.put(new Integer(i), varieta);
        }       
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vUtilizziEfaVO))
    {
      for(int i = 0; i < vUtilizziEfaVO.size(); i++) 
      {  
        if(Validator.isNotEmpty(vUtilizziEfaVO.get(i).getIdUtilizzo()))
        {       
	        Vector<TipoVarietaVO> varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
	          vUtilizziEfaVO.get(i).getIdTipoEfa(), vUtilizziEfaVO.get(i).getIdUtilizzo(),
	          vUtilizziEfaVO.get(i).getIdTipoDestinazione(), vUtilizziEfaVO.get(i).getIdTipoDettaglioUso(),
	          vUtilizziEfaVO.get(i).getIdTipoQualitaUso());
	        if(Validator.isNotEmpty(varieta))
	          elencoVarietaEfa.put(new Integer(i), varieta);
	      }
	      
      }
    } 
  }
  request.setAttribute("elencoVarietaEfa", elencoVarietaEfa);
    
 
  
  
  

 	// L'utente ha selezionato il pulsante indietro
 	SolmrLogger.debug(this, "--- indietro terreniParticellareInserisciCondUsoCtrl = "+request.getParameter("indietro"));
 	if(Validator.isNotEmpty(request.getParameter("indietro"))) 
  {
 		// Setto i parametri relativi ai dati della conduzione,esclusa la sup condotta,
 		// e i dati dell'uso del suolo all'interno del VO
 		storicoParticellaVO.setSupCatastale(request.getParameter("supCatastale"));
 		
 		//salvataggio valori tipo area...
 		String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
    String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
 		Vector<TipoAreaVO> vTipoArea = storicoParticellaVO.getvValoriTipoArea();	 		
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
 		if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
 		}
 		else 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idIrrigazione"))) 
    {
 			storicoParticellaVO.setIdIrrigazione(Long.decode(request.getParameter("idIrrigazione")));
 		}
 		else 
    {
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
    
    
    
    
 		storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(request.getParameter("supCondotta"));
 		
    if(Validator.isNotEmpty(request.getParameter("percentualePossesso"))) 
    {
      if(Validator.isNumberPercentualeMaggioreZeroDecimali(request.getParameter("percentualePossesso")))
      {
        storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(new BigDecimal(request.getParameter("percentualePossesso").replace(",", ".")));
      }
      else
      {
        storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(null);
      }
    }
    else 
    {
      storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(null);
    }
 		storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(request.getParameter("superficieAgronomica"));
 		if(Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) 
 		{
 			storicoParticellaVO.getElencoConduzioni()[0].setIdTitoloPossesso(Long.decode(request.getParameter("idTitoloPossesso")));
 		}
 		else 
 		{
 			storicoParticellaVO.getElencoConduzioni()[0].setIdTitoloPossesso(null);
 		}
 		
 		//Conto numero utilizzi totali...
 		int numeroUtilizzi = 0;
 		if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo"))) 
    {
      numeroUtilizzi = request.getParameterValues("idTipoUtilizzo").length;
    }
 		
 		Vector<UtilizzoParticellaVO> vUtilizzi = new Vector<UtilizzoParticellaVO>();
 		for(int i=0; i<numeroUtilizzi; i++) 
	 	{
 			UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
 			
 			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")) 
 			  && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) 
 			{
 				utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
 			}
 			else 
 			{
 				utilizzoParticellaVO.setIdUtilizzo(null);
 			}
 			
 			if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i])) 
      {
        utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdTipoDestinazione(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])) 
      {
        utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdTipoDettaglioUso(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i])) 
      {
        utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdTipoQualitaUso(null);
      }
 			
 			if(Validator.isNotEmpty(request.getParameterValues("idVarieta")) 
 			  && Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) 
      {
        utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdVarieta(null);
      }
      
      
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[i])) 
      {
        utilizzoParticellaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdTipoPeriodoSemina(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoSemina")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoSemina")[i])) 
      {
        utilizzoParticellaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[i]));
      }
      else 
      {
        utilizzoParticellaVO.setIdSemina(null);
      }
      
      
      if(Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazione")) 
        && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazione")[i])) 
      {
        utilizzoParticellaVO.setDataInizioDestinazioneStr(request.getParameterValues("dataInizioDestinazione")[i]);
      }
      else 
      {
        utilizzoParticellaVO.setDataInizioDestinazione(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("dataFineDestinazione")) 
        && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazione")[i])) 
      {
        utilizzoParticellaVO.setDataFineDestinazioneStr(request.getParameterValues("dataFineDestinazione")[i]);        
      }
      else 
      {
        utilizzoParticellaVO.setDataFineDestinazione(null);
      }
      
      
 			if(Validator.isNotEmpty(request.getParameterValues("supUtilizzata")) 
 			  && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[i])) 
 			{
 				utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzata")[i]));
 			}
 			else 
 			{
 				utilizzoParticellaVO.setSuperficieUtilizzata(null);
 			}
 			
 			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")) 
 			  && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i])) 
 		  {
 				utilizzoParticellaVO.setIdUtilizzoSecondario(Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[i]));
 				
 				if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneSecondario"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneSecondario")[i])) 
        {
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(Long.decode(request.getParameterValues("idTipoDestinazioneSecondario")[i]));
        }
        else 
        {
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
        }
        
        if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario")[i])) 
        {
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(Long.decode(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]));
        }
        else 
        {
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
        }
        
        if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i])) 
        {
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(Long.decode(request.getParameterValues("idTipoQualitaUsoSecondario")[i]));
        }
        else 
        {
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
        }
 				
 				if(Validator.isNotEmpty(request.getParameterValues("idVarietaSecondaria"))
 				  && Validator.isNotEmpty(request.getParameterValues("idVarietaSecondaria")[i])) 
 				{
 					utilizzoParticellaVO.setIdVarietaSecondaria(Long.decode(request.getParameterValues("idVarietaSecondaria")[i]));
 				}
 				else 
 				{
 					utilizzoParticellaVO.setIdVarietaSecondaria(null);
 				}
 				
 				
        
        if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario"))
          && Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i])) 
        {
          utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]));
        }
        else 
        {
          utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
        }
        
        
        
        if(Validator.isNotEmpty(request.getParameterValues("idTipoSeminaSecondario")) 
	        && Validator.isNotEmpty(request.getParameterValues("idTipoSeminaSecondario")[i])) 
	      {
	        utilizzoParticellaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]));
	      }
	      else 
	      {
	        utilizzoParticellaVO.setIdSeminaSecondario(null);
	      }
	      
	      
	      if(Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazioneSec")) 
	        && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazioneSec")[i])) 
	      {
	        utilizzoParticellaVO.setDataInizioDestinazioneSecStr(request.getParameterValues("dataInizioDestinazioneSec")[i]);
	      }
	      else 
	      {
	        utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
	      }
	      
	      if(Validator.isNotEmpty(request.getParameterValues("dataFineDestinazioneSec")) 
	        && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazioneSec")[i])) 
	      {
	        utilizzoParticellaVO.setDataFineDestinazioneSecStr(request.getParameterValues("dataFineDestinazioneSec")[i]);
	      }
	      else 
	      {
	        utilizzoParticellaVO.setDataFineDestinazioneSec(null);
	      }
 			}
 			else 
 			{
 				utilizzoParticellaVO.setIdUtilizzoSecondario(null);
 				utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
 				utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
 				utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
 				utilizzoParticellaVO.setIdVarietaSecondaria(null); 				
 				utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
 				utilizzoParticellaVO.setIdSeminaSecondario(null);
 				utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
 				utilizzoParticellaVO.setDataFineDestinazioneSec(null);
 			}
 			
 			if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")) 
 			  && Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[i])) 
 			{
 				utilizzoParticellaVO.setSupUtilizzataSecondaria(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzataSecondaria")[i]));
 			}
 			else 
 			{
 				utilizzoParticellaVO.setSupUtilizzataSecondaria(null);
 			}
 			
 			
 			if(Validator.isNotEmpty(request.getParameterValues("idPraticaMantenimento")[i])) {
        utilizzoParticellaVO.setIdPraticaMantenimento(new Long(request.getParameterValues("idPraticaMantenimento")[i]));
      }
      else {
        utilizzoParticellaVO.setIdPraticaMantenimento(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i])) {
        utilizzoParticellaVO.setIdFaseAllevamento(new Long(request.getParameterValues("idFaseAllevamento")[i]));
      }
      else {
        utilizzoParticellaVO.setIdFaseAllevamento(null);
      }
 			
 			if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) {
 				utilizzoParticellaVO.setAnnoImpianto(request.getParameterValues("annoImpianto")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setAnnoImpianto(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i])) {
 				utilizzoParticellaVO.setIdImpianto(Long.decode(request.getParameterValues("idImpianto")[i]));
 			}
 			else {
 				utilizzoParticellaVO.setIdImpianto(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) {
 				utilizzoParticellaVO.setSestoSuFile(request.getParameterValues("sestoSuFile")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setSestoSuFile(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) {
 				utilizzoParticellaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setSestoTraFile(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) {
 				utilizzoParticellaVO.setNumeroPianteCeppi(request.getParameterValues("numeroPianteCeppi")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setNumeroPianteCeppi(null);
 			}
 			// DATI UTILIZZO CONSOCIATI
 			UtilizzoConsociatoVO[] elencoUtilizziConsociati = null;
      int numPianteConsociate = 0;
      if(Validator.isNotEmpty(elencoPianteConsociate))
      {
        numPianteConsociate = elencoPianteConsociate.size();
      }
 			
 			if(numPianteConsociate != 0)  
 			{
 			  elencoUtilizziConsociati = new UtilizzoConsociatoVO[numPianteConsociate];
 				int valorePartenza = i*numPianteConsociate;
 				for(int a = 0; a < numPianteConsociate; a++) 
 				{
 					UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
 					if(i == 0) 
 					{
 						if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[a]);
 						}
 						else 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(null);
 						}
 					}
 					else 
 					{
 						if(request.getParameterValues("numeroPianteConsociate") != null 
 						  && valorePartenza < request.getParameterValues("numeroPianteConsociate").length) 
 						{
 							if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valorePartenza])) 
 							{
 								utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[valorePartenza]);
 							}
 							else 
 							{
 								utilizzoConsociatoVO.setNumeroPiante(null);
 							}
 						}
 					}
 					elencoUtilizziConsociati[a] = utilizzoConsociatoVO;
 					valorePartenza++;
 				}
 				utilizzoParticellaVO.setElencoUtilizziConsociati(elencoUtilizziConsociati);
 			}
 			utilizzoParticellaVO.setElencoUtilizziConsociati(elencoUtilizziConsociati);
 			
 			vUtilizzi.add(utilizzoParticellaVO);
 		}
 		
 		int numeroUtilizziEfa = 0;
    if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoEfa"))) 
    {
      numeroUtilizziEfa = request.getParameterValues("idTipoUtilizzoEfa").length;
    }
    
 		Vector<UtilizzoParticellaVO> vUtilizziEfa = new Vector<UtilizzoParticellaVO>();
    for(int i = 0; i < numeroUtilizziEfa; i++) 
    {
      UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
      utilizzoParticellaVO.setDichiarabileEfa("S");
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoEfa")[i]))
      {
        utilizzoParticellaVO.setIdTipoEfa(Long.decode((request.getParameterValues("idTipoEfa")[i])));
      }
      else
      {
        utilizzoParticellaVO.setIdTipoEfa(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoEfa")[i]))
      {
        utilizzoParticellaVO.setIdUtilizzo(Long.decode((request.getParameterValues("idTipoUtilizzoEfa")[i])));
      }
      else
      {
        utilizzoParticellaVO.setIdUtilizzo(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneEfa")[i]))
      {
        utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazioneEfa")[i]));
      }
      else
      {
        utilizzoParticellaVO.setIdTipoDestinazione(null);
      }
                  
      if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoEfa")[i]))
      {
        utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUsoEfa")[i]));
      }
      else
      {
        utilizzoParticellaVO.setIdTipoDettaglioUso(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoEfa")[i]))
      {
        utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUsoEfa")[i]));
      }
      else
      {
        utilizzoParticellaVO.setIdTipoQualitaUso(null);
      }
      
      if(Validator.isNotEmpty(request.getParameterValues("idVarietaEfa")) 
        && Validator.isNotEmpty(request.getParameterValues("idVarietaEfa")[i]))
      {
        utilizzoParticellaVO.setIdVarieta(Long.decode((request.getParameterValues("idVarietaEfa")[i])));
      }
      else
      {
        utilizzoParticellaVO.setIdVarieta(null);
      } 
      
      if(Validator.isNotEmpty(request.getParameterValues("abbPonderazioneVarieta")) 
        && Validator.isNotEmpty(request.getParameterValues("abbPonderazioneVarieta")[i]))
      {
        utilizzoParticellaVO.setAbbaPonderazione(Integer.decode((request.getParameterValues("abbPonderazioneVarieta")[i])));
      }
      else
      {
        utilizzoParticellaVO.setAbbaPonderazione(null);
      }     
      
      
      
      
      if(Validator.isNotEmpty(request.getParameterValues("valoreOriginale")) 
        && Validator.isNotEmpty(request.getParameterValues("valoreOriginale")[i]))
      {
        try
        {
		      utilizzoParticellaVO.setValoreOriginale(new BigDecimal(request.getParameterValues("valoreOriginale")[i].replace(",", ".")));
		      utilizzoParticellaVO.setValoreDopoConversione(new BigDecimal(request.getParameterValues("valoreDopoConversione")[i].replace(",", ".")));
		      utilizzoParticellaVO.setValoreDopoPonderazione(new BigDecimal(request.getParameterValues("valoreDopoPonderazione")[i].replace(",", ".")));     
		      
		      utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("valoreDopoConversione")[i]));
		    }
		    catch(Exception ex)
        {
          utilizzoParticellaVO.setValoreOriginale(null);
          utilizzoParticellaVO.setValoreDopoConversione(null);
          utilizzoParticellaVO.setValoreDopoPonderazione(null);         
          utilizzoParticellaVO.setSuperficieUtilizzata(null);
        }
      }
      else
      {
        utilizzoParticellaVO.setValoreOriginale(null);
        utilizzoParticellaVO.setValoreDopoConversione(null);
        utilizzoParticellaVO.setValoreDopoPonderazione(null);         
        utilizzoParticellaVO.setSuperficieUtilizzata(null);
      }
      
      vUtilizziEfa.add(utilizzoParticellaVO);
    }
    
    
    
    int numeroTotaleUtilizzi = vUtilizzi.size();
    numeroTotaleUtilizzi = numeroTotaleUtilizzi + vUtilizziEfa.size();
    
    UtilizzoParticellaVO[] elencoUtilizzi = null;
    
    if(numeroTotaleUtilizzi != 0) 
    {
      int contatore = 0;
      elencoUtilizzi = new UtilizzoParticellaVO[numeroTotaleUtilizzi];
      for(int i=0;i<vUtilizzi.size();i++)
      {
        elencoUtilizzi[contatore] = vUtilizzi.get(i); 
        contatore++;
      }
      
      for(int i=0;i<vUtilizziEfa.size();i++)
      {
        elencoUtilizzi[contatore] = vUtilizziEfa.get(i); 
        contatore++;
      }      
    }
    storicoParticellaVO.getElencoConduzioni()[0].setElencoUtilizzi(elencoUtilizzi);
    
    
    String[] arrVIdStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
    session.setAttribute("arrVIdStoricoUnitaArborea", arrVIdStoricoUnitaArborea);
    
    String[] arrVAreaUV = request.getParameterValues("area");
    session.setAttribute("arrVAreaUV", arrVAreaUV);
    
    
 		
 		// Rimetto in sessione l'oggetto aggiornato
 		session.setAttribute("storicoParticellaVO", storicoParticellaVO);

 		// Torno al primo step di inserimento della particella
 		%>
			<jsp:forward page="<%=terreniParticellareInserisciCtrlUrl%>" >
				<jsp:param name="indietro" value="indietro"/>
			</jsp:forward>
		<%
		return;
 	}
	
 	// L'utente ha premuto il tasto salva o confermo dopo il confirm sulla dichiarazione
 	// di superfici utilizzate minore della condotta
 	if(Validator.isNotEmpty(request.getParameter("salva")) 
    || Validator.isNotEmpty(request.getParameter("confermo"))) 
  {
		// Risetto tutti i valori presentati a video e ricalcolo le combo in fuzione
 		// dei valori selezionati e ne controllo la correttezza formale
 		storicoParticellaVO.setSupCatastale(request.getParameter("supCatastale"));
		// La superficie catastale è obbligatoria se non è presente la grafica
    
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
				if((Validator.validateDouble(storicoParticellaVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)
	        && (Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)) 
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
    
 		/*if(Validator.isNotEmpty(request.getParameter("idAreaA"))) {
 			storicoParticellaVO.setIdAreaA(Long.decode(request.getParameter("idAreaA")));
 		}
 		else {
 			storicoParticellaVO.setIdAreaA(null);
 			errors.add("idAreaA", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		}
 		if(Validator.isNotEmpty(request.getParameter("idAreaB"))) {
 			storicoParticellaVO.setIdAreaB(Long.decode(request.getParameter("idAreaB")));
 		}
 		else {
 			storicoParticellaVO.setIdAreaB(null);
 			errors.add("idAreaB", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		}
 		if(Validator.isNotEmpty(request.getParameter("idAreaC"))) {
 			storicoParticellaVO.setIdAreaC(Long.decode(request.getParameter("idAreaC")));
 		}
 		else {
 			storicoParticellaVO.setIdAreaC(null);
 			errors.add("idAreaC", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		}
 		if(Validator.isNotEmpty(request.getParameter("idAreaD"))) {
 			storicoParticellaVO.setIdAreaD(Long.decode(request.getParameter("idAreaD")));
 		}
 		else {
 			storicoParticellaVO.setIdAreaD(null);
 			errors.add("idAreaD", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		}
 		if(Validator.isNotEmpty(request.getParameter("idAreaM"))) {
      storicoParticellaVO.setIdAreaM(Long.decode(request.getParameter("idAreaM")));
    }
    else {
      storicoParticellaVO.setIdAreaM(null);
      errors.add("idAreaM", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }*/
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
 		if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
 		}
 		else 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idIrrigazione"))) 
    {
 			storicoParticellaVO.setIdIrrigazione(Long.decode(request.getParameter("idIrrigazione")));
 		}
 		else 
    {
 			storicoParticellaVO.setIdIrrigazione(null);
 		}
    /*if(request.getParameter("idPotenzialitaIrrigua").equalsIgnoreCase("-1")) 
    {
      errors.add("idPotenzialitaIrrigua", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    else 
    {
      storicoParticellaVO.setIdPotenzialitaIrrigua(Long.decode(request.getParameter("idPotenzialitaIrrigua")));
    }*/
    if(request.getParameter("idRotazioneColturale").equalsIgnoreCase("-1")) 
    {
      errors.add("idRotazioneColturale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    else 
    {
      storicoParticellaVO.setIdRotazioneColturale(Long.decode(request.getParameter("idRotazioneColturale")));
    }
    if(request.getParameter("idTerrazzamento").equalsIgnoreCase("-1")) 
    {
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
    
    
    
    // PERCENTUALE POSSESSO
    if(Validator.isNotEmpty(request.getParameter("percentualePossesso"))) 
    {
      if(!Validator.isNumberPercentualeMaggioreZeroDecimali(request.getParameter("percentualePossesso")))
      {
        errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_CONDUZIONE));
      }
      else
      {
        BigDecimal percentualePossessoTmp = new BigDecimal(request.getParameter("percentualePossesso").replace(',','.')); 
        storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(percentualePossessoTmp);
        // ... controllo che sia un valore valido
	      // se e solo se non è una particella provvisoria
	      if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
	      {
	        if(storicoParticellaVO.getIdParticella() != null)
	        {
	          //Controllo che non superi la percentuale di 100 sulla particella di mia altre conduzioni
	          //Il controllo lo devo fare solo se nn reinserisco la stessa!!!
	          boolean controlloPerc = true;
	          StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");         
	          if(Validator.isNotEmpty(elencoStoricoEvento)
	            && (elencoStoricoEvento.length > 0))
	          {
	            for(int i=0;i<elencoStoricoEvento.length;i++)
	            {
	              if(storicoParticellaVO.getIdParticella().compareTo(elencoStoricoEvento[i].getIdParticella()) == 0)
	              {
	                controlloPerc = false;
	                break;
	              }	              
	            }
	          }
	          
	          if(controlloPerc)
	          {
			        BigDecimal percentualPossessoAltreCondBg = gaaFacadeClient
			          .getSumPercentualPossessoAltreConduzioni(anagAziendaVO.getIdAzienda().longValue(), 
			          storicoParticellaVO.getIdParticella().longValue(), null);
			        percentualePossessoTmp = percentualePossessoTmp.add(percentualPossessoAltreCondBg);
			          
			        if(percentualePossessoTmp.compareTo(new BigDecimal(100)) > 0)
			        {
			          errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_SUM_PERCENTUALE_CONDUZIONE));
			        }
			      }
		      }
		    }   
      }
    }
    else 
    {
      errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
		// SUPERFICIE_CONDOTTA
		storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(request.getParameter("supCondotta"));   
		if(!Validator.isNotEmpty(request.getParameter("supCondotta"))) 
		{
			errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		// Altrimenti, se è valorizzata ...
		else 
    {
      if(Validator.validateDouble(request.getParameter("supCondotta"), 999999.9999) == null) 
      {
        errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
		}
		// SUPERFICIE AGRONOMICA
		storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(request.getParameter("superficieAgronomica"));
		if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieAgronomica())) 
    {
			// ... controllo che sia un valore valido
			if(Validator.validateDouble(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieAgronomica(), SolmrConstants.FORMAT_SUP_CONDOTTA) == null) 
      {
				errors.add("superficieAgronomica", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			// Se lo è...
			else 
      {
				// Controllo che non sia maggiore della superficie condotta
				if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta()) && Validator.validateDouble(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta(), SolmrConstants.FORMAT_SUP_CONDOTTA) != null) 
        {
					if(StringUtils.convertNumericField(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieAgronomica()) > StringUtils.convertNumericField(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta())) 
          {
						errors.add("superficieAgronomica", new ValidationError(AnagErrors.ERRORE_SUP_AGRONOMICA_MAX_SUP_CONDOTTA));
					}
				}
			}
		}
 		if(Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) 
    {
 			storicoParticellaVO.getElencoConduzioni()[0].setIdTitoloPossesso(Long.decode(request.getParameter("idTitoloPossesso")));
 		}
 		else 
    {
 			storicoParticellaVO.getElencoConduzioni()[0].setIdTitoloPossesso(null);
 			errors.add("idTitoloPossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		}
 		
 		
 		
 		
 		int numeroUtilizzi = 0;
    if(Validator.isNotEmpty(elencoUtilizziSelezionati))
    {
      numeroUtilizzi = elencoUtilizziSelezionati.length;
    }
 		
 		
 		if(numeroUtilizzi > 0) 
    {
	 		for(int i=0; i<numeroUtilizzi; i++) 
      {
	 			errori = new ValidationErrors();
	 			UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	 			
	 			
	 			
	 			TipoUtilizzoVO tipoUtilizzoVOSel = null;
	 			String flagFruttaGuscio = "N";
	      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")) 
	         && i < request.getParameterValues("idTipoUtilizzo").length
	         && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) 
	      {
	        tipoUtilizzoVOSel = anagFacadeClient.findTipoUtilizzoByPrimaryKey(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
	        flagFruttaGuscio = tipoUtilizzoVOSel.getFlagFruttaGuscio();
	      }
	      
	      errori = utilizzoParticellaVO.validateModificaTerritorialeCondUso(request, null, flagFruttaGuscio, i);     
	      
	      
	      
	      //controlli uso
	      /*if(Validator.isNotEmpty(elencoDettaglioUso))
	      { 
		      Vector<TipoDettaglioUsoVO> vDettaglioUsoCtrl = elencoDettaglioUso.get(new Integer(i));
		      if(Validator.isNotEmpty(vDettaglioUsoCtrl)
		        &&  (vDettaglioUsoCtrl.size() > 0))
		      {
		        if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso"))
		          && Validator.isEmpty(request.getParameterValues("idTipoDettaglioUso")[i]))        
		        {
		          errori.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		        }
		      }
		    }
		    
		    if(Validator.isNotEmpty(elencoDettaglioUsoSecondario))
		    {	        
	        Vector<TipoDettaglioUsoVO> vDettaglioUsoSecondarioCtrl = elencoDettaglioUsoSecondario.get(new Integer(i));
	        if(Validator.isNotEmpty(vDettaglioUsoSecondarioCtrl)
	          &&  (vDettaglioUsoSecondarioCtrl.size() > 0))
	        {
	          if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario"))
	            && Validator.isEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]))        
	          {
	            errori.add("idTipoDettaglioUsoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	          }
	        }
	      }*/
	        
	      CatalogoMatriceVO catalogoMatriceVO = null;
	      CatalogoMatriceVO catalogoMatriceVOSecondario = null; 
	      if(errori.size() > 0) 
	      {
	        isKoUtilizzi = true;
	      }
	      else
	      {
	        utilizzoParticellaVO.setIdUtilizzo(new Long(request.getParameterValues("idTipoUtilizzo")[i]));
	        utilizzoParticellaVO.setIdTipoDestinazione(new Long(request.getParameterValues("idTipoDestinazione")[i]));
	        utilizzoParticellaVO.setIdTipoDettaglioUso(new Long(request.getParameterValues("idTipoDettaglioUso")[i]));
	        utilizzoParticellaVO.setIdTipoQualitaUso(new Long(request.getParameterValues("idTipoQualitaUso")[i]));
	        utilizzoParticellaVO.setIdVarieta(new Long(request.getParameterValues("idVarieta")[i]));
	        catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
	          utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
	          utilizzoParticellaVO.getIdTipoQualitaUso());
	        utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
	          
	       if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]))
	       {
	         utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(request.getParameterValues("idTipoUtilizzoSecondario")[i]));
           utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(request.getParameterValues("idTipoDestinazioneSecondario")[i]));
           utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]));
           utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(request.getParameterValues("idTipoQualitaUsoSecondario")[i]));
           utilizzoParticellaVO.setIdVarietaSecondaria(new Long(request.getParameterValues("idVarietaSecondaria")[i]));
           catalogoMatriceVOSecondario = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(), 
             utilizzoParticellaVO.getIdVarietaSecondaria(), utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), 
             utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario());
           utilizzoParticellaVO.setIdCatalogoMatriceSecondario(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
	       }
	     }
	      
	      
	      
	      
	      //controlli semina 
	      /*if(!isKoUtilizzi)
	      {
	        Long idTipoPeriodoSemina = new Long(request.getParameterValues("idTipoPeriodoSemina")[i]);
	        //se passato i controlli la varietà e la semina sono valorizzati per forza!!!
	        TipoVarietaVO tipoVarietaVOSel = anagFacadeClient.findTipoVarietaByPrimaryKey(Long.decode(request.getParameterValues("idVarieta")[i]));
	        if(Validator.isNotEmpty(tipoVarietaVOSel.getIdTipoPeriodoSemina()))
	        {
		        if(tipoVarietaVOSel.getIdTipoPeriodoSemina().compareTo(new Long(0)) == 0)
		        {
		          if(idTipoPeriodoSemina.compareTo(new Long(0)) !=0)
		          {
		            errori.add("idTipoPeriodoSemina", new ValidationError(AnagErrors.ERRORE_PERIODO_SEMINA_ZERO));
		          }
		        }
		        else
		        {
		          if(idTipoPeriodoSemina.compareTo(new Long(0)) == 0)
		          {
		            errori.add("idTipoPeriodoSemina", new ValidationError(AnagErrors.ERRORE_PERIODO_SEMINA_MAGGIORE_ZERO));
		          }
		        }
		      }
		      
		      //se passi controlli deve essere per forza valorizzato se no errore..
          utilizzoParticellaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[i]));
		      utilizzoParticellaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[i]));
		      utilizzoParticellaVO.setDataInizioDestinazione(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazione")[i]));
		      utilizzoParticellaVO.setDataFineDestinazione(DateUtils.parseDate(request.getParameterValues("dataFineDestinazione")[i]));
		      
	        //se valorizzato utilizzo secondario obb periodo semina
	        if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")) 
	          && i < request.getParameterValues("idTipoUtilizzoSecondario").length
	          && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i])) 
	        {
	          Long idTipoPeriodoSeminaSecondario = new Long(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]);
	          TipoVarietaVO tipoVarietaSecVOSel = anagFacadeClient.findTipoVarietaByPrimaryKey(Long.decode(request.getParameterValues("idVarietaSecondaria")[i]));
	          if(Validator.isNotEmpty(tipoVarietaSecVOSel.getIdTipoPeriodoSemina()))
	          {
		          if(tipoVarietaSecVOSel.getIdTipoPeriodoSemina().compareTo(new Long(0)) == 0)
		          {
		            if(idTipoPeriodoSeminaSecondario.compareTo(new Long(0)) !=0)
		            {
		              errori.add("idTipoPeriodoSeminaSecondario", new ValidationError(AnagErrors.ERRORE_PERIODO_SEMINA_ZERO));
		            }
		          }
		          else
		          {
		            if(idTipoPeriodoSeminaSecondario.compareTo(new Long(0)) == 0)
		            {
		              errori.add("idTipoPeriodoSeminaSecondario", new ValidationError(AnagErrors.ERRORE_PERIODO_SEMINA_MAGGIORE_ZERO));
		            }
		          }
		        }
		        
		        utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]));
		        utilizzoParticellaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]));
		        utilizzoParticellaVO.setDataInizioDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazioneSec")[i]));
		        utilizzoParticellaVO.setDataFineDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataFineDestinazioneSec")[i]));
		        
		                
	        }
	        
	        if(errori.size() > 0) 
	        {
	          isKoUtilizzi = true;
	        }
	      
	      }	*/ 			
	 			 
	 			if(!isKoUtilizzi)
	 			{ 		 		
 	 			  if(Validator.isNotEmpty(particellaCertificataVO)
             && Validator.isNotEmpty(particellaCertificataVO.getIdParticellaCertificata()))
          {
            /*String flagPratoPermanente = "";
			      if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")) 
			         && i < request.getParameterValues("idTipoDettaglioUso").length
			         && Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])) 
			      {
			        TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(new Long(request.getParameterValues("idTipoDettaglioUso")[i]));
			        flagPratoPermanente = tipoDettaglioUsoVOSel.getFlagPratoPermanente();
			      }*/
			      
			      if(Validator.isNotEmpty(catalogoMatriceVO)
			        && Validator.isNotEmpty(storicoParticellaVO.getIdParticella()))
            {
              //TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
              String flagPratoPermanente = catalogoMatriceVO.getFlagPratoPermanente();
                
              boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                  particellaCertificataVO.getIdParticellaCertificata().longValue());
              
              String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(), 4);
	             
	            if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
	              && !"1".equalsIgnoreCase(valore))
	            {
	              if(!"S".equalsIgnoreCase(flagPratoPermanente))
	              {
	                errori.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
	              }
	            }
	          }
	          
	          if(Validator.isNotEmpty(catalogoMatriceVOSecondario)
	            && Validator.isNotEmpty(storicoParticellaVO.getIdParticella()))
            {
              //TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
              String flagPratoPermanente = catalogoMatriceVOSecondario.getFlagPratoPermanente();
                
              boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                  particellaCertificataVO.getIdParticellaCertificata().longValue());
              
              String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(), 4);
               
              if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
                && !"1".equalsIgnoreCase(valore))
              {
                if(!"S".equalsIgnoreCase(flagPratoPermanente))
                {
                  errori.add("idTipoDettaglioUsoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
                }
              }
            }             
          }   
		 	  } 			
	 			
	 			
	 			if(errori.size() > 0) 
	 			{
	 				isKoUtilizzi = true;
	 			}
	 			erroriUtilizzi.put(new Integer(i), errori);
	 			
	 			// se qui corretta la sup utilizzata
	 			if(!isKoUtilizzi)
	 			{
	 			  totSupUtilizzata += Double.parseDouble(request.getParameterValues("supUtilizzata")[i].replace(',', '.'));	 				
		 			totSupUtilizzata = NumberUtils.arrotonda(totSupUtilizzata,4);
		 	  }
				// Se l'utente ha scelto un tipo impianto allora ne ricerco i dati per
	      // poter effettuare i controlli incrociati con le piante consociate
	      TipoImpiantoVO tipoImpiantoVO = null;
	      if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i])) 
	      {
	        try 
	        {
	          tipoImpiantoVO = anagFacadeClient.findTipoImpiantoByPrimaryKey(Long.decode(request.getParameterValues("idImpianto")[i]));
	        }
	        catch(SolmrException se) 
	        {
	          error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_IMPIANTO);
	          errors.add("idImpianto", error);
				    request.setAttribute("errors", errors);
				    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
				    return;
	        }
	      }
	      
	      int numPianteConsociate = 0;
	      if(Validator.isNotEmpty(elencoPianteConsociate))
	      {
	        numPianteConsociate = elencoPianteConsociate.size();
	      }
	      if(numPianteConsociate != 0)  
	      {
	        for(int a = 0; a < numPianteConsociate; a++) 
	        {
	          UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
	          if(i == 0) 
	          {
	            ValidationErrors erroriCons = utilizzoConsociatoVO.validateModificaTerritorialeCondUso(request, null, tipoImpiantoVO, a);
	            if(erroriCons.size() > 0) 
	            {
	              isKoPiante = true;
	            }
	            erroriPianteConsociate.put(new Integer(a), erroriCons);
	          }
	          else 
	          {
	            ValidationErrors erroriCons = utilizzoConsociatoVO.validateModificaTerritorialeCondUso(request, null, tipoImpiantoVO, ((i*numPianteConsociate)+a));
	            if(erroriCons.size() > 0) 
	            {
	              isKoPiante = true;
	            }
	            erroriPianteConsociate.put(new Integer(((i*numPianteConsociate)+a)), erroriCons);
	          }
	        }
	 			}
	 		}
 		}
 		
 		
 		//controlli utilizzi efa
    int numeroUtilizziEfa = 0;
    if(Validator.isNotEmpty(elencoUtilizziEfaSelezionati))
    {
      numeroUtilizziEfa = elencoUtilizziEfaSelezionati.length;
    }
    
    for(int i=0;i<numeroUtilizziEfa;i++)
    {
      UtilizzoParticellaVO utilizzoParticellaEfaVO = new UtilizzoParticellaVO();
      errori = utilizzoParticellaEfaVO.validateInserisciTerritorialeCondUsoEfa(request, null, i);
      
      if(errori.size() > 0) 
      {
        isKoUtilizziEfa = true;
      }
      /*else
      {
        // se sono qui passati i controlli e sono valorizzati
        if(gaaFacadeClient.isDettaglioUsoObbligatorio(new Long(request.getParameterValues("idTipoEfa")[i]),
          new Long(request.getParameterValues("idVarietaEfa")[i]))
          && Validator.isEmpty(request.getParameterValues("idTipoDettaglioUsoEfa")[i]))
        {
          errori.add("idTipoDettaglioUsoEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          isKoUtilizziEfa = true;
        }      
      }*/
      
      
      if(Validator.isNotEmpty(request.getParameterValues("valoreDopoConversione")[i]) 
        && Validator.validateDouble(request.getParameterValues("valoreDopoConversione")[i], 9999999999.9999) != null 
        && Double.parseDouble(request.getParameterValues("valoreDopoConversione")[i].replace(',', '.')) > 0) 
      {
        String tmp = request.getParameterValues("valoreDopoConversione")[i];
        totSupUtilizzata += Double.parseDouble(request.getParameterValues("valoreDopoConversione")[i].replace(',', '.'));
        totSupUtilizzata = NumberUtils.arrotonda(totSupUtilizzata, 4);
      }      
      
      erroriUtilizziEfa.put(new Integer(i), errori);
    }

    
    
    
    //Controlli supCatastale
    if(session.getAttribute("flagObbligoCatastale") != null)
    {
	    if(errors.size() == 0 && !isKoUtilizzi) 
	    {	      
	      double supCatastaleDb = NumberUtils.arrotonda(Double.parseDouble(storicoParticellaVO.getSupCatastale().replace(',', '.')),4);
	      if(supCatastaleDb == 0)
	      {
	        errors.add("supCatastale", new ValidationError(AnagErrors.ERRORE_KO_SUP_CATASTALE_ZERO));
	      }    
	    
	    }
	  }
    
    
    
    
    //Controlli supCondotta e sup utilizzata
 		if(errors.size() == 0 && !isKoUtilizzi) 
    {
      String supConfronto = AnagUtils.valSupCatGraf(
         storicoParticellaVO.getSupCatastale(), storicoParticellaVO.getSuperficieGrafica());
      //per stabilizzati
      if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()) 
        && Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())  
        && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
        && ((Validator.isNotEmpty(idEvento)
        && (idEvento.compareTo(SolmrConstants.INSERIMENTO_ACCORPAMENTO) != 0))
        || Validator.isEmpty(idEvento)))
      {
        if(Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
          && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supConfronto = storicoParticellaVO.getSuperficieGrafica();
        }
      }     			
 			    
      double supCondottaDb = Double.parseDouble(request.getParameter("supCondotta").replace(',', '.'));
      BigDecimal supCondottaBg = new BigDecimal(request.getParameter("supCondotta").replace(',', '.'));
      if(supCondottaDb == 0)
      {
        errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_ZERO));
      }
      else
      {
        //Caso particella non provvisoria
        if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
        {
	        double maxSupGrafCatDb = Double.parseDouble(supConfronto.replace(',', '.'));
	        if(supCondottaDb > maxSupGrafCatDb)
	        {
	          errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MAX_CAT_GRAF));       
	        }
	        else
	        {
	          //se esite già altra conduzione!!!!
	          if(storicoParticellaVO.getIdParticella() != null)
	          {
	          
	            //Il controllo lo devo fare solo se nn reinserisco la stessa!!!
	            boolean controlloSup = true;
	            StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");         
	            if(Validator.isNotEmpty(elencoStoricoEvento)
	              && (elencoStoricoEvento.length > 0))
	            {
	              for(int i=0;i<elencoStoricoEvento.length;i++)
	              {
	                if(storicoParticellaVO.getIdParticella().compareTo(elencoStoricoEvento[i].getIdParticella()) == 0)
	                {
	                  controlloSup = false;
	                  break;
	                }               
	              }
	            }
	            
	            if(controlloSup)
	            {
	            
			          BigDecimal supCondottaAltreCondBg = gaaFacadeClient
			            .getSumSupCondottaAltreConduzioni(anagAziendaVO.getIdAzienda().longValue(), 
			            storicoParticellaVO.getIdParticella().longValue(), null);
			          supCondottaBg = supCondottaBg.add(supCondottaAltreCondBg);
			          
			          if(supCondottaBg.doubleValue() > maxSupGrafCatDb)
		            {
		              errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUM_SUPERFICIE_CONDUZIONE));
		            }
		            else
		            {
		              //Nel caso di diverso dell'assevimento devo controllare che la condotta 
			            //sia maggiore della somma della superficie degli utilizzi della medesima conduzione
			            if(Long.decode(request.getParameter("idTitoloPossesso")).intValue() != 5)
			            {
			              if(supCondottaDb < totSupUtilizzata)
			              {
			                errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
			              }
			            }
		            
		            }
		          }     
		          
		        }
	          else
	          {
	            //Nel caso di diverso dell'assevimento devo controllare che la condotta 
	            //sia maggiore della somma della superficie degli utilizzi della medesima conduzione
		          if(Long.decode(request.getParameter("idTitoloPossesso")).intValue() != 5)
		          {
		            if(supCondottaDb < totSupUtilizzata)
		            {
		              errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
		            }
		          }
		        }
	        }
	      }
	      //Particella provvisoria
	      else
	      {
	        //Nel caso di diverso dell'assevimento devo controllare che la condotta 
          //sia maggiore della somma della superficie degli utilizzi della medesima conduzione
          if(Long.decode(request.getParameter("idTitoloPossesso")).intValue() != 5)
          {
            if(supCondottaDb < totSupUtilizzata)
            {
              errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
            }
          }
	      }
      }
    
      
 			if((storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().intValue() != 5)
        && (totSupUtilizzata == 0))
      {
        request.setAttribute("confermaUsoSuolo", "confermaUsoSuolo");
        request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
        return;
      }  
        
      
 		}
		// Se si sono verificati errori li visualizzo
 		if(errors.size() > 0 || isKoUtilizzi || isKoPiante || isKoUtilizziEfa) 
    {
 			request.setAttribute("errors", errors);
 			request.setAttribute("erroriUtilizzi", erroriUtilizzi);
 			request.setAttribute("erroriPianteConsociate", erroriPianteConsociate);
 			request.setAttribute("erroriUtilizziEfa", erroriUtilizziEfa);
 	    request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
 	    return;
 		}
 		//Controlli supCatastale
 		//se sono qui i controlli sulla sup catstale sono andati tutti ok!!!
 		// Effettuo il controllo sul supero della superficie condotta da parte delle
    // superfici utilizzate solo se i controlli di correttezza formale sulla
    // superficie condotta sono stati superati
    String msgConfermaSupGlobale = "";
    if(session.getAttribute("flagObbligoCatastale") != null)
    {            
      double supCatastaleDb = NumberUtils.arrotonda(Double.parseDouble(storicoParticellaVO.getSupCatastale().replace(',', '.')),4);
      
      if(Validator.isEmpty(request.getParameter("confermaSupGlobale")))
      {     
	      if(idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)
	      {
	        StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
	        double supCatastaleOldDb = NumberUtils.arrotonda(Double.parseDouble(elencoStoricoEvento[0].getSupCatastale().replace(',', '.')), 4);
	        //Se la particella è cessata puà essere la superficie catastale a 0!!!!
	        //Quindi non devo fare nessun controllo
	        if(supCatastaleOldDb !=0)
	        {
	          if(supCatastaleDb > supCatastaleOldDb)
	          {
	            msgConfermaSupGlobale += "Attenzione: la superficie catastale risulta maggiore della superficie della/e particella/e frazionate.";		          
	          }
	        }
	      }
	    }
      
      if(Validator.isEmpty(request.getParameter("confermaSupGlobale")))
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
	            if(Validator.isEmpty(msgConfermaSupGlobale))
	            {
	              msgConfermaSupGlobale += "Attenzione: la superficie catastale risulta maggiore della superficie della/e particella/e accorpate.";
	            }
	            else
	            {
	              msgConfermaSupGlobale += "La superficie catastale risulta maggiore della superficie della/e particella/e accorpate.";
	            }
	          }
	        }        
	      }
	    }
      
      
      
    }
 		
 		if(Validator.isEmpty(request.getParameter("confermaSupGlobale"))) 
    {
 			if(NumberUtils.arrotonda(totSupUtilizzata, 4) 
 			  < NumberUtils.arrotonda(Double.parseDouble(storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta().replace(',', '.')), 4)) 
 			{
 			
 			  if(Validator.isEmpty(msgConfermaSupGlobale))
        {
          msgConfermaSupGlobale += "Attenzione: non e\\' stato dichiarato l\\'uso del suolo sulla totalita\\' della superficie condotta.";
        }
        else
        {
          msgConfermaSupGlobale += "Non e\\' stato dichiarato l\\'uso del suolo sulla totalita\\' della superficie condotta.";
        }
 			}
 		}
 		
 		if(Validator.isNotEmpty(msgConfermaSupGlobale))
 		{ 		
 		  request.setAttribute("confermaSupGlobale", "confermaSupGlobale");
 		  msgConfermaSupGlobale += "Vuoi proseguire?";
 		  request.setAttribute("msgConfermaSupGlobale", msgConfermaSupGlobale);
      request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
      return;
    }
 		
 		//UV selezionate
 		String[] arrVIdStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
 		String[] arrVAreaUV = request.getParameterValues("area");
 		String[] arrVIdStoricoUnitaArboreaCount = request.getParameterValues("idStoricoUnitaArboreaCount");
 		boolean isKoUv = false;
 		Vector<String> vArea = new Vector<String>();
 		//Nuovi controlli uv se frazionamento
 		if((idEvento.compareTo(SolmrConstants.INSERIMENTO_FRAZIONAMENTO) == 0)
 		  && Validator.isNotEmpty(arrVIdStoricoUnitaArborea) && (arrVIdStoricoUnitaArborea.length > 0)) 
 		{
 		  
 		  BigDecimal totSumUv = new BigDecimal(0);
 		  for(int g=0;g<arrVIdStoricoUnitaArborea.length;g++)
 		  {
 		    //mi prendo il valore dell'area coretto
 		    String areaTmp = "";
 		    for(int j=0;j<arrVIdStoricoUnitaArboreaCount.length;j++)
 		    {
 		      if(arrVIdStoricoUnitaArboreaCount[j].equalsIgnoreCase(arrVIdStoricoUnitaArborea[g]))
 		      {
 		        areaTmp = arrVAreaUV[j];
 		        vArea.add(areaTmp);
 		        break;
 		      }
 		    }
 		  
 		    errori = new ValidationErrors();
 		    if(Validator.isEmpty(areaTmp))
 		    {
 		      errori.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 		      isKoUv = true;
 		    }
 		    else
 		    {
 		      if(Validator.validateDouble(areaTmp, 999999.9999) == null) 
		      {
		        errori.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
		        isKoUv = true;
		      }
		      else
		      {
		        BigDecimal areaInput = new BigDecimal(areaTmp.replace(",", "."));
		        totSumUv = totSumUv.add(areaInput);
		        StoricoUnitaArboreaVO storicoUnitaArboreaVOTmp = anagFacadeClient.findStoricoUnitaArborea(new Long(arrVIdStoricoUnitaArborea[g]));
		        
		        
		        BigDecimal maxArea = gaaFacadeClient.getSumAreaMaxAssegnabile(storicoUnitaArboreaVOTmp.getIdUnitaArborea());
		        BigDecimal maxSommaGiaAssegnata = gaaFacadeClient.getSumAreaGiaAssegnata(storicoUnitaArboreaVOTmp.getIdUnitaArborea());
		      
		        areaInput = areaInput.add(maxSommaGiaAssegnata);
		        if(areaInput.compareTo(maxArea) > 0)
		        {
		          errori.add("area", new ValidationError("La superficie assegnata non puo'' essere maggiore di "+Formatter.formatDouble4(maxArea.subtract(maxSommaGiaAssegnata))));		        
		          isKoUv = true;
		        }
		        else if(totSumUv.compareTo(new BigDecimal(storicoParticellaVO.getSupCatastale().replace(',', '.'))) > 0)
		        {
		          errori.add("area", new ValidationError("La somma delle superfici delle unita'' vitate non può superare la superficie catastale"));
		          isKoUv = true;
		        }    
		      
		      }
 		    
 		    }
 		    
 		    erroriUv.put(new Integer(g), errori);
 		  }
 		
 		}
 		
 		if(isKoUv) 
    {
      request.setAttribute("erroriUv", erroriUv);
      request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
      return;
    }
 		
 		
 		
 		// Se ho passato tutti i controlli effettuo l'inserimento della particella
 		try 
    {
    
      
      // DATI UTILIZZO
	    Vector<UtilizzoParticellaVO> vUtilizzi = new Vector<UtilizzoParticellaVO>();
	    for(int i = 0; i < numeroUtilizzi; i++) 
	    {
	      UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	      utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
	      utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));
	      utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));
	      utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));
	      utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
        
        CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
            utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
            utilizzoParticellaVO.getIdTipoQualitaUso());
        utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
            
  
	      
	      utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzata")[i]));
	      
	      utilizzoParticellaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[i]));
	      utilizzoParticellaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[i]));
        utilizzoParticellaVO.setDataInizioDestinazione(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazione")[i]));
        utilizzoParticellaVO.setDataFineDestinazione(DateUtils.parseDate(request.getParameterValues("dataFineDestinazione")[i]));
	      
	      
	      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i])) 
	      {
	        utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(request.getParameterValues("idTipoUtilizzoSecondario")[i]));
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(request.getParameterValues("idTipoDestinazioneSecondario")[i]));
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]));
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(request.getParameterValues("idTipoQualitaUsoSecondario")[i]));
          utilizzoParticellaVO.setIdVarietaSecondaria(new Long(request.getParameterValues("idVarietaSecondaria")[i]));
          CatalogoMatriceVO catalogoMatriceVOSecondario = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(), 
             utilizzoParticellaVO.getIdVarietaSecondaria(), utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), 
             utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario());
          utilizzoParticellaVO.setIdCatalogoMatriceSecondario(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
	        utilizzoParticellaVO.setSupUtilizzataSecondaria(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzataSecondaria")[i]));
	        
	        
	        utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]));
          utilizzoParticellaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]));
          utilizzoParticellaVO.setDataInizioDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazioneSec")[i]));
          utilizzoParticellaVO.setDataFineDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataFineDestinazioneSec")[i]));
	        
	      }
	      else
	      {
	        utilizzoParticellaVO.setIdUtilizzoSecondario(null);
          utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
          utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
          utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
          utilizzoParticellaVO.setIdVarietaSecondaria(null);
          utilizzoParticellaVO.setIdCatalogoMatriceSecondario(null);
          utilizzoParticellaVO.setSupUtilizzataSecondaria(null);          
          
          utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
          utilizzoParticellaVO.setIdSeminaSecondario(null);
          utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
          utilizzoParticellaVO.setDataFineDestinazioneSec(null);
	      }
	      
	      
	      
	      utilizzoParticellaVO.setIdPraticaMantenimento(new Long(request.getParameterValues("idPraticaMantenimento")[i]));
	      
	      
	      if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i])) {
	        utilizzoParticellaVO.setIdFaseAllevamento(new Long(request.getParameterValues("idFaseAllevamento")[i]));
	      }
	      else {
	        utilizzoParticellaVO.setIdFaseAllevamento(null);
	      }
	      
	      
	      if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) 
	      {
	        utilizzoParticellaVO.setAnnoImpianto(request.getParameterValues("annoImpianto")[i]);
	      }
	      else 
	      {
	        utilizzoParticellaVO.setAnnoImpianto(null);
	      }
	      if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i])) {
	        utilizzoParticellaVO.setIdImpianto(Long.decode(request.getParameterValues("idImpianto")[i]));
	      }
	      else {
	        utilizzoParticellaVO.setIdImpianto(null);         
	      }
	      if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) {
	        utilizzoParticellaVO.setSestoSuFile(request.getParameterValues("sestoSuFile")[i]);
	      }
	      else {
	        utilizzoParticellaVO.setSestoSuFile(null);
	      }
	      if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) {
	        utilizzoParticellaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]);
	      }
	      else {
	        utilizzoParticellaVO.setSestoTraFile(null);
	      }
	      if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) {
	        utilizzoParticellaVO.setNumeroPianteCeppi(request.getParameterValues("numeroPianteCeppi")[i]);
	      }
	      else 
	      {
	        utilizzoParticellaVO.setNumeroPianteCeppi(null);
	      }
	      
	      
	      TipoEfaVO tipoEfaVO = gaaFacadeClient.getTipoEfaFromIdCatalogoMatrice(utilizzoParticellaVO.getIdCatalogoMatrice());
              
        if(Validator.isNotEmpty(tipoEfaVO))
        {
          utilizzoParticellaVO.setIdTipoEfa(new Long(tipoEfaVO.getIdTipoEfa()));
          BigDecimal supUtilizzata = new BigDecimal(utilizzoParticellaVO.getSuperficieUtilizzata().replace(",", ".")); 
          utilizzoParticellaVO.setValoreOriginale(supUtilizzata);
          utilizzoParticellaVO.setValoreDopoConversione(supUtilizzata.multiply(tipoEfaVO.getFattoreDiConversione()));
          BigDecimal valoreCoversione = utilizzoParticellaVO.getValoreDopoConversione();
          utilizzoParticellaVO.setValoreDopoPonderazione(valoreCoversione.multiply(tipoEfaVO.getFattoreDiPonderazione()));
        }
        else
        {
          utilizzoParticellaVO.setIdTipoEfa(null);
          utilizzoParticellaVO.setValoreOriginale(null);
          utilizzoParticellaVO.setValoreDopoConversione(null);
          utilizzoParticellaVO.setValoreDopoPonderazione(null);
        
        }
	      
	      
	      
	      // DATI UTILIZZO CONSOCIATI
	      UtilizzoConsociatoVO[] elencoUtilizziConsociati = null;
	      int numPianteConsociate = 0;
	      if(Validator.isNotEmpty(elencoPianteConsociate))
	      {
	        numPianteConsociate = elencoPianteConsociate.size();
	      }
	      
	      if(numPianteConsociate != 0)  
	      {
	        elencoUtilizziConsociati = new UtilizzoConsociatoVO[numPianteConsociate];
	        int valorePartenza = i*numPianteConsociate;
	        for(int a = 0; a < numPianteConsociate; a++) 
	        {
	          UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
	          if(i == 0) 
	          {
	            if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
	            {
	              utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[a]);
	              utilizzoConsociatoVO.setIdPianteConsociate(((TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(a)).getIdPianteConsociate());
	            }
	            else 
	            {
	              utilizzoConsociatoVO.setNumeroPiante(null);
	            }
	          }
	          else 
	          {
	            if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valorePartenza])) 
	            {
	              utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[valorePartenza]);
	              utilizzoConsociatoVO.setIdPianteConsociate(((TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(a)).getIdPianteConsociate());
	            }
	            else 
	            {
	              utilizzoConsociatoVO.setNumeroPiante(null);
	            }
	          }
	          valorePartenza++;
	          elencoUtilizziConsociati[a] = utilizzoConsociatoVO;
	        }
	      }
	      utilizzoParticellaVO.setElencoUtilizziConsociati(elencoUtilizziConsociati);
	      vUtilizzi.add(utilizzoParticellaVO);
	    }
	    
	    // DATI UTILIZZO EFA
	    Vector<UtilizzoParticellaVO> vUtilizziEfa = new Vector<UtilizzoParticellaVO>();
	    for(int i = 0; i < numeroUtilizziEfa; i++) 
	    {
	      UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
	      utilizzoParticellaVO.setDichiarabileEfa("S");
	      
	      utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("valoreDopoConversione")[i]));
	        
	      utilizzoParticellaVO.setIdTipoEfa(Long.decode((request.getParameterValues("idTipoEfa")[i])));
	      utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzoEfa")[i]));
	      utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazioneEfa")[i]));
	      utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUsoEfa")[i]));
	      utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUsoEfa")[i]));
	      utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarietaEfa")[i]));
	     
	      CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
            utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
            utilizzoParticellaVO.getIdTipoQualitaUso());
        utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
	     
	     
	      utilizzoParticellaVO.setValoreOriginale(new BigDecimal(request.getParameterValues("valoreOriginale")[i].replace(",", ".")));
	      utilizzoParticellaVO.setValoreDopoConversione(new BigDecimal(request.getParameterValues("valoreDopoConversione")[i].replace(",", ".")));
	      utilizzoParticellaVO.setValoreDopoPonderazione(new BigDecimal(request.getParameterValues("valoreDopoPonderazione")[i].replace(",", ".")));     
	      
	      if(Validator.isNotEmpty(utilizzoParticellaVO.getValoreOriginale())
	        && Validator.isEmpty(utilizzoParticellaVO.getValoreDopoConversione()))
	      {
	        TipoEfaVO tipoEfaVOSel = gaaFacadeClient.getTipoEfaFromPrimaryKey(utilizzoParticellaVO.getIdTipoEfa());
	      
	        Integer abbaPonderazione = gaaFacadeClient.getAbbPonderazioneByMatrice(utilizzoParticellaVO.getIdTipoEfa(), 
	          utilizzoParticellaVO.getIdUtilizzo(), utilizzoParticellaVO.getIdTipoDestinazione(),
	          utilizzoParticellaVO.getIdTipoDettaglioUso(), utilizzoParticellaVO.getIdTipoQualitaUso(),
	          utilizzoParticellaVO.getIdVarieta());
	          
	        utilizzoParticellaVO.setAbbaPonderazione(abbaPonderazione);
	        
	
	        BigDecimal valoreDopoConversione = utilizzoParticellaVO.getValoreOriginale().multiply(tipoEfaVOSel.getFattoreDiConversione());
	        valoreDopoConversione = valoreDopoConversione.divide(new BigDecimal(10000), 4,BigDecimal.ROUND_HALF_UP);
	        utilizzoParticellaVO.setValoreDopoConversione(valoreDopoConversione);
	        BigDecimal valoreDopoPonderazione = valoreDopoConversione.multiply(tipoEfaVOSel.getFattoreDiPonderazione());
	        valoreDopoPonderazione = valoreDopoPonderazione.multiply(new BigDecimal(abbaPonderazione.intValue()));	        
	        utilizzoParticellaVO.setValoreDopoPonderazione(valoreDopoPonderazione);     
	      }
	      
	      
	      
	      vUtilizziEfa.add(utilizzoParticellaVO);
	    }
	      
	    
	    int numeroTotaleUtilizzi = vUtilizzi.size();
	    numeroTotaleUtilizzi = numeroTotaleUtilizzi + vUtilizziEfa.size();
	    
	    UtilizzoParticellaVO[] elencoUtilizzi = null;
	    int contatore = 0;
	    elencoUtilizzi = new UtilizzoParticellaVO[numeroTotaleUtilizzi];
	    for(int i=0;i<vUtilizzi.size();i++)
	    {
	      elencoUtilizzi[contatore] = vUtilizzi.get(i); 
	      contatore++;
	    }
	      
	    for(int i=0;i<vUtilizziEfa.size();i++)
	    {
	      elencoUtilizzi[contatore] = vUtilizziEfa.get(i); 
	      contatore++;
	    }
	    
	    storicoParticellaVO.getElencoConduzioni()[0].setElencoUtilizzi(elencoUtilizzi);
    
    
    
      session.removeAttribute("flagObbligoCatastale");      
     
      String[] arrVAreaOutUV = null;
      if(vArea.size() > 0)
      {
        arrVAreaOutUV = new String[vArea.size()];
        for(int k=0;k<vArea.size();k++)
        {
          arrVAreaOutUV[k] = vArea.get(k);
        }
      }
 			String msg = anagFacadeClient.inserisciParticella(storicoParticellaVO, ruoloUtenza, elencoParticelleEvento, 
        idEvento, anagAziendaVO.getIdAzienda(), arrVIdStoricoUnitaArborea, arrVAreaOutUV);
      //Ci sono stati de problemi con l'evento inserito
      if (msg!=null)
      {
        error = new ValidationError(msg);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
        return;
      }
 		}
 		catch(SolmrException se) {
			error = new ValidationError(AnagErrors.ERRORE_KO_INSERIMENTO_PARTICELLA);
			errors.add("error", error);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher(terreniParticellareInserisciCondUsoUrl).forward(request, response);
			return;
		}
 		
		
		storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
    session.removeAttribute("storicoParticellaVO");
    session.removeAttribute("elencoStoricoEvento");
    StoricoParticellaVO nuovoStoricoParticellaVO = new StoricoParticellaVO();
    ConduzioneParticellaVO nuovoConduzioneParticellaVO = new ConduzioneParticellaVO();
    nuovoStoricoParticellaVO.setComuneParticellaVO(storicoParticellaVO.getComuneParticellaVO());
    nuovoConduzioneParticellaVO.setIdUte(storicoParticellaVO.getElencoConduzioni()[0].getIdUte());
    nuovoStoricoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{nuovoConduzioneParticellaVO});
    nuovoStoricoParticellaVO.setSezione(storicoParticellaVO.getSezione());
    nuovoStoricoParticellaVO.setFoglio(storicoParticellaVO.getFoglio());
    session.setAttribute("storicoParticellaVO", nuovoStoricoParticellaVO);
    // Vado alla pagina di inserimento particella(primo step)
    %>
      <jsp:forward page= "<%= terreniParticellareInserisciCtrlUrl %>" >
        <jsp:param name="inserimento" value="inserimento"/>
      </jsp:forward>
   <%
  
		
 	}


	// Vado alla pagina di inserimento conduzione/uso
	%>
		<jsp:forward page="<%=terreniParticellareInserisciCondUsoUrl%>" />
	
