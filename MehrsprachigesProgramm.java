
/*
d
  Dieses Programm ist vom Ansatz her ein mehrsprachiges Programm. Es sollte zwischen den Sprachen 
  deutsch, franz�sisch und englisch wechseln k�nnen. Der Programmierer hat sehr schlecht die 
  Internationalisierung des Programms vorbereitet. Schreiben Sie das Programm so um, dass es dynamisch 
  die Sprachen wechseln kann und die prinzipiellen Funktionen erhalten bleiben. 
  
  Schwierigkeiten, die von der Sprache abh�ngen: --> siehe Locale
  1. Datumsformat  --> siehe DateFormat
  2. Zahlformat	--> siehe NumberFormat
  3. W�hrungsformat	--> NumberFormat
  4. Darstellung von Prozenten	--> NumberFormat
  Schwierigkeiten bei der �bersetzung
  1. Pluralformen kein, ein, zwei  --> siehe ChoiceFormat 
  2. S�tze, abh�ngig vom Geschlecht der Person		--> siehe ChoiceFormat
  3. S�tze, die nicht zusammenh�ngend sind --> siehe TextFormat
  
  Die Sprachdateien m�ssen ausgelagert sein. --> siehe ResourceBundle
  
  Es gibt auch Texte, die nicht �bersetzt werden m�ssen, da hier der Programmierer 
  die Programmlogik unwissentlich mit Texten implementiert hat. 
  Im Detail gibt es auch innerhalb oder besonders am Ende der Texte Leerzeichen, die eher etwas 
  mit der Formatierung zu tun haben. 
 
	Abgabe: Quellcode, properties-Dateien, runnable-jar und einer Erkl�rung, 
	wie die jar-erstellt wurde. Ein Shell-Skript reicht auch dazu. 
	Die runnable-jar soll in einem beliebigen Verzeichnis laufen k�nnen und keine 
	anderen Abh�ngigkeiten haben. 
  
 */

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner;

import java.util.ResourceBundle;
import java.util.Locale;
import java.text.NumberFormat;

public class MehrsprachigesProgramm {

	public static void main(String[] args) {
		ResourceBundle bundle = null; 
		System.out.println("deutsch francais english");
		Scanner sc = new Scanner(System.in);
		String sprache = sc.next();

		Locale countryLocale = Locale.GERMANY;
		countryLocale = selectCountryLocale(sprache);

		// if english: Enter decimal point-separated (e.g. 5.3); else: comma-seperated (e.g. 5,3)
		sc.useLocale(countryLocale);
		
		  
		//resource file name: prompt_en.properties, prompt_de.properties, prompt_fr.properties

		String baseName = "prompt";
		bundle = ResourceBundle.getBundle(baseName, countryLocale);

		System.out.println(bundle.getString("InputName") + ": ");
		
		String name = sc.next();
		System.out.println(
			String.format(
				bundle.getString("ImportantDay"), 
				DateFormat.getDateInstance(DateFormat.FULL, countryLocale).format(new Date()).toString(), 
				name
			)
		);

		System.out.println(String.format(bundle.getString("Time"), LocalTime.now()));
		boolean weiter = true;
		do {
			System.out.println(bundle.getString("ProjectDetails"));
			System.out.println(bundle.getString("ProjectManager"));
			String projektLeiter = sc.next();
			System.out.println(
				bundle.getString("IsFemale") 
				+ " [" + bundle.getString("Yes") + ", " + bundle.getString("No") + "]? ");
			String geschlecht = sc.next().equals(bundle.getString("Yes")) ? "weiblich" : "maennlich";
			if (geschlecht.equals("weiblich")) {
				System.out.println(String.format(bundle.getString("NbProjects"), bundle.getString("She")) + " ");
			} else {
				System.out.println(String.format(bundle.getString("NbProjects"), bundle.getString("He")) + " ");
			}
			int anzahlProjekte = sc.nextInt();
			if (geschlecht.equals("maennlich")) {
				System.out.print(bundle.getString("Mister") + " " + projektLeiter + " "); //CHOICE-FORMAT???
			} else {
				System.out.print(bundle.getString("Mrs") + " " + projektLeiter + " ");
			}
			if (anzahlProjekte == 0) {
				System.out.println(
					String.format(bundle.getString("ProjectsDone"), 
									bundle.getString("None"), 
									bundle.getString("Plural")
					)
				);
			} else if (anzahlProjekte == 1) {
				System.out.println(
					String.format(
						bundle.getString("ProjectsDone"),
						bundle.getString("One"),
						bundle.getString("Singular")
					)
				);
			} else {
				System.out.println(
					String.format(
						bundle.getString("ProjectsDone"),
						anzahlProjekte,
						bundle.getString("Plural")
					)
				);
			}
			System.out.println(bundle.getString("Budget"));
			double projektBudget = sc.nextDouble();
			System.out.println(bundle.getString("BudgetSpent"));
			double ausgegeben = sc.nextDouble();
			double prozent = ausgegeben / projektBudget;

			NumberFormat nf1 = NumberFormat.getCurrencyInstance(countryLocale);
			System.out.println(bundle.getString("ProjectBudget") + " : " + nf1.format(projektBudget));
			
			NumberFormat prozentFormat = NumberFormat.getPercentInstance(countryLocale);
			prozentFormat.setMinimumFractionDigits(2);

			System.out.println(
					String.format(
						bundle.getString("BudgetSummary"),
						nf1.format(projektBudget),
						prozentFormat.format(prozent)
					)
			);
					
			System.out.println(name + ", " + bundle.getString("ChangeLanguage"));
			System.out.print(
				String.format(
					bundle.getString("ChangeLanPrompt"), 
					"deutsch, francais, english", 
					bundle.getString("No")
				) + ":\t"
			);
			sprache = sc.next();
			if (sprache.equals(bundle.getString("No"))) {
				weiter = false;
			} else {
				countryLocale = selectCountryLocale(sprache);
				bundle = ResourceBundle.getBundle(baseName, countryLocale);
				sc.useLocale(countryLocale);
			}
		} while (weiter == true);
		sc.close();
		System.out.println(bundle.getString("Bye"));
	}

	private static Locale selectCountryLocale(String sprache) {
		switch(sprache) {
			case "francais":
				return Locale.FRANCE;
			case "english":
				return Locale.UK;
		}
		return Locale.GERMANY;
	}
}
