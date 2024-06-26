package progetto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utils.ordinamento.ComparatorSfasamento;
import utils.ordinamento.ComparatorSommaAltezze;
import utils.ordinamento.ComparatorTempoDiInizio;


public class Soluzione {

    public ArrayList<Rettangolo> rettangoli;
    public ArrayList<Punto> puntiDiInizioFineRettangoli;
    public double massimoSfasamentoConsentito = 4.2;
    public double massimaAltezzaConsentita = 7.4;
    
    
    //serve per creare il json
    public Soluzione() {  }
    
    
    public Soluzione(ArrayList<Rettangolo> rect, double maxSfas, double maxH) {
    	massimoSfasamentoConsentito = maxSfas;
    	massimaAltezzaConsentita = maxH;
    	
    	//ordina i rettangoli per punto di inizio
    	rettangoli = rect;
  	    Collections.sort(rettangoli);
 	    
    	//sistema i punti di inizio e fine dei rettangoli
    	puntiDiInizioFineRettangoli = new ArrayList<>();
    	for(int i = 0; i < rettangoli.size(); i++) {
    		Rettangolo r = rettangoli.get(i);
    		puntiDiInizioFineRettangoli.add(new Punto(r, r.margineSinistro, r.altezza, true));
    		puntiDiInizioFineRettangoli.add(new Punto(r, r.margineDestro, r.altezza, false));
    	}
    	ComparatorTempoDiInizio c = new ComparatorTempoDiInizio();
    	Collections.sort(puntiDiInizioFineRettangoli, c);	    	
    }
   
   
   public double costoSoluzione() {
	   return altezzaMassima() + 100*sfasamento();
   }
   
   
   public double altezzaMassima() {
	   double h = 0;
	   double maxH = 0;
	   for(int i = 0; i < puntiDiInizioFineRettangoli.size(); i++) {
		   
		   Punto p = puntiDiInizioFineRettangoli.get(i);
		   
		   if(p.punto_di_inizio) {
			   h += p.altezzaRettangolo;
			   p.sommaAltezzeNelPunto = h;
		   }		   
		   else {
			   h -= p.altezzaRettangolo;
			   p.sommaAltezzeNelPunto = h;
		   }
		   if(h > maxH)
			   maxH = h;
	   }
	   return maxH;
   }
   
   
   public double sfasamento() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double h_fase_3 = 0;
	   double sfasamento_massimo = 0; 
	   
	   for(int i = 0; i < puntiDiInizioFineRettangoli.size(); i++) {
		   
		   Punto p = puntiDiInizioFineRettangoli.get(i);
		   
		   if(p.punto_di_inizio) {
			   if(p.rect.fase == 1) {
				   h_fase_1 += p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 2) {
				   h_fase_2 += p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 3) {
				   h_fase_3 += p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 0) {
				   h_fase_1 += p.altezzaRettangolo/3;
				   h_fase_2 += p.altezzaRettangolo/3;
				   h_fase_3 += p.altezzaRettangolo/3;
			   }
		   }
			   
		   else {
			   if(p.rect.fase == 1) {
				   h_fase_1 -= p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 2) {
				   h_fase_2 -= p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 3) {
				   h_fase_3 -= p.altezzaRettangolo;
			   }
			   else if(p.rect.fase == 0) {
				   h_fase_1 -= p.altezzaRettangolo/3;
				   h_fase_2 -= p.altezzaRettangolo/3;
				   h_fase_3 -= p.altezzaRettangolo/3;
			   }
		   }
		   
		   double sfasamento_fase_1_2 = Math.abs(h_fase_1 - h_fase_2);
		   double sfasamento_fase_1_3 = Math.abs(h_fase_1 - h_fase_3);
		   double sfasamento_fase_2_3 = Math.abs(h_fase_2 - h_fase_3);

		   double sfasamento_complessivo = Math.max(sfasamento_fase_1_2, Math.max(sfasamento_fase_1_3, sfasamento_fase_2_3)); 
		   p.sfasamentoNelPunto = sfasamento_complessivo;
		   
		   if(sfasamento_complessivo > sfasamento_massimo) {
			   sfasamento_massimo = sfasamento_complessivo;
		   }
	   }
	   
	   if(sfasamento_massimo < massimoSfasamentoConsentito) {
		   return 0;
	   }
		   
