package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniRiepilogoMacroUso extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/TerreniRiepilogoMacroUso.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEPILOGO_MACROUSO";

  public TerreniRiepilogoMacroUso() throws IOException, SolmrException
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
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    
    int size = 0;
    
    
    Vector<BaseCodeDescription> terreni = anagFacadeClient
      .getTerreniQuadroI5(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione, codiceFotografia);
    
    size = terreni.size();
    
    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di cessioni
      // associate all'azienda
      subReport.removeElement("txtNoTerreniQuadroI5");
    
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblQuadroI5"));
    
      
    
    
      // Imposto l'header
      int col = 0;
      tblTerreni.setColWidth(col, 440);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 65);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 70);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      
      BigDecimal totSupUtil=new BigDecimal(0);
      long totParticelle=0;
    
      for (int i = 0; i < size; i++)
      {
        tblTerreni.addRow();
        BaseCodeDescription temp= (BaseCodeDescription) terreni.get(i);
        col = 0;
        tblTerreni.setObject(i + 1, col++, StampeGaaServlet.checkNull(temp.getDescription()));
        tblTerreni.setObject(i + 1, col, StampeGaaServlet.checkNull(temp.getCode()+""));
        tblTerreni.setAlignment(i + 1, col++, StyleConstants.H_RIGHT);
        totParticelle+=temp.getCode();
        
        tblTerreni.setObject(i + 1, col, StringUtils.parseSuperficieFieldBigDecimal((BigDecimal)temp.getItem()));
        tblTerreni.setAlignment(i + 1, col++, StyleConstants.H_RIGHT);
        totSupUtil=totSupUtil.add((BigDecimal)temp.getItem());
      }
      
      //Aggiungo la riga dei totali
      tblTerreni.addRow();
      tblTerreni.setObject(size + 1, 0, StampeGaaServlet.checkNull("Totale "));
      tblTerreni.setAlignment(size + 1, 0, StyleConstants.H_RIGHT);
      tblTerreni.setObject(size + 1, 1, ""+totParticelle);
      tblTerreni.setAlignment(size + 1, 1, StyleConstants.H_RIGHT);
      tblTerreni.setObject(size + 1, 2, StringUtils.parseSuperficieFieldBigDecimal(totSupUtil));
      tblTerreni.setAlignment(size + 1, 2, StyleConstants.H_RIGHT);
      
      
      tblTerreni.setFont(size + 1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 1, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      setNoBorderQuadroI5(tblTerreni);
      
      subReport.setElement("tblQuadroI5", tblTerreni);
    }
    else
    {
      // Rimuovo la tabella 
      subReport.removeElement("tblQuadroI5");
    }    
  }
  
  
  private final void setNoBorderQuadroI5(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
  }
}