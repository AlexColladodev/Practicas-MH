/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author alexc
 */
public class Genetico {
    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    
    private QAP instancia = new QAP();
    private int [][] poblacion;
    private final int tamanioPoblacion = 50;
    private Random rand;
    
    private final int tamanioPadreAGE = 2;
    
    private final double probabilidadMutacionGenes = 0.01;
    private final double probabilidadCruceAGG = 0.7;
    private final double probabilidadCruceAGE = 1;
    
    private int evaluacion;
    private final int iter_max = 50000;
    private int generacion;
    
    Genetico(int tamanioMatriz){
        this.tamanioMatriz = tamanioMatriz;
    }
    
    Genetico(int tamanioMatriz, int[][] matrizF, int[][] matrizD){
        
        rand = new Random();
        
        this.tamanioMatriz = tamanioMatriz;
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.evaluacion = 0;
        this.generacion = 0;
        
        poblacion = new int[tamanioPoblacion][tamanioMatriz];
        
        generarPoblacion();//De una vez que se construye la instancia se genera la poblacion inicial de 50
    }
    
    Genetico(int tamanioMatriz, int[][] matrizF, int[][] matrizD, int [][] poblacion, Random random){
        
        rand = random;
        
        this.tamanioMatriz = tamanioMatriz;
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.evaluacion = 0;
        
        this.poblacion = poblacion;
        this.generacion = 0;
        
        //generarPoblacion();//De una vez que se construye la instancia se genera la poblacion inicial de 50
    }
    
    
    private void generarPoblacion(){
        for(int i = 0; i < tamanioPoblacion; i++){
            this.poblacion[i] = instancia.generarVectorAleatorio(this.tamanioMatriz);
        }
    }
    
    //Los individuos con menor indice son los que mejor coste tienen.
    //Problema de MINIMIZAR costes
    
    /*
        Se asocia COSTE - POSICION
        Reordeno el vector de COSTE de menor a mayor. Este tendra asociado su par POSICION
        Un vectorAuxiliar ir ordenando respecto poblacion[getSegundo]
        Una vez todo ordenado
            poblacion = vectorAuxiliar;
    */
    public int[][] ordenarPoblacion(){//Cambiado a publico para usarlo en memetico
        PairGreedy<Integer, Integer>[] costeAsociadoI = new PairGreedy[(tamanioPoblacion)];
        int[][] vectorAuxiliar = new int[tamanioPoblacion][tamanioMatriz];
        
        for(int i = 0; i < tamanioPoblacion; i++){
            costeAsociadoI[i] = new PairGreedy(this.calcularCoste(poblacion, i), i);
        }
        
        this.bubbleSort(costeAsociadoI);
        
        for(int i = 0; i < tamanioPoblacion; i++){
            vectorAuxiliar[i] = this.poblacion[costeAsociadoI[i].getSegundo()];
        }
        
        this.poblacion = vectorAuxiliar;//Se ordeno la poblacion
        
        return this.poblacion;
        
    }
    
