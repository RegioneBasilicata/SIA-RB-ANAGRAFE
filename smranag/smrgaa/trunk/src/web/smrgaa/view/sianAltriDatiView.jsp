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
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>






<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/sianAltriDati.htm");
  Htmpl htmpl = new Htmpl(layout);
%>
   <%@include file = "/view/remoteInclude.inc" %>
<%

	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");	
	SianFascicoloResponseVO sianFascicoloResponseVO = (SianFascicoloResponseVO)session.getAttribute("sianFascicoloResponseVO");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	ManodoperaVO manodoperaVO = (ManodoperaVO)request.getAttribute("manodoperaVO");
	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	boolean isNumIscrInpsChecked = request.getParameter("chkNumIscrInps") != null;
  boolean isTipoIscrInpsChecked = request.getParameter("chkTipoIscrInps") != null;
  boolean isDatInInpsChecked = request.getParameter("chkDatInInps") != null;
  boolean isDatFinInpsChecked = request.getParameter("chkDatFinInps") != null;
  boolean isNumIscrRIChecked = request.getParameter("chkNumIscrRI") != null;
  boolean isDatInRIChecked = request.getParameter("chkDatInRI") != null;
  boolean isDatFinRIChecked = request.getParameter("chkDatFinRI")!= null;
  boolean isNumIscrREAChecked = request.getParameter("chkNumIscrREA") != null;
  boolean isDatInREAChecked = request.getParameter("chkDatInREA") != null;
  boolean isDatFinREAChecked = request.getParameter("chkDatFinREA")!= null;
  
  
  if(isNumIscrInpsChecked)
    htmpl.set("blkImpNumIscrInps.checkedNumIscrInps", "checked=\"checked\"", null);
    
  if(isTipoIscrInpsChecked)
    htmpl.set("blkImpTipoIscrInps.checkedTipoIscrInps", "checked=\"checked\"", null);  
    
  if(isDatInInpsChecked)
    htmpl.set("blkImpDatInInps.checkedDatInInps", "checked=\"checked\"", null);
    
  if(isDatFinInpsChecked)
    htmpl.set("blkImpDatFinInps.checkedDatFinInps", "checked=\"checked\"", null); 
    
  if(isNumIscrRIChecked)
    htmpl.set("blkImpNumIscrRI.checkedNumIscrRI", "checked=\"checked\"", null);
    
  if(isDatInRIChecked)
    htmpl.set("blkImpDatInRI.checkedDatInRI", "checked=\"checked\"", null);
    
  if(isDatFinRIChecked)
    htmpl.set("blkImpDatFinRI.checkedDatFinRI", "checked=\"checked\"", null);
    
  if(isNumIscrREAChecked)
    htmpl.set("blkImpNumIscrREA.checkedNumIscrREA", "checked=\"checked\"", null);
    
  if(isDatInREAChecked)
    htmpl.set("blkImpDatInREA.checkedDatInREA", "checked=\"checked\"", null);
    
  if(isDatFinREAChecked)
    htmpl.set("blkImpDatFinREA.checkedDatFinREA", "checked=\"checked\"", null);          

	
	
  if(sianFascicoloResponseVO != null 
    && Validator.isNotEmpty(sianFascicoloResponseVO.getSegnalazione())) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", (String)AnagErrors.get("ERR_SERVIZIO_SIAN_ERRATO")+sianFascicoloResponseVO.getSegnalazione());
  }
   
  //Dati presenti su anagrafe
  if(Validator.isNotEmpty(manodoperaVO))
  {
    htmpl.set("numIscrInpsAnagrafe", manodoperaVO.getCodiceInps());
    htmpl.set("tipoIscrInpsAnagrafe", manodoperaVO.getDescTipoIscrizioneINPS());
    htmpl.set("datInInpsAnagrafe", DateUtils.formatDateNotNull(manodoperaVO.getDataInizioIscrizioneDate()));
    htmpl.set("datFinInpsAnagrafe", DateUtils.formatDateNotNull(manodoperaVO.getDataCessazioneIscrizioneDate())); 
  }
  
  htmpl.set("numIscrRIAnagrafe", anagAziendaVO.getCCIAAnumRegImprese());
  htmpl.set("datInRIAnagrafe", DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRi()));
  htmpl.set("datFinRIAnagrafe", DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRi()));
   
  if(Validator.isNotEmpty(anagAziendaVO.getCCIAAnumeroREA()))
    htmpl.set("numIscrREAAnagrafe", ""+anagAziendaVO.getCCIAAnumeroREA());
  htmpl.set("datInREAAnagrafe", DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRea()));
  htmpl.set("datFinREAAnagrafe", DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRea()));
   
  if(sianFascicoloResponseVO != null)
  {   
    //Dati presenti sul sian
    SianTrovaFascicoloVO sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
    if(sianTrovaFascicoloVO != null) 
    {
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneInps()))
        htmpl.set("numIscrInpsTributaria", sianTrovaFascicoloVO.getNumeroIscrizioneInps());
        
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps()))
      {
        htmpl.set("tipoIscrInpsTributaria", sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps());
        //usato il campi con nome che nn c'entra una fava 
        //per evitare di mettere in sessione un nuovo oggetto!!
        htmpl.set("idTipoIscrInpsTributaria", sianTrovaFascicoloVO.getDetentore());
      }
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneInps()))
      {
        htmpl.set("datInInpsTributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataInizioIscrizioneInps()));
      } 
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneInps()))
      {
        htmpl.set("datFinInpsTributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataFineIscrizioneInps()));
      }
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneRI()))
      {
        htmpl.set("numIscrRITributaria", sianTrovaFascicoloVO.getNumeroIscrizioneRI());
      }
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneRI()))
      {
        htmpl.set("datInRITributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataInizioIscrizioneRI()));
      } 
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneRI()))
      {
        htmpl.set("datFinRITributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataFineIscrizioneRI()));
      }  
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneREA()))
      {
        htmpl.set("numIscrREATributaria", sianTrovaFascicoloVO.getNumeroIscrizioneREA());
      }
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneREA()))
      {
        htmpl.set("datInREATributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataInizioIscrizioneREA()));
      } 
      
      if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneREA()))
      {
        htmpl.set("datFinREATributaria",StringUtils.
            parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy",sianTrovaFascicoloVO.getDataFineIscrizioneREA()));
      }
      
      // Visualizzo a video le differenze tra i dati in possesso
      // dell'anagrafe tributaria e quelli dell'anagrafe regionale
      loadTributiDifferences(htmpl, anagAziendaVO, sianTrovaFascicoloVO, manodoperaVO);
      
      HtmplUtil.setErrors(htmpl, errors, request, application);    
    }
    else
    {
      messaggioErrore = AnagErrors.ERRORE_SERVIZIO_SIAN_NO_DATI;
    }


  }  
    
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggio", messaggioErrore);
  }

