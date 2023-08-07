<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>


<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "unitaArboreeAllineaGISCtrl.jsp";
  
	%>
		 <%@include file="/include/autorizzazione.inc" %>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeAllineaGISCtrl.jsp - INIZIO PAGINA");
	
	String unitaArboreeAllineaGISUrl = "/view/unitaArboreeAllineaGISView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
	
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  String regimeAllineaUVGIS = request.getParameter("regimeAllineaUVGIS");
  
  final String errMsg = "Impossibile procedere nella sezione allinea gis UV. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/unitaArboreeAllineaGIS.htm";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
	ValidationError error = null;
	String messaggioErrore = null;
  
  
  
  //provengo da elenco UV --- INIZIALIZZAZIONE
  if(Validator.isEmpty(regimeAllineaUVGIS)
   && !"conferma".equalsIgnoreCase(operazione))
  {
    WebUtils.removeUselessFilter(session,null);
  }
  
  
  
  // Recupero il parametro UVAG da comune
  String parametroUVAG = null;
  try 
  {
    parametroUVAG = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_UVAG);
  }
  catch(SolmrException se) {
    messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_UVAG;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= unitaArboreeAllineaGISUrl %>" />
    <%
  }
  
  
  try
  { 
  
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {     
      request.setAttribute("action", action);
      operazione = "conferma";
      request.setAttribute("operazione", operazione);
      
      
      //id selezionati da modificare
      String[] elencoIdIsolaParcella = request.getParameterValues("idIsolaParcella");
      String idDichConsAllineaUV = request.getParameter("idDichConsAllineaUV");
      
      Vector<Long> vIdIsolaParcella = null;
      if(Validator.isNotEmpty(elencoIdIsolaParcella))
      {
        for(int i=0;i<elencoIdIsolaParcella.length;i++)
        {
          if(Validator.isNotEmpty(elencoIdIsolaParcella[i]))
          {
            if(vIdIsolaParcella == null)
            {
              vIdIsolaParcella = new Vector<Long>();
            }
            
            vIdIsolaParcella.add(new Long(elencoIdIsolaParcella[i]));
          }
        }
      }
      
      Long idDichConsAllineaUVLg = null;
      if(Validator.isNotEmpty(idDichConsAllineaUV))
      {
        idDichConsAllineaUVLg = new Long(idDichConsAllineaUV);
      }
      
      session.setAttribute("vIdIsolaParcella", vIdIsolaParcella);
      session.setAttribute("idDichConsAllineaUVLg", idDichConsAllineaUVLg);
      
      
      %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
      return;
    }
    else 
    {
    
    
    
      if("conferma".equalsIgnoreCase(operazione))
      {
        //id selezionati da modificare
        Vector<Long> vIdIsolaParcella = (Vector<Long>)session.getAttribute("vIdIsolaParcella");
        Long idDichConsAllineaUVLg = (Long)session.getAttribute("idDichConsAllineaUVLg");
        
        gaaFacadeClient.allineaUVaGIS(vIdIsolaParcella, anagAziendaVO.getIdAzienda().longValue(),
          idDichConsAllineaUVLg.longValue(), ruoloUtenza);
               
        %>
          <jsp:forward page="<%=terreniUnitaArboreeElencoCtrlUrl%>" />
        <%
        return;
        
        
      }
    
  
      //Controllo che esistano UV associate a più parcelle
      ConsistenzaVO consistenzaVO = gaaFacadeClient.getDichConsUVParcelleDoppie(
        anagAziendaVO.getIdAzienda().longValue());
      
      
      Vector<IsolaParcellaVO> elencoIsoleParcelleVO = null;
      
      //Deve essere stato fatto precedentemente un associa parcelle
      //poichè esistono uv associate a più parcelle  
      if(consistenzaVO != null)
      {
        //non è stato fatto un'associa parcelle
        if(consistenzaVO.getDataConnessioneUnarParcella() == null)
        {
          messaggioErrore = AnagErrors.ERRORE_NO_ASSOCIA_PARCELLE_FOUND;
          request.setAttribute("messaggioErrore",messaggioErrore);
          %>
            <jsp:forward page="<%=unitaArboreeAllineaGISUrl%>" />
          <%
          return;  
        }
        //devo verificare che la data si posteriore alla
        //data aggiornamento di tutte le UV
        else
        {
          elencoIsoleParcelleVO = gaaFacadeClient
            .getElencoIsoleParcelle(parametroUVAG,anagAziendaVO.getIdAzienda().longValue());
            
          for(int i=0;i<elencoIsoleParcelleVO.size();i++)
          {
            if(consistenzaVO.getDataConnessioneUnarParcella()
              .before(elencoIsoleParcelleVO.get(i).getDataAggiornamntoUV()))
            {
              messaggioErrore = AnagErrors.ERRORE_NO_ASSOCIA_PARCELLE_FOUND;
              request.setAttribute("messaggioErrore",messaggioErrore);
              %>
                <jsp:forward page="<%=unitaArboreeAllineaGISUrl%>" />
              <%
              return;  
            }
          }
        
        }   
        
      }
      
      //cerco la parcelle se non esistono UV in più parcelle
      if(elencoIsoleParcelleVO == null)
      { 
        elencoIsoleParcelleVO = gaaFacadeClient
          .getElencoIsoleParcelle(parametroUVAG,anagAziendaVO.getIdAzienda().longValue());
      }
      
      
      if(Validator.isEmpty(elencoIsoleParcelleVO))
      {
        messaggioErrore = AnagErrors.ERRORE_NO_ISOLE_PARCELLE_FOUND;
        request.setAttribute("messaggioErrore",messaggioErrore);
        %>
          <jsp:forward page="<%=unitaArboreeAllineaGISUrl%>" />
        <%
        return;  
      }
     
      
      request.setAttribute("elencoIsoleParcelleVO",elencoIsoleParcelleVO);
  
  
  
      
    } //fine else attendere prego
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - unitaArboreeAllineaGISCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  } 
      
     
      
      
      
      
      
      
      
      
      
      
      
			
	
 	// Vado alla pagina di modifica
 	%>
  <jsp:forward page="<%= unitaArboreeAllineaGISUrl %>" />

