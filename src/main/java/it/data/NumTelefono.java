package it.data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

//Query
@NamedQuery(name="getNumeri", query="SELECT n FROM NumTelefono n")

@Entity
@Table(name="numtelefono")
public class NumTelefono implements Serializable{
	
	//Attributi
	private static final long serialVersionUID = 1L;
	
	private String numTelefono;
	private Contatto contatto;

	//Getter e Setter
	@Id
	@Column(name="numtelefono")
	public String getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	
	@ManyToOne
	@JoinColumn(name="id")
	public Contatto getContatto() {
		return contatto;
	}

	public void setContatto(Contatto contatto) {
		this.contatto = contatto;
	}
	
	@Override
	public String toString() {
		String s = getNumTelefono()+" ";
		return s;
	}

}
