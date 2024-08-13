package utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import branchAndBound.PianificazioneBB;
import progetto.Pianificazione;
import progetto.RequestImpossibleException;
import progetto.Richiesta;
import simAnn.Rettangolo;
import simAnn.Soluzione;
import utils.visualization.Dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JSON {

	public static <T extends Object> T stringToObj(String input, Class<T> cls) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(input, cls);
	}
	
	
	public static String objToString(Object obj) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return  objectMapper.writeValueAsString(obj);
	}
	
	
	public static Soluzione caricaRichiestaPianificazione(String path) throws JsonMappingException, JsonProcessingException, RequestImpossibleException {
		//prende il file json e lo trasforma in una stringa
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//prende la stringa e la trasforma in una istanza
		Pianificazione istanza = stringToObj(data, Pianificazione.class);
		ArrayList<Rettangolo> rettangoli = new ArrayList<>();
		
		double maxSfasamento = istanza.getMassimaDifferenzaFasi();
		double maxAltezza = istanza.getMassimaPotenzaReteKw();
		
		//per ogni richiesta dell'istanza crea un rettangolo, con dimensioni fattibili
		for(Richiesta richiesta : istanza.getListaRichieste()) {
			Rettangolo rect = new Rettangolo(richiesta.identificativoMacchina, 
											 richiesta.fase, 
											 richiesta.minutoInizio, 
											 richiesta.minutoFine, 
											 richiesta.energia, 
											 richiesta.potenzaMassimaMacchina, 
											 richiesta.potenzaMinimaMacchina);
			rettangoli.add(rect);
		}
		
		Soluzione sol = new Soluzione(rettangoli, maxSfasamento, maxAltezza);
		return sol;
	}
	
	
	public static PianificazioneBB caricaPianificazioneBB(String path) throws JsonMappingException, JsonProcessingException, RequestImpossibleException {
		//prende il file json e lo trasforma in una stringa
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//prende la stringa e la trasforma in una istanza
		PianificazioneBB istanza = stringToObj(data, PianificazioneBB.class);
		return istanza;
	}
	
	
	
	
	
	
	
	
	
	public static void salvaIstanzaProblema(String path, String filename, Pianificazione istanza) throws JsonMappingException, JsonProcessingException {
		//salva il file nella cartella data
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            //se già esiste il file, ne crea un altro con un numero più alto
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename.substring(1);
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        //crea la stringa json dell'istanza
        String s = JSON.objToString(istanza);
        
        //modifica il file scrivendoci la stringa
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
		
	}
	
	
	public static void salvaSoluzione(String path, String filename, Pianificazione soluzione) throws JsonMappingException, JsonProcessingException {
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename;
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(soluzione);
        
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
	}
	
	
	
	
	
	
	//per quando vengono creati nuovi dati, si salva come varia un valore in funzione dell'altro
	public static void salvaDatiProblema(String path, String filename, Dataset dati) throws JsonMappingException, JsonProcessingException {
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            //se già esiste il file, ne crea un altro con un numero più alto
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename.substring(1);
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(dati);
        
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
	}
		
	//per prendere i dati e disegnare il grafico
	public static Dataset caricaDatiProblema(String path) throws JsonMappingException, JsonProcessingException {
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
				
		Dataset d = stringToObj(data, Dataset.class);
		return d;
	}
}