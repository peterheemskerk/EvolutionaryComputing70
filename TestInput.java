// eerste java programmaatje

public class TestInput
{
	private String testTekst;

	public void setTekst( String tekst )
	{
		testTekst = tekst;
	}

	public String getTekst()
	{
		return "Dit is resultaat: " + testTekst;
	}

	public Integer aantalTekens( String tekst)
	{
		String myString = new String(tekst);
		int aantal;
		aantal = myString.length();
		return aantal;
	}
	
	public void displayMessage( String tekstje )	
	{
		System.out.println( tekstje );
		System.out.printf("lengte van tekst: %d", aantalTekens( tekstje ));

		System.out.println();
	}
}
