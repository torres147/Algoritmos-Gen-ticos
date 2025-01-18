import java.io.File;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Configurador conf = new Configurador(args[0]);
        System.out.println(conf.getArchivo());

        if(Objects.equals(conf.alg_a_ejecutar, "GEN")){

            for(int i = 0; i < conf.getArchivo().size(); i++){
                for(int j = 0; j < conf.getSemillas().size(); j++){

                    CargaDatos carg = new CargaDatos(conf.getArchivo().get(i));
                    Random semilla = new Random(conf.getSemillas().get(j));
                    GestionaLog log = new GestionaLog(conf);


                    AlgEvolutivoGeneracional_Clase02_Grupo01 evGen = new AlgEvolutivoGeneracional_Clase02_Grupo01(conf, carg, log, semilla);


                    log.cambioEjecucionGen(conf.getSemillas().get(j), conf.getArchivo().get(i), conf.CantidadElites, conf.cruce, conf.kbest);
                    evGen.ejecutar();
                    log.escribeFichero(conf.getLogs() + File.separator + "_" + conf.getArchivo().get(i) + "[" + conf.getSemillas().get(j) + "]("+ conf.getCantidadElites() +
                            "-" + conf.cruce + "-" + conf.kbest +").txt");



                }
            }


        }else{

            for(int i = 0; i < conf.getArchivo().size(); i++){
                for(int j = 0; j < conf.getSemillas().size(); j++){

                    CargaDatos carg = new CargaDatos(conf.getArchivo().get(i));
                    Random semilla = new Random(conf.getSemillas().get(j));
                    GestionaLog log = new GestionaLog(conf);


                    AlgEvolutivoEstacionario_Clase02_Grupo01 evEst = new AlgEvolutivoEstacionario_Clase02_Grupo01(conf, carg, log, semilla);


                    log.cambioEjecucionEst(conf.getSemillas().get(j), conf.getArchivo().get(i), conf.cruce);
                    evEst.ejecutar();
                    log.escribeFichero(conf.getLogs() + File.separator + "_" + conf.getArchivo().get(i) + "[" + conf.getSemillas().get(j) + "]("+
                              conf.cruce + ").txt");


                }
            }

        }


        /*System.out.println("---POBLACIÓN INICIAL ---");
        for(int i = 0; i < evGen.getTam_poblacion(); i++){
            System.out.print( i + ": [");
            for(int j = 0; j < evGen.getNumero_ciudades(); j++){
                System.out.print(evGen.getPoblacion()[i][j] + ", ");
            }
            System.out.print("] ");
            System.out.println(evGen.getPoblacion()[i][evGen.getNumero_ciudades()]);
        }
        System.out.print("Elites: ");
        for(int i = 0; i < evGen.getCantidad_elites(); i++){
            System.out.print(evGen.getIndividuos_elites()[i][evGen.getNumero_ciudades()] + "   ");
        }*/

        /*System.out.println("---POBLACIÓN DE PADRES---");
        for(int i = 15; i < 25; i++){
            System.out.print("[");
            for(int j = 0; j < evGen.getNumero_ciudades(); j++){
                System.out.print(evGen.getPoblacion_padres()[i][j] + ", ");
            }
            System.out.print("]");
            System.out.println(evGen.getPoblacion_padres()[i][evGen.getNumero_ciudades()]);
        }

        System.out.println("---POBLACIÓN DE DESCENDIENTES---");
        for(int i = 15; i < 25; i++){
            System.out.print("[");
            for(int j = 0; j < evGen.getNumero_ciudades(); j++){
                System.out.print(evGen.getPoblacion_descendientes()[i][j] + ", ");
            }
            System.out.print("]");
            System.out.println(evGen.getPoblacion_descendientes()[i][evGen.getNumero_ciudades()]);
        }*/


        /*for(int i = 0; i <carg.getnCiudades(); i++ ){
            for(int j = 0; j <carg.getnCiudades(); j++){
                System.out.print(carg.getDistancias()[i][j]);
                System.out.print("    ");
            }
            System.out.println();
        }*/



    }
}