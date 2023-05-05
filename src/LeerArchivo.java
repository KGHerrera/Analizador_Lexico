import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LeerArchivo {

	public String leerContenidoArchivo(String rutaArchivo) throws IOException {
		File archivo = new File(rutaArchivo);
		FileReader lector = new FileReader(archivo);
		BufferedReader buffer = new BufferedReader(lector);

		String linea;
		StringBuilder contenidoArchivo = new StringBuilder();

		while ((linea = buffer.readLine()) != null) {
			contenidoArchivo.append(linea).append(" ");
		}

		buffer.close();
		lector.close();

		return contenidoArchivo.toString();
	}
}