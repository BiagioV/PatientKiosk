# Piano di test manuale

## 1. Identificazione paziente

| Caso | Input | Risultato atteso |
|---|---|---|
| Codice vuoto | campo vuoto | messaggio di errore |
| Codice troppo corto | `AB` | messaggio di errore |
| Codice valido | `PZ001` | passaggio alla selezione questionario |
| Codice con spazi | ` PZ001 ` | codice normalizzato e accettato |

## 2. Selezione questionario

| Caso | Azione | Risultato atteso |
|---|---|---|
| Tap su un questionario | selezione da lista | apertura della prima domanda |
| Lista caricata | avvio app | visualizzazione di DLQI demo, WHO-5 demo, HADS demo |

## 3. Compilazione guidata

| Caso | Azione | Risultato atteso |
|---|---|---|
| Avanti senza risposta | tap su Prossima domanda | messaggio di errore |
| Risposta selezionata | tap su Prossima domanda | avanzamento alla domanda successiva |
| Indietro | tap su Indietro | ritorno alla domanda precedente |
| Risposta precedente | torna indietro | risposta già selezionata mantenuta |

## 4. Calcolo DLQI demo

Formula: somma dei punteggi delle 10 risposte.

| Risposte | Totale atteso | Interpretazione attesa |
|---|---:|---|
| tutte 0 | 0 | Nessun impatto |
| tutte 1 | 10 | Impatto moderato |
| cinque 2 + cinque 1 | 15 | Forte impatto |
| tutte 3 | 30 | Impatto estremamente forte |

## 5. Calcolo WHO-5 demo

Formula: somma grezza 0-25, poi moltiplicazione per 4.

| Risposte | Grezzo | Score finale | Interpretazione attesa |
|---|---:|---:|---|
| tutte 0 | 0 | 0 | Benessere basso |
| tutte 2 | 10 | 40 | Benessere basso |
| tutte 3 | 15 | 60 | Benessere adeguato |
| tutte 5 | 25 | 100 | Benessere adeguato |

## 6. Calcolo HADS demo

Formula: somma separata delle 7 domande di categoria `A` e delle 7 domande di categoria `D`.

| Risposte | HADS-A | HADS-D | Interpretazione attesa |
|---|---:|---:|---|
| tutte 0 | 0 | 0 | Normale / Normale |
| tutte 1 | 7 | 7 | Normale / Normale |
| tutte 2 | 14 | 14 | Elevata / Elevata |
| tutte 3 | 21 | 21 | Elevata / Elevata |

## 7. Robustezza UI

- ruotare lo schermo durante la compilazione;
- verificare che lo stato resti disponibile tramite ViewModel;
- provare caratteri non validi nel codice paziente;
- verificare che il risultato specifichi che non sostituisce la valutazione medica.
