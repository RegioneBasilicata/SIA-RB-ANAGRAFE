package it.csi.smranag.smrgaa.presentation.pdf;

import inetsoft.report.PainterElement;
import inetsoft.report.Size;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

public class PdfFromImg extends PdfSemplice
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -23619260435286635L;
  
  
  public PdfFromImg()
  {
    this.templateXML = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/AllStmRichAz.srt";
    this.nomeFilePdf = "Img.pdf";
    this.orientamentoStampa = StyleConstants.PAPER_A4;
  }

  public void stampaPdf(HttpServletRequest request, TabularSheet report,
      AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza, Long idAllegato)
      throws SolmrException, Exception
  {    
    AllegatoDocumentoVO allegatoDocumentoVO = gaaFacadeClient.getFileAllegato(idAllegato);
    caricaImg(allegatoDocumentoVO, report);

  }

  private void caricaImg(AllegatoDocumentoVO allegatoDocumentoVO,
      TabularSheet report) throws SolmrException
  {
    final int dpi = 170;
    final double hSt = 8.05;
    final double wSt = 11;
    
    byte[] b = allegatoDocumentoVO.getFileAllegato();
    ImageIcon img = new ImageIcon(b);
    
    int h = img.getIconHeight();
    double hDb = h/dpi;           
    int w = img.getIconWidth();
    double wDb = w/dpi;
    if(hDb > hSt)
    {
      hDb = hSt;
    }
    if(wDb > wSt)
    {
      wDb = wSt;
    }
    
    
    PainterElement imgElem = (PainterElement) report.getElement("imgDoc");
    imgElem.setSize(new Size(hDb, wDb));            
    report.setElement("imgDoc", img.getImage());      
      
    

  }

}
