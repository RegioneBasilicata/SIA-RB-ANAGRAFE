package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import inetsoft.report.painter.ImagePainter;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.ValoriCondizionalitaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Dichiarazioni extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Dichiarazioni.srt";
  private final static String CODICE_SUB_REPORT = "DICHIARAZIONI";

  public Dichiarazioni() throws IOException, SolmrException
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
    
    //Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    Long codiceFotografia = (Long)parametri.get("codiceFotografia");
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    DichiarazioneConsistenzaGaaVO  dichConsistenza = 
        (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    
    
    
    
    Long idAllegato = richiestaTipoReportVO.getIdReportSubReport();

    TipoAttestazioneVO[] elencoAttestazioni = null;

    if (idDichiarazioneConsistenza == null)
    {
      elencoAttestazioni = gaaFacadeClient.getListAttestazioniAlPianoAttuale(
          anagAziendaVO.getIdAzienda(), idAllegato);
    }
    else
    {
      Date parametroDtFineCondiz = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DT_FINE_CONDIZ);
      Vector<java.util.Date> vDateVariazioni = anagFacadeClient.getDateVariazioniAllegati(codiceFotografia);
      
      if(vDateVariazioni != null)
      {
        Date dataVariazioneDt = vDateVariazioni.get(0);
        
        Date dataDaPassare = null;
        //Pastruzzo pe ril 2012
        if(!"C".equalsIgnoreCase(dichConsistenza.getTipoMotivo())
          && dataVariazioneDt.after(DateUtils.parseDate("31/12/2011")) 
          && dataVariazioneDt.before(parametroDtFineCondiz))
        {
          dataDaPassare = dataVariazioneDt;
        }
        else
        {
          Date dataAnnoCampagna = DateUtils.parseDate("31/12/"+dichConsistenza.getAnno());
          if(Validator.isNotEmpty(dichConsistenza.getAnnoCampagna()))
          {
            dataAnnoCampagna = DateUtils.parseDate("31/12/"+dichConsistenza.getAnnoCampagna());
          }
          dataDaPassare = dataAnnoCampagna;
        }
        
        
        elencoAttestazioni = gaaFacadeClient
          .getListAttestazioneAllaDichiarazione(codiceFotografia, dataDaPassare, idAllegato);
      }
      
    }

    if (elencoAttestazioni != null && elencoAttestazioni.length != 0)
    {
      subReport.removeElement("txtNoAllegati");
      ReportElement element = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID, elIDCurrent;
      DefaultTableLens quadroVTbl = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport.getElement("quadroVTbl"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());
      elID = "quadroVTbl";

      // Recupero l'immagine del check e dell'uncheck
      Image check, unCheck;

      check = Toolkit.getDefaultToolkit().createImage(
          getClass().getClassLoader().getResource(
              SolmrConstants.get("IMMAGINE_PDF_CHECK").toString()));
      
      ImagePainter ipOK = new ImagePainter(check, true)
      {
        private static final long serialVersionUID = 881297414056681752L;

        @Override
        public Dimension getPreferredSize()
        {
          return new Dimension(9,8);
        }
        
        public void paint(Graphics g, int x, int y, int w, int h)
        {
          g.drawImage(getImage(), x + ((w - 9) / 2), y + ((h - 8) / 2), 9, 8, null);
        }
      };
      
      unCheck = Toolkit.getDefaultToolkit().createImage(
          getClass().getClassLoader().getResource(
              SolmrConstants.get("IMMAGINE_PDF_UNCHECK").toString()));
      
      ImagePainter ipNoOK = new ImagePainter(unCheck, true)
      {
        private static final long serialVersionUID = 3523351272380175903L;

        @Override
        public Dimension getPreferredSize()
        {
          return new Dimension(9,8);
        }
        
        public void paint(Graphics g, int x, int y, int w, int h)
        {
          g.drawImage(getImage(), x + ((w - 9) / 2), y + ((h - 8) / 2), 9, 8, null);
        }
      };

      String tipoAttestazione = elencoAttestazioni[0].getCodiceAttestazione();
      for (int i = 0; i < elencoAttestazioni.length; i++)
      {
        TipoAttestazioneVO tipoAttestazioneVO = elencoAttestazioni[i];
        elIDCurrent = elID + i;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);

        quadroVTbl = new DefaultTableLens(subReport.getTable(elIDCurrent));
        quadroVTbl.setColWidth(0, 11);
        quadroVTbl.setColWidth(1, 1);
        quadroVTbl.setColWidth(2, 11);
        quadroVTbl.setColWidth(3, 1);
        quadroVTbl.setColWidth(4, 548);

        StampeGaaServlet.setNoBorder(quadroVTbl);

        if (SolmrConstants.TIPO_ATTESTAZIONE_CARATTERE_BOLD
            .equalsIgnoreCase(tipoAttestazioneVO.getTipoCarattere()))
        {
          quadroVTbl.setFont(StampeGaaServlet.FONT_SERIF_BOLD_8);
        }
        else
        {
          quadroVTbl.setFont(StampeGaaServlet.FONT_SERIF_8);
        }

        if (tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(
            SolmrConstants.NUMERO_RIGHE_INTESTAZIONE_ATTESTAZIONE))
        {
          // Solo nel caso in cui è presente una sola colonna quindi devo unirle
          // tutte
          quadroVTbl.setSpan(0, 0, new Dimension(5, 1));
          quadroVTbl.setObject(0, 0, StampeGaaServlet.checkNull(tipoAttestazioneVO
              .getDescrizione()));
        }
        else if (tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(
            SolmrConstants.NUMERO_RIGHE_MONO_ATTESTAZIONE))
        {
          // Solo nel caso in cui sono presente due sola colonne quindi devo
          // unirne 3
          quadroVTbl.setSpan(0, 2, new Dimension(3, 1));
          if (tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(
              SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX))
          {
            if (tipoAttestazioneVO.isAttestazioneAzienda())
              quadroVTbl.setObject(0, 0, ipOK);
            else
              quadroVTbl.setObject(0, 0, ipNoOK);
          }
          else if (!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(
              SolmrConstants.FLAG_N))
          {
            quadroVTbl.setAlignment(0, 0, StyleConstants.V_TOP);
            quadroVTbl.setObject(0, 0, StampeGaaServlet.checkNull(tipoAttestazioneVO
                .getTipoRiga()));
          }
          quadroVTbl.setObject(0, 2, StampeGaaServlet.checkNull(tipoAttestazioneVO
              .getDescrizione()));
        }
        else if (tipoAttestazioneVO.getNumeroColonneRiga().equalsIgnoreCase(
            SolmrConstants.NUMERO_RIGHE_DOUBLE_ATTESTAZIONE))
        {
          if (tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(
              SolmrConstants.TIPO_RIGHE_ATTESTAZIONE_CHECKBOX))
          {
            if (tipoAttestazioneVO.isAttestazioneAzienda())
              quadroVTbl.setObject(0, 2, ipOK);
            else
              quadroVTbl.setObject(0, 2, ipNoOK);
          }
          else if (!tipoAttestazioneVO.getTipoRiga().equalsIgnoreCase(
              SolmrConstants.FLAG_N))
          {
            quadroVTbl.setAlignment(0, 2, StyleConstants.V_TOP);
            quadroVTbl.setObject(0, 2, StampeGaaServlet.checkNull(tipoAttestazioneVO
                .getTipoRiga()));
          }
          quadroVTbl.setObject(0, 4, StampeGaaServlet.checkNull(tipoAttestazioneVO
              .getDescrizione()));
        }

        subReport.setElement(elIDCurrent, quadroVTbl);
      }
      
      //La tabellinala faccio vedere solo per la condizionalità
      if(tipoAttestazione.equalsIgnoreCase("COND"))
      {
        ValoriCondizionalitaVO valVO = gaaFacadeClient.getValoriCondizionalita(
            anagAziendaVO.getIdAzienda(), codiceFotografia);
        creaSubReportRilevanza(subReport, valVO);
      }
      else
      {
        StampeGaaServlet.removeRows(layout, "tblZonaRilevanza", 1);
      }
    }
    else
    {
      StampeGaaServlet.removeRows(layout, "tblZonaRilevanza", 1);
    }
    
    
    subReport.removeElement("nwlQudroV");
    subReport.removeElement("quadroVTbl");   
    
    
  }
  
  
  private void creaSubReportRilevanza(ReportSheet subReport, ValoriCondizionalitaVO valVO)
  {
    DefaultTableLens tblZonaRilevanza = new DefaultTableLens(subReport
        .getTable("tblZonaRilevanza"));

    tblZonaRilevanza.setColWidth(0, 240);
    tblZonaRilevanza.setColWidth(1, 120);
    tblZonaRilevanza.setColWidth(2, 120);

    tblZonaRilevanza.setAlignment(0, 1, StyleConstants.H_CENTER);
    tblZonaRilevanza.setAlignment(0, 2, StyleConstants.H_CENTER);

    tblZonaRilevanza.setAlignment(1, 0, StyleConstants.H_LEFT);

    tblZonaRilevanza.setAlignment(2, 0, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(2, 1, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(2, 2, StyleConstants.H_RIGHT);

    tblZonaRilevanza.setAlignment(3, 0, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(3, 1, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(3, 2, StyleConstants.H_RIGHT);

    tblZonaRilevanza.setAlignment(4, 0, StyleConstants.H_LEFT);

    tblZonaRilevanza.setAlignment(5, 0, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(5, 1, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(5, 2, StyleConstants.H_RIGHT);
    
    tblZonaRilevanza.setAlignment(6, 0, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(6, 1, StyleConstants.H_RIGHT);
    tblZonaRilevanza.setAlignment(6, 2, StyleConstants.H_RIGHT);

    tblZonaRilevanza.setSpan(1, 0, new Dimension(3, 1));
    tblZonaRilevanza.setSpan(4, 0, new Dimension(3, 1));

    tblZonaRilevanza.setObject(2, 1, StringUtils.parseSuperficieField(String
        .valueOf(valVO.getSic())));
    tblZonaRilevanza.setObject(3, 1, StringUtils.parseSuperficieField(String
        .valueOf(valVO.getZps())));
    tblZonaRilevanza.setObject(5, 1, StringUtils.parseSuperficieField(String
        .valueOf(valVO.getZvn())));
    tblZonaRilevanza.setObject(6, 1, StringUtils.parseSuperficieField(String
        .valueOf(valVO.getFasceFluviali())));

    if (valVO.getTotSupCondotta() != 0)
    {
      double temp = 0;
      if (valVO.getSic() != 0)
      {
        temp = (valVO.getSic() * 100)
            / valVO.getTotSupCondotta();
        tblZonaRilevanza.setObject(2, 2, StringUtils.parseEuroField(String
            .valueOf(temp)));
      }

      if (valVO.getZps() != 0)
      {
        temp = (valVO.getZps() * 100)
            / valVO.getTotSupCondotta();
        tblZonaRilevanza.setObject(3, 2, StringUtils.parseEuroField(String
            .valueOf(temp)));
      }

      if (valVO.getZvn() != 0)
      {
        temp = (valVO.getZvn() * 100)
            / valVO.getTotSupCondotta();
        tblZonaRilevanza.setObject(5, 2, StringUtils.parseEuroField(String
            .valueOf(temp)));
      }
      if (valVO.getFasceFluviali()!= 0)
      {
        temp = (valVO.getFasceFluviali() * 100)
            / valVO.getTotSupCondotta();
        tblZonaRilevanza.setObject(6, 2, StringUtils.parseEuroField(String
            .valueOf(temp)));
      }
    }
    
    subReport.setElement("tblZonaRilevanza", tblZonaRilevanza);
  }
  
}