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
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String macchinaAgricolaInserisciUrl = "/view/macchinaAgricolaInserisciView.jsp";
  String actionUrl = "../layout/motori_agricoli_incarico.htm";
  String motoriAgricoliIncaricoCtrlUrl = "/ctrl/motori_agricoli_incaricoCtrl.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella sezione inerisci macchina agricola. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  String iridePageName = "macchinaAgricolaInserisciCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  ValidationErrors errors = null;
  
  
  
  Date parametroScadenzaImpoMacc = null;
  try 
  {
    parametroScadenzaImpoMacc = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_SCADENZA_IMPO_MACC);
    request.setAttribute("parametroScadenzaImpoMacc", parametroScadenzaImpoMacc);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - motori_agricoli_incaricoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_SCADENZA_IMPO_MACC+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  try 
  {
    
    
    Vector<UteVO> vUte = anagFacadeClient.getElencoIdUTEByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("vUte", vUte);
    
    String flagIrroratrice = "";
    if(parametroScadenzaImpoMacc.after(new Date()))
      flagIrroratrice = "S";
      
    Vector<TipoMacchinaGaaVO> vTipoMacchina = gaaFacadeClient.getElencoTipoMacchina(flagIrroratrice);
    request.setAttribute("vTipoMacchina", vTipoMacchina);
    
    String idTipoMacchina = request.getParameter("idTipoMacchina");
    if(Validator.isNotEmpty(idTipoMacchina))
    {
      Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = gaaFacadeClient.getElencoTipoGenereMacchinaFromRuolo(
        new Long(idTipoMacchina), ruoloUtenza.getCodiceRuolo());
      request.setAttribute("vGenereMacchina", vGenereMacchina);
    }
    
    
    Vector<CodeDescription> vTipoMarca = null;
    String idGenereMacchina = request.getParameter("idGenereMacchina");
    if(Validator.isNotEmpty(idGenereMacchina))
    {
      Vector<TipoCategoriaGaaVO> vCategoria = gaaFacadeClient.getElencoTipoCategoria(
        new Long(idGenereMacchina));
      request.setAttribute("vCategoria", vCategoria);
      
      vTipoMarca = gaaFacadeClient.getElencoTipoMarcaByIdGenere(new Long(idGenereMacchina));
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
    
    
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
    {
      //Validazione e inserimento
      PossessoMacchinaVO possessoMacchinaVO = new PossessoMacchinaVO();
      MacchinaGaaVO macchinaVO = new MacchinaGaaVO();
      if(Validator.isNotEmpty(request.getParameter("idUte")))
      {
        possessoMacchinaVO.setIdUte(new Long(request.getParameter("idUte")));
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idUte", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      if(Validator.isEmpty(request.getParameter("idTipoMacchina")))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idTipoMacchina", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      if(Validator.isNotEmpty(request.getParameter("idGenereMacchina")))
      {
        macchinaVO.setIdGenereMacchina(new Long(request.getParameter("idGenereMacchina")));
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idGenereMacchina", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      if(Validator.isNotEmpty(request.getParameter("idCategoria")))
      {
        macchinaVO.setIdCategoria(new Long(request.getParameter("idCategoria")));
      }
      else
      {
        Vector<TipoCategoriaGaaVO> vCategoria = null;
        if(Validator.isNotEmpty(idGenereMacchina))
		    {
		      vCategoria = gaaFacadeClient.getElencoTipoCategoria(
		        new Long(idGenereMacchina));
		    }
      
        if(vCategoria != null)
          errors = ErrorUtils.setValidErrNoNull(errors,"idCategoria", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      String annoCostruzione = request.getParameter("annoCostruzione");
      String idMarca = request.getParameter("idMarca");
      String modello = request.getParameter("modello");
      String matricolaTelaio = request.getParameter("matricolaTelaio");
      
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
          macchinaVO.setIdMarca(idMarcaLg);
        }
       
        if(Validator.isNotEmpty(annoCostruzione))
        {
          if(Validator.isNumericInteger(annoCostruzione))
          {
	          annoCostruzioneIt = new Integer(annoCostruzione);
	          macchinaVO.setAnnoCostruzione(annoCostruzioneIt);
	        }
	        else
	        {
	          errors = ErrorUtils.setValidErrNoNull(errors,"annoCostruzione", AnagErrors.ERR_CAMPO_VALIDO);
	        }
        }
        macchinaVO.setModello(modello);
        macchinaVO.setMatricolaTelaio(matricolaTelaio);
        
        
        boolean flagControlliUnivocita = false;        
        if(Validator.isEmpty(macchinaVO.getIdCategoria()))
        {
          flagControlliUnivocita = true;
        }
        else
        {
          TipoCategoriaGaaVO tipoCategoriaGaaVO = gaaFacadeClient.getTipoCategoriaFromPK(macchinaVO.getIdCategoria().longValue());
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
          
        
        if(flagControlliUnivocita)
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
      
      String idTipoFormaPossesso = request.getParameter("idTipoFormaPossesso");
      if(Validator.isNotEmpty(idTipoFormaPossesso))
      {
        possessoMacchinaVO.setIdTipoFormaPossesso(new Long(idTipoFormaPossesso));
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"idTipoFormaPossesso", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      String percentualePossesso = request.getParameter("percentualePossesso");
      if(Validator.isNotEmpty(percentualePossesso))
      {
        if(Validator.isNumberPercentualeMaggioreZero(percentualePossesso))
        {
          BigDecimal percentualePossessoBg = new BigDecimal(percentualePossesso);
          possessoMacchinaVO.setPercentualePossesso(percentualePossessoBg);
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors,"percentualePossesso", AnagErrors.ERRORE_KO_PERCENTUALE_POSSESSO);
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"percentualePossesso", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      String dataCarico = request.getParameter("dataCarico");
      if(Validator.isNotEmpty(dataCarico))
      {
        if(Validator.isDate(dataCarico))
        {
        
          Date dataCaricoDt = DateUtils.parseDate(dataCarico);
          if(Validator.isNotEmpty(annoCostruzioneIt))
          {           
            if(DateUtils.extractYearFromDate(dataCaricoDt) < annoCostruzioneIt.intValue())
            {
              errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERRORE_KO_DATA_CARICO_ANNO);
            }
            else
            {
	            if(dataCaricoDt.after(new Date()))
	            {
	               errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERRORE_KO_DATA_CARICO_SYSDATE);
	            }
	            else
	            {
	              possessoMacchinaVO.setDataCarico(dataCaricoDt);
	            }
            }
          }
          else
          {
            if(dataCaricoDt.after(new Date()))
            {
               errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERRORE_KO_DATA_CARICO_SYSDATE);
            }
            else
            {
              possessoMacchinaVO.setDataCarico(dataCaricoDt);
            }
          }
          
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERR_CAMPO_VALIDO);
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"dataCarico", AnagErrors.ERR_CAMPO_OBBLIGATORIO);
      }
      
      macchinaVO.setNote(request.getParameter("note"));
      
      macchinaVO.setExtIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      possessoMacchinaVO.setMacchinaVO(macchinaVO);
      possessoMacchinaVO.setExtIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      
      
      if(errors !=null) 
      {
        request.setAttribute("errors", errors);
      }
      //salva dati
      else
      {
        gaaFacadeClient.inserisciMacchinaAgricola(possessoMacchinaVO);
        
        %>
          <jsp:forward page="<%= motoriAgricoliIncaricoCtrlUrl %>" />
        <%
        return;
      }
    
    }
    
    
    
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - macchinaAgricolaInserisciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_INSERISCI_DATI_UMA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

%>
<jsp:forward page="<%=macchinaAgricolaInserisciUrl%>" />


 