<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.ws.cciaa.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.comparator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%
  String iridePageName = "popUnitaVitateDettaglioCCIAACtrl.jsp";
  %>
    <%@include file="/include/autorizzazione.inc"%>
  <%

  String popUnitaVitateDettaglioCCIAAUrl = "/view/popUnitaVitateDettaglioCCIAAView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/popUnitaVitateDettaglioCCIAA.htm";

  ValidationError error = null;
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  try
  {

    String operazione = request.getParameter("operazione");
    String messaggioErrore = null;

    // L'utente ha selezionato la voce di menù SIAN e io lo mando alla
    // pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione))
    {
      session.removeAttribute("uvResponseCCIAA");
      session.removeAttribute("storicoParticellaVOCCIAA");
      request.setAttribute("action", action);
      operazione = null;
      request.setAttribute("operazione", operazione);
      //Metto in sessione l'ID dell'unità vitata selezionata
      session.setAttribute("idUnitaCCIAA", request.getParameter("idUnita"));
      %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
      return;
    }
    
    
    
    String parametroLockUvConsolidate = null;
    try 
    {
      parametroLockUvConsolidate = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_LOCK_UV_CONSOLIDATE);
      request.setAttribute("parametroLockUvConsolidate", parametroLockUvConsolidate);
    }
    catch(SolmrException se) 
    {      
      messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_LOCK_UV_CONSOLIDATE;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
      <%
      return;
    }
    
    
    

    // L'utente ha selezionato l'ordinamento per albo decrescente o ascendente
    if("alboDesc".equalsIgnoreCase(operazione) || "alboAsc".equalsIgnoreCase(operazione))
    {
      //Recupero gli eventuali filtri inseriti dall'utente e li metto in sessione
      UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
      uvResponseCCIAA.setOrdAlbo(operazione);

      //Ordino i dati come richiesto dall'utente
      DatiUvCCIAA datiUvCCIAA[]=uvResponseCCIAA.getDatiUv();

      if("alboAsc".equalsIgnoreCase(operazione))
        Arrays.sort(datiUvCCIAA,new DatiUvCCIAAComparator(DatiUvCCIAAComparator.ORD_ALBO_ASC));
      else
        Arrays.sort(datiUvCCIAA,new DatiUvCCIAAComparator(DatiUvCCIAAComparator.ORD_ALBO_DESC));

      uvResponseCCIAA.setDatiUv(datiUvCCIAA);

      session.setAttribute("uvResponseCCIAA",uvResponseCCIAA);
      %>
        <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
      <%
      return;
    }
    
    
    if("importa".equalsIgnoreCase(operazione))
    {
    
      UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
      String[] idDaModificare = request.getParameterValues("chkIdUnitaArborea");
      Vector<StoricoParticellaVO> temp = null;
      //Elimina i doppi!!!!
      Vector<Long> vDoppi = null;      
      String parametroIUVP = null;
      try 
      {
        parametroIUVP = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_IUVP);
      }
      catch(SolmrException se) 
      {
        messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_IUVP;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
        <%
      }
      
      
      StoricoParticellaVO storicoParticellaVO = null;
      StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
      StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = null;
      boolean isErrato = false;
      int countUvValidate = 0;
      if(idDaModificare != null)
      {
        temp = new Vector<StoricoParticellaVO>();
        vDoppi = new Vector<Long>();
        for(int i = 0; i < idDaModificare.length; i++) 
        {
          Long idStorUnitaArborea = new Long(idDaModificare[i]);
          //Controllo che non ci siano di doppi poichè purtoppo il servizio
          //può restituire più record identici
          if(!vDoppi.contains(idStorUnitaArborea))
          {
            storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStorUnitaArborea);
            storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
            if(storicoUnitaArboreaVO.getDataFineValidita() != null || storicoUnitaArboreaVO.getDataCessazione() != null) {
              isErrato = true;
              break;
            }
            // Se il parametro IUVP = S
            if(parametroIUVP.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
            {
              // Verifico che, se valorizzato, lo stato della/e unità arborea/e selezionate
              // non sia incompatibile con il profilo dell'utente che sta cercando di effettuare la modifica
              if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea())) {
                if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())) {
                  countUvValidate++;
                }
              }
            }
            vDoppi.add(idStorUnitaArborea);
            temp.add(storicoParticellaVO);
          }
        }
        
        if(isErrato) 
        {
          String messaggio = "Comune "+storicoParticellaVO.getComuneParticellaVO().getDescom();
          if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
            messaggio += " Sz. " +storicoParticellaVO.getSezione();
          }
          messaggio += " Fgl. "+storicoParticellaVO.getFoglio();
          if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
            messaggio += " Part. " +storicoParticellaVO.getParticella();
          }
          if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
            messaggio += " Sub. " +storicoParticellaVO.getSubalterno();
          }
          error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREA_STORICIZZATA + messaggio);
          request.setAttribute("error", error);
          %>
            <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
          <%
        }
        // Se ho trovato UV già validate dalla PA per l'utente selezionato blocco l'operazione
        else if(countUvValidate > 0) 
        {
          error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_VALIDATE_PA);
          request.setAttribute("error", error);
          %>
            <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
          <%          
        }
        elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
      }
      
      
      HashMap<Long,DatiUvCCIAA> uvImportaDaCCIAA = (HashMap<Long,DatiUvCCIAA>)session.getAttribute("uvImportaDaCCIAA");
      for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
      {
        storicoUnitaArboreaVO = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO();
        DatiUvCCIAA uvCCIAA = uvImportaDaCCIAA.get(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
        //per la tipologia vino, prendo il primo tanto anche nel caso di più elememnti questo campo
        UnitaArboreaCCIAAVO uaCCIAA = uvCCIAA.getVUnitaArboreaCCIAA().get(0); 
        if(uvCCIAA.getSiglaProvincia() != null)
        {
          storicoUnitaArboreaVO.setProvinciaCCIAA(uvCCIAA.getSiglaProvincia());
        }
        
        if(uvCCIAA.getNrMatricola() > 0)
        {
          storicoUnitaArboreaVO.setMatricolaCCIAA(new Integer(uvCCIAA.getNrMatricola()).toString().trim());
        }
        
        if(storicoUnitaArboreaVO.getAnnoIscrizioneAlbo() == null)
        {
          storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(
            storicoUnitaArboreaVO.getAnnoImpianto());
        }        
        storicoUnitaArboreaVO.setIdTipologiaVino(uaCCIAA.getIdTipologiaVino());
        
            
        
        storicoUnitaArboreaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaVO.getArea());
        
        storicoUnitaArboreaVO.setIdCausaleModifica(new Long(7));
        storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
        storicoUnitaArboreaVO.setDataAggiornamento(new Date());
        storicoUnitaArboreaVO.setNoNoteNullable(true);      
        storicoUnitaArboreaVO.setNote("Importazione dati iscrizione albo vigneti" +
                                      " del "+DateUtils.getCurrent());
                                            
                                      
      }
      
      
      
      try 
      {
        anagFacadeClient.modificaUnitaArboree(elencoStoricoParticellaArboreaVO, ruoloUtenza, SolmrConstants.IMPORTA_CCIAA);
      }
      catch(SolmrException se) 
      {
        messaggioErrore = AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
        <%
      }
    
      
      %>
        <html>
          <head>
             <script type="text/javascript">
               window.opener.location.href='../layout/terreniUnitaArboreeElenco.htm';
               window.close();
             </script>
          </head>
        </html>
      <%
      return;
    }


    //Sono arrivato qua dal menu, quindi vado a richiamare il ws di CCIAA
    //per leggere i dati e metterli in session

    //Ricavo i filtri per capire se devo cercare l'unità arborea al piano di
    //riferimento selezionato o al piano attuale
    FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
    String idUnitaCCIAA=(String)session.getAttribute("idUnitaCCIAA");
    session.removeAttribute("idUnitaCCIAA");

    StoricoParticellaVO storicoParticellaVO=null;


    if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().intValue() <= 0)
      storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(new Long(idUnitaCCIAA));
    else
      storicoParticellaVO = anagFacadeClient.findParticellaArboreaDichiarata(new Long(idUnitaCCIAA));

    session.setAttribute("storicoParticellaVOCCIAA",storicoParticellaVO);


    UvResponseCCIAA uvResponseCCIAA =  null;

    try
    {
      uvResponseCCIAA = anagFacadeClient.elencoUnitaVitatePerParticella(storicoParticellaVO.getIstatComune(),
                                                                        storicoParticellaVO.getSezione(),
                                                                        storicoParticellaVO.getFoglio(),
                                                                        storicoParticellaVO.getParticella(),
                                                                        anagAziendaVO.getIdAzienda(),
                                                                        ruoloUtenza);
                                                                        
                                                                        
    }
    catch(SolmrException se)
    {
      request.setAttribute("messaggioErrore",se.getMessage());
      %>
        <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
      <%
      return;
    }

    session.setAttribute("uvResponseCCIAA",uvResponseCCIAA);
    %>
      <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
    <%
  }
  catch(Exception e)
  {
    request.setAttribute("messaggioErrore",e.toString());
    %>
      <jsp:forward page="<%= popUnitaVitateDettaglioCCIAAUrl %>" />
    <%
  }
%>



