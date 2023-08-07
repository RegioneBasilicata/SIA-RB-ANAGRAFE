<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaAziendaAnagrafica.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");

  String CUAASubentro=(String)request.getParameter("CUAASubentro");
  String radiobuttonAzienda=(String)request.getParameter("radiobuttonAzienda");
  String idAziendaSubentro=(String)request.getParameter("idAziendaSubentro");
  String idAziendaProvenienza=(String)request.getParameter("idAziendaProvenienza");
  String cuaaProvenienza=(String)request.getParameter("cuaaProvenienza");
  if (idAziendaSubentro==null || "".equals(idAziendaSubentro))
    idAziendaSubentro=(String)request.getAttribute("idAziendaSubentro");
  if (idAziendaProvenienza==null || "".equals(idAziendaProvenienza))
	  idAziendaProvenienza=(String)request.getAttribute("idAziendaProvenienza");

  if (anagAziendaVO!=null)
  {
    if (idAziendaSubentro==null || "".equals(idAziendaSubentro))
    {
      if (anagAziendaVO.getIdAziendaSubentro()!=null)
        idAziendaSubentro=anagAziendaVO.getIdAziendaSubentro().toString();
    }
    if (radiobuttonAzienda==null || "".equals(radiobuttonAzienda))
    {
      if (anagAziendaVO.isFlagAziendaProvvisoria())
      {
        if (anagAziendaVO.getIdAziendaSubentro()!=null)
          radiobuttonAzienda="3";
        else
          radiobuttonAzienda="2";
      }
    }
  }

  /**
   * Carico le combo dei tipi azienda e delle forme giuridiche.
   * Lo faccio qua altrimenti dovrei duplicarlo nei due controller che
   * chiamano la pagina
   * */
  Collection collTipiAzienda=null;
  if ("2".equals(radiobuttonAzienda) || "3".equals(radiobuttonAzienda))
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,new Boolean(true));
  else
    collTipiAzienda = (Collection)anagFacadeClient.getTipiTipologiaAzienda(null,null);

  if(collTipiAzienda!=null&&collTipiAzienda.size()>0){
    Iterator iterFormaGiuridica = collTipiAzienda.iterator();
    while(iterFormaGiuridica.hasNext()){
      TipoTipologiaAziendaVO tipiAzienda = (TipoTipologiaAziendaVO)iterFormaGiuridica.next();
      htmpl.newBlock("cmbTipiAzienda");
      htmpl.set("cmbTipiAzienda.idTipiAzienda",""+tipiAzienda.getIdTipologiaAzienda());
      htmpl.set("cmbTipiAzienda.descTipiAzienda",tipiAzienda.getDescrizione());
      if(anagAziendaVO!=null && tipiAzienda.getIdTipologiaAzienda().equals(anagAziendaVO.getTipiAzienda())){
        htmpl.set("cmbTipiAzienda.selected","selected");
      }
    }
  }


  if (anagAziendaVO!=null && anagAziendaVO.getTipiAzienda()!=null)
  {
    //Collection collProvincia = (Collection)anagFacadeClient.getProvinceByRegione(SolmrConstants.ID_REG_PIEMONTE);
    Collection collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(anagAziendaVO.getTipiAzienda()));

    if(collFormaGiuridica!=null&&collFormaGiuridica.size()>0){
      Iterator iterFormaGiuridica = collFormaGiuridica.iterator();
      while(iterFormaGiuridica.hasNext()){
        CodeDescription cdFormaGiuridica = (CodeDescription)iterFormaGiuridica.next();
        htmpl.newBlock("cmbTipoFormaGiuridica");
        htmpl.set("cmbTipoFormaGiuridica.idTipiFormaGiuridica",""+cdFormaGiuridica.getCode());
        htmpl.set("cmbTipoFormaGiuridica.descTipiFormaGiuridica",cdFormaGiuridica.getDescription());
        if (anagAziendaVO.getTipoFormaGiuridica()!=null)
          if(anagAziendaVO!=null && cdFormaGiuridica.getCode().equals(anagAziendaVO.getTipoFormaGiuridica().getCode())){
            htmpl.set("cmbTipoFormaGiuridica.selected","selected");
          }
      }
    }
  }

  //Devo cambiare la label della provincia di competenza a seconda che sono
  //in piemonte o in sardegna
  boolean piemonte=SolmrConstants.ID_REG_PIEMONTE.equals(ruoloUtenza.getIstatRegioneAttiva());
  htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_PIEMONTE);
 /* if (piemonte)
    htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_PIEMONTE);
  else
    htmpl.bset("labelProvCompetenza",SolmrConstants.LABEL_PROV_COMP_SARDEGNA);*/

  // Se l'utente è un funzionario PA allora gli permetto di scegliere un intemediario per la nuova
  // azienda
  if(ruoloUtenza.isUtenteProvinciale())
  {
    htmpl.newBlock("bloccoProvinceProvinciale");
    htmpl.set("bloccoProvinceProvinciale.provinciaCompetenza",ruoloUtenza.getProvincia());
  }
  else
  {
	SolmrLogger.debug(this, "-- ruoloUtenza.getIstatRegioneAttiva() ="+ruoloUtenza.getIstatRegioneAttiva());  
    Vector elencoProvince = anagFacadeClient.getProvinceByRegione(ruoloUtenza.getIstatRegioneAttiva());

    Iterator iteraProvince = elencoProvince.iterator();
    htmpl.newBlock("bloccoProvinceIntermediario");
    htmpl.newBlock("bloccoComboProvinceIntermediario");
    while(iteraProvince.hasNext())
    {
      htmpl.newBlock("bloccoComboProvinceIntermediario.codiceProvince");
      ProvinciaVO province = (ProvinciaVO)iteraProvince.next();
      if(anagAziendaVO != null && anagAziendaVO.getProvCompetenza() != null)
      {
        if(anagAziendaVO.getProvCompetenza().equalsIgnoreCase(province.getIstatProvincia()))
          htmpl.set("bloccoComboProvinceIntermediario.codiceProvince.check", "selected");
      }
      else
      {
        //Se sono un regionale devo settare come provincia di default il capoluogo
        if (ruoloUtenza.isUtenteRegionale())
        {
          if (piemonte)
          {
            if (SolmrConstants.CAPOLUOGO_PIEMONTE.equalsIgnoreCase(province.getDescrizione()))
              htmpl.set("bloccoComboProvinceIntermediario.codiceProvince.check", "selected");
          }
          else
          {
            if (SolmrConstants.CAPOLUOGO_SARDEGNA.equalsIgnoreCase(province.getDescrizione()))
              htmpl.set("bloccoComboProvinceIntermediario.codiceProvince.check", "selected");
          }
        }
      }
      htmpl.set("bloccoComboProvinceIntermediario.codiceProvince.idCodice",province.getIstatProvincia());
      htmpl.set("bloccoComboProvinceIntermediario.codiceProvince.descrizione",province.getDescrizione());
    }
  }

  htmpl.set("nomeCognomeUtente",ruoloUtenza.getDenominazione());
  htmpl.set("ente",ruoloUtenza.getDescrizioneEnte());

  htmpl.set("CUAASubentro",CUAASubentro);
  htmpl.set("radiobuttonAzienda",radiobuttonAzienda);
  htmpl.set("idAziendaSubentro",idAziendaSubentro);
  htmpl.set("idAziendaProvenienza",idAziendaProvenienza);
  htmpl.set("cuaaProvenienza",cuaaProvenienza);

  if(errors == null)
  {
    if(anagAziendaVO != null)
    {
      /*if(anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode() != null)
        htmpl.set("codiceOTE",anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode().toString());
      if(anagAziendaVO.getTipoAttivitaOTE().getDescription() != null)
        htmpl.set("descrizioneOTE",anagAziendaVO.getTipoAttivitaOTE().getDescription());
      if(anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode() != null)
        htmpl.set("codiceATECO",anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode().toString());
      if(anagAziendaVO.getTipoAttivitaATECO().getDescription() != null)
        htmpl.set("descrizioneATECO",anagAziendaVO.getTipoAttivitaATECO().getDescription());
      if(Validator.isNotEmpty(anagAziendaVO.getCCIAAprovREA())) {
    	  htmpl.set("CCIAAprovREA", anagAziendaVO.getCCIAAprovREA());
      }*/
      HtmplUtil.setValues(htmpl, anagAziendaVO);
    }
    HtmplUtil.setValues(htmpl, request);
  }
  else
  {
    HtmplUtil.setValues(htmpl, request);
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }
%>

<%= htmpl.text() %>
