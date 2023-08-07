<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

	String iridePageName = "popModificaTerritorialeCondUsoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String popModificaTerritorialeCondUsoUrl = "/view/popModificaTerritorialeCondUsoView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	StoricoParticellaVO[] elencoStoricoParticella = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticella");
	String regimePopModificaTerritorialeCondUso = request.getParameter("regimePopModificaTerritorialeCondUso");
	boolean isKoUtilizzi = false;
	boolean isKoUtilizziEfa = false;
	//boolean isKoDettaglioUso = false;
	boolean isKoPiante = false;
	ValidationErrors errors = null;
	Long idConduzione = null;
	
	//se vuoto prima volta che entro svuoto tutto....
	if(Validator.isEmpty(regimePopModificaTerritorialeCondUso))
	{
	  session.removeAttribute("idConduzione");
	  session.removeAttribute("storicoParticellaVO");
	}
	
	
	if(Validator.isNotEmpty(request.getParameter("idConduzioneParticella"))) 
	{
		idConduzione = Long.decode(request.getParameter("idConduzioneParticella"));
		session.setAttribute("idConduzione",idConduzione);
	}
	else 
	{
		idConduzione = (Long)session.getAttribute("idConduzione");
	}
	
	
	String messaggioErrore = null;
	String[] elencoUtilizziSelezionati = request.getParameterValues("idTipoUtilizzo");
	String[] elencoUtilizziSecondariSelezionati = request.getParameterValues("idTipoUtilizzoSecondario");
	String[] elencoDestSelezionati = request.getParameterValues("idTipoDestinazione");
  String[] elencoDestSecondariSelezionati = request.getParameterValues("idTipoDestinazioneSecondario");
  String[] elencoDettUsoSelezionati = request.getParameterValues("idTipoDettaglioUso");
  String[] elencoDettUsoSecondariSelezionati = request.getParameterValues("idTipoDettaglioUsoSecondario");
  String[] elencoQualitaUsoSelezionati = request.getParameterValues("idTipoQualitaUso");
  String[] elencoQualitaUsoSecondariSelezionati = request.getParameterValues("idTipoQualitaUsoSecondario");
  String[] elencoVarietaSelezionate = request.getParameterValues("idVarieta");
  String[] elencoVarietaSecondarieSelezionate = request.getParameterValues("idVarietaSecondaria");
	// Parametro che indica la particella selezionata dall'elenco...
	String numero = request.getParameter("numero");
	// Se è valorizzato vuol dire che ho richiamato la pop-up dall'elenco
	if(Validator.isNotEmpty(numero)) 
	{
		// Se il valore è diverso da quello che avevo selezionato precedentemente...
		if(!numero.equalsIgnoreCase((String)session.getAttribute("numero"))) 
		{
			// rimuovo i possibili vecchi utilizzi selezionati perchè riguarderebbero
			// un'altra particella
			//session.removeAttribute("storicoParticellaVO");
		}
		session.setAttribute("numero", numero);
	}
	else {
		numero = (String)session.getAttribute("numero");
	}
	
	// Recupero i dati della particella selezionata
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");      
	
	
	StoricoParticellaVO storicoParticellaConfrontoVO = null;
	Vector<Long> vIdTipoEfa = new Vector<Long>();
  Vector<Long> vIdTipoUtilizzoEfa = new Vector<Long>();
  Vector<Long> vIdTipoDestinazioneEfa = new Vector<Long>();
  Vector<Long> vIdTipoDettaglioUsoEfa = new Vector<Long>();
  Vector<Long> vIdTipoQualitaUsoEfa = new Vector<Long>();
  Vector<Long> vIdTipoVarietaEfa = new Vector<Long>();
  Vector<UtilizzoParticellaVO> vUtilizziEfaVO = new Vector<UtilizzoParticellaVO>();
  Vector<UtilizzoParticellaVO> vUtilizziVO = new Vector<UtilizzoParticellaVO>();
  
	// Se è la prima volta che accedo alla pop-up per la particella selezionata
	if(storicoParticellaVO == null) 
  {
		// Modifico i valori degli oggetti della tabella in modo che siano mantenuti
		int valorePartenza = 0;
		//mi calcolo il corretto valore di partenza
		if(Validator.isNotEmpty(elencoStoricoParticella))
		{
		  for(int h=0;h<new Integer(numero).intValue();h++)
		  {
		    StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticella[h];
	      ConduzioneParticellaVO conduzioneVO = storicoVO.getElencoConduzioni()[0];
	      UtilizzoParticellaVO[] elencoUtilizziVO = conduzioneVO.getElencoUtilizzi();
	      for(int a = 0; a < elencoUtilizziVO.length; a++) 
        {
          UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizziVO[a];
          if(Validator.isEmpty(utilizzoParticellaVO.getDichiarabileEfa())
            || (Validator.isNotEmpty(utilizzoParticellaVO.getDichiarabileEfa()) 
                && !"S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa())))
          {
            valorePartenza++;
          }
        }
		  }
		
		}
		
		//request.setAttribute("valorePartenza", new Integer(valorePartenza));
		
    if(Validator.isNotEmpty(elencoStoricoParticella))
    {
      //Carico solo la particella in questione....
      int i = Integer.parseInt(numero); 
   		
 			StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticella[i];
 			ConduzioneParticellaVO conduzioneVO = storicoVO.getElencoConduzioni()[0];
 			UtilizzoParticellaVO[] elencoUtilizziVO = conduzioneVO.getElencoUtilizzi();
 			if(Validator.isNotEmpty(request.getParameterValues("idTitoloPossesso")) 
 			  && Validator.isNotEmpty(request.getParameterValues("idTitoloPossesso")[i])) 
 			{
 				conduzioneVO.setIdTitoloPossesso(Long.decode(request.getParameterValues("idTitoloPossesso")[i]));
 			}
 			else 
 			{
 				conduzioneVO.setIdTitoloPossesso(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("supCondotta")) 
 			  && Validator.isNotEmpty(request.getParameterValues("supCondotta")[i])) 
 			{
 				conduzioneVO.setSuperficieCondotta(request.getParameterValues("supCondotta")[i]);
 			}
 			else 
 			{
 				conduzioneVO.setSuperficieCondotta(null);
 			}
 			
 			if(Validator.isNotEmpty(request.getParameterValues("percentualePossesso")) 
        && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
      {
        conduzioneVO.setPercentualePossesso(new BigDecimal(request.getParameterValues("percentualePossesso")[i].replace(",", ".")));
      }
      else 
      {
        conduzioneVO.setPercentualePossesso(null);
      }
 			// SUPERFICIE AGRONOMICA
 			if(Validator.isNotEmpty(request.getParameterValues("superficieAgronomica")) 
 			  && Validator.isNotEmpty(request.getParameterValues("superficieAgronomica")[i])) 
 			{
 				conduzioneVO.setSuperficieAgronomica(request.getParameterValues("superficieAgronomica")[i]);
 			}
 			else 
 			{
 				conduzioneVO.setSuperficieAgronomica(null);
 			}
      if(Validator.isNotEmpty(elencoUtilizziVO))
      {
   			for(int a = 0; a < elencoUtilizziVO.length; a++) 
        {
   				UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizziVO[a];
   				if(Validator.isEmpty(utilizzoParticellaVO.getDichiarabileEfa())
   				  || (Validator.isNotEmpty(utilizzoParticellaVO.getDichiarabileEfa()) 
   				      && !"S".equalsIgnoreCase(utilizzoParticellaVO.getDichiarabileEfa())))
   				{
    				if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[valorePartenza])) 
    				{
    					utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[valorePartenza]));
    				  utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[valorePartenza]));
    				  utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[valorePartenza]));
    				  utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[valorePartenza]));
    				  utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[valorePartenza]));
    				  utilizzoParticellaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[valorePartenza]));
    				  utilizzoParticellaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[valorePartenza]));
    				  utilizzoParticellaVO.setDataInizioDestinazioneStr(request.getParameterValues("dataInizioDestinazione")[valorePartenza]);
    				  utilizzoParticellaVO.setDataFineDestinazioneStr(request.getParameterValues("dataFineDestinazione")[valorePartenza]);
    				}
    				else 
    				{
    					utilizzoParticellaVO.setIdUtilizzo(null);
    					utilizzoParticellaVO.setIdTipoDestinazione(null);
    					utilizzoParticellaVO.setIdTipoDettaglioUso(null);
    					utilizzoParticellaVO.setIdTipoQualitaUso(null);
    					utilizzoParticellaVO.setIdVarieta(null);
    					utilizzoParticellaVO.setIdTipoPeriodoSemina(null);
    					utilizzoParticellaVO.setIdSemina(null);
    					utilizzoParticellaVO.setDataInizioDestinazione(null);
    					utilizzoParticellaVO.setDataFineDestinazione(null);
    				}
    				
			      if(Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[valorePartenza])) 
    				{
    					utilizzoParticellaVO.setSuperficieUtilizzata(request.getParameterValues("supUtilizzata")[valorePartenza]);
    				}
    				else 
    				{
    					utilizzoParticellaVO.setSuperficieUtilizzata(null);
    				}
    				
    				
    				if(request.getParameterValues("idTipoUtilizzoSecondario") != null 
    				  && (valorePartenza) < request.getParameterValues("idTipoUtilizzoSecondario").length) 
    				{
    					if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[valorePartenza])) 
    					{
    						utilizzoParticellaVO.setIdUtilizzoSecondario(Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[valorePartenza]));
    						utilizzoParticellaVO.setIdTipoDestinazioneSecondario(Long.decode(request.getParameterValues("idTipoDestinazioneSecondario")[valorePartenza]));
    						utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(Long.decode(request.getParameterValues("idTipoDettaglioUsoSecondario")[valorePartenza]));
    						utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(Long.decode(request.getParameterValues("idTipoQualitaUsoSecondario")[valorePartenza]));
    						utilizzoParticellaVO.setIdVarietaSecondaria(Long.decode(request.getParameterValues("idVarietaSecondaria")[valorePartenza]));
    						utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[valorePartenza]));
    						utilizzoParticellaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[valorePartenza]));
				        utilizzoParticellaVO.setDataInizioDestinazioneSecStr(request.getParameterValues("dataInizioDestinazioneSec")[valorePartenza]);
				        utilizzoParticellaVO.setDataFineDestinazioneSecStr(request.getParameterValues("dataFineDestinazioneSec")[valorePartenza]);
				           							
    					}
    					else 
    					{
    						utilizzoParticellaVO.setIdUtilizzoSecondario(null);
    						utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
    						utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
    						utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
    						utilizzoParticellaVO.setIdVarietaSecondaria(null);
    						utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
    						utilizzoParticellaVO.setIdSeminaSecondario(null);
    						utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
    						utilizzoParticellaVO.setDataFineDestinazioneSec(null);
    					}
    				}
    				else 
    				{
    					utilizzoParticellaVO.setIdUtilizzoSecondario(null);
    					utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
              utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
              utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
              utilizzoParticellaVO.setIdVarietaSecondaria(null);
              utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
              utilizzoParticellaVO.setIdSeminaSecondario(null);
              utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
              utilizzoParticellaVO.setDataFineDestinazioneSec(null);
    				}
    				
    				
    				if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[valorePartenza])) 
            {
              utilizzoParticellaVO.setSupUtilizzataSecondaria(request.getParameterValues("supUtilizzataSecondaria")[valorePartenza]);
            }
            else 
            {
              utilizzoParticellaVO.setSupUtilizzataSecondaria(null);
            }
    				
    				
    				if(request.getParameterValues("idPraticaMantenimento") != null 
    				  && (valorePartenza < request.getParameterValues("idPraticaMantenimento").length) 
    				  && Validator.isNotEmpty(request.getParameterValues("idPraticaMantenimento")[valorePartenza])) 
    				{
			        utilizzoParticellaVO.setIdPraticaMantenimento(new Long(request.getParameterValues("idPraticaMantenimento")[valorePartenza]));
			      }
			      else 
			      {
			        utilizzoParticellaVO.setIdPraticaMantenimento(null);
			      }
			      
			      /*if(request.getParameterValues("idFaseAllevamento") != null 
              && (valorePartenza < request.getParameterValues("idFaseAllevamento").length) 
              && Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[valorePartenza])) 
			      {
			        utilizzoParticellaVO.setIdFaseAllevamento(new Long(request.getParameterValues("idFaseAllevamento")[valorePartenza]));
			      }
			      else {
			        utilizzoParticellaVO.setIdFaseAllevamento(null);
			      }*/
    				
    				vUtilizziVO.add(utilizzoParticellaVO);
    				valorePartenza++;
    			}
    			else
    			{    			
    			  vIdTipoEfa.add(utilizzoParticellaVO.getIdTipoEfa());
			      vIdTipoUtilizzoEfa.add(utilizzoParticellaVO.getIdUtilizzo());
			      vIdTipoDestinazioneEfa.add(utilizzoParticellaVO.getIdTipoDestinazione());
			      vIdTipoDettaglioUsoEfa.add(utilizzoParticellaVO.getIdTipoDettaglioUso());
			      vIdTipoQualitaUsoEfa.add(utilizzoParticellaVO.getIdTipoQualitaUso());
			      vIdTipoVarietaEfa.add(utilizzoParticellaVO.getIdVarieta());
			      vUtilizziEfaVO.add(utilizzoParticellaVO);
    			}
    			
    			//per quelli efa sono disabilitati prendo queli che ci sono già!!!
    			elencoUtilizziVO[a] = utilizzoParticellaVO;            
    		}
      }
 			conduzioneVO.setElencoUtilizzi(elencoUtilizziVO);
 			ConduzioneParticellaVO[] elencoConduzioni = {conduzioneVO};
 			storicoVO.setElencoConduzioni(elencoConduzioni);
 			if(Validator.isNotEmpty(request.getParameterValues("idCasoParticolare")[i]))  
 			{
 				storicoVO.setIdCasoParticolare(Long.decode(request.getParameterValues("idCasoParticolare")[i]));
 			}
 			else 
 			{
 				storicoVO.setIdCasoParticolare(null);
 			}
 			
 			elencoStoricoParticella[i] = storicoVO;
   		
    }
  
    request.setAttribute("vUtilizziEfaVO", vUtilizziEfaVO);
    request.setAttribute("vUtilizziVO", vUtilizziVO);  
    
    
 		
		storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticella[Integer.parseInt(numero)];
		
	}
	
  
	session.setAttribute("storicoParticellaVO", storicoParticellaVO);
	
	ProvinciaVO provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
  if(provinciaControlloVO != null) 
  {
    if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
    {
      request.setAttribute("isPiemontese", "true");
    }
  } 
  
  //caricamento tipi area 
  try 
  {
    Vector<TipoAreaVO> vTipoArea = gaaFacadeClient.getAllValoriTipoArea();
    request.setAttribute("vTipoArea", vTipoArea);
  }
  catch(SolmrException se) 
  {   
    messaggioErrore = se.toString();
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }   
  
  
  //caricamento tipo allevamento
  try 
  {
    Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = gaaFacadeClient.getElencoFaseAllevamento();
    request.setAttribute("vTipoFaseAllev", vTipoFaseAllev);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = se.toString();
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }        
	
	
	// Cerco i dati su DB della particella selezionata in modo da poter effettuare
	// i confronti e i controlli associati
	try 
  {
		storicoParticellaConfrontoVO = anagFacadeClient.getDettaglioParticella(filtriParticellareRicercaVO, idConduzione);
		//non più imputabile la supCatastale
    //storicoParticellaConfrontoVO.setSupCatastale(StringUtils.parseSuperficieField(storicoParticellaConfrontoVO.getSupCatastale()));
		request.setAttribute("storicoParticellaConfrontoVO", storicoParticellaConfrontoVO);
	}
	catch(SolmrException se) {
		messaggioErrore = AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
		  <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
   	<%
	}
	
	
	// Cerco i dati dell'U.T.E relativa alla conduzione selezionata
	UteVO uteVO = null;
	try 
	{
		uteVO = anagFacadeClient.findUteByPrimaryKey(storicoParticellaVO.getElencoConduzioni()[0].getIdUte());
		request.setAttribute("uteVO", uteVO);
	}
	catch(SolmrException se) 
	{
		messaggioErrore = AnagErrors.ERRORE_KO_UTE;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
  		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
  	<%
	}
	
	// Cerco i dati della particella certificata
	ParticellaCertificataVO particellaCertificataVO = null;
	if(storicoParticellaVO != null) 
	{
		try 
		{
			particellaCertificataVO = anagFacadeClient.findParticellaCertificataByParameters(storicoParticellaVO.getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getParticella(), storicoParticellaVO.getSubalterno(), true, null);
			request.setAttribute("particellaCertificataVO", particellaCertificataVO);
		}
		catch(SolmrException se) 
		{
			messaggioErrore = AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
    		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    	<%
		}
	}
	
	// Ricerco i titoli possesso
	it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = null;
	try 
	{
		elencoTitoliPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
		request.setAttribute("elencoTitoliPossesso", elencoTitoliPossesso);
	}
	catch(SolmrException se) 
	{
		messaggioErrore = AnagErrors.ERRORE_KO_TITOLO_POSSESSO;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
   		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
   	<%
	}
	
	
	
	// Ricerco i tipi irrigazione
	TipoIrrigazioneVO[] elencoIrrigazione = null;
	try 
	{
		elencoIrrigazione = anagFacadeClient.getListTipoIrrigazione(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, true);
		request.setAttribute("elencoIrrigazione",elencoIrrigazione);
	}
	catch(SolmrException se) 
	{
		messaggioErrore = AnagErrors.ERRORE_KO_TIPO_IRRIGAZIONE;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
  		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
  	<%
	}
  
  // Ricerco i tipi rotazione colturale
  TipoRotazioneColturaleVO[] elencoRotazioneColturale = null;
  try 
  {
    elencoRotazioneColturale = anagFacadeClient.getListTipoRotazioneColturale(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, null);
    request.setAttribute("elencoRotazioneColturale",elencoRotazioneColturale);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_ROTAZIONE_COLTURALE;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
  }
  
  // Ricerco i tipi terrazzamenti
  TipoTerrazzamentoVO[] elencoTerrazzamenti = null;
  try 
  {
    elencoTerrazzamenti = anagFacadeClient.getListTipoTerrazzamento(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, null);
    request.setAttribute("elencoTerrazzamenti",elencoTerrazzamenti);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_TERRAZZAMENTI;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
  }
  
  
  // Ricerco i tipi metodo irriguo
  Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = null;
  try 
  {
    vMetodoIrriguo = gaaFacadeClient.getElencoMetodoIrriguo();
    request.setAttribute("vMetodoIrriguo", vMetodoIrriguo);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_METODO_IRRIGUO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
  }
  
  // Ricerco i tipi metodo irriguo
  Vector<TipoSeminaVO> vTipoSemina = null;
  try 
  {
    vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
    request.setAttribute("vTipoSemina", vTipoSemina);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_SEMINA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
  }
	
	// Ricerco i documenti territoriali associati all'azienda
 	DocumentoVO[] elencoDocumenti = null;
 	try 
 	{
		String[] orderBy = {SolmrConstants.ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION, SolmrConstants.ORDER_BY_DATA_PROTOCOLLO};
		elencoDocumenti = anagFacadeClient.getListDocumentiByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy, SolmrConstants.ID_TIPOLOGIA_DOCUMENTO_TERRITORIALE);
	 	request.setAttribute("elencoDocumenti", elencoDocumenti);
 	}
 	catch(SolmrException se) 
 	{
		messaggioErrore = (String)AnagErrors.ERRORE_KO_DOCUMENTI_AZIENDA;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
		<%
	}
 	
	// Ricerco le causali di modifica della particella
 	it.csi.solmr.dto.CodeDescription[] elencoCausaliModParticella = null;
 	try 
 	{
		elencoCausaliModParticella = anagFacadeClient.getListTipoCausaleModParticella(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
	 	request.setAttribute("elencoCausaliModParticella", elencoCausaliModParticella);
 	}
 	catch(SolmrException se) 
 	{
		messaggioErrore = AnagErrors.ERRORE_KO_CAUSALE_MOD_PARTICELLA;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
		<%
	}
 	
	// Recupero l'elenco delle piante consociate attive censite sul DB
 	Vector<TipoPiantaConsociataVO> elencoPianteConsociate = null;
 	try 
 	{
 		elencoPianteConsociate = anagFacadeClient.getListPianteConsociate(true);
 		request.setAttribute("elencoPianteConsociate", elencoPianteConsociate);
 	}
 	catch(SolmrException se) 
 	{
 		String messaggio = (String)AnagErrors.ERRORE_KO_PIANTE_CONSOCIATE;
 		request.setAttribute("messaggioErrore",messaggio);
 		session.setAttribute("chiudi", "chiudi");
 		%>
  		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
 		<%
 		return;
 	}
 	
    
 	
 	// Ricerco gli usi primari legati all'azienda
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
	if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
	{
 		try 
 		{
	 		elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
	 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
 		}
 		catch(SolmrException se) 
 		{
			messaggioErrore = (String)AnagErrors.ERRORE_KO_USO_PRIMARIO;
			request.setAttribute("messaggioErrore", messaggioErrore);
			session.setAttribute("chiudi", "chiudi");
	    %>
	      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
	    <%
	    return;
 		}
	}
	else 
	{
		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
	}	
	
	
	// Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSelezionati[i]));
            elencoDestinazione.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzo.getIdUtilizzo());
            elencoDestinazione.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    request.setAttribute("elencoDestinazione", elencoDestinazione);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_DESTINAZIONE_PRIMARIA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  
  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]));
            elencoDettUso.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione());
            elencoDettUso.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    request.setAttribute("elencoDettUso", elencoDettUso);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_DETT_USO_PRIMARIA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  
  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]));
            elencoQualitaUso.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso());
            elencoQualitaUso.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    request.setAttribute("elencoQualitaUso", elencoQualitaUso);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }

  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer,Vector<TipoVarietaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]));
            elencoVarieta.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso());
            elencoVarieta.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    request.setAttribute("elencoVarieta", elencoVarieta);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_VARIETA_PRIMARIA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  // Ricerco il periodo semina in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), Long.decode(elencoQualitaUsoSelezionati[i]),
              Long.decode(elencoVarietaSelezionate[i]));
            elencoPerSemina.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzo.getIdUtilizzo(),
              utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso(),
              utilizzo.getIdVarieta());
            elencoPerSemina.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    request.setAttribute("elencoPerSemina", elencoPerSemina);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  
  // Ricerco il pratica Mantenim in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = new Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>();
  Hashtable<Integer,String> elencoFaseAllev = new Hashtable<Integer,String>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSelezionati[i])) 
          {
            CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(Long.decode(elencoUtilizziSelezionati[i]),
              Long.decode(elencoVarietaSelezionate[i]), Long.decode(elencoDestSelezionati[i]), Long.decode(elencoDettUsoSelezionati[i]), 
              Long.decode(elencoQualitaUsoSelezionati[i]));
            Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
            Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
            elencoPerMantenim.put(new Integer(i), vPraticaMantenim);
            
            String faseAllevamento = "N";
            if("S".equalsIgnoreCase(catalogoMatricePraticVO.getPermanente()))
              faseAllevamento = "S";
            elencoFaseAllev.put(new Integer(i), faseAllevamento);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzo())) 
          {
            CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzo.getIdUtilizzo(),
              utilizzo.getIdVarieta(), utilizzo.getIdTipoDestinazione(), utilizzo.getIdTipoDettaglioUso(), utilizzo.getIdTipoQualitaUso());
            Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
            Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
            elencoPerMantenim.put(new Integer(i), vPraticaMantenim);
            
            String faseAllevamento = "N";
            if("S".equalsIgnoreCase(catalogoMatricePraticVO.getPermanente()))
              faseAllevamento = "S";
            elencoFaseAllev.put(new Integer(i), faseAllevamento);
          }
        }
      }
    }
    request.setAttribute("elencoPerMantenim", elencoPerMantenim);
    request.setAttribute("elencoFaseAllev", elencoFaseAllev);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_PRATICA_MANTENIMENTO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }  
	
	
	
 	
	// Ricerco gli usi secondari legati all'azienda
	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloSecondario");
 	if(elencoTipiUsoSuoloSecondario == null || elencoTipiUsoSuoloSecondario.size() == 0) 
 	{
		try 
		{
			elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
	 		request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
		}
		catch(SolmrException se) 
		{
			messaggioErrore = (String)AnagErrors.ERRORE_KO_USO_SECONDARIO;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
			<%
		}
 	}
 	else 
 	{
 		request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
 	}
 	
 	
	// Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]));
            elencoDestinazioneSecondario.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzo.getIdUtilizzoSecondario());
            elencoDestinazioneSecondario.put(new Integer(i), vDestinazione);
          }
        }
      }
    }
    request.setAttribute("elencoDestinazioneSecondario", elencoDestinazioneSecondario);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_DEST_USO_SECONDARIO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]));
            elencoDettUsoSecondario.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoDettaglioUsoVO> vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario());
            elencoDettUsoSecondario.put(new Integer(i), vDettUso);
          }
        }
      }
    }
    request.setAttribute("elencoDettUsoSecondario", elencoDettUsoSecondario);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_DETTAGLIO_USO_SECONDARIO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  
  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]));
            elencoQualitaUsoSecondario.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario());
            elencoQualitaUsoSecondario.put(new Integer(i), vQualitaUso);
          }
        }
      }
    }
    request.setAttribute("elencoQualitaUsoSecondario", elencoQualitaUsoSecondario);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_QUALITA_USO_SECONDARIO;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  }
  
  // Ricerco la varietà secondaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = new Hashtable<Integer,Vector<TipoVarietaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]));
            elencoVarietaSecondaria.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoVarietaVO> vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario(), utilizzo.getIdTipoQualitaUsoSecondario());
            elencoVarietaSecondaria.put(new Integer(i), vVarieta);
          }
        }
      }
    }
    request.setAttribute("elencoVarietaSecondaria", elencoVarietaSecondaria);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_VARIETA_SECONDARIA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  } 
  
  // Ricerco il periodo semina in relazione alla mtarice
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
  try 
  {
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(elencoUtilizziSecondariSelezionati != null) 
      {
        for(int i = 0; i < elencoUtilizziSecondariSelezionati.length; i++) 
        {
          if(Validator.isNotEmpty(elencoUtilizziSecondariSelezionati[i])) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(Long.decode(elencoUtilizziSecondariSelezionati[i]),
              Long.decode(elencoDestSecondariSelezionati[i]), Long.decode(elencoDettUsoSecondariSelezionati[i]), Long.decode(elencoQualitaUsoSecondariSelezionati[i]),
              Long.decode(elencoVarietaSecondarieSelezionate[i]));
            elencoPerSeminaSecondario.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    else 
    {
      if(vUtilizziVO != null) 
      {
        for(int i = 0; i < vUtilizziVO.size(); i++) 
        {
          UtilizzoParticellaVO utilizzo = vUtilizziVO.get(i);
          if(Validator.isNotEmpty(utilizzo.getIdUtilizzoSecondario())) 
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzo.getIdUtilizzoSecondario(),
              utilizzo.getIdTipoDestinazioneSecondario(), utilizzo.getIdTipoDettaglioUsoSecondario(), utilizzo.getIdTipoQualitaUsoSecondario(),
              utilizzo.getIdVarietaSecondaria());
            elencoPerSeminaSecondario.put(new Integer(i), vPerSemina);
          }
        }
      }
    }
    request.setAttribute("elencoPerSeminaSecondario", elencoPerSeminaSecondario);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA_SEC;
    request.setAttribute("messaggioErrore", messaggioErrore);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
    <%
    return;
  } 
	
	
 	
 	// Ricerco i tipi impianti attivi
 	TipoImpiantoVO[] elencoTipoImpianto = null;
 	try 
 	{
 		elencoTipoImpianto = anagFacadeClient.getListTipoImpianto(true, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
 		request.setAttribute("elencoTipoImpianto", elencoTipoImpianto);
 	}
 	catch(SolmrException se) 
 	{
		messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_IMPIANTO;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
		<%
	}
  
  
  
  // TIPO EFA
  Vector<TipoEfaVO> vTipoEfa = null;
  try 
  {
    vTipoEfa = gaaFacadeClient.getListTipoEfa();
    request.setAttribute("vTipoEfa", vTipoEfa);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - popModificaTerritorialeCondUsoUrlCtrl.jsp - FINE PAGINA");
    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_EFA+".\n"+se.toString();
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
    <%
  }
  
  //mi cerco e raggruppo gli utilizzi Efa
  // e li differenzio dagli altri utilizzi
  // solo per la conduione della particella selezionata!!!!
  StoricoParticellaVO StoricoParticellaEfaVO = elencoStoricoParticella[new Integer(numero).intValue()];
  ConduzioneParticellaVO conduzioneEfaVO = StoricoParticellaEfaVO.getElencoConduzioni()[0];
  UtilizzoParticellaVO[] elencoUtilizziEfaVO = conduzioneEfaVO.getElencoUtilizzi();
  
  
  String[] arrIdTipoEfa = request.getParameterValues("idTipoEfa");
  String[] elencoUtilizziEfaSelezionati = request.getParameterValues("idTipoUtilizzoEfa");
  String[] elencoDestinazioneEfaSelezionate = request.getParameterValues("idTipoDestinazioneEfa");
  String[] elencoDettaglioUsoEfaSelezionate = request.getParameterValues("idTipoDettaglioUsoEfa");
  String[] elencoQualitaUsoEfaSelezionate = request.getParameterValues("idTipoQualitaUsoEfa");
  String[] elencoVarietaEfaSelezionate = request.getParameterValues("idVarietaEfa");
  String[] elencoValoreOriginale = request.getParameterValues("valoreOriginale");
  
  
  Hashtable<Integer, Vector<TipoUtilizzoVO>> elencoUtilizziEfa = new Hashtable<Integer, Vector<TipoUtilizzoVO>>();
  if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
  {
	  if(Validator.isNotEmpty(arrIdTipoEfa))
	  {
	    for(int i = 0; i < arrIdTipoEfa.length; i++) 
	    {
	      if(Validator.isNotEmpty(arrIdTipoEfa[i]))
	      { 
		      Vector<TipoUtilizzoVO> vUtilizzi = gaaFacadeClient.getListTipoUtilizzoEfa(new Long(arrIdTipoEfa[i]));
		      if(vUtilizzi != null)
		        elencoUtilizziEfa.put(new Integer(i), vUtilizzi);
		      else
		        elencoUtilizziEfa.put(new Integer(i), new Vector<TipoUtilizzoVO>());
		    }
		    else
		    {
		      elencoUtilizziEfa.put(new Integer(i), new Vector<TipoUtilizzoVO>());
		    }  
	    }   
	  }
	}
	//prima volta che entro
	else
	{
	  if(Validator.isNotEmpty(vIdTipoEfa))
    {
      for(int i = 0; i < vIdTipoEfa.size(); i++) 
      { 
        Vector<TipoUtilizzoVO> vUtilizzi = gaaFacadeClient.getListTipoUtilizzoEfa(vIdTipoEfa.get(i));
        if(vUtilizzi != null)
          elencoUtilizziEfa.put(new Integer(i), vUtilizzi);
        else
          elencoUtilizziEfa.put(new Integer(i), new Vector<TipoUtilizzoVO>());      
      }   
    }
	}
  request.setAttribute("elencoUtilizziEfa", elencoUtilizziEfa);
  
  
  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer, Vector<TipoDestinazioneVO>> elencoDestinazioneEfa = new Hashtable<Integer, Vector<TipoDestinazioneVO>>();
  if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      { 
        if(Validator.isNotEmpty(arrIdTipoEfa[i])
          && Validator.isNotEmpty(elencoUtilizziEfaSelezionati[i]))
        {        
          Vector<TipoDestinazioneVO> vTipoDestinazione = gaaFacadeClient.getListTipoDestinazioneEfa(
            new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziEfaSelezionati[i]));
          elencoDestinazioneEfa.put(new Integer(i), vTipoDestinazione);
        }
        else
        {
          elencoDestinazioneEfa.put(new Integer(i), new Vector<TipoDestinazioneVO>());
        }       
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vIdTipoEfa))
    {
      for(int i = 0; i < vIdTipoEfa.size(); i++) 
      { 
        Vector<TipoDestinazioneVO> vTipoDestinazione = null;
        if(Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i)))
        { 
          vTipoDestinazione = gaaFacadeClient.getListTipoDestinazioneEfa(
            vIdTipoEfa.get(i), vIdTipoUtilizzoEfa.get(i));
        }
        if(vTipoDestinazione == null)
          vTipoDestinazione = new Vector<TipoDestinazioneVO>();
          
        elencoDestinazioneEfa.put(new Integer(i), vTipoDestinazione);        
      }
    } 
  }
  request.setAttribute("elencoDestinazioneEfa", elencoDestinazioneEfa);
  
  
  // Recupero la varietà primaria relativa ai tipi uso suolo utilizzati dall'azienda in esame  
  Hashtable<Integer, Vector<TipoDettaglioUsoVO>> elencoDettaglioUsoEfa = new Hashtable<Integer, Vector<TipoDettaglioUsoVO>>();
  if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i])
          && Validator.isNotEmpty(elencoUtilizziEfaSelezionati[i]) && Validator.isNotEmpty(elencoDestinazioneEfaSelezionate[i]))
        {         
          Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(new Long(arrIdTipoEfa[i]), 
            new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]));
          elencoDettaglioUsoEfa.put(new Integer(i), vTipoDettaglioUso);
        }
        else
        {
          elencoDettaglioUsoEfa.put(new Integer(i), new Vector<TipoDettaglioUsoVO>());
        }
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vIdTipoEfa))
    {
      for(int i = 0; i < vIdTipoEfa.size(); i++) 
      {
        Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
        if(Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i))
          && Validator.isNotEmpty(vIdTipoDestinazioneEfa.get(i)))
        {  
          vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(
            vIdTipoEfa.get(i), vIdTipoUtilizzoEfa.get(i), vIdTipoDestinazioneEfa.get(i));
        }
        if((vTipoDettaglioUso == null) && Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i))
          && (elencoDestinazioneEfa.get(new Integer(i)).size() > 0))
        {
          vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoEfaByMatrice(
            vIdTipoEfa.get(i), 
            vIdTipoUtilizzoEfa.get(i), 
            elencoDestinazioneEfa.get(new Integer(i)).get(0).getIdTipoDestinazione());
        }
        
        if(vTipoDettaglioUso == null)
        {
          vTipoDettaglioUso = new Vector<TipoDettaglioUsoVO>();
        }
        
        elencoDettaglioUsoEfa.put(new Integer(i), vTipoDettaglioUso);          
      }
    } 
  }
  request.setAttribute("elencoDettaglioUsoEfa", elencoDettaglioUsoEfa);
  
  
  
  // Recupero la varietà primaria relativa ai tipi uso suolo utilizzati dall'azienda in esame  
  Hashtable<Integer, Vector<TipoQualitaUsoVO>> elencoQualitaUsoEfa = new Hashtable<Integer, Vector<TipoQualitaUsoVO>>();
  if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      {
        if(Validator.isNotEmpty(arrIdTipoEfa[i]) && Validator.isNotEmpty(elencoUtilizziEfaSelezionati[i])
          && Validator.isNotEmpty(elencoDestinazioneEfaSelezionate[i]) && Validator.isNotEmpty(elencoDettaglioUsoEfaSelezionate[i]))
        {         
          Vector<TipoQualitaUsoVO> vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(new Long(arrIdTipoEfa[i]), 
            new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]), 
            new Long(elencoDettaglioUsoEfaSelezionate[i]));
          elencoQualitaUsoEfa.put(new Integer(i), vTipoQualitaUso);
        }
        else
        {
          elencoQualitaUsoEfa.put(new Integer(i), new Vector<TipoQualitaUsoVO>());
        }
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vIdTipoEfa))
    {
      for(int i = 0; i < vIdTipoEfa.size(); i++) 
      {
        Vector<TipoQualitaUsoVO> vTipoQualitaUso = null;
        if(Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i)) && Validator.isNotEmpty(vIdTipoDestinazioneEfa.get(i))
          && Validator.isNotEmpty(vIdTipoDettaglioUsoEfa.get(i)))
        {  
          vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(
            vIdTipoEfa.get(i), vIdTipoUtilizzoEfa.get(i), vIdTipoDestinazioneEfa.get(i), 
            vIdTipoDettaglioUsoEfa.get(i));
        }
        if((vTipoQualitaUso == null) && Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i)) 
          && (elencoDestinazioneEfa.get(new Integer(i)).size() > 0)
          && (elencoDettaglioUsoEfa.get(new Integer(i)).size() > 0))
        {
          vTipoQualitaUso = gaaFacadeClient.getListQualitaUsoEfaByMatrice(
            vIdTipoEfa.get(i), 
            vIdTipoUtilizzoEfa.get(i), 
            elencoDestinazioneEfa.get(new Integer(i)).get(0).getIdTipoDestinazione(),
            elencoDettaglioUsoEfa.get(new Integer(i)).get(0).getIdTipoDettaglioUso());
        } 
        
        if(vTipoQualitaUso == null)
        {
          vTipoQualitaUso = new Vector<TipoQualitaUsoVO>();
        }
         
        
          
        elencoQualitaUsoEfa.put(new Integer(i), vTipoQualitaUso);
        
      }
    } 
  }
  request.setAttribute("elencoQualitaUsoEfa", elencoQualitaUsoEfa);    
    
    
    
  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
  Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarietaEfa = new Hashtable<Integer, Vector<TipoVarietaVO>>();
  if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
  {
    if(Validator.isNotEmpty(arrIdTipoEfa)) 
    {
      for(int i = 0; i < arrIdTipoEfa.length; i++) 
      { 
        if(Validator.isNotEmpty(arrIdTipoEfa[i]) && Validator.isNotEmpty(elencoUtilizziEfaSelezionati[i])
          && Validator.isNotEmpty(elencoDestinazioneEfaSelezionate[i]) && Validator.isNotEmpty(elencoDettaglioUsoEfaSelezionate[i])
          && Validator.isNotEmpty(elencoQualitaUsoEfaSelezionate[i]))
        {        
          Vector<TipoVarietaVO> varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
            new Long(arrIdTipoEfa[i]), new Long(elencoUtilizziEfaSelezionati[i]), new Long(elencoDestinazioneEfaSelezionate[i]),
            new Long(elencoDettaglioUsoEfaSelezionate[i]), new Long(elencoQualitaUsoEfaSelezionate[i]));
          elencoVarietaEfa.put(new Integer(i), varieta);
        }
        else
        {
          elencoVarietaEfa.put(new Integer(i), new Vector<TipoVarietaVO>());
        }       
      }
    }
  }
  //prima volta che entro
  else
  {
    if(Validator.isNotEmpty(vIdTipoEfa))
    {
      for(int i = 0; i < vIdTipoEfa.size(); i++) 
      {  
        Vector<TipoVarietaVO> varieta = null;
        if(Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i)) && Validator.isNotEmpty(vIdTipoDestinazioneEfa.get(i))
          && Validator.isNotEmpty(vIdTipoDettaglioUsoEfa.get(i)) && Validator.isNotEmpty(vIdTipoQualitaUsoEfa.get(i)))
        {
          varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
            vIdTipoEfa.get(i), vIdTipoUtilizzoEfa.get(i), vIdTipoDestinazioneEfa.get(i), 
            vIdTipoDettaglioUsoEfa.get(i), vIdTipoQualitaUsoEfa.get(i));
        }
        if((varieta == null) && Validator.isNotEmpty(vIdTipoUtilizzoEfa.get(i)) 
          && (elencoDestinazioneEfa.get(new Integer(i)).size() > 0)
          && (elencoDettaglioUsoEfa.get(new Integer(i)).size() > 0)
          && (elencoQualitaUsoEfa.get(new Integer(i)).size() > 0))
        {
          varieta = gaaFacadeClient.getListTipoVarietaEfaByMatrice(
            vIdTipoEfa.get(i), 
            vIdTipoUtilizzoEfa.get(i), 
            elencoDestinazioneEfa.get(new Integer(i)).get(0).getIdTipoDestinazione(),
            elencoDettaglioUsoEfa.get(new Integer(i)).get(0).getIdTipoDettaglioUso(),
            elencoQualitaUsoEfa.get(new Integer(i)).get(0).getIdTipoQualitaUso());    
        } 
        
        if(varieta == null)
        {
          varieta = new Vector<TipoVarietaVO>();
        }
        
             
        elencoVarietaEfa.put(new Integer(i), varieta);
      }
    } 
  }
  request.setAttribute("elencoVarietaEfa", elencoVarietaEfa);
  
  
  
  
  
  
 	
 	// L'utente ha selezionato il pulsante conferma
 	if(Validator.isNotEmpty(request.getParameter("confermo"))) 
  {
 		//request.setAttribute("reload", "reload");
 		// Setto e controllo la validita dei parametri inseriti relativi alla conduzione
 		// ma non setto i nuovi parametri nel VO fino a quando non ho terminato i controlli
 		// di validità:questo per risolvere il problema del multi accesso ai dati
 		// che sono costretto a mantenere in sessione
 		ConduzioneParticellaVO conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
 		errors = conduzioneParticellaVO.validateModificaTerritorialeCondUso(request);    
    
 		errors = storicoParticellaVO.validateModificaTerritorialeCondUso(request, errors);
 		
 		
 		
 		//numero utilizzi con ajax
    int numeroUtilizzi = 0;
    if(Validator.isNotEmpty(elencoUtilizziSelezionati))
    {
      numeroUtilizzi = elencoUtilizziSelezionati.length;
    } 		
 		
 		
 		Hashtable<Integer,ValidationErrors> erroriUtilizzi = new Hashtable<Integer,ValidationErrors>();
 		Hashtable<Integer,ValidationErrors> erroriUtilizziEfa = new Hashtable<Integer,ValidationErrors>();
 		Hashtable<Integer,ValidationErrors> erroriPianteConsociate = new Hashtable<Integer,ValidationErrors>();
 		//Hashtable<Integer,ValidationErrors> erroriDettaglioUso = new Hashtable<Integer,ValidationErrors>();
    
 		double totSupUtilizzata = 0;
 		
 	  for(int i = 0; i < numeroUtilizzi; i++) 
    {
      UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
 			
 			String flagFruttaGuscio = "N";
 			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")) 
         && i < request.getParameterValues("idTipoUtilizzo").length
         && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) 
      {
        TipoUtilizzoVO tipoUtilizzoVOSel = anagFacadeClient.findTipoUtilizzoByPrimaryKey(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
        flagFruttaGuscio = tipoUtilizzoVOSel.getFlagFruttaGuscio();
      }
 			
 			ValidationErrors errori = utilizzoParticellaVO.validateModificaTerritorialeCondUso(request, null, flagFruttaGuscio, i);			
 			
 			
 			
      
      
      if(errori.size() > 0) 
      {
        isKoUtilizzi = true;
      }			
 			
 			
 			if(Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[i]) 
        && Validator.validateDouble(request.getParameterValues("supUtilizzata")[i], 999999.9999) != null 
        && Double.parseDouble(request.getParameterValues("supUtilizzata")[i].replace(',', '.')) > 0) 
      {
        String tmp = request.getParameterValues("supUtilizzata")[i];
 				totSupUtilizzata += Double.parseDouble(request.getParameterValues("supUtilizzata")[i].replace(',', '.'));
 				totSupUtilizzata = NumberUtils.arrotonda(totSupUtilizzata, 4);
 			}
			// Se l'utente ha scelto un tipo impianto allora ne ricerco i dati per
	 		// poter effettuare i controlli incrociati con le piante consociate
	 		TipoImpiantoVO tipoImpiantoVO = null;
	 		if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i])) 
      {
	 			try 
        {
	 				tipoImpiantoVO = anagFacadeClient.findTipoImpiantoByPrimaryKey(Long.decode(request.getParameterValues("idImpianto")[i]));
	 				utilizzoParticellaVO.setTipoImpiantoVO(tipoImpiantoVO);
	 			}
	 			catch(SolmrException se) 
        {
	 				messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_IMPIANTO;
	 				request.setAttribute("messaggioErrore", messaggioErrore);
	 				%>
	 					<jsp:forward page="<%= popModificaTerritorialeCondUsoUrl %>" />
	 				<%
	 			}
	 		}
 			
      int numPianteConsociate = 0;
      if(Validator.isNotEmpty(elencoPianteConsociate))
      {
        numPianteConsociate = elencoPianteConsociate.size();
      }
 			if(numPianteConsociate != 0)  
      {
 				for(int a = 0; a < numPianteConsociate; a++) 
        {
 					UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
 					if(i == 0) 
          {
 						ValidationErrors erroriCons = utilizzoConsociatoVO.validateModificaTerritorialeCondUso(request, null, tipoImpiantoVO, a);
 			 			if(erroriCons.size() > 0) 
            {
 			 				isKoPiante = true;
 			 			}
 						erroriPianteConsociate.put(new Integer(a), erroriCons);
 					}
 					else 
          {
 						ValidationErrors erroriCons = utilizzoConsociatoVO.validateModificaTerritorialeCondUso(request, null, tipoImpiantoVO, ((i*numPianteConsociate)+a));
 			 			if(erroriCons.size() > 0) 
            {
 			 				isKoPiante = true;
 			 			}
 						erroriPianteConsociate.put(new Integer(((i*numPianteConsociate)+a)), erroriCons);
 					}
 				}
 			}
 			
 			
 			if(!isKoUtilizzi)
      {
        if(Validator.isNotEmpty(particellaCertificataVO)
          && Validator.isNotEmpty(particellaCertificataVO.getIdParticellaCertificata()))
        {
          CatalogoMatriceVO catalogoMatriceVO = null;
          CatalogoMatriceVO catalogoMatriceVOSecondario = null; 
          utilizzoParticellaVO.setIdUtilizzo(new Long(request.getParameterValues("idTipoUtilizzo")[i]));
          utilizzoParticellaVO.setIdTipoDestinazione(new Long(request.getParameterValues("idTipoDestinazione")[i]));
          utilizzoParticellaVO.setIdTipoDettaglioUso(new Long(request.getParameterValues("idTipoDettaglioUso")[i]));
          utilizzoParticellaVO.setIdTipoQualitaUso(new Long(request.getParameterValues("idTipoQualitaUso")[i]));
          utilizzoParticellaVO.setIdVarieta(new Long(request.getParameterValues("idVarieta")[i]));
          catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
            utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
            utilizzoParticellaVO.getIdTipoQualitaUso());
          utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
              
          if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]))
          {
            utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(request.getParameterValues("idTipoUtilizzoSecondario")[i]));
            utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(request.getParameterValues("idTipoDestinazioneSecondario")[i]));
            utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]));
            utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(request.getParameterValues("idTipoQualitaUsoSecondario")[i]));
            utilizzoParticellaVO.setIdVarietaSecondaria(new Long(request.getParameterValues("idVarietaSecondaria")[i]));
            catalogoMatriceVOSecondario = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(), 
              utilizzoParticellaVO.getIdVarietaSecondaria(), utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), 
              utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario());
            utilizzoParticellaVO.setIdCatalogoMatriceSecondario(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
          }
          
          
          if(Validator.isNotEmpty(catalogoMatriceVO))
          {
            //TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
            String flagPratoPermanente = catalogoMatriceVO.getFlagPratoPermanente();
              
            boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                particellaCertificataVO.getIdParticellaCertificata().longValue());
            
            String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(), 4);
             
            if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
              && !"1".equalsIgnoreCase(valore))
            {
              if(!"S".equalsIgnoreCase(flagPratoPermanente))
              {
                errori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
                isKoUtilizzi = true;
              }
            }
          }
          
          if(Validator.isNotEmpty(catalogoMatriceVOSecondario))
          {
            //TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
            String flagPratoPermanente = catalogoMatriceVOSecondario.getFlagPratoPermanente();
              
            boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                particellaCertificataVO.getIdParticellaCertificata().longValue());
            
            String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoParticellaVO.getIdParticella(), 4);
             
            if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
              && !"1".equalsIgnoreCase(valore))
            {
              if(!"S".equalsIgnoreCase(flagPratoPermanente))
              {
                errori.add("idTipoUtilizzoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
                isKoUtilizzi = true;
              }
            }
          }          
          
          
        }
        
        //erroriUtilizzi.put(new Integer(i), errori);
      
      }
    
 			
 			erroriUtilizzi.put(new Integer(i), errori);
 		}
 	
 		
 		
 		// Effettuo il controllo sul supero della superficie grafica/catastale da parte delle
 		// superfici utilizzate solo se i controlli di correttezza formale sulla 
 		// superficie condotta sono stati superati
 		String supConfronto = AnagUtils.valSupCatGraf(
      storicoParticellaVO.getSupCatastale(), storicoParticellaVO.getSuperficieGrafica());    
    
 		
 		//controlli utilizzi efa
 		int numeroUtilizziEfa = 0;
    if(Validator.isNotEmpty(elencoUtilizziEfaSelezionati))
    {
      numeroUtilizziEfa = elencoUtilizziEfaSelezionati.length;
    }
    
    for(int i=0;i<numeroUtilizziEfa;i++)
    {
      UtilizzoParticellaVO utilizzoParticellaEfaVO = new UtilizzoParticellaVO();
      ValidationErrors errori = utilizzoParticellaEfaVO.validateModificaTerritorialeCondUsoEfa(request, null, i);
      
      if(errori.size() > 0) 
      {
        isKoUtilizziEfa = true;
      }
      
      
      if(Validator.isNotEmpty(request.getParameterValues("valoreDopoConversione")[i]) 
        && Validator.validateDouble(request.getParameterValues("valoreDopoConversione")[i], 999999.9999) != null 
        && Double.parseDouble(request.getParameterValues("valoreDopoConversione")[i].replace(',', '.')) > 0) 
      {
        String tmp = request.getParameterValues("valoreDopoConversione")[i];
        totSupUtilizzata += Double.parseDouble(request.getParameterValues("valoreDopoConversione")[i].replace(',', '.'));
        totSupUtilizzata = NumberUtils.arrotonda(totSupUtilizzata, 4);
      } 
      
        
      
      erroriUtilizziEfa.put(new Integer(i), errori);
    }
 		
 		
 		
 		
 		// PERCENTUALE POSSESSO
    if(Validator.isNotEmpty(request.getParameter("percentualePossesso"))) 
    {
      if(!Validator.isNumberPercentualeMaggioreZeroDecimali(request.getParameter("percentualePossesso")))
      {
        errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_CONDUZIONE));
      }
    }
    else 
    {
      errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
 		
 		
 		
 		
 		//SUPERFICIE CONDOTTA
 		if(errors.size() == 0) 
    {
	    double supCondottaDb = Double.parseDouble(request.getParameter("supCondotta").replace(',', '.'));
	    if(supCondottaDb == 0)
	    {
	      errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_ZERO));
	    }
	    else
	    {
	      //Il controllo è fatto solo se la particella non è provvisoria
	      if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
	      {
		      double maxSupGrafCatDb = Double.parseDouble(supConfronto.replace(',', '.'));
		      if(supCondottaDb > maxSupGrafCatDb)
		      {
		        errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MAX_CAT_GRAF));       
		      }
		      else
		      {
		        //Nel caso di diverso dell'assevimento devo controllare che la condotta 
		        //sia maggiore della somma della superficie degli utilizzi della medesima conduzione
		        if(Long.decode(request.getParameter("idTitoloPossesso")).intValue() != 5)
		        {
		          if(supCondottaDb < totSupUtilizzata)
		          {
		            errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
		          }
		        }
		      }
		      
		    }
	      
	      
	    }
	  }
    
 		
 		
 		
 		// Se si sono verificati errori li visualizzo
 		if(errors.size() > 0 || isKoUtilizzi || isKoPiante || isKoUtilizziEfa) 
    {
 			request.setAttribute("errors", errors);
 			request.setAttribute("erroriUtilizzi", erroriUtilizzi);
 			request.setAttribute("erroriUtilizziEfa", erroriUtilizziEfa);
 			//request.setAttribute("erroriDettaglioUso", erroriDettaglioUso);
 			request.setAttribute("erroriPianteConsociate", erroriPianteConsociate);
 	    request.getRequestDispatcher(popModificaTerritorialeCondUsoUrl).forward(request, response);
 	    return;
 		}
 		if(!Validator.isNotEmpty(request.getParameter("confermaSup"))) 
    {
 			if(NumberUtils.arrotonda(totSupUtilizzata, 4) 
        < NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
      {
 				request.setAttribute("confermaSup", "confermaSup");
 				request.getRequestDispatcher(popModificaTerritorialeCondUsoUrl).forward(request, response);
 	    	return;
 			}			
 		}
 		// Se ho passato tutti i controlli allora setto i nuovi valori all'interno del VO
 		// DATI CONDUZIONE
 		conduzioneParticellaVO.setIdTitoloPossesso(Long.decode(request.getParameter("idTitoloPossesso")));
 		
 		
 		
 		conduzioneParticellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(request.getParameter("supCondotta")));
 		
    conduzioneParticellaVO.setPercentualePossesso(new BigDecimal(request.getParameter("percentualePossesso").replace(',', '.')));
    
    
     
    
 		ConduzioneParticellaVO[] elencoConduzioni = storicoParticellaVO.getElencoConduzioni();
 		
 		
 		//salvataggio valori tipo area...
    Vector<TipoAreaVO> vTipoArea = storicoParticellaVO.getvValoriTipoArea();
    String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
    String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
    int numValore = 0;
    for(int j=0;j<vTipoArea.size();j++)
    {
      if(j%2 == 0)
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea1[numValore]);
      }
      else
      {
        vTipoArea.get(j).getvTipoValoreArea().get(0).setValore(arrValoreArea2[numValore]);
        numValore++;
      }     
    }
 		// DATI TERRITORIALI
 		/*storicoParticellaVO.setIdAreaA(Long.decode(request.getParameter("idAreaA")));
 		storicoParticellaVO.setIdAreaB(Long.decode(request.getParameter("idAreaB")));
 		storicoParticellaVO.setIdAreaC(Long.decode(request.getParameter("idAreaC")));
 		storicoParticellaVO.setIdAreaD(Long.decode(request.getParameter("idAreaD")));
 		storicoParticellaVO.setIdAreaM(Long.decode(request.getParameter("idAreaM")));
    storicoParticellaVO.setIdPotenzialitaIrrigua(Long.decode(request.getParameter("idPotenzialitaIrrigua")));*/
    storicoParticellaVO.setIdTerrazzamento(Long.decode(request.getParameter("idTerrazzamento")));
    storicoParticellaVO.setIdRotazioneColturale(Long.decode(request.getParameter("idRotazioneColturale")));
 		if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_S);
      String idIrrigazione = request.getParameter("idIrrigazione");
      if(Validator.isNotEmpty(idIrrigazione))
      {
        //messo -1 come parametro della combo irrigazione per segnalare nel caso di modifica la scelta 
        // del null!!!
        if(idIrrigazione.equalsIgnoreCase("-1"))
        {
 			    storicoParticellaVO.setIdIrrigazione(null);
        }
        else
        {
          storicoParticellaVO.setIdIrrigazione(Long.decode(idIrrigazione));
        }
      }
      else
      {
        storicoParticellaVO.setIdIrrigazione(null);
      }
 		}
 		else 
    {
 			storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
      storicoParticellaVO.setIdIrrigazione(null);
 		}
 		if(Validator.isNotEmpty(request.getParameter("flagCaptazionePozzi"))) 
    {
 			storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_S);
 		}
 		else {
 			storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) 
    {
      storicoParticellaVO.setIdMetodoIrriguo(Long.decode(request.getParameter("idMetodoIrriguo")));
    }
    else 
    {
      storicoParticellaVO.setIdMetodoIrriguo(null);
    }
 		if(Validator.isNotEmpty(request.getParameter("idDocumentoProtocollato"))) {
 			storicoParticellaVO.setIdDocumentoProtocollato(Long.decode(request.getParameter("idDocumentoProtocollato")));
 		}
 		if(Validator.isNotEmpty(request.getParameter("idCausaleModParticella"))) {
 			storicoParticellaVO.setIdCausaleModParticella(Long.decode(request.getParameter("idCausaleModParticella")));
 		}
 		if(Validator.isNotEmpty(request.getParameter("motivoModifica"))) {
 			storicoParticellaVO.setMotivoModifica(request.getParameter("motivoModifica"));
 		}
 		// DATI UTILIZZO
 		Vector<UtilizzoParticellaVO> vUtilizzi = new Vector<UtilizzoParticellaVO>();
 		for(int i = 0; i < numeroUtilizzi; i++) 
    {
 			UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
 			utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
 			utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
      utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));
      utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));
      utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));
      utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
      
      CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
          utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
          utilizzoParticellaVO.getIdTipoQualitaUso());
      utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
          

      
      utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzata")[i]));
      
      utilizzoParticellaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[i]));
      utilizzoParticellaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[i]));
      utilizzoParticellaVO.setDataInizioDestinazione(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazione")[i]));
      utilizzoParticellaVO.setDataFineDestinazione(DateUtils.parseDate(request.getParameterValues("dataFineDestinazione")[i]));
      
      
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i])) 
      {
        utilizzoParticellaVO.setIdUtilizzoSecondario(new Long(request.getParameterValues("idTipoUtilizzoSecondario")[i]));
        utilizzoParticellaVO.setIdTipoDestinazioneSecondario(new Long(request.getParameterValues("idTipoDestinazioneSecondario")[i]));
        utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(new Long(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]));
        utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(new Long(request.getParameterValues("idTipoQualitaUsoSecondario")[i]));
        utilizzoParticellaVO.setIdVarietaSecondaria(new Long(request.getParameterValues("idVarietaSecondaria")[i]));
        CatalogoMatriceVO catalogoMatriceVOSecondario = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzoSecondario(), 
           utilizzoParticellaVO.getIdVarietaSecondaria(), utilizzoParticellaVO.getIdTipoDestinazioneSecondario(), 
           utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario(), utilizzoParticellaVO.getIdTipoQualitaUsoSecondario());
        utilizzoParticellaVO.setIdCatalogoMatriceSecondario(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
        utilizzoParticellaVO.setSupUtilizzataSecondaria(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzataSecondaria")[i]));
        
        
        utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]));
        utilizzoParticellaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]));
        utilizzoParticellaVO.setDataInizioDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazioneSec")[i]));
        utilizzoParticellaVO.setDataFineDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataFineDestinazioneSec")[i]));
        
      }
      else
      {
        utilizzoParticellaVO.setIdUtilizzoSecondario(null);
        utilizzoParticellaVO.setIdTipoDestinazioneSecondario(null);
        utilizzoParticellaVO.setIdTipoDettaglioUsoSecondario(null);
        utilizzoParticellaVO.setIdTipoQualitaUsoSecondario(null);
        utilizzoParticellaVO.setIdVarietaSecondaria(null);
        utilizzoParticellaVO.setIdCatalogoMatriceSecondario(null);
        utilizzoParticellaVO.setSupUtilizzataSecondaria(null);          
        
        utilizzoParticellaVO.setIdTipoPeriodoSeminaSecondario(null);
        utilizzoParticellaVO.setIdSeminaSecondario(null);
        utilizzoParticellaVO.setDataInizioDestinazioneSec(null);
        utilizzoParticellaVO.setDataFineDestinazioneSec(null);
      }
      
      
      
      utilizzoParticellaVO.setIdPraticaMantenimento(new Long(request.getParameterValues("idPraticaMantenimento")[i]));
      
      
      if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i])) {
        utilizzoParticellaVO.setIdFaseAllevamento(new Long(request.getParameterValues("idFaseAllevamento")[i]));
      }
      else {
        utilizzoParticellaVO.setIdFaseAllevamento(null);
      }
        
 			if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) 
      {
 				utilizzoParticellaVO.setAnnoImpianto(request.getParameterValues("annoImpianto")[i]);
 			}
 			else 
      {
 				utilizzoParticellaVO.setAnnoImpianto(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i])) {
 				utilizzoParticellaVO.setIdImpianto(Long.decode(request.getParameterValues("idImpianto")[i]));
 			}
 			else {
 				utilizzoParticellaVO.setIdImpianto(null); 				
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) {
 				utilizzoParticellaVO.setSestoSuFile(request.getParameterValues("sestoSuFile")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setSestoSuFile(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) {
 				utilizzoParticellaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]);
 			}
 			else {
 				utilizzoParticellaVO.setSestoTraFile(null);
 			}
 			if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) {
 				utilizzoParticellaVO.setNumeroPianteCeppi(request.getParameterValues("numeroPianteCeppi")[i]);
 			}
 			else 
 			{
 				utilizzoParticellaVO.setNumeroPianteCeppi(null);
 			}
 			// DATI UTILIZZO CONSOCIATI
 			UtilizzoConsociatoVO[] elencoUtilizziConsociati = null;
 			int numPianteConsociate = 0;
 			if(Validator.isNotEmpty(elencoPianteConsociate))
 			{
 			  numPianteConsociate = elencoPianteConsociate.size();
 			}
 			if(numPianteConsociate != 0)  
      {
        elencoUtilizziConsociati = new UtilizzoConsociatoVO[numPianteConsociate];
 				int valorePartenza = i*numPianteConsociate;
 				for(int a = 0; a < numPianteConsociate; a++) 
 				{
 					UtilizzoConsociatoVO utilizzoConsociatoVO = new UtilizzoConsociatoVO();
 					if(i == 0) 
 					{
 						if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[a]);
 							utilizzoConsociatoVO.setIdPianteConsociate(((TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(a)).getIdPianteConsociate());
 						}
 						else 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(null);
 						}
 					}
 					else 
 					{
 						if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valorePartenza])) 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(request.getParameterValues("numeroPianteConsociate")[valorePartenza]);
 							utilizzoConsociatoVO.setIdPianteConsociate(((TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(a)).getIdPianteConsociate());
 						}
 						else 
 						{
 							utilizzoConsociatoVO.setNumeroPiante(null);
 						}
 					}
 					valorePartenza++;
 					elencoUtilizziConsociati[a] = utilizzoConsociatoVO;
 				}
 			}
 			utilizzoParticellaVO.setElencoUtilizziConsociati(elencoUtilizziConsociati);
 			vUtilizzi.add(utilizzoParticellaVO);
 		}
 		
 		// DATI UTILIZZO EFA
    Vector<UtilizzoParticellaVO> vUtilizziEfa = new Vector<UtilizzoParticellaVO>();
    for(int i = 0; i < numeroUtilizziEfa; i++) 
    {
      UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
      utilizzoParticellaVO.setDichiarabileEfa("S");
      
      utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("valoreDopoConversione")[i]));
        
      utilizzoParticellaVO.setIdTipoEfa(Long.decode((request.getParameterValues("idTipoEfa")[i])));
      utilizzoParticellaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzoEfa")[i]));
      utilizzoParticellaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazioneEfa")[i]));
      utilizzoParticellaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUsoEfa")[i]));
      utilizzoParticellaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUsoEfa")[i]));
      utilizzoParticellaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarietaEfa")[i]));
      utilizzoParticellaVO.setAbbaPonderazione(Integer.decode(request.getParameterValues("abbPonderazioneVarieta")[i]));
       
      CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoParticellaVO.getIdUtilizzo(), 
         utilizzoParticellaVO.getIdVarieta(), utilizzoParticellaVO.getIdTipoDestinazione(), utilizzoParticellaVO.getIdTipoDettaglioUso(), 
         utilizzoParticellaVO.getIdTipoQualitaUso());
      utilizzoParticellaVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
      
      utilizzoParticellaVO.setValoreOriginale(new BigDecimal(request.getParameterValues("valoreOriginale")[i].replace(",", ".")));
      utilizzoParticellaVO.setValoreDopoConversione(new BigDecimal(request.getParameterValues("valoreDopoConversione")[i].replace(",", ".")));
      utilizzoParticellaVO.setValoreDopoPonderazione(new BigDecimal(request.getParameterValues("valoreDopoPonderazione")[i].replace(",", ".")));     
      //fatto per sicurezza!!!!
      if(Validator.isNotEmpty(utilizzoParticellaVO.getValoreOriginale())
        && Validator.isEmpty(utilizzoParticellaVO.getValoreDopoConversione()))
      {
        TipoEfaVO tipoEfaVOSel = gaaFacadeClient.getTipoEfaFromPrimaryKey(utilizzoParticellaVO.getIdTipoEfa());
      
        Integer abbaPonderazione = gaaFacadeClient.getAbbPonderazioneByMatrice(utilizzoParticellaVO.getIdTipoEfa(), 
            utilizzoParticellaVO.getIdUtilizzo(), utilizzoParticellaVO.getIdTipoDestinazione(),
            utilizzoParticellaVO.getIdTipoDettaglioUso(), utilizzoParticellaVO.getIdTipoQualitaUso(),
            utilizzoParticellaVO.getIdVarieta());
            
        utilizzoParticellaVO.setAbbaPonderazione(abbaPonderazione);
		    

        BigDecimal valoreDopoConversione = utilizzoParticellaVO.getValoreOriginale().multiply(tipoEfaVOSel.getFattoreDiConversione());
        valoreDopoConversione = valoreDopoConversione.divide(new BigDecimal(10000), 4,BigDecimal.ROUND_HALF_UP);
        utilizzoParticellaVO.setValoreDopoConversione(valoreDopoConversione);
        BigDecimal valoreDopoPonderazione = valoreDopoConversione.multiply(tipoEfaVOSel.getFattoreDiPonderazione());
        valoreDopoPonderazione = valoreDopoPonderazione.multiply(new BigDecimal(abbaPonderazione.intValue()));
        utilizzoParticellaVO.setValoreDopoPonderazione(valoreDopoPonderazione);     
      }
      
      
      vUtilizziEfa.add(utilizzoParticellaVO);
    }
      
 		
 		int numeroTotaleUtilizzi = vUtilizzi.size();
 		numeroTotaleUtilizzi = numeroTotaleUtilizzi + vUtilizziEfa.size();
 		
 		UtilizzoParticellaVO[] elencoUtilizzi = null;
 		// Se non ci sono utilizzi perchè eliminati volutamente dall'utente ne creo uno io di default
 		if(numeroTotaleUtilizzi == 0) 
    {
 			UtilizzoParticellaVO utilizzoParticellaVO = new UtilizzoParticellaVO();
 			utilizzoParticellaVO.setIdUtilizzoParticella(new Long(-1));
 			utilizzoParticellaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
 			elencoUtilizzi = new UtilizzoParticellaVO[1];
 			elencoUtilizzi[0] = utilizzoParticellaVO;
 		}
 		else
 		{
 		  int contatore = 0;
 		  elencoUtilizzi = new UtilizzoParticellaVO[numeroTotaleUtilizzi];
 		  for(int i=0;i<vUtilizzi.size();i++)
 		  {
 		    elencoUtilizzi[contatore] = vUtilizzi.get(i); 
 		    contatore++;
 		  }
 		  
 		  for(int i=0;i<vUtilizziEfa.size();i++)
      {
        elencoUtilizzi[contatore] = vUtilizziEfa.get(i); 
        contatore++;
      }
 		  
 		}
 		conduzioneParticellaVO.setElencoUtilizzi(elencoUtilizzi);
 		elencoConduzioni[0] = conduzioneParticellaVO;
 		storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
 		// Se ho passato i controlli chiudo la pop up e aggiorno la pagina chiamante
 		elencoStoricoParticella[Integer.parseInt(numero)] = storicoParticellaVO;
 		session.setAttribute("elencoStoricoParticella", elencoStoricoParticella);
 		
 		session.removeAttribute("storicoParticellaVO");
 		
 		%>
			<html>
				<head>
					<script type="text/javascript">
						window.opener.aggiungiRigaDaPopUp('<%=numero %>');
						window.close();
					</script>
				</head>
			</html>
		<%
		return;
 	}
 	
 	else 
 	{
		%>
   		<jsp:forward page="<%=popModificaTerritorialeCondUsoUrl%>" />
   	<%
 	}

%>

