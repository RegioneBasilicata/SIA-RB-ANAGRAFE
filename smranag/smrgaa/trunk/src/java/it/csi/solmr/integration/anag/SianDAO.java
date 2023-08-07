package it.csi.solmr.integration.anag;

import it.csi.cciaa.albovigneti.DatiUv;
import it.csi.sian.ricercaAnagrafica.ISAttivitaPro2P;
import it.csi.sian.ricercaAnagrafica.ISDatiAnagraficiPro2P;
import it.csi.sian.ricercaAnagrafica.ISDittaIndividualePro2P;
import it.csi.sian.ricercaAnagrafica.ISPersonaFisicaPro2P;
import it.csi.sian.ricercaAnagrafica.ISSoggettoPro2P;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDati;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiBoundingBox;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiConfrontoCatastoCensuario;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiDatiCatastoCensuario;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiDatiGIS;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiDatiGISInfoParticella;
import it.csi.sian.validazioneGIS.OutputValidazioneGISDatiDatiGISUsoSuoloPoligono;
import it.csi.sian.validazioneGIS.UnitArborea;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.ws.cciaa.DatiUvCCIAA;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.DoubleStringcodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.RespAnagFascicoloVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaAtecoSecGaaVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.terreni.UnitaArboreaCCIAAVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

// <p>Title: S.O.L.M.R.</p>
// <p>Description: Servizi On-Line per il Mondo Rurale</p>
// <p>Copyright: Copyright (c) 2003</p>
// <p>Company: TOBECONFIG</p>
// @author: Mauro Vocale
// @version 1.0

public class SianDAO extends BaseDAO
{

  public SianDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public SianDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  // Metodo per eliminare un record dalla tabella DB_AZIENDA_TRIBUTARIA
  public void deleteAziendaTributaria(String cuaa) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_AZIENDA_TRIBUTARIA " + " WHERE  CUAA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setString(1, cuaa.toUpperCase());

