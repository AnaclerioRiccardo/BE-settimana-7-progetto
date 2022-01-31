package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.business.ContattoEJB;
import it.data.Contatto;
import it.data.NumTelefono;

/**
 * Servlet implementation class DispatcherServlet
 */
@WebServlet("/esegui/*")
public class DispatcherServlet extends HttpServlet {
	
	//Attributi
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ContattoEJB cEjb;
       
   //Costruttore
    public DispatcherServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String azione = request.getPathInfo();	//lo usero' per sapere da dove arrivo e quindi che operazione fare
		boolean successo = true;
		String messaggio = "Operazione andata a buon fine";
		//Visualizza tutti i contatti
		if(azione.equals("/visualizza")) {
			List<Object[]> contatti = cEjb.getContattiNumeri();
			request.setAttribute("contatti", contatti);
			request.getServletContext().getRequestDispatcher("/WEB-INF/templates/listaContatti.jsp").forward(request, response);
			
		//Inserisci nuovo contatto
		} else if(azione.equals("/inserisci")) {
			messaggio="Inserimento avvenuto con successo";
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String email = request.getParameter("email");
			String num1 = request.getParameter("numero1");
			String num2 = request.getParameter("numero2");
			//controllo che i numeri di telefono siano effettivamente composti da cifre
			if(!num1.isBlank()) {													//se il primo numero e' stato inserito
				if(!(Character.isDigit(num1.charAt(0)) || num1.charAt(0)=='+')){	//allora controllo che il primo carattere del numero sia effettivamente una cifra o il segno +
					successo=false;
					messaggio="Il primo carattere del primo numero di telefono contiene caratteri non corretti";
				}
				for(int i =1; i<num1.length();i++) {								//controllo che il resto del numero sia composto da cifre
					if(!Character.isDigit(num1.charAt(i))) {
						successo=false;
						messaggio="Il primo numero di telefono contiene caratteri non corretti";
					}
				}
			}
			if(!num2.isBlank()) {
				if(!(Character.isDigit(num2.charAt(0)) || num2.charAt(0)=='+')){
					successo=false;
					messaggio="Il primo carattere del secondo numero di telefono contiene caratteri non corretti";
				}
				for(int i =1; i<num2.length();i++) {
					if(!Character.isDigit(num2.charAt(i))) {
						successo=false;
						messaggio="Il secondo numero di telefono contiene caratteri non corretti";
					}
				}
			}
			
			if(cognome.isBlank()) {	//controllo che il cognome sia stato inserito
				successo=false;
				messaggio="cognome non inserito";
			} else if(num1.isBlank() && num2.isBlank()) {	//controllo che almeno un numero di telefono sia stato inserito
				successo=false;
				messaggio="numero di telefono non inserito";
			} else {
				if(!num1.isBlank()) {
					NumTelefono n1 = cEjb.getNumTelefono(num1);			
					if(n1!=null && n1.getNumTelefono().equals(num1)) {	//se la prima condizione e' falsa non controlla nemmeno la seconda. controllo che il primo numero di telefono non esista gia'
						successo=false;
						messaggio="Il primo numero di telefono esiste gia'";
					}
				}
				if(!num2.isBlank()) {
					NumTelefono n2 = cEjb.getNumTelefono(num2);
					if(n2!= null && n2.getNumTelefono().equals(num2)) {				//controllo che il secondo numero di telefono non esista gia'
						successo=false;
						messaggio="Il secondo numero di telefono esiste gia'";
					}
				}
				if(num1.equals(num2)) {		//controllo che l'utente non abbia inserito due numeri uguali
					successo=false;
					messaggio="Il primo numero di telefono e' uguale al secondo";
				}
			}
			if(successo) {
				Contatto c = new Contatto();
				c.setCognome(cognome);
				c.setNome(nome);
				c.setEmail(email);
				if(!num1.isBlank()) {
					NumTelefono n1 = new NumTelefono();
					n1.setNumTelefono(num1);
					c.aggiungiNumero(n1);
				}
				if(!num2.isBlank()) {
					NumTelefono n2 = new NumTelefono();
					n2.setNumTelefono(num2);
					c.aggiungiNumero(n2);
				}
				cEjb.inserisci(c);
			}
		
			request.setAttribute("successo", successo);
			request.setAttribute("messaggio", messaggio);
			request.getServletContext().getRequestDispatcher("/WEB-INF/templates/risultato.jsp").forward(request, response);
			
		//Cerca per cognome
		} else if(azione.equals("/cerca/cognome")) {
			String cognome = request.getParameter("cognome");
			if(cognome.isBlank()) {
				successo=false;
				messaggio="cognome non inserito";
				request.setAttribute("successo", successo);
				request.setAttribute("messaggio", messaggio);
				request.getServletContext().getRequestDispatcher("/WEB-INF/templates/risultato.jsp").forward(request, response);
			} else {
				List<Object[]> contatti = cEjb.getContattiByCognome(cognome);
				request.setAttribute("contatti", contatti);
				request.getServletContext().getRequestDispatcher("/WEB-INF/templates/listaContatti.jsp").forward(request, response);
			} 
			
		//Cerca per nuero di telefono
		}else if(azione.equals("/cerca/numero")) {
			String num = request.getParameter("numero");
			if(num.isBlank()) {
				successo=false;
				messaggio="Numero di telefono non inserito";
			} else {
				if(!(Character.isDigit(num.charAt(0)) || num.charAt(0)=='+')){	//controllo che il primo carattere del numero sia effittivamente una cifra o il + 
					successo=false;
					messaggio="Il primo carattere del primo numero di telefono contiene caratteri non corretti";
				}
				for(int i =1; i<num.length();i++) {		//controllo che il resto  del numero sia effettivamente composto da cifre
					if(!Character.isDigit(num.charAt(i))) {
						successo=false;
						messaggio="Il primo numero di telefono contiene caratteri non corretti";
					}
				}
			}
			if(successo) {
				List<Object[]> contatti = cEjb.getContattoByNumero(num);
				request.setAttribute("contatti", contatti);
				request.getServletContext().getRequestDispatcher("/WEB-INF/templates/listaContatti.jsp").forward(request, response);
			} else {
				request.setAttribute("successo", successo);
				request.setAttribute("messaggio", messaggio);
				request.getServletContext().getRequestDispatcher("/WEB-INF/templates/risultato.jsp").forward(request, response);
			}
			
		//Modifica
		} else if(azione.equals("/modifica")) {
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String email = request.getParameter("email");
			String num1 = request.getParameter("numero1");
			String num2 = request.getParameter("numero2");
			//controllo che i numeri di telefono siano effettivamente composti da cifre
			if(!num1.isBlank()) {
				if(!(Character.isDigit(num1.charAt(0)) || num1.charAt(0)=='+')){	//controllo che il primo numero sia una cifra o il +
					successo=false;
					messaggio="Il primo carattere del primo numero di telefono contiene caratteri non corretti";
				}
				for(int i =1; i<num1.length();i++) {	//controllo che il resto del numero inserito sia composto da cifre
					if(!Character.isDigit(num1.charAt(i))) {
						successo=false;
						messaggio="Il primo numero di telefono contiene caratteri non corretti";
					}
				}
			}
			if(!num2.isBlank()) {
				if(!(Character.isDigit(num2.charAt(0)) || num2.charAt(0)=='+')){
					successo=false;
					messaggio="Il primo carattere del secondo numero di telefono contiene caratteri non corretti";
				}
				for(int i =1; i<num2.length();i++) {
					if(!Character.isDigit(num2.charAt(i))) {
						successo=false;
						messaggio="Il secondo numero di telefono contiene caratteri non corretti";
					}
				}
			}
			if(id.isBlank()) {	//controllo che l'id sia stato inserito
				successo=false;
				messaggio="Id non inserito";
			} else if(cognome.isBlank()) {	//controllo che sia stato inserito il cognome
				successo=false;
				messaggio="cognome non inserito";
			} else if(num1.isBlank() && num2.isBlank()) {	//controllo che sia stato inserito almeno un numero di telefono
				successo=false;
				messaggio="numero di telefono non inserito";
			} else if(successo) {
				Long idLong = Long.valueOf(id); //se sono qui l'id e' stato inserito, quindi lo casto a Long
				Contatto c = cEjb.getContattoById(idLong);
				if(c==null) {	//controllo che l'id esista
					successo = false;
					messaggio="L'id del contatto inserito non esiste";
				} else {
					c.setCognome(cognome);
					c.setNome(nome);
					c.setEmail(email);
					c.setNumTelefoni(new ArrayList<NumTelefono>());
					if(!num1.isBlank()) {
						NumTelefono n1 = new NumTelefono();
						n1.setNumTelefono(num1);
						c.aggiungiNumero(n1);
					}
					if(!num2.isBlank()) {
						NumTelefono n2 = new NumTelefono();
						n2.setNumTelefono(num2);
						c.aggiungiNumero(n2);
					}
					cEjb.modificaNumeroContatto(c);
				}
			}
			request.setAttribute("successo", successo);
			request.setAttribute("messaggio", messaggio);
			request.getServletContext().getRequestDispatcher("/WEB-INF/templates/risultato.jsp").forward(request, response);
			
		//Elimina
		} else if(azione.equals("/elimina")) {
			String id = request.getParameter("id");
			if(id.isBlank()) {
				successo=false;
				messaggio="Id non inserito";
			} else {
				Long idLong = Long.valueOf(id);
				Contatto c = cEjb.getContattoById(idLong);
				if(c==null) {	//controllo che l'id esista
					successo = false;
					messaggio="L'id del contatto inserito non esiste";
				} else {
					cEjb.eliminaContatto(idLong);
					messaggio="Eliminazione avvenuta con successo";
				}
			}
			request.setAttribute("successo", successo);
			request.setAttribute("messaggio", messaggio);
			request.getServletContext().getRequestDispatcher("/WEB-INF/templates/risultato.jsp").forward(request, response);
		}		
		
	}

}
