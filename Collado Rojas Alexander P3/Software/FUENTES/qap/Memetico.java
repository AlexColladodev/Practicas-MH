/*
Por razones de orden, para el memetico he copiado directamente el AGG_PMX aca 
haciendo los cambios respectivos para los AMM
 */
package qap;

import java.util.Random;

/**
 *
 * @author alexc
 */
public class Memetico {
    

    
    private int [][] matrizFlujo;
    private int [][] matrizDistancia;
    private int tamanioMatriz;
    
    private QAP instancia;
    private int [][] poblacion;
    private final int tamanioPoblacion = 50;
    private Random rand;
    
    private final double probabilidadMutacionGenes = 0.01;
    private final double probabilidadCruceAGG = 0.7;
    private final double pLS = 0.1;
    
    private int evaluacion;
    private final int iter_max = 50000;
    private int generacion;
    private int iteracionesMaximas = 400; //Para BL
    
    private boolean [] vectorBinario;
    private int memetico;
    private int costeSolucion;
    
    //Random
    Memetico(int tamanioMatriz, int [][] matrizF, int [][] matrizD, int memetico){
        
        rand = new Random();
        this.tamanioMatriz = tamanioMatriz;
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.memetico = memetico;
        
        instancia = new QAP();
        
        this.poblacion = new int[tamanioPoblacion][this.tamanioMatriz];
        
        this.generarPoblacion();
        
        
        int generacion = 0;
        int evaluacion = 0;
        
        this.vectorBinario = new boolean [tamanioMatriz];
        
        for(int i = 0; i < tamanioMatriz; i++){
            vectorBinario[i] = false;
        }
        
        costeSolucion = 0;
        
    }
    
    //Semilla
    Memetico(int tamanioMatriz, int [][] matrizF, int [][] matrizD, int memetico, int [][] poblacion, Random random){
        
        instancia = new QAP();
        
        rand = random;
        
        this.matrizFlujo = matrizF;
        this.matrizDistancia = matrizD;
        this.tamanioMatriz = tamanioMatriz;
        this.memetico = memetico;
        
        
        this.poblacion = poblacion;
        
        int generacion = 0;
        int evaluacion = 0;
        
        this.vectorBinario = new boolean [tamanioMatriz];
        
        for(int i = 0; i < tamanioMatriz; i++){
            vectorBinario[i] = false;
        }
        
        costeSolucion = 0;
        
        
    }
    
    private void generarPoblacion(){
        for(int i = 0; i < tamanioPoblacion; i++){
            this.poblacion[i] = instancia.generarVectorAleatorio(this.tamanioMatriz);
        }
    }
    
