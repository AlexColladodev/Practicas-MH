/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qap;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

/**
 * Alexander Collado Rojas Y7412507N
 * Problema a desarrollar
 */
public class QAP {

    public static void main(String[] args) { 
        int totalArchivos = 20;
        
        Reader instancia[] = new Reader[totalArchivos];
        
        rellenarVectorArchivos(instancia);
        
        for(int i = 0; i < totalArchivos; i++){
            instancia[i].leerMatrices();
        }
        
        
        
        Scanner scannerAlg = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        
        int algoritmoElegido = -1;
        int tamanioPoblacion = 50;  
        
        //################ ELECCION ALGORITMO ################
        System.out.println("0 - Algoritmo Greedy");
        System.out.println("1 - Algoritmo Busqueda Local");
        System.out.println("2 - Algoritmo Genetico");
        System.out.println("3 - Algoritmo Memetico");
        System.out.println("4 - Algoritmo Busqueda Local Modificado");
        System.out.println("5 - Algoritmo Busqueda Multiarranque Basica");
        System.out.println("6 - Algoritmo ILS");
        System.out.println("7 - Algoritmo VNS");
        System.out.println("8 - Algoritmo de Enfriamiento Simulado");
        System.out.println("9 - Algoritmo ILS ES \n");
        
        do{
            System.out.println("Elige cual algoritmo quieres usar: ");
            algoritmoElegido = scannerAlg.nextInt();
        }while(algoritmoElegido >= 10 || algoritmoElegido <= -1);//Por ahora que solo son 3 algoritmos
        
        long [] tiempoInicial = new long[totalArchivos];
        long [] tiempoFinal = new long[totalArchivos];
        
        for(int i = 0; i < totalArchivos; i++){
            tiempoInicial[i] = 0;
            tiempoFinal[i] = 0;
        }
        
        
        switch(algoritmoElegido){
            case 0: //Case Greedy
                
                for(int i = 0; i < totalArchivos; i++){
                    
                    Greedy instanciaGeedy = new Greedy(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia()); 
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaGeedy.solucionGreedy();
                    tiempoFinal[i] = System.nanoTime(); 
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }  
                break;
        
            case 1: //Case Busqueda Local
                
                BL [] instanciaBL = new BL[totalArchivos];
                
                int eleccion = -1;
                
                //################ ELECCION SEMILLA PARA BL ################
                System.out.println("0 - Semilla Greedy \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccion = scannerAlg.nextInt();
                }while(eleccion >= 2 || eleccion <= -1);
                
                if(eleccion == 0){//Usando semilla de la solucion del Greedy
                    
                    for(int i = 0; i < totalArchivos; i++){
                        int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                        Greedy instanciaGeedy = new Greedy(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia());
                        instanciaBL[i] = new BL(instanciaGeedy.solucionGreedy(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia());
                        
                        tiempoInicial[i] = System.nanoTime();
                        vectorSolucion[i] = instanciaBL[i].algoritmoBL();
                        tiempoFinal[i] = System.nanoTime();
                        
                        mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        
                    }
                    
                    
                }
                else{//Usando el generador de aleatorios
                    
                    for(int i = 0; i < totalArchivos; i++){
                        int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                        int [] vectorBL = new int[instancia[i].getTamanioMatriz()];
                        vectorBL = generarVectorAleatorio(instancia[i].getTamanioMatriz());
                        
                        instanciaBL[i] = new BL(vectorBL, instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia()); 
                        
                        tiempoInicial[i] = System.nanoTime();
                        vectorSolucion[i] = instanciaBL[i].algoritmoBL(); 
                        tiempoFinal[i] = System.nanoTime();
                        
                        mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                    }    
                    
                }
                break;
        
            case 2:
                
                int eleccionGenetico = -1;
                int eleccionAGEAGG = -1;
                Genetico[] instanciaGenetico = new Genetico[totalArchivos];
                
                //################ ELECCION SEMILLA GENETICO ################
                System.out.println("0 - Semilla con BL \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionGenetico = scannerAlg.nextInt();
                }while(eleccionGenetico >= 2 || eleccionGenetico <= -1);
                
                //################ ELECCION ALGORITMO GENETICO ################
                System.out.println("0 - AGG Posicion \n1 - AGG PMX \n2 - AGE Posicion \n3 - AGE PMX");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionAGEAGG = scannerAlg.nextInt();
                }while(eleccionAGEAGG >= 4 || eleccionAGEAGG <= -1); 
                

                if(eleccionGenetico == 0){
                    
                    BL semillaBL;

                    for(int i = 0; i < totalArchivos; i++){
                        
                        Greedy instanciaGeedy = new Greedy(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia()); 
                        int [][] poblacion = new int [tamanioPoblacion][instancia[i].getTamanioMatriz()];
                        semillaBL = new BL(instanciaGeedy.solucionGreedy(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia());
                        
                        Random random = new Random(42);
                        
                        //Crear la poblacion Semilla
                        for (int j = 0; j < tamanioPoblacion; j++) {
                            int[] permutacion = semillaBL.algoritmoBL().clone();
                            shuffleArray(permutacion, random);
                            //System.out.println(Arrays.toString(permutacion));
                            poblacion[j] = permutacion;
                        }
   
                        instanciaGenetico[i] = new Genetico(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), poblacion, random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaGenetico[i] = new Genetico(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia()); 
                    }

                }
                switch(eleccionAGEAGG){
                    case 0:
                        for(int i = 0; i < totalArchivos; i++){
                            int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                            
                            tiempoInicial[i] = System.nanoTime();
                            vectorSolucion[i] = instanciaGenetico[i].AGG_posicion();
                            tiempoFinal[i] = System.nanoTime();
                            
                            mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        }
                        
                        break;
                    case 1:
                        for(int i = 0; i < totalArchivos; i++){
                            int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                            
                            tiempoInicial[i] = System.nanoTime();
                            vectorSolucion[i] = instanciaGenetico[i].AGG_pmx();
                            tiempoFinal[i] = System.nanoTime();
                            
                            mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        }
                        break;
                    case 2:
                        for(int i = 0; i < totalArchivos; i++){
                            int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                            
                            tiempoInicial[i] = System.nanoTime();
                            vectorSolucion[i] = instanciaGenetico[i].AGE_posicion();
                            tiempoFinal[i] = System.nanoTime();
                            
                            mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        }
                        break;
                    case 3:
                        for(int i = 0; i < totalArchivos; i++){
                            int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                            
                            tiempoInicial[i] = System.nanoTime();
                            vectorSolucion[i] = instanciaGenetico[i].AGE_pmx();
                            tiempoFinal[i] = System.nanoTime();
                            
                            mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        }
                        break;
                    }
                
                break;
                
            case 3:
                Memetico [] instanciaMemetico = new Memetico[totalArchivos];
                int eleccionMemetico = -1;
                int eleccionMemeticoSemilla = -1;
                
                //################ ELECCION SEMILLA GENETICO ################
                System.out.println("0 - Semilla con BL \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionMemeticoSemilla = scannerAlg.nextInt();
                }while(eleccionMemeticoSemilla >= 2 || eleccionMemeticoSemilla <= -1);
                
                //################ ELECCION ALGORITMO GENETICO ################
                System.out.println("0 - AMM_All \n1 - AMM_Rand \n2 - AMM_Best");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionMemetico = scannerAlg.nextInt();
                }while(eleccionMemetico >= 3 || eleccionMemetico <= -1); 
                
                if(eleccionMemeticoSemilla == 0){
                    
                    BL semillaBL;

                    for(int i = 0; i < totalArchivos; i++){
                        
                        Greedy instanciaGeedy = new Greedy(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia()); 
                        int [][] poblacion = new int [tamanioPoblacion][instancia[i].getTamanioMatriz()];
                        semillaBL = new BL(instanciaGeedy.solucionGreedy(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia());
                        
                        Random random = new Random(42);
                        
                        //Crear la poblacion Semilla
                        for (int j = 0; j < tamanioPoblacion; j++) {
                            int[] permutacion = semillaBL.algoritmoBL().clone();
                            shuffleArray(permutacion, random);
                            //System.out.println(Arrays.toString(permutacion));
                            poblacion[j] = permutacion;
                        }
   
                        instanciaMemetico[i] = new Memetico(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), eleccionMemetico, poblacion, random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaMemetico[i] = new Memetico(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), eleccionMemetico);
                    }
                }
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                            
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaMemetico[i].AM();
                    tiempoFinal[i] = System.nanoTime();
                            
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }
                break;
                
            case 4:
                
                BL_Modificado [] instanciaBLm = new BL_Modificado[totalArchivos];
                
                int eleccionBLMod = -1;
                
                //################ ELECCION SEMILLA PARA BL ################
                System.out.println("0 - Semilla Greedy \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionBLMod = scannerAlg.nextInt();
                }while(eleccionBLMod >= 2 || eleccionBLMod <= -1);
                
                if(eleccionBLMod == 0){//Usando semilla de la solucion del Greedy
                    
                    for(int i = 0; i < totalArchivos; i++){
                        int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                        Greedy instanciaGeedy = new Greedy(instancia[i].getTamanioMatriz(), instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia());
                        
                        Random random = new Random(42);
                        
                        instanciaBLm[i] = new BL_Modificado(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random, instanciaGeedy.solucionGreedy(), 50000);
                        
                        tiempoInicial[i] = System.nanoTime();
                        vectorSolucion[i] = instanciaBLm[i].algoritmoBL();
                        tiempoFinal[i] = System.nanoTime();
                        
                        mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                        
                    }
                    
                    
                }
                else{//Usando el generador de aleatorios
                    
                    for(int i = 0; i < totalArchivos; i++){
                        int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                        int [] vectorBLm = new int[instancia[i].getTamanioMatriz()];
                        vectorBLm = generarVectorAleatorio(instancia[i].getTamanioMatriz());
                        
                        instanciaBLm[i] = new BL_Modificado(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), vectorBLm, 50000); 
                        
                        tiempoInicial[i] = System.nanoTime();
                        vectorSolucion[i] = instanciaBLm[i].algoritmoBL(); 
                        tiempoFinal[i] = System.nanoTime();
                        
                        mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                    }    
                    
                }
                break;
                
