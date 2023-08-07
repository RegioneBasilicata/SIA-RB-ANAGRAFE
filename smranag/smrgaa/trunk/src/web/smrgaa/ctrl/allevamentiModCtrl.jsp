<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  String iridePageName = "allevamentiModCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%
  String elencoAllevamentiUrl = "/layout/allevamenti.htm";
  String allevamentiModUrl = "/view/allevamentiModView.jsp";

  try
  {
    AnagFacadeClient anagClient = new AnagFacadeClient();
    GaaFacadeClient clientGaa = GaaFacadeClient.getInstance();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");
    String operazione=request.getParameter("operazioneModAll");
    String flagStallaPascolo = request.getParameter("flagStallaPascolo");
    
    
    
    Date parametroAllSuGnps = (Date)anagClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ALL_SU_GNPS);
    request.setAttribute("parametroAllSuGnps", parametroAllSuGnps);
    
    
    
    
    
    if (request.getParameter("conferma") != null || Validator.isNotEmpty(operazione))
    {
      //Indipendentemente dal motivo per cui sono qui devo andare a vedere
      //se ci sono righe nella consistenza e memorizzarle in session sottoCategorieAllevamenti
      Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = (Vector<SottoCategoriaAllevamento>) session.getAttribute("sottoCategorieAllevamenti");
      if (sottoCategorieAllevamenti != null)
      {
        String capiTab[]=request.getParameterValues("capiTab");
        String capiProprietaTab[]=request.getParameterValues("capiProprietaTab");
        String pesoVivoTab[]=request.getParameterValues("pesoVivoTab");
        
        String numeroCicliAnnualiTab[]=request.getParameterValues("numeroCicliAnnualiTab");
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
            SottoCategoriaAllevamento sottoCategoria=(SottoCategoriaAllevamento)sottoCategorieAllevamenti.get(i);
            sottoCategoria.setNumeroCicliAnnuali(numeroCicliAnnualiTab[i]);
            sottoCategoria.setQuantita(capiTab[i]);
            sottoCategoria.setQuantitaProprieta(capiProprietaTab[i]);
            sottoCategoria.setPesoVivo(pesoVivoTab[i]);
            
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
            StabulazioneTrattamento stabulazione = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
            stabulazione.setIdSottoCategoriaAnimale(idCatSottocatStab[i]);
            stabulazione.setIdStabulazione(idStabulazione[i]);
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
            stabulazione.setQuantita(quantitaStab[i]);
            stabulazione.setPalabile(palabile[i]);
            stabulazione.setNonPalabile(nonPalabile[i]);
            
            stabulazione.setTipoTrattamento(null);
            stabulazione.setFlagCalcolo(true);
          }
        }
      }
      
      //recupero i dati dalla sessione e vado a sovrascriverli con quelli aggiornati
      AllevamentoAnagVO all = (AllevamentoAnagVO)session.getAttribute("allevamentoMod");
      
      Vector<TipoDestinoAcquaLavaggio> vTipoDestinoLavaggio = clientGaa.getElencoDestAcquaLavaggio();
      Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio = null;
      if(Validator.isNotEmpty(vTipoDestinoLavaggio))
      {
        for(int i=0;i<vTipoDestinoLavaggio.size();i++)
        {
          if(Validator.isNotEmpty(request.getParameter("idDestinoAcquaLavaggio_"+i)))
          {
            if(vAllevamentoAcquaLavaggio == null)
            {
              vAllevamentoAcquaLavaggio = new Vector<AllevamentoAcquaLavaggio>();
            }
            
            AllevamentoAcquaLavaggio allevamentoAcquaLavaggio = new AllevamentoAcquaLavaggio();
            
            allevamentoAcquaLavaggio.setQuantitaAcquaLavaggioStr(request.getParameter("destQuantitaAcquaLavaggio_"+i));
            allevamentoAcquaLavaggio.setIdDestinoAcquaLavaggio(new Long(request.getParameter("idDestinoAcquaLavaggio_"+i)));
          
            vAllevamentoAcquaLavaggio.add(allevamentoAcquaLavaggio);
          
          }        
        }
        
      }
      session.setAttribute("vAllevamentoAcquaLavaggio",vAllevamentoAcquaLavaggio);
      
      
      // Recupero i parametri
      String indirizzo = request.getParameter("indirizzo");
      String cap = request.getParameter("cap");
      String telefono = request.getParameter("telefono");
      String descrizionComune = request.getParameter("descResComune");
  
      
      all.setDenominazione(request.getParameter("denominazioneAllevamento"));
      all.setDataInizioAttivita(request.getParameter("dataInizioAttivita"));
      all.setLatitudineStr(request.getParameter("latitudine"));
      all.setLongitudineStr(request.getParameter("longitudine"));
      all.setLongitudineStr(request.getParameter("longitudine"));
  
      all.setIdAllevamento(request.getParameter("idAllevamento"));
  
      all.setIdUTE(request.getParameter("idUTE"));
      all.setIdASL(request.getParameter("idASL"));
  
      all.setCodiceFiscaleProprietario(request.getParameter("codiceFiscaleProprietario"));
      all.setDenominazioneProprietario(request.getParameter("denominazioneProprietario"));
      all.setCodiceFiscaleDetentore(request.getParameter("codiceFiscaleDetentore"));
      all.setDenominazioneDetentore(request.getParameter("denominazioneDetentore"));
      all.setDataInizioDetenzione(request.getParameter("dataInizioDetenzione"));
      all.setDataFineDetenzione(request.getParameter("dataFineDetenzione"));
  
      if (SolmrConstants.FLAG_S.equals(request.getParameter("soccida")))
        all.setFlagSoccida(true);
      else
        all.setFlagSoccida(false);
        
      all.setMotivoSoccida(request.getParameter("motivoSoccida"));
  		all.setFlagAssicuratoCosman(request.getParameter("assicuratoSoccida"));
     
  
      all.setIdOrientamentoProduttivo(request.getParameter("idOrientamentoProduttivo"));
      all.setIdTipoProduzione(request.getParameter("idTipoProduzione"));
      all.setIdTipoProduzioneCosman(request.getParameter("idTipoProduzioneCosman"));
      all.setIdMungitura(request.getParameter("idMungitura"));
      all.setQuantitaAcquaLavaggio(request.getParameter("quantitaAcquaLavaggio"));
      all.setFlagDeiezioneAvicoli(request.getParameter("flagDeiezioneAvicoli"));
      all.setMediaCapiLattazione(request.getParameter("mediaCapiLattazione"));
      
      all.setSuperficieLettieraPermanente(request.getParameter("superficieLettieraPermanente"));
      all.setAltezzaLettieraPermanente(request.getParameter("altezza"));
  
      // Setto i parametri nel VO
      all.setIndirizzo(indirizzo);
      
      ComuneVO comune=new ComuneVO();
        
      all.setCodiceProvinciaAllevamento(request.getParameter("resProvincia").toUpperCase());
      comune.setDescom(descrizionComune);
      comune.setSiglaProv(all.getCodiceProvinciaAllevamento());
      all.setComuneVO(comune);
      
      all.setCap(cap);
      all.setTelefono(telefono);
      all.setIdSpecieAnimale(request.getParameter("idSpecie"));
      all.setCodiceAziendaZootecnica(request.getParameter("codiceAziendaZootecnica").toUpperCase());
      all.setNote(request.getParameter("note"));
  
      if (request.getParameter("conferma") == null)
      {
        ValidationErrors errors = new ValidationErrors();
        if ("inserisciConsistenza".equals(operazione))
        {
          //l'utente vuole aggiungere una consistenza
          String idSpecie = all.getIdSpecieAnimale();
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
              if (idSottoCategorial == ((SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(i)).getIdSottoCategoriaAnimale())
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
              TipoCategoriaAnimaleAnagVO categoriaAnimale = anagClient.getTipoCategoriaAnimale(sottoCategoriaAnimale.getIdCategoriaAnimale());
              
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
              sottoCategoria.setLattazione(categoriaAnimale.getLattazione());
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
                  StabulazioneTrattamento stabulazione = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
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
          AllevamentoAnagVO allTemp = new AllevamentoAnagVO();
          allTemp.setCodiceFiscaleDetentore(all.getCodiceFiscaleDetentore());
          allTemp.setCodiceFiscaleProprietario(all.getCodiceFiscaleProprietario());
          allTemp.setCategorieAllevamentoVector(sottoCategorieAllevamenti);
          allTemp.setStabulazioniTrattamenti(stabulazioniTrattamenti);
          
          //Verifico se detentore e/o proprietario
          Vector<String> vCuaaPossDet = (Vector<String>)session.getAttribute("vCuaaPossDet");
          Vector<String> vCuaaPossProp = (Vector<String>)session.getAttribute("vCuaaPossProp");
          String codFiscPropTmp = request.getParameter("codiceFiscaleProprietario");
          String codFiscDetTmp = request.getParameter("codiceFiscaleDetentore");
          
          boolean isProprietario = false;
          boolean isDetentore = false;
          //boolean isProprietarioCat = false;
          
          
          if(Validator.isNotEmpty(codFiscPropTmp))
          {
            for(int j=0;j<vCuaaPossProp.size();j++)
            {
              if(codFiscPropTmp.equalsIgnoreCase(vCuaaPossProp.get(j)))
              {
                isProprietario = true;
                //isProprietarioCat = true;
                break;
              }
            }
          }
          if(Validator.isNotEmpty(codFiscDetTmp))
          {
            for(int j=0;j<vCuaaPossDet.size();j++)
            {
              if(codFiscDetTmp.equalsIgnoreCase(vCuaaPossDet.get(j)))
              {
                isDetentore = true;
                break;
              }
            }
          }
          //controllo se posso considerare uguali detentore e proprietario 
          boolean ugualiDetProp = false;
            if(isDetentore && isProprietario)
              ugualiDetProp = true;
           
          //Controllo con anche i dati bdn se è proprietario,
          //se nn è cosi' deve essere detentore altrimenti nn avrebbe senso... 
          /*Vector<String> vCuaaPossPropBdn = (Vector<String>)session.getAttribute("vCuaaPossPropBdn");
          if(Validator.isNotEmpty(codFiscPropTmp))
          {
            for(int j=0;j<vCuaaPossPropBdn.size();j++)
            {
              if(codFiscPropTmp.equalsIgnoreCase(vCuaaPossPropBdn.get(j)))
              {
                isProprietario = true;
                break;
              }
            }
          }*/
          
          errors = allTemp.validateCalcoli(errors, ugualiDetProp, isProprietario);
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
              long idSottoCategoriaAnimale=Long.parseLong(stab.getIdSottoCategoriaAnimale());
              long idStabulazione=Long.parseLong(stab.getIdStabulazione());
              stab.setStabPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,true, idStabulazione));
              stab.setStabNonPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,false, idStabulazione));
              stab.effettuaCalcoli();
              //Totali azoto
              stab.effettuaCalcoliEfflPreTratt();
              
              stab.impostaValoriDefault();
            }
          }
        }       
          
        //Se non ho scelto di salvare la pagina e non sto inserendo o cancellando la consistenza
        //devo ricaricare la pagina con i valori modificati
        session.setAttribute("allevamentoMod", all);
        %>
          <jsp:forward page="../view/allevamentiModView.jsp" />
        <%
        return;
      }
      else
      {
        ValidationErrors errors = new ValidationErrors();
        String istatComune = null;
        if (Validator.isNotEmpty(descrizionComune) && Validator.isNotEmpty(all.getCodiceProvinciaAllevamento()))
        {
          try
          {
            istatComune = anagClient.ricercaCodiceComuneNonEstinto(descrizionComune.toUpperCase(), all.getCodiceProvinciaAllevamento().toUpperCase());
          }
          catch (SolmrException se)
          {
            ValidationError error = new ValidationError(se.getMessage());
            errors.add("descComune", error);
          }
        }
        all.setIstatComuneAllevamento(istatComune);
          
        //memorizzo le categorie di allevamento inserite per poterle salvare
        all.setCategorieAllevamentoVector(sottoCategorieAllevamenti);
        
        all.setStabulazioniTrattamenti(stabulazioniTrattamenti);
        
        all.setvAllevamentoAcquaLavaggio(vAllevamentoAcquaLavaggio);
      
        if (stabulazioniTrattamenti!=null)
        {
          //Preparo dei dati necessari ai calcoli
          int sizeStab = stabulazioniTrattamenti.size();
          for (int j = 0; j < sizeStab; j++)
          {
            try
            {
              StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
          
              long idSottoCategoriaAnimale=Long.parseLong(stab.getIdSottoCategoriaAnimale());
              long idStabulazione=Long.parseLong(stab.getIdStabulazione());
              stab.setStabPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,true, idStabulazione));
              stab.setStabNonPal(clientGaa.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,false, idStabulazione));
            }
            catch(Exception e){}
          }
        }
        
        
        //Verifico se detentore e/o proprietario
        Vector<String> vCuaaPossDet = (Vector<String>)session.getAttribute("vCuaaPossDet");
        Vector<String> vCuaaPossProp = (Vector<String>)session.getAttribute("vCuaaPossProp");
        String codFiscPropTmp = request.getParameter("codiceFiscaleProprietario");
        String codFiscDetTmp = request.getParameter("codiceFiscaleDetentore");
        boolean isProprietario = false;
        //usato solo nei conrolli delle categorie
        boolean isProprietarioCat = false;
        boolean isDetentore = false;
        if(Validator.isNotEmpty(codFiscPropTmp))
        {
          for(int j=0;j<vCuaaPossProp.size();j++)
          {
            if(codFiscPropTmp.equalsIgnoreCase(vCuaaPossProp.get(j)))
            {
              isProprietario = true;
              isProprietarioCat = true;
              break;
            }
          }
        }
        if(Validator.isNotEmpty(codFiscDetTmp))
        {
          for(int j=0;j<vCuaaPossDet.size();j++)
          {
            if(codFiscDetTmp.equalsIgnoreCase(vCuaaPossDet.get(j)))
            {
              isDetentore = true;
              break;
            }
          }
        }
        
        //controllo se posso considerare uguali detentore e proprietario 
        boolean ugualiDetProp = false;
          if(isDetentore && isProprietario)
            ugualiDetProp = true;
         
        //li faccio solo per specie censite su bdn
        if (!all.getTipoSpecieAnimaleAnagVO().isFlagMofCodAzZoot())
        {
	        //Controllo con anche i dati bdn se è proprietario,
	        //se nn è cosi' deve essere detentore altrimenti nn avrebbe senso... 
	        isProprietario = false;
	        Vector<String> vCuaaPossPropBdn = (Vector<String>)session.getAttribute("vCuaaPossPropBdn");
	        if(Validator.isNotEmpty(codFiscPropTmp))
	        {
	          for(int j=0;j<vCuaaPossPropBdn.size();j++)
	          {
	            if(codFiscPropTmp.equalsIgnoreCase(vCuaaPossPropBdn.get(j)))
	            {
	              isProprietario = true;
	              break;
	            }
	          }
	        }
	        
	        //Controllo con anche i dati bdn se è proprietario,
	        //se nn è cosi' deve essere detentore altrimenti nn avrebbe senso... 
	        isDetentore = false;
	        Vector<String> vCuaaPossDetBdn = (Vector<String>)session.getAttribute("vCuaaPossDetBdn");
	        if(Validator.isNotEmpty(codFiscDetTmp))
	        {
	          for(int j=0;j<vCuaaPossDetBdn.size();j++)
	          {
	            if(codFiscDetTmp.equalsIgnoreCase(vCuaaPossDetBdn.get(j)))
	            {
	              isDetentore = true;
	              break;
	            }
	          }
	        }
	        
	      }
        
  
        all.validateUpdate(errors, ugualiDetProp, isProprietarioCat, isProprietario, isDetentore);
        
  
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
              request.getRequestDispatcher(allevamentiModUrl).forward(request, response);
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
  
        // Metto l'oggetto modificato in request
        session.setAttribute("allevamentoMod", all);
  
        if (errors.size() > 0)
        {
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(allevamentiModUrl).forward(request, response);
          return;
        }
  
        if (errors.size() == 0)
        {
          /**
           * Rimuovo i dati che ho salvato in session
           */
          session.removeAttribute("common");
          session.removeAttribute("allevamentoMod");
          session.removeAttribute("sottoCategorieAllevamenti");
          session.removeAttribute("stabulazioniTrattamenti");
          session.removeAttribute("vCuaaPossDet");
          session.removeAttribute("vCuaaPossProp");
          session.removeAttribute("vCuaaPossDetBdn");
          session.removeAttribute("vCuaaPossPropBdn");
  
          
  
          try
          {
            anagClient.updateAllevamento(all, ruoloUtenza.getIdUtente());
          }
          catch (SolmrException se)
          {
            if (se.getMessage().startsWith("Non è possibile modificare contemporaneamente la data di fine") || se.getMessage().startsWith("Impossibile inserire una data di fine validità"))
            {
              ValidationErrors err = new ValidationErrors();
              err.add("al", new ValidationError(se.getMessage()));
              request.setAttribute("errors", err);
              %>
                <jsp:forward page="../view/allevamentiModView.jsp" />
              <%
              return;
            }
            else
            {
              ValidationErrors err = new ValidationErrors();
              err.add("error", new ValidationError(se.getMessage()));
              request.setAttribute("errors", err);
              %>
                <jsp:forward page="../ctrl/allevamentiCtrl.jsp" />
              <%
              return;
            }
          }
          request.removeAttribute("errors");
          %>
            <jsp:forward page="<%=elencoAllevamentiUrl%>" />
          <%
          return;
        }
        else
        {
          request.setAttribute("errors", errors);
        }
      }
    }
    else if (request.getParameter("annulla") != null)
    {
      /**
       * Rimuovo i dati che ho salvato in session
       */
      session.removeAttribute("common");
      session.removeAttribute("allevamentoMod");
      session.removeAttribute("sottoCategorieAllevamenti");
      session.removeAttribute("stabulazioniTrattamenti");
      session.removeAttribute("vCuaaPossDet");
      session.removeAttribute("vCuaaPossProp");
      session.removeAttribute("vCuaaPossDetBdn");
      session.removeAttribute("vCuaaPossPropBdn");
      
      %>
        <jsp:forward page="<%=elencoAllevamentiUrl%>" />
      <%
      return;
    }
    else
    {
      //Rimuovo gli eventuali attributi delle precedenti modifiche dalla sessione
      session.removeAttribute("common");
      session.removeAttribute("allevamentoMod");
      session.removeAttribute("sottoCategorieAllevamenti");
      session.removeAttribute("stabulazioniTrattamenti");
      session.removeAttribute("vAllevamentoAcquaLavaggio");
      session.removeAttribute("vCuaaPossDet");
      session.removeAttribute("vCuaaPossProp");
      session.removeAttribute("vCuaaPossDetBdn");
      session.removeAttribute("vCuaaPossPropBdn");
      
      Long idAllevamento = null;
      if (request.getParameter("radiobutton") != null && !"".equals(request.getParameter("radiobutton")))
      {
        idAllevamento = new Long((String) request.getParameter("radiobutton"));
      }
      if (idAllevamento != null)
      {
        AllevamentoAnagVO all = anagClient.getAllevamento(idAllevamento);
        
        //Imposto i seguenti campi per poterlivi sualizzare correttamente
        //come cifre decimali
        if (Validator.isNotEmpty(all.getSuperficieLettieraPermanente()))
          all.setSuperficieLettieraPermanente(StringUtils.parseSuperficieField(all.getSuperficieLettieraPermanente()));
          
        if (Validator.isNotEmpty(all.getAltezzaLettieraPermanente()))
          all.setAltezzaLettieraPermanente(StringUtils.parseDoubleFieldTwoDecimal(all.getAltezzaLettieraPermanente()));  
          
          
        if (Validator.isNotEmpty(all.getQuantitaAcquaLavaggio()))
          all.setQuantitaAcquaLavaggio(StringUtils.parsePesoCapiMod(all.getQuantitaAcquaLavaggio())); 
           
          
  
        //L'utente sceglie un allevamento la cui specie non è più valida
        //al'interno del sistema (db_tipo_specie.Data_fine_validita valorizzato)
        if (all.getTipoSpecieAnimaleAnagVO() != null && all.getTipoSpecieAnimaleAnagVO().getDataFineValidita() != null)
        {
          //Il sistema visualizza il messaggio \u201CImpossibile modificare
          //l\u2019allevamento selezionato in quanto la specie non è più valida\u201D
          ValidationErrors err = new ValidationErrors();
          err.add("error", new ValidationError(AnagErrors.ERRORE_ALLEVAMENTI_SPECIE_NON_VALIDA));
          request.setAttribute("errors", err);
          %>
            <jsp:forward page="../ctrl/allevamentiCtrl.jsp" />
          <%
          return;
        }
        //Vado a ricercarmi a idati del comune e li memorizzo nell'allevamento
        ComuneVO comune = anagClient.getComuneByISTAT(all.getIstatComuneAllevamento());
        all.setComuneVO(comune);
        
        //leggo le sottocategorie e le metto in sessione
        Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = clientGaa.getTipiSottoCategoriaAllevamento(all.getIdAllevamentoLong().longValue());
        session.setAttribute("sottoCategorieAllevamenti",sottoCategorieAllevamenti);
        
        //leggo le stabulazioni e le metto in sessione
        //le faccio vedere solo per il vecchio...
        if(parametroAllSuGnps.after(new Date()))
        {
	        Vector<StabulazioneTrattamento> stabulazioniTrattamenti=clientGaa.getStabulazioni(all.getIdAllevamentoLong().longValue(),true);
	        session.setAttribute("stabulazioniTrattamenti",stabulazioniTrattamenti);
	      }
        
        Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio = clientGaa.getElencoAllevamentoAcquaLavaggio(all.getIdAllevamentoLong().longValue());
        session.setAttribute("vAllevamentoAcquaLavaggio", vAllevamentoAcquaLavaggio);
        
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
        Vector<String> vCuaaPossDetBdn = new Vector<String>();
        Vector<String> vCuaaPossPropBdn = new Vector<String>();
        vCuaaPossDet.add(anagAziendaVO.getCUAA());
        vCuaaPossProp.add(anagAziendaVO.getCUAA());
        vCuaaPossDetBdn.add(anagAziendaVO.getCUAA());
        vCuaaPossPropBdn.add(anagAziendaVO.getCUAA());
        
        for(int j=0;j<v_soggetti.size();j++)
        {
          if(vIdRuolo.contains(v_soggetti.get(j).getIdRuolo()))
          {
            if(!vCuaaPossDet.contains(v_soggetti.get(j).getCodiceFiscale()))
            {
              vCuaaPossDet.add(v_soggetti.get(j).getCodiceFiscale());
              vCuaaPossDetBdn.add(v_soggetti.get(j).getCodiceFiscale());
            }
              
            if(!vCuaaPossProp.contains(v_soggetti.get(j).getCodiceFiscale()))
            {
              vCuaaPossProp.add(v_soggetti.get(j).getCodiceFiscale());
              vCuaaPossPropBdn.add(v_soggetti.get(j).getCodiceFiscale());
            }
          }          
        }
        
        session.setAttribute("vCuaaPossDet", vCuaaPossDet);
        session.setAttribute("vCuaaPossProp", vCuaaPossProp);
        
        //devo aggiungere i cuaa BDN
        if (!all.getTipoSpecieAnimaleAnagVO().isFlagMofCodAzZoot())
        {
          Vector<AllevamentoVO> vAllevamentiSian = clientGaa.getElencoAllevamentiSian(
            anagAziendaVO.getCUAA(), all.getIdSpecieAnimaleLong(), all.getCodiceAziendaZootecnica());
          if(Validator.isNotEmpty(vAllevamentiSian))
          {
            for(int j=0;j<vAllevamentiSian.size();j++)
            {
              if(!vCuaaPossDetBdn.contains(vAllevamentiSian.get(j).getCodFiscDetentore()))
                vCuaaPossDetBdn.add(vAllevamentiSian.get(j).getCodFiscDetentore());
              if(!vCuaaPossPropBdn.contains(vAllevamentiSian.get(j).getCodFiscProprietario()))
                vCuaaPossPropBdn.add(vAllevamentiSian.get(j).getCodFiscProprietario());
            }
          }
        }
        
        session.setAttribute("vCuaaPossDetBdn", vCuaaPossDetBdn);
        session.setAttribute("vCuaaPossPropBdn", vCuaaPossPropBdn);
        
        
        
        session.setAttribute("allevamentoMod", all);
        if (all.getDataFine() != null)
        {
          ValidationErrors err = new ValidationErrors();
          err.add("error", new ValidationError("Allevamento storicizzato. Impossibile procedere."));
          request.setAttribute("errors", err);
          %>
            <jsp:forward page="../ctrl/allevamentiCtrl.jsp" />
          <%
          return;
        }
        else
        {
          /* Memorizzo in sessione l'allevamento
             per confrontare quello che è stato modificato prima di fare l'update
           */
          session.setAttribute("common", all);
        }
      }
    }
  }
  catch (SolmrException s)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    ValidationErrors ve = new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error", new ValidationError(s.getMessage()));
    request.setAttribute("errors", ve);
    s.printStackTrace();
  }
  catch (Exception e)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error", new it.csi.solmr.util.ValidationError((String) SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors", ve);
    e.printStackTrace();
  }
  %>
    <jsp:forward page="../view/allevamentiModView.jsp" />
