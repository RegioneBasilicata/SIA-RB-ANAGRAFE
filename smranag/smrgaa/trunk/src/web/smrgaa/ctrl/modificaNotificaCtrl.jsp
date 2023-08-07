<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.profile.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smrcomms.siapcommws.InvioPostaCertificata" %>
<%@ page import="it.csi.smrcomms.siapcommws.DatiMailVO" %>
<%@ page import="it.csi.solmr.dto.comune.AmmCompetenzaVO" %>
<%@ page import="it.csi.solmr.dto.comune.TecnicoAmministrazioneVO" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "modificaNotificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  final String errMsg = "Impossibile procedere nella sezione modifica notifica."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/notifiche.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

  String modificaNotificaUrl = "/view/modificaNotificaView.jsp";
  //String notificheUrl = "/view/notificheView.jsp";
  String elencoNotificheUrl = "/ctrl/notificheCtrl.jsp";
  
  WebUtils.removeUselessFilter(session, "notificaModificaVO"); 

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationErrors errors = new ValidationErrors();
  String operazione = request.getParameter("operazione");
  NotificaVO notificaModificaVO = (NotificaVO)session.getAttribute("notificaModificaVO");
  

  // Recupero l'oggetto notifica, prima volta che entro.
  if(Validator.isEmpty(notificaModificaVO))
  {
    Long idNotifica = Long.decode(request.getParameter("idNotifica"));
	  try 
	  {
	    notificaModificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, null);
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+":"+ se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
	}
	else
	{
	  String descrizione = request.getParameter("descrizione").trim();    
    notificaModificaVO.setDescrizione(descrizione);
    
    if(Validator.isNotEmpty(notificaModificaVO.getElencoParticelle()))
    {
      if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
        && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
      {
		    String[] arrNoteUv = request.getParameterValues("noteUv"); 
		    for(int i=0;i<notificaModificaVO.getElencoParticelle().size();i++)
		    {
		      notificaModificaVO.getElencoParticelle().get(i).getUnitaArboreaDichiarataVO().setNote(arrNoteUv[i]);
		    }
		  }
		  else if (Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
        && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
      {
        String[] arrNoteParticelle = request.getParameterValues("noteParticelle"); 
        for(int i=0;i<notificaModificaVO.getElencoParticelle().size();i++)
        {
          notificaModificaVO.getElencoParticelle().get(i).setNote(arrNoteParticelle[i]);
        }
      }
	  }
	}
	
	if(!anagFacadeClient.isModificaNotificaRuoloPossibile(ruoloUtenza, notificaModificaVO.getIdCategoriaNotifica().longValue()))
	{
	  request.setAttribute("idNotifica", notificaModificaVO.getIdNotifica());
    // Effettuo la ricerca delle notifiche
    // Creo l'oggetto contenente i criteri per effettuare la ricerca
    ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_MOD_NOTIF);
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(elencoNotificheUrl).forward(request, response);
    return;
	}
	
	//Notifica già chiusa.
	if(Validator.isNotEmpty(notificaModificaVO.getDataChiusura()))
  {
    request.setAttribute("idNotifica", notificaModificaVO.getIdNotifica());
    ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_MOD_NOTIF_CHIUSA);
    errors.add("error",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(elencoNotificheUrl).forward(request, response);
    return;
  }
    
  
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = null;
  //entro qui solo se selezionata notifica che prevede inserimento uv
  if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
    && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
  {
	  try 
	  {
	    vCategoriaNotifica = anagFacadeClient.
	      getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
	  }
	  catch(SolmrException se) 
	  {
	    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
	    errors.add("idCategoriaNotifica", error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
	    return;
	  }
	  // Metto il vettore in request
	  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  
  
    ConsistenzaVO consistenzaVO = anagFacadeClient.getUltimaDichConsNoCorrettiva(
      anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("consistenzaVO", consistenzaVO);
    
    if(Validator.isNotEmpty(consistenzaVO))
    {
      // Recupero i valori relativi alla destinazione produttiva
      TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
      try 
      {
        //String[] ordinamento = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
        elencoTipiUsoSuolo = anagFacadeClient.getListTipoUtilizzoByIdDichiarazioneConsistenza(SolmrConstants.TIPO_UTILIZZO_VIGNETO,
          new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
        request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA);
        errors.add("idTipoUtilizzoElenco", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      // Recupero il vitigno in funzione della destinazione produttiva selezionata
      TipoVarietaVO[] elencoVarieta = null;
      try 
      {
        elencoVarieta = anagFacadeClient.getListTipoVarietaByIdDichiarazioneConsistenza(
          new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
        request.setAttribute("elencoVarieta", elencoVarieta);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_VITIGNO);
        errors.add("idVarieta", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      
      // Recupero i valori relativi alle provincie su cui insistono le particelle
      Vector<ProvinciaVO> elencoProvincie = null;
      try 
      {
        elencoProvincie = gaaFacadeClient.getListProvincieParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
        request.setAttribute("elencoProvincie", elencoProvincie);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatProvinciaConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      // Recupero i valori relativi ai comuni su cui insistono le particelle
      Vector<ComuneVO> elencoComuni = null;
      try 
      {
        String[] ordinamento = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE};
        elencoComuni = anagFacadeClient.getListComuniParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), ordinamento);
        request.setAttribute("elencoComuni", elencoComuni);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatComuniConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      // Recupero i valori relativi alla tipologia Vino
      TipoTipologiaVinoVO[] elencoTipiTipologiaVino = null;
      try 
      {
        TipoTipologiaVinoVO[] elencoTipiVino = null;
        elencoTipiTipologiaVino = anagFacadeClient.getListTipoTipologiaVinoForAzienda(
          new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue());
        int dim = elencoTipiTipologiaVino.length;
        
        elencoTipiVino = new TipoTipologiaVinoVO[dim+2];
        TipoTipologiaVinoVO ttvAll = new TipoTipologiaVinoVO();
        TipoTipologiaVinoVO ttvNn = new TipoTipologiaVinoVO();
        ttvAll.setIdTipologiaVino(new Long(-1));
        ttvAll.setDescrizione("qualunque tipologia di vino");
        ttvNn.setIdTipologiaVino(new Long(0));
        ttvNn.setDescrizione("senza tipologia di vino");
        
        elencoTipiVino[0] = ttvAll;
        elencoTipiVino[1] = ttvNn;
        
        for(int i=2; i<dim+2; i++)
          elencoTipiVino[i] = elencoTipiTipologiaVino[i-2];
          
        request.setAttribute("elencoTipiTipologiaVino", elencoTipiVino);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_VINO);
        errors.add("idTipologiaVino", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      
      
      
    }
  
  }
  else if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
    && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
  {
    try 
    {
      vCategoriaNotifica = anagFacadeClient.
        getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
      errors.add("idCategoriaNotifica", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
      return;
    }
    // Metto il vettore in request
    request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  
  
    ConsistenzaVO consistenzaVO = anagFacadeClient.getUltimaDichConsNoCorrettiva(
      anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("consistenzaVO", consistenzaVO);
    
    if(Validator.isNotEmpty(consistenzaVO))
    {
    
    // Recupero i valori relativi alle provincie su cui insistono le particelle
      Vector<ProvinciaVO> elencoProvincie = null;
      try 
      {
        elencoProvincie = gaaFacadeClient.getListProvincieParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()));
        request.setAttribute("elencoProvincie", elencoProvincie);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatProvinciaConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      // Recupero i valori relativi ai comuni su cui insistono le particelle
      Vector<ComuneVO> elencoComuni = null;
      try 
      {
        String[] ordinamento = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE};
        elencoComuni = anagFacadeClient.getListComuniParticelleByIdDichiarazioneConsistenza(new Long(consistenzaVO.getIdDichiarazioneConsistenza()), ordinamento);
        request.setAttribute("elencoComuni", elencoComuni);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("istatComuniConduzioniParticelle", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
    
      // Recupero i valori relativi ai tipi uso suolo(attivi e non) utilizzati dall'azienda in esame
      Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();
      try 
      {
        String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
        elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByIdDichCons(new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue(), orderBy);
        request.setAttribute("elencoTipiUsoSuoloPart", elencoTipiUsoSuolo);
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
        errors.add("idTipoUtilizzo", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
      
      //Combo anomalie
      TipoControlloVO[] elencoTipiControllo = null;
      try 
      {
        String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
        elencoTipiControllo = anagFacadeClient.getListTipoControlloForAziendaByIdGruppoControlloAndDichCons(
          SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, new Long(consistenzaVO.getIdDichiarazioneConsistenza()), orderBy);
        request.setAttribute("elencoTipiControllo", elencoTipiControllo);
      }
      catch(SolmrException se)
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_ELENCO_ANOMALIE);
        errors.add("idAnomalie", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }
      
    }
  
  
  } 
  
  

  // L'utente ha premuto il pulsante salva
  if(request.getParameter("salva") != null) 
  {

    // Recupero i parametri inseriti dall'utente
    String descrizione = request.getParameter("descrizione").trim();    
    notificaModificaVO.setDescrizione(descrizione);
    

    // Effettuo i controlli di validazione formale
    errors = notificaModificaVO.validateModificaNotifica();

    // Se ci sono errori li visualizzo
    if(errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
      return;
    }

    //Vettore per l'invio delle mail
    Vector<String> vIstatProvincia = new Vector<String>();
    //questa la ho solo se la notifica nn è bloccante e del tipo....
    Vector<StoricoParticellaVO> elencoParticelle = notificaModificaVO.getElencoParticelle();
    if(Validator.isNotEmpty(elencoParticelle) && (elencoParticelle.size() > 0))
    {
    
      Vector<NotificaEntitaVO> vNotificaEntita = new Vector<NotificaEntitaVO>();
      Vector<ValidationErrors> elencoErroriNote = new Vector<ValidationErrors>();
      int countErroriNote = 0;
      String[] arrNoteUv = request.getParameterValues("noteUv");
      String[] arrNoteParticelle = request.getParameterValues("noteParticelle");
      String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");  
	    for(int i=0;i<elencoParticelle.size();i++)
	    {
	      NotificaEntitaVO notificaEntitaVO = new NotificaEntitaVO();
	      notificaEntitaVO.setIdTipoEntita(notificaModificaVO.getIdTipoEntita());
	      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
          notificaEntitaVO.setIdDichiarazioneConsistenza(new Long(idDichiarazioneConsistenza));
	      ValidationErrors errorsNoteParticelle = new ValidationErrors(); 
	      
	      if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
          && SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
        {
          notificaEntitaVO.setIdentificativo(elencoParticelle.get(i).getStoricoUnitaArboreaVO().getIdUnitaArborea());         
	        if(arrNoteUv[i].length() > 2000)
	        {
	          errorsNoteParticelle.add("noteUv", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	          countErroriNote++;
	        }
	        else
	        {
	          notificaEntitaVO.setNote(arrNoteUv[i]);
	        }
	      }
	      else if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
          && SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString()))
        {
          notificaEntitaVO.setIdentificativo(elencoParticelle.get(i).getIdParticella());         
          if(arrNoteParticelle[i].length() > 2000)
          {
            errorsNoteParticelle.add("noteParticelle", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            countErroriNote++;
          }
          else
          {
            notificaEntitaVO.setNote(arrNoteParticelle[i]);
          }
        } 
        
        vNotificaEntita.add(notificaEntitaVO);
        
        elencoErroriNote.add(errorsNoteParticelle);
        
        //aggiungo istat prov per la mail...
        if(!vIstatProvincia.contains(elencoParticelle.get(i).getComuneParticellaVO().getIstatProvincia()))
        {
          vIstatProvincia.add(elencoParticelle.get(i).getComuneParticellaVO().getIstatProvincia());
        }                 
	    
	    }
	    
	    if(countErroriNote > 0) 
      {
        request.setAttribute("elencoErroriNote", elencoErroriNote);
        request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
        return;
      }	    
	    
	    notificaModificaVO.setvNotificaEntita(vNotificaEntita);
	  }
	  
	  
	  
	  boolean flagInvioMail = false;
	  if(Validator.isNotEmpty(notificaModificaVO.getIdTipoEntita()) 
      && (SolmrConstants.TIPO_ENTITA_UV.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString())
       || SolmrConstants.TIPO_ENTITA_PARTICELLE.equalsIgnoreCase(notificaModificaVO.getIdTipoEntita().toString())))
    {
      //se cambio la categoria invio la mail
      Long idCategoriaNotifica = new Long(request.getParameter("idCategoriaNotifica"));
      if(idCategoriaNotifica.compareTo(notificaModificaVO.getIdCategoriaNotifica()) !=0)
      {      
        flagInvioMail = true;
      }      
      
      notificaModificaVO.setIdCategoriaNotifica(idCategoriaNotifica);
    }

    try 
    {
      anagFacadeClient.updateNotifica(notificaModificaVO, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
      return;
    }
    
    if(flagInvioMail)
    {
      flagInvioMail = false;
      String parametroInvioMailNotif = "";
	    try 
	    {
	      parametroInvioMailNotif = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_INVIO_MAIL_NOTIF);
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_INVIO_MAIL_NOTIF+".\n"+se.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
	    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroInvioMailNotif);
	    
	    
	    String descCategoria = "";  
	    for(int i=0; i<vCategoriaNotifica.size(); i++)
	    {
	      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);      
	      if(Validator.isNotEmpty(notificaModificaVO.getIdCategoriaNotifica()) 
	        && (notificaModificaVO.getIdCategoriaNotifica().intValue() == tipoCategoriaNotificaVO.getIdCategoriaNotifica().intValue())) 
	      {
	        if("S".equalsIgnoreCase(tipoCategoriaNotificaVO.getInviaEmail())
	          && "S".equalsIgnoreCase(parametroInvioMailNotif))
	        {
	          flagInvioMail = true;
	          descCategoria = tipoCategoriaNotificaVO.getDescrizione();
	        }
	        break;       
	      }
	    }
	    
	    
	    if(flagInvioMail)
	    {   
	    
	      try 
	      {
	        
	        // ** Costruisco gli oggetti con i dati per l'invio mail      
	        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
	        
	        Vector<Long> vIdAmmCompetenza = new Vector<Long>();
	        for(int i=0;i<vIstatProvincia.size();i++)
	        {
	          AmmCompetenzaVO ammCompetenza = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(vIstatProvincia.get(i));
	          vIdAmmCompetenza.add(ammCompetenza.getIdAmmCompetenzaLong());
	        }
	        
	        
	        Vector<String> destinatariMailA = new Vector<String>(); 
	        for(int i=0;i<vIdAmmCompetenza.size();i++)
	        {
	          TecnicoAmministrazioneVO[] arrTecnico = anagFacadeClient.serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(
	            vIdAmmCompetenza.get(i), new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
	          if(Validator.isNotEmpty(arrTecnico))
	          {
	            for(int j=0;j<arrTecnico.length;j++)
	            {
	              TecnicoAmministrazioneVO tecnico = arrTecnico[j];
	              for(int k=0;k<tecnico.getTecnico().getARecapitoTecnicoVO().length;k++)
	              {
	                //Prendo solo recapito mail
	                if("5".equalsIgnoreCase(tecnico.getTecnico().getARecapitoTecnicoVO()[k].getIdTipoRecapito()))
	                {
	                  destinatariMailA.add(tecnico.getTecnico().getARecapitoTecnicoVO()[k].getRecapito());
	                }
	              }
	            }
	          }
	        }
	        
	        String destinatarioMail = "";
	        for(int i=0;i<destinatariMailA.size();i++)
	        {
	          destinatarioMail += destinatariMailA.get(i)+",";
	        }
	        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
	        
	        if(destinatariMailA.size() > 0)
	        {
	          
	          // ----- Mittente -> VTMA di DB_ALTRI_DATI       
	          String mittente = "";
	          try 
	          {
	            mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_NOTIF_M);
	          }
	          catch(SolmrException se) 
	          {
	            SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_NOTIF_M+".\n"+se.toString();
	            request.setAttribute("messaggioErrore",messaggio);
	            request.setAttribute("pageBack", actionUrl);
	            %>
	              <jsp:forward page="<%= erroreViewUrl %>" />
	            <%
	            return;
	          }
	          SolmrLogger.debug(this, " --- MITTENTE ="+mittente);
	          
	          String mittenteReplyTo = "";
		        try 
		        {
		          mittenteReplyTo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REPLY_TO_MAIL);
		        }
		        catch(SolmrException se) 
		        {
		          SolmrLogger.info(this, " - newInserimentoConfermaCessazioneCtrl.jsp - FINE PAGINA");
		          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_REPLY_TO_MAIL+".\n"+se.toString();
		          request.setAttribute("messaggioErrore",messaggio);
		          request.setAttribute("pageBack", actionUrl);
		          %>
		            <jsp:forward page="<%= erroreViewUrl %>" />
		          <%
		          return;
		        }
		        SolmrLogger.debug(this, " --- MITTENTE REPLYTO="+mittenteReplyTo);
	              
	          // ---- Oggetto
	          String oggettoMail = "";
	          try 
	          {
	            oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_MAIL_NOTIF_M);
	          }
	          catch(SolmrException se) 
	          {
	            SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_MAIL_NOTIF_M+".\n"+se.toString();
	            request.setAttribute("messaggioErrore",messaggio);
	            request.setAttribute("pageBack", actionUrl);
	            %>
	              <jsp:forward page="<%= erroreViewUrl %>" />
	            <%
	            return;
	          }
	          
	          if(Validator.isNotEmpty(oggettoMail))
	          { 
	            oggettoMail = oggettoMail.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
	            oggettoMail = oggettoMail.replaceAll("<<TIPO_NOTIFICA>>", descCategoria);          
	          }
	            
	          SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
	              
	              
	          // ----- Testo
	          String testo = "";
	          try 
	          {
	            testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_PER_NOTIF_M);
	          }
	          catch(SolmrException se) 
	          {
	            SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_PER_NOTIF_M+".\n"+se.toString();
	            request.setAttribute("messaggioErrore",messaggio);
	            request.setAttribute("pageBack", actionUrl);
	            %>
	              <jsp:forward page="<%= erroreViewUrl %>" />
	            <%
	            return;
	          }
	          
	          if(Validator.isNotEmpty(testo))
	          {                   
	            testo = testo.replaceAll("<<TIPO_NOTIFICA>>", descCategoria);
	            testo = testo.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
	            testo = testo.replaceAll("<<DENOMINAZIONE>>",anagAziendaVO.getDenominazione());       
	            
	          }
	          SolmrLogger.debug(this, " --- TESTO ="+testo);
	          
	          
	          
	          // Setto i valori per le mail da inviare
            InvioPostaCertificata invioPosta = new InvioPostaCertificata();
	          DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
	          DatiMailVO datiMailVO = new DatiMailVO(); 
	          
	          String[] arrDestinatari = new String[destinatariMailA.size()];
	          for(int k=0;k<destinatariMailA.size();k++)
	          {
	            arrDestinatari[k] = destinatariMailA.get(k);
	          }
	          datiMailVO.setDestinatariA(arrDestinatari);
	          datiMailVO.setMittente(mittente);
	          datiMailVO.setMittenteDisplayName(mittenteReplyTo);
	          datiMailVO.setOggetto(oggettoMail);
	          datiMailVO.setTesto(testo);
	          
	          
	          arrDatiMail[0] = datiMailVO;
	          invioPosta.setInput(arrDatiMail);
	            
	            
	          anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
	          
	          
	              
	          // Setto i valori per le mail da inviare
	          //destinatariMailA.add(destinatarioMail);           
	          
	          /*DatiMail datiMail = new DatiMail();
	          datiMail.setDestinatariA(destinatariMailA);
	          datiMail.setMittente(mittente);
	          datiMail.setMittenteReplyTo(mittenteReplyTo);           
	          datiMail.setOggetto(oggettoMail);           
	          datiMail.setTesto(testo);           
	          datiMailVect.add(datiMail); 
	                         
	           
	          
	          
	          // Se ci sono destinatari per l'invio mail, invio MAIL
	          SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
	          if(datiMailVect != null && datiMailVect.size()>0)
	          {
	            SolmrLogger.error(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
	            SolmrLogger.error(this, " -- numero di mail da inviare ="+datiMailVect.size());
	            invioMail.sendMail(datiMailVect);     
	                    
	          }*/
	        }
	        
	      }
	      catch (SolmrException e) 
	      {
	        SolmrLogger.info(this, " - modificaNotificaCtrl.jsp - FINE PAGINA");
	        String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL_NOTIFICA+".\n"+e.toString();
	        request.setAttribute("messaggioErrore",messaggio);
	        request.setAttribute("pageBack", actionUrl);
	        %>
	          <jsp:forward page="<%= erroreViewUrl %>" />
	        <%
	        return;
	      } 
	    
	    }    
    
    }
    
    
    session.removeAttribute("notificaModificaVO");
    %>
      <jsp:forward page= "<%= elencoNotificheUrl %>" />
    <%
    return;

  }
  // L'utente ha selezionato il pulsante annulla
  else if(request.getParameter("annulla") != null) 
  {

    session.removeAttribute("notificaModificaVO");
    %>
      <jsp:forward page= "<%= elencoNotificheUrl %>" />
    <%
    return;
  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle uv
  else if(request.getParameter("eliminaUv") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idStoricoUnitaArborea");
    Vector<StoricoParticellaVO> temp = null;
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_UV_NOTIFICA_ELIMA_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      Vector<StoricoParticellaVO> elencoParticelle = notificaModificaVO.getElencoParticelle();
      if(elementsToRemove.length == elencoParticelle.size()) 
      {
        notificaModificaVO.setElencoParticelle(null);
        notificaModificaVO.setvNotificaEntita(null);
      }
      else 
      {
        Hashtable<Long,StoricoParticellaVO> hash = new Hashtable<Long,StoricoParticellaVO>();
        for(int i =0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = elencoParticelle.get(i);
          hash.put(storicoParticellaVO.getUnitaArboreaDichiarataVO().getIdStoricoUnitaArborea(), storicoParticellaVO);
        }
        for(int j = 0; j < elementsToRemove.length; j++) 
        {
          hash.remove(Long.decode((String)elementsToRemove[j]));
        }
        temp = new Vector<StoricoParticellaVO>();
        Enumeration<StoricoParticellaVO> enumeration = hash.elements();
        while(enumeration.hasMoreElements()) 
        {
          temp.add(enumeration.nextElement());
        }
      }
    }
    // Metto il vettore in sessione
    if(temp != null && temp.size() > 0) 
    {
      Collections.sort(temp, new StoricoParticellaUVComparator());
      
      notificaModificaVO.setElencoParticelle(temp);
      session.setAttribute("notificaModificaVO", notificaModificaVO);
    }
    
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= modificaNotificaUrl %>" />
    <%
  }
  // E'stato selezionato il pulsante "elimina" relativo all'elenco delle particelle
  else if(request.getParameter("eliminaParticelle") != null) 
  {
    // Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idStoricoParticella");
    Vector<StoricoParticellaVO> temp = null;
    // Controllo che sia stato selezionato qualcosa dall'utente
    if(elementsToRemove == null || elementsToRemove.length == 0) 
    {
      errors.add("error", new ValidationError(AnagErrors.ERRORE_PARTICELLE_NOTIFICA_ELIM_KO));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(modificaNotificaUrl).forward(request, response);
      return;
    }
    // Se sì, elimino gli elementi selezionati
    else 
    {
      Vector<StoricoParticellaVO> elencoParticelle = notificaModificaVO.getElencoParticelle();
      if(elementsToRemove.length == elencoParticelle.size()) 
      {
        notificaModificaVO.setElencoParticelle(null);
        notificaModificaVO.setvNotificaEntita(null);
      }
      else 
      {
        Hashtable<Long,StoricoParticellaVO> hash = new Hashtable<Long,StoricoParticellaVO>();
        for(int i =0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = elencoParticelle.get(i);
          hash.put(storicoParticellaVO.getIdStoricoParticella(), storicoParticellaVO);
        }
        for(int j = 0; j < elementsToRemove.length; j++) 
        {
          hash.remove(Long.decode((String)elementsToRemove[j]));
        }
        temp = new Vector<StoricoParticellaVO>();
        Enumeration<StoricoParticellaVO> enumeration = hash.elements();
        while(enumeration.hasMoreElements()) 
        {
          temp.add(enumeration.nextElement());
        }
      }
    }
    // Metto il vettore in sessione
    if(temp != null && temp.size() > 0) 
    {
      Collections.sort(temp, new StoricoParticellaComparator());
      
      notificaModificaVO.setElencoParticelle(temp);
      session.setAttribute("notificaModificaVO", notificaModificaVO);
    }
    
    // Torno alla pagina di inserimento
    %>
        <jsp:forward page="<%= modificaNotificaUrl %>" />
    <%
  }
  // L'utente ha selezionato la funzione inserisci notifica
  else 
  {
  
    session.setAttribute("notificaModificaVO", notificaModificaVO);
    // Vado alla pagina di modifica della notifica
    %>
      <jsp:forward page= "<%= modificaNotificaUrl %>" />
    <%
  }

%>
