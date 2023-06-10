package org.example;

public class main {

    public static void main(String[] args) {

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("Intercambio en milisegundos?: ");
        float num1 = scanner.nextFloat();

        System.out.print("Tamaño del QUANTUM?: ");
        float num2 = scanner.nextFloat();
        float quantum = (num1 / num2);

        System.out.print("Cuantos Procesos desea?: ");
        int procesos = scanner.nextInt();

        int respuesta = 0;
        int datos[][] = new int[procesos][4];


        for (int i = 0; i < procesos; i++) {
            System.out.println("-------------------------------------------------");
            System.out.print("Tiempo de llegada de P" + (i) + ": ");
            datos[i][0] = scanner.nextInt();

            System.out.print("Necesidad que requiere P" + (i) + ": ");
            datos[i][1] = scanner.nextInt();

            System.out.print("Entradas/salidas para P" + (i) + "? Y(1) N(0): ");
            respuesta = scanner.nextInt();

            if (respuesta > 0) {
                System.out.print("Cuanto tiempo va a gastar la E/S de P" + (i) + ": ");
                datos[i][2] = scanner.nextInt();

                System.out.print("QUANTUMS de Retorno que requiere P" + (i) + ": ");
                datos[i][3] = scanner.nextInt();
            } else {
                datos[i][2] = 0;
                datos[i][3] = 0;
            }
        }

        MetodosRoind rb = new MetodosRoind(num1, num2, procesos, quantum, datos);
    }
}
