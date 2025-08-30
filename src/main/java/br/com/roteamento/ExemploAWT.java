import java.awt.*;
import java.awt.event.*;

public class ExemploAWT extends Frame {
    public ExemploAWT() {
        setTitle("Janela AWT");
        setSize(300, 200);
        
        Button botao = new Button("Clique Aqui");
        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botão clicado!");
            }
        });
        
        add(botao, BorderLayout.CENTER);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ExemploAWT();
    }
}
