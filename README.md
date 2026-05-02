# PatientKiosk

**PatientKiosk** è un'applicazione Android nativa sviluppata interamente in **Kotlin** per la compilazione guidata di questionari clinici da parte di pazienti in ambito dermatologico.

Il progetto è stato realizzato per il corso di **Programmazione di Dispositivi Mobili** dell'Università degli Studi dell'Insubria.

## Obiettivo del progetto

L'app simula un tablet/kiosk utilizzabile in sala d'attesa o durante una visita medica.

Il paziente può:

1. inserire un codice identificativo;
2. selezionare un questionario clinico;
3. compilare le domande in modo guidato;
4. ottenere automaticamente il punteggio;
5. visualizzare una semplice interpretazione del risultato.

## Focus del progetto

Il progetto è stato sviluppato ponendo attenzione a:

- usabilità clinica;
- correttezza dei calcoli;
- robustezza dell'interfaccia;
- gestione degli errori;
- modularità dei questionari;
- separazione tra contenuto clinico e logica software.

## Tecnologie utilizzate

- Kotlin
- Android Studio
- Android SDK
- XML Layout
- ViewModel
- LiveData
- ListView
- Adapter personalizzato
- JSON locale in `assets`
- Git e GitHub

## Questionari disponibili

L'app include tre questionari dimostrativi caricati da file JSON:

- DLQI demo;
- WHO-5 demo;
- HADS demo.

I testi presenti nel progetto sono versioni didattiche/parafrasate.  
Per un utilizzo reale sarebbe necessario utilizzare le versioni ufficiali e autorizzate dei questionari clinici.

## Flusso applicativo

```text
Identificazione paziente
        ↓
Selezione questionario
        ↓
Compilazione domande
        ↓
Calcolo score
        ↓
Visualizzazione risultato
```

## Funzionalità implementate

### Identificazione paziente

La prima schermata permette l'inserimento di un codice paziente.

Sono gestiti i seguenti controlli:

- codice vuoto;
- codice troppo corto;
- caratteri non validi;
- invito a non inserire nome, cognome o dati personali.

La validazione è gestita nel file:

```text
app/src/main/java/it/uninsubria/patientkiosk/validation/PatientCodeValidator.kt
```

### Selezione questionario

Dopo l'inserimento del codice paziente, l'app mostra la lista dei questionari disponibili.

Per ogni questionario vengono mostrati:

- nome;
- descrizione;
- numero di domande;
- punteggio massimo;
- invito alla compilazione.

La lista è gestita tramite `ListView` e adapter personalizzato.

File principale:

```text
app/src/main/java/it/uninsubria/patientkiosk/ui/QuestionnaireAdapter.kt
```

### Compilazione guidata

Durante la compilazione:

- viene mostrata una domanda alla volta;
- viene visualizzato l'avanzamento;
- il pulsante avanti è disabilitato se non viene selezionata una risposta;
- è possibile tornare alla domanda precedente;
- la risposta già selezionata viene mantenuta;
- dalla prima domanda è possibile tornare alla selezione del questionario.

### Calcolo automatico dello score

La logica di calcolo è separata dall'interfaccia utente.

