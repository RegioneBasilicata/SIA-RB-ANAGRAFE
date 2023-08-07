package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Ute extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Ute.srt";
  private final static String CODICE_SUB_REPORT = "UTE";

  public Ute() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    Vector<UteVO> utes = anagFacadeClient.getUteQuadroB(anagAziendaVO
        .getIdAzienda(), dataInserimentoDichiarazione);
    int num = utes.size();
    if (num > 0)
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunaUTE");

      DefaultTableLens tblUTE = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblUTE = new DefaultTableLens(subReport.getTable("tblUTE"));

      tblUTE.setColWidth(0, 120);
      tblUTE.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUTE.setColWidth(1, 170);
      tblUTE.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUTE.setColWidth(2, 30);
      tblUTE.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUTE.setColWidth(3, 120);
      tblUTE.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUTE.setColWidth(4, 50);
      tblUTE.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblUTE.setAlignment(0, 3, StyleConstants.H_CENTER);
      tblUTE.setAlignment(0, 4, StyleConstants.H_CENTER);
      tblUTE.setAlignment(0, 5, StyleConstants.H_CENTER);
      tblUTE.setColAlignment(3, StyleConstants.H_CENTER);
      tblUTE.setColAlignment(4, StyleConstants.H_CENTER);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < num; i++)
      {
        tblUTE.addRow();
        UteVO ute = (UteVO) utes.get(i);
        tblUTE.setObject(i + 1, 0, StampeGaaServlet.checkNull(ute.getComune() + " (" + ute.getProvincia() + ")"));
        tblUTE.setObject(i + 1, 1, StampeGaaServlet.checkNull(ute.getIndirizzo()));
        tblUTE.setObject(i + 1, 2, StampeGaaServlet.checkNull(ute.getCap()));
        tblUTE.setAlignment(i + 1, 2, StyleConstants.H_CENTER);
        tblUTE.setObject(i + 1, 3, StampeGaaServlet.checkNull(ute.getZonaAltimetrica()));
        tblUTE.setAlignment(i + 1, 3, StyleConstants.H_CENTER);
        tblUTE.setObject(i + 1, 4, StampeGaaServlet.checkNull(ute.getDataInizioAttivitaStr()));
        tblUTE.setAlignment(i + 1, 4, StyleConstants.H_RIGHT);
      }
      
      subReport.setElement("tblUTE", tblUTE);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblUTE");
    }
    
    
    
  }
}