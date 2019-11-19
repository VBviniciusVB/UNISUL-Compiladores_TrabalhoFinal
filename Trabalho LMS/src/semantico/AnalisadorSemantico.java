package semantico;

import java.util.ArrayList;

import application.ControllerMain;
import sintatico.Pilha;
import tabeladesimbolos.Simbolo;
import tabeladesimbolos.Tabela;

//Necessita quando abre pelo Intelij


public class AnalisadorSemantico {

    public static String ultimo = "Ultimo";
    public static String penultimo = "Penultimo";
    private static int acaoAcumulada = 3;
    private static AreaInstrucoes AreaInstrucoes;
    public static Hipotetica MaquinaHipotetica;
    public static AreaLiterais areaLiterais;
    private static int end_ident = 0;
    private static int np;
    private static String nomePro = "";
    private static String nomeIdentificador = "";
    private static String nome_atribuicao_esquerda = "";
    private static String nomeProcedimento = "";
    private static int npe = 0;

    static int nivel_atual;
    static int Pt_livre;
    static int[] escopo  = new int[1000];
    static int nv;
    static int deslocamento;
    static int Lc;
    static int Lit;
    
    static String tipo_identificador;

    //Inicializa Pilha
    ArrayList<Pilha> ifs = new ArrayList<Pilha>();
    ArrayList<Pilha> whiles = new ArrayList<Pilha>();
    ArrayList<Pilha> repeat = new ArrayList<Pilha>();
    public static ArrayList<Pilha> procedures = new ArrayList<Pilha>();
    ArrayList<Pilha> cases = new ArrayList<Pilha>();
    ArrayList<Pilha> fors = new ArrayList<Pilha>();
    
    //Inicializa Instruções da máquina hipotética
    public static ArrayList<String> instrucoes = new ArrayList<String>();
    public static AreaInstrucoes AI = new AreaInstrucoes();
    public static AreaLiterais AL = new AreaLiterais();
    
