<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.iride2.policy.entity.Actor" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@page import="javax.xml.bind.JAXBElement"%>
<%@ page import="javax.xml.namespace.QName"%>

<%

  String iridePageName = "newInserimentoAnagraficaImpreseEntiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoAnagraficaPrivatiUrl = "/view/newInserimentoAnagraficaPrivatiView.jsp";    
  String indietroUrl = "/ctrl/newInserimentoAziendaCtrl.jsp";
  String URL_ERRORE = "/view/erroreNuovaAziendaAAEPView.jsp";
  String pageBack= "../layout/newInserimentoAzienda.htm";
  
  
  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  String pageNext ="";
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
	  pageNext = "../layout/";
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
	  pageNext = "/layout/";
  pageNext += "newInserimentoUte.htm";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoAnagraficaPrivati.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  ValidationErrors errors = new ValidationErrors();


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_ANA3);
  request.setAttribute("testoHelp", testoHelp);  
  
  
  /*Vector<TipoTipologiaAziendaVO> collTipiAzienda = anagFacadeClient.getTipiTipologiaAzienda(null,null);
  request.setAttribute("collTipiAzienda", collTipiAzienda);*/
  
  /*Vector<CodeDescription> vTipoFormaGiuridica = anagFacadeClient.getCodeDescriptionsFormaGiuridica();
  request.setAttribute("vTipoFormaGiuridica", vTipoFormaGiuridica);*/
  
  
  String cuaaInserimento = request.getParameter("cuaaInserimento");
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = null;
  if(Validator.isNotEmpty(cuaaInserimento))
  {  
     aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizione(
        cuaaInserimento, new long[]{SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE,SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA});
     //resetto
     if(Validator.isNotEmpty(aziendaNuovaVO) 
        && (aziendaNuovaVO.getIdStatoRichiesta().compareTo(SolmrConstants.RICHIESTA_STATO_ANNULLAMENTO) ==0))
     {
       aziendaNuovaVO = null;
     }
  }
  else if(Validator.isNotEmpty(idAziendaNuova))
  {
    aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(
        idAziendaNuova);
  }
  
  
  String idTipoRichiesta = request.getParameter("idTipoRichiesta");
  Integer idTipoRichiestaIt = null;
  if(Validator.isEmpty(idTipoRichiesta))
  {
    idTipoRichiestaIt = new Integer(aziendaNuovaVO.getIdTipoRichiesta().intValue());
  }
  else
  {
    idTipoRichiestaIt = new Integer(idTipoRichiesta);
  }
  Vector<MotivoRichiestaVO> vMotivoRichiesta = gaaFacadeClient.getListMotivoRichiesta(idTipoRichiestaIt.intValue());
  request.setAttribute("vMotivoRichiesta", vMotivoRichiesta);
  
  
  //non esiste sulal tabella temporanea devo cercarlo coi servizi
  //spolo la prima volta che entro
  if(Validator.isEmpty(aziendaNuovaVO) && (request.getParameter("regimeInserimentoPrivati") == null))
  {
    String parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
    String parametroTRIB = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_TRIB"));    
    
    Azienda aziendaAAEP=null;
    boolean aziendaAAEPTrovata=false;
    boolean aziendaAAEPCessata=false;
    boolean aziendaAAEPAltreFonti=false;
    try
    {
      aziendaAAEP = anagFacadeClient.cercaPerCodiceFiscale(cuaaInserimento);
      if(aziendaAAEP != null)
        aziendaAAEPTrovata=true;
    }
    catch (SolmrException sex)
    {
      if (((String)AnagErrors.get("ERR_AAEP_GENERICO")).equals(sex.getMessage()))
      {
        request.setAttribute("pageBack",pageBack);
        request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_GENERICO"));
        request.setAttribute("CUAA", cuaaInserimento);
        request.getRequestDispatcher(URL_ERRORE).forward(request, response);
        return;
      }
      if (((String)AnagErrors.get("ERR_AAEP_TO_CONNECT")).equals(sex.getMessage()))
      {
        request.setAttribute("pageBack",pageBack);
        request.setAttribute("msgErrore", (String)AnagErrors.get("ERR_AAEP_TO_CONNECT"));
        request.setAttribute("CUAA", cuaaInserimento);
        request.getRequestDispatcher(URL_ERRORE).forward(request, response);
        return;
      }
    }
    
    
    if (aziendaAAEPTrovata)
    {      
      //Vado a vedere che il dato restituito abbia come fonte infocamere
    /*  if (!SolmrConstants.FONTE_DATO_INFOCAMERE_STR.equalsIgnoreCase(aziendaAAEP.getIdFonteDato()))
        aziendaAAEPAltreFonti=true;*/
      
      //Vado a controllare se l'azienda risulta cessata
      if (!aziendaAAEPAltreFonti)
        if (aziendaAAEP.getDataCessazione().getValue()!=null && !"".equals(aziendaAAEP.getDataCessazione().getValue()))
          aziendaAAEPCessata=true;
    }
    
    
    if (aziendaAAEPTrovata && !aziendaAAEPCessata && !aziendaAAEPAltreFonti)
    {
      request.setAttribute("aziendaAAEPTrovata", "true");
      request.setAttribute("aziendaAAEP", aziendaAAEP);
        
        
      String pecAAEP =  aziendaAAEP.getPostaElettronicaCertificata().getValue();
      if(Validator.isNotEmpty(pecAAEP))
      {
        request.setAttribute("sedePec", pecAAEP);
      }
      
      
      RappresentanteLegale rappresentanteLegaleAAEP=aziendaAAEP.getRappresentanteLegale().getValue();
      if(rappresentanteLegaleAAEP != null) 
      {
    	String codComuneNascita = rappresentanteLegaleAAEP.getCodComuneNascita().getValue(); 
    	String codComuneResidenza = rappresentanteLegaleAAEP.getCodComuneResidenza().getValue();
    	
    	// --- Comune nascita
        if(codComuneNascita!= null && !codComuneNascita.trim().equals("")){
          // Risetto il codComuneNascita secondo la decodifica presente sul nostro db (comune.istat_comune)	
          String descrComuneNasc = rappresentanteLegaleAAEP.getDescrComuneNascita().getValue();
  	      Vector<ComuneVO> comuni = anagFacadeClient.getComuniByDescCom(descrComuneNasc);
  	      if(comuni != null){
  	    	  ComuneVO comune = comuni.get(0);
  	    	  SolmrLogger.debug(this, " -- istat_comune nascita ="+comune.getIstatComune());
  	    	  
  	    	  QName codComuneNascQName = new QName("http://servizio.frontend.ls.com", "codComuneNascita");
  	    	  JAXBElement<String> codComuneNascitaValue = new JAXBElement<String>(codComuneNascQName, String.class, comune.getIstatComune());
  	    	  
  	    	  rappresentanteLegaleAAEP.setCodComuneNascita(codComuneNascitaValue);
  	      }                              
          ComuneVO comuneNascita = anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
          request.setAttribute("comuneNascita", comuneNascita);
        }
    	// --- Comune residenza
        if(codComuneResidenza != null && !codComuneResidenza.trim().equals("")){
        	 String descrComuneRes = rappresentanteLegaleAAEP.getDescrComuneResidenza().getValue();
   	      Vector<ComuneVO> comuni = anagFacadeClient.getComuniByDescCom(descrComuneRes);
   	      if(comuni != null){
   	    	  ComuneVO comune = comuni.get(0);
   	    	  SolmrLogger.debug(this, " -- istat_comune residenza ="+comune.getIstatComune());
   	    	  
   	    	  QName codComuneResQName = new QName("http://servizio.frontend.ls.com", "codComuneResidenza");
   	    	  JAXBElement<String> codComuneResidenzaValue = new JAXBElement<String>(codComuneResQName, String.class, comune.getIstatComune());
   	    	  
   	    	  rappresentanteLegaleAAEP.setCodComuneResidenza(codComuneResidenzaValue);
   	      }	
        	
          ComuneVO comuneRes = anagFacadeClient.getComuneByISTAT(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
          request.setAttribute("comuneRes", comuneRes);
        }
      }
      
      
                  
    }
    else
    {
      //Se non ho trovato l'azienda su AAEP la ricerco su anagrafe tributaria
      if (parametroTRIB.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        try 
        {
          SianAnagTributariaVO anagTrib = anagFacadeClient.ricercaAnagrafica(
              cuaaInserimento, ProfileUtils.getSianUtente(ruoloUtenza));
          request.setAttribute("anagTrib", anagTrib);
          if(anagTrib != null)
          {
            try
            {
              Vector<ComuneVO> comuniTribNas = anagFacadeClient.getComuniByDescCom(anagTrib.getComuneNascita());
              request.setAttribute("comuniTribNas", comuniTribNas);
            }
            catch(SolmrException se) 
            {}
            try
            {
              Vector<ComuneVO> comuniTribRes = anagFacadeClient.getComuniByDescCom(anagTrib.getComuneResidenza());            
              request.setAttribute("comuniTribRes", comuniTribRes);
            }
            catch(SolmrException se) 
            {}
          }
          
          
        }
        catch(SolmrException se) 
        {
          //Dato che non sono in grado di distinguere il caso in cui il record non è presente dall'errore
          //vero e proprio non segnalo niente
        }
        
        
      }
      
    }
  
  
  }
  else if(Validator.isNotEmpty(aziendaNuovaVO))
  {
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    if(Validator.isEmpty(idAziendaNuova))
    {
      idAziendaNuova = aziendaNuovaVO.getIdAziendaNuova();
    }
    session.setAttribute("idAziendaNuova", idAziendaNuova);
  } 
  
  
  
    
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoPrivati") != null)) 
  {
    // Prelevo i parametri
    String idMotivoRichiesta = request.getParameter("idMotivoRichiesta");
    String cuaa = request.getParameter("cuaa");
    String codiceFiscaleRL = request.getParameter("codiceFiscaleRL");
    String partitaIva = request.getParameter("partitaIva");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String nascitaData = request.getParameter("nascitaData");
    String nascitaProv = request.getParameter("nascitaProv");
    String descNascitaComune = request.getParameter("descNascitaComune");
    String nascitaComune = request.getParameter("nascitaComune");
    String nascitaStatoEstero = request.getParameter("nascitaStatoEstero");
    String istatStatoEsteroNascita = request.getParameter("istatStatoEsteroNascita");
    String cittaNascita = request.getParameter("cittaNascita");
    String resIndirizzo = request.getParameter("resIndirizzo");
    String resProvincia = request.getParameter("resProvincia");
    String descResComune = request.getParameter("descResComune");
    String resComune = request.getParameter("resComune");
    String resCAP = request.getParameter("resCAP");
    String resTelefono = request.getParameter("resTelefono");
    String resFax = request.getParameter("resFax");
    String resMail = request.getParameter("resMail");
    String sedePec = request.getParameter("sedePec");
    String note = request.getParameter("note");
    String subentro = request.getParameter("subentro");
    String cuaaSubentro = request.getParameter("cuaaSubentro");
    String denomSubentro = request.getParameter("denomSubentro");
    String idAziendaSubentro = request.getParameter("idAziendaSubentro");

    // Controllo che l'utente abbia inserito il codice fiscale per effettuare la ricerca
    if(Validator.isEmpty(cuaa)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO"));
      errors.add("cuaa", error);
    }
    // Se lo ha inserito controllo che sia valido
    else if((cuaa.length() == 16) && !Validator.controlloCf(cuaa)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE"));
      errors.add("cuaa", error);
    }
    else
    {
      cuaa = cuaa.toUpperCase();
    }
    
    
    //rappresentante legale
    if(Validator.isEmpty(codiceFiscaleRL)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO"));
      errors.add("codiceFiscaleRL", error);
    }
    // Se lo ha inserito controllo che sia valido
    else if(!Validator.controlloCf(codiceFiscaleRL)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE"));
      errors.add("codiceFiscaleRL", error);
    }
    else
    {
      codiceFiscaleRL = codiceFiscaleRL.toUpperCase();
    }
    
    if(Validator.isNotEmpty(partitaIva)
      && !Validator.controlloPIVA(partitaIva))
    {
      errors.add("partitaIva", new ValidationError(
         AnagErrors.ERR_PARTITA_IVA_ERRATA));
    }
    
    
    if (Validator.isEmpty(cognome))
      errors.add("cognome", new ValidationError(AnagErrors.ERR_COGNOME_TITOLARE_OBBLIGATORIO));
    else
      cognome = cognome.toUpperCase();
    // Il nome è obbligatorio
    if (Validator.isEmpty(nome))
      errors.add("nome", new ValidationError(AnagErrors.ERR_NOME_TITOLARE_OBBLIGATORIO));
    else
      nome = nome.toUpperCase();
    // Il sesso è obbligatorio
    if (Validator.isEmpty(sesso))
      errors.add("sesso", new ValidationError(AnagErrors.ERR_SESSO_TITOLARE_OBBLIGATORIO));
    // Il campo data di nascita è obbligatorio
    Date nascitaDataDt = null;
    if (Validator.isEmpty(nascitaData))
    {
      errors.add("nascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_OBBLIGATORIA));
    }
    else 
    {
      if(nascitaData.length() != 10) 
      {
        errors.add("nascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(nascitaData)) 
      {
        errors.add("nascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      else 
      {
        try 
        {
          nascitaDataDt = DateUtils.parseDate(nascitaData);
        }
        catch (Exception ex) 
        {
          errors.add("nascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
        }
      }
    }
    
    if(Validator.isNotEmpty(istatStatoEsteroNascita)
      && Validator.isNotEmpty(nascitaComune))
    {
      errors.add("nascitaProv", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("descNascitaComune", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("descNascitaStatoEstero", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("cittaNascita", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
    }
    if(Validator.isEmpty(descNascitaComune) && Validator.isEmpty(nascitaProv)
      && Validator.isEmpty(nascitaStatoEstero)) 
    {
      errors.add("nascitaProv", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
      errors.add("descNascitaComune", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
      errors.add("descNascitaStatoEstero", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
      errors.add("cittaNascita", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
    }
    else if(Validator.isNotEmpty(descNascitaComune) && Validator.isNotEmpty(nascitaProv)
      && Validator.isNotEmpty(nascitaStatoEstero)) 
    {
      errors.add("nascitaProv", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("descNascitaComune", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("descNascitaStatoEstero", new ValidationError("E'' possibile inserire solo il comune o lo stato estero di nascita."));
      errors.add("cittaNascita", new ValidationError("E'' necessario inserire il comune/stato di nascita se estero"));
    }
    else if(Validator.isEmpty(nascitaComune)
      && Validator.isNotEmpty(descNascitaComune) 
      && Validator.isNotEmpty(nascitaProv))
    {
      if (Validator.isNotEmpty(nascitaProv) && Validator.isNotEmpty(descNascitaComune))
      { 
        Vector<ComuneVO> elencoComuni = anagFacadeClient.getComuniNonEstintiLikeProvAndCom(nascitaProv, descNascitaComune, null);
        if(elencoComuni == null)
        {
          errors.add("nascitaProv", new ValidationError("Non e'' stato trovato nessun comune coi parametri di ricerca impostati."));
          errors.add("descNascitaComune", new ValidationError("Non e'' stato trovato nessun comune coi parametri di ricerca impostati."));
        }        
        else if((elencoComuni != null) 
          && (elencoComuni.size() > 1))
        {
          errors.add("nascitaProv", new ValidationError("Esistono più comuni/stati coi parametri inseriti."));
          errors.add("descNascitaComune", new ValidationError("Esistono più comuni/stati coi parametri inseriti."));
        }
        else
        {
          nascitaComune = elencoComuni.get(0).getIstatComune();
        }
      }
    }
    else if(Validator.isEmpty(istatStatoEsteroNascita)
      && Validator.isNotEmpty(nascitaStatoEstero))
    {
      Vector<ComuneVO> elencoComuni = anagFacadeClient.ricercaStatoEstero(nascitaStatoEstero, "N", null);
      if(elencoComuni == null)
      {
        errors.add("descNascitaStatoEstero", new ValidationError("Non e'' stato trovato nessuno stato coi parametri di ricerca impostati."));
        errors.add("cittaNascita", new ValidationError("Non e'' stato trovato nessuno stato coi parametri di ricerca impostati."));
      }
      else if((elencoComuni != null) 
        && (elencoComuni.size() > 1))
      {
        errors.add("descNascitaStatoEstero", new ValidationError("Esistono più comuni/stati coi parametri inseriti."));          
      }
      else
      {
        nascitaComune = elencoComuni.get(0).getIstatComune();
      }
    }
        
    
    if(Validator.isNotEmpty(cittaNascita))
    {
      cittaNascita = cittaNascita.toUpperCase();
    }
    
    
    
    
    
    
    // L'indirizzo è obbligatorio
    if (Validator.isEmpty(resIndirizzo))
      errors.add("resIndirizzo", new ValidationError(AnagErrors.ERR_INDIRIZZO_RESIDENZA_TITOLARE_OBBLIGATORIO));
    else
      resIndirizzo = resIndirizzo.toUpperCase();

    if (Validator.isEmpty(resCAP)) 
    {
      errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_OBBLIGATORIO));
    }
    else 
    {
      if(!Validator.isCapOk(resCAP)) 
      {
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_ERRATO));
      }
    }
      
    if (Validator.isEmpty(descResComune))
      errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_OBBLIGATORIO));
    if (Validator.isEmpty(resProvincia))
      errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_OBBLIGATORIO));
    
    
    
    if(Validator.isEmpty(resComune))
    {
      Vector<ComuneVO> elencoComuni = null;
      if (Validator.isNotEmpty(resProvincia) && Validator.isNotEmpty(descResComune))
      {      
        elencoComuni = anagFacadeClient.getComuniNonEstintiLikeProvAndCom(resProvincia, descResComune, null);
      }
      
      
      if(elencoComuni == null)
      {
        errors.add("resProvincia", new ValidationError("Non e'' stato trovato nessun comune coi parametri di ricerca impostati."));
        errors.add("descResComune", new ValidationError("Non e'' stato trovato nessun comune coi parametri di ricerca impostati."));
      }
      else if((elencoComuni != null) 
        && (elencoComuni.size() > 1))
      {
        errors.add("resProvincia", new ValidationError("Esistono più comuni coi parametri inseriti."));
        errors.add("descResComune", new ValidationError("Esistono più comuni coi parametri inseriti."));          
      }
      else
      {
        resComune = elencoComuni.get(0).getIstatComune();
      }
    } 
    
    
    
    
    if (Validator.isNotEmpty(resMail) && !Validator.isValidEmail(resMail)) 
    {
      errors.add("resMail", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
    }
    
    // controllo la correttezza del campo pec
    if (Validator.isNotEmpty(sedePec))
    {
      if (!Validator.isValidEmail(sedePec))
      {
        errors.add("sedePec", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
      }
    }
    
    if(Validator.isEmpty(resMail) && Validator.isEmpty(sedePec))
    {
      errors.add("resMail", new ValidationError(
          "Inserire almeno un campo tra mail e pec"));
      errors.add("sedePec", new ValidationError(
          "Inserire almeno un campo tra mail e pec"));
        
    }
    
    if (Validator.isEmpty(resTelefono)) 
    {
      errors.add("resTelefono", new ValidationError("Campo obbligatorio"));
    }

    String codiceFiscaleComuneNascita = "";
    try 
    {
      if(Validator.isNotEmpty(descNascitaComune)
        && Validator.isNotEmpty(nascitaProv)) 
      {
        codiceFiscaleComuneNascita = anagFacadeClient.ricercaCodiceFiscaleComune(descNascitaComune,nascitaProv);
      }
      else 
      {
        codiceFiscaleComuneNascita = anagFacadeClient.ricercaCodiceFiscaleComune(nascitaStatoEstero,"");
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descNascitaComune", error);
    }    
    
    
    String flagNoteObbligatorie = "N";
    for(int i=0;i<vMotivoRichiesta.size();i++)
    {
      if(vMotivoRichiesta.get(i).getIdMotivoRichiesta().compareTo(new Integer(idMotivoRichiesta)) == 0)
      {
        flagNoteObbligatorie = vMotivoRichiesta.get(i).getNoteObbligatorie();
        break;
      }  
    }
    
    
    
    if("S".equalsIgnoreCase(flagNoteObbligatorie))
    {
      if(Validator.isEmpty(note))
      {
        errors.add("note", new ValidationError("E'' obbligatorio valorizzare il campo note quando il motivo di richiesta selezionato e'' ''altro'' "));
      }
    }
    if (Validator.isNotEmpty(note))
    {
      if (note.length() > 1000)
      {
        errors.add("note", new ValidationError("Le note non posso essere lunghe piu'' di 1000 caratteri."));
      }
    }
    
    
    if("S".equalsIgnoreCase(subentro))
    {
      if(Validator.isEmpty(cuaaSubentro) && Validator.isEmpty(denomSubentro))
      {
        errors.add("cuaaSubentro", new ValidationError("Campo obbligatorio"));
        errors.add("denomSubentro", new ValidationError("Campo obbligatorio"));
      }
      else
      {
        int countErrSubentro = 0;
        if(Validator.isNotEmpty(cuaaSubentro))
        {
        
          if (cuaaSubentro.length() != 11 && cuaaSubentro.length() != 16)
          {
            errors.add("cuaaSubentro", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRORE")));
            countErrSubentro++;
          }
          else if (cuaaSubentro.length() == 11 && !Validator.controlloPIVA(cuaaSubentro))
          {
            errors.add("cuaaSubentro", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRORE")));
            countErrSubentro++;
          }
          else if (cuaaSubentro.length() == 16 && !Validator.controlloCf(cuaaSubentro))
          {
            errors.add("cuaaSubentro", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRORE")));
            countErrSubentro++;
          }
        }
        
        if(countErrSubentro == 0)
        {
          AnagAziendaVO aziendaVOSub = new AnagAziendaVO();
          aziendaVOSub.setCUAA(cuaaSubentro);
          aziendaVOSub.setDenominazione(denomSubentro);
          Vector<Long> vectIdAnagAziendaInitial = null;
          try
          {
            //false prendo le azzienda anche cessate
            //false prendo le aziende non provvisorie 
            vectIdAnagAziendaInitial = anagFacadeClient
              .getListIdAziendeFlagProvvisorio(aziendaVOSub, null,false,false);
          }
          catch (SolmrException se) 
          {}
          
          if((vectIdAnagAziendaInitial == null)
            || ((vectIdAnagAziendaInitial != null) && (vectIdAnagAziendaInitial.size() == 0)))
          {
            errors.add("cuaaSubentro", new ValidationError("Attenzione: l''azienda/persona fisica non e'' censita nei nostri archivi."));
            errors.add("denomSubentro", new ValidationError("Attenzione: l''azienda/persona fisica non e'' censita nei nostri archivi."));
          }
          else if((vectIdAnagAziendaInitial != null) 
            && (vectIdAnagAziendaInitial.size() > 1) 
            && (Validator.isNotEmpty(idAziendaSubentro) && !vectIdAnagAziendaInitial.contains(new Long(idAziendaSubentro))))
          {
            errors.add("cuaaSubentro", new ValidationError("Coi criteri impostati sono state trovate piu'' aziende."));
            errors.add("denomSubentro", new ValidationError("Coi criteri impostati sono state trovate piu'' aziende."));          
          }
          else
          {
            idAziendaSubentro = vectIdAnagAziendaInitial.get(0).toString();
          }
        }     
	      
      }    
    }
    
    boolean controlloCuaa = true;
    //Ha macro caso uso iride opportuno...
    if(request.getAttribute("inserisciChiunque") != null)
    {
      controlloCuaa = false;
    }
    
    //aggiungo controllo cuua utente iride
    /*String parametroUtenteRichiestaAz = null;
    try 
    {
      parametroUtenteRichiestaAz = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_UTENTE_RICHIESTA_AZ);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - newInserimentoAnagraficaPrivatiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_UTENTE_RICHIESTA_AZ+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }*/
    
    if(Validator.isNotEmpty(codiceFiscaleRL) && controlloCuaa)
    {
      if(!codiceFiscaleRL.equalsIgnoreCase(ruoloUtenza.getCodiceFiscale()))
      {
         ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CUAA_NO_IRIDE);
         errors.add("codiceFiscaleRL", error);      
      }    
    }   
    

    

    if(errors != null && errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(newInserimentoAnagraficaPrivatiUrl).forward(request, response);
      return;
    }

    
    try 
    {
      Validator.verificaCf(nome, cognome, sesso, nascitaDataDt, 
        codiceFiscaleComuneNascita, codiceFiscaleRL);
    }
    catch(CodiceFiscaleException ce) 
    {
      if(ce.getNome()) {
        errors.add("nome",new ValidationError(""+AnagErrors.get("ERR_NOME_CODICE_FISCALE")));
      }
      if(ce.getCognome()) {
        errors.add("cognome",new ValidationError(""+AnagErrors.get("ERR_COGNOME_CODICE_FISCALE")));
      }
      if(ce.getAnnoNascita()) {
        errors.add("nascitaData",new ValidationError(""+AnagErrors.get("ERR_ANNO_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getMeseNascita()) {
        errors.add("nascitaData",new ValidationError(""+AnagErrors.get("ERR_MESE_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getGiornoNascita()) {
        errors.add("nascitaData",new ValidationError(""+AnagErrors.get("ERR_GIORNO_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getComuneNascita()) {
        errors.add("descNascitaComune",new ValidationError(""+AnagErrors.get("ERR_COMUNE_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getSesso()) {
        errors.add("sesso",new ValidationError(""+AnagErrors.get("ERR_SESSO_CODICE_FISCALE")));
      }
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(newInserimentoAnagraficaPrivatiUrl).forward(request, response);
      return;
    }
    
    //setto i valore per il db!!!!
    AziendaNuovaVO aziendaNuovaVOMod = new AziendaNuovaVO();
    //questo serve per evntuale modifica se iscrizione diversa da bozza...
    if(Validator.isNotEmpty(aziendaNuovaVO))
    {
	    aziendaNuovaVOMod.setIdStatoRichiesta(aziendaNuovaVO.getIdStatoRichiesta());
	    aziendaNuovaVOMod.setIdIterRichiestaAzienda(aziendaNuovaVO.getIdIterRichiestaAzienda());
	    aziendaNuovaVOMod.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
	  }
    //**************************
    aziendaNuovaVOMod.setIdMotivoRichiesta(new Integer(idMotivoRichiesta));
    aziendaNuovaVOMod.setCuaa(cuaa);
    aziendaNuovaVOMod.setPartitaIva(partitaIva);
    //fisso tipo azienda
    if(Validator.isNotEmpty(partitaIva))
    {
	    aziendaNuovaVOMod.setIdTipologiaAzienda(new Long(1));
	    aziendaNuovaVOMod.setIdFormaGiuridica(new Long(1));
	  }
	  else
	  {
	    aziendaNuovaVOMod.setIdTipologiaAzienda(new Long(2));
      aziendaNuovaVOMod.setIdFormaGiuridica(new Long(52));
	  }
    aziendaNuovaVOMod.setSedeLegUte("N");
    aziendaNuovaVOMod.setMail(resMail);
    aziendaNuovaVOMod.setTelefono(resTelefono);
    aziendaNuovaVOMod.setFax(resFax);
    aziendaNuovaVOMod.setPec(sedePec);
    aziendaNuovaVOMod.setTipoSoggetto("P");
    aziendaNuovaVOMod.setCodiceFiscale(codiceFiscaleRL);
    aziendaNuovaVOMod.setCognome(cognome);
    aziendaNuovaVOMod.setNome(nome);
    aziendaNuovaVOMod.setDenominazione(cognome+" "+nome);
    aziendaNuovaVOMod.setSesso(sesso);
    aziendaNuovaVOMod.setDataNascita(nascitaDataDt);
    aziendaNuovaVOMod.setComuneNascita(nascitaComune);
    aziendaNuovaVOMod.setCittaNascitaEstero(cittaNascita);
    aziendaNuovaVOMod.setResIndirizzo(resIndirizzo);
    aziendaNuovaVOMod.setResComune(resComune);
    aziendaNuovaVOMod.setResCap(resCAP);
    aziendaNuovaVOMod.setSedelegIndirizzo(resIndirizzo);
    aziendaNuovaVOMod.setSedelegComune(resComune);
    aziendaNuovaVOMod.setSedelegCap(resCAP);
    aziendaNuovaVOMod.setTelefonoSoggetto(resTelefono);
    aziendaNuovaVOMod.setFaxSoggetto(resFax);
    aziendaNuovaVOMod.setMailSoggetto(resMail);
    aziendaNuovaVOMod.setNote(note);
    Long idAziendaSubentroLg = null;
    if(Validator.isNotEmpty(idAziendaSubentro))
    {
      idAziendaSubentroLg = new Long(idAziendaSubentro);
    }
    aziendaNuovaVOMod.setIdAziendaSubentro(idAziendaSubentroLg);
    
    //Faccio l'update record già esistente
    if(Validator.isNotEmpty(idAziendaNuova))
    {
      try 
      {
        aziendaNuovaVOMod.setIdAziendaNuova(idAziendaNuova);
        gaaFacadeClient.updateAziendaNuovaIscrizione(aziendaNuovaVOMod, ruoloUtenza.getIdUtente());
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - unitaArboreeAllineaGISCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }    
    }
    //Prima volta che inserisco
    else
    {    
      try 
      {
        //Verifico che nn sia già stata inserita un'azienda con la stessa partita iva
	      if(Validator.isNotEmpty(aziendaNuovaVOMod.getPartitaIva()))
	      {
	        if(gaaFacadeClient.isPartitaIvaPresente(partitaIva, new long[]{SolmrConstants.RICHIESTA_NUOVA_ISCRIZIONE,SolmrConstants.RICHIESTA_NI_AZIENDA_OBSOLETA}))
	        {
	          SolmrLogger.info(this, " - newInserimentoAnagraficaPrivatiCtrl.jsp - FINE PAGINA");
	          String messaggio = AnagErrors.ERRORE_DUPLICATO_PARTITA_IVA;
	          request.setAttribute("messaggioErrore",messaggio);
	          request.setAttribute("history", "history");
	          %>
	            <jsp:forward page="<%= erroreViewUrl %>" />
	          <%
	          return;        
	        }
	      }
      
        aziendaNuovaVOMod.setIdTipoRichiesta(new Long(request.getParameter("idTipoRichiesta")));
      
        Long idAziendaNuovaNew = gaaFacadeClient.insertAziendaNuovaIscrizione(aziendaNuovaVOMod, ruoloUtenza.getIdUtente());
        session.setAttribute("idAziendaNuova", idAziendaNuovaNew);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - unitaArboreeAllineaGISCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    } 
    
    %>
      <jsp:forward page="<%= pageNext %>"/>
    <%
    

  }
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoPrivati") != null)) 
  {    
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }

%>

  <jsp:forward page="<%= newInserimentoAnagraficaPrivatiUrl %>"/>