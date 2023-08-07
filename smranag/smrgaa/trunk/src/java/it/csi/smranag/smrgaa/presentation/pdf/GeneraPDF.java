package it.csi.smranag.smrgaa.presentation.pdf;

import inetsoft.report.PDFPrinter;
import inetsoft.report.ReportSheet;
import inetsoft.report.SectionBand;
import inetsoft.report.SectionElement;
import inetsoft.report.Size;
import inetsoft.report.TabularSheet;
import inetsoft.report.io.Builder;
import inetsoft.report.lens.DefaultSectionLens;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;

import java.awt.PrintJob;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class GeneraPDF
{
  
  protected String templateXML = null;
  protected String nomeFilePdf = null;
  protected Size orientamentoStampa = null;
  
  public void generaDocumento(HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    creaPdf(request, response, baos);
  }
  
  
  protected abstract void salvaPdf(HttpServletRequest request,
      ByteArrayOutputStream baos,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient) throws Exception;
  
  protected void creaPdf(HttpServletRequest request,
      HttpServletResponse response,
      OutputStream outStampa) throws Exception  
  {
    try 
    {
      InputStream fis = getStream(this.templateXML);
      TabularSheet report = (TabularSheet)Builder.getBuilder(Builder.TEMPLATE, fis).read("./");
      
      AnagFacadeClient anagFacadeclient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient= GaaFacadeClient.getInstance();
      
      HashMap<String, Object> hmParametri = stampaPdf(request, report, anagFacadeclient, gaaFacadeClient);
      
      generaOutputStampa(request, response, report, outStampa, gaaFacadeClient, hmParametri);
      
      salvaPdf(request, (ByteArrayOutputStream)outStampa, anagFacadeclient, gaaFacadeClient);
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      throw e;
    }
  }
  
  protected void generaOutputStampa(HttpServletRequest request,
      HttpServletResponse response, TabularSheet report,
      OutputStream outStampa, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> hmParametri) throws Exception
  {
    PDFPrinter pdf = new PDFPrinter(outStampa);
    pdf.setPageSize(orientamentoStampa);
    PrintJob pj = pdf.getPrintJob();
    report.print(pj);
    pdf.close();

  }
  
  protected final InputStream getStream(String resource)  {
    SolmrLogger.debug(this, "URL risorsa richiesta: "+getClass().getClassLoader().getResource(resource).toString());
    return getClass().getClassLoader().getResourceAsStream(resource);
  }
  
  protected abstract HashMap<String, Object> stampaPdf(HttpServletRequest request,
      TabularSheet xss,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient) throws Exception;
  
  
  public static Subreport addSubReportToMainReport(TabularSheet report,
      ReportSheet subreport, int[] rowCol, boolean newPageBefore)
  {
    return addSubReportToMainReport(report, subreport, rowCol, newPageBefore,
        null);
  }
  
  
  
  public static Subreport addSubReportToMainReport(TabularSheet report,
      ReportSheet subreport, int[] rowCol, boolean newPageBefore,
      Boolean breakable)
  {
    // Background del report contenente il subreport
    subreport.setBackground(report.getBackground());

    // Se non specificato la sezione contenente il subreport viene collocata
    // nella prima riga
    if (rowCol == null)
    {
      rowCol = new int[2];
      rowCol[0] = 0;
      rowCol[1] = 0;
    }

    // Inserimento nuova riga in cui viene posizionata la sezione contenente il
    // subreport
    report.insertRows(rowCol[0], 1);
    if (newPageBefore)
    {
      report.addPageBreak(rowCol[0], 0);
    }

    int prevRow = rowCol[0] - 1;
    Integer subReportOrientation = null;
    subReportOrientation = subreport.getOrientation();
    Integer reportOrientation = null;
    if (prevRow <= 0)
    {
      reportOrientation = report.getOrientation();
    }
    else
    {
      reportOrientation = report.getRowOrientation(prevRow);
      if (reportOrientation == null)
      {
        reportOrientation = report.getOrientation();
      }
    }
    if (!StringUtils.checkNull(reportOrientation).equals(
        StringUtils.checkNull(subReportOrientation)))
    {
      report.setRowOrientation(rowCol[0], subReportOrientation);
    }
    //    

    // Nuova sezione, in cui verrà creato il subreport
    SectionBand header = new SectionBand(report);
    SectionBand content = new SectionBand(report);
    SectionBand footer = new SectionBand(report);
    DefaultSectionLens sectionLens = new DefaultSectionLens(header, content,
        footer);
    String idSectionElement = report.addSection(rowCol[0], rowCol[1],
        sectionLens);
    SectionElement sectionElement = (SectionElement) report
        .getElement(idSectionElement);
    sectionElement.setFullName(idSectionElement);

    // Header della sezione
    SectionBand[] sectionBand = sectionElement.getSection().getSectionHeader();
    sectionBand[0].setVisible(false);

    // Content della sezione (deve essere visible altrimenti il metodo
    // setPageBefore() applicato al footer non funziona)
    sectionBand = (SectionBand[]) sectionElement.getSection()
        .getSectionContent();
    sectionBand[0].setHeight(0);
    sectionBand[0].setVisible(true);
    String idSubreport = sectionBand[0].addSubreport(subreport,
        new String[][] {}, new java.awt.Rectangle(1000, 10));
    if (breakable != null)
    {
      sectionBand[0].setBreakable(breakable.booleanValue());
      sectionBand[0].setShrinkToFit(true);
    }
    // Footer della sezione (il metodo setPageBefore() funziona solo sul footer)
    sectionBand = sectionElement.getSection().getSectionFooter();
    sectionBand[0].setHeight(0);
    sectionBand[0].setVisible(true);

    // Salto pagina prima

    return new Subreport(sectionElement, idSectionElement, subreport,
        idSubreport, rowCol);
  }
  
  
  public String checkNull(String stringa) {
    if(stringa==null)
      stringa="";
    stringa = StringUtils.replace(stringa," ", (char)160 + " ");
    return stringa;
  }
  
  
  
  /**
   * @author luca.diana
   *
   */
  static class Subreport
  {
    private SectionElement sectionElement;
    private String         idSectionElement;
    private ReportSheet    subreport;
    private String         idSubreport;
    private int[]          rowCol;

    public Subreport(SectionElement sectionElement, String idSectionElement,
        ReportSheet subreport, String idSubreport, int[] rowCol)
    {
      this.sectionElement = sectionElement;
      this.idSectionElement = idSectionElement;
      this.subreport = subreport;
      this.idSubreport = idSubreport;
      this.rowCol = rowCol;
    }

    public SectionElement getSectionElement()
    {
      return sectionElement;
    }

    public String getIdSectionElement()
    {
      return idSectionElement;
    }

    public ReportSheet getSubreport()
    {
      return subreport;
    }

    public String getIdSubreport()
    {
      return idSubreport;
    }

    public int[] getRowCol()
    {
      return rowCol;
    }

    public int[] getRowColBefore()
    {
      rowCol[0] = (rowCol[0] == 0) ? 0 : rowCol[0] - 1;
      return rowCol;
    }

    public int[] getRowColAfter()
    {
      rowCol[0] = rowCol[0] + 1;
      return rowCol;
    }
  }
  
  
}