    public int [] AGG_posicion(){
        
        int [][] padres;
        int [][] hijos = new int [tamanioPoblacion][tamanioMatriz];
        PairGenetico<int[], int[]>[] parHijos = new PairGenetico[(tamanioPoblacion / 2)];
        
        double cruce = probabilidadCruceAGG * (tamanioPoblacion / 2);
        int cruceEsperado = (int) Math.round(cruce);
        
        double mutacion = (tamanioPoblacion * tamanioMatriz) * probabilidadMutacionGenes;
        int mutacionEsperada = (int) Math.round(mutacion);
        
        this.ordenarPoblacion();
        
        while(this.evaluacion < this.iter_max){
            
 
            /*################ SELECCION ################## */
            
            //Selecciono toda la poblacion
            padres = this.seleccion(poblacion, tamanioPoblacion);
            
            
            /*################ CRUCE ################## */
            
            //Hago los cruces - 17.5 --> 18 cruces
            int contador = 0;
            
            //Se generan un total de 36 nuevos hijos
            for(int j = 0; j < (cruceEsperado * 2) - 1; j+=2){
                parHijos[contador] = this.crucePosicion(padres[j], padres[j+1]);
                hijos[j] = parHijos[contador].getPrimero();
                hijos[j+1] = parHijos[contador].getSegundo();
                contador++;
            }                
            
            //Se rellenan las otras posiciones de las cuales no se ha hecho nada
            for(int k = cruceEsperado * 2; k < tamanioPoblacion; k++){
                hijos[k] = padres[k];
            }
            //Hasta aqui desde 0 - 35 nuevos hijos generados por cruce
            //                36 - 49 mismos elementos del padre
                    
           
            /*################ MUTACION ################## */
            
            int gen = 0;
            int individuo = 0;
            
            for(int j = 0; j < mutacionEsperada; j++){
                gen = rand.nextInt(tamanioMatriz);
                individuo = rand.nextInt(tamanioPoblacion);
                hijos[individuo] = mutacion(gen, hijos[individuo]);
            }               
            
            
            /*################ REEMPLAZO ################## */
            this.reemplazarPoblacionAGG(hijos);
            this.generacion++;
            
        }
        this.ordenarPoblacion();
        return this.poblacion[0];//Se esta ordenado de tal manera que en la priemra posicion este el que menor coste tiene
        
    }
    
    
    public int [] AGG_pmx(){
        
        int [][] padres;
        int [][] hijos = new int [tamanioPoblacion][tamanioMatriz];
        PairGenetico<int[], int[]>[] parHijos = new PairGenetico[(tamanioPoblacion / 2)];

        
        double cruce = probabilidadCruceAGG * (tamanioPoblacion / 2);
        int cruceEsperado = (int) Math.round(cruce);
        
        double mutacion = (tamanioPoblacion * tamanioMatriz) * this.probabilidadMutacionGenes;
        int mutacionEsperada = (int) Math.round(mutacion);
        
        this.ordenarPoblacion();
        
        while(this.evaluacion < this.iter_max){
            
 
            /*################ SELECCION ################## */
            
            //Selecciono toda la poblacion
            padres = this.seleccion(poblacion, tamanioPoblacion);
            
            
            /*################ CRUCE ################## */
            
            //Hago los cruces - 17.5 --> 18 cruces
            int contador = 0;
            
            //Se generan un total de 36 nuevos hijos
            for(int j = 0; j < (cruceEsperado * 2) - 1; j+=2){
                parHijos[contador] = this.crucePMX(padres[j], padres[j+1]);
                hijos[j] = parHijos[contador].getPrimero();
                hijos[j+1] = parHijos[contador].getSegundo();
                contador++;
            }             
            
            
            //Se rellenan las otras posiciones de las cuales no se ha hecho nada
            for(int k = cruceEsperado * 2; k < tamanioPoblacion; k++){
                hijos[k] = padres[k];
            }
            //Hasta aqui desde 0 - 35 nuevos hijos generados por cruce
            //                36 - 49 mismos elementos del padre     
           
            /*################ MUTACION ################## */
            
            int gen = 0;
            int individuo = 0;
            
            for(int j = 0; j < mutacionEsperada; j++){
                gen = rand.nextInt(tamanioMatriz);
                individuo = rand.nextInt(tamanioPoblacion);
                hijos[individuo] = mutacion(gen, hijos[individuo]);
            }               
            
            
            /*################ REEMPLAZO ################## */
            this.reemplazarPoblacionAGG(hijos);
            this.generacion++;
            
        }
        this.ordenarPoblacion();
        return this.poblacion[0];//Se esta ordenado de tal manera que en la priemra posicion este el que menor coste tiene       
    }
    
