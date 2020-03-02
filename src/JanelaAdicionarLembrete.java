import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaAdicionarLembrete extends JFrame{
    
    public JanelaAdicionarLembrete( JanelaAgenda janelaAgendaInvocadora, String[] assuntos, int diaDefault, int mesDefault, int anoDefault ){
        //Variáveis auxiliares
        Font f;
        JButton jb;
        JLabel jl;
        JPanel jp, jp2;
        
        //Inicialização dos campos de dados
        boolean dataPreDefinida;
        String strDia, strMes;
        if( diaDefault == 0 || mesDefault == 0 || anoDefault == 0 ){
            dataPreDefinida = false;
            strDia = "";
            strMes = "";
        }else{
            dataPreDefinida = true;
            strDia = String.valueOf( diaDefault );
            strMes = String.valueOf( mesDefault );
        }
        f = new Font( Font.SERIF, Font.PLAIN, 20 );
        JTextField lacunaDia, lacunaMes, lacunaAno, lacunaTitulo;
        JComboBox jcbAssuntos;
        JTextArea lacunaDetalhes;
        lacunaDia = new JTextField( strDia, 2 );
        lacunaDia.setFont( f );
        lacunaMes = new JTextField( strMes, 2 );
        lacunaMes.setFont( f );
        lacunaAno = new JTextField( String.valueOf( anoDefault ), 4 );
        lacunaAno.setFont( f );
        jcbAssuntos = new JComboBox();
        for( String assunto : assuntos )
            jcbAssuntos.addItem( assunto );
        lacunaTitulo = new JTextField( 25 );
        lacunaTitulo.setFont( f );
        lacunaDetalhes = new JTextArea();
        lacunaDetalhes.setFont( f );
        lacunaDetalhes.setLineWrap( true );
        lacunaDetalhes.setWrapStyleWord( true );
        
        f = new Font( Font.SERIF, Font.BOLD, 20 );
        //Painel superior: Campos do lembrete
        jp = new JPanel( new GridLayout( 4, 1 ) );
        jp.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "Data: " );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( lacunaDia );
        jl = new JLabel( "/" );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( lacunaMes );
        jl = new JLabel( "/" );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( lacunaAno );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "Assunto: " );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( jcbAssuntos );
        jp.add( jp2 );
        jp2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        jl = new JLabel( "Título: " );
        jl.setFont( f );
        jp2.add( jl );
        jp2.add( lacunaTitulo );
        jp.add( jp2 );
        jl = new JLabel( "Detalhes (opcional)", JLabel.CENTER );
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
        //Criação e arquivamento do novo lembrete
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                String stringDia = lacunaDia.getText(), stringMes = lacunaMes.getText(), stringAno = lacunaAno.getText();
                String assunto = (String)jcbAssuntos.getSelectedItem(), titulo = lacunaTitulo.getText(), detalhes = lacunaDetalhes.getText();
                try{
                    int dia = Integer.parseInt( stringDia );
                    int mes = Integer.parseInt( stringMes );
                    int ano = Integer.parseInt( stringAno );
                    //Tratamento de exceções
                    if( dia < 1 || dia > 31 || mes < 1 || mes > 12 ){
                        new JanelaMensagem( "Erro: A data possui valor inválido." );
                        return;
                    }
                    if( titulo.isEmpty() ){
                        new JanelaMensagem( "Erro: O lembrete precisa ter um título." );
                        return;
                    }
                    if( !Lembrete.insere( new Lembrete( dia, mes, ano, assunto, titulo, detalhes ) ) )
                        return;
                    janelaAgendaInvocadora.atualiza();
                    new JanelaMensagem( "O lembrete foi adicionado com sucesso." );
                    dispose();
                }catch( NumberFormatException e ){
                    new JanelaMensagem( "Erro: O formato da data está incorreto." );
                }
            }
        });
        jp.add( jb );
        add( jp, BorderLayout.SOUTH );
        
        //Configurações da janela
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setResizable( false );
        setSize( 500, 375 );
        setTitle( "Adicionar lembrete" );
        setVisible( true );
        if( dataPreDefinida )
            lacunaTitulo.requestFocusInWindow();
    }
}
