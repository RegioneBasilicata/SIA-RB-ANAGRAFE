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
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO" %>
<%@ page import="it.csi.solmr.ws.infoc.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>


<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/newInserimentoAnagraficaPrivati.htm");
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
  
  if(Validator.isNotEmpty(aziendaNuovaVO) && Validator.isNotEmpty(aziendaNuovaVO.getIdTipoRichiesta()))
  {
    htmpl.set("idTipoRichiesta", ""+aziendaNuovaVO.getIdTipoRichiesta());
  }
  else
  {
    htmpl.set("idTipoRichiesta", request.getParameter("idTipoRichiesta"));
  }
  String regimeInserimentoPrivati = request.getParameter("regimeInserimentoPrivati");
  
  
  String idMotivoRichiesta = request.getParameter("idMotivoRichiesta");
  Vector<MotivoRichiestaVO> vMotivoRichiesta = (Vector<MotivoRichiestaVO>)request.getAttribute("vMotivoRichiesta");
  for(int i=0;i<vMotivoRichiesta.size();i++)
  {
    htmpl.newBlock("tipoMotivoRichiesta");
    htmpl.set("tipoMotivoRichiesta.idMotivoRichiesta", ""+vMotivoRichiesta.get(i).getIdMotivoRichiesta());
    htmpl.set("tipoMotivoRichiesta.descrizione", vMotivoRichiesta.get(i).getDescrizione());
    if(Validator.isNotEmpty(regimeInserimentoPrivati))
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
  
 
  String cuaa = "";
  String sedePec = "";
  String codiceFiscaleRL = "";
  String partitaIva = "";
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
  if(request.getParameter("regimeInserimentoPrivati") == null)
  {  
    if(request.getAttribute("aziendaAAEPTrovata") != null)
    {  
      Azienda aziendaAAEP = (Azienda)request.getAttribute("aziendaAAEP");
      
      cuaa = aziendaAAEP.getCodiceFiscale().getValue();      
      partitaIva = aziendaAAEP.getPartitaIva().getValue();
      
      if(request.getAttribute("sedePec") != null)
      {
        sedePec = (String)request.getAttribute("sedePec");
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
          nascitaData = rappresentanteLegaleAAEP.getDataNascita().getValue();
        }
        ComuneVO comuneNascita = (ComuneVO)request.getAttribute("comuneNascita");
        if(Validator.isNotEmpty(comuneNascita))
        {
          nascitaProv = comuneNascita.getSiglaProv();
          descNascitaComune = comuneNascita.getDescom();
          nascitaComune = comuneNascita.getIstatComune();
        }
        if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getIndirizzo().getValue())) 
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
        if(Validator.isNotEmpty(rappresentanteLegaleAAEP.getCap().getValue())) 
        {
          resCAP = rappresentanteLegaleAAEP.getCap().getValue();
        }
        
      }
      
      
      
    }
    else if(request.getAttribute("anagTrib") != null)
    {
      SianAnagTributariaVO anagTrib = (SianAnagTributariaVO)request.getAttribute("anagTrib");
      cuaa = anagTrib.getCodiceFiscale();
      partitaIva = anagTrib.getPartitaIva();
      
      
      if(Validator.isNotEmpty(anagTrib.getCognome()))
      {
        cognome = anagTrib.getCognome();
      }          
      if(Validator.isNotEmpty(anagTrib.getNome()))
      {
        nome = anagTrib.getNome();
      }
      if(Validator.isNotEmpty(anagTrib.getSesso()))
      { 
        sesso = anagTrib.getSesso();
      }
      if(Validator.isNotEmpty(anagTrib.getDataNascita()))
      {
        nascitaData = anagTrib.getDataNascita();
      }
      Vector<ComuneVO> comuniTribNas = (Vector<ComuneVO>)request.getAttribute("comuniTribNas");
      if(Validator.isNotEmpty(comuniTribNas)) 
      {
        ComuneVO comuneNascita = null;
        if (comuniTribNas!=null && comuniTribNas.size()==1)
        { 
          comuneNascita=(ComuneVO)comuniTribNas.get(0);
        }
        else
        {
          for(int i=0;i<comuniTribNas.size();i++)
          {
            if("N".equalsIgnoreCase(comuniTribNas.get(i).getFlagEstinto()))
            {
              comuneNascita=(ComuneVO)comuniTribNas.get(i);
              break;
            }
          }
        }
            
        if (comuneNascita!=null)
        {
          nascitaProv = comuneNascita.getSiglaProv();
          descNascitaComune = comuneNascita.getDescom();
          nascitaComune = comuneNascita.getIstatComune();        
        }
      }
         
      if(Validator.isNotEmpty(anagTrib.getIndirizzoResidenza()))
      {  
        resIndirizzo = anagTrib.getIndirizzoResidenza();
      }
      
      Vector<ComuneVO> comuniTribRes = (Vector<ComuneVO>)request.getAttribute("comuniTribRes");
      if(Validator.isNotEmpty(comuniTribRes)) 
      {
        ComuneVO comuneRes = null;
        if (comuniTribRes!=null && comuniTribRes.size()==1)
        { 
          comuneRes=(ComuneVO)comuniTribRes.get(0);
        }
        else
        {
          for(int i=0;i<comuniTribRes.size();i++)
          {
            if("N".equalsIgnoreCase(comuniTribRes.get(i).getFlagEstinto()))
            {
              comuneRes=(ComuneVO)comuniTribRes.get(i);
              break;
            }
          }
        }
            
        if (comuneRes!=null)
        {
          resProvincia = comuneRes.getSiglaProv();
          descResComune = comuneRes.getDescom();
          resComune = comuneRes.getIstatComune();    
        }
      }

      if(Validator.isNotEmpty(anagTrib.getCapResidenza()))
      {    
        resCAP = anagTrib.getCapResidenza();
      }            
    
    }
    else if(Validator.isNotEmpty(aziendaNuovaVO))
    {
      cuaa = aziendaNuovaVO.getCuaa();
      if(Validator.isNotEmpty(aziendaNuovaVO.getPartitaIva()))
        partitaIva = aziendaNuovaVO.getPartitaIva();
      if(Validator.isNotEmpty(aziendaNuovaVO.getPec()))
        sedePec = aziendaNuovaVO.getPec();
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
      cuaa = request.getParameter("cuaaInserimento");
    }
    
    
  }
  else
  {
    cuaa = request.getParameter("cuaa");
    sedePec = request.getParameter("sedePec");
    codiceFiscaleRL = request.getParameter("cuaa");
    partitaIva = request.getParameter("partitaIva");
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
    resTelefono = request.getParameter("resTelefono");
	  resFax = request.getParameter("resFax");
	  resMail = request.getParameter("resMail");
	  note = request.getParameter("note");
	  subentro = request.getParameter("subentro");
	  cuaaSubentro = request.getParameter("cuaaSubentro");
	  denomSubentro = request.getParameter("denomSubentro");
	  idAziendaSubentro = request.getParameter("idAziendaSubentro");
   
  }
  
  
  
  htmpl.set("cuaa",cuaa);
  htmpl.set("sedePec",sedePec);
  htmpl.set("codiceFiscaleRL",codiceFiscaleRL);
  htmpl.set("partitaIva",partitaIva);
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
  if("S".equalsIgnoreCase(subentro))
  {
    htmpl.set("checkedSubentroSi","checked=\"true\"",null);
  }
  else
  {
    htmpl.set("checkedSubentroNo","checked=\"true\"",null);
  }
  htmpl.set("cuaaSubentro", cuaaSubentro);
  htmpl.set("denomSubentro",denomSubentro);
  htmpl.set("idAziendaSubentro", idAziendaSubentro);
  
  
 
	


      
  

%>
<%= htmpl.text() %>