    public static void AcaoSemantica (int Numero){

        switch (Numero) {
            case 100:

            	//Inicia a pilha 
            	instrucoes.clear();
            	
            	//Inicializa Instruções e Literais na Hipotetica
            	MaquinaHipotetica.InicializaAI(AI);
            	MaquinaHipotetica.InicializaAL(AL);
            	
            	//Tabela de Símbolos
            	Tabela tabela = new Tabela();
            	
                nivel_atual = 0;
                Pt_livre = 1;
                escopo[0] = 1;
                nv = 0;
                deslocamento = 3;
                Lc = 1;
                Lit = 1;

                break;
            case 101:

                //Hipotetica.IncluirAI(null, 26, 0, 0);
//                this.maquinaHipotetica.incluir(this.areaInstrucoes, 26, 0, 0);
//                for (int i = 0; i < this.tabelaSimbolos.getTabela().length; i++) {
//                    if ((this.tabelaSimbolos.getTabela()[i][0] != null) && (this.tabelaSimbolos.getTabela()[i][1].equals("rótulo"))){
//                        if (this.tabelaSimbolos.getTabela()[i][4].equals(""))
//                            break;
//                        this.erro = "Erro semântico";
//                        msgRetornoSemantico = "Erro semântico";
//
//                        break;
//                    }
//
//                }


            	instrucoes.add("PARA");



                break;
            case 102:

            	instrucoes.add("AMEM");

                MaquinaHipotetica.IncluirAI(AI, 24, 0, acaoAcumulada);

                acaoAcumulada = 3;

                break;
            case 103:

                tipo_identificador = "rótulo";

                break;
            case 104:

                acaoAcumulada += 1;

                Simbolo simbolo1 = new Simbolo();
                simbolo1.setNome(penultimo+"");
                simbolo1.setCategoria("variável");
                simbolo1.setNivel(nivel_atual+"");
                simbolo1.setGeralA(deslocamento+"");
                simbolo1.setGeralB("");
                simbolo1.setProximo(null);

            	if (tipo_identificador.equals("rótulo")) {
                    if (Tabela.buscar(simbolo1) == null) {

                        int nivel = Integer.parseInt((Tabela.buscar(simbolo1)).getNivel()) ;
                        if (nivel == nivel_atual) {
                            System.out.println("Erro semântico : Rótulo já declarado no mesmo nível");
                        }
                        else{
                            simbolo1.setCategoria("rótulo");
                            simbolo1.setGeralA("0");

                            Tabela.adiciona(simbolo1);
                        }

                    } else {
                        simbolo1.setCategoria("rótulo");
                        simbolo1.setGeralA("0");

                        Tabela.adiciona(simbolo1);
                    }
            		
            	}


            	if (tipo_identificador.equals("variável")) {
                    if (Tabela.buscar(simbolo1) == null) {

                        Tabela.adiciona(simbolo1);
                        deslocamento += 1;
                        nv += 1;

                    } else {
                        System.out.println("Erro semântico\nVariável \""+penultimo+"\" já foi declarada");
                    }

            	}
            	
            	if (tipo_identificador.equals("parâmetro")) {

                    simbolo1.setCategoria("parâmetro");
                    simbolo1.setGeralA("");

                    if (Tabela.buscar(simbolo1) == null) {
                        int nivel = Integer.parseInt((Tabela.buscar(simbolo1)).getNivel()) ;
                        if (nivel == nivel_atual) {
                            System.out.println("Erro semântico\nParâmetro já declarado no mesmo nível");
                        } else {

                            Tabela.adiciona(simbolo1);

                            //this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));

                            np += 1;
                        }
                    } else {

                        Tabela.adiciona(simbolo1);

                        //this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));

                        np += 1;
                    }

            	}
            	
                break;
            case 105:

                Simbolo simbolo2 = new Simbolo();
                simbolo2.setNome(penultimo+"");
                simbolo2.setCategoria("constante");
                simbolo2.setNivel(nivel_atual+"");
                simbolo2.setGeralA("0");
                simbolo2.setGeralB("0");
                simbolo2.setProximo(null);

                if (Tabela.buscar (simbolo2) == null) {
                    System.out.println("Erro semântico = Constante já foi declarada");
                } else {

                    Tabela.adiciona(simbolo2);

                    end_ident = Integer.parseInt(Tabela.buscar(simbolo2).getNivel());
                }

                break;
            case 106:

                //String[][] tabelaSimbolo = this.tabelaSimbolos.getTabela();
                //tabelaSimbolo[this.end_ident][3] = this.penultimo;
                //this.tabelaSimbolos.setTabela(tabelaSimbolo);

                break;
            case 107:

            	tipo_identificador = "variável";
                nv = 0;

                break;
            case 108:

                deslocamento = 3;
                nomePro = penultimo;

                Simbolo simbolo3 = new Simbolo();
                simbolo3.setNome(penultimo+"");
                simbolo3.setCategoria("procedure");
                simbolo3.setNivel(nivel_atual+"");
                simbolo3.setGeralA(AreaInstrucoes.LC + 1 + "");
                simbolo3.setGeralB("0");
                simbolo3.setProximo(null);

                possuiParametro(false);
                //this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));
                nivel_atual += 1;
                np = 0;

                break;
            case 109:
                if (np > 0) {

                    Simbolo simbolo109 = new Simbolo();
                    simbolo109.setNome(nomePro+"");

                    Simbolo table = new Simbolo();
                    table = Tabela.buscar (simbolo109);

                    //String[][] tabelaSimbolo2 = this.tabelaSimbolos.getTabela();
                    //tabelaSimbolo2[this.tabelaSimbolos.buscar(this.nomePro)][4] = this.np+"";
                    //for (int i = 0; i < this.np; i++) {
                    //    tabelaSimbolo2[this.parametros.topo()][3] = (-(this.np - i)+"");

                    //    this.parametros.tiraElemento();
                    //}
                    //this.tabelaSimbolos.setTabela(tabelaSimbolo2);
                }

                //this.instrucoes.insereInstrucao(19, 0, 0);
                //this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);
                //this.procedures.insereElemento(this.areaInstrucoes.LC - 1);

                //this.parametros.insereElemento(this.np);

                break;
            case 110:
            	instrucoes.add("RETU");

                procedures.remove(procedures.size());

                //MaquinaHipotetica.IncluirAI(AreaInstrucoes, 1, 0, np+1);
                //instrucoes.insereInstrucao(1, 0, this.np + 1);

                Simbolo simbolo4 = new Simbolo();
                simbolo4.setNome("Busca");
                simbolo4.setCategoria("rótulo");

//                for (int i = 0; i < tabelaSimbolos.getTabela().procedures.size(); i++) {
//                    if ((this.tabelaSimbolos.getTabela()[i][0] != null) &&
//                            (this.tabelaSimbolos.getTabela()[i][1].equals("rótulo")))
//                    {
//                        if (this.tabelaSimbolos.getTabela()[i][4].equals("")) break;

//                        System.out.println ("Erro semântico");
//
//                        break;
//                    }
//
//                }

                break;
            case 111:
                tipo_identificador = "parâmetro";
                possuiParametro(true);

                break;
            case 112:
                nomeIdentificador = penultimo;
            	
            	break;
            case 113:

                nomeIdentificador = penultimo;

                Simbolo simbolo5 = new Simbolo();
                simbolo5.setNome(penultimo+"");


                if (Tabela.buscar(simbolo5) == null) {
//                    if (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeIdentificador)][1].equals("rótulo")) {
//                        if (!this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeIdentificador)][2].equals(this.nivelAtual)) {
//                            msgRetornoSemantico = "Erro semântico\nRótulo não está no escopo";
//                        } else {
//                            String[][] tabelaSimbolo3 = this.tabelaSimbolos.getTabela();
//
//                            tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][3] = this.areaInstrucoes.LC+"";
//
//                            if (!tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4].equals(""))
//                            {
//                                String lista = tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4];
//                                int qtd = 0;
//
//                                for (int i = 0; i < lista.length(); i++) {
//                                    if (lista.charAt(i) == ' ') {
//                                        qtd++;
//                                    }
//                                }
//
//                                int endereco = 0;
//
//                                lista = tiraProximo(lista);
//
//                                for (int i = 0; i < qtd; i++)
//                                {
//                                    endereco = Integer.parseInt(proximo(lista));
//                                    lista = tiraProximo(lista);
//
//                                    this.instrucoes.alteraInstrucao(endereco, 0, this.areaInstrucoes.LC + 1);
//                                    MaquinaHipotetica.alterar(this.areaInstrucoes, endereco, 0, this.areaInstrucoes.LC);
//                                }
//
//                            }
//
//                            tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4] = "";
//
//                            this.tabelaSimbolos.setTabela(tabelaSimbolo3);
//                        }
//                    }
//                    else
//                        this.erro = "Erro semântico: rótulo não está declarado";
                }
                else {
                    System.out.println("Erro semântico: rótulo não está declarado");
                }
            	
            	break;
            case 114:

                nomeIdentificador = penultimo;

                Simbolo simbolo6 = new Simbolo();
                simbolo6.setNome(penultimo+"");

                Simbolo Table = new Simbolo();
                Table = Tabela.buscar(simbolo6);

                if (Tabela.buscar(simbolo6) == null) {
                    if (Table.getNome().equals("variável")){
                        System.out.println("Erro semântico: atribuição da parte esquerda inválida");
                    } else {
                        nome_atribuicao_esquerda = nomeIdentificador;
                    }
                } else {
                    System.out.println("Erro semântico: identificador \""+penultimo+"\" não encontrado na tabela de símbolos");
                }


                break;
            case 115:
            	instrucoes.add("ARMZ");

                nomeIdentificador = penultimo;

                Simbolo Table115 = new Simbolo();
                Table115.setNome(nome_atribuicao_esquerda+"");
                Table115 = Tabela.buscar(Table115);

                //int d_nivel = nivel_atual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][2]);
                //this.instrucoes.insereInstrucao(4, d_nivel, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][3]));
                //this.maquinaHipotetica.incluir(this.areaInstrucoes, 4, d_nivel, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][3]));

            	break;
            case 116:

                Simbolo Table116 = new Simbolo();
                Table116.setNome(penultimo+"");
                Table116 = Tabela.buscar(Table116);

                if (Table116 == null){
                    System.out.println("Erro Semântico: procedure "+penultimo+" não fou declarado");
                }else{
                    if(Table116.getCategoria().equals("procedure")){
                        nomeProcedimento = penultimo;
                    }
                }
            	
            	break;
            case 117:
            	instrucoes.add("CALL");

                nomeProcedimento = nomePro;

                Simbolo Table117 = new Simbolo();
                Table117.setNome(nomeProcedimento+"");
                Table117 = Tabela.buscar(Table117);

                if(!Table117.getNivel().equals((np+""))){
                    System.out.println("Erro semântico: numero de parametros da procedure " + nomeProcedimento + " Não conferem com o número parâmetros passados");
                }else{
                    //this.instrucoes.insereInstrucao(25, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeProcedimento)][3]) + 1);
                    //this.maquinaHipotetica.incluir(this.areaInstrucoes, 25, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeProcedimento)][3]));
                }




            	
            	break;
            case 118:

                npe += 1;
            	
            	break;
            case 119:
            	instrucoes.add("DSVS");

                Simbolo Table119 = new Simbolo();
                Table119.setNome(penultimo+"");
                Table119 = Tabela.buscar(Table119);

                if(Table119 == null){
                    System.out.println("Erro semântico: identificador \""+penultimo+"\" não está declarado");
                }else {
                    if (Table119.getNivel().equals(nivel_atual)) {
                        System.out.println("Erro semântico: o rótulo não está declarado no escopo do nível");
                    } else {

                        // Não sei se é o GeralB por isso deixei isso aki ainda
                        //int op2 = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]);

                        int op2 = Integer.parseInt(Table119.getGeralB());



                        if (op2 != 0) {
                            //this.instrucoes.insereInstrucao(19, 0, 0);
                            //this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, op2);
                        } else {
                            //this.instrucoes.insereInstrucao(19, 0, 0);
                            //this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);

                            //String[][] tabelaSimbolo4 = this.tabelaSimbolos.getTabela();
                            //int ind = this.tabelaSimbolos.buscar(this.penultimo);

                            //tabelaSimbolo4[ind][4] = (tabelaSimbolo4[ind][4] + " " + (this.areaInstrucoes.LC - 1));

                            //this.tabelaSimbolos.setTabela(tabelaSimbolo4);
                        }
                    }
                }

            	
            	break;
            case 120:
            	instrucoes.add("DSVF");
            	
            	break;
            case 121:
            	
            	
            	break;
            case 122:
            	
            	
            	break;
            case 123:
            	
            	
            	break;
            case 124:
            	
            	
            	break;
            case 125:
            	
            	
            	break;
            case 126:
            	
            	
            	break;
            case 127:
            	
            	
            	break;
            case 128:
            	instrucoes.add("READLN");
            	
            	break;
            case 129:
//            	instrucoes.add("LEIT");
//            	instrucoes.add("ARMZ");
            	
            	break;
            case 130:
            	instrucoes.add("WRITELN");
            	
            	break;
            case 131:
            	instrucoes.add("IMPR");
            	
            	break;
            case 132:
            	
            	
            	break;
            case 133:
            	
            	
            	break;
            case 134:
//            	instrucoes.add("COPIA");
//            	instrucoes.add("CRCT");
//            	instrucoes.add("CMIG");
            	
            	break;
            case 135:
            	
            	
            	break;
            case 136:
//            	instrucoes.add("COPIA");
//            	instrucoes.add("CRCT");
//            	instrucoes.add("CMIG");
//            	instrucoes.add("DSVT");
            	
            	break;
            case 137:
            	
            	
            	break;
            case 138:
            	
            	
            	break;
            case 139:
            	
            	
            	break;
            case 140:
            	
            	
            	break;
            case 141:
            	
            	
            	break;
            case 142:
            	
            	
            	break;
            case 143:
            	
            	
            	break;
            case 144:
            	
            	
            	break;
            case 145:
            	
            	
            	break;
            case 146:
            	
            	
            	break;
            case 147:
            	
            	
            	break;
            case 148:
            	
            	
            	break;
            case 149:
            	
            	
            	break;
            case 150:
            	
            	
            	break;
            case 151:
            	
            	
            	break;
            case 152:
            	
            	
            	break;
            case 153:
            	
            	
            	break;
            case 154:
            	
            	
            	break;
            case 155:
            	
            	
            	break;
            case 156:
            	
            	
            	break;
        }


        }

    public static void possuiParametro(boolean temParametro) {
        temParametro = temParametro;

    }



}