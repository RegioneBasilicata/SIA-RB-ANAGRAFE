<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.ws.cciaa.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.comparator.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  String iridePageName = "popUnitaVitateCCIAACtrl.jsp";
  %>
    <%@include file="/include/autorizzazione.inc"%>
  <%
  String popUnitaVitateCCIAAUrl = "/view/popUnitaVitateCCIAAView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/popUnitaVitateCCIAA.htm";
  String excelUrl = "/servlet/ExcelUnitaVitateCUAAServlet";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  ValidationError error = null;
  
  
  WebUtils.removeUselessFilter(session, "uvResponseCCIAA, uvImportaDaCCIAA, filtriUnitaArboreaRicercaVO");

  try
  {

    String operazione = request.getParameter("operazione");
    String messaggioErrore = null;

    // L'utente ha selezionato la voce di menù SIAN e io lo mando alla
    // pagina di attesa per il caricamento dati
    if("attenderePrego".equalsIgnoreCase(operazione))
    {
      session.removeAttribute("uvResponseCCIAA");
      request.setAttribute("action", action);
      operazione = null;
      request.setAttribute("operazione", operazione);
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
        <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
      <%
      return;
    }
    
    
    
    
    
    if("ricerca".equalsIgnoreCase(operazione))
    {
      //Recupero gli eventuali filtri inseriti dall'utente e li metto in sessione
      UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
      uvResponseCCIAA.setFiltroAlbo(request.getParameter("albo"));
      uvResponseCCIAA.setFiltroVitigno(request.getParameter("vitigno"));
      session.setAttribute("uvResponseCCIAA",uvResponseCCIAA);
      %>
        <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
      <%
      return;
    }
    // L'utente ha selezionato l'ordinamento per comune decrescente o ascendente
    if("comuneDesc".equalsIgnoreCase(operazione) || "comuneAsc".equalsIgnoreCase(operazione))
    {
      //Recupero gli eventuali filtri inseriti dall'utente e li metto in sessione
      UvResponseCCIAA uvResponseCCIAA =  (UvResponseCCIAA) session.getAttribute("uvResponseCCIAA");
      uvResponseCCIAA.setOrdComune(operazione);

      //Ordino i dati come richiesto dall'utente
      DatiUvCCIAA datiUvCCIAA[]=uvResponseCCIAA.getDatiUv();

      if("comuneAsc".equalsIgnoreCase(operazione))
        Arrays.sort(datiUvCCIAA,new DatiUvCCIAAComparator(DatiUvCCIAAComparator.ORD_COM_ASC));
      else
        Arrays.sort(datiUvCCIAA,new DatiUvCCIAAComparator(DatiUvCCIAAComparator.ORD_COM_DESC));

      uvResponseCCIAA.setDatiUv(datiUvCCIAA);

      session.setAttribute("uvResponseCCIAA",uvResponseCCIAA);
      %>
        <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
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
        <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
      <%
      return;
    }
    //Controllo se è stato selezionato lo scarico del file excel
    if("excel".equals(request.getParameter("operazione")))
    {
      %>
          <jsp:forward page="<%=excelUrl%>" />
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
      String PARAMETRO_IUVP = null;
      try 
      {
        PARAMETRO_IUVP = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_IUVP);
      }
      catch(SolmrException se) {
        messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_IUVP;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
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
            if(PARAMETRO_IUVP.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
            {
              // Verifico che, se valorizzato, lo stato della/e unità arborea/e selezionate
              // non sia incompatibile con il profilo dell'utente che sta cercando di effettuare la modifica
              if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea())) 
              {
                if((ruoloUtenza.isUtenteIntermediario() 
                  || ruoloUtenza.isUtenteOPRGestore()) 
                  && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())) 
                {
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
            <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
          <%
        }
        // Se ho trovato UV già validate dalla PA per l'utente selezionato blocco l'operazione
        else if(countUvValidate > 0) 
        {
          error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_VALIDATE_PA);
          request.setAttribute("error", error);
          %>
            <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
          <%          
        }
        elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
      }
      
      
      HashMap<Long,DatiUvCCIAA> uvImportaDaCCIAA = (HashMap<Long,DatiUvCCIAA>)session.getAttribute("uvImportaDaCCIAA");
      for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
      {
        storicoUnitaArboreaVO = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO();
        DatiUvCCIAA uvCCIAA = uvImportaDaCCIAA.get(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
        //per la tipologia vino, prendo il primo poichè è per tutti uguale
        UnitaArboreaCCIAAVO uaCCIAA = uvCCIAA.getVUnitaArboreaCCIAA().get(0); 
        if(uvCCIAA.getSiglaProvincia() != null)
        {
          storicoUnitaArboreaVO.setProvinciaCCIAA(uvCCIAA.getSiglaProvincia());
        }
        
        if(uvCCIAA.getNrMatricola() > 0)
        {
          storicoUnitaArboreaVO.setMatricolaCCIAA(new Integer(uvCCIAA.getNrMatricola()).toString());
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
          <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
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
    UvResponseCCIAA uvResponseCCIAA =  null;

    try
    {
      uvResponseCCIAA = anagFacadeClient.elencoUnitaVitatePerCUAA(
        anagAziendaVO.getCUAA(),anagAziendaVO.getIdAzienda(), ruoloUtenza);
    }
    catch(SolmrException se)
    {
      request.setAttribute("messaggioErrore",se.getMessage());
      %>
        <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
      <%
      return;
    }

    session.setAttribute("uvResponseCCIAA",uvResponseCCIAA);
    %>
      <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
    <%
  }
  catch(Exception e)
  {
    request.setAttribute("messaggioErrore",e.toString());
    %>
      <jsp:forward page="<%= popUnitaVitateCCIAAUrl %>" />
    <%
  }
%>



