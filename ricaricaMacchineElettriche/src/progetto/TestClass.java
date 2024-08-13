package progetto;

import java.awt.Dimension;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import branchAndBound.AlgoritmoBranchAndBound;
import branchAndBound.PianificazioneBB;
import simAnn.AlgoritmoSimulatedAnnealing;
import simAnn.Soluzione;
import utils.GeneratoreIstanze;
import utils.JSON;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;


public class TestClass {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;

	public static boolean newProblem = false,
						  saveSolution = false;
	
	public static int macchine_tranquille = 8, macchine_urgenti = 0;
	public static int istanza = 10;
	
	public static double massimoSfasamentoConsentito = 6; //4.2;
	public static double massimaAltezzaConsentita = 11; //7.4;
	
		
	public static void main(String[] args) throws RequestImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {		
		
		if(newProblem) {			
			GeneratoreIstanze.generaIstanzaProblema(macchine_tranquille, macchine_urgenti);  
			return;
		}	
		
		//trovo una soluz accettabile col SA
		Soluzione soluzioneIniziale = JSON.caricaRichiestaPianificazione("data/istanze/" + istanza + "_problema_con_"+macchine_tranquille+
																 		"_macchine_tranquille_"+macchine_urgenti+"_macchine_urgenti.json");
		
		massimoSfasamentoConsentito = soluzioneIniziale.massimoSfasamentoConsentito;
		massimaAltezzaConsentita = soluzioneIniziale.massimaAltezzaConsentita;
		
//		double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();
//      visualizzaDatiIniziali(soluzioneIniziale, costoSoluzioneIniziale); 
        
        Soluzione migliore_soluzione;
        double costo_migliore_soluzione;

    	migliore_soluzione = AlgoritmoSimulatedAnnealing.preProcessing2(soluzioneIniziale.clone());
        costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        visualizzaDatiIntermedi(migliore_soluzione, costo_migliore_soluzione);
        
        long start = System.currentTimeMillis();
        migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(migliore_soluzione);
        long durata = System.currentTimeMillis() - start;
        System.out.println("tempo SA " + durata);
        costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        visualizzaDatiFinali(migliore_soluzione, costo_migliore_soluzione);
		
        
        //////////////////////	B & B	///////////////////////////////        
//        System.out.println("\n ######################## B B ######################## \n");
//        System.out.println("trovo soluz ottima..");
//
//        //chiede ora al BB di prendere la soluzione subottima col suo costo e di usare quella come punto di partenza
//        PianificazioneBB problema = JSON.caricaPianificazioneBB("data/istanze/" + istanza + "_problema_con_"+macchine_tranquille+
//																"_macchine_tranquille_"+macchine_urgenti+"_macchine_urgenti.json");
//        problema.sistemaRichieste();        
//        
//        //quando non trova una soluz. fattibile, per non far partire il costo del BB da più di 100
//        if(migliore_soluzione.sfasamento() > 0) {
//        	costo_migliore_soluzione = massimaAltezzaConsentita;
//        }
//        
////        for(double i = 2; i < massimaAltezzaConsentita; i+= 0.25) {
////        	System.out.println("cerco soluz che costa meno di " + i);
////        	Soluzione sol = AlgoritmoBranchAndBound.trovaSoluzMigliore(problema, i);
////        	if(sol != null) {
////        		double costo_soluzione_ottima = sol.costoSoluzione();
////    	        controllaSoluzione(sol);
////    	        visualizzaDatiOttimi(sol, costo_soluzione_ottima);
////        		break;
////        	}
////        }
//        
//        start = System.currentTimeMillis();
//        Soluzione soluzioneOttima = AlgoritmoBranchAndBound.trovaSoluzMigliore(problema, costo_migliore_soluzione);   
//        durata = System.currentTimeMillis() - start;
//        System.out.println("tempo BB " + durata);
//        if( soluzioneOttima != null) {
//	        double costo_soluzione_ottima = soluzioneOttima.costoSoluzione();
//	        //controllaSoluzione(soluzioneOttima);
//	        visualizzaDatiOttimi(soluzioneOttima, costo_soluzione_ottima);
//        }
//        else {
//        	System.out.println("non ho trovato nessuna soluzione migliore della subottima");
//        }  
	}

	
	private static void controllaSoluzione(Soluzione migliore_soluzione) throws PlanImpossibleException, JsonMappingException, JsonProcessingException {
		if(migliore_soluzione.sfasamento() >  migliore_soluzione.massimoSfasamentoConsentito) {
        	throw new PlanImpossibleException("piano impossibile, troppo sbilanciamento di fase nel punto: " + migliore_soluzione.puntoMassimoSfasamento().toString());
        }
        
        if(migliore_soluzione.costoSoluzione() > migliore_soluzione.massimaAltezzaConsentita) {
        	throw new PlanImpossibleException("piano impossibile, troppa richiesta di potenza nel punto: " + migliore_soluzione.puntoMaxAltezza().toString());
        }  
        
        if(saveSolution) {
        	Pianificazione sol = migliore_soluzione.creaSoluzioneDaMettereNelJson();
        	JSON.salvaSoluzione("data/soluzioni/", "problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti + "_macchine_urgenti.json", sol);
        }
	}
	
	
	////////////////////////// METODI AUSILIARI ///////////////////////////
	private static void visualizzaDatiIntermedi(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE DOPO TRASLAZIONE");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO DOPO TRASLAZIONE");
        migliore_soluzione.printSoluzione();
        System.out.println("intersezioni " + migliore_soluzione.contaIntersezioni());
        System.out.println("media restringimento basi " + migliore_soluzione.mediaInnalzamentoRettangoli());
        System.out.println("costo dopo traslaz " + costo_migliore_soluzione);
        System.out.println("altezza max dopo traslaz " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max dopo traslaz " + migliore_soluzione.sfasamento());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizzaDatiFinali(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE FINALI");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO FINALE");
        migliore_soluzione.printSoluzione();
        System.out.println("intersezioni " + migliore_soluzione.contaIntersezioni());
        System.out.println("media restringimento basi " + migliore_soluzione.mediaInnalzamentoRettangoli());
        System.out.println("costo finale " + costo_migliore_soluzione);
        System.out.println("altezza max finale " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max finale " + migliore_soluzione.sfasamento());
		System.out.println("max sfasamento " + massimoSfasamentoConsentito);
		System.out.println("max altezza " + massimaAltezzaConsentita);

	}
	
	
	private static void visualizzaDatiOttimi(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE OTTIME");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO OTTIMO");
        migliore_soluzione.printSoluzione();
        System.out.println("intersezioni " + migliore_soluzione.contaIntersezioni());
        System.out.println("media restringimento basi " + migliore_soluzione.mediaInnalzamentoRettangoli());
        System.out.println("costo finale " + costo_migliore_soluzione);
        System.out.println("altezza max finale " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max finale " + migliore_soluzione.sfasamento());
		
	}
	

	private static void visualizzaDatiIniziali(Soluzione soluzioneIniziale, double costoSoluzioneIniziale) {
		visualizzaAltezzaSoluzione(soluzioneIniziale, "ALTEZZE PRIMA");
        visualizzaSfasamentoSoluzione(soluzioneIniziale, "SFASAMENTO PRIMA");
        soluzioneIniziale.printSoluzione();
        System.out.println("intersezioni " + soluzioneIniziale.contaIntersezioni());
        System.out.println("media restringimento basi " + soluzioneIniziale.mediaInnalzamentoRettangoli());
        System.out.println("costo iniziale " + costoSoluzioneIniziale);
        System.out.println("altezza max iniziale " + soluzioneIniziale.altezzaMassima());
        System.out.println("sfasamento max iniziale " + soluzioneIniziale.sfasamento());
        System.out.println(" \n" + "/////////////////////////" + "\n");
		
	}
	
	
	public static void visualizzaAltezzaSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloAltezzaSoluzione solPan = new PannelloAltezzaSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	public static void visualizzaSfasamentoSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloSfasamentoSoluzione solPan = new PannelloSfasamentoSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
}

//distribuire i rect nel tempo (prima dell'algoritmo viene fatto un preprocessing dove i rect vengono distribuiti in modo uniforme nel tempo)
//per fare ciò, prima si mettono i rect uno dopo l'altro (separati oppure no), poi si fa un mini annealing che cerca di separarli meglio
//il prof vuole un algo che crea tot soluzioni casuali e usa la migliore come punto di partenza	(tanto vale usare il SA per traslarli, no?)
//all'aumentare delle richieste urgenti, il preprocessing funziona sempre meno

//implementato un criterio secondario per la scelta dove si privilegiano le soluzioni con rect di base più larga/meno sovrapposti

//aggiustare i parametri di temperatura, raffreddamento, passo (vedi qaunte iterazioni fa senza cambiare)	(cambiata implementazione)
//diminuire il passo al diminuire della temperatura non pare cambiare di molto la situazione (anzi, peggiora la soluzione)
//potrei pensare di spostare maggiormente i rect che sono più vicini al punto critico

//usa excell per i grafici
//controllare cosa fa ogni tot (ogni volta che cambia, fatto) 


//scrivere codice dove si gestisce una soluzione non valida prima di mandarla al BB: if (!feasibleSolution){ prendi i rettangoli che si intersecano nel punto
//critico e usa il BB solo su di loro. se non funziona, vuol dire che non esiste una soluzione...}
//la criticità sta nel fatto che non so gestire quando esiste una soluzione accettabile ma il SA non la trova, perchè potrebbe esplodere il BB
//(potrei pensare di far aumentare pian piano il valore ottimo da usare nel BB)
//(il BB non mi dà l'ottimo per quanto riguarda il costo secondario!) shhh


//10*(n/p) Vn in (4, 20), Vp in (0, 25, 50, 75) prendi tempo e valore






//potrei far discendere Soluzione e PianoBB da Piano e far discendere da Richiesta RichiestaDaSistemare e Rettangolo
