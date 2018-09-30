#EvolutionaryComputing70

Some Documentation: 
Programming done in 2 java files: 
- player70.java
- Individual.java

Deze eerste versie heeft alle genetics procedures zeer eenvoudig ingevuld: 

Elk individu heeft een: 
- genome (nu een int getal)
- fenotype (de lijst van 10 double getallen)
- fitness (bepaald door gegeven procedure BRENT SIGAR etc). 

Parents worden nu geselecteerd door alle individuen met fitnes > gemiddelde te nemen. 
Children kunnen door mutatie tot stand komen (mutatie: getal genome met 1 ophogen).
Children kunnen ook door cross over tot stand komen (genome van kind = dat van ouders + 2) 

