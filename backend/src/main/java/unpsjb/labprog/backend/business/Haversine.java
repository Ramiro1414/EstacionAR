package unpsjb.labprog.backend.business;

public class Haversine {

    private static final double R = 6371.0;

    /** Metodo que retorna la distancia en metros entre dos coordenadas */
    public static double distanciaEntrePuntos(double[] coordenadaA, double[] coordenadaB) {

        double result = 0;

        // Convertir latitud y longitud de grados a radianes
        double latA = Math.toRadians(coordenadaA[0]);
        double lonA = Math.toRadians(coordenadaA[1]);
        double latB = Math.toRadians(coordenadaB[0]);
        double lonB = Math.toRadians(coordenadaB[1]);

        // Diferencia entre latitudes y longitudes
        double dLat = latB - latA;
        double dLon = lonB - lonA;

        // Aplicar fórmula de Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(latA) * Math.cos(latB)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia en kilómetros
        result = R * c;

        // Convertir a metros
        return result * 1000;
    }
}