export abstract class Producto {
  constructor(
    public readonly idProducto: number,
    public readonly nombre: string,
    public readonly precioBase: number,
    public readonly descripcion: string,
    protected stock: number = 0
  ) {}

  abstract getPrecio(): number;
  abstract getDescripcion(): string;

  actualizarStock(cantidad: number): void {
    this.stock = Math.max(0, this.stock + cantidad);
  }

  getStock(): number {
    return this.stock;
  }
}

export class Bebida extends Producto {
  getPrecio(): number {
    return this.precioBase;
  }

  getDescripcion(): string {
    return `${this.nombre} — ${this.descripcion}`;
  }
}

export class Plato extends Producto {
  getPrecio(): number {
    return this.precioBase;
  }

  getDescripcion(): string {
    return `${this.nombre} — ${this.descripcion}`;
  }
}
