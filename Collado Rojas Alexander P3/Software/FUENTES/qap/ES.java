package qap;

import java.util.Random;


public class ES {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    private Random random;
    private int [] mejorSolucion;
    private QAP instanciaQAP = new QAP();
    private static final double MU = 0.3;
    private static final double PHI = 0.2;
    private double max_exitos;
    private int max_vecinos;
    private int numeroIteraciones;
    private double Beta;
    private static final int MAX_EV = 50000;
    private int [] s;
    
    
    ES(int [][] matrizF, int[][] matrizD, int tamanio, Random rand, int [] vector){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = rand;
        
        this.max_vecinos = 15 * this.tamanioMatriz;
        this.max_exitos = 0.1 * this.max_vecinos;
        this.numeroIteraciones = MAX_EV / this.max_vecinos;
        
        this.s = vector;
        
    }
    
    ES(int [][] matrizF, int[][] matrizD, int tamanio, Random rand){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = rand;
        
        this.max_vecinos = 15 * this.tamanioMatriz;
        this.max_exitos = 0.1 * this.max_vecinos;
        this.numeroIteraciones = MAX_EV / this.max_vecinos;
        
        this.s = instanciaQAP.generarVectorAleatorio(this.tamanioMatriz, this.random);
        
    }
    
    ES(int [][] matrizF, int[][] matrizD, int tamanio, int [] vector){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = new Random(); 
        
        this.max_vecinos = 15 * this.tamanioMatriz;
        this.max_exitos = 0.1 * this.max_vecinos;
        this.numeroIteraciones = MAX_EV / this.max_vecinos;
        
        this.s = vector;
        
    }
    
    ES(int [][] matrizF, int[][] matrizD, int tamanio){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = new Random(); 
        
        this.max_vecinos = 15 * this.tamanioMatriz;
        this.max_exitos = 0.1 * this.max_vecinos;
        this.numeroIteraciones = MAX_EV / this.max_vecinos;
        
        this.s = instanciaQAP.generarVectorAleatorio(this.tamanioMatriz);
        
    }
    
    public int [] ES(){
        
        int [] sp = new int [this.tamanioMatriz];
        double T0 = 0.0;
        double Tf = 0.0001; // 10e-4
        double T = 0.0;
        int coste = 0;
        
        int i, j;
        
        int exitosEnfriamiento;
        int vecinos;
        int costeS = 0;
        int costeBest = 0;
        int t = 0;
        
        int mejoraCoste = 0;
        
        this.mejorSolucion = this.s;
        
        int costeMejorSolucion = instanciaQAP.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, this.mejorSolucion);
        
        T0 = (MU * costeMejorSolucion) / (-Math.log(PHI));
        
        //TF siempre debe ser menor que T0, teniendo un valor muy cercano a 0
        if(T0 < Tf){
            Tf = T0 * Tf;
        }
        
        T = T0;
        
        Beta = (T0 - Tf) / (T0 * Tf * this.numeroIteraciones);
        
        do{
            exitosEnfriamiento = 0;
            vecinos = 0;
            
            while(exitosEnfriamiento < this.max_exitos && vecinos < this.max_vecinos){
                do{
                    i = random.nextInt(this.tamanioMatriz);
                    j = random.nextInt(this.tamanioMatriz);
                }while(i == j);
                
                //La diferencia de los costes
                mejoraCoste = factorizar(s, i, j);
                t++;


                sp = this.intercambioES(s, i, j);
                vecinos++;
                
                
                double U = random.nextDouble();
                
                //K = 1 siempre porque no se
                if((mejoraCoste < 0) || (U <= Math.exp(-mejoraCoste / T))){
                    s = sp;
                    exitosEnfriamiento++;
                    
                    costeS = instanciaQAP.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, s);
                    costeBest = instanciaQAP.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, this.mejorSolucion);
                    t++;
                    
                    if(costeS < costeBest)
                        this.mejorSolucion = s;
                }
            }
            
            if(exitosEnfriamiento == 0)
                break;
            
            T = T / (1 + Beta * T);
             
        }while(T > Tf && t < this.MAX_EV);
        
        
        return this.mejorSolucion;
    }
    
    //Solo es un intercambio
    private int [] intercambioES(int [] vector, int i, int j){
        int aux;
        int [] vectorReturn = vector.clone();
        
        aux = vectorReturn[i];
        vectorReturn[i] = vectorReturn[j];
        vectorReturn[j] = aux;
        
        return vectorReturn;
    }
    
    
    public int factorizar(int [] vectorPermutado, int pos1, int pos2){
        int coste = 0;
        
        for(int i = 0; i < vectorPermutado.length; i++){
            if(i != pos1 && i != pos2){
                
                coste += ( this.matrizFlujo[pos1][i]*( this.matrizDistancia[vectorPermutado[pos2]][vectorPermutado[i]] - this.matrizDistancia[vectorPermutado[pos1]][vectorPermutado[i]]) +
                 this.matrizFlujo[pos2][i]*( this.matrizDistancia[vectorPermutado[pos1]][vectorPermutado[i]] - this.matrizDistancia[vectorPermutado[pos2]][vectorPermutado[i]]) +
                 this.matrizFlujo[i][pos1]*( this.matrizDistancia[vectorPermutado[i]][vectorPermutado[pos2]] - this.matrizDistancia[vectorPermutado[i]][vectorPermutado[pos1]]) +
                 this.matrizFlujo[i][pos2]*( this.matrizDistancia[vectorPermutado[i]][vectorPermutado[pos1]] - this.matrizDistancia[vectorPermutado[i]][vectorPermutado[pos2]]));
            }
        }
        
        return coste;
    }
    
}
