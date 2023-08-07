package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniRiepilogoZVN extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/TerreniRiepilogoZVN.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEPILOGO_ZVN";

  public TerreniRiepilogoZVN() throws IOException, SolmrException
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
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    
    Comunicazione10RVO[] comunicazione10RVO = (Comunicazione10RVO[])parametri.get("comunicazione10RVO");
    
    
    int size = 0;
    
    Vector<QuadroDTerreni> terreni = null;
    
    if (codiceFotografia == null)
    {
      terreni = anagFacadeClient.getTerreniQuadroD10R(anagAziendaVO.getIdAzienda());
    }
    else
    {
      terreni = anagFacadeClient.getTerreniQuadroD10R(
           dataInserimentoDichiarazione, codiceFotografia);
    }
      

    size = terreni.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di cessioni
      // associate all'azienda
      subReport.removeElement("txtNoTerreni");
      
      
      String aziendaZVN = "No";
      if (comunicazione10RVO != null && comunicazione10RVO.length!=0)
      {
        //aziendaZVN
        BigDecimal totSupSauPiemonte = new BigDecimal(0);
        BigDecimal totSupSauPiemonteZVN = new BigDecimal(0);
        for(int i=0;i<comunicazione10RVO.length;i++)
        {
          Comunicazione10RVO comVOTmp = comunicazione10RVO[i];
            
          if(comVOTmp.getSuperficieSauPiemonte()!= null)
          {
            totSupSauPiemonte = totSupSauPiemonte.add(comVOTmp.getSuperficieSauPiemonte());
          }
          
          if(comVOTmp.getSuperficieSauPiemonteZVN()!= null)
          {
            totSupSauPiemonteZVN = totSupSauPiemonteZVN.add(comVOTmp.getSuperficieSauPiemonteZVN());
          }       
        }
        
        BigDecimal totAziendaZVN = new BigDecimal(0);
        if(totSupSauPiemonte.compareTo(new BigDecimal(0)) > 0)
        {
          
          totSupSauPiemonteZVN = totSupSauPiemonteZVN.divide(totSupSauPiemonte,4,BigDecimal.ROUND_HALF_UP);
          totAziendaZVN = totSupSauPiemonteZVN.multiply(new BigDecimal(100));
          
          if(totAziendaZVN.compareTo(new BigDecimal(25)) >= 0)
          {
             aziendaZVN = "Si";
          }
        }
        
        subReport.setElement("txtRespAziendaZVN", StampeGaaServlet.checkNull(aziendaZVN));
      }
      
      subReport.setElement("txtRespAziendaZVN", StampeGaaServlet.checkNull(aziendaZVN));
      
    
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblTerreni"));
      
      
  
      
      int col = 0;      
      
      tblTerreni.setColWidth(col, 105);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 105);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 40);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 80);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col, StyleConstants.H_CENTER);
      tblTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 70);
      tblTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 80);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col, StyleConstants.H_CENTER);
      tblTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 70);
      tblTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      
      BigDecimal totSupAssVul = new BigDecimal(0);
      BigDecimal totSupCondVul = new BigDecimal(0);
      BigDecimal totSupAssNonVul = new BigDecimal(0);
      BigDecimal totSupCondNonVul = new BigDecimal(0);
  
      for (int i = 0; i < size; i++)
      {
        
        tblTerreni.addRow();
        
        QuadroDTerreni temp = (QuadroDTerreni) terreni.get(i);
        col = 0;
        totSupAssVul=totSupAssVul.add(temp.getSupAssVul());
        totSupCondVul=totSupCondVul.add(temp.getSupCondVul());
        totSupAssNonVul=totSupAssNonVul.add(temp.getSupAssNonVul());
        totSupCondNonVul=totSupCondNonVul.add(temp.getSupCondNonVul());
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(temp.getComuneUte())
            +" ("+temp.getSiglaProvUte()+") - "+temp.getIndirizzoUte());
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(temp.getComune())+" ("+temp.getSiglaProv()+")");
        tblTerreni.setObject(i + 2, col, StampeGaaServlet.checkNull(temp.getFoglio()));
        tblTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col, StringUtils.parseSuperficieFieldBigDecimal(temp.getSupAssVul())); 
        tblTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col, StringUtils.parseSuperficieFieldBigDecimal(temp.getSupCondVul())); 
        tblTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col, StringUtils.parseSuperficieFieldBigDecimal(temp.getSupAssNonVul())); 
        tblTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col, StringUtils.parseSuperficieFieldBigDecimal(temp.getSupCondNonVul())); 
        tblTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
      }
      
      //Aggiungo la riga dei totali
      tblTerreni.addRow();
      tblTerreni.setSpan(size + 2, 0 , new Dimension(3, 1));
      tblTerreni.setObject(size + 2, 0, "Totale superficie (ha)");
      tblTerreni.setAlignment(size + 2, 0, StyleConstants.H_RIGHT);
      tblTerreni.setObject(size + 2, 3, StringUtils.parseSuperficieFieldBigDecimal(totSupAssVul));
      tblTerreni.setAlignment(size + 2, 3, StyleConstants.H_CENTER);
      tblTerreni.setObject(size + 2, 4, StringUtils.parseSuperficieFieldBigDecimal(totSupCondVul));
      tblTerreni.setAlignment(size + 2, 4, StyleConstants.H_CENTER);
      tblTerreni.setObject(size + 2, 5, StringUtils.parseSuperficieFieldBigDecimal(totSupAssNonVul));
      tblTerreni.setAlignment(size + 2, 5, StyleConstants.H_CENTER);
      tblTerreni.setObject(size + 2, 6, StringUtils.parseSuperficieFieldBigDecimal(totSupCondNonVul));
      tblTerreni.setAlignment(size + 2, 6, StyleConstants.H_CENTER);
      
      
      tblTerreni.setFont(size + 2, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 2, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 2, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 2, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setFont(size + 2, 6, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      tblTerreni.setHeaderRowCount(2);
      setNoBorderTerreni(tblTerreni);
      
      
      subReport.setElement("tblTerreni", tblTerreni);
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblTerreni");
      subReport.removeElement("txtAziendaZVN");
      subReport.removeElement("txtRespAziendaZVN");
      subReport.removeElement("nlAziendaZVN");
      subReport.removeElement("nlAziendaZVN2");
    }   
    
  }
  
  
  private final void setNoBorderTerreni(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 2, StyleConstants.NO_BORDER);
  }
}