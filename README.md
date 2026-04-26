# PatientKiosk

Applicazione Android nativa in Kotlin per la compilazione guidata di questionari clinici da tablet.

## Obiettivo didattico

Il progetto implementa il flusso richiesto dal tema d'esame:

1. identificazione anonima del paziente;
2. selezione del questionario;
3. compilazione guidata domanda per domanda;
4. calcolo automatico dello score;
5. visualizzazione del risultato e dell'interpretazione.

## Tecnologie usate

- Kotlin
- Android Studio
- XML layout
- `ListView` + `BaseAdapter`
- `ViewModel` + `LiveData`
- JSON locale in `assets/questionnaires.json`
- Logica di calcolo separata in `ScoreCalculator`

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
└── viewmodel/
    ├── KioskUiState.kt
    └── KioskViewModel.kt
```

## Modularità

I questionari sono caricati da JSON. Per aggiungere un nuovo questionario, si può aggiungere un nuovo oggetto dentro:

```text
app/src/main/assets/questionnaires.json
```

La UI non contiene le formule cliniche. I calcoli sono isolati in:

```text
ScoreCalculator.kt
```

## Note importanti sui testi dei questionari

Il file JSON contiene versioni didattiche/parafrasate per mostrare il funzionamento dell'app. Per una consegna finale o per uso clinico reale, sostituire i testi con le versioni ufficiali autorizzate/licenziate dei questionari scelti.

Le formule e le fasce di interpretazione sono tenute separate dai testi, così il contenuto può essere aggiornato senza modificare l'architettura dell'app.

## Workflow Git consigliato

Esempio di sviluppo con commit frequenti:

```bash
git init
git add .
git commit -m "Initial Android Kotlin project"

git checkout -b feature/questionnaire-json
git add .
git commit -m "Add questionnaire JSON loader"

git checkout -b feature/scoring-engine
git add .
git commit -m "Add score calculator and interpretation bands"

git checkout -b feature/patient-flow
git add .
git commit -m "Add patient identification and questionnaire flow"

git checkout -b feature/ui-polish
git add .
git commit -m "Improve tablet UI and validation messages"
```

Se il gruppo ha più componenti, ogni persona dovrebbe lavorare su branch/commit propri, per esempio:

- componente 1: UI e layout;
- componente 2: modello dati e JSON;
- componente 3: calcoli e test manuali;
- componente 4: documentazione e Git.

## Come aprire il progetto

1. Aprire Android Studio.
2. Selezionare `Open`.
3. Aprire la cartella `PatientKiosk`.
4. Attendere il Gradle sync.
5. Avviare su emulatore tablet o dispositivo Android.