    public int [] AGE_posicion(){
        
        int [][] padres;
        int [][] hijos = new int [this.tamanioPadreAGE][tamanioMatriz];
        
        //Del resultado del cruce me da UN par de hijos de dos padres
        PairGenetico<int[], int[]>[] parHijos = new PairGenetico[(this.tamanioPadreAGE / 2)];
        
        double cruce = probabilidadCruceAGE * (this.tamanioPadreAGE / 2);
        int cruceEsperado = (int) Math.round(cruce);
        
        double mutacion = (this.tamanioPadreAGE * tamanioMatriz) * this.probabilidadMutacionGenes;
        int mutacionEsperada = (int) Math.round(mutacion);
        
        this.ordenarPoblacion();
        
        while(this.evaluacion < this.iter_max){
            
 
            /*################ SELECCION ################## */
            
            //Selecciono toda la poblacion
            padres = this.seleccion(poblacion, this.tamanioPadreAGE);
            
            
            /*################ CRUCE ################## */
            
            //Hago los cruces - 2 cruces
            int contador = 0;
            
            //Se generan un total de 2 nuevos hijos a partir de los dos padres
            for(int j = 0; j < cruceEsperado; j+=2){
                parHijos[contador] = this.crucePosicion(padres[j], padres[j+1]);
                hijos[j] = parHijos[contador].getPrimero();
                hijos[j+1] = parHijos[contador].getSegundo();
                contador++;
            }             
            
            /*################ MUTACION ################## */
            
            int gen = 0;
            int individuo = 0;
            
            for(int j = 0; j < mutacionEsperada; j++){
                gen = rand.nextInt(tamanioMatriz);
                individuo = rand.nextInt(this.tamanioPadreAGE);
                hijos[individuo] = mutacion(gen, hijos[individuo]);
            }               
            
            
            /*################ REEMPLAZO ################## */
            this.reemplazarPoblacionAGE(hijos);
            this.generacion++;
            
        }
        this.ordenarPoblacion();
        return this.poblacion[0];   
    }
    
    public int [] AGE_pmx(){
        
        int [][] padres;
        int [][] hijos = new int [this.tamanioPadreAGE][tamanioMatriz];
        
        //Del resultado del cruce me da UN par de hijos de dos padres
        PairGenetico<int[], int[]>[] parHijos = new PairGenetico[(this.tamanioPadreAGE / 2)];
        
        double cruce = probabilidadCruceAGE * (this.tamanioPadreAGE / 2);
        int cruceEsperado = (int) Math.round(cruce);
        
        double mutacion = (this.tamanioPadreAGE * tamanioMatriz) * this.probabilidadMutacionGenes;
        int mutacionEsperada = (int) Math.round(mutacion);
        
        this.ordenarPoblacion();
        
        while(this.evaluacion < this.iter_max){
            
 
            /*################ SELECCION ################## */
            
            //Selecciono toda la poblacion
            padres = this.seleccion(poblacion, this.tamanioPadreAGE);
            
            
            /*################ CRUCE ################## */
            
            //Hago los cruces - 2 cruces
            int contador = 0;
            
            //Se generan un total de 2 nuevos hijos a partir de los dos padres
            for(int j = 0; j < cruceEsperado; j+=2){
                parHijos[contador] = this.crucePMX(padres[j], padres[j+1]);
                hijos[j] = parHijos[contador].getPrimero();
                hijos[j+1] = parHijos[contador].getSegundo();
                contador++;
            }             
            
            /*################ MUTACION ################## */
            
            int gen = 0;
            int individuo = 0;
            
            for(int j = 0; j < mutacionEsperada; j++){
                gen = rand.nextInt(tamanioMatriz);
                individuo = rand.nextInt(this.tamanioPadreAGE);
                hijos[individuo] = mutacion(gen, hijos[individuo]);
            }               
            
            
            /*################ REEMPLAZO ################## */
            this.reemplazarPoblacionAGE(hijos);
            this.generacion++;
            
        }
        this.ordenarPoblacion();
        return this.poblacion[0];       
    }
    
   
    /*
    Seleccion por torneo binario eligiendo aleatoriamente dos individuos de la poblacion y seleccionar el mejor de ellos.
    En el generacional se aplicaran torneos como individuos de la poblacion existan, es decir 50
    En el estacionario solo se aplicaran dos torneos
    Se incluyen los individuos ganadores en al poblacion de padres
    */
    public int[][] seleccion(int [][]poblacionParametro, int cantidadPadres){
        
        int costeInd1;
        int costeInd2;
        
        int [][] padres;
        padres = new int[cantidadPadres][tamanioMatriz];
        
        int contadorPadre = 0;
        
        //Inicializarlas a 0 porque sino manda error
        int ind1 = 0;
        int ind2 = 0;
        
        
        //Seleccionar random dos individuos de la poblacion
        for(int i = 0; i < cantidadPadres; i++){
            ind1 = rand.nextInt(tamanioPoblacion);
            ind2 = rand.nextInt(tamanioPoblacion);
        
        
            costeInd1 = this.calcularCoste(poblacionParametro, ind1);
            costeInd2 = this.calcularCoste(poblacionParametro, ind2);

            //Dependiendo de cual tiene menor coste se devuelve uno u otro
            if(costeInd1 < costeInd2){
                padres[contadorPadre] = poblacionParametro[ind1];
                contadorPadre++;
            }else{
                padres[contadorPadre] = poblacionParametro[ind2];
                contadorPadre++;            
            }
        }
        
        //Esta seria la poblacion de padres con la cual se va a trabajar
        return padres;
         
    }
    
