<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
  public final static String VIEW = "../view/manodoperaNewView.jsp";
%>

<%

  String iridePageName = "manodoperaNewCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
		
	  
	String AVANTI ="";
	String ANNULLA ="";	
	  
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		AVANTI = "../layout/";
		ANNULLA = "../layout/";		
	}	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		AVANTI = "/layout/";
		ANNULLA = "/layout/";
	}	
	AVANTI += "manodoperaNewConduzione.htm";
	ANNULLA += "manodopera.htm";
  
  
  String actionUrl = "../layout/manodopera.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserisci manodopera."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  //Click sulla voce di menù "Inserisci Manodopera"
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ValidationErrors errors = new ValidationErrors();

  //Il sistema controlla che non esista già una dichiarazione di manodopera valida,
  try
  {
    String errore = anagFacadeClient.isManodoperaValida(null, anagAziendaVO.getIdAzienda());
    if(Validator.isNotEmpty(errore))
    {
      String messaggio = errore;
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;     
    }
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_DETT_MANODOPERA+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  

  //Value Object necessari per l'inserimento della manodopera
  ManodoperaVO manodoperaVO = null;

  //Vector contenente un DettaglioManodoperaVO per ciascun tipo classe manodopera
  Vector vDettaglioManodopera = null;

  //Vector presente in common contenente i record di tipo forma conduzione
  //e necessario per proporre il tipo forma conduzione nella pagina seguente
  Vector vTipoFormaConduzione = null;
  
  
  
  try
  {
    Vector<TipoIscrizioneINPSVO> vTipoIscrizioneINPS = anagFacadeClient.getElencoTipoIscrizioneINPSAttivi();
    request.setAttribute("vTipoIscrizioneINPS", vTipoIscrizioneINPS);
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_FORMA_ISCRIZIONE_INPS+"::"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  //HashMap tenuto in session e contenente tutti i dati per l'inserimento della manodopera
  HashMap hmManodopera = (HashMap) session.getAttribute("common");
  if (hmManodopera == null) 
  {
    hmManodopera = new HashMap();

    manodoperaVO = new ManodoperaVO();
    vDettaglioManodopera = new Vector(5); //(numero di record Tipo Classe Manodopera)

    //HashMap necessari per la gestione attività complementari tra i due folder;
    //viene gestito il problema di tenere traccia dell'aggiungi ed elimina
    //delle attività complementari da parte dell'utente andando avanti e indietro
    //da una pagina all'altra
    hmManodopera.put("idTipoAttCompl", new String[0]);
    hmManodopera.put("desTipoAttCompl", new String[0]);
    hmManodopera.put("descrizioneAttivitaCompl", new String[0]);
  }
  else 
  {
    manodoperaVO = (ManodoperaVO) hmManodopera.get("manodoperaVO");
    vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();
  }

  //E' stato premuto il tasto avanti
  if (request.getParameter("avanti") != null) 
  {
    //set dei valori impostati dall'utente
    manodoperaVO.setCodiceInps(request.getParameter("codiceInps").trim());
    manodoperaVO.setMatricolaInail(request.getParameter("matricolaInail").trim());
    if(Validator.isNotEmpty(request.getParameter("idTipoIscrizioneINPS")))
      manodoperaVO.setIdTipoIscrizioneINPS(new Integer(request.getParameter("idTipoIscrizioneINPS")));
    else
      manodoperaVO.setIdTipoIscrizioneINPS(null);
    
    if(Validator.isNotEmpty(request.getParameter("dataInizioIscrizione")))
      manodoperaVO.setDataInizioIscrizione(request.getParameter("dataInizioIscrizione"));
    else
    {
      manodoperaVO.setDataInizioIscrizione(null);
      manodoperaVO.setDataInizioIscrizioneDate(null);
    }
    
    if(Validator.isNotEmpty(request.getParameter("dataCessazioneIscrizione")))
      manodoperaVO.setDataCessazioneIscrizione(request.getParameter("dataCessazioneIscrizione"));
    else
    {
      manodoperaVO.setDataCessazioneIscrizione(null);
      manodoperaVO.setDataCessazioneIscrizioneDate(null);
    }
    
    if(!(Validator.isEmpty(request.getParameter("famTempoPienoUomini")) && Validator.isEmpty(request.getParameter("famTempoPienoDonne"))
      && Validator.isEmpty(request.getParameter("famTempoParzUomini")) && Validator.isEmpty(request.getParameter("famTempoParzDonne"))
      && Validator.isEmpty(request.getParameter("salFisTempoPienoUomini")) && Validator.isEmpty(request.getParameter("salFisTempoPienoDonne"))
      && Validator.isEmpty(request.getParameter("salFisTempoParzUomini")) && Validator.isEmpty(request.getParameter("salFisTempoParzDonne"))
      && Validator.isEmpty(request.getParameter("salAvvUomini")) && Validator.isEmpty(request.getParameter("salAvvDonne"))))
    {
    
      if(vDettaglioManodopera == null)
        vDettaglioManodopera = new Vector<DettaglioManodoperaVO>();
        
	    //Dettaglio manodopera tipo classe manodopera = 10
	    vDettaglioManodopera = impostaDettaglioManodopera(vDettaglioManodopera, (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO"), request.getParameter("famTempoPienoUomini"), request.getParameter("famTempoPienoDonne"), new String("0"), request.getParameter("idFamTempoPieno"));
	
	    //Dettaglio manodopera tipo classe manodopera = 20
	    vDettaglioManodopera = impostaDettaglioManodopera(vDettaglioManodopera, (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE"), request.getParameter("famTempoParzUomini"), request.getParameter("famTempoParzDonne"), new String("0"), request.getParameter("idFamTempoParz"));
	
	    //Dettaglio manodopera tipo classe manodopera = 30
	    vDettaglioManodopera = impostaDettaglioManodopera(vDettaglioManodopera, (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO"), request.getParameter("salFisTempoPienoUomini"), request.getParameter("salFisTempoPienoDonne"), new String("0"), request.getParameter("idSalFisTempoPieno"));
	
	    //Dettaglio manodopera tipo classe manodopera = 40
	    vDettaglioManodopera = impostaDettaglioManodopera(vDettaglioManodopera, (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE"), request.getParameter("salFisTempoParzUomini"), request.getParameter("salFisTempoParzDonne"), new String("0"), request.getParameter("idSalFisTempoParz"));
	
	    //Dettaglio manodopera tipo classe manodopera = 50
	    String giornateAnnue = request.getParameter("salAvvGiornateAnnue");
	    if(!Validator.isNotEmpty(giornateAnnue)) {
	      giornateAnnue = new String("0");
	    }
	    vDettaglioManodopera = impostaDettaglioManodopera(vDettaglioManodopera, (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI"), request.getParameter("salAvvUomini"), request.getParameter("salAvvDonne"), giornateAnnue, request.getParameter("idSalAvv"));
	
	    manodoperaVO.setVDettaglioManodopera(vDettaglioManodopera);
	  }
	  else
	  {
	    vDettaglioManodopera = null;
	    manodoperaVO.setVDettaglioManodopera(null);
	  }

    hmManodopera.put("manodoperaVO", manodoperaVO);

    //questa variabile dovrà essere rimossa dalla sessione
    //al click sull'elenco manodopera (manodoperaCtrl.jsp)
    session.setAttribute("common", hmManodopera);

    errors = manodoperaVO.validateManodopera();
    if (errors != null && errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
    }
    else 
    {
      //Il tipo forma conduzione della pagina successiva viene
      //proposto in base ai dati inseriti in questa pagina
      if(manodoperaVO.getVDettaglioManodopera() != null)
      {
	     // if (manodoperaVO.getTipoFormaConduzione() == null) 
	     // {
        vTipoFormaConduzione = (Vector) hmManodopera.get("vTipoFormaConduzione");
        if (vTipoFormaConduzione == null) 
        {
          vTipoFormaConduzione = anagFacadeClient.getTipoFormaConduzione();
          hmManodopera.put("vTipoFormaConduzione", vTipoFormaConduzione);
        }
        
        String tipoFormaConduzione = manodoperaVO.calcoloIdTipoFormaConduzione(vTipoFormaConduzione);
        manodoperaVO.setTipoFormaConduzione(tipoFormaConduzione);	        

        hmManodopera.put("manodoperaVO", manodoperaVO);

        //questa variabile dovrà essere rimossa dalla sessione
        //al click sull'elenco manodopera (manodoperaCtrl.jsp)
        session.setAttribute("common", hmManodopera);
	    //  }
	    }
	    else
	    {
        manodoperaVO.setTipoFormaConduzione(null);          
 
        hmManodopera.put("manodoperaVO", manodoperaVO);
 
        //questa variabile dovrà essere rimossa dalla sessione
        //al click sull'elenco manodopera (manodoperaCtrl.jsp)
        session.setAttribute("common", hmManodopera);
      }

      %>
        <jsp:forward page="<%= AVANTI %>"/>
      <%
      return;
    }
  }

  //E' stato premuto il tasto annulla
  if (request.getParameter("annulla")!=null) 
  {
    %>
        <jsp:forward page="<%= ANNULLA %>"/>
      <%
      return;
  }

  %>
    <jsp:forward page="<%= VIEW %>"/>
   

<%!

  private Vector impostaDettaglioManodopera(Vector vDettaglioManodopera, String codeTipoClasseManodopera, String uomini, String donne, String giornateAnnue, String idClasseManodopera) {

    int index = new Integer(codeTipoClasseManodopera).intValue()/10-1;

    DettaglioManodoperaVO dettaglioManodoperaVO = null;

    if (vDettaglioManodopera.size() > index) {
      dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(index);
      vDettaglioManodopera.remove(index);
    }

    if (dettaglioManodoperaVO == null)
      dettaglioManodoperaVO = new DettaglioManodoperaVO();

    dettaglioManodoperaVO.setCodTipoClasseManodopera(codeTipoClasseManodopera);
    dettaglioManodoperaVO.setIdClasseManodopera(idClasseManodopera);
    dettaglioManodoperaVO.setUomini(uomini.trim());
    dettaglioManodoperaVO.setDonne(donne.trim());
    if (giornateAnnue != null) giornateAnnue = giornateAnnue.trim();
    dettaglioManodoperaVO.setGiornateAnnue(giornateAnnue);

    vDettaglioManodopera.add(index, dettaglioManodoperaVO);

    return vDettaglioManodopera;
  }
%>
