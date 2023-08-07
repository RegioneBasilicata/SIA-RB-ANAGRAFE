<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.exception.SolmrException" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.profile.AgriConstants" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%@ page import="it.csi.iride2.policy.entity.Identita"%>
<%@ page import="java.nio.charset.Charset"%>

<%!
  public static String APPLICATIVO_URL="../servlet/IrideRoleSetter";
%>
<%
  SolmrLogger.debug(this,"[sceltaRuoloPEP:service] BEGIN.");
  java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaRuolo.htm");
  
  final String erroreGenerico = "Si è verificato un errore nella configurazione dei ruoli. Contattare l'assistenza.";

  Htmpl htmpl = new Htmpl(layout);

  String portalName=(String)session.getAttribute("PORTAL_NAME");
  SolmrLogger.debug(this, "--- portaName = "+portalName);  



  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String messaggioErrore = null;
  String URL_ACCESS_POINT = null;

  SolmrLogger.debug(this," ********** charset ="+Charset.defaultCharset());
  
  try
  {
    URL_ACCESS_POINT = (String) session.getAttribute("URL_ACCESS_POINT");
    SolmrLogger.debug(this,"[sceltaRuoloPEP:service] URL_ACCESS_POINT = "+URL_ACCESS_POINT);
    
    if (session.getAttribute(SolmrConstants.IDENTITA) != null){
		SolmrLogger.debug(this," ----- session.getAttribute(SolmrConstants.IDENTITA) != null");
		Identita identita = (Identita)session.getAttribute(SolmrConstants.IDENTITA);
		SolmrLogger.debug(this," sceltaRuoloPEP.jsp - codiceFiscale ="+identita.getCodFiscale());
		SolmrLogger.debug(this," sceltaRuoloPEP.jsp - nome ="+identita.getNome());
		SolmrLogger.debug(this," sceltaRuoloPEP.jsp - cognome ="+identita.getCognome());
		SolmrLogger.debug(this," sceltaRuoloPEP.jsp - livelloAutenticazione ="+identita.getLivelloAutenticazione());
	}	
    else{
    	SolmrLogger.debug(this," ----- session.getAttribute(SolmrConstants.IDENTITA) == null");
    }


    Vector<DoubleStringcodeDescription> ruoliV = new Vector<DoubleStringcodeDescription>();
    //messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
    AnagFacadeClient anagFacadeClient=new AnagFacadeClient();
    //HashMap<String, Ruolo> ruoliH;
    it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo[] arrRuoli;
    it.csi.iride2.policy.entity.Identita id=(it.csi.iride2.policy.entity.Identita)session.getAttribute(SolmrConstants.IDENTITA);
    boolean uniqueRole = false;
    String codeFirstRole = null;
    String idProcedimento = null;

    //Non piu usato con papua ws
    /*it.csi.iride2.policy.entity.Application myApp = new Application(it.csi.solmr.etc.SolmrConstants.APP_NAME_IRIDE2_SMRGAA);
    session.setAttribute("myApp", myApp);*/

    idProcedimento = (String) it.csi.solmr.etc.SolmrConstants.ID_TIPO_PROCEDIMENTO_ANAG;
    htmpl.set("idProcedimento", idProcedimento);
    it.csi.iride2.iridefed.entity.Ruolo[] ruoli = null;

    try
    {
      SolmrLogger.debug(this," ---- chiamata a findRuoliForPersonaInApplicazione");	
      arrRuoli = anagFacadeClient.findRuoliForPersonaInApplicazione(id.getCodFiscale(), id.getLivelloAutenticazione());
      SolmrLogger.debug(this," ---- dopo la chiamata a findRuoliForPersonaInApplicazione");
    }
    catch (SolmrException ex)
    {
      SolmrLogger.error(this,"[sceltaRuoloPEP::service] Rilevata eccezione SolmrException nella chiamata a papua findRuoliForPersonaInApplicazione");
      SolmrLogger.dumpStackTrace(this,"[sceltaRuoloPEP::service] Dumping stack trace\n",ex);
      //messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
      throw new SolmrException(ex.getMessage());
    }
    catch(RuntimeException ruExc)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione RuntimeException nella chiamata a papua findRuoliForPersonaInApplicazione");
      SolmrLogger.dumpStackTrace(this,"[sceltaRuoloPEP::service] Dumping stack trace\n",ruExc);
      //messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
      throw new SolmrException(ruExc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,"[sceltaRuoloPEP::service] Rilevata eccezione Exception nella chiamata a papua findRuoliForPersonaInApplicazione");
      SolmrLogger.dumpStackTrace(this,"[sceltaRuoloPEP::service] Dumping stack trace\n",ex);
      //messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
      throw new SolmrException(ex.getMessage());
    }

   

    if(arrRuoli.length==0)
    {
      SolmrLogger.error(this,"  -- CASO arrRuoli.length==0"); 	
      messaggioErrore = AgriConstants.UTENTE_NON_ABILITATO_PROCEDIMENTO;
      throw new SolmrException(messaggioErrore);
    }

    // Se per la stessa login/pwd esistono più ruoli associati...
    if(arrRuoli.length > 1) 
    {
      SolmrLogger.error(this,"  -- CASO arrRuoli.length > 1");	
      for(int i=0;i<arrRuoli.length;i++)
      {
        htmpl.newBlock("blkRuolo");
        htmpl.set("blkRuolo.ruolo",arrRuoli[i].getCodice());
        htmpl.set("blkRuolo.descRuolo",arrRuoli[i].getDescrizione());
        if(request.getParameter("ruolo") != null && arrRuoli[i].getCodice().equals(request.getParameter("ruolo"))) {
          htmpl.set("blkRuolo.checked","checked", null);
        }
      }
    }

    
    //DoubleStringcodeDescription strCode = null;

    if( (arrRuoli.length == 1) || (request.getParameter("conferma")!=null)
        || (request.getParameter("funzione")!=null && request.getParameter("funzione").equalsIgnoreCase("conferma")))
    {
      SolmrLogger.error(this,"  -- CASO arrRuoli.length == 1");

      //ParseXmlIride2 parseXmlIride2 = new ParseXmlIride2();

      if((arrRuoli.length == 1) || request.getParameter("ruolo")!=null)
      {
        String ruoloString = request.getParameter("ruolo");

        if(arrRuoli.length == 1)
        {
          ruoloString = arrRuoli[0].getCodice();
        }

        
        String codiceFiscale = id.getCodFiscale();
        String denominazione = id.getCognome() + " " + id.getNome();

        
        

        //session.setAttribute("codiceFiscale", codiceFiscale);
        SolmrLogger.error(this,"  - codiceRuolo ="+ruoloString);
        session.setAttribute("codiceRuolo", ruoloString);
        //session.setAttribute("codiceEnte", codiceEnte);
        //session.setAttribute("idProcedimento", idProcedimento);
        //session.setAttribute("denominazione", denominazione);
        //session.setAttribute("ruoloSelezionato", ruoloString);
        //session.setAttribute("htIride2", htIride2);

        SolmrLogger.error(this,"  -- response.sendRedirect "+APPLICATIVO_URL);
        response.sendRedirect(APPLICATIVO_URL);
        return;
      }
    }
    
    htmpl.newBlock("blkPulsante");

  }
  catch(SolmrException ex)
  {
    //SolmrLogger.debug(this,"URL_ACCESS_POINT = "+URL_ACCESS_POINT);
    SolmrLogger.error(this,"[sceltaRuoloPEP::service] Rilevata eccezione SolmrException");
    SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
    
    messaggioErrore = erroreGenerico;
  }
  catch(Exception ex)
  {
    SolmrLogger.error(this,"[sceltaRuoloPEP::service] Rilevata eccezione Exception codice #14");
    SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
    
    messaggioErrore = erroreGenerico;
  }
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  //050607 - Gestione messaggi errore - End
  SolmrLogger.debug(this,"[sceltaRuoloPEP:service] END.");
