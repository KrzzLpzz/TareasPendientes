package com.krzz.tareaspendientes;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author JUNIOR
 */
public class Notificacion {

    PopupMenu popup = new PopupMenu();
    private Image image = new ImageIcon("notificacion.png").getImage();
    private final TrayIcon trayIcon = new TrayIcon(image, "Tareas Pendientes", popup);
    //obtiene instancia SystemTray
    final SystemTray systemtray = SystemTray.getSystemTray();
    //para el Timer
    private Timer timer;
    public static boolean band;
  

    public Notificacion() {
        //comprueba si SystemTray es soportado en el sistema
        if (SystemTray.isSupported()) {

            //acciones del raton sobre el icono en la barra de tareas
            MouseListener mouseListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent evt) {
                    //Si se presiono el boton izquierdo y la aplicacion esta minimizada
                    if (evt.getButton() == MouseEvent.BUTTON1)// && miframe.getExtendedState()==JFrame.ICONIFIED )
                    {
                        // Crear una instancia del nuevo formulario que quieres mostrar
                        TareasPendientes otroFormulario = new TareasPendientes();
                        // Hacer visible el nuevo formulario
                        otroFormulario.setVisible(true);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent evt) {
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                }

                @Override
                public void mousePressed(MouseEvent evt) {
                }

                @Override
                public void mouseReleased(MouseEvent evt) {
                }
            };

            //ACCIONES DEL MENU POPUP
            ActionListener salir = (ActionEvent e) -> {
                System.exit(0);
                systemtray.remove(trayIcon);
                Notificacion.band = true;
            };

            ActionListener verinformacion = (ActionEvent e) -> {
                JOptionPane.showMessageDialog(null, "made by Krzz.", "Aplicacion de Tareas",
                        JOptionPane.INFORMATION_MESSAGE);
                band = true;
            };
            //Se crean los Items del menu PopUp y se añaden
            MenuItem SalirItem = new MenuItem("Salir");
            SalirItem.addActionListener(salir);
            popup.add(SalirItem);

            MenuItem Itemverinfo = new MenuItem("Ver informacion");
            Itemverinfo.addActionListener(verinformacion);
            popup.add(Itemverinfo);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(mouseListener);

            //Añade el TrayIcon al SystemTray
            try {
                systemtray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("Error:" + e.getMessage());
            }
        } else {
            System.err.println("Error: SystemTray no es soportado");
        }
    }

    //Muestra una burbuja con la accion que se realiza
    public void MensajeTrayIcon(String texto, TrayIcon.MessageType tipo) {
        trayIcon.displayMessage("Tareas Pendientes", texto, tipo);
    }

    //clase interna que manejara una accion en segundo plano
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            String prod = "";
            if (Notificacion.band) {//Termina Timer
                timer.cancel();
            } else {//realiza acción
                    prod += "* " + TareasPendientes.mensaje + "\n";
                notificacion(prod);
            }
        }

        //Una accion a realiza cuando la aplicacion a sido minimizada
        public void notificacion(String nomprod) {
            MensajeTrayIcon("Estimado usuario,\nTienes la tarea " + nomprod + " pendiente. ", TrayIcon.MessageType.INFO);
        }
    }

    public void mje() throws AWTException {
        Notificacion.band = false;
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 0, 60000);//Se ejecuta cada 50 segundos
    }
}
