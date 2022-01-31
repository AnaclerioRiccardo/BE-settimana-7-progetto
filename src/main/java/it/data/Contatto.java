package it.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

//Query
@NamedQuery(name="getContattiNumeri", query="SELECT c.nome, c.cognome, c.email, n.numTelefono FROM Contatto c JOIN c.numTelefoni n")
@NamedQuery(name="getContattiNumeri.ByCognome", query="SELECT c.nome, c.cognome, c.email, n.numTelefono FROM Contatto c JOIN c.numTelefoni n WHERE c.cognome LIKE :cognome")
@NamedQuery(name="getContattiNumeri.ByNumero", query="SELECT c.nome, c.cognome, c.email, n.numTelefono FROM Contatto c JOIN c.numTelefoni n WHERE n.numTelefono = :numero")

@Entity
@Table(name="contatto")
public class Contatto implements Serializable {
	
	//Attributi
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String cognome;
	private String email;
	private List<NumTelefono> numTelefoni;
	
	//Getter e Setter
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO) //genero l'id in automatico
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="nome")
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Column(name="cognome")
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(mappedBy ="contatto", cascade = CascadeType.ALL)
	public List<NumTelefono> getNumTelefoni() {
		if(this.numTelefoni==null) {
			numTelefoni = new ArrayList<>();
		}
		return numTelefoni;
	}
	public void setNumTelefoni(List<NumTelefono> numTelefoni) {
	
		this.numTelefoni = numTelefoni;
	}
	
	//metodi
	//Aggiungo un numero di telefono alla lista e setto questo conttatto a quel numero di telefono
	public void aggiungiNumero(NumTelefono num) {
		getNumTelefoni().add(num);
		num.setContatto(this);
	}

}
