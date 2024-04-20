DOKUMENTAATIO

INTRO:

Tästä harkkatyöstä tulikin loppujen lopuksi aika erilaisen näkönen verrattuna siihen mitä suunnittelin, mutta ei kai siinä mitään. Ohjelmalla
pystyy nyt siis hakemaan erilaista Suomen kuntiin liittyvää dataa. Eli siis käyttäjä pystyy hakemaan kunnan nimen ja sitten ohjelma näyttää eri-
laista dataa käyttäjälle. Ohjelmalla pystyy myös vertailemaan samoja datoja jonkun muun kunnan kanssa.

Lisää ohjelman ominaisuuksista ja sielunelämästä syvemmällä dokumentaatiossa.

PAKETIT:

Ohjelmassa käytin kahta kirjastoa:

Jackson: https://github.com/FasterXML/jackson
AnyChart: https://github.com/AnyChart/AnyChart-Android

VAATIMUKSET:

Tässä tarkempi lista vaatimuksista, eli siis tässä on lista siitä mitä pisteitä haluan:

1. RecycleView 3p, RecycleView:tä käytetään esittämään käyttäjän hakuhistoriaa alkunäkymässä.
2. Datassa käytetään myös kuvia 2p, Säädatan mukana on myös kiva kuva
3. Datalähteitä enemmän kuin yksi 3p, Ohjelma hakee tilastokeskukselta ja openweather:iltä dataa
4. Tilastot 2p, Ohjelma näyttää käyttäjälle tämän hakuhistorian
5. Kuntien vertailu 3p, Kuntia pystyy vertailemaan toistensa kanssa
6. Fragmentit 4p, Fragmenttejä käytetään datan esittämiseen / kuntien vertailuun
7. Datan visualisointi 5p, Kunnan poliittinen jakauma esitetään ympyräkaaviolla AnyChart kirjaston avulla
8. Kuvat haetaan netistä 1p, Säädatassa esitetyt kuvat haetaan openweather:iltä

Eli siis tästä saisi noin 23 pistettä yhteensä pakollisten pisteiden lisäksi (jos siis kaikki menee hyväksytysti läpi D:)

SITTEN VIELÄ NIISTÄ LUOKISTA ETTÄ MITÄ NE TEKEE:

Luokkakaavio

![image](https://github.com/PANAANI/Olio-Ohjelmointi-Harkkatyo/assets/127149891/9d4b8366-6abf-4410-adec-af94425b42e8)

MunicipalityData, PopulationData ja WeatherData:

MunicipalityData:n tehtävänä on toimia konttina Population-, Weather- ja PoliticalData:lle.

Population- ja WeatherData ovat luokkia jotka sisältää niihin liittyvät datat (katso UML tarkempaa määritelmää siitä mitä näissä säilötään)

PoliticalData on Hashmap:pi joka koostuu puoluenimistä ja niiden saamista äänimääristä.

DataStorage:

DataStorage on singleton, jotta siihen saa helposti referenssin mistä päin tahansa ohjelmaa.

DataStorage:n tehtäviin kuuluu MunicipalityData instanssien säilöminen ohjelman ajon aikana ja kuntahakujen säilöminen ja lukeminen/kirjoittaminen tiedostoon.

DataStorage tallentaa aina viisi viimeisintä hakusanaa mitä käyttäjä hakee.

DataRetriever:

Tämän ohjelman monimutkaisin rakennelma. Yleisesti ottaen DataRetrieverin tehtävä on kysellä tarvittavat datat OpenWeatherin ja Tilastokeskuksen API:eista fetchDataOpenWeather() ja fetchDataStatisticsFinland() metodien avulla.
DataRetriever myös rakentaa näistä datoista instanssit Population-, Political- ja WeatherDatasta ja asettaa ne MunicipalityData luokan instanssiin.

Vielä erikseen tähän maininta tuosta loadWeatherImage() metodista. WeatherData tarjoaa käyttöön erilaisia kuvia eri sääolosuhteiden kuvaamiseen, jotka pystyy kivasti saamaan netistä API-kyselyn kautta.
loadWeatherImage() siis periaatteessa vaan käy netistä hakemassa OpenWeatheriltä sopivan kuvan ja asettaa sen BitMap arvoon, jonka pystyy sitten asettamaan näytölle ImageView:iin.
