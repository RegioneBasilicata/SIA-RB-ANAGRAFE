package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import inetsoft.report.painter.ImagePainter;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class DocumentoContoCorrente extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoContoCorrente.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_CONTO_CORRENTE";

  public DocumentoContoCorrente() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    DocumentoVO documentoVO = (DocumentoVO)parametri.get("documentoVO");
    
    
    // Recupero l'immagine del check e dell'uncheck
    Image check;

    check = Toolkit.getDefaultToolkit().createImage(
        getClass().getClassLoader().getResource(
            SolmrConstants.get("IMMAGINE_PDF_CHECK").toString()));
    
    ImagePainter ipOK = new ImagePainter(check, true)
    {
      private static final long serialVersionUID = 1L;

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
    
    
    ContoCorrenteVO contoCorrenteVO = anagFacadeClient
      .getContoCorrente(documentoVO.getIdContoCorrente().toString());
  
    if (contoCorrenteVO != null)
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunContoCorrente");

      DefaultTableLens tblContoCorrente = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblContoCorrente = new DefaultTableLens(subReport.getTable("tblContoCorrente"));

      tblContoCorrente.setColWidth(0, 146);
      tblContoCorrente.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblContoCorrente.setColWidth(1, 146);
      tblContoCorrente.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblContoCorrente.setColWidth(2, 146);
      tblContoCorrente.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblContoCorrente.setColWidth(3, 50);
      tblContoCorrente.setAlignment(0, 3, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblContoCorrente.setColWidth(4, 50);
      tblContoCorrente.setAlignment(0, 4, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblContoCorrente.setColWidth(5, 28);
      tblContoCorrente.setAlignment(0, 5, StyleConstants.H_CENTER);
      tblContoCorrente.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);

      
      tblContoCorrente.addRow();
      //tblContoCorrente.setAlignment(1, 0, StyleConstants.H_CENTER);
      tblContoCorrente.setObject(1, 0, StampeGaaServlet.checkNull(
          contoCorrenteVO.getDenominazioneBanca()));
      tblContoCorrente.setObject(1, 1, StampeGaaServlet.checkNull(
          contoCorrenteVO.getDenominazioneSportello()));
      tblContoCorrente.setObject(1, 2, StampeGaaServlet.checkNull(
          contoCorrenteVO.getIban()));
      tblContoCorrente.setAlignment(1, 3, StyleConstants.H_CENTER);
      tblContoCorrente.setObject(1, 3, StampeGaaServlet.checkNull(
          DateUtils.formatDateNotNull(contoCorrenteVO.getDataInizioValiditaContoCorrente())));
      tblContoCorrente.setAlignment(1, 4, StyleConstants.H_CENTER);
      Date dataFineValidita = contoCorrenteVO.getDataEstinzione();
      if (dataFineValidita == null)
      {
        dataFineValidita = contoCorrenteVO.getDataFineValiditaContoCorrente();
      }
      tblContoCorrente.setObject(1, 4, StampeGaaServlet.checkNull(
          DateUtils.formatDateNotNull(dataFineValidita)));
      
      tblContoCorrente.setAlignment(1, 5, StyleConstants.H_CENTER);
      if(Validator.isNotEmpty(contoCorrenteVO.getflagValidato()) 
          && contoCorrenteVO.getflagValidato().equalsIgnoreCase("S"))
      {
        tblContoCorrente.setObject(1, 5, ipOK);
      }
      
      
      subReport.setElement("tblContoCorrente", tblContoCorrente);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblContoCorrente");
    }
    
    
    
    
    
    
    
    
    
  }
}