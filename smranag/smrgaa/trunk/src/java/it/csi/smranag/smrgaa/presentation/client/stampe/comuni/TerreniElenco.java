package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

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
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.services.DateUtils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class TerreniElenco extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/TerreniElenco.srt";
  private final static String CODICE_SUB_REPORT = "TERRENI_ELENCO";

  public TerreniElenco() throws IOException, SolmrException
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
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    //Controllo se sono nel nuovo o nel vecchio stile
    int tipoModelloStampa = SolmrConstants.STAMPA_ATTUALE;
    try
    {
      String parametroFAPP = anagFacadeClient
        .getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_FAPP);
      Date dataPreSCI = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_STAMPA_SCI);
      Date dataPrePercPoss = null;
      dataPrePercPoss = DateUtils.parseDate(parametroFAPP);
      if(dataInserimentoDichiarazione != null)
      {
        if(dataInserimentoDichiarazione.before(dataPrePercPoss))
        {
          tipoModelloStampa = SolmrConstants.STAMPA_PRE_PERC_POSSESSO;
        }
        else if(dataInserimentoDichiarazione.before(dataPreSCI))
        {
          tipoModelloStampa = SolmrConstants.STAMPA_PRE_CORPO_IDRICO;
        }
        
      }
    }
    catch(Exception ex)
    {}    
    
    int size=0;
    Vector<ParticellaVO> particelle = anagFacadeClient.getElencoParticelleQuadroI1(
        anagAziendaVO.getIdAzienda(), codiceFotografia);
    if (particelle!=null) size=particelle.size();
    
    if (size>0)
    {
      subReport.removeElement("nlTerreni1");
      subReport.removeElement("nlTerreni2");
      subReport.removeElement("txtNoTerreni");
      
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblQuadroI1"));
      
      for (int i=0;i<25;i++)
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
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//subalterno
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//superficie catastale
      tblTerreni.setSpan(0, col , new Dimension(2, 1));
      tblTerreni.setObject(0, col, StampeGaaServlet.checkNull("Cond (1)"));
      tblTerreni.setColWidth(col++, 10);//codice conduzione
      if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_PERC_POSSESSO)
      {
        tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("Sup."));
      }
      else
      {
        tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("%"));
      }
      tblTerreni.setColWidth(col++, 30);//superficie candotta/percentuale possesso
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//uso agronomico
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 200);//uso del suolo
      
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 30);//Sup. util
      
      tblTerreni.setSpan(0, col , new Dimension(4, 1));
      tblTerreni.setColWidth(col++, 10);//A
      tblTerreni.setColWidth(col++, 10);//B
      tblTerreni.setColWidth(col++, 10);//C
      tblTerreni.setColWidth(col++, 10);//D
      
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//C.P.(1)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//ZVN(2)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Z.A.(3)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//I R R (4)
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Capt. Acque pozzi
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Potenzialità irrigua
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Rotazione colturale
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 10);//Terrazzamenti
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 14);//Stato copro idrico
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 156);//Documento
      
      
      BigDecimal[] totSupCatGraf = anagFacadeClient
        .getTotSupQuadroI1CatastaleAndGrafica(anagAziendaVO.getIdAzienda(),codiceFotografia);
      BigDecimal[] superfici = anagFacadeClient.getTotSupQuadroI1CondottaAndAgronomica(
          anagAziendaVO.getIdAzienda(),codiceFotografia);
      
      BigDecimal totSupUtilizzata = new BigDecimal(0);
      
    
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
        
        if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_PERC_POSSESSO)
        {
          try
          {
            temp = new BigDecimal(particella.getSuperficieCondotta());
          }
          catch(Exception e)
          {
            temp=new BigDecimal(0);
          }
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
          tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        }
        else
        {
          tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
          BigDecimal percentualePossessoTmp = particella.getPercentualePossesso();
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }          
          tblTerreni.setObject(i + 2, col++, Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
        }
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          if(particella.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            temp = new BigDecimal(particella.getSuperficieCondotta());
          }
          else
          {
            temp = new BigDecimal(particella.getSupAgronomico());  
          }
          
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
            temp = new BigDecimal(0);
            totSupUtilizzata=totSupUtilizzata.add(temp);
          }
          else
          {
            temp = new BigDecimal(particella.getSuperficieUtilizzata());
            totSupUtilizzata=totSupUtilizzata.add(temp);
          }
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaA()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaB()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaC()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaD()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdCasoParticolare()));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getZonaVulnerabileNitrati()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdZonaAltimetrica()));
      
        if (particella.getFlagIrrigabile())
          tblTerreni.setObject(i + 2, col++, SolmrConstants.FLAG_S);
        else
          tblTerreni.setObject(i + 2, col++, SolmrConstants.FLAG_N);
        
        if (particella.getFlagCaptazionePozzi())
          tblTerreni.setObject(i + 2, col++, SolmrConstants.FLAG_S);
        else
          tblTerreni.setObject(i + 2, col++, SolmrConstants.FLAG_N);
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdPotenzaIrrigua()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdRotazioneColturale()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdTerrazzamento()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdAreaM()));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getDocumento()));
        
      }
      
      
      //Aggiungo la riga dei totali
      if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_PERC_POSSESSO)
      {
        aggiungiTotaliPrePercPoss(tblTerreni,size,totSupCatGraf,superfici,totSupUtilizzata);
      }
      else
      {
        aggiungiTotali(tblTerreni,size,totSupCatGraf,superfici,totSupUtilizzata);
      }
      
      
      //tblTerreni.setHeaderRowCount(2);
      
      
      if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_PERC_POSSESSO)
      {
        //Rimuovo le nuove colonne
        tblTerreni.removeColumn(20);
        tblTerreni.removeColumn(20);
        tblTerreni.removeColumn(20);
        tblTerreni.removeColumn(20);
        
        setNoBorderQuadroI1PrePercPoss(tblTerreni);
      }
      else if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_CORPO_IDRICO)
      {
        //Rimuovo le nuove colonne
        tblTerreni.removeColumn(23);        
        setNoBorderQuadroI1PreSCI(tblTerreni);
      }
      else
      {
        setNoBorderQuadroI1(tblTerreni);
      }
      
      

      subReport.setElement("tblQuadroI1", tblTerreni);
      
      
      popolaLegenda(anagFacadeClient, subReport, tipoModelloStampa, dataInserimentoDichiarazione);
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
  
  
  
  private final void aggiungiTotaliPrePercPoss(DefaultTableLens tblTerreni, int size, BigDecimal totSupCatGraf[],
      BigDecimal totSupCondAgron[], BigDecimal totSupUtilizzata)
  {
    tblTerreni.addRow();
    tblTerreni.setSpan(size + 2, 0 , new Dimension(5, 1));
    tblTerreni.setObject(size + 2, 0, StampeGaaServlet.checkNull("Totale "));
    tblTerreni.setAlignment(size + 2, 0, StyleConstants.H_CENTER);
    tblTerreni.setObject(size + 2, 5, StringUtils.parseSuperficieFieldBigDecimal(totSupCatGraf[0]));
    
    tblTerreni.setAlignment(size + 2, 5, StyleConstants.H_RIGHT);
    
    tblTerreni.setObject(size + 2, 7, StringUtils.parseSuperficieFieldBigDecimal(totSupCondAgron[0]));
    tblTerreni.setAlignment(size + 2, 7, StyleConstants.H_RIGHT);
    
    tblTerreni.setObject(size + 2, 8, StringUtils.parseSuperficieFieldBigDecimal(totSupCondAgron[1]));
    tblTerreni.setAlignment(size + 2, 8, StyleConstants.H_RIGHT);
    
    tblTerreni.setObject(size + 2, 10, StringUtils.parseSuperficieFieldBigDecimal(totSupUtilizzata));
    tblTerreni.setAlignment(size + 2, 10, StyleConstants.H_RIGHT);
    
    
    
    tblTerreni.setFont(size + 2, 0, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 7, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 8, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 10, StampeGaaServlet.FONT_SERIF_BOLD_8);
    
  }
  
  
  private final void aggiungiTotali(DefaultTableLens tblTerreni, int size, BigDecimal totSupCatGraf[],
      BigDecimal totSupCondAgron[], BigDecimal totSupUtilizzata)
  {
    tblTerreni.addRow();
    tblTerreni.setSpan(size + 2, 0 , new Dimension(5, 1));
    tblTerreni.setObject(size + 2, 0, StampeGaaServlet.checkNull("Totale "));
    tblTerreni.setAlignment(size + 2, 0, StyleConstants.H_CENTER);
    tblTerreni.setObject(size + 2, 5, StringUtils.parseSuperficieFieldBigDecimal(totSupCatGraf[0]));
    
    tblTerreni.setAlignment(size + 2, 5, StyleConstants.H_RIGHT);
     
    tblTerreni.setObject(size + 2, 8, StringUtils.parseSuperficieFieldBigDecimal(totSupCondAgron[1]));
    tblTerreni.setAlignment(size + 2, 8, StyleConstants.H_RIGHT);
    
    tblTerreni.setObject(size + 2, 10, StringUtils.parseSuperficieFieldBigDecimal(totSupUtilizzata));
    tblTerreni.setAlignment(size + 2, 10, StyleConstants.H_RIGHT);
    
    
    
    tblTerreni.setFont(size + 2, 0, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 7, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 8, StampeGaaServlet.FONT_SERIF_BOLD_8);
    tblTerreni.setFont(size + 2, 10, StampeGaaServlet.FONT_SERIF_BOLD_8);
    
  }
  
  
  
  
  private final void setNoBorderQuadroI1PrePercPoss(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    for(int i=0;i<5;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 6, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 9, StyleConstants.NO_BORDER);
    for(int i=11;i<21;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    for(int i=11;i<21;i++)
      tblLens.setColBorder(row-1, i, StyleConstants.NO_BORDER);
  }
  
  
  private final void setNoBorderQuadroI1PreSCI(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    for(int i=0;i<5;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    
    tblLens.setRowBorder(row-1, 6, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 6, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 7, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 9, StyleConstants.NO_BORDER);
    
    for(int i=11;i<24;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    for(int i=11;i<24;i++)
      tblLens.setColBorder(row-1, i, StyleConstants.NO_BORDER);
  }
  
  
  private final void setNoBorderQuadroI1(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    for(int i=0;i<5;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    
    tblLens.setRowBorder(row-1, 6, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 6, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 7, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 9, StyleConstants.NO_BORDER);
    
    for(int i=11;i<25;i++)
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
    for(int i=11;i<25;i++)
      tblLens.setColBorder(row-1, i, StyleConstants.NO_BORDER);
  }
  
  
  private void popolaLegenda(AnagFacadeClient anagFacadeClient, ReportSheet report, int tipoModelloStampa,
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
    
    
    //Legenda Area A ***** inizio ********
    CodeDescription[] elencoAreaA  = anagFacadeClient
      .getListTipoAreaA("ID_AREA_A"); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblTipoAreaA"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 45);
    String legendaAreaA = "";
    
    for(int i=0;i<elencoAreaA.length;i++)
    {
      legendaAreaA += elencoAreaA[i].getCode() +" - ";
      legendaAreaA += elencoAreaA[i].getDescription();
      
      if(i != (elencoAreaA.length -1))
      {
        legendaAreaA += ", ";
      }      
    }
    
    tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(legendaAreaA));
    tblTemp.setAlignment(0, 1, StyleConstants.H_LEFT);
    tblTemp.setColWidth(1, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblTipoAreaA", tblTemp);    
    //Legenda Area A ***** fine ********
    
    //Legenda Area B ***** inizio ********
    CodeDescription[] elencoAreaB  = anagFacadeClient
      .getListTipoAreaB("ID_AREA_B"); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblTipoAreaB"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 45);
    String legendaAreaB = "";
    
    for(int i=0;i<elencoAreaB.length;i++)
    {
      legendaAreaB += elencoAreaB[i].getCode() +" - ";
      legendaAreaB += elencoAreaB[i].getDescription();
      
      if(i != (elencoAreaB.length -1))
      {
        legendaAreaB += ", ";
      }      
    }
    
    tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(legendaAreaB));
    tblTemp.setAlignment(0, 1, StyleConstants.H_LEFT);
    tblTemp.setColWidth(1, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblTipoAreaB", tblTemp);    
    //Legenda Area B ***** fine ********
    
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
    
    //Legenda Area D ***** inizio ********
    CodeDescription[] elencoAreaD  = anagFacadeClient
      .getListTipoAreaD("ID_AREA_D"); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblTipoAreaD"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 45);
    String legendaAreaD = "";
    
    for(int i=0;i<elencoAreaD.length;i++)
    {
      legendaAreaD += elencoAreaD[i].getCode() +" - ";
      legendaAreaD += elencoAreaD[i].getDescription();
      
      if(i != (elencoAreaD.length -1))
      {
        legendaAreaD += ", ";
      }      
    }
    
    tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(legendaAreaD));
    tblTemp.setAlignment(0, 1, StyleConstants.H_LEFT);
    tblTemp.setColWidth(1, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblTipoAreaD", tblTemp);    
    //Legenda Area D ***** fine ********
    
    
    
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
    
    
    
    //Legenda Zona Altimetrica ***** inizio ********
    CodeDescription[] elencoZonaAltimetrica  = anagFacadeClient
      .getListTipoZonaAltimetrica("ID_ZONA_ALTIMETRICA"); 
    
    tblTemp = new DefaultTableLens(report.getTable("tblLegZonaAlt"));
    
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(0, 9);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setColWidth(1, 60);
    
    String legendaZonaAltimetrica = "";
    
    for(int i=0;i<elencoZonaAltimetrica.length;i++)
    {
      legendaZonaAltimetrica += elencoZonaAltimetrica[i].getCode() +" - ";
      legendaZonaAltimetrica += elencoZonaAltimetrica[i].getDescription();
      
      if(i != (elencoZonaAltimetrica.length -1))
      {
        legendaZonaAltimetrica += ", ";
      }      
    }
    
    tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaZonaAltimetrica));
    tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
    tblTemp.setColWidth(2, 739);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblLegZonaAlt", tblTemp);    
    //Legenda Zona Altimetrica ***** fine ********    
    
    tblTemp = new DefaultTableLens(report.getTable("tblLegTerrIrr"
       ));
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    
    tblTemp.setColWidth(0, 9);
    tblTemp.setColWidth(1, 100);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblLegTerrIrr", tblTemp);
    
    tblTemp = new DefaultTableLens(report.getTable("tblLegCapAcquePozzi"
       ));
    tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
    
    tblTemp.setColWidth(0, 9);
    tblTemp.setColWidth(1, 100);
    
    StampeGaaServlet.setNoBorder(tblTemp);
    report.setElement("tblLegCapAcquePozzi", tblTemp);
    
    if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_PERC_POSSESSO)
    {
      report.removeElement("tblPotenzialitaIrrigua");
      report.removeElement("tblRotazioneColturale");
      report.removeElement("tblTerrazzamento");
      report.removeElement("tblCorpoIdrico");
    }
    else
    {
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
      
      
      //Legenda Terrazzamento ***** inizio ********
      TipoTerrazzamentoVO[] elencoTerrazzamento  = anagFacadeClient
        .getListTipoTerrazzamento("ID_TERRAZZAMENTO", dataInserimentoDichiarazione); 
      
      tblTemp = new DefaultTableLens(report.getTable("tblTerrazzamento"));
      
      tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
      tblTemp.setColWidth(0, 9);
      tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
      tblTemp.setColWidth(1, 80);
      
      String legendaTerrazzamento = "";
      
      for(int i=0;i<elencoTerrazzamento.length;i++)
      {
        legendaTerrazzamento += elencoTerrazzamento[i].getIdTerrazzamento() +" - ";
        legendaTerrazzamento += elencoTerrazzamento[i].getDescrizione();
        
        if(i != (elencoTerrazzamento.length -1))
        {
          legendaTerrazzamento += ", ";
        }      
      }
      
      tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaTerrazzamento));
      tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
      tblTemp.setColWidth(2, 739);
      
      StampeGaaServlet.setNoBorder(tblTemp);
      report.setElement("tblTerrazzamento", tblTemp);    
      //Legenda Terrazzamento ***** fine ********
      
      
      if(tipoModelloStampa == SolmrConstants.STAMPA_PRE_CORPO_IDRICO)
      {
        report.removeElement("tblCorpoIdrico");
      }
      else
      {
        //Legenda Stato corpo idrico ***** inizio ********
        CodeDescription[] elencoAreaM  = anagFacadeClient
          .getListTipoAreaM("ID_AREA_M"); 
        
        tblTemp = new DefaultTableLens(report.getTable("tblCorpoIdrico"));
        
        tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
        tblTemp.setColWidth(0, 12);
        tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_ITALIC_7);
        tblTemp.setColWidth(1, 77);
        String legendaAreaM = "";
        
        for(int i=0;i<elencoAreaM.length;i++)
        {
          legendaAreaM += elencoAreaM[i].getCode() +" - ";
          legendaAreaM += elencoAreaM[i].getDescription();
          
          if(i != (elencoAreaM.length -1))
          {
            legendaAreaM += ", ";
          }      
        }
        
        tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(legendaAreaM));
        tblTemp.setAlignment(0, 2, StyleConstants.H_LEFT);
        tblTemp.setColWidth(2, 739);
        
        StampeGaaServlet.setNoBorder(tblTemp);
        report.setElement("tblCorpoIdrico", tblTemp);    
        //Legenda Terrazzamento ***** fine ******** 
      }
      
    }
    
    
  
  
  }
}