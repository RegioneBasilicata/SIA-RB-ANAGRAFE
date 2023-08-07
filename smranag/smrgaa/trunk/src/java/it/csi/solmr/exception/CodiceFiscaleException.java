package it.csi.solmr.exception;

public class CodiceFiscaleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2701479977835495637L;
	private boolean nome;
	private boolean cognome;
	private boolean annoNascita;
	private boolean meseNascita;
	private boolean giornoNascita;
	private boolean comuneNascita;
	private boolean carattereControllo;
	private boolean sesso;

	public CodiceFiscaleException(String message) {
		super(message);
	}

	public CodiceFiscaleException() {
		super();
	}

	public void setNome(boolean nome) {
		this.nome = nome;
	}

	public void setCognome(boolean cognome) {
		this.cognome = cognome;
	}

	public void setAnnoNascita(boolean annoNascita) {
		this.annoNascita = annoNascita;
	}

	public void setMeseNascita(boolean meseNascita) {
		this.meseNascita = meseNascita;
	}

	public void setGiornoNascita(boolean giornoNascita) {
		this.giornoNascita = giornoNascita;
	}

	public void setComuneNascita(boolean comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public void setCarattereControllo(boolean carattereControllo) {
		this.carattereControllo = carattereControllo;
	}

	public void setSesso(boolean sesso) {
		this.sesso = sesso;
	}




	public boolean getNome() {
		return nome;
	}

	public boolean getCognome() {
		return cognome;
	}

	public boolean getAnnoNascita() {
		return annoNascita;
	}

	public boolean getMeseNascita() {
		return meseNascita;
	}

	public boolean getGiornoNascita() {
		return giornoNascita;
	}

	public boolean getComuneNascita() {
		return comuneNascita;
	}

	public boolean getCarattereControllo() {
		return carattereControllo;
	}

	public boolean getSesso() {
		return sesso;
	}

	public void setErrore(int indice) {
		switch(indice) {
		case 0:
		case 1:
		case 2:
			setCognome(true);
			break;
		case 3:
		case 4:
		case 5:
			setNome(true);
			break;
		case 6:
		case 7:
			setAnnoNascita(true);
			break;
		case 8:
			setMeseNascita(true);
			break;
		case 9:
		case 10:
			setGiornoNascita(true);
			break;
		case 11:
		case 12:
		case 13:
		case 14:
			setComuneNascita(true);
			break;
		case 15:
			setCarattereControllo(true);
			break;
		}
	}

	public boolean getPresenzaErrori() {
		return nome || cognome || annoNascita || meseNascita || giornoNascita
		|| comuneNascita || carattereControllo || sesso;
	}

}