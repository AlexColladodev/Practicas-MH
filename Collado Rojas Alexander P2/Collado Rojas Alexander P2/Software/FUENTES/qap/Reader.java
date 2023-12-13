
package qap;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Alexander Collado Rojas Y7412507N
 * Lector de los datos de entrada desde un archivo
 */
public class Reader {
        private int matrizFlujo[][] = null;
        private int matrizDistancia[][] = null;
        private int tamanioMatrices;
        private String archivo;
        
        
        public void establecerNombreArchivo(String archivo){
            this.archivo = archivo;
        }
        
        public void leerMatrices(){
            
            String rutaPre = "../Tablas/Instancias_QAP/"; //EJECUCION EN NETBEANS
           // String rutaPre = "../../Tablas/Instancias_QAP/"; //EJECUCION COMO EN EL MANUAL
            String rutaDef = rutaPre + this.archivo;
            
            try{
                File archivo = new File(rutaDef);
                Scanner scanner = new Scanner(archivo);
            
                //Leer el primer entero que es el tamanio de la matriz cuadrada e inicializarlas
                this.tamanioMatrices = scanner.nextInt();
                this.matrizFlujo = new int [tamanioMatrices][tamanioMatrices];
                this.matrizDistancia = new int [tamanioMatrices][tamanioMatrices];
                
               
                
            
                // Leer los valores de la primera matriz
                for (int i = 0; i < tamanioMatrices; i++) {
                    for (int j = 0; j < tamanioMatrices; j++) {
                        matrizFlujo[i][j] = scanner.nextInt();
                    }
                }
            
            
                // Leer los valores de la segunda matriz
                 for (int i = 0; i < tamanioMatrices; i++) {
                    for (int j = 0; j < tamanioMatrices; j++) {
                        matrizDistancia[i][j] = scanner.nextInt();
                    }
                }
            
                scanner.close();
            } 
            catch(FileNotFoundException e){
                System.out.println("Archivo no encontrado");
            }
        }
        
        //Metodo para comprobacion
        public void imprimirMatrices(){
        System.out.println("Matriz Flujo:");
            for (int i = 0; i < this.tamanioMatrices; i++) {
                for (int j = 0; j < this.tamanioMatrices; j++) {
                    System.out.print(matrizFlujo[i][j] + " ");
                }
                System.out.println();
            }
            
        System.out.println("Matriz Distancia:");
            for (int i = 0; i < this.tamanioMatrices; i++) {
                for (int j = 0; j < this.tamanioMatrices; j++) {
                    System.out.print(matrizDistancia[i][j] + " ");
                }
                System.out.println();
            }            
        }
        
        public int[][] getMatrizFlujo(){
            return this.matrizFlujo;
        }
        
        public int [][] getMatrizDistancia(){
            return this.matrizDistancia;
        }
        
        public int getTamanioMatriz(){
            return this.tamanioMatrices;
        }
        
        public String getNombreArchivo(){
            return this.archivo;
        }
        
        
        public  String[] obtenerTodosArchivos() {
        File carpeta = new File("../Tablas/Instancias_QAP/");
        File[] archivos = carpeta.listFiles();

        List<String> nombresArchivos = new ArrayList<>();

        for (File archivo : archivos) {
            if (archivo.isFile()) {
                nombresArchivos.add(archivo.getName());
            }
        }

        return nombresArchivos.toArray(new String[0]);
    }
        

        
}
