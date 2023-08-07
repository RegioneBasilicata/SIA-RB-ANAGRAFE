package it.csi.solmr.interfaceCSI.anag;

 // <p>Title: S.O.L.M.R.</p>
 // <p>Description: Servizi On-Line per il Mondo Rurale</p>
 // <p>Copyright: Copyright (c) 2003</p>
 // <p>Company: TOBECONFIG</p>
 // @author
 // @version 1.0

import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.UserException;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;

import java.util.Date;

public interface AnagCSIInterface
{
  public ProvinciaVO[] getProvinceByRegione(String idRegione) throws UserException, CSIException;
  public AnagAziendaVO findAziendaAttiva(Long idAzienda) throws UserException, CSIException;
  public String getRappLegaleTitolareByIdAzienda(Long idAzienda) throws UserException, CSIException;
  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)throws UserException, CSIException;
  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(Long idAzienda, Date dataSituazioneAl) throws UserException, CSIException;
  public CodeDescription[] getTipiIntermediario() throws UserException, CSIException;
  public String getFlagPartitaIva(Long idTipoFormaGiuridica) throws UserException, CSIException;
  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica) throws UserException, CSIException;
  public boolean isProvinciaReaValida(String siglaProvincia) throws UserException, CSIException;
  public String ricercaCodiceComune(String descrizioneComune, String siglaProvincia) throws UserException, CSIException;
  public String ricercaCodiceComuneNonEstinto(String descrizioneComune, String siglaProvincia) throws UserException, CSIException;
  public String ricercaCodiceFiscaleComune(String descrizioneComune, String siglaProvincia) throws UserException, CSIException;
  public void checkIsCUAAPresent(String cuaa, Long idAzienda) throws UserException, CSIException;
  public void checkPartitaIVA(String partitaIVA, Long idAzienda) throws UserException, CSIException;
  public AnagAziendaVO getAziendaById(Long idAnagAzienda) throws UserException, CSIException;
  public PersonaFisicaVO getPersonaFisica(String cuaa) throws UserException, CSIException;
  public ComuneVO getComuneByCUAA(String cuaa) throws UserException, CSIException;
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(Long idAnagAzienda) throws UserException, CSIException;
  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa, String partitaIva)  throws UserException, CSIException;
  public CodeDescription[] getTipiIntermediarioUmaProv() throws UserException, CSIException;
  public PersonaFisicaVO[] getSoggetti(Long idAzienda, Date data)throws UserException, CSIException;
  public PersonaFisicaVO[] getSoggetti(Long idAzienda, Boolean storico)throws UserException, CSIException;
  public DelegaVO getDelegaByAzienda(Long idAzienda, Long idProcedimento) throws UserException, CSIException;
  public String getDenominazioneByIdAzienda(Long idAzienda) throws UserException, CSIException;
  public ComuneVO[] ricercaStatoEstero(String statoEstero, String estinto) throws UserException, CSIException;
  public ComuneVO[] getComuniLikeProvAndCom(String provincia, String comune) throws UserException, CSIException;
  public ComuneVO[] getComuniNonEstintiLikeProvAndCom(String provincia, String comune,String flagEstero) throws UserException, CSIException;
  public StringcodeDescription[] getProvincePiemonte() throws UserException, CSIException;
  public CodeDescription[] getTipiFormaGiuridica() throws UserException, CSIException;
}
