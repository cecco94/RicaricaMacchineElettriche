package branchAndBound;

public class RichiestaDaSistemare implements Comparable<RichiestaDaSistemare>{

	public int identificativoMacchina;	
	public int fase;	
	public double energia, potenzaMassimaMacchina, potenzaMinimaMacchina;	
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
		
		minBase = (int)( Math.ceil(energia*60/potenzaMassimaMacchina) );		
		maxBase = Math.min( (int)(energia*60/potenzaMinimaMacchina), minutoFine - minutoInizio );		
	}

	
	@Override
	public int compareTo(RichiestaDaSistemare r) {
		//first the rect with less avaible time
		if( (minutoFine - minutoInizio) < (r.minutoFine - r.minutoInizio) ) {
			return -1;
		}
		if( (minutoFine - minutoInizio) > (r.minutoFine - r.minutoInizio) ) {
			return 1;
		}
		//se hanno lo stesso tempo, prima quello che si può allargare di meno
		if( (minutoFine - minutoInizio) == (r.minutoFine - r.minutoInizio) ) {
			if( maxBase < r.maxBase ) {
				return -1;
			}
		}
		return 0;
	}
	
}
