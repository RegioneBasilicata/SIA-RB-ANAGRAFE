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
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
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

public class ConduzioneAziendale extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/ConduzioneAziendale.srt";
  private final static String CODICE_SUB_REPORT = "CONDUZIONE_AZIENDALE";

  public ConduzioneAziendale() throws IOException, SolmrException
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
    
    
    Vector<TipoFormaConduzioneVO> vTipoFormaConduzione = anagFacadeClient.getFormeConduzioneQuadroD();
    int size = vTipoFormaConduzione.size();
    boolean primaVoltaQuadroDTitolo = true;
    boolean primaVoltaQuadroDForme = true;
    if (size > 0)
    {
      Long id = anagFacadeClient.getFormaConduzioneQuadroD(
          anagAziendaVO.getIdAzienda(),
          dataInserimentoDichiarazione);
      if (id == null)
      {
        subReport.removeElement("tblQuadroDTitolo");
        subReport.removeElement("tblQuadroDForme");
        return;
      }
      
      subReport.removeElement("txtNessunaFormaConduzione");
      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID = null, elIDCurrent = null;
      DefaultTableLens tblTemp = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout
          .getElementCell(subReport.getElement("tblQuadroDTitolo"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());
      String formaOld = "#@@#";
      
      
      // Recupero l'immagine del check e dell'uncheck
      Image check, unCheck;

      check = Toolkit.getDefaultToolkit().createImage(
          getClass().getClassLoader().getResource(
              SolmrConstants.get("IMMAGINE_PDF_CHECK").toString()));
      
      ImagePainter ipOK = new ImagePainter(check, true)
      {
        private static final long serialVersionUID = 1L;

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
        private static final long serialVersionUID = -3642910455635621335L;
        

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

      int i = 0;
      while (i < size)
      {
        // Recupero le informazioni sul conto corrente da stampare
        TipoFormaConduzioneVO forma = (TipoFormaConduzioneVO) vTipoFormaConduzione
            .get(i);

        if (!formaOld.equals(forma.getForma()))
        {
          elID = "tblQuadroDTitolo";
          // Se siamo al primo ciclo, non clono nulla
          // altrimenti clono tblConti1
          if (primaVoltaQuadroDTitolo)
          {
            elIDCurrent = elID;
            primaVoltaQuadroDTitolo = false;
          }
          else
          {
            elIDCurrent = elID + i;
            element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
            layout.addElement(elStartRow, elStartCol, element);
          }
          // Popolo tblQuadroDTitolo (o il suo i-esimo clone)
          tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
          StampeGaaServlet.setNoBorder(tblTemp);
          tblTemp.setObject(0, 0, StampeGaaServlet.checkNull(forma.getForma()));
          layout.setElement(elIDCurrent, tblTemp);
      
        }
        formaOld = forma.getForma();

        elID = "tblQuadroDForme";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblQuadroDForme
        if (primaVoltaQuadroDForme)
        {
          primaVoltaQuadroDForme = false;
          elIDCurrent = elID;
        }
        else
        {
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblQuadroDForme (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
      
        try
        {
          if (id.longValue() == forma.getIdFormaConduzioneLong().longValue())
            tblTemp.setObject(0, 0, ipOK);
          else
            tblTemp.setObject(0, 0, ipNoOK);
        }
        catch (Exception e)
        {
          tblTemp.setObject(0, 0, "");
        }
        tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(forma.getDescrizione()));

        tblTemp.setColWidth(0, 13);
        tblTemp.setColWidth(1, 17);
        tblTemp.setColWidth(2, 300);
        
        StampeGaaServlet.setNoBorder(tblTemp);

        tblTemp.setAlignment(0, 0, StyleConstants.H_CENTER);

        subReport.setElement(elIDCurrent, tblTemp);

        
        i++;
      }
    }
    else
    {
      subReport.removeElement("tblQuadroDTitolo");
      subReport.removeElement("tblQuadroDForme");
    }
    
    
    
  }
}