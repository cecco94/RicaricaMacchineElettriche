package utils.ordinamento;
import java.util.Comparator;

import progetto.Punto;

public class ComparatorSommaAltezze implements Comparator<Punto>{
//mi serve per trovare velocemente i punti dei rect dove la somma delle altezze è maggiore
	
	
	@Override
	public int compare(Punto p1, Punto p2) {
		if(p1.sommaAltezzeNelPunto < p2.sommaAltezzeNelPunto)
			return 1;
		else if(p1.sommaAltezzeNelPunto > p2.sommaAltezzeNelPunto)
			return -1;
		return 0;
	}

}
