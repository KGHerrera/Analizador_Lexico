import java.util.ArrayList;

public class AnalizadorLexico {	

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
				if(item == ';') {
					Token nuevoToken = new Token("FIN_SENTENCIA", String.valueOf(item));
					tokens.add(nuevoToken);
				} else if(item == '('){
					Token nuevoToken = new Token("PARENTECIS_A", String.valueOf(item));
					tokens.add(nuevoToken);
				} else if(item == ')'){
					Token nuevoToken = new Token("PARENTECIS_C", String.valueOf(item));
					tokens.add(nuevoToken);
				} else if(item == '{'){
					Token nuevoToken = new Token("LLAVE_A", String.valueOf(item));
					tokens.add(nuevoToken);
				} else if(item == '}'){
					Token nuevoToken = new Token("LLAVE_C", String.valueOf(item));
					tokens.add(nuevoToken);
				} else if(item == ',') {
					Token nuevoToken = new Token("SEPARADOR", String.valueOf(item));
					tokens.add(nuevoToken);
				}
				
				// operadores logicos y operador igual (=)
			} else if (item == 'y' || item == 'o' || item == '=' || item == '<' || item == '>') {

				if (entrada.length() > i) {

					if ((item == '=' || item == '<' || item == '>') && entrada.charAt(i + 1) == '=') {
						i++;

						Token nuevoToken = new Token("OPERADOR_LOGICO", item + "=");
						tokens.add(nuevoToken);
						continue;

					} else {

						if (item == '=') {
							Token nuevoToken = new Token("ASIGNACION", item + "");
							tokens.add(nuevoToken);

						} else if(item == 'y' || item == 'o'){
							Token nuevoToken = new Token("CONDICION_LOGICA", item + "");
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
				Token nuevoToken = new Token("OPERADOR_ARITMETICO", item + "");
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

						Token nuevoToken = new Token("NOMBRE_FUNCION", palabra);
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

						Token nuevoToken = new Token("NOMBRE_VARIABLE", palabra);
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

						palabra += item + "";
						for (int j = i + 1; j < entrada.length(); j++) {
							if (Character.isDigit(entrada.charAt(j)) || entrada.charAt(j) == '.' || Character.isLetter(entrada.charAt(i + 1))) {
								palabra += entrada.charAt(j);
								i++;

							} else {
								//i++;
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
							//i++;
							break;
						}
					}
					
					palabra.toLowerCase();
					
					if (palabra.equals("fun") || palabra.equals("ent") || palabra.equals("dec") || palabra.equals("cad") || palabra.equals("bol")
							|| palabra.equals("ver") || palabra.equals("fal") || palabra.equals("sic") || palabra.equals("sin")
							|| palabra.equals("cic") || palabra.equals("imp") || palabra.equals("fin") || palabra.equals("dev")) {
						
						if(palabra.equals("fun")) {
							Token nuevoToken = new Token("FUNCION", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("ent")) {
							Token nuevoToken = new Token("TIPO_VARIABLE", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("dec")) {
							Token nuevoToken = new Token("TIPO_VARIABLE", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("cad")) {
							Token nuevoToken = new Token("TIPO_VARIABLE", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("bol")) {
							Token nuevoToken = new Token("TIPO_VARIABLE", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("ver")) {
							Token nuevoToken = new Token("BOOLEANO", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("fal")) {
							Token nuevoToken = new Token("BOOLEANO", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("sic")) {
							Token nuevoToken = new Token("SI", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("sin")) {
							Token nuevoToken = new Token("ENTONCES", palabra);
							tokens.add(nuevoToken);
						} 
						
//						else if(palabra.equals("sin")) {
//							Token nuevoToken = new Token("ENTONCES_SI", palabra);
//							tokens.add(nuevoToken);
//						}
						
						else if(palabra.equals("cic")) {
							Token nuevoToken = new Token("CICLO", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("imp")) {
							Token nuevoToken = new Token("IMPRIMIR", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("fin")) {
							Token nuevoToken = new Token("BREAK", palabra);
							tokens.add(nuevoToken);
						} else if(palabra.equals("dev")) {
							Token nuevoToken = new Token("RETURN", palabra);
							tokens.add(nuevoToken);
						}
												
					}
										
				} else {
					continue;
				}
				
			}
		}

		return tokens;
	}

}
