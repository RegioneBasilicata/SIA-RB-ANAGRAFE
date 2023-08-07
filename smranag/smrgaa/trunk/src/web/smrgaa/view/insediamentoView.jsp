<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  SolmrLogger.debug(this, " - insediamentoView.jsp - INIZIO PAGINA");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/insediamento.htm");

  %>
  <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO modAnagAziendaVO = (AnagAziendaVO)session.getAttribute("modAnagAziendaVO");
  PersonaFisicaVO modPersonaVO = (PersonaFisicaVO)session.getAttribute("modPersonaVO");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Devo cambiare la label della provincia di competenza a seconda che sono
  // in piemonte o in sardegna
  boolean piemonte=SolmrConstants.ID_REG_PIEMONTE.equals(ruoloUtenza.getIstatRegioneAttiva());
  if(piemonte) htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_PIEMONTE);
  else htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_SARDEGNA);

  /*******************************************/
  /*  Valorizzo i dati relativi all'azienda  */
  /*******************************************/
  if(Validator.isNotEmpty(modAnagAziendaVO.getCUAA()))
    htmpl.set("cuaa", modAnagAziendaVO.getCUAA().toUpperCase());
  htmpl.set("strCCIAAnumeroREA",modAnagAziendaVO.getStrCCIAAnumeroREA());


  Vector elencoProvince = anagFacadeClient.getProvinceByRegione(ruoloUtenza.getIstatRegioneAttiva());
  Iterator iteraProvince = elencoProvince.iterator();
  while(iteraProvince.hasNext())
  {
    htmpl.newBlock("codiceProvince");
    ProvinciaVO province = (ProvinciaVO)iteraProvince.next();
    if(modAnagAziendaVO.getProvCompetenza().equalsIgnoreCase(province.getIstatProvincia()))
            htmpl.set("codiceProvince.check", "selected");
    htmpl.set("codiceProvince.idCodice",province.getIstatProvincia());
    htmpl.set("codiceProvince.descrizione",province.getDescrizione());
  }

  // Carico le combo dei tipi azienda e delle forme giuridiche.
  // Lo faccio qua altrimenti dovrei duplicarlo nei due controller che
  // chiamano la pagina
  Collection collTipiAzienda = null;
  if(modAnagAziendaVO.isFlagAziendaProvvisoria())
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,new Boolean(true));
  else
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,null);
  if(collTipiAzienda != null && collTipiAzienda.size() > 0)
  {
    Iterator iterFormaGiuridica = collTipiAzienda.iterator();
    while(iterFormaGiuridica.hasNext())
    {
      TipoTipologiaAziendaVO tipiAzienda = (TipoTipologiaAziendaVO)iterFormaGiuridica.next();
      htmpl.newBlock("cmbTipiAzienda");
      htmpl.set("cmbTipiAzienda.idTipiAzienda",""+tipiAzienda.getIdTipologiaAzienda());
      htmpl.set("cmbTipiAzienda.descTipiAzienda",tipiAzienda.getDescrizione());
      if(modAnagAziendaVO != null)
      {
        if(tipiAzienda.getIdTipologiaAzienda().equals(modAnagAziendaVO.getTipiAzienda()))
          htmpl.set("cmbTipiAzienda.selected","selected");
      }
      else
      {
        if(modAnagAziendaVO != null && tipiAzienda.getIdTipologiaAzienda().equals(modAnagAziendaVO.getTipiAzienda()))
          htmpl.set("cmbTipiAzienda.selected","selected");
      }
    }
  }

  if((modAnagAziendaVO != null && modAnagAziendaVO.getTipiAzienda() != null) || (modAnagAziendaVO != null && modAnagAziendaVO.getTipiAzienda() != null))
  {
    Collection collFormaGiuridica = null;
    if(modAnagAziendaVO != null)
    {
      if(modAnagAziendaVO.getTipiAzienda() != null)
        collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(modAnagAziendaVO.getTipiAzienda()));
    }
    else
    {
      if(modAnagAziendaVO.getTipiAzienda() != null)
        collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(modAnagAziendaVO.getTipiAzienda()));
    }
    if(collFormaGiuridica != null && collFormaGiuridica.size() > 0)
    {
      Iterator iterFormaGiuridica = collFormaGiuridica.iterator();
      while(iterFormaGiuridica.hasNext())
      {
        CodeDescription cdFormaGiuridica = (CodeDescription)iterFormaGiuridica.next();
        htmpl.newBlock("cmbTipoFormaGiuridica");
        htmpl.set("cmbTipoFormaGiuridica.idTipiFormaGiuridica",""+cdFormaGiuridica.getCode());
        htmpl.set("cmbTipoFormaGiuridica.descTipiFormaGiuridica",cdFormaGiuridica.getDescription());
        if(modAnagAziendaVO != null)
        {
          if(modAnagAziendaVO.getTipoFormaGiuridica() != null)
          {
            if(cdFormaGiuridica.getCode().equals(modAnagAziendaVO.getTipoFormaGiuridica().getCode()))
              htmpl.set("cmbTipoFormaGiuridica.selected","selected");
          }
        }
        else
        {
          if(modAnagAziendaVO.getTipoFormaGiuridica() != null)
          {
            if(modAnagAziendaVO!=null && cdFormaGiuridica.getCode().equals(modAnagAziendaVO.getTipoFormaGiuridica().getCode()))
              htmpl.set("cmbTipoFormaGiuridica.selected","selected");
          }
        }
      }
    }
  }

  /******************************************************/
  /* Valorizzo i Dati relativi al rappresentante legale */
  /******************************************************/

  if(modPersonaVO!=null)
  {
    if(modPersonaVO.getStrNascitaData() == null && modPersonaVO.getNascitaData()!=null)
      modPersonaVO.setStrNascitaData(DateUtils.formatDate(modPersonaVO.getNascitaData()));

    htmpl.set("codiceFiscale", modPersonaVO.getCodiceFiscale());
    htmpl.set("cognome", modPersonaVO.getCognome());
    htmpl.set("nome", modPersonaVO.getNome());

    if(modPersonaVO.getSesso()!=null)
    {
      if(modPersonaVO.getSesso().equalsIgnoreCase("M"))
        htmpl.set("checkedM","checked");
      else
        htmpl.set("checkedF","checked");
    }
    else htmpl.set("checkedM","checked");

    htmpl.set("strNascitaData", modPersonaVO.getStrNascitaData());


    ComuneVO comuneVO = null;
    if(modPersonaVO.getNascitaComune()!=null && !modPersonaVO.getNascitaComune().equals(""))
    {
      try
      {
        comuneVO = anagFacadeClient.getComuneByISTAT(modPersonaVO.getNascitaComune());
      }
      catch(Exception ex) {}
    }
    if(comuneVO!=null && comuneVO.getFlagEstero().equals(SolmrConstants.FLAG_S))
    {
      htmpl.set("nascitaStatoEstero",modPersonaVO.getLuogoNascita());
      htmpl.set("cittaNascita",modPersonaVO.getNascitaCittaEstero());
      htmpl.set("descNascitaComune","");
      htmpl.set("nascitaProv","");
    }
    else
    {
      htmpl.set("descNascitaComune",modPersonaVO.getDescNascitaComune());
      htmpl.set("nascitaProv",modPersonaVO.getNascitaProv());
    }
  }




  HtmplUtil.setValues(htmpl, modAnagAziendaVO);
  HtmplUtil.setValues(htmpl, modPersonaVO);
  HtmplUtil.setErrors(htmpl, errors, request, application);


  SolmrLogger.debug(this, " - insediamentoView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>


