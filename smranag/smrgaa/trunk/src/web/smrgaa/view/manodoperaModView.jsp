<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="java.util.*" %>

<%
  SolmrLogger.debug(this, " - manodoperaModView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/manodoperaMod.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Vector<TipoIscrizioneINPSVO> vTipoIscrizioneINPS = (Vector<TipoIscrizioneINPSVO>)request
    .getAttribute("vTipoIscrizioneINPS");


  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());
  
  

  //Tipo Classi Manodopera: la descrizione viene prelevata dalla tabella di codifica,
  //è ordinata per codice tipo classe manodopera
  Vector vTipoClassiManodopera = anagFacadeClient.getTipoClassiManodopera();
  htmpl.set("desFamTempoPieno", ((CodeDescription) vTipoClassiManodopera.get(0)).getDescription().toString());
  htmpl.set("idFamTempoPieno", ((CodeDescription) vTipoClassiManodopera.get(0)).getCode().toString());
  htmpl.set("desFamTempoParz", ((CodeDescription) vTipoClassiManodopera.get(1)).getDescription().toString());
  htmpl.set("idFamTempoParz", ((CodeDescription) vTipoClassiManodopera.get(1)).getCode().toString());
  htmpl.set("desSalFisTempoPieno", ((CodeDescription) vTipoClassiManodopera.get(2)).getDescription().toString());
  htmpl.set("idSalFisTempoPieno", ((CodeDescription) vTipoClassiManodopera.get(2)).getCode().toString());
  htmpl.set("desSalFisTempoParz", ((CodeDescription) vTipoClassiManodopera.get(3)).getDescription().toString());
  htmpl.set("idSalFisTempoParz", ((CodeDescription) vTipoClassiManodopera.get(3)).getCode().toString());
  htmpl.set("desSalAvv", ((CodeDescription) vTipoClassiManodopera.get(4)).getDescription().toString());
  htmpl.set("idSalAvv", ((CodeDescription) vTipoClassiManodopera.get(4)).getCode().toString());

  ManodoperaVO manodoperaVO = null;
  Vector vDettaglioManodopera = null;
  DettaglioManodoperaVO dettaglioManodoperaVO = null;
  DettaglioAttivitaVO dettaglioAttivitaVO = null;

  HashMap hmManodopera = (HashMap) session.getAttribute("common");
  if (hmManodopera != null && hmManodopera.size() > 0) 
  {
    //indice 0 del vettore: Manodopera e Vector Dettaglio Manodopera
    manodoperaVO = (ManodoperaVO) hmManodopera.get("manodoperaVO");
    if (manodoperaVO != null)
    {
      htmpl.set("codiceInps", manodoperaVO.getCodiceInps());
      htmpl.set("dataInizioIscrizione", manodoperaVO.getDataInizioIscrizione());
      htmpl.set("dataCessazioneIscrizione", manodoperaVO.getDataCessazioneIscrizione());
      
      
      
      if(Validator.isNotEmpty(vTipoIscrizioneINPS))
		  {
		    for(int i=0;i<vTipoIscrizioneINPS.size();i++)
		    {
		      TipoIscrizioneINPSVO tipoIscrizione = vTipoIscrizioneINPS.get(i);
		      htmpl.newBlock("blkTipoIscrizioneINPS");
		      htmpl.set("blkTipoIscrizioneINPS.idTipoIscrizioneINPS", ""+tipoIscrizione.getIdTipoIscrizioneINPS());
		      htmpl.set("blkTipoIscrizioneINPS.descTipoIscizioneINPS", tipoIscrizione.getDescrizione());
		      if(manodoperaVO.getIdTipoIscrizioneINPS() != null && manodoperaVO.getIdTipoIscrizioneINPS().compareTo(new Integer(tipoIscrizione.getIdTipoIscrizioneINPS())) == 0) 
		      {
		        htmpl.set("blkTipoIscrizioneINPS.selected", "selected=\"selected\"", null);
		      }
		    }
		  }
      
      

      vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();
      if (vDettaglioManodopera != null)
      {
        //indice 0 del Vector: tipo classe codice 10
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(new Integer(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO").toString()).intValue()/10-1);
        impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "famTempoPienoUomini", "famTempoPienoDonne");

        //indice 1 del Vector: tipo classe codice 20
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(new Integer(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE").toString()).intValue()/10-1);
        impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "famTempoParzUomini", "famTempoParzDonne");

        //indice 2 del Vector: tipo classe codice 30
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(new Integer(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO").toString()).intValue()/10-1);
        impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salFisTempoPienoUomini", "salFisTempoPienoDonne");

        //indice 3 del Vector: tipo classe codice 40
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(new Integer(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE").toString()).intValue()/10-1);
        impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salFisTempoParzUomini", "salFisTempoParzDonne");

        //indice 4 del Vector: tipo classe codice 50
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vDettaglioManodopera.get(new Integer(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI").toString()).intValue()/10-1);
        impostaRigaDettaglioManodopera(dettaglioManodoperaVO, htmpl, "salAvvUomini", "salAvvDonne");
        if (dettaglioManodoperaVO.getGiornateAnnue() != null && ! dettaglioManodoperaVO.getGiornateAnnue().equals("0"))
          htmpl.set("salAvvGiornateAnnue", dettaglioManodoperaVO.getGiornateAnnue());
      }
    }
  }

  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - manodoperaModView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>

<%!
  private void impostaRigaDettaglioManodopera(DettaglioManodoperaVO dettaglioManodoperaVO, Htmpl htmpl, String segnUomini, String segnDonne)
  {
    if (dettaglioManodoperaVO != null)
    {
      if (dettaglioManodoperaVO.getUomini() != null && ! dettaglioManodoperaVO.getUomini().equals("0"))
        htmpl.set(segnUomini, dettaglioManodoperaVO.getUomini());
      if (dettaglioManodoperaVO.getDonne() != null && ! dettaglioManodoperaVO.getDonne().equals("0"))
        htmpl.set(segnDonne, dettaglioManodoperaVO.getDonne());
    }
  }
%>
