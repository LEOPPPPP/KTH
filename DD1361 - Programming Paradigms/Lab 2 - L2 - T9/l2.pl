%L�s in ordlistan fr�n words.txt till variabeln Words.
words(Words):-
        words('words.txt', Words).

%�ppna str�m till filen som meddelas i Source, l�s in orden, st�ng str�mmen.
words(Source, Words):-
	open(Source, read, In),
	read_words(In, Words),
	close(In).

%L�ser in f�rsta bokstaven fr�n str�mmen In.
%Kallar f�r att forts�tta inl�sningen via read_words/ 3.
read_words(In, Words):-
	get_code(In, Char),
	read_words(Char, In, Words).

%Om inl�st char �r -1, Words blir nil och rekursionen stoppas.
read_words(-1, _In, []):- !.

%L�ser in resterande av bokst�ver via read_words/ 5
read_words(Char, In, Words):-
	read_words(Char, Word, Word, In, Words).

%Om radslut - Ordet l�ggs f�rst i listan som h�ller orden.
%read_words/ 2 tillkallas f�r att b�rja l�sa in n�sta ord.
read_words(10, [], Word, In, [Word|Words]):- !,
	read_words(In, Words).
%Om vi ville l�gga in orden direkt i ett tr�d hade vi innan vi rekurserar
%anropat insert_word p� ordet vi h�mtat fr�n filen. Ist�llet f�r listan
%Words hade vi ersatt den med ett tr�d.



%L�gger den nyinl�sta bokstaven f�rst i ordet som l�ses in
%och anropar f�r att l�sa in n�sta bokstav som l�gs efter den f�rsta.
read_words(Char, [Char|Rest], Word, In, Words):-
	get_code(In, Next),
	read_words(Next, Rest, Word, In, Words).

%%%DEL 1%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Tar en sifferkod och ordlista och ger ett ord som motsvarar kombinationen.
%Skapar f�rst ordet fr�n koden och kollar sedan om det finns i ordlistan.
t9_1(Code, Words, Word) :-
	make_word(Code, Word),
	member(Word, Words).

%Skapar ett ord av given sifferkod. Anropar digit_char med f�rsta siffran
%och l�gger resultatet f�rst i listan Word och anropar sedan make_word
%igen f�r att l�gga resterande bokst�ver efter.
make_word([], []) :- !.
make_word([Digit|Code], [Char|Word]) :-
	digit_char(Digit, Char),
	make_word(Code, Word).

%%%DEL 2%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Tar en sifferkod och ordlista och ger ett ord som mostsvarar kombinationen.
t9_2(Code, Words, Word) :-
	decode(Code, Words, Word).

%Om Listan �r tom returneras nil som sista element i resultatet f�r att
%markera slutet. Rekursionen avslutas.
decode([], _, []).

%Skapar ett ord av given sifferkod. H�mtar f�rsta bokstaven genom digit_char
%och kollar om det finns ord som b�rjar med den bokstaven i ordlistan.
%�r s� fallet filtrerar den listan till att bara inneh�lla ord som b�rjar
%med given bokstav. Rekurserar sedan vidare med n�sta bokstav som f�rsta.
decode([Digit|Code], Words, [Char|Word]):-
	digit_char(Digit, Char),
	memberchk([Char|_], Words),
	filter(Words, Char, Filtered),
	decode(Code, Filtered, Word).

%Filtrerar listan s� att den endast inneh�ller ord som b�rjar med given bokstav.
%Om listan �r tom, returnera nil som det sista elementet i resultatlistan f�r
%att markera slutet. Rekursienen slutar h�r.
filter([], _, []).

%Matchar f�rsta bokstaven i orden i listan och l�gger till ordet minus bokstaven
%till resultatlistan. D�refter rekurseras �ver �terst�ende del av listan.
filter([[Char|Restchars]|Rest], Char, [Restchars|Filtered]) :- !,
	    filter(Rest, Char, Filtered).

%Om f�rsta bokstaven i ordet inte matchar, allts� ovanst�ende funktion inte
%anv�nds s� rekurserar man �ver resterande del av listan minus f�rsta ordet
%utan att ordet l�ggs till i resultatlistan.
filter([_|T], Char, Filtered) :-
	filter(T, Char, Filtered).

%%%DEL 3%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Tar en sifferkod och ordlista och ger ett ord som mostsvarar kombinationen.
t9_3(Code, Tree, Word) :-
	decode_2(Code, Tree, Word).

%Om tr�det �r tom returneras nil som sista element i resultatet f�r att
%markera slutet. Rekursionen avslutas.
decode_2([], _, []).

%Skapar ett ord av given sifferkod. H�mtar f�rsta bokstaven genom digit_char
%och kollar om det finns ord som b�rjar med den bokstaven i tr�det.
%�r s� fallet rekurseras sedan vidare p� deltr�det som inneh�ller bokstaven.
decode_2([Digit|Code], Tree, [Char|Word]):-
	digit_char(Digit, Char),
	memberchk(branch(Char, Sub), Tree),
	decode_2(Code, Sub, Word).

%Om inga fler bokst�ver att l�gga till i tr�det, avsluta rekursionen.
insert_word([], _).

