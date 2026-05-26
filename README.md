Markdown

# Proiect ISS - Teledon

Aplicatie web dezvoltata pentru laboratorul de Ingineria Sistemelor Software (ISS). Sistemul gestioneaza campanii caritabile, donatori si donatii, oferind actualizari in timp real prin WebSockets.

## Tehnologii folosite
* **Frontend:** React.js
* **Backend:** Java, Spring Boot
* **Baza de date:** SQLite (mapata cu Hibernate ORM)
* **Comunicare:** REST API, WebSockets

## Cum se ruleaza proiectul

### 1. Backend (Java / Spring Boot)
* Deschide folderul de backend al proiectului in IntelliJ IDEA (sau alt IDE preferat).
* Asteapta ca Maven/Gradle sa descarce dependentele necesare.
* Ruleaza aplicatia din clasa principala (cea care contine metoda `StartRestServices`).
* Serverul va porni automat pe portul `8080`. Baza de date SQLite (`teledon.db`) este inclusa direct in proiect, deci nu este nevoie de o configurare separata a bazei de date.

### 2. Frontend (React)
* Deschide un terminal (Command Prompt / terminalul din VS Code) in folderul aplicatiei de frontend.
* Ruleaza comanda de mai jos pentru a instala modulele Node.js necesare (se face o singura data la prima descarcare a proiectului):
  ```bash
  npm install

    Porneste serverul de dezvoltare pentru interfata cu comanda:
    Bash

    npm run dev

    Deschide in browser link-ul afisat in terminal 

3. Date de testare (Login)

Baza de date vine preconfigurata cu conturi de voluntari. Pentru a te conecta in aplicatie si a avea acces la functionalitatile de adaugare cazuri si donatii, poti folosi unul dintre urmatoarele conturi:

Cont 1:

    Username: admin

    Parola: admin123

Cont 2:

    Username: test

    Parola: 1234
