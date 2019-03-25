import java.util.List;
import java.util.Objects;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Hashtable;

public class Lexer {
	
	//HashTable para guardar (Lexema, Token) de las palabras reservadas
	public static Hashtable<String, String> tablaDeSimbolos = new Hashtable<String, String>();
	
	//Llenar la tabla de simbolos
	public static void TablaDeSimbolos() {
		tablaDeSimbolos.put("inicio","PALARES");
		tablaDeSimbolos.put("final","PALARES");
		tablaDeSimbolos.put("Si","PALARES");
		tablaDeSimbolos.put("sino","PALARES");
		tablaDeSimbolos.put("finsi","PALARES");
		tablaDeSimbolos.put("Mientras","PALARES");
		tablaDeSimbolos.put("finmientras","PALARES");
		tablaDeSimbolos.put("Escribe","PALARES");
	}
	
	//Tipos de Token que puede hacer en el lenguaje establecido
    public static enum Type {
        PARENIZ, PARENDE, ID, PALARES, DELIM, MAS, MENOS, DIV, MULT, IGUAL, ASIG, MENOR, MAYOR, MENORIG, MAYORIG, NUM, ERROR;
    }
    
    //Clase Token que contiene el tipo de token mas el lexema correspondiente
    public static class Token {
        public final Type token;
        public final String content; 
        
        public Token(Type t, String c) {
            this.token = t;
            this.content = c;
        }
        
        //toString de cada Token, el lexema en la clase es para usarlo realmente cuando es una palabra reservada, identificador o numero
        public String toString() {
            if(token == Type.ID) {
                return "ID, " + content + "\n";
            }
            
            if(token == Type.PALARES) {
                return "PALARES, " + content + "\n";
            }
            
            if(token == Type.NUM) {
                return "NUM, " + content + "\n";
            }
            
            if(token == Type.PARENIZ) {
                return "PARENIZ, (\n";
            }
            
            if(token == Type.PARENDE) {
                return "PARENDE, )\n";
            }
            
            if(token == Type.DELIM) {
                return "DELIM, ;\n";
            }
            
            if(token == Type.MAS) {
                return "MAS, +\n";
            }
            
            if(token == Type.MENOS) {
                return "MENOS, -\n";
            }
            
            if(token == Type.DIV) {
                return "DIV, /\n";
            }
            
            if(token == Type.MULT) {
                return "MULT, *\n";
            }
            
            if(token == Type.IGUAL) {
                return "IGUAL, ==\n";
            }
            
            if(token == Type.ASIG) {
                return "ASIG, =\n";
            }
            
            if(token == Type.MENOR) {
                return "MENOR, <\n";
            }
            
            if(token == Type.MAYOR) {
                return "MAYOR, >\n";
            }
            
            if(token == Type.MENORIG) {
                return "MENORIG, <=\n";
            }
            
            if(token == Type.MAYORIG) {
                return "MAYORIG, >=\n";
            }
            
            if(token == Type.ERROR) {
            	return "ERROR LEXICO\n";
            }
            
            else {
            	return "ERROR LEXICO\n";
            }
            
        }
    }

