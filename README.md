# DooR DasH: Scare vs Laugh Touchdown

A two-player, turn-based board game built in Java, inspired by Monsters, Inc. Two monsters — a Scarer and a Laugher — race across a 100-cell zigzag board, collecting energy through doors, cards, and hazard cells, aiming to be the first to reach the final cell with at least 1000 energy.

## Overview

This project was built for the Computer Programming Lab course at the German University in Cairo (GUC), with an emphasis on clean OOP design rather than just "making the game work."

- **Java + JavaFX** desktop application
- **MVC architecture** — game logic (`game.engine`), UI (`view`), and input/update handling (`controller`) are fully separated
- **Abstract class hierarchies** for `Monster`, `Card`, and `Cell`, each extended by multiple concrete subtypes with their own polymorphic behavior
- **Data-driven design** — monsters, cards, and cells are defined in CSV files (`monsters.csv`, `cards.csv`, `cells.csv`) and loaded at runtime via a dedicated `DataLoader`, instead of being hardcoded
- **Custom exception hierarchy** for invalid moves, malformed CSV input, invalid turns, and energy errors
- **Unit tests** covering core game logic

## Features

- 8 monsters across 4 types — Dasher, Dynamo, MultiTasker, Schemer — each with a passive trait and an activatable powerup
- 50 door cells, 10 monster cells, 10 card cells, 5 conveyor belts, and 5 contamination-sock hazards
- 5 card types (Swapper, Energy Steal, Start Over, Shield, Confusion) drawn from a shuffled, auto-reshuffling deck
- Full turn-based game loop: dice movement, occupied-cell retries, cell/card effects, and a win condition requiring both board position and an energy threshold

## Project Structure

```
DoorDash/
├── src/
│   ├── controller/        # Input handling, turn orchestration
│   ├── view/               # JavaFX UI (Main, MainMenuView, GameView)
│   └── game/
│       ├── engine/
│       │   ├── monsters/   # Monster (abstract) + 4 subtypes
│       │   ├── cards/      # Card (abstract) + 5 subtypes
│       │   ├── cells/      # Cell (base) + 5 subtypes
│       │   ├── dataloader/ # CSV parsing
│       │   └── exceptions/ # Custom exception hierarchy
│       └── tests/          # Unit tests
├── monsters.csv
├── cards.csv
└── cells.csv
```

## Running the project

This is an Eclipse project with a JavaFX dependency.

1. Clone the repo and open it in Eclipse (or your IDE of choice) as an existing project.
2. Make sure a JavaFX SDK is configured (Eclipse: *Project Properties → Java Build Path → Libraries*, or via a JavaFX-enabled JRE).
3. Run `view/Main.java`.

## Tech Stack

`Java` · `JavaFX` · `JUnit` · `CSV data parsing` · `MVC architecture` · `OOP design (inheritance, polymorphism, abstraction)`
