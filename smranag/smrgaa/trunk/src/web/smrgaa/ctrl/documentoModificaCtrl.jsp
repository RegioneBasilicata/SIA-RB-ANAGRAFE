<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.comune.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	//Setto l'area di provenienza
  request.setAttribute("pageFrom", "documentale");

	String iridePageName = "documentoModificaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String documentiElencoUrl = "/view/documentiElencoView.jsp";
  String documentoModificaUrl = "/view/documentoModificaView.jsp";
  String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione modifica documenti."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String actionUrl = "../layout/documentiElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
 
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoModificaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  DocumentoVO documentoControlloVO = null;
  // Recupero l'id del documento che si intende modificare
  Long idDocumento = null;
  //**************************************
  //Parametri usati per l'istannza di riesame
  //vIdParticelleSuDB memorizza le particelle che sono presenti sul db prima della modifica.
  Vector<Long> vIdParticelleSuDb = null;
  //vIdParticelleNew memorizza le particelle che sono presenti dopo la modifica.
  Vector<Long> vIdParticelleNew = null;
  //**************************************
  //Arrivo dalla pagina visualizzaFIleAllegatoCtrl con un errore
  /*if(session.getAttribute("idDocumentoErr") != null)
  {
    idDocumento = (Long)session.getAttribute("idDocumentoErr");
    session.removeAttribute("idDocumentoErr");
  }
  else
  {
    idDocumento = Long.decode(request.getParameter("idDocumento"));
  }*/
  
  idDocumento = Long.decode(request.getParameter("idDocumento"));
  String[] elencoId = request.getParameterValues("idDocumento");
  ValidationErrors errors = new ValidationErrors();
  ValidationError error = null;
  String messaggioErrore = null;
  
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

	//  Ricerco sul DB i dati del documento selezionato
  try 
  {
    //documentoControlloVO = anagFacadeClient.getDettaglioDocumento(idDocumento, false); 
    documentoControlloVO = anagFacadeClient.getDettaglioDocumento(idDocumento, true); //Nuova modifica Luca 19/04/2010
     
    if(documentoVO == null) 
    {
      documentoVO = documentoControlloVO;
    
      //se il documento è di tipo correttiva terreni devo caricare anche le particelle associate
      /*if(documentoVO.getTipoDocumentoVO() != null 
          && documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
      { 
        particelleAssociate=anagFacadeClient.getParticelleDocCor(idDocumento);
        session.setAttribute("particelleAssociate",particelleAssociate);
      }*/
			// Se passo i controlli metto l'oggetto in sessione
    	session.setAttribute("documentoModificaVO", documentoVO);
    }
    else 
    {
      documentoVO.setTipoDocumentoVO(documentoControlloVO.getTipoDocumentoVO());
      documentoVO.setvAllegatoDocumento(gaaFacadeClient.getElencoFileAllegati(idDocumento));
    }
    
    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
    {
      java.util.Date firstDataInserimento = gaaFacadeClient.getFirstDataInserimentoDocumento(
        documentoVO.getIdDocumento().longValue());
      request.setAttribute("firstDataInserimento", firstDataInserimento);
    }
  }
  catch(SolmrException se) 
  {
    errors.add("error", new ValidationError(AnagErrors.ERRORE_KO_DOCUMENTO));
    request.setAttribute("elencoId", elencoId);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(documentiElencoUrl).forward(request, response);
    return;
  }
  // Controllo che il documento selezionato non abbia l'id_stato_documento valorizzato
  if(Validator.isNotEmpty(documentoControlloVO.getIdStatoDocumento())) 
  {
    errors.add("error", new ValidationError(AnagErrors.ERRORE_MODIFICA_DOCUMENTO_KO));
    request.setAttribute("errors", errors);
    request.setAttribute("elencoId", elencoId);
    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
    return;
  }
  // Controllo che la tipologia di documento selezionata non sia scaduta
  if(documentoVO.getTipoDocumentoVO().getDataFineValidita() != null) 
  {
    errors.add("error", new ValidationError(AnagErrors.ERRORE_MODIFICA_DOCUMENTO_KO_FOR_TIPOLOGIA));
    request.setAttribute("errors", errors);
    request.setAttribute("elencoId", elencoId);
    request.getRequestDispatcher(documentiElencoCtrlUrl).forward(request, response);
    return;
  }
  
  
  // COMBO CAUSALE MODIFICA
  Vector<BaseCodeDescription> elencoCausaleModifica = null;
  try 
  {
    elencoCausaleModifica = gaaFacadeClient.getCausaleModificaDocumentoValid();
  }
  catch(SolmrException se) 
  {
    messaggioErrore = se.getMessage();
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= documentiElencoUrl %>" />
    <%
  }
  request.setAttribute("elencoCausaleModifica", elencoCausaleModifica);
  
  
  
  // Ricerco la combo delle varietà se il tipo documento selezionato è di
  // tipo correttiva terreni
  if(documentoVO.getTipoDocumentoVO() != null 
    && documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
    .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
  {
    // Recupero i valori relativi ai tipi uso suolo(attivi e non) utilizzati dall'azienda in esame
    Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
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
        request.getRequestDispatcher(documentiElencoUrl).forward(request, response);
        return;
      }
    }
  }

	// Ricerco la combo del titolo possesso e del caso particolare
	// solo se il tipo documento selezionato è di tipo territoriale
	FaseRiesameDocumentoVO faseRiesameDocumentoVO = null;
  if(documentoVO.getTipoDocumentoVO() != null 
   && (documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
    .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
   || documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
    .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))) 
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
    
    
    String parametroCancPartIstanzaF2 = null;
    try 
    {
      parametroCancPartIstanzaF2 = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_CANC_PART_ISTANZA_F2);
      request.setAttribute("parametroCancPartIstanzaF2", parametroCancPartIstanzaF2);
    }
    catch(SolmrException se) 
    {
      messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_CANC_PART_ISTANZA_F2;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= documentiElencoUrl %>" />
      <%
    }
    
    
    String isVisibleTastoElimina = "true";
    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
    {	    
	    
	    try 
	    {
	      if(!gaaFacadeClient.isVisibleTastoElimina(anagAziendaVO.getIdAzienda(), 
	        DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame()))
	      {
	        isVisibleTastoElimina = "false";
	      }
	    }
	    catch(SolmrException se) 
	    {
	      messaggioErrore = AnagErrors.ERRORE_KO_VALORE_TASTO_ELIMINA;
	      request.setAttribute("messaggioErrore", messaggioErrore);
	      %>
	        <jsp:forward page="<%= documentiElencoUrl %>" />
	      <%
	    }
	  }
	  request.setAttribute("isVisibleTastoElimina", isVisibleTastoElimina);
	  
	  
	  //Se istanza di riesame
    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
    {
      try 
      {    
        faseRiesameDocumentoVO = gaaFacadeClient
          .getFaseRiesameDocumentoByIdDocumento(documentoVO.getExtIdDocumento().longValue());
       
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
  if(documentoVO.getTipoDocumentoVO() != null 
   && documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
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
  
  

  // L'utente ha selezionato il tasto "conferma"
  if("conferma".equalsIgnoreCase(operazione)) 
  { 
    // Recupero i parametri, valorizzo il VO ed effettuo la validazione formale dei dati
    errors = documentoVO.validateUpdateDocumento(request);
    
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
      //Se non trovo erroti proseguo con il controllo sulla consuzione
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
    {
      errors.add("Fittizia", new ValidationError("Fittizia"));
    }

    // Se ci sono errori li visualizzo
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
      return;
    }
    // Se passo i controlli e il documento ha FLAG_ANAG_TERR = A
    if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) 
    {
      // Controllo che non esista già un documento con lo stesso numero documento ed ente rilascio inseriti dall'utente
      DocumentoVO documentoControlloAnagraficoVO = null;
      try 
      {
        documentoControlloAnagraficoVO = anagFacadeClient.findDocumentoVOBydDatiAnagrafici(documentoVO);
      }
      catch(SolmrException se) 
      {
        error = new ValidationError(se.getMessage());
        errors.add("numeroDocumento", error);
		    errors.add("enteRilascioDocumento", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
        return;
      }
      if(documentoControlloAnagraficoVO != null 
        && documentoControlloAnagraficoVO.getIdDocumento().compareTo(documentoVO.getIdDocumento()) != 0) 
      {
        error = new ValidationError(AnagErrors.ERR_DOCUMENTO_PRESENTE_FOR_NUMERO_ENTE_RILASCIO);
        errors.add("numeroDocumento", error);
        errors.add("enteRilascioDocumento", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
        return;
      }
    }
		// Se è stato scelto un documento di tipo "Z"(zootecnico)...
   	else if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) 
    {
		  // Effettuo i controlli di verifica solo se il cuaa soccidario è stato indicato
		  if(Validator.isNotEmpty(documentoVO.getCuaaSoccidario())) 
      {
     		// Verifico che il Cuaa soccidario non sia uguale a quello dell'azienda
  			// agricola
  			if(documentoVO.getCuaaSoccidario().equalsIgnoreCase(anagAziendaVO.getCUAA())) 
        {
  			  error = new ValidationError(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_EQUAL_CUAA_AZIENDA);
         	errors.add("cuaaSoccidario", error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
     			  anagTrib = anagFacadeClient.ricercaAnagrafica(
              documentoVO.getCuaaSoccidario().toUpperCase(), ProfileUtils.getSianUtente(ruoloUtenza));
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
     				if(!documentoVO.getCuaaSoccidario().equalsIgnoreCase(anagTrib.getCodiceFiscale())) 
            {
         		  error = new ValidationError(AnagErrors.ERRORE_KO_CUAA_SOCCIDARIO_NOT_EQUAL_AT);
              errors.add("cuaaSoccidario", error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
    else if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
     || documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
    {
      
    
      // Se FLAG_OBBLIGO_PROPRIETARIO è uguale a S
      if(documentoVO.getTipoDocumentoVO().getFlagObbligoProprietario().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
     	  // Controllo che sia stato inserito almeno un proprietario
       	if(documentoVO.getElencoProprietari() == null || documentoVO.getElencoProprietari().size() == 0) 
        {
          error = new ValidationError(AnagErrors.ERRORE_PROPRIETARIO_DOCUMENTO_KO);
         	errors.add("noProprietari", error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
         	return;
       	}
      }
  		// Se sono stati indicati dei proprietari
      if(documentoVO.getElencoProprietari() != null && documentoVO.getElencoProprietari().size() > 0) 
      {
  		  // Recupero il parametro DOAT
        String parametroDOAT = null;
  			Vector elencoErrori = new Vector();
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
          for(int i = 0; i < documentoVO.getElencoProprietari().size(); i++) 
          {
         	  DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)documentoVO.getElencoProprietari().elementAt(i);
         		// Richiamo il WEB-SERVICE SIAN per recuperari i dati relativi all'anagrafe tributaria
            SianAnagTributariaVO anagTrib = null;
            try 
            {
              anagTrib = anagFacadeClient.ricercaAnagrafica(documentoProprietarioVO.getCuaa().toUpperCase(), 
               ProfileUtils.getSianUtente(ruoloUtenza));
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
             	if(!documentoProprietarioVO.getCuaa().equalsIgnoreCase(anagTrib.getCodiceFiscale())) 
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
          for(int i = 0; i < documentoVO.getElencoProprietari().size(); i++) 
          {
         	  DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)documentoVO.getElencoProprietari().elementAt(i);
         		documentoProprietarioVO.setFlagValidato(SolmrConstants.FLAG_N);
         	}
        }
        // Se si sono verificate delle incongruenze con anagrafe tributaria
        // blocco l'operazione di inserimento
        if(elencoErrori.size() > 0 && isKo) 
        {
          request.setAttribute("elencoErrori", elencoErrori);
         	%>
            <jsp:forward page="<%= documentoModificaUrl %>" />
          <%
        }
      }
      // Controllo che sia stata inserita almeno una particella solo se FLAG_OBBLIGO_PARTICELLA == S
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
        && documentoVO.getTipoDocumentoVO().getFlagObbligoParticella().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //Se a S sono anche sicuro che sono in un istanza di riesame
        if(!"S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesameNoModTotale()))
        {
	        if(documentoVO.getElencoParticelle() == null || documentoVO.getElencoParticelle().size() == 0) {
	          error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_KO);
	          errors.add("noParticelle", error);
	          request.setAttribute("errors", errors);
	          request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	          return;
	        }
	      }
      }
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)
        && documentoVO.getTipoDocumentoVO().getFlagObbligoParticella().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //Se a S sono anche sicuro che sono in un istanza di riesame
        if(!"S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesameNoModTotale()))
        {
          if(documentoVO.getElencoParticelle() == null || documentoVO.getElencoParticelle().size() == 0)
          //|| particelleAssociate==null || particelleAssociate.size()==0 ) 
          {
            error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_ASS_DOCUMENTO_KO);
            errors.add("noParticelleAssociate", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
            return;
          }
        }
      }
      
      
      //Controllo correttezza lunghezza note
      //e setto i valori
      Vector<ValidationErrors> elencoErroriNote = new Vector<ValidationErrors>();
      int countErroriNote = 0;
      String[] arrNoteDocConduzione = request.getParameterValues("noteDocConduzione"); 
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)) 
      {
        if(Validator.isNotEmpty(documentoVO.getElencoParticelle()))
        {
          for(int j=0;j<documentoVO.getElencoParticelle().size();j++)
          {
            ValidationErrors errorsNoteParticelle = new ValidationErrors();            
            if(arrNoteDocConduzione[j].length() > 500)
            {
              errorsNoteParticelle.add("noteDocConduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
              countErroriNote++;
            }
            else
            {
              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)documentoVO.getElencoParticelle().get(j); 
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
            request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
            return;
          }
        }
      }
      
      
      arrNoteDocConduzione = request.getParameterValues("cessNoteDocConduzione"); 
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
      {
        if(Validator.isNotEmpty(documentoVO.getElencoParticelle()))
        {
          for(int j=0;j<documentoVO.getElencoParticelle().size();j++)
          {
            ValidationErrors errorsNoteParticelle = new ValidationErrors();            
            if(arrNoteDocConduzione[j].length() > 500)
            {
              errorsNoteParticelle.add("cessNoteDocConduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
              countErroriNote++;
            }
            else
            {
              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)documentoVO.getElencoParticelle().get(j); 
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
            request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
            return;
          }
        }
      }
      
      
      
      String[] arrLavorazionePrioritaria = request.getParameterValues("lavorazionePrioritaria"); 
      //Se istanza di riesame setto il valore della priorità lavorazione
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)) 
      {
        if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
        {
          if(Validator.isNotEmpty(documentoVO.getElencoParticelle()))
          {
            for(int j=0;j<documentoVO.getElencoParticelle().size();j++)
            {
              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)documentoVO.getElencoParticelle().get(j);
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
        }
      }
      
      
      
      
      
      
      
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)) 
      {
        //Controlli se documenti istanza di riesame
        if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
        {
          if(Validator.isNotEmpty(documentoVO.getElencoParticelle()))
          {
          
            if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
            {
              //legenda 6  ***inizio*****
              if(gaaFacadeClient.isSitiConvocaValid(anagAziendaVO.getIdAzienda().longValue(),
                 DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), SolmrConstants.FASE_IST_RIESAM_CONTRO))
              {
                error = new ValidationError(AnagErrors.ERRORE_DOC_ANNUL_MOD_IST_RIE_CONVOCA);
                errors.add("particelleP", error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
                return;
              }
              //legenda 6  ***fine*****
            
            }
          
            
            //Vedo almeno una particella a video, mi ricavo le particelle presenti su DB!!
            //per controllare se esiste già una istanza di riesame per le particella inserite su altro documento
            Vector<StoricoParticellaVO> vStoricPartDB = anagFacadeClient
              .getListParticelleByIdDocumentoAlPianoCorrente(documentoVO.getIdDocumento());
            if(vStoricPartDB.size() > 0)
            {
              for(int z=0;z<vStoricPartDB.size();z++)
              {
	              if(vIdParticelleSuDb == null)
	              {
	                vIdParticelleSuDb = new Vector<Long>();
	              }              
              
                if(!vIdParticelleSuDb.contains(vStoricPartDB.get(z).getIdParticella()))
                {
                  vIdParticelleSuDb.add(vStoricPartDB.get(z).getIdParticella());
                }               
              }              
            }
            //************************************************** 
          
          
            //Memorizzo la priorità di lavorazione e le note pe rimpedire due conduzioni con queste diverse
            //per la stessa particella
            HashMap<Long,String> hLavNote = new HashMap<Long,String>();
            for(int j=0;j<documentoVO.getElencoParticelle().size();j++)
            {
              StoricoParticellaVO stvoTmp = (StoricoParticellaVO)documentoVO.getElencoParticelle().get(j);
              
              //solo per particella "normali"
              if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
              {
                //Controlli P qualcosa...
                //devono tutte le particelle esclusivamente essere in p26
                //legenda 2 *****inizio****
	              int p30 = gaaFacadeClient.calcolaP30PlSql(stvoTmp.getIdStoricoParticella().longValue(), null);
	              if(p30 == 1)
	              {
	                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
					        errors.add("particelleP", error);
					        request.setAttribute("errors", errors);
					        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
					        return;
	              }
	              
	              int p25 = gaaFacadeClient.calcolaP25PlSql(stvoTmp.getIdStoricoParticella().longValue(), null);
	              if(p25 == 1)
	              {
	                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
	                errors.add("particelleP", error);
	                request.setAttribute("errors", errors);
	                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
		                  request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
		                  return;
                    }
                  }
                  else
                  {
		                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_P26);
		                errors.add("particelleP", error);
		                request.setAttribute("errors", errors);
		                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
		                return;
		              }
	              }
	              //legenda 2 *****fine****
	              
	              
	              
	              
	              //Controllo che non si asservimento
	              //legenda 4 *******inizio *******
	              if(stvoTmp.getElencoConduzioni()[0].getIdTitoloPossesso().longValue() == 5)
	              {
	                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_ASSERV);
	                errors.add("particelleP", error);
	                request.setAttribute("errors", errors);
	                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                return;
	              }
	              //legenda 4 *******fine *******
	              
	              
	            }
              else
              {
                //legenda 10 *****inizio*****
                //solo per quelle non presenti già su db
                if(!((vIdParticelleSuDb !=null) && vIdParticelleSuDb.contains(stvoTmp.getIdParticella())))
                {
	                if(!gaaFacadeClient.isParticellaInPotenziale(anagAziendaVO.getIdAzienda(), stvoTmp.getIdParticella(), 
	                  DateUtils.getCurrentYear()))
	                {
	                  error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_POTENZ);
	                  errors.add("particelleP", error);
	                  request.setAttribute("errors", errors);
	                  request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                  return;
	                }
	              }
                //legenda 10 *****fine*****                  
              }
              
              //Controllo se esistono altri doc IS per questa conduzione non evasi!!!
              //legenda 3 *****inizio *******
              if(gaaFacadeClient.exitsOtherDocISForParticellaAndAzienda(stvoTmp.getIdParticella().longValue(), 
                anagAziendaVO.getIdAzienda().longValue(), documentoVO.getIdDocumento().longValue()))
              {
                error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_OTHER_DOC);
                errors.add("particelleP", error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
                return;
              }
              //legenda 3 *****fine *******
              
              
              
              //legenda 9 ******inizio******
              if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                || (SolmrConstants.FASE_IST_RIESAM_CONTRO == documentoVO.getFaseIstanzaRiesame()))
              {
                //Verifico che nn siano presenti altre fotointerpretazioni valide per tale particella
                //ma solo per le particelle che sono stata inserite nuove!!!!
                if(!((vIdParticelleSuDb !=null) && vIdParticelleSuDb.contains(stvoTmp.getIdParticella())))
                {
	                if(gaaFacadeClient.existAltraFaseFotoParticella(anagAziendaVO.getIdAzienda().longValue(), 
	                  stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(documentoVO.getDataInserimento()),
	                  new Long(documentoVO.getFaseIstanzaRiesame()).longValue()))
	                {
	                  error = new ValidationError(AnagErrors.ERRORE_DOC_IST_RIE_PART_OTHER_DOC_FOTO);
	                  errors.add("particelleP", error);
	                  request.setAttribute("errors", errors);
	                  request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                  return;
	                }
	              }
              
              }
              //legenda 9 ******fine********
              
              
              //Controllo che la priorità di lavorazione e le note per la stessa particella
              //siano identiche (DISTINCT nel bact IS)
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
	                request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
              
              
              //Nuovi controlli fasi sucessive fotoint
              //Solo per particelle "normali"
              if("N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
              {
	              if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
	                || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
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
	                  request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                  return;
	                }
	                
	                
	                
	                
	                
	                if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
	                {
	                  if(gaaFacadeClient.isDataSospensioneScaduta(anagAziendaVO.getIdAzienda().longValue(), 
	                          stvoTmp.getIdParticella().longValue(), DateUtils.extractYearFromDate(documentoVO.getDataInserimento())))
	                  {
	                    error = new ValidationError("La particella "+componiNomeParticella(stvoTmp)+ " e'' rimasta "+
	                      "in stato sospeso oltre i termini consentiti per la presentazione di documentazione.");
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                    return;
	                  }
	                
	                                
	                  /*if(!gaaFacadeClient.exsitsDocFaseIstanzaRiesameFasePrec(anagAziendaVO.getIdAzienda().longValue(), 
	                      SolmrConstants.FASE_IST_RIESAM_FOTO, stvoTmp.getIdParticella().longValue(), SolmrConstants.PARAMETRO_GGFOTORIC))
	                  {
	                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " non risulta "+
	                      "nessun documento di Istanza di riesame per la fase precedente");
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                    return;
	                  }*/
	                  
	                  if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	                      stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, SolmrConstants.PARAMETRO_GGFOTO))
	                  {
	                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " e'' scaduto "+
	                      "il termine per la presentazione di questa fase di Istanza di riesame");
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                    return;
	                  }
	                }                
	            
	                if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA)
	                {
	                       
	                  /*if(!gaaFacadeClient.exsitsDocFaseIstanzaRiesameFasePrec(anagAziendaVO.getIdAzienda().longValue(), 
	                      SolmrConstants.FASE_IST_RIESAM_CONTRO, stvoTmp.getIdParticella().longValue(), SolmrConstants.PARAMETRO_GGCONTRORIC))
	                  {
	                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " non risulta "+
	                      "nessun documento di Istanza di riesame per la fase precedente");
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                    return;
	                  }*/
	                  
	                  
	                  if(!gaaFacadeClient.isPossCreateDocFaseIstanzaRiesameFaseSucc(anagAziendaVO.getIdAzienda().longValue(), 
	                      stvoTmp.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_CONTRO, SolmrConstants.PARAMETRO_GGCONTRO))
	                  {
	                    error = new ValidationError("Per la particella "+componiNomeParticella(stvoTmp)+ " e'' scaduto "+
	                      "il termine per la presentazione di questa fase di Istanza di riesame");
	                    errors.add("particelleP", error);
	                    request.setAttribute("errors", errors);
	                    request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
	                    return;
	                  }
	                                  
	                }
	                
	                
	                
	              }
	            }
              
              
              
              
              
              
              
              
            } //for elenco particelle
          }
        }
      }
      
      
      
      
   
      
      
      
    }
    else if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)) 
    {
      String idContoCorrente = request.getParameter("idContoCorrente");
      if(Validator.isEmpty(idContoCorrente)) 
      {
        error = new ValidationError(AnagErrors.ERR_INSERT_DOCUMENTO_KO_CONTO_CORRENTE);
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
        return;
      }
      
      
      documentoVO.setIdContoCorrente(new Long(idContoCorrente));
    
    }
    
    if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))   
    { 
      //vado a leggere le particelle precedentemente associate al documento per vedere se sono state fatte modifiche o meno
      documentoControlloVO.setParticelleAssociate(anagFacadeClient.getParticelleDocCor(idDocumento));
      //documentoVO.setParticelleAssociate(particelleAssociate);
    }
  		
    // Se non ci sono particelle associate ma sono passato dalla pop-up
    // associa particelle...
    if(documentoVO.getElencoParticelle() != null && documentoVO.getElencoParticelle().size() == 0) 
    {
      //... forzo il valore a null in modo da permettere all'equals di funzionare
     	// correttamente
     	documentoVO.setElencoParticelle(null);
    }
    // Se è stato effettivamente modificato il documento    
    Vector<Object> diff = documentoControlloVO.equalsVect(documentoVO);
    if(diff !=null) 
    {
      // Verifico se sono stati modificati i dati "non chiave", note e causale modifica
      if(!documentoControlloVO.equalsForUpdate(documentoVO,diff)) 
      {
     	  // Modifico
     	  try 
        {
     	    documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
     	 		documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
     	 		documentoVO.setEsitoControllo(null);
     	    documentoVO.setDataEsecuzione(null);
     	    anagFacadeClient.aggiornaDocumento(documentoVO, ruoloUtenza, "modificaNote");
          
     	  }
     	  catch(SolmrException se) 
        {
     	    SolmrLogger.info(this, " - documentoModificaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_AGGIORNA_DOCUMENTO+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
     	  }
     	}
     	// Altrimenti provvedo a storicizzare
     	else 
      {
     	  documentoVO.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
        String note = request.getParameter("note");
     	  documentoVO.setNote(note.trim());
     	  // Se il documento prevede l'obbligo di protocollo o il documento selezionato possedeva già il
     	  // numero protocollo
     	  if(documentoVO.getTipoDocumentoVO().getFlagObbligoProtocollo().equalsIgnoreCase(SolmrConstants.FLAG_S) 
         || Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
        {
     	    // Se l'utente non è un intermediario
     	  	if(!ruoloUtenza.isUtenteIntermediario()) 
          {
     	      // Contatto il servizio di comune per reperire la sigla amministrazione
     	      String siglaAmministrazione = null;
     	      try 
            {
     	        AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
     	        siglaAmministrazione = ammCompetenzaVO.getSiglaAmministrazione();
     	        ruoloUtenza.setSiglaAmministrazione(siglaAmministrazione);
     	      }
     	      catch(SolmrException se) 
            {
     	        error = new ValidationError(AnagErrors.ERRORE_UPDATE_DOCUMENTO_KO_FOR_COMUNE);
     	        errors.add("error", error);
     	        request.setAttribute("errors", errors);
     	        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
     	        return;
     	      }
     	    }
     	  }
     	  try 
        {
     	    documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
     	    documentoVO.setEsitoControllo(null);
     	    documentoVO.setDataEsecuzione(null);
     	    if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesameNoModTotale()))
     	    {
     	      anagFacadeClient.aggiornaDocumentoIstanzaLimitato(documentoVO, ruoloUtenza, "storicizza");
     	    }
     	    else
     	    {     	    
     			  anagFacadeClient.aggiornaDocumento(documentoVO, ruoloUtenza, "storicizza");
     			}
     	  }
     	  catch(SolmrException se) 
        {
     	    SolmrLogger.info(this, " - documentoModificaCtrl.jsp - FINE PAGINA");
			    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_AGGIORNA_DOCUMENTO+".\n"+se.toString();
			    request.setAttribute("messaggioErrore",messaggio);
			    request.setAttribute("pageBack", actionUrl);
			    %>
			      <jsp:forward page="<%= erroreViewUrl %>" />
			    <%
			    return;
     	  }
     	}
      //session.removeAttribute("particelleAssociate");
      //session.removeAttribute("particelleDaAssociare");
     	// Vado alla pagina di ricerca/elenco dei documenti
  	  %>
  	    <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
  	  <%
    }
    // Altrimenti...
    else 
    {
      //session.removeAttribute("particelleAssociate");
      //session.removeAttribute("particelleDaAssociare");
      %>
        <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
      <%
    }
  }
	// L'utente ha selezionato il tasto "annulla"
  else if(request.getParameter("annulla") != null) 
  {
    //session.removeAttribute("particelleAssociate");
    //session.removeAttribute("particelleDaAssociare");
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
      request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
          request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
          return;
        }
      }
      else 
      {
        if(!Validator.controlloPIVA(codiceFiscale)) 
        {
          errors.add("codiceFiscale", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
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
   		  anagTrib = anagFacadeClient.ricercaAnagrafica(codiceFiscale.toUpperCase(), ProfileUtils.getSianUtente(ruoloUtenza));
   			if(anagTrib.getCodiceFiscale().length() == 16) 
        {
   			  denomSian = anagTrib.getCognome()+" "+anagTrib.getNome();
   			}
   			else 
        {
   			  denomSian = anagTrib.getDenominazione();
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
          request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
          return;
   			}
				// Altrimenti setto il flag relativo alla validazione del cuaa
       	else 
        {
       	  isValidato = true;
       	}
 			}
		}
    if(documentoVO.getElencoProprietari() == null) 
    {
      Vector elencoProprietari = new Vector();
      documentoVO.setElencoProprietari(elencoProprietari);
    }

    DocumentoProprietarioVO documentoProprietarioVO = new DocumentoProprietarioVO();
    // Se non c'erano elementi nel vettore inserisco subito il risultato
    // Se non c'erano elementi nel vettore inserisco subito il risultato
    if(documentoVO.getElencoProprietari().size() == 0) 
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
      
      documentoVO.getElencoProprietari().add(documentoProprietarioVO);
    }
    // Altrimenti controllo che il codice fiscale/partita iva non sia già stato inserito
    else 
    {
      boolean isCensito = false;
      for(int i = 0; i < documentoVO.getElencoProprietari().size(); i++) 
      {
        DocumentoProprietarioVO documentoElencoProprietariVO = (DocumentoProprietarioVO)documentoVO.getElencoProprietari().elementAt(i);
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
        
        documentoVO.getElencoProprietari().add(documentoProprietarioVO);
      }
      else 
      {
        errors.add("codiceFiscale", new ValidationError(AnagErrors.ERR_PROPRIETARIO_DOCUMENTO_GIA_INSERITO));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
        return;
      }
    }
    // Torno alla pagina di modifica
    %>
      <jsp:forward page="<%= documentoModificaUrl %>" />
    <%
  }
  // E' stato selezionato il pulsante "elimina" relativo all'elenco dei proprietari
  else if(request.getParameter("elimina") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("countProprietario");

    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_PROPRIETARIO_DOCUMENTO_ELIMA_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      if(elementsToRemove.length == documentoVO.getElencoProprietari().size()) 
      {
        documentoVO.getElencoProprietari().removeAllElements();
      }
      else 
      {
        for(int i = 0; i < elementsToRemove.length; i++) 
        {
          if(((String)elementsToRemove[i]).equalsIgnoreCase("0") || elementsToRemove.length == 1) 
          {
            documentoVO.getElencoProprietari().removeElementAt(Integer.parseInt((String)elementsToRemove[i]));
          }
          else 
          {
            documentoVO.getElencoProprietari().removeElementAt(Integer.parseInt((String)elementsToRemove[i]) - 1);
          }
        }
      }
  	}
    // Torno alla pagina di modifica
    %>
      <jsp:forward page="<%= documentoModificaUrl %>" />
    <%
  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle
  else if(request.getParameter("eliminaParticella") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idConduzioneParticella");
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_ELIMA_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {      
      
    
      Vector elencoParticelle = new Vector();
      if(elementsToRemove.length == documentoVO.getElencoParticelle().size()) 
      {
        documentoVO.getElencoParticelle().removeAllElements();
      }
      else 
      {
        Hashtable hash = new Hashtable();
        String[] arrNoteDocConduzione = request.getParameterValues("noteDocConduzione"); 
        for(int i =0; i < documentoVO.getElencoParticelle().size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)documentoVO.getElencoParticelle().elementAt(i);
          if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
          {
            storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].setNote(arrNoteDocConduzione[i]);              
          }
          else
          {
            DocumentoConduzioneVO docCondVoTmp = new DocumentoConduzioneVO();
            docCondVoTmp.setNote(arrNoteDocConduzione[i]);
            storicoParticellaVO.getElencoConduzioni()[0].setElencoDocumentoConduzione((DocumentoConduzioneVO[])new DocumentoConduzioneVO[]{docCondVoTmp});
          }
          
          hash.put(storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella(), storicoParticellaVO);
        }
        for(int j = 0; j < elementsToRemove.length; j++) 
        {
          hash.remove(Long.decode((String)elementsToRemove[j]));
        }
        Enumeration enumeration = hash.elements();
        while(enumeration.hasMoreElements()) 
        {
          elencoParticelle.add((StoricoParticellaVO)enumeration.nextElement());
        }
        
        
        if(Validator.isNotEmpty(elencoParticelle) && elencoParticelle.size() > 0)
        {
          Collections.sort(elencoParticelle, new StoricoParticellaComparator());
        }
        
        
      }
      
      documentoVO.setElencoParticelle(elencoParticelle);
    }
    // Torno alla pagina di modifica
    %>
      <jsp:forward page="<%= documentoModificaUrl %>" />
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
        request.getRequestDispatcher(documentoModificaUrl).forward(request, response);
        return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      if(elementsToRemove.length == particelleAssociate.size()) 
        session.removeAttribute("particelleAssociate");
      else 
      {
        for (int i = elementsToRemove.length - 1; i >= 0; i--)
            particelleAssociate.remove(Integer.parseInt(elementsToRemove[i]));
        session.setAttribute("particelleAssociate",particelleAssociate);    
      }
    }
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= documentoModificaUrl %>" />
    <%
  }*/
	// Vado alla pagina di modifica del documento
  %>
    <jsp:forward page="<%= documentoModificaUrl %>" />
    
    
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

  
