<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	
  String allevamentiUrl ="/view/allevamentiView.jsp";
  String excelUrl = "/servlet/ExcelAllevamentiServlet";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
	// Pulisco la sessione dai filtri di altre sezioni
  //String noRemove = "idPianoRiferimentoAllevamento";
	WebUtils.removeUselessFilter(session, null);

	String iridePageName = "allevamentiCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  final String errMsg = "Impossibile procedere nella sezione Allevamenti. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  
  String idUte = request.getParameter("idUte");
  Long idUteLg = null;
  if(Validator.isNotEmpty(idUte))
  {
    idUteLg = new Long(idUte);
  }
  // Recupero i valori relativi alle unità produttive
  Vector<UteVO> elencoUte = new Vector<UteVO>();
  try 
  {
    String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE, (String)SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
    elencoUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
    request.setAttribute("elencoUte", elencoUte);
  }
  catch(SolmrException se) 
  {
    ValidationErrors errors = new ValidationErrors();
    errors.add("idUte", new ValidationError(AnagErrors.ERRORE_KO_UTE));
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(allevamentiUrl).forward(request, response);
    return;
  }
  
  
  
  //Controllo se è stato selezionato lo scarico del file excel
  if("excel".equals(request.getParameter("operazione")))
  {
    String pianoRiferimentoEx = request.getParameter("idDichiarazioneConsistenza");
    Date dataInserimentoDichiarazione = null;
    if(pianoRiferimentoEx.equalsIgnoreCase("-1"))
    {
      pianoRiferimentoEx = "AL PIANO IN LAVORAZIONE DEL "+DateUtils.getCurrentDateString();
    }
    else
    {
      ConsistenzaVO consVO = null;
      TipoMotivoDichiarazioneVO tipoVO = null;
      try 
      {
        consVO = anagFacadeClient.getDichiarazioneConsistenza(new Long(pianoRiferimentoEx));
        if(consVO.getIdMotivo() != null)
        {
          tipoVO = anagFacadeClient.findTipoMotivoDichiarazioneByPrimaryKey(new Long(consVO.getIdMotivo()));
        }
        dataInserimentoDichiarazione = consVO.getDataInserimentoDichiarazione();   
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - allevamentiCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      if(tipoVO != null)
      {
        pianoRiferimentoEx = "ALLA DICHIARAZIONE ";
        if(tipoVO.getTipoDichiarazione()
          .equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_CORRETTIVA))
        {
          pianoRiferimentoEx += "(CORRETTIVA) ";
        }
        else if(tipoVO.getTipoDichiarazione()
          .equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_COMUNICAZIONE_10R))
        {
          pianoRiferimentoEx += "(COM 10R) ";
        }
      }
      
      pianoRiferimentoEx += "DEL "+DateUtils.formatDateTimeNotNull(consVO.getDataInserimentoDichiarazione());    
    }
    request.setAttribute("pianoRiferimentoEx",pianoRiferimentoEx);
    
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    
    Vector<AllevamentoVO> vAllevamenti = null;
    try 
    {
      vAllevamenti = gaaFacadeClient
        .getElencoAllevamentiExcel(anagAziendaVO.getIdAzienda().longValue(), dataInserimentoDichiarazione, idUteLg);
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - allevamentiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    request.setAttribute("vAllevamenti", vAllevamenti);    
    
    
    %>
        <jsp:forward page="<%=excelUrl%>" />
    <%
    return;
  }
  
  ValidationError error = null;
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(errors == null) 
  {
    errors = new ValidationErrors();
  }

	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
	// in modo che venga sempre effettuato
	try 
  {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
    request.setAttribute("statoAzienda", se);
  }

	
    	
      		
	// Visualizzazione allevamenti
  session.removeAttribute("notifica");
  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
  Long idPianoRiferimento = null;
  
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    //idPianoRiferimento = new Long("-1");
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }
        		
  // Ricerco gli allevamenti in funzione del piano di riferimento selezionato
	try 
  {
		String[] orderBy = {SolmrConstants.ORDER_BY_DESC_COMUNE, SolmrConstants.ORDER_BY_DESC_TIPO_SPECIE_ASC ,SolmrConstants.ORDER_BY_GENERIC_DATA_INIZIO_ASC, SolmrConstants.ORDER_BY_GENERIC_DATA_FINE_ASC};
    AllevamentoAnagVO[] elencoAllevamenti = anagFacadeClient.getListAllevamentiAziendaByPianoRifererimento(anagAziendaVO.getIdAzienda(), idPianoRiferimento, idUteLg, orderBy);
    request.setAttribute("elencoAllevamenti", elencoAllevamenti);
    AnagAziendaVO aziendaVOTmp = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
    request.setAttribute("aziendaVOTmp", aziendaVOTmp);
  }
  catch(Exception e) 
  {
    error = new ValidationError(AnagErrors.ERRORE_KO_ALLEVAMENTI);
  	errors.add("idDichiarazioneConsistenza", error);
  	request.setAttribute("errors", errors);
  	request.getRequestDispatcher(allevamentiUrl).forward(request, response);
  	return;
  }
  if(errors != null && errors.size() > 0) 
  {
    request.setAttribute("errors", errors);
  }
  %>
    <jsp:forward page="<%=allevamentiUrl%>" />
