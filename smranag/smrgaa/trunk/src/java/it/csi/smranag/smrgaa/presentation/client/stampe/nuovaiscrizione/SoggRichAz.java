package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smrcomms.reportdin.util.DateUtils;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class SoggRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/SoggRichAz.srt";
  private final static String CODICE_SUB_REPORT = "SOGG_RICH_AZ";

  public SoggRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idAziendaNuova = (Long)parametri.get("idAziendaNuova");
    
    Vector<SoggettoAziendaNuovaVO> vSoggettiAziendaNuova = gaaFacadeClient.getSoggettiAziendaNuovaIscrizione(idAziendaNuova.longValue());
    
    if (vSoggettiAziendaNuova != null 
      && vSoggettiAziendaNuova.size() > 0)
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunSoggetto");

      DefaultTableLens tblElencoSoggetti = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblElencoSoggetti = new DefaultTableLens(subReport.getTable("tblElencoSoggetti"));

      tblElencoSoggetti.setColWidth(0, 80);
      tblElencoSoggetti.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(1, 80);
      tblElencoSoggetti.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(2, 100);
      tblElencoSoggetti.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(3, 80);
      tblElencoSoggetti.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(4, 80);
      tblElencoSoggetti.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(5, 70);
      tblElencoSoggetti.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblElencoSoggetti.setColWidth(6, 70);
      tblElencoSoggetti.setFont(0, 6, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblElencoSoggetti.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 3, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 4, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 5, StyleConstants.H_CENTER);
      tblElencoSoggetti.setAlignment(0, 6, StyleConstants.H_CENTER);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < vSoggettiAziendaNuova.size(); i++)
      {
        tblElencoSoggetti.addRow();
        SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = vSoggettiAziendaNuova.get(i);
        tblElencoSoggetti.setObject(i + 1, 0, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getCognome()));
        tblElencoSoggetti.setObject(i + 1, 1, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getNome()));
        tblElencoSoggetti.setObject(i + 1, 2, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getCodiceFiscale()));
        tblElencoSoggetti.setObject(i + 1, 3, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getDescTipoRuolo()));
        if(Validator.isNotEmpty(soggettoAziendaNuovaVO.getDataInizioRuolo()))
          tblElencoSoggetti.setObject(i + 1, 4, DateUtils.formatDate(soggettoAziendaNuovaVO.getDataInizioRuolo()));
        else
          tblElencoSoggetti.setObject(i + 1, 4, "");
        tblElencoSoggetti.setAlignment(i + 1, 4, StyleConstants.H_CENTER);
        tblElencoSoggetti.setObject(i + 1, 5, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getTelefono()));
        tblElencoSoggetti.setObject(i + 1, 6, StampeGaaServlet.checkNull(soggettoAziendaNuovaVO.getEmail()));        
      }
      
      subReport.setElement("tblElencoSoggetti", tblElencoSoggetti);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblElencoSoggetti");
    }
    
    
    
  }
}