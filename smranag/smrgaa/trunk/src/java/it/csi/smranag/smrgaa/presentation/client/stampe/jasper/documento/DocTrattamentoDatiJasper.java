package it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.documento.DocTrattamentoDati;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.SubReportJasper;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

public class DocTrattamentoDatiJasper extends SubReportJasper
{
  private final static String CODICE_SUB_REPORT = "DOC_TRATTAMENTO_DATI";

  public DocTrattamentoDatiJasper() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(Protocollo protocollo, RichiestaTipoReportVO richiestaTipoReportVO,
      HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());

    DocTrattamentoDati docTrattamentoDati = new DocTrattamentoDati();
    
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      for(int i=0;i<vAttestazioneVO.size();i++)
      {        
    	  docTrattamentoDati.addDichiarazione(StampeGaaServlet.checkNull(vAttestazioneVO.get(i).getDescrizione()));
      }
      
    }
    
  }
  
}