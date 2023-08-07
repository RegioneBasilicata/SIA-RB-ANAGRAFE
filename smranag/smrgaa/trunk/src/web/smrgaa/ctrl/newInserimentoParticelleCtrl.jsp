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
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "newInserimentoParticelleCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  WebUtils.removeUselessFilter(session, "idAziendaNuova,vParticelleAziendaNuova,elencoAllevamentiBdn");

  String newInserimentoParticelleUrl = "/view/newInserimentoParticelleView.jsp";  
  String indietroUrl = "/ctrl/newInserimentoUteCtrl.jsp";
  String pageNext = "/ctrl/newInserimentoFabbricatiCtrl.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione nuova iscrizione. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/newInserimentoParticelle.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  String testoHelp = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ET_UT_NAP_TER);
  request.setAttribute("testoHelp", testoHelp);
  
  Long idAziendaNuova = (Long)session.getAttribute("idAziendaNuova");
  AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  request.setAttribute("aziendaNuovaVO", aziendaNuovaVO);
  
  HashMap<String, Vector<ComuneVO>> hProvCom = new HashMap<String, Vector<ComuneVO>>();
  HashMap<Long, TipoVarietaVO[]> hUtilVar = new HashMap<Long, TipoVarietaVO[]>();
  HashMap<Long, Vector<CodeDescription>> hIndUtil = new HashMap<Long, Vector<CodeDescription>>();
  
  String operazione = request.getParameter("operazione");
  
  
  
  // L'utente ha premuto il tasto annulla
  if((request.getParameter("indietro") != null)
    && (request.getParameter("regimeInserimentoParticelle") != null)) 
  { 
    request.getRequestDispatcher(indietroUrl).forward(request, response);
    return;
  }
  
  
  
  Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova = null;
  if(request.getParameter("regimeInserimentoParticelle") == null)
  {
    //Prima volta che entro carico le ute da db
    vParticelleAziendaNuova = gaaFacadeClient.getParticelleAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  else
  {
    vParticelleAziendaNuova = (Vector<ParticellaAziendaNuovaVO>)session.getAttribute("vParticelleAziendaNuova");
  }
  
  Vector<ProvinciaVO> vProvince =  anagFacadeClient.getProvince();
     String[] orderBy = new String[] {SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC};
  request.setAttribute("vProvince", vProvince);
  
  Vector<UteAziendaNuovaVO> vUteAziendaNuova = null;
  try
  {
    vUteAziendaNuova = gaaFacadeClient.getUteAziendaNuovaIscrizione(idAziendaNuova.longValue());
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - newInserimentoParticelleCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("vUteAziendaNuova", vUteAziendaNuova);
  
  CodeDescription[] elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
  request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  
  Vector<CodeDescription> vIndirizzoUtilizzo = anagFacadeClient.getIndirizziTipiUtilizzoAttivi();
  request.setAttribute("vIndirizzoUtilizzo", vIndirizzoUtilizzo);
  
  Vector<UnitaMisuraVO> vUnitaMisura = gaaFacadeClient.getListUnitaMisura();
  request.setAttribute("vUnitaMisura", vUnitaMisura);   
  
  String[] idUteAziendaNuova = request.getParameterValues("idUteAziendaNuova");
  String[] istatProvincia = request.getParameterValues("istatProvincia");
  String[] istatComune = request.getParameterValues("istatComune");
  String[] sezione = request.getParameterValues("sezione");
  String[] foglio = request.getParameterValues("foglio");
  String[] particella = request.getParameterValues("particella");
  String[] idTitoloPossesso = request.getParameterValues("idTitoloPossesso");
  String[] percentualePossesso = request.getParameterValues("percentualePossesso");
  String[] idIndirizzoUtilizzo = request.getParameterValues("idIndirizzoUtilizzo");
  String[] idUtilizzo = request.getParameterValues("idUtilizzo");
  String[] idVarieta = request.getParameterValues("idVarieta");
  String[] idUnitaMisura = request.getParameterValues("idUnitaMisura");
  String[] superficie = request.getParameterValues("superficie");
  
  if((vParticelleAziendaNuova != null) && Validator.isNotEmpty(idUnitaMisura))
  {
	  for(int i=0;i<vParticelleAziendaNuova.size();i++)
	  {
	    ParticellaAziendaNuovaVO particellaAziendaNuovaVO = vParticelleAziendaNuova.get(i);	    
	    
	    Long idUteAziendaNuovaLg = null;
		  if(Validator.isNotEmpty(idUteAziendaNuova[i])) 
		  {
		    idUteAziendaNuovaLg = Long.decode(idUteAziendaNuova[i]);
		  }
		  
		  particellaAziendaNuovaVO.setIdUteAziendaNuova(idUteAziendaNuovaLg);
	    
	    particellaAziendaNuovaVO.setIstatProvincia(istatProvincia[i]);
	    if(Validator.isNotEmpty(vParticelleAziendaNuova.get(i).getIstatProvincia()) 
	      && (hProvCom.get(vParticelleAziendaNuova.get(i).getIstatProvincia()) == null))
	    {
	      String istatProvinciaTmp = vParticelleAziendaNuova.get(i).getIstatProvincia();
	      hProvCom.put(istatProvinciaTmp, anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvinciaTmp));        
	    }
	    particellaAziendaNuovaVO.setIstatComune(istatComune[i]);
	    if(Validator.isNotEmpty(sezione[i]))
	    {
	      particellaAziendaNuovaVO.setSezione(sezione[i].toUpperCase());
	    }
	    particellaAziendaNuovaVO.setStrFoglio(foglio[i]);
	    particellaAziendaNuovaVO.setStrParticella(particella[i]);
	    
	    Integer idTitoloPossessoIt = null;
      if(Validator.isNotEmpty(idTitoloPossesso[i])) 
      {
        idTitoloPossessoIt = Integer.decode(idTitoloPossesso[i]);
      }
      particellaAziendaNuovaVO.setIdTitoloPossesso(idTitoloPossessoIt);
      
      particellaAziendaNuovaVO.setStrPercentualePossesso(percentualePossesso[i]);
      
      particellaAziendaNuovaVO.setStrSuperficie(superficie[i]);
      
      Long idIndirizzoUtilizzoLg = null;
      if(Validator.isNotEmpty(idIndirizzoUtilizzo[i]))
      {
        idIndirizzoUtilizzoLg = new Long(idIndirizzoUtilizzo[i]);
      }
      particellaAziendaNuovaVO.setIdIndirizzoUtilizzo(idIndirizzoUtilizzoLg);
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo()) 
        && (hIndUtil.get(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo()) == null))
      {
        hIndUtil.put(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo(), 
          anagFacadeClient.getTipiUtilizzoAttivi(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo().intValue()));        
      }
      Long idUtilizzoLg = null;
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdIndirizzoUtilizzo()) 
        && Validator.isNotEmpty(idUtilizzo[i]))
      {
        idUtilizzoLg = new Long(idUtilizzo[i]);
      }
      particellaAziendaNuovaVO.setIdUtilizzo(idUtilizzoLg);
      
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUtilizzo()) 
        && (hUtilVar.get(particellaAziendaNuovaVO.getIdUtilizzo()) == null))
      {
        Long idUtilizzoTmp = particellaAziendaNuovaVO.getIdUtilizzo();
        hUtilVar.put(idUtilizzoTmp,  anagFacadeClient.getListTipoVarietaByIdUtilizzo(idUtilizzoTmp, true));        
      }
      Long idVarietaLg = null;
      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUtilizzo())
        && Validator.isNotEmpty(idVarieta[i]))
      {
        idVarietaLg = new Long(idVarieta[i]);
      }
      particellaAziendaNuovaVO.setIdVarieta(idVarietaLg);
      
      Integer idUnitaMisuraIt = null;
      if(Validator.isNotEmpty(idUnitaMisura[i]))
      {
        idUnitaMisuraIt = new Integer(idUnitaMisura[i]);
      }
      particellaAziendaNuovaVO.setIdUnitaMisura(idUnitaMisuraIt);	        
	    
	  }
	  
	   
	}
	//prima volta che entro con particelle già inserita
	else if(vParticelleAziendaNuova != null)
	{
	  for(int i=0;i<vParticelleAziendaNuova.size();i++)
    {
		  if(Validator.isNotEmpty(vParticelleAziendaNuova.get(i).getIstatProvincia()) 
	      && (hProvCom.get(vParticelleAziendaNuova.get(i).getIstatProvincia()) == null))
	    {
	      String istatProvinciaTmp = vParticelleAziendaNuova.get(i).getIstatProvincia();
	      hProvCom.put(istatProvinciaTmp, anagFacadeClient.getComuniAttiviByIstatProvincia(istatProvinciaTmp));        
	    }
	    
	    if(Validator.isNotEmpty(vParticelleAziendaNuova.get(i).getIdIndirizzoUtilizzo()) 
        && (hIndUtil.get(vParticelleAziendaNuova.get(i).getIdIndirizzoUtilizzo()) == null))
      {
        hIndUtil.put(vParticelleAziendaNuova.get(i).getIdIndirizzoUtilizzo(), 
          anagFacadeClient.getTipiUtilizzoAttivi(vParticelleAziendaNuova.get(i).getIdIndirizzoUtilizzo().intValue()));        
      }
	    
	    if(Validator.isNotEmpty(vParticelleAziendaNuova.get(i).getIdUtilizzo()) 
        && (hUtilVar.get(vParticelleAziendaNuova.get(i).getIdUtilizzo()) == null))
      {
        Long idUtilizzoTmp = vParticelleAziendaNuova.get(i).getIdUtilizzo();
        hUtilVar.put(idUtilizzoTmp,  anagFacadeClient.getListTipoVarietaByIdUtilizzo(idUtilizzoTmp, true));        
      }
	  }
	
	}
	
	request.setAttribute("hProvCom", hProvCom);
	request.setAttribute("hIndUtil", hIndUtil);  
  request.setAttribute("hUtilVar", hUtilVar);  
  
  if(Validator.isNotEmpty(operazione) && "cambio".equalsIgnoreCase(operazione))
  {
    //nn fa nulla....
  }
  else if(Validator.isNotEmpty(operazione) && "elimina".equalsIgnoreCase(operazione))      
  {
    String idRigaElim = request.getParameter("idRigaElim");
    vParticelleAziendaNuova.remove(new Integer(idRigaElim).intValue());
  }
  else if(Validator.isNotEmpty(operazione) && "inserisci".equalsIgnoreCase(operazione))
  {
    if(vParticelleAziendaNuova == null)
    {
      vParticelleAziendaNuova = new Vector<ParticellaAziendaNuovaVO>();
    }
    vParticelleAziendaNuova.add(new ParticellaAziendaNuovaVO());
  }
  
  
  session.setAttribute("vParticelleAziendaNuova", vParticelleAziendaNuova);
  
  
  
  
  
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if ((request.getParameter("avanti")!=null)
    && (request.getParameter("regimeInserimentoParticelle") != null)) 
  {
    HashMap<String,BigDecimal> hKeyPerc = new HashMap<String,BigDecimal>();     
    
    if(Validator.isNotEmpty(vParticelleAziendaNuova)
      && (vParticelleAziendaNuova.size() > 0))
    {
    
	    Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	    int countErrori = 0;
	    
	    //Controlli da effettuare
	    Vector<Integer> vParticelleVuote = new Vector<Integer>();
	    for(int i=0;i<vParticelleAziendaNuova.size();i++)
	    {
	      ValidationErrors errors = new ValidationErrors();
	      ParticellaAziendaNuovaVO particellaAziendaNuovaVO = vParticelleAziendaNuova.get(i);
	      int countErroriSingolo = 0;
	      
	      if(Validator.isNotEmpty(particellaAziendaNuovaVO.getIstatProvincia())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getIstatComune())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getStrFoglio())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getStrParticella())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getIdTitoloPossesso())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getStrPercentualePossesso())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUtilizzo())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getIdUnitaMisura())
	        || Validator.isNotEmpty(particellaAziendaNuovaVO.getStrSuperficie()))
	      {
	      
		      if (Validator.isEmpty(particellaAziendaNuovaVO.getIdUteAziendaNuova()))
			    {
			      errors.add("idUteAziendaNuova", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
			      countErrori++;
			      countErroriSingolo++;
			    }
			    
			    if (Validator.isEmpty(particellaAziendaNuovaVO.getIstatProvincia()))
	        {
	          errors.add("istatProvincia", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getIstatComune()))
	        {
	          errors.add("istatComune", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getStrFoglio()))
	        {
	          errors.add("foglio", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        else
	        {
	          if(!Validator.isNumericInteger(particellaAziendaNuovaVO.getStrFoglio())) 
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "foglio", AnagErrors.ERR_CAMPO_NON_CORRETTO);
	            countErrori++;
	            countErroriSingolo++;
	          }
	          else 
	          {
	            particellaAziendaNuovaVO.setFoglio(new Long(particellaAziendaNuovaVO.getStrFoglio()));
	          }
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getStrParticella()))
	        {
	          errors.add("particella", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        else
	        {
	          if(!Validator.isNumericInteger(particellaAziendaNuovaVO.getStrParticella())) 
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "particella", AnagErrors.ERR_CAMPO_NON_CORRETTO);
	            countErrori++;
	            countErroriSingolo++;
	          }
	          else 
	          {
	            particellaAziendaNuovaVO.setParticella(new Long(particellaAziendaNuovaVO.getStrParticella()));
	          }
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getIdTitoloPossesso()))
	        {
	          errors.add("idTitoloPossesso", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
			     
			       
			    if(Validator.isEmpty(particellaAziendaNuovaVO.getStrPercentualePossesso())) 
	        {
	          errors = ErrorUtils.setValidErrNoNull(errors, "percentualePossesso", AnagErrors.ERR_CAMPO_OBBLIGATORIO );
	          countErrori++;
	          countErroriSingolo++;
	        }
	        // Se è stata valorizzata controllo che sia un numero valido
	        else 
	        {
	          if(Validator.validateDouble(particellaAziendaNuovaVO.getStrPercentualePossesso(), 999.99) == null) 
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "percentualePossesso", AnagErrors.ERR_CAMPO_NON_CORRETTO);
	            countErrori++;
	            countErroriSingolo++;
	          }
	          else if(Double.parseDouble(Validator.validateDouble(particellaAziendaNuovaVO.getStrPercentualePossesso(), 999.99)) <= 0) 
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors, "percentualePossesso", AnagErrors.ERR_CAMPO_NON_CORRETTO);
	            countErrori++;
	            countErroriSingolo++;
	          }
	          else 
	          {
	            particellaAziendaNuovaVO.setPercentualePossesso(new BigDecimal(particellaAziendaNuovaVO.getStrPercentualePossesso().replace(',','.')));
	          }
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getIdUtilizzo()))
	        {
	          errors.add("idUtilizzo", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getIdVarieta()))
	        {
	          errors.add("idVarieta", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        
	        
	        boolean validUnitaMisura = false;
	        if (Validator.isEmpty(particellaAziendaNuovaVO.getIdUnitaMisura()))
	        {
	          errors.add("idUnitaMisura", new ValidationError(
	              AnagErrors.ERR_CAMPO_OBBLIGATORIO));
	          countErrori++;
	          countErroriSingolo++;
	        }
	        else
	        {
	          validUnitaMisura = true;
	        }
	        
	        
	        
	        if(Validator.isEmpty(particellaAziendaNuovaVO.getStrSuperficie())) 
	        {
	          errors = ErrorUtils.setValidErrNoNull(errors, "superficie", AnagErrors.ERR_CAMPO_OBBLIGATORIO );
	          countErrori++;
	          countErroriSingolo++;
	        }
	        // Se è stata valorizzata controllo che sia un numero valido
	        else if(validUnitaMisura) 
	        { 
	        
	          if(SolmrConstants.UM_ETTARI.compareTo(particellaAziendaNuovaVO.getIdUnitaMisura()) == 0)
	          {         
		          if(Validator.validateDouble(particellaAziendaNuovaVO.getStrSuperficie(), 999999.9999) == null) 
		          {
		            errors = ErrorUtils.setValidErrNoNull(errors, "superficie", AnagErrors.ERR_CAMPO_NON_CORRETTO);
		            countErrori++;
		            countErroriSingolo++;
		          }
		          else if(Double.parseDouble(Validator.validateDouble(particellaAziendaNuovaVO.getStrSuperficie(), 999999.9999)) <= 0) 
		          {
		            errors = ErrorUtils.setValidErrNoNull(errors, "superficie", AnagErrors.ERR_CAMPO_NON_CORRETTO);
		            countErrori++;
		            countErroriSingolo++;
		          }
		          else 
		          {
		            particellaAziendaNuovaVO.setSuperficie(new BigDecimal(particellaAziendaNuovaVO.getStrSuperficie().replace(',','.')));
		          }
		        }
		        else if(SolmrConstants.UM_METRIQUADRI.compareTo(particellaAziendaNuovaVO.getIdUnitaMisura()) == 0)
		        {
		          if(!Validator.isPositiveInteger(particellaAziendaNuovaVO.getStrSuperficie())) 
	            {
	              errors = ErrorUtils.setValidErrNoNull(errors, "superficie", AnagErrors.ERR_CAMPO_NON_CORRETTO_INTERO);
	              countErrori++;
	              countErroriSingolo++;
	            }
	            else 
	            {
	              particellaAziendaNuovaVO.setSuperficie(new BigDecimal(particellaAziendaNuovaVO.getStrSuperficie().replace(',','.')));
	            }	        
		        }
	        }
	        
	        
	        if(countErroriSingolo == 0)
	        {
	          //se nn ci sono stati errori verifico che si apresente su db_particella_certificata
	          try
	          {
	            if(!gaaFacadeClient.isParticellAttivaStoricoParticella(particellaAziendaNuovaVO.getIstatComune(), 
	               particellaAziendaNuovaVO.getSezione(), particellaAziendaNuovaVO.getStrFoglio(), 
	               particellaAziendaNuovaVO.getStrParticella(), null))
	            { 
		            ParticellaCertificataVO particellaCertificataVO = anagFacadeClient
		              .findParticellaCertificataByParameters(particellaAziendaNuovaVO.getIstatComune(), 
		               particellaAziendaNuovaVO.getSezione(), particellaAziendaNuovaVO.getStrFoglio(), 
		               particellaAziendaNuovaVO.getStrParticella(), null, true, null);
		            if(Validator.isEmpty(particellaCertificataVO)
		              || (Validator.isNotEmpty(particellaCertificataVO) && !particellaCertificataVO.isCertificata()))
		            {
		              errors.add("particellaCertificata", new ValidationError(AnagErrors.ERR_PART_NO_CERT));
		              countErrori++;
		            }
		          }
	          }
	          catch(Exception ex)
	          {
	            SolmrLogger.info(this, " - newInserimentoParticelleCtrl.jsp - FINE PAGINA");
	            String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
	            request.setAttribute("messaggioErrore",messaggio);
	            request.setAttribute("pageBack", actionUrl);
	            %>
	              <jsp:forward page="<%= erroreViewUrl %>" />
	            <%
	            return;
	          }
	          
	          String sez = "-";
		        if(Validator.isNotEmpty(particellaAziendaNuovaVO.getSezione()))
		        {
		          sez = particellaAziendaNuovaVO.getSezione();
		        }
		        String keyCat = sez+"_"+particellaAziendaNuovaVO.getFoglio()+"_"
		          +particellaAziendaNuovaVO.getParticella()+"_"+particellaAziendaNuovaVO.getIstatComune()
		          +"_"+particellaAziendaNuovaVO.getIdTitoloPossesso();
		          
		        if(hKeyPerc.get(keyCat) != null)
		        {
		          BigDecimal percTemp = hKeyPerc.get(keyCat);
		          if(percTemp.compareTo(particellaAziendaNuovaVO.getPercentualePossesso()) !=0)
		          {
		            errors.add("percPossesso", new ValidationError(AnagErrors.ERR_PART_MULT_PERC_POSS));
	              countErrori++;
		          }	        
		        }
		        else
		        {
		          hKeyPerc.put(keyCat, particellaAziendaNuovaVO.getPercentualePossesso());
		        }
		          
		      }
		      else
		      {
		        errors.add("particellaCertificata", new ValidationError(AnagErrors.ERR_GENERICO_PART));
            countErrori++;
		      }
		    } //if se nn selezionato nulla
	      else
	      {
	        if(vParticelleVuote == null)
	        {
	          vParticelleVuote = new Vector<Integer>();
	        }       
	        vParticelleVuote.add(new Integer(i));
	      
	      }    
        
        
		    
		    
		    
		    
	      
		    
		    
		    elencoErrori.add(errors);
		  }
		  
		  if(countErrori > 0) 
		  {
	      request.setAttribute("elencoErrori", elencoErrori);
	    }
	    else
	    {
	      
	      if(Validator.isNotEmpty(vParticelleVuote))
	      {
	        Collections.reverse(vParticelleVuote);
	        for(int j=0;j<vParticelleVuote.size();j++)
	        {
	          vParticelleAziendaNuova.remove(vParticelleVuote.get(j).intValue());       
	        }
	      }
	   
	          
		    try 
		    {
		      if(vParticelleAziendaNuova.size() > 0)
          {
		        gaaFacadeClient.aggiornaParticelleAziendaNuovaIscrizione(
		          aziendaNuovaVO, ruoloUtenza.getIdUtente(), vParticelleAziendaNuova);
		      }
		      else
		      {
		        //Cancello tutto...rimaste vuoto tutti itasti...
            gaaFacadeClient.aggiornaParticelleAziendaNuovaIscrizione(
               aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
		      }
		    }
		    catch(Exception ex)
		    {
		      SolmrLogger.info(this, " - newInserimentoParticelletrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
		    
		    %>
		      <jsp:forward page="<%= pageNext %>"/>
		    <%
		    return;		    
		    
		  }
		}
		else
		{
		  
		  //Cancello tutto...
		  try 
      {
        gaaFacadeClient.aggiornaParticelleAziendaNuovaIscrizione(
          aziendaNuovaVO, ruoloUtenza.getIdUtente(), null);
      }
      catch(Exception ex)
      {
        SolmrLogger.info(this, " - newInserimentoParticelletrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
		
		
		  %>
	      <jsp:forward page="<%= pageNext %>"/>
	    <%
	    return;
		}
		
		
  }
  
  
  
  
  
  
  
  

%>
  <jsp:forward page="<%= newInserimentoParticelleUrl %>"/>
  
