<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="java.util.*" %>


<%!
  public static String DICHIARAZIONE_URL="../layout/dichiarazioneConsistenza.htm";
  public static String DICHIARAZIONE_ELIMINA_URL = "../layout/dichiarazioneElimina.htm";
  public static String DICHIARAZIONE_CONFERMA_ELIMINA_URL = "/view/confermaNuovaAziendaView.jsp";
%>


<%

    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	String dichiarazioneConsistenzaUrl ="";
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
		dichiarazioneConsistenzaUrl = "../layout/";
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
		dichiarazioneConsistenzaUrl = "/layout/";
	dichiarazioneConsistenzaUrl += "dichiarazioneConsistenza.htm";


	String iridePageName = "dichiarazioneEliminaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
  	
	try 
  {
  	Long idDichiarazioneConsistenza = null;
  	try 
    {
    	idDichiarazioneConsistenza=new Long(Long.parseLong(request.getParameter("radiobuttonAzienda")));
  	}
  	catch(Exception e) 
    {}

  	AnagFacadeClient anagClient = new AnagFacadeClient();

  	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    ConsistenzaVO consistenzaVO=null;

  	if(idDichiarazioneConsistenza != null)
    { 
    	anagClient.deleteDichiarazioneConsistenza(idDichiarazioneConsistenza);
    }
  	else 
    {
     		// Sono stato chiamato dalla pagina dell'elenco, quindi devo andare a vedere l'idDichiarazioneConsistenza selezionato
  		try 
      {
    		idDichiarazioneConsistenza = new Long(Long.parseLong(request.getParameter("radiobutton")));
        consistenzaVO = anagClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
  		}
  		catch(Exception e) {
    		ValidationErrors ve = new ValidationErrors();
    		ve.add("error",new ValidationError("Selezionare una dichiarazione di consistenza!"));
    		request.setAttribute("errors",ve);
  		}
  		// controllo se sia possibile eliminare una dichiarazione
  		if(anagClient.deleteDichConsAmmessa(anagVO.getIdAzienda(),ruoloUtenza.getIdUtente(),idDichiarazioneConsistenza)) 
      {
  			String domanda = null;
  			// Verifico se la validazione selezionata è relativa ad un fascicolo già inviato a Roma
        boolean fascicoloInviato=false;
    
        // Icona relativa alla validazione dati da parte del SIAN Fascicolo
        if(consistenzaVO.getDataAggiornamentoFascicolo() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomalia()) && consistenzaVO.getFlagAnomalia().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
            fascicoloInviato=true;
        }
        // Icona relativa alla validazione dati da parte del SIAN UV
        if(consistenzaVO.getDataAggiornamentoUV() != null) 
        {
          if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaUV()) && consistenzaVO.getFlagAnomaliaUV().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
            fascicoloInviato=true;
        }
     		
  			if(fascicoloInviato) 
  				domanda = SolmrConstants.RICHIESTA_CONFERMA_DELETE_VALIDAZIONE_CON_FASCICOLI_NAZIONALE_BACKUP;
  			else 
  				domanda = (String)SolmrConstants.get("DICHIARAZIONE_CONFIRM_DELETE");
  			
      	request.setAttribute("radiobuttonAzienda", idDichiarazioneConsistenza.toString());
      	request.setAttribute("pageBack",DICHIARAZIONE_URL);
      	request.setAttribute("DICHIARAZIONE_ELIMINA_URL",DICHIARAZIONE_URL);
      	request.setAttribute("domanda", domanda);
      	request.getRequestDispatcher(DICHIARAZIONE_CONFERMA_ELIMINA_URL).forward(request, response);
      	return;
  		}
  		else 
      {
    		// Non è possibile eliminare la dichiarazione
    		/*ValidationErrors ve = new ValidationErrors();
    		ve.add("error", new ValidationError((String)SolmrConstants.get("DICHIARAZIONE_N0_DELETE")));
    		request.setAttribute("errors",ve);*/
    		request.setAttribute("errorsJQuery", SolmrConstants.get("DICHIARAZIONE_N0_DELETE"));
  		}
  	}
	}
	catch(SolmrException s) {
  	ValidationErrors ve = new ValidationErrors();
  	ve.add("error",new ValidationError(s.getMessage()));
  	request.setAttribute("errors",ve);
  	s.printStackTrace();
	}
	catch(Exception e) {
  	it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
  	ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  	request.setAttribute("errors",ve);
  	e.printStackTrace();
	}
	%>
	<jsp:forward page="<%= dichiarazioneConsistenzaUrl %>"/>
	
