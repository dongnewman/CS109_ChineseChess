@echo off
REM _runtest.bat - 自动检测并编译 srcs 目录下的所有 .cpp 文件 (在 srcs 目录中运行)
REM 使用示例：
REM   _runtest.bat           只编译，输出到 ..\bin\runtest.exe
REM   _runtest.bat run       编译并运行

SETLOCAL ENABLEDELAYEDEXPANSION

REM 支持通过环境变量 GPP 覆盖编译器路径
IF DEFINED GPP (
	SET "COMPILER=%GPP%"
) ELSE (
	SET "COMPILER=g++"
)

REM 简单检查编译器是否可用
where %COMPILER% >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
	echo Error: %COMPILER% not found in PATH. Set GPP or add g++ to PATH.
	ENDLOCAL
	exit /b 1
)

REM 输出目录
IF NOT EXIST "..\bin" (
	mkdir "..\bin"
)

SET OUT=..\bin\runtest.exe

REM 收集当前目录下所有 .cpp 文件
SET SRC_LIST=
FOR %%F IN (*.cpp) DO (
	SET SRC_LIST=!SRC_LIST! "%%~fF"
)

REM 如果没有 cpp 文件，退出
IF "%SRC_LIST%"=="" (
	echo No .cpp files found in %CD%
	ENDLOCAL
	exit /b 1
)

echo Found source files:
FOR %%F IN (*.cpp) DO echo  - %%~nF.cpp

REM 编译命令（C++17），包含上级 heads 目录
echo Compiling to %OUT% using %COMPILER%
"%COMPILER%" -g -std=c++17 -I..\heads -O0 %SRC_LIST% -o "%OUT%"
IF %ERRORLEVEL% NEQ 0 (
	echo Build failed.
	ENDLOCAL
	exit /b 2
)

echo Build succeeded: %OUT%

IF "%1"=="run" (
	echo Running: %OUT%
	"%OUT%"
)

ENDLOCAL
