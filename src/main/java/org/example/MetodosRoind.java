package org.example;

import java.util.ArrayList;



class MetodosRoind{

    int reinicia = 0;
    int ignor = 0;
    String ColaListos = "";
    String Bloqueados = "";
    int n;
    float num1, num2;
    int anterior = 0;
    int contador = 0;
    int bandera = 0;
    float quantum;
    Variables[] procesos;
    ArrayList<String> cola;

    public MetodosRoind(float num1, float num2, int n, float quantum, int datos[][]) {
        this.n = n;
        this.num1 = num1;
        this.num2 = num2;
        this.quantum = quantum;
        this.procesos = new Variables[n];
        this.cola = new ArrayList<>();
        Variables p;
        for (int i = 0; i < this.n; i++) {
            p = new Variables();
            p.proceso = "P" + Integer.toString(i);
            p.id = i;
            p.Q = datos[i][1];
            p.llegada = datos[i][0];
            p.GastaES = datos[i][2];;
            p.QReturn = datos[i][3];
            p.estado = "N";
            p.inicio = -1;
            p.On = 0;
            p.Rllegada = 0;
            p.Off = 0;
            p.finalizo = 0;
            procesos[i] = p;
        }
        tabla(n, datos);
        colaListo(num1, num2, n);
        colaydiagrama();
        Final();
    }

    private void tabla(int n, int datos[][]) {
         System.out.print("\n          |Llegada en  |   NCPU     |Gasta E/S | NCPU retorno|\n");
         System.out.print("Procesos |Milisegundos| en QUANTUM |en QUANTUM| en QUANTUM  |\n");
         System.out.print("---------------------------------------------------------\n");
        for (int i = 0; i < n; i++) {
             System.out.print("P" + i + ":     " + datos[i][0] + "     |     " + datos[i][1] + "     |     " + datos[i][2] + "     |     " + datos[i][3] + "     |\n");
        }
    }

    private void colaydiagrama() {
         System.out.println("--------------------------------------------------------------");
         System.out.println("Cola de procesos");
         System.out.println(this.ColaListos);
         System.out.println("\n-------------------------------------------------------------");
         System.out.println("Diagrama de Gantt");
         String C = String.join("", this.cola);
         System.out.println(C);
         System.out.println("\n-------------------------------------------------------------");
         System.out.println("Regresa a la cola de listos en el tiempo");
         System.out.println(this.Bloqueados);
    }

    private boolean laTrue(int n, float num2) {
        if (bandera == n) {
            return false;
        }
        return true;
    }

    private void Final() {
        int TE = 0;
        int TEA = 0;
        float TEM;
        int TV = 0;
        int TVA = 0;
        float TVM;
        System.out.println("---------------------------------------------------------");
         System.out.println("Tiempo de Vuelta:");
        for (Variables p : procesos) {
            TV = TV + ((p.ultimo - (int) num1) - (p.GastaES * Math.round(num2)) - p.llegada);
            TVA = TVA + TV;
            System.out.println(p.proceso + ": " + (p.ultimo - (int) num1) + " - " + (p.GastaES * Math.round(num2)) + " - " + p.llegada + ": " + TV);
            TV = 0;

        }
        TVM = (float) TVA / (float) n;
         System.out.println("\nTiempo Medio de Vuelta: " + TVM);

         System.out.println("---------------------------------------------------------");
         System.out.println("Tiempo de Espera:");
         for (Variables p : procesos) {
            TE = TE + (p.inicio - p.llegada);
            TEA = TEA + TE;
             System.out.println(p.proceso + ": " + p.inicio + " - " + p.llegada + ": " + TE);
            TE = 0;
        }
        TEM = (float) TEA / (float) n;
         System.out.println("\nTiempo Medio de espera: " + TEM);
    }

