import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AlgEvolutivoEstacionario_Clase02_Grupo01 {

    private Configurador configurador;
    private CargaDatos datos;
    private GestionaLog logs;
    private Random semilla;
    private Integer tam_poblacion;
    private Double cantidad_random_inicial;
    private Double prob_mutacion;
    private Integer condicion_parada_iteraciones;
    private Integer condicion_parada_tiempo;
    private String tipo_cruce;
    private Integer probSelecOX2;
    private Double[][] m_adyacencias;
    private Double[][] poblacion;
    private Double[][] poblacion_padres;
    private Double[][] poblacion_descendientes;
    Integer numero_ciudades;

    public AlgEvolutivoEstacionario_Clase02_Grupo01(Configurador configurador, CargaDatos datos, GestionaLog logs, Random semilla){

        this.configurador = configurador;
        this.datos = datos;
        this.logs = logs;
        this.semilla = semilla;
        tam_poblacion = configurador.getPoblacionInicial();
        cantidad_random_inicial = (configurador.getCantidadAleatoriosEnPoblacionInicial()*tam_poblacion)/100;
        prob_mutacion = configurador.getProbabilidadMutacion();
        condicion_parada_iteraciones = configurador.getCondicionParadaIteraciones();
        condicion_parada_tiempo = configurador.getCondicionParadaTiempo();
        tipo_cruce = configurador.cruce;
        probSelecOX2 = configurador.probSelecOX2;
        numero_ciudades = datos.getnCiudades();
        m_adyacencias = new Double[numero_ciudades][numero_ciudades];
        poblacion = new Double[tam_poblacion][numero_ciudades+1];
        poblacion_padres = new Double[2][numero_ciudades+1];
        poblacion_descendientes = new Double[2][numero_ciudades+1];


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
            }
        }

        for(int i = 0; i < 2; i++){
            for(int j = 1; j <= numero_ciudades; j++){
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
            }
            poblacion[i][numero_ciudades] = 0.0;
            poblacion_padres[0][numero_ciudades] = 0.0;
            poblacion_descendientes[0][numero_ciudades] = 0.0;
            poblacion_padres[1][numero_ciudades] = 0.0;
            poblacion_descendientes[1][numero_ciudades] = 0.0;
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



            generaPadres();
            generaDescendientes();
            reemplazaPoblacion();

            if(i % 1000 == 0){
                logs.registraLog("\n");
                logs.registraLog( "----------POBLACION(" + i + ")----------\n");
                for(int j = 0; j < tam_poblacion; j++){
                    logs.registraLog("(" + j + "): " + poblacion[j][numero_ciudades] + "\n");
                }
                logs.registraLog("\n");
                logs.registraLog("\n");
            }


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

        //Generamos el resto de soluciones de manera aleatoria
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
        for(int i = 0; i < 2; i++){
            ganador = Funciones_Auxiliares.kBest2(semilla, tam_poblacion, poblacion, numero_ciudades);
            for(int j = 0; j <= numero_ciudades; j++){
                poblacion_padres[i][j] = poblacion[ganador][j];
            }
        }

    }



    void generaDescendientes(){

        //Dependidendo del tipo de cruce establecido comenzamos un bucle u otro
        if(Objects.equals(tipo_cruce, "MOC")) {
            ArrayList<Double> hijo1= Funciones_Auxiliares.MOC(1, 0, semilla, numero_ciudades, poblacion_padres);
            for(int i = 0; i < numero_ciudades; i++){
                poblacion_descendientes[0][i] = hijo1.get(i);
            }

            ArrayList<Double> hijo2= Funciones_Auxiliares.MOC(0, 1, semilla, numero_ciudades, poblacion_padres);
            for(int i = 0; i < numero_ciudades; i++){
                poblacion_descendientes[1][i] = hijo2.get(i);
            }

            Double fitnes_H1 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, 0, m_adyacencias, numero_ciudades);
            poblacion_descendientes[0][numero_ciudades] = fitnes_H1;
            Double fitnes_H2 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, 1, m_adyacencias, numero_ciudades);
            poblacion_descendientes[1][numero_ciudades] = fitnes_H2;

            if(semilla.nextInt(0,101) < prob_mutacion){
                mutacion(0);
            }

            if(semilla.nextInt(0,101) < prob_mutacion){
                mutacion(1);
            }

        }else{
            Double[] hijo1 = Funciones_Auxiliares.OX2(0,1,numero_ciudades,semilla, poblacion_padres, probSelecOX2);
            for(int i = 0; i <= numero_ciudades; i++){
                poblacion_descendientes[0][i] = hijo1[i];
            }

            Double[] hijo2 = Funciones_Auxiliares.OX2(1,0,numero_ciudades,semilla, poblacion_padres, probSelecOX2);
            for(int i = 0; i <= numero_ciudades; i++){
                poblacion_descendientes[1][i] = hijo2[i];
            }

            Double fitnes_H1 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, 0, m_adyacencias, numero_ciudades);
            poblacion_descendientes[0][numero_ciudades] = fitnes_H1;
            Double fitnes_H2 = Funciones_Auxiliares.calculaFitness(poblacion_descendientes, 1, m_adyacencias, numero_ciudades);
            poblacion_descendientes[1][numero_ciudades] = fitnes_H2;
        }

        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(0);
        }

        if(semilla.nextInt(0,101) < prob_mutacion){
            mutacion(1);
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

        //Hacemos el torneo de perdedores y reemplazamos el individuo de élite que no se encuentre en la población
        for(int i = 0; i < 2; i++){

            int reemplaza = kWorst2();
            for(int j = 0; j <= numero_ciudades; j++){
                poblacion[reemplaza][j] = poblacion_descendientes[i][j];
            }

        }
        
    }


    Integer kWorst2() {
        Integer jugador_1;
        Integer jugador_2;


        //Damos valores a los participantes del torneo hasta que todos sean diferentes
        do {
            jugador_1 = semilla.nextInt(tam_poblacion);
            jugador_2 = semilla.nextInt(tam_poblacion);
        } while (jugador_1.equals(jugador_2));

        if (poblacion[jugador_1][numero_ciudades] > poblacion[jugador_2][numero_ciudades]) {
            return jugador_1;
        } else {
            return jugador_2;
        }
    }


}
