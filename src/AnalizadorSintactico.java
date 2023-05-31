import java.util.ArrayList;

public class AnalizadorSintactico {
	private ArrayList<Token> tokens;
	private int index;
	private Token currentToken;

	public void analizar(ArrayList<Token> tokens) {
		this.tokens = tokens;
		this.index = 0;
		this.currentToken = tokens.get(0);
		try {
			programa();
			System.out.println("Analisis sintactico exitoso FIN");
		} catch (Exception e) {
			System.out.println("Error de sintaxis: " + e.getMessage());
		}
	}

	// Regla de inicio: programa -> sentencia*
	private void programa() {
	    while (index < tokens.size()) {
	        try {
	            sentencia();
	        } catch (RuntimeException e) {
	            if (e.getMessage().equals("Detener analisis")) {
	                break; // Sale del bucle de análisis
	            }
	        }
	    }
	}
	
	// Regla: sentencia -> asignacion | estructura | funcion | imprimir | si
	private void sentencia() {
	    try {
	        if (currentToken.getTipo().equals("TIPO_VARIABLE")) {
	            asignacion();
	        } else if (currentToken.getTipo().equals("FUNCION")) {
	            funcion();
	        } else if (currentToken.getTipo().equals("IMPRIMIR")) {
	            imprimir();
	        } else if (currentToken.getTipo().equals("CICLO")) {
	            estructura();
	        } else if (currentToken.getTipo().equals("SI")) {
	            si();
	        } else if (currentToken.getTipo().equals("NOMBRE_VARIABLE")) {
	            variable();
	        } else {
	            throw new RuntimeException("Error de sintaxis. Sentencia invalida");
	        }
	        
	    } catch (RuntimeException e) {
	        System.out.println(e.getMessage());
	        throw new RuntimeException("Detener analisis");
	    }
	}
	
	private void variable() {
		verificar("NOMBRE_VARIABLE");
        if (currentToken.getTipo().equals("ASIGNACION")) {
            verificar("ASIGNACION");
            valor();
            verificar("FIN_SENTENCIA");
        } else {
            verificar("FIN_SENTENCIA");
        }
	}
	
	// Regla: si -> SI '(' condicion ')' bloque [SINO bloque]
	private void si() {
	    verificar("SI");
	    verificar("PARENTECIS_A");
	    condicion();
	    verificar("PARENTECIS_C");
	    bloque();
	    
	    if (currentToken.getTipo().equals("ENTONCES")) {
	        verificar("ENTONCES");
	        bloque();
	    }
	}


	// Regla: asignacion -> TIPO_VARIABLE NOMBRE_VARIABLE FIN_SENTENCIA |
	// TIPO_VARIABLE NOMBRE_VARIABLE '=' expresion ';'
	private void asignacion() {
	    verificar("TIPO_VARIABLE");
	    verificar("NOMBRE_VARIABLE");
	    if (currentToken.getTipo().equals("ASIGNACION")) {
	        verificar("ASIGNACION");
	        valor();
	        
	        if (currentToken.getTipo().equals("OPERADOR_ARITMETICO")) {
	            verificar("OPERADOR_ARITMETICO");
	            valor();
	        }
	        
	        verificar("FIN_SENTENCIA");
	    } else if (currentToken.getTipo().equals("FIN_SENTENCIA")) {
	        verificar("FIN_SENTENCIA");
	    }
	}
	

	// Regla: VALOR
	private void valor() {
		if (currentToken.getTipo().equals("ENTERO") || currentToken.getTipo().equals("DECIMAL")
	            || currentToken.getTipo().equals("CADENA") || currentToken.getTipo().equals("NOMBRE_VARIABLE")
	            || currentToken.getTipo().equals("BOOLEANO")) {
			
	        verificar(currentToken.getTipo());
	        
		} else {
			verificar("NOMBRE_VARIABLE O TIPO_DATO");
		}
	}
	
	// Regla: funcion -> FUNCION NOMBRE_FUNCION '(' ')' bloque
	private void funcion() {
		verificar("FUNCION");
		verificar("NOMBRE_FUNCION");
		verificar("PARENTECIS_A");
		verificar("PARENTECIS_C");
		bloque();
	}

	// Regla: imprimir -> IMPRIMIR '(' CADENA ')' ';'
	private void imprimir() {
	    verificar("IMPRIMIR");
	    verificar("PARENTECIS_A");
	    
	    if (currentToken.getTipo().equals("ENTERO") || currentToken.getTipo().equals("DECIMAL")
	            || currentToken.getTipo().equals("CADENA") || currentToken.getTipo().equals("NOMBRE_VARIABLE")
	            || currentToken.getTipo().equals("BOOLEANO")) {
	        valor();
	        
		} else {
			verificar("NOMBRE_VARIABLE O TIPO_DATO");
		}
	    
	    verificar("PARENTECIS_C");
	    verificar("FIN_SENTENCIA");
	}

	// Regla: estructura -> CICLO '(' ENTERO ')' bloque
	private void estructura() {
		verificar("CICLO");
		verificar("PARENTECIS_A");
		verificar("ENTERO");
		verificar("PARENTECIS_C");
		bloque();
	}

	// Regla: condicion -> expresion OPERADOR_LOGICO expresion
	private void condicion() {
	    valor();
	    verificar("OPERADOR_LOGICO");
	    valor();
	    
	    if (currentToken.getTipo().equals("CONDICION_LOGICA")) {
	        verificar("CONDICION_LOGICA");
	        condicion();
	    }
	}

	// Regla: bloque -> LLAVE_A sentencia* LLAVE_C
	private void bloque() {
		verificar("LLAVE_A");
		while (!currentToken.getTipo().equals("LLAVE_C")) {
			sentencia();
		}
		verificar("LLAVE_C");
	}

	// Método auxiliar para verificar y avanzar al siguiente token
	private void verificar(String tipo) {
		if (currentToken.getTipo().equals(tipo)) {
			System.out.println("token: " + currentToken);
			index++;
			if (index < tokens.size()) {
				currentToken = tokens.get(index);
				
			}
			
		} else {
			throw new RuntimeException("Error de sintaxis. Se esperaba '" + tipo + "'");
		}
	}
}
