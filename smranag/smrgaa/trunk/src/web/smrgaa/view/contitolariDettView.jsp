<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.anag.services.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/contitolari_det.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)request.getAttribute("personaVO");
 AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 TesserinoFitoSanitarioVO tesserinoVO = (TesserinoFitoSanitarioVO)request.getAttribute("tesserinoVO");
 AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 htmpl.set("idSoggetto", personaFisicaVO.getIdSoggetto().toString());

 if(personaFisicaVO.getDataInizioRuolo()!=null&&!personaFisicaVO.getDataInizioRuolo().equals(""))
   htmpl.set("dataInizioRuolo", DateUtils.formatDate(personaFisicaVO.getDataInizioRuolo()));
 if(personaFisicaVO.getDataFineRuolo()!=null&&!personaFisicaVO.getDataFineRuolo().equals(""))
   htmpl.set("dataFineRuolo", DateUtils.formatDate(personaFisicaVO.getDataFineRuolo()));
   
   
 if(personaFisicaVO.getDataInizioRuoloMod()!=null&&!personaFisicaVO.getDataInizioRuoloMod().equals(""))
   htmpl.set("dataInizioRuoloMod", DateUtils.formatDate(personaFisicaVO.getDataInizioRuoloMod()));
 if(personaFisicaVO.getDataFineRuoloMod()!=null&&!personaFisicaVO.getDataFineRuoloMod().equals(""))
   htmpl.set("dataFineRuoloMod", DateUtils.formatDate(personaFisicaVO.getDataFineRuoloMod()));  
   
   
   
 if(personaFisicaVO.getNascitaData()!=null&&!personaFisicaVO.getNascitaData().equals(""))
   htmpl.set("nascitaData", DateUtils.formatDate(personaFisicaVO.getNascitaData()));

  // Informazioni relative ai dati anagrafici della persona
  if(Validator.isNotEmpty(personaFisicaVO.getNascitaStatoEstero())) 
  {
    htmpl.set("nascitaStatoEstero", personaFisicaVO.getNascitaStatoEstero());
    if(Validator.isNotEmpty(personaFisicaVO.getNascitaCittaEstero())) {
      htmpl.set("nascitaCittaEstero", " - "+personaFisicaVO.getNascitaCittaEstero());
    }
  }
  else 
  {
    htmpl.set("descNascitaComune", personaFisicaVO.getDescNascitaComune());
    if(Validator.isNotEmpty(personaFisicaVO.getNascitaProv())) {
      htmpl.set("siglaProvinciaComune", "("+personaFisicaVO.getNascitaProv()+")");
    }
  }

  // Informazioni relative alla residenza
  if(Validator.isNotEmpty(personaFisicaVO.getDescStatoEsteroResidenza())) 
  {
    htmpl.newBlock("blkResEstero");
    htmpl.set("blkResEstero.descStatoEsteroResidenza", personaFisicaVO.getDescStatoEsteroResidenza());
    if(Validator.isNotEmpty(personaFisicaVO.getResCittaEstero())) {
      htmpl.set("blkResEstero.resCittaEstero",personaFisicaVO.getResCittaEstero());
    }
  }
  else {
    htmpl.newBlock("blkResItalia");
    htmpl.set("blkResItalia.descResProvincia", personaFisicaVO.getDescResProvincia());
    htmpl.set("blkResItalia.descResComune", personaFisicaVO.getDescResComune());
    htmpl.set("blkResItalia.resCAP", personaFisicaVO.getResCAP());
  }  
	// Verifica che l'utente gestisca il fascicolo o si azienda agricola per visualizzare l'informazione del cellulare
	htmpl.set("resCellulare", personaFisicaVO.getdesNumeroCellulare());
  // Inserisco le informazioni relative al titolo di studio
  String titoloIndirizzoStudio = null;
  if(personaFisicaVO.getIdTitoloStudio() != null) {
    titoloIndirizzoStudio = personaFisicaVO.getDescrizioneTitoloStudio();
  }
  if(personaFisicaVO.getIdIndirizzoStudio() != null) {
    titoloIndirizzoStudio += " - "+personaFisicaVO.getDescrizioneIndirizzoStudio();
  }
  if(titoloIndirizzoStudio != null) {
    htmpl.set("titoloIndirizzoStudio",titoloIndirizzoStudio);
  }

  // Informazioni relative al domicilio
  if(Validator.isNotEmpty(personaFisicaVO.getDomicilioStatoEstero())) {
    htmpl.newBlock("blkDomicilioEstero");
    htmpl.set("blkDomicilioEstero.domicilioStatoEstero", personaFisicaVO.getDomicilioStatoEstero());
    if(Validator.isNotEmpty(personaFisicaVO.getDescCittaEsteroDomicilio())) {
      htmpl.set("blkDomicilioEstero.domCittaEstera", personaFisicaVO.getDescCittaEsteroDomicilio());
    }
  }
  else {
    htmpl.newBlock("blkDomicilioItalia");
    if(Validator.isNotEmpty(personaFisicaVO.getDomProvincia())) {
      htmpl.set("blkDomicilioItalia.domProvincia", personaFisicaVO.getDomProvincia());
    }
    if(Validator.isNotEmpty(personaFisicaVO.getDomComune())) {
      htmpl.set("blkDomicilioItalia.domComune", personaFisicaVO.getDomComune());
    }
    if(Validator.isNotEmpty(personaFisicaVO.getDomCAP())) {
      htmpl.set("blkDomicilioItalia.domCAP", personaFisicaVO.getDomCAP());
    }
  }
  
  String certFito = "";
  if(Validator.isNotEmpty(tesserinoVO))
  {
    if("S".equalsIgnoreCase(tesserinoVO.getFlagTesserinoScaduto()))
    {
      certFito = "No: "+tesserinoVO.getDescTipoCertificato()+" scaduto il "
        +DateUtils.formatDateNotNull(tesserinoVO.getDataScadenza());
    }
    else 
    {
      certFito = "Si: "+tesserinoVO.getDescTipoCertificato()
        +" rilasciato il "+DateUtils.formatDateNotNull(tesserinoVO.getDataRilascio())
        +", scadenza il "+DateUtils.formatDateNotNull(tesserinoVO.getDataScadenza());
    }
    
  }
  else
  {
    certFito = "No";
  }
  htmpl.set("certFito", certFito);
  
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(personaFisicaVO.getDataAggiornamento() !=null)
  {
    dateUlt = DateUtils.formatDate(personaFisicaVO.getDataAggiornamento());
  } 
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, personaFisicaVO.getDescUtenteAggiornamento(),
    null,null);

  HtmplUtil.setValues(htmpl, personaFisicaVO);
%>
<%= htmpl.text()%>
