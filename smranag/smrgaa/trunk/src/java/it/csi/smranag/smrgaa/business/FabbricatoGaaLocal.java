package it.csi.smranag.smrgaa.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.Local;

import it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.exception.SolmrException;



/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface FabbricatoGaaLocal 
{
  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(Long idTipologiaFabbricato) throws SolmrException;
  
  public TipoFormaFabbricatoVO getTipoFormaFabbricato(Long idFormaFabbricato) throws SolmrException;
  
  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio() throws SolmrException;
  
  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda, 
      Date dataInserimentoDichiarazione) throws SolmrException;
  
  public HashMap<Long,String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException;
  
  
}
