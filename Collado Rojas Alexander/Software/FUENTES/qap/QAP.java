/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qap;

import java.util.Scanner;
import java.util.Random;

/**
 * Alexander Collado Rojas Y7412507N
 * Problema a desarrollar
 */
public class QAP {

    public static void main(String[] args) {
        Reader instancia = new Reader();
        
        Scanner scannerAlg = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        
        int algoritmoElegido = -1;
        long tiempoInicial = 0;
        long tiempoFinal = 0;
       
        
        System.out.println("0 - Algoritmo Greedy");
        System.out.println("1 - Algoritmo Busqueda Local  \n");
        
        do{
            System.out.println("Elige cual algoritmo quieres usar: ");
            algoritmoElegido = scannerAlg.nextInt();
        }while(algoritmoElegido >= 2 || algoritmoElegido <= -1);//Por ahora que solo son 2 algoritmos
        
        System.out.println("Elige el archivo que desees usar: ");
        /*
        for(int i = 0; i < instancia.obtenerTodosArchivos().length; i++){
            System.out.println( i + " - "  + instancia.obtenerTodosArchivos()[i]);
        }
        
        int archivo = scanner.nextInt();   
        */
        
        String archivo = scanner.nextLine();
        
        //instancia.establecerNombreArchivo(instancia.obtenerTodosArchivos()[archivo]);
        instancia.establecerNombreArchivo(archivo);
        instancia.leerMatrices();
        int[] vectorSolucion = new int[instancia.getTamanioMatriz()];
        
        Greedy instanciaGeedy = new Greedy(instancia.getTamanioMatriz(), instancia.getMatrizFlujo(), instancia.getMatrizDistancia()); 
        
        
        switch(algoritmoElegido){
            case 0: //Case Greedy
               
                tiempoInicial = System.nanoTime();
                vectorSolucion = instanciaGeedy.solucionGreedy();
                tiempoFinal = System.nanoTime(); 
                
                System.out.println("\n" + "El coste de la solucion es: " + calcularCosteSolucion(instancia.getMatrizFlujo(), instancia.getMatrizDistancia(), vectorSolucion));
                
                break;
            case 1: //Case Busqueda Local
                
                BL instanciaBL;
                
                int eleccion = -1;
                System.out.println("0 - Semilla Greedy \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccion = scannerAlg.nextInt();
                }while(eleccion >= 2 || eleccion <= -1);
                
                if(eleccion == 0){//Usando semilla de la solucion del Greedy
                    
                    instanciaBL = new BL(instanciaGeedy.solucionGreedy(), instancia.getMatrizFlujo(), instancia.getMatrizDistancia());
                    
                    tiempoInicial = System.nanoTime();
                    vectorSolucion = instanciaBL.algoritmoBL();
                    tiempoFinal = System.nanoTime();
                }
                else{//Usando el generador de aleatorios
                    
                    int [] vectorBL = new int[instancia.getTamanioMatriz()];
                    vectorBL = generarVectorAleatorio(instancia.getTamanioMatriz());    
                    
                    instanciaBL = new BL(vectorBL, instancia.getMatrizFlujo(), instancia.getMatrizDistancia()); 
                    
                    tiempoInicial = System.nanoTime();
                    vectorSolucion = instanciaBL.algoritmoBL(); 
                    tiempoFinal = System.nanoTime();
                }
                
                System.out.println("Coste Busqueda Local: " + calcularCosteSolucion(instancia.getMatrizFlujo(), instancia.getMatrizDistancia(), vectorSolucion));
                
                
                
                
                break;
        }
        
        scanner.close();  
        
        long tiempo = (tiempoFinal - tiempoInicial) / 1000;
        
        System.out.println("Tiempo de ejecucion: " + tiempo + " microsegundos" );
        System.out.println("Permutacion Solucion: ");
        System.out.println("ARCHIVO ELEGIDO: " + archivo);
        
        for(int i = 0; i < vectorSolucion.length; i++){
            vectorSolucion[i]++;
            System.out.print(vectorSolucion[i] + ", ");
        }
        
        
    }
    
    //Funciona correctamente con el greedy
    public static int calcularCosteSolucion(int [][] matrizFlujo, int[][] matrizDistancia ,int [] vectorPermutado){
        
        int costo = 0;
        
        for(int i = 0; i < vectorPermutado.length; i++){
            int l = vectorPermutado[i];
            
            for(int j = 0; j < vectorPermutado.length; j++){
                
                if(j!=i){
                    
                    int k = vectorPermutado[j];
                    costo += matrizFlujo[i][j] * matrizDistancia[l][k];
                            
                }
            }
        }
        
        return costo;
        
    }
    
    public static int [] generarVectorAleatorio(int tamanio){
        
        int[] vectorGenerado = new int[tamanio];
        Random random = new Random();
        
        for (int i = 0; i < tamanio; i++) {
          int value;
          do {
            value = random.nextInt(tamanio) + 1;
          } while (contiene(vectorGenerado, value));
          vectorGenerado[i] = value;
        }

        for(int i = 0; i < tamanio; i++){
            vectorGenerado[i] -= 1; //Necesario porque sino el bucle se me va al infinito
        }
        
        return vectorGenerado;
    }
    
    private static boolean contiene(int[] vector, int value) {
        for (int i = 0; i < vector.length; i++) {
          if (vector[i] == value) {
            return true;
          }
        }
        return false;
  }
    
}
