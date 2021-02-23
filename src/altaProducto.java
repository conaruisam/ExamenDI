import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class altaProducto {
    private JFrame ventana;
    private JPanel JPanel;

    private JButton btnAceptar;
    private JButton btnSalir;



    private JTextField marca;
    private JTextField txtnUnidades;
    private JTextField txtEnPedido;

    private JLabel JLTipoProducto;
    private JLabel JLExtras;
    private JLabel title;

    private JTextField txtTipoProducto;
    private JTextField txtMarca;
    private JLabel JLMarca;
    private JLabel JLUnidades;
    private JLabel JLPedido;

    /***
     *  Variables BBDD
     */

    private Connection con;

    private String tipoProductoE;
    private String marcaE;
    private String nUnidadesE;
    private String enPedidoE;


    public altaProducto() {
        colocarVentana();
        estilizarBotones();

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
            }
        });
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    marcaE = txtMarca.getText();
                    nUnidadesE = txtnUnidades.getText();
                    enPedidoE = txtEnPedido.getText();
                    tipoProductoE = txtTipoProducto.getText();


                    System.out.println("Antes de validar entrada");
                    boolean entradaCorrecta = validarEntrada();
                    System.out.println(" ES LA ENTRADA CORRECTA: " + entradaCorrecta);
                    if (entradaCorrecta) {
                        abrirConexion();
                        altaProducto(marcaE, nUnidadesE, enPedidoE, tipoProductoE);
                    }

                    ventana.setVisible(false);
                } catch (ExceptionDatos ex) {
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Faltan datos");
                    return;
                }
            }
        });
    }

    private void colocarVentana() {

        ventana = new JFrame("Alta de producto");
        ventana.setContentPane(JPanel);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);


        //ventana.setSize(600, 600);
    }

    private void estilizarBotones() {

        btnAceptar.setBackground(Color.lightGray);
        btnSalir.setBackground(Color.lightGray);


        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        btnAceptar.setBorder(compound);
        btnSalir.setBorder(compound);
    }

    private boolean validarEntrada() throws ExceptionDatos {
        boolean salida;

        if (tipoProductoE   == "" // Si los datos de entrada están vacios mostramos la excepción
            && marcaE   == ""
            && nUnidadesE      == ""
            && enPedidoE     == "") {
            salida = false;
            throw new ExceptionDatos("FALTAN DATOS POR ESCRIBIR");
        } else {
            /*** Todo este apartado es frente a las posiciones de la entrada.
             *
             */
            if (tipoProductoE.length() > 99) {
                throw new ExceptionDatos("El tipo de producto ocupa 99 posiciones");
            } else if (marcaE.length() > 99) {
                throw new ExceptionDatos("La marca ocupa 99 posiciones");
            } else if (nUnidadesE.length() > 99) {
                throw new ExceptionDatos("El campo 'numero de unidades' ocupa 99 posiciones");
            } else if (enPedidoE.length() > 99) {
                throw new ExceptionDatos("El campo numero de pedidos ocupa 99 posiciones");
            } {
                salida = true;
            }
        }

        return salida;
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

    public void altaProducto(String marca, String unidades, String pedido, String tipo) {

        // Pondré toda la entrada en mayúscula
        marca = marca.toUpperCase();
        unidades = unidades.toUpperCase();
        pedido = pedido.toUpperCase();
        tipo = tipo.toUpperCase();

        final String SQL_INSERT = "INSERT INTO PRODUCTO (MARCA, UNIDADES, PEDIDO, TIPO) VALUES (?,?,?,?)"; // Creación de un String para poder hacer el INSERT.


        try {
            PreparedStatement insert_dinamico = con.prepareStatement(SQL_INSERT); // PARA HACER UN INSERT DINAMICO.

            // RELLENAMOS EL STRING DINAMICO
            insert_dinamico.setString(1, marca);
            insert_dinamico.setString(2, unidades);
            insert_dinamico.setString(3, pedido);
            insert_dinamico.setString(4, tipo);

            System.out.println(insert_dinamico);
            int fila = insert_dinamico.executeUpdate();
            System.out.println(fila + " filas insertadas correctamente");
            // Una vez registrado el coche salimos del menú de alta.
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "¡Registro duplicado!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true); // Activamos otra vez el commit una vez habiendo comprado que funciona correctamente nuestro insert
                con.close(); // Y cerramos nuestra conexion
                JOptionPane.showMessageDialog(null, "¡Producto registrado correctamente!");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    private class ExceptionDatos extends Exception {
        public ExceptionDatos(String msg) {
            super(msg);
            JOptionPane.showMessageDialog(null, msg);
        }
    }

}
