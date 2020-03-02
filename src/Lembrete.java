import java.io.*;
import java.util.*;

public class Lembrete implements Comparable<Lembrete>, Serializable{
    
    private final static String PATH_ARQUIVO = "Agenda_lembretes";
    private static ArrayList<Lembrete> conjunto;
    
    public static Lembrete get( int posicao ){
        return conjunto.get( posicao );
    }
    
    public static int size(){
        return conjunto.size();
    }
    
    //Registra os dados de todos os lembretes no arquivo indicado em PATH_ARQUIVO
    public static boolean salva(){
        try{
            FileOutputStream fos = new FileOutputStream( PATH_ARQUIVO );
            ObjectOutputStream oos = new ObjectOutputStream( fos );
            oos.writeObject( conjunto );
            oos.close();
            return true;
        }catch( IOException ioe ){
            new JanelaMensagem( "Erro ao salvar arquivo de lembretes." );
            return false;
        }
    }
    
    //Resgata os dados de todos os lembretes do arquivo indicado em PATH_ARQUIVO
    public static void carrega(){
        try{
            FileInputStream fis = new FileInputStream( PATH_ARQUIVO );
            ObjectInputStream ois = new ObjectInputStream( fis );
            conjunto = ( ArrayList<Lembrete> )ois.readObject();
            ois.close();
        }catch( ClassNotFoundException cnfe ){
            new JanelaMensagem( "Erro ao carregar arquivo de lembretes." );
        }catch( IOException ioe ){
            conjunto = new ArrayList<>();
            new JanelaMensagem( "Arquivo de lembretes não encontrado. Um novo arquivo \"" + PATH_ARQUIVO + "\" será criado ao salvar." );
        }
    }
    
    //Insere um novo lembrete no conjunto, mantendo-o ordenado por data. A operação é negada caso já exista outro lembrete para a mesma data
    public static boolean insere( Lembrete l ){
        int posicao = Collections.binarySearch( conjunto, l );
        if( posicao >= 0 ){
            new JanelaMensagem( "Erro: Já existe lembrete para a data em questão" );
            return false;
        }
        posicao = - posicao - 1;
        conjunto.add( posicao, l );
        return salva();
    }
    
    //Remove o lembrete indicado nos parâmetros, já assumindo que tal lembrete exista no conjunto
    public static void remove( Lembrete l ){
        conjunto.remove( l );
        salva();
    }
    
    //Remove do conjunto os lembretes correspondentes à datas passadas
    public static void removeAntigos( Lembrete hoje ){
        while( conjunto.size() > 0 && conjunto.get(0).compareTo( hoje ) < 0 )
            conjunto.remove(0);
        salva();
    }
    
    //Limpa o conjunto de lembretes
    public static void removeTodos(){
        conjunto = new ArrayList<>();
        salva();
    }
    
    //Retorna a matriz hash correspondente ao calendário de um mês contendo a posição dos dias que contenham lembretes correspondentes
    public static int[][] lembretesDoMes( int diaDaSemanaDia1, int mes, int ano ){
        int linha, coluna;
        int[][] hashLembretes = new int[6][7];
        for( int i = 0; i < 6; i++ )
            for( int j = 0; j < 7; j++ )
                hashLembretes[i][j] = -1;
        for( int i = 0; i < conjunto.size(); i++ ){
            Lembrete l = conjunto.get(i);
            if( l.getMes() == mes && l.getAno() == ano ){
                linha = ( diaDaSemanaDia1 + l.getDia() - 2 ) / 7;
                coluna = ( diaDaSemanaDia1 + l.getDia() - 2 ) % 7;
                hashLembretes[linha][coluna] = i;
            }
        }        
        return hashLembretes;
    }
    
    private int dia;
    private int mes;
    private int ano;
    private String assunto;
    private String titulo;
    private String detalhes;
    
    public Lembrete( int dia, int mes, int ano ){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.assunto = null;
        this.titulo = null;
        this.detalhes = null;
    }
    
    public Lembrete( int dia, int mes, int ano, String assunto, String titulo, String detalhes ){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.assunto = assunto;
        this.titulo = titulo;
        this.detalhes = detalhes;
    }
    
    //Getters
    public int getDia(){ return dia; }
    public int getMes(){ return mes; }
    public int getAno(){ return ano; }
    public String getAssunto(){ return assunto; }
    public String getTitulo(){ return titulo; }
    public String getDetalhes(){ return detalhes; }
    
    //Setters
    public void setAssunto( String assunto ){ this.assunto = assunto; }
    public void setTitulo( String titulo ){ this.titulo = titulo; }
    public void setDetalhes( String detalhes ){ this.detalhes = detalhes; }
    
    public String data(){
        return dia + "/" + mes + "/" + ano;
    }
    
    //Estabelecimento dos parâmetros para manter a lista de lembretes ordenada por data
    public int compareTo( Lembrete l ){
        if( this.ano < l.getAno() )
            return -1;
        else if( this.ano > l.getAno() )
            return 1;
        else{
            if( this.mes < l.getMes() )
                return -1;
            else if( this.mes > l.getMes() )
                return 1;
            else{
                if( this.dia < l.getDia() )
                    return -1;
                else if( this.dia > l.getDia() )
                    return 1;
                else
                    return 0;
            }
        }
    }
}
