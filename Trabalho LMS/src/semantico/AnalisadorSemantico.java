package semantico;

import java.util.ArrayList;

import tabeladesimbolos.Simbolo;
import tabeladesimbolos.Tabela;

public class AnalisadorSemantico {

    public static String ultimo = "Ultimo";
    public static String penultimo = "Penultimo";
    private static String antepenultimo;
    private static int acaoAcumulada = 3;
    private static AreaInstrucoes Instrucao;
    public static Hipotetica MaquinaHipotetica;
    public static AreaLiterais Literal;
    private static int end_ident = 0;
    private static int np;
    private static String nomePro = "";
    private static String nomeIdentificador = "";
    private static String nome_atribuicao_esquerda = "";
    private static String nomeProcedimento = "";
    private static int npe = 0;
    private static String contexto;
    private static int forEndNome = 0;

    static int nivel_atual;
    static int Pt_livre;
    static int[] escopo  = new int[1000];
    static int nv;
    static int deslocamento;
    static int Lc;
    static int Lit;
    
    static String tipo_identificador;

    //Inicializa Pilha ???
    public static ArrayList<Integer> ifs = new ArrayList<Integer>();
    public static ArrayList<Integer> whiles = new ArrayList<Integer>();
    public static ArrayList<Integer> repeats = new ArrayList<Integer>();
    public static ArrayList<Integer> procedures = new ArrayList<Integer>();
    public static ArrayList<Integer> parametros = new ArrayList<Integer>();
    public static ArrayList<Integer> cases = new ArrayList<Integer>();
    public static ArrayList<Integer> fors = new ArrayList<Integer>();

    //Gravar nomes da tabela de símbolo
    public static ArrayList<String> SalvaParaMostrarTabelaSemantica = new ArrayList<String>();
    
    //Inicializa Instruções e Literais da máquina hipotética
    public static AreaInstrucoes AI = new AreaInstrucoes();
    public static AreaLiterais AL = new AreaLiterais();
    
