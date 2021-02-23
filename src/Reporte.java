import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Reporte {

    private JFrame ventana;
    private JPanel JPanel;
    private JPanel Reporte;
    private JButton btnSalir;
    public static Connection con; // Conexion a BBDD

    public Reporte(){

        abrirBBDD();
        colocarVentana();
        estilizarBotones();

        try {
            /*
                Meter al objeto el reporte .jasper
             */

            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("./src/reporte.jasper");
            JRDataSource vacio = new JREmptyDataSource(1);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, con);

            JasperViewer viewer = new JasperViewer(jasperPrint);
            JPanel.add(viewer.getContentPane(), BorderLayout.CENTER);

            ventana.setVisible(true);

        } catch (JRException e) {
            e.printStackTrace();
        }

        cerrarBBDD();

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
            }
        });
    }


    private void estilizarBotones() {

        btnSalir.setBackground(Color.lightGray);


        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        btnSalir.setBorder(compound);
    }
    private void colocarVentana() {

        ventana = new JFrame("Formulario");
        ventana.setContentPane(JPanel);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ventana.setSize(1000,500);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        //ventana.setResizable(true);

    }

    public static void main(String[] args) {

        Reporte informeSwing = new Reporte();
    }

    private static void abrirBBDD(){

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

    private static void cerrarBBDD(){

        try {
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
