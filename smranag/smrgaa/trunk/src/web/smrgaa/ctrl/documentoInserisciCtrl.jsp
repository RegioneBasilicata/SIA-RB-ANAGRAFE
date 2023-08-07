<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	//Setto l'area di provenienza
	request.setAttribute("pageFrom", "documentale");

	String iridePageName = "documentoInserisciCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String documentoInserisciUrl = "/view/documentoInserisciView.jsp";
 	String documentiElencoUrl = "/view/documentiElencoView.jsp";
 	
 	
 	// Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
 	ResourceBundle res = ResourceBundle.getBundle("config");
 	String ambienteDeploy = res.getString("ambienteDeploy");
 	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
 	String documentoConfermaInserisciUrl ="";
 	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
 	  documentoConfermaInserisciUrl = "../layout/";
 	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
 		documentoConfermaInserisciUrl = "/layout/";
 	documentoConfermaInserisciUrl += "documentoConfermaInserisci.htm";
 	

 	
 	String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";
 	
 	String actionUrl = "../layout/documentiElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserisci Documento."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  //Indipendentemente dal motivo per cui sono qui devo andare a vedere
  //se ci sono righe nelle particelle associate e memorizzarle in session particelleAssociate
  /*Vector particelleAssociate=(Vector) session.getAttribute("particelleAssociate");
     
  if (particelleAssociate != null)
  {
    String supConduzione[]=request.getParameterValues("supConduzione");
    String idConduzione[]=request.getParameterValues("idConduzione");
    String supUtilizzata[]=request.getParameterValues("supUtilizzata");
    String idTipoUtilizzo[]=request.getParameterValues("idTipoUtilizzo");
    String idVarieta[]=request.getParameterValues("idVarieta");
      
      
    //devo memorizzare i dati inseriti/modificati dall'utente 
    int size=particelleAssociate.size();
    if (supConduzione!=null)
    {
      for (int i=0;i<size && i<supConduzione.length;i++)
      {
        ParticellaAssVO particella=(ParticellaAssVO)particelleAssociate.get(i);
        particella.setSupCondotta(supConduzione[i]);
        if (supUtilizzata!=null) particella.setSupUtilizzata(supUtilizzata[i]);
        if (idConduzione!=null) particella.setIdConduzione(idConduzione[i]);
        if (idTipoUtilizzo!=null) particella.setIdUtilizzo(idTipoUtilizzo[i]);
        if (idVarieta!=null) particella.setIdVarieta(idVarieta[i]);
      }
    }
    session.setAttribute("particelleAssociate",particelleAssociate);    
  }*/
       

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	String messaggioErrore = "";
 	ValidationError error = null;
 	ValidationErrors errors = new ValidationErrors();
 	DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoVO");
 	if(documentoVO == null) 
  {
 		documentoVO = new DocumentoVO();
 		session.setAttribute("documentoVO", documentoVO);
 	}
 	Vector elencoProprietari = (Vector)session.getAttribute("elencoProprietari");
 	StoricoParticellaVO[] elencoParticelle = (StoricoParticellaVO[])session.getAttribute("elencoParticelle");

  // Ricerco nuovamente l'azienda per essere sicuro di lavorare sulla situazione aggiornata
  try 
  {
  	anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
  	messaggioErrore = se.getMessage();
    request.setAttribute("messaggioErrore", messaggioErrore);;
    %>
     	<jsp:forward page="<%= documentiElencoUrl %>" />
    <%
  }

  // Rimetto l'oggetto in sessione disponibile per l'utente con i dati aggiornati
  session.setAttribute("anagAziendaVO", anagAziendaVO);

    // Controllo che l'azienda selezionato abbia il CUAA valorizzato
	if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
  {
  	messaggioErrore = (String)AnagErrors.get("ERR_NO_CUAA_FOR_DOCUMENTI");
  	request.setAttribute("messaggioErrore", messaggioErrore);
  	%>
    	<jsp:forward page="<%= documentiElencoUrl %>" />
  	<%
  }
    
  boolean cessata=false;
  if (anagAziendaVO.getDataCessazione()!=null) cessata=true;

 	// COMBO TIPO TIPOLOGIA DOCUMENTO
 	Vector<it.csi.solmr.dto.CodeDescription> elencoTipiTipologiaDocumento = null;
 	try 
  {
    //Caricamento: combo popolata con il campo descrizione
    //dei record di DB_TIPO_TIPOLOGIA_DOCUMENTO aventi ID_TIPOLOGIA_DOCUMENTO <> 4
    //se l'azienda non è cessata
    //di tutti i record di DB_TIPO_TIPOLOGIA_DOCUMENTO se l'azienda è cessata
    elencoTipiTipologiaDocumento = anagFacadeClient.getTipiTipologiaDocumento(cessata);
 	}
 	catch(SolmrException se) 
 	{
  	messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      	<jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
 	}
 	request.setAttribute("elencoTipiTipologiaDocumento", elencoTipiTipologiaDocumento);

  // COMBO TIPO DOCUMENTO
  //Se valorizzzato arrivo dalla popUp di ricerca documenti
  String tipoTipologiaDocumento = request.getParameter("idTipologiaDocumentoPopUp");
  if(Validator.isEmpty(tipoTipologiaDocumento))
  {
	  tipoTipologiaDocumento = request.getParameter("idTipologiaDocumento");
	}
	Long idTipologiaDocumento = null;
  TipoCategoriaDocumentoVO[] elencoTipiCategoriaDocumento = null;
	if(Validator.isNotEmpty(tipoTipologiaDocumento)) 
  {
 		idTipologiaDocumento = Long.decode(tipoTipologiaDocumento);
 		request.setAttribute("idTipologiaDocumento", idTipologiaDocumento);
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try 
 		{
 			elencoTipiCategoriaDocumento = anagFacadeClient.getListTipoCategoriaDocumentoByIdTipologiaDocumento(idTipologiaDocumento, orderBy);
 		}
 		catch(SolmrException se) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_DOCUMENTO);
 			errors.add("idCategoriaDocumento", error);
 			request.setAttribute("errors", errors);
 			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     	return;
 		}
 		request.setAttribute("elencoTipiCategoriaDocumento", elencoTipiCategoriaDocumento);
	}

	// COMBO DESCRIZIONE DOCUMENTO
	//Se valorizzzato arrivo dalla popUp di ricerca documenti
	String tipoCategoriaDocumento = request.getParameter("idCategoriaDocumentoPopUp");
  if(Validator.isEmpty(tipoCategoriaDocumento))
  {
    tipoCategoriaDocumento = request.getParameter("idCategoriaDocumento");
  }
	Long idCategoriaDocumento = null;
	TipoDocumentoVO[] elencoTipiDocumento = null;
	if(Validator.isNotEmpty(tipoCategoriaDocumento) && idTipologiaDocumento != null) 
  {
		idCategoriaDocumento = Long.decode(tipoCategoriaDocumento);
 		request.setAttribute("idCategoriaDocumento", idCategoriaDocumento);
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try 
    {
      //Caricamento: popolare la combo con tutte le occorrenze della tabella db_tipo_documento con data_fine_validità = NULL 
      //e id_documento presente sulla tabella db_documento_categoria per id_categoria_documento scelto dalla combo precedente 
      //+ entry vuota ed aventi DB_TIPO_DOCUMENTO.FLAG_AZIENDA_CESSATA='A' oppure ='T' se l'azienda non è cessata  
      //DB_TIPO_DOCUMENTO.FLAG_AZIENDA_CESSATA='C' se l'azienda è cessata
 			elencoTipiDocumento = anagFacadeClient.getListTipoDocumentoByIdCategoriaDocumento(idCategoriaDocumento, orderBy, true, new Boolean(cessata));
 		}
 		catch(SolmrException se) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO);
 			errors.add("idTipoDocumento", error);
 			request.setAttribute("errors", errors);
 			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
      return;
 		}
 		request.setAttribute("elencoTipiDocumento", elencoTipiDocumento);
	}
	else 
  {
		session.removeAttribute("documentoVO");
		session.removeAttribute("elencoProprietari");
    session.removeAttribute("elencoParticelle");
    //session.removeAttribute("particelleAssociate");
    //session.removeAttribute("particelleDaAssociare");
    session.removeAttribute("elencoTipiUsoSuoloPrimario");   
	}

	// Recupero il tipo documento per la corretta gestione dei dati dell'inserimento
	//Se valorizzzato arrivo dalla popUp di ricerca documenti
  String tipoDocumento = request.getParameter("idTipoDocumentoPopUp");
  if(Validator.isEmpty(tipoDocumento))
  {
    tipoDocumento = request.getParameter("idTipoDocumento");
  }
	Long idTipoDocumento = null;
	TipoDocumentoVO tipoDocumentoVO = null;
	if(Validator.isNotEmpty(tipoDocumento) && idCategoriaDocumento != null && idTipologiaDocumento != null) 
  {
		idTipoDocumento = Long.decode(tipoDocumento);
		request.setAttribute("idTipoDocumento", idTipoDocumento);
		try 
    {
 			tipoDocumentoVO = anagFacadeClient.findTipoDocumentoVOByPrimaryKey(idTipoDocumento);
 			request.setAttribute("tipoDocumentoVO", tipoDocumentoVO);
 		}
 		catch(SolmrException se) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO);
	 		errors.add("idTipoDocumento", error);
 			request.setAttribute("errors", errors);
 			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
 			return;
 		}
	}
	
	
	
	TipoCategoriaDocumentoVO tipoCategoriaDocumentoVOSelezionato = null;
  if(elencoTipiCategoriaDocumento != null && elencoTipiCategoriaDocumento.length > 0) 
  {
    for(int i = 0; i < elencoTipiCategoriaDocumento.length; i++) 
    {
      TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = (TipoCategoriaDocumentoVO)elencoTipiCategoriaDocumento[i];
      if(idCategoriaDocumento != null 
        && tipoCategoriaDocumentoVO.getIdCategoriaDocumento().compareTo(idCategoriaDocumento) == 0) 
      {
        tipoCategoriaDocumentoVOSelezionato = tipoCategoriaDocumentoVO;
        break;
      }
    }
  }
  
  
  TipoDocumentoVO tipoDocumentoVOSelezionato = null;
  if(elencoTipiDocumento != null && elencoTipiDocumento.length > 0) 
  {
    for(int i = 0; i < elencoTipiDocumento.length; i++) 
    {
      TipoDocumentoVO tipoDocVO = (TipoDocumentoVO)elencoTipiDocumento[i];
      if(idTipoDocumento != null 
        && tipoDocVO.getIdDocumento().compareTo(idTipoDocumento) == 0) 
      {
        tipoDocumentoVOSelezionato = tipoDocVO;
        break;
      }
    }
  }
	
	
  
  
  // Ricerco la combo delle varietà se il tipo documento selezionato è di
  // tipo correttiva terreni
  if(tipoDocumentoVO != null 
    && tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
  {
    // Recupero i valori relativi ai tipi uso suolo(attivi e non) utilizzati dall'azienda in esame
    Vector elencoTipiUsoSuolo = (Vector)session.getAttribute("elencoTipiUsoSuoloPrimario");
    if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0)
    {
      try 
      {
        String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
        elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAziendaCess(anagAziendaVO.getIdAzienda(), orderBy);
        session.setAttribute("elencoTipiUsoSuoloPrimario", elencoTipiUsoSuolo);
      }
      catch(SolmrException se) 
      {
        error = new ValidationError(AnagErrors.ERRORE_KO_VARIETA_PRIMARIA);
        errors.add("idTipoUtilizzo", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
        return;
      }
    }
  }

 	// Ricerco la combo del titolo possesso solo se il tipo documento selezionato è di
 	// tipo territoriale
 	FaseRiesameDocumentoVO faseRiesameDocumentoVO = null;
 	if(tipoDocumentoVO != null 
    && (tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
      || tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))) 
  {
 		// COMBO TITOLO POSSESSO
 		it.csi.solmr.dto.CodeDescription[] elencoTitoloPossesso = null;
 		try 
 		{
  		elencoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
 		}
 		catch(SolmrException se) 
 		{
  		messaggioErrore = se.getMessage();
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      		<jsp:forward page="<%= documentiElencoUrl %>" />
   		<%
 		}
 		request.setAttribute("elencoTitoloPossesso", elencoTitoloPossesso);

 		// COMBO CASO PARTICOLARE
 		it.csi.solmr.dto.CodeDescription[] elencoCasoParticolare = null;
 		try 
    {
 			elencoCasoParticolare = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_CODE);
 		}
 		catch(SolmrException se) 
 		{
  		messaggioErrore = AnagErrors.ERRORE_KO_CASI_PARTICOLARI;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      		<jsp:forward page="<%= documentiElencoUrl %>" />
   		<%
 		}
 		request.setAttribute("elencoCasoParticolare", elencoCasoParticolare);
    
    //Combo anomalie
    TipoControlloVO[] elencoTipiControllo = null;
    try 
    {
      String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
      elencoTipiControllo = anagFacadeClient.getListTipoControlloForAziendaByIdGruppoControllo(
        SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, anagAziendaVO.getIdAzienda(), orderBy);
    }
    catch(Exception se)
    {
      messaggioErrore = AnagErrors.ERRORE_KO_ELENCO_ANOMALIE;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
          <jsp:forward page="<%= documentiElencoUrl %>" />
      <%
    }
    request.setAttribute("elencoTipiControllo", elencoTipiControllo);
   
    
  
    //Se istanza di riesame
    if("TC".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getTipoIdentificativo())
       && "518".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getIdentificativo()))
    {
      try 
      {    
	      faseRiesameDocumentoVO = gaaFacadeClient
	        .getFaseRiesameDocumentoByIdDocumento(tipoDocumentoVOSelezionato.getIdDocumento().longValue());
	     
	      int faseIstanzaRiesame = faseRiesameDocumentoVO.getIdFaseIstanzaRiesame().intValue();
	      documentoVO.setFaseIstanzaRiesame(faseIstanzaRiesame);
	      request.setAttribute("faseRiesameDocumentoVO", faseRiesameDocumentoVO);
	    }
	    catch(Exception se)
	    {
	      messaggioErrore = AnagErrors.ERRORE_KO_ELENCO_FASI_DOC;
	      request.setAttribute("messaggioErrore", messaggioErrore);
	      %>
	          <jsp:forward page="<%= documentiElencoUrl %>" />
	      <%
	    }
    }  
    
 	}
  
  
  // Ricerco la combo del titolo possesso solo se il tipo documento selezionato è di
  // tipo conto corrente
  if(tipoDocumentoVO != null 
    && tipoDocumentoVO.getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)) 
  {
    // COMBO TITOLO POSSESSO
    Vector<ContoCorrenteVO> conti = null;
    try 
    {
      conti = anagFacadeClient.getContiCorrenti(anagAziendaVO.getIdAzienda(),false);
    }
    catch(SolmrException se) 
    {
      messaggioErrore = se.getMessage();
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
          <jsp:forward page="<%= documentiElencoUrl %>" />
      <%
    }
    request.setAttribute("conti", conti);
    

  }

 	// L'utente ha selezionato il tasto conferma per la registrazione del documento
 	if(request.getParameter("conferma") != null) 
  {
  	// Verifico l'obbligatorietà e la correttezza formale dei dati inseriti dall'utente
   	documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
  	errors = documentoVO.validateInsertDocumento(request);
    
    boolean errori=false;
    
    //Vado a vedere se ci sono particelle associate
    /*if (particelleAssociate!=null) 
    {
      int size=particelleAssociate.size();
      for(int i=0;i<size;i++)
      {
        boolean errore=((ParticellaAssVO)particelleAssociate.get(i)).validateInsert();
        errori=errori || errore;
      }
      //Se non trovo erroti proseguo con il controllo sulla conduzione
      if (!errori)
      {
        particelleAssociate= ParticellaAssVO.checkValidazione(particelleAssociate);
        errori=((Boolean)particelleAssociate.get(size)).booleanValue();
        particelleAssociate.remove(size);
      }
    }*/
      
    //se ho trovato almeno un errore devo aggiungere un errore fittizio per bloccare 
    //l'inserimento del documento
    if (errori)
        errors.add("Fittizia", new ValidationError("Fittizia"));
    
   	// Se ci sono errori li visualizzo
   	if(errors.size() > 0) 
    {
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
   		return;
   	}
   	documentoVO.setCuaa(anagAziendaVO.getCUAA());
   	documentoVO.setIdAzienda(anagAziendaVO.getIdAzienda());
   	documentoVO.setExtIdDocumento(tipoDocumentoVO.getIdDocumento());

   	// Se non ci sono stati errori controllo...
   	if(tipoDocumentoVO != null 
      && Validator.isNotEmpty(tipoDocumentoVO.getFlagAnagTerr())) 
    {
   		// Se è stato scelto un documento di tipo "A"...
   		if(tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) 
      {
     		//... che non esista già un documento con lo stesso numero documento ed ente rilascio inseriti dall'utente
     		DocumentoVO documentoControlloVO = null;
     		try 
        {
       		documentoControlloVO = anagFacadeClient.findDocumentoVOBydDatiAnagrafici(documentoVO);
     		}
     		catch(SolmrException se) 
        {
       		error = new ValidationError(se.getMessage());
       		errors.add("numeroDocumento", error);
          errors.add("enteRilascioDocumento", error);
       		request.setAttribute("errors", errors);
       		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
       		return;
     		}
     		if(documentoControlloVO != null) 
        {
     			error = new ValidationError(AnagErrors.ERR_DOCUMENTO_PRESENTE_FOR_NUMERO_ENTE_RILASCIO);
     			errors.add("numeroDocumento", error);
     			errors.add("enteRilascioDocumento", error);
     			request.setAttribute("errors", errors);
     			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     			return;
     		}
   		}
		  // Se è stato scelto un documento di tipo "Z"(zootecnico)...
     	else if(tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) 
      {
				// Se è stato indicato il cuaa soccidario
				if(Validator.isNotEmpty(documentoVO.getCuaaSoccidario())) 
        {
       		// Verifico che il Cuaa soccidario non sia uguale a quello dell'azienda
					// agricola
					if(documentoVO.getCuaaSoccidario().equalsIgnoreCase(anagAziendaVO.getCUAA())) 
          {
						error = new ValidationError(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_EQUAL_CUAA_AZIENDA);
     				errors.add("cuaaSoccidario", error);
     				request.setAttribute("errors", errors);
     				request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     				return;
					}
   				// Recupero il parametro DOAT
   				String parametroDOAT = null;
   				try 
          {
   					parametroDOAT = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DOAT);
   				}
   				catch(SolmrException se) 
          {
       			messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_DOAT;
       			request.setAttribute("messaggioErrore", messaggioErrore);
       			%>
          			<jsp:forward page="<%= documentiElencoUrl %>" />
       			<%
     			}
       		// Se il parametro DOAT == S
       	  if(parametroDOAT.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
						// Richiamo il WEB-SERVICE SIAN per recuperari i dati relativi all'anagrafe tributaria
   					SianAnagTributariaVO anagTrib = null;
   					try 
            {
   						anagTrib = anagFacadeClient.ricercaAnagrafica(documentoVO.getCuaaSoccidario().toUpperCase(), 
   						  ProfileUtils.getSianUtente(ruoloUtenza));
   					}
   					catch(SolmrException se) 
            {
   						// Se il SIAN va in errore non blocco l'operazione: semplicemente
     					// attesto che il CUAA soccidario non è stato certificato
    					documentoVO.setFlagCuaaSoccidarioValidato(SolmrConstants.FLAG_N);
    				}
    				// Se l'oggetto è diverso da null e quindi il CUAA soccidario indicato è censito
    				// su AT
    				if(anagTrib != null) 
            {
    					// Controllo la congruenza tra il CUAA soccidario indicato
    					// e quello reperito dal SIAN
    					if(!documentoVO.getCuaaSoccidario()
                .equalsIgnoreCase(anagTrib.getCodiceFiscale())) 
              {
       					error = new ValidationError(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_NOT_EQUAL_AT);
           			errors.add("cuaaSoccidario", error);
           			request.setAttribute("errors", errors);
           			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
           			return;
    					}
						  // Altrimenti setto il flag relativo alla validazione del cuaa
          		else 
              {
          		  documentoVO.setFlagCuaaSoccidarioValidato(SolmrConstants.FLAG_S);
          		}
    				}
						// Se il SIAN non restituisce nulla per il CUUA indicato attesto che non è stato validato
	   				else 
            {
	   					documentoVO.setFlagCuaaSoccidarioValidato(SolmrConstants.FLAG_N);
	   				}
 	      	}
					// Se il SIAN non viene richiamato attesto che il CUAA soccidario non è stato certificato
					else 
          {
						documentoVO.setFlagCuaaSoccidarioValidato(SolmrConstants.FLAG_N);
					}
				}
      }
   		// Se è stato scelto un documento di tipo "T"...
   		else if(tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
      || tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
      {
     		// Se FLAG_OBBLIGO_PROPRIETARIO è uguale a S
     		if(tipoDocumentoVO.getFlagObbligoProprietario().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
   				// Controllo che sia stato inserito almeno un proprietario
     			if(elencoProprietari == null || elencoProprietari.size() == 0) 
          {
     				error = new ValidationError(AnagErrors.ERRORE_PROPRIETARIO_DOCUMENTO_KO);
     				errors.add("noProprietari", error);
     				request.setAttribute("errors", errors);
     				request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     				return;
     			}
     		}
        // Se sono stati indicati dei proprietari
        if(elencoProprietari != null && elencoProprietari.size() > 0) 
        {
					// Recupero il parametro DOAT
          String parametroDOAT = null;
					Vector<String> elencoErrori = new Vector<String>();
					boolean isKo = false;
     			try 
          {
     				parametroDOAT = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DOAT);
     			}
     			catch(SolmrException se) 
          {
            messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_DOAT;
           	request.setAttribute("messaggioErrore", messaggioErrore);
           	%>
            	<jsp:forward page="<%= documentiElencoUrl %>" />
           	<%
         	}
     			// Se il parametro DOAT == S
     			if(parametroDOAT.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
          {
     				for(int i = 0; i < elencoProprietari.size(); i++) 
            {
     					DocumentoProprietarioVO documentoProprietarioVO = 
                (DocumentoProprietarioVO)elencoProprietari.elementAt(i);
     					// Richiamo il WEB-SERVICE SIAN per recuperari i dati relativi all'anagrafe tributaria
       				SianAnagTributariaVO anagTrib = null;
       				try 
              {
       					anagTrib = anagFacadeClient.ricercaAnagrafica(
                  documentoProprietarioVO.getCuaa().toUpperCase(), ProfileUtils.getSianUtente(ruoloUtenza));
       				}
       				catch(SolmrException se) 
              {
       					// Se il SIAN va in errore non blocco l'operazione: semplicemente
       					// attesto che il CUAA soccidario non è stato certificato
       					documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
       				}
							// Se l'oggetto è diverso da null e quindi il CUAA indicato è censito
       				// su AT
       				if(anagTrib != null) 
              {
         				// Controllo la congruenza tra il CUAA soccidario indicato
         				// e quello reperito dal SIAN
         				if(!documentoProprietarioVO.getCuaa()
                  .equalsIgnoreCase(anagTrib.getCodiceFiscale())) 
                {
  	       				isKo = true;
         					elencoErrori.add(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_NOT_EQUAL_AT);
         				}
  						  // Altrimenti setto il flag relativo alla validazione del cuaa
             		else 
                {
             			elencoErrori.add(null);
             			documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_S);
             		}
	         		}
	         		else 
              {
	         			elencoErrori.add(null);
	         			documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
	         		}
           	}
          }
     			else 
          {
     				for(int i = 0; i < elencoProprietari.size(); i++) 
            {
     					DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)elencoProprietari.elementAt(i);
     					documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
     				}
     			}
     			// Se si sono verificate delle incongruenze con anagrafe tributaria
     			// blocco l'operazione di inserimento
     			if(elencoErrori.size() > 0 && isKo) 
          {
     				request.setAttribute("elencoErrori", elencoErrori);
     				%>
          			<jsp:forward page="<%= documentoInserisciUrl %>" />
       			<%
     			}
        }
     		// Controllo che sia stata inserita almeno una particella solo se FLAG_OBBLIGO_PARTICELLA == S
     		if(tipoDocumentoVO.getFlagAnagTerr()
          .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
          && tipoDocumentoVO.getFlagObbligoParticella().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
     			if(elencoParticelle == null || elencoParticelle.length == 0) 
          {
       			error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_KO);
       			errors.add("noParticelle", error);
       			request.setAttribute("errors", errors);
       			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
       			return;
     			}
     		}
        if(tipoDocumentoVO.getFlagAnagTerr()
          .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)
        && tipoDocumentoVO.getFlagObbligoParticella().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
          if(elencoParticelle == null || elencoParticelle.length == 0)
            //|| particelleAssociate==null || particelleAssociate.size()==0 ) 
          {
            error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_ASS_DOCUMENTO_KO);
            errors.add("noParticelle", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
            return;
          }
        }
        
        
        
        
        //Controllo correttezza lunghezza note
        //e setto i valori
        Vector<ValidationErrors> elencoErroriNote = new Vector<ValidationErrors>();
        int countErroriNote = 0;
        String[] arrNoteDocConduzione = request.getParameterValues("noteDocConduzione"); 
        if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)) 
        {
          if(Validator.isNotEmpty(elencoParticelle))
          {
            for(int j=0;j<elencoParticelle.length;j++)
            {
              ValidationErrors errorsNoteParticelle = new ValidationErrors();            
              if(arrNoteDocConduzione[j].length() > 500)
              {
                errorsNoteParticelle.add("noteDocConduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                countErroriNote++;
              }
              else
              {
                StoricoParticellaVO stvoTmp = (StoricoParticellaVO)elencoParticelle[j]; 
                if(Validator.isNotEmpty(stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
                {
                  stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].setNote(arrNoteDocConduzione[j]);              
                }
                else
                {
                  DocumentoConduzioneVO docCondVoTmp = new DocumentoConduzioneVO();
                  docCondVoTmp.setNote(arrNoteDocConduzione[j]);
                  stvoTmp.getElencoConduzioni()[0].setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{docCondVoTmp});
                }
              }
              
              elencoErroriNote.add(errorsNoteParticelle);              
    
            }
            
            
            if(countErroriNote > 0) 
            {
              request.setAttribute("elencoErroriNote", elencoErroriNote);
              request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
              return;
            }
          }
        }
        
        
        arrNoteDocConduzione = request.getParameterValues("cessNoteDocConduzione"); 
        if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
        {
          if(Validator.isNotEmpty(elencoParticelle))
          {
            for(int j=0;j<elencoParticelle.length;j++)
            {
              ValidationErrors errorsNoteParticelle = new ValidationErrors();            
              if(arrNoteDocConduzione[j].length() > 500)
              {
                errorsNoteParticelle.add("cessNoteDocConduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                countErroriNote++;
              }
              else
              {
                StoricoParticellaVO stvoTmp = (StoricoParticellaVO)elencoParticelle[j]; 
                if(Validator.isNotEmpty(stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
                {
                  stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].setNote(arrNoteDocConduzione[j]);              
                }
                else
                {
                  DocumentoConduzioneVO docCondVoTmp = new DocumentoConduzioneVO();
                  docCondVoTmp.setNote(arrNoteDocConduzione[j]);
                  stvoTmp.getElencoConduzioni()[0].setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{docCondVoTmp});
                }
              }
              
              elencoErroriNote.add(errorsNoteParticelle);              
    
            }
            
            
            if(countErroriNote > 0) 
            {
              request.setAttribute("elencoErroriNote", elencoErroriNote);
              request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
              return;
            }
          }
        }
        
        
        
        /*TipoCategoriaDocumentoVO tipoCategoriaDocumentoVOSelezionato = null;
        if(elencoTipiCategoriaDocumento != null && elencoTipiCategoriaDocumento.length > 0) 
        {
          for(int i = 0; i < elencoTipiCategoriaDocumento.length; i++) 
          {
            TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = (TipoCategoriaDocumentoVO)elencoTipiCategoriaDocumento[i];
            if(idCategoriaDocumento != null 
              && tipoCategoriaDocumentoVO.getIdCategoriaDocumento().compareTo(idCategoriaDocumento) == 0) 
            {
              tipoCategoriaDocumentoVOSelezionato = tipoCategoriaDocumentoVO;
              break;
            }
          }
        }
        
        
        TipoDocumentoVO tipoDocumentoVOSelezionato = null;
        if(elencoTipiDocumento != null && elencoTipiDocumento.length > 0) 
        {
          for(int i = 0; i < elencoTipiDocumento.length; i++) 
          {
            TipoDocumentoVO tipoDocVO = (TipoDocumentoVO)elencoTipiDocumento[i];
            if(idTipoDocumento != null 
              && tipoDocVO.getIdDocumento().compareTo(idTipoDocumento) == 0) 
            {
              tipoDocumentoVOSelezionato = tipoDocVO;
              break;
            }
          }
        }*/
        
        
        
        
        
        if(Validator.isNotEmpty(tipoCategoriaDocumentoVOSelezionato))
        {
        
          String[] arrLavorazionePrioritaria = request.getParameterValues("lavorazionePrioritaria");           
          
          
          //Se istanza di riesame setto il valore della priorità lavorazione
          if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)) 
          {
            if("TC".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getTipoIdentificativo())
                  && "518".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getIdentificativo()))
            {
              
              HashMap<Long,String> hLavNote = new HashMap<Long,String>();
              
              if(Validator.isNotEmpty(request.getParameter("dataFineValidita")))
              {
	              error = new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
	              errors.add("dataFineValidita", error);
	              request.setAttribute("errors", errors);
	              request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	              return;
	            }
	            
	            
	            Vector<Long> vIdParticella = new Vector<Long>();	            
	            if(Validator.isNotEmpty(elencoParticelle))
		          {
		            for(int j=0;j<elencoParticelle.length;j++)
		            {
		              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)elencoParticelle[j];
		              if(!vIdParticella.contains(stvoTmp.getIdParticella()))
		              {
		                vIdParticella.add(stvoTmp.getIdParticella());
		              }
		              
		              String lavorazionePrioritaria = "N";
		              if(Validator.isNotEmpty(arrLavorazionePrioritaria))
		              {
		                for(int k=0;k<arrLavorazionePrioritaria.length;k++)
		                {
		                  if(arrLavorazionePrioritaria[k].equalsIgnoreCase(""+stvoTmp.getElencoConduzioni()[0].getIdConduzioneParticella()))
		                  {
		                    lavorazionePrioritaria = "S";
		                    break;
		                  }        
		                }
		                
		              }
		              if(Validator.isNotEmpty(stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
		              {
		                stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].setLavorazionePrioritaria(lavorazionePrioritaria);              
		              }
		              else
		              {
		                DocumentoConduzioneVO docCondVoTmp = new DocumentoConduzioneVO();
		                docCondVoTmp.setLavorazionePrioritaria(lavorazionePrioritaria);
		                stvoTmp.getElencoConduzioni()[0].setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{docCondVoTmp});
		              }              
		            }
		          }
	            
	            
	            
		          if(Validator.isNotEmpty(elencoParticelle))
		          {		          
		          
		            if(SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame())
                {
                  //solo per particella "normali"
                  /*if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
                  {
	                  //legenda 1 ******inizio ******
	                  if(!gaaFacadeClient.isFaseIstanzaRiesameEvasa(anagAziendaVO.getIdAzienda().longValue(),
	                    SolmrConstants.FASE_IST_RIESAM_FOTO, vIdParticella, SolmrConstants.PARAMETRO_GGFOTORIC))
	                  {
	                    error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_FASE_PREC);
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                    return;
	                  }
	                }*/
                  //legenda 1 ******fine*****
                  
                  //legenda 9  ***inizio*****
                  if(gaaFacadeClient.isSitiConvocaValid(anagAziendaVO.getIdAzienda().longValue(),
                    DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                  {
                    error = new ValidationError(AnagErrors.ERRORE_DOC_ANNUL_MOD_IST_RIE_CONVOCA);
                    errors.add("particelleP", error);
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
                    return;
                  }
                  //legenda 9  ***fine*****
                  
                }                
                
                /*if(SolmrConstants.FASE_IST_RIESAM_SOPRA == documentoVO.getFaseIstanzaRiesame())
                {
                  if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
                  {
	                  //legenda 1 ******inizio ******
	                  if(!gaaFacadeClient.isFaseIstanzaRiesameEvasa(anagAziendaVO.getIdAzienda().longValue(),
	                    SolmrConstants.FASE_IST_RIESAM_CONTRO, vIdParticella, SolmrConstants.PARAMETRO_GGCONTRORIC))
	                  {
	                    error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_FASE_PREC);
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                    return;
	                  }
	                  //legenda 1 ******fine ******
	                }                
                }*/
		          
		            for(int j=0;j<elencoParticelle.length;j++)
		            {
		              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)elencoParticelle[j];
		              
		              
		              //Da fare solo coi documenti istanza classici
		              if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
		              {
		              
		                //legenda 2  ***inizio ****
			              int p30 = gaaFacadeClient.calcolaP30PlSql(stvoTmp.getIdStoricoParticella().longValue(), null);
			              if(p30 == 1)
			              {
			                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
			                errors.add("particelleP", error);
			                request.setAttribute("errors", errors);
			                request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
			                return;
			              }
			              
			              int p25 = gaaFacadeClient.calcolaP25PlSql(stvoTmp.getIdStoricoParticella().longValue(), null);
			              if(p25 == 1)
			              {
			                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
			                errors.add("particelleP", error);
			                request.setAttribute("errors", errors);
			                request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
			                return;
			              }
			              
			              Long idParticellaCertificata = null;
			              if(Validator.isNotEmpty(stvoTmp.getParticellaCertificataVO()))
			              {
			                idParticellaCertificata = stvoTmp.getParticellaCertificataVO().getIdParticellaCertificata();
			              }
			              int p26 = gaaFacadeClient.calcolaP26PlSql(anagAziendaVO.getIdAzienda().longValue(),
			                stvoTmp.getIdParticella().longValue(), idParticellaCertificata);
			                
			              if(p26 != 1)
			              {
			                if(SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame())
                      {
                        java.util.Date dataInserimento = new java.util.Date();
					              if(Validator.isNotEmpty(documentoVO)
					                && Validator.isNotEmpty(documentoVO.getDataInserimento()))
					              {
					                dataInserimento = documentoVO.getDataInserimento();
					              }
					              
					              if(!gaaFacadeClient.isParticellaInPotenzialeContra(anagAziendaVO.getIdAzienda().longValue(),
					                 stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(dataInserimento)))
					              {
					                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
		                      errors.add("particelleP", error);
		                      request.setAttribute("errors", errors);
		                      request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
		                      return;					              
					              }                      
                      }
                      else
                      {			              
				                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
				                errors.add("particelleP", error);
				                request.setAttribute("errors", errors);
				                request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
				                return;
				              }
			              }
			              //legenda 2 ***** fine ******
			              
			              
			              
			              
			              //Controllo che non si asservimento
			              //legenda 4 *****inizio ******
	                  if(stvoTmp.getElencoConduzioni()[0].getIdTitoloPossesso().longValue() == 5)
	                  {
	                    error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_ASSERV);
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                    return;
	                  }
	                  //legenda 4 *****fine*******          
			              
			              
			            }
			            else
			            {
			              //legenda 8 *****inizio*****
			              if(!gaaFacadeClient.isParticellaInPotenziale(anagAziendaVO.getIdAzienda(), stvoTmp.getIdParticella(), 
			                DateUtils.getCurrentYear()))
                    {
                      error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_POTENZ);
                      errors.add("particelleP", error);
                      request.setAttribute("errors", errors);
                      request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
                      return;
                    }
                    //legenda 8 *****fine*****			            
			            }
		              
		              
		              //Controllo se esistono altri doc IS per questa conduzione
		              //legenda 3 ******inizio ******
		              if(gaaFacadeClient.exitsOtherDocISForParticellaAndAzienda(stvoTmp.getIdParticella().longValue(), 
		                anagAziendaVO.getIdAzienda().longValue(), null))
		              {
		                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_OTHER_DOC);
		                errors.add("particelleP", error);
		                request.setAttribute("errors", errors);
		                request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
		                return;
		              }
		              //legenda 3 ******fine ******
		              
		              
		              //legenda 7 ******inizio********
		              if((SolmrConstants.FASE_IST_RIESAM_FOTO == documentoVO.getFaseIstanzaRiesame())
		                || (SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame()))
		              {
		                //Verifico che nn siano presenti altre fotointerpretazioni valide per tale particella
	                  if(gaaFacadeClient.existAltraFaseFotoParticella(anagAziendaVO.getIdAzienda().longValue(), 
	                    stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(new java.util.Date()),
	                    new Long(documentoVO.getFaseIstanzaRiesame()).longValue()))
	                  {
	                    error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_OTHER_DOC_FOTO);
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                    return;
	                  }		              
		              }
		              //legenda 7 ******fine********
		              
		              
		              
		              //Controllo che la priorità di lavorazione e le note per la stessa particella
		              //siano identiche (DISTINCT nel batch IS)
		              //legenda 5 ******inizio*****
		              if(hLavNote.get(stvoTmp.getIdParticella()) != null)
		              {
		                String valPrioNote = stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria();
		                if(Validator.isNotEmpty(stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
		                {
		                  valPrioNote += "_"+stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote();
		                }   
		                if(!hLavNote.get(stvoTmp.getIdParticella()).equalsIgnoreCase(valPrioNote))
		                {
		                  error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_PRIOR_NOTE);
		                  errors.add("particelleP", error);
		                  request.setAttribute("errors", errors);
		                  request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
		                  return;
		                }
		              }
		              else
		              {
		                String valPrioNote = stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria();
		                if(Validator.isNotEmpty(stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
		                {
		                  valPrioNote += "_"+stvoTmp.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote();
		                }                  
		                hLavNote.put(stvoTmp.getIdParticella(), valPrioNote);
		              }
		              //legenda 5 ******fine*****
		              
		              //Solo per particelle "normali"
                  if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
                  {
			              //Nuovi controlli fasi sucessive fotoint
			              if((SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame())
			                || (SolmrConstants.FASE_IST_RIESAM_SOPRA == documentoVO.getFaseIstanzaRiesame()))
			              {
			                boolean isPiemontese = false;
			                ProvinciaVO provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(stvoTmp.getComuneParticellaVO().getSiglaProv());
					            if(provinciaControlloVO != null) 
					            {
					              if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
					              {
					                isPiemontese = true;    
					              }
					            }
					            
					            if(!isPiemontese)
	                    {
	                      error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_NO_PIEMO);
	                      errors.add("particelleP", error);
	                      request.setAttribute("errors", errors);
	                      request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                      return;
	                    }
		                  
	                    
	                    
	                    
	                    if(SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame())
			                {
			                
			                  //legenda 1 ******inizio ******
		                    if(gaaFacadeClient.isNotPossibleIstanzaRiesameFaseSuccessiva(anagAziendaVO.getIdAzienda().longValue(),
		                      stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, 
		                      DateUtils.getCurrentYear().intValue(), SolmrConstants.PARAMETRO_GGFOTORIC))
		                    {
		                      error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_FASE_PREC);
		                      errors.add("particelleP", error);
		                      request.setAttribute("errors", errors);
		                      request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
		                      return;
		                    }                   
                        //legenda 1 ******fine*****
			                
			                  //legenda 6 *****inizio *****
			                  if(gaaFacadeClient.isDataSospensioneScaduta(anagAziendaVO.getIdAzienda().longValue(), 
	                          stvoTmp.getIdParticella().longValue(), DateUtils.getCurrentYear().intValue()))
	                      {
	                        error = new ValidationError("La particella "+componiNomeParticella(stvoTmp)+ " e'' rimasta "+
	                          "in stato sospeso oltre i termini consentiti per la presentazione di documentazione.");
	                        errors.add("particelleP", error);
	                        request.setAttribute("errors", errors);
	                        request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                        return;
	                      }
	                      
	                      
			                  /*if(!gaaFacadeClient.exsitsDocFaseIstanzaRiesameFasePrec(anagAziendaVO.getIdAzienda().longValue(), 
			                      SolmrConstants.FASE_IST_RIESAM_FOTO, stvoTmp.getIdParticella().longValue(), SolmrConstants.PARAMETRO_GGFOTORIC))
			                  {
			                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " non risulta "+
			                      "nessun documento di Istanza di riesame per la fase precedente");
			                    errors.add("particelleP", error);
			                    request.setAttribute("errors", errors);
			                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
			                    return;
			                  }*/
			                  
			                  if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	                          stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, SolmrConstants.PARAMETRO_GGFOTO))
	                      {
	                        error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " e'' scaduto "+
	                          "il termine per la presentazione di questa fase di Istanza di riesame");
	                        errors.add("particelleP", error);
	                        request.setAttribute("errors", errors);
	                        request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                        return;
	                      }
	                      
	                      //legenda 6 *****fine *****
			                }                
	                
			                if(SolmrConstants.FASE_IST_RIESAM_SOPRA == documentoVO.getFaseIstanzaRiesame())
			                {
			                  //legenda 6 *****inizio*****
			                  /*if(!gaaFacadeClient.exsitsDocFaseIstanzaRiesameFasePrec(anagAziendaVO.getIdAzienda().longValue(), 
	                          SolmrConstants.FASE_IST_RIESAM_CONTRO, stvoTmp.getIdParticella().longValue(), SolmrConstants.PARAMETRO_GGCONTRORIC))
			                  {
			                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " non risulta "+
	                          "nessun documento di Istanza di riesame per la fase precedente");
			                    errors.add("particelleP", error);
			                    request.setAttribute("errors", errors);
			                    request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
			                    return;
			                  }*/
			                  
			                  
			                  //legenda 1 ******inizio ******
		                    if(gaaFacadeClient.isNotPossibleIstanzaRiesameFaseSuccessiva(anagAziendaVO.getIdAzienda().longValue(),
		                      stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_CONTRO, DateUtils.getCurrentYear().intValue(), 
		                      SolmrConstants.PARAMETRO_GGCONTRORIC))
		                    {
		                      error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_FASE_PREC);
		                      errors.add("particelleP", error);
		                      request.setAttribute("errors", errors);
		                      request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
		                      return;
		                    }
		                    //legenda 1 ******fine ******
			                  
			                  
			                  if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	                          stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_CONTRO, SolmrConstants.PARAMETRO_GGCONTRO))
	                      {
	                        error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " e'' scaduto "+
	                          "il termine per la presentazione di questa fase di Istanza di riesame");
	                        errors.add("particelleP", error);
	                        request.setAttribute("errors", errors);
	                        request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	                        return;
	                      }
	                      
	                      //legenda 6 *****fine*****
			                                  
			                }
			              }
                    
                    
                    
		              
		              
		              
		              } //Fine particelle "normali"
		              
		               
		              
		              
		              
		            } //For elenco particelle
		          }
			        
      
            
      
              
              
              
            }  //Fine controlli particelle istanza di riesame
          }
        }
        
        
        
        
        
        
      }
       // Se è stato scelto un documento di tipo "Z"(zootecnico)...
      else if(tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)) 
      {
        String idContoCorrente = request.getParameter("idContoCorrente");
        if(Validator.isEmpty(idContoCorrente)) 
        {
          error = new ValidationError(AnagErrors.ERR_INSERT_DOCUMENTO_KO_CONTO_CORRENTE);
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
          return;
        }
        
        
        documentoVO.setIdContoCorrente(new Long(idContoCorrente));
      
      }
   		// Se il documento prevede l'obbligo di protocollo o l'utente ha selezionato l'opzione di protocollare il documento
   		if(tipoDocumentoVO.getFlagObbligoProtocollo().equalsIgnoreCase(SolmrConstants.FLAG_S) 
        || Validator.isNotEmpty(documentoVO.getProtocolla())) 
      {
      	// Se l'utente non è un intermediario e non è un OPR GESTORE
      	if(!ruoloUtenza.isUtenteIntermediario() && !ruoloUtenza.isUtenteOPRGestore()) 
        {
     			// Contatto il servizio di comune per reperire la sigla amministrazione
     			String siglaAmministrazione = null;
    	 		try 
          {
       			AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient
              .serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
       			// Se comune mi restituisce l'amministrazione di competenza, recupero la sigla per
       			// l'inserimento del documento
       			if(ammCompetenzaVO != null) 
            {
       				siglaAmministrazione = ammCompetenzaVO.getSiglaAmministrazione();
         			ruoloUtenza.setSiglaAmministrazione(siglaAmministrazione);
       			}
       			// Altrimenti segnalo l'errore perchè questa informazione è indispensabile per poter inserire
       			// correttamente il documento
       			else 
            {
       				error = new ValidationError(AnagErrors.ERR_INSERT_DOCUMENTO_KO_FOR_COMUNE);
       				errors.add("error", error);
       				request.setAttribute("errors", errors);
       				request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
       				return;
       			}
     			}
     			catch(SolmrException se) 
          {
       			error = new ValidationError(AnagErrors.ERR_INSERT_DOCUMENTO_KO_FOR_COMUNE);
       			errors.add("error", error);
       			request.setAttribute("errors", errors);
       			request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
       			return;
     			}
   		  }
      }

   		// Se tutti i controlli sono andati a buon fine effettuo l'inserimento
   		Long idDocumento = null;
   		try 
      {
   			documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
   			documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
			  documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
     		idDocumento = anagFacadeClient.inserisciDocumento(documentoVO, ruoloUtenza, DateUtils.getCurrentYear().toString(), elencoProprietari, elencoParticelle, null);
   		}
   		catch(SolmrException se) 
      {
     		SolmrLogger.info(this, " - documentoInserisciCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_DOCUMENTO+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
   		}

   		
   		session.removeAttribute("documentoVO");
   		session.removeAttribute("elencoProprietari");
      session.removeAttribute("elencoParticelle");
      //session.removeAttribute("particelleAssociate");
      //session.removeAttribute("particelleDaAssociare");
      session.removeAttribute("elencoTipiUsoSuoloPrimario");   
   		//request.setAttribute("idDocumento", idDocumento);
   		%>
        <jsp:forward page= "<%= documentoConfermaInserisciUrl %>" >
          <jsp:param name="idDocumento" value="<%=idDocumento%>"/>
        </jsp:forward>
   		<%
      return;
   		
   		
    }
  }
 	// E' stato selezionato il pulsante "annulla"
 	else if(request.getParameter("annulla") != null) 
  {
   	// Pulisco la sessione
   	session.removeAttribute("documentoVO");
   	session.removeAttribute("elencoProprietari");
   	session.removeAttribute("elencoParticelle");
    //session.removeAttribute("particelleAssociate");
    //session.removeAttribute("particelleDaAssociare");
    session.removeAttribute("elencoTipiUsoSuoloPrimario");   
   	// Torno alla pagina della ricerca/elenco dei documenti
   	%>
     	<jsp:forward page="<%= documentiElencoCtrlUrl %>" />
   	<%
  }
 	// E' stato selezionato il pulsante "inserisci" relativo all'elenco dei proprietari
 	else if(request.getParameter("inserisci") != null) 
  {
  	// Recupero il codice fiscale
   	String codiceFiscale = request.getParameter("codiceFiscale");
   	String denominazione = request.getParameter("denominazione");
   	// Controllo che sia valorizzato
   	if(!Validator.isNotEmpty(codiceFiscale)) 
    {
   		errors.add("codiceFiscale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
   		return;
   	}
    // Se lo è controllo che sia un codice fiscale o una partita iva valida
    else 
    {
     	if(codiceFiscale.length() == 16) 
      {
      	if(!Validator.controlloCf(codiceFiscale)) 
        {
        	errors.add("codiceFiscale", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
		      request.setAttribute("errors", errors);
	       	request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
	       	return;
       	}
     	}
     	else 
      {
      	if(!Validator.controlloPIVA(codiceFiscale)) 
        {
        	errors.add("codiceFiscale", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          request.setAttribute("errors", errors);
         	request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
         	return;
       	}
     	}
   	}
		// Recupero il parametro DOAT
		String parametroDOAT = null;
		try 
    {
			parametroDOAT = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DOAT);
		}
		catch(SolmrException se) 
    {
   		messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_DOAT;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      		<jsp:forward page="<%= documentiElencoUrl %>" />
   		<%
 		}
		// Se il parametro DOAT == S
		boolean isValidato = false;
		String denomSian = "";
		if(parametroDOAT.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
			// Richiamo il WEB-SERVICE SIAN per recuperari i dati relativi all'anagrafe tributaria
 			SianAnagTributariaVO anagTrib = null;
 			try 
      {
 				anagTrib = anagFacadeClient.ricercaAnagrafica(
          codiceFiscale.toUpperCase(), ProfileUtils.getSianUtente(ruoloUtenza));
 				if(anagTrib.getCodiceFiscale().length() == 16) 
        {
          //Possono essere a null
          if((anagTrib.getNome() != null)
            && (anagTrib.getCognome() != null))
          {
 					  denomSian = anagTrib.getCognome()+" "+anagTrib.getNome();
          }
 				}
 				else 
        {
          //Può essere a null
          if(anagTrib.getDenominazione() != null)
          {
 					  denomSian = anagTrib.getDenominazione();
          }
 				}
 			}
 			catch(SolmrException se) 
      {
 				// Se il SIAN va in errore non blocco l'operazione
 			}
			// Se l'oggetto è diverso da null e quindi il CUAA indicato è censito
 			// su AT
 			if(anagTrib != null) 
      {
 				// Controllo la congruenza tra il CUAA del proprietario indicato
 				// e quello reperito dal SIAN
 				if(!codiceFiscale.equalsIgnoreCase(anagTrib.getCodiceFiscale())) 
        {
     			errors.add("codiceFiscale", new ValidationError(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_NOT_EQUAL_AT));
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
        	return;
 				}
			  // Altrimenti setto il flag relativo alla validazione del cuaa
     		else 
        {
          //Se la denominazione non è null valido altrimenti no...
          if(Validator.isNotEmpty(denomSian))
          {
     			  isValidato = true;
          }
     		}
 			}
		}
   	if(elencoProprietari == null) 
    {
    	elencoProprietari = new Vector();
   	}
   	DocumentoProprietarioVO documentoProprietarioVO = new DocumentoProprietarioVO();
   	// Se non c'erano elementi nel vettore inserisco subito il risultato
   	if(elencoProprietari.size() == 0) 
    {
    	documentoProprietarioVO.setCuaa(codiceFiscale.toUpperCase());
    	if(isValidato) 
      {
     		documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_S);
     		documentoProprietarioVO.setDenominazione(denomSian.toUpperCase());
     	}
     	else 
      {
     		documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
     		documentoProprietarioVO.setDenominazione(denominazione.toUpperCase());
     	}
     	elencoProprietari.add(documentoProprietarioVO);
   	}
   	// Altrimenti controllo che il codice fiscale/partita iva non sia già stato inserito
   	else 
    {
    	boolean isCensito = false;
    	for(int i = 0; i < elencoProprietari.size(); i++) 
      {
      	DocumentoProprietarioVO documentoElencoProprietariVO = (DocumentoProprietarioVO)elencoProprietari.elementAt(i);
      	if(documentoElencoProprietariVO.getCuaa().equalsIgnoreCase(codiceFiscale)) 
        {
        	isCensito = true;
        	break;
       	}
     	}
     	if(!isCensito) 
      {
     		documentoProprietarioVO.setCuaa(codiceFiscale.toUpperCase());
     		if(isValidato) 
        {
       		documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_S);
       		documentoProprietarioVO.setDenominazione(denomSian.toUpperCase());
       	}
       	else 
        {
       		documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
       		documentoProprietarioVO.setDenominazione(denominazione.toUpperCase());
       	}
       	elencoProprietari.add(documentoProprietarioVO);
     	}
     	else 
      {
     		errors.add("codiceFiscale", new ValidationError(AnagErrors.ERR_PROPRIETARIO_DOCUMENTO_GIA_INSERITO));
    	 	request.setAttribute("errors", errors);
     		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     		return;
   		}
   	}
   	// Metto il vettore in sessione
   	session.setAttribute("elencoProprietari", elencoProprietari);
   	// Torno alla pagina di inserimento
   	%>
      	<jsp:forward page="<%= documentoInserisciUrl %>" />
   	<%
 	}
   	// E' stato selezionato il pulsante "elimina" relativo all'elenco dei proprietari
	else if(request.getParameter("elimina") != null) 
  {
  	// Recupero gli elementi da eliminare
   	String[] elementsToRemove = request.getParameterValues("countProprietario");
   	// Controllo che sia stato selezionato qualcosa dall'utente
   	if(elementsToRemove == null || elementsToRemove.length == 0) {
     		errors.add("error", new ValidationError(AnagErrors.ERRORE_PROPRIETARIO_DOCUMENTO_ELIMA_KO));
     		request.setAttribute("errors", errors);
     		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
     		return;
   	}
   	// Se sì, elimino gli elementi selezionati
   	else 
    {
    	if(elementsToRemove.length == elencoProprietari.size()) 
      {
      	elencoProprietari.removeAllElements();
    	}
     	else 
      {
      	for(int i = 0; i < elementsToRemove.length; i++) 
        {
          if(((String)elementsToRemove[i]).equalsIgnoreCase("0") || elementsToRemove.length == 1) 
          {
          	elencoProprietari.removeElementAt(Integer.parseInt((String)elementsToRemove[i]));
         	}
         	else 
          {
       			elencoProprietari.removeElementAt(Integer.parseInt((String)elementsToRemove[i]) - 1);
     			}
       	}
     	}
    }
   	// Metto il vettore in sessione
   	session.setAttribute("elencoProprietari", elencoProprietari);
   	// Torno alla pagina di inserimento
   	%>
      	<jsp:forward page="<%= documentoInserisciUrl %>" />
   	<%
 	}
 	// E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle
 	else if(request.getParameter("eliminaParticella") != null) 
  {
  	// Recupero gli elementi da eliminare
   	String[] elementsToRemove = request.getParameterValues("idConduzioneParticella");
   	Vector temp = null;
   	// Controllo che sia stato selezionato qualcosa dall'utente
   	if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
   		errors.add("error", new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_ELIMA_KO));
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
   		return;
   	}
   	// Se sì, elimino gli elementi selezionati
   	else 
    {
   		if(elementsToRemove.length == elencoParticelle.length) 
      {
     		session.removeAttribute("elencoParticelle");
   		}
   		else 
      {
     		Hashtable hash = new Hashtable();
     		for(int i =0; i < elencoParticelle.length; i++) 
        {
     			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
     			hash.put(storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella(), storicoParticellaVO);
     		}
     		for(int j = 0; j < elementsToRemove.length; j++) 
        {
       		hash.remove(Long.decode((String)elementsToRemove[j]));
     		}
     		temp = new Vector();
     		Enumeration enumeration = hash.elements();
     		while(enumeration.hasMoreElements()) 
        {
       		temp.add((StoricoParticellaVO)enumeration.nextElement());
     		}
     		
     		
     		if(Validator.isNotEmpty(temp) && temp.size() > 0)
		    {
		      Collections.sort(temp, new StoricoParticellaComparator());
		    }
     		
     		
   		}
    }
   	// Metto il vettore in sessione
   	if(temp != null && temp.size() > 0) 
    {
   		session.setAttribute("elencoParticelle", (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]));
   	}
   	// Torno alla pagina di inserimento
   	%>
      	<jsp:forward page="<%= documentoInserisciUrl %>" />
   	<%
  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle associate (correttiva terreni)
  /*else if(request.getParameter("eliminaParticellaAssociata") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idParticellaAssociata");
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
        errors.add("error", new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_ELIMA_KO));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoInserisciUrl).forward(request, response);
        return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      if(elementsToRemove.length == particelleAssociate.size())
      { 
        session.removeAttribute("particelleAssociate");
      }
      else 
      {
        for (int i = elementsToRemove.length - 1; i >= 0; i--)
            particelleAssociate.remove(Integer.parseInt(elementsToRemove[i]));
        session.setAttribute("particelleAssociate",particelleAssociate);    
      }
    }
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= documentoInserisciUrl %>" />
    <%
  }*/

	//  Vado alla pagina di inserimento
  %>
  	<jsp:forward page="<%= documentoInserisciUrl %>" />
  	
  	
<%! 
      private String componiNomeParticella(StoricoParticellaVO stvoTmp)
      {
        String nomeParticella = "Comune-"+stvoTmp.getComuneParticellaVO().getDescom()+" ("+stvoTmp.getComuneParticellaVO().getSiglaProv()+") ";
        if(Validator.isNotEmpty(stvoTmp.getSezione()))
        {
          nomeParticella = nomeParticella+" Sez-"+(stvoTmp.getSezione());
        }
        if(Validator.isNotEmpty(stvoTmp.getFoglio()))
        {
          nomeParticella = nomeParticella+" Fgl-"+(stvoTmp.getFoglio());
        }
        if(Validator.isNotEmpty(stvoTmp.getParticella()))
        {
          nomeParticella = nomeParticella+" Part-"+(stvoTmp.getParticella());
        }
        if(Validator.isNotEmpty(stvoTmp.getSubalterno()))
        {
          nomeParticella = nomeParticella+" Sub-"+(stvoTmp.getSubalterno());
        }
        
        
        return nomeParticella;
      }


%>
