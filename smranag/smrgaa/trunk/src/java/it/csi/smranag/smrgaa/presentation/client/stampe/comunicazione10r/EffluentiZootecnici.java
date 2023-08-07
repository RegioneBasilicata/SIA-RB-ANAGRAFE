package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class EffluentiZootecnici extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/EffluentiZootecnici.srt";
  private final static String CODICE_SUB_REPORT = "EFFLUENTI_ZOOTECNICI";

  public EffluentiZootecnici() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    Comunicazione10RVO[] comunicazione10RVO = (Comunicazione10RVO[])parametri.get("comunicazione10RVO");
    Date dataDettaglioAllevamenti = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DETT_ALLEVAMENTI);
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    int size = 0;
    Vector<EffluenteVO> effluenti = null;
    Vector<String> note = new Vector<String>();
    
    if (comunicazione10RVO != null && comunicazione10RVO.length!=0)
    {
      long idComunicazione[]=new long[comunicazione10RVO.length];
      
      //Mi ricavo tutti gli di degli effluenti e anche le note
      for (int i=0;i<comunicazione10RVO.length;i++)
      {
        idComunicazione[i]=comunicazione10RVO[i].getIdComunicazione10R();
        if (comunicazione10RVO[i].getNote()!=null)
          note.add("- "+comunicazione10RVO[i].getNote());
      }
      
      effluenti = gaaFacadeClient.getListEffluentiStampa(idComunicazione);
    }
    
    
    if (effluenti != null)
      size = effluenti.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di consistenze zootecniche
      // associate all'azienda
      subReport.removeElement("txtNoEffluenti");
      subReport.removeElement("nlNoEffluenti");

      DefaultTableLens tblEffluenti = new DefaultTableLens(subReport.getTable("tblEffluenti"));

      // Imposto l'header
      int col = 0;
      tblEffluenti.setColWidth(col, 120);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 120);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 70);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col, StyleConstants.H_CENTER);
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 50);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 70);
      tblEffluenti.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(0, col, StyleConstants.H_CENTER);
      if(Validator.isEmpty(dataInserimentoDichiarazione)
        || (Validator.isNotEmpty(dataInserimentoDichiarazione)) && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
      {
        tblEffluenti.setObject(0, col, StampeGaaServlet.checkNull("Post trattamento"));
      }
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblEffluenti.setColWidth(col, 50);
      tblEffluenti.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setAlignment(1, col++, StyleConstants.H_CENTER);

      BigDecimal totAzotoInit = new BigDecimal(0);
      BigDecimal totAzotoPostDich = new BigDecimal(0);
      
      for (int i = 0; i < size; i++)
      {
        EffluenteVO temp = (EffluenteVO) effluenti.get(i);
        col = 0;
        tblEffluenti.addRow();
        tblEffluenti.setObject(i + 2, col++, StampeGaaServlet.checkNull(temp.getTipoEffluente()));
        tblEffluenti.setObject(i + 2, col++, StampeGaaServlet.checkNull(temp.getDescrizione()));
        
        if(Validator.isEmpty(temp.getAzotoPostTrattamentoDec()))
        {
          temp.setAzotoPostTrattamentoDec(new BigDecimal(0));
        }
        totAzotoInit = totAzotoInit.add(temp.getAzotoPostTrattamentoDec());
        totAzotoPostDich = totAzotoPostDich.add(temp.getAzotoPostDichiarato());

        tblEffluenti.setObject(i + 2, col, StringUtils.parseDoubleFieldBigDecimal(temp.getVolumePostTrattamento()));
        tblEffluenti.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        tblEffluenti.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(temp.getAzotoPostTrattamentoDec()));
        tblEffluenti.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        
        
        tblEffluenti.setObject(i + 2, col, StringUtils.parseDoubleFieldBigDecimal(temp.getVolumePostDichiarato())); 
        tblEffluenti.setAlignment(i + 2,col++, StyleConstants.H_CENTER); 
        tblEffluenti.setObject(i + 2, col,Formatter.formatAndRoundBigDecimal0(temp.getAzotoPostDichiarato())); 
        tblEffluenti.setAlignment(i + 2,col++, StyleConstants.H_CENTER);
        
         

      }
      
      //Aggiungo la riga dei totali
      /*tblEffluenti.addRow();
      tblEffluenti.setObject(size + 2, 2, "Totale");
      
      tblEffluenti.setAlignment(size + 2, 2, StyleConstants.H_CENTER);
      tblEffluenti.setObject(size + 2, 3, Formatter.formatAndRoundBigDecimal0(totAzotoInit));
      tblEffluenti.setAlignment(size + 2, 3, StyleConstants.H_CENTER);
      
      
      tblEffluenti.setObject(size + 2, 5, Formatter.formatAndRoundBigDecimal0(totAzotoPostDich));
      tblEffluenti.setAlignment(size + 2, 5, StyleConstants.H_CENTER);
      
      tblEffluenti.setFont(size + 2, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(size + 2, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblEffluenti.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      tblEffluenti.setHeaderRowCount(2);
      setNoBorderTotaliEff(tblEffluenti);*/

      if(Validator.isEmpty(dataInserimentoDichiarazione)
        || (Validator.isNotEmpty(dataInserimentoDichiarazione)) && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
      {
        tblEffluenti.removeColumn(2);
        tblEffluenti.removeColumn(2);
        
        
        //Aggiungo la riga dei totali
        tblEffluenti.addRow();
        tblEffluenti.setObject(size + 2, 2, "Totale");
        
        tblEffluenti.setAlignment(size + 2, 2, StyleConstants.H_CENTER);
        tblEffluenti.setObject(size + 2, 3, Formatter.formatAndRoundBigDecimal0(totAzotoPostDich));
        tblEffluenti.setAlignment(size + 2, 3, StyleConstants.H_CENTER);
        
        tblEffluenti.setFont(size + 2, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblEffluenti.setFont(size + 2, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
        
        tblEffluenti.setHeaderRowCount(2);
        setNoBorderTotaliEffNew(tblEffluenti);
        
      }
      else
      {
        //Aggiungo la riga dei totali
        tblEffluenti.addRow();
        tblEffluenti.setObject(size + 2, 2, "Totale");
        
        tblEffluenti.setAlignment(size + 2, 2, StyleConstants.H_CENTER);
        tblEffluenti.setObject(size + 2, 3, Formatter.formatAndRoundBigDecimal0(totAzotoInit));
        tblEffluenti.setAlignment(size + 2, 3, StyleConstants.H_CENTER);
        
        
        tblEffluenti.setObject(size + 2, 5, Formatter.formatAndRoundBigDecimal0(totAzotoPostDich));
        tblEffluenti.setAlignment(size + 2, 5, StyleConstants.H_CENTER);
        
        tblEffluenti.setFont(size + 2, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblEffluenti.setFont(size + 2, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblEffluenti.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
        
        tblEffluenti.setHeaderRowCount(2);
        setNoBorderTotaliEff(tblEffluenti);
        
      }
      subReport.setElement("tblEffluenti", tblEffluenti);
      
      DefaultTableLens tblNoteEffluenti = new DefaultTableLens(subReport.getTable("tblNoteEffluenti"));
      
      tblNoteEffluenti.setAlignment(0,0,StyleConstants.V_TOP);
      tblNoteEffluenti.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      for (int i=0;i<note.size();i++)
      {
        //Se non sono al primo step devo aggiungere una riga alla tabella
        if (i!=0) tblNoteEffluenti.addRow();
        tblNoteEffluenti.setObject(i, 1, StampeGaaServlet.checkNewLine((String)note.get(i)));
          
      }
      
      StampeGaaServlet.setNoBorder(tblNoteEffluenti);
      
      subReport.setElement("tblNoteEffluenti", tblNoteEffluenti);
      
    }
    else
    {
      // Rimuovo la tabella delle consistenze
      subReport.removeElement("tblEffluenti");
      // Rimuovo le note ed il relativo new line
      subReport.removeElement("tblNoteEffluenti");
      subReport.removeElement("spNoteEffluenti");
      subReport.removeElement("nvlNoteEffluenti");
    }    
    
  }
  
  
  private final void setNoBorderTotaliEff(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 2, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 4, StyleConstants.NO_BORDER);
  }
  
  private final void setNoBorderTotaliEffNew(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 2, StyleConstants.NO_BORDER);
  }
}