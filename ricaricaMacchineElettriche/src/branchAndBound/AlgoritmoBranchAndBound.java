package branchAndBound;

import java.util.ArrayList;
import java.util.Collections;

import progetto.RequestImpossibleException;
import simAnn.Soluzione;

public class AlgoritmoBranchAndBound {

	
	public static Soluzione trovaSoluzMigliore(PianificazioneBB problema, double costoSubottimo) throws RequestImpossibleException {
        long start = System.currentTimeMillis();
        
		Nodo soluzioneOttima = null;
		double costoOttimo = costoSubottimo;
		
		Nodo radice = new Nodo(new ArrayList<>(), 0);
		
		ArrayList<Nodo> frontiera = new ArrayList<>();		//frontiera = FIFO queue
		frontiera.add(radice);

		long counter = 0;
		int numFigliAaggiunti = 0;
		
		while ( !frontiera.isEmpty() && counter <= 2300000) {
			counter++;
			
			//prendi l'ultimo nodo
			int lastNodeIndex = frontiera.size()-1;
			Nodo nodo = frontiera.remove(lastNodeIndex);
			//Collections.sort(frontiera);
			
			if( isFoglia(nodo, problema) ) {
				numFigliAaggiunti = 0;
				if( nodo.costoDisposizione < costoOttimo ) {
					soluzioneOttima = nodo;
					costoOttimo = nodo.costoDisposizione;
				}
//				else if( nodo.costoDisposizione == costoOttimo ) {
//					if( nodo.costoSecondario() < soluzioneOttima.costoSecondario() ) {
//						soluzioneOttima = nodo;
//						costoOttimo = nodo.costoDisposizione;
//					}
//				}
			} 
			else {	//nodo interno
				numFigliAaggiunti = 0;
				if( nodo.costoDisposizione < costoOttimo ) {
					RichiestaDaSistemare r = problema.getListaRichieste().get(nodo.indiceRichiestaDaPiazzare);
					ArrayList<Nodo> figli = nodo.espandi(r, costoOttimo);
					numFigliAaggiunti = figli.size();
					for(int i = 0; i < figli.size(); i++) {
						frontiera.add(figli.get(i));
					}
				}
			}
						
			if(counter%1000 == 0) {
				if( !isFoglia(nodo, problema)) {
				System.out.println("nodi in frontiera " + frontiera.size() + ", costo migliore " + costoOttimo + 
						", richiesta " + problema.getListaRichieste().get(nodo.indiceRichiestaDaPiazzare).identificativoMacchina + ", nodi aggiunti " + numFigliAaggiunti + ", su " + nodo.figliCreati + ", iteraz " + counter +  ", durata " + (System.currentTimeMillis() - start) );
			
				}
			}
		}				
		
		
		if(soluzioneOttima != null) {
			return soluzioneOttima.daNodoASoluzione();
		}
		return null;
	}

	
	public static boolean isFoglia(Nodo n, PianificazioneBB probl) {
		return n.indiceRichiestaDaPiazzare == probl.getListaRichieste().size();
	}
	
	
	public static double trunc(double numero) {
		//tronco il costo della soluzione a 5 cifre dopo la virgola
		double precisione = 100000.0;
		return Math.floor(numero*precisione)/precisione;
	}
	
}






