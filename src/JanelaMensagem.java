import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaMensagem extends JFrame{
    
    public JanelaMensagem( String mensagem ){
        //Variáveis auxiliares
        JButton jb = new JButton( "OK" );
        JLabel jl = new JLabel( "<html><p>" + mensagem + "</p></html>", JLabel.CENTER );
        JPanel jp = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 20 ) );
        
        //Painel superior: Texto
        jl.setBorder( BorderFactory.createEmptyBorder( 20, 20, 0, 20 ) );
        add( jl, BorderLayout.NORTH );
        
        //Painel inferior: Botão
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                dispose();
            }
        });
        jp.add( jb );
        add( jp, BorderLayout.SOUTH );
        
        //Configurações da janela
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setResizable( false );
        setSize( 350, 175 );
        setTitle( "Mensagem" );
        setLocationRelativeTo( null );
        setVisible( true );
    }
}
