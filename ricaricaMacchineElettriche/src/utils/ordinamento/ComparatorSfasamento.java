package utils.ordinamento;

import java.util.Comparator;

import progetto.Punto;

public class ComparatorSfasamento implements Comparator<Punto>{

	@Override
	public int compare(Punto p1, Punto p2) {
		if(p1.sfasamentoNelPunto > p2.sfasamentoNelPunto)
			return -1;
		else if(p1.sfasamentoNelPunto < p2.sfasamentoNelPunto)
			return 1;
		return 0;
	}

}