      SolmrLogger.debug(this, "Executing deleteAziendaTributaria: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteAziendaTributaria with CUAA = "
          + cuaa.toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteAziendaTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  public void deleteAtecoSecTributaria(long idAziendaTributaria) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_ATECO_SEC_TRIBUTARIA " + " WHERE  ID_AZIENDA_TRIBUTARIA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setLong(1, idAziendaTributaria);

      SolmrLogger.debug(this, "Executing deleteAtecoSecTributaria: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteAtecoSecTributaria with idAziendaTributaria = "
          + idAziendaTributaria);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteAtecoSecTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteAtecoSecTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteAtecoSecTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteAtecoSecTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  
  //metodo per il servizio nuovo ricerca anagrafica
  public Long insertAziendaTributaria(ISDatiAnagraficiPro2P isDatiAnagraficiPro2P,
      SianAnagTributariaVO sianAnagTributariaVO) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ISAttivitaPro2P[] arrAttivita = null;

    try
    {
      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_AZIENDA_TRIBUTARIA"));
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_AZIENDA_TRIBUTARIA " +
          "            (ID_AZIENDA_TRIBUTARIA, " +  //1
          "             CUAA, " +  //2
          "             FLAG_PRESENTE_AT, " + //3 
          "             DENOMINAZIONE," +  //4
          "             COGNOME, " +  //5
          "             NOME, " + //6
          "             DATA_NASCITA, " +  //7
          "             COMUNE_NASCITA, " +  //8
          "             PROVINCIA_NASCITA, " +  //9
          "             SESSO, " + //10
          "             INDIRIZZO, " +  //11
          "             COMUNE, " +  //12
          "             PROVINCIA, " +  //13
          "             CAP, " +  //14
          "             DATA_DECESSO, " +  //15
          "             PARTITA_IVA, " + //16
          "             FORMA_GIURIDICA, " +  //17
          "             DATA_AGGIORNAMENTO, " +
          "             CUAA_ANAGRAFE_TRIBUTARIA, " +  //18
          "             MESSAGGIO_ERRORE, " +  //19
          "             COMUNE_SEDE_LEGALE, " +  //20
          "             PROVINCIA_SEDE_LEGALE, " +  //21
          "             INDIRIZZO_SEDE_LEGALE, " +  //22
          "             CAP_SEDE_LEGALE, " +  //23
          "             COMUNE_DOMICILIO_FISCALE, " +  //24
          "             PROVINCIA_DOMICILIO_FISCALE, " +  //25
          "             INDIRIZZO_DOMICILIO_FISCALE, " +  //26
          "             CAP_DOMICILIO_FISCALE, " +  //27
          "             DATA_INIZIO_ATTIVITA, " +  //28
          "             STATO_ATTIVITA, " +  //29
          "             ID_ATTIVITA_ATECO, " + //30
          "             CODICE_ATECO, " + //31
          "             DESCRIZIONE_STATO_ATTIVITA, " +  //32
          "             TIPO_ATTIVITA, " +
          "             TIPO_CARICA, " +
          "             DESCRIZIONE_TIPO_CARICA, " +
          "             CODICE_FISCALE_RAPPRESENTANTE, " +
          "             DENOMINAZIONE_RAPPRESENTANTE, " +
          "             DATA_DECESSO_RAPPRESENTANTE, " +
          "             COGNOME_RAPPRESENTANTE, " +
          "             NOME_RAPPRESENTANTE, " +
          "             FLAG_PRESENTE_GAA, " +
          "             DATA_AGGIORNAMENTO_GAA) " +
          " VALUES " + 
          "(?," + // ID_AZIENDA_TRIBUTARIA  1
          "?," + // CUAA 2
          "?," + // FLAG_PRESENTE_AT 3
          "?," + // DENOMINAZIONE 4
          "?," + // COGNOME 5
          "?," + // NOME 6
          "?," + // DATA_NASCITA 7
          "?," + // COMUNE_NASCITA 8
          "?," + // PROVINCIA_NASCITA 9
          "?," + // SESSO 10
          "?," + // INDIRIZZO 11
          "?," + // COMUNE 12
          "?," + // PROVINCIA 13
          "?," + // CAP 14
          "?," + // DATA_DECESSO 15
          "?," + // PARTITA_IVA 16
          "?," + // FORMA_GIURIDICA 17
          "SYSDATE," + // DATA_AGGIORNAMENTO 
          "?," + // CUAA_ANAGRAFE_TRIBUTARIA 18
          "?," + // MESSAGGIO_ERRORE 19
          "?," + // COMUNE_SEDE_LEGALE 20
          "?," + // PROVINCIA_SEDE_LEGALE 21
          "?," + // INDIRIZZO_SEDE_LEGALE 22
          "?," + // CAP_SEDE_LEGALE 23
          "?," + // COMUNE_DOMICILIO_FISCALE 24
          "?," + // PROVINCIA_DOMICILIO_FISCALE 25
          "?," + // INDIRIZZO_DOMICILIO_FISCALE 26
          "?," + // CAP_DOMICILIO_FISCALE 27
          "?," + // DATA_INIZIO_ATTIVITA 28
          "?," + // STATO_ATTIVITA 29
          "?," + // ID_ATTIVITA_ATECO 30
          "?," + // CODICE_ATECO 31
          "?," + // DESCRIZIONE_STATO_ATTIVITA 32
          "?," + // TIPO_ATTIVITA 33
          "?," + // TIPO_CARICA 34
          "?," + // DESCRIZIONE_TIPO_CARICA 35
          "?," + // CODICE_FISCALE_RAPPRESENTANTE 36
          "?," + // DENOMINAZIONE_RAPPRESENTANTE 37
          "?, " + //DATA_DECESSO_RAPPRESENTANTE 38
          "?," + // COGNOME_RAPPRESENTANTE 39
          "?," + // NOME_RAPPRESENTANTE 40
          "'S'," + // FLAG_PRESENTE_GAA 
          "SYSDATE)"; // DATA_AGGIORNAMENTO_GAA
      stmt = conn.prepareStatement(insert);
      
      int indice = 0;

      stmt.setLong(++indice, primaryKey.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(++indice, sianAnagTributariaVO.getCuaa().toUpperCase()); // CUAA
      stmt.setString(++indice, sianAnagTributariaVO.getFlagPresente()); // FLAG_PRESENTE_AT
      //Società
      if(isDatiAnagraficiPro2P.getSoggetto() != null)
      {
        ISSoggettoPro2P isSoggettoPro2p = isDatiAnagraficiPro2P.getSoggetto();
        if(isSoggettoPro2p.getDenominazione() != null)
        {
          stmt.setString(++indice, isSoggettoPro2p.getDenominazione().getDenominazione()); // DENOMINAZIONE
        }
        else
        {
          stmt.setString(++indice, null); // DENOMINAZIONE
        }
        stmt.setString(++indice, null); // COGNOME
        stmt.setString(++indice, null); // NOME
        stmt.setDate(++indice, null); // DATA_NASCITA
        stmt.setString(++indice, null); // COMUNE_NASCITA
        stmt.setString(++indice, null); // PROVINCIA_NASCITA
        stmt.setString(++indice, null);// SESSO
        stmt.setString(++indice, null); // INDIRIZZO
        stmt.setString(++indice, null); // COMUNE
        stmt.setString(++indice, null); // PROVINCIA
        stmt.setString(++indice, null); // CAP
        stmt.setDate(++indice, null); // DATA_DECESSO
        if(isSoggettoPro2p.getPartitaIva() != null)
        {
          stmt.setString(++indice, isSoggettoPro2p.getPartitaIva().getPiva()); // PARTITA_IVA
        }
        else
        {
          stmt.setString(++indice, null); // PARTITA_IVA          
        }
        if((isSoggettoPro2p.getDenominazione() != null)
          && (isSoggettoPro2p.getDenominazione().getNaturaGiuridica() != null))
        {
          stmt.setString(++indice, isSoggettoPro2p.getDenominazione()
              .getNaturaGiuridica().getDescrizione()); // FORMA_GIURIDICA
        }
        else
        {
          stmt.setString(++indice, null); // FORMA_GIURIDICA        
        }
        
        if (isSoggettoPro2p.getCodiceFiscale() != null)
        {
          stmt.setString(++indice, isSoggettoPro2p.getCodiceFiscale().toUpperCase()); // CUAA_ANAGRAFE_TRIBUTARIA
        }
        else
        {
          stmt.setString(++indice, null); // CUAA_ANAGRAFE_TRIBUTARIA
        }
        stmt.setString(++indice, sianAnagTributariaVO.getMessaggioErrore()); // MESSAGGIO_ERRORE
        
        if((isSoggettoPro2p.getSede() != null)
          && (isSoggettoPro2p.getSede().getUbicazione() != null))
        {
          stmt.setString(++indice, isSoggettoPro2p.getSede().getUbicazione().getComune()); // COMUNE_SEDE_LEGALE
          stmt.setString(++indice, isSoggettoPro2p.getSede().getUbicazione().getProvincia()); // PROVINCIA_SEDE_LEGALE
          stmt.setString(++indice, isSoggettoPro2p.getSede().getUbicazione().getToponimo()); // INDIRIZZO_SEDE_LEGALE
          stmt.setString(++indice, isSoggettoPro2p.getSede().getUbicazione().getCap()); // CAP_SEDE_LEGALE
        }
        else
        {
          stmt.setString(++indice, null); // COMUNE_SEDE_LEGALE
          stmt.setString(++indice, null); // PROVINCIA_SEDE_LEGALE
          stmt.setString(++indice, null); // INDIRIZZO_SEDE_LEGALE
          stmt.setString(++indice, null); // CAP_SEDE_LEGALE
        }
        
        if((isSoggettoPro2p.getDomicilioFiscale() != null)
            && (isSoggettoPro2p.getDomicilioFiscale().getUbicazione() != null))
        {
          stmt.setString(++indice, isSoggettoPro2p.getDomicilioFiscale().getUbicazione().getComune()); // COMUNE_DOMICILIO_FISCALE
          stmt.setString(++indice, isSoggettoPro2p.getDomicilioFiscale().getUbicazione().getProvincia()); // PROVINCIA_DOMICILIO_FISCALE
          stmt.setString(++indice, isSoggettoPro2p.getDomicilioFiscale().getUbicazione().getToponimo()); // INDIRIZZO_DOMICILIO_FISCALE
          stmt.setString(++indice, isSoggettoPro2p.getDomicilioFiscale().getUbicazione().getCap()); // CAP_DOMICILIO_FISCALE
        }
        else
        {
          stmt.setString(++indice, null); // COMUNE_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // PROVINCIA_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // INDIRIZZO_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // CAP_DOMICILIO_FISCALE
        }
        
        
        //Da verificare **********
        if(isSoggettoPro2p.getAttivita() != null
          && isSoggettoPro2p.getAttivita().length > 0)
        {
          arrAttivita = isSoggettoPro2p.getAttivita();
          if(arrAttivita[0].getInfoDate() != null)
          {
            if(arrAttivita[0].getInfoDate().getDataInizio() != null)
              stmt.setDate(++indice, convertSqlDate(arrAttivita[0].getInfoDate().getDataInizio().getTime())); // DATA_INIZIO_ATTIVITA
            else
              stmt.setString(++indice, null); // DATA_INIZIO_ATTIVITA
            
            if(arrAttivita[0].getInfoDate().getDataFine() != null)
            {
              stmt.setString(++indice, "C"); // STATO_ATTIVITA
            }
            else
            {
              stmt.setString(++indice, "A"); // STATO_ATTIVITA
            }
          }
          else
          {
            stmt.setDate(++indice, null); // DATA_INIZIO_ATTIVITA
            stmt.setString(++indice, null); // STATO_ATTIVITA
          }
          
          if(arrAttivita[0].getCodice() != null)
          {
            CodeDescription codeDesc = getAttivitaATECObySian(arrAttivita[0].getCodice());
            if(codeDesc != null)
            {
              stmt.setInt(++indice, codeDesc.getCode().intValue()); // ID_ATTIVITA_ATECO
            }
            else
            {
              stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
            }
          }
          else
          {
            stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
          }
          
          stmt.setString(++indice, arrAttivita[0].getCodice()); // CODICE_ATECO          
        }
        else
        {
          stmt.setDate(++indice, null); // DATA_INIZIO_ATTIVITA
          stmt.setString(++indice, null); // STATO_ATTIVITA
          stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
          stmt.setString(++indice, null); // CODICE_ATECO
        }
        
        stmt.setString(++indice, null); // DESCRIZIONE_STATO_ATTIVITA
        stmt.setString(++indice, null); // TIPO_ATTIVITA
        
        if(isSoggettoPro2p.getRappresentante() != null)
        {
          if(isSoggettoPro2p.getRappresentante().getCarica() != null)
          {
            stmt.setString(++indice, isSoggettoPro2p.getRappresentante().getCarica().getCodice()); // TIPO_CARICA
            stmt.setString(++indice, isSoggettoPro2p.getRappresentante().getCarica().getDescrizione()); // DESCRIZIONE_TIPO_CARICA
          }
          else
          {
            stmt.setString(++indice, null); // TIPO_CARICA
            stmt.setString(++indice, null); // DESCRIZIONE_TIPO_CARICA
          }
          
          if(isSoggettoPro2p.getRappresentante().getRappresentante() != null)
          {
            stmt.setString(++indice, isSoggettoPro2p.getRappresentante().getRappresentante().getCodiceFiscale()); // CODICE_FISCALE_RAPPRESENTANTE
            stmt.setString(++indice, isSoggettoPro2p.getRappresentante().getRappresentante().getDatiIdentificativi()); // DENOMINAZIONE_RAPPRESENTANTE
            if(isSoggettoPro2p.getRappresentante().getRappresentante().getDecesso() != null)
            {
              stmt.setDate(++indice, convertSqlDate(isSoggettoPro2p.getRappresentante().getRappresentante().getDecesso().getData().getTime())); // DATA_DECESSO_RAPPRESENTANTE
            }
            else
            {
              stmt.setDate(++indice, null); // DATA_DECESSO_RAPPRESENTANTE
            }
            stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
            stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
          }
          else
          {
            stmt.setString(++indice, null); // CODICE_FISCALE_RAPPRESENTANTE
            stmt.setString(++indice, null); // DENOMINAZIONE_RAPPRESENTANTE
            stmt.setDate(++indice, null); // DATA_DECESSO_RAPPRESENTANTE
            stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
            stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
          }
        }
        else
        {
          stmt.setString(++indice, null); // TIPO_CARICA
          stmt.setString(++indice, null); // DESCRIZIONE_TIPO_CARICA
          stmt.setString(++indice, null); // CODICE_FISCALE_RAPPRESENTANTE
          stmt.setString(++indice, null); // DENOMINAZIONE_RAPPRESENTANTE
          stmt.setDate(++indice, null); // DATA_DECESSO_RAPPRESENTANTE
          stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
          stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
        }
        
      }     
      //ditta individuale/persona fisica
      //La persona fisica se trattasi di ditta individuale o persona fisica è sempre presente
      // come da specifiche del servizio
      else if((isDatiAnagraficiPro2P.getPersona() != null)
        && (isDatiAnagraficiPro2P.getPersona().getPersonaFisica() != null))
      {          
        ISDittaIndividualePro2P isDittaIndividualePro2P = isDatiAnagraficiPro2P.getPersona().getDittaIndividuale();
        ISPersonaFisicaPro2P isPersonaFisicaPro2P = isDatiAnagraficiPro2P.getPersona().getPersonaFisica();
        
        if(isDittaIndividualePro2P != null)
        {
          stmt.setString(++indice, isDittaIndividualePro2P.getDenominazione()); // DENOMINAZIONE
        }
        else
        {
          stmt.setString(++indice, null); // DENOMINAZIONE
        }
        
        
        stmt.setString(++indice, isPersonaFisicaPro2P.getCognome()); // COGNOME
        stmt.setString(++indice, isPersonaFisicaPro2P.getNome()); // NOME
        if(isPersonaFisicaPro2P.getDatiNascita() != null)
        {
          stmt.setDate(++indice, convertSqlDate(isPersonaFisicaPro2P.getDatiNascita().getData().getTime())); // DATA_NASCITA
          stmt.setString(++indice, isPersonaFisicaPro2P.getDatiNascita().getComune()); // COMUNE_NASCITA
          stmt.setString(++indice, isPersonaFisicaPro2P.getDatiNascita().getProvincia()); // PROVINCIA_NASCITA
        }
        else
        {
          stmt.setDate(++indice, null); // DATA_NASCITA
          stmt.setString(++indice, null); // COMUNE_NASCITA
          stmt.setString(++indice, null); // PROVINCIA_NASCITA
        }
        
        if(isPersonaFisicaPro2P.getSesso() != null)
        {
          stmt.setString(++indice, isPersonaFisicaPro2P.getSesso().getValore());// SESSO
        }
        else
        {
          stmt.setString(++indice, null);// SESSO
        }
       
        if((isPersonaFisicaPro2P.getDomicilioFiscale() != null)
            && (isPersonaFisicaPro2P.getDomicilioFiscale().getUbicazione() != null))
        {
          stmt.setString(++indice, isPersonaFisicaPro2P.getDomicilioFiscale().getUbicazione().getToponimo()); // INDIRIZZO
          stmt.setString(++indice, isPersonaFisicaPro2P.getDomicilioFiscale().getUbicazione().getComune()); // COMUNE
          stmt.setString(++indice, isPersonaFisicaPro2P.getDomicilioFiscale().getUbicazione().getProvincia()); // PROVINCIA
          stmt.setString(++indice, isPersonaFisicaPro2P.getDomicilioFiscale().getUbicazione().getCap()); // CAP
        }
        else
        {
          stmt.setString(++indice, null); // INDIRIZZO
          stmt.setString(++indice, null); // COMUNE
          stmt.setString(++indice, null); // PROVINCIA
          stmt.setString(++indice, null); // CAP
        }
        
        if(isPersonaFisicaPro2P.getDecesso() != null)
        {
          stmt.setDate(++indice, convertSqlDate(isPersonaFisicaPro2P.getDecesso().getData().getTime())); // DATA_DECESSO
        }
        else
        {
          stmt.setDate(++indice, null); // DATA_DECESSO
        }
        
        
        
        if((isDittaIndividualePro2P != null)
          && (isDittaIndividualePro2P.getPartitaIva() != null))
        {        
          stmt.setString(++indice, isDittaIndividualePro2P.getPartitaIva().getPiva()); // PARTITA_IVA
        }
        else
        {
          stmt.setString(++indice, null); // PARTITA_IVA          
        }
        if(isDittaIndividualePro2P != null)
        {
          stmt.setString(++indice, "DITTA INDIVIDUALE"); // FORMA_GIURIDICA
        }
        else
        {
          stmt.setString(++indice, "PERSONA FISICA"); // FORMA_GIURIDICA        
        }
        
        if (isPersonaFisicaPro2P.getCodiceFiscale() != null)
        {
          stmt.setString(++indice, isPersonaFisicaPro2P.getCodiceFiscale().toUpperCase()); // CUAA_ANAGRAFE_TRIBUTARIA
        }
        else
        {
          stmt.setString(++indice, null); // CUAA_ANAGRAFE_TRIBUTARIA
        }
        stmt.setString(++indice, sianAnagTributariaVO.getMessaggioErrore()); // MESSAGGIO_ERRORE
        
        if((isDittaIndividualePro2P != null)
          && (isDittaIndividualePro2P.getSede() != null)
          && (isDittaIndividualePro2P.getSede().getUbicazione() != null))
        {
          stmt.setString(++indice, isDittaIndividualePro2P.getSede().getUbicazione().getComune()); // COMUNE_SEDE_LEGALE
          stmt.setString(++indice, isDittaIndividualePro2P.getSede().getUbicazione().getProvincia()); // PROVINCIA_SEDE_LEGALE
          stmt.setString(++indice, isDittaIndividualePro2P.getSede().getUbicazione().getToponimo()); // INDIRIZZO_SEDE_LEGALE
          stmt.setString(++indice, isDittaIndividualePro2P.getSede().getUbicazione().getCap()); // CAP_SEDE_LEGALE
        }
        else
        {
          stmt.setString(++indice, null); // COMUNE_SEDE_LEGALE
          stmt.setString(++indice, null); // PROVINCIA_SEDE_LEGALE
          stmt.setString(++indice, null); // INDIRIZZO_SEDE_LEGALE
          stmt.setString(++indice, null); // CAP_SEDE_LEGALE
        }
        
        if((isDittaIndividualePro2P != null)
            && (isDittaIndividualePro2P.getDomicilioFiscale() != null)
            && (isDittaIndividualePro2P.getDomicilioFiscale().getUbicazione() != null))
        {
          stmt.setString(++indice, isDittaIndividualePro2P.getDomicilioFiscale().getUbicazione().getComune()); // COMUNE_DOMICILIO_FISCALE
          stmt.setString(++indice, isDittaIndividualePro2P.getDomicilioFiscale().getUbicazione().getProvincia()); // PROVINCIA_DOMICILIO_FISCALE
          stmt.setString(++indice, isDittaIndividualePro2P.getDomicilioFiscale().getUbicazione().getToponimo()); // INDIRIZZO_DOMICILIO_FISCALE
          stmt.setString(++indice, isDittaIndividualePro2P.getDomicilioFiscale().getUbicazione().getCap()); // CAP_DOMICILIO_FISCALE
        }
        else
        {
          stmt.setString(++indice, null); // COMUNE_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // PROVINCIA_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // INDIRIZZO_DOMICILIO_FISCALE
          stmt.setString(++indice, null); // CAP_DOMICILIO_FISCALE
        }
        
        if((isDittaIndividualePro2P != null)
           && (isDittaIndividualePro2P.getAttivita() != null)
           && (isDittaIndividualePro2P.getAttivita().length > 0))
        {
          arrAttivita = isDittaIndividualePro2P.getAttivita();
          if(arrAttivita[0].getInfoDate() != null)
          {
            if(arrAttivita[0].getInfoDate().getDataInizio() != null)
              stmt.setDate(++indice, convertSqlDate(arrAttivita[0].getInfoDate().getDataInizio().getTime())); // DATA_INIZIO_ATTIVITA
            else
              stmt.setString(++indice, null); // DATA_INIZIO_ATTIVITA
            
            if(arrAttivita[0].getInfoDate().getDataFine() != null)
            {
              stmt.setString(++indice, "C"); // STATO_ATTIVITA
            }
            else
            {
              stmt.setString(++indice, "A"); // STATO_ATTIVITA
            }
          }
          else
          {
            stmt.setDate(++indice, null); // DATA_INIZIO_ATTIVITA
            stmt.setString(++indice, null); // STATO_ATTIVITA
          }
          
          if(arrAttivita[0].getCodice() != null)
          {
            CodeDescription codeDesc = getAttivitaATECObySian(arrAttivita[0].getCodice());
            if(codeDesc != null)
            {
              stmt.setInt(++indice, codeDesc.getCode().intValue()); // ID_ATTIVITA_ATECO
            }
            else
            {
              stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
            }
          }
          else
          {
            stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
          }
          
          stmt.setString(++indice, arrAttivita[0].getCodice()); // CODICE_ATECO          
        }
        else
        {
          stmt.setDate(++indice, null); // DATA_INIZIO_ATTIVITA
          stmt.setString(++indice, null); // STATO_ATTIVITA
          stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
          stmt.setString(++indice, null); // CODICE_ATECO
        }
        
        stmt.setString(++indice, null); // DESCRIZIONE_STATO_ATTIVITA
        stmt.setString(++indice, null); // TIPO_ATTIVITA
        
        if((isDittaIndividualePro2P != null)
          &&  (isDittaIndividualePro2P.getRappresentante() != null))
        {
          if(isDittaIndividualePro2P.getRappresentante().getCarica() != null)
          {
            stmt.setString(++indice, isDittaIndividualePro2P.getRappresentante().getCarica().getCodice()); // TIPO_CARICA
            stmt.setString(++indice, isDittaIndividualePro2P.getRappresentante().getCarica().getDescrizione()); // DESCRIZIONE_TIPO_CARICA
          }
          else
          {
            stmt.setString(++indice, null); // TIPO_CARICA
            stmt.setString(++indice, null); // DESCRIZIONE_TIPO_CARICA
          }
          
          if(isDittaIndividualePro2P.getRappresentante().getRappresentante() != null)
          {
            stmt.setString(++indice, isDittaIndividualePro2P.getRappresentante().getRappresentante().getCodiceFiscale()); // CODICE_FISCALE_RAPPRESENTANTE
            stmt.setString(++indice, isDittaIndividualePro2P.getRappresentante().getRappresentante().getDatiIdentificativi()); // DENOMINAZIONE_RAPPRESENTANTE
            if(isDittaIndividualePro2P.getRappresentante().getRappresentante().getDecesso() != null)
            {
              stmt.setDate(++indice, convertSqlDate(isDittaIndividualePro2P.getRappresentante().getRappresentante().getDecesso().getData().getTime())); // DATA_DECESSO_RAPPRESENTANTE
            }
            else
            {
              stmt.setDate(++indice, null); // DATA_DECESSO_RAPPRESENTANTE
            }
            stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
            stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
          }
          else
          {
            stmt.setString(++indice, null); // CODICE_FISCALE_RAPPRESENTANTE
            stmt.setString(++indice, null); // DENOMINAZIONE_RAPPRESENTANTE
            stmt.setDate(++indice, null);   // DATA_DECESSO_RAPPRESENTANTE
            stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
            stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
          }
        }
        else
        {
          stmt.setString(++indice, null); // TIPO_CARICA
          stmt.setString(++indice, null); // DESCRIZIONE_TIPO_CARICA
          stmt.setString(++indice, null); // CODICE_FISCALE_RAPPRESENTANTE
          stmt.setString(++indice, null); // DENOMINAZIONE_RAPPRESENTANTE
          stmt.setDate(++indice, null);   // DATA_DECESSO_RAPPRESENTANTE
          stmt.setString(++indice, null); // COGNOME_RAPPRESENTANTE
          stmt.setString(++indice, null); // NOME_RAPPRESENTANTE
        }
        
      }
      
      

      SolmrLogger.debug(this, "Executing insertAziendaTributaria: " + insert);

      stmt.executeUpdate();

      stmt.close();
      
      
      //ha anche attività ateco secondarie
      if(arrAttivita != null && arrAttivita.length > 1)
      {
        //Filtro per i doppi codici
        Vector<ISAttivitaPro2P> vAttivita = new Vector<ISAttivitaPro2P>();
        Vector<String> vCodiceAtecoSec = new Vector<String>();
        for(int i=1;i<arrAttivita.length;i++)
        {
          if(!vCodiceAtecoSec.contains(arrAttivita[i].getCodice()))
          {
            vCodiceAtecoSec.add(arrAttivita[i].getCodice());
            vAttivita.add(arrAttivita[i]);
          }
        } 
        
        
        for(int i=0;i<vAttivita.size();i++)
        {
          insertAziendaTributariaAtecoSec(primaryKey, vAttivita.get(i));
        }            
      }
      
      

      SolmrLogger.debug(this, "Executed insertAziendaTributaria where cuaa= "
          + sianAnagTributariaVO.getCuaa().toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertAziendaTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  /**
   * inserisce le attività secondarie ateco...
   * 
   * 
   * @param idAziendaTributaria
   * @param isAttivita
   * @return
   * @throws DataAccessException
   */
  public Long insertAziendaTributariaAtecoSec(long idAziendaTributaria, ISAttivitaPro2P isAttivita) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DB_ATECO_SEC_TRIBUTARIA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_ATECO_SEC_TRIBUTARIA " +
          "            (ID_ATECO_SEC_TRIBUTARIA, " +
          "             ID_ATTIVITA_ATECO, " +
          "             ID_AZIENDA_TRIBUTARIA," +
          "             CODICE_ATECO) " +
          " VALUES " + 
          "(?," + // ID_ATECO_SEC_TRIBUTARIA  1
          "?," + // ID_ATTIVITA_ATECO 2
          "?," + // ID_AZIENDA_TRIBUTARIA 3
          "?)";  //CODICE_ATECO
      stmt = conn.prepareStatement(insert);
      
      int indice = 0;

      stmt.setLong(++indice, primaryKey.longValue()); // ID_ATECO_SEC_TRIBUTARIA
      CodeDescription codeDesc = getAttivitaATECObySian(isAttivita.getCodice());
      if(codeDesc != null)
      {
        stmt.setInt(++indice, codeDesc.getCode().intValue()); // ID_ATTIVITA_ATECO
      }
      else
      {
        stmt.setString(++indice, null); // ID_ATTIVITA_ATECO
      }
      stmt.setLong(++indice, idAziendaTributaria); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(++indice, isAttivita.getCodice()); // CODICE_ATECO
      
      
      
      

      SolmrLogger.debug(this, "Executing insertAziendaTributariaAtecoSec: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertAziendaTributariaAtecoSec where idAziendaTributaria= "
          +idAziendaTributaria);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertAziendaTributariaAtecoSec: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertAziendaTributariaAtecoSec: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertAziendaTributariaAtecoSec: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertAziendaTributariaAtecoSec: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }
  
  
  //Metodo per inserire un record su DB_AZIENDA_TRIBUTARIA nel caso si sia 
  //verificato un errore
  public void insertAziendaTributaria(SianAnagTributariaVO sianAnagTributariaVO) 
  throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey((String) SolmrConstants
          .get("SEQ_AZIENDA_TRIBUTARIA"));
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_AZIENDA_TRIBUTARIA "
          + "            (ID_AZIENDA_TRIBUTARIA, " 
          + "             CUAA, "
          + "             FLAG_PRESENTE_AT, " 
          + "             DATA_AGGIORNAMENTO, "
          + "             MESSAGGIO_ERRORE) " + 
          " VALUES " + 
          "(?," + // ID_AZIENDA_TRIBUTARIA  1
          " ?," + // CUAA 2
          " ?," + // FLAG_PRESENTE_AT 3
          " SYSDATE," + // DATA_AGGIORNAMENTO
          " ?)";  // MESSAGGIO_ERRORE 4

      stmt = conn.prepareStatement(insert);


      SolmrLogger.debug(this,"\n\ngetMessaggioErrore() "+ sianAnagTributariaVO.getMessaggioErrore());
      SolmrLogger.debug(this,"\n\ngetFlagPresente() "+ sianAnagTributariaVO.getFlagPresente());

      stmt.setLong(1, primaryKey.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(2, sianAnagTributariaVO.getCuaa().toUpperCase()); // CUAA
      stmt.setString(3, sianAnagTributariaVO.getFlagPresente()); // FLAG_PRESENTE_AT
      stmt.setString(4, sianAnagTributariaVO.getMessaggioErrore()); // MESSAGGIO_ERRORE

      SolmrLogger.debug(this, "Executing insertAziendaTributaria: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertAziendaTributaria where cuaa= "
          + sianAnagTributariaVO.getCuaa().toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertAziendaTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  //Metodo per modificare un record su DB_AZIENDA_TRIBUTARIA nel caso si sia 
  //verificato un errore
  public void updateAziendaTributaria(SianAnagTributariaVO sianAnagTributariaVO) 
  throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String update = " UPDATE DB_AZIENDA_TRIBUTARIA " +
                      "    SET FLAG_PRESENTE_AT = ?, " +
                      "    MESSAGGIO_ERRORE = ?, " +
                      "    DATA_AGGIORNAMENTO = SYSDATE " +
                      " WHERE CUAA = ? ";

      stmt = conn.prepareStatement(update);


      SolmrLogger.debug(this,"\n\ngetMessaggioErrore() "+ sianAnagTributariaVO.getMessaggioErrore());
      SolmrLogger.debug(this,"\n\ngetFlagPresente() "+ sianAnagTributariaVO.getFlagPresente());

      
      stmt.setString(1, sianAnagTributariaVO.getFlagPresente()); // FLAG_PRESENTE_AT
      stmt.setString(2, sianAnagTributariaVO.getMessaggioErrore()); // MESSAGGIO_ERRORE
      stmt.setString(3, sianAnagTributariaVO.getCuaa().toUpperCase()); // CUAA

      SolmrLogger.debug(this, "Executing updateAziendaTributaria: " + update);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateAziendaTributaria where cuaa= "
          + sianAnagTributariaVO.getCuaa().toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in updateAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in updateAziendaTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in updateAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in updateAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  

  // Metodo per inserire un record su DB_CF_COLLEGATI_SIAN
  /*public Long insertCodiciFiscaliCollegatiSian(CUAAType cuaaType,
      Long idAziendaTributaria) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_CF_COLLEGATI_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_CF_COLLEGATI_SIAN "
          + "            (ID_CF_COLLEGATI_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, "
          + "             CODICE_FISCALE_PF, "
          + "             CODICE_FISCALE_PG) " + " VALUES " + "(?," + // ID_CF_COLLEGATI_SIAN
                                                                      // 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // CODICE_FISCALE_PF 3
          "?)"; // CODICE_FISCALE_PG 4

      stmt = conn.prepareStatement(insert);

      if (cuaaType.getCodiceFiscalePersonaFisica() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza cuaaType.getCodiceFiscalePersonaFisica() "
                + cuaaType.getCodiceFiscalePersonaFisica().length());
      if (cuaaType.getCodiceFiscalePersonaGiuridica() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza cuaaType.getCodiceFiscalePersonaGiuridica() "
                + cuaaType.getCodiceFiscalePersonaGiuridica().length());

      if (cuaaType.getCodiceFiscalePersonaFisica() != null)
        SolmrLogger.debug(this,
            "\n\nContenuto cuaaType.getCodiceFiscalePersonaFisica() "
                + cuaaType.getCodiceFiscalePersonaFisica());
      if (cuaaType.getCodiceFiscalePersonaGiuridica() != null)
        SolmrLogger.debug(this,
            "\n\nContenuto cuaaType.getCodiceFiscalePersonaGiuridica() "
                + cuaaType.getCodiceFiscalePersonaGiuridica());

      stmt.setLong(1, primaryKey.longValue()); // ID_CF_COLLEGATI_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(3, cuaaType.getCodiceFiscalePersonaFisica()); // CODICE_FISCALE_PF
      stmt.setString(4, cuaaType.getCodiceFiscalePersonaGiuridica()); // CODICE_FISCALE_PG

      SolmrLogger.debug(this, "Executing insertCodiciFiscaliCollegatiSian: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertCodiciFiscaliCollegatiSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in insertCodiciFiscaliCollegatiSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertCodiciFiscaliCollegatiSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertCodiciFiscaliCollegatiSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertCodiciFiscaliCollegatiSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_PARTITE_IVA_ATTRIBUITE_SIAN
  /*public Long insertPartiteIvaAttribuiteSian(
      ISATPartitaIvaAttribuita iSATPartitaIvaAttribuita,
      Long idAziendaTributaria) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PARTITE_IVA_ATTRIBUITE_SIA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PARTITE_IVA_ATTRIBUITE_SIAN "
          + "            (ID_PARTITE_IVA_ATTRIBUITE_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, "
          + "             PARTITA_IVA, " + "             DATA_FINE, "
          + "             DESCRIZIONE_MOD_FINE, "
          + "             DESCRIZIONE_TIPO_FINE_PF, "
          + "             DESCRIZIONE_TIPO_FINE_PNF, "
          + "             MODELLO_FINE, "
          + "             DESCRIZIONE_MODELLO_FINE, "
          + "             PARTITA_IVA_CONFLUENZA, "
          + "             TIPO_FINE, " + "             DESCRIZIONE_TIPO_FINE) "
          + " VALUES " + "(?," + // ID_PARTITE_IVA_ATTRIBUITE_SIAN 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // PARTITA_IVA 3
          "?," + // DATA_FINE 4
          "?," + // DESCRIZIONE_MOD_FINE 5
          "?," + // DESCRIZIONE_TIPO_FINE_PF 6
          "?," + // DESCRIZIONE_TIPO_FINE_PNF 7
          "?," + // MODELLO_FINE 8
          "?," + // DESCRIZIONE_MODELLO_FINE 9
          "?," + // PARTITA_IVA_CONFLUENZA 10
          "?," + // TIPO_FINE 11
          "?)"; // DESCRIZIONE_TIPO_FINE 12

      stmt = conn.prepareStatement(insert);

      if (iSATPartitaIvaAttribuita.getPartitaIva() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getPartitaIva "
                + iSATPartitaIvaAttribuita.getPartitaIva().length());
      if (iSATPartitaIvaAttribuita.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getDataFine "
                + iSATPartitaIvaAttribuita.getDataFine().length());
      if (iSATPartitaIvaAttribuita.getDecoModFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getDecoModFine "
                + iSATPartitaIvaAttribuita.getDecoModFine().length());
      if (iSATPartitaIvaAttribuita.getDecoTipoFinePf() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getDecoTipoFinePf "
                + iSATPartitaIvaAttribuita.getDecoTipoFinePf().length());
      if (iSATPartitaIvaAttribuita.getDecoTipoFinePnf() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getDecoTipoFinePnf "
                + iSATPartitaIvaAttribuita.getDecoTipoFinePnf().length());
      if (iSATPartitaIvaAttribuita.getModelloFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getModelloFine "
                + iSATPartitaIvaAttribuita.getModelloFine().length());
      if (iSATPartitaIvaAttribuita.getModelloFineDesc() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getModelloFineDesc "
                + iSATPartitaIvaAttribuita.getModelloFineDesc().length());
      if (iSATPartitaIvaAttribuita.getPartitaIvaConfluenza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getPartitaIvaConfluenza "
                + iSATPartitaIvaAttribuita.getPartitaIvaConfluenza().length());
      if (iSATPartitaIvaAttribuita.getTipoFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getTipoFine "
                + iSATPartitaIvaAttribuita.getTipoFine().length());
      if (iSATPartitaIvaAttribuita.getTipoFineDesc() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATPartitaIvaAttribuita.getTipoFineDesc "
                + iSATPartitaIvaAttribuita.getTipoFineDesc().length());

      if (iSATPartitaIvaAttribuita.getPartitaIva() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getPartitaIva "
                + iSATPartitaIvaAttribuita.getPartitaIva());
      if (iSATPartitaIvaAttribuita.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getDataFine "
                + iSATPartitaIvaAttribuita.getDataFine());
      if (iSATPartitaIvaAttribuita.getDecoModFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getDecoModFine "
                + iSATPartitaIvaAttribuita.getDecoModFine());
      if (iSATPartitaIvaAttribuita.getDecoTipoFinePf() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getDecoTipoFinePf "
                + iSATPartitaIvaAttribuita.getDecoTipoFinePf());
      if (iSATPartitaIvaAttribuita.getDecoTipoFinePnf() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getDecoTipoFinePnf "
                + iSATPartitaIvaAttribuita.getDecoTipoFinePnf());
      if (iSATPartitaIvaAttribuita.getModelloFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getModelloFine "
                + iSATPartitaIvaAttribuita.getModelloFine());
      if (iSATPartitaIvaAttribuita.getModelloFineDesc() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getModelloFineDesc "
                + iSATPartitaIvaAttribuita.getModelloFineDesc());
      if (iSATPartitaIvaAttribuita.getPartitaIvaConfluenza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getPartitaIvaConfluenza "
                + iSATPartitaIvaAttribuita.getPartitaIvaConfluenza());
      if (iSATPartitaIvaAttribuita.getTipoFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getTipoFine "
                + iSATPartitaIvaAttribuita.getTipoFine());
      if (iSATPartitaIvaAttribuita.getTipoFineDesc() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATPartitaIvaAttribuita.getTipoFineDesc "
                + iSATPartitaIvaAttribuita.getTipoFineDesc());

      stmt.setLong(1, primaryKey.longValue()); // ID_PARTITE_IVA_ATTRIBUITE_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(3, iSATPartitaIvaAttribuita.getPartitaIva()); // PARTITA_IVA
      if (Validator.isNotEmpty(iSATPartitaIvaAttribuita.getDataFine()))
      {
        String dataTemp = iSATPartitaIvaAttribuita.getDataFine().substring(0,
            10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(4, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE
        else
          stmt.setDate(4, null); // DATA_FINE
      }
      else
        stmt.setDate(4, null); // DATA_FINE
      stmt.setString(5, iSATPartitaIvaAttribuita.getDecoModFine()); // DESCRIZIONE_MOD_FINE
      stmt.setString(6, iSATPartitaIvaAttribuita.getDecoTipoFinePf()); // DESCRIZIONE_TIPO_FINE_PF
      stmt.setString(7, iSATPartitaIvaAttribuita.getDecoTipoFinePnf()); // DESCRIZIONE_TIPO_FINE_PNF
      stmt.setString(8, iSATPartitaIvaAttribuita.getModelloFine()); // MODELLO_FINE
      stmt.setString(9, iSATPartitaIvaAttribuita.getModelloFineDesc()); // DESCRIZIONE_MODELLO_FINE
      stmt.setString(10, iSATPartitaIvaAttribuita.getPartitaIvaConfluenza()); // PARTITA_IVA_CONFLUENZA
      stmt.setString(11, iSATPartitaIvaAttribuita.getTipoFine()); // TIPO_FINE
      stmt.setString(12, iSATPartitaIvaAttribuita.getTipoFineDesc()); // DESCRIZIONE_TIPO_FINE

      SolmrLogger.debug(this, "Executing insertPartiteIvaAttribuiteSian: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertPartiteIvaAttribuiteSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in insertPartiteIvaAttribuiteSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertPartiteIvaAttribuiteSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertPartiteIvaAttribuiteSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertPartiteIvaAttribuiteSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_SOCIETA_RAPPRESENTATE_SIAN
  /*public Long insertSocietaRappresentataSian(
      ISATSocietaRappresentata iSATSocietaRappresentata,
      Long idAziendaTributaria) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_SOCIETA_RAPPRESENTATE_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_SOCIETA_RAPPRESENTATE_SIAN "
          + "            (ID_SOCIETA_RAPPRESENTATE_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, "
          + "             DATA_INIZIO, " + "             DATA_FINE, "
          + "             TIPO_RAPPRESENTANZA, "
          + "             CODICE_FISCALE_RAPPRESENTATO, "
          + "             DECODIFICA_TIPO_RAPPRESENTANZA, "
          + "             DESCRIZIONE_TIPO_RAPPRESENTAZA) " + " VALUES "
          + "(?," + // ID_SOCIETA_RAPPRESENTATE_SIAN 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // DATA_INIZIO 3
          "?," + // DATA_FINE 4
          "?," + // TIPO_RAPPRESENTANZA 5
          "?," + // CODICE_FISCALE_RAPPRESENTATO 6
          "?," + // DECODIFICA_TIPO_RAPPRESENTANZA 7
          "?)"; // DESCRIZIONE_TIPO_RAPPRESENTAZA 8

      stmt = conn.prepareStatement(insert);

      if (iSATSocietaRappresentata.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATSocietaRappresentata.getDataInizio() "
                + iSATSocietaRappresentata.getDataInizio().length());
      if (iSATSocietaRappresentata.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATSocietaRappresentata.getDataFine() "
                + iSATSocietaRappresentata.getDataFine().length());
      if (iSATSocietaRappresentata.getTipoRappresentanza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATSocietaRappresentata.getTipoRappresentanza() "
                + iSATSocietaRappresentata.getTipoRappresentanza().length());
      if (iSATSocietaRappresentata.getCodiceFiscaleRappresentato() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATSocietaRappresentata.getCodiceFiscaleRappresentato() "
                + iSATSocietaRappresentata.getCodiceFiscaleRappresentato()
                    .length());
      if (iSATSocietaRappresentata.getDecoTipoRapp() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATSocietaRappresentata.getDecoTipoRapp() "
                + iSATSocietaRappresentata.getDecoTipoRapp().length());
      if (iSATSocietaRappresentata.getTipoRappresentanzaDesc() != null)
        SolmrLogger
            .debug(this,
                "\n\nLunghezza iSATSocietaRappresentata.getTipoRappresentanzaDesc() "
                    + iSATSocietaRappresentata.getTipoRappresentanzaDesc()
                        .length());

      if (iSATSocietaRappresentata.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getDataInizio() "
                + iSATSocietaRappresentata.getDataInizio());
      if (iSATSocietaRappresentata.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getDataFine() "
                + iSATSocietaRappresentata.getDataFine());
      if (iSATSocietaRappresentata.getTipoRappresentanza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getTipoRappresentanza() "
                + iSATSocietaRappresentata.getTipoRappresentanza());
      if (iSATSocietaRappresentata.getCodiceFiscaleRappresentato() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getCodiceFiscaleRappresentato() "
                + iSATSocietaRappresentata.getCodiceFiscaleRappresentato());
      if (iSATSocietaRappresentata.getDecoTipoRapp() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getDecoTipoRapp() "
                + iSATSocietaRappresentata.getDecoTipoRapp());
      if (iSATSocietaRappresentata.getTipoRappresentanzaDesc() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATSocietaRappresentata.getTipoRappresentanzaDesc() "
                + iSATSocietaRappresentata.getTipoRappresentanzaDesc());

      stmt.setLong(1, primaryKey.longValue()); // ID_SOCIETA_RAPPRESENTATE_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA
      if (Validator.isNotEmpty(iSATSocietaRappresentata.getDataInizio()))
      {
        String dataTemp = iSATSocietaRappresentata.getDataInizio().substring(0,
            10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(3, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_INIZIO
        else
          stmt.setDate(3, null); // DATA_INIZIO
      }
      else
        stmt.setDate(3, null); // DATA_INIZIO

      if (Validator.isNotEmpty(iSATSocietaRappresentata.getDataFine()))
      {
        String dataTemp = iSATSocietaRappresentata.getDataFine().substring(0,
            10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(4, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE
        else
          stmt.setDate(4, null); // DATA_FINE
      }
      else
        stmt.setDate(4, null); // DATA_FINE
      stmt.setString(5, iSATSocietaRappresentata.getTipoRappresentanza()); // TIPO_RAPPRESENTANZA
      stmt.setString(6, iSATSocietaRappresentata
          .getCodiceFiscaleRappresentato()); // CODICE_FISCALE_RAPPRESENTATO
      stmt.setString(7, iSATSocietaRappresentata.getDecoTipoRapp()); // DECODIFICA_TIPO_RAPPRESENTANZA
      stmt.setString(8, iSATSocietaRappresentata.getTipoRappresentanzaDesc()); // DESCRIZIONE_TIPO_RAPPRESENTAZA

      SolmrLogger.debug(this, "Executing insertSocietaRappresentataSian: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertSocietaRappresentataSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in insertSocietaRappresentataSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertSocietaRappresentataSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertSocietaRappresentataSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertSocietaRappresentataSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_RESIDENZA_VARIATA_SIAN
  /*public Long insertResidenzaVariataSian(
      ISATResidenzaVariata iSATResidenzaVariata, Long idAziendaTributaria)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_RESIDENZA_VARIATA_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_RESIDENZA_VARIATA_SIAN "
          + "            (ID_RESIDENZA_VARIATA_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, " + "             DATA_FINE, "
          + "             DATA_INIZIO, " + "             COMUNE_RESIDENZA, "
          + "             CAP_RESIDENZA, "
          + "             INDIRIZZO_RESIDENZA, "
          + "             PROVINCIA_RESIDENZA) " + " VALUES " + "(?," + // ID_RESIDENZA_VARIATA_SIAN
                                                                        // 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // DATA_FINE 3
          "?," + // DATA_INIZIO 4
          "?," + // COMUNE_RESIDENZA 5
          "?," + // CAP_RESIDENZA 6
          "?," + // INDIRIZZO_RESIDENZA 7
          "?)"; // PROVINCIA_RESIDENZA 8

      stmt = conn.prepareStatement(insert);

      if (iSATResidenzaVariata.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getDataFine() "
                + iSATResidenzaVariata.getDataFine().length());
      if (iSATResidenzaVariata.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getDataInizio() "
                + iSATResidenzaVariata.getDataInizio().length());
      if (iSATResidenzaVariata.getComuneResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getComuneResidenza() "
                + iSATResidenzaVariata.getComuneResidenza().length());
      if (iSATResidenzaVariata.getCapResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getCapResidenza() "
                + iSATResidenzaVariata.getCapResidenza().length());
      if (iSATResidenzaVariata.getIndirizzoResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getIndirizzoResidenza() "
                + iSATResidenzaVariata.getIndirizzoResidenza().length());
      if (iSATResidenzaVariata.getProvinciaResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATResidenzaVariata.getProvinciaResidenza() "
                + iSATResidenzaVariata.getProvinciaResidenza().length());

      if (iSATResidenzaVariata.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getDataFine() "
                + iSATResidenzaVariata.getDataFine());
      if (iSATResidenzaVariata.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getDataInizio() "
                + iSATResidenzaVariata.getDataInizio());
      if (iSATResidenzaVariata.getComuneResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getComuneResidenza() "
                + iSATResidenzaVariata.getComuneResidenza());
      if (iSATResidenzaVariata.getCapResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getCapResidenza() "
                + iSATResidenzaVariata.getCapResidenza());
      if (iSATResidenzaVariata.getIndirizzoResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getIndirizzoResidenza() "
                + iSATResidenzaVariata.getIndirizzoResidenza());
      if (iSATResidenzaVariata.getProvinciaResidenza() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATResidenzaVariata.getProvinciaResidenza() "
                + iSATResidenzaVariata.getProvinciaResidenza());

      stmt.setLong(1, primaryKey.longValue()); // ID_RESIDENZA_VARIATA_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA

      if (Validator.isNotEmpty(iSATResidenzaVariata.getDataFine()))
      {
        String dataTemp = iSATResidenzaVariata.getDataFine().substring(0, 10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(3, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE
        else
          stmt.setDate(3, null); // DATA_FINE
      }
      else
        stmt.setDate(3, null); // DATA_FINE

      if (Validator.isNotEmpty(iSATResidenzaVariata.getDataInizio()))
      {
        String dataTemp = iSATResidenzaVariata.getDataInizio().substring(0, 10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(4, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_INIZIO
        else
          stmt.setDate(4, null); // DATA_INIZIO
      }
      else
        stmt.setDate(4, null); // DATA_INIZIO
      stmt.setString(5, iSATResidenzaVariata.getComuneResidenza()); // COMUNE_RESIDENZA
      stmt.setString(6, iSATResidenzaVariata.getCapResidenza()); // CAP_RESIDENZA
      stmt.setString(7, iSATResidenzaVariata.getIndirizzoResidenza()); // INDIRIZZO_RESIDENZA
      stmt.setString(8, iSATResidenzaVariata.getProvinciaResidenza()); // PROVINCIA_RESIDENZA

      SolmrLogger
          .debug(this, "Executing insertResidenzaVariataSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertResidenzaVariataSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertResidenzaVariataSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in insertResidenzaVariataSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertResidenzaVariataSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertResidenzaVariataSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_DOMICILIO_FISC_VARIATO_SIAN
  /*public Long insertDomicilioFiscaleVariatoSian(
      ISATDomicilioFiscaleVariato iSATDomicilioFiscaleVariato,
      Long idAziendaTributaria) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DOMICILIO_FISC_VARIATO_SIA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_DOMICILIO_FISC_VARIATO_SIAN "
          + "            (ID_DOMICILIO_FISC_VARIATO_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, " + "             DATA_FINE, "
          + "             CAP_DOMICILIO_FISCALE, "
          + "             INDIRIZZO_DOMICILIO_FISCALE, "
          + "             PROVINCIA_DOMICILIO_FISCALE, "
          + "             COMUNE_DOMICILIO_FISCALE) " + " VALUES " + "(?," + // ID_DOMICILIO_FISC_VARIATO_SIAN
                                                                              // 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // DATA_FINE 3
          "?," + // CAP_DOMICILIO_FISCALE 4
          "?," + // INDIRIZZO_DOMICILIO_FISCALE 5
          "?," + // PROVINCIA_DOMICILIO_FISCALE 6
          "?)"; // COMUNE_DOMICILIO_FISCALE 7

      stmt = conn.prepareStatement(insert);

      if (iSATDomicilioFiscaleVariato.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATDomicilioFiscaleVariato.getDataFine() "
                + iSATDomicilioFiscaleVariato.getDataFine().length());
      if (iSATDomicilioFiscaleVariato.getCapDomicilioFiscale() != null)
        SolmrLogger
            .debug(this,
                "\n\nLunghezza iSATDomicilioFiscaleVariato.getCapDomicilioFiscale() "
                    + iSATDomicilioFiscaleVariato.getCapDomicilioFiscale()
                        .length());
      if (iSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale()
                    .length());
      if (iSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale()
                    .length());
      if (iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale()
                    .length());

      if (iSATDomicilioFiscaleVariato.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATDomicilioFiscaleVariato.getDataFine() "
                + iSATDomicilioFiscaleVariato.getDataFine());
      if (iSATDomicilioFiscaleVariato.getCapDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATDomicilioFiscaleVariato.getCapDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getCapDomicilioFiscale());
      if (iSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getIndirizzoDomicilioFiscale());
      if (iSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getProvinciaDomicilioFiscale());
      if (iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATDomicilioFiscaleVariato.getComuneDomicilioFiscale() "
                + iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale());

      stmt.setLong(1, primaryKey.longValue()); // ID_DOMICILIO_FISC_VARIATO_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA

      if (Validator.isNotEmpty(iSATDomicilioFiscaleVariato.getDataFine()))
      {
        String dataTemp = iSATDomicilioFiscaleVariato.getDataFine().substring(
            0, 10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(3, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE
        else
          stmt.setDate(3, null); // DATA_FINE
      }
      else
        stmt.setDate(3, null); // DATA_FINE
      stmt.setString(4, iSATDomicilioFiscaleVariato.getCapDomicilioFiscale()); // CAP_DOMICILIO_FISCALE
      stmt.setString(5, iSATDomicilioFiscaleVariato
          .getIndirizzoDomicilioFiscale()); // INDIRIZZO_DOMICILIO_FISCALE
      stmt.setString(6, iSATDomicilioFiscaleVariato
          .getProvinciaDomicilioFiscale()); // PROVINCIA_DOMICILIO_FISCALE
      stmt
          .setString(7, iSATDomicilioFiscaleVariato.getComuneDomicilioFiscale()); // COMUNE_DOMICILIO_FISCALE

      SolmrLogger.debug(this, "Executing insertDomicilioFiscaleVariatoSian: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertDomicilioFiscaleVariatoSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in insertDomicilioFiscaleVariatoSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertDomicilioFiscaleVariatoSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertDomicilioFiscaleVariatoSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertDomicilioFiscaleVariatoSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_RAPPRESENTANTI_SOCIETA_SIAN
  /*public Long insertRappresentantiSocietaSian(
      ISATRappresentanteSocieta iSATRappresentanteSocieta,
      Long idAziendaTributaria) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_RAPPRESENTANTI_SOCIETA_SIA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_RAPPRESENTANTI_SOCIETA_SIAN "
          + "            (ID_RAPPRESENTANTI_SOCIETA_SIAN, "
          + "             ID_AZIENDA_TRIBUTARIA, "
          + "             CODICE_FISCALE, " + "             DATA_INIZIO, "
          + "             DATA_FINE, " + "             TIPO_CARICA, "
          + "             DESCRIZIONE_TIPO_CARICA, "
          + "             DECODIFICA_TIPO_CARICA, "
          + "             DECODIFICA_TIPO_CARICA_PF, "
          + "             DECODIFICA_TIPO_CARICA_PNF) " + " VALUES " + "(?," + // ID_RAPPRESENTANTI_SOCIETA_SIAN
                                                                                // 1
          "?," + // ID_AZIENDA_TRIBUTARIA 2
          "?," + // CODICE_FISCALE 3
          "?," + // DATA_INIZIO 4
          "?," + // DATA_FINE 5
          "?," + // TIPO_CARICA 6
          "?," + // DESCRIZIONE_TIPO_CARICA 7
          "?," + // DECODIFICA_TIPO_CARICA 8
          "?," + // DECODIFICA_TIPO_CARICA_PF 9
          "?)"; // DECODIFICA_TIPO_CARICA_PNF 10

      stmt = conn.prepareStatement(insert);

      if (iSATRappresentanteSocieta.getCodiceFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getCodiceFiscale() "
                + iSATRappresentanteSocieta.getCodiceFiscale().length());
      if (iSATRappresentanteSocieta.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getDataInizio() "
                + iSATRappresentanteSocieta.getDataInizio().length());
      if (iSATRappresentanteSocieta.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getDataFine() "
                + iSATRappresentanteSocieta.getDataFine().length());
      if (iSATRappresentanteSocieta.getTipoCarica() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getTipoCarica() "
                + iSATRappresentanteSocieta.getTipoCarica().length());
      if (iSATRappresentanteSocieta.getTipoCaricaDesc() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getTipoCaricaDesc() "
                + iSATRappresentanteSocieta.getTipoCaricaDesc().length());
      if (iSATRappresentanteSocieta.getDecoTipoCari() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getDecoTipoCari() "
                + iSATRappresentanteSocieta.getDecoTipoCari().length());
      if (iSATRappresentanteSocieta.getDecoTipoCariPf() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getDecoTipoCariPf() "
                + iSATRappresentanteSocieta.getDecoTipoCariPf().length());
      if (iSATRappresentanteSocieta.getDecoTipoCariPnf() != null)
        SolmrLogger.debug(this,
            "\n\nLunghezza iSATRappresentanteSocieta.getDecoTipoCariPnf() "
                + iSATRappresentanteSocieta.getDecoTipoCariPnf().length());

      if (iSATRappresentanteSocieta.getCodiceFiscale() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getCodiceFiscale() "
                + iSATRappresentanteSocieta.getCodiceFiscale());
      if (iSATRappresentanteSocieta.getDataInizio() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getDataInizio() "
                + iSATRappresentanteSocieta.getDataInizio());
      if (iSATRappresentanteSocieta.getDataFine() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getDataFine() "
                + iSATRappresentanteSocieta.getDataFine());
      if (iSATRappresentanteSocieta.getTipoCarica() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getTipoCarica() "
                + iSATRappresentanteSocieta.getTipoCarica());
      if (iSATRappresentanteSocieta.getTipoCaricaDesc() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getTipoCaricaDesc() "
                + iSATRappresentanteSocieta.getTipoCaricaDesc());
      if (iSATRappresentanteSocieta.getDecoTipoCari() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getDecoTipoCari() "
                + iSATRappresentanteSocieta.getDecoTipoCari());
      if (iSATRappresentanteSocieta.getDecoTipoCariPf() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getDecoTipoCariPf() "
                + iSATRappresentanteSocieta.getDecoTipoCariPf());
      if (iSATRappresentanteSocieta.getDecoTipoCariPnf() != null)
        SolmrLogger.debug(this,
            "\n\nContenutoiSATRappresentanteSocieta.getDecoTipoCariPnf() "
                + iSATRappresentanteSocieta.getDecoTipoCariPnf());

      stmt.setLong(1, primaryKey.longValue()); // ID_RAPPRESENTANTI_SOCIETA_SIAN
      stmt.setLong(2, idAziendaTributaria.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setString(3, iSATRappresentanteSocieta.getCodiceFiscale()); // CODICE_FISCALE

      if (Validator.isNotEmpty(iSATRappresentanteSocieta.getDataInizio()))
      {
        String dataTemp = iSATRappresentanteSocieta.getDataInizio().substring(
            0, 10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(4, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_INIZIO
        else
          stmt.setDate(4, null); // DATA_INIZIO
      }
      else
        stmt.setDate(4, null); // DATA_INIZIO

      if (Validator.isNotEmpty(iSATRappresentanteSocieta.getDataFine()))
      {
        String dataTemp = iSATRappresentanteSocieta.getDataFine().substring(0,
            10);
        if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
          stmt.setDate(5, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE
        else
          stmt.setDate(5, null); // DATA_FINE
      }
      else
        stmt.setDate(5, null); // DATA_FINE
      stmt.setString(6, iSATRappresentanteSocieta.getTipoCarica()); // TIPO_CARICA
      stmt.setString(7, iSATRappresentanteSocieta.getTipoCaricaDesc()); // DESCRIZIONE_TIPO_CARICA
      stmt.setString(8, iSATRappresentanteSocieta.getDecoTipoCari()); // DECODIFICA_TIPO_CARICA
      stmt.setString(9, iSATRappresentanteSocieta.getDecoTipoCariPf()); // DECODIFICA_TIPO_CARICA_PF
      stmt.setString(10, iSATRappresentanteSocieta.getDecoTipoCariPnf()); // DECODIFICA_TIPO_CARICA_PNF

      SolmrLogger.debug(this, "Executing insertRappresentantiSocietaSian: "
          + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertRappresentantiSocietaSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in insertRappresentantiSocietaSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertRappresentantiSocietaSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertRappresentantiSocietaSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertRappresentantiSocietaSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al codice specie
   * 
   * @param codiceSpecie
   *          String
   * @return StringcodeDescription
   * @throws DataAccessException
   * @throws SolmrException
   */
  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(
      String codiceSpecie) throws DataAccessException, SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating method getSianTipoSpecieByCodiceSpecie in SianDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    StringcodeDescription sianTipoSpecie = null;

    try
    {
      SolmrLogger
          .debug(this,
              "Creating db_connection in method getSianTipoSpecieByCodiceSpecie in SianDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getSianTipoSpecieByCodiceSpecie in SianDAO with value: "
                  + conn);

      SolmrLogger
          .debug(this,
              "Creating query in method getSianTipoSpecieByCodiceSpecie in SianDAO");

      String query = " SELECT STS.CODICE_SPECIE, "
          + "        STS.DESCRIZIONE, " + "        STS.ID_TIPO_SPECIE_ANIMALE "
          + " FROM   DB_SIAN_TIPO_SPECIE STS "
          + " WHERE  STS.CODICE_SPECIE = ? "
          + " AND DATA_FINE_VALIDITA IS NULL ";

      SolmrLogger.debug(this,
          "Created query in method getSianTipoSpecieByCodiceSpecie in SianDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CODICE_SPECIE] in method getSianTipoSpecieByCodiceSpecie in SianDAO: "
                  + codiceSpecie);
      stmt.setString(1, codiceSpecie);

      SolmrLogger.debug(this, "Executing getSianTipoSpecieByCodiceSpecie: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        sianTipoSpecie = new StringcodeDescription();
        sianTipoSpecie.setCode(rs.getString(1));
        sianTipoSpecie.setDescription(rs.getString(2));
        if (Validator.isNotEmpty(rs.getString(3)))
        {
          sianTipoSpecie.setSecondaryCode(new Long(rs.getLong(3)));
        }
      }

      if (sianTipoSpecie == null)
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_NO_CODICE_SPECIE_SIAN"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .error(this, "getSianTipoSpecieByCodiceSpecie - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException se)
    {
      SolmrLogger.error(this,
          "getSianTipoSpecieByCodiceSpecie - SolmrException: " + se);
      throw new SolmrException(se.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getSianTipoSpecieByCodiceSpecie - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getSianTipoSpecieByCodiceSpecie - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getSianTipoSpecieByCodiceSpecie - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getSianTipoSpecieByCodiceSpecie in SianDAO");
    return sianTipoSpecie;
  }

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al idTipoSpecieAnimale
   * 
   * @param idTipoSpecieAnimale
   *          long
   * @return StringcodeDescription
   * @throws DataAccessException
   */
  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(
      long idTipoSpecieAnimale) throws DataAccessException
  {
    SolmrLogger.debug(this,
        "Invocating method getSianTipoSpecieByIdSpecieAnimale in SianDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    StringcodeDescription sianTipoSpecie = null;

    try
    {
      SolmrLogger
          .debug(
              this,
              "Creating db_connection in method getSianTipoSpecieByIdSpecieAnimale in SianDAO");
      conn = getDatasource().getConnection();
      SolmrLogger
          .debug(
              this,
              "Created db_connection in method getSianTipoSpecieByIdSpecieAnimale in SianDAO with value: "
                  + conn);

      SolmrLogger
          .debug(this,
              "Creating query in method getSianTipoSpecieByIdSpecieAnimale in SianDAO");

      String query = " SELECT S.CODICE_SPECIE "
          + " FROM   DB_R_SPECIE_AN_SIAN_SIAP S "
          + " WHERE S.ID_SPECIE_ANIMALE = ? "
          + " AND S.FLAG_RELAZIONE_ATTIVA = 'S' ";

      SolmrLogger
          .debug(this,
              "Created query in method getSianTipoSpecieByIdSpecieAnimale in SianDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CODICE_SPECIE] in method getSianTipoSpecieByIdSpecieAnimale in SianDAO: "
                  + idTipoSpecieAnimale);
      stmt.setLong(1, idTipoSpecieAnimale);

      SolmrLogger.debug(this, "Executing getSianTipoSpecieByIdSpecieAnimale: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        sianTipoSpecie = new StringcodeDescription();
        sianTipoSpecie.setCode(rs.getString("CODICE_SPECIE"));
      }

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this,
          "getSianTipoSpecieByIdSpecieAnimale - SQLException: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this,
          "getSianTipoSpecieByIdSpecieAnimale - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .error(
                this,
                "getSianTipoSpecieByIdSpecieAnimale - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getSianTipoSpecieByIdSpecieAnimale - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this,
        "Invocated method getSianTipoSpecieByIdSpecieAnimale in SianDAO");
    return sianTipoSpecie;
  }

  /**
   * Metodo per recuperare la descrizione del carico nascita proveniente da
   * Teramo
   * 
   * @param caricoNascita
   *          String
   * @return StringcodeDescription
   * @throws DataAccessException
   */
  public String getDescCaricoNascita(String caricoNascita)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating method getDescCaricoNascita in SianDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    String caricoNascitaDesc = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating db_connection in method getDescCaricoNascita in SianDAO");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created db_connection in method getDescCaricoNascita in SianDAO with value: "
              + conn);

      SolmrLogger.debug(this,
          "Creating query in method getDescCaricoNascita in SianDAO");

      String query = " SELECT TC.DESCRIZIONE "
          + " FROM DB_BDN_TIPO_CARICOALLEVAMENTO TC "
          + " WHERE TC.CARICO_NASCITA = ? "
          + " AND TC.DATA_FINE_VALIDITA IS NULL ";

      SolmrLogger.debug(this,
          "Created query in method getDescCaricoNascita in SianDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CARICO_NASCITA] in method getDescCaricoNascita in SianDAO: "
                  + caricoNascita);
      stmt.setString(1, caricoNascita);

      SolmrLogger.debug(this, "Executing getDescCaricoNascita: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        caricoNascitaDesc = rs.getString("DESCRIZIONE");

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDescCaricoNascita - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .error(this, "getDescCaricoNascita - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "getDescCaricoNascita - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getDescCaricoNascita - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getDescCaricoNascita in SianDAO");
    return caricoNascitaDesc;
  }

  /**
   * Metodo per recuperare la descrizione del carico nascita proveniente da
   * Teramo
   * 
   * @param caricoNascita
   *          String
   * @return StringcodeDescription
   * @throws DataAccessException
   */
  public String getDescScaricoMorte(String scaricoMorte)
      throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating method getDescScaricoMorte in SianDAO");
    Connection conn = null;
    PreparedStatement stmt = null;
    String scaricoMorteDesc = null;

    try
    {
      SolmrLogger.debug(this,
          "Creating db_connection in method getDescScaricoMorte in SianDAO");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created db_connection in method getDescScaricoMorte in SianDAO with value: "
              + conn);

      SolmrLogger.debug(this,
          "Creating query in method getDescScaricoMorte in SianDAO");

      String query = " SELECT TS.DESCRIZIONE "
          + " FROM DB_BDN_TIPO_SCARICOALLEVAMENTO TS "
          + " WHERE TS.SCARICO_MORTE = ? "
          + " AND TS.DATA_FINE_VALIDITA IS NULL ";

      SolmrLogger.debug(this,
          "Created query in method getDescScaricoMorte in SianDAO");

      stmt = conn.prepareStatement(query);

      SolmrLogger
          .debug(
              this,
              "Value of parameter 1 [CARICO_NASCITA] in method getDescScaricoMorte in SianDAO: "
                  + scaricoMorte);
      stmt.setString(1, scaricoMorte);

      SolmrLogger.debug(this, "Executing getDescScaricoMorte: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        scaricoMorteDesc = rs.getString("DESCRIZIONE");

      stmt.close();
      rs.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getDescScaricoMorte - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getDescScaricoMorte - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "getDescScaricoMorte - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getDescScaricoMorte - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated method getDescScaricoMorte in SianDAO");
    return scaricoMorteDesc;
  }

  // Metodo per eliminare un record dalla tabella DB_ALLEVAMENTI_SIAN
  public void deleteAllevamentiSian(String cuaa) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_ALLEVAMENTI_SIAN " + " WHERE  CUAA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setString(1, cuaa.toUpperCase());

      SolmrLogger.debug(this, "Executing deleteAllevamentiSian: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteAllevamentiSian with CUAA = "
          + cuaa.toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteAllevamentiSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteAllevamentiSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteAllevamentiSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteAllevamentiSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per eliminare un record dalla tabella DB_FASCICOLO_SIAN
  /*public void deleteFascicoloSian(String cuaa) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_FASCICOLO_SIAN " + " WHERE  CUAA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setString(1, cuaa.toUpperCase());

      SolmrLogger.debug(this, "Executing deleteFascicoloSian: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteFascicoloSian with CUAA = "
          + cuaa.toUpperCase());
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteFascicoloSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteFascicoloSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in deleteFascicoloSian: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteFascicoloSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }*/

  // Metodo per inserire un record su DB_FASCICOLO_SIAN
  /*public Long insertFascicoloSian(
      it.csi.sian.oprFascicolo.ISWSToOprResponse oprResponse, String cuaa,
      String istatComuneNascitaPF, StringBuffer codErr, StringBuffer msgErr,
      StringBuffer flagPresente, boolean isPresente) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_ALLEVAMENTI_SIAN);
      conn = getDatasource().getConnection();

      String insert = "INSERT INTO DB_FASCICOLO_SIAN ( "
          + "ID_FASCICOLO_SIAN, " + "CUAA, " + "CUAA_SIAN, " + "TIPO_AZIENDA, "
          + "DENOMINAZIONE, " + "NOME_PF, " + "SESSO_PF, "
          + "DATA_NASCITA_PF, " + "COMUNE_NASCITA_PF, "
          + "ISTAT_COMUNE_NASCITA_PF, " + "PARTITA_IVA, " + "ISCRIZIONE_REA, "
          + "CODICE_REGISTRO_IMPRESE, " + "CODICE_INPS, "
          + "ORGANISMO_PAGATORE, " + "DATA_APERTURA_FASCICOLO, "
          + "DATA_CHIUSURA_FASCICOLO, " + "CODICE_TIPO_DETENTORE, "
          + "DETENTORE, " + "DATA_VALIDAZIONE_FASCICOLO, "
          + "DATA_ELABORAZIONE, " + "PRESENZA_SIAN, " + "MESSAGGIO_ERRORE, "
          + "ISTAT_COMUNE_RESIDENZA, " + "INDIRIZZO_RESIDENZA, "
          + "CAP_RESIDENZA, " + "CODICE_STATO_ESTERO_RESIDENZA, "
          + "ISTAT_COMUNE_RECAPITO, " + "INDIRIZZO_RECAPITO, "
          + "CAP_RECAPITO, " + "CODICE_STATO_ESTERO_RECAPITO, "
          + "DATA_ULTIMO_AGGIORNAMENTO) " + "VALUES ( "
          + "SEQ_FASCICOLO_SIAN.NEXTVAL," + // ID_FASCICOLO_SIAN
          "? ," + // CUAA // 1
          "? ," + // CUAA_SIAN // 2
          "?, " + // TIPO_AZIENDA // 3
          "?, " + // DENOMINAZIONE // 4
          "?, " + // NOME_PF // 5
          "?, " + // SESSO_PF // 6
          "?, " + // DATA_NASCITA_PF // 7
          "?, " + // COMUNE_NASCITA_PF // 8
          "?, " + // ISTAT_COMUNE_NASCITA_PF // 9
          "?, " + // PARTITA_IVA //10
          "?, " + // ISCRIZIONE_REA //11
          "?, " + // CODICE_REGISTRO_IMPRESE //12
          "?, " + // CODICE_INPS //13
          "?, " + // ORGANISMO_PAGATORE //14
          "?, " + // DATA_APERTURA_FASCICOLO //15
          "?, " + // DATA_CHIUSURA_FASCICOLO //16
          "?, " + // CODICE_TIPO_DETENTORE //17
          "?, " + // DETENTORE //18
          "?, " + // DATA_VALIDAZIONE_FASCICOLO //19
          "?, " + // DATA_ELABORAZIONE //20
          "?, " + // PRESENZA_SIAN //21
          "?, " + // MESSAGGIO_ERRORE //22
          "?, " + // ISTAT_COMUNE_RESIDENZA //23
          "?, " + // INDIRIZZO_RESIDENZA //24
          "?, " + // CAP_RESIDENZA //25
          "?, " + // CODICE_STATO_ESTERO_RESIDENZA //26
          "?, " + // ISTAT_COMUNE_RECAPITO //27
          "?, " + // INDIRIZZO_RECAPITO //28
          "?, " + // CAP_RECAPITO //29
          "?, " + // CODICE_STATO_ESTERO_RECAPITO //30
          "SYSDATE) "; // DATA_ULTIMO_AGGIORNAMENTO

      stmt = conn.prepareStatement(insert);

      if (isPresente)
      {
        ISWSRespAnagFascicolo2 resp = oprResponse.getRisposta10();

        SolmrLogger.debug(this, "cuaa " + cuaa);
        SolmrLogger.debug(this, "resp.getCUAA() " + resp.getCUAA());
        if (resp.getTipoAzienda() != null)
          SolmrLogger.debug(this, "resp.getTipoAzienda().getValue() "
              + resp.getTipoAzienda().getValue());
        SolmrLogger.debug(this, "resp.getDenominazione() "
            + resp.getDenominazione());
        SolmrLogger.debug(this, "resp.getNomePF() " + resp.getNomePF());
        if (resp.getSessoPF() != null)
          SolmrLogger.debug(this, "resp.getSessoPF().getValue() "
              + resp.getSessoPF().getValue());

        SolmrLogger.debug(this, "resp.getDataNascitaPF() "
            + resp.getDataNascitaPF());

        SolmrLogger.debug(this, "resp.getComuneNascitaPF() "
            + resp.getComuneNascitaPF());
        SolmrLogger.debug(this, "istatComuneNascitaPF " + istatComuneNascitaPF);
        SolmrLogger.debug(this, "resp.getPartitaIVA() " + resp.getPartitaIVA());
        SolmrLogger.debug(this, "resp.getIscrizioneRea() "
            + resp.getIscrizioneRea());
        SolmrLogger.debug(this, "resp.getIscrizioneRegistroImprese() "
            + resp.getIscrizioneRegistroImprese());
        SolmrLogger.debug(this, "resp.getCodiceInps() " + resp.getCodiceInps());
        SolmrLogger.debug(this, "resp.getOrganismoPagatore() "
            + resp.getOrganismoPagatore());
        SolmrLogger.debug(this, "resp.getDataAperturaFascicolo() "
            + resp.getDataAperturaFascicolo());
        SolmrLogger.debug(this, "resp.getDataChiusuraFascicolo() "
            + resp.getDataChiusuraFascicolo());
        SolmrLogger.debug(this, "resp.getDetentore() " + resp.getDetentore());
        if (resp.getTipoDetentore() != null)
          SolmrLogger.debug(this, "resp.getTipoDetentore().getValue() "
              + resp.getTipoDetentore().getValue());

        SolmrLogger.debug(this, "resp.getDataValidazFascicolo() "
            + resp.getDataValidazFascicolo());
        SolmrLogger.debug(this, "resp.getDataElaborazione() "
            + resp.getDataElaborazione());

        stmt.setString(1, cuaa.toUpperCase()); // CUAA
        stmt.setString(2, resp.getCUAA()); // CUAA_SIAN

        if (resp.getTipoAzienda() != null)
          stmt.setString(3, resp.getTipoAzienda().getValue()); // TIPO_AZIENDA
        else
          stmt.setString(3, null); // TIPO_AZIENDA

        stmt.setString(4, resp.getDenominazione()); // DENOMINAZIONE
        stmt.setString(5, resp.getNomePF()); // NOME_PF

        if (resp.getSessoPF() != null)
          stmt.setString(6, resp.getSessoPF().getValue()); // SESSO_PF
        else
          stmt.setString(6, null); // SESSO_PF

        if (Validator.isNotEmpty(resp.getDataNascitaPF()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyyMMdd", "dd/MM/yyyy", resp.getDataNascitaPF());
          stmt.setDate(7, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_NASCITA_PF
        }
        else
          stmt.setString(7, null); // DATA_NASCITA_PF

        stmt.setString(8, resp.getComuneNascitaPF()); // COMUNE_NASCITA_PF
        stmt.setString(9, istatComuneNascitaPF); // ISTAT_COMUNE_NASCITA_PF
        stmt.setString(10, resp.getPartitaIVA()); // PARTITA_IVA
        stmt.setString(11, resp.getIscrizioneRea()); // ISCRIZIONE_REA
        stmt.setString(12, resp.getIscrizioneRegistroImprese()); // CODICE_REGISTRO_IMPRESE
        stmt.setString(13, resp.getCodiceInps()); // CODICE_INPS
        stmt.setString(14, resp.getOrganismoPagatore()); // ORGANISMO_PAGATORE

        if (Validator.isNotEmpty(resp.getDataAperturaFascicolo()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyyMMdd", "dd/MM/yyyy", resp.getDataAperturaFascicolo());
          stmt.setDate(15, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_APERTURA_FASCICOLO
        }
        else
          stmt.setString(15, null); // DATA_APERTURA_FASCICOLO
        if (Validator.isNotEmpty(resp.getDataChiusuraFascicolo()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyyMMdd", "dd/MM/yyyy", resp.getDataChiusuraFascicolo());
          stmt.setDate(16, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_CHIUSURA_FASCICOLO
        }
        else
          stmt.setString(16, null); // DATA_CHIUSURA_FASCICOLO

        if (resp.getTipoDetentore() != null)
          stmt.setString(17, resp.getTipoDetentore().getValue()); // CODICE_TIPO_DETENTORE
        else
          stmt.setString(17, null); // CODICE_TIPO_DETENTORE

        stmt.setString(18, resp.getDetentore()); // DETENTORE

        if (Validator.isNotEmpty(resp.getDataValidazFascicolo()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyyMMdd", "dd/MM/yyyy", resp.getDataValidazFascicolo());
          stmt.setDate(19, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_VALIDAZIONE_FASCICOLO
        }
        else
          stmt.setString(19, null); // DATA_VALIDAZIONE_FASCICOLO

        if (Validator.isNotEmpty(resp.getDataElaborazione()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyyMMdd", "dd/MM/yyyy", resp.getDataElaborazione());
          stmt.setDate(20, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_ELABORAZIONE
        }
        else
          stmt.setString(20, null); // DATA_ELABORAZIONE

        stmt.setString(21, flagPresente.toString()); // PRESENZA_SIAN
        stmt.setString(22, codErr.toString() + " -- " + msgErr.toString()); // MESSAGGIO_ERRORE

        if (resp.getSedeResidenza() != null)
        {
          stmt.setString(23, StringUtils.checkNull(resp.getSedeResidenza()
              .getProvincia())
              + StringUtils.checkNull(resp.getSedeResidenza().getComune())); // ISTAT_COMUNE_RESIDENZA
          stmt.setString(24, resp.getSedeResidenza().getIndirizzo()); // INDIRIZZO_RESIDENZA
          stmt.setString(25, resp.getSedeResidenza().getCap()); // CAP_RESIDENZA
          stmt.setString(26, resp.getSedeResidenza().getCodiceStatoEstero()); // CODICE_STATO_ESTERO_RESIDENZA
        }
        else
        {
          stmt.setString(23, null); // ISTAT_COMUNE_RESIDENZA
          stmt.setString(24, null); // INDIRIZZO_RESIDENZA
          stmt.setString(25, null); // CAP_RESIDENZA
          stmt.setString(26, null); // CODICE_STATO_ESTERO_RESIDENZA
        }

        if (resp.getRecapito() != null)
        {
          stmt.setString(27, StringUtils.checkNull(resp.getRecapito()
              .getProvincia())
              + StringUtils.checkNull(resp.getRecapito().getComune())); // ISTAT_COMUNE_RECAPITO
          stmt.setString(28, resp.getRecapito().getIndirizzo()); // INDIRIZZO_RECAPITO
          stmt.setString(29, resp.getRecapito().getCap()); // CAP_RECAPITO
          stmt.setString(30, resp.getRecapito().getCodiceStatoEstero()); // CODICE_STATO_ESTERO_RECAPITO
        }
        else
        {
          stmt.setString(27, null); // ISTAT_COMUNE_RECAPITO
          stmt.setString(28, null); // INDIRIZZO_RECAPITO
          stmt.setString(29, null); // CAP_RECAPITO
          stmt.setString(30, null); // CODICE_STATO_ESTERO_RECAPITO
        }

      }
      else
      {
        stmt.setString(1, cuaa.toUpperCase()); // CUAA
        stmt.setString(2, null);
        stmt.setString(3, null);
        stmt.setString(4, null);
        stmt.setString(5, null);
        stmt.setString(6, null);
        stmt.setString(7, null);
        stmt.setString(8, null);
        stmt.setString(9, null);
        stmt.setString(10, null);
        stmt.setString(11, null);
        stmt.setString(12, null);
        stmt.setString(13, null);
        stmt.setString(14, null);
        stmt.setString(15, null);
        stmt.setString(16, null);
        stmt.setString(17, null);
        stmt.setString(18, null);
        stmt.setString(19, null);
        stmt.setString(20, null);
        stmt.setString(21, flagPresente.toString()); // PRESENZA_SIAN
        stmt.setString(22, codErr.toString() + " -- " + msgErr.toString()); // MESSAGGIO_ERRORE
        stmt.setString(23, null);
        stmt.setString(24, null);
        stmt.setString(25, null);
        stmt.setString(26, null);
        stmt.setString(27, null);
        stmt.setString(28, null);
        stmt.setString(29, null);
        stmt.setString(30, null);
      }

      SolmrLogger.debug(this, "Executing insertFascicoloSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertFascicoloSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertFascicoloSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertFascicoloSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in insertFascicoloSian: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertFascicoloSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }*/

  // Metodo per inserire un record su DB_ALLEVAMENTI_SIAN
  public Long insertAllevamentiSian(SianAllevamentiVO sianAllevamentiVO,
      String cuaa, String messaggioErrore, String flagPresenzaSian,
      boolean isPresenteAt) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_ALLEVAMENTI_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_ALLEVAMENTI_SIAN "
          + "            (ID_ALLEVAMENTI_SIAN, " + "             CUAA, "
          + "             CODICE_SPECIE, " + "             CODICE_AZIENDA, "
          + "             ALLEVAMENTO_ID_BDN, "
          + "             DENOMINAZIONE, " + "             INDIRIZZO, "
          + "             CAP, " + "             LOCALITA, "
          + "             COMUNE, " + "             TIPO_PRODUZIONE, "
          + "             AUTORIZZAZIONE_LATTE, "
          + "             DATA_INIZIO_ATTIVITA, "
          + "             DATA_FINE_ATTIVITA, "
          + "             CODICE_FISCALE_PROPRIETARIO, "
          + "             DENOMINAZIONE_PROPRIETARIO, "
          + "             CODICE_FISCALE_DETENTORE, "
          + "             DENOMINAZIONE_DETENTORE, "
          + "             DATA_INIZIO_DETENTORE, "
          + "             DATA_FINE_DETENTORE, " + "             SOCCIDA, "
          + "             LATITUDINE, " + "             LONGITUDINE, "
          + "             FOGLIO, " + "             SEZIONE, "
          + "             PARTICELLA, " + "             SUBALTERNO, "
          + "             CAPI_TOTALI, " + "             DATA_CALCOLO_CAPI, "
          + "             DATA_AGGIORNAMENTO, "
          + "             FLAG_PRESENZA_SIAN, "
          + "             MESSAGGIO_ERRORE, "
          + "              TIPO_ORIENTAMENTO)" +

          " VALUES " +

          "(?," + // ID_ALLEVAMENTI_SIAN 1
          "?," + // CUAA 2
          "?," + // CODICE_SPECIE 3
          "?," + // CODICE_AZIENDA 4
          "?," + // ALLEVAMENTO_ID_BDN 5
          "?," + // DENOMINAZIONE 6
          "?," + // INDIRIZZO 7
          "?," + // CAP 8
          "?," + // LOCALITA 9
          "?," + // COMUNE 10
          "?," + // TIPO_PRODUZIONE 1
          "?," + // AUTORIZZAZIONE_LATTE 12
          "?," + // DATA_INIZIO_ATTIVITA 13
          "?," + // DATA_FINE_ATTIVITA 14
          "?," + // CODICE_FISCALE_PROPRIETARIO 15
          "?," + // DENOMINAZIONE_PROPRIETARIO 16
          "?," + // CODICE_FISCALE_DETENTORE 17
          "?," + // DENOMINAZIONE_DETENTORE 18
          "?," + // DATA_INIZIO_DETENTORE 19
          "?," + // DATA_FINE_DETENTORE 20
          "?," + // SOCCIDA 21
          "?," + // LATITUDINE 22
          "?," + // LONGITUDINE 23
          "?," + // FOGLIO 24
          "?," + // SEZIONE 25
          "?," + // PARTICELLA 26
          "?," + // SUBALTERNO 27
          "?," + // CAPI_TOTALI 28
          "?," + // DATA_CALCOLO_CAPI 29
          "SYSDATE," + // DATA_AGGIORNAMENTO
          "?," + // FLAG_PRESENZA_SIAN 30
          "?," + // MESSAGGIO_ERRORE 31
          "?)"; // TIPO_ORIENTAMENTO 32

      stmt = conn.prepareStatement(insert);

      if (isPresenteAt)
      {
        if (sianAllevamentiVO.getSpeCodice() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getSpeCodice() "
                  + sianAllevamentiVO.getSpeCodice().length());
        if (sianAllevamentiVO.getAziendaCodice() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getAziendaCodice() "
                  + sianAllevamentiVO.getAziendaCodice().length());
        if (sianAllevamentiVO.getAllevId() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO "
              + sianAllevamentiVO.getAllevId());
        if (sianAllevamentiVO.getDenominazione() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDenominazione() "
                  + sianAllevamentiVO.getDenominazione().length());
        if (sianAllevamentiVO.getIndirizzo() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getIndirizzo() "
                  + sianAllevamentiVO.getIndirizzo().length());
        if (sianAllevamentiVO.getCap() != null)
          SolmrLogger.debug(this, "\n\nLunghezza sianAllevamentiVO.getCap() "
              + sianAllevamentiVO.getCap().length());
        if (sianAllevamentiVO.getLocalita() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getLocalita() "
                  + sianAllevamentiVO.getLocalita().length());
        if (sianAllevamentiVO.getComune() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getComune() "
                  + sianAllevamentiVO.getComune().length());
        if (sianAllevamentiVO.getTipoProduzione() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getTipoProduzione() "
                  + sianAllevamentiVO.getTipoProduzione().length());
        if (sianAllevamentiVO.getAutorizzazioneLatte() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getAutorizzazioneLatte() "
                  + sianAllevamentiVO.getAutorizzazioneLatte().length());
        if (sianAllevamentiVO.getDtInizioAttivita() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDtInizioAttivita() "
                  + sianAllevamentiVO.getDtInizioAttivita().length());
        if (sianAllevamentiVO.getDtFineAttivita() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDtFineAttivita() "
                  + sianAllevamentiVO.getDtFineAttivita().length());
        if (sianAllevamentiVO.getCodFiscaleProp() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getCodFiscaleProp() "
                  + sianAllevamentiVO.getCodFiscaleProp().length());
        if (sianAllevamentiVO.getDenomProprietario() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDenomProprietario() "
                  + sianAllevamentiVO.getDenomProprietario().length());
        if (sianAllevamentiVO.getCodFiscaleDeten() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getCodFiscaleDeten() "
                  + sianAllevamentiVO.getCodFiscaleDeten().length());
        if (sianAllevamentiVO.getDenomDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDenomDetentore() "
                  + sianAllevamentiVO.getDenomDetentore().length());
        if (sianAllevamentiVO.getDtInizioDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDtInizioDetentore() "
                  + sianAllevamentiVO.getDtInizioDetentore().length());
        if (sianAllevamentiVO.getDtFineDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDtFineDetentore() "
                  + sianAllevamentiVO.getDtFineDetentore().length());
        if (sianAllevamentiVO.getSoccida() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getSoccida() "
                  + sianAllevamentiVO.getSoccida().length());
        if (sianAllevamentiVO.getLatitudine() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO.getLatitudine() "
              + sianAllevamentiVO.getLatitudine());
        if (sianAllevamentiVO.getLongitudine() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO.getLongitudine() "
              + sianAllevamentiVO.getLongitudine());
        if (sianAllevamentiVO.getFoglioCatastale() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getFoglioCatastale() "
                  + sianAllevamentiVO.getFoglioCatastale().length());
        if (sianAllevamentiVO.getSezione() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getSezione() "
                  + sianAllevamentiVO.getSezione().length());
        if (sianAllevamentiVO.getParticella() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getParticella() "
                  + sianAllevamentiVO.getParticella().length());
        if (sianAllevamentiVO.getSubalterno() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getSubalterno() "
                  + sianAllevamentiVO.getSubalterno().length());
        if (sianAllevamentiVO.getCapiTotali() != null)
          SolmrLogger.debug(this, "\n\nLunghezza sianAllevamentiVO "
              + sianAllevamentiVO.getCapiTotali());
        if (sianAllevamentiVO.getDataCalcoloCapi() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getDataCalcoloCapi() "
                  + sianAllevamentiVO.getDataCalcoloCapi().length());
        if (sianAllevamentiVO.getOrientamentoProduttivo() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza sianAllevamentiVO.getOrientamentoProduttivo() "
                  + sianAllevamentiVO.getOrientamentoProduttivo().length());
        if (sianAllevamentiVO.getSpeCodice() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getSpeCodice() "
                  + sianAllevamentiVO.getSpeCodice());
        if (sianAllevamentiVO.getAziendaCodice() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getAziendaCodice() "
                  + sianAllevamentiVO.getAziendaCodice());
        if (sianAllevamentiVO.getAllevId() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO "
              + sianAllevamentiVO.getAllevId());
        if (sianAllevamentiVO.getDenominazione() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDenominazione() "
                  + sianAllevamentiVO.getDenominazione());
        if (sianAllevamentiVO.getIndirizzo() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getIndirizzo() "
                  + sianAllevamentiVO.getIndirizzo());
        if (sianAllevamentiVO.getCap() != null)
          SolmrLogger.debug(this, "\n\nContenutosianAllevamentiVO.getCap() "
              + sianAllevamentiVO.getCap());
        if (sianAllevamentiVO.getLocalita() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getLocalita() "
                  + sianAllevamentiVO.getLocalita());
        if (sianAllevamentiVO.getComune() != null)
          SolmrLogger.debug(this, "\n\nContenutosianAllevamentiVO.getComune() "
              + sianAllevamentiVO.getComune());
        if (sianAllevamentiVO.getTipoProduzione() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getTipoProduzione() "
                  + sianAllevamentiVO.getTipoProduzione());
        if (sianAllevamentiVO.getAutorizzazioneLatte() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getAutorizzazioneLatte() "
                  + sianAllevamentiVO.getAutorizzazioneLatte());
        if (sianAllevamentiVO.getDtInizioAttivita() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDtInizioAttivita() "
                  + sianAllevamentiVO.getDtInizioAttivita());
        if (sianAllevamentiVO.getDtFineAttivita() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDtFineAttivita() "
                  + sianAllevamentiVO.getDtFineAttivita());
        if (sianAllevamentiVO.getCodFiscaleProp() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getCodFiscaleProp() "
                  + sianAllevamentiVO.getCodFiscaleProp());
        if (sianAllevamentiVO.getDenomProprietario() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDenomProprietario() "
                  + sianAllevamentiVO.getDenomProprietario());
        if (sianAllevamentiVO.getCodFiscaleDeten() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getCodFiscaleDeten() "
                  + sianAllevamentiVO.getCodFiscaleDeten());
        if (sianAllevamentiVO.getDenomDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDenomDetentore() "
                  + sianAllevamentiVO.getDenomDetentore());
        if (sianAllevamentiVO.getDtInizioDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDtInizioDetentore() "
                  + sianAllevamentiVO.getDtInizioDetentore());
        if (sianAllevamentiVO.getDtFineDetentore() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDtFineDetentore() "
                  + sianAllevamentiVO.getDtFineDetentore());
        if (sianAllevamentiVO.getSoccida() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getSoccida() "
                  + sianAllevamentiVO.getSoccida());
        if (sianAllevamentiVO.getLatitudine() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO.getLatitudine() "
              + sianAllevamentiVO.getLatitudine());
        if (sianAllevamentiVO.getLongitudine() != null)
          SolmrLogger.debug(this, "\n\nsianAllevamentiVO.getLongitudine() "
              + sianAllevamentiVO.getLongitudine());
        if (sianAllevamentiVO.getFoglioCatastale() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getFoglioCatastale() "
                  + sianAllevamentiVO.getFoglioCatastale());
        if (sianAllevamentiVO.getSezione() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getSezione() "
                  + sianAllevamentiVO.getSezione());
        if (sianAllevamentiVO.getParticella() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getParticella() "
                  + sianAllevamentiVO.getParticella());
        if (sianAllevamentiVO.getSubalterno() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getSubalterno() "
                  + sianAllevamentiVO.getSubalterno());
        if (sianAllevamentiVO.getCapiTotali() != null)
          SolmrLogger.debug(this,
              "\n\nContenuto sianAllevamentiVO.getCapiTotali "
                  + sianAllevamentiVO.getCapiTotali());
        if (sianAllevamentiVO.getDataCalcoloCapi() != null)
          SolmrLogger.debug(this,
              "\n\nContenutosianAllevamentiVO.getDataCalcoloCapi() "
                  + sianAllevamentiVO.getDataCalcoloCapi());
        SolmrLogger.debug(this,
            "\n\nContenuto sianAllevamentiVO.getOrientamentoProduttivo() "
                + sianAllevamentiVO.getOrientamentoProduttivo());

      }

      if (isPresenteAt)
      {
        stmt.setLong(1, primaryKey.longValue()); // ID_ALLEVAMENTI_SIAN
        stmt.setString(2, cuaa.toUpperCase()); // CUAA
        stmt.setString(3, sianAllevamentiVO.getSpeCodice()); // CODICE_SPECIE
        stmt.setString(4, sianAllevamentiVO.getAziendaCodice()); // CODICE_AZIENDA
        stmt.setBigDecimal(5, sianAllevamentiVO.getAllevId()); // ALLEVAMENTO_ID_BDN
        stmt.setString(6, sianAllevamentiVO.getDenominazione()); // DENOMINAZIONE
        stmt.setString(7, sianAllevamentiVO.getIndirizzo()); // INDIRIZZO
        stmt.setString(8, sianAllevamentiVO.getCap()); // CAP
        stmt.setString(9, sianAllevamentiVO.getLocalita()); // LOCALITA
        stmt.setString(10, sianAllevamentiVO.getComune()); // COMUNE
        stmt.setString(11, sianAllevamentiVO.getTipoProduzione()); // TIPO_PRODUZIONE
        stmt.setString(12, sianAllevamentiVO.getAutorizzazioneLatte()); // AUTORIZZAZIONE_LATTE

        if (Validator.isNotEmpty(sianAllevamentiVO.getDtInizioAttivita()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO
                  .getDtInizioAttivita());

          if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
            stmt.setDate(13, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_INIZIO_ATTIVITA
          else
            stmt.setDate(13, null); // DATA_INIZIO_ATTIVITA
        }
        else
          stmt.setDate(13, null); // DATA_INIZIO_ATTIVITA

        if (Validator.isNotEmpty(sianAllevamentiVO.getDtFineAttivita()))
        {

          String dataTemp = StringUtils
              .parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy",
                  sianAllevamentiVO.getDtFineAttivita());
          if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
            stmt.setDate(14, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE_ATTIVITA
          else
            stmt.setDate(14, null); // DATA_FINE_ATTIVITA
        }
        else
          stmt.setDate(14, null); // DATA_FINE_ATTIVITA

        stmt.setString(15, sianAllevamentiVO.getCodFiscaleProp()); // CODICE_FISCALE_PROPRIETARIO
        stmt.setString(16, sianAllevamentiVO.getDenomProprietario()); // DENOMINAZIONE_PROPRIETARIO
        stmt.setString(17, sianAllevamentiVO.getCodFiscaleDeten()); // CODICE_FISCALE_DETENTORE
        stmt.setString(18, sianAllevamentiVO.getDenomDetentore()); // DENOMINAZIONE_DETENTORE

        if (Validator.isNotEmpty(sianAllevamentiVO.getDtInizioDetentore()))
        {

          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO
                  .getDtInizioDetentore());
          if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
            stmt.setDate(19, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_INIZIO_DETENTORE
          else
            stmt.setDate(19, null); // DATA_INIZIO_DETENTORE
        }
        else
          stmt.setDate(19, null); // DATA_INIZIO_DETENTORE

        if (Validator.isNotEmpty(sianAllevamentiVO.getDtFineDetentore()))
        {

          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO
                  .getDtFineDetentore());
          if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
            stmt.setDate(20, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_FINE_DETENTORE
          else
            stmt.setDate(20, null); // DATA_FINE_DETENTORE
        }
        else
          stmt.setDate(20, null); // DATA_FINE_DETENTORE

        stmt.setString(21, sianAllevamentiVO.getSoccida()); // SOCCIDA
        stmt.setBigDecimal(22, sianAllevamentiVO.getLatitudine()); // LATITUDINE
        stmt.setBigDecimal(23, sianAllevamentiVO.getLongitudine()); // LONGITUDINE
        stmt.setString(24, sianAllevamentiVO.getFoglioCatastale()); // FOGLIO
        stmt.setString(25, sianAllevamentiVO.getSezione()); // SEZIONE
        stmt.setString(26, sianAllevamentiVO.getParticella()); // PARTICELLA
        stmt.setString(27, sianAllevamentiVO.getSubalterno()); // SUBALTERNO
        stmt.setBigDecimal(28, sianAllevamentiVO.getCapiTotali()); // CAPI_TOTALI

        if (Validator.isNotEmpty(sianAllevamentiVO.getDataCalcoloCapi()))
        {
          String dataTemp = StringUtils.parseDateFieldToEuropeStandard(
              "yyyy-MM-dd", "dd/MM/yyyy", sianAllevamentiVO
                  .getDataCalcoloCapi());
          if (!SolmrConstants.SIAN_DATA_NULL.equals(dataTemp))
            stmt.setDate(29, new Date(DateUtils.parseDate(dataTemp).getTime())); // DATA_CALCOLO_CAPI
          else
            stmt.setDate(29, null); // DATA_CALCOLO_CAPI
        }
        else
          stmt.setDate(29, null); // DATA_CALCOLO_CAPI
        stmt.setString(30, flagPresenzaSian); // FLAG_PRESENZA_SIAN
        stmt.setString(31, null); // MESSAGGIO_ERRORE
        stmt.setString(32, sianAllevamentiVO.getOrientamentoProduttivo()); // TIPO_ORIENTAMENTO
      }
      else
      {
        stmt.setLong(1, primaryKey.longValue()); // ID_ALLEVAMENTI_SIAN
        stmt.setString(2, cuaa.toUpperCase()); // CUAA
        stmt.setString(3, null);
        stmt.setString(4, null);
        stmt.setString(5, null);
        stmt.setString(6, null);
        stmt.setString(7, null);
        stmt.setString(8, null);
        stmt.setString(9, null);
        stmt.setString(10, null);
        stmt.setString(11, null);
        stmt.setString(12, null);
        stmt.setString(13, null);
        stmt.setString(14, null);
        stmt.setString(15, null);
        stmt.setString(16, null);
        stmt.setString(17, null);
        stmt.setString(18, null);
        stmt.setString(19, null);
        stmt.setString(20, null);
        stmt.setString(21, null);
        stmt.setString(22, null);
        stmt.setString(23, null);
        stmt.setString(24, null);
        stmt.setString(25, null);
        stmt.setString(26, null);
        stmt.setString(27, null);
        stmt.setString(28, null);
        stmt.setString(29, null);
        stmt.setString(30, flagPresenzaSian); // FLAG_PRESENZA_SIAN
        stmt.setString(31, messaggioErrore); // MESSAGGIO_ERRORE
        stmt.setString(32, null); // TIPO_ORIENTAMENTO
      }

      SolmrLogger.debug(this, "Executing insertAllevamentiSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertAllevamentiSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertAllevamentiSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertAllevamentiSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertAllevamentiSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertAllevamentiSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per eliminare un record dalla tabella DB_AZIENDA_TRIBUTARIA
  public void deleteParticellaSian(String istat, String sezione,
      BigDecimal foglio, String particella, String subalterno,
      java.util.Date dataRiferimento) throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      String delete = " DELETE DB_PARTICELLA_SIAN " + " WHERE  COMUNE = ? "
          + "    AND FOGLIO = ? " + "    AND PARTICELLA = ? ";
      if (sezione == null)
        delete += "    AND SEZIONE IS NULL ";
      else
        delete += "    AND SEZIONE =  '" + sezione + "' ";

      if (subalterno == null)
        delete += "    AND SUBALTERNO IS NULL ";
      else
        delete += "    AND SUBALTERNO =  '" + subalterno + "' ";

      if (dataRiferimento == null)
        delete += "    AND ANNO_RIFERIMENTO = TO_CHAR(SYSDATE,'YYYY') ";
      else
        delete += "    AND ANNO_RIFERIMENTO = TO_CHAR(?,'YYYY') ";

      stmt = conn.prepareStatement(delete);

      stmt.setString(1, istat);
      stmt.setBigDecimal(2, foglio);
      stmt.setString(3, particella);
      if (dataRiferimento != null)
        stmt.setDate(4, new java.sql.Date(dataRiferimento.getTime()));

      SolmrLogger.debug(this, "Executing deleteParticellaSIAN: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteParticellaSIAN");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteParticellaSIAN: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteParticellaSIAN: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in deleteParticellaSIAN: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteParticellaSIAN: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per inserire un record su DB_PARTICELLA_SIAN
  public Long insertParticellaSian(String istat, String sezione,
      BigDecimal foglio, String particella, String subalterno,
      java.util.Date dataRiferimento, OutputValidazioneGISDati dati,
      String messaggioErrore, String flagPresenzaSian, boolean isPresenteAt,
      BigDecimal codErrore) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PARTICELLA_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PARTICELLA_SIAN "
          + "            (ID_PARTICELLA_SIAN, " + "             COMUNE, "
          + "             SEZIONE, " + "             FOGLIO, "
          + "             PARTICELLA, " + "             SUBALTERNO, "
          + "             ANNO_RIFERIMENTO, "
          + "             DATA_RIFERIMENTO, " + "             ESITO_GIS, "
          + "             DESCRIZIONE_GIS, "
          + "             SUP_CATASTALE_VETTORIALE, "
          + "             TIPO_CATASTO_CENSUARIO, "
          + "             ESITO_CATASTO_CENSUARIO, "
          + "             DESCRIZIONE_CATASTO_CENSUARIO, "
          + "             SUP_CATASTALE_CENSUARIO, "
          + "             ANNO_FOTO_INFOPART, "
          + "             SIGLA_PROV_INFOPART, "
          + "             COD_NAZIONALE_INFOPART, "
          + "             ID_SEZ_C_INFOPART, "
          + "             COMUNE_INFOPART, " + "             NOME_INFOPART, "
          + "             ESITO_CONFRONTO_CAT_CENS, "
          + "             DESCRIZIONE_CONFRONTO_CAT_CENS, "
          + "             X_MIN, " + "             X_MAX, "
          + "             Y_MIN, " + "             Y_MAX, "
          + "             DATA_AGGIORNAMENTO, "
          + "             FLAG_PRESENZA_SIAN, "
          + "             MESSAGGIO_ERRORE, " + "             COD_ERRORE )"
          + " VALUES " + "(?," + // ID_PARTICELLA_SIAN 1
          "?," + // COMUNE 2
          "?," + // SEZIONE 3
          "?," + // FOGLIO 4
          "?," + // PARTICELLA 5
          "?,"; // SUBALTERNO 6
      if (dataRiferimento == null)
      {
        insert += " TO_CHAR(SYSDATE,'YYYY')," + // ANNO_RIFERIMENTO 7
            " SYSDATE,"; // DATA_RIFERIMENTO 8
      }
      else
      {
        insert += " TO_CHAR(?,'YYYY')," + // ANNO_RIFERIMENTO 7
            " ?,"; // DATA_RIFERIMENTO 8
      }
      insert += "?," + // ESITO_GIS 9
          "?," + // DESCRIZIONE_GIS 10
          "?," + // SUP_CATASTALE_VETTORIALE 11
          "?," + // TIPO_CATASTO_CENSUARIO 12
          "?," + // ESITO_CATASTO_CENSUARIO 13
          "?," + // DESCRIZIONE_CATASTO_CENSUARIO 14
          "?," + // SUP_CATASTALE_CENSUARIO 15
          "?," + // ANNO_FOTO_INFOPART 16
          "?," + // SIGLA_PROV_INFOPART 17
          "?," + // COD_NAZIONALE_INFOPART 18
          "?," + // ID_SEZ_C_INFOPART 19
          "?," + // COMUNE_INFOPART 20
          "?," + // NOME_INFOPART 21
          "?," + // ESITO_CONFRONTO_CAT_CENS 22
          "?," + // DESCRIZIONE_CONFRONTO_CAT_CENS 23
          "?," + // X_MIN 24
          "?," + // X_MAX 25
          "?," + // Y_MIN 26
          "?," + // Y_MAX 27
          "SYSDATE," + // DATA_AGGIORNAMENTO
          "?," + // FLAG_PRESENZA_SIAN 28
          "?," + // MESSAGGIO_ERRORE 29
          "?)"; // COD_ERRORE 30

      stmt = conn.prepareStatement(insert);

      int indice = 1;
      if (isPresenteAt)
      {
        stmt.setLong(indice++, primaryKey.longValue()); // ID_PARTICELLA_SIAN

        SolmrLogger.debug(this, "\n\nContenuto istat " + istat);
        if (istat != null)
          SolmrLogger.debug(this, "\n\nLunghezza istat " + istat.length());
        stmt.setString(indice++, istat); // COMUNE

        SolmrLogger.debug(this, "\n\nContenuto sezione " + sezione);
        if (sezione != null)
          SolmrLogger.debug(this, "\n\nLunghezza sezione " + sezione.length());
        stmt.setString(indice++, sezione); // SEZIONE

        SolmrLogger.debug(this, "\n\nContenuto foglio " + foglio);
        if (foglio != null)
          SolmrLogger.debug(this, "\n\nLunghezza foglio " + foglio.longValue());
        stmt.setBigDecimal(indice++, foglio); // FOGLIO

        SolmrLogger.debug(this, "\n\nContenuto particella " + particella);
        if (particella != null)
          SolmrLogger.debug(this, "\n\nLunghezza particella "
              + particella.length());
        stmt.setString(indice++, particella); // PARTICELLA

        SolmrLogger.debug(this, "\n\nContenuto subalterno " + subalterno);
        if (subalterno != null)
          SolmrLogger.debug(this, "\n\nLunghezza subalterno "
              + subalterno.length());
        stmt.setString(indice++, subalterno); // SUBALTERNO
        SolmrLogger.debug(this, "\n\nContenuto dataRiferimento "
            + dataRiferimento);
        if (dataRiferimento != null)
        {
          stmt.setDate(indice++, new java.sql.Date(dataRiferimento.getTime())); // ANNO_RIFERIMENTO
          stmt.setDate(indice++, new java.sql.Date(dataRiferimento.getTime())); // DATA_RIFERIMENTO
        }

        OutputValidazioneGISDatiDatiGIS datiGIS = dati.getDatiGIS();

        SolmrLogger.debug(this, "\n\nContenuto datiGIS " + datiGIS);

        OutputValidazioneGISDatiDatiCatastoCensuario datiCatastoCensuario = dati
            .getDatiCatastoCensuario();

        SolmrLogger.debug(this, "\n\nContenuto datiCatastoCensuario "
            + datiCatastoCensuario);

        OutputValidazioneGISDatiConfrontoCatastoCensuario datiConfrontoCatastoCensuario = dati
            .getConfrontoCatastoCensuario();

        SolmrLogger.debug(this, "\n\nContenuto datiConfrontoCatastoCensuario "
            + datiConfrontoCatastoCensuario);

        OutputValidazioneGISDatiBoundingBox datiBoundingBox = dati
            .getBoundingBox();

        SolmrLogger.debug(this, "\n\nContenuto datiBoundingBox "
            + datiBoundingBox);

        OutputValidazioneGISDatiDatiGISInfoParticella infoParticella = null;

        SolmrLogger.debug(this, "\n\nContenuto infoParticella "
            + infoParticella);

        if (datiGIS != null)
        {
          infoParticella = datiGIS.getInfoParticella();

          SolmrLogger.debug(this, "\n\nContenuto datiGIS.getEsito() "
              + datiGIS.getEsito());
          if (datiGIS.getEsito() != null)
            stmt.setLong(indice++, datiGIS.getEsito().longValue()); // ESITO_GIS
          else
            stmt.setString(indice++, null); // ESITO_GIS

          SolmrLogger.debug(this, "\n\nContenuto datiGIS.getDescrizione() "
              + datiGIS.getDescrizione());
          if (datiGIS.getDescrizione() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiGIS.getDescrizione() "
                + datiGIS.getDescrizione().length());
          stmt.setString(indice++, datiGIS.getDescrizione()); // DESCRIZIONE_GIS

          SolmrLogger.debug(this, "\n\nContenuto datiGIS.getSupeCatVett() "
              + datiGIS.getSupeCatVett());
          if (datiGIS.getSupeCatVett() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiGIS.getSupeCatVett() "
                + datiGIS.getSupeCatVett().doubleValue());
          stmt.setBigDecimal(indice++, datiGIS.getSupeCatVett()); // SUP_CATASTALE_VETTORIALE
        }
        else
        {
          stmt.setString(indice++, null); // ESITO_GIS
          stmt.setString(indice++, null); // DESCRIZIONE_GIS
          stmt.setString(indice++, null); // SUP_CATASTALE_VETTORIALE
        }
        if (datiCatastoCensuario != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto datiCatastoCensuario.getTipoCatasto() "
                  + datiCatastoCensuario.getTipoCatasto());
          if (datiCatastoCensuario.getTipoCatasto() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiCatastoCensuario.getTipoCatasto() "
                    + datiCatastoCensuario.getTipoCatasto().length());
          stmt.setString(indice++, datiCatastoCensuario.getTipoCatasto()); // TIPO_CATASTO_CENSUARIO

          SolmrLogger.debug(this,
              "\n\nContenuto datiCatastoCensuario.getEsito() "
                  + datiCatastoCensuario.getEsito());
          if (datiCatastoCensuario.getEsito() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiCatastoCensuario.getEsito() "
                    + datiCatastoCensuario.getEsito().longValue());

          if (datiCatastoCensuario.getEsito() != null)
            stmt.setLong(indice++, datiCatastoCensuario.getEsito().longValue()); // ESITO_CATASTO_CENSUARIO
          else
            stmt.setString(indice++, null); // ESITO_CATASTO_CENSUARIO

          SolmrLogger.debug(this,
              "\n\nContenuto datiCatastoCensuario.getDescrizione() "
                  + datiCatastoCensuario.getDescrizione());
          if (datiCatastoCensuario.getDescrizione() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiCatastoCensuario.getDescrizione() "
                    + datiCatastoCensuario.getDescrizione().length());
          stmt.setString(indice++, datiCatastoCensuario.getDescrizione()); // DESCRIZIONE_CATASTO_CENSUARIO

          SolmrLogger.debug(this,
              "\n\nContenuto datiCatastoCensuario.getSupeValCat() "
                  + datiCatastoCensuario.getSupeValCat());
          if (datiCatastoCensuario.getSupeValCat() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiCatastoCensuario.getSupeValCat() "
                    + datiCatastoCensuario.getSupeValCat().doubleValue());
          stmt.setBigDecimal(indice++, datiCatastoCensuario.getSupeValCat()); // SUP_CATASTALE_CENSUARIO
        }
        else
        {
          stmt.setString(indice++, null); // TIPO_CATASTO_CENSUARIO
          stmt.setString(indice++, null); // ESITO_CATASTO_CENSUARIO
          stmt.setString(indice++, null); // DESCRIZIONE_CATASTO_CENSUARIO
          stmt.setString(indice++, null); // SUP_CATASTALE_CENSUARIO
        }

        if (infoParticella != null)
        {
          SolmrLogger.debug(this, "\n\nContenuto infoParticella.getAnnoFoto() "
              + infoParticella.getAnnoFoto());
          if (infoParticella.getAnnoFoto() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza infoParticella.getAnnoFoto() "
                    + infoParticella.getAnnoFoto().length());
          stmt.setString(indice++, infoParticella.getAnnoFoto()); // ANNO_FOTO_INFOPART

          SolmrLogger.debug(this,
              "\n\nContenuto infoParticella.getSigla_prov() "
                  + infoParticella.getSigla_prov());
          if (infoParticella.getSigla_prov() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza infoParticella.getSigla_prov() "
                    + infoParticella.getSigla_prov().length());
          stmt.setString(indice++, infoParticella.getSigla_prov()); // SIGLA_PROV_INFOPART

          SolmrLogger.debug(this,
              "\n\nContenuto infoParticella.getCod_nazionale() "
                  + infoParticella.getCod_nazionale());
          if (infoParticella.getCod_nazionale() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza infoParticella.getCod_nazionale() "
                    + infoParticella.getCod_nazionale().length());
          stmt.setString(indice++, infoParticella.getCod_nazionale()); // COD_NAZIONALE_INFOPART

          SolmrLogger.debug(this, "\n\nContenuto infoParticella.getId_sezc() "
              + infoParticella.getId_sezc());
          if (infoParticella.getId_sezc() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza infoParticella.getId_sezc() "
                    + infoParticella.getId_sezc().length());
          stmt.setString(indice++, infoParticella.getId_sezc()); // ID_SEZ_C_INFOPART

          SolmrLogger.debug(this, "\n\nContenuto infoParticella.getIstatc() "
              + infoParticella.getIstatc());
          if (infoParticella.getIstatc() != null)
            SolmrLogger.debug(this, "\n\nLunghezza infoParticella.getIstatc() "
                + infoParticella.getIstatc().length());
          SolmrLogger.debug(this, "\n\nContenuto infoParticella.getIstatp() "
              + infoParticella.getIstatp());
          if (infoParticella.getIstatp() != null)
            SolmrLogger.debug(this, "\n\nLunghezza infoParticella.getIstatp() "
                + infoParticella.getIstatp().length());
          if (infoParticella.getIstatc() != null
              && infoParticella.getIstatp() != null)
            stmt.setString(indice++, infoParticella.getIstatp()
                + infoParticella.getIstatc()); // COMUNE_INFOPART
          else
            stmt.setString(indice++, null); // COMUNE_INFOPART

          SolmrLogger.debug(this, "\n\nContenuto infoParticella.getNome() "
              + infoParticella.getNome());
          if (infoParticella.getNome() != null)
            SolmrLogger.debug(this, "\n\nLunghezza infoParticella.getNome() "
                + infoParticella.getNome().length());
          stmt.setString(indice++, infoParticella.getNome()); // NOME_INFOPART
        }
        else
        {
          stmt.setString(indice++, null); // ANNO_FOTO_INFOPART
          stmt.setString(indice++, null); // SIGLA_PROV_INFOPART
          stmt.setString(indice++, null); // COD_NAZIONALE_INFOPART
          stmt.setString(indice++, null); // ID_SEZ_C_INFOPART
          stmt.setString(indice++, null); // COMUNE_INFOPART
          stmt.setString(indice++, null); // NOME_INFOPART
        }

        if (datiConfrontoCatastoCensuario != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto datiConfrontoCatastoCensuario.getEsito() "
                  + datiConfrontoCatastoCensuario.getEsito());
          if (datiConfrontoCatastoCensuario.getEsito() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiConfrontoCatastoCensuario.getEsito() "
                    + datiConfrontoCatastoCensuario.getEsito().longValue());
          if (datiConfrontoCatastoCensuario.getEsito() != null)
            stmt.setLong(indice++, datiConfrontoCatastoCensuario.getEsito()
                .longValue()); // ESITO_CONFRONTO_CAT_CENS
          else
            stmt.setString(indice++, null); // ESITO_CONFRONTO_CAT_CENS

          SolmrLogger.debug(this,
              "\n\nContenuto datiConfrontoCatastoCensuario.getDescrizione() "
                  + datiConfrontoCatastoCensuario.getDescrizione());
          if (datiConfrontoCatastoCensuario.getDescrizione() != null)
            SolmrLogger.debug(this,
                "\n\nLunghezza datiConfrontoCatastoCensuario.getDescrizione() "
                    + datiConfrontoCatastoCensuario.getDescrizione().length());
          stmt.setString(indice++, datiConfrontoCatastoCensuario
              .getDescrizione()); // DESCRIZIONE_CONFRONTO_CAT_CENS
        }
        else
        {
          stmt.setString(indice++, null); // ESITO_CONFRONTO_CAT_CENS
          stmt.setString(indice++, null); // DESCRIZIONE_CONFRONTO_CAT_CENS
        }

        if (datiBoundingBox != null)
        {
          SolmrLogger.debug(this, "\n\nContenuto datiBoundingBox.getXmin() "
              + datiBoundingBox.getXmin());
          if (datiBoundingBox.getXmin() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiBoundingBox.getXmin() "
                + datiBoundingBox.getXmin().doubleValue());
          stmt.setBigDecimal(indice++, datiBoundingBox.getXmin()); // X_MIN

          SolmrLogger.debug(this, "\n\nContenuto datiBoundingBox.getXmax() "
              + datiBoundingBox.getXmax());
          if (datiBoundingBox.getXmax() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiBoundingBox.getXmax() "
                + datiBoundingBox.getXmax().doubleValue());
          stmt.setBigDecimal(indice++, datiBoundingBox.getXmax()); // X_MAX

          SolmrLogger.debug(this, "\n\nContenuto datiBoundingBox.getYmin() "
              + datiBoundingBox.getYmin());
          if (datiBoundingBox.getYmin() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiBoundingBox.getYmin() "
                + datiBoundingBox.getYmin().doubleValue());
          stmt.setBigDecimal(indice++, datiBoundingBox.getYmin()); // Y_MIN

          SolmrLogger.debug(this, "\n\nContenuto datiBoundingBox.getYmax() "
              + datiBoundingBox.getYmax());
          if (datiBoundingBox.getYmax() != null)
            SolmrLogger.debug(this, "\n\nLunghezza datiBoundingBox.getYmax() "
                + datiBoundingBox.getYmax().doubleValue());
          stmt.setBigDecimal(indice++, datiBoundingBox.getYmax()); // Y_MAX

        }
        else
        {
          stmt.setString(indice++, null); // X_MIN
          stmt.setString(indice++, null); // X_MAX
          stmt.setString(indice++, null); // Y_MIN
          stmt.setString(indice++, null); // Y_MAX
        }
        SolmrLogger.debug(this, "\n\nContenuto flagPresenzaSian "
            + flagPresenzaSian);
        stmt.setString(indice++, flagPresenzaSian); // FLAG_PRESENZA_SIAN

        SolmrLogger.debug(this, "\n\nContenuto messaggioErrore "
            + messaggioErrore);
        stmt.setString(indice++, messaggioErrore); // MESSAGGIO_ERRORE

        if (codErrore != null)
          SolmrLogger.debug(this, "\n\nContenuto codErrore "
              + codErrore.longValue());
        if (codErrore != null)
          stmt.setLong(indice++, codErrore.longValue()); // COD_ERRORE
        else
          stmt.setString(indice++, null); // COD_ERRORE

      }
      else
      {
        stmt.setLong(indice++, primaryKey.longValue()); // ID_PARTICELLA_SIAN
        stmt.setString(indice++, istat); // COMUNE
        stmt.setString(indice++, sezione); // SEZIONE
        stmt.setBigDecimal(indice++, foglio); // FOGLIO
        stmt.setString(indice++, particella); // PARTICELLA
        stmt.setString(indice++, subalterno); // SUBALTERNO
        if (dataRiferimento != null)
        {
          stmt.setDate(indice++, new java.sql.Date(dataRiferimento.getTime())); // ANNO_RIFERIMENTO
          stmt.setDate(indice++, new java.sql.Date(dataRiferimento.getTime())); // DATA_RIFERIMENTO
        }
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        stmt.setString(indice++, null);
        SolmrLogger.debug(this, "\n\nContenuto flagPresenzaSian "
            + flagPresenzaSian);
        stmt.setString(indice++, flagPresenzaSian); // FLAG_PRESENZA_SIAN
        SolmrLogger.debug(this, "\n\nContenuto messaggioErrore "
            + messaggioErrore);
        stmt.setString(indice++, messaggioErrore); // MESSAGGIO_ERRORE
        if (codErrore != null)
          SolmrLogger.debug(this, "\n\nContenuto codErrore "
              + codErrore.longValue());
        if (codErrore != null)
          stmt.setLong(indice++, codErrore.longValue()); // COD_ERRORE
        else
          stmt.setString(indice++, null); // COD_ERRORE

      }

      SolmrLogger.debug(this, "Executing insertParticellaSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertParticellaSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertParticellaSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertParticellaSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in insertParticellaSian: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertParticellaSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per inserire un record su DB_UNITA_ARBOREA_SIAN
  public Long insertUnitArboreaSian(UnitArborea unitArborea,
      Long idParticellaSian) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_UNITA_ARBOREA_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_UNITA_ARBOREA_SIAN "
          + "            (ID_UNITA_ARBOREA_SIAN, "
          + "             ID_PARTICELLA_SIAN, "
          + "             ANNO_IMPIANTO, " + "             AREA, "
          + "             CAMPAGNA, " + "             COD_DEST_PROD, "
          + "             DESC_DEST_PROD, "
          + "             COD_FLAG_IRRIGABILE, "
          + "             DESC_FLAG_IRRIGABILE, "
          + "             COD_FORMA_ALLEVAMENTO, "
          + "             DESC_FORMA_ALLEVAMENTO, "
          + "             NUMERO_CEPPI, " + "             SESTO_SU_FILA, "
          + "             SESTO_TRA_FILA, " + "             COD_TIPO_COLTURA, "
          + "             DESC_TIPO_COLTURA, " + "             COD_TIPO_UNAR, "
          + "             DESC_TIPO_UNAR, " + "             COD_TIPO_VARI, "
          + "             DESC_TIPO_VARI, " + "             COD_TIPO_VIGN, "
          + "             DESC_TIPO_VIGN, " + "             SUPERFICIE_UNAR, "
          + "             COD_VARIETA, " + "             DESC_VARIETA) "
          + " VALUES " + "(?," + // ID_UNITA_ARBOREA_SIAN 1
          "?," + // ID_PARTICELLA_SIAN 2
          "?," + // ANNO_IMPIANTO 3
          "?," + // AREA 4
          "?," + // CAMPAGNA 5
          "?," + // COD_DEST_PROD 6
          "?," + // DESC_DEST_PROD 7
          "?," + // COD_FLAG_IRRIGABILE 8
          "?," + // DESC_FLAG_IRRIGABILE 9
          "?," + // COD_FORMA_ALLEVAMENTO 10
          "?," + // DESC_FORMA_ALLEVAMENTO 11
          "?," + // NUMERO_CEPPI 12
          "?," + // SESTO_SU_FILA 13
          "?," + // SESTO_TRA_FILA 14
          "?," + // COD_TIPO_COLTURA 15
          "?," + // DESC_TIPO_COLTURA 16
          "?," + // COD_TIPO_UNAR 17
          "?," + // DESC_TIPO_UNAR 18
          "?," + // COD_TIPO_VARI 19
          "?," + // DESC_TIPO_VARI 20
          "?," + // COD_TIPO_VIGN 21
          "?," + // DESC_TIPO_VIGN 22
          "?," + // SUPERFICIE_UNAR 23
          "?," + // COD_VARIETA 24
          "?)"; // DESC_VARIETA 25

      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue()); // ID_UNITA_ARBOREA_SIAN
      stmt.setLong(2, idParticellaSian.longValue()); // ID_PARTICELLA_SIAN

      if (unitArborea.getAnnoImpi() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getAnnoImpi() "
            + unitArborea.getAnnoImpi().longValue());
        stmt.setLong(3, unitArborea.getAnnoImpi().longValue()); // ANNO_IMPIANTO
      }
      else
        stmt.setString(3, null); // ANNO_IMPIANTO

      if (unitArborea.getArea() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getArea() "
            + unitArborea.getArea().longValue());
        stmt.setLong(4, unitArborea.getArea().longValue()); // AREA
      }
      else
        stmt.setString(4, null); // AREA

      if (unitArborea.getCampagna() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getCampagna() "
            + unitArborea.getCampagna().longValue());
        stmt.setLong(5, unitArborea.getCampagna().longValue()); // CAMPAGNA
      }
      else
        stmt.setString(5, null); // CAMPAGNA

      if (unitArborea.getDestProd() != null)
      {
        if (unitArborea.getDestProd().getCodDestProd() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getDestProd().getCodDestProd() "
                  + unitArborea.getDestProd().getCodDestProd().longValue());
          stmt.setLong(6, unitArborea.getDestProd().getCodDestProd()
              .longValue()); // COD_DEST_PROD
        }
        else
          stmt.setString(6, null); // COD_DEST_PROD

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getDestProd().getDescDestProd() "
                + unitArborea.getDestProd().getDescDestProd());
        if (unitArborea.getDestProd().getDescDestProd() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getDestProd().getDescDestProd() "
                  + unitArborea.getDestProd().getDescDestProd().length());
        stmt.setString(7, unitArborea.getDestProd().getDescDestProd()); // DESC_DEST_PROD
      }
      else
      {
        stmt.setString(6, null); // COD_DEST_PROD
        stmt.setString(7, null); // DESC_DEST_PROD
      }

      if (unitArborea.getFlagIrri() != null)
      {
        if (unitArborea.getFlagIrri().getCodFlagIrri() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getFlagIrri().getCodFlagIrri() "
                  + unitArborea.getFlagIrri().getCodFlagIrri().longValue());
          stmt.setLong(8, unitArborea.getFlagIrri().getCodFlagIrri()
              .longValue()); // COD_FLAG_IRRIGABILE
        }
        else
          stmt.setString(8, null); // COD_FLAG_IRRIGABILE

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFlagIrri().getDescFlagIrri() "
                + unitArborea.getFlagIrri().getDescFlagIrri());
        if (unitArborea.getFlagIrri().getDescFlagIrri() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getFlagIrri().getDescFlagIrri() "
                  + unitArborea.getFlagIrri().getDescFlagIrri().length());
        stmt.setString(9, unitArborea.getFlagIrri().getDescFlagIrri()); // DESC_FLAG_IRRIGABILE
      }
      else
      {
        stmt.setString(8, null); // COD_FLAG_IRRIGABILE
        stmt.setString(9, null); // DESC_FLAG_IRRIGABILE
      }

