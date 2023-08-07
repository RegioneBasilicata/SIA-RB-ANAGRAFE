package it.csi.solmr.servlet.anag;

import inetsoft.report.PDFPrinter;
import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.SectionBand;
import inetsoft.report.SectionElement;
import inetsoft.report.Size;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.io.Builder;
import inetsoft.report.lens.DefaultSectionLens;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;

import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.PrintJob;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

public abstract class StampeServlet extends HttpServlet {
  private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  protected String templateXML = null;
  protected String nomeFilePdf = null;
  protected Size orientamentoStampa = null;
  protected ServletContext context = null;

  private static final String CONTENT_TYPE_PDF = "application/pdf";
  private static final String CONTENT_TYPE_DOWNLOAD = "application/x-download";
  private static final String ERROR_PAGE = "/errorPage.jsp";

  private static final Font FONT_SERIF_BOLD_10 = new Font("Serif",Font.BOLD,10);

  public void init(ServletConfig config) throws ServletException {
     super.init(config);
     context = config.getServletContext();
  }

  public void destroy() {
    templateXML = null;
    nomeFilePdf = null;
    orientamentoStampa = null;
    super.destroy();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    SolmrLogger.error(this, "Generazione PDF passando per doGet()");
     doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    SolmrLogger.debug(this, "Passato da doPost(): " + request.getRequestURI());

    // Controllo validità della sessione
    HttpSession session = request.getSession(false);
    if (! (session != null &&
        session.getAttribute("ruoloUtenza") != null))
      throw new ServletException("StampaServlet - Sessione non valida");

    // Controllo esistenza degli attributi della sottoclasse
    if (null==this.nomeFilePdf)
      throw new ServletException("\nStampaServlet - Tentativo fallito a causa di nomeFilePdf non definito");
    if (null==this.templateXML)
      throw new ServletException("\nStampaServlet - Tentativo fallito a causa di templateXML non definito");
    if (null==this.orientamentoStampa)
      throw new ServletException("\nStampaServlet - Tentativo fallito a causa di orientamentoStampa non definito");

    try {
      String action=request.getParameter("action");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      creaPdf(request, response, baos);
      byte ba[] = baos.toByteArray();
      response.setContentLength(ba.length);
      if ("download".equals(action))
      {
        response.setContentType(CONTENT_TYPE_DOWNLOAD);
        response.addHeader("Content-Disposition",
                           "attachment;filename = " + this.nomeFilePdf);
      }
      else //if ("open".equals(action)) // azione predefinita
        response.setContentType(CONTENT_TYPE_PDF);

      OutputStream out = response.getOutputStream();
      out.write(ba);
      //out.flush();
      //out.close();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      SolmrLogger.error(this, "\nStampeServlet.doPost()\n"+e.getMessage()+"\n");
      // Imposto l'oggetto exception standard delle JSP
      request.setAttribute("javax.servlet.jsp.jspException", e);
      // Imposto l'oggetto exception richiesto nello scriptlet di errorPage.jsp
      request.getSession().setAttribute("exception", e);
      request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
    }
  }

  protected final Image getImage(String image)  {
    if (image == null) {
      SolmrLogger.error(this, "Errore: Immagine non specificata");
      return null;
    }

    final byte[][] buffer = new byte[1][];
    try {
      InputStream resource = getStream(image);
      if (resource == null)
      {
        SolmrLogger.error(this, "Errore: Risorsa "+image+" non trovata");
        return null;
      }

      BufferedInputStream in = new BufferedInputStream(resource);
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      buffer[0] = new byte[1024];
      int n;
      while ((n = in.read(buffer[0])) > 0)
        out.write(buffer[0], 0, n);
      in.close();
      out.flush();
      buffer[0] = out.toByteArray();
    }
    catch (IOException ioe) {
      SolmrLogger.error(this, ioe.toString());
      return null;
    }

    if (buffer[0] == null) {
      SolmrLogger.error(this, "StampeServlet.getImage('"+image+"') - errore: immagine non trovata");
      return null;
    }
    if (buffer[0].length == 0) {
      SolmrLogger.error(this, "StampeServlet.getImage('"+image+"') - errore: immagine di lunghezza zero");
      return null;
    }

    return new ImageIcon(buffer[0]).getImage();
  }

