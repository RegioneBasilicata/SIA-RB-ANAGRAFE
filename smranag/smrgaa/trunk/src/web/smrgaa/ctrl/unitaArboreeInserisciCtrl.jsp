<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>




<%
	
	String iridePageName = "unitaArboreeInserisciCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String unitaArboreeInserisciUrl = "/view/unitaArboreeInserisciView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica unità arboree "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	String idUtilizzo = request.getParameter("idUtilizzo");
	String idTipoDestinazione = request.getParameter("idTipoDestinazione");
	String idTipoDettaglioUso = request.getParameter("idTipoDettaglioUso");
	String idTipoQualitaUso = request.getParameter("idTipoQualitaUso");
  String idVarieta = request.getParameter("idVarieta");
	StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
	ValidationErrors errors = new ValidationErrors();
	ValidationError error = null;
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	String[] elencoIdVarietaVitigno = request.getParameterValues("idVarietaVitigno");
	String[] elencoPercentuale = request.getParameterValues("percentuale");
	ValidationErrors errori = new ValidationErrors();
	int countErroriPercentuale = 0;
	int totPercentualeAltriVitigni = 0;
	Hashtable<Integer,ValidationErrors> erroriAltriVitigni = new Hashtable<Integer,ValidationErrors>();
	//se questo campo è valorizzato è la prima volta che entro nel duplica!!!
	Long idStoricoUnitaArborea = null;
	boolean primaVoltaDuplica = false;
	
	
	
	
	String parametroInserisciNuovaUv = null;
  try 
  {
    parametroInserisciNuovaUv = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INSERISCI_NUOVA_UV);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INSERISCI_NUOVA_UV+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
	
	
	
	
	//caso duplica....
	if(Validator.isNotEmpty(request.getParameter("idUnita")))
	{
	  idStoricoUnitaArborea = Long.decode(request.getParameter("idUnita"));
	  storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStoricoUnitaArborea);	  
	  session.setAttribute("storicoParticellaVO", storicoParticellaVO);
	  storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
	  BigDecimal areaTmp = new BigDecimal(0);
	  if(storicoParticellaVO.getParticellaCertificataVO() != null
	    && storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata() != null)
	  {
	    areaTmp = areaTmp.add(gaaFacadeClient.getSupEleggibilePlSql(
	      storicoParticellaVO.getParticellaCertificataVO().getIdParticellaCertificata().longValue(), 
	      storicoUnitaArboreaVO.getIdCatalogoMatrice()));
	     
	    BigDecimal sumArea = anagFacadeClient.getSumAreaUVParticella(anagAziendaVO.getIdAzienda().longValue(),
	      storicoParticellaVO.getIdParticella().longValue());
	    if(sumArea != null)
	    {
	      areaTmp = areaTmp.subtract(sumArea);
	    }
	    
	    if(areaTmp.compareTo(new BigDecimal(0)) > 0 )
	    {
	      storicoUnitaArboreaVO.setArea(StringUtils.parseSuperficieFieldBigDecimal(areaTmp));
	    }
	    else
	    {
	      storicoUnitaArboreaVO.setArea(null);
	    }
	  }
	  
	  
	  session.setAttribute("storicoParticellaVODup", storicoUnitaArboreaVO);
	  
	  if(storicoUnitaArboreaVO.getElencoAltriVitigni() != null)
	  {
	    session.setAttribute("elencoAltriVitigni", storicoUnitaArboreaVO.getElencoAltriVitigni());
	  }
	   
	  
	  primaVoltaDuplica = true;
	}
	
	
	
	if("S".equalsIgnoreCase(parametroInserisciNuovaUv))
  {
  
    if(Validator.isNotEmpty(storicoParticellaVO) 
      && gaaFacadeClient.isAltreUvDaSchedario(storicoParticellaVO.getIdParticella().longValue()))
    {
      String messaggio = AnagErrors.ERRORE_KO_INSERISCI_CON_SCHEDARIO;
      String particellaStr = "Comune "+storicoParticellaVO.getComuneParticellaVO().getDescom();
      if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
        particellaStr += " Sz. " +storicoParticellaVO.getSezione();
      }
      particellaStr += " Fgl. "+storicoParticellaVO.getFoglio();
      if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
        particellaStr += " Part. " +storicoParticellaVO.getParticella();
      }
      if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
        particellaStr += " Sub. " +storicoParticellaVO.getSubalterno();
      }
      
      messaggio = messaggio.replaceAll("<<PARTICELLA>>", particellaStr);
      
      error = new ValidationError(messaggio);
      request.setAttribute("error", error);
      %>
        <jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
      <%
    }
  }
	
	
	
	

	// Recupero i valori relativi alla destinazione produttiva
	TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
	try 
	{
		String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
 		elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_VIGNETO, true, orderBy);
 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
	}
	catch(SolmrException se) 
	{
		error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA);
		errors.add("idUtilizzo", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
   	return;
	}
  
  // Recupero il parametro che mi indica l'obbligatorietà di un campo o meno
  boolean datiObbligatori=false;
  try 
  {
    String parametroAlbo = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_ALBO);
    if (SolmrConstants.FLAG_S.equals(parametroAlbo)) datiObbligatori=true;
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  // Recupero il parametro che mi indica l'obbligatioità di alcuni dati dell'idoneità
  String parametroDAID = null;
  try 
  {
    parametroDAID = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DAID);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DAID+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  String parametroAccessoDtSovr = null;
  try 
  {
    parametroAccessoDtSovr = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_DT_SOVR);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_DT_SOVR+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("parametroAccessoDtSovr", parametroAccessoDtSovr);
	
	// Recupero il vitigno in funzione della destinazione produttiva selezionata e del
	// comune di riferimento
 	//Vector<TipoVarietaVO> elencoVarieta = null;
 	if(primaVoltaDuplica)
 	{
 	  if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdCatalogoMatrice()))
 	  {
 	    CatalogoMatriceVO catalogoMatriceVOTmp = gaaFacadeClient
 	      .getCatalogoMatriceFromPrimariKey(storicoUnitaArboreaVO.getIdCatalogoMatrice());
 	    //se la matrice è attiva carico i valori....
 	    if(Validator.isEmpty(catalogoMatriceVOTmp.getDataFineValidita()))
 	    {
	 	    idUtilizzo = new Long(catalogoMatriceVOTmp.getIdUtilizzo()).toString();
	 	    idTipoDestinazione = new Long(catalogoMatriceVOTmp.getIdTipoDestinazione()).toString();
	 	    idTipoDettaglioUso = new Long(catalogoMatriceVOTmp.getIdTipoDettaglioUso()).toString();
	 	    idTipoQualitaUso = new Long(catalogoMatriceVOTmp.getIdTipoQualitaUso()).toString();
	 	    idVarieta = storicoUnitaArboreaVO.getIdVarieta().toString();
	 	  }
    }
 	}
 	try 
  {
 		if(Validator.isNotEmpty(idUtilizzo)) 
    {
      Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(idUtilizzo));
      request.setAttribute("vTipoDestinazione", vTipoDestinazione);
    }
    
    if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione))
    {
      Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(
            Long.decode(idUtilizzo), Long.decode(idTipoDestinazione));
      request.setAttribute("vTipoDettaglioUso", vTipoDettaglioUso);
    }
    
    if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione)
      && Validator.isNotEmpty(idTipoDettaglioUso))
    {
      Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(
            Long.decode(idUtilizzo), Long.decode(idTipoDestinazione), Long.decode(idTipoDettaglioUso));
      request.setAttribute("vTipoQualitaUso", vTipoQualitaUso);
    }
    
    if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione)
      && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso))
    {
      Vector<TipoVarietaVO> vTipoVarieta = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(
            Long.decode(idUtilizzo), Long.decode(idTipoDestinazione), Long.decode(idTipoDettaglioUso), 
            Long.decode(idTipoQualitaUso), storicoParticellaVO.getIstatComune());
      if(vTipoVarieta == null)
      {
        vTipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
          Long.decode(idUtilizzo), Long.decode(idTipoDestinazione), Long.decode(idTipoDettaglioUso), 
            Long.decode(idTipoQualitaUso));
      }      
            
      request.setAttribute("vTipoVarieta", vTipoVarieta);
    }
 	}
 	catch(SolmrException se) 
  {
		error = new ValidationError(AnagErrors.ERRORE_KO_VITIGNO);
		errors.add("idVarieta", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
     	return;
	}
 	
	// Recupero le forme allevamento
 	TipoFormaAllevamentoVO[] elencoFormeAllevamento = null;
 	try 
  {
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		elencoFormeAllevamento = anagFacadeClient.getListTipoFormaAllevamento(true, orderBy);
 		request.setAttribute("elencoFormeAllevamento", elencoFormeAllevamento);
 	}
 	catch(SolmrException se) 
  {
 		error = new ValidationError(AnagErrors.ERRORE_KO_FORME_ALLEVAMENTO);
		errors.add("idFormaAllevamento", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
    return;
 	}
 	
	// Recupero le causali modifica
 	TipoCausaleModificaVO[] elencoCausaleModifica = null;
 	try 
 	{
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		elencoCausaleModifica = anagFacadeClient.getListTipoCausaleModifica(true, orderBy);
 		request.setAttribute("elencoCausaleModifica", elencoCausaleModifica);
 	}
 	catch(SolmrException se) 
 	{
 		error = new ValidationError(AnagErrors.ERRORE_KO_CAUSALE_MOD_UNITA_ARBOREA);
		errors.add("idCausaleModifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
    return;
 	}
 	
 	// Varietà altri vitigni
	//Vector<TipoVarietaVO> elencoVarietaVitigni = null;
	if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idTipoDestinazione)
	  && Validator.isNotEmpty(idTipoDettaglioUso) && Validator.isNotEmpty(idTipoQualitaUso)) 
	{
		try 
		{
			Vector<TipoVarietaVO> elencoVarietaVitigni = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(
            Long.decode(idUtilizzo), Long.decode(idTipoDestinazione), Long.decode(idTipoDettaglioUso), 
            Long.decode(idTipoQualitaUso), storicoParticellaVO.getIstatComune());
      if(elencoVarietaVitigni == null)
      {
        elencoVarietaVitigni = gaaFacadeClient.getElencoTipoVarietaByMatrice(
          Long.decode(idUtilizzo), Long.decode(idTipoDestinazione), Long.decode(idTipoDettaglioUso), 
            Long.decode(idTipoQualitaUso));
      }      
			request.setAttribute("elencoVarietaVitigni", elencoVarietaVitigni);
		}
		catch(SolmrException se) 
		{
			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_VARIETA);
			errors.add("idVarietaVitigno", error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
	    return;
		}
	}
  
  
  Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = null;
  if((storicoParticellaVO != null) && (storicoParticellaVO.getComuneParticellaVO() != null)
    && (storicoParticellaVO.getComuneParticellaVO().getIstatComune() != null))
  {
    try 
    {
      if(Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(idVarieta)) 
      {
        vTipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComuneAndVarieta(
          storicoParticellaVO.getComuneParticellaVO().getIstatComune(), Long.decode(idVarieta), storicoParticellaVO.getIdParticella());
        request.setAttribute("vTipoTipologiaVino", vTipoTipologiaVino);
      }
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeInserisciCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  
  
  
  
	//  L'utente ha modificato il valore della destinazione produttiva
 	if(Validator.isNotEmpty(request.getParameter("cambio"))) 
  {
		// ALTRI VITIGNI
		storicoUnitaArboreaVO.setPresenzaAltriVitigni(request.getParameter("altriVitigni"));
		// FLAG IMPRODUTTIVA
    storicoUnitaArboreaVO.setFlagImproduttiva(request.getParameter("flagImproduttiva"));
		// COLTURA SPECIALIZZATA
		storicoUnitaArboreaVO.setColturaSpecializzata(request.getParameter("colturaSpecializzata"));
		// RICADUTA
		storicoUnitaArboreaVO.setRicaduta(request.getParameter("ricaduta"));
		// DESTINAZIONE PRODUTTIVA
		if(Validator.isNotEmpty(idUtilizzo)) 
    {
			storicoUnitaArboreaVO.setIdUtilizzo(Long.decode(idUtilizzo)); 					
		}
		else 
    {
			storicoUnitaArboreaVO.setIdUtilizzo(null); 
		}
		if(Validator.isNotEmpty(idTipoDestinazione)) 
    {
      storicoUnitaArboreaVO.setIdTipoDestinazione(Long.decode(idTipoDestinazione));
    }
    else {
      storicoUnitaArboreaVO.setIdTipoDestinazione(null);
    }
    
    if(Validator.isNotEmpty(idTipoDettaglioUso)) 
    {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(Long.decode(idTipoDettaglioUso));
    }
    else {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(null);
    }
    
    if(Validator.isNotEmpty(idTipoQualitaUso)) 
    {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(Long.decode(idTipoQualitaUso));
    }
    else {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(null);
    }
    
    if(Validator.isNotEmpty(idVarieta)) 
    {
      storicoUnitaArboreaVO.setIdVarieta(Long.decode(idVarieta));
    }
    else 
    {
      storicoUnitaArboreaVO.setIdVarieta(null);
    }
		// DATA IMPIANTO
		if(Validator.isNotEmpty(request.getParameter("dataImpianto"))) 
    {
      try
      {
        storicoUnitaArboreaVO.setDataImpianto(DateUtils
          .parseDate(request.getParameter("dataImpianto")));
      }
      catch(Exception ex)
      {
        storicoUnitaArboreaVO.setDataImpianto(null);
      }					
		}
		else 
    {
			storicoUnitaArboreaVO.setDataImpianto(null); 
		}
		// ANNO RIFERIMENTO
		if(Validator.isNotEmpty(request.getParameter("annoRiferimento"))) 
		{
			storicoUnitaArboreaVO.setAnnoRiferimento(request.getParameter("annoRiferimento")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setAnnoRiferimento(null); 
		}
    // DATA PRIMA PRODUZIONE
    if(Validator.isNotEmpty(request.getParameter("dataPrimaProduzione"))) 
    {
      try
      {
        storicoUnitaArboreaVO.setDataPrimaProduzione(DateUtils
          .parseDate(request.getParameter("dataPrimaProduzione")));
      }
      catch(Exception ex)
      {
        storicoUnitaArboreaVO.setDataPrimaProduzione(null);
      }             
    }
    else {
      storicoUnitaArboreaVO.setDataPrimaProduzione(null); 
    }
    // DATA SOVRAINNESTO
    if(Validator.isNotEmpty(request.getParameter("dataSovrainnesto"))) 
    {
      try
      {
        storicoUnitaArboreaVO.setDataSovrainnesto(DateUtils
          .parseDate(request.getParameter("dataSovrainnesto")));
      }
      catch(Exception ex)
      {
        storicoUnitaArboreaVO.setDataSovrainnesto(null);
      }             
    }
    else {
      storicoUnitaArboreaVO.setDataSovrainnesto(null); 
    }
		// VITIGNO
    //Se non valorizzato vuol dire che ho cambiato solo l'uso
    // e l'idVarieta deve essere annullato in quanto non è selezionato
    /*if(Validator.isEmpty(request.getParameter("cambioVarieta")))
    {
      storicoUnitaArboreaVO.setIdVarieta(null);
    }
    else
    { 
  		if(Validator.isNotEmpty(request.getParameter("idVarieta"))) 
  		{
  			storicoUnitaArboreaVO.setIdVarieta(Long.decode(request.getParameter("idVarieta")));
  		}
  		else 
  		{
  			storicoUnitaArboreaVO.setIdVarieta(null); 
  		}
    }*/
		// SESTO SU FILA
		if(Validator.isNotEmpty(request.getParameter("sestoSuFila"))) 
		{
			storicoUnitaArboreaVO.setSestoSuFila(request.getParameter("sestoSuFila")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoSuFila(null); 
		}
		// SUPERFICIE VITATA
		if(Validator.isNotEmpty(request.getParameter("area"))) 
		{
			storicoUnitaArboreaVO.setArea(request.getParameter("area")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setArea(null); 
		}
		// SESTO TRA FILE
		if(Validator.isNotEmpty(request.getParameter("sestoTraFile"))) 
		{
			storicoUnitaArboreaVO.setSestoTraFile(request.getParameter("sestoTraFile")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoTraFile(null); 
		}
		// PERCENTUALE VITIGNO
		if(Validator.isNotEmpty(request.getParameter("percentualeVitigno"))) 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(request.getParameter("percentualeVitigno")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(null); 
		}
		// PERCENTUALE FALLANZA
		if(Validator.isNotEmpty(request.getParameter("percentualeFallanza"))
		  && Validator.isNumericInteger(request.getParameter("percentualeFallanza"))) 
    {
      storicoUnitaArboreaVO.setPercentualeFallanza(
        new BigDecimal(request.getParameter("percentualeFallanza")));          
    }
    else 
    {
      storicoUnitaArboreaVO.setPercentualeFallanza(new BigDecimal(0)); 
    }
		// NUMERO CEPPI
		if(Validator.isNotEmpty(request.getParameter("numeroCeppi"))) 
		{
			storicoUnitaArboreaVO.setNumCeppi(request.getParameter("numeroCeppi")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setNumCeppi(null); 
		}
		// FORMA ALLEVAMENTO
		if(Validator.isNotEmpty(request.getParameter("idFormaAllevamento"))) 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(Long.decode(request.getParameter("idFormaAllevamento"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(null); 
		}
		// MOTIVO INSERIMENTO
		if(Validator.isNotEmpty(request.getParameter("idCausaleModifica"))) 
		{
			storicoUnitaArboreaVO.setIdCausaleModifica(Long.decode(request.getParameter("idCausaleModifica"))); 					
		}
		else {
			storicoUnitaArboreaVO.setIdCausaleModifica(null); 
		}
		// ALTRI VITIGNI
		AltroVitignoVO[] elencoAltriVitigni = (AltroVitignoVO[])session.getAttribute("elencoAltriVitigni");
		if(elencoAltriVitigni != null && elencoAltriVitigni.length > 0) 
		{
			for(int i = 0; i < elencoAltriVitigni.length; i++) 
			{
				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigni[i];
				if(Validator.isNotEmpty(elencoIdVarietaVitigno[i])) 
				{
					altroVitignoVO.setIdVarieta(Long.decode(elencoIdVarietaVitigno[i]));
				}
				else 
				{
					altroVitignoVO.setIdVarieta(null);
				}
				if(Validator.isNotEmpty(elencoPercentuale[i])) 
				{
					altroVitignoVO.setPercentualeVitigno(elencoPercentuale[i]);
				}
				else 
				{
					altroVitignoVO.setPercentualeVitigno(null);
				}
				elencoAltriVitigni[i] = altroVitignoVO;
				storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltriVitigni);
			}
		}
 	}
 	// L'utente ha cliccato il pulsante conferma
 	else if((request.getParameter("conferma") != null)
     ||  (request.getParameter("confermaPA") != null))
  {
    //se settato anche se trattasi di PA non deve essere fatta la validazione
    String provenienza = null;
    if(request.getParameter("confermaPA") != null)
    {
      provenienza = SolmrConstants.CONFERMA_PA;
    } 
  
    //Sono nel caso duplica
    if(session.getAttribute("storicoParticellaVODup") !=  null)
    {
      storicoUnitaArboreaVO = (StoricoUnitaArboreaVO)session.getAttribute("storicoParticellaVODup");
    }
    
 		AltroVitignoVO[] elencoAltriVitigni = (AltroVitignoVO[])session.getAttribute("elencoAltriVitigni");
 		// Recupero i parametri ed effettuo i controlli di validita:
		// ALTRI VITIGNI
		storicoUnitaArboreaVO.setPresenzaAltriVitigni(request.getParameter("altriVitigni"));
		// FLAG IMPRODUTTIVA
    storicoUnitaArboreaVO.setFlagImproduttiva(request.getParameter("flagImproduttiva"));
		// COLTURA SPECIALIZZATA
		storicoUnitaArboreaVO.setColturaSpecializzata(request.getParameter("colturaSpecializzata"));
		// RICADUTA
		storicoUnitaArboreaVO.setRicaduta(request.getParameter("ricaduta"));
 		// Se presenza altri vitigni == N ...
		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
    {
			//... non deve essere possibile che siano stati inseriti altri vitigni
			if(elencoAltriVitigni != null && elencoAltriVitigni.length > 0) 
			{
				errors.add("altroVitignoN", new ValidationError(AnagErrors.ERRORE_PRESENZA_ALTRI_VITIGNI_N));
			}
		}
		// Se presenza altri vitigni == S ...
		else 
    {
			//... non deve essere possibile che non siano stati inseriti altri vitigni
			if(elencoAltriVitigni == null || elencoAltriVitigni.length == 0) 
			{
				errors.add("altroVitignoS", new ValidationError(AnagErrors.ERRORE_PRESENZA_ALTRI_VITIGNI_S));
			}
		}
		// DESTINAZIONE PRODUTTIVA
		if(Validator.isNotEmpty(request.getParameter("idUtilizzo"))) 
		{
			storicoUnitaArboreaVO.setIdUtilizzo(Long.decode(request.getParameter("idUtilizzo")));
			
			if(Validator.isNotEmpty(request.getParameter("idTipoDestinazione")) && Validator.isNotEmpty(request.getParameter("idTipoDettaglioUso"))
        && Validator.isNotEmpty(request.getParameter("idTipoQualitaUso")) && Validator.isNotEmpty(request.getParameter("idVarieta")))
      { 
     
        CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(storicoUnitaArboreaVO.getIdUtilizzo(),
          new Long(request.getParameter("idVarieta")), new Long(request.getParameter("idTipoDestinazione")), 
          new Long(request.getParameter("idTipoDettaglioUso")), new Long(request.getParameter("idTipoQualitaUso")));
			
				if(ruoloUtenza.isUtenteIntermediario() && Validator.isNotEmpty(catalogoMatriceVO))
				{
					if("N".equalsIgnoreCase(catalogoMatriceVO.getFlagUnarModificabile()))
				  {
				    errors.add("idUtilizzo", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
				  }
				}
		  }
			
			
			 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdUtilizzo(null); 
			errors.add("idUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		
		// VITIGNO
    if(Validator.isNotEmpty(request.getParameter("idVarieta"))) 
    {
      storicoUnitaArboreaVO.setIdVarieta(Long.decode(request.getParameter("idVarieta")));
    }
    else 
    {
      storicoUnitaArboreaVO.setIdVarieta(null); 
      errors.add("idVarieta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
    // Destinazione
    if(Validator.isNotEmpty(request.getParameter("idTipoDestinazione"))) 
    {
      storicoUnitaArboreaVO.setIdTipoDestinazione(Long.decode(request.getParameter("idTipoDestinazione")));            
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoDestinazione(null); 
      errors.add("idTipoDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    // Dettaglio uso
    if(Validator.isNotEmpty(request.getParameter("idTipoDettaglioUso"))) 
    {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(Long.decode(request.getParameter("idTipoDettaglioUso")));            
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(null);
      errors.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    // Qualita uso
    if(Validator.isNotEmpty(request.getParameter("idTipoQualitaUso"))) 
    {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(Long.decode(request.getParameter("idTipoQualitaUso")));            
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(null);
      errors.add("idTipoQualitaUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
    
    
		// DATA IMPIANTO
		boolean isOkImpianto = false;
		if(Validator.isNotEmpty(request.getParameter("dataImpianto"))) 
    {			
			if(!Validator.validateDateF(request.getParameter("dataImpianto"))) 
      {
				errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			else 
      {
        storicoUnitaArboreaVO.setDataImpianto(
          DateUtils.parseDate(request.getParameter("dataImpianto")));
        storicoUnitaArboreaVO.setAnnoImpianto(new Integer(DateUtils
            .extractYearFromDate(storicoUnitaArboreaVO.getDataImpianto())).toString());
				// La data impianto non può essere maggiore dell'anno di sistema
        if(storicoUnitaArboreaVO.getDataImpianto().after(new Date())) 
        {
          errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MAGGIORE_DATA_CORRENTE));
        }
				// La data impianto non deve essere minore del 1900
        else if(storicoUnitaArboreaVO.getDataImpianto().before(DateUtils.parseDate("31/12/1900"))) 
        {
          errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MINORE_1900));
        }
				else 
        {
					isOkImpianto = true;
				}
			}
		}
		else 
    {
      storicoUnitaArboreaVO.setDataImpianto(null);
			storicoUnitaArboreaVO.setAnnoImpianto(null); 
			errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		// ANNO RIFERIMENTO
		if(Validator.isNotEmpty(request.getParameter("annoRiferimento"))) 
    {
			storicoUnitaArboreaVO.setAnnoRiferimento(request.getParameter("annoRiferimento")); 					
			if(!Validator.isNumericInteger(storicoUnitaArboreaVO.getAnnoRiferimento())) 
      {
				errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			else 
      {
				// L'anno riferimento non può essere maggiore dell'anno di sistema
				if(Integer.parseInt(storicoUnitaArboreaVO.getAnnoRiferimento()) > DateUtils.getCurrentYear().intValue()) {
					errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_KO_ANNO_RIFERIMENTO_UV_MAGGIORE_ANNO_CORRENTE));
				}
				// L'anno riferimento non deve essere minore del 1900
				else if(Integer.parseInt(storicoUnitaArboreaVO.getAnnoRiferimento()) < 1900) {
					errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}
				// L'anno di riferimento non può essere minore dell'anno di impianto
				if(isOkImpianto) 
        {
					if(Integer.parseInt(storicoUnitaArboreaVO.getAnnoImpianto()) > Integer.parseInt(storicoUnitaArboreaVO.getAnnoRiferimento())) {
						errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_KO_ANNO_RIFERIMENTO_UV_MINORE_ANNO_IMPIANTO));
					}
				}
			}
		}
		else 
    {
			storicoUnitaArboreaVO.setAnnoRiferimento(null); 
			errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		// DATA SOVRAINNESTO
		boolean isOkSovrainnesto = false;
    if(Validator.isNotEmpty(request.getParameter("dataSovrainnesto"))) 
    {          
      if(!Validator.validateDateF(request.getParameter("dataSovrainnesto"))) 
      {
        errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
      else 
      {
        storicoUnitaArboreaVO.setDataSovrainnesto(
          DateUtils.parseDate(request.getParameter("dataSovrainnesto")));
        // La data sovrainnesto non può essere maggiore dell'anno di sistema
        if(storicoUnitaArboreaVO.getDataSovrainnesto().after(new Date())) 
        {
          errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MAGGIORE_DATA_CORRENTE));
        }
        // La data impianto non deve essere minore del 1900
        else if(storicoUnitaArboreaVO.getDataSovrainnesto().before(DateUtils.parseDate("31/12/1900"))) 
        {
          errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_1900));
        }
        else
        {
          isOkSovrainnesto = true;
        }
        // L'anno prima produzione non può essere minore dell'anno impiano piu il parametro APUV
        if(isOkImpianto) 
        {
        
          if(storicoUnitaArboreaVO.getDataSovrainnesto().before(
            storicoUnitaArboreaVO.getDataImpianto()))  
          {
            errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_DT_IMPIANTO));
            isOkSovrainnesto = false;
          }  
        }
      }
    }
    else 
    {
      storicoUnitaArboreaVO.setDataSovrainnesto(null);
    }
    // DATA PRIMA PRODUZIONE
    if(Validator.isNotEmpty(request.getParameter("dataPrimaProduzione"))) 
    {          
      if(!Validator.validateDateF(request.getParameter("dataPrimaProduzione"))) 
      {
        errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
      else 
      {
        // La data prima produzione non deve essere minore del 1900
        storicoUnitaArboreaVO.setDataPrimaProduzione(
              DateUtils.parseDate(request.getParameter("dataPrimaProduzione")));
        storicoUnitaArboreaVO.setAnnoPrimaProduzione(new Integer(DateUtils
             .extractYearFromDate(storicoUnitaArboreaVO.getDataPrimaProduzione())).toString());
        // L'anno prima produzione non può essere minore dell'anno impiano piu il parametro APUV
        if(isOkImpianto) 
        {
          int numConfronto = 2;
          Date dataConfronto = storicoUnitaArboreaVO.getDataImpianto();
          if(isOkSovrainnesto)
          {
            dataConfronto = storicoUnitaArboreaVO.getDataSovrainnesto();
            numConfronto = 1;
          }
          
          int yearDataConfronto =  DateUtils.extractYearFromDate(dataConfronto);
          int monthDataConfronto =  DateUtils.extractMonthFromDate(dataConfronto);
          int annoPrimaProduzioneInt = DateUtils.extractYearFromDate(storicoUnitaArboreaVO.getDataPrimaProduzione());
          
          //prima del 31/07
          if(monthDataConfronto <= 7)
          {
            if(annoPrimaProduzioneInt != yearDataConfronto + numConfronto)
            {
              errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataConfronto + numConfronto)));
            }
          }
          //dopo il 31/07
          else
          {
            if(annoPrimaProduzioneInt != yearDataConfronto + (numConfronto+1))
            {
              errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataConfronto + (numConfronto+1))));              
            }
          }
        }
      }
    }
    else 
    {
      storicoUnitaArboreaVO.setDataPrimaProduzione(null); 
      storicoUnitaArboreaVO.setAnnoPrimaProduzione(null); 
      errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
		
		// SESTO SU FILA
		if(Validator.isNotEmpty(request.getParameter("sestoSuFila"))) 
		{
			storicoUnitaArboreaVO.setSestoSuFila(request.getParameter("sestoSuFila")); 					
			if(!Validator.isNumericInteger(storicoUnitaArboreaVO.getSestoSuFila())) 
			{
				errors.add("sestoSuFila", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			// Deve essere maggiore di 0
			else if(Integer.parseInt(storicoUnitaArboreaVO.getSestoSuFila()) <= 0) 
			{
				errors.add("sestoSuFila", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoSuFila(null);
		}
		// SUPERFICIE VITATA
		if(Validator.isNotEmpty(request.getParameter("area"))) 
		{
			storicoUnitaArboreaVO.setArea(request.getParameter("area")); 					
			if(Validator.validateDouble(storicoUnitaArboreaVO.getArea(), 999999.9999) == null) 
			{
				errors.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			else 
      {
				storicoUnitaArboreaVO.setArea(StringUtils.parseSuperficieField(request.getParameter("area")));
				// La superficie vitata non può essere maggiore della superficie grafica
        
        
        
        String supConfronto = AnagUtils.valSupCatGraf(
              storicoParticellaVO.getSupCatastale(), storicoParticellaVO.getSuperficieGrafica());
        
        
				if(NumberUtils.arrotonda(Double.parseDouble(storicoUnitaArboreaVO.getArea().replace(',', '.')), 4) 
          > NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
        {
					errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_MAGGIORE_SUP_GRAFICA_CAT));
				}
			}
		}
		else 
		{
			storicoUnitaArboreaVO.setArea(null); 
			errors.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		// SESTO TRA FILE
		if(Validator.isNotEmpty(request.getParameter("sestoTraFile"))) 
		{
			storicoUnitaArboreaVO.setSestoTraFile(request.getParameter("sestoTraFile")); 					
			if(!Validator.isNumericInteger(storicoUnitaArboreaVO.getSestoTraFile())) 
			{
				errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			// Deve essere maggiore di 0
			else if(Integer.parseInt(storicoUnitaArboreaVO.getSestoTraFile()) <= 0) 
			{
				errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoTraFile(null); 
		}
		// PERCENTUALE VITIGNO
		if(Validator.isNotEmpty(request.getParameter("percentualeVitigno"))) 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(request.getParameter("percentualeVitigno")); 					
			if(!Validator.isNumericInteger(storicoUnitaArboreaVO.getPercentualeVarieta())) 
			{
				errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			// Controllo che non sia maggiore di 100
			else 
			{
				if(Integer.parseInt(storicoUnitaArboreaVO.getPercentualeVarieta()) > 100) 
				{
					errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}
				// Se altri vitigni = "S" non può essere < di 85 eliminato come da richiesta analisi CU-GAA09-54 Inserisci Unita arboree- v7.doc
				/*else if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S) && Integer.parseInt(storicoUnitaArboreaVO.getPercentualeVarieta()) < 85) {
					errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI));
				}*/
				else if(Integer.parseInt(storicoUnitaArboreaVO.getPercentualeVarieta()) == 100 && storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
				{
					errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI));
				}
				else if(Integer.parseInt(storicoUnitaArboreaVO.getPercentualeVarieta()) < 100 && storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
				{
					errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI));
				}
			}
		}
		else 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(null); 
			errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
		
		
		// PERCENTUALE FALLANZA
    if(Validator.isNotEmpty(request.getParameter("percentualeFallanza"))) 
    {  
      if(!Validator.isNumberPercentuale(request.getParameter("percentualeFallanza"))) 
      {
        errors.add("percentualeFallanza", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_FALLANZA));
      }
      else 
      {
        storicoUnitaArboreaVO.setPercentualeFallanza(new BigDecimal(request.getParameter("percentualeFallanza")));
      }
    }
    else 
    {
      storicoUnitaArboreaVO.setPercentualeFallanza(new BigDecimal(0)); 
      errors.add("percentualeFallanza", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
		// NUMERO CEPPI
		if(Validator.isNotEmpty(request.getParameter("numeroCeppi"))) 
		{
			storicoUnitaArboreaVO.setNumCeppi(request.getParameter("numeroCeppi")); 					
			// Dal momento che viene calcolato automaticamente, verifico che abbia dimensione > di 6
			if(storicoUnitaArboreaVO.getNumCeppi().length() > 6) 
			{
				errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_KO_NUMERO_CEPPI));
			}
			else if(!Validator.isNumericInteger(storicoUnitaArboreaVO.getNumCeppi())) 
			{
				errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			// Deve essere maggiore di 0
			else if(Integer.parseInt(storicoUnitaArboreaVO.getNumCeppi()) <= 0) 
			{
				errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
		}
		else 
		{
			storicoUnitaArboreaVO.setNumCeppi(null);
		}
		// FORMA ALLEVAMENTO
		if(Validator.isNotEmpty(request.getParameter("idFormaAllevamento"))) 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(Long.decode(request.getParameter("idFormaAllevamento"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(null); 
		}
		// MOTIVO INSERIMENTO
		if(Validator.isNotEmpty(request.getParameter("idCausaleModifica"))) 
		{
			storicoUnitaArboreaVO.setIdCausaleModifica(Long.decode(request.getParameter("idCausaleModifica"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdCausaleModifica(null); 
			errors.add("idCausaleModifica", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		}
    
    //MATRICOLA CCIAA
    //Controlla Matricola CCIAA max 15 caratteri
    String matricolaCCIAA = request.getParameter("matricolaCCIAA");
    if(Validator.isNotEmpty(matricolaCCIAA))
    { 
      if(matricolaCCIAA.trim().length() > 15)
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "matricolaCCIAA", AnagErrors.ERR_MATRICOLA_CCIAA);
      }
      else
      {
        storicoUnitaArboreaVO.setMatricolaCCIAA(matricolaCCIAA.trim());
      }
    }
    else
    {
      storicoUnitaArboreaVO.setMatricolaCCIAA(null);
    }
    
    
    //Provincia CCIAA
    String provinciaCCIAA = request.getParameter("provinciaCCIAA");
    if(Validator.isNotEmpty(provinciaCCIAA))
    { 
      storicoUnitaArboreaVO.setProvinciaCCIAA(provinciaCCIAA);
    }
    else
    {
      storicoUnitaArboreaVO.setProvinciaCCIAA(null);
    }
    
    
    //Controlla Annotzione in etichetta max 1000 caratteri
    String  annotazioneEtichetta = request.getParameter("annotazioneEtichetta");
    if(Validator.isNotEmpty(annotazioneEtichetta))
    {
      annotazioneEtichetta = annotazioneEtichetta.trim();
    }
    storicoUnitaArboreaVO.setEtichetta(annotazioneEtichetta);
    if(Validator.isNotEmpty(annotazioneEtichetta) && (annotazioneEtichetta.length() > 1000))
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "annotazioneEtichetta", AnagErrors.ERR_ANNOTAZIONE_ETICHETTA);
    }   
    
    //ANNO ISCRIZIONE ALBO
    //Controllo anno iscrizione albo minore anno di sitema e maggiore 1900 e >= anno impianto
    String annoIscrizioneAlbo = request.getParameter("annoIscrizioneAlbo");
    boolean isOKAnnoIscrizione = false;
    if(Validator.isNotEmpty(annoIscrizioneAlbo))
    {
      if(!Validator.isNumericInteger(annoIscrizioneAlbo))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_FORMA_ANNO_ISCRIZIONE_ALBO);
      }
      else
      {
        Integer currYear = DateUtils.getCurrentYear();
        Integer year1900 = new Integer(1900);
        Integer annoIscrizioneAlboInt = new Integer(annoIscrizioneAlbo);
        Integer annoImpianto = null;
        
        if(storicoUnitaArboreaVO.getAnnoImpianto() != null)
        {
          annoImpianto = new Integer(storicoUnitaArboreaVO.getAnnoImpianto());        
        }
        else //se null metto l'anno più basso
        {
          annoImpianto = year1900;
        }
        
        if((annoIscrizioneAlboInt.intValue() < year1900.intValue())
          || (annoIscrizioneAlboInt.intValue() < annoImpianto.intValue()))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_ISCRIZIONE_ALBO_ANNI);
        }
        else
        {
          storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(annoIscrizioneAlbo);
          isOKAnnoIscrizione = true;
        }        
      }
    }
    else
    {
      storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(null);
    }
    
    storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
    
    String idTipologiaVino = request.getParameter("idTipologiaVino");
    if(Validator.isNotEmpty(idTipologiaVino))
    {
      storicoUnitaArboreaVO.setIdTipologiaVino(new Long(idTipologiaVino));   
    }
    else
    {
      storicoUnitaArboreaVO.setIdTipologiaVino(null);
    }
    
    if(Validator.isNotEmpty(matricolaCCIAA) || Validator.isNotEmpty(annoIscrizioneAlbo))
    {
      if(!Validator.isNotEmpty(idTipologiaVino))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "idTipologiaVino", AnagErrors.ERR_TIPOVINO_OBBLIGATORIO);
      }    
    }
    
    TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdTipologiaVino()))
    {
      for(int i=0;i<vTipoTipologiaVino.size();i++)
      {
        if(storicoUnitaArboreaVO.getIdTipologiaVino()
          .compareTo(vTipoTipologiaVino.get(i).getIdTipologiaVino()) == 0)
        {
          tipoTipologiaVinoVO = vTipoTipologiaVino.get(i);
          break;
        }
      }
    }
    
    String vigna = request.getParameter("vigna");
    if(Validator.isNotEmpty(tipoTipologiaVinoVO)
      && "S".equalsIgnoreCase(tipoTipologiaVinoVO.getIndicazioneVigna()))
    {
      //esiste record su DB_VIGNA
      if(Validator.isNotEmpty(tipoTipologiaVinoVO.getVignaVO()))
      {
        if(Validator.isNotEmpty(vigna))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "vigna", AnagErrors.ERR_VIGNA_NON_INPUT);
        }
        
        //Allo stato attuale dovrei trovare uno ed un solo record
        storicoUnitaArboreaVO.setIdVigna(tipoTipologiaVinoVO.getVignaVO().getIdVigna());
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "vigna", "Campo obbligatoio");
      }      
    
    }
    else
    {
	    //Controlla Vigna max 1000 carattere
	    if(Validator.isNotEmpty(vigna))
	    {
	      vigna = vigna.trim();
	    }
	    if(Validator.isNotEmpty(vigna) && (vigna.length() > 1000))
	    {
	      errors = ErrorUtils.setValidErrNoNull(errors, "vigna", AnagErrors.ERR_VIGNA);
	    }
	  }
	  storicoUnitaArboreaVO.setVigna(vigna); 
    
    
    
    //Se il vino è DOC ed il paramtero ALBO della tabllea DB_PARAMTERO è a S 
    //devono essere valorizzati i campi matricola e anno iscrizione
    String vinoDoc = request.getParameter("vinoDoc");
    if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori
      && isOKAnnoIscrizione && (new Integer(storicoUnitaArboreaVO.getAnnoIscrizioneAlbo()).intValue()
         <= new Integer(parametroDAID).intValue()))
    {
      if(Validator.isEmpty(matricolaCCIAA))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "matricolaCCIAA", AnagErrors.ERR_CAMPO_OBBLIGATORIO_MATRICOLA_VINODOC);
      }
    }
      
    if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori)
    {  
      if(Validator.isEmpty(annoIscrizioneAlbo))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_VINODOC);
      }  
    }
    
   
    
    
		if(elencoAltriVitigni != null && elencoAltriVitigni.length > 0) 
    {
	 		for(int i = 0; i < elencoAltriVitigni.length; i++) 
	 		{
	 			AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigni[i];
	 			// Vitigno
	 			if(!Validator.isNotEmpty(elencoIdVarietaVitigno[i])) 
	 			{
	 				errori.add("idVarietaVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 				altroVitignoVO.setIdVarieta(null);
	 			}
	 			else 
	 			{
	 				// La varietà dell'altro vitigno deve essere diversa da quella dell'UV principale
	 				altroVitignoVO.setIdVarieta(Long.decode(elencoIdVarietaVitigno[i]));
          //Controllo nel caso nel vitigno principale è stato messo null!!!
          if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdVarieta()))
          {
  	 		 		if(altroVitignoVO.getIdVarieta().compareTo(storicoUnitaArboreaVO.getIdVarieta()) == 0) 
  	 		 		{
  	 		 			errori.add("idVarietaVitigno", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_VARIETA_UGUALE_VITIGNO_PRINCIPALE));
  	 		 		}
          }
	 			}
	 			// Percentuale
	 			if(!Validator.isNotEmpty(elencoPercentuale[i]))
	 			{
	 				errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 				altroVitignoVO.setPercentualeVitigno(null);
	 				countErroriPercentuale++;
	 			}
	 			else 
	 			{
	 				altroVitignoVO.setPercentualeVitigno(elencoPercentuale[i]);
	 				if(!Validator.isNumericInteger(elencoPercentuale[i]) || Integer.parseInt(elencoPercentuale[i]) == 0 
	 				  || Integer.parseInt(elencoPercentuale[i]) > 100) 
	 				{
	 					errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	 					countErroriPercentuale++;
	 				}
	 				else 
	 				{
	 					totPercentualeAltriVitigni += Integer.parseInt(elencoPercentuale[i]);
	 				}
	 			}
	 			erroriAltriVitigni.put(new Integer(i), errori);
	 			elencoAltriVitigni[i] = altroVitignoVO;
	 			session.setAttribute("elencoAltriVitigni", elencoAltriVitigni);
	 			storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltriVitigni);
	 		}
	 		if(countErroriPercentuale == 0 && Validator.isNotEmpty(storicoUnitaArboreaVO.getPercentualeVarieta()) 
	 		  && Validator.isNumericInteger(storicoUnitaArboreaVO.getPercentualeVarieta())) 
	 		{
	 			int percentualeVarieta = 0;
	 			if(Validator.isNotEmpty(storicoUnitaArboreaVO.getPercentualeVarieta())) 
	 			{
	 				percentualeVarieta = Integer.parseInt(storicoUnitaArboreaVO.getPercentualeVarieta());
	 			}
	 			if((percentualeVarieta + totPercentualeAltriVitigni) > 100) 
	 			{
	 				errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_ERRATA));
	 				erroriAltriVitigni.put(new Integer(storicoUnitaArboreaVO.getElencoAltriVitigni().length - 1), errori);
	 			}
        if((percentualeVarieta + totPercentualeAltriVitigni) < 100) 
        {
          errori.add("percentuale", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_MINORE_100));
          erroriAltriVitigni.put(new Integer(storicoUnitaArboreaVO.getElencoAltriVitigni().length - 1), errori);
        }
	 		}
 		}
		
		// Se si sono verificati degli errori li visualizzo
		if(errors.size() > 0 || errori.size() > 0) 
    {
			request.setAttribute("errors", errors);
			request.setAttribute("erroriAltriVitigni", erroriAltriVitigni);
			// Metto l'oggetto in request
		 	request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
			%>
				<jsp:forward page="<%= unitaArboreeInserisciUrl %>" />
			<%
		}
		// Altrimenti procedo con l'inserimento dell'unità arborea
		else 
    {
      //controllo che non ci siano pratiche uv associate alla particella
      //solo se l'utente non è una pa
      if(!ruoloUtenza.isUtentePA())
      {
        long idParticella = storicoParticellaVO.getIdParticella().longValue();
        long[] aIdParticella = new long[]{idParticella};
        
        RitornoAgriservUvVO ritornoAgriservUvVO = gaaFacadeClient.existPraticheEstirpoUV(aIdParticella,
          anagAziendaVO.getIdAzienda(), null, null, null, 0);
        Vector<Long>  vPraticheIdParticella = ritornoAgriservUvVO.getvPraticheIdParticella();
        
        //Passando una sola particella se il vettore è valorizzato vuol dire che ad
        // essa sono associate pratiche
        if(Validator.isNotEmpty(vPraticheIdParticella))
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV);
          request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
          %>
            <jsp:forward page="<%= unitaArboreeInserisciUrl %>" />
          <%
          return;     
        }
        
        //errore nella chiamata al servizio
        if(Validator.isNotEmpty(ritornoAgriservUvVO.getErrori())  && (ritornoAgriservUvVO.getErrori().length > 0))
        {
          request.setAttribute("messaggioErrore", ritornoAgriservUvVO.getErrori()[0]);
          request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
          %>
            <jsp:forward page="<%= unitaArboreeInserisciUrl %>" />
          <%
          return;     
        }
      }
      
      
      
     
                       
    
			try 
      {
        CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(storicoUnitaArboreaVO.getIdUtilizzo(),
          storicoUnitaArboreaVO.getIdVarieta(), storicoUnitaArboreaVO.getIdTipoDestinazione(), storicoUnitaArboreaVO.getIdTipoDettaglioUso(), 
          storicoUnitaArboreaVO.getIdTipoQualitaUso());
          storicoUnitaArboreaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
				anagFacadeClient.inserisciUnitaArborea(storicoUnitaArboreaVO, storicoParticellaVO, anagAziendaVO, ruoloUtenza, provenienza);
			}
			catch(SolmrException se) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_INSERIMENTO_UV);
				errors.add("errors", error);
	     	request.setAttribute("errors", errors);
	     	request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
	     	return;
			}
			// Torno alla pagina di ricerca elenco dell UV
			%>
				<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
			<%
      return;
		}
 	}
	// L'utente ha cliccato il pulsante annulla
	else if(request.getParameter("annulla") != null) 
  {
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
    return;
 	}
 	// L'utente ha premuto uno dei due pulsanti aggiungi/elimina
 	else if(Validator.isNotEmpty(request.getParameter("operazione"))) 
  {
 		// ALTRI VITIGNI
		storicoUnitaArboreaVO.setPresenzaAltriVitigni(request.getParameter("altriVitigni"));
		// COLTURA SPECIALIZZATA
		storicoUnitaArboreaVO.setColturaSpecializzata(request.getParameter("colturaSpecializzata"));
		// RICADUTA
		storicoUnitaArboreaVO.setRicaduta(request.getParameter("ricaduta"));
 		// DESTINAZIONE PRODUTTIVA
		if(Validator.isNotEmpty(request.getParameter("idUtilizzo"))) 
		{
			storicoUnitaArboreaVO.setIdUtilizzo(Long.decode(request.getParameter("idUtilizzo"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdUtilizzo(null); 
		}
		if(Validator.isNotEmpty(request.getParameter("idTipoDestinazione"))) 
    {
      storicoUnitaArboreaVO.setIdTipoDestinazione(Long.decode(request.getParameter("idTipoDestinazione")));           
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoDestinazione(null); 
    }
    if(Validator.isNotEmpty(request.getParameter("idTipoDettaglioUso"))) 
    {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(Long.decode(request.getParameter("idTipoDettaglioUso")));           
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoDettaglioUso(null); 
    }
    if(Validator.isNotEmpty(request.getParameter("idTipoQualitaUso"))) 
    {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(Long.decode(request.getParameter("idTipoQualitaUso")));           
    }
    else 
    {
      storicoUnitaArboreaVO.setIdTipoQualitaUso(null); 
    }      		
		// VITIGNO
    if(Validator.isNotEmpty(request.getParameter("idVarieta"))) 
    {
      storicoUnitaArboreaVO.setIdVarieta(Long.decode(request.getParameter("idVarieta")));
    }
    else 
    {
      storicoUnitaArboreaVO.setIdVarieta(null); 
    }
		// ANNO IMPIANTO
		if(Validator.isNotEmpty(request.getParameter("annoImpianto"))) 
		{
			storicoUnitaArboreaVO.setAnnoImpianto(request.getParameter("annoImpianto")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setAnnoImpianto(null); 
		}
		// ANNO RIFERIMENTO
		if(Validator.isNotEmpty(request.getParameter("annoRiferimento"))) 
		{
			storicoUnitaArboreaVO.setAnnoRiferimento(request.getParameter("annoRiferimento")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setAnnoRiferimento(null); 
		}
    // ANNO PRIMA PRODUZIONE
    if(Validator.isNotEmpty(request.getParameter("annoPrimaProduzione"))) 
    {
      storicoUnitaArboreaVO.setAnnoPrimaProduzione(request.getParameter("annoPrimaProduzione"));          
    }
    else 
    {
      storicoUnitaArboreaVO.setAnnoPrimaProduzione(null); 
    }
		// SESTO SU FILA
		if(Validator.isNotEmpty(request.getParameter("sestoSuFila"))) 
		{
			storicoUnitaArboreaVO.setSestoSuFila(request.getParameter("sestoSuFila")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoSuFila(null); 
		}
		// SUPERFICIE VITATA
		if(Validator.isNotEmpty(request.getParameter("area"))) 
		{
			storicoUnitaArboreaVO.setArea(request.getParameter("area")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setArea(null); 
		}
		// SESTO TRA FILE
		if(Validator.isNotEmpty(request.getParameter("sestoTraFile"))) 
		{
			storicoUnitaArboreaVO.setSestoTraFile(request.getParameter("sestoTraFile")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setSestoTraFile(null); 
		}
		// PERCENTUALE VITIGNO
		if(Validator.isNotEmpty(request.getParameter("percentualeVitigno"))) 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(request.getParameter("percentualeVitigno")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setPercentualeVarieta(null); 
		}
		// NUMERO CEPPI
		if(Validator.isNotEmpty(request.getParameter("numeroCeppi"))) 
		{
			storicoUnitaArboreaVO.setNumCeppi(request.getParameter("numeroCeppi")); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setNumCeppi(null); 
		}
		// FORMA ALLEVAMENTO
		if(Validator.isNotEmpty(request.getParameter("idFormaAllevamento"))) 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(Long.decode(request.getParameter("idFormaAllevamento"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdFormaAllevamento(null); 
		}
		// MOTIVO INSERIMENTO
		if(Validator.isNotEmpty(request.getParameter("idCausaleModifica"))) 
		{
			storicoUnitaArboreaVO.setIdCausaleModifica(Long.decode(request.getParameter("idCausaleModifica"))); 					
		}
		else 
		{
			storicoUnitaArboreaVO.setIdCausaleModifica(null); 
		}
 		Vector<AltroVitignoVO> temp = new Vector<AltroVitignoVO>();
 		AltroVitignoVO[] elencoAltriVitigni = (AltroVitignoVO[])session.getAttribute("elencoAltriVitigni");
 		if(elencoAltriVitigni != null) 
 		{
 			for(int i = 0; i < elencoAltriVitigni.length; i++) 
 			{
 				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigni[i];
 				temp.add(altroVitignoVO);
 				if(Validator.isNotEmpty(elencoIdVarietaVitigno[i])) 
 				{
 					altroVitignoVO.setIdVarieta(Long.decode(elencoIdVarietaVitigno[i]));
 				}
 				else 
 				{
 					altroVitignoVO.setIdVarieta(null);
 				}
 				if(Validator.isNotEmpty(elencoPercentuale[i])) 
 				{
 					altroVitignoVO.setPercentualeVitigno(elencoPercentuale[i]);
 				}
 				else 
 				{
 					altroVitignoVO.setPercentualeVitigno(null);
 				}
 				elencoAltriVitigni[i] = altroVitignoVO;
 				storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltriVitigni);
 			}
 		}
 		// AGGIUNGI	
 		if(request.getParameter("operazione").equalsIgnoreCase("aggiungi")) 
    {
 			AltroVitignoVO nuovoAltroVitignoVO = new AltroVitignoVO();
 			nuovoAltroVitignoVO.setIdAltroVitigno(new Long(-1));
 			temp.add(nuovoAltroVitignoVO);
 			elencoAltriVitigni = (AltroVitignoVO[])temp.toArray(new AltroVitignoVO[temp.size()]);
 			session.setAttribute("elencoAltriVitigni", elencoAltriVitigni);
 			storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltriVitigni);
 			request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
 	 		request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
 		    return;
 		}
 		// ELIMINA
 		else if(request.getParameter("operazione").equalsIgnoreCase("elimina")) 
    {
 			// Controllo che sia stato selezionato almeno un elemento
 			String[] altriVitigniDaEliminare = request.getParameterValues("numeroAltriVitigni");
 			if(altriVitigniDaEliminare == null || altriVitigniDaEliminare.length == 0) 
 			{
 				errors = new ValidationErrors();
 				errors.add("error", new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT));
 				request.setAttribute("errors", errors);
 				request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
 	     	request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
 	     	return;
 			}
 			// Se sì...
 			else 
 			{
 				// Elimino gli utilizzi selezionati
 				if(altriVitigniDaEliminare.length == elencoAltriVitigni.length) 
 				{
 					elencoAltriVitigni = new AltroVitignoVO[0];
 					session.removeAttribute("elencoAltriVitigni");
 				}
 				else 
 				{
 					int contatore = 0;
 					for(int i = 0; i < altriVitigniDaEliminare.length; i++) {
 						String indiceToRemove = (String)altriVitigniDaEliminare[i];
 						if(i == 0) {
 							temp.removeElementAt(Integer.parseInt(indiceToRemove));
 						}
 						else {
 							temp.removeElementAt(Integer.parseInt(indiceToRemove) - contatore);
 						}
 						contatore++;
 					}
 					elencoAltriVitigni = (AltroVitignoVO[])temp.toArray(new AltroVitignoVO[temp.size()]);
 					session.setAttribute("elencoAltriVitigni", elencoAltriVitigni);
 				}
 				storicoUnitaArboreaVO.setElencoAltriVitigni(elencoAltriVitigni);
 	 	 		request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
 	 	 		request.getRequestDispatcher(unitaArboreeInserisciUrl).forward(request, response);
 	 		  return;
 			}
 		}
 	}
 	else if(Validator.isNotEmpty(idStoricoUnitaArborea))
 	{
 	  //caso duplica l aprima volta ch si entra non si fà niente
 	}
 	// L'utente ha cliccato il pulsante inserisci da elenco unità arboree
	else 
  {
		// Imposto i valori di default
		storicoUnitaArboreaVO.setPercentualeVarieta("100");
		storicoUnitaArboreaVO.setPresenzaAltriVitigni(SolmrConstants.FLAG_N);
		storicoUnitaArboreaVO.setAnnoRiferimento(DateUtils.getCurrentYear().toString());
		storicoUnitaArboreaVO.setColturaSpecializzata(SolmrConstants.FLAG_N);
		storicoUnitaArboreaVO.setRicaduta(SolmrConstants.FLAG_N);
	}
	
 	// Metto l'oggetto in request
 	request.setAttribute("storicoUnitaArboreaVO", storicoUnitaArboreaVO);
 	
 	// Vado alla pagina di inserimento
 	%>
		<jsp:forward page="<%= unitaArboreeInserisciUrl %>" />
	<%
%>

