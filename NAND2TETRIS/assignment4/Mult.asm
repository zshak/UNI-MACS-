// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.

//pseuchdo code:
//for(int i = m[R1]; i >=0; i --){
//M[R2]+=M[R0]
//}
//return;


@R2
M = 0 //initalize M[R2] = 0 in case M[R1] is zero

(LOOP)
@R1
D = M
@END
D,JEQ

@R0
D = M //READ R0
@R2
M = M + D //M[R2] += M[R0]

@R1
M = M - 1 // M[R1]--

@LOOP
0;JMP

(END)
@END
0;JMP
