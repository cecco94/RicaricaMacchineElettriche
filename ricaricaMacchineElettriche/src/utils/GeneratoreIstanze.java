package utils;


import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import progetto.Pianificazione;
import progetto.JSON;
import progetto.RequestImpossibleException;
import progetto.Rettangolo;
import progetto.Richiesta;
import progetto.Soluzione;



public class GeneratoreIstanze {
	

	public static int numero_richieste_urgenti;
	
    public static double massimoSfasamentoConsentito = 4.2;
    public static double massimaAltezzaConsentita = 7.4;
    
	//l'altezza dei rect è in kw, questi numeri sono limiti fisici che ho letto su internet
	public static double altezza_massima = massimaAltezzaConsentita/2;
	public static double altezza_minima = 2.3;
	
	
	//crea istanza casuale di un piano e la salva
	public static void generaIstanzaProblema(int macchine_tranquille, int macchine_urgenti) throws RequestImpossibleException, JsonMappingException, JsonProcessingException {
		numero_richieste_urgenti = macchine_urgenti;
		int numero_macchine = macchine_tranquille + macchine_urgenti;
		
		ArrayList<Richiesta> rect = new ArrayList<>(); 
        for(int i = 0; i < numero_macchine; i++) {
        	rect.add(generaRichiesta(i));     
        }
        Pianificazione istanza = new Pianificazione(rect, massimoSfasamentoConsentito, massimaAltezzaConsentita);
        
        String path = "data/istanze/";
        String filename = "1_problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti +"_macchine_urgenti.json";
        JSON.salvaIstanzaProblema(path, filename, istanza);
 	}
	
	
	//crea istanza casuale di una macchina
	public static Richiesta generaRichiesta(int id){
			Random rand = new Random();	
			
			int fase = rand.nextInt(4);		//fase = 0 dignifica che la macchina usa tutte e tre le fasi
			
			double area = 30.0 + rand.nextDouble(250.0); //quanti kwh può accumulare in media un'auto

			int inizio_nottata = 0;	
			int fine_nottata = 480;
			
			//a caso crea richieste con tempi minori di tutta la notte, per simulare richieste più urgenti
			if(numero_richieste_urgenti > 0) {
				
				int base_massima = (int)(area/altezza_minima);	
				int base_minima = (int)(Math.ceil(area/altezza_massima));

				int base = rand.nextInt(base_minima, base_massima);			//prende come base una qualsiasi base fattibile
				
				int inizio = rand.nextInt(fine_nottata - base_massima);		//prende come punto di inizio un qualsiasi momento fattibile della nottata
				int fine =  inizio + base; 
				
				numero_richieste_urgenti--;
				return new Richiesta(id, fase, area, inizio, fine,  altezza_massima, altezza_minima);
			}
			
			//crea richieste con tempo a disposizione = tutta la notte
			return new Richiesta(id, fase, area, inizio_nottata, fine_nottata, altezza_massima, altezza_minima);
		}
	
	
	public static Pianificazione generaIstanzaProblemaSenzaSalvare(int macchine_tranquille, int macchine_urgenti) throws RequestImpossibleException {
		numero_richieste_urgenti = macchine_urgenti;
		int numero_macchine = macchine_tranquille + macchine_urgenti;
		
		ArrayList<Richiesta> rect = new ArrayList<>(); 
        for(int i = 0; i < numero_macchine; i++) {
        	rect.add(generaRichiesta(i));     
        }        
          
        return new Pianificazione(rect, massimoSfasamentoConsentito, massimaAltezzaConsentita);
	}
		
	
	public static void generaIstanzaSpecifica() throws RequestImpossibleException, JsonMappingException, JsonProcessingException {
		int macchine_tranquille = 0;
		int macchine_urgenti = 0;
		
		ArrayList<Richiesta> richieste = new ArrayList<>();
		richieste.add(new Richiesta(0, 1, 250.0, 10, 110, 7.5, 2.5));
		//aggiungi altre richieste..
		
		Pianificazione istanzaSpecifica = new Pianificazione(richieste, massimoSfasamentoConsentito, massimaAltezzaConsentita);
		
		String path = "data/istanze/";
		String filename = "1_problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti +"_macchine_urgenti.json";
		
		JSON.salvaIstanzaProblema(path, filename, istanzaSpecifica);
		
	}
	
	
	//crea istanza casuale di un piano senza salvarla
	public static Soluzione generaIstanzaSpecificaProblemaSenzaSalvare() throws RequestImpossibleException {
		ArrayList<Rettangolo> rect = new ArrayList<>();
//		rect.add(new Rettangolo(1, 1, 0, 100, 300.0, 10, 3));
//		rect.add(new Rettangolo(2, 4, 50, 150, 300.0, 10, 3));
//		rect.add(new Rettangolo(3, 2, 140, 300, 300.0, 10, 3));
//		rect.add(new Rettangolo(4, 3, 0, 120, 300.0, 10, 1));

		rect.add(new Rettangolo(0, 1, 205, 246, 122.57449463908152, altezza_massima, altezza_minima));
		rect.add(new Rettangolo(2, 3, 213, 244, 109.95380364705792, altezza_massima, altezza_minima));
		rect.add(new Rettangolo(3, 3, 241, 270, 93.26641808851427, altezza_massima, altezza_minima));
		
		rect.add(new Rettangolo(4, 1, 319, 390, 204.2159663011782, altezza_massima, altezza_minima));
		rect.add(new Rettangolo(5, 3, 351, 366, 38.768136014971255, altezza_massima, altezza_minima));
		
//		rect.add(new Rettangolo(0, 1, 0, 100, 0, 100, 300, 10, 3, 30, 100));
//		rect.add(new Rettangolo(1, 2, 110, 300, 100, 300, 300, 10, 3, 30, 100));

//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));
//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));
//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));

		Soluzione istanzaSpecifica = new Soluzione(rect, massimoSfasamentoConsentito, massimaAltezzaConsentita);
        return istanzaSpecifica;
	}
	
}




