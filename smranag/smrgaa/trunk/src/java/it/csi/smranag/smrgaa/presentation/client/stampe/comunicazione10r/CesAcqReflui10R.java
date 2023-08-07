package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class CesAcqReflui10R extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/CesAcqReflui10R.srt";
  private final static String CODICE_SUB_REPORT = "CES_ACQ_REFLUI_10R";

  public CesAcqReflui10R() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    Comunicazione10RVO[] comunicazione10RVO = (Comunicazione10RVO[])parametri.get("comunicazione10RVO");
    
    
    
    int size = 0;
    Vector<EffluenteCesAcqVO> cessAcquis = null;

    if (comunicazione10RVO != null && comunicazione10RVO.length!=0)
    {
      long idComunicazione[]=new long[comunicazione10RVO.length];
      
      for (int i=0;i<comunicazione10RVO.length;i++)
        idComunicazione[i]=comunicazione10RVO[i].getIdComunicazione10R();
      
      cessAcquis = gaaFacadeClient.getListEffluentiCessAcquPerStampa(idComunicazione);
    }

    if (cessAcquis != null)
      size = cessAcquis.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di cessioni
      // associate all'azienda
      subReport.removeElement("txtNoCessAcqu");
      subReport.removeElement("nlNoCessAcqu");
    
      DefaultTableLens tblCessAcqu = new DefaultTableLens(subReport.getTable("tblCessAcqu"));
    
      
    
      //tblCessAcqu.setRowBackground(0, new Color(-4137792));
      
      
    
      // Imposto l'header
      int col = 0;
      tblCessAcqu.setColWidth(col, 100);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 130);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 80);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 60);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 110);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 40);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblCessAcqu.setColWidth(col, 45);
      tblCessAcqu.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblCessAcqu.setAlignment(0, col++, StyleConstants.H_CENTER);
    
      for (int i = 0; i < size; i++)
      {
        tblCessAcqu.addRow();
        EffluenteCesAcqVO temp = (EffluenteCesAcqVO) cessAcquis.get(i);
        col = 0;
        tblCessAcqu.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getCuaa()));
        tblCessAcqu.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDenominazione()));
        tblCessAcqu.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescComune()) 
            + " ("+StampeGaaServlet.checkNull(temp.getSglProv()+")"));
        tblCessAcqu.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescrizione()));
        tblCessAcqu.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescTipoEffluente()));
        tblCessAcqu.setObject(i + 1, col, StringUtils.parseDoubleFieldBigDecimal(temp.getQuantita()));
        tblCessAcqu.setAlignment(i + 1, col++, StyleConstants.H_CENTER);
        tblCessAcqu.setObject(i + 1, col, Formatter.formatAndRoundBigDecimal0(temp.getQuantitaAzotoDichiarato()));
        tblCessAcqu.setAlignment(i + 1, col++, StyleConstants.H_CENTER);
      }
      subReport.setElement("tblCessAcqu", tblCessAcqu);
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblCessAcqu");
    }
    
    
    
    
    
    
    
    
  }
}