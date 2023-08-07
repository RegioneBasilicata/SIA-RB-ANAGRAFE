package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Anomalie extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Anomalie.srt";
  private final static String CODICE_SUB_REPORT = "ANOMALIE";

  public Anomalie() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    TabularSheet layout = (TabularSheet) subReport;
    
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    Vector<String[]> anomalie = anagFacadeClient.getAnomalie(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza);
    int size = anomalie.size();
    
    if (size > 0)
    {
      subReport.removeElement("txtNoAnomalie");
    }
    if (size == 0
        || idDichiarazioneConsistenza != null)
    {
      subReport.removeElement("txtUltimoControllo");
      subReport.removeElement("nlUltimoControllo");
    }

    
    
    int nRow = 0;
    String[] anomalia = null;
    DefaultTableLens tblAnomalie = null;

    // Variabili utili per la clonazione
    String oldTipoControllo = null, newTipoControllo = null;
    String elID = null, elIDCurrent = null;
    ReportElement element = null;
    // Recupero le coordinate della cella in cui mi trovo
    Point elStartCell = layout.getElementCell(subReport.getElement("tblAnomalie"));
    int elStartRow = (int) Math.round(elStartCell.getY());
    int elStartCol = (int) Math.round(elStartCell.getX());

    int i = 0;
    for (i = 0; i < size; i++)
    {
      anomalia = (String[]) anomalie.get(i);
      newTipoControllo = anomalia[0];
      if (!newTipoControllo.equals(oldTipoControllo))
      {
        if (elIDCurrent != null && tblAnomalie != null)
        {
          subReport.setElement(elIDCurrent, tblAnomalie);
        }
        
        // Clonare tutto e impostare txtAnomalieTipoControllo
        elID = "spAnomalie1";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);        
        
        elID = "nlAnomalie1";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        

        elID = "txtAnomalieTipoControllo";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        subReport.setElement(elIDCurrent, anomalia[0]);
        if (i == 0
            && idDichiarazioneConsistenza == null)
          subReport.setElement("txtUltimoControllo",
              "Ultimo controllo effettuato il " + anomalia[4]);

        elID = "tblAnomalie";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        tblAnomalie = new DefaultTableLens(subReport.getTable(elIDCurrent));
        tblAnomalie.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblAnomalie.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblAnomalie.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblAnomalie.setColWidth(0, 120);
        tblAnomalie.setColWidth(1, 300);
        tblAnomalie.setColWidth(2, 150);
        
        
        
      }
      oldTipoControllo = newTipoControllo;

      tblAnomalie.addRow();
      nRow = tblAnomalie.getRowCount() - 1;
      tblAnomalie.setObject(nRow, 0, StampeGaaServlet.checkNull(anomalia[1]));
      tblAnomalie.setObject(nRow, 1, StampeGaaServlet.checkNull(anomalia[3]));
      tblAnomalie.setObject(nRow, 2, StampeGaaServlet.checkNull(anomalia[5]));
    }
    if (elIDCurrent != null && tblAnomalie != null)
    {
      subReport.setElement(elIDCurrent, tblAnomalie);
    }
    
    // Clonare tutto e impostare txtAnomalieTipoControllo
    i++;
    elID = "spAnomalie1";
    elIDCurrent = elID + i;
    element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
    layout.addElement(elStartRow, elStartCol, element);        
    
    elID = "nlAnomalie1";
    elIDCurrent = elID + i;
    element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
    layout.addElement(elStartRow, elStartCol, element);
    
    subReport.removeElement("tblAnomalie");
    subReport.removeElement("txtAnomalieTipoControllo");
    
    
    
    
    
    
    
  }
}