import java.util.ArrayList;

public class AnalizadorSintactico {
	private ArrayList<Token> tokens;
	private int index;
	private Token currentToken;

	public Nodo analizar(ArrayList<Token> tokens) {
		this.tokens = tokens;
		this.index = 0;
		this.currentToken = tokens.get(0);
		
		Nodo arbolSintactico = new Nodo(null);
		
		try {
			arbolSintactico = programa();
			System.out.println("Analisis sintactico exitoso FIN");
			return arbolSintactico;
		} catch (Exception e) {
			System.out.println("Error de sintaxis: " + e.getMessage());
			return new Nodo("");
		}
				
	}
	
	public static void imprimirArbolSintactico(Nodo nodo, int nivel) {
	    for (int i = 0; i < nivel; i++) {
	        System.out.print("  ");
	    }
	    System.out.println(nodo.getValor());
	    for (Nodo hijo : nodo.getHijos()) {
	        imprimirArbolSintactico(hijo, nivel + 1);
	    }
	}

	// Regla de inicio: programa -> sentencia*
	private Nodo programa() {
		
		Nodo programa = new Nodo("programa");
		
	    while (index < tokens.size()) {
	        try {
	            programa.agregarHijo(sentencia());
	        } catch (RuntimeException e) {
	            if (e.getMessage().equals("Detener analisis")) {
	                break; // Sale del bucle de análisis
	            }
	        }
	    }
	    
	    return programa;
	}
	
	// Regla: sentencia -> asignacion | estructura | funcion | imprimir | si | NOMBRE_VARIABLE | NOMBRE_FUNCION
	private Nodo sentencia() {
	    try {
	        if (currentToken.getTipo().equals("TIPO_VARIABLE")) {
	            return asignacion();
	        } else if (currentToken.getTipo().equals("FUNCION")) {
	            return funcion();
	        } else if (currentToken.getTipo().equals("IMPRIMIR")) {
	            return imprimir();
	        } else if (currentToken.getTipo().equals("CICLO")) {
	            return ciclo();
	        } else if (currentToken.getTipo().equals("SI")) {
	            return si();
	        } else if (currentToken.getTipo().equals("NOMBRE_VARIABLE")) {
	            return variable();
	        } else if (currentToken.getTipo().equals("NOMBRE_FUNCION")) {
	            return llamarFuncion();
	        } else {
	            throw new RuntimeException("Error de sintaxis. Sentencia invalida");
	        }
	    } catch (RuntimeException e) {
	        System.out.println(e.getMessage());
	        throw new RuntimeException("Detener analisis");
	    }
	}
	// Regla: llamarFuncion -> NOMBRE_FUNCION | PARENTECIS_A | PARENTECIS_C | FIN_SENTENCIA
	private Nodo llamarFuncion() {
		Nodo llamarFuncion = new Nodo("llamarFuncion");
		
		verificar("NOMBRE_FUNCION");
        verificar("PARENTECIS_A");
        verificar("PARENTECIS_C");
        verificar("FIN_SENTENCIA");
        
        return llamarFuncion;
	}
	
	// Regla: variable -> NOMBRE_VARIABLE | ASIGNACION | VALOR | FIN_SENTENCIA
	private Nodo variable() {
		Nodo variable = new Nodo("variable");
		verificar("NOMBRE_VARIABLE");
        if (currentToken.getTipo().equals("ASIGNACION")) {
            verificar("ASIGNACION");
            variable.agregarHijo(valor());
            verificar("FIN_SENTENCIA");
        } else {
            verificar("FIN_SENTENCIA");
        }
        return variable;
	}
	
	// Regla: si -> SI '(' condicion ')' bloque [SINO bloque]
	private Nodo si() {
		
		Nodo si = new Nodo("comparacion");
	    verificar("SI");
	    verificar("PARENTECIS_A");
	    si.agregarHijo(condicion());
	    verificar("PARENTECIS_C");
	    si.agregarHijo(bloque());
	    
	    if (currentToken.getTipo().equals("ENTONCES")) {
	        verificar("ENTONCES");
	        si.agregarHijo(bloque());
	    }
	    return si;
	}


