package wavelengthAssignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import measurement.LinkMeasure;
import network.Link;
import routing.Route;

public class MaxSum extends WaveAlgo {
	/**
	   * método invocado pelas requisições
	   *Dado uma lista de comprimentos de onda livres Comprimentos de onda livres continuos.cenario sem conversao
	   *Seta em waveList quais os comprimentos de onda serao utilizados
	   * @param waveList int[] comprimentos de onda serao utilizados
	   * @param freeWave int[] lista de comprimentos de onda livres
	   * @param category int categoria do par
	   * @return boolean
	   */
	 public boolean setWaveAssignment(int[]waveList, int[] freeWave, int category, boolean controlChannel) {
		    int index=0;
		    Vector<Link>  links = new Vector<Link> ();
		    Vector<LinkMeasure>  TotallinksMeasure = new Vector<LinkMeasure> ();
		    Vector<LinkMeasure> linksMeasure = new Vector<LinkMeasure> ();
		    Vector<Route> routes = new Vector<Route> ();
		    Map <Integer, Integer> contador = new TreeMap <Integer, Integer>();

		    links=mesh.linkList_actual_route;
		    mesh.calculateMeasureLink();
		    TotallinksMeasure=mesh.getMeasurements().getListLinkMeasure();
		    
		    //getListLinkMeasure()
		    
		   // Vector<Route> routes=this.mesh.getroutes()
		    for(int k=0; k<TotallinksMeasure.size(); k++)
		    {	
		    	for(int i=0; i<links.size(); i++){
		    		String Teste2=links.get(i).getName();
		    		String Teste1=TotallinksMeasure.get(k).getName();
		    		int p=0;
					//String Teste2=links.get(i).getName();
		    	//if("TotallinksMeasure.get(k).getName()"=="links.get(i).getName()")
		    		if(Teste2.equals(Teste1))
		    	{
		    		linksMeasure.add(TotallinksMeasure.get(k));
		    	
		    	}//if
		    	}//for
		    	
		    }//for
		    
		    for(int a=0; a<links.size(); a++)
		    {	
		    	routes=linksMeasure.get(a).getRoutes();
		    	for(int l=0; l<routes.size(); l++)
		    	{
		    		int lambda[];
		    		
		    		lambda=routes.get(l).getAllWaveEmpty(false);
		    		Arrays.sort(freeWave); //garante a pesquisa binarySort
		    		for(int z=0;z<lambda.length;z++)
		    		{
		    			if(Arrays.binarySearch(freeWave, lambda[z]) >= 0)
		    				contador.put(z, contador.get(z) != null ? contador.get(z) : 1);		    			
		    		}
		    	}
		    	
		    }
		    
		    List <Integer> escolhido = new ArrayList<Integer>(contador.values());
		    Collections.sort(escolhido);
		    
		    if(!escolhido.isEmpty()){
			    for (int i = 0; i < waveList.length; i++) {
			      waveList[i]= escolhido.get(0);
			    }
			    return true;
		    }else
		    	return false;
		
		  }

}

