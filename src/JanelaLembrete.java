import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaLembrete extends JFrame{
    
    private JanelaAgenda janelaAgendaInvocadora;
    private Lembrete lembrete;
    
    public JanelaLembrete( JanelaAgenda janelaAgendaInvocadora, Lembrete lembrete, String[] assuntos ){
        this.janelaAgendaInvocadora = janelaAgendaInvocadora;
        this.lembrete = lembrete;
        
        //Variáveis auxiliares
        Font f = new Font( Font.SERIF, Font.PLAIN, 20 );
        JanelaLembrete janelaLembrete = this;
        JButton jb;
        JLabel jl;
        JPanel jp, jp2;
        
        //Painel superior: Dados do lembrete
        jp = new JPanel( new GridLayout( 4, 1 ) );
        jp.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Data:</b> " + lembrete.data() );
        jl.setFont( f );
        jp2.add( jl );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Assunto:</b> " + lembrete.getAssunto() );
        jl.setFont( f );
        jp2.add( jl );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Título:</b> " + lembrete.getTitulo() );
        jl.setFont( f );
        jp2.add( jl );
        jp.add( jp2 );
        if( !lembrete.getDetalhes().isEmpty() ){
            jp2 = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
            jl = new JLabel( "<html><b>Detalhes" );
            jl.setFont( f );
            jp2.add( jl );
            jp.add( jp2 );
            //Painel central: Detalhes do lembrete( se houver ).
            jp2 = new JPanel( new GridLayout( 1, 1 ) );
            jp2.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
            JTextArea jta = new JTextArea( lembrete.getDetalhes() );
            jta.setEditable( false );
            jta.setFont( f );
            jta.setLineWrap( true );
            jta.setWrapStyleWord( true );
            JScrollPane jsp = new JScrollPane( jta );
            jp2.add( jsp );
            add( jp2, BorderLayout.CENTER );
        }
        add( jp, BorderLayout.NORTH );
        
        //Painel inferior: Botões
        jp = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 20 ) );
        jb = new JButton( "Alterar" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                new JanelaAlterarLembrete( janelaAgendaInvocadora, lembrete, assuntos );
                dispose();
            }
        });
        jp.add( jb );
        jb = new JButton( "Remover" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                new JanelaConfirmarRemocao( janelaLembrete, JanelaConfirmarRemocao.DETERMINADO );
            }
        });
        jp.add( jb );
        add( jp, BorderLayout.SOUTH );
        
        //Configurações da janela
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setResizable( false );
        if( lembrete.getDetalhes().isEmpty() )
            setSize( 500, 250 );
        else
            setSize( 500, 375 );
        setTitle( "Lembrete" );
        setVisible( true );
    }
    
    public void removeLembrete(){
        Lembrete.remove( lembrete );
        janelaAgendaInvocadora.atualiza();
        dispose();
    }
}
