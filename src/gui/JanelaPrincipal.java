package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import servidor.Servidor;

/**
 *
 * @author Giovanne
 */
class JanelaPrincipal extends JFrame implements ActionListener {

    private final int altura = 400;
    private final int largura = 400;

    private final Servidor servidor = new Servidor();

    private JButton btnStartStop;
    private ImageIcon imgStart;
    private ImageIcon imgStop;

    private JPanel panel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JLabel statusLabel = new JLabel("Parado");

    public JanelaPrincipal() {

        imgStart = new ImageIcon(getClass().getResource("ServidorOff.png"));
        imgStop = new ImageIcon(getClass().getResource("ServidorOn.png"));

        btnStartStop = new JButton(imgStart);
        btnStartStop.setBorder(BorderFactory.createEmptyBorder());
        btnStartStop.setContentAreaFilled(false);
        btnStartStop.setOpaque(false);
        btnStartStop.addActionListener(this);

//        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 30));

        panel.add(btnStartStop);

        this.setLayout(new BorderLayout());
        this.add(panel);
        this.add(statusPanel, BorderLayout.SOUTH);
        this.setSize(altura, largura);
        Dimension TamTela = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((TamTela.width - largura) / 2, (TamTela.height - altura) / 2);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Servidor");
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStartStop) {
            if (servidor.getStatus()) {
                System.out.println("Parando o servidor...");
                servidor.kill();
                System.out.println("Servidor parado com sucesso!");
                btnStartStop.setIcon(imgStart);
                statusLabel.setText("Parado");
                this.dispose();
            } else {
                System.out.println("Iniciando o servidor...");
                servidor.start();
                System.out.println("Servidor iniciado com sucesso!");
                btnStartStop.setIcon(imgStop);
                statusLabel.setText("Iniciado");
            }
        }
    }
}