      if (unitArborea.getFormaAll() != null)
      {
        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFormaAll().getCodFormaAll() "
                + unitArborea.getFormaAll().getCodFormaAll());
        if (unitArborea.getFormaAll().getCodFormaAll() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza unitArborea.getFormaAll().getCodFormaAll() "
                  + unitArborea.getFormaAll().getCodFormaAll().length());
        stmt.setString(10, unitArborea.getFormaAll().getCodFormaAll()); // COD_FORMA_ALLEVAMENTO

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFormaAll().getDescFormaAll() "
                + unitArborea.getFormaAll().getDescFormaAll());
        if (unitArborea.getFormaAll().getDescFormaAll() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getFormaAll().getDescFormaAll() "
                  + unitArborea.getFormaAll().getDescFormaAll().length());
        stmt.setString(11, unitArborea.getFormaAll().getDescFormaAll()); // DESC_FORMA_ALLEVAMENTO
      }
      else
      {
        stmt.setString(10, null); // COD_FORMA_ALLEVAMENTO
        stmt.setString(11, null); // DESC_FORMA_ALLEVAMENTO
      }

      if (unitArborea.getNumCeppi() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getNumCeppi() "
            + unitArborea.getNumCeppi().longValue());
        stmt.setLong(12, unitArborea.getNumCeppi().longValue()); // NUMERO_CEPPI
      }
      else
        stmt.setString(12, null); // NUMERO_CEPPI

      if (unitArborea.getSestoSuFila() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getSestoSuFila() "
            + unitArborea.getSestoSuFila().longValue());
        stmt.setLong(13, unitArborea.getSestoSuFila().longValue()); // SESTO_SU_FILA
      }
      else
        stmt.setString(13, null); // SESTO_SU_FILA

      if (unitArborea.getSestoTraFila() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getSestoTraFila() "
            + unitArborea.getSestoTraFila().longValue());
        stmt.setLong(14, unitArborea.getSestoTraFila().longValue()); // SESTO_TRA_FILA
      }
      else
        stmt.setString(14, null); // SESTO_TRA_FILA

      if (unitArborea.getTipoColt() != null)
      {
        if (unitArborea.getTipoColt().getCodTipoColt() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoColt().getCodTipoColt() "
                  + unitArborea.getTipoColt().getCodTipoColt().longValue());
          stmt.setLong(15, unitArborea.getTipoColt().getCodTipoColt()
              .longValue()); // COD_TIPO_COLTURA
        }
        else
          stmt.setString(15, null); // COD_TIPO_COLTURA

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoColt().getDescTipoColt() "
                + unitArborea.getTipoColt().getDescTipoColt());
        if (unitArborea.getTipoColt().getDescTipoColt() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoColt().getDescTipoColt() "
                  + unitArborea.getTipoColt().getDescTipoColt().length());
        stmt.setString(16, unitArborea.getTipoColt().getDescTipoColt()); // DESC_TIPO_COLTURA
      }
      else
      {
        stmt.setString(15, null); // COD_TIPO_COLTURA
        stmt.setString(16, null); // DESC_TIPO_COLTURA
      }

      if (unitArborea.getTipoUnar() != null)
      {
        if (unitArborea.getTipoUnar().getCodTipoUnar() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoUnar().getCodTipoUnar() "
                  + unitArborea.getTipoUnar().getCodTipoUnar().longValue());
          stmt.setLong(17, unitArborea.getTipoUnar().getCodTipoUnar()
              .longValue()); // COD_TIPO_UNAR
        }
        else
          stmt.setString(17, null); // COD_TIPO_UNAR

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoUnar().getDescTipoUnar() "
                + unitArborea.getTipoUnar().getDescTipoUnar());
        if (unitArborea.getTipoUnar().getDescTipoUnar() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoUnar().getDescTipoUnar() "
                  + unitArborea.getTipoUnar().getDescTipoUnar().length());
        stmt.setString(18, unitArborea.getTipoUnar().getDescTipoUnar()); // DESC_TIPO_UNAR
      }
      else
      {
        stmt.setString(17, null); // COD_TIPO_UNAR
        stmt.setString(18, null); // DESC_TIPO_UNAR
      }

      if (unitArborea.getTipoVari() != null)
      {
        if (unitArborea.getTipoVari().getCodTipoVari() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoVari().getCodTipoVari() "
                  + unitArborea.getTipoVari().getCodTipoVari().longValue());
          stmt.setLong(19, unitArborea.getTipoVari().getCodTipoVari()
              .longValue()); // COD_TIPO_VARI
        }
        else
          stmt.setString(19, null); // COD_TIPO_VARI

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoVari().getDescTipoVari() "
                + unitArborea.getTipoVari().getDescTipoVari());
        if (unitArborea.getTipoVari().getDescTipoVari() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoVari().getDescTipoVari() "
                  + unitArborea.getTipoVari().getDescTipoVari().length());
        stmt.setString(20, unitArborea.getTipoVari().getDescTipoVari()); // DESC_TIPO_VARI
      }
      else
      {
        stmt.setString(19, null); // COD_TIPO_VARI
        stmt.setString(20, null); // DESC_TIPO_VARI
      }

      if (unitArborea.getTipoVign() != null)
      {
        if (unitArborea.getTipoVign().getCodTipoVign() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoVign().getCodTipoVign() "
                  + unitArborea.getTipoVign().getCodTipoVign().longValue());
          stmt.setLong(21, unitArborea.getTipoVign().getCodTipoVign()
              .longValue()); // COD_TIPO_VIGN
        }
        else
          stmt.setString(21, null); // COD_TIPO_VIGN

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoVign().getDescTipoVign() "
                + unitArborea.getTipoVign().getDescTipoVign());
        if (unitArborea.getTipoVign().getDescTipoVign() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoVign().getDescTipoVign() "
                  + unitArborea.getTipoVign().getDescTipoVign().length());
        stmt.setString(22, unitArborea.getTipoVign().getDescTipoVign()); // DESC_TIPO_VIGN
      }
      else
      {
        stmt.setString(21, null); // COD_TIPO_VIGN
        stmt.setString(22, null); // DESC_TIPO_VIGN
      }

      if (unitArborea.getUnar() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getUnar() "
            + unitArborea.getUnar().longValue());
        stmt.setLong(23, unitArborea.getUnar().longValue()); // SUPERFICIE_UNAR
      }
      else
        stmt.setString(23, null); // SUPERFICIE_UNAR

      if (unitArborea.getVarieta() != null)
      {
        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getVarieta().getCodVarieta() "
                + unitArborea.getVarieta().getCodVarieta());
        if (unitArborea.getVarieta().getCodVarieta() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza unitArborea.getVarieta().getCodVarieta() "
                  + unitArborea.getVarieta().getCodVarieta().length());
        stmt.setString(24, unitArborea.getVarieta().getCodVarieta()); // COD_VARIETA

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getVarieta().getDescVarieta() "
                + unitArborea.getVarieta().getDescVarieta());
        if (unitArborea.getVarieta().getDescVarieta() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getVarieta().getDescVarieta() "
                  + unitArborea.getVarieta().getDescVarieta().length());
        stmt.setString(25, unitArborea.getVarieta().getDescVarieta()); // DESC_VARIETA
      }
      else
      {
        stmt.setString(24, null); // COD_VARIETA
        stmt.setString(25, null); // DESC_VARIETA
      }

      SolmrLogger.debug(this, "Executing insertUnitArboreaSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertUnitArboreaSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertUnitArboreaSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertUnitArboreaSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertUnitArboreaSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertUnitArboreaSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per inserire un record su DB_POLIGONO_UNITA_ARBOREA_SIAN
  public Long insertUnitArboreaPoligoni(UnitArborea unitArborea,
      Long idPoligonoSian) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_POLIGONO_UNITA_ARBOREA_SIA);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_POLIGONO_UNITA_ARBOREA_SIAN "
          + "            (ID_POLIGONO_UNITA_ARBOREA_SIAN, "
          + "             ID_PARTICELLA_POLIGONO_SIAN, "
          + "             ANNO_IMPIANTO, " + "             AREA, "
          + "             CAMPAGNA, " + "             COD_DEST_PROD, "
          + "             DESC_DEST_PROD, "
          + "             COD_FLAG_IRRIGABILE, "
          + "             DESC_FLAG_IRRIGABILE, "
          + "             COD_FORMA_ALLEVAMENTO, "
          + "             DESC_FORMA_ALLEVAMENTO, "
          + "             NUMERO_CEPPI, " + "             SESTO_SU_FILA, "
          + "             SESTO_TRA_FILA, " + "             COD_TIPO_COLTURA, "
          + "             DESC_TIPO_COLTURA, " + "             COD_TIPO_UNAR, "
          + "             DESC_TIPO_UNAR, " + "             COD_TIPO_VARI, "
          + "             DESC_TIPO_VARI, " + "             COD_TIPO_VIGN, "
          + "             DESC_TIPO_VIGN, " + "             SUPERFICIE_UNAR, "
          + "             COD_VARIETA, " + "             DESC_VARIETA) "
          + " VALUES " + "(?," + // ID_POLIGONO_UNITA_ARBOREA_SIAN 1
          "?," + // ID_PARTICELLA_POLIGONO_SIAN 2
          "?," + // ANNO_IMPIANTO 3
          "?," + // AREA 4
          "?," + // CAMPAGNA 5
          "?," + // COD_DEST_PROD 6
          "?," + // DESC_DEST_PROD 7
          "?," + // COD_FLAG_IRRIGABILE 8
          "?," + // DESC_FLAG_IRRIGABILE 9
          "?," + // COD_FORMA_ALLEVAMENTO 10
          "?," + // DESC_FORMA_ALLEVAMENTO 11
          "?," + // NUMERO_CEPPI 12
          "?," + // SESTO_SU_FILA 13
          "?," + // SESTO_TRA_FILA 14
          "?," + // COD_TIPO_COLTURA 15
          "?," + // DESC_TIPO_COLTURA 16
          "?," + // COD_TIPO_UNAR 17
          "?," + // DESC_TIPO_UNAR 18
          "?," + // COD_TIPO_VARI 19
          "?," + // DESC_TIPO_VARI 20
          "?," + // COD_TIPO_VIGN 21
          "?," + // DESC_TIPO_VIGN 22
          "?," + // SUPERFICIE_UNAR 23
          "?," + // COD_VARIETA 24
          "?)"; // DESC_VARIETA 25

      stmt = conn.prepareStatement(insert);

      stmt.setLong(1, primaryKey.longValue()); // ID_POLIGONO_UNITA_ARBOREA_SIAN
      stmt.setLong(2, idPoligonoSian.longValue()); // ID_PARTICELLA_POLIGONO_SIAN

      if (unitArborea.getAnnoImpi() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getAnnoImpi() "
            + unitArborea.getAnnoImpi().longValue());
        stmt.setLong(3, unitArborea.getAnnoImpi().longValue()); // ANNO_IMPIANTO
      }
      else
        stmt.setString(3, null); // ANNO_IMPIANTO

      if (unitArborea.getArea() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getArea() "
            + unitArborea.getArea().longValue());
        stmt.setLong(4, unitArborea.getArea().longValue()); // AREA
      }
      else
        stmt.setString(4, null); // AREA

      if (unitArborea.getCampagna() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getCampagna() "
            + unitArborea.getCampagna().longValue());
        stmt.setLong(5, unitArborea.getCampagna().longValue()); // CAMPAGNA
      }
      else
        stmt.setString(5, null); // CAMPAGNA

      if (unitArborea.getDestProd() != null)
      {
        if (unitArborea.getDestProd().getCodDestProd() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getDestProd().getCodDestProd() "
                  + unitArborea.getDestProd().getCodDestProd().longValue());
          stmt.setLong(6, unitArborea.getDestProd().getCodDestProd()
              .longValue()); // COD_DEST_PROD
        }
        else
          stmt.setString(6, null); // COD_DEST_PROD

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getDestProd().getDescDestProd() "
                + unitArborea.getDestProd().getDescDestProd());
        if (unitArborea.getDestProd().getDescDestProd() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getDestProd().getDescDestProd() "
                  + unitArborea.getDestProd().getDescDestProd().length());
        stmt.setString(7, unitArborea.getDestProd().getDescDestProd()); // DESC_DEST_PROD
      }
      else
      {
        stmt.setString(6, null); // COD_DEST_PROD
        stmt.setString(7, null); // DESC_DEST_PROD
      }

      if (unitArborea.getFlagIrri() != null)
      {
        if (unitArborea.getFlagIrri().getCodFlagIrri() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getFlagIrri().getCodFlagIrri() "
                  + unitArborea.getFlagIrri().getCodFlagIrri().longValue());
          stmt.setLong(8, unitArborea.getFlagIrri().getCodFlagIrri()
              .longValue()); // COD_FLAG_IRRIGABILE
        }
        else
          stmt.setString(8, null); // COD_FLAG_IRRIGABILE

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFlagIrri().getDescFlagIrri() "
                + unitArborea.getFlagIrri().getDescFlagIrri());
        if (unitArborea.getFlagIrri().getDescFlagIrri() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getFlagIrri().getDescFlagIrri() "
                  + unitArborea.getFlagIrri().getDescFlagIrri().length());
        stmt.setString(9, unitArborea.getFlagIrri().getDescFlagIrri()); // DESC_FLAG_IRRIGABILE
      }
      else
      {
        stmt.setString(8, null); // COD_FLAG_IRRIGABILE
        stmt.setString(9, null); // DESC_FLAG_IRRIGABILE
      }

      if (unitArborea.getFormaAll() != null)
      {
        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFormaAll().getCodFormaAll() "
                + unitArborea.getFormaAll().getCodFormaAll());
        if (unitArborea.getFormaAll().getCodFormaAll() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza unitArborea.getFormaAll().getCodFormaAll() "
                  + unitArborea.getFormaAll().getCodFormaAll().length());
        stmt.setString(10, unitArborea.getFormaAll().getCodFormaAll()); // COD_FORMA_ALLEVAMENTO

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getFormaAll().getDescFormaAll() "
                + unitArborea.getFormaAll().getDescFormaAll());
        if (unitArborea.getFormaAll().getDescFormaAll() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getFormaAll().getDescFormaAll() "
                  + unitArborea.getFormaAll().getDescFormaAll().length());
        stmt.setString(11, unitArborea.getFormaAll().getDescFormaAll()); // DESC_FORMA_ALLEVAMENTO
      }
      else
      {
        stmt.setString(10, null); // COD_FORMA_ALLEVAMENTO
        stmt.setString(11, null); // DESC_FORMA_ALLEVAMENTO
      }

      if (unitArborea.getNumCeppi() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getNumCeppi() "
            + unitArborea.getNumCeppi().longValue());
        stmt.setLong(12, unitArborea.getNumCeppi().longValue()); // NUMERO_CEPPI
      }
      else
        stmt.setString(12, null); // NUMERO_CEPPI

      if (unitArborea.getSestoSuFila() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getSestoSuFila() "
            + unitArborea.getSestoSuFila().longValue());
        stmt.setLong(13, unitArborea.getSestoSuFila().longValue()); // SESTO_SU_FILA
      }
      else
        stmt.setString(13, null); // SESTO_SU_FILA

      if (unitArborea.getSestoTraFila() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getSestoTraFila() "
            + unitArborea.getSestoTraFila().longValue());
        stmt.setLong(14, unitArborea.getSestoTraFila().longValue()); // SESTO_TRA_FILA
      }
      else
        stmt.setString(14, null); // SESTO_TRA_FILA

      if (unitArborea.getTipoColt() != null)
      {
        if (unitArborea.getTipoColt().getCodTipoColt() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoColt().getCodTipoColt() "
                  + unitArborea.getTipoColt().getCodTipoColt().longValue());
          stmt.setLong(15, unitArborea.getTipoColt().getCodTipoColt()
              .longValue()); // COD_TIPO_COLTURA
        }
        else
          stmt.setString(15, null); // COD_TIPO_COLTURA

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoColt().getDescTipoColt() "
                + unitArborea.getTipoColt().getDescTipoColt());
        if (unitArborea.getTipoColt().getDescTipoColt() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoColt().getDescTipoColt() "
                  + unitArborea.getTipoColt().getDescTipoColt().length());
        stmt.setString(16, unitArborea.getTipoColt().getDescTipoColt()); // DESC_TIPO_COLTURA
      }
      else
      {
        stmt.setString(15, null); // COD_TIPO_COLTURA
        stmt.setString(16, null); // DESC_TIPO_COLTURA
      }

      if (unitArborea.getTipoUnar() != null)
      {
        if (unitArborea.getTipoUnar().getCodTipoUnar() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoUnar().getCodTipoUnar() "
                  + unitArborea.getTipoUnar().getCodTipoUnar().longValue());
          stmt.setLong(17, unitArborea.getTipoUnar().getCodTipoUnar()
              .longValue()); // COD_TIPO_UNAR
        }
        else
          stmt.setString(17, null); // COD_TIPO_UNAR

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoUnar().getDescTipoUnar() "
                + unitArborea.getTipoUnar().getDescTipoUnar());
        if (unitArborea.getTipoUnar().getDescTipoUnar() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoUnar().getDescTipoUnar() "
                  + unitArborea.getTipoUnar().getDescTipoUnar().length());
        stmt.setString(18, unitArborea.getTipoUnar().getDescTipoUnar()); // DESC_TIPO_UNAR
      }
      else
      {
        stmt.setString(17, null); // COD_TIPO_UNAR
        stmt.setString(18, null); // DESC_TIPO_UNAR
      }

      if (unitArborea.getTipoVari() != null)
      {
        if (unitArborea.getTipoVari().getCodTipoVari() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoVari().getCodTipoVari() "
                  + unitArborea.getTipoVari().getCodTipoVari().longValue());
          stmt.setLong(19, unitArborea.getTipoVari().getCodTipoVari()
              .longValue()); // COD_TIPO_VARI
        }
        else
          stmt.setString(19, null); // COD_TIPO_VARI

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoVari().getDescTipoVari() "
                + unitArborea.getTipoVari().getDescTipoVari());
        if (unitArborea.getTipoVari().getDescTipoVari() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoVari().getDescTipoVari() "
                  + unitArborea.getTipoVari().getDescTipoVari().length());
        stmt.setString(20, unitArborea.getTipoVari().getDescTipoVari()); // DESC_TIPO_VARI
      }
      else
      {
        stmt.setString(19, null); // COD_TIPO_VARI
        stmt.setString(20, null); // DESC_TIPO_VARI
      }

      if (unitArborea.getTipoVign() != null)
      {
        if (unitArborea.getTipoVign().getCodTipoVign() != null)
        {
          SolmrLogger.debug(this,
              "\n\nContenuto unitArborea.getTipoVign().getCodTipoVign() "
                  + unitArborea.getTipoVign().getCodTipoVign().longValue());
          stmt.setLong(21, unitArborea.getTipoVign().getCodTipoVign()
              .longValue()); // COD_TIPO_VIGN
        }
        else
          stmt.setString(21, null); // COD_TIPO_VIGN

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getTipoVign().getDescTipoVign() "
                + unitArborea.getTipoVign().getDescTipoVign());
        if (unitArborea.getTipoVign().getDescTipoVign() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getTipoVign().getDescTipoVign() "
                  + unitArborea.getTipoVign().getDescTipoVign().length());
        stmt.setString(22, unitArborea.getTipoVign().getDescTipoVign()); // DESC_TIPO_VIGN
      }
      else
      {
        stmt.setString(21, null); // COD_TIPO_VIGN
        stmt.setString(22, null); // DESC_TIPO_VIGN
      }

      if (unitArborea.getUnar() != null)
      {
        SolmrLogger.debug(this, "\n\nContenuto unitArborea.getUnar() "
            + unitArborea.getUnar().longValue());
        stmt.setLong(23, unitArborea.getUnar().longValue()); // SUPERFICIE_UNAR
      }
      else
        stmt.setString(23, null); // SUPERFICIE_UNAR

      if (unitArborea.getVarieta() != null)
      {
        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getVarieta().getCodVarieta() "
                + unitArborea.getVarieta().getCodVarieta());
        if (unitArborea.getVarieta().getCodVarieta() != null)
          SolmrLogger.debug(this,
              "\n\nLunghezza unitArborea.getVarieta().getCodVarieta() "
                  + unitArborea.getVarieta().getCodVarieta().length());
        stmt.setString(24, unitArborea.getVarieta().getCodVarieta()); // COD_VARIETA

        SolmrLogger.debug(this,
            "\n\nContenuto unitArborea.getVarieta().getDescVarieta() "
                + unitArborea.getVarieta().getDescVarieta());
        if (unitArborea.getVarieta().getDescVarieta() != null)
          SolmrLogger.debug(this,
              "\n\n Lunghezza unitArborea.getVarieta().getDescVarieta() "
                  + unitArborea.getVarieta().getDescVarieta().length());
        stmt.setString(25, unitArborea.getVarieta().getDescVarieta()); // DESC_VARIETA
      }
      else
      {
        stmt.setString(24, null); // COD_VARIETA
        stmt.setString(25, null); // DESC_VARIETA
      }

      SolmrLogger.debug(this, "Executing insertUnitArboreaPoligoni: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertUnitArboreaPoligoni");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertUnitArboreaPoligoni: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in insertUnitArboreaPoligoni: " + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertUnitArboreaPoligoni: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertUnitArboreaPoligoni: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  // Metodo per inserire un record su DB_PARTICELLA_POLIGONO_SIAN
  public Long insertPoligoniSian(
      OutputValidazioneGISDatiDatiGISUsoSuoloPoligono poligoni,
      Long idParticellaSian) throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_PARTICELLA_POLIGONO_SIAN);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_PARTICELLA_POLIGONO_SIAN "
          + "            (ID_PARTICELLA_POLIGONO_SIAN, "
          + "             ID_PARTICELLA_SIAN, " + "             CODICE_UTIL, "
          + "             CODICE_PRODOTTO, " + "             CODICE_VARIETA, "
          + "             SUP_POLIGONO, " + "             SUP_TARA, "
          + "             DESCRIZIONE_PRODOTTO) " + " VALUES " + "(?," + // ID_PARTICELLA_POLIGONO_SIAN
                                                                          // 1
          "?," + // ID_PARTICELLA_SIAN 2
          "?," + // CODICE_UTIL 3
          "?," + // CODICE_PRODOTTO 4
          "?," + // CODICE_VARIETA 5
          "?," + // SUP_POLIGONO 6
          "?," + // SUP_TARA 7
          "?)"; // DESCRIZIONE_PRODOTTO 8

      stmt = conn.prepareStatement(insert);

      if (poligoni.getCodUtil() != null)
        SolmrLogger.debug(this, "\n\nContenuto poligoni.getCodUtil() "
            + poligoni.getCodUtil().doubleValue());

      if (poligoni.getCodProdotto() != null)
        SolmrLogger.debug(this, "\n\nContenuto poligoni.getCodProdotto() "
            + poligoni.getCodProdotto().doubleValue());
      if (poligoni.getCodVarieta() != null)
        SolmrLogger.debug(this, "\n\nContenuto poligoni.getCodVarieta() "
            + poligoni.getCodVarieta().doubleValue());
      if (poligoni.getSupePoli() != null)
        SolmrLogger.debug(this, "\n\nContenuto poligoni.getSupePoli() "
            + poligoni.getSupePoli().doubleValue());
      if (poligoni.getSupeTara() != null)
        SolmrLogger.debug(this, "\n\nContenuto poligoni.getSupeTara() "
            + poligoni.getSupeTara().doubleValue());
      SolmrLogger.debug(this, "\n\nContenuto poligoni.getDescProdotto() "
          + poligoni.getDescProdotto());
      if (poligoni.getDescProdotto() != null)
        SolmrLogger.debug(this, "\n\n Lunghezza poligoni.getDescProdotto() "
            + poligoni.getDescProdotto().length());

      stmt.setLong(1, primaryKey.longValue()); // ID_PARTICELLA_POLIGONO_SIAN
      stmt.setLong(2, idParticellaSian.longValue()); // ID_AZIENDA_TRIBUTARIA
      stmt.setBigDecimal(3, poligoni.getCodUtil()); // CODICE_UTIL
      stmt.setBigDecimal(4, poligoni.getCodProdotto()); // CODICE_PRODOTTO
      stmt.setBigDecimal(5, poligoni.getCodVarieta()); // CODICE_VARIETA
      stmt.setBigDecimal(6, poligoni.getSupePoli()); // SUP_POLIGONO
      stmt.setBigDecimal(7, poligoni.getSupeTara()); // SUP_TARA
      stmt.setString(8, poligoni.getDescProdotto()); // DESCRIZIONE_PRODOTTO

      SolmrLogger.debug(this, "Executing insertPoligoniSian: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertPoligoniSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertPoligoniSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertPoligoniSian: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "SQLException while closing Statement and Connection in insertPoligoniSian: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertPoligoniSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return primaryKey;
  }

  /**
   * Recupero i dati del gestore del fascicolo dal nostro DB
   * 
   * @param idAzienda
   *          Long
   * @it.csi.sian.oprFascicolo.ISWSRespAnagFascicolo
   * @throws DataAccessException
   */
  public RespAnagFascicoloVO getRespAnagFascicolo(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    RespAnagFascicoloVO result = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT STO.DESCRIZIONE,"
          + " TO_CHAR(A.DATA_APERTURA_FASCICOLO,'DD/MM/YYYY') AS DATA_APERTURA_FASCICOLO,"
          + " TO_CHAR(A.DATA_CHIUSURA_FASCICOLO,'DD/MM/YYYY') AS DATA_CHIUSURA_FASCICOLO "
          + "FROM DB_AZIENDA A,DB_SIAN_TIPO_OPR STO " + "WHERE ID_AZIENDA = ? "
          + "  AND A.ID_OPR=STO.ID_OPR(+)";

      SolmrLogger.debug(this, "Executing query: " + query);
      SolmrLogger.debug(this, "Executing idAzienda: " + idAzienda);

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        result = new RespAnagFascicoloVO();
        result.setOrganismoPagatore(rs.getString("DESCRIZIONE"));
        result
            .setDataAperturaFascicolo(rs.getString("DATA_APERTURA_FASCICOLO"));
        result
            .setDataChiusuraFascicolo(rs.getString("DATA_CHIUSURA_FASCICOLO"));
      }
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query: " + query);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getRespAnagFascicolo - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "getRespAnagFascicolo - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getRespAnagFascicolo - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getRespAnagFascicolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Recupero l'idOpr usato da noi a fronte del codice usto sal SIAN
   * 
   * @param codiceSianOpr
   *          String codice sian
   * @throws DataAccessException
   *           codice nostro
   * @return Long
   */
  public Long getIdOprFormSianCode(String codiceSianOpr)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Long idOpr = null;

    try
    {
      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT ID_OPR " + "FROM DB_SIAN_TIPO_OPR " + "WHERE CODICE=? ";

      SolmrLogger.debug(this, "getIdOprFormSianCode executing query: " + query);
      SolmrLogger.debug(this, "Executing codiceSianOpr: " + codiceSianOpr);

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codiceSianOpr);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        try
        {
          idOpr = new Long(rs.getString("ID_OPR"));
        }
        catch (Exception e)
        {
        }
      }
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getIdOprFormSianCode Executed query");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getIdOprFormSianCode - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "getIdOprFormSianCode - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getIdOprFormSianCode - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getIdOprFormSianCode - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idOpr;
  }

  /**
   * Metodo che mi restituisce l'elenco dei TIPI_OPR decodificati da SIAN
   * 
   * @param principale
   * @param orderBy []
   * @return it.csi.solmr.dto.DoubleStringcodeDescription[]
   * @throws DataAccessException
   */
  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws DataAccessException
  {
    SolmrLogger.debug(this, "Invocating getListSianTipoOpr method in SianDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<DoubleStringcodeDescription> elencoSianTipiOPR = new Vector<DoubleStringcodeDescription>();

    try
    {

      SolmrLogger.debug(this,
          "Creating db-connection in getListSianTipoOpr method in SianDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this,
          "Created db-connection in getListSianTipoOpr method in SianDAO and it values: "
              + conn + "\n");

      String query = " SELECT ID_OPR, " + "        CODICE, "
          + "        DESCRIZIONE, " + "        FLAG_PRINCIPALE "
          + " FROM   DB_SIAN_TIPO_OPR ";
      if (principale)
      {
        query += " WHERE FLAG_PRINCIPALE = ? ";
      }

      String ordinamento = "";
      if (orderBy != null && orderBy.length > 0)
      {
        String criterio = "";
        for (int i = 0; i < orderBy.length; i++)
        {
          if (i == 0)
          {
            criterio = (String) orderBy[i];
          }
          else
          {
            criterio += ", " + (String) orderBy[i];
          }
        }
        ordinamento = "ORDER BY " + criterio;
      }
      if (!ordinamento.equals(""))
      {
        query += ordinamento;
      }
      stmt = conn.prepareStatement(query);
      if (principale)
      {
        SolmrLogger.debug(this,
            "Value of parameter 1 [FLAG] in method getListSianTipoOpr in SianDAO: "
                + SolmrConstants.FLAG_S + "\n");
        stmt.setString(1, SolmrConstants.FLAG_S);
      }
      SolmrLogger.debug(this, "Executing getListSianTipoOpr: " + query + "\n");
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        DoubleStringcodeDescription doubleStringcodeDescription = new DoubleStringcodeDescription();
        doubleStringcodeDescription.setFirstCode(rs.getString("ID_OPR"));
        doubleStringcodeDescription.setSecondCode(rs.getString("CODICE"));
        doubleStringcodeDescription.setFirstDescription(rs
            .getString("DESCRIZIONE"));
        doubleStringcodeDescription.setSecondDescription(rs
            .getString("FLAG_PRINCIPALE"));
        elencoSianTipiOPR.add(doubleStringcodeDescription);
      }

      rs.close();
      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.error(this, "getListSianTipoOpr - SQLException: "
          + exc.getMessage() + "\n");
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.error(this, "getListSianTipoOpr - Generic Exception: " + ex
          + "\n");
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (conn != null)
        {
          conn.close();
        }
      }
      catch (SQLException exc)
      {
        SolmrLogger.error(this,
            "getListSianTipoOpr - SQLException while closing Statement and Connection: "
                + exc.getMessage() + "\n");
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .error(
                this,
                "getListSianTipoOpr - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage() + "\n");
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated getListSianTipoOpr method in SianDAO\n");
    if (elencoSianTipiOPR.size() == 0)
    {
      return (DoubleStringcodeDescription[]) elencoSianTipiOPR
          .toArray(new DoubleStringcodeDescription[0]);
    }
    else
    {
      return (DoubleStringcodeDescription[]) elencoSianTipiOPR
          .toArray(new DoubleStringcodeDescription[elencoSianTipiOPR.size()]);
    }
  }

  // Metodo per controllare se c'è un record su DB_AZIENDA_TRIBUTARIA
  // per un determinato CUAA inserito da un numero di giorni non superiori
  // a quelli indicati in DB_PARAMETRO per codice=GGAT
  public boolean selectAziendaTributaria(String cuaa)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT A.* "
          + "FROM DB_AZIENDA_TRIBUTARIA A,DB_PARAMETRO P " + "WHERE CUAA = ? "
          + "AND A.FLAG_PRESENTE_AT='" + SolmrConstants.FLAG_S + "' "
          + "AND (TRUNC(SYSDATE)-TRUNC(A.DATA_AGGIORNAMENTO))< P.VALORE "
          + "AND P.ID_PARAMETRO='" + SolmrConstants.PARAMETRO_GGAT + "' ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "\n\ncuaa " + cuaa);
      
      if (cuaa!=null) cuaa=cuaa.toUpperCase().trim();

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing selectAziendaTributaria: " + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      SolmrLogger.debug(this, "selectAziendaTributaria: result: " + result);

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in selectAziendaTributaria: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Metodo per controllare se c'è un record su DB_AZIENDA_TRIBUTARIA
  // per un determinato CUAA valido (FLAG_PRESENTE_AT a S),
  // non mi interessa la data
  public boolean selectAziendaTributariaValido(String cuaa)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT * " + "FROM DB_AZIENDA_TRIBUTARIA  "
          + "WHERE CUAA = ? " + "AND FLAG_PRESENTE_GAA='"
          + SolmrConstants.FLAG_S + "' ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "\n\ncuaa " + cuaa);
      
      if (cuaa!=null) cuaa=cuaa.toUpperCase().trim();

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing selectAziendaTributariaValido: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      SolmrLogger
          .debug(this, "selectAziendaTributariaValido: result: " + result);

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectAziendaTributariaValido: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectAziendaTributariaValido: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectAziendaTributariaValido: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectAziendaTributariaValido: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  /**
   * Metodo per leggere i dati da DB_AZIENDA_TRIBUTARIA ed inserirli all'interno
   * di un oggetto di tipo ISATDett: simuliamo una chiamata al sian
   * 
   * @param cuaa
   *          String: cuaa di cui cerchiamo i dati
   * @throws DataAccessException
   * @return ISATDett
   */
  public SianAnagTributariaGaaVO selectDatiAziendaTributaria(String cuaa)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    SianAnagTributariaGaaVO sianAnagTributariaVO = null;

    try
    {
      conn = getDatasource().getConnection();

      String select = 
          "SELECT AT.ID_AZIENDA_TRIBUTARIA, " +
          "       AT.DENOMINAZIONE, " +
          "       AT.COGNOME, " +
          "       AT.NOME, " +
          "       TO_CHAR(AT.DATA_NASCITA,'DD/MM/YYYY') AS DATA_NASCITA, " +
          "       TO_CHAR(AT.DATA_DECESSO,'DD/MM/YYYY') AS DATA_DECESSO, " +
          "       AT.COMUNE_NASCITA, " +
          "       AT.SESSO, " +
          "       AT.INDIRIZZO, " +
          "       AT.COMUNE, " +
          "       AT.PROVINCIA, " +
          "       AT.PARTITA_IVA, " +
          "       AT.FORMA_GIURIDICA, " +
          "       AT.CUAA_ANAGRAFE_TRIBUTARIA, " +
          "       AT.PROVINCIA_NASCITA, " +
          "       AT.COMUNE_SEDE_LEGALE, " +
          "       AT.PROVINCIA_SEDE_LEGALE, " +
          "       AT.INDIRIZZO_SEDE_LEGALE, " +
          "       AT.CAP_SEDE_LEGALE, " +
          "       AT.COMUNE_DOMICILIO_FISCALE, " +
          "       AT.PROVINCIA_DOMICILIO_FISCALE, " +
          "       AT.INDIRIZZO_DOMICILIO_FISCALE, " +
          "       AT.CAP_DOMICILIO_FISCALE, " +
          "       AT.CAP, " +
          "       TO_CHAR(AT.DATA_INIZIO_ATTIVITA,'DD/MM/YYYY') AS DATA_INIZIO_ATTIVITA, " +
          "       AT.STATO_ATTIVITA, " +
          "       AT.ID_ATTIVITA_ATECO, " +
          "       TAA.CODICE AS COD_ATECO, " +
          "       TAA.DESCRIZIONE AS DESC_ATECO, " +
          "       AT.DESCRIZIONE_STATO_ATTIVITA, " +
          "       AT.TIPO_ATTIVITA, " +
          "       AT.TIPO_CARICA, " +
          "       AT.DESCRIZIONE_TIPO_CARICA, " +
          "       AT.CODICE_FISCALE_RAPPRESENTANTE, " +
          "       AT.DENOMINAZIONE_RAPPRESENTANTE, " +
          "       TO_CHAR(AT.DATA_DECESSO_RAPPRESENTANTE,'DD/MM/YYYY') AS DATA_DECESSO_RAPPRESENTANTE, " +
          "       AT.COGNOME_RAPPRESENTANTE, " +
          "       AT.NOME_RAPPRESENTANTE,  " +
          "       AT.MESSAGGIO_ERRORE, " +
          "       AT.FLAG_PRESENTE_AT " +
          "FROM   DB_AZIENDA_TRIBUTARIA AT," +
          "       DB_TIPO_ATTIVITA_ATECO TAA " +
          "WHERE  AT.CUAA = ? " +
          "AND    AT.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO (+) ";

      stmt = conn.prepareStatement(select);

      SolmrLogger.debug(this, "\n\ncuaa " + cuaa);
      
      if (cuaa!=null) cuaa=cuaa.toUpperCase().trim();

      stmt.setString(1, cuaa);

      SolmrLogger.debug(this, "Executing selectDatiAziendaTributaria: "
          + select);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        sianAnagTributariaVO = new SianAnagTributariaGaaVO();
        sianAnagTributariaVO.setIdAziendaTributaria(rs.getLong("ID_AZIENDA_TRIBUTARIA"));
        sianAnagTributariaVO.setDenominazione(rs.getString("DENOMINAZIONE")); // DENOMINAZIONE
        sianAnagTributariaVO.setCognome(rs.getString("COGNOME")); // COGNOME
        sianAnagTributariaVO.setNome(rs.getString("NOME")); // NOME
        sianAnagTributariaVO.setDataNascita(rs.getString("DATA_NASCITA")); // DATA_NASCITA
        sianAnagTributariaVO.setDataDecesso(rs.getString("DATA_DECESSO")); // DATA_DECESSO
        sianAnagTributariaVO.setComuneNascita(rs.getString("COMUNE_NASCITA")); // COMUNE_NASCITA
        sianAnagTributariaVO.setSesso(rs.getString("SESSO"));// SESSO
        sianAnagTributariaVO.setIndirizzoResidenza(rs.getString("INDIRIZZO")); // INDIRIZZO
        sianAnagTributariaVO.setComuneResidenza(rs.getString("COMUNE")); // COMUNE
        sianAnagTributariaVO.setProvinciaResidenza(rs.getString("PROVINCIA")); // PROVINCIA
        sianAnagTributariaVO.setPartitaIva(rs.getString("PARTITA_IVA")); // PARTITA_IVA
        sianAnagTributariaVO.setNaturaGiuridica(rs.getString("FORMA_GIURIDICA")); // FORMA_GIURIDICA
        sianAnagTributariaVO.setCodiceFiscale(rs.getString("CUAA_ANAGRAFE_TRIBUTARIA")); // CUAA_ANAGRAFE_TRIBUTARIA
        sianAnagTributariaVO.setProvinciaNascita(rs.getString("PROVINCIA_NASCITA")); // PROVINCIA_NASCITA
        sianAnagTributariaVO.setComuneSedeLegale(rs.getString("COMUNE_SEDE_LEGALE")); // COMUNE_SEDE_LEGALE
        sianAnagTributariaVO.setProvinciaSedeLegale(rs.getString("PROVINCIA_SEDE_LEGALE")); // PROVINCIA_SEDE_LEGALE
        sianAnagTributariaVO.setIndirizzoSedeLegale(rs.getString("INDIRIZZO_SEDE_LEGALE")); // INDIRIZZO_SEDE_LEGALE
        sianAnagTributariaVO.setCapSedeLegale(rs.getString("CAP_SEDE_LEGALE")); // CAP_SEDE_LEGALE
        sianAnagTributariaVO.setComuneDomicilioFiscale(rs
            .getString("COMUNE_DOMICILIO_FISCALE")); // COMUNE_DOMICILIO_FISCALE
        sianAnagTributariaVO.setProvinciaDomicilioFiscale(rs
            .getString("PROVINCIA_DOMICILIO_FISCALE")); // PROVINCIA_DOMICILIO_FISCALE
        sianAnagTributariaVO.setIndirizzoDomicilioFiscale(rs
            .getString("INDIRIZZO_DOMICILIO_FISCALE")); // INDIRIZZO_DOMICILIO_FISCALE
        sianAnagTributariaVO.setCapDomicilioFiscale(rs.getString("CAP_DOMICILIO_FISCALE")); // CAP_DOMICILIO_FISCALE
        sianAnagTributariaVO.setCapResidenza(rs.getString("CAP")); // CAP
        sianAnagTributariaVO.setDataInizioAttivita(rs.getString("DATA_INIZIO_ATTIVITA")); // DATA_INIZIO_ATTIVITA
        sianAnagTributariaVO.setStatoAttivita(rs.getString("STATO_ATTIVITA")); // STATO_ATTIVITA
        sianAnagTributariaVO.setStatoAttivitaDesc(rs.getString("DESCRIZIONE_STATO_ATTIVITA")); // DESCRIZIONE_STATO_ATTIVITA
        sianAnagTributariaVO.setTipoAttivita(rs.getString("TIPO_ATTIVITA")); // TIPO_ATTIVITA
        sianAnagTributariaVO.setTipoCarica(rs.getString("TIPO_CARICA")); // TIPO_CARICA
        sianAnagTributariaVO.setTipoCaricaDesc(rs.getString("DESCRIZIONE_TIPO_CARICA")); // DESCRIZIONE_TIPO_CARICA
        sianAnagTributariaVO.setCodiceFiscaleRappresentante(rs
            .getString("CODICE_FISCALE_RAPPRESENTANTE")); // CODICE_FISCALE_RAPPRESENTANTE
        sianAnagTributariaVO.setDenominazioneRappresentante(rs
            .getString("DENOMINAZIONE_RAPPRESENTANTE")); // DENOMINAZIONE_RAPPRESENTANTE
        sianAnagTributariaVO.setDataDecessoRappresentante(rs
            .getString("DATA_DECESSO_RAPPRESENTANTE")); // DENOMINAZIONE_RAPPRESENTANTE
        sianAnagTributariaVO.setCognomeRappresentante(rs
            .getString("COGNOME_RAPPRESENTANTE")); // COGNOME_RAPPRESENTANTE
        sianAnagTributariaVO.setNomeRappresentante(rs.getString("NOME_RAPPRESENTANTE")); // NOME_RAPPRESENTANTE

        sianAnagTributariaVO.setIdAttivitaAteco(checkLongNull(rs.getString("ID_ATTIVITA_ATECO")));
        sianAnagTributariaVO.setCodiceAteco(rs.getString("COD_ATECO"));
        sianAnagTributariaVO.setDescAttivitaAteco(rs.getString("DESC_ATECO"));
        
        sianAnagTributariaVO.setMessaggioErrore(rs.getString("MESSAGGIO_ERRORE"));
        sianAnagTributariaVO.setFlagPresente(rs.getString("FLAG_PRESENTE_AT"));

      }

      stmt.close();

      SolmrLogger.debug(this,
          "Executed selectDatiAziendaTributaria where cuaa= " + cuaa);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectDatiAziendaTributaria: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectDatiAziendaTributaria: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectDatiAziendaTributaria: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectDatiAziendaTributaria: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return sianAnagTributariaVO;
  }
  
  public Vector<SianAnagTributariaAtecoSecGaaVO> selectDatiAziendaTributariaAtecoSec(long idAziendaTributaria)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<SianAnagTributariaAtecoSecGaaVO> vAtecoSecSian = null;
    SianAnagTributariaAtecoSecGaaVO sianAnagTributariaAtecoSecGaaVO = null;
    try
    {
      conn = getDatasource().getConnection();

      String select = 
          "SELECT ATS.ID_ATECO_SEC_TRIBUTARIA, " +
          "       ATS.ID_AZIENDA_TRIBUTARIA, " +
          "       ATS.ID_ATTIVITA_ATECO, " +
          "       TAA.CODICE AS COD_ATECO, " +
          "       TAA.DESCRIZIONE AS DESC_ATECO " +
          "FROM   DB_ATECO_SEC_TRIBUTARIA ATS, " +
          "       DB_TIPO_ATTIVITA_ATECO TAA " +
          "WHERE  ATS.ID_AZIENDA_TRIBUTARIA = ? " +
          "AND    ATS.ID_ATTIVITA_ATECO = TAA.ID_ATTIVITA_ATECO ";

      stmt = conn.prepareStatement(select);

      SolmrLogger.debug(this, "\n\nidAziendaTributaria " + idAziendaTributaria);
      

      stmt.setLong(1, idAziendaTributaria);

      SolmrLogger.debug(this, "Executing selectDatiAziendaTributariaAtecoSec: "
          + select);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        if(vAtecoSecSian == null)
          vAtecoSecSian = new Vector<SianAnagTributariaAtecoSecGaaVO>();
        
        sianAnagTributariaAtecoSecGaaVO = new SianAnagTributariaAtecoSecGaaVO();
        sianAnagTributariaAtecoSecGaaVO.setIdAtecoSecTributaria(rs.getLong("ID_ATECO_SEC_TRIBUTARIA"));
        sianAnagTributariaAtecoSecGaaVO.setIdAziendaTributaria(rs.getLong("ID_AZIENDA_TRIBUTARIA"));
        sianAnagTributariaAtecoSecGaaVO.setIdAttivitaAteco(checkLongNull(rs.getString("ID_ATTIVITA_ATECO")));
        sianAnagTributariaAtecoSecGaaVO.setCodiceAteco(rs.getString("COD_ATECO"));
        sianAnagTributariaAtecoSecGaaVO.setDescAttivitaAteco(rs.getString("DESC_ATECO"));
        
        vAtecoSecSian.add(sianAnagTributariaAtecoSecGaaVO);

      }

      stmt.close();

      SolmrLogger.debug(this,
          "Executed selectDatiAziendaTributariaAtecoSec where idAziendaTributaria= " + idAziendaTributaria);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectDatiAziendaTributariaAtecoSec: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectDatiAziendaTributariaAtecoSec: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectDatiAziendaTributariaAtecoSec: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectDatiAziendaTributariaAtecoSec: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vAtecoSecSian;
  }

  // Metodo per leggere i codici fiscali collegati
  /*public CUAAType[] selectCodiciFiscaliCollegatiSian(Long idAziendaTributaria)
      throws DataAccessException
  {
    CUAAType[] array = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT CODICE_FISCALE_PF,CODICE_FISCALE_PG "
          + "        FROM DB_CF_COLLEGATI_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger.debug(this, "Executing selectCodiciFiscaliCollegatiSian: "
          + search);

      ResultSet rs = stmt.executeQuery();

      Vector<CUAAType> result = new Vector<CUAAType>();

      while (rs.next())
      {
        CUAAType temp = new CUAAType();
        temp.setCodiceFiscalePersonaFisica(rs.getString("CODICE_FISCALE_PF"));
        temp
            .setCodiceFiscalePersonaGiuridica(rs.getString("CODICE_FISCALE_PG"));
        result.add(temp);
      }

      if (result.size() != 0)
      {
        array = (CUAAType[]) result.toArray(new CUAAType[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectCodiciFiscaliCollegatiSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in selectCodiciFiscaliCollegatiSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectCodiciFiscaliCollegatiSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectCodiciFiscaliCollegatiSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectCodiciFiscaliCollegatiSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  // Metodo per leggere i dati delle partite iva attribuite
  /*public ISATPartitaIvaAttribuita[] selectPartiteIvaAttribuiteSian(
      Long idAziendaTributaria) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ISATPartitaIvaAttribuita[] array = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT " + "             PARTITA_IVA, "
          + "             TO_CHAR(DATA_FINE,'DD/MM/YYYY') AS DATA_FINE, "
          + "             DESCRIZIONE_MOD_FINE, "
          + "             DESCRIZIONE_TIPO_FINE_PF, "
          + "             DESCRIZIONE_TIPO_FINE_PNF, "
          + "             MODELLO_FINE, "
          + "             DESCRIZIONE_MODELLO_FINE, "
          + "             PARTITA_IVA_CONFLUENZA, "
          + "             TIPO_FINE, " + "             DESCRIZIONE_TIPO_FINE "
          + " FROM DB_PARTITE_IVA_ATTRIBUITE_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger.debug(this, "Executing selectPartiteIvaAttribuiteSian: "
          + search);

      ResultSet rs = stmt.executeQuery();

      Vector<ISATPartitaIvaAttribuita> result = new Vector<ISATPartitaIvaAttribuita>();

      while (rs.next())
      {
        ISATPartitaIvaAttribuita temp = new ISATPartitaIvaAttribuita();
        temp.setPartitaIva(rs.getString("PARTITA_IVA"));
        temp.setDataFine(rs.getString("DATA_FINE"));
        temp.setDecoModFine(rs.getString("DESCRIZIONE_MOD_FINE"));
        temp.setDecoTipoFinePf(rs.getString("DESCRIZIONE_TIPO_FINE_PF"));
        temp.setDecoTipoFinePnf(rs.getString("DESCRIZIONE_TIPO_FINE_PNF"));
        temp.setModelloFine(rs.getString("MODELLO_FINE"));
        temp.setModelloFineDesc(rs.getString("DESCRIZIONE_MODELLO_FINE"));
        temp.setPartitaIvaConfluenza(rs.getString("PARTITA_IVA_CONFLUENZA"));
        temp.setTipoFine(rs.getString("TIPO_FINE"));
        temp.setTipoFineDesc(rs.getString("DESCRIZIONE_TIPO_FINE"));
        result.add(temp);
      }

      if (result.size() != 0)
      {

        array = (ISATPartitaIvaAttribuita[]) result
            .toArray(new ISATPartitaIvaAttribuita[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectPartiteIvaAttribuiteSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in selectPartiteIvaAttribuiteSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectPartiteIvaAttribuiteSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectPartiteIvaAttribuiteSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectPartiteIvaAttribuiteSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  // Metodo per leggere i dati delle società rappresentate
  /*public ISATSocietaRappresentata[] selectSocietaRappresentataSian(
      Long idAziendaTributaria,boolean orderSocRapp) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ISATSocietaRappresentata[] array = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT TO_CHAR(DATA_INIZIO,'DD/MM/YYYY') AS DATA_INIZIO, "
          + "        TO_CHAR(DATA_FINE,'DD/MM/YYYY') AS DATA_FINE, "
          + "        TIPO_RAPPRESENTANZA, "
          + "        CODICE_FISCALE_RAPPRESENTATO, "
          + "        DECODIFICA_TIPO_RAPPRESENTANZA, "
          + "        DESCRIZIONE_TIPO_RAPPRESENTAZA "
          + " FROM DB_SOCIETA_RAPPRESENTATE_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";
      
      if (orderSocRapp)
        search += " ORDER BY CODICE_FISCALE_RAPPRESENTATO,DB_SOCIETA_RAPPRESENTATE_SIAN.DATA_INIZIO ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger.debug(this, "Executing selectSocietaRappresentataSian: "
          + search);

      ResultSet rs = stmt.executeQuery();

      Vector<ISATSocietaRappresentata> result = new Vector<ISATSocietaRappresentata>();

      while (rs.next())
      {
        ISATSocietaRappresentata temp = new ISATSocietaRappresentata();
        temp.setDataInizio(rs.getString("DATA_INIZIO"));
        temp.setDataFine(rs.getString("DATA_FINE"));
        temp.setTipoRappresentanza(rs.getString("TIPO_RAPPRESENTANZA"));
        temp.setCodiceFiscaleRappresentato(rs
            .getString("CODICE_FISCALE_RAPPRESENTATO"));
        temp.setDecoTipoRapp(rs.getString("DECODIFICA_TIPO_RAPPRESENTANZA"));
        temp.setTipoRappresentanzaDesc(rs
            .getString("DESCRIZIONE_TIPO_RAPPRESENTAZA"));
        result.add(temp);
      }

      if (result.size() != 0)
      {
        array = (ISATSocietaRappresentata[]) result
            .toArray(new ISATSocietaRappresentata[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectSocietaRappresentataSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger
          .fatal(this, "SQLException in selectSocietaRappresentataSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectSocietaRappresentataSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectSocietaRappresentataSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectSocietaRappresentataSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  // Metodo per leggere i dati delle residenze variate
  /*public ISATResidenzaVariata[] selectResidenzaVariataSian(
      Long idAziendaTributaria) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ISATResidenzaVariata[] array = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT TO_CHAR(DATA_INIZIO,'DD/MM/YYYY') AS DATA_INIZIO, "
          + "        TO_CHAR(DATA_FINE,'DD/MM/YYYY') AS DATA_FINE, "
          + "        COMUNE_RESIDENZA, "
          + "        CAP_RESIDENZA, "
          + "        INDIRIZZO_RESIDENZA, "
          + "        PROVINCIA_RESIDENZA "
          + " FROM DB_RESIDENZA_VARIATA_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger
          .debug(this, "Executing selectResidenzaVariataSian: " + search);

      ResultSet rs = stmt.executeQuery();

      Vector<ISATResidenzaVariata> result = new Vector<ISATResidenzaVariata>();

      while (rs.next())
      {
        ISATResidenzaVariata temp = new ISATResidenzaVariata();
        temp.setDataInizio(rs.getString("DATA_INIZIO"));
        temp.setDataFine(rs.getString("DATA_FINE"));
        temp.setComuneResidenza(rs.getString("COMUNE_RESIDENZA"));
        temp.setCapResidenza(rs.getString("CAP_RESIDENZA"));
        temp.setIndirizzoResidenza(rs.getString("INDIRIZZO_RESIDENZA"));
        temp.setProvinciaResidenza(rs.getString("PROVINCIA_RESIDENZA"));
        result.add(temp);
      }

      if (result.size() != 0)
      {
        array = (ISATResidenzaVariata[]) result
            .toArray(new ISATResidenzaVariata[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectResidenzaVariataSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectResidenzaVariataSian: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "Generic Exception in selectResidenzaVariataSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectResidenzaVariataSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectResidenzaVariataSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  // Metodo per leggere i dati dei domicili fiscali variati
  /*public ISATDomicilioFiscaleVariato[] selectDomicilioFiscaleVariatoSian(
      Long idAziendaTributaria) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ISATDomicilioFiscaleVariato[] array = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT TO_CHAR(DATA_FINE,'DD/MM/YYYY') AS DATA_FINE, "
          + "        CAP_DOMICILIO_FISCALE, "
          + "        INDIRIZZO_DOMICILIO_FISCALE, "
          + "        PROVINCIA_DOMICILIO_FISCALE, "
          + "        COMUNE_DOMICILIO_FISCALE "
          + " FROM DB_DOMICILIO_FISC_VARIATO_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger.debug(this, "Executing selectDomicilioFiscaleVariatoSian: "
          + search);

      ResultSet rs = stmt.executeQuery();

      Vector<ISATDomicilioFiscaleVariato> result = new Vector<ISATDomicilioFiscaleVariato>();

      while (rs.next())
      {
        ISATDomicilioFiscaleVariato temp = new ISATDomicilioFiscaleVariato();
        temp.setDataFine(rs.getString("DATA_FINE"));
        temp.setCapDomicilioFiscale(rs.getString("CAP_DOMICILIO_FISCALE"));
        temp.setIndirizzoDomicilioFiscale(rs
            .getString("INDIRIZZO_DOMICILIO_FISCALE"));
        temp.setProvinciaDomicilioFiscale(rs
            .getString("PROVINCIA_DOMICILIO_FISCALE"));
        temp
            .setComuneDomicilioFiscale(rs.getString("COMUNE_DOMICILIO_FISCALE"));
        result.add(temp);

      }

      if (result.size() != 0)
      {
        array = (ISATDomicilioFiscaleVariato[]) result
            .toArray(new ISATDomicilioFiscaleVariato[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectDomicilioFiscaleVariatoSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in selectDomicilioFiscaleVariatoSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectDomicilioFiscaleVariatoSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectDomicilioFiscaleVariatoSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectDomicilioFiscaleVariatoSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  // Metodo per leggere i dati dei rappresentanti delle società
  /*public ISATRappresentanteSocieta[] selectRappresentantiSocietaSian(
      Long idAziendaTributaria,boolean storicoRappLeg) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ISATRappresentanteSocieta[] array = null;

    try
    {
      conn = getDatasource().getConnection();

      String search = " SELECT TO_CHAR(DATA_INIZIO,'DD/MM/YYYY') AS DATA_INIZIO, "
          + "        TO_CHAR(DATA_FINE,'DD/MM/YYYY') AS DATA_FINE, "
          + "        CODICE_FISCALE, "
          + "        TIPO_CARICA, "
          + "        DESCRIZIONE_TIPO_CARICA, "
          + "        DECODIFICA_TIPO_CARICA, "
          + "        DECODIFICA_TIPO_CARICA_PF, "
          + "        DECODIFICA_TIPO_CARICA_PNF "
          + " FROM DB_RAPPRESENTANTI_SOCIETA_SIAN "
          + " WHERE ID_AZIENDA_TRIBUTARIA = ? ";
      
      if (storicoRappLeg)
        search +=" ORDER BY DB_RAPPRESENTANTI_SOCIETA_SIAN.DATA_INIZIO ";

      stmt = conn.prepareStatement(search);

      if (idAziendaTributaria != null)
        SolmrLogger.debug(this, "\n\nContenuto idAziendaTributaria "
            + idAziendaTributaria);

      stmt.setLong(1, idAziendaTributaria.longValue());

      SolmrLogger.debug(this, "Executing selectRappresentantiSocietaSian: "
          + search);

      ResultSet rs = stmt.executeQuery();

      Vector<ISATRappresentanteSocieta> result = new Vector<ISATRappresentanteSocieta>();

      while (rs.next())
      {
        ISATRappresentanteSocieta temp = new ISATRappresentanteSocieta();
        temp.setDataInizio(rs.getString("DATA_INIZIO"));
        temp.setDataFine(rs.getString("DATA_FINE"));
        temp.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
        temp.setTipoCarica(rs.getString("TIPO_CARICA"));
        temp.setTipoCaricaDesc(rs.getString("DESCRIZIONE_TIPO_CARICA"));
        temp.setDecoTipoCari(rs.getString("DECODIFICA_TIPO_CARICA"));
        temp.setDecoTipoCariPf(rs.getString("DECODIFICA_TIPO_CARICA_PF"));
        temp.setDecoTipoCariPnf(rs.getString("DECODIFICA_TIPO_CARICA_PNF"));

        result.add(temp);
      }

      if (result.size() != 0)
      {
        array = (ISATRappresentanteSocieta[]) result
            .toArray(new ISATRappresentanteSocieta[0]);
      }

      stmt.close();

      SolmrLogger.debug(this, "Executed selectRappresentantiSocietaSian");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this,
          "SQLException in selectRappresentantiSocietaSian: "
              + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectRappresentantiSocietaSian: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectRappresentantiSocietaSian: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectRappresentantiSocietaSian: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return array;
  }*/

  /**
   * Recupero i cuaa da aggiornare dalla tabella DB_AZIENDA_TRIBUTARIA_BATCH
   * 
   * @throws DataAccessException
   * @return Vector
   */
  /*public Vector<String> getCuaaForBatch() throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<String> result = new Vector<String>();

    try
    {
      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT CUAA FROM DB_AZIENDA_TRIBUTARIA_BATCH ";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
        result.add(rs.getString("CUAA"));
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query: " + query);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getRespAnagFascicolo - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger
          .fatal(this, "getRespAnagFascicolo - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this,
            "getRespAnagFascicolo - SQLException while closing Statement and Connection: "
                + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getRespAnagFascicolo - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }*/

  /**
   * Recupero l'orgPagatore usato da noi a fronte del codice usato sal SIAN
   * 
   * @param codiceSianOpr
   *          String codice sian cosi formato "CODICE-DESCRIZIONE"
   * @throws DataAccessException
   *           codice nostro
   * @return String
   */
  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    String orgPagatore = "";

    try
    {
      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT CODICE, DESCRIZIONE  " + "FROM DB_SIAN_TIPO_OPR "
          + "WHERE CODICE=? ";

      SolmrLogger.debug(this, "getOrganismoPagatoreFormatted executing query: "
          + query);
      SolmrLogger.debug(this, "Executing codiceSianOpr: " + codiceSianOpr);

      stmt = conn.prepareStatement(query);
      stmt.setString(1, codiceSianOpr);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        try
        {
          orgPagatore = rs.getString("CODICE");
          if (orgPagatore != null)
          {
            orgPagatore += " - " + rs.getString("DESCRIZIONE");
          }
        }
        catch (Exception e)
        {
        }
      }
      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getOrganismoPagatoreFormatted Executed query");
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "getOrganismoPagatoreFormatted - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "getOrganismoPagatoreFormatted - Generic Exception: " + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "getOrganismoPagatoreFormatted - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "getOrganismoPagatoreFormatted - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return orgPagatore;
  }

  /**
   * Devo trovare le u.v. che non trovano una corrispondenza come chiave
   * catastale nell’archivio dell’anagrafe considerando quindi la chiave
   * catastale ritornata dalla CCIAA (Comune, Sz., Fgl., Part.)
   * 
   * @throws DataAccessException
   * @return Vector
   */
  /*public boolean trovaChiaveCatastaleCCIAA(Long idAzienda,
      DatiUvCCIAA datiUvCCIAA) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      // Dato che il comune ed il foglio son campi NOT null sul DB è innutile
      // fare la query. Stessa cosa per la particella con la differenza che è
      // una
      // campo null, ma solo nel caso di particelle provvisorie
      if (datiUvCCIAA.getIstatComune() == null
          || datiUvCCIAA.getFoglio() == null
          || datiUvCCIAA.getParticellaSearch() == null)
        return false;

      long foglioL = -1, particellaL = -1;

      try
      {
        foglioL = Long.parseLong(datiUvCCIAA.getFoglio().trim());
      }
      catch (Exception e)
      {
        // Se non è un numero non riuscirò di sicuro a trovarlo sul nostro DB
        return false;
      }

      try
      {
        particellaL = Long.parseLong(datiUvCCIAA.getParticellaSearch().trim());
      }
      catch (Exception e)
      {
        // Se non è un numero non riuscirò di sicuro a trovarlo sul nostro DB
        return false;
      }

      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT * "
          + "FROM DB_STORICO_PARTICELLA SP,DB_STORICO_UNITA_ARBOREA SUA, "
          + "DB_CONDUZIONE_PARTICELLA CP, DB_UTE U "
          + "WHERE SUA.ID_PARTICELLA=SP.ID_PARTICELLA "
          + "AND SUA.ID_AZIENDA = ? " + "AND SP.DATA_FINE_VALIDITA IS NULL "
          + "AND SUA.DATA_FINE_VALIDITA IS NULL "
          + "AND SUA.ID_TIPOLOGIA_UNAR = ? " + "AND SP.COMUNE = ? "
          + "AND NVL(SP.SEZIONE, '-') = NVL(?,'-') " + "AND SP.FOGLIO = ? "
          + "AND SP.PARTICELLA = ? "
          + "AND CP.ID_PARTICELLA = SUA.ID_PARTICELLA "
          + "AND CP.DATA_FINE_CONDUZIONE IS NULL "
          + "AND U.DATA_FINE_ATTIVITA IS NULL " + "AND U.ID_UTE=CP.ID_UTE "
          + "AND U.ID_AZIENDA=SUA.ID_AZIENDA ";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      int indice = 0;

      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setLong(++indice, SolmrConstants.ID_TIPOLOGIA_UNAR_VINO.longValue());
      stmt.setString(++indice, datiUvCCIAA.getIstatComune());
      stmt.setString(++indice, datiUvCCIAA.getSezione());
      stmt.setLong(++indice, foglioL);
      stmt.setLong(++indice, particellaL);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query: " + query);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "trovaChiaveCatastaleCCIAA - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "trovaChiaveCatastaleCCIAA - Generic Exception: "
          + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "trovaChiaveCatastaleCCIAA - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "trovaChiaveCatastaleCCIAA - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }*/
  
  
  
  
  /**
   * Controlla se sull'unità vitata è presente una conduzione
   * e se sono valorizzati su anangrafe alcuni campi che permettono di
   * importare i dati dell'albo.
   * Se l'anno impianto trova corrispondenza con l'anno vegetativo restituisco solo
   * tali record, altrimenti restituisco tutti.
   * 
   * 
   * 
   */
  public Vector<UnitaArboreaCCIAAVO> confrontaInformazioniCCIAA(Long idAzienda,
      DatiUvCCIAA datiUvCCIAA) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    //utilizzati per cercare la relazione con l'anno vegetativo del CCIAA
    Vector<UnitaArboreaCCIAAVO> result = null;
    try
    {
      // Dato che il comune ed il foglio son campi NOT null sul DB è inutile
      // fare la query. Stessa cosa per la particella con la differenza che è
      // una
      // campo null, ma solo nel caso di particelle provvisorie
      if (datiUvCCIAA.getIstatComune() == null
          || datiUvCCIAA.getFoglio() == null
          || datiUvCCIAA.getParticellaSearch() == null)
        return null;

      long foglioL = -1, particellaL = -1;

      try
      {
        foglioL = Long.parseLong(datiUvCCIAA.getFoglioSearch().trim());
      }
      catch (Exception e)
      {
        // Se non è un numero non riuscirò di sicuro a trovarlo sul nostro DB
        return null;
      }

      try
      {
        particellaL = Long.parseLong(datiUvCCIAA.getParticellaSearch().trim());
      }
      catch (Exception e)
      {
        // Se non è un numero non riuscirò di sicuro a trovarlo sul nostro DB
        return null;
      }

      conn = getDatasource().getConnection();

      String query = null;

      query = "SELECT" +
      		"(SELECT 'TRUE' "+
      		" FROM   DB_CONDUZIONE_PARTICELLA CP, " +
          "        DB_UTE UT " +
          " WHERE  CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          " AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          " AND    CP.ID_UTE = UT.ID_UTE " +
          " AND    UT.ID_AZIENDA = SUA.ID_AZIENDA " +
          " AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          " AND    ROWNUM = 1 " +
          ") AS TROVATA_CONDUZIONE, " +
          "(SELECT 'TRUE' " +
          " FROM   DB_TIPO_VITIGNO_CCIAA TVC2, " +
          "        DB_R_VITIGNO_CCIAA_VARIETA VCV " +
          " WHERE  TVC2.DESCRIZIONE = ? " +
          " AND    TVC2.DATA_FINE_VALIDITA IS NULL " +
          " AND    TVC2.ID_VITIGNO_CCIAA = VCV.ID_VITIGNO_CCIAA " +
          " AND    VCV.DATA_FINE_VALIDITA IS NULL " +
          " AND    VCV.ID_VARIETA = SUA.ID_VARIETA " +
          " AND    ROWNUM = 1 " +
          ") AS PRESENZA_VITIGNO_CCIAA, " +
          "(SELECT TVCS.ID_TIPOLOGIA_VINO " +
          " FROM   DB_TIPO_CCIAA_ALBO_VIGNETO TCAV, " +
          "        DB_TIPO_VINO_CCIAA_SIAP TVCS " +    
          " WHERE TCAV.DATA_FINE_VALIDITA IS NULL " +
          " AND   TCAV.DESCRIZIONE = ? " +
          " AND   TCAV.ID_CCIAA_ALBO_VIGNETO = TVCS.ID_CCIAA_ALBO_VIGNETO " +
          " AND   TVCS.DATA_FINE_VALIDITA IS NULL " +
          " AND    ROWNUM = 1 " +
          ") AS ID_TIPOLOGIA_VINO_CCIAA, " +
          "SUA.ANNO_IMPIANTO, " +
          "SUA.ID_STORICO_UNITA_ARBOREA, " +
          "SUA.AREA, " +
          "TCM.ALTRO_PROCEDIMENTO, " +
          "UA.DATA_CONSOLIDAMENTO_GIS " +
          "FROM   DB_STORICO_PARTICELLA SP, " +
          "       DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_TIPO_CAUSALE_MODIFICA TCM, " +
          "       DB_UNITA_ARBOREA UA " +
          "WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " + 
          "AND    SUA.ID_AZIENDA = ? " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " + 
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = ? " +
          "AND    SP.COMUNE =  ? " +
          "AND    NVL(SP.SEZIONE, '-') = NVL(?,'-') " + 
          "AND    SP.FOGLIO = ? " +
          "AND    SP.PARTICELLA = ? " +
          "AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
          "AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA ";

      SolmrLogger.debug(this, "Executing query: " + query);

      stmt = conn.prepareStatement(query);

      int indice = 0;

      String varietaCCIAA = "";
      if(datiUvCCIAA.getVarieta() != null)
      {
        varietaCCIAA = datiUvCCIAA.getVarieta().trim();
      }
      String descAlboCCIAA = "";
      if(datiUvCCIAA.getDescAlbo() != null)
      {
        descAlboCCIAA = datiUvCCIAA.getDescAlbo().trim();
      }
      stmt.setString(++indice, varietaCCIAA);
      stmt.setString(++indice, descAlboCCIAA);
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setLong(++indice, SolmrConstants.ID_TIPOLOGIA_UNAR_VINO.longValue());
      stmt.setString(++indice, datiUvCCIAA.getIstatComune());
      stmt.setString(++indice, datiUvCCIAA.getSezione());
      stmt.setLong(++indice, foglioL);
      stmt.setLong(++indice, particellaL);

      ResultSet rs = stmt.executeQuery();

      while(rs.next())
      {
        UnitaArboreaCCIAAVO uvCCIAA = new UnitaArboreaCCIAAVO();
        if(rs.getString("TROVATA_CONDUZIONE") != null)
        {
          uvCCIAA.setTrovataConduzione(true);
        }
        
        if(rs.getString("PRESENZA_VITIGNO_CCIAA") != null)
        {
          uvCCIAA.setPresenzaVitignoCCIAA(true);
        }
        
        if(rs.getString("ID_TIPOLOGIA_VINO_CCIAA") != null)
        {
          uvCCIAA.setTrovatoAlbo(true);
        }
        uvCCIAA.setAnnoImpianto(checkLongNull(rs.getString("ANNO_IMPIANTO")));
        uvCCIAA.setIdStoricoUnitaArborea(checkLongNull(rs.getString("ID_STORICO_UNITA_ARBOREA")));
        uvCCIAA.setIdTipologiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO_CCIAA")));
        uvCCIAA.setSupIscritta(rs.getBigDecimal("AREA"));
        if(Validator.isNotEmpty(rs.getTimestamp("DATA_CONSOLIDAMENTO_GIS")))
        {
          uvCCIAA.setConsolidamentoGis(true);
        }
        
        String altroProcedimento = rs.getString("ALTRO_PROCEDIMENTO");
        if("S".equalsIgnoreCase(altroProcedimento))
        {
          uvCCIAA.setModificaVITI(true);
        }
        
        if(result == null)
        {
          result = new Vector<UnitaArboreaCCIAAVO>();
        }
        
        result.add(uvCCIAA);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query: " + query);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "confrontaInformazioniCCIAA - SQLException: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "confrontaInformazioniCCIAA - Generic Exception: "
          + ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "confrontaInformazioniCCIAA - SQLException while closing Statement and Connection: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "confrontaInformazioniCCIAA - Generic Exception while closing Statement and Connection: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
     
    return result;
  }
  
  
  
  //Metodo per inserire un record su DB_CCIAA_ALBO_VIGNETI 
  public Long insertCciaaAlboVigneti(Long idAzienda, String codice, String descrizioneEsito) 
    throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try
    {
      primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DB_CCIAA_ALBO_VIGNETI);
      conn = getDatasource().getConnection();

      String insert = " INSERT INTO DB_CCIAA_ALBO_VIGNETI ( "+   
                      "ID_CCIAA_ALBO_VIGNETI, DATA_SCARICO, ID_AZIENDA, CODICE_ESITO, DESCRIZIONE_ESITO) "+
                      "VALUES ( ?, SYSDATE, ?, ?, ?)";  

      stmt = conn.prepareStatement(insert);


      SolmrLogger.debug(this,"\n\nidAzienda "+ idAzienda);
      SolmrLogger.debug(this,"\n\ncodice "+ codice);
      SolmrLogger.debug(this,"\n\ndescrizioneEsito "+ descrizioneEsito);

      stmt.setLong(1, primaryKey.longValue()); //ID_CCIAA_ALBO_VIGNETI
      stmt.setLong(2, idAzienda.longValue()); // ID_AZIENDA
      stmt.setString(3, codice); // CODICE_ESITO
      stmt.setString(4, descrizioneEsito); // DESCRIZIONE_ESITO

      SolmrLogger.debug(this, "Executing insertCciaaAlboVigneti: " + insert);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertCciaaAlboVigneti where idAzienda= "+ idAzienda);
      return primaryKey;
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in insertCciaaAlboVigneti: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in insertCciaaAlboVigneti: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in insertCciaaAlboVigneti: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in insertCciaaAlboVigneti: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  /**
   * Metodo per effettuare l'inserimento massivo di DB_CCIAA_ALBO_DETTAGLIO
   *  
   * @throws DataAccessException
   */
  public void insertAllCciaaAlboDettaglio(DatiUv datiUvCCIAA[],Long idCciaaAlboVigneti) 
    throws DataAccessException 
  {
    SolmrLogger.debug(this, "Invocating insertAllCciaaAlboDettaglio method in SianDAO\n");
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try 
    {
      SolmrLogger.debug(this, "Creating db-connection in insertAllCciaaAlboDettaglio method in SianDAO\n");
      conn = getDatasource().getConnection();
      SolmrLogger.debug(this, "Created db-connection in insertAllCciaaAlboDettaglio method in SianDAO and it values: "+conn+"\n");
      
      String query = " INSERT INTO DB_CCIAA_ALBO_DETTAGLIO ( "+
                     " ID_CCIAA_ALBO_DETTAGLIO, ID_CCIAA_ALBO_VIGNETI, DESC_ALBO, "+
                     " MATRICOLA, ISTAT_COMUNE, SEZIONE, "+
                     " FOGLIO, PARTICELLA, SUBALTERNO, "+
                     " SUPERFICIE, ANNO_IMPIANTO, DESC_VARIETA, "+
                     " NUM_CEPPI, CODICE_MIPAF) "+
                     " VALUES (SEQ_DB_CCIAA_ALBO_DETTAGLIO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
      
      stmt = conn.prepareStatement(query);
  
      SolmrLogger.debug(this, "Executing insertAllCciaaAlboDettaglio: "+query);
      
      for(int i = 0; i < datiUvCCIAA.length; i++) 
      {
        Vector<String> vParticella = new Vector<String>();
        if (datiUvCCIAA[i].getPARTICELLA()!=null)
        {
          StringTokenizer strT = new StringTokenizer(StringUtils
              .sostituisceCaratteriNonNumeroConSpazio(datiUvCCIAA[i].getPARTICELLA()));
          while (strT.hasMoreElements())
          {
            vParticella.add((String)strT.nextElement());
          }          
        }
        
        if(vParticella.size() > 1)
        {          
          for(int j=0;j<vParticella.size();j++)
          {
            stmt.setLong(1, idCciaaAlboVigneti.longValue());
            SolmrLogger.debug(this, "Value of parameter [ID_CCIAA_ALBO_VIGNETI] in method insertAllCciaaAlboDettaglio in SianDAO: "+idCciaaAlboVigneti.longValue()+"\n");
            stmt.setString(2, datiUvCCIAA[i].getDESC_ALBO());
            SolmrLogger.debug(this, "Value of parameter [DESC_ALBO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getDESC_ALBO()+"\n");
            stmt.setInt(3, datiUvCCIAA[i].getNR_MATRICOLA());
            SolmrLogger.debug(this, "Value of parameter [MATRICOLA] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getNR_MATRICOLA()+"\n");
            stmt.setString(4, datiUvCCIAA[i].getISTAT_COMUNE());
            SolmrLogger.debug(this, "Value of parameter [ISTAT_COMUNE] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getISTAT_COMUNE()+"\n");
            //Se il campo non riporta un dato significativo impostare NULL Es:Spazio, spazi >>Null
            if (datiUvCCIAA[i].getSEZIONE()!= null 
                && "".equals(datiUvCCIAA[i].getSEZIONE().trim()))
            {
              datiUvCCIAA[i].setSEZIONE(null);
            }
            else
            {
              if(datiUvCCIAA[i].getSEZIONE() != null)
              {
                datiUvCCIAA[i].setSEZIONE(datiUvCCIAA[i].getSEZIONE().trim());
              }
            }
            stmt.setString(5, datiUvCCIAA[i].getSEZIONE());
            SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getSEZIONE()+"\n");
            
            if (datiUvCCIAA[i].getFOGLIO()!=null)
            {
              //Tenere presente che se nel campo foglio fornita dalla CCIAA è presente la lettera “P” 
              //oppure altri caratteri non numerici, questi devono essere omessi e comunque tale campo deve essere numerico.
              String temp= StringUtils.sostituisceCaratteriNonNumeroConSpazio(datiUvCCIAA[i].getFOGLIO());
              temp = temp.trim();
              if ("".equals(temp))
              {
                datiUvCCIAA[i].setFOGLIO(null);
                stmt.setString(6, null);
              }
              else 
              {
                try
                {
                  datiUvCCIAA[i].setFOGLIO(temp);
                  long tmpL = Long.parseLong(temp);
                  stmt.setLong(6, tmpL);
                }
                catch(Exception ex )
                {
                  datiUvCCIAA[i].setFOGLIO(null);
                  stmt.setString(6, null);
                }
              }
            }
            else
            {
              datiUvCCIAA[i].setFOGLIO(null);
              stmt.setString(6, null);
            }
            
            SolmrLogger.debug(this, "Value of parameter [FOGLIO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getFOGLIO()+"\n");
            
            SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method insertAllCciaaAlboDettaglio in SianDAO: "+vParticella.get(j)+"\n");
            stmt.setLong(7, Long.parseLong(vParticella.get(j)));
            
            //Se il campo non riporta un dato significativo impostare NULL Es:Spazio, spazi >>Null
            stmt.setString(8, null);
            SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method insertAllCciaaAlboDettaglio in SianDAO: null\n");
            if(j==0)
            {
              stmt.setDouble(9, (double) datiUvCCIAA[i].getSUPERFICIE_MQ() / 10000);
              SolmrLogger.debug(this, "Value of parameter [SUPERFICIE] in method insertAllCciaaAlboDettaglio in SianDAO: "+(double) datiUvCCIAA[i].getSUPERFICIE_MQ() / 10000+"\n");
            }
            else
            {
              stmt.setDouble(9, 0);
              SolmrLogger.debug(this, "Value of parameter [SUPERFICIE] in method insertAllCciaaAlboDettaglio in SianDAO: "+0+"\n");
            }
            stmt.setInt(10, datiUvCCIAA[i].getANNO_VEGETATIVO());
            SolmrLogger.debug(this, "Value of parameter [ANNO_IMPIANTO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getANNO_VEGETATIVO()+"\n");
            stmt.setString(11, datiUvCCIAA[i].getVARIETA());
            SolmrLogger.debug(this, "Value of parameter [DESC_VARIETA] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getVARIETA()+"\n");
            stmt.setInt(12, datiUvCCIAA[i].getNUM_CEPPI());
            SolmrLogger.debug(this, "Value of parameter [NUM_CEPPI] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getNUM_CEPPI()+"\n");
            stmt.setString(13, datiUvCCIAA[i].getCODICE_MIPAF());
            SolmrLogger.debug(this, "Value of parameter [CODICE_MIPAF] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getCODICE_MIPAF()+"\n");
            stmt.addBatch();
            
          }
          
          
          
        }
        else
        {        
          stmt.setLong(1, idCciaaAlboVigneti.longValue());
          SolmrLogger.debug(this, "Value of parameter [ID_CCIAA_ALBO_VIGNETI] in method insertAllCciaaAlboDettaglio in SianDAO: "+idCciaaAlboVigneti.longValue()+"\n");
          stmt.setString(2, datiUvCCIAA[i].getDESC_ALBO());
          SolmrLogger.debug(this, "Value of parameter [DESC_ALBO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getDESC_ALBO()+"\n");
          stmt.setInt(3, datiUvCCIAA[i].getNR_MATRICOLA());
          SolmrLogger.debug(this, "Value of parameter [MATRICOLA] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getNR_MATRICOLA()+"\n");
          stmt.setString(4, datiUvCCIAA[i].getISTAT_COMUNE());
          SolmrLogger.debug(this, "Value of parameter [ISTAT_COMUNE] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getISTAT_COMUNE()+"\n");
          //Se il campo non riporta un dato significativo impostare NULL Es:Spazio, spazi >>Null
          if (datiUvCCIAA[i].getSEZIONE()!= null 
              && "".equals(datiUvCCIAA[i].getSEZIONE().trim()))
          {
            datiUvCCIAA[i].setSEZIONE(null);
          }
          else
          {
            if(datiUvCCIAA[i].getSEZIONE() != null)
            {
              datiUvCCIAA[i].setSEZIONE(datiUvCCIAA[i].getSEZIONE().trim());
            }
          }
          stmt.setString(5, datiUvCCIAA[i].getSEZIONE());
          SolmrLogger.debug(this, "Value of parameter [SEZIONE] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getSEZIONE()+"\n");
          
          if (datiUvCCIAA[i].getFOGLIO()!=null)
          {
            //Tenere presente che se nel campo foglio fornita dalla CCIAA è presente la lettera “P” 
            //oppure altri caratteri non numerici, questi devono essere omessi e comunque tale campo deve essere numerico.
            String temp= StringUtils.sostituisceCaratteriNonNumeroConSpazio(datiUvCCIAA[i].getFOGLIO());
            temp = temp.trim();
            if ("".equals(temp))
            {
              datiUvCCIAA[i].setFOGLIO(null);
              stmt.setString(6, null);
            }
            else 
            {
              try
              {
                datiUvCCIAA[i].setFOGLIO(temp);
                long tmpL = Long.parseLong(temp);
                stmt.setLong(6, tmpL);
              }
              catch(Exception ex )
              {
                datiUvCCIAA[i].setFOGLIO(null);
                stmt.setString(6, null);
              }
            }
          }
          else
          {
            datiUvCCIAA[i].setFOGLIO(null);
            stmt.setString(6, null);
          }
          
          SolmrLogger.debug(this, "Value of parameter [FOGLIO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getFOGLIO()+"\n");
          
          SolmrLogger.debug(this, "Value of parameter [PARTICELLA] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getPARTICELLA()+"\n");
          if (datiUvCCIAA[i].getPARTICELLA()!=null)
          {
            //Tenere presente che se nel campo Particella fornita dalla CCIAA è presente la lettera “P” 
            //oppure altri caratteri non numerici, questi devono essere omessi e comunque tale campo deve essere numerico.
            String temp=StringUtils.sostituisceCaratteriNonNumeroConSpazio(datiUvCCIAA[i].getPARTICELLA());
            temp = temp.trim();
            if ("".equals(temp))
              stmt.setString(7, null);
            else stmt.setLong(7, Long.parseLong(temp));
          }
          else stmt.setString(7, null);
          
          //Se il campo non riporta un dato significativo impostare NULL Es:Spazio, spazi >>Null
          stmt.setString(8, null);
          SolmrLogger.debug(this, "Value of parameter [SUBALTERNO] in method insertAllCciaaAlboDettaglio in SianDAO: null\n");
          stmt.setDouble(9, (double) datiUvCCIAA[i].getSUPERFICIE_MQ() / 10000);
          SolmrLogger.debug(this, "Value of parameter [SUPERFICIE] in method insertAllCciaaAlboDettaglio in SianDAO: "+(double) datiUvCCIAA[i].getSUPERFICIE_MQ() / 10000+"\n");
          stmt.setInt(10, datiUvCCIAA[i].getANNO_VEGETATIVO());
          SolmrLogger.debug(this, "Value of parameter [ANNO_IMPIANTO] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getANNO_VEGETATIVO()+"\n");
          stmt.setString(11, datiUvCCIAA[i].getVARIETA());
          SolmrLogger.debug(this, "Value of parameter [DESC_VARIETA] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getVARIETA()+"\n");
          stmt.setInt(12, datiUvCCIAA[i].getNUM_CEPPI());
          SolmrLogger.debug(this, "Value of parameter [NUM_CEPPI] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getNUM_CEPPI()+"\n");
          stmt.setString(13, datiUvCCIAA[i].getCODICE_MIPAF());
          SolmrLogger.debug(this, "Value of parameter [CODICE_MIPAF] in method insertAllCciaaAlboDettaglio in SianDAO: "+datiUvCCIAA[i].getCODICE_MIPAF()+"\n");
          stmt.addBatch();
        }
      }
  
      stmt.executeBatch();
  
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.error(this, "insertAllCciaaAlboDettaglio in SianDAO - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.error(this, "insertAllCciaaAlboDettaglio in SianDAO - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if(stmt != null) {
          stmt.close();
        }
        if(conn != null) {
          conn.close();
        }
      }
      catch(SQLException exc) {
        SolmrLogger.error(this, "insertAllCciaaAlboDettaglio in SianDAO - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch(Exception ex) {
        SolmrLogger.error(this, "insertAllCciaaAlboDettaglio in SianDAO - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    SolmrLogger.debug(this, "Invocated insertAllCciaaAlboDettaglio method in SianDAO\n");
  }
  
  //Metodo per eliminare i record dalla tabella DB_CCIAA_ALBO_DETTAGLIO e DB_CCIAA_ALBO_VIGNETI
  public void deleteCciaaAlboVigneti(Long idAzienda) 
    throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();

      SolmrLogger.debug(this, "deleteCciaaAlboVigneti idAzienda: " + idAzienda);
      
      //Prima cancello tutti i record figli
      String delete = " DELETE DB_CCIAA_ALBO_DETTAGLIO WHERE ID_CCIAA_ALBO_VIGNETI IN "+
                      " ( "+
                      "  SELECT ID_CCIAA_ALBO_VIGNETI "+
                      "  FROM DB_CCIAA_ALBO_VIGNETI "+
                      "  WHERE ID_AZIENDA = ? "+
                      " ) ";

      stmt = conn.prepareStatement(delete);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing deleteCciaaAlboVigneti: " + delete);

      stmt.executeUpdate();

      stmt.close();
      
      //Poi cancello il padre
      delete = " DELETE DB_CCIAA_ALBO_VIGNETI WHERE ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing deleteCciaaAlboVigneti: " + delete);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed deleteCciaaAlboVigneti with idAzienda = " + idAzienda);
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in deleteCciaaAlboVigneti: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception in deleteCciaaAlboVigneti: "
          + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in deleteCciaaAlboVigneti: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in deleteCciaaAlboVigneti: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  //Metodo per controllare se c'è un record su DB_CCIAA_ALBO_VIGNETI
  // per una determinata azienda con codice_esito=012 oppure 016
  public boolean selectAlboVignetiValido(Long idAzienda)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;

    try
    {
      conn = getDatasource().getConnection();

      String query = "SELECT  ID_CCIAA_ALBO_VIGNETI "+
                     "FROM DB_CCIAA_ALBO_VIGNETI "+
                     "WHERE ID_AZIENDA = ? "+
                     "AND (CODICE_ESITO = ? OR CODICE_ESITO = ? ) ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "\n\nidAzienda " + idAzienda);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setString(2, SolmrConstants.CCIAA_CODICE_SERVIZIO_OK);
      stmt.setString(3, SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD);

      SolmrLogger.debug(this, "Executing selectAlboVignetiValido: "
          + query);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        result = true;

      SolmrLogger
          .debug(this, "selectAlboVignetiValido: result: " + result);

      stmt.close();
    }
    catch (SQLException exc)
    {
      SolmrLogger.fatal(this, "SQLException in selectAlboVignetiValido: "
          + exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this,
          "Generic Exception in selectAlboVignetiValido: "
              + ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger
            .fatal(
                this,
                "SQLException while closing Statement and Connection in selectAlboVignetiValido: "
                    + exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger
            .fatal(
                this,
                "Generic Exception while closing Statement and Connection in selectAlboVignetiValido: "
                    + ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  
  /*private CodeDescription getAttivitaATECObyCodeParametroCATE(String codiceAteco)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    CodeDescription attivitaATECO = null;
    String query = null;
    StringBuffer queryBuf = null;

    try
    {
      SolmrLogger.debug(this,
          "[SianDAO::getAttivitaATECObyCodeParametroCATE] BEGIN.");

      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 

      queryBuf = new StringBuffer();
      queryBuf
          .append("SELECT ID_ATTIVITA_ATECO,DESCRIZIONE FROM DB_TIPO_ATTIVITA_ATECO "
              + " WHERE CODICE=? "
              + " AND ID_TIPO_CODIFICA_ATECO = "
              + "(SELECT VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO like '"
              + SolmrConstants.PARAMETRO_CATE + "')");

      query = queryBuf.toString();
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getAttivitaATECObyCode Executing query: "
          + query);
      SolmrLogger.debug(this, "getAttivitaATECObyCode codiceAteco: "
          + codiceAteco);

      stmt.setString(1, codiceAteco);

      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next())
      {
        attivitaATECO = new CodeDescription();
        attivitaATECO
            .setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
        attivitaATECO.setDescription(resultSet.getString("DESCRIZIONE"));
      }

      return attivitaATECO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile("query", query),
          new Variabile("queryBuf", queryBuf),
          new Variabile("attivitaATECO", attivitaATECO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] { new Parametro("codiceAteco",
          codiceAteco) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[SianDAO::getAttivitaATECObyCodeParametroCATE] ", t, query,
          variabili, parametri);
      
             
       //Rimappo e rilancio l'eccezione come DataAccessException.
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      // Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      // ignora ogni eventuale eccezione)
      
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[SianDAO::getAttivitaATECObyCodeParametroCATE] END.");
    }
  }*/
  
  
  private CodeDescription getAttivitaATECObySian(String codiceAtecoSian)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    CodeDescription attivitaATECO = null;
    String query = null;
    StringBuffer queryBuf = null;
    
    int valoreInizio = codiceAtecoSian.indexOf(".")+1;
    codiceAtecoSian = codiceAtecoSian.substring(valoreInizio);
    
    

    try
    {
      SolmrLogger.debug(this,
          "[SianDAO::getAttivitaATECObySian] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append("SELECT ID_ATTIVITA_ATECO,DESCRIZIONE FROM DB_TIPO_ATTIVITA_ATECO "
              + " WHERE CODICE_ORIGINALE = ? ");

      query = queryBuf.toString();
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getAttivitaATECObySian Executing query: "
          + query);
      SolmrLogger.debug(this, "getAttivitaATECObySian codiceAtecoSian: "
          + codiceAtecoSian);

      stmt.setString(1, codiceAtecoSian);

      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next())
      {
        attivitaATECO = new CodeDescription();
        attivitaATECO
            .setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO")));
        attivitaATECO.setDescription(resultSet.getString("DESCRIZIONE"));
      }

      return attivitaATECO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile("query", query),
          new Variabile("queryBuf", queryBuf),
          new Variabile("attivitaATECO", attivitaATECO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] { new Parametro("codiceAtecoSian",
          codiceAtecoSian) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[SianDAO::getAttivitaATECObySian] ", t, query,
          variabili, parametri);
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
          "[SianDAO::getAttivitaATECObySian] END.");
    }
  }
  
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    StringcodeDescription strDesc = null;
    String query = null;
    StringBuffer queryBuf = null;

    try
    {
      SolmrLogger.debug(this,
          "[SianDAO::getDescTipoIscrizioneInpsByCodice] BEGIN.");

      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */

      queryBuf = new StringBuffer();
      queryBuf
          .append("SELECT ID_TIPO_ISCRIZIONE_INPS, DESCRIZIONE FROM DB_TIPO_ISCRIZIONE_INPS "
              + " WHERE CODICE_TIPO_ISCRIZIONE = ? ");

      query = queryBuf.toString();
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "getDescTipoIscrizioneInpsByCodice Executing query: "
          + query);
      SolmrLogger.debug(this, "getDescTipoIscrizioneInpsByCodice codiceTipoIscrizioneInps: "
          + codiceTipoIscrizioneInps);

      stmt.setString(1, codiceTipoIscrizioneInps);

      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next())
      {
        strDesc = new StringcodeDescription();
        strDesc.setCode(resultSet.getString("ID_TIPO_ISCRIZIONE_INPS"));
        strDesc.setDescription(resultSet.getString("DESCRIZIONE"));
      }

      return strDesc;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[] { new Variabile("query", query),
          new Variabile("queryBuf", queryBuf),
          new Variabile("strDesc", strDesc) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[] { new Parametro("codiceTipoIscrizioneInps",
          codiceTipoIscrizioneInps) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[SianDAO::getDescTipoIscrizioneInpsByCodice] ", t, query,
          variabili, parametri);
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
          "[SianDAO::getDescTipoIscrizioneInpsByCodice] END.");
    }
  }

}
