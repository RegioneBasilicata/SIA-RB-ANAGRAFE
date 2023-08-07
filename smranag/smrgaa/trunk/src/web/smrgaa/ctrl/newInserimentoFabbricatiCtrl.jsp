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
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoFabbricatiCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,vFabbrAziendaNuova,vPartFabbrAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoFabbricatiUrl = "/view/newInserimentoFabbricatiView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoParticelleCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoFabbricati.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String pageNext = "/ctrl/newInserimentoAllevamentiCtrl.jsp";
  
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoFabbricati") != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  ValidationErrors errors = new ValidationErrors();
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_FAB);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  FabbricatoAziendaNuovaVO fabbricatoAziendaNuovaVO = (FabbricatoAziendaNuovaVO)session.getAttribute("fabbricatoAziendaNuovaVO");
  HashMap<String, Vector<ComuneVO>> hProvCom = new HashMap<String, Vector<ComuneVO>>();
  
  String operazione = request.getParameter("operazione");
  Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova = null;
  if(request.getParameter("regimeInserimentoFabbricati") == null)
  {
    //Prima volta che entro carico le ute da db
    try
    {
      vFabbrAziendaNuova = gaaFacadeClient.getFabbrAziendaNuovaIscrizione(idAziendaNuova.longValue());
    }
    catch(Exception ex)
    {
      SolmrLogger.info(this, " - newInserimentoFabbricatiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    session.removeAttribute("fabbricatoAziendaNuovaVO");
  }
  else
  {
    vFabbrAziendaNuova = (Vector<FabbricatoAziendaNuovaVO>)session.getAttribute("vFabbrAziendaNuova");
  }
  
  
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = null;
  try
  {
    vUteAziendaNuova = gaaFacadeClient.getUteAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoFabbricatiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("vUteAziendaNuova", vUteAziendaNuova);
  
  Vector<CodeDescription> elencoTipologiaFabbricato = anagFacadeClient.getTipiTipologiaFabbricato();
  request.setAttribute("elencoTipologiaFabbricato", elencoTipologiaFabbricato);
  
  
  Vector<ProvinciaVO> vProvince =  anagFacadeClient.getProvince();
  request.setAttribute("vProvince", vProvince);
  
  CodeDescription[] elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
  request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  
  String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
  TipoUtilizzoVO[] elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_FABBRICATO, true, orderBy);
  request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
  
  Vector<PartFabbrAziendaNuovaVO> vPartFabbrAziendaNuova = (Vector<PartFabbrAziendaNuovaVO>)session
    .getAttribute("vPartFabbrAziendaNuova");
  
  
  
  // Setto i parametri all'interno del VO
  if(fabbricatoAziendaNuovaVO == null) 
  {
    fabbricatoAziendaNuovaVO = new FabbricatoAziendaNuovaVO();
  }
  
  
  String uteAziendaNuova = request.getParameter("idUteAziendaNuova");
  Long idUteAziendaNuova = null;
  if(Validator.isNotEmpty(uteAziendaNuova)) 
  {
    idUteAziendaNuova = Long.decode(uteAziendaNuova);
  }
  fabbricatoAziendaNuovaVO.setIdUteAziendaNuova(idUteAziendaNuova);
  String tipoFabbricato = request.getParameter("idTipologiaFabbricato");
  Long idTipologiaFabbricato = null;
  if(Validator.isNotEmpty(tipoFabbricato)) 
  {
    idTipologiaFabbricato = Long.decode(tipoFabbricato);
  }
  
  
  
  
  
  
  String[] istatProvincia = request.getParameterValues("istatProvincia");
  String[] istatComune = request.getParameterValues("istatComune");
  String[] sezione = request.getParameterValues("sezione");
  String[] foglio = request.getParameterValues("foglio");
  String[] particella = request.getParameterValues("particella");
  String[] subalterno = request.getParameterValues("subalterno");
  String[] superficiePart = request.getParameterValues("superficiePart");
  String[] idTitoloPossesso = request.getParameterValues("idTitoloPossesso");
  
  if((vPartFabbrAziendaNuova != null) && Validator.isNotEmpty(istatComune))
  {
    for(int i=0;i<vPartFabbrAziendaNuova.size();i++)
    {
      PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = vPartFabbrAziendaNuova.get(i);      
      partFabbrAziendaNuovaVO.setIstatProvincia(istatProvincia[i]);
      if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatProvincia()) 
        && (hProvCom.get(partFabbrAziendaNuovaVO.getIstatProvincia()) == null))
      {
        String istatProvinciaTmp = partFabbrAziendaNuovaVO.getIstatProvincia();
        hProvCom.put(istatProvinciaTmp, anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvinciaTmp));        
      }
      partFabbrAziendaNuovaVO.setIstatComune(istatComune[i]);
      if(Validator.isNotEmpty(sezione[i]))
      {
        partFabbrAziendaNuovaVO.setSezione(sezione[i].toUpperCase());
      }
      
      partFabbrAziendaNuovaVO.setStrFoglio(foglio[i]);
      partFabbrAziendaNuovaVO.setStrParticella(particella[i]);
      partFabbrAziendaNuovaVO.setSubalterno(subalterno[i]);
      partFabbrAziendaNuovaVO.setStrSuperficie(superficiePart[i]);
      
      if(Validator.isNotEmpty(idTitoloPossesso[i]))
      {
        partFabbrAziendaNuovaVO.setIdTitoloPossesso(new Integer(idTitoloPossesso[i]));
      }
      else
      {
        partFabbrAziendaNuovaVO.setIdTitoloPossesso(null);
      }
      
    }
    
    request.setAttribute("hProvCom", hProvCom);
  }
  
  
  
  
  
  
  
  
  
  
  String unitaMisura = request.getParameter("unitaMisura");
  String larghezzaFabbricato = request.getParameter("larghezzaFabbricato");
  String lunghezzaFabbricato = request.getParameter("lunghezzaFabbricato");
  String altezzaFabbricato = request.getParameter("altezzaFabbricato");
  String superficieFabbricato = request.getParameter("superficieFabbricato");
  String dimensioneFabbricato = request.getParameter("dimensioneFabbricato");
  String annoCostruzioneFabbricato = request.getParameter("annoCostruzioneFabbricato");
  String superficieCopertaFabbricato = request.getParameter("superficieCopertaFabbricato");
  String superficieScopertaFabbricato = request.getParameter("superficieScopertaFabbricato");
  fabbricatoAziendaNuovaVO.setIdTipologiaFabbricato(idTipologiaFabbricato);    
  fabbricatoAziendaNuovaVO.setStrLarghezza(larghezzaFabbricato);
  fabbricatoAziendaNuovaVO.setStrLunghezza(lunghezzaFabbricato);
  fabbricatoAziendaNuovaVO.setStrAltezza(altezzaFabbricato);
  fabbricatoAziendaNuovaVO.setUnitaMisura(unitaMisura);
  fabbricatoAziendaNuovaVO.setStrSuperficie(superficieFabbricato);
  fabbricatoAziendaNuovaVO.setStrDimensione(dimensioneFabbricato);
  fabbricatoAziendaNuovaVO.setStrAnnoCostruzione(annoCostruzioneFabbricato);
  fabbricatoAziendaNuovaVO.setStrSuperficieCoperta(superficieCopertaFabbricato);
  fabbricatoAziendaNuovaVO.setStrSuperficieScoperta(superficieScopertaFabbricato);
  
  
  
  
  // L'utente ha cliccato il pulsante calcola
  if (Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("calcolaVolume"))
  {
    //Ricavo il tipo di struttura
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = fabbricatoAziendaNuovaVO.getTipoTipologiaFabbricatoVO();
    String tipoFormula = SolmrConstants.STRUTTURA_STANDARD;
    if((tipoTipologiaFabbricatoVO !=null) && tipoTipologiaFabbricatoVO.getTipoFormula() !=null)
    {
      tipoFormula = tipoTipologiaFabbricatoVO.getTipoFormula();
    }
    
    double superficie = 0;

    // SUPERFICIE

    // Se l'utente ha valorizzato la larghezza e la lunghezza del fabbricato calcolo la
    // superficie altrimenti lo avviso che non è possibile ricavarla    
    String messaggioErrore = "";
    if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
    {
      if(Validator.isNotEmpty(larghezzaFabbricato))
      {
        if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(), 999.99) !=null) 
        {
          double larghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLarghezza());
          superficie = 3.14 * (larghezza/2) * (larghezza/2);
          superficieFabbricato = String.valueOf(superficie);
          fabbricatoAziendaNuovaVO.setStrSuperficie(StringUtils.parseDoubleFieldOneDecimal((superficieFabbricato)));
          fabbricatoAziendaNuovaVO.setStrLarghezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(larghezza)));
        }
        else 
        {
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO +", ";
          errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);        
        }
      }
      else 
      {
        messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_OBBLIGATORIO +", ";
        errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", AnagErrors.ERR_DIAMETRO_FABBRICATO_OBBLIGATORIO);
      }
    }
    else
    {
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLarghezza()) 
        && Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza())) 
      {
        if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(), 999.9) !=null 
          && Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(), 999.9) != null) 
        {
          double lunghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLunghezza());
          double larghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLarghezza());
          superficie = lunghezza * larghezza;
          superficieFabbricato = String.valueOf(superficie);
          fabbricatoAziendaNuovaVO.setStrSuperficie(StringUtils.parseDoubleFieldOneDecimal(superficieFabbricato));
          fabbricatoAziendaNuovaVO.setStrLunghezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(lunghezza)));
          fabbricatoAziendaNuovaVO.setStrLarghezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(larghezza)));
        }
        else 
        {
          if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(), 999.9) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA") +", ";
            errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
          }
          if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(), 999.9) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors,"lunghezza", (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
          }
        }
      }
      else 
      {
        if(!Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLarghezza())) 
        {
          messaggioErrore = (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_OBBLIGATORIA") +", ";
          errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_OBBLIGATORIA"));
        }
        if(!Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza())) 
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_OBBLIGATORIA");
          errors = ErrorUtils.setValidErrNoNull(errors,"lunghezza", (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_OBBLIGATORIA"));
        }
      }
    }

    String dimensione = "";
    // DIMENSIONE
    // Controllo che l'utente abbia selezionato un tipo fabbricato e che quest'ultimo preveda
    // come unita di misura i metri cubi.
    if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_STANDARD))
    {
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato())) 
      {
        if(fabbricatoAziendaNuovaVO.getUnitaMisura() != null 
          && fabbricatoAziendaNuovaVO.getUnitaMisura().equalsIgnoreCase((String)SolmrConstants.get("UNITA_MISURA_METRI_CUBI"))) 
        {
          if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLarghezza()) 
            && Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza()) 
            && Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrAltezza())) 
          {
            if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(),999.9) != null 
              && Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(),999.9) != null 
              && Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(),999.9) != null) 
            {
              double larghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLarghezza());
              double lunghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLunghezza());
              double altezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrAltezza());
              dimensione = String.valueOf(larghezza * lunghezza * altezza);
              fabbricatoAziendaNuovaVO.setStrDimensione(StringUtils.parseDoubleFieldOneDecimal(dimensione));
              fabbricatoAziendaNuovaVO.setStrAltezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
            }
          }
        }
      }
  
      // Controllo che l'utente abbia selezionato un tipo fabbricato e che quest'ultimo preveda
      // come unita di misura i metri quadri.
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato())) 
      {
        if(fabbricatoAziendaNuovaVO.getUnitaMisura() != null 
          && fabbricatoAziendaNuovaVO.getUnitaMisura().equalsIgnoreCase(SolmrConstants.UNITA_MISURA_METRI_QUADRI)) 
        {
          if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLarghezza()) 
            && Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza())) 
          {
            if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(),999.9) != null 
              && Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(),999.9) != null) 
            {
              double larghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLarghezza());
              double lunghezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLunghezza());
              dimensione = String.valueOf(larghezza * lunghezza);
              fabbricatoAziendaNuovaVO.setStrDimensione(StringUtils.parseDoubleFieldOneDecimal(dimensione));
            }
          }
        }
      }
    }
    //Per i conti considero l'altezza che però essendo presenti a video solo due campi
    //corrisponde al parametro lunghezza
    else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
    {
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza())) 
      {
        if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(), 999.9) !=null) 
        {
          double altezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrLunghezza());
          dimensione = String.valueOf(superficie * (altezza - 0.1));
          fabbricatoAziendaNuovaVO.setStrDimensione(StringUtils.parseDoubleFieldOneDecimal(dimensione));
          fabbricatoAziendaNuovaVO.setStrLunghezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
        }
        else
        {
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA +", ";
          errors = ErrorUtils.setValidErrNoNull(errors,"lunghezza", AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
      }
      else
      {
        messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA +", ";
        errors = ErrorUtils.setValidErrNoNull(errors,"lunghezza", AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA);
      }
    }
    else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_RETTANGOLARE))
    {
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrAltezza())) 
      {
        if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9) !=null) 
        {
          double altezza = StringUtils.convertNumericField(fabbricatoAziendaNuovaVO.getStrAltezza());
          dimensione = String.valueOf(superficie * (altezza - 0.1));
          fabbricatoAziendaNuovaVO.setStrDimensione(StringUtils.parseDoubleFieldOneDecimal(dimensione));
          fabbricatoAziendaNuovaVO.setStrAltezza(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
        }
        else
        {
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA +", ";
          errors = ErrorUtils.setValidErrNoNull(errors,"altezza", AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
      }
      else
      {
        messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA +", ";
        errors = ErrorUtils.setValidErrNoNull(errors,"altezza", AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA);
      }
    }
    else //Inseribile dall'utente
    {}
    
    
    if((tipoTipologiaFabbricatoVO !=null) && 
      (Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getFlagStoccaggio()) 
      &&  tipoTipologiaFabbricatoVO.getFlagStoccaggio().equalsIgnoreCase("S")))
    {
      fabbricatoAziendaNuovaVO.setStrSuperficieScoperta(fabbricatoAziendaNuovaVO.getStrSuperficie());
    }





    // Metto il VO in sessione
    session.setAttribute("fabbricatoAziendaNuovaVO",fabbricatoAziendaNuovaVO);

    // Se ci sono errori li visualizzo
    if((errors !=null) && (errors.size() > 0)) 
    {
      errors.add("calcola", new ValidationError(messaggioErrore));
      request.setAttribute("errors", errors);
    }

    // Altrimenti vado alla pagina di inserimento con la superficie calcolata
    %>
      <jsp:forward page="<%= newInserimentoFabbricatiUrl %>"/>
    <%
    return;

  } 
  
	
	// Se l'utente ha modificato la tipologia del fabbricato cambio l'unita di misura della
  // dimensione
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("cambioUnitaMisura")) 
  {
    
    larghezzaFabbricato = null;
    lunghezzaFabbricato = null;
    altezzaFabbricato = null; 
    superficieFabbricato = null; 
    dimensioneFabbricato = null; 
    annoCostruzioneFabbricato = null; 
    superficieCopertaFabbricato = null; 
    superficieScopertaFabbricato = null; 
    

    fabbricatoAziendaNuovaVO.setIdTipologiaFabbricato(idTipologiaFabbricato);
    fabbricatoAziendaNuovaVO.setStrLarghezza(larghezzaFabbricato);
    fabbricatoAziendaNuovaVO.setStrLunghezza(lunghezzaFabbricato);
    fabbricatoAziendaNuovaVO.setStrAltezza(altezzaFabbricato);
    fabbricatoAziendaNuovaVO.setStrSuperficie(superficieFabbricato);
    fabbricatoAziendaNuovaVO.setStrDimensione(dimensioneFabbricato);
    fabbricatoAziendaNuovaVO.setStrAnnoCostruzione(annoCostruzioneFabbricato);
    fabbricatoAziendaNuovaVO.setStrSuperficieCoperta(superficieCopertaFabbricato);
    fabbricatoAziendaNuovaVO.setStrSuperficieScoperta(superficieScopertaFabbricato);

    // Se l'utente ha scelto una tipologia di fabbricato recupero l'unita di misura associata
    //String unitaMisura = null;

    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = null;
    if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato())) 
    {
      try
      {
        //unitaMisura = anagFacadeClient.getUnitaMisuraByTipoFabbricato(fabbricatoVO.getIdTipologiaFabbricato());
        tipoTipologiaFabbricatoVO = gaaFacadeClient.getInfoTipologiaFabbricato(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato());
      }
      catch(SolmrException se) 
      {
        request.setAttribute("history","true");
        request.setAttribute("messaggioErrore", se.getMessage() );
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    fabbricatoAziendaNuovaVO.setUnitaMisura(tipoTipologiaFabbricatoVO.getUnitaMisura());
    fabbricatoAziendaNuovaVO.setTipoTipologiaFabbricatoVO(tipoTipologiaFabbricatoVO);
    session.setAttribute("fabbricatoAziendaNuovaVO",fabbricatoAziendaNuovaVO);

    // Ritorno alla pagina di inserimento
    %>
       <jsp:forward page="<%= newInserimentoFabbricatiUrl %>"/>
    <%
    return;
  }
	
	
	if(Validator.isNotEmpty(operazione) && "cambio".equalsIgnoreCase(operazione))
  {
    //nn fa nulla....
  }
  else if(Validator.isNotEmpty(operazione) && "eliminaPart".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElimPart");
    vPartFabbrAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisciPart".equalsIgnoreCase(operazione))
  {
    if(vPartFabbrAziendaNuova == null)
    {
      vPartFabbrAziendaNuova = new Vector<PartFabbrAziendaNuovaVO>();
    }
    PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = new PartFabbrAziendaNuovaVO();    
    
    vPartFabbrAziendaNuova.add(partFabbrAziendaNuovaVO);
    
  }
  
  
  session.setAttribute("vPartFabbrAziendaNuova", vPartFabbrAziendaNuova);
	
	
  
  if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    vFabbrAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisciFabb".equalsIgnoreCase(operazione))
  {        
    if (Validator.isEmpty(fabbricatoAziendaNuovaVO.getIdUteAziendaNuova()))
    {
      errors.add("idUteAziendaNuova", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      for(int j=0;j<vUteAziendaNuova.size();j++)
      {
        if(fabbricatoAziendaNuovaVO.getIdUteAziendaNuova().compareTo(vUteAziendaNuova.get(j).getIdUteAziendaNuova()) == 0)
        {
          fabbricatoAziendaNuovaVO.setDenominazUte(vUteAziendaNuova.get(j).getDenominazione());
          break;
        }
      
      }
    }
    
    
    String tipoFormula = "";
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = null;  
    if (Validator.isEmpty(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato()))
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "tipologiaFabbricato",(String)AnagErrors.get("ERR_TIPOLOGIA_FABBRICATO_OBBLIGATORIA"));
    }
    else
    {
      tipoTipologiaFabbricatoVO = gaaFacadeClient.getInfoTipologiaFabbricato(fabbricatoAziendaNuovaVO.getIdTipologiaFabbricato());
      fabbricatoAziendaNuovaVO.setDescFabbricato(tipoTipologiaFabbricatoVO.getDescrizione());
      
      tipoFormula = fabbricatoAziendaNuovaVO.getTipoTipologiaFabbricatoVO().getTipoFormula();  
    }
      
      
    // Se l'anno di costruzione è valorizzato controllo che sia un numero valido
    if(Validator.isEmpty(annoCostruzioneFabbricato)) 
    {
      errors.add("annoCostruzioneFabbricato", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    else
    {
      if(!Validator.isNumericInteger(fabbricatoAziendaNuovaVO.getStrAnnoCostruzione())) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_ERRATO"));
      }
      else 
      {
        int annoCorrente = DateUtils.getCurrentYear().intValue();
        int annoCostruzione = Integer.parseInt(fabbricatoAziendaNuovaVO.getStrAnnoCostruzione());
        // L'anno di costruzione fabbricato non può essere posteriore all'anno di sistema
        if(annoCostruzione > annoCorrente) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_NO_POST_ODIERNO"));
        }
        else
        {
          fabbricatoAziendaNuovaVO.setAnnoCostruzione(new Integer(fabbricatoAziendaNuovaVO.getStrAnnoCostruzione()));
        }
      }
    }      
    
    
       
      
    String messaggioErrore = "";
    if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLarghezza())) 
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(), 999.9) == null) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //Larghezza usata come diametro
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLarghezza(), 999.9)) <= 0) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { // larghezza usata come diametro
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else 
      {
        fabbricatoAziendaNuovaVO.setLarghezza(new BigDecimal(fabbricatoAziendaNuovaVO.getStrLarghezza().replace(',','.')));
      }
    }
    // Se la lunghezza è valorizzata controllo che sia un valore numerico valido
    if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrLunghezza())) 
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(), 999.9) == null) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        {
          //lunghezza usata come altezza
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrLunghezza(), 999.9)) <= 0) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //lunghezza usata come altezza
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else 
      {
        fabbricatoAziendaNuovaVO.setLunghezza(new BigDecimal(fabbricatoAziendaNuovaVO.getStrLunghezza().replace(',','.')));
      }
    }
    // Se l'altezza è presente a video è obbligatoria!!!!
    if(Validator.isNotEmpty(tipoTipologiaFabbricatoVO) 
        && Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getVLabel()) && (tipoTipologiaFabbricatoVO.getVLabel().size() == 3))
    {
      if(!Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrAltezza())) 
      {
        messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA;
        errors = ErrorUtils.setValidErrNoNull(errors, "altezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA);
      }
      else
      {       
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_PLATEA))
        {
          if(Validator.validateDoubleIncludeZero(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDoubleIncludeZero(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9)) < 0) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else 
          {
            fabbricatoAziendaNuovaVO.setAltezza(new BigDecimal(fabbricatoAziendaNuovaVO.getStrAltezza().replace(',','.')));
          }
        }
        else
        {
          if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9)) <= 0) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else 
          {
            fabbricatoAziendaNuovaVO.setAltezza(new BigDecimal(fabbricatoAziendaNuovaVO.getStrAltezza().replace(',','.')));
          }       
        }
      }
    }
    else
    {
      if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrAltezza())) 
      {
        if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9) == null) {
          messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrAltezza(), 999.9)) <= 0) {
          messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else 
        {
          fabbricatoAziendaNuovaVO.setAltezza(new BigDecimal(fabbricatoAziendaNuovaVO.getStrAltezza().replace(',','.')));
        }
      }
    }
    
    
    if((errors !=null) && (errors.size() > 0) && Validator.isNotEmpty(messaggioErrore))  {
      errors.add("calcola",new ValidationError(messaggioErrore));
    }
    
    boolean flagCoperta = false;
    boolean flagScoperta = false;
    boolean flagSuperficie = false;
    // La superficie è obbligatoria
    if(Validator.isEmpty(fabbricatoAziendaNuovaVO.getStrSuperficie())) 
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_OBBLIGATORIA"));
    }
    // Se è valorizzata controllare che sia un valore numerico valido
    else 
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficie(), 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_ERRATA"));
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficie(), 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_ERRATA"));
      }
      else 
      {
        flagSuperficie = true;
        fabbricatoAziendaNuovaVO.setSuperficie(new BigDecimal(fabbricatoAziendaNuovaVO.getStrSuperficie().replace(',','.')));
      }
    }
    
    if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta()))
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta(), 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta(), 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie)
        {
          Double val1 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta(), 999999999.9));
          Double val2 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficie(), 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_COPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagCoperta = true;
        fabbricatoAziendaNuovaVO.setSuperficieCoperta(new BigDecimal(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta().replace(',','.')));
      }
    }
    
    if(Validator.isNotEmpty(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta()))
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta(), 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta(), 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie)
        {
          Double val1 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta(), 999999999.9));
          Double val2 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficie(), 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagScoperta = true;
        fabbricatoAziendaNuovaVO.setSuperficieScoperta(new BigDecimal(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta().replace(',','.')));
      }
    }
    
    if(flagCoperta && flagScoperta && flagSuperficie)
    {
      Double val1 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieCoperta(), 999999999.9));
      Double val2 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficieScoperta(), 999999999.9));
      Double val3 = new Double(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrSuperficie(), 999999999.9));
      if((val1.doubleValue() + val2.doubleValue()) > val3.doubleValue())
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
      }
    }
    
    // La dimensione è obbligatoria
    if(Validator.isEmpty(fabbricatoAziendaNuovaVO.getStrDimensione())) 
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_OBBLIGATORIA"));
    }
    // Se è stata valorizzata controllo che sia un numero valido
    else 
    {
      if(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrDimensione(), 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
      }
      else if(Double.parseDouble(Validator.validateDouble(fabbricatoAziendaNuovaVO.getStrDimensione(), 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
      }
      else 
      {
        fabbricatoAziendaNuovaVO.setDimensione(new BigDecimal(fabbricatoAziendaNuovaVO.getStrDimensione().replace(',','.')));
      }
    }
    
    
    
    
    Vector<ValidationErrors> elencoErroriPart = new Vector<ValidationErrors>();
    int countErrori = 0;
    
    if(Validator.isNotEmpty(vPartFabbrAziendaNuova) && (vPartFabbrAziendaNuova.size() > 0))
    {
    
      TipoUtilizzoVO[]  arrTipoUtilizzo = anagFacadeClient.getListTipiUsoSuoloByCodice(SolmrConstants.COD_TIPO_UTILIZZO_FABBRICATO,true,null,null,null);
      Long idUtilizzo =  arrTipoUtilizzo[0].getIdUtilizzo();
      TipoVarietaVO[] arrTipoVarieta = anagFacadeClient
        .getListTipoVarietaByIdUtilizzoAndCodice(idUtilizzo, SolmrConstants.COD_TIPO_VARIETA_GEN, true, null);
      Long idVarieta = arrTipoVarieta[0].getIdVarieta();
    
	    //Controlli da effettuare sulle particelle
	    for(int i=0;i<vPartFabbrAziendaNuova.size();i++)
	    {
	      int countErroriSingolo = 0;
	      ValidationErrors errorsPart = new ValidationErrors();
	      PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = vPartFabbrAziendaNuova.get(i);
	      
	      if (Validator.isEmpty(partFabbrAziendaNuovaVO.getIstatProvincia()))
        {
          errorsPart.add("istatProvincia", new ValidationError(
              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
          countErroriSingolo++;
        }
        
        if (Validator.isEmpty(partFabbrAziendaNuovaVO.getIstatComune()))
        {
          errorsPart.add("istatComune", new ValidationError(
              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
          countErroriSingolo++;
        }
        else
        {
          Vector<ComuneVO> vComune = hProvCom.get(partFabbrAziendaNuovaVO.getIstatProvincia());
	        for(int j=0;j<vComune.size();j++)
	        {
	          if(partFabbrAziendaNuovaVO.getIstatComune().equalsIgnoreCase(vComune.get(j).getIstatComune()))
	          {
	            partFabbrAziendaNuovaVO.setDesCom(vComune.get(j).getDescom());
	            partFabbrAziendaNuovaVO.setSglProv(vComune.get(j).getSiglaProv());
	          }
	        }
	      }
        
        if (Validator.isEmpty(partFabbrAziendaNuovaVO.getStrFoglio()))
        {
          errorsPart.add("foglio", new ValidationError(
              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
          countErroriSingolo++;
        }
        else
        {
          if(!Validator.isNumericInteger(partFabbrAziendaNuovaVO.getStrFoglio())) 
		      {
		        errorsPart = ErrorUtils.setValidErrNoNull(errorsPart, "foglio", AnagErrors.ERR_CAMPO_NON_CORRETTO);
		        countErrori++;
		        countErroriSingolo++;
		      }
		      else 
		      {
		        partFabbrAziendaNuovaVO.setFoglio(new Long(partFabbrAziendaNuovaVO.getStrFoglio()));
		      }
        }
        
        if (Validator.isEmpty(partFabbrAziendaNuovaVO.getStrParticella()))
        {
          errorsPart.add("particella", new ValidationError(
              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
          countErroriSingolo++;
        }
        else
        {
          if(!Validator.isNumericInteger(partFabbrAziendaNuovaVO.getStrParticella())) 
          {
            errorsPart = ErrorUtils.setValidErrNoNull(errorsPart, "particella", AnagErrors.ERR_CAMPO_NON_CORRETTO);
            countErrori++;
            countErroriSingolo++;
          }
          else 
          {
            partFabbrAziendaNuovaVO.setParticella(new Long(partFabbrAziendaNuovaVO.getStrParticella()));
          }
        }
        
        
        
        if(Validator.isEmpty(partFabbrAziendaNuovaVO.getStrSuperficie())) 
		    {
		      errorsPart = ErrorUtils.setValidErrNoNull(errorsPart, "superficiePart", AnagErrors.ERR_CAMPO_OBBLIGATORIO );
		      countErrori++;
		      countErroriSingolo++;
		    }
		    // Se è stata valorizzata controllo che sia un numero valido
		    else 
		    {
		      if(Validator.validateDouble(partFabbrAziendaNuovaVO.getStrSuperficie(), 999999.9999) == null) 
		      {
		        errorsPart = ErrorUtils.setValidErrNoNull(errorsPart, "superficiePart", AnagErrors.ERR_CAMPO_NON_CORRETTO);
		        countErrori++;
		        countErroriSingolo++;
		      }
		      else if(Double.parseDouble(Validator.validateDouble(partFabbrAziendaNuovaVO.getStrSuperficie(), 999999.9999)) <= 0) 
		      {
		        errorsPart = ErrorUtils.setValidErrNoNull(errorsPart, "superficiePart", AnagErrors.ERR_CAMPO_NON_CORRETTO);
		        countErrori++;
		        countErroriSingolo++;
		      }
		      else 
		      {
		        partFabbrAziendaNuovaVO.setSuperficie(new BigDecimal(partFabbrAziendaNuovaVO.getStrSuperficie().replace(',','.')));
		      }
		    }
	      
	      partFabbrAziendaNuovaVO.setIdUtilizzo(idUtilizzo);    
	      partFabbrAziendaNuovaVO.setIdVarieta(idVarieta);
        
        
        if (Validator.isEmpty(partFabbrAziendaNuovaVO.getIdTitoloPossesso()))
        {
          errorsPart.add("idTitoloPossesso", new ValidationError(
              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          countErrori++;
          countErroriSingolo++;
        }
        else
        {
          for(int j=0;j<elencoTipoTitoloPossesso.length;j++)
          {
            if(partFabbrAziendaNuovaVO.getIdTitoloPossesso().compareTo(elencoTipoTitoloPossesso[j].getCode()) == 0)
            {
              partFabbrAziendaNuovaVO.setDescTitoloPossesso(elencoTipoTitoloPossesso[j].getDescription());
              break;
            }
          }
        }
	      
	      
	      /*if(countErroriSingolo == 0)
	      {
	        //se nn ci sono stati errori verifico che sia presente su db_storico_particella
	        //e se esito negativo anche sudb_particella_certificata
	        try
	        {
	          if(!gaaFacadeClient.isParticellAttivaStoricoParticella(partFabbrAziendaNuovaVO.getIstatComune(), 
               partFabbrAziendaNuovaVO.getSezione(), partFabbrAziendaNuovaVO.getStrFoglio(), 
               partFabbrAziendaNuovaVO.getStrParticella(), partFabbrAziendaNuovaVO.getSubalterno()))
            {	        
			        ParticellaCertificataVO particellaCertificataVO = anagFacadeClient
			          .findParticellaCertificataByParameters(partFabbrAziendaNuovaVO.getIstatComune(), 
			           partFabbrAziendaNuovaVO.getSezione(), partFabbrAziendaNuovaVO.getStrFoglio(), 
			           partFabbrAziendaNuovaVO.getStrParticella(), partFabbrAziendaNuovaVO.getSubalterno(), true, null);
			        if(Validator.isEmpty(particellaCertificataVO)
			          || (Validator.isNotEmpty(particellaCertificataVO) && !particellaCertificataVO.isCertificata()))
			        {
			          errorsPart.add("particellaCertificata", new ValidationError(AnagErrors.ERR_PART_NO_CERT));
	              countErrori++;
			        }
			      }
		      }
		      catch(Exception ex)
			    {
			      SolmrLogger.info(this, " - newInserimentoFabbricatiCtrl.jsp - FINE PAGINA");
			      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
			      request.setAttribute("messaggioErrore",messaggio);
			      request.setAttribute("pageBack", actionUrl);
			      %>
			        <jsp:forward page="<%= erroreViewUrl %>" />
			      <%
			      return;
			    }
		      
	      }*/
	      
	      
	      
	      if(countErroriSingolo == 0)
        {
          //se nn ci sono stati errori verifico che sia presente su db_storico_particella
          //e se esito negativo anche sudb_particella_certificata
          try
          {
            if(Validator.isEmpty(anagFacadeClient.findFoglioByParameters(partFabbrAziendaNuovaVO.getIstatComune(), 
               partFabbrAziendaNuovaVO.getStrFoglio(), partFabbrAziendaNuovaVO.getSezione())))
            {              
              errorsPart.add("particellaCertificata", new ValidationError(AnagErrors.ERR_PART_NO_FOGLIO));
              countErrori++;
            }
          }
          catch(Exception ex)
          {
            SolmrLogger.info(this, " - newInserimentoFabbricatiCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
        }
	      
	      
	      
	      
	      elencoErroriPart.add(errorsPart);
	    }
	  }
	  //Nessuna particella selezionata
	  else
	  {
	    errors = ErrorUtils.setValidErrNoNull(errors, "inserisciFabb", "Deve essere associata almeno una particella");
	  }
    
    if((countErrori > 0) || (errors.size() > 0))
    {
      request.setAttribute("elencoErroriPart", elencoErroriPart);
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= newInserimentoFabbricatiUrl %>"/>
      <%
      return;
    }    
    else
    {
      fabbricatoAziendaNuovaVO.setvPartFabbrAziendaNuova(vPartFabbrAziendaNuova);
    }   
    
    
    
    if(vFabbrAziendaNuova == null)
    {
      vFabbrAziendaNuova = new Vector<FabbricatoAziendaNuovaVO>();
    }
    vFabbrAziendaNuova.add(fabbricatoAziendaNuovaVO);
    
    session.removeAttribute("vPartFabbrAziendaNuova");
    session.removeAttribute("fabbricatoAziendaNuovaVO");
  }
  
  
  session.setAttribute("vFabbrAziendaNuova", vFabbrAziendaNuova);
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoFabbricati") != null)) 
  {
    boolean trovato = false;
    if(Validator.isNotEmpty(vPartFabbrAziendaNuova)
      && (vPartFabbrAziendaNuova.size() > 0))
    {
      for(int i=0;i<vPartFabbrAziendaNuova.size();i++)
      {
        PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = vPartFabbrAziendaNuova.get(i);
        if(Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatProvincia())
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIstatComune())
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getSezione())
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrFoglio()) 
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrParticella()) 
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getSubalterno()) 
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getStrSuperficie()) 
          || Validator.isNotEmpty(partFabbrAziendaNuovaVO.getIdTitoloPossesso()))
        {
          trovato = true;
          break;
        }
        
      }
    
      if(trovato)
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "avanti", "Attenzione, cliccare sul pulsante ''inserisci fabbricato'' per non perdere i dati del fabbricato e delle particelle su cui esso e'' ubicato.");
      }
    }
    
    if(errors.size() > 0)
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= newInserimentoFabbricatiUrl %>"/>
      <%
      return;
    } 
  
  
    try 
		{
		  gaaFacadeClient.aggiornaFabbrAziendaNuovaIscrizione(aziendaNuovaVO, ruoloUtenza.getIdUtente(), vFabbrAziendaNuova);
		}
		catch(Exception ex)
		{
		  SolmrLogger.info(this, " - newInserimentoFabbricatiCtrl.jsp - FINE PAGINA");
		  String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		  request.setAttribute("messaggioErrore",messaggio);
		  request.setAttribute("pageBack", actionUrl);
		  %>
		    <jsp:forward page="<%= erroreViewUrl %>" />
		  <%
		  return;
		}
	  
	  %>
      <jsp:forward page="<%= pageNext %>"/>
    <%
    return;
  }
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoFabbricatiUrl %>"/>
  
