<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>





<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/sianAnagrafeTributaria.htm");
  Htmpl htmpl = new Htmpl(layout);
%>
   <%@include file = "/view/remoteInclude.inc" %>
<%

	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)request.getAttribute("personaFisicaVO");
	SianAnagTributariaGaaVO anagTrib = (SianAnagTributariaGaaVO)session.getAttribute("anagTrib");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec");

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	Boolean isCUAAChecked = (Boolean)request.getAttribute("isCUAAChecked");
	Boolean isPartitaIvaChecked = (Boolean)request.getAttribute("isPartitaIvaChecked");
	Boolean isDenominazioneChecked = (Boolean)request.getAttribute("isDenominazioneChecked");
	Boolean isAtecoPrincChecked = (Boolean)request.getAttribute("isAtecoPrincChecked");
	Boolean isAtecoSecChecked = (Boolean)request.getAttribute("isAtecoSecChecked");
	Boolean isSedelegIndirizzoChecked = (Boolean)request.getAttribute("isSedelegIndirizzoChecked");
	Boolean isCheckedComuneResidenza = (Boolean)request.getAttribute("isCheckedComuneResidenza");
	Boolean isCheckedIndirizzoResidenza = (Boolean)request.getAttribute("isCheckedIndirizzoResidenza");
	Boolean isCheckedCapResidenza = (Boolean)request.getAttribute("isCheckedCapResidenza");
	Boolean isCheckedComuneSedeLegale = (Boolean)request.getAttribute("isCheckedComuneSedeLegale");
	Boolean isCheckedSedeLegaleCAP = (Boolean)request.getAttribute("isCheckedSedeLegaleCAP");
	Boolean isCheckedComuneDomicilioFiscale = (Boolean)request.getAttribute("isCheckedComuneDomicilioFiscale");
	Boolean isCheckedDomicilioIndirizzo = (Boolean)request.getAttribute("isCheckedDomicilioIndirizzo");
	Boolean isCheckedDomicilioCAP = (Boolean)request.getAttribute("isCheckedDomicilioCAP");


	String istatNascita=(String)request.getAttribute("istatNascita");
	String istatResidenza=(String)request.getAttribute("istatResidenza");
	String istatSedeLegale=(String)request.getAttribute("istatSedeLegale");
	String istatDomicilioFiscale=(String)request.getAttribute("istatDomicilioFiscale");
	
	
	if (istatNascita==null)
	  istatNascita=request.getParameter("istatNascitaTributaria");
	if (istatResidenza==null)
	  istatResidenza=request.getParameter("istatResidenzaTributaria");
	if (istatSedeLegale==null)
	  istatSedeLegale=request.getParameter("istatSedeLegaleTributaria");
	if (istatDomicilioFiscale==null)
	  istatDomicilioFiscale=request.getParameter("istatDomicilioFiscaleTributaria");

	// Settaggio parametri hidden
	//HtmplUtil.setValues(htmpl, anagAziendaVO);
	//HtmplUtil.setValues(htmpl, profile);
	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	if(Validator.isNotEmpty(messaggioErrore)) 
	{
	  htmpl.newBlock("blkErrore");
	  htmpl.set("blkErrore.messaggio", messaggioErrore);
	}
  else 
  {
    if(anagTrib.getMessaggioErrore() != null) 
    {
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggio", (String)AnagErrors.get("ERR_SERVIZIO_SIAN_ERRATO")+anagTrib.getMessaggioErrore());
    }
    else 
    {
	    HtmplUtil.setErrors(htmpl, errors, request, application);
	    if(anagTrib != null) 
	    {
        // CUAA
        htmpl.set("blkGestioneSian.CUAATributaria", anagTrib.getCodiceFiscale());
        htmpl.set("blkGestioneSian.CUAAAnagrafe", anagAziendaVO.getCUAA());
        if(isCUAAChecked !=null)
          htmpl.set("blkGestioneSian.blkImportaCUAA.checkedCUAA", "checked=\"checked\"");
       
        // PARTITA IVA
        htmpl.set("blkGestioneSian.partitaIVATributaria", anagTrib.getPartitaIva());
        htmpl.set("blkGestioneSian.partitaIVAAnagrafe", anagAziendaVO.getPartitaIVA());
        if(isPartitaIvaChecked !=null) 
        {
          htmpl.set("blkGestioneSian.blkImportaRadio.checkedPartivaIVA", "checked=\"checked\"", null);
        }
        //ATECO 2007
        //htmpl.set("blkGestioneSian.codiceAteco", anagTrib.getCodiceAteco());
        if(isAtecoPrincChecked !=null) {
          htmpl.set("blkGestioneSian.blkImportaAtecoPrinc.checkedIdAtecoPrincipale", "checked=\"checked\"", null);
        }
        String atecoPrincTrib = "";
        if(Validator.isNotEmpty(anagTrib.getCodiceAteco()))
        {
          atecoPrincTrib += anagTrib.getCodiceAteco();
          if(Validator.isNotEmpty(anagTrib.getDescAttivitaAteco()))
          {
            atecoPrincTrib += " - " +anagTrib.getDescAttivitaAteco();
          } 
        }
        htmpl.set("blkGestioneSian.atecoPrincTrib", atecoPrincTrib);
        
        String atecoPrincAnag = "";
        if(Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO())
          && Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode()))
        {
          atecoPrincAnag += anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode();
          if(Validator.isNotEmpty(anagAziendaVO.getTipoAttivitaATECO().getDescription()))
          {
            atecoPrincAnag += " - " +anagAziendaVO.getTipoAttivitaATECO().getDescription();
          } 
        }
        htmpl.set("blkGestioneSian.atecoPrincAnag", atecoPrincAnag);
        
        //ATECO SECONDARIO
        if(isAtecoSecChecked !=null) {
          htmpl.set("blkGestioneSian.blkImportaAtecoSec.checkedIdAtecoSec", "checked=\"checked\"", null);
        }
        String atecoSecTrib = "";
        if(Validator.isNotEmpty(anagTrib.getvAtecoSecSian()))
        {          
          for(int i=0;i<anagTrib.getvAtecoSecSian().size();i++)
          {
            if( anagTrib.getvAtecoSecSian().get(i).getCodiceAteco() != null)
            {
              atecoSecTrib += anagTrib.getvAtecoSecSian().get(i).getCodiceAteco();
              if(Validator.isNotEmpty(anagTrib.getvAtecoSecSian().get(i).getDescAttivitaAteco()))
              {
                atecoSecTrib += " - "+anagTrib.getvAtecoSecSian().get(i).getDescAttivitaAteco();
              }
              atecoSecTrib += "<br>";
            }
          }
        }
        htmpl.set("blkGestioneSian.atecoSecTrib", atecoSecTrib, null);
        
        String atecoSecAnag = "";
        if(Validator.isNotEmpty(vAziendaAtecoSec))
        {          
          for(int i=0;i<vAziendaAtecoSec.size();i++)
          {
            atecoSecAnag += vAziendaAtecoSec.get(i).getCodAttivitaAteco();
            if(Validator.isNotEmpty(vAziendaAtecoSec.get(i).getDescAttivitaAteco()))
            {
              atecoSecAnag += " - "+vAziendaAtecoSec.get(i).getDescAttivitaAteco();
            }
            atecoSecAnag += "<br>";
          }
        }
        htmpl.set("blkGestioneSian.atecoSecAnag", atecoSecAnag, null);
        
        
        // DENOMINAZIONE
        if(isDenominazioneChecked !=null) {
          htmpl.set("blkGestioneSian.blkImportaDenominazione.checkedDenominazione", "checked=\"checked\"", null);
        }
        htmpl.set("blkGestioneSian.denominazioneTributaria", anagTrib.getDenominazione());
        htmpl.set("blkGestioneSian.denominazioneAnagrafe", anagAziendaVO.getDenominazione());
        
        
        
        

       // COMUNE SEDE LEGALE
       htmpl.set("blkGestioneSian.sedeLegaleComuneTributaria", anagTrib.getComuneSedeLegale());
       if(Validator.isNotEmpty(anagAziendaVO.getDescComune())) 
       {
         htmpl.set("blkGestioneSian.sedeLegaleComuneAnagrafe", anagAziendaVO.getDescComune());
       }
       else 
       {
         htmpl.set("blkGestioneSian.sedeLegaleComuneAnagrafe", anagAziendaVO.getSedelegEstero());
       }
       htmpl.set("blkGestioneSian.istatSedeLegaleTributaria", istatSedeLegale);
       if(isCheckedComuneSedeLegale !=null)
          htmpl.set("blkGestioneSian.blkImportaComuneSedeLegale.checkedComuneSedeLegale", "checked=\"checked\"", null);


       // PROVINCIA SEDE LEGALE
       if (anagTrib.getProvinciaSedeLegale()!=null)
         htmpl.set("blkGestioneSian.sedeLegaleProvTributaria", "("+anagTrib.getProvinciaSedeLegale()+")");
       if (anagAziendaVO.getSedelegProv()!=null)
         htmpl.set("blkGestioneSian.sedeLegaleProvAnagrafe", "("+anagAziendaVO.getSedelegProv()+")");
       // INDIRIZZO SEDE LEGALE
       if(isSedelegIndirizzoChecked !=null) {
         htmpl.set("blkGestioneSian.blkImportaRadio.checkedSedelegIndirizzo", "checked=\"checked\"", null);
       }
       htmpl.set("blkGestioneSian.sedeLegaleIndirizzoTributaria", anagTrib.getIndirizzoSedeLegale());
       htmpl.set("blkGestioneSian.sedeLegaleIndirizzoAnagrafe", anagAziendaVO.getSedelegIndirizzo());
       // CAP SEDE LEGALE
       htmpl.set("blkGestioneSian.sedeLegaleCAPTributaria", anagTrib.getCapSedeLegale());
       htmpl.set("blkGestioneSian.sedeLegaleCAPAnagrafe", anagAziendaVO.getSedelegCAP());
       if(isCheckedSedeLegaleCAP !=null) {
         htmpl.set("blkGestioneSian.blkImportaRadio.checkedSedeLegaleCAP", "checked=\"checked\"", null);
       }

       // COMUNE DOMICILIO FISCALE
       htmpl.set("blkGestioneSian.domicilioComuneTributaria", anagTrib.getComuneDomicilioFiscale());
       if(Validator.isNotEmpty(personaFisicaVO.getDomComune())) {
         htmpl.set("blkGestioneSian.domicilioComuneAnagrafe", personaFisicaVO.getDomComune());
       }
       else {
         htmpl.set("blkGestioneSian.domicilioComuneAnagrafe", personaFisicaVO.getDomicilioStatoEstero());
       }
       htmpl.set("blkGestioneSian.istatDomicilioFiscaleTributaria", istatDomicilioFiscale);
       if(isCheckedComuneDomicilioFiscale !=null)
          htmpl.set("blkGestioneSian.blkImportaComuneDomicilioFiscale.checkedComuneDomicilioFiscale", "checked=\"checked\"", null);


       // PROVINCIA DOMICILIO FISCALE
       if (anagTrib.getProvinciaDomicilioFiscale()!=null)
         htmpl.set("blkGestioneSian.domicilioProvTributaria", "("+anagTrib.getProvinciaDomicilioFiscale()+")");
       if (personaFisicaVO.getDomProvincia()!=null)
         htmpl.set("blkGestioneSian.domicilioProvAnagrafe", "("+personaFisicaVO.getDomProvincia()+")");

       // INDIRIZZO DOMICILIO FISCALE
       htmpl.set("blkGestioneSian.domicilioIndirizzoTributaria", anagTrib.getIndirizzoDomicilioFiscale());
       htmpl.set("blkGestioneSian.domicilioIndirizzoAnagrafe", personaFisicaVO.getDomIndirizzo());
       if(isCheckedDomicilioIndirizzo !=null)
         htmpl.set("blkGestioneSian.blkImportaRadio.checkedDomicilioIndirizzo", "checked=\"checked\"", null);


       // CAP DOMICILIO FISCALE
       htmpl.set("blkGestioneSian.domicilioCAPTributaria", anagTrib.getCapDomicilioFiscale());
       htmpl.set("blkGestioneSian.domicilioCAPAnagrafe", personaFisicaVO.getDomCAP());
       if(isCheckedDomicilioCAP !=null)
         htmpl.set("blkGestioneSian.blkImportaRadio.checkedDomicilioCAP", "checked=\"checked\"", null);



       if(anagTrib.getCodiceFiscale().length() == 11) 
       {
         // FORMA GIURIDICA
         if(Validator.isNotEmpty(anagTrib.getNaturaGiuridica())) {
           String formaGiuridica = StringUtils.replace(anagTrib.getNaturaGiuridica(), "\"", "'");
           htmpl.set("blkGestioneSian.blkBloccoNoIndividuale1.formaGiuridicaTributaria", formaGiuridica);
         }
         htmpl.set("blkGestioneSian.blkBloccoNoIndividuale1.formaGiuridicaAnagrafe", anagAziendaVO.getTipoFormaGiuridica().getDescription());


         // CODICE FISCALE RAPP. LEG.
         htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.codiceFiscaleRappresentanteTributaria", anagTrib.getCodiceFiscaleRappresentante());
         htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.codiceFiscaleRappresentanteAnagrafe", personaFisicaVO.getCodiceFiscale());

         // DENOMINAZIONE RAPP. LEG.
         htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.denominazioneRappresentanteTributaria", anagTrib.getDenominazioneRappresentante());
         
         // DATA DECESSO RAPPRESENTANTE LEGALE
         if(anagTrib.getDataDecessoRappresentante() != null)
         {
           htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.dataDecessoRappresentante", anagTrib.getDataDecessoRappresentante().substring(0,10));
         }
       }
       if(anagTrib.getCodiceFiscale().length() == 16) 
       {
         if(personaFisicaVO != null) 
         {
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegCognomeAnagrafe", personaFisicaVO.getCognome());
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegNomeAnagrafe", personaFisicaVO.getNome());
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegSessoAnagrafe", personaFisicaVO.getSesso());
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegDataNascita", DateUtils.formatDate(personaFisicaVO.getNascitaData()));
           if(Validator.isNotEmpty(personaFisicaVO.getDescNascitaComune())) {
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegComuneAnagrafe", personaFisicaVO.getDescNascitaComune());
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegSiglaProvAnagrafe", "("+personaFisicaVO.getNascitaProv()+")");
           }
           else {
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegComuneAnagrafe", personaFisicaVO.getNascitaStatoEstero());
           }
           if(Validator.isNotEmpty(personaFisicaVO.getDescResComune())) {
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegResidenzaComuneAnagrafe", personaFisicaVO.getDescResComune());
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegResidenzaSiglaProvAnagrafe", "("+personaFisicaVO.getResProvincia()+")");
           }
           else {
             htmpl.set("blkGestioneSian.blkIndividuale.rapLegResidenzaComuneAnagrafe", personaFisicaVO.getDescStatoEsteroResidenza());
           }
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegResidenzaIndirizzoAnagrafe", personaFisicaVO.getResIndirizzo());
           htmpl.set("blkGestioneSian.blkIndividuale.rapLegResidenzaCAPAnagrafe", personaFisicaVO.getResCAP());
         }
         // COGNOME
         htmpl.set("blkGestioneSian.blkIndividuale.cognomeTributaria", anagTrib.getCognome());
         // NOME
         htmpl.set("blkGestioneSian.blkIndividuale.nomeTributaria", anagTrib.getNome());
         //SESSO
         htmpl.set("blkGestioneSian.blkIndividuale.sessoTributaria", anagTrib.getSesso());
         // DATA DI NASCITA
         if(Validator.isNotEmpty(anagTrib.getDataNascita())) {
           htmpl.set("blkGestioneSian.blkIndividuale.dataNascitaTributaria", anagTrib.getDataNascita().substring(0,10));
         }
         // DATA DI DECESSO
         if(Validator.isNotEmpty(anagTrib.getDataDecesso())) 
         {
           htmpl.set("blkGestioneSian.blkIndividuale.dataDecesso", anagTrib.getDataDecesso().substring(0,10));
         }
         // COMUNE(SIGLA PROVINCIA) DI NASCITA
         htmpl.set("blkGestioneSian.blkIndividuale.comuneNascitaTributaria", anagTrib.getComuneNascita());
         if(Validator.isNotEmpty(anagTrib.getProvinciaNascita())) {
           htmpl.set("blkGestioneSian.blkIndividuale.provNascitaTributaria", "("+anagTrib.getProvinciaNascita()+")");
         }
         htmpl.set("blkGestioneSian.blkIndividuale.istatNascitaTributaria", istatNascita);
         // COMUNE(SIGLA PROVINCIA) DI RESIDENZA
         if(isCheckedComuneResidenza !=null)
           htmpl.set("blkGestioneSian.blkIndividuale.blkImportaComuneResidenza.checkedComuneResidenza", "checked=\"checked\"", null);
         htmpl.set("blkGestioneSian.blkIndividuale.comuneResidenzaTributaria", anagTrib.getComuneResidenza());
         if(Validator.isNotEmpty(anagTrib.getProvinciaResidenza())) {
           htmpl.set("blkGestioneSian.blkIndividuale.provResidenzaTributaria", "("+anagTrib.getProvinciaResidenza()+")");
         }
         htmpl.set("blkGestioneSian.blkIndividuale.istatResidenzaTributaria", istatResidenza);
         // INDIRIZZO DI RESIDENZA
         if(isCheckedIndirizzoResidenza !=null)
           htmpl.set("blkGestioneSian.blkIndividuale.blkImportaRadio.checkedIndirizzoResidenza", "checked=\"checked\"", null);
         htmpl.set("blkGestioneSian.blkIndividuale.indirizzoResidenzaTributaria", anagTrib.getIndirizzoResidenza());
         // CAP DI RESIDENZA
         if(isCheckedCapResidenza !=null)
           htmpl.set("blkGestioneSian.blkIndividuale.blkImportaRadio.checkedCapResidenza", "checked=\"checked\"", null);
         htmpl.set("blkGestioneSian.blkIndividuale.capResidenzaTributaria", anagTrib.getCapResidenza());
       }


       // Visualizzo a video le differenze tra i dati in possesso
       // dell'anagrafe tributaria e quelli dell'anagrafe regionale
       loadTributiDifferences(htmpl, anagAziendaVO, anagTrib, personaFisicaVO, vAziendaAtecoSec);
     }
   }
}
%>
<%!
  // Inserisco il metodo qui perchè non posso utilizzare il VO
  // ISATDett, usato per import di dati, e non credo sia
  // giusto inserire il metodo in una classe di utility dal momento che
  // è specifico solo del caso d'uso in questione.
  private void loadTributiDifferences(Htmpl htmpl, AnagAziendaVO anagAziendaVO,
                                      SianAnagTributariaGaaVO anagTrib,
                                      PersonaFisicaVO personaFisicaVO,
                                      Vector<AziendaAtecoSecVO> vAziendaAtecoSec) {
    String colorUguali = "nero";
    String colorDiversi = "rosso";
    String sesso = null;
    String dataNascita = null;
    // CUAA
    if(StringUtils.eliminaSpazi(anagTrib.getCodiceFiscale()).equalsIgnoreCase(anagAziendaVO.getCUAA())) {
      htmpl.set("blkGestioneSian.CUAATributariaColor", colorUguali);
    }
    else {
      htmpl.set("blkGestioneSian.CUAATributariaColor", colorDiversi);
    }
    // PARTITA IVA
    if(Validator.isNotEmpty(anagTrib.getPartitaIva())) {
      if(anagTrib.getPartitaIva().equalsIgnoreCase(anagAziendaVO.getPartitaIVA())) {
        htmpl.set("blkGestioneSian.partitaIVATributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.partitaIVATributariaColor", colorDiversi);
      }
    }
    // DENOMINAZIONE
    if(StringUtils.eliminaSpazi(anagTrib.getDenominazione()).equalsIgnoreCase(StringUtils.eliminaSpazi(anagAziendaVO.getDenominazione()))) {
      htmpl.set("blkGestioneSian.denominazioneTributariaColor", colorUguali);
    }
    else {
      htmpl.set("blkGestioneSian.denominazioneTributariaColor", colorDiversi);
    }
    
    
    if((anagTrib.getIdAttivitaAteco() != null)
      && ((anagAziendaVO.getTipoAttivitaATECO() == null)
         || ((anagAziendaVO.getTipoAttivitaATECO() != null) && (anagAziendaVO.getTipoAttivitaATECO().getCode() == null))))
    {
      htmpl.set("blkGestioneSian.atecoPrincTributariaColor", colorDiversi);
    }
    else if((anagTrib.getIdAttivitaAteco() != null)
      && (anagAziendaVO.getTipoAttivitaATECO() != null) 
      && (anagAziendaVO.getTipoAttivitaATECO().getCode() != null))
    {      
      if(anagTrib.getIdAttivitaAteco().intValue() != anagAziendaVO.getTipoAttivitaATECO().getCode().intValue())
      {
        htmpl.set("blkGestioneSian.atecoPrincTributariaColor", colorDiversi);
      }
      else
      {
        htmpl.set("blkGestioneSian.atecoPrincTributariaColor", colorUguali);
      }
    }
    
    
    if((anagTrib.getvAtecoSecSian() != null)
      && (vAziendaAtecoSec == null))
    {
      htmpl.set("blkGestioneSian.atecoSecTributariaColor", colorDiversi);
    }
    else if((anagTrib.getvAtecoSecSian() != null)
      && (vAziendaAtecoSec != null))
    {
      if(anagTrib.getvAtecoSecSian().size() != vAziendaAtecoSec.size())
      {
        htmpl.set("blkGestioneSian.atecoSecTributariaColor", colorDiversi);
      }
      else
      {
        Vector<Long> vIdAtecoTrib = new Vector<Long>();
        for(int i=0;i<anagTrib.getvAtecoSecSian().size();i++)
        {
          vIdAtecoTrib.add(anagTrib.getvAtecoSecSian().get(i).getIdAttivitaAteco());
        }
        
        boolean trovato = true;
        for(int i=0;i<vAziendaAtecoSec.size();i++)
        {
          if(!vIdAtecoTrib.contains(new Long(vAziendaAtecoSec.get(i).getIdAttivitaAteco())))
          {
            trovato = false;
            break;
          }
        }
        
        if(trovato)         
          htmpl.set("blkGestioneSian.atecoSecTributariaColor", colorUguali);
        else
          htmpl.set("blkGestioneSian.atecoSecTributariaColor", colorDiversi);
      }
    }
    
    
    // COMUNE SEDE LEGALE O STATO ESTERO
      if(Validator.isNotEmpty(anagAziendaVO.getDescComune())) {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneSedeLegale()).equalsIgnoreCase(StringUtils.eliminaSpazi(anagAziendaVO.getDescComune()))) {
          htmpl.set("blkGestioneSian.sedeLegaleComuneTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.sedeLegaleComuneTributariaColor", colorDiversi);
        }
      }
      else {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneSedeLegale()).equalsIgnoreCase(StringUtils.eliminaSpazi(anagAziendaVO.getSedelegEstero()))) {
          htmpl.set("blkGestioneSian.sedeLegaleComuneTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.sedeLegaleComuneTributariaColor", colorDiversi);
        }
      }
      // INDIRIZZO SEDE LEGALE
      if(StringUtils.eliminaSpazi(anagTrib.getIndirizzoSedeLegale()).equalsIgnoreCase(StringUtils.eliminaSpazi(anagAziendaVO.getSedelegIndirizzo()))) {
        htmpl.set("blkGestioneSian.sedeLegaleIndirizzoTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.sedeLegaleIndirizzoTributariaColor", colorDiversi);
      }
      // CAP SEDE LEGALE
      if(StringUtils.eliminaSpazi(anagTrib.getCapSedeLegale()).equalsIgnoreCase(anagAziendaVO.getSedelegCAP())) {
        htmpl.set("blkGestioneSian.sedeLegaleCAPTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.sedeLegaleCAPTributariaColor", colorDiversi);
      }

      // COMUNE DOMICILIO FISCALE
      if(Validator.isNotEmpty(personaFisicaVO.getDomComune())) {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneDomicilioFiscale()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getDomComune()))) {
          htmpl.set("blkGestioneSian.domicilioComuneTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.domicilioComuneTributariaColor", colorDiversi);
        }
      }
      else {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneDomicilioFiscale()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getDomicilioStatoEstero()))) {
          htmpl.set("blkGestioneSian.domicilioComuneTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.domicilioComuneTributariaColor", colorDiversi);
        }
      }
      // INDIRIZZO DOMICILIO FISCALE
      if(StringUtils.eliminaSpazi(anagTrib.getIndirizzoDomicilioFiscale()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getDomIndirizzo()))) {
        htmpl.set("blkGestioneSian.domicilioIndirizzoTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.domicilioIndirizzoTributariaColor", colorDiversi);
      }
      // CAP DOMICILIO FISCALE
      if(StringUtils.eliminaSpazi(anagTrib.getCapDomicilioFiscale()).equalsIgnoreCase(personaFisicaVO.getDomCAP())) {
        htmpl.set("blkGestioneSian.domicilioCAPTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.domicilioCAPTributariaColor", colorDiversi);
      }

    // Casi di confronto con CUAA di 11: da differenziare a causa
    // del comportamento del SIAN che restituisce valori diversi
    // a seconda della forma giuridica dell'azienda
    if(anagTrib.getCodiceFiscale().length() == 11) {
      // CODICE FISCALE RAPP. LEG.
      if(StringUtils.eliminaSpazi(anagTrib.getCodiceFiscaleRappresentante()).equalsIgnoreCase(personaFisicaVO.getCodiceFiscale())) {
        htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.codiceFiscaleRappresentanteTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkBloccoNoIndividuale2.codiceFiscaleRappresentanteTributariaColor", colorDiversi);
      }
    }
    else if(anagTrib.getCodiceFiscale().length() == 16) {
      // COGNOME
      if(StringUtils.eliminaSpazi(anagTrib.getCognome()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getCognome()))) {
        htmpl.set("blkGestioneSian.blkIndividuale.cognomeTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkIndividuale.cognomeTributariaColor", colorDiversi);
      }
      // NOME
      if(StringUtils.eliminaSpazi(anagTrib.getNome()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getNome()))) {
        htmpl.set("blkGestioneSian.blkIndividuale.nomeTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkIndividuale.nomeTributariaColor", colorDiversi);
      }
      // SESSO
      if(StringUtils.eliminaSpazi(anagTrib.getSesso()).equalsIgnoreCase(personaFisicaVO.getSesso())) {
        htmpl.set("blkGestioneSian.blkIndividuale.sessoTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkIndividuale.sessoTributariaColor", colorDiversi);
      }
      // DATA DI NASCITA
      if(Validator.isNotEmpty(anagTrib.getDataNascita())) {
        if((anagTrib.getDataNascita().substring(0,10)).equalsIgnoreCase(DateUtils.formatDate(personaFisicaVO.getNascitaData()))) {
          htmpl.set("blkGestioneSian.blkIndividuale.dataNascitaTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.blkIndividuale.dataNascitaTributariaColor", colorDiversi);
        }
      }
      // COMUNE + PROVINCIA DI NASCITA O STATO ESTERO
      if(Validator.isNotEmpty(personaFisicaVO.getDescNascitaComune())) {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneNascita()).equalsIgnoreCase(personaFisicaVO.getDescNascitaComune()) &&
           StringUtils.eliminaSpazi(anagTrib.getProvinciaNascita()).equalsIgnoreCase(personaFisicaVO.getNascitaProv())) {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneNascitaTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneNascitaTributariaColor", colorDiversi);
        }
      }
      else {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneNascita()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getNascitaStatoEstero()))) {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneNascitaTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneNascitaTributariaColor", colorDiversi);
        }
      }
      // COMUNE + PROVINCIA DI RESIDENZA O STATO ESTERO
      if(Validator.isNotEmpty(personaFisicaVO.getDescResComune())) {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneResidenza()).equalsIgnoreCase(personaFisicaVO.getDescResComune()) &&
           StringUtils.eliminaSpazi(anagTrib.getProvinciaResidenza()).equalsIgnoreCase(personaFisicaVO.getResProvincia())) {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneResidenzaTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneResidenzaTributariaColor", colorDiversi);
        }
      }
      else {
        if(StringUtils.eliminaSpazi(anagTrib.getComuneResidenza()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getDescStatoEsteroResidenza()))) {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneResidenzaTributariaColor", colorUguali);
        }
        else {
          htmpl.set("blkGestioneSian.blkIndividuale.comuneResidenzaTributariaColor", colorDiversi);
        }
      }
      // INDIRIZZO DI RESIDENZA
      if(StringUtils.eliminaSpazi(anagTrib.getIndirizzoResidenza()).equalsIgnoreCase(StringUtils.eliminaSpazi(personaFisicaVO.getResIndirizzo()))) {
        htmpl.set("blkGestioneSian.blkIndividuale.indirizzoResidenzaTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkIndividuale.indirizzoResidenzaTributariaColor", colorDiversi);
      }
      // CAP DI RESIDENZA
      if(StringUtils.eliminaSpazi(anagTrib.getCapResidenza()).equalsIgnoreCase(personaFisicaVO.getResCAP())) {
        htmpl.set("blkGestioneSian.blkIndividuale.capResidenzaTributariaColor", colorUguali);
      }
      else {
        htmpl.set("blkGestioneSian.blkIndividuale.capResidenzaTributariaColor", colorDiversi);
      }
    }
  }
%>
<%= htmpl.text()%>
