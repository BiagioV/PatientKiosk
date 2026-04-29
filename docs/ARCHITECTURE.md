# Architettura - PatientKiosk

Questo documento descrive l'architettura dell'applicazione Android **PatientKiosk**.

Il progetto è sviluppato interamente in Kotlin e segue una struttura semplice ispirata al pattern **MVVM**.

## Obiettivo architetturale

L'obiettivo principale è separare:

- interfaccia utente;
- gestione dello stato;
- caricamento dei dati;
- modello dei questionari;
- calcolo degli score clinici.

Questa separazione rende il progetto più ordinato, leggibile, modulare e facile da estendere.

## Struttura principale

```text
app/src/main/java/it/uninsubria/patientkiosk/
├── MainActivity.kt
├── data/
│   └── QuestionnaireRepository.kt
├── model/
│   └── Questionnaire.kt
├── scoring/
│   ├── ScoreCalculator.kt
│   └── ScoreResult.kt
├── ui/
│   └── QuestionnaireAdapter.kt
├── validation/
│   └── PatientCodeValidator.kt
└── viewmodel/
    ├── KioskUiState.kt
    └── KioskViewModel.kt
```

## MainActivity

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/MainActivity.kt
```

`MainActivity` è responsabile della gestione dell'interfaccia utente.

Si occupa di:

- collegare i componenti XML tramite `findViewById`;
- ascoltare i click dei pulsanti;
- mostrare o nascondere le sezioni della schermata;
- visualizzare errori;
- aggiornare domande, risposte, progress bar e risultato finale;
- osservare lo stato esposto dal `KioskViewModel`.

La `MainActivity` non contiene direttamente la logica clinica di calcolo, che è invece delegata a `ScoreCalculator`.

## KioskViewModel

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/viewmodel/KioskViewModel.kt
```

`KioskViewModel` gestisce lo stato applicativo.

Contiene informazioni come:

- codice paziente;
- questionario selezionato;
- domanda corrente;
- risposte già date;
- risultato finale.

Espone lo stato tramite `LiveData<KioskUiState>`.

Questo permette alla UI di aggiornarsi in base allo stato corrente, senza mescolare troppo la logica applicativa con il layout.

## KioskUiState

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/viewmodel/KioskUiState.kt
```

`KioskUiState` rappresenta le possibili schermate o condizioni dell'app.

Gli stati principali sono:

- identificazione paziente;
- selezione questionario;
- compilazione domanda;
- risultato finale;
- errore.

Questa struttura rende il flusso più chiaro e controllato.

## QuestionnaireRepository

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/data/QuestionnaireRepository.kt
```

`QuestionnaireRepository` si occupa del caricamento dei questionari dal file JSON.

File dati:

```text
app/src/main/assets/questionnaires.json
```

Il repository:

- legge il file JSON;
- converte i dati in oggetti Kotlin;
- valida la struttura dei questionari;
- controlla che i dati principali siano presenti;
- segnala errori se il file è incompleto o non valido.

Questa classe separa il contenuto dei questionari dalla logica dell'app.

## Modello dati

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/model/Questionnaire.kt
```

Il modello dati rappresenta la struttura dei questionari clinici.

Classi principali:

- `Questionnaire`;
- `Question`;
- `AnswerOption`;
- `ScoreBand`;
- `ScoringType`.

### Questionnaire

Rappresenta un questionario completo.

Contiene:

- id;
- titolo;
- descrizione;
- fonte;
- tipo di scoring;
- punteggio massimo;
- lista domande;
- fasce di interpretazione.

### Question

Rappresenta una singola domanda.

Contiene:

- id;
- testo;
- eventuale categoria;
- risposte disponibili.

La categoria è utile per questionari come HADS, dove esistono sottoscale diverse.

### AnswerOption

Rappresenta una risposta possibile.

Contiene:

- id;
- etichetta mostrata all'utente;
- punteggio associato.

### ScoreBand

Rappresenta una fascia di interpretazione clinica.

Contiene:

- categoria opzionale;
- punteggio minimo;
- punteggio massimo;
- etichetta;
- descrizione.

### ScoringType

Indica il tipo di calcolo da applicare.

Tipi attualmente previsti:

- `SUM`;
- `WHO5_PERCENTAGE`;
- `HADS_SUBSCALES`.

## ScoreCalculator

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreCalculator.kt
```

