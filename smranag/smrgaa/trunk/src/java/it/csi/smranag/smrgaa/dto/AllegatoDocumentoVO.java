package it.csi.smranag.smrgaa.dto;

import java.util.Date;

import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.ValueObject;



public class AllegatoDocumentoVO  implements ValueObject 
{
  
  
	
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 6377157223614628366L;
  
  
  
  private Long  idAllegato;
  private Long  idDocumento;  
	private String nomeLogico;
	private String nomeFisico;
	private byte[] fileAllegato;
	private Date dataUltimoAggiornamento;
	private Date dataRagistrazione;
	private Long idUtenteAggiornamento;
	private Long extIdDocumentoIndex;
	private Long idTipoAllegato;
	private Long idTipoFirma;

  public ValidationErrors validate(long maxFileSize)
  {
  	ValidationErrors errors = new ValidationErrors();
  	
    
    if(Validator.isEmpty(nomeLogico))
    {
      errors.add("nomeLogico", new ValidationError("Campo obbligatorio"));
    }
    if (Validator.isNotEmpty(nomeLogico) && nomeLogico.length() > 250)
    {
      errors.add("nomeLogico", new ValidationError(AnagErrors.ERRORE_LUNGHEZZA_MAX_CARATTERI + " : " + 250));
    }
    
    if(Validator.isEmpty(nomeFisico))
    {
      errors.add("fileAllegato", new ValidationError("Campo obbligatorio"));
    }
    if (Validator.isNotEmpty(nomeFisico) && nomeFisico.length() > 250)
    {
      errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_LUNGHEZZA_MAX_CARATTERI + " : " + 250));
    }
    if (Validator.isNotEmpty(nomeFisico))
    {
      int i2 = nomeFisico.lastIndexOf ('.');
      if(i2 == -1)
      {
        errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_FILE_NON_CORRETTO));
      }
    }
    
    if(fileAllegato == null || fileAllegato.length == 0)
    {
      errors.add("fileAllegato", new ValidationError("Campo obbligatorio"));
    }
    if (Validator.isNotEmpty(fileAllegato) && fileAllegato.length > maxFileSize *1024*1024)
    {
      errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_DIMENSIONE_MAX_FILE + " : " + maxFileSize));
    }
    
    return errors;
  }
  
  
  public ValidationErrors validateNuovaIscrizione(long maxFileSize)
  {
    ValidationErrors errors = new ValidationErrors();
    
    
    if(Validator.isEmpty(nomeFisico))
    {
      errors.add("fileAllegato", new ValidationError("Campo obbligatorio"));
    }
    if (Validator.isNotEmpty(nomeFisico) && nomeFisico.length() > 250)
    {
      errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_LUNGHEZZA_MAX_CARATTERI + " : " + 250));
    }
    
    if(fileAllegato == null || fileAllegato.length == 0)
    {
      errors.add("fileAllegato", new ValidationError("Campo obbligatorio"));
    }
    if (Validator.isNotEmpty(fileAllegato) && fileAllegato.length > maxFileSize *1024*1024)
    {
      errors.add("fileAllegato", new ValidationError(AnagErrors.ERRORE_DIMENSIONE_MAX_FILE + " : " + maxFileSize));
    }
    
    return errors;
  }


	public String getNomeLogico() {
		return nomeLogico;
	}

	public void setNomeLogico(String nomeLogico) {
		this.nomeLogico = nomeLogico;
	}

	public String getNomeFisico() {
		return nomeFisico;
	}

	public void setNomeFisico(String nomeFisico) {
		this.nomeFisico = nomeFisico;
	}

  public byte[] getFileAllegato()
  { 
  	return fileAllegato; 
  }
  
  public void setFileAllegato(byte[] fileAllegato)
  { 
  	this.fileAllegato = fileAllegato; 
  }

  public Long getIdAllegato()
  {
    return idAllegato;
  }

  public void setIdAllegato(Long idAllegato)
  {
    this.idAllegato = idAllegato;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public Date getDataRagistrazione()
  {
    return dataRagistrazione;
  }

  public void setDataRagistrazione(Date dataRagistrazione)
  {
    this.dataRagistrazione = dataRagistrazione;
  }

  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Long getIdDocumento()
  {
    return idDocumento;
  }

  public void setIdDocumento(Long idDocumento)
  {
    this.idDocumento = idDocumento;
  }


  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }


  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }


  public Long getIdTipoAllegato()
  {
    return idTipoAllegato;
  }


  public void setIdTipoAllegato(Long idTipoAllegato)
  {
    this.idTipoAllegato = idTipoAllegato;
  }


  public Long getIdTipoFirma()
  {
    return idTipoFirma;
  }


  public void setIdTipoFirma(Long idTipoFirma)
  {
    this.idTipoFirma = idTipoFirma;
  }
  
  

}