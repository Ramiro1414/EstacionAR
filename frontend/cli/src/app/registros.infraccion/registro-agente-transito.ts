export interface RegistroAgenteTransito {
    id: number;
    patente: String;
    horaRegistro: Date;
    latitud: number;
    longitud: number;
    foto: Blob;
}