%Kollar om tr�det har en gren f�r f�rsta bokstaven i givet ord.
%�r s� fallet kallas insert_word igen p� resterande del av ordet.
insert_word([Char|Word], Tree):-
	memberchk(branch(Char, Branches), Tree), !,
	insert_word(Word, Branches).

%Om inga fler ord i listan, avsluta rekursionen.
insert_all([], _).

%S�tter in orden i given lista i tr�det genom anrop till insert_word med
%det f�rsta ordet i listan. Rekurserar sedan vidare med listan utan det 
%f�rsta ordet.
insert_all([First|Rest], Tree) :-
	insert_word(First, Tree), 
	insert_all(Rest, Tree).

%Traverserar tr�det rekursivt gren efter gren.
%Cut g�r s� att backtracking inte �r m�jlig efter att man n�tt slutet
%och prolog p� s� s�tt inte f�rs�ker hitta fler l�sningar.
done([]):- !.
done([branch(_,Branches)|Rest]):-
	done(Branches),
	done(Rest).

%Bygger upp tr�det fr�n given ordlista genom att kalla p� insert_all.
build(Words, Tree) :-
	insert_all(Words, Tree),
	done(Tree).

%%%TESTER%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%6,3,6,2,3,7,7,4,4,7 - membership
test1:-
	words(Words),
	statistics(runtime,_),
	t9_1([3,2,7,8,4], Words, Word),
	statistics(runtime,[_, Time]),
	format("one solution is \"~s\", found in ~w ms~n" ,[Word, Time]).

test2:-
	words(Words),
	statistics(runtime,_),
	t9_2([3,2,7,8,4], Words, Word),
	statistics(runtime,[_, Time]),
	format("one solution is \"~s\", found in ~w ms~n" ,[Word, Time]).

test3:-
	words(Words),
	statistics(runtime,_),
	build(Words, Tree),!,
	statistics(runtime,[_,Build]),
	t9_3([3,2,7,8,4], Tree, Word),
	statistics(runtime,[_, Time]),
	format("one solution is \"~s\", found in ~w ms, (building of tree in ~w ms)~n" ,[Word, Time, Build]).

test4:-
	statistics(runtime,_),
	words(Words),
	statistics(runtime,[_,List]),
	build(Words, Tree),!,
	statistics(runtime,[_,Build]),
	t9_3([3,2,7,8,4], Tree, Word),
	statistics(runtime,[_, Time]),
	format("one solution is \"~s\", found in ~w ms, (building of List in ~w ms, building of Tree in ~w ms)~n" ,[Word, Time, List, Build]).


digit_char(2,0'a).
digit_char(2,0'b).
digit_char(2,0'c).
digit_char(3,0'd).
digit_char(3,0'e).
digit_char(3,0'f).
digit_char(4,0'g).
digit_char(4,0'h).
digit_char(4,0'i).
digit_char(5,0'j).
digit_char(5,0'k).
digit_char(5,0'l).
digit_char(6,0'm).
digit_char(6,0'n).
digit_char(6,0'o).
digit_char(7,0'p).
digit_char(7,0'q).
digit_char(7,0'r).
digit_char(7,0's).
digit_char(8,0't).
digit_char(8,0'u).
digit_char(8,0'v).
digit_char(9,0'w).
digit_char(9,0'x).
digit_char(9,0'y).
digit_char(9,0'z).

%%%SKRIV DIREKT TILL TR�DET EFTER ATT HA H�MTAT ETT HELT ORD%%%%%%%%%%

words2(Words):-
        words2('words.txt', Words).
words2(Source, Words):-
	open(Source, read, In),
	read_words2(In, Words),
	close(In).
read_words2(In, Words):-
	get_code(In, Char),
	read_words2(Char, In, Words).
read_words2(-1, _In, []):- !.
read_words2(Char, In, Words):-
	read_words2(Char, Word, Word, In, Words).
read_words2(-1, _, _Word, _In, _Words):- !.
%	insert_word(Word, Words).
read_words2(10, [], _Word, In, Words):- !,
%	insert_word(Word, Words),
	read_words2(In, Words).
read_words2(Char, [Char|Rest], Word, In, Words):-
	memberchk(branch(Char, Branches), Words), !, %Tag bort
	get_code(In, Next),
	read_words2(Next, Rest, Word, In, Branches). %Replace branches with words


test5:-
	statistics(runtime,_),
	words2(Words),
	done(Words),
	statistics(runtime,[_,List]),
	t9_3([3,2,7,8,4], Words, Word),
	statistics(runtime,[_, Time]),
	format("one solution is \"~s\", found in ~w ms, (Building of Tree in ~w ms)~n" ,[Word,Time,List])
	.
test5x :-
	statistics(runtime,_),
	test5xtimes(0),
	statistics(runtime,[_,List]),
	format("Building of Tree in ~w ms~n" ,[ List]).
test5xtimes(10).
test5xtimes(N) :-
	test5,
	M is N+1,
	test5xtimes(M).

appendx(X,[],[X]).
appendx(X,[H|T],[H|L]):-
	appendx(X,T,L).
reverse([],[]).
reverse([H|T],Y):-
	reverse(T, RT),
	appendx(H,RT,Y).
