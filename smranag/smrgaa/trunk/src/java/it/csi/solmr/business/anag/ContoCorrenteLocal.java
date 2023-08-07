package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.BancaSportelloVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.exception.SolmrException;

import java.util.Vector;

import javax.ejb.Local;

@Local
public interface ContoCorrenteLocal
{

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,java.util.Date dataSituazioneAl, Boolean soloAnno)
    throws  Exception;

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,boolean estinto)
    throws  Exception;


  // Metodo per effettuare una cancellazione logica da un conto corrente
  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
    throws Exception, SolmrException;

  // Inserisce un nuovo conto corrente
  public void insertContoCorrente(ContoCorrenteVO conto,
                                  long idUtenteAggiornamento)
      throws Exception, SolmrException;
  
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
      throws Exception, SolmrException;

  // Carica le informazioni su un conto corrente
  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
    throws Exception;

  public BancaSportelloVO[] searchSportello(String abi,String cab,String comune)
      throws Exception, SolmrException;

  public BancaSportelloVO[] searchBanca(String abi,String denominazione)
      throws  SolmrException, Exception;

  public void desistsAccountCorrent(Long idAzienda, Long idUtente) throws SolmrException, Exception;
}
