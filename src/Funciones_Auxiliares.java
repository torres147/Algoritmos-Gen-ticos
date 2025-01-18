import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class Funciones_Auxiliares {

    public Funciones_Auxiliares() {
    }

    public static double dos_opt(Integer a, Integer b, Vector<Integer> solucion, Double distancia_actual, Double[][] m_adyacencias,Integer num_datos, int iteraccion) {
        Double nueva_distancia = distancia_actual;
        Integer ciudad_a = solucion.get(a);
        Integer ciudad_b = solucion.get(b);

        if (a == (b + 1) || b == (a + 1)) {
            Pair<Integer, Integer> num = new Pair<>(0, 0);
            mayorYMenor(num, a, b);
            Integer peque = num.getSecond();
            Integer gran = num.getFirst();
            if (peque == 0) {
                int ciudad_despues_gran = solucion.get(gran + 1);
                nueva_distancia -= m_adyacencias[solucion.get(gran)][ciudad_despues_gran];
                nueva_distancia += m_adyacencias[solucion.get(peque)][ciudad_despues_gran];
            } else if (gran == (num_datos - 1)) {
                int ciudad_anterior_peque = solucion.get(peque - 1);
                nueva_distancia -= m_adyacencias[ciudad_anterior_peque][solucion.get(peque)];
                nueva_distancia += m_adyacencias[ciudad_anterior_peque][solucion.get(gran)];
            } else {
                int ciudad_anterior_peque = solucion.get(peque - 1);
                int ciudad_despues_gran = solucion.get(gran + 1);
                nueva_distancia -= m_adyacencias[ciudad_anterior_peque][solucion.get(peque)];
                nueva_distancia += m_adyacencias[ciudad_anterior_peque][solucion.get(gran)];
                nueva_distancia -= m_adyacencias[solucion.get(gran)][ciudad_despues_gran];
                nueva_distancia += m_adyacencias[solucion.get(peque)][ciudad_despues_gran];
            }
        } else if (!Objects.equals(ciudad_a, ciudad_b)) {
            if (a == 0) {
                Integer siguiente_a = solucion.get(a + 1);
                nueva_distancia -= m_adyacencias[ciudad_a][siguiente_a];
                nueva_distancia += m_adyacencias[ciudad_b][siguiente_a];
            } else if (a == (num_datos - 1)) {
                Integer anterior_a = solucion.get(a - 1);
                nueva_distancia -= m_adyacencias[anterior_a][ciudad_a];
                nueva_distancia += m_adyacencias[anterior_a][ciudad_b];
            } else {
                Integer siguiente_a = solucion.get(a + 1);
                Integer anterior_a = solucion.get(a - 1);
                nueva_distancia -= (m_adyacencias[ciudad_a][siguiente_a] + m_adyacencias[ciudad_a][anterior_a]);
                nueva_distancia += (m_adyacencias[ciudad_b][siguiente_a] + m_adyacencias[ciudad_b][anterior_a]);
            }
            if (b == 0) {
                Integer siguiente_b = solucion.get(b + 1);
                nueva_distancia -= m_adyacencias[ciudad_b][siguiente_b];
                nueva_distancia += m_adyacencias[ciudad_a][siguiente_b];
            }else if (b == (num_datos - 1)) {
                Integer anterior_b = solucion.get(b - 1);
                nueva_distancia -= m_adyacencias[anterior_b][ciudad_b];
                nueva_distancia += m_adyacencias[anterior_b][ciudad_a];
            }else {
                Integer siguiente_b = solucion.get(b + 1);
                Integer anterior_b = solucion.get(b - 1);
                nueva_distancia -= (m_adyacencias[ciudad_b][siguiente_b] + m_adyacencias[ciudad_b][anterior_b]);
                nueva_distancia += (m_adyacencias[ciudad_a][siguiente_b] + m_adyacencias[ciudad_a][anterior_b]);
            }
        }

        return nueva_distancia;
    }


    public static double dos_opt(Integer a, Integer b, Double[] solucion, Double distancia_actual, Double[][] m_adyacencias,Integer num_datos) {
        Double nueva_distancia = distancia_actual;
        Integer ciudad_a = solucion[a].intValue();
        Integer ciudad_b = solucion[b].intValue();

        if (a == (b + 1) || b == (a + 1)) {
            Pair<Integer, Integer> num = new Pair<>(0, 0);
            mayorYMenor(num, a, b);
            Integer peque = num.getSecond();
            Integer gran = num.getFirst();
            if (peque == 0) {
                int ciudad_despues_gran = solucion[gran +1 ].intValue();
                nueva_distancia -= m_adyacencias[solucion[gran].intValue()][ciudad_despues_gran];
                nueva_distancia += m_adyacencias[solucion[peque].intValue()][ciudad_despues_gran];
            } else if (gran == (num_datos - 1)) {
                int ciudad_anterior_peque = solucion[peque-1].intValue();
                nueva_distancia -= m_adyacencias[ciudad_anterior_peque][solucion[peque].intValue()];
                nueva_distancia += m_adyacencias[ciudad_anterior_peque][solucion[gran].intValue()];
            } else {
                int ciudad_anterior_peque = solucion[peque-1].intValue();
                int ciudad_despues_gran = solucion[gran+1].intValue();
                nueva_distancia -= m_adyacencias[ciudad_anterior_peque][solucion[peque].intValue()];
                nueva_distancia += m_adyacencias[ciudad_anterior_peque][solucion[gran].intValue()];
                nueva_distancia -= m_adyacencias[solucion[gran].intValue()][ciudad_despues_gran];
                nueva_distancia += m_adyacencias[solucion[peque].intValue()][ciudad_despues_gran];
            }
        } else if (!Objects.equals(ciudad_a, ciudad_b)) {
            if (a == 0) {
                Integer siguiente_a = solucion[a+1].intValue();
                nueva_distancia -= m_adyacencias[ciudad_a][siguiente_a];
                nueva_distancia += m_adyacencias[ciudad_b][siguiente_a];
            } else if (a == (num_datos - 1)) {
                Integer anterior_a = solucion[a-1].intValue();
                nueva_distancia -= m_adyacencias[anterior_a][ciudad_a];
                nueva_distancia += m_adyacencias[anterior_a][ciudad_b];
            } else {
                Integer siguiente_a = solucion[a+1].intValue();
                Integer anterior_a = solucion[a-1].intValue();
                nueva_distancia -= (m_adyacencias[ciudad_a][siguiente_a] + m_adyacencias[ciudad_a][anterior_a]);
                nueva_distancia += (m_adyacencias[ciudad_b][siguiente_a] + m_adyacencias[ciudad_b][anterior_a]);
            }
            if (b == 0) {
                Integer siguiente_b = solucion[b+1].intValue();
                nueva_distancia -= m_adyacencias[ciudad_b][siguiente_b];
                nueva_distancia += m_adyacencias[ciudad_a][siguiente_b];
            }else if (b == (num_datos - 1)) {
                Integer anterior_b = solucion[b-1].intValue();
                nueva_distancia -= m_adyacencias[anterior_b][ciudad_b];
                nueva_distancia += m_adyacencias[anterior_b][ciudad_a];
            }else {
                Integer siguiente_b = solucion[b+1].intValue();
                Integer anterior_b = solucion[b-1].intValue();
                nueva_distancia -= (m_adyacencias[ciudad_b][siguiente_b] + m_adyacencias[ciudad_b][anterior_b]);
                nueva_distancia += (m_adyacencias[ciudad_a][siguiente_b] + m_adyacencias[ciudad_a][anterior_b]);
            }
        }

        return nueva_distancia;
    }


    public static void mayorYMenor(Pair<Integer, Integer> numeros, Integer val_1, Integer val_2){
        if(val_1 < val_2){
            numeros.setFirst(val_2);
            numeros.setSecond(val_1);
        }else{
            numeros.setFirst(val_1);
            numeros.setSecond(val_2);
        }
    }

    public static Double calculaFitness(Double[][] poblacion_a_evaluar, Integer fila, Double[][] m_adyacencias, Integer num_ciudades){

        Double distancia = 0.0;

        for(int i = 0; i < num_ciudades-1; i++){
            distancia += m_adyacencias[poblacion_a_evaluar[fila][i].intValue()][poblacion_a_evaluar[fila][i+1].intValue()];
        }
        distancia += m_adyacencias[poblacion_a_evaluar[fila][0].intValue()][poblacion_a_evaluar[fila][num_ciudades-1].intValue()];

        return distancia;
    }

    public static Double[] OX2(Integer padre_1, Integer padre_2, Integer numero_ciudades, Random semilla, Double[][] poblacion_padres, Integer probSelecOX2){

        //Vector hijo del cruce de los dos padres
        Double[] hijo = new Double[numero_ciudades+1];

        //Vector auxiliar para almacenar las ciudades seleccionadas del padre 1
        ArrayList<Integer> seleccionados = new ArrayList<>();

        //Bucle para seleccionar las ciudades del padre 1
        for(int i = 0; i < numero_ciudades; i++){
            if(semilla.nextInt(101) < probSelecOX2){
                seleccionados.add(poblacion_padres[padre_1][i].intValue());
            }
        }

        //Pivote para controlar las ciudades seleccionadas
        int pivote_seleccionados = 0;

        //Bucle de construcción del hijo
        for (int i = 0; i < numero_ciudades; i++) {
            if(!seleccionados.contains(poblacion_padres[padre_2][i].intValue())){
                hijo[i] = poblacion_padres[padre_2][i];
            }else{
                hijo[i] = seleccionados.get(pivote_seleccionados).doubleValue();
                pivote_seleccionados++;
            }
        }

        //Ponemos el fitness a 0
        hijo[numero_ciudades] = 0.0;

        return hijo;
    }

    public static ArrayList<Double> MOC(Integer padre_1, Integer padre_2, Random semilla, Integer numero_ciudades, Double[][] poblacion_padres){

        Integer punto_cruce = semilla.nextInt(1, numero_ciudades);
        ArrayList<Double> hijo = new ArrayList<>(numero_ciudades);

        for(int i = punto_cruce; i < numero_ciudades; i++){
            hijo.add(poblacion_padres[padre_1][i]);
        }

        int pivote_hijo = 0;
        for(int i = 0; i < numero_ciudades; i++){
            if(!hijo.contains(poblacion_padres[padre_2][i])){
                hijo.add(0, poblacion_padres[padre_2][i]);
                pivote_hijo++;
            }
        }

        return hijo;

    }

    public static Integer[] calculaElites(Double[][] poblacion, int num_ciudades, int tam_poblacion, int cantidad_elites){

        Integer[] elites = new Integer[cantidad_elites];

        int mejor = 0;
        Double valor_mejor = poblacion[0][num_ciudades];

        int segundo_mejor = -1;
        Double valor_segundo_mejor = Double.MAX_VALUE;
        for(int i = 1; i < tam_poblacion; i++){

            if(poblacion[i][num_ciudades] < valor_mejor){
                valor_segundo_mejor = valor_mejor;
                segundo_mejor = mejor;
                valor_mejor = poblacion[i][num_ciudades];
                mejor = i;
            }else if(poblacion[i][num_ciudades] < valor_segundo_mejor){
                valor_segundo_mejor = poblacion[i][num_ciudades];
                segundo_mejor = i;
            }

        }
        if(cantidad_elites == 2){
            elites[0] = mejor;
            elites[1] = segundo_mejor;
        }else{
            elites[0] = mejor;
        }

        return elites;
    }

    public static Integer calculaPeor(Double[][] poblacion, int num_ciudades, int tam_poblacion){

        Double valor_peor = poblacion[0][num_ciudades];
        int peor = 0;

        for(int i = 1; i < tam_poblacion; i++){
            if(poblacion[i][num_ciudades] > valor_peor){
                peor = i;
                valor_peor = poblacion[i][num_ciudades];
            }
        }

        return peor;

    }

    public static Integer kBest2(Random semilla, int tam_poblacion, Double[][] poblacion, int numero_ciudades){
        Integer jugador_1;
        Integer jugador_2;

        //Damos valores a los participantes del torneo hasta que todos sean diferentes
        do {
            jugador_1 = semilla.nextInt(tam_poblacion);
            jugador_2 = semilla.nextInt(tam_poblacion);
        }while(jugador_1.equals(jugador_2));

        if(poblacion[jugador_1][numero_ciudades] < poblacion[jugador_2][numero_ciudades]){
            return jugador_1;
        }else{
            return jugador_2;
        }
    }

}
