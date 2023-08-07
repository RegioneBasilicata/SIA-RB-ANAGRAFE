package it.csi.smranag.smrgaa.presentation.firma;

import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.presentation.excel.ExcelServlet;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;



/**
 * Classe generica per la generazione dei file excel
 * <p>Title: Smrgaa</p>
 * <p>Description: Classe di utilità per la generazione di file excel</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */
public class DownloadFileFirmaServlet extends it.csi.smranag.smrgaa.presentation.excel.ExcelServlet
{

 
  
  


  /**
   * 
   */
  private static final long serialVersionUID = -5892158174529073863L;
  
  


  /**
   * Inizializza il servlet
   *
   * @throws ServletException
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request, response);
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request, response);
  }

  public void destroy() {
    super.destroy();
  }

  /**
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      SolmrLogger.debug(this, " - DownloadFileFirmaServlet - INIZIO PAGINA ");

      byte[] ba = (byte[]) request.getSession().getAttribute("fileAllegatoA");
     String nomeFile = (String)request.getSession().getAttribute("fileName");
      
      
//      byte ba[] = FileUtils.readFileToByteArray(new File("D:/Firmami.pdf"));
      response.setContentLength(ba.length);
      response.setContentType("application/x-download");
      response.addHeader("Content-Disposition", "attachment;filename ="+nomeFile);
     

      OutputStream out = response.getOutputStream();
      out.write(ba);
      out.flush();
      out.close();
      
      
      SolmrLogger.debug(this, " - DownloadFileFirmaServlet - FINE PAGINA ");
    }
    catch(Exception ex)
    {
      request.setAttribute("errorReport",ex);
      SolmrLogger.fatal(this, "DownloadFileFirmaServlet Exception  "+ex.toString());
      throw new ServletException(ex.getMessage());
    }
  }





}
