package it.csi.solmr.servlet.anag;

import inetsoft.report.ReportElement;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.awt.Font;
import java.awt.Point;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class StampaServletProtocollo extends StampeServlet
{
  /**
   * 
   */
  private static final long serialVersionUID = 5075041460029589270L;
  private static final Font FONT_SERIF_BOLD_10 = new Font("Serif", Font.BOLD,
      10);

  public StampaServletProtocollo()
  {
    this.templateXML = "it/csi/solmr/etc/anag/stampe/TemplateProtocollo.srt";
    this.nomeFilePdf = "Protocollo.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  public void stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient client, GaaFacadeClient newClient,
      RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    AnagAziendaVO aziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    String idDocumenti[] = request.getParameterValues("idDocumento");
    Vector<DocumentoVO> documenti = client.getDocumenti(idDocumenti);
    stampaProtocolli(documenti, aziendaVO, report);

  }

  private void stampaProtocolli(Vector<DocumentoVO> documenti,
      AnagAziendaVO aziendaVO, TabularSheet report) throws SolmrException,
      CloneNotSupportedException
  {
    int size = documenti.size();

    DefaultTableLens tblDocumento = null;

    // Variabili utili per la clonazione
    String elID = null, elIDCurrent = null;
    ReportElement element = null;
    // Recupero le coordinate della cella in cui mi trovo
    Point elStartCell = report
        .getElementCell(report.getElement("tblDocumento"));
    int elStartRow = (int) Math.round(elStartCell.getY());
    int elStartCol = (int) Math.round(elStartCell.getX());

    for (int i = 0; i < size; i++)
    {
      DocumentoVO documentoVO = (DocumentoVO) documenti.get(i);
      elID = "tblDocumento";
      elIDCurrent = elID + i;
      element = this.reportElementClone(report, elID, elIDCurrent);
      report.addElement(elStartRow, elStartCol, element);
      tblDocumento = new DefaultTableLens(report.getTable(elIDCurrent));

      tblDocumento.setColWidth(0, 100);
      tblDocumento.setColWidth(1, 340);

      tblDocumento.setFont(0, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(1, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(3, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(4, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(6, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(7, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(9, 0, FONT_SERIF_BOLD_10);
      tblDocumento.setFont(10, 0, FONT_SERIF_BOLD_10);

      tblDocumento.setObject(0, 1, checkNull(aziendaVO.getCUAA()));
      tblDocumento.setObject(1, 1, checkNull(aziendaVO.getDenominazione()));

      TipoDocumentoVO tipoDocumentoVO = documentoVO.getTipoDocumentoVO();

      if (tipoDocumentoVO != null
          && tipoDocumentoVO.getTipoTipologiaDocumento() != null)
        tblDocumento.setObject(3, 1, checkNull(tipoDocumentoVO
            .getTipoTipologiaDocumento().getDescription()));
      if (tipoDocumentoVO != null)
        tblDocumento.setObject(4, 1,
            checkNull(tipoDocumentoVO.getDescrizione()));
      tblDocumento.setObject(6, 1, DateUtils.formatDate(documentoVO
          .getDataInizioValidita()));
      if (documentoVO.getDataFineValidita() != null)
        tblDocumento.setObject(7, 1, DateUtils.formatDate(documentoVO
            .getDataFineValidita()));
      if (documentoVO.getNumeroProtocollo() != null)
        tblDocumento.setObject(9, 1, StringUtils
            .parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
      if (documentoVO.getDataProtocollo() != null)
        tblDocumento.setObject(10, 1, DateUtils.formatDate(documentoVO
            .getDataProtocollo()));

      setNoBorder(tblDocumento);
      report.setElement(elIDCurrent, tblDocumento);

      elID = "PageBreak1";
      elIDCurrent = elID + i;
      element = this.reportElementClone(report, elID, elIDCurrent);
      report.addElement(elStartRow, elStartCol, element);

    }

    report.removeElement("tblDocumento");
    report.removeElement("PageBreak1");

  }

}
