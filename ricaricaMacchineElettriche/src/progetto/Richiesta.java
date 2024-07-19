package progetto;

public class Richiesta {

	public int identificativoMacchina;
	
	public int fase;
	
	public double energia, potenzaMassimaMacchina, potenzaMinimaMacchina;
	
	//intervallo temporale imigliore, da trovare
	public int minutoInizio, minutoFine;
	
	
	//serve un costruttore vuoto per la creazione del file json
	public Richiesta() {
	}

	public Richiesta(int id, int f, double e, int start, int stop, double kwMax, double kwMin) {
		identificativoMacchina = id;
		fase = f;
		energia = e;
		minutoInizio = start;
		minutoFine = stop;
		potenzaMassimaMacchina = kwMax;
		potenzaMinimaMacchina = kwMin;
	}

	@Override
	public String toString() {
		return "Richiesta [identificativoMacchina=" + identificativoMacchina + ", fase=" + fase + ", energia=" + energia
				+ ", potenzaMassimaMacchina=" + potenzaMassimaMacchina + ", potenzaMinimaMacchina="
				+ potenzaMinimaMacchina + ", minutoInizio=" + minutoInizio + ", minutoFine=" + minutoFine + "]";
	}
	
	
	
}
