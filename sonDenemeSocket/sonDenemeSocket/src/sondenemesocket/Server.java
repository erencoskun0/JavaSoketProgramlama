package sondenemesocket;


import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Eren
 */
public class Server extends javax.swing.JFrame {
    public static DataInputStream din;
    public static DataOutputStream dout;
    String id;
    Socket clientSocket;
    ServerSocket server;
    
    
    
     // Sunucu soketi oluşturulur ve 3000 numaralı porttan gelen bağlantılar dinlenir
    public Server() {
        initComponents();
        try {
            server = new ServerSocket(3000);
            sunucudurumu.setText("Sunucu Aktif");
                    
            new ClientAccept().start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   // İstemci bağlantılarını kabul etmek için kullaılan thread
    
    class ClientAccept extends Thread{
        static ArrayList<MsgRead> clients = new ArrayList<>();

        public void run(){
            while(true){
                try {
                    clientSocket = server.accept();
                   String id=new DataInputStream(clientSocket.getInputStream()).readUTF();
                   mesajkutusu.append(id + " Katıldı! \n");
                   dout = new DataOutputStream(clientSocket.getOutputStream());
                   dout.writeUTF("");
                   
                    // Bu istemciden gelen mesajları okumak için bir thread başlattır
                    MsgRead msgRead = new MsgRead(clientSocket, id);
                msgRead.start();
                 clients.add(msgRead);
                } catch (Exception e) {
                    e.printStackTrace();
                }      
            }
        }
    }   
    
    // İstemcilerden gelen mesajları okumak ve diğer istemcilere iletmek için kullanılan thread
    class MsgRead extends Thread { 
        static ArrayList<MsgRead> clients = new ArrayList<>();
        Socket clientSocket;
        String id;
        MsgRead(Socket clientSocket, String id){
            this.clientSocket=clientSocket;
            this.id=id;
            clients.add(this);
        }
        public void run(){
            while(true){
                
            try {
                String mesaj = new DataInputStream(clientSocket.getInputStream()).readUTF();
                mesajkutusu.append(id + " : " + mesaj + "\n"); 
                for (MsgRead client : clients) {
                    if (client != this) {
                        DataOutputStream dos = new DataOutputStream(client.clientSocket.getOutputStream());
                        dos.writeUTF(id + " : " + mesaj);
                    }
                }
            } catch (Exception e) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        mesajkutusu = new javax.swing.JTextArea();
        mesajmetni = new javax.swing.JTextField();
        gonder = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sunucudurumu = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");

        mesajkutusu.setColumns(20);
        mesajkutusu.setRows(5);
        jScrollPane1.setViewportView(mesajkutusu);

        gonder.setText("Gönder");
        gonder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gonderActionPerformed(evt);
            }
        });

        jLabel1.setText("Sunucu Durumu:");

        sunucudurumu.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sunucudurumu, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mesajmetni, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(gonder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(sunucudurumu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mesajmetni, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gonder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void gonderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gonderActionPerformed
        
        try {
         String mesaj = mesajmetni.getText();
         dout.writeUTF("Server : " + mesaj);
         mesajmetni.setText("");
         mesajkutusu.append("Server : " + mesaj + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_gonderActionPerformed

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
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gonder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea mesajkutusu;
    private javax.swing.JTextField mesajmetni;
    private javax.swing.JLabel sunucudurumu;
    // End of variables declaration//GEN-END:variables
}
