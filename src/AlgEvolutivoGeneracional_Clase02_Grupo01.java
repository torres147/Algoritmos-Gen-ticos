import java.io.File;
import java.util.*;


public class AlgEvolutivoGeneracional_Clase02_Grupo01 {

    private Configurador configurador;
    private CargaDatos datos;
    private GestionaLog logs;
    private Random semilla;
    private Integer tam_poblacion;
    private Double cantidad_random_inicial;
    private Integer cantidad_elites;
    private Double[][] individuos_elites;
    private Double prob_cruce;
    private Double prob_mutacion;
    private Integer condicion_parada_iteraciones;
    private Integer condicion_parada_tiempo;
    private String tipo_cruce;
    private Integer probSelecOX2;
    private Integer kbest;
    private final Double[][] m_adyacencias;

    //Para almacenar la solución usaremos una matriz de Double en la que el número de filas
    //es el tamaño de nuestra población y el de columnas es el número de ciudades del problema +1
    //asi, de esta forma alacenaremos en la primera columna de todas las filas la evaluació de dicha solución
    private Double[][] poblacion;
    private Double[][] poblacion_padres;
    private Double[][] poblacion_descendientes;
    Integer numero_ciudades;


    public AlgEvolutivoGeneracional_Clase02_Grupo01(Configurador configurador, CargaDatos datos, GestionaLog logs, Random semilla) {

        this.configurador = configurador;
        this.datos = datos;
        this.logs = logs;
        this.semilla = semilla;
        tam_poblacion = configurador.getPoblacionInicial();
        cantidad_random_inicial = (configurador.getCantidadAleatoriosEnPoblacionInicial()*tam_poblacion)/100;
        cantidad_elites = configurador.getCantidadElites();

        prob_cruce = configurador.getProbabilidadCruceGeneracional();
        prob_mutacion = configurador.getProbabilidadMutacion();
        condicion_parada_iteraciones = configurador.getCondicionParadaIteraciones();
        condicion_parada_tiempo = configurador.getCondicionParadaTiempo();
        tipo_cruce = configurador.cruce;
        probSelecOX2 = configurador.probSelecOX2;
        kbest = configurador.kbest;
        numero_ciudades = datos.getnCiudades();
        individuos_elites = new Double[cantidad_elites][numero_ciudades+1];
        m_adyacencias = new Double[numero_ciudades][numero_ciudades];
        poblacion = new Double[tam_poblacion][numero_ciudades+1];
        poblacion_padres = new Double[tam_poblacion][numero_ciudades+1];
        poblacion_descendientes = new Double[tam_poblacion][numero_ciudades+1];

        for(int i = 0; i <cantidad_elites; i++){
            for(int j = 0; j <= numero_ciudades; j++){
                individuos_elites[i][j] = 0.0;
            }
        }


        //GUARDAMOS LA MATRIZ DE ADYACENCIAS
        for(int i = 0; i <numero_ciudades; i++){
            for(int j = 0; j <numero_ciudades; j++){
                m_adyacencias[i][j] = datos.getDistancias()[i][j];
            }
        }

        //INICIALIZAMOS LA PRIMERA PARTE DE LA POBLACIÓN (Soluciones del Greedy Aleatorio)
        for(int i = 0; i < tam_poblacion-cantidad_random_inicial; i++){
            for(int j = 1; j <= numero_ciudades; j++){
                poblacion[i][j] = 0.0;
                poblacion_padres[i][j] = 0.0;
                poblacion_descendientes[i][j] = 0.0;
            }
        }
        //INICIALIZAMOS LA SEGUNDA PARTE DE LA POBLACIÓN (Soluciones aleatorias)
        //
        // Al inicializar la parte aleatoria lo hacemos de manera que las ciudades estén en orden
        // así, después en cada una de las soluciones solo tenemos que hacer un 2-opt de ciudades random
        // el número de veces que queramos, en nuestro caso se hace tantas veces como ciudades haya
        //
        for(int i = tam_poblacion-cantidad_random_inicial.intValue(); i < tam_poblacion; i++){
            for(int j = 0; j < numero_ciudades; j++){
                poblacion[i][j] = j*1.0;
                poblacion_padres[i][j] = 0.0;
                poblacion_descendientes[i][j] = 0.0;
            }
            poblacion[i][numero_ciudades] = 0.0;
            poblacion_padres[i][numero_ciudades] = 0.0;
            poblacion_descendientes[i][numero_ciudades] = 0.0;
        }

        //CALCULAMOS LA DISTANCIA DE LAS CIUDADES EN ORDEN
        for(int i = 0; i < numero_ciudades-1; i++){
            poblacion[cantidad_random_inicial.intValue()][numero_ciudades] +=
                    m_adyacencias[poblacion[cantidad_random_inicial.intValue()][i].intValue()][poblacion[cantidad_random_inicial.intValue()][i+1].intValue()];
        }
        poblacion[cantidad_random_inicial.intValue()][numero_ciudades] +=
                m_adyacencias[poblacion[cantidad_random_inicial.intValue()][0].intValue()][poblacion[cantidad_random_inicial.intValue()][numero_ciudades-1].intValue()];

        //COPIAMOS LA DISTANCIA RESULTANTE EN EL RESTO DE SOLUCIONES
        for(int i = tam_poblacion-cantidad_random_inicial.intValue(); i < tam_poblacion; i++){
            poblacion[i][numero_ciudades] = poblacion[cantidad_random_inicial.intValue()][numero_ciudades];
        }

    }

