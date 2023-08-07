package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocIdentificazione;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

public class DocumentoIdentificazione extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_IDENTIFICAZIONE";

  public DocumentoIdentificazione() throws IOException, SolmrException
  {
    super( CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    //Metto il flag a false poiche Sergio ha dettoche bisogna prendere sempre tutto
    //quindi utilizzare la query col flag a false
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");

    DocIdentificazione docIdentificazione = new DocIdentificazione();
    protocollo.setDocIdentificazione(docIdentificazione);
    
    
    docIdentificazione.setCuaa(StampeGaaServlet.checkNull(anagAziendaVO.getCUAA()));
    docIdentificazione.setDenominazione(StampeGaaServlet.checkNull(
        anagAziendaVO.getDenominazione()));

    if (documentoVO.getTipoDocumentoVO() != null)
    {
    	docIdentificazione.setDescrizione(StampeGaaServlet.checkNull(
          documentoVO.getTipoDocumentoVO().getDescrizione()));
    }
    
    if(documentoVO.getTipoTipologiaDocumento() != null)
    {
    	docIdentificazione.setTipologiaDocumento( StampeGaaServlet.checkNull(
          documentoVO.getTipoTipologiaDocumento().getDescription()));
    }
    
    docIdentificazione.setStato(StampeGaaServlet.checkNull(
        documentoVO.getDescStatoDocumento()));
    docIdentificazione.setDataInizioValidita(StampeGaaServlet.checkNull(
            DateUtils.formatDateNotNull(documentoVO.getDataInizioValidita())));
    docIdentificazione.setDataFineValidita(StampeGaaServlet.checkNull(
            DateUtils.formatDateNotNull(documentoVO.getDataFineValidita())));
    docIdentificazione.setDataUltimoAggiornamento(StampeGaaServlet.checkNull(
            DateUtils.formatDateNotNull(documentoVO.getDataUltimoAggiornamento())));
    
    if (documentoVO.getNumeroProtocollo() != null)
    {
    	docIdentificazione.setNumeroProtocollo(StampeGaaServlet.checkNull(
    			StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())));
    }
    docIdentificazione.setDataProtocollo(StampeGaaServlet.checkNull(
        DateUtils.formatDateNotNull(documentoVO.getDataProtocollo())));
    docIdentificazione.setProtocolloEsterno(StampeGaaServlet.checkNull(
        documentoVO.getNumeroProtocolloEsterno())); 
    docIdentificazione.setCausaleModifica(StampeGaaServlet.checkNull(
        documentoVO.getDescCausaleModificaDocumento())); 
    docIdentificazione.setNote(StampeGaaServlet.checkNull(
        documentoVO.getNote()));
  }
}