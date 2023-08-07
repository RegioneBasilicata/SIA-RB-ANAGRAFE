package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentoProprietari extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoProprietari.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_PROPRIETARI";

  public DocumentoProprietari() throws IOException, SolmrException
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
    
    
    
    Vector<DocumentoProprietarioVO> elencoProprietari = documentoVO.getElencoProprietari();    
    int num = elencoProprietari.size();
    if (num > 0)
    {
      // Rimuovo il testo indicante l'assenza di UTE associate all'azienda
      subReport.removeElement("txtNessunProprietario");

      DefaultTableLens tblProprietari = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblProprietari = new DefaultTableLens(subReport.getTable("tblProprietari"));

      tblProprietari.setColWidth(0, 100);
      tblProprietari.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblProprietari.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblProprietari.setColWidth(1, 478);
      tblProprietari.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblProprietari.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < num; i++)
      {
        tblProprietari.addRow();
        DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)elencoProprietari.get(i);
        tblProprietari.setObject(i + 1, 0, StampeGaaServlet.checkNull(documentoProprietarioVO.getCuaa()));
        tblProprietari.setObject(i + 1, 1, StampeGaaServlet.checkNull(documentoProprietarioVO.getDenominazione()));
      }
      
      subReport.setElement("tblProprietari", tblProprietari);

    }
    else
    {
      // Rimuovo gli elementi non più necessari
      subReport.removeElement("tblProprietari");
    }
    
    
    
    
    
    
    
    
    
  }
}