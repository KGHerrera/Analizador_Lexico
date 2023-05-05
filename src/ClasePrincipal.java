import java.io.IOException;
import java.util.ArrayList;

public class ClasePrincipal {

	public static void main(String[] args) {
		LeerArchivo a1 = new LeerArchivo();
		String codigoFuente = "";

		try {
			codigoFuente = a1.leerContenidoArchivo("src/codigoFuente.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AnalizadorLexico al1 = new AnalizadorLexico();
		ArrayList<String> tokens = al1.analizarTexto(codigoFuente);
		System.out.println(tokens + "\n");

		ArrayList<Token> tokens2 = al1.obtenerTokens(codigoFuente);
		System.out.println(tokens2 + "\n");
			
			
		ArrayList<Token> tokens3 = al1.obtenerTokensSinMatcher(codigoFuente);
		System.out.println(tokens3 + "\n");
		
		ArrayList<Token> tokens4 = al1.obtenerTokensAPie(codigoFuente);
		System.out.println(tokens4 + "\n");
		
		

	}

}
