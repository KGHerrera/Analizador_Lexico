import java.io.IOException;
import java.util.ArrayList;

public class ClasePrincipal {
	
	public static void main(String[] args) {
		LeerArchivo a1 = new LeerArchivo();
		String codigoFuente = "";

		try {
			codigoFuente = a1.leerContenidoArchivo("src/codigoFuente.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		AnalizadorLexico al = new AnalizadorLexico();
		
		
		System.out.println("Este es el resultado verdadero:");
		
		ArrayList<Token> tokens = al.obtenerTokensAPie(codigoFuente);
		System.out.println(tokens + "\n");
		
		AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
		
		analizadorSintactico.analizar(tokens);
		
		
		

	}

}
