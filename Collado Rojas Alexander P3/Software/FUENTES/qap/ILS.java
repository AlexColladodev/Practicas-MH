package qap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class ILS {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    private Random random;
    private int [] mejorSolucion;
    private QAP instanciaQAP = new QAP();
    private BL_Modificado BLm;
    
    ILS(int [][] matrizF, int[][] matrizD, int tamanio, Random rand){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = rand;
        
    }
    
    ILS(int [][] matrizF, int[][] matrizD, int tamanio){
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanio;
        this.random = new Random();
        
    }
    
    public int [] ILS(){
        int costeA;
        int costeB = Integer.MAX_VALUE;
        
        int [] vectorIntermedio = new int [this.tamanioMatriz];
        
        vectorIntermedio = instanciaQAP.generarVectorAleatorio(this.tamanioMatriz, this.random);

        BLm = new BL_Modificado(this.matrizFlujo, this.matrizDistancia, this.tamanioMatriz, this.random, vectorIntermedio, 2000);
        
        this.mejorSolucion = BLm.algoritmoBL();
        
        for(int i = 0; i < 24; i++){
            
            //Mutacion
            vectorIntermedio = mutarVector(this.mejorSolucion);
            BLm = new BL_Modificado(this.matrizFlujo, this.matrizDistancia, this.tamanioMatriz, this.random, vectorIntermedio, 2000);
            vectorIntermedio = BLm.algoritmoBL();
            
            costeA = instanciaQAP.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, vectorIntermedio);
            
            if(costeA < costeB){
                this.mejorSolucion = vectorIntermedio;
                costeB = costeA;
            }
        
        }
        
        return this.mejorSolucion;
        
    }
    
    private int[] mutarVector(int[] vector) {

           int [] auxVec = vector.clone();
           int posicion = random.nextInt(auxVec.length);  // Seleccionar una posición aleatoria en el vector
           int t = auxVec.length / 3;  // Determinar cuántos elementos modificar a partir de la posición

           // Obtener los elementos que se modificarán
           List<Integer> elementosModificar = new ArrayList<>();
           for (int i = 0; i < t; i++) {
               elementosModificar.add(auxVec[(posicion + i) % auxVec.length]);  // Modo cíclico
           }

           shuffleList(elementosModificar);

           // Aplicar los nuevos valores al vector original
           for (int i = 0; i < t; i++) {
               auxVec[(posicion + i) % auxVec.length] = elementosModificar.get(i);
           }

           return auxVec;
       }

    
    private void shuffleList(List<Integer> list) {
        int n = list.size();

        for (int i = n - 1; i > 0; i--) {
            int j = this.random.nextInt(i + 1);
            Collections.swap(list, i, j);
        }
    }
    
}
