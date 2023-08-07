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
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.services.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniRiepilogoCond extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/TerreniRiepilogoCond.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_RIEPILOGO_COND";

  public TerreniRiepilogoCond() throws IOException, SolmrException
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
    
    //Controllo se sono nel nuovo o nel vecchio stile
    boolean flagOldStyle = false;
    Date dataConfronto = null;
    try
    {
      String parametroFAPP = anagFacadeClient
        .getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_FAPP);
      dataConfronto = DateUtils.parseDate(parametroFAPP);
      if(dataInserimentoDichiarazione != null)
      {
        if(dataInserimentoDichiarazione.before(dataConfronto))
        {
          flagOldStyle = true;
        }
      }
    }
    catch(Exception ex)
    {}
    
    
    
    int size = 0;
    
    
    Vector<BaseCodeDescription> terreni = anagFacadeClient
      .getTerreniQuadroI4(anagAziendaVO.getIdAzienda(), codiceFotografia);
    
    size = terreni.size();
    
    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di cessioni
      // associate all'azienda
      subReport.removeElement("txtNoTerreniQuadroI4");
    
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblQuadroI4"));
    
    
      // Imposto l'header
      int col = 0;
      tblTerreni.setColWidth(col, 370);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 65);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 70);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblTerreni.setColWidth(col, 70);
      tblTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTerreni.setAlignment(0, col, StyleConstants.H_CENTER);
      if(flagOldStyle)
      {
        tblTerreni.setObject(0, col++, "Sup. condotta");
      }
      else
      {
        tblTerreni.setObject(0, col++, "Sup. utilizzata");
      }
      
      BigDecimal totSupCat=new BigDecimal(0);
      BigDecimal totSupCon=new BigDecimal(0);
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
        
        BigDecimal num[]=new BigDecimal[3];
        num=(BigDecimal[])temp.getItem();
        
        
        
        
        tblTerreni.setObject(i + 1, col, StringUtils.parseSuperficieFieldBigDecimal(num[0]));
        tblTerreni.setAlignment(i + 1, col++, StyleConstants.H_RIGHT);
        totSupCat=totSupCat.add(num[0]);        
        totSupCon=totSupCon.add(num[1]);
        if(num[2] != null)
        {
          totSupUtil = totSupUtil.add(num[2]);
        }
        if(flagOldStyle)
        {
          tblTerreni.setObject(i + 1, col, StringUtils.parseSuperficieFieldBigDecimal(num[1]));
          tblTerreni.setAlignment(i + 1, col++, StyleConstants.H_RIGHT);
        }
        else
        {
          tblTerreni.setObject(i + 1, col, StringUtils.parseSuperficieFieldBigDecimal(num[2]));
          tblTerreni.setAlignment(i + 1, col++, StyleConstants.H_RIGHT);
        }
        
      }
      
      
      BigDecimal supTmp = null;
      if(flagOldStyle)
      {
        supTmp = totSupCon;        
      }
      else
      {
        supTmp = totSupUtil;        
      }
      
      aggiornaTotaliOld(tblTerreni,size,totParticelle,totSupCat,supTmp);
      setNoBorderQuadroI4Old(tblTerreni);
      
      
      
      
      subReport.setElement("tblQuadroI4", tblTerreni);
    }
    else
    {
      // Rimuovo la tabella 
      subReport.removeElement("tblQuadroI4");
    }    
    
  }
  
  
  private final void aggiornaTotaliOld(DefaultTableLens tblTerreni, int size,
      long totParticelle, BigDecimal totSupCat, BigDecimal totSupCon)
  {
    //Aggiungo la riga dei totali
    tblTerreni.addRow();
    tblTerreni.setObject(size + 1, 0, StampeGaaServlet.checkNull("Totale "));
    tblTerreni.setAlignment(size + 1, 0, StyleConstants.H_RIGHT);
    tblTerreni.setObject(size + 1, 1, ""+totParticelle);
    tblTerreni.setAlignment(size + 1, 1, StyleConstants.H_RIGHT);
    tblTerreni.setObject(size + 1, 2, StringUtils.parseSuperficieFieldBigDecimal(totSupCat));
    tblTerreni.setAlignment(size + 1, 2, StyleConstants.H_RIGHT);
    tblTerreni.setObject(size + 1, 3, StringUtils.parseSuperficieFieldBigDecimal(totSupCon));
    tblTerreni.setAlignment(size + 1, 3, StyleConstants.H_RIGHT);
    
    
    tblTerreni.setFont(size + 1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblTerreni.setFont(size + 1, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblTerreni.setFont(size + 1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblTerreni.setFont(size + 1, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
  }
  
  private final void aggiornaTotali(DefaultTableLens tblTerreni, int size,
      long totParticelle, BigDecimal totSupCat)
  {
    //Aggiungo la riga dei totali
    tblTerreni.addRow();
    tblTerreni.setObject(size + 1, 0, StampeGaaServlet.checkNull("Totale "));
    tblTerreni.setAlignment(size + 1, 0, StyleConstants.H_RIGHT);
    tblTerreni.setObject(size + 1, 1, ""+totParticelle);
    tblTerreni.setAlignment(size + 1, 1, StyleConstants.H_RIGHT);
    tblTerreni.setObject(size + 1, 2, StringUtils.parseSuperficieFieldBigDecimal(totSupCat));
    tblTerreni.setAlignment(size + 1, 2, StyleConstants.H_RIGHT);
    
    
    tblTerreni.setFont(size + 1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblTerreni.setFont(size + 1, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
    tblTerreni.setFont(size + 1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
  }
  
  private final void setNoBorderQuadroI4Old(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
  }
  
  private final void setNoBorderQuadroI4(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 3, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 3, StyleConstants.NO_BORDER);
  }
}