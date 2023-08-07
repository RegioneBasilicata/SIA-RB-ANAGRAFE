package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniElencoRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/TerreniElencoRichAz.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_ELENCO_RICH_AZ";

  public TerreniElencoRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idAziendaNuova = (Long)parametri.get("idAziendaNuova");
    
    Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova = 
        gaaFacadeClient.getParticelleAziendaNuovaIscrizione(idAziendaNuova.longValue());
    
    
    int size=0;
    if (vParticelleAziendaNuova!=null) 
      size=vParticelleAziendaNuova.size();
    
    if (size>0)
    {
      subReport.removeElement("nlTerreni1");
      subReport.removeElement("nlTerreni2");
      subReport.removeElement("txtNoTerreni");
      
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblQuadroI1"));
      
      for (int i=0;i<9;i++)
      {
        tblTerreni.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(0, i, StyleConstants.H_CENTER);
        tblTerreni.setFont(1, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(1, i, StyleConstants.H_CENTER);
      }
      
      
      
        
      // Imposto l'header
      int col = 0;
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 83);//comune
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//sezione
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//foglio
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 18);//particella
      tblTerreni.setSpan(0, col , new Dimension(2, 1));
      tblTerreni.setObject(0, col, StampeGaaServlet.checkNull("Cond (1)"));
      tblTerreni.setColWidth(col++, 10);//codice conduzione
      tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("%"));
      tblTerreni.setColWidth(col++, 30);//superficie candotta/percentuale possesso
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 200);//uso del suolo
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//unita misura
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 50);//Sup. util
      
    
      for (int i = 0; i < size; i++)
      {
        tblTerreni.addRow();
        ParticellaAziendaNuovaVO particella= (ParticellaAziendaNuovaVO) vParticelleAziendaNuova.get(i);
        col = 0;
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDesCom()+" ("+particella.getSglProv()+")"));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSezione()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getFoglio()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getParticella()));        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdTitoloPossesso()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        BigDecimal percentualePossessoTmp = particella.getPercentualePossesso();
        if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
        {
          percentualePossessoTmp = new BigDecimal(1);
        }          
        tblTerreni.setObject(i + 2, col++, Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));       
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDescTipoUtilizzo())+" - "
            +StampeGaaServlet.checkNull(particella.getDescTipoVarieta()));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDescUnitaMisura()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(particella.getSuperficie()));       
        
      }
      

      subReport.setElement("tblQuadroI1", tblTerreni);
      
      
      popolaLegenda(anagFacadeClient, subReport);
    }
    else
    {
      //devo rimuovore la tabella e la legenda
      // Recupero le coordinate della cella in cui mi trovo
      subReport.removeElement("tblQuadroI1");
      subReport.removeElement("tblLegendaHeader");
      subReport.removeElement("spLegenda1");
      subReport.removeElement("legendaConduzione");
      subReport.removeElement("spTerreni3");
      subReport.removeElement("nlTerreni3");
    }
    
    
    
  }
  
  
  private void popolaLegenda(AnagFacadeClient anagFacadeClient, ReportSheet report)
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
    
   
  }
}