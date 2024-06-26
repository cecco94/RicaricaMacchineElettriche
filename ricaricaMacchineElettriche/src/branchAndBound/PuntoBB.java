package branchAndBound;


public class PuntoBB implements Comparable<PuntoBB>{

	
	public RichiestaDaSistemare richiesta;
	public int minuto;
	public double altezzaRettangolo, sfasamentoNelPunto, sommaAltezzeNelPunto;
	public boolean punto_di_inizio;
	
	
	public PuntoBB() {}
	
	
	public PuntoBB(RichiestaDaSistemare r, int m, double altezza_rect, boolean punto_di_inizio) {
		this.richiesta = r;
		this.minuto = m;
		this.altezzaRettangolo = altezza_rect;
		this.punto_di_inizio = punto_di_inizio;
	}
	
	public String toString() {
		return "id rect " + richiesta.identificativoMacchina + ",  minuto " + minuto + ",  altezza " + altezzaRettangolo +
				",  fase " + richiesta.fase + ",  sommaAltezze " + sommaAltezzeNelPunto + ",  sfasamento " + sfasamentoNelPunto;
	}


	@Override
	public int compareTo(PuntoBB p) {
		if( minuto < p.minuto)
			return -1;
		if( minuto > p.minuto)
			return 1;
		return 0;
	}
	
	
	public PuntoBB clone() {
		return new PuntoBB(richiesta, minuto, altezzaRettangolo, punto_di_inizio);
	}
	
}
