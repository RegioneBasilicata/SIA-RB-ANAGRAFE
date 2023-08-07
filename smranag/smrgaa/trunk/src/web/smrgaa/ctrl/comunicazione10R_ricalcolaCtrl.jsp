<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "comunicazione10R_ricalcolaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - comunicazione10R_ricalcolaCtrl.jsp.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String comunicazione10R_ricalcolaUrl = "/view/comunicazione10R_ricalcolaView.jsp";
  String url = "../layout/comunicazione10R_ricalcola.htm";
  String urlAttenderePrego = "/view/attenderePregoView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  ValidationError error = null;
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  //Comunicazione10RVO  com10 = null;
  
  WebUtils.removeUselessFilter(session,null);

  
  
  final String errMsg = "Impossibile procedere nella sezione comunicazione 10R. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  if(Validator.isNotEmpty("operazione") && "attenderePrego".equalsIgnoreCase(operazione)) 
  {
      request.setAttribute("action", url);
      operazione = null;
      request.setAttribute("operazione", operazione);
      %>
          <jsp:forward page= "<%= urlAttenderePrego %>" />
      <%
  }
  else
  {
  
  
    String flagAdesioneDeroga = null;
    try 
    {
      flagAdesioneDeroga = (String)anagFacadeClient.getValoreParametroAltriDati("DEROGA");
      request.setAttribute("flagAdesioneDeroga", flagAdesioneDeroga);
    }
    catch(SolmrException se) {
      SolmrLogger.info(this, " - comunicazione10R_ricalcolaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+AnagErrors.ERR_PAR_ADESIONE_DEROGA+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    String flagModAdesioneDeroga = null;
	  try 
	  {
	    flagModAdesioneDeroga = (String)anagFacadeClient.getValoreParametroAltriDati("MOD_DEROGA");
	    request.setAttribute("flagModAdesioneDeroga", flagModAdesioneDeroga);
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - comunicazione10R_dettaglio.jsp - FINE PAGINA");
	    String messaggio = errMsg+"::"+AnagErrors.ERR_PAR_MOD_ADESIONE_DEROGA+"::"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
	  }
  
    // Recupero i valori relativi alle unità produttive
    Vector elencoUte = null;
    try 
    {
      //con - 1 si prende il piano corrente
      elencoUte = anagFacadeClient.getListUteByIdAziendaAndIdPianoRiferimento(anagAziendaVO.getIdAzienda(), -1);
      if(Validator.isNotEmpty(elencoUte) && (elencoUte.size() > 0))
      {
        HashMap hElencoUte = new HashMap();
        for(int i=0;i<elencoUte.size();i++)
        {
          UteVO uteVO = (UteVO)elencoUte.get(i);
          hElencoUte.put(uteVO.getIdUte(),uteVO);
        }
        request.setAttribute("hElencoUte", hElencoUte);
      }
    }
    catch(SolmrException se) {
      SolmrLogger.info(this, " - comunicazione10R_ricalcolaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+AnagErrors.ERR_RICERCA_UTE+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    PlSqlCodeDescription plCode = null;
    
    try
    {
      plCode = gaaFacadeClient.ricalcolaPlSql(anagAziendaVO.getIdAzienda().longValue(),
        ruoloUtenza.getIdUtente().longValue());
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - comunicazione10R_ricalcolaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    //Errori nel PlSql
    if((plCode !=null) && (plCode.getDescription() != null))
    {
      String messaggio = "Si è verificato un errore durante il ricalcolo. Chiamare l'assistenza tecnica "+
        "comunicando il seguente errore: "+plCode.getDescription()+" -  "+plCode.getOtherdescription(); 
      request.setAttribute("messaggioErrore",messaggio);
    }
    else
    {
    
      if(Validator.isNotEmpty(elencoUte) && (elencoUte.size() > 0 ))
      { 
        Vector vTotCom10r = null;
        
        for(int i=0;i<elencoUte.size();i++)
        {
          UteVO uteVO = (UteVO)elencoUte.get(i);
          Comunicazione10RVO  com10 = null;
          try 
          {
            com10 = gaaFacadeClient.getComunicazione10RByIdUteAndPianoRifererimento(uteVO.getIdUte().longValue(),-1);      
          }
          catch (SolmrException se) 
          {
            SolmrLogger.info(this, " - comunicazione10R_ricalcolaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          HashMap hCom10R = null;
          
          if(com10 != null)
          {        
            hCom10R = new HashMap();
            hCom10R.put("comunicazione10R",com10);
            Vector<EffluenteVO> vEffluenti = null;
            Vector<EffluenteVO> vEffluentiTratt = null;
            Vector<AcquaExtraVO> vAcqueReflue = null;
            Vector<EffluenteCesAcqVO> vAcquisizioni = null;
            Vector<EffluenteCesAcqVO> vCessioni = null;
            Vector<EffluenteStocExtVO> vStoccaggio = null;
            Vector<RefluoEffluenteVO> vReflui = null;
            try 
            { 
              vEffluenti = gaaFacadeClient.getListEffluenti(com10.getIdComunicazione10R());
              vAcqueReflue = gaaFacadeClient.getListAcquaExtra(com10.getIdComunicazione10R());
              vAcquisizioni = gaaFacadeClient.getListEffluentiCessAcquByidComunicazione(com10.getIdComunicazione10R(), SolmrConstants.ID_TIPO_CAUS_EFF_ACQUISIZIONE);
              vCessioni = gaaFacadeClient.getListEffluentiCessAcquByidComunicazione(com10.getIdComunicazione10R(), SolmrConstants.ID_TIPO_CAUS_EFF_CESSIONE);
              vStoccaggio = gaaFacadeClient.getListStoccaggiExtrAziendali(com10.getIdComunicazione10R());
              vEffluentiTratt = gaaFacadeClient.getListTrattamenti(com10.getIdComunicazione10R());
		          vReflui = gaaFacadeClient.getRefluiComunocazione10r(com10.getIdUte(), 
		            com10.getIdComunicazione10R(), null); 
            }
            catch (SolmrException se) 
            {
              SolmrLogger.info(this, " - comunicazione10R_ricalcolaCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
            
            hCom10R.put("vEffluenti", vEffluenti);
            hCom10R.put("vAcqueReflue",vAcqueReflue);
            hCom10R.put("vAcquisizioni",vAcquisizioni);
            hCom10R.put("vCessioni", vCessioni);
            hCom10R.put("vStoccaggio",vStoccaggio);
            hCom10R.put("vEffluentiTratt", vEffluentiTratt);
            hCom10R.put("vReflui", vReflui);
          
          }
          
          if(hCom10R !=null)
          {
            if(vTotCom10r == null)
            {
              vTotCom10r = new Vector();
            }
            
            vTotCom10r.add(hCom10R);
          }
        }
        
        if(vTotCom10r == null) //non è stata trovata nessuna dichiarazione di consistenza relativa alle ute
        {
          request.setAttribute("messaggioErrore",AnagErrors.ERR_NO_COMUNICAZIONE_10R);  
        }
        else
        {
          request.setAttribute("vTotCom10r",vTotCom10r);
        }
      }
      else //relativo alle ute, non esitono ute per questa azienda
      {
        request.setAttribute("messaggioErrore",AnagErrors.ERR_NO_COMUNICAZIONE_10R);  
      }
    }
  }
  
  
  
  
  
  
  

  

%>

<jsp:forward page= "<%= comunicazione10R_ricalcolaUrl %>" />
