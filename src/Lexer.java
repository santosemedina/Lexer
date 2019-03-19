import java.util.Hashtable;

public class Lexer {
	
	//HashTable para guardar (Lexema, Token) de las palabras reservadas
	public static Hashtable<String, String> tablaDeSimbolos = new Hashtable<String, String>();
	
	//Llenar la tabla de simbolos
	public static void TablaDeSimbolos() {
		tablaDeSimbolos.put("Inicio","TKN_palabraReservada");
		tablaDeSimbolos.put("final","TKN_palabraReservada");
		tablaDeSimbolos.put("Si","TKN_palabraReservada");
		tablaDeSimbolos.put("sino","TKN_palabraReservada");
		tablaDeSimbolos.put("finSi","TKN_palabraReservada");
		tablaDeSimbolos.put("Mientras","TKN_palabraReservada");
		tablaDeSimbolos.put("finmientras","TKN_palabraReservada");
		tablaDeSimbolos.put("Escribe","TKN_palabraReservada");	
	}
	
	//EsPalabraReservada verifica que sea una palabra reservada que esta en la tabla
	public static boolean EsPalabraReservada(String palabra) {
		if(tablaDeSimbolos.containsKey(palabra)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//PalabraReservada devuelve el token y la palabra
	public static String PalabraReservada(String palabra) {
		if(EsPalabraReservada(palabra)) {
			return tablaDeSimbolos.get(palabra)+", "+palabra;
		}
		return "Error";
	}
	
	//EsParentesis (   ) Verifica si el character es un parentesis.
	public static boolean EsParentesis(char[] caracteres, int indice) {
		if((caracteres[indice]=='(') || (caracteres[indice]==')'))
		{
			return true;
		}
		return false;
	}
	
	//Parentesis Devuelve el token del tipo de parentesis
	public static String Parentesis(char[] caracteres, int indice) {
		if(EsParentesis(caracteres, indice)) {
			if(caracteres[indice]=='(') {
				return "TKN_parentesisIzq, )";
			}
			else if(caracteres[indice]==')') {
				return "TKN_parentesisDer , )";
			}
		}
		return "Error";
	}
	
	
	//EsOperadorBinario verifica si el caracter es operador binario ej. +
	public static boolean EsOperadorBinario(char[] caracteres, int indice) {
		if(caracteres[indice]=='+' || caracteres[indice]=='-'|| caracteres[indice]=='*'|| caracteres[indice]=='/'|| caracteres[indice]=='<'|| caracteres[indice]=='>') {
			return true;
		}
		
		//Maneja la excepcion si es un = seguido por =,<,>
		else if(EsOperadorCompuesto(caracteres,indice)) {
			return true;
		}
		return false;
	}
	
	//EsOperadorCompuesto Revisa Si es un operador de mas de un caracter para asi procesarlo como uno
	public static boolean EsOperadorCompuesto(char[] caracteres, int indice) {
		if((caracteres[indice]=='<') ||(caracteres[indice]=='>') || (caracteres[indice]=='=') && (caracteres[indice+1] == '=')){
			return true;
		}
		return false;
	}
	
	/*
	 * OperadorBinario revisa primero si es un operador compuesto y lo identifica 
	 *                 sino busca si es un operador binario simple y lo identifica.
	 */
	public static String OperadorBinario(char[] caracteres, int indice) {
		
		if(EsOperadorCompuesto(caracteres, indice)) {
			
			String operadorCompuesto=String.valueOf(caracteres[indice]) + String.valueOf(caracteres[indice+1]);
			
			switch(operadorCompuesto) {
				
				case "<=":
					return "TKN_opBinario , MEI";
				
				case ">=":
					return "TKN_opBinario , MAI";
				
				case "==":
					return "TKN_opBinario , IGU";
					
				default:
					return "Error";
			}
		}
		else {
			switch(caracteres[indice]) {
			
				case '+':
					return "TKN_opBinario , SUM";
				
				case '-':
					return "TKN_opBinario , RES";
				
				case '*':
					return "TKN_opBinario , MUL";
				
				case '/':
					return "TKN_opBinario , DIV";
				
				case '>':
					return "TKN_opBinario , MAY";
				
				case '<':
					return "TKN_opBinario , MEN";
					
				default:
					return "Error";
			}
		}
	}
	
	public static void main(String[] args)
	{
		TablaDeSimbolos();
		
		String test = "+";
		char[] array = test.toCharArray();
		
		for(int i=0;i<array.length;i++) {
			if(EsOperadorBinario(array,i)){
				System.out.println(OperadorBinario(array,i));
				if(EsOperadorCompuesto(array,i)) {
					//Es importante que si es un operador compuesto se debe sumar 1 al apuntador 
					//ya que va a leer ambos como un solo operador binario.
					i++;
				}
			}
		}
	}
}
