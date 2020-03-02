import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.*;

public class JanelaAgenda extends JFrame{
    
    public static void main( String args[] ){
        AssuntosCores ac = new AssuntosCores();
        ac.add( "Prova", Color.CYAN );
        ac.add( "Prazo de trabalho", Color.PINK );
        ac.add( "Etc.", Color.YELLOW );
        new JanelaAgenda( ac );
    }
    
    private AssuntosCores assuntosCores;
    
    private int diaAtual;
    private int mesAtual;
    private int anoAtual;
    
    private Calendar calendario;
    private int mes;
    private int ano;
    private int[][] hashLembretes;
    
    private JLabel tituloCalendario;
    private JTable tabelaCalendario;
    private JTable tabelaLembretes;
    
    public JanelaAgenda( AssuntosCores assuntosCores ){
        this.assuntosCores = assuntosCores;
        
        //Armazenamento da data atual
        calendario = Calendar.getInstance();
        diaAtual = calendario.get( Calendar.DAY_OF_MONTH );
        mesAtual = calendario.get( Calendar.MONTH ) + 1;
        anoAtual = calendario.get( Calendar.YEAR );
        
        //Variáveis auxiliares
        DefaultTableCellRenderer dtcr;
        TableColumnModel tcm;
        JanelaAgenda ja = this;
        JButton jb;
        JLabel jl;
        JPanel jp, jp2, jp3, jp4, jp5;
        String[] assuntos = assuntosCores.getAssuntos();
        
        /**********************************
         * Painel da esquerda: Calendário *
         **********************************/
        jp = new JPanel( new BorderLayout() );
        jp.setBorder( new CompoundBorder( BorderFactory.createEmptyBorder( -1, -1, -1, 0 ), BorderFactory.createDashedBorder( null, 30, 15 ) ) );
        
        //Título do calendário
        tituloCalendario = new JLabel();
        tituloCalendario.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        jp2 = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 10 ) );
        jp2.add( tituloCalendario );
        jp.add( jp2, BorderLayout.NORTH );
        
        //Tabela do calendário
        tabelaCalendario = new JTable( new String[6][7], new String[]{ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" } );
        tabelaCalendario.setCellSelectionEnabled( true );
        tabelaCalendario.setFont( new Font( Font.SERIF, Font.BOLD, 24 ) );
        tabelaCalendario.setRowHeight( 60 );
        tabelaCalendario.setPreferredScrollableViewportSize( tabelaCalendario.getPreferredSize() );
        //Cabeçalho
        JTableHeader jth = tabelaCalendario.getTableHeader();
        jth.setFont( new Font( Font.SERIF, Font.PLAIN, 24 ) );
        jth.setReorderingAllowed( false );
        jth.setResizingAllowed( false );
        TableCellRenderer tcr = new TableCellRenderer(){
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ){
                Component c = jth.getDefaultRenderer().getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                c.setForeground( Color.RED );
                return c;
            }
        };
        tcm = tabelaCalendario.getColumnModel();
        tcm.getColumn(0).setHeaderRenderer( tcr );
        tcm.getColumn(6).setHeaderRenderer( tcr );
        //Conteúdo
        Lembrete.carrega();
        calendario = new GregorianCalendar( anoAtual, mesAtual - 1, 1 );
        atualizaCalendario();
        dtcr = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ){
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                setHorizontalAlignment( SwingConstants.CENTER );
                //Pintura de fundo para diferenciar dias com lembrete( uma cor para cada assunto de lembrete )
                if( hashLembretes[row][column] >= 0 ){
                    String assunto = Lembrete.get( hashLembretes[row][column] ).getAssunto();
                    String[] assuntos = assuntosCores.getAssuntos();
                    Color[] cores = assuntosCores.getCores();
                    for( int i = 0; i < assuntosCores.size(); i++ )
                        if( assunto.equals( assuntos[i] ) ){
                            setBackground( cores[i] );
                            break;
                        }
                }else
                    setBackground( Color.WHITE );
                //Pintura de fonte para diferenciar dia atual( azul ), dias de semana( preto )e final de semana( vermelho )
                String diaStr = (String)tabelaCalendario.getValueAt( row, column );
                if( diaStr.isEmpty() )
                    return this;
                else if( mes == mesAtual && ano == anoAtual && Integer.parseInt(diaStr) == diaAtual )
                    setForeground( Color.BLUE );
                else if( column == 0 || column == 6 )
                    setForeground( Color.RED );
                else
                    setForeground( Color.BLACK );
                return this;
            }
        };
        for( int i = 0; i < 7; i++ )
            tcm.getColumn(i).setCellRenderer( dtcr );
        //Eventos de clique
        tabelaCalendario.addMouseListener( new MouseAdapter(){
            public void mouseClicked( MouseEvent e ){
                Point p = e.getPoint();
                int linha = tabelaCalendario.rowAtPoint( p ), coluna = tabelaCalendario.columnAtPoint( p );
                String diaStr = (String)tabelaCalendario.getValueAt( linha, coluna );
                if( diaStr.isEmpty() )
                    return;
                else if( hashLembretes[linha][coluna] == -1 )
                   new JanelaAdicionarLembrete( ja, assuntos, Integer.parseInt( diaStr ), mes, ano );
                else
                    new JanelaLembrete( ja, Lembrete.get( hashLembretes[linha][coluna] ), assuntos );
            }
        });
        jp.add( new JScrollPane( tabelaCalendario ), BorderLayout.CENTER );
        
        //Botão "<"
        jp2 = new JPanel( new GridBagLayout() );
        jp3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
        jb = new JButton( "<" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                if( mes == 1 )
                    calendario = new GregorianCalendar( ano - 1, 11, 1 );
                else
                    calendario = new GregorianCalendar( ano, mes - 2, 1 );
                atualizaCalendario();
            }
        });
        jb.setFont( new Font( Font.DIALOG_INPUT, Font.BOLD, 30 ) );
        jp3.add( jb );
        jp2.add( jp3 );
        jp.add( jp2, BorderLayout.WEST );
        
        //Botão ">"
        jp2 = new JPanel( new GridBagLayout() );
        jp3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
        jb = new JButton( ">" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                if( mes == 12 )
                    calendario = new GregorianCalendar( ano + 1, 0, 1 );
                else
                    calendario = new GregorianCalendar( ano, mes, 1 );
                atualizaCalendario();
            }
        });
        jb.setFont( new Font( Font.DIALOG_INPUT, Font.BOLD, 30 ) );
        jp3.add( jb );
        jp2.add( jp3 );
        jp.add( jp2, BorderLayout.EAST );
        
        //Legenda e botão
        jp2 = new JPanel( new BorderLayout() );
        //Legenda
        jp3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 20, 0 ) );
        jp3.setBorder( BorderFactory.createEmptyBorder( 20, 0, 0, 0 ) );
        for( int i = 0; i < assuntosCores.size(); i++ ){
            jp4 = new JPanel( new FlowLayout( FlowLayout.CENTER, 4, 0 ) );
            jp5 = new JPanel();
            jp5.setBackground( assuntosCores.getCores()[i] );
            jp5.setBorder( BorderFactory.createMatteBorder( 1, 1, 1, 1, Color.BLACK ) );
            jp5.setPreferredSize( new Dimension( 30, 30 ) );
            jl = new JLabel( assuntos[i] );
            jl.setFont( new Font( Font.SERIF, Font.PLAIN, 20 ) );
            jp4.add( jp5 );
            jp4.add( jl );
            jp3.add( jp4 );
        }
        jp2.add( jp3, BorderLayout.CENTER );
        //Botão
        jp3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 20 ) );
        jb = new JButton( "Adicionar lembrete" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                new JanelaAdicionarLembrete( ja, assuntos, 0, 0, ano );
            }
        });
        jp3.add( jb );
        jp2.add( jp3, BorderLayout.SOUTH );
        jp.add( jp2, BorderLayout.SOUTH );
        add( jp, BorderLayout.WEST );
        
        /*****************************************
         * Painel da direita: Lista de lembretes *
         *****************************************/
        jp = new JPanel( new BorderLayout() );
        
        //Título
        jp2 = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 10 ) );
        jl = new JLabel( "Lembretes" );
        jl.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        jp2.add( jl );
        jp.add( jp2, BorderLayout.NORTH );
        
        //Lista de lembretes
        tabelaLembretes = new JTable();
        tabelaLembretes.setFont( new Font( Font.SERIF, Font.PLAIN, 20 ) );
        tabelaLembretes.setRowHeight( 30 );
        //Cabeçalho
        JTableHeader jth2 = tabelaLembretes.getTableHeader();
        jth2.setFont( new Font( Font.SERIF, Font.PLAIN, 20 ) );
        jth2.setReorderingAllowed( false );
        jth2.setResizingAllowed( false );
        DefaultTableModel dtm = (DefaultTableModel)tabelaLembretes.getModel();
        dtm.setColumnIdentifiers( new String[]{ "Data", "Assunto", "Título" } );
        tcm = tabelaLembretes.getColumnModel();
        tcm.getColumn(0).setPreferredWidth( 120 );
        tcm.getColumn(1).setPreferredWidth( 120 );
        tcm.getColumn(2).setPreferredWidth( 240 );
        tabelaLembretes.setPreferredScrollableViewportSize( tabelaLembretes.getPreferredSize() );
        //Conteúdo
        atualizaLista();
        dtcr = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ){
                Lembrete l = Lembrete.get( row );
                super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                if( column == 2 )
                    setHorizontalAlignment( SwingConstants.LEFT );
                else
                    setHorizontalAlignment( SwingConstants.CENTER );
                //Fundo cinza para diferenciar lembretes de data passada
                if( l.compareTo( new Lembrete( diaAtual, mesAtual, anoAtual ) ) < 0 )
                    setBackground( Color.LIGHT_GRAY );
                else
                    setBackground( Color.WHITE );
                //Fonte azul para diferenciar lembretes com detalhes
                if( l.getDetalhes().isEmpty() )
                    setForeground( Color.BLACK );
                else
                    setForeground( Color.BLUE );
                return this;
            }
        };
        tcm.getColumn(0).setCellRenderer( dtcr );
        tcm.getColumn(1).setCellRenderer( dtcr );
        tcm.getColumn(2).setCellRenderer( dtcr );
        //Eventos de clique
        tabelaLembretes.addMouseListener( new MouseAdapter(){
            public void mouseClicked( MouseEvent e ){
                Point p = e.getPoint();
                int linha = tabelaLembretes.rowAtPoint( p );
                new JanelaLembrete( ja, Lembrete.get( linha ), assuntos );
            }
        });
        jp2 = new JPanel( new GridLayout( 1, 1 ) );
        jp2.setBorder( BorderFactory.createEmptyBorder( 0, 20, 0, 20 ) );
        jp2.add( new JScrollPane( tabelaLembretes ) );
        jp.add( jp2, BorderLayout.CENTER );
        
        //Legenda e botões
        jp2 = new JPanel( new BorderLayout() );
        //Legenda
        jl = new JLabel( "<html>- Fundo cinza: Lembrete de data passada<br/>- Fonte azul: Lembrete com detalhes</html>" );
        jl.setBorder( BorderFactory.createEmptyBorder( 10, 20, 0, 20 ) );
        jl.setFont( new Font( Font.SERIF, Font.PLAIN, 20 ) );
        jp2.add( jl, BorderLayout.CENTER );
        //Botões
        jp3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 100, 20 ) );
        jb = new JButton( "Remover antigos" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                new JanelaConfirmarRemocao( ja, JanelaConfirmarRemocao.ANTIGOS );
            }
        });
        jp3.add( jb );
        jb = new JButton( "Remover todos" );
        jb.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent ae ){
                new JanelaConfirmarRemocao( ja, JanelaConfirmarRemocao.TODOS );
            }
        });
        jp3.add( jb );
        jp2.add( jp3, BorderLayout.SOUTH );
        jp.add( jp2, BorderLayout.SOUTH );
        add( jp, BorderLayout.EAST );
        
        //Configurações da janela
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable( false );
        setTitle( "Agenda" );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }
    
    private void atualizaCalendario(){
        int diaDaSemanaDia1 = calendario.get( Calendar.DAY_OF_WEEK );
        mes = calendario.get( Calendar.MONTH ) + 1;
        ano = calendario.get( Calendar.YEAR );
        hashLembretes = Lembrete.lembretesDoMes( diaDaSemanaDia1, mes, ano );
        
        //Atualização do título
        String[] mesesStr = {null, "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        tituloCalendario.setText( mesesStr[mes] + " de " + ano );
        
        //Remoção de todo o conteúdo
        for( int i = 0; i < 6; i++ )
            tabelaCalendario.setValueAt( "", 0, i );
        for( int i = 4; i < 6; i++ )
            for( int j = 0; j < 7; j++ )
                tabelaCalendario.setValueAt( "", i, j );
        
        //Preenchîmento com os dias do mês
        int incrementador = 0, totalDeDiasNoMes = calendario.getActualMaximum( Calendar.DAY_OF_MONTH );
        for( int i = diaDaSemanaDia1 - 1; i < 7; i++ )
            tabelaCalendario.setValueAt( String.valueOf( ++incrementador ), 0, i );
        for( int i = 1; incrementador < totalDeDiasNoMes; i++ )
            for( int j = 0; j < 7 && incrementador < totalDeDiasNoMes; j++ )
                tabelaCalendario.setValueAt( String.valueOf( ++incrementador ), i, j );
    }
    
    private void atualizaLista(){
        int numLembretes = Lembrete.size();
        DefaultTableModel dtm = (DefaultTableModel)tabelaLembretes.getModel();
        dtm.setRowCount( numLembretes );
        
        for( int i = 0; i < numLembretes; i++ ){
            tabelaLembretes.setValueAt( Lembrete.get(i).data(), i, 0 );
            tabelaLembretes.setValueAt( Lembrete.get(i).getAssunto(), i, 1 );
            tabelaLembretes.setValueAt( " " + Lembrete.get(i).getTitulo(), i, 2 );
        }
    }
    
    public void atualiza(){
        atualizaCalendario();
        atualizaLista();
    }
    
    public void removeLembretesAntigos(){
        Lembrete.removeAntigos( new Lembrete( diaAtual, mesAtual, anoAtual ) );
        atualiza();
    }
    
    public void removeTodosLembretes(){
        Lembrete.removeTodos();
        atualiza();
    }
}
