*Architecture / Data handling / Tradeoffs:
The application works by running a schedular every 12 hours to download the covid data file. It runs on the start of application and after every 12 hours from the start.
The file is downloaded and saved locally. All the analytics is done on this downloaded file.

To run the application simply (mvn clean install). Then just run it as java application.

***************Database**************
Mysql server should be running. Configure the mysql host and dbname in application.properties


Total 7 endpoints are exposed for the analytics. 2 endpoints are about user registration and logged in user.

User Registration takes the user name in post request and returns the token.

*To register a user*
http://localhost:8080/register  (body raw json: {"username":"arif","password":"1234"} ). It return token.
Response: { "arif"}

*To login user and get a JWT token*
http://localhost:8080/authenticate  (body raw json: {"username":"arif","password":"1234"} ). It return token.
Response: { "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzabcdefghijkMSwiaWF0IjoxNTkzODQ2NzgxfQ.sa7iBwMOXgvBrXI_jY1_cvJHVvmzJXjqrptuTWonXORab7z8Boa9OezrCNFTV9Lt2Wkp4o4xzJ6pa1B4dP-iUA"}

Use this token to access all the other endpoints using authentication bearer token.

following are the endpoints with examples

*To get list of users currently accessing the api*
http://localhost:8080/loggedinusers
Response: [ "arif" ]


*All new cases reported today*
http://localhost:8080/analytics/newcases
Response: 119136


*All new cases reported today country wise (sorted by cases reported today descending)*
http://localhost:8080/analytics/sortnewcase
Response: {"us":23586,"brazil":18912,"india":10864,"russia":8971,"chile":6405,"pakistan":4960,"peru":4757,"mexico":3484,"saudiarabia":3045,"bangladesh":2743,"iran":2364,"southafrica":2312,"qatar":1595,"egypt":1467,"colombia":1390,"unitedkingdom":1327,"iraq":1268,"turkey":914,"belarus":879,"oman":866,"sweden":843,"afghanistan":791,"argentina":774,"armenia":766,"kuwait":717,"canada":703,"indonesia":672,"poland":575,"philippines":555,"unitedarabemirates":540,"ukraine":498,"panama":421,"dominicanrepublic":405,"ecuador":392,"singapore":383,"bahrain":380,"france":343,"portugal":342,"southsudan":323,"azerbaijan":314,"cameroon":309,"germany":300,"bolivia":285,"guatemala":263,"haiti":262,"nigeria":253,"spain":240,"netherlands":239,"uzbekistan":237,"nepal":213,"italy":197,"moldova":189,"romania":189,"kazakhstan":183,"coted\u0027ivoire":182,"ghana":176,"honduras":172,"kenya":167,"belgium":154,"congo(kinshasa)":138,"israel":111,"northmacedonia":110,"algeria":104,"mauritania":102,"ethiopia":86,"serbia":82,"elsalvador":81,"senegal":79,"tajikistan":76,"morocco":73,"centralafricanrepublic":64,"venezuela":61,"czechia":61,"costarica":55,"paraguay":45,"somalia":45,"japan":39,"djibouti":38,"korea":38,"kyrgyzstan":33,"malawi":29,"madagascar":26,"denmark":24,"uganda":23,"sierraleone":23,"suriname":22,"srilanka":21,"malaysia":19,"cuba":18,"hungary":18,"ireland":18,"greece":17,"finland":17,"syria":16,"norway":16,"bulgaria":16,"mozambique":15,"albania":14,"liberia":14,"saotomeandprincipe":14,"jordan":13,"caboverde":12,"eswatini":11,"lebanon":11,"bhutan":11,"mali":10,"switzerland":9,"lithuania":9,"estonia":8,"rwanda":8,"westbankandgaza":8,"togo":8,"thailand":8,"australia":6,"china":5,"austria":4,"luxembourg":4,"cyprus":4,"niger":3,"angola":3,"zimbabwe":3,"vietnam":2,"yemen":2,"jamaica":2,"maldives":2,"burma":2,"latvia":2,"malta":2,"saintvincentandthegrenadines":1,"cambodia":1,"chad":1,"georgia":1,"burkinafaso":1,"slovenia":1,"iceland":1,"mszaandam":0,"gambia":0,"guinea":0,"botswana":0,"andorra":0,"saintlucia":0,"mauritius":0,"sanmarino":0,"seychelles":0,"equatorialguinea":0,"laos":0,"nicaragua":0,"newzealand":0,"guinea-bissau":0,"monaco":0,"mongolia":0,"diamondprincess":0,"benin":0,"sudan":0,"sinteustatiusandsaba":0,"papuanewguinea":0,"fiji":0,"libya":0,"antiguaandbarbuda":0,"bosniaandherzegovina":0,"trinidadandtobago":0,"tunisia":0,"bahamas":0,"uruguay":0,"gabon":0,"dominica":0,"kosovo":0,"liechtenstein":0,"burundi":0,"slovakia":0,"tanzania":0,"grenada":0,"eritrea":0,"zambia":0,"namibia":0,"comoros":0,"croatia":0,"taiwan*":0,"timor-leste":0,"guyana":0,"lesotho":0,"saintkittsandnevis":0,"congo(brazzaville)":0,"belize":0,"holysee":0,"montenegro":0,"westernsahara":0,"brunei":0,"barbados":0}


*All new cases reported today in a country*
http://localhost:8080/analytics/incountry?country=pakistan
Response: {"pakistan":4960}


*Top N countries with most reported cases today*
http://localhost:8080/analytics/topncountries?limit=4
Response: {"us":23586,"brazil":18912,"india":10864,"russia":8971}


*All new cases reported since a date in a country (choose whatever format but explain that in readme file)*
http://localhost:8080/analytics/bydateincountry?date=20200601&country=pakistan
Response: {"pakistan":26483}


