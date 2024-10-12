import { RegistroAgenteTransito } from "./registro-agente-transito";

export interface RegistroInfraccion {
    id: number;
    registroAgenteTransito: RegistroAgenteTransito;
    ubicacion: string;
}