package it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo;

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.QuadroManodopera;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.SubReportModol;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DettaglioManodoperaVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class Manodopera extends SubReportModol
{
  
  private final static String CODICE_SUB_REPORT = "MANODOPERA";

  public Manodopera() throws IOException, SolmrException
  {
    super(CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      Fascicolo fascicoDigitale, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
   
    QuadroManodopera quadroManodopera = new QuadroManodopera();
    fascicoDigitale.setQuadroManodopera(quadroManodopera);
    quadroManodopera.setVisibility(true);
    
    quadroManodopera.setTitoloManodopera(richiestaTipoReportVO.getQuadro());
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");    
    Long idManodopera = anagFacadeClient.getIdManodoperaQuadroF(anagAziendaVO.getIdAzienda(),
        dataInserimentoDichiarazione);

    if (idManodopera != null)
    {
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

          // Familiari a tempo pieno
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO").toString()))
          {
            rigaFamTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }
          
          // Familiari a tempo parziale
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE").toString()))
          {
            rigaFamTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Salariati fissi a tempo pieno
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO").toString()))
          {
            rigaSalFisTempoPieno = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }         

          // Salariati fissi a tempo parziale
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE").toString()))
          {
            rigaSalFisTempoParziale = impostaRigaDettaglioManodopera(dettaglioManodoperaVO);
          }

          // Salariati avventizi
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().toString()
              .equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI").toString()))
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

        

        quadroManodopera.setFamTempoPieno(new Long(rigaFamTempoPieno[2]).toString());
        quadroManodopera.setFamTempoParziale(new Long(rigaFamTempoParziale[2]).toString());
        
        quadroManodopera.setSalTempoPieno(new Long(rigaSalFisTempoPieno[2]).toString());
        quadroManodopera.setSalTempoParziale(new Long(rigaSalFisTempoParziale[2]).toString());
        
        quadroManodopera.setTotTempoPieno(new Long(rigaTotaliColonneTempoPieno[2]).toString());
        quadroManodopera.setTotTempoParziale(new Long(rigaTotaliColonneTempoParziale[2]).toString());
        
        quadroManodopera.setAvventizi(new Long(rigaAvventizi[2]).toString());
        
        String giornateAnnue = "";
        if (dettaglioManodoperaVO.getGiornateAnnue() == null
            || "0".equals(dettaglioManodoperaVO.getGiornateAnnue()))
          giornateAnnue = "";
        else
          giornateAnnue = dettaglioManodoperaVO.getGiornateAnnue();
        quadroManodopera.setGiornateAnnue(giornateAnnue);
      }
      else
      {
        quadroManodopera.setSezioneVuota(true);
      } 
    }
    else
    {
      quadroManodopera.setSezioneVuota(true);
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
  
  
  
  
}