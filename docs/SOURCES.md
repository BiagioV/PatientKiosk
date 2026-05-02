# Fonti cliniche - PatientKiosk

Questo documento raccoglie le fonti utilizzate per definire i questionari e le regole di scoring presenti nel progetto **PatientKiosk**.

I questionari presenti nell'app sono utilizzati a scopo didattico.  
I testi sono versioni demo/parafrasate, mentre struttura, range di punteggio e interpretazioni sono documentati tramite fonti ufficiali o pubblicazioni scientifiche.

---

## DLQI - Dermatology Life Quality Index

### Fonte principale

- Cardiff University - Dermatology Life Quality Index
- Finlay AY, Khan GK. Dermatology Life Quality Index

### Utilizzo nel progetto

Nel progetto è presente:

```text
DLQI - versione didattica
```

Il questionario è rappresentato in forma demo/parafrasata e contiene 10 domande con risposte a punteggio 0-3.

### Scoring usato

Il punteggio totale viene calcolato sommando i punteggi delle 10 risposte.

Range:

```text
0 - 30
```

### Interpretazione usata

Le fasce usate nel progetto sono:

```text
0 - 1    Nessun impatto
2 - 5    Piccolo impatto
6 - 10   Impatto moderato
11 - 20  Forte impatto
21 - 30  Impatto estremamente forte
```

### Note

Per uso clinico reale è necessario utilizzare la versione ufficiale/autorizzata del DLQI.

Nel progetto i testi sono mantenuti in forma didattica/parafrasata per evitare problemi di licenza o copyright.

---

## WHO-5 - Well-Being Index

### Fonte principale

- World Health Organization - WHO-5 Well-Being Index

### Utilizzo nel progetto

Nel progetto è presente:

```text
WHO-5 Well-Being Index - demo
```

Il questionario è rappresentato in forma demo/parafrasata e contiene 5 domande con risposte a punteggio 0-5.

### Scoring usato

Il punteggio grezzo viene calcolato sommando le 5 risposte.

Range grezzo:

```text
0 - 25
```

Per ottenere il punteggio percentuale:

```text
punteggio finale = punteggio grezzo × 4
```

Range finale:

```text
0 - 100
```

### Interpretazione usata

Nel progetto viene usata una soglia didattica:

```text
0 - 49     Benessere basso
50 - 100   Benessere adeguato
```

### Note

La fonte WHO indica che il punteggio grezzo 0-25 viene moltiplicato per 4 per ottenere un punteggio percentuale 0-100.

Nel progetto i testi sono mantenuti in forma didattica/parafrasata.

---

## HADS - Hospital Anxiety and Depression Scale

### Fonte principale

- Zigmond AS, Snaith RP. Hospital Anxiety and Depression Scale
- ePROVIDE / Mapi Research Trust

### Utilizzo nel progetto

Nel progetto è presente:

```text
HADS - demo ansia/depressione
```

Il questionario è mantenuto come versione demo/parafrasata.

### Motivazione

HADS è citato nella traccia come esempio di questionario clinico per ansia e depressione.

Tuttavia, la versione ufficiale può essere soggetta a copyright/licenza.  
Per questo motivo nel progetto non viene copiata la versione ufficiale completa, ma viene mantenuta una struttura didattica/parafrasata con:

- 14 domande;
- 7 item per ansia;
- 7 item per depressione;
- punteggi 0-3;
- sottoscale separate.

### Scoring usato

Sottoscala ansia:

```text
0 - 21
```

Sottoscala depressione:

```text
0 - 21
```

Totale complessivo:

```text
0 - 42
```

### Interpretazione usata

Per ogni sottoscala:

```text
0 - 7     Normale
8 - 10    Borderline
11 - 21   Elevata
```

### Note

Per uso clinico reale è necessario utilizzare la versione ufficiale/licenziata del questionario HADS.

Nel progetto HADS viene usato solo come simulazione didattica per mostrare la gestione di sottoscale separate.

---

## Nota generale sui testi

Per evitare problemi di copyright/licenza, nel repository sono presenti testi didattici/parafrasati.

Le fonti ufficiali vengono usate per documentare:

- struttura dei questionari;
- numero di item;
- range dei punteggi;
- formule di calcolo;
- fasce di interpretazione.

Prima di un eventuale utilizzo reale o clinico sarebbe necessario:

- verificare le licenze;
- usare versioni ufficiali autorizzate;
- validare clinicamente i contenuti;
- rispettare la normativa sulla privacy e sui dati sanitari.

---

## Riepilogo fonti

| Questionario | Fonte principale | Uso nel progetto |
|---|---|---|
| DLQI | Cardiff University, Finlay & Khan | Versione demo/parafrasata |
| WHO-5 | World Health Organization | Versione demo/parafrasata |
| HADS | Zigmond & Snaith, ePROVIDE / Mapi Research Trust | Versione demo/parafrasata |

---

## Disclaimer

PatientKiosk è un progetto didattico.

I questionari presenti nel progetto non devono essere usati per diagnosi, terapia o decisioni cliniche reali.