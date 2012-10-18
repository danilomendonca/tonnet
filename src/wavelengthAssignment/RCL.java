package wavelengthAssignment;

import java.util.Vector;

import measurement.LinkMeasure;
import network.Link;
import routing.Route;

public class RCL extends WaveAlgo {
	 public boolean setWaveAssignment(int[]waveList, int[] freeWave, int category, boolean controlChannel) {
		    int index=0;
		    //Instancia o objeto do tipo vetor de links. Este vetor armazenara os links que a rota atual possui.
		    Vector<Link>  links = new Vector<Link> ();
		    
		    //Instancia o objeto do tipo vetor de links. Este vetor armazenara as medidas de todos os links
		    Vector<LinkMeasure>  TotallinksMeasure = new Vector<LinkMeasure> ();
		    
		    //Instancia o objeto do tipo vetor de linkMeasure. Este vetor armazenara as medidas apenas dos links da rota atual
		    Vector<LinkMeasure> linksMeasure = new Vector<LinkMeasure> ();
		    
		    //Instancia o objeto do tipo vetor de Route. Este vetor armazerana as rotas que passar por cada link
		    Vector<Route> routes = new Vector<Route> ();
		    
		    //Acessando a o objeto mesh, obtemos o valor da variavel que contem os links da rota atual
		    links=mesh.linkList_actual_route;
		    
		   
		    //Cacula as medidas dos links. Devera ser retirado!!!
		    mesh.calculateMeasureLink();
		    
		    //Obtem as medidas de todos os links
		    TotallinksMeasure=mesh.getMeasurements().getListLinkMeasure();
		    
		   // Atribuiremos a linkMeasure as medidas apenas dos links da rota atual
		    for(int k=0; k<TotallinksMeasure.size(); k++)
		    {	
		    	for(int i=0; i<links.size(); i++){
		    		String Teste2=links.get(i).getName();
		    		String Teste1=TotallinksMeasure.get(k).getName();
		    		if(Teste2.equals(Teste1))
		    		{
		    		linksMeasure.add(TotallinksMeasure.get(k));
		    	
		    	}//if
		    	}//for
		    	
		    }//for
		    int numero_rotas=0;
		  //Atribui a routes os rotas que passam pelo link 
		    for(int a=0; a<links.size(); a++)
		    {	
		    	routes=linksMeasure.get(a).getRoutes();
		    	numero_rotas=routes.size() + numero_rotas;
		    }
		    //Inicializa o contador que irá armazenar os comprimentos de ondas bloqueados em cada link
		    //. Nas colunas temos os links que serão o bloqueados. Nas linhas temos em quais links o comprimento de onda será bloqueado
		    int contador[][]=new int[freeWave.length][numero_rotas];
		    
		    //Zera o contador
		    for(int j=0; j<numero_rotas; j++)
		    for(int i=0; i<freeWave.length; i++)
		    {
		    	contador[i][j]=0;
		    }
		    
		    //Inicia o somador. O somador contem a soma da linha.
		    int somador[]=new int[freeWave.length];
		    
		    //Zera o somador.
		    for(int i=0; i<freeWave.length; i++)
		    {
		    	somador[i]=0;
		    }
		    
		    //Soma os elementos das colunas. Será usado para dividir o elemento de cada coluna.
		    int divisor[]=new int[numero_rotas];
		    
		    //Zera o divisor
		    for(int i=0; i<numero_rotas; i++)
		    {
		    	divisor[i]=0;
		    }
		    
		    int contador_rotas=0;
		    //Atribui a routes os rotas que passam pelo link 
		    for(int a=0; a<links.size(); a++)
		    {	
		    	routes=linksMeasure.get(a).getRoutes();
		    	for(int l=0; l<routes.size(); l++)
		    	{	
		    		//Ira armazenar os lambdas livres de cada rota
		    		int lambda[];
		    		contador_rotas++;
		    		lambda=routes.get(l).getAllWaveEmpty(false);
		    		for(int z=0;z<lambda.length;z++)
		    		{
		    			for(int w=0;w<freeWave.length;w++)
		    			{
		    			if(freeWave[w]==lambda[z])
		    			{	
		    				//l armazer o link e w o comprimento de onda
		    				contador[w][contador_rotas-1]++;
		    				divisor[contador_rotas-1]++;
		    				
		    			}//if
		    			}//for
		    		}//for
		    	}//for
		    	
		    }//for
		    
		    for(int w=0;w< freeWave.length;w++)
		    	for(int i=0;i< numero_rotas;i++)
		    	{	
		    		//se o divisor é igual a zero, igualamos a um para não realizarmos divisão por zero
		    		if(divisor[i]==0)
		    		{
		    			divisor[i]=1;
		    		}
		    		//realizamos a soma da linha
		    		somador[w]=somador[w] + contador[w][i]/divisor[i];
		    	}//for
		    
		   //Realiza a vrificação da linha que possui menor valor
		    for(int i=0; i<somador.length; i++)
		    {
		    	if(somador[i]<somador[index])
		    	{
		    		index=i;
		    	}//if
		    }//for
		    //Atribui o indice do lambda selecionado para cada link da rota atual
		    for (int i = 0; i < waveList.length; i++) {
		      waveList[i]= freeWave[index];
		    }
		    return true;
		  }//main
}//class