    public static void AcaoSemantica (int Numero){

        switch (Numero) {
            case 100:

            	//Inicializa Instruções e Literais na Hipotetica
                MaquinaHipotetica = new Hipotetica();
                AI = new AreaInstrucoes();
                AL = new AreaLiterais();
            	MaquinaHipotetica.InicializaAI(AI);
            	MaquinaHipotetica.InicializaAL(AL);

            	//Inicializa Pilha
                ifs = new ArrayList<Integer>();
                whiles = new ArrayList<Integer>();
                repeats = new ArrayList<Integer>();
                procedures = new ArrayList<Integer>();
                parametros = new ArrayList<Integer>();
                cases = new ArrayList<Integer>();
                fors = new ArrayList<Integer>();


                // Inicializa valores padrões
                nivel_atual = 0;
                Pt_livre = 1;
                escopo[0] = 1;
                nv = 0;
                deslocamento = 3;
                Lc = 1;
                Lit = 1;

                break;
            case 101:

                MaquinaHipotetica.IncluirAI(AI, 26, 0, 0);

//                for (int i = 0; i < this.tabelaSimbolos.getTabela().length; i++) {
//                    if ((this.tabelaSimbolos.getTabela()[i][0] != null) && (this.tabelaSimbolos.getTabela()[i][1].equals("rótulo"))){
//                        if (this.tabelaSimbolos.getTabela()[i][4].equals(""))
//                            break;
//
//                        System.out.println("Erro semântico");
//
//                        break;
//                    }
//
//                }

                break;
            case 102:


                MaquinaHipotetica.IncluirAI(AI, 24, 0, acaoAcumulada);

                acaoAcumulada = 3;

                break;
            case 103:

                tipo_identificador = "rótulo";

                break;
            case 104:

                acaoAcumulada += 1;

                Simbolo Table104 = new Simbolo();
                Table104.setNome(penultimo+"");
                Table104.setCategoria("variável");
                Table104.setNivel(nivel_atual+"");
                Table104.setGeralA(deslocamento+"");
                Table104.setGeralB("");
                Table104.setProximo(null);

            	if (tipo_identificador.equals("rótulo")) {
                    if (Tabela.buscar(Table104) == null) {

                        int nivel = Integer.parseInt((Tabela.buscar(Table104)).getNivel()) ;
                        if (nivel == nivel_atual) {
                            System.out.println("Erro semântico : Rótulo já declarado no mesmo nível");
                        }
                        else{
                            Table104.setCategoria("rótulo");
                            Table104.setGeralA("0");

                            Tabela.adiciona(Table104);
                        }

                    } else {
                        Table104.setCategoria("rótulo");
                        Table104.setGeralA("0");

                        Tabela.adiciona(Table104);
                    }

            	}


            	if (tipo_identificador.equals("variável")) {
                    if (Tabela.buscar(Table104) == null) {

                        Tabela.adiciona(Table104);
                        deslocamento += 1;
                        nv += 1;

                    } else {
                        System.out.println("Erro semântico\nVariável \""+penultimo+"\" já foi declarada");
                    }

            	}

            	if (tipo_identificador.equals("parâmetro")) {

                    Table104.setCategoria("parâmetro");
                    Table104.setGeralA("");

                    if (Tabela.buscar(Table104) == null) {
                        int nivel = Integer.parseInt((Tabela.buscar(Table104)).getNivel()) ;
                        if (nivel == nivel_atual) {
                            System.out.println("Erro semântico\nParâmetro já declarado no mesmo nível");
                        } else {

                            Tabela.adiciona(Table104);

                            np += 1;
                        }
                    } else {

                        Tabela.adiciona(Table104);

                        np += 1;
                    }

            	}

                SalvaParaMostrarTabelaSemantica.add(Table104.getNome());

                break;
            case 105:

                Simbolo Table105 = new Simbolo();
                Table105.setNome(penultimo+"");
                Table105.setCategoria("constante");
                Table105.setNivel(nivel_atual+"");
                Table105.setGeralA("0");
                Table105.setGeralB("0");
                Table105.setProximo(null);

                if (Tabela.buscar (Table105) == null) {
                    System.out.println("Erro semântico = Constante já foi declarada");
                } else {

                    Tabela.adiciona(Table105);

                    end_ident = Integer.parseInt(Tabela.buscar(Table105).getNivel());
                }

                break;
            case 106:

                Simbolo Table106 = new Simbolo();
                Table106.setNome(end_ident+"");
                Table106 = Tabela.buscar (Table106);

                Simbolo Table106_2 = new Simbolo();
                Table106_2 = Table106;

                Table106_2.setGeralA(penultimo);

                Tabela.atualizar(Table106,Table106_2);

                break;
            case 107:

            	tipo_identificador = "variável";
                nv = 0;

                break;
            case 108:

                deslocamento = 3;
                nomePro = penultimo;

                Simbolo Table108 = new Simbolo();
                Table108.setNome(penultimo+"");
                Table108.setCategoria("procedure");
                Table108.setNivel(nivel_atual+"");
                // Instrução LC esta inicializando vazia, verificar o que pode ser
                Table108.setGeralA((Instrucao.LC + 1) + "");
                Table108.setGeralA("0");
                Table108.setGeralB("0");
                Table108.setProximo(null);

                possuiParametro(false);

                //o que é adicionado como parâmetro aki ???
                //this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));

                nivel_atual += 1;
                np = 0;

                break;
            case 109:
                if (np > 0) {

                    Simbolo Table109 = new Simbolo();
                    Table109.setNome(nomePro+"");
                    Table109 = Tabela.buscar (Table109);

                    Simbolo Table109_2 = new Simbolo();
                    Table109_2 = Table109;

                    Table109_2.setGeralB(np+"");


                    for (int i = 0; i < np; i++) {
                    //    tabelaSimbolo2[this.parametros.topo()][3] = (-(this.np - i)+"");
                          //Table109_2.setGeralA(-(np - i)+"");

                          parametros.remove(parametros.size() - 1);
                    }

                    Tabela.atualizar(Table109,Table109_2);

                }

                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);

                procedures.add(Instrucao.LC - 1);

                parametros.add(np);


                break;
            case 110:

                procedures.remove(procedures.size()-1);

                MaquinaHipotetica.IncluirAI(AI, 1, 0, np+1);


                Simbolo Table110 = new Simbolo();
                Table110.setNome("Busca");
                Table110.setCategoria("rótulo");

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

                Simbolo Table113 = new Simbolo();
                Table113.setNome(penultimo+"");
                Table113 = Tabela.buscar(Table113);

                if (Table113 != null) {
                    if (Table113.getCategoria().equals("rótulo")) {
                        if (!Table113.getNivel().equals(nivel_atual)) {
                            System.out.println("Erro semântico: rótulo não está no escopo");
                        } else {

                            Simbolo Table113_2 = new Simbolo();
                            Table113_2.setNome(nomeIdentificador+"");
                            Table113_2 = Tabela.buscar(Table113_2);
                            Table113_2.setGeralA(Instrucao.LC+"");

                            if (!Table113_2.getGeralA().equals("")){
                                String lista = Table113_2.getGeralB();
                                int qtd = 0;

                                for (int i = 0; i < lista.length(); i++) {
                                    if (lista.charAt(i) == ' ') {
                                        qtd++;
                                    }
                                }

                                int endereco = 0;

                                lista = tiraProximo(lista);

                                for (int i = 0; i < qtd; i++)
                                {
                                    endereco = Integer.parseInt(proximo(lista));
                                    lista = tiraProximo(lista);

                                    MaquinaHipotetica.AlterarAI(AI, endereco, 0, Instrucao.LC);

                                }
                            }


                            Table113_2.setGeralA("");

                            Tabela.atualizar(Table113,Table113_2);

                        }
                    }
                    else
                        System.out.println("Erro semântico: rótulo não está declarado");
                }
                else {
                    System.out.println("Erro semântico: rótulo não está declarado");
                }

            	break;
            case 114:

                nomeIdentificador = penultimo;

                System.out.println(penultimo);

                Simbolo Table114 = new Simbolo();
                Table114.setNome(penultimo+"");
                Table114 = Tabela.buscar(Table114);


                if (Table114 != null) {
                    if (Table114.getCategoria().equals("variável")){
                        System.out.println("Erro semântico: atribuição da parte esquerda inválida");
                    } else {
                        nome_atribuicao_esquerda = nomeIdentificador;
                    }
                } else {
                    System.out.println("Erro semântico: identificador \""+penultimo+"\" não encontrado na tabela de símbolos");
                }


                break;
            case 115:

                nomeIdentificador = penultimo;

                Simbolo Table115 = new Simbolo();
                Table115.setNome(nome_atribuicao_esquerda+"");
                Table115 = Tabela.buscar(Table115);

                int d_nivel = nivel_atual - Integer.parseInt(Table115.getNivel());

                MaquinaHipotetica.IncluirAI(AI, 4, d_nivel, Integer.parseInt(Table115.getGeralA()));

            	break;
            case 116:

                Simbolo Table116 = new Simbolo();
                Table116.setNome(penultimo+"");
                Table116 = Tabela.buscar(Table116);

                if (Table116 == null){
                    System.out.println("Erro Semântico: procedure "+penultimo+" não foi declarado");
                }else{
                    if(Table116.getCategoria().equals("procedure")){
                        nomeProcedimento = penultimo;
                    }
                }

            	break;
            case 117:

                nomeProcedimento = nomePro;

                Simbolo Table117 = new Simbolo();
                Table117.setNome(nomeProcedimento+"");
                Table117 = Tabela.buscar(Table117);

                if (Table117 == null){

                    System.out.println("Erro semântico: Símbolo "+nomeProcedimento+" não declarado !!!");

                }else{
                    if(!Table117.getNivel().equals((np+""))){
                        System.out.println("Erro semântico: numero de parametros da procedure " + nomeProcedimento + " Não conferem com o número parâmetros passados");
                    }else{

                        MaquinaHipotetica.IncluirAI(AI, 25, 0, Integer.parseInt(Table117.getGeralA()));

                    }
                }

            	break;
            case 118:

                npe += 1;

            	break;
            case 119:

                Simbolo Table119 = new Simbolo();
                Table119.setNome(penultimo+"");
                Table119 = Tabela.buscar(Table119);

                if(Table119 == null){
                    System.out.println("Erro semântico: identificador \""+penultimo+"\" não está declarado");
                }else {
                    if (Table119.getNivel().equals(nivel_atual)) {
                        System.out.println("Erro semântico: o rótulo não está declarado no escopo do nível");
                    } else {

                        int op2 = Integer.parseInt(Table119.getGeralB());



                        if (op2 != 0) {

                            MaquinaHipotetica.IncluirAI(AI, 19, 0, op2);

                        } else {

                            MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);

                            //String[][] tabelaSimbolo4 = this.tabelaSimbolos.getTabela();
                            //int ind = this.tabelaSimbolos.buscar(this.penultimo);

                            //tabelaSimbolo4[ind][4] = (tabelaSimbolo4[ind][4] + " " + (this.areaInstrucoes.LC - 1));

                            //this.tabelaSimbolos.setTabela(tabelaSimbolo4);
                        }
                    }
                }