    //Funcion que coge todas las letras o numeros para formar lo que seria un identificador
    public static String getID(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isLetter(s.charAt(j)) || Character.isDigit(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    
    //Funcion que coge todos los numeros que aparescan corridos en el source code para identificarlos como un numero
    public static String getNumber(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isDigit(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }

    //Funcion que analiza linea por linea el source code para establecer los token correspondientes (basicamente todo el analizador lexico)
    public static void lex(String input) {
    	
    	//Una lista para guardar todos los token por linea de texto que salga del source code
        List<Token> result = new ArrayList<Token>();
        
        //For loop que coge letra-a-letra y busca si hay algun operador binario u otra letra que se pueda identificar sola con un token
        //Si encuentre una letra va al loop de buscar identificador, si encuentra un digito va al de numero, si es un whitespace se lo salta, otra cosa es error
        for(int i = 0; i < input.length(); ) {
        	
            switch(input.charAt(i)) {
            
            case '(':
                result.add(new Token(Type.PARENIZ, "("));
                i++;
                break;
            case ')':
                result.add(new Token(Type.PARENDE, ")"));
                i++;
                break;
            case ';':
                result.add(new Token(Type.DELIM, ";"));
                i++;
                break;
            case '+':
                result.add(new Token(Type.MAS, "+"));
                i++;
                break;
            case '-':
                result.add(new Token(Type.MENOS, "-"));
                i++;
                break;
            case '/':
                result.add(new Token(Type.DIV, "/"));
                i++;
                break;
            case '*':
                result.add(new Token(Type.MULT, "*"));
                i++;
                break;
            case '=':
            	if(input.charAt(i + 1) == '=') {
            		result.add(new Token(Type.IGUAL, "=="));
            		i = i + 2;
            	}
            	
            	else {
            		result.add(new Token(Type.ASIG, "="));
            		i++;
            	}
                break;
            case '<':
            	if(input.charAt(i + 1) == '=') {
            		result.add(new Token(Type.MENORIG, "<="));
            		i = i + 2;
            	}
            	
            	else {
            		result.add(new Token(Type.MENOR, "<"));
            		i++;
            	}
                break;
            case '>':
            	if(input.charAt(i + 1) == '=') {
            		result.add(new Token(Type.MAYORIG, ">="));
            		i = i + 2;
            	}
            	
            	else {
            		result.add(new Token(Type.MAYOR, ">"));
            		i++;
            	}
                break;
                
            default:
                if(Character.isWhitespace(input.charAt(i))) {
                    i++;
                } 
                
                else if(Character.isLetter(input.charAt(i))){
                    String nameID = getID(input, i);
                    i += nameID.length();
                    
                    if(tablaDeSimbolos.containsKey(nameID)) {
                    	if(Objects.equals("PALARES", tablaDeSimbolos.get(nameID))) {
                    		result.add(new Token(Type.PALARES, nameID));
                    	}
                    	else {
                    		if(!nameID.equals(nameID.toLowerCase()) || nameID.length() < 2) {
                    			result.add(new Token(Type.ERROR, nameID));
                    		}
                    		
                    		else {
                    			result.add(new Token(Type.ID, nameID));
                    		}
                    		
                    	}
                    }
                    
                    else {
                    	if(!nameID.equals(nameID.toLowerCase()) || nameID.length() < 2) {
                			result.add(new Token(Type.ERROR, nameID));
                		}
                    	
                    	else {
                    		tablaDeSimbolos.put(nameID, "ID");
                        	result.add(new Token(Type.ID, nameID));
                    	}
                    }
                }
                
                else if(Character.isDigit(input.charAt(i))) {
                	String numberID = getNumber(input, i);
                    i += numberID.length();
                    
                    if(numberID.length() < 2) {
            			result.add(new Token(Type.ERROR, numberID));
            		}
            		
            		else {
            			result.add(new Token(Type.NUM, numberID));
            		}
                }
                
                else {
                	
                	result.add(new Token(Type.ERROR, Character.toString(input.charAt(i))));
                	i++;
                }
                break;
            }
        }
        
        //Escribe cada token y lexema en un txt para poder visualisar o usar mas tarde
        Path path = Paths.get("/Users/robertomarnegron/Documents/GitHub/Lexer/src/programa.txt");
        
        for(Token t: result) {
        	
        	try {
				Files.write(path, t.toString().getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				System.out.println("No file found to write output.\n");
				System.exit(0);
			}
        }
        
    }

    public static void main(String[] args) {
    	
    	List<String> result = null;
    	int index = 1;
    	
    	TablaDeSimbolos();

    	//Abre el archivo (source code)
		try {

			result = Files.readAllLines(Paths.get("/Users/robertomarnegron/Documents/GitHub/Lexer/src/programa.txt"));
			
		} catch(IOException e) {
			
			System.out.println("No file found.\n");
			System.exit(0);
		}
		
		//Abre y borra el archivo de salida para luego escribir el resultado
		try {
			
			new PrintWriter("/Users/robertomarnegron/Documents/GitHub/Lexer/src/programa.txt").close();
			
		} catch (FileNotFoundException e) {
			
			System.out.println("No file found to write output.\n");
			System.exit(0);
		}
		
		//Envia linea por linea al analisador lexico 
		for(String line: result) {
			String lineOfCodeTXT = "#" + index + " " + line + "\n";
			
			//Escribe el numero de linea y la linea del source code sin aun identificar tokens
			try {
				Files.write(Paths.get("/Users/robertomarnegron/Documents/GitHub/Lexer/src/programa.txt"), lineOfCodeTXT.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				System.out.println("No file found to write output.\n");
				System.exit(0);
			}
			lex(line);
			index++;
		}
		
    }
}