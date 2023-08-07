package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaDichiarataVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.WebUtils;
import it.csi.solmr.util.services.DateUtils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class UVElenco extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/UVElenco.srt";
  private final static String CODICE_SUB_REPORT = "UV_ELENCO";

  public UVElenco() throws IOException, SolmrException
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
    
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
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
    
    
    
    String orderBy[] = null;
    if (idDichiarazioneConsistenza == null)
    {
      orderBy = new String[] {
          SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY,
          SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC,
          SolmrConstants.ORDER_BY_PROGR_UNAR_ASC,
          SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC };
    }
    else
    {
      orderBy = new String[] {
          SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY,
          SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC,
          SolmrConstants.ORDER_BY_PROGR_UNAR_ASC,
          SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC };
    }
    FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = null;
    filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
    filtriUnitaArboreaRicercaVO.setOrderBy(null);
    if (idDichiarazioneConsistenza == null)
    {
      filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(new Long(-1));
    }
    else
    {
      filtriUnitaArboreaRicercaVO
          .setIdPianoRiferimento(idDichiarazioneConsistenza);
    }
    StoricoParticellaVO partVO[] = anagFacadeClient
        .searchStoricoUnitaArboreaByParametersForStampa(
            anagAziendaVO.getIdAzienda(),
            filtriUnitaArboreaRicercaVO, orderBy);

    if (partVO == null || partVO.length == 0)
    {
      subReport.removeElement("tblUnitaArboree");
    }
    else
    {
      subReport.removeElement("txtUnitaArboree");
      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID, elIDCurrent;
      DefaultTableLens tblUnitaArboree = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport.getElement("tblUnitaArboree"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());

      elID = "tblUnitaArboree";
      elIDCurrent = elID;
      element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
      subReport.removeElement("tblUnitaArboree");
      layout.addElement(elStartRow, elStartCol, element);
      tblUnitaArboree = new DefaultTableLens(subReport.getTable(elIDCurrent));

      int col = 0;
      
      if(flagOldStyle)
      {
        tblUnitaArboree.setColWidth(col++, 76);  //comune
        tblUnitaArboree.setColWidth(col++, 10);  //sezione
        tblUnitaArboree.setColWidth(col++, 15);  //foglio
        tblUnitaArboree.setColWidth(col++, 22);  //particella 
        tblUnitaArboree.setColWidth(col++, 10);  //subalterno
        tblUnitaArboree.setColWidth(col++, 30);  //Sup. cat.
        tblUnitaArboree.setColWidth(col++, 15);  //Pgr
        tblUnitaArboree.setColWidth(col++, 30);  //Sup.vit 
        tblUnitaArboree.setColWidth(col++, 90);  //Dest. prod./Vitigno
        tblUnitaArboree.setColWidth(col++, 90);  //Vigna
        tblUnitaArboree.setColWidth(col++, 73);  //Tipologia di vino 
        tblUnitaArboree.setColWidth(col++, 35);  //Sup. iscritta 
        tblUnitaArboree.setColWidth(col++, 18);  //Anno iscr.  
        tblUnitaArboree.setColWidth(col++, 63);  //Matricola
        tblUnitaArboree.setColWidth(col++, 30);  //SsF
        tblUnitaArboree.setColWidth(col++, 30);  //Stf
        tblUnitaArboree.setColWidth(col++, 30);  //N.ceppi
        tblUnitaArboree.setColWidth(col++, 18);  //Anno
        tblUnitaArboree.setColWidth(col++, 60);  //F.all. 
        tblUnitaArboree.setColWidth(col++, 18);  //%V.
      }
      else
      {
        tblUnitaArboree.setColWidth(col++, 80);  //comune
        tblUnitaArboree.setColWidth(col++, 10);  //sezione
        tblUnitaArboree.setColWidth(col++, 15);  //foglio
        tblUnitaArboree.setColWidth(col++, 22);  //particella 
        tblUnitaArboree.setColWidth(col++, 10);  //subalterno
        tblUnitaArboree.setColWidth(col++, 30);  //Sup. cat.
        tblUnitaArboree.setColWidth(col++, 15);  //Pgr
        tblUnitaArboree.setColWidth(col++, 30);  //Sup.vit 
        tblUnitaArboree.setColWidth(col++, 95);  //Dest. prod./Vitigno
        tblUnitaArboree.setColWidth(col++, 96);  //Vigna
        tblUnitaArboree.setObject(0, col, StampeGaaServlet.checkNull("Idoneità"));
        tblUnitaArboree.setColWidth(col++, 73);  //Idoneità 
        tblUnitaArboree.setColWidth(col++, 1);  //Sup. iscritta
        tblUnitaArboree.setObject(0, col, StampeGaaServlet.checkNull("Anno ido."));
        tblUnitaArboree.setColWidth(col++, 18);  //Anno ido.  
        tblUnitaArboree.setColWidth(col++, 63);  //Matricola
        tblUnitaArboree.setColWidth(col++, 30);  //SsF
        tblUnitaArboree.setColWidth(col++, 30);  //Stf
        tblUnitaArboree.setColWidth(col++, 30);  //N.ceppi
        tblUnitaArboree.setObject(0, col, StampeGaaServlet.checkNull("Data impianto"));
        tblUnitaArboree.setColWidth(col++, 37);  //Data Impianto
        tblUnitaArboree.setColWidth(col++, 60);  //F.all. 
        tblUnitaArboree.setColWidth(col++, 18);  //%V.
      }

      for (int i = 0; i <= 19; i++)
      {
        tblUnitaArboree.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblUnitaArboree.setAlignment(0, i, StyleConstants.H_CENTER);
      }

      for (int i = 0; i < partVO.length; i++)
      {        
        col = 0;

        StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) partVO[i];
        if (storicoParticellaVO != null)
        {
          StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
          UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = null;
          if (idDichiarazioneConsistenza == null)
            // alla data odierna
            storicoUnitaArboreaVO = storicoParticellaVO
                .getStoricoUnitaArboreaVO();
          else
            // alla dichiarazione
            unitaArboreaDichiarataVO = storicoParticellaVO
                .getUnitaArboreaDichiarataVO();

          if (storicoUnitaArboreaVO != null)
          {
            tblUnitaArboree.addRow();
            String comune = null;
            if (storicoParticellaVO.getComuneParticellaVO() != null)
            {
              comune = StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                  .getDescom()+ " (");
              comune += StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                      .getSiglaProv()) + ")";
            }

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(comune));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_CENTER);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getSezione()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getFoglio()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getParticella()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_CENTER);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getSubalterno()));
                        
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            try
            {
              tblUnitaArboree.setObject(i + 1, col, StringUtils
                  .parseSuperficieField(storicoParticellaVO.getSupCatastale()));
            }
            catch (Exception e)
            {}
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoUnitaArboreaVO
                .getProgrUnar()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            try
            {
              tblUnitaArboree.setObject(i + 1, col, StringUtils
                  .parseSuperficieField(storicoUnitaArboreaVO.getArea()));
            }
            catch (Exception e)
            {}
            col++;

            String destProdVitigno = "";
            
            if (storicoUnitaArboreaVO.getIdUtilizzo() != null)
            {
              String codice = "";
              if (Validator.isNotEmpty(storicoUnitaArboreaVO
                  .getTipoUtilizzoVO().getCodice()))
              {
                codice += "["
                    + storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()
                    + "] ";
                
                destProdVitigno = codice + storicoUnitaArboreaVO.getTipoUtilizzoVO()
                  .getDescrizione(); 
              }
            }
            
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if (storicoUnitaArboreaVO.getIdVarieta() != null)
            {
              if(Validator.isNotEmpty(destProdVitigno))
              {
                destProdVitigno += " - ";
              }
              
              destProdVitigno += "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+ "] ";
              destProdVitigno += storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione();
              tblUnitaArboree.setObject(i + 1, col, StampeGaaServlet.checkNull(destProdVitigno));
            }
            col++;
            
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col, StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getVigna()));            
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if(storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null)
            { 
              tblUnitaArboree.setObject(i + 1, col,
                  StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getDescrizione()));  
            }
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getAnnoIscrizioneAlbo()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getMatricolaCCIAA()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getSestoSuFila()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getSestoTraFile()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getNumCeppi()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            
            if(flagOldStyle)
            {
              tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getAnnoImpianto()));
            }
            else
            {
              if(storicoUnitaArboreaVO.getDataImpianto() != null)
              {
                tblUnitaArboree.setObject(i + 1, col++,
                    DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
              }
            }

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if (storicoUnitaArboreaVO.getIdFormaAllevamento() != null)
              tblUnitaArboree.setObject(i + 1, col,
                  StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getTipoFormaAllevamentoVO()
                      .getDescrizione()));
            col++;

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(storicoUnitaArboreaVO.getPercentualeVarieta()));
          }
          else
          {
            tblUnitaArboree.addRow();
            String comune = null;
            if (storicoParticellaVO.getComuneParticellaVO() != null)
            {
              comune = StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                  .getDescom());
              comune += "("
                  + StampeGaaServlet.checkNull(storicoParticellaVO.getComuneParticellaVO()
                      .getSiglaProv()) + ")";
            }

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(comune));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_CENTER);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getSezione()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getFoglio()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getParticella()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_CENTER);
            tblUnitaArboree.setObject(i + 1, col++, StampeGaaServlet.checkNull(storicoParticellaVO
                .getSubalterno()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            try
            {
              tblUnitaArboree.setObject(i + 1, col, StringUtils
                  .parseSuperficieField(storicoParticellaVO.getSupCatastale()));
            }
            catch (Exception e)
            {}
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getProgrUnar()));

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            try
            {
              tblUnitaArboree.setObject(i + 1, col, StringUtils
                  .parseSuperficieField(unitaArboreaDichiarataVO.getArea()));
            }
            catch (Exception e)
            {}
            col++;

            String destProdVitigno = "";
            if (unitaArboreaDichiarataVO.getIdUtilizzo() != null)
            {
              String codice = "";
              if (Validator.isNotEmpty(unitaArboreaDichiarataVO
                  .getTipoUtilizzoVO().getCodice()))
              {
                codice += "["
                    + unitaArboreaDichiarataVO.getTipoUtilizzoVO().getCodice()
                    + "] ";
                
                destProdVitigno += codice + unitaArboreaDichiarataVO.getTipoUtilizzoVO()
                  .getDescrizione();
              }
            }
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if (unitaArboreaDichiarataVO.getIdVarieta() != null)
            {
              if(Validator.isNotEmpty(destProdVitigno))
              {
                destProdVitigno += " - ";
              }
              
              destProdVitigno += "[" + unitaArboreaDichiarataVO.getTipoVarietaVO()
                    .getCodiceVarieta()+ "] ";
              destProdVitigno += unitaArboreaDichiarataVO.getTipoVarietaVO()
                .getDescrizione();
              tblUnitaArboree.setObject(i + 1, col, StampeGaaServlet.checkNull(destProdVitigno));
            }
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col, StampeGaaServlet.checkNull(unitaArboreaDichiarataVO
                .getVigna()));
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if(unitaArboreaDichiarataVO.getTipoTipologiaVinoVO() !=null)
            { 
              tblUnitaArboree.setObject(i + 1, col,
                  StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getDescrizione()));  
            }
            col++;
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StringUtils.parseSuperficieField(unitaArboreaDichiarataVO.getSuperficieDaIscrivereAlbo()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getAnnoIscrizioneAlbo()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getMatricolaCCIAA()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getSestoSuFila()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getSestoTraFile()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getNumCeppi()));
            
            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            if(flagOldStyle)
            {
              tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getAnnoImpianto()));
            }
            else
            {
              if(unitaArboreaDichiarataVO.getDataImpianto() !=null)
              {
                tblUnitaArboree.setObject(i + 1, col++,
                    DateUtils.formatDate(unitaArboreaDichiarataVO.getDataImpianto()));
              }
            }

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_LEFT);
            if (unitaArboreaDichiarataVO.getIdFormaAllevamento() != null)
              tblUnitaArboree.setObject(i + 1, col,
                  StampeGaaServlet.checkNull(unitaArboreaDichiarataVO
                      .getTipoFormaAllevamentoVO().getDescrizione()));
            col++;

            tblUnitaArboree.setAlignment(i + 1, col, StyleConstants.H_RIGHT);
            tblUnitaArboree.setObject(i + 1, col++,
                StampeGaaServlet.checkNull(unitaArboreaDichiarataVO.getPercentualeVarieta()));
            
          }

        }
        // Aggiungo la riga dei totali
      }
      tblUnitaArboree.addRow();

      int nRow = tblUnitaArboree.getRowCount() - 1;

      tblUnitaArboree.setAlignment(nRow, 0, StyleConstants.H_LEFT);
      tblUnitaArboree.setAlignment(nRow, 5, StyleConstants.H_RIGHT);
      tblUnitaArboree.setAlignment(nRow, 7, StyleConstants.H_RIGHT);
      tblUnitaArboree.setAlignment(nRow, 11, StyleConstants.H_RIGHT);

      tblUnitaArboree.setSpan(nRow, 0 , new Dimension(2, 1));
      tblUnitaArboree.setFont(nRow, 0, StampeGaaServlet.FONT_SERIF_BOLD_8);
      tblUnitaArboree.setObject(nRow, 0, StampeGaaServlet.checkNull("Totale superficie (ha)"));
      tblUnitaArboree.setFont(nRow, 5, StampeGaaServlet.FONT_SERIF_BOLD_8);
      tblUnitaArboree.setObject(nRow, 5, StringUtils
          .parseSuperficieField(WebUtils.calcolaTotSupCatastaleUnar(partVO)));
      tblUnitaArboree.setFont(nRow, 7, StampeGaaServlet.FONT_SERIF_BOLD_8);
      tblUnitaArboree.setObject(nRow, 7, StringUtils
          .parseSuperficieField(WebUtils.calcolaTotSupVitata(partVO,
              filtriUnitaArboreaRicercaVO)));
      tblUnitaArboree.setFont(nRow, 11, StampeGaaServlet.FONT_SERIF_BOLD_8);
      tblUnitaArboree.setObject(nRow, 11, StringUtils
          .parseSuperficieField(WebUtils.calcolaTotSupDaIscrivereAlbo(partVO,
              filtriUnitaArboreaRicercaVO)));
      
      
      if(flagOldStyle)
      {
        setNoBorderQuadroI3Old(tblUnitaArboree);
      }
      else
      {
        //Rimuovo le nuove colonne
        tblUnitaArboree.removeColumn(11);
        
        setNoBorderQuadroI3(tblUnitaArboree);
      }

      subReport.setElement(elIDCurrent, tblUnitaArboree);
    }
    
    
   
    
  }
  
  
  private final void setNoBorderQuadroI3Old(DefaultTableLens tblLens)
  {
    int nRow = tblLens.getRowCount() - 1;
    for (int j = -1; j < 4; j++)
      tblLens.setColBorder(nRow, j, StyleConstants.NO_BORDER);
    
    for (int j = 8; j <= 9; j++)
      tblLens.setColBorder(nRow, j, StyleConstants.NO_BORDER);
    
    for (int j = 12; j <= 20; j++)
      tblLens.setColBorder(nRow, j, StyleConstants.NO_BORDER);

    for (int j = 0; j <= 4; j++)
      tblLens.setRowBorder(nRow, j, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(nRow, 6, StyleConstants.NO_BORDER);
    for (int j = 8; j <= 10; j++)
      tblLens.setRowBorder(nRow, j, StyleConstants.NO_BORDER);
    for (int j = 12; j <= 19; j++)
      tblLens.setRowBorder(nRow, j, StyleConstants.NO_BORDER);
  }
  
  
  
  private final void setNoBorderQuadroI3(DefaultTableLens tblLens)
  {
    int nRow = tblLens.getRowCount() - 1;
    for (int j = -1; j < 4; j++)
      tblLens.setColBorder(nRow, j, StyleConstants.NO_BORDER);
    for (int j = 0; j <= 4; j++)
      tblLens.setRowBorder(nRow, j, StyleConstants.NO_BORDER);
    
    tblLens.setRowBorder(nRow, 6, StyleConstants.NO_BORDER);
    
    for (int j = 8; j <= 19; j++)
      tblLens.setColBorder(nRow, j, StyleConstants.NO_BORDER);
    for (int j = 8; j <= 19; j++)
      tblLens.setRowBorder(nRow, j, StyleConstants.NO_BORDER);
  }
  
  
}