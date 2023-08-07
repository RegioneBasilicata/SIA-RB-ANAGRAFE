package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Fabbricati10R extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/Fabbricati10R.srt";
  private final static String CODICE_SUB_REPORT = "FABBRICATI_10R";

  public Fabbricati10R() throws IOException, SolmrException
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
    
    
    Vector<FabbricatoVO> fabbricati = anagFacadeClient
      .getFabbricati(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione, true);
    int size = fabbricati.size();
    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di consistenze zootecniche
      // associate all'azienda
      subReport.removeElement("txtNoFabbricati");
      subReport.removeElement("nlFabbricati");

      DefaultTableLens tblFabbricato = new DefaultTableLens(subReport.getTable("tblFabbricato"));

      tblFabbricato.setRowCount(size + 1);// +1 necessario per l'header

      //tblFabbricato.setRowBackground(0, new Color(-4137792));

      // Imposto l'header
      int col = 0;
      tblFabbricato.setObject(0, col, StampeGaaServlet.checkNull("UTE"));
      tblFabbricato.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblFabbricato.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblFabbricato.setColWidth(0, 150);
      
      tblFabbricato.setObject(0, col, StampeGaaServlet.checkNull("Tipologia fabbricato"));
      tblFabbricato.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblFabbricato.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblFabbricato.setColWidth(1, 150);
      
      tblFabbricato.setObject(0, col, StampeGaaServlet.checkNull("Tipo"));
      tblFabbricato.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblFabbricato.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblFabbricato.setColWidth(2, 170);
      
      tblFabbricato.setObject(0, col, StampeGaaServlet.checkNull("Superficie\n(m2)"));
      tblFabbricato.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblFabbricato.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblFabbricato.setColWidth(3, 50);
      
      tblFabbricato.setObject(0, col, StampeGaaServlet.checkNull("Dimensione"));
      tblFabbricato.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblFabbricato.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblFabbricato.setColWidth(4, 50);

      for (int i = 0; i < size; i++)
      {
        FabbricatoVO temp = (FabbricatoVO) fabbricati.get(i);
        col = 0;
        tblFabbricato.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescComuneUte()));
        tblFabbricato.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescrizioneTipoFabbricato()));
        tblFabbricato.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescrizioneTipoFormaFabbricato()));
        tblFabbricato.setObject(i + 1, col, "" + temp.getSuperficieFabbricato());
        tblFabbricato.setAlignment(i + 1, col++, StyleConstants.H_CENTER);
        tblFabbricato.setObject(i + 1, col, temp.getDimensioneFabbricato() + " " + temp.getUnitaMisura());
        tblFabbricato.setAlignment(i + 1, col++, StyleConstants.H_CENTER);
      } 
      
      subReport.setElement("tblFabbricato", tblFabbricato);
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblFabbricato");
    }
    
    
    
    
    
    
    
    
  }
}