	   return sfasamento_massimo;
   }
   
   
   public int contaIntersezioni() {
	   float intersezioni = 0;
	   for( Rettangolo r : rettangoli ) {
		   for( Punto p : puntiDiInizioFineRettangoli ) {
			   //if the start point of the other rectangle in inside the borders of this rectangle
			   if( p.punto_di_inizio && (p.x > r.margineSinistro) && (p.x < r.margineDestro) ) {
				   intersezioni++;
			   }
			   //if the start point of the other rectangle is = start poin of this rectangle
			   if ( p.punto_di_inizio && (p.x == r.margineSinistro) && (p.rect != r ) ) {
				   //0.5 because the program counts this intersection two times
				   intersezioni += 0.5;
			   }
		
			   if( p.x > r.margineDestro ) {
				   break;
			   }
		   }
	   }
	   return (int)intersezioni;
   }

   
   public double mediaInnalzamentoRettangoli() {
	   double mediaInnalzamentoRettangoli = 0;
	   for(int i = 0; i < rettangoli.size(); i++) {
		   
		   Rettangolo r = rettangoli.get(i);
		   mediaInnalzamentoRettangoli += (r.baseMassima - r.base);
	   }
	   
	   mediaInnalzamentoRettangoli /= rettangoli.size();
	   return mediaInnalzamentoRettangoli;
   }
   
   
   public Punto puntoMaxAltezza() {
	   ComparatorSommaAltezze cc = new ComparatorSommaAltezze();
	   Collections.sort(puntiDiInizioFineRettangoli, cc);
	   
	   Punto p = puntiDiInizioFineRettangoli.get(0);
	   
	   ComparatorTempoDiInizio cs = new ComparatorTempoDiInizio();
	   Collections.sort(puntiDiInizioFineRettangoli, cs);
	   
	   return p;
   }
   
  
   public Punto puntoMassimoSfasamento() {
	   ComparatorSfasamento c = new ComparatorSfasamento();
	   Collections.sort(puntiDiInizioFineRettangoli, c);
	   
	   Punto p = puntiDiInizioFineRettangoli.get(0);
	   
	   ComparatorTempoDiInizio cs = new ComparatorTempoDiInizio();
	   Collections.sort(puntiDiInizioFineRettangoli, cs);
	   
	   return p;
   }
   
   
   //serve per l'algoritmo, crea una nuova soluzione sposyando a caso gli estremi dei rettangoli
   public Soluzione generaNuovaSoluzioneCasuale(int passoGenerazione) throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGeneration(rand, passoGenerazione);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list, massimoSfasamentoConsentito, massimaAltezzaConsentita);
	   return new_solution;
   }
   
   
   //serve per il preprocessing: crea una nuova soluzione traslando a caso alcuni rettangoli
   public Soluzione generaNuovaSoluzioneCasualeTraslazione(int passoTraslazione) throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGenerationForTranslationProblem(rand, passoTraslazione);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list, massimoSfasamentoConsentito, massimaAltezzaConsentita);
	   return new_solution;
   }
        
   
   //serve per il preprocessing: crea una nuova configurazione iniziale mettendo i rect a caso nel tempo
   public Soluzione generaNuovaSituazioneDiPartenza() throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).generaNuovoRectDiBaseMassima(rand);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list, massimoSfasamentoConsentito, massimaAltezzaConsentita);
	   return new_solution;
	}
   
	
   public Soluzione clone() {
	   ArrayList<Rettangolo> new_list = new ArrayList<>();
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).clone();
		   new_list.add(new_rect);
	   }
	   Soluzione new_solution = new Soluzione(new_list, massimoSfasamentoConsentito, massimaAltezzaConsentita);
	   return new_solution;
   }
   
   
   public void printSoluzione() {
	   for(int i = 0; i < rettangoli.size(); i++) {
		   System.out.println(rettangoli.get(i).toString());
	   }
   }


public Pianificazione creaSoluzioneDaMettereNelJson() {
	ArrayList<Richiesta> richieste = new ArrayList<>();
	for(int i = 0; i < rettangoli.size(); i++) {
		Rettangolo r = rettangoli.get(i);
		richieste.add(r.fromRectToRequest());
	}
	return new Pianificazione(richieste, massimoSfasamentoConsentito, massimaAltezzaConsentita);
}

   
    
}

