package branchAndBound;

import java.util.ArrayList;

import progetto.RequestImpossibleException;
import progetto.Soluzione;
import progetto.TestClass;

public class AlgoritmoBranchAndBound {

	
	public static Soluzione trovaSoluzMigliore(PianificazioneBB problema, double costoSubottimo) throws RequestImpossibleException {
		
		Nodo soluzioneOttima = null;
		double costoOttimo = costoSubottimo;
		
		Nodo radice = new Nodo(new ArrayList<>(), 0);
		
		ArrayList<Nodo> frontiera = new ArrayList<>();		//frontiera = FIFO queue
		frontiera.add(radice);

		int counter = 0;
		
		while ( !frontiera.isEmpty() ) {
			counter++;
			
			//prendi l'ultimo nodo
			int lastNodeIndex = frontiera.size()-1;
			Nodo nodo = frontiera.remove(lastNodeIndex);
			
			if( isFoglia(nodo, problema) ) {	
				if( nodo.costoDisposizione < costoOttimo ) {
					soluzioneOttima = nodo;
					costoOttimo = nodo.costoDisposizione;
				}
				else if( nodo.costoDisposizione == costoOttimo ) {
					if( nodo.costoSecondario() < soluzioneOttima.costoSecondario() ) {
						soluzioneOttima = nodo;
						costoOttimo = nodo.costoDisposizione;
					}
				}
			} 
			else {	//nodo interno
				if( nodo.costoDisposizione < costoOttimo ) {
					RichiestaDaSistemare r = problema.getListaRichieste().get(nodo.indiceProssimoRectDaPiazzare);
					ArrayList<Nodo> figli = nodo.espandi(r, costoOttimo);
					for(int i = 0; i < figli.size(); i++) {
						frontiera.add(figli.get(i));
					}
				}
			}
			if(counter == 1000) {
				counter = 0;
				System.out.println("nodi in frontiera " + frontiera.size() + " costo migliore " + costoOttimo);
			}
		}				
		
		if(soluzioneOttima != null) {
			return soluzioneOttima.daNodoASoluzione();
		}
		return null;
	}

	
	public static boolean isFoglia(Nodo n, PianificazioneBB probl) {
		return n.indiceProssimoRectDaPiazzare == probl.getListaRichieste().size();
	}
	
	
	public static double trunc(double numero) {
		//tronco il costo della soluzione a 5 cifre dopo la virgola
		double precisione = 100000.0;
		return Math.floor(numero*precisione)/precisione;
	}
	
}