            case 5:
                BMB [] instanciaBMB = new BMB[totalArchivos];
                
                int eleccionBMB = -1;
                
                //################ ELECCION SEMILLA PARA BMB ################
                System.out.println("0 - Semilla \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionBMB = scannerAlg.nextInt();
                }while(eleccionBMB >= 2 || eleccionBMB <= -1);
                
                if(eleccionBMB == 0){
                    
                    Random random = new Random(42);
                    
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaBMB[i] = new BMB(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaBMB[i] = new BMB(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz());
                    }  
                }
                
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaBMB[i].BMB();
                    tiempoFinal[i] = System.nanoTime();
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }
                break;
                
            case 6:
                ILS [] instanciaILS = new ILS[totalArchivos];
                
                int eleccionILS = -1;
                
                //################ ELECCION SEMILLA PARA ILS ################
                System.out.println("0 - Semilla \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionILS = scannerAlg.nextInt();
                }while(eleccionILS >= 2 || eleccionILS <= -1);
                
                if(eleccionILS == 0){
                    
                    Random random = new Random(42);
                    
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaILS[i] = new ILS(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                      instanciaILS[i] = new ILS(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz());
                    }
                }
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaILS[i].ILS();
                    tiempoFinal[i] = System.nanoTime();
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }  
                break;
            case 7:
                VNS [] instanciaVNS = new VNS[totalArchivos];
                
                int eleccionVNS = -1;
                
                //################ ELECCION SEMILLA PARA VNS ################
                System.out.println("0 - Semilla \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionVNS = scannerAlg.nextInt();
                }while(eleccionVNS >= 2 || eleccionVNS <= -1);
                
                if(eleccionVNS == 0){
                    
                    Random random = new Random(42);
                    
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaVNS[i] = new VNS(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaVNS[i] = new VNS(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz());
                    }
                }
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaVNS[i].VNS();
                    tiempoFinal[i] = System.nanoTime();
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }  
                break;
                
            case 8:
                ES [] instanciaES = new ES[totalArchivos];
                
                int eleccionES = -1;
                
                //################ ELECCION SEMILLA PARA VNS ################
                System.out.println("0 - Semilla \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionES = scannerAlg.nextInt();
                }while(eleccionES >= 2 || eleccionES <= -1);
                
                if(eleccionES == 0){
                    
                    Random random = new Random(42);
                    
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaES[i] = new ES(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaES[i] = new ES(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz());
                    }
                }
               
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaES[i].ES();
                    tiempoFinal[i] = System.nanoTime();
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }  
                break;
                
            case 9:
                ILS_ES [] instanciaILSES = new ILS_ES[totalArchivos];
                
                int eleccionILSES = -1;
                
                //################ ELECCION SEMILLA PARA VNS ################
                System.out.println("0 - Semilla \n1 - Aleatoria");
                
                do{
                    System.out.println("Cual opcion quieres elegir?");
                    eleccionILSES = scannerAlg.nextInt();
                }while(eleccionILSES >= 2 || eleccionILSES <= -1);
                
                
                if(eleccionILSES == 0){
                    
                    Random random = new Random(42);
                    
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaILSES[i] = new ILS_ES(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz(), random);
                    }
                }else{
                    for(int i = 0; i < totalArchivos; i++){
                        instanciaILSES[i] = new ILS_ES(instancia[i].getMatrizFlujo(), instancia[i].getMatrizDistancia(), instancia[i].getTamanioMatriz());
                    }
                }
                
                for(int i = 0; i < totalArchivos; i++){
                    int[][] vectorSolucion = new int[totalArchivos][instancia[i].getTamanioMatriz()];
                    
                    tiempoInicial[i] = System.nanoTime();
                    vectorSolucion[i] = instanciaILSES[i].ILS_ES();
                    tiempoFinal[i] = System.nanoTime();
                    
                    mostrarSalida(vectorSolucion[i], instancia[i], tiempoInicial[i], tiempoFinal[i]);
                }  
                break;
        }
        

        scanner.close();  
    }
    
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
    
    public static int [] generarVectorAleatorio(int tamanio, Random rand){
        
        int[] vectorGenerado = new int[tamanio];
        Random random = rand;
        
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
    
    private static void shuffleArray(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    public static void rellenarVectorArchivos(Reader [] instancia){
        Reader nombresArchivos = new Reader();
        String [] archivos = nombresArchivos.obtenerTodosArchivos();
        
        for(int i = 0; i < instancia.length; i++){
            instancia[i] = new Reader();
            instancia[i].establecerNombreArchivo(archivos[i]);
        }
    }
    
    public static void mostrarSalida(int[] vectorSolucion, Reader instancia, long tiempoInicial, long tiempoFinal){
        long tiempo = (tiempoFinal - tiempoInicial) / 1000000;
                        
        System.out.println("\n" + "ARCHIVO: " + instancia.getNombreArchivo() + "\n" + "COSTE: " + 
        calcularCosteSolucion(instancia.getMatrizFlujo(), instancia.getMatrizDistancia(), vectorSolucion));
                    
        System.out.println("Tiempo de ejecucion: " + tiempo + " milisegundos" );        
    }
}

/* Comentarios usados anteriormente */
        //instanciaGenetico.imprimirPoblacion(); // Comprobar que las poblaciones se generan bien CORRECTO
        //instanciaGenetico.imprimirCostes();
        
        //TESTEO Operadores cruce
        /*
        int [] padre1 = {13, 14, 2, 16, 9, 0, 6, 19, 10, 3, 5, 18, 4, 17, 8, 1, 12, 15, 7, 20, 11, 21};
        int [] padre2 = {9, 8, 16, 15, 13, 7, 12, 3, 10, 19, 5, 1, 17, 14, 6, 2, 18, 4, 0, 20, 11, 21};
        
        int [] hijo1 = new int[padre1.length];
        int [] hijo2 = new int[padre2.length];
        
        PairGenetico<int[], int[]> parHijos =instanciaGenetico.crucePosicion(padre1, padre2);
        
        
        for(int i = 0; i < 22; i++){
            System.out.print(parHijos.getPrimero()[i] + ", ");
        }
        System.out.println();
        for(int i = 0; i < 22; i++){
            System.out.print(parHijos.getSegundo()[i] + ", ");
        }
        */
        /*
        int [] hijo1 = new int[22];
        int [] hijo2 = new int[22];
        
        hijo1 = instanciaGenetico.crucePosicion(padre1, padre2).getPrimero();
        hijo2 = instanciaGenetico.crucePosicion(padre1, padre2).getSegundo();
                
        for(int i = 0; i < 22; i++){
            System.out.print(hijo1[i] + ", ");
        }
        System.out.println();
        for(int i = 0; i < 22; i++){
            System.out.print(hijo2[i] + ", ");
        }
        */
        
        //instanciaGenetico.crucePMX(padre1, padre2);
        
        //Testeo Bubble Sort
        /*
        PairGreedy<Integer, Integer>[] arreglo = new PairGreedy[(5)];
        arreglo[0] = new PairGreedy(42, 0);
        arreglo[1] = new PairGreedy(35, 1);
        arreglo[2] = new PairGreedy(11, 2);
        arreglo[3] = new PairGreedy(46, 3);
        arreglo[4] = new PairGreedy(25, 4);
        
        instanciaGenetico.bubbleSort(arreglo);
        for(int i = 0; i < 5; i++){
            System.out.println("coste: " + arreglo[i].getPrimero() + "posicion: " + arreglo[i].getSegundo());            
        }
        */
