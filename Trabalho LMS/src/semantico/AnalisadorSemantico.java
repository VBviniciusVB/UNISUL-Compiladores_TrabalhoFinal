package semantico;

import java.util.ArrayList;

import application.ControllerMain;
import tabeladesimbolos.Simbolo;
import tabeladesimbolos.Tabela;

public class AnalisadorSemantico {

    public static String Ultimo = "Ultimo";
    public static String Penultimo = "Penultimo";
    public static String AntePenultimo = "999";
    private static int Acumulado = 3;
    public static Hipotetica MaquinaHipotetica;
    private static int End_Identificador = 0;
    private static int np;
    private static String Nome_Pro = "";
    private static String Nome_Identificador = "";
    private static String Nome_Atribuicao_Esquerda = "";
    private static String Nome_Procedimento = "";
    private static int npe = 0;
    private static String Contexto;
    private static int ForEndNome = 0;

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

    //Gravar valores para o c�digo intermedi�rio
    public static ArrayList<String> TABCodigo = new ArrayList<String>();
    public static ArrayList<Integer> TABGeralA = new ArrayList<Integer>();
    public static ArrayList<Integer> TABGeralB = new ArrayList<Integer>();

    //Gravar lista de erros dos cases
    public static ArrayList<String> CaseError = new ArrayList<String>();

    //Inicializa Instru��es e Literais da m�quina hipot�tica
    public static AreaInstrucoes AI = new AreaInstrucoes();
    public static AreaLiterais AL = new AreaLiterais();

    public static void Interpreta (){

        MaquinaHipotetica.Interpreta(AI,AL);

    }