    public PairGenetico<int[], int[]> crucePosicion(int [] primerPadre, int [] segundoPadre){
        int [] hijo = new int [tamanioMatriz];
        //ArrayList<Integer> hijo = new ArrayList<Integer>();
        boolean [] elegido = new boolean[tamanioMatriz];
        int contador = 0;
        
        for(int i = 0; i < tamanioMatriz; i++){
            elegido[i] = false;
        }
        
        for(int i = 0; i < tamanioMatriz; i++){
            if (primerPadre[i] == segundoPadre[i]){
                hijo[i] = primerPadre[i]; //Da igual decir que el primero o el segundo porque contienen el mismo elemento
                //hijo.add(i, primerPadre[i]);   
                elegido[i] = true;
                contador++;
            }       
        }
        
        int[] hijoaux = hijo.clone();//Si me salto este paso los dos hijos dan el mismo resultado
        
        List<Integer> listaPermutar = new ArrayList<Integer>();
           
        for(int i = 0; i < tamanioMatriz; i++){
            if(!elegido[i]){
                listaPermutar.add(primerPadre[i]); //Lista con los valores que debe hacerse el aleatorio
            }            
        } 
        int [] hijo1 = generacionHijoPosicion(elegido, hijo, listaPermutar);
        int [] hijo2 = generacionHijoPosicion(elegido, hijoaux, listaPermutar);
        PairGenetico<int[], int[]> parHijos = new PairGenetico<>(hijo1, hijo2);
        
        return parHijos;
    }
    
    //Se toma el hijo y se rellena respecto a los valores de posicion aleatoria de la lista luego del shuffle
    private int[] generacionHijoPosicion(boolean [] elegido, int [] hijoOriginal, List<Integer> listaPermutar){
        int contadorLista = 0;
        int [] hijoResult = hijoOriginal;
        List<Integer> listaResult = listaPermutar;
        //Collections.shuffle(listaResult); SI COLOCABA ESTO ENTONCES ERRROR SI QUERIA SEMILLA
        shuffleList(listaResult);
        for(int i = 0; i < tamanioMatriz; i++){
            if(!elegido[i]){
                hijoResult[i] = listaResult.get(contadorLista);
                contadorLista++;
            }
        }
        
        return hijoResult;  
    }
    
    private void shuffleList(List<Integer> list) {
        int n = list.size();

        for (int i = n - 1; i > 0; i--) {
            int j = this.rand.nextInt(i + 1);
            Collections.swap(list, i, j);
        }
    }
    
