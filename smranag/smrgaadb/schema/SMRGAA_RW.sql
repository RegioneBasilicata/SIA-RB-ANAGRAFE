--------------------------------------------------------
--  File creato - lunedì-luglio-31-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table WRK_SITICOMU
--------------------------------------------------------

  CREATE TABLE "SMRGAA_RW"."WRK_SITICOMU" 
   (	"ISTAT_COMUNE" VARCHAR2(6 BYTE), 
	"COD_NAZIONALE" VARCHAR2(10 BYTE), 
	"ISTATP" VARCHAR2(3 BYTE), 
	"ISTATC" VARCHAR2(3 BYTE), 
	"ID_SEZC" VARCHAR2(10 BYTE), 
	"NOME" VARCHAR2(70 BYTE), 
	"DENO_PROV" VARCHAR2(70 BYTE), 
	"SIGLA_PROV" VARCHAR2(2 BYTE), 
	"ISTATR" VARCHAR2(3 BYTE), 
	"CODI_FISC_LUNA" VARCHAR2(10 BYTE), 
	"FLAG_CATA" NUMBER(1,0), 
	"CO_FOGLIO1" NUMBER(4,0), 
	"CO_FOGLIO2" NUMBER(4,0), 
	"COD_PROVINCIA" NUMBER, 
	"COD_COMUNE" NUMBER, 
	"SEZIONE_CENSUARIA" VARCHAR2(10 BYTE), 
	"COD_TIPO_ZONA" NUMBER(1,0), 
	"DATA_AGGIORNAMENTO" DATE, 
	"ID_COMUNE" NUMBER(9,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMRGAA_RW_TBL" ;


  GRANT FLASHBACK ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT DEBUG ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT QUERY REWRITE ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT ON COMMIT REFRESH ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT REFERENCES ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT UPDATE ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT SELECT ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT INSERT ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT INDEX ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT DELETE ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
  GRANT ALTER ON "SMRGAA_RW"."WRK_SITICOMU" TO PUBLIC;
--------------------------------------------------------
--  DDL for Index IND_SITICOMU_2
--------------------------------------------------------

  CREATE INDEX "SMRGAA_RW"."IND_SITICOMU_2" ON "SMRGAA_RW"."WRK_SITICOMU" ("ID_SEZC") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMRGAA_RW_TBL" ;
--------------------------------------------------------
--  DDL for Index IND_SITICOMU
--------------------------------------------------------

  CREATE INDEX "SMRGAA_RW"."IND_SITICOMU" ON "SMRGAA_RW"."WRK_SITICOMU" ("ISTAT_COMUNE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMRGAA_RW_TBL" ;
--------------------------------------------------------
--  DDL for Index IDX_WRK_SITICOMU
--------------------------------------------------------

  CREATE INDEX "SMRGAA_RW"."IDX_WRK_SITICOMU" ON "SMRGAA_RW"."WRK_SITICOMU" ("COD_NAZIONALE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMRGAA_RW_TBL" ;
--------------------------------------------------------
--  DDL for Synonymn ABIO_D_CATEGORIA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_D_CATEGORIA_ATTIVITA" FOR "ABIO"."ABIO_D_CATEGORIA_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn ABIO_D_ORGANISMO_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_D_ORGANISMO_CONTROLLO" FOR "ABIO"."ABIO_D_ORGANISMO_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn ABIO_D_SPECIFICA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_D_SPECIFICA_ATTIVITA" FOR "ABIO"."ABIO_D_SPECIFICA_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn ABIO_D_STATO_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_D_STATO_ATTIVITA" FOR "ABIO"."ABIO_D_STATO_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn ABIO_R_CATEGORIA_SPECIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_R_CATEGORIA_SPECIFICA" FOR "ABIO"."ABIO_R_CATEGORIA_SPECIFICA";
--------------------------------------------------------
--  DDL for Synonymn ABIO_R_ODC_OPERATORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_R_ODC_OPERATORE" FOR "ABIO"."ABIO_R_ODC_OPERATORE";
--------------------------------------------------------
--  DDL for Synonymn ABIO_T_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_T_NOTIFICA" FOR "ABIO"."ABIO_T_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn ABIO_T_OPERATORE_BIOLOGICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_T_OPERATORE_BIOLOGICO" FOR "ABIO"."ABIO_T_OPERATORE_BIOLOGICO";
--------------------------------------------------------
--  DDL for Synonymn ABIO_T_POSIZIONE_OPERATORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_T_POSIZIONE_OPERATORE" FOR "ABIO"."ABIO_T_POSIZIONE_OPERATORE";
--------------------------------------------------------
--  DDL for Synonymn ABIO_T_UTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_T_UTE" FOR "ABIO"."ABIO_T_UTE";
--------------------------------------------------------
--  DDL for Synonymn ABIO_T_UTE_SPECIFICA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ABIO_T_UTE_SPECIFICA_ATTIVITA" FOR "ABIO"."ABIO_T_UTE_SPECIFICA_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn ACCESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."ACCESSO" FOR "SMRGAA"."ACCESSO";
--------------------------------------------------------
--  DDL for Synonymn BAS_T_CENTRALE_BANDI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."BAS_T_CENTRALE_BANDI" FOR "BAS"."BAS_T_CENTRALE_BANDI";
--------------------------------------------------------
--  DDL for Synonymn BAS_T_ELENCO_STAMPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."BAS_T_ELENCO_STAMPE" FOR "BAS"."BAS_T_ELENCO_STAMPE";
--------------------------------------------------------
--  DDL for Synonymn BAS_T_TEMPLATE_STAMPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."BAS_T_TEMPLATE_STAMPE" FOR "BAS"."BAS_T_TEMPLATE_STAMPE";
--------------------------------------------------------
--  DDL for Synonymn CABAAD778_PS_WP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."CABAAD778_PS_WP" FOR "SMRGAA"."CABAAD778_PS_WP";
--------------------------------------------------------
--  DDL for Synonymn CABAAPCG_TAB
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."CABAAPCG_TAB" FOR "SMRGAA"."CABAAPCG_TAB";
--------------------------------------------------------
--  DDL for Synonymn CABACALLGIS_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."CABACALLGIS_AG" FOR "SMRGAA"."CABACALLGIS_AG";
--------------------------------------------------------
--  DDL for Synonymn CATAVARITYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."CATAVARITYPE" FOR "SMRGAA"."CATAVARITYPE";
--------------------------------------------------------
--  DDL for Synonymn COMPONENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."COMPONENTE" FOR "SMRGAA"."COMPONENTE";
--------------------------------------------------------
--  DDL for Synonymn COMPONENTE_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."COMPONENTE_LIVELLO" FOR "SMRGAA"."COMPONENTE_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn COMUNE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."COMUNE" FOR "SMRGAA"."COMUNE";
--------------------------------------------------------
--  DDL for Synonymn DATISOGGTYPE_WP2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DATISOGGTYPE_WP2" FOR "SMRGAA"."DATISOGGTYPE_WP2";
--------------------------------------------------------
--  DDL for Synonymn DB_ACCESSO_PIANO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ACCESSO_PIANO_GRAFICO" FOR "SMRGAA"."DB_ACCESSO_PIANO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn DB_ACQUA_EXTRA_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ACQUA_EXTRA_10R" FOR "SMRGAA"."DB_ACQUA_EXTRA_10R";
--------------------------------------------------------
--  DDL for Synonymn DB_AGRISERV_CHIAMATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AGRISERV_CHIAMATA" FOR "DTA"."CMN_R_AGRISERV_CHIAMATA";
--------------------------------------------------------
--  DDL for Synonymn DB_AGRISERV_ESPOSITORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AGRISERV_ESPOSITORE" FOR "DTA"."CMN_D_AGRISERV_ESPOSITORE";
--------------------------------------------------------
--  DDL for Synonymn DB_AGRISERV_TIPO_CHIAMATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AGRISERV_TIPO_CHIAMATA" FOR "DTA"."CMN_D_AGRISERV_TIPO_CHIAMATA";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO" FOR "SMRGAA"."DB_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO_DICHIARAZIONE" FOR "SMRGAA"."DB_ALLEGATO_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO_DOCUMENTO" FOR "SMRGAA"."DB_ALLEGATO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO_INVIO_EMAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO_INVIO_EMAIL" FOR "SMRGAA"."DB_ALLEGATO_INVIO_EMAIL";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO_NOTIFICA" FOR "SMRGAA"."DB_ALLEGATO_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEGATO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEGATO_RICHIESTA" FOR "SMRGAA"."DB_ALLEGATO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTI_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTI_SIAN" FOR "SMRGAA"."DB_ALLEVAMENTI_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTO" FOR "SMRGAA"."DB_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTO_ACQUA_LAVAGGIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTO_ACQUA_LAVAGGIO" FOR "SMRGAA"."DB_ALLEVAMENTO_ACQUA_LAVAGGIO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTO_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTO_BIO" FOR "SMRGAA"."DB_ALLEVAMENTO_BIO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTO_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTO_NAZIONALE" FOR "SMRGAA"."DB_ALLEVAMENTO_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_ALLEVAMENTO_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALLEVAMENTO_NAZIONALE_BKP" FOR "SMRGAA"."DB_ALLEVAMENTO_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_ALTRI_DATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALTRI_DATI" FOR "SMRGAA"."DB_ALTRI_DATI";
--------------------------------------------------------
--  DDL for Synonymn DB_ALTRO_VITIGNO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALTRO_VITIGNO" FOR "SMRGAA"."DB_ALTRO_VITIGNO";
--------------------------------------------------------
--  DDL for Synonymn DB_ALTRO_VITIGNO_DICHIARATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ALTRO_VITIGNO_DICHIARATO" FOR "SMRGAA"."DB_ALTRO_VITIGNO_DICHIARATO";
--------------------------------------------------------
--  DDL for Synonymn DB_AMM_COMPETENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AMM_COMPETENZA" FOR "SMRCOMUNE"."DB_AMM_COMPETENZA";
--------------------------------------------------------
--  DDL for Synonymn DB_ANAGRAFICA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ANAGRAFICA_AZIENDA" FOR "SMRGAA"."DB_ANAGRAFICA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_ANNO_UTILIZZATO_ISTRUTTORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ANNO_UTILIZZATO_ISTRUTTORIA" FOR "SMRGAA"."DB_ANNO_UTILIZZATO_ISTRUTTORIA";
--------------------------------------------------------
--  DDL for Synonymn DB_ANOMALIA_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ANOMALIA_POLIZZA" FOR "SMRGAA"."DB_ANOMALIA_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn DB_ATECO_SEC_TRIBUTARIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ATECO_SEC_TRIBUTARIA" FOR "SMRGAA"."DB_ATECO_SEC_TRIBUTARIA";
--------------------------------------------------------
--  DDL for Synonymn DB_ATTESTAZIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ATTESTAZIONE_AZIENDA" FOR "SMRGAA"."DB_ATTESTAZIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_ATTESTAZIONE_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ATTESTAZIONE_DICHIARATA" FOR "SMRGAA"."DB_ATTESTAZIONE_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA" FOR "SMRGAA"."DB_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_AAEP" FOR "SMRGAA"."DB_AZIENDA_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_AAEP_ATECO_SEC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_AAEP_ATECO_SEC" FOR "SMRGAA"."DB_AZIENDA_AAEP_ATECO_SEC";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_AAEP_SEZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_AAEP_SEZIONI" FOR "SMRGAA"."DB_AZIENDA_AAEP_SEZIONI";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_ATECO_SEC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_ATECO_SEC" FOR "SMRGAA"."DB_AZIENDA_ATECO_SEC";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_COLLEGATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_COLLEGATA" FOR "SMRGAA"."DB_AZIENDA_COLLEGATA";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_CONTROLLO" FOR "SMRGAA"."DB_AZIENDA_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_DESTINAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_DESTINAZIONE" FOR "SMRGAA"."DB_AZIENDA_DESTINAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_EFA" FOR "SMRGAA"."DB_AZIENDA_EFA";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_GREENING" FOR "SMRGAA"."DB_AZIENDA_GREENING";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_GREENING_ESONERO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_GREENING_ESONERO" FOR "SMRGAA"."DB_AZIENDA_GREENING_ESONERO";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_MOT_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_MOT_DICHIARAZIONE" FOR "SMRGAA"."DB_AZIENDA_MOT_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_SEZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_SEZIONI" FOR "SMRGAA"."DB_AZIENDA_SEZIONI";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_SEZIONI_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_SEZIONI_AAEP" FOR "SMRGAA"."DB_AZIENDA_SEZIONI_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_TRIBUTARIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_TRIBUTARIA" FOR "SMRGAA"."DB_AZIENDA_TRIBUTARIA";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDA_TRIBUTARIA_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDA_TRIBUTARIA_BATCH" FOR "SMRGAA"."DB_AZIENDA_TRIBUTARIA_BATCH";
--------------------------------------------------------
--  DDL for Synonymn DB_AZIENDE_DOM_GRAFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_AZIENDE_DOM_GRAFICA" FOR "SMRGAA"."DB_AZIENDE_DOM_GRAFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_BDN_TIPO_CARICOALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_BDN_TIPO_CARICOALLEVAMENTO" FOR "SMRGAA"."DB_BDN_TIPO_CARICOALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_BDN_TIPO_SCARICOALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_BDN_TIPO_SCARICOALLEVAMENTO" FOR "SMRGAA"."DB_BDN_TIPO_SCARICOALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_CAPO_ALLEVAMENTO_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CAPO_ALLEVAMENTO_NAZIONALE" FOR "SMRGAA"."DB_CAPO_ALLEVAMENTO_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_CAPO_ALLEV_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CAPO_ALLEV_NAZIONALE_BKP" FOR "SMRGAA"."DB_CAPO_ALLEV_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_CATEGORIA_PRODOTTO_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CATEGORIA_PRODOTTO_MIPAF" FOR "SMRGAA"."DB_CATEGORIA_PRODOTTO_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_CATEGORIE_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CATEGORIE_ALLEVAMENTO" FOR "SMRGAA"."DB_CATEGORIE_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_CAUSALE_MODIFICA_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CAUSALE_MODIFICA_DOCUMENTO" FOR "SMRGAA"."DB_CAUSALE_MODIFICA_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_CCIAA_ALBO_DETTAGLIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CCIAA_ALBO_DETTAGLIO" FOR "SMRGAA"."DB_CCIAA_ALBO_DETTAGLIO";
--------------------------------------------------------
--  DDL for Synonymn DB_CCIAA_ALBO_VIGNETI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CCIAA_ALBO_VIGNETI" FOR "SMRGAA"."DB_CCIAA_ALBO_VIGNETI";
--------------------------------------------------------
--  DDL for Synonymn DB_CCIAA_ALBO_VIGNETI_MASSIVO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CCIAA_ALBO_VIGNETI_MASSIVO" FOR "SMRGAA"."DB_CCIAA_ALBO_VIGNETI_MASSIVO";
--------------------------------------------------------
--  DDL for Synonymn DB_CC_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CC_NAZIONALE" FOR "SMRGAA"."DB_CC_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_CC_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CC_NAZIONALE_BKP" FOR "SMRGAA"."DB_CC_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_CF_COLLEGATI_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CF_COLLEGATI_SIAN" FOR "SMRGAA"."DB_CF_COLLEGATI_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_CLASSIFICAZIONE_VINO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CLASSIFICAZIONE_VINO" FOR "SMRGAA"."DB_CLASSIFICAZIONE_VINO";
--------------------------------------------------------
--  DDL for Synonymn DB_COLORE_BACCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COLORE_BACCA" FOR "SMRGAA"."DB_COLORE_BACCA";
--------------------------------------------------------
--  DDL for Synonymn DB_COLORE_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COLORE_MIPAF" FOR "SMRGAA"."DB_COLORE_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_COLTURA_PRATICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COLTURA_PRATICATA" FOR "SMRUMA"."DB_COLTURA_PRATICATA";
--------------------------------------------------------
--  DDL for Synonymn DB_COMPAGNIA_ASSICURATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COMPAGNIA_ASSICURATRICE" FOR "SMRGAA"."DB_COMPAGNIA_ASSICURATRICE";
--------------------------------------------------------
--  DDL for Synonymn DB_COMPENSAZIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COMPENSAZIONE_AZIENDA" FOR "SMRGAA"."DB_COMPENSAZIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_COMPENSAZIONE_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COMPENSAZIONE_UV" FOR "SMRGAA"."DB_COMPENSAZIONE_UV";
--------------------------------------------------------
--  DDL for Synonymn DB_COMUNE_COLLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COMUNE_COLLEGATO" FOR "SMRCOMUNE"."DB_COMUNE_COLLEGATO";
--------------------------------------------------------
--  DDL for Synonymn DB_COMUNICAZIONE_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COMUNICAZIONE_10R" FOR "SMRGAA"."DB_COMUNICAZIONE_10R";
--------------------------------------------------------
--  DDL for Synonymn DB_CONDUZIONE_CONTRATTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONDUZIONE_CONTRATTO" FOR "SMRGAA"."DB_CONDUZIONE_CONTRATTO";
--------------------------------------------------------
--  DDL for Synonymn DB_CONDUZIONE_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONDUZIONE_DICHIARATA" FOR "SMRGAA"."DB_CONDUZIONE_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_CONDUZIONE_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONDUZIONE_ELEGGIBILITA" FOR "SMRGAA"."DB_CONDUZIONE_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn DB_CONDUZIONE_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONDUZIONE_PARTICELLA" FOR "SMRGAA"."DB_CONDUZIONE_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_CONSISTENZA_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONSISTENZA_NAZIONALE" FOR "SMRGAA"."DB_CONSISTENZA_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_CONSISTENZA_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONSISTENZA_NAZIONALE_BKP" FOR "SMRGAA"."DB_CONSISTENZA_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_CONSORZIO_DIFESA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONSORZIO_DIFESA" FOR "SMRGAA"."DB_CONSORZIO_DIFESA";
--------------------------------------------------------
--  DDL for Synonymn DB_CONTITOLARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONTITOLARE" FOR "SMRGAA"."DB_CONTITOLARE";
--------------------------------------------------------
--  DDL for Synonymn DB_CONTO_CORRENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONTO_CORRENTE" FOR "SMRGAA"."DB_CONTO_CORRENTE";
--------------------------------------------------------
--  DDL for Synonymn DB_CONTO_CORRENTE_VINCOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONTO_CORRENTE_VINCOLO" FOR "SMRGAA"."DB_CONTO_CORRENTE_VINCOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_CONTRATTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONTRATTO" FOR "SMRGAA"."DB_CONTRATTO";
--------------------------------------------------------
--  DDL for Synonymn DB_CONTRATTO_PROPRIETARI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONTRATTO_PROPRIETARI" FOR "SMRGAA"."DB_CONTRATTO_PROPRIETARI";
--------------------------------------------------------
--  DDL for Synonymn DB_CONVERSIONE_ATECO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_CONVERSIONE_ATECO" FOR "SMRGAA"."DB_CONVERSIONE_ATECO";
--------------------------------------------------------
--  DDL for Synonymn DB_COPIA_SITICOMU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_COPIA_SITICOMU" FOR "SMRGAA"."DB_COPIA_SITICOMU";
--------------------------------------------------------
--  DDL for Synonymn DB_DATI_DITTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DATI_DITTA" FOR "SMRUMA"."DB_DATI_DITTA";
--------------------------------------------------------
--  DDL for Synonymn DB_DATI_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DATI_MACCHINA" FOR "SMRUMA"."DB_DATI_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_DELEGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DELEGA" FOR "SMRGAA"."DB_DELEGA";
--------------------------------------------------------
--  DDL for Synonymn DB_D_ENTE_PRIVATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_D_ENTE_PRIVATO" FOR "SMRCOMUNE"."DB_D_ENTE_PRIVATO";
--------------------------------------------------------
--  DDL for Synonymn DB_DESTINATARIO_INVIO_EMAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DESTINATARIO_INVIO_EMAIL" FOR "SMRCOMUNE"."DB_DESTINATARIO_INVIO_EMAIL";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_ATTIVITA" FOR "SMRGAA"."DB_DETTAGLIO_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_CLASSE_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_CLASSE_ULU" FOR "SMRGAA"."DB_DETTAGLIO_CLASSE_ULU";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_MANODOPERA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_MANODOPERA" FOR "SMRGAA"."DB_DETTAGLIO_MANODOPERA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_POLIZZA" FOR "SMRGAA"."DB_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_POLIZZA_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_POLIZZA_COLTURA" FOR "SMRGAA"."DB_DETTAGLIO_POLIZZA_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_POLIZZA_STRUTTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_POLIZZA_STRUTTURA" FOR "SMRGAA"."DB_DETTAGLIO_POLIZZA_STRUTTURA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_POLIZZA_ZOOTECNIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_POLIZZA_ZOOTECNIA" FOR "SMRGAA"."DB_DETTAGLIO_POLIZZA_ZOOTECNIA";
--------------------------------------------------------
--  DDL for Synonymn DB_DETTAGLIO_VARIAZIONE_AZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETTAGLIO_VARIAZIONE_AZI" FOR "SMRGAA"."DB_DETTAGLIO_VARIAZIONE_AZI";
--------------------------------------------------------
--  DDL for Synonymn DB_DETT_UTILIZZO_PART_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETT_UTILIZZO_PART_SIAN" FOR "SMRGAA"."DB_DETT_UTILIZZO_PART_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_DETT_UTILIZZO_PART_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DETT_UTILIZZO_PART_SIAN_BKP" FOR "SMRGAA"."DB_DETT_UTILIZZO_PART_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_DET_UTILIZZO_PART_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DET_UTILIZZO_PART_NAZIONALE" FOR "SMRGAA"."DB_DET_UTILIZZO_PART_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_DICHIARAZIONE_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DICHIARAZIONE_CONSISTENZA" FOR "SMRGAA"."DB_DICHIARAZIONE_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn DB_DICHIARAZIONE_CORREZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DICHIARAZIONE_CORREZIONE" FOR "SMRGAA"."DB_DICHIARAZIONE_CORREZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_DICHIARAZIONE_SEGNALAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DICHIARAZIONE_SEGNALAZIONE" FOR "SMRGAA"."DB_DICHIARAZIONE_SEGNALAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_DIRITTO_DA_COMPENSAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DIRITTO_DA_COMPENSAZIONE" FOR "SMRGAA"."DB_DIRITTO_DA_COMPENSAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_DIRITTO_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DIRITTO_UV" FOR "SMRGAA"."DB_DIRITTO_UV";
--------------------------------------------------------
--  DDL for Synonymn DB_DITTA_UMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DITTA_UMA" FOR "SMRUMA"."DB_DITTA_UMA";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO" FOR "SMRGAA"."DB_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_CATEGORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_CATEGORIA" FOR "SMRGAA"."DB_DOCUMENTO_CATEGORIA";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_CONDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_CONDUZIONE" FOR "SMRGAA"."DB_DOCUMENTO_CONDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_CONTROLLO" FOR "SMRGAA"."DB_DOCUMENTO_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_CORR_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_CORR_PARTICELLA" FOR "SMRGAA"."DB_DOCUMENTO_CORR_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_DA_FIRMARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_DA_FIRMARE" FOR "SMRGAA"."DB_DOCUMENTO_DA_FIRMARE";
--------------------------------------------------------
--  DDL for Synonymn DB_DOCUMENTO_PROPRIETARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOCUMENTO_PROPRIETARIO" FOR "SMRGAA"."DB_DOCUMENTO_PROPRIETARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_DOMANDA_ASSEGNAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOMANDA_ASSEGNAZIONE" FOR "SMRUMA"."DB_DOMANDA_ASSEGNAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_DOMICILIO_FISC_VARIATO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_DOMICILIO_FISC_VARIATO_SIAN" FOR "SMRGAA"."DB_DOMICILIO_FISC_VARIATO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_D_SEZIONE_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_D_SEZIONE_MANUALE" FOR "SMRGAA"."DB_D_SEZIONE_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn DB_D_TIPO_GRUPPO_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_D_TIPO_GRUPPO_RUOLO" FOR "SMRCOMUNE"."DB_D_TIPO_GRUPPO_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_D_TIPO_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_D_TIPO_RUOLO" FOR "SMRCOMUNE"."DB_D_TIPO_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_EFFLUENTE_CES_ACQ_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_EFFLUENTE_CES_ACQ_10R" FOR "SMRGAA"."DB_EFFLUENTE_CES_ACQ_10R";
--------------------------------------------------------
--  DDL for Synonymn DB_EFFLUENTE_PRODOTTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_EFFLUENTE_PRODOTTO" FOR "SMRGAA"."DB_EFFLUENTE_PRODOTTO";
--------------------------------------------------------
--  DDL for Synonymn DB_EFFLUENTE_STOC_EXT_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_EFFLUENTE_STOC_EXT_10R" FOR "SMRGAA"."DB_EFFLUENTE_STOC_EXT_10R";
--------------------------------------------------------
--  DDL for Synonymn DB_EFFLUENTE_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_EFFLUENTE_10R" FOR "SMRGAA"."DB_EFFLUENTE_10R";
--------------------------------------------------------
--  DDL for Synonymn DB_ELEGGIBILITA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ELEGGIBILITA_DICHIARATA" FOR "SMRGAA"."DB_ELEGGIBILITA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_CONTROLLO_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_CONTROLLO_ALLEVAMENTO" FOR "SMRGAA"."DB_ESITO_CONTROLLO_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_CONTROLLO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_CONTROLLO_DOCUMENTO" FOR "SMRGAA"."DB_ESITO_CONTROLLO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_CONTROLLO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_CONTROLLO_PARTICELLA" FOR "SMRGAA"."DB_ESITO_CONTROLLO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_CONTROLLO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_CONTROLLO_UNAR" FOR "SMRGAA"."DB_ESITO_CONTROLLO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_DETTAGLIO_POLIZZA" FOR "SMRGAA"."DB_ESITO_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_GREENING" FOR "SMRGAA"."DB_ESITO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn DB_ESITO_PASCOLO_MAGRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESITO_PASCOLO_MAGRO" FOR "SMRGAA"."DB_ESITO_PASCOLO_MAGRO";
--------------------------------------------------------
--  DDL for Synonymn DB_ESTENSIONE_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ESTENSIONE_FILE" FOR "SMRGAA"."DB_ESTENSIONE_FILE";
--------------------------------------------------------
--  DDL for Synonymn DB_EVENTO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_EVENTO_PARTICELLA" FOR "SMRGAA"."DB_EVENTO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_FABB_DIM_TIPOLOGIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABB_DIM_TIPOLOGIA" FOR "SMRGAA"."DB_FABB_DIM_TIPOLOGIA";
--------------------------------------------------------
--  DDL for Synonymn DB_FABBRICATI_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABBRICATI_NAZIONALE" FOR "SMRGAA"."DB_FABBRICATI_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_FABBRICATI_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABBRICATI_NAZIONALE_BKP" FOR "SMRGAA"."DB_FABBRICATI_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_FABBRICATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABBRICATO" FOR "SMRGAA"."DB_FABBRICATO";
--------------------------------------------------------
--  DDL for Synonymn DB_FABBRICATO_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABBRICATO_BIO" FOR "SMRGAA"."DB_FABBRICATO_BIO";
--------------------------------------------------------
--  DDL for Synonymn DB_FABBRICATO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FABBRICATO_PARTICELLA" FOR "SMRGAA"."DB_FABBRICATO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_FASCICOLI_MACROUSI_NO_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASCICOLI_MACROUSI_NO_DETT" FOR "SMRGAA"."DB_FASCICOLI_MACROUSI_NO_DETT";
--------------------------------------------------------
--  DDL for Synonymn DB_FASCICOLI_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASCICOLI_NAZIONALE" FOR "SMRGAA"."DB_FASCICOLI_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_FASCICOLI_NAZIONALE_BACKUP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASCICOLI_NAZIONALE_BACKUP" FOR "SMRGAA"."DB_FASCICOLI_NAZIONALE_BACKUP";
--------------------------------------------------------
--  DDL for Synonymn DB_FASCICOLI_NAZIONALE_P
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASCICOLI_NAZIONALE_P" FOR "SMRGAA"."DB_FASCICOLI_NAZIONALE_P";
--------------------------------------------------------
--  DDL for Synonymn DB_FASCICOLO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASCICOLO_SIAN" FOR "SMRGAA"."DB_FASCICOLO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_FASE_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FASE_ISTANZA_RIESAME" FOR "SMRGAA"."DB_FASE_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn DB_FILE_SCARICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FILE_SCARICO" FOR "SMRGAA"."DB_FILE_SCARICO";
--------------------------------------------------------
--  DDL for Synonymn DB_FIRMADICH_FASCICOLO_DOMANDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FIRMADICH_FASCICOLO_DOMANDA" FOR "SMRGAA"."DB_FIRMADICH_FASCICOLO_DOMANDA";
--------------------------------------------------------
--  DDL for Synonymn DB_FOGLIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FOGLIO" FOR "SMRGAA"."DB_FOGLIO";
--------------------------------------------------------
--  DDL for Synonymn DB_FORMATO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_FORMATO_SCARICO_FILE" FOR "SMRGAA"."DB_FORMATO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn DB_GRAFICO_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_GRAFICO_ELEGGIBILITA" FOR "SMRGAA"."DB_GRAFICO_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn DB_GRUPPO_ATTIVITA_INEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_GRUPPO_ATTIVITA_INEA" FOR "SMRGAA"."DB_GRUPPO_ATTIVITA_INEA";
--------------------------------------------------------
--  DDL for Synonymn DB_GRUPPO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_GRUPPO_GREENING" FOR "SMRGAA"."DB_GRUPPO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn DB_GRUPPO_MACRO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_GRUPPO_MACRO_USO" FOR "SMRGAA"."DB_GRUPPO_MACRO_USO";
--------------------------------------------------------
--  DDL for Synonymn DB_GRUPPO_MANTENIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_GRUPPO_MANTENIMENTO" FOR "SMRGAA"."DB_GRUPPO_MANTENIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_COEFFICIENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_COEFFICIENTE" FOR "SMRGAA"."DB_INEA_COEFFICIENTE";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_CONTROLLO" FOR "SMRGAA"."DB_INEA_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_CONTROLLO_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_CONTROLLO_DETT" FOR "SMRGAA"."DB_INEA_CONTROLLO_DETT";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_CONTROLLO_DETT_MEMBRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_CONTROLLO_DETT_MEMBRO" FOR "SMRGAA"."DB_INEA_CONTROLLO_DETT_MEMBRO";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_OPERATORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_OPERATORE" FOR "SMRGAA"."DB_INEA_OPERATORE";
--------------------------------------------------------
--  DDL for Synonymn DB_INEA_TIPO_PARAMETRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INEA_TIPO_PARAMETRO" FOR "SMRGAA"."DB_INEA_TIPO_PARAMETRO";
--------------------------------------------------------
--  DDL for Synonymn DB_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INTERMEDIARIO" FOR "SMRGAA"."DB_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_INVIO_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INVIO_DETTAGLIO_POLIZZA" FOR "SMRGAA"."DB_INVIO_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn DB_INVIO_FASCICOLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_INVIO_FASCICOLI" FOR "SMRGAA"."DB_INVIO_FASCICOLI";
--------------------------------------------------------
--  DDL for Synonymn DB_ISOLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISOLA" FOR "SMRGAA"."DB_ISOLA";
--------------------------------------------------------
--  DDL for Synonymn DB_ISOLA_ANOMALIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISOLA_ANOMALIA" FOR "SMRGAA"."DB_ISOLA_ANOMALIA";
--------------------------------------------------------
--  DDL for Synonymn DB_ISOLA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISOLA_DICHIARATA" FOR "SMRGAA"."DB_ISOLA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_ISOLA_PARCELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISOLA_PARCELLA" FOR "SMRGAA"."DB_ISOLA_PARCELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISTANZA_RIESAME" FOR "SMRGAA"."DB_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn DB_ISTANZA_RIESAME_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISTANZA_RIESAME_AZIENDA" FOR "SMRGAA"."DB_ISTANZA_RIESAME_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_ISTANZA_RIESAME_POTENZIALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISTANZA_RIESAME_POTENZIALE" FOR "SMRGAA"."DB_ISTANZA_RIESAME_POTENZIALE";
--------------------------------------------------------
--  DDL for Synonymn DB_ISTRUTTORIA_POLIZZA_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ISTRUTTORIA_POLIZZA_COLTURA" FOR "SMRGAA"."DB_ISTRUTTORIA_POLIZZA_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn DB_ITER_RICHIESTA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ITER_RICHIESTA_AZIENDA" FOR "SMRGAA"."DB_ITER_RICHIESTA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_ITER_RIESAME_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ITER_RIESAME_AZIENDA" FOR "SMRGAA"."DB_ITER_RIESAME_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_LEGAME_EFFLUENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_LEGAME_EFFLUENTE" FOR "SMRGAA"."DB_LEGAME_EFFLUENTE";
--------------------------------------------------------
--  DDL for Synonymn DB_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MACCHINA" FOR "SMRUMA"."DB_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_MACCHINA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MACCHINA_GAA" FOR "SMRGAA"."DB_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_MACCHINE_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MACCHINE_AZ_NUOVA" FOR "SMRGAA"."DB_MACCHINE_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn DB_MANODOPERA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MANODOPERA" FOR "SMRGAA"."DB_MANODOPERA";
--------------------------------------------------------
--  DDL for Synonymn DB_MATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MATRICE" FOR "SMRUMA"."DB_MATRICE";
--------------------------------------------------------
--  DDL for Synonymn DB_MESSAGGIO_ERRORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MESSAGGIO_ERRORE" FOR "SMRCOMUNE"."DB_MESSAGGIO_ERRORE";
--------------------------------------------------------
--  DDL for Synonymn DB_MOTIVO_ESCLUSO_PROCEDIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MOTIVO_ESCLUSO_PROCEDIMENTO" FOR "SMRGAA"."DB_MOTIVO_ESCLUSO_PROCEDIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_MOTIVO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MOTIVO_RICHIESTA" FOR "SMRGAA"."DB_MOTIVO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_MOVIMENTI_TARGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_MOVIMENTI_TARGA" FOR "SMRUMA"."DB_MOVIMENTI_TARGA";
--------------------------------------------------------
--  DDL for Synonymn DB_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NOTIFICA" FOR "SMRGAA"."DB_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_NOTIFICA_ENTITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NOTIFICA_ENTITA" FOR "SMRGAA"."DB_NOTIFICA_ENTITA";
--------------------------------------------------------
--  DDL for Synonymn DB_NUMERO_PROTOCOLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NUMERO_PROTOCOLLO" FOR "SMRGAA"."DB_NUMERO_PROTOCOLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_NUMERO_PROTOCOLLO_GTFO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NUMERO_PROTOCOLLO_GTFO" FOR "SMRGAA"."DB_NUMERO_PROTOCOLLO_GTFO";
--------------------------------------------------------
--  DDL for Synonymn DB_NUMERO_TARGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NUMERO_TARGA" FOR "SMRUMA"."DB_NUMERO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn DB_NUMERO_TARGA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_NUMERO_TARGA_GAA" FOR "SMRGAA"."DB_NUMERO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn DB_ODC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ODC" FOR "SMRGAA"."DB_ODC";
--------------------------------------------------------
--  DDL for Synonymn DB_OPERATORE_BIOLOGICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_OPERATORE_BIOLOGICO" FOR "SMRGAA"."DB_OPERATORE_BIOLOGICO";
--------------------------------------------------------
--  DDL for Synonymn DB_ORIENTAMENTO_COSMAN_AMMESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ORIENTAMENTO_COSMAN_AMMESSO" FOR "SMRGAA"."DB_ORIENTAMENTO_COSMAN_AMMESSO";
--------------------------------------------------------
--  DDL for Synonymn DB_ORIENTAMENTO_PROD_COSMAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_ORIENTAMENTO_PROD_COSMAN" FOR "SMRGAA"."DB_ORIENTAMENTO_PROD_COSMAN";
--------------------------------------------------------
--  DDL for Synonymn DB_PARAMETRI_ATT_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARAMETRI_ATT_AZIENDA" FOR "SMRGAA"."DB_PARAMETRI_ATT_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_PARAMETRI_ATT_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARAMETRI_ATT_DICHIARATA" FOR "SMRGAA"."DB_PARAMETRI_ATT_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_PARAMETRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARAMETRO" FOR "SMRCOMUNE"."DB_PARAMETRO";
--------------------------------------------------------
--  DDL for Synonymn DB_PARAMETRO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARAMETRO_SCARICO_FILE" FOR "SMRGAA"."DB_PARAMETRO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn DB_PARCELLA_CONDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARCELLA_CONDUZIONE" FOR "SMRGAA"."DB_PARCELLA_CONDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA" FOR "SMRGAA"."DB_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_BIO" FOR "SMRGAA"."DB_PARTICELLA_BIO";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_CERT_ELEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_CERT_ELEG" FOR "SMRGAA"."DB_PARTICELLA_CERT_ELEG";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_CERTIFICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_CERTIFICATA" FOR "SMRGAA"."DB_PARTICELLA_CERTIFICATA";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_CERT_LOG_GIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_CERT_LOG_GIS" FOR "SMRGAA"."DB_PARTICELLA_CERT_LOG_GIS";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_POLIGONO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_POLIGONO_SIAN" FOR "SMRGAA"."DB_PARTICELLA_POLIGONO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_SIAN" FOR "SMRGAA"."DB_PARTICELLA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_SIGMATER" FOR "SMRGAA"."DB_PARTICELLA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTICELLA_SOSPESA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTICELLA_SOSPESA" FOR "SMRGAA"."DB_PARTICELLA_SOSPESA";
--------------------------------------------------------
--  DDL for Synonymn DB_PARTITE_IVA_ATTRIBUITE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PARTITE_IVA_ATTRIBUITE_SIAN" FOR "SMRGAA"."DB_PARTITE_IVA_ATTRIBUITE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_PERSONA_FISICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PERSONA_FISICA" FOR "SMRGAA"."DB_PERSONA_FISICA";
--------------------------------------------------------
--  DDL for Synonymn DB_PERSONA_FISICA_COMUNE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PERSONA_FISICA_COMUNE" FOR "SMRCOMUNE"."DB_PERSONA_FISICA_COMUNE";
--------------------------------------------------------
--  DDL for Synonymn DB_PERSONA_GIURIDICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PERSONA_GIURIDICA" FOR "SMRGAA"."DB_PERSONA_GIURIDICA";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIGONO_UNITA_ARBOREA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIGONO_UNITA_ARBOREA_SIAN" FOR "SMRGAA"."DB_POLIGONO_UNITA_ARBOREA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIZZA_ASSICURATIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIZZA_ASSICURATIVA" FOR "SMRGAA"."DB_POLIZZA_ASSICURATIVA";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIZZA_COLTURA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIZZA_COLTURA_GARANZIA" FOR "SMRGAA"."DB_POLIZZA_COLTURA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIZZA_STRUTTURA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIZZA_STRUTTURA_GARANZIA" FOR "SMRGAA"."DB_POLIZZA_STRUTTURA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIZZA_ZOOTECNIA_EPIZOOZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIZZA_ZOOTECNIA_EPIZOOZIA" FOR "SMRGAA"."DB_POLIZZA_ZOOTECNIA_EPIZOOZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_POLIZZA_ZOOTECNIA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POLIZZA_ZOOTECNIA_GARANZIA" FOR "SMRGAA"."DB_POLIZZA_ZOOTECNIA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_PORZIONE_CERTIFICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PORZIONE_CERTIFICATA" FOR "SMRGAA"."DB_PORZIONE_CERTIFICATA";
--------------------------------------------------------
--  DDL for Synonymn DB_POSSESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POSSESSO" FOR "SMRUMA"."DB_POSSESSO";
--------------------------------------------------------
--  DDL for Synonymn DB_POSSESSO_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_POSSESSO_MACCHINA" FOR "SMRGAA"."DB_POSSESSO_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_PROCEDIMENTO_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROCEDIMENTO_AZIENDA" FOR "SMRGAA"."DB_PROCEDIMENTO_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_PROCEDURA_CONCORSUALE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROCEDURA_CONCORSUALE_AAEP" FOR "SMRGAA"."DB_PROCEDURA_CONCORSUALE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETA_CERTIFICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETA_CERTIFICATA" FOR "SMRGAA"."DB_PROPRIETA_CERTIFICATA";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO" FOR "SMRUMA"."DB_PROPRIETARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO_NAZIONALE" FOR "SMRGAA"."DB_PROPRIETARIO_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO_NAZIONALE_BKP" FOR "SMRGAA"."DB_PROPRIETARIO_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO_NAZIONALE_P
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO_NAZIONALE_P" FOR "SMRGAA"."DB_PROPRIETARIO_NAZIONALE_P";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO_NAZIONALE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO_NAZIONALE_SIAN" FOR "SMRGAA"."DB_PROPRIETARIO_NAZIONALE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_PROPRIETARIO_NAZ_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_PROPRIETARIO_NAZ_SIAN_BKP" FOR "SMRGAA"."DB_PROPRIETARIO_NAZ_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_RAPPRESENTANTE_LEGALE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RAPPRESENTANTE_LEGALE_AAEP" FOR "SMRGAA"."DB_RAPPRESENTANTE_LEGALE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_RAPPRESENTANTI_SOCIETA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RAPPRESENTANTI_SOCIETA_SIAN" FOR "SMRGAA"."DB_RAPPRESENTANTI_SOCIETA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_R_ATTEST_REPORT_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_ATTEST_REPORT_SUB_REPORT" FOR "SMRGAA"."DB_R_ATTEST_REPORT_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_R_AZIENDA_INFO_AGGIUNTIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_AZIENDA_INFO_AGGIUNTIVA" FOR "SMRGAA"."DB_R_AZIENDA_INFO_AGGIUNTIVA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_COMPATIBIL_ELEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_COMPATIBIL_ELEG" FOR "SMRGAA"."DB_R_CATALOGO_COMPATIBIL_ELEG";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_GRAFICO" FOR "SMRGAA"."DB_R_CATALOGO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_MANTENIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_MANTENIMENTO" FOR "SMRGAA"."DB_R_CATALOGO_MANTENIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_MATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_MATRICE" FOR "SMRGAA"."DB_R_CATALOGO_MATRICE";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_MATRICE_SEMINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_MATRICE_SEMINA" FOR "SMRGAA"."DB_R_CATALOGO_MATRICE_SEMINA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_MATRICE_UMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_MATRICE_UMA" FOR "SMRUMA"."DB_R_CATALOGO_MATRICE_UMA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_CATALOGO_MATRICE_UMA_IRR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_CATALOGO_MATRICE_UMA_IRR" FOR "SMRUMA"."DB_R_CATALOGO_MATRICE_UMA_IRR";
--------------------------------------------------------
--  DDL for Synonymn DB_R_DOMANDA_SIAN_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_DOMANDA_SIAN_LIVELLO" FOR "SMRGAA"."DB_R_DOMANDA_SIAN_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn DB_REPORT_MOTIVO_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_REPORT_MOTIVO_DICHIARAZIONE" FOR "SMRGAA"."DB_REPORT_MOTIVO_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_RESA_PRODOTTO_LUOGO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RESA_PRODOTTO_LUOGO" FOR "SMRGAA"."DB_RESA_PRODOTTO_LUOGO";
--------------------------------------------------------
--  DDL for Synonymn DB_RESIDENZA_VARIATA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RESIDENZA_VARIATA_SIAN" FOR "SMRGAA"."DB_RESIDENZA_VARIATA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_RESPONSABILE_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RESPONSABILE_INTERMEDIARIO" FOR "SMRGAA"."DB_RESPONSABILE_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_FASE_RIESAME_TP_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_FASE_RIESAME_TP_DOCUMENTO" FOR "SMRGAA"."DB_R_FASE_RIESAME_TP_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_FOGLIO_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_FOGLIO_AREA" FOR "SMRGAA"."DB_R_FOGLIO_AREA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_GENERE_MACCHINA_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_GENERE_MACCHINA_RUOLO" FOR "SMRGAA"."DB_R_GENERE_MACCHINA_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_GRAFICO_MANTENIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_GRAFICO_MANTENIMENTO" FOR "SMRGAA"."DB_R_GRAFICO_MANTENIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_RICHIESTA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RICHIESTA_AZIENDA" FOR "SMRGAA"."DB_RICHIESTA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_RICHIESTA_AZIENDA_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RICHIESTA_AZIENDA_DOCUMENTO" FOR "SMRGAA"."DB_RICHIESTA_AZIENDA_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_RICHIESTA_FIRMA_MASSIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RICHIESTA_FIRMA_MASSIVA" FOR "SMRGAA"."DB_RICHIESTA_FIRMA_MASSIVA";
--------------------------------------------------------
--  DDL for Synonymn DB_RICHIESTA_INVIO_EMAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RICHIESTA_INVIO_EMAIL" FOR "SMRCOMUNE"."DB_RICHIESTA_INVIO_EMAIL";
--------------------------------------------------------
--  DDL for Synonymn DB_RICHIESTA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RICHIESTA_SIGMATER" FOR "SMRGAA"."DB_RICHIESTA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn DB_RIDUZIONE_RESA_VIGNETO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RIDUZIONE_RESA_VIGNETO" FOR "SMRGAA"."DB_RIDUZIONE_RESA_VIGNETO";
--------------------------------------------------------
--  DDL for Synonymn DB_RIEPILOGO_ANNUALE_PRODOTTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_RIEPILOGO_ANNUALE_PRODOTTO" FOR "SMRGAA"."DB_RIEPILOGO_ANNUALE_PRODOTTO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_MARCA_GENERE_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_MARCA_GENERE_MACCHINA" FOR "SMRGAA"."DB_R_MARCA_GENERE_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_MENU_ESPORTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_MENU_ESPORTAZIONE" FOR "SMRGAA"."DB_R_MENU_ESPORTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_R_MENU_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_MENU_REPORT" FOR "SMRGAA"."DB_R_MENU_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_R_MENZIONE_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_MENZIONE_PARTICELLA" FOR "SMRGAA"."DB_R_MENZIONE_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_PARTICELLA_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_PARTICELLA_AREA" FOR "SMRGAA"."DB_R_PARTICELLA_AREA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_PRATICA_GRUPPO_MANTENIMEN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_PRATICA_GRUPPO_MANTENIMEN" FOR "SMRGAA"."DB_R_PRATICA_GRUPPO_MANTENIMEN";
--------------------------------------------------------
--  DDL for Synonymn DB_R_PROFILO_RUOLO_IRIDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_PROFILO_RUOLO_IRIDE" FOR "SMRGAA"."DB_R_PROFILO_RUOLO_IRIDE";
--------------------------------------------------------
--  DDL for Synonymn DB_R_REPORT_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_REPORT_SUB_REPORT" FOR "SMRGAA"."DB_R_REPORT_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_R_RUOLO_ESPORTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_RUOLO_ESPORTAZIONE" FOR "SMRGAA"."DB_R_RUOLO_ESPORTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_R_RUOLO_IRIDE2_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_RUOLO_IRIDE2_MANUALE" FOR "SMRGAA"."DB_R_RUOLO_IRIDE2_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn DB_R_RUOLO_IRIDE2_RIEPILOGO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_RUOLO_IRIDE2_RIEPILOGO" FOR "SMRGAA"."DB_R_RUOLO_IRIDE2_RIEPILOGO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_RUOLO_TIPO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_RUOLO_TIPO_RICHIESTA" FOR "SMRGAA"."DB_R_RUOLO_TIPO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_SPECIE_AN_SIAN_SIAP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_SPECIE_AN_SIAN_SIAP" FOR "SMRGAA"."DB_R_SPECIE_AN_SIAN_SIAP";
--------------------------------------------------------
--  DDL for Synonymn DB_R_TIPO_DOCUMENTO_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_TIPO_DOCUMENTO_REPORT" FOR "SMRGAA"."DB_R_TIPO_DOCUMENTO_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_R_TIPO_MOTIVO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_TIPO_MOTIVO_RICHIESTA" FOR "SMRGAA"."DB_R_TIPO_MOTIVO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_R_TIPO_NOTIFICA_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_TIPO_NOTIFICA_RUOLO" FOR "SMRGAA"."DB_R_TIPO_NOTIFICA_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_UTILIZZO_SITIUNAR_DECO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_UTILIZZO_SITIUNAR_DECO" FOR "SMRGAA"."DB_R_UTILIZZO_SITIUNAR_DECO";
--------------------------------------------------------
--  DDL for Synonymn DB_R_VITIGNO_CCIAA_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_R_VITIGNO_CCIAA_VARIETA" FOR "SMRGAA"."DB_R_VITIGNO_CCIAA_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_SCARICO_LOG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SCARICO_LOG" FOR "SMRGAA"."DB_SCARICO_LOG";
--------------------------------------------------------
--  DDL for Synonymn DB_SCARICO_PARAMETRI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SCARICO_PARAMETRI" FOR "SMRGAA"."DB_SCARICO_PARAMETRI";
--------------------------------------------------------
--  DDL for Synonymn DB_SEDE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SEDE_AAEP" FOR "SMRGAA"."DB_SEDE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_SERRA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SERRA" FOR "SMRUMA"."DB_SERRA";
--------------------------------------------------------
--  DDL for Synonymn DB_SEZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SEZIONE" FOR "SMRGAA"."DB_SEZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_SPECIE_ORIENTAM_PROD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_SPECIE_ORIENTAM_PROD" FOR "SMRGAA"."DB_SIAN_SPECIE_ORIENTAM_PROD";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_TIPO_MOVIMENTO_TITOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_TIPO_MOVIMENTO_TITOLO" FOR "SMRGAA"."DB_SIAN_TIPO_MOVIMENTO_TITOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_TIPO_OPR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_TIPO_OPR" FOR "SMRGAA"."DB_SIAN_TIPO_OPR";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_TIPO_ORIGINE_TITOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_TIPO_ORIGINE_TITOLO" FOR "SMRGAA"."DB_SIAN_TIPO_ORIGINE_TITOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_TIPO_SPECIE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_TIPO_SPECIE" FOR "SMRGAA"."DB_SIAN_TIPO_SPECIE";
--------------------------------------------------------
--  DDL for Synonymn DB_SIAN_TIPO_TITOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SIAN_TIPO_TITOLO" FOR "SMRGAA"."DB_SIAN_TIPO_TITOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_SOCIETA_RAPPRESENTATE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOCIETA_RAPPRESENTATE_SIAN" FOR "SMRGAA"."DB_SOCIETA_RAPPRESENTATE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_SOGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOGGETTO" FOR "SMRGAA"."DB_SOGGETTO";
--------------------------------------------------------
--  DDL for Synonymn DB_SOGGETTO_ASSOCIATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOGGETTO_ASSOCIATO" FOR "SMRGAA"."DB_SOGGETTO_ASSOCIATO";
--------------------------------------------------------
--  DDL for Synonymn DB_SOGGETTO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOGGETTO_SCARICO_FILE" FOR "SMRGAA"."DB_SOGGETTO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn DB_SOGGETTO_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOGGETTO_SIGMATER" FOR "SMRGAA"."DB_SOGGETTO_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn DB_SOPRALLUOGO_ISTANZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOPRALLUOGO_ISTANZA" FOR "SMRGAA"."DB_SOPRALLUOGO_ISTANZA";
--------------------------------------------------------
--  DDL for Synonymn DB_SOTTOCATEGORIA_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOTTOCATEGORIA_ALLEVAMENTO" FOR "SMRGAA"."DB_SOTTOCATEGORIA_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_SOTTOCATEGORIA_ANIM_STAB
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SOTTOCATEGORIA_ANIM_STAB" FOR "SMRGAA"."DB_SOTTOCATEGORIA_ANIM_STAB";
--------------------------------------------------------
--  DDL for Synonymn DB_STABULAZIONE_TRATTAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STABULAZIONE_TRATTAMENTO" FOR "SMRGAA"."DB_STABULAZIONE_TRATTAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_STATO_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STATO_ATTIVITA" FOR "SMRGAA"."DB_STATO_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn DB_STATO_COLTIVAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STATO_COLTIVAZIONE_UNAR" FOR "SMRGAA"."DB_STATO_COLTIVAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_STATO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STATO_RICHIESTA" FOR "SMRGAA"."DB_STATO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_STATO_SITICONVOCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STATO_SITICONVOCA" FOR "SMRGAA"."DB_STATO_SITICONVOCA";
--------------------------------------------------------
--  DDL for Synonymn DB_STORICO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STORICO_PARTICELLA" FOR "SMRGAA"."DB_STORICO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_STORICO_RESIDENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STORICO_RESIDENZA" FOR "SMRGAA"."DB_STORICO_RESIDENZA";
--------------------------------------------------------
--  DDL for Synonymn DB_STORICO_UNITA_ARBOREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_STORICO_UNITA_ARBOREA" FOR "SMRGAA"."DB_STORICO_UNITA_ARBOREA";
--------------------------------------------------------
--  DDL for Synonymn DB_SUPERFICIE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_SUPERFICIE_AZIENDA" FOR "SMRUMA"."DB_SUPERFICIE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_NAZ_BRUZZONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_NAZ_BRUZZONE" FOR "SMRGAA"."DB_TERRITORIALE_NAZ_BRUZZONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_NAZIONALE" FOR "SMRGAA"."DB_TERRITORIALE_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_NAZIONALE_BKP" FOR "SMRGAA"."DB_TERRITORIALE_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_NAZIONALE_P
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_NAZIONALE_P" FOR "SMRGAA"."DB_TERRITORIALE_NAZIONALE_P";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_SIAN" FOR "SMRGAA"."DB_TERRITORIALE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_SIAN_BKP" FOR "SMRGAA"."DB_TERRITORIALE_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_TERRITORIALE_SIAN_BRUZZONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TERRITORIALE_SIAN_BRUZZONE" FOR "SMRGAA"."DB_TERRITORIALE_SIAN_BRUZZONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TESTO_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TESTO_SUB_REPORT" FOR "SMRGAA"."DB_TESTO_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ACQUA_AGRONOMICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ACQUA_AGRONOMICA" FOR "SMRGAA"."DB_TIPO_ACQUA_AGRONOMICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ALIMENTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ALIMENTAZIONE" FOR "SMRUMA"."DB_TIPO_ALIMENTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ALIMENTAZIONE_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ALIMENTAZIONE_GAA" FOR "SMRGAA"."DB_TIPO_ALIMENTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ALLEGATO" FOR "SMRGAA"."DB_TIPO_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ALLEGATO_TIPO_FIRMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ALLEGATO_TIPO_FIRMA" FOR "SMRGAA"."DB_TIPO_ALLEGATO_TIPO_FIRMA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ANCORAGGIO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ANCORAGGIO_UNAR" FOR "SMRGAA"."DB_TIPO_ANCORAGGIO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA" FOR "SMRGAA"."DB_TIPO_AREA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_A
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_A" FOR "SMRGAA"."DB_TIPO_AREA_A";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_B
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_B" FOR "SMRGAA"."DB_TIPO_AREA_B";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_C
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_C" FOR "SMRGAA"."DB_TIPO_AREA_C";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_D
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_D" FOR "SMRGAA"."DB_TIPO_AREA_D";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_E
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_E" FOR "SMRGAA"."DB_TIPO_AREA_E";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_F
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_F" FOR "SMRGAA"."DB_TIPO_AREA_F";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_G
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_G" FOR "SMRGAA"."DB_TIPO_AREA_G";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_H
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_H" FOR "SMRGAA"."DB_TIPO_AREA_H";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_I
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_I" FOR "SMRGAA"."DB_TIPO_AREA_I";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_L
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_L" FOR "SMRGAA"."DB_TIPO_AREA_L";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_M
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_M" FOR "SMRGAA"."DB_TIPO_AREA_M";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_AREA_PSN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_AREA_PSN" FOR "SMRGAA"."DB_TIPO_AREA_PSN";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ASL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ASL" FOR "SMRGAA"."DB_TIPO_ASL";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTESTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTESTAZIONE" FOR "SMRGAA"."DB_TIPO_ATTESTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTESTAZIONE_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTESTAZIONE_VARIETA" FOR "SMRGAA"."DB_TIPO_ATTESTAZIONE_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTIVITA_ATECO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTIVITA_ATECO" FOR "SMRGAA"."DB_TIPO_ATTIVITA_ATECO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTIVITA_COMPLEMENTARI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTIVITA_COMPLEMENTARI" FOR "SMRGAA"."DB_TIPO_ATTIVITA_COMPLEMENTARI";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTIVITA_INEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTIVITA_INEA" FOR "SMRGAA"."DB_TIPO_ATTIVITA_INEA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ATTIVITA_OTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ATTIVITA_OTE" FOR "SMRGAA"."DB_TIPO_ATTIVITA_OTE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_BANCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_BANCA" FOR "SMRGAA"."DB_TIPO_BANCA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CASO_PARTICOLARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CASO_PARTICOLARE" FOR "SMRGAA"."DB_TIPO_CASO_PARTICOLARE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA" FOR "SMRUMA"."DB_TIPO_CATEGORIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA_ANIMALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA_ANIMALE" FOR "SMRGAA"."DB_TIPO_CATEGORIA_ANIMALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA_ATTIVITA" FOR "SMRGAA"."DB_TIPO_CATEGORIA_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA_DOCUMENTO" FOR "SMRGAA"."DB_TIPO_CATEGORIA_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA_GAA" FOR "SMRGAA"."DB_TIPO_CATEGORIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CATEGORIA_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CATEGORIA_NOTIFICA" FOR "SMRGAA"."DB_TIPO_CATEGORIA_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CAUSA_INVALIDAZIONE_CC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CAUSA_INVALIDAZIONE_CC" FOR "SMRGAA"."DB_TIPO_CAUSA_INVALIDAZIONE_CC";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CAUSALE_EFFLUENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CAUSALE_EFFLUENTE" FOR "SMRGAA"."DB_TIPO_CAUSALE_EFFLUENTE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CAUSALE_MODIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CAUSALE_MODIFICA" FOR "SMRGAA"."DB_TIPO_CAUSALE_MODIFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CAUSALE_MOD_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CAUSALE_MOD_PARTICELLA" FOR "SMRGAA"."DB_TIPO_CAUSALE_MOD_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CCIAA_ALBO_VIGNETO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CCIAA_ALBO_VIGNETO" FOR "SMRGAA"."DB_TIPO_CCIAA_ALBO_VIGNETO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CESSAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CESSAZIONE" FOR "SMRGAA"."DB_TIPO_CESSAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CESSAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CESSAZIONE_UNAR" FOR "SMRGAA"."DB_TIPO_CESSAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CLASSE_AZIENDA_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CLASSE_AZIENDA_ULU" FOR "SMRGAA"."DB_TIPO_CLASSE_AZIENDA_ULU";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CLASSE_DIMENSIONE_AZ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CLASSE_DIMENSIONE_AZ" FOR "SMRGAA"."DB_TIPO_CLASSE_DIMENSIONE_AZ";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CLASSE_PRODUTTORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CLASSE_PRODUTTORE" FOR "SMRGAA"."DB_TIPO_CLASSE_PRODUTTORE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CLASSE_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CLASSE_ULU" FOR "SMRGAA"."DB_TIPO_CLASSE_ULU";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CLASSI_MANODOPERA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CLASSI_MANODOPERA" FOR "SMRGAA"."DB_TIPO_CLASSI_MANODOPERA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CODICE_AGRICOLTORE_ATT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CODICE_AGRICOLTORE_ATT" FOR "SMRGAA"."DB_TIPO_CODICE_AGRICOLTORE_ATT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CODIFICA_ATECO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CODIFICA_ATECO" FOR "SMRGAA"."DB_TIPO_CODIFICA_ATECO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_COLTIVAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_COLTIVAZIONE_UNAR" FOR "SMRGAA"."DB_TIPO_COLTIVAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_COLTURA" FOR "SMRUMA"."DB_TIPO_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_COLTURA_SERRA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_COLTURA_SERRA" FOR "SMRGAA"."DB_TIPO_COLTURA_SERRA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CONTRIBUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CONTRIBUTO" FOR "SMRGAA"."DB_TIPO_CONTRIBUTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CONTROLLO" FOR "SMRGAA"."DB_TIPO_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CONTROLLO_FASE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CONTROLLO_FASE" FOR "SMRGAA"."DB_TIPO_CONTROLLO_FASE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_CONTROLLO_PARAMETRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_CONTROLLO_PARAMETRO" FOR "SMRGAA"."DB_TIPO_CONTROLLO_PARAMETRO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DATA_ALLINEAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DATA_ALLINEAMENTO" FOR "SMRGAA"."DB_TIPO_DATA_ALLINEAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DEST_FABBRICATO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DEST_FABBRICATO_SIAN" FOR "SMRGAA"."DB_TIPO_DEST_FABBRICATO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DESTINAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DESTINAZIONE" FOR "SMRGAA"."DB_TIPO_DESTINAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DESTINAZIONE_PRODUTTIV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DESTINAZIONE_PRODUTTIV" FOR "SMRGAA"."DB_TIPO_DESTINAZIONE_PRODUTTIV";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DESTINO_ACQUA_LAVAGGIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DESTINO_ACQUA_LAVAGGIO" FOR "SMRGAA"."DB_TIPO_DESTINO_ACQUA_LAVAGGIO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DEST_PROD_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DEST_PROD_VARIETA" FOR "SMRGAA"."DB_TIPO_DEST_PROD_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DETTAGLIO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DETTAGLIO_USO" FOR "SMRGAA"."DB_TIPO_DETTAGLIO_USO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DIMENSIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DIMENSIONE_AZIENDA" FOR "SMRGAA"."DB_TIPO_DIMENSIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DIRITTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DIRITTO" FOR "SMRGAA"."DB_TIPO_DIRITTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DOCUMENTO" FOR "SMRGAA"."DB_TIPO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_DOMANDA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_DOMANDA_SIAN" FOR "SMRGAA"."DB_TIPO_DOMANDA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_EFA" FOR "SMRGAA"."DB_TIPO_EFA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_EFA_TIPO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_EFA_TIPO_VARIETA" FOR "SMRGAA"."DB_TIPO_EFA_TIPO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_EFFLUENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_EFFLUENTE" FOR "SMRGAA"."DB_TIPO_EFFLUENTE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ELEGGIBILITA" FOR "SMRGAA"."DB_TIPO_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ELEGGIBILITA_FIT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ELEGGIBILITA_FIT" FOR "SMRGAA"."DB_TIPO_ELEGGIBILITA_FIT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ELEGGIBILITA_RILEVATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ELEGGIBILITA_RILEVATA" FOR "SMRGAA"."DB_TIPO_ELEGGIBILITA_RILEVATA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ENTITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ENTITA" FOR "SMRGAA"."DB_TIPO_ENTITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_EPIZOOZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_EPIZOOZIA" FOR "SMRGAA"."DB_TIPO_EPIZOOZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ESITO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ESITO_GRAFICO" FOR "SMRGAA"."DB_TIPO_ESITO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ESONERO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ESONERO_GREENING" FOR "SMRGAA"."DB_TIPO_ESONERO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ESPORTAZIONE_DATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ESPORTAZIONE_DATI" FOR "SMRGAA"."DB_TIPO_ESPORTAZIONE_DATI";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ETA_IMPIANTO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ETA_IMPIANTO_UNAR" FOR "SMRGAA"."DB_TIPO_ETA_IMPIANTO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_EVENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_EVENTO" FOR "SMRGAA"."DB_TIPO_EVENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FABBRICATO_DIMENSIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FABBRICATO_DIMENSIONE" FOR "SMRGAA"."DB_TIPO_FABBRICATO_DIMENSIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FABBRICATO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FABBRICATO_SIAN" FOR "SMRGAA"."DB_TIPO_FABBRICATO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FAMIGLIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FAMIGLIA" FOR "SMRGAA"."DB_TIPO_FAMIGLIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FASCIA_FLUVIALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FASCIA_FLUVIALE" FOR "SMRGAA"."DB_TIPO_FASCIA_FLUVIALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FASE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FASE" FOR "SMRGAA"."DB_TIPO_FASE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FASE_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FASE_ALLEVAMENTO" FOR "SMRGAA"."DB_TIPO_FASE_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FG_AAEP_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FG_AAEP_GAA" FOR "SMRGAA"."DB_TIPO_FG_AAEP_GAA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FG_TIPOLOGIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FG_TIPOLOGIA" FOR "SMRGAA"."DB_TIPO_FG_TIPOLOGIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FILE" FOR "SMRGAA"."DB_TIPO_FILE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FILO_SOSTEGNO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FILO_SOSTEGNO" FOR "SMRGAA"."DB_TIPO_FILO_SOSTEGNO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FIRMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FIRMA" FOR "SMRGAA"."DB_TIPO_FIRMA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FIRMATARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FIRMATARIO" FOR "SMRGAA"."DB_TIPO_FIRMATARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FONTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FONTE" FOR "SMRGAA"."DB_TIPO_FONTE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_ALLEVAMENTO" FOR "SMRGAA"."DB_TIPO_FORMA_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_ASSOCIATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_ASSOCIATA" FOR "SMRGAA"."DB_TIPO_FORMA_ASSOCIATA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_CONDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_CONDUZIONE" FOR "SMRGAA"."DB_TIPO_FORMA_CONDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_FABBRICATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_FABBRICATO" FOR "SMRGAA"."DB_TIPO_FORMA_FABBRICATO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_GIURIDICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_GIURIDICA" FOR "SMRGAA"."DB_TIPO_FORMA_GIURIDICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_POSSESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_POSSESSO" FOR "SMRUMA"."DB_TIPO_FORMA_POSSESSO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_FORMA_POSSESSO_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_FORMA_POSSESSO_GAA" FOR "SMRGAA"."DB_TIPO_FORMA_POSSESSO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GARANZIA" FOR "SMRGAA"."DB_TIPO_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GENERE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GENERE" FOR "SMRGAA"."DB_TIPO_GENERE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GENERE_ISCRIZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GENERE_ISCRIZIONE" FOR "SMRGAA"."DB_TIPO_GENERE_ISCRIZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GENERE_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GENERE_MACCHINA" FOR "SMRUMA"."DB_TIPO_GENERE_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GENERE_MACCHINA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GENERE_MACCHINA_GAA" FOR "SMRGAA"."DB_TIPO_GENERE_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GIACITURA_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GIACITURA_UNAR" FOR "SMRGAA"."DB_TIPO_GIACITURA_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GIUDIZIO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GIUDIZIO_UNAR" FOR "SMRGAA"."DB_TIPO_GIUDIZIO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GRAFICO" FOR "SMRGAA"."DB_TIPO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GREENING" FOR "SMRGAA"."DB_TIPO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GREENING_TIPO_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GREENING_TIPO_EFA" FOR "SMRGAA"."DB_TIPO_GREENING_TIPO_EFA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GREENING_TIPO_ESONERO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GREENING_TIPO_ESONERO" FOR "SMRGAA"."DB_TIPO_GREENING_TIPO_ESONERO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GRUPPO_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GRUPPO_CONTROLLO" FOR "SMRGAA"."DB_TIPO_GRUPPO_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_GRUPPO_INEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_GRUPPO_INEA" FOR "SMRGAA"."DB_TIPO_GRUPPO_INEA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_IMPIANTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_IMPIANTO" FOR "SMRGAA"."DB_TIPO_IMPIANTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INDIRIZZO_STUDIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INDIRIZZO_STUDIO" FOR "SMRGAA"."DB_TIPO_INDIRIZZO_STUDIO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INDIRIZZO_UTILIZZO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INDIRIZZO_UTILIZZO" FOR "SMRGAA"."DB_TIPO_INDIRIZZO_UTILIZZO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INFO_AGGIUNTIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INFO_AGGIUNTIVA" FOR "SMRGAA"."DB_TIPO_INFO_AGGIUNTIVA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INTERVENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INTERVENTO" FOR "SMRGAA"."DB_TIPO_INTERVENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INTERVENTO_VITICOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INTERVENTO_VITICOLO" FOR "SMRGAA"."DB_TIPO_INTERVENTO_VITICOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_INVECCHIAMENTO_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_INVECCHIAMENTO_MIPAF" FOR "SMRGAA"."DB_TIPO_INVECCHIAMENTO_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_IRRIGAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_IRRIGAZIONE" FOR "SMRGAA"."DB_TIPO_IRRIGAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_IRRIGAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_IRRIGAZIONE_UNAR" FOR "SMRGAA"."DB_TIPO_IRRIGAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ISCRIZIONE_INPS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ISCRIZIONE_INPS" FOR "SMRGAA"."DB_TIPO_ISCRIZIONE_INPS";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_LIQUIDAZIONE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_LIQUIDAZIONE_AAEP" FOR "SMRGAA"."DB_TIPO_LIQUIDAZIONE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_LIVELLO" FOR "SMRGAA"."DB_TIPO_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MACCHINA" FOR "SMRGAA"."DB_TIPO_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MACRO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MACRO_USO" FOR "SMRGAA"."DB_TIPO_MACRO_USO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MACRO_USO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MACRO_USO_VARIETA" FOR "SMRGAA"."DB_TIPO_MACRO_USO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MARCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MARCA" FOR "SMRUMA"."DB_TIPO_MARCA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MARCA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MARCA_GAA" FOR "SMRGAA"."DB_TIPO_MARCA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MENU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MENU" FOR "SMRGAA"."DB_TIPO_MENU";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MENZIONE_GEOGRAFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MENZIONE_GEOGRAFICA" FOR "SMRGAA"."DB_TIPO_MENZIONE_GEOGRAFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MENZIONE_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MENZIONE_MIPAF" FOR "SMRGAA"."DB_TIPO_MENZIONE_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_METODO_IRRIGUO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_METODO_IRRIGUO" FOR "SMRGAA"."DB_TIPO_METODO_IRRIGUO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MOTIVO_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MOTIVO_DICHIARAZIONE" FOR "SMRGAA"."DB_TIPO_MOTIVO_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MOTIVO_RIDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MOTIVO_RIDUZIONE" FOR "SMRGAA"."DB_TIPO_MOTIVO_RIDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_MUNGITURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_MUNGITURA" FOR "SMRGAA"."DB_TIPO_MUNGITURA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_NAZIONALITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_NAZIONALITA" FOR "SMRUMA"."DB_TIPO_NAZIONALITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_NAZIONALITA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_NAZIONALITA_GAA" FOR "SMRGAA"."DB_TIPO_NAZIONALITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ORIENTAMENTO_PRODUT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ORIENTAMENTO_PRODUT" FOR "SMRGAA"."DB_TIPO_ORIENTAMENTO_PRODUT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PALO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PALO_UNAR" FOR "SMRGAA"."DB_TIPO_PALO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PARAMETRI_ATTESTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PARAMETRI_ATTESTAZIONE" FOR "SMRGAA"."DB_TIPO_PARAMETRI_ATTESTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PERIODO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PERIODO" FOR "SMRGAA"."DB_TIPO_PERIODO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PERIODO_SEMINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PERIODO_SEMINA" FOR "SMRGAA"."DB_TIPO_PERIODO_SEMINA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PIANTE_CONSOCIATE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PIANTE_CONSOCIATE" FOR "SMRGAA"."DB_TIPO_PIANTE_CONSOCIATE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_POTATURA_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_POTATURA_UNAR" FOR "SMRGAA"."DB_TIPO_POTATURA_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_POTENZIALITA_IRRIGUA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_POTENZIALITA_IRRIGUA" FOR "SMRGAA"."DB_TIPO_POTENZIALITA_IRRIGUA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PRATICA_MANTENIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PRATICA_MANTENIMENTO" FOR "SMRGAA"."DB_TIPO_PRATICA_MANTENIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PREFISSO_CELLULARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PREFISSO_CELLULARE" FOR "SMRGAA"."DB_TIPO_PREFISSO_CELLULARE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PROCEDIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PROCEDIMENTO" FOR "SMRGAA"."DB_TIPO_PROCEDIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PRODOTTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PRODOTTO" FOR "SMRGAA"."DB_TIPO_PRODOTTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PRODOTTO_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PRODOTTO_MIPAF" FOR "SMRGAA"."DB_TIPO_PRODOTTO_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_PRODUZIONE_COSMAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_PRODUZIONE_COSMAN" FOR "SMRGAA"."DB_TIPO_PRODUZIONE_COSMAN";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_QUALITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_QUALITA" FOR "SMRGAA"."DB_TIPO_QUALITA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_QUALITA_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_QUALITA_USO" FOR "SMRGAA"."DB_TIPO_QUALITA_USO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_REPORT" FOR "SMRGAA"."DB_TIPO_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_RICHIESTA" FOR "SMRGAA"."DB_TIPO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_RIEPILOGO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_RIEPILOGO" FOR "SMRGAA"."DB_TIPO_RIEPILOGO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ROCCIA_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ROCCIA_UNAR" FOR "SMRGAA"."DB_TIPO_ROCCIA_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ROTAZIONE_COLTURALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ROTAZIONE_COLTURALE" FOR "SMRGAA"."DB_TIPO_ROTAZIONE_COLTURALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_RUOLO" FOR "SMRGAA"."DB_TIPO_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SCADENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SCADENZA" FOR "SMRGAA"."DB_TIPO_SCADENZA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SCARICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SCARICO" FOR "SMRUMA"."DB_TIPO_SCARICO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SCARICO_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SCARICO_GAA" FOR "SMRGAA"."DB_TIPO_SCARICO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SCHELETRO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SCHELETRO_UNAR" FOR "SMRGAA"."DB_TIPO_SCHELETRO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SEMINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SEMINA" FOR "SMRGAA"."DB_TIPO_SEMINA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SETTORE_ABACO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SETTORE_ABACO" FOR "SMRGAA"."DB_TIPO_SETTORE_ABACO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SEZIONI_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SEZIONI_AAEP" FOR "SMRGAA"."DB_TIPO_SEZIONI_AAEP";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SOSPENSIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SOSPENSIONE" FOR "SMRGAA"."DB_TIPO_SOSPENSIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SOTTOCATEGORIA_ANIMALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SOTTOCATEGORIA_ANIMALE" FOR "SMRGAA"."DB_TIPO_SOTTOCATEGORIA_ANIMALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SP_ANIM_ATTESTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SP_ANIM_ATTESTAZIONE" FOR "SMRGAA"."DB_TIPO_SP_ANIM_ATTESTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SPECIE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SPECIE" FOR "SMRGAA"."DB_TIPO_SPECIE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SPECIE_ANIMALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SPECIE_ANIMALE" FOR "SMRGAA"."DB_TIPO_SPECIE_ANIMALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SPECIE_ORIENTAM_PROD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SPECIE_ORIENTAM_PROD" FOR "SMRGAA"."DB_TIPO_SPECIE_ORIENTAM_PROD";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SPORTELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SPORTELLO" FOR "SMRGAA"."DB_TIPO_SPORTELLO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_STABULAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_STABULAZIONE" FOR "SMRGAA"."DB_TIPO_STABULAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_STATO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_STATO_DOCUMENTO" FOR "SMRGAA"."DB_TIPO_STATO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_STATO_VEGETATIVO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_STATO_VEGETATIVO_UNAR" FOR "SMRGAA"."DB_TIPO_STATO_VEGETATIVO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_SUB_REPORT" FOR "SMRGAA"."DB_TIPO_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TARGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TARGA" FOR "SMRUMA"."DB_TIPO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TARGA_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TARGA_GAA" FOR "SMRGAA"."DB_TIPO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TERRAZZAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TERRAZZAMENTO" FOR "SMRGAA"."DB_TIPO_TERRAZZAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPO_FOTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPO_FOTO" FOR "SMRGAA"."DB_TIPO_TIPO_FOTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_AZIENDA" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_CONTRATTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_CONTRATTO" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_CONTRATTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_DOCUMENTO" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_FABBRICATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_FABBRICATO" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_FABBRICATO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_NOTIFICA" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_UNAR" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_VARIAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_VARIAZIONE" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_VARIAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_VIGNETO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_VIGNETO" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_VIGNETO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_VINO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_VINO" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_VINO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_VINO_ODC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_VINO_ODC" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_VINO_ODC";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPOLOGIA_VINO_RICAD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPOLOGIA_VINO_RICAD" FOR "SMRGAA"."DB_TIPO_TIPOLOGIA_VINO_RICAD";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TIPO_PRODUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TIPO_PRODUZIONE" FOR "SMRGAA"."DB_TIPO_TIPO_PRODUZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TITOLO_POSSESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TITOLO_POSSESSO" FOR "SMRGAA"."DB_TIPO_TITOLO_POSSESSO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TITOLO_STUDIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TITOLO_STUDIO" FOR "SMRGAA"."DB_TIPO_TITOLO_STUDIO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TRATTAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TRATTAMENTO" FOR "SMRGAA"."DB_TIPO_TRATTAMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TRAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TRAZIONE" FOR "SMRUMA"."DB_TIPO_TRAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_TRAZIONE_GAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_TRAZIONE_GAA" FOR "SMRGAA"."DB_TIPO_TRAZIONE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_UDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_UDE" FOR "SMRGAA"."DB_TIPO_UDE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_UTILIZZO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_UTILIZZO" FOR "SMRGAA"."DB_TIPO_UTILIZZO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_UTILIZZO_PROCEDIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_UTILIZZO_PROCEDIMENTO" FOR "SMRGAA"."DB_TIPO_UTILIZZO_PROCEDIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_UTILIZZO_UMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_UTILIZZO_UMA" FOR "SMRUMA"."DB_TIPO_UTILIZZO_UMA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VALORE_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VALORE_AREA" FOR "SMRGAA"."DB_TIPO_VALORE_AREA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VARIAZIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VARIAZIONE_AZIENDA" FOR "SMRGAA"."DB_TIPO_VARIAZIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VARIAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VARIAZIONE_UNAR" FOR "SMRGAA"."DB_TIPO_VARIAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VARIETA" FOR "SMRGAA"."DB_TIPO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VARIETA_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VARIETA_UNAR" FOR "SMRGAA"."DB_TIPO_VARIETA_UNAR";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VERSIONE_MATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VERSIONE_MATRICE" FOR "SMRGAA"."DB_TIPO_VERSIONE_MATRICE";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VINIDOC_COMUNI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VINIDOC_COMUNI" FOR "SMRGAA"."DB_TIPO_VINIDOC_COMUNI";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VINO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VINO" FOR "SMRGAA"."DB_TIPO_VINO";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VINO_CCIAA_SIAP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VINO_CCIAA_SIAP" FOR "SMRGAA"."DB_TIPO_VINO_CCIAA_SIAP";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VINO_MIPAF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VINO_MIPAF" FOR "SMRGAA"."DB_TIPO_VINO_MIPAF";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VINO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VINO_VARIETA" FOR "SMRGAA"."DB_TIPO_VINO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_VITIGNO_CCIAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_VITIGNO_CCIAA" FOR "SMRGAA"."DB_TIPO_VITIGNO_CCIAA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ZONA_ALTIMETRICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ZONA_ALTIMETRICA" FOR "SMRGAA"."DB_TIPO_ZONA_ALTIMETRICA";
--------------------------------------------------------
--  DDL for Synonymn DB_TIPO_ZONA_ALT_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIPO_ZONA_ALT_ULU" FOR "SMRGAA"."DB_TIPO_ZONA_ALT_ULU";
--------------------------------------------------------
--  DDL for Synonymn DB_TITOLARITA_PARTICELLA_SIG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TITOLARITA_PARTICELLA_SIG" FOR "SMRGAA"."DB_TITOLARITA_PARTICELLA_SIG";
--------------------------------------------------------
--  DDL for Synonymn DB_TITOLARITA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TITOLARITA_SIGMATER" FOR "SMRGAA"."DB_TITOLARITA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn DB_TIT_POSSESSO_PROCEDIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TIT_POSSESSO_PROCEDIMENTO" FOR "SMRGAA"."DB_TIT_POSSESSO_PROCEDIMENTO";
--------------------------------------------------------
--  DDL for Synonymn DB_T_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_T_MANUALE" FOR "SMRGAA"."DB_T_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn DB_TMP_INEA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TMP_INEA_AZIENDA" FOR "SMRGAA"."DB_TMP_INEA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn DB_TMP_INEA_AZIENDA_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TMP_INEA_AZIENDA_DETT" FOR "SMRGAA"."DB_TMP_INEA_AZIENDA_DETT";
--------------------------------------------------------
--  DDL for Synonymn DB_TP_DICHIARAZ_TP_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TP_DICHIARAZ_TP_ALLEGATO" FOR "SMRGAA"."DB_TP_DICHIARAZ_TP_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn DB_TP_DOCUMENTO_TP_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_TP_DOCUMENTO_TP_ALLEGATO" FOR "SMRGAA"."DB_TP_DOCUMENTO_TP_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn DB_UFFICIO_ZONA_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UFFICIO_ZONA_INTERMEDIARIO" FOR "SMRGAA"."DB_UFFICIO_ZONA_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn DB_UNAR_PARCELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UNAR_PARCELLA" FOR "SMRGAA"."DB_UNAR_PARCELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_UNITA_ARBOREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UNITA_ARBOREA" FOR "SMRGAA"."DB_UNITA_ARBOREA";
--------------------------------------------------------
--  DDL for Synonymn DB_UNITA_ARBOREA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UNITA_ARBOREA_DICHIARATA" FOR "SMRGAA"."DB_UNITA_ARBOREA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn DB_UNITA_ARBOREA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UNITA_ARBOREA_SIAN" FOR "SMRGAA"."DB_UNITA_ARBOREA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_UNITA_MISURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UNITA_MISURA" FOR "SMRGAA"."DB_UNITA_MISURA";
--------------------------------------------------------
--  DDL for Synonymn DB_UTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTE" FOR "SMRGAA"."DB_UTE";
--------------------------------------------------------
--  DDL for Synonymn DB_UTE_ATECO_SECONDARI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTE_ATECO_SECONDARI" FOR "SMRGAA"."DB_UTE_ATECO_SECONDARI";
--------------------------------------------------------
--  DDL for Synonymn DB_UTE_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTE_NAZIONALE" FOR "SMRGAA"."DB_UTE_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_UTE_NAZIONALE_BCK
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTE_NAZIONALE_BCK" FOR "SMRGAA"."DB_UTE_NAZIONALE_BCK";
--------------------------------------------------------
--  DDL for Synonymn DB_UTENTE_ABILITAZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTENTE_ABILITAZIONI" FOR "SMRCOMUNE"."DB_UTENTE_ABILITAZIONI";
--------------------------------------------------------
--  DDL for Synonymn DB_UTENTE_IN_CONFLITTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTENTE_IN_CONFLITTO" FOR "SMRGAA"."DB_UTENTE_IN_CONFLITTO";
--------------------------------------------------------
--  DDL for Synonymn DB_UTENTE_IRIDE2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTENTE_IRIDE2" FOR "SMRCOMUNE"."DB_UTENTE_IRIDE2";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO" FOR "SMRUMA"."DB_UTILIZZO";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_CONSOCIATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_CONSOCIATO" FOR "SMRGAA"."DB_UTILIZZO_CONSOCIATO";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_CONSOCIATO_DICH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_CONSOCIATO_DICH" FOR "SMRGAA"."DB_UTILIZZO_CONSOCIATO_DICH";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_DICHIARATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_DICHIARATO" FOR "SMRGAA"."DB_UTILIZZO_DICHIARATO";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_FABB_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_FABB_NAZIONALE" FOR "SMRGAA"."DB_UTILIZZO_FABB_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_FABB_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_FABB_NAZIONALE_BKP" FOR "SMRGAA"."DB_UTILIZZO_FABB_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PARTICELLA" FOR "SMRGAA"."DB_UTILIZZO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PART_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PART_NAZIONALE" FOR "SMRGAA"."DB_UTILIZZO_PART_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PART_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PART_NAZIONALE_BKP" FOR "SMRGAA"."DB_UTILIZZO_PART_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PART_NAZIONALE_P
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PART_NAZIONALE_P" FOR "SMRGAA"."DB_UTILIZZO_PART_NAZIONALE_P";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PART_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PART_SIAN" FOR "SMRGAA"."DB_UTILIZZO_PART_SIAN";
--------------------------------------------------------
--  DDL for Synonymn DB_UTILIZZO_PART_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UTILIZZO_PART_SIAN_BKP" FOR "SMRGAA"."DB_UTILIZZO_PART_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_UV_NAZIONALE_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UV_NAZIONALE_DETT" FOR "SMRGAA"."DB_UV_NAZIONALE_DETT";
--------------------------------------------------------
--  DDL for Synonymn DB_UV_NAZIONALE_DETT_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UV_NAZIONALE_DETT_BKP" FOR "SMRGAA"."DB_UV_NAZIONALE_DETT_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_UV_NAZIONALE_PROV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UV_NAZIONALE_PROV" FOR "SMRGAA"."DB_UV_NAZIONALE_PROV";
--------------------------------------------------------
--  DDL for Synonymn DB_UV_NAZIONALE_PROV_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_UV_NAZIONALE_PROV_BKP" FOR "SMRGAA"."DB_UV_NAZIONALE_PROV_BKP";
--------------------------------------------------------
--  DDL for Synonymn DB_VARIAZIONE_AZIENDALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VARIAZIONE_AZIENDALE" FOR "SMRGAA"."DB_VARIAZIONE_AZIENDALE";
--------------------------------------------------------
--  DDL for Synonymn DB_VARIETA_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VARIETA_ELEGGIBILITA" FOR "SMRGAA"."DB_VARIETA_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn DB_VARIETA_FONTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VARIETA_FONTE" FOR "SMRGAA"."DB_VARIETA_FONTE";
--------------------------------------------------------
--  DDL for Synonymn DB_VIGNA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VIGNA" FOR "SMRGAA"."DB_VIGNA";
--------------------------------------------------------
--  DDL for Synonymn DB_VIGNA_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VIGNA_PARTICELLA" FOR "SMRGAA"."DB_VIGNA_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn DB_VISIONE_VARIAZIONE_AZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DB_VISIONE_VARIAZIONE_AZI" FOR "SMRGAA"."DB_VISIONE_VARIAZIONE_AZI";
--------------------------------------------------------
--  DDL for Synonymn DECO_COND_AGEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DECO_COND_AGEA" FOR "SMRGAA"."DECO_COND_AGEA";
--------------------------------------------------------
--  DDL for Synonymn DEUSPOLITYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."DEUSPOLITYPE_AG" FOR "SMRGAA"."DEUSPOLITYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn FUNZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."FUNZIONE" FOR "SMRGAA"."FUNZIONE";
--------------------------------------------------------
--  DDL for Synonymn GIS_ELEG_FIT_2009
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GIS_ELEG_FIT_2009" FOR "SMRGAA"."GIS_ELEG_FIT_2009";
--------------------------------------------------------
--  DDL for Synonymn GIS_ELEG_2009
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GIS_ELEG_2009" FOR "SMRGAA"."GIS_ELEG_2009";
--------------------------------------------------------
--  DDL for Synonymn GNPS_D_AIUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_D_AIUTO" FOR "GNPS"."GNPS_D_AIUTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_D_INTERVENTO_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_D_INTERVENTO_UV" FOR "GNPS"."GNPS_D_INTERVENTO_UV";
--------------------------------------------------------
--  DDL for Synonymn GNPS_D_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_D_LIVELLO" FOR "GNPS"."GNPS_D_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_D_STATO_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_D_STATO_PRATICA" FOR "GNPS"."GNPS_D_STATO_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn GNPS_D_TIPO_IMPORTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_D_TIPO_IMPORTO" FOR "GNPS"."GNPS_D_TIPO_IMPORTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_R_LIVELLO_AIUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_R_LIVELLO_AIUTO" FOR "GNPS"."GNPS_R_LIVELLO_AIUTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_COORDINATE_PAGAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_COORDINATE_PAGAMENTO" FOR "GNPS"."GNPS_T_COORDINATE_PAGAMENTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_FASE_AVANZAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_FASE_AVANZAMENTO" FOR "GNPS"."GNPS_T_FASE_AVANZAMENTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_FASE_BACKOFFICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_FASE_BACKOFFICE" FOR "GNPS"."GNPS_T_FASE_BACKOFFICE";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_FORMA_PAGAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_FORMA_PAGAMENTO" FOR "GNPS"."GNPS_T_FORMA_PAGAMENTO";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_IMPORTI_LIQUIDATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_IMPORTI_LIQUIDATI" FOR "GNPS"."GNPS_T_IMPORTI_LIQUIDATI";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_INTERVENTI_RIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_INTERVENTI_RIS" FOR "GNPS"."GNPS_T_INTERVENTI_RIS";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_ITER_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_ITER_PRATICA" FOR "GNPS"."GNPS_T_ITER_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_LISTA_LIQUIDAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_LISTA_LIQUIDAZIONE" FOR "GNPS"."GNPS_T_LISTA_LIQUIDAZIONE";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_PARTICELLA_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_PARTICELLA_COLTURA" FOR "GNPS"."GNPS_T_PARTICELLA_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_PRATICA" FOR "GNPS"."GNPS_T_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_UV_IMPIANTO_RIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_UV_IMPIANTO_RIS" FOR "GNPS"."GNPS_T_UV_IMPIANTO_RIS";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_VALORI_PREVISTI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_VALORI_PREVISTI" FOR "GNPS"."GNPS_T_VALORI_PREVISTI";
--------------------------------------------------------
--  DDL for Synonymn GNPS_T_VOCE_BUDGET
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."GNPS_T_VOCE_BUDGET" FOR "GNPS"."GNPS_T_VOCE_BUDGET";
--------------------------------------------------------
--  DDL for Synonymn LIVELLO_ABILITAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."LIVELLO_ABILITAZIONE" FOR "SMRGAA"."LIVELLO_ABILITAZIONE";
--------------------------------------------------------
--  DDL for Synonymn MATB1CATATYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."MATB1CATATYPE" FOR "SMRGAA"."MATB1CATATYPE";
--------------------------------------------------------
--  DDL for Synonymn NUM_VARRAY
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."NUM_VARRAY" FOR "SMRGAA"."NUM_VARRAY";
--------------------------------------------------------
--  DDL for Synonymn PACK_AGGIORNA_ATTESTAZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_AGGIORNA_ATTESTAZIONI" FOR "SMRGAA"."PACK_AGGIORNA_ATTESTAZIONI";
--------------------------------------------------------
--  DDL for Synonymn PACK_AGGIORNA_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_AGGIORNA_UV" FOR "SMRGAA"."PACK_AGGIORNA_UV";
--------------------------------------------------------
--  DDL for Synonymn PACK_COMUNICAZIONE_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_COMUNICAZIONE_10R" FOR "SMRGAA"."PACK_COMUNICAZIONE_10R";
--------------------------------------------------------
--  DDL for Synonymn PACK_CONTROLLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_CONTROLLI" FOR "SMRGAA"."PACK_CONTROLLI";
--------------------------------------------------------
--  DDL for Synonymn PACK_CONTROLLI_PARTICELLARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_CONTROLLI_PARTICELLARE" FOR "SMRGAA"."PACK_CONTROLLI_PARTICELLARE";
--------------------------------------------------------
--  DDL for Synonymn PACK_DICHIARAZIONE_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_DICHIARAZIONE_CONSISTENZA" FOR "SMRGAA"."PACK_DICHIARAZIONE_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn PACK_IMPORTA_DATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_IMPORTA_DATI" FOR "SMRUMA_RW"."PACK_IMPORTA_DATI";
--------------------------------------------------------
--  DDL for Synonymn PACK_INEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_INEA" FOR "SMRGAA"."PACK_INEA";
--------------------------------------------------------
--  DDL for Synonymn PACK_MANAGE_SEQUENCE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_MANAGE_SEQUENCE" FOR "SMRGAA"."PACK_MANAGE_SEQUENCE";
--------------------------------------------------------
--  DDL for Synonymn PACK_PRATICA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_PRATICA_AZIENDA" FOR "SMRGAA"."PACK_PRATICA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn PACK_RIBALTAMENTO_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_RIBALTAMENTO_CONSISTENZA" FOR "SMRGAA"."PACK_RIBALTAMENTO_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn PACK_RIBALTA_UV_SU_PCOLTURALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_RIBALTA_UV_SU_PCOLTURALE" FOR "SMRGAA"."PACK_RIBALTA_UV_SU_PCOLTURALE";
--------------------------------------------------------
--  DDL for Synonymn PACK_RIPRISTINA_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_RIPRISTINA_DICHIARAZIONE" FOR "SMRGAA"."PACK_RIPRISTINA_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn PACK_SRV_ENTI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PACK_SRV_ENTI" FOR "GNPS"."PACK_SRV_ENTI";
--------------------------------------------------------
--  DDL for Synonymn PAPUA_V_UTENTE_LOGIN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PAPUA_V_UTENTE_LOGIN" FOR "PAPUA"."PAPUA_V_UTENTE_LOGIN";
--------------------------------------------------------
--  DDL for Synonymn PATE_V_PERSONA_ULTIMO_CERTIF
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PATE_V_PERSONA_ULTIMO_CERTIF" FOR "SMRPATE"."PATE_V_PERSONA_ULTIMO_CERTIF";
--------------------------------------------------------
--  DDL for Synonymn PCK_AGGIORNA_DATI_BIOLOGICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_AGGIORNA_DATI_BIOLOGICO" FOR "SMRGAA"."PCK_AGGIORNA_DATI_BIOLOGICO";
--------------------------------------------------------
--  DDL for Synonymn PCK_AGGIORNA_DATI_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_AGGIORNA_DATI_ELEGGIBILITA" FOR "SMRGAA"."PCK_AGGIORNA_DATI_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn PCK_AGGIORNA_FASCICOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_AGGIORNA_FASCICOLO" FOR "ABIO_RW"."PCK_AGGIORNA_FASCICOLO";
--------------------------------------------------------
--  DDL for Synonymn PCK_AGGIORNA_ISOLE_PARCELLE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_AGGIORNA_ISOLE_PARCELLE" FOR "SMRGAA"."PCK_AGGIORNA_ISOLE_PARCELLE";
--------------------------------------------------------
--  DDL for Synonymn PCK_AGGIORNA_PROCEDIMENTI_AZ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_AGGIORNA_PROCEDIMENTI_AZ" FOR "SMRGAA"."PCK_AGGIORNA_PROCEDIMENTI_AZ";
--------------------------------------------------------
--  DDL for Synonymn PCK_CALCOLO_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_CALCOLO_EFA" FOR "SMRGAA"."PCK_CALCOLO_EFA";
--------------------------------------------------------
--  DDL for Synonymn PCK_CALCOLO_ESITO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_CALCOLO_ESITO_GREENING" FOR "SMRGAA"."PCK_CALCOLO_ESITO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn PCK_CALCOLO_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_CALCOLO_ULU" FOR "SMRGAA"."PCK_CALCOLO_ULU";
--------------------------------------------------------
--  DDL for Synonymn PCK_CARICA_MISURA_H
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_CARICA_MISURA_H" FOR "SMRGAA"."PCK_CARICA_MISURA_H";
--------------------------------------------------------
--  DDL for Synonymn PCK_CARICA_MISURA_H_ALTRO_CUAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_CARICA_MISURA_H_ALTRO_CUAA" FOR "SMRGAA"."PCK_CARICA_MISURA_H_ALTRO_CUAA";
--------------------------------------------------------
--  DDL for Synonymn PCK_ELENCA_SOCI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_ELENCA_SOCI" FOR "ABIO"."PCK_ELENCA_SOCI";
--------------------------------------------------------
--  DDL for Synonymn PCK_EXP_PARTICELLE_OUT_CATASTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_EXP_PARTICELLE_OUT_CATASTO" FOR "SMRGAA"."PCK_EXP_PARTICELLE_OUT_CATASTO";
--------------------------------------------------------
--  DDL for Synonymn PCK_GESTIONE_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_GESTIONE_ISTANZA_RIESAME" FOR "SMRGAA"."PCK_GESTIONE_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn PCK_GESTIONE_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_GESTIONE_POLIZZA" FOR "SMRGAA"."PCK_GESTIONE_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn PCK_GESTIONE_REGISTRO_PASCOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_GESTIONE_REGISTRO_PASCOLO" FOR "SMRGAA"."PCK_GESTIONE_REGISTRO_PASCOLO";
--------------------------------------------------------
--  DDL for Synonymn PCK_GESTIONE_SOCI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_GESTIONE_SOCI" FOR "SMRGAA"."PCK_GESTIONE_SOCI";
--------------------------------------------------------
--  DDL for Synonymn PCK_SCARICA_ANAGRAFICA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_SCARICA_ANAGRAFICA_AZIENDA" FOR "SMRGAA"."PCK_SCARICA_ANAGRAFICA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn PCK_SCARICA_SCHEDE_AGRONOMICHE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_SCARICA_SCHEDE_AGRONOMICHE" FOR "SMRGAA"."PCK_SCARICA_SCHEDE_AGRONOMICHE";
--------------------------------------------------------
--  DDL for Synonymn PCK_SMRGAA_LIBRERIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_SMRGAA_LIBRERIA" FOR "SMRGAA"."PCK_SMRGAA_LIBRERIA";
--------------------------------------------------------
--  DDL for Synonymn PCK_SMRGAA_LOG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_SMRGAA_LOG" FOR "SMRGAA"."PCK_SMRGAA_LOG";
--------------------------------------------------------
--  DDL for Synonymn PCK_UTILITY_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_UTILITY_FILE" FOR "SMRGAA"."PCK_UTILITY_FILE";
--------------------------------------------------------
--  DDL for Synonymn PCK_UTL_OBFUSCATION
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_UTL_OBFUSCATION" FOR "SMRGAA"."PCK_UTL_OBFUSCATION";
--------------------------------------------------------
--  DDL for Synonymn PCK_UTL_OBJ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_UTL_OBJ" FOR "SMRGAA"."PCK_UTL_OBJ";
--------------------------------------------------------
--  DDL for Synonymn PCK_VARIAZIONI_AZIENDALI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PCK_VARIAZIONI_AZIENDALI" FOR "SMRGAA"."PCK_VARIAZIONI_AZIENDALI";
--------------------------------------------------------
--  DDL for Synonymn PERSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PERSISTENZA" FOR "SMRGAA"."PERSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn PLAN_TABLE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PLAN_TABLE" FOR "SMRGAA"."PLAN_TABLE";
--------------------------------------------------------
--  DDL for Synonymn PRATMANTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PRATMANTYPE" FOR "SMRGAA"."PRATMANTYPE";
--------------------------------------------------------
--  DDL for Synonymn PRIMO_ACCESSO_UTENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PRIMO_ACCESSO_UTENTE" FOR "SMRGAA"."PRIMO_ACCESSO_UTENTE";
--------------------------------------------------------
--  DDL for Synonymn PROFILO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PROFILO" FOR "SMRGAA"."PROFILO";
--------------------------------------------------------
--  DDL for Synonymn PROFILO_FUNZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PROFILO_FUNZIONE" FOR "SMRGAA"."PROFILO_FUNZIONE";
--------------------------------------------------------
--  DDL for Synonymn PROVINCIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PROVINCIA" FOR "SMRGAA"."PROVINCIA";
--------------------------------------------------------
--  DDL for Synonymn PSR_D_BANDO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_D_BANDO" FOR "PSR"."PSR_D_BANDO";
--------------------------------------------------------
--  DDL for Synonymn PSR_D_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_D_LIVELLO" FOR "PSR"."PSR_D_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn PSR_D_OGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_D_OGGETTO" FOR "PSR"."PSR_D_OGGETTO";
--------------------------------------------------------
--  DDL for Synonymn PSR_D_STATO_OGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_D_STATO_OGGETTO" FOR "PSR"."PSR_D_STATO_OGGETTO";
--------------------------------------------------------
--  DDL for Synonymn PSR_D_TIPO_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_D_TIPO_LIVELLO" FOR "PSR"."PSR_D_TIPO_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn PSR_R_LEGAME_GRUPPO_OGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_R_LEGAME_GRUPPO_OGGETTO" FOR "PSR"."PSR_R_LEGAME_GRUPPO_OGGETTO";
--------------------------------------------------------
--  DDL for Synonymn PSR_R_PROCEDIMENTO_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_R_PROCEDIMENTO_LIVELLO" FOR "PSR"."PSR_R_PROCEDIMENTO_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn PSR_T_DATI_PARTICELLE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_T_DATI_PARTICELLE_SIAN" FOR "PSR"."PSR_T_DATI_PARTICELLE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn PSR_T_ITER_PROCEDIMENTO_OGGETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_T_ITER_PROCEDIMENTO_OGGETT" FOR "PSR"."PSR_T_ITER_PROCEDIMENTO_OGGETT";
--------------------------------------------------------
--  DDL for Synonymn PSR_T_PROCEDIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_T_PROCEDIMENTO" FOR "PSR"."PSR_T_PROCEDIMENTO";
--------------------------------------------------------
--  DDL for Synonymn PSR_T_PROCEDIMENTO_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_T_PROCEDIMENTO_AZIENDA" FOR "PSR"."PSR_T_PROCEDIMENTO_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn PSR_T_PROCEDIMENTO_OGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."PSR_T_PROCEDIMENTO_OGGETTO" FOR "PSR"."PSR_T_PROCEDIMENTO_OGGETTO";
--------------------------------------------------------
--  DDL for Synonymn RECCOPAPOLITYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECCOPAPOLITYPE_AG" FOR "SMRGAA"."RECCOPAPOLITYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn RECDEUSPOLITYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECDEUSPOLITYPE_AG" FOR "SMRGAA"."RECDEUSPOLITYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn RECDEUSPOLITYPE_WP2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECDEUSPOLITYPE_WP2" FOR "SMRGAA"."RECDEUSPOLITYPE_WP2";
--------------------------------------------------------
--  DDL for Synonymn RECPARTICELLEGISTYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECPARTICELLEGISTYPE_AG" FOR "SMRGAA"."RECPARTICELLEGISTYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn RECSITIPART
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECSITIPART" FOR "SMRGAA"."RECSITIPART";
--------------------------------------------------------
--  DDL for Synonymn RECSITISUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RECSITISUOLO" FOR "SMRGAA"."RECSITISUOLO";
--------------------------------------------------------
--  DDL for Synonymn REGIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."REGIONE" FOR "SMRGAA"."REGIONE";
--------------------------------------------------------
--  DDL for Synonymn RPU_D_AIUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_D_AIUTO" FOR "RPU"."RPU_D_AIUTO";
--------------------------------------------------------
--  DDL for Synonymn RPU_D_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_D_LIVELLO" FOR "RPU"."RPU_D_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn RPU_D_STATO_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_D_STATO_PRATICA" FOR "RPU"."RPU_D_STATO_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn RPU_R_LIVELLO_AIUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_R_LIVELLO_AIUTO" FOR "RPU"."RPU_R_LIVELLO_AIUTO";
--------------------------------------------------------
--  DDL for Synonymn RPU_T_FORMA_PAGAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_T_FORMA_PAGAMENTO" FOR "RPU"."RPU_T_FORMA_PAGAMENTO";
--------------------------------------------------------
--  DDL for Synonymn RPU_T_ITER_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_T_ITER_PRATICA" FOR "RPU"."RPU_T_ITER_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn RPU_T_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_T_PRATICA" FOR "RPU"."RPU_T_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn RPU_T_SET_ASIDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_T_SET_ASIDE" FOR "RPU"."RPU_T_SET_ASIDE";
--------------------------------------------------------
--  DDL for Synonymn RPU_V_DATI_CAMPAGNIA_DUP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."RPU_V_DATI_CAMPAGNIA_DUP" FOR "RPU"."RPU_V_DATI_CAMPAGNIA_DUP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ABI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ABI" FOR "SMRGAA"."SEQ_ABI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ACQUA_EXTRA_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ACQUA_EXTRA_10R" FOR "SMRGAA"."SEQ_ACQUA_EXTRA_10R";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ALLEVAMENTI_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ALLEVAMENTI_SIAN" FOR "SMRGAA"."SEQ_ALLEVAMENTI_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ALLEVAMENTO" FOR "SMRGAA"."SEQ_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ALTRO_VITIGNO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ALTRO_VITIGNO" FOR "SMRGAA"."SEQ_ALTRO_VITIGNO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ALTRO_VITIGNO_DICHIARATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ALTRO_VITIGNO_DICHIARATO" FOR "SMRGAA"."SEQ_ALTRO_VITIGNO_DICHIARATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ANAGRAFICA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ANAGRAFICA_AZIENDA" FOR "SMRGAA"."SEQ_ANAGRAFICA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ATTESTAZIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ATTESTAZIONE_AZIENDA" FOR "SMRGAA"."SEQ_ATTESTAZIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ATTESTAZIONE_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ATTESTAZIONE_DICHIARATA" FOR "SMRGAA"."SEQ_ATTESTAZIONE_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AVANZAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AVANZAMENTO" FOR "SMRGAA"."SEQ_AVANZAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA" FOR "SMRGAA"."SEQ_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA_AAEP" FOR "SMRGAA"."SEQ_AZIENDA_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA_ATECO_SEC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA_ATECO_SEC" FOR "SMRGAA"."SEQ_AZIENDA_ATECO_SEC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA_DESTINAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA_DESTINAZIONE" FOR "SMRGAA"."SEQ_AZIENDA_DESTINAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA_TRIBUTARIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA_TRIBUTARIA" FOR "SMRGAA"."SEQ_AZIENDA_TRIBUTARIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_AZIENDA_TRIBUTARIA_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_AZIENDA_TRIBUTARIA_BATCH" FOR "SMRGAA"."SEQ_AZIENDA_TRIBUTARIA_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_BAS_T_CENTRALE_BANDI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_BAS_T_CENTRALE_BANDI" FOR "BAS"."SEQ_BAS_T_CENTRALE_BANDI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CAB
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CAB" FOR "SMRGAA"."SEQ_CAB";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CATEGORIE_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CATEGORIE_ALLEVAMENTO" FOR "SMRGAA"."SEQ_CATEGORIE_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CF_COLLEGATI_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CF_COLLEGATI_SIAN" FOR "SMRGAA"."SEQ_CF_COLLEGATI_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CODICE_FOTOGRAFIA_TERRENI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CODICE_FOTOGRAFIA_TERRENI" FOR "SMRGAA"."SEQ_CODICE_FOTOGRAFIA_TERRENI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_COMUNICAZIONE_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_COMUNICAZIONE_10R" FOR "SMRGAA"."SEQ_COMUNICAZIONE_10R";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONDUZIONE_CONTRATTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONDUZIONE_CONTRATTO" FOR "SMRGAA"."SEQ_CONDUZIONE_CONTRATTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONDUZIONE_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONDUZIONE_DICHIARATA" FOR "SMRGAA"."SEQ_CONDUZIONE_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONDUZIONE_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONDUZIONE_PARTICELLA" FOR "SMRGAA"."SEQ_CONDUZIONE_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONTITOLARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONTITOLARE" FOR "SMRGAA"."SEQ_CONTITOLARE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONTO_CORRENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONTO_CORRENTE" FOR "SMRGAA"."SEQ_CONTO_CORRENTE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONTRATTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONTRATTO" FOR "SMRGAA"."SEQ_CONTRATTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_CONTRATTO_PROPRIETARI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_CONTRATTO_PROPRIETARI" FOR "SMRGAA"."SEQ_CONTRATTO_PROPRIETARI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ACCESSO_PIANO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ACCESSO_PIANO_GRAFICO" FOR "SMRGAA"."SEQ_DB_ACCESSO_PIANO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO" FOR "SMRGAA"."SEQ_DB_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO_DICHIARAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO_DICHIARAZIONE" FOR "SMRGAA"."SEQ_DB_ALLEGATO_DICHIARAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO_DOCUMENTO" FOR "SMRGAA"."SEQ_DB_ALLEGATO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO_INVIO_EMAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO_INVIO_EMAIL" FOR "SMRGAA"."SEQ_DB_ALLEGATO_INVIO_EMAIL";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO_NOTIFICA" FOR "SMRGAA"."SEQ_DB_ALLEGATO_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEGATO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEGATO_RICHIESTA" FOR "SMRGAA"."SEQ_DB_ALLEGATO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG" FOR "SMRGAA"."SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEVAMENTO_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEVAMENTO_BIO" FOR "SMRGAA"."SEQ_DB_ALLEVAMENTO_BIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEVAMENTO_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEVAMENTO_NAZIONALE" FOR "SMRGAA"."SEQ_DB_ALLEVAMENTO_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALLEV_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALLEV_NAZIONALE_BKP" FOR "SMRGAA"."SEQ_DB_ALLEV_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ALTRI_DATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ALTRI_DATI" FOR "SMRGAA"."SEQ_DB_ALTRI_DATI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ANNO_UTILIZ_ISTRUTTORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ANNO_UTILIZ_ISTRUTTORIA" FOR "SMRGAA"."SEQ_DB_ANNO_UTILIZ_ISTRUTTORIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ANOMALIA_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ANOMALIA_POLIZZA" FOR "SMRGAA"."SEQ_DB_ANOMALIA_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ATECO_SEC_TRIBUTARIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ATECO_SEC_TRIBUTARIA" FOR "SMRGAA"."SEQ_DB_ATECO_SEC_TRIBUTARIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_AAEP_ATECO_SEC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_AAEP_ATECO_SEC" FOR "SMRGAA"."SEQ_DB_AZIENDA_AAEP_ATECO_SEC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_AAEP_SEZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_AAEP_SEZIONI" FOR "SMRGAA"."SEQ_DB_AZIENDA_AAEP_SEZIONI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_COLLEGATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_COLLEGATA" FOR "SMRGAA"."SEQ_DB_AZIENDA_COLLEGATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_CONTROLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_CONTROLLO" FOR "SMRGAA"."SEQ_DB_AZIENDA_CONTROLLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_EFA" FOR "SMRGAA"."SEQ_DB_AZIENDA_EFA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_GREENING" FOR "SMRGAA"."SEQ_DB_AZIENDA_GREENING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_GREENING_ESON
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_GREENING_ESON" FOR "SMRGAA"."SEQ_DB_AZIENDA_GREENING_ESON";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_MOT_DICHIARAZ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_MOT_DICHIARAZ" FOR "SMRGAA"."SEQ_DB_AZIENDA_MOT_DICHIARAZ";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_SEZIONI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_SEZIONI" FOR "SMRGAA"."SEQ_DB_AZIENDA_SEZIONI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDA_SEZIONI_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDA_SEZIONI_AAEP" FOR "SMRGAA"."SEQ_DB_AZIENDA_SEZIONI_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_AZIENDE_DOM_GRAFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_AZIENDE_DOM_GRAFICA" FOR "SMRGAA"."SEQ_DB_AZIENDE_DOM_GRAFICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CALC_INEA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CALC_INEA_AZIENDA" FOR "SMRGAA"."SEQ_DB_CALC_INEA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CALC_INEA_UTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CALC_INEA_UTE" FOR "SMRGAA"."SEQ_DB_CALC_INEA_UTE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CAPO_ALLEV_NAZ_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CAPO_ALLEV_NAZ_BKP" FOR "SMRGAA"."SEQ_DB_CAPO_ALLEV_NAZ_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CAPO_ALLEV_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CAPO_ALLEV_NAZIONALE" FOR "SMRGAA"."SEQ_DB_CAPO_ALLEV_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CAUSALE_MOD_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CAUSALE_MOD_DOCUMENTO" FOR "SMRGAA"."SEQ_DB_CAUSALE_MOD_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CCIAA_ALBO_DETTAGLIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CCIAA_ALBO_DETTAGLIO" FOR "SMRGAA"."SEQ_DB_CCIAA_ALBO_DETTAGLIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CCIAA_ALBO_VIGNETI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CCIAA_ALBO_VIGNETI" FOR "SMRGAA"."SEQ_DB_CCIAA_ALBO_VIGNETI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CCIAA_ALBO_VIGNETI_MASS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CCIAA_ALBO_VIGNETI_MASS" FOR "SMRGAA"."SEQ_DB_CCIAA_ALBO_VIGNETI_MASS";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CC_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CC_NAZIONALE" FOR "SMRGAA"."SEQ_DB_CC_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CC_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CC_NAZIONALE_BKP" FOR "SMRGAA"."SEQ_DB_CC_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CLASSIFICAZIONE_VINO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CLASSIFICAZIONE_VINO" FOR "SMRGAA"."SEQ_DB_CLASSIFICAZIONE_VINO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_COLORE_BACCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_COLORE_BACCA" FOR "SMRGAA"."SEQ_DB_COLORE_BACCA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_COMPAGNIA_ASSICURATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_COMPAGNIA_ASSICURATRICE" FOR "SMRGAA"."SEQ_DB_COMPAGNIA_ASSICURATRICE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_COMPENSAZIONE_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_COMPENSAZIONE_AZIENDA" FOR "SMRGAA"."SEQ_DB_COMPENSAZIONE_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_COMPENSAZIONE_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_COMPENSAZIONE_UV" FOR "SMRGAA"."SEQ_DB_COMPENSAZIONE_UV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CONDUZIONE_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CONDUZIONE_ELEGGIBILITA" FOR "SMRGAA"."SEQ_DB_CONDUZIONE_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CONSISTENZA_NAZ_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CONSISTENZA_NAZ_BKP" FOR "SMRGAA"."SEQ_DB_CONSISTENZA_NAZ_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CONSISTENZA_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CONSISTENZA_NAZIONALE" FOR "SMRGAA"."SEQ_DB_CONSISTENZA_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CONSORZIO_DIFESA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CONSORZIO_DIFESA" FOR "SMRGAA"."SEQ_DB_CONSORZIO_DIFESA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_CONTO_CORRENTE_VINCOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_CONTO_CORRENTE_VINCOLO" FOR "SMRGAA"."SEQ_DB_CONTO_CORRENTE_VINCOLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DESTINATARIO_INVIO_MAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DESTINATARIO_INVIO_MAIL" FOR "SMRGAA"."SEQ_DB_DESTINATARIO_INVIO_MAIL";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DETTAGLIO_POLIZZA" FOR "SMRGAA"."SEQ_DB_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DETT_POLIZZA_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DETT_POLIZZA_COLTURA" FOR "SMRGAA"."SEQ_DB_DETT_POLIZZA_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DETT_POLIZZA_STRUTTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DETT_POLIZZA_STRUTTURA" FOR "SMRGAA"."SEQ_DB_DETT_POLIZZA_STRUTTURA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DETT_POLIZZA_ZOOTECNIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DETT_POLIZZA_ZOOTECNIA" FOR "SMRGAA"."SEQ_DB_DETT_POLIZZA_ZOOTECNIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DET_UTIL_PART_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DET_UTIL_PART_NAZIONALE" FOR "SMRGAA"."SEQ_DB_DET_UTIL_PART_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DIRITTO_COMPENSAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DIRITTO_COMPENSAZIONE" FOR "SMRGAA"."SEQ_DB_DIRITTO_COMPENSAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DIRITTO_UV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DIRITTO_UV" FOR "SMRGAA"."SEQ_DB_DIRITTO_UV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_DOCUMENTO_DA_FIRMARE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_DOCUMENTO_DA_FIRMARE" FOR "SMRGAA"."SEQ_DB_DOCUMENTO_DA_FIRMARE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_D_SEZIONE_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_D_SEZIONE_MANUALE" FOR "SMRGAA"."SEQ_DB_D_SEZIONE_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ELEGGIBILITA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ELEGGIBILITA_DICHIARATA" FOR "SMRGAA"."SEQ_DB_ELEGGIBILITA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESITO_CONTROLLO_ALLEV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESITO_CONTROLLO_ALLEV" FOR "SMRGAA"."SEQ_DB_ESITO_CONTROLLO_ALLEV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESITO_CONTROLLO_DOCUMEN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESITO_CONTROLLO_DOCUMEN" FOR "SMRGAA"."SEQ_DB_ESITO_CONTROLLO_DOCUMEN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESITO_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESITO_DETTAGLIO_POLIZZA" FOR "SMRGAA"."SEQ_DB_ESITO_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESITO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESITO_GREENING" FOR "SMRGAA"."SEQ_DB_ESITO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESITO_PASCOLO_MAGRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESITO_PASCOLO_MAGRO" FOR "SMRGAA"."SEQ_DB_ESITO_PASCOLO_MAGRO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ESTENSIONE_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ESTENSIONE_FILE" FOR "SMRGAA"."SEQ_DB_ESTENSIONE_FILE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_FABBRICATO_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_FABBRICATO_BIO" FOR "SMRGAA"."SEQ_DB_FABBRICATO_BIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_FASE_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_FASE_ISTANZA_RIESAME" FOR "SMRGAA"."SEQ_DB_FASE_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_FILE_SCARICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_FILE_SCARICO" FOR "SMRGAA"."SEQ_DB_FILE_SCARICO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_FIRMADICH_FASCICOLO_DOM
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_FIRMADICH_FASCICOLO_DOM" FOR "SMRGAA"."SEQ_DB_FIRMADICH_FASCICOLO_DOM";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_FORMATO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_FORMATO_SCARICO_FILE" FOR "SMRGAA"."SEQ_DB_FORMATO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_GRAFICO_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_GRAFICO_ELEGGIBILITA" FOR "SMRGAA"."SEQ_DB_GRAFICO_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_GRUPPO_DIVERSIFICAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_GRUPPO_DIVERSIFICAZIONE" FOR "SMRGAA"."SEQ_DB_GRUPPO_DIVERSIFICAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_GRUPPO_DIVERS_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_GRUPPO_DIVERS_VARIETA" FOR "SMRGAA"."SEQ_DB_GRUPPO_DIVERS_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_GRUPPO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_GRUPPO_GREENING" FOR "SMRGAA"."SEQ_DB_GRUPPO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_GRUPPO_MACRO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_GRUPPO_MACRO_USO" FOR "SMRGAA"."SEQ_DB_GRUPPO_MACRO_USO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_INVIO_DETTAGLIO_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_INVIO_DETTAGLIO_POLIZZA" FOR "SMRGAA"."SEQ_DB_INVIO_DETTAGLIO_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_INVIO_FASCICOLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_INVIO_FASCICOLI" FOR "SMRGAA"."SEQ_DB_INVIO_FASCICOLI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ISOLA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ISOLA_DICHIARATA" FOR "SMRGAA"."SEQ_DB_ISOLA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ISTANZA_RIESAME" FOR "SMRGAA"."SEQ_DB_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ISTANZA_RIESAME_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ISTANZA_RIESAME_AZIENDA" FOR "SMRGAA"."SEQ_DB_ISTANZA_RIESAME_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_IST_RIESAME_POTENZIALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_IST_RIESAME_POTENZIALE" FOR "SMRGAA"."SEQ_DB_IST_RIESAME_POTENZIALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ISTRUT_POLIZZA_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ISTRUT_POLIZZA_COLTURA" FOR "SMRGAA"."SEQ_DB_ISTRUT_POLIZZA_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ITER_RICHIESTA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ITER_RICHIESTA_AZIENDA" FOR "SMRGAA"."SEQ_DB_ITER_RICHIESTA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ITER_RIESAME_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ITER_RIESAME_AZIENDA" FOR "SMRGAA"."SEQ_DB_ITER_RIESAME_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_MACCHINA" FOR "SMRGAA"."SEQ_DB_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_MACCHINE_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_MACCHINE_AZ_NUOVA" FOR "SMRGAA"."SEQ_DB_MACCHINE_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_MOTIVO_ESCLUSO_PROCEDIM
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_MOTIVO_ESCLUSO_PROCEDIM" FOR "SMRGAA"."SEQ_DB_MOTIVO_ESCLUSO_PROCEDIM";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_MOTIVO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_MOTIVO_RICHIESTA" FOR "SMRGAA"."SEQ_DB_MOTIVO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_NOTIFICA_ENTITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_NOTIFICA_ENTITA" FOR "SMRGAA"."SEQ_DB_NOTIFICA_ENTITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_NUMERO_TARGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_NUMERO_TARGA" FOR "SMRGAA"."SEQ_DB_NUMERO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ORIENTAM_COSMAN_AMMESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ORIENTAM_COSMAN_AMMESSO" FOR "SMRGAA"."SEQ_DB_ORIENTAM_COSMAN_AMMESSO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_ORIENTAM_PROD_COSMAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_ORIENTAM_PROD_COSMAN" FOR "SMRGAA"."SEQ_DB_ORIENTAM_PROD_COSMAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARAMETRO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARAMETRO_SCARICO_FILE" FOR "SMRGAA"."SEQ_DB_PARAMETRO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARCELLA_CONDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARCELLA_CONDUZIONE" FOR "SMRGAA"."SEQ_DB_PARCELLA_CONDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARTICELLA_BIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARTICELLA_BIO" FOR "SMRGAA"."SEQ_DB_PARTICELLA_BIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARTICELLA_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARTICELLA_EFA" FOR "SMRGAA"."SEQ_DB_PARTICELLA_EFA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARTICELLA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARTICELLA_SIGMATER" FOR "SMRGAA"."SEQ_DB_PARTICELLA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PARTICELLA_SOSPESA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PARTICELLA_SOSPESA" FOR "SMRGAA"."SEQ_DB_PARTICELLA_SOSPESA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POL_COLTURA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POL_COLTURA_GARANZIA" FOR "SMRGAA"."SEQ_DB_POL_COLTURA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POLIZZA_ASSICURATIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POLIZZA_ASSICURATIVA" FOR "SMRGAA"."SEQ_DB_POLIZZA_ASSICURATIVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POL_STRUTTURA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POL_STRUTTURA_GARANZIA" FOR "SMRGAA"."SEQ_DB_POL_STRUTTURA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POL_ZOOTECNIA_EPIZOOZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POL_ZOOTECNIA_EPIZOOZIA" FOR "SMRGAA"."SEQ_DB_POL_ZOOTECNIA_EPIZOOZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POL_ZOOTECNIA_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POL_ZOOTECNIA_GARANZIA" FOR "SMRGAA"."SEQ_DB_POL_ZOOTECNIA_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_POSSESSO_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_POSSESSO_MACCHINA" FOR "SMRGAA"."SEQ_DB_POSSESSO_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PROC_CONCORSUALE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PROC_CONCORSUALE_AAEP" FOR "SMRGAA"."SEQ_DB_PROC_CONCORSUALE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_PROPRIETA_CERTIFICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_PROPRIETA_CERTIFICATA" FOR "SMRGAA"."SEQ_DB_PROPRIETA_CERTIFICATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_ATTEST_REP_SUB_REP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_ATTEST_REP_SUB_REP" FOR "SMRGAA"."SEQ_DB_R_ATTEST_REP_SUB_REP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_AZIENDA_INFO_AGGIUNT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_AZIENDA_INFO_AGGIUNT" FOR "SMRGAA"."SEQ_DB_R_AZIENDA_INFO_AGGIUNT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_CATALOGO_COMPATI_ELEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_CATALOGO_COMPATI_ELEG" FOR "SMRGAA"."SEQ_DB_R_CATALOGO_COMPATI_ELEG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_CATALOGO_GRAFICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_CATALOGO_GRAFICO" FOR "SMRGAA"."SEQ_DB_R_CATALOGO_GRAFICO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_CATALOGO_MATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_CATALOGO_MATRICE" FOR "SMRGAA"."SEQ_DB_R_CATALOGO_MATRICE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_CATALOGO_MATRICE_SEMI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_CATALOGO_MATRICE_SEMI" FOR "SMRGAA"."SEQ_DB_R_CATALOGO_MATRICE_SEMI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_REPORT_MOTIVO_DICH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_REPORT_MOTIVO_DICH" FOR "SMRGAA"."SEQ_DB_REPORT_MOTIVO_DICH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RESA_PRODOTTO_LUOGO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RESA_PRODOTTO_LUOGO" FOR "SMRGAA"."SEQ_DB_RESA_PRODOTTO_LUOGO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_FASE_RIESAME_TP_DOC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_FASE_RIESAME_TP_DOC" FOR "SMRGAA"."SEQ_DB_R_FASE_RIESAME_TP_DOC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_FOGLIO_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_FOGLIO_AREA" FOR "SMRGAA"."SEQ_DB_R_FOGLIO_AREA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_GENERE_MACCHINA_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_GENERE_MACCHINA_RUOLO" FOR "SMRGAA"."SEQ_DB_R_GENERE_MACCHINA_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_GRAFICO_MANTENIMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_GRAFICO_MANTENIMENTO" FOR "SMRGAA"."SEQ_DB_R_GRAFICO_MANTENIMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RICHIESTA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RICHIESTA_AZIENDA" FOR "SMRGAA"."SEQ_DB_RICHIESTA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RICHIESTA_AZIENDA_DOC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RICHIESTA_AZIENDA_DOC" FOR "SMRGAA"."SEQ_DB_RICHIESTA_AZIENDA_DOC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RICHIESTA_FIRMA_MASSIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RICHIESTA_FIRMA_MASSIVA" FOR "SMRGAA"."SEQ_DB_RICHIESTA_FIRMA_MASSIVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RICHIESTA_INVIO_EMAIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RICHIESTA_INVIO_EMAIL" FOR "SMRGAA"."SEQ_DB_RICHIESTA_INVIO_EMAIL";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RICHIESTA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RICHIESTA_SIGMATER" FOR "SMRGAA"."SEQ_DB_RICHIESTA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RIDUZIONE_RESA_VIGNETO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RIDUZIONE_RESA_VIGNETO" FOR "SMRGAA"."SEQ_DB_RIDUZIONE_RESA_VIGNETO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_RIEPILOGO_ANNUALE_PROD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_RIEPILOGO_ANNUALE_PROD" FOR "SMRGAA"."SEQ_DB_RIEPILOGO_ANNUALE_PROD";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_MARCA_GENERE_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_MARCA_GENERE_MACCHINA" FOR "SMRGAA"."SEQ_DB_R_MARCA_GENERE_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_MENU_ESPORTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_MENU_ESPORTAZIONE" FOR "SMRGAA"."SEQ_DB_R_MENU_ESPORTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_MENU_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_MENU_REPORT" FOR "SMRGAA"."SEQ_DB_R_MENU_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_MENZIONE_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_MENZIONE_PARTICELLA" FOR "SMRGAA"."SEQ_DB_R_MENZIONE_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_PARTICELLA_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_PARTICELLA_AREA" FOR "SMRGAA"."SEQ_DB_R_PARTICELLA_AREA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_PROFILO_RUOLO_IRIDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_PROFILO_RUOLO_IRIDE" FOR "SMRGAA"."SEQ_DB_R_PROFILO_RUOLO_IRIDE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_REPORT_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_REPORT_SUB_REPORT" FOR "SMRGAA"."SEQ_DB_R_REPORT_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_RUOLO_ESPORTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_RUOLO_ESPORTAZIONE" FOR "SMRGAA"."SEQ_DB_R_RUOLO_ESPORTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_RUOLO_IRIDE2_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_RUOLO_IRIDE2_MANUALE" FOR "SMRGAA"."SEQ_DB_R_RUOLO_IRIDE2_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_RUOLO_IRIDE2_RIEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_RUOLO_IRIDE2_RIEP" FOR "SMRGAA"."SEQ_DB_R_RUOLO_IRIDE2_RIEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_TIPO_DOCUMENTO_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_TIPO_DOCUMENTO_REPORT" FOR "SMRGAA"."SEQ_DB_R_TIPO_DOCUMENTO_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_TIPO_MOTIVO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_TIPO_MOTIVO_RICHIESTA" FOR "SMRGAA"."SEQ_DB_R_TIPO_MOTIVO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_TIPO_NOTIFICA_RUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_TIPO_NOTIFICA_RUOLO" FOR "SMRGAA"."SEQ_DB_R_TIPO_NOTIFICA_RUOLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_R_VITIGNO_CCIAA_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_R_VITIGNO_CCIAA_VARIETA" FOR "SMRGAA"."SEQ_DB_R_VITIGNO_CCIAA_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_SCARICO_LOG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_SCARICO_LOG" FOR "SMRGAA"."SEQ_DB_SCARICO_LOG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_SOGGETTO_SCARICO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_SOGGETTO_SCARICO_FILE" FOR "SMRGAA"."SEQ_DB_SOGGETTO_SCARICO_FILE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_SOGGETTO_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_SOGGETTO_SIGMATER" FOR "SMRGAA"."SEQ_DB_SOGGETTO_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_SOPRALLUOGO_ISTANZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_SOPRALLUOGO_ISTANZA" FOR "SMRGAA"."SEQ_DB_SOPRALLUOGO_ISTANZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_STATO_COLTIVAZIONE_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_STATO_COLTIVAZIONE_UNAR" FOR "SMRGAA"."SEQ_DB_STATO_COLTIVAZIONE_UNAR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_STATO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_STATO_RICHIESTA" FOR "SMRGAA"."SEQ_DB_STATO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TESTO_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TESTO_SUB_REPORT" FOR "SMRGAA"."SEQ_DB_TESTO_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ALIMENTAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ALIMENTAZIONE" FOR "SMRGAA"."SEQ_DB_TIPO_ALIMENTAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ALLEGATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ALLEGATO" FOR "SMRGAA"."SEQ_DB_TIPO_ALLEGATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ANCORAGGIO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ANCORAGGIO_UNAR" FOR "SMRGAA"."SEQ_DB_TIPO_ANCORAGGIO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_AREA_I
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_AREA_I" FOR "SMRGAA"."SEQ_DB_TIPO_AREA_I";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_AREA_L
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_AREA_L" FOR "SMRGAA"."SEQ_DB_TIPO_AREA_L";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_AREA_M
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_AREA_M" FOR "SMRGAA"."SEQ_DB_TIPO_AREA_M";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CATEGORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CATEGORIA" FOR "SMRGAA"."SEQ_DB_TIPO_CATEGORIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CATEGORIA_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CATEGORIA_NOTIFICA" FOR "SMRGAA"."SEQ_DB_TIPO_CATEGORIA_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CAUSA_INVALIDAZ_CC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CAUSA_INVALIDAZ_CC" FOR "SMRGAA"."SEQ_DB_TIPO_CAUSA_INVALIDAZ_CC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CONTRIBUTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CONTRIBUTO" FOR "SMRGAA"."SEQ_DB_TIPO_CONTRIBUTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CONTROLLO_FASE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CONTROLLO_FASE" FOR "SMRGAA"."SEQ_DB_TIPO_CONTROLLO_FASE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_CONTROLLO_PARAM
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_CONTROLLO_PARAM" FOR "SMRGAA"."SEQ_DB_TIPO_CONTROLLO_PARAM";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_DEST_ACQUA_LAVAG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_DEST_ACQUA_LAVAG" FOR "SMRGAA"."SEQ_DB_TIPO_DEST_ACQUA_LAVAG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_DESTINAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_DESTINAZIONE" FOR "SMRGAA"."SEQ_DB_TIPO_DESTINAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_DETTAGLIO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_DETTAGLIO_USO" FOR "SMRGAA"."SEQ_DB_TIPO_DETTAGLIO_USO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_DIRITTO_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_DIRITTO_SIGMATER" FOR "SMRGAA"."SEQ_DB_TIPO_DIRITTO_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_EFA" FOR "SMRGAA"."SEQ_DB_TIPO_EFA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_EFA_TIPO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_EFA_TIPO_VARIETA" FOR "SMRGAA"."SEQ_DB_TIPO_EFA_TIPO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ELEGGIBILITA_RILEV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ELEGGIBILITA_RILEV" FOR "SMRGAA"."SEQ_DB_TIPO_ELEGGIBILITA_RILEV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ENTITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ENTITA" FOR "SMRGAA"."SEQ_DB_TIPO_ENTITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_EPIZOOZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_EPIZOOZIA" FOR "SMRGAA"."SEQ_DB_TIPO_EPIZOOZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ESONERO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ESONERO_GREENING" FOR "SMRGAA"."SEQ_DB_TIPO_ESONERO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ESPORTAZIONE_DATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ESPORTAZIONE_DATI" FOR "SMRGAA"."SEQ_DB_TIPO_ESPORTAZIONE_DATI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FAMIGLIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FAMIGLIA" FOR "SMRGAA"."SEQ_DB_TIPO_FAMIGLIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FILE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FILE" FOR "SMRGAA"."SEQ_DB_TIPO_FILE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FILO_SOSTEGNO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FILO_SOSTEGNO" FOR "SMRGAA"."SEQ_DB_TIPO_FILO_SOSTEGNO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FIRMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FIRMA" FOR "SMRGAA"."SEQ_DB_TIPO_FIRMA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FIRMATARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FIRMATARIO" FOR "SMRGAA"."SEQ_DB_TIPO_FIRMATARIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_FORMA_POSSESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_FORMA_POSSESSO" FOR "SMRGAA"."SEQ_DB_TIPO_FORMA_POSSESSO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GARANZIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GARANZIA" FOR "SMRGAA"."SEQ_DB_TIPO_GARANZIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GENERE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GENERE" FOR "SMRGAA"."SEQ_DB_TIPO_GENERE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GENERE_ISCRIZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GENERE_ISCRIZIONE" FOR "SMRGAA"."SEQ_DB_TIPO_GENERE_ISCRIZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GENERE_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GENERE_MACCHINA" FOR "SMRGAA"."SEQ_DB_TIPO_GENERE_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GREENING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GREENING" FOR "SMRGAA"."SEQ_DB_TIPO_GREENING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GREENING_TIPO_EFA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GREENING_TIPO_EFA" FOR "SMRGAA"."SEQ_DB_TIPO_GREENING_TIPO_EFA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_GREENING_TIPO_ESON
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_GREENING_TIPO_ESON" FOR "SMRGAA"."SEQ_DB_TIPO_GREENING_TIPO_ESON";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_INFO_AGGIUNTIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_INFO_AGGIUNTIVA" FOR "SMRGAA"."SEQ_DB_TIPO_INFO_AGGIUNTIVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_INTERVENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_INTERVENTO" FOR "SMRGAA"."SEQ_DB_TIPO_INTERVENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ISCRIZIONE_INPS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ISCRIZIONE_INPS" FOR "SMRGAA"."SEQ_DB_TIPO_ISCRIZIONE_INPS";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_LIQUIDAZIONE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_LIQUIDAZIONE_AAEP" FOR "SMRGAA"."SEQ_DB_TIPO_LIQUIDAZIONE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_LIVELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_LIVELLO" FOR "SMRGAA"."SEQ_DB_TIPO_LIVELLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_MACCHINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_MACCHINA" FOR "SMRGAA"."SEQ_DB_TIPO_MACCHINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_MARCA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_MARCA" FOR "SMRGAA"."SEQ_DB_TIPO_MARCA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_MENU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_MENU" FOR "SMRGAA"."SEQ_DB_TIPO_MENU";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_MOTIVO_RIDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_MOTIVO_RIDUZIONE" FOR "SMRGAA"."SEQ_DB_TIPO_MOTIVO_RIDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_NAZIONALITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_NAZIONALITA" FOR "SMRGAA"."SEQ_DB_TIPO_NAZIONALITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_PALO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_PALO_UNAR" FOR "SMRGAA"."SEQ_DB_TIPO_PALO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_PERIODO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_PERIODO" FOR "SMRGAA"."SEQ_DB_TIPO_PERIODO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_PERIODO_SEMINA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_PERIODO_SEMINA" FOR "SMRGAA"."SEQ_DB_TIPO_PERIODO_SEMINA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_POTENZIALITA_IRRIG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_POTENZIALITA_IRRIG" FOR "SMRGAA"."SEQ_DB_TIPO_POTENZIALITA_IRRIG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_PRODOTTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_PRODOTTO" FOR "SMRGAA"."SEQ_DB_TIPO_PRODOTTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_PRODUZIONE_COSMAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_PRODUZIONE_COSMAN" FOR "SMRGAA"."SEQ_DB_TIPO_PRODUZIONE_COSMAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_QUALITA_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_QUALITA_USO" FOR "SMRGAA"."SEQ_DB_TIPO_QUALITA_USO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_REPORT" FOR "SMRGAA"."SEQ_DB_TIPO_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_RICHIESTA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_RICHIESTA" FOR "SMRGAA"."SEQ_DB_TIPO_RICHIESTA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_RIEPILOGO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_RIEPILOGO" FOR "SMRGAA"."SEQ_DB_TIPO_RIEPILOGO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_ROTAZIONE_COLT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_ROTAZIONE_COLT" FOR "SMRGAA"."SEQ_DB_TIPO_ROTAZIONE_COLT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_SCARICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_SCARICO" FOR "SMRGAA"."SEQ_DB_TIPO_SCARICO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_SEZIONI_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_SEZIONI_AAEP" FOR "SMRGAA"."SEQ_DB_TIPO_SEZIONI_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_SOSPENSIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_SOSPENSIONE" FOR "SMRGAA"."SEQ_DB_TIPO_SOSPENSIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_SPECIE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_SPECIE" FOR "SMRGAA"."SEQ_DB_TIPO_SPECIE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_SUB_REPORT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_SUB_REPORT" FOR "SMRGAA"."SEQ_DB_TIPO_SUB_REPORT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_TARGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_TARGA" FOR "SMRGAA"."SEQ_DB_TIPO_TARGA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_TERRAZZAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_TERRAZZAMENTO" FOR "SMRGAA"."SEQ_DB_TIPO_TERRAZZAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_TIPOLOGIA_VINO_ODC
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_TIPOLOGIA_VINO_ODC" FOR "SMRGAA"."SEQ_DB_TIPO_TIPOLOGIA_VINO_ODC";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_TIPOLOG_VINO_RICAD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_TIPOLOG_VINO_RICAD" FOR "SMRGAA"."SEQ_DB_TIPO_TIPOLOG_VINO_RICAD";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_TRAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_TRAZIONE" FOR "SMRGAA"."SEQ_DB_TIPO_TRAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_VARIETA_DETT_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_VARIETA_DETT_USO" FOR "SMRGAA"."SEQ_DB_TIPO_VARIETA_DETT_USO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIPO_VITIGNO_CCIAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIPO_VITIGNO_CCIAA" FOR "SMRGAA"."SEQ_DB_TIPO_VITIGNO_CCIAA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TITOLARITA_SIGMATER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TITOLARITA_SIGMATER" FOR "SMRGAA"."SEQ_DB_TITOLARITA_SIGMATER";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TITOL_PARTICELLA_SIG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TITOL_PARTICELLA_SIG" FOR "SMRGAA"."SEQ_DB_TITOL_PARTICELLA_SIG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TIT_POSSESSO_PROCEDIM
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TIT_POSSESSO_PROCEDIM" FOR "SMRGAA"."SEQ_DB_TIT_POSSESSO_PROCEDIM";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_T_MANUALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_T_MANUALE" FOR "SMRGAA"."SEQ_DB_T_MANUALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TMP_INEA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TMP_INEA_AZIENDA" FOR "SMRGAA"."SEQ_DB_TMP_INEA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TMP_INEA_AZIENDA_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TMP_INEA_AZIENDA_DETT" FOR "SMRGAA"."SEQ_DB_TMP_INEA_AZIENDA_DETT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TP_ALLEGATO_TP_FIRMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TP_ALLEGATO_TP_FIRMA" FOR "SMRGAA"."SEQ_DB_TP_ALLEGATO_TP_FIRMA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TP_DICHIARAZ_TP_ALLEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TP_DICHIARAZ_TP_ALLEG" FOR "SMRGAA"."SEQ_DB_TP_DICHIARAZ_TP_ALLEG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TP_DOCUMENTO_TP_ALLEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TP_DOCUMENTO_TP_ALLEG" FOR "SMRGAA"."SEQ_DB_TP_DOCUMENTO_TP_ALLEG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TP_INTERVENTO_VITICOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TP_INTERVENTO_VITICOLO" FOR "SMRGAA"."SEQ_DB_TP_INTERVENTO_VITICOLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_TP_MENZIONE_GEOGRAFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_TP_MENZIONE_GEOGRAFICA" FOR "SMRGAA"."SEQ_DB_TP_MENZIONE_GEOGRAFICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_UNAR_PARCELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_UNAR_PARCELLA" FOR "SMRGAA"."SEQ_DB_UNAR_PARCELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_UNITA_MISURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_UNITA_MISURA" FOR "SMRGAA"."SEQ_DB_UNITA_MISURA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_UTE_ATECO_SECONDARI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_UTE_ATECO_SECONDARI" FOR "SMRGAA"."SEQ_DB_UTE_ATECO_SECONDARI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_UTENTE_IN_CONFLITTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_UTENTE_IN_CONFLITTO" FOR "SMRGAA"."SEQ_DB_UTENTE_IN_CONFLITTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_VARIETA_FONTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_VARIETA_FONTE" FOR "SMRGAA"."SEQ_DB_VARIETA_FONTE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_VIGNA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_VIGNA" FOR "SMRGAA"."SEQ_DB_VIGNA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DB_VIGNA_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DB_VIGNA_PARTICELLA" FOR "SMRGAA"."SEQ_DB_VIGNA_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DELEGA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DELEGA" FOR "SMRGAA"."SEQ_DELEGA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETTAGLIO_ATTIVITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETTAGLIO_ATTIVITA" FOR "SMRGAA"."SEQ_DETTAGLIO_ATTIVITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETTAGLIO_CLASSE_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETTAGLIO_CLASSE_ULU" FOR "SMRGAA"."SEQ_DETTAGLIO_CLASSE_ULU";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETTAGLIO_MANODOPERA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETTAGLIO_MANODOPERA" FOR "SMRGAA"."SEQ_DETTAGLIO_MANODOPERA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETTAGLIO_VARIAZIONE_AZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETTAGLIO_VARIAZIONE_AZI" FOR "SMRGAA"."SEQ_DETTAGLIO_VARIAZIONE_AZI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETT_UTILIZZO_PART_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETT_UTILIZZO_PART_SIAN" FOR "SMRGAA"."SEQ_DETT_UTILIZZO_PART_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DETT_UTILIZZO_PART_SIAN_BK
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DETT_UTILIZZO_PART_SIAN_BK" FOR "SMRGAA"."SEQ_DETT_UTILIZZO_PART_SIAN_BK";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DICHIARAZIONE_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DICHIARAZIONE_CONSISTENZA" FOR "SMRGAA"."SEQ_DICHIARAZIONE_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DICHIARAZIONE_CORREZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DICHIARAZIONE_CORREZIONE" FOR "SMRGAA"."SEQ_DICHIARAZIONE_CORREZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DICHIARAZIONE_SEGNALAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DICHIARAZIONE_SEGNALAZIONE" FOR "SMRGAA"."SEQ_DICHIARAZIONE_SEGNALAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DITTA_UMA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DITTA_UMA" FOR "SMRUMA"."SEQ_DITTA_UMA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOCUMENTO" FOR "SMRGAA"."SEQ_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOCUMENTO_CATEGORIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOCUMENTO_CATEGORIA" FOR "SMRGAA"."SEQ_DOCUMENTO_CATEGORIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOCUMENTO_CONDUZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOCUMENTO_CONDUZIONE" FOR "SMRGAA"."SEQ_DOCUMENTO_CONDUZIONE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOCUMENTO_CORR_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOCUMENTO_CORR_PARTICELLA" FOR "SMRGAA"."SEQ_DOCUMENTO_CORR_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOCUMENTO_PROPRIETARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOCUMENTO_PROPRIETARIO" FOR "SMRGAA"."SEQ_DOCUMENTO_PROPRIETARIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_DOMICILIO_FISC_VARIATO_SIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_DOMICILIO_FISC_VARIATO_SIA" FOR "SMRGAA"."SEQ_DOMICILIO_FISC_VARIATO_SIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_EFFLUENTE_CES_ACQ_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_EFFLUENTE_CES_ACQ_10R" FOR "SMRGAA"."SEQ_EFFLUENTE_CES_ACQ_10R";
--------------------------------------------------------
--  DDL for Synonymn SEQ_EFFLUENTE_PRODOTTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_EFFLUENTE_PRODOTTO" FOR "SMRGAA"."SEQ_EFFLUENTE_PRODOTTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_EFFLUENTE_STOC_EXT_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_EFFLUENTE_STOC_EXT_10R" FOR "SMRGAA"."SEQ_EFFLUENTE_STOC_EXT_10R";
--------------------------------------------------------
--  DDL for Synonymn SEQ_EFFLUENTE_10R
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_EFFLUENTE_10R" FOR "SMRGAA"."SEQ_EFFLUENTE_10R";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ESITO_CONTROLLO_DOCUMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ESITO_CONTROLLO_DOCUMENTO" FOR "SMRGAA"."SEQ_ESITO_CONTROLLO_DOCUMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ESITO_CONTROLLO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ESITO_CONTROLLO_PARTICELLA" FOR "SMRGAA"."SEQ_ESITO_CONTROLLO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ESITO_CONTROLLO_UNAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ESITO_CONTROLLO_UNAR" FOR "SMRGAA"."SEQ_ESITO_CONTROLLO_UNAR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_EVENTO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_EVENTO_PARTICELLA" FOR "SMRGAA"."SEQ_EVENTO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FABBRICATI_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FABBRICATI_NAZIONALE" FOR "SMRGAA"."SEQ_FABBRICATI_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FABBRICATI_NAZIONALE_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FABBRICATI_NAZIONALE_BKP" FOR "SMRGAA"."SEQ_FABBRICATI_NAZIONALE_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FABBRICATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FABBRICATO" FOR "SMRGAA"."SEQ_FABBRICATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FASCICOLI_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FASCICOLI_NAZIONALE" FOR "SMRGAA"."SEQ_FASCICOLI_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FASCICOLI_SANTHIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FASCICOLI_SANTHIA" FOR "SMRGAA"."SEQ_FASCICOLI_SANTHIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FASCICOLO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FASCICOLO_SIAN" FOR "SMRGAA"."SEQ_FASCICOLO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_FOGLIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_FOGLIO" FOR "SMRGAA"."SEQ_FOGLIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_GOSMAR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_GOSMAR" FOR "SMRGAA"."SEQ_GOSMAR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ID_APPEZZAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ID_APPEZZAMENTO" FOR "SMRGAA"."SEQ_DB_TIPO_ELEGGIBILITA_RILEV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ID_COPA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ID_COPA" FOR "SMRGAA"."SEQ_ID_COPA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ID_DEUS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ID_DEUS" FOR "SMRGAA"."SEQ_ID_DEUS";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ID_ISOLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ID_ISOLA" FOR "SMRGAA"."SEQ_ID_ISOLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_IMP_FRULLINO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_IMP_FRULLINO" FOR "SMRGAA"."SEQ_IMP_FRULLINO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_INTERMEDIARIO" FOR "SMRGAA"."SEQ_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ISOLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ISOLA" FOR "SMRGAA"."SEQ_ISOLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ISOLA_ANOMALIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ISOLA_ANOMALIA" FOR "SMRGAA"."SEQ_ISOLA_ANOMALIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_ISOLA_PARCELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_ISOLA_PARCELLA" FOR "SMRGAA"."SEQ_ISOLA_PARCELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_LOG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_LOG" FOR "SMRGAA"."SEQ_LOG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_LUISA_UV2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_LUISA_UV2" FOR "SMRGAA"."SEQ_LUISA_UV2";
--------------------------------------------------------
--  DDL for Synonymn SEQ_MACRO_USO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_MACRO_USO" FOR "SMRGAA"."SEQ_MACRO_USO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_MACRO_USO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_MACRO_USO_VARIETA" FOR "SMRGAA"."SEQ_MACRO_USO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_MANODOPERA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_MANODOPERA" FOR "SMRGAA"."SEQ_MANODOPERA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_NOTIFICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_NOTIFICA" FOR "SMRGAA"."SEQ_NOTIFICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_NUMERO_PROCESSO_JAVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_NUMERO_PROCESSO_JAVA" FOR "SMRGAA"."SEQ_NUMERO_PROCESSO_JAVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_NUMERO_PROTOCOLLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_NUMERO_PROTOCOLLO" FOR "SMRGAA"."SEQ_NUMERO_PROTOCOLLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_NUMERO_PROTOCOLLO_GTFO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_NUMERO_PROTOCOLLO_GTFO" FOR "SMRGAA"."SEQ_NUMERO_PROTOCOLLO_GTFO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_OPERATORE_BIOLOGICO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_OPERATORE_BIOLOGICO" FOR "SMRGAA"."SEQ_OPERATORE_BIOLOGICO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARAMETRI_ATT_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARAMETRI_ATT_AZIENDA" FOR "SMRGAA"."SEQ_PARAMETRI_ATT_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARAMETRI_ATT_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARAMETRI_ATT_DICHIARATA" FOR "SMRGAA"."SEQ_PARAMETRI_ATT_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA" FOR "SMRGAA"."SEQ_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA_CERT_ELEG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA_CERT_ELEG" FOR "SMRGAA"."SEQ_PARTICELLA_CERT_ELEG";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA_CERTIFICATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA_CERTIFICATA" FOR "SMRGAA"."SEQ_PARTICELLA_CERTIFICATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA_FABBRICATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA_FABBRICATO" FOR "SMRGAA"."SEQ_PARTICELLA_FABBRICATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA_POLIGONO_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA_POLIGONO_SIAN" FOR "SMRGAA"."SEQ_PARTICELLA_POLIGONO_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLA_SIAN" FOR "SMRGAA"."SEQ_PARTICELLA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTICELLE_AGEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTICELLE_AGEA" FOR "SMRGAA"."SEQ_PARTICELLE_AGEA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PARTITE_IVA_ATTRIBUITE_SIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PARTITE_IVA_ATTRIBUITE_SIA" FOR "SMRGAA"."SEQ_PARTITE_IVA_ATTRIBUITE_SIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PERSONA_FISICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PERSONA_FISICA" FOR "SMRGAA"."SEQ_PERSONA_FISICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PERSONA_GIURIDICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PERSONA_GIURIDICA" FOR "SMRGAA"."SEQ_PERSONA_GIURIDICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_POLIGONO_UNITA_ARBOREA_SIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_POLIGONO_UNITA_ARBOREA_SIA" FOR "SMRGAA"."SEQ_POLIGONO_UNITA_ARBOREA_SIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PORZIONI_CERT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PORZIONI_CERT" FOR "SMRGAA"."SEQ_PORZIONI_CERT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PROCEDIMENTO_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PROCEDIMENTO_AZIENDA" FOR "SMRGAA"."SEQ_PROCEDIMENTO_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PROP_NAZIONALE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PROP_NAZIONALE_SIAN" FOR "SMRGAA"."SEQ_PROP_NAZIONALE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PROPRIETARIO_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PROPRIETARIO_NAZIONALE" FOR "SMRGAA"."SEQ_PROPRIETARIO_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PROPRIETARIO_NAZIONALE_SIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PROPRIETARIO_NAZIONALE_SIA" FOR "SMRGAA"."SEQ_PROPRIETARIO_NAZIONALE_SIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_PROPRIETARIO_NAZ_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_PROPRIETARIO_NAZ_SIAN_BKP" FOR "SMRGAA"."SEQ_PROPRIETARIO_NAZ_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_RAPPRESENTANTE_LEGALE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_RAPPRESENTANTE_LEGALE_AAEP" FOR "SMRGAA"."SEQ_RAPPRESENTANTE_LEGALE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_RAPPRESENTANTI_SOCIETA_SIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_RAPPRESENTANTI_SOCIETA_SIA" FOR "SMRGAA"."SEQ_RAPPRESENTANTI_SOCIETA_SIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_RESIDENZA_VARIATA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_RESIDENZA_VARIATA_SIAN" FOR "SMRGAA"."SEQ_RESIDENZA_VARIATA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_RESPONSABILE_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_RESPONSABILE_INTERMEDIARIO" FOR "SMRGAA"."SEQ_RESPONSABILE_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_RICHIESTE_INVIO_FASCICOLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_RICHIESTE_INVIO_FASCICOLI" FOR "SMRGAA"."SEQ_RICHIESTE_INVIO_FASCICOLI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SANTHIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SANTHIA" FOR "SMRGAA"."SEQ_SANTHIA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SCHEDA_AGRONOMICA_ABACO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SCHEDA_AGRONOMICA_ABACO" FOR "SMRGAA"."SEQ_SCHEDA_AGRONOMICA_ABACO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SEDE_AAEP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SEDE_AAEP" FOR "SMRGAA"."SEQ_SEDE_AAEP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_D_APPLICAZ_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_D_APPLICAZ_BATCH" FOR "SMRGAA"."SEQ_SMRGAA_D_APPLICAZ_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_L_LOG_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_L_LOG_BATCH" FOR "SMRGAA"."SEQ_SMRGAA_L_LOG_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_R_PARAM_APPLICAZ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_R_PARAM_APPLICAZ" FOR "SMRGAA"."SEQ_SMRGAA_R_PARAM_APPLICAZ";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_R_STORICODATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_R_STORICODATI" FOR "SMRGAA"."SEQ_SMRGAA_R_STORICODATI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_R_VALORE_PARAM_VET
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_R_VALORE_PARAM_VET" FOR "SMRGAA"."SEQ_SMRGAA_R_VALORE_PARAM_VET";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_R_VINCOLI_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_R_VINCOLI_BATCH" FOR "SMRGAA"."SEQ_SMRGAA_R_VINCOLI_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_T_ESECUZIONE_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_T_ESECUZIONE_BATCH" FOR "SMRGAA"."SEQ_SMRGAA_T_ESECUZIONE_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_T_ESECUZ_PARAMETRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_T_ESECUZ_PARAMETRO" FOR "SMRGAA"."SEQ_SMRGAA_T_ESECUZ_PARAMETRO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_ALLEV_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_ALLEV_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_ALLEV_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV" FOR "SMRGAA"."SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_AVVICENDAMENTO_CD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_AVVICENDAMENTO_CD" FOR "SMRGAA"."SEQ_SMRGAA_W_AVVICENDAMENTO_CD";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_AZIENDA_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_AZIENDA_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_AZIENDA_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_CC_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_CC_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_CC_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_ELEGGIBILITA_GIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_ELEGGIBILITA_GIS" FOR "SMRGAA"."SEQ_SMRGAA_W_ELEGGIBILITA_GIS";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_ELEG_GIS_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_ELEG_GIS_DETT" FOR "SMRGAA"."SEQ_SMRGAA_W_ELEG_GIS_DETT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_FABBR_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_FABBR_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_FABBR_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_INVIO_SCHEDE_AGR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_INVIO_SCHEDE_AGR" FOR "SMRGAA"."SEQ_SMRGAA_W_INVIO_SCHEDE_AGR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_JAVA_INSERT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_JAVA_INSERT" FOR "SMRGAA"."SEQ_SMRGAA_W_JAVA_INSERT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_RICHIESTA_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_RICHIESTA_POLIZZA" FOR "SMRGAA"."SEQ_SMRGAA_W_RICHIESTA_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_WRK_DELEGA_CON_PADR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_WRK_DELEGA_CON_PADR" FOR "SMRGAA"."SEQ_SMRGAA_WRK_DELEGA_CON_PADR";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_WRK_LIST_SPORTELLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_WRK_LIST_SPORTELLI" FOR "SMRGAA"."SEQ_SMRGAA_WRK_LIST_SPORTELLI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_UNAR_FAG_INVIATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_UNAR_FAG_INVIATA" FOR "SMRGAA"."SEQ_SMRGAA_W_UNAR_FAG_INVIATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA" FOR "SMRGAA"."SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SOCIETA_RAPPRESENTATE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SOCIETA_RAPPRESENTATE_SIAN" FOR "SMRGAA"."SEQ_SOCIETA_RAPPRESENTATE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SOGGETTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SOGGETTO" FOR "SMRGAA"."SEQ_SOGGETTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SOGGETTO_ASSOCIATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SOGGETTO_ASSOCIATO" FOR "SMRGAA"."SEQ_SOGGETTO_ASSOCIATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SOTTOCATEGORIA_ALLEVAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SOTTOCATEGORIA_ALLEVAMENTO" FOR "SMRGAA"."SEQ_SOTTOCATEGORIA_ALLEVAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_SPORTELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_SPORTELLO" FOR "SMRGAA"."SEQ_SPORTELLO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_STABULAZIONE_TRATTAMENTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_STABULAZIONE_TRATTAMENTO" FOR "SMRGAA"."SEQ_STABULAZIONE_TRATTAMENTO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_STORICO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_STORICO_PARTICELLA" FOR "SMRGAA"."SEQ_STORICO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_STORICO_RESIDENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_STORICO_RESIDENZA" FOR "SMRGAA"."SEQ_STORICO_RESIDENZA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_STORICO_UNITA_ARBOREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_STORICO_UNITA_ARBOREA" FOR "SMRGAA"."SEQ_STORICO_UNITA_ARBOREA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_T_ELAB_MASSIVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_T_ELAB_MASSIVA" FOR "SMRGAA"."SEQ_T_ELAB_MASSIVA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TEMPORANEA_PRATICA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TEMPORANEA_PRATICA" FOR "SMRGAA"."SEQ_TEMPORANEA_PRATICA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TERRITORIALE_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TERRITORIALE_NAZIONALE" FOR "SMRGAA"."SEQ_TERRITORIALE_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TERRITORIALE_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TERRITORIALE_SIAN" FOR "SMRGAA"."SEQ_TERRITORIALE_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TERRITORIALE_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TERRITORIALE_SIAN_BKP" FOR "SMRGAA"."SEQ_TERRITORIALE_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_T_INTERSCAMBIO_SITI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_T_INTERSCAMBIO_SITI" FOR "SMRGAA"."SEQ_T_INTERSCAMBIO_SITI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TIPO_ATTESTAZIONE_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TIPO_ATTESTAZIONE_VARIETA" FOR "SMRGAA"."SEQ_TIPO_ATTESTAZIONE_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TIPO_CLASSE_ULU
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TIPO_CLASSE_ULU" FOR "SMRGAA"."SEQ_TIPO_CLASSE_ULU";
--------------------------------------------------------
--  DDL for Synonymn SEQ_TMP_INEA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_TMP_INEA" FOR "SMRGAA"."SEQ_TMP_INEA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UFFICIO_ZONA_INTERMEDIARIO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UFFICIO_ZONA_INTERMEDIARIO" FOR "SMRGAA"."SEQ_UFFICIO_ZONA_INTERMEDIARIO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UNITA_ARBOREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UNITA_ARBOREA" FOR "SMRGAA"."SEQ_UNITA_ARBOREA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UNITA_ARBOREA_DICHIARATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UNITA_ARBOREA_DICHIARATA" FOR "SMRGAA"."SEQ_UNITA_ARBOREA_DICHIARATA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UNITA_ARBOREA_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UNITA_ARBOREA_SIAN" FOR "SMRGAA"."SEQ_UNITA_ARBOREA_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTE" FOR "SMRGAA"."SEQ_UTE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTE_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTE_NAZIONALE" FOR "SMRGAA"."SEQ_UTE_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTENTE" FOR "SMRGAA"."SEQ_UTENTE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTE_PORTING
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTE_PORTING" FOR "SMRGAA"."SEQ_UTE_PORTING";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_CONS_DICH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_CONS_DICH" FOR "SMRGAA"."SEQ_UTILIZZO_CONS_DICH";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_CONSOCIATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_CONSOCIATO" FOR "SMRGAA"."SEQ_UTILIZZO_CONSOCIATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_DICHIARATO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_DICHIARATO" FOR "SMRGAA"."SEQ_UTILIZZO_DICHIARATO";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_FABB_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_FABB_NAZIONALE" FOR "SMRGAA"."SEQ_UTILIZZO_FABB_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_FABB_NAZIONALE_B
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_FABB_NAZIONALE_B" FOR "SMRGAA"."SEQ_UTILIZZO_FABB_NAZIONALE_B";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_PARTICELLA" FOR "SMRGAA"."SEQ_UTILIZZO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_PART_NAZIONALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_PART_NAZIONALE" FOR "SMRGAA"."SEQ_UTILIZZO_PART_NAZIONALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_PART_SIAN
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_PART_SIAN" FOR "SMRGAA"."SEQ_UTILIZZO_PART_SIAN";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UTILIZZO_PART_SIAN_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UTILIZZO_PART_SIAN_BKP" FOR "SMRGAA"."SEQ_UTILIZZO_PART_SIAN_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UV_NAZIONALE_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UV_NAZIONALE_DETT" FOR "SMRGAA"."SEQ_UV_NAZIONALE_DETT";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UV_NAZIONALE_DETT_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UV_NAZIONALE_DETT_BKP" FOR "SMRGAA"."SEQ_UV_NAZIONALE_DETT_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UV_NAZIONALE_PROV
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UV_NAZIONALE_PROV" FOR "SMRGAA"."SEQ_UV_NAZIONALE_PROV";
--------------------------------------------------------
--  DDL for Synonymn SEQ_UV_NAZIONALE_PROV_BKP
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_UV_NAZIONALE_PROV_BKP" FOR "SMRGAA"."SEQ_UV_NAZIONALE_PROV_BKP";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VARIAZIONE_AZIENDALE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VARIAZIONE_AZIENDALE" FOR "SMRGAA"."SEQ_VARIAZIONE_AZIENDALE";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VARIETA" FOR "SMRGAA"."SEQ_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VARIETA_ELEGGIBILITA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VARIETA_ELEGGIBILITA" FOR "SMRGAA"."SEQ_VARIETA_ELEGGIBILITA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VINIDOC_COMUNI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VINIDOC_COMUNI" FOR "SMRGAA"."SEQ_VINIDOC_COMUNI";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VINO_VARIETA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VINO_VARIETA" FOR "SMRGAA"."SEQ_VINO_VARIETA";
--------------------------------------------------------
--  DDL for Synonymn SEQ_VISIONE_VARIAZIONE_AZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SEQ_VISIONE_VARIAZIONE_AZI" FOR "SMRGAA"."SEQ_VISIONE_VARIAZIONE_AZI";
--------------------------------------------------------
--  DDL for Synonymn SITICODS_ELVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SITICODS_ELVA" FOR "SMRGAA"."SITICODS_ELVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_D_ANAGRAFICA_SERVIZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_D_ANAGRAFICA_SERVIZI" FOR "SMRGAA"."SMRGAA_D_ANAGRAFICA_SERVIZI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_D_APPLICAZIONI_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_D_APPLICAZIONI_BATCH" FOR "SMRGAA"."SMRGAA_D_APPLICAZIONI_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_L_LOG_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_L_LOG_BATCH" FOR "SMRGAA"."SMRGAA_L_LOG_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_R_PARAM_APPLICAZIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_R_PARAM_APPLICAZIONE" FOR "SMRGAA"."SMRGAA_R_PARAM_APPLICAZIONE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_R_STORICODATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_R_STORICODATI" FOR "SMRGAA"."SMRGAA_R_STORICODATI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_R_VALORE_PARAM_VETTORE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_R_VALORE_PARAM_VETTORE" FOR "SMRGAA"."SMRGAA_R_VALORE_PARAM_VETTORE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_R_VINCOLI_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_R_VINCOLI_BATCH" FOR "SMRGAA"."SMRGAA_R_VINCOLI_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_T_ESECUZIONE_BATCH
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_T_ESECUZIONE_BATCH" FOR "SMRGAA"."SMRGAA_T_ESECUZIONE_BATCH";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_T_ESECUZIONE_PARAMETRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_T_ESECUZIONE_PARAMETRO" FOR "SMRGAA"."SMRGAA_T_ESECUZIONE_PARAMETRO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_ALLEVAMENTI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_ALLEVAMENTI" FOR "SMRGAA"."SMRGAA_V_ALLEVAMENTI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_AZIENDA_CAA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_AZIENDA_CAA" FOR "SMRGAA"."SMRGAA_V_AZIENDA_CAA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_BANCA_SPORTELLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_BANCA_SPORTELLO" FOR "SMRGAA"."SMRGAA_V_BANCA_SPORTELLO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_CONDUZIONE_UTILIZZO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_CONDUZIONE_UTILIZZO" FOR "SMRGAA"."SMRGAA_V_CONDUZIONE_UTILIZZO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_CONTO_CORRENTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_CONTO_CORRENTE" FOR "SMRGAA"."SMRGAA_V_CONTO_CORRENTE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_DATI_AMMINISTRATIVI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_DATI_AMMINISTRATIVI" FOR "SMRGAA"."SMRGAA_V_DATI_AMMINISTRATIVI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_DATI_ANAGRAFICI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_DATI_ANAGRAFICI" FOR "SMRGAA"."SMRGAA_V_DATI_ANAGRAFICI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_DECO_ALLEVAMENTI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_DECO_ALLEVAMENTI" FOR "SMRGAA"."SMRGAA_V_DECO_ALLEVAMENTI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_DICH_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_DICH_CONSISTENZA" FOR "SMRGAA"."SMRGAA_V_DICH_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_ELENCO_SOCI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_ELENCO_SOCI" FOR "SMRGAA"."SMRGAA_V_ELENCO_SOCI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_FOGLIO_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_FOGLIO_AREA" FOR "SMRGAA"."SMRGAA_V_FOGLIO_AREA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_INDICATORI_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_INDICATORI_AZIENDA" FOR "SMRGAA"."SMRGAA_V_INDICATORI_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_MACCHINE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_MACCHINE" FOR "SMRGAA"."SMRGAA_V_MACCHINE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_MATRICE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_MATRICE" FOR "SMRGAA"."SMRGAA_V_MATRICE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_PARTICELLA_AREA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_PARTICELLA_AREA" FOR "SMRGAA"."SMRGAA_V_PARTICELLA_AREA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_PARTICELLA_CERTIFICAT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_PARTICELLA_CERTIFICAT" FOR "SMRGAA"."SMRGAA_V_PARTICELLA_CERTIFICAT";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_PRATICHE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_PRATICHE" FOR "SMRGAA"."SMRGAA_V_PRATICHE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_REGISTRO_VINCOLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_REGISTRO_VINCOLI" FOR "SMRGAA"."SMRGAA_V_REGISTRO_VINCOLI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_SOGGETTI_COLLEGATI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_SOGGETTI_COLLEGATI" FOR "SMRGAA"."SMRGAA_V_SOGGETTI_COLLEGATI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_STORICO_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_STORICO_PARTICELLA" FOR "SMRGAA"."SMRGAA_V_STORICO_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_TIPOLOGIA_AZIENDA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_TIPOLOGIA_AZIENDA" FOR "SMRGAA"."SMRGAA_V_TIPOLOGIA_AZIENDA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_UTE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_UTE" FOR "SMRGAA"."SMRGAA_V_UTE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_V_UTENTE_CONFLITTO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_V_UTENTE_CONFLITTO" FOR "SMRGAA"."SMRGAA_V_UTENTE_CONFLITTO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ALLEVAMENTO_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ALLEVAMENTO_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_ALLEVAMENTO_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ASSOCIATE_AZ_NUOVE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ASSOCIATE_AZ_NUOVE" FOR "SMRGAA"."SMRGAA_W_ASSOCIATE_AZ_NUOVE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_AVVICENDAMENTO_COND
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_AVVICENDAMENTO_COND" FOR "SMRGAA"."SMRGAA_W_AVVICENDAMENTO_COND";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_AZIENDA_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_AZIENDA_NUOVA" FOR "SMRGAA"."SMRGAA_W_AZIENDA_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_AZIENDA_SCARICO_XML
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_AZIENDA_SCARICO_XML" FOR "SMRGAA"."SMRGAA_W_AZIENDA_SCARICO_XML";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_AZIENDE_SCHEDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_AZIENDE_SCHEDE" FOR "SMRGAA"."SMRGAA_W_AZIENDE_SCHEDE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_CC_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_CC_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_CC_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ELEGGIBILITA_GIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ELEGGIBILITA_GIS" FOR "SMRGAA"."SMRGAA_W_ELEGGIBILITA_GIS";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ELEGGIBILITA_GIS_DETT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ELEGGIBILITA_GIS_DETT" FOR "SMRGAA"."SMRGAA_W_ELEGGIBILITA_GIS_DETT";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_FABBRICATO_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_FABBRICATO_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_FABBRICATO_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_INVIO_SCHEDE_AGR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_INVIO_SCHEDE_AGR" FOR "SMRGAA"."SMRGAA_W_INVIO_SCHEDE_AGR";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ISOLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ISOLA" FOR "SMRGAA"."SMRGAA_W_ISOLA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ISOLA_ANOMALIA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ISOLA_ANOMALIA" FOR "SMRGAA"."SMRGAA_W_ISOLA_ANOMALIA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ISTANZA_RIESAME" FOR "SMRGAA"."SMRGAA_W_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_ISTANZE_LAVORATE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_ISTANZE_LAVORATE" FOR "SMRGAA"."SMRGAA_W_ISTANZE_LAVORATE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_JAVA_INSERT
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_JAVA_INSERT" FOR "SMRGAA"."SMRGAA_W_JAVA_INSERT";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARCELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARCELLA" FOR "SMRGAA"."SMRGAA_W_PARCELLA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARCELLA_PARTICELLA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARCELLA_PARTICELLA" FOR "SMRGAA"."SMRGAA_W_PARCELLA_PARTICELLA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PART_FABBR_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PART_FABBR_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_PART_FABBR_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARTICELLA_CAMPIONE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARTICELLA_CAMPIONE" FOR "SMRGAA"."SMRGAA_W_PARTICELLA_CAMPIONE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARTICELLA_REGISTRO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARTICELLA_REGISTRO" FOR "SMRGAA"."SMRGAA_W_PARTICELLA_REGISTRO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARTICELLA_SOSPESA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARTICELLA_SOSPESA" FOR "SMRGAA"."SMRGAA_W_PARTICELLA_SOSPESA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PARTICELLE_SCHEDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PARTICELLE_SCHEDE" FOR "SMRGAA"."SMRGAA_W_PARTICELLE_SCHEDE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_PART_ISTANZA_RIESAME
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_PART_ISTANZA_RIESAME" FOR "SMRGAA"."SMRGAA_W_PART_ISTANZA_RIESAME";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_RICHIESTA_POLIZZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_RICHIESTA_POLIZZA" FOR "SMRGAA"."SMRGAA_W_RICHIESTA_POLIZZA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_RIEP_PRODOTTO_ANNO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_RIEP_PRODOTTO_ANNO" FOR "SMRGAA"."SMRGAA_W_RIEP_PRODOTTO_ANNO";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_COD_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_COD_COLTURA" FOR "SMRGAA"."SMRGAA_WRK_COD_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_DELEGA_CON_PADRI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_DELEGA_CON_PADRI" FOR "SMRGAA"."SMRGAA_WRK_DELEGA_CON_PADRI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_LIST_SPORTELLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_LIST_SPORTELLI" FOR "SMRGAA"."SMRGAA_WRK_LIST_SPORTELLI";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGRAZIONE_H_B2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGRAZIONE_H_B2" FOR "SMRGAA"."SMRGAA_WRK_MIGRAZIONE_H_B2";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGRAZIONE_H_MR
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGRAZIONE_H_MR" FOR "SMRGAA"."SMRGAA_WRK_MIGRAZIONE_H_MR";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGRAZIONE_H_P2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGRAZIONE_H_P2" FOR "SMRGAA"."SMRGAA_WRK_MIGRAZIONE_H_P2";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGRAZIONE_H_RATE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGRAZIONE_H_RATE" FOR "SMRGAA"."SMRGAA_WRK_MIGRAZIONE_H_RATE";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGR_H_TIPO_COLTURA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGR_H_TIPO_COLTURA" FOR "SMRGAA"."SMRGAA_WRK_MIGR_H_TIPO_COLTURA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_WRK_MIGR_H_TP_COLT_UTIL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_WRK_MIGR_H_TP_COLT_UTIL" FOR "SMRGAA"."SMRGAA_WRK_MIGR_H_TP_COLT_UTIL";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_SOGG_ASS_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_SOGG_ASS_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_SOGG_ASS_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_SOGGETTI_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_SOGGETTI_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_SOGGETTI_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_UNAR_FAG_INVIATA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_UNAR_FAG_INVIATA" FOR "SMRGAA"."SMRGAA_W_UNAR_FAG_INVIATA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_UTE_AZIENDA_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_UTE_AZIENDA_NUOVA" FOR "SMRGAA"."SMRGAA_W_UTE_AZIENDA_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SMRGAA_W_UTILIZZO_AZ_NUOVA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SMRGAA_W_UTILIZZO_AZ_NUOVA" FOR "SMRGAA"."SMRGAA_W_UTILIZZO_AZ_NUOVA";
--------------------------------------------------------
--  DDL for Synonymn SUOLIGISTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SUOLIGISTYPE" FOR "SMRGAA"."SUOLIGISTYPE";
--------------------------------------------------------
--  DDL for Synonymn SUPERFICI_UTILIZZI_GIS
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SUPERFICI_UTILIZZI_GIS" FOR "SMRGAA"."SUPERFICI_UTILIZZI_GIS";
--------------------------------------------------------
--  DDL for Synonymn SUPERFICI_UTILIZZI_GIS_TAB
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."SUPERFICI_UTILIZZI_GIS_TAB" FOR "SMRGAA"."SUPERFICI_UTILIZZI_GIS_TAB";
--------------------------------------------------------
--  DDL for Synonymn TABCATAVARITYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABCATAVARITYPE" FOR "SMRGAA"."TABCATAVARITYPE";
--------------------------------------------------------
--  DDL for Synonymn TABCOPAPOLITYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABCOPAPOLITYPE_AG" FOR "SMRGAA"."TABCOPAPOLITYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn TABDATISOGGTYPE_WP2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABDATISOGGTYPE_WP2" FOR "SMRGAA"."TABDATISOGGTYPE_WP2";
--------------------------------------------------------
--  DDL for Synonymn TABDEUSPOLITYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABDEUSPOLITYPE_AG" FOR "SMRGAA"."TABDEUSPOLITYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn TABDEUSPOLITYPE_WP2
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABDEUSPOLITYPE_WP2" FOR "SMRGAA"."TABDEUSPOLITYPE_WP2";
--------------------------------------------------------
--  DDL for Synonymn TABMATB1CATATYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABMATB1CATATYPE" FOR "SMRGAA"."TABMATB1CATATYPE";
--------------------------------------------------------
--  DDL for Synonymn TABPARTICELLEGISTYPE_AG
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABPARTICELLEGISTYPE_AG" FOR "SMRGAA"."TABPARTICELLEGISTYPE_AG";
--------------------------------------------------------
--  DDL for Synonymn TABPRATMANTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABPRATMANTYPE" FOR "SMRGAA"."TABPRATMANTYPE";
--------------------------------------------------------
--  DDL for Synonymn TABSUOLIGISTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABSUOLIGISTYPE" FOR "SMRGAA"."TABSUOLIGISTYPE";
--------------------------------------------------------
--  DDL for Synonymn TABUTILTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TABUTILTYPE" FOR "SMRGAA"."TABUTILTYPE";
--------------------------------------------------------
--  DDL for Synonymn TBLSITIPART
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TBLSITIPART" FOR "SMRGAA"."TBLSITIPART";
--------------------------------------------------------
--  DDL for Synonymn TBLSITISUOLO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TBLSITISUOLO" FOR "SMRGAA"."TBLSITISUOLO";
--------------------------------------------------------
--  DDL for Synonymn TIPOLOGIA_PROFILO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TIPOLOGIA_PROFILO" FOR "SMRGAA"."TIPOLOGIA_PROFILO";
--------------------------------------------------------
--  DDL for Synonymn TMP_DITTE_INESISTENTI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TMP_DITTE_INESISTENTI" FOR "SMRUMA"."TMP_DITTE_INESISTENTI";
--------------------------------------------------------
--  DDL for Synonymn TO_UPPER
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TO_UPPER" FOR "SMRGAA"."TO_UPPER";
--------------------------------------------------------
--  DDL for Synonymn TYPCOLL_CUAA_AZ
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TYPCOLL_CUAA_AZ" FOR "SMRGAA"."TYPCOLL_CUAA_AZ";
--------------------------------------------------------
--  DDL for Synonymn TYPCOLL_CUAA_REL
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."TYPCOLL_CUAA_REL" FOR "SMRGAA"."TYPCOLL_CUAA_REL";
--------------------------------------------------------
--  DDL for Synonymn UTENTE_IRIDE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."UTENTE_IRIDE" FOR "SMRGAA"."UTENTE_IRIDE";
--------------------------------------------------------
--  DDL for Synonymn UTENTE_IRIDE_ACCESSO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."UTENTE_IRIDE_ACCESSO" FOR "SMRGAA"."UTENTE_IRIDE_ACCESSO";
--------------------------------------------------------
--  DDL for Synonymn UTENTE_IRIDE_ACCESSO_OLD
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."UTENTE_IRIDE_ACCESSO_OLD" FOR "SMRGAA"."UTENTE_IRIDE_ACCESSO_OLD";
--------------------------------------------------------
--  DDL for Synonymn UTILTYPE
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."UTILTYPE" FOR "SMRGAA"."UTILTYPE";
--------------------------------------------------------
--  DDL for Synonymn V_AGGIORNAMENTO_CONSISTENZA
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."V_AGGIORNAMENTO_CONSISTENZA" FOR "SMRGAA"."V_AGGIORNAMENTO_CONSISTENZA";
--------------------------------------------------------
--  DDL for Synonymn V_PARTICELLE_ELENCO
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."V_PARTICELLE_ELENCO" FOR "SMRGAA"."V_PARTICELLE_ELENCO";
--------------------------------------------------------
--  DDL for Synonymn V_PARTICELLE_UTILIZZI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."V_PARTICELLE_UTILIZZI" FOR "SMRGAA"."V_PARTICELLE_UTILIZZI";
--------------------------------------------------------
--  DDL for Synonymn WRK_IACS_DATI_TABELLA_T3
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."WRK_IACS_DATI_TABELLA_T3" FOR "GNPS_RW"."WRK_IACS_DATI_TABELLA_T3";
--------------------------------------------------------
--  DDL for Synonymn WRK_REGISTRO_STORICO_PASCOLI
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "SMRGAA_RW"."WRK_REGISTRO_STORICO_PASCOLI" FOR "RPU_RW"."WRK_REGISTRO_STORICO_PASCOLI";
--------------------------------------------------------
--  Constraints for Table WRK_SITICOMU
--------------------------------------------------------

  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("ID_COMUNE" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("DATA_AGGIORNAMENTO" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("SEZIONE_CENSUARIA" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("CODI_FISC_LUNA" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("ISTATR" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("SIGLA_PROV" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("DENO_PROV" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("NOME" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("ISTATC" NOT NULL ENABLE);
  ALTER TABLE "SMRGAA_RW"."WRK_SITICOMU" MODIFY ("ISTATP" NOT NULL ENABLE);
