<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Converter"%>
<%@page import="it.csi.solmr.util.Validator"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%!  public static final String ERROR_VIEW = "/view/erroreView.jsp";
 %><%
  String iridePageName = "cessazioneTerrenoCtrl.jsp";
%><%@include file="/include/autorizzazione.inc"%>
<%
  String confermaCessazioneTerrenoUrl = "/view/confermaView.jsp";
 
  //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
  ResourceBundle res = ResourceBundle.getBundle("config");
  String ambienteDeploy = res.getString("ambienteDeploy");
  SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
  String paginaRitorno ="";
  if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
	paginaRitorno = "../layout/";
  else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
	paginaRitorno = "/layout/";
  paginaRitorno += "elencoTerreni.htm";
  
  String urlForm = "../layout/cessazioneTerreno.htm";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String idParticelleVect[] = request
      .getParameterValues("idParticella");
  long idParticella[] = Converter
      .stringArrayToLongBaseArray(idParticelleVect);
  if (anagFacadeClient
      .areParticelleNonCessateByIdParticelle(idParticella))
  {
    if (idParticella.length>1)
    {
      request.setAttribute("errore",
        AnagErrors.ERRORE_CESSAZIONE_PARTICELLE_CON_PARTICELLE_GIA_CESSATE);
    }
    else
    {
      request.setAttribute("errore",
          AnagErrors.ERRORE_CESSAZIONE_PARTICELLE_CON_PARTICELLA_GIA_CESSATA);
    }
    // Vado alla pagina di errore con messaggio di errore
    request.setAttribute("pageBack","javascript:history.go(-1)");
%>
<jsp:forward page="<%=ERROR_VIEW%>" />
<%
  return;
  }
  // L'utente ha premuto il pulsante salva
  if (request.getParameter("conferma") != null)
  {

    // Recupero l'array di id_storico_particella  

    // Effettuo la cessazione del terreno
    anagFacadeClient.cessaParticelleByIdParticellaRange(idParticella);
    String from = request.getParameter("from");
    if (Validator.isNotEmpty(from))
    {
      paginaRitorno = from;
    }
%>
<jsp:forward page="<%=paginaRitorno%>" />
<%
  }
  // L'utente ha selezionato il pulsante annulla
  else
    if (request.getParameter("annulla") != null)
    {
      String from = request.getParameter("from");
      if (Validator.isNotEmpty(from))
      {
        paginaRitorno = from;
      }
%>
<jsp:forward page="<%=paginaRitorno%>" />
<%
  }
    // L'utente ha selezionato la funzione cessazione terreno
    else
    {

      // Controllo se la particella selezionata possiede delle conduzioni attive
      int countParticelleConConduzioniAttive = 0;
      countParticelleConConduzioniAttive = anagFacadeClient
          .countParticelleConConduzioniAttive(idParticella);
      String messaggio = null;
      if (countParticelleConConduzioniAttive > 0)
      {
        // Se le possiede...
        // ...Mando l'utente ad una pagina di warning che lo avvisi che, cessando
        // la particella, si creerà uno stato di contenzioso e se vuole procedere
        // con l'operazione
        if (countParticelleConConduzioniAttive == 1)
        {
          messaggio = (String) AnagErrors
              .get("WARNING_CESSAZIONE_TERRENO_CONTENZIOSO");
        }
        else
        {
          messaggio = AnagErrors.WARNING_CESSAZIONE_TERRENO_CONTENZIOSO_MULTIPLO;
        }
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("urlForm", urlForm);
        // Vado alla pagina di warning
%>
<jsp:forward page="<%=confermaCessazioneTerrenoUrl%>" />
<%
  }
      else
      {
        // Se non le possiede...
        // ...Mando l'utente ad una pagina di richiesta conferma operazione cessazione
        // particella
        messaggio = (String) SolmrConstants
            .get("RICHIESTA_CONFERMA_CESSAZIONE_TERRENO");
        request.setAttribute("messaggio", messaggio);
        request.setAttribute("urlForm", urlForm);
        // Vado alla pagina di richiesta conferma operazione
%>
<jsp:forward page="<%=confermaCessazioneTerrenoUrl%>" />
<%
  }
    }
%>