    public PairGenetico<int[], int[]> crucePMX(int[] primerPadre, int[] segundoPadre){
        int [] primerHijo = new int[tamanioMatriz];
        int [] segundoHijo = new int[tamanioMatriz];
        int posicionInicial = 0;
        int posicionFinal = 0;
        
        
        while(!(posicionInicial < posicionFinal)){
            //Genera un numero entre 0 y tamanioMatriz
            posicionInicial = rand.nextInt(tamanioMatriz);
            //Genera un numero entre posicionInicial y tamanioMatriz
            posicionFinal = rand.nextInt(tamanioMatriz - posicionInicial);
            posicionFinal += posicionInicial;
        }//Asegurarme que posicionInicial sea menor que posicionFinal
        
        
        for(int i = posicionInicial; i < posicionFinal + 1; i++){
            primerHijo[i] = segundoPadre[i];
            segundoHijo[i] = primerPadre[i];
        }
        
        //Correspondencia
        PairGreedy<Integer, Integer>[] correspondencia = new PairGreedy[(posicionFinal - posicionInicial) + 1];
        int contadorCorrespondencia = 0;
        
        for(int i = 0; i < tamanioMatriz; i++){
            if(i >= posicionInicial && i <= posicionFinal){
                correspondencia[contadorCorrespondencia] = new PairGreedy(segundoPadre[i], primerPadre[i]);
                contadorCorrespondencia++;
            }
        }
        
        primerHijo = auxiliarOperadorPMX(correspondencia, primerHijo, primerPadre, posicionInicial, posicionFinal, false);
        segundoHijo = auxiliarOperadorPMX(correspondencia, segundoHijo, segundoPadre, posicionInicial, posicionFinal, true);
        
        
        PairGenetico<int[], int[]> parHijos = new PairGenetico<>(primerHijo, segundoHijo);

        return parHijos;        
        
    }
    
    private int[] auxiliarOperadorPMX(PairGreedy<Integer, Integer>[] correspondencia, int [] hijo, int [] padre, int posicionInicial, int posicionFinal, boolean eleccion){
        int [] hijoResult = new int [tamanioMatriz];
        boolean elegido;
        int valorEnCorrespondencia;
        int auxiliarIndex = 0;
        
        for(int i = 0; i < tamanioMatriz; i++){
            if(i >= posicionInicial && i <= posicionFinal){
                hijoResult[i] = hijo[i];
            }
            else{
                hijoResult[i] = -1;
            }
        }
        
        //Si el parametro es
            //FALSE == hijo1 es decir correspondencia PRIMERO a SEGUNDO
            //TRUE == hijo2 es decir correspondencia SEGUNDO a PRIMERO
        if(eleccion == true){
            correspondencia = intercambiarParCorrespondencia(correspondencia);
        }
        
        for(int i = 0; i < tamanioMatriz; i++){
            elegido = false;
            valorEnCorrespondencia = padre[i];
            
            if(i < posicionInicial || i > posicionFinal){//Ejecuto correspondencia
                    while(!elegido){
                        auxiliarIndex = posicionEnCorrespondencia(correspondencia, valorEnCorrespondencia);
                        if(auxiliarIndex != -1){
                            //Toma el valor que le corresponde respecto al valor que se busca del padre.
                            valorEnCorrespondencia = correspondencia[posicionEnCorrespondencia(correspondencia, valorEnCorrespondencia)].getSegundo();

                            if(!valorRepetido(hijoResult, valorEnCorrespondencia)){
                                hijoResult[i] = valorEnCorrespondencia;
                                elegido = true;
                            }
                        }else{
                            hijoResult[i] = valorEnCorrespondencia;
                            elegido = true;
                        }    
                    }
                
            }
        }
        
        return hijoResult;
    }
    
