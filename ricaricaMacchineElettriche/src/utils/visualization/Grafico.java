package utils.visualization;

import org.jfree.chart.ChartPanel;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import utils.JSON;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class Grafico extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	private String titoloFinestra, titoloGrafico, x, y, path;
	
   public Grafico(String applicationTitle , String chartTitle, String x, String y, String p) throws JsonMappingException, JsonProcessingException {
      super(applicationTitle);
      
      titoloFinestra = applicationTitle;
      titoloGrafico = chartTitle;
      this.x = x;
      this.y = y;
      path = p;
      
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         x, y,
         createDataset(),
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
   }

   private DefaultCategoryDataset createDataset() throws JsonMappingException, JsonProcessingException {
	   	   
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );     
      Dataset datasetObj = JSON.caricaDatiProblema(path);   
      ArrayList<Dato> dati = datasetObj.dati;
      
      for(int i = 0;  i < dati.size(); i++) {
    	  Dato d = dati.get(i);
    	  dataset.addValue(d.y, d.tipoInfo, d.x);
      }
//      dataset.addValue( 15 , "schools" , "1970" );
      return dataset;
   }
   
   public void printPlot() throws JsonMappingException, JsonProcessingException {
	   Grafico chart = new Grafico(titoloFinestra, titoloGrafico, x, y, path);

      chart.pack( );
      RefineryUtilities.centerFrameOnScreen( chart );
      chart.setVisible( true );
   }
}