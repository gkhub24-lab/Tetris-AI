Tetris AI — Java Tetris with a Self-Playing AI Brain

Course: Stanford University CS108 — designed by Nick Parlante.
Java implementation of Tetris built around clean object-oriented design and an integrated AI "brain" that can evaluate board positions and autonomously play the game in real time.

What this project is

This started as the CS108 Tetris assignment, which is really two projects in one: a correctly-engineered Tetris engine, and an AI system layered on top of it that plays the game on its own. The assignment spec and a handful of support/interface files (JTetris, the Brain interface, DefaultBrain) come from the Stanford course materials — the Piece and Board implementations, the AI-driven JBrainTetris, and all unit tests are my own work.

The AI component is the most interesting part of this repo: it isn't a scripted or hardcoded solver. It works by exhaustively simulating every legal placement of a falling piece — every rotation, in every column — scoring the resulting board, and picking whichever placement minimizes stack height and hole count. The same evaluation logic is then reused, unmodified, to build an "adversary" mode that instead hands the player the worst possible next piece. That reuse — one scoring interface driving two opposite goals — is the core design idea of the project.
How to run

bash# compile everything
javac *.java

# play manually, no AI
java JTetris

# play with the AI brain and adversary controls available
java JBrainTetris

Controls: j / l — move left/right · k — rotate · i — drop · "Brain active" checkbox — let the AI play · Adversary slider — control how often the game feeds you the worst piece.



If you're a current CS108 student: please write your own implementation before referencing this — this is shared as a completed project, not a solution to copy.