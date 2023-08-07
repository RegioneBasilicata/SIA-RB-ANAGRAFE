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
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentiElenco extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/DocumentiElenco.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTI_ELENCO";

  public DocumentiElenco() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    
    
    Vector<DocumentoVO> documenti = anagFacadeClient.getDocumentiStampa(
        anagAziendaVO.getIdAzienda(), idDichiarazioneConsistenza,
        anagAziendaVO.getCUAA(), dataInserimentoDichiarazione);
    int size = 0;
    if (documenti != null)
      size = documenti.size();

    if (size > 0)
    {
      subReport.removeElement("nlDocumenti1");
      subReport.removeElement("txtNoDocumenti");
      
      DefaultTableLens tblDocumenti = null;
      
      // Recupero le coordinate della cella in cui mi trovo
      tblDocumenti = new DefaultTableLens(subReport.getTable("tblDocumenti"));

      tblDocumenti.setColWidth(0, 90);
      tblDocumenti.setColWidth(1, 390);
      tblDocumenti.setColWidth(2, 46);
      tblDocumenti.setColWidth(3, 46);
      //tblDocumenti.setColWidth(4, 180);

      tblDocumenti.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 3, StyleConstants.H_CENTER);
      //tblDocumenti.setAlignment(0, 4, StyleConstants.H_CENTER);

      tblDocumenti.setAlignment(1, 0, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(1, 1, StyleConstants.H_LEFT);
      tblDocumenti.setAlignment(1, 2, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(1, 3, StyleConstants.H_CENTER);
      //tblDocumenti.setAlignment(1, 4, StyleConstants.H_CENTER);

      tblDocumenti.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      //tblDocumenti.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblDocumenti.setFont(1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(1, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(1, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      //tblDocumenti.setFont(1, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);

      tblDocumenti.setColAlignment(0, StyleConstants.H_CENTER);
      tblDocumenti.setColAlignment(1, StyleConstants.H_LEFT);
      tblDocumenti.setColAlignment(2, StyleConstants.H_CENTER);
      tblDocumenti.setColAlignment(3, StyleConstants.H_CENTER);
      //tblDocumenti.setColAlignment(4, StyleConstants.H_CENTER);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < size; i++)
      {
        tblDocumenti.addRow();
        DocumentoVO documentoVO = (DocumentoVO) documenti.get(i);
        TipoDocumentoVO tipoDocumentoVO = documentoVO.getTipoDocumentoVO();

        if (tipoDocumentoVO != null
            && tipoDocumentoVO.getTipoTipologiaDocumento() != null)
          tblDocumenti.setObject(i + 2, 0, StampeGaaServlet.checkNull(tipoDocumentoVO
              .getTipoTipologiaDocumento().getDescription()));
        if (tipoDocumentoVO != null)
          tblDocumenti.setObject(i + 2, 1, StampeGaaServlet.checkNull(tipoDocumentoVO
              .getDescrizione()));
        tblDocumenti.setObject(i + 2, 2, DateUtils.formatDate(documentoVO
            .getDataInizioValidita()));
        if (documentoVO.getDataFineValidita() != null)
          tblDocumenti.setObject(i + 2, 3, DateUtils.formatDate(documentoVO
              .getDataFineValidita()));       
        

      }
      
      
      //tblDocumenti.setHeaderRowCount(2);
      subReport.setElement("tblDocumenti", tblDocumenti);
      

    }
    else
    {
      subReport.removeElement("tblDocumenti");
    }
    
    
    
  }
}