	// Regla: asignacion -> TIPO_VARIABLE NOMBRE_VARIABLE FIN_SENTENCIA | TIPO_VARIABLE NOMBRE_VARIABLE '=' expresion ';'
	private Nodo asignacion() {
		Nodo asignacion = new Nodo("asignacion");
		
	    verificar("TIPO_VARIABLE");
	    verificar("NOMBRE_VARIABLE");
	    if (currentToken.getTipo().equals("ASIGNACION")) {
	        verificar("ASIGNACION");
	        
	        asignacion.agregarHijo(valor());
	        
	        if (currentToken.getTipo().equals("OPERADOR_ARITMETICO")) {
	        	verificar("OPERADOR_ARITMETICO");
	        	asignacion.agregarHijo(valor());
	        }
	        
	        verificar("FIN_SENTENCIA");
	    } else if (currentToken.getTipo().equals("FIN_SENTENCIA")) {
	        verificar("FIN_SENTENCIA");
	    }
	    
	    return asignacion;
	}
	// Regla: operacion -> VALOR | OPERADOR_ARITMETICO
	private Nodo operacionAritmetica() {
		
		Nodo operacion = new Nodo("operacion");
		operacion.agregarHijo(valor());
        if (currentToken.getTipo().equals("OPERADOR_ARITMETICO")) {
        	verificar("OPERADOR_ARITMETICO");
        	operacion.agregarHijo(valor());	
        }
        
        return operacion;
	}
	

	// Regla: VALOR
	private Nodo valor() {
		
		Nodo valor = new Nodo("valor");
		if (currentToken.getTipo().equals("ENTERO") || currentToken.getTipo().equals("DECIMAL")
	            || currentToken.getTipo().equals("CADENA") || currentToken.getTipo().equals("NOMBRE_VARIABLE")
	            || currentToken.getTipo().equals("BOOLEANO")) {
			
	        verificar(currentToken.getTipo());
	        
		} else {
			verificar("NOMBRE_VARIABLE O TIPO_DATO");
		}
		
		return valor;
	}
	
	// Regla: funcion -> FUNCION NOMBRE_FUNCION '(' ')' bloque
	private Nodo funcion() {
		Nodo funcion = new Nodo("funcion");
		verificar("FUNCION");
		verificar("NOMBRE_FUNCION");
		verificar("PARENTECIS_A");
		verificar("PARENTECIS_C");
		funcion.agregarHijo(bloque());
		
		return funcion;
	}

	// Regla: imprimir -> IMPRIMIR '(' CADENA ')' ';'
	private Nodo imprimir() {
		Nodo imprimir = new Nodo("imprimir");
	    verificar("IMPRIMIR");
	    verificar("PARENTECIS_A");
	    
	    if (currentToken.getTipo().equals("ENTERO") || currentToken.getTipo().equals("DECIMAL")
	            || currentToken.getTipo().equals("CADENA") || currentToken.getTipo().equals("NOMBRE_VARIABLE")
	            || currentToken.getTipo().equals("BOOLEANO")) {
	        imprimir.agregarHijo(valor());
	        
		} else {
			verificar("NOMBRE_VARIABLE O TIPO_DATO");
		}
	    
	    verificar("PARENTECIS_C");
	    verificar("FIN_SENTENCIA");
	    return imprimir;
	}

	// Regla: estructura -> CICLO '(' ENTERO ')' bloque
	private Nodo ciclo() {
		Nodo ciclo = new Nodo("ciclo");
		verificar("CICLO");
		verificar("PARENTECIS_A");
		ciclo.agregarHijo(operacionAritmetica());
		verificar("PARENTECIS_C");
		ciclo.agregarHijo(bloque());
		return ciclo;
	}

	// Regla: condicion -> expresion OPERADOR_LOGICO expresion
	private Nodo condicion() {
		
		Nodo condicion = new Nodo("condicion");
	    condicion.agregarHijo(valor());
	    verificar("OPERADOR_LOGICO");
	    condicion.agregarHijo(valor());
	    
	    if (currentToken.getTipo().equals("CONDICION_LOGICA")) {
	        verificar("CONDICION_LOGICA");
	        condicion.agregarHijo(condicion());
	    }
	    return condicion;
	}

	// Regla: bloque -> LLAVE_A sentencia* LLAVE_C
	private Nodo bloque() {
		Nodo bloque = new Nodo("bloque");
		verificar("LLAVE_A");
		while (!currentToken.getTipo().equals("LLAVE_C")) {
			bloque.agregarHijo(sentencia());
		}
		verificar("LLAVE_C");
		return bloque;
	}

	// Método auxiliar para verificar y avanzar al siguiente token
	private void verificar(String tipo) {
		if (currentToken.getTipo().equals(tipo)) {
			//System.out.println("token: " + currentToken);
			index++;
			if (index < tokens.size()) {
				currentToken = tokens.get(index);
				
			}
			
		} else {
			throw new RuntimeException("Error de sintaxis. Se esperaba '" + tipo + "'");
		}
	}
}
