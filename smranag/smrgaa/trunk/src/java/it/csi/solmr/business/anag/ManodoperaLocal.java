package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.FrmManodoperaVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.dto.anag.TipoIscrizioneINPSVO;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.SolmrException;

import java.util.Vector;

import javax.ejb.Local;

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

@Local
public interface ManodoperaLocal
{
  /**
   * Elenco Manodopera
   * 
   * @param manodoperaVO
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<FrmManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception;

  public Vector<FrmManodoperaVO> getManodoperaByPianoRifererimento(
      ManodoperaVO manodoperaVO, Long idPianoRiferimento)
      throws SolmrException, Exception;

  /**
   * Dettaglio Manodopera
   * 
   * @param idManodopera
   * @return ManodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public ManodoperaVO dettaglioManodopera(Long idManodopera)
      throws SolmrException, Exception;

  /**
   * Cancellazione di tutti i dati relativi alla manodopera
   * 
   * @param idManodopera
   * @throws SolmrException
   * @throws Exception
   */
  public void deleteManodopera(Long idManodopera) throws SolmrException,
      Exception;

  /**
   * Inserimento di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public void insertManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception;

  /**
   * Ultima dichiarazione di manodopera non valida
   * 
   * @return ManodoperaVO
   * @throws DataAccessException
   */
  public ManodoperaVO findLastManodopera(Long idAzienda) throws SolmrException,
      Exception;

  /**
   * Controllo esistenza manodopera valida
   * 
   * @param idManodopera
   * @param idAzienda
   * @return boolean
   * @throws SolmrException
   * @throws Exception
   */
  public String isManodoperaValida(Long idManodopera, Long idAzienda)
      throws SolmrException, Exception;

  /**
   * Modifica di tutti i dati relativi alla manodopera
   * 
   * @param manodoperaVO
   * @throws SolmrException
   * @throws Exception
   */
  public void updateManodopera(ManodoperaVO manodoperaVO, Long idAzienda)
      throws SolmrException, Exception;
  
  public void updateManodoperaSian(ManodoperaVO manodoperaChgVO, long idAzienda, long idUtente)
      throws SolmrException, Exception;

  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws SolmrException, Exception;
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi()
      throws SolmrException, Exception;
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda)
      throws SolmrException, Exception;
}
