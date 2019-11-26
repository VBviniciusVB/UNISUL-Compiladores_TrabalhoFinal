package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import semantico.AnalisadorSemantico;
import sintatico.Constants;
import sintatico.ParserConstants;
import sintatico.Pilha;

public class ControllerMain {

	@FXML
	private TextArea Texto;

	@FXML
	private TextArea Error;

	@FXML
	private Button ButtonCarregaArquivo;

	@FXML
	private Button ButtonLexico;

	@FXML
	private Button ButtonSintatico;

	@FXML
	private Button ButtonSemantico;

	// Tabela Token

	@FXML
	private TableView<Tabela> TabelaToken;

	@FXML
	private TableColumn<Tabela,String> TabelaTokenToken;

	@FXML
	private TableColumn<Tabela,String> TabelaTokenLinha;

	@FXML
	private TableColumn<Tabela,String> TabelaTokenTipo;

	@FXML
	private TableColumn<Tabela,String> TabelaTokenID;

	@FXML
	private TableColumn<Tabela,String> TabelaTokenNome;

	// Tabela Semantico

	@FXML
	private TableView<TabelaSemantico> TabelaSemantico;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoID;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoNome;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoCategoria;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoNivel;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoA;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoB;

	@FXML
	private TableColumn<TabelaSemantico,String> TabelaSemanticoProximo;

	// Tabela Intermediaria

	@FXML
	private TableView<TabelaIntermediaria> TabelaIntermediaria;

	@FXML
	private TableColumn<TabelaIntermediaria,String> TabelaIntermediariaNumero;

	@FXML
	private TableColumn<TabelaIntermediaria,String> TabelaIntermediariaCategoria;

	@FXML
	private TableColumn<TabelaIntermediaria,String> TabelaIntermediariaA;

	@FXML
	private TableColumn<TabelaIntermediaria,String> TabelaIntermediariaB;
	
	//
	//Label dos Simbolos
	//
	
	@FXML
	private TextArea TextoSimboloNome;
	
	@FXML
	private TextArea TextoSimboloCategoria;
	
	@FXML
	private TextArea TextoSimboloNivel;
	
	@FXML
	private TextArea TextoSimboloGeralA;
	
	@FXML
	private TextArea TextoSimboloGeralB;
	
	@FXML
	private TextArea TextoSimboloProximo;
	
	//Todo o Léxico e o Código serão armazenados neste lugar
	ArrayList<String> Lexico = new ArrayList<String>();
	ArrayList<Integer> LexicoID = new ArrayList<Integer>();
	ArrayList<String> LexicoCodigo = new ArrayList<String>();
	ArrayList<String> LexicoLinha = new ArrayList<String>();
	ArrayList<Integer> LexicoToken = new ArrayList<Integer>();
	

	

