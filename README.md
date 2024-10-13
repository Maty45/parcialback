INSTRUCCIONES
1. Descargar/clonar repositorio a tu pc
2. Puede ser iniciado en un IDE y luego ser probado con Postman. O simplemente probarlo con postman en las siguientes URL:
   SERVICIO POST -> /mutant/ :  https://parcialback-dcl6.onrender.com/mutant/
   SERVICIO GET -> /stats/ : https://parcialback-dcl6.onrender.com/stats/
3. En el servicio /mutant/ podremos ingresar el adn de la persona en forma de una matriz NxN (unicamente matrices cuadradas), con un arreglo de cadenas.
   Un ejemplo de mutante seria:
   {
    "dna": ["ATCCC","TTTAC","ACATG","CATTT","GGGGA"]
   }
   Un ejemplo de humano seria:
   {
    "dna": ["ATCCC","TTTAC","ACATG","CATTT","GAGGA"]
   }
4. En el servicio /stats/ obtendremos en formato JSON las estadisticas de humanos y mutantes ingresados. Por ejemplo:
   {
    "count_humans": 1,
    "count_mutants": 1,
    "ratio": 1.0
   }
   
ACLARACIONES FINALES:
No puede existir más de una persona con el mismo ADN. Al momento de ingresar una persona obtendremos un mensaje si es o no mutante. 
