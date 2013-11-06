%Martin version - WIP

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

%Calculates the time and cost when travelling a Distance from Town.
answerTimeAndCost(Time,Cost,Time,Cost).
timeAndCost(Town,Distance, Time, Cost, plane) :- Distance >= 400,
	Time2 is Distance / 500, Cost2 is Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(Town,Distance, Time, Cost, train) :- Time2 is Distance / 120, Cost2 is 0.75 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(Town,Distance, Time, Cost, bus) :- Town \= a, Town \= e, Town \= f, Time2 is Distance / 80, Cost2 is 0.40 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(Town,Distance, Time, Cost, car) :- Time2 is Distance / 100, Cost2 is 0.60 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(Town,Distance, Time, Cost, walk) :- Time2 is Distance / 5, Cost2 is 0, answerTimeAndCost(Time,Cost,Time2,Cost2).

%Returns all possible Modes of transportations for this traveled Distance from given Town, along with their Cost, while sorted by Time
quickTravel(Town,Distance,Solution) :-
	setof([Time,Cost,Mode],timeAndCost(Town,Distance,Time,Cost,Mode),Solution).
%Only returns quickest possible movement
quickestTravel(Town,Distance,Time,Cost,Mode):- quickTravel(Town,Distance,[[Time,Cost,Mode]|_]).

%Returns all possible Modes of transportations for this traveled Distance from given Town, along with their Time, while sorted by Cost	
cheapTravel(Town,Distance,Solution) :-
	setof([Cost,Time,Mode],timeAndCost(Town,Distance,Time,Cost,Mode),Solution).
%Only returns cheapest possible movement
cheapestTravel(Town,Distance,Time,Cost,Mode):- cheapTravel(Town,Distance,[[Cost,Time,Mode]|_]).
	
%Returns a Solution displaying traveled nodes with the mode of transportation, the total Time of this solution, and the total Cost of this solution. Emphasis is on the speed and doesn't account for cost.
getQuickTimeAndCost([_ | []], Solution, Solution, Time, Cost, Time, Cost).
getQuickTimeAndCost([T1, T2 | Tail], TempSolution, Solution, TempTime, TempCost, Time, Cost) :- route(T1, T2, Distance), quickestTravel(Town,Distance, Time1, Cost1, Mode),
	append(TempSolution, [T1, Mode], Sol2), append([T2], Tail, Path2), TempTime2 is TempTime + Time1, TempCost2 is TempCost + Cost1,
	getQuickTimeAndCost(Path2, Sol2, Solution, TempTime2, TempCost2, Time, Cost).

%Returns a Solution displaying traveled nodes with the mode of transportation, the total Time of this solution, and the total Cost of this solution. Emphasis is on the cost and doesn't account for speed.	
getCheapTimeAndCost([_ | []], Solution, Solution, Time, Cost, Time, Cost).
getCheapTimeAndCost([T1, T2 | Tail], TempSolution, Solution, TempTime, TempCost, Time, Cost) :- route(T1, T2, Distance), cheapestTravel(Town,Distance, Time1, Cost1, Mode),
	append(TempSolution, [T1, Mode], Sol2), append([T2], Tail, Path2), TempTime2 is TempTime + Time1, TempCost2 is TempCost + Cost1,
	getCheapTimeAndCost(Path2, Sol2, Solution, TempTime2, TempCost2, Time, Cost).
	
%Generates a "Quick" solution from starting node S hitting at least all Towns specified exactly once and going back to s
generateQuick(S, Towns, S1, Distance, Time, Cost) :- findPath(S, S, Path, Distance), submember(Towns, Path), 
	setof([Distance,S1,Time,Cost],getQuickTimeAndCost(Path, [], S1, 0, 0, Time, Cost),Set).

%Generates a "Cheap" solution from starting node S hitting at least all Towns specified exactly once and going back to s
generateCheap(S, Towns, Solution, Distance, Time, Cost) :- 
	setof([Distance,Path],findPath(S, S, Path, Distance),Set),
	plausible(Set,[],Best,Towns),
	setof([Cost,S1,Time,Distance],getCheapTimeAndCost(Best, [], S1, 0, 0, Time, Cost),[Solution|_]).	
	
plausible([],Answer,Answer,_).
plausible([[Solution|_]|T],TempSol,Answer,Towns):-
	subset(Towns,Solution),
	append(TempSol, [Solution], NewSol),
	!,
	NewSol is TempSol,
	plausible(T,NewSol,Answer,Towns).
	
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

%Appends the second list to the first list.
append([], L, L).
append([H | T], L2, [H | L]) :- append(T, L2, L).

submember([], _).
submember([H | T], L2) :- member(H, L2), !, submember(T, L2).

%Prints [a, car, b] as 'a car b'.
print([]).
print([H | T]) :- write(H), write(' '), print(T).

%Question 1:
%tripMaxTime(S, Towns, MaxTime) :- generateFastest(S, Towns, Solution, Distance, Time, Cost, MaxTime), Time < MaxTime, printTrip(Solution, Distance, Time, Cost).
%q1 :- tripMaxTime(s, [a], 15).

%Question 2:
%tripMaxCost(S, Towns, MaxCost) :- generateCheapest(S, Towns, Solution, Distance, Time, Cost, MaxCost), Cost < MaxCost, printTrip(Solution, Distance, Time, Cost).
%q2 :- tripMaxCost(s, [a,b,c,d,e,f,g], 2000).

%Question 3 Build a set of solutions and return element going from Start to Start while including Including nodes in the shortest distance
q3(Start,Including):- setof([D,A],generate(Start,Including,A,D,_,_),[Solution|_]), print(Solution).

%Path solver includes round trip from s to s every time
pathSolver(S, Towns, Path, Distance) :- 
	findPath(S, S, Path, Distance), submember(Towns, Path).

%See full output from q3
q3test(Start,Including):- setof([D,A],pathSolver(Start,Including,A,D),Set), printAll(Set).

%print ever element in a set
printAll([]).
printAll([H|T]) :-
	print(H),nl,
	printAll(T).

%Prints a path, its distance, time and cost.
printTrip([Path, _, _, _], Distance, Time, Cost) :- print(Path), write('s'), nl, write('Total distance: '), write(Distance), write('km.'),
	nl, write('Total time: '), write(Time), write('h.'), nl, write('Total cost: $'), write(Cost), write('.').


