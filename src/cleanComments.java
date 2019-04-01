
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class cleanComments {

	// Constantes de inicio de comentario y final de comentario
	public static final String begingCommentFlag = "/*";
	public static final String endCommentFlag = "*/";
	public static final String onLineCommentFlag = "//";
	public static final String endOfLine = "\r\n";
	
	//---------------------------------------------------------------------------------
	//
	// CAMBIAR LOS PARAMETROS DEL PATH Y EL NOMBRE DEL ARCHIVO POR DEFAULT
	//
		public static final String filePath = "/Users/santosmedina/git/Lexer/src/";
		public static final String defaultFile = "programa";
	//
	//---------------------------------------------------------------------------------
	
	
	// Método que limpia el archivo de comentarios
	public static void cleanFile(String fileName, String outputFileName) {		
		String line = "", stringBeforeComment = "", stringAfterComment ="";
		int initComment = -1, endComment = -1;
		
	    try {
			File inputFile = new File(filePath+fileName);
			Scanner scanner = new Scanner(inputFile);
			scanner.useDelimiter(endOfLine);
			
			FileWriter outputFile = new FileWriter(filePath+outputFileName);
			
			boolean readNextLine = true;
			boolean treatingFirstLine = true;
			while (scanner.hasNext()) {
				line = scanner.next();
				
				// Trato la linea para eliminar los comentarios
				do{		
					if ( (line.indexOf(onLineCommentFlag) != -1 ) &&
						 (line.indexOf(endCommentFlag+begingCommentFlag) == -1) ){
						// Linea tiene comentarios de tipo "En La Linea"
						
						// elimino el sub string desde el inicio de comentario hasta el final de la linea
						stringBeforeComment = line.substring(0, line.indexOf(onLineCommentFlag) );	// substring antes del comentario
						stringAfterComment  = "";													// substring despues del comentario
						line = stringBeforeComment + stringAfterComment;						
						readNextLine = false; // continuo procesando la linea, puede haber comentarios de tipo bloque

					}
					else if (initComment == 0) {
						// La linea de comentario empezo en una linea anterior
						
						if( line.indexOf(endCommentFlag) != -1 ) { 
							// Linea tiene un fin de comentario 
							endComment = line.indexOf(endCommentFlag);

							// elimino el sub string desde el principio de la linea hasta hasta el fin de comentario
							stringBeforeComment = ""; 											// substring antes del comentario
							stringAfterComment  = line.substring(endComment+2, line.length());	// substring despues del comentario
							line = stringBeforeComment + stringAfterComment;							
							initComment = -1;
							readNextLine = false; // el resto de la linea pudiese contener más comentarios
							
						} 
						else {
							// Linea no tiene fin de comentario => es todo comentario
							
							// elimino la linea completa
							stringBeforeComment = "";	// substring antes del comentario
							stringAfterComment  = "";	// substring despues del comentario
							line = stringBeforeComment + stringAfterComment;						
							readNextLine = true; // el comentario empeso en anteriormente y la linea no tiene el fin de comentario
						}									
					}
					else if( line.indexOf(begingCommentFlag) != -1 ) { 
						// Si la linea contiene in inicio de comentario y no viene de linease anteriores
						initComment = line.indexOf(begingCommentFlag);
						
						if( line.indexOf(endCommentFlag) != -1 ) { 
							// Linea tiene un comienzo y fin de comentario 
							endComment = line.indexOf(endCommentFlag);

							// elimino el sub string desde el principio del comentario hasta el fin
							stringBeforeComment = line.substring(0, initComment); 				// substring antes del comentario
							stringAfterComment  = line.substring(endComment+2, line.length());	// substring despues del comentario
							line = stringBeforeComment + stringAfterComment;							
							initComment = -1;
							readNextLine = false; // el resto de la linea pudiese contener más comentarios
						} 
						else {
							// Linea tiene un comienzo pero no tiene fin de comentario 
							
							// elimino el sub string desde el principio del comentario hasta el fin
							stringBeforeComment = line.substring(0, initComment); 	// substring antes del comentario
							stringAfterComment  = "";								// substring despues del comentario
							line = stringBeforeComment + stringAfterComment;						
							initComment = 0;
							readNextLine = true; // la linea tiene el inicio de los comentarios pero el final esta en otra linea
						}		
					} 
					else {
						// Linea sin comentarios
						initComment = -1; endComment = -1;
						readNextLine = true;
					}
					
				} while (!readNextLine );
				
				
				if( !line.trim().equals("") ) {
					// Si la linea contiene algo es código por lo tanto lo escribo en el archivo de salida
					if (treatingFirstLine) {
						outputFile.write(line);	
						treatingFirstLine = false;
					}
					else {
						outputFile.write(endOfLine+line);	
					}
									
					initComment = -1; endComment = -1;
				}		
			}
			outputFile.close();
			scanner.close();
		  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public cleanComments() {
		// TODO Auto-generated constructor stub
	}
	//Test de Clean File
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		//int i = args.length;
//		
//		if(args.length == 0) {
//			cleanFile(defaultFile+".txt",defaultFile+".out");
//		}
//		else if(args.length > 0) {
//			cleanFile(args[0]+".txt",args[0]+".out");
//		}
//		else if(args.length > 1) {
//			cleanFile(args[0], args[1]);
//		}
//
//	}

}
