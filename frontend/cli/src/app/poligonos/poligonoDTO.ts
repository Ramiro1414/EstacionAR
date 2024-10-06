import { PolylineData } from "./polylineData";

export interface PoligonoDTO {
    id: number;
    nombre: string;
    precio: number;
    polylinesList: PolylineData[]
}