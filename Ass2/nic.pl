%basic logic to follow:
%generate list of all possible solutions
%on this list, apply criteria (like distance) <-While creating would be ideal for speed
%q1: list build (max time)
%q2: list build (max cost)
%q3: list build (max dist)

%edge(town1, town2, distance).
edge(s, a, 300).
edge(s, d, 20).
edge(a, b, 1500).
edge(a, d, 400).
edge(b, c, 9).
edge(b, e, 200).
edge(c, d, 2000).
edge(c, g, 12).
edge(d, e, 3).
edge(e, f, 400).
edge(f, g, 800).

%Since our edges are bidirectional, edge(A,B) means we can go from A to B and from B to A.
route(A, B, Distance) :- edge(A, B, Distance).
route(A, B, Distance) :- edge(B, A, Distance).

%Makes a trip from town A to town B while saving the visited towns (path) and the total distance.
trip(A, B, Path, [B | Path], Distance) :-
	route(A, B, Distance).
trip(A, B, PreviousPath, NewPath, Distance) :-
	route(A, C, D1), B \= C, \+(member(C, PreviousPath)), 				
	trip(C, B, [C | PreviousPath], NewPath, D2), Distance is D1 + D2.
	
%Reverses a list. reverse([a, b, c], L): L = [c, b, a].
reverseList(List, ReversedList) :- reverseList(List, [], ReversedList).
reverseList([], ReversedList, ReversedList).
reverseList([H | T], Accumulator, ReversedList) :-
	reverseList(T, [H | Accumulator], ReversedList).

%Finds a path from town A to town B.
findPath(A, B, Path, Distance) :-
	trip(A, B, [A], ReversedPath, Distance), reverseList(ReversedPath, Path).
	
%Path solver includes round trip from s to s every time
pathSolver(S, Towns, Path, Distance) :- 
	findPath(S, S, Path, Distance), submember(Towns, Path).
	
submember([], _).
submember([H | T], L2) :- member(H, L2), !, submember(T, L2).
	
%Appends the second list to the first list.
append([], L, L).
append([H | T], L2, [H | L]) :- append(T, L2, L).
	
%Answer question 3. Build a set of solutions and return element going from Start to Start while including Including nodes in the shortest distance
q3(Start,Including):- setof([D,A],pathSolver(Start,Including,A,D),[Solution|_]), print(Solution).






%Testing to see if an infitite amount of solutions are created when going from starting node to ending node
infinitTest(Start,End):- setof([D,A],findPath(Start,End,A,D),Set), printAll(Set).

printAll([]).
printAll([H|T]) :-
	print(H),nl,
	printAll(T).