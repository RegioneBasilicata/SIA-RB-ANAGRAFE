package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocZootecnico;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

public class DocumentoZootecnicoDett extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_ZOOTECNICO_DETT";

  public DocumentoZootecnicoDett() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");

    DocZootecnico docZootecnico = new DocZootecnico();
    docZootecnico.setCuaa(StampeGaaServlet.checkNull(documentoVO.getCuaaSoccidario()));
    protocollo.setDocZootecnico(docZootecnico);
  }
}