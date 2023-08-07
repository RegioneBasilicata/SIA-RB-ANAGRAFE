  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/ricerca.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  //Valorizza segnaposto htmpl
  HtmplUtil.setValues(htmpl, request);
  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);

  AnagAziendaVO aziendaVO = (AnagAziendaVO)(request.getAttribute("aziendaVO"));
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  /**
   * Carico le combo dei tipi azienda e delle forme giuridiche.
   * */
  Collection collTipiAzienda=(Collection)anagFacadeClient.getTipiTipologiaAzienda(null,null);

  if(collTipiAzienda!=null&&collTipiAzienda.size()>0)
  {
    Iterator iterFormaGiuridica = collTipiAzienda.iterator();
    while(iterFormaGiuridica.hasNext())
    {
      TipoTipologiaAziendaVO tipiAzienda = (TipoTipologiaAziendaVO)iterFormaGiuridica.next();
      htmpl.newBlock("cmbTipiAzienda");
      htmpl.set("cmbTipiAzienda.idTipiAzienda",""+tipiAzienda.getIdTipologiaAzienda());
      htmpl.set("cmbTipiAzienda.descTipiAzienda",tipiAzienda.getDescrizione());
      if(aziendaVO!=null && tipiAzienda.getIdTipologiaAzienda().equals(aziendaVO.getTipiAzienda()))
        htmpl.set("cmbTipiAzienda.selected","selected");
    }
  }


  if ((aziendaVO!=null && aziendaVO.getTipiAzienda()!=null))
  {
    Collection collFormaGiuridica=null;
    if (aziendaVO.getTipiAzienda()!=null)
      collFormaGiuridica = (Collection)anagFacadeClient.getTipiFormaGiuridica(new Long(aziendaVO.getTipiAzienda()));

    if(collFormaGiuridica!=null&&collFormaGiuridica.size()>0)
    {
      Iterator iterFormaGiuridica = collFormaGiuridica.iterator();
      while(iterFormaGiuridica.hasNext())
      {
        CodeDescription cdFormaGiuridica = (CodeDescription)iterFormaGiuridica.next();
        htmpl.newBlock("cmbTipoFormaGiuridica");
        htmpl.set("cmbTipoFormaGiuridica.idTipiFormaGiuridica",""+cdFormaGiuridica.getCode());
        htmpl.set("cmbTipoFormaGiuridica.descTipiFormaGiuridica",cdFormaGiuridica.getDescription());
          if (aziendaVO.getTipoFormaGiuridica()!=null)
            if(aziendaVO!=null && cdFormaGiuridica.getCode().equals(aziendaVO.getTipoFormaGiuridica().getCode()))
              htmpl.set("cmbTipoFormaGiuridica.selected","selected");
      }
    }
  }

  htmpl.set("descComune", aziendaVO.getDescComune());

  htmpl.set("sedelegProv", request.getParameter("provincia"));

  if ( request.getParameter("attivita") != null && request.getParameter("attivita").trim().equalsIgnoreCase("si") ){
    htmpl.set("checkedAziendaInAttivita", "checked=\"checked\"");
  }
  // Se è la prima volta che entro nella pagina seleziono il checkbox in attività
  // come richiesto nel caso d'uso CU-GAA01: Ricerca azienda versione 35.work
  else if(Validator.isNotEmpty(request.getAttribute("firstTime"))) {
    htmpl.set("checkedAziendaInAttivita", "checked=\"checked\"");
  }

  if(ruoloUtenza.isUtenteProvinciale()&&!ruoloUtenza.isUtenteRegionale()){
    if(request.getParameter("provincia") == null || request.getParameter("provincia").equals("")){
      Vector prov = anagFacadeClient.getProvinceByRegione(SolmrConstants.ID_REG_PIEMONTE);
      for(int i = 0; i<prov.size(); i++){
        ProvinciaVO vo = (ProvinciaVO)prov.elementAt(i);
        if(vo.getIstatProvincia().equals(ruoloUtenza.getIstatProvincia())){
          htmpl.set("sedelegProv", vo.getSiglaProvincia());
        }

      }
    }
  }

  htmpl.set("dataPuntuale", DateUtils.getCurrentDateString());
  htmpl.set("dataAvanzata", DateUtils.getCurrentDateString());


%>
<%= htmpl.text()%>
