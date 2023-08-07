<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>


<%!
  public static String NO_MODIFICHE_URL="/view/dichiarazioneNoModificheView.jsp";
  public static String ATTENDERE_URL="/view/dichiarazione_attendereView.jsp";  
  public static String NEXT_PAGE_ERRORE_PLSQL="/view/erroreView.jsp";
  public static String URL_DICHIARAZIONE_CONSISTENZA = "../ctrl/dichiarazioneConsistenzaCtrl.jsp";
  
%>


<%

	//Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	String PAGE_ANOMALIA ="";
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
		PAGE_ANOMALIA = "../layout/";
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
		PAGE_ANOMALIA = "/layout/";
	PAGE_ANOMALIA += "dichiarazioneAnomalia.htm";
	
  String iridePageName = "dichiarazioneControllaModificheCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  try
  {
    Integer anno;
    anno=(Integer)(request.getAttribute("anno"));
    if (anno==null)
    {
      anno=DateUtils.getCurrentYear();
    }

    // Metto in sessione il valore dell'anno per cui sto effettuando
    // una nuova dichiarazione di consistenza
    session.setAttribute("anno", anno);

    AnagFacadeClient anagClient = new AnagFacadeClient();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");

    if(anagClient.controlloUltimeModifiche(anagVO.getIdAzienda(),anno)) 
    {
      //Sono state fatte modifiche alla dichiarazione di consistenza quindi
      //si può continuare con l'inserimento

      // Effettuo i controlli PL-SQL
      String ris = null;
      try 
      {
        ris = anagClient.controlliDichiarazionePLSQL(
        anagVO.getIdAzienda(),anno, idMotivoDichiarazione, ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) 
      {
        String messaggio = se.getMessage();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        //E' stata fatta una previsione del piano colturale
        %>
          <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
        <%
        return;

      }
      if(ris == null) 
      {
        //Non posso proseguire con la dichiarazione
        String messaggio = (String)AnagErrors.get("ERR_GENERIC_PLSQL");
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        //E' stata fatta una previsione del piano colturale
        %>
          <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
        <%
        return;
      }
      if (ris.equals("N")) 
      {
        //Posso permettere il salvataggio della dichiarazione
        %>
          <jsp:forward page="<%=PAGE_ANOMALIA%>" />
        <%
        return;
      }
      if (ris.equals("E"))
      {
        //Visualizzo gli errori e non permetto il proseguimento
        %>
          <jsp:forward page="<%=PAGE_ANOMALIA%>" />
        <%
        return;
      }
    }
    else
    {
      %>
        <jsp:forward page="<%=NO_MODIFICHE_URL%>" />
      <%
      return;
    }
  }
  catch(SolmrException s) 
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new ValidationError(s.getMessage()));
    request.setAttribute("errors",ve);
    s.printStackTrace();
  }
  catch(Exception e) 
  {
    /**
     * La ValidationErrors è una collezione di ValidationError
     */
    it.csi.solmr.util.ValidationErrors ve=new ValidationErrors();
    /**
     * Il ValidationError rappresenta un'errore
     */
    ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
    request.setAttribute("errors",ve);
    e.printStackTrace();
  }
%>

