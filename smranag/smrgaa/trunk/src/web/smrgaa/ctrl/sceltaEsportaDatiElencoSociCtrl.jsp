<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO" %>


<%

  String iridePageName = "sceltaEsportaDatiElencoSociCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - sceltaEsportaDatiElencoSociCtrl.jsp - INIZIO PAGINA");
  
  String sceltaEsportaDatiElencoSociURL = "/view/sceltaEsportaDatiElencoSociView.jsp";
  String excelUrl = "/servlet/ExcelElencoSociServlet";
  String excelUVUrl = "/servlet/ExcelElencoSociRiepilogoVinoDOPProvinciaServlet";
  String excelUmaUrl = "/servlet/ExcelElencoSociUmaServlet";
  String excelColtureVarietaUrl = "/servlet/ExcelElencoSociColturaVarietaServlet";
  String excelFruttaGuscioUrl = "/servlet/ExcelElencoSociFruttaGuscioServlet";
  String actionUrl = "../layout/aziendeCollegate.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String sceltaEsportaDatiUV = "../layout/sceltaEsportaDatiElencoSoci.htm";
  
  final String errMsg = "Impossibile procedere nella sezione esporta dati elenco soci. " +
    "Contattare l'assistenza comunicando il seguente messaggio: ";    
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String messaggioErrore = null;
  
  
  
  //sono già nella pagine e richiamo uno scarico
  if(request.getParameter("regimeEsportaDatiElencoSoci") != null)
  {   
    
    Vector<StoricoParticellaArboreaExcelVO> elencoUnitaArboree = null;
    
    //Scarico UV
    if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiElencoSoci"))
      && request.getParameter("tipoEsportaDatiElencoSoci").equalsIgnoreCase("1"))
    {
      %>
        <jsp:forward page="<%=excelUVUrl %>" />
      <%
      return;
    }
    //Uma
    else if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiElencoSoci"))
      && request.getParameter("tipoEsportaDatiElencoSoci").equalsIgnoreCase("2"))
    {
       %>
         <jsp:forward page="<%=excelUmaUrl %>" />
       <%
       return;
      
      
    }
    //ColtureVaraieta
    else if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiElencoSoci"))
      && request.getParameter("tipoEsportaDatiElencoSoci").equalsIgnoreCase("3"))
    {
       %>
         <jsp:forward page="<%=excelColtureVarietaUrl %>" />
       <%
       return;
      
      
    }
    //FruttaGuscio
    else if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiElencoSoci"))
      && request.getParameter("tipoEsportaDatiElencoSoci").equalsIgnoreCase("4"))
    {
    	Vector<AziendaColVarExcelVO> vAziendeColVar = gaaFacadeClient.getScaricoExcelElencoSociFruttaGuscio(anagAziendaVO.getIdAzienda().longValue());
    	if(vAziendeColVar != null && vAziendeColVar.size() > 0){    	
	       %>
	         <jsp:forward page="<%=excelFruttaGuscioUrl %>" />
	       <%
	       return;
    	}    
    	else{
    		request.setAttribute("messaggioErrore","Non sono stati trovati dati per lo scarico selezionato");
    	}
       
      
      
    }
    //Scarico isole parcelle (classico)
    else
    {
    
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;      
      
    }
      
  }
  else
  { //prima volta che entro
  }
%>
   <jsp:forward page="<%=sceltaEsportaDatiElencoSociURL %>" />