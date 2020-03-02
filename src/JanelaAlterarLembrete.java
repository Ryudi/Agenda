import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaAlterarLembrete extends JFrame{
    
    public JanelaAlterarLembrete( JanelaAgenda janelaAgendaInvocadora, Lembrete lembrete, String[] assuntos ){
        //Variáveis auxiliares
        Font f;
        JButton jb;
        JLabel jl;
        JPanel jp, jp2;
        
        //Inicialização dos campos de dados
        f = new Font( Font.SERIF, Font.PLAIN, 20 );
        JComboBox jcbAssuntos = new JComboBox();
        for( int i = 0; i < assuntos.length; i++ )
            jcbAssuntos.addItem( assuntos[i] );
        jcbAssuntos.setSelectedItem( lembrete.getAssunto() );
        JTextField lacunaTitulo = new JTextField( lembrete.getTitulo(), 25 );
        lacunaTitulo.setFont( f );
        JTextArea lacunaDetalhes = new JTextArea( lembrete.getDetalhes());
        lacunaDetalhes.setFont( f );
        lacunaDetalhes.setLineWrap( true );
        lacunaDetalhes.setWrapStyleWord( true );
        
        //Painel superior: Campos do lembrete
        jp = new JPanel( new GridLayout( 4, 1 ) );
        jp.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Data:</b> " + lembrete.data() );
        jl.setFont( new Font( Font.SERIF, Font.PLAIN, 20 ) );
        jp2.add( jl );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Assunto: " );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( jcbAssuntos );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "<html><b>Título: " );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( lacunaTitulo );
        jp.add( jp2 );
        jl = new JLabel( "<html><b>Detalhes (opcional)", JLabel.CENTER );
        jl.setFont( f );
        jp.add( jl );
        add( jp, BorderLayout.NORTH );
        
        //Painel central: Grande lacuna para inserção de detalhes opcionais
        jp = new JPanel( new GridLayout( 1, 1 ) );
        jp.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
        jp.add( new JScrollPane( lacunaDetalhes ) );
        add( jp, BorderLayout.CENTER );
        
        //Painel inferior: Botôes
        jp = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 20 ) );
        jb = new JButton( "OK" );
        //Alteração e arquivamento do novo lembrete
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                String titulo = lacunaTitulo.getText();
                //Tratamento de exceção
                if( titulo.isEmpty() ){
                    new JanelaMensagem( "Erro: O lembrete precisa ter um título." );
                    return;
                }
                lembrete.setAssunto( (String)jcbAssuntos.getSelectedItem() );
                lembrete.setTitulo( titulo );
                lembrete.setDetalhes( lacunaDetalhes.getText() );
                Lembrete.salva();
                janelaAgendaInvocadora.atualiza();
                new JanelaMensagem( "O lembrete foi alterado com sucesso." );
                dispose();
            }
        });
        jp.add( jb );
        add( jp, BorderLayout.SOUTH );
        
        //Configurações da janela
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setResizable( false );
        setSize( 500, 375 );
        setTitle( "Alterar lembrete" );
        setVisible( true );
    }
}