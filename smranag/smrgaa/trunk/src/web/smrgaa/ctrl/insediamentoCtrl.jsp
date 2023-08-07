<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  public static String INSEDIAMENTO_URL="/view/insediamentoView.jsp";
  public static String DICHIARAZIONE_INSEDIAMENTO_URL="../layout/dichiarazioneInsediamento.htm";
%>

<%

  String iridePageName = "dichiarazioneInsediamentoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String url=INSEDIAMENTO_URL;
  try
  {
    AnagFacadeClient client = new AnagFacadeClient();
    ValidationErrors errors = new ValidationErrors();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

    if("reload".equals(request.getParameter("operazione")) || (request.getParameter("avanti") != null))
    {
      //la pagina deve essere ricaricata oppure i dati salvati
      AnagAziendaVO modAnagAziendaVO=(AnagAziendaVO)session.getAttribute("modAnagAziendaVO");

      //Recupero dalal pagina i dati del'azienda
      String provinciaCompetenza = request.getParameter("provincePiemonte");
      modAnagAziendaVO.setCUAA(request.getParameter("cuaa"));
      modAnagAziendaVO.setDenominazione(request.getParameter("denominazione"));
      modAnagAziendaVO.setPartitaIVA(request.getParameter("partitaIVA"));
      modAnagAziendaVO.setProvCompetenza(provinciaCompetenza);
      modAnagAziendaVO.setProvincePiemonte(provinciaCompetenza);

      Integer idTipoAzienda = null;
      if(request.getParameter("tipiAzienda") != null && !request.getParameter("tipiAzienda").equals(""))
        idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
      if(idTipoAzienda != null)
      {
        modAnagAziendaVO.setTipiAzienda(String.valueOf(idTipoAzienda));
        String tipiAzienda = client.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
        modAnagAziendaVO.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
      }
      else
      {
        modAnagAziendaVO.setTipiAzienda(null);
        modAnagAziendaVO.setTipoTipologiaAzienda(null);
      }
      modAnagAziendaVO.setTipiFormaGiuridica(null);
      modAnagAziendaVO.setTipoFormaGiuridica(null);

      if(request.getParameter("CCIAAprovREA") != null)
        modAnagAziendaVO.setCCIAAprovREA(request.getParameter("CCIAAprovREA").toUpperCase());
      else  modAnagAziendaVO.setCCIAAprovREA("");
      modAnagAziendaVO.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));
      modAnagAziendaVO.setCCIAAnumRegImprese(request.getParameter("CCIAAnumRegImprese"));
      modAnagAziendaVO.setCCIAAannoIscrizione(request.getParameter("CCIAAannoIscrizione"));
      modAnagAziendaVO.setNote(request.getParameter("note"));

      //Recupero dalal pagina i dati della sede legale
      modAnagAziendaVO.setSedelegIndirizzo(request.getParameter("sedelegIndirizzo"));
      modAnagAziendaVO.setSedelegProv(request.getParameter("sedelegProv"));
      modAnagAziendaVO.setDescComune(request.getParameter("descComune"));
      modAnagAziendaVO.setSedelegCAP(request.getParameter("sedelegCAP"));
      String sedeLegaleStatoEstero = request.getParameter("sedelegEstero");
      modAnagAziendaVO.setSedelegEstero(sedeLegaleStatoEstero);
      modAnagAziendaVO.setStatoEstero(sedeLegaleStatoEstero);
      modAnagAziendaVO.setSedelegCittaEstero(request.getParameter("sedelegCittaEstero"));

      PersonaFisicaVO modPersonaVO = (PersonaFisicaVO)session.getAttribute("modPersonaVO");

      //Recupero dalal pagina i dati del rappresentante legale
      modPersonaVO.setCodiceFiscale(request.getParameter("codiceFiscale"));
      modPersonaVO.setCognome(request.getParameter("cognome"));
      modPersonaVO.setNome(request.getParameter("nome"));
      modPersonaVO.setSesso(request.getParameter("sesso"));

      String dataNascita = request.getParameter("strNascitaData");
      modPersonaVO.setStrNascitaData(dataNascita);

      modPersonaVO.setNascitaProv(request.getParameter("nascitaProv"));
      String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
      modPersonaVO.setDescNascitaComune(descrizioneLuogoNascita);
      String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
      String cittaEsteroNascita = request.getParameter("cittaNascita");
      modPersonaVO.setNascitaStatoEstero(statoEsteroNascita);
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals(""))
        modPersonaVO.setLuogoNascita(descrizioneLuogoNascita);
      else modPersonaVO.setLuogoNascita(statoEsteroNascita);
      modPersonaVO.setCittaNascita(request.getParameter("cittaNascita"));
      modPersonaVO.setResIndirizzo(request.getParameter("resIndirizzo"));
      modPersonaVO.setDescResProvincia(request.getParameter("resProvincia"));
      modPersonaVO.setResProvincia(request.getParameter("resProvincia"));
      modPersonaVO.setDescResComune(request.getParameter("descResComune"));
      modPersonaVO.setResCAP(request.getParameter("resCAP"));
      String statoEsteroResidenza = request.getParameter("statoEsteroRes");
      String cittaEsteroResidenza = request.getParameter("cittaResidenza");
      modPersonaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
      modPersonaVO.setStatoEsteroRes(statoEsteroResidenza);
      modPersonaVO.setNascitaCittaEstero(cittaEsteroNascita);
      modPersonaVO.setCittaResidenza(cittaEsteroResidenza);
      modPersonaVO.setResCittaEstero(cittaEsteroResidenza);
      modPersonaVO.setResTelefono(request.getParameter("resTelefono"));
      modPersonaVO.setResFax(request.getParameter("resFax"));
      modPersonaVO.setResMail(request.getParameter("resMail"));

      try
      {
        if(Validator.isDate(dataNascita))
          modPersonaVO.setNascitaData(DateUtils.parseDate(dataNascita));
        else  modPersonaVO.setNascitaData(null);
      }
      catch(Exception e)
      {
        modPersonaVO.setNascitaData(null);
      }

      if("reload".equals(request.getParameter("operazione")))
      {
        //devo ricaricare la pagina
        session.setAttribute("modPersonaVO",modPersonaVO);
        session.setAttribute("modAnagAziendaVO",modAnagAziendaVO);
        %><jsp:forward page="<%= url %>"/><%
        return;
      }

      if(request.getParameter("avanti") != null)
      {
        //E' stato premuto il pulsante avanti
        String tipiFormaGiuridica = request.getParameter("tipiFormaGiuridica");
        Integer idTipoFormaGiuridica = null;
        if(tipiFormaGiuridica != null && !tipiFormaGiuridica.equals(""))
          idTipoFormaGiuridica = Integer.decode(request.getParameter("tipiFormaGiuridica"));
        if(idTipoFormaGiuridica != null)
        {
          modAnagAziendaVO.setTipiFormaGiuridica(String.valueOf(idTipoFormaGiuridica));
          modAnagAziendaVO.setTipoFormaGiuridica(new CodeDescription(Integer.decode(tipiFormaGiuridica),""));
        }
        else
        {
          modAnagAziendaVO.setTipiFormaGiuridica(null);
          modAnagAziendaVO.setTipoFormaGiuridica(null);
        }

        String flagCCIAA = null;
        String flagPartitaIva = null;
        if(idTipoFormaGiuridica != null && !idTipoFormaGiuridica.equals(""))
        {
          try
          {
            flagCCIAA = client.getFormaGiuridicaFlagCCIAA(new Long(idTipoFormaGiuridica.longValue()));
            flagPartitaIva = client.getFlagPartitaIva(new Long(idTipoFormaGiuridica.longValue()));
          }
          catch(SolmrException se)
          {
            ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_FORMA_GIURIDICA"));
            errors.add("error", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(url).forward(request, response);
            return;
          }
        }
        modAnagAziendaVO.setFlagCCIAA(flagCCIAA);
        modAnagAziendaVO.setFlagPartitaIva(flagPartitaIva);

        //Valido i dati dell'azienda
        errors = modAnagAziendaVO.validateInsediamentoGiovani();

        errors = modPersonaVO.validateModificaNuovoRappresentanteLegale(errors);

        //Valido i dati della sede lelage
        errors = modAnagAziendaVO.validateSedeLegale(errors);


        // Controllo la validità della provincia REA
        if(modAnagAziendaVO.getCCIAAprovREA() != null && !modAnagAziendaVO.getCCIAAprovREA().equals(""))
        {
          try
          {
            boolean isValida = client.isProvinciaReaValida(modAnagAziendaVO.getCCIAAprovREA());
            if(!isValida)
            {
              ValidationError error = new ValidationError(""+AnagErrors.get("ERR_PROV_REA"));
              errors.add("CCIAAprovREA",error);
            }
          }
          catch(SolmrException se)
          {
            ValidationError error = new ValidationError(se.getMessage());
            errors.add("error",error);
            request.getRequestDispatcher(url).forward(request, response);
            return;
          }
        }

        if(request.getParameter("strCCIAAnumeroREA") != null && !request.getParameter("strCCIAAnumeroREA").equals(""))
        {
          try
          {
            modAnagAziendaVO.setCCIAAnumeroREA(Long.decode(request.getParameter("strCCIAAnumeroREA")));
          }
          catch(Exception ex)
          {
            ValidationError error = new ValidationError(""+AnagErrors.get("ERR_NUMERO_REA_ERRATO"));
            errors.add("strCCIAAnumeroREA", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(url).forward(request, response);
            return;
          }
        }
        else modAnagAziendaVO.setCCIAAnumeroREA(null);

        AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

        // Controllo che non esista già un'azienda attiva con il cuaa inserito
        if(anagAziendaVO.getCUAA()==null || !anagAziendaVO.getCUAA().equalsIgnoreCase(modAnagAziendaVO.getCUAA()))
        {
          if (client.isCUAAAlreadyPresentInsediate(modAnagAziendaVO.getCUAA().toUpperCase()))
          {
            ValidationError error = new ValidationError(""+AnagErrors.get("ERR_CUAA_INSEDIATO_DUP"));
            errors.add("cuaa", error);
          }
        }

        //Verificare che: se il CUAA, in origine, era un codice fiscale questo
        //può diventare una partita iva ma non può diventare un altro codice fiscale.
        //Se il CUAA, in origine, era una partita iva non applicare ulteriori vincoli
        if(anagAziendaVO.getCUAA()!=null && anagAziendaVO.getCUAA().length()==16 && !anagAziendaVO.getCUAA().equalsIgnoreCase(modAnagAziendaVO.getCUAA()))
        {
          if (modAnagAziendaVO.getCUAA().length()==16)
          {
            ValidationError error = new ValidationError(AnagErrors.ERR_CODFIS_SU_CODFIS);
            errors.add("cuaa", error);
          }
        }

       //Se il codice CUAA è un codice fiscale: Il  campo \u201Ccodice fiscale\u201D del
       //rappresentante legale  ed il campo CUAA devono essere uguali,
       //altrimenti visualizzare l\u2019errore \u201CIl codice fiscale del titolare
       //deve coincidere con il CUAA\u201D
       if(modAnagAziendaVO.getCUAA()!=null && modAnagAziendaVO.getCUAA().length()==16)
       {
         if (!modAnagAziendaVO.getCUAA().equalsIgnoreCase(modPersonaVO.getCodiceFiscale()))
         {
           ValidationError error = new ValidationError(AnagErrors.ERR_CODFISRL_SU_CUAA);
           errors.add("codiceFiscale", error);
         }
       }


        if(!modAnagAziendaVO.isFlagAziendaProvvisoria() && client.isFlagUnivocitaAzienda(idTipoAzienda))
        {
          // controllo che non esista un'azienda attiva diversa da quella che sto modificando
          // con la stessa partita iva
          try
          {
            if(modAnagAziendaVO.getPartitaIVA()!=null && !modAnagAziendaVO.getPartitaIVA().equals(""))
              client.checkPartitaIVA(modAnagAziendaVO.getPartitaIVA(), modAnagAziendaVO.getIdAzienda());
          }
          catch(SolmrException se)
          {
            ValidationError error = new ValidationError(se.getMessage());
            if(se.getMessage().equalsIgnoreCase(""+AnagErrors.get("PIVA_GIA_ESISTENTE")))
              errors.add("partitaIVA", error);
            else
              errors.add("error", error);
          }
        }
        else
        {
          // controllo che non esista un'azienda attiva diversa da quella che sto modificando
          // con la stessa partita iva
          if(modAnagAziendaVO.getPartitaIVA() != null && !modAnagAziendaVO.getPartitaIVA().equals(""))
          {
            AnagAziendaVO anagTemp = client.getAltraAziendaFromPartitaIVA(modAnagAziendaVO.getPartitaIVA(), modAnagAziendaVO.getIdAzienda());
            ValidationError error = new ValidationError(""+AnagErrors.get("PIVA_GIA_ESISTENTE"));
            if(anagTemp != null) errors.add("partitaIVA", error);
          }
        }



        String istatComune = null;
        String istatStatoEstero = null;
        if(modAnagAziendaVO.getDescComune() != null && !modAnagAziendaVO.getDescComune().equals(""))
        {
          try
          {
            istatComune = client.ricercaCodiceComuneNonEstinto(modAnagAziendaVO.getDescComune(),modAnagAziendaVO.getSedelegProv());
            modAnagAziendaVO.setSedelegComune(istatComune);
            modAnagAziendaVO.setSedelegEstero("");
          }
          catch(SolmrException se)
          {
            ValidationError error = new ValidationError(""+AnagErrors.get("ERR_COMUNE_SEDE_INESISTENTE"));
            errors.add("sedelegComune",error);
          }
        }
        else
        {
          if(modAnagAziendaVO.getStatoEstero() != null && !modAnagAziendaVO.getStatoEstero().equals(""))
          {
            try
            {
              istatStatoEstero = client.ricercaCodiceComune(modAnagAziendaVO.getStatoEstero(),"");
              modAnagAziendaVO.setSedelegComune(istatStatoEstero);
            }
            catch(SolmrException se)
            {
              ValidationError error = new ValidationError(""+AnagErrors.get("ERR_STATO_ESTERO_SEDE_INESISTENTE"));
              errors.add("sedelegEstero",error);
            }
          }
        }



        String provinciaNascita=modPersonaVO.getNascitaProv();
        String provinciaResidenza=modPersonaVO.getDescResProvincia();
        String comuneDomicilio=modPersonaVO.getDomComune();
        String comuneResidenza=modPersonaVO.getDescResComune();
        String provinciaDomicilio=modPersonaVO.getDomProvincia();
        String statoEsteroDomicilio=modPersonaVO.getDomicilioStatoEstero();


        String codFiscaleComune = "";
        String etichettaStr = "";
        try
        {
         if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") || Validator.isNotEmpty(provinciaNascita))
         {
           etichettaStr = "descNascitaComune";
           codFiscaleComune = client.ricercaCodiceFiscaleComune(descrizioneLuogoNascita,provinciaNascita);
         }
         else
         {
           etichettaStr = "nascitaStatoEstero";
           codFiscaleComune = client.ricercaCodiceFiscaleComune(statoEsteroNascita,"");
         }
        }
        catch(SolmrException se)
        {
         ValidationError error = new ValidationError(se.getMessage());
         errors.add(etichettaStr, error);
        }
        // Recupero i codici istat dei comuni
        // NASCITA
        String istatComuneNascita = "";
        String istatEsteroNascita = "";
        etichettaStr = "";
        try
        {
          if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
              provinciaNascita != null && !provinciaNascita.equals(""))
          {
            etichettaStr = "descNascitaComune";
            istatComuneNascita = client.ricercaCodiceComune(descrizioneLuogoNascita,provinciaNascita);
          }
          else
          {
            etichettaStr = "nascitaStatoEstero";
            istatEsteroNascita = client.ricercaCodiceComune(statoEsteroNascita,"");
          }
        }
        catch(SolmrException se)
        {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add(etichettaStr, error);
        }
        if(!istatComuneNascita.equals(""))
          modPersonaVO.setNascitaComune(istatComuneNascita);
        else 
        {
          modPersonaVO.setNascitaComune(istatEsteroNascita);
          modPersonaVO.setNascitaStatoEstero(istatEsteroNascita);
        }

        // RESIDENZA
        String istatComuneResidenza = "";
        etichettaStr = "";
        try
        {
         if(provinciaResidenza != null && !provinciaResidenza.equals("") || comuneResidenza != null && !comuneResidenza.equals(""))
         {
           etichettaStr = "descResComune";
           istatComuneResidenza = client.ricercaCodiceComuneNonEstinto(comuneResidenza,provinciaResidenza);
         }
         else
         {
           etichettaStr = "statoEsteroRes";
           istatComuneResidenza = client.ricercaCodiceComuneNonEstinto(statoEsteroResidenza,"");
         }
        }
        catch(SolmrException se) {
         ValidationError error = new ValidationError(se.getMessage());
         errors.add(etichettaStr, error);
        }

        modPersonaVO.setResComune(istatComuneResidenza);
        
        // Controllo che il domicilio inserito dall'utente sia valido
        /**
        String istatComuneDomicilio = null;
        if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals("")))
        {
         try
         {
           istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
         }
         catch(SolmrException se)
         {
           ValidationError error = new ValidationError(se.getMessage());
           errors.add("domComune", error);
         }
        }
        if(Validator.isNotEmpty(statoEsteroDomicilio))
        {
         try
         {
           istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(statoEsteroDomicilio ,"");
         }
         catch(SolmrException se)
         {
           ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
           errors.add("domicilioStatoEstero", error);
         }
        }
        modPersonaVO.setIstatComuneDomicilio(istatComuneDomicilio);*/

        session.setAttribute("modPersonaVO",modPersonaVO);
        session.setAttribute("modAnagAziendaVO",modAnagAziendaVO);

        if(errors != null && errors.size() != 0)
        {
          request.setAttribute("errors", errors);
          %><jsp:forward page="<%= url %>"/><%
          return;
        }

        // Controllo della coerenza del codice fiscale non
        try
        {
         Validator.verificaCf(modPersonaVO.getNome(),modPersonaVO.getCognome(),
                              modPersonaVO.getSesso(),modPersonaVO.getNascitaData(),
                              codFiscaleComune, modPersonaVO.getCodiceFiscale());
        }
        catch(CodiceFiscaleException ce)
        {
          if(ce.getNome()) errors.add("nome",new ValidationError(""+AnagErrors.get("ERR_NOME_CODICE_FISCALE")));
          else
            if(ce.getCognome()) errors.add("cognome",new ValidationError(""+AnagErrors.get("ERR_COGNOME_CODICE_FISCALE")));
            else
              if(ce.getAnnoNascita()) errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_ANNO_NASCITA_CODICE_FISCALE")));
              else
                if(ce.getMeseNascita()) errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_MESE_NASCITA_CODICE_FISCALE")));
                else
                  if(ce.getGiornoNascita())  errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_GIORNO_NASCITA_CODICE_FISCALE")));
                  else
                    if(ce.getComuneNascita()) errors.add("descNascitaComune",new ValidationError(""+AnagErrors.get("ERR_COMUNE_NASCITA_CODICE_FISCALE")));
                    else
                      if(ce.getSesso()) errors.add("sesso",new ValidationError(""+AnagErrors.get("ERR_SESSO_CODICE_FISCALE")));
                      else errors.add("codiceFiscale",new ValidationError(ce.getMessage()+": impossibile procedere"));

          request.setAttribute("errors", errors);
          %><jsp:forward page="<%= url %>"/><%
          return;
        }

        /*
         Se sono arrivato qui vuol dire che posso salvare i dati
         Storicizzare tenendo presente che:
         Se in data corrente, per l'azienda in questione, è già stata effettuata una variazione storicizzata,
         il sistema effettua direttamente la correzione dei dati. (E' ammessa
         una sola storicizzazione al giorno) a meno che venga modificato il
         cuaa ed in tal caso storicizzare. Nel caso dell'aggiornamento dei dati,
         senza tenere storia della modifica, il sistema memorizza però
         l'utente che opera e la data. Nella storicizzazione indicare la
         motivazione della modifica "Dichiarazione di insediamento".
        */

        //Leggo la persona fisica inserita attualmente per poterla ripristinare in caso di rollback 
        PersonaFisicaVO personaOld = (PersonaFisicaVO)request.getSession().getAttribute("personaVO");
        
        
        
        
        String esito=client.insediamentoAtomico(
          modAnagAziendaVO,modPersonaVO,request,anagAziendaVO,
          ProfileUtils.getSianUtente(ruoloUtenza), ruoloUtenza);
        
        
        if (((String)SolmrConstants.get("P_ESITO_CONTR")).equals(esito))
        {
          //devo ripristinare i vecchi dati
          request.getSession().setAttribute("anagAziendaVO",anagAziendaVO);
          request.getSession().setAttribute("personaVO",personaOld);
        }
        
        session.removeAttribute("common");
        session.setAttribute("common",esito);
 

        //E' andato tutto bene, quindi lo mando avanti a fare i controlli pls-sql
        response.sendRedirect(DICHIARAZIONE_INSEDIAMENTO_URL);
        return;
      }

    }
    else // E' la prima volta che entro
    {
      session.removeAttribute("modAnagAziendaVO");
      session.removeAttribute("modPersonaVO");
      session.removeAttribute("anomalieDichiarazione");

      AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
      //Leggo anagAziendaVO da DB per evitare di perdere eventuali modifiche
      //fatte da qualcun altro.
      AnagAziendaVO modAnagAziendaVO = client.findAziendaAttiva(anagAziendaVO.getIdAzienda());
      if(modAnagAziendaVO.getDelegaVO() != null)
      {
        String idTipoIntermediarioDelegato = modAnagAziendaVO.getDelegaVO().getIdIntermediario().toString();
        modAnagAziendaVO.setIdIntermediarioDelegato(idTipoIntermediarioDelegato);
      }
      //Metto in session l'oggetto caricato da DB
      session.setAttribute("modAnagAziendaVO",modAnagAziendaVO);


       // recupero dalla sessione i dati del rappresentante legale
      PersonaFisicaVO modPersonaVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
      //Metto in session l'oggetto caricato da DB
      session.setAttribute("modPersonaVO",modPersonaVO);
    }
  }
  catch(Exception e)
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new ValidationError(e.getMessage()));
    request.setAttribute("errors",ve);
    e.printStackTrace();
  }
  %><jsp:forward page="<%= url %>"/><%
%>



