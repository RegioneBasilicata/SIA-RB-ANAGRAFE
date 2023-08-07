package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.exception.SolmrException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ConsistenzaZootecnicaRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/ConsistenzaZootecnicaRichAz.srt";
  private final static String CODICE_SUB_REPORT = "CONSISTENZA_ZOOTECNICA_RICH_AZ";

  public ConsistenzaZootecnicaRichAz() throws IOException, SolmrException
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
    
    
    
    Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova = gaaFacadeClient
       .getAllevamentiAziendaNuovaIscrizione(idAziendaNuova.longValue());

    if ((vAllevamentiAziendaNuova != null) && (vAllevamentiAziendaNuova.size() > 0))
    {
      // Rimuovo il testo indicante l'assenza di consisetnza associata all'azienda
      subReport.removeElement("txtNessunaConsistenza");

      DefaultTableLens tblConsZoo = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblConsZoo = new DefaultTableLens(subReport.getTable("tblConsZootecnica"));

      tblConsZoo.setColWidth(0, 130);
      tblConsZoo.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 0, StyleConstants.H_CENTER);
      tblConsZoo.setColWidth(1, 70);
      tblConsZoo.setFont(0, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 1, StyleConstants.H_CENTER);
      tblConsZoo.setColWidth(2, 130);
      tblConsZoo.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 2, StyleConstants.H_CENTER);
      tblConsZoo.setColWidth(3, 70);
      tblConsZoo.setFont(0, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 3, StyleConstants.H_CENTER);
      tblConsZoo.setColWidth(4, 70);
      tblConsZoo.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 4, StyleConstants.H_CENTER);
      tblConsZoo.setColWidth(5, 70);
      tblConsZoo.setFont(0, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblConsZoo.setAlignment(0, 5, StyleConstants.H_CENTER);


      // this.setBorderRiquadri(tblTemp);
      for (int i = 0; i < vAllevamentiAziendaNuova.size(); i++)
      {
        tblConsZoo.addRow();
        AllevamentoAziendaNuovaVO all = vAllevamentiAziendaNuova.get(i);
        String denominazioneUte = "";
        if(Validator.isNotEmpty(all.getDenominazUte()))
          denominazioneUte += all.getDenominazUte()+" ";
        denominazioneUte += all.getDescUte();
        tblConsZoo.setObject(i + 1, 0, StampeGaaServlet.checkNull(denominazioneUte));
        tblConsZoo.setObject(i + 1, 1, StampeGaaServlet.checkNull(all.getCodiceAziendaZootecnica()));
        tblConsZoo.setObject(i + 1, 2, StampeGaaServlet.checkNull(all.getDescSpecie()));
        tblConsZoo.setObject(i + 1, 3, StampeGaaServlet.checkNull(all.getDescCategoria()));
        tblConsZoo.setObject(i + 1, 4, StampeGaaServlet.checkNull(all.getStrNumeroCapi()));
        tblConsZoo.setAlignment(i + 1, 4, StyleConstants.H_CENTER);
        tblConsZoo.setObject(i + 1, 5, StampeGaaServlet.checkNull(all.getUnitaMisura()));
      }
      
      subReport.setElement("tblConsZootecnica", tblConsZoo);
    }
    else
    {
      subReport.removeElement("tblConsZootecnica");
    }
    
    
    
  }
  
  
}