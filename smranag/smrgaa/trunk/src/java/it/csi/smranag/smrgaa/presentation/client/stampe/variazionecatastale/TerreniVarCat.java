package it.csi.smranag.smrgaa.presentation.client.stampe.variazionecatastale;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniVarCat extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/variazionecatastale/TerreniVarCat.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_VAR_CAT";

  public TerreniVarCat() throws IOException, SolmrException
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
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    
    int size=0;
    Vector<ParticellaVO> particelle = gaaFacadeClient.getElencoParticelleVarCat(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza);
    if (particelle!=null) size=particelle.size();
    
    if (size>0)
    {
      subReport.removeElement("nlTerreni1");
      subReport.removeElement("nlTerreni2");
      subReport.removeElement("txtNoTerreni");
      
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblQuadroI1"));
      
      for (int i=0;i<19;i++)
      {
        tblTerreni.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(0, i, StyleConstants.H_CENTER);
        tblTerreni.setFont(1, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(1, i, StyleConstants.H_CENTER);
      }
      
      
      
        
      // Imposto l'header
      int col = 0;
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 87);//comune
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//sezione
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//foglio
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 18);//particella
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//subalterno
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//superficie catastale
      tblTerreni.setSpan(0, col , new Dimension(2, 1));
      tblTerreni.setObject(0, col, StampeGaaServlet.checkNull("Cond (1)"));
      tblTerreni.setColWidth(col++, 10);//codice conduzione
      tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("%"));
      tblTerreni.setColWidth(col++, 30);//superficie candotta/percentuale possesso
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//uso agronomico
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 110);//uso del suolo
      
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//Sup. util
      
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//C
      
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//C.P.(1)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//ZVN(2)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Potenzialità irrigua
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Rotazione colturale
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 35);//Qualita colturale
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 165);//Titolare.....
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 120);//Documento
      
      

      
    
      for (int i = 0; i < size; i++)
      {
        BigDecimal temp=new BigDecimal(0);
        tblTerreni.addRow();
        ParticellaVO particella= (ParticellaVO) particelle.get(i);
        col = 0;
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDescComuneParticella()));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSezione()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getFoglio()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getParticella()));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSubalterno()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupCatastale());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdTitoloPossesso()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        BigDecimal percentualePossessoTmp = particella.getPercentualePossesso();
        if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
        {
          percentualePossessoTmp = new BigDecimal(1);
        }          
        tblTerreni.setObject(i + 2, col++, Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupAgronomico());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDescUtilizzoParticella())+
            " "+StampeGaaServlet.checkNull(particella.getDescVarieta()));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          if(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO.compareTo(particella.getIdTitoloPossesso()) == 0)
          {
            temp = new BigDecimal(particella.getSuperficieCondotta());
          }
          else
          {
            temp = new BigDecimal(particella.getSuperficieUtilizzata());
          }
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
       
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaC()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdCasoParticolare()));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getZonaVulnerabileNitrati()));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdPotenzaIrrigua()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdRotazioneColturale()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getCodQualita()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_LEFT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getProprietari()));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDocumento()));
        
      }
      
      
      
      
      
      //setNoBorderQuadroI1(tblTerreni);
      
      
      

      subReport.setElement("tblQuadroI1", tblTerreni);
      
      
      popolaLegenda(anagFacadeClient, subReport, dataInserimentoDichiarazione);
    }
    else
    {
      //devo rimuovore la tabella e la legenda
      // Recupero le coordinate della cella in cui mi trovo
      Point elStartCell = layout.getElementCell(subReport.getElement("tblQuadroI1"));
      int elStartRow = (int) Math.round(elStartCell.getY());
      layout.removeRows(elStartRow, 2);
    }
    
    
    
  }
  
  
  private void popolaLegenda(AnagFacadeClient anagFacadeClient, ReportSheet report,
      Date dataInserimentoDichiarazione)
    throws SolmrException, CloneNotSupportedException
  {
    /**
     * Preparo la legenda
     */
    DefaultTableLens tblTemp = null;
    
    tblTemp = new DefaultTableLens(report.getTable("tblLegendaHeader"));
    tblTemp.setColWidth(0, 55);
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblLegendaHeader", tblTemp);
    
    
   //Legenda Area C ***** inizio ********
    CodeDescription[] elencoAreaC  = anagFacadeClient
      .getListTipoAreaC("ID_AREA_C"); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblTipoAreaC"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 45);
    String legendaAreaC = "";
    
    for(int i=0;i<elencoAreaC.length;i++)
    {
      legendaAreaC += elencoAreaC[i].getCode() +" - ";
      legendaAreaC += elencoAreaC[i].getDescription();
      
      if(i != (elencoAreaC.length -1))
      {
        legendaAreaC += ", ";
      }      
    }
    
    tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(legendaAreaC));
    tblTemp.setAlignment(0, 1, StyleConstants.H_LEFT);
    tblTemp.setColWidth(1, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblTipoAreaC", tblTemp);    
    //Legenda Area C ***** fine ********       
    
    //Legenda Conduzione ***** inizio ********
    CodeDescription[] elencoTitoliPossesso = anagFacadeClient
      .getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE); 
    
    tblTemp = new DefaultTableLens(report.getTable("legendaConduzione"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 60);
    
    String legendaConduzione = "";
    
    for(int i=0;i<elencoTitoliPossesso.length;i++)
    {
      legendaConduzione += elencoTitoliPossesso[i].getCode() +" - ";
      legendaConduzione += elencoTitoliPossesso[i].getDescription();
      
      if(i != (elencoTitoliPossesso.length -1))
      {
        legendaConduzione += ", ";
      }      
    }
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaConduzione));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("legendaConduzione", tblTemp);    
    //Legenda Conduzione ***** fine ********
    
    
    
    
    
    //Legenda Caso Particolare ***** inizio ********
    CodeDescription[] elencoCasoParticolare  = anagFacadeClient
      .getListTipoCasoParticolare("ID_CASO_PARTICOLARE"); 
    
    tblTemp = new DefaultTableLens(report.getTable("legendaCasiParticolari"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 60);
    
    String legendaCasoParticolare = "";
    
    for(int i=0;i<elencoCasoParticolare.length;i++)
    {
      legendaCasoParticolare += elencoCasoParticolare[i].getCode() +" - ";
      legendaCasoParticolare += elencoCasoParticolare[i].getDescription();
      
      if(i != (elencoCasoParticolare.length -1))
      {
        legendaCasoParticolare += ", ";
      }      
    }
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaCasoParticolare));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("legendaCasiParticolari", tblTemp);    
    //Legenda Caso Particolare ***** fine ********
    
    tblTemp = new DefaultTableLens(report.getTable("tblLegZonaVulNitrati"
       ));
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_9);
    
    tblTemp.setColWidth(0, 9);
    tblTemp.setColWidth(1, 100);
    tblTemp.setColWidth(2, 8);
    tblTemp.setColWidth(3, 200);
    tblTemp.setColWidth(4, 140);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblLegZonaVulNitrati", tblTemp);
    
    
    //Legenda Potenzialita Irrigua ***** inizio ********
    TipoPotenzialitaIrriguaVO[] elencoPotenzialitaIrrigua  = anagFacadeClient
      .getListTipoPotenzialitaIrrigua("ID_POTENZIALITA_IRRIGUA", dataInserimentoDichiarazione); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblPotenzialitaIrrigua"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 80);
    
    String legendaPotenzialitaIrrigua = "";
    
    for(int i=0;i<elencoPotenzialitaIrrigua.length;i++)
    {
      legendaPotenzialitaIrrigua += elencoPotenzialitaIrrigua[i].getIdPotenzialitaIrrigua() +" - ";
      legendaPotenzialitaIrrigua += elencoPotenzialitaIrrigua[i].getDescrizione();
      
      if(i != (elencoPotenzialitaIrrigua.length -1))
      {
        legendaPotenzialitaIrrigua += ", ";
      }      
    }
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaPotenzialitaIrrigua));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblPotenzialitaIrrigua", tblTemp);    
    //Legenda Potenzialita Irrigua ***** fine ********  
    
    
    //Legenda Rotazione colturale ***** inizio ********
    TipoRotazioneColturaleVO[] elencoRotazioneColturale  = anagFacadeClient
      .getListTipoRotazioneColturale("ID_ROTAZIONE_COLTURALE", dataInserimentoDichiarazione); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblRotazioneColturale"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 80);
    
    String legendaRotazioneColturale = "";
    
    for(int i=0;i<elencoRotazioneColturale.length;i++)
    {
      legendaRotazioneColturale += elencoRotazioneColturale[i].getIdRotazioneColturale() +" - ";
      legendaRotazioneColturale += elencoRotazioneColturale[i].getDescrizione();
      
      if(i != (elencoRotazioneColturale.length -1))
      {
        legendaRotazioneColturale += ", ";
      }      
    }
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaRotazioneColturale));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblRotazioneColturale", tblTemp);    
    //Legenda Rotazione colturale ***** fine ********  
    
    
    //Legenda qualita colturale ***** inizio ********    
    tblTemp = new DefaultTableLens(report.getTable("tblQualitaColturale"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 80);
    
    String legendaQualitaColturale = "vedere \"QUADRO B - INFORMATIVA\" - Tabella 1";
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaQualitaColturale));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblQualitaColturale", tblTemp);    
    //Legenda qualita colturale ***** fine ********
    
    //Legenda titolare diritto ***** inizio ********    
    tblTemp = new DefaultTableLens(report.getTable("tblTitolareDiritto"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 80);
    
    String legendaTitolareDiritto = "vedere \"QUADRO B - INFORMATIVA\" - Provv. Ag. del Territorio n. 49783 del 16/10/2012, art. 1, comma 3, punto j";
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaTitolareDiritto));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblTitolareDiritto", tblTemp);    
    //Legenda titolare diritto ***** fine ********
    
    
    
      
    
    
    
  
  
  }
}