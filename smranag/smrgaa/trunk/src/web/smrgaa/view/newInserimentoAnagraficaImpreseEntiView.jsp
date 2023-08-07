<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.dto.anag.IntermediarioAnagVO" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoAnagraficaImpreseEnti.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors"); 
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
  IntermediarioAnagVO intermediarioVO = (IntermediarioAnagVO)request.getAttribute("intermediarioVO");
  
  if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isNotEmpty(aziendaNuovaVO.getIdTipoRichiesta()))
  {
    htmpl.set("idTipoRichiesta", ""+aziendaNuovaVO.getIdTipoRichiesta());
  }
  else
  {
    htmpl.set("idTipoRichiesta", request.getParameter("idTipoRichiesta"));
  }
  
  
  String regimeInserimentoEnti = request.getParameter("regimeInserimentoEnti");
  String idMotivoRichiesta = request.getParameter("idMotivoRichiesta");
  Vector<MotivoRichiestaVO> vMotivoRichiesta = (Vector<MotivoRichiestaVO>)request.getAttribute("vMotivoRichiesta");
  for(int i=0;i<vMotivoRichiesta.size();i++)
  {
    htmpl.newBlock("tipoMotivoRichiesta");
    htmpl.set("tipoMotivoRichiesta.idMotivoRichiesta", ""+vMotivoRichiesta.get(i).getIdMotivoRichiesta());
    htmpl.set("tipoMotivoRichiesta.descrizione", vMotivoRichiesta.get(i).getDescrizione());
    if(Validator.isNotEmpty(regimeInserimentoEnti))
    {
      if(Validator.isNotEmpty(idMotivoRichiesta) 
        && (vMotivoRichiesta.get(i).getIdMotivoRichiesta().compareTo(new Integer(idMotivoRichiesta)) == 0))
      {
        htmpl.set("tipoMotivoRichiesta.selected","selected", null);
      }
    }
    //prima volta che entro
    else
    {
      if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isNotEmpty(aziendaNuovaVO.getIdMotivoRichiesta())
        && (aziendaNuovaVO.getIdMotivoRichiesta().compareTo(vMotivoRichiesta.get(i).getIdMotivoRichiesta()) == 0))
      {
        htmpl.set("tipoMotivoRichiesta.selected","selected", null);
      }
    }   
  }
  
  
  String codEnte = "";
  String denominazione = "";
  String cuaa = "";
  String partitaIva = "";
  String idTipoAzienda = "";
  String idTipoFormaGiuridica = "";
  String sedePec = "";
  String sedelegIndirizzo = "";
  String sedelegProv = "";
  String descComune = "";
  String sedelegComune = "";
  String sedelegCAP = "";
  String sedeMail = "";
  String sedeTelefono = "";
  String sedeFax = "";
  String unitaProduttiva = "";
  String codiceFiscaleRL = "";
  String cognome = "";
  String nome = "";
  String sesso = "";
  String nascitaData = "";
  String nascitaProv = "";
  String descNascitaComune = "";
  String codiceFiscaleComuneNascita = "";
  String nascitaComune = "";
  String nascitaStatoEstero = "";
  String istatStatoEsteroNascita = "";
  String cittaNascita = "";
  String resIndirizzo = "";
  String resProvincia = "";
  String descResComune = "";
  String resComune = "";
  String resCAP = "";
  String resTelefono = "";
  String resFax = "";
  String resMail = "";
  String note = "";
  String subentro = "";
  String cuaaSubentro = "";
  String denomSubentro = "";
  String idAziendaSubentro = "";
  
  //prima volta che entro
  if(request.getParameter("regimeInserimentoEnti") == null)
  {
    
	  if(request.getAttribute("aziendaAAEPTrovata") != null)
	  {  
	    Azienda aziendaAAEP =  (Azienda)request.getAttribute("aziendaAAEP");
	    
	    cuaa = aziendaAAEP.getCodiceFiscale().getValue();
      partitaIva = aziendaAAEP.getPartitaIva().getValue();
	    
	    String temp=AnagAAEPAziendaVO.eliminaSpazi(aziendaAAEP.getRagioneSociale().getValue()).toUpperCase();
      if (temp.length()>120) 
        temp=temp.substring(0,120);      
      denominazione = temp;
      
      if(request.getAttribute("idTipoFormaGiuridica") != null)
      {
        idTipoFormaGiuridica = ((Long)(request.getAttribute("idTipoFormaGiuridica"))).toString();
      }
      if(request.getAttribute("idTipoAzienda") != null)
      {
        idTipoAzienda = ((Long)(request.getAttribute("idTipoAzienda"))).toString();
      }
      
      if(request.getAttribute("sedePec") != null)
      {
        sedePec = (String)request.getAttribute("sedePec");
      }
      
      Sede sedeAAEP = (Sede)request.getAttribute("sedeAAEP");
      if(sedeAAEP != null)
      {
        // Recupero solo i primi 100 caratteri dell'indirizzo sede di AAEP
        // e faccio la trim per eliminare gli spazi
        if(Validator.isNotEmpty(sedeAAEP.getIndirizzoSede())) 
        {
          if(sedeAAEP.getIndirizzoSede().getValue().length() > 100) 
          {
            sedelegIndirizzo = sedeAAEP.getIndirizzoSede().getValue().substring(0, 100).trim();
          }
          else 
          {
            sedelegIndirizzo = sedeAAEP.getIndirizzoSede().getValue().trim();
          }
        }
        
        sedelegComune = sedeAAEP.getCodComune().getValue();
        ComuneVO comuneSede = (ComuneVO)request.getAttribute("comuneSede");
        if(Validator.isNotEmpty(comuneSede))
        {
          descComune = comuneSede.getDescom();
          sedelegProv = comuneSede.getSiglaProv();
          sedelegCAP = comuneSede.getCap();
        }
        
        
        
        
        sedeTelefono = sedeAAEP.getTelefono().getValue();
        sedeFax = sedeAAEP.getFax().getValue();
        sedeMail = sedeAAEP.getEmail().getValue();      
      }      
      
      //Vado a leggere i dati del titolare/rappresentante legale
      RappresentanteLegale rappresentanteLegaleAAEP=aziendaAAEP.getRappresentanteLegale().getValue();
      if(rappresentanteLegaleAAEP != null) 
	    {
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getCodiceFiscale().getValue())) 
	      {
	        codiceFiscaleRL = rappresentanteLegaleAAEP.getCodiceFiscale().getValue();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getCognome().getValue())) 
	      {
	        cognome = rappresentanteLegaleAAEP.getCognome().getValue();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getNome().getValue())) 
	      {
	        nome = rappresentanteLegaleAAEP.getNome().getValue();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getSesso().getValue())) 
	      {
	        sesso = rappresentanteLegaleAAEP.getSesso().getValue();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getDataNascita().getValue())) 
	      {
	        //personaFisicaVO.setNascitaData(DateUtils.parseDate(rappresentanteLegaleAAEP.getDataNascita()));
	        nascitaData = rappresentanteLegaleAAEP.getDataNascita().getValue();
	      }
	      ComuneVO comuneNascita = (ComuneVO)request.getAttribute("comuneNascita");
        if(Validator.isNotEmpty(comuneNascita))
        {
	        nascitaProv = comuneNascita.getSiglaProv();
					descNascitaComune = comuneNascita.getDescom();
					nascitaComune = comuneNascita.getIstatComune();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getIndirizzo())) 
	      {
	        resIndirizzo = rappresentanteLegaleAAEP.getIndirizzo().getValue();
	      }
	      ComuneVO comuneRes = (ComuneVO)request.getAttribute("comuneRes");
        if(Validator.isNotEmpty(comuneNascita))
        {
	        resProvincia = comuneRes.getSiglaProv();
          descResComune = comuneRes.getDescom();
          resComune = comuneRes.getIstatComune();
	      }
	      if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getCap())) 
	      {
	        resCAP = rappresentanteLegaleAAEP.getCap().getValue();
	      }
	      
	    }
      
      
      if(Validator.isNotEmpty(intermediarioVO))
      {
        codEnte = intermediarioVO.getCodiceFiscale();
        if(Validator.isEmpty(cuaa))
	        cuaa = intermediarioVO.getExtCuaa();
	      if(Validator.isEmpty(partitaIva))
	        partitaIva = intermediarioVO.getPartitaIva();
	      if(Validator.isEmpty(denominazione))
	        denominazione = intermediarioVO.getDenominazione();
	      if(Validator.isEmpty(sedelegIndirizzo))
	        sedelegIndirizzo = intermediarioVO.getIndirizzo();
	      if(Validator.isEmpty(descComune))
	        descComune = intermediarioVO.getDesCom();
	      if(Validator.isEmpty(sedelegProv))
	        sedelegProv = intermediarioVO.getSglProv();
	      if(Validator.isEmpty(sedelegComune))
	        sedelegComune = intermediarioVO.getComune();
	      if(Validator.isEmpty(sedelegCAP))      
	        sedelegCAP = intermediarioVO.getCap();
	      if(Validator.isEmpty(sedeMail))      
	        sedeMail = intermediarioVO.getEmail();
	      if(Validator.isEmpty(sedeTelefono))     
	        sedeTelefono = intermediarioVO.getTelefono();
	      if(Validator.isEmpty(sedeFax))      
	        sedeFax = intermediarioVO.getFax();
	      if(Validator.isEmpty(sedePec))      
	        sedePec = intermediarioVO.getPec();      
      }
      else
      {
        if(Validator.isNotEmpty(request.getParameter("cuaaCaaInserimento")))
        {
          codEnte = request.getParameter("cuaaCaaInserimento");
        }
      }
      
      
      
	  }
	  else if(request.getAttribute("anagTrib") != null)
	  {
	    SianAnagTributariaVO anagTrib = (SianAnagTributariaVO)request.getAttribute("anagTrib");
	    cuaa = anagTrib.getCodiceFiscale();
      partitaIva = anagTrib.getPartitaIva();
      idTipoAzienda = "1";
      
      String temp=AnagAAEPAziendaVO.eliminaSpazi(anagTrib.getDenominazione()).toUpperCase();
      if (temp.length()>120) 
        temp=temp.substring(0,120);
      denominazione = temp;
      if(Validator.isNotEmpty(anagTrib.getCodiceFiscaleRappresentante()))
      {
        codiceFiscaleRL = anagTrib.getCodiceFiscaleRappresentante();
      }
      if(Validator.isNotEmpty(anagTrib.getCognomeRappresentante()))
      {
        cognome = anagTrib.getCognomeRappresentante();
      }          
      if(Validator.isNotEmpty(anagTrib.getNomeRappresentante()))
      {
        nome = anagTrib.getNomeRappresentante();
      }
     
            
      // Recupero solo i primi 100 caratteri dell'indirizzo sede di AAEP
      // e faccio la trim per eliminare gli spazi
      if(Validator.isNotEmpty(anagTrib.getIndirizzoSedeLegale())) 
      {
        if(anagTrib.getIndirizzoSedeLegale().length() > 100) 
        {
          sedelegIndirizzo = anagTrib.getIndirizzoSedeLegale().substring(0, 100).trim();
        }
        else 
        {
          sedelegIndirizzo = anagTrib.getIndirizzoSedeLegale().trim();
        }
      }
          
      Vector<ComuneVO> comuniTribSede = (Vector<ComuneVO>)request.getAttribute("comuniTribSede");
      if(Validator.isNotEmpty(comuniTribSede)) 
      {
        ComuneVO comuneSede = null;
        if (comuniTribSede!=null && comuniTribSede.size()==1)
        { 
          comuneSede=(ComuneVO)comuniTribSede.get(0);
        }
        else
        {
          for(int i=0;i<comuniTribSede.size();i++)
          {
            if("N".equalsIgnoreCase(comuniTribSede.get(i).getFlagEstinto()))
            {
              comuneSede=(ComuneVO)comuniTribSede.get(i);
              break;
            }
          }
        }
            
        if (comuneSede!=null)
        {
          sedelegProv = comuneSede.getSiglaProv();
          descComune = comuneSede.getDescom();
          sedelegComune = comuneSede.getIstatComune();    
        }
      }
      
      if(Validator.isNotEmpty(anagTrib.getCapSedeLegale()))
      {
        sedelegCAP = anagTrib.getCapSedeLegale();
      }
      
      
      
      if(Validator.isNotEmpty(intermediarioVO))
      {
        codEnte = intermediarioVO.getCodiceFiscale();
        if(Validator.isEmpty(cuaa))
          cuaa = intermediarioVO.getExtCuaa();
        if(Validator.isEmpty(partitaIva))
          partitaIva = intermediarioVO.getPartitaIva();
        if(Validator.isEmpty(denominazione))
          denominazione = intermediarioVO.getDenominazione();
        if(Validator.isEmpty(sedelegIndirizzo))
          sedelegIndirizzo = intermediarioVO.getIndirizzo();
        if(Validator.isEmpty(descComune))
          descComune = intermediarioVO.getDesCom();
        if(Validator.isEmpty(sedelegProv))
          sedelegProv = intermediarioVO.getSglProv();
        if(Validator.isEmpty(sedelegComune))
          sedelegComune = intermediarioVO.getComune();
        if(Validator.isEmpty(sedelegCAP))      
          sedelegCAP = intermediarioVO.getCap();
        if(Validator.isEmpty(sedeMail))      
          sedeMail = intermediarioVO.getEmail();
        if(Validator.isEmpty(sedeTelefono))     
          sedeTelefono = intermediarioVO.getTelefono();
        if(Validator.isEmpty(sedeFax))      
          sedeFax = intermediarioVO.getFax();
        if(Validator.isEmpty(sedePec))      
          sedePec = intermediarioVO.getPec();     
      }
      else
      {
        if(Validator.isNotEmpty(request.getParameter("cuaaCaaInserimento")))
        {
          codEnte = request.getParameter("cuaaCaaInserimento");
        }
      }
	  
	  }
	  else if(intermediarioVO != null)
    {
      codEnte = intermediarioVO.getCodiceFiscale();
      cuaa = intermediarioVO.getExtCuaa();
      if(Validator.isEmpty(cuaa))
        cuaa = request.getParameter("partitaIvaCaaInserimento");
      partitaIva = intermediarioVO.getPartitaIva();
      denominazione = intermediarioVO.getDenominazione();
      sedelegIndirizzo = intermediarioVO.getIndirizzo();
      descComune = intermediarioVO.getDesCom();
      sedelegProv = intermediarioVO.getSglProv();
      sedelegComune = intermediarioVO.getComune();      
      sedelegCAP = intermediarioVO.getCap();
      sedeMail = intermediarioVO.getEmail();
      sedeTelefono = intermediarioVO.getTelefono();
      sedeFax = intermediarioVO.getFax();
      sedePec = intermediarioVO.getPec();
      
    }
	  else if(Validator.isNotEmpty(aziendaNuovaVO))
	  {
	    codEnte = aziendaNuovaVO.getCodEnte(); 
	    cuaa = aziendaNuovaVO.getCuaa();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getPartitaIva()))
	      partitaIva = aziendaNuovaVO.getPartitaIva();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getDenominazione()))
	      denominazione = aziendaNuovaVO.getDenominazione();
	    idTipoFormaGiuridica = aziendaNuovaVO.getIdFormaGiuridica().toString();
	    idTipoAzienda = aziendaNuovaVO.getIdTipologiaAzienda().toString();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getPec()))
	      sedePec = aziendaNuovaVO.getPec();
	    sedelegIndirizzo = aziendaNuovaVO.getSedelegIndirizzo();
      sedelegProv = aziendaNuovaVO.getSedelegProv();
	    descComune = aziendaNuovaVO.getDescComune();
	    sedelegComune = aziendaNuovaVO.getSedelegComune();
	    sedelegCAP = aziendaNuovaVO.getSedelegCap();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getTelefono()))
	      sedeTelefono = aziendaNuovaVO.getTelefono();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getFax()))
	      sedeFax = aziendaNuovaVO.getFax();
	    if(Validator.isNotEmpty(aziendaNuovaVO.getMail()))
	      sedeMail = aziendaNuovaVO.getMail();
	    codiceFiscaleRL = aziendaNuovaVO.getCodiceFiscale();
	    cognome = aziendaNuovaVO.getCognome();
	    nome = aziendaNuovaVO.getNome();
	    sesso = aziendaNuovaVO.getSesso();
	    nascitaData = DateUtils.formatDate(aziendaNuovaVO.getDataNascita());
	    if(Validator.isNotEmpty(aziendaNuovaVO.getCittaNascitaEstero()))
	    {
	      cittaNascita = aziendaNuovaVO.getCittaNascitaEstero();
	      nascitaStatoEstero = aziendaNuovaVO.getDescNascitaComune();
        istatStatoEsteroNascita = aziendaNuovaVO.getComuneNascita();
	    }
	    else
	    {  
        nascitaProv = aziendaNuovaVO.getNascitaProv();
        descNascitaComune = aziendaNuovaVO.getDescNascitaComune();
        nascitaComune = aziendaNuovaVO.getComuneNascita();
	    }
	    resIndirizzo = aziendaNuovaVO.getResIndirizzo();
	    resProvincia = aziendaNuovaVO.getResProvincia();
	    descResComune = aziendaNuovaVO.getDescResComune();
	    resComune = aziendaNuovaVO.getResComune();
	    resCAP = aziendaNuovaVO.getResCap();
		  unitaProduttiva = aziendaNuovaVO.getSedeLegUte();
		  if(Validator.isNotEmpty(aziendaNuovaVO.getTelefonoSoggetto()))
		    resTelefono = aziendaNuovaVO.getTelefonoSoggetto();
		  if(Validator.isNotEmpty(aziendaNuovaVO.getFaxSoggetto()))
		    resFax = aziendaNuovaVO.getFaxSoggetto();
		  if(Validator.isNotEmpty(aziendaNuovaVO.getMailSoggetto()))
		    resMail = aziendaNuovaVO.getMailSoggetto();
		  if(Validator.isNotEmpty(aziendaNuovaVO.getNote()))
		    note = aziendaNuovaVO.getNote();
		  if(Validator.isNotEmpty(aziendaNuovaVO.getIdAziendaSubentro()))
		  {
			  subentro = "S";
			  cuaaSubentro = aziendaNuovaVO.getCuaaSubentro();
			  denomSubentro = aziendaNuovaVO.getDenomSubentro();
			  idAziendaSubentro = aziendaNuovaVO.getIdAziendaSubentro().toString();
			}
	    
	    
	  }
	  else
	  {
	    if(Validator.isNotEmpty(request.getParameter("cuaaCaaInserimento")))
	    {
	      codEnte = request.getParameter("cuaaCaaInserimento");
	      cuaa = request.getParameter("partitaIvaCaaInserimento");
	    }
	    else
	    {  
	      cuaa = request.getParameter("partitaIvaInserimento");
	    }
	    idTipoAzienda = "1";
	  }
	  
	  
	}
	else
	{
	  codEnte = request.getParameter("codEnte");
	  cuaa = request.getParameter("cuaa");
    partitaIva = request.getParameter("partitaIva");
	  denominazione = request.getParameter("denominazione");
	  idTipoFormaGiuridica = request.getParameter("idTipoFormaGiuridica");
	  idTipoAzienda = request.getParameter("idTipoAzienda");
	  sedePec = request.getParameter("sedePec");
	  sedelegIndirizzo = request.getParameter("sedelegIndirizzo");
	  sedelegProv = request.getParameter("sedelegProv");
    descComune = request.getParameter("descComune");
    sedelegComune = request.getParameter("sedelegComune");
    sedelegCAP = request.getParameter("sedelegCAP");
    sedeTelefono = request.getParameter("sedeTelefono");
    sedeFax = request.getParameter("sedeFax");
    sedeMail = request.getParameter("sedeMail");
    codiceFiscaleRL = request.getParameter("codiceFiscaleRL");
    cognome = request.getParameter("cognome");
    nome = request.getParameter("nome");
    sesso = request.getParameter("sesso");
    nascitaData = request.getParameter("nascitaData");
    nascitaProv = request.getParameter("nascitaProv");
    descNascitaComune = request.getParameter("descNascitaComune");
    codiceFiscaleComuneNascita = request.getParameter("codiceFiscaleComuneNascita");
    nascitaComune = request.getParameter("nascitaComune");
    nascitaStatoEstero = request.getParameter("nascitaStatoEstero");
    istatStatoEsteroNascita = request.getParameter("istatStatoEsteroNascita");
    cittaNascita = request.getParameter("cittaNascita");
    resIndirizzo = request.getParameter("resIndirizzo");
    resProvincia = request.getParameter("resProvincia");
    descResComune = request.getParameter("descResComune");
    resComune = request.getParameter("resComune");
    resCAP = request.getParameter("resCAP");
    unitaProduttiva = request.getParameter("unitaProduttiva");
	  resTelefono = request.getParameter("resTelefono");
	  resFax = request.getParameter("resFax");
	  resMail = request.getParameter("resMail");
	  note = request.getParameter("note");
	  subentro = request.getParameter("subentro");
	  cuaaSubentro = request.getParameter("cuaaSubentro");
	  denomSubentro = request.getParameter("denomSubentro");
	  idAziendaSubentro = request.getParameter("idAziendaSubentro");
	 
	}
	
	
	Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
  if(vActor.contains(SolmrConstants.GESTORE_CAA))
  {
    htmpl.newBlock("blkCampoCodEnte");
    htmpl.set("blkCampoCodEnte.codEnte", codEnte);
  }
  
  htmpl.set("cuaa",cuaa);
	htmpl.set("denominazione", denominazione);
	htmpl.set("cuaa",cuaa);
  htmpl.set("partitaIva",partitaIva);
  htmpl.set("sedePec",sedePec);
  htmpl.set("sedelegIndirizzo",sedelegIndirizzo);
  htmpl.set("sedelegProv",sedelegProv);
  htmpl.set("descComune",descComune);
  htmpl.set("sedelegComune",sedelegComune);
  htmpl.set("sedelegCAP",sedelegCAP);
  htmpl.set("sedeMail",sedeMail);
  htmpl.set("sedeTelefono",sedeTelefono);
  htmpl.set("sedeFax",sedeFax);
  if("N".equalsIgnoreCase(unitaProduttiva))
  {
    htmpl.set("checkedNo","checked=\"true\"",null);
  }
  else
  {
    htmpl.set("checkedSi","checked=\"true\"",null);
  }
  htmpl.set("codiceFiscaleRL",codiceFiscaleRL);
  htmpl.set("cognome",cognome);
  htmpl.set("nome",nome);
  if("M".equalsIgnoreCase(sesso))
  {
    htmpl.set("checkedM","checked=\"true\"",null);
  }
  else if("F".equalsIgnoreCase(sesso))
  {
    htmpl.set("checkedF","checked=\"true\"",null);
  }
  htmpl.set("nascitaData",nascitaData);
  htmpl.set("nascitaProv",nascitaProv);
  htmpl.set("descNascitaComune",descNascitaComune);
  htmpl.set("codiceFiscaleComuneNascita", codiceFiscaleComuneNascita);
  htmpl.set("nascitaComune",nascitaComune);
  htmpl.set("nascitaStatoEstero",nascitaStatoEstero);
  htmpl.set("istatStatoEsteroNascita",istatStatoEsteroNascita);
  htmpl.set("cittaNascita",cittaNascita);
  htmpl.set("resIndirizzo",resIndirizzo);
  htmpl.set("resProvincia",resProvincia);
  htmpl.set("descResComune",descResComune);
  htmpl.set("resComune",resComune);
  htmpl.set("resCAP",resCAP);
  htmpl.set("resTelefono",resTelefono);
  htmpl.set("resFax",resFax);
  htmpl.set("resMail",resMail);
  htmpl.set("note", note);
  
  if(!vActor.contains(SolmrConstants.GESTORE_CAA))
  {
    htmpl.newBlock("blkSubentro");
    if("S".equalsIgnoreCase(subentro))
	  {
	    htmpl.set("blkSubentro.checkedSubentroSi","checked=\"true\"",null);
	  }
	  else
	  {
	    htmpl.set("blkSubentro.checkedSubentroNo","checked=\"true\"",null);
	  }
	  htmpl.set("blkSubentro.cuaaSubentro", cuaaSubentro);
	  htmpl.set("blkSubentro.denomSubentro",denomSubentro);
	  htmpl.set("blkSubentro.idAziendaSubentro", idAziendaSubentro);
  }
  
  
  
	
	
  
  
  Vector<TipoTipologiaAziendaVO> collTipiAzienda = (Vector<TipoTipologiaAziendaVO>)request.getAttribute("collTipiAzienda");
  if(collTipiAzienda!=null&&collTipiAzienda.size()>0)
  {
    for(int i=0;i<collTipiAzienda.size();i++)
    {
      TipoTipologiaAziendaVO tipiAzienda = collTipiAzienda.get(i);
      htmpl.newBlock("cmbTipoAzienda");
      htmpl.set("cmbTipoAzienda.idTipoAzienda",""+tipiAzienda.getIdTipologiaAzienda());
      htmpl.set("cmbTipoAzienda.descTipoAzienda",tipiAzienda.getDescrizione());
      if(Validator.isNotEmpty(idTipoAzienda) && tipiAzienda.getIdTipologiaAzienda().equals(idTipoAzienda))
      {
        htmpl.set("cmbTipoAzienda.selected","selected", null);
      }
    }
  }
  
  
  // Combo tipo forma giuridica
  Vector<CodeDescription> vTipoFormaGiuridica = (Vector<CodeDescription>)request.getAttribute("vTipoFormaGiuridica");
  if(vTipoFormaGiuridica != null && vTipoFormaGiuridica.size() > 0) 
  {
    for(int i=0; i<vTipoFormaGiuridica.size(); i++)
    {
      CodeDescription tipoFormaGiuridica = vTipoFormaGiuridica.get(i);
      htmpl.newBlock("blkTipoFormaGiuridicaCombo");
      htmpl.set("blkTipoFormaGiuridicaCombo.idTipoFormaGiuridica", ""+tipoFormaGiuridica.getCode());
      htmpl.set("blkTipoFormaGiuridicaCombo.idTipoAzienda", tipoFormaGiuridica.getSecondaryCode());
      htmpl.set("blkTipoFormaGiuridicaCombo.descTipoFormaGiuridica", tipoFormaGiuridica.getDescription());
      htmpl.set("blkTipoFormaGiuridicaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(idTipoFormaGiuridica) 
        && idTipoFormaGiuridica.equals(tipoFormaGiuridica.getCode().toString())) 
      {
        htmpl.set("idTipoFormaGiuridicaSel",""+tipoFormaGiuridica.getCode());
      }
    }
  }
  
  
  
  
	
	


      
  

%>
<%= htmpl.text() %>
