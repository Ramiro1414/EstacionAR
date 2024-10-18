import { Punto } from "./punto";

export interface Poligono {
    id: number,
    nombre: string,
    precio: number,
    puntos: Punto[]
}