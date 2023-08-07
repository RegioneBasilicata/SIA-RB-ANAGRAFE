<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "contiCorrentiModCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

  String contiCorrentiModUrl = "/view/conti_correnti_mod.htm";
	String contiCorrentiModView = "/view/contiCorrentiModView.jsp";
	String elencoContiCorrentiUrl = "/view/contiCorrentiView.jsp";
  String actionUrl = "../layout/conti_correnti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
	ValidationErrors errors = new ValidationErrors();
  
  final String errMsg = "Impossibile procedere nella sezione modifica dei conti correnti. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
	// in modo che venga sempre effettuato
	try 
  {
  	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
	}
	catch(SolmrException se) {
 		request.setAttribute("statoAzienda", se);
	}
	
	
	String obbligoGF = null;
  try 
  {
    if((anagAziendaVO.getTipoFormaGiuridica() != null)
      &&  (anagAziendaVO.getTipoFormaGiuridica().getCode() != null))
    {
      obbligoGF = anagFacadeClient.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      request.setAttribute("obbligoGF", obbligoGF);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - contiCorrentiModCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_OBBLIGO_CF+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

	
  ContoCorrenteVO contoCorrenteVO = anagFacadeClient.getContoCorrente(request.getParameter("idContoCorrente"));
  request.setAttribute("contoCorrenteVO", contoCorrenteVO);

	if(request.getParameter("salva") != null) 
  {
  
    contoCorrenteVO.setCin(request.getParameter("cin"));
    contoCorrenteVO.setCifraCtrl(request.getParameter("cctrl"));
    
    contoCorrenteVO.setIntestazione(request.getParameter("intestatario"));
    contoCorrenteVO.setMotivoRivalidazione(request.getParameter("motivoValidazione"));
    contoCorrenteVO.setFlagContoGf(request.getParameter("flagContoGf"));
    
    String ibanConcatenato = contoCorrenteVO.getCodPaese()
        +contoCorrenteVO.getCifraCtrl()
        +contoCorrenteVO.getCin()
        +contoCorrenteVO.getAbi()
        +contoCorrenteVO.getCab()
        +contoCorrenteVO.getNumeroContoCorrente();
    
		SolmrLogger.debug(this,"Comune in Ctrl [" + contoCorrenteVO.getDescrizioneComuneSportello() + "]");
	  // Recupero il parametro OCIN per la gestione dell'obbligatorietà del CIN
  	// del conto corrente
  	String parametroOCin = null;
  	try 
    {
  		parametroOCin = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_OCIN);
  		request.setAttribute("parametroOCin", parametroOCin);
  	}
  	catch(SolmrException se) 
    {        
      SolmrLogger.info(this, " - contiCorrentiModCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_PARAMETRO_OCIN+"::"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
  	}

  	// Se il parametro OCIN è nullo interrompo le operazioni di inserimento
  	if(!Validator.isNotEmpty(parametroOCin)) 
    {
      SolmrLogger.info(this, " - contiCorrentiModCtrl.jsp - FINE PAGINA");
      String messaggio = AnagErrors.ERRORE_KO_INSERT_CONTO_CORRENTE_FOR_PARAMETRO_OCIN;
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", contiCorrentiModUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
  	}
  	
  	

    boolean estinto=false;
    if(request.getParameter("estinto") != null && !"".equals(request.getParameter("estinto"))) 
      estinto=true;

		//Effettuo i controlli per la modifica solo se il conto non è estinto
    if (!estinto) 
      errors = contoCorrenteVO.validateForUpdate(request,ibanConcatenato);
      
      
      
    if("S".equalsIgnoreCase(obbligoGF))
    {
      if("S".equalsIgnoreCase(contoCorrenteVO.getFlagContoGf())
        && !"S".equalsIgnoreCase(contoCorrenteVO.getFlagSportelloGf()))
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("flagContoGf", new ValidationError(AnagErrors.ERRORE_SPORT_NO_GF));
      }
    }

  	if(errors != null && errors.size() > 0) 
    {
    	request.setAttribute("errors",errors);
  	}
  	else 
    {
    	if(estinto) 
      {
        if(request.getParameter("confermaEstinzione") == null) 
        {
          %>
        	  <jsp:forward page="/view/confermaModificaContoCorrenteView.jsp" />
          <%
          return;
      	}
      	contoCorrenteVO.setDataEstinzione(new Date());
    	}
      try
      {
    	  anagFacadeClient.storicizzaContoCorrente(contoCorrenteVO, ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se)
      {
        SolmrLogger.info(this, " - contiCorrentiModCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_PARAMETRO_OCIN+"::"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      //response.sendRedirect("../layout/conti_correnti.htm");
      %>
         <jsp:forward page="../layout/conti_correnti.htm" />
      <%            
      return;
  	}    	
	}
	else if(request.getParameter("annulla") != null) 
  {
   	Vector<ContoCorrenteVO> conti = null;
   	try 
    {
     	conti = anagFacadeClient.getContiCorrenti(anagAziendaVO.getIdAzienda(),false);
    }
    catch(SolmrException se) 
    {
     	ValidationError error = new ValidationError(se.getMessage());
     	errors.add("error",error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(contiCorrentiModView).forward(request, response);
     	return;
    }
    request.setAttribute("conti",conti);
    %>
     	<jsp:forward page= "<%= elencoContiCorrentiUrl %>" />
    <%
	}
  
%>

<jsp:forward page="<%=contiCorrentiModView %>" />

