DOKUMENTAATIO

INTRO:

Tästä harkkatyöstä tulikin loppujen lopuksi aika erilaisen näkönen verrattuna siihen mitä suunnittelin, mutta ei kai siinä mitään. Ohjelmalla
pystyy nyt siis hakemaan erilaista Suomen kuntiin liittyvää dataa. Eli siis käyttäjä pystyy hakemaan kunnan nimen ja sitten ohjelma näyttää eri-
laista dataa käyttäjälle. Ohjelmalla pystyy myös vertailemaan samoja datoja jonkun muun kunnan kanssa.

Lisää ohjelman ominaisuuksista ja sielunelämästä syvemmällä

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
![image](https://github.com/PANAANI/Olio-Ohjelmointi-Harkkatyo/assets/127149891/0646e7f0-b730-4c47-aee2-4b8df89cedf8)