  protected final InputStream getStream(String resource)  {
    SolmrLogger.debug(this, "URL risorsa richiesta: "+getClass().getClassLoader().getResource(resource).toString());
    return getClass().getClassLoader().getResourceAsStream(resource);
  }

  protected void creaPdf(HttpServletRequest request,
                             HttpServletResponse response,
                             OutputStream outStampa) throws Exception  
  {
    try 
    {
      InputStream fis = getStream(this.templateXML);
      TabularSheet report = (TabularSheet)Builder.getBuilder(Builder.TEMPLATE, fis).read("./");

      AnagFacadeClient client = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession().getAttribute("ruoloUtenza");

      stampaPdf(request, report, client, gaaFacadeClient, ruoloUtenza);

      PDFPrinter pdf = new PDFPrinter(outStampa);
      //pdf.setPageSize(StyleConstants.PAPER_A4);
      pdf.setPageSize(orientamentoStampa);
      PrintJob pj = pdf.getPrintJob();
      report.print(pj);
      pdf.close();
    }
    catch (Exception e) 
    {
      e.printStackTrace();
      throw e;
    }
  }

  protected final ReportElement reportElementClone(ReportSheet report,
                                                   String elID,
                                                   String elIDClone)
      throws CloneNotSupportedException
  {
    ReportElement element = (ReportElement)report.getElement(elID).clone();
    element.setID(elIDClone);
    return element;
  }

  protected final void setBorderRiquadri(DefaultTableLens tblLens)
  {
    int iMax=tblLens.getRowCount();
    int jMax=tblLens.getColCount();
    for (int i=-1; i<iMax; i++)
      for (int j=1; j<jMax; j+=2)
        tblLens.setRowBorder(i, j, StyleConstants.NO_BORDER);

    for (int j=-1; j<jMax; j++)
    {
      tblLens.setRowBorder(-1, j, StyleConstants.NO_BORDER);
      tblLens.setColBorder(0, j, StyleConstants.NO_BORDER);
    }

    for (int j=0; j<jMax; j+=2)
      tblLens.setFont(0,j,FONT_SERIF_BOLD_10);
  }

  //versione 5.2 style report
  /*protected final void setNoBorder(DefaultTableLens tblLens)
  {
    tblLens.setColBorder(StyleConstants.NO_BORDER);
    tblLens.setRowBorder(StyleConstants.NO_BORDER);
    int iMax=tblLens.getRowCount();
    int jMax=tblLens.getColCount();
    for (int i=0; i<iMax; i++)
      for (int j=0; j<jMax; j++) {
          tblLens.setColBorder(i, j, StyleConstants.NO_BORDER);
          tblLens.setRowBorder(i, j, StyleConstants.NO_BORDER);
      }
  }*/
  
  
  public static void setNoBorder(DefaultTableLens tblLens)
  {
    tblLens.setColBorder(StyleConstants.NO_BORDER);
    tblLens.setRowBorder(StyleConstants.NO_BORDER);
    int iMax = tblLens.getRowCount();
    int jMax = tblLens.getColCount();
    for (int i = -1; i < iMax; i++)
    {
      for (int j = -1; j < jMax; j++)
      {
        tblLens.setColBorder(i, j, StyleConstants.NO_BORDER);
        tblLens.setRowBorder(i, j, StyleConstants.NO_BORDER);
      }
    }
  }

  protected final void setNoBorderWithTitle(DefaultTableLens tblLens) {
    tblLens.setColBorder(StyleConstants.NO_BORDER);
    tblLens.setRowBorder(StyleConstants.NO_BORDER);
    int iMax=tblLens.getRowCount();
    int jMax=tblLens.getColCount();
    for (int i=0; i<iMax; i++)
      for (int j=0; j<jMax; j++) {
        tblLens.setColBorder(i, j, StyleConstants.NO_BORDER);
        if (i!=0) {
          tblLens.setRowBorder(i, j, StyleConstants.NO_BORDER);
        }
      }
  }