    void ejecutar() {

        inicializaPoblacion();

        logs.registraLog("----------FITNESS DE LA POBLACIÓN INICIAL----------" + "\n");
        for(int i = 0; i < tam_poblacion; i++){
            logs.registraLog("(" + i + "): " + poblacion[i][numero_ciudades] + "\n");
        }

        Tiempos temp = new Tiempos();
        temp.comienza();
        for(int i = 0; i < condicion_parada_iteraciones; i++) {

            Integer[] elites = Funciones_Auxiliares.calculaElites(poblacion, numero_ciudades, tam_poblacion, cantidad_elites);
            for(int j = 0; j < cantidad_elites; j++){
                for(int k = 0; k <= numero_ciudades; k++) {
                    individuos_elites[j][k] = poblacion[elites[j]][k];
                }
            }

            if(i % 1000 == 0){
                logs.registraLog( "Elites en la iteracion " +  i + ": ");
                for(int j = 0; j < cantidad_elites; j++){
                    logs.registraLog( "(" + elites[j] + "): " + individuos_elites[j][numero_ciudades]  + "  ");
                }
                logs.registraLog("\n");
                logs.registraLog( "----------POBLACION---------- ");
                for(int j = 0; j < tam_poblacion; j++){
                    logs.registraLog("(" + j + "): " + poblacion[j][numero_ciudades] + "\n");
                }
                logs.registraLog("\n");
                logs.registraLog("\n");
            }

            generaPadres();
            generaDescendientes();
            reemplazaPoblacion();



            temp.acaba();
            if(temp.getTotal() > condicion_parada_tiempo){
                i = condicion_parada_iteraciones;
                temp.acaba();
            }

        }

        System.out.println();
        int mejor_individuo = -1;
        Double valor_mejor_individuo = Double.MAX_VALUE;
        for(int i = 0; i < tam_poblacion; i++){
            if(poblacion[i][numero_ciudades] < valor_mejor_individuo){
                mejor_individuo = i;
                valor_mejor_individuo = poblacion[i][numero_ciudades];
            }
        }

        logs.registraLog("El mejor individuo es " +  mejor_individuo  + " y tiene un fitness de " + valor_mejor_individuo + "\n");
        logs.registraLog("La ejecución ha tardado un total de " + temp.getTotal() + " segundos");


    }



    void inicializaPoblacion() {

        //Bucle para obtener las 5 primeras soluciones usando el algoritmo greedy
        for(int i = 0; i < tam_poblacion-cantidad_random_inicial; i++){
            AlgGreedyAleatorio_Clase02_Grupo01 alg_greedy = new AlgGreedyAleatorio_Clase02_Grupo01(configurador, datos, semilla, logs);
            alg_greedy.ejecutar();

            poblacion[i][numero_ciudades] = alg_greedy.getDistancia_total();
            for(int j = 0; j < numero_ciudades; j++){
                poblacion[i][j] = alg_greedy.getCamino_final().get(j).doubleValue();
            }
        }

        //Generamos el resto de soluciones de manera aleatoria (Generamos tantos 2opt como ciudades tiene el problema)
        for(int i = tam_poblacion-cantidad_random_inicial.intValue(); i < tam_poblacion; i++){
            int cambia_a;
            int cambia_b;
            Double aux;
            Double distancia = poblacion[i][numero_ciudades];
            for(int j = 0; j < numero_ciudades; j++){
                cambia_a = semilla.nextInt(numero_ciudades);
                cambia_b = semilla.nextInt(numero_ciudades);

                distancia = Funciones_Auxiliares.dos_opt(cambia_a,cambia_b,poblacion[i],distancia,m_adyacencias, numero_ciudades);

                aux = poblacion[i][cambia_a];
                poblacion[i][cambia_a] = poblacion[i][cambia_b];
                poblacion[i][cambia_b] = aux;
            }
            poblacion[i][numero_ciudades] = distancia;
        }

    }

