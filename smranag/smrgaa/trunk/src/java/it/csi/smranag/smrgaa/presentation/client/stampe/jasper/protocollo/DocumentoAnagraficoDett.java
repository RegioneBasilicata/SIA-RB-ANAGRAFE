package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocAnagrafico;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

public class DocumentoAnagraficoDett extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_ANAGRAFICO_DETT";

  public DocumentoAnagraficoDett() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    DocAnagrafico anagrafico = new DocAnagrafico();
    anagrafico.setNumDocumento(StampeGaaServlet.checkNull(documentoVO.getNumeroDocumento()));
    anagrafico.setEnteRilascio(StampeGaaServlet.checkNull(documentoVO.getEnteRilascioDocumento()));
    
    protocollo.setDocAnagrafico(anagrafico);
    
  }
}