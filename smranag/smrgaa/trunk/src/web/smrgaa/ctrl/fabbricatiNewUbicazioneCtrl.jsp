<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "fabbricatiNewUbicazioneCtrl.jsp";

  %><%@include file = "/include/autorizzazione.inc" %><%

  String fabbricatiNewCaratteristicheUrl = "/view/fabbricatiNewCaratteristicheView.jsp";
  String fabbricatiNewUbicazioneUrl = "/view/fabbricatiNewUbicazioneView.jsp";
  String fabbricatiCtrlUrl = "/ctrl/fabbricatiCtrl.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) {
    request.setAttribute("statoAzienda", se);
  }

  /**
   * Leggo tutti gli anni che sono collagati agli allevamenti di
   * un'azienda e li memorizzo in request per caricarli nella combo
   * */
  Integer anniFabbricati[]= anagFacadeClient.getAnniByIdAzienda(anagAziendaVO.getIdAzienda());

  request.setAttribute("anniFabbricati", anniFabbricati);

  int anno=0;
  try
  {
    anno= ((Integer)session.getAttribute("common")).intValue();
  }
  catch(Exception e)
  {
    anno=DateUtils.getCurrentYear().intValue();
  }
  request.setAttribute("annoFabbricati",""+anno);
  session.removeAttribute("common");


  ValidationErrors errors = new ValidationErrors();

  // L'utente ha cliccato il pulsante "particelle a fabbricato"
  if (request.getParameter("operazione") != null 
    && request.getParameter("operazione").equalsIgnoreCase("ricercaParticellaPerFabbricato"))
  {

    // Recupero i parametri
    String utmx = request.getParameter("utmx");
    String utmy = request.getParameter("utmy");
    String idUnitaProduttivaFabbricato = request.getParameter("idUnitaProduttiva");

    // Setto i parametri all'interno del VO
    fabbricatoVO.setUtmx(utmx);
    fabbricatoVO.setUtmy(utmy);

    // Controllo che l'utente abbia selezionato l'unita produttiva su cui ricercare le
    // particelle ad utilizzo fabbricato
    if(!Validator.isNotEmpty(idUnitaProduttivaFabbricato)) {
      errors.add("idUnitaProduttiva", new ValidationError((String)AnagErrors.get("ERR_UNITA_PRODUTTIVA_OBBLIGATORIA_PER_FABBRICATO")));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(fabbricatiNewUbicazioneUrl).forward(request, response);
      return;
    }
    else {
      fabbricatoVO.setIdUnitaProduttivaFabbricato(Long.decode(idUnitaProduttivaFabbricato));
    }

    // Se l'utente l'ha selezionata ricerca le particelle relative all'ute selezionata
    Vector elencoParticelleFabbricato = null;
    try
    {
      if(fabbricatoVO.getIdTipologiaFabbricato().equals((Long)SolmrConstants.get("TIPO_TIPOLOGIA_FABBRICATO_SERRA"))
      || fabbricatoVO.getIdTipologiaFabbricato().equals((Long)SolmrConstants.get("TIPO_TIPOLOGIA_FABBRICATO_SERRA_RISCALDATA")))
      {
        //SERRA
        elencoParticelleFabbricato = anagFacadeClient.getElencoParticelleFabbricatoByUte(fabbricatoVO.getIdUnitaProduttivaFabbricato(),true);
      }
      else
      {
        elencoParticelleFabbricato = anagFacadeClient.getElencoParticelleFabbricatoByUte(fabbricatoVO.getIdUnitaProduttivaFabbricato(),false);
      }
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(fabbricatiNewUbicazioneUrl).forward(request, response);
      return;
    }
    if(elencoParticelleFabbricato == null || elencoParticelleFabbricato.size() == 0) {
      String messaggio = "errore";
      session.setAttribute("messaggio",messaggio);
    }
    // Metto il vettore in session
    session.setAttribute("elencoParticelleFabbricato",elencoParticelleFabbricato);

    // Ritorno alla pagina di inserimento ubicazione
    %>
       <jsp:forward page="<%= fabbricatiNewUbicazioneUrl %>"/>
    <%

  }

  // L'utente ha premuto il pulsante salva
  if(request.getParameter("salva") != null) 
  {

    // Recupero i parametri
    String utmx = request.getParameter("utmx");
    String utmy = request.getParameter("utmy");
    String idUnitaProduttivaFabbricato = request.getParameter("idUnitaProduttiva");

    // Setto i parametri all'interno del VO
    fabbricatoVO.setUtmx(utmx);
    fabbricatoVO.setUtmy(utmy);
    if(Validator.isNotEmpty(idUnitaProduttivaFabbricato)) {
      fabbricatoVO.setIdUnitaProduttivaFabbricato(Long.decode(idUnitaProduttivaFabbricato));
    }
    
    boolean obbligoCoordinate = false;
    if("S".equalsIgnoreCase(fabbricatoVO.getTipoTipologiaFabbricatoVO().getObbligoCoordinate()))
    {
      obbligoCoordinate = true;
    }
    // Effettuo il controllo formale sulla correttezza dei dati
    errors = fabbricatoVO.validateInsFabbricatoUbicazione(obbligoCoordinate);

    // Recupero le particelle selezionate
    Vector<ParticellaVO> elencoParticelle = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleFabbricato");

    Vector<ParticellaVO> elementiSelezionati = new Vector<ParticellaVO>();
    if(elencoParticelle != null)
    {
      for(int i= 0;i<elencoParticelle.size(); i++)
      {
        ParticellaVO particellaFabbricatoVO = (ParticellaVO)elencoParticelle.get(i);
        if(request.getParameter("idStoricoParticella"+particellaFabbricatoVO.getIdStoricoParticella()) != null) 
        {
          particellaFabbricatoVO.setChecked(true);
          elementiSelezionati.add(particellaFabbricatoVO);
        }
        else 
        {
          particellaFabbricatoVO.setChecked(false);
        }
      }
    }
    
    if((elementiSelezionati.size() == 0)
      && "S".equalsIgnoreCase(fabbricatoVO.getTipoTipologiaFabbricatoVO().getObbligoParticella()))
    {
      errors.add("salva",new ValidationError(AnagErrors.ERRORE_PARTICELLA_ASS_DOCUMENTO_KO));
    }
    

    

    // Se ci sono errori li visualizzo
    if(errors != null && errors.size() > 0) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(fabbricatiNewUbicazioneUrl).forward(request, response);
      return;
    }

    // Se passo tutti i controlli procedo con l'inserimento
    try 
    {
    	anagFacadeClient.inserisciFabbricato(fabbricatoVO, elementiSelezionati, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) {
    	errors.add("error", new ValidationError(se.getMessage()));
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(fabbricatiNewUbicazioneUrl).forward(request, response);
      	return;
    }

    //Rimuovo i bean dalla sesione
    session.removeAttribute("fabbricatoVO");
    session.removeAttribute("messaggio");
    session.removeAttribute("elencoParticelleFabbricato");
    %>
       <jsp:forward page="<%= fabbricatiCtrlUrl %>"/>
    <%
  }

  // L'utente ha premuto il pulsante indietro
  if(request.getParameter("indietro") != null) {

    // Recupero i parametri
    String utmx = request.getParameter("utmx");
    String utmy = request.getParameter("utmy");
    String idUnitaProduttivaFabbricato = request.getParameter("idUnitaProduttiva");

    // Setto i parametri all'interno del VO
    fabbricatoVO.setUtmx(utmx);
    fabbricatoVO.setUtmy(utmy);
    if(Validator.isNotEmpty(idUnitaProduttivaFabbricato)) {
      fabbricatoVO.setIdUnitaProduttivaFabbricato(Long.decode(idUnitaProduttivaFabbricato));
    }

    // Recupero le particelle selezionate
    Vector elencoParticelle = (Vector)session.getAttribute("elencoParticelleFabbricato");

    Vector elementi = new Vector();
    //double supDichiarateSelezionate = 0;
    if(elencoParticelle != null)
    {
      for(int i= 0;i<elencoParticelle.size(); i++)
      {
        ParticellaVO particellaFabbricatoVO = (ParticellaVO)elencoParticelle.get(i);
        if(request.getParameter("idStoricoParticella"+particellaFabbricatoVO.getIdStoricoParticella()) != null)
          particellaFabbricatoVO.setChecked(true);
        else    particellaFabbricatoVO.setChecked(false);
        elementi.add(particellaFabbricatoVO);
      }
    }
    session.setAttribute("elencoParticelleFabbricato",elementi);

    // Torno alla pagina delle caratteristiche fisiche
    %>
       <jsp:forward page="<%= fabbricatiNewCaratteristicheUrl %>"/>
    <%
  }

%>
