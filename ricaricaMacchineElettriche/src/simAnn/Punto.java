package simAnn;

import lombok.Data;


@Data
public class Punto {

	public Rettangolo rect;
	public int x;
	public double altezzaRettangolo, sfasamentoNelPunto = 0, sommaAltezzeNelPunto = 0;
	public boolean punto_di_inizio;
	
	
	public Punto() {
		
	}
	
	public Punto(Rettangolo r, int m, double altezza_rect, boolean punto_di_inizio) {
		super();
		this.rect = r;
		this.x = m;
		this.altezzaRettangolo = altezza_rect;
		this.punto_di_inizio = punto_di_inizio;
	}
	
	public String toString() {
		return "id rect " + rect.identificativo + ",  minuto " + x + ",  altezza " + altezzaRettangolo +
				",  fase " + rect.fase + ",  sommaAltezze " + sommaAltezzeNelPunto + ",  sfasamento " + sfasamentoNelPunto;
	}
	
	
}
