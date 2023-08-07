<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>

<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.anag.ParticellaCertElegVO" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	
	String iridePageName = "unitaArboreeModificaMultiplaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeModificaMultiplaCtrl.jsp - INIZIO PAGINA");
  
  final String errMsg = "Impossibile procedere nella sezione modifica multipla Unità Arboree."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	String unitaArboreeModificaMultiplaUrl = "/view/unitaArboreeModificaMultiplaView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
	ValidationError error = null;
  String istatComune="";
	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
	String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
  int countErrori=0;
  int countErroriMultipli=0;
	if(Validator.isNotEmpty(pagina)) {
		session.setAttribute("pagina", pagina);
	}
	Vector<Long> elencoIdDaModificare = new Vector<Long>();
	Vector<StoricoParticellaVO> temp = null;
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	// Rimuovo dalla sessione il numero dell'UV selezionata in precedenza per modificare i dati da pop-up
	session.removeAttribute("numero");
  
  boolean funzioneCambia=false;
  boolean funzioneTipologiaVino=false;
  boolean funzioneDataImpianto=false;
  boolean funzioneVigna=false;
  boolean funzioneAnnotazioneEtichetta=false;
  boolean funzioneAllineaUVGIS=false;
  if ("cambia".equals(request.getParameter("funzioneCambia"))) funzioneCambia=true;
  if ("tipologiaVino".equals(request.getParameter("funzioneTipologiaVino"))) funzioneTipologiaVino=true;
  if ("dataImpianto".equals(request.getParameter("funzioneDataImpianto"))) funzioneDataImpianto=true;
  if ("vigna".equals(request.getParameter("funzioneVigna"))) funzioneVigna=true;
  if ("annotazioneEtichetta".equals(request.getParameter("funzioneAnnotazioneEtichetta"))) funzioneAnnotazioneEtichetta=true;
  if ("allineaUVGIS".equals(request.getParameter("funzioneAllineaUVGIS"))) funzioneAllineaUVGIS=true;
	
	// Controllo che siano stati selezionati degli elementi dall'elenco
	if((numUnitaArboreeSelezionate == null || numUnitaArboreeSelezionate.size() == 0) && (idStoricoUnitaArborea == null 
    || idStoricoUnitaArborea.length == 0)) 
  {
		error = new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
  
	// Gestione della selezione delle unità arboree
	Vector<Long> elenco = new Vector<Long>();
	if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) 
	{
		numUnitaArboreeSelezionate.remove(pagina);
		if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) 
		{
			numUnitaArboreeSelezionate.remove(pagina);
			for(int i = 0; i < idStoricoUnitaArborea.length; i++) 
			{
				elenco.add(Long.decode((String)idStoricoUnitaArborea[i]));
			}
		}
	}
	else 
	{
		if(numUnitaArboreeSelezionate == null) 
		{
			numUnitaArboreeSelezionate = new HashMap<String,Vector<Long>>();
		}
		// Se non ho selezionato il pulsante annulla
		if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) 
		{
			for(int i = 0; i < idStoricoUnitaArborea.length; i++) 
			{
				Long idElemento = Long.decode((String)idStoricoUnitaArborea[i]);
				elenco.add(idElemento);
			}
		}
	}
	if(elenco.size() > 0) 
	{
		numUnitaArboreeSelezionate.put(pagina, elenco);
		session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
	}
	
	// Recupero il parametro che mi indica il numero massimo di record selezionabili
	String parametroRUVM = null;
	try 
  {
		parametroRUVM = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RUVM);
	}
	catch(SolmrException se) 
  {    
    SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RUVM+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
  
  // Recupero il parametro che mi indica l'obbligatorietà di un campo o meno
  boolean datiObbligatori=false;
  try 
  {
    String parametroAlbo = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_ALBO);;
    if (SolmrConstants.FLAG_S.equals(parametroAlbo)) datiObbligatori=true;
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ALBO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  String parametroDAID = null;
  try 
  {
    parametroDAID = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DAID);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DAID+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  // Recupero i dati della compensazione azienale
  /*CompensazioneAziendaVO compensazioneAziendaVO = null;
  try 
  {
    compensazioneAziendaVO = gaaFacadeClient.getCompensazioneAzienda(anagAziendaVO.getIdAzienda().longValue());
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_COMPENSAZIONE_AZIENDA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  boolean flagTolleranzaCompensazione = true;
  if(Validator.isNotEmpty(compensazioneAziendaVO)
    && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
  {
    flagTolleranzaCompensazione = false;
  }*/
  
   
	
	// Verifico che non siano state selezionate più particelle rispetto a quelle consentite
	Set<String> elencoKeys = numUnitaArboreeSelezionate.keySet();
	Iterator<String> iteraKey = elencoKeys.iterator();
	while(iteraKey.hasNext())
	{
		Vector<Long> selezioni = (Vector<Long>)numUnitaArboreeSelezionate.get((String)iteraKey.next());
		if(selezioni != null && selezioni.size() > 0) 
		{
			for(int a = 0; a < selezioni.size(); a++) 
			{
				elencoIdDaModificare.add((Long)selezioni.elementAt(a));
			}
		}
	}
	if(elencoIdDaModificare.size() > Integer.parseInt(parametroRUVM)) 
	{
		error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRUVM+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
	
	// L'utente ha cliccato il pulsante annulla
 	if(request.getParameter("annulla") != null) 
  {
 		// Torno alla pagina di ricerca/elenco
 	 	String valorePagina = (String)session.getAttribute("pagina");
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" >
				<jsp:param name="pagina" value="<%= valorePagina %>" /> 
			</jsp:forward>
		<%
 	}
 	else 
  {    
    String parametroAccessoIdoneita = null;
    try 
    {
      parametroAccessoIdoneita = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_IDONEITA);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_IDONEITA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoIdoneita", parametroAccessoIdoneita);
    
    String parametroAccessoVarArea = null;
    try 
    {
      parametroAccessoVarArea = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_VAR_AREA);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_IDONEITA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoVarArea", parametroAccessoVarArea);
    
    
    String parametroAccessoAltriDati = null;
    try 
    {
      parametroAccessoAltriDati = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_ALTRI_DATI);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_ALTRI_DATI+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoAltriDati", parametroAccessoAltriDati);
    
    String parametroAccessoAllineaGis = null;
    try 
    {
      parametroAccessoAllineaGis = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_ALLINEA_GIS);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_ALLINEA_GIS+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoAllineaGis", parametroAccessoAllineaGis);
    
    
    
    String parametroOtherUVP = null;
	  try 
	  {
	    parametroOtherUVP = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OTHER_UVP);
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OTHER_UVP+".\n"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
	  request.setAttribute("parametroOtherUVP", parametroOtherUVP);
	  
	  
	  String parametroLockUvConsolidate = null;
    try 
    {
      parametroLockUvConsolidate = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_LOCK_UV_CONSOLIDATE);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_LOCK_UV_CONSOLIDATE+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    
 				
		// Verifico che tra le unità arboree selezionate dall'utente non ve ne sia nessuna
		// con data_fine_validita valorizzata
		StoricoParticellaVO storicoParticellaVO = null;
		StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
		StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
		if(elencoStoricoParticellaArboreaVO == null) 
    {
			boolean isErrato = false;
			int countUvValidate = 0;
			int countUvConsolidate = 0;
			try 
      {
				temp = new Vector<StoricoParticellaVO>();
				Long idStorUnitaArborea = null;
        
        //variabili per la sup post vit
        //mi dice se sono state selezionate tutte le uv della particella
        String noTutteUvAllGis = "";
        Long idParticellaUvAllGis = new Long(0);
        BigDecimal superficieGis = new BigDecimal(0);
        BigDecimal sommaAreaUV = new BigDecimal(0);
        BigDecimal sommaAreaUVAggiornate = new BigDecimal(0);
        int numUVParticella = 0;
        int posUv = 0;
        boolean primoGiro = true;
        boolean flagNoTutteUvAllGis = false;
         
				for(int i = 0; i < elencoIdDaModificare.size(); i++) 
        {
					idStorUnitaArborea = (Long)elencoIdDaModificare.elementAt(i);
					storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStorUnitaArborea);
					storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
          
          
          
          if(storicoParticellaVO.getIdParticella().compareTo(idParticellaUvAllGis) != 0)
          {
          
            if(primoGiro)
            {
              primoGiro = false;
            }
            else
            {
              //Se valida la condizione vuol dire che non sono
              //state selezionate tutte le uv della particella
              //non è possibile andare avanti
              if(posUv != numUVParticella)
              {
                flagNoTutteUvAllGis = true;
              }
            }
            //Vector<ParticellaCertElegVO> vPartCertEleg = anagFacadeClient
              //.getEleggibilitaByIdParticella(storicoParticellaVO.getIdParticella().longValue());            
            numUVParticella = anagFacadeClient.getNumUVParticella(
                  anagAziendaVO.getIdAzienda().longValue(), 
                  storicoParticellaVO.getIdParticella().longValue());
            posUv = 0;
            sommaAreaUV = new BigDecimal(0);
            sommaAreaUVAggiornate = new BigDecimal(0);
            superficieGis = new BigDecimal(0);
            if((storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg() != null)
              && (storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg()
                .get(0).getSuperficie() != null))
            {
              superficieGis = storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg()
                .get(0).getSuperficie();
              if((superficieGis != null)
                && (superficieGis.compareTo(new BigDecimal(0)) > 0))
              {
                
                BigDecimal percentualePossesso = gaaFacadeClient
                  .getPercUtilizzoEleggibile(anagAziendaVO.getIdAzienda().longValue(), storicoParticellaVO
                  .getIdParticella().longValue());
                
                if(percentualePossesso.compareTo(new BigDecimal(0)) == 0)
                {
                  percentualePossesso = anagFacadeClient
                    .getPercentualePosesso(anagAziendaVO.getIdAzienda().longValue(), 
                    storicoParticellaVO.getIdParticella().longValue());
                }
                
                
                if(percentualePossesso.compareTo(new BigDecimal(100)) > 0)
		            {
		              percentualePossesso = new BigDecimal(100);
		            }
                
                superficieGis = superficieGis.divide(new BigDecimal(100));
                superficieGis = superficieGis.multiply(percentualePossesso);
                
                sommaAreaUV = anagFacadeClient.getSumAreaUVParticella(
                  anagAziendaVO.getIdAzienda().longValue(), 
                  storicoParticellaVO.getIdParticella().longValue());		          
              }
            }
          }
          
          
          if((superficieGis != null)
                && (superficieGis.compareTo(new BigDecimal(0)) > 0))
          {
            BigDecimal areaUV = new BigDecimal(storicoUnitaArboreaVO.getArea().replace(',','.'));
            //riprorziono l'area della UV                
            areaUV = areaUV.multiply(superficieGis);
            areaUV = areaUV.divide(sommaAreaUV,4,BigDecimal.ROUND_HALF_UP);
            sommaAreaUVAggiornate = sommaAreaUVAggiornate.add(areaUV);
            //raccolgo gli sfrigu persi
            if(posUv == (numUVParticella - 1))
            {
              if(superficieGis.compareTo(sommaAreaUVAggiornate) > 0)
              {
                superficieGis = superficieGis.subtract(sommaAreaUVAggiornate);
                areaUV = areaUV.add(superficieGis);
              }
            }
            storicoUnitaArboreaVO.setSupPostVit(StringUtils.parseSuperficieFieldBigDecimal(areaUV));
          }
          else
          {
            storicoUnitaArboreaVO.setSupPostVit(SolmrConstants.DEFAULT_SUPERFICIE);
          }         
          
          posUv++;
          idParticellaUvAllGis = storicoParticellaVO.getIdParticella();          
          
          
          
          
          
          
          
          //Se almeno un campo è valorizzato nel
          //caso sono intermediario la l'uv è validata non posso modificare
          //usato per bloccare idCausale
          if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
           && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(
             storicoUnitaArboreaVO.getStatoUnitaArborea()))
          {
            //Si possono modificare solo i dati dell'idoneità
            if(parametroAccessoIdoneita.equalsIgnoreCase(
              SolmrConstants.MODIFICA_UV_PARAMETRO_TUTTO))
            {
              if((storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null) 
                  || (storicoUnitaArboreaVO.getAnnoIscrizioneAlbo() !=null)
                  || (storicoUnitaArboreaVO.getMatricolaCCIAA() != null))
              {
                storicoUnitaArboreaVO.setBloccaModificaIdoneitaValida(true);
                //Se questo parametro è a S devo permettere cmq la modifica di
                //allinea uv gis, vigna, annotazione in etichetta
                /*if("N".equalsIgnoreCase(parametroOtherUVP))
                {
                  countUvValidate++;
                }*/
              }
            }
          }
          
          if("S".equalsIgnoreCase(parametroLockUvConsolidate))
          {
            if(storicoUnitaArboreaVO.getDataConsolidamentoGis() != null)
            {
              countUvConsolidate++;
            }
          }
          
          
					if(storicoUnitaArboreaVO.getDataFineValidita() != null || storicoUnitaArboreaVO.getDataCessazione() != null) 
          {
						isErrato = true;
						break;
					}
          
          //devo controllare che tutte le unità selezionate siano sullo stesso comune
          if (i>0 && !istatComune.equals(storicoParticellaVO.getIstatComune()))
          {
            error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_MULTIPLA_UNITA_VITATE_COMUNI);
            request.setAttribute("error", error);
            request.setAttribute("erroreModificaUv", SolmrConstants.FLAG_S);
            %>
              <jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
            <%    
            return;
          }
          
          istatComune=storicoParticellaVO.getIstatComune();
          
          
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea()))
          { 
            if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) 
              && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea()))
            { 
					    if((parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
                  || parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
                && (parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
                   || parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
                && (parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
                   || parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
                && (parametroAccessoAllineaGis.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
                   || parametroAccessoAllineaGis.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) )  
              {
								countUvValidate++;
						  }
						}
					}
					
					temp.add(storicoParticellaVO);
				}
        
        
        if(posUv != numUVParticella)
        {
          flagNoTutteUvAllGis = true;
        }
        
        if(flagNoTutteUvAllGis)
        {
          session.setAttribute("noTutteUvAllGis","true");
        }
        else
        {
          session.removeAttribute("noTutteUvAllGis");
        }
        
        
				if(isErrato) 
        {
					String messaggio = "Comune "+storicoParticellaVO.getComuneParticellaVO().getDescom();
					if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
					{
						messaggio += " Sz. " +storicoParticellaVO.getSezione();
					}
					messaggio += " Fgl. "+storicoParticellaVO.getFoglio();
					if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
					{
						messaggio += " Part. " +storicoParticellaVO.getParticella();
					}
					if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) 
					{
						messaggio += " Sub. " +storicoParticellaVO.getSubalterno();
					}
					error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREA_STORICIZZATA + messaggio);
					request.setAttribute("error", error);
					%>
						<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
					<%
				}
				// Se ho trovato UV consolidate
        else if((countUvConsolidate > 0) && !ruoloUtenza.isUtentePA()) 
        {
          error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_CONSOLIDATE);
          request.setAttribute("error", error);
          request.setAttribute("erroreModificaUv", SolmrConstants.FLAG_S);
          %>
            <jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
          <%          
        }
				// Se ho trovato UV già validate dalla PA per l'utente selezionato blocco l'operazione
				else if(countUvValidate > 0) 
        {
					error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_VALIDATE_PA);
					request.setAttribute("error", error);
					request.setAttribute("erroreModificaUv", SolmrConstants.FLAG_S);
					%>
						<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
					<%					
				}
				elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
        
			}
			catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_UNAR+".\n"+se.toString();
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
      //Recupero istat comune
      istatComune=elencoStoricoParticellaArboreaVO[0].getIstatComune();
    }
		session.setAttribute("elencoStoricoParticellaArboreaVO", elencoStoricoParticellaArboreaVO);
    
    //mi ricavo gli idParticella per vedere se sono associte pratiche all'uv
    Vector<Long> vIdParticella = null;
    if(Validator.isNotEmpty(elencoStoricoParticellaArboreaVO))
    {
      for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
      {
        if(vIdParticella == null)
        {
          vIdParticella = new Vector<Long>();
        }
        StoricoParticellaVO storicoParticellaVOTmp = elencoStoricoParticellaArboreaVO[i];
        if(!vIdParticella.contains(storicoParticellaVOTmp.getIdParticella()))
        {
          vIdParticella.add(storicoParticellaVOTmp.getIdParticella());
        }
      }
    }
    
    
    //mi ricavo gli idStoricoUnitaArborea per vedere se esiste una o piu' UV 
    // modificate/inserite dal procedimento VITI
    Vector<Long> vIdStoricoUvModVITI = null;
    if(Validator.isNotEmpty(elencoStoricoParticellaArboreaVO))
    {
      for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
      {
        StoricoUnitaArboreaVO storicoUnitaArboreaVOTmp = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO();
        if("S".equalsIgnoreCase(storicoUnitaArboreaVOTmp.getTipoCausaleModificaVO().getAltroProcedimento()))
        {
          if(vIdStoricoUvModVITI == null)
          {
            vIdStoricoUvModVITI = new Vector<Long>();
          }
                    
          vIdStoricoUvModVITI.add(storicoUnitaArboreaVOTmp.getIdStoricoUnitaArborea());
        }
      }
    }
    
    
    
    
    
    
		
		// Recupero i valori relativi alla destinazione produttiva
		TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
 		try 
    {
			String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
	 		elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_VIGNETO, true, orderBy);
	 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
 		}
 		catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
 		}
    
    //Lettura dei parametri impostati dall'utente
    String idTipoUtilizzo=request.getParameter("idTipoUtilizzo");
    String idTipoDestinazione=request.getParameter("idTipoDestinazione");
    String idTipoDettaglioUso=request.getParameter("idTipoDettaglioUso");
    String idTipoQualitaUso=request.getParameter("idTipoQualitaUso");
    String idVarieta=request.getParameter("idVarieta");
    String idTipologiaVino=request.getParameter("idTipologiaVino");
    String matricolaCCIAA=request.getParameter("matricolaCCIAA");
    String annoIscrizioneAlbo=request.getParameter("annoIscrizioneAlbo");    
	 	String dataImpiantoText=request.getParameter("dataImpiantoText");
    Date dataImpianto = null;
    String dataPrimaProduzioneText=request.getParameter("dataPrimaProduzioneText");
    Date dataPrimaProduzione = null;
    String vignaText=request.getParameter("vignaText");
    String annotazioneEtichettaText=request.getParameter("annotazioneEtichettaText");
    Long idTipologiaVinoLg = null;
    
    
    
    
    
    
    // Destinazione PRIMARIO
	  Vector<TipoDestinazioneVO> vTipoDestinazione = null;
	  if(Validator.isNotEmpty(idTipoUtilizzo)) 
	  {
	    try 
	    {      
	      vTipoDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(new Long(idTipoUtilizzo));
	      request.setAttribute("vTipoDestinazione", vTipoDestinazione);      
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
	      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DEST_USO_PRIMARIA+".\n"+se.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
	  }
	  
	  // Dettaglio Uso PRIMARIO
	  Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
	  if(Validator.isNotEmpty(idTipoDestinazione)) 
	  {
	    try 
	    {      
	      vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(new Long(idTipoUtilizzo), new Long(idTipoDestinazione));
	      request.setAttribute("vTipoDettaglioUso", vTipoDettaglioUso);      
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
	      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_USO_PRIMARIO+".\n"+se.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
	  }
	  
	  // Qualita Uso PRIMARIO
	  Vector<TipoQualitaUsoVO> vTipoQualitaUso = null;
	  if(Validator.isNotEmpty(idTipoDettaglioUso)) 
	  {
	    try 
	    {      
	      vTipoQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(new Long(idTipoUtilizzo), 
	        new Long(idTipoDestinazione), new Long(idTipoDettaglioUso));
	      request.setAttribute("vTipoQualitaUso", vTipoQualitaUso);      
	    }
	    catch(SolmrException se) 
	    {
	      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
	      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIO+".\n"+se.toString();
	      request.setAttribute("messaggioErrore",messaggio);
	      request.setAttribute("pageBack", actionUrl);
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;
	    }
	  }
	  
	  // VARIETA' PRIMARIA
	  Vector<TipoVarietaVO> elencoTipoVarieta = null;
	  if(Validator.isNotEmpty(idTipoQualitaUso)) 
	  {
      try 
      {
        elencoTipoVarieta = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(
          new Long(idTipoUtilizzo), 
          new Long(idTipoDestinazione), 
          new Long(idTipoDettaglioUso), 
          new Long(idTipoQualitaUso), 
          istatComune);  
        // Nel caso in cui non ci siano legami e quindi non vengano estratti
        // records, recupero il vitigno solo in relazione all'utilizzo selezionato
        if(elencoTipoVarieta == null)
        {
          elencoTipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
            new Long(idTipoUtilizzo),
            new Long(idTipoDestinazione), 
            new Long(idTipoDettaglioUso), 
            new Long(idTipoQualitaUso));
        }
        request.setAttribute("elencoTipoVarieta", elencoTipoVarieta);     
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VITIGNO+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
	  }
    
    
    
    
    
    
    // Recupero i valori relativi alla tipologia di vino
    Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = null;
    String vinoDoc = null;
    try 
    {
      vTipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComune(istatComune);
      request.setAttribute("vTipoTipologiaVino", vTipoTipologiaVino);
      //Tipologia di vino
      if(vTipoTipologiaVino != null)
      { 
        for(TipoTipologiaVinoVO tipoTipologiaVinoVO:vTipoTipologiaVino)
        {
          if(Validator.isNotEmpty(idTipologiaVino) && tipoTipologiaVinoVO.getIdTipologiaVino()!=null && idTipologiaVino.equals(tipoTipologiaVinoVO.getIdTipologiaVino().toString()))
          {
            vinoDoc=tipoTipologiaVinoVO.getVinoDoc();
            break;
          }   
        }
      }
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_VINO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    
    // Recupero il vitigno (per l'elenco delle particelle) in funzione della destinazione produttiva selezionata
    Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer, Vector<TipoVarietaVO>>();
    try 
    {
      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
        if((storico.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)
          && (storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)
          && (storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso() != null)
          && (storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso() != null))   
        {
          String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
          Vector<TipoVarietaVO> tipoVarieta = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(
            storico.getStoricoUnitaArboreaVO().getIdUtilizzo(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso(), 
		        storico.getIstatComune());
		        
          // Nel caso in cui non ci siano legami e quindi non vengano estratti
          // records, recupero il vitigno solo in relazione all'utilizzo selezionato
          if(tipoVarieta == null) 
          {
            tipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
		          storico.getStoricoUnitaArboreaVO().getIdUtilizzo(),
		          storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
		          storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
		          storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso());
          }
          elencoVarieta.put(new Integer(i), tipoVarieta);
        }
        else 
        {
          elencoVarieta.put(new Integer(i), new Vector<TipoVarietaVO>());
        }
      }
      request.setAttribute("elencoVarieta", elencoVarieta);     
    }
    catch(SolmrException se) 
    {     
      SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VITIGNO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
	 	
	 	
		// L'utente ha modificato il valore della destinazione produttiva
	 	/*if(Validator.isNotEmpty(request.getParameter("cambio"))) 
    {
      //e' stata modificata la combo Dest. prod.
	 	}*/
	 	// L'utente ha cliccato il pulsante conferma
	 	if((request.getParameter("conferma") != null)
	 	  || (request.getParameter("confermaPA") != null))
    {
      //se settato anche se trattasi di PA non deve essere fatta la validazione
      String provenienza = null;
      if(request.getParameter("confermaPA") != null)
      {
        provenienza = SolmrConstants.CONFERMA_PA;
      } 
    
	 		Vector<ValidationErrors> elencoErroriMultipli = new Vector<ValidationErrors>();
      ValidationErrors elencoErrori = new ValidationErrors();
      
      //Controllo che l'utente abbia selezionato almeno una voce da modificare
      if(!(funzioneCambia||funzioneTipologiaVino||funzioneDataImpianto||funzioneVigna||funzioneAnnotazioneEtichetta||funzioneAllineaUVGIS)) 
      {
        elencoErrori.add("vociCheckBox", new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT));
        countErrori++;
      }
      
      // Setto e verifico tutti i parametri inseriti a seconda dell'opzione scelta dall'utente:
      if(funzioneCambia)
      {
        // DESTINAZIONE PRODUTTIVA
        if(!Validator.isNotEmpty(idTipoUtilizzo))
        {
          elencoErrori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        if(!Validator.isNotEmpty(idTipoDestinazione))
        {
          elencoErrori.add("idTipoDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        if(!Validator.isNotEmpty(idTipoDettaglioUso))
        {
          elencoErrori.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        if(!Validator.isNotEmpty(idTipoQualitaUso))
        {
          elencoErrori.add("idTipoQualitaUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        // VITIGNO
        boolean flagVitigno = true;
        if(Validator.isEmpty(idVarieta)) 
        {
          flagVitigno = false;
          elencoErrori.add("idVarieta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        
        
        
        if(flagVitigno)
        {
	        for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
	        {
	          StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	          StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
	          ValidationErrors errors = new ValidationErrors();
	              
	          // Data sovrainnesto
	          if(storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length > 0) 
	          {
	            for(int j=0;j<storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length;j++)
	            {
	              if(storicoUnitaArboreaConfermaVO.getElencoAltriVitigni()[j].getIdVarieta()
	               .compareTo(new Long(idVarieta)) == 0)
	              {
	                errors.add("generico", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
                  countErroriMultipli++;
                  break;
	              }	              
	            }
	            
	          }
	            
	          elencoErroriMultipli.add(errors);
	        
	        }
	      }
        
        if(countErroriMultipli > 0)
        {
          elencoErrori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
          countErrori++;
          elencoErrori.add("idTipoDestinazione", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
          countErrori++;
          elencoErrori.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
          countErrori++;
          elencoErrori.add("idTipoQualitaUso", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
          countErrori++;
          elencoErrori.add("idVarieta", new ValidationError(AnagErrors.ERRORE_ALTRO_VITIGNO_PRESENTE));
          countErrori++;
        }
        
        
        if(countErrori == 0)
        {
          CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(new Long(request.getParameter("idTipoUtilizzo")),
                new Long(request.getParameter("idVarieta")), new Long(request.getParameter("idTipoDestinazione")), 
                new Long(request.getParameter("idTipoDettaglioUso")), 
                new Long(request.getParameter("idTipoQualitaUso")));
                        
          if(ruoloUtenza.isUtenteIntermediario() && Validator.isNotEmpty(catalogoMatriceVO))
          {
            if("N".equalsIgnoreCase(catalogoMatriceVO.getFlagUnarModificabile()))
            {
              elencoErrori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
		          countErrori++;
		          elencoErrori.add("idTipoDestinazione", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
		          countErrori++;
		          elencoErrori.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
		          countErrori++;
		          elencoErrori.add("idTipoQualitaUso", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
		          countErrori++;
		          elencoErrori.add("idVarieta", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
		          countErrori++;
            }
          }
        
        }
        
        
      }
      
      
      if (funzioneDataImpianto)
      {
        boolean isOKannoImpianto = false;
        boolean isOKdataPrimaProduzione = false;        
        if(Validator.isNotEmpty(dataImpiantoText)) 
        {
          if(!Validator.validateDateF(dataImpiantoText)) 
          {
            elencoErrori.add("dataImpiantoText", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            countErrori++;
          }
          else 
          {
            
            dataImpianto = DateUtils.parseDate(dataImpiantoText);
            // La data impianto non può essere maggiore di quella di sistema 
            if(dataImpianto.after(new Date())) 
            {
              elencoErrori.add("dataImpiantoText", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MAGGIORE_DATA_CORRENTE));
              countErrori++;
            }
            // L'anno impianto non deve essere minore del 1900
            else if(dataImpianto.before(DateUtils.parseDate("31/12/1900")))  
            {
              elencoErrori.add("dataImpiantoText", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MINORE_1900));
              countErrori++;
            }
            else
            {
              isOKannoImpianto = true;
            }
          }
        }
        else 
        {
          elencoErrori.add("dataImpiantoText", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        
        
        if(Validator.isNotEmpty(dataPrimaProduzioneText)) 
        {
          if(!Validator.validateDateF(dataPrimaProduzioneText)) 
          {
            elencoErrori.add("dataPrimaProduzioneText", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            countErrori++;
          }
          else
          {
            dataPrimaProduzione = DateUtils.parseDate(dataPrimaProduzioneText);
            
            if(dataPrimaProduzione.before(DateUtils.parseDate("31/12/1900")))
            {
              elencoErrori.add("dataPrimaProduzioneText", new ValidationError(AnagErrors.ERRORE_KO_DATA_PRIMA_PRODUZIONE_UV_MINORE_1900));
              countErrori++;
            }
            else if(isOKannoImpianto) 
            {
              int yearDataImpianto =  DateUtils.extractYearFromDate(dataImpianto);
              int monthDataImpianto =  DateUtils.extractMonthFromDate(dataImpianto);
              int annoPrimaProduzioneInt = DateUtils.extractYearFromDate(dataPrimaProduzione);
              
              //prima del 31/07
              if(monthDataImpianto <= 7)
              {
                if(annoPrimaProduzioneInt != yearDataImpianto + 2)
                {
                  elencoErrori.add("dataPrimaProduzioneText", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataImpianto + 2)));
                  countErrori++;
                }
                else
                {
                  isOKdataPrimaProduzione = true;
                }
              }
              //dopo il 31/07
              else
              {
                if(annoPrimaProduzioneInt != yearDataImpianto + 3)
                {
                  elencoErrori.add("dataPrimaProduzioneText", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataImpianto + 3)));
                  countErrori++;
                }
                else
                {
                  isOKdataPrimaProduzione = true;
                }
              }
            
            }            
          }         
        }
        else 
        {
          elencoErrori.add("dataPrimaProduzioneText", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        
        
        
        
	      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
	      {
	        StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	        StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
	        ValidationErrors errors = new ValidationErrors();
	            
	        // Data sovrainnesto
	        if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getDataSovrainnesto())) 
	        {
	          errors.add("generico", new ValidationError(AnagErrors.ERRORE_DATA_SOVR_PRESENTE));
            countErroriMultipli++;
          }
	          
	        elencoErroriMultipli.add(errors);
	      
	      }
        
      }
      
      
      
      TipoTipologiaVinoVO tipoTipologiaVinoVOSel = null; 
      if (funzioneTipologiaVino)
      {
        //Tipologia di vino
        if(!Validator.isNotEmpty(idTipologiaVino))
        {
          elencoErrori.add("idTipologiaVino", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        else
        {
          idTipologiaVinoLg = new Long(idTipologiaVino);
          tipoTipologiaVinoVOSel = anagFacadeClient.getTipoTipologiaVinoByPrimaryKey(idTipologiaVinoLg);
          boolean trovataAnomaliaDipendenzaIdVarieta = false;
          
          //la tipologia di vino deve essere una tra quelle possibili di idVarieta
          //selezionato
          if(Validator.isNotEmpty(idVarieta))
          {
            Long idVarietaLg = new Long(idVarieta);
            trovataAnomaliaDipendenzaIdVarieta = true;
            //Prendo la prima unita vitata tanto il comune è per tutte uguali
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[0];
            Vector<TipoTipologiaVinoVO> tipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComuneAndVarieta(
              storicoParticellaConfermaVO.getIstatComune(), idVarietaLg, storicoParticellaConfermaVO.getIdParticella());
            if(tipoTipologiaVino != null)
            {
              for(int j=0;j<tipoTipologiaVino.size();j++)
              {
                if(idTipologiaVinoLg.compareTo(tipoTipologiaVino.get(j).getIdTipologiaVino()) == 0)
                {
                  trovataAnomaliaDipendenzaIdVarieta = false;
                  break;
                }
              }
            }
            else
            {
              trovataAnomaliaDipendenzaIdVarieta = false;
            }            
          }
          else
          {
            // Ciclo sulle unità arboree selezionate
            //la tipologia di vino deve essere una tra quelle possibili di idVarieta
            //delle unita arborre selezionate
            for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
            {
              if(!trovataAnomaliaDipendenzaIdVarieta)
              {
                StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
                StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
                Long idVarietaLg = null;
                
                if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getIdVarieta()))
                {
                  idVarietaLg = storicoUnitaArboreaConfermaVO.getIdVarieta();
                }
                
                
                if(Validator.isNotEmpty(idVarietaLg))
                {
                  Vector<TipoTipologiaVinoVO> tipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComuneAndVarieta(
                    storicoParticellaConfermaVO.getIstatComune(), idVarietaLg, storicoParticellaConfermaVO.getIdParticella());
                  boolean trovataCorrispondenza = false;
                  if(tipoTipologiaVino != null)
                  {
                    for(int j=0;j<tipoTipologiaVino.size();j++)
                    {
                      if(idTipologiaVinoLg.compareTo(tipoTipologiaVino.get(j).getIdTipologiaVino()) == 0)
                      {
                        trovataCorrispondenza = true;
                        break;
                      }
                    }
                  }
                  else
                  {
                    trovataCorrispondenza = true;
                  }
                 
                  if(!trovataCorrispondenza)
                  {
                    trovataAnomaliaDipendenzaIdVarieta = true;
                  }               
                }
              }
            }            
          }
          
          if(trovataAnomaliaDipendenzaIdVarieta)
          {
            elencoErrori.add("idTipologiaVino", new ValidationError(AnagErrors.ERR_TIPOLOGIAVINO));
            countErrori++;
          }
          //Setto l'idmenzionegeografica a null se è cambiata l'idTipologiaVino
          else
          {
            for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
            {
              StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
              StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
              if(Validator.isEmpty(storicoUnitaArboreaConfermaVO.getIdTipologiaVino())
                || (Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getIdTipologiaVino())
                  && tipoTipologiaVinoVOSel.getIdTipologiaVino().compareTo(storicoUnitaArboreaConfermaVO.getIdTipologiaVino()) !=0))
              {
                storicoUnitaArboreaConfermaVO.setIdMenzioneGeografica(null);              
              }
                
            }
          
          }
        
        }
        
        
        //Matricola
        if(Validator.isNotEmpty(matricolaCCIAA))
        {
          if(matricolaCCIAA.trim().length() > 15)
          {
            elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "matricolaCCIAA", AnagErrors.ERR_MATRICOLA_CCIAA);
            countErrori++;
          }
        }    
        
      
        // Ciclo sulle unità arboree selezionate
        for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
        {
          StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
          StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
          //ValidationErrors errors = new ValidationErrors();
          
          //La provinciaCCIAA deve essere sempre valorizzata quindi se è null devo prenderla dal comune
          //della particella        
          if(Validator.isEmpty(storicoUnitaArboreaConfermaVO.getProvinciaCCIAA()))
          {
            if(storicoParticellaConfermaVO.getComuneParticellaVO() != null)
            { 
              storicoUnitaArboreaConfermaVO.setProvinciaCCIAA(
                storicoParticellaConfermaVO.getComuneParticellaVO().getSiglaProv());
            }
          }
                 
          storicoUnitaArboreaConfermaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaConfermaVO.getArea()); 
                  
          //elencoErroriMultipli.add(errors);
        }
        
        
        
        //Controllo anno iscrizione albo minore anno di sitema e maggiore 1900 e >= anno impianto
        boolean isOKAnnoIscrizione = false;
        if(Validator.isNotEmpty(annoIscrizioneAlbo))
        {
          if(!Validator.isNumericInteger(annoIscrizioneAlbo))
          {
            elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "annoIscrizioneAlbo", AnagErrors.ERR_FORMA_ANNO_ISCRIZIONE_ALBO);
            countErrori++;
          }
          else
          {
            Integer currYear = DateUtils.getCurrentYear();
            Integer annoIscrizioneAlboInt = new Integer(annoIscrizioneAlbo);
            
            int annoImpianto = 1900;
            
            if (dataImpianto!=null) 
              annoImpianto = DateUtils.extractYearFromDate(dataImpianto);

            
            if(annoIscrizioneAlboInt.intValue() < annoImpianto)
            {
              if (dataImpianto!=null)  
                elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "annoIscrizioneAlbo", AnagErrors.ERR_ISCRIZIONE_ALBO_ANNI);
              else elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "annoIscrizioneAlbo", AnagErrors.ERR_ISCRIZIONE_ALBI_ANNI);
              countErrori++;
            }
            else
            {
              isOKAnnoIscrizione = true;
            }        
          }
        }
        
        
        boolean controlloDAID = false;
        //Se valoriazzata l'anno a video uso quello
        if(isOKAnnoIscrizione)
        {
          if(new Integer(annoIscrizioneAlbo).intValue() <= new Integer(parametroDAID).intValue())
          {
            controlloDAID = true;
          }
        }
        else
        {
          for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
          {
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
            StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
            if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getAnnoIscrizioneAlbo())
              && (new Integer(storicoUnitaArboreaConfermaVO.getAnnoIscrizioneAlbo()).intValue() 
                  <= new Integer(parametroDAID).intValue()))
            {
              controlloDAID = true;
              break;
            }           
          }        
        }
        
        
        
        //Se il vino è DOC, il paramtero ALBO della tabllea DB_PARAMTERO è a S
        // e l'anno impianto è <= parametro UV23  
        //devono essere valorizzati i campi matricola e anno iscrizione
        if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori
         && controlloDAID)
        {
          if(Validator.isEmpty(matricolaCCIAA))
          {
            elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "matricolaCCIAA", AnagErrors.ERR_CAMPO_OBBLIGATORIO_MATRICOLA_VINODOC);
            countErrori++;
          } 
        }
        
        
        //Se il vino è DOC, il paramtero ALBO della tabllea DB_PARAMTERO è a S
        // e l'anno impianto è <= parametro UV23  
        //devono essere valorizzati i campi matricola e anno iscrizione
        if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori)
        {
          if(Validator.isEmpty(annoIscrizioneAlbo))
          {
            elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "annoIscrizioneAlbo", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_VINODOC);
            countErrori++;
          } 
        }
        
      }
      
      
      if(funzioneVigna)
      {
        String flagGestioneVigna = "S";
        boolean erroreVigna = false;
        
        //non è stata selezionata la combo dell'idoneità
        //devo controllare i valori delle singole uv
        if(Validator.isEmpty(tipoTipologiaVinoVOSel))
        {         
          for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
          {
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
            StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
            if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO())
              && "N".equalsIgnoreCase(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO().getFlagGestioneVigna()))
            {
              flagGestioneVigna = "N";
              break;
            }
            
            if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getIdVigna()))
            {
              flagGestioneVigna = "N";
              erroreVigna = true;
              break;
            }
            
            if(Validator.isEmpty(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO()))
            {
              flagGestioneVigna = "N";
              break;
            }           
          }
        }  
          
        
        if("S".equalsIgnoreCase(flagGestioneVigna))
        {
          if(Validator.isNotEmpty(vignaText)) 
          {
            //Controlla Vigna max 1000 caratteri
            if(Validator.isNotEmpty(vignaText) && (vignaText.length() > 1000))
            {
              elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "vignaText", AnagErrors.ERR_VIGNA);
              countErrori++;
            }
          }
          else
          {
            elencoErrori.add("vignaText", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
        //qui posso entrare solo se non ho selezionato l'idoneità 
        else
        {
          if(erroreVigna)
          {
            elencoErrori.add("vignaText", new ValidationError(AnagErrors.ERRORE_FLAG_GESTIONE_IDVIGNA));
            countErrori++;
          }
          else
          {
            elencoErrori.add("vignaText", new ValidationError(AnagErrors.ERRORE_FLAG_GESTIONE_VIGNA));
            countErrori++;
          }
          
        }
        
      }
      
      
      
      if(funzioneAnnotazioneEtichetta)
      {
        String flagGestioneEtichetta = "S";
        
        
        
        //non è stata selezionata la combo dell'idoneità
        //devo controllare i valori delle singole uv
        if(Validator.isEmpty(tipoTipologiaVinoVOSel))
        {        
          for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
          {
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
            StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
            if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO())
              && "N".equalsIgnoreCase(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO().getFlagGestioneEtichetta()))
            {
              flagGestioneEtichetta = "N";
              break;
            }
            
            if(Validator.isEmpty(storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO()))
            {
              flagGestioneEtichetta = "N";
              break;
            }           
          }
        }  
        
      
        if("S".equalsIgnoreCase(flagGestioneEtichetta))
        {
          if(Validator.isNotEmpty(annotazioneEtichettaText)) 
          {
            //Controlla l'annotazione etichetta max 1000 caratteri
            if(Validator.isNotEmpty(annotazioneEtichettaText) && (annotazioneEtichettaText.length() > 1000))
            {
              elencoErrori = ErrorUtils.setValidErrNoNull(elencoErrori, "annotazioneEtichettaText", AnagErrors.ERR_ANNOTAZIONE_ETICHETTA);
              countErrori++;
            }
          }
          else
          {
            elencoErrori.add("annotazioneEtichettaText", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
        //qui posso entrare solo se non ho selezionato l'idoneità 
        else
        {
          elencoErrori.add("annotazioneEtichettaText", new ValidationError(AnagErrors.ERRORE_FLAG_GESTIONE_ETICHETTA));
          countErrori++;
        }
      }
      
      
      
      if (funzioneAllineaUVGIS)
      {
      
        if(session.getAttribute("noTutteUvAllGis") !=null)
        {
          elencoErrori.add("vociCheckBox", new ValidationError(AnagErrors.ERRORE_NO_TUTTE_UV_ALLINEA_GIS));
          countErrori++;
        }
        else
        {
        
          // Recupero il parametro VTUV da comune
          String parametroVTUV = null;
          try 
          {
            parametroVTUV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_VTUV);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_VTUV+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          // Recupero il parametro UVAG da comune
          String parametroUVAG = null;
          try 
          {
            parametroUVAG = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_UVAG);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_UVAG+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
        
          // Ciclo sulle unità arboree selezionate
          for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
          {
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
            StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
            ValidationErrors errors = new ValidationErrors();
            
            // SUPERFICIE ALLINEA GIS
            if(Validator.isNotEmpty(request.getParameterValues("supPostVit"))) 
            {
              if(Validator.isNotEmpty(request.getParameterValues("supPostVit")[i])) 
              {
                storicoUnitaArboreaConfermaVO.setSupPostVit(request.getParameterValues("supPostVit")[i]);           
                if(Validator.validateDouble(storicoUnitaArboreaConfermaVO.getSupPostVit(), 999999.9999) == null) {
                  errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                  countErroriMultipli++;
                }
                else 
                {
                  storicoUnitaArboreaConfermaVO.setSupPostVit(StringUtils.parseSuperficieField(request.getParameterValues("supPostVit")[i]));
                  boolean flagTolleranza = false;
                  
                  //Il controllo va fatto solo se l'utenza è diversa dalla PA
                  /*if(!ruoloUtenza.isUtentePA())
                  {
                    //vado avanti solo se parametro uguale a S e se non è stato fato ancora il consolidamento
                    if("S".equalsIgnoreCase(parametroVTUV) && flagTolleranzaCompensazione)
                    {
                      Integer tolleranza = gaaFacadeClient.getTolleranzaPlSql(parametroUVAG,
                        anagAziendaVO.getIdAzienda().longValue(),
                        storicoUnitaArboreaConfermaVO.getIdUnitaArborea());
                        
                      if(tolleranza != null)
                      {
                        StoricoUnitaArboreaVO storicoTollVO = anagFacadeClient.findStoricoUnitaArborea(
                          storicoUnitaArboreaConfermaVO.getIdStoricoUnitaArborea());
                        String oldArea = storicoUnitaArboreaConfermaVO.getArea();
                        String newArea = storicoUnitaArboreaConfermaVO.getSupPostVit();                  
                        if(tolleranza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0)
                        {
                          if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                            > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                          {
                            flagTolleranza = true;
                            errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_FUORI_TOLLERANZA));
                            countErroriMultipli++;
                          
                          } 
                        }
                        else if(tolleranza.compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0)
                        {
                          if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                            > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                          {
                            flagTolleranza = true;
                            errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_UVDOPPIE));
                            countErroriMultipli++;                    
                          }
                        
                        }
                        else if(tolleranza.compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0)
                        {
                          if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                            > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                          {
                            flagTolleranza = true;
                            errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_NO_PARCELLE));
                            countErroriMultipli++;                    
                          }                  
                        }
                        else if(tolleranza.compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0)
                        {
                          if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                            > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                          {
                            flagTolleranza = true;
                            errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_ERR_PL));
                            countErroriMultipli++;                    
                          }
                        
                        }
                        else if(tolleranza.compareTo(SolmrConstants.UV_NON_PRESENTE) == 0)
		                    {
		                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
		                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
		                      {
		                        flagTolleranza = true;
		                        errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_NON_PRESENTE));
		                        countErroriMultipli++;                    
		                      }
		                    
		                    }
		                    else if(tolleranza.compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0)
		                    {
		                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
		                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
		                      {
		                        flagTolleranza = true;
		                        errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_PARTICELLA_ORFANA));
		                        countErroriMultipli++;                    
		                      }
		                    
		                    }
		                    else if(tolleranza.compareTo(SolmrConstants.PARCELLE_NO_VITE) == 0)
		                    {
		                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
		                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
		                      {
		                        flagTolleranza = true;
		                        errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_PARCELLE_NO_VITE));
		                        countErroriMultipli++;                    
		                      }
		                    
		                    }
		                    else if(tolleranza.compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0)
		                    {
		                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
		                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
		                      {
		                        flagTolleranza = true;
		                        errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_UV_PIU_OCCORR_ATTIVE));
		                        countErroriMultipli++;                    
		                      }
		                    
		                    }
                      }                    
                    
                    }
                  }*/ 
                  
                  
                  
                  // La superficie vitata non può essere maggiore della superficie catastale
                  //controllo solo se non ci sono già stati problemi per la tolleranza
                  if(!flagTolleranza)
                  {
                    String supConfronto = AnagUtils.valSupCatGraf(
                      storicoParticellaConfermaVO.getSupCatastale(), storicoParticellaConfermaVO.getSuperficieGrafica());
                    
                    
                    if(NumberUtils.arrotonda(Double.parseDouble(storicoUnitaArboreaConfermaVO.getSupPostVit().replace(',', '.')), 4) 
                      > NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
                    {
                      errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_MAGGIORE_SUP_GRAFICA_CAT));
                      countErroriMultipli++;
                    }
                  }
                }
              }
              else 
              {
                storicoUnitaArboreaConfermaVO.setSupPostVit(null);
                errors.add("supPostVit", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
                countErroriMultipli++;
              }
            }        
                      
            
            elencoErroriMultipli.add(errors);
          }
        }
      }
      
            
      /*if((countErrori == 0) && (countErroriMultipli == 0))
      {
        //sono stati selezionate sia la tipologia vino che l'anno impianto
        // e sono state valorizzate senza errori
        TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
        if(Validator.isNotEmpty(idTipologiaVinoLg))
        {
          tipoTipologiaVinoVO = anagFacadeClient.getTipoTipologiaVinoByPrimaryKey(
            idTipologiaVinoLg.longValue());        
        }
        
        if(Validator.isNotEmpty(tipoTipologiaVinoVO)
          && Validator.isNotEmpty(dataImpianto))
        {
          if("S".equalsIgnoreCase(tipoTipologiaVinoVO.getVinoDoc())
            && (DateUtils.extractYearFromDate(dataImpianto) ==
              DateUtils.getCurrentYear().intValue()))
          {
            elencoErrori.add("dataImpiantoText", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE+""+DateUtils.getCurrentYear()));
            elencoErrori.add("idTipologiaVino", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE+""+DateUtils.getCurrentYear()));  
            countErrori++;         
          }         
        }
        else
        {
          Vector<Long>  vIdStoricoUnitaArboreaDataImpianto = null;        
          for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
          {
            ValidationErrors errors = new ValidationErrors();
            StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
            StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
            TipoTipologiaVinoVO tipoTipologiaVinoVOTmp = tipoTipologiaVinoVO;
            Date dataImpiantoTmp = dataImpianto;
            //Non è stata selezionata la tipologia di vino dalla combo
            if(Validator.isEmpty(tipoTipologiaVinoVOTmp))
            {
              tipoTipologiaVinoVOTmp = storicoUnitaArboreaConfermaVO.getTipoTipologiaVinoVO();
            }
            //Non è stata selezionata la data impianto
            if(Validator.isEmpty(dataImpiantoTmp))
            {
              dataImpiantoTmp = storicoUnitaArboreaConfermaVO.getDataImpianto();
            }
            
            
            if(Validator.isNotEmpty(tipoTipologiaVinoVOTmp)
              && Validator.isNotEmpty(dataImpiantoTmp))
            {
              if("S".equalsIgnoreCase(tipoTipologiaVinoVOTmp.getVinoDoc())
                && (DateUtils.extractYearFromDate(dataImpiantoTmp) ==
                  DateUtils.getCurrentYear().intValue()))
              {
                if(vIdStoricoUnitaArboreaDataImpianto == null)
                {
                  vIdStoricoUnitaArboreaDataImpianto = new Vector<Long>();
                }
                vIdStoricoUnitaArboreaDataImpianto.add(storicoUnitaArboreaConfermaVO.getIdStoricoUnitaArborea());                        
              }
            
            }
          }
          
          
          if(Validator.isNotEmpty(vIdStoricoUnitaArboreaDataImpianto))
          {
            request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE_MSG+""+DateUtils.getCurrentYear());
            request.setAttribute("vIdStoricoUnitaArboreaDataImpianto",vIdStoricoUnitaArboreaDataImpianto);
            %>
              <jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
            <%
            return;     
          }
          
          
        }
        
        
      
      }*/
      
      
      
      
			// Se si sono verificati degli errori li visualizzo
	 		if((countErrori>0) || (countErroriMultipli > 0)) 
      {
	 			request.setAttribute("elencoErrori", elencoErrori);
        request.setAttribute("elencoErroriMultipli", elencoErroriMultipli);
	 			%>
	 				<jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
	 			<%
	 		}
			// Altrimenti procedo con la modifica dell'unità vitata
			else 
      {
        //il controllo và fatto solo se il ruolo è diverso da PA
        if(!ruoloUtenza.isUtentePA())
        {
          //controllo che non ci siano pratiche uv associate alla particella
          if(Validator.isNotEmpty(vIdParticella))
          {
            //controllo se vi sono pratiche essociate alle uv
            long[] aIdParticella = new long[vIdParticella.size()];
            for(int i=0;i<vIdParticella.size();i++)
            {
              aIdParticella[i] = vIdParticella.get(i).longValue();
            }
            
            RitornoAgriservUvVO ritornoAgriservUvVO = gaaFacadeClient.existPraticheEstirpoUV(aIdParticella,
              anagAziendaVO.getIdAzienda(), null, null, null, 0);
            Vector<Long>  vPraticheIdParticella = ritornoAgriservUvVO.getvPraticheIdParticella();
            
            //Passando una sola particella se il vettore è valorizzato vuol dire che ad
            // essa sono associate pratiche
            if(Validator.isNotEmpty(vPraticheIdParticella))
            {
              request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_MULTIPLE);
              request.setAttribute("vPraticheIdParticella",vPraticheIdParticella);
              %>
                <jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
              <%
              return;     
            }
            
            //errore nella chiamata al servizio
            if(Validator.isNotEmpty(ritornoAgriservUvVO.getErrori())  && (ritornoAgriservUvVO.getErrori().length > 0))
            {
              request.setAttribute("messaggioErrore", ritornoAgriservUvVO.getErrori()[0]);
              %>
                <jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
              <%
              return;         
            }
          }
                   
          //Se il vettore è valorizzato vuol dire che ci sono UV modificate/inserite
          //dal procedimenti VITI
          if(Validator.isNotEmpty(vIdStoricoUvModVITI))
          {
            request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_MULTIPLE);
            request.setAttribute("vIdStoricoUvModVITI", vIdStoricoUvModVITI);
            %>
              <jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
            <%
            return;     
          }          
          
        }        
      
      
				try 
        {
          //devo inserire all'interno dell'array i dati che voglio modificare per poter usare la
          //stessa funzionalità del modifica semplice
          if (elencoStoricoParticellaArboreaVO!=null)
          {         
          
            if (funzioneCambia)
            {
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
              {
                //Salvo catalogoMatrice
                CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient
                  .getCatalogoMatriceFromMatrice(new Long(idTipoUtilizzo), new Long(idVarieta), 
                  new Long(idTipoDestinazione), new Long(idTipoDettaglioUso), new Long(idTipoQualitaUso));     
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setIdUtilizzo(Long.decode(idTipoUtilizzo));
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setIdVarieta(Long.decode(idVarieta));
              }
            }
            if (funzioneTipologiaVino)
            {
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
              {               
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setIdTipologiaVino(Long.decode(idTipologiaVino));
                if(Validator.isNotEmpty(matricolaCCIAA))
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setMatricolaCCIAA(matricolaCCIAA.trim());
                if(Validator.isNotEmpty(annoIscrizioneAlbo))
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setAnnoIscrizioneAlbo(annoIscrizioneAlbo);
              }
            }
           
            if (funzioneDataImpianto)
            {
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
              {
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setAnnoImpianto(new Integer(DateUtils.extractYearFromDate(dataImpianto)).toString());
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setAnnoPrimaProduzione(new Integer(DateUtils.extractYearFromDate(dataPrimaProduzione)).toString());
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setDataImpianto(dataImpianto);
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setDataPrimaProduzione(dataPrimaProduzione);
              }
                
            }
            //Setto la causale della modifica
            for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
              elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setIdCausaleModifica(new Long(SolmrConstants.ID_CAUSALE_MODIFICA_MULTIPLA_UNI_ARBOREE));
              
            
            //é selezionato la combo idoneità....
            if(Validator.isNotEmpty(tipoTipologiaVinoVOSel)
              && "N".equalsIgnoreCase(tipoTipologiaVinoVOSel.getFlagGestioneVigna()))
            {
              //Setto la vigna
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setVigna(null);
            }
            else
            {            
              if (funzioneVigna)
              {  
                //Setto la vigna
                if(Validator.isNotEmpty(vignaText))
                {
                  vignaText = vignaText.trim();
                }
                for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
                {
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setVigna(vignaText);
                }
              }
            }
            
            
            //é selezionato la combo idoneità....
            if(Validator.isNotEmpty(tipoTipologiaVinoVOSel)
              && "N".equalsIgnoreCase(tipoTipologiaVinoVOSel.getFlagGestioneEtichetta()))
            {
              //Setto l'annotazione in etichetta
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setEtichetta(null);
            }
            else
            {
              if (funzioneAnnotazioneEtichetta)
              {  
                //Setto l'annotazione in etichetta
                //Setto la vigna
                if(Validator.isNotEmpty(annotazioneEtichettaText))
                {
                  annotazioneEtichettaText = annotazioneEtichettaText.trim();
                }
                for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setEtichetta(annotazioneEtichettaText);
              }
            }
            
            if (funzioneAllineaUVGIS)
            {  
              //Setto l'area
              for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
              {
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setArea(
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().getSupPostVit());
                elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().setSuperficieDaIscrivereAlbo(
                  elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO().getArea());
              }
            }
              
          }
          
          
          
          
          
					anagFacadeClient.modificaUnitaArboree(elencoStoricoParticellaArboreaVO, ruoloUtenza, provenienza);
				}
				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - unitaArboreeModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
			 	}
				// Torno alla pagina di ricerca/elenco
		 	 	String valorePagina = (String)session.getAttribute("pagina");
				%>
					<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" >
						<jsp:param name="pagina" value="<%= valorePagina %>" /> 
					</jsp:forward>
				<%
			}
	 	}
 	}
	
 	// Vado alla pagina di modifica
 	%>
		<jsp:forward page="<%= unitaArboreeModificaMultiplaUrl %>" />
	

