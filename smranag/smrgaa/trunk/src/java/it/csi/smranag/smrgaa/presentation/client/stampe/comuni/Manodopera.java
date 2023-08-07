package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DettaglioManodoperaVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Manodopera extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/Manodopera.srt";
  private final static String CODICE_SUB_REPORT = "MANODOPERA";

  public Manodopera() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    
    DefaultTableLens tblTemp = null;

    Long idManodopera = anagFacadeClient.getIdManodoperaQuadroF(
        anagAziendaVO.getIdAzienda(),
        dataInserimentoDichiarazione);

    if (idManodopera != null)
    {
      subReport.removeElement("txtNessunaManodopera");

      ManodoperaVO manodoperaVO = anagFacadeClient.dettaglioManodopera(idManodopera);

      Vector<DettaglioManodoperaVO> vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();

      if (vDettaglioManodopera != null && vDettaglioManodopera.size() > 0)
      {
        long[] rigaFamTempoPieno = null;
        long[] rigaSalFisTempoPieno = null;
        long[] rigaFamTempoParziale = null;
        long[] rigaSalFisTempoParziale = null;
        long[] rigaTotaliColonneTempoPieno = null;
        long[] rigaTotaliColonneTempoParziale = null;

        long[] rigaAvventizi = null;

        Iterator<DettaglioManodoperaVO> iteraDettaglioManodopera = vDettaglioManodopera.iterator();
        DettaglioManodoperaVO dettaglioManodoperaVO = null;

        while (iteraDettaglioManodopera.hasNext())
        {
          dettaglioManodoperaVO = (DettaglioManodoperaVO) iteraDettaglioManodopera
              .next();

          SolmrLogger.debug(this,
              "dettaglioManodoperaVO.getCodTipoClasseManodopera(): "
                  + dettaglioManodoperaVO.getCodTipoClasseManodopera());

          // Familiari a tempo pieno
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(
                  SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO")
                      .toString()))
          {
            rigaFamTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Salariati fissi a tempo pieno
          if (dettaglioManodoperaVO
              .getCodTipoClasseManodopera()
              .toString()
              .equals(
                  SolmrConstants.get(
                      "CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO").toString()))
          {
            rigaSalFisTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Familiari a tempo parziale
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(
                  SolmrConstants
                      .get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE")
                      .toString()))
          {
            rigaFamTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Salariati fissi a tempo parziale
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(
                  SolmrConstants.get(
                      "CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE")
                      .toString()))
          {
            rigaSalFisTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Salariati avventizi
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(
                  SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI")
                      .toString()))
          {
            rigaAvventizi = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }
        }

        // Somma familiari e salariati fissi a tempo pieno
        rigaTotaliColonneTempoPieno = impostaTotaliColonne(rigaFamTempoPieno,
            rigaSalFisTempoPieno);

        // Somma familiari e salariati fissi a tempo parziale
        rigaTotaliColonneTempoParziale = impostaTotaliColonne(
            rigaFamTempoParziale, rigaSalFisTempoParziale);

        subReport.setElement("txtINPS", StampeGaaServlet.checkNull(manodoperaVO.getCodiceInps()));

        tblTemp = new DefaultTableLens(subReport.getTable("tblManodoperaTitolo"));
        impostaColonneManodopera(tblTemp);
        StampeGaaServlet.setNoBorder(tblTemp);
        subReport.setElement("tblManodoperaTitolo", tblTemp);

        tblTemp = new DefaultTableLens(subReport
            .getTable("tblManodoperaFamiliari"));
        rimuoviBordiManodopera(tblTemp);
        impostaColonneManodopera(tblTemp);
        tblTemp.setObject(0, 1, StampeGaaServlet.convertiLong(rigaFamTempoPieno[0]));
        tblTemp.setObject(0, 3, StampeGaaServlet.convertiLong(rigaFamTempoPieno[1]));
        tblTemp.setObject(0, 5, StampeGaaServlet.convertiLong(rigaFamTempoPieno[2]));
        tblTemp.setObject(0, 9, StampeGaaServlet.convertiLong(rigaFamTempoParziale[0]));
        tblTemp.setObject(0, 11, StampeGaaServlet.convertiLong(rigaFamTempoParziale[1]));
        tblTemp.setObject(0, 13, StampeGaaServlet.convertiLong(rigaFamTempoParziale[2]));
        subReport.setElement("tblManodoperaFamiliari", tblTemp);

        tblTemp = new DefaultTableLens(subReport
            .getTable("tblManodoperaSalariati"));
        rimuoviBordiManodopera(tblTemp);
        impostaColonneManodopera(tblTemp);
        tblTemp.setObject(0, 1, StampeGaaServlet.convertiLong(rigaSalFisTempoPieno[0]));
        tblTemp.setObject(0, 3, StampeGaaServlet.convertiLong(rigaSalFisTempoPieno[1]));
        tblTemp.setObject(0, 5, StampeGaaServlet.convertiLong(rigaSalFisTempoPieno[2]));
        tblTemp.setObject(0, 9, StampeGaaServlet.convertiLong(rigaSalFisTempoParziale[0]));
        tblTemp.setObject(0, 11, StampeGaaServlet.convertiLong(rigaSalFisTempoParziale[1]));
        tblTemp.setObject(0, 13, StampeGaaServlet.convertiLong(rigaSalFisTempoParziale[2]));
        subReport.setElement("tblManodoperaSalariati", tblTemp);

        tblTemp = new DefaultTableLens(subReport.getTable("tblManodoperaTotale"));
        rimuoviBordiManodopera(tblTemp);
        impostaColonneManodopera(tblTemp);
        tblTemp.setObject(0, 1, StampeGaaServlet.convertiLong(rigaTotaliColonneTempoPieno[0]));
        tblTemp.setObject(0, 3, StampeGaaServlet.convertiLong(rigaTotaliColonneTempoPieno[1]));
        tblTemp.setObject(0, 5, StampeGaaServlet.convertiLong(rigaTotaliColonneTempoPieno[2]));
        tblTemp
            .setObject(0, 9, StampeGaaServlet.convertiLong(rigaTotaliColonneTempoParziale[0]));
        tblTemp.setObject(0, 11,
            StampeGaaServlet.convertiLong(rigaTotaliColonneTempoParziale[1]));
        tblTemp.setObject(0, 13,
            StampeGaaServlet.convertiLong(rigaTotaliColonneTempoParziale[2]));
        subReport.setElement("tblManodoperaTotale", tblTemp);

        tblTemp = new DefaultTableLens(subReport
            .getTable("tblManodoperaSalariatiAvventizi"));
        rimuoviBordiManodoperaUltimaRiga(tblTemp);
        impostaColonneManodopera(tblTemp);
        tblTemp.setObject(0, 1, StampeGaaServlet.convertiLong(rigaAvventizi[0]));
        tblTemp.setObject(0, 3, StampeGaaServlet.convertiLong(rigaAvventizi[1]));
        tblTemp.setObject(0, 5, StampeGaaServlet.convertiLong(rigaAvventizi[2]));
        if (dettaglioManodoperaVO.getGiornateAnnue() == null
            || "0".equals(dettaglioManodoperaVO.getGiornateAnnue()))
          tblTemp.setObject(0, 9, "");
        else
          tblTemp.setObject(0, 9, dettaglioManodoperaVO.getGiornateAnnue());

        subReport.setElement("tblManodoperaSalariatiAvventizi", tblTemp);
      }
    }
    else
    {
      subReport.removeElement("txtINPS");
      subReport.removeElement("TextBox73");
      subReport.removeElement("tblManodoperaTitolo");
      subReport.removeElement("tblManodoperaFamiliari");
      subReport.removeElement("tblManodoperaSalariati");
      subReport.removeElement("tblManodoperaTotale");
      subReport.removeElement("tblManodoperaSalariatiAvventizi");
      subReport.removeElement("Newline245");
      subReport.removeElement("Newline246");
      subReport.removeElement("Newline247");
      subReport.removeElement("Newline248");
      subReport.removeElement("Newline249");

    }
    
    
    
  }
  
  
  private long[] impostaRigaDettaglioManodopera(
      DettaglioManodoperaVO dettaglioManodoperaVO)
  {
    long[] somma = new long[3];
    // indice 0: uomini
    // indice 1: donne
    // indice 2: somma uomini + donne

    somma[0] = dettaglioManodoperaVO.getUominiLong().longValue();
    somma[1] = dettaglioManodoperaVO.getDonneLong().longValue();
    somma[2] = dettaglioManodoperaVO.getUominiLong().longValue()
        + dettaglioManodoperaVO.getDonneLong().longValue();
    return somma;
  }
  
  private long[] impostaTotaliColonne(long[] riga1, long[] riga2)
  {
    long[] totaliColonne = new long[3];
    // indice 0: totale uomini
    // indice 1: totale donne
    // indice 2: totale somma uomini + donne

    // inizializzazione a zero dei vettori null
    if (riga1 == null)
      riga1 = totaliColonne;
    if (riga2 == null)
      riga2 = totaliColonne;

    SolmrLogger.debug(this, "riga1.length: " + riga1.length + " riga2.length: "
        + riga2.length);

    // somma colonne delle due righe
    for (int i = 0; i < riga1.length; i++)
      totaliColonne[i] = riga1[i] + riga2[i];

    return totaliColonne;
  }
  
  private void impostaColonneManodopera(DefaultTableLens tblTemp)
  {
    tblTemp.setAlignment(0, 1, StyleConstants.H_CENTER);
    tblTemp.setAlignment(0, 3, StyleConstants.H_CENTER);
    tblTemp.setAlignment(0, 5, StyleConstants.H_CENTER);
    tblTemp.setAlignment(0, 9, StyleConstants.H_CENTER);
    tblTemp.setAlignment(0, 11, StyleConstants.H_CENTER);
    tblTemp.setAlignment(0, 13, StyleConstants.H_CENTER);
    tblTemp.setColWidth(0, 149);
    tblTemp.setColWidth(1, 31);
    tblTemp.setColWidth(2, 7);
    tblTemp.setColWidth(3, 31);
    tblTemp.setColWidth(4, 7);
    tblTemp.setColWidth(5, 31);
    tblTemp.setColWidth(6, 7);
    tblTemp.setColWidth(7, 149);
    tblTemp.setColWidth(8, 7);
    tblTemp.setColWidth(9, 31);
    tblTemp.setColWidth(10, 7);
    tblTemp.setColWidth(11, 31);
    tblTemp.setColWidth(12, 7);
    tblTemp.setColWidth(13, 31);
  }
  
  private void rimuoviBordiManodopera(DefaultTableLens tblTemp)
  {
    tblTemp.setRowBorder(-1, 0, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 0, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 2, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 2, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 4, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 4, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 6, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 6, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 7, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 7, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 8, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 8, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 10, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 10, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(-1, 12, StyleConstants.NO_BORDER);
    tblTemp.setRowBorder(0, 12, StyleConstants.NO_BORDER);

    tblTemp.setColBorder(0, -1, StyleConstants.NO_BORDER);
    tblTemp.setColBorder(0, 6, StyleConstants.NO_BORDER);
    tblTemp.setColBorder(0, 7, StyleConstants.NO_BORDER);
  }
  
  
  private void rimuoviBordiManodoperaUltimaRiga(DefaultTableLens tblTemp)
  {
    rimuoviBordiManodopera(tblTemp);
    tblTemp.setRowBorderColor(-1, 11, Color.white);
    tblTemp.setRowBorderColor(0, 11, Color.white);
    tblTemp.setRowBorderColor(-1, 13, Color.white);
    tblTemp.setRowBorderColor(0, 13, Color.white);

    tblTemp.setColBorderColor(0, 10, Color.white);
    tblTemp.setColBorderColor(0, 11, Color.white);
    tblTemp.setColBorderColor(0, 12, Color.white);
    tblTemp.setColBorderColor(0, 13, Color.white);
  }
}