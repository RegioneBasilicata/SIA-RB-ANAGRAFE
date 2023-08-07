package it.csi.smranag.smrgaa.integration;


import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.IterRichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.PartFabbrAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.StatoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UnitaMisuraVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UteAziendaNuovaVO;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class NuovaIscrizioneDAO extends it.csi.solmr.integration.BaseDAO {
  
  
  public NuovaIscrizioneDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public NuovaIscrizioneDAO(String refName) throws ResourceAccessException {
   super(refName);
  }

  /**
   * Azienda nuova col cuaa
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public AziendaNuovaVO getAziendaNuovaIscrizione(
      String cuaa, long[] arrTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT WAN.ID_AZIENDA_NUOVA, " +
        "       WAN.ID_TIPOLOGIA_AZIENDA, " +
        "       WAN.CUAA, " +
        "       WAN.PARTITA_IVA, " +
        "       WAN.DENOMINAZIONE, " +
        "       WAN.ID_FORMA_GIURIDICA, " +
        "       WAN.SEDELEG_COMUNE, " +
        "       COM_SEDE.DESCOM AS DESCOM_SEDE, " +
        "       PROV_SEDE.SIGLA_PROVINCIA AS SGL_PROV_SEDE, " +
        "       WAN.SEDELEG_INDIRIZZO, " +
        "       WAN.SEDELEG_CITTA_ESTERO, " +
        "       WAN.SEDELEG_CAP, " +
        "       WAN.MAIL, " +
        "       WAN.TELEFONO, " +
        "       WAN.FAX, " +
        "       WAN.PEC, " +
        "       WAN.NOTE, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       WAN.TIPO_SOGGETTO, " +
        "       WAN.COGNOME, " +
        "       WAN.NOME, " +
        "       WAN.CODICE_FISCALE, " +
        "       WAN.SESSO, " +
        "       WAN.DATA_NASCITA, " +
        "       WAN.COMUNE_NASCITA, " +
        "       COM_NAS.DESCOM AS DESCOM_NAS, " +
        "       PROV_NAS.SIGLA_PROVINCIA AS SGL_PROV_NAS, " +
        "       WAN.CITTA_NASCITA_ESTERO, " +
        "       WAN.RES_COMUNE, " +
        "       COM_RES.DESCOM AS DESCOM_RES, " +
        "       PROV_RES.SIGLA_PROVINCIA AS SGL_PROV_RES, " +
        "       WAN.RES_INDIRIZZO, " +
        "       WAN.RES_CITTA_ESTERO, " +
        "       WAN.RES_CAP, " +
        "       WAN.MAIL_SOGGETTO, " +
        "       WAN.TELEFONO_SOGGETTO, " +
        "       WAN.FAX_SOGGETTO, " +
        "       WAN.SEDELEG_UTE," +
        "       WAN.ID_AZIENDA_SUBENTRO, " +
        "       AA.DENOMINAZIONE AS DENOM_SUBENTRO, " +
        "       AA.CUAA AS CUAA_SUBENTRO, " +
        "       RA.ID_MOTIVO_RICHIESTA " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       COMUNE COM_SEDE, " +
        "       PROVINCIA PROV_SEDE, " +
        "       COMUNE COM_NAS, " +
        "       PROVINCIA PROV_NAS, " +
        "       COMUNE COM_RES, " +
        "       PROVINCIA PROV_RES, " +
        "       DB_ANAGRAFICA_AZIENDA AA " +
        "WHERE  WAN.CUAA = ? " +
        "AND    WAN.ID_AZIENDA_NUOVA = RA.ID_AZIENDA_NUOVA " +
        "AND    RA.ID_TIPO_RICHIESTA  IN (")
          .append(getIdListFromArrayForSQL(arrTipoRichiesta))
          .append(") ").append(
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRA.ID_ITER_RICHIESTA_AZIENDA = (SELECT MAX(IRA1.ID_ITER_RICHIESTA_AZIENDA) " +  
        "                                        FROM   SMRGAA_W_AZIENDA_NUOVA WAN1, " +
        "                                               DB_RICHIESTA_AZIENDA RA1, " +
        "                                               DB_ITER_RICHIESTA_AZIENDA IRA1 " +
        "                                        WHERE  WAN1.CUAA = WAN.CUAA " +
        "                                        AND    WAN1.ID_AZIENDA_NUOVA = RA1.ID_AZIENDA_NUOVA " +
        "                                        AND    RA1.ID_TIPO_RICHIESTA IN (1, 4) " +
        "                                        AND    RA1.ID_RICHIESTA_AZIENDA = IRA1.ID_RICHIESTA_AZIENDA " +
        "                                        AND    IRA1.DATA_FINE_VALIDITA IS NULL) "+
        "AND    WAN.SEDELEG_COMUNE = COM_SEDE.ISTAT_COMUNE (+) " +
        "AND    COM_SEDE.ISTAT_PROVINCIA = PROV_SEDE.ISTAT_PROVINCIA (+) " +
        "AND    WAN.COMUNE_NASCITA = COM_NAS.ISTAT_COMUNE (+) " +
        "AND    COM_NAS.ISTAT_PROVINCIA = PROV_NAS.ISTAT_PROVINCIA (+) " +
        "AND    WAN.RES_COMUNE = COM_RES.ISTAT_COMUNE (+) " +
        "AND    COM_RES.ISTAT_PROVINCIA = PROV_RES.ISTAT_PROVINCIA (+) " +
        "AND    WAN.ID_AZIENDA_SUBENTRO = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " );
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, cuaa);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        aziendaNuovaVO.setIdTipologiaAzienda(new Long(rs.getLong("ID_TIPOLOGIA_AZIENDA")));
        aziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        aziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        aziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaNuovaVO.setIdFormaGiuridica(new Long(rs.getLong("ID_FORMA_GIURIDICA")));
        aziendaNuovaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM_SEDE"));
        aziendaNuovaVO.setSedelegProv(rs.getString("SGL_PROV_SEDE"));
        aziendaNuovaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        aziendaNuovaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        aziendaNuovaVO.setSedelegCap(rs.getString("SEDELEG_CAP"));
        aziendaNuovaVO.setMail(rs.getString("MAIL"));
        aziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        aziendaNuovaVO.setFax(rs.getString("FAX"));
        aziendaNuovaVO.setPec(rs.getString("PEC"));
        aziendaNuovaVO.setNote(rs.getString("NOTE"));
        aziendaNuovaVO.setIdStatoRichiesta(checkLongNull(rs.getString("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
        aziendaNuovaVO.setCognome(rs.getString("COGNOME"));
        aziendaNuovaVO.setNome(rs.getString("NOME"));
        aziendaNuovaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        aziendaNuovaVO.setSesso(rs.getString("SESSO"));
        aziendaNuovaVO.setDataNascita(rs.getDate("DATA_NASCITA"));
        aziendaNuovaVO.setComuneNascita(rs.getString("COMUNE_NASCITA"));
        aziendaNuovaVO.setNascitaProv(rs.getString("SGL_PROV_NAS"));
        aziendaNuovaVO.setDescNascitaComune(rs.getString("DESCOM_NAS"));
        aziendaNuovaVO.setCittaNascitaEstero(rs.getString("CITTA_NASCITA_ESTERO"));
        aziendaNuovaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        aziendaNuovaVO.setResComune(rs.getString("RES_COMUNE"));
        aziendaNuovaVO.setDescResComune(rs.getString("DESCOM_RES"));
        aziendaNuovaVO.setResProvincia(rs.getString("SGL_PROV_RES"));
        aziendaNuovaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        aziendaNuovaVO.setResCap(rs.getString("RES_CAP"));
        aziendaNuovaVO.setMailSoggetto(rs.getString("MAIL_SOGGETTO"));
        aziendaNuovaVO.setTelefonoSoggetto(rs.getString("TELEFONO_SOGGETTO"));
        aziendaNuovaVO.setFaxSoggetto(rs.getString("FAX_SOGGETTO"));
        aziendaNuovaVO.setSedeLegUte(rs.getString("SEDELEG_UTE"));
        aziendaNuovaVO.setIdAziendaSubentro(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRO")));
        aziendaNuovaVO.setCuaaSubentro(rs.getString("CUAA_SUBENTRO"));
        aziendaNuovaVO.setDenomSubentro(rs.getString("DENOM_SUBENTRO"));
        aziendaNuovaVO.setIdMotivoRichiesta(checkIntegerNull(rs.getString("ID_MOTIVO_RICHIESTA")));
        
      }
      return aziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("cuaa", cuaa) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(
      String codEnte, long[] arrTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneEnte] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT WAN.ID_AZIENDA_NUOVA, " +
        "       WAN.ID_TIPOLOGIA_AZIENDA, " +
        "       WAN.CODICE_ENTE, " +
        "       WAN.CUAA, " +
        "       WAN.PARTITA_IVA, " +
        "       WAN.DENOMINAZIONE, " +
        "       WAN.ID_FORMA_GIURIDICA, " +
        "       WAN.SEDELEG_COMUNE, " +
        "       COM_SEDE.DESCOM AS DESCOM_SEDE, " +
        "       PROV_SEDE.SIGLA_PROVINCIA AS SGL_PROV_SEDE, " +
        "       WAN.SEDELEG_INDIRIZZO, " +
        "       WAN.SEDELEG_CITTA_ESTERO, " +
        "       WAN.SEDELEG_CAP, " +
        "       WAN.MAIL, " +
        "       WAN.TELEFONO, " +
        "       WAN.FAX, " +
        "       WAN.PEC, " +
        "       WAN.NOTE, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       WAN.TIPO_SOGGETTO, " +
        "       WAN.COGNOME, " +
        "       WAN.NOME, " +
        "       WAN.CODICE_FISCALE, " +
        "       WAN.SESSO, " +
        "       WAN.DATA_NASCITA, " +
        "       WAN.COMUNE_NASCITA, " +
        "       COM_NAS.DESCOM AS DESCOM_NAS, " +
        "       PROV_NAS.SIGLA_PROVINCIA AS SGL_PROV_NAS, " +
        "       WAN.CITTA_NASCITA_ESTERO, " +
        "       WAN.RES_COMUNE, " +
        "       COM_RES.DESCOM AS DESCOM_RES, " +
        "       PROV_RES.SIGLA_PROVINCIA AS SGL_PROV_RES, " +
        "       WAN.RES_INDIRIZZO, " +
        "       WAN.RES_CITTA_ESTERO, " +
        "       WAN.RES_CAP, " +
        "       WAN.MAIL_SOGGETTO, " +
        "       WAN.TELEFONO_SOGGETTO, " +
        "       WAN.FAX_SOGGETTO, " +
        "       WAN.SEDELEG_UTE," +
        "       WAN.ID_AZIENDA_SUBENTRO, " +
        "       AA.DENOMINAZIONE AS DENOM_SUBENTRO, " +
        "       AA.CUAA AS CUAA_SUBENTRO, " +
        "       RA.ID_MOTIVO_RICHIESTA " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       COMUNE COM_SEDE, " +
        "       PROVINCIA PROV_SEDE, " +
        "       COMUNE COM_NAS, " +
        "       PROVINCIA PROV_NAS, " +
        "       COMUNE COM_RES, " +
        "       PROVINCIA PROV_RES, " +
        "       DB_ANAGRAFICA_AZIENDA AA " +
        "WHERE  WAN.CODICE_ENTE = ? " +
        "AND    WAN.ID_AZIENDA_NUOVA = RA.ID_AZIENDA_NUOVA " +
        "AND    RA.ID_TIPO_RICHIESTA  IN (")
          .append(getIdListFromArrayForSQL(arrTipoRichiesta))
          .append(") ").append(
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRA.ID_ITER_RICHIESTA_AZIENDA = (SELECT MAX(IRA1.ID_ITER_RICHIESTA_AZIENDA) " +  
        "                                        FROM   SMRGAA_W_AZIENDA_NUOVA WAN1, " +
        "                                               DB_RICHIESTA_AZIENDA RA1, " +
        "                                               DB_ITER_RICHIESTA_AZIENDA IRA1 " +
        "                                        WHERE  WAN1.CODICE_ENTE = WAN.CODICE_ENTE " +
        "                                        AND    WAN1.ID_AZIENDA_NUOVA = RA1.ID_AZIENDA_NUOVA " +
        "                                        AND    RA1.ID_TIPO_RICHIESTA IN (1, 4) " +
        "                                        AND    RA1.ID_RICHIESTA_AZIENDA = IRA1.ID_RICHIESTA_AZIENDA " +
        "                                        AND    IRA1.DATA_FINE_VALIDITA IS NULL) "+
        "AND    WAN.SEDELEG_COMUNE = COM_SEDE.ISTAT_COMUNE (+) " +
        "AND    COM_SEDE.ISTAT_PROVINCIA = PROV_SEDE.ISTAT_PROVINCIA (+) " +
        "AND    WAN.COMUNE_NASCITA = COM_NAS.ISTAT_COMUNE (+) " +
        "AND    COM_NAS.ISTAT_PROVINCIA = PROV_NAS.ISTAT_PROVINCIA (+) " +
        "AND    WAN.RES_COMUNE = COM_RES.ISTAT_COMUNE (+) " +
        "AND    COM_RES.ISTAT_PROVINCIA = PROV_RES.ISTAT_PROVINCIA (+) " +
        "AND    WAN.ID_AZIENDA_SUBENTRO = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " );
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneEnte] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, codEnte);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        aziendaNuovaVO.setIdTipologiaAzienda(new Long(rs.getLong("ID_TIPOLOGIA_AZIENDA")));
        aziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
        aziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        aziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        aziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaNuovaVO.setIdFormaGiuridica(new Long(rs.getLong("ID_FORMA_GIURIDICA")));
        aziendaNuovaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM_SEDE"));
        aziendaNuovaVO.setSedelegProv(rs.getString("SGL_PROV_SEDE"));
        aziendaNuovaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        aziendaNuovaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        aziendaNuovaVO.setSedelegCap(rs.getString("SEDELEG_CAP"));
        aziendaNuovaVO.setMail(rs.getString("MAIL"));
        aziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        aziendaNuovaVO.setFax(rs.getString("FAX"));
        aziendaNuovaVO.setPec(rs.getString("PEC"));
        aziendaNuovaVO.setNote(rs.getString("NOTE"));
        aziendaNuovaVO.setIdStatoRichiesta(checkLongNull(rs.getString("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
        aziendaNuovaVO.setCognome(rs.getString("COGNOME"));
        aziendaNuovaVO.setNome(rs.getString("NOME"));
        aziendaNuovaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        aziendaNuovaVO.setSesso(rs.getString("SESSO"));
        aziendaNuovaVO.setDataNascita(rs.getDate("DATA_NASCITA"));
        aziendaNuovaVO.setComuneNascita(rs.getString("COMUNE_NASCITA"));
        aziendaNuovaVO.setNascitaProv(rs.getString("SGL_PROV_NAS"));
        aziendaNuovaVO.setDescNascitaComune(rs.getString("DESCOM_NAS"));
        aziendaNuovaVO.setCittaNascitaEstero(rs.getString("CITTA_NASCITA_ESTERO"));
        aziendaNuovaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        aziendaNuovaVO.setResComune(rs.getString("RES_COMUNE"));
        aziendaNuovaVO.setDescResComune(rs.getString("DESCOM_RES"));
        aziendaNuovaVO.setResProvincia(rs.getString("SGL_PROV_RES"));
        aziendaNuovaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        aziendaNuovaVO.setResCap(rs.getString("RES_CAP"));
        aziendaNuovaVO.setMailSoggetto(rs.getString("MAIL_SOGGETTO"));
        aziendaNuovaVO.setTelefonoSoggetto(rs.getString("TELEFONO_SOGGETTO"));
        aziendaNuovaVO.setFaxSoggetto(rs.getString("FAX_SOGGETTO"));
        aziendaNuovaVO.setSedeLegUte(rs.getString("SEDELEG_UTE"));
        aziendaNuovaVO.setIdAziendaSubentro(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRO")));
        aziendaNuovaVO.setCuaaSubentro(rs.getString("CUAA_SUBENTRO"));
        aziendaNuovaVO.setDenomSubentro(rs.getString("DENOM_SUBENTRO"));
        aziendaNuovaVO.setIdMotivoRichiesta(checkIntegerNull(rs.getString("ID_MOTIVO_RICHIESTA")));
        
      }
      return aziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codEnte", codEnte) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneEnte] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneEnte] END.");
    }
  }
  
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(
      Long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneByPrimaryKey] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT WAN.ID_AZIENDA_NUOVA, " +
        "       WAN.ID_TIPOLOGIA_AZIENDA, " +
        "       WAN.CUAA, " +
        "       WAN.PARTITA_IVA, " +
        "       WAN.DENOMINAZIONE, " +
        "       WAN.ID_FORMA_GIURIDICA, " +
        "       WAN.SEDELEG_COMUNE, " +
        "       COM_SEDE.DESCOM AS DESCOM_SEDE, " +
        "       PROV_SEDE.SIGLA_PROVINCIA AS SGL_PROV_SEDE, " +
        "       WAN.SEDELEG_INDIRIZZO, " +
        "       WAN.SEDELEG_CITTA_ESTERO, " +
        "       WAN.SEDELEG_CAP, " +
        "       WAN.MAIL, " +
        "       WAN.TELEFONO, " +
        "       WAN.FAX, " +
        "       WAN.PEC, " +
        "       WAN.NOTE, " +
        "       IRA.ID_ITER_RICHIESTA_AZIENDA, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       IRA.DATA_AGGIORNAMENTO AS DATA_ITER, " +
        "       WAN.TIPO_SOGGETTO, " +
        "       WAN.COGNOME, " +
        "       WAN.NOME, " +
        "       WAN.CODICE_FISCALE, " +
        "       WAN.SESSO, " +
        "       WAN.DATA_NASCITA, " +
        "       WAN.COMUNE_NASCITA, " +
        "       COM_NAS.DESCOM AS DESCOM_NAS, " +
        "       PROV_NAS.SIGLA_PROVINCIA AS SGL_PROV_NAS, " +
        "       WAN.CITTA_NASCITA_ESTERO, " +
        "       WAN.RES_COMUNE, " +
        "       COM_RES.DESCOM AS DESCOM_RES, " +
        "       PROV_RES.SIGLA_PROVINCIA AS SGL_PROV_RES, " +
        "       WAN.RES_INDIRIZZO, " +
        "       WAN.RES_CITTA_ESTERO, " +
        "       WAN.RES_CAP, " +
        "       WAN.MAIL_SOGGETTO, " +
        "       WAN.TELEFONO_SOGGETTO, " +
        "       WAN.FAX_SOGGETTO, " +
        "       WAN.SEDELEG_UTE," +
        "       WAN.ID_AZIENDA_SUBENTRO, " +
        "       AA.DENOMINAZIONE AS DENOM_SUBENTRO, " +
        "       AA.CUAA AS CUAA_SUBENTRO, " +
        "       RA.ID_RICHIESTA_AZIENDA, " +
        "       RA.FLAG_DICHIARAZIONE_ALLEGATI, " +
        "       RA.ID_MOTIVO_RICHIESTA, " +
        "       RA.NOTE AS NOTE_RICHIESTA_AZIENDA, " +
        "       TFG.DESCRIZIONE AS DESC_FORMA_GIUR," +
        "       TR.DESCRIZIONE AS DESC_TIPO_RICHIESTA, " +
        "       TR.ID_TIPO_RICHIESTA, " +
        "       TR.DESCRIZIONE_ESTESA AS DESC_EST_TIPO_RICH, " +
        "       TR.NOME_ALLEGATO, " +
        "       MR.DESCRIZIONE AS DESC_MOTIVO_RICHIESTA, " +
        "       MR.NOTE_OBBLIGATORIE, " +
        "       WAN.CODICE_ENTE " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       COMUNE COM_SEDE, " +
        "       PROVINCIA PROV_SEDE, " +
        "       COMUNE COM_NAS, " +
        "       PROVINCIA PROV_NAS, " +
        "       COMUNE COM_RES, " +
        "       PROVINCIA PROV_RES, " +
        "       DB_ANAGRAFICA_AZIENDA AA," +
        "       DB_TIPO_FORMA_GIURIDICA TFG," +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_MOTIVO_RICHIESTA MR " +
        "WHERE  WAN.ID_AZIENDA_NUOVA = ? " +
        "AND    WAN.ID_AZIENDA_NUOVA = RA.ID_AZIENDA_NUOVA " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    RA.ID_MOTIVO_RICHIESTA = MR.ID_MOTIVO_RICHIESTA (+) " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    WAN.SEDELEG_COMUNE = COM_SEDE.ISTAT_COMUNE (+) " +
        "AND    COM_SEDE.ISTAT_PROVINCIA = PROV_SEDE.ISTAT_PROVINCIA (+) " +
        "AND    WAN.COMUNE_NASCITA = COM_NAS.ISTAT_COMUNE (+) " +
        "AND    COM_NAS.ISTAT_PROVINCIA = PROV_NAS.ISTAT_PROVINCIA (+) " +
        "AND    WAN.RES_COMUNE = COM_RES.ISTAT_COMUNE (+) " +
        "AND    COM_RES.ISTAT_PROVINCIA = PROV_RES.ISTAT_PROVINCIA (+) " +
        "AND    WAN.ID_AZIENDA_SUBENTRO = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    WAN.ID_FORMA_GIURIDICA = TFG.ID_FORMA_GIURIDICA (+) " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA (+) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneByPrimaryKey] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova.longValue());
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        aziendaNuovaVO.setIdTipologiaAzienda(new Long(rs.getLong("ID_TIPOLOGIA_AZIENDA")));
        aziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        aziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        aziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaNuovaVO.setIdFormaGiuridica(new Long(rs.getLong("ID_FORMA_GIURIDICA")));
        aziendaNuovaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM_SEDE"));
        aziendaNuovaVO.setSedelegProv(rs.getString("SGL_PROV_SEDE"));
        aziendaNuovaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        aziendaNuovaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        aziendaNuovaVO.setSedelegCap(rs.getString("SEDELEG_CAP"));
        aziendaNuovaVO.setMail(rs.getString("MAIL"));
        aziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        aziendaNuovaVO.setFax(rs.getString("FAX"));
        aziendaNuovaVO.setPec(rs.getString("PEC"));
        aziendaNuovaVO.setNote(rs.getString("NOTE"));
        aziendaNuovaVO.setIdIterRichiestaAzienda(checkLongNull(rs.getString("ID_ITER_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setIdStatoRichiesta(checkLongNull(rs.getString("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setDataAggiornamentoIter(rs.getTimestamp("DATA_ITER"));
        aziendaNuovaVO.setTipoSoggetto(rs.getString("TIPO_SOGGETTO"));
        aziendaNuovaVO.setCognome(rs.getString("COGNOME"));
        aziendaNuovaVO.setNome(rs.getString("NOME"));
        aziendaNuovaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        aziendaNuovaVO.setSesso(rs.getString("SESSO"));
        aziendaNuovaVO.setDataNascita(rs.getDate("DATA_NASCITA"));
        aziendaNuovaVO.setComuneNascita(rs.getString("COMUNE_NASCITA"));
        aziendaNuovaVO.setNascitaProv(rs.getString("SGL_PROV_NAS"));
        aziendaNuovaVO.setDescNascitaComune(rs.getString("DESCOM_NAS"));
        aziendaNuovaVO.setCittaNascitaEstero(rs.getString("CITTA_NASCITA_ESTERO"));
        aziendaNuovaVO.setResIndirizzo(rs.getString("RES_INDIRIZZO"));
        aziendaNuovaVO.setResComune(rs.getString("RES_COMUNE"));
        aziendaNuovaVO.setDescResComune(rs.getString("DESCOM_RES"));
        aziendaNuovaVO.setResProvincia(rs.getString("SGL_PROV_RES"));
        aziendaNuovaVO.setResCittaEstero(rs.getString("RES_CITTA_ESTERO"));
        aziendaNuovaVO.setResCap(rs.getString("RES_CAP"));
        aziendaNuovaVO.setMailSoggetto(rs.getString("MAIL_SOGGETTO"));
        aziendaNuovaVO.setTelefonoSoggetto(rs.getString("TELEFONO_SOGGETTO"));
        aziendaNuovaVO.setFaxSoggetto(rs.getString("FAX_SOGGETTO"));
        aziendaNuovaVO.setSedeLegUte(rs.getString("SEDELEG_UTE"));
        aziendaNuovaVO.setIdAziendaSubentro(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRO")));
        aziendaNuovaVO.setCuaaSubentro(rs.getString("CUAA_SUBENTRO"));
        aziendaNuovaVO.setDenomSubentro(rs.getString("DENOM_SUBENTRO"));
        aziendaNuovaVO.setIdRichiestaAzienda(checkLongNull(rs.getString("ID_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setFlagDichiarazioneAllegati(rs.getString("FLAG_DICHIARAZIONE_ALLEGATI"));
        aziendaNuovaVO.setDescFormaGiur(rs.getString("DESC_FORMA_GIUR"));
        aziendaNuovaVO.setDescTipoRichiesta(rs.getString("DESC_TIPO_RICHIESTA"));
        aziendaNuovaVO.setIdTipoRichiesta(new Long(rs.getLong("ID_TIPO_RICHIESTA")));
        aziendaNuovaVO.setDescEstesaTipoRichiesta(rs.getString("DESC_EST_TIPO_RICH"));
        aziendaNuovaVO.setNomAllegato(rs.getString("NOME_ALLEGATO"));
        aziendaNuovaVO.setIdMotivoRichiesta(checkIntegerNull(rs.getString("ID_MOTIVO_RICHIESTA")));
        aziendaNuovaVO.setDescMotivoRichiesta(rs.getString("DESC_MOTIVO_RICHIESTA"));
        aziendaNuovaVO.setFlagNoteObbligatorie(rs.getString("NOTE_OBBLIGATORIE"));
        aziendaNuovaVO.setNoteRichiestaAzienda(rs.getString("NOTE_RICHIESTA_AZIENDA"));
        aziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
      }
      return aziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneByPrimaryKey] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendaNuovaIscrizioneByPrimaryKey] END.");
    }
  }
  
  
  public Long insertAziendaNuova(AziendaNuovaVO aziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_AZIENDA_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_AZIENDA_NUOVA   " +
        "      (ID_AZIENDA_NUOVA, " +
        "       ID_TIPOLOGIA_AZIENDA, " +
        "       CUAA, " +
        "       PARTITA_IVA, " +
        "       DENOMINAZIONE, " +
        "       ID_FORMA_GIURIDICA, " +
        "       SEDELEG_COMUNE, " +
        "       SEDELEG_INDIRIZZO, " +
        "       SEDELEG_CITTA_ESTERO, " +
        "       SEDELEG_CAP, " +
        "       MAIL, " +
        "       TELEFONO, " +
        "       FAX, " +
        "       PEC, " +
        "       ID_AZIENDA_SUBENTRO, " +
        "       NOTE, " +
        "       TIPO_SOGGETTO, " +
        "       COGNOME, " +
        "       NOME, " +
        "       CODICE_FISCALE, " +
        "       SESSO, " +
        "       DATA_NASCITA, " +
        "       COMUNE_NASCITA, " +
        "       CITTA_NASCITA_ESTERO, " +
        "       RES_COMUNE, " +
        "       RES_INDIRIZZO, " +
        "       RES_CITTA_ESTERO, " +
        "       RES_CAP, " +
        "       MAIL_SOGGETTO, " +
        "       TELEFONO_SOGGETTO, " +
        "       FAX_SOGGETTO, " +
        "       SEDELEG_UTE," +
        "       CODICE_ENTE) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(aziendaNuovaVO.getIdTipologiaAzienda()));
      stmt.setString(++indice, aziendaNuovaVO.getCuaa());
      stmt.setString(++indice, aziendaNuovaVO.getPartitaIva());
      stmt.setString(++indice, aziendaNuovaVO.getDenominazione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(aziendaNuovaVO.getIdFormaGiuridica()));
      stmt.setString(++indice, aziendaNuovaVO.getSedelegComune());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegIndirizzo());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegCittaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegCap());
      stmt.setString(++indice, aziendaNuovaVO.getMail());
      stmt.setString(++indice, aziendaNuovaVO.getTelefono());
      stmt.setString(++indice, aziendaNuovaVO.getFax());
      stmt.setString(++indice, aziendaNuovaVO.getPec());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(aziendaNuovaVO.getIdAziendaSubentro()));
      stmt.setString(++indice, aziendaNuovaVO.getNote());
      stmt.setString(++indice, aziendaNuovaVO.getTipoSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getCognome());
      stmt.setString(++indice, aziendaNuovaVO.getNome());
      stmt.setString(++indice, aziendaNuovaVO.getCodiceFiscale());
      stmt.setString(++indice, aziendaNuovaVO.getSesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaNuovaVO.getDataNascita()));
      stmt.setString(++indice, aziendaNuovaVO.getComuneNascita());
      stmt.setString(++indice, aziendaNuovaVO.getCittaNascitaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getResComune());
      stmt.setString(++indice, aziendaNuovaVO.getResIndirizzo());
      stmt.setString(++indice, aziendaNuovaVO.getResCittaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getResCap());
      stmt.setString(++indice, aziendaNuovaVO.getMailSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getTelefonoSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getFaxSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getSedeLegUte());
      stmt.setString(++indice, aziendaNuovaVO.getCodEnte());
      
      stmt.executeUpdate();
      
      return idAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAziendaNuova", idAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("aziendaNuovaVO", aziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAziendaNuova] END.");
    }
  }
  
  public void updateAziendaNuova(AziendaNuovaVO aziendaNuovaVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE SMRGAA_W_AZIENDA_NUOVA   " +
        "   SET ID_TIPOLOGIA_AZIENDA = ? , " +
        "       CUAA = ? , " +
        "       PARTITA_IVA = ?, " +
        "       DENOMINAZIONE = ?, " +
        "       ID_FORMA_GIURIDICA = ?, " +
        "       SEDELEG_COMUNE = ?, " +
        "       SEDELEG_INDIRIZZO = ?, " +
        "       SEDELEG_CITTA_ESTERO = ?, " +
        "       SEDELEG_CAP = ?, " +
        "       MAIL = ?, " +
        "       TELEFONO = ?, " +
        "       FAX = ?, " +
        "       PEC = ?, " +
        "       ID_AZIENDA_SUBENTRO = ?, " +
        "       NOTE = ?, " +
        "       TIPO_SOGGETTO = ?, " +
        "       COGNOME = ?, " +
        "       NOME = ?, " +
        "       CODICE_FISCALE = ?, " +
        "       SESSO = ?, " +
        "       DATA_NASCITA = ?, " +
        "       COMUNE_NASCITA = ?, " +
        "       CITTA_NASCITA_ESTERO = ?, " +
        "       RES_COMUNE = ?, " +
        "       RES_INDIRIZZO = ?, " +
        "       RES_CITTA_ESTERO = ?, " +
        "       RES_CAP = ?, " +
        "       MAIL_SOGGETTO = ?, " +
        "       TELEFONO_SOGGETTO = ?, " +
        "       FAX_SOGGETTO = ?, " +
        "       SEDELEG_UTE = ?, " +
        "       CODICE_ENTE = ? " +
        "   WHERE" +
        "       ID_AZIENDA_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, new Long(aziendaNuovaVO.getIdTipologiaAzienda()));
      stmt.setString(++indice, aziendaNuovaVO.getCuaa());
      stmt.setString(++indice, aziendaNuovaVO.getPartitaIva());
      stmt.setString(++indice, aziendaNuovaVO.getDenominazione());
      stmt.setLong(++indice, new Long(aziendaNuovaVO.getIdFormaGiuridica()));
      stmt.setString(++indice, aziendaNuovaVO.getSedelegComune());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegIndirizzo());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegCittaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getSedelegCap());
      stmt.setString(++indice, aziendaNuovaVO.getMail());
      stmt.setString(++indice, aziendaNuovaVO.getTelefono());
      stmt.setString(++indice, aziendaNuovaVO.getFax());
      stmt.setString(++indice, aziendaNuovaVO.getPec());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(aziendaNuovaVO.getIdAziendaSubentro()));
      stmt.setString(++indice, aziendaNuovaVO.getNote());
      stmt.setString(++indice, aziendaNuovaVO.getTipoSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getCognome());
      stmt.setString(++indice, aziendaNuovaVO.getNome());
      stmt.setString(++indice, aziendaNuovaVO.getCodiceFiscale());
      stmt.setString(++indice, aziendaNuovaVO.getSesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(aziendaNuovaVO.getDataNascita()));
      stmt.setString(++indice, aziendaNuovaVO.getComuneNascita());
      stmt.setString(++indice, aziendaNuovaVO.getCittaNascitaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getResComune());
      stmt.setString(++indice, aziendaNuovaVO.getResIndirizzo());
      stmt.setString(++indice, aziendaNuovaVO.getResCittaEstero());
      stmt.setString(++indice, aziendaNuovaVO.getResCap());
      stmt.setString(++indice, aziendaNuovaVO.getMailSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getTelefonoSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getFaxSoggetto());
      stmt.setString(++indice, aziendaNuovaVO.getSedeLegUte());
      stmt.setString(++indice, aziendaNuovaVO.getCodEnte());
      
      stmt.setLong(++indice, aziendaNuovaVO.getIdAziendaNuova().longValue());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("aziendaNuovaVO", aziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateAziendaNuova] END.");
    }
  }
  
  
  public Long insertRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idRichiestaAzienda = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertRichiestaAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idRichiestaAzienda = getNextPrimaryKey(SolmrConstants.SEQ_DB_RICHIESTA_AZIENDA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO DB_RICHIESTA_AZIENDA   " +
        "      (ID_RICHIESTA_AZIENDA, " +
        "       ID_TIPO_RICHIESTA, " +
        "       ID_AZIENDA_NUOVA, " +
        "       ID_AZIENDA, " +
        "       CODICE_ENTE, " +
        "       DATA_AGGIORNAMENTO, " +
        "       ID_UTENTE_AGGIORNAMENTO, " +
        "       NOTE," +
        "       FILE_STAMPA," +
        "       ID_MOTIVO_RICHIESTA," +
        "       ID_MOTIVO_DICHIARAZIONE," +
        "       ID_CESSAZIONE," +
        "       DATA_CESSAZIONE," +
        "       ID_AZIENDA_SUBENTRANTE," +
        "       CUAA_SUBENTRANTE," +
        "       DENOMINAZIONE_SUBENTRANTE," +
        "       FLAG_SOLO_AGGIUNTA) " +
        "   VALUES(?,?,?,?,?,?,?,?,empty_blob(),?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertRichiestaAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda.longValue());
      stmt.setLong(++indice, new Long(richiestaAziendaVO.getIdTipoRichiesta()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdAziendaNuova()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdAzienda()));
      stmt.setString(++indice, richiestaAziendaVO.getCodiceEnte());
      stmt.setTimestamp(++indice, convertDateToTimestamp(richiestaAziendaVO.getDataAggiornamento()));
      stmt.setLong(++indice, new Long(richiestaAziendaVO.getIdUtenteAggiornamento()));
      stmt.setString(++indice, richiestaAziendaVO.getNote());
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(richiestaAziendaVO.getIdMotivoRichiesta()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(richiestaAziendaVO.getIdMotivoDichiarazione()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdCessazione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(richiestaAziendaVO.getDataCessazione()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdAziendaSubentrante()));
      stmt.setString(++indice, richiestaAziendaVO.getCuaaSubentrante());
      stmt.setString(++indice, richiestaAziendaVO.getDenominazioneSubentrante());
      String flagSoloAggiunta = "N";
      if(Validator.isNotEmpty(richiestaAziendaVO.getFlagSoloAggiunta()))
        flagSoloAggiunta = richiestaAziendaVO.getFlagSoloAggiunta();
      stmt.setString(++indice, flagSoloAggiunta);
      
      stmt.executeUpdate();
      
      return idRichiestaAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idRichiestaAzienda", idRichiestaAzienda)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("richiestaAziendaVO", richiestaAziendaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertRichiestaAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertRichiestaAzienda] END.");
    }
  }
  
  public Long insertIterRichiestaAzienda(IterRichiestaAziendaVO iterRichiestaAziendaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idIterRichiestaAzienda = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertIterRichiestaAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idIterRichiestaAzienda = getNextPrimaryKey(SolmrConstants.SEQ_DB_ITER_RICHIESTA_AZIENDA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO DB_ITER_RICHIESTA_AZIENDA   " +
        "      (ID_ITER_RICHIESTA_AZIENDA, " +
        "       ID_RICHIESTA_AZIENDA, " +
        "       ID_STATO_RICHIESTA, " +
        "       DATA_INIZIO_VALIDITA, " +
        "       DATA_FINE_VALIDITA, " +
        "       DATA_AGGIORNAMENTO, " +
        "       ID_UTENTE_AGGIORNAMENTO, " +
        "       NOTE, " +
        "       FLAG_MAIL_NOTIFICA) " +
        "   VALUES(?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertIterRichiestaAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIterRichiestaAzienda.longValue());
      stmt.setLong(++indice, new Long(iterRichiestaAziendaVO.getIdRichiestaAzienda()));
      stmt.setLong(++indice, new Long(iterRichiestaAziendaVO.getIdStatoRichiesta()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(iterRichiestaAziendaVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(iterRichiestaAziendaVO.getDataFineValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(iterRichiestaAziendaVO.getDataAggiornamento()));
      stmt.setLong(++indice, new Long(iterRichiestaAziendaVO.getIdUtenteAggiornamento()));
      stmt.setString(++indice, iterRichiestaAziendaVO.getNote());
      stmt.setString(++indice, iterRichiestaAziendaVO.getFlagMailNotifica());
      
      stmt.executeUpdate();
      
      return idIterRichiestaAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idIterRichiestaAzienda", idIterRichiestaAzienda)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("iterRichiestaAziendaVO", iterRichiestaAziendaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertIterRichiestaAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertIterRichiestaAzienda] END.");
    }
  }
  
  public void storicizzaIterRichiestaAzienda(long idIterRichiestaAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::storicizzaIterRichiestaAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE DB_ITER_RICHIESTA_AZIENDA   " +
        "   SET DATA_FINE_VALIDITA = SYSDATE " +
        "   WHERE ID_ITER_RICHIESTA_AZIENDA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::storicizzaIterRichiestaAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idIterRichiestaAzienda);
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idIterRichiestaAzienda", idIterRichiestaAzienda)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("idIterRichiestaAzienda", idIterRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::storicizzaIterRichiestaAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::storicizzaIterRichiestaAzienda] END.");
    }
  }
  
  
  public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UteAziendaNuovaVO> vUteAziendaNuova = null;
    UteAziendaNuovaVO uteAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UAN.ID_UTE_AZIENDA_NUOVA, " +
        "       UAN.ID_AZIENDA_NUOVA, " +
        "       UAN.DENOMINAZIONE, " +
        "       UAN.INDIRIZZO, " +
        "       UAN.COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       PROV.ISTAT_PROVINCIA, " +
        "       UAN.CAP,  " +
        "       UAN.TELEFONO, " +
        "       UAN.FAX, " +
        "       UAN.NOTE " +
        "FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM " +
        "WHERE  UAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    UAN.COMUNE = COM.ISTAT_COMUNE (+) " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA (+) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
        if(vUteAziendaNuova == null)
        {
          vUteAziendaNuova = new Vector<UteAziendaNuovaVO>();
        }
        uteAziendaNuovaVO = new UteAziendaNuovaVO();
        uteAziendaNuovaVO.setIdUteAziendaNuova(new Long(rs.getLong("ID_UTE_AZIENDA_NUOVA")));
        uteAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        uteAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        uteAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        uteAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        uteAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        uteAziendaNuovaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        uteAziendaNuovaVO.setSglProvincia(rs.getString("SIGLA_PROVINCIA"));
        uteAziendaNuovaVO.setCap(rs.getString("CAP"));
        uteAziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        uteAziendaNuovaVO.setFax(rs.getString("FAX"));
        uteAziendaNuovaVO.setNote(rs.getString("NOTE"));
        
        
        vUteAziendaNuova.add(uteAziendaNuovaVO);
        
      }
      return vUteAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("uteAziendaNuovaVO", uteAziendaNuovaVO),
        new Variabile("vUteAziendaNuova", vUteAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaIscrizione] END.");
    }
  }
  
  public UteAziendaNuovaVO getUteAziendaNuovaByPrimariKey(
      long idUteAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    UteAziendaNuovaVO uteAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaByPrimariKey] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UAN.ID_UTE_AZIENDA_NUOVA, " +
        "       UAN.ID_AZIENDA_NUOVA, " +
        "       UAN.DENOMINAZIONE, " +
        "       UAN.INDIRIZZO, " +
        "       UAN.COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       PROV.ISTAT_PROVINCIA, " +
        "       UAN.CAP,  " +
        "       UAN.TELEFONO, " +
        "       UAN.FAX, " +
        "       UAN.NOTE " +
        "FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM " +
        "WHERE  UAN.ID_UTE_AZIENDA_NUOVA =  ? " +
        "AND    UAN.COMUNE = COM.ISTAT_COMUNE (+) " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA (+) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaByPrimariKey] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUteAziendaNuova);
      ResultSet rs = stmt.executeQuery();
          
      if(rs.next())
      {
        uteAziendaNuovaVO = new UteAziendaNuovaVO();
        uteAziendaNuovaVO.setIdUteAziendaNuova(new Long(rs.getLong("ID_UTE_AZIENDA_NUOVA")));
        uteAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        uteAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        uteAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        uteAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        uteAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        uteAziendaNuovaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        uteAziendaNuovaVO.setSglProvincia(rs.getString("SIGLA_PROVINCIA"));
        uteAziendaNuovaVO.setCap(rs.getString("CAP"));
        uteAziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        uteAziendaNuovaVO.setFax(rs.getString("FAX"));
        uteAziendaNuovaVO.setNote(rs.getString("NOTE"));
        
        
      }
      return uteAziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("uteAziendaNuovaVO", uteAziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUteAziendaNuova", idUteAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaByPrimariKey] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getUteAziendaNuovaByPrimariKey] END.");
    }
  }
  
  public Long insertUteAziendaNuova(UteAziendaNuovaVO uteAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idUteAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertUteAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idUteAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_UTE_AZIENDA_NUOVA   " +
        "      (ID_UTE_AZIENDA_NUOVA, " +
        "       ID_AZIENDA_NUOVA, " +
        "       DENOMINAZIONE, " +
        "       INDIRIZZO, " +
        "       COMUNE, " +
        "       CAP, " +
        "       TELEFONO, " +
        "       FAX, " +
        "       NOTE ) " +
        "   VALUES(?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertUteAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUteAziendaNuova.longValue());
      stmt.setLong(++indice, uteAziendaNuovaVO.getIdAziendaNuova().longValue());
      stmt.setString(++indice, uteAziendaNuovaVO.getDenominazione());
      stmt.setString(++indice, uteAziendaNuovaVO.getIndirizzo());
      stmt.setString(++indice, uteAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, uteAziendaNuovaVO.getCap());
      stmt.setString(++indice, uteAziendaNuovaVO.getTelefono());
      stmt.setString(++indice, uteAziendaNuovaVO.getFax());
      stmt.setString(++indice, uteAziendaNuovaVO.getNote());
      
      stmt.executeUpdate();
      
      return idUteAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idUteAziendaNuova", idUteAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("uteAziendaNuovaVO", uteAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertUteAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertUteAziendaNuova] END.");
    }
  }
  
  
  public void updateUteAziendaNuova(UteAziendaNuovaVO uteAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateUteAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE SMRGAA_W_UTE_AZIENDA_NUOVA   " +
        "   SET ID_AZIENDA_NUOVA = ?, " +
        "       DENOMINAZIONE = ? , " +
        "       INDIRIZZO = ? , " +
        "       COMUNE = ? , " +
        "       CAP = ? , " +
        "       TELEFONO = ?, " +
        "       FAX = ? , " +
        "       NOTE = ? " +
        "   WHERE ID_UTE_AZIENDA_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateUteAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, uteAziendaNuovaVO.getIdAziendaNuova().longValue());
      stmt.setString(++indice, uteAziendaNuovaVO.getDenominazione());
      stmt.setString(++indice, uteAziendaNuovaVO.getIndirizzo());
      stmt.setString(++indice, uteAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, uteAziendaNuovaVO.getCap());
      stmt.setString(++indice, uteAziendaNuovaVO.getTelefono());
      stmt.setString(++indice, uteAziendaNuovaVO.getFax());
      stmt.setString(++indice, uteAziendaNuovaVO.getNote());
      
      stmt.setLong(++indice, uteAziendaNuovaVO.getIdUteAziendaNuova().longValue());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("uteAziendaNuovaVO", uteAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateUteAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateUteAziendaNuova] END.");
    }
  }
  
  
  public void deleteUteAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteUteAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(
        "DELETE SMRGAA_W_UTE_AZIENDA_NUOVA UAN " +
        "WHERE UAN.ID_UTE_AZIENDA_NUOVA  NOT IN (SELECT UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        FROM   SMRGAA_W_FABBRICATO_AZ_NUOVA FAN, " +
        "                                               SMRGAA_W_UTE_AZIENDA_NUOVA UAN1 " +
        "                                        WHERE  FAN.ID_UTE_AZIENDA_NUOVA = UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        AND    UAN1.ID_AZIENDA_NUOVA = UAN.ID_AZIENDA_NUOVA" +
        "                                        UNION " +
        "                                        SELECT UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        FROM   SMRGAA_W_UTILIZZO_AZ_NUOVA TAN, " +
        "                                               SMRGAA_W_UTE_AZIENDA_NUOVA UAN1 " +
        "                                        WHERE  TAN.ID_UTE_AZIENDA_NUOVA = UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        AND    UAN1.ID_AZIENDA_NUOVA = UAN.ID_AZIENDA_NUOVA " +
        "                                        UNION " +
        "                                        SELECT UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        FROM   SMRGAA_W_ALLEVAMENTO_AZ_NUOVA AAN, " +
        "                                               SMRGAA_W_UTE_AZIENDA_NUOVA UAN1 " +
        "                                        WHERE  AAN.ID_UTE_AZIENDA_NUOVA = UAN1.ID_UTE_AZIENDA_NUOVA " +
        "                                        AND    UAN1.ID_AZIENDA_NUOVA = UAN.ID_AZIENDA_NUOVA )" +
        "AND   UAN.ID_AZIENDA_NUOVA = ? " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteUteAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUteAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteUteAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteUteAziendaNuova] END.");
    }
  }
  
  
  public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova = null;
    FabbricatoAziendaNuovaVO fabbricatoAziendaNuovaVO = null;
    PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getFabbrAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UAN.ID_UTE_AZIENDA_NUOVA, " +
        "       UAN.DENOMINAZIONE, " +
        "       FAN.ID_FABBRICATO_AZ_NUOVA, " +
        "       FAN.ID_TIPOLOGIA_FABBRICATO, " +
        "       TTF.DESCRIZIONE AS DESC_FABBR, " +
        "       TTF.UNITA_MISURA, " +
        "       FAN.LUNGHEZZA, " +
        "       FAN.LARGHEZZA, " +
        "       FAN.ALTEZZA, " +
        "       FAN.ANNO_COSTRUZIONE, " +
        "       FAN.DIMENSIONE, " +
        "       FAN.SUPERFICIE AS SUP_FABB, " +
        "       FAN.SUPERFICIE_COPERTA, " +
        "       FAN.SUPERFICIE_SCOPERTA, " +
        "       PAN.COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       PAN.SEZIONE, " +
        "       PAN.FOGLIO, " +
        "       PAN.PARTICELLA, " +
        "       PAN.SUBALTERNO, " +
        "       PAN.SUPERFICIE AS SUP_PART, " +
        "       PAN.ID_UTILIZZO, " +
        "       TP.CODICE AS COD_UTILIZZO, " +
        "       TP.DESCRIZIONE AS DESC_UTILIZZO, " +
        "       PAN.ID_VARIETA, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       PAN.ID_TITOLO_POSSESSO, " +
        "       TTP.DESCRIZIONE AS DESC_TITOLO_POSSESSO " +        
        "FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN, " +
        "       SMRGAA_W_FABBRICATO_AZ_NUOVA FAN," +
        "       SMRGAA_W_PART_FABBR_AZ_NUOVA PAN, " +
        "       DB_TIPO_TIPOLOGIA_FABBRICATO TTF, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM, " +
        "       DB_TIPO_UTILIZZO TP, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_TITOLO_POSSESSO TTP " +
        "WHERE  UAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    UAN.ID_UTE_AZIENDA_NUOVA = FAN.ID_UTE_AZIENDA_NUOVA " +
        "AND    FAN.ID_FABBRICATO_AZ_NUOVA = PAN.ID_FABBRICATO_AZ_NUOVA " +
        "AND    FAN.ID_TIPOLOGIA_FABBRICATO = TTF.ID_TIPOLOGIA_FABBRICATO " +
        "AND    PAN.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND    PAN.ID_UTILIZZO = TP.ID_UTILIZZO " +
        "AND    PAN.ID_VARIETA = TV.ID_VARIETA " +
        "AND    PAN.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "ORDER BY UAN.DENOMINAZIONE, " +
        "         FAN.ID_FABBRICATO_AZ_NUOVA, " +
        "         COM.DESCOM, " +
        "         PAN.FOGLIO, " +
        "         PAN.PARTICELLA ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getFabbrAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      Long idFabbrAzNuovaTmp = new Long(0);
      while (rs.next())
      {
        if(vFabbrAziendaNuova == null)
        {
          vFabbrAziendaNuova = new Vector<FabbricatoAziendaNuovaVO>();
        }
        Long idFabbrAzNuova = new Long(rs.getLong("ID_FABBRICATO_AZ_NUOVA"));
        
        if(idFabbrAzNuova.compareTo(idFabbrAzNuovaTmp) != 0)
        {
          if(idFabbrAzNuovaTmp.compareTo(new Long(0)) != 0)
          {
            vFabbrAziendaNuova.add(fabbricatoAziendaNuovaVO);
          }          
          fabbricatoAziendaNuovaVO = new FabbricatoAziendaNuovaVO();
          fabbricatoAziendaNuovaVO.setIdUteAziendaNuova(rs.getLong("ID_UTE_AZIENDA_NUOVA"));
          fabbricatoAziendaNuovaVO.setIdTipologiaFabbricato(rs.getLong("ID_TIPOLOGIA_FABBRICATO"));
          fabbricatoAziendaNuovaVO.setIdFabbricatoAzNuova(idFabbrAzNuova);
          fabbricatoAziendaNuovaVO.setDenominazUte(rs.getString("DENOMINAZIONE"));
          fabbricatoAziendaNuovaVO.setDescFabbricato(rs.getString("DESC_FABBR"));
          fabbricatoAziendaNuovaVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
          fabbricatoAziendaNuovaVO.setLunghezza(rs.getBigDecimal("LUNGHEZZA"));
          fabbricatoAziendaNuovaVO.setLarghezza(rs.getBigDecimal("LARGHEZZA"));
          fabbricatoAziendaNuovaVO.setAltezza(rs.getBigDecimal("ALTEZZA"));
          fabbricatoAziendaNuovaVO.setAnnoCostruzione(checkIntegerNull(rs.getString("ANNO_COSTRUZIONE")));
          fabbricatoAziendaNuovaVO.setDimensione(rs.getBigDecimal("DIMENSIONE"));
          fabbricatoAziendaNuovaVO.setSuperficie(rs.getBigDecimal("SUP_FABB"));
          fabbricatoAziendaNuovaVO.setSuperficieCoperta(rs.getBigDecimal("SUPERFICIE_COPERTA"));
          fabbricatoAziendaNuovaVO.setSuperficieScoperta(rs.getBigDecimal("SUPERFICIE_SCOPERTA"));
        }
        
        Vector<PartFabbrAziendaNuovaVO> vPartFabbrAziendaNuova = null;
        if(fabbricatoAziendaNuovaVO.getvPartFabbrAziendaNuova() != null)
        {
          vPartFabbrAziendaNuova = fabbricatoAziendaNuovaVO.getvPartFabbrAziendaNuova();
        }
        else
        {
          vPartFabbrAziendaNuova = new Vector<PartFabbrAziendaNuovaVO>();
        }
        
        partFabbrAziendaNuovaVO = new PartFabbrAziendaNuovaVO();
        partFabbrAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        partFabbrAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        partFabbrAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        partFabbrAziendaNuovaVO.setSezione(rs.getString("SEZIONE"));
        partFabbrAziendaNuovaVO.setStrFoglio(rs.getString("FOGLIO"));
        partFabbrAziendaNuovaVO.setFoglio(checkLongNull(partFabbrAziendaNuovaVO.getStrFoglio()));
        partFabbrAziendaNuovaVO.setStrParticella(rs.getString("PARTICELLA"));
        partFabbrAziendaNuovaVO.setParticella(checkLongNull(partFabbrAziendaNuovaVO.getStrParticella()));
        partFabbrAziendaNuovaVO.setSubalterno(rs.getString("SUBALTERNO"));
        partFabbrAziendaNuovaVO.setSuperficie(rs.getBigDecimal("SUP_PART"));
        partFabbrAziendaNuovaVO.setIdUtilizzo(checkLongNull(rs.getString("ID_UTILIZZO")));
        partFabbrAziendaNuovaVO.setDescTipoUtilizzo("["+rs.getString("COD_UTILIZZO")+"] "+rs.getString("DESC_UTILIZZO"));
        partFabbrAziendaNuovaVO.setIdVarieta(checkLongNull(rs.getString("ID_VARIETA")));
        partFabbrAziendaNuovaVO.setDescTipoVarieta("["+rs.getString("CODICE_VARIETA")+"] "+rs.getString("DESC_VARIETA"));
        partFabbrAziendaNuovaVO.setIdTitoloPossesso(checkIntegerNull(rs.getString("ID_TITOLO_POSSESSO")));
        partFabbrAziendaNuovaVO.setDescTitoloPossesso(rs.getString("DESC_TITOLO_POSSESSO"));
        
        vPartFabbrAziendaNuova.add(partFabbrAziendaNuovaVO);
        fabbricatoAziendaNuovaVO.setvPartFabbrAziendaNuova(vPartFabbrAziendaNuova);
        
        idFabbrAzNuovaTmp = idFabbrAzNuova;
      }
      
      if(vFabbrAziendaNuova != null)
      {
        vFabbrAziendaNuova.add(fabbricatoAziendaNuovaVO);
      }
      
      return vFabbrAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("partFabbrAziendaNuovaVO", partFabbrAziendaNuovaVO),
        new Variabile("fabbricatoAziendaNuovaVO", fabbricatoAziendaNuovaVO),
        new Variabile("vFabbrAziendaNuova", vFabbrAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getFabbrAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getFabbrAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public Long insertFabbrAziendaNuova(FabbricatoAziendaNuovaVO fabbrAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idFabbrAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertFabbrAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idFabbrAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_FABBR_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_FABBRICATO_AZ_NUOVA   " +
        "      (ID_FABBRICATO_AZ_NUOVA, " +
        "       ID_UTE_AZIENDA_NUOVA, " +
        "       ID_TIPOLOGIA_FABBRICATO, " +
        "       LUNGHEZZA, " +
        "       LARGHEZZA, " +
        "       ANNO_COSTRUZIONE, " +
        "       DIMENSIONE, " +
        "       SUPERFICIE, " +
        "       SUPERFICIE_COPERTA, " +
        "       SUPERFICIE_SCOPERTA, " +
        "       ALTEZZA ) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertFabbrAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idFabbrAziendaNuova.longValue());
      stmt.setLong(++indice, fabbrAziendaNuovaVO.getIdUteAziendaNuova());
      stmt.setLong(++indice, fabbrAziendaNuovaVO.getIdTipologiaFabbricato());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getLunghezza());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getLarghezza());
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(fabbrAziendaNuovaVO.getAnnoCostruzione()));
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getDimensione());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getSuperficie());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getSuperficieCoperta());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getSuperficieScoperta());
      stmt.setBigDecimal(++indice, fabbrAziendaNuovaVO.getAltezza());
      
      stmt.executeUpdate();
      
      return idFabbrAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idFabbrAziendaNuova", idFabbrAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("fabbrAziendaNuovaVO", fabbrAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertFabbrAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertFabbrAziendaNuova] END.");
    }
  }
  
  public Long insertPartFabbrAziendaNuova(PartFabbrAziendaNuovaVO partFabbrAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idPartFabbrAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertPartFabbrAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idPartFabbrAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_PART_FABBR_AZ_NUOVA   " +
        "      (ID_PART_FABBR_AZ_NUOVA, " +
        "       ID_FABBRICATO_AZ_NUOVA, " +
        "       COMUNE, " +
        "       SEZIONE, " +
        "       FOGLIO, " +
        "       PARTICELLA, " +
        "       SUBALTERNO, " +
        "       SUPERFICIE, " +
        "       ID_UTILIZZO, " +
        "       ID_VARIETA, " +
        "       ID_TITOLO_POSSESSO ) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertPartFabbrAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idPartFabbrAziendaNuova.longValue());
      stmt.setLong(++indice, partFabbrAziendaNuovaVO.getIdFabbricatoAziendaNuova());
      stmt.setString(++indice, partFabbrAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, partFabbrAziendaNuovaVO.getSezione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partFabbrAziendaNuovaVO.getFoglio()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partFabbrAziendaNuovaVO.getParticella()));
      stmt.setString(++indice, partFabbrAziendaNuovaVO.getSubalterno());
      stmt.setBigDecimal(++indice, partFabbrAziendaNuovaVO.getSuperficie());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partFabbrAziendaNuovaVO.getIdUtilizzo()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(partFabbrAziendaNuovaVO.getIdVarieta()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(partFabbrAziendaNuovaVO.getIdTitoloPossesso()));
      
      stmt.executeUpdate();
      
      return idPartFabbrAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idPartFabbrAziendaNuova", idPartFabbrAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("partFabbrAziendaNuovaVO", partFabbrAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertPartFabbrAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertPartFabbrAziendaNuova] END.");
    }
  }
  
  public void deletePartFabbrAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deletePartFabbrAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_PART_FABBR_AZ_NUOVA " +
        "WHERE  ID_FABBRICATO_AZ_NUOVA IN (  " +
        "       SELECT FAN.ID_FABBRICATO_AZ_NUOVA " +
        "       FROM   SMRGAA_W_FABBRICATO_AZ_NUOVA FAN, " +
        "              SMRGAA_W_UTE_AZIENDA_NUOVA UAN " +
        "       WHERE  UAN.ID_AZIENDA_NUOVA = ? " +
        "       AND    UAN.ID_UTE_AZIENDA_NUOVA = FAN.ID_UTE_AZIENDA_NUOVA) " );
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deletePartFabbrAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deletePartFabbrAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deletePartFabbrAziendaNuova] END.");
    }
  }
  
  
  public void deleteFabbrAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteFabbrAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_FABBRICATO_AZ_NUOVA " +
        "WHERE  ID_UTE_AZIENDA_NUOVA IN (  " +
        "       SELECT UAN.ID_UTE_AZIENDA_NUOVA " +
        "       FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN " +
        "       WHERE  UAN.ID_AZIENDA_NUOVA = ? ) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteFabbrAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteFabbrAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteFabbrAziendaNuova] END.");
    }
  }
  
  /**
   * 
   * Mi ritorna tru se quell'ute a delle dipendenze su altre tabelle
   * 
   * 
   * @param idUteAziendaNuova
   * @return
   * @throws DataAccessException
   */
  public boolean existsDependenciesUte(
      long idUteAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovato = false;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::existsDependenciesUte] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT FAN.ID_UTE_AZIENDA_NUOVA " +
        "FROM   SMRGAA_W_FABBRICATO_AZ_NUOVA FAN " +
        "WHERE  FAN.ID_UTE_AZIENDA_NUOVA =  ? " +
        "UNION " +
        "SELECT TAN.ID_UTE_AZIENDA_NUOVA " +
        "FROM   SMRGAA_W_UTILIZZO_AZ_NUOVA TAN " +
        "WHERE  TAN.ID_UTE_AZIENDA_NUOVA =  ? " +
        "UNION " +
        "SELECT AAN.ID_UTE_AZIENDA_NUOVA " +
        "FROM   SMRGAA_W_ALLEVAMENTO_AZ_NUOVA AAN " +
        "WHERE  AAN.ID_UTE_AZIENDA_NUOVA =  ? ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::existsDependenciesUte] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idUteAziendaNuova);
      stmt.setLong(++indice, idUteAziendaNuova);
      stmt.setLong(++indice, idUteAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        trovato = true;
      }
      
      return trovato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("trovato", trovato) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUteAziendaNuova", idUteAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::existsDependenciesUte] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::existsDependenciesUte] END.");
    }
  }
  
  
  public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova = null;
    ParticellaAziendaNuovaVO particellaAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getParticelleAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TAN.ID_UTILIZZO_AZ_NUOVA, " +
        "       UAN.ID_UTE_AZIENDA_NUOVA, " +
        "       UAN.DENOMINAZIONE, " +
        "       TAN.COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.ISTAT_PROVINCIA, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       TAN.SEZIONE, " +
        "       TAN.FOGLIO, " +
        "       TAN.PARTICELLA, " +
        "       TAN.SUPERFICIE, " +
        "       TAN.PERCENTUALE_CONDUZIONE, " +
        "       TAN.ID_UTILIZZO, " +
        "       TIU.ID_INDIRIZZO_UTILIZZO, " +
        "       TIU.DESCRIZIONE AS DESC_INDIRIZZO_UTILIZZO, " +
        "       TP.CODICE AS COD_UTILIZZO, " +
        "       TP.DESCRIZIONE AS DESC_UTILIZZO, " +
        "       TAN.ID_VARIETA, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       TAN.ID_TITOLO_POSSESSO, " +
        "       TTP.DESCRIZIONE AS DESC_TITOLO_POSSESSO, " +
        "       TAN.ID_UNITA_MISURA, " +
        "       UM.DESCRIZIONE AS DESC_UNITA_MISURA " +  
        "FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN, " +
        "       SMRGAA_W_UTILIZZO_AZ_NUOVA TAN, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM, " +
        "       DB_TIPO_INDIRIZZO_UTILIZZO TIU, " +
        "       DB_TIPO_UTILIZZO TP, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_TITOLO_POSSESSO TTP, " +
        "       DB_UNITA_MISURA UM " +
        "WHERE  UAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    UAN.ID_UTE_AZIENDA_NUOVA = TAN.ID_UTE_AZIENDA_NUOVA " +
        "AND    TAN.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND    TAN.ID_UTILIZZO = TP.ID_UTILIZZO " +
        "AND    TP.ID_INDIRIZZO_UTILIZZO = TIU.ID_INDIRIZZO_UTILIZZO " +
        "AND    TAN.ID_VARIETA = TV.ID_VARIETA " +
        "AND    TAN.ID_TITOLO_POSSESSO = TTP.ID_TITOLO_POSSESSO " +
        "AND    TAN.ID_UNITA_MISURA = UM.ID_UNITA_MISURA " +
        "ORDER BY UAN.DENOMINAZIONE, " +
        "         COM.DESCOM, " +
        "         TAN.FOGLIO, " +
        "         TAN.PARTICELLA ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getParticelleAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vParticelleAziendaNuova == null)
        {
          vParticelleAziendaNuova = new Vector<ParticellaAziendaNuovaVO>();
        }
       
        
        particellaAziendaNuovaVO = new ParticellaAziendaNuovaVO();
        particellaAziendaNuovaVO.setIdUtilizzoAziendaNuova(new Long(rs.getLong("ID_UTILIZZO_AZ_NUOVA")));
        particellaAziendaNuovaVO.setIdUteAziendaNuova(new Long(rs.getLong("ID_UTE_AZIENDA_NUOVA")));
        particellaAziendaNuovaVO.setDenominazUte(rs.getString("DENOMINAZIONE"));
        particellaAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        particellaAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        particellaAziendaNuovaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        particellaAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        particellaAziendaNuovaVO.setSezione(rs.getString("SEZIONE"));
        particellaAziendaNuovaVO.setFoglio(checkLongNull(rs.getString("FOGLIO")));
        particellaAziendaNuovaVO.setStrFoglio(rs.getString("FOGLIO"));
        particellaAziendaNuovaVO.setParticella(checkLongNull(rs.getString("PARTICELLA")));
        particellaAziendaNuovaVO.setStrParticella(rs.getString("PARTICELLA"));
        particellaAziendaNuovaVO.setSuperficie(rs.getBigDecimal("SUPERFICIE"));
        particellaAziendaNuovaVO.setStrSuperficie(
            Formatter.formatDouble4(particellaAziendaNuovaVO.getSuperficie()));
        particellaAziendaNuovaVO.setIdIndirizzoUtilizzo(new Long(rs.getLong("ID_INDIRIZZO_UTILIZZO")));
        particellaAziendaNuovaVO.setDescIndirizzoUtilizzo(rs.getString("DESC_INDIRIZZO_UTILIZZO"));
        particellaAziendaNuovaVO.setIdUtilizzo(new Long(rs.getLong("ID_UTILIZZO")));
        particellaAziendaNuovaVO.setDescTipoUtilizzo("["+rs.getString("COD_UTILIZZO")+"] "+rs.getString("DESC_UTILIZZO"));
        particellaAziendaNuovaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
        particellaAziendaNuovaVO.setDescTipoVarieta("["+rs.getString("CODICE_VARIETA")+"] "+rs.getString("DESC_VARIETA"));
        particellaAziendaNuovaVO.setIdTitoloPossesso(new Integer(rs.getInt("ID_TITOLO_POSSESSO")));
        particellaAziendaNuovaVO.setIdUnitaMisura(new Integer(rs.getInt("ID_UNITA_MISURA")));
        particellaAziendaNuovaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_CONDUZIONE"));
        particellaAziendaNuovaVO.setStrPercentualePossesso(
            Formatter.formatDouble2(particellaAziendaNuovaVO.getPercentualePossesso()));
        particellaAziendaNuovaVO.setDescUnitaMisura(rs.getString("DESC_UNITA_MISURA"));
        
        vParticelleAziendaNuova.add(particellaAziendaNuovaVO);
        
      }
      
      
      
      return vParticelleAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("particellaAziendaNuovaVO", particellaAziendaNuovaVO),
        new Variabile("vParticelleAziendaNuova", vParticelleAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getParticelleAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getParticelleAziendaNuovaIscrizione] END.");
    }
  }
  
  public Vector<UnitaMisuraVO> getListUnitaMisura() throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<UnitaMisuraVO> vUnitaMisura = null;
    UnitaMisuraVO unitaMisuraVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListUnitaMisura] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT UM.ID_UNITA_MISURA, " +
        "       UM.DESCRIZIONE, " +
        "       UM.ID_UNITA_MISURA_DEF, " +
        "       UM.COEFF_CONVERSIONE " + 
        "FROM   DB_UNITA_MISURA UM " +
        "WHERE  FLAG_SPECIFICA_EFA = 'N' " +
        "ORDER BY UM.DESCRIZIONE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListUnitaMisura] Query=" + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vUnitaMisura == null)
        {
          vUnitaMisura = new Vector<UnitaMisuraVO>();
        }
       
        
        unitaMisuraVO = new UnitaMisuraVO();
        unitaMisuraVO.setIdUnitaMisura(new Long(rs.getLong("ID_UNITA_MISURA")));
        unitaMisuraVO.setDescrizione(rs.getString("DESCRIZIONE"));
        unitaMisuraVO.setIdUnitaMisuraDef(checkLongNull(rs.getString("ID_UNITA_MISURA_DEF")));
        unitaMisuraVO.setCoeffConversione(rs.getBigDecimal("COEFF_CONVERSIONE"));
        
        vUnitaMisura.add(unitaMisuraVO);
        
      }
      
      
      
      return vUnitaMisura;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("unitaMisuraVO", unitaMisuraVO),
        new Variabile("vUnitaMisura", vUnitaMisura)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getListUnitaMisura] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListUnitaMisura] END.");
    }
  }
  
  public Long insertParticellaAziendaNuova(ParticellaAziendaNuovaVO particellaAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idParticellaAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertParticellaAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idParticellaAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_UTILIZZO_AZ_NUOVA   " +
        "      (ID_UTILIZZO_AZ_NUOVA, " +
        "       ID_UTE_AZIENDA_NUOVA, " +
        "       COMUNE, " +
        "       SEZIONE, " +
        "       FOGLIO, " +
        "       PARTICELLA, " +
        "       ID_TITOLO_POSSESSO, " +
        "       PERCENTUALE_CONDUZIONE, " +
        "       ID_UTILIZZO, " +
        "       ID_VARIETA, " +
        "       ID_UNITA_MISURA, " +
        "       SUPERFICIE ) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertParticellaAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idParticellaAziendaNuova.longValue());
      stmt.setLong(++indice, particellaAziendaNuovaVO.getIdUteAziendaNuova());
      stmt.setString(++indice, particellaAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, particellaAziendaNuovaVO.getSezione());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(particellaAziendaNuovaVO.getFoglio()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(particellaAziendaNuovaVO.getParticella()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(particellaAziendaNuovaVO.getIdTitoloPossesso()));
      stmt.setBigDecimal(++indice, particellaAziendaNuovaVO.getPercentualePossesso());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(particellaAziendaNuovaVO.getIdUtilizzo()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(particellaAziendaNuovaVO.getIdVarieta()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(particellaAziendaNuovaVO.getIdUnitaMisura()));
      stmt.setBigDecimal(++indice, particellaAziendaNuovaVO.getSuperficie());
      
      stmt.executeUpdate();
      
      return idParticellaAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idParticellaAziendaNuova", idParticellaAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("particellaAziendaNuovaVO", particellaAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertParticellaAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertParticellaAziendaNuova] END.");
    }
  }
  
  public void deleteParticelleAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteParticelleAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_UTILIZZO_AZ_NUOVA " +
        "WHERE  ID_UTE_AZIENDA_NUOVA IN (  " +
        "       SELECT UAN.ID_UTE_AZIENDA_NUOVA " +
        "       FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN " +
        "       WHERE  UAN.ID_AZIENDA_NUOVA = ? ) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteParticelleAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteParticelleAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteParticelleAziendaNuova] END.");
    }
  }
  
  public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova = null;
    AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllevamentiAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ALLEVAMENTO_AZ_NUOVA, " +
        "       UAN.ID_UTE_AZIENDA_NUOVA, " +
        "       UAN.DENOMINAZIONE, " +
        "       AAN.CODICE_ASL, " +
        "       AAN.ID_ASL, " +
        "       AAN.ID_CATEGORIA_ANIMALE, " +
        "       AAN.NUMERO_CAPI, " +
        "       AAN.NOTE AS NOTE_ALL, " +
        "       TCA.ID_SPECIE_ANIMALE, " +
        "       TCA.DESCRIZIONE AS DESC_CATEGORIA, " +
        "       TSA.DESCRIZIONE AS DESC_SPECIE, " +
        "       TSA.UNITA_MISURA, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       UAN.INDIRIZZO, " +
        "       UAN.CAP " +
        "FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN, " +
        "       SMRGAA_W_ALLEVAMENTO_AZ_NUOVA AAN, " +
        "       DB_TIPO_CATEGORIA_ANIMALE TCA, " +
        "       DB_TIPO_SPECIE_ANIMALE TSA, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV " +
        "WHERE  UAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    UAN.ID_UTE_AZIENDA_NUOVA = AAN.ID_UTE_AZIENDA_NUOVA " +
        "AND    AAN.ID_CATEGORIA_ANIMALE = TCA.ID_CATEGORIA_ANIMALE " +
        "AND    TCA.ID_SPECIE_ANIMALE = TSA.ID_SPECIE_ANIMALE " +
        "AND    UAN.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "ORDER BY UAN.DENOMINAZIONE, TSA.DESCRIZIONE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllevamentiAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAllevamentiAziendaNuova == null)
        {
          vAllevamentiAziendaNuova = new Vector<AllevamentoAziendaNuovaVO>();
        }
       
        
        allevamentoAziendaNuovaVO = new AllevamentoAziendaNuovaVO();
        allevamentoAziendaNuovaVO.setIdAllevamentoAziendaNuova(new Long(rs.getLong("ID_ALLEVAMENTO_AZ_NUOVA")));
        allevamentoAziendaNuovaVO.setIdUteAziendaNuova(new Long(rs.getLong("ID_UTE_AZIENDA_NUOVA")));
        allevamentoAziendaNuovaVO.setDenominazUte(rs.getString("DENOMINAZIONE"));
        allevamentoAziendaNuovaVO.setCodiceAziendaZootecnica(rs.getString("CODICE_ASL"));
        allevamentoAziendaNuovaVO.setIdAsl(checkIntegerNull(rs.getString("ID_ASL")));
        allevamentoAziendaNuovaVO.setIdCategoriaAnimale(checkIntegerNull(rs.getString("ID_CATEGORIA_ANIMALE")));
        allevamentoAziendaNuovaVO.setStrNumeroCapi(rs.getString("NUMERO_CAPI"));
        allevamentoAziendaNuovaVO.setNumeroCapi(checkLongNull(allevamentoAziendaNuovaVO.getStrNumeroCapi()));
        allevamentoAziendaNuovaVO.setNote(rs.getString("NOTE_ALL"));
        allevamentoAziendaNuovaVO.setIdSpecieAnimale(checkLongNull(rs.getString("ID_SPECIE_ANIMALE")));
        allevamentoAziendaNuovaVO.setDescSpecie(rs.getString("DESC_SPECIE"));
        allevamentoAziendaNuovaVO.setDescCategoria(rs.getString("DESC_CATEGORIA"));
        allevamentoAziendaNuovaVO.setDescUte(rs.getString("INDIRIZZO")+" - "+rs.getString("CAP")
            +" "+rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        allevamentoAziendaNuovaVO.setUnitaMisura(rs.getString("UNITA_MISURA"));
        
        
        vAllevamentiAziendaNuova.add(allevamentoAziendaNuovaVO);
        
      }
      
      
      
      return vAllevamentiAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("allevamentoAziendaNuovaVO", allevamentoAziendaNuovaVO),
        new Variabile("vAllevamentiAziendaNuova", vAllevamentiAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAllevamentiAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllevamentiAziendaNuovaIscrizione] END.");
    }
  }
  
  public Long insertAllevamentoAziendaNuova(AllevamentoAziendaNuovaVO allevamentoAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAllevamentoAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAllevamentoAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAllevamentoAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_ALLEV_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_ALLEVAMENTO_AZ_NUOVA   " +
        "      (ID_ALLEVAMENTO_AZ_NUOVA, " +
        "       ID_UTE_AZIENDA_NUOVA, " +
        "       CODICE_ASL, " +
        "       ID_ASL, " +
        "       ID_CATEGORIA_ANIMALE, " +
        "       NUMERO_CAPI, " +
        "       NOTE) " +
        "   VALUES(?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertAllevamentoAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAllevamentoAziendaNuova.longValue());
      stmt.setLong(++indice, allevamentoAziendaNuovaVO.getIdUteAziendaNuova());
      stmt.setString(++indice, allevamentoAziendaNuovaVO.getCodiceAziendaZootecnica());
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(allevamentoAziendaNuovaVO.getIdAsl()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(allevamentoAziendaNuovaVO.getIdCategoriaAnimale()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(allevamentoAziendaNuovaVO.getNumeroCapi()));
      stmt.setString(++indice, allevamentoAziendaNuovaVO.getNote());
      
      stmt.executeUpdate();
      
      return idAllevamentoAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAllevamentoAziendaNuova", idAllevamentoAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("allevamentoAziendaNuovaVO", allevamentoAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertAllevamentoAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAllevamentoAziendaNuova] END.");
    }
  }
  
  public void deleteAllevamentiAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAllevamentiAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_ALLEVAMENTO_AZ_NUOVA " +
        "WHERE  ID_UTE_AZIENDA_NUOVA IN (  " +
        "       SELECT UAN.ID_UTE_AZIENDA_NUOVA " +
        "       FROM   SMRGAA_W_UTE_AZIENDA_NUOVA UAN " +
        "       WHERE  UAN.ID_AZIENDA_NUOVA = ? ) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteAllevamentiAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteAllevamentiAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAllevamentiAziendaNuova] END.");
    }
  }
  
  
  public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CCAziendaNuovaVO> vCCAziendaNuova = null;
    CCAziendaNuovaVO cCAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getCCAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT CAZ.ID_CC_AZ_NUOVA, " +
        "       CAZ.IBAN, " +
        "       TB.DENOMINAZIONE AS DENOM_BANCA, " +
        "       TS.DENOMINAZIONE AS DENOM_SPORTELLO, " +
        "       TS.INDIRIZZO, " +
        "       TS.CAP, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA " +
        "FROM   SMRGAA_W_CC_AZ_NUOVA CAZ, " +
        "       DB_TIPO_SPORTELLO TS, "+
        "       DB_TIPO_BANCA TB," +
        "       COMUNE COM, " +
        "       PROVINCIA PROV "+
        "WHERE  CAZ.ID_AZIENDA_NUOVA =  ? " +
        "AND    SUBSTR(CAZ.IBAN,6,5) = TB.ABI " +
        "AND    TB.ID_BANCA = TS.ID_BANCA " +
        "AND    SUBSTR(CAZ.IBAN,11,5) = TS.CAB " +
        "AND    TS.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "ORDER BY TB.DENOMINAZIONE, TS.DENOMINAZIONE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getCCAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vCCAziendaNuova == null)
        {
          vCCAziendaNuova = new Vector<CCAziendaNuovaVO>();
        }
       
        
        cCAziendaNuovaVO = new CCAziendaNuovaVO();
        cCAziendaNuovaVO.setIdCCAziendaNuova(new Long(rs.getLong("ID_CC_AZ_NUOVA")));
        cCAziendaNuovaVO.setIban(rs.getString("IBAN"));
        cCAziendaNuovaVO.setDescBanca(rs.getString("DENOM_BANCA"));
        cCAziendaNuovaVO.setDescFiliale(rs.getString("DENOM_SPORTELLO"));
        cCAziendaNuovaVO.setIndirizzoFiliale(rs.getString("INDIRIZZO"));
        cCAziendaNuovaVO.setDescComuneFiliale(rs.getString("DESCOM"));
        cCAziendaNuovaVO.setSglProvFiliale(rs.getString("SIGLA_PROVINCIA"));
        cCAziendaNuovaVO.setCapFiliale(rs.getString("CAP"));
        
        vCCAziendaNuova.add(cCAziendaNuovaVO);
        
      }
      
      
      
      return vCCAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("cCAziendaNuovaVO", cCAziendaNuovaVO),
        new Variabile("vCCAziendaNuova", vCCAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getCCAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getCCAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public Long insertCCAziendaNuova(CCAziendaNuovaVO cCAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idCCAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertCCAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idCCAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_CC_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_CC_AZ_NUOVA   " +
        "      (ID_CC_AZ_NUOVA, " +
        "       ID_AZIENDA_NUOVA, " +
        "       IBAN) " +
        "   VALUES(?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertCCAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idCCAziendaNuova.longValue());
      stmt.setLong(++indice, cCAziendaNuovaVO.getIdAziendaNuova());
      stmt.setString(++indice, cCAziendaNuovaVO.getIban());
      
      stmt.executeUpdate();
      
      return idCCAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idCCAziendaNuova", idCCAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("cCAziendaNuovaVO", cCAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertCCAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertCCAziendaNuova] END.");
    }
  }
    
  public void deleteCCAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteCCAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_CC_AZ_NUOVA " +
        "WHERE  ID_AZIENDA_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteCCAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteCCAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteCCAziendaNuova] END.");
    }
  }
  
  public Long insertSoggettoAziendaNuova(SoggettoAziendaNuovaVO soggettoAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idSoggettoAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertSoggettoAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idSoggettoAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_SOGGETTI_AZ_NUOVA   " +
        "      (ID_SOGGETTI_AZ_NUOVA, " +
        "       ID_AZIENDA_NUOVA, " +
        "       CODICE_FISCALE, " +
        "       COGNOME, " +
        "       NOME, " +
        "       COMUNE_RESIDENZA, " +
        "       INDIRIZZO_RESIDENZA, " +
        "       TELEFONO, " +
        "       EMAIL, " +
        "       ID_RUOLO, " +
        "       DATA_INIZIO_RUOLO, " +
        "       CAP_RESIDENZA) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertSoggettoAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idSoggettoAziendaNuova.longValue());
      stmt.setLong(++indice, soggettoAziendaNuovaVO.getIdAziendaNuova());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getCodiceFiscale());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getCognome());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getNome());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getIndirizzo());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getTelefono());
      stmt.setString(++indice, soggettoAziendaNuovaVO.getEmail());
      stmt.setInt(++indice, soggettoAziendaNuovaVO.getIdRuolo().intValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(soggettoAziendaNuovaVO.getDataInizioRuolo()));
      stmt.setString(++indice, soggettoAziendaNuovaVO.getCap());
      
      stmt.executeUpdate();
      
      return idSoggettoAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idSoggettoAziendaNuova", idSoggettoAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("soggettoAziendaNuovaVO", soggettoAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertSoggettoAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertSoggettoAziendaNuova] END.");
    }
  }
  
  
  public Long insertAzzAssAziendaNuova(AzAssAziendaNuovaVO azAssAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idAssociateAzNuove = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAzzAssAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idAssociateAzNuove = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_ASSOCIATE_AZ_NUOVE   " +
        "      (ID_ASSOCIATE_AZ_NUOVE, " +
        "       ID_AZIENDA_NUOVA, " +
        "       DATA_INGRESSO, " +
        "       DATA_USCITA, " +
        "       CODICE_ENTE," +
        "       ID_AZIENDA_ASSOCIATA,"  +
        "       ID_AZIENDA_COLLEGATA, " +
        "       ID_SOGG_ASS_AZ_NUOVA, " +
        "       ID_RICHIESTA_AZIENDA) " +
        "   VALUES(?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertAzzAssAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAssociateAzNuove.longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(azAssAziendaNuovaVO.getIdAziendaNuova()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(azAssAziendaNuovaVO.getDataIngresso()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(azAssAziendaNuovaVO.getDataUscita()));
      stmt.setString(++indice, azAssAziendaNuovaVO.getCodEnte());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(azAssAziendaNuovaVO.getIdAziendaAssociata()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(azAssAziendaNuovaVO.getIdAziendaCollegata()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(azAssAziendaNuovaVO.getIdSoggAssAzNuova()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(azAssAziendaNuovaVO.getIdRichiestaAzienda()));
      
      stmt.executeUpdate();
      
      return idAssociateAzNuove;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idAssociateAzNuove", idAssociateAzNuove)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("azAssAziendaNuovaVO", azAssAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertAzzAssAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAzzAssAziendaNuova] END.");
    }
  }
  
  public Long insertAzzAssSoggAziendaNuova(AzAssAziendaNuovaVO azAssAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idSoggAssAzNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAzzAssSoggAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idSoggAssAzNuova = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO SMRGAA_W_SOGG_ASS_AZ_NUOVA " +
        "      (ID_SOGG_ASS_AZ_NUOVA, " +
        "       CUAA, " +
        "       DENOMINAZIONE, " +
        "       PARTITA_IVA, " +
        "       INDIRIZZO, " +
        "       COMUNE, " +
        "       CAP) " +
        "   VALUES(?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertAzzAssSoggAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idSoggAssAzNuova.longValue());
      stmt.setString(++indice, azAssAziendaNuovaVO.getCuaa());
      stmt.setString(++indice, azAssAziendaNuovaVO.getDenominazione());
      stmt.setString(++indice, azAssAziendaNuovaVO.getPartitaIva());
      stmt.setString(++indice, azAssAziendaNuovaVO.getIndirizzo());
      stmt.setString(++indice, azAssAziendaNuovaVO.getIstatComune());
      stmt.setString(++indice, azAssAziendaNuovaVO.getCap());
      
      stmt.executeUpdate();
      
      return idSoggAssAzNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idSoggAssAzNuova", idSoggAssAzNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("azAssAziendaNuovaVO", azAssAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertAzzAssSoggAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertAzzAssSoggAziendaNuova] END.");
    }
  }
  
  public void deleteSoggettoAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggettoAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_SOGGETTI_AZ_NUOVA " +
        "WHERE  ID_AZIENDA_NUOVA = ? " +
        "AND    ID_RUOLO <> 1 ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteSoggettoAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteSoggettoAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggettoAziendaNuova] END.");
    }
  }
  
  public void deleteSoggettoFromId(long idSoggettiAzNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggettoFromId] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_SOGGETTI_AZ_NUOVA " +
        "WHERE  ID_SOGGETTI_AZ_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteSoggettoFromId] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idSoggettiAzNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idSoggettiAzNuova", idSoggettiAzNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteSoggettoFromId] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggettoFromId] END.");
    }
  }
  
  public void deleteSoggAssAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAssAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_SOGG_ASS_AZ_NUOVA " +
        "WHERE  ID_SOGG_ASS_AZ_NUOVA IN ( SELECT ID_SOGG_ASS_AZ_NUOVA " +
        "                                 FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "                                 WHERE  ID_AZIENDA_NUOVA = ? ) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteSoggAssAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteSoggAssAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAssAziendaNuova] END.");
    }
  }
  
  public void deleteAzAssAziendaNuova(long idAziendaNuova) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzAssAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "WHERE  ID_AZIENDA_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteAzAssAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idAziendaNuova);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteAzAssAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzAssAziendaNuova] END.");
    }
  }
  
  
  public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<RichiestaAziendaDocumentoVO> vRichiestaziendaDocumento = null;
    RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllegatiAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RAD.ID_RICHIESTA_AZIENDA_DOCUMENTO, " +
        "       RAD.ID_RICHIESTA_AZIENDA, " +
        "       RAD.EXT_ID_DOCUMENTO, " +
        "       TD.DESCRIZIONE AS DESC_DOC, " +
        "       RAD.NUMERO_DOCUMENTO, " +
        "       RAD.ENTE_RILASCIO_DOCUMENTO, " +
        "       RAD.DATA_INIZIO_VALIDITA, " +
        "       RAD.DATA_FINE_VALIDITA " +        
        "FROM   DB_RICHIESTA_AZIENDA_DOCUMENTO RAD, " +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_TIPO_DOCUMENTO TD " +
        "WHERE  RA.ID_AZIENDA_NUOVA =  ? " +
        "AND    RA.ID_TIPO_RICHIESTA = ? " +
        "AND    RA.ID_RICHIESTA_AZIENDA = RAD.ID_RICHIESTA_AZIENDA " +
        "AND    RAD.EXT_ID_DOCUMENTO = TD.ID_DOCUMENTO " +
        "ORDER BY TD.DESCRIZIONE ASC ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllegatiAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      stmt.setLong(++indice, idTipoRichiesta);
      
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vRichiestaziendaDocumento == null)
        {
          vRichiestaziendaDocumento = new Vector<RichiestaAziendaDocumentoVO>();
        }
       
        
        richiestaAziendaDocumentoVO = new RichiestaAziendaDocumentoVO();
        richiestaAziendaDocumentoVO.setIdRichiestaAziendaDocumento(new Long(rs.getLong("ID_RICHIESTA_AZIENDA_DOCUMENTO")));
        richiestaAziendaDocumentoVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        richiestaAziendaDocumentoVO.setExtIdDocumento(new Long(rs.getLong("EXT_ID_DOCUMENTO")));
        richiestaAziendaDocumentoVO.setDescDocumento(rs.getString("DESC_DOC"));
        richiestaAziendaDocumentoVO.setNumeroDocumento(rs.getString("NUMERO_DOCUMENTO"));
        richiestaAziendaDocumentoVO.setEnteRilascioDocumento(rs.getString("ENTE_RILASCIO_DOCUMENTO"));
        richiestaAziendaDocumentoVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        richiestaAziendaDocumentoVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        
        vRichiestaziendaDocumento.add(richiestaAziendaDocumentoVO);
        
      }
      
      
      
      return vRichiestaziendaDocumento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("richiestaAziendaDocumentoVO", richiestaAziendaDocumentoVO),
        new Variabile("vRichiestaziendaDocumento", vRichiestaziendaDocumento)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAllegatiAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAllegatiAziendaNuovaIscrizione] END.");
    }
  }
  
  public Long insertRichAzDocAziendaNuova(RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idRichiestaAziendaDoc = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertRichAzDocAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idRichiestaAziendaDoc = getNextPrimaryKey(SolmrConstants.SEQ_DB_RICHIESTA_AZIENDA_DOC);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO DB_RICHIESTA_AZIENDA_DOCUMENTO   " +
        "      (ID_RICHIESTA_AZIENDA_DOCUMENTO, " +
        "       ID_RICHIESTA_AZIENDA, " +
        "       EXT_ID_DOCUMENTO, " +
        "       ID_UTENTE_AGGIORNAMENTO, " +
        "       DATA_AGGIORNAMENTO, " +
        "       NOTE," +
        "       NUMERO_DOCUMENTO," +
        "       ENTE_RILASCIO_DOCUMENTO," +
        "       DATA_INIZIO_VALIDITA," +
        "       DATA_FINE_VALIDITA) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertRichAzDocAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAziendaDoc.longValue());
      stmt.setLong(++indice, richiestaAziendaDocumentoVO.getIdRichiestaAzienda().longValue());
      stmt.setLong(++indice, richiestaAziendaDocumentoVO.getExtIdDocumento().longValue());
      stmt.setLong(++indice, richiestaAziendaDocumentoVO.getIdUtenteAggiornamento().longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));      
      stmt.setString(++indice, richiestaAziendaDocumentoVO.getNote());
      stmt.setString(++indice, richiestaAziendaDocumentoVO.getNumeroDocumento());
      stmt.setString(++indice, richiestaAziendaDocumentoVO.getEnteRilascioDocumento());
      stmt.setTimestamp(++indice, convertDateToTimestamp(richiestaAziendaDocumentoVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(richiestaAziendaDocumentoVO.getDataFineValidita()));   
      
      stmt.executeUpdate();
      
      return idRichiestaAziendaDoc;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idRichiestaAziendaDoc", idRichiestaAziendaDoc)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("richiestaAziendaDocumentoVO", richiestaAziendaDocumentoVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertRichAzDocAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertRichAzDocAziendaNuova] END.");
    }
  }
  
  
  public void deleteRichAzDocAziendaNuova(long idRichiestaDocumento) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteRichAzDocAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_RICHIESTA_AZIENDA_DOCUMENTO " +
        "WHERE  ID_RICHIESTA_AZIENDA_DOCUMENTO = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteRichAzDocAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaDocumento);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaDocumento", idRichiestaDocumento)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteRichAzDocAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteRichAzDocAziendaNuova] END.");
    }
  }
  
  
  public void insertFileStampa(long idRichiestaAzienda,  byte ba[]) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    ResultSet rs = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertFileStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */      
      queryBuf = new StringBuffer();
      queryBuf
      .append("" +
        "SELECT ID_RICHIESTA_AZIENDA, " +
        "       FILE_STAMPA " +
        "FROM   DB_RICHIESTA_AZIENDA " + 
        "WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "FOR UPDATE ");
         
  
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::insertFileStampa] Query2="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      rs = stmt.executeQuery();      
      rs.next();      
      java.sql.Blob blob = rs.getBlob("FILE_STAMPA");
      java.io.OutputStream os =  convertBlobToOutputStream(blob);      
      os.write(ba, 0, ba.length);
      os.flush();
        
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)
        };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::insertFileStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::insertFileStampa] END.");
    }
  }
  
  
  public void updateFileStampa(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::updateFileStampa] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */      
      queryBuf = new StringBuffer();
      queryBuf
      .append("" +
        "UPDATE DB_RICHIESTA_AZIENDA " +
        "SET FILE_STAMPA = empty_blob() " +
        "WHERE ID_RICHIESTA_AZIENDA = ? ");
         
  
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[DocumentoGaaDAO::updateFileStampa] Query2="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      stmt.executeUpdate();
        
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)
        };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[DocumentoGaaDAO::updateFileStampa] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[DocumentoGaaDAO::updateFileStampa] END.");
    }
  }
  
  
  /**
   * ritorna gli id dell'elenco delle aziende per
   * la ricerca 
   * 
   * 
   * 
   * 
   * @param idTipoRichiesta
   * @param idStatoRichiesta
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getElencoIdRichiestaAzienda(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      Long idAzienda, RuoloUtenza ruoloUtenza) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdAziendaNuova = null;
    Long idRichiestaAzienda = null;
    
    
    //Uati per inserire il valore 0 = tutti!!!
    if(Validator.isNotEmpty(idTipoRichiesta)
      && idTipoRichiesta.compareTo(new Long(0)) == 0)
    {
      idTipoRichiesta = null;
    }
    if(Validator.isNotEmpty(idStatoRichiesta)
      && idStatoRichiesta.compareTo(new Long(0)) == 0)
    {
      idStatoRichiesta = null;
    }
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       NVL2 (RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       TR.DESCRIZIONE, " +
        "       SR.DESCRIZIONE, " +
        "       IRA.DATA_AGGIORNAMENTO " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_STATO_RICHIESTA SR," +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_R_RUOLO_TIPO_RICHIESTA RRR " +
        "WHERE  RA.ID_AZIENDA_NUOVA = WAN.ID_AZIENDA_NUOVA (+)  " +
        "AND    RA.ID_AZIENDA = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+)  IS NULL " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    IRA.ID_STATO_RICHIESTA = SR.ID_STATO_RICHIESTA " +
        "AND    RA.ID_TIPO_RICHIESTA = RRR.ID_TIPO_RICHIESTA " +
        "AND    RRR.CODICE_RUOLO = ? " +
        "AND    RRR.FLAG_VISIBILE = 'S' ");
      
      
      if(Validator.isNotEmpty(idStatoRichiesta))
      {
        queryBuf.append(""+
        "AND    IRA.ID_STATO_RICHIESTA = ? ");
      }
      if(Validator.isNotEmpty(idTipoRichiesta))
      {
        queryBuf.append(""+
        "AND    RA.ID_TIPO_RICHIESTA = ? ");
      }
      if(Validator.isNotEmpty(cuaa))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.CUAA, WAN.CUAA) = ? ");
      }
      if(Validator.isNotEmpty(partitaIva))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.PARTITA_IVA, WAN.PARTITA_IVA) = ? ");
      }
      if(Validator.isNotEmpty(denominazione))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) LIKE UPPER(?) ");
      }
      if(Validator.isNotEmpty(idAzienda))
      {
        queryBuf.append(""+
        "AND    RA.ID_AZIENDA = ? ");
      }
      
      
      if(Validator.isNotEmpty(idAzienda))
      {
        queryBuf.append(""+
        "ORDER BY IRA.DATA_AGGIORNAMENTO DESC, TR.DESCRIZIONE ASC, SR.DESCRIZIONE ASC ");
      }
      else
      {
        queryBuf.append(""+
        "ORDER BY DENOMINAZIONE ASC, IRA.DATA_AGGIORNAMENTO DESC, TR.DESCRIZIONE ASC, SR.DESCRIZIONE ASC ");
      }
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      stmt.setString(++indice, ruoloUtenza.getCodiceRuolo());

      // Setto i parametri della query
      if(Validator.isNotEmpty(idStatoRichiesta))
      {
        stmt.setLong(++indice, idStatoRichiesta.longValue());
      }
      
      if(Validator.isNotEmpty(idTipoRichiesta))
      {
        stmt.setLong(++indice, idTipoRichiesta);
      }
      
      if(Validator.isNotEmpty(cuaa))
      {
        stmt.setString(++indice, cuaa);
      }
      
      if(Validator.isNotEmpty(partitaIva))
      {
        stmt.setString(++indice, partitaIva);
      }
      
      if(Validator.isNotEmpty(denominazione))
      {
        stmt.setString(++indice, denominazione.trim().toUpperCase()+"%");
      }
      
      if(Validator.isNotEmpty(idAzienda))
      {
        stmt.setLong(++indice, idAzienda.longValue());
      }
      
      
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
        
        if(vIdAziendaNuova == null)
        {
          vIdAziendaNuova = new Vector<Long>();
        }
        
        vIdAziendaNuova.add(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));       
        
      }
      
      return vIdAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("vIdAziendaNuova", vIdAziendaNuova),
        new Variabile("idRichiestaAzienda", idRichiestaAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceRuolo", ruoloUtenza.getCodiceRuolo()),
        new Parametro("idTipoRichiesta", idTipoRichiesta),
        new Parametro("idStatoRichiesta", idStatoRichiesta),
        new Parametro("cuaa", cuaa),
        new Parametro("partitaIva", partitaIva),
        new Parametro("denominazione", denominazione),
        new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] END.");
    }
  }
  
  
  public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdAziendaNuova = null;
    Long idRichiestaAzienda = null;
    
    
    //Uati per inserire il valore 0 = tutti!!!
    if(Validator.isNotEmpty(idTipoRichiesta)
      && idTipoRichiesta.compareTo(new Long(0)) == 0)
    {
      idTipoRichiesta = null;
    }
    if(Validator.isNotEmpty(idStatoRichiesta)
      && idStatoRichiesta.compareTo(new Long(0)) == 0)
    {
      idStatoRichiesta = null;
    }
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       NVL2 (RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       TR.DESCRIZIONE, " +
        "       SR.DESCRIZIONE, " +
        "       IRA.DATA_AGGIORNAMENTO " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_STATO_RICHIESTA SR," +
        "       DB_ANAGRAFICA_AZIENDA AA " +
        "WHERE  RA.ID_AZIENDA_NUOVA = WAN.ID_AZIENDA_NUOVA  " +
        "AND    RA.ID_AZIENDA = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+)  IS NULL " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    IRA.ID_STATO_RICHIESTA = SR.ID_STATO_RICHIESTA " +
        "AND    SUBSTR(WAN.CODICE_ENTE, 1, 3) = ? ");
      
      
      if(Validator.isNotEmpty(idStatoRichiesta))
      {
        queryBuf.append(""+
        "AND    IRA.ID_STATO_RICHIESTA = ? ");
      }
      if(Validator.isNotEmpty(idTipoRichiesta))
      {
        queryBuf.append(""+
        "AND    RA.ID_TIPO_RICHIESTA = ? ");
      }
      if(Validator.isNotEmpty(cuaa))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.CUAA, WAN.CUAA) = ? ");
      }
      if(Validator.isNotEmpty(partitaIva))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.PARTITA_IVA, WAN.PARTITA_IVA) = ? ");
      }
      if(Validator.isNotEmpty(denominazione))
      {
        queryBuf.append(""+
        "AND    NVL2 (RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) LIKE UPPER(?) ");
      }
      
      
      
      queryBuf.append(""+
        "ORDER BY DENOMINAZIONE ASC, IRA.DATA_AGGIORNAMENTO DESC, TR.DESCRIZIONE ASC, SR.DESCRIZIONE ASC ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;
      
      String codEnte = ruoloUtenza.getCodiceEnte();
      stmt.setString(++indice, codEnte.substring(0, 3));

      // Setto i parametri della query
      if(Validator.isNotEmpty(idStatoRichiesta))
      {
        stmt.setLong(++indice, idStatoRichiesta.longValue());
      }
      
      if(Validator.isNotEmpty(idTipoRichiesta))
      {
        stmt.setLong(++indice, idTipoRichiesta);
      }
      
      if(Validator.isNotEmpty(cuaa))
      {
        stmt.setString(++indice, cuaa);
      }
      
      if(Validator.isNotEmpty(partitaIva))
      {
        stmt.setString(++indice, partitaIva);
      }
      
      if(Validator.isNotEmpty(denominazione))
      {
        stmt.setString(++indice, denominazione.trim().toUpperCase()+"%");
      }
     
      
      
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
        
        if(vIdAziendaNuova == null)
        {
          vIdAziendaNuova = new Vector<Long>();
        }
        
        vIdAziendaNuova.add(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));       
        
      }
      
      return vIdAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("vIdAziendaNuova", vIdAziendaNuova),
        new Variabile("idRichiestaAzienda", idRichiestaAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceRuolo", ruoloUtenza.getCodiceRuolo()),
        new Parametro("idTipoRichiesta", idTipoRichiesta),
        new Parametro("idStatoRichiesta", idStatoRichiesta),
        new Parametro("cuaa", cuaa),
        new Parametro("partitaIva", partitaIva),
        new Parametro("denominazione", denominazione) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoIdRichiestaAzienda] END.");
    }
  }
  
  
  public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
     throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<Long> vIdAziendaNuova = null;
    Long idRichiestaAzienda = null;
    
    
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoRichieseteAziendaByIdRichiestaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       NVL2 (RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       TR.DESCRIZIONE, " +
        "       SR.DESCRIZIONE, " +
        "       IRA.DATA_AGGIORNAMENTO " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_STATO_RICHIESTA SR," +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_R_RUOLO_TIPO_RICHIESTA RRR " +
        "WHERE  RA.ID_AZIENDA_NUOVA = WAN.ID_AZIENDA_NUOVA (+) " +
        "AND    RA.ID_AZIENDA = AA.ID_AZIENDA " +
        "AND    AA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    IRA.ID_STATO_RICHIESTA = SR.ID_STATO_RICHIESTA " +
        "AND    RA.ID_AZIENDA = ? " +
        "AND    RA.ID_TIPO_RICHIESTA = RRR.ID_TIPO_RICHIESTA " +
        "AND    RRR.CODICE_RUOLO = ? " +
        "AND    RRR.FLAG_VISIBILE = 'S' " +
        "ORDER BY IRA.DATA_AGGIORNAMENTO DESC, TR.DESCRIZIONE ASC, SR.DESCRIZIONE ASC ");
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoRichieseteAziendaByIdRichiestaAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      stmt.setLong(++indice, idAzienda);
      stmt.setString(++indice, codiceRuolo);
      
      
      
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
        
        if(vIdAziendaNuova == null)
        {
          vIdAziendaNuova = new Vector<Long>();
        }
        
        vIdAziendaNuova.add(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));       
        
      }
      
      return vIdAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("vIdAziendaNuova", vIdAziendaNuova),
        new Variabile("idRichiestaAzienda", idRichiestaAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getElencoRichieseteAziendaByIdRichiestaAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoRichieseteAziendaByIdRichiestaAzienda] END.");
    }
  }
  
  
  public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<Long,AziendaNuovaVO> hAziendaNuova = null; 
    Vector<AziendaNuovaVO> vAziendaNuova = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoAziendaNuovaByIdRichiestaAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       WAN.ID_AZIENDA_NUOVA, " +
        "       NVL2(RA.ID_AZIENDA, AA.CUAA, WAN.CUAA) AS CUAA, " +
        "       NVL2(RA.ID_AZIENDA, AA.PARTITA_IVA, WAN.PARTITA_IVA) AS PARTITA_IVA, " +
        "       NVL2(RA.ID_AZIENDA, AA.DENOMINAZIONE, WAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       NVL2(RA.ID_AZIENDA, AA.SEDELEG_COMUNE, WAN.SEDELEG_COMUNE) AS SEDELEG_COMUNE, " +
        "       NVL2(RA.ID_AZIENDA, COM_SEDE_AA.DESCOM, COM_SEDE.DESCOM) AS DESCOM_SEDE, " +
        "       NVL2(RA.ID_AZIENDA, PROV_SEDE_AA.SIGLA_PROVINCIA, PROV_SEDE.SIGLA_PROVINCIA) AS SGL_PROV_SEDE, " +
        "       NVL2(RA.ID_AZIENDA, AA.SEDELEG_INDIRIZZO, WAN.SEDELEG_INDIRIZZO) AS SEDELEG_INDIRIZZO, " +
        "       NVL2(RA.ID_AZIENDA, AA.SEDELEG_CITTA_ESTERO, WAN.SEDELEG_CITTA_ESTERO) AS SEDELEG_CITTA_ESTERO, " +
        "       NVL2(RA.ID_AZIENDA, AA.SEDELEG_CAP, WAN.SEDELEG_CAP) AS SEDELEG_CAP, " +
        "       RA.ID_TIPO_RICHIESTA, " +
        "       RA.ID_AZIENDA, " +
        "       RA.ID_UTENTE_AGGIORNAMENTO, " +
        "       RA.NOTE AS NOTE_RICH, " +
        "       TR.DESCRIZIONE AS DESC_TIPO_RICH, " +
        "       TR.DESCRIZIONE_ESTESA, " +
        "       TR.TESTO_ANNULLAMENTO, " +
        "       WAN.ID_AZIENDA_SUBENTRO, " +
        "       SR.DESCRIZIONE AS DESC_STATO_RICHIESTA, " +
        "       IRA.DATA_INIZIO_VALIDITA, " +
        "       IRA.DATA_AGGIORNAMENTO, " +
        "       IRA.NOTE AS ITER_NOTE, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE RA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DENOM_UTENTE, " +
        "       MR.DESCRIZIONE AS DESC_MOTIVO_RICHIESTA, " +
        "       TMD.DESCRIZIONE AS DESC_MOTIVO_DICHIARAZIONE, " +
        "       TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
        "       (SELECT MAX(DIC.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA_VALIDAZIONE " +
        "        FROM   DB_DICHIARAZIONE_CONSISTENZA DIC " +
        "        WHERE  DIC.ID_AZIENDA = RA.ID_AZIENDA " +
        "        AND    DIC.NUMERO_PROTOCOLLO IS NOT NULL) AS DATA_VALIDAZIONE, " +
        "       IRA.ID_ITER_RICHIESTA_AZIENDA " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       COMUNE COM_SEDE, " +
        "       PROVINCIA PROV_SEDE, " +
        "       COMUNE COM_SEDE_AA, " +
        "       PROVINCIA PROV_SEDE_AA, " +
        "       DB_TIPO_RICHIESTA TR," +
        "       DB_STATO_RICHIESTA SR, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA," +
   //     "       PAPUA_V_UTENTE_LOGIN PVU, " +
        "       DB_MOTIVO_RICHIESTA MR, " +
        "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
        "       DB_TIPO_CESSAZIONE TC " +
        "WHERE  RA.ID_RICHIESTA_AZIENDA IN ( ").append(
            getIdListFromVectorLongForSQL(vIdRichiestaAzienda)).append(")" +
        "AND    RA.ID_AZIENDA_NUOVA = WAN.ID_AZIENDA_NUOVA (+) " +
        "AND    RA.ID_AZIENDA = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    RA.ID_MOTIVO_RICHIESTA = MR.ID_MOTIVO_RICHIESTA (+) " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRA.ID_STATO_RICHIESTA = SR.ID_STATO_RICHIESTA " +
        "AND    WAN.SEDELEG_COMUNE = COM_SEDE.ISTAT_COMUNE (+) " +
        "AND    COM_SEDE.ISTAT_PROVINCIA = PROV_SEDE.ISTAT_PROVINCIA (+) " +
        "AND    AA.SEDELEG_COMUNE = COM_SEDE_AA.ISTAT_COMUNE (+) " +
        "AND    COM_SEDE_AA.ISTAT_PROVINCIA = PROV_SEDE_AA.ISTAT_PROVINCIA (+) " +
     //   "AND    RA.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND    RA.ID_CESSAZIONE = TC.ID_CESSAZIONE (+) " +
        "AND    RA.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE (+) ");
       // "ORDER BY WAN.DENOMINAZIONE ASC, TR.DESCRIZIONE ASC, SR.DESCRIZIONE ASC, IRA.DATA_AGGIORNAMENTO ASC ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoAziendaNuovaByIdRichiestaAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
    
      ResultSet rs = stmt.executeQuery();
          
      while (rs.next())
      {
        if(hAziendaNuova == null)
        {
          hAziendaNuova = new HashMap<Long, AziendaNuovaVO>();
        }
        
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        aziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        aziendaNuovaVO.setIdUtenteAggiornamentoRich(new Long(rs.getLong("ID_UTENTE_AGGIORNAMENTO")));
        aziendaNuovaVO.setNoteRichiestaAzienda(rs.getString("NOTE_RICH"));
        aziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        aziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        aziendaNuovaVO.setSedelegComune(rs.getString("SEDELEG_COMUNE"));
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM_SEDE"));
        aziendaNuovaVO.setSedelegProv(rs.getString("SGL_PROV_SEDE"));
        aziendaNuovaVO.setSedelegIndirizzo(rs.getString("SEDELEG_INDIRIZZO"));
        aziendaNuovaVO.setSedelegCittaEstero(rs.getString("SEDELEG_CITTA_ESTERO"));
        aziendaNuovaVO.setSedelegCap(rs.getString("SEDELEG_CAP"));
        aziendaNuovaVO.setIdTipoRichiesta(checkLongNull(rs.getString("ID_TIPO_RICHIESTA")));
        aziendaNuovaVO.setDescTipoRichiesta(rs.getString("DESC_TIPO_RICH"));
        aziendaNuovaVO.setIdAziendaSubentro(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRO")));
        aziendaNuovaVO.setIdAzienda(checkLongNull(rs.getString("ID_AZIENDA")));
        aziendaNuovaVO.setDescStatoRichiesta(rs.getString("DESC_STATO_RICHIESTA"));
        aziendaNuovaVO.setDataAggiornamento(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        aziendaNuovaVO.setNote(rs.getString("ITER_NOTE"));
        aziendaNuovaVO.setDenomUtenteModifica(rs.getString("DENOM_UTENTE"));
        aziendaNuovaVO.setIdStatoRichiesta(new Long(rs.getLong("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setDescEstesaTipoRichiesta(rs.getString("DESCRIZIONE_ESTESA"));
        aziendaNuovaVO.setDataAggiornamentoIter(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        aziendaNuovaVO.setTestoAnnullamento(rs.getString("TESTO_ANNULLAMENTO"));
        String motivoRichiesta = rs.getString("DESC_MOTIVO_RICHIESTA");
        if(Validator.isEmpty(motivoRichiesta))
          motivoRichiesta = rs.getString("DESC_MOTIVO_DICHIARAZIONE");
        if(Validator.isEmpty(motivoRichiesta))
          motivoRichiesta = rs.getString("DESC_CESSAZIONE");
        aziendaNuovaVO.setDescMotivoRichiesta(motivoRichiesta);
        
        aziendaNuovaVO.setDataValidazione(rs.getTimestamp("DATA_VALIDAZIONE"));
        aziendaNuovaVO.setIdIterRichiestaAzienda(checkLongNull(rs.getString("ID_ITER_RICHIESTA_AZIENDA")));
        
        hAziendaNuova.put(aziendaNuovaVO.getIdRichiestaAzienda(), aziendaNuovaVO);
        
      }
      
      if(hAziendaNuova != null)
      {
        if(vAziendaNuova == null)
        {
          vAziendaNuova = new Vector<AziendaNuovaVO>();
        }
        for(int i=0;i<vIdRichiestaAzienda.size();i++)
        {
          vAziendaNuova.add(hAziendaNuova.get(vIdRichiestaAzienda.get(i)));
        }        
      }
      
      return vAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO),
        new Variabile("hAziendaNuova", hAziendaNuova),
        new Variabile("vAziendaNuova", vAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdRichiestaAzienda", vIdRichiestaAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getElencoAziendaNuovaByIdRichiestaAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getElencoAziendaNuovaByIdRichiestaAzienda] END.");
    }
  }
  
  
  public Vector<CodeDescription> getListTipoRichiesta() throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vTipoRichiesta = null;
    CodeDescription cd = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiesta] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TR.ID_TIPO_RICHIESTA, " +
        "       TR.DESCRIZIONE " +
        "FROM   DB_TIPO_RICHIESTA TR " +
        "WHERE  TR.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TR.ID_TIPO_RICHIESTA ASC");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiesta] Query=" + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoRichiesta == null)
        {
          vTipoRichiesta = new Vector<CodeDescription>();
        }
       
        
        cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_TIPO_RICHIESTA")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        
        vTipoRichiesta.add(cd);
        
      }
      
      
      
      return vTipoRichiesta;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("cd", cd),
        new Variabile("vTipoRichiesta", vTipoRichiesta)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getListTipoRichiesta] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiesta] END.");
    }
  }
  
  
  
  public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<CodeDescription> vTipoRichiesta = null;
    CodeDescription cd = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiestaVariazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT TR.ID_TIPO_RICHIESTA, " +
        "       TR.DESCRIZIONE " +
        "FROM   DB_TIPO_RICHIESTA TR, " +
        "       DB_R_RUOLO_TIPO_RICHIESTA RRT " +
        "WHERE  TR.DATA_FINE_VALIDITA IS NULL " +
        "AND    TR.FLAG_VARIAZIONE = 'S' " +
        "AND    TR.ID_TIPO_RICHIESTA = RRT.ID_TIPO_RICHIESTA " +
        "AND    RRT.CODICE_RUOLO = ? " +
        "AND    RRT.FLAG_INSERIBILE = 'S' " +
        "ORDER BY TR.ID_TIPO_RICHIESTA ASC ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiestaVariazione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setString(++indice, codiceRuolo);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoRichiesta == null)
        {
          vTipoRichiesta = new Vector<CodeDescription>();
        }
       
        
        cd = new CodeDescription();
        cd.setCode(new Integer(rs.getInt("ID_TIPO_RICHIESTA")));
        cd.setDescription(rs.getString("DESCRIZIONE"));
        
        vTipoRichiesta.add(cd);
        
      }
      
      
      
      return vTipoRichiesta;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("cd", cd),
        new Variabile("vTipoRichiesta", vTipoRichiesta)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getListTipoRichiestaVariazione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListTipoRichiestaVariazione] END.");
    }
  }
  
  
  
  public Vector<StatoRichiestaVO> getListStatoRichiesta() throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StatoRichiestaVO> vStatoRichiesta = null;
    StatoRichiestaVO statoRichiestaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListStatoRichiesta] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SR.ID_STATO_RICHIESTA, " +
        "       SR.DESCRIZIONE, " +
        "       SR.DATA_INIZIO_VALIDITA, " +
        "       SR.DESCRIZIONE_ESTESA, " +
        "       SR.INTESTAZIONE_PASSO, " +
        "       SR.DESCRIZIONE_PASSO, " +
        "       SR.FLAG_VISUALIZZA_STEP, " +
        "       SR.ID_STATO_RICHIESTA_PRECEDENTE " +
        "FROM   DB_STATO_RICHIESTA SR " +
        "WHERE  SR.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY SR.ID_STATO_RICHIESTA ASC");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListStatoRichiesta] Query=" + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vStatoRichiesta == null)
        {
          vStatoRichiesta = new Vector<StatoRichiestaVO>();
        }
       
        
        statoRichiestaVO = new StatoRichiestaVO();
        statoRichiestaVO.setIdStatoRichiesta(new Integer(rs.getInt("ID_STATO_RICHIESTA")));
        statoRichiestaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        statoRichiestaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        statoRichiestaVO.setDescrizioneEstesa(rs.getString("DESCRIZIONE_ESTESA"));
        statoRichiestaVO.setIntestazionePasso(rs.getString("INTESTAZIONE_PASSO"));
        statoRichiestaVO.setDescrizionePasso(rs.getString("DESCRIZIONE_PASSO"));
        statoRichiestaVO.setFlagVisualizzaStep(rs.getString("FLAG_VISUALIZZA_STEP"));
        statoRichiestaVO.setIdStatoRichiestaPrecedente(checkIntegerNull(rs.getString("ID_STATO_RICHIESTA_PRECEDENTE")));
        
        
        vStatoRichiesta.add(statoRichiestaVO);
        
      }
      
      
      
      return vStatoRichiesta;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("statoRichiestaVO", statoRichiestaVO),
        new Variabile("vStatoRichiesta", vStatoRichiesta)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getListStatoRichiesta] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListStatoRichiesta] END.");
    }
  }
  
  public RichiestaAziendaVO getPdfAziendaNuova(
      long idRichiestaAzienda) throws DataAccessException
  {
    String query = null;
    StringBuffer queryBuf = new StringBuffer();
    Connection conn = null;
    PreparedStatement stmt = null;
    RichiestaAziendaVO richiestaAziendaVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::getPdfAziendaNuova] BEGIN.");

      queryBuf.append(
          "SELECT RA.FILE_STAMPA, " +
          "       RA.EXT_ID_DOCUMENTO_INDEX " +
          "FROM   DB_RICHIESTA_AZIENDA RA " +
          "WHERE  RA.ID_RICHIESTA_AZIENDA = ? ");
      
      query = queryBuf.toString();

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il debug è abilitato

        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::getPdfAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        richiestaAziendaVO = new RichiestaAziendaVO();       
        
        richiestaAziendaVO.setExtIdDocumentoIndex(checkLongNull(rs.getString("EXT_ID_DOCUMENTO_INDEX")));
        
         
        Blob blob = rs.getBlob("FILE_STAMPA");
        if(blob!=null && (blob.length() >0))
        {
          richiestaAziendaVO.setFileStampa(blob.getBytes(1, (int)blob.length()));
        }
        
      }
      
      
      return richiestaAziendaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("richiestaAziendaVO", richiestaAziendaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[NuovaIscrizioneDAO::getPdfAziendaNuova] ", t,
          query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[NuovaIscrizioneDAO::getPdfAziendaNuova] END.");
    }
  }
  
  public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::ribaltaAziendaPlSql] BEGIN.");
      /***
       *  PROCEDURE Ribalta_Azienda (pIdRichiestaAzienda    IN DB_RICHIESTA_AZIENDA.ID_RICHIESTA_AZIENDA%TYPE,
                                      pResult      OUT VARCHAR2,
                                      pMsg         OUT VARCHAR2);
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PCK_SMRGAA_LIBRERIA.Ribalta_Azienda(?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ParticellaGaaDAO::ribaltaAziendaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idRichiestaAzienda);
      cs.registerOutParameter(2,Types.VARCHAR); //codice errore
      cs.registerOutParameter(3,Types.VARCHAR); //msg errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();  
      plqObj.setDescription(cs.getString(2));
      plqObj.setOtherdescription(cs.getString(3));
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ParticellaGaaDAO::ribaltaAziendaPlSql] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[ParticellaGaaDAO::ribaltaAziendaPlSql] END.");
    }
  }
  
  
  /**
   * Mi dice se per questa partita iva è già presente un record 
   * su smrga_w_azienda_nuova
   * 
   * 
   * 
   * @param partitaIva
   * @param idTipoRichiesta
   * @return
   * @throws DataAccessException
   */
  public boolean isPartitaIvaPresente(
      String partitaIva, long[] arrTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean trovata = false;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isPartitaIvaPresente] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT WAN.ID_AZIENDA_NUOVA " +
        "FROM   SMRGAA_W_AZIENDA_NUOVA WAN," +
        "       DB_RICHIESTA_AZIENDA RA, " +
        "       db_iter_richiesta_azienda ir " +
        "WHERE  WAN.PARTITA_IVA = ? " +
        "AND    WAN.ID_AZIENDA_NUOVA = RA.ID_AZIENDA_NUOVA " +
        "AND    RA.ID_TIPO_RICHIESTA IN ( ")
          .append(getIdListFromArrayForSQL(arrTipoRichiesta))
          .append(") ")
          .append("  and ir.id_richiesta_Azienda = ra.id_richiesta_Azienda ")
          .append("  and ir.id_stato_richiesta <> 60 ")
          .append("  and ir.data_Fine_Validita is null ");
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isPartitaIvaPresente] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, partitaIva);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        trovata = true;
      }
      return trovata;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("trovata", trovata) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("partitaIva", partitaIva),
        new Parametro("arrTipoRichiesta", arrTipoRichiesta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::isPartitaIvaPresente] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isPartitaIvaPresente] END.");
    }
  }
  
  public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda, String flagDichiarazioneAllegati) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateFlagDichiarazioneAllegati] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE DB_RICHIESTA_AZIENDA   " +
        "   SET FLAG_DICHIARAZIONE_ALLEGATI = ? " +
        "   WHERE ID_RICHIESTA_AZIENDA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateFlagDichiarazioneAllegati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setString(++indice, flagDichiarazioneAllegati);
      
      stmt.setLong(++indice, idRichiestaAzienda);
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("idRichiestaAzienda", idRichiestaAzienda),
            new Parametro("flagDichiarazioneAllegati", flagDichiarazioneAllegati) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateFlagDichiarazioneAllegati] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateFlagDichiarazioneAllegati] END.");
    }
  }
  
  public void updateIdMotivoRichiesta(long idRichiestaAzienda, int idMotivoRichiesta) 
      throws DataAccessException
    {
      String query = null;
      Connection conn = null;
      PreparedStatement stmt = null;
      StringBuffer queryBuf = null;
      
      try
      {
        SolmrLogger
            .debug(this,
                "[NuovaIscrizioneDAO::updateIdMotivoRichiesta] BEGIN.");
    
        /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
        queryBuf = new StringBuffer();
        queryBuf.append("" +
          "   UPDATE DB_RICHIESTA_AZIENDA   " +
          "   SET ID_MOTIVO_RICHIESTA = ? " +
          "   WHERE ID_RICHIESTA_AZIENDA = ? ");
               
        
        query = queryBuf.toString();
        /* CONCATENAZIONE/CREAZIONE QUERY END. */
    
        conn = getDatasource().getConnection();
        if (SolmrLogger.isDebugEnabled(this))
        {
          // Dato che la query costruita dinamicamente è un dato importante la
          // registro sul file di log se il
          // debug è abilitato
    
          SolmrLogger.debug(this,
              "[NuovaIscrizioneDAO::updateIdMotivoRichiesta] Query="
                  + query);
        }
        stmt = conn.prepareStatement(query);
        
        int indice = 0;
        
        stmt.setInt(++indice, idMotivoRichiesta);
        
        stmt.setLong(++indice, idRichiestaAzienda);
        
        stmt.executeUpdate();
        
      }
      catch (Throwable t)
      {
        // Vettore di variabili interne del metodo
        Variabile variabili[] = new Variabile[]
        { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
    
        // Vettore di parametri passati al metodo
        Parametro parametri[] = new Parametro[]
            { new Parametro("idRichiestaAzienda", idRichiestaAzienda),
              new Parametro("idMotivoRichiesta", idMotivoRichiesta) };
        // Logging dell'eccezione, query, variabili e parametri del metodo
        LoggerUtils
            .logDAOError(
                this,
                "[NuovaIscrizioneDAO::updateIdMotivoRichiesta] ",
                t, query, variabili, parametri);
        /*
         * Rimappo e rilancio l'eccezione come DataAccessException.
         */
        throw new DataAccessException(t.getMessage());
      }
      finally
      {
        /*
         * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
         * ignora ogni eventuale eccezione)
         */
        close(null, stmt, conn);
    
        // Fine metodo
        SolmrLogger
            .debug(this,
                "[NuovaIscrizioneDAO::updateIdMotivoRichiesta] END.");
      }
    }
  
  
  public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<MotivoRichiestaVO> vMotivoRichiesta = null;
    MotivoRichiestaVO motivoRichiestaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListMotivoRichiesta] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT MR.ID_MOTIVO_RICHIESTA, " +
        "       MR.DESCRIZIONE, " +
        "       MR.NOTE_OBBLIGATORIE, " +
        "       TMR.ID_TIPO_RICHIESTA " +
        "FROM   DB_MOTIVO_RICHIESTA MR, " +
        "       DB_R_TIPO_MOTIVO_RICHIESTA TMR " +
        "WHERE  MR.DATA_FINE_VALIDITA IS NULL " +
        "AND    TMR.DATA_FINE_VALIDITA IS NULL " +
        "AND    TMR.ID_TIPO_RICHIESTA = ? " +
        "AND    TMR.ID_MOTIVO_RICHIESTA = MR.ID_MOTIVO_RICHIESTA " +
        "ORDER BY MR.ORDINAMENTO ASC");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListMotivoRichiesta] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setInt(++indice, idTipoRichiesta);

      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vMotivoRichiesta == null)
        {
          vMotivoRichiesta = new Vector<MotivoRichiestaVO>();
        }
       
        
        motivoRichiestaVO = new MotivoRichiestaVO();
        motivoRichiestaVO.setIdMotivoRichiesta(new Integer(rs.getInt("ID_MOTIVO_RICHIESTA")));
        motivoRichiestaVO.setDescrizione(rs.getString("DESCRIZIONE"));
        motivoRichiestaVO.setNoteObbligatorie(rs.getString("NOTE_OBBLIGATORIE"));
        motivoRichiestaVO.setIdTipoRichiesta(new Integer(rs.getInt("ID_TIPO_RICHIESTA")));
        
        
        vMotivoRichiesta.add(motivoRichiestaVO);
        
      }
      
      
      
      return vMotivoRichiesta;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("motivoRichiestaVO", motivoRichiestaVO),
        new Variabile("vMotivoRichiesta", vMotivoRichiesta)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      {  new Parametro("idTipoRichiesta", idTipoRichiesta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getListMotivoRichiesta] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getListMotivoRichiesta] END.");
    }
  }
  
  public IterRichiestaAziendaVO getIterRichiestaAziendaByPrimaryKey(
      long idIterRichiestaAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    IterRichiestaAziendaVO iterRichiestaAziendaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIterRichiestaAziendaByPrimaryKey] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT IRA.ID_ITER_RICHIESTA_AZIENDA, " +
        "       IRA.ID_RICHIESTA_AZIENDA, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       IRA.NOTE, " +
        "       IRA.FLAG_MAIL_NOTIFICA, " +
        "       IRA.DATA_INIZIO_VALIDITA, " +
        "       IRA.DATA_FINE_VALIDITA " +        
        "FROM   DB_ITER_RICHIESTA_AZIENDA IRA " +
        "WHERE  IRA.ID_ITER_RICHIESTA_AZIENDA =  ? ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIterRichiestaAziendaByPrimaryKey] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idIterRichiestaAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      if(rs.next())
      {
        iterRichiestaAziendaVO = new IterRichiestaAziendaVO();
        iterRichiestaAziendaVO.setIdIterRichiestaAzienda(new Long(rs.getLong("ID_ITER_RICHIESTA_AZIENDA")));
        iterRichiestaAziendaVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        iterRichiestaAziendaVO.setIdStatoRichiesta(new Long(rs.getLong("ID_STATO_RICHIESTA")));
        iterRichiestaAziendaVO.setNote(rs.getString("NOTE"));
        iterRichiestaAziendaVO.setFlagMailNotifica(rs.getString("FLAG_MAIL_NOTIFICA"));
        iterRichiestaAziendaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        iterRichiestaAziendaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));      
        
      }
      
      
      
      return iterRichiestaAziendaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("iterRichiestaAziendaVO", iterRichiestaAziendaVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idIterRichiestaAzienda", idIterRichiestaAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getIterRichiestaAziendaByPrimaryKey] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIterRichiestaAziendaByPrimaryKey] END.");
    }
  }
  
  
  public void updateRichiestaAziendaIndex(long idRichiestaAzienda, long idDocumentoIndex) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateRichiestaAziendaIndex] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE DB_RICHIESTA_AZIENDA   " +
        "   SET EXT_ID_DOCUMENTO_INDEX = ?, " +
        "       FILE_STAMPA = empty_blob() " +
        "   WHERE ID_RICHIESTA_AZIENDA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateRichiestaAziendaIndex] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      
      stmt.setLong(++indice, idDocumentoIndex);
      stmt.setLong(++indice, idRichiestaAzienda);
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("idRichiestaAzienda", idRichiestaAzienda),
            new Parametro("idDocumentoIndex", idDocumentoIndex) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateRichiestaAziendaIndex] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateRichiestaAziendaIndex] END.");
    }
  }
  
  /**
   * Ritorna i dati della richiesta e l'ultimo stato dell'iter attivo
   * solo per quelle non validate e annullate,
   * valido solo per validazione e cessazione!!!!
   * 
   * 
   * @param idAzienda
   * @param idTipoRichiesta
   * @return
   * @throws DataAccessException
   */
  public AziendaNuovaVO getRichAzByIdAzienda(
      long idAzienda, long idTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       RA.ID_AZIENDA, " +
        "       RA.NOTE, " +
        "       RA.ID_MOTIVO_DICHIARAZIONE, " +
        "       RA.ID_UTENTE_AGGIORNAMENTO, " +
        "       RA.ID_TIPO_RICHIESTA, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       IRA.ID_ITER_RICHIESTA_AZIENDA, " +
        "       TMD.DESCRIZIONE AS DESC_DICHIARAZIONE, " +
        "       IRA.DATA_AGGIORNAMENTO, " +
        "       TR.DESCRIZIONE AS DESC_RICHIESTA, " +
        "       TR.DESCRIZIONE_ESTESA AS DESC_RICHIESTA_ESTESA, " +
        "       RA.ID_CESSAZIONE, " +
        "       TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
        "       RA.DATA_CESSAZIONE," +
        "       RA.ID_AZIENDA_SUBENTRANTE, " +
        "       RA.CUAA_SUBENTRANTE, " +
        "       RA.DENOMINAZIONE_SUBENTRANTE, " +
        "       AA.CUAA, " +
        "       AA.DENOMINAZIONE, " +
        "       COM.DESCOM, " +
        "       AA1.MAIL, " +
        "       AA1.PEC, " +
        "       RA.FLAG_SOLO_AGGIUNTA " +
        "FROM   DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_ANAGRAFICA_AZIENDA AA1, " +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_TIPO_CESSAZIONE TC, " +
        "       COMUNE COM " +
        "WHERE  RA.ID_AZIENDA = ? " +
        "AND    RA.ID_TIPO_RICHIESTA = ? " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRA.ID_STATO_RICHIESTA <> 60 " +
        "AND    IRA.ID_STATO_RICHIESTA <> 50 " +
        "AND    RA.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE (+) " +
        "AND    RA.ID_CESSAZIONE = TC.ID_CESSAZIONE (+) " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    RA.ID_AZIENDA_SUBENTRANTE = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    RA.ID_AZIENDA = AA1.ID_AZIENDA " +
        "AND    AA1.DATA_FINE_VALIDITA IS NULL " +
        "AND    AA1.SEDELEG_COMUNE = COM.ISTAT_COMUNE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idTipoRichiesta);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaNuovaVO.setIdMotivoDichiarazione(checkIntegerNull(rs.getString("ID_MOTIVO_DICHIARAZIONE")));
        aziendaNuovaVO.setNote(rs.getString("NOTE"));
        aziendaNuovaVO.setIdStatoRichiesta(new Long(rs.getLong("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setIdTipoRichiesta(new Long(rs.getLong("ID_TIPO_RICHIESTA")));
        aziendaNuovaVO.setIdIterRichiestaAzienda(new Long(rs.getLong("ID_ITER_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setDescMotivoDichiarazione(rs.getString("DESC_DICHIARAZIONE"));
        aziendaNuovaVO.setDataAggiornamentoIter(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        aziendaNuovaVO.setDescTipoRichiesta(rs.getString("DESC_RICHIESTA"));
        aziendaNuovaVO.setDescEstesaTipoRichiesta(rs.getString("DESC_RICHIESTA_ESTESA"));
        aziendaNuovaVO.setIdUtenteAggiornamentoRich(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        aziendaNuovaVO.setIdCessazione(checkLongNull(rs.getString("ID_CESSAZIONE")));
        aziendaNuovaVO.setDescCessazione(rs.getString("DESC_CESSAZIONE"));
        aziendaNuovaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
        aziendaNuovaVO.setIdAziendaSubentrante(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRANTE")));
        String cuaaSubentrante = rs.getString("CUAA_SUBENTRANTE");
        String denominazioneSubentrante = rs.getString("DENOMINAZIONE_SUBENTRANTE");
        if(Validator.isNotEmpty(aziendaNuovaVO.getIdAziendaSubentrante()))
        {
          cuaaSubentrante = rs.getString("CUAA");
          denominazioneSubentrante = rs.getString("DENOMINAZIONE");
        }
        aziendaNuovaVO.setCuaaSubentrante(cuaaSubentrante);
        aziendaNuovaVO.setDenominazioneSubentrante(denominazioneSubentrante);
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM"));
        aziendaNuovaVO.setMail(rs.getString("MAIL"));
        aziendaNuovaVO.setPec(rs.getString("PEC"));
        aziendaNuovaVO.setFlagSoloAggiunta(rs.getString("FLAG_SOLO_AGGIUNTA"));
      }
      return aziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idTipoRichiesta", idTipoRichiesta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] END.");
    }
  }
  
  
  public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    AziendaNuovaVO aziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT RA.ID_RICHIESTA_AZIENDA, " +
        "       RA.ID_AZIENDA, " +
        "       RA.NOTE, " +
        "       RA.ID_MOTIVO_DICHIARAZIONE, " +
        "       RA.ID_UTENTE_AGGIORNAMENTO, " +
        "       RA.ID_TIPO_RICHIESTA, " +
        "       IRA.ID_STATO_RICHIESTA, " +
        "       IRA.ID_ITER_RICHIESTA_AZIENDA, " +
        "       TMD.DESCRIZIONE AS DESC_DICHIARAZIONE, " +
        "       IRA.DATA_AGGIORNAMENTO, " +
        "       TR.DESCRIZIONE AS DESC_RICHIESTA, " +
        "       TR.DESCRIZIONE_ESTESA AS DESC_RICHIESTA_ESTESA, " +
        "       RA.ID_CESSAZIONE, " +
        "       TC.DESCRIZIONE AS DESC_CESSAZIONE, " +
        "       RA.DATA_CESSAZIONE," +
        "       RA.ID_AZIENDA_SUBENTRANTE, " +
        "       RA.CUAA_SUBENTRANTE, " +
        "       RA.DENOMINAZIONE_SUBENTRANTE, " +
        "       AA.CUAA, " +
        "       AA.DENOMINAZIONE, " +
        "       COM.DESCOM, " +
        "       AA1.MAIL, " +
        "       AA1.PEC " +
        "FROM   DB_RICHIESTA_AZIENDA RA, " +
        "       DB_ITER_RICHIESTA_AZIENDA IRA, " +
        "       DB_TIPO_MOTIVO_DICHIARAZIONE TMD, " +
        "       DB_TIPO_RICHIESTA TR, " +
        "       DB_ANAGRAFICA_AZIENDA AA1, " +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_TIPO_CESSAZIONE TC, " +
        "       COMUNE COM " +
        "WHERE  RA.ID_AZIENDA = ? " +
        "AND    RA.ID_TIPO_RICHIESTA = ? " +
        "AND    RA.ID_RICHIESTA_AZIENDA = IRA.ID_RICHIESTA_AZIENDA " +
        "AND    IRA.DATA_FINE_VALIDITA IS NULL " +
        "AND    IRA.ID_STATO_RICHIESTA <> 60 " +
      //  "AND    IRA.ID_STATO_RICHIESTA <> 50 " +
        "AND    RA.ID_MOTIVO_DICHIARAZIONE = TMD.ID_MOTIVO_DICHIARAZIONE (+) " +
        "AND    RA.ID_CESSAZIONE = TC.ID_CESSAZIONE (+) " +
        "AND    RA.ID_TIPO_RICHIESTA = TR.ID_TIPO_RICHIESTA " +
        "AND    RA.ID_AZIENDA_SUBENTRANTE = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    RA.ID_AZIENDA = AA1.ID_AZIENDA " +
        "AND    AA1.DATA_FINE_VALIDITA IS NULL " +
        "AND    AA1.SEDELEG_COMUNE = COM.ISTAT_COMUNE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      stmt.setLong(++indice, idTipoRichiesta);
      ResultSet rs = stmt.executeQuery();
          
      if (rs.next())
      {
        aziendaNuovaVO = new AziendaNuovaVO();
        aziendaNuovaVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        aziendaNuovaVO.setIdMotivoDichiarazione(checkIntegerNull(rs.getString("ID_MOTIVO_DICHIARAZIONE")));
        aziendaNuovaVO.setNote(rs.getString("NOTE"));
        aziendaNuovaVO.setIdStatoRichiesta(new Long(rs.getLong("ID_STATO_RICHIESTA")));
        aziendaNuovaVO.setIdTipoRichiesta(new Long(rs.getLong("ID_TIPO_RICHIESTA")));
        aziendaNuovaVO.setIdIterRichiestaAzienda(new Long(rs.getLong("ID_ITER_RICHIESTA_AZIENDA")));
        aziendaNuovaVO.setDescMotivoDichiarazione(rs.getString("DESC_DICHIARAZIONE"));
        aziendaNuovaVO.setDataAggiornamentoIter(rs.getTimestamp("DATA_AGGIORNAMENTO"));
        aziendaNuovaVO.setDescTipoRichiesta(rs.getString("DESC_RICHIESTA"));
        aziendaNuovaVO.setDescEstesaTipoRichiesta(rs.getString("DESC_RICHIESTA_ESTESA"));
        aziendaNuovaVO.setIdUtenteAggiornamentoRich(checkLongNull(rs.getString("ID_UTENTE_AGGIORNAMENTO")));
        aziendaNuovaVO.setIdCessazione(checkLongNull(rs.getString("ID_CESSAZIONE")));
        aziendaNuovaVO.setDescCessazione(rs.getString("DESC_CESSAZIONE"));
        aziendaNuovaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
        aziendaNuovaVO.setIdAziendaSubentrante(checkLongNull(rs.getString("ID_AZIENDA_SUBENTRANTE")));
        String cuaaSubentrante = rs.getString("CUAA_SUBENTRANTE");
        String denominazioneSubentrante = rs.getString("DENOMINAZIONE_SUBENTRANTE");
        if(Validator.isNotEmpty(aziendaNuovaVO.getIdAziendaSubentrante()))
        {
          cuaaSubentrante = rs.getString("CUAA");
          denominazioneSubentrante = rs.getString("DENOMINAZIONE");
        }
        aziendaNuovaVO.setCuaaSubentrante(cuaaSubentrante);
        aziendaNuovaVO.setDenominazioneSubentrante(denominazioneSubentrante);
        aziendaNuovaVO.setDescComune(rs.getString("DESCOM"));
        aziendaNuovaVO.setMail(rs.getString("MAIL"));
        aziendaNuovaVO.setPec(rs.getString("PEC"));
      }
      return aziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
        new Variabile("aziendaNuovaVO", aziendaNuovaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idTipoRichiesta", idTipoRichiesta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRichAzByIdAzienda] END.");
    }
  }
  
  
  public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateRichiestaAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   UPDATE DB_RICHIESTA_AZIENDA   " +
        "   SET ID_MOTIVO_DICHIARAZIONE = ? , " +
        "       NOTE = ? , " +
        "       ID_UTENTE_AGGIORNAMENTO = ? , " +
        "       DATA_AGGIORNAMENTO = SYSDATE , " +
        "       ID_CESSAZIONE = ? , " +
        "       DATA_CESSAZIONE = ? , " +
        "       ID_AZIENDA_SUBENTRANTE = ?, " +
        "       CUAA_SUBENTRANTE = ?, " +
        "       DENOMINAZIONE_SUBENTRANTE = ?, " +
        "       FLAG_SOLO_AGGIUNTA = ? " +
        "   WHERE ID_RICHIESTA_AZIENDA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateRichiestaAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(richiestaAziendaVO.getIdMotivoDichiarazione()));
      stmt.setString(++indice, richiestaAziendaVO.getNote());
      stmt.setLong(++indice, richiestaAziendaVO.getIdUtenteAggiornamento().longValue());      
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdCessazione()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(richiestaAziendaVO.getDataCessazione()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(richiestaAziendaVO.getIdAziendaSubentrante()));
      stmt.setString(++indice, richiestaAziendaVO.getCuaaSubentrante());
      stmt.setString(++indice, richiestaAziendaVO.getDenominazioneSubentrante());
      stmt.setString(++indice, richiestaAziendaVO.getFlagSoloAggiunta());
      stmt.setLong(++indice, richiestaAziendaVO.getIdRichiestaAzienda().longValue());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("richiestaAziendaVO", richiestaAziendaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateRichiestaAzienda] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateRichiestaAzienda] END.");
    }
  }
  
  /**
   * 
   * Tira fuori tutti tranne il rapp legale!!!
   * 
   * 
   * @param idAziendaNuova
   * @return
   * @throws DataAccessException
   */
  public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova = null;
    SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getSoggettiAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SAN.ID_SOGGETTI_AZ_NUOVA, " +
        "       SAN.ID_AZIENDA_NUOVA, " +
        "       SAN.CODICE_FISCALE, " +
        "       SAN.COGNOME, " +
        "       SAN.NOME, " +
        "       SAN.COMUNE_RESIDENZA, " +
        "       COM.DESCOM, " +
        "       PROV.ISTAT_PROVINCIA, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       SAN.INDIRIZZO_RESIDENZA, " +
        "       SAN.TELEFONO, " +
        "       SAN.EMAIL, " +
        "       SAN.ID_RUOLO, " +
        "       SAN.DATA_INIZIO_RUOLO, " +
        "       SAN.CAP_RESIDENZA, " +
        "       TR.DESCRIZIONE AS DESC_TIPO_RUOLO " +
        "FROM   SMRGAA_W_SOGGETTI_AZ_NUOVA SAN, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM, " +
        "       DB_TIPO_RUOLO TR " +
        "WHERE  SAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    SAN.COMUNE_RESIDENZA = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND    SAN.ID_RUOLO <> 1 " +
        "AND    SAN.ID_RUOLO = TR.ID_RUOLO " +
        "ORDER BY SAN.CODICE_FISCALE, " +
        "         SAN.COGNOME, " +
        "         SAN.NOME ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getSoggettiAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vSoggettoAziendaNuova == null)
        {
          vSoggettoAziendaNuova = new Vector<SoggettoAziendaNuovaVO>();
        }
       
        
        soggettoAziendaNuovaVO = new SoggettoAziendaNuovaVO();
        soggettoAziendaNuovaVO.setIdSoggettiAziendaNuova(new Long(rs.getLong("ID_SOGGETTI_AZ_NUOVA")));
        soggettoAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        soggettoAziendaNuovaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        soggettoAziendaNuovaVO.setCognome(rs.getString("COGNOME"));
        soggettoAziendaNuovaVO.setNome(rs.getString("NOME"));
        soggettoAziendaNuovaVO.setIstatComune(rs.getString("COMUNE_RESIDENZA"));
        soggettoAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        soggettoAziendaNuovaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        soggettoAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        soggettoAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO_RESIDENZA"));
        soggettoAziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        soggettoAziendaNuovaVO.setEmail(rs.getString("EMAIL"));
        soggettoAziendaNuovaVO.setIdRuolo(checkIntegerNull(rs.getString("ID_RUOLO")));
        soggettoAziendaNuovaVO.setDataInizioRuolo(rs.getTimestamp("DATA_INIZIO_RUOLO"));
        soggettoAziendaNuovaVO.setCap(rs.getString("CAP_RESIDENZA"));
        soggettoAziendaNuovaVO.setDescTipoRuolo(rs.getString("DESC_TIPO_RUOLO"));
        
        
        vSoggettoAziendaNuova.add(soggettoAziendaNuovaVO);
        
      }
      
      
      
      return vSoggettoAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("soggettoAziendaNuovaVO", soggettoAziendaNuovaVO),
        new Variabile("vSoggettoAziendaNuova", vSoggettoAziendaNuova)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getSoggettiAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getSoggettiAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    SoggettoAziendaNuovaVO soggettoAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRappLegaleNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT SAN.ID_SOGGETTI_AZ_NUOVA, " +
        "       SAN.ID_AZIENDA_NUOVA, " +
        "       SAN.CODICE_FISCALE, " +
        "       SAN.COGNOME, " +
        "       SAN.NOME, " +
        "       SAN.COMUNE_RESIDENZA, " +
        "       COM.DESCOM, " +
        "       PROV.ISTAT_PROVINCIA, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       SAN.INDIRIZZO_RESIDENZA, " +
        "       SAN.TELEFONO, " +
        "       SAN.EMAIL, " +
        "       SAN.ID_RUOLO, " +
        "       SAN.DATA_INIZIO_RUOLO, " +
        "       SAN.CAP_RESIDENZA, " +
        "       TR.DESCRIZIONE AS DESC_TIPO_RUOLO " +
        "FROM   SMRGAA_W_SOGGETTI_AZ_NUOVA SAN, " +
        "       PROVINCIA PROV, " +
        "       COMUNE COM, " +
        "       DB_TIPO_RUOLO TR " +
        "WHERE  SAN.ID_AZIENDA_NUOVA =  ? " +
        "AND    SAN.COMUNE_RESIDENZA = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND    SAN.ID_RUOLO = 1 " +
        "AND    SAN.ID_RUOLO = TR.ID_RUOLO ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRappLegaleNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {        
        soggettoAziendaNuovaVO = new SoggettoAziendaNuovaVO();
        soggettoAziendaNuovaVO.setIdSoggettiAziendaNuova(new Long(rs.getLong("ID_SOGGETTI_AZ_NUOVA")));
        soggettoAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        soggettoAziendaNuovaVO.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        soggettoAziendaNuovaVO.setCognome(rs.getString("COGNOME"));
        soggettoAziendaNuovaVO.setNome(rs.getString("NOME"));
        soggettoAziendaNuovaVO.setIstatComune(rs.getString("COMUNE_RESIDENZA"));
        soggettoAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        soggettoAziendaNuovaVO.setIstatProvincia(rs.getString("ISTAT_PROVINCIA"));
        soggettoAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        soggettoAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO_RESIDENZA"));
        soggettoAziendaNuovaVO.setTelefono(rs.getString("TELEFONO"));
        soggettoAziendaNuovaVO.setEmail(rs.getString("EMAIL"));
        soggettoAziendaNuovaVO.setIdRuolo(checkIntegerNull(rs.getString("ID_RUOLO")));
        soggettoAziendaNuovaVO.setDataInizioRuolo(rs.getTimestamp("DATA_INIZIO_RUOLO"));
        soggettoAziendaNuovaVO.setCap(rs.getString("CAP_RESIDENZA"));
        soggettoAziendaNuovaVO.setDescTipoRuolo(rs.getString("DESC_TIPO_RUOLO"));        
      }
      
      
      
      return soggettoAziendaNuovaVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("soggettoAziendaNuovaVO", soggettoAziendaNuovaVO)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getRappLegaleNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getRappLegaleNuovaIscrizione] END.");
    }
  }
  
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<String, AzAssAziendaNuovaVO> hAssAzienda = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ASSOCIATE_AZ_NUOVE, " +
        "       AAN.ID_AZIENDA_NUOVA, " +
        "       AAN.DATA_INGRESSO, " +
        "       AAN.CODICE_ENTE, " +
        "       AAN.DATA_USCITA " +
        "FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE AAN " +
        "WHERE  AAN.ID_AZIENDA_NUOVA = ? ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(hAssAzienda == null)
        {
          hAssAzienda = new HashMap<String, AzAssAziendaNuovaVO>();
        }
       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(rs.getLong("ID_ASSOCIATE_AZ_NUOVE")));
        azAssAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setDataUscita(rs.getTimestamp("DATA_USCITA"));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));        
        
        hAssAzienda.put(azAssAziendaNuovaVO.getCodEnte(), azAssAziendaNuovaVO);
        
      }
      
      
      
      return hAssAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("hAssAzienda", hAssAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    HashMap<String, AzAssAziendaNuovaVO> hAssAzienda = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaRichVariazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ASSOCIATE_AZ_NUOVE, " +
        "       AAN.ID_AZIENDA_NUOVA, " +
        "       AAN.DATA_INGRESSO, " +
        "       AAN.CODICE_ENTE, " +
        "       AAN.DATA_USCITA, " +
        "       AAN.ID_AZIENDA_COLLEGATA " +
        "FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE AAN " +
        "WHERE  AAN.ID_RICHIESTA_AZIENDA = ? ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaRichVariazione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idRichiestaAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(hAssAzienda == null)
        {
          hAssAzienda = new HashMap<String, AzAssAziendaNuovaVO>();
        }
       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(rs.getLong("ID_ASSOCIATE_AZ_NUOVE")));
        azAssAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setDataUscita(rs.getTimestamp("DATA_USCITA"));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
        azAssAziendaNuovaVO.setIdAziendaCollegata(checkLongNull(rs.getString("ID_AZIENDA_COLLEGATA")));
        
        
        hAssAzienda.put(azAssAziendaNuovaVO.getCodEnte(), azAssAziendaNuovaVO);
        
      }
      
      
      
      return hAssAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("hAssAzienda", hAssAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaRichVariazione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaAziendaRichVariazione] END.");
    }
  }
  
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ASSOCIATE_AZ_NUOVE, " +
        "       AAN.ID_AZIENDA_NUOVA, " +
        "       AAN.DATA_INGRESSO, " +
        "       AAN.CODICE_ENTE, " +
        "       AAN.ID_AZIENDA_ASSOCIATA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.CUAA,SAN.CUAA) AS CUAA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.PARTITA_IVA,SAN.PARTITA_IVA) AS PARTITA_IVA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,SAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_INDIRIZZO,SAN.INDIRIZZO) AS INDIRIZZO, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_CAP,SAN.CAP) AS CAP, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, SAN.COMUNE) AS COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA " +
        "FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE AAN, " +
        "       SMRGAA_W_SOGG_ASS_AZ_NUOVA SAN, " +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV " +
        "WHERE  AAN.ID_AZIENDA_NUOVA = ? " +
        "AND    AAN.ID_SOGG_ASS_AZ_NUOVA = SAN.ID_SOGG_ASS_AZ_NUOVA (+) " +
        "AND    AAN.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    NVL2 (AAN.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, SAN.COMUNE) = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "ORDER BY NVL2(AAN.ID_AZIENDA_ASSOCIATA, AA.DENOMINAZIONE, SAN.DENOMINAZIONE), " +
        "         NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.CUAA,SAN.CUAA) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAssAzienda == null)
        {
          vAssAzienda = new Vector<AzAssAziendaNuovaVO>();
        }
       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(rs.getLong("ID_ASSOCIATE_AZ_NUOVE")));
        azAssAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
        azAssAziendaNuovaVO.setIdAziendaAssociata(checkLongNull(rs.getString("ID_AZIENDA_ASSOCIATA")));
        azAssAziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        azAssAziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        azAssAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        azAssAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        azAssAziendaNuovaVO.setCap(rs.getString("CAP"));
        azAssAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        azAssAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        azAssAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        
        vAssAzienda.add(azAssAziendaNuovaVO);
        
      }
      
      
      
      return vAssAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("vAssAzienda", vAssAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaNuovaIscrizione] END.");
    }
  }
  
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaRichVariazione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ASSOCIATE_AZ_NUOVE, " +
        "       AAN.ID_AZIENDA_NUOVA, " +
        "       AAN.DATA_INGRESSO, " +
        "       AAN.DATA_USCITA, " +
        "       AAN.CODICE_ENTE, " +
        "       AAN.ID_AZIENDA_ASSOCIATA, " +
        "       AAN.ID_AZIENDA_COLLEGATA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.CUAA,SAN.CUAA) AS CUAA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.PARTITA_IVA,SAN.PARTITA_IVA) AS PARTITA_IVA, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.DENOMINAZIONE,SAN.DENOMINAZIONE) AS DENOMINAZIONE, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_INDIRIZZO,SAN.INDIRIZZO) AS INDIRIZZO, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_CAP,SAN.CAP) AS CAP, " +
        "       NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.SEDELEG_COMUNE, SAN.COMUNE) AS COMUNE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       AAN.FLAG_ELIMINATO " +
        "FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE AAN, " +
        "       SMRGAA_W_SOGG_ASS_AZ_NUOVA SAN, " +
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV " +
        "WHERE  AAN.ID_RICHIESTA_AZIENDA = ? " +
        "AND    AAN.ID_SOGG_ASS_AZ_NUOVA = SAN.ID_SOGG_ASS_AZ_NUOVA (+) " +
        "AND    AAN.ID_AZIENDA_ASSOCIATA = AA.ID_AZIENDA (+) " +
        "AND    AA.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    NVL2 (AAN.ID_AZIENDA_ASSOCIATA, AA.SEDELEG_COMUNE, SAN.COMUNE) = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "ORDER BY NVL2(AAN.ID_AZIENDA_ASSOCIATA, AA.DENOMINAZIONE, SAN.DENOMINAZIONE), " +
        "         NVL2(AAN.ID_AZIENDA_ASSOCIATA,AA.CUAA,SAN.CUAA), AAN.CODICE_ENTE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaRichVariazione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idRichiestaAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAssAzienda == null)
        {
          vAssAzienda = new Vector<AzAssAziendaNuovaVO>();
        }
       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(rs.getLong("ID_ASSOCIATE_AZ_NUOVE")));
        azAssAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setDataUscita(rs.getTimestamp("DATA_USCITA"));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
        azAssAziendaNuovaVO.setIdAziendaAssociata(checkLongNull(rs.getString("ID_AZIENDA_ASSOCIATA")));
        azAssAziendaNuovaVO.setIdAziendaCollegata(checkLongNull(rs.getString("ID_AZIENDA_COLLEGATA")));
        azAssAziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        azAssAziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        azAssAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        azAssAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        azAssAziendaNuovaVO.setCap(rs.getString("CAP"));
        azAssAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        azAssAziendaNuovaVO.setDesCom(rs.getString("DESCOM"));
        azAssAziendaNuovaVO.setSglProv(rs.getString("SIGLA_PROVINCIA"));
        azAssAziendaNuovaVO.setFlagEliminato(rs.getString("FLAG_ELIMINATO"));
        
        vAssAzienda.add(azAssAziendaNuovaVO);
        
      }
      
      
      
      return vAssAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("vAssAzienda", vAssAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaRichVariazione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateAziendaRichVariazione] END.");
    }
  }
  
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AzAssAziendaNuovaVO> vAssAzienda = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaStampaAziendaNuovaIscrizione] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AAN.ID_ASSOCIATE_AZ_NUOVE, " +
        "       AAN.ID_AZIENDA_NUOVA, " +
        "       AAN.DATA_INGRESSO, " +
        "       AAN.CODICE_ENTE, " +
        "       DI.EXT_CUAA AS CUAA, " +
        "       DI.PARTITA_IVA, " +
        "       DI.DENOMINAZIONE " +
        "FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE AAN, " +
        "       DB_INTERMEDIARIO DI " +
        "WHERE  AAN.ID_AZIENDA_NUOVA = ? " +
        "AND    AAN.CODICE_ENTE = DI.CODICE_FISCALE " +
        "AND    DI.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY DI.DENOMINAZIONE ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaStampaAziendaNuovaIscrizione] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAziendaNuova);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAssAzienda == null)
        {
          vAssAzienda = new Vector<AzAssAziendaNuovaVO>();
        }
       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAssociateAzNuove(new Long(rs.getLong("ID_ASSOCIATE_AZ_NUOVE")));
        azAssAziendaNuovaVO.setIdAziendaNuova(new Long(rs.getLong("ID_AZIENDA_NUOVA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_ENTE"));
        azAssAziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        azAssAziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        azAssAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        
        vAssAzienda.add(azAssAziendaNuovaVO);
        
      }
      
      
      
      return vAssAzienda;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("vAssAzienda", vAssAzienda)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAziendaNuova", idAziendaNuova) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaStampaAziendaNuovaIscrizione] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAziendeAssociateCaaStampaAziendaNuovaIscrizione] END.");
    }
  }
  
  public Vector<MacchinaAziendaNuovaVO> getMacchineForImportAzNuova(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<MacchinaAziendaNuovaVO> vMacchine = null;
    MacchinaAziendaNuovaVO macchinaAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineForImportAzNuova] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT PM.ID_MACCHINA, " +
        "       PM.ID_UTE, " +
        "       MG.ID_GENERE_MACCHINA, " +
        "       MG.ID_CATEGORIA, " +
        "       MG.ID_MARCA, " +
        "       MG.ANNO_COSTRUZIONE, " +
        "       MG.MATRICOLA_TELAIO, " +
        "       MG.TIPO_MACCHINA, " +
        "       PM.ID_TIPO_FORMA_POSSESSO, " +
        "       PM.PERCENTUALE_POSSESSO, " +
        "       PM.DATA_CARICO " + 
        "FROM   DB_POSSESSO_MACCHINA PM, " +
        "       DB_ANAGRAFICA_AZIENDA AA," +
        "       DB_UTE UT," +
        "       DB_MACCHINA_GAA MG," +
        "       DB_TIPO_GENERE_MACCHINA_GAA GMG " +
        "WHERE  AA.ID_AZIENDA = ? " +
        "AND    AA.ID_AZIENDA = UT.ID_AZIENDA " +
        "AND    AA.DATA_FINE_VALIDITA IS NULL " +
        "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
        "AND    UT.ID_UTE = PM.ID_UTE " +
        "AND    PM.DATA_FINE_VALIDITA IS NULL " +
        "AND    PM.DATA_SCARICO IS NULL " +
        "AND    PM.ID_MACCHINA = MG.ID_MACCHINA " +
        "AND    MG.ID_GENERE_MACCHINA = GMG.ID_GENERE_MACCHINA " +
        "AND    GMG.ID_TIPO_MACCHINA = 1 ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineForImportAzNuova] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vMacchine == null)
        {
          vMacchine = new Vector<MacchinaAziendaNuovaVO>();
        }       
        
        macchinaAziendaNuovaVO = new MacchinaAziendaNuovaVO();
        macchinaAziendaNuovaVO.setIdMacchina(new Long(rs.getLong("ID_MACCHINA")));
        macchinaAziendaNuovaVO.setIdUte(new Long(rs.getLong("ID_UTE")));
        macchinaAziendaNuovaVO.setIdGenereMacchina(new Long(rs.getLong("ID_GENERE_MACCHINA")));
        macchinaAziendaNuovaVO.setIdCategoria(checkLongNull(rs.getString("ID_CATEGORIA")));
        macchinaAziendaNuovaVO.setIdMarca(checkLongNull(rs.getString("ID_MARCA")));
        macchinaAziendaNuovaVO.setAnnoCostruzione(checkIntegerNull(rs.getString("ANNO_COSTRUZIONE")));
        macchinaAziendaNuovaVO.setMatricolaTelaio(rs.getString("MATRICOLA_TELAIO"));
        macchinaAziendaNuovaVO.setTipoMacchina(rs.getString("TIPO_MACCHINA"));
        macchinaAziendaNuovaVO.setIdTipoFormaPossesso(new Long(rs.getLong("ID_TIPO_FORMA_POSSESSO")));
        macchinaAziendaNuovaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        macchinaAziendaNuovaVO.setDataCarico(rs.getTimestamp("DATA_CARICO"));
        
        
        vMacchine.add(macchinaAziendaNuovaVO);
        
      }
      
      
      
      return vMacchine;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("macchinaAziendaNuovaVO", macchinaAziendaNuovaVO),
        new Variabile("vMacchine", vMacchine)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getMacchineForImportAzNuova] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineForImportAzNuova] END.");
    }
  }
  
  public Vector<AzAssAziendaNuovaVO> getAzzAssForImportAzNuova(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AzAssAziendaNuovaVO> vAzzAss = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssForImportAzNuova] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AC.ID_AZIENDA_ASSOCIATA, " +
        "       AC.DATA_INGRESSO, " +
        "       SA.ID_SOGGETTO_ASSOCIATO, " +
        "       AC.ID_AZIENDA_COLLEGATA, " +
        "       SA.CUAA, " +
        "       SA.PARTITA_IVA, " +
        "       SA.DENOMINAZIONE," +
        "       SA.COMUNE, " +
        "       SA.INDIRIZZO, " +
        "       SA.CAP " +
        "FROM   DB_AZIENDA_COLLEGATA AC, " +
        "       DB_SOGGETTO_ASSOCIATO SA " +
        "WHERE  AC.ID_AZIENDA = ? " +
        "AND    AC.DATA_FINE_VALIDITA IS NULL " +
        "AND    AC.DATA_USCITA IS NULL " +
        "AND    AC.ID_SOGGETTO_ASSOCIATO = SA.ID_SOGGETTO_ASSOCIATO (+) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssForImportAzNuova] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAzzAss == null)
        {
          vAzzAss = new Vector<AzAssAziendaNuovaVO>();
        }       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAziendaAssociata(checkLongNull(rs.getString("ID_AZIENDA_ASSOCIATA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setIdSoggettoAssociato(checkLongNull(rs.getString("ID_SOGGETTO_ASSOCIATO")));
        azAssAziendaNuovaVO.setIdAziendaCollegata(checkLongNull(rs.getString("ID_AZIENDA_COLLEGATA")));
        azAssAziendaNuovaVO.setCuaa(rs.getString("CUAA"));
        azAssAziendaNuovaVO.setPartitaIva(rs.getString("PARTITA_IVA"));
        azAssAziendaNuovaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        azAssAziendaNuovaVO.setIstatComune(rs.getString("COMUNE"));
        azAssAziendaNuovaVO.setIndirizzo(rs.getString("INDIRIZZO"));
        azAssAziendaNuovaVO.setCap(rs.getString("CAP"));
        
        
        
        
        vAzzAss.add(azAssAziendaNuovaVO);
        
      }
      
      
      
      return vAzzAss;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("vAzzAss", vAzzAss)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAzzAssForImportAzNuova] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssForImportAzNuova] END.");
    }
  }
  
  
  public Vector<AzAssAziendaNuovaVO> getAzzAssCaaForImportAzNuova(long idAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<AzAssAziendaNuovaVO> vAzzAss = null;
    AzAssAziendaNuovaVO azAssAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssCaaForImportAzNuova] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT AC.ID_AZIENDA_ASSOCIATA, " +
        "       AC.DATA_INGRESSO, " +
        "       AC.ID_AZIENDA_COLLEGATA, " +
        "       DI.CODICE_FISCALE " +
        "FROM   DB_AZIENDA_COLLEGATA AC, " +
        "       DB_INTERMEDIARIO DI " +
        "WHERE  AC.ID_AZIENDA = ? " +
        "AND    AC.DATA_FINE_VALIDITA IS NULL " +
        "AND    AC.DATA_USCITA IS NULL " +
        "AND    AC.ID_AZIENDA_ASSOCIATA IS NOT NULL " +
        "AND    AC.ID_AZIENDA_ASSOCIATA = DI.EXT_ID_AZIENDA ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssCaaForImportAzNuova] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vAzzAss == null)
        {
          vAzzAss = new Vector<AzAssAziendaNuovaVO>();
        }       
        
        azAssAziendaNuovaVO = new AzAssAziendaNuovaVO();
        azAssAziendaNuovaVO.setIdAziendaAssociata(checkLongNull(rs.getString("ID_AZIENDA_ASSOCIATA")));
        azAssAziendaNuovaVO.setDataIngresso(rs.getTimestamp("DATA_INGRESSO"));
        azAssAziendaNuovaVO.setIdAziendaCollegata(checkLongNull(rs.getString("ID_AZIENDA_COLLEGATA")));
        azAssAziendaNuovaVO.setCodEnte(rs.getString("CODICE_FISCALE"));       
        
        vAzzAss.add(azAssAziendaNuovaVO);
        
      }
      
      
      
      return vAzzAss;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("azAssAziendaNuovaVO", azAssAziendaNuovaVO),
        new Variabile("vAzzAss", vAzzAss)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getAzzAssCaaForImportAzNuova] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getAzzAssCaaForImportAzNuova] END.");
    }
  }
  
  public void deleteMacchineImport(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteMacchineImport] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_MACCHINE_AZ_NUOVA " +
        "WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "AND    ID_MACCHINA IS NOT NULL ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteMacchineImport] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteMacchineImport] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteMacchineImport] END.");
    }
  }
  
  
  public void deleteAzzAssImport(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzzAssImport] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "AND    ID_AZIENDA_COLLEGATA IS NOT NULL ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteAzzAssImport] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteAzzAssImport] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzzAssImport] END.");
    }
  }
  
  
  public void deleteSoggAzzAssImport(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssImport] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_SOGG_ASS_AZ_NUOVA " +
        "WHERE  ID_SOGG_ASS_AZ_NUOVA IN (SELECT ID_SOGG_ASS_AZ_NUOVA " +
        "                                FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "                                WHERE  ID_RICHIESTA_AZIENDA = ? )" +
        "AND    ID_SOGGETTO_ASSOCIATO IS NOT NULL ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteSoggAzzAssImport] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssImport] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssImport] END.");
    }
  }
  
  
  public void deleteMacchineInsert(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteMacchineInsert] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE DB_MACCHINE_AZ_NUOVA " +
        "WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "AND    ID_MACCHINA IS NULL ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteMacchineInsert] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteMacchineInsert] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteMacchineInsert] END.");
    }
  }
  
  public void deleteAzzAssInsert(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzzAssInsert] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "AND    ID_AZIENDA_COLLEGATA IS NULL ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteAzzAssInsert] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteAzzAssInsert] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteAzzAssInsert] END.");
    }
  }
  
  public void deleteSoggAzzAssInsert(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssInsert] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "DELETE SMRGAA_W_SOGG_ASS_AZ_NUOVA " +
        "WHERE  ID_SOGG_ASS_AZ_NUOVA IN (SELECT ID_SOGG_ASS_AZ_NUOVA " +
        "                                FROM   SMRGAA_W_ASSOCIATE_AZ_NUOVE " +
        "                                WHERE  ID_RICHIESTA_AZIENDA = ? " +
        "                                AND    ID_AZIENDA_COLLEGATA IS NULL " +
        "                                AND    ID_SOGG_ASS_AZ_NUOVA IS NOT NULL )");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::deleteSoggAzzAssInsert] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idRichiestaAzienda);
      
      
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssInsert] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::deleteSoggAzzAssInsert] END.");
    }
  }
  
  
  public Long insertMacchinaAziendaNuova(MacchinaAziendaNuovaVO macchinaAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMacchineAziendaNuova = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertMacchinaAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idMacchineAziendaNuova = getNextPrimaryKey(SolmrConstants.SEQ_DB_MACCHINE_AZ_NUOVA);
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "   INSERT INTO DB_MACCHINE_AZ_NUOVA   " +
        "      (ID_MACCHINE_AZ_NUOVA, " +
        "       ID_RICHIESTA_AZIENDA, " +
        "       ID_MACCHINA, " +
        "       ID_UTE, " +
        "       ID_GENERE_MACCHINA, " +
        "       ID_CATEGORIA, " +
        "       ID_MARCA, " +
        "       ANNO_COSTRUZIONE, " +
        "       MATRICOLA_TELAIO, " +
        "       TIPO_MACCHINA, " +
        "       ID_TIPO_FORMA_POSSESSO, " +
        "       PERCENTUALE_POSSESSO, " +
        "       DATA_CARICO, " +
        "       DATA_SCARICO) " +
        "   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::insertMacchinaAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idMacchineAziendaNuova.longValue());
      stmt.setLong(++indice, macchinaAziendaNuovaVO.getIdRichiestaAzienda());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaAziendaNuovaVO.getIdMacchina()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaAziendaNuovaVO.getIdUte()));
      stmt.setLong(++indice, macchinaAziendaNuovaVO.getIdGenereMacchina());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaAziendaNuovaVO.getIdCategoria()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaAziendaNuovaVO.getIdMarca()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaAziendaNuovaVO.getAnnoCostruzione()));
      stmt.setString(++indice, macchinaAziendaNuovaVO.getMatricolaTelaio());
      stmt.setString(++indice, macchinaAziendaNuovaVO.getTipoMacchina());
      stmt.setLong(++indice, macchinaAziendaNuovaVO.getIdTipoFormaPossesso());
      stmt.setBigDecimal(++indice, macchinaAziendaNuovaVO.getPercentualePossesso());
      stmt.setTimestamp(++indice, convertDateToTimestamp(macchinaAziendaNuovaVO.getDataCarico()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(macchinaAziendaNuovaVO.getDataScarico()));
      
      stmt.executeUpdate();
      
      return idMacchineAziendaNuova;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idMacchineAziendaNuova", idMacchineAziendaNuova)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("macchinaAziendaNuovaVO", macchinaAziendaNuovaVO)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::insertMacchinaAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::insertMacchinaAziendaNuova] END.");
    }
  }
  
  
  public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<MacchinaAziendaNuovaVO> vMacchine = null;
    MacchinaAziendaNuovaVO macchinaAziendaNuovaVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineAzNuova] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT MA.ID_MACCHINE_AZ_NUOVA, " +
        "       MA.ID_RICHIESTA_AZIENDA, " +
        "       MA.ID_MACCHINA, " +
        "       MA.ID_UTE, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       UT.INDIRIZZO, " +
        "       MA.ID_GENERE_MACCHINA, " +
        "       TGM.DESCRIZIONE AS DESC_GEN_MACCHINA, " +
        "       MA.ID_CATEGORIA, " +
        "       TC.DESCRIZIONE AS DESC_CATEGORIA, " +
        "       MA.ID_MARCA, " +
        "       TMG.DESCRIZIONE AS DESC_MARCA, " +
        "       MA.ANNO_COSTRUZIONE, " +
        "       MA.MATRICOLA_TELAIO, " +
        "       MA.TIPO_MACCHINA, " +
        "       MA.ID_TIPO_FORMA_POSSESSO, " +
        "       TFP.DESCRIZIONE AS DESC_FORMA_POSSESSO, " +
        "       MA.PERCENTUALE_POSSESSO, " +
        "       MA.DATA_CARICO, " +
        "       MA.DATA_SCARICO " + 
        "FROM   DB_MACCHINE_AZ_NUOVA MA, " +
        "       DB_TIPO_GENERE_MACCHINA_GAA TGM, " +
        "       DB_TIPO_CATEGORIA_GAA TC, " +
        "       DB_UTE UT, " +
        "       DB_TIPO_FORMA_POSSESSO_GAA TFP, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV, " +
        "       DB_TIPO_MARCA_GAA TMG " +
        "WHERE  MA.ID_RICHIESTA_AZIENDA = ? " +
        "AND    MA.ID_UTE = UT.ID_UTE  " +
        "AND    UT.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
        "AND    MA.ID_GENERE_MACCHINA = TGM.ID_GENERE_MACCHINA " +
        "AND    MA.ID_CATEGORIA = TC.ID_CATEGORIA (+) " +
        "AND    MA.ID_MARCA = TMG.ID_MARCA (+) " +
        "AND    MA.ID_TIPO_FORMA_POSSESSO = TFP.ID_TIPO_FORMA_POSSESSO " +
        "ORDER BY TGM.DESCRIZIONE, " +
        "         TC.DESCRIZIONE, " +
        "         MA.DATA_CARICO ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineAzNuova] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, idRichiestaAzienda);
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vMacchine == null)
        {
          vMacchine = new Vector<MacchinaAziendaNuovaVO>();
        }       
        
        macchinaAziendaNuovaVO = new MacchinaAziendaNuovaVO();
        macchinaAziendaNuovaVO.setIdMacchineAzNuova(new Long(rs.getLong("ID_MACCHINE_AZ_NUOVA")));
        macchinaAziendaNuovaVO.setIdRichiestaAzienda(new Long(rs.getLong("ID_RICHIESTA_AZIENDA")));
        macchinaAziendaNuovaVO.setIdMacchina(checkLongNull(rs.getString("ID_MACCHINA")));
        macchinaAziendaNuovaVO.setIdUte(checkLongNull(rs.getString("ID_UTE")));
        String descUte = rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+") - "+rs.getString("INDIRIZZO");
        macchinaAziendaNuovaVO.setDescUte(descUte);
        macchinaAziendaNuovaVO.setIdGenereMacchina(new Long(rs.getLong("ID_GENERE_MACCHINA")));
        macchinaAziendaNuovaVO.setDescGenereMacchina(rs.getString("DESC_GEN_MACCHINA"));
        macchinaAziendaNuovaVO.setIdCategoria(checkLongNull(rs.getString("ID_CATEGORIA")));
        macchinaAziendaNuovaVO.setDescCategoria(rs.getString("DESC_CATEGORIA"));
        macchinaAziendaNuovaVO.setIdMarca(checkLongNull(rs.getString("ID_MARCA")));
        macchinaAziendaNuovaVO.setDescMarca(rs.getString("DESC_MARCA"));
        macchinaAziendaNuovaVO.setAnnoCostruzione(checkIntegerNull(rs.getString("ANNO_COSTRUZIONE")));
        macchinaAziendaNuovaVO.setMatricolaTelaio(rs.getString("MATRICOLA_TELAIO"));
        macchinaAziendaNuovaVO.setTipoMacchina(rs.getString("TIPO_MACCHINA"));
        macchinaAziendaNuovaVO.setIdTipoFormaPossesso(new Long(rs.getLong("ID_TIPO_FORMA_POSSESSO")));
        macchinaAziendaNuovaVO.setDescTipoFormaPossesso(rs.getString("DESC_FORMA_POSSESSO"));
        macchinaAziendaNuovaVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        macchinaAziendaNuovaVO.setDataCarico(rs.getTimestamp("DATA_CARICO"));
        macchinaAziendaNuovaVO.setDataScarico(rs.getTimestamp("DATA_SCARICO"));
        
        vMacchine.add(macchinaAziendaNuovaVO);
        
      }
      
      
      
      return vMacchine;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("macchinaAziendaNuovaVO", macchinaAziendaNuovaVO),
        new Variabile("vMacchine", vMacchine)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idRichiestaAzienda", idRichiestaAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getMacchineAzNuova] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getMacchineAzNuova] END.");
    }
  }
  
  public void updateMacchinaAziendaNuova(MacchinaAziendaNuovaVO macchinaAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateMacchinaAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE DB_MACCHINE_AZ_NUOVA   " +
        "   SET DATA_SCARICO = ? " +
        "WHERE ID_RICHIESTA_AZIENDA = ? " +
        "AND   ID_MACCHINA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateMacchinaAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setTimestamp(++indice, convertDateToTimestamp(macchinaAziendaNuovaVO.getDataScarico()));
      stmt.setLong(++indice, macchinaAziendaNuovaVO.getIdRichiestaAzienda());
      stmt.setLong(++indice, macchinaAziendaNuovaVO.getIdMacchina());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("macchinaAziendaNuovaVO", macchinaAziendaNuovaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateMacchinaAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateMacchinaAziendaNuova] END.");
    }
  }
  
  public void updateEliminaAzAssAzNuova(AzAssAziendaNuovaVO azAssAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateEliminaAzAssAzNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE SMRGAA_W_ASSOCIATE_AZ_NUOVE   " +
        "   SET FLAG_ELIMINATO = 'S' " +
        "WHERE ID_ASSOCIATE_AZ_NUOVE = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateEliminaAzAssAzNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, azAssAziendaNuovaVO.getIdAssociateAzNuove());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("azAssAziendaNuovaVO", azAssAziendaNuovaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateEliminaAzAssAzNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateEliminaAzAssAzNuova] END.");
    }
  }
  
  public void updateAzAssAzNuova(AzAssAziendaNuovaVO azAssAziendaNuovaVO) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateAzAssAzNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE SMRGAA_W_ASSOCIATE_AZ_NUOVE   " +
        "   SET DATA_INGRESSO = ?, " +
        "       DATA_USCITA = ? " +
        "WHERE ID_ASSOCIATE_AZ_NUOVE = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateAzAssAzNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setTimestamp(++indice, convertDateToTimestamp(azAssAziendaNuovaVO.getDataIngresso()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(azAssAziendaNuovaVO.getDataUscita()));
      stmt.setLong(++indice, azAssAziendaNuovaVO.getIdAssociateAzNuove());
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("azAssAziendaNuovaVO", azAssAziendaNuovaVO) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateAzAssAzNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateAzAssAzNuova] END.");
    }
  }
  
  
  public Long getIdMacchinaSuDb(MacchinaAziendaNuovaVO macchinaNuovaVO) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idMacchina = null;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIdMacchinaSuDb] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT MG.ID_MACCHINA " +
        "FROM   DB_MACCHINA_GAA MG " +
        "WHERE  MG.ID_GENERE_MACCHINA = ? " +
        "AND    NVL(MG.ID_CATEGORIA,-1)  = NVL(?, -1) " +
        "AND    NVL(MG.ID_MARCA,-1)  = NVL(?, -1) " +
        "AND    NVL(MG.MATRICOLA_TELAIO, -1)  = NVL(?, -1) " +
        "AND    NVL(MG.ANNO_COSTRUZIONE, -1)  = NVL(?, -1) " +
        "AND    NVL(MG.TIPO_MACCHINA, -1)  = NVL(?, -1) ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIdMacchinaSuDb] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setLong(++indice, macchinaNuovaVO.getIdGenereMacchina().longValue());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaNuovaVO.getIdCategoria()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(macchinaNuovaVO.getIdMarca()));
      stmt.setString(++indice, macchinaNuovaVO.getMatricolaTelaio());
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(macchinaNuovaVO.getAnnoCostruzione()));
      stmt.setString(++indice, macchinaNuovaVO.getTipoMacchina());
      
      ResultSet rs = stmt.executeQuery();
      
      int count = 0;
      while (rs.next())
      {
        idMacchina = new Long(rs.getLong("ID_MACCHINA"));
        count++;
      }
      
      //se ottengo più risultati nn va bene!!!
      if(count > 1)
        idMacchina = null;
      
      return idMacchina;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("idMacchina", idMacchina) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("macchinaNuovaVO", macchinaNuovaVO) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::getIdMacchinaSuDb] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::getIdMacchinaSuDb] END.");
    }
  }
  
  
  public void updateIdMacchinaNuovaMacchinaAziendaNuova(long idMacchineAzNuova, long idMacchina) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateIdMacchinaNuovaMacchinaAziendaNuova] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "UPDATE DB_MACCHINE_AZ_NUOVA   " +
        "   SET ID_MACCHINA_NUOVA = ? " +
        "WHERE ID_MACCHINE_AZ_NUOVA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[NuovaIscrizioneDAO::updateIdMacchinaNuovaMacchinaAziendaNuova] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      
      stmt.setLong(++indice, idMacchina);
      stmt.setLong(++indice, idMacchineAzNuova);
      
      stmt.executeUpdate();
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
          { new Parametro("idMacchineAzNuova", idMacchineAzNuova),
            new Parametro("idMacchina", idMacchina)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[NuovaIscrizioneDAO::updateIdMacchinaNuovaMacchinaAziendaNuova] ",
              t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[NuovaIscrizioneDAO::updateIdMacchinaNuovaMacchinaAziendaNuova] END.");
    }
  }
  
  
  public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean isAbilitato = false;
    
    try
    {
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isUtenteAbilitatoPresaInCarico] BEGIN.");

      queryBuf = new StringBuffer();

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      queryBuf.append(
        "SELECT FLAG_PRESA_IN_CARICO " +
        "FROM  DB_R_RUOLO_TIPO_RICHIESTA " +
        "WHERE CODICE_RUOLO = ? " +
        "AND   ID_TIPO_RICHIESTA = ? ");
      
     
      /* CONCATENAZIONE/CREAZIONE QUERY END. */

      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isUtenteAbilitatoPresaInCarico] Query=" + query);
      }
      stmt = conn.prepareStatement(query);
      int indice = 0;

      // Setto i parametri della query
      stmt.setString(++indice, codiceRuolo);
      stmt.setLong(++indice, idTipoRichiesta);
      
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next())
      {
        String flagPresaInCarico = rs.getString("FLAG_PRESA_IN_CARICO");
        
        if("S".equalsIgnoreCase(flagPresaInCarico))
          isAbilitato = true;        
      }
      
      
      
      return isAbilitato;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("isAbilitato", isAbilitato)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceRuolo", codiceRuolo),
        new Parametro("idTipoRichiesta", idTipoRichiesta) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this, "[NuovaIscrizioneDAO::isUtenteAbilitatoPresaInCarico] ", t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come GaaservInternalException. Per
       * informazione sui codici di errore utilizzati vedere il javadoc di
       * BaseDAO.convertIntoGaaservInternalException()
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatemente (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this, "[NuovaIscrizioneDAO::isUtenteAbilitatoPresaInCarico] END.");
    }
  }
  
  
  
  
  
}