    public int [] AM(){
        
        int [][] padres;
        int [][] hijos = new int [tamanioPoblacion][tamanioMatriz];
        PairGenetico<int[], int[]>[] parHijos = new PairGenetico[(tamanioPoblacion / 2)];

        
        double cruce = probabilidadCruceAGG * (tamanioPoblacion / 2);
        int cruceEsperado = (int) Math.round(cruce);
        
        double mutacion = (tamanioPoblacion * tamanioMatriz) * this.probabilidadMutacionGenes;
        int mutacionEsperada = (int) Math.round(mutacion);
        
        ordenarPoblacion();
        
        while(this.evaluacion < this.iter_max){
            
 
            /*################ SELECCION ################## */
            
            //Selecciono toda la poblacion
            padres = seleccion(this.poblacion, tamanioPoblacion);
            
            
            /*################ CRUCE ################## */
            
            //Hago los cruces - 17.5 --> 18 cruces
            int contador = 0;
            
            //Se generan un total de 36 nuevos hijos
            for(int j = 0; j < (cruceEsperado * 2) - 1; j+=2){
                parHijos[contador] = crucePMX(padres[j], padres[j+1]);
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
            
            
            this.generacion++;

            /*################ REEMPLAZO ################## */
            this.reemplazarPoblacionAGG(hijos);
            
            if(this.generacion % 10 == 0){
                switch(this.memetico){
                    case 0://AM All
                        AM_All();
                        break;
                    case 1://AM Rand
                        AM_Rand();
                        break;
                    case 2://AM Best
                        AM_Best();
                        break;
                } 
            }

            
        }
        ordenarPoblacion();
        return this.poblacion[0];//Se esta ordenado de tal manera que en la priemra posicion este el que menor coste tiene       
    }
    
    private void AM_All(){
            for(int i = 0; i < tamanioPoblacion; i++){
                    
                poblacion[i] = this.algoritmoBL(poblacion[i]);
                costeSolucion = 0;
                    
                for(int j = 0; j < tamanioMatriz; j++){
                    this.vectorBinario[j] = false;
                }
            }
        
    }
    
    private void AM_Rand(){
            double seleccion = tamanioPoblacion * this.pLS;
            int seleccionEsperada = (int) Math.round(seleccion);
            int cromosomaElegido;
            
            for(int i = 0; i < seleccionEsperada; i++){
                
                cromosomaElegido = rand.nextInt(tamanioPoblacion);
                    
                poblacion[cromosomaElegido] = this.algoritmoBL(poblacion[cromosomaElegido]);
                costeSolucion = 0;
                    
                for(int j = 0; j < tamanioMatriz; j++){
                    this.vectorBinario[j] = false;
                }
            }
        
    }
    
    private void AM_Best(){//Ya viene ordenada del reemplazo
        
            double seleccion = tamanioPoblacion * this.pLS;
            int seleccionEsperada = (int) Math.round(seleccion);
            
            for(int i = 0; i < seleccionEsperada; i++){
                
                    
                poblacion[i] = this.algoritmoBL(poblacion[i]);
                costeSolucion = 0;
                    
                for(int j = 0; j < tamanioMatriz; j++){
                    this.vectorBinario[j] = false;
                }
            }
        
    }
    
    public void reemplazarPoblacionAGG(int [][] poblacionHijos){//Lo cambio para poder utilizarlo en memetico
        
        this.poblacion = ordenarPoblacion();
        
        int [] mejorPadre = this.poblacion[0];
        int costeMejorPadre = this.calcularCoste(this.poblacion, 0);
        
        this.poblacion = poblacionHijos;
        
        this.poblacion = ordenarPoblacion();
        int costePeorHijo = this.calcularCoste(this.poblacion, tamanioPoblacion - 1);
        
        if(costeMejorPadre < costePeorHijo)
            this.poblacion[tamanioPoblacion - 1] = mejorPadre;
        
    }
    
        
    private int calcularCoste(int [][] poblacionParametro, int posicion){
        this.evaluacion++;
        return instancia.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, poblacionParametro[posicion]);
    }  
    
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
    
    private int[] algoritmoBL(int [] hijo){
        boolean improve_flag = true;
        boolean mejoraEntorno = true;
        boolean mejoraCoste = false;
        
        //50 000 iteraciones o si no se encuentra mejora
        for(int k = 0; k < iteracionesMaximas && mejoraEntorno; k++){
            mejoraEntorno = false;
            
            //Bucle interno exploracion del vecindario -- Diapositivas Seminario
            for(int i = 0; i < this.tamanioMatriz; i++){
                if(this.vectorBinario[i] == false){
                    improve_flag = false;
                    
                    for(int j = 0; j < this.tamanioMatriz; j++){
                        mejoraCoste = (factorizaBL(i, j, hijo) < 0);
                        if(mejoraCoste){
                            aplicarMovimiento(i, j, factorizaBL(i, j, hijo), hijo);
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

        
        return hijo;
    }
    
    //Funcion factorizacion que mira si la diferencia es positiva o negativa
    private int factorizaBL(int pos1, int pos2, int [] hijo){
        int coste = 0;
        
        for(int i = 0; i < hijo.length; i++){
            if(i != pos1 && i != pos2){
                
                coste += ( this.matrizFlujo[pos1][i]*( this.matrizDistancia[hijo[pos2]][hijo[i]] - this.matrizDistancia[hijo[pos1]][hijo[i]]) +
                 this.matrizFlujo[pos2][i]*( this.matrizDistancia[hijo[pos1]][hijo[i]] - this.matrizDistancia[hijo[pos2]][hijo[i]]) +
                 this.matrizFlujo[i][pos1]*( this.matrizDistancia[hijo[i]][hijo[pos2]] - this.matrizDistancia[hijo[i]][hijo[pos1]]) +
                 this.matrizFlujo[i][pos2]*( this.matrizDistancia[hijo[i]][hijo[pos1]] - this.matrizDistancia[hijo[i]][hijo[pos2]]));
            }
        }
        this.evaluacion++;
        return coste;
    }
    
    //Funcion de intercambio
    private void aplicarMovimiento(int i, int j, int costeFactor, int [] hijo){
               
        int aux = hijo[i];
        hijo[i] = hijo[j];
        hijo[j] = aux;
        
        costeSolucion = instancia.calcularCosteSolucion(this.matrizFlujo, this.matrizDistancia, hijo);
        
        costeSolucion += costeFactor; 
    }
    
    public void setIteracionesMaximas(int iter){
        this.iteracionesMaximas = iter;
    }
}
