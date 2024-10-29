import java.util.Random;

public class AlgEvolutivoGeneracional_Clase02_Grupo01 {

    private Configurador configurador;
    private CargaDatos datos;
    private GestionaLog logs;
    private Random semilla;
    private Integer k_greedy;
    private Integer tam_poblacion;
    private Double cantidad_random_inicial;
    private Integer cantidad_elites;
    private Double prob_cruce;
    private Double prob_mutacion;
    private Integer condicion_parada_iteraciones;
    private Integer condicion_parada_tiempo;
    private Integer[][] poblacion;


    public AlgEvolutivoGeneracional_Clase02_Grupo01(Configurador configurador, CargaDatos datos, GestionaLog logs, Random semilla) {

        this.configurador = configurador;
        this.datos = datos;
        this.logs = logs;
        this.semilla = semilla;
        k_greedy = configurador.getK();
        tam_poblacion = configurador.getPoblacionInicial();
        cantidad_random_inicial = configurador.getCantidadAleatoriosEnPoblacionInicial();
        cantidad_elites = configurador.getCantidadElites();
        prob_cruce = configurador.getProbabilidadCruceGeneracional();
        prob_mutacion = configurador.getProbabilidadMutacion();
        condicion_parada_iteraciones = configurador.getCondicionParadaIteraciones();
        condicion_parada_tiempo = configurador.getCondicionParadaTiempo();

    }





}
