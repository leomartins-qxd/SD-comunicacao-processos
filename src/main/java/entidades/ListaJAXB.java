package entidades;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="Lista")
public class ListaJAXB<T>{
    protected List<T> lista;

    public ListaJAXB(){}

    public ListaJAXB(List<T> lista){
        this.lista=lista;
    }

    @XmlElement(name="Item")
    public List<T> getList(){
        return lista;
    }
}