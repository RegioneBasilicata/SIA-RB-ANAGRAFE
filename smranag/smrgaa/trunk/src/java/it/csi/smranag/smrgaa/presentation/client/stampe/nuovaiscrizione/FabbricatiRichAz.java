package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.PartFabbrAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class FabbricatiRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/FabbricatiRichAz.srt";
  private final static String CODICE_SUB_REPORT = "FABBRICATI_RICH_AZ";

  public FabbricatiRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idAziendaNuova = (Long)request.getSession().getAttribute("idAziendaNuova");
    Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova = 
        gaaFacadeClient.getFabbrAziendaNuovaIscrizione(idAziendaNuova.longValue());
    
    
    
    TabularSheet layout = (TabularSheet) subReport;
    int size = 0;
    if(vFabbrAziendaNuova != null)
      size = vFabbrAziendaNuova.size();
    
    if (size > 0)
    {
      subReport.removeElement("txtNoFabbricati");

      FabbricatoAziendaNuovaVO fabbricato = null;
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
        fabbricato = vFabbrAziendaNuova.get(i);

        // Clonare tutto
        elID = "tblFabbricatoRiga1";
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        tblFabbricato = new DefaultTableLens(subReport.getTable(elIDCurrent));

        tblFabbricato.setColWidth(0, 73);
        tblFabbricato.setColWidth(1, 200);

        tblFabbricato.setObject(0, 1, StampeGaaServlet.checkNull(fabbricato
            .getDescFabbricato()));

        StampeGaaServlet.setNoBorder(tblFabbricato);

        tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);

        subReport.setElement(elIDCurrent, tblFabbricato);


        elID = "tblFabbricatoRiga2";
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

        tblFabbricato.setObject(0, 1, Formatter.formatDouble1(fabbricato
            .getSuperficie()));
        tblFabbricato.setObject(0, 2, StampeGaaServlet.checkNull("Dimensione ("
            + fabbricato.getUnitaMisura() + "):"));
        tblFabbricato.setObject(0, 3, Formatter.formatDouble1(fabbricato
            .getDimensione()));
        tblFabbricato
            .setObject(0, 5, fabbricato.getAnnoCostruzione());

        StampeGaaServlet.setNoBorder(tblFabbricato);

        tblFabbricato.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblFabbricato.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);

        subReport.setElement(elIDCurrent, tblFabbricato);

        

        int numParticelle = 0;
        Vector<PartFabbrAziendaNuovaVO> particelle = fabbricato.getvPartFabbrAziendaNuova();
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
          
          tblFabbricato.setAlignment(0, 1, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 2, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 3, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 4, StyleConstants.H_CENTER);
          tblFabbricato.setAlignment(0, 5, StyleConstants.H_CENTER);

          for (int j = 0; j < numParticelle; j++)
          {
            int posRiga = j + 1;
            PartFabbrAziendaNuovaVO pV = particelle.get(j);
            tblFabbricato.insertRow(j + 1);

            tblFabbricato.setObject(posRiga, 0, StampeGaaServlet.checkNull(pV
                .getSglProv()));
            tblFabbricato.setObject(posRiga, 1, StampeGaaServlet.checkNull(pV
                .getDesCom()));
            tblFabbricato.setObject(posRiga, 2, StampeGaaServlet.checkNull(pV.getSezione()));
            tblFabbricato.setObject(posRiga, 3, StampeGaaServlet.checkNull(pV.getStrFoglio()));
            tblFabbricato.setObject(posRiga, 4,
                StampeGaaServlet.checkNull(pV.getStrParticella()));
            tblFabbricato.setObject(posRiga, 5, Formatter.formatDouble4(pV
                .getSuperficie()));

            // Allineamento celle
            tblFabbricato.setAlignment(posRiga, 2, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 3, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 4, StyleConstants.H_CENTER);
            tblFabbricato.setAlignment(posRiga, 5, StyleConstants.H_RIGHT);

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
    subReport.removeElement("tblParticelleInsiste");
    subReport.removeElement("tblParticelleFabbricato");
    
    
    
  }
}