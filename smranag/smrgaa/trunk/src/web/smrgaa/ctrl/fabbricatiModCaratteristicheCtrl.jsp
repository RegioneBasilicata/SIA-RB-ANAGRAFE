<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "fabbricatiModCaratteristicheCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String elencoFabbricatiUrl = "/view/fabbricatiView.jsp";
  String elencoFabbricatiCtrlUrl = "/ctrl/fabbricatiCtrl.jsp";
  String fabbricatiModCaratteristicheUrl = "/view/fabbricatiModCaratteristicheView.jsp";
  String fabbricatiModUbicazioneUrl = "/view/fabbricatiModUbicazioneView.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  String operazione = request.getParameter("operazione");

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");

  ValidationErrors errors = null;

  // L'utente ha cliccato il pulsante calcola
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("calcolaVolume")) 
  {

    // Recupero i parametri
    String tipoFormaFabbricato = request.getParameter("tipiFormaFabbricato");
    double fattoreCubatura=0;
    TipoFormaFabbricatoVO tipoFormaFabbricatoVO = null;
    if(Validator.isNotEmpty(tipoFormaFabbricato))
    {
      try
      {
        tipoFormaFabbricatoVO = gaaFacadeClient.getTipoFormaFabbricato(new Long(tipoFormaFabbricato));
        fattoreCubatura = tipoFormaFabbricatoVO.getFattoreCubatura().doubleValue();
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
    Long idTipologiaFabbricato = null;
    String tipoFabbricato = request.getParameter("idTipologiaFabbricato");
    if(Validator.isNotEmpty(tipoFabbricato)) {
      idTipologiaFabbricato = Long.decode(tipoFabbricato);
    }
    String tipologiaColturaSerra = request.getParameter("tipologiaColturaSerra");
    String denominazioneFabbricato = request.getParameter("denominazioneFabbricato");
    String larghezzaFabbricato = request.getParameter("larghezzaFabbricato");
    String lunghezzaFabbricato = request.getParameter("lunghezzaFabbricato");
    String altezzaFabbricato = request.getParameter("altezzaFabbricato");
    String superficieFabbricato = request.getParameter("superficieFabbricato");
    String dimensioneFabbricato = request.getParameter("dimensioneFabbricato");
    String annoCostruzioneFabbricato = request.getParameter("annoCostruzioneFabbricato");
    String oreRisc = request.getParameter("oreRisc");
    String mesiRiscSerra = request.getParameter("mesiRiscSerra");
    String dataInizioValiditaFabbricato = request.getParameter("strDataInizioValiditaFabbricato");
    java.util.Date dataInizio = null;
    if(Validator.isNotEmpty(dataInizioValiditaFabbricato)) {
      if(Validator.validateDateF(dataInizioValiditaFabbricato)) {
        dataInizio = DateUtils.parseDate(dataInizioValiditaFabbricato);
      }
    }
    String noteFabbricato = request.getParameter("noteFabbricato");
    String unitaMisura = request.getParameter("unitaMisura");
    
    //**************************************
    String volumeUtilePresunto = request.getParameter("volumeUtilePresunto");
    String superficieScopertaExtraFabbricato = request.getParameter("superficieScopertaExtraFabbricato");
    String superficieCopertaFabbricato = request.getParameter("superficieCopertaFabbricato");
    String superficieScopertaFabbricato = request.getParameter("superficieScopertaFabbricato");

    // Setto i parametri all'interno del VO
    fabbricatoVO.setFattoreCubatura(fattoreCubatura);
    fabbricatoVO.setIdTipologiaFabbricato(idTipologiaFabbricato);
    fabbricatoVO.setTipiFormaFabbricato(tipoFormaFabbricato);
    fabbricatoVO.setTipologiaColturaSerra(tipologiaColturaSerra);
    fabbricatoVO.setDenominazioneFabbricato(denominazioneFabbricato);
    fabbricatoVO.setLarghezzaFabbricato(larghezzaFabbricato);
    fabbricatoVO.setLunghezzaFabbricato(lunghezzaFabbricato);
    fabbricatoVO.setAltezzaFabbricato(altezzaFabbricato);
    fabbricatoVO.setSuperficieFabbricato(superficieFabbricato);
    fabbricatoVO.setDimensioneFabbricato(dimensioneFabbricato);
    fabbricatoVO.setAnnoCostruzioneFabbricato(annoCostruzioneFabbricato);
    fabbricatoVO.setOreRisc(oreRisc);
    fabbricatoVO.setMesiRiscSerra(mesiRiscSerra);
    fabbricatoVO.setDataInizioValiditaFabbricato(dataInizio);
    fabbricatoVO.setStrDataInizioValiditaFabbricato(dataInizioValiditaFabbricato);
    fabbricatoVO.setNoteFabbricato(noteFabbricato);
    fabbricatoVO.setUnitaMisura(unitaMisura);
    
    //******************
    /*fabbricatoVO.setVolumeUtilePresuntoFabbricato(StringUtils.parseSuperficieField(volumeUtilePresunto));
    fabbricatoVO.setSuperficieScopertaExtraFabbricato(StringUtils.parseSuperficieField(superficieScopertaExtraFabbricato));
    fabbricatoVO.setSuperficieCopertaFabbricato(StringUtils.parseSuperficieField(superficieCopertaFabbricato));
    fabbricatoVO.setSuperficieScopertaFabbricato(StringUtils.parseSuperficieField(superficieScopertaFabbricato));*/
    
    fabbricatoVO.setVolumeUtilePresuntoFabbricato(volumeUtilePresunto);
    fabbricatoVO.setSuperficieScopertaExtraFabbricato(superficieScopertaExtraFabbricato);
    fabbricatoVO.setSuperficieCopertaFabbricato(superficieCopertaFabbricato);
    fabbricatoVO.setSuperficieScopertaFabbricato(superficieScopertaFabbricato);
    
    
    //Ricavo il tipo di struttura
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = fabbricatoVO.getTipoTipologiaFabbricatoVO();
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
      if(Validator.isNotEmpty(fabbricatoVO.getLarghezzaFabbricato()))
      {
        if(Validator.validateDouble(fabbricatoVO.getLarghezzaFabbricato(), 999.9) !=null) 
        {
          double larghezza = StringUtils.convertNumericField(fabbricatoVO.getLarghezzaFabbricato());
          superficie = 3.14 * (larghezza/2) * (larghezza/2);
          superficieFabbricato = String.valueOf(superficie);
          fabbricatoVO.setSuperficieFabbricato(StringUtils.parseDoubleFieldOneDecimal(superficieFabbricato));
          fabbricatoVO.setLarghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(larghezza)));
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
    else if(!"N".equalsIgnoreCase(tipoFormula))
    {
      if(Validator.isNotEmpty(fabbricatoVO.getLarghezzaFabbricato()) 
        && Validator.isNotEmpty(fabbricatoVO.getLunghezzaFabbricato())) 
      {
        if(Validator.validateDouble(fabbricatoVO.getLarghezzaFabbricato(), 999.9) !=null 
          && Validator.validateDouble(fabbricatoVO.getLunghezzaFabbricato(), 999.9) != null) 
        {
          double lunghezza = StringUtils.convertNumericField(fabbricatoVO.getLunghezzaFabbricato());
          double larghezza = StringUtils.convertNumericField(fabbricatoVO.getLarghezzaFabbricato());
          superficie = lunghezza * larghezza;
          superficieFabbricato = String.valueOf(superficie);
          fabbricatoVO.setSuperficieFabbricato(StringUtils.parseDoubleFieldOneDecimal(superficieFabbricato));
          fabbricatoVO.setLunghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(lunghezza)));
          fabbricatoVO.setLarghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(larghezza)));
        }
        else 
        {
          if(Validator.validateDouble(fabbricatoVO.getLarghezzaFabbricato(), 999.9) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA") +", ";
            errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
          }
          if(Validator.validateDouble(fabbricatoVO.getLunghezzaFabbricato(), 999.99) == null) 
          {
            messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors,"lunghezza", (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
          }
        }
      }
      else 
      {
        if(!Validator.isNotEmpty(fabbricatoVO.getLarghezzaFabbricato())) 
        {
          messaggioErrore = (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_OBBLIGATORIA") +", ";
          errors = ErrorUtils.setValidErrNoNull(errors,"larghezza", (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_OBBLIGATORIA"));
        }
        if(!Validator.isNotEmpty(fabbricatoVO.getLunghezzaFabbricato())) 
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
      if(Validator.isNotEmpty(fabbricatoVO.getIdTipologiaFabbricato())) 
      {
        if(fabbricatoVO.getUnitaMisura() != null 
          && fabbricatoVO.getUnitaMisura().equalsIgnoreCase((String)SolmrConstants.get("UNITA_MISURA_METRI_CUBI"))) 
        {
          if(Validator.isNotEmpty(fabbricatoVO.getLarghezzaFabbricato()) 
            && Validator.isNotEmpty(fabbricatoVO.getLunghezzaFabbricato()) && Validator.isNotEmpty(fabbricatoVO.getAltezzaFabbricato())) 
          {
            if(Validator.validateDouble(fabbricatoVO.getLarghezzaFabbricato(),999.9) != null 
              && Validator.validateDouble(fabbricatoVO.getLunghezzaFabbricato(),999.9) != null 
              && Validator.validateDouble(fabbricatoVO.getAltezzaFabbricato(),999.9) != null) 
            {
              double larghezza = StringUtils.convertNumericField(fabbricatoVO.getLarghezzaFabbricato());
              double lunghezza = StringUtils.convertNumericField(fabbricatoVO.getLunghezzaFabbricato());
              double altezza = StringUtils.convertNumericField(fabbricatoVO.getAltezzaFabbricato());
              if (fattoreCubatura>0)
              {
                dimensione = String.valueOf(larghezza * lunghezza * altezza * fattoreCubatura);
              }
              else
              {
                dimensione = String.valueOf(larghezza * lunghezza * altezza);
              }
              fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(dimensione));
              fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
            }
          }
        }
      }
  
      // Controllo che l'utente abbia selezionato un tipo fabbricato e che quest'ultimo preveda
      // come unita di misura i metri quadri.
      if(Validator.isNotEmpty(fabbricatoVO.getIdTipologiaFabbricato())) 
      {
        if(fabbricatoVO.getUnitaMisura() != null 
          && fabbricatoVO.getUnitaMisura().equalsIgnoreCase(SolmrConstants.UNITA_MISURA_METRI_QUADRI)) 
        {
          if(Validator.isNotEmpty(fabbricatoVO.getLarghezzaFabbricato()) 
            && Validator.isNotEmpty(fabbricatoVO.getLunghezzaFabbricato())) 
          {
            if(Validator.validateDouble(fabbricatoVO.getLarghezzaFabbricato(),999.9) != null 
              && Validator.validateDouble(fabbricatoVO.getLunghezzaFabbricato(),999.9) != null) 
            {
              double larghezza = StringUtils.convertNumericField(fabbricatoVO.getLarghezzaFabbricato());
              double lunghezza = StringUtils.convertNumericField(fabbricatoVO.getLunghezzaFabbricato());
              dimensione = String.valueOf(larghezza * lunghezza);
              fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(dimensione));
            }
          }
        }
      }
    }
    //Per i conti considero l'altezza che però essendo presenti a video solo due campi
    //corrisponde al parametro lunghezza
    else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
    {
      if(Validator.isNotEmpty(fabbricatoVO.getLunghezzaFabbricato())) 
      {
        if(Validator.validateDouble(fabbricatoVO.getLunghezzaFabbricato(), 999.9) !=null) 
        {
          double altezza = StringUtils.convertNumericField(fabbricatoVO.getLunghezzaFabbricato());
          dimensione = String.valueOf(superficie * (altezza - 0.1));
          fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(dimensione));
          fabbricatoVO.setLunghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
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
      if(Validator.isNotEmpty(fabbricatoVO.getAltezzaFabbricato())) 
      {
        if(Validator.validateDouble(fabbricatoVO.getAltezzaFabbricato(), 999.9) !=null) 
        {
          double altezza = StringUtils.convertNumericField(fabbricatoVO.getAltezzaFabbricato());
          dimensione = String.valueOf(superficie * (altezza - 0.1));
          fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(dimensione));
          fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
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
    else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_PLATEA))
    {
      if((tipoFormaFabbricatoVO !=null) && (Validator.isNotEmpty(tipoFormaFabbricatoVO.getCoeffImpilabile())))
      {
        if(Validator.isNotEmpty(fabbricatoVO.getAltezzaFabbricato())) 
        {
          if(Validator.validateDoubleIncludeZero(fabbricatoVO.getAltezzaFabbricato(), 999.9) !=null) 
          {
            double coeffImpilabile = tipoFormaFabbricatoVO.getCoeffImpilabile().doubleValue();
            double altezza = StringUtils.convertNumericField(fabbricatoVO.getAltezzaFabbricato());
            double dimensioneDoubl = (superficie * altezza * fattoreCubatura) + (superficie * coeffImpilabile);
            dimensione = String.valueOf(dimensioneDoubl);
            fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(dimensione));
            fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(altezza)));
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
      else
      {
        throw new SolmrException("Attenzione coefficiente Impilabile non presente su DB");
      }
    }
    else //Inseribile dall'utente Lagone
    {}
    
    if((tipoTipologiaFabbricatoVO !=null) && 
      (Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getFlagStoccaggio()) 
      &&  tipoTipologiaFabbricatoVO.getFlagStoccaggio().equalsIgnoreCase("S")))
    {
      fabbricatoVO.setVolumeUtilePresuntoFabbricato(StringUtils.parseDoubleFieldOneDecimal(String.valueOf(dimensione)));
      fabbricatoVO.setSuperficieScopertaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieFabbricato()));
    }


    // Metto il VO in sessione
    session.setAttribute("fabbricatoVO",fabbricatoVO);

    // Se ci sono errori li visualizzo
    if(errors !=null) 
    {
      errors.add("calcola", new ValidationError(messaggioErrore));
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= fabbricatiModCaratteristicheUrl %>"/>
      <%
      return;
    }

    // Altrimenti vado alla pagina di inserimento con la superficie calcolata
    %>
       <jsp:forward page="<%= fabbricatiModCaratteristicheUrl %>"/>
    <%

  }

  // L'utente ha cliccato su avanti
  if(request.getParameter("avanti") != null) 
  {

    // Ricerco la informazioni relative all'unità produttiva su cui si trova il fabbricato
    UteVO uteVO = null;
    try {
      uteVO = anagFacadeClient.getUteById(fabbricatoVO.getIdUnitaProduttivaFabbricato());
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(elencoFabbricatiUrl).forward(request, response);
      return;
    }
    // Metto l'oggetto in request
    request.setAttribute("uteVO", uteVO);

    // Recupero i parametri
    String tipoFabbricato = request.getParameter("idTipologiaFabbricato");
    Long idTipologiaFabbricato = null;
    if(Validator.isNotEmpty(tipoFabbricato)) {
      idTipologiaFabbricato = Long.decode(tipoFabbricato);
    }
    String tipoFormaFabbricato = request.getParameter("tipiFormaFabbricato");
    String tipologiaColturaSerra = request.getParameter("tipologiaColturaSerra");
    String denominazioneFabbricato = request.getParameter("denominazioneFabbricato");
    String larghezzaFabbricato = request.getParameter("larghezzaFabbricato");
    String lunghezzaFabbricato = request.getParameter("lunghezzaFabbricato");
    String altezzaFabbricato = request.getParameter("altezzaFabbricato");
    String superficieFabbricato = request.getParameter("superficieFabbricato");
    String dimensioneFabbricato = request.getParameter("dimensioneFabbricato");
    String annoCostruzioneFabbricato = request.getParameter("annoCostruzioneFabbricato");
    String oreRisc = request.getParameter("oreRisc");
    String mesiRiscSerra = request.getParameter("mesiRiscSerra");
    String dataInizioValiditaFabbricato = request.getParameter("strDataInizioValiditaFabbricato");
    java.util.Date dataInizio = null;
    if(Validator.isNotEmpty(dataInizioValiditaFabbricato)) {
      if(Validator.validateDateF(dataInizioValiditaFabbricato)) {
        dataInizio = DateUtils.parseDate(dataInizioValiditaFabbricato);
      }
    }
    String noteFabbricato = request.getParameter("noteFabbricato");
    String unitaMisura = request.getParameter("unitaMisura");
    String volumeUtilePresunto = request.getParameter("volumeUtilePresunto");
    String superficieScopertaExtraFabbricato = request.getParameter("superficieScopertaExtraFabbricato");
    String superficieCopertaFabbricato = request.getParameter("superficieCopertaFabbricato");
    String superficieScopertaFabbricato = request.getParameter("superficieScopertaFabbricato");

    // Setto i parametri all'interno del VO
    fabbricatoVO.setIdTipologiaFabbricato(idTipologiaFabbricato);
    fabbricatoVO.setTipiFormaFabbricato(tipoFormaFabbricato);
    fabbricatoVO.setTipologiaColturaSerra(tipologiaColturaSerra);
    fabbricatoVO.setDenominazioneFabbricato(denominazioneFabbricato);
    fabbricatoVO.setLarghezzaFabbricato(larghezzaFabbricato);
    fabbricatoVO.setLunghezzaFabbricato(lunghezzaFabbricato);
    fabbricatoVO.setAltezzaFabbricato(altezzaFabbricato);
    fabbricatoVO.setSuperficieFabbricato(superficieFabbricato);
    fabbricatoVO.setDimensioneFabbricato(dimensioneFabbricato);
    fabbricatoVO.setAnnoCostruzioneFabbricato(annoCostruzioneFabbricato);
    fabbricatoVO.setOreRisc(oreRisc);
    fabbricatoVO.setMesiRiscSerra(mesiRiscSerra);
    fabbricatoVO.setDataInizioValiditaFabbricato(dataInizio);
    fabbricatoVO.setStrDataInizioValiditaFabbricato(dataInizioValiditaFabbricato);
    fabbricatoVO.setNoteFabbricato(noteFabbricato);
    fabbricatoVO.setUnitaMisura(unitaMisura);
    fabbricatoVO.setVolumeUtilePresuntoFabbricato(volumeUtilePresunto);
    fabbricatoVO.setSuperficieScopertaExtraFabbricato(superficieScopertaExtraFabbricato);
    fabbricatoVO.setSuperficieCopertaFabbricato(superficieCopertaFabbricato);
    fabbricatoVO.setSuperficieScopertaFabbricato(superficieScopertaFabbricato);

    int numeroMesi=0;

    if(Validator.isNotEmpty(fabbricatoVO.getTipologiaColturaSerra()))
      numeroMesi = anagFacadeClient.getMesiRiscaldamentoBySerra(fabbricatoVO.getTipologiaColturaSerra());

    // Effettuo il controllo formale sulla correttezza dei dati
    errors = fabbricatoVO.validateModFabbricato(numeroMesi);

    // Se ci sono errori li visualizzo
    if(errors != null) 
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= fabbricatiModCaratteristicheUrl %>"/>
      <%
      return;
    }

    // Recupero l'elenco delle particelle associate al fabbricato
    Vector elencoParticelleAssociate = (Vector)session.getAttribute("elencoParticelleAssociate");
    if(elencoParticelleAssociate == null) {
      try {
        elencoParticelleAssociate = anagFacadeClient.getElencoParticelleByFabbricato(fabbricatoVO, true);
      }
      catch(SolmrException se) {
        errors.add("error", new ValidationError(se.getMessage()));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(fabbricatiModCaratteristicheUrl).forward(request, response);
        return;
      }
      session.setAttribute("elencoParticelleAssociate", elencoParticelleAssociate);
    }

    // Recupero l'elenco delle particelle associabili al fabbricato
    Vector elencoParticelleAssociabili = (Vector)session.getAttribute("elencoParticelleAssociabili");
    if(elencoParticelleAssociabili == null)
    {
      Vector elencoId = new Vector();
      if(elencoParticelleAssociate != null && elencoParticelleAssociate.size() > 0) {
        Iterator itera = elencoParticelleAssociate.iterator();
        while(itera.hasNext()) {
          ParticellaVO vo = (ParticellaVO)itera.next();
          Long id = vo.getIdParticella();
          elencoId.add(id);
        }
      }

      try
      {
        if(fabbricatoVO.getIdTipologiaFabbricato().equals((Long)SolmrConstants.get("TIPO_TIPOLOGIA_FABBRICATO_SERRA"))
      || fabbricatoVO.getIdTipologiaFabbricato().equals((Long)SolmrConstants.get("TIPO_TIPOLOGIA_FABBRICATO_SERRA_RISCALDATA")))
        {
          //SERRA
          elencoParticelleAssociabili = anagFacadeClient.getElencoParticelleFabbricatoByUteAssociabili(fabbricatoVO.getIdUnitaProduttivaFabbricato(),elencoId,true);
        }
        else
        {
          elencoParticelleAssociabili = anagFacadeClient.getElencoParticelleFabbricatoByUteAssociabili(fabbricatoVO.getIdUnitaProduttivaFabbricato(),elencoId,false);
        }
      }
      catch(SolmrException se)
      {
        errors.add("error", new ValidationError(se.getMessage()));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(fabbricatiModCaratteristicheUrl).forward(request, response);
        return;
      }
      session.setAttribute("elencoParticelleAssociabili", elencoParticelleAssociabili);
    }
    // Altrimenti passo allo step successivo
    session.setAttribute("fabbricatoVO",fabbricatoVO);
    %>
       <jsp:forward page="<%= fabbricatiModUbicazioneUrl %>"/>
    <%
  }

	// L'utente ha scelto la funzionalità "modifica" del fabbricato
	else 
  {
	  session.removeAttribute("fabbricatoVO");
  	session.removeAttribute("elencoParticelleAssociate");
  	session.removeAttribute("elementiSelezionatiAssociabili");
  	session.removeAttribute("elencoParticelleAssociabili");

  	// Controllo che l'utente abbia scelto il fabbricato da modificare
  	String idFabbricato = request.getParameter("idFabbricato");
  	if(!Validator.isNotEmpty(idFabbricato)) 
    {
    	errors.add("error", new ValidationError((String)AnagErrors.get("ERR_FABBRICATO_NO_SELEZIONATO_MODIFICA")));
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(elencoFabbricatiCtrlUrl).forward(request, response);
    	return;
  	}

  	// Se lo ha selezionato recupero i dati dal DB e controllo che il fabbricato
  	// selezionato sia ancora in utilizzo all'azienda trattata
  	try 
    {
    	fabbricatoVO = anagFacadeClient.findFabbricatoByPrimaryKey(Long.decode(idFabbricato));
      //Aggiunta forma fabbricato!!!!
      TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = gaaFacadeClient.getInfoTipologiaFabbricato(fabbricatoVO.getIdTipologiaFabbricato());
      fabbricatoVO.setTipoTipologiaFabbricatoVO(tipoTipologiaFabbricatoVO);
    }
  	catch(SolmrException se) 
    {
    	errors.add("error", new ValidationError(se.getMessage()));
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(elencoFabbricatiCtrlUrl).forward(request, response);
    	return;
  	}
  	if(fabbricatoVO.getDataFineValiditaFabbricato() != null) 
    {
    	errors.add("error", new ValidationError((String)AnagErrors.get("ERR_FABBRICATO_NO_MODIFICABILE")));
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(elencoFabbricatiCtrlUrl).forward(request, response);
    	return;
  	}
  	// Se invece lo è ancora metto l'oggetto in sessione e vado alla pagina di modifica
  	session.setAttribute("fabbricatoVO",fabbricatoVO);

  	%>
     	<jsp:forward page="<%= fabbricatiModCaratteristicheUrl %>"/>
  	<%
	}

%>