File principale:

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreCalculator.kt
```

Tipi di calcolo supportati:

- `SUM`;
- `WHO5_PERCENTAGE`;
- `HADS_SUBSCALES`.

Esempi:

- DLQI demo: somma dei punteggi;
- WHO-5 demo: punteggio grezzo moltiplicato per 4;
- HADS demo: calcolo separato delle sottoscale ansia e depressione.

### Schermata risultato

Al termine della compilazione vengono mostrati:

- codice paziente;
- questionario compilato;
- data e ora della compilazione;
- punteggio calcolato;
- interpretazione;
- dettagli;
- disclaimer didattico.

## Modularità

I questionari sono definiti nel file:

```text
app/src/main/assets/questionnaires.json
```

Ogni questionario contiene:

- id;
- titolo;
- descrizione;
- fonte;
- tipo di scoring;
- punteggio massimo;
- domande;
- risposte possibili;
- punteggio associato alle risposte;
- fasce di interpretazione.

Questa struttura permette di aggiungere nuovi questionari modificando principalmente il file JSON, senza riscrivere la logica principale dell'app.

## Gestione errori

L'app gestisce diversi casi di errore:

- codice paziente non valido;
- risposta non selezionata;
- errore nel caricamento del JSON;
- struttura del questionario incompleta;
- punteggio massimo non coerente;
- risposte mancanti;
- punteggio calcolato fuori intervallo.

L'obiettivo è evitare crash e mostrare messaggi comprensibili per l'utente.

## Struttura del progetto

```text
PatientKiosk/
├── app/
│   └── src/
│       └── main/
│           ├── assets/
│           │   └── questionnaires.json
│           ├── java/it/uninsubria/patientkiosk/
│           │   ├── MainActivity.kt
│           │   ├── data/
│           │   │   └── QuestionnaireRepository.kt
│           │   ├── model/
│           │   │   └── Questionnaire.kt
│           │   ├── scoring/
│           │   │   ├── ScoreCalculator.kt
│           │   │   └── ScoreResult.kt
│           │   ├── ui/
│           │   │   └── QuestionnaireAdapter.kt
│           │   ├── validation/
│           │   │   └── PatientCodeValidator.kt
│           │   └── viewmodel/
│           │       ├── KioskUiState.kt
│           │       └── KioskViewModel.kt
│           └── res/
│               ├── layout/
│               ├── values/
│               └── drawable/
├── docs/
│   ├── ARCHITECTURE.md
│   ├── EXAM_REQUIREMENTS.md
│   ├── SOURCES.md
│   └── TESTING.md
├── README.md
└── .gitignore
```

## File principali

### UI principale

```text
app/src/main/java/it/uninsubria/patientkiosk/MainActivity.kt
```

Gestisce la visualizzazione delle schermate e l'interazione con l'utente.

### ViewModel

```text
app/src/main/java/it/uninsubria/patientkiosk/viewmodel/KioskViewModel.kt
```

Gestisce lo stato dell'applicazione, il questionario selezionato, la domanda corrente e le risposte date.

### Repository

```text
app/src/main/java/it/uninsubria/patientkiosk/data/QuestionnaireRepository.kt
```

Carica e valida i questionari dal file JSON.

### Modello dati

```text
app/src/main/java/it/uninsubria/patientkiosk/model/Questionnaire.kt
```

Definisce la struttura dei questionari, delle domande, delle risposte e delle fasce di interpretazione.

### Calcolo score

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreCalculator.kt
```

Contiene la logica di calcolo dei punteggi.

## Documentazione

La documentazione aggiuntiva si trova nella cartella:

```text
docs/
```

File principali:

- `EXAM_REQUIREMENTS.md`: descrive come il progetto soddisfa i requisiti dell'esame;
- `TESTING.md`: contiene i test manuali eseguiti;
- `ARCHITECTURE.md`: descrive l'architettura del progetto;
- `SOURCES.md`: contiene riferimenti e fonti.

## Come aprire il progetto

1. Aprire Android Studio.
2. Selezionare `Open`.
3. Aprire la cartella `PatientKiosk`.
4. Attendere il completamento del Gradle Sync.
5. Avviare un emulatore Android o collegare un dispositivo fisico.
6. Premere `Run`.

## Come eseguire l'app

Dopo l'avvio:

1. inserire un codice paziente, ad esempio:

```text
PZ001
```

2. selezionare un questionario;
3. rispondere alle domande;
4. visualizzare il risultato finale.

## Esempi di test

### DLQI demo

Tutte risposte con punteggio `0`:

```text
0 / 30
Nessun impatto
```

### WHO-5 demo

Tutte risposte con punteggio `5`:

```text
100 / 100
Benessere adeguato
```

### HADS demo

Tutte risposte con punteggio `0`:

```text
0 / 42
Ansia: Normale · Depressione: Normale
```

## Gestione Git

Il progetto è gestito tramite Git e pubblicato su GitHub.

Repository:

```text
https://github.com/BiagioV/PatientKiosk
```

Durante lo sviluppo sono stati effettuati commit progressivi per documentare l'evoluzione del progetto.

Comandi principali:

```bash
git status
git add .
git commit -m "Descrizione modifica"
git push
```

## Limiti noti

Il progetto è didattico e presenta alcuni limiti:

- i risultati non vengono salvati in un database;
- non è presente autenticazione;
- non è presente esportazione PDF;
- non è presente integrazione con sistemi clinici reali;
- i questionari sono demo/parafrasati;
- l'app non deve essere usata per diagnosi o decisioni cliniche reali.

## Possibili sviluppi futuri

Possibili miglioramenti:

- salvataggio locale dei risultati;
- esportazione dei risultati;
- supporto multilingua;
- caricamento questionari da server;
- interfaccia tablet più avanzata;
- test automatici;
- maggiore accessibilità.

## Disclaimer

PatientKiosk è un progetto didattico.

Non è un dispositivo medico e non deve essere utilizzato per diagnosi, terapia o decisioni cliniche reali.