    void generaPadres(){


        Integer ganador;

        //Comprobamos que kBest está establecido
        if(kbest == 2){
            for(int i = 0; i < tam_poblacion; i++){
                ganador = Funciones_Auxiliares.kBest2(semilla, tam_poblacion, poblacion, numero_ciudades);

                //Metemos al individuo ganador del torneo en la matriz de padres
                for(int j = 0; j <= numero_ciudades; j++){
                    poblacion_padres[i][j] = poblacion[ganador][j];
                }
            }
        }else{
            for(int i = 0; i < tam_poblacion; i++){
                ganador = kBest3();

                //Metemos al individuo ganador del torneo en la matriz de padres
                for(int j = 0; j <= numero_ciudades; j++){
                    poblacion_padres[i][j] = poblacion[ganador][j];
                }
            }
        }
    }

    Integer kBest3(){
        Integer jugador_1;
        Integer jugador_2;
        Integer jugador_3;

        //Damos valores a los participantes del torneo hasta que todos sean diferentes
        do {
            jugador_1 = semilla.nextInt(tam_poblacion);
            jugador_2 = semilla.nextInt(tam_poblacion);
            jugador_3 = semilla.nextInt(tam_poblacion);
        }while(jugador_1.equals(jugador_2) || jugador_1.equals(jugador_3) || jugador_2.equals(jugador_3));

        //Devolvemos la fila en la que se encuentra el ganador del torneo
        if(poblacion[jugador_1][numero_ciudades] < poblacion[jugador_2][numero_ciudades]){
            if(poblacion[jugador_1][numero_ciudades] < poblacion[jugador_3][numero_ciudades]){
                return jugador_1;
            }else {
                return jugador_3;
            }
        }else{
            if(poblacion[jugador_2][numero_ciudades] < poblacion[jugador_3][numero_ciudades]){
                return jugador_2;
            }else{
                return jugador_3;
            }
        }
    }

    void generaDescendientes(){

        //Dependidendo del tipo de cruce establecido comenzamos un bucle u otro

        //*
        // Basicamente la dos funciones "cruzaConMOC" y "cruzaConOX2" hacen lo mismo
        // pero está dividido en dos funciones porque a la hora de implementar el MOC
        // era ma fácil hacer que este devolviera un ArrayList<Double> en vez de un Double[]
        // que es lo que devuelve el OX2
        // *
        if(Objects.equals(tipo_cruce, "MOC")) {
            for (int i = 0; i < tam_poblacion; i = i+2) {
                cruzaConMOC(i,i+1);
            }
        }else{
            for(int i = 0; i < tam_poblacion; i = i+2){
                cruzaConOX2(i,i+1);
            }
        }

    }