%>
<%!
  // Inserisco il metodo qui perchè non posso utilizzare il VO
  // ISATDett, usato per import di dati, e non credo sia
  // giusto inserire il metodo in una classe di utility dal momento che
  // è specifico solo del caso d'uso in questione.
  private void loadTributiDifferences(Htmpl htmpl, AnagAziendaVO anagAziendaVO,
                                      SianTrovaFascicoloVO sianTrovaFascicoloVO, ManodoperaVO manodoperaVO) 
  {
    String colorUguali = "nero";
    String colorDiversi = "rosso";
    String sesso = null;
    String dataNascita = null;
    // numero iscrizone inps
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneInps()))
    {
    	if(Validator.isNotEmpty(manodoperaVO) && sianTrovaFascicoloVO.getNumeroIscrizioneInps()
	        .equalsIgnoreCase(manodoperaVO.getCodiceInps())) 
	    {
	      htmpl.set("numIscrInpsTributariaColor", colorUguali);
	    }
	    else 
	    {
	      htmpl.set("numIscrInpsTributariaColor", colorDiversi);
	    }
	  }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps()))
    {
      if(Validator.isNotEmpty(manodoperaVO) && sianTrovaFascicoloVO.getCodiceTipoIscrizioneInps()
        .equalsIgnoreCase(manodoperaVO.getDescTipoIscrizioneINPS())) 
      {
        htmpl.set("tipoIscrInpsTributariaColor", colorUguali);
      }
      else 
      {
        htmpl.set("tipoIscrInpsTributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneInps()))
    {    
      String datInInpsTributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataInizioIscrizioneInps());
      if(Validator.isNotEmpty(manodoperaVO) 
        && datInInpsTributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(manodoperaVO.getDataInizioIscrizioneDate()))) 
      {
        htmpl.set("datInInpsTributariaColor", colorUguali);
      }
      else {
        htmpl.set("datInInpsTributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneInps()))
    {
      String datFinInpsTributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataFineIscrizioneInps());
    
      if(Validator.isNotEmpty(manodoperaVO) 
        && datFinInpsTributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(manodoperaVO.getDataCessazioneIscrizioneDate()))) 
      {
        htmpl.set("datFinInpsTributariaColor", colorUguali);
      }
      else 
      {
        htmpl.set("datFinInpsTributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneRI()))
    {
      if(sianTrovaFascicoloVO.getNumeroIscrizioneRI()
        .equalsIgnoreCase(anagAziendaVO.getCCIAAnumRegImprese())) 
      {
        htmpl.set("numIscrRITributariaColor", colorUguali);
      }
      else {
        htmpl.set("numIscrRITributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneRI()))
    {
      String datInRITributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataInizioIscrizioneRI());
    
      if(datInRITributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRi()))) 
      {
        htmpl.set("datInRITributariaColor", colorUguali);
      }
      else {
        htmpl.set("datInRITributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneRI()))
    {
      String datFinRITributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataFineIscrizioneRI());
    
      if(datFinRITributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRi()))) 
      {
        htmpl.set("datFinRITributariaColor", colorUguali);
      }
      else {
        htmpl.set("datFinRITributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getNumeroIscrizioneREA()))
    {
      if(Validator.isNotEmpty(anagAziendaVO.getCCIAAnumeroREA()))
      {
        if(sianTrovaFascicoloVO.getNumeroIscrizioneREA()
          .equalsIgnoreCase(anagAziendaVO.getCCIAAnumeroREA().toString())) 
	      {
	        htmpl.set("numIscrREATributariaColor", colorUguali);
	      }
	      else {
	        htmpl.set("numIscrREATributariaColor", colorDiversi);
	      }
      }
      else
      {
        htmpl.set("numIscrREATributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataInizioIscrizioneREA()))
    {
      String datInREATributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataInizioIscrizioneREA());
     
      if(datInREATributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataIscrizioneRea()))) 
      {
        htmpl.set("datInREATributariaColor", colorUguali);
      }
      else {
        htmpl.set("datInREATributariaColor", colorDiversi);
      }
    }
    
    if(Validator.isNotEmpty(sianTrovaFascicoloVO.getDataFineIscrizioneREA()))
    {
      String datFinREATributariaStr = StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", 
        sianTrovaFascicoloVO.getDataFineIscrizioneREA());
        
      if(datFinREATributariaStr.equalsIgnoreCase(DateUtils.formatDateNotNull(anagAziendaVO.getDataCessazioneRea()))) 
      {
        htmpl.set("datFinREATributariaColor", colorUguali);
      }
      else {
        htmpl.set("datFinREATributariaColor", colorDiversi);
      }
    }
    
    
    
  }
%>
<%= htmpl.text()%>
