<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="javax.servlet.http.HttpSession.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String importaMacchinaAgricolaModUrl = "/view/importaMacchinaAgricolaModView.jsp";
  String actionUrl = "../layout/motori_agricoli_incarico.htm";
  String motoriAgricoliIncaricoCtrlUrl = "/ctrl/motori_agricoli_incaricoCtrl.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella sezione carica macchina agricola. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  String iridePageName = "macchinaAgricolaModificaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  ValidationErrors errors = null;
  String messaggioErrore = "";
  
  
  
  String parametroModMaccLibera = null;
  try 
  {
    parametroModMaccLibera = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MOD_MACCH_LIBERA);
    request.setAttribute("parametroModMaccLibera", parametroModMaccLibera);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - macchinaAgricolaModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MOD_MACC_LIBERA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  

  try 
  {
    
    String idPossessoMacchinaStr = request.getParameter("idPossessoMacchina");
    Long idPossessoMacchina = null;
    if(Validator.isNotEmpty(idPossessoMacchinaStr))
      idPossessoMacchina = new Long(idPossessoMacchinaStr);
    else
      throw new SolmrException("idPossessoMacchina a null");
      
    
    PossessoMacchinaVO possessoMacchinaVO = gaaFacadeClient.getPosessoMacchinaFromId(idPossessoMacchina);
    
    if(gaaFacadeClient.isMacchinaGiaPossesso(possessoMacchinaVO.getMacchinaVO().getIdMacchina(), 
      anagAziendaVO.getIdAzienda()))
    {
      SolmrLogger.info(this, " - importaMacchinaAgricolaModCtrl.jsp - FINE PAGINA");
	    String messaggio = AnagErrors.ERRORE_KO_MACCH_IMPORTAZIONE;
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;    
    }
    
    
    request.setAttribute("possessoMacchinaVO", possessoMacchinaVO);
    
    
    boolean isPossesoMultiplo = gaaFacadeClient.isMacchinaPossMultiplo(possessoMacchinaVO.getMacchinaVO().getIdMacchina());
    if(isPossesoMultiplo)
    request.setAttribute("isPossesoMultiplo", "true");
    
    
    Vector<UteVO> vUte = anagFacadeClient.getElencoIdUTEByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("vUte", vUte);
    
    
    
    Vector<CodeDescription> vTipoMarca = null;
    if(Validator.isNotEmpty(possessoMacchinaVO.getMacchinaVO().getIdGenereMacchina()))
    {
      vTipoMarca = gaaFacadeClient.getElencoTipoMarcaByIdGenere(possessoMacchinaVO.getMacchinaVO().getIdGenereMacchina());
      if(vTipoMarca == null)
        vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
    }
    else
    {    
      vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
    }
    request.setAttribute("vTipoMarca", vTipoMarca);
    
    Vector<TipoFormaPossessoGaaVO> vTipoFormaPossesso = gaaFacadeClient.getElencoTipoFormaPossesso();
    request.setAttribute("vTipoFormaPossesso", vTipoFormaPossesso);
    
    Vector<CodeDescription> vTipoScarico = gaaFacadeClient.getElencoTipoScarico();
    request.setAttribute("vTipoScarico", vTipoScarico);
    
    
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
    {
      //Validazione e inserimento
      
      if(Validator.isEmpty(request.getParameter("idUte")))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idUte", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
          
  
      String annoCostruzione = request.getParameter("annoCostruzione");
      String idMarca = request.getParameter("idMarca");
      String modello = request.getParameter("modello");
      String matricolaTelaio = request.getParameter("matricolaTelaio");
      MacchinaGaaVO macchinaEqVO = new MacchinaGaaVO();
      Integer annoCostruzioneIt = null;
      
      if(Validator.isEmpty(annoCostruzione) && Validator.isEmpty(idMarca)
        && Validator.isEmpty(modello))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"annoCostruzione", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA);
        errors = ErrorUtils.setValidErrNoNull(errors,"idMarca", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA);
        errors = ErrorUtils.setValidErrNoNull(errors,"modello", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA);      
      }
      else
      {
        Long idMarcaLg = null;
        if(Validator.isNotEmpty(idMarca))
        {
          idMarcaLg = new Long(idMarca);
          macchinaEqVO.setIdMarca(idMarcaLg);
        }
        
        if(Validator.isNotEmpty(annoCostruzione))
        {
          if(Validator.isNumericInteger(annoCostruzione))
          {
	          annoCostruzioneIt = new Integer(annoCostruzione);
	          macchinaEqVO.setAnnoCostruzione(annoCostruzioneIt);
	        }
	        else
	        {
	          errors = ErrorUtils.setValidErrNoNull(errors,"annoCostruzione", AnagErrors.ERR_CAMPO_VALIDO);
	        }
        }
        macchinaEqVO.setMatricolaTelaio(matricolaTelaio);
        macchinaEqVO.setModello(modello);
        
        
        
        //se sono mdoficati i dati macchina rispetto a aquelli su db devo fare questo controllo!!
        if(!possessoMacchinaVO.getMacchinaVO().equalsDatiMacchina(macchinaEqVO))
        {
	        if(gaaFacadeClient.existsMotoreAgricolo(idMarcaLg, modello, annoCostruzioneIt, matricolaTelaio))
	        {
	          if(Validator.isNotEmpty(matricolaTelaio))
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors,"annoCostruzione", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE_TELAIO);
	            errors = ErrorUtils.setValidErrNoNull(errors,"idMarca", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE_TELAIO);
	            errors = ErrorUtils.setValidErrNoNull(errors,"modello", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE_TELAIO);
	            errors = ErrorUtils.setValidErrNoNull(errors,"matricolaTelaio", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE_TELAIO);    
	          }
	          else
	          {
	            errors = ErrorUtils.setValidErrNoNull(errors,"annoCostruzione", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE);
	            errors = ErrorUtils.setValidErrNoNull(errors,"idMarca", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE);
	            errors = ErrorUtils.setValidErrNoNull(errors,"modello", AnagErrors.ERR_CAMPO_MACCHINA_PRESENTE);
	          }
	        }
	      }
      
      }
      
      
      //eliminato controllo 11/05/2016 richiesta Elisa R 
      //Da inserire controllo su idMarca e categoria..
      /*if("N".equalsIgnoreCase(possessoMacchinaVO.getMacchinaVO()
          .getTipoCategoriaVO().getFlagControlliUnivocita()))
      {
        if(Validator.isEmpty(idMarca))
          errors = ErrorUtils.setValidErrNoNull(errors,"idMarca", AnagErrors.ERR_CAMPO_OBBLIGATORIO);      
      }*/
      
      String idTipoFormaPossesso = request.getParameter("idTipoFormaPossesso");
      if(Validator.isEmpty(idTipoFormaPossesso))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idTipoFormaPossesso", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      String percentualePossesso = request.getParameter("percentualePossesso");
      if(Validator.isNotEmpty(percentualePossesso))
      {
        if(!Validator.isNumberPercentualeMaggioreZero(percentualePossesso))
        {
          errors = ErrorUtils.setValidErrNoNull(errors,"percentualePossesso", AnagErrors.ERRORE_KO_PERCENTUALE_POSSESSO);
        }
        else
        {
          BigDecimal percInserita = new BigDecimal(percentualePossesso);  
          BigDecimal percGiaPossesso = gaaFacadeClient.percMacchinaGiaInPossesso(possessoMacchinaVO.getMacchinaVO().getIdMacchina());       
          percInserita = percInserita.add(percGiaPossesso);
          if(percInserita.compareTo(new BigDecimal(100)) > 0)
          {
            errors = ErrorUtils.setValidErrNoNull(errors,"percentualePossesso", "la macchina e'' gia'' in carico ad altre aziende per una percentuale di "+
              Formatter.formatDouble(percGiaPossesso));
          }
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"percentualePossesso", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      
      String dataCarico = request.getParameter("dataCarico");
      if(Validator.isNotEmpty(dataCarico))
	    {
	      if(!Validator.isDate(dataCarico))
	      {
	        errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERR_CAMPO_VALIDO);
	      }
	      else
	      {
	        Date dataCaricoDt = DateUtils.parseDate(dataCarico);
          if(DateUtils.extractYearFromDate(dataCaricoDt) < annoCostruzioneIt.intValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERRORE_KO_DATA_CARICO_ANNO);
          }
          else if(Validator.isNotEmpty(possessoMacchinaVO.getDataScarico()) 
            && dataCaricoDt.before(possessoMacchinaVO.getDataScarico()))
          {
            errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERRORE_KO_DATA_CARICO);
         }
        }
	    }
	    else
	    {
	      errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
	    }
	    
      
      
      
      if(errors !=null) 
      {
        request.setAttribute("errors", errors);
      }
      //salva dati
      else
      {
        PossessoMacchinaVO possessoMacchinaConfVO = new PossessoMacchinaVO();
        MacchinaGaaVO macchinaConfVO = new MacchinaGaaVO();
        
        possessoMacchinaConfVO.setIdUte(new Long(request.getParameter("idUte")));
        
        Long idMarcaLg = null;
        if(Validator.isNotEmpty(idMarca))
        {
          idMarcaLg = new Long(idMarca);
          macchinaConfVO.setIdMarca(idMarcaLg);
        }
        annoCostruzioneIt = null;
        if(Validator.isNotEmpty(annoCostruzione))
        {
          annoCostruzioneIt = new Integer(annoCostruzione);
          macchinaConfVO.setAnnoCostruzione(annoCostruzioneIt);
        }
        macchinaConfVO.setModello(modello);
        macchinaConfVO.setMatricolaTelaio(matricolaTelaio);
        
        possessoMacchinaConfVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso));
        
        BigDecimal percentualePossessoBg = new BigDecimal(percentualePossesso);
        possessoMacchinaConfVO.setPercentualePossesso(percentualePossessoBg);
        
        if(Validator.isNotEmpty(dataCarico))
        {
          possessoMacchinaConfVO.setDataCarico(DateUtils.parseDate(dataCarico));
        }
        
        
        macchinaConfVO.setNote(request.getParameter("note"));
      
        macchinaConfVO.setExtIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
        possessoMacchinaConfVO.setMacchinaVO(macchinaConfVO);
        possessoMacchinaConfVO.setExtIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      
        gaaFacadeClient.importaMacchinaAgricola(idPossessoMacchina, possessoMacchinaConfVO);
        
        %>
          <jsp:forward page="<%= motoriAgricoliIncaricoCtrlUrl %>" />
        <%
        return;
      }
    
    }
    
    
    
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - importaMacchinaAgricolaModCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_DATI_UMA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

%>
<jsp:forward page="<%=importaMacchinaAgricolaModUrl%>" />


 