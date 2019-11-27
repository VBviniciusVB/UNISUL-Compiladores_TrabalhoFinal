package tabeladesimbolos;

public class Tabela {

    private static final int tableSize = 25147; //first prime after 25143
    static int index;
    int collisions;
    public static Simbolo[] hashtable = new Simbolo[tableSize];

    public static int hash(String key, int tableSize) {

        int hashVal = 0; //uses Horner’s method to evaluate a polynomial
        for (int i = 0; i < key.length(); i++)
            hashVal = 37 * hashVal + key.charAt(i);
        hashVal %= tableSize;
        if (hashVal < 0)
            hashVal += tableSize; //needed if hashVal is negative
        return hashVal;
    }

    public static int hastableLEngth(){
        return hashtable.length;
    }

    public static void mostraConteudoTabela() {
        for (Simbolo simbolo : hashtable) {
            if (simbolo != null) {

                System.out.println(simbolo.hashCode() + " - " + simbolo.getNome() + " - " + "Próximo:  " + ( simbolo.getProximo() != null ? simbolo.getProximo().getNome() : null)  + " - " + simbolo.getCategoria() + " - " + simbolo.getGeralA() + " - " + simbolo.getGeralB() + " - " + simbolo.getNivel());

                Simbolo proximo = simbolo.getProximo();
                while (proximo != null) {
                    System.out.println(simbolo.getNome() + " - " + "Próximo: " + simbolo.getProximo().getNome() + " - " + simbolo.getCategoria() + " - " + simbolo.getGeralA() + " - " + simbolo.getGeralB() + " - " + simbolo.getNivel());
                    proximo = proximo.getProximo();
                }
            }
        }
    }

    public static void adiciona(Simbolo simbolo) {
        index = hash(simbolo.getNome(), tableSize);
        if (isNullIndex(index))
            hashtable[index] = simbolo;
        else {
            Simbolo auxSimbolo = hashtable[index];
            while (hasProximo(auxSimbolo)) {
                auxSimbolo = auxSimbolo.getProximo();
            }
            auxSimbolo.setProximo(simbolo);
        }
    }

    public static Simbolo[] clearHashtable() {
        Simbolo[] emptyHashytable = new Simbolo[tableSize];
        hashtable = emptyHashytable;
        return hashtable;
    }

    public boolean remover(Simbolo simbolo) {
        int index = this.hash(simbolo.getNome(), this.tableSize);

        Simbolo atual = hashtable[index];
        Simbolo proximo = atual.getProximo();

        if (proximo == null && atual.getNome().equals(simbolo.getNome())) {
            hashtable[index] = null;
            return true;

        } else if (proximo == null && !atual.getNome().equals(simbolo.getNome())) {
            return false;

        } else {
            do {

                if (proximo != null && proximo.getNome().equals(simbolo.getNome())) {
                    atual.setProximo(proximo.getProximo());
                    return true;
                }
            } while (hasProximo(proximo));

            return false;
        }
    }

    public static Simbolo buscar(Simbolo simbolo) {
        int index = hash(simbolo.getNome(), tableSize);

        Simbolo busca = hashtable[index];

        if (busca == null) {

            return null;
        }

        if (busca.getNome().equals(simbolo.getNome())) {
            return busca;
        } else {
            do {
                busca = busca.getProximo();
            } while (hasProximo(busca));

            return busca;
        }
    }

    public static void atualizar(Simbolo simbolo, Simbolo novoSimbolo) {
        int index = hash(simbolo.getNome(), tableSize);
        if (hashtable[index] != null) {
            Simbolo atual = hashtable[index];
            if (atual == simbolo && !hasProximo(atual)) {
                hashtable[index] = novoSimbolo;
            } else if (atual == simbolo) {
                novoSimbolo.setProximo(atual.getProximo());
                hashtable[index] = novoSimbolo;
            } else {
                Simbolo anterior = atual;
                atual = atual.getProximo();
                while (atual != simbolo) {
                    anterior = atual;
                    atual = atual.getProximo();
                }
                if (atual != null) {
                    anterior.setProximo(novoSimbolo);
                    novoSimbolo.setProximo(atual.getProximo());
                }
            }
        }
    }


    public static boolean isNullIndex(int index) {
        return hashtable[index] == null;
    }

    public static boolean hasProximo(Simbolo simbolo) {
        return simbolo.getProximo() != null;
    }
}
