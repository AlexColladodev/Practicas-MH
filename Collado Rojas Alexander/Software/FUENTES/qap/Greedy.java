/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Alexander Collado Rojas Y7412507N
 * Algoritmo Greedy
 */
public class Greedy {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    
    //Vectores de media por fila de flujo y distancia
    private int [] mediaFlujo;
    private int [] mediaDistancia;
    
    //Vector de dupla para representar la solucion
    Pair<Integer, Integer>[] solucion;
    private int [] solucionDefinitiva;
    
    public Greedy(int tamanioM, int [][] matrizF, int [][] matrizD){
        
        this.tamanioMatriz = tamanioM;
        this.matrizDistancia = new int[this.tamanioMatriz][this.tamanioMatriz];
        this.matrizFlujo = new int[this.tamanioMatriz][this.tamanioMatriz];
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        
        this.mediaDistancia = new int[this.tamanioMatriz];
        this.mediaFlujo = new int[this.tamanioMatriz];
        
    }  
    
    //METODO GREEDY
    public int [] solucionGreedy(){
          
        this.mediaDistancia = calcularMediaFila(this.matrizDistancia);
        this.mediaFlujo = calcularMediaFila(this.matrizFlujo);
        this.solucion = new Pair[this.tamanioMatriz];
        
        int[] cloneDistancia = this.mediaDistancia.clone();
        int[] cloneFlujo = this.mediaFlujo.clone();
       
         
        //Flujo de mayor a menor
        Arrays.sort(cloneFlujo);
        List<Integer> lista = Arrays.asList(Arrays.stream(cloneFlujo).boxed().toArray(Integer[]::new));
        Collections.reverse(lista);
        cloneFlujo = lista.stream().mapToInt(i -> i).toArray();
        
        //Distancia de menor a mayor
        Arrays.sort(cloneDistancia);
        
        
        for(int i = 0; i < this.tamanioMatriz; i++){
            solucion[i] = new Pair<>(buscarValorVector(cloneFlujo[i], this.mediaFlujo), buscarValorVector(cloneDistancia[i], this.mediaDistancia));
        }
        
        /* Solucion representada como dupla
        for(int i = 0; i < this.tamanioMatriz; i++)
            System.out.println("(" + this.solucion[i].getPrimero() + ", " + this.solucion[i].getSegundo() + ")");
        */
        
        this.arreglarVectorSolucion();
        
        return this.solucionDefinitiva; //Solo devuelvo la permutacion dada en un vector en donde la posicion del vector es U y el valor del vector es la posicion L
    }
    
    //Problema: Sobreescribo el vector de las medias para marcar que un valor ha sido utilizado
    private int buscarValorVector(int a, int [] vector){
        
        int resp = 0;
        boolean encontrado = false;
        
        for(int i = 0; i < vector.length && !encontrado; i++){
            if(a == vector[i]){
                encontrado = true;
                vector[i] = -1; //Marcar como valor ya leido
                resp = i;
            }
        }
        
        return resp;
    }
    
    //Para no representar la solucion como una dupla sino solo como un vector
    private void arreglarVectorSolucion(){  
        
        this.solucionDefinitiva = new int[this.tamanioMatriz];
        
        for(int i = 0; i < this.tamanioMatriz; i++){
            this.solucionDefinitiva[this.solucion[i].getPrimero()] = this.solucion[i].getSegundo();
        }
        /*
        for(int i = 0; i < this.tamanioMatriz; i++){
            System.out.print(this.solucionDefinitiva[i] + ", ");
        }*/
    }
    
    //Metodo necesario para las medias
    private int[] calcularMediaFila(int [][] matriz){
        
        int [] vectorSuma = new int[this.tamanioMatriz];
        
        for(int i = 0; i < this.tamanioMatriz; i++){
            for(int j = 0; j < this.tamanioMatriz; j++){
                vectorSuma[i] += matriz[i][j];
            }
        }
        
        return vectorSuma;
    }
    
    public int[] getVectorFlujo(){
        return this.mediaFlujo;
    }
    
    public int[] getVectorDistancia(){
        return this.mediaDistancia;
    }
    
    //Solo comprobando que las matrices se guarden bien
    public void imprimirMatrices(){
        System.out.println("Matriz Flujo:");
            for (int i = 0; i < this.tamanioMatriz; i++) {
                for (int j = 0; j < this.tamanioMatriz; j++) {
                    System.out.print(matrizFlujo[i][j] + " ");
                }
                System.out.println();
            }
            
        System.out.println("Matriz Distancia:");
            for (int i = 0; i < this.tamanioMatriz; i++) {
                for (int j = 0; j < this.tamanioMatriz; j++) {
                    System.out.print(matrizDistancia[i][j] + " ");
                }
                System.out.println();
            }            
        }
    
}
