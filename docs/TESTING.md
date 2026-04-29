# Testing manuale - PatientKiosk

Questo documento descrive i test manuali eseguiti sull'applicazione Android **PatientKiosk**.

L'obiettivo dei test è verificare:

- correttezza del flusso applicativo;
- validazione del codice paziente;
- gestione degli errori;
- navigazione tra le domande;
- correttezza del calcolo degli score;
- robustezza dell'interfaccia.

## Ambiente di test

Applicazione testata tramite:

- Android Studio;
- emulatore Android;
- progetto eseguito in modalità debug;
- codice sorgente aggiornato dal branch `main`.

## Flusso principale testato

Scenario:

1. apertura dell'app;
2. inserimento codice paziente;
3. selezione questionario;
4. compilazione domande;
5. calcolo risultato;
6. visualizzazione schermata finale.

Risultato atteso:

- l'app permette di completare il questionario;
- il risultato viene mostrato correttamente;
- non si verificano crash durante il flusso.

Esito:

```text
OK
```

---

## Test codice paziente

### Test 1 - Codice paziente vuoto

Passi:

1. aprire l'app;
2. lasciare vuoto il campo codice paziente;
3. premere `Inizia compilazione`.

Risultato atteso:

```text
Inserisci il codice paziente prima di continuare.
```

Esito:

```text
OK
```

### Test 2 - Codice paziente troppo corto

Input:

```text
AB
```

Risultato atteso:

```text
Il codice paziente deve contenere almeno 3 caratteri.
```

Esito:

```text
OK
```

### Test 3 - Codice paziente con caratteri non validi

Input:

```text
Paziente 1
```

Risultato atteso:

```text
Usa solo lettere, numeri, trattino - o underscore _. Non inserire nome e cognome.
```

Esito:

```text
OK
```

### Test 4 - Codice paziente valido

Input:

```text
PZ001
```

Risultato atteso:

- il codice viene accettato;
- l'app passa alla schermata di selezione questionario.

Esito:

```text
OK
```

---

## Test selezione questionario

### Test 5 - Visualizzazione questionari disponibili

Passi:

1. inserire codice paziente valido;
2. accedere alla schermata di selezione.

Risultato atteso:

- vengono mostrati i questionari disponibili;
- per ogni questionario sono visibili:
  - nome;
  - descrizione;
  - numero domande;
  - punteggio massimo;
  - testo `Tocca per iniziare`.

Esito:

```text
OK
```

### Test 6 - Avvio questionario

Passi:

1. selezionare un questionario dalla lista.

Risultato atteso:

- l'app apre la schermata di compilazione;
- viene mostrata la prima domanda;
- viene mostrato l'avanzamento.

Esito:

```text
OK
```

---

## Test compilazione domande

### Test 7 - Avanzamento questionario

Passi:

1. aprire un questionario;
2. selezionare una risposta;
3. premere `Prossima domanda`.

Risultato atteso:

- l'app passa alla domanda successiva;
- il testo di avanzamento cambia, ad esempio:
  - `Domanda 1 di 10`;
  - `Domanda 2 di 10`;
  - `Domanda 3 di 10`.

Esito:

```text
OK
```

### Test 8 - Risposta mancante

Passi:

1. aprire una domanda;
2. non selezionare nessuna risposta.

Risultato atteso:

- il pulsante `Prossima domanda` è disabilitato;
- viene mostrato un messaggio che invita a selezionare una risposta.

Esito:

```text
OK
```

### Test 9 - Navigazione indietro

Passi:

1. rispondere alla prima domanda;
2. andare alla seconda domanda;
3. premere `Domanda precedente`.

Risultato atteso:

- l'app torna alla domanda precedente;
- la risposta selezionata rimane memorizzata.

Esito:

```text
OK
```

### Test 10 - Cambio questionario dalla prima domanda

Passi:

1. aprire un questionario;
2. restare sulla prima domanda;
3. premere `Cambia questionario`.

Risultato atteso:

