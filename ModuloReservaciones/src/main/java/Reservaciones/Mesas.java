package Reservaciones;

import conexion.ConexionBD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Mesas extends javax.swing.JFrame {

    private final int idSucursal;
    private int selectedIdMesa = -1;
    private int idMesa;

    /**
     * Creates new form MesaReservacion
     */
    public Mesas(int idSucursal) {
        this.idSucursal = idSucursal;
        initComponents();
        loadMesas();
        loadInformacionSucursal();
    }

    private Mesas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void loadInformacionSucursal() {
        Connection connection = ConexionBD.getConnection();
        String query = "SELECT nombre, direccion, telefono FROM sucursal WHERE idSucursal = ?";

        try ( PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idSucursal);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");

                // Set the values to the corresponding labels
                jLabelSucursal.setText(nombre);
                jLabelDireccion.setText("Direccion: " + direccion);
                jLabelTelefono.setText("Telefono: " + telefono);
            } else {
                // Handle the case where no records are found for the given idSucursal
                System.out.println("No records found for idSucursal: " + idSucursal);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } finally {
            ConexionBD.closeConnection(connection);
        }
    }

    private void loadMesas() {
        Connection connection = ConexionBD.getConnection();
        String query = "SELECT idMesa, capacidad, area, estado FROM mesa WHERE idSucursal = ?";
        try ( PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idSucursal);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder mesasText = new StringBuilder();

            while (resultSet.next()) {
                int idMesa = resultSet.getInt("idMesa");
                int capacidad = resultSet.getInt("capacidad");
                String area = resultSet.getString("area");
                int estado = resultSet.getInt("estado");

                // Set icon for the corresponding jCheckBoxMesas
                switch (idMesa) {
                    case 1:
                    case 11:
                    case 21:
                    case 31:
                    case 41:
                    case 51:
                        setMesaIcon(jCheckBoxMesas1, estado, idMesa);
                        break;
                    case 2:
                    case 12:
                    case 22:
                    case 32:
                    case 42:
                    case 52:
                        setMesaIcon(jCheckBoxMesas2, estado, idMesa);
                        break;
                    case 3:
                    case 13:
                    case 23:
                    case 33:
                    case 43:
                    case 53:
                        setMesaIcon(jCheckBoxMesas3, estado, idMesa);
                        break;
                    case 4:
                    case 14:
                    case 24:
                    case 34:
                    case 44:
                    case 54:
                        setMesaIcon(jCheckBoxMesas4, estado, idMesa);
                        break;
                    case 5:
                    case 15:
                    case 25:
                    case 35:
                    case 45:
                    case 55:
                        setMesaIcon(jCheckBoxMesas5, estado, idMesa);
                        break;
                    case 6:
                    case 16:
                    case 26:
                    case 36:
                    case 46:
                    case 56:
                        setMesaIcon(jCheckBoxMesas6, estado, idMesa);
                        break;
                    case 7:
                    case 17:
                    case 27:
                    case 37:
                    case 47:
                    case 57:
                        setMesaIcon(jCheckBoxMesas7, estado, idMesa);
                        break;
                    case 8:
                    case 18:
                    case 28:
                    case 38:
                    case 48:
                    case 58:
                        setMesaIcon(jCheckBoxMesas8, estado, idMesa);
                        break;
                    case 9:
                    case 19:
                    case 29:
                    case 39:
                    case 49:
                    case 59:
                        setMesaIcon(jCheckBoxMesas9, estado, idMesa);
                        break;
                    case 10:
                    case 20:
                    case 30:
                    case 40:
                    case 50:
                    case 60:
                        setMesaIcon(jCheckBoxMesas10, estado, idMesa);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } finally {
            ConexionBD.closeConnection(connection);
        }
    }

    private void setMesaIcon(JCheckBox checkBox, int estado, int idMesa) {
        String iconPath = "";
        String selectedIconPath = "";

        switch (estado) {
            case 1:
                iconPath = "/Recursos/MesaDisponible.png";
                selectedIconPath = "/Recursos/MesaDisponibleS.png";
                break;
            case 2:
                iconPath = "/Recursos/MesaOcupada.png";
                selectedIconPath = "/Recursos/MesaOcupadaS.png";
                break;
            case 3:
                iconPath = "/Recursos/MesaProceso.png";
                selectedIconPath = "/Recursos/MesaProcesoS.png";
                break;
            // Add more cases if needed

            default:
                // Default icon paths for unknown estado values
                iconPath = "/Recursos/DefaultMesaIcon.png";
                selectedIconPath = "/Recursos/DefaultMesaIconS.png";
                break;
        }

        URL iconURL = getClass().getResource(iconPath);
        URL selectedIconURL = getClass().getResource(selectedIconPath);

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIdMesa = idMesa; // Update selectedIdMesa with the correct idMesa
            }
        });

        if (iconURL != null && selectedIconURL != null) {
            checkBox.setIcon(new ImageIcon(iconURL));
            checkBox.setSelectedIcon(new ImageIcon(selectedIconURL));
        } else {
            // Handle the case where the resource is not found
            System.out.println("Resource not found for estado " + estado);
        }
    }

    private int getEstadoMesa(int mesaId) {
        Connection connection = ConexionBD.getConnection();
        String query = "SELECT estado FROM mesa WHERE idMesa = ?";
        int estado = -1;

        try ( PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, mesaId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                estado = resultSet.getInt("estado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.closeConnection(connection);
        }

        return estado;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelAreaA = new javax.swing.JPanel();
        jLabelAreaA = new javax.swing.JLabel();
        jCheckBoxMesas1 = new javax.swing.JCheckBox();
        jCheckBoxMesas2 = new javax.swing.JCheckBox();
        jCheckBoxMesas3 = new javax.swing.JCheckBox();
        jPanelAreaC = new javax.swing.JPanel();
        jCheckBoxMesas8 = new javax.swing.JCheckBox();
        jCheckBoxMesas9 = new javax.swing.JCheckBox();
        jLabelAreaC = new javax.swing.JLabel();
        jCheckBoxMesas10 = new javax.swing.JCheckBox();
        jCheckBoxMesas11 = new javax.swing.JCheckBox();
        jPanelAreaB = new javax.swing.JPanel();
        jLabelAreaB = new javax.swing.JLabel();
        jCheckBoxMesas4 = new javax.swing.JCheckBox();
        jCheckBoxMesas5 = new javax.swing.JCheckBox();
        jCheckBoxMesas6 = new javax.swing.JCheckBox();
        jCheckBoxMesas7 = new javax.swing.JCheckBox();
        jButtonRegresar = new javax.swing.JButton();
        jLabelSucursal = new javax.swing.JLabel();
        jLabelDireccion = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(168, 46, 38));

        jPanelAreaA.setBackground(new java.awt.Color(222, 164, 126));
        jPanelAreaA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 255)));

        jLabelAreaA.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabelAreaA.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAreaA.setText("Area C");

        jCheckBoxMesas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N
        jCheckBoxMesas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMesas1ActionPerformed(evt);
            }
        });

        jCheckBoxMesas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaProceso.png"))); // NOI18N
        jCheckBoxMesas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMesas2ActionPerformed(evt);
            }
        });

        jCheckBoxMesas3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N
        jCheckBoxMesas3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMesas3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAreaALayout = new javax.swing.GroupLayout(jPanelAreaA);
        jPanelAreaA.setLayout(jPanelAreaALayout);
        jPanelAreaALayout.setHorizontalGroup(
            jPanelAreaALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaALayout.createSequentialGroup()
                .addGroup(jPanelAreaALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreaALayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxMesas1))
                    .addGroup(jPanelAreaALayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxMesas2))
                    .addGroup(jPanelAreaALayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxMesas3))
                    .addGroup(jPanelAreaALayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabelAreaA)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAreaALayout.setVerticalGroup(
            jPanelAreaALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaALayout.createSequentialGroup()
                .addComponent(jLabelAreaA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxMesas1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxMesas2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxMesas3))
        );

        jPanelAreaC.setBackground(new java.awt.Color(222, 164, 126));
        jPanelAreaC.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));

        jCheckBoxMesas8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N
        jCheckBoxMesas8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMesas8ActionPerformed(evt);
            }
        });

        jCheckBoxMesas9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N

        jLabelAreaC.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabelAreaC.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAreaC.setText("Area A");

        jCheckBoxMesas10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N

        jCheckBoxMesas11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaOcupada.png"))); // NOI18N

        javax.swing.GroupLayout jPanelAreaCLayout = new javax.swing.GroupLayout(jPanelAreaC);
        jPanelAreaC.setLayout(jPanelAreaCLayout);
        jPanelAreaCLayout.setHorizontalGroup(
            jPanelAreaCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaCLayout.createSequentialGroup()
                .addGroup(jPanelAreaCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreaCLayout.createSequentialGroup()
                        .addComponent(jCheckBoxMesas8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxMesas9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxMesas11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxMesas10))
                    .addGroup(jPanelAreaCLayout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jLabelAreaC)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAreaCLayout.setVerticalGroup(
            jPanelAreaCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelAreaC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAreaCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxMesas9)
                    .addGroup(jPanelAreaCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBoxMesas8, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jCheckBoxMesas11, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jCheckBoxMesas10, javax.swing.GroupLayout.Alignment.TRAILING))))
        );

        jPanelAreaB.setBackground(new java.awt.Color(222, 164, 126));
        jPanelAreaB.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 204, 255)));

        jLabelAreaB.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabelAreaB.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAreaB.setText("Area B");

        jCheckBoxMesas4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N

        jCheckBoxMesas5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaOcupada.png"))); // NOI18N

        jCheckBoxMesas6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N

        jCheckBoxMesas7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/MesaDisponible.png"))); // NOI18N

        javax.swing.GroupLayout jPanelAreaBLayout = new javax.swing.GroupLayout(jPanelAreaB);
        jPanelAreaB.setLayout(jPanelAreaBLayout);
        jPanelAreaBLayout.setHorizontalGroup(
            jPanelAreaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaBLayout.createSequentialGroup()
                .addGroup(jPanelAreaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreaBLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxMesas5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxMesas7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxMesas6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxMesas4))
                    .addGroup(jPanelAreaBLayout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jLabelAreaB)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelAreaBLayout.setVerticalGroup(
            jPanelAreaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAreaBLayout.createSequentialGroup()
                .addGroup(jPanelAreaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreaBLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jCheckBoxMesas5))
                    .addGroup(jPanelAreaBLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelAreaB)
                        .addGap(28, 28, 28)
                        .addGroup(jPanelAreaBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxMesas4)
                            .addComponent(jCheckBoxMesas6)
                            .addComponent(jCheckBoxMesas7))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/BotonRegresar.png"))); // NOI18N
        jButtonRegresar.setPreferredSize(new java.awt.Dimension(30, 30));
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jLabelSucursal.setFont(new java.awt.Font("Bahiana", 0, 36)); // NOI18N
        jLabelSucursal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSucursal.setText("Sucursal");

        jLabelDireccion.setFont(new java.awt.Font("Bahiana", 0, 36)); // NOI18N
        jLabelDireccion.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDireccion.setText("Dirección");

        jLabelTelefono.setFont(new java.awt.Font("Bahiana", 0, 36)); // NOI18N
        jLabelTelefono.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTelefono.setText("Teléfono");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("(teléfono de sucursal)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("(Nombre Sucursal)");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("(Direccion de sucursal)");

        jButton2.setBackground(new java.awt.Color(102, 201, 205));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("RESERVAR");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 102, 51), null));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelTelefono)
                                        .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelDireccion))
                                    .addGap(163, 163, 163))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(73, 73, 73)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelAreaB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelAreaC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38)
                .addComponent(jPanelAreaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(981, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabelSucursal, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(jLabelDireccion)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabelTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanelAreaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelAreaB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanelAreaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 89, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(148, 148, 148)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(452, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButtonRegresarActionPerformed

    private void jCheckBoxMesas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMesas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMesas1ActionPerformed

    private void jCheckBoxMesas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMesas2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMesas2ActionPerformed

    private void jCheckBoxMesas3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMesas3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMesas3ActionPerformed

    private void jCheckBoxMesas8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMesas8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxMesas8ActionPerformed

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
            java.util.logging.Logger.getLogger(Mesas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mesas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mesas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mesas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mesas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JCheckBox jCheckBoxMesas1;
    private javax.swing.JCheckBox jCheckBoxMesas10;
    private javax.swing.JCheckBox jCheckBoxMesas11;
    private javax.swing.JCheckBox jCheckBoxMesas2;
    private javax.swing.JCheckBox jCheckBoxMesas3;
    private javax.swing.JCheckBox jCheckBoxMesas4;
    private javax.swing.JCheckBox jCheckBoxMesas5;
    private javax.swing.JCheckBox jCheckBoxMesas6;
    private javax.swing.JCheckBox jCheckBoxMesas7;
    private javax.swing.JCheckBox jCheckBoxMesas8;
    private javax.swing.JCheckBox jCheckBoxMesas9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelAreaA;
    private javax.swing.JLabel jLabelAreaB;
    private javax.swing.JLabel jLabelAreaC;
    private javax.swing.JLabel jLabelDireccion;
    private javax.swing.JLabel jLabelSucursal;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAreaA;
    private javax.swing.JPanel jPanelAreaB;
    private javax.swing.JPanel jPanelAreaC;
    // End of variables declaration//GEN-END:variables
}
