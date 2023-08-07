package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentiElencoRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/DocumentiElencoRichAz.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTI_ELENCO_RICH_AZ";

  public DocumentiElencoRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    Long idAziendaNuova = (Long)request.getSession().getAttribute("idAziendaNuova");
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    Vector<RichiestaAziendaDocumentoVO> vAllegatiAziendaNuova = 
        gaaFacadeClient.getAllegatiAziendaNuovaIscrizione(idAziendaNuova.longValue(),aziendaNuovaVO.getIdTipoRichiesta());
    
    int size = 0;
    if (vAllegatiAziendaNuova != null)
      size = vAllegatiAziendaNuova.size();

    if (size > 0)
    {
      subReport.removeElement("nlDocumenti1");
      subReport.removeElement("txtNoDocumenti");
      
      DefaultTableLens tblDocumenti = null;
      
      // Recupero le coordinate della cella in cui mi trovo
      tblDocumenti = new DefaultTableLens(subReport.getTable("tblDocumenti"));

      tblDocumenti.setColWidth(0, 170);
      tblDocumenti.setColWidth(1, 120);
      tblDocumenti.setColWidth(2, 100);
      tblDocumenti.setColWidth(3, 60);
      tblDocumenti.setColWidth(4, 60);
      tblDocumenti.setColWidth(5, 50);

      tblDocumenti.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 3, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 4, StyleConstants.H_CENTER);
      tblDocumenti.setAlignment(0, 5, StyleConstants.H_CENTER);

      tblDocumenti.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblDocumenti.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);


      tblDocumenti.setColAlignment(0, StyleConstants.H_LEFT);
      tblDocumenti.setColAlignment(1, StyleConstants.H_LEFT);
      tblDocumenti.setColAlignment(2, StyleConstants.H_LEFT);
      tblDocumenti.setColAlignment(3, StyleConstants.H_CENTER);
      tblDocumenti.setColAlignment(4, StyleConstants.H_CENTER);
      tblDocumenti.setColAlignment(5, StyleConstants.H_CENTER);

      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < size; i++)
      {
        tblDocumenti.addRow();
        RichiestaAziendaDocumentoVO documentoVO = vAllegatiAziendaNuova.get(i);

        tblDocumenti.setObject(i + 1, 0, StampeGaaServlet.checkNull(documentoVO
            .getDescDocumento()));
        tblDocumenti.setObject(i + 1, 1, StampeGaaServlet.checkNull(documentoVO
            .getNumeroDocumento()));
        tblDocumenti.setObject(i + 1, 2, StampeGaaServlet.checkNull(documentoVO
            .getEnteRilascioDocumento()));
        tblDocumenti.setObject(i + 1, 3, StampeGaaServlet.checkNull(StampeGaaServlet
            .checkNull(DateUtils.formatDateNotNull(documentoVO.getDataInizioValidita()))));
        tblDocumenti.setObject(i + 1, 4, StampeGaaServlet.checkNull(StampeGaaServlet
            .checkNull(DateUtils.formatDateNotNull(documentoVO.getDataFineValidita()))));        
        if(documentoVO.getvAllegatoDocumento() != null)
           tblDocumenti.setObject(i + 1, 5, "Si");
        
        

      }
      
      subReport.setElement("tblDocumenti", tblDocumenti);
      
      if("S".equalsIgnoreCase(aziendaNuovaVO.getFlagDichiarazioneAllegati()))
      {
        String txtFlagDichiarazioneAllegati = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_CK_ALL_OBB_NAP);
        subReport.setElement("txtFlagDichiarazioneAllegati", StampeGaaServlet.checkNull(txtFlagDichiarazioneAllegati));
      }
      else
      {
        subReport.removeElement("txtFlagDichiarazioneAllegati");
        subReport.removeElement("nlFlag1");
        subReport.removeElement("nlFlag2");
      }
      

    }
    else
    {
      subReport.removeElement("tblDocumenti");
      subReport.removeElement("txtFlagDichiarazioneAllegati");
      subReport.removeElement("nlFlag1");
      subReport.removeElement("nlFlag2");
    }
    
    
    
  }
}