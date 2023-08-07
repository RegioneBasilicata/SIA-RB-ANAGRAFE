package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class UVRiepilogoIdoneita extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/UVRiepilogoIdoneita.srt";
  private final static String CODICE_SUB_REPORT = "UV_RIEPILOGO_IDONEITA";

  public UVRiepilogoIdoneita() throws IOException, SolmrException
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
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = gaaFacadeClient.getStampaUVRiepilogoIdoneita(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza);
    
    if (vRiepiloghi != null)
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunaUV");
      
      BigDecimal totSupVit = new BigDecimal(0);

      DefaultTableLens tblUV = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblUV = new DefaultTableLens(subReport.getTable("tblUV"));

      tblUV.setColWidth(0, 190);
      tblUV.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUV.setColWidth(1, 190);
      tblUV.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblUV.setColWidth(2, 190);
      tblUV.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblUV.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblUV.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblUV.setAlignment(0, 2, StyleConstants.H_CENTER);
      
      
    
      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < vRiepiloghi.size(); i++)
      {
        tblUV.addRow();
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = vRiepiloghi.get(i);
        tblUV.setObject(i + 1, 0, StampeGaaServlet.checkNull(riepiloghiUnitaArboreaVO.getDescProv()));
        tblUV.setObject(i + 1, 1, StampeGaaServlet.checkNull(riepiloghiUnitaArboreaVO.getTipoTipolgiaVino()));
        tblUV.setObject(i + 1, 2, StringUtils.parseSuperficieFieldBigDecimal(riepiloghiUnitaArboreaVO.getSupVitata()));
        totSupVit = totSupVit.add(riepiloghiUnitaArboreaVO.getSupVitata());
        tblUV.setAlignment(i + 1, 2, StyleConstants.H_RIGHT);
      }
      
      aggiungiTotali(tblUV, vRiepiloghi.size(), totSupVit);
      
      subReport.setElement("tblUV", tblUV);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblUV");
    }
    
    
    
  }
  
  
  private final void aggiungiTotali(DefaultTableLens tblUV, int size, BigDecimal totSupVit)
  {
    tblUV.addRow();
    tblUV.setSpan(size + 1, 0 , new Dimension(2, 1));
    tblUV.setObject(size + 1, 0, StampeGaaServlet.checkNull("Totale "));
    tblUV.setAlignment(size + 1, 0, StyleConstants.H_RIGHT);
    tblUV.setObject(size + 1, 2, StringUtils.parseSuperficieFieldBigDecimal(totSupVit));
    
    tblUV.setAlignment(size + 1, 2, StyleConstants.H_RIGHT);   
    
    tblUV.setFont(size + 1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblUV.setFont(size + 1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
    
    
    int row=tblUV.getRowCount();
    tblUV.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblUV.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);  
    tblUV.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    
  }
}