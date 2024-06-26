package utils.ordinamento;

import java.util.Comparator;

import progetto.Punto;
import progetto.Rettangolo;

public class ComparatorTempoDiFine  implements Comparator<Rettangolo>{
	
	//mi serve per ordinare i rect e vedere qual'è quello con tempo di fine ricarica a disposizione maggiore
	@Override
	public int compare(Rettangolo r1, Rettangolo r2) {
		if(r1.margineDestroMassimo > r2.margineDestroMassimo)
			return -1;
		
		if(r1.margineDestroMassimo > r2.margineDestroMassimo)
			return 1;
		
		return 0;
	}
}
