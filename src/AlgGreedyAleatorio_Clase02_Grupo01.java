import java.util.*;

public class AlgGreedyAleatorio_Clase02_Grupo01 {

    private final Double[][] m_adyacencias;
    private final Integer num_datos;
    private final Integer k;
    private final Vector<Integer> camino_final;
    private Double distancia_total;
    Random semilla;
    GestionaLog log;





    public AlgGreedyAleatorio_Clase02_Grupo01(Configurador configurador, CargaDatos datos, Random seed, GestionaLog log) {

        num_datos = datos.getnCiudades();
        semilla = seed;
        k = configurador.getK();
        m_adyacencias = new Double[num_datos][num_datos];
        distancia_total = 0.0;
        this.log = log;

        camino_final = new Vector<>(num_datos);


        for(int i = 0; i < num_datos; i++){
            for(int j = 0; j < num_datos; j++){
                m_adyacencias[i][j] = datos.getDistancias()[i][j];
            }
        }

    }

    public Double[][] getM_adyacencias() {
        return m_adyacencias;
    }

    public Integer getNum_datos() {
        return num_datos;
    }

    public Integer getK() {
        return k;
    }

    public Vector<Integer> getCamino_final() {
        return camino_final;
    }

    public Double getDistancia_total() {
        return distancia_total;
    }

    void ejecutar(){

        Vector<Pair<Integer, Double>> ciudades = new Vector<>(num_datos);

        //Añadimos las ciudades
        for(int i = 0; i < num_datos; i++){
            double suma = 0;
            for(int j = 0; j < num_datos; j++){
                if(i!=j)suma += m_adyacencias[i][j];
            }
            Pair<Integer, Double> aux = new Pair<>(i, suma);
            ciudades.add(aux);
        }

        //Ordenamos el vector en función de las distancias
        Collections.sort(ciudades);




        Integer ciudad_destino = -1;
        Integer pivote = k;
        Integer aleatorio = semilla.nextInt(k);
        Integer ciudad_origen = ciudades.get(aleatorio).getFirst();
        ciudades.set(aleatorio, ciudades.get(pivote));
        camino_final.add(ciudad_origen);
        pivote++;


        distancia_total = 0.0;
        for(int i = 0; i < num_datos-k-1; i++){


            //Obtenemos el siguiente numero de la semilla y guardamos la ciudad de la posicion correspondiente
            aleatorio = semilla.nextInt(k);
            ciudad_destino = ciudades.get(aleatorio).getFirst();
            camino_final.add(ciudad_destino);

            //Sobreescribimos la ciudad que acabamos de escoger con la siguiente mejor
            ciudades.set(aleatorio, ciudades.get(pivote));
            pivote++;

            //Sumamos la distancia de la ciudad de origen a la de destino y actualizamos la de origen a la de destino
            distancia_total+=m_adyacencias[ciudad_origen][ciudad_destino];
            ciudad_origen = ciudad_destino;

        }



        for(int i = k-1; i > 0; i--){
            aleatorio = semilla.nextInt(i+1);
            ciudad_destino = ciudades.get(aleatorio).getFirst();
            camino_final.add(ciudad_destino);

            if(aleatorio != i){
                ciudades.set(aleatorio, ciudades.get(i));
            }

            distancia_total+=m_adyacencias[ciudad_origen][ciudad_destino];
            ciudad_origen = ciudad_destino;
        }
        camino_final.add(ciudades.get(0).getFirst());

        log.registraLog("\nDistancia final en el Greedy: " + distancia_total + "\n");

    }


}

