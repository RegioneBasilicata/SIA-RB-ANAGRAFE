package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoBioVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti;
import it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAnimStab;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoDestinoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoMungitura;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoSottoCategoriaAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.HashMap;
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
public interface AllevamentoLocal 
{
  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(long idCategoriaAnimale) throws SolmrException;
  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(long idSottoCategoriaAnimale) throws SolmrException;
  public TipoMungitura[] getTipiMungitura() throws SolmrException;
  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(long idAllevamento) throws SolmrException;
  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,boolean modifica) throws SolmrException;
  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale) throws SolmrException;
  public Vector<TipoTrattamento> getTipiTrattamento() throws SolmrException;
  public TipoTrattamento getTipoTrattamento(long idTrattamento) throws SolmrException;
  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(long idSottoCategoriaAnimale,boolean palabile, long idStabulazione) throws SolmrException;
  public Vector<AllevamentoVO> getElencoAllevamentiExcel(long idAzienda, Date dataInserimentiDich, Long idUte)  throws SolmrException;
  public Vector<AllevamentoBioVO> getAllevamentiBio(Date dataInserimentoDichiarazione, long idAllevamento) 
    throws SolmrException;
  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio() throws SolmrException;
  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(long idAllevamento)
      throws SolmrException;  
  public Vector<Long> getElencoSpecieAzienda(long idAzienda) 
      throws SolmrException;
  //public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long idAzienda)  
    //  throws SolmrException;
  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa, 
      long idTipoSpecie, String codAziendaZoo) throws SolmrException;  
  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(long idAllevamento)
      throws SolmrException;
  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException;    
  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException;
}
