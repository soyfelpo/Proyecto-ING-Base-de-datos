import { Producto } from "./Producto";

export class Menu {
  public fechaActualizacion: Date = new Date();
  private productos: Producto[] = [];

  constructor(public readonly idMenu: number) {}

  agregarProducto(producto: Producto): void {
    this.productos.push(producto);
    this.fechaActualizacion = new Date();
  }

  obtenerProductos(): Producto[] {
    return [...this.productos];
  }
}