    private PairGreedy<Integer, Integer>[] intercambiarParCorrespondencia(PairGreedy<Integer, Integer> [] correspondencia){
        PairGreedy<Integer, Integer>[] nuevaCorrespondencia = new PairGreedy[(correspondencia.length)];
        
        for(int i = 0; i < correspondencia.length; i++){
            nuevaCorrespondencia[i] = new PairGreedy(correspondencia[i].getSegundo(), correspondencia[i].getPrimero());
        }
        
        return nuevaCorrespondencia;
    }
    
    private int posicionEnCorrespondencia(PairGreedy<Integer, Integer> [] correspondencia, int valorBuscado){
        boolean encontrado = false;
        int valorEncontrado = -1;
        
        for(int i = 0; i < correspondencia.length && !encontrado; i++){
            if(valorBuscado == correspondencia[i].getPrimero()){
                encontrado = true;
                valorEncontrado = i;
            }
        }
        
        return valorEncontrado;
    }
    
    private boolean valorRepetido(int [] hijo, int valor){
        boolean repetido = false;
        
        for(int i = 0; i < tamanioMatriz; i++){
            if(valor == hijo[i]){
                repetido = true;
            }
        }
        
        return repetido;
    }
    
    //Operador Interacambio del BL
    public int[] mutacion(int genAMutar, int [] individuoAMutar){
        
        int posicionPrimera = genAMutar;
        int posicionSegunda = -1;
        int intercambioPosicionPrimera;
        boolean salir = false;
        
        while(!salir){
            intercambioPosicionPrimera = rand.nextInt(tamanioMatriz);
            posicionSegunda = buscarPosicion(individuoAMutar, intercambioPosicionPrimera);
            
            if(!(posicionPrimera == posicionSegunda)){
                salir = true;
            }
        }  
               
        int aux = individuoAMutar[posicionPrimera];
        individuoAMutar[posicionPrimera] = individuoAMutar[posicionSegunda];
        individuoAMutar[posicionSegunda] = aux;
        
        return individuoAMutar;
        
    }
    
    private int buscarPosicion(int [] vector, int valor){
        boolean encontrado = false;
        int result = -1;
        for(int i = 0; i < vector.length && !encontrado; i++){
            if(valor == vector[i]){
                encontrado = true;
                result = i;
            }
        }
        
        return result;
    }
    
    //La poblacion de hijos reemplaza al de los padres, exceptuando la peor de los hijos se reemplaza por la mejor de la de los padres
    public void reemplazarPoblacionAGG(int [][] poblacionHijos){
        
        this.ordenarPoblacion();
        
        int [] mejorPadre = this.poblacion[0];
        int costeMejorPadre = this.calcularCoste(this.poblacion, 0);
        
        this.poblacion = poblacionHijos;
        
        this.ordenarPoblacion();
        int costePeorHijo = this.calcularCoste(this.poblacion, tamanioPoblacion - 1);
        
        if(costeMejorPadre < costePeorHijo)
            this.poblacion[tamanioPoblacion - 1] = mejorPadre;
        
    }
    
    public void reemplazarPoblacionAGE(int [][] hijos){
        this.ordenarPoblacion();
        
        int posicion1 = -1;
        int posicion2 = -1;
        
        List<int []> candidatos = new ArrayList<>();
        
        candidatos.add(hijos[0]);
        candidatos.add(hijos[1]);
        candidatos.add(this.poblacion[tamanioPoblacion - 1]);
        candidatos.add(this.poblacion[tamanioPoblacion - 2]);

        for (int i = 0; i < candidatos.size(); i++) {
            if (posicion1 == -1 || this.calcularCoste(candidatos.get(i)) < this.calcularCoste(candidatos.get(posicion1))) {
                posicion2 = posicion1;
                posicion1 = i;
            } else if (posicion2 == -1 || this.calcularCoste(candidatos.get(i)) < this.calcularCoste(candidatos.get(posicion2))) {
                posicion2 = i;
            }
        }
        
        this.poblacion[tamanioPoblacion - 2] = candidatos.get(posicion1);
        this.poblacion[tamanioPoblacion - 1] = candidatos.get(posicion2);
        
        this.ordenarPoblacion();

    }
    
    
    
