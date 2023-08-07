<%@ page language="java"
    contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.abio.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="it.csi.solmr.util.SolmrLogger" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

  String iridePageName = "anagraficaAltreInformazioniCtrl.jsp";
   %><%@include file = "/include/autorizzazione.inc" %><%
   
  SolmrLogger.debug(this, "- anagraficaAltreInformazioniCtrl.jsp -  INIZIO PAGINA");
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
  OperatoreBiologicoVO operatoreBiologico = null;
  PosizioneOperatoreVO[] posizioneOperatore = null;
  CodeDescription[] cd = null;
  HashMap<Date, CodeDescription[]> hm = null;
  String checkStorico = request.getParameter("chkStorico");
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
          
  String obbligoGF = null;
  try 
  {
    if((anagAziendaVO.getTipoFormaGiuridica() != null)
      && (anagAziendaVO.getTipoFormaGiuridica().getCode() != null))
    {
      obbligoGF = anagFacadeclient.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      request.setAttribute("obbligoGF", obbligoGF);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - anagraficaAltreInformazioniCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_OBBLIGO_CF+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
          
  operatoreBiologico = gaaFacadeClient.getOperatoreBiologicoByIdAzienda(anagAziendaVO.getIdAzienda(), 
    anagAziendaVO.getDataFineVal());
  if(operatoreBiologico!=null)
  {
    if(checkStorico!=null) //caso visualizza storico
      posizioneOperatore = gaaFacadeClient.getAttivitaBiologicheByIdAzienda(
              operatoreBiologico.getIdOperatoreBiologico(), null, true);
    else 
      posizioneOperatore = gaaFacadeClient.getAttivitaBiologicheByIdAzienda(
              operatoreBiologico.getIdOperatoreBiologico(), anagAziendaVO.getDataFineVal(), false);
    
    if(posizioneOperatore!=null&&posizioneOperatore.length>0)
    {
      boolean pianoCorrente = (anagAziendaVO.getDataFineVal()==null)?true:false;
      hm = new HashMap<Date, CodeDescription[]>();
      for(int i=0; i<posizioneOperatore.length; i++)
      {
        cd = gaaFacadeClient.getODCbyIdOperatoreBiologico(operatoreBiologico.getIdOperatoreBiologico(), 
             posizioneOperatore[i].getDataInizioValidita(), pianoCorrente);
        hm.put(posizioneOperatore[i].getDataInizioValidita(), cd);
      }
    }
  }
  
  session.setAttribute("operatoreBiologico", operatoreBiologico);
  session.setAttribute("posizioneOperatore", posizioneOperatore);
  session.setAttribute("ODC", hm);
  
  //Controllo se in sessione esistono errori da altre pagine (ad esempio la pagina di modifica)
	ValidationError errors = (ValidationError)session.getAttribute(SolmrConstants.SESSION_ERRORI_PAGINA);
	
	if (errors!=null) {
		request.setAttribute("errors", errors);
		session.removeAttribute(SolmrConstants.SESSION_ERRORI_PAGINA);
	}
	
%>
    <jsp:forward page="/view/anagraficaAltreInformazioniView.jsp"/>
  <%
  SolmrLogger.debug(this,"- anagraficaAltreInformazioniCtrl.jsp -  FINE PAGINA");
%>