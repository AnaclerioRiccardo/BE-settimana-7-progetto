package it.business;

import java.util.List;

import it.data.Contatto;
import it.data.NumTelefono;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 * Session Bean implementation class ContattoEJB
 */
@Stateless
@LocalBean
public class ContattoEJB implements ContattoEJBLocal {

	//Attributi
	@PersistenceContext(unitName = "rubricaPS")
	private EntityManager em;
	
    public ContattoEJB() {
        // TODO Auto-generated constructor stub
    }
    
    //Metodi
    //ritorna tutti i contatti con rispettivi numeri di telefono che ci sono in rubrica
    public List<Object[]> getContattiNumeri(){
    	Query q = em.createNamedQuery("getContattiNumeri");
    	return q.getResultList();
    }
    
    //Inserisce un contatto nel db
    public void inserisci(Contatto c) {
    	em.persist(c);
    }
    
    //ritorna tutti i contatti con un parametro che e' contenuto in una parte di cognome
    public List<Object[]> getContattiByCognome(String cognome){
    	Query q = em.createNamedQuery("getContattiNumeri.ByCognome");
    	q.setParameter("cognome", "%"+cognome+"%");
    	return q.getResultList();
    }
    
    //Ritorna un contatto passando il numero di telefono
    public List<Object[]> getContattoByNumero(String numero) {
    	Query q = em.createNamedQuery("getContattiNumeri.ByNumero");
    	q.setParameter("numero", numero);
    	return q.getResultList();	
    }
    
    //Ritorna un contatto passando l'id
    public Contatto getContattoById(Long id) {
    	Contatto c = em.find(Contatto.class, id);
    	return c;
    }
    
    //Elimina un contatto con id passato come parametro
    public void eliminaContatto(Long id) {
    	Contatto c = getContattoById(id);
    	em.remove(c);
    }
    
    //Modifica un contatto
    public void modificaNumeroContatto(Contatto c) {
    	Contatto contatto = em.find(Contatto.class, c.getId());
    	for(NumTelefono num : contatto.getNumTelefoni()) {
    		em.remove(em.merge(num));
    	}
    	em.merge(c);
    }
    
    //Ritorna tutti i numeri di telefono
    public List<NumTelefono> getNumeri(){
    	Query q = em.createNamedQuery("getNumeri");
    	return q.getResultList();
    }
    
    //Ritorna l'oggetto NumTelefono passandogli la chiave
    //lo usero per controllare se un numero di telefono esiste gia'
    public NumTelefono getNumTelefono(String numero) {
    	return em.find(NumTelefono.class, numero);
    }

}
