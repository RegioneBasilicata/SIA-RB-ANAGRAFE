<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "particellareTerreniImportaAsservimentoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  final String errMsg = "Impossibile procedere nella sezione Terreni->importa asservimento. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  final String RICERCA = "../layout/terreniImportaAsservimento.htm";
    
  String particellaTerreniImportaAsservimentoUrl = "/view/particellareTerreniImportaAsservimentoView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/particellareTerreniImportaAsservimento.htm";
  String terreniImportaAsservimentoOkUrl = "/view/terreniImportaAsservimentoOkView.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  String actionUrl = "../layout/terreniParticellareElenco.htm";


  ValidationErrors errors = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO anagAziendaSearchVO = (AnagAziendaVO)session.getAttribute("anagAziendaSearchVoImportaAsservimento");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //se la variabile in sessione ricercaImportaAsservimento == ricercaParticelle
  // elencoParticelleImportaAsservimento sono idParticelle.
  //altrimenti sono idConduzioneDichiarata!
  //Particelle selezionate
  long[] elencoIdParticelle = (long[])session.getAttribute("elencoIdParticelleImportaAsservimento");
  
  
  //****************IMPORTANTE*******************
  //se la variabile in sessione ricercaImportaAsservimento == ricercaParticelle
  //idParticella corrisponde ai veri idParticella
  //altrimenti sono idConduzioneDichiarata!!!!!
  //Particelle selezionate
  String[] elencoIdChkParticella = request.getParameterValues("idParticella");
  //Tutte le particelle
  String[] elencoIdElem = request.getParameterValues("idElem");
  
  String idUte = request.getParameter("idUte");
  
  String operazione = request.getParameter("operazione");
  String idTitoloPossesso = request.getParameter("idTitoloPossesso");
  if (Validator.isEmpty(idTitoloPossesso))
    idTitoloPossesso = (String)session.getAttribute("idTitoloPossesso");
  session.setAttribute("idTitoloPossesso", idTitoloPossesso);
  
  // TITOLO DI POSSESSO
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
  try 
  {
    elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
    request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  }
  catch(SolmrException se) 
  {       
    request.setAttribute("messaggioErrore",(String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO+" "+se.getMessage());
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;    
  }
  
  
  FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoImportaAsservimentoVO 
    = (FiltriRicercaTerrenoImportaAsservimentoVO) session.getAttribute("filtriRicercaTerrenoImportaAsservimentoVO");
  if (filtriRicercaTerrenoImportaAsservimentoVO == null)
  {
    response.sendRedirect(RICERCA);
    return;
  }
  
  //gestione dei checkbox con la paginazione!! ***************
  //la prima volta che entro non ho valorizzato nessun elemento (arrivo da terreniImporttaAsservimento!!!
  if(Validator.isNotEmpty(elencoIdElem))
  {
    HashMap<String,Long> hId = filtriRicercaTerrenoImportaAsservimentoVO.getHIdSelezionati();
    if(hId == null)
    {
      hId = new HashMap<String,Long>();
    }
    
    HashMap<String,Long> hElimin = new HashMap<String,Long>();
    for(int i=0;i<elencoIdElem.length;i++)
    {
      hElimin.put(elencoIdElem[i], new Long(elencoIdElem[i]));
    }
    
    if(Validator.isNotEmpty(elencoIdChkParticella))
    {  
      for(int i=0;i<elencoIdChkParticella.length;i++)
      {
        hElimin.remove(elencoIdChkParticella[i]);
      }
      
      HashMap<String,Long> hSel = new HashMap<String,Long>();
      for(int i=0;i<elencoIdChkParticella.length;i++)
      {
        hSel.put(elencoIdChkParticella[i], new Long(elencoIdChkParticella[i]));
      }
      
      if(hSel.size() > 0)
      {
        for(int i=0;i<elencoIdChkParticella.length;i++)
        {
          hId.put(elencoIdChkParticella[i], new Long(elencoIdChkParticella[i]));
        }
      }
    }
  
    if(hElimin.size() > 0)
    {
      Iterator iterator = hElimin.keySet().iterator();
      while(iterator.hasNext())
      {
        hId.remove(iterator.next());
      }
    }
    
    filtriRicercaTerrenoImportaAsservimentoVO.setHIdSelezionati(hId);
  }
  
  //******************************Fine gestione checkbox
  
  
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("attenderePrego"))
  {
    if(elencoIdChkParticella !=null)
      session.setAttribute("elencoIdParticellaChkImportaAsservimento",elencoIdChkParticella);
    else
      session.removeAttribute("elencoIdParticellaChkImportaAsservimento");
      
    if(Validator.isNotEmpty(idUte))
      session.setAttribute("idUteImportaAsservimento",idUte);
    else
      session.removeAttribute("idUteImportaAsservimento");
  } 
  
  
  
  
  try
  { 
  
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {     
      request.setAttribute("action", action);
      operazione = "importa";
      request.setAttribute("operazione", operazione);
      
      %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
    }
    else 
    {
      // Recupero l'elenco delle UTE a cui associare le particelle
      Vector elencoUte = null;
      try 
      {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - particellareTerreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
      }
    
      // Metto il vettore in request
      request.setAttribute("elencoUte", elencoUte);
      
       
      if(elencoIdParticelle != null)
      {
        
        //messi in sessione per la attendere
        String idUteTmp = (String)session.getAttribute("idUteImportaAsservimento");
        String tipoRicerca = filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca();
        
        
        String paginaCorrenteRichiesta = request.getParameter("paginaCorrente");
        if (paginaCorrenteRichiesta==null)
        {
          String mantieniPagina=request.getParameter("mantieniPagina");
          if ("true".equals(mantieniPagina))
          {
            paginaCorrenteRichiesta=String.valueOf(filtriRicercaTerrenoImportaAsservimentoVO.getPaginaCorrente());
          }
        }
        PaginazioneUtils pager = PaginazioneUtils.newInstance(elencoIdParticelle,
            SolmrConstants.NUM_RIGHE_PER_PAGINA_RICERCA_TERRENI_IMPORTA_ASSERVIMENTO, 
            filtriRicercaTerrenoImportaAsservimentoVO.getPaginaCorrente(),
            paginaCorrenteRichiesta, "paginaCorrente");
        long idsForPage[] = pager.getIdForCurrentPage(true);
        if (idsForPage != null && idsForPage.length > 0)
        {
          RigaRicercaTerreniImportaAsservimentoVO righe[] = null;
          try 
          {
            if(filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca()
              .equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE))
            {
              righe = anagFacadeClient
                  .getRigheRicercaTerreniImportaAsservimentoByIdParticellaRange(idsForPage);
            }
            else
            {
              righe = anagFacadeClient
                  .getRigheRicercaTerreniImportaAsservimentoByIdConduzioneRange(idsForPage);
            }
            
            //asocio il check se selezionato!
            for(int i=0;i<righe.length;i++)
            {
              if(Validator.isNotEmpty(filtriRicercaTerrenoImportaAsservimentoVO.getHIdSelezionati())
                && (filtriRicercaTerrenoImportaAsservimentoVO.getHIdSelezionati().size() > 0)) 
              {
                String idElementoSelezionato = String.valueOf(righe[i].getIdParticella());
                HashMap hash = filtriRicercaTerrenoImportaAsservimentoVO.getHIdSelezionati();
                if(hash.containsKey(idElementoSelezionato)) 
                {
                  righe[i].setChecked(true);
                }                
              }
            }
            
            pager.setRighe(righe);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - particellareTerreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
          }
          
        }
        filtriRicercaTerrenoImportaAsservimentoVO.setPaginaCorrente(pager.getPaginaCorrente());
        session.setAttribute("filtriRicercaTerrenoImportaAsservimentoVO", filtriRicercaTerrenoImportaAsservimentoVO);
        request.setAttribute("pager", pager);
        
        
        
        
        // L'utente ha selezionato il pulsante "importa"
        if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("importa"))    
        {
          // Controllo che il CUAA sia stato valorizzato
          if(!Validator.isNotEmpty(idUteTmp)) 
          {
            if(errors == null)
            {
              errors = new ValidationErrors();
            }
            errors.add("idUte", new ValidationError(AnagErrors.ERRORE_NO_UTE));
          }
          
          if(errors == null)
          {
          
            HashMap hIdTmp = filtriRicercaTerrenoImportaAsservimentoVO.getHIdSelezionati();
            if(Validator.isNotEmpty(hIdTmp)
              && hIdTmp.size() > 0)
            {
              //Da cambiare il nome del parametro
              String parametroRsas = null;
              try 
              {
                parametroRsas = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RSAS);
              }
              catch(SolmrException se) {
              
                SolmrLogger.info(this, " - particellareTerreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
                String messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RSAS;
                String messaggio = messaggioErrore +": "+se.toString();
                request.setAttribute("messaggioErrore",messaggio);
                request.setAttribute("pageBack", actionUrl);
                %>
                  <jsp:forward page="<%= erroreViewUrl %>" />
                <%
                return;
              }
              
              Integer paramRsasInt = new Integer(parametroRsas);
              if(hIdTmp.size() > paramRsasInt.intValue()) 
              {
                request.setAttribute("messaggioErrore",AnagErrors.ERRORE_TROPPE_VOCI_SELEZIONATE);
                request.setAttribute("pageBack", action);
                %>
                  <jsp:forward page="<%= erroreViewUrl %>" />
                <%
                return;        
              }
              else
              {       
                try 
                {
                  Vector temp= new Vector();
                  
                  String[] elencoIdParticellaTmp = new String[hIdTmp.size()];
                  
                  Iterator iterator = hIdTmp.keySet().iterator();
                  int i = 0;
                  while(iterator.hasNext())
                  {
                    elencoIdParticellaTmp[i] = (String)iterator.next();
                    i++;
                  }
                  Long idTitoloPossessoL=new Long(idTitoloPossesso);
                  
                  if(tipoRicerca.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE))
                  {
                    //Chiave catastale
                    temp=anagFacadeClient.importParticelleAsserviteFromRicercaParticella(
                      elencoIdParticellaTmp, new Long(idUteTmp), anagAziendaVO, ruoloUtenza,idTitoloPossessoL);
                  }
                  else
                  {  
                    //CUAA
                    temp=anagFacadeClient.importParticelleAsservite(elencoIdParticellaTmp,
                      new Long(idUteTmp), anagAziendaSearchVO, ruoloUtenza,idTitoloPossessoL);
                  }
                  request.setAttribute("idParticelleImportate",(String[])temp.get(0));
                  request.setAttribute("idParticelleNonImportate",(String[])temp.get(1));
                  request.setAttribute("tipoImport",(String)temp.get(2));
                }
                catch(SolmrException se) 
                {
                  SolmrLogger.info(this, " - particellareTerreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
                  String messaggio = errMsg+""+se.toString();
                  request.setAttribute("messaggioErrore",messaggio);
                  request.setAttribute("pageBack", actionUrl);
                  %>
                    <jsp:forward page="<%= erroreViewUrl %>" />
                  <%
                }
              }
            }
            else
            {
              request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_PARTICELLE_SELEZIONATE);
              request.setAttribute("pageBack", action);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;  
            }
            
            %>
              <jsp:forward page="<%= terreniImportaAsservimentoOkUrl %>" />
            <%
            return;
          }
          else
          {
            request.setAttribute("errors", errors);
          }           
        }
      }
      else
      {
        String tipoRicerca = filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca();
        //Ricerca con chiave catastale
        if(tipoRicerca.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE))
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_PARTICELLE);
        }
        //Ricerca con cuaa
        else
        {
          request.setAttribute("messaggioErrore", AnagErrors.NO_CONDUZIONI);
        }
      }
    }
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - particellareTerreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
   

%>

<jsp:forward page= "<%= particellaTerreniImportaAsservimentoUrl %>" />

