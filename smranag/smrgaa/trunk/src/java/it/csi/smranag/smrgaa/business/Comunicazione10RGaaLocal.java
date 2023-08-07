package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;
import it.csi.solmr.exception.SolmrException;

import java.math.BigDecimal;
import java.util.Date;
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
public interface Comunicazione10RGaaLocal 
{
  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(long idUte, long idPianoRiferimento) 
    throws SolmrException;
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[]) throws SolmrException;
  
  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[]) throws SolmrException;
  
  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente() throws SolmrException;
  
  public Vector<TipoEffluenteVO> getListTipoEffluente() throws SolmrException;
  
  public TipoEffluenteVO getTipoEffluenteById(long idEffluente) 
      throws SolmrException;
  
  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica() throws SolmrException;
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R[], Long idTipoCausale) 
    throws SolmrException;
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(long idComunicazione10R[]) 
    throws SolmrException;
  
  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R[]) 
    throws SolmrException;
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[]) 
    throws SolmrException;
  
  public PlSqlCodeDescription storicizzaComunicazione10R(long idUtenteAggiornamento, long idAzienda,
	      Vector<Comunicazione10RVO> vCom10r, Vector<EffluenteVO> vEffluentiTratt, 
	      Vector<EffluenteCesAcqVO> vCessioniAcquisizioni, Vector<EffluenteCesAcqVO> vCessioni,
	      Vector<EffluenteStocExtVO> vStoccaggi, Vector<AcquaExtraVO> vAcqueExtra) throws SolmrException;
  
  public boolean hasEffluenteProdotto(long idEffluente, long idUte) 
    throws SolmrException;
  
  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(long idAzienda, long idPianoRiferimento) 
    throws SolmrException;
  
  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte, long idEffluente) 
    throws SolmrException;


  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte, long idComunicazione10r, 
    long idCausaleEffluente, long idEffluente, BigDecimal quantita) 
    throws SolmrException;
  
  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, 
      long idUtente) 
    throws SolmrException;
  
  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(long idComunicazione10R) 
    throws SolmrException;
  
  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R, boolean palabile) 
    throws SolmrException;
  
  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda, 
      long idAziendaCess, long idCausaleEffluente, long idEffluente) 
    throws SolmrException;
  
  public boolean controlloRefluoPascolo(long idUte) 
      throws SolmrException;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte)
      throws SolmrException;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente) 
      throws SolmrException;
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(
      long idComunicazione10R, long idEffluente) 
    throws SolmrException;
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[]) 
      throws SolmrException;
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[]) 
      throws SolmrException;
  
  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte, long idComunicazione10r, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException;
  
  public Vector<RefluoEffluenteVO> getListRefluiStampa(long idComunicazione10R[]) 
    throws SolmrException;
  
  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte)
      throws SolmrException;
  
  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte)
      throws SolmrException;
  
  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(long idComunicazione10r) 
      throws SolmrException;

}
