# Prueba técnica Inditex

## Intoducción

Se trata diseñar en implementar un software modular aplicando principios SOLID y una arquitectura limpia (inspirada en la arquitectura hexagonal).

Se ha implementado mediante un microservicio que por una parte expone una interface REST (API) y por otra tiene la funcionalidad de actualizar los datos de la base de datos. Esta actualización se  produce al arrancar el servicio y, a partir de ahí, todos los días, a medianoche mediante una tarea programada.

La interfaz REST está documentada con `Swagger` y expone tres *endpoints*:

`GET /price` Para consultar el precio de un producto.

`POST /price` Para subir el archivo CSV de precios (no actualiza los precios inmediatamente).

`DELETE /price` Para borrar el archivo CSV de precios (no borra los precios).

Indicar que los precios de los productos solo se actualizan, nunca se borran.

Los nombres de los campos de base de datos se han mantenido como si de una base de datos heredada se tratase, algunos nombres se han renombrado en la clase que representa la entidad para que sean mas explícitos y mantengan la convención de nombres de atributos de Java.

Para la mayoría de  los campos se ha preferido usar el tipo cadena (`String`)  porque, aunque en el ejemplo algunos de ellos se insinúe que puedan ser numéricos, no hay seguridad de que necesariamente lo tengan que ser.

Para los precios se ha usado `BigDecimal` por ser un dato que en ningún caso va a perder precisión, como es el caso de los valores representados en coma flotante (`Float` y `Double`). 

El formato de las fechas se ha usado el mismo que tiene el CSV para devolverlo en la API, aunque fácilmente pueda ser sustituido por otro formato mas amigable.



## Consideraciones de diseño

El microservicio conta de tres módulos, cada uno implementa una de las siguientes capas:

- Infraestructura: `prices-api`, implementa el punto de entrada de la aplicación, la configuración y el controlador.
- Aplicación: `prices-application`, implementa la lógica de negocio y casos de uso.
- Dominio: `prices-domain`, implementa las entidades, *beans*, DTO, repositorios e interfaces de servicio.

### Frameworks y librerías

Se ha implementado un microservicio servicio `Spring Boot` mediante un proyecto `Maven` multi-módulo.

Para la persistencia se ha usado una base de datos en memoria `H2` implementando los repositorios con `Spring Data JPA`.

Para el mapeo de objetos (entidades, *beans* y DTO) se ha usado la librería `MapStruct` .

Para la lectura del archivo CSV se ha usado la librería `OpenCSV`.

Se ha intentado optimizar la carga del archivo CSV realizando la lectura del mismo de forma progresiva usando un *stream* para la lectura, conversión en entidades y persistencia.

La alternativa menos eficiente, en teoría, es cargar todos los objetos en una lista y persistirlos de una sola vez. Esto consumiría mucha mas memoria RAM porque tendría que cargar el contenido del archivo CSV en memoria y crear la lista con todas las entidades también en memoria, lo que hace que el consumo de memoria crezca. 

Para las pruebas se ha usado `Spring MVC testing framework` que permite emular las llamadas al controlador como si de llamadas  HTTP se tratara.

Se ha implementado un test para validar los resultados tal como se especifica en los requisitos y se ha realizado una prueba de rendimiento con una archivo CSV de mas de 200.000 líneas para comparar las dos formas de carga y persistencia de los datos CSV. No se ha encontrado mucha diferencia, quizás para que se llegue a notar la mejora en cuanto del uso de la memoria tiene que tener cargas superiores. Por otra parte las forma de persistencia por pedazos no penaliza demasiado en tiempo.

Se ha descartado una implementación usando técnicas de paralelización porque al ser una única base de datos a la que van dirigidas las peticione se considera que no va a mejorar el rendimiento, incluso lo pudiese empeorar por los bloqueos que se puedan producir en la implementación concurrente.



## Requisitos

En la base de datos de comercio electrónico de la compañía disponemos de la tabla PRICES que refleja el precio final (pvp) y la tarifa que aplica a un producto de una cadena entre unas fechas determinadas. A continuación se muestra un ejemplo de la tabla con los campos relevantes:

 

PRICES

-------


````
BRAND_ID         START_DATE                                    END_DATE                        PRICE_LIST                   PRODUCT_ID  PRIORITY                 PRICE           CURR

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1         2020-06-14-00.00.00                        2020-12-31-23.59.59                        1                        35455                0                        35.50            EUR

1         2020-06-14-15.00.00                        2020-06-14-18.30.00                        2                        35455                1                        25.45            EUR

1         2020-06-15-00.00.00                        2020-06-15-11.00.00                        3                        35455                1                        30.50            EUR

1         2020-06-15-16.00.00                        2020-12-31-23.59.59                        4                        35455                1                        38.95            EUR
````

Campos: 

 

BRAND_ID: foreign key de la cadena del grupo (1 = ZARA).

START_DATE, END_DATE: rango de fechas en el que aplica el precio tarifa indicado.

PRICE_LIST: Identificador de la tarifa de precios aplicable.

PRODUCT_ID: Identificador código de producto.

PRIORITY: Desambiguador de aplicación de precios. Si dos tarifas coinciden en un rago de fechas se aplica la de mayor prioridad (mayor valor numérico).

PRICE: precio final de venta.

CURR: iso de la moneda.

 

Se pide:

 

Construir una aplicación/servicio en SpringBoot que provea una end point rest de consulta  tal que:


Acepte como parámetros de entrada: fecha de aplicación, identificador de producto, identificador de cadena.
Devuelva como datos de salida: identificador de producto, identificador de cadena, tarifa a aplicar, fechas de aplicación y precio final a aplicar.


Se debe utilizar una base de datos en memoria (tipo h2) e inicializar con los datos del ejemplo, (se pueden cambiar el nombre de los campos y añadir otros nuevos si se quiere, elegir el tipo de dato que se considere adecuado para los mismos).

​              

Desarrollar unos test al endpoint rest que validen las siguientes peticiones al servicio con los datos del ejemplo:
                                                                                       

-          Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)

-          Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)

-          Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)

-          Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)

-          Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)

 

 
