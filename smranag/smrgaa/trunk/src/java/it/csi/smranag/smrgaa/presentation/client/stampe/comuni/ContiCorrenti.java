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
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.exception.SolmrException;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ContiCorrenti extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/ContiCorrenti.srt";
  private final static String CODICE_SUB_REPORT = "CONTI_CORRENTI";

  public ContiCorrenti() throws IOException, SolmrException
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
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    Vector<ContoCorrenteVO> contiCorrenti = gaaFacadeClient.getStampaContiCorrenti(
        anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    int size = contiCorrenti.size();
    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di conti associati all'azienda
      subReport.removeElement("txtNessunConto");

      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID = null, elIDCurrent = null;
      DefaultTableLens tblTemp = null;
      ContoCorrenteVO ccVO = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport.getElement("tblConti1"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());

      for (int i = 0; i < size; i++)
      {
        // Recupero le informazioni sul conto corrente da stampare
        ccVO = (ContoCorrenteVO) (contiCorrenti.get(i));

        elID = "tblConti1";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblConti1
        if (i == 0)
          elIDCurrent = elID;
        else
        {
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblConti1 (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
        StampeGaaServlet.setBorderRiquadri(tblTemp);
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(ccVO.getCodPaese()));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(ccVO.getCifraCtrl()));
        tblTemp.setObject(1, 4, StampeGaaServlet.checkNull(ccVO.getCin()));
        tblTemp.setObject(1, 6, StampeGaaServlet.checkNull(ccVO.getAbi()));
        tblTemp.setObject(1, 8, StampeGaaServlet.checkNull(ccVO.getCab()));
        tblTemp.setObject(1, 10, StampeGaaServlet.checkNull(ccVO.getNumeroContoCorrente()));
        tblTemp.setObject(1, 12, StampeGaaServlet.checkNull(ccVO.getIban()));
        subReport.setElement(elIDCurrent, tblTemp);

        // Se siamo oltre il primo ciclo
        // clono nlConti1, il newline situato tra tblConti1 e tblConti2
        if (i > 0)
        {
          elID = "nlConti1";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }

        elID = "tblConti2";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblConti2
        if (i == 0)
          elIDCurrent = elID;
        else
        {
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblConti2 (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
        StampeGaaServlet.setBorderRiquadri(tblTemp);
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(ccVO.getDenominazioneBanca()));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(ccVO.getDenominazioneSportello()));
        layout.setElement(elIDCurrent, tblTemp);

        // clono nlConti2, il newline situato dopo tblConti2
        if (i > 0)
        {
          elID = "nlConti2";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }

        elID = "tblConti3";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblConti2
        if (i == 0)
          elIDCurrent = elID;
        else
        {
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblConti2 (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
        StampeGaaServlet.setBorderRiquadri(tblTemp);
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(ccVO.getIndirizzoSportello()));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(ccVO.getDescrizioneComuneSportello()));
        tblTemp.setObject(1, 4, StampeGaaServlet.checkNull(ccVO.getSiglaProvincia()));
        tblTemp.setObject(1, 6, StampeGaaServlet.checkNull(ccVO.getCapSportello()));
        layout.setElement(elIDCurrent, tblTemp);

        // clono nlConti3, il newline situato dopo tblConti3
        if (i > 0)
        {
          elID = "nlConti3";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }

      }
    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblConti1");
      subReport.removeElement("nlConti1");
      subReport.removeElement("tblConti2");
      subReport.removeElement("nlConti2");
      subReport.removeElement("tblConti3");
    }

    
    
    
    
    
    
  }
}