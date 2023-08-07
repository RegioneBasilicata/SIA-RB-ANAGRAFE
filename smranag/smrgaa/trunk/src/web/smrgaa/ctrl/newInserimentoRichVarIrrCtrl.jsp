<%@page import="it.csi.smranag.smrgaa.util.CodiceFiscaleUtils"%>
<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String iridePageName = "newInserimentoRichVarIrrCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  

  String newInserimentoRichVarIrrUrl = "/view/newInserimentoRichVarIrrView.jsp";
  String newInserimentoConfermaVariazioneUrl = "/ctrl/newInserimentoConfermaVariazioneCtrl.jsp";  
  
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova richiesta variazione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String indietroUrl = "../layout/newInserimentoRichiestaVariazione.htm";
  //String actionUrl = "../layout/elencoRicercaRichiesta.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String msgErrore = null;
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String idAzienda = request.getParameter("idAzienda");  
  String operazione = request.getParameter("operazione");
  String regimeInserimentoRichVarIrr = request.getParameter("regimeInserimentoRichVarIrr");
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (regimeInserimentoRichVarIrr != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  

 
  
  Vector<MacchinaAziendaNuovaVO> vMacchine = null;
  AziendaNuovaVO aziendaNuovaVO = null;
  Vector<CodeDescription> vTipoMarca = null; 
  try
  {
  
    String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_VAP_IR);
    request.setAttribute("testoHelp", testoHelp);
  
    AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    request.setAttribute("anagAziendaVO", anagAziendaVO);
  
    aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        new Long(SolmrConstants.RICHIESTA_VAR_IRRORATRICI).longValue());
    
    aziendaNuovaVO.setCuaa(anagAziendaVO.getCUAA());
    aziendaNuovaVO.setDenominazione(anagAziendaVO.getDenominazione());      
    request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
    request.setAttribute("valCess", "valCess");
  
    if(regimeInserimentoRichVarIrr == null)
	  {
	    //Prima volta che entro carico tutte le macchine da db
	    gaaFacadeClient.caricaMacchineNuovaRichiesta(anagAziendaVO.getIdAzienda(), aziendaNuovaVO.getIdRichiestaAzienda());
	    vMacchine = gaaFacadeClient.getMacchineAzNuova(aziendaNuovaVO.getIdRichiestaAzienda());
	    request.setAttribute("vMacchine", vMacchine);
	  }
	  
	  String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE, (String)SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
    Vector<UteVO> vUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
    request.setAttribute("vUte", vUte);
  
    Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = gaaFacadeClient.getElencoTipoGenereMacchinaFromRuolo(
        new Long(1), ruoloUtenza.getCodiceRuolo());
    request.setAttribute("vGenereMacchina", vGenereMacchina);
    
    vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
    //request.setAttribute("vTipoMarca", vTipoMarca);    
    
    Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = gaaFacadeClient.getElencoTipoFormaPossesso();
    request.setAttribute("vTipoFormaPossesso", vTipoFormaPossesso);
  }
  catch(Exception ex)
  {    
    
    SolmrLogger.info(this, " - newInserimentoRichVarIrrCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("history","history");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  String[] idMacchina = request.getParameterValues("idMacchina");
  String[] idUte = request.getParameterValues("idUte");
  String[] idGenereMacchina = request.getParameterValues("idGenereMacchina");
  String[] idCategoria = request.getParameterValues("idCategoria");
  String[] annoCostruzione = request.getParameterValues("annoCostruzione");
  String[] idMarca = request.getParameterValues("idMarca");
  String[] modello = request.getParameterValues("modello");
  String[] matricolaTelaio = request.getParameterValues("matricolaTelaio");
  String[] idTipoFormaPossesso = request.getParameterValues("idTipoFormaPossesso");
  String[] percentualePossesso = request.getParameterValues("percentualePossesso");
  String[] dataCarico = request.getParameterValues("dataCarico");
  String[] dataScarico = request.getParameterValues("dataScarico");
  
  
  
  // Ricerco la categoria in relazione al genere
  HashMap<Long, Vector<TipoCategoriaGaaVO>> hCategorie = new HashMap<Long, Vector<TipoCategoriaGaaVO>>();
  //Devo aggiungere il default se idGenereMacchina non è valorizzato....
  HashMap<Long, Vector<CodeDescription>> hMarche = new HashMap<Long, Vector<CodeDescription>>();  
  hMarche.put(new Long(-1), vTipoMarca);
  try 
  {
    if(Validator.isNotEmpty(regimeInserimentoRichVarIrr))
    {
      if(idGenereMacchina != null) 
      {
        for(int i = 0; i < idGenereMacchina.length; i++) 
        {         
          if(Validator.isNotEmpty(idGenereMacchina[i])) 
          {
            Vector<TipoCategoriaGaaVO> vTipoCategoria = gaaFacadeClient.getElencoTipoCategoria(new Long(idGenereMacchina[i]));
            hCategorie.put(new Long(idGenereMacchina[i]), vTipoCategoria);   
          }
        }
      }
    }
    //prima volta che entro!!!
    else 
    {
      if(vMacchine != null) 
      {
        for(int i = 0; i < vMacchine.size(); i++) 
        {          
          if(vMacchine.get(i).getIdGenereMacchina() != null) 
          {
            Vector<TipoCategoriaGaaVO> vTipoCategoria = gaaFacadeClient.getElencoTipoCategoria(vMacchine.get(i).getIdGenereMacchina());
            hCategorie.put(vMacchine.get(i).getIdGenereMacchina(), vTipoCategoria);   
          }
        } 
      }
    }
    request.setAttribute("hCategorie", hCategorie);
    
    
    if(Validator.isNotEmpty(regimeInserimentoRichVarIrr))
    {
      if(idGenereMacchina != null) 
      {
        for(int i = 0; i < idGenereMacchina.length; i++) 
        {         
          if(Validator.isNotEmpty(idGenereMacchina[i])) 
          {
            Vector<CodeDescription> vTipoMarcaTmp = gaaFacadeClient.getElencoTipoMarcaByIdGenere(new Long(idGenereMacchina[i]));
            if(vTipoMarcaTmp != null)
            {              
              hMarche.put(new Long(idGenereMacchina[i]), vTipoMarcaTmp);
            }   
          }
        }
      }
    }
    //prima volta che entro!!!
    else 
    {
      if(vMacchine != null) 
      {
        for(int i = 0; i < vMacchine.size(); i++) 
        {          
          if(vMacchine.get(i).getIdGenereMacchina() != null) 
          {
            Vector<CodeDescription> vTipoMarcaTmp = gaaFacadeClient.getElencoTipoMarcaByIdGenere(vMacchine.get(i).getIdGenereMacchina());
            if(vTipoMarcaTmp != null)
            {
              hMarche.put(vMacchine.get(i).getIdGenereMacchina(), vTipoMarcaTmp);
            }   
          }
        } 
      }
    }
    request.setAttribute("hMarche", hMarche);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - newInserimentoRichVarIrrCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(se);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("history","history");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (regimeInserimentoRichVarIrr != null)) 
  {    
    //E' stata inserita almeno una persona
    if(Validator.isNotEmpty(idMacchina))
    {
      Vector<MacchinaAziendaNuovaVO> vMacchineMod = new Vector<MacchinaAziendaNuovaVO>();  
	    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	    int countErrori = 0;
	    
	    //Controlli da effettuare
	    for(int i=0;i<idMacchina.length;i++)
	    {
	      ValidationErrors errors = new ValidationErrors();
	      
	      MacchinaAziendaNuovaVO macchinaAziendaNuovaModVO = new MacchinaAziendaNuovaVO();
	        
        macchinaAziendaNuovaModVO.setIdRichiestaAzienda(aziendaNuovaVO.getIdRichiestaAzienda());
        if(Validator.isNotEmpty(idMacchina[i]))
          macchinaAziendaNuovaModVO.setIdMacchina(new Long(idMacchina[i]));
      
        //prese da db!!
        if(Validator.isNotEmpty(macchinaAziendaNuovaModVO.getIdMacchina()))
        {
          /*macchinaAziendaNuovaModVO.setIdUte(new Long(idUte[i]));
          macchinaAziendaNuovaModVO.setIdGenereMacchina(new Long(idGenereMacchina[i]));
          if(Validator.isNotEmpty(idCategoria[i]))
          {
            macchinaAziendaNuovaModVO.setIdCategoria(new Long(idCategoria[i]));
          }
          if(Validator.isNotEmpty(annoCostruzione[i]))
          {
            macchinaAziendaNuovaModVO.setAnnoCostruzione(new Integer(annoCostruzione[i]));
          }
          if(Validator.isNotEmpty(idMarca[i]))
          {
            macchinaAziendaNuovaModVO.setIdMarca(new Long(idMarca[i]));
          }
          macchinaAziendaNuovaModVO.setTipoMacchina(modello[i]);
          macchinaAziendaNuovaModVO.setMatricolaTelaio(matricolaTelaio[i]);
          macchinaAziendaNuovaModVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso[i]));
          macchinaAziendaNuovaModVO.setPercentualePossesso(new BigDecimal(percentualePossesso[i]));*/
          macchinaAziendaNuovaModVO.setDataCarico(DateUtils.parseDate(dataCarico[i]));
          
          
          if(Validator.isNotEmpty(dataScarico[i]))
          {
            if(Validator.isDate(dataScarico[i]))
            {
            
              Date dataScaricoDt = DateUtils.parseDate(dataScarico[i]);
              if(dataScaricoDt.before(macchinaAziendaNuovaModVO.getDataCarico()))
              {
                errors.add("dataScarico", new ValidationError(AnagErrors.ERRORE_KO_DATA_SCARICO));
                countErrori++;
              }
              else
              {
                macchinaAziendaNuovaModVO.setDataScarico(DateUtils.parseDate(dataScarico[i]));
              }             
            }
            else
            {
              errors.add("dataScarico", new ValidationError(AnagErrors.ERR_CAMPO_VALIDO));
              countErrori++;
            }
          }          
        }
        else
        {
        
		      if (Validator.isEmpty(idUte[i]))
			    {
			      errors.add("idUte", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			      countErrori++;
			    }
			    else
			    {
			      macchinaAziendaNuovaModVO.setIdUte(new Long(idUte[i]));
			    }
		    
			    if (Validator.isEmpty(idGenereMacchina[i]))
	        {
	          errors.add("idGenereMacchina", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	        }
	        else
	        {
	          macchinaAziendaNuovaModVO.setIdGenereMacchina(new Long(idGenereMacchina[i]));
	        }
        
	        if(Validator.isNotEmpty(idCategoria[i]))
		      {
		        macchinaAziendaNuovaModVO.setIdCategoria(new Long(idCategoria[i]));
		      }
		      else
		      {
		        Vector<TipoCategoriaGaaVO> vCategoria = null;
		        if(Validator.isNotEmpty(idGenereMacchina[i]))
		        {
		          vCategoria = hCategorie.get(new Long(idGenereMacchina[i]));
		        }
		      
		        if(vCategoria != null)
		        {
		          errors.add("idCategoria", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
		          countErrori++;
		        }
		      }
	      
	      
	      
			    String annoCostruzioneStr = annoCostruzione[i];
		      String idMarcaStr = idMarca[i];
		      String modelloStr = modello[i];
		      String matricolaTelaioStr = matricolaTelaio[i];
		      
		      Integer annoCostruzioneIt = null;
		      if(Validator.isEmpty(annoCostruzione) && Validator.isEmpty(idMarca)
		        && Validator.isEmpty(modello))
		      {
		        errors.add("annoCostruzione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA));
		        errors.add("idMarca", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA));
		        errors.add("modello", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA));
		        countErrori++;      
		      }
		      else
		      {
		        Long idMarcaLg = null;
		        if(Validator.isNotEmpty(idMarcaStr))
		        {
		          idMarcaLg = new Long(idMarcaStr);
		          macchinaAziendaNuovaModVO.setIdMarca(idMarcaLg);
		        }
		       
		        if(Validator.isNotEmpty(annoCostruzioneStr))
		        {
		          if(Validator.isNumericInteger(annoCostruzioneStr))
		          {
		            annoCostruzioneIt = new Integer(annoCostruzioneStr);
		            macchinaAziendaNuovaModVO.setAnnoCostruzione(annoCostruzioneIt);
		          }
		          else
		          {
		            errors.add("annoCostruzione", new ValidationError(AnagErrors.ERR_CAMPO_VALIDO));
		            countErrori++;
		          }
		        }
		        macchinaAziendaNuovaModVO.setTipoMacchina(modelloStr);
		        macchinaAziendaNuovaModVO.setMatricolaTelaio(matricolaTelaioStr);
		        
		        
		        boolean flagControlliUnivocita = false;        
		        if(Validator.isEmpty(macchinaAziendaNuovaModVO.getIdCategoria()))
		        {
		          flagControlliUnivocita = true;
		        }
		        else
		        {
		          TipoCategoriaGaaVO tipoCategoriaGaaVO = gaaFacadeClient.getTipoCategoriaFromPK(macchinaAziendaNuovaModVO.getIdCategoria().longValue());
		          if("S".equalsIgnoreCase(tipoCategoriaGaaVO.getFlagControlliUnivocita()))
		          {
		            flagControlliUnivocita = true;
		          } 
		          
		          //eliminato controllo 11/05/2016 richiesta Elisa R 
		          /*if("N".equalsIgnoreCase(tipoCategoriaGaaVO.getFlagControlliUnivocita()))
		          {
		            if(Validator.isEmpty(idMarca))
		            {
		              errors = ErrorUtils.setValidErrNoNull(errors,"idMarca", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
		            }
		          }*/               
		        }
		        
		        
		        //Controllo fatto se e solo se almeno uno è diverso da null
		        if(flagControlliUnivocita)
		        {
			        if(Validator.isEmpty(idMarcaLg) || Validator.isEmpty(modelloStr)
			          || Validator.isEmpty(annoCostruzioneIt) || Validator.isEmpty(matricolaTelaioStr))
			        {		        
				        if(gaaFacadeClient.existsMotoreAgricolo(idMarcaLg, modelloStr, annoCostruzioneIt, matricolaTelaioStr))
				        {
				          errors.add("annoCostruzione", new ValidationError(AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE));
				          errors.add("idMarca", new ValidationError(AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE));
				          errors.add("modello", new ValidationError(AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE));
				          countErrori++;
				        }
				      }
				    }
		      
		      }
		      
		      if(Validator.isNotEmpty(idTipoFormaPossesso[i]))
		      {
		        macchinaAziendaNuovaModVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso[i]));
		      }
		      else
		      {
		        errors.add("idTipoFormaPossesso", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
		        countErrori++;
		      }
		      
		      if(Validator.isNotEmpty(percentualePossesso[i]))
		      {
		        if(Validator.isNumberPercentualeMaggioreZero(percentualePossesso[i]))
		        {
		          BigDecimal percentualePossessoBg = new BigDecimal(percentualePossesso[i]);
		          macchinaAziendaNuovaModVO.setPercentualePossesso(percentualePossessoBg);
		        }
		        else
		        {
		          errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_POSSESSO));
		          countErrori++;
		        }
		      }
		      else
		      {
		        errors.add("percentualePossesso", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
		        countErrori++;
		      }
		      
		      if(Validator.isNotEmpty(dataCarico[i]))
		      {
		        if(Validator.isDate(dataCarico[i]))
		        {
		        
		          if(Validator.isNotEmpty(annoCostruzioneIt))
		          {
		            Date dataCaricoDt = DateUtils.parseDate(dataCarico[i]);
		            if(DateUtils.extractYearFromDate(dataCaricoDt) < annoCostruzioneIt.intValue())
		            {
		              errors.add("dataCarico", new ValidationError(AnagErrors.ERRORE_KO_DATA_CARICO_ANNO));
		              countErrori++;
		            }
		            else
		            {
		              macchinaAziendaNuovaModVO.setDataCarico(DateUtils.parseDate(dataCarico[i]));
		            }
		          }
		          else
              {
                macchinaAziendaNuovaModVO.setDataCarico(DateUtils.parseDate(dataCarico[i]));
              }
		          
		        }
		        else
		        {
		          errors.add("dataCarico", new ValidationError(AnagErrors.ERR_CAMPO_VALIDO));
		          countErrori++;
		        }
		      }
		      else
		      {
		        errors.add("dataCarico", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
		        countErrori++;
		      }        
        } 
         
        vMacchineMod.add(macchinaAziendaNuovaModVO);
        
        elencoErrori.add(errors);	    
		    
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {	          
		    try 
		    {
		      gaaFacadeClient.aggiornaMacchineIrrAziendaNuova(
		        aziendaNuovaVO, ruoloUtenza.getIdUtente(), vMacchineMod);		      
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoRichVarIrrCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		      request.setAttribute("messaggioErrore",messaggio);
			    request.setAttribute("history","history");
			    %>
			      <jsp:forward page="<%= erroreViewUrl %>" />
			    <%
			    return;
		    }
		    
		    
		    %>
	        <jsp:forward page="<%= newInserimentoConfermaVariazioneUrl %>" >
	          <jsp:param name="idTipoRichiesta" value="<%=new Integer(SolmrConstants.RICHIESTA_VAR_IRRORATRICI).toString() %>" />
          </jsp:forward>
	      <%
	      return;		    
		  }
		}
		else
		{
		  msgErrore = "Deve essere inserita almeno una macchina."; 
		  request.setAttribute("msgErrore", msgErrore);    
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoRichVarIrrUrl %>"/>
  
