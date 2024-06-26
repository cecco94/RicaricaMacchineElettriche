package utils.ordinamento;

import java.util.Comparator;

import progetto.Punto;

public class ComparatorTempoDiInizio implements Comparator<Punto>{
//mi serve per ordinare i punti dei rect in ordine di tempo
	
	@Override
	public int compare(Punto p1, Punto p2) {
		if(p1.x < p2.x)
			return -1;
		else if(p1.x > p2.x)
			return 1;
		return 0;
	}


}
