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


    public static void mayorYMenor(Pair<Integer, Integer> numeros, Integer val_1, Integer val_2){
        if(val_1 < val_2){
            numeros.setFirst(val_2);
            numeros.setSecond(val_1);
        }else{
            numeros.setFirst(val_1);
            numeros.setSecond(val_2);
        }
    }

    public static int k_best(Random semilla){




        return 0;
    }

}
