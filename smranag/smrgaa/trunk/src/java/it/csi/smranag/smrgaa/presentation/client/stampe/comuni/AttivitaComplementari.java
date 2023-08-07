package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import inetsoft.report.painter.ImagePainter;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AttivitaComplementari extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/AttivitaComplementari.srt";
  private final static String CODICE_SUB_REPORT = "ATTIVITA_COMPLEMENTARI";

  public AttivitaComplementari() throws IOException, SolmrException
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
    
    
    Vector<CodeDescription> vAttivitaComplementari = anagFacadeClient.getAttivitaComplementariQuadroE(
        anagAziendaVO.getIdAzienda(),
        dataInserimentoDichiarazione);
    int size = vAttivitaComplementari.size();
    boolean primaVoltaQuadroE = true;
    
    
    
    // Recupero l'immagine del check e dell'uncheck
    Image check, unCheck;

    check = Toolkit.getDefaultToolkit().createImage(
        getClass().getClassLoader().getResource(
            SolmrConstants.get("IMMAGINE_PDF_CHECK").toString()));
    
    ImagePainter ipOK = new ImagePainter(check, true)
    {
      private static final long serialVersionUID = 744772809647313019L;

      @Override
      public Dimension getPreferredSize()
      {
        return new Dimension(9,8);
      }
      
      public void paint(Graphics g, int x, int y, int w, int h)
      {
        g.drawImage(getImage(), x + ((w - 9) / 2), y + ((h - 8) / 2), 9, 8, null);
      }
    };
    
    unCheck = Toolkit.getDefaultToolkit().createImage(
        getClass().getClassLoader().getResource(
            SolmrConstants.get("IMMAGINE_PDF_UNCHECK").toString()));
    
    ImagePainter ipNoOK = new ImagePainter(unCheck, true)
    {
      private static final long serialVersionUID = 465969654862922949L;

      @Override
      public Dimension getPreferredSize()
      {
        return new Dimension(9,8);
      }
      
      public void paint(Graphics g, int x, int y, int w, int h)
      {
        g.drawImage(getImage(), x + ((w - 9) / 2), y + ((h - 8) / 2), 9, 8, null);
      }
    };
    
    if (size > 0)
    {
      subReport.removeElement("txtNessunaAttivitaComplementare");
      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID = null, elIDCurrent = null;
      DefaultTableLens tblTemp = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport.getElement("tblQuadroE"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());

      int i = 0;
      boolean dispari = false;
      if (size % 2 != 0)
        dispari = true;
      while (i < size)
      {
        // Recupero le informazioni sul conto corrente da stampare
        CodeDescription code = (CodeDescription) vAttivitaComplementari.get(i);

        elID = "tblQuadroE";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblConti1
        if (primaVoltaQuadroE)
        {
          elIDCurrent = elID;
          primaVoltaQuadroE = false;
        }
        else
        {
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblQuadroDTitolo (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
        tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(code.getDescription()));
        if (code.getCode() != null)
          tblTemp.setObject(0, 0, ipOK);
        else
          tblTemp.setObject(0, 0, ipNoOK);

        i++;

        if (i < size)
        {
          code = (CodeDescription) vAttivitaComplementari.get(i);
          tblTemp.setObject(0, 5, StampeGaaServlet.checkNull(code.getDescription()));
          if (code.getCode() != null)
            tblTemp.setObject(0, 3, ipOK);
          else
            tblTemp.setObject(0, 3, ipNoOK);
          i++;
        }

        tblTemp.setAlignment(0, 0, StyleConstants.H_CENTER);
        tblTemp.setAlignment(0, 3, StyleConstants.H_CENTER);

        tblTemp.setColWidth(0, 13);
        tblTemp.setColWidth(1, 17);
        tblTemp.setColWidth(2, 250);
        tblTemp.setColWidth(3, 13);
        tblTemp.setColWidth(4, 17);
        tblTemp.setColWidth(5, 250);
        
        
        StampeGaaServlet.setNoBorder(tblTemp);

        if (i == size && dispari)
        {
          tblTemp.setObject(0, 5, "");
          tblTemp.setObject(0, 3, "");
          
        }

        subReport.setElement(elIDCurrent, tblTemp);
        
      }
    }
    else
    {
      subReport.removeElement("tblQuadroE");
    }
    
    
    
  }
}