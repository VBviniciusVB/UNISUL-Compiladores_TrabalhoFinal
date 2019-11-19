package sintatico;

import sintatico.Constants;

public class Pilha {

    public Integer[] pilha;
    public int posicaoPilha;

    public Pilha() {
        this.posicaoPilha = -1;
        this.pilha = new Integer[100];
    }

    public boolean pilhaVazia() {
        return this.posicaoPilha == -1;
    }


    public Integer desempilhar() {
        if (pilhaVazia())
            return null;

        return this.pilha[this.posicaoPilha--];
    }
    
    public Integer exibeUltimoValor() {
        if (this.pilhaVazia())
            return null;

        return this.pilha[this.posicaoPilha];
    }

    public void empilhar(Integer valor) {
        if (this.posicaoPilha < this.pilha.length - 1) {
        	this.pilha[++posicaoPilha] = valor;
        }   
    }
    
    

    public void startSymbolStack(){

        empilhar(Constants.DOLLAR);
        empilhar(Constants.START_SYMBOL);
    }

}