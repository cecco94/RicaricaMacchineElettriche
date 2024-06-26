package branchAndBound;

import java.util.ArrayList;

import progetto.RequestImpossibleException;
import progetto.Soluzione;

public class AlgoritmoBranchAndBound {

	
	public static Soluzione trovaSoluzMigliore(PianificazioneBB problema, Soluzione subottima, double costoSubottimo) throws RequestImpossibleException {
		
		Nodo soluzioneMigliore = null;
		double costoMiglioreSoluzione = costoSubottimo;
		
		Nodo radice = new Nodo(new ArrayList<>(), 0);
		
		ArrayList<Nodo> frontiera = new ArrayList<>();		//frontiera = FIFO queue
		frontiera.add(radice);

		int counter = 0;
		
		while ( !frontiera.isEmpty() ) {
			counter++;
			int lastNodeIndex = frontiera.size()-1;
			Nodo nodo = frontiera.remove(lastNodeIndex);
			
			if( isFoglia(nodo, problema) ) {	
				if( nodo.costoDisposizione < costoMiglioreSoluzione ) {
					soluzioneMigliore = nodo;
					costoMiglioreSoluzione = nodo.costoDisposizione;
				}
				else if( nodo.costoDisposizione == costoMiglioreSoluzione ) {
					if( nodo.costoSecondario() < soluzioneMigliore.costoSecondario() ) {
						soluzioneMigliore = nodo;
						costoMiglioreSoluzione = nodo.costoDisposizione;
					}
				}
					
			}
			else {
				if( nodo.costoDisposizione < costoMiglioreSoluzione ) {
					RichiestaDaSistemare r = problema.getListaRichieste().get(nodo.indiceProssimoRectDaPiazzare);
					ArrayList<Nodo> figli = nodo.espandi(r, costoMiglioreSoluzione);
					for(int i = 0; i < figli.size(); i++) {
						frontiera.add(figli.get(i));
					}
				}
			}
			if(counter == 10000) {
				counter = 0;
				System.out.println("nodi in frontiera " + frontiera.size() + ", costo migliore " + costoMiglioreSoluzione);
			}
		}				
		
		if(soluzioneMigliore != null) {
			return soluzioneMigliore.daNodoASoluzione();
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