`ScoreCalculator` contiene la logica di calcolo degli score.

È separato dalla UI per migliorare:

- leggibilità;
- manutenibilità;
- controllo della correttezza;
- possibilità di test futuri.

Gestisce:

- somma semplice dei punteggi;
- calcolo percentuale WHO-5;
- sottoscale HADS per ansia e depressione;
- validazione delle risposte;
- controllo del range dello score.

## ScoreResult

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreResult.kt
```

`ScoreResult` rappresenta il risultato finale del questionario.

Contiene:

- punteggio principale;
- punteggio massimo;
- etichetta interpretativa;
- descrizione;
- dettagli aggiuntivi.

La schermata risultato usa questo oggetto per mostrare un output leggibile.

## PatientCodeValidator

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/validation/PatientCodeValidator.kt
```

`PatientCodeValidator` controlla il codice paziente inserito nella prima schermata.

Regole principali:

- il codice non può essere vuoto;
- deve avere almeno 3 caratteri;
- non deve superare 20 caratteri;
- può contenere lettere, numeri, trattino e underscore;
- non deve contenere nome, cognome o dati personali.

Questa validazione aiuta a ridurre errori di inserimento e tutela la privacy.

## QuestionnaireAdapter

File:

```text
app/src/main/java/it/uninsubria/patientkiosk/ui/QuestionnaireAdapter.kt
```

`QuestionnaireAdapter` gestisce la visualizzazione della lista dei questionari nella `ListView`.

Mostra:

- titolo questionario;
- descrizione;
- numero domande;
- punteggio massimo;
- invito all'azione.

## Layout XML

Cartella:

```text
app/src/main/res/layout/
```

Layout principali:

```text
activity_main.xml
item_questionnaire.xml
```

`activity_main.xml` contiene le sezioni principali dell'app:

- identificazione paziente;
- selezione questionario;
- compilazione domanda;
- risultato finale.

Le sezioni vengono mostrate o nascoste dalla `MainActivity` in base allo stato corrente.

## Risorse

Cartella:

```text
app/src/main/res/values/
```

File principali:

```text
strings.xml
colors.xml
dimens.xml
```

Le stringhe sono centralizzate in `strings.xml`, così i testi dell'app non sono scritti direttamente nel codice Kotlin o nei layout.

## Flusso applicativo

```text
Avvio app
   ↓
Caricamento questionari da JSON
   ↓
Inserimento codice paziente
   ↓
Validazione codice paziente
   ↓
Selezione questionario
   ↓
Compilazione domande
   ↓
Salvataggio risposte nello stato
   ↓
Calcolo score
   ↓
Visualizzazione risultato
```

## Modularità

La modularità è ottenuta separando i questionari dal codice.

I questionari sono definiti nel file:

```text
app/src/main/assets/questionnaires.json
```

Per aggiungere un nuovo questionario, in linea generale è necessario:

1. aggiungere una nuova voce nel JSON;
2. indicare domande e risposte;
3. indicare il tipo di scoring;
4. indicare le fasce di interpretazione.

Se il nuovo questionario usa un tipo di scoring già supportato, non è necessario modificare la UI principale.

## Gestione degli errori

L'app gestisce errori come:

- codice paziente vuoto o non valido;
- risposta non selezionata;
- JSON mancante o non valido;
- questionario incompleto;
- calcolo impossibile;
- punteggio fuori range.

I messaggi mostrati all'utente sono pensati per essere semplici e comprensibili.

## Limiti attuali

Il progetto è didattico e presenta alcuni limiti:

- i dati non vengono salvati in un database;
- non è presente autenticazione;
- non è presente esportazione PDF del risultato;
- non è presente integrazione con sistemi clinici reali;
- i questionari sono demo e non devono essere usati per decisioni cliniche reali.

## Possibili sviluppi futuri

Possibili estensioni:

- salvataggio locale dei risultati;
- esportazione del risultato;
- supporto multilingua;
- caricamento questionari da server;
- modalità tablet dedicata;
- maggiore accessibilità;
- test automatici.