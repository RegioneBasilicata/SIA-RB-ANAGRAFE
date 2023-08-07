package it.csi.smranag.smrgaa.presentation.client.stampe.protocollo;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DocumentoCorrettivaTerreni extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/protocollo/DocumentoCorrettivaTerreni.srt";
  private final static String CODICE_SUB_REPORT = "DOCUMENTO_CORRETTIVA_TERRENI";

  public DocumentoCorrettivaTerreni() throws IOException, SolmrException
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
    
    int size=0;
    Vector<ParticellaAssVO> elencoParticelle = anagFacadeClient
      .getParticelleDocCor(documentoVO.getIdDocumento());
    
    if(elencoParticelle!=null)
    {
      size=elencoParticelle.size();
    }
    
    if (size>0)
    {
      subReport.removeElement("txtNoTerreni");     
      
      DefaultTableLens tblTerreni = new DefaultTableLens(subReport.getTable("tblTerreni"));
      
      for (int i=0;i<11;i++)
      {
        tblTerreni.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(0, i, StyleConstants.H_CENTER);
        tblTerreni.setFont(1, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTerreni.setAlignment(1, i, StyleConstants.H_CENTER);
      }      
        
      // Imposto l'header
      int col = 0;
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 140);//comune
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//sezione
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//foglio
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//particella
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//subalterno
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 40);//superficie catastale
      tblTerreni.setSpan(0, col , new Dimension(2, 1));
      tblTerreni.setObject(0, col, StampeGaaServlet.checkNull("Conduzione"));
      tblTerreni.setColWidth(col++, 25);//codice conduzione
      tblTerreni.setObject(1, col, StampeGaaServlet.checkNull("Sup."));
      tblTerreni.setColWidth(col++, 40);//superficie candotta
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 140);//Uso Primario
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 40);//sup utilizzata     
      tblTerreni.setSpan(0, col , new Dimension(1, 2));
      tblTerreni.setColWidth(col++, 25);//C.P.
    
      for (int i = 0; i < size; i++)
      {
        BigDecimal temp=new BigDecimal(0);
        tblTerreni.addRow();
        ParticellaAssVO particella= (ParticellaAssVO) elencoParticelle.get(i);
        col = 0;
        String descComune = particella.getDescComuneParticella()+" ("
          +particella.getSiglaProvinciaParticella()+")";
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(descComune));
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getSezione()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getFoglio()));
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
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
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(
            particella.getIdConduzione()));
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupCondotta());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(
            particella.getDescUsoPrimario()));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_RIGHT);
        try
        {
          temp = new BigDecimal(particella.getSupUtilizzata());
        }
        catch(Exception e)
        {
          temp=new BigDecimal(0);
        }
        tblTerreni.setObject(i + 2, col++, StringUtils.parseSuperficieFieldBigDecimal(temp));
        
        
        tblTerreni.setAlignment(i + 2, col, StyleConstants.H_CENTER);
        tblTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(particella.getIdCasoParticolare()));
     
      }
      
      subReport.setElement("tblTerreni", tblTerreni);
    }
    else
    {
      subReport.removeElement("tblTerreni");
    }
    
    
    
    
    
    
    
    
    
  }
}