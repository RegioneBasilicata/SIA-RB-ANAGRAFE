package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ConsistenzaZootecnica10R extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/ConsistenzaZootecnica10R.srt";
  private final static String CODICE_SUB_REPORT = "CONSISTENZA_ZOOTECNICA_10R";

  public ConsistenzaZootecnica10R() throws IOException, SolmrException
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
    
    
    Vector<ConsistenzaZootecnicaStampa> cons = anagFacadeClient
      .getAllevamentiQuadroC10R(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    int size = cons.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di consistenze zootecniche
      // associate all'azienda
      subReport.removeElement("txtNessunaConsistenza");
      subReport.removeElement("nlConsZootecnica0");

      DefaultTableLens tblConsZootecnica = new DefaultTableLens(subReport.getTable("tblConsZootecnica"));

      tblConsZootecnica.setRowCount(size + 1);// +1 necessario per l'header

      //tblConsZootecnica.setRowBackground(0, new Color(-4137792));

      // Imposto l'header
      int col = 0;
      tblConsZootecnica.setObject(0, col, StampeGaaServlet.checkNull("UTE"));
      tblConsZootecnica.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZootecnica.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblConsZootecnica.setColWidth(0, 210);
      
      tblConsZootecnica.setObject(0, col, StampeGaaServlet.checkNull("Codice\nazienda\nzootecnica"));
      tblConsZootecnica.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZootecnica.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblConsZootecnica.setColWidth(1, 50);
      
      tblConsZootecnica.setObject(0, col, StampeGaaServlet.checkNull("Specie"));
      tblConsZootecnica.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZootecnica.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblConsZootecnica.setColWidth(2, 160);
      
      tblConsZootecnica.setObject(0, col, StampeGaaServlet.checkNull("Totale capi"));
      tblConsZootecnica.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZootecnica.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblConsZootecnica.setColWidth(3, 40);

      for (int i = 0; i < size; i++)
      {
        ConsistenzaZootecnicaStampa temp = (ConsistenzaZootecnicaStampa) cons.get(i);
        col = 0;
        tblConsZootecnica.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getComuneProvUTE()));
        tblConsZootecnica.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getCodiceAziendaZootecnica()));
        tblConsZootecnica.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescrizioneSpecie()));
        tblConsZootecnica.setObject(i + 1, col, "" + temp.getTotaleCapi());
        tblConsZootecnica.setAlignment(i + 1, col++, StyleConstants.H_CENTER);
      }
      subReport.setElement("tblConsZootecnica", tblConsZootecnica);
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblConsZootecnica");
    }
    
    
    
    
    
    
    
    
  }
}