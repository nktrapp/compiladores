.assembly extern mscorlib {}
.assembly _programa{}
.module _programa.exe
.class public _unica{
.method static public void _principal(){
.entrypoint
ldc.i4.1
brfalse novo_rotulo1
ldstr "oi"
[mscorlib]System.Console::Write(string)
[mscorlib]System.Console::WriteLine()
novo_rotulo1:
ret
}
}
