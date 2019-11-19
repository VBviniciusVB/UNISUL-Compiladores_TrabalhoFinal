package tabeladesimbolos;

public class Teste {

    public static void main(String[] args) {
        teste();
    }

    public static void teste() {
        Tabela tabela = new Tabela();

        Simbolo simbolo = new Simbolo();
        simbolo.setNome("Token");
        simbolo.setCategoria("INTEIRO");
        simbolo.setGeralA("1");
        simbolo.setGeralB("2");
        simbolo.setNivel("3");

    }
}