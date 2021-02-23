public class Producto {

    int idProducto;
    String marca, unidades, pedido, tipo;

    public Producto(int idProducto, String marca, String unidades, String pedido, String tipo) {
        this.idProducto = idProducto;
        this.marca = marca;
        this.unidades = unidades;
        this.pedido = pedido;
        this.tipo = tipo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", marca='" + marca + '\'' +
                ", unidades='" + unidades + '\'' +
                ", pedido='" + pedido + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
