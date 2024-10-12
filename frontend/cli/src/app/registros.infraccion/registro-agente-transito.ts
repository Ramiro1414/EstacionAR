export interface RegistroAgenteTransito {
    id: number;
    patente: string;
    horaRegistro: Date;
    latitud: number;
    longitud: number;
    foto: Blob | string;
}