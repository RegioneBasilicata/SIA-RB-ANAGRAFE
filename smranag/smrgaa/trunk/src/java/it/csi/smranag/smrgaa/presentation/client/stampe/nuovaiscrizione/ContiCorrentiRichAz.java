package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ContiCorrentiRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/ContiCorrentiRichAz.srt";
  private final static String CODICE_SUB_REPORT = "CONTI_CORRENTI_RICH_AZ";

  public ContiCorrentiRichAz() throws IOException, SolmrException
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
    
    Long idAziendaNuova = (Long)parametri.get("idAziendaNuova");
    
    
    Vector<CCAziendaNuovaVO> vCCAziendaNuova = gaaFacadeClient
        .getCCAziendaNuovaIscrizione(idAziendaNuova.longValue());
   
    if ((vCCAziendaNuova !=null) && (vCCAziendaNuova.size() > 0))
    {
      // Rimuovo il testo indicante l'assenza di conti associati all'azienda
      subReport.removeElement("txtNessunConto");

      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID = null, elIDCurrent = null;
      DefaultTableLens tblTemp = null;
      CCAziendaNuovaVO ccVO = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport.getElement("tblConti1"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());

      for (int i = 0; i < vCCAziendaNuova.size(); i++)
      {
        // Recupero le informazioni sul conto corrente da stampare
        ccVO = vCCAziendaNuova.get(i);

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
        String iban = ccVO.getIban();
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(iban.substring(0, 2)));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(iban.substring(2, 4)));
        tblTemp.setObject(1, 4, StampeGaaServlet.checkNull(iban.substring(4, 5)));
        tblTemp.setObject(1, 6, StampeGaaServlet.checkNull(iban.substring(5, 10)));
        tblTemp.setObject(1, 8, StampeGaaServlet.checkNull(iban.substring(10, 15)));
        tblTemp.setObject(1, 10, StampeGaaServlet.checkNull(iban.substring(15, 27)));
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
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(ccVO.getDescBanca()));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(ccVO.getDescFiliale()));
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
        tblTemp.setObject(1, 0, StampeGaaServlet.checkNull(ccVO.getIndirizzoFiliale()));
        tblTemp.setObject(1, 2, StampeGaaServlet.checkNull(ccVO.getDescComuneFiliale()));
        tblTemp.setObject(1, 4, StampeGaaServlet.checkNull(ccVO.getSglProvFiliale()));
        tblTemp.setObject(1, 6, StampeGaaServlet.checkNull(ccVO.getCapFiliale()));
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