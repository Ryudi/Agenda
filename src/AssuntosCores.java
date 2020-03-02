import java.awt.Color;
import java.util.ArrayList;

public class AssuntosCores {
    
    private ArrayList<String> assuntos;
    private ArrayList<Color> cores;
    
    public AssuntosCores(){
        assuntos = new ArrayList();
        cores = new ArrayList();
    }
    
    public String[] getAssuntos(){
        return assuntos.toArray( new String[assuntos.size()] );
    }
    
    public Color[] getCores(){
        return cores.toArray( new Color[cores.size()] );
    }
    
    public void add( String assunto, Color cor ){
        assuntos.add( assunto );
        cores.add( cor );
    }
    
    public int size(){
        return assuntos.size();
    }
}
