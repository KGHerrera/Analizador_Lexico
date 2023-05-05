import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico {
	public String palabrasClave = "(fun|ent|dec|cad|bol|ver|fal|sic|noc|sin|cic|imp|fin|dev)";
	public String enteros = "[0-9]+";
	public String decimales = "\\d*\\.\\d+|\\d+\\.\\d*";
	public String cadenas = "'[^']*'";
	public String identificadores = "![a-zA-Z][a-zA-Z0-9]*";
	public String funciones = ":[a-zA-Z][a-zA-Z0-9]*";
	public String operadores = "[+|\\-|*|/|=]";
	public String operadoresLogicos = "(y|o|n|==|[><]=?)";
	public String estructuras = "\\(|\\)|\\{|\\}|\\;|\\,";

	public String patronTokens = "(" + identificadores + ")|(" + palabrasClave + ")|(" + decimales + ")|(" + enteros
			+ ")|(" + cadenas + ")|(" + funciones + ")|(" + operadoresLogicos + ")|(" + operadores + ")|(" + estructuras
			+ ")";

	public ArrayList<Token> obtenerTokens(String entrada) {

		ArrayList<Token> tokens = new ArrayList<Token>();
		Pattern patron = Pattern.compile(patronTokens);
		Matcher matcher = patron.matcher(entrada);
		
		

		while (matcher.find()) {
			String token = matcher.group().trim();
			String tipo = "";
			
			if (token.matches(identificadores)) {
				tipo = "IDENTIFICADOR";
			}else if (token.matches(palabrasClave)) {
				tipo = "PALABRA_CLAVE";
			} else if (token.matches(enteros)) {
				tipo = "ENTERO";
			} else if (token.matches(decimales)) {
				tipo = "DECIMAL";
			} else if (token.matches(cadenas)) {
				tipo = "CADENA";
			} else if (token.matches(funciones)) {
				tipo = "FUNCION";
			} else if (token.matches(operadoresLogicos)) {
				tipo = "OPERADOR_LOGICO";
			} else if (token.matches(operadores)) {
				tipo = "OPERADOR";
			} else if (token.matches(estructuras)) {
				tipo = "ESTRUCTURA";
			} 

			if (tipo != null) {
				Token nuevoToken = new Token(tipo, token);
				tokens.add(nuevoToken);
			}
		}

		return tokens;
	}

	public ArrayList<String> analizarTexto(String texto) {
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern patron = Pattern.compile(patronTokens);
		Matcher matcher = patron.matcher(texto);
		while (matcher.find()) {
			String token = matcher.group();
			if (!token.trim().isEmpty()) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	public ArrayList<Token> obtenerTokensSinMatcher(String entrada) {
		ArrayList<Token> tokens = new ArrayList<>();
		String[] palabras = entrada.split("\\s+");

		for (String palabra : palabras) {
			String tipo = "";
			if (palabra.matches(identificadores)) {
				tipo = "IDENTIFICADOR";
			} else if (palabra.matches(palabrasClave)) {
				tipo = "PALABRA_CLAVE";
			} else if (palabra.matches(enteros)) {
				tipo = "ENTERO";
			} else if (palabra.matches(decimales)) {
				tipo = "DECIMAL";
			} else if (palabra.matches(cadenas)) {
				tipo = "CADENA";
			} else if (palabra.matches(funciones)) {
				tipo = "FUNCION";
			} else if (palabra.matches(operadoresLogicos)) {
				tipo = "OPERADOR_LOGICO";
			} else if (palabra.matches(operadores)) {
				tipo = "OPERADOR";
			} else if (palabra.matches(estructuras)) {
				tipo = "ESTRUCTURA";
			} 

			if (!tipo.equals("")) {
				Token nuevoToken = new Token(tipo, palabra);
				tokens.add(nuevoToken);
			}
		}

		return tokens;
	}

	public ArrayList<Token> obtenerTokensAPie(String entrada) {

		String palabra = "";
		ArrayList<Token> tokens = new ArrayList<>();

		for (int i = 0; i < entrada.length(); i++) {
			char item = entrada.charAt(i);
			palabra = "";

			// si es un espacio regresa
			if (item == ' ') {
				palabra = "";
				continue;

				// estructuras
			} else if (item == '{' || item == '}' || item == '(' || item == ')' || item == ';' || item == ',') {
				Token nuevoToken = new Token("ESTRUCTURA", String.valueOf(item));
				tokens.add(nuevoToken);

				// operadores logicos y operador igual (=)
			} else if (item == 'y' || item == 'n' || item == 'o' || item == '=' || item == '<' || item == '>') {

				if (entrada.length() > i) {

					if ((item == '=' || item == '<' || item == '>') && entrada.charAt(i + 1) == '=') {
						i++;

						Token nuevoToken = new Token("OPERADOR_LOGICO", item + "=");
						tokens.add(nuevoToken);
						continue;

					} else {

						if (item == '=') {
							Token nuevoToken = new Token("OPERADOR", item + "");
							tokens.add(nuevoToken);

						} else {
							Token nuevoToken = new Token("OPERADOR_LOGICO", item + "");
							tokens.add(nuevoToken);
						}

					}

				} else {
					Token nuevoToken = new Token("OPERADOR_LOGICO", palabra);
					tokens.add(nuevoToken);
				}

				// operadores aritmeticos
			} else if (item == '+' || item == '-' || item == '*' || item == '/') {
				Token nuevoToken = new Token("OPERADOR", item + "");
				tokens.add(nuevoToken);

				// funciones D:
			} else if (item == ':') {
				if (entrada.length() > i) {
					if (Character.isLetter(entrada.charAt(i + 1))) {

						palabra = String.valueOf(item) + entrada.charAt(i + 1) + "";
						i++;

						for (int j = i + 1; j < entrada.length(); j++) {
							if (Character.isLetter(entrada.charAt(j)) || Character.isDigit(entrada.charAt(j))) {
								palabra += entrada.charAt(j);
								i++;

							} else {
								break;
							}
						}

						Token nuevoToken = new Token("FUNCION", palabra);
						tokens.add(nuevoToken);

					} else {
						continue;
					}
				}

				// identificadores
			} else if (item == '!') {
				if (entrada.length() > i) {
					if (Character.isLetter(entrada.charAt(i + 1))) {

						palabra = String.valueOf(item) + entrada.charAt(i + 1) + "";
						i++;

						for (int j = i + 1; j < entrada.length(); j++) {
							if (Character.isLetter(entrada.charAt(j)) || Character.isDigit(entrada.charAt(j))) {
								palabra += entrada.charAt(j);
								i++;

							} else {
								break;
							}
						}

						Token nuevoToken = new Token("IDENTIFICADOR", palabra);
						tokens.add(nuevoToken);

					} else {
						continue;
					}
				}

				// cadenas
			} else if (item == '\'') {
				if (entrada.length() > i) {

					if (entrada.charAt(i + 1) != '\'') {

						palabra = String.valueOf(item) + String.valueOf(entrada.charAt(i + 1));
						i++;

						for (int j = i + 1; j < entrada.length(); j++) {
							if (entrada.charAt(j) != '\'') {
								palabra += entrada.charAt(j);
								i++;

							} else {
								i++;
								break;
							}
						}

						Token nuevoToken = new Token("CADENA", palabra + "'");
						tokens.add(nuevoToken);

					} else {
						i++;
						Token nuevoToken = new Token("CADENA", "''");
						tokens.add(nuevoToken);
						continue;
					}
				}

				// numeros y numeros decimales
			} else if (Character.isDigit(item)) {
				
				if (entrada.length() > i) {

					if (Character.isDigit(entrada.charAt(i + 1)) || entrada.charAt(i+1) == '.' || Character.isLetter(entrada.charAt(i + 1))) {

						palabra+= item + "";
						for (int j = i + 1; j < entrada.length(); j++) {
							if (Character.isDigit(entrada.charAt(j)) || entrada.charAt(j) == '.' || Character.isLetter(entrada.charAt(i + 1))) {
								palabra += entrada.charAt(j);
								i++;

							} else {
								i++;
								break;
							}
						}
						
						try {							
						    double numero = Double.parseDouble(palabra);
						    
						    if(palabra.contains(".")) {
						    	Token nuevoToken = new Token("DECIMAL", palabra);
								tokens.add(nuevoToken);
						    } else {
						    	Token nuevoToken = new Token("ENTERO", palabra);
								tokens.add(nuevoToken);
						    }
							
						} catch (NumberFormatException e) {
						    continue;
						}

						

					} else {
						i++;
						Token nuevoToken = new Token("ENTERO", item + "");
						tokens.add(nuevoToken);
						continue;
					}
				} else {
					Token nuevoToken = new Token("ENTERO", item + "");
					tokens.add(nuevoToken);
				}
				
				// palabras clave fun|ent|dec|cad|bol|ver|fal|sic|noc|sin|cic|imp|fin|dev
			} else if(Character.isLetter(item)) {
				
				if(entrada.length() > i) {
					palabra += item;
					
					for (int j = i + 1; j < entrada.length(); j++) {
						if (Character.isLetter(entrada.charAt(i + 1))) {
							palabra += entrada.charAt(j);
							i++;

						} else {
							i++;
							break;
						}
					}
					
					palabra.toLowerCase();
					
					if (palabra.equals("fun") || palabra.equals("ent") || palabra.equals("dec") || palabra.equals("cad") || palabra.equals("bol")
							|| palabra.equals("ver") || palabra.equals("fal") || palabra.equals("sic") || palabra.equals("noc") || palabra.equals("sin")
							|| palabra.equals("cic") || palabra.equals("imp") || palabra.equals("fin") || palabra.equals("dev")) {
						
						Token nuevoToken = new Token("PALABRA_CLAVE", palabra);
						tokens.add(nuevoToken);
						
					}
										
				} else {
					continue;
				}
				
			}
		}

		return tokens; // cada dia menos eficiente
	}

}
