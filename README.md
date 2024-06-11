# FTN - Projekat iz predmeta sistemi bazirani na znanju

## Članovi tima

- Branislav Stojković SV64-2020
- Anja Petković SV22-2020

## Pokretanje aplikacije

Predmetni projekat iz predmeta Sistemi bazirani na znanju. Aplikacija se sastoji od backend dela razvijenog u Spring Bootu i frontend dela razvijenog u Reactu.

### Prerequisites

- Java 17
- Maven
- Node.js (uključuje npm)
- MySQL baza podataka

### Instalacija

#### Kloniranje repozitorijuma

```sh
git clone https://github.com/branislavstojkovic70/SBNZ.git
cd SBNZ
```

#### Backend

Navigirajte do backend direktorijuma i instalirajte zavisnosti:

```sh
cd backend
mvn clean install
```

#### Frontend

Navigirajte do frontend direktorijuma i instalirajte zavisnosti:

```sh
cd ../frontend
npm install
```

### Konfiguracija baze podataka

Pre pokretanja backend-a, potrebno je da imate MySQL bazu podataka pokrenutu na vašem računaru. Kreirajte bazu podataka sa sledećim podacima:

- Database name: `your_database_name`
- Username: `your_username`
- Password: `your_password`

Podesite `application.properties` fajl u backend delu projekta da uključuje ove podatke:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Pokretanje aplikacije

#### Backend

Za pokretanje Spring Boot backend-a, koristite:

```sh
mvn spring-boot:run
```

Backend server će se pokrenuti na `http://localhost:8080`.

#### Frontend

Za pokretanje React frontend-a, koristite:

```sh
npm start
```

Frontend server će se pokrenuti na `http://localhost:3000`.