    private void colaListo(float num1, float num2, int n) {
        Variables p;
        while (laTrue(n, num2)) {
            int banderita = 0;
            for (int i = 0; i < n; i++) {

                if(procesos[i].Q == 1)
                {
                    reinicia = reinicia +1;
                }
                if(reinicia == n)
                {
                    break;
                }
                if (procesos[i].estuvo == 1 && procesos[i].llegada <= contador) {
                    this.procesos[i].estado = "L";
                    procesos[i].llegada = procesos[i].Rllegada;
                    procesos[i].Q=procesos[i].QReturn;
                    this.ColaListos = this.ColaListos + procesos[i].proceso + ":" + procesos[i].Q + "/ ";
                    this.cola.add(ignor + "-" + procesos[i].proceso);
                    if (procesos[i].On == 0) {
                        procesos[i].inicio = ignor;
                        procesos[i].On = 1;
                    }
                    ignor = ignor + (Math.round(num2 + num1));
                    if (procesos[i].Off == 0) {
                        procesos[i].ultimo = ignor;
                        procesos[i].On = 1;
                    }
                    this.cola.add("-" + (ignor) + " / ");
                    i++;
                }
                if(reinicia == n)
                {
                    break;
                }
                if (procesos[i].llegada <= contador && !procesos[i].estado.equals("B")) {
                    this.procesos[i].estado = "L";
                    if (procesos[i].Q != 0) {
                        this.ColaListos = this.ColaListos + procesos[i].proceso + ":" + procesos[i].Q + "/ ";
                        this.cola.add(ignor + "-" + procesos[i].proceso);
                        if (procesos[i].On == 0) {
                            procesos[i].inicio = ignor;
                            procesos[i].On = 1;
                        }
                        ignor = ignor + (Math.round(num2 + num1));
                        if (procesos[i].Off == 0) {
                            procesos[i].ultimo = ignor;
                            procesos[i].On = 1;
                        }
                        this.cola.add("-" + (ignor) + " / ");
                    }
                }
                procesarCola(num1, num2, procesos[i].id);
                if (banderita != 0) {
                    if (procesos[i - 1].GastaES > 0 && procesos[i - 1].Q == 1) {
                        int B = i - 1;
                        //System.out.println("bloqueado");
                        bloqueado(B);
                    }
                } else {
                    banderita = 1;
                }
            }
        }
    }

    private void bloqueado(int B) {
        if (procesos[B].Q == 1 && procesos[B].GastaES > 0) {

            reinicia = reinicia - 1;
            contador = contador + (Math.round(num2 + num1));
            procesos[B].Rllegada = procesos[B].llegada;
            procesos[B].estuvo = 1;
            procesos[B].Rllegada2 = contador + (Math.round(procesos[B].GastaES * num2));
            procesos[B].estado = "B";

            this.Bloqueados = procesos[B].proceso + ": " + contador + "+" + (Math.round(procesos[B].GastaES * num2)) + "= " + (procesos[B].Rllegada2) + "/\n";

            this.ColaListos = this.ColaListos + procesos[B].proceso + ":" + procesos[B].Q + " / ";

            this.cola.add(ignor + "-" + procesos[B].proceso);

            if (procesos[B].On == 0) {
                procesos[B].inicio = ignor;
                procesos[B].On = 1;
            }
            ignor = ignor + (Math.round(num2 + num1));
            if (procesos[B].Off == 0) {
                procesos[B].ultimo = ignor;
                procesos[B].On = 1;
            }
            this.cola.add("-" + (ignor) + " / ");
            if (procesos[B].QReturn > 0) {
                procesos[B].Q = procesos[B].QReturn;
            }

            // Verificar si el nuevo tiempo es mayor que el tiempo actual
            if (procesos[B].llegada > contador) {
                contador = procesos[B].llegada;
            }
        }
    }

    private void procesarCola(float num1, float num2, int id) {
        if (procesosNoTerminados()) {
            for (Variables p : procesos) {
                if (id == p.id) {
                    trabajarVariables(num1, num2, p);
                }
            }
        }
    }

    private boolean procesosNoTerminados() {
        for (Variables p : procesos) {
            if (!"T".equals(p.estado)) {
                return true;
            }
        }
        return false;
    }

    private void trabajarVariables(float num1, float num2, Variables p) {
        if(p.Q == 1)
        {
            p.estado = "T";
        }

        if (p.Q > quantum) {
            p.Q = p.Q - 1;
            p.estado = "E";
            contador = contador + Math.round(num1 + num2);
            if (p.Q == 0) {
                p.estado = "T";
                p.ultimo = contador;
                bandera = bandera + 1;
            } else {
                p.estado = "L";
            }
            System.out.println("");
        } else if (!"T".equals(p.estado)) {
            p.Q = 0;
            p.estado = "T";

            // Verificar si es el último proceso en la lista
            if (p.id == n - 1) {
                p.ultimo = contador;
                bandera = n;
            }
        }

    }
}

