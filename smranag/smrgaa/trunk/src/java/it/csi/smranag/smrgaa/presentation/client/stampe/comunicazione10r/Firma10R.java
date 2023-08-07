package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class Firma10R extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/Firma10R.srt";
  private final static String CODICE_SUB_REPORT = "FIRMA_10R";

  public Firma10R() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    boolean situazioneAttuale = true;
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    DichiarazioneConsistenzaGaaVO  dichConsistenza = (DichiarazioneConsistenzaGaaVO)
      parametri.get("dichConsistenza");
    
    
    
    if(idDichiarazioneConsistenza != null)
    {
      situazioneAttuale = false;
    }
    
    
    DefaultTableLens tblTemp = new DefaultTableLens(subReport.getTable("tblFirma"));
    String firmaAdd = "";
    if (situazioneAttuale)
    {
      firmaAdd += StampeGaaServlet.checkNull(" Repertorio n.              del");
    }
    else
    {
      if (dichConsistenza.getNumeroProtocollo() != null 
            || dichConsistenza.getDataProtocollo() != null)
      {
        firmaAdd += StampeGaaServlet.checkNull(" Repertorio n. ") 
          + StampeGaaServlet.checkNull(dichConsistenza.getNumeroProtocollo()) + " del " 
          + StampeGaaServlet.checkNull(dichConsistenza.getDataProtocollo());
      }
      else
      {
        firmaAdd += StampeGaaServlet.checkNull(" Repertorio n.              del");
      }
    }
    tblTemp.setObject(0, 0, tblTemp.getObject(0, 0) + firmaAdd);

    StampeGaaServlet.setNoBorder(tblTemp);
    subReport.setElement("tblFirma", tblTemp);
    
    
    subReport.setElement("txtNomeProduttore", StampeGaaServlet.checkNull(anagAziendaVO.getDenominazione()));
    
    
  }
}