package branchAndBound;

public class RichiestaDaSistemare implements Comparable<RichiestaDaSistemare>{

	public int identificativoMacchina;	
	public int fase;	
	public double energia, potenzaMassimaMacchina, potenzaMinimaMacchina;	
	//intervallo temporale imigliore, da trovare
	public int minutoInizio, minutoFine, minBase, maxBase;

	
	public RichiestaDaSistemare() {}
	
	
	public RichiestaDaSistemare(int id, int f, double e, int start, int stop, double kwMax, double kwMin) {
		identificativoMacchina = id;
		fase = f;
		energia = e;
		minutoInizio = start;
		minutoFine = stop;
		potenzaMassimaMacchina = kwMax;
		potenzaMinimaMacchina = kwMin;
		
		minBase = (int)( Math.ceil(energia/potenzaMassimaMacchina) );		
		maxBase = Math.min( (int)(energia/potenzaMinimaMacchina), minutoFine - minutoInizio );		
	}

	
	@Override
	public int compareTo(RichiestaDaSistemare r) {
		if( minutoFine - minutoInizio < r.minutoFine - r.minutoInizio ) {
			return -1;
		}
		if( minutoFine - minutoInizio > r.minutoFine - r.minutoInizio ) {
			return 1;
		}
		//first the rect with less avaible time
		if( minutoFine - minutoInizio == r.minutoFine - r.minutoInizio ) {
			if( maxBase < r.maxBase ) {
				return -1;
			}
		}
		return 0;
	}
	
}