  protected final void setFooter(TabularSheet report, RuoloUtenza ruoloUtenza, String siglaProvincia, String dittaUma )
  {
    //-------------------------------------------- Creazione Footer
    long id = ruoloUtenza.getIdUtente().longValue() + DateUtils.getCurrentDay().intValue();
    report.setCurrentFooter(TabularSheet.DEFAULT_FOOTER);
    report.setFooterFromEdge(0.25);
    report.setCurrentFont(new Font("SansSerif", Font.ITALIC, 7));
    report.setCurrentAlignment(StyleConstants.H_LEFT|StyleConstants.V_BOTTOM);
    report.addFooterText("Pagina {P} di {N}");
    report.setCurrentAlignment(StyleConstants.H_RIGHT|StyleConstants.V_BOTTOM);
    report.addFooterText((siglaProvincia==null ? "" : siglaProvincia + " ") + (dittaUma==null ? "" : dittaUma)
                         + ((siglaProvincia!=null || dittaUma!=null)?"/":"") + id + " " + formatter.format(new Date()));
  }

  protected final void setFooterDate(TabularSheet report, RuoloUtenza ruoloUtenza, String siglaProvincia, String dittaUma, String strDataUser)
  {
    //-------------------------------------------- Creazione Footer
    long id = ruoloUtenza.getIdUtente().longValue() + DateUtils.getCurrentDay().intValue();
    report.setCurrentFooter(TabularSheet.DEFAULT_FOOTER);
    report.setFooterFromEdge(0.25);
    report.setCurrentFont(new Font("SansSerif", Font.ITALIC, 7));
    report.setCurrentAlignment(StyleConstants.H_LEFT|StyleConstants.V_BOTTOM);
    report.addFooterText("Pagina {P} di {N}");
    report.setCurrentAlignment(StyleConstants.H_RIGHT|StyleConstants.V_BOTTOM);
    report.addFooterText((siglaProvincia==null ? "" : siglaProvincia + " ") + (dittaUma==null ? "" : dittaUma)
                         + ((siglaProvincia!=null || dittaUma!=null)?"/":"") + id + " " + strDataUser);
  }

  protected abstract void stampaPdf(HttpServletRequest request,
                                    TabularSheet xss,
                                    AnagFacadeClient client,
                                    GaaFacadeClient newClient,
                                    RuoloUtenza ruoloUtenza) throws Exception;

  public static String checkNull(String stringa) {
    if(stringa==null)
      stringa="";
    stringa = StringUtils.replace(stringa," ", (char)160 + " ");
    return stringa;
  }
  
  public static String checkNewLine(String stringa) 
  {
    stringa=checkNull(stringa);
    stringa = StringUtils.replace(stringa,"\n", (char)160 + " ");
    return stringa;
  }

  public static String checkNull(Object obj) {
    if(obj ==null)
      return new String("");
    else
      return StringUtils.replace(obj.toString(), " ", (char)160 + " ");
  }
  
  
  protected static final void removeRows(TabularSheet report, String nameElement, int numRowsDeleted)
  {
    Point elStartCell = report.getElementCell(report.getElement(nameElement));
    int rowPosition = (int)Math.round(elStartCell.getY());    
    report.removeRows(rowPosition, numRowsDeleted);//numero di righe a partire dal fondo delle quali viene effettuata la cancellazione
  }
  
  public static int[] getRowColElement(TabularSheet report, String nameElement)
  {
    int rowCol[] = new int[2];

    try
    {
      Point elStartCell = report.getElementCell(report.getElement(nameElement));
      rowCol[0] = (int) Math.round(elStartCell.getY());
      rowCol[1] = (int) Math.round(elStartCell.getX());
      
      return rowCol;
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  
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