            	break;
            case 120:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);

                ifs.add(Instrucao.LC - 1);

            	break;
            case 121:

                MaquinaHipotetica.AlterarAI(AI, ifs.get(ifs.size() - 1), 0, Instrucao.LC);

                ifs.remove(ifs.size() - 1);

            	break;
            case 122:

                MaquinaHipotetica.AlterarAI(AI, ifs.get(ifs.size() - 1), 0, Instrucao.LC + 1);

                ifs.remove(ifs.size() - 1);

                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);

                ifs.add(Instrucao.LC - 1);

            	break;
            case 123:

                whiles.add(Instrucao.LC);

            	break;
            case 124:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);

                whiles.add(Instrucao.LC - 1);

            	break;
            case 125:

                MaquinaHipotetica.AlterarAI(AI, whiles.get(whiles.size() - 1), 0, Instrucao.LC + 1);

                whiles.remove(whiles.size());

                MaquinaHipotetica.IncluirAI(AI, 19, 0, whiles.size() - 1);

                whiles.remove(whiles.size());

            	break;
            case 126:

                repeats.add(Instrucao.LC);

            	break;
            case 127:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, repeats.size());

                repeats.remove(repeats.size() - 1);

            	break;
            case 128:

                contexto = "readln";

            	break;
            case 129:

                Simbolo Table129 = new Simbolo();
                Table129.setNome(penultimo+"");
                Table129 = Tabela.buscar(Table129);

                if (Table129 == null)
                    {

                        System.out.println("Erro semântico: identificador \""+penultimo+"\" não está declarado");
                    }else{

                        int d_nivel2 = nivel_atual - Integer.parseInt(Table129.getNivel());

                    if (contexto.equals("readln")) {

                        if (Table129.getCategoria().equals("variável")) {

                            MaquinaHipotetica.IncluirAI(AI, 21, 0, 0);

                            MaquinaHipotetica.IncluirAI(AI, 4, d_nivel2, Integer.parseInt(Table129.getGeralA()));

                            }
                        else {
                            System.out.println("Erro semântico: identificador \""+penultimo+"\" não é uma variável");
                        }
                    }

                    if (contexto.equals("expressão"))
                    {

                        if (Table129.getCategoria().equals("procedure") || (Table129.getCategoria().equals("rótulo")))
                        {
                            System.out.println("Erro semântico: identificador \""+penultimo+"\" não é uma constante");
                        }
                        else if (Table129.getCategoria().equals("constante")) {

                            MaquinaHipotetica.IncluirAI(AI, 3, 0, Integer.parseInt(Table129.getGeralA()));

                            } else {

                            MaquinaHipotetica.IncluirAI(AI, 2, d_nivel2, Integer.parseInt(Table129.getGeralA()));

                        }

                    }

                }

            	break;
            case 130:

                Simbolo Table130 = new Simbolo();
                Table130.setNome(penultimo+"");
                Table130 = Tabela.buscar(Table130);

                MaquinaHipotetica.IncluirAL(AL, penultimo);

                //MaquinaHipotetica.IncluirAI(AI, 23, ???));
                //this.instrucoes.insereInstrucao(23, 0, this.areaLiterais.literais - 1);
                //this.maquinaHipotetica.incluir(this.areaInstrucoes, 23, 0, this.areaLiterais.literais - 1);

            	break;
            case 131:

                MaquinaHipotetica.IncluirAI(AI, 22, 0,0);

            	break;
            case 132:

                //Acopla mecanismo de controle de inicio de CASE junto à pilha de controle de CASE
            	
            	break;
            case 133:

                MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size() - 1), 0,Instrucao.LC);

                cases.remove(cases.size() - 1);

                MaquinaHipotetica.IncluirAI(AI, 24, 0,-1);
            	
            	break;
            case 134:

                MaquinaHipotetica.IncluirAI(AI, 28, 0,0);

                int ant = Integer.parseInt(antepenultimo);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, ant);

                MaquinaHipotetica.IncluirAI(AI, 15, 0,0);


                //if (!cases.vazia()) {

                MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size() - 1), 0,Instrucao.LC + 1);

                cases.remove(cases.size() - 1);

                //}
            	
            	break;
            case 135:

                MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size()-1), 0, Instrucao.LC + 1);

                cases.remove(cases.size()-1);

                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);

                cases.add(Instrucao.LC - 1);

            	break;
            case 136:

                if (cases.size() > 0) {

                    MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size()-1), 0, Instrucao.LC + 1);

                    cases.remove(cases.size()-1);

                }

                MaquinaHipotetica.IncluirAI(AI, 28, 0, 0);

                int ant2 = Integer.parseInt(antepenultimo);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, ant2);

                MaquinaHipotetica.IncluirAI(AI, 15, 0, 0);

                MaquinaHipotetica.IncluirAI(AI, 29, 0, 0);

                cases.add(Instrucao.LC - 1);

            	break;
            case 137:

                Simbolo Table137 = new Simbolo();
                Table137.setNome(penultimo+"");
                Table137 = Tabela.buscar(Table137);

                if ((Table137 != null)  && (Table137.getCategoria().equals("variável"))) {

                    forEndNome =  Table137.hashCode();
                }
                else {
                    System.out.println("Erro semântico: variável \""+penultimo+"\" não declarada");
                }

            	break;
            case 138:

                Simbolo Table138 = new Simbolo();
                Table138.setNome(forEndNome+"");
                Table138 = Tabela.buscar(Table138);

                int op1 = nivel_atual - Integer.parseInt(Table138.getNivel());
                int op2 = Integer.parseInt(Table138.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 4, op1, op2);
            	
            	break;
            case 139:

                fors.add(Instrucao.LC);

                MaquinaHipotetica.IncluirAI(AI, 28, 0, 0);

                Simbolo Table139 = new Simbolo();
                Table139.setNome(forEndNome+"");
                Table139 = Tabela.buscar(Table139);

                int op12 = nivel_atual - Integer.parseInt(Table139.getNivel());
                int op22 = Integer.parseInt(Table139.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 2, op12, op22);

                MaquinaHipotetica.IncluirAI(AI, 18, 0, 0);

                fors.add(Instrucao.LC);

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);
            	
            	break;
            case 140:

                Simbolo Table140 = new Simbolo();
                Table140.setNome(forEndNome+"");
                Table140 = Tabela.buscar(Table140);

                int op13 = nivel_atual - Integer.parseInt(Table140.getNivel());
                int op23 = Integer.parseInt(Table140.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 2, op13, op23);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, 1);

                MaquinaHipotetica.IncluirAI(AI, 5, 0, 1);

                MaquinaHipotetica.IncluirAI(AI, 4, op13, op23);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, 1);

                MaquinaHipotetica.AlterarAI(AI, fors.get(fors.size()-1), 0, Instrucao.LC + 1);

                MaquinaHipotetica.AlterarAI(AI, fors.get(fors.size()-1), 0, Instrucao.LC + 1);

                fors.remove(fors.size()-1);

                MaquinaHipotetica.IncluirAI(AI, 19, 0, fors.get(fors.size()-1));

                MaquinaHipotetica.IncluirAI(AI, 24, 0, -1);
            	
            	break;
            case 141:

                MaquinaHipotetica.IncluirAI(AI, 15, 0, 0);
            	
            	break;
            case 142:

                MaquinaHipotetica.IncluirAI(AI, 13, 0, 0);
            	
            	break;
            case 143:

                MaquinaHipotetica.IncluirAI(AI, 14, 0, 0);
            	
            	break;
            case 144:

                MaquinaHipotetica.IncluirAI(AI, 18, 0, 0);
            	
            	break;
            case 145:

                MaquinaHipotetica.IncluirAI(AI, 17, 0, 0);
            	
            	break;
            case 146:

                MaquinaHipotetica.IncluirAI(AI, 16, 0, 0);
            	
            	break;
            case 147:

                MaquinaHipotetica.IncluirAI(AI, 9, 0, 0);
            	
            	break;
            case 148:

                MaquinaHipotetica.IncluirAI(AI, 5, 0, 0);
            	
            	break;
            case 149:

                MaquinaHipotetica.IncluirAI(AI, 6, 0, 0);
            	
            	break;
            case 150:

                MaquinaHipotetica.IncluirAI(AI, 12, 0, 0);
            	
            	break;
            case 151:

                MaquinaHipotetica.IncluirAI(AI, 7, 0, 0);
            	
            	break;
            case 152:

                MaquinaHipotetica.IncluirAI(AI, 8, 0, 0);
            	
            	break;
            case 153:

                MaquinaHipotetica.IncluirAI(AI, 11, 0, 0);
            	
            	break;
            case 154:
                int pen = Integer.parseInt((penultimo));

                MaquinaHipotetica.IncluirAI(AI, 3, 0, pen);
            	
            	break;
            case 155:

                MaquinaHipotetica.IncluirAI(Instrucao ,10, 0, 0);
            	
            	break;
            case 156:

                contexto = "expressão";

            	break;
        }


        }

    public static void possuiParametro(boolean temParametro) {
        temParametro = temParametro;

    }

    public static String tiraProximo(String a)
    {
        String aux = "";

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == ' ') {
                for (int j = i + 1; j < a.length(); j++) {
                    aux = aux + a.charAt(j);
                }
                break;
            }
        }

        return aux;
    }

    public static String proximo(String a)
    {
        String aux = "";

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == ' ') break;
            aux = aux + a.charAt(i);
        }

        return aux;
    }



}