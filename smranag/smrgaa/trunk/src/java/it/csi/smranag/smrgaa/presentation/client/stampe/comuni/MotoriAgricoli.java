package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.uma.DatiMacchinaVO;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.MatriceVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class MotoriAgricoli extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/MotoriAgricoli.srt";
  private final static String CODICE_SUB_REPORT = "MOTORI_AGRICOLI";

  public MotoriAgricoli() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    boolean situazioneAttuale = true;
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    if(idDichiarazioneConsistenza != null)
    {
      situazioneAttuale = false;
    }
    
    
    
    
    
    
    
    Vector<MacchinaVO> vMotoriAgricoli = null;
    MacchinaVO macchinaVO = null;
    DatiMacchinaVO datiMacchinaVO = null;
    MatriceVO matriceVO = null;
    try
    {
      vMotoriAgricoli = gaaFacadeClient.serviceGetElencoMacchineByIdAzienda(
          anagAziendaVO.getIdAzienda(), new Boolean(situazioneAttuale), null);
    }
    catch (Exception e)
    {
      if (e != null)
        SolmrLogger.error(this, "stampaQuadroM " + e.getMessage());
      
      subReport.removeElement("nlMotoriAgricoli1");
      subReport.removeElement("txtNoMotoriAgricoli");
      subReport.removeElement("tblMotoriAgricoli");
      return;
    }
    int size = 0;
    if (vMotoriAgricoli != null)
      size = vMotoriAgricoli.size();
    
    if (size > 0)
    {
      String nomeTabella = "tblMotoriAgricoli";
      subReport.removeElement("nlMotoriAgricoli1");
      subReport.removeElement("txtServizioMotoriNonFunzionante");

      DefaultTableLens tblTemp = null;

      tblTemp = new DefaultTableLens(subReport.getTable(nomeTabella));
      
      tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblTemp.setFont(0, 6, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      tblTemp.setColWidth(0, 30);
      tblTemp.setColWidth(1, 150);
      tblTemp.setColWidth(2, 130);
      tblTemp.setColWidth(3, 80);
      tblTemp.setColWidth(4, 60);
      tblTemp.setColWidth(5, 60);
      tblTemp.setColWidth(6, 50);
      
      int posRiga = 0;// posizione in cui verrà disegnata la nuova riga
      String genere = null;
      String categoria = null;
      String marca = null;
      String tipo = null;
      String potenza = null;

      for (int nRiga = 0; nRiga < vMotoriAgricoli.size(); nRiga++)
      {
        macchinaVO = (MacchinaVO) vMotoriAgricoli
            .get(nRiga);

        if (macchinaVO != null)
        {
          
          if (!situazioneAttuale)
          {
            // In questa sezione vengono stampate tutte le macchine agricole di una determinata azienda alla data della 
            // dichiarazione di consistenza, cioè per
            // db_utilizzo.data_carico <= data della dichiarazione di consistenza
            // db_utilizzo.data_scarico a null oppure maggiore della data della dichiarazione di consistenza

            if (!(
                !macchinaVO.getUtilizzoVO().getDataCaricoDate().after(dataInserimentoDichiarazione)
                &&
                (
                  macchinaVO.getUtilizzoVO().getDataScaricoDate()==null 
                    || macchinaVO.getUtilizzoVO().getDataScaricoDate().after(dataInserimentoDichiarazione)
                )
               ))
            {
              continue;
            }
          }
          
          datiMacchinaVO = macchinaVO.getDatiMacchinaVO();
          matriceVO = macchinaVO.getMatriceVO();

          posRiga ++;

          if (matriceVO == null)
          {
            // Dati Macchina
            datiMacchinaVO = macchinaVO.getDatiMacchinaVO();

            if (datiMacchinaVO != null)
            {
              genere = datiMacchinaVO.getCodBreveGenereMacchina();
              categoria = datiMacchinaVO.getDescCategoria();
              marca = datiMacchinaVO.getMarca();
              tipo = datiMacchinaVO.getTipoMacchina();
              potenza = datiMacchinaVO.getPotenza();
            }
          }
          else
          {
            // Dati Matrice
            genere = matriceVO.getCodBreveGenereMacchina();
            categoria = matriceVO.getDescCategoria();
            marca = matriceVO.getDescMarca();
            tipo = matriceVO.getTipoMacchina();
            potenza = matriceVO.getPotenzaKW();
          }

          tblTemp.insertRow(posRiga);

          // Contenuto celle
          tblTemp.setObject(posRiga, 0, StampeGaaServlet.checkNull(genere));
          tblTemp.setObject(posRiga, 1, StampeGaaServlet.checkNull(categoria));
          tblTemp.setObject(posRiga, 2, StampeGaaServlet.checkNull(marca));
          tblTemp.setObject(posRiga, 3, StampeGaaServlet.checkNull(tipo));
          if (macchinaVO.getTargaCorrente() != null
              && macchinaVO.getTargaCorrente().getNumeroTarga() != null)
            tblTemp.setObject(posRiga, 4, macchinaVO.getTargaCorrente()
                .getNumeroTarga());
          else
            tblTemp.setObject(posRiga, 4, "");
          tblTemp.setObject(posRiga, 5, StampeGaaServlet.checkNull(potenza));
          tblTemp.setObject(posRiga, 6,
              StampeGaaServlet.checkNull(DateUtils.formatDate(macchinaVO.getUtilizzoVO().getDataCaricoDate())));

          // Allineamento celle
          tblTemp.setAlignment(posRiga, 4, StyleConstants.H_CENTER);
          tblTemp.setAlignment(posRiga, 5, StyleConstants.H_CENTER);
          tblTemp.setAlignment(posRiga, 6, StyleConstants.H_CENTER);
        }
      }
      if (posRiga==0)
      {
        subReport.removeElement("tblMotoriAgricoli");
      }
      else
      {
        subReport.removeElement("txtNoMotoriAgricoli");
        subReport.setElement(nomeTabella, tblTemp);
      }
    }
    else
    {
      subReport.removeElement("nlMotoriAgricoli1");
      subReport.removeElement("txtServizioMotoriNonFunzionante");
      subReport.removeElement("tblMotoriAgricoli");
    }
    
    
    
  }
}