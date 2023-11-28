package Reservaciones;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReservacionRegistro extends javax.swing.JFrame {

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private DateFormat horaFormat = new SimpleDateFormat("HH:mm");
    private final int idMesa;
    private List<Integer> selectedMesas = new ArrayList<>();

    /**
     * Creates new form Reservacion
     */
    public ReservacionRegistro(int idMesa) {
        this.idMesa = idMesa;
        initComponents();
    }

    ReservacionRegistro() {
        this.idMesa = 0; // Provide a default value or update as needed
        initComponents();
    }

    public void setMesas(List<Integer> selectedMesas) {
        this.selectedMesas = selectedMesas;
    }

    public void setSelectedIdMesas(List<Integer> selectedMesas) {
        this.selectedMesas = selectedMesas;
    }

    void updateDatabase(List<Integer> selectedMesas) {
        Connection connection = ConexionBD.getConnection();
        String updateQuery = "UPDATE mesa SET estado = ? WHERE idMesa = ?";

        try ( PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            for (int mesaId : selectedMesas) {
                updateStatement.setInt(1, 2);  // Assuming 2 represents the reserved state
                updateStatement.setInt(2, mesaId);
                updateStatement.addBatch();
            }

            updateStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately
        } finally {
            ConexionBD.closeConnection(connection);
        }
    }

    private void MensajeReservacion() {

        Date date = new Date();
        String fecha = dateFormat.format(date);

        Date horario = new Date();
        String hora = horaFormat.format(horario);

        JOptionPane.showMessageDialog(this, "Reservación realizada con éxito. \nFecha Reservada: " + fecha + " Hora Reservada: " + hora + " \nTiene 20 minutos antes de que su reservación expire", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void realizarReservacion() {
        try {
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            String correo = txtCorreo.getText();
            String telefono = txtTelefono.getText();

            // Step 1: Look for idCliente
            int idCliente = buscarIdCliente(nombre, apellidos, correo, telefono);

            if (idCliente != -1) {
                // Step 2: Get the selected tables
                if (!selectedMesas.isEmpty()) {
                    // Assuming you want to get the first mesa ID from the list
                    int mesaId = selectedMesas.get(0);

                    // Step 3: Insert reservation and update mesa for each selected table
                    Date date = new Date();
                    String fecha = dateFormat.format(date);

                    Date horario = new Date();
                    String hora = horaFormat.format(horario);

                    insertarReservacion(idCliente, fecha, hora, selectedMesas);

                    MensajeReservacion(); // Show success message
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor selecciona al menos una mesa.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private int buscarIdCliente(String nombre, String apellidos, String correo, String telefono) {
        try {
            Connection connection = ConexionBD.getConnection();
            String sql = "SELECT idCliente FROM cliente WHERE nombre = ? AND apellidos = ? AND correo = ? AND telefono = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nombre);
            statement.setString(2, apellidos);
            statement.setString(3, correo);
            statement.setString(4, telefono);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("idCliente");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return -1; // Return -1 if not found
    }

    private void insertarReservacion(int idCliente, String fecha, String hora, List<Integer> selectedMesas) {
        Connection connection = null;
        try {
            connection = ConexionBD.getConnection();
            connection.setAutoCommit(false); // Disable auto-commit

            // Step 1: Insert reservation for each selected table
            String sqlReservacion = "INSERT INTO reservacion (FolioReserva, idCliente, fecha, hora, idMesa) VALUES (NULL, ?, STR_TO_DATE(?, '%d/%m/%Y'), ?, ?)";
            try ( PreparedStatement statementReservacion = connection.prepareStatement(sqlReservacion, Statement.RETURN_GENERATED_KEYS)) {
                for (int mesaId : selectedMesas) {
                    statementReservacion.setInt(1, idCliente);
                    statementReservacion.setString(2, fecha);
                    statementReservacion.setString(3, hora);
                    statementReservacion.setInt(4, mesaId);  // Use mesaId instead of idMesa

                    statementReservacion.executeUpdate();

                    // Retrieve the generated FolioReserva
                    ResultSet generatedKeys = statementReservacion.getGeneratedKeys();
                    int folioReserva = -1;
                    if (generatedKeys.next()) {
                        folioReserva = generatedKeys.getInt(1);
                    }

                    // Step 2: Update mesa for each selected table
                    String sqlUpdateMesa = "UPDATE mesa SET estado = ? WHERE idMesa = ?";
                    try ( PreparedStatement statementUpdateMesa = connection.prepareStatement(sqlUpdateMesa)) {
                        statementUpdateMesa.setInt(1, 2); // Set estado to 2 (occupied)
                        statementUpdateMesa.setInt(2, mesaId);  // Use mesaId instead of idMesa
                        statementUpdateMesa.executeUpdate();
                    }

                    // Use folioReserva or perform any additional logic for each reservation
                    System.out.println("Reservation successful. FolioReserva: " + folioReserva);
                }

                // Commit the transaction
                connection.commit();
            }
        } catch (SQLException e) {
            // Rollback the transaction in case of an exception
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println(e);
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Enable auto-commit before closing
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelReservacionMesas = new javax.swing.JPanel();
        txtCorreo = new javax.swing.JTextField();
        jButtonRegresar = new javax.swing.JButton();
        lblTituloReservacion = new javax.swing.JLabel();
        jLabelTelefonoReservacion = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabelReservacionNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabelApellidosReservacion = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabelCorreoReservacion = new javax.swing.JLabel();
        jLabelIcono = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(32, 17, 72));

        jPanelReservacionMesas.setBackground(new java.awt.Color(168, 46, 38));
        jPanelReservacionMesas.setForeground(new java.awt.Color(32, 17, 72));

        txtCorreo.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        txtCorreo.setForeground(new java.awt.Color(0, 204, 253));

        jButtonRegresar.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        jButtonRegresar.setForeground(new java.awt.Color(0, 204, 253));
        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/BotonRegresar.png"))); // NOI18N
        jButtonRegresar.setPreferredSize(new java.awt.Dimension(30, 30));
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        lblTituloReservacion.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        lblTituloReservacion.setForeground(new java.awt.Color(102, 201, 205));
        lblTituloReservacion.setText("Registrate y reserva");

        jLabelTelefonoReservacion.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        jLabelTelefonoReservacion.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTelefonoReservacion.setText("Telefono:");

        txtTelefono.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(0, 204, 253));

        jLabelReservacionNombre.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        jLabelReservacionNombre.setForeground(new java.awt.Color(255, 255, 255));
        jLabelReservacionNombre.setText("Nombre:");

        txtNombre.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(0, 204, 253));
        txtNombre.setName("txtNombre"); // NOI18N
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        jLabelApellidosReservacion.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        jLabelApellidosReservacion.setForeground(new java.awt.Color(255, 255, 255));
        jLabelApellidosReservacion.setText("Apellidos:");

        txtApellidos.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(0, 204, 253));

        jLabelCorreoReservacion.setFont(new java.awt.Font("Barlow Light", 1, 24)); // NOI18N
        jLabelCorreoReservacion.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCorreoReservacion.setText("Correo:");

        jLabelIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/ICONO.png"))); // NOI18N

        jButton2.setBackground(new java.awt.Color(102, 201, 205));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("RESERVAR");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 102, 51), null));

        javax.swing.GroupLayout jPanelReservacionMesasLayout = new javax.swing.GroupLayout(jPanelReservacionMesas);
        jPanelReservacionMesas.setLayout(jPanelReservacionMesasLayout);
        jPanelReservacionMesasLayout.setHorizontalGroup(
            jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                        .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                                .addComponent(jLabelReservacionNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelReservacionMesasLayout.createSequentialGroup()
                                .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelCorreoReservacion)
                                    .addComponent(jLabelApellidosReservacion)
                                    .addComponent(jLabelTelefonoReservacion))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtApellidos)
                                    .addComponent(txtCorreo)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(44, 44, 44)
                        .addComponent(jLabelIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(476, 476, 476))
                    .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                        .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTituloReservacion)
                        .addGap(891, 891, 891))))
            .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                .addGap(199, 199, 199)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelReservacionMesasLayout.setVerticalGroup(
            jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblTituloReservacion)
                        .addGap(38, 38, 38)
                        .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelReservacionNombre)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelApellidosReservacion))
                        .addGap(6, 6, 6)
                        .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCorreoReservacion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelReservacionMesasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTelefonoReservacion)))
                    .addGroup(jPanelReservacionMesasLayout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabelIcono)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelReservacionMesas, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelReservacionMesas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButtonRegresarActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReservacionRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservacionRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservacionRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservacionRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReservacionRegistro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JLabel jLabelApellidosReservacion;
    private javax.swing.JLabel jLabelCorreoReservacion;
    private javax.swing.JLabel jLabelIcono;
    private javax.swing.JLabel jLabelReservacionNombre;
    private javax.swing.JLabel jLabelTelefonoReservacion;
    private javax.swing.JPanel jPanelReservacionMesas;
    private javax.swing.JLabel lblTituloReservacion;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

}