    void cruzaConMOC(int padre1, int padre2){
        if(semilla.nextInt(0,101) < prob_cruce){

            //Generamos hijos
            ArrayList<Double> hijo_1 = Funciones_Auxiliares.MOC(padre1, padre2, semilla, numero_ciudades, poblacion_padres);
            ArrayList<Double> hijo_2 = Funciones_Auxiliares.MOC(padre2, padre1, semilla, numero_ciudades, poblacion_padres);
            for(int j = 0; j < numero_ciudades; j++){
                poblacion_descendientes[padre1][j] = hijo_1.get(j);
                poblacion_descendientes[padre2][j] = hijo_2.get(j);
            }

            //Calculamos fitness de los hijos
            Double fitnes_H1 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, padre1, m_adyacencias, numero_ciudades);
            poblacion_descendientes[padre1][numero_ciudades] = fitnes_H1;
            Double fitnes_H2 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, padre2, m_adyacencias, numero_ciudades);
            poblacion_descendientes[padre2][numero_ciudades] = fitnes_H2;

        }else{
            for(int j = 0; j <= numero_ciudades; j++){
                poblacion_descendientes[padre1][j] = poblacion_padres[padre1][j];
                poblacion_descendientes[padre2][j] = poblacion_padres[padre2][j];
            }
        }

        //Mutamos la primera solución guardada en descendientes
        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(padre1);
        }

        //Mutamos la segunda solución guardada en descendientes
        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(padre2);
        }
    }

    void cruzaConOX2(int padre1, int padre2){
        if(semilla.nextInt(101) < prob_cruce){

            //Generamos hijos
            Double[] hijo_1 = Funciones_Auxiliares.OX2(padre1, padre2, numero_ciudades, semilla, poblacion_padres, probSelecOX2);
            Double[] hijo_2 = Funciones_Auxiliares.OX2(padre2, padre1, numero_ciudades, semilla, poblacion_padres, probSelecOX2);
            for(int j = 0; j <= numero_ciudades; j++){
                poblacion_descendientes[padre1][j] = hijo_1[j];
                poblacion_descendientes[padre2][j] = hijo_2[j];
            }

            //Calculamos fitness de cada hijo
            Double fitnes_H1 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, padre1, m_adyacencias, numero_ciudades);
            poblacion_descendientes[padre1][numero_ciudades] = fitnes_H1;
            Double fitnes_H2 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, padre2, m_adyacencias, numero_ciudades);
            poblacion_descendientes[padre2][numero_ciudades] = fitnes_H2;

        }else{
            for(int j = 0; j <= numero_ciudades; j++){
                poblacion_descendientes[padre1][j] = poblacion_padres[padre1][j];
                poblacion_descendientes[padre2][j] = poblacion_padres[padre2][j];
            }

        }

        //Mutamos la primera solución guardada en descendientes
        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(padre1);
        }

        //Mutamos la segunda solución guardada en descendientes
        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(padre2);
        }
    }

    void mutacion(int fila){
        Integer pos_a;
        Integer pos_b;
        Integer aux;
        Double fitness = poblacion_descendientes[fila][numero_ciudades];
        do{
            pos_a = semilla.nextInt(numero_ciudades);
            pos_b = semilla.nextInt(numero_ciudades);
        }while(pos_a.equals(pos_b));

        poblacion_descendientes[fila][numero_ciudades] = Funciones_Auxiliares.dos_opt(pos_a, pos_b, poblacion_descendientes[fila], fitness, m_adyacencias, numero_ciudades);

        aux = poblacion_descendientes[fila][pos_a].intValue();
        poblacion_descendientes[fila][pos_a] = poblacion_descendientes[fila][pos_b];
        poblacion_descendientes[fila][pos_b] = aux.doubleValue();
    }

    void reemplazaPoblacion(){

        boolean[] marcaElites = new boolean[cantidad_elites];
        for(int i = 0; i < cantidad_elites; i++){
            marcaElites[i] = false;
        }

        //Marcamos si los individuos de la población mejoran a los individuos élites
        for(int i = 0; i < tam_poblacion; i++){
            for(int j = 0; j < cantidad_elites; j++){
                if(poblacion_descendientes[i][numero_ciudades] <= individuos_elites[j][numero_ciudades]){
                    marcaElites[j] = true;
                }
            }
        }

        //Hacemos el torneo de perdedores y reemplazamos el individuo de élite que no se encuentre en la población
        for(int i = 0; i < cantidad_elites; i++){
            if(!marcaElites[i]){
                int reemplaza = kWorst3();
                for(int j = 0; j <= numero_ciudades; j++){
                    poblacion_descendientes[reemplaza][j] = individuos_elites[i][j];
                }
            }
        }

        //Cambiamos los punteros de las matrices
        Double[][] m_aux = poblacion_descendientes;
        poblacion_descendientes = poblacion;
        poblacion = m_aux;

    }

    Integer kWorst3(){
        Integer jugador_1;
        Integer jugador_2;
        Integer jugador_3;

        //Damos valores a los participantes del torneo hasta que todos sean diferentes
        do {
            jugador_1 = semilla.nextInt(tam_poblacion);
            jugador_2 = semilla.nextInt(tam_poblacion);
            jugador_3 = semilla.nextInt(tam_poblacion);
        }while(jugador_1.equals(jugador_2) || jugador_1.equals(jugador_3) || jugador_2.equals(jugador_3));

        if(poblacion_descendientes[jugador_1][numero_ciudades] > poblacion_descendientes[jugador_2][numero_ciudades]){
            if(poblacion_descendientes[jugador_1][numero_ciudades] > poblacion_descendientes[jugador_3][numero_ciudades]){
                return jugador_1;
            }else {
                return jugador_3;
            }
        }else{
            if(poblacion_descendientes[jugador_2][numero_ciudades] > poblacion_descendientes[jugador_3][numero_ciudades]){
                return jugador_2;
            }else{
                return jugador_3;
            }
        }
    }



    public Double[][] getPoblacion() {
        return poblacion;
    }

    public Integer getNumero_ciudades() {
        return numero_ciudades;
    }

    public Double[][] getPoblacion_padres() {
        return poblacion_padres;
    }

    public Double[][] getPoblacion_descendientes() {
        return poblacion_descendientes;
    }

    public Double[][] getIndividuos_elites() {
        return individuos_elites;
    }

    public Integer getTam_poblacion() {
        return tam_poblacion;
    }

    public Integer getCantidad_elites() {
        return cantidad_elites;
    }
}
