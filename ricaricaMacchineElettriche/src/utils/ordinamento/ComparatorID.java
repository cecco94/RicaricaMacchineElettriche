package utils.ordinamento;

import java.util.Comparator;

import branchAndBound.PuntoBB;

public class ComparatorID implements Comparator<PuntoBB>{

	@Override
	public int compare(PuntoBB p1, PuntoBB p2) {
		if(p1.richiesta.identificativoMacchina > p2.richiesta.identificativoMacchina)
			return -1;
		else if(p1.richiesta.identificativoMacchina < p2.richiesta.identificativoMacchina)
			return 1;
		return 0;
	}
}