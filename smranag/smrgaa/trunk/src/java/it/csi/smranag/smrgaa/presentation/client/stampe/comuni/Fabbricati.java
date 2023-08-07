package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Fabbricati extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Fabbricati.srt";
  private final static String CODICE_SUB_REPORT = "FABBRICATI";

  public Fabbricati() throws IOException, SolmrException
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
    
    
    Vector<FabbricatoVO> fabbricati = anagFacadeClient
      .getFabbricati(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione, false); 
    int size = fabbricati.size();
    if (size > 0)
    {
      subReport.removeElement("txtNoFabbricati");

      FabbricatoVO fabbricato = null;
      DefaultTableLens tblFabbricato = null;

      // Variabili utili per la clonazione
      String elID = null, elIDCurrent = null;
      ReportElement element = null;
      // Recupero le coordinate della cella in cui mi trovo
      Point elStartCell = layout.getElementCell(subReport
          .getElement("tblFabbricatoRiga1"));
      int elStartRow = (int) Math.round(elStartCell.getY());
      int elStartCol = (int) Math.round(elStartCell.getX());

      for (int i = 0; i < size; i++)
      {
        fabbricato = (FabbricatoVO) fabbricati.get(i);

        // Clonare tutto
        elID = "tblFabbricatoRiga1";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));

        tblFabbricato.setColWidth(0, 97);
        tblFabbricato.setColWidth(1, 185);
        tblFabbricato.setColWidth(2, 73);
        tblFabbricato.setColWidth(3, 200);

        tblFabbricato.setObject(0, 1, StampeGaaServlet.checkNull(fabbricato.getDescComuneUte()));
        tblFabbricato.setObject(0, 3, StampeGaaServlet.checkNull(fabbricato
            .getDescrizioneTipoFabbricato()));

        StampeGaaServlet.setNoBorder(tblFabbricato);

        tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);

        subReport.setElement(elIDCurrent, tblFabbricato);

        if (fabbricato.getTipologiaColturaSerra() != null
            && !"".equals(fabbricato.getTipologiaColturaSerra()))
        {
          elID = "tblFabbricatoRiga2";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));

          tblFabbricato.setColWidth(0, 110);
          tblFabbricato.setColWidth(1, 142);
          tblFabbricato.setColWidth(2, 73);
          tblFabbricato.setColWidth(3, 250);

          tblFabbricato.setObject(0, 1, StampeGaaServlet.checkNull(fabbricato
              .getDescrizioneTipologiaColturaSerra()));
          tblFabbricato.setObject(0, 3, StampeGaaServlet.checkNull(fabbricato
              .getDescrizioneTipoFormaFabbricato()));

          StampeGaaServlet.setNoBorder(tblFabbricato);

          tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);

          subReport.setElement(elIDCurrent, tblFabbricato);
        }

        elID = "tblFabbricatoRiga3";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));

        tblFabbricato.setColWidth(0, 70);
        tblFabbricato.setColWidth(1, 70);
        tblFabbricato.setAlignment(0, 1, StyleConstants.H_LEFT);
        tblFabbricato.setColWidth(2, 78);
        tblFabbricato.setColWidth(3, 63);
        tblFabbricato.setAlignment(0, 3, StyleConstants.H_LEFT);
        tblFabbricato.setColWidth(4, 205);
        tblFabbricato.setColWidth(5, 45);
        tblFabbricato.setAlignment(0, 5, StyleConstants.H_LEFT);

        tblFabbricato.setObject(0, 1, StringUtils.parseDoubleField(fabbricato
            .getSuperficieFabbricato()));
        tblFabbricato.setObject(0, 2, StampeGaaServlet.checkNull("Dimensione ("
            + fabbricato.getUnitaMisura() + "):"));
        tblFabbricato.setObject(0, 3, StringUtils.parseDoubleField(fabbricato
            .getDimensioneFabbricato()));
        tblFabbricato
            .setObject(0, 5, fabbricato.getAnnoCostruzioneFabbricato());

        StampeGaaServlet.setNoBorder(tblFabbricato);

        tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);

        subReport.setElement(elIDCurrent, tblFabbricato);

        if (fabbricato.getTipologiaColturaSerra() != null
            && !"".equals(fabbricato.getTipologiaColturaSerra()))
        {
          elID = "tblFabbricatoRiga4";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));

          tblFabbricato.setColWidth(0, 140);
          tblFabbricato.setColWidth(1, 112);
          tblFabbricato.setColWidth(2, 123);
          tblFabbricato.setColWidth(3, 200);

          tblFabbricato.setObject(0, 1,
              StampeGaaServlet.checkNull(fabbricato.getMesiRiscSerra()));
          tblFabbricato.setObject(0, 3, StampeGaaServlet.checkNull(fabbricato.getOreRisc()));

          StampeGaaServlet.setNoBorder(tblFabbricato);

          tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);

          subReport.setElement(elIDCurrent, tblFabbricato);
        }

        int numParticelle = 0;
        Vector<ParticellaVO> particelle = anagFacadeClient.getFabbricatiParticelle(fabbricato
            .getIdFabbricato(), dataInserimentoDichiarazione);
        if (particelle != null)
          numParticelle = particelle.size();
        if (numParticelle > 0)
        {
          elID = "tblParticelleInsiste";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));
          StampeGaaServlet.setNoBorder(tblFabbricato);
          tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);

          subReport.setElement(elIDCurrent, tblFabbricato);

          elID = "tblParticelleFabbricato";
          elIDCurrent = elID + i;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));
          tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(0, 25);
          tblFabbricato.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(1, 200);
          tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(2, 45);
          tblFabbricato.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(3, 45);
          tblFabbricato.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(4, 45);
          tblFabbricato.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(5, 45);
          tblFabbricato.setFont(0, 6, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblFabbricato.setColWidth(6, 70);
          
          tblFabbricato.setAlignment(0, 2, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 3, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 4, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 5, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 6, StyleConstants.H_CENTER);

          for (int j = 0; j < numParticelle; j++)
          {
            int posRiga = j + 1;
            ParticellaVO pV = (ParticellaVO) particelle.get(j);
            tblFabbricato.insertRow(j + 1);

            tblFabbricato.setObject(posRiga, 0, StampeGaaServlet.checkNull(pV
                .getSiglaProvinciaParticella()));
            tblFabbricato.setObject(posRiga, 1, StampeGaaServlet.checkNull(pV
                .getDescComuneParticella()));
            tblFabbricato.setObject(posRiga, 2, StampeGaaServlet.checkNull(pV.getSezione()));
            tblFabbricato.setObject(posRiga, 3, StampeGaaServlet.checkNull(pV.getStrFoglio()));
            tblFabbricato.setObject(posRiga, 4,
                StampeGaaServlet.checkNull(pV.getStrParticella()));
            tblFabbricato.setObject(posRiga, 5, StampeGaaServlet.checkNull(pV.getSubalterno()));
            tblFabbricato.setObject(posRiga, 6, StringUtils.parseDoubleField(pV
                .getSupCatastale()));

            // Allineamento celle
            tblFabbricato.setAlignment(posRiga, 2, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 3, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 4, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 5, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 6, StyleConstants.H_RIGHT);

          }

          subReport.setElement(elIDCurrent, tblFabbricato);
        }

        elID = "spFabbricato";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        elID = "nlFabbricato";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);

      }

    }
    
    subReport.removeElement("tblFabbricatoRiga1");
    subReport.removeElement("tblFabbricatoRiga2");
    subReport.removeElement("tblFabbricatoRiga3");
    subReport.removeElement("tblFabbricatoRiga4");
    subReport.removeElement("tblParticelleInsiste");
    subReport.removeElement("tblParticelleFabbricato");
    
    
    
  }
}