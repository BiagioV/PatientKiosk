# PatientKiosk

**PatientKiosk** è un'applicazione Android nativa sviluppata in Kotlin per la compilazione guidata di questionari clinici da parte di pazienti in ambito dermatologico.

Il progetto è stato realizzato per il corso di **Programmazione di Dispositivi Mobili** dell'Università degli Studi dell'Insubria.

## Obiettivo del progetto

L'app simula un kiosk/tablet utilizzabile in sala d'attesa o durante la visita medica.

Il paziente può:

1. inserire un codice identificativo;
2. scegliere un questionario clinico;
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
- Architettura semplice ispirata a MVVM

## Questionari disponibili

L'app include questionari dimostrativi caricati da file JSON:

- DLQI demo
- WHO-5 demo
- HADS demo

I testi presenti nel progetto sono versioni didattiche/parafrasate. Per un utilizzo reale sarebbe necessario utilizzare le versioni ufficiali e autorizzate dei questionari clinici.

## Flusso applicativo

```text
Codice paziente
        ↓
Selezione questionario
        ↓
Compilazione domande
        ↓
Calcolo score
        ↓
Risultato finale
```

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
│           │   └── viewmodel/
│           │       ├── KioskUiState.kt
│           │       └── KioskViewModel.kt
│           └── res/
│               ├── layout/
│               ├── values/
│               └── drawable/
├── docs/
│   ├── TESTING.md
│   ├── SOURCES.md
│   └── EXAM_REQUIREMENTS.md
├── README.md
└── .gitignore
```

## Modularità

I questionari sono definiti nel file:

```text
app/src/main/assets/questionnaires.json
```

Ogni questionario contiene:

- identificativo;
- nome;
- descrizione;
- domande;
- risposte possibili;
- punteggio associato alle risposte;
- tipo di calcolo;
- interpretazioni del risultato.

Questa struttura permette di aggiungere nuovi questionari modificando principalmente il file JSON, senza riscrivere la logica principale dell'app.

## Calcolo degli score

La logica di calcolo è separata dalla UI ed è contenuta in:

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreCalculator.kt
```

Questo rende il progetto più ordinato, testabile e manutenibile.

## Come aprire il progetto

1. Aprire Android Studio.
2. Selezionare `Open`.
3. Aprire la cartella `PatientKiosk`.
4. Attendere il completamento del Gradle Sync.
5. Avviare un emulatore Android o collegare un dispositivo fisico.
6. Premere `Run`.

## Come eseguire l'app

Dopo l'avvio:

1. inserire un codice paziente, ad esempio `PZ001`;
2. selezionare un questionario;
3. rispondere alle domande;
4. visualizzare il risultato finale.

## Gestione Git

Il progetto viene gestito tramite Git e pubblicato su GitHub.

Repository:

```text
https://github.com/BiagioV/PatientKiosk
```

Durante lo sviluppo vengono effettuati commit progressivi per documentare le modifiche realizzate.

## Documentazione

La documentazione aggiuntiva si trova nella cartella:

```text
docs/
```

File principali:

- `TESTING.md`: test manuali e casi di verifica;
- `SOURCES.md`: fonti e riferimenti;
- `EXAM_REQUIREMENTS.md`: requisiti del progetto e loro copertura.

## Disclaimer

PatientKiosk è un progetto didattico.

Non è un dispositivo medico e non deve essere utilizzato per diagnosi, terapia o decisioni cliniche reali.