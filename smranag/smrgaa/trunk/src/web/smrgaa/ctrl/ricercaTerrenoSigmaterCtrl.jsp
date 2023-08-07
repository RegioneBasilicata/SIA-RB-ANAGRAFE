<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno" %>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato" %>
<%@ page import="it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%
  SolmrLogger.debug(this, " - ricercaTerrenoSigmaterCtrl.jsp - INIZIO PAGINA");

  String iridePageName = "ricercaTerrenoSigmaterCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String viewUrl = "/view/ricercaTerrenoSigmaterView.jsp";
  
  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    StoricoParticellaVO storicoParticellaVO = null;
    Long idParticella = Long.decode(request.getParameter("idParticella"));
    String codIstatComune=null;
    String codBelfioreComune=null;
    String sezione=null;
    String foglio=null;
    String numero=null;
    String subalterno=null;
    String progressivo=null;
    
    
    // Cerco i dati della particella in relazione all'id conduzione selezionato
    try 
    {
      storicoParticellaVO = anagFacadeClient.findCurrStoricoParticellaByIdParticella(new Long(idParticella));
    }
    catch(Exception e) 
    {
      SolmrLogger.getStackTrace(e);
      request.setAttribute("messaggioErrore1",(String)AnagErrors.get("ERRORE_KO_DETTAGLIO_PARTICELLA"));
      request.setAttribute("messaggioErrore2",e.toString());
      %>
        <jsp:forward page="<%=viewUrl%>" />
      <%
      return;
    }
    request.setAttribute("storicoParticellaVO", storicoParticellaVO);
    if(storicoParticellaVO != null) 
    {
      codIstatComune=storicoParticellaVO.getIstatComune();
      sezione=storicoParticellaVO.getSezione();
      foglio=storicoParticellaVO.getFoglio();
      numero=storicoParticellaVO.getParticella();
      subalterno=storicoParticellaVO.getSubalterno();
      progressivo=null;
    }
      
    
    
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    
    
    
    DettaglioTerreno dettTerreno=null;
    DettaglioFabbricato dettFabbricato=null;
    Titolarita titolarita[]=null;
    String presenzaFabbricati=SolmrConstants.FLAG_NO;
    
    dettTerreno=gaaFacadeClient.cercaTerreno(codIstatComune,codBelfioreComune,sezione,foglio,numero,subalterno,progressivo);             
    request.setAttribute("dettaglioTerreno",dettTerreno);    
                                  
    try
    {                 
      dettFabbricato=gaaFacadeClient.cercaFabbricato(codIstatComune,codBelfioreComune,sezione,foglio,numero,subalterno,progressivo);
      if (dettFabbricato!=null && dettFabbricato.getFabbricato()!=null) presenzaFabbricati=SolmrConstants.FLAG_SI;
    }
    catch(SolmrException se) 
    {
      //Se c'è un errore nella chiamata al servizio lo comunico ma continuo con il flusso del programma
      presenzaFabbricati=(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT")+"<BR>"+se.getMessage();
    }
    request.setAttribute("presenzaFabbricati",presenzaFabbricati); 
    
    try
    {                      
      String idImmobile=null;
      if (dettTerreno!=null && dettTerreno.getTerreno()!=null)
        idImmobile=dettTerreno.getTerreno().getIdImmobile();
      String tipoImmobile="T"; 
      String data=DateUtils.getCurrent(DateUtils.DATA);
      //idImmobile è un dato obbligatorio, quindi chiamo il servizio solo se è valorizzato
      if (idImmobile!=null)
        titolarita=gaaFacadeClient.cercaTitolaritaOggettoCatastale(codIstatComune,codBelfioreComune,sezione,idImmobile,tipoImmobile,data,data);
    }
    catch(SolmrException se) 
    {
      //Se c'è un errore nella chiamata al servizio lo comunico ma continuo con il flusso del programma
      request.setAttribute("messaggioErrore3",(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT"));
      request.setAttribute("messaggioErrore4",se.getMessage());
    }
    request.setAttribute("titolarita",titolarita); 
                           
    
  }
  catch(SolmrException se) 
  {
    request.setAttribute("messaggioErrore1",(String)AnagErrors.get("ERR_SIGMATER_TO_CONNECT"));
    request.setAttribute("messaggioErrore2",se.getMessage());
  }
  catch(Exception e)
  {
    SolmrLogger.getStackTrace(e);
    request.setAttribute("messaggioErrore1",AnagErrors.ERRORE_500);
    request.setAttribute("messaggioErrore2",e.toString());
  }
  
  SolmrLogger.debug(this, " - ricercaTerrenoSigmaterCtrl.jsp - FINE PAGINA");
    
  request.getRequestDispatcher(viewUrl).forward(request, response);

%>