	@FXML
	private void ButtonProcuraArquivo (MouseEvent event) {

		String TEXTO = "";

		FileChooser fileChooser = new FileChooser();
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		fileChooser.setInitialDirectory(new File(currentPath));
		File selectedFile = fileChooser.showOpenDialog(new Stage());
		fileChooser.getInitialFileName();
		TEXTO = selectedFile.toString();

		try {
			FileReader arq = new FileReader(TEXTO);
			BufferedReader lerArq = new BufferedReader(arq);
			String linha = lerArq.readLine();

			String TEXT = "";

			while (linha != null) {

				TEXT = TEXT + linha + "\n";
				linha = lerArq.readLine();
			}

			Texto.setText(TEXT);

			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo",
					e.getMessage());
		}

	}

	// ---

	@FXML 
	private void ButtonLexico(MouseEvent event)
	{

		Lexico.clear();
		LexicoID.clear();
		LexicoCodigo.clear();
		LexicoLinha.clear();
		LexicoToken.clear();


		// Define a STRING do código inteiro léxico
		String Codigo = Texto.getText();

		// Define a quantia de letras do código léxico
		int Tamanho = Codigo.length();

		// Determina o atual token que esta
		String Token = ""; //Servirá para analizar qual as palavras do token

		// Determina um Token de Identificador
		String Identificador = ""; //Servirá para analizar qual as palavras do token

		// Definirá a letra atual para comparar se o token acabou ou não
		char Letra ;

		// Ignorar letras pois é complemento de um token
		int ignorar = 0;

		// Serve para quando achar um Token não ler como Identificador ou Número Inteiro
		boolean achou = false;

		int Linha = 1;

		//O sistema irá procurar letra por letra no código inteiro

		for (int i= 0;i<Tamanho;i++) {

			if (ignorar == 0) {

				Error.setText("Nenhum erro encontrado");

				achou = false;
				Token = "";
				Letra = Codigo.charAt(i);

				if (Letra == '$') {

					achou = true;
					Lexico.add("$");
					LexicoID.add(1);
					LexicoCodigo.add("Dolar");
					LexicoLinha.add(Linha+"");
				}
				if (Letra == ',' || Letra == ';' || Letra == '.' || Letra == ':' || Letra == '(' || Letra == ')') {

					achou = true;

					if ( Letra == ',') {
						Lexico.add(",");
						LexicoID.add(15);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ';') {
						Lexico.add(";");
						LexicoID.add(14);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '.') {
						Lexico.add(".");
						LexicoID.add(16);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ':') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add(":=");
							LexicoID.add(12);
							LexicoCodigo.add("Símbolos Especiais");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(":");
							LexicoID.add(13);
							LexicoCodigo.add("Símbolos Especiais");
							LexicoLinha.add(Linha+"");
						}

					}

					if ( Letra == '(' && '*' != Codigo.charAt(i+1)) {
						Lexico.add("(");
						LexicoID.add(17);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ')') {
						Lexico.add(")");
						LexicoID.add(18);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					//virgula (,); pontovirgula (;); ponto (.); doispontos(:);
					//atribuição(:=); abrepar((); fechapar()) ;

				}

				if (Letra >= 'A' && Letra <= 'Z') {

					if(Codigo.charAt(i) == 'A' && i < Tamanho - 3) {
						if(Codigo.charAt(i+1) == 'N') {
							if(Codigo.charAt(i+2) == 'D'){
								//AND
								achou = true;
								ignorar= 2;
								Lexico.add("AND");
								LexicoID.add(41);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'B' && i < Tamanho - 5) {
						if(Codigo.charAt(i+1) == 'E') {
							if(Codigo.charAt(i+2) == 'G'){
								if(Codigo.charAt(i+3) == 'I'){
									if(Codigo.charAt(i+4) == 'N'){
										//BEGIN
										achou = true;
										ignorar= 4;
										Lexico.add("BEGIN");
										LexicoID.add(26);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'C' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'O' && i < Tamanho - 5){
							if(Codigo.charAt(i+2) == 'N'){
								if(Codigo.charAt(i+3) == 'S'){
									if(Codigo.charAt(i+4) == 'T'){
										//CONST
										achou = true;
										ignorar= 4;
										Lexico.add("CONST");
										LexicoID.add(23);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'A' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'S'){
								if(Codigo.charAt(i+3) == 'E'){
									//CASE
									achou = true;
									ignorar= 3;
									Lexico.add("CASE");
									LexicoID.add(45);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}

							if(Codigo.charAt(i+2) == 'L'){
								if(Codigo.charAt(i+3) == 'L'){
									//CALL
									achou = true;
									ignorar= 3;
									Lexico.add("CALL");
									LexicoID.add(30);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'D' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'O'){
							//DO
							achou = true;
							ignorar= 1;
							Lexico.add("DO");
							LexicoID.add(35);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}
					}

					if(Codigo.charAt(i) == 'E' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'N'){
							if(Codigo.charAt(i+2) == 'D'){
								//END
								achou = true;
								ignorar= 2;
								Lexico.add("END");
								LexicoID.add(27);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}

						if(Codigo.charAt(i+1) == 'L' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'S'){
								if(Codigo.charAt(i+3) == 'E'){
									//ELSE
									achou = true;
									ignorar= 3;
									Lexico.add("ELSE");
									LexicoID.add(33);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'F' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'O'){
							if(Codigo.charAt(i+2) == 'R'){
								//FOR
								achou = true;
								ignorar= 2;
								Lexico.add("FOR");
								LexicoID.add(43);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'I' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'N' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'G'){
										if(Codigo.charAt(i+5) == 'E'){
											if(Codigo.charAt(i+6) == 'R'){
												//INTEGER
												achou = true;
												ignorar= 6;
												Lexico.add("INTEGER");
												LexicoID.add(28);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'F'){
							//IF
							achou = true;
							ignorar= 1;
							Lexico.add("IF");
							LexicoID.add(31);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}
					}

					if(Codigo.charAt(i) == 'N' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'O'){
							if(Codigo.charAt(i+2) == 'T'){
								//NOT
								achou = true;
								ignorar= 2;
								Lexico.add("NOT");
								LexicoID.add(42);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'O' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'F'){
							//OF
							achou = true;
							ignorar= 1;
							Lexico.add("OF");
							LexicoID.add(29);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

						if(Codigo.charAt(i+1) == 'R'){
							//OR
							achou = true;
							ignorar= 1;
							Lexico.add("OR");
							LexicoID.add(40);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

					}

					if(Codigo.charAt(i) == 'P' && i < Tamanho - 4){
						if(Codigo.charAt(i+1) == 'R'){
							if(Codigo.charAt(i+2) == 'O'){
								if(Codigo.charAt(i+3) == 'G' && i < Tamanho - 7){
									if(Codigo.charAt(i+4) == 'R'){
										if(Codigo.charAt(i+5) == 'A'){
											if(Codigo.charAt(i+6) == 'M'){
												//PROGRAM
												achou = true;
												ignorar= 6;
												Lexico.add("PROGRAM");
												LexicoID.add(22);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}

								if(Codigo.charAt(i+3) == 'C'  && i < Tamanho - 9){
									if(Codigo.charAt(i+4) == 'E'){
										if(Codigo.charAt(i+5) == 'D'){
											if(Codigo.charAt(i+6) == 'U'){
												if(Codigo.charAt(i+7) == 'R'){
													if(Codigo.charAt(i+8) == 'E'){
														//PROCEDURE
														achou = true;
														ignorar= 8;
														Lexico.add("PROCEDURE");
														LexicoID.add(25);
														LexicoCodigo.add("Palavras Reservadas");
														LexicoLinha.add(Linha+"");
													}
												}
											}
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'R'  && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'E'){

							if(Codigo.charAt(i+2) == 'A' && i < Tamanho - 6){
								if(Codigo.charAt(i+3) == 'D'){
									if(Codigo.charAt(i+4) == 'L'){
										if(Codigo.charAt(i+5) == 'N'){
											//READLN
											achou = true;
											ignorar= 5;
											Lexico.add("READLN");
											LexicoID.add(38);
											LexicoCodigo.add("Palavras Reservadas");
											LexicoLinha.add(Linha+"");
										}
									}
								}
							}

							if(Codigo.charAt(i+2) == 'P' && i < Tamanho - 6){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'A'){
										if(Codigo.charAt(i+5) == 'T'){
											//REPEAT
											achou = true;
											ignorar= 5;
											Lexico.add("REPEAT");
											LexicoID.add(36);
											LexicoCodigo.add("Palavras Reservadas");
											LexicoLinha.add(Linha+"");
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'T' && i < Tamanho - 2){

						if(Codigo.charAt(i+1) == 'O'){
							//TO
							achou = true;
							ignorar= 1;
							Lexico.add("TO");
							LexicoID.add(44);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

						if(Codigo.charAt(i+1) == 'H' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'E'){
								if(Codigo.charAt(i+3) == 'N'){
									//THEN
									achou = true;
									ignorar= 3;
									Lexico.add("THEN");
									LexicoID.add(32);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'U' && i < Tamanho - 5){
						if(Codigo.charAt(i+1) == 'N'){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'I'){
									if(Codigo.charAt(i+4) == 'L'){
										//UNTIL
										achou = true;
										ignorar= 4;
										Lexico.add("UNTIL");
										LexicoID.add(37);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'V' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'A'){
							if(Codigo.charAt(i+2) == 'R'){
								//VAR
								achou = true;
								ignorar= 2;
								Lexico.add("VAR");
								LexicoID.add(24);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'W' && i < Tamanho - 2){

						if(Codigo.charAt(i+1) == 'R' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'I'){
								if(Codigo.charAt(i+3) == 'T'){
									if(Codigo.charAt(i+4) == 'E'){
										if(Codigo.charAt(i+5) == 'L'){
											if(Codigo.charAt(i+6) == 'N'){
												//WRITELN
												achou = true;
												ignorar= 6;
												Lexico.add("WRITELN");
												LexicoID.add(39);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'H' && i < Tamanho - 5){
							if(Codigo.charAt(i+2) == 'I'){
								if(Codigo.charAt(i+3) == 'L'){
									if(Codigo.charAt(i+4) == 'E'){
										//WHILE
										achou = true;
										ignorar= 4;
										Lexico.add("WHILE");
										LexicoID.add(34);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					//AND – BEGIN – CALL – CASE – CONST – DO – ELSE – END – FOR – IF – INTEGER – NOT - OF
					//- OR - AND - PROCEDURE - PROGRAM – READLN - REPEAT - THEN - TO - UNTIL - VAR
					//- WHILE - WRITELN


					//Alguns símbolos não estavam inclusos mas eu adicionei também

					if(Codigo.charAt(i) == 'I' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'D'){
							//ID
							achou = true;
							ignorar= 1;
							Lexico.add("ID");
							LexicoID.add(19);
							LexicoCodigo.add("Identificador");
							LexicoLinha.add(Linha+"");
						}
						if(Codigo.charAt(i+1) == 'N' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'I'){
										if(Codigo.charAt(i+5) == 'R'){
											if(Codigo.charAt(i+6) == 'O'){
												//WRITELN
												achou = true;
												ignorar= 6;
												Lexico.add("INTEGER");
												LexicoID.add(28);
												LexicoCodigo.add("Inteiro");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'L' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'I'){
							if(Codigo.charAt(i+2) == 'T'){
								//ID
								achou = true;
								ignorar= 2;
								Lexico.add("LIT");
								LexicoID.add(21);
								LexicoCodigo.add("Literal");
								LexicoLinha.add(Linha+"");
							}
						}
					}
				}

				// - - -

				if ( '"' == Codigo.charAt(i) ) {

					achou = true;

					//Para saber se o código finalizou sem as " no final
					boolean FIM = false;

					int Atual = i+1;
					Token = Token + '"';

					LexicoLinha.add(Linha+"");

					while (FIM == false && (Codigo.charAt(Atual) != '"')) {

						if (Letra == '\n') {

							Linha++;
						}

						Letra = Codigo.charAt(Atual);
						Token = Token + Letra;
						Atual++;

						if ( Atual == Tamanho - 1 && Codigo.charAt(Tamanho-1) != '"') {

							FIM = true;
							Error.setText("Linha : "+Linha+" - Erro Encontrado, literal não foi finalizada");

							Letra = Codigo.charAt(Tamanho-1);
							Token = Token + Letra;



						}

					}

					Token = Token + '"';
					ignorar = Atual - i;

					Lexico.add(Token);
					LexicoID.add(0);
					LexicoCodigo.add("Literal");


					// Literal
				} 

				if (Letra == '+' || Letra == '-' || Letra == '*' || Letra == '/' || Letra == '<' || Letra == '>' || Letra == '=') {

					// AND e NOT estão nas Palavras reservadas ( O código será como um operador ) 

					achou = true;

					if ( Letra == '+') {
						Lexico.add("+");
						LexicoID.add(2);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '-') {
						Lexico.add("-");
						LexicoID.add(3);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '*') {
						Lexico.add("*");
						LexicoID.add(4);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '/') {
						Lexico.add("/");
						LexicoID.add(5);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '<') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add("<=");
							LexicoID.add(10);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}else {
							if( Codigo.charAt(i+1) == '>') {
								ignorar++;
								Lexico.add("<>");
								LexicoID.add(11);
								LexicoCodigo.add("Operador");
								LexicoLinha.add(Linha+"");
							}else {
								Lexico.add("<");
								LexicoID.add(9);
								LexicoCodigo.add("Operador");
								LexicoLinha.add(Linha+"");
							}
						}

					}

					if ( Letra == '>') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add(">=");
							LexicoID.add(8);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(">");
							LexicoID.add(7);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}

					}

					if ( Letra == '=') {
						Lexico.add("=");
						LexicoID.add(6);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					// Operadores

				}

				if ( Letra == ' ' || Letra == '\n' || Letra == '\t' ) {

					achou = true;

					// Isso serve para não entrar como identificador/número ou outro

					// Não vai fazer nada

					// Delimitadores

				}

				if ( '(' == Codigo.charAt(i) && '*' == Codigo.charAt(i+1)) {

					achou = true;

					int Atual = i+2;
					Token = Token + "(*";
					boolean FIM = false;

					LexicoLinha.add(Linha+"");

					while (FIM == false && (Codigo.charAt(Atual) != '*' || Codigo.charAt(Atual+1) != ')')) {

						if (Letra == '\n') {

							Linha++;
						}

						Letra = Codigo.charAt(Atual);
						Token = Token + Letra;
						Atual++;



						if ( Atual == Tamanho - 2 && (Codigo.charAt(Tamanho-2) != '*' && Codigo.charAt(Tamanho-1) != ')')) {

							if (Atual == Tamanho -2) {
								FIM = true;
							}

							Error.setText("Linha : "+LexicoLinha.get(LexicoLinha.size()-1)+" - Erro Encontrado, comentário não foi finalizado");

							Letra = Codigo.charAt(Tamanho-2);
							Token = Token + Letra;
							Letra = Codigo.charAt(Tamanho-1);
							Token = Token + Letra;



						}

					}            		

					Token = Token + "*)";            		
					ignorar = Atual - i + 1;

					Lexico.add(Token);
					LexicoID.add(0);
					LexicoCodigo.add("Comentário");


					// Comentários
				}       	

				if (achou == false) {

					// Identificador + Número Inteiro

					Identificador = Identificador + Letra;

					if(i == Tamanho-1) {
						Lexico.add(Lexico.size(),Identificador);
						LexicoID.add(19);
						LexicoCodigo.add(LexicoCodigo.size(),"Identificador");
						LexicoLinha.add(Linha+"");
						Error.setText("Linha : "+Linha+" - Caso o último token não seja um Identificador,por favor, finalize o token");

					}

				}else {

					if (Identificador.contentEquals("") ) {

					}else {

						int Valor = 0;

						//Verificar se ele adicionou um novo código ou não para adicionar antes
						if(Letra == ' ' || Letra == '\n' || Letra == '\t' || i == Tamanho) {
							Valor = 0;
						}else {
							Valor = 1;
						}

						//Verifica cada letra do Identificador para saber se é inteiro ou não
						boolean VerificaInteiro = true;

						for (int j=0;j<Identificador.length();j++) {
							if (Identificador.charAt(j) >= '0' && Identificador.charAt(j) <= '9') {
							}else {
								VerificaInteiro = false;
							}
						}

						if (VerificaInteiro == true) {
							Lexico.add(Lexico.size()-Valor,Identificador);
							LexicoID.add(LexicoID.size()-Valor,20);
							LexicoCodigo.add(LexicoCodigo.size()-Valor,"Numeros Inteiros");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(Lexico.size()-Valor,Identificador);
							LexicoID.add(LexicoID.size()-Valor,19);
							LexicoCodigo.add(LexicoCodigo.size()-Valor,"Identificador");
							LexicoLinha.add(Linha+"");
						}

						Identificador = "";
					}
				}

				if (Letra == '\n') {
					Linha++;
				}

			}else {

				ignorar--;

			}
		}

		//Error.setText("Token : "+Lexico+"\n"+"Codigo : "+LexicoCodigo);

		TabelaTokenToken.setCellValueFactory(
				new PropertyValueFactory<>("token"));
		TabelaTokenTipo.setCellValueFactory(
				new PropertyValueFactory<>("tipo"));
		TabelaTokenLinha.setCellValueFactory(
				new PropertyValueFactory<>("linha"));
		TabelaTokenID.setCellValueFactory(
				new PropertyValueFactory<>("ID"));
		TabelaTokenNome.setCellValueFactory(
				new PropertyValueFactory<>("nome"));

		LexicoID.add(1);

		int TAMANHO = 0;

		ObservableList<Tabela> Tabela1 = FXCollections.observableArrayList();
		for (int i=0;i<Lexico.size();i++) {
			TAMANHO++;
			LexicoToken.add(TAMANHO);
			Tabela1.add(new Tabela(LexicoToken.get(i)+"",LexicoCodigo.get(i),LexicoLinha.get(i),LexicoID.get(i)+"",Lexico.get(i)));

		}

		TabelaToken.setItems(Tabela1);

	}

	@FXML 
	private void ButtonSintatico(MouseEvent event)
	{

		System.out.print("Sintático : ");

		//Inicia a pilha de simbolos
		Pilha symbols = new Pilha();
		Pilha inputStack = new Pilha();

		//popular a pilha se simbolos
		symbols.startSymbolStack();

		//popula a pilha de entrada

		for (int i = LexicoID.size()-1; i >= 0; i--){
			inputStack.empilhar(LexicoID.get(i));
		}

		System.out.println("");
		int C = 0;
		int TokenAtual = 0;

		do {

			C++;

			AnalisadorSemantico.penultimo = AnalisadorSemantico.ultimo;
			AnalisadorSemantico.ultimo = Lexico.get(TokenAtual)+"";

			//pega os primeiros simbolos das pilhas
			Integer topoSimbolos = symbols.exibeUltimoValor();
			Integer topoEntrada = inputStack.exibeUltimoValor();

			//remove cadeia vazia
			while (topoSimbolos == Constants.EPSILON){
				symbols.desempilhar();
				topoSimbolos = symbols.exibeUltimoValor();
			}

			//System.out.println("Contou : "+C);
			System.out.println(topoSimbolos+" --- "+topoEntrada);

			//se x for terminal ou a pilha estiver vazia
			if(symbols.pilhaVazia() || isTerminal(topoSimbolos)){
				//se x = a e x Ã© $ entÃ£o analise terminada
				if(topoSimbolos.equals(topoEntrada)){

					if(topoEntrada == Constants.DOLLAR){
						//    	                        JOptionPane.showMessageDialog(null,"Analise finalizada");
						Error.setText("Analise finalizada");
						break;
					}
					Error.setText("Desempilhado da matriz de simbolos " + topoSimbolos + " desempilhado da matriz entrada " + topoEntrada);
					symbols.desempilhar();
					inputStack.desempilhar();

					if (Lexico.size() > TokenAtual+1){
						TokenAtual++;
					}


				}else{
					//    	                    JOptionPane.showMessageDialog(null,"\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
					Error.setText("\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
					System.out.println(ParserConstants.PARSER_ERROR[topoSimbolos]);
					break;
				}

			}else {

				// -----
				// Aqui ele verifica se é uma ação semântica ou não
				// -----

				if (topoSimbolos-77 >= 0){
					int Simbolo = topoSimbolos-77;

					System.out.println("Símbolo "+Simbolo);
					AnalisadorSemantico.AcaoSemantica(Simbolo);

					//
					symbols.desempilhar();


				}else{

					if(isInParserMatrix(topoSimbolos, topoEntrada)){

						symbols.desempilhar();
						int [] productionRules = getProductionRules(getParserMatrix(topoSimbolos, topoEntrada));

						for (int i = productionRules.length - 1; i >= 0; i--){
							symbols.empilhar(productionRules[i]);
						}
					}else {
						//    	                    JOptionPane.showMessageDialog(null, "Não encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
						Error.setText("Não encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
						System.out.println("Error : " + topoSimbolos + " - " + topoEntrada);
						for (int i = 0; i < symbols.pilha.length; i++){
							System.out.println("  " + symbols.pilha[i] + " - " + inputStack.pilha[i]);
						}
						break;
					}

				}


			}

		} while (!symbols.pilhaVazia() || C < 1000);
		

		
		//
		//
		// Semântico
		//
		//
		

		System.out.println("Mostrando :" + AnalisadorSemantico.SalvaParaMostrarTabelaSemantica.get(0));

		AdicionaTabelaSemantico();
		
	}



	@FXML
	private void AdicionaTabelaSemantico () {

		// Inicializar
		// Adicionar
		// Deletar
		// Buscar
		// Atualização


		TabelaSemanticoNome.setCellValueFactory(
				new PropertyValueFactory<>("nome"));
		TabelaSemanticoCategoria.setCellValueFactory(
				new PropertyValueFactory<>("categoria"));
		TabelaSemanticoNivel.setCellValueFactory(
				new PropertyValueFactory<>("nivel"));
		TabelaSemanticoA.setCellValueFactory(
				new PropertyValueFactory<>("a"));
		TabelaSemanticoB.setCellValueFactory(
				new PropertyValueFactory<>("b"));
		TabelaSemanticoProximo.setCellValueFactory(
				new PropertyValueFactory<>("proximo"));

		ObservableList<TabelaSemantico> Tabela2 = FXCollections.observableArrayList();

		Tabela2.add(new TabelaSemantico("1","2","3","4","5","6","7"));

		TabelaSemantico.setItems(Tabela2);

	}

	// ---

	public boolean isTerminal(int codigo){
		return codigo < ParserConstants.FIRST_NON_TERMINAL;
	}

	public boolean isInParserMatrix(int x, int a){
		return getParserMatrix(x,a) >= 0;
	}

	public int getParserMatrix(int x, int a){
		return Constants.PARSER_TABLE[x - ParserConstants.START_SYMBOL][a - 1];
	}

	public int[] getProductionRules(int matrixResult){
		return ParserConstants.PRODUCTIONS[matrixResult];
	}

	//
	// Definindo a Tabela Léxica
	//

	public static class Tabela {
		private final SimpleStringProperty token;
		private final SimpleStringProperty tipo;
		private final SimpleStringProperty linha;
		private final SimpleStringProperty ID;
		private final SimpleStringProperty nome;

		public Tabela(String token, String tipo, String linha, String id, String nome) {
			this.token = new SimpleStringProperty(token);
			this.tipo = new SimpleStringProperty(tipo);
			this.linha = new SimpleStringProperty(linha);
			this.ID = new SimpleStringProperty(id);
			this.nome = new SimpleStringProperty(nome);
		}

		// ---

		public String getToken() {
			return token.get();
		}

		public SimpleStringProperty tokenProperty() {
			return token;
		}

		public void setToken(String token) {
			this.token.set(token);
		}

		// ---

		public String getTipo() {
			return tipo.get();
		}

		public SimpleStringProperty tipoProperty() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo.set(tipo);
		}

		// ---

		public String getLinha() {
			return linha.get();
		}

		public SimpleStringProperty linhaProperty() {
			return linha;
		}

		public void setLinha(String linha) {
			this.linha.set(linha);
		}

		// ---

		public String getID() {
			return ID.get();
		}

		public SimpleStringProperty IDProperty() {
			return ID;
		}

		public void setID(String id) {
			this.ID.set(id);
		}

		// ---

		public String getNome() {
			return nome.get();
		}

		public SimpleStringProperty NomeProperty() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome.set(nome);
		}
	}

	//
	// Definindo a Tabela Semantica
	//

	public static class TabelaSemantico {
		private final SimpleStringProperty id;
		private final SimpleStringProperty nome;
		private final SimpleStringProperty categoria;
		private final SimpleStringProperty nivel;
		private final SimpleStringProperty a;
		private final SimpleStringProperty b;
		private final SimpleStringProperty proximo;

		public TabelaSemantico(String id, String nome, String categoria, String nivel, String a, String b, String proximo) {
			this.id = new SimpleStringProperty(id);
			this.nome = new SimpleStringProperty(nome);
			this.categoria = new SimpleStringProperty(categoria);
			this.nivel = new SimpleStringProperty(nivel);
			this.a = new SimpleStringProperty(a);
			this.b = new SimpleStringProperty(b);
			this.proximo = new SimpleStringProperty(proximo);
		}

		// ---

		public SimpleStringProperty getId() {
			return id;
		}

		public SimpleStringProperty idProperty() {
			return id;
		}

		// ---

		public SimpleStringProperty getNome() {
			return nome;
		}

		public SimpleStringProperty nomeProperty() {
			return nome;
		}

		// ---

		public SimpleStringProperty getCategoria() {
			return categoria;
		}

		public SimpleStringProperty categoriaProperty() {
			return categoria;
		}

		// ---

		public SimpleStringProperty getNivel() {
			return nivel;
		}

		public SimpleStringProperty nivelProperty() {
			return nivel;
		}

		// ---

		public SimpleStringProperty getA() {
			return a;
		}

		public SimpleStringProperty aProperty() {
			return a;
		}

		// ---

		public SimpleStringProperty getB() {
			return b;
		}

		public SimpleStringProperty bProperty() {
			return b;
		}

		// ---

		public SimpleStringProperty getProximo() {
			return proximo;
		}

		public SimpleStringProperty proximoProperty() {
			return proximo;
		}

		//		public void setToken(String token) {
		//			this.token.set(token);
		//		}

	}

	//
	// Definindo a Tabela Semantica
	//

	public static class TabelaIntermediaria {
		private final SimpleStringProperty numero;
		private final SimpleStringProperty categoria;
		private final SimpleStringProperty a;
		private final SimpleStringProperty b;

		public TabelaIntermediaria(String numero, String categoria, String a, String b) {
			this.numero = new SimpleStringProperty(numero);
			this.categoria = new SimpleStringProperty(categoria);
			this.a = new SimpleStringProperty(a);
			this.b = new SimpleStringProperty(b);
		}

		// ---

		public SimpleStringProperty getId() {
			return numero;
		}

		public SimpleStringProperty idProperty() {
			return numero;
		}

		// ---

		public SimpleStringProperty getCategoria() {
			return categoria;
		}

		public SimpleStringProperty categoriaProperty() {
			return categoria;
		}

		// ---

		public SimpleStringProperty getA() {
			return a;
		}

		public SimpleStringProperty aProperty() {
			return a;
		}

		// ---

		public SimpleStringProperty getB() {
			return b;
		}

		public SimpleStringProperty bProperty() {
			return b;
		}

	}

}