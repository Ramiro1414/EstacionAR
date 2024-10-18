import { HoraInicioHoraFin } from "./hora.inicio.hora.fin";

export interface HorarioEstacionamiento {
    id: number;
    nombre: string;

    fechaInicio: string;  // Por ejemplo, "05-01" para 1 de mayo
    fechaFin: string;     // Por ejemplo, "09-30" para 30 de septiembre

    horariosDelDia : HoraInicioHoraFin[];
}
