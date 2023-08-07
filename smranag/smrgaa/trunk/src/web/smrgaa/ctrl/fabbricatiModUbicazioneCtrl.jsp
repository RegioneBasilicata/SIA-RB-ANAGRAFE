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

	String iridePageName = "fabbricatiModUbicazioneCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String fabbricatiModCaratteristicheUrl = "/view/fabbricatiModCaratteristicheView.jsp";
  	String fabbricatiModUbicazioneUrl = "/view/fabbricatiModUbicazioneView.jsp";
  	String fabbricatiCtrlUrl = "/ctrl/fabbricatiCtrl.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
  	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

   	// Leggo tutti gli anni che sono collagati agli allevamenti di
   	// un'azienda e li memorizzo in request per caricarli nella combo
  	Integer anniFabbricati[]= anagFacadeClient.getAnniByIdAzienda(anagAziendaVO.getIdAzienda());

  	request.setAttribute("anniFabbricati", anniFabbricati);

  	int anno = 0;
  	try {
    	anno = ((Integer)session.getAttribute("common")).intValue();
  	}
  	catch(Exception e) {
    	anno = DateUtils.getCurrentYear().intValue();
  	}
  	request.setAttribute("annoFabbricati",""+anno);
  	session.removeAttribute("common");

  	ValidationErrors errors = new ValidationErrors();

  	// L'utente ha premuto il pulsante salva
  	if(request.getParameter("salva") != null) 
  	{
    	// Recupero i parametri
    	String utmx = request.getParameter("utmx");
    	String utmy = request.getParameter("utmy");

    	// Setto i parametri all'interno del VO
    	fabbricatoVO.setUtmx(utmx);
    	fabbricatoVO.setUtmy(utmy);

    	// Effettuo il controllo formale sulla correttezza dei dati
    	boolean obbligoCoordinate = false;
	    if("S".equalsIgnoreCase(fabbricatoVO.getTipoTipologiaFabbricatoVO().getObbligoCoordinate()))
	    {
	      obbligoCoordinate = true;
	    }
    	errors = fabbricatoVO.validateModFabbricatoUbicazione(obbligoCoordinate);

    	// Recupero le particelle associate selezionate
    	Vector<ParticellaVO> elencoParticelleAssociate = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleAssociate");
    	Vector<ParticellaVO> elementiSelezionatiAssociati = new Vector<ParticellaVO>();
    	Vector<ParticellaVO> elementiNoSelezionatiAssociati = new Vector<ParticellaVO>();
    	Vector<ParticellaVO> particelleForFabbricato = new Vector<ParticellaVO>();
    	if(elencoParticelleAssociate != null) 
    	{
     		for(int i = 0; i < elencoParticelleAssociate.size(); i++) 
     		{
       		ParticellaVO particellaAssociataVO = (ParticellaVO)elencoParticelleAssociate.get(i);
       		if(request.getParameter("idStoricoParticellaElimina"+particellaAssociataVO.getIdStoricoParticella()) != null 
       		  || particellaAssociataVO.getDataFineConduzione() != null) 
       		{
         	  particellaAssociataVO.setChecked(true);
         		elementiSelezionatiAssociati.add(particellaAssociataVO);
       		}
       		else 
       		{
         		particellaAssociataVO.setChecked(false);
         		elementiNoSelezionatiAssociati.add(particellaAssociataVO);
         		particelleForFabbricato.add(particellaAssociataVO);
       		}
     		}
    	}

    	// Recupero le particelle associabili selezionate
    	Vector<ParticellaVO> elencoParticelleAssociabili = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleAssociabili");
    	Vector<ParticellaVO> elementiSelezionatiAssociabili = new Vector<ParticellaVO>();
    	if(elencoParticelleAssociabili != null) 
    	{
     		for(int i = 0; i < elencoParticelleAssociabili.size(); i++) 
     		{
       		ParticellaVO particellaAssociabileVO = (ParticellaVO)elencoParticelleAssociabili.get(i);
       		if(request.getParameter("idStoricoParticellaAssocia"+particellaAssociabileVO.getIdStoricoParticella()) != null) 
       		{
         		particellaAssociabileVO.setChecked(true);
         		elementiSelezionatiAssociabili.add(particellaAssociabileVO);
         		particelleForFabbricato.add(particellaAssociabileVO);
       		}
       		else 
       		{
         		particellaAssociabileVO.setChecked(false);
       		}
     		}
    	}
    	
    	
    	if("S".equalsIgnoreCase(fabbricatoVO.getTipoTipologiaFabbricatoVO().getObbligoParticella())) 
      {
        if((elencoParticelleAssociate.size() == elementiSelezionatiAssociati.size())
          && (elementiSelezionatiAssociabili == null || elementiSelezionatiAssociabili.size() == 0)) 
        {
          errors.add("salva",new ValidationError((String)AnagErrors.get("ERR_PARTICELLA_OBBLIGATORIA_PER_MODIFICA_FABBRICATO")));
        }
      }
    	
    	

    	// Se ci sono errori li visualizzo
    	if(errors != null && errors.size() > 0) 
    	{
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(fabbricatiModUbicazioneUrl).forward(request, response);
     		return;
    	}

    	// Se passo tutti i controlli procedo con la modifica
    	try 
    	{
      	anagFacadeClient.modificaFabbricato(fabbricatoVO, particelleForFabbricato, elementiSelezionatiAssociati, elementiSelezionatiAssociabili, ruoloUtenza.getIdUtente(), anagAziendaVO.getIdAzienda().longValue());
    	}
    	catch(SolmrException se) 
    	{
      	errors.add("error", new ValidationError(se.getMessage()));
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(fabbricatiModUbicazioneUrl).forward(request, response);
      	return;
    	}
    	
    	// Torno alla pagina dell'elenco dei terreni
    	%>
       	<jsp:forward page="<%= fabbricatiCtrlUrl %>"/>
    	<%
  	}

  	// L'utente ha premuto il pulsante indietro
  	if(request.getParameter("indietro") != null) 
  	{
    	// Recupero i parametri
    	String utmx = request.getParameter("utmx");
    	String utmy = request.getParameter("utmy");

    	// Setto i parametri all'interno del VO
    	fabbricatoVO.setUtmx(utmx);
    	fabbricatoVO.setUtmy(utmy);

    	// Recupero le particelle associate selezionate
    	Vector<ParticellaVO> elencoParticelleAssociate = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleAssociate");
    	Vector<ParticellaVO> elementiSelezionatiAssociati = new Vector<ParticellaVO>();
    	if(elencoParticelleAssociate != null) 
    	{
      	for(int i = 0; i < elencoParticelleAssociate.size(); i++) 
      	{
        	ParticellaVO particellaAssociataVO = (ParticellaVO)elencoParticelleAssociate.get(i);
        	if(request.getParameter("idStoricoParticellaElimina"+particellaAssociataVO.getIdStoricoParticella()) != null) 
        	{
          	particellaAssociataVO.setChecked(true);
          	elementiSelezionatiAssociati.add(particellaAssociataVO);
        	}
        	else 
        	{
          	particellaAssociataVO.setChecked(false);
        	}
      	}
    	}
    	// Recupero le particelle associabili selezionate
    	Vector<ParticellaVO> elencoParticelleAssociabili = (Vector<ParticellaVO>)session.getAttribute("elencoParticelleAssociabili");
    	Vector<ParticellaVO> elementiSelezionatiAssociabili = new Vector<ParticellaVO>();
    	if(elencoParticelleAssociabili != null) 
    	{
      	for(int i = 0; i < elencoParticelleAssociabili.size(); i++) 
      	{
        	ParticellaVO particellaAssociabileVO = (ParticellaVO)elencoParticelleAssociabili.get(i);
        	if(request.getParameter("idStoricoParticellaAssocia"+particellaAssociabileVO.getIdStoricoParticella()) != null) 
        	{
          	particellaAssociabileVO.setChecked(true);
          	elementiSelezionatiAssociabili.add(particellaAssociabileVO);
        	}
        	else 
        	{
          	particellaAssociabileVO.setChecked(false);
          	elementiSelezionatiAssociabili.add(particellaAssociabileVO);
        	}
      	}
    	}
    	session.setAttribute("elencoParticelleAssociabili", elementiSelezionatiAssociabili);

    	// Torno alla pagina delle caratteristiche fisiche
    	%>
       	<jsp:forward page="<%= fabbricatiModCaratteristicheUrl %>"/>
    	<%
  	}

%>
