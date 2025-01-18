import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configurador {
    ArrayList<String> archivo;
    int k;
    String alg_a_ejecutar;
    ArrayList<Integer> semillas;
    Integer PoblacionInicial;
    Double CantidadAleatoriosEnPoblacionInicial;
    Integer CantidadElites;
    Double ProbabilidadCruceGeneracional;
    Double ProbabilidadMutacion;
    Integer CondicionParadaIteraciones;
    Integer CondicionParadaTiempo;
    String logs;
    String cruce;
    Integer probSelecOX2;
    int kbest;


    public Configurador(String ruta) {
        archivo = new ArrayList<>();
        semillas = new ArrayList<>();
        String linea = null;
        FileReader f = null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(CargaDatos.class.getName()).log(Level.SEVERE, null, ex);
            }

            while(!linea.equals("EOF")){



                switch (linea){
                    case "Archivos":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        while (!linea.equals("Fin_Archivos")){

                            String[] split = linea.trim().split(":");
                            archivo.add(split[0]);
                            try {
                                linea = b.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    case "Parametros":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String[] split = linea.trim().split(" ");
                        k = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        alg_a_ejecutar = split[1];

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        PoblacionInicial = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        CantidadAleatoriosEnPoblacionInicial = Double.parseDouble(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        CantidadElites = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        ProbabilidadCruceGeneracional = Double.parseDouble(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        ProbabilidadMutacion = Double.parseDouble(split[1]);


                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        CondicionParadaIteraciones = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        CondicionParadaTiempo = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        cruce = split[1];

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        probSelecOX2 = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        kbest = Integer.parseInt(split[1]);

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        split = linea.trim().split(" ");
                        logs= split[1];


                    case "Semillas":

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            linea = b.readLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        while (!linea.equals("Fin_Semillas")){
                            semillas.add(Integer.parseInt(linea));
                            try {
                                linea = b.readLine();
                            } catch (IOException ex) {
                                Logger.getLogger(Configurador.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                }


                try {
                    linea = b.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(CargaDatos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public ArrayList<String> getArchivo() {
        return archivo;
    }


    public ArrayList<Integer> getSemillas() {
        return semillas;
    }

    public int getK() {
        return k;
    }

    public Integer getPoblacionInicial() {
        return PoblacionInicial;
    }

    public Integer getCantidadElites() {
        return CantidadElites;
    }

    public Double getProbabilidadCruceGeneracional() {
        return ProbabilidadCruceGeneracional;
    }

    public Double getProbabilidadMutacion() {
        return ProbabilidadMutacion;
    }

    public Integer getCondicionParadaIteraciones() {
        return CondicionParadaIteraciones;
    }

    public Integer getCondicionParadaTiempo() {
        return CondicionParadaTiempo;
    }

    public Double getCantidadAleatoriosEnPoblacionInicial() {
        return CantidadAleatoriosEnPoblacionInicial;
    }

    public String getLogs() {
        return logs;
    }
}


