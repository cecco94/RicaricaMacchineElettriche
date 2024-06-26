package progetto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Pianificazione {
    private ArrayList<Richiesta> listaRichieste;
    private double massimaDifferenzaFasi = 4.2;
    private double massimaPotenzaReteKw = 7.4;
    
    
    //serve per creare il json
    public Pianificazione() {}
    
    
    public Pianificazione(ArrayList<Richiesta> r, double sfasMax, double hMax) {
    	listaRichieste = r;
    	massimaDifferenzaFasi = sfasMax;
        massimaPotenzaReteKw = hMax;
    }
    
}
