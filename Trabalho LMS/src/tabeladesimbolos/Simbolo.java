package tabeladesimbolos;

public class Simbolo {

    private String nome;
    private String categoria;
    private String nivel;
    private String geralA;
    private String geralB;

    private Simbolo proximo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public  String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
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

    public Simbolo getProximo() {
        return proximo;
    }

    public void setProximo(Simbolo proximo) {
        this.proximo = proximo;
    }
}
