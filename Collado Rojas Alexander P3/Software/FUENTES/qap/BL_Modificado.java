package qap;

import java.util.Random;

//Modificarlo para que sea mediante posiciones i/j aleatorias y no en la busqueda de vecinos
public class BL_Modificado {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioVector;
    private Random random;
    private int iteracionesMaximas;
    int [] vectorPermutado;
    int costeSolucion;
    QAP instancia = new QAP();
    
    BL_Modificado(int [][] matrizF, int[][] matrizD, int tamanio, Random rand, int [] vector, int maxIter){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioVector = tamanio;
        this.random = rand;
        
        this.vectorPermutado = new int[this.tamanioVector];
        this.vectorPermutado = vector;
        
        this.iteracionesMaximas = maxIter;
        
    }
    
    BL_Modificado(int [][] matrizF, int[][] matrizD, int tamanio, int [] vector, int maxIter){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioVector = tamanio;
        random = new Random();
        
        this.vectorPermutado = new int[this.tamanioVector];
        this.vectorPermutado = vector;
        
        this.iteracionesMaximas = maxIter;
        
    }
    
    public int[] algoritmoBL(){
        boolean mejoraCoste = false;
        int i, j;
        
        //50 000 iteraciones o si no se encuentra mejora
        for(int k = 0; k < iteracionesMaximas; k++){
            
            do{
            i = random.nextInt(this.tamanioVector);
            j = random.nextInt(this.tamanioVector);
            }while(i == j);
                mejoraCoste = (factorizaBL(i, j) < 0);
                if(mejoraCoste){
                    aplicarMovimiento(i, j, factorizaBL(i, j));
                }
        }
        
        return this.vectorPermutado;
    }
    
    public int[] algoritmoBL(int k_vns){
        boolean mejoraCoste = false;
        int i, j;
        
        //50 000 iteraciones o si no se encuentra mejora
        for(int k = 0; k < iteracionesMaximas; k++){
            
            for(int t = 0; t < k_vns; t++){
                do{
                i = random.nextInt(this.tamanioVector);
                j = random.nextInt(this.tamanioVector);
                }while(i == j);
                    mejoraCoste = (factorizaBL(i, j) < 0);
                    if(mejoraCoste){
                        aplicarMovimiento(i, j, factorizaBL(i, j));
                    }
            }
        }
        
        return this.vectorPermutado;
    }

    
    //Funcion factorizacion que mira si la diferencia es positiva o negativa
    public int factorizaBL(int pos1, int pos2){
        int coste = 0;
        
        for(int i = 0; i < this.vectorPermutado.length; i++){
            if(i != pos1 && i != pos2){
                
                coste += ( this.matrizFlujo[pos1][i]*( this.matrizDistancia[this.vectorPermutado[pos2]][this.vectorPermutado[i]] - this.matrizDistancia[this.vectorPermutado[pos1]][this.vectorPermutado[i]]) +
                 this.matrizFlujo[pos2][i]*( this.matrizDistancia[this.vectorPermutado[pos1]][this.vectorPermutado[i]] - this.matrizDistancia[this.vectorPermutado[pos2]][this.vectorPermutado[i]]) +
                 this.matrizFlujo[i][pos1]*( this.matrizDistancia[this.vectorPermutado[i]][this.vectorPermutado[pos2]] - this.matrizDistancia[this.vectorPermutado[i]][this.vectorPermutado[pos1]]) +
                 this.matrizFlujo[i][pos2]*( this.matrizDistancia[this.vectorPermutado[i]][this.vectorPermutado[pos1]] - this.matrizDistancia[this.vectorPermutado[i]][this.vectorPermutado[pos2]]));
            }
        }
        
        return coste;
    }
    
    //Funcion de intercambio
    public void aplicarMovimiento(int i, int j, int costeFactor){
               
        int aux = this.vectorPermutado[i];
        this.vectorPermutado[i] = this.vectorPermutado[j];
        this.vectorPermutado[j] = aux;
        
        this.costeSolucion = instancia.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, this.vectorPermutado);
        
        this.costeSolucion += costeFactor; 
    }
}
