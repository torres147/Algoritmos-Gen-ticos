
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


public class GestionaLog {
    private StringBuilder log;

    private StringBuilder logCostes;
    private File carpeta;

    public GestionaLog(Configurador config) {
        log = new StringBuilder();
        carpeta = new File(config.getLogs());
        logCostes = new StringBuilder();
    }



    public void registraLog(String texto){
        log.append(texto);
    }

    public void escribeFichero(String ruta){
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(log.toString());


        }catch (IOException ex){
        }
        finally {
            try{
                if (null != fichero){
                    fichero.close();
                }
            }catch (IOException e2){
            }
        }
    }

    public void muestraCamino(Vector<Integer> muestra){
        this.registraLog("\n"+"---------------MOSTRANDO 40 PRIMEROS ELEMENTOS DEL CAMINO Y 40 ULTIMOS---------------"+"\n");
        for(int i = 0; i < 40; i++) {
            registraLog(muestra.get(i) + "->");
            if ((i + 21) % 20 == 0) {
                registraLog("\n");
            }
        }
        registraLog(".\n.\n.\n");
        int salto_de_linea = 0;
        for(int i = muestra.size()-39; i < muestra.size(); i++) {
            registraLog(muestra.get(i) + "->");
            salto_de_linea++;
            if ((salto_de_linea+21) % 20 == 0) {
                registraLog("\n");
            }
        }
        this.registraLog("---------------FIN CAMINO---------------" + "\n\n");
    }

    public void cambioEjecucion(int semilla, String archivo){
        this.registraLog("\n"+"**********************************************************************"+"\n");
        this.registraLog("\n"+"EMPEZAMOS A EJECUTAR EL ARCHIVO " + archivo + " CON LA SEMILLA " + semilla +"\n");
        this.registraLog("\n---------- Jorge Barbero López ---------- Francisco Torres Peñalver ----------\n");
        this.registraLog("\n"+"**********************************************************************"+"\n");
    }
}
