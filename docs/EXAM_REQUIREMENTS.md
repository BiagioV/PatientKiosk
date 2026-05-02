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
PatientCodeValidator.kt
KioskViewModel.kt
KioskUiState.kt
```

L'interfaccia è definita tramite layout XML, mentre la logica applicativa, la gestione dello stato, il caricamento dei dati, la validazione e il calcolo dei punteggi sono implementati in Kotlin.

## Gestione tramite Git

Il progetto è versionato tramite Git e pubblicato su GitHub.

Repository:

```text
https://github.com/BiagioV/PatientKiosk
```

Lo sviluppo è stato organizzato tramite commit progressivi, in modo da mostrare l'evoluzione del progetto nel tempo.

Sono stati creati commit dedicati per:

- configurazione progetto;
- documentazione iniziale;
- validazione codice paziente;
- miglioramento schermata iniziale;
- miglioramento selezione questionario;
- indicatore avanzamento domande;
- gestione risposte mancanti;
- navigazione indietro;
- schermata risultato;
- validazione calcoli e caricamento JSON;
- documentazione test e architettura;
- aggiornamento README finale.

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

## Obiettivo PatientKiosk

Il progetto realizza una applicazione tablet/kiosk per la compilazione guidata di questionari clinici da parte dei pazienti.

Il flusso implementato è:

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

Questo segue lo schema logico richiesto dalla traccia del progetto.

## Focus: usabilità clinica

L'app è pensata per essere utilizzata da un paziente su tablet, in sala d'attesa o durante una visita.

Scelte progettuali:

- flusso semplice e guidato;
- una domanda alla volta;
- testi chiari e leggibili;
- inserimento tramite codice paziente;
- avviso di non inserire nome, cognome o dati personali;
- selezione chiara del questionario;
- indicatore di avanzamento;
- pulsanti grandi e facili da usare;
- risultato finale mostrato in modo comprensibile;
- disclaimer didattico nella schermata risultato.

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

Tipi di calcolo supportati:

- `SUM`;
- `WHO5_PERCENTAGE`;
- `HADS_SUBSCALES`.

Esempi implementati:

- DLQI demo: somma dei punteggi delle risposte;
- WHO-5 demo: punteggio grezzo moltiplicato per 4;
- HADS demo: calcolo separato delle sottoscale ansia e depressione.

La logica controlla anche:

- presenza di tutte le risposte;
- coerenza tra punteggio massimo dichiarato e punteggio massimo calcolabile;
- validità dei punteggi selezionati;
- punteggio finale dentro l'intervallo previsto.

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
- risposte già selezionate;
- risultato finale.

Funzionalità di robustezza implementate:

- il pulsante avanti è disabilitato se non è stata selezionata una risposta;
- la risposta selezionata viene mantenuta quando si torna indietro;
- dalla prima domanda è possibile tornare alla selezione del questionario;
- è possibile iniziare una nuova compilazione dopo il risultato;
- gli errori vengono mostrati in modo visibile all'utente.

## Focus: gestione degli errori

L'app prevede controlli per ridurre gli errori di inserimento e di utilizzo.

Controlli implementati:

- codice paziente mancante;
- codice paziente troppo corto;
- codice paziente con caratteri non validi;
- risposta non selezionata;
- errore nel caricamento del file JSON;
- questionario senza domande;
- questionario senza interpretazioni;
- domande duplicate;
- risposte duplicate;
- punteggi negativi;
- `maxScore` non coerente;
- configurazione HADS non valida;
- punteggio calcolato fuori intervallo.

I messaggi di errore sono pensati per essere semplici e comprensibili per l'utente finale.

## Focus: modularità

I questionari sono salvati in formato JSON.

File:

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
- punteggi;
- fasce di interpretazione.

Questa struttura permette di aggiungere o modificare questionari senza dover riscrivere la struttura principale dell'applicazione.

Se un nuovo questionario usa un tipo di scoring già supportato, è sufficiente aggiungerlo al JSON rispettando la struttura prevista.

## Questionari gestiti

Il progetto include tre questionari dimostrativi:

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

## Documentazione prodotta

La documentazione si trova nella cartella:

```text
docs/
```

File principali:

- `EXAM_REQUIREMENTS.md`: copertura dei requisiti d'esame;
- `TESTING.md`: test manuali effettuati;
- `ARCHITECTURE.md`: architettura del progetto;
- `SOURCES.md`: riferimenti e fonti.

## Test manuali

I test manuali sono documentati in:

```text
docs/TESTING.md
```

Sono stati considerati test su:

- codice paziente valido e non valido;
- visualizzazione questionari;
- avvio compilazione;
- risposta mancante;
- avanzamento domande;
- navigazione indietro;
- cambio questionario;
- nuova compilazione;
- calcolo DLQI demo;
- calcolo WHO-5 demo;
- calcolo HADS demo;
- caricamento e validazione del JSON.

## Stato attuale del progetto

Funzionalità presenti:

- progetto Android nativo in Kotlin;
- repository GitHub con commit progressivi;
- caricamento questionari da JSON;
- validazione struttura questionari;
- identificazione tramite codice paziente;
- validazione codice paziente;
- visualizzazione dei questionari disponibili;
- compilazione guidata;
- indicatore avanzamento;
- gestione risposte mancanti;
- navigazione avanti/indietro;
- calcolo automatico dello score;
- gestione di sottoscale HADS;
- schermata risultato clinicamente leggibile;
- disclaimer didattico;
- documentazione di architettura;
- documentazione dei test;
- README finale aggiornato.

## Limiti noti

Il progetto è didattico e presenta alcuni limiti:

- i risultati non vengono salvati in un database;
- non è presente autenticazione;
- non è presente esportazione PDF;
- non è presente integrazione con sistemi clinici reali;
- i questionari sono demo/parafrasati;
- l'app non deve essere usata per diagnosi o decisioni cliniche reali.

## Conclusione

PatientKiosk soddisfa i requisiti principali del progetto:

- applicazione Android sviluppata in Kotlin;
- gestione tramite Git;
- compilazione guidata di questionari clinici;
- caricamento dei questionari da JSON;
- calcolo automatico dello score;
- output risultato;
- attenzione a usabilità, robustezza, errori e modularità.