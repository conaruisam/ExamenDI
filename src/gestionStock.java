import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class gestionStock {

    private Connection con; // Conexión a BBDD

    private JPanel panelMain;
    private JFrame ventana;
    private JButton btnSalir;
    private JButton buttonAlta;
    private JButton btnConsulta;
    private JButton btnModificacion;
    private JButton btnBaja;
    private JTable tabla;
    private JScrollPane jps;
    private JButton btnActualizarTabla;
    private JTextField JTextMarca;
    private JLabel JLMarca;
    private JLabel JLNunidades;
    private JTextField JTNunidades;
    private JTextField JTEnpedido;
    private JLabel JLPedido;
    private JLabel JLTipoproducto;
    private JTextField JTipoproducto;
    private JLabel Almacen;
    private JTextField JTIDProducto;


    public gestionStock() {
        iniciarVentana();
        rellenarJTable();
        estilizarBotones();

        btnConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProductoE = 0;
                String marcaE;
                String unidadesE;
                String pedidoE;
                String tipoE;

                try {
                    if (!JTIDProducto.getText().equals("")){
                        idProductoE = Integer.parseInt(JTIDProducto.getText());
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Escriba un numerico en el ID", "Error!", JOptionPane.ERROR_MESSAGE);
                }



                marcaE = JTextMarca.getText();
                unidadesE = JTNunidades.getText();
                pedidoE = JTEnpedido.getText();
                tipoE = JTipoproducto.getText();

                ArrayList<Producto> listaDevuelta;

                abrirConexion();
                listaDevuelta = consultaProductos(idProductoE, marcaE, unidadesE, pedidoE, tipoE);

                construirTabla(listaDevuelta);
                System.out.println("\n\tEmpiezo a crear la tabla ");
                System.out.println(" -- LISTA DEVUELTA -- " + listaDevuelta.size());
            }
        });

        btnModificacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila_seleccionada = tabla.getSelectedRow();

                System.out.println(tabla.getValueAt(fila_seleccionada, 0).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 1).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 2).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 3).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 4).toString());

                int idProducto = Integer.parseInt(tabla.getValueAt(fila_seleccionada, 0).toString());
                String marca = tabla.getValueAt(fila_seleccionada, 1).toString();
                String unidades = tabla.getValueAt(fila_seleccionada, 2).toString();
                String pedido = tabla.getValueAt(fila_seleccionada, 3).toString();
                String tipo = tabla.getValueAt(fila_seleccionada, 4).toString();

                marca = marca.toUpperCase();
                unidades = unidades.toUpperCase();
                pedido = pedido.toUpperCase();
                tipo = tipo.toUpperCase();


                int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de modificar este registro?");

                if (resp == 0) { // Si desea borrar el registro procedemos a borramos.

                    abrirConexion();
                    modificarProducto(idProducto, marca, unidades, pedido, tipo);

                }
            }
        });
        btnActualizarTabla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rellenarJTable();
            }
        });
        /***
        btnBaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int fila_seleccionada = tabla.getSelectedRow();

                System.out.println(tabla.getValueAt(fila_seleccionada, 0).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 1).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 2).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 3).toString());
                System.out.println(tabla.getValueAt(fila_seleccionada, 4).toString());

                String matricula = tabla.getValueAt(fila_seleccionada, 0).toString();

                int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de dar de baja este registro?");

                if (resp == 0) { // Si desea borrar el registro procedemos a borramos.

                    abrirConexion();
                    eliminarProducto(idProducto);

                }
            }
        });
         */
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
            }
        });
    }


    private void iniciarVentana() {
        ventana = new JFrame("Gestion de stock");

        ventana.setContentPane(panelMain);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(true);


    }

    /**
     * LOS SIGUIENTES TRES MÉTODOS RELLENAN NUESTRO COMPONENTE PRINCIPAL DE LA APLICACIÓN, EL JTABLE.
     */
    private void rellenarJTable() {
        abrirConexion();

        ArrayList<Producto> listaDevuelta = consultaProductos(0, "", "", "", ""); // Arraylist que me llenará la tabla.

        construirTabla(listaDevuelta);
    }

    public void construirTabla(ArrayList<Producto> listaProductos) {

        final String[] encabezadoTabla = { // Primer paso. creo un array con el la primera fila de la tabla..
                    "Id producto", "Marca", "Unidades", "Pedido", "Tipo"
        };

        String informacion[][] = obtenerMatriz(listaProductos); // Segundo paso. obtengo la información de la bbdd, que previamente transforme en un arraylist y la meto en una matriz.

        tabla = new JTable(informacion, encabezadoTabla); // Tercer paso. creo el JTable, con la información del encabezado y la información de dentro

        jps.setViewportView(tabla);

    }

    private String[][] obtenerMatriz(ArrayList<Producto> listaProductos) {

        String matrizInfo[][] = new String[listaProductos.size()][5]; // Las filas equivaldrán al número de registros encontrados y las columnas son las 5 de la tabla.

        for (int i = 0; i < matrizInfo.length; i++) { // Recorro el array...

            matrizInfo[i][0] = listaProductos.get(i).getIdProducto()+ "";
            matrizInfo[i][1] = listaProductos.get(i).getMarca();
            matrizInfo[i][2] = listaProductos.get(i).getUnidades();
            matrizInfo[i][3] = listaProductos.get(i).getPedido();
            matrizInfo[i][4] = listaProductos.get(i).getTipo();

        }

        return matrizInfo;
    }

    private void estilizarBotones() {

        btnConsulta.setBackground(Color.lightGray);
        btnActualizarTabla.setBackground(Color.lightGray);
        btnModificacion.setBackground(Color.lightGray);
        btnSalir.setBackground(Color.lightGray);


        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        btnConsulta.setBorder(compound);
        btnActualizarTabla.setBorder(compound);
        btnModificacion.setBorder(compound);
        btnSalir.setBorder(compound);

    }


    /**
     * BBDD -- OPCIONES DE BASES DE DATOS
     */

    private void abrirConexion() { // Abriendo la BBDD

        try {

            String url = "jdbc:mysql://localhost/almacen?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String pass = "1234";

            con = DriverManager.getConnection(url, user, pass);

            if (con != null) {

                System.out.println("Entrando correctamente!");
                con.setAutoCommit(false); // Quitando el autocommit

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Producto> consultaProductos(int idProducto, String marca, String unidades, String enPedido, String tipoProducto) {

        /** EN ESTE PARRAFO LO QUE HARÉ SERÁ UNA CONSULTA DINÁMICA, DEPENDIENDO DE LA ENTRADA, LA SELECT SE HARA DE UNA FORMA
         * U OTRA y devolveré un arraylist.
         * */

        ArrayList<Producto> listaProductos = new ArrayList<Producto>();

        try {

            String SQL_SELECT = "SELECT * FROM PRODUCTO";
            final String where = " WHERE ";
            final String and = " AND ";
            boolean primero = true;
            boolean idProdu = false, mar = false, uni = false, enPedid = false, tipProd = false; // BOOLEANS UTILIZADOS PARA EL PREPARE STATEMENT

            if (idProducto != 0) {
                if (primero) {
                    SQL_SELECT += where;
                    primero = false;
                }
                SQL_SELECT += " IDPRODUCTO LIKE ? ";
                idProdu = true;
            }

            if (!marca.equals("")) {
                if (primero) {
                    SQL_SELECT += where;
                    primero = false;
                } else {
                    SQL_SELECT += and;
                }
                SQL_SELECT += "MARCA LIKE ?";
                mar = true;
            }

            if (!unidades.equals("")) {
                if (primero) {
                    SQL_SELECT += where;
                    primero = false;
                } else {
                    SQL_SELECT += and;
                }
                SQL_SELECT += " UNIDADES LIKE ?";
                uni = true;
            }

            if (!enPedido.equals("")) {
                if (primero) {
                    SQL_SELECT += where;
                    primero = false;
                } else {
                    SQL_SELECT += and;
                }

                SQL_SELECT += " PEDIDO LIKE ?";
                enPedid = true;
            }

            if (!tipoProducto.equals("")) {
                if (primero) {
                    SQL_SELECT += where;
                    primero = false;
                } else {
                    SQL_SELECT += and;
                }

                SQL_SELECT += " TIPO LIKE ?";
                tipProd = true;
            }

            SQL_SELECT += " ORDER BY IDPRODUCTO";

            PreparedStatement select_dinamico = con.prepareStatement(SQL_SELECT); // PARA HACER UN INSERT DINAMICO.

            int nPosicionDinamica = 1; // Esta posicion es la que utilizaré para

            if (idProdu) {
                select_dinamico.setString(nPosicionDinamica, "%" + idProducto + "%");
                nPosicionDinamica++;
            }

            if (mar) {
                select_dinamico.setString(nPosicionDinamica, "%" + marca + "%");
                nPosicionDinamica++;
            }

            if (uni) {
                select_dinamico.setString(nPosicionDinamica, "%" + unidades + "%");
                nPosicionDinamica++;
            }

            if (tipProd) {

                select_dinamico.setString(nPosicionDinamica, "%" + tipoProducto + "%");
                nPosicionDinamica++;
            }

            if (enPedid) {
                select_dinamico.setString(nPosicionDinamica, "%" + enPedido + "%");
                nPosicionDinamica++;
            }


            System.out.println(select_dinamico); // CONSULTA A UTILIZAR.


            ResultSet registro = select_dinamico.executeQuery();

            int contadorRegistros = 0;


            while (registro.next()) { // LEO TODOS LOS REGISTROS QUE HAYAN SALIDO DE LA QUERY.

                // Voy añadiendo registros...


                int idPr = registro.getInt("IDPRODUCTO");
                String marcaPr = registro.getString("MARCA");
                String nUnidadesPr = registro.getString("UNIDADES");
                String pedidoPr = registro.getString("PEDIDO");
                String tipoPr = registro.getString("TIPO");



                // CREO UN NUEVO OBJETO COCHE CADA VEZ QUE COJA UN REGISTRO.
                Producto producto = new Producto(idPr, marcaPr, nUnidadesPr, pedidoPr, tipoPr);

                listaProductos.add(producto);

                contadorRegistros++;

                System.out.println("\n**************************************");
                System.out.println("REGISTRO NUMERO  " + contadorRegistros);
                System.out.println("ID PRODUCTO " + idPr);
                System.out.println("Marca " + marcaPr);
                System.out.println("Nº Unidades " + nUnidadesPr);
                System.out.println("Pedido " + pedidoPr);
                System.out.println("Tipo " + tipoPr);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.close(); // Y por último cerramos la BBDD
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return listaProductos;
    }

    private void modificarProducto(int idProducto, String marca, String unidades, String enPedido, String tipoProducto) {

        /***
         *  Este método servirá para actualizar en la BBDD un registro
         *
         */


        int contadorRegistros = 0;


        try {

            String SQL_UPDATE = "UPDATE PRODUCTO";
            final String where = " WHERE ";
            final String coma = " , ";
            boolean primero = true;
            Boolean mar = false, uni = false, enPedid = false, tipProd = false; // BOOLEANS UTILIZADOS PARA EL PREPARE STATEMENT


            if (!marca.equals("")) {
                if (primero) {
                    SQL_UPDATE += " SET ";
                    primero = false;
                } else {
                    SQL_UPDATE += coma;
                }
                SQL_UPDATE += "MARCA = ?";
                mar = true;
            }

            if (!unidades.equals("")) {
                if (primero) {
                    SQL_UPDATE += where;
                    primero = false;
                } else {
                    SQL_UPDATE += coma;
                }
                SQL_UPDATE += " UNIDADES = ?";
                uni = true;
            }

            if (!enPedido.equals("")) {
                if (primero) {
                    SQL_UPDATE += where;
                    primero = false;
                } else {
                    SQL_UPDATE += coma;
                }

                SQL_UPDATE += " PEDIDO = ?";
                enPedid = true;
            }

            if (!tipoProducto.equals("")) {
                if (primero) {
                    SQL_UPDATE += where;
                    primero = false;
                } else {
                    SQL_UPDATE += coma;
                }

                SQL_UPDATE += " TIPO = ?";
                tipProd = true;
            }

            SQL_UPDATE += " WHERE IDPRODUCTO = ?";

            PreparedStatement update_dinamico = con.prepareStatement(SQL_UPDATE); // PARA HACER UN INSERT DINAMICO.

            int nPosicionDinamica = 1; // Esta posicion es la que utilizaré para


            if (mar) {
                update_dinamico.setString(nPosicionDinamica, marca);
                nPosicionDinamica++;
            }

            if (uni) {
                update_dinamico.setString(nPosicionDinamica, unidades);
                nPosicionDinamica++;
            }

            if (tipProd) {

                update_dinamico.setString(nPosicionDinamica, tipoProducto);
                nPosicionDinamica++;
            }

            if (enPedid) {
                update_dinamico.setString(nPosicionDinamica, enPedido);
                nPosicionDinamica++;
            }

            update_dinamico.setInt(nPosicionDinamica, idProducto); // POR ÚLTIMO PONGO EL ID DEL PRODUCTO EN EL WHERE DEL UPDATE.

            System.out.println(update_dinamico); // UPDATE A UTILIZAR A UTILIZAR.


            contadorRegistros = update_dinamico.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {

                if (contadorRegistros > 0) {
                    JOptionPane.showMessageDialog(null, "EL PRODUCTO CON EL ID'" + idProducto + "' HA SIDO ACTUALIZADO CORRECTAMENTE");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO MODIFICAR EL PRODUCTO CON EL ID "+ idProducto);
                }
                con.setAutoCommit(true);
                con.close(); // Y por último cerramos la BBDD
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        gestionStock gestionStock = new gestionStock();
    }
}

