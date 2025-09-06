// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.


//pseuchdo code:
// take default value of screen row white, so 0000...000 = 0 (SCREEN_CLR = 0)
// if M[KBD] = 0 { set screen row to black, so 11.....111 = -1 (SCREEN_CLR = -1)
// cur_row_to_fill = first coordinate of screen (@screen)
// while cur_row_to_fill!=@kbd {fill row}

(START)

//check keyboard
@KBD
D = M

//set default value
@SCREEN_CLR
M = 0 
@SCREEN_CHANGE
D;JEQ

//change default value
@SCREEN_CLR
M = -1

//change screen color
(SCREEN_CHANGE)

@SCREEN
D = A
@CUR_POS
M = D

//while cur_pos != kbd {fill row}
(LOOP)
@CUR_POS
D = M
@KBD
D = D - A
@START
D;JEQ

//fill row
@SCREEN_CLR
D = M
@CUR_POS
A = M
M = D

//increase iteration
@CUR_POS
M = M + 1

//go back to loop
@LOOP
0;JMP

