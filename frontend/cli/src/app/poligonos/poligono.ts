import { LineaPoligono } from "./lineaPoligono";

export interface Poligono {
    id: number,
    nombre: string,
    precio: number,
    lineasPoligono: LineaPoligono[]
}