    /*
    Tendra como parametros la poblacion a la cual querra calcular el coste
    La salida sera un vector con todos los costes
    Haciendo asi que la posicion n del vector de coste se relaciones con el individuo de la poblacion n
    */
    private int calcularCoste(int [][] poblacionParametro, int posicion){
        this.evaluacion++;
        return instancia.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, poblacionParametro[posicion]);
    }
    
    private int calcularCoste(int [] candidato){
        this.evaluacion++;
        return instancia.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, candidato);
    }
    
    
    private void bubbleSort(PairGreedy<Integer, Integer>[] arreglo) {
        int n = arreglo.length;
        PairGreedy<Integer, Integer>[] tmp = new PairGreedy[(1)];
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arreglo[j].getPrimero() > arreglo[j + 1].getPrimero()) {
                    // Intercambiar los elementos
                    tmp[0] = new PairGreedy(arreglo[j].getPrimero(), arreglo[j].getSegundo());   
                    arreglo[j] = arreglo[j + 1];
                    arreglo[j + 1] = tmp[0];
                }
            }
        }
    }
    
    public int getGeneracion(){
        return this.generacion;
    }
    
    //Comprobaciones
    public void imprimirPoblacion(){
        
        this.ordenarPoblacion();
        
        for(int i = 0; i < tamanioPoblacion; i++){
            System.out.println("Coste: " + this.calcularCoste(poblacion, i));
            for(int j = 0; j < tamanioMatriz; j++){
                System.out.print(poblacion[i][j] + ", ");
            }
            System.out.println();
        }
    }
    
    /*
    public void imprimirCostes(){
        
        int [] vectorCostes = new int [tamanioPoblacion];
        vectorCostes = this.calcularCoste(poblacion);
        
        for(int i = 0; i < tamanioPoblacion; i++){
            System.out.println(vectorCostes[i]);
        }
    }
    */
    
}


/*
COMPROBACION
        
        /* //Verificacion de Correspondencia Correcta
        System.out.println("Posicion Inicial Cruce: " + posicionInicial);
        System.out.println("Posicion Final Cruce " + posicionFinal);
        
        System.out.println("\n CORRESPONDENCIAS");
        
        for(int i = 0; i < contadorCorrespondencia; i++){
            System.out.println(correspondencia[i].getPrimero() + " corresponde a " + correspondencia[i].getSegundo());
        }
        
        System.out.println("\n NUEVA CORRESPONDENCIA");
        correspondencia = intercambiarParCorrespondencia(correspondencia);
        
        for(int i = 0; i < contadorCorrespondencia; i++){
            System.out.println(correspondencia[i].getPrimero() + " corresponde a " + correspondencia[i].getSegundo());
        }        
        
        
        System.out.println("POSICION INICIAL " + posicionInicial);
        System.out.println("POSICION FINAL " + posicionFinal);
        */

        /*
        int [] padre1 = {14, 10, 20, 6, 11, 4, 3, 13, 1, 2, 0, 15, 16, 12, 7, 9, 5, 8, 21, 17, 19, 18};
        int [] padre2 = {7, 9, 10, 2, 14, 18, 11, 13, 5, 6, 15, 21, 20, 16, 1, 0, 8, 19, 3, 4, 17, 12};        
        int [] hijo1 = {15, 5, 13, 2, 20, 21, 1, 14, 16, 8, 10, 9, 17, 18, 3, 11, 19, 4, 0, 6, 12, 7};
        int [] hijo2 = {3, 6, 10, 19, 13, 0, 8, 20, 9, 1, 7, 4, 12, 17, 15, 16, 14, 21, 11, 18, 5, 2};
        
        candidatos.add(padre1);
        candidatos.add(padre2);
        candidatos.add(hijo1);
        candidatos.add(hijo2);    
        */

        /*
        System.out.println("POSICION 1: " + posicion1);
        System.out.println("POSICION 2: " + posicion2);
        */
        

