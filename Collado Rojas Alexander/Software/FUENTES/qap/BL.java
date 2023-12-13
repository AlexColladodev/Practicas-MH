/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qap;

/**
 * Alexander Collado Rojas Y7412507N
 * Algoritmo BL
 * Junto con Factorizar y
 * Don't look bits
 * 
 * El vector aleatorio generado va del 0 a n - 1 
 * El vector Greedy semilla va del 0 al n -1
 * CAMBIAR SALIDA PARA QUE EL VECTOR SOLUCION SEA DE 1 A N
 */
public class BL {
    
    private int [] vectorPermutado;
    private boolean[] vectorBinario;
    private int tamanioVector;
    private static final int iteracionesMaximas = 50000;
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private QAP instancia = new QAP();
    private int costeSolucion;
    
    public BL(int[] vector, int[][] flujo, int [][] distancia){//Permutacion Greedy o permutacion aleatoria
        this.tamanioVector = vector.length;
        this.vectorPermutado = new int[this.tamanioVector];
        this.vectorPermutado = vector;
        this.costeSolucion = 0;
        
        this.vectorBinario = new boolean[this.tamanioVector];
        
        this.matrizDistancia = distancia;
        this.matrizFlujo = flujo;
        
        for(int i = 0; i < this.tamanioVector; i++){
            this.vectorBinario[i] = false;
        }
    }
    
    public int[] algoritmoBL(){
        boolean improve_flag = true;
        boolean mejoraEntorno = true;
        boolean mejoraCoste = false;
        
        //50 000 iteraciones o si no se encuentra mejora
        for(int k = 0; k < iteracionesMaximas && mejoraEntorno; k++){
            mejoraEntorno = false;
            
            //Bucle interno exploracion del vecindario -- Diapositivas Seminario
            for(int i = 0; i < this.tamanioVector; i++){
                if(this.vectorBinario[i] == false){
                    improve_flag = false;
                    
                    for(int j = 0; j < this.tamanioVector; j++){
                        mejoraCoste = (factorizaBL(i, j) < 0);
                        if(mejoraCoste){
                            aplicarMovimiento(i, j, factorizaBL(i, j));
                            this.vectorBinario[i] = false;
                            this.vectorBinario[j] = false;
                            improve_flag = true;
                            mejoraEntorno = true; //Se emcontro una mejora en el entorno
                        }
                    }
                    if(!improve_flag){
                        this.vectorBinario[i] = true;
                    }
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
