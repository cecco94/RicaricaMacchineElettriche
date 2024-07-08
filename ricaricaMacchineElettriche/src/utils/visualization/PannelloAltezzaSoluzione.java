package utils.visualization;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import progetto.Punto;
import progetto.Rettangolo;
import progetto.Soluzione;
import progetto.TestClass;


public class PannelloAltezzaSoluzione extends JPanel {

	private static final long serialVersionUID = 1L;
	public Soluzione soluzione;
	int altezzaAsseX = 3*TestClass.altezzaFinestra/4;

		
	public PannelloAltezzaSoluzione(Soluzione sol) {
		super();
		soluzione = sol;
	}
	
	 @Override
     protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
    	 Graphics2D g2 = (Graphics2D)g;
         drawRectangles(g2);
         drawHigthFunction(g2);
         drawAxes(g2);
     }


	private void drawAxes(Graphics2D g2) {				
		g2.setColor(Color.black);
		int lunghezzaTrattino = 5;
		//trattini verticali
		for(int x = 0; x < TestClass.larghezzaFinestra; x += 10) {
			if (x == 480){
				lunghezzaTrattino = 30;
			}
			else if(x % 60 == 0) {
				lunghezzaTrattino = 15;
			}
			else {
				lunghezzaTrattino = 7;
			}
			g2.drawLine(x, altezzaAsseX - lunghezzaTrattino, x, altezzaAsseX);
		}
		
		//linea orizzontale
		g2.drawLine(0, altezzaAsseX, TestClass.larghezzaFinestra, altezzaAsseX);
		
		//linea verticale
		g2.drawLine(TestClass.larghezzaFinestra - 5, 0, TestClass.larghezzaFinestra - 5, altezzaAsseX);
		
		//trattini orizzontali
		for(int y = 0; y < altezzaAsseX; y += 10) {
			if(y % 40 == 0) {
				lunghezzaTrattino = 10;
			}
			else {
				lunghezzaTrattino = 5;
			}
			g2.drawLine(TestClass.larghezzaFinestra - lunghezzaTrattino, y, TestClass.larghezzaFinestra + lunghezzaTrattino, y);
		}
	}

	
	private void drawRectangles(Graphics2D g2) {		
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 for(int i = 0; i < soluzione.rettangoli.size(); i++) {
    		 Rettangolo r = soluzione.rettangoli.get(i);
    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.margineSinistro, altezzaAsseX - r.altezza*40, r.base, r.altezza*40);    		 
    		 if((r.fase) == 1) {
    			 g2.setColor(Color.GREEN);
    		 }
    		 else if((r.fase) == 2) {
    			 g2.setColor(Color.BLUE);
    		 }
    		 else if((r.fase) == 3) {
    			 g2.setColor(Color.orange);
    		 }
    		 else if((r.fase) == 0) {
    			 g2.setColor(Color.magenta);
    		 }
    		 
    		 g2.fill(rect);
    		 g2.setColor(Color.black);
    		 g2.draw(rect);
    		 
    		 //disegna id rect
    		 String id = String.valueOf(r.identificativo);
    		 g2.drawString(id, r.margineSinistro + r.base/2, (int)(altezzaAsseX - r.altezza*40 - 5));
    		 
    		 //disegna tempo a disposizione rect
    		 g2.drawLine(r.margineSinistroMinimo, 20 + altezzaAsseX + i*20, r.margineDestroMassimo, 20 + altezzaAsseX + i*20);
    		 g2.drawString(id, r.margineDestroMassimo, 20 + altezzaAsseX + i*20);
    	 }
    	 
     }
	
	
    private void drawHigthFunction(Graphics2D g2) {
	   	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	   	 g2.setColor(Color.red);
	   	 
	   	 for(int i = 0; i < soluzione.puntiDiInizioFineRettangoli.size() - 1; i++) {
	   		 
	   		Punto p = soluzione.puntiDiInizioFineRettangoli.get(i);
	   		Punto p2 = soluzione.puntiDiInizioFineRettangoli.get(i + 1);   
	   		
	   		Rectangle2D.Double rect = new Rectangle2D.Double(p.x, altezzaAsseX - p.sommaAltezzeNelPunto*40, p2.x - p.x, 2);  
	   		g2.fill(rect);
	   	 }
   	 
	   	 g2.drawLine(0, (int)(altezzaAsseX - soluzione.altezzaMassima()*40) , +
				 TestClass.larghezzaFinestra, (int)(altezzaAsseX - soluzione.altezzaMassima()*40));
	}
 
}
