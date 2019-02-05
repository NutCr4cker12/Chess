# Shakkipeli

ShakkiPeli.java sisältää Main luokan.
Aloittaakseen pelin tarvitsee vain käynnistää ShakkiPeli.java tiedosto.

Main luokka ei tee muuta kuin käynnistää tekstikäyttöliittymän ja
kaikki pelin kulku / hallinta / päivittäminen tapahtuu tekstikäyttöliittymä-luokassa.

Mahdollisia muutoksia / lisäyksiä:
-Matti - tilanteen huomioiminen ja sen vaikutukset pelinappien valinta/siirtymämahdollisuuksiin
-Shakkimatin huomioiminen -> pelin loppuminen.
-Useamman pelin tallentaminen samaan teksitiedostoon ja tallennetuista peleistä oikean pelin valitseminen jatkettavaksi.
-Pelin tallentaminen tietokantaan.
-Grafinen käyttöliittymä.

Huomautuksia:
Käynnissä olevan pelin tallentamisen pitäisi luoda uusi .txt tiedosto, jossei sellaista vielä ole, mutta tämän toimivuus olisi hyvä varmistaa myös toisella koneella.
Pelinappula - luokka ja sen aliluokka Nappi on jokseenkin turha, kaikki pelinappulan metodit yms voisi suoraan toteuttaa Nappi luokassa, eikä Nappi luokan oleminen Pelinappulan aliluokkana juuri hyödytä mitään. Tämän voisi konstruoida uusiksi järkevämmin niin, että Nappi luokan sijaan olisi jokaiselle pelinappulan arvolla omat luokat, jotka kukin toteuttavat omat "sallittuSiirto()" -metdoit (voisi hyödyntää myös rajapintaa).
Metodien kommentoinnit ovat vielä jokseenkin vähäiset, mutta metodien nimien pitäisi olla suhteellisen itsestäänselviä, mikä niiden tarkoitus / funktio on.
© 2019 GitHub, Inc.
