package br.com.roteamento;

import javax.swing.*;
import java.awt.event.*;

public class ExemploSwing extends JFrame {
    public ExemploSwing() {
        setTitle("Janela Swing");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton botao = new JButton("Clique Aqui");
        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Botão clicado!");
            }
        });
        
        add(botao);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExemploSwing());
    }
}
