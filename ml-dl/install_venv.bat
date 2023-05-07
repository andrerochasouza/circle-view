@echo off

echo --------------------------------------------------
echo Iniciando instalação Venv
echo --------------------------------------------------

rem Verificar se já existe a pasta venv
if exist venv (
    echo Erro: Ja existe a pasta venv.
    echo --
    echo Remova a pasta venv e tente novamente.
    echo --------------------------------------------------
    pause
    exit
)

rem Verificar se o caminho do Python 3.7 foi especificado na linha de comando
set PYTHON_PATH=%1
if "%1"=="" (
    echo Erro: Caminho do Python 3.7 nao especificado na linha de comando.
    echo --
    echo Usando o caminho padrao: "C:\Program Files\Python37\python.exe"
    echo --
    set "PYTHON_PATH=C:\Program Files\Python37\python.exe"
)


echo Caminho completo do Python 3.7: %PYTHON_PATH%
echo --------------------------------------------------

rem criar o ambiente virtual com o python 3.7
"%PYTHON_PATH%" -m venv venv

rem ativar o ambiente virtual
call venv\Scripts\activate.bat

rem instalar as dependências
pip install -r requirements.txt
pip install tensorflow

echo --------------------------------------------------
echo Instalacao Venv finalizada!
echo --------------------------------------------------
pause