<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.text.*" %>

<%

	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/correzioneAnomalia.htm");

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	HashMap elencoDocumentiCorrezioni = (HashMap)request.getAttribute("elencoDocumentiCorrezioni");

  	HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);

  	Vector anomalie = (Vector)request.getAttribute("anomalie");
  	Vector errAnomaliaDicConsistenzaVOs = (Vector)request.getAttribute("errAnomaliaDicConsistenzaVOs");

  	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "alt=\"{2}\" border=\"0\"></a>";
  	String htmlStringOK = "<img src=\"{0}\" alt=\"{1}\">";
  	String imko = "ko.gif";
  	String imok = "ok.gif";
  	StringProcessor jssp = new JavaScriptStringProcessor();

  	String erroreComboDocumento = "Selezionare un documento";

  	if(anomalie != null) {
    	int size = anomalie.size();
    	int valoreLegenda = 0;
    	if(size > 0) {
      		Iterator iteraAnomalie = anomalie.iterator();
      		ErrAnomaliaDicConsistenzaVO err = null;
      		int j = 0;
      		while(iteraAnomalie.hasNext()) {
        		err = (ErrAnomaliaDicConsistenzaVO)iteraAnomalie.next();
        		htmpl.newBlock("blkTabAnomalia");
        		htmpl.set("blkTabAnomalia.descAnomaliaErrore",err.getDescAnomaliaErrore());
        		htmpl.set("blkTabAnomalia.idDichiarazioneSegnalazione",err.getIdDichiarazioneSegnalazione());
        		htmpl.set("blkTabAnomalia.idControllo",err.getIdControllo());
        		htmpl.set("blkTabAnomalia.idStoricoParticella",err.getIdStoricoParticella());
        		if(err.getFlagDocumentoGiustificativo() == null) {
          			htmpl.newBlock("blkTabAnomalia.blkNoGiustificativo");
        		}
        		else {
          			htmpl.newBlock("blkTabAnomalia.blkGiustificativo");
           			// Sono qua perchè ho già provato a salvare ma ho riscontrato degli errori
          			ErrAnomaliaDicConsistenzaVO errOld = null;
           			if(errAnomaliaDicConsistenzaVOs != null && errAnomaliaDicConsistenzaVOs.size() > 0) {
           				errOld = (ErrAnomaliaDicConsistenzaVO)errAnomaliaDicConsistenzaVOs.get(j);
           			}
          			// Gestione dei documenti
          			if(elencoDocumentiCorrezioni != null && elencoDocumentiCorrezioni.size() > 0) {
          				if(elencoDocumentiCorrezioni.get(Long.decode(err.getIdDichiarazioneSegnalazione())) instanceof DocumentoVO[]) {
          					htmpl.newBlock("blkTabAnomalia.blkGiustificativo.blkComboDocumento");
          					htmpl.set("blkTabAnomalia.blkGiustificativo.blkComboDocumento.flagDocumentoGiustificativo",err.getFlagDocumentoGiustificativo());
          					DocumentoVO[] elencoDocumenti = (DocumentoVO[])elencoDocumentiCorrezioni.get(Long.decode(err.getIdDichiarazioneSegnalazione()));
          					for(int i = 0; i < elencoDocumenti.length; i++) {
          						DocumentoVO documentoVO = (DocumentoVO)elencoDocumenti[i];
          						htmpl.newBlock("blkTabAnomalia.blkGiustificativo.blkComboDocumento.blkElencoDocumenti");
          						htmpl.set("blkTabAnomalia.blkGiustificativo.blkComboDocumento.blkElencoDocumenti.code", documentoVO.getIdDocumento().toString());
          						String descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
          						if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
                      {
          							descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())+" del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
          						}
          						htmpl.set("blkTabAnomalia.blkGiustificativo.blkComboDocumento.blkElencoDocumenti.description", descrizione);
          						if(errAnomaliaDicConsistenzaVOs != null && errOld != null && errOld.getIdDocumento() != null) 
                      {                        
                        if(documentoVO.getIdDocumento().compareTo(errOld.getIdDocumento()) == 0) 
                        {
    			                htmpl.set("blkTabAnomalia.blkGiustificativo.blkComboDocumento.blkElencoDocumenti.selected","selected=\"selected\"");
    			            	}
          						}
                      else //popolo il combo nel caso sia costituito da un valore solo con quel valore!!!
                      {
                        if(elencoDocumenti.length == 1)
                        {
                          htmpl.set("blkTabAnomalia.blkGiustificativo.blkComboDocumento.blkElencoDocumenti.selected","selected=\"selected\"");
                        }
                      } 
          						valoreLegenda++;
          					}
          				}
          				else {
          					htmpl.newBlock("blkTabAnomalia.blkGiustificativo.blkLabelDocumento");
          					TipoDocumentoVO[] elencoTipiDocumento = (TipoDocumentoVO[])elencoDocumentiCorrezioni.get(Long.decode(err.getIdDichiarazioneSegnalazione()));
          					for(int i = 0; i < elencoTipiDocumento.length; i++) {
          						htmpl.newBlock("blkTabAnomalia.blkGiustificativo.blkLabelDocumento.blkElencoTipoDocumento");
          						TipoDocumentoVO tipoDocumentoVO = (TipoDocumentoVO)elencoTipiDocumento[i];
          						if(elencoTipiDocumento.length > 1) {
          							htmpl.set("blkTabAnomalia.blkGiustificativo.blkLabelDocumento.blkElencoTipoDocumento.descTipoDocumento", String.valueOf(i+1)+") "+tipoDocumentoVO.getDescrizione()+" ");
          						}
          						else {
          							htmpl.set("blkTabAnomalia.blkGiustificativo.blkLabelDocumento.blkElencoTipoDocumento.descTipoDocumento", tipoDocumentoVO.getDescrizione());
          						}
          					}
          				}
          			}
          			if(errAnomaliaDicConsistenzaVOs != null) {
            			if(!errOld.isNoError()) {
              				if(errOld.isErrExtIdTipoDocumento()) {
                				htmpl.set("blkTabAnomalia.blkGiustificativo.err_idDocumento",
                          				   MessageFormat.format(htmlStringKO,
                                           new Object[] {
                                           pathErrori + "/"+ imko,
                                           "'"+jssp.process(erroreComboDocumento)+"'",
                                           erroreComboDocumento}),
                        				   null);
              				}
              				else {
                				htmpl.set("blkTabAnomalia.blkGiustificativo.err_idDocumento",
                          		           MessageFormat.format(htmlStringOK,
                                           new Object[] {
                                           pathErrori + imok,""}),
                                           null);
              				}
            			}
          			}
        		}
        		j++;
      		}
    	}
    	if(valoreLegenda > 0) {
    		htmpl.newBlock("blkLegenda");
    	}
  	}
  	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  	HtmplUtil.setErrors(htmpl, errors, request, application);

%>

<%= htmpl.text()%>
