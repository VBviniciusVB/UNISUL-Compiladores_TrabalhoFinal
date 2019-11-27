package semantico;

public class TabelaIntermediaria {

    private String codigo;
    private String geralA;
    private String geralB;

    public TabelaIntermediaria (String codigo, String geralA, String geralB) {
        this.codigo = codigo;
        this.geralA = geralA;
        this.geralB = geralB;
        }

    public TabelaIntermediaria() {

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getGeralA() {
        return geralA;
    }

    public void setGeralA(String geralA) {
        this.geralA = geralA;
    }

    public String getGeralB() {
        return geralB;
    }

    public void setGeralB(String geralB) {
        this.geralB = geralB;
    }

}