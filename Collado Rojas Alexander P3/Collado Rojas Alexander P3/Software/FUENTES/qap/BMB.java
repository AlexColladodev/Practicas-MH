package qap;

import java.util.Random;

public class BMB {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    private Random random;
    private int [] mejorSolucion;
    private BL instanciaBL;
    private QAP instanciaQAP = new QAP();
    
    BMB(int [][] matrizF, int[][] matrizD, int tamanio, Random rand){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = rand;
        
    }
    
    BMB(int [][] matrizF, int[][] matrizD, int tamanio){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = new Random();
        
    }
    
    int [] BMB(){
        
        int iteraciones = 25;
        int costeA;
        int costeB = Integer.MAX_VALUE;
        int [] vectorSolucionAux = new int [this.tamanioMatriz];
        
        for(int i = 0; i < iteraciones; i++){
            
            //Generar Solucion Aleatoria
            this.mejorSolucion = instanciaQAP.generarVectorAleatorio(this.tamanioMatriz, this.random);

            instanciaBL = new BL(this.mejorSolucion, this.matrizFlujo, this.matrizDistancia, 2000);

            //Aplicar BL(De la P1) a la solución anteriormente generada
            this.mejorSolucion = instanciaBL.algoritmoBL();
            
            //Actualizar solucion respecto al coste
            costeA = instanciaQAP.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, this.mejorSolucion);
            
            if(costeA < costeB){
                vectorSolucionAux = this.mejorSolucion;
                costeB = costeA;
            }
        }
        
        return vectorSolucionAux;
        
    }
    
}
