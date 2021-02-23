/***
 * MainScreen - Clase para el main de la aplicación de gestión de stock
 */

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class mainScreen {
    private JFrame ventana;
    private JPanel JPanel;

    /***
     *  Apartado menús
     */

    private JMenuBar barraMenus;
    private JMenu menuArchivo;
    private JMenuItem menuNuevo;
    private JMenuItem menuGestion;
    private JMenuItem menuVer;
    private JMenuItem menuSalir;
    private JMenu menuAyuda;
    private JMenuItem menuyuda;

    private JLabel iconAmazon;

    private JButton btnNewProduct;
    private JButton btnGestion;
    private JButton btnProductos;

    private HelpSet helpSet;
    private HelpBroker helpBroker;

    public mainScreen(){
        colocarVentana();
        prepararHelpSet();
        prepararHelpBroker();

        btnNewProduct.setBackground(Color.lightGray);
        btnGestion.setBackground(Color.lightGray);
        btnProductos.setBackground(Color.lightGray);

        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        btnNewProduct.setBorder(compound);
        btnGestion.setBorder(compound);
        btnProductos.setBorder(compound);

        menuSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Sí", "No", "Cancelar"};
                if ((JOptionPane.showOptionDialog(null, "¿Realmente desea salir del sistema?",
                    "SALIR", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
                    options[0])) == 0) {
                    System.exit(0);
                }
            }
        });

        btnNewProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                altaProducto alta = new altaProducto();
            }
        });
        menuNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                altaProducto alta = new altaProducto();
            }
        });
        btnGestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionStock gestionStock = new gestionStock();
            }
        });
        menuGestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionStock gestionStock = new gestionStock();
            }
        });
        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reporte r = new Reporte();

            }
        });
        menuVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reporte r = new Reporte();
            }
        });
    }

    private void colocarVentana() {

        ventana = new JFrame("Vista princpal");
        ventana.setContentPane(JPanel);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);

        ventana.setSize(600, 600);
    }

    private void prepararHelpSet() {

        try {
            File fichero = new File("help/help_set.hs");

            URL hsURL = fichero.toURI().toURL();

            helpSet = new HelpSet(getClass().getClassLoader(), hsURL);

        } catch (MalformedURLException e) {
            System.err.println("ERROR HSURL...");
        } catch (HelpSetException e) {
            System.err.println("ERROR HELPSET...");
        }

    }

    private void prepararHelpBroker(){

        helpBroker = helpSet.createHelpBroker();

        helpBroker.enableHelpOnButton(menuyuda, "inicio", helpSet);



        // Ayuda al pulsar F1 sobre la ventana secundaria
        helpBroker.enableHelpKey(ventana.getContentPane(), "inicio", helpSet);

    }
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        mainScreen Screen = new mainScreen();
    }

}