    public static void AcaoSemantica (int Numero){

        switch (Numero) {
            case 100:

            	//Inicializa Instru��es e Literais na Hipotetica
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


                // Inicializa valores padr�es
                nivel_atual = 0;
                Pt_livre = 1;
                escopo[0] = 1;
                nv = 0;
                deslocamento = 3;
                Lc = 1;
                Lit = 1;

                //Limpar lista de s�mbolos
                Tabela.clearHashtable();

                //Limpar Tabela intermedi�ria
                TABCodigo.clear();
                TABGeralA.clear();
                TABGeralB.clear();

                //Limpa lista de erros sem�nticos
                CaseError.clear();


                break;
            case 101:

                MaquinaHipotetica.IncluirAI(AI, 26, 0, 0);
                TABCodigo.add("PARA");
                TABGeralA.add(0);
                TABGeralB.add(0);

                for (Simbolo simbolo : tabeladesimbolos.Tabela.hashtable) {
                    if (simbolo != null) {

                        if (simbolo.getCategoria().equals("r�tulo")){
                            if (simbolo.getGeralB().equals("")){
                                break;
                            }

                            CaseError.add("Erro sem�ntico, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        }
                    }
                }

                break;
            case 102:

                MaquinaHipotetica.IncluirAI(AI, 24, 0, Acumulado);
                TABCodigo.add("AMEM");
                TABGeralA.add(0);
                TABGeralB.add(Acumulado);

                Acumulado = 3;

                break;
            case 103:

                tipo_identificador = "r�tulo";

                break;
            case 104:

                Acumulado += 1;

                Simbolo Table104 = new Simbolo();
                Table104.setNome(Penultimo +"");
                Table104.setCategoria("vari�vel");
                Table104.setNivel(nivel_atual+"");
                Table104.setGeralA(deslocamento+"");
                Table104.setGeralB("");
                Table104.setProximo(null);

                System.out.println("Adicionando o penultimo : "+ Penultimo);

            	if (tipo_identificador.equals("r�tulo")) {
                    if (Tabela.buscar(Table104) != null) {

                        Simbolo Table104_2 = new Simbolo();
                        Table104_2 = Tabela.buscar(Table104);

                        int nivel = Integer.parseInt((Tabela.buscar(Table104)).getNivel()) ;
                        if (nivel == nivel_atual) {

                            CaseError.add("Erro sem�ntico, R�tulo j� foi declarado no mesmo n�vel, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        }
                        else{

                            Table104.setCategoria("r�tulo");
                            Table104.setGeralA("0");
                            Table104.setGeralB(Table104_2.getGeralB());

                            Tabela.adiciona(Table104);
                        }

                    } else {
                        Table104.setCategoria("r�tulo");
                        Table104.setGeralA("0");

                        Tabela.adiciona(Table104);
                    }

            	}


            	if (tipo_identificador.equals("vari�vel")) {
                    if (Tabela.buscar(Table104) == null) {

                        Tabela.adiciona(Table104);


                        deslocamento += 1;
                        nv += 1;

                    } else {

                        CaseError.add("Erro sem�ntico, vari�vel "+ Penultimo +" ja foi declarado, erro no token ("+(ControllerMain.TokenAtual+1)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                    }

            	}

            	if (tipo_identificador.equals("par�metro")) {

                    Table104.setCategoria("par�metro");
                    Table104.setGeralA("");

                    if (Tabela.buscar(Table104) != null) {
                        int nivel = Integer.parseInt((Tabela.buscar(Table104)).getNivel()) ;
                        if (nivel == nivel_atual) {

                            CaseError.add("Erro sem�ntico, Par�metro j� foi declarado no mesmo n�vel, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");


                        } else {

                            Tabela.adiciona(Table104);

                            np += 1;
                        }
                    } else {

                        Tabela.adiciona(Table104);

                        np += 1;
                    }

            	}

                System.out.println("Adicionando :  "+Table104.getNome()+" --- da categoria : "+Table104.getCategoria());

                break;
            case 105:

                Simbolo Table105 = new Simbolo();
                Table105.setNome(Penultimo +"");
                Table105.setCategoria("constante");
                Table105.setNivel(nivel_atual+"");
                Table105.setGeralA("0");
                Table105.setGeralB("0");
                Table105.setProximo(null);



                if (Tabela.buscar (Table105) != null) {

                    CaseError.add("Erro sem�ntico, Constante j� foi declarada, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                } else {

                    Tabela.adiciona(Table105);

                    End_Identificador = Integer.parseInt(Tabela.buscar(Table105).getNivel());
                }

                break;
            case 106:

                Simbolo Table106 = new Simbolo();
                Table106.setNome(End_Identificador +"");
                Table106 = Tabela.buscar (Table106);

                Simbolo Table106_2 = new Simbolo();
                Table106_2 = Table106;

                Table106_2.setGeralA(Penultimo);

                Tabela.atualizar(Table106,Table106_2);

                break;
            case 107:

            	tipo_identificador = "vari�vel";
                nv = 0;

                break;
            case 108:

                deslocamento = 3;
                Nome_Pro = Penultimo;

                Simbolo Table108 = new Simbolo();
                Table108.setNome(Penultimo +"");
                Table108.setCategoria("procedure");
                Table108.setNivel(nivel_atual+  "");
                Table108.setGeralA((AI.LC + 1) + "");
                Table108.setGeralB("0");
                Table108.setProximo(null);

                Tabela.adiciona(Table108);

                possuiParametro(false);

                //ser� mesmo que tem que adicionar NP para o par�metro ? n�o tinha nada informando
                parametros.add(np);

                nivel_atual += 1;
                np = 0;

                break;
            case 109:
                if (np > 0) {

                    Simbolo Table109 = new Simbolo();
                    Table109.setNome(Nome_Pro +"");
                    Table109 = Tabela.buscar (Table109);

                    Simbolo Table109_2 = new Simbolo();
                    Table109_2 = Table109;

                    Table109_2.setGeralB(np+"");


                    for (int i = 0; i < np; i++) {
                          Table109_2.setNivel(parametros.get(parametros.size() - 1)+"");
                          Table109_2.setGeralA(-(np - i)+"");

                          parametros.remove(parametros.get(parametros.size() - 1));
                    }

                    Tabela.atualizar(Table109,Table109_2);

                }

                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);
                TABCodigo.add("DSVS");
                TABGeralA.add(0);
                TABGeralB.add(0);

                procedures.add(AI.LC - 1);

                parametros.add(np);

                break;
            case 110:

                parametros.remove(parametros.get(parametros.size()-1));

                MaquinaHipotetica.IncluirAI(AI, 1, 0, np+1);
                TABCodigo.add("RETU");
                TABGeralA.add(0);
                TABGeralB.add(np+1);

                Simbolo Table110 = new Simbolo();
                Table110.setNome("Busca");
                Table110.setCategoria("r�tulo");
                Table110.setProximo(null);

                for (Simbolo simbolo : tabeladesimbolos.Tabela.hashtable) {
                    if (simbolo != null) {

                        if (simbolo.getCategoria().equals("r�tulo")){
                            if (simbolo.getGeralB().equals("")){
                                break;
                            }

                            CaseError.add("Erro sem�ntico, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        }

                    }
                }

                MaquinaHipotetica.AlterarAI(AI, procedures.get(procedures.size() - 1), 0, AI.LC);
                TABGeralA.set(procedures.get(procedures.size() - 1) , 0);
                TABGeralB.set(procedures.get(procedures.size() - 1) , AI.LC);

                procedures.remove(procedures.get(procedures.size()-1));

                Tabela.remover(Table110);

                nivel_atual -= 1;

                break;
            case 111:
                tipo_identificador = "par�metro";
                possuiParametro(true);

                break;
            case 112:
                Nome_Identificador = Penultimo;

            	break;
            case 113:

                Nome_Identificador = Penultimo;

                Simbolo Table113 = new Simbolo();
                Table113.setNome(Penultimo +"");
                Table113 = Tabela.buscar(Table113);

                if (Table113 != null) {
                    if (Table113.getCategoria().equals("r�tulo")) {
                        if (!Table113.getNivel().equals(nivel_atual)) {

                            CaseError.add("Erro sem�ntico, r�tulo n�o est� no escopo, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        } else {

                            Simbolo Table113_2 = new Simbolo();
                            Table113_2.setNome(Nome_Identificador +"");
                            Table113_2 = Tabela.buscar(Table113_2);
                            Table113_2.setGeralA(AI.LC+"");

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

                                    MaquinaHipotetica.AlterarAI(AI, endereco, 0, AI.LC);
                                    TABGeralA.set(endereco,0);
                                    TABGeralB.set(endereco,AI.LC);

                                }
                            }


                            Table113_2.setGeralA("");

                            Tabela.atualizar(Table113,Table113_2);

                        }
                    }
                    else

                        CaseError.add("Erro sem�ntico, r�tulo n�o est� declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }
                else {

                    CaseError.add("Erro sem�ntico, r�tulo n�o est� declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }

            	break;
            case 114:

                Nome_Identificador = Penultimo;

                System.out.println(Penultimo);

                Simbolo Table114 = new Simbolo();
                Table114.setNome(Penultimo +"");
                Table114 = Tabela.buscar(Table114);


                if (Table114 != null) {
                    if (!Table114.getCategoria().equals("vari�vel")){

                        CaseError.add("Erro sem�ntico, atribui��o da parte esquerda inv�lida, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                    } else {
                        Nome_Atribuicao_Esquerda = Nome_Identificador;
                    }
                } else {

                    CaseError.add("Erro sem�ntico, identificador "+ Penultimo +" n�o foi encontrado na tabela de s�mbolos, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }


                break;
            case 115:

                Nome_Identificador = Penultimo;

                Simbolo Table115 = new Simbolo();
                Table115.setNome(Nome_Atribuicao_Esquerda +"");
                Table115 = Tabela.buscar(Table115);

                if (Table115 == null){

                    System.out.println("Erro Sem�ntico");

                }else{

                    int d_nivel = nivel_atual - Integer.parseInt(Table115.getNivel());

                    MaquinaHipotetica.IncluirAI(AI, 4, d_nivel, Integer.parseInt(Table115.getGeralA()));
                    TABCodigo.add("ARMZ");
                    TABGeralA.add(d_nivel);
                    TABGeralB.add(Integer.parseInt(Table115.getGeralA()));

                }



            	break;
            case 116:

                Simbolo Table116 = new Simbolo();
                Table116.setNome(Penultimo +"");
                Table116 = Tabela.buscar(Table116);

                if (Table116 == null){

                    CaseError.add("Erro sem�ntico, procedure "+ Penultimo +" n�o foi declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }else{
                    if(Table116.getCategoria().equals("procedure")){
                        Nome_Procedimento = Penultimo;
                    }
                }

            	break;
            case 117:

                Nome_Procedimento = Nome_Pro;

                Simbolo Table117 = new Simbolo();
                Table117.setNome(Nome_Procedimento +"");
                Table117 = Tabela.buscar(Table117);

                if (Table117 == null){

                    CaseError.add("Erro sem�ntico, S�mbolo "+ Nome_Procedimento +" n�o declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }else{
                    if(!Table117.getGeralB().equals((np+""))){

                        CaseError.add("Erro sem�ntico, numero de parametros da procedure '  '" + Nome_Procedimento + " N�o conferem com o n�mero par�metros passados, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                    }else{

                        MaquinaHipotetica.IncluirAI(AI, 25, 0, Integer.parseInt(Table117.getGeralA()));
                        TABCodigo.add("CALL");
                        TABGeralA.add(0);
                        TABGeralB.add(Integer.parseInt(Table117.getGeralA()));

                    }
                }

            	break;
            case 118:

                // Para que server isso ? Nunca � usado
                npe += 1;

            	break;
            case 119:

                Simbolo Table119 = new Simbolo();
                Table119.setNome(Penultimo +"");
                Table119 = Tabela.buscar(Table119);

                if(Table119 == null){

                    CaseError.add("Erro sem�ntico, identificador "+ Penultimo +" n�o est� declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }else {
                    if (Table119.getNivel().equals(nivel_atual)) {

                        CaseError.add("Erro sem�ntico, o r�tulo n�o est� declarado no escopo do n�vel, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                    } else {

                        int op2 = Integer.parseInt(Table119.getGeralB());



                        if (op2 != 0) {

                            MaquinaHipotetica.IncluirAI(AI, 19, 0, op2);
                            TABCodigo.add("DSVS");
                            TABGeralA.add(0);
                            TABGeralB.add(op2);

                        } else {

                            MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);
                            TABCodigo.add("DSVS");
                            TABGeralA.add(0);
                            TABGeralB.add(0);

                            Simbolo Table119_2 = new Simbolo();
                            Table119_2 = Tabela.buscar(Table119);

                            String ind = Table119_2.getNivel();

                            Table119_2.setNivel(ind);

                            Tabela.atualizar(Table119,Table119_2);

                        }
                    }
                }


            	break;
            case 120:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);
                TABCodigo.add("DSVF");
                TABGeralA.add(0);
                TABGeralB.add(0);

                ifs.add(AI.LC - 1);

            	break;
            case 121:

                MaquinaHipotetica.AlterarAI(AI, ifs.get(ifs.size() - 1), 0, AI.LC);
                TABGeralA.set(ifs.get(ifs.size() - 1),0);
                TABGeralB.set(ifs.get(ifs.size() - 1),AI.LC);

                ifs.remove(ifs.get(ifs.size() - 1));

            	break;
            case 122:

                MaquinaHipotetica.AlterarAI(AI, ifs.get(ifs.size() - 1), 0, AI.LC + 1);
                TABGeralA.set(ifs.get(ifs.size() - 1), 0);
                TABGeralB.set(ifs.get(ifs.size() - 1), AI.LC + 1);

                ifs.remove(ifs.get(ifs.size() - 1));

                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);
                TABCodigo.add("DSVS");
                TABGeralA.add(0);
                TABGeralB.add(0);


                ifs.add(AI.LC - 1);

            	break;
            case 123:

                whiles.add(AI.LC);

            	break;
            case 124:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);
                TABCodigo.add("DSVF");
                TABGeralA.add(0);
                TABGeralB.add(0);

                whiles.add(AI.LC - 1);

            	break;
            case 125:


                MaquinaHipotetica.AlterarAI(AI, whiles.get(whiles.size() - 1), 0, AI.LC + 1);
                TABGeralA.set(whiles.get(whiles.size() - 1), 0);
                TABGeralB.set(whiles.get(whiles.size() - 1), AI.LC + 1);

                whiles.remove(whiles.get(whiles.size() - 1));

                if (whiles.size() > 0){
                    MaquinaHipotetica.IncluirAI(AI, 19, 0, whiles.get(whiles.size() -1));
                    TABGeralB.add(whiles.get(whiles.size() - 1));
                }else{
                    MaquinaHipotetica.IncluirAI(AI, 19, 0, 1);
                    TABGeralB.add(0);
                }

                TABCodigo.add("DSVS");
                TABGeralA.add(0);

                whiles.remove(whiles.get(whiles.size() - 1));

            	break;
            case 126:

                repeats.add(AI.LC);

            	break;
            case 127:

                MaquinaHipotetica.IncluirAI(AI, 20, 0, repeats.get(repeats.size()));
                TABCodigo.add("DSVF");
                TABGeralA.add(0);
                TABGeralB.add(repeats.get(repeats.size()));

                repeats.remove(repeats.get(repeats.size() - 1));

            	break;
            case 128:

                Contexto = "readln";

            	break;
            case 129:

                Simbolo Table129 = new Simbolo();
                Table129.setNome(Penultimo +"");
                Table129 = Tabela.buscar(Table129);

                System.out.println("Aqui esta o penultimo : "+ Penultimo);

                if (Table129 == null)
                    {

                        CaseError.add("Erro sem�ntico, identificador "+ Penultimo +" n�o est� declarado, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                    }else{

                        int d_nivel2 = nivel_atual - Integer.parseInt(Table129.getNivel());

                    if (Contexto.equals("readln")) {

                        if (Table129.getCategoria().equals("vari�vel")) {

                            MaquinaHipotetica.IncluirAI(AI, 21, 0, 0);
                            TABCodigo.add("LEIT");
                            TABGeralA.add(0);
                            TABGeralB.add(0);

                            MaquinaHipotetica.IncluirAI(AI, 4, d_nivel2, Integer.parseInt(Table129.getGeralA()));
                            TABCodigo.add("ARMZ");
                            TABGeralA.add(d_nivel2);
                            TABGeralB.add(Integer.parseInt(Table129.getGeralA()));

                            }
                        else {

                            CaseError.add("Erro sem�ntico, identificador "+ Penultimo +" n�o � uma vari�vel, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        }
                    }

                    if (Contexto.equals("express�o"))
                    {

                        if (Table129.getCategoria().equals("procedure") || (Table129.getCategoria().equals("r�tulo")))
                        {

                            CaseError.add("Erro sem�ntico, identificador "+ Penultimo +" n�o � uma constante, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                        }
                        else if (Table129.getCategoria().equals("constante")) {

                            MaquinaHipotetica.IncluirAI(AI, 3, 0, Integer.parseInt(Table129.getGeralA()));
                            TABCodigo.add("CRCT");
                            TABGeralA.add(0);
                            TABGeralB.add(Integer.parseInt(Table129.getGeralA()));

                            } else {

                            MaquinaHipotetica.IncluirAI(AI, 2, d_nivel2, Integer.parseInt(Table129.getGeralA()));
                            TABCodigo.add("CRVL");
                            TABGeralA.add(d_nivel2);
                            TABGeralB.add(Integer.parseInt(Table129.getGeralA()));

                        }

                    }

                }

            	break;
            case 130:

                Simbolo Table130 = new Simbolo();
                Table130.setNome(Penultimo +"");
                Table130 = Tabela.buscar(Table130);

                MaquinaHipotetica.IncluirAL(AL, Penultimo);

                MaquinaHipotetica.IncluirAI(AI, 23, 0, AL.LIT - 1);
                TABCodigo.add("IMPRLIT");
                TABGeralA.add(0);
                TABGeralB.add(AL.LIT - 1);


            	break;
            case 131:

                MaquinaHipotetica.IncluirAI(AI, 22, 0,0);
                TABCodigo.add("IMPR");
                TABGeralA.add(0);
                TABGeralB.add(0);

            	break;
            case 132:

                //Acopla mecanismo de controle de inicio de CASE junto � pilha de controle de CASE

                // N�O FAZ NADA ???
            	
            	break;
            case 133:

                MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size() - 1), 0,AI.LC);
                TABGeralA.set(cases.get(cases.size() - 1), 0);
                TABGeralB.set(cases.get(cases.size() - 1), AI.LC);

                cases.remove(cases.get(cases.size() - 1));

                MaquinaHipotetica.IncluirAI(AI, 24, 0,-1);
                TABCodigo.add("AMEM");
                TABGeralA.add(0);
                TABGeralB.add(-1);
            	
            	break;
            case 134:

                MaquinaHipotetica.IncluirAI(AI, 28, 0,0);
                TABCodigo.add("COPI");
                TABGeralA.add(0);
                TABGeralB.add(0);

                int ant = Integer.parseInt(AntePenultimo);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, ant);
                TABCodigo.add("CRCT");
                TABGeralA.add(0);
                TABGeralB.add(ant);

                MaquinaHipotetica.IncluirAI(AI, 15, 0,0);
                TABCodigo.add("CMIG");
                TABGeralA.add(0);
                TABGeralB.add(0);


                if (cases.size() > 0) {

                    MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size() - 1), 0,AI.LC + 1);
                    TABGeralA.set(cases.get(cases.size() - 1), 0);
                    TABGeralB.set(cases.get(cases.size() - 1), AI.LC + 1);

                    cases.remove(cases.get(cases.size() - 1));

                }

                MaquinaHipotetica.IncluirAI(AI, 20, 0,0);
                TABCodigo.add("DSVF");
                TABGeralA.add(0);
                TABGeralB.add(0);

                cases.add(AI.LC - 1);
            	
            	break;
            case 135:

                if (cases.size() > 0) {

                    MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size() - 1), 0, AI.LC + 1);
                    TABGeralA.set(cases.get(cases.size() - 1), 0);
                    TABGeralB.set(cases.get(cases.size() - 1), AI.LC + 1);

                    cases.remove(cases.get(cases.size() - 1));
                }



                MaquinaHipotetica.IncluirAI(AI, 19, 0, 0);
                TABCodigo.add("DSVS");
                TABGeralA.add(0);
                TABGeralB.add(0);

                cases.add(AI.LC - 1);

            	break;
            case 136:

                if (cases.size() > 0) {

                    MaquinaHipotetica.AlterarAI(AI, cases.get(cases.size()-1), 0, AI.LC + 1);
                    TABGeralA.set(cases.get(cases.size() - 1), 0);
                    TABGeralB.set(cases.get(cases.size() - 1), AI.LC + 1);

                    cases.remove(cases.get(cases.size() - 1));

                }

                MaquinaHipotetica.IncluirAI(AI, 28, 0, 0);
                TABCodigo.add("COPI");
                TABGeralA.add(0);
                TABGeralB.add(0);

                int ant2 = Integer.parseInt(AntePenultimo);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, ant2);
                TABCodigo.add("CRCT");
                TABGeralA.add(0);
                TABGeralB.add(ant2);

                MaquinaHipotetica.IncluirAI(AI, 15, 0, 0);
                TABCodigo.add("CMIG");
                TABGeralA.add(0);
                TABGeralB.add(0);

                MaquinaHipotetica.IncluirAI(AI, 29, 0, 0);
                TABCodigo.add("DSVT");
                TABGeralA.add(0);
                TABGeralB.add(0);

                cases.add(AI.LC - 1);

            	break;
            case 137:

                Simbolo Table137 = new Simbolo();
                Table137.setNome(Penultimo +"");
                Table137 = Tabela.buscar(Table137);

                if ((Table137 != null)  && (Table137.getCategoria().equals("vari�vel"))) {

                    ForEndNome =  Table137.hashCode();
                }
                else {

                    CaseError.add("Erro sem�ntico, vari�vel "+ Penultimo +" n�o declarada, erro no token ("+(ControllerMain.TokenAtual)+") da linha ("+ControllerMain.LexicoLinha.get(ControllerMain.TokenAtual - 1)+")");

                }

            	break;
            case 138:

                Simbolo Table138 = new Simbolo();
                Table138.setNome(ForEndNome +"");
                Table138 = Tabela.buscar(Table138);

                int op1 = nivel_atual - Integer.parseInt(Table138.getNivel());
                int op2 = Integer.parseInt(Table138.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 4, op1, op2);
                TABCodigo.add("ARMZ");
                TABGeralA.add(op1);
                TABGeralB.add(op2);
            	
            	break;
            case 139:

                fors.add(AI.LC);

                MaquinaHipotetica.IncluirAI(AI, 28, 0, 0);
                TABCodigo.add("COPI");
                TABGeralA.add(0);
                TABGeralB.add(0);

                Simbolo Table139 = new Simbolo();
                Table139.setNome(ForEndNome +"");
                Table139 = Tabela.buscar(Table139);

                int op12 = nivel_atual - Integer.parseInt(Table139.getNivel());
                int op22 = Integer.parseInt(Table139.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 2, op12, op22);
                TABCodigo.add("CRVL");
                TABGeralA.add(op12);
                TABGeralB.add(op22);

                MaquinaHipotetica.IncluirAI(AI, 18, 0, 0);
                TABCodigo.add("CMAI");
                TABGeralA.add(0);
                TABGeralB.add(0);

                fors.add(AI.LC);

                MaquinaHipotetica.IncluirAI(AI, 20, 0, 0);
                TABCodigo.add("DSVF");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 140:

                Simbolo Table140 = new Simbolo();
                Table140.setNome(ForEndNome +"");
                Table140 = Tabela.buscar(Table140);

                int op13 = nivel_atual - Integer.parseInt(Table140.getNivel());
                int op23 = Integer.parseInt(Table140.getGeralA());

                MaquinaHipotetica.IncluirAI(AI, 2, op13, op23);
                TABCodigo.add("CRVL");
                TABGeralA.add(op13);
                TABGeralB.add(op23);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, 1);
                TABCodigo.add("CRCT");
                TABGeralA.add(0);
                TABGeralB.add(1);

                MaquinaHipotetica.IncluirAI(AI, 5, 0, 1);
                TABCodigo.add("SOMA");
                TABGeralA.add(0);
                TABGeralB.add(1);

                MaquinaHipotetica.IncluirAI(AI, 4, op13, op23);
                TABCodigo.add("ARMZ");
                TABGeralA.add(op13);
                TABGeralB.add(op23);

                MaquinaHipotetica.IncluirAI(AI, 3, 0, 1);
                TABCodigo.add("CRCT");
                TABGeralA.add(0);
                TABGeralB.add(1);

                MaquinaHipotetica.AlterarAI(AI, fors.get(fors.size()-1), 0, AI.LC + 1);
                TABGeralA.set(fors.get(fors.size() - 1), 0);
                TABGeralB.set(fors.get(fors.size() - 1), AI.LC + 1);

                fors.remove(fors.get(fors.size()-1));

                MaquinaHipotetica.IncluirAI(AI, 19, 0, fors.get(fors.size()-1));
                TABCodigo.add("DSVS");
                TABGeralA.add(0);
                TABGeralB.add(fors.get(fors.size()-1));

                MaquinaHipotetica.IncluirAI(AI, 24, 0, -1);
                TABCodigo.add("AMEM");
                TABGeralA.add(0);
                TABGeralB.add(-1);
            	
            	break;
            case 141:

                MaquinaHipotetica.IncluirAI(AI, 15, 0, 0);
                TABCodigo.add("CMIG");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 142:

                MaquinaHipotetica.IncluirAI(AI, 13, 0, 0);
                TABCodigo.add("CMME");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 143:

                MaquinaHipotetica.IncluirAI(AI, 14, 0, 0);
                TABCodigo.add("CMMA");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 144:

                MaquinaHipotetica.IncluirAI(AI, 18, 0, 0);
                TABCodigo.add("CMAI");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 145:

                MaquinaHipotetica.IncluirAI(AI, 17, 0, 0);
                TABCodigo.add("CMEI");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 146:

                MaquinaHipotetica.IncluirAI(AI, 16, 0, 0);
                TABCodigo.add("CMDF");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 147:

                MaquinaHipotetica.IncluirAI(AI, 9, 0, 0);
                TABCodigo.add("INVR");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 148:

                MaquinaHipotetica.IncluirAI(AI, 5, 0, 0);
                TABCodigo.add("SOMA");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 149:

                MaquinaHipotetica.IncluirAI(AI, 6, 0, 0);
                TABCodigo.add("SUBT");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 150:

                MaquinaHipotetica.IncluirAI(AI, 12, 0, 0);
                TABCodigo.add("DISJ");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 151:

                MaquinaHipotetica.IncluirAI(AI, 7, 0, 0);
                TABCodigo.add("MULT");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 152:

                MaquinaHipotetica.IncluirAI(AI, 8, 0, 0);
                TABCodigo.add("DIVI");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 153:

                MaquinaHipotetica.IncluirAI(AI, 11, 0, 0);
                TABCodigo.add("CONJ");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 154:

                int pen = Integer.parseInt((Penultimo));

                MaquinaHipotetica.IncluirAI(AI, 3, 0, pen);
                TABCodigo.add("CRCT");
                TABGeralA.add(0);
                TABGeralB.add(pen);
            	
            	break;
            case 155:

                MaquinaHipotetica.IncluirAI(AI ,10, 0, 0);
                TABCodigo.add("NEGA");
                TABGeralA.add(0);
                TABGeralB.add(0);
            	
            	break;
            case 156:

                Contexto = "express�o";

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