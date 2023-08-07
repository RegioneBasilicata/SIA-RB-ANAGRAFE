<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  String iridePageName = "allevamentiNewCtrl.jsp";
%>
<%@include file="/include/autorizzazione.inc"%>
<%
  String allevamentiNewUrl = "/view/allevamentiNewView.jsp";

  try
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    GaaFacadeClient clientGaa =  GaaFacadeClient.getInstance();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
    
    Date parametroAllSuGnps = (Date)anagClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ALL_SU_GNPS);
    request.setAttribute("parametroAllSuGnps", parametroAllSuGnps);
    
    //Popolo vettore di di cuaa inerenti all'azienda per controlli cuaa det/prop
    Vector<PersonaFisicaVO> v_soggetti = anagClient.getSoggetti(anagAziendaVO.getIdAzienda(), new Boolean(false));
    String parametroCtrlAllevamenti = (String)anagClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_CTRL_ALLEVAMENTI);
    StringTokenizer strToken = new StringTokenizer(parametroCtrlAllevamenti, ",");
    Vector<Long> vIdRuolo = new Vector<Long>();
    while(strToken.hasMoreTokens()) 
    {
      vIdRuolo.add(new Long(strToken.nextToken()));
    }        
    Vector<String> vCuaaPossDet = new Vector<String>();
    Vector<String> vCuaaPossProp = new Vector<String>();
    vCuaaPossDet.add(anagAziendaVO.getCUAA());
    vCuaaPossProp.add(anagAziendaVO.getCUAA());
    
    for(int j=0;j<v_soggetti.size();j++)
    {
      if(vIdRuolo.contains(v_soggetti.get(j).getIdRuolo()))
      {
        if(!vCuaaPossDet.contains(v_soggetti.get(j).getCodiceFiscale()))
        {
          vCuaaPossDet.add(v_soggetti.get(j).getCodiceFiscale());
        }
          
        if(!vCuaaPossProp.contains(v_soggetti.get(j).getCodiceFiscale()))
        {
          vCuaaPossProp.add(v_soggetti.get(j).getCodiceFiscale());
        }
      }          
    }
    
    request.setAttribute("vCuaaPossDet", vCuaaPossDet);
    request.setAttribute("vCuaaPossProp", vCuaaPossProp);
    
    
    
    
    
    
    
    String operazione = request.getParameter("operazione");
    String flagStallaPascolo = request.getParameter("flagStallaPascolo");
    if ("comboSpecie".equals(operazione))
    {
      session.removeAttribute("sottoCategorieAllevamenti");
      session.removeAttribute("stabulazioniTrattamenti");
    }
    
    //Indipendentemente dal motivo per cui sono qui devo andare a vedere
    //se ci sono righe nella consistenza e memorizzarle in session sottoCategorieAllevamenti
    Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = (Vector<SottoCategoriaAllevamento>)session.getAttribute("sottoCategorieAllevamenti");
       
    if (sottoCategorieAllevamenti != null)
    {
      String capiTab[]=request.getParameterValues("capiTab");
      String capiProprietaTab[]=request.getParameterValues("capiProprietaTab");
      String pesoVivoTab[]=request.getParameterValues("pesoVivoTab");
      
      String nummeroClicliAnnualiTab[]=request.getParameterValues("numeroClicliAnnualiTab");
      String cicliTab[]=request.getParameterValues("cicliTab");
      String giorniPascoloEstateTab[]=request.getParameterValues("giorniPascoloEstateTab");
      String orePascoloEstateTab[]=request.getParameterValues("orePascoloEstateTab");
      String giorniPascoloInvernoTab[]=request.getParameterValues("giorniPascoloInvernoTab");
      String orePascoloInvernoTab[]=request.getParameterValues("orePascoloInvernoTab");
      //devo memorizzare i dati inseriti/modificati dall'utente
      
      int size=sottoCategorieAllevamenti.size();
      if (capiTab!=null)
      {
        for (int i=0;i<size && i<capiTab.length;i++)
        {
          SottoCategoriaAllevamento sottoCategoria = sottoCategorieAllevamenti.get(i);
          sottoCategoria.setQuantita(capiTab[i]);
          sottoCategoria.setQuantitaProprieta(capiProprietaTab[i]);
          sottoCategoria.setPesoVivo(pesoVivoTab[i]);
          
          
          sottoCategoria.setNumeroCicliAnnuali(nummeroClicliAnnualiTab[i]);
          sottoCategoria.setCicli(cicliTab[i]);
          sottoCategoria.setGiorniPascoloEstate(giorniPascoloEstateTab[i]);
          sottoCategoria.setOrePascoloEstate(orePascoloEstateTab[i]);
          sottoCategoria.setGiorniPascoloInverno(giorniPascoloInvernoTab[i]);
          sottoCategoria.setOrePascoloInverno(orePascoloInvernoTab[i]);
        }
      }
    }
    
    
    //Indipendentemente dal motivo per cui sono qui devo andare a vedere
    //se ci sono righe nella stabulazione e memorizzarle in session stabulazioniTrattamenti
    Vector<StabulazioneTrattamento> stabulazioniTrattamenti = (Vector<StabulazioneTrattamento>) session.getAttribute("stabulazioniTrattamenti");
    
    if (stabulazioniTrattamenti != null)
    {
      String idCatSottocatStab[]=request.getParameterValues("idCatSottocatStab");
      String idStabulazione[]=request.getParameterValues("idStabulazione");
      String quantitaStab[]=request.getParameterValues("quantitaStab");
      String palabile[]=request.getParameterValues("palabile");
      String nonPalabile[]=request.getParameterValues("nonPalabile");
      
      int size=stabulazioniTrattamenti.size();
      if (idCatSottocatStab!=null)
      {
        for (int i=0;i<size && i<idCatSottocatStab.length;i++)
        {
          StabulazioneTrattamento stabulazione = stabulazioniTrattamenti.get(i);
          stabulazione.setIdSottoCategoriaAnimale(idCatSottocatStab[i]);
          stabulazione.setIdStabulazione(idStabulazione[i]);
          stabulazione.setQuantita(quantitaStab[i]);
          
          if(Validator.isNotEmpty(idCatSottocatStab[i]) 
             && Validator.isNotEmpty(idStabulazione[i]))
           {
             BaseCodeDescription[] stabulazioniDesc = clientGaa.getTipiStabulazione(Long.parseLong(idCatSottocatStab[i]));
             for(int j=0;j<stabulazioniDesc.length;j++)
             {
               if(new Long(idStabulazione[i]).longValue() == stabulazioniDesc[j].getCode())
               {
                 stabulazione.setLettieraPermanente((String)stabulazioniDesc[j].getItem());
                 break;
               }
             }
           }
          
          
          //stabulazione.setIdTrattamento(idTrattamentoStab[i]);
          stabulazione.setPalabile(palabile[i]);
          stabulazione.setNonPalabile(nonPalabile[i]);
          
          stabulazione.setTipoTrattamento(null);
          stabulazione.setFlagCalcolo(true);
        }
      }
    }
    
    
    
    
    AllevamentoAnagVO all = null;
    if (request.getParameter("salva") != null)
    {
      // Recupero i parametri
      all = new AllevamentoAnagVO();
      String indirizzo = request.getParameter("indirizzo");
      String cap = request.getParameter("cap");
      String telefono = request.getParameter("telefono");
      all.setDenominazione(request.getParameter("denominazioneAllevamento"));
      all.setIdUTE(request.getParameter("idUTE"));
      all.setIdASL(request.getParameter("idASL"));
      all.setIstatComuneAllevamento(request.getParameter("resComune"));
      // Setto i parametri nel VO
      all.setIndirizzo(indirizzo);
      all.setCap(cap);
      all.setTelefono(telefono);
      ValidationErrors errors = new ValidationErrors();
      all.setIdSpecieAnimale(request.getParameter("idSpecie"));
      all.setCodiceAziendaZootecnica(request.getParameter("codiceAziendaZootecnica").toUpperCase());
      all.setDataInizioAttivita(request.getParameter("dataInizioAttivita"));
      all.setLatitudineStr(request.getParameter("latitudine"));
      all.setLongitudineStr(request.getParameter("longitudine"));
      all.setLongitudineStr(request.getParameter("longitudine"));
      all.setDataInizio(request.getParameter("dal"));
      all.setNote(request.getParameter("note"));

      if(request.getParameter("codiceFiscaleProprietario") != null)
        all.setCodiceFiscaleProprietario(request.getParameter("codiceFiscaleProprietario").trim());
      all.setDenominazioneProprietario(request.getParameter("denominazioneProprietario"));
      if(request.getParameter("codiceFiscaleDetentore") != null)
      all.setCodiceFiscaleDetentore(request.getParameter("codiceFiscaleDetentore").trim());
      all.setDenominazioneDetentore(request.getParameter("denominazioneDetentore"));
      all.setDataInizioDetenzione(request.getParameter("dataInizioDetenzione"));
      all.setDataFineDetenzione(request.getParameter("dataFineDetenzione"));

      all.setIdTipoProduzione(request.getParameter("idTipoProduzione"));
      all.setIdOrientamentoProduttivo(request.getParameter("idOrientamentoProduttivo"));
      all.setIdTipoProduzioneCosman(request.getParameter("idTipoProduzioneCosman"));

      if (SolmrConstants.FLAG_S.equals(request.getParameter("soccida")))
        all.setFlagSoccida(true);
      else
        all.setFlagSoccida(false);
      
      all.setMotivoSoccida(request.getParameter("motivoSoccida"));
      all.setFlagAssicuratoCosman(request.getParameter("assicuratoSoccida"));
        
      all.setFlagDeiezioneAvicoli(request.getParameter("flagDeiezioneAvicoli"));  
      
        
      
      //***********************
      all.setSuperficieLettieraPermanente(request.getParameter("superficieLettieraPermanente"));
      all.setAltezzaLettieraPermanente(request.getParameter("altezza"));
      
      
      
      try
      {
        //Leggo i dati della specie per poter controllare che l'altezza sia compresa tra un minimo ed un 
        //massimo
        TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = anagClient.getTipoSpecieAnimale(Long.decode(all.getIdSpecieAnimale()));
        all.setAltLettieraPermMin(tipoSpecieAnimaleAnagVO.getAltLettieraPermMin());
        all.setAltLettieraPermMax(tipoSpecieAnimaleAnagVO.getAltLettieraPermMax());
      }
      catch (Exception e)   { }
      
      

      if (request.getParameter("resProvincia") != null)
      {
        all.setCodiceProvinciaAllevamento(request.getParameter("resProvincia").toUpperCase());
      }

      String istatComune = null;
      if (Validator.isNotEmpty(request.getParameter("descResComune")) && Validator.isNotEmpty(all.getCodiceProvinciaAllevamento()))
      {
        try
        {
          istatComune = anagClient.ricercaCodiceComuneNonEstinto(request.getParameter("descResComune").toUpperCase(), all.getCodiceProvinciaAllevamento());
        }
        catch (SolmrException se)
        {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("descComune", error);
        }
      }
      all.setIstatComuneAllevamento(istatComune);
      
      all.setCategorieAllevamentoVector(sottoCategorieAllevamenti);
      
      all.setStabulazioniTrattamenti(stabulazioniTrattamenti);
      
      if (stabulazioniTrattamenti!=null)
      {
        //Preparo dei dati necessari ai calcoli
        int sizeStab = stabulazioniTrattamenti.size();
        for (int j = 0; j < sizeStab; j++)
        {
          try
          {
            StabulazioneTrattamento stab = stabulazioniTrattamenti.get(j);
            stab.setStabPal(null); //Rimuovo i dati eventualmente inseriti da salvataggi precedenti o da calcoli
            stab.setStabNonPal(null);
            long idSottoCategoriaAnimale=Long.parseLong(stab.getIdSottoCategoriaAnimale());
            long idStabulazione=Long.parseLong(stab.getIdStabulazione());
            stab.setStabPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,true, idStabulazione));
            stab.setStabNonPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,false, idStabulazione));
          }
          catch(Exception e){}
        }
      }
      
      
      //Verifico se detentore e/o proprietario
      boolean isProprietario = false;
      boolean isDetentore = false;
      if(Validator.isNotEmpty(all.getCodiceFiscaleProprietario()))
      {
        for(int j=0;j<vCuaaPossProp.size();j++)
        {
          if(all.getCodiceFiscaleProprietario().equalsIgnoreCase(vCuaaPossProp.get(j)))
          {
            isProprietario = true;
            break;
          }
        }
      }
      if(Validator.isNotEmpty(all.getCodiceFiscaleDetentore()))
      {
        for(int j=0;j<vCuaaPossDet.size();j++)
        {
          if(all.getCodiceFiscaleDetentore().equalsIgnoreCase(vCuaaPossDet.get(j)))
          {
            isDetentore = true;
            break;
          }
        }
      }
      
     
      
      

      errors = all.validateForInsert(errors, isProprietario, isDetentore);

      //request.setAttribute("errors", errors);
      
      
      //controlli cosman
      if (errors.size() == 0)
      {
        Vector<CodeDescription> tipoCosman = anagClient.getTipoProduzioneCosman(all.getIdSpecieAnimaleLong().longValue(), 
          new Long(all.getIdTipoProduzione()).longValue(), new Long(all.getIdOrientamentoProduttivo()).longValue());
        //Il controllo va effettuato solo se nella combo è presente almeno un record...
        if(tipoCosman.size() > 0)
        {
        
          if(Validator.isEmpty(all.getIdTipoProduzioneCosman()))
          {
            errors.add("idTipoProduzioneCosman", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
          }
          else
          {
      
            Vector<CodeDescription> vSottoCateSi = anagClient.getSottocategorieCosman(all.getIdSpecieAnimaleLong().longValue(), 
              new Long(all.getIdTipoProduzione()).longValue(), new Long(all.getIdOrientamentoProduttivo()).longValue(), 
              new Long(all.getIdTipoProduzioneCosman()).longValue(), "S");
            Vector<CodeDescription> vSottoCateNo = anagClient.getSottocategorieCosman(all.getIdSpecieAnimaleLong().longValue(), 
              new Long(all.getIdTipoProduzione()).longValue(), new Long(all.getIdOrientamentoProduttivo()).longValue(), 
              new Long(all.getIdTipoProduzioneCosman()).longValue(), "N");
            
            
            //cerco se il vettore è valorizzato che esista almeno un sottocategoria di quelle possibili
            boolean okSi = false;  
            if(Validator.isNotEmpty(vSottoCateSi))
            {
              for(int j=0;j<sottoCategorieAllevamenti.size();j++)
              {
                for(int z=0;z<vSottoCateSi.size();z++)
                {
                  if(vSottoCateSi.get(z).getCode().intValue() == new Long(sottoCategorieAllevamenti.get(j).getIdSottoCategoriaAnimale()).intValue())
                  {
                    okSi = true;
                    break;
                  }
                }
                
                if(okSi)
                  break;
              }
            }
            else
            {
              okSi = true;
            }
            
            String msgCosman = "";
            if(!okSi)
            {
              //errors.add("idTipoProduzioneCosman", new ValidationError(AnagErrors.ERRORE_TIPO_PROD_COSMAN));
              
              msgCosman = "E'' possibile indicare l''orientamento Cosman selezionato solo in presenza di capi delle sottocategorie ";
              for(int z=0;z<vSottoCateSi.size();z++)
              {
                if(z!=0)
                {
                  msgCosman += ", ";
                }
                msgCosman += vSottoCateSi.get(z).getDescription();
              } 
              msgCosman += ". ";
            }
            
            boolean okNo = true;  
            if(Validator.isNotEmpty(vSottoCateNo))
            {
              for(int j=0;j<sottoCategorieAllevamenti.size();j++)
              {
                for(int z=0;z<vSottoCateNo.size();z++)
                {                   
                  if(vSottoCateNo.get(z).getCode().intValue() 
                    == new Long(sottoCategorieAllevamenti.get(j).getIdSottoCategoriaAnimale()).intValue())
                  {
                    okNo = false;
                    break;
                  }
                }
                
                if(!okNo)
                  break;
              }
            }
              
            if(!okNo)
            {
              //errors.add("idTipoProduzioneCosman", new ValidationError(AnagErrors.ERRORE_TIPO_PROD_COSMAN));
              msgCosman += "E'' possibile indicare l''orientamento Cosman selezionato solo se non sono presenti capi delle sottocategorie ";
              for(int z=0;z<vSottoCateNo.size();z++)
              {
                if(z!=0)
                {
                  msgCosman += ", ";
                }
                msgCosman += vSottoCateNo.get(z).getDescription();
              }               
            }
            
            if(!okSi || !okNo)
            {
              errors.add("idTipoProduzioneCosman", new ValidationError(msgCosman));
            }
            
          }
        }
        
      }
      
      

      // Se l'azienda non è provvisoria l'id asl e il codice azienda
      // zootecnico sono obbligatorio
      if (!anagAziendaVO.isFlagAziendaProvvisoria())
      {
        // Se l'id_specie_animale è stato valorizzato
        if (Validator.isNotEmpty(all.getIdSpecieAnimale()))
        {
          // Controllo che se flag_obbligo_asl = "S"
          TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO = null;
          try
          {
            tipoSpecieAnimaleAnagVO = anagClient.getTipoSpecieAnimale(Long.decode(all.getIdSpecieAnimale()));
          }
          catch (SolmrException se)
          {
            ValidationError error = new ValidationError(se.getMessage());
            errors.add("errors", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(allevamentiNewUrl).forward(request, response);
            return;
          }
          if (tipoSpecieAnimaleAnagVO.isObbligoAsl())
          {
            // L'ASL è obbligatoria
            if (!Validator.isNotEmpty(all.getIdASL()))
            {
              errors.add("idASL", new ValidationError((String) AnagErrors.get("ERR_ALLEVAMENTO_ASL_OBBLIGATORIA")));
            }
            // Il codice azienda zootecnico è obbligatorio
            if (!Validator.isNotEmpty(all.getCodiceAziendaZootecnica()))
            {
              errors.add("codiceAziendaZootecnica", new ValidationError((String) AnagErrors.get("ERR_CODICE_AZIENDA_ZOOTECNICO_OBBLIGATORIO")));
            }
          }
        }
      }

      if (errors.size() > 0)
      {
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(allevamentiNewUrl).forward(request, response);
        return;
      }
      

      if (errors.size() == 0)
      {
        anagClient.insertAllevamento(all, ruoloUtenza.getIdUtente());
        //rimuovo l'attributo delle sottocategie degli allevamenti 
        //perchè non serve più
        session.removeAttribute("sottoCategorieAllevamenti");
        session.removeAttribute("stabulazioniTrattamenti");
        response.sendRedirect("../layout/allevamenti.htm");
        return;
      }
    }
    else
    {
      if (request.getParameter("annulla") != null)
      {
        response.sendRedirect("../layout/allevamenti.htm");
        return;
      }
      else
      {
        request.setAttribute("operazione", operazione);
        if (operazione != null && !"".equals(operazione))
        {
          ValidationErrors errors = new ValidationErrors();
          if ("inserisciConsistenza".equals(operazione))
          {
            //l'utente vuole aggiungere una consistenza
            String idSpecie = request.getParameter("idSpecie");
            String idCategoria = request.getParameter("idCategoria");
            String idSottoCategoria = request.getParameter("idSottoCategoria");
            SottoCategoriaAllevamento sottoCategoria = new SottoCategoriaAllevamento();
            errors = sottoCategoria.validateInsConsistenzaZootec(errors, idSpecie, idCategoria, idSottoCategoria);
            if (errors.size() > 0)
            {
              request.setAttribute("errors", errors);
            }
            else
            {
              //posso inserire in record sul vettore
              if (sottoCategorieAllevamenti == null)
                sottoCategorieAllevamenti = new Vector<SottoCategoriaAllevamento>();

              //devo controlalre che non sia già stata inserita una sottocategoria
              //uguale a questa
              int size = sottoCategorieAllevamenti.size();
              boolean trovato = false;
              int i = 0;
              long idSottoCategorial = Long.parseLong(idSottoCategoria);
              long idCategorial = Long.parseLong(idCategoria);
              while (i < size && !trovato)
              {
                if (idSottoCategorial == sottoCategorieAllevamenti.get(i).getIdSottoCategoriaAnimale())
                  trovato = true;
                i++;
              }

              if (trovato)
              {
                errors.add("idSottoCategoria", new ValidationError(AnagErrors.ERR_SOTTO_CATEGORIA_UNIVOCA));
                request.setAttribute("errors", errors);
              }
              else
              {
                //memorizzo le descrizioni della categoria scelta e della sottocategoria
                TipoSottoCategoriaAnimale sottoCategoriaAnimale = clientGaa.getTipoSottoCategoriaAnimale(idSottoCategorial);
                
                
                if ("S".equals(flagStallaPascolo))
                {
                  sottoCategoria.setFlagStallaPascolo(true);
                  //Imposto i valori di default
                  sottoCategoria.setNumeroCicliAnnuali("1");
                  sottoCategoria.setCicli("365");
                }
                else
                  sottoCategoria.setFlagStallaPascolo(false);  
                sottoCategoria.setFlagSottocatFittizia(sottoCategoriaAnimale.isFlagSottocatFittizia());
                sottoCategoria.setDescSottoCategoriaAnimale(sottoCategoriaAnimale.getDescrizione());
                sottoCategoria.setPesoVivoMax(sottoCategoriaAnimale.getPesoVivoMax());
                sottoCategoria.setPesoVivoMin(sottoCategoriaAnimale.getPesoVivoMin());
                sottoCategoria.setGgDurataCiclo(sottoCategoriaAnimale.getGgDurataCiclo());
                sottoCategoria.setPesoVivoAzoto(sottoCategoriaAnimale.getPesoVivoAzoto());
                sottoCategoria.setPesoVivo(StringUtils.parsePesoCapiMod("" + sottoCategoriaAnimale.getPesoVivoMedio()));
                sottoCategoria.setDescCategoriaAnimale(request.getParameter("descCategoria"));
                sottoCategoria.setIdSottoCategoriaAnimale(idSottoCategorial);
                sottoCategoria.setIdCategoriaAnimale(idCategorial);
                sottoCategoria.setPesoVivoAzoto(sottoCategoriaAnimale.getPesoVivoAzoto());
                sottoCategorieAllevamenti.add(sottoCategoria);
              }
            }
          }
          if ("cancellaConsistenza".equals(operazione))
          {
            //l'utente vuole cancellare una o più consistenza
            String idCategorieTab[]=request.getParameterValues("idCategorieTab");
            if (idCategorieTab == null || idCategorieTab.length == 0)
            {
              errors.add("cancellaConsistenza", new ValidationError(AnagErrors.ERR_NO_CONSISTENZA_DELETE));
              request.setAttribute("errors", errors);
            }
            else
            {
              boolean probCanc=false;
              for (int i = idCategorieTab.length - 1; i >= 0; i--)
              {
                boolean delete=true;
                //Prima di rimuoverlo controllo che non venga usato dalla stabulazione
                //quindi recupero id della sotto categoria
                if (stabulazioniTrattamenti != null)
                {
                  String idCanc=""+((SottoCategoriaAllevamento)sottoCategorieAllevamenti.get(Integer.parseInt(idCategorieTab[i]))).getIdSottoCategoriaAnimale();
                
                  int size=stabulazioniTrattamenti.size();
                  for (int j=0;j<size;j++)
                  {
                    StabulazioneTrattamento stabulazione = stabulazioniTrattamenti.get(j);
                    if (idCanc.equals(stabulazione.getIdSottoCategoriaAnimale()))
                      delete=false;
                  }
                }
                if (delete) sottoCategorieAllevamenti.remove(Integer.parseInt(idCategorieTab[i]));
                else probCanc=true;;
              }
              if (probCanc)
              {
                errors.add("cancellaConsistenza", new ValidationError(AnagErrors.ERR_CONSISTENZA_DELETE_USED));
                request.setAttribute("errors", errors);
              }
            }
          }

          if ("inserisciStabulazione".equals(operazione))
          {
            //l'utente vuole aggiungere una stabulazione
            StabulazioneTrattamento stabulazione = new StabulazioneTrattamento();
            if (stabulazioniTrattamenti == null) stabulazioniTrattamenti = new Vector<StabulazioneTrattamento>();
            
            stabulazioniTrattamenti.add(stabulazione);
          }
          if ("cancellaStabulazione".equals(operazione))
          {
            //l'utente vuole cancellare una o più consistenza
            String idStabulazioneTab[]=request.getParameterValues("idStabulazioneTab");
            if (idStabulazioneTab == null || idStabulazioneTab.length == 0)
            {
              errors.add("cancellaStabulazione", new ValidationError(AnagErrors.ERR_NO_CONSISTENZA_DELETE));
              request.setAttribute("errors", errors);
            }
            else
            {
              for (int i = idStabulazioneTab.length - 1; i >= 0; i--)
                stabulazioniTrattamenti.remove(Integer.parseInt(idStabulazioneTab[i]));
            }
          }
          
          if ("calcolaStabulazione".equals(operazione))
          {
            AllevamentoAnagVO allTmp = new AllevamentoAnagVO();
            allTmp.setCategorieAllevamentoVector(sottoCategorieAllevamenti);
            allTmp.setStabulazioniTrattamenti(stabulazioniTrattamenti);
            allTmp.setCodiceFiscaleDetentore(request.getParameter("codiceFiscaleDetentore"));
            allTmp.setCodiceFiscaleProprietario(request.getParameter("codiceFiscaleProprietario"));
            
            //Verifico se detentore e/o proprietario
	          boolean isProprietario = false;
	          boolean isDetentore = false;
	          if(Validator.isNotEmpty(allTmp.getCodiceFiscaleProprietario()))
	          {
	            for(int j=0;j<vCuaaPossProp.size();j++)
	            {
	              if(allTmp.getCodiceFiscaleProprietario().equalsIgnoreCase(vCuaaPossProp.get(j)))
	              {
	                isProprietario = true;
	                break;
	              }
	            }
	          }
	          if(Validator.isNotEmpty(allTmp.getCodiceFiscaleProprietario()))
	          {
	            for(int j=0;j<vCuaaPossDet.size();j++)
	            {
	              if(allTmp.getCodiceFiscaleProprietario().equalsIgnoreCase(vCuaaPossDet.get(j)))
	              {
	                isDetentore = true;
	                break;
	              }
	            }
	          }
	          
	          boolean ugualiDetProp = false;
	          if(isDetentore && isProprietario)
	            ugualiDetProp = true;
            
            
            
            errors = allTmp.validateCalcoli(errors, ugualiDetProp, isProprietario);
            if (errors.size() > 0)
            {
              request.setAttribute("errors", errors);
            }   
            else
            {
              //se non ci sono errori procedo con i calcoli
              int sizeStab = stabulazioniTrattamenti.size();
              for (int j = 0; j < sizeStab; j++)
              {
                StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
                //non ci sono più i trattamenti....
                //stab.setFlagCalcolo(false);
                
                long idSottoCategoriaAnimale=Long.parseLong(stab.getIdSottoCategoriaAnimale());
                long idStabulazione=Long.parseLong(stab.getIdStabulazione());
                stab.setStabPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,true, idStabulazione));
                stab.setStabNonPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,false, idStabulazione));
                
                
                stab.effettuaCalcoli();
                //Totale azoto
                stab.effettuaCalcoliEfflPreTratt();
                
                stab.impostaValoriDefault();
              }
            }
          }       
        }
        else
        {
          //Se sono arrivato qua vuol dire che è la 
          //prima volta che entro quindi ripulisco gli oggetti in sessione
          session.removeAttribute("sottoCategorieAllevamenti");
          session.removeAttribute("stabulazioniTrattamenti");
        }
      }
    }
    session.setAttribute("sottoCategorieAllevamenti", sottoCategorieAllevamenti);
    session.setAttribute("stabulazioniTrattamenti", stabulazioniTrattamenti); 
    
    
  }
  catch (SolmrException s)
  {
    ValidationErrors ve = new ValidationErrors();
    ve.add("error", new ValidationError(s.getMessage()));
    request.setAttribute("errors", ve);
    s.printStackTrace();
  }
  catch (Exception e)
  {
    it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
    ve.add("error", new it.csi.solmr.util.ValidationError((String) SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors", ve);
    e.printStackTrace();
  }
%>
<jsp:forward page="../view/allevamentiNewView.jsp" />
