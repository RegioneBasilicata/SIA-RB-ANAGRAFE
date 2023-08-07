package it.csi.solmr.business.anag;

import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.DettaglioAttivitaVO;
import it.csi.solmr.dto.anag.DettaglioManodoperaVO;
import it.csi.solmr.dto.anag.FrmManodoperaVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.dto.anag.TipoIscrizioneINPSVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.ManodoperaDAO;
import it.csi.solmr.util.SolmrLogger;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author Nadia B.
 * @version 1.0
 */

@Stateless(name="comp/env/solmr/anag/Manodopera",mappedName="comp/env/solmr/anag/Manodopera")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class ManodoperaBean implements ManodoperaLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = 6968555580203219691L;
  SessionContext sessionContext;
  ManodoperaDAO manodoperaDAO;
  CommonDAO commonDAO;

  private static String TEMPO_PIENO = "TEMPO_PIENO";
  private static String TEMPO_PARZIALE = "TEMPO_PARZIALE";
  private static String SALAR_AVVENTIZI = "SALAR_AVVENTIZI";

 

  private void initializeDAO() throws EJBException
  {
    try
    {
      manodoperaDAO = new ManodoperaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  /**
   * Tipo Classe Manodopera
   * 
   * @return Vector
   * @throws Exception
   * @throws NotFoundException
   */
  public Vector<CodeDescription> getTipiClassiManodopera()
      throws Exception, NotFoundException
  {
    try
    {
      return commonDAO.getCodeDescriptions(SolmrConstants.get(
          "TAB_TIPO_CLASSI_MANODOPERA").toString(),
          SolmrConstants.CD_DESCRIPTION);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco delle manodopere di
   * un'azienda relativi ad un determinato piano di riferimento
   * 
   * @param manodoperaVO
   * @param idPianoRiferimento
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(
      ManodoperaVO manodoperaVO, Long idPianoRiferimento)
      throws SolmrException, Exception
  {

    Vector<String> vTipoClassiManodopera = null;

    Vector<FrmManodoperaVO> vManodopera = null;
    TreeMap<Long, FrmManodoperaVO> hmManodopera = null;
    FrmManodoperaVO frmManodoperaVO = null;

    try
    {
      hmManodopera = new TreeMap<Long, FrmManodoperaVO>();

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera FAMILIARI a tempo pieno e
      // al tipo classe manodopera SALARIATI FISSI a tempo pieno
      vTipoClassiManodopera = new Vector<String>();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO"));
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, TEMPO_PIENO, idPianoRiferimento);

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera FAMILIARI a tempo parziale e
      // al tipo classe manodopera SALARIATI FISSI a tempo parziale
      vTipoClassiManodopera.clear();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE"));
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, TEMPO_PARZIALE, idPianoRiferimento);

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera SALARIATI avventizi
      vTipoClassiManodopera.clear();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, SALAR_AVVENTIZI, idPianoRiferimento);

      // corretta visualizzazione dei campi numerici,
      // inoltre il metodo deve restituire un Vector
      Collection<FrmManodoperaVO> cManodopera = hmManodopera.values();
      vManodopera = new Vector<FrmManodoperaVO>();
      for (Iterator<FrmManodoperaVO> i = cManodopera.iterator(); i.hasNext();)
      {
        frmManodoperaVO = (FrmManodoperaVO) i.next();

        SolmrLogger.debug(this, "dentro iterator idManodopera: "
            + frmManodoperaVO.getIdManodopera());
        // SolmrLogger.debug(this, "dentro iterator totale ore: " +
        // frmManodoperaVO.getTotaleULU());

        // ULU = ore / 1800
        // frmManodoperaVO.setTotaleULU("" +
        // (frmManodoperaVO.getTotaleULULong().longValue() / 1800));
        // SolmrLogger.debug(this, "dentro iterator totale ULU: " +
        // frmManodoperaVO.getTotaleULU());

        // in caso di null viene visualizzato lo zero
        if (frmManodoperaVO.getNumPersTempoPieno() == null)
          frmManodoperaVO.setNumPersTempoPieno("0");
        if (frmManodoperaVO.getNumPersTempoParz() == null)
          frmManodoperaVO.setNumPersTempoParz("0");
        if (frmManodoperaVO.getNumSalariatiAvventizi() == null)
          frmManodoperaVO.setNumSalariatiAvventizi("0");

        vManodopera.add(frmManodoperaVO);
      }
      SolmrLogger.debug(this, "dopo iterator vManodopera.size(): "
          + vManodopera.size());
    }
    catch (Exception ex)
    {
      throw ex;
    }

    return vManodopera;
  }

  /**
   * Elenco manodopera annua
   * 
   * @param manodoperaVO
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception
  {

    Vector<String> vTipoClassiManodopera = null;

    Vector<FrmManodoperaVO> vManodopera = null;
    TreeMap<Long, FrmManodoperaVO> hmManodopera = null;
    FrmManodoperaVO frmManodoperaVO = null;

    try
    {
      hmManodopera = new TreeMap<Long, FrmManodoperaVO>();

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera FAMILIARI a tempo pieno e
      // al tipo classe manodopera SALARIATI FISSI a tempo pieno
      vTipoClassiManodopera = new Vector<String>();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO"));
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, TEMPO_PIENO, null);

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera FAMILIARI a tempo parziale e
      // al tipo classe manodopera SALARIATI FISSI a tempo parziale
      vTipoClassiManodopera.clear();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE"));
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, TEMPO_PARZIALE, null);

      // N. persone a tempo pieno = uomini + donne appartenenti
      // al tipo classe manodopera SALARIATI avventizi
      vTipoClassiManodopera.clear();
      vTipoClassiManodopera.add((String) SolmrConstants
          .get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI"));
      hmManodopera = calcoloElencoManodopera(hmManodopera, manodoperaVO,
          vTipoClassiManodopera, SALAR_AVVENTIZI, null);

      // corretta visualizzazione dei campi numerici,
      // inoltre il metodo deve restituire un Vector
      Collection<FrmManodoperaVO> cManodopera = hmManodopera.values();
      vManodopera = new Vector<FrmManodoperaVO>();
      for (Iterator<FrmManodoperaVO> i = cManodopera.iterator(); i.hasNext();)
      {
        frmManodoperaVO = (FrmManodoperaVO) i.next();

        SolmrLogger.debug(this, "dentro iterator idManodopera: "
            + frmManodoperaVO.getIdManodopera());
        // SolmrLogger.debug(this, "dentro iterator totale ore: " +
        // frmManodoperaVO.getTotaleULU());

        // ULU = ore / 1800
        // frmManodoperaVO.setTotaleULU("" +
        // (frmManodoperaVO.getTotaleULULong().longValue() / 1800));
        // SolmrLogger.debug(this, "dentro iterator totale ULU: " +
        // frmManodoperaVO.getTotaleULU());

        // in caso di null viene visualizzato lo zero
        if (frmManodoperaVO.getNumPersTempoPieno() == null)
          frmManodoperaVO.setNumPersTempoPieno("0");
        if (frmManodoperaVO.getNumPersTempoParz() == null)
          frmManodoperaVO.setNumPersTempoParz("0");
        if (frmManodoperaVO.getNumSalariatiAvventizi() == null)
          frmManodoperaVO.setNumSalariatiAvventizi("0");

        vManodopera.add(frmManodoperaVO);
      }
      SolmrLogger.debug(this, "dopo iterator vManodopera.size(): "
          + vManodopera.size());
    }
    catch (Exception ex)
    {
      throw ex;
    }

    return vManodopera;
  }

  /**
   * Dettaglio manodopera
   * 
   * @param idManodopera
   * @return ManodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException, Exception
  {
    ManodoperaVO manodoperaVO = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    Vector<DettaglioAttivitaVO> vDettaglioAttivita = null;

    try
    {
      // Dati manodopera azienda
      manodoperaVO = manodoperaDAO.findManodoperaByIdManodopera(idManodopera);

      // Numero persone e totali suddivise per tipo classe
      vDettaglioManodopera = manodoperaDAO
          .getDettaglioManClassiByIdManodopera(idManodopera);
      // elemento 1 del vettore: TIPO_CLASSI_MANODOPERA CODICE = 10
      // elemento 2 del vettore: TIPO_CLASSI_MANODOPERA CODICE = 20
      // elemento 3 del vettore: TIPO_CLASSI_MANODOPERA CODICE = 30
      // elemento 4 del vettore: TIPO_CLASSI_MANODOPERA CODICE = 40
      // elemento 5 del vettore: TIPO_CLASSI_MANODOPERA CODICE = 50

      // nel caso in cui il vettore contenga un numero di elementi inferiore a 5
      // l'elemento assente viene sostituito da valori numeri impostati a zero
      if (vDettaglioManodopera != null && vDettaglioManodopera.size() < 5)
      {
        // Inizializzazione di un vettore di supporto a 5 elementi
        Vector<DettaglioManodoperaVO> vDettaglioManodoperaNew = new Vector<DettaglioManodoperaVO>();
        for (int contElem = 10; contElem <= 50; contElem = contElem + 10)
        {
          dettaglioManodoperaVO = new DettaglioManodoperaVO();

          dettaglioManodoperaVO.setCodTipoClasseManodopera("" + contElem);
          dettaglioManodoperaVO.setUomini("0");
          dettaglioManodoperaVO.setDonne("0");
          vDettaglioManodoperaNew.add(dettaglioManodoperaVO);
        }

        Iterator<DettaglioManodoperaVO> iteraDettaglioManodopera = vDettaglioManodopera
            .iterator();
        int contElem = 0;
        while (iteraDettaglioManodopera.hasNext())
        {
          dettaglioManodoperaVO = (DettaglioManodoperaVO) iteraDettaglioManodopera
              .next();

          SolmrLogger.debug(this,
              "dettaglioManodoperaVO.getCodTipoClasseManodopera(): "
                  + dettaglioManodoperaVO.getCodTipoClasseManodopera());

          contElem = new Integer(dettaglioManodoperaVO
              .getCodTipoClasseManodopera()).intValue();
          SolmrLogger.debug(this, "contElem: " + contElem);

          SolmrLogger.debug(this, "Elemento sostituito nella posizione "
              + (contElem / 10 - 1) + " del vettore!!!!!!");

          vDettaglioManodoperaNew.remove(contElem / 10 - 1);
          vDettaglioManodoperaNew.add(contElem / 10 - 1, dettaglioManodoperaVO);
        }

        SolmrLogger.debug(this, "vDettaglioManodoperaNew.size(): "
            + vDettaglioManodoperaNew);
        // il vettore è stato modificato in modo che contenga 5 elementi
        manodoperaVO.setVDettaglioManodopera(vDettaglioManodoperaNew);
      }
      else
      {
        // il vettore proveniente dal DAO contiene 5 elementi o è null
        manodoperaVO.setVDettaglioManodopera(vDettaglioManodopera);
      }

      // Attività complementari svolte in azienda
      vDettaglioAttivita = manodoperaDAO
          .getDetAttivitaComplByIdManodopera(idManodopera);
      manodoperaVO.setVDettaglioAttivita(vDettaglioAttivita);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }

    return manodoperaVO;
  }

  /**
   * Effettua il calcolo per ottenere l'elenco delle dichiarazioni relative alla
   * manodopera associata a un'azienda
   * 
   * @param hmManodopera
   * @param manodoperaVO
   * @param vTipoClassiManodopera
   * @param tipoClasse
   * @return HashMap
   * @throws SolmrException
   * @throws Exception
   */
  private TreeMap<Long, FrmManodoperaVO> calcoloElencoManodopera(
      TreeMap<Long, FrmManodoperaVO> hmManodopera, ManodoperaVO manodoperaVO,
      Vector<String> vTipoClassiManodopera, String tipoClasse,
      Long idPianoRiferimento) throws SolmrException, Exception
  {
    Vector<DettaglioManodoperaVO> vManodoperaAnnua = null;
    Long idManodopera = null;
    FrmManodoperaVO frmManodoperaVO = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    long numPersone = 0;
    // long sommaOre = 0;

    try
    {
      if (idPianoRiferimento == null)
        vManodoperaAnnua = manodoperaDAO.getManodoperaAnnua(manodoperaVO,
            vTipoClassiManodopera);
      else
        vManodoperaAnnua = manodoperaDAO.getManodoperaAnnua(manodoperaVO,
            vTipoClassiManodopera, idPianoRiferimento);

      SolmrLogger.debug(this, "vManodoperaAnnua.size(): "
          + vManodoperaAnnua.size());
      for (int cntManodopera = 0; cntManodopera < vManodoperaAnnua.size(); cntManodopera++)
      {
        dettaglioManodoperaVO = (DettaglioManodoperaVO) vManodoperaAnnua
            .get(cntManodopera);

        idManodopera = dettaglioManodoperaVO.getIdManodoperaLong();
        SolmrLogger.debug(this, "idManodopera: " + idManodopera);

        // tiene traccia delle somme relative a vari richiami di query ma
        // riguardanti una stessa riga dell'elenco manodopera
        if (hmManodopera.containsKey(idManodopera))
        {
          SolmrLogger.debug(this,
              "dentro if hmManodopera.containsKey(idManodopera)");
          frmManodoperaVO = (FrmManodoperaVO) hmManodopera.get(idManodopera);
        }
        else
        {
          SolmrLogger.debug(this,
              "dentro else hmManodopera.containsKey(idManodopera)");
          frmManodoperaVO = new FrmManodoperaVO();

          frmManodoperaVO.setIdManodoperaLong(idManodopera);
          frmManodoperaVO.setCodiceINPS(dettaglioManodoperaVO.getCodiceInps());
          frmManodoperaVO.setMatrciolaInail(dettaglioManodoperaVO.getMatricolaInail());

          // frmManodoperaVO.setTotaleULU("0");

          frmManodoperaVO.setDataInizioValidita(dettaglioManodoperaVO
              .getDataInizioValidita());
          frmManodoperaVO.setDataFineValidita(dettaglioManodoperaVO
              .getDataFineValidita());
          frmManodoperaVO.setTipoFormaConduzioneVO(dettaglioManodoperaVO
              .getTipoFormaConduzioneVO());

          SolmrLogger.debug(this,
              "dettaglioManodoperaVO.getDataInizioValidita(): "
                  + dettaglioManodoperaVO.getDataInizioValidita());
          SolmrLogger.debug(this,
              "dettaglioManodoperaVO.getDataFineValidita(): "
                  + dettaglioManodoperaVO.getDataFineValidita());
        }

        // somma delle ore annue di ciascun record relativo a idManodopera;
        // per ottenere il totale ULU, il totale ore andrà diviso per 1800
        /*
         * SolmrLogger.debug(this, "frmManodoperaVO.getTotaleULULong(): " +
         * frmManodoperaVO.getTotaleULULong()); SolmrLogger.debug(this,
         * "dettaglioManodoperaVO.getGiornateAnnueLong(): " +
         * dettaglioManodoperaVO.getGiornateAnnueLong()); sommaOre =
         * frmManodoperaVO.getTotaleULULong().longValue() +
         * dettaglioManodoperaVO.getGiornateAnnueLong().longValue();
         * SolmrLogger.debug(this, "sommaOre: " + sommaOre);
         * frmManodoperaVO.setTotaleULU("" + sommaOre);
         */

        // numero persone = dettaglio manodopera uomini + donne per ciascun
        // record manodopera
        SolmrLogger.debug(this, "dettaglioManodoperaVO.getDonne(): "
            + dettaglioManodoperaVO.getDonne());
        SolmrLogger.debug(this, "dettaglioManodoperaVO.getUomini(): "
            + dettaglioManodoperaVO.getUomini());
        numPersone = dettaglioManodoperaVO.getDonneLong().longValue()
            + dettaglioManodoperaVO.getUominiLong().longValue();
        SolmrLogger.debug(this, "numPersone: " + numPersone);

        SolmrLogger.debug(this, "tipoClasse: " + tipoClasse);
        if (tipoClasse.equals(TEMPO_PIENO))
        {
          frmManodoperaVO.setNumPersTempoPieno("" + numPersone);
        }
        else if (tipoClasse.equals(TEMPO_PARZIALE))
        {
          frmManodoperaVO.setNumPersTempoParz("" + numPersone);
        }
        else if (tipoClasse.equals(SALAR_AVVENTIZI))
        {
          frmManodoperaVO.setNumSalariatiAvventizi("" + numPersone);
        }

        hmManodopera.put(idManodopera, frmManodoperaVO);
      }
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }

    return hmManodopera;
  }

  /**
   * Cancellazione di tutti i dati relativi alla manodopera
   * 
   * @param idManodopera
   * @throws SolmrException
   * @throws Exception
   */
  public void deleteManodopera(Long idManodopera) throws SolmrException,
      Exception
  {
    ManodoperaVO manodoperaVO = null;

    try
    {
      manodoperaVO = manodoperaDAO.findManodoperaByIdManodopera(idManodopera);

      // E' possibile eliminare la manodopera solo nel caso in cui
      // la data fine validità sia impostata a null
      SolmrLogger.debug(this,
          "ManodoperaBean deleteManodopera manodoperaVO.getDataFineValidita(): "
              + manodoperaVO.getDataFineValidita());
      if (manodoperaVO.getDataFineValidita() != null)
      {
        throw new SolmrException(
            "Dichiarazione di manodopera storicizzata. Impossibile procedere");
      }
      else
      {
        if (manodoperaDAO.isManodoperaDichiarata(idManodopera))
        {
          throw new SolmrException(
              "Non è possibile eliminare la manodopera in quanto "
                  + "è stata inclusa nella dichiarazione di consistenza. ");
        }
        else
        {
          // Cancellazione della dichiarazione di manodopera in questione
          // (compresi i relativi dettagli di manodopera ed attività
          // complementari)
          manodoperaDAO.deleteDettaglioAttivita(idManodopera);
          SolmrLogger.debug(this, "ManodoperaBean deleteDettaglioAttivita...");
          manodoperaDAO.deleteDettaglioManodopera(idManodopera);
          SolmrLogger
              .debug(this, "ManodoperaBean deleteDettaglioManodopera...");
          manodoperaDAO.deleteManodopera(idManodopera);
          SolmrLogger.debug(this, "ManodoperaBean deleteManodopera...");
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Inserimento di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    Vector<DettaglioAttivitaVO> vDettaglioAttivita = null;
    DettaglioAttivitaVO dettaglioAttivitaVO = null;
    Long idManodopera = null;
    //ValidationErrors errors = null;

    try
    {
      if (manodoperaDAO.isManodoperaValida(null, manodoperaVO
          .getIdAziendaLong()))
      {
        // esiste già una dichiarazione di manodopera valida

        throw new SolmrException(
            "Impossibile inserire una nuova dichiarazione di manodopera. "
                + "Qualora l''attuale dichiarazione di manodopera non risulti più valida, "
                + "procedere su questa attraverso la funzione di cessazione "
                + "e solo successivamente proseguire con "
                + "l''inserimento della nuova dichiarazione.");
      }
      else
      {
        // non esiste una dichiarazione di manodopera valida,
        // è possibile effettuarne l'inserimento

        // Validazione dati inserimento
        /*errors = manodoperaVO.validateManodopera();
        if (errors != null && errors.size() > 0)
        {
          throw new SolmrException(
              "Errore nella validazione dei dati di manodopera", errors);
        }
        errors = manodoperaVO.validateConduzione(manodoperaDAO
            .findLastManodopera(idAzienda).getDataFineValidita());
        if (errors != null && errors.size() > 0)
        {
          throw new SolmrException(
              "Errore nella validazione dei dati di conduzione manodopera",
              errors);
        }*/

        // inserimento manodopera
        idManodopera = manodoperaDAO.insertManodopera(manodoperaVO);

        // inserimento dettaglio manodopera
        vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();
        if (vDettaglioManodopera != null)
        {
          for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera
              .iterator(); iter.hasNext();)
          {
            dettaglioManodoperaVO = (DettaglioManodoperaVO) iter.next();
            manodoperaDAO.insertDettaglioManodopera(idManodopera,
                dettaglioManodoperaVO);
          }
        }

        // inserimento dettaglio attivita
        vDettaglioAttivita = manodoperaVO.getVDettaglioAttivita();
        if (vDettaglioAttivita != null)
        {
          for (Iterator<DettaglioAttivitaVO> iter = vDettaglioAttivita
              .iterator(); iter.hasNext();)
          {
            dettaglioAttivitaVO = (DettaglioAttivitaVO) iter.next();
            manodoperaDAO.insertDettaglioAttivita(idManodopera,
                dettaglioAttivitaVO);
          }
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
  }
  
  
  private void insertManodoperaSian(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    Vector<DettaglioAttivitaVO> vDettaglioAttivita = null;
    DettaglioAttivitaVO dettaglioAttivitaVO = null;
    Long idManodopera = null;
    

    try
    {
      // inserimento manodopera
      idManodopera = manodoperaDAO.insertManodopera(manodoperaVO);

      // inserimento dettaglio manodopera
      vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();
      if (vDettaglioManodopera != null)
      {
        for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera.iterator(); iter.hasNext();)
        {
          dettaglioManodoperaVO = (DettaglioManodoperaVO) iter.next();
          manodoperaDAO.insertDettaglioManodopera(idManodopera,
              dettaglioManodoperaVO);
        }
      }

      // inserimento dettaglio attivita
      vDettaglioAttivita = manodoperaVO.getVDettaglioAttivita();
      if (vDettaglioAttivita != null)
      {
        for (Iterator<DettaglioAttivitaVO> iter = vDettaglioAttivita
            .iterator(); iter.hasNext();)
        {
          dettaglioAttivitaVO = (DettaglioAttivitaVO) iter.next();
          manodoperaDAO.insertDettaglioAttivita(idManodopera,
              dettaglioAttivitaVO);
        }
      }
      
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Ultima dichiarazione di manodopera non valida
   * 
   * @return ManodoperaVO
   * @throws DataAccessException
   */
  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException,
      Exception
  {
    try
    {
      return manodoperaDAO.findLastManodopera(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * restituisce un'eccezione se trova una manodopera valida
   * 
   * @return boolean
   * @throws SolmrException
   * @throws Exception
   */
  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException, Exception
  {
    boolean isValida = false;
    String errore = "";

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try
    {
      isValida = manodoperaDAO.isManodoperaValida(idManodopera, idAzienda);
      SolmrLogger.debug(this, "isValida: " + isValida);

      if (isValida)
      {
        // esiste già una dichiarazione di manodopera valida

        if (idManodopera == null)
        {
          // Controllo per l'inserimento
          errore = 
              "Impossibile inserire una nuova dichiarazione di manodopera. "
                  + "Qualora l''attuale dichiarazione di manodopera non risulti più valida, "
                  + "operare su questa attraverso la funzione di cessazione "
                  + "e solo successivamente proseguire con "
                  + "l''inserimento della nuova dichiarazione.";
        }
      }
      else
      {
        if (idManodopera != null)
        {
          // Controllo per la modifica
          errore = 
              "Dichiarazione di manodopera storicizzata. Impossibile procedere.";
        }
      }

    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    
    return errore;
  }

  /**
   * Modifica di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    //ValidationErrors errors = null;

    try
    {
      if (!manodoperaDAO.isManodoperaValida(manodoperaVO.getIdManodoperaLong(),
          manodoperaVO.getIdAziendaLong()))
      {
        // non esiste una dichiarazione di manodopera valida

        throw new SolmrException(
            "Dichiarazione di manodopera storicizzata. Impossibile procedere.");
      }
      else
      {

        // Validazione dati modifica
       /* errors = manodoperaVO.validateManodopera();
        if (errors != null && errors.size() > 0)
        {
          throw new SolmrException(
              "Errore nella validazione dei dati di manodopera", errors);
        }
        errors = manodoperaVO.validateConduzione(manodoperaDAO
            .findLastManodopera(idAzienda).getDataFineValidita());
        if (errors != null && errors.size() > 0)
        {
          throw new SolmrException(
              "Errore nella validazione dei dati di conduzione manodopera",
              errors);
        }*/

        manodoperaDAO.lockTableManodopera(manodoperaVO.getIdManodoperaLong()
            .longValue());

        // Storicizzo
        if (manodoperaDAO.isManodoperaDichiarata(manodoperaVO
            .getIdManodoperaLong()))
        {
          // Storicizzo
          manodoperaDAO.storicizzaManodopera(manodoperaVO);
          // Inserisce manodopera, dettaglio manodopera e dettaglio attività
          insertManodopera(manodoperaVO, idAzienda);
        }
        else
        // Update
        {
          manodoperaDAO.updateManodopera(manodoperaVO);

          Vector<DettaglioManodoperaVO> vDettaglioManodopera = manodoperaVO
              .getVDettaglioManodopera();

          if (vDettaglioManodopera != null)
          {
            //esistono su db fare update!!!!
            if(manodoperaDAO.getDettaglioManClassiByIdManodopera(manodoperaVO.getIdManodoperaLong()) != null)
            {              
              for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera
                  .iterator(); iter.hasNext();)
              {
                DettaglioManodoperaVO dettaglioManodoperaVO = (DettaglioManodoperaVO) iter
                    .next();
                dettaglioManodoperaVO.setIdManodoperaLong(manodoperaVO
                    .getIdManodoperaLong());
                manodoperaDAO.updateDettaglioManodopera(dettaglioManodoperaVO);
              }
            }
            else
            {
              // inserimento dettaglio manodopera
              vDettaglioManodopera = manodoperaVO.getVDettaglioManodopera();
              for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera
                .iterator(); iter.hasNext();)
              {
                DettaglioManodoperaVO dettaglioManodoperaVO = (DettaglioManodoperaVO) iter.next();
                manodoperaDAO.insertDettaglioManodopera(manodoperaVO.getIdManodoperaLong(),
                  dettaglioManodoperaVO);
              }
            }
          }
          else
          {
            //Cancello se ci fossero tutti i record!!!
            manodoperaDAO.deleteDettaglioManodopera(manodoperaVO
                .getIdManodoperaLong());
          }

          Vector<DettaglioAttivitaVO> vDettaglioAttivita = manodoperaVO
              .getVDettaglioAttivita();

          // delete
          if (vDettaglioAttivita != null && vDettaglioAttivita.size() > 0)
          {
            manodoperaDAO.deleteDettaglioAttivita(manodoperaVO
                .getIdManodoperaLong());
            SolmrLogger.debug(this,
                "delete delle attivita complementari presenti su db\n\n\n");
          }

          // Inserimento delle attività complementari su db
          if (vDettaglioAttivita != null)
          {
            for (Iterator<DettaglioAttivitaVO> iter = vDettaglioAttivita
                .iterator(); iter.hasNext();)
            {
              DettaglioAttivitaVO dettaglioAttivitaVO = (DettaglioAttivitaVO) iter
                  .next();
              manodoperaDAO.insertDettaglioAttivita(manodoperaVO
                  .getIdManodoperaLong(), dettaglioAttivitaVO);
            }
          }

        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
  }
  
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException, Exception
  {
    try
    {
      ManodoperaVO manodoperaVO = manodoperaDAO.findManodoperaAttivaByIdAzienda(idAzienda);
      //ne esiste una attiva!!!
      if(manodoperaVO != null)
      {
        manodoperaVO = dettaglioManodopera(manodoperaVO.getIdManodoperaLong());
        // Storicizzo
        manodoperaDAO.storicizzaManodopera(manodoperaVO);
        // Inserisce manodopera, dettaglio manodopera e dettaglio attività
        UtenteIrideVO utenteAggiornamento = new UtenteIrideVO();
        utenteAggiornamento.setIdUtente(idUtente);
        manodoperaVO.setUtenteAggiornamento(utenteAggiornamento);
        if(Validator.isNotEmpty(manodoperaChgVO.getCodiceInps()))
          manodoperaVO.setCodiceInps(manodoperaChgVO.getCodiceInps());
        if(Validator.isNotEmpty(manodoperaChgVO.getIdTipoIscrizioneINPS()))
          manodoperaVO.setIdTipoIscrizioneINPS(manodoperaChgVO.getIdTipoIscrizioneINPS());
        if(Validator.isNotEmpty(manodoperaChgVO.getDataInizioIscrizioneDate()))
          manodoperaVO.setDataInizioIscrizioneDate(manodoperaChgVO.getDataInizioIscrizioneDate());
        if(Validator.isNotEmpty(manodoperaChgVO.getDataCessazioneIscrizioneDate()))
          manodoperaVO.setDataCessazioneIscrizioneDate(manodoperaChgVO.getDataCessazioneIscrizioneDate());
        
        insertManodoperaSian(manodoperaVO, idAzienda);
      }
      else
      // Update
      {
        Date data = new Date();
        manodoperaChgVO.setIdAziendaLong(idAzienda);
        manodoperaChgVO.setDataInizioValiditaDate(data);
        UtenteIrideVO utenteAggiornamento = new UtenteIrideVO();
        utenteAggiornamento.setIdUtente(idUtente);
        manodoperaChgVO.setUtenteAggiornamento(utenteAggiornamento);
        manodoperaChgVO.setDataAggiornamentoDate(data);
        manodoperaDAO.insertManodopera(manodoperaChgVO);
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    catch (Exception ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
  }

  // Metodo per effettuare la cessazione di una manodopera
  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception
  {
    try
    {
      manodoperaDAO.storicizzaManodopera(manodoperaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  

  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException, Exception
  {
    try
    {
      return manodoperaDAO.getElencoTipoIscrizioneINPSAttivi();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    try
    {
      return manodoperaDAO.findManodoperaAttivaByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
}
