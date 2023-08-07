package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TesserinoFitoSanitarioVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.ws.infoc.Azienda;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface SoggettiLocal
{
  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Date data)
      throws Exception, SolmrException;

  public Vector<PersonaFisicaVO> getSoggetti(Long idAzienda, Boolean storico)
      throws Exception, SolmrException;

  public PersonaFisicaVO getDettaglioSoggetti(Long idSoggetto, Long idAzienda)
      throws Exception;

  public void insertSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public void checkForDeleteSoggetto(Long idContitolare)
      throws Exception, SolmrException;

  public void deleteContitolare(Long idContitolare) throws Exception,
      SolmrException;

  public void updateSoggetto(PersonaFisicaVO pfVO, long idUtenteAggiornamento)
      throws Exception, SolmrException;

  public Vector<Long> getIdPersoneFisiche(String codFiscale, String cognome,
      String nome, String dataNascita, String istatNascita,
      String istatResidenza, boolean personaAttiva) throws Exception,
      SolmrException;

  public Vector<PersonaFisicaVO> getListPersoneFisicheByIdRange(
      Vector<Long> collIdPF) throws Exception, SolmrException;

  public PersonaFisicaVO findByPrimaryKey(Long idPersonaFisica)
      throws Exception, SolmrException;

  public Vector<Long> getIdAziendeBySoggetto(Long idSoggetto)
      throws Exception, SolmrException;

  public PersonaFisicaVO findPersonaFisica(Long idPersonaFisica)
      throws Exception, SolmrException;

  public PersonaFisicaVO getDatiSoggettoPerMacchina(Long idPersonaFisica)
      throws Exception, SolmrException;

  public Vector<PersonaFisicaVO> findPersonaFisicaByIdSoggettoAndIdAzienda(
      Long idSoggetto, Long idAzienda) throws Exception, SolmrException;

  public PersonaFisicaVO getDettaglioSoggettoByIdContitolare(Long idContitolare)
      throws Exception, SolmrException;
  
  public  TesserinoFitoSanitarioVO getTesserinoFitoSanitario(String codiceFiscale) 
      throws Exception, SolmrException;

  public void updateDatiSoggettoAndStoricizzaResidenza(
      PersonaFisicaVO newPersonaFisicaVO, PersonaFisicaVO oldPersonaFisicaVO,
      long idUtenteAggiornamento) throws Exception, SolmrException;

  public void importaSoggCollAAEP(AnagAziendaVO anagAziendaVO,
      Azienda impresaInfoc, HashMap<?,?> listaPersone, String idParametri[],
      Long idUtenteAggiornamento) throws Exception,SolmrException ;

}