%>
<%!
  /*private Hashtable leggiXml(String src, String roleSelected)
  {
    java.util.Hashtable htRole = new Hashtable();
    ParseXmlIrideServices parseXmlIrideServices = new ParseXmlIrideServices();
    try 
    {
      htRole = parseXmlIrideServices.creaDocumentDaStringXML(src, roleSelected);
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione Exception codice #16");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
    }
    return htRole;
  }

  private void setDescRuoli(DoubleStringcodeDescription code) 
  {
    String codiceRuolo = code.getFirstCode();
    
    code.setFirstDescription(PortaleUtils.stampaDescRuoli(codiceRuolo));
  }


  private Hashtable getDirittoAccesso(HttpServletRequest request, HashMap ruoliH, String ruoloString)
      throws SolmrException, RemoteException, Exception
  {
    HttpSession session = (HttpSession) request.getSession(false);

    String appName = it.csi.solmr.etc.SolmrConstants.APP_NAME_IRIDE2_SMRGAA;
    Application myApp = (Application) session.getAttribute("myApp");

    String messaggioErrore = null;
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

    it.csi.iride2.policy.entity.Identita id = null;
    if(session.getAttribute(SolmrConstants.IDENTITA)!=null){
      id=(it.csi.iride2.policy.entity.Identita)session.getAttribute(SolmrConstants.IDENTITA);
    }

    //it.csi.iride2.iridefed.entity.Ruolo ruolo =
    it.csi.iride2.iridefed.entity.Ruolo ruolo =
       (it.csi.iride2.iridefed.entity.Ruolo) ruoliH.get(ruoloString);


    //Modifica gestione portale - 050607 - End
    String xmlSchemaUser = null;
    try 
    {
      //Utenti registrati su Sistema Piemonte non possiedono info-persona associato
      //Utenti Legali rappresentanti non possiedono info-persona associato   
      if(!ruolo.toString().equalsIgnoreCase(AgriConstants.TITOLARE_CF)
             &&
             !ruolo.toString().equalsIgnoreCase(AgriConstants.LEGALE_RAPPRESENTANTE)
             &&
             !ruolo.toString().equalsIgnoreCase(AgriConstants.MONITORAGGIO)
             )   
      {

        //050614 Modifica recupero info-persona - Begin
        String cu = "ACCESSO_SISTEMA";
        UseCase useCase = new it.csi.iride2.policy.entity.UseCase(myApp, cu);
        //050614 Modifica recupero info-persona - End

        xmlSchemaUser = anagFacadeClient.getInfoPersonaInUseCase(id, useCase);
      }
      else
      {
        if(ruolo.toString().equalsIgnoreCase((String)AgriConstants.get("TITOLARE_CF"))){
          xmlSchemaUser = AgriConstants.INFO_PERSONA_TITOLARE_CF;
        }
        if(ruolo.toString().equalsIgnoreCase((String)AgriConstants.get("LEGALE_RAPPRESENTANTE"))){
          xmlSchemaUser = AgriConstants.INFO_PERSONA_LEGALE_RAPPRESENTANTE;
        }
        if(ruolo.toString().equalsIgnoreCase((String)AgriConstants.get("MONITORAGGIO"))){
          xmlSchemaUser = AgriConstants.INFO_PERSONA_MONITORAGGIO;
        }
      }
    }
    catch (IdentitaNonAutenticaException ex)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione IdentitaNonAutenticaException codice #7");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
      messaggioErrore = AgriConstants.UTENTE_NON_ABILITATO_IDENTITA_PROVIDER;
      throw new SolmrException(messaggioErrore);
    }
    catch (InternalException ex)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione InternalException codice #8");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
      messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
      throw new SolmrException(messaggioErrore);
    }
    catch (NoSuchUseCaseException ex)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione NoSuchUseCaseException codice #9");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
      messaggioErrore = AgriConstants.UTENTE_NON_ABILITATO_CASO_D_USO_NON_ESISTENTE;
      throw new SolmrException(messaggioErrore);
    }
    catch (NoSuchApplicationException ex)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione NoSuchApplicationException codice #10");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ex);
      messaggioErrore = AgriConstants.UTENTE_NON_ABILITATO_APPLICAZIONE_NON_ESISTENTE;
      throw new SolmrException(messaggioErrore);
    }
    catch(RuntimeException ruExc)
    {
      SolmrLogger.error(this,"[LoginPEP::service] Rilevata eccezione RuntimeException codice #11");
      SolmrLogger.dumpStackTrace(this,"[LoginPEP::service] Dumping stack trace\n",ruExc);
      messaggioErrore = AgriConstants.PROBLEMI_ACCESSO_IRIDE2;
      throw new SolmrException(messaggioErrore);
    }

    SolmrLogger.debug(this, "xmlSchemaUser: "+xmlSchemaUser);
    Hashtable htIride2 = leggiXml(xmlSchemaUser, ruoloString);
    SolmrLogger.debug(this, "htIride2: "+htIride2);
    //String dirittoAccesso = (String) htIride2.get(appName);

    return htIride2;
  }*/

%>
<%= htmpl.text()%>

