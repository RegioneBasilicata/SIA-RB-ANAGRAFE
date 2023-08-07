package it.csi.smranag.smrgaa.presentation.client.stampe.documento;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class HeaderStampaDoc extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/documento/HeaderStampaDoc.srt";
  private final static String CODICE_SUB_REPORT = "HEADER_STAMPA_DOC";

  public HeaderStampaDoc() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  { 
    
    TabularSheet layout = (TabularSheet) subReport;   
    
    ReportElement element = null;
    int elStartRow, elStartCol;
    Point elStartCell;
    String elID, elIDCurrent;
    DefaultTableLens tblTitolo = null;

    // Recupero le coordinate della cella in cui mi trovo
    elStartCell = layout.getElementCell(subReport.getElement("tblTitolo"));
    elStartRow = (int) Math.round(elStartCell.getY());
    elStartCol = (int) Math.round(elStartCell.getX());

    elID = "tblTitolo";
    elIDCurrent = elID;
    element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
    subReport.removeElement("tblTitolo");
    layout.addElement(elStartRow, elStartCol, element);
    tblTitolo = new DefaultTableLens(subReport.getTable(elIDCurrent));
    
    tblTitolo.setColWidth(0, 600);  
    
    StampeGaaServlet.setNoBorder(tblTitolo);
    
    tblTitolo.setFont(StampeGaaServlet.FONT_SERIF_BOLD_17);
     
    tblTitolo.addRow();    
    String idTipoReport = (String)parametri.get("idTipoReport");
    TipoReportVO tipoReportVO = gaaFacadeClient.getTipoReport(new Long(idTipoReport).longValue());
    tblTitolo.setAlignment(0, 0, StyleConstants.H_CENTER);
    tblTitolo.setObject(0, 0, StampeGaaServlet.checkNull(tipoReportVO.getDescrizione()));
    
    if(Validator.isNotEmpty(richiestaTipoReportVO.getNote()))
    {
      tblTitolo.addRow();    
      tblTitolo.setAlignment(1, 0, StyleConstants.H_CENTER);
      tblTitolo.setObject(1, 0, StampeGaaServlet.checkNull(richiestaTipoReportVO.getNote()));
    }
    
    
    
    subReport.setElement(elIDCurrent, tblTitolo);
    
    
    
    
    
  }

}