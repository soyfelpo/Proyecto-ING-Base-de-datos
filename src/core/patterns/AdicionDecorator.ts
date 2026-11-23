import { Producto } from "../domain/Producto";

export abstract class AdicionDecorator extends Producto {
  constructor(
    public readonly idAdicion: number,
    public readonly nombreAdicion: string,
    public readonly precioExtra: number,
    protected readonly producto: Producto
  ) {
    super(
      producto.idProducto,
      producto.nombre,
      producto.precioBase,
      producto.descripcion,
      producto.getStock()
    );
  }

  abstract override getPrecio(): number;
  abstract override getDescripcion(): string;
}

export class Extra extends AdicionDecorator {
  override getPrecio(): number {
    return this.producto.getPrecio() + this.precioExtra;
  }

  override getDescripcion(): string {
    return `${this.producto.getDescripcion()} | con ${this.nombreAdicion} (+$${this.precioExtra})`;
  }
}
