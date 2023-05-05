@echo off

echo Indo iniciar o VirtualEnv...

cd ..

call venv\Scripts\activate.bat

echo VirtualEnv iniciado!

echo Indo iniciar o servidor...

call venv\Scripts\python.exe prod\run.py