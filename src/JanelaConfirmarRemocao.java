import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaConfirmarRemocao extends JFrame{
    
    //Macros que indicam quais lembretes serão removidos
    public static final int DETERMINADO = 0;
    public static final int ANTIGOS = 1;
    public static final int TODOS = 2;
    
    public JanelaConfirmarRemocao( JFrame janelaInvocadora, int categoriaRemocao ){
        //Variáveis auxiliares
        JButton jb;
        JLabel jl;
        JPanel jp = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 20 ) );
        
        //Painel superior: Texto
        if( categoriaRemocao == DETERMINADO )
            jl = new JLabel( "<html><p>Deseja mesmo remover este lembrete?</p></html>", JLabel.CENTER );
        else if( categoriaRemocao == ANTIGOS )
            jl = new JLabel( "<html><p>Deseja mesmo remover os lembretes antigos?</p></html>", JLabel.CENTER );
        else
            jl = new JLabel( "<html><p>Deseja mesmo remover todos os lembretes?</p></html>", JLabel.CENTER );
        jl.setBorder( BorderFactory.createEmptyBorder( 20, 20, 0, 20 ) );
        add( jl, BorderLayout.NORTH );
        
        //Painel inferior: Botôes
        jb = new JButton( "Sim" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                if( janelaInvocadora instanceof JanelaAgenda ){
                    JanelaAgenda ja = (JanelaAgenda)janelaInvocadora;
                    if( categoriaRemocao == ANTIGOS )
                        ja.removeLembretesAntigos();
                    else if( categoriaRemocao == TODOS )
                        ja.removeTodosLembretes();
                    dispose();
                }else if( janelaInvocadora instanceof JanelaLembrete ){
                    JanelaLembrete jl = (JanelaLembrete)janelaInvocadora;
                    jl.removeLembrete();
                    dispose();
                }
            }
        });
        jp.add( jb );
        jb = new JButton( "Não" );
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
        setTitle( "Confirmar remoção" );
        setLocationRelativeTo( null );
        setVisible( true );
    }
}
