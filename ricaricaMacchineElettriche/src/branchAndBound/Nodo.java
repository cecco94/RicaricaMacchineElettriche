package branchAndBound;

import java.util.ArrayList;
import java.util.Collections;

import progetto.RequestImpossibleException;
import progetto.Rettangolo;
import progetto.Soluzione;
import progetto.TestClass;
import utils.ordinamento.ComparatorID;

public class Nodo {

	ArrayList<PuntoBB> puntiRectPiazzati;
	int indiceRichiestaDaPiazzare;
	double costoDisposizione = 0;
	public int figliCreati;
	
	public Nodo() {}
	
	
	public Nodo(ArrayList<PuntoBB> punti, int indice) {
		puntiRectPiazzati = punti;
		indiceRichiestaDaPiazzare = indice;
		costoDisposizione = costo();
		figliCreati = 0;
	}
	
	
	public ArrayList<Nodo> espandi( RichiestaDaSistemare rectDaFissare, double costoMiglioreSoluz ) {				
		ArrayList<Nodo> figli = new ArrayList<>();
		int inizioEstremoSinistro = rectDaFissare.minutoInizio;
		int fineEstremoSinistro = rectDaFissare.minutoFine - rectDaFissare.minBase;
		for ( int i = inizioEstremoSinistro; i <= fineEstremoSinistro; i++ ) {
			for ( int b = rectDaFissare.minBase; b <= rectDaFissare.maxBase; b++ ) {
				if ( i + b <= rectDaFissare.minutoFine ) {
					Nodo figlio = creaFiglio(rectDaFissare, b, i, costoMiglioreSoluz);
					figliCreati++;
					if( figlio.costoDisposizione < costoMiglioreSoluz && !figlio.superaSfasamento()) 
						figli.add(figlio);	
				}
			}
		}
		return figli;	
	}
	
	
	public Nodo creaFiglio(RichiestaDaSistemare rectDaFissare, int b, int i, double costoMiglioreSoluz) {
		//dati rect piazzato
		double nuovaAltezza = rectDaFissare.energia/b;
		PuntoBB inizio = new PuntoBB(rectDaFissare, i, nuovaAltezza, true);
		PuntoBB fine = new PuntoBB(rectDaFissare, i + b, nuovaAltezza, false);
		
		//situazione dei rect inseriti
		ArrayList<PuntoBB> nuoviEstremiRect = new ArrayList<>();					
		for ( int index = 0; index < puntiRectPiazzati.size(); index++ ) {
			nuoviEstremiRect.add(puntiRectPiazzati.get(index).clone());
		}
		nuoviEstremiRect.add(inizio);
		nuoviEstremiRect.add(fine);
		Collections.sort(nuoviEstremiRect);
		
		Nodo figlio = new Nodo(nuoviEstremiRect, indiceRichiestaDaPiazzare + 1);
		return figlio;
	}
	
	
	public Soluzione daNodoASoluzione() throws RequestImpossibleException {		
		ComparatorID cid = new ComparatorID();
		Collections.sort(puntiRectPiazzati, cid);
		ArrayList<Rettangolo> rectList = new ArrayList<>();
		for(int i = 0; i < puntiRectPiazzati.size(); i+=2) {
			PuntoBB p = puntiRectPiazzati.get(i);
			PuntoBB p2 = puntiRectPiazzati.get(i+1);
			Rettangolo r = new Rettangolo(p.richiesta.identificativoMacchina, p.richiesta.fase, p.richiesta.minutoInizio, p.richiesta.minutoFine, p.minuto, p2.minuto, p.richiesta.energia, p.richiesta.potenzaMassimaMacchina, p.richiesta.potenzaMinimaMacchina, p.richiesta.minBase, p.richiesta.maxBase);
			rectList.add(r);
		}
		return new Soluzione(rectList, TestClass.massimoSfasamentoConsentito, TestClass.massimaAltezzaConsentita);
	}
	
	
	public double costo() {
		   return AlgoritmoBranchAndBound.trunc( altezzaMassima() );
	   }
	   
	   
   public double altezzaMassima() {
	   double h = 0;
	   double maxH = 0;
	   for(int i = 0; i < puntiRectPiazzati.size(); i++) {
		   
		   PuntoBB p = puntiRectPiazzati.get(i);
		   
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
   
   
   public boolean superaSfasamento() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double h_fase_3 = 0;
	   double sfasamento_massimo = 0; 
	   
	   for(int i = 0; i < puntiRectPiazzati.size(); i++) {
		   
		   PuntoBB p = puntiRectPiazzati.get(i);
		   
		   if(p.punto_di_inizio) {
			   if(p.richiesta.fase == 1) {
				   h_fase_1 += p.altezzaRettangolo;
			   }
			   else if(p.richiesta.fase == 2) {
				   h_fase_2 += p.altezzaRettangolo;
			   }
			   else if(p.richiesta.fase == 3) {
				   h_fase_3 += p.altezzaRettangolo;
			   }
		   }
			   
		   else {
			   if(p.richiesta.fase == 1) {
				   h_fase_1 -= p.altezzaRettangolo;
			   }
			   else if(p.richiesta.fase == 2) {
				   h_fase_2 -= p.altezzaRettangolo;
			   }
			   else if(p.richiesta.fase == 3) {
				   h_fase_3 -= p.altezzaRettangolo;
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
	   
	   if(sfasamento_massimo < TestClass.massimoSfasamentoConsentito) {
		   return false;
	   }
		   
	   return true;
   }
	
   
   public double costoSecondario() {
	   return AlgoritmoBranchAndBound.trunc( mediaRestringimentoRect() + contaIntersezioni() );
   }
   
   
   public double mediaRestringimentoRect() {
	   double media = 0;
	   
	   for (int i = 0; i < puntiRectPiazzati.size(); i++) {
		   PuntoBB p = puntiRectPiazzati.get(i);
		   if( p.punto_di_inizio ) {
			   for (int j = i+1; j < puntiRectPiazzati.size(); j++) {
				   PuntoBB p2 = puntiRectPiazzati.get(j);
				   if ( p2.richiesta == p.richiesta ) {
					   media += p.richiesta.maxBase - (p2.minuto - p.minuto);
					   break;
				   }
			   }
		   }
	   }

	   return media;
   }
   
   
   public int contaIntersezioni() {
	   float intersezioni = 0;
	   for(int i = 0; i < puntiRectPiazzati.size(); i++) {
		   PuntoBB p = puntiRectPiazzati.get(i);
		   if(p.punto_di_inizio) {
			   
			  for(int j = i+1; j < puntiRectPiazzati.size();j++) {
				  PuntoBB p2 = puntiRectPiazzati.get(j);
				  if(p2.richiesta == p.richiesta)	//estremo destro dello stesso rect
					  break;
				  if(p2.punto_di_inizio && p2.minuto == p.minuto)
					  intersezioni += 0.5;
				  if(p2.punto_di_inizio && p2.minuto < p.minuto)
					  intersezioni++;
				  }
			  }
		  }
	      
	   return (int)intersezioni;
   }
   
   
   public void printNodo() {
	   System.out.println("costo " + costoDisposizione);
	   System.out.println("numero rect " + puntiRectPiazzati.size()/2);
	   for(int i = 0; i < puntiRectPiazzati.size(); i++) {
		   System.out.println(puntiRectPiazzati.get(i).toString());
	   }
   }
   
}
