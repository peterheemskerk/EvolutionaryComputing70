// eerste java programmaatje

import java.util.Scanner;

public class TestPeter
{
	public static void main( String[] args )	
	{
		Scanner input = new Scanner( System.in );
		TestInput myTestInput = new TestInput();
				
		System.out.println( "Hallo allemaal. Typ hier tekstje in:" );
		String textTest = input.nextLine();
		System.out.println();

		myTestInput.setTekst(textTest);
		myTestInput.displayMessage(myTestInput.getTekst());

	}
}
