# Requisiti del progetto PatientKiosk

Questo documento descrive come il progetto **PatientKiosk** soddisfa i requisiti richiesti per l'esame di **Programmazione di Dispositivi Mobili**.

## Sviluppo interamente in Kotlin

Il progetto è sviluppato come applicazione Android nativa in Kotlin.

La logica principale dell'applicazione si trova nei seguenti file:

```text
MainActivity.kt
QuestionnaireRepository.kt
Questionnaire.kt
ScoreCalculator.kt
ScoreResult.kt
QuestionnaireAdapter.kt
KioskViewModel.kt
KioskUiState.kt
```

L'interfaccia è definita tramite layout XML, mentre la logica applicativa, la gestione dello stato, il caricamento dei dati e il calcolo dei punteggi sono implementati in Kotlin.

## Gestione tramite Git

Il progetto è versionato tramite Git e pubblicato su GitHub.

Repository:

```text
https://github.com/BiagioV/PatientKiosk
```

Lo sviluppo viene organizzato tramite commit progressivi, in modo da mostrare l'evoluzione del progetto nel tempo.

Ogni modifica significativa viene salvata tramite:

```bash
git add .
git commit -m "Descrizione della modifica"
git push
```

## Esecuzione da Android Studio

Il progetto può essere aperto ed eseguito direttamente da Android Studio.

Procedura:

1. aprire Android Studio;
2. selezionare `Open`;
3. aprire la cartella `PatientKiosk`;
4. attendere il completamento del Gradle Sync;
5. avviare un emulatore Android o collegare un dispositivo fisico;
6. premere `Run`.

Durante la presentazione sarà quindi possibile mostrare sia l'esecuzione dell'app sia il codice sorgente.

## Focus: usabilità clinica

L'app è pensata per essere utilizzata da un paziente su tablet, in sala d'attesa o durante una visita.

Scelte progettuali:

- flusso semplice e guidato;
- una domanda alla volta;
- testi chiari e leggibili;
- inserimento del codice paziente;
- selezione del questionario da compilare;
- risultato finale mostrato in modo comprensibile;
- assenza di passaggi tecnici visibili al paziente.

## Focus: correttezza dei calcoli

La logica di calcolo degli score è separata dall'interfaccia utente.

File principale:

```text
app/src/main/java/it/uninsubria/patientkiosk/scoring/ScoreCalculator.kt
```

Questa separazione permette di:

- controllare più facilmente le formule;
- evitare che la logica clinica sia mescolata alla UI;
- rendere il codice più leggibile;
- facilitare eventuali test futuri.

Il punteggio viene calcolato a partire dalle risposte selezionate dal paziente.

## Focus: robustezza dell'interfaccia

Lo stato dell'applicazione viene gestito tramite ViewModel.

File principale:

```text
app/src/main/java/it/uninsubria/patientkiosk/viewmodel/KioskViewModel.kt
```

Questa scelta permette di separare:

- logica della schermata;
- dati del questionario;
- stato della compilazione;
- risultato finale.

L'app mantiene il flusso della compilazione in modo controllato, evitando che l'utente debba gestire manualmente la navigazione tra i dati.

## Focus: gestione degli errori

L'app prevede controlli per ridurre gli errori di inserimento e di utilizzo.

Esempi di controlli previsti o da migliorare:

- codice paziente mancante;
- codice paziente non valido;
- questionario non selezionato;
- risposta non selezionata;
- errore nel caricamento del file JSON;
- dati del questionario non corretti o incompleti.

I messaggi di errore devono essere semplici e comprensibili per l'utente finale.

## Focus: modularità

I questionari sono salvati in formato JSON.

File:

```text
app/src/main/assets/questionnaires.json
```

Ogni questionario contiene:

- id;
- nome;
- descrizione;
- domande;
- risposte possibili;
- punteggi;
- tipo di calcolo;
- interpretazioni.

Questa struttura permette di aggiungere o modificare questionari senza dover riscrivere la logica principale dell'applicazione.

## Flusso logico implementato

```text
Identificazione paziente
        ↓
Selezione questionario
        ↓
Compilazione domande
        ↓
Calcolo score
        ↓
Output risultato
```

Questo flusso segue la struttura richiesta dal progetto PatientKiosk.

## Questionari gestiti

Il progetto include questionari dimostrativi relativi a:

- DLQI demo;
- WHO-5 demo;
- HADS demo.

I questionari sono usati a scopo didattico.

Per un utilizzo reale sarebbe necessario:

- usare versioni ufficiali autorizzate;
- verificare eventuali licenze;
- validare clinicamente le formule;
- proteggere i dati personali e sanitari;
- rispettare le normative sulla privacy.

## Stato attuale del progetto

Funzionalità già presenti:

- progetto Android nativo in Kotlin;
- caricamento questionari da JSON;
- visualizzazione dei questionari disponibili;
- compilazione guidata;
- calcolo automatico dello score;
- schermata risultato;
- struttura modulare;
- documentazione di base;
- gestione tramite repository GitHub.

## Miglioramenti previsti

Funzionalità da migliorare nei prossimi commit:

- validazione più completa del codice paziente;
- interfaccia più adatta a tablet;
- messaggi di errore più chiari;
- indicatore di avanzamento delle domande;
- schermata risultato più clinica;
- documentazione dei test manuali;
- miglioramento della gestione degli errori sul caricamento JSON.