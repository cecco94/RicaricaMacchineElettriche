package branchAndBound;

import java.util.ArrayList;
import java.util.Collections;


public class PianificazioneBB {
    private ArrayList<RichiestaDaSistemare> listaRichieste;
    private double massimaDifferenzaFasi = 4.2;
    private double massimaPotenzaReteKw = 7.4;
    
    
    //serve per creare il json
    public PianificazioneBB() {}
    
    //una volta creata l'stanza leggendo il json, bisogna ordinare le richieste per velocizzare il BB
	public void sistemaRichieste() {
		ArrayList<RichiestaDaSistemare> richiesteSistemate = new ArrayList<>();
		for(RichiestaDaSistemare r : listaRichieste) {
			r = new RichiestaDaSistemare(r.identificativoMacchina, 
										 r.fase, 
										 r.energia, 
										 r.minutoInizio,
										 r.minutoFine, 
										 r.potenzaMassimaMacchina, 
										 r.potenzaMinimaMacchina);
			richiesteSistemate.add(r);
		}
		Collections.sort(richiesteSistemate);
		listaRichieste = richiesteSistemate;		
	}
    
	
	public ArrayList<RichiestaDaSistemare> getListaRichieste() {
		return listaRichieste;
	}


	public void setListaRichieste(ArrayList<RichiestaDaSistemare> listaRichieste) {
		this.listaRichieste = listaRichieste;
	}


	public double getMassimaDifferenzaFasi() {
		return massimaDifferenzaFasi;
	}


	public void setMassimaDifferenzaFasi(double massimaDifferenzaFasi) {
		this.massimaDifferenzaFasi = massimaDifferenzaFasi;
	}


	public double getMassimaPotenzaReteKw() {
		return massimaPotenzaReteKw;
	}


	public void setMassimaPotenzaReteKw(double massimaPotenzaReteKw) {
		this.massimaPotenzaReteKw = massimaPotenzaReteKw;
	}
   
    
}
