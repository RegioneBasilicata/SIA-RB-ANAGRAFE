<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors "%>
<%@ page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%!
  public final static String VIEW = "../view/allevamentiVerificaView.jsp";
%>

<%

  String iridePageName = "allevamentiVerificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String erroreViewUrl = "/view/erroreView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  

  SolmrLogger.debug(this, " - allevamentiVerificaCtrl.jsp - INIZIO PAGINA");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  ValidationErrors errors = new ValidationErrors();
  String idControllo = request.getParameter("idControllo");
  String segnalazioneBloccante = request.getParameter("segnalazioneBloccante");
  String segnalazioneWarning = request.getParameter("segnalazioneWarning");
  String segnalazioneOk = request.getParameter("segnalazioneOk");
  
  String operazione = request.getParameter("operazione");
  String ordinamento = null;
  
  
  
  
  
  // Recupero i valori relativi al tipo controllo
  TipoControlloVO[] elencoTipiControllo = null;
  try 
  {
    String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
    elencoTipiControllo = anagFacadeClient.getListTipoControlloByIdGruppoControlloAttivi(
       new Long(SolmrConstants.ID_GRUPPO_CONTROLLO_ALLEVAMENTI), orderBy);
    request.setAttribute("elencoTipiControllo", elencoTipiControllo);
      
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_ALLEVAMENTI);
    errors.add("idControllo", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(VIEW).forward(request, response);
    return;
  }

  try
  {
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");    
    AnagAziendaVO aziendaVOTmp = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
    request.setAttribute("aziendaVOTmp", aziendaVOTmp);
       
        
    HashMap<Long, ControlloAllevamenti> hEsitoControllo 
      = gaaFacadeClient.getEsitoControlliAllevamentiAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("hEsitoControllo", hEsitoControllo);
    
    HashMap<Long, ControlloAllevamenti> hSegnalazioneControllo 
      = gaaFacadeClient.getSegnalazioniControlliAllevamentiAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("hSegnalazioneControllo", hSegnalazioneControllo);
    
    
    Vector<TipoControlloVO> elencoTipiControlloEsito = new Vector<TipoControlloVO>();
    
    for(int j=0;j<elencoTipiControllo.length;j++)
    {
      elencoTipiControlloEsito.add(elencoTipiControllo[j]);
    }
    
    // L'utente ha selezionato l'ordinamento per titolo possesso decrescente
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idTipologiaDisc")) 
    {
      elencoTipiControlloEsito = new Vector<TipoControlloVO>();
      //reverse elenco
      for(int j=elencoTipiControllo.length-1;j>=0;j--)
      {
        elencoTipiControlloEsito.add(elencoTipiControllo[j]);
      }
    }
    
    
    
    Long idControlloL = null;
    if(Validator.isNotEmpty(idControllo))
    {
      idControlloL = new Long(idControllo);
      int k = elencoTipiControlloEsito.size();
      for(int i=0;i<k;)
      {
	      if(elencoTipiControlloEsito.get(i).getIdControllo().compareTo(idControlloL) !=0)
	      {
	        elencoTipiControlloEsito.remove(i);
	        k--;
	      }
	      else
	      {
	        i++;
	      }
	    }
    }
    
    
    
    boolean flagBloccante = false;
    boolean flagWarning = false;
    boolean flagOK = false;
    if(Validator.isEmpty(segnalazioneBloccante) 
      && Validator.isEmpty(segnalazioneWarning) 
      && Validator.isEmpty(segnalazioneOk))
    {
      flagBloccante = true;
      flagWarning = true;
      flagOK = true;
    }
    else
    {
      if(Validator.isNotEmpty(segnalazioneBloccante))
      {
        flagBloccante = true;        
      }
      
      if(Validator.isNotEmpty(segnalazioneWarning))
      {
        flagWarning = true;
      }
      
      if(Validator.isNotEmpty(segnalazioneOk))
      {
        flagOK = true;
      }
    }
    
    int k = elencoTipiControlloEsito.size();
    for(int i=0;i<k;)
    {
      boolean trovato = false;
      TipoControlloVO tipoControllo = elencoTipiControlloEsito.get(i);
      Long idControlloTmp = tipoControllo.getIdControllo();
      if((hEsitoControllo != null ) && (hEsitoControllo.get(idControlloTmp) != null))
      {
        ControlloAllevamenti ctrlAllevamenti = hEsitoControllo.get(idControlloTmp);
        String bloccante = ctrlAllevamenti.getBloccante();
        if("S".equalsIgnoreCase(bloccante) && flagBloccante)
        {
          trovato = true;
        }
        else if("N".equalsIgnoreCase(bloccante) && flagWarning)
        {
          trovato = true;
        }
      }
      else if((hSegnalazioneControllo != null) && (hSegnalazioneControllo.get(idControlloTmp) != null))
      {
        ControlloAllevamenti ctrlAllevamenti = hSegnalazioneControllo.get(idControlloTmp);
        String bloccante = ctrlAllevamenti.getBloccante();
        if("S".equalsIgnoreCase(bloccante) && flagBloccante)
        {
          trovato = true;
        }
        else if("N".equalsIgnoreCase(bloccante) && flagWarning)
        {
          trovato = true;
        }
      }
      else
      {
        if(flagOK)
          trovato = true;
      }
      
      if(!trovato)
      {
        elencoTipiControlloEsito.remove(i);
        k--;
      }
      else
      {
        i++;
      }
    }
    
    request.setAttribute("elencoTipiControlloEsito", elencoTipiControlloEsito);
    


    %><jsp:forward page="<%= VIEW %>"/><%
  }
  catch(Exception se)
  {
    SolmrLogger.info(this, " - allevamentiVerificaCtrl.jsp - FINE PAGINA");
    String messaggio = se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  SolmrLogger.debug(this, " - allevamentiVerificaCtrl.jsp - FINE PAGINA");
%>