- l'app torna alla schermata di selezione questionario.

Esito:

```text
OK
```

---

## Test calcolo score - DLQI demo

### Test 11 - DLQI con tutte risposte a punteggio 0

Passi:

1. inserire codice paziente `PZ001`;
2. selezionare `DLQI demo`;
3. rispondere a tutte le domande scegliendo la risposta con punteggio `0`.

Risultato atteso:

```text
0 / 30
Nessun impatto
```

Esito:

```text
OK
```

### Test 12 - DLQI con punteggio massimo

Passi:

1. selezionare `DLQI demo`;
2. scegliere per tutte le domande la risposta con punteggio massimo.

Risultato atteso:

```text
30 / 30
```

L'interpretazione deve appartenere alla fascia più grave configurata nel JSON.

Esito:

```text
OK
```

---

## Test calcolo score - WHO-5 demo

### Test 13 - WHO-5 con tutte risposte a punteggio massimo

Passi:

1. selezionare `WHO-5 demo`;
2. rispondere a tutte le domande scegliendo la risposta con punteggio `5`.

Formula attesa:

```text
punteggio grezzo = 25
punteggio finale = 25 × 4 = 100
```

Risultato atteso:

```text
100 / 100
Benessere adeguato
```

Esito:

```text
OK
```

### Test 14 - WHO-5 con tutte risposte a punteggio minimo

Passi:

1. selezionare `WHO-5 demo`;
2. rispondere a tutte le domande scegliendo la risposta con punteggio `0`.

Formula attesa:

```text
punteggio grezzo = 0
punteggio finale = 0 × 4 = 0
```

Risultato atteso:

```text
0 / 100
```

L'interpretazione deve indicare una fascia bassa di benessere.

Esito:

```text
OK
```

---

## Test calcolo score - HADS demo

### Test 15 - HADS con tutte risposte a punteggio 0

Passi:

1. selezionare `HADS demo`;
2. rispondere a tutte le domande scegliendo la risposta con punteggio `0`.

Risultato atteso:

```text
0 / 42
Ansia: Normale · Depressione: Normale
```

Esito:

```text
OK
```

### Test 16 - HADS con punteggio massimo

Passi:

1. selezionare `HADS demo`;
2. scegliere per tutte le domande la risposta con punteggio massimo.

Risultato atteso:

```text
42 / 42
```

L'interpretazione deve mostrare una fascia elevata sia per ansia sia per depressione.

Esito:

```text
OK
```

---

## Test gestione errori dati

### Test 17 - Caricamento questionari

Passi:

1. avviare l'app;
2. verificare che i questionari vengano caricati dal file JSON.

Risultato atteso:

- la lista dei questionari viene mostrata correttamente;
- nessun errore di caricamento viene visualizzato.

Esito:

```text
OK
```

### Test 18 - Validazione struttura questionari

La logica di caricamento controlla:

- presenza del campo `questionnaires`;
- presenza di id, titolo, descrizione e sorgente;
- presenza di domande;
- presenza di risposte;
- presenza dei punteggi;
- coerenza tra punteggio massimo dichiarato e punteggio massimo calcolabile;
- presenza delle fasce di interpretazione.

Risultato atteso:

- se il JSON è corretto, l'app carica i questionari;
- se il JSON è errato, viene prodotto un errore comprensibile.

Esito:

```text
OK
```

---

## Test finale completo

Scenario finale:

1. aprire app;
2. inserire `PZ001`;
3. selezionare `DLQI demo`;
4. compilare tutte le domande;
5. visualizzare risultato;
6. premere `Cambia questionario`;
7. selezionare un altro questionario;
8. completare anche il secondo questionario;
9. premere `Nuova compilazione`.

Risultato atteso:

- nessun crash;
- navigazione coerente;
- risultati mostrati correttamente;
- stato dell'app resettato correttamente con `Nuova compilazione`.

Esito:

```text
OK
```

## Note

L'applicazione è un progetto didattico e non deve essere usata per diagnosi o decisioni cliniche reali.