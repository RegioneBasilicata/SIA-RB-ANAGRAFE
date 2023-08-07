package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.DocContoCorrente;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

public class DocumentoContoCorrente extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_CONTO_CORRENTE";

  public DocumentoContoCorrente() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
       HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
   
    ContoCorrenteVO contoCorrenteVO = anagFacadeClient
      .getContoCorrente(documentoVO.getIdContoCorrente().toString());
  
    if (contoCorrenteVO != null)
    {
      DocContoCorrente contoCorrente = new DocContoCorrente();

      
      contoCorrente.setDenominazioneBanca(StampeGaaServlet.checkNull(
              contoCorrenteVO.getDenominazioneBanca()));
      contoCorrente.setDenominazioneSportello(StampeGaaServlet.checkNull(
              contoCorrenteVO.getDenominazioneSportello()));
      contoCorrente.setIban(StampeGaaServlet.checkNull(
          contoCorrenteVO.getIban()));
      contoCorrente.setDataInizioValiditaContoCorrente(contoCorrenteVO.getDataInizioValiditaContoCorrente());
      contoCorrente.setDataEstinzione(contoCorrenteVO.getDataEstinzione());
      
      Date dataFineValidita = contoCorrenteVO.getDataEstinzione();
      if (dataFineValidita == null)
      {
        dataFineValidita = contoCorrenteVO.getDataFineValiditaContoCorrente();
      }
      contoCorrente.setDataFineValiditaContoCorrente(dataFineValidita);
      protocollo.setDocContoCorrente(contoCorrente);
    }
  }
}